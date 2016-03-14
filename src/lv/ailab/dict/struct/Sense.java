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
import org.json.simple.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Nozīmes, nozīmes nianses vai skaidrojuma lauks.
 * @author Lauma
 */
public class Sense implements HasToJSON, HasToXML
{
	/**
	 * Gramatikas lauks nav obligāts.
	 */
	public Gram grammar;

	/**
	 * Nozīmes, skaidrojuma teksts ("definīcija").
	 */
	public Gloss gloss;

	/**
	 * Nozīmes numurs (ID).
	 */
	public String ordNumber;

	/**
	 * Piemēru, frāžu grupa (neobligāta, Tēzaura XML apzīmēta ar g_piem).
	 */

	public LinkedList<Phrase> examples = null;
	/**
	 * Apakšnozīmju, nozīmes nianšu grupa ( neobligāta, Tēzaura XML apzīmēta ar
	 * g_an).
	 */
	public LinkedList<Sense> subsenses = null;

	public Sense()
	{
		grammar = null;
		gloss = null;
		examples = null;
		subsenses = null;
		ordNumber = null;
	}

	public Sense(String glossText)
	{
		grammar = null;
		gloss = new Gloss(glossText);
		examples = null;
		subsenses = null;
		ordNumber = null;
	}

	/**
	 * Nav īsti skaidrs, vai šis ir labākais veids, kā apieties ar paradigmām.
	 * Šobrīd ņem vērā tikai gramatikas paradigmu.
	 */
	public boolean hasParadigm()
	{
		if (grammar == null) return false;
		return grammar.paradigmCount() > 0;
		//if (grammar.hasParadigm()) return true;
		//for (TPhrase e : examples)
		//{
		//	if (e.hasParadigm()) return true;
		//}
		//for (TSense s : subsenses)
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
	 * Nav īsti skaidrs, vai šis ir labākais veids, kā apieties ar paradigmām.
	 */
	public boolean hasMultipleParadigms()
	{
		return getAllMentionedParadigms().size() > 1;
	}

	/**
	 * Tikai statistiskām vajadzībām! Savāc visus paradigmu skaitlīšus, kas
	 * kaut kur šajā struktūrā parādās.
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
	 * Savāc visus karodziņus, kas kaut kur šajā struktūrā parādās.
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
	 * Savāc visus hederus, kas parādās šajā struktūrā.
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList();
		if (grammar != null) res.addAll(grammar.getImplicitHeaders());
		if (examples != null) for (Phrase e : examples)
			res.addAll(e.getImplicitHeaders());
		if (subsenses != null) for (Sense s : subsenses)
			res.addAll(s.getImplicitHeaders());
		return res;
	}

	/**
	 * Saskaita visus karodziņus, kas kaut kur šajā struktūrā parādās.
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

	/**
	 * Nozīmes numuru iekļauj kā atribūtu, pārējo kā elementus.
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Element senseN = doc.createElement("sense");
		if (ordNumber != null) senseN.setAttribute("senseID", ordNumber);

		if (grammar != null) grammar.toXML(senseN);
		if (gloss != null) gloss.toXML(senseN);
		if (examples != null && !examples.isEmpty())
		{
			Node exContN = doc.createElement("examples");
			for (Phrase e : examples) e.toXML(exContN);
			senseN.appendChild(exContN);
		}

		if (subsenses != null && !subsenses.isEmpty())
		{
			Node subsContN = doc.createElement("subsenses");
			for (Sense subs : subsenses) subs.toXML(subsContN);
			senseN.appendChild(subsContN);
		}
		parent.appendChild(senseN);
	}

}
