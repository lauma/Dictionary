package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.struct.Sense;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pašlaik te nav būtiski paplašināta Sense funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class MLVVSense extends Sense
{
	/**
	 * Metode, kas izgūst nozīmi vai nozīmes niansi no dotās simbolu virknes.
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo nozīmi un
	 *                      neko citu
	 * @param lemma			šķirkļavārds šķirklim, kurā šī nozīme atrodas (kļūdu
	 *                      paziņojumiem)
	 * @return	izgūtā nozīme vai null
	 */
	public static MLVVSense parse(String linePart, String lemma)
	{
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		MLVVSense res = new MLVVSense();

		// Nocērp un apstrādā beigas, lai nemaisās: nozīmes nianses
		linePart = res.extractSubsenses(linePart, lemma);

		// Nocērp no sākuma saprotamo: nozīmes gramatikau
		linePart = res.extractBeginGrammar(linePart, lemma);

		// Tagad vajadzētu sekot definīcijai.
		linePart = res.extractGloss(linePart);

		// Piemēru analīze.
		LinkedList<MLVVPhrase> samples = MLVVPhrase.parseAllPhrases(linePart, lemma);
		if (samples != null && samples.size() > 0)
			res.examples = new LinkedList<>(samples);
		return res;
	}

	/**
	 * No apstrādājamās rindiņas nogriež un izparsē ar "<lines/>" atdalītās
	 * apakšnozīmes.
	 * @param linePart	apstrādājamā rindiņa (rindiņas daļa).
	 * @param lemma		reizēm vajag zināt šķirkļa lemmu.
	 * @return	neapstrādātā (pāri palikusī rindas daļa).
	 */
	protected String extractSubsenses(String linePart, String lemma)
	{
		if (linePart.contains("<lines/>"))
		{
			String[] subsensesParts = linePart.substring(
					linePart.indexOf("<lines/>") + "<lines/>".length())
					.trim().split("<lines/>");
			if (subsenses == null) subsenses = new LinkedList<>();
			for (String subP : subsensesParts)
			{
				MLVVSense ss = parse(subP, lemma);
				if (ss != null) subsenses.add(ss);
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
	protected String extractBeginGrammar (String linePart, String lemma)
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
			MLVVGram resGram = MLVVGram.parse(realHeder);
			if (preHeader != null && preHeader.trim().length() > 0)
			{
				MLVVGram other = new MLVVGram(preHeader);
				resGram.addTextsBefore(other, true);
			}
			grammar = resGram;
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
					grammar = MLVVGram.parse(gramMatch.group(1));
					if (grammar.freeText != null && !grammar.freeText.isEmpty())
						System.out.printf(
								"No fragmenta \"%s\" sanāk nozīmes gramatika ar locījumiem \"%s\" (šķirklī %s)\n",
								linePart, grammar.freeText, lemma);
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
				grammar = new MLVVGram(linePart.substring(3));
				linePart = "";
			}
		}
		if (star)
		{
			if (grammar != null)((MLVVGram)grammar).addStar();
			else grammar = new MLVVGram("*");
		}
		return linePart;
	}


	/**
	 * No apstrādājamās rindiņas sākuma nogriež un izparsē nozīmes skaidrojumu
	 * (glosu).
	 * @param linePart	apstrādājamā rindiņa (rindiņas daļa).
	 * @return	neapstrādātā (pāri palikusī rindas daļa).
	 */
	protected String extractGloss(String linePart)
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
			gloss = MLVVGloss.parse(linePart.substring(0, cut).trim());
			linePart = linePart.substring(cut).trim();
		}
		// Ja piemēru nav.
		else
		{
			gloss = MLVVGloss.parse(linePart.trim());
			linePart = "";
		}
		return linePart;
	}

}
