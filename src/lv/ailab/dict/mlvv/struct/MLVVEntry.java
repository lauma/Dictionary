package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.struct.*;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * MLVV šķirkļa struktūra + word izguve.
 * TODO: uzrakstīt smuku json eksportu
 * Piemēri: miers (vairāki stabilie savienojumi), māja (vairākas nozīmes frāzei)
 * mākoņains (vairāki atvasinājumi)
 *
 * Izveidots 2016-02-02.
 * @author Lauma
 *
 */
public class MLVVEntry extends Entry
{
	/**
	 * Vai <i> tagus izcelsmē vajag automātiski aizvietot ar apakšsvītrām vai
	 * labāk ar <em>?
	 */
	public static boolean UNDERSCORE_FOR_ETYMOLOGY_CURSIVE = false;
	/**
	 * Vai <i> tagus normatīvajā komentārā vajag automātiski aizvietot ar
	 * apakšsvītrām vai labāk ar <em>?
	 */
	public static boolean UNDERSCORE_FOR_NORMATIVE_CURSIVE = false;
	/**
	 * MLVV tiek šķirti divu veidu brīvi komentāri - komentārs par vārda cilmi
	 * (izcelsmi) tiek glabāts Entry.etymology, bet normatīvā lietojuma
	 * komentārs - MLVV.normative
	 */
	public String normative;

	protected MLVVEntry(){};

