package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.FirstConjRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.FirstConjStems;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.utils.Tuple;

/**
 * 1. konjugācijas ērtummetodes.
 *
 * Izveidots 2016-10-12.
 * @author Lauma
 */
public class FirstConj
{
	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 */
	public static FirstConjRule direct(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 15,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām un nenoteiksmes homoformām, tikai 3. personas formas
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 */
	public static FirstConjRule direct3Pers(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām, bet ar  nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return RegularVerbRule ar paradigmu 15 un karodziņiem
	 */
	public static FirstConjRule directHomof(
			String patternBegin, String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam bez paralēlajām
	 * formām, bet ar nenoteiksmes homoformām, tikai 3 personas formas
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                  	bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return ThirdPersVerbRule ar paradigmu 15 un karodziņiem
	 */
	public static FirstConjRule direct3PersHomof(
			String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas tiesajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņiem
	 * TODO add IDs from lexicon
	 */
	public static FirstConjRule direct3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternEnd, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return FirstConjRule.of(patternEnd, lemmaEnd, 15, posFlags,
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 15 un karodziņiem, ka tas ir darbības
	 * 		   vārds ar paralēlajām formām
	 * TODO extract stems
	 */
	public static FirstConjRule directAllPersParallel(
			String patternText, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternText, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternText))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return FirstConjRule.of(patternText, null, lemmaEnd, 15, posFlags,
				null, stems);

	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 */
	public static FirstConjRule refl(String patternBegin, String patternEnd,
			String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnds		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 */
	public static FirstConjRule reflMultiLemma(String patternBegin, String patternEnd,
			String[] lemmaEnds)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnds);
		String lemmaEnd = "(" + String.join("|",lemmaEnds) + ")";
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdamm bez
	 * paralēlajām formām un nenoteiksmes homoformām, tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņu par locīšanu
	 * TODO add IDs from lexicon
	 */
	public static FirstConjRule refl3Pers(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, bet ar nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 */
	public static FirstConjRule reflHomof(
			String patternBegin, String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām, bet ar nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                  	bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņiem
	 */
	public static FirstConjRule refl3PersHomof(
			String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return FirstConjRule.of(patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return ThirdPersVerbRule ar paradigmu 18 un karodziņiem
	 * TODO add IDs from lexicon
	 */
	public static FirstConjRule refl3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternEnd, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return FirstConjRule.of(patternEnd, lemmaEnd, 18, posFlags,
				null, stems);
	}

	/**
	 * Izveido likumu 1. konjugācijas atgriezeniskajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return likums ar paradigmu 18 un karodziņiem, ka tas ir darbības
	 * 		   vārds ar paralēlajām formām
	 */
	public static FirstConjRule reflAllPersParallel(
			String patternText, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternText, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternText))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return FirstConjRule.of(patternText, null, lemmaEnd, 18, posFlags,
				null, stems);
	}
}
