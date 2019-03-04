package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.BaseRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.SimpleSubRule;
import lv.ailab.dict.tezaurs.analyzer.struct.constants.flags.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Vispārīgās ērtummetodes lietvārdiem, kas nepiešķir paradigmu.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public final class GenNoun
{
	/**
	 * Izveido BaseRule, visām pozitīvo karodziņu kopām papildinot ar vārdšķiru
	 * Lietvārds.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaLogic		nosacījumu saraksts, kurā katrs elements sastāv
	 *                          no trijnieka: 1)lemmu aprakstoša šablona, 2)
	 *                          paradigmām, ko lietot, ja lemma atbilst šim
	 *                          šablonam, 3) karodziņiem, ko uzstādīt, ja lemma
	 *                          atbilst šim šablonam.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	BaseRule ar karodziņu POS__NOUN visās pozitīvo karodziņu kopās.
	 */
	public static BaseRule any(String patternText, SimpleSubRule[] lemmaLogic,
			Tuple<String,String>[] alwaysFlags)
	{
		ArrayList<SimpleSubRule> tmp = new ArrayList<>();
		for (SimpleSubRule r : lemmaLogic)
			tmp.add(r.cloneWithFeature(TFeatures.POS__NOUN));
		return new BaseRule(patternText, tmp,
				alwaysFlags == null ? null :
						new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Izveido BaseRule, visām pozitīvo karodziņu kopām papildinot ar vārdšķiru
	 * Lietvārds.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @param paradigms			paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	jauns BaseRule, kam ir pazīme Vārdšķira ar vērtību Lietvārds.
	 */
	public static BaseRule any(String patternText, String lemmaRestrictions,
			Integer[] paradigms, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags)
	{
		HashSet<Tuple<String,String>> fullPosFlags = new HashSet<>();
		if (positiveFlags != null) fullPosFlags.addAll(Arrays.asList(positiveFlags));
		fullPosFlags.add(TFeatures.POS__NOUN);
		return BaseRule.simple(patternText, lemmaRestrictions,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				fullPosFlags,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Izveido BaseRule, visām pozitīvo karodziņu kopām papildinot ar vārdšķiru
	 * Lietvārds.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @param paradigm			paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	jauns BaseRule, kam ir pazīme Vārdšķira ar vērtību Lietvārds.
	 */
	public static BaseRule any(String patternText, String lemmaRestrictions,
			int paradigm, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags)
	{
		HashSet<Tuple<String,String>> fullPosFlags = new HashSet<>();
		if (positiveFlags != null) fullPosFlags.addAll(Arrays.asList(positiveFlags));
		fullPosFlags.add(TFeatures.POS__NOUN);
		return BaseRule.simple(patternText, lemmaRestrictions,
				new HashSet<Integer>()
				{{add(paradigm);}}, fullPosFlags,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

}
