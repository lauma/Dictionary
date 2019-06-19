package lv.ailab.dict.mlvv.analyzer.docparser;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.mlvv.struct.MLVVGram;
import lv.ailab.dict.mlvv.struct.MLVVPhrase;
import lv.ailab.dict.mlvv.struct.MLVVSense;
import lv.ailab.dict.struct.Phrase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraseParser
{
	protected PhraseParser(){};
	protected static PhraseParser singleton = new PhraseParser();
	public static PhraseParser me()
	{
		return singleton;
	}

	/**
	 * Metode, kas izgūst dotā tipa frāzi no dotās simbolu virknes.
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo frāzi un
	 *                      neko citu
	 * @param phraseType	frāzes tips (Type)
	 * @param lemma			šķirkļavārds šķirklim, kurā šī frāze atrodas (kļūdu
	 *                      paziņojumiem)
	 * @return	izgūtā frāze vai null
	 */
	public MLVVPhrase parseSpecialPhrasal(
			String linePart, Phrase.Type phraseType, String lemma)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		Matcher dashMatcher = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)").matcher(linePart);
		MLVVPhrase res = MLVVElementFactory.me().getNewPhrase();
		res.type = phraseType;
		res.text = new LinkedList<>();

		// Izanalizē frāzi ar skaidrojumu
		if (dashMatcher.matches())
		{
			String begin = dashMatcher.group(1).trim();
			String end = dashMatcher.group(2).trim();
			// Izanalizē frāzes tekstu un pie tā piekārtoto gramatiku.
			extractGramAndText(res, begin);
			// Analizē skaidrojumus.
			parseGramAndGloss(res, end, lemma, linePart);
		}
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
	public ArrayList<MLVVPhrase> parseTaxons(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;

		ArrayList<MLVVPhrase> results = new ArrayList<>();
		MLVVPhrase res = MLVVElementFactory.me().getNewPhrase();
		results.add(res);
		res.type = Phrase.Type.TAXON;
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
			{
				System.out.printf("Taksonam \"%s\" sanāk tukša skaidrojošā daļa pēc domuzīmes\n", linePart);
				gloss = null;
			}
			else if (gloss.matches("[-\u2013\u2014].+"))
				gloss = gloss.substring(1).trim();
				// TODO: iespējams, ka te vajag brīdinājumu par trūkstošu domuzīmi.
			if (gloss != null && !gloss.isEmpty())
			{
				res.subsenses = new LinkedList<>();
				MLVVSense tmp = MLVVElementFactory.me().getNewSense();
				tmp.reinitialize(GlossParser.me().parse(gloss.trim()));
				res.subsenses.add(tmp);
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
	 * No apstrādājamās rindiņas daļas izvelk ar kursīvu atdalītas gramatikas un
	 * vienu vai vairākas glosas.
	 * Funkcija oriģināli izstrādāta sarežgītu frāžu (ar skaidrojumiem)
	 * analīzei.
	 * @param preDashLinePart	rindiņas daļa, kas tiek izmantota gramatikas un
	 *                          frāžu tekstu formēšanai.
	 * @param postDashLinePart	rindiņas daļa, kas tiek izmantota gramatikas
	 *                          un frāžu tekstu formēšanai.
	 * @param lemma				reizēm vajag zināt šķirkļa lemmu.
	 * @param linePartForErrorMsg	teksts ko norāda kā oriģināltekstu kļūdu
	 *                              ziņojumos.
	 */
	public MLVVPhrase makePhrasalFromSplitText(
			String preDashLinePart, String postDashLinePart, Phrase.Type type,
			String lemma, String linePartForErrorMsg)
	{
		MLVVPhrase resPhrase = MLVVElementFactory.me().getNewPhrase();
		resPhrase.type = type;
		resPhrase.text = new LinkedList<>();
		// Izanalizē frāzes tekstu un pie tā piekārtoto gramatiku.
		extractGramAndText(resPhrase, preDashLinePart);
		// Analizē skaidrojumus.
		parseGramAndGloss(resPhrase, postDashLinePart, lemma, linePartForErrorMsg);
		resPhrase.variantCleanup();
		// Pabrīdina, ja nu ir sanākusi frāze bez teksta.
		if (resPhrase.text.isEmpty())
		{
			System.out.printf("Frāzes skaidrojumam \"%s\" nav neviena frāzes teksta",
					linePartForErrorMsg);
			if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
			System.out.println();
		}
		return resPhrase;
	}

	/**
	 * No apstrādājamās rindiņas daļas izvelk ar kolu vai kursīvu atdalītu
	 * gramatiku un vienu vai vairākus frāzes tekstus.
	 * Funkcija oriģināli izstrādāta sarežgītu frāžu (ar skaidrojumiem)
	 * analīzei.
	 * @param preDashLinePart	rindiņas daļa, kas tiek izmantota gramatikas un
	 *                          frāžu tekstu formēšanai.
	 */
	protected void extractGramAndText(MLVVPhrase phrase, String preDashLinePart)
	{
		// Ja vajag, frāzi sadala.
		Pattern phrasesplitter = Pattern.compile("(?:(?<=</i>)[,;?!]|(?<=[,;?!]</i>)) (?:arī|biežāk|retāk)\\s*(?=<i>)");
		ArrayList<String> beginParts = new ArrayList<>();
		Matcher tmp = phrasesplitter.matcher(preDashLinePart);
		int whereToStart = 0;
		while (tmp.find(whereToStart))
		{
			String begin = preDashLinePart.substring(0, tmp.start());
			String end = preDashLinePart.substring(tmp.end());
			if (begin.matches(".*\\([^\\)]*"))
				whereToStart = tmp.end();
			else
			{
				beginParts.add(begin);
				preDashLinePart = end;
				whereToStart = 0;
				tmp = phrasesplitter.matcher(preDashLinePart);
			}
		}
		beginParts.add(preDashLinePart);
		//String[] beginParts = preDashLinePart.split("(?:(?<=</i>)[,?!]|(?<=[,;?!]</i>)) (?:arī|biežāk|retāk)\\s*(?=<i>)");
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
				phrase.text.add(newText.trim());
			}
			// Kursīvs sākas kaut kur vidū un iet līdz beigām - tātad kursīvā ir gramatika
			// Izņēmums "... <i>arī</i> ...)
			else if (endsWithCursive.matches())
			{
				phrase.text.add(Editors.removeCursive(endsWithCursive.group(1).trim()));
				if (gramText.length() > 0) gramText = gramText + "; ";
				gramText = (gramText + endsWithCursive.group(2)).trim();
			}
			// Frāzē nekas nav kursīvā - tātad tur ir tikai frāze bez problēmām.
			else phrase.text.add(Editors.removeCursive(part.trim()));
		}
		if (gramText.length() > 0)
		{
			MLVVGram tmpGr = MLVVElementFactory.me().getNewGram();
			tmpGr.reinitialize(gramText);
			phrase.grammar = tmpGr;
		}
	}

	/**
	 * No apstrādājamās rindiņas daļas izvelk ar kursīvu atdalītas gramatikas un
	 * vienu vai vairākas glosas.
	 * Funkcija oriģināli izstrādāta sarežgītu frāžu (ar skaidrojumiem)
	 * analīzei.
	 * @param postDashLinePart	rindiņas daļa, kas tiek izmantota gramatikas un
	 *                          frāžu tekstu formēšanai.
	 * @param lemma				reizēm vajag zināt šķirkļa lemmu.
	 * @param linePartForErrorMsg    teksts ko norāda kā oriģināltekstu kļūdu
	 *                               ziņojumos.
	 */
	protected void parseGramAndGloss(
			MLVVPhrase phrase, String postDashLinePart, String lemma,
			String linePartForErrorMsg)
	{
		// Ja te ir lielā gramatika un vairākas nozīmes, tad gramatiku piekārto
		// pie frāzes, nevis pirmās nozīmes.
		if (postDashLinePart.startsWith("</i>"))
			postDashLinePart = postDashLinePart.substring("</i>".length()).trim(); // ja defise nejauši ir kursīvā.
			//else if (end.startsWith("<i>") && end.contains(" b. "))
		else
		{
			Matcher endMatcher = Pattern
					.compile("(.*?)(?:</i> | </i>)(a\\.\\s.*?\\sb\\.\\s.*)")
					.matcher(postDashLinePart);
			if (endMatcher.matches())
			{
				String gram = endMatcher.group(1).trim();
				if (phrase.grammar == null)
				{
					if (gram.startsWith("<i>")) gram = gram.substring(3);
					MLVVGram tmp = MLVVElementFactory.me().getNewGram();
					tmp.reinitialize(gram);
					phrase.grammar = tmp;
					postDashLinePart = endMatcher.group(2).trim();
				} else
					System.out.printf(
							"Frāzē \"%s\" ir divas vispārīgās gramatikas, tāpēc nozīmes netiek sadalītas\n",
							linePartForErrorMsg);
			}
		}

		String[] subsenseTexts = new String[]{postDashLinePart};
		// Ir vairākas nozīmes.
		if (postDashLinePart.startsWith("a."))
		{
			subsenseTexts = postDashLinePart.split("\\s(?=[bcdefghijklmnop]\\.\\s)");
			// Ja skaidrojums ir formā "a. skaidrojums <i>piemēri b.</i> b. skaidrojums...",
			// tad var sanākt nepareizs dalījums dalot tā pa prasto.
			if (postDashLinePart.contains("<i>"))
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
		parseSubsenses(phrase, subsenseTexts, lemma);
	}

	/**
	 * No rindas fragmentu masīva izgūst apakšnozīmes - no katra fragmenta
	 * vienu.
	 * @param subsenseTexts	apstrādājamo rindas fragmentu masīvs.
	 * @param lemma			veidojot jaunu apakšnozīmi, reizēm vajag zināt
	 *                      šķirkļa lemmu.
	 */
	protected void parseSubsenses(
			MLVVPhrase phrase, String[] subsenseTexts, String lemma)
	{
		// Normalizē un apstrādā izgūtos apakšnozīmju fragmentus.
		phrase.subsenses = new LinkedList<>();
		for (String subsense : subsenseTexts)
		{
			if (subsense.contains("</i>") && !subsense.contains("<i>"))
				subsense = "<i>" + subsense;
			else if (subsense.contains("<i>") && !subsense.contains("</i>"))
				subsense = subsense + "</i>";
			phrase.subsenses.add(SenseParser.me().parse(subsense, lemma));
		}
		// Ja frāzei ir vairākas nozīmes, tās sanumurē.
		phrase.enumerateGloses();
	}

}
