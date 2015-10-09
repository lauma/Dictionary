package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.analyzer.struct.Flags;
import lv.ailab.tezaurs.utils.Tuple;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Vienkāršais likums - pārbauda, vai gramatika sākas ar noteiktu tekstu un vai
 * lemma atbilst dotai regulārai izteiksmei. Ja jā, pievieno vienu paradigmu.
 * @author Lauma
 */
public class SimpleRule implements Rule
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
	 * Lai likums būtu piemērojams, lemmai jāatbilst šim šablonam.
	 */
	protected final Pattern lemmaRestrict;
	/**
	 * Paradigmas ID, ko lieto, ja likums ir piemērojams (gan gramatikas teksts,
	 * gan lemma atbilst attiecīgajiem šabloniem).
	 */
	protected final int paradigmId;
	/**
	 * Šos karodziņus uzstāda, ja gan gramatikas teksts, gan lemma atbilst
	 * attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<Keys,String>> positiveFlags;
	/**
	 * Šos karodziņus uzstāda, ja gramatikas teksts atbilst attiecīgajam
	 * šablonam.
	 */
	protected final Set<Tuple<Keys,String>> alwaysFlags;

	public SimpleRule(String pattern, String lemmaRestrict, int paradigmId,
			Set<Tuple<Keys,String>> positiveFlags, Set<Tuple<Keys,String>> alwaysFlags)
	{
		this.patternText = pattern;
		directPattern = Pattern.compile("(\\Q" + patternText + "\\E)([;,.].*)?");
		String regExpPattern = patternText.replace("-", "\\E-?\\Q");
		optHyphenPattern = Pattern.compile("(\\Q" + regExpPattern + "\\E)([;,.].*)?");
		this.lemmaRestrict = Pattern.compile(lemmaRestrict);
		this.paradigmId = paradigmId;
		this.positiveFlags = positiveFlags == null? null : Collections.unmodifiableSet(positiveFlags);
		this.alwaysFlags = alwaysFlags == null ? null : Collections.unmodifiableSet(alwaysFlags);
	}

	/**
	 * Papildus konstruktors īsumam.
	 * Izveido SimpleRule, ja karodziņi doti masīvos, nevis kopās.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @param paradigmId		paradigmas ID, ko uzstādīt, ja likums ir
	 *                          piemērojams
	 * @param positiveFlags		karodziņi, ko uzstādīt, ja gan gramatikas
	 *                          teksts, gan lemma atbilst attiecīgajiem
	 *                          šabloniem.
	 * @param alwaysFlags		karodziņi, ko uzstādīt, ja gramatikas teksts
	 *                          atbilst attiecīgajam šablonam.
	 * @return	jauns SimpleRule
	 */
	public static SimpleRule of(String patternText, String lemmaRestrictions,
			int paradigmId, Tuple<Keys,String>[] positiveFlags, Tuple<Keys,String>[] alwaysFlags)
	{
		return new SimpleRule(patternText, lemmaRestrictions, paradigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Metode īsumam.
	 * Izveido SimpleRule 5. deklinācijas sieviešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return SimpleRule ar 9. paradigmu
	 */
	public static SimpleRule fifthDeclStd(String patternText, String lemmaRestrictions)
	{
		return SimpleRule.of(patternText, lemmaRestrictions, 9,
				new Tuple[]{Features.POS__NOUN, Features.GENDER__FEM}, null);
	}
	/**
	 * Metode īsumam.
	 * Izveido SimpleRule 2. deklinācijas vīriešu dzimtes lietvārdiem ar
	 * šķirkļa vārdu vienskaitlī.
	 * @param patternText		teksts, ar kuru jāsākas gramatikai
	 * @param lemmaRestrictions	regulārā izteiksme, kurai jāarbilst lemmai
	 * @return SimpleRule ar 3. paradigmu
	 */
	public static SimpleRule secondDeclStd(String patternText, String lemmaRestrictions)
	{
		return SimpleRule.of(patternText, lemmaRestrictions, 3,
				new Tuple[]{Features.POS__NOUN},
				new Tuple[]{Features.GENDER__MASC});
	}

	/**
	 * Piemērot likumu bez papildus maģijas.
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
	public int applyDirect (String gramText, String lemma,
			HashSet<Integer> paradigmCollector, Flags flagCollector)
	{
		return apply(directPattern, gramText, lemma, paradigmCollector, flagCollector);
	}

	/**
	 * Piemērot likumu tā, ka patternText defises ir neobligātas.
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
		int newBegin = -1;
		Matcher m = gramPattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			if (lemmaRestrict.matcher(lemma).matches())
			{
				paradigmCollector.add(paradigmId);
				if (positiveFlags != null)
					for (Tuple<Keys, String> t : positiveFlags)
						flagCollector.add(t.first, t.second);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt paradigmā %s\n", lemma, paradigmId);
				newBegin = 0;
			}
			if (alwaysFlags != null)
				for (Tuple<Keys, String> t : alwaysFlags)
					flagCollector.add(t.first, t.second);
		}
		return newBegin;
	}
}