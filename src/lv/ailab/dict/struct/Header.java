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

import lv.ailab.dict.utils.HasToJSON;

/**
 * Šķirkļa "galva" - vārds + gramatika
 * @author Lauma
 */
public class Header implements HasToJSON
{
	/**
	 * Vārdforma
	 */
	public Lemma lemma;
	/**
	 * Neobligāta gramatika
	 */
	public Gram gram;


	public Header()
	{
		lemma = null;
		gram = null;
	}

	public int paradigmCount()
	{
		if (gram == null) return 0;
		return gram.paradigmCount();
	}

	public String toJSON()
	{
		StringBuilder res = new StringBuilder();

		res.append("\"Header\":{");
		res.append(lemma.toJSON());

		if (gram != null)
		{
			res.append(", ");
			res.append(gram.toJSON());
		}

		res.append("}");
		return res.toString();
	}

	/**
	 * Izdrukā padotos datus līdzīgā stilā, kā jau parasti drukā THeader.
	 * @param addTitle vai sākumā pielikt "\"Header\":"
	 */
	public static String toJSON(Lemma lemma, int paradigm, Flags flags, boolean addTitle)
	{
		StringBuilder res = new StringBuilder();

		if (addTitle) res.append("\"Header\":");
		res.append("{");
		res.append(lemma.toJSON());
		res.append(", ");
		res.append(Gram.toJSON(new HashSet<Integer>(){{add(paradigm);}}, flags));

		res.append("}");
		return res.toString();
	}



}
