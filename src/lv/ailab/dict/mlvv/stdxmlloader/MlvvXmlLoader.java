package lv.ailab.dict.mlvv.stdxmlloader;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.stdxml.StdXmlLoader;
import lv.ailab.dict.io.stdxml.XmlFieldMapping;
import lv.ailab.dict.io.stdxml.XmlFieldMappingHandler;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.mlvv.struct.MLVVEntry;
import lv.ailab.dict.mlvv.struct.MLVVGram;
import lv.ailab.dict.mlvv.struct.MLVVPhrase;
import lv.ailab.dict.struct.GenericElementFactory;
import lv.ailab.dict.struct.Phrase;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MlvvXmlLoader extends StdXmlLoader
{
	protected MlvvXmlLoader() {};
	protected static MlvvXmlLoader singleton = new MlvvXmlLoader();
	public static MlvvXmlLoader me()
	{
		return singleton;
	}

	@Override
	public MLVVEntry makeEntry(Node entryNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		MLVVElementFactory castedFact = (MLVVElementFactory) elemFact;
		MLVVEntry result = castedFact.getNewEntry();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) entryNode);
		if (fields == null || fields.isEmpty()) return null;

		// HomonymNumber
		result.homId = XmlFieldMappingHandler.me().getSinglarStringField(fields,
				"Entry", "HomonymNumber");
		// Header
		result.head = loadSingularHeaderBlock(fields, castedFact,
				"Entry");
		// Senses
		result.senses = loadSensesBlock(fields, castedFact,
				"Entry", "Senses");
		// StablePhrases
		result.phrases = loadStablePhrasesBlock(fields, castedFact,
				"Entry");
		//Derivatives
		result.derivs = loadHeaderListBlock(fields, castedFact,
				"Entry", "Derivatives");
		// Etymology
		result.etymology = XmlFieldMappingHandler.me().getSinglarStringField(fields,
				"Entry", "Etymology");
		// Normative
		result.normative = XmlFieldMappingHandler.me().getSinglarStringField(fields,
				"Entry", "Normative");
		// References
		result.references = XmlFieldMappingHandler.me().getStringFieldArray(fields,
				"Entry", "References", "EntryRef");
		// Sources - nav te plānots būt.
		//result.sources = loadSourcesBlock(fields, castedFact, "Entry");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Entry");
		return result;
	}

	@Override
	public MLVVGram makeGram(Node gramNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		MLVVElementFactory castedFact = (MLVVElementFactory) elemFact;
		MLVVGram result = castedFact.getNewGram();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) gramNode);
		if (fields == null || fields.isEmpty()) return null;

		// Paradigms - šobrīd nav
		/*LinkedList<String> tmpPar = XmlFieldMappingHandler.me().getStringFieldArray(fields,
				"Gram", "Paradigms", "Paradigm");
		if (tmpPar != null) result.paradigm = tmpPar.stream()
				.map(Integer::parseInt).collect(Collectors.toCollection(HashSet<Integer>::new));
		 */
		// AltLemmas
		result.altLemmas = loadHeaderListBlock(fields, castedFact,
				"Gram", "AltLemmas");
		// FormRestrictions
		result.formRestrictions = loadHeaderListBlock(fields, castedFact,
				"Gram", "FormRestrictions");
		// Flags - šobrīd nav.
		//result.flags = loadFlagsBlock(fields, castedFact, "Gram");
		// Inflections
		result.freeText = XmlFieldMappingHandler.me().getSinglarStringField(fields,
				"Gram", "Inflections");
		// FlagText
		result.flagText = XmlFieldMappingHandler.me().getSinglarStringField(fields,
				"Gram", "FlagText");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Gram");
		return result;
	}


	@Override
	public MLVVPhrase makePhrase(Node phraseNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		MLVVElementFactory castedFact = (MLVVElementFactory) elemFact;
		MLVVPhrase result = castedFact.getNewPhrase();
		XmlFieldMapping fields = XmlFieldMappingHandler.me().domElemToHash((Element) phraseNode);
		if (fields == null || fields.isEmpty()) return null;

		// Type
		String typeStr = XmlFieldMappingHandler.me().getSinglarStringField(fields,
				"Phrase", "Type");
		if (typeStr != null) result.type = Phrase.Type.parseString(typeStr);
		// Text
		result.text = XmlFieldMappingHandler.me().getStringFieldArray(fields,
				"Phrase", "Text", "Variant");
		// SciName
		result.sciName = XmlFieldMappingHandler.me().getStringFieldArray(fields,
				"Phrase", "SciName", "Variant");
		// Gram
		result.grammar = loadGramBlock(fields, elemFact, "Phrase");
		// Senses
		result.subsenses = loadSensesBlock(fields, elemFact,
				"Phrase", "Senses");
		// Brīdina, ja ir vēl kaut kas.
		dieOnNonempty(fields, "Phrase");
		return result;
	}
}
