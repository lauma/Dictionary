package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
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
	/**
	 * Metode, kas izgūst dotā tipa frāzi no dotās simbolu virknes.
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo frāzi un
	 *                      neko citu
	 * @param phraseType	frāzes tips (PhraseTypes)
	 * @param lemma			šķirkļavārds šķirklim, kurā šī frāze atrodas (kļūdu
	 *                      paziņojumiem)
	 * @return	izgūtā frāze vai null
	 */
	public static MLVVPhrase parseSampleOrPhrasal(String linePart, String phraseType, String lemma)
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
			// Izanalizē frāzes tekstu un pie tā piekārtoto gramatiku.
			res.extractGramAndText(begin);
			// Analizē skaidrojumus.
			res.parseGramAndGloss(end, lemma, linePart);
		}

		// Izanalizē frāzi ar gramatiku, kas atdalīta ar kolu, bet bez
		// skaidrojuma.
		else if (colonMatcher.matches())
		{
			String begin = colonMatcher.group(1).trim();
			if (begin.length() > 0) res.grammar = new MLVVGram(begin);
			String end = colonMatcher.group(2).trim();
			res.text.add(Editors.removeCursive(end));
		}

		// Frāze bez gramatikas un bez skaidrojuma.
		else if (simpleMatcher.matches())
			res.text.add(Editors.removeCursive(linePart));

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
	public static ArrayList<MLVVPhrase> parseTaxons(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;

		ArrayList<MLVVPhrase> results = new ArrayList<>();
		MLVVPhrase res = new MLVVPhrase();
		results.add(res);
		res.type = PhraseTypes.TAXON;
		res.text = new LinkedList<>();
		Matcher m = Pattern.compile("<i>(.*?)</i>\\s*\\[([^\\]]*)\\](.*?)((?:<i>.*|<bullet/>.*)?)").matcher(linePart);
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
			res.subsenses.add(new Sense(MLVVGloss.parse(gloss.trim())));
			// TODO vai uzskatāmāk nebūs bez rekursijas?
			if (m.group(4) != null && !m.group(4).isEmpty())
				results.addAll(parseTaxons(m.group(4)));
		}
		else
		{
			String resText = linePart.trim();
			if (resText.matches("<i>((?!</i>).)*</i>"))
				resText = resText.substring(3, resText.length()-4);
			res.text.add(resText);
			System.out.printf("Taksons \"%s\" neatbilst apstrādes šablonam\n", resText);
		}
		return results;

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
		res.addAll(parseAllSamplesAndPhrasals(linePart, lemma));
		// Apstrādā to, kas ir aiz tukšā vai pilnā aplīša.
		res.addAll(parseAllTaxonsAndQuotes(lineEndPart, lemma));
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
		LinkedList<MLVVPhrase> res = new LinkedList<>();

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
				res.add(MLVVPhrase.parseNoGlossSample(m.group(1).trim()));
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
						"(.*<i>(?:(?:(?<!</i>).)*\\.\\s)?)(\\(?\\p{Lu}[^\\p{Lu}]*[\\-\u2014\u2013]\\s?.*)");
				LinkedList<String> finalParts = new LinkedList<>();
				for (String concatPart : concatParts)
				{
					Matcher resplitter = resplitPat1.matcher(concatPart);
					if (resplitter.matches())
					{
						String preLast = resplitter.group(1);
						String last = resplitter.group(2);
						Matcher gramMatcher = Pattern
								.compile("(.*?)((?:<i>\\s*\\p{Lu}(?:(?<!/?i>)[^\\p{Lu}])*\\.</i>:\\s*)?<i>\\s*)")
								.matcher(preLast);
						if (gramMatcher.matches())
						{
							preLast = gramMatcher.group(1);
							last = gramMatcher.group(2) + last;
						}
						preLast = Editors.closeCursive(preLast);
						last = Editors.openCursive(last);
						if (!preLast.trim().isEmpty()) finalParts.add(preLast);
						finalParts.add(last);
					}
					else finalParts.add(concatPart);
				}

				// Apstrādā iegūtās daļas.
				for (String part : finalParts)
				{
					MLVVPhrase sample = parseSampleOrPhrasal(
							part, PhraseTypes.SAMPLE, lemma);
					if (sample != null) res.add(sample);
				}
			}
		}
		return res;
	}

	/**
	 * Izanalizē simbolu virkni, kas satur ar "bumbiņām" atdalītās frāzes -
	 * citātus un taksonus.
	 * @param linePart	apstrādājamā rindiņas daļa.
	 * @param lemma		individuālo frāžu apstrādē reizēm vajag zināt lemmu.
	 * @return izgūto frāžu saraksts.
	 */
	protected static LinkedList<MLVVPhrase> parseAllTaxonsAndQuotes(
			String linePart, String lemma)
	{
		LinkedList<MLVVPhrase> res = new LinkedList<>();
		// Pirms <circle/> var būt "Pārn.:", bet pirms <bullet/> tādam nevajadzētu būt.
		if (linePart != null && linePart.startsWith("<bullet/>"))
		{
			Pattern taxonPat = Pattern.compile(
					"<bullet/>\\s*(<i>.*?</i>\\s*\\[.*\\](?:\\s+[-\u2013\u2014]?(?:(?!(?:<(?:/?i|bullet/)>|(?:(?:<i>\\s*)?Pārn\\.</i>:\\s*)?<circle/>)).)*|\\.)?)(.*)");
			// <bullet/> suga kursīvā [latīniskais nos] - skaidrojums līdz nākamajam "bullet" vai "i", vai "circle" un pārējais
			Matcher m = taxonPat.matcher(linePart);
			if (m.matches())
			{
				res.addAll(MLVVPhrase.parseTaxons(m.group(1)));
				res.addAll(parseAllPhrases(m.group(2), lemma));
			}
			else
			{
				System.out.printf("Taksons \"%s\" neatbilst atdalīšanas šablonam\n", linePart);
				res.addAll(MLVVPhrase.parseTaxons(linePart.substring("<bullet/>".length())));
			}
		}
		else if (linePart != null && linePart.matches("((<i>\\s*)?Pārn\\.</i>:\\s*)?<circle/>.*"))
		{
			Pattern quotePat = Pattern.compile(
					"((?:(?:<i>\\s*)?Pārn\\.</i>:\\s*)?)<circle/>\\s*((?:(?:\\.{2,3}|\")\\s*)?<i>.*?</i>[.?!\"]*\\s*\\(.*?\\)\\.?)(.*)");
			// Neobligāts Pārn. kursīvā : <circle/> neobligātas pieturzīmes <i> citāta teksts </i> neobligātas pieturz. (autors) pārējais
			Matcher m = quotePat.matcher(linePart);
			if (m.matches())
			{
				res.add(MLVVPhrase.extractQuote(m.group(1) + m.group(2)));
				res.addAll(parseAllPhrases(m.group(3), lemma));
			}
			else
			{
				System.out.printf("Citāts \"%s\" neatbilst atdalīšanas šablonam\n", linePart);
				res.add(MLVVPhrase.extractQuote(linePart.replaceFirst("<circle/>", "")));
			}
		}
		return res;
	}
	/**
	 * // TODO vai šis nedublējas ar parseSampleOrPhrasal?
	 * No dotā rindas fragmenta izgūst frāzi bez skaidrojuma, bet ar gramatiku,
	 * kas, iespējams, atdalīta ar kolu.
	 * Funkcija oriģināli izstrādāta kā daļa no frāžu bloka analīzes
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo frāzi un
	 *                      neko citu
	 */
	protected static MLVVPhrase parseNoGlossSample(String linePart)
	{
		MLVVPhrase res = new MLVVPhrase();
		res.type = PhraseTypes.SAMPLE;
		res.text = new LinkedList<>();
		if (linePart.contains(".: "))
		{
			res.grammar = new MLVVGram(linePart
					.substring(0, linePart.indexOf(".: ") + 1));
			linePart = linePart.substring(linePart.indexOf(".: ") + 2).trim();
		} else if (linePart.contains(".</i>: <i>"))
		{
			res.grammar = new MLVVGram(linePart
					.substring(0, linePart.indexOf(".</i>: <i>") + 1));
			linePart = linePart
					.substring(linePart.indexOf(".</i>: <i>") + ".</i>: <i>"
							.length()).trim();
		}
		res.text.add(linePart);
		return res;
	}

	/**
	 * No apstrādājamās rindiņas daļas izvelk ar kolu vai kursīvu atdalītu
	 * gramatiku un vienu vai vairākus frāzes tekstus.
	 * Funkcija oriģināli izstrādāta sarežgītu frāžu (ar skaidrojumiem)
	 * analīzei.
	 * @param preDefiseLinePart	rindiņas daļa, kas tiek izmantota gramatikas un
	 *                          frāžu tekstu formēšanai.
	 */
	protected void extractGramAndText(String preDefiseLinePart)
	{
		// Ja vajag, frāzi sadala.
		String[] beginParts = preDefiseLinePart.split("(?:(?<=</i>), |(?<=, </i>))arī\\s*<i>");
		String gramText = "";
		for (String part : beginParts)
		{
			// Frāze pati ir kursīvā
			if (part.startsWith("<i>") || part.startsWith("(<i>"))
			{
				// ir ar kolu atdalītā gramatika
				if (part.contains(".: "))
				{
					if (gramText.length() > 0) gramText = gramText + "; ";
					gramText = (gramText + part.substring(0, part.indexOf(".: ") + 1)).trim();
					part = part.substring(part.indexOf(".: ") + 2).trim();
				} else if (preDefiseLinePart.contains(".</i>: <i>"))
				{
					if (gramText.length() > 0) gramText = gramText + "; ";
					gramText = (gramText + part.substring(0, part.indexOf(".</i>: <i>") + 1))
							.trim();
					part = part.substring(part.indexOf(".</i>: <i>") + ".</i>: <i>".length())
							.trim();
				}

				text.add(Editors.removeCursive(part));
			}
			// Kursīvs sākas kaut kur vidū - tātad kursīvā ir gramatika
			else if (part.contains("<i>"))
			{
				text.add(part.substring(0, part.indexOf("<i>")).trim());
				if (gramText.length() > 0) gramText = gramText + "; ";
				gramText = (gramText + part.substring(part.indexOf("<i>"))).trim();
			}
			// Frāzē nekas nav kursīvā - tātad tur ir tikai frāze bez problēmām.
			else text.add(part);
		}
		if (gramText.length() > 0) grammar = new MLVVGram(gramText);
	}

	/**
	 * No apstrādājamās rindiņas daļas izvelk ar kursīvu atdalītas gramatikas un
	 * vienu vai vairākas glosas.
	 * Funkcija oriģināli izstrādāta sarežgītu frāžu (ar skaidrojumiem)
	 * analīzei.
	 * @param postDefiseLinePart	rindiņas daļa, kas tiek izmantota gramatikas
	 *                              un frāžu tekstu formēšanai.
	 * @param lemma					reizēm vajag zināt šķirkļa lemmu.
	 */
	protected void parseGramAndGloss(String postDefiseLinePart, String lemma,
			String linePartForErrorMsg)
	{
		// Ja te ir lielā gramatika un vairākas nozīmes, tad gramatiku piekārto
		// pie frāzes, nevis pirmās nozīmes.
		if (postDefiseLinePart.startsWith("</i>"))
			postDefiseLinePart = postDefiseLinePart.substring("</i>".length()).trim(); // ja defise nejauši ir kursīvā.
			//else if (end.startsWith("<i>") && end.contains(" b. "))
		else
		{
			Matcher endMatcher = Pattern
					.compile("(.*?)(?:</i> | </i>)(a\\.\\s.*?\\sb\\.\\s.*)")
					.matcher(postDefiseLinePart);
			if (endMatcher.matches())
			{
				String gram = endMatcher.group(1).trim();
				if (grammar == null)
				{
					if (gram.startsWith("<i>")) gram = gram.substring(3);
					grammar = new MLVVGram(gram);
					postDefiseLinePart = endMatcher.group(2).trim();
				} else
					System.out.printf(
							"Frāzē \"%s\" ir divas vispārīgās gramatikas, tāpēc nozīmes netiek sadalītas\n",
							linePartForErrorMsg);
			}
		}

		String[] subsenseTexts = new String[]{postDefiseLinePart};
		// Ir vairākas nozīmes.
		if (postDefiseLinePart.startsWith("a."))
		{
			subsenseTexts = postDefiseLinePart.split("\\s(?=[bcdefghijklmnop]\\.\\s)");
			// Ja skaidrojums ir formā "a. skaidrojums <i>piemēri b.</i> b. skaidrojums...",
			// tad var sanākt nepareizs dalījums dalot tā pa prasto.
			if (postDefiseLinePart.contains("<i>"))
			{
				LinkedList<String> betterGloses = new LinkedList<>();
				betterGloses.addLast(subsenseTexts[0]);
				for (int i = 1; i < subsenseTexts.length; i++)
				{
					String prev = betterGloses.getLast();
					if (prev.indexOf("<i>") > prev.indexOf("</i>"))
						betterGloses.addLast(
								betterGloses.removeLast() + " " +subsenseTexts[i]);
					else betterGloses.addLast(subsenseTexts[i]);
				}
				subsenseTexts = betterGloses.toArray(new String[betterGloses.size()]);
			}
			subsenseTexts = Arrays.stream(subsenseTexts)
					.map(s -> s.substring(2).trim())
					.toArray(String[]::new);
		}
		parseSubsenses(subsenseTexts, lemma);
	}

	/**
	 * No rindas fragmentu masīva izgūst apakšnozīmes - no katra fragmenta
	 * vienu.
	 * @param subsenseTexts	apstrādājamo rindas fragmentu masīvs.
	 * @param lemma			veidojot jaunu apakšnozīmi, reizēm vajag zināt
	 *                      šķirkļa lemmu.
	 */
	protected void parseSubsenses(String[] subsenseTexts, String lemma)
	{
		// Normalizē un apstrādā izgūtos apakšnozīmju fragmentus.
		subsenses = new LinkedList<>();
		for (String subsense : subsenseTexts)
		{
			if (subsense.contains("</i>") && !subsense.contains("<i>"))
				subsense = "<i>" + subsense;
			else if (subsense.contains("<i>") && !subsense.contains("</i>"))
				subsense = subsense + "</i>";
			subsenses.add(MLVVSense.parse(subsense, lemma));
		}
		// Ja frāzei ir vairākas nozīmes, tās sanumurē.
		enumerateGloses();
	}

	/**
	 * Ja frāzei ir vairākas nozīmes, tās sanumurē.
	 */
	protected void enumerateGloses()
	{
		if (subsenses != null && subsenses.size() > 1)
			for (int senseNumber = 0; senseNumber < subsenses.size(); senseNumber++)
			{
				Sense sense = subsenses.get(senseNumber);
				if (sense.ordNumber != null)
					System.out.printf(
							"Frāzē \"%s\" nozīme ar numuru \"%s\" tiek pārnumurēta par \"%s\"\n",
							text, sense.ordNumber, senseNumber + 1);
				sense.ordNumber = Integer.toString(senseNumber + 1);
			}
	}
}
