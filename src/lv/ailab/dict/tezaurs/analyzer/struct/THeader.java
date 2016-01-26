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


import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * v (vārds) field.
 */
public class THeader extends Header
{
	
	public THeader()
	{
		lemma = null;
		gram = null;
	}
	
	public THeader(Node vNode)
	{
		NodeList fields = vNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("vf")) // lemma
			{
				if (lemma != null)
					System.err.printf("vf with lemma \"%s\" contains more than one \'vf\'\n", lemma.text);
				lemma = new TLemma(field);
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				postponed.add(field);
		}
		if (lemma == null)
			System.err.printf("Thesaurus v-entry without a lemma :(\n");
		
		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram")) // grammar
				gram = new TGram(field, lemma.text);
			else System.err.printf(
					"v entry field %s not processed\n", fieldname);				
		}				
	}
	
	public int paradigmCount()
	{
		if (gram == null) return 0;
		return gram.paradigmCount();
	}
	
	public boolean hasUnparsedGram()
	{
		return THeader.hasUnparsedGram(this);
	}
	public static boolean hasUnparsedGram(Header header)
	{
		if (header == null || header.gram == null) return false;
		return TGram.hasUnparsedGram(header.gram);
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
	public static String toJSON(TLemma lemma, int paradigm, Flags flags, boolean addTitle)
	{
		StringBuilder res = new StringBuilder();

		if (addTitle) res.append("\"Header\":");
		res.append("{");
		res.append(lemma.toJSON());
		res.append(", ");
		res.append(TGram.toJSON(new HashSet<Integer>(){{add(paradigm);}}, flags));

		res.append("}");
		return res.toString();
	}
	


}