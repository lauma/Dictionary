package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums šabloniem, kas sākas ar "parasti dsk.," vai  "tikai dsk.,"
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * TODO Vai šo vispārināt un daļēji apvienot ar ThirdPersVerbRule?
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public class PluralVerbRule implements Rule
{
	protected BaseRule pluralOnly;
	protected BaseRule pluralUsually;

	/**
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms		paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti 3. personā" pievieno
	 *                      automātiski)
	 */
	public PluralVerbRule(String patternText, String lemmaEnding, Set<Integer> paradigms,
			Set<Tuple<String,String>> positiveFlags, Set<Tuple<String,String>> alwaysFlags)
	{
		pluralUsually = BaseRule.simple(
				"parasti dsk., " + patternText, ".*" + lemmaEnding, paradigms,
				new HashSet<Tuple<String, String>>()
				{{
					add(TFeatures.POS__VERB);
					if (positiveFlags != null) addAll(positiveFlags);
				}},
				new HashSet<Tuple<String, String>>()
				{{
					add(TFeatures.USUALLY_USED__PLURAL);
					if (alwaysFlags != null) addAll(alwaysFlags);
				}});
		pluralOnly = BaseRule.simple(
				"tikai dsk., " + patternText, ".*" + lemmaEnding, paradigms,
				new HashSet<Tuple<String, String>>()
				{{
					add(TFeatures.POS__VERB);
					if (positiveFlags != null) addAll(positiveFlags);
				}},
				new HashSet<Tuple<String, String>>()
				{{
					add(TFeatures.USED_ONLY__PLURAL);
					if (alwaysFlags != null) addAll(alwaysFlags);
				}});
	}

	/**
	 * Papildus konstruktors īsumam - gadījumam, kad ir tikai viena paradigma.
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti dskaudzskaitlī" pievieno
	 *                      automātiski)
	 */
	public static PluralVerbRule simple(String patternText, String lemmaEnding,
			int paradigmId,	Set<Tuple<String,String>> positiveFlags,
			Set<Tuple<String,String>> alwaysFlags)
	{
		return new PluralVerbRule(patternText, lemmaEnding,
				new HashSet<Integer>() {{add(paradigmId);}}, positiveFlags, alwaysFlags);
	}

	/**
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti daudzskaitlī" pievieno
	 *                      automātiski)
	 */
	public static PluralVerbRule of(String patternText, String lemmaEnding,
			int paradigmId, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags)
	{
		return new PluralVerbRule(patternText, lemmaEnding,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms	paradigmas, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti daudzskaitlī" pievieno
	 *                      automātiski)
	 */
	public static PluralVerbRule of(String patternText, String lemmaEnding,
			Integer[] paradigms, Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new PluralVerbRule(patternText, lemmaEnding,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Izveido PluralVerbRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 16
	 */
	public static PluralVerbRule secondConjDir(String patternText, String lemmaEnd)
	{
		return PluralVerbRule.of(patternText, lemmaEnd, 16, null, null);
	}

	/**
	 * Izveido PluralVerbRule 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return RegularVerbRule ar paradigmu 17
	 */
	public static PluralVerbRule thirdConjDir(
			String patternText, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  PluralVerbRule.of(patternText, lemmaEnd, 17, new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido PluralVerbRule 2. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 19
	 */
	public static PluralVerbRule secondConjRefl(String patternText, String lemmaEnd)
	{
		return PluralVerbRule.of(patternText, lemmaEnd, 19, null, null);
	}

	/**
	 * Izveido PluralVerbRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return RegularVerbRule ar paradigmu 17
	 */
	public static PluralVerbRule thirdConjRefl(
			String patternText, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  PluralVerbRule.of(patternText, lemmaEnd, 20, new Tuple[]{soundChange}, null);
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
		int newBegin = pluralUsually.applyDirect(
				gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1)
			newBegin = pluralOnly.applyDirect(
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
		int newBegin = pluralUsually.applyOptHyphens(
				gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1)
			newBegin = pluralOnly.applyOptHyphens(
					gramText, lemma, paradigmCollector, flagCollector);
		return newBegin;
	}
}
