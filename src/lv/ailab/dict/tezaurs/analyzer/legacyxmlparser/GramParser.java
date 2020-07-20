package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.struct.constants.flags.Keys;
import lv.ailab.dict.struct.constants.structrestrs.Type;
import lv.ailab.dict.tezaurs.analyzer.TPronuncNormalizer;
import lv.ailab.dict.tezaurs.analyzer.gramdata.AltLemmaRules;
import lv.ailab.dict.tezaurs.analyzer.gramdata.DirectRules;
import lv.ailab.dict.tezaurs.analyzer.gramdata.OptHypernRules;
import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AdditionalHeaderRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.EndingRule;
import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.TGram;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.tezaurs.struct.constants.flags.TKeys;
import lv.ailab.dict.tezaurs.struct.constants.flags.TValues;
import lv.ailab.dict.tezaurs.struct.constants.structrestrs.TFrequency;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * g (gramatika) lauka ielasīšana un apstrāde.
 * Gramatikas apstrāde notiek šādos, secīgos etapos:
 * 1) apstrādā gramatikas sākumu atbilstoši gramdata likumu masīvos dotajiem
 * likumiem,
 * 2) sadala pāri palikušo (vēl neapstrādāto) gramatiku pa semikoliem un
 * apstrādā katru iegūto fragmentu atsevišķi,
 * 3) sadala pāri palikušos gramatikas gabalus pa komatiem un apstrādā katru
 * iegūto fragmentu atsevišķi, izmantojot gan šablonus, gan salīdzinot ar
 * saīsinājumu sarakstu,
 * 4) piešķir papildus karodziņus, kas izsecināmi no gramatikas teksta apstrādes
 * laikā iegūtajiem karodziņiem (piemēram, ja vārdam ir bijis gan saīsinājums
 * vēst., gan vietv., tad šajā solī pieliek jaunu karodziņu "Vēsturisks
 * vietvārds", vai arī konkrētu divdabju gadījumā piešķir vispārīgo divdabja
 * karodziņu),
 * 5) piešķir paradigmu, vadoties pēc atpazītajiem karodziņiem, ja tas ir
 * iespējams.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 */
public class GramParser
{
	public final static String ADVERBLIST_LOCATION = "saraksti/adverbs-noDegree.txt";
	protected HashSet<String> noDegreeAdverbs = initAdverbList();
	protected GramParser(){}
	protected static GramParser singleton = new GramParser();
	public static GramParser me()
	{
		return singleton;
	}

