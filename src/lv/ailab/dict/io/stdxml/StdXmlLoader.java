package lv.ailab.dict.io.stdxml;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.DomIoUtils;
import lv.ailab.dict.struct.*;
import lv.ailab.dict.utils.MappingSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class StdXmlLoader
{
	protected StdXmlLoader() {};
	protected static StdXmlLoader singleton = new StdXmlLoader();
	public static StdXmlLoader me()
	{
		return singleton;
	}

	/**
	 * Dom pieeja - visu ielasa atmiņā uzreiz.
	 */
	public Dictionary makeDictionary(Document doc, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Element docElem = doc.getDocumentElement();
		if (!"Dictionary".equals(docElem.getNodeName()))
			throw new DictionaryXmlReadingException("Nav atrasts XML saknes elements \"Dictionary\"");
		Node latvianNode = DomIoUtils.getOnlyChildFromXml(docElem, "Latvian");
		if (latvianNode == null)
			throw new DictionaryXmlReadingException("Nav atrasts XML saknes elements \"Dictionary\\Latvian\"");
		LinkedList<Node> entries = DomIoUtils.getSingleTypeNodeArrayFromXml(latvianNode, "Entry");
		if (entries == null || entries.isEmpty())
			throw new DictionaryXmlReadingException("Nav neviena šķirkļa - elementa \"Entry\"");
		Dictionary result = elemFact.getNewDictionary();
		result.entries = new ArrayList<>();
		for (Node eNode : entries)
		{
			Entry e = makeEntry(eNode, elemFact);
			if (e != null) result.entries.add(e);
		}
		return result;
	}

	public Entry makeEntry(Node entryNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Entry result = elemFact.getNewEntry();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) entryNode);
		if (fields == null || fields.isEmpty()) return null;

		// HomonymNumber
		result.homId = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Entry", "HomonymNumber");
		// Header
		result.head = loadSingularHeaderBlock(fields, elemFact,
				"Entry");
		// Senses
		result.senses = loadSensesBlock(fields, elemFact,
				"Entry", "Senses");
		// StablePhrases
		result.phrases = loadStablePhrasesBlock(fields, elemFact,
				"Entry", "StablePhrases");
		//Derivatives
		result.derivs = loadHeaderListBlock(fields, elemFact,
				"Entry", "Derivatives");
		// Etymology
		result.etymology = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Entry", "Etymology");
		// References
		result.references = XmlFieldMappingHandler.me().takeoutStringFieldArray(fields,
				"Entry", "References", "EntryRef");
		// Sources
		result.sources = loadSourcesBlock(fields, elemFact,
				"Entry");
		// Pārbauda galveno šķirkļavārdu un LemmaSign.
		String lemmaSign = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Entry", "LemmaSign");
		checkMainLemma(result, lemmaSign);
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Entry");
		return result;
	}

	public Header makeHeader(Node headerNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Header result = elemFact.getNewHeader();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) headerNode);
		if (fields == null || fields.isEmpty()) return null;

		// Lemma
		result.lemma = loadLemmaBlock(fields, elemFact, "Header");
		// Pronunciations
		LinkedList<String> tempProns = XmlFieldMappingHandler.me().takeoutStringFieldArray(fields,
				"Header", "Pronunciations", "Pronunciation");
		if (tempProns != null && !tempProns.isEmpty())
		{
			if (result.lemma == null)
				throw new DictionaryXmlReadingException("Elementā \"Header\" ir \"Pronunciations\", bet nav \"Lemma\"!");
			result.lemma.pronunciation = tempProns.toArray(new String[0]);
		}
		// Gram
		result.gram = loadGramBlock(fields, elemFact,
				"Header");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Header");
		return result;
	}

	public Sense makeSense(Node senseNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Sense result = elemFact.getNewSense();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) senseNode);
		if (fields == null || fields.isEmpty()) return null;

		// SenseNumber
		result.ordNumber = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Sense", "SenseNumber");
		// Gram
		result.grammar = loadGramBlock(fields, elemFact, "Sense");
		// Gloss
		result.gloss = loadGlosesBlock(fields, elemFact, "Sense");
		// Examples
		result.examples = loadExamplesBlock(fields, elemFact, "Sense");
		// StablePhrases
		result.phrases = loadStablePhrasesBlock(fields, elemFact,
				"Sense", "StablePhrases");
		// Subsenses
		result.subsenses = loadSensesBlock(fields, elemFact,
				"Sense", "Subsenses");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Sense");
		return result;
	}

	public Phrase makePhrase(Node phraseNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Phrase result = elemFact.getNewPhrase();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) phraseNode);
		if (fields == null || fields.isEmpty()) return null;

		// Type
		String typeStr = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Phrase", "Type");
		if (typeStr != null) result.type = Phrase.Type.parseString(typeStr);
		// Text
		result.text = XmlFieldMappingHandler.me().takeoutStringFieldArray(fields,
				"Phrase", "Text", "Variant");
		// Gram
		result.grammar = loadGramBlock(fields, elemFact, "Phrase");
		// Senses
		result.subsenses = loadSensesBlock(fields, elemFact,
				"Phrase", "Senses");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Phrase");
		return result;
	}

	public Flags makeFlags(Node flagsNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Flags result = elemFact.getNewFlags();
		result.pairings = new MappingSet<>();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) flagsNode);
		if (fields == null || fields.isEmpty()) return null;

		ArrayList<Node> flagNodes = fields.removeNodeChildren("Flag");
		if (flagNodes != null) for (Node flagNode : flagNodes)
		{
			String key = null;
			String value = null;

			XmlFieldMapping flagFields = XmlFieldMappingHandler.me().domElemToHash((Element) flagNode);
			if (flagFields == null || flagFields.isEmpty()) break;

			ArrayList<String> keyTexts = flagFields.removeStringChildren("Key");
			if (keyTexts != null && keyTexts.size() > 1)
				throw new DictionaryXmlReadingException("Elementā \"Flag\" atrasti vairāki \"Key\"!");
			if (keyTexts!= null && !keyTexts.isEmpty()) key = keyTexts.get(0);

			ArrayList<String> valueTexts = flagFields.removeStringChildren("Value");
			if (valueTexts != null && valueTexts.size() > 1)
				throw new DictionaryXmlReadingException("Elementā \"Flag\" atrasti vairāki \"Value\"!");
			if (valueTexts!= null && !valueTexts.isEmpty()) value = valueTexts.get(0);

			dieOnNonempty(flagFields, "Flag");
			result.pairings.put(key, value);
		}
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Flags");
		if (result.pairings.isEmpty()) return null;
		return result;
	}

	public Gram makeGram(Node gramNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Gram result = elemFact.getNewGram();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) gramNode);
		if (fields == null || fields.isEmpty()) return null;

		// Paradigms
		LinkedList<String> tmpPar = XmlFieldMappingHandler.me().takeoutStringFieldArray(fields,
				"Gram", "Paradigms", "Paradigm");
		if (tmpPar != null) result.paradigm = tmpPar.stream()
				.map(Integer::parseInt).collect(Collectors.toCollection(HashSet<Integer>::new));
		// AltLemmas
		result.altLemmas = loadHeaderListBlock(fields, elemFact,
				"Gram", "AltLemmas");
		// FormRestrictions
		result.formRestrictions = loadHeaderListBlock(fields, elemFact,
				"Gram", "FormRestrictions");
		// Flags
		result.flags = loadFlagsBlock(fields, elemFact,
				"Gram");
		// FreeText
		result.freeText = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Gram", "FreeText");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Gram");
		return result;
	}

	public Gloss makeGloss(Node glossVarNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Gloss result = elemFact.getNewGloss();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) glossVarNode);
		if (fields == null || fields.isEmpty()) return null;

		// GlossText
		result.text = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"GlossVariant", "GlossText");
		// Gram
		result.grammar = loadGramBlock(fields, elemFact,
				"GlossVariant");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "GlossVariant");
		return result;
	}

	public Sample makeSample(Node sampleNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Sample result = elemFact.getNewSample();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) sampleNode);
		if (fields == null || fields.isEmpty()) return null;

		// Content
		result.text = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Sample", "Content");
		// Gram
		result.grammar = loadGramBlock(fields, elemFact, "Sample");
		// CitedSource
		result.citedSource = XmlFieldMappingHandler.me().takeoutSinglarStringField(fields,
				"Sample", "CitedSource");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Sample");
		return result;
	}

	public Sources makeSources(Node sourcesNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Sources result = elemFact.getNewSources();
		result.s = DomIoUtils.getPrimitiveArrayFromXml(sourcesNode, "Source");
		if (result.s != null && result.s.isEmpty()) return null;
		return result;
	}

	/**
	 * Standarta brīdinājums, ko lieto, lai pateiktu, ka pēc apstrādes
	 * XmlFieldMapping struktūrā ir palicis kas neapstrādāts (neizņemts).
	 * @param fields	struktūra, ko pārbaudīt
	 * @param parentElemName	elementa vārds, ko izmantot kļūdas paziņojumā
	 */
	protected void dieOnNonempty(XmlFieldMapping fields, String parentElemName)
	throws DictionaryXmlReadingException
	{
		if (!fields.isEmpty())
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasts neatpazina \"%s\"!", parentElemName,
					fields.keyStringForLog()));
	}

	/**
	 * Standarta brīdinājums, ko lieto, lai pateiktu, ka pēc apstrādes
	 * ir radies šķirklis bez šķirkļavārda, vai šķirkļavārds nesakrīt ar XMLā
	 * doto LemmaSign atribūtu.
	 */
	protected void checkMainLemma(Entry e, String lemmaSign)
	throws DictionaryXmlReadingException
	{
		if (e.head == null || e.head.lemma == null || e.head.lemma.text == null)
			throw new DictionaryXmlReadingException(
					"Uztaisīts šķirklis bez šķirkļavārda!");
		if (!e.head.lemma.text.equals(lemmaSign))
			throw new DictionaryXmlReadingException(String.format(
					"Šķirkļa galvenā Header lemma %s nesakrīt ar LemmaSign atribūtu %s!",
					e.head.lemma.text, lemmaSign));
	}

	//===== Bloku lādēšana =====================================================
	//======== Masīvu bloki ====================================================

	protected LinkedList<Header> loadHeaderListBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		LinkedList<Node> headerNodes = XmlFieldMappingHandler.me().takeoutNodeList(fields,
				parentElemName, elemName, "Header");
		if (headerNodes == null) return null;
		LinkedList<Header> result = new LinkedList<>();
		for (Node hNode : headerNodes)
		{
			Header h = makeHeader(hNode, elemFact);
			if (h != null) result.add(h);
		}
		return result.isEmpty() ? null : result;
	}

	protected LinkedList<Sense> loadSensesBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		LinkedList<Node> senseNodes = XmlFieldMappingHandler.me().takeoutNodeList(fields,
				parentElemName, elemName, "Sense");
		if (senseNodes == null) return null;
		LinkedList<Sense> result = new LinkedList<>();
		for (Node sNode : senseNodes)
		{
			Sense s = makeSense(sNode, elemFact);
			if (s != null) result.add(s);
		}
		return result.isEmpty() ? null : result;
	}

	protected LinkedList<Gloss> loadGlosesBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		LinkedList<Node> glossVarNodes = XmlFieldMappingHandler.me().takeoutNodeList(fields,
				parentElemName, "Gloss", "GlossVariant");
		if (glossVarNodes == null) return null;
		LinkedList<Gloss> result = new LinkedList<>();
		for (Node gNode : glossVarNodes)
		{
			Gloss g = makeGloss(gNode, elemFact);
			if (g != null) result.add(g);
		}
		return result.isEmpty() ? null : result;
	}

	protected LinkedList<Phrase> loadStablePhrasesBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		LinkedList<Node> phraseNodes = XmlFieldMappingHandler.me().takeoutNodeList(fields,
				parentElemName, elemName, "Phrase");
		if (phraseNodes == null) return null;
		LinkedList<Phrase> result = new LinkedList<>();
		for (Node pNode : phraseNodes)
		{
			Phrase p = makePhrase(pNode, elemFact);
			if (p != null) result.add(p);
		}
		return result.isEmpty() ? null : result;
	}

	protected LinkedList<Sample> loadExamplesBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		LinkedList<Node> sampleNodes = XmlFieldMappingHandler.me().takeoutNodeList(fields,
				parentElemName, "Examples", "Sample");
		if (sampleNodes == null) return null;
		LinkedList<Sample> result = new LinkedList<>();
		for (Node sNode : sampleNodes)
		{
			Sample s = makeSample(sNode, elemFact);
			if (s != null) result.add(s);
		}
		return result.isEmpty() ? null : result;
	}

	//======== Vienpatņu elementu bloki ========================================

	protected Header loadSingularHeaderBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> headerNodes = fields.removeNodeChildren("Header");
		if (headerNodes != null && headerNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Header\"!", parentElemName));
		if (headerNodes != null && !headerNodes.isEmpty())
			return makeHeader(headerNodes.get(0), elemFact);
		return null;
	}

	protected Sources loadSourcesBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> sourcesNodes = fields.removeNodeChildren("Sources");
		if (sourcesNodes != null && sourcesNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Sources\"!", parentElemName));
		if (sourcesNodes!= null && !sourcesNodes.isEmpty())
			return makeSources(sourcesNodes.get(0), elemFact);
		return null;
	}

	protected Gram loadGramBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> gramNodes = fields.removeNodeChildren("Gram");
		if (gramNodes != null && gramNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Gram\"!", parentElemName));
		if (gramNodes!= null && !gramNodes.isEmpty())
			return makeGram(gramNodes.get(0), elemFact);
		return null;
	}

	protected Flags loadFlagsBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> flagsNodes = fields.removeNodeChildren("Flags");
		if (flagsNodes != null && flagsNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Flags\"!", parentElemName));
		if (flagsNodes!= null && !flagsNodes.isEmpty())
			return makeFlags(flagsNodes.get(0), elemFact);
		return null;
	}

	protected Lemma loadLemmaBlock(
			XmlFieldMapping fields, GenericElementFactory elemFact,
			String parentElemName) throws DictionaryXmlReadingException
	{
		ArrayList<String> lemmaTexts = fields.removeStringChildren("Lemma");
		if (lemmaTexts != null && lemmaTexts.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Lemma\"!", parentElemName));
		if (lemmaTexts!= null && !lemmaTexts.isEmpty())
		{
			Lemma result = elemFact.getNewLemma();
			result.text = lemmaTexts.get(0);
			return result;
		}
		return null;
	}
}
