package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.struct.constants.structrestrs.Type;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.SimpleSubRule;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.utils.Tuple;

/**
 * Ērtummetodes vārdiem, kas pieder vairākām vārdšķirām vienlaicīgi.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class MultiPos
{
	/**
	 * Izveido BaseRule īpašības vārdiem šabloniem formā:
	 * -ais; s. -a, -ā
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @return BaseRule ar 13 un 14. paradigmu divdabju karodziņiem
	 * TODO likt šeit tās dzimtes vai nelikt?
	 */
	public static BaseRule adjectiveParticiple (String patternText)
	{
		return BaseRule.of(patternText, new SimpleSubRule[] {
				SimpleSubRule.of(".*[^aeiouāēīōū]š", new Integer[]{14},
						new Tuple[]{TFeatures.POS__ADJ}),
				SimpleSubRule.of(".*ts", new Integer[]{13},
						new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__VERB, TFeatures.UNCLEAR_POS}, StructRestrs.One.of(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_TS)),
				SimpleSubRule.of(".*ošs", new Integer[]{13},
						new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__VERB, TFeatures.UNCLEAR_POS}, StructRestrs.One.of(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_OSS)),
				//SimpleSubRule.of(".*dams", new Integer[]{13, 0},
						// new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__VERB, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}, StructRestrs.One.of(Type.IN_FORM, TFeatures.POS__PARTICIPLE_DAMS)),
				SimpleSubRule.of(".*[āa]ms", new Integer[]{13},
						new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__VERB, TFeatures.UNCLEAR_POS}, StructRestrs.One.of(Type.IN_FORM, TFeatures.MOOD__PARTICIPLE_AMS)),
				SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{13},
						new Tuple[]{TFeatures.POS__ADJ})},
			null);
		// -dams divdabis izmests, jo tie lokās savādāk.
	}

}
