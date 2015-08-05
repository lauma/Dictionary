package lv.ailab.tezaurs.analyzer.gramlogic;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple rule - tries to match grammar text to given string and lemma
 * ending. If matched, adds a single paradigm.
 * @author Lauma
 */
public class SimpleRule implements Rule
{
	/**
	 * Un-escaped ending string grammar text must begin with to apply this
	 * rule.
	 */
	protected final String patternText;

	/**
	 * Compiled pattern for direct rule (derived from patternText).
	 */
	protected final Pattern directPattern;
	/**
	 * Compiled pattern for optional hyphen rule (derived from patternText).
	 */
	protected final Pattern optHyphenPattern;
	/**
	 * To apply rule lemma must match this regular expression.
	 */
	protected final Pattern lemmaRestrict;
	/**
	 * Paradigm ID to set if rule matched.
	 */
	protected final int paradigmId;
	/**
	 * These flags are added if rule patternText and lemma ending matched.
	 */
	protected final Set<String> positiveFlags;
	/**
	 * These flags are added if rule patternText matched.
	 */
	protected final Set<String> alwaysFlags;

	public SimpleRule(String pattern, String lemmaRestrict, int paradigmId,
			Set<String> positiveFlags, Set<String> alwaysFlags)
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
	 * Constructor method for convenience - make SimpleRule if flags are given
	 * in arrays, not sets.
	 * @param patternText		text grammar string must start with
	 * @param lemmaRestrictions	to apply rule lemma must match this regular
	 *                          expression
	 * @param paradigmId		paradigm ID to set if rule matched
	 * @param positiveFlags		flags to set if rule patternText and lemma
	 * 							ending matched
	 * @param alwaysFlags		flags to set if rule patternText matched
	 * @return	new SimpleRule
	 */
	public static SimpleRule of(String patternText, String lemmaRestrictions,
			int paradigmId, String[] positiveFlags, String[] alwaysFlags)
	{
		return new SimpleRule(patternText, lemmaRestrictions, paradigmId,
				positiveFlags == null ? null : new HashSet<String>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<String>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Shortcut method.
	 * Creates SimpleRule for 5th declension nouns if entry word is singular and
	 * feminine.
	 * @param patternText		text grammar string must start with
	 * @param lemmaRestrictions	to apply rule lemma must match this regular
	 *                          expression
	 * @return SimpleRule with paradigm 9
	 */
	public static SimpleRule fifthDeclStd(String patternText, String lemmaRestrictions)
	{
		return SimpleRule.of(patternText, lemmaRestrictions, 9,
				new String[]{"Lietvārds"},
				new String[]{"Sieviešu dzimte"});
	}
	/**
	 * Shortcut method.
	 * Creates SimpleRule for 1th declension nouns if entry word is singular and
	 * masculine.
	 * @param patternText		text grammar string must start with
	 * @param lemmaRestrictions	to apply rule lemma must match this regular
	 *                          expression
	 * @return SimpleRule with paradigm 3
	 */
	public static SimpleRule secondDeclStd(String patternText, String lemmaRestrictions)
	{
		return SimpleRule.of(patternText, lemmaRestrictions, 3,
				new String[]{"Lietvārds"},
				new String[]{"Vīriešu dzimte"});
	}

	/**
	 * Apply rule as-is - no magic whatsoever.
	 * @param gramText			Grammar string currently being processed.
	 * @param lemma				Lemma string for this header.
	 * @param paradigmCollector	Map, where paradigm will be added, if rule
	 * 							matches.
	 * @param flagCollector		Map, where flags will be added, if rule
	 * 							matches.
	 * @return New beginning for gram string if rule matched, -1 otherwise.
	 */
	public int applyDirect (
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		return apply(directPattern, gramText, lemma, paradigmCollector, flagCollector);
	}
	
	/**
	 * Apply rule, but hyperns in patternText are optional.
	 * @param gramText			Grammar string currently being processed.
	 * @param lemma				Lemma string for this header.
	 * @param paradigmCollector	Map, where paradigm will be added, if rule
	 * 							matches.
	 * @param flagCollector	Map, where flags will be added, if rule
	 * 							matches.
	 * @return New beginning for gram string if rule matched, -1 otherwise.
	 */
	public int applyOptHyphens(
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		return apply(optHyphenPattern, gramText, lemma, paradigmCollector, flagCollector);
	}

	/**
	 * Apply rule, determining match by provided pattern.
	 * Internal function.
	 * @param gramPattern		Pattern for determining if rule should be
	 *                          applied.
	 * @param gramText			Grammar string currently being processed.
	 * @param lemma				Lemma string for this header.
	 * @param paradigmCollector	Map, where paradigm will be added, if rule
	 * 							matches.
	 * @param flagCollector	Map, where flags will be added, if rule
	 * 							matches.
	 * @return New beginning for gram string if rule matched, -1 otherwise.
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
			if (lemmaRestrict.matcher(lemma).matches())
			{
				paradigmCollector.add(paradigmId);
				if (positiveFlags != null)
					flagCollector.addAll(positiveFlags);
			}
			else
			{
				System.err.printf("Problem matching \"%s\" with paradigm %s\n", lemma, paradigmId);
				newBegin = 0;
			}
			if (alwaysFlags != null) flagCollector.addAll(alwaysFlags);
		}
		return newBegin;
	}
}