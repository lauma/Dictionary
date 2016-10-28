package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.tezaurs.analyzer.struct.THeader;
import lv.ailab.dict.tezaurs.analyzer.struct.TLemma;
import lv.ailab.dict.utils.Tuple;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created on 2016-10-26.
 *
 * @author Lauma
 */
public class StemSlotRule implements AdditionalHeaderRule
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
	 * Masīvs ar likuma "otrājām pusēm"  - lemmas nosacījumi, piešķiramās
	 * paradigmas un karodziņi, kā arī alternatīvās lemmas veidošanas dati un
	 * tai piešķiramie karodziņi.
	 */
	protected final List<StemSlotSubRule> lemmaLogic;

	/**
	 * Skaitītājs, kas norāda, cik reižu likums ir ticis lietots (applyDirect).
	 */
	protected int usageCount = 0;

	public StemSlotRule(
			String patternBegin, String patternEnding,
			List<StemSlotSubRule> lemmaLogic)
	{
		if (lemmaLogic == null)
			throw new IllegalArgumentException (
					"Nav paredzēts, ka " + getClass().getSimpleName()+
							" tiek viedots vispār bez lemmu nosacījumiem!");
		this.patternTextBegin = patternBegin;
		this.patternTextEnding = patternEnding;
		this.lemmaLogic = Collections.unmodifiableList(lemmaLogic);
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
		for (StemSlotSubRule curLemmaLogic : lemmaLogic)
			if (curLemmaLogic.lemmaRestrict.matcher(lemma).matches())
			{
				String lemmaStub = lemma.substring(0,
						lemma.length() - curLemmaLogic.lemmaEndingCutLength);
				String pattern = patternTextBegin + lemmaStub + patternTextEnding;
				if (Pattern.compile("(\\Q" + pattern + "\\E)([;,.].*)?")
						.matcher(gramText).matches())
				{
					int newBegin = pattern.length();
					TLemma altLemma = new TLemma(lemmaStub + curLemmaLogic.altWordEnding);
					Flags altParams = new Flags();
					if (curLemmaLogic.altWordFlags != null)
						altParams.addAll(curLemmaLogic.altWordFlags);
					altLemmasCollector.add(
							new THeader(altLemma, curLemmaLogic.altWordParadigms, altParams));

					paradigmCollector.addAll(curLemmaLogic.paradigms);
					if (curLemmaLogic.positiveFlags != null)
						for (Tuple<String, String> t : curLemmaLogic.positiveFlags)
							flagCollector.add(t);

					usageCount++;
					return newBegin;
				}
			}
		return -1;
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
