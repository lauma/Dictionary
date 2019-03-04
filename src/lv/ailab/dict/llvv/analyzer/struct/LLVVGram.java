package lv.ailab.dict.llvv.analyzer.struct;

import lv.ailab.dict.struct.Gram;
import org.w3c.dom.Node;

/**
 * Pašlaik te nav būtiski paplašināta Gram funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
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
		freeText = gramNode.getTextContent();
	}

	public LLVVGram(String text)
	{
		this.freeText = text;
	}
}
