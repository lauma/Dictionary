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
	 * Required ending for the lemma to apply this rule.
	 */
	protected final String lemmaEnding;
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

	public SimpleRule(String pattern, String lemmaEnding, int paradigmId,
			Set<String> positiveFlags, Set<String> alwaysFlags)
	{
		this.patternText = pattern;
		directPattern = Pattern.compile("\\Q" + patternText + "\\E([;,.].*)?");
		String regExpPattern = patternText.replace("-", "\\E-?\\Q");
		optHyphenPattern = Pattern.compile("(\\Q" + regExpPattern + "\\E)([;,.].*)?");
		this.lemmaEnding = lemmaEnding;
		this.paradigmId = paradigmId;
		this.positiveFlags = positiveFlags == null? null : Collections.unmodifiableSet(positiveFlags);
		this.alwaysFlags = alwaysFlags == null ? null : Collections.unmodifiableSet(alwaysFlags);
	}

	/**
	 * Constructor method for convenience - make SimpleRule if flags are given
	 * in arrays, not sets.
	 * @param patternText	text grammar string must start with
	 * @param lemmaEnding	required ending for the lemma to apply this rule
	 * @param paradigmId	paradigm ID to set if rule matched
	 * @param positiveFlags	flags to set if rule patternText and lemma ending
	 * 						matched
	 * @param alwaysFlags	flags to set if rule patternText matched
	 * @return	new SimpleRule
	 */
	public static SimpleRule of(String patternText, String lemmaEnding,
			int paradigmId, String[] positiveFlags, String[] alwaysFlags)
	{
		return new SimpleRule(patternText, lemmaEnding, paradigmId,
				positiveFlags == null ? null : new HashSet<String>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<String>(Arrays.asList(alwaysFlags)));
	}
	
	/**
	 * Apply rule as-is - no magic whatsoever.
	 * @param gramText			Grammar string currently being processed.
	 * @param lemma				Lemma string for this header.
	 * @param paradigmCollector	Map, where paradigm will be added, if rule
	 * 							matches.
	 * @param flagCollector		Map, where flags will be added, if rule
	 * 							matches.
	 * @return New beginning for gram string if one of these rules matched,
	 * -1 otherwise.
	 */
	public int applyDirect (
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		int newBegin = -1;
		if (directPattern.matcher(gramText).matches())
		{
			newBegin = patternText.length();
			if (lemma.endsWith(lemmaEnding))
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
	
	/**
	 * Apply rule, but hyperns in patternText are optional.
	 * @param gramText			Grammar string currently being processed.
	 * @param lemma				Lemma string for this header.
	 * @param paradigmCollector	Map, where paradigm will be added, if rule
	 * 							matches.
	 * @param flagCollector	Map, where flags will be added, if rule
	 * 							matches.
	 * @return New beginning for gram string if one of these rules matched,
	 * -1 otherwise.
	 */
	public int applyOptHyphens(
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		int newBegin = -1;
		Matcher m = optHyphenPattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			if (lemma.endsWith(lemmaEnding))
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