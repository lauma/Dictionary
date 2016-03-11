/*******************************************************************************
 * Copyright 2013-2016 Institute of Mathematics and Computer Science, University of Latvia
 * Author: Lauma
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
package lv.ailab.dict.tezaurs.analyzer.struct;

import lv.ailab.dict.struct.*;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.io.Loaders;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tēzaura šķirklis, papildināts ar ielādēšanas/apstrādes mehānismiem un
 * melnajiem sarakstiem.
 */
public class TEntry extends Entry
{
	/**
	 * Lemmas šķirkļiem, kurus šobrīd ignorē (neapstrādā).
	 * Skatīt arī inBlacklist().
	 */
	private static HashSet<String> blacklist = initBlacklist();

	/**
	 * No XML elementam "s" atbilstošā DOM izveido šķirkļa datu struktūru. Tas
	 * ietver arī visu analīzi.
	 * @param sNode XML DOM elements, kas atbilst "s"
	 */
	public TEntry(Node sNode)
	{
		NodeList fields = sNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("v")) // Šķirkļavārda informācija
			{
				if (head != null)
					System.err.printf("Šķirklis \"%s\" satur vairāk kā vienu \'v\'!\n", head.lemma.text);
				head = new THeader(field);
			}
			else if (!fieldname.equals("#text")) // Teksta elementus ignorē, jo šajā vietā ir tikai atstarpjojums.
				postponed.add(field);
		}
		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("avots")) // avoti
				sources = new TSources(field);
			else if (fieldname.equals("g_n")) // visas nozīmes
				senses = Loaders.loadSenses(field, head.lemma.text);
			else if (fieldname.equals("g_fraz")) //frazeoloģiskās vienības
				phrases = Loaders.loadPhrases(field, head.lemma.text, "fraz");
			else if (fieldname.equals("g_de")) //atvasinātās formas
				loadDerivs(field);
			else if (fieldname.equals("ref")) // atsauce uz citu šķirkli
				reference = field.getTextContent();
			else
				System.err.printf("Šķirklī \"%s\" lauks %s netiek apstrādāts!\n", head.lemma.text, fieldname);
		}
		
		homId = ((org.w3c.dom.Element)sNode).getAttribute("i");
		if ("".equals(homId)) homId = null;

		if (head == null)
			System.err.printf("Šķirklis bez šķirkļa vārda / šķirkļa galvas:\n%s\n", sNode.toString());
	}
	
	/**
	 * Process g_de field.
	 * Derived forms - in Lexicon sense, they are separate lexemes, alternate
	 * wordforms but with a link to the same dictionary entry. 
	 */
	private void loadDerivs(Node allDerivs)
	{
		if (derivs == null) derivs = new LinkedList<>();
		NodeList derivNodes = allDerivs.getChildNodes(); 
		for (int i = 0; i < derivNodes.getLength(); i++)
		{
			Node deriv = derivNodes.item(i);
			if (deriv.getNodeName().equals("de"))
			{
				NodeList derivSubNodes = deriv.getChildNodes(); 
				for (int j = 0; j < derivSubNodes.getLength(); j++)
				{
					Node derivSubNode = derivSubNodes.item(j);
					if (derivSubNode.getNodeName().equals("v"))
						derivs.add(new THeader(derivSubNode));
					else if (!derivSubNode.getNodeName().equals("#text")) // Text nodes here are ignored.
						System.err.printf(
							"g_de/de lauks %s netiek apstrādāts, jo tiek sagaidīts 'v'.\n",
							derivSubNode.getNodeName());
				}
			}
			else if (!deriv.getNodeName().equals("#text")) // Text nodes here are ignored.
				System.err.printf(
					"g_de lauks %s netiek apstrādāts, jo tiek sagaidīts 'de'.\n",
					deriv.getNodeName());
		}		
	}	

	public boolean inBlacklist()
	{
		//if (sources == null || !sources.s.contains("LLVV")) return true; // FIXME - temporary restriction to focus on LLVV first
		return blacklist.contains(head.lemma.text);
	}
	
	/**
	 * Constructing a list of lemmas to ignore - basically meant to ease
	 * development and testing.
	 */
	private static HashSet<String> initBlacklist()
	{
		HashSet<String> blist = new HashSet<>();
		BufferedReader input;
		try {
			// Blacklist file format - one word (lemma) per line.
			input = new BufferedReader(
					new InputStreamReader(
					new FileInputStream("blacklist.txt"), "UTF-8"));
			String line;
			while ((line = input.readLine()) != null)
			{
				//if (currentLine.contains("<s>") || currentLine.contains("</s>") || currentLine.isEmpty())
				//	continue;
				blist.add(line.trim());
			}		
			input.close();
		} catch (Exception e)
		{
			System.err.println("Ignorējamo šķirkļu saraksts netiek lietots.");
		} //TODO - any IO issues ignored
		return blist;
	}

	public boolean hasUnparsedGram()
	{
		return TEntry.hasUnparsedGram(this);
	}
	public static boolean hasUnparsedGram(Entry entry)
	{
		if (entry == null) return false;
		if (THeader.hasUnparsedGram(entry.head)) return true;
		if (entry.senses != null) for (Sense s : entry.senses)
		{
			if (TSense.hasUnparsedGram(s)) return true;
		}
		if (entry.phrases != null) for (Phrase p : entry.phrases)
		{
			if (TPhrase.hasUnparsedGram(p)) return true;
		}
		if (entry.derivs != null) for (Header h : entry.derivs)
		{
			if (THeader.hasUnparsedGram(h)) return true;
		}
		return false;
	}

	/**
	 * Pieņemot, ka šķirklis ir apstrādāts un pabeigts, pārbauda dažādas loģikas
	 * lietas, kas varētu norādīt uz kļūdainu programmas loģiku apstrādes
	 * laikā.
	 */
	public void printConsistencyReport()
	{
		if (getUsedFlags().test(Features.UNCLEAR_PARADIGM)
				&& !hasMultipleParadigms()
				&& !getAllMentionedParadigms().contains(0))
			System.err.printf(
					"Šķirklī \"%s\" ir neskaidro paradigmu karodziņš, bet nav vairāku paradigmu.\n",
					head.lemma.text);
	}

}
