/*******************************************************************************
 * Copyright 2013, 2014 Institute of Mathematics and Computer Science, University of Latvia
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
package lv.ailab.tezaurs.analyzer.struct;


import java.util.LinkedList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lv.ailab.tezaurs.utils.HasToJSON;

/**
 * v (vārds) field.
 */
public class Header implements HasToJSON
{
	/**
	 * vf (vārdforma) field.
	 */
	public Lemma lemma;
	/**
	 * gram (gamatika) field, optional here.
	 */
	public Gram gram;
	
	
	public Header ()
	{
		lemma = null;
		gram = null;
	}
	
	public Header (Node vNode)
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
				lemma = new Lemma(field);
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
				gram = new Gram (field, lemma.text);
			else System.err.printf(
					"v entry field %s not processed\n", fieldname);				
		}				
	}
	
	public boolean hasParadigm()
	{
		if (gram == null) return false;
		return gram.hasParadigm();
	}
	
	public boolean hasUnparsedGram()
	{
		if (gram == null) return false;
		return gram.hasUnparsedGram();
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
	


}