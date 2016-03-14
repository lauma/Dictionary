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
import lv.ailab.dict.utils.*;

import org.json.simple.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.*;


/**
 * Datu struktūra, kas apraksta vienu vārdnīcas šķirkli un visu, no kā tas
 * sastāv.
 */
public class Entry implements HasToJSON, HasToXML
{
	/**
	 * Homonīma indekss (Tezaura XML - i)
	 */
	public String homId;

	/**
	 * Avotu saraksts, MLVV nelieto.
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
	 * Papildus teksts, kas nu šķirklī var būt vajadzīgs.
	 * Tēzaurā nelieto.
	 */
	public String freeText;

	/**
	 * Atsauce uz citu šķirkli (Tēzaura XML -  ref).
	 * MLVV nelieto.
	 * TODO vai šeit var atdalīti atsevišķi homonīma numuru?
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
	public Set<Integer> getAllMentionedParadigms()
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
	 * Savāc vārdnīcā dotos hederus - galveno + atvasinājumus
	 */
	public ArrayList<Header> getOfficialHeaders()
	{
		ArrayList<Header> res = new ArrayList<>();
		res.add(head);
		if (derivs != null) res.addAll(derivs);
		return res;
	}


	/**
	 * Savāc izsecinātos hederus (altLemmas).
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList<>();
		if (head != null) res.addAll(head.getImplicitHeaders());
		if (senses != null) for (Sense s : senses)
			res.addAll(s.getImplicitHeaders());
		if (phrases != null) for (Phrase p : phrases)
			res.addAll(p.getImplicitHeaders());
		if (derivs != null) for (Header d : derivs)
			res.addAll(d.getImplicitHeaders());
		return res;
	}

	/**
	 * Savāc visus headerus.
	 */
	public ArrayList<Header> getAllHeaders()
	{
		ArrayList<Header> res = new ArrayList<>();
		res.addAll(getOfficialHeaders());
		res.addAll(getImplicitHeaders());
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

		if (phrases != null && !phrases.isEmpty())
		{
			s.append(", \"Phrases\":");
			s.append(JSONUtils.objectsToJSON(phrases));
		}

		if (derivs != null && !derivs.isEmpty())
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
		if (freeText != null && freeText.length() > 0)
		{
			s.append(", \"FreeText\":\"");
			s.append(JSONObject.escape(freeText));
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

	/**
	 * Standarta transformēšanas risinājums mazākām vārdnīcām, paredzēts, lai
	 * visu sabūvētu vienā DOM kokā.
	 * Noklusētais risinājums ir Header pārveidot par XML, neko neizlaižot,
	 * un head.lemma.text uzstādīt par LemmaSign, lai TLex veido sarakstus.
	 * Wrapper metodei toXML(Document).
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node entryN = toXML(doc);
		parent.appendChild(entryN);
	}

	/**
	 * Standarta transformēšanas risinājums lielākām vārdnīcām, paredzēts, lai
	 * dabūtu DOM nodi, kas reprezentē šo šķirkli. Ērts Tēzauram.
	 * Noklusētais risinājums ir Header pārveidot par XML, neko neizlaižot,
	 * un head.lemma.text uzstādīt par LemmaSign, lai TLex veido sarakstus.
	 * Šis ir tas, ko vajag pārrakstīt, ja grib, lai šķirklis drukātos savādāk.
	 * Šķirkļa ķermenis tiek realizēts ar contentsToXML().
	 */
	public Element toXML(Document doc)
	{
		Element entryN = doc.createElement("entry");

		if (homId != null)
			entryN.setAttribute("ID", homId);
		if (head != null)
		{
			head.toXML(entryN);
			if (head.lemma != null && head.lemma.text != null)
				entryN.setAttribute("LemmaSign", head.lemma.text);
		}
		contentsToXML(entryN);

		return entryN;
	}

	/**
	 * Palīgmetode, kas vecākvirsotnei piekabina nozīmes, frāzes, atvasinājumus
	 * un atsauces, kas realizētas šajā objektā.
	 */
	public void contentsToXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (senses != null && !senses.isEmpty())
		{
			Node sensesContN = doc.createElement("senses");
			for (Sense s : senses) s.toXML(sensesContN);
			parent.appendChild(sensesContN);
		}
		if (phrases != null && !phrases.isEmpty())
		{
			Node phrasesContN = doc.createElement("phrases");
			for (Phrase p : phrases) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}
		if (derivs != null && !derivs.isEmpty())
		{
			Node derivContN = doc.createElement("derivatives");
			for (Header d : derivs) d.toXML(derivContN);
			parent.appendChild(derivContN);
		}
		if (reference != null && reference.length() > 0)
		{
			Node refN = doc.createElement("reference");
			refN.appendChild(doc.createTextNode(reference));
			parent.appendChild(refN);
		}
		if (freeText != null && freeText.length() > 0)
		{
			Node freeTextN = doc.createElement("freeText");
			freeTextN.appendChild(doc.createTextNode(freeText));
			parent.appendChild(freeTextN);
		}
		if (sources != null && !sources.isEmpty()) sources.toXML(parent);

	}
}
