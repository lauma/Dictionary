package lv.ailab.dict.mlvv.analyzer.docparser;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.mlvv.struct.MLVVGram;
import lv.ailab.dict.mlvv.struct.MLVVPhrase;
import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sample;
import lv.ailab.dict.utils.Tuple;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhrasalHelper
{
	protected static PhrasalHelper singleton = new PhrasalHelper();
	public static PhrasalHelper me()
	{
		return singleton;
	}

	/**
	 * No simbolu virknes, kas satur dažādas frāzes, izgūst frāžu masīvu.
	 * @param linePart	šķirkļa daļa, kas jāpārtaisa frāzēs
	 * @param lemma		šķirkļavārds šķirklim, kurā šī frāze atrodas (kļūdu
	 *                  paziņojumiem)
	 * @return	izgūto frāžu masīvs (tukšs, ja nekā nav)
	 */
	public Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>>
		parseAllPhrases(MLVVElementFactory factory, String linePart, String lemma)
	{
		Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> res =
				Tuple.of(new LinkedList<>(), new LinkedList<>());
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
		{
			Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> newRes =
					parseAllSamplesAndPhrasals(factory, linePart, lemma);
			res.first.addAll(newRes.first);
			res.second.addAll(newRes.second);
		}
		// Apstrādā to, kas ir aiz tukšā vai pilnā aplīša.
		if (lineEndPart != null && !lineEndPart.isEmpty())
		{
			Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> newRes =
					PhrasalHelper.me().parseAllTaxonsAndQuotes(factory, lineEndPart, lemma);
			res.first.addAll(newRes.first);
			res.second.addAll(newRes.second);
		}
		return res;
	}

	/**
	 * Izanalizē simbolu virkni, kas satur ar "bumbiņām" atdalītās frāzes -
	 * citātus un taksonus.
	 * @param linePart	apstrādājamā rindiņas daļa.
	 * @param lemma		individuālo frāžu apstrādē reizēm vajag zināt lemmu.
	 * @return izgūto piemēru un frāžu saraksti
	 */
	public Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>>
			parseAllTaxonsAndQuotes(
					MLVVElementFactory factory, String linePart, String lemma)
	{
		if (linePart == null || linePart.trim().isEmpty()) return null;
		Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> res
				= Tuple.of(new LinkedList<>(), new LinkedList<>());
		// Pirms <circle/> var būt "Pārn.:" vai "Intr.:", bet pirms <bullet/> tādam nevajadzētu būt.
		if (linePart.startsWith("<bullet/>"))
		{
			Pattern taxonPat = Pattern.compile(
					"(<bullet/>\\s*<i>.*?</i>\\s*\\[[^\\[\\]]*\\]" // bullet, suga kursīvā, latīniskais nosauums kvadrātiekavās.
							+ "(?:\\s+[-\u2013\u2014]?" // neobligāts skaidrojums parasti sākas ar domuzīmi, kas ievada skaidrojumu
							+ "(?:(?!<(?:/?i|bullet/)>|(?:(?:<i>\\s*)?\\p{Lu}\\p{Ll}+\\.</i>:\\s*)?<circle/>).)*" // skaidrojums nedrīkst saturēt <bullet/>, <i>, </i> un <circle/>.
							+ "|\\.)?)" // skaidrojuma vietā var būt vienkārši punkts.
							+ "(.*)"); // pārējais

			Matcher m = taxonPat.matcher(linePart);
			if (m.matches())
			{
				res.second.addAll(PhraseParser.me().parseTaxons(factory, m.group(1)));
				Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> newRes =
						PhrasalHelper.me().parseAllPhrases(factory, m.group(2), lemma);
				res.first.addAll(newRes.first);
				res.second.addAll(newRes.second);
			}
			else
			{
				System.out.printf("Taksons \"%s\" neatbilst atdalīšanas šablonam\n", linePart);
				res.second.addAll(PhraseParser.me().parseTaxons(
						factory, linePart.substring("<bullet/>".length())));
			}
		}
		else if (linePart.matches("((<i>\\s*)?\\p{Lu}\\p{Ll}+\\.</i>:\\s*)?<circle/>.*"))
		{
			Pattern quotePat = Pattern.compile(
					"((?:(?:<i>\\s*)?\\p{Lu}\\p{Ll}+\\.</i>:\\s*)?)<circle/>\\s*((?:(?:\\.{2,3}|[\"\u201e\u201d])\\s*)?<i>.*?</i>[.?!\"\u201e\u201d]*\\s*\\(.*?\\)\\.?)(.*)");
			// Neobligāts Pārn. kursīvā : <circle/> neobligātas pieturzīmes <i> citāta teksts </i> neobligātas pieturz. (autors) pārējais
			Matcher m = quotePat.matcher(linePart);
			if (m.matches())
			{
				res.first.add(SampleParser.extractQuote(
						factory, m.group(1) + m.group(2)));
				Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> newRes =
						PhrasalHelper.me().parseAllPhrases(
								factory, m.group(3), lemma);
				res.first.addAll(newRes.first);
				res.second.addAll(newRes.second);
			}
			else
			{
				System.out.printf("Citāts \"%s\" neatbilst atdalīšanas šablonam\n", linePart);
				res.first.add(SampleParser.extractQuote(
						factory, linePart.replaceFirst("<circle/>", "")));
			}
		}
		else
		{
			System.out.printf("Citāts vai taksons \"%s\" neatbilst atsalīšanas šablonam\n", linePart);
			res.first.add(SampleParser.extractQuote(factory, linePart));
		}
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
	protected Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>>
		parseAllSamplesAndPhrasals(
				MLVVElementFactory factory, String linePart, String lemma)
	{
		if (linePart == null || linePart.trim().isEmpty()) return null;
		Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> res =
				Tuple.of(new LinkedList<>(), new LinkedList<>());

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
			res.first.add(SampleParser.parseNoGlossSample(factory, m.group(1).trim()));
			linePart = m.group(2);
			m = splitter.matcher(linePart);
		}
		// Tālāk sadala lielos piemērus ar skaidrojumu, bet uzmanās, vai
		// pa vidu tomēr nav mazie.
		if (linePart.length() > 0 && !linePart.matches("\\s*</i>\\s*"))
		{
			LinkedList<String> finalParts = splitExplainedPhrasals(linePart);
			Pattern dashPhrases = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)");
			Pattern colonPhrases = Pattern.compile("(.*?</i>):\\s*(<i>.*)");
			Pattern simplePhrases = Pattern.compile("<i>[^:]+(:\\s+[\"\u201e\u201d][^:]+)?</i>");

			// Apstrādā iegūtās daļas.
			for (String part : finalParts)
			{
				// Ja tukšs, tad neko.
				if (part == null) continue;
				part = part.trim();
				if (part.length() < 1) continue;

				// Izanalizē frāzi ar skaidrojumu
				Matcher dashMatcher = dashPhrases.matcher(part);
				if (dashMatcher.matches())
				{
					MLVVPhrase resPhrase = PhraseParser.me().makePhrasalFromSplitText(
							factory,
							dashMatcher.group(1).trim(), dashMatcher.group(2).trim(),
							Phrase.Type.STABLE_UNIT, lemma, part);
							//Phrase.Type.EXPLAINED_SAMPLE, lemma, part);
					res.second.add(resPhrase);
					continue;
				}

				// Analizē bezskaidrojumu gadījumus
				Matcher colonMatcher = colonPhrases.matcher(part);
				Matcher simpleMatcher = simplePhrases.matcher(part);

				Sample resSample = new Sample();
				//resSample.type = Sample.Type.SAMPLE;
				//resSample.text = new LinkedList<>();

				// Izanalizē frāzi ar gramatiku, kas atdalīta ar kolu, bet bez
				// skaidrojuma.
				if (colonMatcher.matches())
				{
					String begin = colonMatcher.group(1).trim();
					if (begin.length() > 0)
					{
						MLVVGram tmp = factory.getNewGram();
						tmp.reinitialize(begin);
						resSample.grammar = tmp;
					}
					String end = colonMatcher.group(2).trim();
					//resSample.text.add(Editors.removeCursive(end));
					resSample.text = Editors.removeCursive(end);
				}
				// Frāze bez gramatikas un bez skaidrojuma.
				else if (simpleMatcher.matches())
					//resSample.text.add(Editors.removeCursive(part));
					resSample.text = Editors.removeCursive(part);

				// Nu nesanāca!
				else
				{
					System.out.printf("Neizdodas izanalizēt piemēru \"%s\"", part);
					if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
					System.out.println();
					//resSample.text.add(part);
					resSample.text = part;
				}

				//resSample.variantCleanup();
				if (resSample.text.isEmpty())
				{
					System.out.printf("No \"%s\" sanāca piemērs bez teksta", linePart);
					if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
					System.out.println();
				}
				res.first.add(resSample);
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
	protected LinkedList<String> splitExplainedPhrasals(String linePart)
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
