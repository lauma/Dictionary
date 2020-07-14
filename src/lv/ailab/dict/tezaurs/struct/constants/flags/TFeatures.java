package lv.ailab.dict.tezaurs.struct.constants.flags;

import lv.ailab.dict.struct.constants.flags.Features;
import lv.ailab.dict.utils.Tuple;

/**
 * Atslēgu un vērtību pārīši, ko lietot, lai likumus pierakstīt ir īsāk.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * Izveidots 2015-10-09.
 * @author Lauma
 */
public class TFeatures extends Features
{
	public static final Tuple<String, String> POS__POSTPOSITION = Tuple.of(TKeys.POS, TValues.POSTPOSITION);

	public static final Tuple<String, String> POS__REFL_NOUN = Tuple.of(TKeys.POS, TValues.REFLEXIVE_NOUN);
	public static final Tuple<String, String> POS__IRREG_VERB = Tuple.of(TKeys.POS, TValues.IRREGULAR_VERB);
	public static final Tuple<String, String> POS__DIRECT_VERB = Tuple.of(TKeys.POS, TValues.DIRECT_VERB);
	public static final Tuple<String, String> POS__NEG_VERB = Tuple.of(TKeys.POS, TValues.NEGATIVE_VERB);
	public static final Tuple<String, String> POS__REFL_VERB = Tuple.of(TKeys.POS, TValues.REFLEXIVE_VERB);

	public static final Tuple<String, String> POS__PERS_PRONOUN = Tuple.of(TKeys.POS, TValues.PERSONAL_PRONOUN);
	public static final Tuple<String, String> POS__POSS_PRONOUN = Tuple.of(TKeys.POS, TValues.POSSESIVE_PRONOUN);
	public static final Tuple<String, String> POS__DEM_PRONOUN = Tuple.of(TKeys.POS, TValues.DEMONSTRATIVE_PRONOUN);
	public static final Tuple<String, String> POS__DEF_PRONOUN = Tuple.of(TKeys.POS, TValues.DEFINITE_PRONOUN);
	public static final Tuple<String, String> POS__INDEF_PRONOUN = Tuple.of(TKeys.POS, TValues.INDEFINITE_PRONOUN);
	public static final Tuple<String, String> POS__REFL_PRONOUN = Tuple.of(TKeys.POS, TValues.REFLEXIVE_PRONOUN);
	public static final Tuple<String, String> POS__REL_PRONOUN = Tuple.of(TKeys.POS, TValues.RELATIVE_PRONOUN);
	public static final Tuple<String, String> POS__GEN_PRONOUN = Tuple.of(TKeys.POS, TValues.GENRERIC_PRONOUN);
	public static final Tuple<String, String> POS__INTERROG_PRONOUN = Tuple.of(TKeys.POS, TValues.INTERROGATIVE_PRONOUN);
	public static final Tuple<String, String> POS__NEG_PRONOUN = Tuple.of(TKeys.POS, TValues.NEGATIVE_PRONOUN);

	public static final Tuple<String, String> POS__CARD_NUMERAL = Tuple.of(TKeys.POS, TValues.CARDINAL_NUMERAL);
	public static final Tuple<String, String> POS__ORD_NUMERAL = Tuple.of(TKeys.POS, TValues.ORDINAL_NUMERAL);
	public static final Tuple<String, String> POS__FRACT_NUMERAL = Tuple.of(TKeys.POS, TValues.FRACTIONAL_NUMERAL);
	public static final Tuple<String, String> POS__FOREIGN = Tuple.of(TKeys.POS, TValues.FOREIGN);
	public static final Tuple<String, String> POS__PIECE = Tuple.of(TKeys.POS, TValues.PIECE_OF_WORD);
	public static final Tuple<String, String> WPPOS__PREFIX = Tuple.of(TKeys.WORDPART_POS, TValues.PREFIX);
	public static final Tuple<String, String> WPPOS__COMPOUND_PIECE = Tuple.of(TKeys.WORDPART_POS, TValues.COMPOUND_PIECE);
	public static final Tuple<String, String> WPPOS__COMPOUND_FIRST = Tuple.of(TKeys.WORDPART_POS, TValues.COMPOUND_FIRST_PIECE);
	public static final Tuple<String, String> WPPOS__COMPOUND_LAST = Tuple.of(TKeys.WORDPART_POS, TValues.COMPOUND_LAST_PIECE);

