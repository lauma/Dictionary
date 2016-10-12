package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.VerbDoubleRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.SimpleSubRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * Ērtummetodes darbības vārdiem, kas vienlaicīgi pieder gan 2., gan 3.
 * konjugācijai.
 * NB: Metodes, kam klāt norādīts "AllPers", neizveido 3. personas likumus.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class SecondThirdConj
{
	/**
	 * Izveido likumu tiešajam darbības vārdam ar paralēlajām formām, kas veido
	 * pilnu 2. un 3. konjugācijas formu sistēmu, un ir norādītas tikai 3.
	 * personas formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmām 16, 17 un tikai 3. personas formām.
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
		return VerbDoubleRule.of(patternEnd, lemmaEnd, new Integer[]{16, 17},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido VerbDoubleRule darbības vārdam, kas ir gan 2, gan 3. konjugācijā
	 * un kuram dotas visu personu formas/galotnes.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return	VerbDoubleRule ar paradigmām 16, 17 bez 3. personas likuma
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

		return VerbDoubleRule.of(patternText, null, lemmaEnd, new Integer[]{16, 17},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido likumu atgriezeniskajam darbības vārdam ar paralēlajām formām,
	 * kas veido pilnu 2. un 3. konjugācijas formu sistēmu, un ir norādītas
	 * tikai 3. personas formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmām 19, 20 un tikai 3. personas formām
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
		return VerbDoubleRule.of(patternEnd, lemmaEnd, new Integer[]{19, 20},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
     * Izveido VerbDoubleRule darbības vārdam, kas ir gan 2, gan 3. konjugācijā un
     * kuram dotas visu personu formas/galotnes.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
     * @param patternText	teksts, ar kuru jāsākas gramatikai
     * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
     * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return	VerbDoubleRule ar paradigmām 19, 20 bez 3. personas likuma
	 */
    public static VerbDoubleRule reflAllPersParallel(
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

		return VerbDoubleRule.of(patternText, null, lemmaEnd, new Integer[]{19, 20},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
    }
}
