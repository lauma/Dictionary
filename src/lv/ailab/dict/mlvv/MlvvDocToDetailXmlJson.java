package lv.ailab.dict.mlvv;

import lv.ailab.dict.io.DocLoader;
import lv.ailab.dict.mlvv.analyzer.docparser.EntryParser;
import lv.ailab.dict.mlvv.analyzer.FlagStringCollector;
import lv.ailab.dict.mlvv.analyzer.PreNormalizer;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.mlvv.struct.MLVVEntry;
import lv.ailab.dict.mlvv.struct.MLVVGloss;
import lv.ailab.dict.mlvv.analyzer.validation.Validator;
import lv.ailab.dict.struct.Dictionary;
import lv.ailab.dict.utils.Trio;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Programmas ārējā saskarne. Nodrošina XML izgūšanas rīku darbināšanu.
 * DOC failus lasa ar HWPF, TXT - pa taisno.
 * Izveidots 2016-01-28.
 * @author Lauma
 */
public class MlvvDocToDetailXmlJson
{
	public static final class Config
	{
		public static boolean DEBUG = false;
		public static boolean PRINT_MIDLLE = false;
		public static boolean PRINT_XML = true;
		public static boolean PRINT_JSON = true;
		public static boolean PRINT_PRONUNCIATION = true;
		public static boolean PRINT_FLAGSTRINGS = true;

		public static boolean UNDERSCORE_FOR_CURSIVE = false;
	}

	public String inputDataPath; //= "./dati/mlvv/";
	public String outputDataPath; //= "./dati/mlvv/result/";

	public Dictionary dict = MLVVElementFactory.me().getNewDictionary();
	public Validator val = new Validator();
	public FlagStringCollector flags = new FlagStringCollector();

	/**
	 * Izruna, šķirkļavārds, šķirkļa homonīma indekss.
	 */
	public ArrayList<Trio<String, String, String>> pronunciations = new ArrayList<>();

	public MlvvDocToDetailXmlJson(String inputPath, String outputPath)
	{
		inputDataPath = inputPath;
		outputDataPath = outputPath;
		File dicFolder = new File(outputDataPath);
		if (!dicFolder.exists()) dicFolder.mkdirs();
	}

	/**
	 * @param args pirmais arguments - ceļš uz vietu, kur stāv apstrādājamie XML
	 *             faili
	 */
	public static void main (String[] args)
	{
		String path = args[0];
		if (!path.endsWith("/") && !path.endsWith("\\"))
			path = path + "\\";
		File folder = new File(path);
		if (!folder.exists())
		{
			System.out.println(
					"Ups! Nevar atrast ieejas datu mapi \"" + path + "\"!");
			return;
		}
		MLVVGloss.UNDERSCORE_FOR_CURSIVE = Config.UNDERSCORE_FOR_CURSIVE;
		MLVVEntry.UNDERSCORE_FOR_ETYMOLOGY_CURSIVE = Config.UNDERSCORE_FOR_CURSIVE;
		MLVVEntry.UNDERSCORE_FOR_NORMATIVE_CURSIVE = Config.UNDERSCORE_FOR_CURSIVE;
		MlvvDocToDetailXmlJson extractor = new MlvvDocToDetailXmlJson(path, path + "result/");

		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (f.isDirectory() || f.getName().startsWith("~")) continue;
			if (fileName.endsWith(".doc")) extractor.processDoc(f);
			else if (fileName.endsWith(".txt")) extractor.processTxt(f);
			else System.out.println(
						"Ups! Neparedzēta tipa fails \"" + fileName + "\"!");
		}
		extractor.val.checkAfterAll();
		extractor.printResults();
	}

	public void processLine(String line)
	{
		try
		{
			MLVVEntry e = EntryParser.me().parse(PreNormalizer.normalizeLine(line));
			if (e != null)
			{
				dict.entries.add(e);
				if (Config.DEBUG) System.out.println(e.head.lemma.text);
				if (Config.PRINT_PRONUNCIATION)
				{
					String entryword = e.head.lemma.text;
					String homID = e.homId;
					for (String pronun : e.collectPronunciations())
						pronunciations.add(Trio.of(pronun, entryword, homID));
				}
				val.checkEntry(e);
				flags.addFromEntry(e);
			}

		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdevās apstrādāt šādu rindu:" + line);
		}
	}

	public void processTxt(File file)
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputDataPath + file.getName()), "UTF8"));
			PrintWriter middleOut = null;
			if (Config.PRINT_MIDLLE) middleOut = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + file.getName() + ".txt"), "UTF8")));
			String line = in.readLine();
			while (line != null)
			{
				if (Config.PRINT_MIDLLE)
					middleOut.println(PreNormalizer.normalizeLine(line));
				processLine(line);
				line = in.readLine();
			}
			if (Config.PRINT_MIDLLE)
			{
				middleOut.flush();
				middleOut.close();
			}
			System.out.println(file.getName() + " [Pabeigts]");
		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println(file.getName() + " [TXT problēma]");
		}
	}

	public void processDoc(File file)
	{
		try
		{
			String[] lines = DocLoader.loadDoc(file.getPath());
			PrintWriter MiddleOut = null;
			if (Config.PRINT_MIDLLE) MiddleOut = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + file.getName() + ".txt"), "UTF8")));
			for (String line : lines)
			{
				if (Config.PRINT_MIDLLE)
					MiddleOut.println(PreNormalizer.normalizeLine(line));
				processLine(line);
			}
			if (Config.PRINT_MIDLLE)
			{
				MiddleOut.flush();
				MiddleOut.close();
			}
			System.out.println(file.getName() + " [Pabeigts]");
		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println(file.getName() + " [DOC problēma]");
		}
	}

	public void printResults()
	{
		System.out.println("Drukā rezultātu...");
		val.printStats();
		boolean success = true;

		if (Config.PRINT_PRONUNCIATION && pronunciations != null) try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputDataPath + "pronun.txt"),
					StandardCharsets.UTF_8));
			for (Trio<String, String, String> p : pronunciations)
			{
				out.write(p.first + "\t" + p.second + "\t" + p.third + "\n");
			}
			out.close();
		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt izrunas failā " + outputDataPath + "pronun.txt!");
			success = false;
		}

		if (Config.PRINT_FLAGSTRINGS) try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputDataPath + "flags.txt"),
					StandardCharsets.UTF_8));
			flags.printToFile(out);
			out.close();
		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt karodziņu tekstus failā " + outputDataPath + "flags.txt!");
			success = false;
		}

		if (Config.PRINT_XML) try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputDataPath + "mlvv.xml"),
					StandardCharsets.UTF_8)));
			dict.toXMLFile(out);
			out.close();

		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt rezultātu XML failā " + outputDataPath + "mlvv.xml!");
			success = false;
		}

		if (Config.PRINT_JSON) try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputDataPath + "mlvv.json"),
					StandardCharsets.UTF_8));
			dict.toJSONFile(out);
			out.close();

		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt rezultātu JSON failā " + outputDataPath + "mlvv.json!");
			success = false;
		}

		if (success)
			System.out.println("Viss pabeigts!");
		else System.out.println("Pabeigts, bet neveiksmīgi.");
	}
}
