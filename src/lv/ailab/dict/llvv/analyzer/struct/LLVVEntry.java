package lv.ailab.dict.llvv.analyzer.struct;

import lv.ailab.dict.struct.Entry;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.LinkedList;

public class LLVVEntry extends Entry
{
	/**
	 * No XML elementam "s" atbilstošā DOM izveido šķirkļa datu struktūru. Tas
	 * ietver arī visu analīzi.
	 * @param sNode XML DOM elements, kas atbilst "s"
	 */
	public LLVVEntry(Node sNode)
	{
		NodeList fields = sNode.getChildNodes();
		LinkedList<Node> header = new LinkedList<>();
		LinkedList<Node> body = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("v")) // Šķirkļavārda informācija
				header.add(field);
			else if (!fieldname.equals("#text")) // Teksta elementus ignorē, jo šajā vietā ir tikai atstarpjojums.
				body.add(field);
		}

		homId = ((org.w3c.dom.Element)sNode).getAttribute("i");
		if ("".equals(homId)) homId = null;

		if (header.isEmpty())
			System.err.printf("Šķirklis bez šķirkļa vārda / šķirkļa galvas:\n%s\n",
					sNode.toString());
		head = new LLVVHeader(header);

		for (Node field : body)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("n"))
			{
				if (senses == null) senses = new LinkedList<>();
				senses.add(new LLVVSense(field));
			}
			else if (fieldname.equals("fraz"))
			{
				if (phrases == null) phrases = new LinkedList<>();
				phrases.add(new LLVVPhrase(field, LLVVPhrase.Type.PHRASEOLOGICAL));
			}
			else if (fieldname.equals("ref"))
			{
				if (references != null)
					System.err.printf("\"Šķirklis \"%s\" satur vairāk kā vienu \'ref\'\n",
							head.lemma.text);
				references =  new LinkedList<>();
				references.addAll(Arrays.asList(field.getTextContent().split(", ")));
			}
			else
				System.err.printf("Šķirklī \"%s\" elements \'%s\' netiek apstrādāts!\n",
						head.lemma.text, fieldname);
		}

	}

}
