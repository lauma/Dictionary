package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

/**
 * 2. deklinācijas ērtummetodes.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class SecondConj
{
	/**
	 * Izveido BaseRule 2. deklinācijas vīriešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 3. paradigmu
	 */
	public static BaseRule std(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 3,
				new Tuple[]{TFeatures.POS__NOUN},
				new Tuple[]{TFeatures.GENDER__MASC});
	}
}
