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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.utils.CountingSet;
import lv.ailab.tezaurs.utils.Tuple;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.json.simple.JSONObject;

import lv.ailab.tezaurs.utils.HasToJSON;
import lv.ailab.tezaurs.utils.JSONUtils;
import lv.ailab.tezaurs.analyzer.io.Loaders;

/**
 * n (nozīme / nozīmes nianse) field.
 */
public class Sense implements HasToJSON
{
	
	/**
	 * gram field  is optional here.
	 */
	public Gram grammar;
	
	/**
	 * d (definīcija) field.
	 */
	public Gloss gloss;
	
	/**
	 * id field.
	 */
	public String ordNumber;
	
	/**
	 * g_piem (piemēru grupa) field, optional here.
	 */
	
	public LinkedList<Phrase> examples = null;
	/**
	 * g_an (apakšnozīmju grupa) field, optional here.
	 */
	public LinkedList<Sense> subsenses = null;
			
	public Sense ()
	{
		grammar = null;
		gloss = null;
		examples = null;
		subsenses = null;
		ordNumber = null;
	}
	
	/**
	 * @param lemma is used for grammar parsing.
	 */
	public Sense (Node nNode, String lemma)
	{
		NodeList fields = nNode.getChildNodes(); 
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram"))
				grammar = new Gram (field, lemma);
			else if (fieldname.equals("d"))
			{
				NodeList glossFields = field.getChildNodes();
				for (int j = 0; j < glossFields.getLength(); j++)
				{
					Node glossField = glossFields.item(j);
					String glossFieldname = glossField.getNodeName();
					if (glossFieldname.equals("t"))
					{
						if (gloss != null)
							System.err.println("d entry contains more than one \'t\'");
						gloss = new Gloss (glossField);
					}
					else if (!glossFieldname.equals("#text")) // Text nodes here are ignored.
						System.err.printf("d entry field %s not processed\n", glossFieldname);
				}
			}
			else if (fieldname.equals("g_piem"))
				examples = Loaders.loadPhrases(field, lemma, "piem");
			else if (fieldname.equals("g_an"))
				subsenses = Loaders.loadSenses(field, lemma);
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("n entry field %s not processed\n", fieldname);
		}
		ordNumber = ((org.w3c.dom.Element)nNode).getAttribute("nr");
		if ("".equals(ordNumber)) ordNumber = null;
	}
	
	/**
	 * Not sure if this is the best way to treat paradigms.
	 * Currently only grammar paradigm is considered.
	 */
	public boolean hasParadigm()
	{
		if (grammar == null) return false;
		return grammar.paradigmCount() > 0;
		//if (grammar.hasParadigm()) return true;
		//for (Phrase e : examples)
		//{
		//	if (e.hasParadigm()) return true;
		//}
		//for (Sense s : subsenses)
		//{
		//	if (s.hasParadigm()) return true;
		//}
		//return false;
	}

	public boolean glossOnly()
	{
		return gloss != null && grammar == null &&
				(ordNumber == null || ordNumber.equals("")) &&
				(examples == null || examples.isEmpty()) &&
				(subsenses == null || subsenses.isEmpty());
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
		if (grammar!= null && grammar.paradigmCount() > 0)
			paradigms.addAll(grammar.paradigm);
		if (examples != null) for (Phrase e : examples)
			paradigms.addAll(e.getAllMentionedParadigms());
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
		if (examples != null) for (Phrase e : examples)
			flags.addAll(e.getUsedFlags());
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
		if (examples != null) for (Phrase e : examples)
			counts.addAll(e.getFlagCounts());
		if (subsenses != null) for (Sense s : subsenses)
			counts.addAll(s.getFlagCounts());
		return counts;
	}


	public boolean hasUnparsedGram()
	{
		if (grammar != null && grammar.hasUnparsedGram()) return true;
		if (examples != null) for (Phrase e : examples)
		{
			if (e.hasUnparsedGram()) return true;
		}
		if (subsenses != null) for (Sense s : subsenses)
		{
			if (s.hasUnparsedGram()) return true;
		}			
		return false;
	}
	
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		
		boolean hasPrev = false;
		
		if (ordNumber != null)
		{
			res.append("\"SenseID\":\"");
			res.append(JSONObject.escape(ordNumber));
			res.append("\"");
			hasPrev = true;
		}
		
		if (grammar != null)
		{
			if (hasPrev) res.append(", ");
			res.append(grammar.toJSON());
			hasPrev = true;
		}
		
		if (gloss != null)
		{
			if (hasPrev) res.append(", ");
			res.append(gloss.toJSON());
			hasPrev = true;
		}
		
		if (examples != null && !examples.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Examples\":");
			res.append(JSONUtils.objectsToJSON(examples));
			hasPrev = true;
		}
		
		if (subsenses != null && !subsenses.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Senses\":");
			res.append(JSONUtils.objectsToJSON(subsenses));
			hasPrev = true;
		}
		
		return res.toString();
	}
}