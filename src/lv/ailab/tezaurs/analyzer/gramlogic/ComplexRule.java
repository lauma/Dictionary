package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.utils.Trio;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Vispārīgais likums - ja gramatika atbilst dotajam šablonam, tad atkarībā no
 * tā, kādam šablonam atbilst lemma, tiek piekārtotas paradigmas un karodziņi.
 * Atbilstība lemmu šabloniem tiek pārbaudīta secīgi un, kad pirmā atbilsme ir
 * atrasta, tālāk nepārbauda (tātad uzdošanās kārtība ir svarīga).
 * @author Lauma
 */
public class ComplexRule implements Rule
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
     * Nokompilēts šablons likumam ar neoglibātām defisēm (iegūts no
     * patternText).
     */
    protected final Pattern optHyphenPattern;

    /**
     * Vairāki komplekti ar nosacījumiem: lemmas ierobežojumi un paradigmu un
     * karodziņu kopas, kas lietojami, ja attiecīgais lemmas ierobežojums
     * izpildās.
     */
    protected final List<Trio<Pattern, Set<Integer>, Set<String>>> lemmaLogic;
    /**
     * Šos karodziņus uzstāda, ja gramatikas teksts atbilst attiecīgajam
     * šablonam.
     */
    protected final Set<String> alwaysFlags;

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
    public ComplexRule(String pattern,
            List<Trio<Pattern, Set<Integer>, Set<String>>> lemmaLogic,
            Set<String> alwaysFlags)

    {
        if (lemmaLogic == null)
            throw new IllegalArgumentException (
                    "Nav paredzēts, ka ComplexRule tiek viedots vispār bez lemmu nosacījumiem!");
        this.patternText = pattern;
        directPattern = Pattern.compile("(\\Q" + patternText + "\\E)([;,.].*)?");
        String regExpPattern = patternText.replace("-", "\\E-?\\Q");
        optHyphenPattern = Pattern.compile("(\\Q" + regExpPattern + "\\E)([;,.].*)?");

        this.lemmaLogic = Collections.unmodifiableList(lemmaLogic);
        this.alwaysFlags = alwaysFlags == null ? null : Collections.unmodifiableSet(alwaysFlags);

        errorMessage = "Problem matching \"%s\" with paradigm " +
                lemmaLogic.stream().map(t -> t.second).flatMap(m -> m.stream())
                        .distinct().sorted().map(i -> i.toString())
                        .reduce((a, b) -> a + ", " + b).orElse("")+ "\n";
    }
    /**
     * Papildus konstruktors īsumam.
     * Izveido ComplexRule, ja lemmu nosacījumu doti masīvu struktūrā, nevis
     * sarakstos un kopās.
     * @param patternText		teksts, ar kuru jāsākas gramatikai
     * @param lemmaLogic		nosacījumu saraksts, kurā katrs elements sastāv
     *                          no trijnieka: 1)lemmu aprakstoša šablona, 2)
     *                          paradigmām, ko lietot, ja lemma atbilst šim
     *                          šablonam, 3) karodziņiem, ko uzstādīt, ja lemma
     *                          atbilst šim šablonam.
     * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
     *                          atbilst attiecīgajam šablonam.
     * @return	jauns ComplexRule
     */
    public static ComplexRule of (String patternText,
            Trio<String, Integer[], String[]>[] lemmaLogic,
            String[] alwaysFlags)
    {
        ArrayList<Trio<Pattern, Set<Integer>, Set<String>>> tmp = new ArrayList<>();
        for (Trio<String, Integer[], String[]> t : lemmaLogic)
            tmp.add(Trio.of(Pattern.compile(t.first),
                    Collections.unmodifiableSet(new HashSet<Integer>(Arrays.asList(t.second))),
                    t.third == null ? null : Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(t.third)))));
        return new ComplexRule(patternText, tmp,
                alwaysFlags == null ? null : new HashSet<String>(Arrays.asList(alwaysFlags)));
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
            HashSet<Integer> paradigmCollector, HashSet<String> flagCollector)
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
            HashSet<Integer> paradigmCollector, HashSet<String> flagCollector)
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
            HashSet<Integer> paradigmCollector,
            HashSet<String> flagCollector)
    {
        int newBegin = -1;
        Matcher m = gramPattern.matcher(gramText);
        if (m.matches())
        {
            newBegin = m.group(1).length();
            boolean matchedLemma = false;
            for (Trio<Pattern, Set<Integer>, Set<String>> rule : this.lemmaLogic)
            {
                if (rule.first.matcher(lemma).matches())
                {
                    paradigmCollector.addAll(rule.second);
                    if (rule.third != null)
                        flagCollector.addAll(rule.third);
                    matchedLemma = true;
                    break;
                }
            }
            if (!matchedLemma)
            {
                System.err.printf(errorMessage, lemma);
                newBegin = 0;
            }
            if (alwaysFlags != null) flagCollector.addAll(alwaysFlags);
        }
        return newBegin;
    }
}
