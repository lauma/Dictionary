package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.struct.Flags;
import java.util.HashSet;

/**
 * Gramatiku apstrādes likums.
 * @author Lauma
 *
 */
public interface Rule {

	/**
	 * Piemērot likumu bez papildus maģijas.
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @return  jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	int applyDirect (
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			Flags flagCollector);
	
	/**
	 * Piemērot likumu tā, ka patternText defises ir neobligātas.
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @return  jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	int applyOptHyphens(
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			Flags flagCollector);
	
	
}
