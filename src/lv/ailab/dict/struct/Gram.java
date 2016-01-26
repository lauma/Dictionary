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


import java.util.HashSet;
import java.util.Iterator;

import lv.ailab.dict.utils.HasToJSON;
import org.json.simple.JSONObject;

import lv.ailab.dict.utils.MappingSet;
import lv.ailab.dict.utils.Tuple;
import lv.ailab.dict.utils.JSONUtils;

/**
 * Gramatikas lauks.
 * @author Lauma
 */
public class Gram implements HasToJSON
{

	/**
	 * Gramatikas teksts kāds tas ir vārdnīcā.
	 */
	public String orig;
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
	 * Kartējums no paradigmas uz lemmas/karodziņu kopas pārīšiem. Karodziņu
	 * kopa satur vienīgi tos karodziņus, kas "alternatīvajai lemmai" atšķiras
	 * no pamata lemmas.
	 * TODO: pāriet uz List<Header>
	 */
	public MappingSet<Integer, Tuple<Lemma, Flags>> altLemmas;


	public Gram()
	{
		orig = null;
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
		return toJSON(paradigm, altLemmas, flags, orig, printOrig, additional);
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
	 * Izveido JSON reprezentāciju TGram elementa stilā no padotiem datiem.
	 * Iekšējai lietošanai.
	 * Ātruma problēmu gadījumā, iespējams, jāpāriet uz StringBuilder
	 * atgriešanu.
	 * @param printOrig vai izdrukā iekļaut oriģinālo tekstu?
	 */
	protected static String toJSON (
			HashSet<Integer> paradigm, MappingSet<Integer, Tuple<Lemma, Flags>> altLemmas,
			Flags flags, String orig, boolean printOrig, String additional)
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
			res.append("\"AltLemmas\":{");
			Iterator<Integer> it = altLemmas.keySet().iterator();
			while (it.hasNext())
			{
				Integer next = it.next();
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
			res.append("}");
			hasPrev = true;
		}

		if (flags != null && !flags.pairings.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Flags\":");
			res.append(JSONUtils.mappingSetToJSON(flags.pairings));
			hasPrev = true;
		}

		if (printOrig && orig != null && orig.length() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Original\":\"");
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

}

