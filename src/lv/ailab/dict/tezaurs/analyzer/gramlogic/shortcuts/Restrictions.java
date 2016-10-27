package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.FormRestrRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.Tuple;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ērtības metodes likumiem, kas rada ierobežojošās vārdformas.
 * Izveidots 2016-10-26.
 * @author Lauma
 */
public final class Restrictions
{
	/**
	 * Tiek pieņemts, ka no lemmas jānogriež tieši "lemmaEnding" apjoms, lai
	 * veidotu likumu un ka vārdformas izskaņu no pārējā pārējā "patternEnding"
	 * atdala pirmais komats/atstarpe/semikols.
	 * Piemērs: lietv. nozīmē: absolūtais, -ā, v.
	 * @param patternBegin		šablona sākums bez noslēdzošās atstarpes
	 * @param patternEnding		šablona beigas formā "izskaņa, pārējais šablons"
	 * @param lemmaEnding		izskaņa, ar ko jābeidzas lemmai, lai likumu varētu
	 *                      	piemērot
	 * @param positiveFlags		šos karodziņus uzstāda pamata karodziņu
	 *                          savācējam, ja gan gramatikas teksts, gan lemma
	 *                          atbilst attiecīgajiem šabloniem
	 * @param restrFormFlags	karodziņi, ko pievienot ierobežojošajai
	 *                          vārdformai
	 * @return
	 */
	public static FormRestrRule anyOneForm(
			String patternBegin, String patternEnding, String lemmaEnding,
			Tuple<String, String>[] positiveFlags, Tuple<String, String>[] restrFormFlags)
	{
		Matcher m = Pattern.compile("([^., ]*)[,. ].*").matcher(patternEnding);
		String formEnding = "";
		if (!m.matches())
			System.err.println ("Šablonam \"lietv. nozīmē: _?_" + patternEnding
					+ "\" neizdodas noteikt vārdformas izskaņu.");
		else formEnding = m.group(1);
		return FormRestrRule.of(patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), positiveFlags, formEnding, restrFormFlags);
	}

	/**
	 * Tiek pieņemts, ka no lemmas jānogriež tieši "lemmaEnding" apjoms, lai
	 * veidotu likumu un ka vārdformas izskaņu no pārējā pārējā "patternMiddle"
	 * atdala pirmais  komats/atstarpe/semikols.
	 * Piemērs: lietv. nozīmē: aizturētais, -ā, v. aizturētā, -ās, s.
	 * @param patternBegin		šablona sākums bez noslēdzošās atstarpes
	 * @param patternMiddle		šablona vidus formā "izskaņa, pārējais šablons"
	 *                          bez noslēdzošās atstarpes
	 * @param patternEnding		šablona beigas
	 * @param lemmaEnding		izskaņa, ar ko jābeidzas lemmai, lai likumu
	 *                      	varētu piemērot
	 * @param positiveFlags		šos karodziņus uzstāda pamata karodziņu
	 *                          savācējam, ja gan gramatikas teksts, gan lemma
	 *                          atbilst attiecīgajiem šabloniem
	 * @param restrFormFlags	karodziņi, ko pievienot ierobežojošajai
	 *                          vārdformai
	 * @return
	 */
	public static FormRestrRule anyTwoForm(
			String patternBegin, String patternMiddle, String patternEnding,
			String lemmaEnding, Tuple<String, String>[] positiveFlags,
			Tuple<String, String>[] restrFormFlags)
	{
		Matcher m = Pattern.compile("([^., ]*)[,. ].*").matcher(patternMiddle);
		String formEnding = "";
		if (!m.matches())
			System.err.println ("Šablonam \"lietv. nozīmē: _?_" + patternEnding
					+ "\" neizdodas noteikt vārdformas izskaņu.");
		else formEnding = m.group(1);
		return FormRestrRule.of(patternBegin + " ", patternMiddle + " ", patternEnding,
				".*" + lemmaEnding,
				lemmaEnding.length(), positiveFlags, formEnding, restrFormFlags);
	}

	/**
	 * Likums, kas sākas ar "lietv. nozīmē:". Tiek pieņemts, ka no lemmas
	 * jānogriež tieši "lemmaEnding" apjoms, lai veidotu likumu un ka
	 * vārdformas izskaņu no pārējā pārējā "patternEnding" atdala pirmais
	 * komats/atstarpe/semikols.
	 * Piemērs: lietv. nozīmē: absolūtais, -ā, v.
	 * @param patternEnding	šablona beigas formā "izskaņa, pārējais šablons"
	 * @param lemmaEnding	izskaņa, ar ko jābeidzas lemmai, lai likumu varētu
	 *                      piemērot.
	 * @return
	 */
	public static FormRestrRule nounContSimple(
			String patternEnding, String lemmaEnding)
	{
		return anyOneForm("lietv. nozīmē:", patternEnding, lemmaEnding,
				null, new Tuple[] {TFeatures.CONTAMINATION__NOUN});
	}

	/**
	 * Likums, kas sākas ar "lietv. nozīmē:". Tiek pieņemts, ka no lemmas
	 * jānogriež tieši "lemmaEnding" apjoms, lai veidotu likumu un ka
	 * vārdformas izskaņu no pārējā pārējā "patternMiddle" atdala pirmais
	 * komats/atstarpe/semikols.
	 * Piemērs: lietv. nozīmē: aizturētais, -ā, v. aizturētā, -ās, s.
	 * @param patternMiddle	šablona vidus formā "izskaņa, pārējais šablons"
	 * @param patternEnding	šablona beigas
	 * @param lemmaEnding	izskaņa, ar ko jābeidzas lemmai, lai likumu varētu
	 *                      piemērot
	 * @return
	 */
	public static FormRestrRule nounContDouble(
			String patternMiddle, String patternEnding, String lemmaEnding)
	{
		return anyTwoForm("lietv. nozīmē:", patternMiddle, patternEnding,
				lemmaEnding, null, new Tuple[] {TFeatures.CONTAMINATION__NOUN});
	}

	/**
	 * Likums, kas satur vienu formu un aiz tās nav nekādas papildu gramatikas.
	 * Tiek pieņemts, ka no lemmas jānogriež tieši "lemmaEnding" apjoms, lai
	 * veidotu likumu.
	 * @param patternBegin		šablona sākums
	 * @param patternEnding 	šablona beigas, kas sakrīt ar ierobežojošās
	 *                         	vārdformas izskaņu
	 * @param lemmaEnding		izskaņa, ar ko jābeidzas lemmai, lai likumu
	 *                          varētu piemērot
	 * @param restrFormFlags	karodziņi, ko pievienot ierobežojošajai
	 *                          vārdformai
	 * @return
	 */
	public static FormRestrRule noPostGram(String patternBegin, String patternEnding,
			String lemmaEnding, Tuple<String, String>[] restrFormFlags)
	{
		return FormRestrRule.of(patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), null, patternEnding, restrFormFlags);
	}

	/**
	 * Likums, kas satur vienu formu un aiz tās nav nekādas papildu gramatikas.
	 * Tiek pieņemts, ka no lemmas jānogriež tieši "lemmaEnding" apjoms, lai
	 * veidotu likumu.
	 * @param patternBegin		šablona sākums
	 * @param patternEnding 	šablona beigas, kas sakrīt ar ierobežojošās
	 *                         	vārdformas izskaņu
	 * @param lemmaEnding		izskaņa, ar ko jābeidzas lemmai, lai likumu
	 *                          varētu piemērot
	 * @param patternEnding		neeskepota teksta virkne, ar kuru grmatikai
	 *                          jāturpinās pēc lemmai specifiskās daļas, lai šis
	 *                          likums būtu piemērojams
	 * @param restrFormFlags	karodziņi, ko pievienot ierobežojošajai
	 *                          vārdformai
	 * @return
	 */
	public static FormRestrRule noPostGram(String patternBegin, String patternEnding,
			String lemmaEnding, Tuple<String, String>[] positiveFlags,
			Tuple<String, String>[] restrFormFlags)
	{
		return FormRestrRule.of(patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), positiveFlags, patternEnding, restrFormFlags);
	}

