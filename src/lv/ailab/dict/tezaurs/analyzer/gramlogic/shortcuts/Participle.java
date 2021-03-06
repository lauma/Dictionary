package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.struct.constants.structrestrs.Type;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.utils.Tuple;
import org.apache.poi.ss.formula.functions.T;

/**
 * Ērtummetodes divdabjiem.
 * Izveidots 2016-10-12.
 *
 * @author Lauma
 */
public final class Participle
{
	/**
	 * Izveido BaseRule divdabjiem ar izskaņām -is, -usi.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 42. paradigmu un divdabju karodziņiem
	 * TODO likt šeit tās dzimtes vai nelikt?
	 */
	public static BaseRule isUsi(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 42,
				null, StructRestrs.One.of(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_IS),
				adjConversion(patternText) ? new Tuple[] {TFeatures.CONVERSION__ADJECTIVE} : null, null);
	}

	/**
	 * Izveido BaseRule divdabjiem ar izskaņām -is, -usi, -ies, -usies.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 43. paradigmu un divdabju karodziņiem
	 */
	public static BaseRule iesUsies(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 43,
				null, StructRestrs.One.of(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_IS),
				adjConversion(patternText) ? new Tuple[] {TFeatures.CONVERSION__ADJECTIVE} : null, null);
	}

	/**
	 * Izveido BaseRule divdabjiem ar izskaņām -ts, -ta.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 13. paradigmu un divdabju karodziņiem
	 */
	public static BaseRule tsTa(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 13,
				null, StructRestrs.One.of(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_TS),
				adjConversion(patternText) ? new Tuple[] {TFeatures.CONVERSION__ADJECTIVE} : null, null);
	}

	/* Izveido BaseRule divdabjiem ar izskaņām -dams, -dama.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 0 paradigmu un divdabju karodziņiem
	 */
	public static BaseRule damsDamaDamiesDamas(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 0,
				null, StructRestrs.One.of(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_DAMS),
				adjConversion(patternText) ? new Tuple[] {TFeatures.CONVERSION__ADJECTIVE} : null, null);
	}

	protected static boolean adjConversion (String patternText)
	{
		return patternText.matches("^.*\bīp\\. (v\\. )?noz(\\.|īmē\\.?)$");
	}
}
