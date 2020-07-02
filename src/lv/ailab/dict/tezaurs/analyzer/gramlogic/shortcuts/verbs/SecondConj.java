package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.PluralVerbRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.VerbDoubleRule;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.tezaurs.struct.constants.flags.TKeys;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * 2. konjugācijas ērtummetodes.
 * NB: Metodes, kam nosaukumā norādīts "AllPers", neizveido 3. personas likumus!
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class SecondConj
{
	/**
	 * Izveido VerbDoubleRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 16
	 */
	public static VerbDoubleRule direct(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 16,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 16 un tikai 3.personas formām
	 */
	public static VerbDoubleRule direct3Pers(String patternEnd,
			String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 16,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas tiešajam darbības vārdam ar
	 * 1. konjugācijas paralēlajām formām tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param pastStem		jau sagatavots 3. personas celms 1. konjugācijai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 16 un tikai 3.perosnas formām
	 */
	public static VerbDoubleRule direct3PersParallelFirstConj(
			String patternEnd, String pastStem, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
		posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM_ALL_PERS);
		posFlags.add(Tuple.of(TKeys.PAST_STEMS, pastStem));
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 16,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas tiešajam darbības vārdam, kam
	 * dotas visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 16. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule directAllPers(
			String patternText, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 16,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas tiešajam darbības vārdam, kam
	 * dotas visas formas un 1. konjugācijas paralēlformas, bet atvasināt tikai
	 * trešās personas formu likumu nav iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param pastStem		jau sagatavots 3. personas celms 1. konjugācijai
	 * @param parallelFor3PersOnly	norāde, ka paralēlforma ir tikai 3. personai,
	 *                              nevis visām
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 16. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule directAllPersParallelFirstConj(
			String patternText, String pastStem, boolean parallelFor3PersOnly,
			String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
		if (parallelFor3PersOnly) posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM_3_PERS);
		else posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM_ALL_PERS);
		posFlags.add(Tuple.of(TKeys.PAST_STEMS, pastStem));
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 16,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido PluralVerbRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return PluralVerbRule ar paradigmu 16
	 */
	public static PluralVerbRule directPlural(String patternText, String lemmaEnd)
	{
		return PluralVerbRule.of(patternText, lemmaEnd, 16,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 19
	 */
	public static VerbDoubleRule refl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 19,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 19 un tikai 3. personas formām
	 */
	public static VerbDoubleRule refl3Pers(String patternEnd,
			String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 19,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas atgriezeniskajam darbības vārdam,
	 * kam dotas visas formas, bet atvasināt tikai trešās personas formu likumu
	 * nav iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 19. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule reflAllPers(
			String patternText, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 19,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}

	/**
	 * Izveido PluralVerbRule 2. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return PluralVerbRule ar paradigmu 19
	 */
	public static PluralVerbRule reflPlural(String patternText, String lemmaEnd)
	{
		return PluralVerbRule.of(patternText, lemmaEnd, 19,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}
}
