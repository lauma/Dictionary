/*******************************************************************************
 * Copyright 2013, 2014 Institute of Mathematics and Computer Science, University of Latvia
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
package lv.ailab.tezaurs.analyzer.struct;

import java.util.*;

import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.utils.CountingSet;
import lv.ailab.tezaurs.utils.Tuple;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.json.simple.JSONObject;

import lv.ailab.tezaurs.utils.HasToJSON;
import lv.ailab.tezaurs.utils.JSONUtils;

/**
 * piem (piemērs) and fraz (frazeoloģisms) fields.
 */
public class Phrase implements HasToJSON
{
	/**
	 * t (teksts) field.
	 */
	public String text;		

	/**
	 * gram field  is optional here.
	 */
	public Gram grammar;

	/**
	 * n field is optional here.
	 */
	public LinkedList<Sense> subsenses;
	
	public Phrase()
	{
		text = null;
		grammar = null;
		subsenses = null;
	}

	public Phrase (Node piemNode, String lemma)
	{
		text = null;
		grammar = null;
		subsenses = null;
		NodeList fields = piemNode.getChildNodes(); 
		for (int i = 0; i < fields.getLength(); i++) {
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("t"))
				text = field.getTextContent();
			else if (fieldname.equals("gram"))
				grammar = new Gram (field, lemma);
			else if (fieldname.equals("n"))
			{
				if (subsenses == null) subsenses = new LinkedList<>();
				Sense newMade = new Sense (field, lemma);
				if (newMade.gloss.text.matches("\\(a\\).*?\\(b\\).*"))
				{
					if (!newMade.glossOnly())
						System.err.println("Trying to seperate piem gloss in multiple senses despite nonempty other fields.");
					String text = newMade.gloss.text;
					Integer nextOrd = 1;
					String ids = "abcdefghijklmnop";
					while(text.startsWith("(" + ids.charAt(0) + ")") &&
							text.contains("(" + ids.charAt(1) + ")"))
					{
						newMade.ordNumber = nextOrd.toString();
						// Te principā varētu pārtaisīt pirmo burtu uz lielo,
						// bet es neriskēju - ja nu ir kas specifisks?
						// Saīsinājums vai kas tāds?
						newMade.gloss = new Gloss(
								text.substring(3, text.indexOf("(" + ids.charAt(1) + ")")).trim());
						subsenses.add(newMade);
						text = text.substring(text.indexOf("(" + ids.charAt(1) + ")"));
						newMade = new Sense();
						nextOrd++;
						ids = ids.substring(1);
					}
					// Te principā varētu pārtaisīt pirmo burtu uz lielo, bet es
					// neriskēju - ja nu ir kas specifisks? Saīsinājums vai kas
					// tāds?
					newMade.gloss = new Gloss(text.substring(3).trim());
					newMade.ordNumber = nextOrd.toString();
					subsenses.add(newMade);

				} else subsenses.add(newMade);

				//if (newMade.grammar != null)
				//	System.out.println("Ir gramatika! " + field.toString());
				//if (newMade.examples != null)
				//	System.out.println("Ir piemēri! " + field.toString());
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("piem entry field %s not processed\n", fieldname);
		}			
	}
	
	/**
	 * Not sure if this is the best way to treat paradigms.
	 * Currently to trigger true, paradigm must be set for either header or
	 * at least one sense.
	 */
	public boolean hasParadigm()
	{
		if (grammar != null && grammar.paradigmCount() > 0) return true;
		if (subsenses != null) for (Sense s : subsenses)
		{
			if (s.hasParadigm()) return true;
		}
		return false;
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
		if (grammar != null && grammar.paradigmCount() > 0)
			paradigms.addAll(grammar.paradigm);
		if (subsenses != null) for (Sense s : subsenses)
			paradigms.addAll(s.getAllMentionedParadigms());
		return paradigms;
	}

	/**
	 * Get all flags used in this structure.
	 */
	public Flags getUsedFlags()
	{
		Flags flags = new Flags();
		if (grammar != null && grammar.flags != null)
			flags.addAll(grammar.flags);
		if (subsenses != null) for (Sense s : subsenses)
			flags.addAll(s.getUsedFlags());
		return flags;
	}

	/**
	 * Count all flags used in this structure.
	 */
	public CountingSet<Tuple<Keys, String>> getFlagCounts()
	{
		CountingSet<Tuple<Keys, String>> counts = new CountingSet<>();

		if (grammar != null && grammar.flags != null)
			grammar.flags.count(counts);
		if (subsenses != null) for (Sense s : subsenses)
			counts.addAll(s.getFlagCounts());
		return counts;
	}

	public boolean hasUnparsedGram()
	{
		if (grammar != null && grammar.hasUnparsedGram()) return true;
		if (subsenses != null) for (Sense s : subsenses)
		{
			if (s.hasUnparsedGram()) return true;
		}			
		return false;
	}
	
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		
		//res.append("\"Phrase\":{");
		boolean hasPrev = false;
		
		if (text != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Text\":\"");
			res.append(JSONObject.escape(text));
			res.append("\"");
			hasPrev = true;
		}	
		
		if (grammar != null)
		{
			if (hasPrev) res.append(", ");
			res.append(grammar.toJSON());
			hasPrev = true;
		}
		
		if (subsenses != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Senses\":");
			res.append(JSONUtils.objectsToJSON(subsenses));
			hasPrev = true;
		}
		
		//res.append("}");
		return res.toString();
	}
}