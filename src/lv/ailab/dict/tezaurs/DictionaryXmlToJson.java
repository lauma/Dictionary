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

package lv.ailab.dict.tezaurs;

import java.io.*;
import java.util.ArrayList;

import lv.ailab.dict.tezaurs.analyzer.FirstConjStatsCollector;
import lv.ailab.dict.tezaurs.analyzer.GeneralStatsCollector;
import lv.ailab.dict.tezaurs.analyzer.io.StaxWriter;
import lv.ailab.dict.tezaurs.analyzer.io.StaxReader;
import lv.ailab.dict.tezaurs.analyzer.struct.TEntry;
import lv.ailab.dict.utils.Tuple;
import org.w3c.dom.Node;

/**
 * Tēzaura gramatiku analīzes rīka ieejas punkts.
 *
 * Parametrus, kas nav immutable, nevajag censties izpildes laikā mainīt, tas
 * ne pie kā laba nenovedīs.
 */
public class DictionaryXmlToJson
{
	public final static String[] XML_FILES = {"entries", "references"};
	public final static boolean PRINT_SINGLE_XML = false;
	public final static boolean PRINT_SINGLE_JSON = false;

	// Parametri, kas attiecināmi ur GeneralStatsCollector.
	public final static boolean PRINT_WORDLISTS = false;

	public final static boolean PRINT_PRONONCATIONS = false;
	public final static boolean PRINT_FIFTH_DECL_EXC = false;
	//public final static boolean PRINT_FIRST_CONJ = false;
	public final static boolean PRINT_NON_INFL = false;

	public final static String PRINT_WITH_REGEXP = null;
	//public static String PRINT_WITH_REGEXP = ".*fe";

	public final static ArrayList<Integer> PPRINT_WITH_PARADIGM = null;
	/*public static ArrayList<Integer> PPRINT_WITH_PARADIGM = new ArrayList<Integer>() {{
		add (30);
		add (40);
		add (41);
	}};//*/

	// Neitrālais: neizgūt neko papildus. Ātrāk un ģenerē pārskatāmākus stats failus.
	public final static ArrayList<Tuple<String, String>> PRINT_WITH_FEATURE = null;
	public final static ArrayList<Tuple<String, String>> PRINT_WITH_FEATURE_DESC = null;
	// Stindzeņu izgūšana ar plašāku aprakstu.
	/*public static ArrayList<Tuple<TKeys, String>> PRINT_WITH_FEATURE = new ArrayList<Tuple<TKeys, String>>(){{
		add(new Tuple<TKeys, String>(TKeys.CASE, null));
		add(TFeatures.NON_INFLECTIVE);
	}};
	public final static ArrayList<Tuple<TKeys, String>> PRINT_WITH_FEATURE_DESC = new ArrayList<Tuple<TKeys, String>>(){{
		add(new Tuple<TKeys, String>(TKeys.CASE, null));
		add(new Tuple<TKeys, String>(TKeys.DOMAIN, null));
	}};//*/
	// Vēsturisko izgūšana.
	/*public static ArrayList<Tuple<TKeys, String>> PRINT_WITH_FEATURE = new ArrayList<Tuple<TKeys, String>>(){{
		add(TFeatures.USAGE_RESTR__HISTORICAL);
	}};
	public final static ArrayList<Tuple<TKeys, String>> PRINT_WITH_FEATURE_DESC = new ArrayList<Tuple<TKeys, String>>(){{
		add(TFeatures.POS__FOREIGN);
		add(TFeatures.PLACE_NAME);
		add(TFeatures.PERSON_NAME);
		add(TFeatures.DOMAIN__HIST_PLACE);
		add(TFeatures.DOMAIN__HIST_PERSON);
	}};//*/
	// Dialektismu un apvidvārdu izgūšana
	/*public final static ArrayList<Tuple<TKeys, String>> PRINT_WITH_FEATURE = new ArrayList<Tuple<TKeys, String>>(){{
		add(TFeatures.USAGE_RESTR__DIALECTICISM);
	}};//*/
	/*public final static ArrayList<Tuple<TKeys, String>> PRINT_WITH_FEATURE = new ArrayList<Tuple<TKeys, String>>(){{
		add(TFeatures.USAGE_RESTR__REGIONAL);
	}};//*/
	/*public final static ArrayList<Tuple<TKeys, String>> PRINT_WITH_FEATURE_DESC = new ArrayList<Tuple<TKeys, String>>(){{
		add(TFeatures.USAGE_RESTR__REGIONAL);
		add(new Tuple<TKeys, String>(TKeys.DIALECT_FEATURES, null));
		add(new Tuple<TKeys, String>(TKeys.POS, null));
	}};//*/
	// Vēl raksturlielumi, ar kuriem aprakstīt šķirkļus, kas izgūti ar PRINT_WITH_FEATURE
	public final static boolean PRINT_PARADIGMS = true;
	public final static boolean PRINT_OTHER_LEMMAS = true;

