package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.struct.constants.structrestrs.Type;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.tezaurs.struct.constants.structrestrs.TFrequency;
import lv.ailab.dict.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums šabloniem, kas sākas ar "parasti 3. pers.," vai  "tikai 3. pers.,"
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * @author Lauma
 */
public class ThirdPersVerbRule implements EndingRule
{
	/**
	 * Neeskepota teksta virkne, ar kuru grmatikai jāsākas, lai šis likums būtu
	 * piemērojams.
	 */
	protected final String patternText;

	/**
	 * Likums šablonam, kas sākas ar "tikai 3. pers.,".
	 */
    protected BaseRule thirdPersOnly;

	/**
	 * Likums šablonam, kas sākas ar "parasti 3. pers.,".
	 */
    protected BaseRule thirdPersUsually;

    /**
     * @param patternText   gramatikas šablons bez "parasti/tikai 3. pers.,".
     * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
     *                      varētu piemērot
     * @param paradigms		paradigma, ko lietot, ja konstatēta atbilstība šim
     *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param positiveRestrictions	ierobežojumi, ko uzstādīt, ja ir gan
	 *                              atbilstība likuma šablonam, gan lemmas
	 *                              nosacījumiem
	 * @param alwaysFlags			karodziņi, ko uzstādīt, ja ir konstatēta
	 *                              atbilstība likuma šablonam
	 * @param alwaysRestrictions	ierobežojumi, ko uzstādīt, ja ir konstatēta
	 *                              atbilstība likuma šablonam ("Parasti 3.
	 *                              personā" pievieno automātiski)
     */
    public ThirdPersVerbRule(
    		String patternText, String lemmaEnding, Set<Integer> paradigms,
            Set<Tuple<String,String>> positiveFlags, Set<StructRestrs.One> positiveRestrictions,
			Set<Tuple<String,String>> alwaysFlags, Set<StructRestrs.One> alwaysRestrictions)
    {
		this.patternText = patternText;
        thirdPersUsually = BaseRule.simple(
                "parasti 3. pers., " + patternText, ".*" + lemmaEnding, paradigms,
                new HashSet<Tuple<String, String>>()
                {{
                        add(TFeatures.POS__VERB);
                        if (positiveFlags != null) addAll(positiveFlags);
                }},
				alwaysFlags,
				new HashSet<StructRestrs.One>()
				{{
					add(StructRestrs.One.of(Type.IN_FORM, TFrequency.USUALLY, TFeatures.PERSON__3));
				}});
        thirdPersOnly = BaseRule.simple(
                "tikai 3. pers., " + patternText, ".*" + lemmaEnding, paradigms,
                new HashSet<Tuple<String, String>>()
                {{
                        add(TFeatures.POS__VERB);
                        if (positiveFlags != null) addAll(positiveFlags);
                }},
				alwaysFlags,
				new HashSet<StructRestrs.One>()
				{{
					add(StructRestrs.One.of(Type.IN_FORM, TFrequency.ONLY, TFeatures.PERSON__3));
				}});
    }

	/**
	 * Papildus konstruktors īsumam - gadījumam, kad ir tikai viena paradigma.
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
	public static ThirdPersVerbRule simple(String patternText, String lemmaEnding,
			int paradigmId,	Set<Tuple<String,String>> positiveFlags,
			Set<Tuple<String,String>> alwaysFlags)
	{
		return new ThirdPersVerbRule(
				patternText, lemmaEnding, new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags, null, alwaysFlags, null);
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
            int paradigmId, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags)
    {
        return new ThirdPersVerbRule(
        		patternText, lemmaEnding, new HashSet<Integer>() {{add(paradigmId);}},
                positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
                alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null);
    }

	/**
	 * @param patternText   gramatikas šablons bez "parasti/tikai 3. pers.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms	paradigmas, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti 3. personā" pievieno
	 *                      automātiski)
	 */
	public static ThirdPersVerbRule of(String patternText, String lemmaEnding,
			Integer[] paradigms, Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new ThirdPersVerbRule(
				patternText, lemmaEnding,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null);
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
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 *                          ja vismaz gramatika atbilst šim likumam
     * @return  jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
     *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
     */
    @Override
    public int applyDirect(
    		String gramText, String lemma, Set<Integer> paradigmCollector,
			Flags flagCollector, StructRestrs restrCollector)
    {
        int newBegin = thirdPersUsually.applyDirect(
                gramText, lemma, paradigmCollector, flagCollector, restrCollector);
        if (newBegin == -1)
            newBegin = thirdPersOnly.applyDirect(
                    gramText, lemma, paradigmCollector, flagCollector, restrCollector);
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
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 *                          ja vismaz gramatika atbilst šim likumam
     * @return  jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
     *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
     */
    @Override
    public int applyOptHyphens(
    		String gramText, String lemma, Set<Integer> paradigmCollector,
			Flags flagCollector, StructRestrs restrCollector)
    {
        int newBegin = thirdPersUsually.applyOptHyphens(
                gramText, lemma, paradigmCollector, flagCollector, restrCollector);
        if (newBegin == -1)
            newBegin = thirdPersOnly.applyOptHyphens(
                    gramText, lemma, paradigmCollector, flagCollector, restrCollector);
        return newBegin;
    }

	/**
	 * Cik reižu likums ir lietots?
	 * @return skaits, cik reižu likums ir lietots.
	 */
	@Override
	public int getUsageCount()
	{
		int res = 0;
		if (thirdPersOnly != null) res = res + thirdPersOnly.getUsageCount();
		if (thirdPersUsually != null) res = res + thirdPersUsually.getUsageCount();
		return res;
	}

	/**
	 * Metode, kas ļauj dabūt likuma nosaukumu, kas ļautu šo likumu atšķirt no
	 * citiem.
	 * @return likuma vienkāršota reprezentācija, kas izmantojama diagnostikas
	 * izdrukās.
	 */
	@Override
	public String getStrReprezentation()
	{
		return String.format("%s \"%s\"",
				this.getClass().getSimpleName(), patternText);
	}
}
