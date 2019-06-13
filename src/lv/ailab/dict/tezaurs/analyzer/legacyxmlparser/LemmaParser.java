package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.tezaurs.analyzer.TPronuncNormalizer;
import lv.ailab.dict.tezaurs.struct.TElementFactory;
import org.w3c.dom.Node;

/**
 * Lemmas izgūšana no Tēzaura xml vf (vārdforma) lauka.
 */
public class LemmaParser
{
	protected LemmaParser(){};
	protected static LemmaParser singleton = new LemmaParser();
	public static LemmaParser me()
	{
		return singleton;
	}

	public Lemma parseLemma(Node vfNode)
	{
		Lemma result = TElementFactory.me().getNewLemma();
		result.text = vfNode.getTextContent();

		String pronString = ((org.w3c.dom.Element)vfNode).getAttribute("ru");
		result.setPronunciation(pronString, TPronuncNormalizer.me());
		return result;
	}
}
