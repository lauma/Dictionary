package lv.ailab.dict.tezaurs.analyzer.gramlogic;

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
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
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
    //TODO: varbūt šeit vajag Flags objektu?
    protected final Set<Tuple<String,String>> alwaysFlags;

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
			Set<Tuple<String, String>> alwaysFlags)
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
			int paradigmId,	Set<Tuple<String, String>> positiveFlags,
			Set<Tuple<String, String>> alwaysFlags)
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
			Set<Integer> paradigms,	Set<Tuple<String, String>> positiveFlags,
			Set<Tuple<String, String>> alwaysFlags)
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
			Tuple<String,String>[] alwaysFlags)
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
			Tuple<String, String>[] positiveFlags,
			Tuple<String, String>[] alwaysFlags)
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
			Integer[] paradigms, Tuple<String, String>[] positiveFlags,
			Tuple<String, String>[] alwaysFlags)
	{
		return BaseRule.of(patternText, new SimpleSubRule[]{
						SimpleSubRule.of(lemmaRestrictions, paradigms, positiveFlags)},
				alwaysFlags);
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
					for (Tuple<String, String> t : alwaysFlags)
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
