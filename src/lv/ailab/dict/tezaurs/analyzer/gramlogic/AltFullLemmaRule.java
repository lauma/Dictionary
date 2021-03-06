package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.utils.Tuple;

import java.util.*;

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
	 * @param lemmaLogic	saraksts ar likuma "otrājām pusēm" - lemmas
	 *                      nosacījumi, piešķiramās paradigmas un karodziņi, kā
	 *                      arī alternatīvās lemmas veidošanas dati un tai
	 *                      piešķiramie karodziņi
	 */
	public AltFullLemmaRule(
			String patternBegin, String patternEnding,
			List<StemSlotSubRule> lemmaLogic)
	{
		super (patternBegin, patternEnding, lemmaLogic);
	}

	public static AltFullLemmaRule simple(String patternBegin, String patternEnding,
			StemSlotSubRule[] lemmaLogic)
	{
		return new AltFullLemmaRule(patternBegin, patternEnding,
				lemmaLogic == null ? null : Arrays.asList(lemmaLogic));
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
	 * @param positiveRestrictions	šos ierobežojumus uzstāda pamata ierobežojumu
	 *                              savācējam, ja gan gramatikas teksts, gan
	 *                              lemma atbilst attiecīgajiem šabloniem.
	 * @param altLemmaEnding		teksta virkne kuru izmantos kā izskaņu,
	 *                              veidojot papildus lemmu
	 * @param altLemmaParadigms		paradigmu ID, ko lieto papildus izveidotajai
	 *                              lemmai
	 * @param altLemmaFlags			šos karodziņus uzstāda papildu pamatformai,
	 *                              nevis pamatvārdam, ja gan gramatikas teksts,
	 *                              gan lemma atbilst attiecīgajiem šabloniem
	 * @param altWordRestrictions	šos ierobežojumus uzstāda papildu pamatformai,
	 *                              nevis pamatvārdam, ja gan gramatikas teksts,
	 *                              gan lemma atbilst attiecīgajiem šabloniem
	 * @return	izveidotais likums
	 */
	public static AltFullLemmaRule simple(
			String patternBegin, String patternEnding, String lemmaRestrict,
			int lemmaEndingCutLength, Set<Integer> paradigms,
			Set<Tuple<String, String>> positiveFlags,
			Set<StructRestrs.One> positiveRestrictions,
			String altLemmaEnding, Set<Integer> altLemmaParadigms,
			Set<Tuple<String, String>> altLemmaFlags,
			Set<StructRestrs.One> altWordRestrictions)
	{
		return new AltFullLemmaRule(patternBegin, patternEnding,
				new ArrayList<StemSlotSubRule>(){{add(new StemSlotSubRule(
						lemmaRestrict, lemmaEndingCutLength, paradigms,
						positiveFlags, positiveRestrictions, altLemmaEnding,
						altLemmaParadigms, altLemmaFlags, altWordRestrictions));}});
	}
	public static AltFullLemmaRule of(
			String patternBegin, String patternEnding, String lemmaEnding,
			int lemmaEndingCutLength, Integer[] paradigms, Tuple<String,String>[] positiveFlags,
			String altLemmaEnding, Integer[] altParadigms, Tuple<String,String>[] altLemmaFlags)
	{
		return simple(patternBegin, patternEnding, lemmaEnding,
				lemmaEndingCutLength,
				paradigms == null ? null : new HashSet<Integer>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				altLemmaEnding,
				altParadigms == null ? null : new HashSet<Integer>(Arrays.asList(altParadigms)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)),
				null);
	}

	public static AltFullLemmaRule of(
			String patternBegin, String patternEnding, String lemmaEnding,
			int lemmaEndingCutLength, int paradigmId, Tuple<String,String>[] positiveFlags,
			String altLemmaEnding, int altParadigmId, Tuple<String,String>[] altLemmaFlags)
	{
		return simple(patternBegin, patternEnding, lemmaEnding,
				lemmaEndingCutLength, new HashSet<Integer>(){{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				altLemmaEnding, new HashSet<Integer>(){{add(altParadigmId);}},
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)),
				null);
	}
}
