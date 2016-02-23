package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.utils.Tuple;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Likumi, kas tikai ar galotnēm norāda, ka jāizveido alternatīvā lemma.
 * Vienākāršākajā gadījuma - gramatikas šablonam atbilst viens galotnes šablons,
 * viena pamata paradigma ar vienu alternatīvo lemmu, kurai atkal ir viena
 * paradigma.
 * Vispārīgajā gadījumā - ja gramatika atbilst dotajam šablonam, tad atkarībā no
 * tā, kādam šablonam atbilst lemma, tiek piekārtotas paradigmas un karodziņi.
 * Atbilstība lemmu šabloniem tiek pārbaudīta secīgi un, kad pirmā atbilsme ir
 * atrasta, tālāk nepārbauda (tātad uzdošanās kārtība ir svarīga).
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
 *
 * Izveidots 2015-10-26.
 * @author Lauma
 */
public class BaseRule implements Rule
{
    /**
     * Neeskepota teksta virkne, ar kuru grmatikai jāsākas, lai šis likums būtu
     * piemērojams.
     */
    protected final String patternText;

    /**
     * Nokompilēts šablons tiešajam likumam (iegūts no patternText).
     */
    protected final Pattern directPattern;
    /**
     * Nokompilēts šablons likumam ar neobligātām defisēm (iegūts no
     * patternText).
     */
    protected final Pattern optHyphenPattern;

    /**
     * Vairāki komplekti ar nosacījumiem: lemmas ierobežojumi un paradigmu un
     * karodziņu kopas, kas lietojami, ja attiecīgais lemmas ierobežojums
     * izpildās.
     */
    protected final List<SimpleSubRule> lemmaLogic;
    /**
     * Šos karodziņus uzstāda, ja gramatikas teksts atbilst attiecīgajam
     * šablonam.
     */
    protected final Set<Tuple<Keys,String>> alwaysFlags;

    /**
     * Šo izdrukā, kad liekas, ka likums varētu būt nepilnīgs - gramatikas
     * šablonam atbilstība tiek fiksēta, bet lemmu šabloniem - nē.
     */
    protected final String errorMessage;

    /**
     * @param pattern		teksts, ar kuru jāsākas gramatikai
     * @param lemmaLogic	nosacījumu saraksts (nedrīkst būt null, kurā katrs
     *                      elements sastāv no trijnieka: 1)lemmu aprakstoša
     *                      šablona, 2) paradigmām, ko lietot, ja lemma atbilst
     *                      šim šablonam, 3) karodziņiem, ko uzstādīt, ja lemma
     *                      atbilst šim šablonam
     * @param alwaysFlags	karodziņi, ko uzstādīt, ja gramatikas teksts
     *                      atbilst attiecīgajam šablonam.
     */
    public BaseRule(String pattern, List<SimpleSubRule> lemmaLogic,
			Set<Tuple<Keys, String>> alwaysFlags)
    {
        if (lemmaLogic == null)
            throw new IllegalArgumentException (
                    "Nav paredzēts, ka BaseRule tiek viedots vispār bez lemmu nosacījumiem!");
        this.patternText = pattern;
        directPattern = Pattern.compile("(\\Q" + patternText + "\\E)([;,.].*)?");
        String regExpPattern = patternText.replace("-", "\\E-?\\Q");
        optHyphenPattern = Pattern.compile("(\\Q" + regExpPattern + "\\E)([;,.].*)?");

        this.lemmaLogic = Collections.unmodifiableList(lemmaLogic);
        this.alwaysFlags = alwaysFlags == null ? null : Collections.unmodifiableSet(alwaysFlags);

		if (lemmaLogic.size() == 1 && lemmaLogic.get(0).paradigms.size() == 1)
			errorMessage = "Neizdodas \"%s\" ielikt paradigmā " + 
					lemmaLogic.get(0).paradigms.toArray()[0] +"\n";
		else errorMessage = "Neizdodas \"%s\" ielikt kādā no paradigmām " +
                lemmaLogic.stream().map(t -> t.paradigms).flatMap(m -> m.stream())
                        .distinct().sorted().map(i -> i.toString())
                        .reduce((a, b) -> a + ", " + b).orElse("")+ "\n";
    }

