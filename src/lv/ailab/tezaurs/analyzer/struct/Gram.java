package lv.ailab.tezaurs.analyzer.struct;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.analyzer.gramdata.*;
import org.w3c.dom.Node;
import org.json.simple.JSONObject;

import lv.ailab.tezaurs.utils.HasToJSON;
import lv.ailab.tezaurs.utils.MappingSet;
import lv.ailab.tezaurs.utils.Tuple;
import lv.ailab.tezaurs.utils.JSONUtils;
import lv.ailab.tezaurs.analyzer.gramlogic.*;

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
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
 */
public class Gram  implements HasToJSON
{

	/**
	 * Gramatikas teksts kāds tas ir vārdnīcā.
	 */
	public String orig;
	/**
	 * No gramatikas izgūtie karodziņi.
	 */
	public Flags flags;
	/**
	 * Neatpazītas / neizparsētās gramatikas teksta daļas.
	 */
	public LinkedList<LinkedList<String>> leftovers;
	/**
	 * No šīs gramatikas izsecinātās paradigmas.
	 */
	public HashSet<Integer> paradigm;
	/**
	 * Struktūra, kurā tiek savākta papildus informāciju par citiem šķirkļu
	 * vārdiem / pamatformām (kodā daudzviet saukti par alternatīvajām lemmām),
	 * ja gramatika tādu satur.
	 * Kartējums no paradigmas uz lemmas/karodziņu kopas pārīšiem. Karodziņu
	 * kopa satur vienīgi tos karodziņus, kas "alternatīvajai lemmai" atšķiras
	 * no pamata lemmas.
	 */
	public MappingSet<Integer, Tuple<Lemma, Flags>> altLemmas;

	/**
	 * Zināmie saīsinājumi un to atšifrējumi.
	 */
	public static AbbrMap knownAbbr = AbbrMap.getAbbrMap();

	public Gram ()
	{
		orig = null;
		flags = null;
		leftovers = null;
		paradigm = null;
		altLemmas = null;
	}
	/**
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	public Gram (Node gramNode, String lemma)
	{
		orig = gramNode.getTextContent();
		leftovers = null;
		flags = new Flags();
		paradigm = new HashSet<>();
		altLemmas = null;
		parseGram(lemma);
	}
	/**
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	public void set (String gramText, String lemma)
	{
		orig = gramText;
		leftovers = null;
		flags = new Flags();
		paradigm = new HashSet<>();
		altLemmas = null;
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
		//cleanupLeftovers();		// Kas ir labāk - nagaidīti blakusefekti vai tas, ka nestrādā, ja lieto nepareizi?
		return !leftovers.isEmpty();
	}
	
	/**
	 * Gramatikas teksta analīzes virsmetode.
	 * @param lemma		lemmu skatās, lai labāk saprastu apstrādājamo gramatiku
	 */
	private void parseGram(String lemma)
	{
		String correctedGram = correctOCRErrors(orig);
		altLemmas = new MappingSet<>();

		// Salikteņu daļām, galotnēm un izskaņām.
		if (lemma.startsWith("-") || lemma.endsWith("-"))
			flags.add(Features.POS__PART);
		
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
		postprocessFlags();

		// Mēģina izdomāt paradigmu no karodziņiem.
		paradigmFromFlags(lemma);
		
		cleanupLeftovers();
		// TODO cleanup altLemmas;
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
		for (AltLemmaRule r : AltLemmaRules.pluralToSingular)
		{
			if (newBegin != -1) break;
			newBegin = r.apply(gramText, lemma, paradigm, flags, altLemmas);
		}
		for (AltLemmaRule r : AltLemmaRules.mascToFem)
		{
			if (newBegin != -1) break;
			newBegin = r.apply(gramText, lemma, paradigm, flags, altLemmas);
		}

		// Likumi, kuros citu lemmu variantu nav.
		// Darbības vārdi.
		for (Rule s : DirectRules.directMultiConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : DirectRules.directFirstConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : DirectRules.directSecondConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : DirectRules.directThirdConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : DirectRules.reflMultiConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : DirectRules.reflFirstConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : DirectRules.reflSecondConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : DirectRules.reflThirdConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}

		for (Rule s : OptHypernRules.directMultiConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.directFirstConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.directSecondConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.directThirdConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.reflMultiConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.reflFirstConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.reflThirdConjVerb)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}