	public static final Tuple<String, String> MOOD__PARTICIPLE_AMS = Tuple.of(TKeys.MOOD, TValues.PARTICIPLE_AMS);
	public static final Tuple<String, String> MOOD__PARTICIPLE_IS = Tuple.of(TKeys.MOOD, TValues.PARTICIPLE_IS);
	public static final Tuple<String, String> MOOD__PARTICIPLE_OSS = Tuple.of(TKeys.MOOD, TValues.PARTICIPLE_OSS);
	public static final Tuple<String, String> MOOD__PARTICIPLE_TS = Tuple.of(TKeys.MOOD, TValues.PARTICIPLE_TS);
	public static final Tuple<String, String> MOOD__PARTICIPLE_DAMS = Tuple.of(TKeys.MOOD, TValues.PARTICIPLE_DAMS);
	public static final Tuple<String, String> MOOD__PARTICIPLE_OT = Tuple.of(TKeys.MOOD, TValues.PARTICIPLE_OT);

	public static final Tuple<String, String> CONVERSION__NOUN = Tuple.of(TKeys.POS_CONVERSION, TValues.NOUN);
	public static final Tuple<String, String> CONVERSION__ADJECTIVE = Tuple.of(TKeys.POS_CONVERSION, TValues.ADJECTIVE);
	public static final Tuple<String, String> CONVERSION__CARD_NUM = Tuple.of(TKeys.POS_CONVERSION, TValues.CARDINAL_NUMERAL);
	public static final Tuple<String, String> CONVERSION__ADVERB = Tuple.of(TKeys.POS_CONVERSION, TValues.ADVERB);

	public static final Tuple<String, String> CASE__NOPRON_INSTRUMENTAL = Tuple.of(TKeys.CASE, TValues.NOPRON_INSTRUMENTAL);
	public static final Tuple<String, String> CASE__INSTRUMENTAL = Tuple.of(TKeys.CASE, TValues.INSTRUMENTAL);
	public static final Tuple<String, String> CASE__VOCATIVE = Tuple.of(TKeys.CASE, TValues.VOCATIVE);

	public static final Tuple<String, String> NUMBER__DUAL = Tuple.of(TKeys.NUMBER, TValues.DUAL);

	public static final Tuple<String, String> TRANSITIVITY_TRANS = Tuple.of(TKeys.TRANSITIVITY, TValues.TRANSITIVE);
	public static final Tuple<String, String> TRANSITIVITY_INTRANS = Tuple.of(TKeys.TRANSITIVITY, TValues.INTRANSITIVE);
	public static final Tuple<String, String> TRANSITIVITY_MIXED = Tuple.of(TKeys.TRANSITIVITY, TValues.MIXED_TRANSITIVITY);

	public static final Tuple<String, String> NEGATIVE = Tuple.of(TKeys.OTHER_FLAGS, TValues.NEGATIVE);

	public static final Tuple<String, String> USAGE_RESTR__HISTORICAL = Tuple.of(TKeys.USAGE_RESTRICTIONS, TValues.HISTORICAL);
	public static final Tuple<String, String> USAGE_RESTR__DIALECTICISM = Tuple.of(TKeys.USAGE_RESTRICTIONS, TValues.DIALECTICISM);
	public static final Tuple<String, String> USAGE_RESTR__REGIONAL = Tuple.of(TKeys.USAGE_RESTRICTIONS, TValues.REGIONAL_TERM);

