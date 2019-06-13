package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.THeader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

/**
 * v (vārds) lauka apstrāde.
 */
public class HeaderParser
{
	protected HeaderParser(){};
	protected static HeaderParser singleton = new HeaderParser();
	public static HeaderParser me()
	{
		return singleton;
	}

	public THeader parseHeader(Node vNode)
	{
		THeader result = TElementFactory.me().getNewHeader();
		NodeList fields = vNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("vf")) // lemma
			{
				if (result.lemma != null) System.err.printf(
						"\'vf\' ar lemmu \"%s\" satur vēl vienu \'vf\'\n", result.lemma.text);
				result.lemma = LemmaParser.me().parseLemma(field);
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				postponed.add(field);
		}
		if (result.lemma == null)
			System.err.printf("Elements \'v\' ir bez lemmas:\n %s", vNode.toString());

		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram")) // grammar
				result.gram = GramParser.me().parseGram(field, result.lemma.text);
			else System.err.printf(
					"\'v\' elements \'%s\' netika apstrādāts\n", fieldname);
		}
		return result;
	}

}
