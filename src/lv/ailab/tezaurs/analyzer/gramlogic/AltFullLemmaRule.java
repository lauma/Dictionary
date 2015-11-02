package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.struct.Flags;
import lv.ailab.tezaurs.analyzer.struct.Lemma;
import lv.ailab.tezaurs.utils.MappingSet;
import lv.ailab.tezaurs.utils.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Likumi gramatikām, kas satur pilnu alternatīvo lemmu, nevis tikai galdotnes,
 * kas to norāda, piemēram, šķirklī dižtauriņi: -ņu, vsk. dižtauriņš, -ņa, v.
 * Izveidots 2015-10-26.
 *
 * @author Lauma
 */
public class AltFullLemmaRule implements AltLemmaRule
{
	/**
	 * Neeskepota teksta virkne, ar kuru grmatikai jāsākas, lai šis likums būtu
	 * piemērojams.
	 */
	protected final String patternTextBegin;
	/**
	 * Neeskepota teksta virkne, ar kuru grmatikai jāturpinās pēc lemmai
	 * specifiskās daļas, lai šis likums būtu piemērojams.
	 */
	protected final String patternTextEnding;

	/**
	 * Likuma "otrā puse" - lemmas nosacījumi, piešķiramās paradigmas un
	 * karodziņi, kā arī alternatīvās lemmas veidošanas dati un tai piešķiramie
	 * karodziņi.
	 */
	protected final AltLemmaSubRule lemmaLogic;


	public AltFullLemmaRule(
			String patternBegin, String patternEnding, String lemmaRestrict,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigm,
			int altLemmaParadigm, Set<Tuple<Keys, String>> positiveFlags,
			Set<Tuple<Keys, String>> altLemmaFlags)
	{
		this.patternTextBegin = patternBegin;
		this.patternTextEnding = patternEnding;
		this.lemmaLogic = new AltLemmaSubRule(lemmaRestrict,
				new HashSet<Integer>(){{add(paradigm);}}, positiveFlags,
				lemmaEndingCutLength, altLemmaEnding, altLemmaParadigm, altLemmaFlags);
	}

	public static AltFullLemmaRule of(
			String patternBegin, String patternEnding, String lemmaEnding,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigmId,
			int altParadigmId, Tuple<Keys,String>[] positiveFlags,
			Tuple<Keys,String>[] altLemmaFlags)
	{
		return new AltFullLemmaRule(patternBegin, patternEnding, lemmaEnding,
				altLemmaEnding, lemmaEndingCutLength, paradigmId, altParadigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}


	/**
	 * Speciālgadījums vīriešu dzimtes lietvārdiem ar šķirkļa vārdu daudzskaitlī
	 * un papildformu vienskaitlī. Tiek pieņemts, ka no dotās lemmas beigām
	 * jānogriež tieši lemmaEnding apjoma daļa.
	 */
	public static AltFullLemmaRule nounPluralToSingularMasc(
			String patternBegin, String patternEnding, String lemmaEnding,
			int paradigmId)
	{
		Matcher m = Pattern.compile("([^,;]+)[,;].*").matcher(patternEnding);
		//System.out.println(patternEnding);
		String altLemmaEnding = "";
		if (!m.find())
			System.err.printf(
					"Neizdevās iegūt galotni papildformu likumam \"%s _?_%s\"\n",
					patternBegin, patternEnding);
		else altLemmaEnding = m.group(1);

		return AltFullLemmaRule.of(
				patternBegin + " ", patternEnding, ".*" + lemmaEnding, altLemmaEnding,
				lemmaEnding.length(), paradigmId, paradigmId,
				new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN, Features.ENTRYWORD__PLURAL},
				new Tuple[]{Features.ENTRYWORD__SINGULAR});
	}




	/**
	 * Likuma piemērošana.
	 *
	 * @param gramText           apstrādājamā gramatika
	 * @param lemma              hederim, kurā atrodas gramatika, atbilstošā
	 *                           lemma
	 * @param paradigmCollector  kolekcija, kurā pielikt paradigmu gadījumā,
	 *                           ja gramatika un lemma atbilst šim likumam
	 * @param flagCollector      kolekcija, kurā pielikt karodziņus gadījumā,
	 *                           ja vismaz gramatika atbilst šim likumam
	 * @param altLemmasCollector kolekcija, kurā ielikt izveidotās papildus
	 *                           formas un tām raksturīgos karodziņus.
	 * @return jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 * daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int apply(String gramText, String lemma,
			HashSet<Integer> paradigmCollector, Flags flagCollector,
			MappingSet<Integer, Tuple<Lemma, Flags>> altLemmasCollector)
	{
		if (!lemmaLogic.lemmaRestrict.matcher(lemma).matches()) return -1;
		int newBegin = -1;

		String lemmaStub = lemma.substring(0, lemma.length() - lemmaLogic.lemmaEndingCutLength);
		String pattern = patternTextBegin + lemmaStub + patternTextEnding;
		if (gramText.startsWith(pattern))
		{
			newBegin = pattern.length();
			Lemma altLemma = new Lemma(lemmaStub + lemmaLogic.altLemmaEnding);
			Flags altParams = new Flags();
			if (lemmaLogic.altLemmaFlags != null)
				for (Tuple<Keys, String> t : lemmaLogic.altLemmaFlags) altParams.add(t);
			altLemmasCollector.put(lemmaLogic.altLemmaParadigm, new Tuple<>(altLemma, altParams));

			paradigmCollector.addAll(lemmaLogic.paradigms);
			if (lemmaLogic.positiveFlags != null)
				for (Tuple<Keys, String> t : lemmaLogic.positiveFlags) flagCollector.add(t);
			return newBegin;
		}
		else return -1;
	}
}
