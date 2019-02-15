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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

/**
 * piem (piemērs) un fraz (frazeoloģisms) lauki Tēzaura XML.
 */
public class TPhrase extends Phrase
{
	public TPhrase(Node piemNode, String lemma)
	{
		super();
		//type = Type.STABLE_UNIT;
		NodeList fields = piemNode.getChildNodes(); 
		for (int i = 0; i < fields.getLength(); i++) {
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("t"))
			{
				if (text == null) text = new LinkedList<>();
				text.add(field.getTextContent());
			}
			else if (fieldname.equals("gram"))
				grammar = new TGram(field, lemma);
			else if (fieldname.equals("n"))
			{
				if (subsenses == null) subsenses = new LinkedList<>();
				TSense newMade = new TSense(field, lemma);
				if (newMade.gloss.text.matches("\\(a\\).*?\\(b\\).*"))
				{
					if (!newMade.glossOnly())
						System.err.println("Cenšas sadalīt \'piem\' skaidrojumu vairākās apakšnozīmēs, lai gan citi lauki  nav tukši.");
					String text = newMade.gloss.text;
					Integer nextOrd = 1;
					String ids = "abcdefghijklmnop";
					while(text.startsWith("(" + ids.charAt(0) + ")") &&
							text.contains("(" + ids.charAt(1) + ")"))
					{
						newMade.ordNumber = nextOrd.toString();
						// Te principā varētu pārtaisīt pirmo burtu uz lielo,
						// bet es neriskēju - ja nu ir kas specifisks?
						// Saīsinājums vai kas tāds?
						newMade.gloss = new TGloss(
								text.substring(3, text.indexOf("(" + ids.charAt(1) + ")")).trim());
						subsenses.add(newMade);
						text = text.substring(text.indexOf("(" + ids.charAt(1) + ")"));
						newMade = new TSense();
						nextOrd++;
						ids = ids.substring(1);
					}
					// Te principā varētu pārtaisīt pirmo burtu uz lielo, bet es
					// neriskēju - ja nu ir kas specifisks? Saīsinājums vai kas
					// tāds?
					newMade.gloss = new TGloss(text.substring(3).trim());
					newMade.ordNumber = nextOrd.toString();
					subsenses.add(newMade);

				} else subsenses.add(newMade);

				//if (newMade.grammar != null)
				//	System.out.println("Ir gramatika! " + field.toString());
				//if (newMade.phrases != null)
				//	System.out.println("Ir piemēri! " + field.toString());
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("piem entry field %s not processed\n", fieldname);
		}			
	}

	public boolean hasUnparsedGram()
	{
		return hasUnparsedGram(this);
	}

	public static boolean hasUnparsedGram(Phrase phrase)
	{
		if (phrase == null) return false;
		if (phrase.grammar != null && TGram.hasUnparsedGram(phrase.grammar))
			return true;

		if (phrase.subsenses != null) for (Sense s : phrase.subsenses)
		{
			if (TSense.hasUnparsedGram(s)) return true;
		}

		return false;
	}

}