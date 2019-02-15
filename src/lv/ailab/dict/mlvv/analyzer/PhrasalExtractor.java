package lv.ailab.dict.mlvv.analyzer;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.mlvv.analyzer.struct.MLVVGram;
import lv.ailab.dict.mlvv.analyzer.struct.MLVVPhrase;
import lv.ailab.dict.struct.Phrase;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhrasalExtractor
{
	/**
	 * No simbolu virknes, kas satur dažādas frāzes, izgūst frāžu masīvu.
	 * @param linePart	šķirkļa daļa, kas jāpārtaisa frāzēs
	 * @param lemma		šķirkļavārds šķirklim, kurā šī frāze atrodas (kļūdu
	 *                  paziņojumiem)
	 * @return	izgūto frāžu masīvs (tukšs, ja nekā nav)
	 */
	public static LinkedList<MLVVPhrase> parseAllPhrases(String linePart, String lemma)
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
		if (linePart != null && !linePart.isEmpty())
			res.addAll(parseAllSamplesAndPhrasals(linePart, lemma));
		// Apstrādā to, kas ir aiz tukšā vai pilnā aplīša.
		if (lineEndPart != null && !lineEndPart.isEmpty())
			res.addAll(MLVVPhrase.parseAllTaxonsAndQuotes(lineEndPart, lemma));
		return res;
	}

	// TODO: šis ir uzblīdis, vajag sacirst gabaliņos.
	/**
	 * Izanalizē simbolu virkni, kas satur parastās frāzes - tās, kas dotas ar
	 * vai bez skaidrojumiem, bet ne taksonus vai citātus.
	 * @param linePart	apstrādājamā rindiņas daļa.
	 * @param lemma		individuālo frāžu apstrādē reizēm vajag zināt lemmu.
	 * @return izgūto frāžu saraksts.
	 */
	protected static LinkedList<MLVVPhrase> parseAllSamplesAndPhrasals(
			String linePart, String lemma)
	{
		if (linePart == null || linePart.trim().isEmpty()) return null;
		LinkedList<MLVVPhrase> res = new LinkedList<>();

		// Ja pēdējais punkts ir nejauši palicis ārā no kursīva, to iebāž
		// atpakaļ iekšā.
		if (linePart.endsWith("</i>."))
			linePart = linePart.substring(0, linePart.length() - "</i>.".length())
					+ ".</i>";

		// Vispirms jāmēģina atdalīt bezskaidrojumu piemērus.
		if (linePart.startsWith("<i>"))
			linePart = linePart.substring(3).trim();
		Pattern splitter = Pattern.compile(
				"((?:(?!</i>).|\\.</i>:\\s<i>|</i>:\\s<i>\")*?[.!?]\"?)(\\s\\p{Lu}.*|\\s*</i>|\\s*$)");
		Matcher m = splitter.matcher(linePart);
		while (m.matches())
		{
			res.add(MLVVPhrase.parseNoGlossSample(m.group(1).trim()));
			linePart = m.group(2);
			m = splitter.matcher(linePart);
		}
		// Tālāk sadala lielos piemērus ar skaidrojumu
		if (linePart.length() > 0 && !linePart.matches("\\s*</i>\\s*"))
		{
			LinkedList<String> finalParts = splitExplainedPhrasals(linePart);
			// Apstrādā iegūtās daļas.
			for (String part : finalParts)
			{
				if (part == null) continue;
				part = part.trim();
				if (part.length() < 1) continue;

				Matcher dashMatcher = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)").matcher(part);
				Matcher colonMatcher = Pattern.compile("(.*?</i>):\\s*(<i>.*)").matcher(part);
				Matcher simpleMatcher = Pattern.compile("<i>[^:]+(:\\s+[\"\u201e\u201d][^:]+)?</i>").matcher(part);

				MLVVPhrase resPhrase = new MLVVPhrase();
				resPhrase.type = Phrase.Type.SAMPLE;
				resPhrase.text = new LinkedList<>();
				// Izanalizē frāzi ar skaidrojumu
				if (dashMatcher.matches())
				{
					String begin = dashMatcher.group(1).trim();
					String end = dashMatcher.group(2).trim();
					// Izanalizē frāzes tekstu un pie tā piekārtoto gramatiku.
					resPhrase.extractGramAndText(begin);
					// Analizē skaidrojumus.
					resPhrase.parseGramAndGloss(end, lemma, part);
				}

				// Izanalizē frāzi ar gramatiku, kas atdalīta ar kolu, bet bez
				// skaidrojuma.
				else if (colonMatcher.matches())
				{
					String begin = colonMatcher.group(1).trim();
					if (begin.length() > 0) resPhrase.grammar = new MLVVGram(begin);
					String end = colonMatcher.group(2).trim();
					resPhrase.text.add(Editors.removeCursive(end));
				}

				// Frāze bez gramatikas un bez skaidrojuma.
				else if (simpleMatcher.matches())
					resPhrase.text.add(Editors.removeCursive(part));

					// Nu nesanāca!
				else
				{
					System.out.printf("Neizdodas izanalizēt frāzi \"%s\" ar tipu \"%s\"",
							part, Phrase.Type.SAMPLE);
					if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
					System.out.println();
					resPhrase.text.add(part);
				}
				resPhrase.variantCleanup();
				if (resPhrase.text.isEmpty())
				{
					System.out.printf("Frāzes skaidrojumam \"%s\" nav neviena frāzes teksta",
							part);
					if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
					System.out.println();
				}
				res.add(resPhrase);
			}
		}

		return res;
	}

	// TODO: šis ir uzblīdis, vajag sacirst gabaliņos.
	/**
	 * Simbolu virkni, kas satur vairākas frāzes ar skaidrojumiem (be ne
	 * taksonus vai citātus) saskalda pa atsevišķi analizējamām frāzēm.
	 * @param linePart	apstrādājamā rindiņas daļa.
	 * @return izgūto frāžu saraksts.
	 */
	protected static LinkedList<String> splitExplainedPhrasals(String linePart)
	{
		if (!linePart.startsWith("<i>")) linePart = "<i>" + linePart;
		// Te ir maģija, lai nesadalītu "a. skaidrojums. <i>piem.</i> b. <i>sar.</i> skaidrojums.",
		// "<i>frāze</i> - <i>gramatika</i> skaidrojums." un
		// "<i>frāze</i>, arī <i>frāze</i> - ..." divos.
		String[] initialParts = linePart.split("(?=<i>)");

		// Vispirms apvieno lieki sadalīto.
		LinkedList<String> concatParts = new LinkedList<>();
		concatParts.addLast(initialParts[0]);
		for (int i = 1; i < initialParts.length; i++)
		{
			// Apvieno, ja pēdējā daļa beidzas ar elementiem, kas nevar būt frāzes beigās.
			if (concatParts.getLast().matches(".*(\\s[a-p]\\.\\s|\\bpiem\\.,\\s*|[\\-\u2014\u2013]\\s?|\\.</i>:\\s|[,;?!(](</i>)?\\s?(arī|biežāk|retāk|saīsināti:)\\s?)"))
				concatParts.addLast(concatParts.removeLast() + initialParts[i]);
				// Apvieno, ja pēdējā daļa satur numurētu apakšnozīmi un jaunā daļa sākas kursīvā (frāžu nozīmju piemēri)
			else if (concatParts.getLast().matches(".*\\s[a-o]\\.\\s((?<!</?i>).)*")
					&& initialParts[i].matches("\\s*<i>.*"))
				concatParts.addLast(concatParts.removeLast() + initialParts[i]);
				// Apvieno, ja pēdējā daļa beidzas ar komatu vai u.tml. bez kursīva un tālāk seko kursīvs ar mazo burtu vai defise.
			else if (concatParts.getLast().matches(".*(\\su\\.tml\\.\\)|,)\\s*")
					&& initialParts[i].matches("\\s*(<i>,?\\s*\\p{Ll}|[\\-\u2014\u2013]).*"))
				concatParts.addLast(concatParts.removeLast() + initialParts[i]);
				// Citādi neapvieno.
			else concatParts.addLast(initialParts[i]);
		}

		// Tad sadala to, kas nesadalījās - tekstuāls piemērs var būt
		// saplūdis kopā ar nākamo piemēru, aiz kā seko domuzīme, ja
		// viss ir kursīvā.
		Pattern resplitPat1 = Pattern.compile(
				"(.*?<i>(?:(?<!</i>).)*(?:</i>)?\\.\\s)" // Daļa kursīvā, kas beidzas ar punktu
						+ "(\\(?\\p{Lu}[^\\p{Lu}]*[\\-\u2014\u2013]\\s?.*)"); // Arī pirms domuzīmes ir jāpaliek reālam tekstam no reāliem burtiem.
		LinkedList<String> finalParts = new LinkedList<>();
		for (String concatPart : concatParts)
		{
			String inProgressPart = Editors.openCursive(concatPart);
			Matcher resplitter = resplitPat1.matcher(inProgressPart);
			while (resplitter.matches())
			{
				String preLast = resplitter.group(1);
				String last = resplitter.group(2);
				// TODO šitā izteiksme varētu nebūt pareiza.
				Matcher gramMatcher = Pattern
						.compile("(.*?)((?:<i>\\s*\\p{Lu}(?:(?<!/?i>)[^\\p{Lu}]\\)?)*\\.</i>:\\s*)?<i>\\s*)")
						.matcher(preLast);
				if (gramMatcher.matches())
				{
					preLast = gramMatcher.group(1);
					last = gramMatcher.group(2) + last;
				}
				preLast = Editors.closeCursive(preLast);
				last = Editors.openCursive(last);
				if (!preLast.trim().isEmpty()) finalParts.add(preLast);
				inProgressPart = last;
				resplitter = resplitPat1.matcher(inProgressPart);
			}
			finalParts.add(inProgressPart);
		}
		return finalParts;
	}

}
