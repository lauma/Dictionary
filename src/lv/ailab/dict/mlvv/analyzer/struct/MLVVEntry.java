package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
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
	 * Izanalizē rindu, un atgriež null, ja tajā nekā nav, vai MLVVEntry, ja no
	 * šīs rindas tādu var izgūt.
	 */
	public static MLVVEntry extractFromString(String line)
	{
		if (line == null || line.isEmpty()) return null;

		// Ja rinda satur tikai burtu, neizgūst neko.
		if (line.matches("<b>\\p{L}\\p{M}*</b>")) return null;

		MLVVEntry result = new MLVVEntry();

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
						"((?:<b>.*?</b>[,.;]?|<i>.*?</i>[,.;]?|<sup>.*?</sup>[,.;]?|\\[.*?\\][,.;]?|(?:[-,][^.<]*\\.?)|(?:[:,]\\s)?<u>.*?</u>\\.?)\\s*)(.*)");
						 //((?: cita lemma   | gram. kursīvā  | homonīma indekss   | [izruna]      | "galotne"        | formas ierobežojums  ) atstarpe) (pārējais)
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
				return null;
				//throw new IllegalArgumentException("Can't match input line with regexp!");
			}
		}
		head = head.trim();
		body = body.trim();
		if (head.isEmpty())
		{
			System.out
					.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
			return null;
		}
		else result.extractHead(head);
		if (body.isEmpty())
			System.out
					.println("Neizdodas izgūt šķirkļa ķermeni no šīs rindas:\n\t" + line);
		else result.extractBody(body);
		return result;
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
			head.lemma = new Lemma(m.group(1).trim());
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
				// Izmet ārā homonīma indeksu.
				linePart = "<b>" + head.lemma.text + "</b> " + m.group(2);
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

	protected void extractPhraseology(String linePart)
	{
		String[] phrasesParts = linePart.split("<diamond/>");
		if (phrasesParts.length > 0)  phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			Phrase p = extractSinglePhrase(phraseText.trim(), PhraseTypes.PHRASEOLOGICAL);
			if (p != null) phrases.add(p);
		}
	}

	protected void extractStable(String linePart)
	{
		linePart = linePart.trim();
		String[] phrasesParts = linePart.split("<triangle/>");
		if (phrasesParts.length > 0)  phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			Phrase p = extractSinglePhrase(phraseText.trim(), PhraseTypes.STABLE_UNIT);
			if (p != null) phrases.add(p);
		}
	}
	protected void extractSenses(String linePart)
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
			Sense s = extractSingleSense(sensesParts[senseNumber]);
			if (s != null) senses.add(s);
			if (numberSenses && (s.ordNumber == null || s.ordNumber.isEmpty()))
				s.ordNumber = Integer.toString(senseNumber + 1);
		}
	}
	protected Sense extractSingleSense(String linePart)
	{
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		Sense res = new Sense();

		// Nocērp un apstrādā beigas, lai nemaisās.
		// Nozīmes nianses
		if (linePart.contains("<lines/>"))
		{
			String[] subsensesParts = linePart.substring(
					linePart.indexOf("<lines/>") + "<lines/>".length())
					.trim().split("<lines/>");
			res.subsenses = new LinkedList<>();
			for (String subP : subsensesParts)
			{
				Sense ss = extractSingleSense(subP);
				if (ss != null) res.subsenses.add(ss);
			}
			linePart = linePart.substring(0, linePart.indexOf("<lines/>"));
		}

		// Nocērp no sākuma saprotamo.
		// Nozīmes gramatikas apstrāde
		if (linePart.startsWith("<i>"))
		{
			if (linePart.contains("</i>"))
			{
				Matcher gramMatch = Pattern.compile(
						"(<i>.*?</i>(?::\\s<u>.*?</u>!?(?:\\s\\[[^\\]]+\\])?(?:,\\s<u>.*?</u>!?(?:\\s\\[[^\\]]+\\])?)*[.;,]*(?:\\s*<i>.*?</i>[.,;]?)*)*)\\s*(.*)")
						// (gramatika kursīvā (: pasvītrota forma( [izruna])?(, atkārtota forma( [izruna])?)* pieturz.? (vēl gramatika)*)*) atstarpe (pārējais)
						.matcher(linePart);
				if (gramMatch.matches())
				{
					res.grammar = MLVVGram.extractFromString(gramMatch.group(1));
					linePart = gramMatch.group(2);
				}
				else
					System.out.printf("No fragmenta \"%s\" neizdodas atdalīt gramatiku (šķirklī %s)", linePart, head.lemma.text);
			}
			else
			{
				System.out.printf("Nesapārots i tags nozīmē \"%s\"", linePart);
				res.grammar = new MLVVGram(linePart.substring(3));
				linePart = "";
			}
		}

		// Tagad vajadzētu sakot definīcijai.
		// Ja tālāk būs piemēri
		if (linePart.contains(". <i>"))
		{
			res.gloss = new Gloss(linePart.substring(0, linePart.indexOf(". <i>") + 1).trim());
			linePart = linePart.substring(linePart.indexOf(". <i>") + 1).trim();
		}
		// Ja piemēru nav.
		else
		{
			res.gloss = new Gloss(linePart.trim());
			linePart = "";
		}

		// Piemēru analīze.
		LinkedList<Phrase> samples = extractSamples(linePart);
		if (samples != null && samples.size() > 0)
			res.examples = samples;

		return res;
	}

	protected LinkedList<Phrase> extractSamples(String linePart)
	{
		LinkedList<Phrase> res = new LinkedList<>();
		if (linePart != null) linePart = linePart.trim();
		if (linePart == null || linePart.length() < 1) return res;

		int beginTaxonOrQuot = getAnyCircleIndex(linePart);
		String lineEndPart = null;
		if (beginTaxonOrQuot > -1)
		{
			lineEndPart = linePart.substring(beginTaxonOrQuot);
			linePart = linePart.substring(0, beginTaxonOrQuot);
		}

		// Apstrādā "pamata" piemērus.
		if (linePart.length() > 0)
		{
			// Ja pēdējais punkts ir nejauši palicis ārā no kursīva, to iebāž
			// atpakaļ iekšā.
			if (linePart.endsWith("</i>."))
				linePart = linePart
						.substring(0, linePart.length() - "</i>.".length())
						+ ".</i>";
			// Piemēros iekļautās atkursīvotās iekavas uzskata par parastām iekavām.
			// Šim vajadzētu būt izdarītam jau normalizācijas solī.
			//linePart = linePart.replace("</i> (<i>", " (");
			//linePart = linePart.replace("</i>(<i>", "(");
			//linePart = linePart.replace("</i>) <i>", ") ");
			//linePart = linePart.replace("</i>)<i>", ")");

			// Vispirms jāmēģina atdalīt bezskaidrojumu piemērus.
			if (linePart.startsWith("<i>"))
				linePart = linePart.substring(3).trim();
			Pattern splitter = Pattern
					.compile("((?:.(?!</i>)|\\.</i>:\\s<i>)*?[.!?])(\\s\\p{Lu}.*|\\s*</i>)");
			Matcher m = splitter.matcher(linePart);
			while (m.matches())
			{
				Phrase sample = new Phrase();
				sample.type = PhraseTypes.SAMPLE;
				String text = m.group(1).trim();
				if (text.contains(".: "))
				{
					sample.grammar = new MLVVGram(text
							.substring(0, text.indexOf(".: ") + 1));
					text = text.substring(text.indexOf(".: ") + 2).trim();
				} else if (text.contains(".</i>: <i>"))
				{
					sample.grammar = new MLVVGram(text
							.substring(0, text.indexOf(".</i>: <i>") + 1));
					text = text
							.substring(text.indexOf(".</i>: <i>") + ".</i>: <i>"
									.length()).trim();
				}
				sample.text = text;
				res.add(sample);
				linePart = m.group(2);
				m = splitter.matcher(linePart);
			}
			// Tālāk sadala lielos piemērus ar skaidrojumu
			if (linePart.length() > 0 && !linePart.matches("\\s*</i>\\s*"))
			{
				if (!linePart.startsWith("<i>")) linePart = "<i>" + linePart;
				// Te ir maģija, lai nesadalītu "a. skaidrojums. b. <i>sar.</i> skaidrojums."
				// un "<i>frāze</i> - <i>gramatika</i> skaidrojums." divos.
				String[] parts = linePart
						.split("(?<!\\s[a-p]\\.\\s|[\\-\u2014\u2013]\\s?|\\.</i>:\\s)(?=<i>)");//
				for (String part : parts)
				{
					Phrase sample = extractSinglePhrase(part, PhraseTypes.SAMPLE);
					if (sample != null) res.add(sample);
				}
			}
		}

		// Apstrādā to, kas ir aiz tukšā vai pilnā aplīša.
		if (lineEndPart != null && lineEndPart.startsWith("<bullet/>"))
		{
			Pattern taxonPat = Pattern.compile(
					"<bullet/>\\s*(<i>.*?</i>\\s*\\[.*\\](?:\\s+[-\u2013](?:(?!<(/?i|bullet/|circle/)>).)*|\\.)?)(.*)");
			Matcher m = taxonPat.matcher(lineEndPart);
			if (m.matches())
			{
				res.add(extractTaxon(m.group(1)));
				res.addAll(extractSamples(m.group(2)));
			}
			else
			{
				System.out.printf("Taksons \"%s\" neatbilst atdalīšanas šablonam\n", lineEndPart);
				res.add(extractTaxon(lineEndPart.substring("<bullet/>".length())));
			}
		}
		else if (lineEndPart != null && lineEndPart.startsWith("<circle/>"))
		{
			Pattern quotePat = Pattern.compile("<circle/>\\s*(<i>.*?</i>[.?!]*\\s*\\(.*\\)\\.?)(.*)");
			Matcher m = quotePat.matcher(lineEndPart);
			if (m.matches())
			{
				res.add(extractQuote(m.group(1)));
				res.addAll(extractSamples(m.group(2)));
			}
			else
			{
				System.out.printf("Citāts \"%s\" neatbilst atdalīšanas šablonam\n", lineEndPart);
				res.add(extractQuote(lineEndPart.substring("<circle/>".length())));
			}
		}

		return res;
	}


	// TODO - atstāt kvadrātiekavas vai mest ārā
	protected Phrase extractTaxon(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;

		Phrase res = new Phrase();
		res.type = PhraseTypes.TAXON;
		Matcher m = Pattern.compile("<i>(.*?)</i>\\s*\\[(.*)\\](.*)").matcher(linePart);
		if (m.matches())
		{
			res.text = m.group(1).trim();
			String gloss = m.group(2).trim();
			String glossEnd = m.group(3).trim();
			if (glossEnd.equals(".")) gloss = gloss + glossEnd;
			else gloss = gloss + " " + glossEnd;
			res.subsenses = new LinkedList<>();
			res.subsenses.add(new Sense(gloss.trim()));
		}
		else
		{
			res.text = linePart.trim();
			if (res.text.matches("<i>((?!</i>).)*</i>"))
				res.text = res.text.substring(3, res.text.length()-4);
			System.out.printf("Taksons \"%s\" neatbilst apstrādes šablonam\n", res.text);
		}
		return res;

	}

	protected Phrase extractQuote(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;

		Phrase res = new Phrase();
		res.type = PhraseTypes.QUOTE;
		// Izmest tos kursīvus, kas iezīmē papildinājumus citātā (pietiks, ka
		// tos iezīmē kvadātiekavas).
		// Šo izdara jau pie normalizācijas.
		//linePart = linePart.replace("</i> [", " [");
		//linePart = linePart.replace("</i>[", "[");
		//linePart = linePart.replace("] <i>", "] ");
		//linePart = linePart.replace("]<i>", "]");
		Matcher m = Pattern.compile("<i>(.*?)</i>([.?!]*)\\s*\\((.*)\\)\\.?").matcher(linePart);
		if (m.matches())
		{
			res.text = (m.group(1) + m.group(2)).trim();
			res.source = m.group(3).trim();
		}
		else
		{
			res.text = linePart;
			System.out.printf("Citāts \"%s\" neatbilst apstrādes šablonam\n", linePart);
		}
		return res;
	}

	protected Phrase extractSinglePhrase(String linePart, String phraseType)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		Matcher m = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)").matcher(linePart);
		if (!m.matches())
		{
			System.out.printf("Neizdodas izanalizēt frāzi \"%s\" ar tipu \"%s\"", linePart, phraseType);
			if (head != null && head.lemma != null)
				System.out.printf(" (lemma \"%s\")", head.lemma.text);
			System.out.println();
			Phrase res = new Phrase();
			res.text = linePart;
			return res;
		}
		Phrase res = new Phrase();
		res.type = phraseType;
		String begin = m.group(1).trim();
		String end = m.group(2).trim();

		// Izanalizē frāzi.
		if (begin.contains("<i>"))
		{
			// Frāze pati ir kursīvā
			if (begin.startsWith("<i>"))
			{
				// ir ar kolu atdalītā gramatika
				if (begin.contains(".: "))
				{
					res.grammar = new MLVVGram(begin.substring(0, begin.indexOf(".: ") + 1));
					begin = begin.substring(begin.indexOf(".: ") + 2).trim();
				} else if (begin.contains(".</i>: <i>"))
				{
					res.grammar = new MLVVGram(begin.substring(0, begin.indexOf(".</i>: <i>") + 1));
					begin = begin.substring(begin.indexOf(".</i>: <i>") + ".</i>: <i>".length()).trim();
				}

				begin = begin.replace("<i>", "");
				begin = begin.replace("</i>", "");
				begin = begin.replaceAll("\\s\\s+", " ");
				res.text = begin;
			}
			// Kursīvs sākas kaut kur vidū - tātad kursīvā ir gramatika
			else
			{
				res.text = begin.substring(0, begin.indexOf("<i>")).trim();
				res.grammar = new MLVVGram(begin.substring(begin.indexOf("<i>")).trim());
			}
		}
		// Frāzē nekas nav kursīvā - tātad tur ir tikai frāze bez problēmām.
		else res.text = begin;

		// Analizē skaidrojumus.
		// Ja te ir lielā gramatika un vairākas nozīmes, tad gramatiku piekārto
		// pie frāzes, nevis pirmās nozīmes.
		if (end.startsWith("<i>") && end.contains(" b. "))
		{
			if (end.contains("</i>"))
			{
				String gram = end.substring(0, end.indexOf("</i>") + "</i>".length()).trim();
				if (res.grammar == null)
				{
					res.grammar = new MLVVGram(gram);
					end = end.substring(end.indexOf("</i>") + "</i>".length()).trim();
				}
				else
					System.out.printf("Frāzē \"%s\" ir divas vispārīgās gramatikas, tāpēc nozīmes netiek sadalītas\n", linePart);
			}
		}

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
				String gramText = null;
				if (end.contains("</i>"))
				{

					gramText = g.substring(0, g.indexOf("</i>") + 4);
					g = g.substring(g.indexOf("</i>") + 4);
				}
				else
				{
					System.out.printf("Frāzē \"%s\" skaidrojumā nevar atrast aizverošo i tagu\n", linePart);
					gramText = g;
					g = "";
				}
				newSense.grammar = new MLVVGram(gramText);

				newSense.gloss = new Gloss(g);
				res.subsenses.add(newSense);
			}
			else
			{
				res.subsenses.add(new Sense(g));
			}
		}
		// Ja frāzei ir vairākas nozīmes, tās sanumurē.
		if (res.subsenses != null && res.subsenses.size() > 1)
			for (int senseNumber = 0; senseNumber < res.subsenses.size(); senseNumber++)
		{
			Sense sense = res.subsenses.get(senseNumber);
			if (sense.ordNumber != null)
				System.out.printf("Frāzē \"%s\" nozīme ar numuru \"%s\" tiek pārnumurēta par \"%s\"\n",
						res.text, sense.ordNumber, senseNumber +1);
			sense.ordNumber = Integer.toString(senseNumber + 1);
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
		if (derivs != null && !derivs.isEmpty())
		{
			Node derivContN = doc.createElement("Derivatives");
			for (Header d : derivs) d.toXML(derivContN);
			parent.appendChild(derivContN);
		}
		if (reference != null && reference.length() > 0)
		{
			System.out
					.printf("Šķirklim \"%s\" norādītas atsauces, lai gan MLVV šo lauku nav paredzēts aizpildīt!\n",
							head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
			Node refN = doc.createElement("Reference");
			refN.appendChild(doc.createTextNode(reference));
			parent.appendChild(refN);
		}
		if (origin != null && origin.length() > 0)
		{
			Node originN = doc.createElement("Origin");
			originN.appendChild(doc.createTextNode(origin));
			parent.appendChild(originN);
		}
		if (freeText != null && freeText.length() > 0)
		{
			Node freeTextN = doc.createElement("Normative");
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

	protected static int getAnyCircleIndex(String linePart)
	{
		int bulletIndex = linePart.indexOf("<bullet/>");
		int circleIndex = linePart.indexOf("<circle/>");
		int res = -1;
		if (bulletIndex > -1 && circleIndex > -1)
			res = Math.min(bulletIndex, circleIndex);
		else if (bulletIndex > -1)
			res = bulletIndex;
		else if (circleIndex > -1)
			res = circleIndex;
		return res;
	}
}