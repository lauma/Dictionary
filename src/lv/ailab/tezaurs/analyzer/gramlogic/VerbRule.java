package lv.ailab.tezaurs.analyzer.gramlogic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Rule that contains two verb-specific grammar string cases, each of them is
 * represented as SimpleRule. One case is grammar for all persons, other is for
 * third person only. Example: "-brāžu, -brāz, -brāž, pag. -brāzu" and
 * "parasti 3. pers., -brāž, pag. -brāzu".
 * @author Lauma
 *
 */
public class VerbRule implements Rule
{
	protected SimpleRule allPersonRule;
	protected ThirdPersVerbRule thirdPersonRule;
	
	/**
	 * @param patternBegin	part of the grammar string containing endings for
	 * 						1st and 2nd person
	 * @param patternEnd	part of the grammar string containing endings for
	 * 						3rd parson in present and past
	 * @param lemmaEnd		required ending for the lemma to apply this rule
	 * @param paradigmId	paradigm ID to set if rule matched
	 * @param positiveFlags	flags to set if rule patternText and lemma ending
	 * 						matched
	 * @param alwaysFlags	flags to set if rule patternText matched
	 */
	public VerbRule(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			Set<String> positiveFlags, Set<String> alwaysFlags)
	{
		HashSet<String> positiveFlagsFull = new HashSet<>();
		positiveFlagsFull.add("Darbības vārds");
		if (positiveFlags != null) positiveFlagsFull.addAll(positiveFlags);
		HashSet<String> alwaysFlagsSet = alwaysFlags == null ? null : new HashSet<>(alwaysFlags);

		String begin = patternBegin.trim();
		String end = patternEnd.trim();
		String allPersonPattern = begin + " " + end;
		String thirdPersonPattern;
		if (end.endsWith("u"))
			thirdPersonPattern = end.substring(0, end.length()-1) + "a";
		else if (end.endsWith("os"))
			thirdPersonPattern = end.substring(0, end.length()-2) + "ās";
		else
		{
			System.err.printf("Could not figure out third-person-only rule for grammar pattern \"%s\"\n", allPersonPattern);
			thirdPersonPattern = allPersonPattern;
		}
		allPersonRule = new SimpleRule(allPersonPattern,
				".*" + lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
		thirdPersonRule = new ThirdPersVerbRule(thirdPersonPattern,
				lemmaEnd, paradigmId, positiveFlagsFull, alwaysFlagsSet);
	}

	public static VerbRule of(String patternBegin, String patternEnd,
			String lemmaEnd, int paradigmId,
			String[] positiveFlags, String[] alwaysFlags)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, paradigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)));
	}

	/**
	 * Create simple VerbRule for 1st conjugation direct verbs without parallel
	 * forms or infinitive homoforms.
	 * @param patternBegin	part of the grammar string containing endings for
	 * 						1st and 2nd person
	 * @param patternEnd	part of the grammar string containing endings for
	 * 						3rd parson in present and past
	 * @param lemmaEnd		required ending for the lemma to apply this rule
	 * @return VerbRule with Paradigm 15 and flag about conjugating.
	 * TODO add IDs from lexicon
	 */
	public static VerbRule firstConjDir(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return VerbRule.of(patternBegin, patternEnd, lemmaEnd,
				15, new String[] {"Locīt kā \""+ lemmaEnd + "\""}, null);
	}
	
	/**
	 * Create simple VerbRule for 2nd conjugation direct verbs without parallel
	 * forms.
	 * @param patternBegin	part of the grammar string containing endings for
	 * 						1st and 2nd person
	 * @param patternEnd	part of the grammar string containing endings for
	 * 						3rd parson in present and past
	 * @param lemmaEnd		required ending for the lemma to apply this rule
	 * @return VerbRule with Paradigm 16
	 */
	public static VerbRule secondConjDir(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 16, null, null);
	}
	
	/**
	 * Create simple VerbRule for 3rd conjugation direct verbs without parallel
	 * forms.
	 * @param patternBegin	part of the grammar string containing endings for
	 * 						1st and 2nd person
	 * @param patternEnd	part of the grammar string containing endings for
	 * 						3rd parson in present and past
	 * @param lemmaEnd		required ending for the lemma to apply this rule
	 * @return VerbRule with Paradigm 17
	 */
	public static VerbRule thirdConjDir(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 17, null, null);
	}
	
	/**
	 * Create simple VerbRule for 1st conjugation reflexive verbs without
	 * parallel forms or infinitive homoforms.
	 * @param patternBegin	part of the grammar string containing endings for
	 * 						1st and 2nd person
	 * @param patternEnd	part of the grammar string containing endings for
	 * 						3rd parson in present and past
	 * @param lemmaEnd		required ending for the lemma to apply this rule
	 * @return VerbRule with Paradigm 18 and flag about conjugating.
	 * TODO add IDs from lexicon
	 */
	public static VerbRule firstConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return VerbRule.of(patternBegin, patternEnd, lemmaEnd,
				18, new String[] {"Locīt kā \""+ lemmaEnd + "\""}, null);
	}
	
	/**
	 * Create simple VerbRule for 2nd conjugation reflexive verbs without
	 * parallel forms.
	 * @param patternBegin	part of the grammar string containing endings for
	 * 						1st and 2nd person
	 * @param patternEnd	part of the grammar string containing endings for
	 * 						3rd parson in present and past
	 * @param lemmaEnd		required ending for the lemma to apply this rule
	 * @return VerbRule with Paradigm 19
	 */
	public static VerbRule secondConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 19, null, null);
	}
	
	/**
	 * Create simple VerbRule for 3rd conjugation reflexive verbs without 
	 * parallel forms.
	 * @param patternBegin	part of the grammar string containing endings for
	 * 						1st and 2nd person
	 * @param patternEnd	part of the grammar string containing endings for
	 * 						3rd parson in present and past
	 * @param lemmaEnd		required ending for the lemma to apply this rule
	 * @return VerbRule with Paradigm 20
	 */
	public static VerbRule thirdConjRefl(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		return new VerbRule(patternBegin, patternEnd, lemmaEnd, 20, null, null);
	}
	
	/**
	 * Apply rule as-is - no magic whatsoever.
	 * @param gramText			Grammar string currently being processed.
	 * @param lemma				Lemma string for this header.
	 * @param paradigmCollector	Map, where paradigm will be added, if rule
	 * 							matches.
	 * @param flagCollector	Map, where flags will be added, if rule
	 * 							matches.
	 * @return New beginning for gram string if one of these rules matched,
	 * -1 otherwise.
	 */
	public int applyDirect (
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		int newBegin = thirdPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		if (newBegin == -1)
			newBegin = allPersonRule.applyDirect(gramText, lemma, paradigmCollector, flagCollector);
		return newBegin;
	}
	
	/**
	 * Apply rule, but hyperns in patternText are optional.
	 * @param gramText			Grammar string currently being processed.
	 * @param lemma				Lemma string for this header.
	 * @param paradigmCollector	Map, where paradigm will be added, if rule
	 * 							matches.
	 * @param flagCollector		Map, where flags will be added, if rule
	 * 							matches.
	 * @return New beginning for gram string if one of these rules matched,
	 * -1 otherwise.
	 */
	public int applyOptHyphens(
			String gramText, String lemma,
			HashSet<Integer> paradigmCollector,
			HashSet<String> flagCollector)
	{
		int newBegin = thirdPersonRule.applyOptHyphens(gramText, lemma,
				paradigmCollector, flagCollector);
		if (newBegin == -1)
			newBegin = allPersonRule.applyOptHyphens(gramText, lemma,
					paradigmCollector, flagCollector);
		return newBegin;		
	}


}
