package lv.ailab.dict.llvv.analyzer.struct;

import lv.ailab.dict.struct.Sample;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Pašlaik te nav būtiski paplašināta Sample funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2019-02-25.
 * @author Lauma
 */
public class LLVVSample extends Sample
{
	public LLVVSample(Node piemNode)
	{
		NodeList fields = piemNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("t"))
			{
				if (text != null)
					System.err.println("\'piem\' elements satur vairāk kā vienu \'t\'");
				text = field.getTextContent();
			}
			else if (fieldname.equals("gram"))
			{
				if (grammar != null)
					System.err.println("\'piem\' elements satur vairāk kā vienu \'gram\'");
				grammar = new LLVVGram(field);
			}
			else if (fieldname.equals("bib"))
			{
				if (citedSource != null)
					System.err.println("\'piem\' elements satur vairāk kā vienu \'bib\'");
				citedSource = field.getTextContent();
			}
			else if (!fieldname.equals("#text")) // Teksta lauki šeit tiek ignorēti.
				System.err.printf("\'piem\' elements \'%s\' netiek apstrādāts\n", fieldname);

		}
	}
}
