package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.tezaurs.analyzer.struct.TLemma;
import lv.ailab.dict.utils.MappingSet;
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
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
 *
 * Izveidots 2015-10-26.
 * @author Lauma
 */
public class AltEndingRule implements AltLemmaRule
{
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
	protected final List<AltLemmaSubRule> lemmaLogic;

	/**
	 * Šo izdrukā, kad liekas, ka likums varētu būt nepilnīgs - gramatikas
	 * šablonam atbilstība tiek fiksēta, bet lemmu šabloniem - nē.
	 */
	protected final String errorMessage;

	public AltEndingRule(String patternText, List<AltLemmaSubRule> lemmaLogic)
	{
		if (lemmaLogic == null)
			throw new IllegalArgumentException (
					"Nav paredzēts, ka BaseRule tiek viedots vispār bez lemmu nosacījumiem!");
		this.pattern = Pattern.compile("(\\Q" + patternText + "\\E)([;,.].*)?");
		this.lemmaLogic = Collections.unmodifiableList(lemmaLogic);

		if (lemmaLogic.size() == 1 && lemmaLogic.get(0).paradigms.size() == 1)
			errorMessage = "Neizdodas \"%s\" ielikt paradigmā " +
					lemmaLogic.get(0).paradigms.toArray()[0] +
					" ar papildus paradigmu " + lemmaLogic.get(0).altLemmaParadigm + "\n";
		else errorMessage = "Neizdodas \"%s\" ielikt kādā no paradigmām " +
				lemmaLogic.stream().map(t -> t.paradigms).flatMap(m -> m.stream())
						.distinct().sorted().map(i -> i.toString())
						.reduce((a, b) -> a + ", " + b).orElse("") +
				" ar papildus paradigmām " + lemmaLogic.stream()
						.map(t->t.altLemmaParadigm).distinct().sorted()
						.map(i -> i.toString()).reduce((a, b) -> a + ", " + b)
						.orElse("") + "\n";
	}

	/**
	 * Konstruktors īsumam - gadījumiem, kad likums apskata tikai vienu lemmas
	 * nosacījumu.
	 */
	public static AltEndingRule simple(String patternText, String lemmaRestrictions,
			Set<Integer> paradigms, Set<Tuple<Keys, String>> positiveFlags,
			int lemmaEndCutLength, String altLemmaEnd,
			int altLemmaParadigm, Set<Tuple<Keys, String>> altLemmaFlags)
	{
		return new AltEndingRule(patternText,
				new ArrayList<AltLemmaSubRule>(){{
					add( new AltLemmaSubRule(lemmaRestrictions, paradigms,
							positiveFlags, lemmaEndCutLength, altLemmaEnd,
							altLemmaParadigm, altLemmaFlags));}});
	}

	/**
	 * Konstruktors īsumam - gadījumiem, kad likums apskata tikai vienu lemmas
	 * nosacījumu ar vienu paradigmu.
	 */
	public static AltEndingRule simple(String patternText, String lemmaRestrictions,
			int paradigm, Set<Tuple<Keys, String>> positiveFlags,
			int lemmaEndCutLength, String altLemmaEnd,
			int altLemmaParadigm, Set<Tuple<Keys, String>> altLemmaFlags)
	{
		return new AltEndingRule(patternText,
				new ArrayList<AltLemmaSubRule>(){{
					add( new AltLemmaSubRule(lemmaRestrictions,
							new HashSet<Integer>(){{add(paradigm);}},
							positiveFlags, lemmaEndCutLength, altLemmaEnd,
							altLemmaParadigm, altLemmaFlags));}});
	}

	public static AltEndingRule of(String patternText, AltLemmaSubRule[] lemmaLogic)
	{
		return new AltEndingRule(patternText,
				lemmaLogic == null ? null : Arrays.asList(lemmaLogic));
	}

	public static AltEndingRule of(String patternText, String lemmaRestrictions,
			Integer[] paradigms, Tuple<Keys, String>[] positiveFlags,
			int lemmaEndCutLength, String altLemmaEnd,
			int altLemmaParadigm, Tuple<Keys, String>[] altLemmaFlags)
	{
		return simple(patternText, lemmaRestrictions,
				paradigms == null ? null : new HashSet<>(Arrays
						.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays
						.asList(positiveFlags)),
				lemmaEndCutLength, altLemmaEnd, altLemmaParadigm,
				altLemmaFlags == null ? null : new HashSet<>(Arrays
						.asList(altLemmaFlags)));
	}

