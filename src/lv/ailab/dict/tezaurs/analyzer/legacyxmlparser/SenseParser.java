package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.TSense;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

public class SenseParser
{
	protected SenseParser(){};
	protected static SenseParser singleton = new SenseParser();
	public static SenseParser me()
	{
		return singleton;
	}

	/**
	 * @param lemma is used for grammar parsing.
	 */
	public TSense parseSense(Node nNode, String lemma)
	{
		TSense result = TElementFactory.me().getNewSense();
		NodeList fields = nNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram"))
				result.grammar = GramParser.me().parseGram(field, lemma, GramParser.ParseType.SENSE);
			else if (fieldname.equals("d"))
			{
				NodeList glossFields = field.getChildNodes();
				for (int j = 0; j < glossFields.getLength(); j++)
				{
					Node glossField = glossFields.item(j);
					String glossFieldname = glossField.getNodeName();
					if (glossFieldname.equals("t"))
					{
						if (result.gloss != null)
							System.err.println("\'d\' elements satur vairāk kā vienu \'t\'");
						else result.gloss = new LinkedList<>();
						result.gloss.add(GlossParser.me().parseGloss(glossField));
					}
					else if (!glossFieldname.equals("#text")) // Text nodes here are ignored.
						System.err.printf("\'d\' elements \'%s\' netiek apstrādāts\n", glossFieldname);
				}
			}
			else if (fieldname.equals("g_piem"))
				result.phrases = LoadingUtils.loadPhrases(field, lemma, "piem");
			else if (fieldname.equals("g_an"))
				result.subsenses = LoadingUtils.loadSenses(field, lemma);
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("\'n\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
		result.ordNumber = ((org.w3c.dom.Element)nNode).getAttribute("nr");
		if ("".equals(result.ordNumber)) result.ordNumber = null;
		return result;
	}

}
