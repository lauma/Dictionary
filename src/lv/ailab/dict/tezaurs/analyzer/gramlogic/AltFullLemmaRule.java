package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
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

	/**
	 * Skaitītājs, kas norāda, cik reižu likums ir ticis lietots (applyDirect).
	 */
	protected int usageCount = 0;

	public AltFullLemmaRule(
			String patternBegin, String patternEnding, String lemmaRestrict,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigm,
			int altLemmaParadigm, Set<Tuple<String, String>> positiveFlags,
			Set<Tuple<String, String>> altLemmaFlags)
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
			int altParadigmId, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] altLemmaFlags)
	{
		return new AltFullLemmaRule(patternBegin, patternEnding, lemmaEnding,
				altLemmaEnding, lemmaEndingCutLength, paradigmId, altParadigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
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
	public int applyDirect(String gramText, String lemma,
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
				for (Tuple<String, String> t : lemmaLogic.altLemmaFlags) altParams.add(t);
			altLemmasCollector.add(new Header(altLemma, lemmaLogic.altLemmaParadigms, altParams));

			paradigmCollector.addAll(lemmaLogic.paradigms);
			if (lemmaLogic.positiveFlags != null)
				for (Tuple<String, String> t : lemmaLogic.positiveFlags) flagCollector.add(t);
			if (newBegin > -1) usageCount++;
			return newBegin;
		}
		else return -1;
	}

	/**
	 * Cik reižu likums ir lietots?
	 * @return skaits, cik reižu likums ir lietots.
	 */
	@Override
	public int getUsageCount()
	{
		return usageCount;
	}

	/**
	 * Metode, kas ļauj dabūt likuma nosaukumu, kas ļautu šo likumu atšķirt no
	 * citiem.
	 * @return likuma vienkāršota reprezentācija, kas izmantojama diagnostikas
	 * izdrukās.
	 */
	@Override
	public String getStrReprezentation()
	{
		return String.format("%s \"%s_?_%s\"",
				this.getClass().getSimpleName(), patternTextBegin, patternTextEnding);
	}
}
