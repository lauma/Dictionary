package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.tezaurs.analyzer.struct.THeader;
import lv.ailab.dict.tezaurs.analyzer.struct.TLemma;
import lv.ailab.dict.utils.Tuple;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Likums, kas apstrādā gramatikas daļu, kas atbildīga par formu ierobežojumiem,
 * piemēram:
 * lietv. nozīmē: absolūtais, -ā, v.
 * lietv. nozīmē: aizturētais, -ā, v., aizturētā, -ās, s.
 * akuz.: acumirkli, apst. nozīmē.
 * bieži lok.: agrumā
 * ģen.: aizmugures
 * Izveidots 2016-10-26.
 * @author Lauma
 */
public class FormRestrRule extends StemSlotRule
{
	/**
	 * Ja lemmai specifiskā daļa šablonā atkārtojas 2x, tad šeit ir neeskepota
	 * teksta virkne, ar kuru grmatikai jāturpinās pēc pirmās un pirms otrās
	 * lemmai specifiskās daļas, lai šis likums būtu piemērojams. Citādi null.
	 */
	protected final String patternTextMiddle;

	/**
	 * @param patternBegin	neeskepota teksta virkne, ar kuru grmatikai jāsākas,
	 *                      lai šis likums būtu piemērojams
	 * @param patternMiddle ja lemmai specifiskā daļa šablonā atkārtojas 2x, tad
	 *                      šeit ir neeskepota teksta virkne, ar kuru grmatikai
	 *                      jāturpinās pēc pirmās un pirms otrās lemmai
	 *                      specifiskās daļas, lai šis likums būtu piemērojams,
	 *                      citādi null
	 * @param patternEnding	neeskepota teksta virkne, ar kuru grmatikai
	 *                      jāturpinās pēc lemmai specifiskās daļas, lai šis
	 *                      likums būtu piemērojams
	 * @param lemmaLogic	likuma "otrā puse" - lemmas nosacījumi, piešķiramās
	 *                      paradigmas un karodziņi, kā arī ierobežojošās formas
	 *                      veidošanas dati un tai piešķiramie karodziņi
	 */
	public FormRestrRule(
			String patternBegin, String patternMiddle, String patternEnding,
			List<StemSlotSubRule> lemmaLogic)
	{
		super(patternBegin, patternEnding, lemmaLogic);
		this.patternTextMiddle = patternMiddle;
	}

	public static FormRestrRule simple(String patternBegin, String patternMiddle,
			String patternEnding, StemSlotSubRule[] lemmaLogic)
	{
		return new FormRestrRule(patternBegin, patternMiddle, patternEnding,
				lemmaLogic == null ? null : Arrays.asList(lemmaLogic));
	}

	/**
	 * Ērummetode / alternatīvais konstruktors.
	 * @param patternBegin			neeskepota teksta virkne, ar kuru grmatikai
	 *                              jāsākas, lai šis likums būtu piemērojams
	 * @param patternMiddle 		ja lemmai specifiskā daļa šablonā atkārtojas
	 *                              2x, tad šeit ir neeskepota teksta virkne, ar
	 *                              kuru grmatikai jāturpinās pēc pirmās un
	 *                              pirms otrās lemmai specifiskās daļas, lai
	 *                              šis likums būtu piemērojams, citādi null
	 * @param patternEnding			neeskepota teksta virkne, ar kuru grmatikai
	 *                              jāturpinās pēc lemmai specifiskās daļas, lai
	 *                              šis likums būtu piemērojams
	 * @param lemmaRestrict			lai likums būtu piemērojams, lemmai jāatbilst
	 *                              šim šablonam
	 * @param lemmaEndingCutLength	simbolu skaits, kas tiks noņemts no dotās
	 *                              lemmas beigām, to izmantojot gramatikas
	 *                              šablona viedošanai
	 * @param paradigms				paradigmu ID, ko lieto, ja likums ir
	 *                              piemērojams (gan gramatikas teksts, gan
	 *                              lemma atbilst attiecīgajiem šabloniem)
	 * @param positiveFlags			šos karodziņus uzstāda pamata karodziņu
	 *                              savācējam, ja gan gramatikas teksts, gan
	 *                              lemma atbilst attiecīgajiem šabloniem.
	 * @param restrFormEnding		teksta virkne kuru izmantos kā izskaņu,
	 *                              veidojot ierobežojošo formu
	 * @param restrFormFlags			šos karodziņus uzstāda ierobežojošajai
	 *                              formai, nevis pamatvārdam, ja gan gramatikas
	 *                              teksts, gan lemma atbilst attiecīgajiem
	 *                              šabloniem
	 * @return	izveidotais likums
	 */
	public static FormRestrRule simple(String patternBegin, String patternMiddle,
			String patternEnding, String lemmaRestrict,
			int lemmaEndingCutLength, Set<Integer> paradigms,
			Set<Tuple<String, String>> positiveFlags,
			String restrFormEnding, Set<Tuple<String, String>> restrFormFlags)
	{
		return new FormRestrRule(patternBegin, patternMiddle, patternEnding,
				new ArrayList<StemSlotSubRule>(){{add(new StemSlotSubRule(
						lemmaRestrict, lemmaEndingCutLength, paradigms,
						positiveFlags, restrFormEnding, null, restrFormFlags));}});
	}

