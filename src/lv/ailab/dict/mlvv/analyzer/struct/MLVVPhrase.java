package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.mlvv.analyzer.stringutils.Replacers;
import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pašlaik te nav būtiski paplašināta Phrase funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class MLVVPhrase extends Phrase
{
	// TODO: šis ir uzblīdis, vajag sacirst gabaliņos.
	/**
	 * Metode, kas izgūst dotā tipa frāzi no dotās simbolu virknes.
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo frāzi un
	 *                      neko citu
	 * @param phraseType	frāzes tips (PhraseTypes)
	 * @param lemma			šķirkļavārds šķirklim, kurā šī frāze atrodas (kļūdu
	 *                      paziņojumiem)
	 * @return	izgūtā frāze vai null
	 */
	public static MLVVPhrase extractSampleOrPhrase(String linePart, String phraseType, String lemma)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		Matcher dashMatcher = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)").matcher(linePart);
		Matcher colonMatcher = Pattern.compile("(.*?</i>):\\s*(<i>.*)").matcher(linePart);
		Matcher simpleMatcher = Pattern.compile("<i>[^:]*</i>").matcher(linePart);
		MLVVPhrase res = new MLVVPhrase();
		res.type = phraseType;
		res.text = new LinkedList<>();

		// Izanalizē frāzi ar skaidrojumu
		if (dashMatcher.matches())
		{
			String begin = dashMatcher.group(1).trim();
			String end = dashMatcher.group(2).trim();

			// Ja vajag, frāzi sadala.
			String[] beginParts = begin.split("(?<=</i>), arī\\s*<i>");
			String gramText = "";
			for (String part : beginParts)
			{
				// Frāze pati ir kursīvā
				if (part.startsWith("<i>"))
				{
					// ir ar kolu atdalītā gramatika
					if (part.contains(".: "))
					{
						if (gramText.length() > 0) gramText = gramText + "; ";
						gramText = (gramText + part.substring(0, part.indexOf(".: ") + 1)).trim();
						part = phraseType.substring(part.indexOf(".: ") + 2).trim();
					} else if (begin.contains(".</i>: <i>"))
					{
						if (gramText.length() > 0) gramText = gramText + "; ";
						gramText = (gramText + part.substring(0, part.indexOf(".</i>: <i>") + 1))
								.trim();
						part = part.substring(part.indexOf(".</i>: <i>") + ".</i>: <i>".length())
								.trim();
					}

					res.text.add(Replacers.removeCursive(part));
				}
				// Kursīvs sākas kaut kur vidū - tātad kursīvā ir gramatika
				else if (part.contains("<i>"))
				{
					res.text.add(part.substring(0, part.indexOf("<i>")).trim());
					if (gramText.length() > 0) gramText = gramText + "; ";
					gramText = (gramText + part.substring(part.indexOf("<i>"))).trim();
				}
				// Frāzē nekas nav kursīvā - tātad tur ir tikai frāze bez problēmām.
				else res.text.add(part);
			}
			if (gramText.length() > 0) res.grammar = new MLVVGram(gramText);

			// Analizē skaidrojumus.
			// Ja te ir lielā gramatika un vairākas nozīmes, tad gramatiku piekārto
			// pie frāzes, nevis pirmās nozīmes.
			if (end.startsWith("</i>")) end = end.substring("</i>".length()).trim(); // ja defise nejauši ir kursīvā.
				//else if (end.startsWith("<i>") && end.contains(" b. "))
			else
			{
				Matcher endMatcher = Pattern
						.compile("(.*?)(?:</i> | </i>)(a\\.\\s.*?\\sb\\.\\s.*)")
						.matcher(end);
				if (endMatcher.matches())
				{
					String gram = endMatcher.group(1).trim();
					if (res.grammar == null)
					{
						if (gram.startsWith("<i>")) gram = gram.substring(3);
						res.grammar = new MLVVGram(gram);
						end = endMatcher.group(2).trim();
					} else
						System.out.printf(
								"Frāzē \"%s\" ir divas vispārīgās gramatikas, tāpēc nozīmes netiek sadalītas\n",
								linePart);
				}
			}

			String[] subsenses = new String[]{end};
			// Ir vairākas nozīmes.
			if (end.startsWith("a."))
			{
				subsenses = end.split("\\s(?=[bcdefghijklmnop]\\.\\s)");
				// Ja skaidrojums ir formā "a. skaidrojums <i>piemēri b.</i> b. skaidrojums...",
				// tad var sanākt nepareizs dalījums dalot tā pa prasto.
				if (end.contains("<i>"))
				{
					LinkedList<String> betterGloses = new LinkedList<>();
					betterGloses.addLast(subsenses[0]);
					for (int i = 1; i < subsenses.length; i++)
					{
						String prev = betterGloses.getLast();
						if (prev.indexOf("<i>") > prev.indexOf("</i>"))
							betterGloses.addLast(
									betterGloses.removeLast() + " " +subsenses[i]);
						else betterGloses.addLast(subsenses[i]);
					}
					subsenses = betterGloses.toArray(new String[betterGloses.size()]);
				}
				subsenses = Arrays.stream(subsenses).map(s -> s.substring(2).trim())
						.toArray(String[]::new);
			}
			res.subsenses = new LinkedList<>();
			for (String subsense : subsenses)
			{
				if (subsense.contains("</i>") && !subsense.contains("<i>"))
					subsense = "<i>" + subsense;
				else if (subsense.contains("<i>") && !subsense.contains("</i>"))
					subsense = subsense + "</i>";
				res.subsenses.add(MLVVSense.extract(subsense, lemma));
				/*if (end.startsWith("<i>"))
				{
					Sense newSense = new Sense();
					String senseGramText = null;
					if (end.contains("</i>"))
					{
						senseGramText = subsense.substring(0, subsense.indexOf("</i>") + 4);
						subsense = subsense.substring(subsense.indexOf("</i>") + 4);
					} else
					{
						System.out.printf(
								"Frāzē \"%s\" skaidrojumā nevar atrast aizverošo i tagu\n",
								linePart);
						senseGramText = subsense;
						subsense = "";
					}
					newSense.grammar = new MLVVGram(senseGramText);
					newSense.gloss = MLVVGloss.extract(subsense);
					res.subsenses.add(newSense);
				} else
					res.subsenses.add(new Sense(MLVVGloss.extract(subsense)));//*/
			}
			// Ja frāzei ir vairākas nozīmes, tās sanumurē.
			if (res.subsenses != null && res.subsenses.size() > 1)
				for (int senseNumber = 0; senseNumber < res.subsenses.size(); senseNumber++)
				{
					Sense sense = res.subsenses.get(senseNumber);
					if (sense.ordNumber != null)
						System.out.printf(
								"Frāzē \"%s\" nozīme ar numuru \"%s\" tiek pārnumurēta par \"%s\"\n",
								res.text, sense.ordNumber, senseNumber + 1);
					sense.ordNumber = Integer.toString(senseNumber + 1);
				}
		}
		// Izanalizē frāzi ar gramatiku, kas atdalīta ar kolu, bet bez
		// skaidrojuma.
		else if (colonMatcher.matches())
		{
			String begin = colonMatcher.group(1).trim();
			if (begin.length() > 0) res.grammar = new MLVVGram(begin);
			String end = colonMatcher.group(2).trim();
			res.text.add(Replacers.removeCursive(end));
		}
		// Frāze bez gramatikas un bez skaidrojuma.
		else if (simpleMatcher.matches())
			res.text.add(Replacers.removeCursive(linePart));
		// Nu nesanāca!
		else
		{
			System.out.printf("Neizdodas izanalizēt frāzi \"%s\" ar tipu \"%s\"",
					linePart, phraseType);
			if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
			System.out.println();
			res.text.add(linePart);
		}
		return res;

	}

	/**
	 * Metode, kas no dotā šķirkļa fragmenta izgūst taxona tipa frāzi.
	 * @param linePart	šķirkļa teksta daļa, kas apraksta tieši šo frāzi un neko
	 *                  citu
	 * @return izgūtā frāze vai null
	 */
	public static MLVVPhrase extractTaxon(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;

		MLVVPhrase res = new MLVVPhrase();
		res.type = PhraseTypes.TAXON;
		res.text = new LinkedList<>();
		Matcher m = Pattern.compile("<i>(.*?)</i>\\s*\\[(.*)\\](.*)").matcher(linePart);
		if (m.matches())
		{
			res.text.add(m.group(1).trim());
			String gloss = m.group(2).trim();
			String glossEnd = m.group(3).trim();
			if (glossEnd.equals(".")) gloss = gloss + glossEnd;
			else if (glossEnd.matches("\\s*[-\u2013\u2014].+"))gloss = gloss + " " + glossEnd;
				// TODO: iespējams, ka te vajag brīdinājumu par trūkstošu domuzīmi.
			else if (glossEnd.trim().length() > 0) gloss = gloss + " \u2013 " + glossEnd.trim();
			res.subsenses = new LinkedList<>();
			res.subsenses.add(new Sense(MLVVGloss.extract(gloss.trim())));
		}
		else
		{
			String resText = linePart.trim();
			if (resText.matches("<i>((?!</i>).)*</i>"))
				resText = resText.substring(3, resText.length()-4);
			res.text.add(resText);
			System.out.printf("Taksons \"%s\" neatbilst apstrādes šablonam\n", resText);
		}
		return res;

	}

	/**
	 * Metode, kas no dotā šķirkļa fragmenta izgūst citāta tipa frāzi.
	 * @param linePart	šķirkļa teksta daļa, kas apraksta tieši šo frāzi un neko
	 *                  citu
	 * @return izgūtā frāze vai null
	 */
	public static MLVVPhrase extractQuote(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.replaceAll("\\s\\s+", " ").trim();
		if (linePart.length() < 1) return null;

		MLVVPhrase res = new MLVVPhrase();
		res.type = PhraseTypes.QUOTE;
		res.text = new LinkedList<>();

		Matcher m = Pattern.compile(
				"((?:(?:<i>)?\\s*Pārn\\.</i>:\\s*)?)((?:(?:\\.{2,3}|\")\\s*)?)<i>(.*?)</i>([.?!\"]*)\\s*\\((.*)\\)\\.?")
				// Neobligāts Pārn. kursīvā : neobligātas pieturzīmes <i> citāta teksts </i> neobligātas pieturz. (autors)
				.matcher(linePart);
		if (m.matches())
		{
			res.text.add((m.group(2) + m.group(3) + m.group(4)).replaceAll("\\s\\s+", " ").trim());
			res.source = m.group(5).trim();
			String gramString = m.group(1).trim().replaceAll("</?i>", "");
			if (gramString.endsWith(":")) gramString = gramString.substring(0, gramString.length()-1);
			res.grammar = new MLVVGram(gramString);
		}
		else
		{
			res.text.add(linePart);
			System.out.printf("Citāts \"%s\" neatbilst apstrādes šablonam\n", linePart);
		}
		return res;
	}

	// TODO: šis ir uzblīdis, vajag sacirst gabaliņos.
	/**
	 * No simbolu virknes, kas satur dažādas frāzes, izgūst frāžu masīvu.
	 * @param linePart	šķirkļa daļa, kas jāpārtaisa frāzēs
	 * @param lemma		šķirkļavārds šķirklim, kurā šī frāze atrodas (kļūdu
	 *                  paziņojumiem)
	 * @return	izgūto frāžu masīvs (tukšs, ja nekā nav)
	 */
	public static LinkedList<MLVVPhrase> extractPhrases(String linePart, String lemma)
	{
		LinkedList<MLVVPhrase> res = new LinkedList<>();
		if (linePart != null) linePart = linePart.trim();
		if (linePart == null || linePart.length() < 1) return res;

		int beginTaxonOrQuot = Finders.getAnyCircleIndex(linePart);
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

			// Vispirms jāmēģina atdalīt bezskaidrojumu piemērus.
			if (linePart.startsWith("<i>"))
				linePart = linePart.substring(3).trim();
			Pattern splitter = Pattern
					.compile("((?:.(?!</i>)|\\.</i>:\\s<i>)*?[.!?])(\\s\\p{Lu}.*|\\s*</i>|\\s*$)");
			Matcher m = splitter.matcher(linePart);
			while (m.matches())
			{
				MLVVPhrase sample = new MLVVPhrase();
				sample.type = PhraseTypes.SAMPLE;
				sample.text = new LinkedList<>();
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
				sample.text.add(text);
				res.add(sample);
				linePart = m.group(2);
				m = splitter.matcher(linePart);
			}
			// Tālāk sadala lielos piemērus ar skaidrojumu
			if (linePart.length() > 0 && !linePart.matches("\\s*</i>\\s*"))
			{
				if (!linePart.startsWith("<i>")) linePart = "<i>" + linePart;
				// Te ir maģija, lai nesadalītu "a. skaidrojums. <i>piem.</i> b. <i>sar.</i> skaidrojums.",
				// "<i>frāze</i> - <i>gramatika</i> skaidrojums." un
				// "<i>frāze</i>, arī <i>frāze</i> - ..." divos.
				String[] initialParts = linePart.split("(?=<i>)");

				// Vispirms apvieno lieki sadalīto
				LinkedList<String> concatParts = new LinkedList<>();
				concatParts.addLast(initialParts[0]);
				for (int i = 1; i < initialParts.length; i++)
				{
					// Un sākotnējais sadalījums bieži ir par daudz gabalos.
					if (concatParts.getLast().matches(".*(\\s[a-p]\\.\\s|[\\-\u2014\u2013]\\s?|\\.</i>:\\s|[,(]\\s?(arī|saīsināti:)\\s?)")
							|| concatParts.getLast().matches(".*\\s[a-o]\\.\\s((?<!</?i>).)*")
							&& initialParts[i].matches("\\s*<i>.*"))
							//&& subParts[i].matches("\\s*<i>((?<!</i>).*)(</i>\\s|\\s</i>)[b-p]\\.\\s.*"))
						concatParts.addLast(concatParts.removeLast() + initialParts[i]);
					else concatParts.addLast(initialParts[i]);
				}

				// Tad sadala to, kas nesadalījās - tekstuāls piemērs var būt
				// saplūdis kopā ar nākamo piemēru, aiz kā seko domuzīme, ja
				// viss ir kursīvā.
				Pattern resplitPat1 = Pattern.compile(
						"(.*<i>(?:(?<!</i>).)*\\.\\s)(\\(?\\p{Lu}[^\\p{Lu}]*[\\-\u2014\u2013]\\s?.*)");
				LinkedList<String> finalParts = new LinkedList<>();
				for (String concatPart : concatParts)
				{
					Matcher resplitter = resplitPat1.matcher(concatPart);
					if (resplitter.matches())
					{
						finalParts.add(resplitter.group(1));
						finalParts.add(resplitter.group(2));
					}
					else finalParts.add(concatPart);
				}

				// Apstrādā iegūtās daļas.
				for (String part : finalParts)
				{
					MLVVPhrase sample = extractSampleOrPhrase(
							part, PhraseTypes.SAMPLE, lemma);
					if (sample != null) res.add(sample);
				}
			}
		}

		// Apstrādā to, kas ir aiz tukšā vai pilnā aplīša.
		// Pirms <circle/> var būt "Pārn.:", bet pirms <bullet/> tādam nevajadzētu būt.
		if (lineEndPart != null && lineEndPart.startsWith("<bullet/>"))
		{
			Pattern taxonPat = Pattern.compile(
					"<bullet/>\\s*(<i>.*?</i>\\s*\\[.*\\](?:\\s+[-\u2013\u2014]?(?:(?!(?:<(?:/?i|bullet/)>|(?:(?:<i>\\s*)?Pārn\\.</i>:\\s*)?<circle/>)).)*|\\.)?)(.*)");
			// <bullet/> suga kursīvā [latīniskais nos] - skaidrojums līdz nākamajam "bullet" vai "i", vai "circle" un pārējais
			Matcher m = taxonPat.matcher(lineEndPart);
			if (m.matches())
			{
				res.add(MLVVPhrase.extractTaxon(m.group(1)));
				res.addAll(extractPhrases(m.group(2), lemma));
			}
			else
			{
				System.out.printf("Taksons \"%s\" neatbilst atdalīšanas šablonam\n", lineEndPart);
				res.add(MLVVPhrase.extractTaxon(lineEndPart.substring("<bullet/>".length())));
			}
		}
		else if (lineEndPart != null && lineEndPart.matches("((<i>\\s*)?Pārn\\.</i>:\\s*)?<circle/>.*"))
		{
			Pattern quotePat = Pattern.compile(
					"((?:(?:<i>\\s*)?Pārn\\.</i>:\\s*)?)<circle/>\\s*((?:(?:\\.{2,3}|\")\\s*)?<i>.*?</i>[.?!\"]*\\s*\\(.*?\\)\\.?)(.*)");
			// Neobligāts Pārn. kursīvā : <circle/> neobligātas pieturzīmes <i> citāta teksts </i> neobligātas pieturz. (autors) pārējais
			Matcher m = quotePat.matcher(lineEndPart);
			if (m.matches())
			{
				res.add(MLVVPhrase.extractQuote(m.group(1) + m.group(2)));
				res.addAll(extractPhrases(m.group(3), lemma));
			}
			else
			{
				System.out.printf("Citāts \"%s\" neatbilst atdalīšanas šablonam\n", lineEndPart);
				res.add(MLVVPhrase.extractQuote(lineEndPart.replaceFirst("<circle/>", "")));
			}
		}
		return res;
	}
}
