package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.tezaurs.struct.TElementFactory;
import lv.ailab.dict.tezaurs.struct.THeader;
import lv.ailab.dict.utils.Tuple;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Likumi, kas tikai ar galotnēm norāda, ka jāizveido alternatīvā lemma.
 * Vienākāršais likums - gramatikas šablonam atbilst viens galotnes šablons,
 * viena pamata paradigma ar vienu alternatīvo lemmu, kurai atkal ir viena
 * paradigma.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * Izveidots 2015-10-26.
 * @author Lauma
 */
public class AltEndingRule implements AdditionalHeaderRule
{
	/**
	 * Neeskepota teksta virkne, ar kuru grmatikai jāsākas, lai šis likums būtu
	 * piemērojams.
	 */
	protected final String patternText;
	/**
	 * Nokompilēts šablons, kuram jāatbilst grmatikai, lai šis likums būtu
	 * piemērojams.
	 */
	protected final Pattern pattern;

	/**
	 * Masīvs ar likuma "otrājām pusēm" - lemmas nosacījumi, piešķiramās
	 * paradigmas un karodziņi, kā arī alternatīvās lemmas veidošanas dati un
	 * tai piešķiramie karodziņi.
	 */
	protected final List<StemSlotSubRule> lemmaLogic;

	/**
	 * Šo izdrukā, kad liekas, ka likums varētu būt nepilnīgs - gramatikas
	 * šablonam atbilstība tiek fiksēta, bet lemmu šabloniem - nē.
	 */
	protected final String errorMessage;

	/**
	 * Skaitītājs, kas norāda, cik reižu likums ir ticis lietots (applyDirect).
	 */
	protected int usageCount = 0;

	public AltEndingRule(String patternText, List<StemSlotSubRule> lemmaLogic)
	{
		if (lemmaLogic == null)
			throw new IllegalArgumentException (
					"Nav paredzēts, ka " + getClass().getSimpleName()+
							" tiek viedots vispār bez lemmu nosacījumiem!");
		this.patternText = patternText;
		this.pattern = Pattern.compile("(\\Q" + patternText + "\\E)([;,.].*)?");
		this.lemmaLogic = Collections.unmodifiableList(lemmaLogic);

		if (lemmaLogic.size() == 1 && lemmaLogic.get(0).paradigms.size() == 1
				&& lemmaLogic.get(0).altWordParadigms.size() == 1)
			errorMessage = "Neizdodas \"%s\" ielikt paradigmā " +
					lemmaLogic.get(0).paradigms.toArray()[0] +
					" ar papildus paradigmu " + lemmaLogic.get(0).altWordParadigms
					.toArray()[0] + "\n";
		else errorMessage = "Neizdodas \"%s\" ielikt kādā no paradigmām " +
				lemmaLogic.stream().map(t -> t.paradigms).flatMap(m -> m.stream())
						.distinct().sorted().map(i -> i.toString())
						.reduce((a, b) -> a + ", " + b).orElse("") +
				" ar papildus paradigmām " + lemmaLogic.stream()
						.map(t->t.altWordParadigms).flatMap(m -> m.stream()).distinct().sorted()
						.map(i -> i.toString()).reduce((a, b) -> a + ", " + b)
						.orElse("") + "\n";
	}

	/**
	 * Konstruktors īsumam - gadījumiem, kad likums apskata tikai vienu lemmas
	 * nosacījumu.
	 */
	public static AltEndingRule simple(
			String patternText, String lemmaRestrictions, int lemmaEndCutLength,
			Set<Integer> paradigms, Set<Tuple<String, String>> positiveFlags,
			Set<StructRestrs.One> positiveRestrictions, String altLemmaEnd,
			Set<Integer> altLemmaParadigms, Set<Tuple<String, String>> altLemmaFlags,
			Set<StructRestrs.One> altLemmaRestrictions)
	{
		return new AltEndingRule(patternText,
				new ArrayList<StemSlotSubRule>(){{
					add( new StemSlotSubRule(lemmaRestrictions,lemmaEndCutLength,
							paradigms, positiveFlags, positiveRestrictions,
							altLemmaEnd, altLemmaParadigms, altLemmaFlags,
							altLemmaRestrictions));}});
	}

	/**
	 * Konstruktors īsumam - gadījumiem, kad likums apskata tikai vienu lemmas
	 * nosacījumu ar vienu paradigmu un jaunajai lemmai ari ir viena paradigma
	 */
	public static AltEndingRule simple(
			String patternText, String lemmaRestrictions, int lemmaEndCutLength,
			int paradigm, Set<Tuple<String, String>> positiveFlags,
			Set<StructRestrs.One> positiveRestrictions, String altLemmaEnd,
			int altLemmaParadigm, Set<Tuple<String, String>> altLemmaFlags,
			Set<StructRestrs.One> altLemmaRestrictions)
	{
		return new AltEndingRule(patternText,
				new ArrayList<StemSlotSubRule>(){{
					add( new StemSlotSubRule(lemmaRestrictions, lemmaEndCutLength,
							new HashSet<Integer>(){{add(paradigm);}},
							positiveFlags, positiveRestrictions, altLemmaEnd,
							new HashSet<Integer>(){{add(altLemmaParadigm);}},
							altLemmaFlags, altLemmaRestrictions));}});
	}

	public static AltEndingRule of(String patternText, StemSlotSubRule[] lemmaLogic)
	{
		return new AltEndingRule(patternText,
				lemmaLogic == null ? null : Arrays.asList(lemmaLogic));
	}

