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
package lv.ailab.dict.struct;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.utils.CountingSet;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.JSONUtils;
import lv.ailab.dict.utils.Tuple;

import org.json.simple.JSONObject;

import java.util.*;


/**
 * Datu struktūra, kas apraksta vienu vārdnīcas šķirkli un visu, no kā tas
 * sastāv.
 */
public class Entry implements HasToJSON
{
	/**
	 * Homonīma indekss (Tezaura XML - i)
	 */
	public String homId;

	/**
	 * Avotu saraksts
	 */
	public Sources sources;

	/**
	 * Lemma un uz visu šķirkli attiecināmā gramatika (šķirkļa galva).
	 */
	public Header head;

	/**
	 * Nozīmes (Tēzaura XML - g_n).
	 */
	public LinkedList<Sense> senses;

	/**
	 * Patstāvīgu vārdu savienojumu skaidrojumi (Tēzaura XML - g_fraz).
	 */
	public LinkedList<Phrase> phrases;

	/**
	 * Atvasinājumi un "negalvenie" šķirkļavārdi (Tēzaura XML - g_de).
	 */
	public LinkedList<Header> derivs;

	/**
	 * Atsauce uz citu šķirkli (Tēzaura XML -  ref).
	 */
	public String reference;

	/**
	 * Nav īsti skaidrs, vai šis ir labākais veids, kā apieties ar paradigmām.
	 * Šobrīd, lai iestātos true, paradigmai jābūt uzstdītai visiem
	 * atvasinājumiem un vai nu šķirkļa galvai  vai vismaz vienai nozīmei.
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
	 * Nav īsti skaidrs, vai šis ir labākais veids, kā apieties ar paradigmām.
	 */
	public boolean hasMultipleParadigms()
	{
		return getAllMentionedParadigms().size() > 1;
	}

	/**
	 * Vai šim šķirklim ir atsauce uz citu šķirkli?
	 */
	public boolean hasReference()
	{
		return (reference != null && reference.length() > 0);
	}

	/**
	 * Vai šķirklim ir saturīgs saturs, kas nav reference.
	 */
	public boolean hasContents()
	{
		return (senses != null || phrases != null || derivs != null);
	}

	/*
	 * Tikai statistiskām vajadzībām! Savāc visus paradigmu skaitlīšus, kas
	 * kaut kur šajā struktūrā parādās.
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
	 * Savāc visus karodziņus, kas kaut kur šajā struktūrā parādās.
	 */
	public Flags getUsedFlags()
	{
		Flags flags = new Flags();
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
	 * Count all flags used in this structure.
	 */
	public CountingSet<Tuple<Keys, String>> getFlagCounts()
	{
		CountingSet<Tuple<Keys, String>> counts = new CountingSet<>();
		if (head != null && head.gram != null && head.gram.flags != null)
			head.gram.flags.count(counts);

		if (derivs != null) for (Header d : derivs)
			if (d.gram != null && d.gram.flags != null)
				d.gram.flags.count(counts);
		return counts;
	}

	/**
	 * Savāc visus hederus - galveno + atvasinājumus
	 */
	public ArrayList<Header> getAllHeaders()
	{
		ArrayList<Header> res = new ArrayList<>();
		res.add(head);
		if (derivs != null) res.addAll(derivs);
		return res;
	}

	/**
	 * Savāc visas izrunas no pamata lemmas un atvasinājumiem.
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
	 * Uzbūvē JSON reprezentāciju.
	 * TODO nebūvēt manuāli
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

		if (senses != null && !senses.isEmpty())
		{
			s.append(", \"Senses\":");
			s.append(JSONUtils.objectsToJSON(senses));
		}

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
		if (reference != null && reference.length() > 0)
		{
			s.append(", \"Reference\":\"");
			s.append(JSONObject.escape(reference));
			s.append("\"");
		}
		if (sources != null && !sources.isEmpty())
		{
			s.append(",");
			s.append(sources.toJSON());
		}
		s.append('}');
		return s.toString();
	}

}
