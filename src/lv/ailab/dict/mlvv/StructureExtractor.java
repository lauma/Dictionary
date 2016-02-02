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

import lv.ailab.dict.mlvv.analyzer.Extractor;
import lv.ailab.dict.mlvv.analyzer.Normalizer;
import lv.ailab.dict.struct.Dictionary;
import lv.ailab.dict.struct.Entry;

import java.io.*;

/**
 * Programmas ārējā saskarne. Nodrošina XML izgūšanas rīku darbināšanu.
 * Izveidots 2016-01-28.
 * @author Lauma
 */
public class StructureExtractor
{
	public static String inputDataPath = "./dati/mlvv/";
	public static String outputDataPath = "./dati/mlvv/xml/";

	public static void main (String[] args)
	{
		File folder = new File(inputDataPath);
		if (!folder.exists())
		{
			System.out.println(
					"Ups! Nevar atrast ieejas datu mapi \"" + inputDataPath + "\"!");
			return;
		}

		Dictionary dict = new Dictionary();
		File dicFolder = new File(outputDataPath);
		if (!dicFolder.exists()) dicFolder.mkdirs();
		File[] listOfFiles = folder.listFiles();
		for (File f : listOfFiles)
		{
			String fileName = f.getName();
			//if (!fileName.endsWith(".txt")) continue;
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(inputDataPath + fileName));
				String line = in.readLine();
				while (line != null)
				{
					Entry e = Extractor.extractFromString(Normalizer.normalizeLine(line));
					if (e != null) dict.entries.add(e);
					line = in.readLine();
				}
				System.out.println(fileName + " [Pabeigts]");
			} catch (Exception e)
			{
				e.printStackTrace(System.err);
				System.out.println(fileName + " [Problēma]");
			}
		}
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
