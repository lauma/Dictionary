/*******************************************************************************
 * Copyright 2013, 2014 Institute of Mathematics and Computer Science, University of Latvia
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
package lv.ailab.tezaurs.analyzer.struct;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import org.w3c.dom.Node;
import org.json.simple.JSONObject;

import lv.ailab.tezaurs.utils.HasToJSON;
import lv.ailab.tezaurs.utils.MappingSet;
import lv.ailab.tezaurs.utils.Tuple;
import lv.ailab.tezaurs.utils.JSONUtils;
import lv.ailab.tezaurs.analyzer.gramlogic.*;

/**
 * g (gramatika) field.
 */
public class Gram  implements HasToJSON
{

	public String orig;
	public Flags flags;
	public LinkedList<LinkedList<String>> leftovers;
	public HashSet<Integer> paradigm;
	/**
	 * If grammar contains additional information about lemmas, it is
	 * collected here. Mapping from paradigms to lemma-flagset tuples.
	 * Flag set contains only flags for which alternate lemma differs from
	 * general flags given in "flags" field in this grammar.
	 */
	public MappingSet<Integer, Tuple<Lemma, Flags>> altLemmas;

	/**
	 * Known abbreviations and their de-abbreviations.
	 */
	public static  AbbrMap knownAbbr = AbbrMap.getAbbrMap();

	public Gram ()
	{
		orig = null;
		flags = null;
		leftovers = null;
		paradigm = null;
		altLemmas = null;
	}
	/**
	 * @param lemma is used for grammar parsing.
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
	 * @param lemma is used for grammar parsing.
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
	 * Only works correctly, if cleanupLeftovers is used, when needed.
	 */
	public boolean hasUnparsedGram()
	{
		//cleanupLeftovers();		// What is better - unexpected side effects or not working, when used incorrectly?
		return !leftovers.isEmpty();
	}
	
	/**
	 * @param lemma is used for grammar parsing.
	 */
	private void parseGram(String lemma)
	{
		String correctedGram = correctOCRErrors(orig);
		altLemmas = new MappingSet<>();
		
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
		
		// Mēģina izdomāt paradigmu no karodziņiem.
		paradigmFromFlags(lemma);
		
		cleanupLeftovers();
		// TODO cleanup altLemmas;
	}
	
