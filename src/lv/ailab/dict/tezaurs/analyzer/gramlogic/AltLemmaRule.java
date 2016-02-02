package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Flags;

import java.util.List;
import java.util.Set;

/**
 * Gramatiku apstrādes likums, kurš var ne tikai radīt jaunus karodziņus un
 * paradigmas, bet arī jaunas formas un visu ar to saistīto.
 * Izveidots 2015-10-26.
 *
 * @author Lauma
 */
public interface AltLemmaRule
{
	/**
	 * Likuma piemērošana.
	 * @param gramText          	apstrādājamā gramatika
	 * @param lemma             	hederim, kurā atrodas gramatika, atbilstošā
	 *                          	lemma
	 * @param paradigmCollector 	kolekcija, kurā pielikt paradigmu gadījumā,
	 *                              ja gramatika un lemma atbilst šim likumam
	 * @param flagCollector     	kolekcija, kurā pielikt karodziņus gadījumā,
	 *                              ja vismaz gramatika atbilst šim likumam
	 * @param altLemmasCollector	kolekcija, kurā ielikt izveidotās papildus
	 *                              formas un tām raksturīgos karodziņus.
	 * @return  jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	public int apply(String gramText, String lemma,
			Set<Integer> paradigmCollector, Flags flagCollector,
			List<Header> altLemmasCollector);
}
