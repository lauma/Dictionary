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

import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;
import lv.ailab.dict.tezaurs.analyzer.io.Loaders;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * n (nozīme / nozīmes nianse) field.
 */
public class TSense extends Sense
{
	public TSense()
	{
		super();
	}
	
	/**
	 * @param lemma is used for grammar parsing.
	 */
	public TSense(Node nNode, String lemma)
	{
		NodeList fields = nNode.getChildNodes(); 
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram"))
				grammar = new TGram(field, lemma);
			else if (fieldname.equals("d"))
			{
				NodeList glossFields = field.getChildNodes();
				for (int j = 0; j < glossFields.getLength(); j++)
				{
					Node glossField = glossFields.item(j);
					String glossFieldname = glossField.getNodeName();
					if (glossFieldname.equals("t"))
					{
						if (gloss != null)
							System.err.println("\'d\' elements satur vairāk kā vienu \'t\'");
						gloss = new TGloss(glossField);
					}
					else if (!glossFieldname.equals("#text")) // Text nodes here are ignored.
						System.err.printf("\'d\' elements \'%s\' netiek apstrādāts\n", glossFieldname);
				}
			}
			else if (fieldname.equals("g_piem"))
				phrases = Loaders.loadPhrases(field, lemma, "piem");
			else if (fieldname.equals("g_an"))
				subsenses = Loaders.loadSenses(field, lemma);
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("\'n\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
		ordNumber = ((org.w3c.dom.Element)nNode).getAttribute("nr");
		if ("".equals(ordNumber)) ordNumber = null;
	}

	public boolean hasUnparsedGram()
	{
		return hasUnparsedGram(this);
	}

	public static boolean hasUnparsedGram(Sense sense)
	{
		if (sense == null) return false;
		if (sense.grammar != null && TGram.hasUnparsedGram(sense.grammar))
			return true;

		if (sense.phrases != null) for (Phrase e : sense.phrases)
		{
			if (TPhrase.hasUnparsedGram(e)) return true;
		}
		if (sense.subsenses != null) for (Sense s : sense.subsenses)
		{
			if (TSense.hasUnparsedGram(s)) return true;
		}			
		return false;
	}

}