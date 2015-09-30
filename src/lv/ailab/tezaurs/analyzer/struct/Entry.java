/*******************************************************************************
 * Copyright 2013-2015 Institute of Mathematics and Computer Science, University of Latvia
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
package lv.ailab.tezaurs.analyzer.struct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.json.simple.JSONObject;

import lv.ailab.tezaurs.analyzer.io.Loaders;
import lv.ailab.tezaurs.utils.JSONUtils;

/**
 * Structured representation of entry header.
 */
public class Entry
{
	/**
	 * i field.
	 */
	public String homId;

	/**
	 * avots field.
	 */
	public Sources sources;

	/**
	 * Lemma and all-entry related grammar information.
	 */
	public Header head;

	/**
	 * g_n (nozīmju grupa) field.
	 */
	public LinkedList<Sense> senses;
	
	/**
	 * g_fraz (frazeoloģismu grupa) field.
	 */
	public LinkedList<Phrase> phrases;
	
	/**
	 * g_de (atvasinājumu grupa) field.
	 */
	public LinkedList<Header> derivs;
	
	/**
	 * Lemmas identifying entries currently ignored. See also inBlacklist().
	 */
	private static HashSet<String> blacklist = initBlacklist();

	// Reads data of a single thesaurus entry from the XML format
	public Entry(Node sNode)
	{
		NodeList fields = sNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("v")) // word info
			{
				if (head != null)
					System.err.printf("Šķirklis \"%s\" satur vairāk kā vienu \'v\'!\n", head.lemma.text);
				head = new Header (field);
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				postponed.add(field);
		}
		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("avots")) // source
				sources = new Sources (field);
			else if (fieldname.equals("g_n")) // all senses
				senses = Loaders.loadSenses(field, head.lemma.text);
			else if (fieldname.equals("g_fraz")) //phraseological forms
				phrases = Loaders.loadPhrases(field, head.lemma.text, "fraz");
			else if (fieldname.equals("g_de")) //derived forms
				loadDerivs(field);
			else
				System.err.printf("Šķirklī \"%s\" lauks %s netiek apstrādāts!\n", head.lemma.text, fieldname);
		}
		
		homId = ((org.w3c.dom.Element)sNode).getAttribute("i");
		if ("".equals(homId)) homId = null;
		
		//if (inBlacklist()) return;
		
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
						derivs.add(new Header(derivSubNode));
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
	
	/**
	 * Not sure if this is the best way to treat paradigms.
	 * Currently to trigger true, paradigm must be set for all derivatives and
	 * either for header or at least one sense.
	 */
	public boolean hasParadigm()
	{
		boolean res = head.paradigmCount() > 0;
		//if (head.hasParadigm()) return true;
		if (senses != null) for (Sense s : senses)
		{
			if (s != null && s.hasParadigm()) res = true; //return true;
		}
		//for (Phrase e : phrases)
		//{
		//	if (e.hasParadigm()) return true;
		//}
		
		if (derivs != null) for (Header d : derivs)
		{
			if (d.paradigmCount() <= 0) res = false;
		}
		return res;
	}

	/**
	 * Not sure if this is the best way to treat paradigms.
	 */
	public boolean hasMultipleParadigms()
	{
		return getAllMentionedParadigms().size() > 1;
	}

	/*
	 * For statistical use only. Collects all paradigm numbers mentioned in this
 	 * structure
 	 */
	protected Set<Integer> getAllMentionedParadigms()
	{
		HashSet<Integer> paradigms = new HashSet<>();
		if (head != null && head.paradigmCount() > 0)
			paradigms.addAll(head.gram.paradigm);
		if (senses != null) for (Sense s : senses)
			paradigms.addAll(s.getAllMentionedParadigms());
		if (phrases != null) for (Phrase p : phrases)
			paradigms.addAll(p.getAllMentionedParadigms());
		if (derivs != null) for (Header d : derivs)
			if (d.paradigmCount() > 0) paradigms.addAll(d.gram.paradigm);
		return paradigms;
	}

	/**
	 * Get all flags used in this structure.
	 */
	public Set<String> getUsedFlags()
	{
		HashSet<String> flags = new HashSet<>();
		if (head != null && head.gram != null && head.gram.flags != null)
			flags.addAll(head.gram.flags);
		if (senses != null) for (Sense s : senses)
			flags.addAll(s.getUsedFlags());
		if (phrases != null) for (Phrase p : phrases)
			flags.addAll(p.getUsedFlags());
		if (derivs != null) for (Header d : derivs)
			if (d.gram != null && d.gram.flags != null)
				flags.addAll(d.gram.flags);
		return flags;
	}

	/**
	 * Get all headers - main header + derivatives
	 */
	public ArrayList<Header> getAllHeaders()
	{
		ArrayList<Header> res = new ArrayList<>();
		res.add(head);
		if (derivs != null) res.addAll(derivs);
		return res;
	}

	public boolean hasUnparsedGram()
	{
		if (head != null && head.hasUnparsedGram()) return true;
		if (senses != null) for (Sense s : senses)
		{
			if (s.hasUnparsedGram()) return true;
		}
		if (phrases != null) for (Phrase e : phrases)
		{
			if (e.hasUnparsedGram()) return true;
		}
		if (derivs != null) for (Header h : derivs)
		{
			if (h.hasUnparsedGram()) return true;
		}
		return false;
	}
	
	/**
	 * Collects all pronunciations elements from lemmas and derivatives.
	 */
	public ArrayList<String> collectPronunciations()
	{
		ArrayList<String> res = new ArrayList<> ();
		if (head.lemma.pronunciation != null)
			res.addAll(Arrays.asList(head.lemma.pronunciation));
		if (derivs == null || derivs.isEmpty()) return res;
		for (Header h : derivs)
		{
			if (h.lemma.pronunciation != null)
				res.addAll(Arrays.asList(h.lemma.pronunciation));
		}
		return res;
	}
		
	/**
	 * Build a JSON representation, designed to load in Tezaurs2 webapp well.
	 * @return JSON representation
	 */
	public String toJSON()
	{
		StringBuilder s = new StringBuilder();
		s.append('{');
		s.append(head.toJSON());
		/*if (paradigm != 0) {
			s.append(String.format(",\"Paradigm\":%d", paradigm));
			if (analyzer != null) {
				// generate a list of inflected wordforms and format them as JSON array
				ArrayList<Wordform> inflections = analyzer.generateInflections(lemma.l, paradigm);
				s.append(String.format(",\"Inflections\":%s", formatInflections(inflections) )); 
			}
		}//*/
		
		if (homId != null)
		{
			s.append(", \"ID\":\"");
			s.append(JSONObject.escape(homId));
			s.append("\"");
		}
		
		s.append(", \"Senses\":");
		s.append(JSONUtils.objectsToJSON(senses));
		
		if (phrases != null)
		{
			s.append(", \"Phrases\":");
			s.append(JSONUtils.objectsToJSON(phrases));
		}
		
		if (derivs != null)
		{
			s.append(", \"Derivatives\":");
			s.append(JSONUtils.objectsToJSON(derivs));
		}
		
		if (sources != null && !sources.isEmpty())
		{
			s.append(",");
			s.append(sources.toJSON());
		}		
		s.append('}');
		return s.toString();
	}
	
		
	/**
	 *  Formats a list of inflections as an JSON array.
	 */
/*	private static Object formatInflections(ArrayList<Wordform> inflections) {
		StringBuilder s = new StringBuilder();
		s.append('[');
		
		LinkedList<String> showAttrs = new LinkedList<String>();
		showAttrs.add(AttributeNames.i_Word);
		showAttrs.add(AttributeNames.i_Case);
		showAttrs.add(AttributeNames.i_Number);
		
		Iterator<Wordform> i = inflections.iterator();
		while (i.hasNext()) {
			Wordform wf = i.next();
			wf.filterAttributes(showAttrs);
			s.append(wf.toJSON());
			if (i.hasNext()) s.append(", ");
		}
		s.append(']');
		return s.toString();
	}//*/
	


}