	public static AltEndingRule of(String patternText, String lemmaRestrictions,
			int lemmaEndCutLength, Integer[] paradigms, Tuple<String, String>[] positiveFlags,
			String altLemmaEnd, Integer[] altLemmaParadigms, Tuple<String, String>[] altLemmaFlags)
	{
		return simple(patternText, lemmaRestrictions, lemmaEndCutLength,
				paradigms == null ? null : new HashSet<>(Arrays
						.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays
						.asList(positiveFlags)),
				null, altLemmaEnd,
				altLemmaParadigms == null ? null : new HashSet<>(Arrays
						.asList(altLemmaParadigms)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays
						.asList(altLemmaFlags)),
				null);
	}

	public static AltEndingRule of(String patternText, String lemmaRestrictions,
			int lemmaEndingCutLength, int paradigm, Tuple<String, String>[] positiveFlags,
			String altLemmaEnding, int altLemmaParadigm, Tuple<String, String>[] altLemmaFlags)
	{
		return simple(patternText, lemmaRestrictions, lemmaEndingCutLength,
				paradigm,
				positiveFlags == null ? null : new HashSet<>(Arrays
						.asList(positiveFlags)),
				null, altLemmaEnding, altLemmaParadigm,
				altLemmaFlags == null ? null : new HashSet<>(Arrays
						.asList(altLemmaFlags)),
				null);
	}

	public static AltEndingRule of(
			String patternText, String lemmaRestrictions, int lemmaEndingCutLength,
			int paradigm, Tuple<String, String>[] positiveFlags,
			StructRestrs.One positiveRestriction, String altLemmaEnding,
			int altLemmaParadigm, Tuple<String, String>[] altLemmaFlags,
			StructRestrs.One altLemmaRestriction)
	{
		return simple(patternText, lemmaRestrictions, lemmaEndingCutLength,
				paradigm,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				positiveRestriction == null ? null : new HashSet<StructRestrs.One>(){{add(positiveRestriction);}},
				altLemmaEnding, altLemmaParadigm,
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)),
				altLemmaRestriction == null ? null : new HashSet<StructRestrs.One>(){{add(altLemmaRestriction);}});
	}


	/**
	 * Likuma piemērošana.
	 *
	 * @param gramText           apstrādājamā gramatika
	 * @param lemma              hederim, kurā atrodas gramatika, atbilstošā
	 *                           lemma
	 * @param paradigmCollector  kolekcija, kurā pielikt paradigmu gadījumā,
	 *                           ja gramatika un lemma atbilst šim likumam
	 * @param flagCollector      kolekcija, kurā pielikt karodziņus gadījumā,
	 *                           ja vismaz gramatika atbilst šim likumam
	 * @param restrCollector     kolekcija, kurā pielikt ierobežojumus gadījumā,
	 *                           ja vismaz gramatika atbilst šim likumam
	 * @param altLemmasCollector kolekcija, kurā ielikt izveidotās papildus
	 *                           formas un tām raksturīgos karodziņus.
	 * @return jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 * daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int applyDirect(
			String gramText, String lemma, Set<Integer> paradigmCollector,
			Flags flagCollector, StructRestrs restrCollector,
			List<Header> altLemmasCollector)
	{
		int newBegin = -1;
		Matcher m = pattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			boolean matchedLemma = false;
			for (StemSlotSubRule rule : lemmaLogic)
			{
				if (rule.lemmaRestrict.matcher(lemma).matches()
						&& lemma.length() >= rule.lemmaEndingCutLength)
				{
					String lemmaStub = lemma.substring(0, lemma.length() - rule.lemmaEndingCutLength);
					Lemma altLemma = TElementFactory.me().getNewLemma();
					altLemma.text = lemmaStub + rule.altWordEnding;
					Flags altParams = TElementFactory.me().getNewFlags();
					if (rule.altWordFlags != null)
						altParams.addAll(rule.altWordFlags);
					StructRestrs altRestrs = TElementFactory.me().getNewStructRestrs();
					if (rule.altWordRestrictions != null)
						altRestrs.restrictions.addAll(rule.altWordRestrictions);

					THeader tmp = TElementFactory.me().getNewHeader();
					tmp.reinitialize(TElementFactory.me(), altLemma, rule.altWordParadigms, altParams, altRestrs);
					altLemmasCollector.add(tmp);

					paradigmCollector.addAll(rule.paradigms);
					if (rule.positiveFlags != null)
						flagCollector.addAll(rule.positiveFlags);
					if (rule.positiveRestrictions != null)
						restrCollector.restrictions.addAll(rule.positiveRestrictions);

					matchedLemma = true;
					break;
				}
			}

			if (!matchedLemma)
			{
				System.err.printf(errorMessage, lemma);
				newBegin = 0;
			}
		}
		if (newBegin > -1) usageCount++;
		return newBegin;
	}

	/**
	 * Cik reižu likums ir lietots?
	 * @return skaits, cik reižu likums ir lietots.
	 */
	@Override
	public int getUsageCount()
	{
		return usageCount;
	}

	/**
	 * Metode, kas ļauj dabūt likuma nosaukumu, kas ļautu šo likumu atšķirt no
	 * citiem.
	 * @return likuma vienkāršota reprezentācija, kas izmantojama diagnostikas
	 * izdrukās.
	 */
	@Override
	public String getStrReprezentation()
	{
		return String.format("%s \"%s\"",
				this.getClass().getSimpleName(), patternText);
	}
}
