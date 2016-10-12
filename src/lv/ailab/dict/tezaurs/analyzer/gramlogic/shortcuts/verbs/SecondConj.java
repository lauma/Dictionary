package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.VerbDoubleRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.PluralVerbRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * 2. konjugācijas ērtummetodes.
 * NB: Metodes, kam nosaukumā norādīts "AllPers", neizveido 3. personas likumus!
 * TODO: vai AllPers metodes vajag no BaseRule pārvest uz VerbDoubleRule, kā tas ir FirstConj?
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
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 16, null, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 16 un tikai 3.perosnas formām
	 */
	public static VerbDoubleRule direct3Pers(String patternEnd,
			String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 16, null, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām tikai 3. personas formām.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 16 un tikai 3.perosnas formām
	 */
	public static VerbDoubleRule direct3PersParallel(String patternEnd,
			String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 16,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido BaseRule 2. konjugācijas tiešajam darbības vārdam, kam dotas
	 * visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return BaseRule ar 16. paradigmu
	 */
	public static BaseRule directAllPers(
			String patternText, String lemmaEnd)
	{
		return BaseRule.of(patternText, ".*" + lemmaEnd, 16,
				new Tuple[]{TFeatures.POS__VERB}, null);
	}

	/**
	 * Izveido BaseRule 2. konjugācijas tiešajam darbības vārdam, kam dotas
	 * visas formas un paralēlformas, bet atvasināt tikai trešās personas formu
	 * likumu nav iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return BaseRule ar 16. paradigmu
	 */
	public static BaseRule directAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.POS__VERB);
		posFlags.add(TFeatures.PARALLEL_FORMS);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return BaseRule.of(patternText, ".*" + lemmaEnd, 16,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido PluralVerbRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 16
	 */
	public static PluralVerbRule directPlural(String patternText, String lemmaEnd)
	{
		return PluralVerbRule.of(patternText, lemmaEnd, 16, null, null);
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
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 19, null, null);
	}

	/**
	 * Izveido VerbDoubleRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 19
	 */
	public static VerbDoubleRule refl3Pers(String patternEnd,
			String lemmaEnd)
	{
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 19, null, null);
	}

	/**
	 * Izveido BaseRule 2. konjugācijas atgriezeniskajam darbības vārdam, kam
	 * dotas visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return BaseRule ar 19. paradigmu
	 */
	public static BaseRule reflAllPers(
			String patternText, String lemmaEnd)
	{
		return BaseRule.of(patternText, ".*" + lemmaEnd, 19,
				new Tuple[]{TFeatures.POS__VERB}, null);
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
		return PluralVerbRule.of(patternText, lemmaEnd, 19, null, null);
	}
}
