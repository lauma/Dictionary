package lv.ailab.dict.llvv.struct;

import lv.ailab.dict.struct.Entry;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		if (header.isEmpty())
			System.err.printf("Šķirklis bez šķirkļa vārda / šķirkļa galvas:\n%s\n",
					sNode.toString());
		head = new LLVVHeader(header);

		for (Node field : body)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals(""));
			else
				System.err.printf("Šķirklī \"%s\" lauks %s netiek apstrādāts!\n",
						head.lemma.text, fieldname);
		}

		/*for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("v")) // Šķirkļavārda informācija
			{
				if (head != null)
					System.err.printf("Šķirklis \"%s\" satur vairāk kā vienu \'v\'!\n", head.lemma.text);
				head = new THeader(field);
			}
			else if (!fieldname.equals("#text")) // Teksta elementus ignorē, jo šajā vietā ir tikai atstarpjojums.
				postponed.add(field);
		}
		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("avots")) // avoti
				sources = new TSources(field);
			else if (fieldname.equals("g_n")) // visas nozīmes
				senses = Loaders.loadSenses(field, head.lemma.text);
			else if (fieldname.equals("g_fraz")) //frazeoloģiskās vienības
				phrases = Loaders.loadPhrases(field, head.lemma.text, "fraz");
			else if (fieldname.equals("g_de")) //atvasinātās formas
				loadDerivs(field);
			else if (fieldname.equals("ref")) // atsauce uz citu šķirkli
				reference = field.getTextContent();
			else
				System.err.printf("Šķirklī \"%s\" lauks %s netiek apstrādāts!\n", head.lemma.text, fieldname);
		}

		homId = ((org.w3c.dom.Element)sNode).getAttribute("i");
		if ("".equals(homId)) homId = null;

		if (head == null)
			System.err.printf("Šķirklis bez šķirkļa vārda / šķirkļa galvas:\n%s\n", sNode.toString());

			// Move etymology from gram to its own field.
		else if (head != null && head.gram != null && head.gram.flags != null &&
				head.gram.flags.testKey(TKeys.ETYMOLOGY))
		{
			HashSet<String> etym = head.gram.flags.getAll(TKeys.ETYMOLOGY);
			if (etym.size() > 1)
				System.err.printf("Šķirklī \"%s\" ir vairāki etimoloģijas lauki\n", head.lemma.text);
			etymology = etym.stream().reduce((a, b) -> a + "; " + b).orElse(null);
			head.gram.flags.pairings.removeAll(TKeys.ETYMOLOGY);
		}//*/
	}

}
