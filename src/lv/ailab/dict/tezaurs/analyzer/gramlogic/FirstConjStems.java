package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.utils.Tuple;

import java.util.*;

/**
 * Darbošanās ar 1. konjugācijas celmiem, kas katra 1. konjugācijas verba
 * apstrādei ir vajadzīgi, lai nodrošinātu korektu integrāciju ar morfoloģisko
 * analizatoru. Katru verbu raksturo viens vai vairāki celmi nenoteiksmei,
 * tagadnei un pagātnei.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public class FirstConjStems
{
	/**
	 * Nenoteiksmes celmi - katrā likuma trigerošanās reizē lietos tikai VIENU -
	 * to, kurš tajā reizē atbildīs lemmai.
	 */
	protected List<String> infinityStems;
	/**
	 * Tagadnes celmi, kas izsecināmi no attiecīgā gramatikas šablona - katrā
	 * likuma trigerošanās riezē pielietos VISUS.
	 */
	protected List<String> presentStems;
	/**
	 * Pagātnes celmi, kas izsecināmi no attiecīgā gramatikas šablona - katrā
	 * likuma trigerošanās riezē pielietos VISUS.
	 */
	protected List<String> pastStems;

	/**
	 * Izveido objektu no jau gataviem celmu komplektiem.
	 * @param infinityStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      vairākām nenoteiksmēm
	 * @param presentStems	tagadnes celmi
	 * @param pastStems		pagātnes celmi
	 */
	public FirstConjStems(
			List<String> infinityStems, List<String> presentStems, List<String> pastStems)
	{
		this.infinityStems = Collections.unmodifiableList(infinityStems);
		this.presentStems = Collections.unmodifiableList(presentStems);
		this.pastStems = Collections.unmodifiableList(pastStems);
	}

	/**
	 * Konstruktors objektam no jau gataviem celmu komplektiem.
	 * @param infinityStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      vairākām nenoteiksmēm
	 * @param presentStems	tagadnes celmi
	 * @param pastStems		pagātnes celmi
	 * @return atbilstoši aizpildīts celmu objekts
	 */
	public static FirstConjStems of (
			String[] infinityStems, String[] presentStems, String[] pastStems)
	{
		return new FirstConjStems(
				infinityStems == null ? null : Arrays.asList(infinityStems),
				presentStems == null ? null : Arrays.asList(presentStems),
				pastStems == null ? null : Arrays.asList(pastStems));
	}

	/**
	 * Konstruktors vienkāršas celmu kopas izveidei no vienkārša gramatikas
	 * šablona un vienas nenoteiksmes.
	 * @param patternEnd	verba gramatikas paterna otrās (vienmer esošā) daļa
	 *                      - no tās izvelk tagadnes un pagātnes celmus,
	 *                      pieņemot, ka nav paralēlformu.
	 * @param lemmaEnd		lemmas beigas - no tā izvelk nenoteiksmes celmu.
	 * @return	celmu komplekts, kam katra veida celms ir viens.
	 */
	public static FirstConjStems singlePP (String patternEnd, String lemmaEnd)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjStems.of(
				new String[] {extractInfinityStemSimple(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Konstruktors celmu kopas izveidei no vienkārša gramatikas
	 * šablona un vairākām nenoteiksmēm.
	 * @param patternEnd	verba gramatikas paterna otrās (vienmer esošā) daļa
	 *                      - no tās izvelk tagadnes un pagātnes celmus,
	 *                      pieņemot, ka nav paralēlformu.
	 * @param lemmaEnds		iespējamās lemmas beigas - no tām izvelk
	 *                      nenoteiksmes celmu.
	 * @return	celmu komplekts, kam ir viena tagadne, viena pagātne, vairākas
	 * 			nenoteiksmes.
	 */
	public static FirstConjStems singlePP (String patternEnd, String[] lemmaEnds)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjStems.of(
				extractInfinityStemsParallel(lemmaEnds),
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Konstruktors celmu kopas izveidei no gramatikas šablona ar paralēlformām
	 * un vienas nenoteiksmes.
	 * @param patternText	verba gramatikas paterna otrās (vienmer esošā) daļa
	 *                      - no tās izvelk tagadnes un pagātnes celmus,
	 *                      pieņemot, ka nav paralēlformu.
	 * @param lemmaEnd		lemmas beigas - no tā izvelk nenoteiksmes celmu.
	 * @return	celmu komplekts, kam ir vairākas tagadnes vai pagātnes un
	 * 			viena nenoteiksme.
	 */
	public static FirstConjStems parallelPP (String patternText, String lemmaEnd)
	{
		Tuple<ArrayList<String>, ArrayList<String>> stems = extractPPStemsParallel(patternText);
		return new FirstConjStems (
				new ArrayList<String>() {{add(extractInfinityStemSimple(lemmaEnd));}},
				stems.first, stems.second);
	}

	/**
	 * Konstruktors celmu kopas izveidei no gramatikas šablona ar paralēlformām
	 * un vairākām nenoteiksmēm.
	 * @param patternText	verba gramatikas paterna otrās (vienmer esošā) daļa
	 *                      - no tās izvelk tagadnes un pagātnes celmus,
	 *                      pieņemot, ka nav paralēlformu.
	 * @param lemmaEnds		iespējamās lemmas beigas - no tām izvelk
	 *                      nenoteiksmes celmu.
	 * @return	celmu komplekts, kam ir vairākas tagadnes vai pagātnes un
	 * 			vairākas nenoteiksme.
	 */
	public static FirstConjStems parallelPP (String patternText, String[] lemmaEnds)
	{
		Tuple<ArrayList<String>, ArrayList<String>> stems = extractPPStemsParallel(patternText);
		return new FirstConjStems (
				Arrays.asList(extractInfinityStemsParallel(lemmaEnds)),
				stems.first, stems.second);
	}

	/**
	 * Metode, kas izvelk nenoteiksmes celmu no virknes ar ko jābeidzas lemmai.
	 */
	public static String extractInfinityStemSimple(String lemmaEnd)
	{
		String result = lemmaEnd;
		if (result.startsWith(".*")) result = result.substring(2);
		if (lemmaEnd.endsWith("t")) result = result.substring(0, result.length()-1);
		else if (lemmaEnd.endsWith("ties")) result = result.substring(0, result.length() - 4);
		else System.err.printf("Problēma, veidojot nenoteiksmes celmu verba likumam \"%s\"\n", lemmaEnd);
		return result;
	}

	/**
	 * Metode, kas izvelk nenoteiksmes celmus no katras virknes ar ko jābeidzas
	 * lemmai.
	 */
	public static String[] extractInfinityStemsParallel(String[] lemmaEnds)
	{
		return Arrays.asList(lemmaEnds).stream().map(s -> extractInfinityStemSimple(s))
				.toArray(size -> new String[size]);
	}

	/**
	 * Metode, kas no verba gramatikas paterna otrās (vienmer esošās) daļas
	 * izvelk tagadnes un pagātnes celmus, pieņemot, ka nav paralēlformu.
	 * @return Pārītis, kur pirmais celms ir tagadnes un otrais - pagātnes.
	 * TODO - vai šis ir extractPPStemsParallel() speciālgadījumam?
	 */
	public static Tuple<String,String> extractPPStemsSimple(String patternEnd)
	{
		String[] parts = patternEnd.split("[,;]");
		if (parts.length < 2)
			System.err.printf("Neizdodas izveidot tagadnes un pagātnes celmus verba šablonam \"%s\"\n", patternEnd);

		String thirdPers = parts[0].trim();
		if (thirdPers.startsWith("-")) thirdPers = thirdPers.substring(1);
		if (thirdPers.endsWith("as")) thirdPers = thirdPers.substring(0, thirdPers.length()-2);

		String past = parts[1].trim();
		if (past.startsWith("pag.")) past = past.substring(4).trim();
		if (past.startsWith("-")) past = past.substring(1).trim();
		if (past.endsWith("u") || past.endsWith("a")) past = past.substring(0, past.length()-1);
		else if (past.endsWith("os") || past.endsWith("ās")) past = past.substring(0, past.length()-2);
		else System.err.printf("Problēma, veidojot pagātnes celmu verba likumam \"%s\"\n", patternEnd);

		return Tuple.of(thirdPers, past);
	}

	/**
	 * Metode, kas no verba gramatikas paterna visām personai izvelk tagadnes
	 * un pagātnes celmus, pieņemot, ka likums ir kādā no formām
	 * -auju, -auj, -auj, arī -aunu, -aun, -aun, pag. -āvu
	 * -gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu
	 * -jaušu, -jaut, -jauš, pag. -jautu, arī -jaužu, -jaud, -jauž, pag. -jaudu
	 * -gulstos, -gulsties, -gulstas, arī -guļos, -gulies, -guļas, pag. -gūlos, arī -gulos
	 * -?tagcelms, arī -?tagcelms[,;] pag. -?pagcelms, arī -?pagcelms
	 * -?tagcelms, pag. -?pagcelms, arī -?tagcelms[,;], pag. -?pagcelms
	 * @return Pārītis, kur pirmais saraksts ir tagadnes celmu sarakst un otrais
	 * - pagātnes.
	 */
	public static Tuple<ArrayList<String>,ArrayList<String>> extractPPStemsParallel(
			String pattern)
	{
		ArrayList<String> presentTexts = new ArrayList<>();
		ArrayList<String> pastTexts = new ArrayList<>();

		String[] parts = pattern.split(", (arī|retāk|pag\\.) ");
		if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			pastTexts.add(parts[1]);
			presentTexts.add(parts[2]);
			pastTexts.add(parts[3]);
		}
		else if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			presentTexts.add(parts[1]);
			pastTexts.add(parts[2]);
			pastTexts.add(parts[3]);
		}
		else if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			presentTexts.add(parts[1]);
			pastTexts.add(parts[2]);
		}
		else if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			pastTexts.add(parts[1]);
			pastTexts.add(parts[2]);
		}
		else System.err.printf(
					"Nespēj izgūt tagadnes un pagātnes celmus no šablona \"%s\"\n", pattern);

		ArrayList<String> presents = new ArrayList<>();
		ArrayList<String> pasts = new ArrayList<>();

		if (presentTexts.size() + pastTexts.size() == parts.length)
		{
			for (String present : presentTexts)
			{
				if (present.contains(","))
					present = present.substring(present.lastIndexOf(',') + 1).trim();
				if (present.startsWith("-")) present = present.substring(1);
				if (present.endsWith("as")) present = present.substring(0, present.length()-2);
				if (present.matches(".*[ ,\\-].*")) System.err.printf(
						"Problēma, veidojot tagadnes celmu verba likumam \"%s\"\n", pattern);
				else presents.add(present);
			}

			for (String past : pastTexts)
			{
				boolean good = true;
				past = past.trim();
				if (past.startsWith("-")) past = past.substring(1).trim();
				if (past.endsWith("u") || past.endsWith("a")) past = past.substring(0, past.length()-1);
				else if (past.endsWith("os") || past.endsWith("ās")) past = past.substring(0, past.length()-2);
				else
				{
					System.err.printf("Problēma, veidojot pagātnes celmu verba likumam \"%s\"\n", pattern);
					good = false;
				}
				if (good) pasts.add(past);
			}
		}
		else System.err.printf(
				"Nespēj izgūt tagadnes un pagātnes celmus no šablona \"%s\"\n", pattern);
		return Tuple.of(presents, pasts);
	}

	/**
	 * Pieņemot, ka vārds ir veiksmīgi atpazīts, pievieno celmus aprakstošos
	 * karodziņus.
	 * @param lemma 		atpazītais vārds
	 * @param flagCollector	kolekcija, kurā pielikt karodziņus
	 */
	public void addStemFlags(String lemma, Flags flagCollector)
	{
		String prefix = lemma;
		if (prefix.endsWith("t"))
			prefix = prefix.substring(0, prefix.length() - 1);
		else if (prefix.endsWith("ties"))
			prefix = prefix.substring(0, prefix.length() - 4);
		else System.err.printf(
					"Neizdodas noteikt prefiksu 1. konj. vārdam \"%s\"\n", lemma);
		String infinityStem = null;
		for (String stem : infinityStems)
		{
			if (prefix.endsWith(stem))
				infinityStem = stem;
		}
		if (infinityStem != null)
		{
			prefix = prefix.substring(0, prefix.length() - infinityStem.length());
			if (prefix.length() > 0)
				flagCollector.add(TKeys.VERB_PREFIX, prefix);
			flagCollector.add(TKeys.INFINITIVE_STEM, prefix + infinityStem);

			for (String stem : presentStems)
				flagCollector.add(TKeys.PRESENT_STEM, prefix + stem);
			for (String stem : pastStems)
				flagCollector.add(TKeys.PAST_STEM, prefix + stem);

		}
		else System.err.printf(
				"Neizdodas noteikt prefiksu 1. konj. vārdam \"%s\" ar nenoteiksmes celmu \"%s\"\n",
				lemma, infinityStems.stream().reduce((s1, s2)-> s1 + "\", \"" + s2).orElse(""));
	}
}
