package lv.ailab.dict.llvv.struct;

import lv.ailab.dict.llvv.analyzer.LLVVPronuncNormalizer;
import lv.ailab.dict.struct.Gram;
import org.w3c.dom.Node;

/**
 * Pašlaik te nav būtiski paplašināta Gram funkcionalitāte, te ir iznestas
 * izgūšanas metodes. Veidojot gramatiku, tiek normalizētas izrunas
 * kvadrātiekavās!
 * TODO: atdalīt flagText no inflection text
 *
 * Izveidots 2019-02-25.
 * @author Lauma
 */
public class LLVVGram extends Gram
{
	public LLVVGram () {};

	public LLVVGram(Node gramNode)
	{
		freeText = normalizePronunc(
				gramNode.getTextContent(), LLVVPronuncNormalizer.singleton());
	}

	public LLVVGram(String text)
	{
		this.freeText = normalizePronunc(text, LLVVPronuncNormalizer.singleton());
	}

}
