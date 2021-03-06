package lv.ailab.dict.tezaurs.analyzer.gramlogic;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.struct.constants.structrestrs.Type;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.tezaurs.struct.constants.structrestrs.TFrequency;
import lv.ailab.dict.utils.Tuple;

import java.util.*;

/**
 * Likums šabloniem, kas sākas ar "parasti dsk.," vai  "tikai dsk.,". Tiek
 * pārbaudīts, vai likums nesatur " arī vsk. 3. pers. " un vajadzības gadījumā
 * tiek pielikti nepieciešamie karodziņi.
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public class PluralVerbRule implements EndingRule
{
	/**
	 * Neeskepota teksta virkne, ar kuru grmatikai jāsākas, lai šis likums būtu
	 * piemērojams.
	 */
	protected final String patternText;

	/**
	 * Likums šablonam, kas sākas ar "tikai dsk.,".
	 */
	protected BaseRule pluralOnly;

	/**
	 * Likums šablonam, kas sākas ar "parasti dsk.,".
	 */
	protected BaseRule pluralUsually;

	/**
	 * Celmus glabā atsevišķi, nevis jau kā gatavus karodziņus tāpēc, ka
	 * karodziņam jāsatur arī "priedēklis", bet šobrīd tas nav zināms.
	 * NULL 2. un 3. konjufgācijas verbiem.
	 */
	protected FirstConjStems stems;

	/**
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms		paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti 3. personā" pievieno
	 *                      automātiski)
	 */
	public PluralVerbRule(String patternText, String lemmaEnding, Set<Integer> paradigms,
			Set<Tuple<String,String>> positiveFlags, Set<Tuple<String,String>> alwaysFlags,
			FirstConjStems stems)
	{
		this.patternText = patternText;
		this.stems = stems;
		boolean singular3Pers = patternText.contains(" vai vsk. 3. pers. ");
		pluralUsually = BaseRule.simple(
				"parasti dsk., " + patternText, ".*" + lemmaEnding, paradigms,
				new HashSet<Tuple<String, String>>()
				{{
					add(TFeatures.POS__VERB);
					if (positiveFlags != null) addAll(positiveFlags);
				}},
				alwaysFlags,
				new HashSet<StructRestrs.One>()
				{{
					add(StructRestrs.One.of(Type.IN_FORM, TFrequency.USUALLY, TFeatures.NUMBER__PLURAL));
					if (singular3Pers)
						add(StructRestrs.One.of(Type.IN_FORM, TFrequency.USUALLY, new Tuple[]{TFeatures.NUMBER__SINGULAR, TFeatures.PERSON__3}));
				}});
		pluralOnly = BaseRule.simple(
				"tikai dsk., " + patternText, ".*" + lemmaEnding, paradigms,
				new HashSet<Tuple<String, String>>()
				{{
					add(TFeatures.POS__VERB);
					if (positiveFlags != null) addAll(positiveFlags);
				}},
				alwaysFlags,
				new HashSet<StructRestrs.One>()
				{{
					add(StructRestrs.One.of(Type.IN_FORM, TFrequency.ONLY, TFeatures.NUMBER__PLURAL));
					if (singular3Pers)
						add(StructRestrs.One.of(Type.IN_FORM, TFrequency.ONLY, new Tuple[] {TFeatures.NUMBER__SINGULAR, TFeatures.PERSON__3}));
				}});
	}

	/**
	 * Konstruktors, ja ir viena paradigma un celmi jau ir izdalīti.
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti daudzskaitlī" pievieno
	 *                      automātiski)
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public static PluralVerbRule of(String patternText, String lemmaEnding,
			int paradigmId, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags, FirstConjStems stems)
	{
		return new PluralVerbRule(patternText, lemmaEnding,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				stems);
	}

	/**
	 * 	Konstruktors, ja ir vairākas paradigmas un celmi jau ir izdalīti.
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms	paradigmas, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti daudzskaitlī" pievieno
	 *                      automātiski)
	 * @param stems			visi nepieciešamie celmi, jau izgūti, vai null.
	 */
	public static PluralVerbRule of(String patternText, String lemmaEnding,
			Integer[] paradigms, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags, FirstConjStems stems)
	{
		return new PluralVerbRule(patternText, lemmaEnding,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				stems);
	}

	/**
	 * Konstruktors, ja ir viena paradigma un celmi nav jāuzdod (parasti 2.,
	 * 3. konj.).
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigmId	paradigma, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti daudzskaitlī" pievieno
	 *                      automātiski)
	 */
	public static PluralVerbRule of(String patternText, String lemmaEnding,
			int paradigmId, Tuple<String,String>[] positiveFlags,
			Tuple<String,String>[] alwaysFlags)
	{
		return new PluralVerbRule(patternText, lemmaEnding,
				new HashSet<Integer>() {{add(paradigmId);}},
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null);
	}

	/**
	 * 	Konstruktors, ja ir vairākas paradigmas un celmi nav jāuzdod, (parasti
	 * 	2., 3. konj.).
	 * @param patternText   gramatikas šablons bez "parasti/tikai dsk.,".
	 * @param lemmaEnding   nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @param paradigms	paradigmas, ko lietot, ja konstatēta atbilstība šim
	 *                      likumam
	 * @param positiveFlags	karodziņi, ko uzstādīt, ja ir gan atbilstība likuma
	 *                      šablonam, gan lemmas nosacījumiem ("Darbības vārds"
	 *                      pievieno automātiski)
	 * @param alwaysFlags	karodziņi, ko uzstādīt, ja ir konstatēta atbilstība
	 *                      likuma šablonam ("Parasti daudzskaitlī" pievieno
	 *                      automātiski)
	 */
	public static PluralVerbRule of(String patternText, String lemmaEnding,
			Integer[] paradigms, Tuple<String,String>[] positiveFlags, Tuple<String,String>[] alwaysFlags)
	{
		return new PluralVerbRule(patternText, lemmaEnding,
				paradigms == null ? null : new HashSet<>(Arrays.asList(paradigms)),
				positiveFlags == null ? null : new HashSet<>(Arrays.asList(positiveFlags)),
				alwaysFlags == null ? null : new HashSet<>(Arrays.asList(alwaysFlags)),
				null);
	}

	/**
	 * Piemērot likumu bez papildus maģijas.
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 *                          ja vismaz gramatika atbilst šim likumam
	 * @return  jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int applyDirect(
			String gramText, String lemma, Set<Integer> paradigmCollector,
			Flags flagCollector, StructRestrs restrCollector)
	{
		int newBegin = pluralUsually.applyDirect(
				gramText, lemma, paradigmCollector, flagCollector, restrCollector);
		if (newBegin == -1)
			newBegin = pluralOnly.applyDirect(
					gramText, lemma, paradigmCollector, flagCollector, restrCollector);
		if (newBegin != -1 && stems != null)
			stems.addStemFlags(lemma, flagCollector);
		return newBegin;
	}

	/**
	 * Piemērot likumu tā, ka patternText defises ir neobligātas.
	 * @param gramText          apstrādājamā gramatika
	 * @param lemma             hederim, kurā atrodas gramatika, atbilstošā
	 *                          lemma
	 * @param paradigmCollector kolekcija, kurā pielikt paradigmu gadījumā, ja
	 *                          gramatika un lemma atbilst šim likumam
	 * @param flagCollector     kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                          vismaz gramatika atbilst šim likumam
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 *                          ja vismaz gramatika atbilst šim likumam
	 * @return  jaunā sākumpozīcija (vieta, kur sākas neatpazītā gramatikas
	 *          daļa) gramatikas tekstam, ja ir atbilsme šim likumam, -1 citādi.
	 */
	@Override
	public int applyOptHyphens(
			String gramText, String lemma, Set<Integer> paradigmCollector,
			Flags flagCollector, StructRestrs restrCollector)
	{
		int newBegin = pluralUsually.applyOptHyphens(
				gramText, lemma, paradigmCollector, flagCollector, restrCollector);
		if (newBegin == -1)
			newBegin = pluralOnly.applyOptHyphens(
					gramText, lemma, paradigmCollector, flagCollector, restrCollector);
		if (newBegin != -1 && stems != null)
			stems.addStemFlags(lemma, flagCollector);
		return newBegin;
	}

	/**
	 * Cik reižu likums ir lietots?
	 * @return skaits, cik reižu likums ir lietots.
	 */
	@Override
	public int getUsageCount()
	{
		int res = 0;
		if (pluralOnly != null) res = res + pluralOnly.getUsageCount();
		if (pluralUsually != null) res = res + pluralUsually.getUsageCount();
		return res;
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
