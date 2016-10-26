package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.utils.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Gramatikas apstrādes likuma gabals, kas apraksta, kas notiek, kad piemeklēta
 * atbilstība noteiktam gramatikas teksta šablonam. Šeit tiek aprakstīts, kādam
 * lemmas šablonam ir jāatbilst apstrādājamajai lemmai, lai piešķirtu
 * atbilstošas paradigmas un karodziņus, kā arī kā jāveido alternatīvā
 * lemma/forma.
 * Immutable.
 * Izveidots 2015-11-02.
 *
 * @author Lauma
 */
public class StemSlotSubRule
{
	/**
	 * Lai likums būtu piemērojams, lemmai jāatbilst šim šablonam.
	 */
	protected final Pattern lemmaRestrict;
	/**
	 * Simbolu skaits, kas tiks noņemts no dotās lemmas beigām, to izmantojot
	 * gramatikas šablona viedošanai.
	 */
	protected final int lemmaEndingCutLength;

	/**
	 * Paradigmas ID, ko lieto, ja likums ir piemērojams (gan gramatikas teksts,
	 * gan lemma atbilst attiecīgajiem šabloniem).
	 */
	protected final Set<Integer> paradigms;
	/**
	 * Šos karodziņus uzstāda pamata karodziņu savācējam, ja gan gramatikas
	 * teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<String,String>> positiveFlags;

	/**
	 * Teksta virkne kuru izmantos kā izskaņu, veidojot papildus lemmu/vārdformu.
	 */
	protected final String altWordEnding;
	/**
	 * Paradigmas ID, ko lieto papildus izveidotajai lemmai/formai.
	 */
	protected final Set<Integer> altWordParadigms;
	/**
	 * Šos karodziņus uzstāda papildu pamatformai, nevis pamatvārdam, ja gan
	 * gramatikas teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<String,String>> altWordFlags;

	public StemSlotSubRule(String lemmaRestrict, int lemmaEndingCutLength,
			Set<Integer>paradigms,	Set<Tuple<String,String>> positiveFlags,
			String altWordEnding, Set<Integer> altWordParadigms,
			Set<Tuple<String,String>> altWordFlags)
	{
		this.lemmaRestrict = Pattern.compile(lemmaRestrict);
		this.lemmaEndingCutLength = lemmaEndingCutLength;

		this.paradigms = paradigms == null ? null : Collections.unmodifiableSet(paradigms);
		this.positiveFlags = positiveFlags == null ? null : Collections.unmodifiableSet(positiveFlags);

		this.altWordEnding = altWordEnding;
		this.altWordParadigms = altWordParadigms == null ? null :Collections.unmodifiableSet(altWordParadigms);
		this.altWordFlags = altWordFlags == null ? null :Collections.unmodifiableSet(altWordFlags);
	}

	public static StemSlotSubRule of (String lemmaRestrict, Integer[] paradigms,
			Tuple<String,String>[] positiveFlags, int lemmaEndingCutLength,
			String altLemmaEnding, int altLemmaParadigm, Tuple<String,String>[] altLemmaFlags)
	{
		return new StemSlotSubRule(lemmaRestrict, lemmaEndingCutLength,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				altLemmaEnding, new HashSet<Integer>(){{add(altLemmaParadigm);}},
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}

	public static StemSlotSubRule of (String lemmaRestrict, Integer[] paradigms,
			Tuple<String,String>[] positiveFlags, int lemmaEndingCutLength,
			String altLemmaEnding, Integer[] altLemmaParadigms, Tuple<String,String>[] altLemmaFlags)
	{
		return new StemSlotSubRule(lemmaRestrict, lemmaEndingCutLength,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				altLemmaEnding,
				altLemmaParadigms == null ? null :  new HashSet<>(Arrays.asList(altLemmaParadigms)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}
}