		// Kaut kādi sarežģītie likumi.
		for (Rule s : DirectRules.other)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.other)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}

		// "-??a, v.", "-??u, s.", "-??u, v."
		// "-es, dsk. ģen. -??u, s."
		// Paradigmas: 3
		for (Rule s : DirectRules.secondDeclNounRulesDirect)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.secondDeclNoun)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		// Paradigmas: 9
		for (Rule s : DirectRules.fifthDeclNoun)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : OptHypernRules.fifthDeclNoun)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		// Paradigmas: 1, 2, 3, 5, 9
		for (Rule s : DirectRules.nounMultiDecl)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}

		// === Bīstamie likumi =================================================
		// Likumi, kas ir prefiksi citiem likumiem
		for (Rule s : DirectRules.dangerous)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
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
		boolean found;
		do
		{
			found = false;
			//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
			int newBegin = RulesAsFunctions.processInParticipleFormFlag(
					gramText, flags, altLemmas);
			// aijā - savienojumā "aijā, žūžū"
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, flags);
			// savienojumā ar slimības izraisītāja mikroorganisma, arī slimības nosaukumu
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, flags);

			if (newBegin > 0)
			{
				gramText = gramText.substring(newBegin);
				if (gramText.startsWith(".") || gramText.startsWith(","))
					gramText = gramText.substring(1);
				gramText = gramText.trim();
				found = true;
			}
			//System.out.println(lemma + " " + newBegin + " " + gramText);
		} while (found && gramText.length() > 0);

		return gramText;
	}



	/**
	 * Gramatikas teksta apstrādes trešais etaps - katras ar semikolu atdalītās
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
			int newBegin = RulesAsFunctions.processInParticipleFormFlag(
					gramText, flags, altLemmas);
			//
			if (newBegin == -1) newBegin = RulesAsFunctions.processInPhraseFlag(
					gramText, flags);
			// savienojumā ar ...
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithGenFlag(
					gramText, flags);
			// aizbļaut - savienojumā ar "ausis"
			if(newBegin == -1) newBegin = RulesAsFunctions.processTogetherWithQuotFlag(
					gramText, flags);
			// agrums->agrumā
			if(newBegin == -1) newBegin = RulesAsFunctions.processUsuallyInCaseFlag(
					gramText, flags);
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
		HashSet<String> pos = flags.getAll(Keys.POS);
		if (pos != null)
		{
			if (pos.contains(Values.ADJECTIVE.s))
			{
				if (lemma.endsWith("ais") || lemma.endsWith("ā")) paradigm.add(30);
				else if (lemma.matches(".*[^aeiouāēīōū]s")) paradigm.add(13);
				else if (lemma.matches(".*[^aeiouāēīōū]š")) paradigm.add(14);
			}

			if (pos.contains(Values.ADVERB.s)) paradigm.add(21);
			if (pos.contains(Values.PARTICLE.s)) paradigm.add(28);
			if (pos.contains(Values.PREPOSITION.s)) paradigm.add(26);
			if (pos.contains(Values.CONJUNCTION.s)) paradigm.add(27);

			if (pos.contains(Values.INTERJECTION.s)) paradigm.add(29); // Hardcoded
			if (pos.contains(Values.ABBREVIATION.s)) paradigm.add(29); // Hardcoded

			if (pos.contains(Values.PRONOUN.s)) paradigm.add(25);

			if (pos.contains(Values.FOREIGN.s)) paradigm.add(29);

			if (pos.contains(Values.PART_OF_WORD.s)) paradigm.add(0); //Priedēkļi un salikteņu gabali nav vārdi.
		}

		if (flags.testKey(Keys.CASE) && flags.test(Features.NON_INFLECTIVE))
		{
			if (paradigm.size() > 0)
				System.out.println("Sastingušajai \"" + lemma + "\" formai jau ir paradigmas " +
						(paradigm.stream().map(t -> toString())
								.reduce((t1, t2) -> t1 + ", " + t2).orElse("")) + ".");
			paradigm.add(29); // Sastingusi forma.
		}
	}

	/**
	 * Gramatikas analīzes piektais etaps - analizējot no gramatikas teksta
	 * izgūtos karodziņus, pieliek vai noņem (varbūt arī to vēlāk vajadzēs)
	 * karodziņus.
	 */
	private void postprocessFlags()
	{
		// Šis tiek darīts tāpēc, ka "vēst." oriģinālajā vārdnīcā nozīmē visu un neko.
		if (flags.test(Features.USAGE_RESTR__HISTORICAL) && flags.test(Features.PERSON_NAME))
			flags.add(Features.DOMAIN__HIST_PERSON);
		if (flags.test(Features.USAGE_RESTR__HISTORICAL) && flags.test(Features.PLACE_NAME))
			flags.add(Features.DOMAIN__HIST_PLACE);

		// Tālāk sekojošais ir shortcut, lai nebūtu dažāda mēroga karodziņiem
		// jānorāda visi, pietiktu ar konkrētāku. Populārākie gadījumi.
		// TODO - papildināt. Te šobrīd noteikti nav viss, tikai tas, ko ātrumā pamanīju likumu failā.
		if (flags.test(Features.POS__PARTICIPLE_AMS) ||
				flags.test(Features.POS__PARTICIPLE_DAMS) ||
				flags.test(Features.POS__PARTICIPLE_IS) ||
				flags.test(Features.POS__PARTICIPLE_OSS) ||
				flags.test(Features.POS__PARTICIPLE_OT) ||
				flags.test(Features.POS__PARTICIPLE_TS))
			flags.add(Features.POS__PARTICIPLE);

		if (flags.test(Features.POS__PARTICIPLE))
			flags.add(Features.POS__VERB);
		if (flags.test(Features.POS__REFL_NOUN))
			flags.add(Features.POS__NOUN);
		if (flags.test(Features.POS__REFL_NOUN))
			flags.add(Features.POS__NOUN);

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
	public String toJSON()
	{
		return toJSON(true);
	}

	/**
	 * Izveido JSON reprezentāciju.
	 * Ātruma problēmu gadījumā, iespējams, jāpāriet uz StringBuilder
	 * atgriešanu.
	 * @param printOrig vai izdrukā iekļaut oriģinālo tekstu?
	 */
	public String toJSON (boolean printOrig)
	{
		StringBuilder res = new StringBuilder();
		
		res.append("\"Gram\":{");
		boolean hasPrev = false;
		
		if (paradigm != null && !paradigm.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Paradigm\":");
			res.append(JSONUtils.simplesToJSON(paradigm));
			hasPrev = true;
		}
		
		if (altLemmas != null && !altLemmas.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"AltLemmas\":{");
			Iterator<Integer> it = altLemmas.keySet().iterator();
			while (it.hasNext())
			{
				Integer next = it.next();
				if (!altLemmas.getAll(next).isEmpty())
				{
					res.append("\"");
					res.append(JSONObject.escape(next.toString()));
					res.append("\":[");
					Iterator<Tuple<Lemma, Flags>> flagIt = altLemmas.getAll(next).iterator();
					while (flagIt.hasNext())
					{
						Tuple<Lemma, Flags> alt = flagIt.next();
						res.append("{");
						res.append(alt.first.toJSON());
						if (alt.second != null && !alt.second.pairings.isEmpty())
						{
							res.append(", ");
							res.append("\"Flags\":");
							res.append(JSONUtils.mappingSetToJSON(alt.second.pairings));
						}
						res.append("}");
						if (flagIt.hasNext()) res.append(", ");
					}
					
					res.append("]");
					if (it.hasNext()) res.append(", ");
				}
			}
			res.append("}");
			hasPrev = true;
		}
		

		if (flags != null && !flags.pairings.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Flags\":");
			res.append(JSONUtils.mappingSetToJSON(flags.pairings));
			hasPrev = true;
		}
		
		if (leftovers != null && leftovers.size() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Leftovers\":[");
			
			Iterator<LinkedList<String>> it = leftovers.iterator();
			while (it.hasNext())
			{
				LinkedList<String> next = it.next();
				if (!next.isEmpty())
				{
					res.append(JSONUtils.simplesToJSON(next));
					if (it.hasNext()) res.append(", ");
				}
			}
			res.append("]");
			hasPrev = true;
		}
		
		if (printOrig && orig != null && orig.length() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Original\":\"");
			res.append(JSONObject.escape(orig));
			res.append("\"");
			hasPrev = true;
		}
		
		res.append("}");
		return res.toString();
	}
	
}