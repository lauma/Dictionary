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
import lv.ailab.dict.mlvv.analyzer.struct.MLVVEntry;
import lv.ailab.dict.mlvv.analyzer.Normalizer;
import lv.ailab.dict.struct.Dictionary;
import lv.ailab.dict.struct.Entry;

import java.io.*;

/**
 * Programmas ārējā saskarne. Nodrošina XML izgūšanas rīku darbināšanu.
 * DOC failus lasa ar HWPF, TXT - pa taisno.
 * Izveidots 2016-01-28.
 * @author Lauma
 */
public class StructureExtractor
{
	public static String inputDataPath = "./dati/mlvv/";
	public static String outputDataPath = "./dati/mlvv/xml/";

	public Dictionary dict = new Dictionary();

	public static void main (String[] args)
	{
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
			else extractor.processTxt(f);
		}
		extractor.printResults();
	}

	public void processLine(String line)
	{
		try
		{
			Entry e = MLVVEntry.extractFromString(Normalizer.normalizeLine(line));
			if (e != null) dict.entries.add(e);
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
			String line = in.readLine();
			while (line != null)
			{
				processLine(line);
				line = in.readLine();
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
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + file.getName() + ".txt"), "UTF8")));
			for (String line : lines)
			{
				out.println(line.trim());
				processLine(line);
			}
			out.flush();
			out.close();
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
		try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputDataPath + "mlvv.xml"), "UTF8")));
			dict.toXMLFile(out);
			out.close();
			System.out.println("Viss pabeigts!");
		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt rezultātu XML failā " + outputDataPath + "mlvv.xml!");
			System.out.println("Pabeigts, bet neveiksmīgi.");
		}
	}
}
