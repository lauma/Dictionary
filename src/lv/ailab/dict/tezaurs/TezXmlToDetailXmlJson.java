/*
 *******************************************************************************
 * Copyright 2013-2019 Institute of Mathematics and Computer Science, University of Latvia
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

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.StaxReader;
import lv.ailab.dict.io.XmlEntryStreamWriter;
import lv.ailab.dict.tezaurs.analyzer.stats.FirstConjStatsCollector;
import lv.ailab.dict.tezaurs.analyzer.stats.GeneralStatsCollector;
import lv.ailab.dict.tezaurs.analyzer.gramdata.AltLemmaRules;
import lv.ailab.dict.tezaurs.analyzer.gramdata.DirectRules;
import lv.ailab.dict.tezaurs.analyzer.gramdata.OptHypernRules;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AdditionalHeaderRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.EndingRule;
import lv.ailab.dict.tezaurs.struct.TEntry;
import lv.ailab.dict.utils.Tuple;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Tēzaura gramatiku analīzes rīka ieejas punkts.
 *
 * Parametrus, kas nav immutable, nevajag censties izpildes laikā mainīt, tas
 * ne pie kā laba nenovedīs.
 */
public class TezXmlToDetailXmlJson
{
	public final static class Config
	{
		public final static String[] XML_FILES = {"entries", "references"};
		public final static boolean PRINT_ALL_RULE_STATS = false;
		public final static boolean PRINT_SINGLE_XML = true;
		public final static boolean PRINT_SINGLE_JSON = true;

		// Parametri, kas attiecināmi ur GeneralStatsCollector.
		public final static boolean PRINT_WORDLISTS = true;

		public final static boolean PRINT_PRONONCATIONS = false;
		public final static boolean PRINT_EMPTY_GLOSSES = true;
		public final static boolean PRINT_INFL_WEARDNESS = true;
		public final static boolean PRINT_FIFTH_DECL_EXC = false;
		//public final static boolean PRINT_FIRST_CONJ = false;
		public final static boolean PRINT_NON_INFL = false;

		public final static ArrayList<Integer> PPRINT_WITH_PARADIGM = null;
		/*public static ArrayList<Integer> PPRINT_WITH_PARADIGM = new ArrayList<Integer>() {{
			add (30);
			add (40);
			add (41);
		}};//*/

		public final static Pattern PRINT_WITH_ENTRYWORD = null;
		//public static Pattern PRINT_WITH_ENTRYWORD = Pattern.compile(".*fe");
		public final static Pattern PRINT_WITH_GLOSS = null;
		//public final static Pattern PRINT_WITH_GLOSS = Pattern.compile(".*?(Čehij|Polij|Slovākij|Čehoslovākij).*");

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
		public final static boolean PRINT_FIRST_CONJ_DIRECT = false;
		public final static boolean PRINT_FIRST_CONJ_REFL = false;
	}

	protected int fileCount = 0;
	protected FirstConjStatsCollector firstConjSC;
	protected XmlEntryStreamWriter completeXmlOut;
	protected BufferedWriter completeJsonOut;
	protected BufferedWriter ruleStatsOut;
	protected HashMap<String, Integer> entryCount = new HashMap<>();
	protected HashMap<String, Integer> goodCount = new HashMap<>();
	protected HashMap<String, Integer> noParadigmCount = new HashMap<>();
	protected HashMap<String, Integer> badCount = new HashMap<>();

