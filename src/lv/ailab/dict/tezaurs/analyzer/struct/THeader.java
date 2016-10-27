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


import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.Set;

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
	public THeader(Lemma lemma, Set<Integer> paradigm, Flags flags)
	{
		super (lemma, paradigm, flags);
	}

	public THeader(Lemma lemma, int paradigm, Flags flags)
	{
		super (lemma, paradigm, flags);
	}

	public THeader(Lemma lemma, String gramText)
	{
		this.lemma = lemma;
		TGram newGram = new TGram();
		newGram.set(gramText, lemma.text);
		this.gram = newGram;
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

}