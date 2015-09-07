package lv.ailab.tezaurs.analyzer.gramlogic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums, kas apvieno divus SimpleRule - divus sdarbības vārdiem raksturīgos
 * gadījumus: vienu visām personām, otru - tikai trešajai.
 * Piemēram, "-brāžu, -brāz, -brāž, pag. -brāzu" un
 * "parasti 3. pers., -brāž, pag. -brāzu".
 * @author Lauma
 *
 */
public class VerbRule implements Rule
{
	protected SimpleRule allPersonRule;
	protected ThirdPersVerbRule thirdPersonRule;
	
	/**
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public VerbRule(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Set<String> positiveFlags, Set<String> alwaysFlags)
	{
		HashSet<String> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add("Darbības vārds");
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<String> alwaysFlagsSet = alwaysFlags == null ? null : new HashSet<>(alwaysFlags);

		String begin = patternBegin.trim();
		String end = patternEnd.trim();
		String allPersonPattern = begin + " " + end;
		String thirdPersonPattern;
		if (end.endsWith("u"))
			thirdPersonPattern = end.substring(0, end.length()-1) + "a";
		else if (end.endsWith("os"))
			thirdPersonPattern = end.substring(0, end.length()-2) + "ās";
		else
		{
			System.err.printf("Neizdevās izveidot \"parasti 3. pers.\" likumu gramatikas šablonam \"%s\"\n", allPersonPattern);
			thirdPersonPattern = allPersonPattern;
		}
		allPersonRule = new SimpleRule(allPersonPattern,
				".*" + lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
		thirdPersonRule = new ThirdPersVerbRule(thirdPersonPattern,
				lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
	}

	/**
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public static VerbRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			String[] positiveFlags, String[] alwaysFlags)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, paradigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Izveido VerbRule 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām un nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbRule ar paradigmu 15 un karodziņu par locīšanu
	 * TODO add IDs from lexicon
	 */
	public static VerbRule firstConjDir(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return VerbRule.of(patternBegin, patternEnd, lemmaEnd,
				15, new String[] {"Locīt kā \""+ lemmaEnd + "\""}, null);
	}

	/**
	 * Izveido VerbRule 2. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbRule ar paradigmu 16
	 */
	public static VerbRule secondConjDir(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 16, null, null);
	}

/*	/**
	 * Izveido VerbRule 3. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbRule ar paradigmu 17
	 */
/*	public static VerbRule thirdConjDir(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 17, null, null);
	}*/

	/**
	 * Izveido VerbRule 3. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return VerbRule ar paradigmu 17
	 */
	public static VerbRule thirdConjDir(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		if (presentChange)
			return VerbRule.of(patternBegin, patternEnd, lemmaEnd, 17,
					new String[] {"Tagadnes mija ir"}, null);
		else
			return VerbRule.of(patternBegin, patternEnd, lemmaEnd, 17,
					new String[] {"Tagadnes mijas nav"}, null);

	}

	/**
	 * Izveido VerbRule 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām un nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbRule ar paradigmu 18 un karodziņu par locīšanu
	 * TODO add IDs from lexicon
	 */
	public static VerbRule firstConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return VerbRule.of(patternBegin, patternEnd, lemmaEnd,
				18, new String[] {"Locīt kā \""+ lemmaEnd + "\""}, null);
	}

	/**
	 * Izveido VerbRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbRule ar paradigmu 19
	 */
	public static VerbRule secondConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 19, null, null);
	}

/*	/**
	 * Izveido VerbRule 3. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbRule ar paradigmu 20
	 */
/*	public static VerbRule thirdConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 20, null, null);
	}*/

	/**
	 * Izveido VerbRule 3. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return VerbRule ar paradigmu 20
	 */
	public static VerbRule thirdConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		if (presentChange)
			return VerbRule.of(patternBegin, patternEnd, lemmaEnd, 20,
					new String[] {"Tagadnes mija ir"}, null);
		else
			return VerbRule.of(patternBegin, patternEnd, lemmaEnd, 20,
					new String[] {"Tagadnes mijas nav"}, null);

	}

	/**
	 * Piemērot likumu bez papildus maģijas.
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @return  jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
	 *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	public int applyDirect (
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		int newBegin = thirdPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1)
			newBegin = allPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		return newBegin;
	}

	/**
	 * Piemērot likumu tā, ka patternText defises ir neobligātas.
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @return  jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
	 *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	public int applyOptHyphens(
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		int newBegin = thirdPersonRule.applyOptHyphens(gramText, lemma,
				paradigmCollector, flagCollector);
		if (newBegin == -1)
			newBegin = allPersonRule.applyOptHyphens(gramText, lemma,
					paradigmCollector, flagCollector);
		return newBegin;		
	}


}