	/**
	 * Savāc izsecinātos hederus (altLemmas) - šis ir drošības pēc pārrakstīts,
	 * jo, lai gan šobrīd frazeoloģismim nevajadzētu saturēt altLemmas, tomēr
	 * strukturāli tas pieļaujas, tāpēc nākotnē var rasties pārpratumi, šo
	 * nepārrakstot.
	 */
	@Override
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList<>();
		if (head != null) res.addAll(head.getImplicitHeaders());
		if (senses != null) for (Sense s : senses)
			res.addAll(s.getImplicitHeaders());
		if (phrases != null) for (Phrase p : phrases)
			res.addAll(p.getImplicitHeaders());
		//if (phraseology != null) for (Phrase p : phraseology)
		//	res.addAll(p.getImplicitHeaders());
		if (derivs != null) for (Header d : derivs)
			res.addAll(d.getImplicitHeaders());
		return res;
	}

	/**
	 * Savāc elementā izmantotos "flagText". Semikolu uzskata par atdalītāju.
	 */
	public TreeSet<String> getFlagStrings()
	{
		TreeSet<String> res = new TreeSet<>();
		if (head != null) res.addAll(((MLVVHeader)head).getFlagStrings());
		if (senses != null) for (Sense s : senses)
			res.addAll(((MLVVSense)s).getFlagStrings());
		if (phrases != null) for (Phrase p : phrases)
			res.addAll(((MLVVPhrase)p).getFlagStrings());
		//if (phraseology != null) for (Phrase p : phraseology)
		//	res.addAll(((MLVVPhrase)p).getFlagStrings());
		if (derivs != null) for (Header d : derivs)
			res.addAll(((MLVVHeader)d).getFlagStrings());
		return res;
	}

	/**
	 * Uzbūvē JSON reprezentāciju.
	 * TODO nebūvēt manuāli
	 * @return JSON representation
	 */
	public String toJSON()
	{
		StringBuilder s = new StringBuilder();
		s.append('{');
		s.append(head.toJSON());
		/*if (paradigm != 0) {
			s.append(String.format(",\"Paradigm\":%d", paradigm));
			if (analyzer != null) {
				// generate a list of inflected wordforms and format them as JSON array
				ArrayList<Wordform> inflections = analyzer.generateInflections(lemma.l, paradigm);
				s.append(String.format(",\"Inflections\":%s", formatInflections(inflections) ));
			}
		}//*/

		if (homId != null)
		{
			s.append(", \"HomonymNumber\":\"");
			s.append(JSONObject.escape(homId));
			s.append("\"");
		}

		if (senses != null && !senses.isEmpty())
		{
			s.append(", \"Senses\":");
			s.append(JSONUtils.objectsToJSON(senses));
		}

		if (phrases != null && !phrases.isEmpty())
		{
			s.append(", \"StablePhrases\":");
			s.append(JSONUtils.objectsToJSON(phrases));
		}
		/*if (phraseology != null && !phraseology.isEmpty())
		{
			s.append(", \"Phraseology\":");
			s.append(JSONUtils.objectsToJSON(phraseology));
		}*/

		/*if ((senses == null || senses.isEmpty()) &&
				((phrases != null && !phrases.isEmpty()) || (phraseology != null && !phraseology.isEmpty())))
		{
			System.out.printf(
					"Šķirklim \"%s\" norādītas frāzes, bet nav nozīmes!\n",
						head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
		}*/

		if (derivs != null && !derivs.isEmpty())
		{
			s.append(", \"Derivatives\":");
			s.append(JSONUtils.objectsToJSON(derivs));
		}
		if (etymology != null && etymology.length() > 0)
		{
			s.append(", \"Etymology\":\"");
			s.append(JSONObject.escape(etymology));
			s.append("\"");
		}
		if (normative != null && normative.length() > 0)
		{
			s.append(", \"Normative\":\"");
			s.append(JSONObject.escape(normative));
			s.append("\"");
		}
		if (references != null && references.size() > 0)
		{
			s.append(", \"References\":[");
			s.append(references.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
					.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			s.append("]");
		}
		if (sources != null && !sources.isEmpty())
		{
			System.out.printf("Šķirklim \"%s\" norādīti avoti, lai gan MLVV šo lauku nav paredzēts aizpildīt!\n",
							head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
			s.append(",");
			s.append(sources.toJSON());
		}
		s.append('}');
		return s.toString();
	}

	/**
	 * Override, lai MLVV šķirkļos nedrukā galvenā header lemmu, jo tā jau ir
	 * iekļauta altLemmu sarakstā.
	 */
	@Override
	public Element toXML(Document doc)
	{
		Element entryN = doc.createElement("Entry");

		if (homId != null)
			entryN.setAttribute("HomonymNumber", homId);
		if (head != null)
		{
			//head.toXML(entryN);
			if (head.lemma != null && head.lemma.text != null)
				entryN.setAttribute("LemmaSign", head.lemma.text);
			if (head.lemma != null && head.lemma.pronunciation != null && head.lemma.pronunciation.length > 0)
			{
				System.out
						.printf("Šķirklim \"%s\" šķirkļavārdam ir norādīta izruna, lai gan šo lauku MLVV nav paredzēts aizpildīt!\n",
								head.lemma.text != null ? head.lemma.text : "");
				head.toXML(entryN);
			} else if (head.gram != null)
			{
				Node headerN = doc.createElement("Header");
				head.gram.toXML(headerN);
				entryN.appendChild(headerN);
			}
		}
		contentsToXML(entryN);

		return entryN;
	}

	/**
	 * Override, lai šķirkļa saturs saturētu visu vajadzīgo.
	 */
	@Override
	public void contentsToXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (senses != null && !senses.isEmpty())
		{
			Node sensesContN = doc.createElement("Senses");
			for (Sense s : senses) s.toXML(sensesContN);
			parent.appendChild(sensesContN);
		}
		if (phrases != null && !phrases.isEmpty())
		{
			Node phrasesContN = doc.createElement("StablePhrases");
			for (Phrase p : phrases) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}
		/*if (phraseology != null && !phraseology.isEmpty())
		{
			Node phrasesContN = doc.createElement("Phraseology");
			for (Phrase p : phraseology) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}*/
		/*if ((senses == null || senses.isEmpty()) &&
				((phrases != null && !phrases.isEmpty()) || (phraseology != null && !phraseology.isEmpty())))
		{
			System.out.printf(
					"Šķirklim \"%s\" norādītas frāzes, bet nav nozīmes!\n",
					head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
		}*/
		if (derivs != null && !derivs.isEmpty())
		{
			Node derivContN = doc.createElement("Derivatives");
			for (Header d : derivs) d.toXML(derivContN);
			parent.appendChild(derivContN);
		}
		if (etymology != null && etymology.length() > 0)
		{
			Node etymNode = doc.createElement("Etymology");
			etymNode.appendChild(doc.createTextNode(etymology));
			parent.appendChild(etymNode);
		}
		if (normative != null && normative.length() > 0)
		{
			Node freeTextN = doc.createElement("Normative");
			freeTextN.appendChild(doc.createTextNode(normative));
			parent.appendChild(freeTextN);
		}
		if (references != null && references.size() > 0)
		{
			Node refContainer = doc.createElement("References");
			//refN.appendChild(doc.createTextNode(references));
			for (String ref : references)
			{
				Node refItem = doc.createElement("EntryRef");
				refItem.appendChild(doc.createTextNode(ref));
				refContainer.appendChild(refItem);
			}
			parent.appendChild(refContainer);
		}
		if (sources != null && !sources.isEmpty())
		{
			System.out
					.printf("Šķirklim \"%s\" norādīti avoti, lai gan MLVV šo lauku nav paredzēts aizpildīt!\n",
							head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
			sources.toXML(parent);
		}

	}
}