	/**
	 * Papildus konstruktors īsumam - gadījumiem, kad ir tikai viens lemmas
	 * nosacījums ar tikai vienu paradigmu. (Agrāk šis gadījums bija izdalīts
	 * atsevišķā klasē).
	 * @param patternText		teksts, ar kuru jāsākas gramatikai (tiks
	 *                          eskeipots)
	 * @param lemmaRestrictions	regulā izteiksme, kas nosaka lemmas īpatnības
	 *                      	(netiks eskeipota)
	 * @param paradigmId		paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                      	atbilst attiecīgajam šablonam
	 */
	public static BaseRule simple(String patternText, String lemmaRestrictions,
			int paradigmId,	Set<Tuple<Keys, String>> positiveFlags,
			Set<Tuple<Keys, String>> alwaysFlags)
	{
		return new BaseRule(patternText, new ArrayList<SimpleSubRule>() {{
						add(new SimpleSubRule(lemmaRestrictions, new HashSet<Integer>(){{
							add(paradigmId);}}, positiveFlags));}},
				alwaysFlags);
	}

	/**
	 * Papildus konstruktors īsumam - gadījumiem, kad ir tikai viens lemmas
	 * nosacījums ar vairākām paradigmām.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai (tiks
	 *                          eskeipots)
	 * @param lemmaRestrictions	regulā izteiksme, kas nosaka lemmas īpatnības
	 *                      	(netiks eskeipota)
	 * @param paradigms			paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                      	atbilst attiecīgajam šablonam
	 */
	public static BaseRule simple(String patternText, String lemmaRestrictions,
			Set<Integer> paradigms,	Set<Tuple<Keys, String>> positiveFlags,
			Set<Tuple<Keys, String>> alwaysFlags)
	{
		return new BaseRule(patternText, new ArrayList<SimpleSubRule>() {{
			add(new SimpleSubRule(lemmaRestrictions, paradigms, positiveFlags));}},
				alwaysFlags);
	}

