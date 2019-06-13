package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.struct.Gloss;
import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.TGloss;
import org.w3c.dom.Node;

/**
 * Lemmas izgūšana no Tēzaura xml vf (vārdforma) lauka.
 */
public class GlossParser
{
	protected GlossParser(){};
	protected static GlossParser singleton = new GlossParser();
	public static GlossParser me()
	{
		return singleton;
	}

	public Gloss parseGloss(Node dNode)
	{
		TGloss result = TElementFactory.me().getNewGloss();
		result.text = dNode.getTextContent();
		return result;
	}
}
