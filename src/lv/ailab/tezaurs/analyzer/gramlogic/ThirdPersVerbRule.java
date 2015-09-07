package lv.ailab.tezaurs.analyzer.gramlogic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums šabloniem, kas sākas ar "parasti 3. pers.," vai  "tikai 3. pers.,"
 */
public class ThirdPersVerbRule implements Rule
{
    protected SimpleRule thirdPersOnly;
    protected SimpleRule thirdPersUsually;

    /**
     * @param patternText   gramatikas šablons bez "parasti/tikai 3. pers.,".
     * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
     *                      varētu piemērot
     * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
     *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti 3. personā" pievieno
	 *                      automātiski)
     */
    public ThirdPersVerbRule(String patternText, String lemmaEnding, int paradigmId,
            Set<String> positiveFlags, Set<String> alwaysFlags)
    {
        thirdPersUsually = new SimpleRule(
                "parasti 3. pers., " + patternText, ".*" + lemmaEnding, paradigmId,
                new HashSet<String>() {{
                        add("Darbības vārds");
                        if (positiveFlags != null) addAll(positiveFlags); }},
                new HashSet<String>() {{
                        add("Parasti 3. personā");
                        if (alwaysFlags != null) addAll(alwaysFlags); }});
        thirdPersOnly = new SimpleRule(
                "tikai 3. pers., " + patternText, ".*" + lemmaEnding, paradigmId,
                new HashSet<String>() {{
                    add("Darbības vārds");
                    if (positiveFlags != null) addAll(positiveFlags); }},
                new HashSet<String>() {{
                    add("Tikai 3. personā");
                    if (alwaysFlags != null) addAll(alwaysFlags); }});
    }

    /**
     * @param patternText   gramatikas šablons bez "parasti/tikai 3. pers.,".
     * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
     *                      varētu piemērot
     * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
     *                      likumam
     * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
     *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
     *                      pievieno automātiski)
     * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
     *                      likuma šablonam ("Parasti 3. personā" pievieno
	 *                      automātiski)
     */
    public static ThirdPersVerbRule of(String patternText, String lemmaEnding,
            int paradigmId, String[] positiveFlags, String[] alwaysFlags)
    {
        return new ThirdPersVerbRule(patternText, lemmaEnding, paradigmId,
                positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
                alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
    }

    /**
	 * Izveido ThirdPersVerbRule 1. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajāmc formām un nenoteiksmes homoformām.
	 * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 15 un karodziņu par locīšanu
	 * TODO add IDs from lexicon
     */
    public static ThirdPersVerbRule firstConjDir(
            String pattern, String lemmaEnd)
    {
        return ThirdPersVerbRule.of(pattern, lemmaEnd,
                15, new String[]{"Locīt kā \"" + lemmaEnd + "\""}, null);
    }

    /**
	 * Izveido ThirdPersVerbRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajāmc formām.
     * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
     * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
     * @return ThirdPersVerbRule ar paradigmu 16
     */
    public static ThirdPersVerbRule secondConjDir(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 16, null, null);
    }

/*    /**
	 * Izveido ThirdPersVerbRule 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajāmc formām.
	 * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 17
     */
/*    public static ThirdPersVerbRule thirdConjDir(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 17, null, null);
    }*/

	/**
	 * Izveido ThirdPersVerbRule 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajāmc formām.
	 * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return ThirdPersVerbRule ar paradigmu 17
	 */
	public static ThirdPersVerbRule thirdConjDir(
			String pattern, String lemmaEnd, boolean presentChange)
	{
		if (presentChange)
			return ThirdPersVerbRule.of(pattern, lemmaEnd, 17,
					new String[]{"Tagadnes mija ir"} , null);
		else
			return ThirdPersVerbRule.of(pattern, lemmaEnd, 17,
					new String[]{"Tagadnes mijas nav"} , null);
	}

    /**
	 * Izveido ThirdPersVerbRule 1. konjugācijas atgriezeniskajam darbības
	 * vārdam bez paralēlajāmc formām un nenoteiksmes homoformām.
	 * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņu par locīšanu
	 * TODO add IDs from lexicon
     */
    public static ThirdPersVerbRule firstConjRefl(
            String pattern, String lemmaEnd)
    {
        return ThirdPersVerbRule.of(pattern, lemmaEnd,
                18, new String[] {"Locīt kā \""+ lemmaEnd + "\""}, null);
    }

    /**
	 * Izveido ThirdPersVerbRule 2. konjugācijas atgriezeniskajam darbības
	 * vārdam bez paralēlajāmc formām.
	 * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 19
     */
    public static ThirdPersVerbRule secondConjRefl(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 19, null, null);
    }

/*    /**
	 * Izveido ThirdPersVerbRule 3. konjugācijas atgriezeniskajam darbības
	 * vārdam bez paralēlajāmc formām.
	 * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 20

     */
/*	public static ThirdPersVerbRule thirdConjRefl(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 20, null, null);
    }*/

	/**
	 * Izveido ThirdPersVerbRule 3. konjugācijas atgriezeniskajam darbības
	 * vārdam bez paralēlajāmc formām.
	 * @param pattern	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return ThirdPersVerbRule ar paradigmu 20

	 */
	public static ThirdPersVerbRule thirdConjRefl(
			String pattern, String lemmaEnd, boolean presentChange)
	{
		if (presentChange)
			return ThirdPersVerbRule.of(pattern, lemmaEnd, 20,
					new String[]{"Tagadnes mija ir"} , null);
		else
			return ThirdPersVerbRule.of(pattern, lemmaEnd, 20,
					new String[]{"Tagadnes mijas nav"} , null);
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
    @Override
    public int applyDirect(String gramText, String lemma,
            HashSet<Integer> paradigmCollector, HashSet<String> flagCollector)
    {
        int newBegin = thirdPersUsually.applyDirect(
                gramText, lemma, paradigmCollector, flagCollector);
        if (newBegin == -1)
            newBegin = thirdPersOnly.applyDirect(
                    gramText, lemma, paradigmCollector, flagCollector);
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
    @Override
    public int applyOptHyphens(String gramText, String lemma,
            HashSet<Integer> paradigmCollector, HashSet<String> flagCollector)
    {
        int newBegin = thirdPersUsually.applyOptHyphens(
                gramText, lemma, paradigmCollector, flagCollector);
        if (newBegin == -1)
            newBegin = thirdPersOnly.applyOptHyphens(
                    gramText, lemma, paradigmCollector, flagCollector);
        return newBegin;
    }
}
