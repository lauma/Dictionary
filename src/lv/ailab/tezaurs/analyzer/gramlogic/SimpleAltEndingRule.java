package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.struct.Flags;
import lv.ailab.tezaurs.analyzer.struct.Lemma;
import lv.ailab.tezaurs.utils.MappingSet;
import lv.ailab.tezaurs.utils.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Likumi, kas tikai ar galotnēm norāda, ka jāizveido alternatīvā lemma.
 * Vienākāršais likums - gramatikas šablonam atbilst viens galotnes šablons,
 * viena pamata paradigma ar vienu alternatīvo lemmu, kurai atkal ir viena
 * paradigma.
 * Izveidots 2015-10-26.
 *
 * @author Lauma
 */
public class SimpleAltEndingRule implements AltLemmaRule
{
	/**
	 * Nokompilēts šablons, kuram jāatbilst grmatikai, lai šis likums būtu
	 * piemērojams.
	 */
	protected final Pattern pattern;
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
	 * Teksta virkne kuru izmantos kā izskaņu, veidojot papildus lemmu.
	 */
	protected final String altLemmaEnding;

	/**
	 * Paradigmas ID, ko lieto, ja likums ir piemērojams (gan gramatikas teksts,
	 * gan lemma atbilst attiecīgajiem šabloniem).
	 */
	protected final int paradigmId;
	/**
	 * Paradigmas ID, ko lieto papildus izveidotajai lemmai.
	 */
	protected final int altParadigmId;
	/**
	 * Šos karodziņus uzstāda pamata karodziņu savācējam, ja gan gramatikas
	 * teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<Keys,String>> positiveFlags;
	/**
	 * Šos karodziņus uzstāda papildu pamatformai, nevis pamatvārdam, ja gan
	 * gramatikas teksts, gan lemma atbilst attiecīgajiem šabloniem.
	 */
	protected final Set<Tuple<Keys,String>> altLemmaFlags;


	public SimpleAltEndingRule(String patternText, String lemmaRestrict,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigmId,
			int altParadigmId, Set<Tuple<Keys, String>> positiveFlags,
			Set<Tuple<Keys, String>> altLemmaFlags)
	{
		pattern = Pattern.compile("(\\Q" + patternText + "\\E)([;,.].*)?");
		this.lemmaRestrict = Pattern.compile(lemmaRestrict);
		this.altLemmaEnding = altLemmaEnding;
		this.lemmaEndingCutLength = lemmaEndingCutLength;
		this.paradigmId = paradigmId;
		this.altParadigmId = altParadigmId;
		this.positiveFlags = positiveFlags == null? null : Collections.unmodifiableSet(positiveFlags);
		this.altLemmaFlags = altLemmaFlags == null? null : Collections.unmodifiableSet(altLemmaFlags);
	}

	public static SimpleAltEndingRule of(String patternText, String lemmaRestrict,
			String altLemmaEnding, int lemmaEndingCutLength, int paradigmId,
			int altParadigmId, Tuple<Keys, String>[] positiveFlags,
			Tuple<Keys, String>[] altLemmaFlags)
	{
		return new SimpleAltEndingRule(patternText, lemmaRestrict, altLemmaEnding,
				lemmaEndingCutLength, paradigmId, altParadigmId,
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				altLemmaFlags == null ? null : new HashSet<>(Arrays.asList(altLemmaFlags)));
	}

	/**
	 * Speciālgadījums lietvārdiem, kam pamatforma ir 1. deklinācijā un
	 * papildforma - 5. Pirmās deklinācijas paradigmu (1 vai 2) nosaka
	 * automātiski pēc lemmaEnding pēdējā simbola.
	 */
	public static SimpleAltEndingRule mascFirstDeclToFemFifthDecl(
			String patternText, String lemmaEnding, String altLemmaEnding)
	{
		int paradigm = 1;
		if (lemmaEnding.endsWith("s")) paradigm = 1;
		else if (lemmaEnding.endsWith("š")) paradigm = 2;
		else System.err.printf(
				"Neizdodas pēc galotnes \"%s\" noteikt paradigmu likumam \"%s\"\n",
				lemmaEnding, patternText);
		return SimpleAltEndingRule
				.of(patternText, ".*" + lemmaEnding, altLemmaEnding,
						lemmaEnding.length(), paradigm, 9,
						new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN},
						new Tuple[]{Features.ENTRYWORD__FEM, Features.CHANGED_PARADIGM});
	}

	/**
	 * Speciālgadījums lietvārdiem, kam pamatforma ir 2. deklinācijā
	 * (3. paradigma) un papildforma - 5.
	 */
	public static SimpleAltEndingRule mascSeconDeclToFemFifthDecl(
			String patternText, String lemmaEnding, String altLemmaEnding)
	{
		return SimpleAltEndingRule
				.of(patternText, ".*" + lemmaEnding, altLemmaEnding,
						lemmaEnding.length(), 3, 9,
						new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN},
						new Tuple[]{Features.ENTRYWORD__FEM, Features.CHANGED_PARADIGM});
	}

	/**
	 * Speciālgadījums divdabjiem no is/usi grupas. Lemmas ierobešojums ir
	 * regulārā izteiksme, kas netiek nekā papildināta.
	 */
	public static SimpleAltEndingRule participleIsMascToFem(
			String patternText, String lemmaRestrict, String altLemmaEnding,
			int lemmaEndingCutLength)
	{
		return SimpleAltEndingRule
				.of(patternText, lemmaRestrict, altLemmaEnding,
						lemmaEndingCutLength, 0, 0,
						new Tuple[]{Features.POS__PARTICIPLE, Features.POS__PARTICIPLE_IS, Features.GENDER__MASC},
						new Tuple[]{Features.ENTRYWORD__FEM});
	}

	/**
	 * Speciālgadījums divdabjiem no is/usi grupas. Lemmas ierobešojums ir
	 * vārda izskaņa, kas par regulāro izteiksmi tiek pārveidota automātiski.
	 */
	public static SimpleAltEndingRule participleIsMascToFem(
			String patternText, String lemmaEnding, String altLemmaEnding)
	{
		return SimpleAltEndingRule
				.participleIsMascToFem(patternText, ".*" + lemmaEnding,
						altLemmaEnding, lemmaEnding.length());

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
			if (lemmaRestrict.matcher(lemma).matches()
					&& lemma.length() >= lemmaEndingCutLength)
			{
				String lemmaStub = lemma.substring(0, lemma.length() - lemmaEndingCutLength);
				Lemma altLemma = new Lemma(lemmaStub + altLemmaEnding);
				Flags altParams = new Flags();
				if (altLemmaFlags != null)
					for (Tuple<Keys, String> t : altLemmaFlags) altParams.add(t);
				altLemmasCollector.put(altParadigmId, new Tuple<>(altLemma, altParams));

				paradigmCollector.add(paradigmId);
				if (positiveFlags != null)
					for (Tuple<Keys, String> t : positiveFlags)
						flagCollector.add(t);
			}
			else
			{
				System.err.printf("Neizdodas \"%s\" ielikt paradigmā %s un %s\n",
						lemma, paradigmId, altParadigmId);
				newBegin = 0;
			}
		}
		return newBegin;
	}
}
