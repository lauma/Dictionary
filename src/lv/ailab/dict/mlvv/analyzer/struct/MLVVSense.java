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
	// TODO: šis ir uzblīdis, vajag sacirst gabaliņos.
	/**
	 * Metode, kas izgūst nozīmi vai nozīmes niansi no dotās simbolu virknes.
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo nozīmi un
	 *                      neko citu
	 * @param lemma			šķirkļavārds šķirklim, kurā šī nozīme atrodas (kļūdu
	 *                      paziņojumiem)
	 * @return	izgūtā nozīme vai null
	 */
	public static MLVVSense extract(String linePart, String lemma)
	{
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		MLVVSense res = new MLVVSense();

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
				MLVVSense ss = extract(subP, lemma);
				if (ss != null) res.subsenses.add(ss);
			}
			linePart = linePart.substring(0, linePart.indexOf("<lines/>"));
		}

		// Nocērp no sākuma saprotamo.
		// Nozīmes gramatikas apstrāde

		if (linePart.matches("(<i>((?!=</i>).*)</i>\\s*)?<b>.*")) // Šķirklī "abhāzi", "ārieši".
		{
			Pattern headpart = Pattern.compile(
					"((?:(?:[,;]?\\s*arī\\s*)?<b>.*?</b>[,.;]?|<i>.*?</i>[,.;]?|\\[.*?\\][,.;]?|(?:[-,][^.<]*\\.?))\\s*)(.*)");
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
			MLVVGram resGram = MLVVGram.extract(realHeder);
			if (preHeader != null && preHeader.trim().length() > 0)
			{
				MLVVGram other = new MLVVGram(preHeader);
				resGram.addTextsBefore(other, true);
			}
			res.grammar = resGram;
		}
		else if (linePart.startsWith("<i>"))
		{
			if (linePart.contains("</i>"))
			{
				Matcher gramMatch = Pattern.compile(
						"(<i>.*?</i>(?:(?::|(?<=:</i>))\\s<u>.*?</u>!?(?:\\s\\[[^\\]]+\\])?(?:,\\s<u>.*?</u>!?(?:\\s\\[[^\\]]+\\])?)*[.;,]*(?:\\s*<i>.*?</i>[.,;]?)*|(?::\\s*<i>.*?</i>[.,;]?)?)*)\\s*(.*)")
						// (gramatika kursīvā (: pasvītrota forma( [izruna])?              (, atkārtota forma( [izruna])?)*      pieturz.? (vēl gramatika)*         |ar kolu atdalīta gramatika)*) atstarpe (pārējais)
						.matcher(linePart);
				if (gramMatch.matches())
				{
					res.grammar = MLVVGram.extract(gramMatch.group(1));
					linePart = gramMatch.group(2);
				}
				else
					System.out.printf(
							"No fragmenta \"%s\" neizdodas atdalīt gramatiku (šķirklī %s)",
							linePart, lemma);
			}
			else
			{
				System.out.printf("Nesapārots i tags nozīmē \"%s\"", linePart);
				res.grammar = new MLVVGram(linePart.substring(3));
				linePart = "";
			}
		}

		// Tagad vajadzētu sekot definīcijai.
		// Ja tālāk būs piemēri
		int nextCircle = Finders.getAnyCircleIndex(linePart);
		int nextItalic = linePart.indexOf(". <i>");
		int cut = -1;
		if (nextItalic > -1 && nextItalic < nextCircle) cut = nextItalic + 1;
		else if (nextCircle > -1) cut = nextCircle;
		else if (nextItalic > -1) cut = nextItalic + 1;
		if (cut > -1)
		{
			res.gloss = MLVVGloss.extract(linePart.substring(0, cut).trim());
			linePart = linePart.substring(cut).trim();
		}
		// Ja piemēru nav.
		else
		{
			res.gloss = MLVVGloss.extract(linePart.trim());
			linePart = "";
		}

		// Piemēru analīze.
		LinkedList<MLVVPhrase> samples = MLVVPhrase.extractPhrases(linePart, lemma);
		if (samples != null && samples.size() > 0)
			res.examples = new LinkedList<>(samples);
		return res;
	}

}
