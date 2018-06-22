package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;
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
 * Pašlaik te nav būtiski paplašināta Phrase funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class MLVVPhrase extends Phrase
{
	/**
	 * Skaidrojamā frāze latīniski - tikai taksoniem.
	 */
	public LinkedList<String> sciName;
	/**
	 * Savāc elementā izmantotos "flagText". Semikolu uzskata par atdalītāju.
	 */
	public TreeSet<String> getFlagStrings()
	{
		TreeSet<String> res = new TreeSet<>();
		if (grammar != null) res.addAll(((MLVVGram)grammar).getFlagStrings());
		if (subsenses != null) for (Sense s : subsenses)
			res.addAll(((MLVVSense)s).getFlagStrings());
		return res;
	}
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
		Matcher simpleMatcher = Pattern.compile("<i>[^:]+(:\\s+[\"\u201e\u201d][^:]+)?</i>").matcher(linePart);
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
		for (String variant : res.text)
			if (variant == null || variant.isEmpty()) res.text.remove(variant);

		if (res.text.isEmpty())
		{
			System.out.printf("Frāzes skaidrojumam \"%s\" nav neviena frāzes teksta",
					linePart);
			if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
			System.out.println();
		}

		return res;

	}

	/**
	 * Metode, kas no dotā šķirkļa fragmenta izgūst vienu vai vairākas taxona
	 * tipa frāzes.
	 * @param linePart	šķirkļa teksta daļa, kas apraksta tikai taksona tipa
	 *                  frāzes un neko citu.
	 * @return izgūtā frāze vai null
	 */
	public static ArrayList<MLVVPhrase> parseTaxons(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;

		ArrayList<MLVVPhrase> results = new ArrayList<>();
		MLVVPhrase res = new MLVVPhrase();
		results.add(res);
		res.type = PhraseTypes.TAXON;
		res.text = new LinkedList<>();
		res.sciName = new LinkedList<>();
		Matcher m = Pattern.compile("(?:<bullet/>)?\\s*<i>(.*?)</i>\\s*\\[([^\\]]*)\\](.*?)((?:<i>.*|<bullet/>.*)?)").matcher(linePart);
		if (m.matches())
		{
			res.text.add(Editors.removeCursive(m.group(1).trim()));
			String sciNames = Editors.removeCursive(m.group(2).trim());
			if (!sciNames.isEmpty())
				res.sciName.addAll(Arrays.asList(sciNames.split("\\s*[;,](\\s*arī)?\\s*")));
			String gloss = m.group(3).trim();
			if (gloss.equals(".")) gloss = null;
			else if (gloss.matches("[-\u2013\u2014]"))
				System.out.printf("Taksonam \"%s\" sanāk tukša skaidrojošā daļa pēc domuzīmes\n", linePart);
			else if (gloss.matches("[-\u2013\u2014].+"))
				gloss = gloss.substring(1).trim();
				// TODO: iespējams, ka te vajag brīdinājumu par trūkstošu domuzīmi.
			if (gloss != null)
			{
				res.subsenses = new LinkedList<>();
				res.subsenses.add(new MLVVSense(MLVVGloss.parse(gloss.trim())));
			}
			// TODO vai uzskatāmāk nebūs bez rekursijas?
			if (m.group(4) != null && !m.group(4).isEmpty())
				results.addAll(parseTaxons(m.group(4)));
		}
		else
		{
			String resText = linePart.trim();
			if (resText.matches("<i>((?!</i>).)*</i>"))
				resText = resText.substring(3, resText.length()-4);
			res.text.add(Editors.removeCursive(resText));
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
		// TODO: vai šeit likt \p{Lu}\p{Ll}+ nevis (Parn|Intr) ?
		Matcher m = Pattern.compile(
				"((?:(?:<i>)?\\s*(?:Pārn|Intr|Tr|Sal)\\.</i>:\\s*)?)"	//  Neobligāts Pārn. kursīvā :
					+ "((?:(?:\\.{2,3}|[\"\u201e\u201d])\\s*)?)<i>(.*?)</i>"	// neobligātas pieturzīmes <i> citāta teksts </i> neobligātas pieturz.
					+ "([.?!\"\u201e\u201d]*)\\s*\\((.*)\\)\\.?")			// (autors).
				.matcher(linePart);
		if (m.matches())
		{
			res.text.add(
					Editors.removeCursive((m.group(2) + m.group(3) + m.group(4)))
							.replaceAll("\\s\\s+", " ").trim());
			res.source = m.group(5).trim();
			String gramString = Editors.removeCursive(m.group(1)).trim();
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
		if (linePart != null && !linePart.isEmpty())
			res.addAll(parseAllSamplesAndPhrasals(linePart, lemma));
		// Apstrādā to, kas ir aiz tukšā vai pilnā aplīša.
		if (lineEndPart != null && !lineEndPart.isEmpty())
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

			// Apstrādā iegūtās daļas.
			for (String part : finalParts)
			{
				MLVVPhrase sample = parseSampleOrPhrasal(
						part, PhraseTypes.SAMPLE, lemma);
				if (sample != null) res.add(sample);
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
		if (linePart == null || linePart.trim().isEmpty()) return null;
		LinkedList<MLVVPhrase> res = new LinkedList<>();
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
				res.addAll(MLVVPhrase.parseTaxons(m.group(1)));
				res.addAll(parseAllPhrases(m.group(2), lemma));
			}
			else
			{
				System.out.printf("Taksons \"%s\" neatbilst atdalīšanas šablonam\n", linePart);
				res.addAll(MLVVPhrase.parseTaxons(linePart.substring("<bullet/>".length())));
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
				res.add(MLVVPhrase.extractQuote(m.group(1) + m.group(2)));
				res.addAll(parseAllPhrases(m.group(3), lemma));
			}
			else
			{
				System.out.printf("Citāts \"%s\" neatbilst atdalīšanas šablonam\n", linePart);
				res.add(MLVVPhrase.extractQuote(linePart.replaceFirst("<circle/>", "")));
			}
		}
		else
		{
			System.out.printf("Citāts vai taksons \"%s\" neatbilst atsalīšanas šablonam\n", linePart);
			res.add(MLVVPhrase.extractQuote(linePart));
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
		Matcher m = Pattern.compile("(.*\\.)(?::\\s+|</i>:\\s+<i>)((?!\"|\\p{Ll}).*)").matcher(linePart);
		Matcher gramConsts = Pattern.compile("(?:<i>)?\\s*(Tr\\.|Pārn\\.|Sal\\.|Intr\\.)(?:</i>)?: (?:<i>)?(.*)").matcher(linePart);
		if (m.matches())
		{
			res.grammar = new MLVVGram(m.group(1));
			linePart = m.group(2).trim();
		}
		else if (gramConsts.matches())
		{
			res.grammar = new MLVVGram(gramConsts.group(1));
			linePart = gramConsts.group(2).trim();
		}
		/*if (linePart.matches(".*\\.: (?!\"|\\p{Ll}).*"))
		{
			res.grammar = new MLVVGram(linePart
					.substring(0, linePart.indexOf(".: ") + 1));
			linePart = linePart.substring(linePart.indexOf(".: ") + 2).trim();
		} else if (linePart.matches(".*\\.</i>: <i>(?!\"|\\p{Ll}).*"))
		{
			res.grammar = new MLVVGram(linePart
					.substring(0, linePart.indexOf(".</i>: <i>") + 1));
			linePart = linePart
					.substring(linePart.indexOf(".</i>: <i>") + ".</i>: <i>".length())
					.trim();
		}*/
		linePart = linePart.replace("</i>: <i>\"", ": \"");
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
		Pattern phrasesplitter = Pattern.compile("(?:(?<=</i>)[,;?!]|(?<=[,;?!]</i>)) (?:arī|biežāk|retāk)\\s*(?=<i>)");
		ArrayList<String> beginParts = new ArrayList<>();
		Matcher tmp = phrasesplitter.matcher(preDefiseLinePart);
		int whereToStart = 0;
		while (tmp.find(whereToStart))
		{
			String begin = preDefiseLinePart.substring(0, tmp.start());
			String end = preDefiseLinePart.substring(tmp.end());
			if (begin.matches(".*\\([^\\)]*"))
				whereToStart = tmp.end();
			else
			{
				beginParts.add(begin);
				preDefiseLinePart = end;
				whereToStart = 0;
				tmp = phrasesplitter.matcher(preDefiseLinePart);
			}
		}
		beginParts.add(preDefiseLinePart);
		//String[] beginParts = preDefiseLinePart.split("(?:(?<=</i>)[,?!]|(?<=[,;?!]</i>)) (?:arī|biežāk|retāk)\\s*(?=<i>)");
		String gramText = "";
		for (String part : beginParts)
		{
			Matcher endsWithCursive = Pattern.compile("(.*?)(<i>(?:(?!</i>).)*?</i>[.]?)\\s*").matcher(part);
			// Frāze pati ir kursīvā
			if (part.startsWith("<i>") || part.startsWith("(<i>"))
			{
				// ir ar kolu atdalītā gramatika
				if (part.contains(".: "))
				{
					if (gramText.length() > 0) gramText = gramText + "; ";
					gramText = (gramText + part.substring(0, part.indexOf(".: ") + 1)).trim();
					part = part.substring(part.indexOf(".: ") + 2).trim();
				} else if (part.contains(".</i>: <i>"))
				{
					if (gramText.length() > 0) gramText = gramText + "; ";
					gramText = (gramText + part.substring(0, part.indexOf(".</i>: <i>") + 1))
							.trim();
					part = part.substring(part.indexOf(".</i>: <i>") + ".</i>: <i>".length())
							.trim();
				}

				String newText = Editors.removeCursive(part);
				if (newText.endsWith(",")) newText = newText.substring(0, newText.length()-1);
				text.add(newText);
			}
			// Kursīvs sākas kaut kur vidū un iet līdz beigām - tātad kursīvā ir gramatika
			// Izņēmums "... <i>arī</i> ...)
			else if (endsWithCursive.matches())
			{
				text.add(Editors.removeCursive(endsWithCursive.group(1).trim()));
				if (gramText.length() > 0) gramText = gramText + "; ";
				gramText = (gramText + endsWithCursive.group(2)).trim();
			}
			// Frāzē nekas nav kursīvā - tātad tur ir tikai frāze bez problēmām.
			else text.add(Editors.removeCursive(part));
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

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
	@Override
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		boolean hasPrev = false;

		if (type != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Type\":\"");
			res.append(JSONObject.escape(type));
			res.append("\"");
			hasPrev = true;
		}

		if (text != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Text\":[");
			res.append(text.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
					.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			res.append("]");
			hasPrev = true;
		}

		if (sciName != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"SciName\":[");
			res.append(sciName.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
					.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			res.append("]");
			hasPrev = true;
		}

		if (grammar != null)
		{
			if (hasPrev) res.append(", ");
			res.append(grammar.toJSON());
			hasPrev = true;
		}

		if (subsenses != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Senses\":");
			res.append(JSONUtils.objectsToJSON(subsenses));
			hasPrev = true;
		}

		if (source != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Source\":\"");
			res.append(JSONObject.escape(source));
			res.append("\"");
			hasPrev = true;
		}

		//res.append("}");
		return res.toString();
	}

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Element phraseN = doc.createElement("Phrase");
		if (type != null) phraseN.setAttribute("Type", type);
		if (text != null)
		{
			Node textN = doc.createElement("Text");
			for (String var : text)
			{
				Node textVar = doc.createElement("Variant");
				textVar.appendChild(doc.createTextNode(var));
				textN.appendChild(textVar);
			}
			//textN.appendChild(doc.createTextNode(text));
			phraseN.appendChild(textN);
		}
		if (sciName != null)
		{
			Node textN = doc.createElement("SciName");
			for (String var : sciName)
			{
				Node textVar = doc.createElement("Variant");
				textVar.appendChild(doc.createTextNode(var));
				textN.appendChild(textVar);
			}
			//textN.appendChild(doc.createTextNode(text));
			phraseN.appendChild(textN);
		}
		if (grammar != null) grammar.toXML(phraseN);
		if (subsenses != null)
		{
			Node sensesContN = doc.createElement("Senses");
			for (Sense s : subsenses) s.toXML(sensesContN);
			phraseN.appendChild(sensesContN);
		}
		if (source != null)
		{
			Node sourceN = doc.createElement("Source");
			sourceN.setTextContent(source);
			phraseN.appendChild(sourceN);
		}
		parent.appendChild(phraseN);
	}
}
