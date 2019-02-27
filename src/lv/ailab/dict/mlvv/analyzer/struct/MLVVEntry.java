package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.*;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * Vai <i> tagus izcelsmē vajag automātiski aizvietot ar apakšsvītrām?
	 */
	public static boolean UNDERSCORE_FOR_ETYMOLOGY_CURSIVE = false;
	/**
	 * Vai <i> tagus normatīvajā komentārā vajag automātiski aizvietot ar
	 * apakšsvītrām?
	 */
	public static boolean UNDERSCORE_FOR_NORMATIVE_CURSIVE = false;

	/**
	 * MLVV atšķirībā no citām vārdnīcām tiek šķirti stabili vārdu savienojumi
	 * no frazeoloģismiem. Entry.phrases tiek lietots stabiliem vārdu
	 * savienojumiem (atdalīts ar trijstūri), bet MLVVEntry.phraseology -
	 * frazeoloģismiem (atsalīts ar rombu).
	 */
	public LinkedList<Phrase> phraseology;

	/**
	 * MLVV tiek šķirti divu veidu brīvi komentāri - komentārs par vārda cilmi
	 * (izcelsmi) tiek glabāts Entry.etymology, bet normatīvā lietojuma
	 * komentārs - MLVV.normative
	 */
	public String normative;

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
		if (phraseology != null) for (Phrase p : phraseology)
			res.addAll(p.getImplicitHeaders());
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
		if (phraseology != null) for (Phrase p : phraseology)
			res.addAll(((MLVVPhrase)p).getFlagStrings());
		if (derivs != null) for (Header d : derivs)
			res.addAll(((MLVVHeader)d).getFlagStrings());
		return res;
	}

	/**
	 * Izanalizē rindu, un atgriež null, ja tajā nekā nav, vai MLVVEntry, ja no
	 * šīs rindas tādu var izgūt.
	 */
	public static MLVVEntry parse(String line)
	{
		if (line == null || line.isEmpty()) return null;

		// Ja rinda satur tikai burtu, neizgūst neko.
		if (line.matches("<b>\\p{L}\\p{M}*</b>")) return null;

		MLVVEntry result = new MLVVEntry();

		// Atdala šķirkļa galvu.
		String head, body;
		Matcher multiSenseWithGram = Pattern.compile("(<b>.*?</b>.*?)(<b>1\\.</b>.*)")
				.matcher(line);
		Matcher multiSenseNoGram = Pattern.compile("(<b>(?!</?[ib]>).*)\\s(1\\.</b>.*?<b>2\\.</b>.*)")
				.matcher(line);
		// Šķirklis ar numurētām nozīmēm un gramatiku pirms pirmās nozīmes
		if (multiSenseWithGram.matches())
		{
			head = multiSenseWithGram.group(1);
			body = multiSenseWithGram.group(2);
		}
		// Šķirklis ir ar numurētām nozīmēm, bet bez gramatikas pirms pirmās nozīmes.
		else if (multiSenseNoGram.matches())
		{
			head = multiSenseNoGram.group(1) + "</b>";
			body = "<b>" + multiSenseNoGram.group(2);
		}
		// Šķirklis ar vienu nozīmi.
		else
		{
			Matcher m2 = Pattern.compile("(<b>.*?</b>\\*?\\s*)(.*)").matcher(line); // pirmais šķirkļavārds + zvaigznīte jaunvārdiem
			if (m2.matches())
			{
				head = m2.group(1);
				body = m2.group(2);
				Pattern headpart = Pattern.compile(
						"((?:" +
								"\\s*(?:\\p{Ll}\\p{M}*)+(?:\\s\\[[^\\]]+\\])?[.,]|" +	// forma ar neobligātu izrunu
								"(?:[,;]\\s*)?(?:(?:arī|retāk)\\s*)?<b>.*?</b>[,.;]?|" + 			// cita lemma
								"\\((?:(?!<b>|\\)).)*\\s*<b>.*?</b>(?:,?\\s+-(?:\\p{Ll}\\p{M}*)+,?)*\\s*\\)[,.;]?|" + 			// cita lemma iekavās
								":?\\s*<i>.*?</i>[,.;]?|<sup>.*?</sup>[,.;]?|" + 		// gram. kursīvā | homonīma indekss
								"\\[.*?\\][,.;]?|[-,][^.<]*\\.?|" + 			// [izruna] | "galotne"
								"(?:[:,]\\s)?<u>.*?</u>\\.?" +  			//formas ierobežojums
						")\\s*)(.*)");	// atstarpe pārējais
				m2 = headpart.matcher(body);
				while (m2.matches())
				{
					head = head + m2.group(1);
					body = m2.group(2);
					m2 = headpart.matcher(body);
				}
			} else
			{
				System.out.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
				return null;
				//throw new IllegalArgumentException("Can't match input line with regexp!");
			}
		}
		head = head.trim();
		body = body.trim();
		// Izparsē referenci.
		if (body.isEmpty() && head.matches(".*?<i>[Ss]k(at)?(</i>.|.</i>)\\s+[-\\p{L}]+\\."))
		{
			String referencesText = head.substring(head.lastIndexOf("</i>") + 4).trim();
			if (referencesText.endsWith("."))
				referencesText = referencesText.substring(0, referencesText.length()-1);
			head = head.substring(0, head.lastIndexOf("<i>"));

			if (!referencesText.isEmpty())
			{
				result.references = new LinkedList<>();
				result.references.addAll(Arrays.asList(referencesText.split(", ")));
				// TODO: vai šeit vajag dalījumu?
			}
		}
		if (head.isEmpty())
		{
			System.out.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
			return null;
		}
		else result.parseHead(head);
		if (body.isEmpty() && result.references == null )
			System.out.println("Neizdodas izgūt šķirkļa ķermeni no šīs rindas:\n\t" + line);
		else if (!body.isEmpty()) result.parseBody(body);
		return result;
	}

	/**
	 * TODO karodziņi
	 * TODO izrunas
	 */
	protected void parseHead(String linePart)
	{
		head = new MLVVHeader();
		Matcher m = Pattern.compile("<b>(.+?)(\\*?)</b>(\\*?)\\s*(.*)").matcher(linePart);
		if (m.matches())
		{
			head.lemma = new Lemma(m.group(1).trim());
			String star = m.group(2) + m.group(3);
			String gram = m.group(4);
			// Homonīma infekss, ja tāds ir.
			m = Pattern.compile("^<sup>\\s*(?:<b>)?(.*?)(?:</b>)?\\s*</sup>\\s*(.*)")
					.matcher(gram);
			if (m.matches())
			{
				if (homId != null) System.out.printf(
						"No šķirkļavārda \"%s\" homonīma indeksu mēģina piešķiert vairākkārt: vispirms %s, tad %s!",
						head.lemma, homId, m.group(1));
				homId = m.group(1);
				// Izmet ārā homonīma indeksu.
				linePart = "<b>" + head.lemma.text + "</b> " + m.group(2);
			} else linePart = "<b>" + head.lemma.text + "</b>" + gram;
			head.gram = MLVVGram.parse(linePart);
			if (star!= null && !star.isEmpty())((MLVVGram)head.gram).addStar();
		} else
		{
			System.out.printf(
					"Neizdodas izgūt pirmo šķirkļavārdu no šī rindas fragmenta:\n%s\n",
					linePart);
			head = null;
		}
	}

	protected void parseBody(String linePart)
	{
		linePart = linePart.trim();
		// Normatīvais komentārs
		Matcher m = Pattern.compile("(.*)<gray>(.*)</gray>(\\.?)(.*)").matcher(linePart);
		if (m.matches())
		{
			parseNormative((m.group(2) + m.group(3)).trim());
			linePart = m.group(1).trim();
			if (m.group(4).trim().length() > 0)
				linePart = linePart + " " + m.group(4).trim();
		}

		// Cilme
		m = Pattern.compile("(.*?)\\b[cC]ilme: (.*)").matcher(linePart);
		if (m.matches())
		{
			String newEtymology = m.group(2).trim();
			linePart = m.group(1).trim();
			if (newEtymology.contains("<square/>"))
			{
				linePart = linePart + " " + newEtymology.substring(newEtymology.indexOf("<square/>")).trim();
				newEtymology = newEtymology.substring(0, newEtymology.indexOf("<square/>")).trim();
			}
			parseEtymology(newEtymology);
		}

		// Atvasinājumi
		m = Pattern.compile("(.*?)<square/>(.*)").matcher(linePart);
		if (m.matches())
		{
			parseDerivs(m.group(2));
			linePart = m.group(1).trim();
		}

		// Frazeoloģismi
		m = Pattern.compile("(.*?)<diamond/>(.*)").matcher(linePart);
		if (m.matches())
		{
			parsePhraseology(m.group(2).trim());
			linePart = m.group(1).trim();
		}

		// Stabilie vārdu savienojumi
		m = Pattern.compile("(.*?)<triangle/>(.*)").matcher(linePart);
		if (m.matches())
		{
			parseStables(m.group(2).trim());
			linePart = m.group(1).trim();
		}

		if (linePart.length() > 0)
		parseSenses(linePart);

	}

	/**
	 * TODO vai viena gramatika var attiekties uz vairākām lemmām?
	 */
	protected void parseDerivs(String linePart)
	{
		String[] derivTexts = linePart.split("<square/>|\\s*(?=<b>)");
		if (derivTexts.length < 1) return;
		derivs = new LinkedList<>();
		for (String dt :derivTexts)
		{
			MLVVHeader h = MLVVHeader.parseSingularHeader(dt.trim());
			if (h != null) derivs.add(h);
		}
	}

	protected void parsePhraseology(String linePart)
	{
		String[] phrasesParts = linePart.split("<diamond/>");
		if (phrasesParts.length > 0 && phrases == null)  phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			MLVVPhrase p = MLVVPhrase.parseSpecialPhrasal(
					phraseText.trim(), Phrase.Type.PHRASEOLOGICAL, head.lemma.text);
			if (p != null) phrases.add(p);
		}
	}

	protected void parseStables(String linePart)
	{
		linePart = linePart.trim();
		String[] phrasesParts = linePart.split("<triangle/>");
		if (phrasesParts.length > 0 && phrases == null)  phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			MLVVPhrase p = MLVVPhrase.parseSpecialPhrasal(
					phraseText.trim(), Phrase.Type.STABLE_UNIT, head.lemma.text);
			if (p != null) phrases.add(p);
		}
	}
	protected void parseSenses(String linePart)
	{
		linePart = linePart.trim();
		if (linePart.length() < 1) return;
		String[] sensesParts = new String[]{linePart};
		boolean numberSenses = false;
		if (linePart.startsWith("<b>1.</b>"))
		{
			linePart = linePart.substring("<b>1.</b>".length());
			sensesParts = linePart.split("<b>\\d+\\.</b>");
			numberSenses = true;
		}
		senses = new LinkedList<>();
		for (int senseNumber = 0; senseNumber < sensesParts.length; senseNumber++)
		{
			MLVVSense s = MLVVSense.parse(sensesParts[senseNumber], head.lemma.text);
			if (s != null) senses.add(s);
			if (numberSenses && (s.ordNumber == null || s.ordNumber.isEmpty()))
				s.ordNumber = Integer.toString(senseNumber + 1);
		}
	}

	protected void parseEtymology(String linePart)
	{
		linePart = Editors.replaceHomIds(linePart, true);
		if (UNDERSCORE_FOR_ETYMOLOGY_CURSIVE)
			etymology = Editors.cursiveToUnderscore(linePart);
		else etymology = linePart;
	}

	protected void parseNormative(String linePart)
	{
		if (UNDERSCORE_FOR_NORMATIVE_CURSIVE)
			normative = Editors.cursiveToUnderscore(linePart);
		else normative = linePart;
	}
	// TODO - atstāt kvadrātiekavas vai mest ārā

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
			s.append(", \"ID\":\"");
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
		if (phraseology != null && !phraseology.isEmpty())
		{
			s.append(", \"Phraseology\":");
			s.append(JSONUtils.objectsToJSON(phraseology));
		}

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
		if (references != null && references.size() > 0)
		{
			s.append(", \"References\":[");
			s.append(references.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
					.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			s.append("]");
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
		if (phraseology != null && !phraseology.isEmpty())
		{
			Node phrasesContN = doc.createElement("Phraseology");
			for (Phrase p : phraseology) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}
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
		if (references != null && references.size() > 0)
		{
			Node refContainer = doc.createElement("References");
			//refN.appendChild(doc.createTextNode(references));
			for (String ref : references)
			{
				Node refItem = doc.createElement("EntryId");
				refItem.appendChild(doc.createTextNode(ref));
				refContainer.appendChild(refItem);
			}
			parent.appendChild(refContainer);
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
		if (sources != null && !sources.isEmpty())
		{
			System.out
					.printf("Šķirklim \"%s\" norādīti avoti, lai gan MLVV šo lauku nav paredzēts aizpildīt!\n",
							head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
			sources.toXML(parent);
		}

	}
}