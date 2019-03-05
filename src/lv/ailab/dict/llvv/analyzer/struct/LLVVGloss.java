package lv.ailab.dict.llvv.analyzer.struct;

import lv.ailab.dict.struct.Gloss;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LLVVGloss extends Gloss
{
	public LLVVGloss(Node dNode)
	{
		NodeList glossFields = dNode.getChildNodes();
		for (int j = 0; j < glossFields.getLength(); j++)
		{
			Node glossField = glossFields.item(j);
			String glossFieldname = glossField.getNodeName();
			if (glossFieldname.equals("t"))
			{
				if (text == null) text = glossField.getTextContent();
				else
				{
					System.err.println("\'d\' elements satur vairāk kā vienu \'t\'");
					if (text.endsWith(".")) text = text.substring(0, text.length() - 1);
					text = text + "; " + glossField.getTextContent();
				}
			}
			else if (glossFieldname.equals("gram"))
			{
				if (grammar != null)
				{
					System.err.println("\'d\' elements satur vairāk kā vienu \'gram\'");
					grammar = new LLVVGram(grammar.freeText + "; " + glossField.getTextContent());
				}
				else grammar = new LLVVGram(glossField.getTextContent());
			}
			else if (!glossFieldname.equals("#text")) // Teksta elementus šeit ignorē.
				System.err.printf("\'d\' elements \'%s\' netiek apstrādāts\n", glossFieldname);
		}

	}

	public LLVVGloss(String text)
	{
		super(text);
	}
}
