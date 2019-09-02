package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Gram;
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
import org.w3c.dom.Node;

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
	protected GramParser(){}
	protected static GramParser singleton = new GramParser();
	public static GramParser me()
	{
		return singleton;
	}

	/**
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	public TGram parseGram (Node gramNode, String lemma)
	{
		TGram result = TElementFactory.me().getNewGram();
		result.freeText = Gram.normalizePronunc(
				gramNode.getTextContent(), TPronuncNormalizer.me());
		result.leftovers = null;
		result.flags = TElementFactory.me().getNewFlags();
		result.paradigm = new HashSet<>();
		result.altLemmas = null;
		parseGram(result, lemma);
		return result;
	}

	/**
	 * Gramatikas teksta analīzes virsmetode.
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	protected void parseGram(TGram gram, String lemma)
	{
		String correctedGram = correctOCRErrors(gram.freeText);
		gram.altLemmas = new LinkedList<>();

		// Salikteņu daļām, galotnēm un izskaņām.
		if (lemma.startsWith("-") || lemma.endsWith("-"))
			gram.flags.add(TFeatures.POS__PIECE);

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
				boolean isFlag = TGram.knownAbbr.translate(gramElem, gram.flags);
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
				newBegin = r.applyDirect(gramText, lemma, gram.paradigm, gram.flags, gram.altLemmas);
			}
			if (newBegin != -1) break;
		}

		// Likumi, kuros citu lemmu variantu nav.
		for (EndingRule[] rules : OptHypernRules.getAll())
		{
			for (EndingRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyOptHyphens(gramText, lemma, gram.paradigm, gram.flags);
			}
			if (newBegin != -1) break;
		}
		for (EndingRule[] rules : DirectRules.getAll())
		{
			for (EndingRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyDirect(gramText, lemma, gram.paradigm, gram.flags);
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
					gramText, gram.flags);
			// bim - parasti savienojumā "bim, bam" vai atkārtojumā "bim, bim"
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseOrRepFlag(
					gramText, gram.flags);
			// aijā - savienojumā "aijā, žūžū"
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, gram.flags);
			// savienojumā ar slimības izraisītāja mikroorganisma, arī slimības nosaukumu
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, gram.flags);
			// aizliegt - savienojumā ar "zona", "josla", "teritorija", "ūdeņi" u. tml.
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithQuotFlag(
					gramText, gram.flags);
			// parasti savienojumā ar verbu "saukt", "dēvēt" formām
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenQuotFlag(
					gramText, gram.flags);

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
					gramText, gram.flags);
			//
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, gram.flags);
			// savienojumā ar ...
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, gram.flags);
			// aizbļaut - savienojumā ar "ausis"
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithQuotFlag(
					gramText, gram.flags);
			// parasti savienojumā ar verba "nevarēt" formām
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenQuotFlag(
					gramText, gram.flags);
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

			if (pos.contains(TValues.ADVERB)) gram.paradigm.add(21);
			if (pos.contains(TValues.PARTICLE)) gram.paradigm.add(28);
			if (pos.contains(TValues.ADPOSITION)) gram.paradigm.add(26);
			if (pos.contains(TValues.CONJUNCTION)) gram.paradigm.add(27);

			if (pos.contains(TValues.INTERJECTION)) gram.paradigm.add(38);
			if (pos.contains(TValues.ABBREVIATION)) gram.paradigm.add(36);

			if (pos.contains(TValues.PRONOUN)) gram.paradigm.add(25);

			if (pos.contains(TValues.FOREIGN)) gram.paradigm.add(39);

			// NB! Pašlaik nelokāmie skaitļa vārdi iet Hardcoded paradigmā.
			if (pos.contains(TValues.FRACTIONAL_NUMERAL)
					&& gram.flags.test(TFeatures.NON_INFLECTIVE)) gram.paradigm.add(29); // četrarpus, divarpus
			if (pos.contains(TValues.CARDINAL_NUMERAL)
					&& gram.flags.test(TFeatures.NON_INFLECTIVE)) gram.paradigm.add(29); // deviņsimt

			if (pos.contains(TValues.GEN_ONLY)) gram.paradigm.add(49);
				// Nelokāmie lietvārdi - 12.
			else if (gram.flags.test(TFeatures.NON_INFLECTIVE) && gram.flags.testKey(TKeys.GENDER)
					&& !gram.flags.testAnyValue(TKeys.USED_IN_FORM, TValues.allCases)) gram.paradigm.add(12); // bruto

			if (pos.contains(TValues.PIECE_OF_WORD)) gram.paradigm.add(0); //Priedēkļi un salikteņu gabali nav vārdi.
		}
		// Nelokāmie lietvārdi - 12.
		else if (gram.flags.test(TFeatures.NON_INFLECTIVE) && gram.flags.testKey(TKeys.GENDER)
				&& !gram.flags.testAnyValue(TKeys.USED_IN_FORM, TValues.allCases)) gram.paradigm.add(12); // video
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
		if (gram.flags.test(TFeatures.POS__PARTICIPLE_AMS) ||
				gram.flags.test(TFeatures.POS__PARTICIPLE_DAMS) ||
				gram.flags.test(TFeatures.POS__PARTICIPLE_IS) ||
				gram.flags.test(TFeatures.POS__PARTICIPLE_OSS) ||
				gram.flags.test(TFeatures.POS__PARTICIPLE_OT) ||
				gram.flags.test(TFeatures.POS__PARTICIPLE_TS))
			gram.flags.add(TFeatures.POS__PARTICIPLE);
		if (gram.flags.test(TFeatures.POS__DIRECT_VERB) ||
				gram.flags.test(TFeatures.POS__REFL_VERB))
			gram.flags.add(TFeatures.POS__VERB);

		if (gram.flags.test(TFeatures.POS__PARTICIPLE))
			gram.flags.add(TFeatures.POS__VERB);
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

		// Labākais, ko no ģenitīveņiem var izgūt. Pēteris grib gan dzimti,
		// gan skaitli. Varētu būt, ka ieviesīs ģenitīveņiem atsevišķas
		// paradigmas, un tad tiem, kam dzimte vai skaitlis trūks, būs
		// problēmas.
		if (gram.flags.test(TKeys.USED_IN_FORM, TValues.GENITIVE) && gram.flags.test(TFeatures.NON_INFLECTIVE))
		{
			gram.flags.add(TFeatures.POS__GEN_ONLY);
			if (lemma.endsWith("u"))
				gram.flags.add(TKeys.USED_IN_FORM, TValues.PLURAL);
			else if (lemma.endsWith("a") || lemma.endsWith("s"))
				gram.flags.add(TKeys.USED_IN_FORM, TValues.SINGULAR);
			else System.out.println("Ģenitīvenim \"" + lemma + "\" nevar noteikt skaitli.");
			if (lemma.endsWith("a") || lemma.endsWith("us")) // tēvA, jāņA, medus
				gram.flags.add(TKeys.GENDER, TValues.MASCULINE);
			else if (lemma.endsWith("s")) // annAS, eglES, sirdS
				gram.flags.add(TKeys.GENDER, TValues.FEMININE);
			else if (!lemma.endsWith("u"))
				System.out.println("Ģenitīvenim ar nestandarta galotni \"" + lemma + "\" nevar noteikt dzimti.");
			gram.flags.add(TFeatures.FROZEN);
		}
		else if (gram.flags.testAnyValue(TKeys.USED_IN_FORM, TValues.allCases) && gram.flags.test(TFeatures.NON_INFLECTIVE))
		{
			if (gram.paradigm.size() > 0)
				System.out.println("Sastingušajai \"" + lemma + "\" formai jau ir paradigmas " +
						(gram.paradigm.stream().map(t -> toString())
								.reduce((t1, t2) -> t1 + ", " + t2).orElse("")) + ".");
			gram.flags.add(TFeatures.FROZEN);
		}

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


}