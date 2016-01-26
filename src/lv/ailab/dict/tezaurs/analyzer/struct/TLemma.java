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
package lv.ailab.dict.tezaurs.analyzer.struct;

import lv.ailab.dict.struct.Lemma;
import org.w3c.dom.Node;

/**
 * vf (vārdforma), kas papildināta ar iespēju izgūt savu saturu no Tēzaura XML.
 */
public class TLemma extends Lemma
{
	public TLemma(String lemma)
	{
		super(lemma);
	}
	public TLemma(Node vfNode)
	{
		text = vfNode.getTextContent();

		String pronString = ((org.w3c.dom.Element)vfNode).getAttribute("ru");
		if ("".equals(pronString)) return;
		if (pronString.contains(", arī "))
			pronunciation = pronString.split(", arī ");
		else if (pronString.contains(","))
			pronunciation = pronString.split(",");
		else pronunciation = new String[] {pronString};
		for (int i = 0; i < pronunciation.length; i++)
		{
			pronunciation[i] = pronunciation[i].trim();
			if (pronunciation[i].startsWith("["))
				pronunciation[i] = pronunciation[i].substring(1);
			if (pronunciation[i].endsWith("]"))
				pronunciation[i] = pronunciation[i].substring(0, pronunciation[i].length()-1);
		}

	}
}