	public static AltEndingRule of(String patternText, String lemmaRestrictions,
			int paradigm, Tuple<Keys, String>[] positiveFlags,
			int lemmaEndingCutLength, String altLemmaEnding,
			int altLemmaParadigm, Tuple<Keys, String>[] altLemmaFlags)
	{
		return simple(patternText, lemmaRestrictions, paradigm,
				positiveFlags == null ? null : new HashSet<>(Arrays
						.asList(positiveFlags)),
				lemmaEndingCutLength, altLemmaEnding, altLemmaParadigm,
				altLemmaFlags == null ? null : new HashSet<>(Arrays
						.asList(altLemmaFlags)));
	}



	/**
	 * Speciālgadījums lietvārdiem, kam pamatforma ir 1. deklinācijā un
	 * papildforma - 5. Pirmās deklinācijas paradigmu (1 vai 2) nosaka
	 * automātiski pēc lemmaEnd pēdējā simbola.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param altLemmaEnd	galotne, ko izmantos, veidojot alternatīvo lemmu
	 */
	public static AltEndingRule mascFirstDeclToFemFifthDecl(
			String patternText, String lemmaEnd, String altLemmaEnd)
	{
		int paradigm = 1;
		if (lemmaEnd.endsWith("s")) paradigm = 1;
		else if (lemmaEnd.endsWith("š")) paradigm = 2;
		else System.err.printf(
				"Neizdodas pēc galotnes \"%s\" noteikt paradigmu likumam \"%s\"\n",
				lemmaEnd, patternText);
		return AltEndingRule.of(patternText, ".*" + lemmaEnd, paradigm,
				new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN},
				lemmaEnd.length(), altLemmaEnd, 9,
				new Tuple[]{Features.ENTRYWORD__FEM, Features.CHANGED_PARADIGM});
	}

	/**
	 * Speciālgadījums lietvārdiem, kam pamatforma ir 2. deklinācijā
	 * (3. paradigma) un papildforma - 5.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param altLemmaEnd	galotne, ko izmantos, veidojot alternatīvo lemmu
	 */
	public static AltEndingRule mascSeconDeclToFemFifthDecl(
			String patternText, String lemmaEnd, String altLemmaEnd)
	{
		return AltEndingRule.of(patternText, ".*" + lemmaEnd, 3,
				new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN},
				lemmaEnd.length(), altLemmaEnd, 9,
				new Tuple[]{Features.ENTRYWORD__FEM, Features.CHANGED_PARADIGM});
	}

	/*
	 * Speciālgadījums divdabjiem no is/usi grupas. Lemmas ierobešojums ir
	 * regulārā izteiksme, kas netiek nekā papildināta.
	 */
/*	public static AltEndingRule participleIsMascToFem(
			String patternText, String lemmaRestrict, String altLemmaEnding,
			int lemmaEndingCutLength)
	{
		return AltEndingRule.of(patternText, lemmaRestrict, 0,
				new Tuple[]{Features.POS__PARTICIPLE, Features.POS__PARTICIPLE_IS, Features.GENDER__MASC},
				lemmaEndingCutLength, altLemmaEnding, 0,
				new Tuple[]{Features.ENTRYWORD__FEM});
	}*

	/*
	 * Speciālgadījums divdabjiem no is/usi grupas. Lemmas ierobešojums ir
	 * vārda izskaņa, kas par regulāro izteiksmi tiek pārveidota automātiski.
	 */
/*	public static AltEndingRule participleIsMascToFem(
			String patternText, String lemmaEnding, String altLemmaEnding)
	{
		return AltEndingRule
				.participleIsMascToFem(patternText, ".*" + lemmaEnding,
						altLemmaEnding, lemmaEnding.length());

	}*/

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
	 * @param altLemmasCollector kolekcija, kurā ielikt izveidotās papildus
	 *                           formas un tām raksturīgos karodziņus.
	 * @return jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 * daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int apply(String gramText, String lemma,
			HashSet<Integer> paradigmCollector, Flags flagCollector,
			MappingSet<Integer, Tuple<Lemma, Flags>> altLemmasCollector)
	{
		int newBegin = -1;
		Matcher m = pattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			boolean matchedLemma = false;
			for (AltLemmaSubRule rule : lemmaLogic)
			{
				if (rule.lemmaRestrict.matcher(lemma).matches()
						&& lemma.length() >= rule.lemmaEndingCutLength)
				{
					String lemmaStub = lemma.substring(0, lemma.length() - rule.lemmaEndingCutLength);
					Lemma altLemma = new TLemma(lemmaStub + rule.altLemmaEnding);
					Flags altParams = new Flags();
					if (rule.altLemmaFlags != null)
						altParams.addAll(rule.altLemmaFlags);
					altLemmasCollector.put(rule.altLemmaParadigm, new Tuple<>(altLemma, altParams));

					paradigmCollector.addAll(rule.paradigms);
					if (rule.positiveFlags != null)
						flagCollector.addAll(rule.positiveFlags);

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
		return newBegin;
	}
}
