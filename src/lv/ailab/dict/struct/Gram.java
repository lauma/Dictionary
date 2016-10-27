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


import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Gramatikas lauks.
 * TODO: pārrakstīt toXML metodei, lai iekļautu izejā leftovers.
 * @author Lauma
 */
public class Gram implements HasToJSON, HasToXML
{

	/**
	 * Brīvs gramatikas teksts, kāds tas ir vārdnīcā.
	 * Tēzaura gadījumā tā ir pilna analizējamās gramatikas kopija, MLVV
	 * gadījumā - oficiāli nestrukturējamais.
	 */
	public String freeText;
	/**
	 * No gramatikas izgūtie karodziņi.
	 */
	public Flags flags;

	/**
	 * No šīs gramatikas izsecinātās paradigmas.
	 */
	public HashSet<Integer> paradigm;
	/**
	 * Struktūra, kurā tiek savākta papildus informāciju par citiem šķirkļu
	 * vārdiem / pamatformām (kodā daudzviet saukti par alternatīvajām lemmām),
	 * ja gramatika tādu satur.
	 * Karodziņu kopa satur vienīgi tos karodziņus, kas "alternatīvajai lemmai"
	 * atšķiras no pamata lemmas.
	 * TODO: filtrēt vienādos.
	 * TODO: dublēt karodziņus.
	 */
	public ArrayList<Header> altLemmas;

	/**
	 * Struktūra, kas satur norādes, ka elements, ko šī gramatika skaidro,
	 * attiecas tikai uz noteiktu šķirkļavārdu formu apakškopu. Šis elements
	 * visbiežāk sastopams gramatikās, kas atrodas pie nozīmēm.
	 * Visos saprātīgos gadījumos vajadzētu būt ne vairāk kā 1 šādam Header?
	 */
	public ArrayList<Header> formRestrictions;


	public Gram()
	{
		freeText = null;
		flags = null;
		paradigm = null;
		altLemmas = null;
		formRestrictions = null;
	}

	/**
	 * Atlasa tās paradigmas, kas ir tiešā veidā attiecināmas uz to objektu,
	 * kam šī gramatika ir piekārtota.
	 */
	public HashSet<Integer> getDirectParadigms()
	{
		HashSet<Integer> res = new HashSet<>();
		if (paradigm != null) res.addAll(paradigm);
		return res;
	}

	/**
	 * Visas paradigmas, kas vispār ir pieminētas šajā struktūrā.
	 * Izmantojams meklēšanai un statistikai.
	 */
	public HashSet<Integer> getMentionedParadigms()
	{
		HashSet<Integer> res = new HashSet<>();
		res.addAll(getDirectParadigms());
		for (Header h : getImplicitHeaders())
			res.addAll(h.getMentionedParadigms());
		if (formRestrictions != null && formRestrictions.size() > 0)
			for (Header h : formRestrictions)
				res.addAll(h.getMentionedParadigms());
		return res;
	}

	/**
	 * Savāc altLemmas un, drošības pēc, arī pārbauda vai altLemmu objekti sevī
	 * neietver kādu gramatiku ar altLemmu. Teorētiski tā gan nevajadzētu būt,
	 * bet nu drošības pēc.
	 * TODO: vai šeit vajag iekļaut arī formRestrictions?
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList();
		if (altLemmas != null)
		{
			res.addAll(altLemmas);
			for (Header h : altLemmas)
				res.addAll(h.getImplicitHeaders());
		}
		return res;
	}

	/**
	 * Gramatikas struktūras JSON reprezentācija, kas iekļauj arī sākotnējo
	 * gramatikas tekstu.
	 */
	public String toJSON()
	{
		return toJSON(null, null);
	}

	/**
	 * Reālā JSON reprezentācijas izveides metode, ko konkrētais objekts sauc,
	 * dažus parametrus aizpildot automātiski.
	 * Ātruma problēmu gadījumā, iespējams, jāpāriet uz StringBuilder
	 * atgriešanu.
	 * @param freeTextFieldName	freeText lauka nosaukums
	 * @param additional	JSON-noformēts atslēgu vērtību pārītis, ko pievienot
	 *                      izdrukā.
	 */
	public String toJSON (String freeTextFieldName, String additional)
	{
		StringBuilder res = new StringBuilder();

		res.append("\"Gram\":{");
		boolean hasPrev = false;

		if (paradigm != null && !paradigm.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Paradigm\":");
			res.append(JSONUtils.simplesToJSON(paradigm));
			hasPrev = true;
		}

		if (formRestrictions != null && !formRestrictions.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"FormRestrictions\":");
			res.append(JSONUtils.objectsToJSON(formRestrictions));
			hasPrev = true;
		}

		if (flags != null && !flags.pairings.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Flags\":");
			res.append(JSONUtils.mappingSetToJSON(flags.pairings));
			hasPrev = true;
		}

		if (altLemmas != null && !altLemmas.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"AltLemmas\":");
			res.append(JSONUtils.objectsToJSON(altLemmas));
			hasPrev = true;
		}

		if (freeText != null && freeText.length() > 0)
		{
			if (hasPrev) res.append(", ");
			if (freeTextFieldName == null || freeTextFieldName.trim().isEmpty())
				res.append("\"FreeText\":\"");
			else res.append("\"" + JSONObject.escape(freeTextFieldName) + "\":");
			res.append(JSONObject.escape(freeText));
			res.append("\"");
			hasPrev = true;
		}

		if (additional != null && additional.length() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append(additional);
			hasPrev = true;
		}

		res.append("}");
		return res.toString();
	}

	public void toXML(Node parent)
	{
		toXML(parent, null, null);
	}

	/**
	 * Reālā XML izveidošanas metode, ko konkrētais obekts sauc, dažus
	 * parametrus aizpildot automatiski.
 	 * @param freeTextFieldName	freeText lauka nosaukums
	 * @param additional	XML node ar papildus informāciju.
	 */

	public void toXML(Node parent, String freeTextFieldName, Node additional)
	{
		Document doc = parent.getOwnerDocument();
		Node gramN = doc.createElement("Gram");

		if (paradigm != null && !paradigm.isEmpty())
		{
			Node paradigmContN = doc.createElement("Paradigms");
			for (Integer p : paradigm.stream().sorted().collect(Collectors.toList()))
			{
				Node paradigmN = doc.createElement("Paradigm");
				paradigmN.appendChild(doc.createTextNode(p.toString()));
				paradigmContN.appendChild(paradigmN);
			}
			gramN.appendChild(paradigmContN);
		}

		if (formRestrictions != null && !formRestrictions.isEmpty())
		{
			Node formRestrContN = doc.createElement("FormRestrictions");
			for (Header al: formRestrictions)
				if (al != null) al.toXML(formRestrContN);
			gramN.appendChild(formRestrContN);
		}

		if (flags != null) flags.toXML(gramN);


		if (altLemmas != null && !altLemmas.isEmpty())
		{
			Node altLemmasContN = doc.createElement("AltLemmas");
			for (Header al: altLemmas)
				if (al != null) al.toXML(altLemmasContN); // TODO MLVV kā te var nonākt null?
			gramN.appendChild(altLemmasContN);
		}

		if (freeText != null && !freeText.isEmpty())
		{
			Node origN;
			if (freeTextFieldName == null || freeTextFieldName.trim().isEmpty())
				origN = doc.createElement("FreeText");
			else origN = doc.createElement(freeTextFieldName);
			origN.appendChild(doc.createTextNode(freeText));
			gramN.appendChild(origN);
		}
		if (additional != null)
			gramN.appendChild(additional);

		parent.appendChild(gramN);
	}
}

