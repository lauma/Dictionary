package lv.ailab.tezaurs.analyzer.gramlogic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Rule for patterns starting with "parasti 3.pers.,".
 */
public class ThirdPersVerbRule extends SimpleRule
{

    /**

     */
    public ThirdPersVerbRule(String pattern, String lemmaEnding, int paradigmId,
            Set<String> positiveFlags, Set<String> alwaysFlags)
    {
        super("parasti 3. pers., " + pattern, lemmaEnding, paradigmId,
                Collections.unmodifiableSet(new HashSet<String>() {{
                        add("Darbības vārds");
                        if (positiveFlags != null) addAll(positiveFlags); }}),
                Collections.unmodifiableSet(new HashSet<String>() {{
                        add("Parasti 3. personā");
                        if (alwaysFlags != null) addAll(alwaysFlags); }}));
    }

    /**
     * Constructor method for convenience - make ThirdPersVerbRule if flags are
     * given in arrays, not sets.
     * @param patternText   pattern text without "parasti 3.pers.," part.
     * @param lemmaEnding   required ending for the lemma to apply this rule
     * @param paradigmId	paradigm ID to set if rule matched
     * @param positiveFlags	flags to set if rule patternText and lemma ending
     * 						matched ("Darbības vārds" is added automatically).
     * @param alwaysFlags	flags to set if rule patternText matched ("Parasti
     *                      3. personā" is added automatically).
     * @return	new ThirdPersVerbRule
     */
    public static ThirdPersVerbRule of(String patternText, String lemmaEnding,
            int paradigmId, String[] positiveFlags, String[] alwaysFlags)
    {
        return new ThirdPersVerbRule(patternText, lemmaEnding, paradigmId,
                positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
                alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
    }

    /**
     * Create ThirdPersVerbRule for 1st conjugation direct verbs without
     * parallel forms or infinitive homoforms.
     * @param pattern	    part of the grammar string containing endings for
     * 						3rd parson in present and past (without "parasti
     * 						3.pers.," part)
     * @param lemmaEnd		required ending for the lemma to apply this rule
     * @return ThirdPersVerbRule with Paradigm 15 and flag about conjugating.
     * TODO add IDs from lexicon
     */
    public static ThirdPersVerbRule firstConjDir(
            String pattern, String lemmaEnd)
    {
        return ThirdPersVerbRule.of(pattern, lemmaEnd,
                15, new String[]{"Locīt kā \"" + lemmaEnd + "\""}, null);
    }

    /**
     * Create ThirdPersVerbRule for 2nd conjugation direct verbs without
     * parallel forms.
     * @param pattern	    part of the grammar string containing endings for
     * 						3rd parson in present and past (without "parasti
     * 						3.pers.," part)
     * @param lemmaEnd		required ending for the lemma to apply this rule
     * @return ThirdPersVerbRule with Paradigm 16
     */
    public static ThirdPersVerbRule secondConjDir(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 16, null, null);
    }

    /**
     * Create ThirdPersVerbRule for 3rd conjugation direct verbs without
     * parallel forms.
     * @param pattern	    part of the grammar string containing endings for
     * 						3rd parson in present and past (without "parasti
     * 						3.pers.," part)
     * @param lemmaEnd		required ending for the lemma to apply this rule
     * @return ThirdPersVerbRule with Paradigm 17
     */
    public static ThirdPersVerbRule thirdConjDir(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 17, null, null);
    }

    /**
     * Create ThirdPersVerbRule for 1st conjugation reflexive verbs without
     * parallel forms or infinitive homoforms.
     * @param pattern	    part of the grammar string containing endings for
     * 						3rd parson in present and past (without "parasti
     * 						3.pers.," part)
     * @param lemmaEnd		required ending for the lemma to apply this rule
     * @return ThirdPersVerbRule with Paradigm 18 and flag about conjugating.
     * TODO add IDs from lexicon
     */
    public static ThirdPersVerbRule firstConjRefl(
            String pattern, String lemmaEnd)
    {
        return ThirdPersVerbRule.of(pattern, lemmaEnd,
                18, new String[] {"Locīt kā \""+ lemmaEnd + "\""}, null);
    }

    /**
     * Create ThirdPersVerbRule for 2nd conjugation reflexive verbs without
     * parallel forms.
     * @param pattern	    part of the grammar string containing endings for
     * 						3rd parson in present and past (without "parasti
     * 						3.pers.," part)
     * @param lemmaEnd		required ending for the lemma to apply this rule
     * @return ThirdPersVerbRule with Paradigm 19
     */
    public static ThirdPersVerbRule secondConjRefl(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 19, null, null);
    }

    /**
     * Create ThirdPersVerbRule for 3rd conjugation reflexive verbs without
     * parallel forms.
     * @param pattern	    part of the grammar string containing endings for
     * 						3rd parson in present and past (without "parasti
     * 						3.pers.," part)
     * @param lemmaEnd		required ending for the lemma to apply this rule
     * @return ThirdPersVerbRule with Paradigm 20
     */
    public static ThirdPersVerbRule thirdConjRefl(
            String pattern, String lemmaEnd)
    {
        return new ThirdPersVerbRule(pattern, lemmaEnd, 20, null, null);
    }

}
