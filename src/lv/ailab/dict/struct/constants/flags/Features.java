package lv.ailab.dict.struct.constants.flags;

import lv.ailab.dict.utils.Tuple;

/**
 * Atslēgu un vērtību pārīši, ko lietot, lai likumus pierakstīt ir īsāk.
 *
 * Izveidots 2016-09-19.
 * @author Lauma
 */
public class Features
{
	public static final Tuple<String, String> POS__NOUN = Tuple.of(Keys.POS, Values.NOUN);
	public static final Tuple<String, String> POS__ADJ = Tuple.of(Keys.POS, Values.ADJECTIVE);
	public static final Tuple<String, String> POS__NUMERAL = Tuple.of(Keys.POS, Values.NUMERAL);
	public static final Tuple<String, String> POS__PRONOUN = Tuple.of(Keys.POS, Values.PRONOUN);

	public static final Tuple<String, String> POS__VERB = Tuple.of(Keys.POS, Values.VERB);
	public static final Tuple<String, String> POS__ADV = Tuple.of(Keys.POS, Values.ADVERB);

	public static final Tuple<String, String> POS__ADPOSITION = Tuple.of(Keys.POS, Values.ADPOSITION);
	public static final Tuple<String, String> POS__PARTICLE = Tuple.of(Keys.POS, Values.PARTICLE);
	public static final Tuple<String, String> POS__CONJUNCTION = Tuple.of(Keys.POS, Values.CONJUNCTION);
	public static final Tuple<String, String> POS__INTERJECTION = Tuple.of(Keys.POS, Values.INTERJECTION);

	public static final Tuple<String, String> POS__GEN_ONLY = Tuple.of(Keys.POS, Values.GEN_ONLY);

	public static final Tuple<String, String> POS__ABBR = Tuple.of(Keys.POS, Values.ABBREVIATION);


	public static final Tuple<String, String> GENDER__FEM = Tuple.of(Keys.GENDER, Values.FEMININE);
	public static final Tuple<String, String> GENDER__MASC = Tuple.of(Keys.GENDER, Values.MASCULINE);

	public static final Tuple<String, String> NUMBER__SINGULAR = Tuple.of(Keys.NUMBER, Values.SINGULAR);
	public static final Tuple<String, String> NUMBER__PLURAL = Tuple.of(Keys.NUMBER, Values.PLURAL);

	public static final Tuple<String, String> CASE__ACUSATIVE = Tuple.of(Keys.CASE, Values.ACUSATIVE);
	public static final Tuple<String, String> CASE__DATIVE = Tuple.of(Keys.CASE, Values.DATIVE);
	public static final Tuple<String, String> CASE__GENITIVE = Tuple.of(Keys.CASE, Values.GENITIVE);
	public static final Tuple<String, String> CASE__LOCATIVE = Tuple.of(Keys.CASE, Values.LOCATIVE);
	public static final Tuple<String, String> CASE__NOMINATIVE = Tuple.of(Keys.CASE, Values.NOMINATIVE);

	public static final Tuple<String, String> PERSON__1 = Tuple.of(Keys.PERSON, Values.FIRST_PERSON);
	public static final Tuple<String, String> PERSON__2 = Tuple.of(Keys.PERSON, Values.SECOND_PERSON);
	public static final Tuple<String, String> PERSON__3 = Tuple.of(Keys.PERSON, Values.THIRD_PERSON);

	public static final Tuple<String, String> TENSE__FUTURE = Tuple.of(Keys.TENSE, Values.FUTURE);
	public static final Tuple<String, String> TENSE__PAST = Tuple.of(Keys.TENSE, Values.PAST);
	public static final Tuple<String, String> TENSE__PRESENT = Tuple.of(Keys.TENSE, Values.PRESENT);

	public static final Tuple<String, String> MOOD__CONDITIONAL = Tuple.of(Keys.MOOD, Values.CONDITIONAL);
	public static final Tuple<String, String> MOOD__DEBITIVE = Tuple.of(Keys.MOOD, Values.DEBITIVE);
	public static final Tuple<String, String> MOOD__IMPERATIVE = Tuple.of(Keys.MOOD, Values.IMPERATIVE);
	public static final Tuple<String, String> MOOD__INFINITIVE = Tuple.of(Keys.MOOD, Values.INFINITIVE);
	public static final Tuple<String, String> MOOD__INDICATIVE = Tuple.of(Keys.MOOD, Values.INDICATIVE);
	public static final Tuple<String, String> MOOD__PARTICIPLE = Tuple.of(Keys.MOOD, Values.PARTICIPLE);
	public static final Tuple<String, String> MOOD__RELATIVE = Tuple.of(Keys.MOOD, Values.RELATIVE);

	public static final Tuple<String, String> VOICE__ACTIVE = Tuple.of(Keys.VOICE, Values.ACTIVE_VOICE);
	public static final Tuple<String, String> VOICE__PASIVE = Tuple.of(Keys.VOICE, Values.PASSIVE_VOICE);

	public static final Tuple<String, String> DEFINITNESS__DEF = Tuple.of(Keys.DEFINITENESS, Values.DEFINITE_ENDING);
	public static final Tuple<String, String> DEFINITNESS__INDEF = Tuple.of(Keys.DEFINITENESS, Values.INDEFINITE_ENDING);

	public static final Tuple<String, String> DEGREE__POS = Tuple.of(Keys.DEGREE, Values.POSITIVE_DEGREE);
	public static final Tuple<String, String> DEGREE__COMP = Tuple.of(Keys.DEGREE, Values.COMPARATIVE_DEGREE);
	public static final Tuple<String, String> DEGREE__SUPER = Tuple.of(Keys.DEGREE, Values.SUPERLATIVE_DEGREE);

	public static final Tuple<String, String> SENT__DECLAR = Tuple.of(Keys.SENTENCE_TYPE, Values.DECLARATIVE_SENT);
	public static final Tuple<String, String> SENT__ENCOURAGE = Tuple.of(Keys.SENTENCE_TYPE, Values.ENCOURAGE_SENT);
	public static final Tuple<String, String> SENT__INTERJ = Tuple.of(Keys.SENTENCE_TYPE, Values.INTERJECTION_SENT);
	public static final Tuple<String, String> SENT__NEGATION = Tuple.of(Keys.SENTENCE_TYPE, Values.NEGATION_SENT);
	public static final Tuple<String, String> SENT__QUESTION = Tuple.of(Keys.SENTENCE_TYPE, Values.QUESTION_SENT);


	public static final Tuple<String, String> NON_INFLECTIVE = Tuple.of(Keys.OTHER_FLAGS, Values.NON_INFLECTIVE);

	public static final Tuple<String, String> MULTI_INFLECTIVE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.MULTI_INFLECTIVE);

	public static final Tuple<String, String> STEMS_ARE_ORDERED = Tuple.of(Keys.OTHER_FLAGS, Values.STEMS_ARE_ORDERED);

	public static final Tuple<String, String> PREFIX__NONE = Tuple.of(Keys.PREFIXNESS, Values.NO_PREFIX);
	public static final Tuple<String, String> PREFIX__HAS = Tuple.of(Keys.PREFIXNESS, Values.HAS_PREFIX);

}