	/**
	 * Papildus konstruktors īsumam.
	 * Izveido BaseRule, ja lemmu nosacījumu doti masīvu struktūrā, nevis
	 * sarakstos un kopās.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaLogic		nosacījumu saraksts, kurā katrs elements sastāv
	 *                          no trijnieka: 1)lemmu aprakstoša šablona, 2)
	 *                          paradigmām, ko lietot, ja lemma atbilst šim
	 *                          šablonam, 3) karodziņiem, ko uzstādīt, ja lemma
	 *                          atbilst šim šablonam.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	jauns BaseRule
	 */
	public static BaseRule of(String patternText, SimpleSubRule[] lemmaLogic,
			Tuple<Keys,String>[] alwaysFlags)
	{
		return new BaseRule(patternText,
				lemmaLogic == null ? null : Arrays.asList(lemmaLogic),
				alwaysFlags == null ? null :
						new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Papildus konstruktors īsumam - gadījumiem, kad ir tikai viens lemmas
	 * nosacījums ar tikai vienu paradigmu. (Agrāk šis gadījums bija izdalīts
	 * atsevišķā klasē).
	 * @param patternText		teksts, ar kuru jāsākas gramatikai (tiks
	 *                          eskeipots)
	 * @param lemmaRestrictions	regulā izteiksme, kas nosaka lemmas īpatnības
	 *                      	(netiks eskeipota)
	 * @param paradigmId		paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                      	teksts, gan lemma atbilst attiecīgajiem
	 *                      	šabloniem
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                      	atbilst attiecīgajam šablonam
	 */
	public static BaseRule of(String patternText, String lemmaRestrictions,
			int paradigmId,
			Tuple<Keys, String>[] positiveFlags,
			Tuple<Keys, String>[] alwaysFlags)
	{
		return BaseRule.of(patternText, new SimpleSubRule[]{
						SimpleSubRule.of(lemmaRestrictions, new Integer[]{paradigmId}, positiveFlags)},
				alwaysFlags);
	}

	/**
	 * Papildus konstruktors īsumam - gadījumiem, kad ir tikai viens lemmas
	 * nosacījums ar vairākām paradigmām.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai (tiks
	 *                          eskeipots)
	 * @param lemmaRestrictions	regulā izteiksme, kas nosaka lemmas īpatnības
	 *                      	(netiks eskeipota)
	 * @param paradigms			paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                      	atbilst attiecīgajam šablonam
	 */
	public static BaseRule of(String patternText, String lemmaRestrictions,
			Integer[] paradigms, Tuple<Keys, String>[] positiveFlags,
			Tuple<Keys, String>[] alwaysFlags)
	{
		return BaseRule.of(patternText, new SimpleSubRule[]{
						SimpleSubRule.of(lemmaRestrictions, paradigms, positiveFlags)},
				alwaysFlags);
	}

	/**
	 * Papildus metode īsumam - visām pozitīvo karodziņu kopām pieliek vārdšķiru
	 * Lietvārds.
	 * Izveido BaseRule, ja lemmu nosacījumu doti masīvu struktūrā, nevis
	 * sarakstos un kopās.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaLogic		nosacījumu saraksts, kurā katrs elements sastāv
	 *                          no trijnieka: 1)lemmu aprakstoša šablona, 2)
	 *                          paradigmām, ko lietot, ja lemma atbilst šim
	 *                          šablonam, 3) karodziņiem, ko uzstādīt, ja lemma
	 *                          atbilst šim šablonam.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	jauns BaseRule
	 */
	public static BaseRule noun (String patternText, SimpleSubRule[] lemmaLogic,
			Tuple<Keys,String>[] alwaysFlags)
	{
		ArrayList<SimpleSubRule> tmp = new ArrayList<>();
		for (SimpleSubRule r : lemmaLogic)
			tmp.add(r.cloneWithFeature(Features.POS__NOUN));
		return new BaseRule(patternText, tmp,
				alwaysFlags == null ? null :
						new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Papildus metode īsumam - visām pozitīvo karodziņu kopām pieliek vārdšķiru
	 * Lietvārds - gadījumam, kad ir tikai viens lemmas nosacījums un vairākas
	 * paradigmas.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @param paradigms			paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	jauns BaseRule, kam ir pazīme Vārdšķira ar vērtību Lietvārds.
	 */
	public static BaseRule noun(String patternText, String lemmaRestrictions,
			Integer[] paradigms, Tuple<Keys,String>[] positiveFlags,
			Tuple<Keys,String>[] alwaysFlags)
	{
		HashSet<Tuple<Keys,String>> fullPosFlags = new HashSet<>();
		if (positiveFlags != null) fullPosFlags.addAll(Arrays.asList(positiveFlags));
		fullPosFlags.add(Features.POS__NOUN);
		return BaseRule.simple(patternText, lemmaRestrictions,
				paradigms == null ? null : new HashSet<>(Arrays
						.asList(paradigms)),
				fullPosFlags,
				alwaysFlags == null ? null : new HashSet<>(Arrays
						.asList(alwaysFlags)));
	}

	/**
	 * Papildus metode īsumam - visām pozitīvo karodziņu kopām pieliek vārdšķiru
	 * Lietvārds - gadījumam, kad ir tikai viens lemmas nosacījums un viena
	 * paradigma.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @param paradigm			paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	jauns BaseRule, kam ir pazīme Vārdšķira ar vērtību Lietvārds.
	 */
	public static BaseRule noun(String patternText, String lemmaRestrictions,
			int paradigm, Tuple<Keys,String>[] positiveFlags,
			Tuple<Keys,String>[] alwaysFlags)
	{
		HashSet<Tuple<Keys,String>> fullPosFlags = new HashSet<>();
		if (positiveFlags != null) fullPosFlags.addAll(Arrays.asList(positiveFlags));
		fullPosFlags.add(Features.POS__NOUN);
		return BaseRule.simple(patternText, lemmaRestrictions,
				new HashSet<Integer>()
				{{add(paradigm);}}, fullPosFlags,
				alwaysFlags == null ? null : new HashSet<>(Arrays
						.asList(alwaysFlags)));
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule divdabjiem ar izskaņām -is, -usi, -ies, -usies.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 0. paradigmu un divdabju karodziņiem
	 * TODO likt šeit tās dzimtes vai nelikt?
	 */
	public static BaseRule participleIs(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 0,
				new Tuple[]{Features.POS__VERB, Features.POS__PARTICIPLE, Features.POS__PARTICIPLE_IS, Features.GENDER__MASC, Features.GENDER__FEM},
				null);
	}
	/**
	 * Metode īsumam.
	 * Izveido BaseRule īpašības vārdiem šabloniem formā:
	 * -ais; s. -a, -ā
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @return BaseRule ar 13 un 14. paradigmu divdabju karodziņiem
	 * TODO likt šeit tās dzimtes vai nelikt?
	 */
	public static BaseRule adjectiveParticiple (String patternText)
	{
		return BaseRule.of(patternText, new SimpleSubRule[] {
						SimpleSubRule.of(".*[^aeiouāēīōū]š", new Integer[]{14}, new Tuple[]{Features.POS__ADJ}),
						SimpleSubRule.of(".*ts", new Integer[]{13, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_TS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*ošs", new Integer[]{13, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_OSS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*dams", new Integer[]{13, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_DAMS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*[āa]ms", new Integer[]{13, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_AMS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{13}, new Tuple[]{Features.POS__ADJ})},
				new Tuple[]{Features.GENDER__MASC, Features.GENDER__FEM});
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule īpašības vārdiem šabloniem formā:
	 * īp. v. -ais; s. -a, -ā
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @return BaseRule ar 13 un 14. paradigmu divdabju karodziņiem
	 * TODO likt šeit tās dzimtes vai nelikt?
	 */
	public static BaseRule adjective (String patternText)
	{
		return BaseRule.of(patternText, new SimpleSubRule[] {
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{13}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]š", new Integer[]{14}, null)},
				new Tuple[] {Features.POS__ADJ, Features.GENDER__MASC, Features.GENDER__FEM});
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 5. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 9. paradigmu
	 */
	public static BaseRule fifthDeclStd(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 9,
				new Tuple[]{Features.POS__NOUN}, new Tuple[]{Features.GENDER__FEM});
	}
	/**
	 * Metode īsumam.
	 * Izveido BaseRule 5. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī bez līdzskaņu mijas.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 9. paradigmu
	 */
	public static BaseRule fifthDeclNoChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 9,
				new Tuple[]{Features.POS__NOUN, Features.NO_SOUNDCHANGE}, new Tuple[]{Features.GENDER__FEM});
	}
	/**
	 * Metode īsumam.
	 * Izveido BaseRule 5. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī ar fakultatīvu līdzskaņu miju.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 9. paradigmu
	 */
	public static BaseRule fifthDeclOptChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 9,
				new Tuple[]{Features.POS__NOUN, Features.OPT_SOUNDCHANGE}, new Tuple[]{Features.GENDER__FEM});
	}
	/**
	 * Metode īsumam.
	 * Izveido BaseRule 2. deklinācijas vīriešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 3. paradigmu
	 */
	public static BaseRule secondDeclStd(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 3,
				new Tuple[]{Features.POS__NOUN},
				new Tuple[]{Features.GENDER__MASC});
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 6. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī un līdzskaņu miju.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 11. paradigmu
	 */
	public static BaseRule sixthDeclStd(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 11,
				new Tuple[]{Features.POS__NOUN}, new Tuple[]{Features.GENDER__FEM});
	}
	/**
	 * Metode īsumam.
	 * Izveido BaseRule 6. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī bez līdzskaņu mijas.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 11. paradigmu
	 */
	public static BaseRule sixthDeclNoChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 11,
				new Tuple[]{Features.POS__NOUN, Features.NO_SOUNDCHANGE}, new Tuple[]{Features.GENDER__FEM});
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 6. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī un fakultatīvu līdzskaņu miju.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return BaseRule ar 11. paradigmu
	 */
	public static BaseRule sixthDeclOptChange(String patternText, String lemmaRestrictions)
	{
		return BaseRule.of(patternText, lemmaRestrictions, 11,
				new Tuple[]{Features.POS__NOUN, Features.OPT_SOUNDCHANGE}, new Tuple[]{Features.GENDER__FEM});
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 2. konjugācijas tiešajam darbības vārdam, kam dotas
	 * visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return BaseRule ar 16. paradigmu
	 */
	public static BaseRule secondConjDirAllPers(
			String patternText, String lemmaEnd)
	{
		return BaseRule.of(patternText, ".*" + lemmaEnd, 16,
				new Tuple[]{Features.POS__VERB}, null);
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 3. konjugācijas tiešajamdarbības vārdam, kam dotas
	 * visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams, ir paralēlās formas.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param presentChange	vai tagadnes formās ir līdzskaņu mija
	 * @return BaseRule ar 17. paradigmu
	 */
	public static BaseRule thirdConjDirAllPersParallel(
			String patternText, String lemmaEnd, boolean presentChange)
	{
		ArrayList<Tuple<Keys, String>> posFlags = new ArrayList<>();
		posFlags.add(Features.POS__VERB);
		posFlags.add(Features.PARALLEL_FORMS);
		if (presentChange)
			posFlags.add(Features.HAS_PRESENT_SOUNDCHANGE);
		else
			posFlags.add(Features.NO_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(Features.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(Features.ORIGINAL_NEEDED);
		return BaseRule.of(patternText, ".*" + lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 3. konjugācijas tiešajam darbības vārdam, kam dotas
	 * visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams, paralēlās formas ir gan ar miju, gan bez.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return BaseRule ar 17. paradigmu
	 */
	public static BaseRule thirdConjDirAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<Keys, String>> posFlags = new ArrayList<>();
		posFlags.add(Features.POS__VERB);
		posFlags.add(Features.PARALLEL_FORMS);
		posFlags.add(Features.OPT_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(Features.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(Features.ORIGINAL_NEEDED);
		return BaseRule.of(patternText, ".*" + lemmaEnd, 17,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 2. konjugācijas atgriezeniskajam darbības vārdam, kam
	 * dotas visas formas, bet atvasināt tikai trešās personas formu likumu nav
	 * iespējams.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return BaseRule ar 19. paradigmu
	 */
	public static BaseRule secondConjReflAllPers(
			String patternText, String lemmaEnd)
	{
		return BaseRule.of(patternText, ".*" + lemmaEnd, 19,
				new Tuple[]{Features.POS__VERB}, null);
	}

	/**
	 * Metode īsumam.
	 * Izveido BaseRule 3. konjugācijas darbības vārdam, kam dotas visas
	 * formas, bet atvasināt tikai trešās personas formu likumu nav iespējams,
	 * paralēlās formas ir gan ar miju, gan bez.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return BaseRule ar 20. paradigmu
	 */
	public static BaseRule thirdConjReflAllPersParallel(
			String patternText, String lemmaEnd)
	{
		ArrayList<Tuple<Keys, String>> posFlags = new ArrayList<>();
		posFlags.add(Features.POS__VERB);
		posFlags.add(Features.PARALLEL_FORMS);
		posFlags.add(Features.OPT_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(Features.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(Features.ORIGINAL_NEEDED);
		return BaseRule.of(patternText, ".*" + lemmaEnd, 20,
				posFlags.toArray(new Tuple[posFlags.size()]), null);
	}


	/**
	 * Metode īsumam.
	 * Izveido BaseRule darbības vārdam, kas ir gan 2, gan 3. konjugācijā un
	 * kuram dotas visu personu formas/galotnes.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @param presentChange		vai tagadnes formās ir līdzskaņu mija
	 */
	public static BaseRule secondThirdConjDirectAllPersParallel(
			String patternText, String lemmaRestrictions, boolean presentChange)
	{
		ArrayList<Tuple<Keys, String>> posFlags = new ArrayList<>();
		posFlags.add(Features.POS__VERB);
		posFlags.add(Features.PARALLEL_FORMS);
		if (presentChange)
			posFlags.add(Features.HAS_PRESENT_SOUNDCHANGE);
		else
			posFlags.add(Features.NO_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(Features.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(Features.ORIGINAL_NEEDED);

		return BaseRule.of(patternText, new SimpleSubRule[]{
					SimpleSubRule.of(lemmaRestrictions, new Integer[]{16, 17}, posFlags.toArray(new Tuple[posFlags.size()]))},
				null);

	}

    /**
     * Metode īsumam.
     * Izveido BaseRule darbības vārdam, kas ir gan 2, gan 3. konjugācijā un
     * kuram dotas visu personu formas/galotnes.
	 * Metode pārbauda, vai gramatika nesatur paralēlformas tieši no
	 * 1. konjugācijas un, ja satur, pieliek papildus karodziņu.
     * @param patternText		teksts, ar kuru jāsākas gramatikai
     * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
     * @param presentChange		vai tagadnes formās ir līdzskaņu mija
     */
    public static BaseRule secondThirdConjReflAllPersParallel(
            String patternText, String lemmaRestrictions, boolean presentChange)
    {
		ArrayList<Tuple<Keys, String>> posFlags = new ArrayList<>();
		posFlags.add(Features.POS__VERB);
		posFlags.add(Features.PARALLEL_FORMS);
		if (presentChange)
			posFlags.add(Features.HAS_PRESENT_SOUNDCHANGE);
		else
			posFlags.add(Features.NO_PRESENT_SOUNDCHANGE);
		if (RulesAsFunctions.containsFirstConj(patternText))
			posFlags.add(Features.FIRST_CONJ_PARALLELFORM);
		if (!RulesAsFunctions.containsFormsOnly(patternText))
			posFlags.add(Features.ORIGINAL_NEEDED);

		return BaseRule.of(patternText, new SimpleSubRule[]{
					SimpleSubRule.of(lemmaRestrictions, new Integer[]{19, 20}, posFlags.toArray(new Tuple[posFlags.size()]))},
				null);
    }

    /**
     * Piemērot likumu bez papildus maģijas.
     * @param gramText          apstrādājamā gramatika
     * @param lemma             hederim, kurā atrodas gramatika, atbilstošā lemma
     * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
     *                          gramatika un lemma atbilst šim likumam
     * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
     *                          vismaz gramatika atbilst šim likumam
     * @return  jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
     *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
     */
    @Override
    public int applyDirect(String gramText, String lemma,
            HashSet<Integer> paradigmCollector, Flags flagCollector)
    {
        return apply(directPattern, gramText, lemma, paradigmCollector, flagCollector);
    }

    /**
     * Piemērot likumu tā, ka patternText defises ir neobligātas.
     * @param gramText          apstrādājamā gramatika
     * @param lemma             hederim, kurā atrodas gramatika, atbilstošā lemma
     * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
     *                          gramatika un lemma atbilst šim likumam
     * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
     *                          vismaz gramatika atbilst šim likumam
     * @return  jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
     *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
     */
    @Override
    public int applyOptHyphens(String gramText, String lemma,
            HashSet<Integer> paradigmCollector, Flags flagCollector)
    {
        return apply(optHyphenPattern, gramText, lemma, paradigmCollector, flagCollector);
    }

    /**
     * Piemērot likumu, gramatikas teksta atbilstību nosakot ar parametros doto
     * šablonu.
     * Funkcija iekšējām vajadzībām.
     * @param gramPattern		šablons, kas nosaka gramatikas teksta "derīgumu"
     * @param gramText          apstrādājamā gramatika
     * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
     *                          lemma
     * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
     *                          gramatika un lemma atbilst šim likumam
     * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
     *                          vismaz gramatika atbilst šim likumam
     * @return  jaunā sākumpocīcija (vieta, kur sākas neatpazītā gramatikas
     *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
     */
    protected int apply(Pattern gramPattern, String gramText, String lemma,
            HashSet<Integer> paradigmCollector, Flags flagCollector)
	{
		try
		{
			int newBegin = -1;
			Matcher m = gramPattern.matcher(gramText);
			if (m.matches())
			{
				newBegin = m.group(1).length();
				boolean matchedLemma = false;
				for (SimpleSubRule rule : this.lemmaLogic)
				{
					if (rule.lemmaRestrict.matcher(lemma).matches())
					{
						paradigmCollector.addAll(rule.paradigms);
						if (rule.positiveFlags != null)
							flagCollector.addAll(rule.positiveFlags);
						matchedLemma = true;
						break;
					}
				}
				if (!matchedLemma)
				{
					System.err.printf(errorMessage, lemma);
					newBegin = 0;
				}

				if (alwaysFlags != null)
					for (Tuple<Keys, String> t : alwaysFlags)
						flagCollector.add(t.first, t.second);
			}
			return newBegin;
		} catch (Exception e)
		{
			System.err.println("BaseRule:" + patternText);
			throw e;
		}
    }
}
