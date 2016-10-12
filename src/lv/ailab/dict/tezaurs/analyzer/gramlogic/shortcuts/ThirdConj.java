package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.RegularVerbRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * 3. konjugācijas ērtummetodes.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public class ThirdConj
{
	/**
	 * Izveido RegularVerbRule 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return RegularVerbRule ar paradigmu 17
	 */
	public static RegularVerbRule direct(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  RegularVerbRule.of(patternBegin, patternEnd, lemmaEnd, 17,
				new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido likumu 3. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām, tikai 3. personas formas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return likums ar paradigmu 17
	 */
	public static RegularVerbRule direct3Pers(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return RegularVerbRule.of(patternEnd, lemmaEnd, 17,
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
	 * @return likums ar paradigmu 17
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
		return RegularVerbRule.of(patternEnd, lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Izveido RegularVerbRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return RegularVerbRule ar paradigmu 20
	 */
	public static RegularVerbRule refl(
			String patternBegin, String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return  RegularVerbRule.of(patternBegin, patternEnd, lemmaEnd, 20,
				new Tuple[]{soundChange}, null);
	}

	/**
	 * Izveido RegularVerbRule 3. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, tikai 3. personas formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return ThirdPersVerbRule ar paradigmu 20

	 */
	public static RegularVerbRule refl3Pers(
			String patternEnd, String lemmaEnd, boolean presentChange)
	{
		Tuple<String, String> soundChange = presentChange ?
				TFeatures.HAS_PRESENT_SOUNDCHANGE : TFeatures.NO_PRESENT_SOUNDCHANGE;
		return RegularVerbRule.of(patternEnd, lemmaEnd, 20,
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
	 * @return likums ar paradigmu 20
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
		return RegularVerbRule.of(patternEnd, lemmaEnd, 20,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}
}
