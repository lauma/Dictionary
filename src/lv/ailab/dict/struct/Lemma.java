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

import org.json.simple.JSONObject;
import java.util.ArrayList;

/**
 * Vārdforma (vf Tēzaura XML).
 * @author Lauma
 */
public class Lemma implements HasToJSON
{
	public String text;
	/**
	 * ru (runa) field, optional here.
	 */
	public String[] pronunciation;

	public Lemma()
	{
		text = null;
		pronunciation = null;
	}
	public Lemma(String lemma)
	{
		text = lemma;
		pronunciation = null;
	}

	public Lemma(String lemma, String[] pronunciation)
	{
		text = lemma;
		this.pronunciation = pronunciation;
	}
	/**
	 *  Uzstāda lemmu un pārbauda, vai tā jau nav bijusi uzstādīta, par to
	 *  pabrīdinot.
	 */
	public void set(String lemmaText) {
		if (text != null)
			System.err.printf(
					"Duplicēts saturs laukam 'lemma' : '%s' un '%s'", text,
					lemmaText);
		text = lemmaText;
	}

	// Šo vajag, lai lemmas liktu hash struktūrās (hasmap, hashset).
	@Override
	public boolean equals (Object o)
	{
		if (o == null) return false;
		if (this.getClass() != o.getClass()) return false;
		return (text == null && ((Lemma) o).text == null ||
				text != null && text.equals(((Lemma) o).text))
				&& (pronunciation == null && ((Lemma) o).pronunciation == null
				|| pronunciation != null && pronunciation.equals(((Lemma) o).pronunciation));
	}

	// Šo vajag, lai lemmas liktu hash struktūrās (hasmap, hashset).
	@Override
	public int hashCode()
	{
		return 1721 *(text == null ? 1 : text.hashCode())
				+ (pronunciation == null ? 1 : pronunciation.hashCode());
	}

	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		res.append(String.format("\"Lemma\":\"%s\"", JSONObject.escape(text)));
		if (pronunciation != null)
		{
			res.append(", \"Pronunciation\":[\"");
			ArrayList<String> escaped = new ArrayList<>();
			for (String pron : pronunciation)
				escaped.add(JSONObject.escape(pron));
			res.append(String.join("\", \"", escaped));
			res.append("\"]");
		}
		return res.toString();
	}
}