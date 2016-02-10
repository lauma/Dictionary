package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MLVV šķirkļa struktūra + word izguve.
 * TODO: vai origin un nevajadzētu izcelt uz Entry?
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
	 * MLVV atšķirībā no citām vārdnīcām tiek šķirti stabili vārdu savienojumi
	 * no frazeoloģismiem. Entry.phrases tiek lietots stabiliem vārdu
	 * savienojumiem (atdalīts ar trijstūri), bet MLVVEntry.phraseology -
	 * frazeoloģismiem (atsalīts ar rombu).
	 */
	public LinkedList<Phrase> phraseology;
	/**
	 * MLVV tiek šķirti divu veidu brīci komentāri - komentārs par vārda cilmi
	 * (izcelsmi) tiek glabāts MLVVEntry.origin, bet normatīvā lietojuma
	 * komentārs - Entry.freeText
	 */
	public String origin;

	/**
	 * Izanalizē rindu, un atgriež null, ja tajā nekā nav, vai MLVVEntry, ja no
	 * šīs rindas tādu var izgūt.
	 */
	public static MLVVEntry extractFromString(String line)
	{
		if (line == null || line.isEmpty()) return null;

		// Ja rinda satur tikai burtu, neizgūst neko.
		if (line.matches("<b>\\p{L}\\p{M}*</b>")) return null;

		else return new MLVVEntry(line);
	}

	/**
	 * Konstruktors, kas no dotās rindas cenšas izgūt šķirkli, pieņemot, ka tas
	 * tur noteikti ir.
	 */
	protected MLVVEntry(String line)
	{
		super();
		// Atdala šķirkļa galvu.
		String head, body;
		Matcher m1 = Pattern.compile("(<b>.*?</b>.*?)(<b>1\\.</b>.*)")
				.matcher(line);
		if (m1.matches()) // Šķirklis ar numurētām nozīmēm
		{
			head = m1.group(1);
			body = m1.group(2);
		} else // Šķirklis ar vienu nozīmi.
		{
			Matcher m2 = Pattern.compile("(<b>.*?</b>\\s*)(.*)").matcher(line);
			if (m2.matches())
			{
				head = m2.group(1);
				body = m2.group(2);
				Pattern headpart = Pattern.compile(
						"((?:<b>.*?</b>|<i>.*?</i>|<sup>.*?</sup>|\\[.*?\\]|(?:[-,].*?(?=<)))\\s*)(.*)");
				m2 = headpart.matcher(body);
				while (m2.matches())
				{
					head = head + m2.group(1);
					body = m2.group(2);
					m2 = headpart.matcher(body);
				}
			} else
			{
				System.out
						.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
				throw new IllegalArgumentException("Can't match input line with regexp!");
			}
		}
		head = head.trim();
		body = body.trim();
		if (head.isEmpty())
			System.out
					.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
		else extractHead(head);
		if (body.isEmpty())
			System.out
					.println("Neizdodas izgūt šķirkļa ķermeni no šīs rindas:\n\t" + line);
		else this.extractBody(body);
	}

	/**
	 * TODO karodziņi
	 * TODO izrunas
	 */
	protected void extractHead(String linePart)
	{
		head = new MLVVHeader();
		Matcher m = Pattern.compile("<b>(.+?)</b>\\s*(.*)").matcher(linePart);
		if (m.matches())
		{
			head.lemma = new Lemma(m.group(1));
			String gram = m.group(2);
			// Homonīma infekss, ja tāds ir.
			m = Pattern.compile("^<sup>\\s*<b>(.*?)</b>\\s*</sup>\\s*(.*)")
					.matcher(gram);
			if (m.matches())
			{
				if (homId != null) System.out.printf(
						"No šķirkļavārda \"%s\" homonīma indeksu mēģina piešķiert vairākkārt: vispirms %s, tad %s!",
						head.lemma, homId, m.group(1));
				homId = m.group(1);
			}
			head.gram = MLVVGram.extractFromString(linePart);
		} else
		{
			System.out
					.printf("Neizdodas izgūt pirmo šķirkļavārdu no šī rindas fragmenta:\n%s\n",
							linePart);
			head = null;
		}
	}

	protected void extractBody(String linePart)
	{
		linePart = linePart.trim();
		// Normatīvais komentārs
		Matcher m = Pattern.compile("(.*)<gray>(.*)</gray>").matcher(linePart);
		if (m.matches())
		{
			freeText = m.group(2).trim();
			linePart = m.group(1).trim();
		}

		// Cilme
		m = Pattern.compile("(.*) [cC]ilme: (.*)").matcher(linePart);
		if (m.matches())
		{
			origin = m.group(2).trim();
			linePart = m.group(1).trim();
		}

		// Atvasinājumi
		m = Pattern.compile("(.*)<square/>(.*)").matcher(linePart);
		if (m.matches())
		{
			extractDerivs(m.group(2));
			linePart = m.group(1).trim();
		}

		// Frazeoloģismi
		m = Pattern.compile("(.*?)<diamond/>(.*)").matcher(linePart);
		if (m.matches())
		{
			extractPhraseology(m.group(2).trim());
			linePart = m.group(1).trim();
		}

		// Stabilie vārdu savienojumi
		m = Pattern.compile("(.*?)<triangle/>(.*)").matcher(linePart);
		if (m.matches())
		{
			extractStable(m.group(2).trim());
			linePart = m.group(1).trim();
		}

		if (linePart.length() > 0)
		extractSenses(linePart);

	}

	/**
	 * TODO vai viena gramatika var attiekties uz vairākām lemmām?
	 */
	protected void extractDerivs(String linePart)
	{
		String[] derivTexts = linePart.split("\\s*(?=<b>)");
		if (derivTexts.length < 1) return;
		derivs = new LinkedList<>();
		for (String dt :derivTexts)
		{
			MLVVHeader h = MLVVHeader.extractSingularHeader(dt.trim());
			if (h != null) derivs.add(h);
		}
	}

	/**
	 * TODO gramatika pie frāzes
	 * TODO a. nozīme. b. nozīme.
	 */
	protected void extractPhraseology(String linePart)
	{
		String[] phrasesParts = linePart.split("<diamond/>");
		if (phrasesParts.length > 0)  phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			Phrase p = extractSinglePhrase(phraseText.trim());
			if (p != null) phrases.add(p);
		}
	}

	protected void extractStable(String linePart)
	{
		String[] phrasesParts = linePart.split("<triangle/>");
		if (phrasesParts.length > 0)  phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			Phrase p = extractSinglePhrase(phraseText.trim());
			if (p != null) phrases.add(p);
		}
	}
	protected void extractSenses(String linePart)
	{
		// TODO
	}

	protected Phrase extractSinglePhrase(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		Matcher m = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)").matcher(linePart);
		if (!m.matches())
		{
			System.out.printf("Neizdodas izanalizēt frāzi \"%s\"\n", linePart);
			Phrase res = new Phrase();
			res.text = linePart;
			return res;
		}
		Phrase res = new Phrase();
		String begin = m.group(1).trim();
		String end = m.group(2).trim();

		// Izanalizē frāzi.
		if (begin.contains("<i>"))
		{
			// Frāze pati ir kursīvā
			if (begin.startsWith("<i>"))
			{
				begin = begin.replace("<i>", "");
				begin = begin.replace("</i>", "");
				begin = begin.replaceAll("\\s\\s+", " ");
				res.text = begin;
			}
			// Kursīvs sākas kaut kur vidū - tātad kursīvā ir gramatika
			else
			{
				res.text = begin.substring(0, begin.indexOf("<i>")).trim();
				res.grammar = new Gram();
				res.grammar.freeText = begin.substring(begin.indexOf("<i>")).trim();
			}
		}
		// Frāzē nekas nav kursīvā - tātad tur ir tikai frāze bez problēmām.
		else res.text = begin;

		// Analizē skaidrojumus.
		// Te nekāda "lielā" gramatika nav paredzēta.
		if (end.startsWith("<i>") && end.contains(" b. "))
			System.out.printf("Frāzē \"%s\" gramatika ir pirms vairākām nozīmēm, nozīmes netiek sadalītas\n", linePart);

		String[] gloses = new String[] {end};
		// Ir vairākas nozīmes.
		if (end.startsWith("a."))
		{
			end = end.substring(2).trim();
			gloses = end.split("\\s[bcdefghijklmnop]\\.\\s");
		}
		res.subsenses = new LinkedList<>();
		for (String g : gloses)
		{
			if (end.startsWith("<i>"))
			{
				Sense newSense = new Sense();
				newSense.grammar = new Gram();
				if (end.contains("</i>"))
				{

					newSense.grammar.freeText = g.substring(0, g.indexOf("</i>") + 4);
					g = g.substring(g.indexOf("</i>") + 4);
				}
				else
				{
					System.out.printf("Frāzē \"%s\" skaidrojumā nevar atrast aizverošo i tagu\n", linePart);
					newSense.grammar.freeText = g;
					g = "";
				}
				newSense.gloss = new Gloss(g);
				res.subsenses.add(newSense);
			}
			else
			{
				res.subsenses.add(new Sense(g));
			}
		}
		return res;
	}

	/**
	 * Override, lai MLVV šķirkļos nedrukā galvenā header lemmu, jo tā jau ir
	 * iekļauta altLemmu sarakstā.
	 */
	@Override
	public Element toXML(Document doc)
	{
		Element entryN = doc.createElement("entry");

		if (homId != null)
			entryN.setAttribute("ID", homId);
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
				Node headerN = doc.createElement("header");
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
			Node sensesContN = doc.createElement("senses");
			for (Sense s : senses) s.toXML(sensesContN);
			parent.appendChild(sensesContN);
		}
		if (phrases != null && !phrases.isEmpty())
		{
			Node phrasesContN = doc.createElement("stablePhrases");
			for (Phrase p : phrases) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}
		if (phraseology != null && !phraseology.isEmpty())
		{
			Node phrasesContN = doc.createElement("phraseology");
			for (Phrase p : phraseology) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}
		if (derivs != null && !derivs.isEmpty())
		{
			Node derivContN = doc.createElement("derivatives");
			for (Header d : derivs) d.toXML(derivContN);
			parent.appendChild(derivContN);
		}
		if (reference != null && reference.length() > 0)
		{
			System.out
					.printf("Šķirklim \"%s\" norādītas atsauces, lai gan MLVV šo lauku nav paredzēts aizpildīt!\n",
							head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
			Node refN = doc.createElement("reference");
			refN.appendChild(doc.createTextNode(reference));
			parent.appendChild(refN);
		}
		if (origin != null && origin.length() > 0)
		{
			Node originN = doc.createElement("origin");
			originN.appendChild(doc.createTextNode(origin));
			parent.appendChild(originN);
		}
		if (freeText != null && freeText.length() > 0)
		{
			Node freeTextN = doc.createElement("normative");
			freeTextN.appendChild(doc.createTextNode(freeText));
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