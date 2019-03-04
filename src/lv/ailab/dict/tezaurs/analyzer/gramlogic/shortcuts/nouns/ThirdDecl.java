package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.struct.constants.flags.TFeatures;
import lv.ailab.dict.utils.Tuple;

/**
 * 3. deklinācijas ērtummetodes.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class ThirdDecl
{
	/**
	 * Izveido BaseRule 3. deklinācijas vīriešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 3. paradigmu
	 */
	public static BaseRule std(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 6,
				new Tuple[]{TFeatures.POS__NOUN},
				new Tuple[]{TFeatures.GENDER__MASC});
	}
}
