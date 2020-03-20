package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Likums, kas apvieno divus darbības vārdiem raksturīgos gadījumus: vienu visām
 * personām, otru tikai trešajai, piemēram, "-brāžu, -brāz, -brāž, pag. -brāzu"
 * un "parasti 3. pers., -brāž, pag. -brāzu". Pirmās konjugācijas gadījumā - arī
 * celmu iegūšanu un uzstādīšanu.
 * Šajā klasē apvienots saskarne likumiem, kam ir:
 * a) gan visu personu formu šablons, gan tikai trešās personas formu šablons,
 * b) tikai trešās personas formu šablons (jo 3. personas likumi neatbalsta
 *    celmu izgūšanu),
 * c) tikai visu personu formu šablons (jo BaseRule neatbalsta celmu izgūšanu).
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * Izveidots 2015-10-16.
 * @author Lauma
 */
public class VerbDoubleRule implements EndingRule
{
	/**
	 * Neeskepota teksta virkne, ar kuru grmatikai jāsākas, lai šis likums būtu
	 * piemērojams.
	 */
	protected final String patternText;

	protected BaseRule allPersonRule;
	protected ThirdPersVerbRule thirdPersonRule;
	/**
	 * Celmus glabā atsevišķi, nevis jau kā gatavus karodziņus tāpēc, ka
	 * karodziņam jāsatur arī "priedēklis", bet šobrīd tas nav zināms.
	 * NULL 2. un 3. konjufgācijas verbiem.
 	 */
	protected FirstConjStems stems;

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
	 * @param paradigms		paradigmas, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem (Vārdšķira =
	 *                      Darbības vārds nav obligāts)
	 * @param positiveRestrictions	ierobežojumi, ko uzstādīt, ja ir gan
	 *                              atbilstība likuma šablonam, gan lemmas
	 *                              nosacījumiem
	 * @param alwaysFlags			karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 * @param alwaysRestrictions	ierobežojumi, ko uzstādīt, ja ir konstatēta
	 *                              atbilstība likuma šablonam
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public VerbDoubleRule(String patternBegin, String patternEnd,
			String lemmaEnd, Set<Integer> paradigms,
			Set<Tuple<String,String>> positiveFlags,
			Set<StructRestrs.One> positiveRestrictions,
			Set<Tuple<String,String>> alwaysFlags,
			Set<StructRestrs.One> alwaysRestrictions,
			FirstConjStems stems)
	{
		HashSet<Tuple<String,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(TFeatures.POS__VERB);
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<String,String>> alwaysFlagsSet = alwaysFlags == null ?
				null : new HashSet<>(alwaysFlags);
		this.stems = stems;

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
			patternText = allPersonPattern;
			allPersonRule = BaseRule.simple(
					allPersonPattern, ".*" + lemmaEnd, paradigms,
					positiveFlagsFull, positiveRestrictions,
					alwaysFlagsSet, alwaysRestrictions);
			thirdPersonRule = new ThirdPersVerbRule(
					thirdPersonPattern, lemmaEnd, paradigms, positiveFlagsFull,
					positiveRestrictions, alwaysFlagsSet, alwaysRestrictions);
		} else if (patternEnd != null && patternEnd.trim().length() > 0)
		{
			patternText = patternEnd;
			allPersonRule = null;
			thirdPersonRule = new ThirdPersVerbRule(
					patternEnd,	lemmaEnd, paradigms, positiveFlagsFull,
					positiveRestrictions, alwaysFlagsSet, alwaysRestrictions);
		} else if (patternBegin != null && patternBegin.trim().length() > 0)
		{
			patternText = patternBegin;
			allPersonRule = BaseRule.simple(
					patternBegin, ".*" + lemmaEnd, paradigms,
					positiveFlagsFull, positiveRestrictions, alwaysFlagsSet,
					alwaysRestrictions);
			thirdPersonRule = null;
		}
		else
		{
			patternText = "";
			System.err.printf("Veidojot darbības vārda likumu \"%s\", nav norādīti šabloni\n", lemmaEnd);
		}
	}

	/**
	 * Konstruktors likumam, kam ir tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms		paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem (Vārdšķira =
	 *                      Darbības vārds nav obligāts)
	 * @param positiveRestrictions	ierobežojumi, ko uzstādīt, ja ir gan
	 *                              atbilstība likuma šablonam, gan lemmas
	 *                              nosacījumiem
	 * @param alwaysFlags			karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 * @param alwaysRestrictions	ierobežojumi, ko uzstādīt, ja ir konstatēta
	 *                              atbilstība likuma šablonam
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public VerbDoubleRule(
			String patternEnd, String lemmaEnd, Set<Integer> paradigms,
			Set<Tuple<String,String>> positiveFlags,
			Set<StructRestrs.One> positiveRestrictions,
			Set<Tuple<String,String>> alwaysFlags,
			Set<StructRestrs.One> alwaysRestrictions,
			FirstConjStems stems)
	{
		HashSet<Tuple<String,String>> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add(TFeatures.POS__VERB);
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<Tuple<String,String>> alwaysFlagsSet = alwaysFlags == null ?
				null : new HashSet<>(alwaysFlags);

		this.stems = stems;
		allPersonRule = null;
		thirdPersonRule = new ThirdPersVerbRule(
				patternEnd, lemmaEnd, paradigms, positiveFlagsFull,
				positiveRestrictions, alwaysFlagsSet, alwaysRestrictions);
		patternText = patternEnd;
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
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags,
			FirstConjStems stems)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null,
				stems);
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
	 * @param positiveRestriction	ierobežojums, ko uzstādīt, ja ir gan
	 *                              atbilstība likuma šablonam, gan lemmas
	 *                              nosacījumiem
	 * @param alwaysFlags			karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 * @param alwaysRestriction		ierobežojums, ko uzstādīt, ja ir konstatēta
	 *                              atbilstība likuma šablonam
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
									String lemmaEnd, int paradigmId,
									Tuple<String,String>[] positiveFlags,
									StructRestrs.One positiveRestriction,
									Tuple<String,String>[] alwaysFlags,
									StructRestrs.One alwaysRestriction,
									FirstConjStems stems)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				positiveRestriction == null ? null : new HashSet<StructRestrs.One>(){{add(positiveRestriction);}},
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				alwaysRestriction == null ? null : new HashSet<StructRestrs.One>(){{add(alwaysRestriction);}},
				stems);
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
	 * @param positiveRestrictions	ierobežojumi, ko uzstādīt, ja ir gan
	 *                              atbilstība likuma šablonam, gan lemmas
	 *                              nosacījumiem
	 * @param alwaysFlags			karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 * @param alwaysRestrictions	ierobežojumi, ko uzstādīt, ja ir konstatēta
	 *                              atbilstība likuma šablonam
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
									String lemmaEnd, int paradigmId,
									Tuple<String,String>[] positiveFlags,
									StructRestrs.One[] positiveRestrictions,
									Tuple<String,String>[] alwaysFlags,
									StructRestrs.One[] alwaysRestrictions,
									FirstConjStems stems)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				positiveRestrictions == null ? null : new HashSet<>(Arrays.asList(positiveRestrictions)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				alwaysRestrictions == null ? null : new HashSet<>(Arrays.asList(alwaysRestrictions)),
				stems);
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
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public static VerbDoubleRule of(String patternEnd, String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags,
			FirstConjStems stems)
	{
		return new VerbDoubleRule(patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null,
				stems);
	}

	/**
	 * Konstruktors pilnam likumam, ja tiek uzdoti celmi (paredzēts 1. konj.).
	 * @param patternBegin		gramatikas daļa ar galotnēm 1. un 2. personai,
	 *                          var būt null, ja tas ir likums tikai ar
	 *                          3. personas formām
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                      	pagātnei, var būt null, ja 3. personas dormas
	 *                      	nevar atvasīnāt no šī likuma automātiski
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigmId		paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                      	likuma šablonam, gan lemmas nosacījumiem
	 *                      	(Vārdšķira = Darbības vārds nav obligāts)
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      	atbilstība likuma šablonam
	 * @param infinitiveStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      	vairākām nenoteiksmēm (nedrīkst būt null)
	 * @param presentStems		tagadnes celmi (nedrīkst būt null)
	 * @param pastStems			pagātnes celmi (nedrīkst but null)
	 * @param areStemsOrdered	vai celmiem ir prioritāšu secība
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags,
			String[] infinitiveStems, String[] presentStems, String[] pastStems,
			boolean areStemsOrdered)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null,
				FirstConjStems.of(infinitiveStems, presentStems, pastStems,	areStemsOrdered));
	}

	/**
	 * Konstruktors likumam, kam ir tikai 3. personas formas, ja tiek uzdoti
	 * celmi (paredzēts 1. konj.).
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                          agātnei
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigmId		paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                          likuma šablonam, gan lemmas nosacījumiem
	 *                          (Vārdšķira = Darbības vārds nav obligāts)
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja ir konstatēta
	 *                          atbilstība likuma šablonam
	 * @param infinitiveStems	iespējamie netnoteiksmes celmi, ja likums der
	 *                      	vairākām nenoteiksmēm (nedrīkst būt null)
	 * @param presentStems		tagadnes celmi (nedrīkst būt null)
	 * @param pastStems			pagātnes celmi (nedrīkst būt null)
	 * @param areStemsOrdered	vai celmiem ir prioritāšu secība
	 */
	public static VerbDoubleRule of(String patternEnd, String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags,
			String[] infinitiveStems, String[] presentStems, String[] pastStems,
			boolean areStemsOrdered)
	{
		return new VerbDoubleRule(patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null,
				FirstConjStems.of(infinitiveStems, presentStems, pastStems, areStemsOrdered));
	}

	/**
	 * Konstruktors pilnam likumam, ja celmu nav (paredzēts 2., 3. konj.).
	 * @param patternBegin		gramatikas daļa ar galotnēm 1. un 2. personai,
	 *                          var būt null, ja tas ir likums tikai ar
	 *                          3. personas formām
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                      	pagātnei, var būt null, ja 3. personas dormas
	 *                      	nevar atvasīnāt no šī likuma automātiski
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigmId		paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                      	likuma šablonam, gan lemmas nosacījumiem
	 *                      	(Vārdšķira = Darbības vārds nav obligāts)
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      	atbilstība likuma šablonam
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null,
				null);
	}

	/**
	 * Konstruktors pilnam likumam, ja celmu nav (paredzēts 2., 3. konj.).
	 * @param patternBegin		gramatikas daļa ar galotnēm 1. un 2. personai,
	 *                          var būt null, ja tas ir likums tikai ar
	 *                          3. personas formām
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                      	pagātnei, var būt null, ja 3. personas dormas
	 *                      	nevar atvasīnāt no šī likuma automātiski
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigmId		paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                      	likuma šablonam, gan lemmas nosacījumiem
	 *                      	(Vārdšķira = Darbības vārds nav obligāts)
	 * @param positiveRestriction	ierobežojums, ko uzstādīt, ja ir gan
	 *                              atbilstība likuma šablonam, gan lemmas
	 *                              nosacījumiem
	 * @param alwaysFlags			karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 * @param alwaysRestriction		ierobežojums, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
									String lemmaEnd, int paradigmId,
									Tuple<String,String>[] positiveFlags,
									StructRestrs.One positiveRestriction,
									Tuple<String,String>[] alwaysFlags,
									StructRestrs.One alwaysRestriction)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				positiveRestriction == null ? null : new HashSet<StructRestrs.One>(){{add(positiveRestriction);}},
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				alwaysRestriction == null ? null : new HashSet<StructRestrs.One>(){{add(alwaysRestriction);}},
				null);
	}

	/**
	 * Konstruktors pilnam likumam, ja celmu nav (paredzēts 2., 3. konj.).
	 * @param patternBegin		gramatikas daļa ar galotnēm 1. un 2. personai,
	 *                          var būt null, ja tas ir likums tikai ar
	 *                          3. personas formām
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                      	pagātnei, var būt null, ja 3. personas dormas
	 *                      	nevar atvasīnāt no šī likuma automātiski
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigmId		paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                      	likuma šablonam, gan lemmas nosacījumiem
	 *                      	(Vārdšķira = Darbības vārds nav obligāts)
	 * @param positiveRestrictions	ierobežojumi, ko uzstādīt, ja ir gan
	 *                              atbilstība likuma šablonam, gan lemmas
	 *                              nosacījumiem
	 * @param alwaysFlags			karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 * @param alwaysRestrictions	ierobežojumi, ko uzstādīt, ja ir konstatēta
	 *                      		atbilstība likuma šablonam
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
									String lemmaEnd, int paradigmId,
									Tuple<String,String>[] positiveFlags,
									StructRestrs.One[] positiveRestrictions,
									Tuple<String,String>[] alwaysFlags,
									StructRestrs.One[] alwaysRestrictions)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				positiveRestrictions == null ? null : new HashSet<>(Arrays.asList(positiveRestrictions)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				alwaysRestrictions == null ? null : new HashSet<>(Arrays.asList(alwaysRestrictions)),
				null);
	}

	/**
	 * Konstruktors likumam, kam ir tikai 3. personas formas, ja celmu nav
	 * (paredzēts 2., 3. konj.).
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                          agātnei
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigmId		paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                          likuma šablonam, gan lemmas nosacījumiem
	 *                          (Vārdšķira = Darbības vārds nav obligāts)
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja ir konstatēta
	 *                          atbilstība likuma šablonam
	 */
	public static VerbDoubleRule of(String patternEnd, String lemmaEnd, int paradigmId,
			Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new VerbDoubleRule(patternEnd, lemmaEnd,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null, null);
	}

	/**
	 * Konstruktors pilnam likumam, ja celmu nav, bet ir vairākas paradigmas
	 * (paredzēts 2., 3. konj.).
	 * @param patternBegin		gramatikas daļa ar galotnēm 1. un 2. personai,
	 *                          var būt null, ja tas ir likums tikai ar
	 *                          3. personas formām
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                      	pagātnei, var būt null, ja 3. personas dormas
	 *                      	nevar atvasīnāt no šī likuma automātiski
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigms			paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                      	likuma šablonam, gan lemmas nosacījumiem
	 *                      	(Vārdšķira = Darbības vārds nav obligāts)
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja ir konstatēta
	 *                      	atbilstība likuma šablonam
	 */
	public static VerbDoubleRule of(String patternBegin, String patternEnd,
			String lemmaEnd, Integer[] paradigms,
			Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags)
	{
		return new VerbDoubleRule(patternBegin, patternEnd, lemmaEnd,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null, null);
	}

	/**
	 * Konstruktors likumam, kam ir tikai 3. personas formas, ja celmu nav
	 * (paredzēts 2., 3. konj.).
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un
	 *                          agātnei
	 * @param lemmaEnd			nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      	varētu piemērot
	 * @param paradigms			paradigma, ko lietot, ja konstatēta atbilstība
	 *                      	šim likumam
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja ir gan atbilstība
	 *                          likuma šablonam, gan lemmas nosacījumiem
	 *                          (Vārdšķira = Darbības vārds nav obligāts)
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja ir konstatēta
	 *                          atbilstība likuma šablonam
	 */
	public static VerbDoubleRule of(String patternEnd, String lemmaEnd, Integer[] paradigms,
			Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new VerbDoubleRule(patternEnd, lemmaEnd,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null,
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null, null);
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
			Set<Integer> paradigmCollector, Flags flagCollector)
	{
		int newBegin = -1;
		if (thirdPersonRule != null)
			newBegin = thirdPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1 && allPersonRule != null)
			newBegin = allPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin != -1 && stems != null)
			stems.addStemFlags(lemma, flagCollector);
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
			Set<Integer> paradigmCollector, Flags flagCollector)
	{
		int newBegin = -1;
		if (thirdPersonRule != null)
			newBegin = thirdPersonRule.applyOptHyphens(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1 && allPersonRule != null)
			newBegin = allPersonRule.applyOptHyphens(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin != -1 && stems != null)
			stems.addStemFlags(lemma, flagCollector);
		return newBegin;
	}

	/**
	 * Cik reižu likums ir lietots?
	 * @return skaits, cik reižu likums ir lietots.
	 */
	@Override
	public int getUsageCount()
	{
		int res = 0;
		if (allPersonRule != null) res = res + allPersonRule.getUsageCount();
		if (thirdPersonRule != null) res = res + thirdPersonRule.getUsageCount();
		return res;
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
		return String.format("%s \"%s\"",
				this.getClass().getSimpleName(), patternText);
	}
}