	// Parametri, kas attiecināmi ur FirstConjStatsCollector.
	public final static boolean PRINT_FIRST_CONJ_DIRECT = true;
	public final static boolean PRINT_FIRST_CONJ_REFL = true;


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
		String completeXmlPath = path + "analyzed_tezaurs.xml";
		String completeJsonPath = path + "analyzed_tezaurs.json";
		StaxWriter completeXmlOut = null;
		BufferedWriter completeJsonOut = null;

		if (PRINT_SINGLE_XML) completeXmlOut = new StaxWriter(completeXmlPath);
		if (PRINT_SINGLE_JSON) completeJsonOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(completeJsonPath), "UTF-8"));

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
				String wordlistFile = path + file + "-feats.txt";
				wordlistOut = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(wordlistFile), "UTF-8"));
			}
			GeneralStatsCollector genSC = new GeneralStatsCollector(
					PRINT_PRONONCATIONS, //PRINT_FIRST_CONJ,
					PRINT_FIFTH_DECL_EXC, PRINT_NON_INFL, PRINT_WITH_REGEXP,
					PPRINT_WITH_PARADIGM, PRINT_WITH_FEATURE,
					PRINT_WITH_FEATURE_DESC, PRINT_PARADIGMS,
					PRINT_OTHER_LEMMAS,	wordlistOut);

			FirstConjStatsCollector firstConjSC = new FirstConjStatsCollector(
					PRINT_FIRST_CONJ_DIRECT, PRINT_FIRST_CONJ_REFL);

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
			if (PRINT_SINGLE_JSON)
				completeJsonOut.write("[\n");

			// Process each node.
			int count = 0;
			int goodCount = 0;
			int noParadigmCount = 0;
			int badCount = 0;
			Node entryNode = dicReader.readNexEntry();
			while (entryNode != null)
			{
				TEntry entry = new TEntry(entryNode);
				genSC.countEntry(entry);
				firstConjSC.countEntry(entry);
				entry.printConsistencyReport();

				if (PRINT_SINGLE_XML) completeXmlOut.writeNode(entry);
				if (PRINT_SINGLE_JSON)
				{
					if (count > 0) completeJsonOut.write(",\n");
					completeJsonOut.write(entry.toJSON());
				}

				// Print out all pronunciations.
				//if (makePronunceList)
				//	for (String p : entry.collectPronunciations())
				//		statsOut.write(p + "\t" + entry.head.lemma.text + "\t" + entry.homId + "\n");
				if (!entry
						.inBlacklist())    // Blacklisted entries are not included in output logs.
				{
					if (entry.hasParadigm() && !entry.hasUnparsedGram())
					{
						if (goodCount > 0) goodOut.write(",\n");
						goodOut.write(entry.toJSON());
						goodCount++;
					}
					else if (!entry.hasParadigm() && !entry.hasUnparsedGram())
					{
						if (noParadigmCount > 0) noParadigmOut.write(",\n");
						noParadigmOut.write(entry.toJSON());
						noParadigmCount++;
					}
					else
					{
						if (badCount > 0) badOut.write(",\n");
						badOut.write(entry.toJSON());
						badCount++;
					}
				}
				entryNode = dicReader.readNexEntry();
				count++;
				if (count % 50 == 0)
					System.out.print("Apstrādātie šķirkļi:\t" + count + "\r");
			}
			System.out.println("Apstrādātie šķirkļi:\t" + count);
			System.out.println("\t* "+ goodCount + " pilnīgi pabeigti;");
			System.out.println("\t* "+ noParadigmCount + " bez paradigmām;");
			System.out.println("\t* "+ badCount + " ar neatpazītām gramatikām.");
			goodOut.write("\n]");
			goodOut.close();
			noParadigmOut.write("\n]");
			noParadigmOut.close();
			badOut.write("\n]");
			badOut.close();
			if (wordlistOut != null) wordlistOut.close();

			System.out.println("Drukā statistiku...");
			BufferedWriter statsOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(statsFile), "UTF-8"));
			genSC.printContents(statsOut);
			firstConjSC.printContents(path, file);
			statsOut.close();
			//if (makePronunceList) statsOut.close();
		}
		if (PRINT_SINGLE_XML) completeXmlOut.finalize();
		if (PRINT_SINGLE_JSON)
		{
			completeJsonOut.write("\n]");
			completeJsonOut.close();
		}
		System.out.println("Viss pabeigts!");
	}
}
