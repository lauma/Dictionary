package lv.ailab.dict.llvv.analyzer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PhrasalExtractor
{
	public enum Type
	{
		SAMPLE, MULTI_SAMPLE, PHRASAL, UNRECOGNISED;
	}

	public static Type determineType(Node piemNode)
	{
		NodeList fields = piemNode.getChildNodes();
		int tFields = 0;
		int bibFields = 0;
		int nFields = 0;

		for (int i = 0; i < fields.getLength(); i++)
		{
			String fieldname = fields.item(i).getNodeName();
			if (fieldname.equals("t")) tFields++;
			else if (fieldname.equals("bib")) bibFields++;
			else if (fieldname.equals("n")) nFields++;
			else if (!fieldname.equals("gram") && !fieldname.equals("#text"))
				System.err.printf("\'piem\' elements \'%s\' nav atpazīts\n", fieldname);
		}
		if (bibFields < 1 && nFields < 1 && tFields > 1) return Type.MULTI_SAMPLE;
		else if (nFields < 1) return Type.SAMPLE;
		else if (bibFields < 1) return Type.PHRASAL;
		else return Type.UNRECOGNISED;
	}
}
