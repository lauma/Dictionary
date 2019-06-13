package lv.ailab.dict.mlvv.analyzer.docparser;

import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.mlvv.struct.MLVVGram;
import lv.ailab.dict.mlvv.struct.MLVVPhrase;
import lv.ailab.dict.mlvv.struct.MLVVSense;
import lv.ailab.dict.struct.Sample;
import lv.ailab.dict.utils.Tuple;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SenseParser
{
	protected SenseParser(){};
	protected static SenseParser singleton = new SenseParser();
	public static SenseParser me()
	{
		return singleton;
	}

	/**
	 * Metode, kas izgūst nozīmi vai nozīmes niansi no dotās simbolu virknes.
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo nozīmi un
	 *                      neko citu
	 * @param lemma			šķirkļavārds šķirklim, kurā šī nozīme atrodas (kļūdu
	 *                      paziņojumiem)
	 * @return	izgūtā nozīme vai null
	 */
	public MLVVSense parse(String linePart, String lemma)
	{
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		MLVVSense res = MLVVElementFactory.me().getNewSense();

		// Nocērp un apstrādā beigas, lai nemaisās: nozīmes nianses
		linePart = extractSubsenses(res, linePart, lemma);

		// Nocērp no sākuma saprotamo: nozīmes gramatikau
		linePart = extractBeginGrammar(res, linePart, lemma);

		// Tagad vajadzētu sekot definīcijai.
		linePart = extractGloss(res, linePart);

		// Piemēru analīze.
		Tuple<LinkedList<Sample>,LinkedList<MLVVPhrase>> phrasals
				= PhrasalHelper.me().parseAllPhrases(linePart, lemma);
		if (phrasals != null && phrasals.first.size() > 0)
			res.examples = new LinkedList<>(phrasals.first);
		if (phrasals != null && phrasals.second.size() > 0)
			res.phrases = new LinkedList<>(phrasals.second);
		return res;
	}

	/**
	 * No apstrādājamās rindiņas nogriež un izparsē ar "<lines/>" atdalītās
	 * apakšnozīmes.
	 * @param linePart	apstrādājamā rindiņa (rindiņas daļa).
	 * @param lemma		reizēm vajag zināt šķirkļa lemmu.
	 * @return	neapstrādātā (pāri palikusī rindas daļa).
	 */
	protected String extractSubsenses(
			MLVVSense sense, String linePart, String lemma)
	{
		if (linePart.contains("<lines/>"))
		{
			String[] subsensesParts = linePart.substring(
					linePart.indexOf("<lines/>") + "<lines/>".length())
					.trim().split("<lines/>");
			if (sense.subsenses == null) sense.subsenses = new LinkedList<>();
			for (String subP : subsensesParts)
			{
				MLVVSense ss = SenseParser.me().parse(subP, lemma);
				if (ss != null) sense.subsenses.add(ss);
			}
			linePart = linePart.substring(0, linePart.indexOf("<lines/>"));
		}
		return linePart;
	}

	/**
	 * No apstrādājamās rindiņas sākuma nogriež un izparsē nozīmes gramatiku.
	 * @param linePart	apstrādājamā rindiņa (rindiņas daļa).
	 * @param lemma		reizēm vajag zināt šķirkļa lemmu.
	 * @return	neapstrādātā (pāri palikusī rindas daļa).
	 */
	protected String extractBeginGrammar (
			MLVVSense sense, String linePart, String lemma)
	{
		boolean star = false;
		if (linePart.startsWith("*"))
		{
			star = true;
			linePart = linePart.substring(1).trim();
		}
		if (linePart.matches("(<i>((?!=</i>).*)</i>\\s*)?<b>.*")) // Šķirklī "abhāzi", "ārieši".
		{
			Pattern headpart = Pattern.compile(
					"((?:(?:[,;]?\\s*(?:<i>)?\\s*arī\\s*(?:</i>)?\\s*)?<b>.*?</b>[,.;]?|<i>.*?</i>[,.;]?|\\[.*?\\][,.;]?|(?:[-,][^.<]*\\.?))\\s*)(.*)");
			//((?: cita lemma    | gram. kursīvā  | [izruna]      | "galotne"       ) atstarpe) (pārējais)
			Matcher headMatcher = headpart.matcher(linePart);
			String header = "";
			while (headMatcher.matches())
			{
				header = header + headMatcher.group(1);
				linePart = headMatcher.group(2);
				headMatcher = headpart.matcher(linePart);
			}
			String preHeader = null;
			String realHeder = header;
			int bIndex =  header.indexOf("<b>");
			if (bIndex > 0)
			{
				preHeader = header.substring(0, bIndex);
				realHeder = header.substring(bIndex);
			}
			MLVVGram resGram = GramParser.me().parse(realHeder);
			if (preHeader != null && preHeader.trim().length() > 0)
			{
				MLVVGram other = MLVVElementFactory.me().getNewGram();
				other.reinitialize(preHeader);
				resGram.addTextsBefore(other, true);
			}
			sense.grammar = resGram;
		}
		else if (linePart.startsWith("<i>"))
		{
			if (linePart.contains("</i>"))
			{
				Pattern gramPat = Pattern.compile(
						"(<i>.*?</i>" // gramatika kursīvā
								// Dažādas citas lietas, kas var gramatikai pa vidu gadīties:
								+"(?:"
								+ "(?:\\s*:|(?<=:</i>))\\s<u>.*?</u>!?(?:\\s\\[[^\\]]+\\])?(?:,\\s?(?:<i>\\s?(?:retāk|arī)\\s?</i>\\s?)?<u>.*?</u>!?(?:\\s\\[[^\\]]+\\])?)*[.;,]*"
								//                   pasvītrota forma( [izruna])?         , atkārtota forma [izruna]?          pieturz.?
								+ "|:?(?:\\s*-\\p{Ll}+[,;])*\\s*<i>.*?</i>[.,;]?" // vai vēl gramatika (iespējams, ar galotnēm, sk. gnīda, aunapiere)
								//+ "|::\\s*<i>.*?</i>[.,;]?" //vai ar kolu atdalīta gramatika)
								+")*"
								+ ")\\s*(.*)"); // atstarpe un pārējais aiz gramatikas
				Matcher gramMatch = gramPat.matcher(linePart);
				if (gramMatch.matches())
				{
					sense.grammar = GramParser.me().parse(gramMatch.group(1));
					if (sense.grammar.freeText != null && !sense.grammar.freeText.isEmpty())
						System.out.printf(
								"No fragmenta \"%s\" sanāk nozīmes gramatika ar locījumiem \"%s\" (šķirklī %s)\n",
								linePart, sense.grammar.freeText, lemma);
					linePart = gramMatch.group(2);
				}
				else
					System.out.printf(
							"No fragmenta \"%s\" neizdodas atdalīt gramatiku (šķirklī %s)\n",
							linePart, lemma);
			}
			else
			{
				System.out.printf("Nesapārots i tags nozīmē \"%s\"", linePart);
				MLVVGram tmp = MLVVElementFactory.me().getNewGram();
				tmp.reinitialize(linePart.substring(3));
				sense.grammar = tmp;
				linePart = "";
			}
		}
		if (star)
		{
			if (sense.grammar != null)((MLVVGram)sense.grammar).addStar();
			else
			{
				MLVVGram tmp = MLVVElementFactory.me().getNewGram();
				tmp.reinitialize("*");
				sense.grammar = tmp;
			}
		}
		return linePart;
	}


	/**
	 * No apstrādājamās rindiņas sākuma nogriež un izparsē nozīmes skaidrojumu
	 * (glosu).
	 * @param linePart	apstrādājamā rindiņa (rindiņas daļa).
	 * @return	neapstrādātā (pāri palikusī rindas daļa).
	 */
	protected String extractGloss(MLVVSense sense, String linePart)
	{
		// Noskaidro, vai pēc glosas ir piemēri.
		int nextCircle = Finders.getAnyCircleIndex(linePart);
		int nextItalic = linePart.indexOf(". <i>");
		int cut = -1;
		if (nextItalic > -1 && nextItalic < nextCircle) cut = nextItalic + 1;
		else if (nextCircle > -1) cut = nextCircle;
		else if (nextItalic > -1) cut = nextItalic + 1;
		// Ja tālāk būs piemēri.
		if (cut > -1)
		{
			sense.gloss = new LinkedList<>();
			sense.gloss.add(GlossParser.me().parse(linePart.substring(0, cut).trim()));
			linePart = linePart.substring(cut).trim();
		}
		// Ja piemēru nav.
		else
		{
			sense.gloss = new LinkedList<>();
			sense.gloss.add(GlossParser.me().parse(linePart.trim()));
			linePart = "";
		}
		return linePart;
	}

}
