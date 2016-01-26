package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Values;
import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.struct.Flags;
import lv.ailab.dict.utils.Tuple;

import java.util.*;

/**
 * Pirmās konjugācijas likumi, kas sevī ietver celmu iegūšanu un uzstādīšanu.
 * Šajā klasē apvienots saskarne likumiem, kam ir:
 * a) gan visu personu formu šablons, gan tikai trešās personas formu šablons,
 * b) tikai trešās personas formu šablons,
 * c) visu personu formu šablons, no kura kaut kādu iemeslu dēļ nav iespējams
 * atvasināt trešās personas formu šablonu (sarežģītas paralēlformas).
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
 *
 * Izveidots 2015-10-16.
 * @author Lauma
 */
public class FirstConjRule implements Rule
{
	protected BaseRule allPersonRule;
	protected ThirdPersVerbRule thirdPersonRule;
	// Celmus glabā atsevišķi, nevis jau kā gatavus karodziņus tāpēc, ka
	// karodziņam jāsatur arī "priedēklis", bet šobrīd tas nav zināms.
	/**
	 * Nenoteiksmes celmi - katrā likuma trigerošanās reizē lietos tikai VIENU -
	 * to, kurš tajā reizē atbildīs lemmai.
	 */
	protected List<String> infinityStems;
	/**
	 * Tagadnes celmi, kas izsecināmi no attiecīgā gramatikas šablona - katrā
	 * likuma trigerošanās riezē pielietos VISUS.
	 */
	protected List<String> presentStems;
	/**
	 * Pagātnes celmi, kas izsecināmi no attiecīgā gramatikas šablona - katrā
	 * likuma trigerošanās riezē pielietos VISUS.
	 */
	protected List<String> pastStems;

