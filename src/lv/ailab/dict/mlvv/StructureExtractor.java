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
package lv.ailab.dict.mlvv;

import lv.ailab.dict.io.DocLoader;
import lv.ailab.dict.mlvv.analyzer.PreNormalizer;
import lv.ailab.dict.mlvv.analyzer.validation.Validator;
import lv.ailab.dict.mlvv.analyzer.struct.MLVVEntry;
import lv.ailab.dict.mlvv.analyzer.struct.MLVVGloss;
import lv.ailab.dict.struct.Dictionary;
import lv.ailab.dict.struct.Entry;
import lv.ailab.dict.utils.Trio;

import java.io.*;
import java.util.ArrayList;

/**
 * Programmas ārējā saskarne. Nodrošina XML izgūšanas rīku darbināšanu.
 * DOC failus lasa ar HWPF, TXT - pa taisno.
 * Izveidots 2016-01-28.
 * @author Lauma
 */
public class StructureExtractor
{
	public static String inputDataPath = "./dati/mlvv/";
	public static String outputDataPath = "./dati/mlvv/result/";

	public static boolean UNDERSCORE_FOR_CURSIVE = true;

	public static boolean DEBUG = false;
	public static boolean PRINT_MIDLLE = true;
	public static boolean PRINT_JSON = true;
	public static boolean PRINT_PRONUNCIATION = true;

	public Dictionary dict = new Dictionary();
	public Validator val = new Validator();

	/**
	 * Izruna, šķirkļavārds, šķirkļa homonīma indekss.
	 */
	public ArrayList<Trio<String, String, String>> pronunciations = new ArrayList<>();

	public static void main (String[] args)
	{
		MLVVGloss.UNDERSCORE_FOR_CURSIVE = UNDERSCORE_FOR_CURSIVE;
		MLVVEntry.UNDERSCORE_FOR_ORIGIN_CURSIVE = UNDERSCORE_FOR_CURSIVE;
		MLVVEntry.UNDERSCORE_FOR_NORMATIVE_CURSIVE = UNDERSCORE_FOR_CURSIVE;
		StructureExtractor extractor = new StructureExtractor();
		File folder = new File(inputDataPath);
		if (!folder.exists())
		{
			System.out.println(
					"Ups! Nevar atrast ieejas datu mapi \"" + inputDataPath + "\"!");
			return;
		}

		File dicFolder = new File(outputDataPath);
		if (!dicFolder.exists()) dicFolder.mkdirs();
		File[] listOfFiles = folder.listFiles();
		for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (f.isDirectory() || f.getName().startsWith("~")) continue;
			if (fileName.endsWith(".doc")) extractor.processDoc(f);
			else if (fileName.endsWith(".txt")) extractor.processTxt(f);
			else System.out.println(
						"Ups! Neparedzēta tipa fails \"" + fileName + "\"!");
		}
		extractor.printResults();
		extractor.val.checkAfterAll();
		extractor.val.printStats();
	}

	public void processLine(String line)
	{
		try
		{
			MLVVEntry e = MLVVEntry.parse(PreNormalizer.normalizeLine(line));
			if (e != null)
			{
				dict.entries.add(e);
				if (DEBUG) System.out.println(e.head.lemma.text);
				if (PRINT_PRONUNCIATION)
				{
					String entryword = e.head.lemma.text;
					String homID = e.homId;
					for (String pronun : e.collectPronunciations())
						pronunciations.add(Trio.of(pronun, entryword, homID));
				}
				val.checkEntry(e);
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
			if (PRINT_MIDLLE) middleOut = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + file.getName() + ".txt"), "UTF8")));
			String line = in.readLine();
			while (line != null)
			{
				if (PRINT_MIDLLE)
					middleOut.println(PreNormalizer.normalizeLine(line));
				processLine(line);
				line = in.readLine();
			}
			if (PRINT_MIDLLE)
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
			if (PRINT_MIDLLE) MiddleOut = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + file.getName() + ".txt"), "UTF8")));
			for (String line : lines)
			{
				if (PRINT_MIDLLE)
					MiddleOut.println(PreNormalizer.normalizeLine(line));
				processLine(line);
			}
			if (PRINT_MIDLLE)
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
		boolean success = true;

		if (PRINT_PRONUNCIATION && pronunciations != null) try
		{
			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + "pronun.txt"), "UTF8"));
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

		try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + "mlvv.xml"), "UTF8")));
			dict.toXMLFile(out);
			out.close();

		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt rezultātu XML failā " + outputDataPath + "mlvv.xml!");
			success = false;
		}

		if (PRINT_JSON) try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputDataPath + "mlvv.json"), "UTF-8"));
			out.write("[\n");
			int count = 0;
			for (Entry e : dict.entries)
			{
				if (count > 0) out.write(",\n");
				out.write(((MLVVEntry)e).toJSON());
				count++;
			}
			out.write("\n]");
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
