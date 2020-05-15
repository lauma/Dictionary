package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.TGloss;
import lv.ailab.dict.tezaurs.struct.TPhrase;
import lv.ailab.dict.tezaurs.struct.TSense;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

public class PhraseParser
{
	protected PhraseParser(){};
	protected static PhraseParser singleton = new PhraseParser();
	public static PhraseParser me()
	{
		return singleton;
	}

	public TPhrase parsePhrase(Node piemNode, String lemma)
	{
		TPhrase result = TElementFactory.me().getNewPhrase();
		result.type = Phrase.Type.STABLE_UNIT; // Tēzaurā visas frāzes ir viena tipa.
		NodeList fields = piemNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++) {
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("t"))
			{
				if (result.text == null) result.text = new LinkedList<>();
				result.text.add(field.getTextContent());
			}
			else if (fieldname.equals("gram"))
				result.grammar = GramParser.me().parseGram(field, lemma, GramParser.ParseType.PHRASE);
			else if (fieldname.equals("n"))
			{
				if (result.subsenses == null) result.subsenses = new LinkedList<>();
				TSense newMade = SenseParser.me().parseSense(field, lemma);
				if (newMade.gloss.size() > 1)
					System.err.println("Cenšas sadalīt \'piem\' skaidrojumu vairākās apakšnozīmēs, bet ir vairākas glosas.");
				for (String glossVariant : newMade.gloss.stream().map(g -> g.text).toArray(String[]::new)) // Te normāli būtu jābūt vienam.
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
							newMade.gloss = new LinkedList<>();
							TGloss tmp = TElementFactory.me().getNewGloss();
							tmp.text = text.substring(3, text.indexOf("(" + ids.charAt(1) + ")")).trim();
							newMade.gloss.add(tmp);
							result.subsenses.add(newMade);
							text = text.substring(text.indexOf("(" + ids.charAt(1) + ")"));
							newMade = TElementFactory.me().getNewSense();
							nextOrd++;
							ids = ids.substring(1);
						}
						// Te principā varētu pārtaisīt pirmo burtu uz lielo, bet es
						// neriskēju - ja nu ir kas specifisks? Saīsinājums vai kas
						// tāds?
						newMade.gloss = new LinkedList<>();
						TGloss tmp = TElementFactory.me().getNewGloss();
						tmp.text = text.substring(3).trim();
						newMade.gloss.add(tmp);
						newMade.ordNumber = Integer.toString(nextOrd);
						result.subsenses.add(newMade);

					} else result.subsenses.add(newMade);
				}
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("\'piem\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
		return result;
	}
}
