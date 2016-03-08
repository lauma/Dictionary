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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Šķirkļa "galva" - vārds + gramatika
 * NB! Šobrīd diemžēl ir tā, ka MLVV uzskata, ka Lemma esošais vārds ir tikai
 * atslēga, bet īstā informācija par šo vārdu glabājas altLemmās, kamēr Tēzaurs
 * arī šo ārā izvelto lemmu uzskata par pilnvērtīgu un nedublē.
 *
 * @author Lauma
 */
public class Header implements HasToJSON, HasToXML
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
	public Header(Lemma lemma, int paradigm, Flags flags)
	{
		this.lemma = lemma;
		gram = new Gram();
		gram.flags= flags;
		gram.paradigm = new HashSet<Integer>(){{add(paradigm);}};
	}
	public Header(Lemma lemma, Integer[] paradigm, Flags flags)
	{
		this.lemma = lemma;
		gram = new Gram();
		gram.flags= flags;
		if (paradigm != null && paradigm.length > 0)
			gram.paradigm = new HashSet<>(Arrays.asList(paradigm));
		else gram.paradigm = null;
	}

	public Header(Lemma lemma, Set<Integer> paradigm, Flags flags)
	{
		this.lemma = lemma;
		gram = new Gram();
		gram.flags= flags;
		if (paradigm != null && paradigm.size() > 0)
			gram.paradigm = new HashSet<>(paradigm);
		else gram.paradigm = null;
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
	 * Izdrukā padotos datus līdzīgā stilā, kā jau parasti drukā Header.
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

	/**
	 * Noklusētais risinājums ir drukāt visu, arī header galveno lemmu - ja nu
	 * tā ir iekļauta arī altLemmās, tad apakšklasei vajag šo pārrakstīt.
	 *
	 * @param parent
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node headerN = doc.createElement("header");
		if (lemma != null) lemma.toXML(headerN);
		if (gram != null) gram.toXML(headerN);
		parent.appendChild(headerN);
	}

	/**
	 * Noformē dotos datus līdzīgā stilā, kā parasti formē Header.
	 */
/*	public static void toXML (Node parent, Lemma lemma, int paradigm, Flags flags)
	{
		Document doc = parent.getOwnerDocument();
		Element headerN = doc.createElement("header");
		// Šo vajag TLex-am
		headerN.setAttribute("LemmaSign", lemma.text);
			lemma.toXML(headerN);
		Gram.toXML(headerN, new HashSet<Integer>(){{add(paradigm);}}, flags);
		parent.appendChild(headerN);
	}*/

}