	/**
	 * This method contains collection of ending patterns, found in data.
	 * These patterns are meant for using on the beginning of the
	 * unsegmented grammar string.
	 * Thus,e.g., if there was no plural-only nouns with ending -ļas, then
	 * there is no rule for processing such words (at least in most cases).
	 * @param lemma is used for grammar parsing.
	 */
	private String processBeginingWithPatterns(String gramText, String lemma)
	{
		gramText = gramText.trim();
		int newBegin = -1;

		// Īpaši sarežģītie liekumi, kas nav formalizēti citur:
		// Tiek dots vēl viens lemmas variants - kā pilns vārds.

		// Paradigm 2: Lietvārds 1. deklinācija -š
		if (lemma.endsWith("ņi") &&
				gramText.startsWith("-ņu, vsk. "
						+ lemma.substring(0, lemma.length() - 2)
						+ "ņš, -ņa, v.")) // dižtauriņi: -ņu, vsk. dižtauriņš, -ņa, v.
		{
			newBegin = ("-ņu, vsk. "+ lemma.substring(0, lemma.length() - 2) + "ņš, -ņa, v.").length();
			Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 2) + "ņš");
			Flags altParams = new Flags ();
			altParams.add(Features.ENTRYWORD__SINGULAR);
			altLemmas.put(2, new Tuple<>(altLemma, altParams));
			paradigm.add(2);
			flags.add(Features.GENDER__MASC);
			flags.add(Features.POS__NOUN);
			flags.add(Features.ENTRYWORD__PLURAL);

		}
		// Paradigm 3: Lietvārds 2. deklinācija -is
		else if (lemma.endsWith("ņi") &&
				gramText.startsWith("-ņu, vsk. "
						+ lemma.substring(0, lemma.length() - 2)
						+ "nis, -ņa, v.")) // aizvirtņi: -ņu, vsk. aizvirtnis, -ņa, v.
		{
			newBegin = ("-ņu, vsk. "+ lemma.substring(0, lemma.length() - 2)+"nis, -ņa, v.").length();
			Lemma altLemma = new Lemma(lemma.substring(0, lemma.length() - 2) + "nis");
			Flags altParams = new Flags ();
			altParams.add(Features.ENTRYWORD__SINGULAR);
			altLemmas.put(3, new Tuple<>(altLemma, altParams));
			paradigm.add(3);
			flags.add(Features.GENDER__MASC);
			flags.add(Features.POS__NOUN);
			flags.add(Features.ENTRYWORD__PLURAL);
		}
		else if (lemma.endsWith("ņi") &&
				gramText.startsWith("-ņu, vsk. "
						+ lemma.substring(0, lemma.length() - 3)
						+ "lnis, -ļņa, v.")) // starpviļņi: -ņu, vsk. starpvilnis, -ļņa, v.
		{
			newBegin = ("-ņu, vsk. "+ lemma.substring(0, lemma.length() - 3)+"lnis, -ļņa, v.").length();
			Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 3) + "lnis");
			Flags altParams = new Flags ();
			altParams.add(Features.ENTRYWORD__SINGULAR);
			altLemmas.put(3, new Tuple<>(altLemma, altParams));
			paradigm.add(3);
			flags.add(Features.GENDER__MASC);
			flags.add(Features.POS__NOUN);
			flags.add(Features.ENTRYWORD__PLURAL);
		}
		else if (lemma.endsWith("ji") &&
				gramText.startsWith("-u, vsk. " + lemma + "s, -ja, v.")) // airkāji: -u, vsk. airkājis, -ja, v.
		{
			newBegin = ("-u, vsk. " + lemma + "s, -ja, v.").length();
			Lemma altLemma = new Lemma (lemma + "s");
			Flags altParams = new Flags ();
			altParams.add(Features.ENTRYWORD__SINGULAR);
			altLemmas.put(3, new Tuple<>(altLemma, altParams));
			paradigm.add(3);
			flags.add(Features.GENDER__MASC);
			flags.add(Features.POS__NOUN);
			flags.add(Features.ENTRYWORD__PLURAL);
		}

		// Paradigm 1: Lietvārds 1. deklinācija -s
		else if (lemma.endsWith("i") &&
				gramText.startsWith("-u, vsk. "
						+ lemma.substring(0, lemma.length() - 1)
						+ "s, -a, v.")) // aizkari: -u, vsk. aizkars, -a, v.
		{
			newBegin = ("-u, vsk. " + lemma.substring(0, lemma.length() - 1) + "s, -a, v.").length();
			Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 1) + "s");
			Flags altParams = new Flags ();
			altParams.add(Features.ENTRYWORD__SINGULAR);
			altLemmas.put(1, new Tuple<>(altLemma, altParams));
			paradigm.add(1);
			flags.add(Features.GENDER__MASC);
			flags.add(Features.POS__NOUN);
			flags.add(Features.ENTRYWORD__PLURAL);
		}

		// Vēl viens lemmas variants tiek uzdots ar papildus galotņu palīdzību.
		// Paradigm 1: Lietvārds 1. deklinācija -s
		// Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
		else if (gramText.matches("s\\. -te, -šu([;.].*)?")) //abstinents
		{
			newBegin = "s. -te, -šu".length();
			if (lemma.endsWith("ts"))
			{
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 1) + "e");
				Flags altParams = new Flags ();
				altParams.add(Features.ENTRYWORD__FEM);
				altParams.add(Values.CHANGED_PARADIGM);
				altLemmas.put(9, new Tuple<>(altLemma, altParams));

				paradigm.add(1);
				flags.add(Features.GENDER__MASC);
				flags.add(Features.POS__NOUN);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt kādā no paradigmām 1 & 9\n", lemma);
				newBegin = 0;
			}
		}
		// Paradigm 3: Lietvārds 2. deklinācija -is
		// Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
		else if (gramText.matches("-ķa; s\\. -ķe -ķu([;.].*)?")) //agonistiķis
		{
			newBegin = "-ķa; s. -ķe -ķu".length();
			if (lemma.endsWith("ķis"))
			{
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 2) + "e");
				Flags altParams = new Flags ();
				altParams.add(Features.ENTRYWORD__FEM);
				altParams.add(Values.CHANGED_PARADIGM);
				altLemmas.put(9, new Tuple<>(altLemma, altParams));

				paradigm.add(2);
				flags.add(Features.GENDER__MASC);
				flags.add(Features.POS__NOUN);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt kādā no paradigmām 2 & 9\n", lemma);
				newBegin = 0;
			}
		} else if (gramText.matches("-ša; s. -te, -šu([;.].*)?")) //aiolietis
		{
			newBegin = "-ša; s. -te, -šu".length();
			if (lemma.endsWith("tis"))
			{
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 2) + "e");
				Flags altParams = new Flags ();
				altParams.add(Features.ENTRYWORD__FEM);
				altParams.add(Values.CHANGED_PARADIGM);
				altLemmas.put(9, new Tuple<>(altLemma, altParams));

				paradigm.add(2);
				flags.add(Features.GENDER__MASC);
				flags.add(Features.POS__NOUN);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt kādā no paradigmām 2 & 9\n", lemma);
				newBegin = 0;
			}
		}
		// Paradigm 13: Īpašības vārdi ar -s
		// Paradigm 14: Īpašības vārdi ar -š
		else if (gramText.matches("īp\\. v\\. -ais; s\\. -a, -ā([;,.].*)?")) //aerobs
		{
			newBegin = "īp. v. -ais; s. -a, -ā".length();
			if (lemma.matches(".*[^aeiouāēīōū]š"))
			{
				paradigm.add(14);
				flags.add(Features.POS__ADJ);
			}
			else if (lemma.matches(".*[^aeiouāēīōū]s"))
			{
				paradigm.add(13);
				flags.add(Features.POS__ADJ);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt kādā no paradigmām 13, 14\n", lemma);
				newBegin = 0;
			}
		} else if (gramText.matches("-ais[;,] s\\. -a, -ā([;,.].*)?")) //abējāds, acains, agāms
		{
			newBegin = "-ais; s. -a, -ā".length();
			if (lemma.matches(".*[^aeiouāēīōū]š"))
			{
				paradigm.add(14);
				flags.add(Features.POS__ADJ);
			}
			else if (lemma.matches(".*[^aeiouāēīōū]s"))
			{
				paradigm.add(13);
				flags.add(Features.POS__ADJ);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt kādā no paradigmām 13, 14\n", lemma);
				newBegin = 0;
			}
		}

		// Paradigm Unknown: Divdabis
		else if (gramText.matches("-gušais; s\\. -gusi, -gusī([.;].*)?")) //aizdudzis
		{
			newBegin = "-gušais; s. -gusi, -gusī".length();
			if (lemma.endsWith("dzis"))
			{
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 4) + "gusi");
				Flags altParams = new Flags ();
				altParams.add(Features.ENTRYWORD__FEM);
				altLemmas.put(0, new Tuple<>(altLemma, altParams));

				paradigm.add(0);
				flags.add(Features.POS__PARTICIPLE);
				flags.add(Features.POS__PARTICIPLE_IS);
				flags.add(Features.GENDER__MASC);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt paradigmā 0 (Divdabis)\n", lemma);
				newBegin = 0;
			}
		}
		else if (gramText.matches("-ušais; s. -usi, -usī([.;].*)?")) //aizkūpis
		{
			newBegin = "-ušais; s. -usi, -usī".length();
			if (lemma.matches(".*[cdjlmprstv]is"))
			{
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 3) + "usi");
				Flags altParams = new Flags ();
				altParams.add(Features.ENTRYWORD__FEM);
				altLemmas.put(0, new Tuple<>(altLemma, altParams));

				paradigm.add(0);
				flags.add(Features.POS__PARTICIPLE);
				flags.add(Features.POS__PARTICIPLE_IS);
				flags.add(Features.GENDER__MASC);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt paradigmā 0 (Divdabis)\n", lemma);
				newBegin = 0;
			}
		}
		// Ārpus šīs klases formalizētie likumi:
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
	 * This method contains collection of patterns with no commas in them -
	 * these patterns can be applied to any segmented grammar substring, not
	 * only on the beginning of the grammar. Only patterns found in data are
	 * given. Thus,e.g., if there was no plural-only nouns with ending -ļas,
	 * then there is no rule for processing such words (at least in most
	 * cases).
	 * @param lemma is used for grammar parsing.
	 * @return leftovers (unprocessed part of string)
	 */
	private String processWithNoCommaPatterns(String gramText, String lemma)
	{
		gramText = gramText.trim();
		int newBegin = -1;
		
		// Alternative form processing.
		if (gramText.matches("parasti divd\\. formā: (\\w+)")) //aizdzert->aizdzerts
		{
			Matcher m = Pattern.compile("(parasti divd\\. formā: (\\w+))([.;].*)?")
					.matcher(gramText);
			m.matches();
			String newLemma = m.group(2);
			Lemma altLemma = new Lemma (newLemma);
			Flags altParams = new Flags ();
			altParams.add(Features.POS__PARTICIPLE);
			altParams.add(Values.CHANGED_PARADIGM);
			
			newBegin = m.group(1).length();
			if (newLemma.endsWith("ts")) // aizdzert->aizdzerts
			{
				altParams.add(Features.POS__PARTICIPLE_TS);
				altLemmas.put(0, new Tuple<>(altLemma, altParams));
				
				flags.add(Features.POS__VERB);
				//flags.binary.add("Parasti divdabja formā");
				//flags.binary.add("Parasti lokāmā ciešamās kārtas pagātnes divdabja formā");
				flags.add(Features.USUALLY_USED__PARTICIPLE);
				flags.add(Features.USUALLY_USED__PARTICIPLE_TS);
			}
			else if (newLemma.endsWith("is") || newLemma.endsWith("ies")) // aizmakt->aizsmacis, pieriesties->pieriesies
			{
				altParams.add(Features.POS__PARTICIPLE_IS);
				altLemmas.put(0, new Tuple<>(altLemma, altParams));
				
				flags.add(Features.POS__VERB);
				//flags.binary.add("Parasti divdabja formā");
				//flags.binary.add("Parasti lokāmā darāmās kārtas pagātnes divdabja formā");
				flags.add(Features.USUALLY_USED__PARTICIPLE);
				flags.add(Features.USUALLY_USED__PARTICIPLE_IS);
			}
			else if (newLemma.endsWith("damies")) //aizvilkties->aizvilkdamies
			{
				altParams.add(Features.POS__PARTICIPLE_DAMS);
				altLemmas.put(0, new Tuple<>(altLemma, altParams));
				
				flags.add(Features.POS__VERB);
				//flags.binary.add("Parasti divdabja formā");
				//flags.binary.add("Parasti daļēji lokāmā divdabja formā");
				flags.add(Features.USUALLY_USED__PARTICIPLE);
				flags.add(Features.USUALLY_USED__PARTICIPLE_DAMS);
			}
			else
			{
				System.err.printf("Problem matching \"%s\" in entry \"%s\" with paradigm 0 (Divdabis)\n",
						newLemma, lemma);
				newBegin = 0;
			}
		} else if (gramText.matches("bieži lok\\.: (\\w+)")) // agrums->agrumā
		{
			Matcher m = Pattern.compile("(bieži lok\\.: (\\w+))([.;].*)?").matcher(gramText);
			newBegin = m.group(1).length();
			//flags.binary.add("Bieži lokatīva formā");
			flags.add(Keys.OFTEN_USED_IN_FORM, Values.LOCATIVE);
		}
		
		if (newBegin > 0) gramText = gramText.substring(newBegin);
		return gramText;
	}
	
	/**
	 * This method contains collection of patterns with no semicolon in them -
	 * these patterns can be applied to grammar segmented on ';', but not
	 * segmented on ','. Only patterns found in data are
	 * given. Thus,e.g., if there was no plural-only nouns with ending -ļas,
	 * then there is no rule for processing such words (at least in most
	 * cases).
	 * @param lemma is used for grammar parsing.
	 * @return leftovers (unprocessed part of string)
	 */
	private String processWithNoSemicolonPatterns(String gramText, String lemma)
	{
		gramText = gramText.trim();
		int newBegin = -1;
		
		// Alternative form processing.
		if (gramText.matches("parasti divd\\. formā: (\\w+), (\\w+)")) //aizelsties->aizelsies, aizelsdamies
		{
			Matcher m = Pattern.compile("(parasti divd\\. formā: (\\w+), (\\w+))([.;].*)?")
					.matcher(gramText);
			m.matches();
			String[] newLemmas = {m.group(2), m.group(3)};
			newBegin = m.group(1).length();
			for (String newLemma : newLemmas)
			{
				Lemma altLemma = new Lemma (newLemma);
				Flags altParams = new Flags ();
				altParams.add(Features.POS__PARTICIPLE);
				altParams.add(Features.ENTRYWORD__CHANGED_PARADIGM);
				
				if (newLemma.endsWith("ts")) // noliegt->noliegts
				{
					altParams.add(Features.POS__PARTICIPLE_TS);
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add(Features.POS__VERB);
					//flags.binary.add("Parasti divdabja formā");
					//flags.binary.add("Parasti lokāmā ciešamās kārtas pagātnes divdabja formā");
					flags.add(Features.USUALLY_USED__PARTICIPLE);
					flags.add(Features.USUALLY_USED__PARTICIPLE_TS);
				}
				else if (newLemma.endsWith("is") || newLemma.endsWith("ies")) // aizelsties->aizelsies
				{
					altParams.add(Features.POS__PARTICIPLE_IS);
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add(Features.POS__VERB);
					//flags.binary.add("Parasti divdabja formā");
					//flags.binary.add("Parasti lokāmā darāmās kārtas pagātnes divdabja formā");
					flags.add(Features.USUALLY_USED__PARTICIPLE);
					flags.add(Features.USUALLY_USED__PARTICIPLE_IS);
				}
				else if (newLemma.endsWith("ams") || newLemma.endsWith("āms")) // noliegt->noliedzams
				{
					altParams.add(Features.POS__PARTICIPLE_AMS);
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add(Features.POS__VERB);
					//flags.binary.add("Parasti divdabja formā");
					//flags.binary.add("Parasti lokāmā ciešamās kārtas tagadnes divdabja formā");
					flags.add(Features.USUALLY_USED__PARTICIPLE);
					flags.add(Features.USUALLY_USED__PARTICIPLE_AMS);

				}
				else if (newLemma.endsWith("damies")) //aizelsties->aizelsdamies
				{
					altParams.add(Features.POS__PARTICIPLE_DAMS);
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add(Features.POS__VERB);
					//flags.binary.add("Parasti divdabja formā");
					//flags.binary.add("Parasti daļēji lokāmā divdabja formā");
					flags.add(Features.USUALLY_USED__PARTICIPLE);
					flags.add(Features.USUALLY_USED__PARTICIPLE_DAMS);

				}
				else
				{
					System.err.printf("Neizdodas \"%s\" šķirklī \"%s\" ielikt paradigmā 0 (Divdabis)\n",
							newLemma, lemma);
					newBegin = 0;
				}
			}
		}

		if (newBegin > 0) gramText = gramText.substring(newBegin);
		return gramText;
	}

	/**
	 * @param lemma is used for paradigm detection in cases where endings
	 * matter.
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

			if (pos.contains(Values.VERB.s))
			{
				if (lemma.endsWith("īt") || lemma.endsWith("ināt"))
					paradigm.add(17);
				if (lemma.endsWith("īties") || lemma.endsWith("ināties"))
					paradigm.add(20);
			}

			if (pos.contains("Apstākļa vārds")) paradigm.add(21);
			if (pos.contains("Partikula")) paradigm.add(28);
			if (pos.contains("Prievārds")) paradigm.add(26);
			if (pos.contains("Saiklis")) paradigm.add(27);

			if (pos.contains("Izsauksmes vārds")) paradigm.add(29); // Hardcoded
			if (pos.contains("Saīsinājums")) paradigm.add(29); // Hardcoded

			if (pos.contains(Values.PRONOUN.s)) paradigm.add(25);
			if (pos.contains("Jautājamais vietniekvārds")) paradigm.add(25);
			if (pos.contains("Noliedzamais vietniekvārds")) paradigm.add(25);
			if (pos.contains("Norādāmais vietniekvārds")) paradigm.add(25);
			if (pos.contains("Noteicamais vietniekvārds")) paradigm.add(25);
			if (pos.contains("Piederības vietniekvārds")) paradigm.add(25);
			if (pos.contains("Vispārināmais vietniekvārds")) paradigm.add(25);

			if (pos.contains(Values.FOREIGN.s)) paradigm.add(29);

			if (pos.contains("Priedēklis")) paradigm.add(0); //Prefixes are not words.
			if (pos.contains("Salikteņu daļa")) paradigm.add(0); //Prefixes are not words.
		}

		if (flags.testKey(Keys.CASE) && flags.test(Features.NON_INFLECTIVE))
		{
			if (paradigm.size() > 0)
				System.out.println("Sastingušajai \"" + lemma + "\" formai jau ir paradigmas " +
						paradigm.stream().map(t -> toString())
								.reduce((t1, t2) -> t1 + ", " + t2).orElse("") + ".");
			/*if (flags.testKey(Keys.POS))
				System.out.println("Sastingušajai \"" + lemma + "\" formai jau ir vārdšķira " +
						flags.getAll(Keys.POS).stream()
								.reduce((t1, t2) -> t1 + ", " + t2).orElse("") + ".");
								*/ // Vietvārdiem var gadīties.
			paradigm.add(29); // Sastingusi forma.
		}
	}
	
	/**
	 * This should be called after something is removed from leftovers.
	 */
	public void cleanupLeftovers()
	{
		for (int i = leftovers.size() - 1; i >= 0; i--)
		{
			if (leftovers.get(i).isEmpty()) leftovers.remove(i);
		}
	}
	
	/**
	 * Hopefully, this method will be empty for final data ;)
	 */
	private String correctOCRErrors(String gramText)
	{
		//Inconsequences in data

		return gramText;
	}

	public String toJSON()
	{
		return toJSON(true);
	}
	
	// In case of speed problems StringBuilder can be returned.
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