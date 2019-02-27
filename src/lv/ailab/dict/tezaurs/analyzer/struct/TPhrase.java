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
		type = Type.STABLE_UNIT; // Tēzaurā visas frāzes ir viena tipa.
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
				if (newMade.gloss.text.size() > 1)
					System.err.println("Cenšas sadalīt \'piem\' skaidrojumu vairākās apakšnozīmēs, bet ir vairākas glosas.");
				for (String glossVariant : newMade.gloss.text) // Te normāli būtu jābūt vienam.
				{
					if (glossVariant.matches("\\(a\\).*?\\(b\\).*"))
					{
						if (!newMade.glossOnly())
							System.err.println("Cenšas sadalīt \'piem\' skaidrojumu vairākās apakšnozīmēs, lai gan citi lauki nav tukši.");
						String text = glossVariant;
						int nextOrd = 1;
						String ids = "abcdefghijklmnop";
						while (text.startsWith("(" + ids.charAt(0) + ")") &&
								text.contains("(" + ids.charAt(1) + ")"))
						{
							newMade.ordNumber = Integer.toString(nextOrd);
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
						newMade.ordNumber = Integer.toString(nextOrd);
						subsenses.add(newMade);

					} else subsenses.add(newMade);
				}
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("\'piem\' elements \'%s\' netiek apstrādāts\n", fieldname);
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
			if (TSense.hasUnparsedGram(s)) return true;

		return false;
	}

	public int countEmptyGloss()
	{
		return countEmptyGloss(this);
	}

	public static int countEmptyGloss(Phrase p)
	{
		if (p == null) return 0;
		int res = 0;
		if (p.subsenses != null) for (Sense sub : p.subsenses)
			res = res + TSense.countEmptyGloss(sub);
		return res;
	}

}