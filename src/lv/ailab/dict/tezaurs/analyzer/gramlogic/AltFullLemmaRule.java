package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
public class AltFullLemmaRule extends StemSlotRule
{
	/**
	 * @param patternBegin	neeskepota teksta virkne, ar kuru grmatikai jāsākas,
	 *                      lai šis likums būtu piemērojams
	 * @param patternEnding	neeskepota teksta virkne, ar kuru grmatikai
	 *                      jāturpinās pēc lemmai specifiskās daļas, lai šis
	 *                      likums būtu piemērojams
	 * @param lemmaLogic	likuma "otrā puse" - lemmas nosacījumi, piešķiramās
	 *                      paradigmas un karodziņi, kā arī alternatīvās lemmas
	 *                      veidošanas dati un tai piešķiramie karodziņi
	 */
	public AltFullLemmaRule(
			String patternBegin, String patternEnding,
			StemSlotSubRule lemmaLogic)
	{
		super (patternBegin, patternEnding, lemmaLogic);
	}

	/**
	 * Ērummetode / alternatīvais konstruktors.
	 * @param patternBegin			neeskepota teksta virkne, ar kuru grmatikai
	 *                              jāsākas, lai šis likums būtu piemērojams
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
	 * @param altLemmaEnding		teksta virkne kuru izmantos kā izskaņu,
	 *                              veidojot papildus lemmu
	 * @param altLemmaParadigms		paradigmu ID, ko lieto papildus izveidotajai
	 *                              lemmai
	 * @param altLemmaFlags			šos karodziņus uzstāda papildu pamatformai,
	 *                              nevis pamatvārdam, ja gan gramatikas teksts,
	 *                              gan lemma atbilst attiecīgajiem šabloniem
	 * @return	izveidotais likums
	 */
	public static AltFullLemmaRule simple(
			String patternBegin, String patternEnding, String lemmaRestrict,
			int lemmaEndingCutLength, Set<Integer> paradigms,
			Set<Tuple<String, String>> positiveFlags,
			String altLemmaEnding, Set<Integer> altLemmaParadigms,
			Set<Tuple<String, String>> altLemmaFlags)
	{
		return new AltFullLemmaRule(patternBegin, patternEnding,
				new StemSlotSubRule(lemmaRestrict, lemmaEndingCutLength,
						paradigms, positiveFlags, altLemmaEnding,
						altLemmaParadigms, altLemmaFlags));
	}

	public static AltFullLemmaRule of(
			String patternBegin, String patternEnding, String lemmaEnding,
			int lemmaEndingCutLength, int paradigmId, Tuple<String,String>[] positiveFlags,
			String altLemmaEnding, int altParadigmId, Tuple<String,String>[] altLemmaFlags)
	{
		return simple(patternBegin, patternEnding, lemmaEnding,
				lemmaEndingCutLength, new HashSet<Integer>(){{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				altLemmaEnding, new HashSet<Integer>(){{add(altParadigmId);}},
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}
}
