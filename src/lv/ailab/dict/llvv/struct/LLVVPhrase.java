package lv.ailab.dict.llvv.struct;

import lv.ailab.dict.struct.Phrase;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

/**
 * Pašlaik te nav būtiski paplašināta Phrase funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2019-02-25.
 * @author Lauma
 */
public class LLVVPhrase extends Phrase
{
	public LLVVPhrase(Node piemNode, Type type)
	{
		super();
		this.type = type;
		NodeList fields = piemNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++) {
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("t"))
			{
				if (text == null) text = new LinkedList<>();
				text.add(field.getTextContent());
			}
			else if (fieldname.equals("gram"))
				grammar = new LLVVGram(field);
			else if (fieldname.equals("n"))
			{
				if (subsenses == null) subsenses = new LinkedList<>();
				subsenses.add(new LLVVSense(field));
			}
			else if (!fieldname.equals("#text")) // Teksta lauki šeit tiek ignorēti.
				System.err.printf("\'piem\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
	}
}
