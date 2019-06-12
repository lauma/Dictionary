package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.tezaurs.analyzer.TPronuncNormalizer;
import org.w3c.dom.Node;

/**
 * vf (vārdforma), kas papildināta ar iespēju izgūt savu saturu no Tēzaura XML.
 */
public class TLemma extends Lemma
{
	public TLemma(String lemma)
	{
		super(lemma);
	}
	public TLemma(Node vfNode)
	{
		text = vfNode.getTextContent();

		String pronString = ((org.w3c.dom.Element)vfNode).getAttribute("ru");
		this.setPronunciation(pronString, TPronuncNormalizer.singleton());
	}
}