package lv.ailab.dict.tezaurs.analyzer.struct;

import lv.ailab.dict.struct.Gloss;
import org.w3c.dom.Node;

/**
 * d (definīcijas) lauks Tēzaura XML
 */
public class TGloss extends Gloss
{
	
	public TGloss(Node dNode)
	{
		super(dNode.getTextContent());
	}
	public TGloss(String text)
	{
		super(text);
	}

}