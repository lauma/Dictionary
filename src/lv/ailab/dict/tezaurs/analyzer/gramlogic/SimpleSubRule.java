package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.utils.Tuple;

import java.util.*;
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
	public final Set<Tuple<String,String>> positiveFlags;
	/**
	 * Šos formu/struktūru ierobežojumus uzstāda, ja gan gramatikas teksts, gan
	 * lemma atbilst attiecīgajiem šabloniem.
	 */
	public Set<StructRestrs.One> positiveRestrictions;

	public SimpleSubRule(String lemmaRestrict, Set<Integer> paradigms,
			Set<Tuple<String, String>> positiveFlags, Set<StructRestrs.One> positiveRestrictions)
	{
		this.lemmaRestrict = Pattern.compile(lemmaRestrict);
		//this.paradigms = paradigms;
		this.paradigms = paradigms == null ? null :
				Collections.unmodifiableSet(paradigms);
		//this.positiveFlags = positiveFlags;
		this.positiveFlags = positiveFlags == null? null :
				Collections.unmodifiableSet(positiveFlags);
		this.positiveRestrictions = positiveRestrictions;
	}

	public SimpleSubRule(Pattern lemmaRestrict, Set<Integer> paradigms,
			Set<Tuple<String, String>> positiveFlags)
	{
		this.lemmaRestrict = lemmaRestrict;
		//this.paradigms = paradigms;
		this.paradigms = paradigms == null ? null :
				Collections.unmodifiableSet(paradigms);
		//this.positiveFlags = positiveFlags;
		this.positiveFlags = positiveFlags == null? null :
				Collections.unmodifiableSet(positiveFlags);
	}

	public static SimpleSubRule of(
			String lemmaRestrict, Integer[] paradigms,
			Tuple<String,String>[] positiveFlags)
	{
		return new SimpleSubRule(lemmaRestrict,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null);
	}

	public static SimpleSubRule of(
			String lemmaRestrict, Integer[] paradigms,
			Tuple<String,String>[] positiveFlags, StructRestrs.One positiveRestriction)
	{
		return new SimpleSubRule(lemmaRestrict,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				positiveRestriction == null ? null : new LinkedHashSet<StructRestrs.One>(){{add(positiveRestriction);}});
	}

	public static SimpleSubRule of(
			String lemmaRestrict, Integer[] paradigms,
			Tuple<String,String>[] positiveFlags, StructRestrs.One[] positiveRestrictions)
	{
		return new SimpleSubRule(lemmaRestrict,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				positiveRestrictions == null ? null : new LinkedHashSet<>(Arrays.asList(positiveRestrictions)));
	}

	public static SimpleSubRule of(String lemmaRestrict, Integer paradigm, Tuple<String,String>[] positiveFlags)
	{
		return new SimpleSubRule(lemmaRestrict,
				new HashSet<Integer>(){{add(paradigm);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				null);
	}

	/**
	 * Uztaisa šīs objekta kopiju un pievieno tam vēl arī doto karodziņu.
	 */
	public SimpleSubRule cloneWithFeature(Tuple<String, String> feature)
	{
		Set <Tuple<String, String>> pFlags = new HashSet<>();
		if (positiveFlags != null) pFlags.addAll(positiveFlags);
		pFlags.add(feature);
		return new SimpleSubRule(this.lemmaRestrict, this.paradigms, pFlags);
	}
}
