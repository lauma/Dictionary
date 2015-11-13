package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.analyzer.struct.Flags;
import lv.ailab.tezaurs.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums, kas apvieno divus SimpleRule - divus sdarbības vārdiem raksturīgos
 * gadījumus: vienu visām personām, otru - tikai trešajai.
 * Piemēram, "-brāžu, -brāz, -brāž, pag. -brāzu" un
 * "parasti 3. pers., -brāž, pag. -brāzu".
 * Šajā klasē apvienots saskarne likumiem, kam ir:
 * a) gan visu personu formu šablons, gan tikai trešās personas formu šablons,
 * b) tikai trešās personas formu šablons.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
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
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public RegularVerbRule(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Set<Tuple<Keys, String>> positiveFlags,
			Set<Tuple<Keys, String>> alwaysFlags)
	{
		HashSet<Tuple<Keys,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(Tuple.of(Keys.POS, Values.VERB.s));
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<Keys,String>> alwaysFlagsSet = alwaysFlags == null ?
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
					".*" + lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
			thirdPersonRule = new ThirdPersVerbRule(thirdPersonPattern,
					lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
		}
		else
		{
			allPersonRule = null;
			thirdPersonRule = new ThirdPersVerbRule(patternEnd,
					lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
		}
	}

	/**
	 * Konstruktors likumam, kur norādītas tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public RegularVerbRule(String patternEnd, String lemmaEnd, int paradigmId,
			Set<Tuple<Keys, String>> positiveFlags,
			Set<Tuple<Keys, String>> alwaysFlags)
	{
		HashSet<Tuple<Keys,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(Tuple.of(Keys.POS, Values.VERB.s));
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<Keys,String>> alwaysFlagsSet = alwaysFlags == null ?
				null : new HashSet<>(alwaysFlags);

		allPersonRule = null;
		thirdPersonRule = new ThirdPersVerbRule(patternEnd,
				lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
	}

	/**
	 * Konstruktors pilnam likumam.
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
	public static RegularVerbRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Tuple<Keys,String>[] positiveFlags, Tuple<Keys,String>[] alwaysFlags)
	{
		return new RegularVerbRule(patternBegin, patternEnd, lemmaEnd, paradigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Konstruktors likumam, kur norādītas tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 */
	public static RegularVerbRule of(String patternEnd, String lemmaEnd, int paradigmId,
			Tuple<Keys,String>[] positiveFlags, Tuple<Keys,String>[] alwaysFlags)
	{
		return new RegularVerbRule(patternEnd, lemmaEnd, paradigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}


	/**
	 * Izveido RegularVerbRule 2. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 16
	 */
	public static RegularVerbRule secondConjDir(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new RegularVerbRule(patternBegin, patternEnd, lemmaEnd, 16, null, null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 16 un tikai 3.perosnas formām
	 */
	public static RegularVerbRule secondConjDir3Pers(String patternEnd,
			String lemmaEnd)
	{
		return new RegularVerbRule(patternEnd, lemmaEnd, 16, null, null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 16 un tikai 3.perosnas formām
	 */
	public static RegularVerbRule secondConjDir3PersParallel(String patternEnd,
			String lemmaEnd)
	{
		return RegularVerbRule.of(patternEnd, lemmaEnd, 16,
				new Tuple[] {Features.PARALLEL_FORMS}, null);
	}

	/**
	 * Izveido RegularVerbRule 3. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return RegularVerbRule ar paradigmu 17
	 */
	public static RegularVerbRule thirdConjDir(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<Keys, String> soundChange = presentChange ?
				Features.HAS_PRESENT_SOUNDCHANGE : Features.NO_PRESENT_SOUNDCHANGE;
		return  RegularVerbRule.of(patternBegin, patternEnd, lemmaEnd, 17,
				new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei, bez
	 *                  "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmu 17
	 */
	public static RegularVerbRule thirdConjDir3Pers(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<Keys, String> soundChange = presentChange ?
				Features.HAS_PRESENT_SOUNDCHANGE : Features.NO_PRESENT_SOUNDCHANGE;
		return RegularVerbRule.of(patternEnd, lemmaEnd, 17,
				new Tuple[]{soundChange}, null);

	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām, kam visām ir vienādas mijas, un ir norādītas tikai
	 * 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmu 17
	 */
	public static RegularVerbRule thirdConjDir3PersParallel(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<Keys, String> soundChange = presentChange ?
				Features.HAS_PRESENT_SOUNDCHANGE : Features.NO_PRESENT_SOUNDCHANGE;
		return RegularVerbRule.of(patternEnd, lemmaEnd, 17,
				new Tuple[]{Features.PARALLEL_FORMS, soundChange}, null);

	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 19
	 */
	public static RegularVerbRule secondConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new RegularVerbRule(patternBegin, patternEnd, lemmaEnd, 19, null, null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 19
	 */
	public static RegularVerbRule secondConjRefl3Pers(String patternEnd,
			String lemmaEnd)
	{
		return new RegularVerbRule(patternEnd, lemmaEnd, 19, null, null);
	}

	/**
	 * Izveido RegularVerbRule 3. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return RegularVerbRule ar paradigmu 20
	 */
	public static RegularVerbRule thirdConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<Keys, String> soundChange = presentChange ?
				Features.HAS_PRESENT_SOUNDCHANGE : Features.NO_PRESENT_SOUNDCHANGE;
		return  RegularVerbRule.of(patternBegin, patternEnd, lemmaEnd, 20,
				new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return ThirdPersVerbRule ar paradigmu 20

	 */
	public static RegularVerbRule thirdConjRefl3Pers(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<Keys, String> soundChange = presentChange ?
				Features.HAS_PRESENT_SOUNDCHANGE : Features.NO_PRESENT_SOUNDCHANGE;
		return RegularVerbRule.of(patternEnd, lemmaEnd, 20,
				new Tuple[]{soundChange}, null);
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
