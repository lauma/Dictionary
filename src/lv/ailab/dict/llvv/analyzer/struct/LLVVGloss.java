package lv.ailab.dict.llvv.analyzer.struct;

import lv.ailab.dict.struct.Gloss;
import org.w3c.dom.Node;

public class LLVVGloss extends Gloss
{
	public LLVVGloss(Node dNode)
	{
		super(dNode.getTextContent());
	}

	public LLVVGloss(String text)
	{
		super(text);
	}
}
