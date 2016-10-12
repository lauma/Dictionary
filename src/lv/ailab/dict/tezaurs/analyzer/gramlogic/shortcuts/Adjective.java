package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.SimpleSubRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

/**
 * Ērtummetode īpašības vārdiem.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class Adjective
{
	/**
	 * Izveido BaseRule īpašības vārdiem šabloniem formā:
	 * īp. v. -ais; s. -a, -ā
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @return BaseRule ar 13 un 14. paradigmu divdabju karodziņiem
	 * TODO likt šeit tās dzimtes vai nelikt?
	 */
	public static BaseRule std(String patternText)
	{
		return BaseRule.of(patternText, new SimpleSubRule[] {
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{13}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]š", new Integer[]{14}, null)},
				new Tuple[] {TFeatures.POS__ADJ});
	}
}
