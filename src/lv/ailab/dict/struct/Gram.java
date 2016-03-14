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


import java.util.*;
import java.util.stream.Collectors;

import lv.ailab.dict.utils.*;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Gramatikas lauks.
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
	 */
	public ArrayList<Header> altLemmas;


	public Gram()
	{
		freeText = null;
		flags = null;
		paradigm = null;
		altLemmas = null;
	}

	public int paradigmCount()
	{
		if (paradigm == null) return 0;
		return paradigm.size();
	}

	/**
	 * Savāc altLemmas un, drošības pēc, arī pārbauda vai altLemmu objekti sevī
	 * neietver kādu gramatiku ar altLemmu. Teorētiski tā gan nevajadzētu būt,
	 * bet nu drošības pēc.
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
			return toJSON(true, null);
		}

	/**
	 * Izveido JSON reprezentāciju.
	 * Ātruma problēmu gadījumā, iespējams, jāpāriet uz StringBuilder
	 * atgriešanu.
	 * @param printOrig vai izdrukā iekļaut oriģinālo tekstu?
	 */
	public String toJSON (boolean printOrig, String additional)
	{
		return toJSON(paradigm, altLemmas, flags, freeText, printOrig, additional);
	}

	/**
	 * Izveido JSON reprezentāciju TGram elementa stilā no datiem, kas padoti no
	 * ārpuses.
	 * Ātruma problēmu gadījumā, iespējams, jāpāriet uz StringBuilder
	 * atgriešanu.
	 */
	public static String toJSON (HashSet<Integer> paradigm, Flags flags)
	{
		return toJSON(paradigm, null, flags, "", false, null);
	}
	/**
	 * Izveido JSON reprezentāciju Gram elementa stilā no padotiem datiem.
	 * Iekšējai lietošanai.
	 * Ātruma problēmu gadījumā, iespējams, jāpāriet uz StringBuilder
	 * atgriešanu.
	 * @param printFreeText vai izdrukā iekļaut nestrukturēto tekstu?
	 */
	protected static String toJSON (
			Set<Integer> paradigm, List<Header> altLemmas, Flags flags, String orig,
			boolean printFreeText, String additional)
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

		if (altLemmas != null && !altLemmas.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"AltLemmas\":");
			res.append(JSONUtils.objectsToJSON(altLemmas));
			/*Iterator<Header> it = altLemmas.iterator();
			while (it.hasNext())
			{
				Header next = it.next();
				if (!altLemmas.getAll(next).isEmpty())
				{
					res.append("\"");
					res.append(JSONObject.escape(next.toString()));
					res.append("\":[");
					Iterator<Tuple<Lemma, Flags>> flagIt = altLemmas.getAll(next).iterator();
					while (flagIt.hasNext())
					{
						Tuple<Lemma, Flags> alt = flagIt.next();
						//res.append("{");
						//res.append(alt.first.toJSON());
						if (alt.second != null && !alt.second.pairings.isEmpty())
						{
							res.append(Header.toJSON(alt.first, next, alt.second, false));
							//res.append(", ");
							//res.append("\"Flags\":");
							//res.append(JSONUtils.mappingSetToJSON(alt.second.pairings));
						}
						//res.append("}");
						if (flagIt.hasNext()) res.append(", ");
					}

					res.append("]");
					if (it.hasNext()) res.append(", ");
				}
			}
			res.append("}");*/
			hasPrev = true;
		}

		if (flags != null && !flags.pairings.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Flags\":");
			res.append(JSONUtils.mappingSetToJSON(flags.pairings));
			hasPrev = true;
		}

		if (printFreeText && orig != null && orig.length() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append("\"FreeText\":\"");
			res.append(JSONObject.escape(orig));
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
		toXML(parent, paradigm, altLemmas, flags, freeText,
				true);
	}

	/**
	 * Iekšējai lietošanai - reālā XML izveidošanas metode, ko no ārpuses sauc,
	 * dažus parametrus aizpildot automatiski.
	 * @param printFreeText	vai izdrukāt nestrukturēto tekstu, ja tāds ir dots
	 */
	protected static void toXML(
			Node parent, Set<Integer> paradigm, List<Header> altLemmas, Flags flags,
			String orig, boolean printFreeText)
	{
		Document doc = parent.getOwnerDocument();
		Node gramN = doc.createElement("gram");

		if (paradigm != null && !paradigm.isEmpty())
		{
			Node paradigmContN = doc.createElement("paradigms");
			for (Integer p : paradigm.stream().sorted().collect(Collectors.toList()))
			{
				Node paradigmN = doc.createElement("paradigm");
				paradigmN.appendChild(doc.createTextNode(p.toString()));
				paradigmContN.appendChild(paradigmN);
			}
			gramN.appendChild(paradigmContN);
		}

		if (altLemmas != null && !altLemmas.isEmpty())
		{
			Node altLemmasContN = doc.createElement("altLemmas");
			for (Header al: altLemmas)
				al.toXML(altLemmasContN);
			gramN.appendChild(altLemmasContN);
		}

		if (flags != null) flags.toXML(gramN);

		if (printFreeText && orig != null && !orig.isEmpty())
		{
			Node origN = doc.createElement("freeText");
			origN.appendChild(doc.createTextNode(orig));
			gramN.appendChild(origN);
		}

		parent.appendChild(gramN);
	}


}

