package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.TLemma;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Likumi gramatikām, kas satur pilnu alternatīvo lemmu, nevis tikai galdotnes,
 * kas to norāda, piemēram, šķirklī dižtauriņi: -ņu, vsk. dižtauriņš, -ņa, v.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * Izveidots 2015-10-26.
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
			int altLemmaParadigm, Set<Tuple<TKeys, String>> positiveFlags,
			Set<Tuple<TKeys, String>> altLemmaFlags)
	{
		this.patternTextBegin = patternBegin;
		this.patternTextEnding = patternEnding;
		this.lemmaLogic = new AltLemmaSubRule(lemmaRestrict,
				new HashSet<Integer>(){{add(paradigm);}}, positiveFlags,
				lemmaEndingCutLength, altLemmaEnding,
				new HashSet<Integer>(){{add(altLemmaParadigm);}}, altLemmaFlags);
	}

	public static AltFullLemmaRule of(
			String patternBegin, String patternEnding, String lemmaEnding,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigmId,
			int altParadigmId, Tuple<TKeys,String>[] positiveFlags,
			Tuple<TKeys,String>[] altLemmaFlags)
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
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN, TFeatures.ENTRYWORD__PLURAL},
				new Tuple[]{TFeatures.ENTRYWORD__SINGULAR});
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
			Set<Integer> paradigmCollector, Flags flagCollector,
			List<Header> altLemmasCollector)
	{
		if (!lemmaLogic.lemmaRestrict.matcher(lemma).matches()) return -1;
		int newBegin = -1;

		String lemmaStub = lemma.substring(0, lemma.length() - lemmaLogic.lemmaEndingCutLength);
		String pattern = patternTextBegin + lemmaStub + patternTextEnding;
		if (gramText.startsWith(pattern))
		{
			newBegin = pattern.length();
			Lemma altLemma = new TLemma(lemmaStub + lemmaLogic.altLemmaEnding);
			Flags altParams = new Flags();
			if (lemmaLogic.altLemmaFlags != null)
				for (Tuple<TKeys, String> t : lemmaLogic.altLemmaFlags) altParams.add(t);
			altLemmasCollector.add(new Header(altLemma, lemmaLogic.altLemmaParadigms, altParams));

			paradigmCollector.addAll(lemmaLogic.paradigms);
			if (lemmaLogic.positiveFlags != null)
				for (Tuple<TKeys, String> t : lemmaLogic.positiveFlags) flagCollector.add(t);
			return newBegin;
		}
		else return -1;
	}
}
