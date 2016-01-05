/*******************************************************************************
 * Copyright 2013, 2014 Institute of Mathematics and Computer Science, University of Latvia
 * Author: Lauma Pretkalniņa, Pēteris Paikens
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

package lv.ailab.tezaurs;

import java.io.*;
import java.security.Key;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

import lv.ailab.tezaurs.analyzer.StatsCollector;
import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.analyzer.io.StaxReader;
import lv.ailab.tezaurs.utils.Tuple;
import org.w3c.dom.Node;

import lv.ailab.tezaurs.analyzer.struct.Entry;

public class DictionaryXmlToJson
{
	public static String[] XML_FILES = {"entries", "references"};
	public static boolean PRINT_PRONONCATIONS = false;
	public static boolean PRINT_FIFTH_DECL_EXC = false;
	public static boolean PRINT_FIRST_CONJ = false;
	public static boolean PRINT_NON_INFL = false;
	public static boolean PRINT_WORDLISTS = false;
	public static String PRINT_WITH_REGEXP = ".*fe";

	// Stindzeņu izgūšana ar plašāku aprakstu.
	/*public static ArrayList<Tuple<Keys, String>> PRINT_WITH_FEATURE = new ArrayList<Tuple<Keys, String>>(){{
		add(new Tuple<Keys, String>(Keys.CASE, null));
		add(Features.NON_INFLECTIVE);
	}};
	public static ArrayList<Tuple<Keys, String>> PRINT_WITH_FEATURE_DESC = new ArrayList<Tuple<Keys, String>>(){{
		add(new Tuple<Keys, String>(Keys.CASE, null));
		add(new Tuple<Keys, String>(Keys.DOMAIN, null));
	}};//*/
	// Vēsturisko izgūšana.
	/*public static ArrayList<Tuple<Keys, String>> PRINT_WITH_FEATURE = new ArrayList<Tuple<Keys, String>>(){{
		add(Features.USAGE_RESTR__HISTORICAL);
	}};
	public static ArrayList<Tuple<Keys, String>> PRINT_WITH_FEATURE_DESC = new ArrayList<Tuple<Keys, String>>(){{
		add(Features.POS__FOREIGN);
		add(Features.PLACE_NAME);
		add(Features.PERSON_NAME);
		add(Features.DOMAIN__HIST_PLACE);
		add(Features.DOMAIN__HIST_PERSON);
	}};//*/
	// Neitrālais: neizgūt neko papildus. Ātrāk un ģenerē pārskatāmākus stats failus.
	public static ArrayList<Tuple<Keys, String>> PRINT_WITH_FEATURE = null;
	public static ArrayList<Tuple<Keys, String>> PRINT_WITH_FEATURE_DESC = null;

	/**
	 * @param args pirmais arguments - ceļš uz vietu, kur stāv apstrādājamie XML
	 *             faili
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		String path = args[0];
		if (!path.endsWith("/") && !path.endsWith("\\"))
			path = path + "\\";
		for (String file : XML_FILES)
		{
			System.out.println("Sāk apstrādāt failu " + file + ".xml.");

			// Initialize IO.
			String inputFile = path + file + ".xml";;
			String goodOutputFile = path + file + "-good.json";
			String noParadigm = path + file + "-noParadigm.json";
			String badOutputFile = path + file + "-bad.json";
			String statsFile = path + file + "-stats.txt";

			BufferedWriter wordlistOut = null;
			if (PRINT_WORDLISTS)
			{
				String wordlistFile = path + file + "-wordlist.txt";
				wordlistOut = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(wordlistFile), "UTF-8"));
			}
			StatsCollector sc = new StatsCollector(PRINT_PRONONCATIONS,
					PRINT_FIRST_CONJ, PRINT_FIFTH_DECL_EXC, PRINT_NON_INFL,
					PRINT_WITH_REGEXP,
					PRINT_WITH_FEATURE, PRINT_WITH_FEATURE_DESC, wordlistOut);


			StaxReader dicReader = new StaxReader(inputFile);

			BufferedWriter goodOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(goodOutputFile), "UTF-8"));
			goodOut.write("[\n");
			BufferedWriter noParadigmOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(noParadigm), "UTF-8"));
			noParadigmOut.write("[\n");
			BufferedWriter badOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(badOutputFile), "UTF-8"));
			badOut.write("[\n");

			// Process each node.
			int count = 0;
			Node entryNode = dicReader.readNexEntry();
			while (entryNode != null)
			{
				Entry entry = new Entry(entryNode);
				sc.countEntry(entry);

				// Print out all pronunciations.
				//if (makePronunceList)
				//	for (String p : entry.collectPronunciations())
				//		statsOut.write(p + "\t" + entry.head.lemma.text + "\t" + entry.homId + "\n");

				if (!entry
						.inBlacklist())    // Blacklisted entries are not included in output logs.
				{
					if (entry.hasParadigm() && !entry.hasUnparsedGram())
						goodOut.write(entry.toJSON() + ",\n");
					else if (!entry.hasParadigm() && !entry.hasUnparsedGram())
						noParadigmOut.write(entry.toJSON() + ",\n");
					else
						badOut.write(entry.toJSON() + ",\n");
				}
				entryNode = dicReader.readNexEntry();
				count++;
				if (count % 50 == 0)
					System.out.print("Apstrādātie šķirkļi:\t" + count + "\r");
			}
			System.out.println("Apstrādātie šķirkļi:\t" + count);
			goodOut.write("]");
			goodOut.close();
			noParadigmOut.write("]");
			noParadigmOut.close();
			badOut.write("]");
			badOut.close();
			if (wordlistOut != null) wordlistOut.close();

			System.out.println("Drukā statistiku...");
			BufferedWriter statsOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(statsFile), "UTF-8"));
			sc.printContents(statsOut);
			statsOut.close();
			//if (makePronunceList) statsOut.close();
		}
		System.out.println("Viss pabeigts!");
	}
}
