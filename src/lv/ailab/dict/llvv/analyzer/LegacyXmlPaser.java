package lv.ailab.dict.llvv.analyzer;

import lv.ailab.dict.llvv.struct.LLVVElementFactory;
import lv.ailab.dict.struct.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Apvienotā apstrādes klase LLVV izgūšanai no vecā tipa XML struktūras.
 * Izveidots 2019-06-12, metožu saturs pamatā no 2019-02-25.
 */
// TODO vai vajag dalīt mazākās klasēs?
public class LegacyXmlPaser
{
	protected static LegacyXmlPaser singleton = new LegacyXmlPaser();
	public static LegacyXmlPaser me()
	{
		return singleton;
	}
	/**
	 * No XML elementam "s" atbilstošā DOM izveido šķirkļa datu struktūru. Tas
	 * ietver arī visu analīzi.
	 * @param sNode XML DOM elements, kas atbilst "s"
	 */
	public Entry parseEntry(Node sNode, String volume, boolean normalizePronunc)
	{
		Entry result = LLVVElementFactory.me().getNewEntry();
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

		result.homId = ((org.w3c.dom.Element)sNode).getAttribute("i");
		if ("".equals(result.homId)) result.homId = null;

		if (header.isEmpty())
			System.err.printf("Šķirklis bez šķirkļa vārda / šķirkļa galvas:\n%s\n",
					sNode.toString());
		result.head = parseHeader(header);

		for (Node field : body)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("n"))
			{
				if (result.senses == null) result.senses = new LinkedList<>();
				result.senses.add(parseSense(field));
			}
			else if (fieldname.equals("fraz"))
			{
				if (result.phrases == null) result.phrases = new LinkedList<>();
				result.phrases.add(parsePhrase(field, Phrase.Type.PHRASEOLOGICAL, false));
			}
			else if (fieldname.equals("ref"))
			{
				if (result.references != null)
					System.err.printf("\"Šķirklis \"%s\" satur vairāk kā vienu \'ref\'\n",
							result.head.lemma.text);
				else result.references =  new LinkedList<>();
				result.references.addAll(Arrays.asList(field.getTextContent().split(", ")));
			}
			else
				System.err.printf("Šķirklī \"%s\" elements \'%s\' netiek apstrādāts!\n",
						result.head.lemma.text, fieldname);
		}

		if (volume == null || volume.isEmpty())
			System.err.printf("Šķirklim \"%s\" ir tukša sējuma atsauce\n",
					result.head.lemma.text);
		else
		{
			result.sources = LLVVElementFactory.me().getNewSources();
			result.sources.s = new LinkedList<>();
			result.sources.s.add(volume);
		}
		return result;
	}

	public Header parseHeader (List<Node> vNodes)
	{
		if (vNodes == null || vNodes.isEmpty()) return null;
		Header result = LLVVElementFactory.me().getNewHeader();
		result.gram = LLVVElementFactory.me().getNewGram();
		result.gram.altLemmas = new LinkedList<>();
		for (Node vNode : vNodes)
			result.gram.altLemmas.add(parseHeader(vNode));

		result.lemma = LLVVElementFactory.me().getNewLemma();
		result.lemma.text = result.gram.altLemmas.get(0).lemma.text;
		return result;
	}

	public Header parseHeader(Node vNode)
	{
		Header result = LLVVElementFactory.me().getNewHeader();
		NodeList fields = vNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("vf")) // lemma
			{
				if (result.lemma != null) System.err.printf(
						"\'vf\' ar lemmu \"%s\" satur vēl vienu \'vf\'\n", result.lemma.text);
				result.lemma = parseLemma(field);
			}
			else if (!fieldname.equals("#text")) // Teksta elementus šeit ignorē.
				postponed.add(field);
		}
		if (result.lemma == null)
			System.err.printf("Elements \'v\' ir bez lemmas:\n %s", vNode.toString());

		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram")) // grammar
				result.gram = parseGram(field);
			else System.err.printf(
					"\'v\' elements \'%s\' netika apstrādāts\n", fieldname);
		}
		return result;
	}

	public Lemma parseLemma(Node vfNode)
	{
		Lemma result = LLVVElementFactory.me().getNewLemma();
		result.text = vfNode.getTextContent();
		String pronString = ((org.w3c.dom.Element)vfNode).getAttribute("ru");
		result.setPronunciation(pronString, LLVVPronuncNormalizer.me());
		return result;
	}

	public Gram parseGram(Node gramNode)
	{
		Gram result = LLVVElementFactory.me().getNewGram();
		result.freeText = Gram.normalizePronunc(
				gramNode.getTextContent(), LLVVPronuncNormalizer.me());
		return result;
	}

	public Gram parseGram(String gramText)
	{
		Gram result = LLVVElementFactory.me().getNewGram();
		result.freeText = Gram.normalizePronunc(
				gramText, LLVVPronuncNormalizer.me());
		return result;
	}

	public Sense parseSense(Node nNode)
	{
		Sense result = LLVVElementFactory.me().getNewSense();
		NodeList fields = nNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram"))
				result.grammar = parseGram(field);
			else if (fieldname.equals("d"))
			{
				if (result.gloss == null) result.gloss = new LinkedList<>();
				result.gloss.add(parseGloss(field));
			}
			else if (fieldname.equals("n"))
			{
				if (result.subsenses == null) result.subsenses = new LinkedList<>();
				result.subsenses.add(parseSense(field));
			}
			else if (fieldname.equals("piem"))
			{
				PhrasalExtractor.Type piemType = PhrasalExtractor.determineType(field);
				if (piemType == PhrasalExtractor.Type.SAMPLE)
				{
					if (result.examples == null) result.examples = new LinkedList<>();
					result.examples.add(parseSample(field));
				}
				else if (piemType == PhrasalExtractor.Type.PHRASAL)
				{
					if (result.phrases == null) result.phrases = new LinkedList<>();
					result.phrases.add(parsePhrase(field,
							Phrase.Type.STABLE_UNIT, true));
				}
				else
					System.err.printf("\'piem\' ar tipu \'%s\' netiek apstrādāts\n", piemType);

			}
			else if (fieldname.equals("fraz"))
			{
				if (result.phrases == null) result.phrases = new LinkedList<>();
				result.phrases.add(parsePhrase(field,
						Phrase.Type.PHRASEOLOGICAL, true));
			}
			else if (!fieldname.equals("#text")) // Teksta elementus šeit ignorē.
				System.err.printf("\'n\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
		result.ordNumber = ((org.w3c.dom.Element)nNode).getAttribute("nr");
		if ("".equals(result.ordNumber)) result.ordNumber = null;
		return result;
	}

	public Gloss parseGloss(Node dNode)
	{
		Gloss result = LLVVElementFactory.me().getNewGloss();
		NodeList glossFields = dNode.getChildNodes();
		for (int j = 0; j < glossFields.getLength(); j++)
		{
			Node glossField = glossFields.item(j);
			String glossFieldname = glossField.getNodeName();
			if (glossFieldname.equals("t"))
			{
				if (result.text == null) result.text = glossField.getTextContent();
				else
				{
					System.err.println("\'d\' elements satur vairāk kā vienu \'t\'");
					if (result.text.endsWith("."))
						result.text = result.text.substring(0, result.text.length() - 1);
					result.text = result.text + "; " + glossField.getTextContent();
				}
			}
			else if (glossFieldname.equals("gram"))
			{
				if (result.grammar != null)
				{
					System.err.println("\'d\' elements satur vairāk kā vienu \'gram\'");
					result.grammar = parseGram(result.grammar.freeText + "; " + glossField.getTextContent());
				}
				else result.grammar = parseGram(glossField.getTextContent());
			}
			else if (!glossFieldname.equals("#text")) // Teksta elementus šeit ignorē.
				System.err.printf("\'d\' elements \'%s\' netiek apstrādāts\n", glossFieldname);
		}
		return result;
	}


	public Sample parseSample(Node piemNode)
	{
		Sample result = LLVVElementFactory.me().getNewSample();
		NodeList fields = piemNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("t"))
			{
				if (result.text != null)
					System.err.println("\'piem\' elements satur vairāk kā vienu \'t\'");
				result.text = field.getTextContent();
			}
			else if (fieldname.equals("gram"))
			{
				if (result.grammar != null)
					System.err.println("\'piem\' elements satur vairāk kā vienu \'gram\'");
				result.grammar = parseGram(field);
			}
			else if (fieldname.equals("bib"))
			{
				if (result.citedSource != null)
					System.err.println("\'piem\' elements satur vairāk kā vienu \'bib\'");
				result.citedSource = field.getTextContent();
			}
			else if (!fieldname.equals("#text")) // Teksta lauki šeit tiek ignorēti.
				System.err.printf("\'piem\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
		return result;
	}

	public Phrase parsePhrase(
			Node piemNode, Phrase.Type defaultType, boolean usedInSense)
	{
		Phrase result = LLVVElementFactory.me().getNewPhrase();
		String xmlType = ((org.w3c.dom.Element)piemNode).getAttribute("tips");
		result.type = defaultType;
		if (xmlType != null && !xmlType.isEmpty())
		{
			if (usedInSense)
				System.err.printf("\'%s\' nozīmē iekšā lietots ar parametru tips=\"%s\", to ignorē\n",
						piemNode.getNodeName(), xmlType);
			else
			{
				if (xmlType.equals("s") || xmlType.equals("savienojums"))
					result.type = Phrase.Type.STABLE_UNIT;
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
				if (result.text == null) result.text = new LinkedList<>();
				String newText = field.getTextContent();
				if (newText != null)
				{
					newText = newText.trim();
					if (newText.matches(".*[^.]\\."))
						newText = newText.substring(0, newText.length() - 1);
					result.text.add(newText);
				}

			}
			else if (fieldname.equals("gram"))
				result.grammar = parseGram(field);
			else if (fieldname.equals("n"))
			{
				if (result.subsenses == null) result.subsenses = new LinkedList<>();
				result.subsenses.add(parseSense(field));
			}
			else if (!fieldname.equals("#text")) // Teksta lauki šeit tiek ignorēti.
				System.err.printf("\'%s\' elements \'%s\' netiek apstrādāts\n",
						piemNode.getNodeName(), fieldname);
		}
		return result;
	}

}
