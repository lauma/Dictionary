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
 * Likumi gramatikām formā "gramatikas teksts lemmas_sākums gramatikas teksts",
 * piemēram, šķirklī dižtauriņi: -ņu, vsk. dižtauriņš, -ņa, v.
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
	 * Neeskepota teksta virkne, ar kuru jābeidzas lemmai, lai likums būtu
	 * piemērojams.
	 */
	protected final String lemmaRestrict;

	/**
	 * Simbolu skaits, kas tiks noņemts no dotās lemmas beigām, to izmantojot
	 * gramatikas šablona viedošanai.
	 */
	protected final int lemmaEndingCutLength;

	/**
	 * Teksta virkne kuru izmantos kā izskaņu, veidojot papildus lemmu.
	 */
	protected final String altLemmaEnding;

	/**
	 * Paradigmas ID, ko lieto, ja likums ir piemērojams (gan gramatikas teksts,
	 * gan lemma atbilst attiecīgajiem šabloniem).
	 */
	protected final int paradigmId;
	/**
	 * Šos karodziņus uzstāda pamata karodziņu savācējam, ja gan gramatikas
	 * teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<Keys,String>> positiveFlags;
	/**
	 * Šos karodziņus uzstāda papildu pamatformai, nevis pamatvārdam, ja gan
	 * gramatikas teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<Keys,String>> altLemmaFlags;


	public AltFullLemmaRule(
			String patternBegin, String patternEnding, String lemmaRestrict,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigmId,
			Set<Tuple<Keys, String>> positiveFlags,
			Set<Tuple<Keys, String>> altLemmaFlags)
	{
		this.patternTextBegin = patternBegin;
		this.patternTextEnding = patternEnding;
		this.lemmaRestrict = lemmaRestrict;
		this.lemmaEndingCutLength = lemmaEndingCutLength;
		this.altLemmaEnding = altLemmaEnding;
		this.paradigmId = paradigmId;
		this.positiveFlags = positiveFlags == null? null : Collections.unmodifiableSet(positiveFlags);
		this.altLemmaFlags = altLemmaFlags == null? null : Collections.unmodifiableSet(altLemmaFlags);
	}

	public static AltFullLemmaRule of(
			String patternBegin, String patternEnding, String lemmaEnding,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigmId,
			Tuple<Keys,String>[] positiveFlags,
			Tuple<Keys,String>[] altLemmaFlags)
	{
		return new AltFullLemmaRule(patternBegin, patternEnding, lemmaEnding,
				altLemmaEnding, lemmaEndingCutLength, paradigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}


	/**
	 * Speciālgadījums vīriešu dzimtes lietvārdiem ar šķirkļa vārdu daudzskaitlī
	 * un papildformu vienskaitlī. Tiek pieņemts, ka no dotās lemmas beigām
	 * jānogriež tieši lemmaEnding apjoma daļa.
	 */
	public static AltFullLemmaRule pluralToSingularMasc(
			String patternBegin, String patternEnding, String lemmaEnding, int paradigmId)
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
				patternBegin + " ", patternEnding, lemmaEnding, altLemmaEnding,
				lemmaEnding.length(), paradigmId,
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
		if (!lemma.endsWith(lemmaRestrict)) return -1;
		int newBegin = -1;

		String lemmaStub = lemma.substring(0, lemma.length() - lemmaEndingCutLength);
		String pattern = patternTextBegin + lemmaStub + patternTextEnding;
		if (gramText.startsWith(pattern))
		{
			newBegin = pattern.length();
			Lemma altLemma = new Lemma(lemmaStub + altLemmaEnding);
			Flags altParams = new Flags();
			if (altLemmaFlags != null)
				for (Tuple<Keys, String> t : altLemmaFlags) altParams.add(t);
			altLemmasCollector.put(paradigmId, new Tuple<>(altLemma, altParams));

			paradigmCollector.add(paradigmId);
			if (positiveFlags != null)
				for (Tuple<Keys, String> t : positiveFlags) flagCollector.add(t);
			return newBegin;
		}
		else return -1;
	}
}