	/**
	 * Konstruktors pilnam likumam.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      var būt null, ja 3. personas dormas nevar atvasīnāt
	 *                      no šī likuma automātiski
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem (Vārdšķira =
	 *                      Darbības vārds nav obligāts)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 * @param infinityStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      vairākām nenoteiksmēm
	 * @param presentStems	tagadnes celmi
	 * @param pastStems		pagātnes celmi
	 */
	public FirstConjRule (String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Set<Tuple<Keys,String>> positiveFlags, Set<Tuple<Keys,String>> alwaysFlags,
			List<String> infinityStems, List<String> presentStems, List<String> pastStems)
	{
		HashSet<Tuple<Keys,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(Tuple.of(Keys.POS, Values.VERB.s));
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<Keys,String>> alwaysFlagsSet = alwaysFlags == null ?
				null : new HashSet<>(alwaysFlags);

		this.infinityStems = Collections.unmodifiableList(infinityStems);
		this.presentStems = Collections.unmodifiableList(presentStems);
		this.pastStems = Collections.unmodifiableList(pastStems);

		if (patternBegin != null && patternBegin.trim().length() > 0 &&
				patternEnd != null && patternEnd.trim().length() > 0)
		{
			String begin = patternBegin.trim();
			String end = patternEnd.trim();
			String allPersonPattern = begin + " " + end;
			String thirdPersonPattern;
			if (end.endsWith("u"))
				thirdPersonPattern = end.substring(0, end.length()-1) + "a";
			else if (end.endsWith("os"))
				thirdPersonPattern = end.substring(0, end.length()-2) + "ās";
			else
			{
				System.err.printf("Neizdevās izveidot \"parasti 3. pers.\" likumu gramatikas šablonam \"%s\"\n", allPersonPattern);
				thirdPersonPattern = allPersonPattern;
			}

			allPersonRule = BaseRule.simple(allPersonPattern,
					".*" + lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
			thirdPersonRule = new ThirdPersVerbRule(thirdPersonPattern,
					lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
		} else if (patternEnd != null && patternEnd.trim().length() > 0)
		{
			allPersonRule = null;
			thirdPersonRule = new ThirdPersVerbRule(patternEnd,
				lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
		} else if (patternBegin != null && patternBegin.trim().length() > 0)
		{
			allPersonRule = BaseRule.simple(patternBegin,
					".*" + lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
			thirdPersonRule = null;
		}
		else
			System.err.printf("Veidojot 1. konjugācijas likumu \"%s\", nav norādīti šabloni\n", lemmaEnd);

	}

	/**
	 * Konstruktors likumam, kam ir tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem (Vārdšķira =
	 *                      Darbības vārds nav obligāts)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 * @param infinityStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      vairākām nenoteiksmēm
	 * @param presentStems	tagadnes celmi
	 * @param pastStems		pagātnes celmi
	 */
	public FirstConjRule (String patternEnd, String lemmaEnd, int paradigmId,
			Set<Tuple<Keys,String>> positiveFlags, Set<Tuple<Keys,String>> alwaysFlags,
			List<String> infinityStems, List<String> presentStems, List<String> pastStems)
	{
		HashSet<Tuple<Keys,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(Tuple.of(Keys.POS, Values.VERB.s));
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<Keys,String>> alwaysFlagsSet = alwaysFlags == null ?
				null : new HashSet<>(alwaysFlags);

		this.infinityStems = Collections.unmodifiableList(infinityStems);
		this.presentStems = Collections.unmodifiableList(presentStems);
		this.pastStems = Collections.unmodifiableList(pastStems);

		allPersonRule = null;
		thirdPersonRule = new ThirdPersVerbRule(patternEnd,
				lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
	}

	/**
	 * Konstruktors pilnam likumam.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3. personas
	 *                      formām
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      var būt null, ja 3. personas dormas nevar atvasīnāt
	 *                      no šī likuma automātiski
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem (Vārdšķira =
	 *                      Darbības vārds nav obligāts)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 * @param infinityStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      vairākām nenoteiksmēm
	 * @param presentStems	tagadnes celmi
	 * @param pastStems		pagātnes celmi
	 */
	public static FirstConjRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Tuple<Keys,String>[] positiveFlags, Tuple<Keys,String>[] alwaysFlags,
			String[] infinityStems, String[] presentStems, String[] pastStems)
	{
		return new FirstConjRule(patternBegin, patternEnd, lemmaEnd, paradigmId,
			positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
			alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
			infinityStems == null ? null : new ArrayList<>(Arrays.asList(infinityStems)),
			presentStems == null ? null : new ArrayList<>(Arrays.asList(presentStems)),
			pastStems == null ? null : new ArrayList<>(Arrays.asList(pastStems)));
	}

	/**
	 * Konstruktors likumam, kam ir tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem (Vārdšķira =
	 *                     Darbības vārds nav obligāts)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam
	 * @param infinityStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      vairākām nenoteiksmēm
	 * @param presentStems	tagadnes celmi
	 * @param pastStems		pagātnes celmi
	 */
	public static FirstConjRule of(String patternEnd, String lemmaEnd, int paradigmId,
			Tuple<Keys,String>[] positiveFlags, Tuple<Keys,String>[] alwaysFlags,
			String[] infinityStems, String[] presentStems, String[] pastStems)
	{
		return new FirstConjRule(patternEnd, lemmaEnd, paradigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				infinityStems == null ? null : new ArrayList<>(Arrays.asList(infinityStems)),
				presentStems == null ? null : new ArrayList<>(Arrays.asList(presentStems)),
				pastStems == null ? null : new ArrayList<>(Arrays.asList(pastStems)));
	}
	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 */
	public static FirstConjRule direct(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām un nenoteiksmes homoformām, tikai 3. personas formas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 */
	public static FirstConjRule direct3Pers(
			String patternEnd, String lemmaEnd)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām, bet ar  nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return RegularVerbRule ar paradigmu 15 un karodziņiem
	 */
	public static FirstConjRule directHomof(
			String patternBegin, String patternEnd, String lemmaEnd, String inflectAs)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, inflectAs), Features.INFINITIVE_HOMOFORMS},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām, bet ar nenoteiksmes homoformām, tikai 3 personas formas
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                  	bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return ThirdPersVerbRule ar paradigmu 15 un karodziņiem
	 */
	public static FirstConjRule direct3PersHomof(
			String patternEnd, String lemmaEnd, String inflectAs)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, inflectAs), Features.INFINITIVE_HOMOFORMS},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 15 un karodziņiem, ka tas ir darbības
	 * 		   vārds ar paralēlajām formām
	 * TODO extract stems
	 */
	public static FirstConjRule directAllPersParallel(
			String patternText, String lemmaEnd)
	{
		Tuple<ArrayList<String>, ArrayList<String>> stems =
				extractPPStemsAllPersParallel(patternText);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternText))
			posFlags = new Tuple[] {Features.PARALLEL_FORMS, Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {Features.PARALLEL_FORMS, Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\""), Features.ORIGINAL_NEEDED};
		return FirstConjRule.of(patternText, null, lemmaEnd, 15, posFlags,
				null, new String[] {extractInfinityStem(lemmaEnd)},
				stems.first.toArray(new String[stems.first.size()]),
				stems.second.toArray(new String[stems.second.size()]));

	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 */
	public static FirstConjRule refl(String patternBegin, String patternEnd,
			String lemmaEnd)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnds		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 */
	public static FirstConjRule reflMultiLemma(String patternBegin, String patternEnd,
			String[] lemmaEnds)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		String lemmaEnd = "(" + String.join("|",lemmaEnds) + ")";
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, extractInfinityStems(lemmaEnds),
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdamm bez
	 * paralēlajām formām un nenoteiksmes homoformām, tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņu par locīšanu
	 * TODO add IDs from lexicon
	 */
	public static FirstConjRule refl3Pers(
			String patternEnd, String lemmaEnd)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, bet ar nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 */
	public static FirstConjRule reflHomof(
			String patternBegin, String patternEnd, String lemmaEnd, String inflectAs)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, inflectAs), Features.INFINITIVE_HOMOFORMS},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, bet ar nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                  	bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņiem
	 */
	public static FirstConjRule refl3PersHomof(
			String patternEnd, String lemmaEnd, String inflectAs)
	{
		Tuple<String, String> stems = extractPPStemsSimple(patternEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, inflectAs), Features.INFINITIVE_HOMOFORMS},
				null, new String[] {extractInfinityStem(lemmaEnd)},
				new String[] {stems.first}, new String[] {stems.second});
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņiem
	 * TODO add IDs from lexicon
	 */
	public static FirstConjRule refl3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		Tuple<ArrayList<String>, ArrayList<String>> stems =
				extractPPStemsThirdPersParallel(patternEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags = new Tuple[] {Features.PARALLEL_FORMS, Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {Features.PARALLEL_FORMS, Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\""), Features.ORIGINAL_NEEDED};
		return FirstConjRule.of(patternEnd, lemmaEnd, 18, posFlags,
				null, new String[] {extractInfinityStem(lemmaEnd)},
				stems.first.toArray(new String[stems.first.size()]),
				stems.second.toArray(new String[stems.second.size()]));

	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 18 un karodziņiem, ka tas ir darbības
	 * 		   vārds ar paralēlajām formām
	 */
	public static FirstConjRule reflAllPersParallel(
			String patternText, String lemmaEnd)
	{
		Tuple<ArrayList<String>, ArrayList<String>> stems =
				extractPPStemsAllPersParallel(patternText);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternText))
			posFlags = new Tuple[] {Features.PARALLEL_FORMS, Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {Features.PARALLEL_FORMS, Tuple.of(Keys.INFLECT_AS, "\"" + lemmaEnd + "\""), Features.ORIGINAL_NEEDED};
		return FirstConjRule.of(patternText, null, lemmaEnd, 18, posFlags,
				null, new String[] {extractInfinityStem(lemmaEnd)},
				stems.first.toArray(new String[stems.first.size()]),
				stems.second.toArray(new String[stems.second.size()]));
	}

	/**
	 * Palīgmetode, kas izvelk nenoteiksmes celmu no virknes ar ko jābeidzas
	 * lemmai.
	 */
	protected static String extractInfinityStem(String lemmaEnd)
	{
		String result = lemmaEnd;
		if (result.startsWith(".*")) result = result.substring(2);
		if (lemmaEnd.endsWith("t")) result = result.substring(0, result.length()-1);
		else if (lemmaEnd.endsWith("ties")) result = result.substring(0, result.length() - 4);
		else System.err.printf("Problēma, veidojot nenoteiksmes celmu verba likumam \"%s\"\n", lemmaEnd);
		return result;
	}

	/**
	 * Palīgmetode, kas izvelk nenoteiksmes celmus no katras virknes ar ko
	 * jābeidzas lemmai.
	 */
	protected static String[] extractInfinityStems(String[] lemmaEnds)
	{
		return Arrays.asList(lemmaEnds).stream().map(s -> extractInfinityStem(s))
				.toArray(size -> new String[size]);

	}

	/**
	 * Palīgmetode, kas no verba gramatikas paterna otrās (vienmer esošās) daļas
	 * izvelk tagadnes un pagātnes celmus, pieņemot, ka nav paralēlformu.
	 * @return Pārītis, kur pirmais celms ir tagadnes un otrais - pagātnes.
	 * TODO - vai šis ir extractPPStemsThirdPersParallel() speciālgadījumam?
	 */
	protected static Tuple<String,String> extractPPStemsSimple(String patternEnd)
	{
		String[] parts = patternEnd.split("[,;]");
		if (parts.length < 2)
			System.err.printf("Neizdodas izveidot tagadnes un pagātnes celmus verba šablonam \"%s\"\n", patternEnd);

		String thirdPers = parts[0].trim();
		if (thirdPers.startsWith("-")) thirdPers = thirdPers.substring(1);
		if (thirdPers.endsWith("as")) thirdPers = thirdPers.substring(0, thirdPers.length()-2);

		String past = parts[1].trim();
		if (past.startsWith("pag.")) past = past.substring(4).trim();
		if (past.startsWith("-")) past = past.substring(1).trim();
		if (past.endsWith("u") || past.endsWith("a")) past = past.substring(0, past.length()-1);
		else if (past.endsWith("os") || past.endsWith("ās")) past = past.substring(0, past.length()-2);
		else System.err.printf("Problēma, veidojot pagātnes celmu verba likumam \"%s\"\n", patternEnd);

		return Tuple.of(thirdPers, past);
	}

	/**
	 * Palīgmetode, kas no verba gramatikas paterna tikai 3. personai izvelk
	 * tagadnes un pagātnes celmus, pieņemot, ka likums ir formā
	 * -?tagcelms, arī -?tagcelms[,;] pag. -?pagcelms, arī -?pagcelms
	 * (viena vai otra "arī" daļa var nebūt).
	 * @return Pārītis, kur pirmais saraksts ir tagadnes celmu sarakst un otrais
	 * - pagātnes.
	 */
	protected static Tuple<ArrayList<String>,ArrayList<String>> extractPPStemsThirdPersParallel(
			String patternEnd)
	{
		ArrayList<String> presents = new ArrayList<>();
		ArrayList<String> pasts = new ArrayList<>();

		String[] parts = patternEnd.split("[,;] pag\\.");
		if (parts.length != 2)
			System.err.printf("Neizdodas izveidot tagadnes un pagātnes celmus verba šablonam \"%s\"\n", patternEnd);

		String[] thirdPersText = parts[0].trim().split(", arī ");
		for (String present : thirdPersText)
		{
			present = present.trim();
			if (present.startsWith("-")) present = present.substring(1);
			if (present.endsWith("as")) present = present.substring(0, present.length()-2);
			presents.add(present);
		}

		String[] pastText = parts[1].trim().split(", (arī|retāk) ");
		for (String past : pastText)
		{
			boolean good = true;
			if (past.startsWith("-")) past = past.substring(1).trim();
			if (past.endsWith("u") || past.endsWith("a")) past = past.substring(0, past.length()-1);
			else if (past.endsWith("os") || past.endsWith("ās")) past = past.substring(0, past.length()-2);
			else
			{
				System.err.printf("Problēma, veidojot pagātnes celmu verba likumam \"%s\"\n", patternEnd);
				good = false;
			}
			if (good) pasts.add(past);
		}

		return Tuple.of(presents, pasts);
	}

	/**
	 * Palīgmetode, kas no verba gramatikas paterna visām personai izvelk
	 * tagadnes un pagātnes celmus, pieņemot, ka likums ir kādā no formām
	 * -auju, -auj, -auj, arī -aunu, -aun, -aun, pag. -āvu
	 * -gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu
	 * -jaušu, -jaut, -jauš, pag. -jautu, arī -jaužu, -jaud, -jauž, pag. -jaudu
	 * -gulstos, -gulsties, -gulstas, arī -guļos, -gulies, -guļas, pag. -gūlos, arī -gulos
	 * @return Pārītis, kur pirmais saraksts ir tagadnes celmu sarakst un otrais
	 * - pagātnes.
	 */
	protected static Tuple<ArrayList<String>,ArrayList<String>> extractPPStemsAllPersParallel(
			String pattern)
	{
		ArrayList<String> presentTexts = new ArrayList<>();
		ArrayList<String> pastTexts = new ArrayList<>();

		String[] parts = pattern.split(", (arī|retāk|pag\\.) ");
		if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			pastTexts.add(parts[1]);
			presentTexts.add(parts[2]);
			pastTexts.add(parts[3]);
		}
		else if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			presentTexts.add(parts[1]);
			pastTexts.add(parts[2]);
			pastTexts.add(parts[3]);
		}
		else if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			presentTexts.add(parts[1]);
			pastTexts.add(parts[2]);
		}
		else if (pattern.matches(
				"((?!, (arī|retāk|pag\\.) ).)*, pag\\. ((?!, (arī|retāk|pag\\.) ).)*, (arī|retāk) ((?!, (arī|retāk|pag\\.) ).)*"))
		{
			presentTexts.add(parts[0]);
			pastTexts.add(parts[1]);
			pastTexts.add(parts[2]);
		}
		else System.err.printf(
				"Nespēj izgūt tagadnes un pagātnes celmus no šablona \"%s\"\n", pattern);

		ArrayList<String> presents = new ArrayList<>();
		ArrayList<String> pasts = new ArrayList<>();

		if (presentTexts.size() + pastTexts.size() == parts.length)
		{
			for (String present : presentTexts)
			{
				if (present.contains(","))
					present = present.substring(present.lastIndexOf(',') + 1).trim();
				if (present.startsWith("-")) present = present.substring(1);
				if (present.endsWith("as")) present = present.substring(0, present.length()-2);
				if (present.matches(".*[ ,\\-].*")) System.err.printf(
						"Problēma, veidojot tagadnes celmu verba likumam \"%s\"\n", pattern);
				else presents.add(present);
			}

			for (String past : pastTexts)
			{
				boolean good = true;
				past = past.trim();
				if (past.startsWith("-")) past = past.substring(1).trim();
				if (past.endsWith("u") || past.endsWith("a")) past = past.substring(0, past.length()-1);
				else if (past.endsWith("os") || past.endsWith("ās")) past = past.substring(0, past.length()-2);
				else
				{
					System.err.printf("Problēma, veidojot pagātnes celmu verba likumam \"%s\"\n", pattern);
					good = false;
				}
				if (good) pasts.add(past);
			}
		}
		else System.err.printf(
				"Nespēj izgūt tagadnes un pagātnes celmus no šablona \"%s\"\n", pattern);
		return Tuple.of(presents, pasts);
	}

	/**
	 * Piemērot likumu bez papildus maģijas.
	 *
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @return jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
	 * daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int applyDirect(String gramText, String lemma,
			HashSet<Integer> paradigmCollector, Flags flagCollector)
	{
		int newBegin = -1;
		if (thirdPersonRule != null)
			newBegin = thirdPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1 && allPersonRule != null)
			newBegin = allPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin != -1) addStemFlags(lemma, flagCollector);
		return newBegin;
	}

	/**
	 * Piemērot likumu tā, ka patternText defises ir neobligātas.
	 *
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @return jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
	 * daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int applyOptHyphens(String gramText, String lemma,
			HashSet<Integer> paradigmCollector, Flags flagCollector)
	{
		int newBegin = -1;
		if (thirdPersonRule != null)
			newBegin = thirdPersonRule.applyOptHyphens(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1 && allPersonRule != null)
			newBegin = allPersonRule.applyOptHyphens(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin != -1) addStemFlags(lemma, flagCollector);
		return newBegin;
	}

	/**
	 * Pieņemot, ka vārds ir veiksmīgi atpazīts, pievieno celmus aprakstošos
	 * karodziņus.
	 * @param lemma 		atpazītais vārds
	 * @param flagCollector	kolekcija, kurā pielikt karodziņus
	 */
	protected void addStemFlags(String lemma, Flags flagCollector)
	{
		String prefix = lemma;
		if (prefix.endsWith("t"))
			prefix = prefix.substring(0, prefix.length() - 1);
		else if (prefix.endsWith("ties"))
			prefix = prefix.substring(0, prefix.length() - 4);
		else System.err.printf(
					"Neizdodas noteikt prefiksu 1. konj. vārdam \"%s\"\n", lemma);
		String infinityStem = null;
		for (String stem : infinityStems)
		{
			if (prefix.endsWith(stem))
				infinityStem = stem;
		}
		if (infinityStem != null)
		{
			prefix = prefix.substring(0, prefix.length() - infinityStem.length());
			if (prefix.length() > 0)
				flagCollector.add(Keys.VERB_PREFIX, prefix);
			flagCollector.add(Keys.INFINITY_STEM, prefix + infinityStem);

			for (String stem : presentStems)
				flagCollector.add(Keys.PRESENT_STEM, prefix + stem);
			for (String stem : pastStems)
				flagCollector.add(Keys.PAST_STEM, prefix + stem);

		}
		else System.err.printf(
				"Neizdodas noteikt prefiksu 1. konj. vārdam \"%s\" ar nenoteiksmes celmu \"%s\"\n",
				lemma, infinityStems.stream().reduce((s1, s2)-> s1 + "\", \"" + s2).orElse(""));
	}
}
