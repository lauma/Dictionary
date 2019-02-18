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
import lv.ailab.dict.tezaurs.analyzer.io.Loaders;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Tēzaura šķirklis, papildināts ar ielādēšanas/apstrādes mehānismiem un
 * melnajiem sarakstiem.
 */
public class TEntry extends Entry
{
	public final static String BLACKLIST_LOCATION = "saraksti/blacklist.txt";
	/**
	 * Lemmas šķirkļiem, kurus šobrīd ignorē (neapstrādā).
	 * Skatīt arī inBlacklist().
	 */
	private static HashMap<String, HashSet<String>> blacklist = initBlacklist();

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

		// Move etymology from gram to its own field.
		else if (head != null && head.gram != null && head.gram.flags != null &&
				head.gram.flags.testKey(TKeys.ETYMOLOGY))
		{
			HashSet<String> etym = head.gram.flags.getAll(TKeys.ETYMOLOGY);
			if (etym.size() > 1)
				System.err.printf("Šķirklī \"%s\" ir vairāki etimoloģijas lauki\n", head.lemma.text);
			etymology = etym.stream().reduce((a, b) -> a + "; " + b).orElse(null);
			head.gram.flags.pairings.removeAll(TKeys.ETYMOLOGY);
		}
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
		HashSet<String> homs = blacklist.get(head.lemma.text);
		if (homs == null) return false;
		if (homs.contains("-1")) return true;
		return homs.contains(homId);
		//return blacklist.contains(head.lemma.text);
	}
	
	/**
	 * Constructing a list of lemmas to ignore - basically meant to ease
	 * development and testing.
	 * Blacklist file format - one word (lemma) per line with one optional space
	 * separated homonym index. No homonym index mean all homonyms.
	 */
	private static HashMap<String, HashSet<String>> initBlacklist()
	{
		HashMap<String, HashSet<String>> blist = new HashMap<>();
		BufferedReader input;
		try {
			// Blacklist file format - one word (lemma) per line with optional
			// space separated homonym index. No homonym index mean all homonyms.
			input = new BufferedReader(
					new InputStreamReader(
					new FileInputStream(BLACKLIST_LOCATION), StandardCharsets.UTF_8));
			String line;
			while ((line = input.readLine()) != null)
			{
				line = line.trim();
				if (line.contains(" "))
				{
					String[] parts = line.split(" ");
					HashSet<String> homs = blist.get(parts[0]);
					if (homs == null) homs = new HashSet<>();
					if (!homs.contains("-1"))homs.add(parts[1]);
					blist.put(parts[0].trim(), homs);
				}
				else blist.put(line, new HashSet<String>(){{add("-1");}});
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
		Flags usedFlags = getUsedFlags();
		if (usedFlags.test(TFeatures.UNCLEAR_PARADIGM)
				&& !hasMultipleParadigms()
				&& !getMentionedParadigms().contains(0))
			System.err.printf(
					"Šķirklī \"%s\" ir neskaidro paradigmu karodziņš, bet nav vairāku paradigmu.\n",
					head.lemma.text);
		if (usedFlags.testKey(TKeys.ETYMOLOGY))
			System.err.printf(
					"Šķirklī \"%s\" ir etimoloģijas karodziņš.\n",
					head.lemma.text);
	}
	public int countEmptyGloss()
	{
		return countEmptyGloss(this);
	}

	public static int countEmptyGloss(Entry e)
	{
		if (e == null) return 0;
		int res = 0;
		if (e.senses != null) for (Sense s : e.senses)
			 res = res + TSense.countEmptyGloss(s);
		if (e.phrases != null) for (Phrase p : e.phrases)
			res = res + TPhrase.countEmptyGloss(p);
		return res;
	}

}
