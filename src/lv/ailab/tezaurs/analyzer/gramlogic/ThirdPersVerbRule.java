package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.struct.Flags;
import lv.ailab.tezaurs.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums šabloniem, kas sākas ar "parasti 3. pers.," vai  "tikai 3. pers.,"
 */
public class ThirdPersVerbRule implements Rule
{
    protected BaseRule thirdPersOnly;
    protected BaseRule thirdPersUsually;

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
            Set<Tuple<Keys,String>> positiveFlags, Set<Tuple<Keys,String>> alwaysFlags)
    {
        thirdPersUsually = BaseRule.simple(
                "parasti 3. pers., " + patternText, ".*" + lemmaEnding, paradigmId,
                new HashSet<Tuple<Keys, String>>()
                {{
                        add(Features.POS__VERB);
                        if (positiveFlags != null) addAll(positiveFlags);
                    }},
                new HashSet<Tuple<Keys, String>>()
                {{
                        add(Features.USUALLY_USED__THIRD_PERS);
                        if (alwaysFlags != null) addAll(alwaysFlags);
                    }});
        thirdPersOnly = BaseRule.simple(
                "tikai 3. pers., " + patternText, ".*" + lemmaEnding, paradigmId,
                new HashSet<Tuple<Keys, String>>()
                {{
                        add(Features.POS__VERB);
                        if (positiveFlags != null) addAll(positiveFlags);
                    }},
                new HashSet<Tuple<Keys, String>>()
                {{
                        add(Features.USED_ONLY__THIRD_PERS);
                        if (alwaysFlags != null) addAll(alwaysFlags);
                    }});
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
            int paradigmId, Tuple<Keys,String>[] positiveFlags, Tuple<Keys,String>[] alwaysFlags)
    {
        return new ThirdPersVerbRule(patternText, lemmaEnding, paradigmId,
                positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
                alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
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
            HashSet<Integer> paradigmCollector, Flags flagCollector)
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
            HashSet<Integer> paradigmCollector, Flags flagCollector)
    {
        int newBegin = thirdPersUsually.applyOptHyphens(
                gramText, lemma, paradigmCollector, flagCollector);
        if (newBegin == -1)
            newBegin = thirdPersOnly.applyOptHyphens(
                    gramText, lemma, paradigmCollector, flagCollector);
        return newBegin;
    }
}
