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
	public HashSet<String> flags;
	public LinkedList<LinkedList<String>> leftovers;
	public HashSet<Integer> paradigm;
	/**
	 * If grammar contains additional information about lemmas, it is
	 * collected here. Mapping from paradigms to lemma-flagset tuples.
	 * Flag set contains only flags for which alternate lemma differs from
	 * general flags given in "flags" field in this grammar.
	 */
	public MappingSet<Integer, Tuple<Lemma, HashSet<String>>> altLemmas;

	/**
	 * Known abbreviations and their de-abbreviations.
	 */
	public static  MappingSet<String, String> knownAbbr = AbbrMap.getAbbrMap();

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
		flags = new HashSet<> ();
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
		flags = new HashSet<> ();
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
		
		// First process ending patterns, usually located in the beginning
		// of the grammar string.
		correctedGram = processBeginingWithPatterns(correctedGram, lemma);
		
		String[] subGrams = correctedGram.split("\\s*;\\s*");
		leftovers = new LinkedList<> ();
		
		// Process each semicolon-separated substring.
		for (String subGram : subGrams)	
		{
			subGram = processWithNoSemicolonPatterns(subGram, lemma);
			String[] gramElems = subGram.split("\\s*,\\s*");
			LinkedList<String> toDo = new LinkedList<> ();
			
			// Process each comma-separated substring.
			for (String gramElem : gramElems) 
			{
				gramElem = gramElem.trim();
				// Check for abbreviations.
				if (knownAbbr.containsKey(gramElem))
					flags.addAll(knownAbbr.getAll(gramElem));
				else
				{
					// Check for matches regular expressions.
					gramElem = processWithNoCommaPatterns(gramElem, lemma);
					// Unprocessed leftovers. 
					if (!gramElem.equals(""))
						toDo.add(gramElem);	
				}
			}
			
			// TODO: magical patterns for processing endings.
			
			leftovers.add(toDo);
		}
		
		// Try to deduce paradigm from flags.
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
		
		// Blocks of rules.
		// Verbs.
		for (Rule s : Rules.verbRulesDirect)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : Rules.verbRulesOptHyperns)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}


		for (Rule s : Rules.otherRulesDirect)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		for (Rule s : Rules.simpleRulesOptHyperns)
		{
			if (newBegin != -1) break;
			newBegin = s.applyOptHyphens(gramText, lemma, paradigm, flags);
		}
		
		// Complicated rules: grammar contains lemma variation spelled out.
		if (newBegin == -1)
		{
			// Super-complicated case: pronunciations included.
			// Paradigm 1: Lietvārds 1. deklinācija -s
			// Changed in new version
			/*if (lemma.endsWith("di") &&
				gramText.matches("(-u, vsk\\. (\\Q"
						+ lemma.substring(0, lemma.length() - 1)
						+ "s\\E) \\[([^\\]]*?)\\] -a, v\\.)(.*)?")) // ābeļziedi: -u, vsk. ābeļzieds [a^be`ļzie^c] -a, v.
			{
				Pattern patternText = Pattern.compile("(-u, vsk\\. (\\Q"
						+ lemma.substring(0, lemma.length() - 1)
						+ "s\\E) \\[([^\\]]*?)\\] -a, v\\.)(.*)?");
				Matcher matcher = patternText.matcher(gramText);
				if (!matcher.matches()) 
					System.err.printf("Problem matching \"%s\" with \"ābeļzieds\" rule\n", lemma);
				newBegin = matcher.group(1).length();
				Lemma altLemma = new Lemma(matcher.group(2));
				altLemma.pronunciations = matcher.group(3);
				HashSet<String> altParams = new HashSet<String> ();
				altParams.add("Šķirkļavārds vienskaitlī");
				altLemmas.put(1, new Tuple<Lemma, HashSet<String>>(altLemma, altParams));
				
				paradigm.add(1);
				flags.add("Vīriešu dzimte");
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}//*/

			// Paradigm 2: Lietvārds 1. deklinācija -š
			if (lemma.endsWith("ņi") &&
				gramText.startsWith("-ņu, vsk. "
						+ lemma.substring(0, lemma.length() - 2)
						+ "ņš, -ņa, v.")) // dižtauriņi: -ņu, vsk. dižtauriņš, -ņa, v.
			{
				newBegin = ("-ņu, vsk. "+ lemma.substring(0, lemma.length() - 2) + "ņš, -ņa, v.").length();
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 2) + "ņš");
				HashSet<String> altParams = new HashSet<> ();
				altParams.add("Šķirkļavārds vienskaitlī");
				altLemmas.put(2, new Tuple<>(altLemma, altParams));
				paradigm.add(2);
				flags.add("Vīriešu dzimte");
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			// Paradigm 3: Lietvārds 2. deklinācija -is
			else if (lemma.endsWith("ņi") &&
				gramText.startsWith("-ņu, vsk. "
						+ lemma.substring(0, lemma.length() - 2)
						+ "nis, -ņa, v.")) // aizvirtņi: -ņu, vsk. aizvirtnis, -ņa, v.
			{
				newBegin = ("-ņu, vsk. "+ lemma.substring(0, lemma.length() - 2)+"nis, -ņa, v.").length();
				Lemma altLemma = new Lemma(lemma.substring(0, lemma.length() - 2) + "nis");
				HashSet<String> altParams = new HashSet<> ();
				altParams.add("Šķirkļavārds vienskaitlī");
				altLemmas.put(3, new Tuple<>(altLemma, altParams));
				paradigm.add(3);
				flags.add("Vīriešu dzimte");
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else if (lemma.endsWith("ņi") &&
				gramText.startsWith("-ņu, vsk. "
						+ lemma.substring(0, lemma.length() - 3)
						+ "lnis, -ļņa, v.")) // starpviļņi: -ņu, vsk. starpvilnis, -ļņa, v.
			{
				newBegin = ("-ņu, vsk. "+ lemma.substring(0, lemma.length() - 3)+"lnis, -ļņa, v.").length();
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 3) + "lnis");
				HashSet<String> altParams = new HashSet<> ();
				altParams.add("Šķirkļavārds vienskaitlī");
				altLemmas.put(3, new Tuple<>(altLemma, altParams));
				paradigm.add(3);
				flags.add("Vīriešu dzimte");
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else if (lemma.endsWith("ji") &&
				gramText.startsWith("-u, vsk. " + lemma + "s, -ja, v.")) // airkāji: -u, vsk. airkājis, -ja, v.
			{
				newBegin = ("-u, vsk. " + lemma + "s, -ja, v.").length();
				Lemma altLemma = new Lemma (lemma + "s");
				HashSet<String> altParams = new HashSet<> ();
				altParams.add("Šķirkļavārds vienskaitlī");
				altLemmas.put(3, new Tuple<>(altLemma, altParams));
				paradigm.add(3);
				flags.add("Vīriešu dzimte");
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}

			// Paradigm 1: Lietvārds 1. deklinācija -s		
			else if (lemma.endsWith("i") &&
				gramText.startsWith("-u, vsk. "
						+ lemma.substring(0, lemma.length() - 1)
						+ "s, -a, v.")) // aizkari: -u, vsk. aizkars, -a, v.
			{
				newBegin = ("-u, vsk. " + lemma.substring(0, lemma.length() - 1) + "s, -a, v.").length();
				Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 1) + "s");
				HashSet<String> altParams = new HashSet<> ();
				altParams.add("Šķirkļavārds vienskaitlī");
				altLemmas.put(1, new Tuple<>(altLemma, altParams));
				paradigm.add(1);
				flags.add("Vīriešu dzimte");
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}			
		}
		
		// "-es, dsk. ģen. -??u, s."
		if (newBegin == -1) newBegin = esEndingPluralGenUEndingFemRules(gramText, lemma);
		
		
		// More rules
		if (newBegin == -1)
		{
			// Long, specific patterns.
			// Paradigm 7: Lietvārds 4. deklinācija -a siev. dz.
			// Paradigm 8: Lietvārds 4. deklinācija -a vīr. dz.
			if (gramText.startsWith("ģen. -as, v. dat. -am, s. dat. -ai, kopdz."))
			{
				newBegin = "ģen. -as, v. dat. -am, s. dat. -ai, kopdz.".length();
				if (lemma.endsWith("a"))
				{
					paradigm.add(7);
					paradigm.add(8);
					flags.add("Lietvārds");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 7, 8\n", lemma);
					newBegin = 0;
				}
				flags.add("Kopdzimte");
			}
			
			// Paradigm 1: Lietvārds 1. deklinācija -s
			// Paradigm 2: Lietvārds 1. deklinācija -š
			else if (gramText.startsWith("lietv. -a, v.")) // aerobs 
			{
				newBegin = "lietv. -a, v.".length();
				//if (lemma.matches(".*[ģjķr]is")) paradigm.add(3);
				//else
				//{
					//if (lemma.matches(".*[aeiouāēīōū]s") || lemma.matches(".*[^sš]"))
					//	System.err.printf("Problem matching \"%s\" with paradigms 1, 2, 3\n", lemma);
					
					if (lemma.endsWith("š")) paradigm.add(2);
					else if (lemma.matches(".*[^aeiouāēīōū]s")) paradigm.add(1);
					else
					{
						System.err.printf("Problem matching \"%s\" with paradigms 1, 2, 3\n", lemma);
						newBegin = 0;
					}
				//}
				flags.add("Vīriešu dzimte");
				flags.add("Lietvārds");
			}
			else if (gramText.startsWith("vsk. -a, v.")) // acteks
			{
				newBegin = "vsk. -a, v.".length();
				
				if (lemma.endsWith("š"))
				{
					paradigm.add(2);
					flags.add("Lietvārds");
				}
				else if (lemma.matches(".*[^aeiouāēīōū]s"))
				{
					paradigm.add(1);
					flags.add("Lietvārds");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigms 1, 2\n", lemma);
					newBegin = 0;
				}
				flags.add("Vīriešu dzimte");
				flags.add("Vienskaitlis");
			}

			// Paradigm 7: Lietvārds 4. deklinācija -a siev. dz.
			// Paradigm 11: Lietvārds 6. deklinācija -s siev. dz.
			else if (gramText.startsWith("-as, s.")) //aberācija, milns, najādas
			{
				newBegin = "-as, s.".length();
				if (lemma.matches(".*[^aeiouāēīōū]s"))
				{
					paradigm.add(11);
					flags.add("Lietvārds");
				} 
				else if (lemma.endsWith("a"))
				{
					paradigm.add(7);
					flags.add("Lietvārds");
				}
				else if (lemma.matches(".*[^aeiouāēīōū]as"))
				{
					paradigm.add(7);
					flags.add("Šķirkļavārds daudzskaitlī");
					flags.add("Lietvārds");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 7, 11\n", lemma);
					newBegin = 0;
				}
				flags.add("Sieviešu dzimte");
			}
			
			// Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
			// Paradigm 11: Lietvārds 6. deklinācija -s
			else if (gramText.startsWith("dsk. ģen. -ņu, s.")) //ādmine, bākuguns, bārkšsaknes
			{
				newBegin = "dsk. ģen. -ņu, s.".length();
				if (lemma.endsWith("ns"))
				{
					paradigm.add(11);
					flags.add("Lietvārds");
				}
				else if (lemma.endsWith("nes"))
				{
					paradigm.add(9);
					flags.add("Lietvārds");
					flags.add("Šķirkļavārds daudzskaitlī");
				}
				else if (lemma.endsWith("ne"))
				{
					paradigm.add(9);
					flags.add("Lietvārds");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 9, 11\n", lemma);
					newBegin = 0;
				}
				flags.add("Sieviešu dzimte");
			}
			
			// Grammar includes endings for other lemma variants. 
			// Paradigm 1: Lietvārds 1. deklinācija -s
			// Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
			else if (gramText.matches("s\\. -te, -šu([;.].*)?")) //abstinents
			{
				newBegin = "s. -te, -šu".length();
				if (lemma.endsWith("ts"))
				{
					Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 1) + "e");
					HashSet<String> altParams = new HashSet<> ();
					altParams.add("Sieviešu dzimte");
					altParams.add("Cita paradigma");
					altLemmas.put(9, new Tuple<>(altLemma, altParams));
					
					paradigm.add(1);
					flags.add("Lietvārds");
					flags.add("Vīriešu dzimte");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 1 & 5\n", lemma);
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
					HashSet<String> altParams = new HashSet<> ();
					altParams.add("Sieviešu dzimte");
					altParams.add("Cita paradigma");
					altLemmas.put(9, new Tuple<>(altLemma, altParams));
					
					paradigm.add(2);
					flags.add("Lietvārds");
					flags.add("Vīriešu dzimte");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 3 & 5\n", lemma);
					newBegin = 0;
				}
			}
			else if (gramText.matches("-ša; s. -te, -šu([;.].*)?")) //aiolietis
			{
				newBegin = "-ša; s. -te, -šu".length();
				if (lemma.endsWith("tis"))
				{
					Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 2) + "e");
					HashSet<String> altParams = new HashSet<> ();
					altParams.add("Sieviešu dzimte");
					altParams.add("Cita paradigma");
					altLemmas.put(9, new Tuple<>(altLemma, altParams));
					
					paradigm.add(2);
					flags.add("Lietvārds");
					flags.add("Vīriešu dzimte");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 3 & 5\n", lemma);
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
					flags.add("Īpašības vārds");
				}
				else if (lemma.matches(".*[^aeiouāēīōū]s"))
				{
					paradigm.add(13);
					flags.add("Īpašības vārds");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigms 13, 14\n", lemma);
					newBegin = 0;
				}
			}
			else if (gramText.matches("-ais[;,] s\\. -a, -ā([;,.].*)?")) //abējāds, acains, agāms
			{
				newBegin = "-ais; s. -a, -ā".length();
				if (lemma.matches(".*[^aeiouāēīōū]š"))
				{
					paradigm.add(14);
					flags.add("Īpašības vārds");
				}
				else if (lemma.matches(".*[^aeiouāēīōū]s"))
				{
					paradigm.add(13);
					flags.add("Īpašības vārds");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigms 13, 14\n", lemma);
					newBegin = 0;
				}
			}
			
			// Paradigm 13-14: īpašības vārdi, kas doti daudzskaitlī
			else if (gramText.startsWith("s. -as; adj.")) //abēji 2
			{
				newBegin = "s. -as; adj.".length();
				if (lemma.endsWith("i"))
				{
					paradigm.add(13);
					paradigm.add(14);
					flags.add("Īpašības vārds");
					flags.add("Šķirkļavārds daudzskaitlī");
					flags.add("Neviennozīmīga paradigma");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigms 13-14\n", lemma);
					newBegin = 0;
				}

			}
			else if (gramText.startsWith("s. -as; tikai dsk.")) //abēji 1
			{
				// This exception is on purpose! this way "tikai dsk." is later
				// transformed to appropriate flag.
				newBegin = "s. -as;".length();
				if (lemma.endsWith("i"))
				{
					paradigm.add(13);
					paradigm.add(14);
					flags.add("Īpašības vārds");
					flags.add("Šķirkļavārds daudzskaitlī");
					flags.add("Neviennozīmīga paradigma");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigms 13-14\n", lemma);
					newBegin = 0;
				}
			}

			// Paradigm 30: jaundzimušais, pēdējais
			// Paradigm unknown: -šanās
			else if (gramText.startsWith("-ās, s.")) //pirmdzimtā, -šanās
			{
				newBegin = "-ās, s.".length();
				if (lemma.endsWith("šanās"))
				{
					paradigm.add(0);
					flags.add("Atgriezeniskais lietvārds");
					flags.add("Lietvārds");	

				}
				else if (lemma.endsWith("ā"))
				{
					paradigm.add(30);
					flags.add("Īpašības vārds");
					flags.add("Lietvārds");	
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigms 30, -šanās\n", lemma);
					newBegin = 0;
				}
				flags.add("Sieviešu dzimte");
			}
			// Paradigm 30: jaundzimušais, pēdējais
			// Paradigm 22: pirmais
			else if (gramText.matches("s\\. -ā([.;].*)?")) //agrākais, pirmais
			{
				newBegin = "s. -ā".length();
				if (lemma.endsWith("ais"))
				{
					paradigm.add(30);
					paradigm.add(22);
					flags.add("Skaitļa vārds");
					flags.add("Īpašības vārds");
					flags.add("Neviennozīmīga paradigma");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 30\n", lemma);
					newBegin = 0;
				}
			}
		
			// Paradigm Unknown: Divdabis
			// Grammar includes endings for other lemma variants. 
			else if (gramText.matches("-gušais; s\\. -gusi, -gusī([.;].*)?")) //aizdudzis
			{
				newBegin = "-gušais; s. -gusi, -gusī".length();
				if (lemma.endsWith("dzis"))
				{
					Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 4) + "gusi");
					HashSet<String> altParams = new HashSet<> ();
					altParams.add("Sieviešu dzimte");
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					paradigm.add(0);
					flags.add("Divdabis");
					flags.add("Lokāmais darāmās kārtas pagātnes divdabis (-is, -usi, -ies, -usies)");
					flags.add("Vīriešu dzimte");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 0 (Divdabis)\n", lemma);
					newBegin = 0;
				}
			}
			else if (gramText.matches("-ušais; s. -usi, -usī([.;].*)?")) //aizkūpis
			{
				newBegin = "-ušais; s. -usi, -usī".length();
				if (lemma.matches(".*[cdjlmprstv]is"))
				{
					Lemma altLemma = new Lemma (lemma.substring(0, lemma.length() - 3) + "usi");
					HashSet<String> altParams = new HashSet<> ();
					altParams.add("Sieviešu dzimte");
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					paradigm.add(0);
					flags.add("Divdabis");
					flags.add("Lokāmais darāmās kārtas pagātnes divdabis (-is, -usi, -ies, -usies)");
					flags.add("Vīriešu dzimte");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" with paradigm 0 (Divdabis)\n", lemma);
					newBegin = 0;
				}
			}

		}
		
		// "-??a, v."
		if (newBegin == -1) newBegin = aEndingMascRules(gramText, lemma);
		// "-??u, v."
		if (newBegin == -1) newBegin = uEndingMascRules(gramText, lemma);
		// "-??u, s."
		if (newBegin == -1) newBegin = uEndingFemRules(gramText, lemma);
		
		// === Risky rules =================================================
		// These rules matches prefix of some other rule.
		for (Rule s : Rules.dangerousRulesDirect)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		if (newBegin == -1) newBegin = singleEndingOnlyRules(gramText, lemma);
		
		if (newBegin > 0 && newBegin <= gramText.length())
			gramText = gramText.substring(newBegin);
		else if (newBegin > gramText.length())
		{
			System.err.printf("Problem with processing lemma \"%s\" and grammar \"%s\": obtained cut index \"%d\"",
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
			Matcher m = Pattern.compile("(parasti divd\\. formā: (\\w+))([.;].*)?").matcher(gramText);
			m.matches();
			String newLemma = m.group(2);
			Lemma altLemma = new Lemma (newLemma);
			HashSet<String> altParams = new HashSet<> ();
			altParams.add("Divdabis");
			altParams.add("Cita paradigma");
			
			newBegin = m.group(1).length();
			if (newLemma.endsWith("ts")) // aizdzert->aizdzerts
			{
				altParams.add("Lokāmais ciešamās kārtas pagātnes divdabis (-ts, -ta)");
				altLemmas.put(0, new Tuple<>(altLemma, altParams));
				
				flags.add("Darbības vārds");
				flags.add("Parasti divdabja formā");
				flags.add("Parasti lokāmā ciešamās kārtas pagātnes divdabja formā");
			}
			else if (newLemma.endsWith("is") || newLemma.endsWith("ies")) // aizmakt->aizsmacis, pieriesties->pieriesies
			{
				altParams.add("Lokāmais darāmās kārtas pagātnes divdabis (-is, -usi, -ies, -usies)");
				altLemmas.put(0, new Tuple<>(altLemma, altParams));
				
				flags.add("Darbības vārds");
				flags.add("Parasti divdabja formā");
				flags.add("Parasti lokāmā darāmās kārtas pagātnes divdabja formā");
			}
			else if (newLemma.endsWith("damies")) //aizvilkties->aizvilkdamies
			{
				altParams.add("Daļēji lokāmais divdabis (-dams, -dama, -damies, -damās)");
				altLemmas.put(0, new Tuple<>(altLemma, altParams));
				
				flags.add("Darbības vārds");
				flags.add("Parasti divdabja formā");
				flags.add("Parasti daļēji lokāmā divdabja formā");
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
			flags.add("Bieži lokatīva formā");
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
				HashSet<String> altParams = new HashSet<> ();
				altParams.add("Divdabis");
				altParams.add("Cita paradigma");
				
				if (newLemma.endsWith("ts")) // noliegt->noliegts
				{
					altParams.add("Lokāmais ciešamās kārtas pagātnes divdabis (-ts, -ta)");
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add("Darbības vārds");
					flags.add("Parasti divdabja formā");
					flags.add("Parasti lokāmā ciešamās kārtas pagātnes divdabja formā");
				}
				else if (newLemma.endsWith("is") || newLemma.endsWith("ies")) // aizelsties->aizelsies
				{
					altParams.add("Lokāmais darāmās kārtas pagātnes divdabis (-is, -usi, -ies, -usies)");
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add("Darbības vārds");
					flags.add("Parasti divdabja formā");
					flags.add("Parasti lokāmā darāmās kārtas pagātnes divdabja formā");
				}
				else if (newLemma.endsWith("ams") || newLemma.endsWith("āms")) // noliegt->noliedzams
				{
					altParams.add("Lokāmais ciešamās kārtas tagadnes divdabis (-ams, -ama, -āms, -āma)");
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add("Darbības vārds");
					flags.add("Parasti divdabja formā");
					flags.add("Parasti lokāmā ciešamās kārtas tagadnes divdabja formā");
				}
				else if (newLemma.endsWith("damies")) //aizelsties->aizelsdamies
				{
					altParams.add("Daļēji lokāmais divdabis (-dams, -dama, -damies, -damās)");
					altLemmas.put(0, new Tuple<>(altLemma, altParams));
					
					flags.add("Darbības vārds");
					flags.add("Parasti divdabja formā");
					flags.add("Parasti daļēji lokāmā divdabja formā");
				}
				else
				{
					System.err.printf("Problem matching \"%s\" in entry \"%s\" with paradigm 0 (Divdabis)\n",
							newLemma, lemma);
					newBegin = 0;
				}
			}
		}

		if (newBegin > 0) gramText = gramText.substring(newBegin);
		return gramText;
	}
	

	/**
	 * Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
	 * Rules in form "-es, dsk. ģen. -ču, s.".
	 * This function is seperated out for readability from
	 * {@link #processBeginingWithPatterns(String, String)} as currently these rules
	 * for verbs are long and highly specific and, thus, do not conflict
	 * with other rules.
	 * @return new begining for gram string if one of these rulles matched,
	 * -1 otherwise.
	 */
	private int esEndingPluralGenUEndingFemRules (String gramText, String lemma)
	{
		int newBegin = -1;
		for (Rule s : Rules.fifthDeclNounRulesDirect)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		// Paradigm 9: Lietvārds 5. deklinācija -e siev. dz., vienskaitlis + daudzskaitlis.
		if (gramText.startsWith("-es, dsk. ģen. -pju, s.")) //aitkope, tūsklapes
		{
			newBegin = "-es, dsk. ģen. -pju, s.".length();
			if (lemma.endsWith("pe"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("pes"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 9\n", lemma);
				newBegin = 0;
			}
			flags.add("Sieviešu dzimte");
		}
		return newBegin;
	}
	
	/**
	 * Paradigm 7: Lietvārds 4. deklinācija -a siev. dz.
	 * Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
	 * Paradigm 11: Lietvārds 6. deklinācija -s
	 * Rules in form "-šu, s." and "-u, s.".
	 * This function is seperated out for readability from
	 * {@link #processBeginingWithPatterns(String, String)} as currently these rules
	 * for verbs are long and highly specific and, thus, do not conflict
	 * with other rules.
	 * @return new begining for gram string if one of these rulles matched,
	 * -1 otherwise.
	 */
	private int uEndingFemRules (String gramText, String lemma)
	{
		int newBegin = -1;
		// Paradigms: 7, 9, 11
		if (gramText.startsWith("-šu, s.")) //ahajiete, aizkulises, bikses, klaušas
		{
			newBegin = "-šu, s.".length();
			if (lemma.endsWith("te"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("šas"))
			{
				paradigm.add(7);
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("tis"))
			{
				paradigm.add(11);
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Lietvārds");
			}
			else if (lemma.matches(".*[st]es"))
			{
				paradigm.add(9);
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 7, 9, 11\n", lemma);
				newBegin = 0;
			}
			flags.add("Sieviešu dzimte");
		}
		// Paradigms: 7, 9
		else if (gramText.startsWith("-žu, s.")) //mirādes, graizes, bažas
		{
			newBegin = "-žu, s.".length();
			if (lemma.endsWith("žas"))
			{
				paradigm.add(7);
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Lietvārds");
			}
			else if (lemma.matches(".*[dz]es"))
			{
				paradigm.add(9);
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 7, 9\n", lemma);
				newBegin = 0;
			}
			flags.add("Sieviešu dzimte");
		}
		else if (gramText.startsWith("-ņu, s.")) //acenes, iemaņas
		{
			newBegin = "-ņu, s.".length();
			if (lemma.endsWith("ņas"))
			{
				paradigm.add(7);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else if (lemma.endsWith("ne"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("nes"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 7, 9\n", lemma);
				newBegin = 0;
			}
			flags.add("Sieviešu dzimte");
		}
		else if (gramText.startsWith("-u, s.")) // aijas, zeķes
		{
			newBegin = "-u, s.".length();
			if (lemma.endsWith("as"))
			{
				paradigm.add(7);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else if (lemma.endsWith("a"))
			{
				paradigm.add(7);
				flags.add("Lietvārds");
			}
			else if (lemma.matches(".*[ķ]es"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 7, 9\n", lemma);
				newBegin = 0;
			}
			flags.add("Sieviešu dzimte");
		}
		// Paradigms: 9
		else if (gramText.startsWith("-ļu, s.")) //bailes
		{
			newBegin = "-ļu, s.".length();
			if (lemma.endsWith("les"))
			{
				paradigm.add(9);
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 9\n", lemma);
				newBegin = 0;
			}
			flags.add("Sieviešu dzimte");
		}
		return newBegin;
	}
	
	/**
	 * Paradigm 1: Lietvārds 1. deklinācija -s
	 * Paradigm 2: Lietvārds 1. deklinācija -š
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 * Paradigm 4: Lietvārds 2. deklinācija -s (nom. == ģen.)
	 * Paradigm 5: Lietvārds 2. deklinācija -suns
	 * Rules in form "-ļa, v." and "-a, v.".
	 * This function is seperated out for readability from
	 * {@link #processBeginingWithPatterns(String, String)} as currently these rules
	 * for verbs are long and highly specific and, thus, do not conflict
	 * with other rules.
	 * @return new begining for gram string if one of these rulles matched,
	 * -1 otherwise.
	 */
	private int aEndingMascRules (String gramText, String lemma)
	{
		int newBegin = -1;
		// Paradigm 3
		for (Rule s : Rules.secondDeclNounRulesDirect)
		{
			if (newBegin != -1) break;
			newBegin = s.applyDirect(gramText, lemma, paradigm, flags);
		}
		// Paradigms: 3, 5
		if (gramText.startsWith("-ļa, v.")) // acumirklis, durkls
		{
			newBegin = "-ļa, v.".length();
			if (lemma.endsWith("ls"))
			{
				paradigm.add(5);
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("lis"))
			{
				paradigm.add(3);
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 3, 5\n", lemma);
				newBegin = 0;
			}
			flags.add("Vīriešu dzimte");
		}
		else if (gramText.startsWith("-ša, v.")) // abrkasis, lemess
		{
			newBegin = "-ša, v.".length();
			if (lemma.endsWith("ss"))
			{
				paradigm.add(5);
				flags.add("Lietvārds");
			}
			else if (lemma.matches(".*[stš]is"))
			{
				paradigm.add(3);
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 3, 5\n", lemma);
				newBegin = 0;
			}
			flags.add("Vīriešu dzimte");
		}
		//Paradigms: 1, 3
		else if (gramText.matches("-ra[,;] v.(.*)?")) // airis, mūrniekmeistars
		{
			newBegin = "-ra, v.".length();
			if (lemma.endsWith("ris"))
			{
				paradigm.add(3);
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("rs"))
			{
				paradigm.add(1);
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 3\n", lemma);
				newBegin = 0;
			}
			flags.add("Vīriešu dzimte");
		}
		// Paradigms: 2, 3, 5
		else if (gramText.startsWith("-ņa, v.")) // abesīnis
		{
			newBegin = "-ņa, v.".length();
			
			if (lemma.endsWith("suns"))
			{
				paradigm.add(5);
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("ņš"))
			{
				paradigm.add(2);
				flags.add("Lietvārds");
			}
			else if (lemma.endsWith("nis"))
			{
				paradigm.add(3);
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigms 2, 3, 5\n", lemma);
				newBegin = 0;
			}
			flags.add("Vīriešu dzimte");
		}
		// Paradigms: 1, 2, 3 (if no sound changes), 1-5 (if plural)
		else if (gramText.startsWith("-a, v.")) // abats, akustiķis, sparguļi, skostiņi
		{
			newBegin = "-a, v.".length();
			if (lemma.matches(".*[ģjķr]is"))
			{
				paradigm.add(3);
				flags.add("Lietvārds");
				
			}
			else if (lemma.endsWith("š"))
			{
				paradigm.add(2);
				flags.add("Lietvārds");
			}
			else if (lemma.matches(".*[^aeiouāēīōū]s"))
			{
				paradigm.add(1);
				flags.add("Lietvārds");
			}
			else if (lemma.matches(".*[ņ]i"))
			{
				paradigm.add(1);
				paradigm.add(2);
				paradigm.add(3);
				paradigm.add(4);
				paradigm.add(5);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Neviennozīmīga paradigma");
			}
			else if (lemma.matches(".*[ļ]i"))
			{
				paradigm.add(1);
				paradigm.add(2);
				paradigm.add(3);
				paradigm.add(5);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Neviennozīmīga paradigma");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigms 1, 2, 3\n", lemma);
				newBegin = 0;
			}
			flags.add("Vīriešu dzimte");
		}
		return newBegin;
	}
	
	/**
	 * Paradigm 1: Lietvārds 1. deklinācija -s
	 * Paradigm 2: Lietvārds 1. deklinācija -š
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 * Paradigm 4: Lietvārds 2. deklinācija -s (piem., mēness) (vsk. nom. = vsk. gen)
	 * Paradigm 5: Lietvārds 2. deklinācija -suns
	 * Paradigm 32: Lietvārds 6. deklinācija - ļaudis
	 * Rules in form "-žu, v." and "-u, v.".
	 * This function is seperated out for readability from
	 * {@link #processBeginingWithPatterns(String, String)} as currently these rules
	 * for verbs are long and highly specific and, thus, do not conflict
	 * with other rules.
	 * @return new begining for gram string if one of these rulles matched,
	 * -1 otherwise.
	 */
	private int uEndingMascRules (String gramText, String lemma)
	{
		int newBegin = -1;
		// Paradigm 32
		if (gramText.startsWith("-žu, v.")) //ļaudis
		{
			newBegin = "-žu, v.".length();
			if (lemma.endsWith("ļaudis"))
			{
				paradigm.add(11);
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Lietvārds");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 32\n", lemma);
				newBegin = 0;
			}
			flags.add("Vīriešu dzimte");					
			// TODO Daudzskaitlinieks?
		}
		// Paradigms: 1-5 (plural forms)
		else if (gramText.startsWith("-ņu, v.")) // bretoņi
		{
			newBegin = "-ņu, v.".length();
			if (lemma.endsWith("ņi"))
			{
				paradigm.add(1);
				paradigm.add(2);
				paradigm.add(3);
				paradigm.add(4);
				paradigm.add(5);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Neviennozīmīga paradigma");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigms 1-5\n", lemma);
				newBegin = 0;
			}
			flags.add("Vīriešu dzimte");
		}
		else if (gramText.startsWith("-u, v.")) // abesīņi, abhāzi, ādgrauži, adigejieši, adžāri, alimenti, angļi, antinukloni, apakšbrunči
		{
			newBegin = "-u, v.".length();
			if (lemma.endsWith("nieki") || lemma.endsWith("umi")
					|| lemma.endsWith("otāji"))
			{
				paradigm.add(1);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else if (lemma.endsWith("ieši"))
			{
				paradigm.add(3);
				paradigm.add(5);
				flags.add("Lietvārds");
				flags.add("Šķirkļavārds daudzskaitlī");
				flags.add("Neviennozīmīga paradigma");

			}
			else
			{
				if (lemma.matches(".*[ņš]i"))	// akmeņi, mēneši etc.
				{
					paradigm.add(1);
					paradigm.add(2);
					paradigm.add(3);
					paradigm.add(4);
					paradigm.add(5);
					flags.add("Lietvārds");
					flags.add("Šķirkļavārds daudzskaitlī");
					flags.add("Neviennozīmīga paradigma");
				}
				else if (lemma.matches(".*[vpm]ji"))	// looks like these are predefined sound changes always
				{
					paradigm.add(3);
					paradigm.add(5);
					flags.add("Lietvārds");
					flags.add("Šķirkļavārds daudzskaitlī");
					flags.add("Neviennozīmīga paradigma");
				}
				else if (lemma.matches(".*[bgkhrstčģķļž]i")
						|| lemma.matches(".*[aeiouāēīōū]ji"))	// can't determine if there is sound change (t - tēti, s - viesi, j - airkāji)
				{
					paradigm.add(1);
					paradigm.add(2);
					paradigm.add(3);
					paradigm.add(5);
					flags.add("Lietvārds");
					flags.add("Šķirkļavārds daudzskaitlī");
					flags.add("Neviennozīmīga paradigma");
				}
				else if (lemma.matches(".*[cdlmnpvz]i"))	// there is no sound change
				{
					paradigm.add(1);
					paradigm.add(2);
					flags.add("Lietvārds");
					flags.add("Šķirkļavārds daudzskaitlī");
					flags.add("Neviennozīmīga paradigma");
				}
				else 
				{
					System.err.printf("Problem matching \"%s\" with paradigms 1-5\n", lemma);
					newBegin = 0;						
				}
			}
			flags.add("Vīriešu dzimte");
		}			
		return newBegin;
	}
	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 * Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
	 * Rules containing single ending with no other information, e.g. "-ņu".
	 * This function is seperated out for readability from
	 * {@link #processBeginingWithPatterns(String, String)} as currently these rules
	 * for verbs are long and highly specific and, thus, do not conflict
	 * with other rules.
	 * @return new begining for gram string if one of these rulles matched,
	 * -1 otherwise.
	 */
	private int singleEndingOnlyRules (String gramText, String lemma)
	{
		int newBegin = -1;
		// Paradigm 9
		if (gramText.matches("-žu([;.].*)?")) //abioģenēze, ablumozes, akolāde, nematodes
		{
			newBegin = "-žu".length();
			if (lemma.matches(".*[dz]es"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
				flags.add("Sieviešu dzimte");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else if (lemma.matches(".*[dz]e"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
				flags.add("Sieviešu dzimte");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 9\n", lemma);
				newBegin = 0;
			}
		}
		else if (gramText.matches("-ņu([;.].*)?")) //agrene, aizlaidnes
		{
			newBegin = "-ņu".length();
			if (lemma.endsWith("nes"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
				flags.add("Sieviešu dzimte");
				flags.add("Šķirkļavārds daudzskaitlī");
			}
			else if (lemma.endsWith("ne"))
			{
				paradigm.add(9);
				flags.add("Lietvārds");
				flags.add("Sieviešu dzimte");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 9\n", lemma);
				newBegin = 0;
			}
		}
		// Paradigm 3
		else if (gramText.matches("-ņa([;,.].*)?")) //ābolainis
		{
			newBegin = "-ņa".length();
			if (lemma.endsWith("nis"))
			{
				paradigm.add(3);
				flags.add("Lietvārds");
				flags.add("Vīriešu dzimte");
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm 3\n", lemma);
				newBegin = 0;
			}
		}
		return newBegin;
	}
	



	
	/**
	 * @param lemma is used for paradigm detection in cases where endings
	 * matter.
	 */
	private void paradigmFromFlags(String lemma)
	{
		if (flags.contains("Īpašības vārds") )
		{
			if (lemma.endsWith("ais") || lemma.endsWith("ā")) paradigm.add(30);
			else if (lemma.matches(".*[^aeiouāēīōū]s")) paradigm.add(13);
			else if (lemma.matches(".*[^aeiouāēīōū]š")) paradigm.add(14);				
		}
		
		if (flags.contains("Darbības vārds"))
		{
			if (lemma.endsWith("īt") || lemma.endsWith("ināt")) paradigm.add(17);
			if (lemma.endsWith("īties") || lemma.endsWith("ināties")) paradigm.add(20);
		}
		
		if (flags.contains("Apstākļa vārds")) paradigm.add(21);
		if (flags.contains("Partikula")) paradigm.add(28);
		if (flags.contains("Prievārds")) paradigm.add(26);
		
		if (flags.contains("Izsauksmes vārds")) paradigm.add(29); // Hardcoded
		if (flags.contains("Saīsinājums")) paradigm.add(29); // Hardcoded
		if (flags.contains("Vārds svešvalodā")) paradigm.add(29);
		
		if (flags.contains("Vietniekvārds")) paradigm.add(25);
		if (flags.contains("Jautājamais vietniekvārds")) paradigm.add(25);
		if (flags.contains("Noliedzamais vietniekvārds")) paradigm.add(25);
		if (flags.contains("Norādāmais vietniekvārds")) paradigm.add(25);
		if (flags.contains("Noteicamais vietniekvārds")) paradigm.add(25);
		if (flags.contains("Piederības vietniekvārds")) paradigm.add(25);
		if (flags.contains("Vispārināmais vietniekvārds")) paradigm.add(25);

		if (flags.contains("Priedēklis")) paradigm.add(0); //Prefixes are not words.
		if (flags.contains("Salikteņu daļa")) paradigm.add(0); //Prefixes are not words.
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
				
		//gramText = gramText.replaceAll("^māt\\.", "mat\\.");
		//gramText = gramText.replace(" māt.", " mat.");
		//gramText = gramText.replace("vsk..", "vsk.");
		//gramText = gramText.replace("vsk .", "vsk.");
		//gramText = gramText.replaceAll("^gen\\.", "ģen\\.");
		//gramText = gramText.replace(" gen.", " ģen.");
		//gramText = gramText.replaceAll("^trans;", "trans\\.;");
		//gramText = gramText.replace(" trans;", " trans.;");
		
		//gramText = gramText.replace("-ais; s. -a: -ā;", "-ais; s. -a, -ā;"); //apgrēcīgs
		
		

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
					Iterator<Tuple<Lemma, HashSet<String>>> flagIt = altLemmas.getAll(next).iterator();
					while (flagIt.hasNext())
					{
						Tuple<Lemma, HashSet<String>> alt = flagIt.next();
						res.append("{");
						res.append(alt.first.toJSON());
						if (alt.second != null && !alt.second.isEmpty())
						{
							res.append(", \"Flags\":");
							res.append(JSONUtils.simplesToJSON(alt.second));
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
		
		if (flags != null && !flags.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Flags\":");
			res.append(JSONUtils.simplesToJSON(flags));
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