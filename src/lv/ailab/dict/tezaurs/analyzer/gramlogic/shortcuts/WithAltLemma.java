package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltFullLemmaRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ērtummetodes likumiem, kas rada papildlemmas.
 *
 * Izveidots 2016-10-25.
 * @author Lauma
 */
public final class WithAltLemma
{
	/**
	 * Likums vīriešu dzimtes lietvārdiem ar šķirkļa vārdu daudzskaitlī un
	 * papildformu vienskaitlī, locīšana ar deklinācijai atbilstošajām mijām.
	 * Tiek pieņemts, ka no dotās lemmas beigām jānogriež tieši lemmaEnding
	 * apjoma daļa.
	 */
	public static AltFullLemmaRule nounPluralToSingularMascStd(
			String patternBegin, String patternEnding, String lemmaEnding,
			int paradigmId)
	{
		Matcher m = Pattern.compile("([^,;]+)([,;].*)?").matcher(patternEnding);
		String altLemmaEnding = "";
		if (!m.find())
			System.err.printf(
					"Neizdevās iegūt galotni papildformu likumam \"%s _?_%s\"\n",
					patternBegin, patternEnding);
		else altLemmaEnding = m.group(1);

		return AltFullLemmaRule.of(
				patternBegin + " ", patternEnding, ".*" + lemmaEnding, altLemmaEnding,
				lemmaEnding.length(), paradigmId, paradigmId,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR});
	}

	/**
	 * Likums sieviešu dzimtes lietvārdiem ar šķirkļa vārdu daudzskaitlī un
	 * papildformu vienskaitlī, locīšana ar deklinācijai atbilstošajām mijām.
	 * Tiek pieņemts, ka no dotās lemmas beigām jānogriež tieši lemmaEnding
	 * apjoma daļa.
	 */
	public static AltFullLemmaRule nounPluralToSingularFemStd(
			String patternBegin, String patternEnding, String lemmaEnding,
			int paradigmId)
	{
		Matcher m = Pattern.compile("([^,;]+)([,;].*)?").matcher(patternEnding);
		String altLemmaEnding = "";
		if (!m.find())
			System.err.printf(
					"Neizdevās iegūt galotni papildformu likumam \"%s _?_%s\"\n",
					patternBegin, patternEnding);
		else altLemmaEnding = m.group(1);

		return AltFullLemmaRule.of(
				patternBegin + " ", patternEnding, ".*" + lemmaEnding, altLemmaEnding,
				lemmaEnding.length(), paradigmId, paradigmId,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR});
	}

	/**
	 * Likums sieviešu dzimtes lietvārdiem ar šķirkļa vārdu daudzskaitlī un
	 * papildformu vienskaitlī, locīšana bez deklinācijai atbilstošajām mijām.
	 * Tiek pieņemts, ka no dotās lemmas beigām jānogriež tieši lemmaEnding
	 * apjoma daļa.
	 */
	public static AltFullLemmaRule nounPluralToSingularFemNoChange(
			String patternBegin, String patternEnding, String lemmaEnding,
			int paradigmId)
	{
		Matcher m = Pattern.compile("([^,;]+)([,;].*)?").matcher(patternEnding);
		String altLemmaEnding = "";
		if (!m.find())
			System.err.printf(
					"Neizdevās iegūt galotni papildformu likumam \"%s _?_%s\"\n",
					patternBegin, patternEnding);
		else altLemmaEnding = m.group(1);

		return AltFullLemmaRule.of(
				patternBegin + " ", patternEnding, ".*" + lemmaEnding, altLemmaEnding,
				lemmaEnding.length(), paradigmId, paradigmId,
				new Tuple[]{TFeatures.NO_SOUNDCHANGE, TFeatures.GENDER__FEM, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR});
	}
}
