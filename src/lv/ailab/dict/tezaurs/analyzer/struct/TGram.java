/*******************************************************************************
 * Copyright 2013-2016 Institute of Mathematics and Computer Science, University of Latvia
 * Author: Lauma Pretkalniņa
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package lv.ailab.dict.tezaurs.analyzer.struct;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.tezaurs.analyzer.gramdata.*;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AdditionalHeaderRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.EndingRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.FormRestrRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.JSONUtils;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
public class TGram extends Gram
{
	/**
	 * Neatpazītas / neizparsētās gramatikas teksta daļas.
	 */
	public LinkedList<LinkedList<String>> leftovers;

	/**
	 * Zināmie saīsinājumi un to atšifrējumi.
	 */
	public static AbbrMap knownAbbr = AbbrMap.getAbbrMap();

	public TGram()
	{
		super();
		leftovers = null;
	}

	/**
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	public TGram(Node gramNode, String lemma)
	{
		freeText = gramNode.getTextContent();
		leftovers = null;
		flags = new Flags();
		paradigm = new HashSet<>();
		altLemmas = null;
		formRestrictions = null;
		parseGram(lemma);
	}
	/**
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	public void set (String gramText, String lemma)
	{
		freeText = gramText;
		leftovers = null;
		flags = new Flags();
		paradigm = new HashSet<>();
		altLemmas = null;
		formRestrictions = null;
		parseGram(lemma);
	}
	
	public int paradigmCount()
	{
		if (paradigm == null) return 0;
		return paradigm.size();
	}
	
	/**
	 * Šitais strādā pareizi tikai tad, ja cleanupLeftovers tiek izsaukts katru
	 * reizi, kad vajag.
	 */
	public boolean hasUnparsedGram()
	{
		return TGram.hasUnparsedGram(this);
	}

	/**
	 * Šitais strādā pareizi tikai tad, ja cleanupLeftovers tiek izsaukts katru
	 * reizi, kad vajag.
	 */
	public static boolean hasUnparsedGram(Gram gram)
	{
		//cleanupLeftovers();		// Kas ir labāk - nagaidīti blakusefekti vai tas, ka nestrādā, ja lieto nepareizi?
		if (gram == null) return false;
		try
		{
			// Interesanti, vai ar refleksiju šis triks būtu ātrāks?
			if (gram.formRestrictions != null)
				for (Header restr : gram.formRestrictions)
					if (((THeader) restr).hasUnparsedGram()) return true;
			// Šito šobrīd it kā nevajag, bet nu drošības pēc:
			if (gram.altLemmas != null)
				for (Header alt : gram.altLemmas)
					if (((THeader) alt).hasUnparsedGram()) return true;

			return !((TGram) gram).leftovers.isEmpty();
		}
		catch (ClassCastException e)
		{
			return false;
		}
	}
	
	/**
	 * Gramatikas teksta analīzes virsmetode.
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	private void parseGram(String lemma)
	{
		String correctedGram = correctOCRErrors(freeText);
		altLemmas = new ArrayList<>();
		formRestrictions = new ArrayList<>();

		// Salikteņu daļām, galotnēm un izskaņām.
		if (lemma.startsWith("-") || lemma.endsWith("-"))
			flags.add(TFeatures.POS__PIECE);
		
		// Vispirms apstrādā galotņu šablonus (tie parasti ir gramatikas sākumā).
		correctedGram = processBeginingWithPatterns(correctedGram, lemma);
		
		String[] subGrams = correctedGram.split("\\s*;\\s*");
		leftovers = new LinkedList<> ();
		
		// Apstrādā katru ar semikolu atdalīto apakšvirkni.
		for (String subGram : subGrams)	
		{
			subGram = processWithNoSemicolonPatterns(subGram, lemma);
			String[] gramElems = subGram.split("\\s*,\\s*");
			LinkedList<String> toDo = new LinkedList<> ();
			
			// Process each comma-separated substring.
			for (String gramElem : gramElems) 
			{
				gramElem = gramElem.trim();
				// Meklē atbilstību zināmajiem saīsinājumiem.
				boolean isFlag = knownAbbr.translate(gramElem, flags);
				if (!isFlag)
				{
					// Meklē atbilstību regulārājām izteiksmēm.
					gramElem = processWithNoCommaPatterns(gramElem, lemma);
					// Pārpalikumi, ko neizdevās apstrādāt.
					if (!gramElem.equals(""))
						toDo.add(gramElem);	
				}
			}

			leftovers.add(toDo);
		}

		// Skatoties uz visiem atpazītajiem karodziņiem, noteiktu karodziņu
		// pielikšana vai noņemšana.
		postprocessFlags(lemma);

		// Mēģina izdomāt paradigmu no karodziņiem.
		paradigmFromFlags(lemma);
		
		cleanupLeftovers();
		// TODO cleanup altLemmas;

		// Pārbauda loģikas kļūdas.
		aftercheck(lemma);
	}
	
	/**
	 * Gramatikas apstrādes pirmais etaps - sākumdaļas salīdzināšana ar
	 * atbilstošajiem likumiem gramdata pakā un apstrāde.
	 * @param gramText	gramatikas teksts, ko vajag apstrādāt
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 * @return pāri palikusī, neapstrādātā gramatikas daļa
	 */
	private String processBeginingWithPatterns(String gramText, String lemma)
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
				newBegin = r.applyDirect(gramText, lemma, paradigm, flags, altLemmas);
			}
			if (newBegin != -1) break;
		}

		// Likumi, kuros citu lemmu variantu nav.
		for (EndingRule[] rules : OptHypernRules.getAll())
		{
			for (EndingRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyOptHyphens(gramText, lemma, paradigm, flags);
			}
			if (newBegin != -1) break;
		}
		for (EndingRule[] rules : DirectRules.getAllSafe())
		{
			for (EndingRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyDirect(gramText, lemma, paradigm, flags);
			}
			if (newBegin != -1) break;
		}

		// === Bīstamie likumi =================================================
		// Likumi, kas ir prefiksi citiem likumiem
		for (EndingRule[] rules : DirectRules.getAllDangeros())
		{
			for (EndingRule r : rules)
			{
				if (newBegin != -1) break;
				newBegin = r.applyDirect(gramText, lemma, paradigm, flags);
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
	private String processWithNoSemicolonPatterns(String gramText, String lemma)
	{
		gramText = gramText.trim();
		if (gramText.length() < 1) return gramText;
		// Likumi, kas uzdoti kā funkcijas.
		boolean found;
		do
		{
			found = false;
			//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
			int newBegin = RulesAsFunctions.processInParticipleFormFlag(
					gramText, flags, formRestrictions);
			// aijā - savienojumā "aijā, žūžū"
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, flags);
			// savienojumā ar slimības izraisītāja mikroorganisma, arī slimības nosaukumu
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, flags);
			// aizliegt - savienojumā ar "zona", "josla", "teritorija", "ūdeņi" u. tml.
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithQuotFlag(
					gramText, flags);
			// parasti savienojumā ar verbu "saukt", "dēvēt" formām
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenQuotFlag(
					gramText, flags);

			if (newBegin > 0)
			{
				gramText = gramText.substring(newBegin);
				if (gramText.startsWith(".") || gramText.startsWith(","))
					gramText = gramText.substring(1);
				gramText = gramText.trim();
				found = true;
			}
		} while (found && gramText.length() > 0);

		// Ierobežojošo formu likumi.
		int newBegin = -1;
		for (FormRestrRule[] rules : FormRestrRules.getAll())
			if (newBegin == -1) for (FormRestrRule r : rules)
			{
				newBegin = r.applyDirect(gramText, lemma, paradigm, flags, formRestrictions);
				if (newBegin > -1) break;
			}
		// TODO: aiz šitiem likumiem patiesībā vajadzētu palikt tukšam līdz pašām, pašām beigām.
		if (newBegin <= 0) return gramText; // Nebija atbilsmes
		// Atbilsme bija.
		gramText = gramText.substring(newBegin);
		if (gramText.startsWith(".") || gramText.startsWith(","))
			gramText = gramText.substring(1);
		gramText = gramText.trim();

		if (!gramText.equals("")) // Šito būtu jānodrošina jau likumam :)
		{
			LinkedList<String> tmp = new LinkedList<>();
			tmp.add(gramText);
			leftovers.add(tmp);
		}
		return "";
	}

	/**
	 * Gramatikas teksta apstrādes trešais etaps - katras ar komatu atdalītās
	 * daļas analīze un apstrāde.
	 * @param gramText	gramatikas teksta fragmens, ko vajag apstrādāt - bez
	 *                  semikoliem un komatiem
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 * @return pāri palikusī, neapstrādātā gramatikas daļa
	 */
	private String processWithNoCommaPatterns(String gramText, String lemma)
	{
		gramText = gramText.trim();
		if (gramText.length() < 1) return gramText;
		boolean found = false;
		do
		{
			found = false;
			//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
			// Kur ir āķis, ka šito vēl vajag?
			int newBegin = RulesAsFunctions.processInParticipleFormFlag(
					gramText, flags, formRestrictions);
			//
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, flags);
			// savienojumā ar ...
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, flags);
			// aizbļaut - savienojumā ar "ausis"
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithQuotFlag(
					gramText, flags);
			// parasti savienojumā ar verba "nevarēt" formām
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenQuotFlag(
					gramText, flags);
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
	private void paradigmFromFlags(String lemma)
	{
		HashSet<String> pos = flags.getAll(TKeys.POS);
		if (pos != null)
		{
			if (pos.contains(TValues.ADJECTIVE))
			{
				if (lemma.endsWith("ais")) paradigm.add(30);
				else if (lemma.endsWith("ā")) paradigm.add(40);
				else if (lemma.endsWith("usī")) paradigm.add(41);
				else if (lemma.matches(".*[^aeiouāēīōū]s")) paradigm.add(13);
				else if (lemma.matches(".*[^aeiouāēīōū]š")) paradigm.add(14);
			}

			if (pos.contains(TValues.ADVERB)) paradigm.add(21);
			if (pos.contains(TValues.PARTICLE)) paradigm.add(28);
			if (pos.contains(TValues.ADPOSITION)) paradigm.add(26);
			if (pos.contains(TValues.CONJUNCTION)) paradigm.add(27);

			if (pos.contains(TValues.INTERJECTION)) paradigm.add(38);
			if (pos.contains(TValues.ABBREVIATION)) paradigm.add(36);

			if (pos.contains(TValues.PRONOUN)) paradigm.add(25);

			if (pos.contains(TValues.FOREIGN)) paradigm.add(39);

			// NB! Pašlaik nelokāmie skaitļa vārdi iet Hardcoded paradigmā.
			if (pos.contains(TValues.FRACTIONAL_NUMERAL)
					&& flags.test(TFeatures.NON_INFLECTIVE)) paradigm.add(29); // četrarpus, divarpus
			if (pos.contains(TValues.CARDINAL_NUMERAL)
					&& flags.test(TFeatures.NON_INFLECTIVE)) paradigm.add(29); // deviņsimt

			// Labākais, ko no ģenitīveņiem var izgūt. Pēteris grib gan dzimti,
			// gan skaitli. Varētu būt, ka ieviesīs ģenitīveņiem atsevišķas
			// paradigmas, un tad tiem, kam dzimte vai skaitlis trūks, būs
			// problēmas.
			if (pos.contains(TValues.GEN_ONLY))
			{
				paradigm.add(0);
				if (!flags.testKey(TKeys.GENDER) || !flags.testKey(TKeys.NUMBER))
					flags.add(TFeatures.UNCLEAR_PARADIGM);
			}

			if (pos.contains(TValues.PIECE_OF_WORD)) paradigm.add(0); //Priedēkļi un salikteņu gabali nav vārdi.
		}


	}

	/**
	 * Gramatikas analīzes piektais etaps - analizējot no gramatikas teksta
	 * izgūtos karodziņus, pieliek vai noņem (varbūt arī to vēlāk vajadzēs)
	 * karodziņus.
	 */
	private void postprocessFlags(String lemma)
	{
		// Šis tiek darīts tāpēc, ka "vēst." oriģinālajā vārdnīcā nozīmē visu un neko.
		if (flags.test(TFeatures.USAGE_RESTR__HISTORICAL) && flags.test(TFeatures.PERSON_NAME))
			flags.add(TFeatures.DOMAIN__HIST_PERSON);
		if (flags.test(TFeatures.USAGE_RESTR__HISTORICAL) && flags.test(TFeatures.PLACE_NAME))
			flags.add(TFeatures.DOMAIN__HIST_PLACE);

		// Tālāk sekojošais ir shortcut, lai nebūtu dažāda mēroga karodziņiem
		// jānorāda visi, pietiktu ar konkrētāku. Populārākie gadījumi.
		// TODO - papildināt. Te šobrīd noteikti nav viss, tikai tas, ko ātrumā pamanīju likumu failā.
		if (flags.test(TFeatures.POS__PARTICIPLE_AMS) ||
				flags.test(TFeatures.POS__PARTICIPLE_DAMS) ||
				flags.test(TFeatures.POS__PARTICIPLE_IS) ||
				flags.test(TFeatures.POS__PARTICIPLE_OSS) ||
				flags.test(TFeatures.POS__PARTICIPLE_OT) ||
				flags.test(TFeatures.POS__PARTICIPLE_TS))
			flags.add(TFeatures.POS__PARTICIPLE);
		if (flags.test(TFeatures.POS__DIRECT_VERB) ||
				flags.test(TFeatures.POS__REFL_VERB))
			flags.add(TFeatures.POS__VERB);

		if (flags.test(TFeatures.POS__PARTICIPLE))
			flags.add(TFeatures.POS__VERB);
		if (flags.test(TFeatures.POS__REFL_NOUN))
			flags.add(TFeatures.POS__NOUN);
		if (flags.test(TFeatures.POS__REFL_NOUN))
			flags.add(TFeatures.POS__NOUN);

		if (flags.test(TFeatures.POS__REFL_PRONOUN) ||
				flags.test(TFeatures.POS__INTERROG_PRONOUN) ||
				flags.test(TFeatures.POS__INDEF_PRONOUN) ||
				flags.test(TFeatures.POS__DEF_PRONOUN) ||
				flags.test(TFeatures.POS__NEG_PRONOUN) ||
				flags.test(TFeatures.POS__DEM_PRONOUN) ||
				flags.test(TFeatures.POS__PERS_PRONOUN) ||
				flags.test(TFeatures.POS__POSS_PRONOUN) ||
				flags.test(TFeatures.POS__GEN_PRONOUN))
			flags.add(TFeatures.POS__PRONOUN);
		if (flags.test(TFeatures.POS__CARD_NUMERAL) ||
				flags.test(TFeatures.POS__ORD_NUMERAL) ||
				flags.test(TFeatures.POS__FRACT_NUMERAL))
			flags.add(TFeatures.POS__NUMERAL);

		if (flags.test(TFeatures.POS__POSTPOSITION))
			flags.add(TFeatures.POS__ADPOSITION);

		if (flags.test(TKeys.CASE, TValues.GENITIVE) && flags.test(TFeatures.NON_INFLECTIVE))
		{
			flags.add(TFeatures.POS__GEN_ONLY);
			if (lemma.endsWith("u"))
				flags.add(TKeys.NUMBER, TValues.PLURAL);
			else if (lemma.endsWith("a") || lemma.endsWith("s"))
				flags.add(TKeys.NUMBER, TValues.SINGULAR);
			else System.out.println("Ģenitīvenim \"" + lemma + "\" nevar noteikt skaitli.");
			if (lemma.endsWith("a") || lemma.endsWith("us")) // tēvA, jāņA, medus
				flags.add(TKeys.GENDER, TValues.MASCULINE);
			else if (lemma.endsWith("s")) // annAS, eglES, sirdS
				flags.add(TKeys.GENDER, TValues.FEMININE);
			else if (!lemma.endsWith("u"))
				System.out.println("Ģenitīvenim ar nestandarta galotni \"" + lemma + "\" nevar noteikt dzimti.");
			flags.add(TFeatures.FROZEN);
		}
		else if (flags.testKey(TKeys.CASE) && flags.test(TFeatures.NON_INFLECTIVE))
		{
			if (paradigm.size() > 0)
				System.out.println("Sastingušajai \"" + lemma + "\" formai jau ir paradigmas " +
						(paradigm.stream().map(t -> toString())
								.reduce((t1, t2) -> t1 + ", " + t2).orElse("")) + ".");
			flags.add(TFeatures.FROZEN);
		}

	}

	/**
	 * Pēc apstrādes var pārbaudīt karodziņus uz iekšējo konsistenci, vai nav
	 * kaut kas galīgi šķērsu aizgājis.
	 * TODO: papildināt izstrādes gaitā.
	 */
	private void aftercheck(String lemma)
	{
		if (flags.test(TFeatures.POS__VERB) &&
				(paradigm.contains(15) || paradigm.contains(18) ||
						paradigm.contains(16) || paradigm.contains(19) ||
						paradigm.contains(17) || paradigm.contains(20) ||
						paradigm.contains(29)) &&
				!(flags.test(TFeatures.POS__DIRECT_VERB) ||
					flags.test(TFeatures.POS__REFL_VERB)))
			System.out.println("Darbības vārdam \"" + lemma + "\" nav norādīts tiešs/atgriezenisks!");
	}

	/**
	 * Šo jāizsauc katru reizi, kad kaut ko izņem no leftovers.
	 */
	public void cleanupLeftovers()
	{
		for (int i = leftovers.size() - 1; i >= 0; i--)
			if (leftovers.get(i).isEmpty()) leftovers.remove(i);
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
	 * Gramatikas struktūras JSON reprezentācija, kas iekļauj arī sākotnējo
	 * gramatikas tekstu.
	 */
	@Override
	public String toJSON()
	{
		if (leftovers != null && leftovers.size() > 0)
		{
			StringBuilder additional = new StringBuilder();
			additional.append("\"Leftovers\":[");
			Iterator<LinkedList<String>> it = leftovers.iterator();
			while (it.hasNext())
			{
				LinkedList<String> next = it.next();
				if (!next.isEmpty())
				{
					additional.append(JSONUtils.simplesToJSON(next));
					if (it.hasNext()) additional.append(", ");
				}
			}
			additional.append("]");
			return toJSON(null, additional.toString());
		}
		else return toJSON(null, null);
	}
}