	/**
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	public TGram parseGram (Node gramNode, String lemma, ParseType type)
	{
		TGram result = TElementFactory.me().getNewGram();
		result.freeText = Gram.normalizePronunc(
				gramNode.getTextContent(), TPronuncNormalizer.me());
		result.leftovers = null;
		result.flags = TElementFactory.me().getNewFlags();
		result.structRestrictions = TElementFactory.me().getNewStructRestrs();
		result.paradigm = new HashSet<>();
		result.altLemmas = null;
		parseGram(result, lemma, type);
		return result;
	}

	/**
	 * Gramatikas teksta analīzes virsmetode.
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	protected void parseGram(TGram gram, String lemma, ParseType type)
	{
		String correctedGram = correctOCRErrors(gram.freeText);
		correctedGram = normalizeRestrictions(gram.freeText);
		gram.altLemmas = new LinkedList<>();

		// Vispirms apstrādā galotņu šablonus (tie parasti ir gramatikas sākumā).
		correctedGram = processBeginingWithPatterns(gram, correctedGram, lemma);

		String[] subGrams = correctedGram.split("\\s*;\\s*");
		gram.leftovers = new LinkedList<> ();

		// Apstrādā katru ar semikolu atdalīto apakšvirkni.
		for (String subGram : subGrams)
		{
			subGram = processWithNoSemicolonPatterns(gram, subGram, lemma);
			String[] gramElems = subGram.split("\\s*,\\s*");
			LinkedList<String> toDo = new LinkedList<> ();

			// Process each comma-separated substring.
			for (String gramElem : gramElems)
			{
				gramElem = gramElem.trim();
				// Meklē atbilstību zināmajiem saīsinājumiem.
				boolean isFlag = TGram.knownAbbr.translate(gramElem, gram.flags, gram.structRestrictions);
				if (!isFlag)
				{
					// Meklē atbilstību regulārājām izteiksmēm.
					gramElem = processWithNoCommaPatterns(gram, gramElem, lemma);
					// Pārpalikumi, ko neizdevās apstrādāt.
					if (!gramElem.equals(""))
						toDo.add(gramElem);
				}
			}

			gram.leftovers.add(toDo);
		}

		// Skatoties uz visiem atpazītajiem karodziņiem, noteiktu karodziņu
		// pielikšana vai noņemšana.
		postprocessFlags(gram, lemma);

		if (type == ParseType.WORD)
		{
			postprocessLexFlags(gram, lemma);
		}

		// Mēģina izdomāt paradigmu no karodziņiem.
		paradigmFromFlags(gram, lemma);

		gram.cleanupLeftovers();
		// TODO cleanup altLemmas;

		// Pārbauda loģikas kļūdas.
		aftercheck(gram, lemma);
	}

	/**
	 * Gramatikas apstrādes pirmais etaps - sākumdaļas salīdzināšana ar
	 * atbilstošajiem likumiem gramdata pakā un apstrāde.
	 * @param gramText	gramatikas teksts, ko vajag apstrādāt
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 * @return pāri palikusī, neapstrādātā gramatikas daļa
	 */
	private String processBeginingWithPatterns(TGram gram, String gramText, String lemma)
	{
		gramText = gramText.trim();
		int newBegin = -1;

		// Likumi, kuros tiek dots vēl viens lemmas variants - kā pilns vārds
		// vai ar papildus galotņu palīdzību.
		for (AdditionalHeaderRule[] rules : AltLemmaRules.getAll())
		{
			for (AdditionalHeaderRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyDirect(gramText, lemma, gram.paradigm, gram.flags, gram.structRestrictions, gram.altLemmas);
			}
			if (newBegin != -1) break;
		}

		// Likumi, kuros citu lemmu variantu nav.
		for (EndingRule[] rules : OptHypernRules.getAll())
		{
			for (EndingRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyOptHyphens(gramText, lemma, gram.paradigm, gram.flags, gram.structRestrictions);
			}
			if (newBegin != -1) break;
		}
		for (EndingRule[] rules : DirectRules.getAll())
		{
			for (EndingRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyDirect(gramText, lemma, gram.paradigm, gram.flags, gram.structRestrictions);
			}
			if (newBegin != -1) break;
		}

		// === Pēcapstrāde =====================================================
		// Nocērt sākumu, kas atbilst apstrādātajai daļai
		if (newBegin > 0 && newBegin <= gramText.length())
			gramText = gramText.substring(newBegin);
		else if (newBegin > gramText.length())
		{
			System.err.printf(
					"Problēma apstrādājot lemmu \"%s\" un gramatiku \"%s\": iegūtais pārciršanas indekss \"%d\"",
					lemma, gramText, newBegin);
		}
		if (gramText.matches("[.,;].*")) gramText = gramText.substring(1);
		return gramText;
	}

	/**
	 * Gramatikas teksta apstrādes otrais etaps - katras ar semikolu atdalītās
	 * daļas analīze un apstrāde.
	 * @param gramText	gramatikas teksta fragmens, ko vajag apstrādāt - bez
	 *                  semikoliem
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 * @return pāri palikusī, neapstrādātā gramatikas daļa
	 */
	private String processWithNoSemicolonPatterns(TGram gram, String gramText, String lemma)
	{
		gramText = gramText.trim();
		if (gramText.length() < 1) return gramText;
		// Likumi, kas uzdoti kā funkcijas.
		//boolean found;
		boolean found = RulesAsFunctions.matchEtymologyFlag(gramText, gram.flags);
		if (found) return "";
		do
		{
			found = false;
			//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
			int newBegin = RulesAsFunctions.processInParticipleFormFlag(
					gramText, gram.structRestrictions, gram.flags);
			// bim - parasti savienojumā "bim, bam" vai atkārtojumā "bim, bim"
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseOrRepFlag(
					gramText, gram.structRestrictions);
			// aijā - savienojumā "aijā, žūžū"
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, gram.flags, gram.structRestrictions);
			// savienojumā ar slimības izraisītāja mikroorganisma, arī slimības nosaukumu
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, gram.flags, gram.structRestrictions);
			// aizliegt - savienojumā ar "zona", "josla", "teritorija", "ūdeņi" u. tml.
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithQuotFlag(
					gramText, gram.flags, gram.structRestrictions);
			// parasti savienojumā ar verbu "saukt", "dēvēt" formām
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenQuotFlag(
					gramText, gram.flags, gram.structRestrictions);