	public static FormRestrRule of(String patternBegin, String patternMiddle,
			String patternEnding, String lemmaRestrict,
			int lemmaEndingCutLength, Tuple<String,String>[] positiveFlags,
			String restrFormEnding, Tuple<String,String>[] restrFormFlags)
	{
		return FormRestrRule.simple(patternBegin, patternMiddle, patternEnding, lemmaRestrict,
				lemmaEndingCutLength, null,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				restrFormEnding,
				restrFormFlags == null ? null : new HashSet<>(Arrays.asList(restrFormFlags)));
	}

	public static FormRestrRule of(String patternBegin, String patternEnding, String lemmaRestrict,
			int lemmaEndingCutLength, Tuple<String,String>[] positiveFlags,
			String restrFormEnding, Tuple<String,String>[] restrFormFlags)
	{
		return FormRestrRule.simple(patternBegin, null, patternEnding, lemmaRestrict,
				lemmaEndingCutLength, null,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				restrFormEnding,
				restrFormFlags == null ? null : new HashSet<>(Arrays.asList(restrFormFlags)));
	}

	/**
	 * Likuma piemērošana pa tiešo, bez maģijas.
	 *
	 * @param gramText             	apstrādājamā gramatika
	 * @param lemma                	hederim, kurā atrodas gramatika, atbilstošā
	 *                             	lemma
	 * @param paradigmCollector    	kolekcija, kurā pielikt paradigmu gadījumā,
	 *                             	ja gramatika un lemma atbilst šim likumam
	 * @param flagCollector       	kolekcija, kurā pielikt karodziņus gadījumā,
	 *                             	ja vismaz gramatika atbilst šim likumam
	 * @param formRestrCollector	kolekcija, kurā ielikt izveidotās papildus
	 *                             	formas un tām raksturīgos karodziņus.
	 * @return jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 * daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int applyDirect(String gramText, String lemma,
			Set<Integer> paradigmCollector, Flags flagCollector,
			List<Header> formRestrCollector)
	{
		for (StemSlotSubRule curLemmaLogic : lemmaLogic)
			if (curLemmaLogic.lemmaRestrict.matcher(lemma).matches())
		{
			String lemmaStub = lemma.substring(0,
					lemma.length() - curLemmaLogic.lemmaEndingCutLength);
			String pattern = patternTextBegin;
			if (patternTextMiddle != null)
				pattern = pattern + lemmaStub + patternTextMiddle;
			pattern = pattern + lemmaStub + patternTextEnding;

			if (Pattern.compile("(\\Q" + pattern + "\\E)[;,.]?")
					.matcher(gramText).matches())
			{
				// Šis ir gadījums, kad pēc būtības ir viena lemma ar altlemmām.
				int newBegin = pattern.length();
				String patternToProcess = patternTextEnding;
				if (patternTextMiddle != null)
					patternToProcess = patternTextMiddle + lemmaStub + patternTextEnding;
				if (patternToProcess.startsWith(curLemmaLogic.altWordEnding))
					patternToProcess = patternToProcess.substring(
							curLemmaLogic.altWordEnding.length());
				while (patternToProcess.startsWith(".") || patternToProcess.startsWith(",")
						|| patternToProcess.startsWith(" "))
					patternToProcess = patternToProcess.substring(1);
				TLemma restrForm = new TLemma(lemmaStub + curLemmaLogic.altWordEnding);
				THeader restrHeader = new THeader(restrForm, patternToProcess);
				restrHeader.gram.freeText = null;
				if (curLemmaLogic.altWordFlags != null)
				{
					if (restrHeader.gram.flags == null)
						restrHeader.gram.flags = new Flags();
					restrHeader.gram.flags.addAll(curLemmaLogic.altWordFlags);
				}

				formRestrCollector.add(restrHeader);

				if (curLemmaLogic.paradigms != null)
					paradigmCollector.addAll(curLemmaLogic.paradigms);
				if (curLemmaLogic.positiveFlags != null)
					flagCollector.addAll(curLemmaLogic.positiveFlags);
				usageCount++;
				return newBegin;
			}
		}
		return -1;
	}


	/**
	 * Metode, kas ļauj dabūt likuma nosaukumu, kas ļautu šo likumu atšķirt no
	 * citiem. Iesakāmā realizācija satur likumā izmantoto šablonu un, ja vajag,
	 * arī galotņu nosacījumus, klases vārdu, utt.
	 *
	 * @return likuma vienkāršota reprezentācija, kas izmantojama diagnostikas
	 * izdrukās.
	 */
	@Override
	public String getStrReprezentation()
	{
		if (patternTextMiddle == null)
			return String.format("%s \"%s_?_%s\"",
				this.getClass().getSimpleName(), patternTextBegin, patternTextEnding);
		else
			return String.format("%s \"%s_?_%s_?_%s\"",
				this.getClass().getSimpleName(), patternTextBegin,
				patternTextMiddle, patternTextEnding);
	}
}
