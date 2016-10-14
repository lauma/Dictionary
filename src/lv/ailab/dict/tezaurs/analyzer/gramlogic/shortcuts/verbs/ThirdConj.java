package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.PluralVerbRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.VerbDoubleRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * 3. konjugācijas ērtummetodes.
 * NB: Metodes, kam klāt norādīts "AllPers", neizveido 3. personas likumus.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class ThirdConj
{
	/**
	 * Izveido VerbDoubleRule 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return VerbDoubleRule ar paradigmu 17
	 */
	public static VerbDoubleRule direct(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 17,
				new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmu 17 un tikai 3. personas formām
	 */
	public static VerbDoubleRule direct3Pers(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 17,
				new Tuple[]{soundChange}, null);

	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām, kam visām ir vienādas mijas, un ir norādītas tikai
	 * 3. personas formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmu 17 un tikai 3. personas formām
	 */
	public static VerbDoubleRule direct3PersParallel(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		if (presentChange)
			posFlags.add(TFeatures.HAS_PRESENT_SOUNDCHANGE);
		else
			posFlags.add(TFeatures.NO_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam, paralēlās formas
	 * ir gan ar miju, gan bez.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 17. paradigmu un tikai 3. personas formām
	 */
	public static VerbDoubleRule direct3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.OPT_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas tiešajam darbības vārdam, kam
	 * dotas visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams, ir paralēlās formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return VerbDoubleRule ar 17. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule directAllPersParallel(
			String patternText, String lemmaEnd, boolean presentChange)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		if (presentChange)
			posFlags.add(TFeatures.HAS_PRESENT_SOUNDCHANGE);
		else
			posFlags.add(TFeatures.NO_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas tiešajam darbības vārdam, kam
	 * dotas visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams, paralēlās formas ir gan ar miju, gan bez.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 17. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule directAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.OPT_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido PluralVerbRule 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return VerbDoubleRule ar paradigmu 17
	 */
	public static PluralVerbRule directPlural(
			String patternText, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  PluralVerbRule.of(patternText, lemmaEnd, 17, new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return VerbDoubleRule ar paradigmu 20
	 */
	public static VerbDoubleRule refl(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 20,
				new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return ThirdPersVerbRule ar paradigmu 20 un tikai 3. personas formām

	 */
	public static VerbDoubleRule refl3Pers(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 20,
				new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido likumu 3. konjugācijas atgriezeniskajam darbības vārdam ar
	 * paralēlajām formām, kam visām ir vienādas mijas, un ir norādītas tikai
	 * 3. personas formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmu 20 un tikai 3. personas formām
	 */
	public static VerbDoubleRule refl3PersParallel(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		if (presentChange)
			posFlags.add(TFeatures.HAS_PRESENT_SOUNDCHANGE);
		else
			posFlags.add(TFeatures.NO_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 20,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido likumu 3. konjugācijas atgriezeniskajam darbības vārdam,
	 * paralēlās formas ir gan ar miju, gan bez.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 20. paradigmu un tikai 3. personas formām
	 */
	public static VerbDoubleRule refl3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.OPT_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 20,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas darbības vārdam, kam dotas visas
	 * formas, bet atvasināt tikai trešās personas formu likumu nav iespējams,
	 * paralēlās formas ir gan ar miju, gan bez.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 20. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule reflAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.OPT_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 20,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido PluralVerbRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return PluralVerbRule ar paradigmu 20
	 */
	public static PluralVerbRule reflPlural(
			String patternText, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  PluralVerbRule.of(patternText, lemmaEnd, 20, new Tuple[]{soundChange}, null);
	}
}
