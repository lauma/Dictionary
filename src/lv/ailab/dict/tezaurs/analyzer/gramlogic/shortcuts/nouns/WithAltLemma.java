package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltEndingRule;
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
	 * 30. paradigmai pieliek arī noteiktās galotnes karodziņu.
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
		if (paradigmId == 30) return AltFullLemmaRule.of(
				patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), paradigmId,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.DEFINITE_ENDING, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				altLemmaEnding, paradigmId,
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR, TFeatures.DEFINITE_ENDING});
		else return AltFullLemmaRule.of(
				patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), paradigmId,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				altLemmaEnding, paradigmId,
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR});
	}

	/**
	 * Likums sieviešu dzimtes lietvārdiem ar šķirkļa vārdu daudzskaitlī un
	 * papildformu vienskaitlī, locīšana ar deklinācijai atbilstošajām mijām.
	 * Tiek pieņemts, ka no dotās lemmas beigām jānogriež tieši lemmaEnding
	 * apjoma daļa.
	 */
	public static AltFullLemmaRule nounPluralToSingularFem(
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
				patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), paradigmId,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				altLemmaEnding, paradigmId,
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR});
	}



	/*
	 * Likums sieviešu dzimtes lietvārdiem ar šķirkļa vārdu daudzskaitlī un
	 * papildformu vienskaitlī, locīšana bez deklinācijai atbilstošajām mijām.
	 * Tiek pieņemts, ka no dotās lemmas beigām jānogriež tieši lemmaEnding
	 * apjoma daļa.
	 */
/*	public static AltFullLemmaRule nounPluralToSingularFemNoChange(
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
				patternBegin + " ", patternEnding, ".*" + lemmaEnding,
				lemmaEnding.length(), paradigmId,
				new Tuple[]{TFeatures.NO_SOUNDCHANGE, TFeatures.GENDER__FEM, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				altLemmaEnding, paradigmId,
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR});
	}//*/

	/**
	 * Speciālgadījums lietvārdiem, kam pamatforma ir 1. deklinācijā un
	 * papildforma - 5. Pirmās deklinācijas paradigmu (1 vai 2) nosaka
	 * automātiski pēc lemmaEnd pēdējā simbola.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param altLemmaEnd	galotne, ko izmantos, veidojot alternatīvo lemmu
	 */
	public static AltEndingRule mascFirstDeclToFemFifthDecl(
			String patternText, String lemmaEnd, String altLemmaEnd)
	{
		int paradigm = 1;
		if (lemmaEnd.endsWith("s")) paradigm = 1;
		else if (lemmaEnd.endsWith("š")) paradigm = 2;
		else System.err.printf(
				"Neizdodas pēc galotnes \"%s\" noteikt paradigmu likumam \"%s\"\n",
				lemmaEnd, patternText);
		return AltEndingRule.of(patternText, ".*" + lemmaEnd,
				lemmaEnd.length(), paradigm,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN},
				altLemmaEnd, 9,
				new Tuple[]{TFeatures.ENTRYWORD__FEM, TFeatures.CHANGED_PARADIGM});
	}

	/**
	 * Speciālgadījums lietvārdiem, kam pamatforma ir 2. deklinācijā
	 * (3. paradigma) un papildforma - 5.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param altLemmaEnd	galotne, ko izmantos, veidojot alternatīvo lemmu
	 */
	public static AltEndingRule mascSeconDeclToFemFifthDecl(
			String patternText, String lemmaEnd, String altLemmaEnd)
	{
		return AltEndingRule.of(patternText, ".*" + lemmaEnd,
				lemmaEnd.length(), 3,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN},
				altLemmaEnd, 9,
				new Tuple[]{TFeatures.ENTRYWORD__FEM, TFeatures.CHANGED_PARADIGM});
	}
}
