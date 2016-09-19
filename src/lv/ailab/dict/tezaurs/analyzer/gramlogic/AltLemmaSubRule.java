package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
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
 * atbilstošas paradigmas un karodziņus, kā arī kā jāveido alternatīvā lemma.
 * Immutable.
 * Izveidots 2015-11-02.
 *
 * @author Lauma
 */
public class AltLemmaSubRule
{
	/**
	 * Lai likums būtu piemērojams, lemmai jāatbilst šim šablonam.
	 */
	protected final Pattern lemmaRestrict;

	/**
	 * Paradigmas ID, ko lieto, ja likums ir piemērojams (gan gramatikas teksts,
	 * gan lemma atbilst attiecīgajiem šabloniem).
	 */
	protected final Set<Integer> paradigms;
	/**
	 * Šos karodziņus uzstāda pamata karodziņu savācējam, ja gan gramatikas
	 * teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<TKeys,String>> positiveFlags;
	/**
	 * Simbolu skaits, kas tiks noņemts no dotās lemmas beigām, to izmantojot
	 * gramatikas šablona viedošanai.
	 */
	protected final int lemmaEndingCutLength;

	/**
	 * Teksta virkne kuru izmantos kā izskaņu, veidojot papildus lemmu.
	 */
	protected final String altLemmaEnding;
	/**
	 * Paradigmas ID, ko lieto papildus izveidotajai lemmai.
	 */
	protected final Set<Integer> altLemmaParadigms;
	/**
	 * Šos karodziņus uzstāda papildu pamatformai, nevis pamatvārdam, ja gan
	 * gramatikas teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<TKeys,String>> altLemmaFlags;

	public AltLemmaSubRule (String lemmaRestrict, Set<Integer>paradigms,
			Set<Tuple<TKeys,String>> positiveFlags, int lemmaEndingCutLength,
			String altLemmaEnding, Set<Integer> altLemmaParadigms,
			Set<Tuple<TKeys,String>> altLemmaFlags)
	{
		this.lemmaRestrict = Pattern.compile(lemmaRestrict);
		this.paradigms = Collections.unmodifiableSet(paradigms);
		this.positiveFlags = Collections.unmodifiableSet(positiveFlags);

		this.lemmaEndingCutLength = lemmaEndingCutLength;
		this.altLemmaEnding = altLemmaEnding;
		this.altLemmaParadigms = Collections.unmodifiableSet(altLemmaParadigms);
		this.altLemmaFlags = altLemmaFlags;
	}

	public static AltLemmaSubRule of (String lemmaRestrict, Integer[] paradigms,
			Tuple<TKeys,String>[] positiveFlags, int lemmaEndingCutLength,
			String altLemmaEnding, int altLemmaParadigm, Tuple<TKeys,String>[] altLemmaFlags)
	{
		return new AltLemmaSubRule(lemmaRestrict,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				lemmaEndingCutLength, altLemmaEnding, new HashSet<Integer>(){{add(altLemmaParadigm);}},
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}

	public static AltLemmaSubRule of (String lemmaRestrict, Integer[] paradigms,
			Tuple<TKeys,String>[] positiveFlags, int lemmaEndingCutLength,
			String altLemmaEnding, Integer[] altLemmaParadigms, Tuple<TKeys,String>[] altLemmaFlags)
	{
		return new AltLemmaSubRule(lemmaRestrict,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				lemmaEndingCutLength, altLemmaEnding,
				altLemmaParadigms == null ? null :  new HashSet<>(Arrays.asList(altLemmaParadigms)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}
}
