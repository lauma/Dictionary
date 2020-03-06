package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.TEntry;
import lv.ailab.dict.tezaurs.struct.constants.flags.TKeys;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class EntryParser
{
	protected EntryParser(){};
	protected static EntryParser singleton = new EntryParser();
	public static EntryParser me()
	{
		return singleton;
	}

	/**
	 * No XML elementam "s" atbilstošā DOM izveido šķirkļa datu struktūru. Tas
	 * ietver arī visu analīzi.
	 * @param sNode XML DOM elements, kas atbilst "s"
	 */
	public TEntry parseEntry (Node sNode)
	{
		TEntry result = TElementFactory.me().getNewEntry();
		NodeList fields = sNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("v")) // Šķirkļavārda informācija
			{
				if (result.head != null) System.err.printf(
						"Šķirklis \"%s\" satur vairāk kā vienu \'v\'!\n",
						result.head.lemma.text);
				result.head = HeaderParser.me().parseHeader(field);
			}
			else if (!fieldname.equals("#text")) // Teksta elementus ignorē, jo šajā vietā ir tikai atstarpjojums.
				postponed.add(field);
		}
		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("avots")) // avoti
				result.sources = SourcesParser.me().parseSources(field);
			else if (fieldname.equals("g_n")) // visas nozīmes
				result.senses = LoadingUtils.loadSenses(field, result.head.lemma.text);
			else if (fieldname.equals("g_fraz")) //frazeoloģiskās vienības
				result.phrases = LoadingUtils.loadPhrases(field, result.head.lemma.text, "fraz");
			else if (fieldname.equals("g_de")) //atvasinātās formas
				loadDerivs(result, field);
			else if (fieldname.equals("ref")) // atsauce uz citu šķirkli
				loadRef(result, field);
			else
				System.err.printf("Šķirklī \"%s\" lauks %s netiek apstrādāts!\n",
						result.head.lemma.text, fieldname);
		}

		result.homId = ((org.w3c.dom.Element)sNode).getAttribute("i");
		if ("".equals(result.homId)) result.homId = null;

		if (result.head == null)
			System.err.printf("Šķirklis bez šķirkļa vārda / šķirkļa galvas:\n%s\n", sNode.toString());

			// Move etymology from gram to its own field.
		else if (result.head != null && result.head.gram != null && result.head.gram.flags != null &&
				result.head.gram.flags.testKey(TKeys.ETYMOLOGY))
		{
			HashSet<String> etym = result.head.gram.flags.getAll(TKeys.ETYMOLOGY);
			if (etym.size() > 1)
				System.err.printf("Šķirklī \"%s\" ir vairāki etimoloģijas lauki\n", result.head.lemma.text);
			result.etymology = etym.stream().reduce((a, b) -> a + "; " + b).orElse(null);
			result.head.gram.flags.pairings.removeAll(TKeys.ETYMOLOGY);
		}
		return result;
	}

	/**
	 * Process ref field. It must by split by commas, but commas in parenthesis
	 * () must be ignored.
	 */
	private void loadRef (TEntry entry, Node ref)
	{
		entry.references = new LinkedList<>();
		String refText = ref.getTextContent();
		// Vajag sadalīt pa tiem komatiem, kas nav iekavās.
		while (refText != null && !refText.isEmpty())
		{
			if (!refText.contains(", "))
			{
				entry.references.add(refText);
				refText = null;
			}
			else if (!refText.contains("("))
			{
				entry.references.addAll(Arrays.asList(refText.split(", ")));
				refText = null;
			}
			else
			{
				String beginPart = refText.substring(0, refText.indexOf(", "));
				refText = refText.substring(refText.indexOf(", "));
				if (beginPart.indexOf('(') > beginPart.indexOf(')'))
				{
					beginPart = beginPart + refText.substring(0, refText.indexOf(')'));
					refText = refText.substring(refText.indexOf(')'));
					if (refText.contains(", "))
					{
						beginPart = beginPart + refText.indexOf(", ");
						refText = refText.substring(refText.indexOf(", "));
					}
					else
					{
						beginPart = beginPart + refText;
						refText = "";
					}
				}
				if (refText.startsWith(", ")) refText = refText.substring(2);
				entry.references.add(beginPart);
			}
		}
	}

	/**
	 * Process g_de field.
	 * Derived forms - in Lexicon sense, they are separate lexemes, alternate
	 * wordforms but with a link to the same dictionary entry.
	 */
	private void loadDerivs(TEntry entry, Node allDerivs)
	{
		if (entry.derivs == null) entry.derivs = new LinkedList<>();
		NodeList derivNodes = allDerivs.getChildNodes();
		for (int i = 0; i < derivNodes.getLength(); i++)
		{
			Node deriv = derivNodes.item(i);
			if (deriv.getNodeName().equals("de"))
			{
				NodeList derivSubNodes = deriv.getChildNodes();
				for (int j = 0; j < derivSubNodes.getLength(); j++)
				{
					Node derivSubNode = derivSubNodes.item(j);
					if (derivSubNode.getNodeName().equals("v"))
						entry.derivs.add(HeaderParser.me().parseHeader(derivSubNode));
					else if (!derivSubNode.getNodeName().equals("#text")) // Text nodes here are ignored.
						System.err.printf(
								"g_de/de lauks %s netiek apstrādāts, jo tiek sagaidīts 'v'.\n",
								derivSubNode.getNodeName());
				}
			}
			else if (!deriv.getNodeName().equals("#text")) // Text nodes here are ignored.
				System.err.printf(
						"g_de lauks %s netiek apstrādāts, jo tiek sagaidīts 'de'.\n",
						deriv.getNodeName());
		}
	}

}
