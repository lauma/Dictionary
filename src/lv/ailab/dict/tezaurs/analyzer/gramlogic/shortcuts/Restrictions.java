package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.FormRestrRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
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
	 *                      	piemērot.
	 * @param restrFormFlags	karodziņi, ko pievienot ierobežojošajai
	 *                          vārdformai
	 * @return
	 */
	public static FormRestrRule anyOneForm(
			String patternBegin, String patternEnding, String lemmaEnding,
			Tuple<String, String>[] restrFormFlags)
	{
		Matcher m = Pattern.compile("([^., ]*)[,. ].*").matcher(patternEnding);
		String formEnding = "";
		if (!m.matches())
			System.err.println ("Šablonam \"lietv. nozīmē: _?_" + patternEnding
					+ "\" neizdodas noteikt vārdformas izskaņu.");
		else formEnding = m.group(1);
		return FormRestrRule.of(patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), null, formEnding, restrFormFlags);
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
	 * @param restrFormFlags	karodziņi, ko pievienot ierobežojošajai
	 *                          vārdformai
	 * @return
	 */
	public static FormRestrRule anyTwoForm(
			String patternBegin, String patternMiddle, String patternEnding,
			String lemmaEnding, Tuple<String, String>[] restrFormFlags)
	{
		Matcher m = Pattern.compile("([^., ]*)[,. ].*").matcher(patternMiddle);
		String formEnding = "";
		if (!m.matches())
			System.err.println ("Šablonam \"lietv. nozīmē: _?_" + patternEnding
					+ "\" neizdodas noteikt vārdformas izskaņu.");
		else formEnding = m.group(1);
		return FormRestrRule.of(patternBegin + " ", patternMiddle + " ", patternEnding,
				".*" + lemmaEnding,
				lemmaEnding.length(), null, formEnding, restrFormFlags);
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
				new Tuple[] {TFeatures.CONTAMINATION__NOUN});
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
				lemmaEnding, new Tuple[] {TFeatures.CONTAMINATION__NOUN});
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
}
