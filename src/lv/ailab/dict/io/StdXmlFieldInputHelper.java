package lv.ailab.dict.io;

import lv.ailab.dict.struct.*;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.LinkedList;

public class StdXmlFieldInputHelper
{
	/**
	 * Standarta brīdinājums, ko lieto, lai pateiktu, ka pēc apstrādes
	 * FieldMapping struktūrā ir palicis kas neapstrādāts (neizņemts).
	 * @param fields	struktūra, ko pārbaudīt
	 * @param parentElemName	elementa vārds, ko izmantot kļūdas paziņojumā
	 */
	public static void dieOnNonempty(DomIoUtils.FieldMapping fields, String parentElemName)
	throws DictionaryXmlReadingException
	{
		if (!fields.isEmpty())
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasts neatpazina \"%s\"!", parentElemName,
					fields.keyStringForLog()));
	}

	public static String getSinglarStringField(
			DomIoUtils.FieldMapping fields, String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<String> fieldTexts = fields.stringChildren.remove(elemName);
		if (fieldTexts != null && fieldTexts.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!",
					parentElemName, elemName));
		if (fieldTexts!= null && !fieldTexts.isEmpty())
			return fieldTexts.get(0);
		return null;
	}

	public static LinkedList<String> getStringFieldArray(
			DomIoUtils.FieldMapping fields, String parentElemName,
			String elemName, String childElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> referencesNodes = fields.nodeChildren.remove(elemName);
		if (referencesNodes != null && referencesNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!",
					parentElemName, elemName));
		if (referencesNodes != null && !referencesNodes.isEmpty())
		{
			LinkedList<String> entryRefs = DomIoUtils.getPrimitiveArrayFromXml(
					referencesNodes.get(0), childElemName);
			if (entryRefs != null && entryRefs.size() > 0)
				return entryRefs;
			return null;
		}
		return null;
	}

	public static LinkedList<Header> getHeaderList(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> derivativesNodes = fields.nodeChildren.remove(elemName);
		if (derivativesNodes != null && derivativesNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!",
					parentElemName, elemName));
		if (derivativesNodes!= null && !derivativesNodes.isEmpty())
		{
			LinkedList<Node> derHNodes = DomIoUtils.getSingleTypeNodeArrayFromXml(
					derivativesNodes.get(0), "Header");
			if (derHNodes != null)
			{
				LinkedList<Header> result = new LinkedList<>();
				for (Node hNode : derHNodes)
				{
					Header h = Header.fromStdXML(hNode, elemFact);
					if (h != null) result.add(h);
				}
				if (result.isEmpty()) return null;
				return result;
			}
		}
		return null;
	}

	public static Header getSingularHeader(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> headerNodes = fields.nodeChildren.remove("Header");
		if (headerNodes != null && headerNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Header\"!", parentElemName));
		if (headerNodes != null && !headerNodes.isEmpty())
			return Header.fromStdXML(headerNodes.get(0), elemFact);
		return null;
	}

	public static LinkedList<Sense> getSenses(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> sensesNodes = fields.nodeChildren.remove(elemName);
		if (sensesNodes != null && sensesNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!", parentElemName, elemName));
		if (sensesNodes!= null && !sensesNodes.isEmpty())
		{
			LinkedList<Node> senseNodes = DomIoUtils.getSingleTypeNodeArrayFromXml(
					sensesNodes.get(0), "Sense");
			if (senseNodes != null)
			{
				LinkedList<Sense> result = new LinkedList<>();
				for (Node sNode : senseNodes)
				{
					Sense s = Sense.fromStdXML(sNode, elemFact);
					if (s != null) result.add(s);
				}
				if (result.isEmpty()) return null;
				return result;
			}
		}
		return null;
	}

	public static LinkedList<Gloss> getGloses(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> glossNodes = fields.nodeChildren.remove("Gloss");
		if (glossNodes != null && glossNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Gloss\"!", parentElemName));
		if (glossNodes!= null && !glossNodes.isEmpty())
		{
			LinkedList<Node> glossVarNodes = DomIoUtils.getSingleTypeNodeArrayFromXml(
					glossNodes.get(0), "GlossVariant");
			if (glossVarNodes != null)
			{
				LinkedList<Gloss> result = new LinkedList<>();
				for (Node gNode : glossVarNodes)
				{
					Gloss g = Gloss.fromStdXML(gNode, elemFact);
					if (g != null) result.add(g);
				}
				if (result.isEmpty()) return null;
				return result;
			}
		}
		return null;
	}

	public static LinkedList<Phrase> getStablePhrases(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> phrasesNodes = fields.nodeChildren.remove("StablePhrases");
		if (phrasesNodes != null && phrasesNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"StablePhrases\"!", parentElemName));
		if (phrasesNodes!= null && !phrasesNodes.isEmpty())
		{
			LinkedList<Node> phraseNodes = DomIoUtils.getSingleTypeNodeArrayFromXml(
					phrasesNodes.get(0), "Phrase");
			if (phraseNodes != null)
			{
				LinkedList<Phrase> result = new LinkedList<>();
				for (Node phrNode : phraseNodes)
				{
					Phrase p = Phrase.fromStdXML(phrNode, elemFact);
					if (p != null) result.add(p);
				}
				if (result.isEmpty()) return null;
				return result;
			}
		}
		return null;
	}

	public static LinkedList<Sample> getExamples(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> examplesNodes = fields.nodeChildren.remove("Examples");
		if (examplesNodes != null && examplesNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Examples\"!", parentElemName));
		if (examplesNodes!= null && !examplesNodes.isEmpty())
		{
			LinkedList<Node> sampleNodes = DomIoUtils.getSingleTypeNodeArrayFromXml(
					examplesNodes.get(0), "Sample");
			if (sampleNodes != null)
			{
				LinkedList<Sample> result = new LinkedList<>();
				for (Node sNode : sampleNodes)
				{
					Sample s = Sample.fromStdXML(sNode, elemFact);
					if (s != null) result.add(s);
				}
				if (result.isEmpty()) return null;
				return result;
			}
		}
		return null;
	}

	public static Sources getSources(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> sourcesNodes = fields.nodeChildren.remove("Sources");
		if (sourcesNodes != null && sourcesNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Sources\"!", parentElemName));
		if (sourcesNodes!= null && !sourcesNodes.isEmpty())
			return Sources.fromStdXML(sourcesNodes.get(0), elemFact);
		return null;
	}

	public static Gram getGram(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> gramNodes = fields.nodeChildren.remove("Gram");
		if (gramNodes != null && gramNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Gram\"!", parentElemName));
		if (gramNodes!= null && !gramNodes.isEmpty())
			return Gram.fromStdXML(gramNodes.get(0), elemFact);
		return null;
	}

	public static Flags getFlags(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> flagsNodes = fields.nodeChildren.remove("Flags");
		if (flagsNodes != null && flagsNodes.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"Flags\"!", parentElemName));
		if (flagsNodes!= null && !flagsNodes.isEmpty())
			return Flags.fromStdXML(flagsNodes.get(0), elemFact);
		return null;
	}

	public static Lemma getLemma(
			DomIoUtils.FieldMapping fields, GenericElementFactory elemFact,
			String parentElemName) throws DictionaryXmlReadingException
	{
		ArrayList<String> lemmaTexts = fields.stringChildren.remove("Lemma");
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
