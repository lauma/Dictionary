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
	 * tagadnes mijas un bez paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 17
	 */
	public static VerbDoubleRule directStd(String patternBegin, String patternEnd, String lemmaEnd)
	{
		return  VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 17,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas tiešajam darbības vārdam ar
	 * tagadnes miju, bet bez paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 45
	 */
	public static VerbDoubleRule directChange(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return  VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 45,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam bez tagadnes
	 * mijas un bez paralēlajām formām, tikai 3. personas formas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 17 un tikai 3. personas formām
	 */
	public static VerbDoubleRule directStd3Pers(
			String patternEnd, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 17,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam ar tagadnes miju,
	 * bet bez paralēlajām formām, tikai 3. personas formas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu  45 un tikai 3. personas formām
	 */
	public static VerbDoubleRule directChange3Pers(
			String patternEnd, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 45,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);

	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam bez tagadnes
	 * mijas, bet ar paralēlajām formām, kam visām ir vienādas mijas, un ir
	 * norādītas tikai 3. personas formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 17 un tikai 3. personas formām
	 */
	public static VerbDoubleRule directStd3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam ar tagadnes miju
	 * un paralēlajām formām, kam visām ir vienādas mijas, un ir norādītas tikai
	 * 3. personas formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 17 vai 45 un tikai 3. personas formām
	 */
	public static VerbDoubleRule directChange3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 45,
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
	 * @return VerbDoubleRule ar 17. un 45. paradigmu un tikai 3. personas formām
	 */
	public static VerbDoubleRule directOptChange3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, new Integer[]{17, 45},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas tiešajam darbības vārdam, kam
	 * dotas visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams, ir paralēlās formas, nav tagadnes mijas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 17. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule directStdAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
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
	 * iespējams, ir paralēlās formas, ir tagadnes mija.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar 45. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule directChangeAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 45,
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
	 * @return VerbDoubleRule ar 17. un 45. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule directOptChangeAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__DIRECT_VERB);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternText, null, lemmaEnd, new Integer[] {17, 45},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido PluralVerbRule 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām ar tagadnes miju.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 45
	 */
	public static PluralVerbRule directChangePlural(
			String patternText, String lemmaEnd)
	{
		return  PluralVerbRule.of(patternText, lemmaEnd, 45,
				new Tuple[]{TFeatures.POS__DIRECT_VERB}, null);
	}

	//TODO

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām bez tagadnes mijas.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 20
	 */
	public static VerbDoubleRule reflStd(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return  VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 20,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām ar tagadnes miju.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 20 vai 46
	 */
	public static VerbDoubleRule reflChange(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return  VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 46,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}

	/**
	 * Izveido VerbDoubleRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, tikai 3. personas formām, bez tagadnes mijām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 20 un tikai 3. personas formām

	 */
	public static VerbDoubleRule reflStd3Pers(
			String patternEnd, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 20,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}
	/**
	 * Izveido VerbDoubleRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, tikai 3. personas formām, ar tagadnes mijām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 20 vai 46 un tikai 3. personas formām

	 */
	public static VerbDoubleRule reflChange3Pers(
			String patternEnd, String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 46,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}

	/**
	 * Izveido likumu 3. konjugācijas atgriezeniskajam darbības vārdam ar
	 * paralēlajām formām, kam visām ir vienādas mijas, un ir norādītas tikai
	 * 3. personas formas, bez tagadnes mijas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 20 un tikai 3. personas formām
	 */
	public static VerbDoubleRule reflStd3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__REFL_VERB);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 20,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido likumu 3. konjugācijas atgriezeniskajam darbības vārdam ar
	 * paralēlajām formām, kam visām ir vienādas mijas, un ir norādītas tikai
	 * 3. personas formas, ar tagadnes miju.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 46 un tikai 3. personas formām
	 */
	public static VerbDoubleRule reflCHange3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__REFL_VERB);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 46,
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
	 * @return VerbDoubleRule ar 20. un 46. paradigmu un tikai 3. personas formām
	 */
	public static VerbDoubleRule reflOptChange3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__REFL_VERB);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, new Integer[] {20, 46},
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
	 * @return VerbDoubleRule ar 20. un 46. paradigmu bez 3. personas likuma
	 */
	public static VerbDoubleRule reflOptChangeAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		posFlags.add(TFeatures.POS__REFL_VERB);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternText, null, lemmaEnd, new Integer[] {20, 46},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido PluralVerbRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, bez tagadnes mijas.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return PluralVerbRule ar paradigmu 20
	 */
	public static PluralVerbRule reflStdPlural(
			String patternText, String lemmaEnd)
	{
		return  PluralVerbRule.of(patternText, lemmaEnd, 20,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}

	/**
	 * Izveido PluralVerbRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, ar tagadnes miju.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return PluralVerbRule ar paradigmu 46
	 */
	public static PluralVerbRule reflChangePlural(
			String patternText, String lemmaEnd)
	{
		return  PluralVerbRule.of(patternText, lemmaEnd, 46,
				new Tuple[]{TFeatures.POS__REFL_VERB}, null);
	}
}
