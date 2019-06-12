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
	public LLVVPhrase(Node piemNode, Type defaultType, boolean usedInSense)
	{
		super();
		String xmlType = ((org.w3c.dom.Element)piemNode).getAttribute("tips");
		this.type = defaultType;
		if (xmlType != null && !xmlType.isEmpty())
		{
			if (usedInSense)
				System.err.printf("\'%s\' nozīmē iekšā lietots ar parametru tips=\"%s\", to ignorē\n",
						piemNode.getNodeName(), xmlType);
			else
			{
				if (xmlType.equals("s") || xmlType.equals("savienojums"))
					this.type = Type.STABLE_UNIT;
				else System.err.printf("\'%s\' ar neatpazītu tips=\"%s\"\n",
							piemNode.getNodeName(), xmlType);
			}
		}

		NodeList fields = piemNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++) {
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			// Tekstam ņem nost pēdējo punktu.
			if (fieldname.equals("t"))
			{
				if (text == null) text = new LinkedList<>();
				String newText = field.getTextContent();
				if (newText != null)
				{
					newText = newText.trim();
					if (newText.matches(".*[^.]\\."))
						newText = newText.substring(0, newText.length() - 1);
					text.add(newText);
				}

			}
			else if (fieldname.equals("gram"))
				grammar = new LLVVGram(field);
			else if (fieldname.equals("n"))
			{
				if (subsenses == null) subsenses = new LinkedList<>();
				subsenses.add(new LLVVSense(field));
			}
			else if (!fieldname.equals("#text")) // Teksta lauki šeit tiek ignorēti.
				System.err.printf("\'%s\' elements \'%s\' netiek apstrādāts\n",
						piemNode.getNodeName(), fieldname);
		}
	}
}