			if (newBegin > 0)
			{
				gramText = gramText.substring(newBegin);
				if (gramText.startsWith(".") || gramText.startsWith(","))
					gramText = gramText.substring(1);
				gramText = gramText.trim();
				found = true;
			}
		} while (found && gramText.length() > 0);
		return gramText;
	}

	/**
	 * Gramatikas teksta apstrādes trešais etaps - katras ar komatu atdalītās
	 * daļas analīze un apstrāde.
	 * @param gramText	gramatikas teksta fragmens, ko vajag apstrādāt - bez
	 *                  semikoliem un komatiem
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 * @return pāri palikusī, neapstrādātā gramatikas daļa
	 */
	private String processWithNoCommaPatterns(TGram gram, String gramText, String lemma)
	{
		gramText = gramText.trim();
		if (gramText.length() < 1) return gramText;
		boolean found = RulesAsFunctions.matchEtymologyFlag(gramText, gram.flags);
		if (found) return "";
		do
		{
			found = false;
			//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
			// Kur ir āķis, ka šito vēl vajag?
			int newBegin = RulesAsFunctions.processInParticipleFormFlag(
					gramText, gram.structRestrictions, gram.flags);
			//
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, gram.flags, gram.structRestrictions);
			// savienojumā ar ...
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, gram.flags, gram.structRestrictions);
			// aizbļaut - savienojumā ar "ausis"
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithQuotFlag(
					gramText, gram.flags, gram.structRestrictions);
			// parasti savienojumā ar verba "nevarēt" formām
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenQuotFlag(
					gramText, gram.flags, gram.structRestrictions);
			// agrums->agrumā
			//if(newBegin == -1) newBegin = RulesAsFunctions.processUsuallyInCaseFlag(
			//		gramText, flags);
			if (newBegin > 0)
			{
				gramText = gramText.substring(newBegin);
				if (gramText.startsWith("."))
					gramText = gramText.substring(1);
				gramText = gramText.trim();
				found = true;
			}
		} while (found && gramText.length() > 0);

		return gramText;
	}


	/**
	 * Gramatikas apstrādes ceturtais etaps - paradigmu izsecināšana no
	 * karodziņiem.
	 * @param lemma		lemmu skatās gadījumos, kad galotnes/izskaņas palīdz
	 *                  noteikt paradigmu.
	 */
	private void paradigmFromFlags(TGram gram, String lemma)
	{
		HashSet<String> pos = gram.flags.getAll(TKeys.POS);
		if (pos != null)
		{
			if (pos.contains(TValues.ADJECTIVE))
			{
				if (lemma.endsWith("ais")) gram.paradigm.add(30);
				else if (lemma.endsWith("ā")) gram.paradigm.add(40);
				else if (lemma.endsWith("usī")) gram.paradigm.add(41);
				else if (lemma.matches(".*[^aeiouāēīōū]s")) gram.paradigm.add(13);
				else if (lemma.matches(".*[^aeiouāēīōū]š")) gram.paradigm.add(14);
			}

			if (pos.contains(TValues.ADVERB) && noDegreeAdverbs.contains(lemma)) gram.paradigm.add(21);
			else if (pos.contains(TValues.ADVERB)) gram.paradigm.add(51);
			if (pos.contains(TValues.PARTICLE)) gram.paradigm.add(28);
			if (pos.contains(TValues.ADPOSITION)) gram.paradigm.add(26);
			if (pos.contains(TValues.CONJUNCTION)) gram.paradigm.add(27);

			if (pos.contains(TValues.INTERJECTION)) gram.paradigm.add(38);
			if (pos.contains(TValues.ABBREVIATION)) gram.paradigm.add(36);

			if (pos.contains(TValues.PRONOUN)) gram.paradigm.add(25);

			if (pos.contains(TValues.FOREIGN) && !pos.contains(TValues.PIECE_OF_WORD))
				gram.paradigm.add(39);

			// NB! Pašlaik nelokāmie skaitļa vārdi iet Hardcoded paradigmā.
			if (pos.contains(TValues.FRACTIONAL_NUMERAL)
					&& gram.flags.test(TFeatures.NON_INFLECTIVE)) gram.paradigm.add(29); // četrarpus, divarpus
			if (pos.contains(TValues.CARDINAL_NUMERAL)
					&& gram.flags.test(TFeatures.NON_INFLECTIVE)) gram.paradigm.add(29); // deviņsimt

			if (pos.contains(TValues.GEN_ONLY)) gram.paradigm.add(49);
				// Nelokāmie lietvārdi - 12.
			else if (gram.flags.test(TFeatures.NON_INFLECTIVE) && gram.flags.testKey(TKeys.GENDER)
					//&& !gram.flags.testAnyValue(TKeys.USED_IN_FORM, TValues.allCases))
					&& !gram.structRestrictions.testByTypeKey(Type.IN_FORM, TKeys.CASE))
				gram.paradigm.add(12); // bruto

			if (pos.contains(TValues.PIECE_OF_WORD)) gram.paradigm.add(0); //Priedēkļi un salikteņu gabali nav vārdi.
		}
		// Nelokāmie lietvārdi - 12.
		else if (gram.flags.test(TFeatures.NON_INFLECTIVE) && gram.flags.testKey(TKeys.GENDER)
				//&& !gram.flags.testAnyValue(TKeys.USED_IN_FORM, TValues.allCases))
				&& !gram.structRestrictions.testByTypeKey(Type.IN_FORM, TKeys.CASE))
			gram.paradigm.add(12); // video
	}

	/**
	 * Gramatikas analīzes piektais etaps - analizējot no gramatikas teksta
	 * izgūtos karodziņus, pieliek vai noņem (varbūt arī to vēlāk vajadzēs)
	 * karodziņus.
	 */
	private void postprocessFlags(TGram gram, String lemma)
	{
		// Šis tiek darīts tāpēc, ka "vēst." oriģinālajā vārdnīcā nozīmē visu un neko.
		if (gram.flags.test(TFeatures.USAGE_RESTR__HISTORICAL) && gram.flags.test(TFeatures.PERSON_NAME))
			gram.flags.add(TFeatures.DOMAIN__HIST_PERSON);
		if (gram.flags.test(TFeatures.USAGE_RESTR__HISTORICAL) && gram.flags.test(TFeatures.PLACE_NAME))
			gram.flags.add(TFeatures.DOMAIN__HIST_PLACE);

		// Tālāk sekojošais ir shortcut, lai nebūtu dažāda mēroga karodziņiem
		// jānorāda visi, pietiktu ar konkrētāku. Populārākie gadījumi.
		// TODO - papildināt. Te šobrīd noteikti nav viss, tikai tas, ko ātrumā pamanīju likumu failā.
		HashSet<StructRestrs.One> particples =
				gram.structRestrictions.filterByTypeFeature(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_DAMS);
		if (particples != null) for (StructRestrs.One p : particples)
			p.valueFlags.add(TFeatures.MOOD__PARTICIPLE);
		particples = gram.structRestrictions.filterByTypeFeature(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_OT);
		if (particples != null) for (StructRestrs.One p : particples)
			p.valueFlags.add(TFeatures.MOOD__PARTICIPLE);

		particples = gram.structRestrictions.filterByTypeFeature(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_AMS);
		if (particples != null) for (StructRestrs.One p : particples)
			p.valueFlags.add(TFeatures.MOOD__PARTICIPLE);
		particples = gram.structRestrictions.filterByTypeFeature(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_IS);
		if (particples != null) for (StructRestrs.One p : particples)
			p.valueFlags.add(TFeatures.MOOD__PARTICIPLE);
		particples = gram.structRestrictions.filterByTypeFeature(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_OSS);
		if (particples != null) for (StructRestrs.One p : particples)
			p.valueFlags.add(TFeatures.MOOD__PARTICIPLE);
		particples = gram.structRestrictions.filterByTypeFeature(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_TS);
		if (particples != null) for (StructRestrs.One p : particples)
			p.valueFlags.add(TFeatures.MOOD__PARTICIPLE);


		if (gram.flags.test(TFeatures.POS__DIRECT_VERB) ||
				gram.flags.test(TFeatures.POS__REFL_VERB))
			gram.flags.add(TFeatures.POS__VERB);

		//if (gram.flags.test(TFeatures.POS__PARTICIPLE))
		//	gram.flags.add(TFeatures.POS__VERB);
		if (gram.flags.test(TFeatures.POS__REFL_NOUN))
			gram.flags.add(TFeatures.POS__NOUN);
		if (gram.flags.test(TFeatures.POS__REFL_NOUN))
			gram.flags.add(TFeatures.POS__NOUN);

		if (gram.flags.test(TFeatures.POS__REFL_PRONOUN) ||
				gram.flags.test(TFeatures.POS__INTERROG_PRONOUN) ||
				gram.flags.test(TFeatures.POS__INDEF_PRONOUN) ||
				gram.flags.test(TFeatures.POS__DEF_PRONOUN) ||
				gram.flags.test(TFeatures.POS__NEG_PRONOUN) ||
				gram.flags.test(TFeatures.POS__DEM_PRONOUN) ||
				gram.flags.test(TFeatures.POS__PERS_PRONOUN) ||
				gram.flags.test(TFeatures.POS__POSS_PRONOUN) ||
				gram.flags.test(TFeatures.POS__GEN_PRONOUN))
			gram.flags.add(TFeatures.POS__PRONOUN);
		if (gram.flags.test(TFeatures.POS__CARD_NUMERAL) ||
				gram.flags.test(TFeatures.POS__ORD_NUMERAL) ||
				gram.flags.test(TFeatures.POS__FRACT_NUMERAL))
			gram.flags.add(TFeatures.POS__NUMERAL);

		if (gram.flags.test(TFeatures.POS__POSTPOSITION))
			gram.flags.add(TFeatures.POS__ADPOSITION);

		// Labākais, ko no ģenitīveņiem var izgūt. Pēteris kādreiz gribēja gan
		// dzimti, gan skaitli. Varētu būt, ka ieviesīs ģenitīveņiem atsevišķas
		// paradigmas, un tad tiem, kam dzimte vai skaitlis trūks, būs
		// problēmas.
		if (gram.structRestrictions.testByTypeFeature(Type.IN_FORM, TFrequency.UNDISCLOSED, TFeatures.CASE__GENITIVE)
				&& gram.flags.test(TFeatures.NON_INFLECTIVE))
		{
			gram.flags.add(TFeatures.POS__GEN_ONLY);
			if (lemma.endsWith("uguns") || lemma.endsWith("sāls"))
			{
				if (!gram.structRestrictions.testByTypeKey(Type.IN_FORM, TFrequency.UNDISCLOSED, TKeys.NUMBER))
				{
					HashSet<StructRestrs.One> formRestrs = gram.structRestrictions.filterByType(Type.IN_FORM);
					if (formRestrs.size() == 1)
						formRestrs.iterator().next().addFlag(TFeatures.NUMBER__SINGULAR);
					else
					{
						System.out.println("Ģenitīvenim \"" + lemma + "\" nesaprot, kur likt skaitli.");
						gram.structRestrictions.addOne(Type.IN_FORM, TFeatures.NUMBER__SINGULAR);
					}
				} else if (gram.structRestrictions.testByTypeFeature(Type.IN_FORM, TFrequency.UNDISCLOSED, TFeatures.NUMBER__PLURAL))
					System.out.println("Ģenitīvenim \"" + lemma + "\" ir negaidīts daudzskaitlis.");
				System.out.println("Ģenitīvenim \"" + lemma + "\" nevar noteikt dzimti.");
			}
			else if (lemma.endsWith("a") || lemma.endsWith("us")
					|| lemma.endsWith("akmens") || lemma.endsWith("asmens")
					|| lemma.endsWith("zibens")|| lemma.endsWith("mēness")
					|| lemma.endsWith("ūdens") || lemma.endsWith("rudens")) // tēvA, jāņA, medus
			{
				if (!gram.structRestrictions.testByTypeKey(Type.IN_FORM, TFrequency.UNDISCLOSED, TKeys.NUMBER))
				{
					HashSet<StructRestrs.One> formRestrs = gram.structRestrictions.filterByType(Type.IN_FORM);
					if (formRestrs.size() == 1)
						formRestrs.iterator().next().addFlag(TFeatures.NUMBER__SINGULAR);
					else
					{
						System.out.println("Ģenitīvenim \"" + lemma + "\" nesaprot, kur likt skaitli.");
						gram.structRestrictions.addOne(Type.IN_FORM, TFeatures.NUMBER__SINGULAR);
					}
					gram.flags.add(TKeys.GENDER, TValues.MASCULINE);
				}
				else if (gram.structRestrictions.testByTypeFeature(Type.IN_FORM, TFrequency.UNDISCLOSED, TFeatures.NUMBER__PLURAL))
					System.out.println("Ģenitīvenim \"" + lemma + "\" ir negaidīts daudzskaitlis.");
				else gram.flags.add(TKeys.GENDER, TValues.MASCULINE);

			}
			else if (lemma.endsWith("s")) // annAS, eglES, sirdS
			{
				if (!gram.structRestrictions.testByTypeKey(Type.IN_FORM, TFrequency.UNDISCLOSED, TKeys.NUMBER))
				{
					HashSet<StructRestrs.One> formRestrs = gram.structRestrictions.filterByType(Type.IN_FORM);
					if (formRestrs.size() == 1)
						formRestrs.iterator().next().addFlag(TFeatures.NUMBER__SINGULAR);
					else
					{
						System.out.println("Ģenitīvenim \"" + lemma + "\" nesaprot, kur likt skaitli.");
						gram.structRestrictions.addOne(Type.IN_FORM, TFeatures.NUMBER__SINGULAR);
					}
					gram.flags.add(TKeys.GENDER, TValues.FEMININE);
				} else if (gram.structRestrictions.testByTypeFeature(Type.IN_FORM, TFrequency.UNDISCLOSED, TFeatures.NUMBER__PLURAL))
					System.out.println("Ģenitīvenim \"" + lemma + "\" ir negaidīts daudzskaitlis.");
				else gram.flags.add(TKeys.GENDER, TValues.FEMININE);
			}
			else if (lemma.endsWith("u"))
			{
				if (!gram.structRestrictions.testByTypeKey(Type.IN_FORM, TFrequency.UNDISCLOSED, TKeys.NUMBER))
				{
					HashSet<StructRestrs.One> formRestrs = gram.structRestrictions.filterByType(Type.IN_FORM);
					if (formRestrs.size() == 1)
						formRestrs.iterator().next().addFlag(TFeatures.NUMBER__PLURAL);
					else
					{
						System.out.println("Ģenitīvenim \"" + lemma + "\" nesaprot, kur likt skaitli.");
						gram.structRestrictions.addOne(Type.IN_FORM, TFeatures.NUMBER__PLURAL);
					}
				}
				else if (gram.structRestrictions.testByTypeFeature(Type.IN_FORM, TFrequency.UNDISCLOSED, TFeatures.NUMBER__SINGULAR))
				{
					System.out.println("Ģenitīvenim \"" + lemma + "\" ir negaidīts vienskaitlis.");
					System.out.println("Ģenitīvenim ar nestandarta galotni \"" + lemma + "\" nevar noteikt dzimti.");
				}
			}
			else if (!lemma.endsWith("u"))
			{
				System.out.println("Ģenitīvenim \"" + lemma + "\" nevar noteikt skaitli.");
				System.out.println("Ģenitīvenim ar nestandarta galotni \"" + lemma + "\" nevar noteikt dzimti.");
			}
			gram.flags.add(TFeatures.FROZEN);
		}
		//else if (gram.flags.testAnyValue(TKeys.USED_IN_FORM, TValues.allCases) && gram.flags.test(TFeatures.NON_INFLECTIVE))
		else if (gram.structRestrictions.testByTypeKey(Type.IN_FORM, TKeys.CASE)
				&& gram.flags.test(TFeatures.NON_INFLECTIVE))
		{
			if (gram.paradigm.size() > 0)
				System.out.println("Sastingušajai \"" + lemma + "\" formai jau ir paradigmas " +
						(gram.paradigm.stream().map(t -> toString())
								.reduce((t1, t2) -> t1 + ", " + t2).orElse("")) + ".");
			gram.flags.add(TFeatures.FROZEN);
		}

	}

	/**
	 * Gramatikas analīzes piektais etapa apakšsolis, kas specifisks tikai
	 * leksēmu gramatikām - analizējot no gramatikas teksta izgūtos karodziņus,
	 * pieliek vai noņem (varbūt arī to vēlāk vajadzēs) karodziņus.
	 */
	private void postprocessLexFlags(TGram gram, String lemma)
	{
		// Salikteņu daļām, galotnēm un izskaņām.
		if (lemma.startsWith("-") || lemma.endsWith("-"))
			gram.flags.add(TFeatures.POS__PIECE);

		// Izteiksme ir tikai verbiem.
		if (gram.structRestrictions.testByTypeKey(Type.IN_FORM, TKeys.MOOD))
			gram.flags.add(TFeatures.POS__VERB);

	}

	/**
	 * Pēc apstrādes var pārbaudīt karodziņus uz iekšējo konsistenci, vai nav
	 * kaut kas galīgi šķērsu aizgājis.
	 * TODO: papildināt izstrādes gaitā.
	 */
	private void aftercheck(TGram gram, String lemma)
	{
		if (gram.flags.test(TFeatures.POS__VERB) &&
				(gram.paradigm.contains(15) || gram.paradigm.contains(18) ||
						gram.paradigm.contains(16) || gram.paradigm.contains(19) ||
						gram.paradigm.contains(17) || gram.paradigm.contains(20) ||
						gram.paradigm.contains(45) || gram.paradigm.contains(46) ||
						gram.paradigm.contains(29)) &&
				!(gram.flags.test(TFeatures.POS__DIRECT_VERB) ||
						gram.flags.test(TFeatures.POS__REFL_VERB)))
			System.out.println("Darbības vārdam \"" + lemma + "\" nav norādīts tiešs/atgriezenisks!");
		if (gram.paradigm.contains(21) && gram.paradigm.contains(51))
			System.out.println("Apstākļa vārdam \"" + lemma + "\" norādītas divas paradigmas!");
	}


	/**
	 * Priekšapstrāde pirms gramatikas teksta analīzes. Programmas galaversijā
	 * šai metodei jādara pilnīgi neko, jo šādas kļūdas tiek labotas datos.
	 */
	@Deprecated
	private String correctOCRErrors(String gramText)
	{
		//Datu nekonsekvences

		return gramText;
	}

	/**
	 * Priekšapstrāde pirms gramatikas teksta analīzes. Izvāc komatus starp
	 * gramatikas fragmentiem, kam jāveido viens ierobežojums. Tads mazliet
	 * apšaubāms workaround.
	 */
	private String normalizeRestrictions(String gramText)
	{
		gramText = gramText.replaceAll("^parasti 3\\. pers\\., -ē, pag\\. -ēja; trans\\.; parasti saliktajos laikos\\.?($|(?=[,;]))", "parasti 3. pers., -ē, pag. -ēja; parasti saliktajos laikos; trans.");
		gramText = gramText.replaceAll("^parasti 3\\. pers\\., -ē, pag\\. -ēja; intrans\\.; parasti saliktajos laikos\\.?($|(?=[,;]))", "parasti 3. pers., -ē, pag. -ēja; parasti saliktajos laikos; intrans.");
		gramText = gramText.replaceAll("(^|(?![,;] ))parasti 3\\. pers\\. vai divd\\. formā: ", "parasti 3\\. pers\\., parasti divd. formā: ");
		gramText = gramText.replaceAll("(^|(?![,;] ))ar nenot\\. gal(\\.|otni), retāk ar not\\. gal(\\.|otni), pamata pak\\., v\\.($|(?=[,;]))", "ar nenot. gal. vai retāk ar not. gal. pamata pak. v.");
		gramText = gramText.replaceAll("(^|(?![,;] ))ar not\\. gal(\\.|otni), retāk ar nenot\\. gal(\\.|otni), pamata pak\\., v\\.($|(?=[,;]))", "ar not. gal. vai retāk ar nenot. gal. pamata pak. v.");
		gramText = gramText.replaceAll("(^|(?![,;] ))ar not\\. gal(\\.|otni), retāk ar nenot. gal(\\.|otni), pamata pak.($|(?=[,;]))", "ar not. gal. vai retāk ar nenot. gal. pamata pak.");
		gramText = gramText.replaceAll("(^|(?![,;] ))ar not\\. gal(\\.|otni), pārākajā vai vispārākajā pak\\., v\\.($|(?=[,;]))", "ar not. gal. pārākajā vai vispārākajā pak. v.");
		gramText = gramText.replaceAll("(^|(?![,;] ))ar not\\. gal(\\.|otni), pamata pak\\., v.($|(?=[,;]))", "ar not. gal. pamata pak. v.");
		gramText = gramText.replaceAll("(^|(?![,;] ))ar not\\. gal(\\.|otni), pamata pak\\.($|(?=[,;]))", "ar not. gal. pamata pak.");
		gramText = gramText.replaceAll("(^|(?![,;] ))ar nenot\\. gal(\\.|otni), pamata pak\\.($|(?=[,;]))", "ar nenot. gal. pamata pak.");
		gramText = gramText.replaceAll("(^|(?![,;] ))parasti dsk\\.; ar not\\. gal(\\.|otni), v\\.($|(?=[,;]))", "ar not. galotni v.; parasti dsk."); // pelikānveida
		gramText = gramText.replaceAll("(^|(?![,;] ))dsk\\., ar mazo sākumburtu($|(?=[,;]))", "dsk. ar mazo sākumburtu");
		gramText = gramText.replaceAll("(^|(?![,;] ))savienojumā \"līdz ar\"; priev\\. nozīmē ar instr\\.($|(?=[,;]))", "savienojumā \"līdz ar\": priev. nozīmē ar instr.");
		gramText = gramText.replaceAll("(^|(?![,;] ))parasti savienojumā ar skait\\.; priev\\. nozīmē ar dat\\.($|(?=[,;]))", "priev. nozīmē; parasti savienojumā ar skait.; parasti ar dat."); // "pāri" šķirklī ir šāda loģika, tā Laura izskaidroja.
		//gramText = gramText.replaceAll("(^|(?![,;] ))savienojumā ar \"skatīties\", \"raudzīties\" u\\. tml\\.; arī priev\\. nozīmē ar dat\\.($|(?=[,;]))", "savienojumā ar \"skatīties\", \"raudzīties\" u. tml., arī priev. nozīmē ar dat."); // "pāri"
		gramText = gramText.replaceAll("(^|(?![,;] ))priev\\. ar vsk\\. ģen\\. un ak\\., dsk\\. ar ģen\\., arī ar dat\\., divsk\\. nom\\., ak\\.($|(?=[,;]))", "priev. ar vsk. ģen., priev. ar vsk akuz., priev. ar dsk. ģen., retāk priev. ar dsk. dat., retāk priev. ar divsk. nom., retāk priev. ar divsk. akuz.");
		gramText = gramText.replaceAll("(^|(?![,;] ))priev\\. ar ģen\\., retāk dat\\.($|(?=[,;]))", "priev\\. ar ģen\\., retāk priev. ar dat\\.");
		gramText = gramText.replaceAll("(^|(?![,;] ))dsk\\. dat\\., instr\\.($|(?=[,;]))", "dsk. dat., dsk. instr.");
		//gramText = gramText.replaceAll("(^|(?![,;] ))salīdzinājuma konstrukcijā; savienojumā \"tāds kā\"($|(?=[,;]))", "salīdzinājuma konstrukcijā; savienojumā ar \"kā\""); // Ar laiku Laura sola izņemt
		gramText = gramText.replaceAll("(^|(?![,;] ))salīdzinājuma konstrukcijā; savienojumā \"tāds kā\"($|(?=[,;]))", "salīdzinājuma konstrukcijā savienojumā \"tāds kā\""); // Ar laiku Laura sola izņemt
		gramText = gramText.replaceAll("(^|(?![,;] ))ar ģen.; savienojumā ar vienas un tās pašas saknes lietv.($|(?=[,;]))", "savienojumā ar vienas un tās pašas saknes lietv. ģen."); // Ar laiku Laura sola izņemt
		return gramText;
	}

	protected HashSet<String> initAdverbList()
	{
		HashSet<String> advList = new HashSet<>();
		BufferedReader input;
		try
		{
			// Adverb file format - one word (lemma) per line.
			input = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(ADVERBLIST_LOCATION), StandardCharsets.UTF_8));
			String line;
			while ((line = input.readLine()) != null)
				advList.add(line.trim());
			input.close();
		} catch (Exception e)
		{
			System.err.println("Bezpakāpju apstākļa vārdu saraksts netiek lietots.");
		} //TODO - any IO issues ignored
		return advList;
	}

	public enum ParseType
	{
		WORD, PHRASE, SENSE;
	}
}