	public TezXmlToDetailXmlJson(
			String resultXmlPath, String resultJsonPath, String statsPath)
	throws TransformerConfigurationException, IOException
	{
		if (Config.PRINT_SINGLE_XML) completeXmlOut = new XmlEntryStreamWriter(resultXmlPath);
		else completeXmlOut = null;
		if (Config.PRINT_SINGLE_JSON) completeJsonOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(resultJsonPath), StandardCharsets.UTF_8));
		else completeJsonOut = null;
		ruleStatsOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(statsPath), StandardCharsets.UTF_8));
		firstConjSC = new FirstConjStatsCollector(
				Config.PRINT_FIRST_CONJ_DIRECT, Config.PRINT_FIRST_CONJ_REFL);
	}

	/**
	 * @param args pirmais arguments - ceļš uz vietu, kur stāv apstrādājamie XML
	 *             faili
	 */
	public static void main(String[] args)
	throws IOException, TransformerException, SAXException, ParserConfigurationException, DictionaryXmlReadingException, XMLStreamException
	{
		String path = args[0];
		if (!path.endsWith("/") && !path.endsWith("\\"))
			path = path + "\\";
		String ruleStatsPath = path + "rule_stats.txt";
		String completeXmlPath = path + "analyzed_tezaurs.xml";
		String completeJsonPath = path + "analyzed_tezaurs.json";

		TezXmlToDetailXmlJson transformer = new TezXmlToDetailXmlJson(
				completeXmlPath, completeJsonPath, ruleStatsPath);
		for (String file : Config.XML_FILES)
			transformer.processFile(path, file);

		transformer.wrapUpEverything(path);
		System.out.println("Viss pabeigts!");
	}

	protected void processFile(String path, String file)
	throws IOException, XMLStreamException, DictionaryXmlReadingException, ParserConfigurationException, SAXException, TransformerException
	{
		System.out.println("Sāk apstrādāt failu " + file + ".xml.");

		// Initialize IO.
		String inputFile = path + file + ".xml";
		String goodOutputFile = path + file + "-good.json";
		String noParadigm = path + file + "-noParadigm.json";
		String badOutputFile = path + file + "-bad.json";
		String statsFile = path + file + "-stats.txt";

		BufferedWriter wordlistOut = null;
		if (Config.PRINT_WORDLISTS)
		{
			String wordlistFile = path + file + "-feats.txt";
			wordlistOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(wordlistFile), StandardCharsets.UTF_8));
		}
		GeneralStatsCollector genSC = new GeneralStatsCollector(
				Config.PRINT_PRONONCATIONS, Config.PRINT_EMPTY_GLOSSES, //PRINT_FIRST_CONJ,
				Config.PRINT_FIFTH_DECL_EXC, Config.PRINT_NON_INFL,
				Config.PRINT_WITH_ENTRYWORD, Config.PRINT_WITH_GLOSS,
				Config.PPRINT_WITH_PARADIGM, Config.PRINT_WITH_FEATURE,
				Config.PRINT_WITH_FEATURE_DESC, Config.PRINT_PARADIGMS,
				Config.PRINT_OTHER_LEMMAS, Config.PRINT_INFL_WEARDNESS,
				wordlistOut);

		StaxReader dicReader = new StaxReader(inputFile, "tezaurs", "s");

		BufferedWriter goodOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(goodOutputFile), StandardCharsets.UTF_8));
		goodOut.write("[\n");
		BufferedWriter noParadigmOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(noParadigm), StandardCharsets.UTF_8));
		noParadigmOut.write("[\n");
		BufferedWriter badOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(badOutputFile), StandardCharsets.UTF_8));
		badOut.write("[\n");
		if (Config.PRINT_SINGLE_JSON && fileCount == 0)
			completeJsonOut.write("[\n");

		// Process each node.
		entryCount.put(file, 0);
		goodCount.put(file, 0);
		noParadigmCount.put(file, 0);
		badCount.put(file, 0);
		Node entryNode = dicReader.readNexEntry();
		while (entryNode != null)
		{
			TEntry entry = new TEntry(entryNode);
			genSC.countEntry(entry);
			firstConjSC.countEntry(entry);
			entry.printConsistencyReport();
				printEntry(entry, file, goodOut, noParadigmOut, badOut);

			entryNode = dicReader.readNexEntry();
			entryCount.put(file, 1 + entryCount.get(file));
			if (entryCount.get(file) % 50 == 0)
				System.out.print("Apstrādātie šķirkļi:\t" + entryCount.get(file) + "\r");
		}
		System.out.println("Apstrādātie šķirkļi:\t" + entryCount.get(file));
		System.out.println("\t* "+ goodCount.get(file) + " pilnīgi pabeigti;");
		System.out.println("\t* "+ noParadigmCount.get(file) + " bez paradigmām;");
		System.out.println("\t* "+ badCount.get(file) + " ar neatpazītām gramatikām.");
		goodOut.write("\n]");
		goodOut.close();
		noParadigmOut.write("\n]");
		noParadigmOut.close();
		badOut.write("\n]");
		badOut.close();
		if (wordlistOut != null) wordlistOut.close();

		System.out.println("Drukā statistiku...");
		BufferedWriter statsOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(statsFile), StandardCharsets.UTF_8));
		genSC.printContents(statsOut);
		statsOut.close();
		fileCount++;
	}

	protected void printEntry(
			TEntry entry, String fileCode, BufferedWriter goodOut,
			BufferedWriter noParadigmOut, BufferedWriter badOut)
	throws ParserConfigurationException, IOException, TransformerException
	{
		// Blacklisted entries are not included in output logs.
		if (entry.inBlacklist()) return;

		if (Config.PRINT_SINGLE_XML) completeXmlOut.writeNextEntry(entry);
		if (Config.PRINT_SINGLE_JSON)
		{
			if (entryCount.get(fileCode) > 0 || fileCount > 0) completeJsonOut.write(",\n");
			completeJsonOut.write(entry.toJSON());
		}

		if (entry.hasParadigm() && !entry.hasUnparsedGram())
		{
			if (goodCount.get(fileCode) > 0) goodOut.write(",\n");
			goodOut.write(entry.toJSON());
			goodCount.put(fileCode, 1 + goodCount.get(fileCode));
		}
		else if (!entry.hasParadigm() && !entry.hasUnparsedGram())
		{
			if (noParadigmCount.get(fileCode) > 0) noParadigmOut.write(",\n");
			noParadigmOut.write(entry.toJSON());
			noParadigmCount.put(fileCode, 1 + noParadigmCount.get(fileCode));
		}
		else
		{
			if (badCount.get(fileCode) > 0) badOut.write(",\n");
			badOut.write(entry.toJSON());
			badCount.put(fileCode, 1 + badCount.get(fileCode));
		}
	}

	protected void wrapUpEverything(String path)
	throws IOException
	{
		if (Config.PRINT_SINGLE_XML) completeXmlOut.finishFile();
		if (Config.PRINT_SINGLE_JSON)
		{
			completeJsonOut.write("\n]");
			completeJsonOut.close();
		}
		if (Config.PRINT_FIRST_CONJ_DIRECT || Config.PRINT_FIRST_CONJ_REFL)
			System.out.println("Drukā 1. konjugācijas statistikas...");
		firstConjSC.printContents(path, "tezaurs");

		System.out.println("Drukā likumu lietojuma statistikas...");
		printRuleStats();
		ruleStatsOut.close();
	}

	protected void printRuleStats() throws IOException
	{
		ruleStatsOut.write(AltLemmaRules.class.getCanonicalName());
		ruleStatsOut.newLine();
		for (AdditionalHeaderRule[] rules : AltLemmaRules.getAll())
			for (AdditionalHeaderRule r : rules)
				if (r.getUsageCount() == 0 || Config.PRINT_ALL_RULE_STATS)
				{
					ruleStatsOut.write(r.getStrReprezentation().replaceFirst(" ", "\t"));
					ruleStatsOut.write("\t" + r.getUsageCount());
					ruleStatsOut.newLine();
				}

		ruleStatsOut.newLine();
		ruleStatsOut.write(OptHypernRules.class.getCanonicalName());
		ruleStatsOut.newLine();
		for (EndingRule[] rules : OptHypernRules.getAll())
			for (EndingRule r : rules)
				if (r.getUsageCount() == 0 || Config.PRINT_ALL_RULE_STATS)
				{
					ruleStatsOut.write(r.getStrReprezentation().replaceFirst(" ", "\t"));
					ruleStatsOut.write("\t" + r.getUsageCount());
					ruleStatsOut.newLine();
				}
		ruleStatsOut.newLine();
		ruleStatsOut.write(DirectRules.class.getCanonicalName());
		ruleStatsOut.newLine();
		for (EndingRule[] rules : DirectRules.getAll())
			for (EndingRule r : rules)
				if (r.getUsageCount() == 0 || Config.PRINT_ALL_RULE_STATS)
				{
					ruleStatsOut.write(r.getStrReprezentation().replaceFirst(" ", "\t"));
					ruleStatsOut.write("\t" + r.getUsageCount());
					ruleStatsOut.newLine();
				}
	}
}
