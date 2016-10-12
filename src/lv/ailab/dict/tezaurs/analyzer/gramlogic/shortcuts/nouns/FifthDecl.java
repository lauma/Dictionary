package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

/**
 * 5. deklinācijas ērtummetodes.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class FifthDecl
{
	/**
	 * Izveido BaseRule 5. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 9. paradigmu
	 */
	public static BaseRule std(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 9,
				new Tuple[]{TFeatures.POS__NOUN}, new Tuple[]{TFeatures.GENDER__FEM});
	}

	/**
	 * Izveido BaseRule 5. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī bez līdzskaņu mijas.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 9. paradigmu
	 */
	public static BaseRule noChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 9,
				new Tuple[]{TFeatures.POS__NOUN, TFeatures.NO_SOUNDCHANGE}, new Tuple[]{TFeatures.GENDER__FEM});
	}

	/**
	 * Izveido BaseRule 5. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī ar fakultatīvu līdzskaņu miju.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 9. paradigmu
	 */
	public static BaseRule optChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 9,
				new Tuple[]{TFeatures.POS__NOUN, TFeatures.OPT_SOUNDCHANGE}, new Tuple[]{TFeatures.GENDER__FEM});
	}
}
