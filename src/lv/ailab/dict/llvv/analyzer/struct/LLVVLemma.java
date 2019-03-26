package lv.ailab.dict.llvv.analyzer.struct;

import lv.ailab.dict.llvv.analyzer.PronuncNormalizer;
import lv.ailab.dict.struct.Lemma;
import org.w3c.dom.Node;

/**
 * Pašlaik te nav būtiski paplašināta Lemma funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2019-02-25.
 * @author Lauma
 */
public class LLVVLemma extends Lemma
{

	public LLVVLemma(String lemma)
	{
		super(lemma);
	}

	public LLVVLemma(Node vfNode)
	{
		text = vfNode.getTextContent();
		String pronString = ((org.w3c.dom.Element)vfNode).getAttribute("ru");
		pronString = PronuncNormalizer.normalize(pronString);
		this.setPronunciation(pronString);
	}

}
