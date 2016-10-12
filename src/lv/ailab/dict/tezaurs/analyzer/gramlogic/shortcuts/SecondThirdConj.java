package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.RegularVerbRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * Ērtummetodes darbības vārdiem, kas vienlaicīgi pieder gan 2., gan 3.
 * konjugācijai.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public class SecondThirdConj
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
	 * @return likums ar paradigmām 16, 17
	 */
	public static RegularVerbRule direct3PersParallel(
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
		return RegularVerbRule.of(patternEnd, lemmaEnd, new Integer[]{16, 17},
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
	 * @return likums ar paradigmām 19, 20
	 */
	public static RegularVerbRule refl3PersParallel(
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
		return RegularVerbRule.of(patternEnd, lemmaEnd, new Integer[]{19, 20},
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}
}
