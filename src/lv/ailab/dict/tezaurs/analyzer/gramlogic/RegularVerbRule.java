package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums, kas apvieno divus SimpleRule - divus darbības vārdiem raksturīgos
 * gadījumus: vienu visām personām, otru - tikai trešajai.
 * Piemēram, "-brāžu, -brāz, -brāž, pag. -brāzu" un
 * "parasti 3. pers., -brāž, pag. -brāzu".
 * Šajā klasē apvienots saskarne likumiem, kam ir:
 * a) gan visu personu formu šablons, gan tikai trešās personas formu šablons,
 * b) tikai trešās personas formu šablons.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * @author Lauma
 */
public class RegularVerbRule implements Rule
{
	protected BaseRule allPersonRule;
	protected ThirdPersVerbRule thirdPersonRule;
	
	/**
	 * Konstruktors pilnam likumam.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 *                      (var būt null, tad izveido tikai trešās personas
	 *                      likumu)
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa, lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms		paradigmas, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public RegularVerbRule(String patternBegin, String patternEnd,
			String lemmaEnd, Set<Integer> paradigms,
			Set<Tuple<String, String>> positiveFlags,
			Set<Tuple<String, String>> alwaysFlags)
	{
		HashSet<Tuple<String,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(TFeatures.POS__VERB);
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<String,String>> alwaysFlagsSet = alwaysFlags == null ?
				null : new HashSet<>(alwaysFlags);

		if (patternBegin != null)
		{
			String begin = patternBegin.trim();
			String end = patternEnd.trim();
			String allPersonPattern = begin + " " + end;
			String thirdPersonPattern;
			if (end.endsWith("u"))
				thirdPersonPattern = end.substring(0, end.length() - 1) + "a";
			else if (end.endsWith("os"))
				thirdPersonPattern = end.substring(0, end.length() - 2) + "ās";
			else
			{
				System.err
						.printf("Neizdevās izveidot \"parasti 3. pers.\" likumu gramatikas šablonam \"%s\"\n", allPersonPattern);
				thirdPersonPattern = allPersonPattern;
			}
			allPersonRule = BaseRule.simple(allPersonPattern,
					".*" + lemmaEnd, paradigms, positiveFlagsFull, alwaysFlagsSet);
			thirdPersonRule = new ThirdPersVerbRule(thirdPersonPattern,
					lemmaEnd, paradigms, positiveFlagsFull, alwaysFlagsSet);
		}
		else
		{
			allPersonRule = null;
			thirdPersonRule = new ThirdPersVerbRule(patternEnd,
					lemmaEnd, paradigms, positiveFlagsFull, alwaysFlagsSet);
		}
	}

	/**
	 * Konstruktors likumam, kur norādītas tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa, lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public RegularVerbRule(String patternEnd, String lemmaEnd,
			Set<Integer> paradigms,
			Set<Tuple<String, String>> positiveFlags,
			Set<Tuple<String, String>> alwaysFlags)
	{
		HashSet<Tuple<String,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(Tuple.of(TKeys.POS, TValues.VERB));
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<String,String>> alwaysFlagsSet = alwaysFlags == null ?
				null : new HashSet<>(alwaysFlags);

		allPersonRule = null;
		thirdPersonRule = new ThirdPersVerbRule(patternEnd,
				lemmaEnd, paradigms, positiveFlagsFull, alwaysFlagsSet);
	}

	/**
	 * Konstruktors pilnam likumam.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa, lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public static RegularVerbRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new RegularVerbRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Konstruktors likumam, kur norādītas tikai 3. personas formas un viena
	 * paradigma.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa, lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public static RegularVerbRule of(String patternEnd, String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new RegularVerbRule(patternEnd, lemmaEnd,
				new HashSet<Integer>(){{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Konstruktors likumam, kur norādītas tikai 3. personas formas un vairākas
	 * paradigmas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa, lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms		paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public static RegularVerbRule of(String patternEnd, String lemmaEnd,
			Integer[] paradigms,
			Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new RegularVerbRule(patternEnd, lemmaEnd,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}


	/**

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
	public int applyDirect (String gramText, String lemma,
			HashSet<Integer> paradigmCollector, Flags flagCollector)
	{
		int newBegin = -1;
		if (thirdPersonRule != null)
			newBegin = thirdPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1 & allPersonRule != null)
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
	public int applyOptHyphens(String gramText, String lemma,
			HashSet<Integer> paradigmCollector, Flags flagCollector)
	{
		int newBegin = -1;
		if (thirdPersonRule != null)
			newBegin = thirdPersonRule.applyOptHyphens(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1 && allPersonRule != null)
			newBegin = allPersonRule.applyOptHyphens(gramText, lemma,
					paradigmCollector, flagCollector);
		return newBegin;		
	}


}
