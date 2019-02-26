/*
 *******************************************************************************
 * Copyright 2013-2019 Institute of Mathematics and Computer Science, University of Latvia
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
package lv.ailab.dict.llvv;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.StaxReader;
import lv.ailab.dict.llvv.struct.LLVVEntry;
import lv.ailab.dict.struct.Dictionary;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Programmas ārējā saskarne. Nodrošina struktūras transformēšanas rīka
 * darbināšanu.
 * Izveidots 2019-02-19.
 * @author Lauma
 */

public class LlvvXmlToDetailXmlJson
{
	public final static class Config
	{
		public final static boolean PRINT_XML = true;
		public final static boolean PRINT_JSON = true;
	}

	public String inputDataPath;
	public String outputDataPath;
	public Dictionary dict = new Dictionary();

	public LlvvXmlToDetailXmlJson (String inputPath, String outputPath)
	{
		inputDataPath = inputPath;
		if (!inputDataPath.endsWith("/") && !inputDataPath.endsWith("\\"))
			inputDataPath = inputDataPath + "\\";
		outputDataPath = outputPath;
	}

	/**
	 * @param args pirmais arguments - ceļš uz vietu, kur stāv apstrādājamie XML
	 *             faili
	 */
	public static void main(String[] args) throws IOException, DictionaryXmlReadingException, XMLStreamException, ParserConfigurationException, SAXException
	{
		String path = args[0];
		if (!path.endsWith("/") && !path.endsWith("\\"))
			path = path + "\\";
		LlvvXmlToDetailXmlJson transformer = new LlvvXmlToDetailXmlJson(path, path + "result/");
		transformer.loadDictionary();
		transformer.printDictionary();
	}

	protected void loadDictionary () throws IOException, DictionaryXmlReadingException, XMLStreamException, ParserConfigurationException, SAXException
	{
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
		if (listOfFiles != null) for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (f.isDirectory() || f.getName().startsWith("~")) continue;
			if (fileName.endsWith(".xml"))
			{
				System.out.println("Sāk apstrādāt failu " + fileName + ".");
				int fileEntries = 0;
				StaxReader dicReader = new StaxReader(inputDataPath + fileName, "llvv", "s");
				Node entryNode = dicReader.readNexEntry();
				while (entryNode != null)
				{
					LLVVEntry entry = new LLVVEntry(entryNode);
					dict.entries.add(entry);
					entryNode = dicReader.readNexEntry();
					fileEntries++;
					if (fileEntries % 50 == 0)
						System.out.print("Apstrādātie šķirkļi:\t" + fileEntries + "\r");
				}
				System.out.println("Pabeigts.");
			}
			else System.out.println(
					"Ups! Neparedzēta tipa fails \"" + fileName + "\"!");
		}

	}

	protected void printDictionary ()
	{
		System.out.println("Drukā rezultātu...");
		boolean success = true;
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