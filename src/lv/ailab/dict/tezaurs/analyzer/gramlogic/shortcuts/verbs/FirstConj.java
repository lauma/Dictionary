package lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs;

import lv.ailab.dict.tezaurs.analyzer.gramdata.RulesAsFunctions;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.PluralVerbRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.VerbDoubleRule;
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
public final class FirstConj
{
	/**
	 * Izveido VerbDoubleRule 1. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @return VerbDoubleRule ar paradigmu 15
	 */
	public static VerbDoubleRule direct(
			String patternBegin, String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 15,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām un nenoteiksmes homoformām, tikai 3. personas formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 15 un tikai 3 personas formām
	 */
	public static VerbDoubleRule direct3Pers(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido PluralVerbRule 1. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return PluralVerbRule ar paradigmu 15
	 */
	public static PluralVerbRule directPlural(String patternText, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternText, lemmaEnd);
		return PluralVerbRule.of(patternText, lemmaEnd, 15,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām, bet ar  nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return VerbDoubleRule ar paradigmu 15 un nenoteiksmes homoformām
	 */
	public static VerbDoubleRule directHomof(
			String patternBegin, String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas tiešajam darbības vārdam bez
	 * paralēlajām formām, bet ar nenoteiksmes homoformām, tikai 3 personas
	 * formas.
	 * @param patternEnd		gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                  	bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return	VerbDoubleRule ar paradigmu 15 un nenoteiksmes homoformām un
	 * 			tikai 3. personas formām.
	 */
	public static VerbDoubleRule direct3PersHomof(
			String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas tiesajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return	VerbDoubleRule ar paradigmu 15 un paralēlformām un tikai
	 * 			3. personas formām.
	 */
	public static VerbDoubleRule direct3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternEnd, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 15, posFlags,
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas tiešajam darbības vārdam ar
	 * paralēlajām formām, bet bez nenoteiksmes homoformām.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return	VerbDoubleRule ar paradigmu 15 un paralēlformām un bez
	 * 			3. personas likuma.
	 */
	public static VerbDoubleRule directAllPersParallel(
			String patternText, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternText, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternText))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 15, posFlags,
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @return VerbDoubleRule ar paradigmu 18
	 */
	public static VerbDoubleRule refl(String patternBegin, String patternEnd,
			String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas atgriezeniskajam darbības vārdam bez
	 * paralēlajām formām un bez nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai, var
	 *                      būt null, ja tas ir likums tikai ar 3 personas
	 *                      formām.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnds		nepieciešamā nenoteiksmes izskaņa lai šo likumu
	 *                      varētu piemērot
	 * @return VerbDoubleRule ar paradigmu 18 ar vairākām nenoteiksmēm
	 */
	public static VerbDoubleRule reflMultiLemma(
			String patternBegin, String patternEnd, String[] lemmaEnds)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnds);
		String lemmaEnd = "(" + String.join("|",lemmaEnds) + ")";
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas atgriezeniskajam darbības vārdamm
	 * bez paralēlajām formām un nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return VerbDoubleRule ar paradigmu 18 un tikai 3 personas formām
	 */
	public static VerbDoubleRule refl3Pers(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido PluralVerbRule 1. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām.
	 * @param patternText	gramatikas daļa ar galotnēm, bez "parasti dsk.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return PluralVerbRule ar paradigmu 18
	 */
	public static PluralVerbRule reflPlural(String patternText, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternText, lemmaEnd);
		return PluralVerbRule.of(patternText, lemmaEnd, 18,
				new Tuple[] {Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, bet ar nenoteiksmes homoformām.
	 * @param patternBegin	gramatikas daļa ar galotnēm 1. un 2. personai
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return VerbDoubleRule ar paradigmu 18 un nenoteiksmes homoformām
	 */
	public static VerbDoubleRule reflHomof(
			String patternBegin, String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternBegin, patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas atgriezeniskajam darbības vārdam
	 * bez paralēlajām formām, bet ar nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                  	bez "parasti 3.pers.,"
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @param inflectAs		virkne, kas tiks lietota "Locīt kā" karodziņam -
	 *                      pamata vārda nenoteiksme + skaidrojums homoformu
	 *                      atšķiršanai
	 * @return	VerbDoubleRule ar paradigmu 18 un nenoteiksmes homoformām un
	 * 			tikai 3. personas formām.
	 */
	public static VerbDoubleRule refl3PersHomof(
			String patternEnd, String lemmaEnd, String inflectAs)
	{
		FirstConjStems stems = FirstConjStems.singlePP(patternEnd, lemmaEnd);
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, inflectAs), TFeatures.INFINITIVE_HOMOFORMS},
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas atgriezeniskajam darbības vārdam
	 * ar paralēlajām formām, bet bez nenoteiksmes homoformām, tikai 3. personas
	 * formas.
	 * @param patternEnd	gramatikas daļa ar galotnēm 3. personai un pagātnei,
	 *                      bez "parasti 3.pers.,"
	 * @param lemmaEnd	nepieciešamā nenoteiksmes izskaņa
	 * @return	VerbDoubleRule ar paradigmu 18 un paralēlformām un tikai
	 * 			3. personas formām.
	 */
	public static VerbDoubleRule refl3PersParallel(
			String patternEnd, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternEnd, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternEnd))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return VerbDoubleRule.of(patternEnd, lemmaEnd, 18, posFlags,
				null, stems);
	}

	/**
	 * Izveido VerbDoubleRule 1. konjugācijas atgriezeniskajam darbības vārdam
	 * ar paralēlajām formām, bet bez nenoteiksmes homoformām.
	 * @param patternText	teksts, ar kuru jāsākas gramatikai
	 * @param lemmaEnd		nepieciešamā nenoteiksmes izskaņa
	 * @return	VerbDoubleRule ar paradigmu 18 un paralēlformām un bez
	 * 			3. personas likuma.
	 */
	public static VerbDoubleRule reflAllPersParallel(
			String patternText, String lemmaEnd)
	{
		FirstConjStems stems = FirstConjStems.parallelPP(patternText, lemmaEnd);
		Tuple[] posFlags;
		if (RulesAsFunctions.containsFormsOnly(patternText))
			posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\"")};
		else posFlags = new Tuple[] {TFeatures.PARALLEL_FORMS, Tuple.of(TKeys.INFLECT_AS, "\"" + lemmaEnd + "\""), TFeatures.ORIGINAL_NEEDED};
		return VerbDoubleRule.of(patternText, null, lemmaEnd, 18, posFlags,
				null, stems);
	}
}
