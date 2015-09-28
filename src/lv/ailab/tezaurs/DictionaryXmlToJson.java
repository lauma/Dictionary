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

import javax.xml.parsers.DocumentBuilderFactory;

import lv.ailab.tezaurs.analyzer.StatsCollector;
import lv.ailab.tezaurs.analyzer.io.StaxReader;
import org.w3c.dom.Node;

import lv.ailab.tezaurs.analyzer.struct.Entry;

public class DictionaryXmlToJson
{

	/**
	 * Create file with all pronunciations.
	 */
	//public static boolean makePronunceList = true;

	/**
	 * 
	 * @param args File name expected as first argument.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		StatsCollector sc = new StatsCollector();

		// Initialize IO.
		String thesaurusFile = args[0];
		String goodOutputFile = "tezaurs-good.json";
		String noParadigm = "tezaurs-noParadigm.json";
		String badOutputFile = "tezaurs-bad.json";
		String statsFile = "tezaurs-stats.txt";

		StaxReader dicReader = new StaxReader(thesaurusFile);

		BufferedWriter goodOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(goodOutputFile), "UTF-8"));
		goodOut.write("[\n");
		BufferedWriter noParadigmOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(noParadigm), "UTF-8"));
		noParadigmOut.write("[\n");
		BufferedWriter badOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(badOutputFile), "UTF-8"));
		badOut.write("[\n");

		System.out.println("Sāk apstrādāt.");
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

			if (!entry.inBlacklist())	// Blacklisted entries are not included in output logs.
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

		System.out.println("Drukā statistiku...");
		BufferedWriter statsOut  = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(statsFile), "UTF-8"));
		sc.printContents(statsOut);
		statsOut.close();
		//if (makePronunceList) statsOut.close();
		System.out.println("Viss pabeigts!");
	}
}
