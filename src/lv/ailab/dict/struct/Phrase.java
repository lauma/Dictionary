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

import lv.ailab.dict.utils.*;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Frāžu un piemēru skaidrojumi.
 * @author Lauma
 */
public class Phrase implements HasToJSON, HasToXML
{
	/**
	 * Skaidrojamā frāze - parasti viena, taču reizēm var būt vairākas.
	 */
	public LinkedList<String> text;

	/**
	 * Neobligātas gramatiskās norādes.
	 */
	public Gram grammar;

	/**
	 * Neobligāti skaidrojumi
	 */
	public LinkedList<Sense> subsenses;

	/**
	 * Neobligāts tips (netiek lietots Tēzaurā).
	 */
	public String type;

	/**
	 * Neobligāts autors vai avots (netiek lietots Tēzaurā).
	 */
	public String source;


	public Phrase()
	{
		text = null;
		grammar = null;
		subsenses = null;
	}

	/**
	 * Tikai statistiskām vajadzībām! Savāc visas paradigmas, kas šajā truktūrā
	 * ir pieminētas.
 	 */
	protected Set<Integer> getMentionedParadigms()
	{
		HashSet<Integer> paradigms = new HashSet<>();
		if (grammar != null)
			paradigms.addAll(grammar.getMentionedParadigms());
		if (subsenses != null) for (Sense s : subsenses)
			paradigms.addAll(s.getMentionedParadigms());
		return paradigms;
	}

	/**
	 * Savāc visus karodziņus, kas ir lietoti šajā stuktūrā.
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
	 * Savāc visus hederus, kas parādās šajā struktūrā.
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList();
		if (grammar != null) res.addAll(grammar.getImplicitHeaders());
		if (subsenses != null) for (Sense s : subsenses)
			res.addAll(s.getImplicitHeaders());
		return res;
	}


	/**
	 * Saskaita visus karodziņus, kas lietoti šajā struktūrā.
	 */
	public CountingSet<Tuple<String, String>> getFlagCounts()
	{
		CountingSet<Tuple<String, String>> counts = new CountingSet<>();

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

		if (type != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Type\":\"");
			res.append(JSONObject.escape(type));
			res.append("\"");
			hasPrev = true;
		}

		if (text != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Text\":[");
			res.append(text.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
				.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			res.append("]");
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

		if (source != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Source\":\"");
			res.append(JSONObject.escape(source));
			res.append("\"");
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
		Element phraseN = doc.createElement("Phrase");
		if (type != null) phraseN.setAttribute("Type", type);
		if (text != null)
		{
			Node textN = doc.createElement("Text");
			for (String var : text)
			{
				Node textVar = doc.createElement("Variant");
				textVar.appendChild(doc.createTextNode(var));
				textN.appendChild(textVar);
			}
			//textN.appendChild(doc.createTextNode(text));
			phraseN.appendChild(textN);
		}
		if (grammar != null) grammar.toXML(phraseN);
		if (subsenses != null)
		{
			Node sensesContN = doc.createElement("Senses");
			for (Sense s : subsenses) s.toXML(sensesContN);
			phraseN.appendChild(sensesContN);
		}
		if (source != null)
		{
			Node sourceN = doc.createElement("Source");
			sourceN.setTextContent(source);
			phraseN.appendChild(sourceN);
		}
		parent.appendChild(phraseN);
	}
}
