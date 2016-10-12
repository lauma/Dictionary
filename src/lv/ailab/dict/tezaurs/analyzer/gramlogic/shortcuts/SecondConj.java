package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.RegularVerbRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * 2. konjugācijas ērtummetodes.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class SecondConj
{
	/**
	 * Izveido RegularVerbRule 2. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 16
	 */
	public static RegularVerbRule direct(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return RegularVerbRule.of(patternBegin, patternEnd, lemmaEnd, 16, null, null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 16 un tikai 3.perosnas formām
	 */
	public static RegularVerbRule direct3Pers(String patternEnd,
			String lemmaEnd)
	{
		return RegularVerbRule.of(patternEnd, lemmaEnd, 16, null, null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām tikai 3. personas formām.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 16 un tikai 3.perosnas formām
	 */
	public static RegularVerbRule direct3PersParallel(String patternEnd,
			String lemmaEnd)
	{
		ArrayList<Tuple<String, String>> posFlags = new ArrayList<>();
		posFlags.add(TFeatures.PARALLEL_FORMS);
		if (RulesAsFunctions.containsFirstConj(patternEnd))
			posFlags.add(TFeatures.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags.add(TFeatures.ORIGINAL_NEEDED);
		return RegularVerbRule.of(patternEnd, lemmaEnd, 16,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return RegularVerbRule ar paradigmu 19
	 */
	public static RegularVerbRule refl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return RegularVerbRule.of(patternBegin, patternEnd, lemmaEnd, 19, null, null);
	}

	/**
	 * Izveido RegularVerbRule 2. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 19
	 */
	public static RegularVerbRule refl3Pers(String patternEnd,
			String lemmaEnd)
	{
		return RegularVerbRule.of(patternEnd, lemmaEnd, 19, null, null);
	}
}