	public static final Tuple<String, String> DOMAIN__HIST_PERSON = Tuple.of(TKeys.DOMAIN, TValues.HISTORICAL_PERSON);
	public static final Tuple<String, String> DOMAIN__HIST_PLACE = Tuple.of(TKeys.DOMAIN, TValues.HISTORICAL_PLACE);

	public static final Tuple<String, String> ENTRYWORD__PLURAL = Tuple.of(TKeys.ENTRYWORD_WEARDNES, TValues.PLURAL);
	public static final Tuple<String, String> ENTRYWORD__SINGULAR = Tuple.of(TKeys.ENTRYWORD_WEARDNES, TValues.SINGULAR);
	public static final Tuple<String, String> ENTRYWORD__FEM = Tuple.of(TKeys.ENTRYWORD_WEARDNES, TValues.FEMININE);
	//public static final Tuple<String, String> ENTRYWORD__CHANGED_PARADIGM = Tuple.of(TKeys.ENTRYWORD_WEARDNES, TValues.CHANGED_PARADIGM);

	public static final Tuple<String, String> FIRST_LETTER_BIG = Tuple.of(TKeys.CAPITALIZATION, TValues.FIRST_LETTER_BIG);
	public static final Tuple<String, String> FIRST_LETTER_SMALL = Tuple.of(TKeys.CAPITALIZATION, TValues.FIRST_LETTER_SMALL);

	public static final Tuple<String, String> COGENDER = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.COGENDER);
	public static final Tuple<String, String> INFINITIVE_HOMOFORMS = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.INFINITIVE_HOMOFORMS);
	public static final Tuple<String, String> PARALLEL_FORMS = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.PARALLEL_FORMS);
	public static final Tuple<String, String> FIRST_CONJ_PARALLELFORM_ALL_PERS = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.FIRST_CONJ_PARALLELFORM_ALL_PERS);
	public static final Tuple<String, String> FIRST_CONJ_PARALLELFORM_3_PERS = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.FIRST_CONJ_PARALLELFORM_3_PERS);
	public static final Tuple<String, String> SECOND_THIRD_CONJ = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.SECOND_THIRD_CONJ);
	//public static final Tuple<String, String> HAS_PRESENT_SOUNDCHANGE = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.HAS_PRESENT_SOUNDCHANGE);
	//public static final Tuple<String, String> NO_PRESENT_SOUNDCHANGE = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.NO_PRESENT_SOUNDCHANGE);
	//public static final Tuple<String, String> OPT_PRESENT_SOUNDCHANGE = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.OPT_PRESENT_SOUNDCHANGE);
	//public static final Tuple<String, String> NO_SOUNDCHANGE = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.NO_SOUNDCHANGE);
	//public static final Tuple<String, String> OPT_SOUNDCHANGE = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.OPT_SOUNDCHANGE);
	public static final Tuple<String, String> FROZEN = Tuple.of(TKeys.INFLECTION_WEARDNES, TValues.FROZEN_FORM);

	public static final Tuple<String, String> UNCLEAR_PARADIGM = Tuple.of(TKeys.PROBLEMS, TValues.UNCLEAR_PARADIGM);
	public static final Tuple<String, String> UNCLEAR_POS = Tuple.of(TKeys.PROBLEMS, TValues.UNCLEAR_POS);
	public static final Tuple<String, String> ORIGINAL_NEEDED = Tuple.of(TKeys.PROBLEMS, TValues.ORIGINAL_NEEDED);

	public static final Tuple<String, String> CHANGED_PARADIGM = Tuple.of(TKeys.OTHER_FLAGS, TValues.CHANGED_PARADIGM);


	public static final Tuple<String, String> PERSON_NAME = Tuple.of(TKeys.OTHER_FLAGS, TValues.PERSON_NAME);
	public static final Tuple<String, String> PLACE_NAME = Tuple.of(TKeys.OTHER_FLAGS, TValues.PLACE_NAME);

	public static final Tuple<String, String> REPETITION_WITH_ONE_STEM = Tuple.of(TKeys.SMALL_SYNT_RESTR, TValues.REPETITION_WITH_ONE_STEM);

}
