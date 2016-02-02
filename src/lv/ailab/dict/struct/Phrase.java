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
package lv.ailab.dict.struct;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.utils.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Frāžu un piemēru skaidrojumi.
 * @author Lauma
 */
public class Phrase implements HasToJSON, HasToXML
{
	/**
	 * Skaidrojamā frāze.
	 */
	public String text;

	/**
	 * Neobligātas gramatiskās norādes
	 */
	public Gram grammar;

	/**
	 * Neobligāti skaidrojumi
	 */
	public LinkedList<Sense> subsenses;

	public Phrase()
	{
		text = null;
		grammar = null;
		subsenses = null;
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

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
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

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node phraseN = doc.createElement("phrase");

		if (text != null)
		{
			Node textN = doc.createElement("text");
			textN.appendChild(doc.createTextNode(text));
			phraseN.appendChild(textN);
		}
		if (grammar != null) grammar.toXML(phraseN);
		if (subsenses != null)
		{
			Node sensesContN = doc.createElement("senses");
			for (Sense s : subsenses) s.toXML(sensesContN);
			phraseN.appendChild(sensesContN);
		}
		parent.appendChild(phraseN);
	}
}
