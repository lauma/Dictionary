package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.utils.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Gramatikas apstrādes likuma gabals, kas apraksta, kas notiek, kad piemeklēta
 * atbilstība noteiktam gramatikas teksta šablonam. Šeit tiek aprakstīts, kādam
 * lemmas šablonam ir jāatbilst apstrādājamajai lemmai, lai piešķirtu
 * atbilstošas paradigmas un karodziņus.
 * Immutable.
 * Izveidots 2015-11-02.
 *
 * @author Lauma
 */
public class SimpleSubRule
{
	/**
	 * Neeskepota teksta virkne, ar kuru grmatikai jāsākas, lai šis likums būtu
	 * piemērojams.
	 */
	public final Pattern lemmaRestrict;
	/**
	 * Paradigmas ID, ko lieto, ja likums ir piemērojams (gan gramatikas teksts,
	 * gan lemma atbilst attiecīgajiem šabloniem).
	 */
	public final Set<Integer> paradigms;
	/**
	 * Šos karodziņus uzstāda, ja gan gramatikas teksts, gan lemma atbilst
	 * attiecīgajiem šabloniem.
	 */
	public final Set<Tuple<Keys,String>> positiveFlags;

	public SimpleSubRule(String lemmaRestrict, Set<Integer> paradigms,
			Set<Tuple<Keys, String>> positiveFlags)
	{
		this.lemmaRestrict = Pattern.compile(lemmaRestrict);
		//this.paradigms = paradigms;
		this.paradigms = paradigms == null ? null :
				Collections.unmodifiableSet(paradigms);
		//this.positiveFlags = positiveFlags;
		this.positiveFlags = positiveFlags == null? null :
				Collections.unmodifiableSet(positiveFlags);
	}

	public SimpleSubRule(Pattern lemmaRestrict, Set<Integer> paradigms,
			Set<Tuple<Keys, String>> positiveFlags)
	{
		this.lemmaRestrict = lemmaRestrict;
		//this.paradigms = paradigms;
		this.paradigms = paradigms == null ? null :
				Collections.unmodifiableSet(paradigms);
		//this.positiveFlags = positiveFlags;
		this.positiveFlags = positiveFlags == null? null :
				Collections.unmodifiableSet(positiveFlags);
	}

	public static SimpleSubRule of(String lemmaRestrict, Integer[] paradigms, Tuple<Keys,String>[] positiveFlags)
	{
		return new SimpleSubRule(lemmaRestrict,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)));
	}

	/**
	 * Uztaisa šīs objekta kopiju un pievieno tam vēl arī doto karodziņu.
	 */
	public SimpleSubRule cloneWithFeature(Tuple<Keys, String> feature)
	{
		Set <Tuple<Keys, String>> pFlags = new HashSet<>();
		if (positiveFlags != null) pFlags.addAll(positiveFlags);
		pFlags.add(feature);
		return new SimpleSubRule(this.lemmaRestrict, this.paradigms, pFlags);
	}
}