/*	public static FormRestrRule participleSimple(String patternBegin, String patternEnding,
			String lemmaEnding)
	{
		String key = TKeys.USED_IN_FORM;
		if (patternBegin.matches(".*\bparasti\b.*"))
			key = TKeys.USUALLY_USED_IN_FORM;
		else if (patternBegin.matches(".*\bbieži\b.*"))
			key = TKeys.OFTEN_USED_IN_FORM;
		else if (patternBegin.matches(".*\btikai\b.*"))
			key = TKeys.USED_ONLY_IN_FORM;

		String partType = TValues.PARTICIPLE;
		if (patternEnding.endsWith("damies") || patternEnding.endsWith("dams")) //aizvilkties->aizvilkdamies
			partType = TValues.PARTICIPLE_DAMS;
		else if (patternEnding.endsWith("ams") || patternEnding.endsWith("āms"))
			partType = TValues.PARTICIPLE_AMS;
		else if (patternEnding.endsWith("ošs")) // garāmbraucošs
			partType = TValues.PARTICIPLE_OSS;
		else if (patternEnding.endsWith("ts")) // aizdzert->aizdzerts
			partType = TValues.PARTICIPLE_TS;
		else if (patternEnding.endsWith("is") || patternEnding.endsWith("ies")) // aizmakt->aizsmacis, pieriesties->pieriesies
			partType = TValues.PARTICIPLE_IS;
		else if (patternEnding.endsWith("ot") || patternEnding.endsWith("oties")) // ievērojot
			partType = TValues.PARTICIPLE_OT;
		else
			System.err.println("Galotnei \"" + patternEnding + "\" nevar noteikt divdabja tipu!");
		return FormRestrRule.of(patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), null, patternEnding,
				new Tuple[]{Tuple.of(key, partType),
						Tuple.of(key, TValues.PARTICIPLE)});
	}*/
}
