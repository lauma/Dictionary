package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.THeader;
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
	 * @param restrCollector     kolekcija, kurā pielikt ierobežojumus gadījumā
	 *                           ja vismaz gramatika atbilst šim likumam
	 * @param altLemmasCollector kolekcija, kurā ielikt izveidotās papildus
	 *                           formas un tām raksturīgos karodziņus.
	 * @return jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 * daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int applyDirect(
			String gramText, String lemma, Set<Integer> paradigmCollector,
			Flags flagCollector, StructRestrs restrCollector,
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
					Lemma altLemma = TElementFactory.me().getNewLemma();
					altLemma.text = lemmaStub + curLemmaLogic.altWordEnding;
					Flags altParams = TElementFactory.me().getNewFlags();
					if (curLemmaLogic.altWordFlags != null)
						altParams.addAll(curLemmaLogic.altWordFlags);
					THeader tmp = TElementFactory.me().getNewHeader();
					StructRestrs altRestrs = TElementFactory.me().getNewStructRestrs();
					if (curLemmaLogic.altWordRestrictions != null)
						altRestrs.restrictions.addAll(curLemmaLogic.altWordRestrictions);

					tmp.reinitialize(
							TElementFactory.me(), altLemma, curLemmaLogic.altWordParadigms,
							altParams, altRestrs);
					altLemmasCollector.add(tmp);

					paradigmCollector.addAll(curLemmaLogic.paradigms);
					if (curLemmaLogic.positiveFlags != null)
						flagCollector.addAll(curLemmaLogic.positiveFlags);
					if (curLemmaLogic.positiveRestrictions != null)
						restrCollector.restrictions.addAll(curLemmaLogic.positiveRestrictions);

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
