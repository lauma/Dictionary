package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

/**
 * Ērtummetodes divdabjiem.
 * Izveidots 2016-10-12.
 *
 * @author Lauma
 */
public final class Participle
{
	/**
	 * Izveido BaseRule divdabjiem ar izskaņām -is, -usi, -ies, -usies.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 0. paradigmu un divdabju karodziņiem
	 * TODO likt šeit tās dzimtes vai nelikt?
	 */
	public static BaseRule isUsiIesUsies(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 0,
				new Tuple[]{TFeatures.POS__PARTICIPLE_IS}, null);
	}
}
