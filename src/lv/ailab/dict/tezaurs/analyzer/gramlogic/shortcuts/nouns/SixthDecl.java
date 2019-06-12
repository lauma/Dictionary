package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.utils.Tuple;

/**
 * 6. deklinācijas ērtummetodes.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class SixthDecl
{
	/**
	 * Izveido BaseRule 6. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī un līdzskaņu miju.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 11. paradigmu
	 */
	public static BaseRule std(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 11,
				new Tuple[]{TFeatures.POS__NOUN}, new Tuple[]{TFeatures.GENDER__FEM});
	}

	/**
	 * Izveido BaseRule 6. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī bez līdzskaņu mijas.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 11. paradigmu
	 */
	public static BaseRule noChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 35,
				new Tuple[]{TFeatures.POS__NOUN}, new Tuple[]{TFeatures.GENDER__FEM});
	}

	/**
	 * Izveido BaseRule 6. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī un fakultatīvu līdzskaņu miju.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 11. paradigmu
	 */
	public static BaseRule optChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, new Integer[]{11, 35},
				new Tuple[]{TFeatures.POS__NOUN}, new Tuple[]{TFeatures.GENDER__FEM});
				//new Tuple[]{TFeatures.POS__NOUN, TFeatures.OPT_SOUNDCHANGE}, new Tuple[]{TFeatures.GENDER__FEM});
	}
}
