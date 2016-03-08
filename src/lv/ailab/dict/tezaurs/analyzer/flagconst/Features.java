package lv.ailab.dict.tezaurs.analyzer.flagconst;

import lv.ailab.dict.utils.Tuple;

/**
 * Atslēgu un vērtību pārīši, ko lietot, lai likumus pierakstīt ir īsāk.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
 *
 * Izveidots 2015-10-09.
 * @author Lauma
 */
public class Features
{
	public static final Tuple<Keys, String> POS__NOUN = Tuple.of(Keys.POS, Values.NOUN.s);
	public static final Tuple<Keys, String> POS__REFL_NOUN = Tuple.of(Keys.POS, Values.REFLEXIVE_NOUN.s);
	public static final Tuple<Keys, String> POS__GEN_ONLY = Tuple.of(Keys.POS, Values.GEN_ONLY.s);
	public static final Tuple<Keys, String> POS__VERB = Tuple.of(Keys.POS, Values.VERB.s);
	public static final Tuple<Keys, String> POS__ADJ = Tuple.of(Keys.POS, Values.ADJECTIVE.s);

	public static final Tuple<Keys, String> POS__PARTICIPLE = Tuple.of(Keys.POS, Values.PARTICIPLE.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_OSS = Tuple.of(Keys.POS, Values.PARTICIPLE_OSS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_OT = Tuple.of(Keys.POS, Values.PARTICIPLE_OT.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_IS = Tuple.of(Keys.POS, Values.PARTICIPLE_IS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_TS = Tuple.of(Keys.POS, Values.PARTICIPLE_TS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_AMS = Tuple.of(Keys.POS, Values.PARTICIPLE_AMS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_DAMS = Tuple.of(Keys.POS, Values.PARTICIPLE_DAMS.s);

	public static final Tuple<Keys, String> POS__PRONOUN = Tuple.of(Keys.POS, Values.PRONOUN.s);
	public static final Tuple<Keys, String> POS__PERS_PRONOUN = Tuple.of(Keys.POS, Values.PERSONAL_PRONOUN.s);
	public static final Tuple<Keys, String> POS__POSS_PRONOUN = Tuple.of(Keys.POS, Values.POSSESIVE_PRONOUN.s);
	public static final Tuple<Keys, String> POS__DEM_PRONOUN = Tuple.of(Keys.POS, Values.DEMONSTRATIVE_PRONOUN.s);
	public static final Tuple<Keys, String> POS__DEF_PRONOUN = Tuple.of(Keys.POS, Values.DEFINITE_PRONOUN.s);
	public static final Tuple<Keys, String> POS__INDEF_PRONOUN = Tuple.of(Keys.POS, Values.INDEFINITE_PRONOUN.s);
	public static final Tuple<Keys, String> POS__REFL_PRONOUN = Tuple.of(Keys.POS, Values.REFLEXIVE_PRONOUN.s);
	public static final Tuple<Keys, String> POS__GEN_PRONOUN = Tuple.of(Keys.POS, Values.GENRERIC_PRONOUN.s);
	public static final Tuple<Keys, String> POS__INTERROG_PRONOUN = Tuple.of(Keys.POS, Values.INTERROGATIVE_PRONOUN.s);
	public static final Tuple<Keys, String> POS__NEG_PRONOUN = Tuple.of(Keys.POS, Values.NEGATIVE_PRONOUN.s);

	public static final Tuple<Keys, String> POS__NUMERAL = Tuple.of(Keys.POS, Values.NUMERAL.s);
	public static final Tuple<Keys, String> POS__CARD_NUMERAL = Tuple.of(Keys.POS, Values.CARDINAL_NUMERAL.s);
	public static final Tuple<Keys, String> POS__ORD_NUMERAL = Tuple.of(Keys.POS, Values.ORDINAL_NUMERAL.s);
	public static final Tuple<Keys, String> POS__FRACT_NUMERAL = Tuple.of(Keys.POS, Values.FRACTIONAL_NUMERAL.s);
	public static final Tuple<Keys, String> POS__PARTICLE = Tuple.of(Keys.POS, Values.PARTICLE.s);
	public static final Tuple<Keys, String> POS__PREPOSITION = Tuple.of(Keys.POS, Values.PREPOSITION.s);
	public static final Tuple<Keys, String> POS__ABBR = Tuple.of(Keys.POS, Values.ABBREVIATION.s);
	public static final Tuple<Keys, String> POS__FOREIGN = Tuple.of(Keys.POS, Values.FOREIGN.s);
	public static final Tuple<Keys, String> POS__PIECE = Tuple.of(Keys.POS, Values.PIECE_OF_WORD.s);
	public static final Tuple<Keys, String> POS__PREFIX = Tuple.of(Keys.POS, Values.PREFIX.s);
	public static final Tuple<Keys, String> POS__COMPOUND_PIECE = Tuple.of(Keys.POS, Values.COMPOUND_PIECE.s);
	public static final Tuple<Keys, String> POS__COMPOUND_FIRST = Tuple.of(Keys.POS, Values.COMPOUND_FIRST_PIECE.s);
	public static final Tuple<Keys, String> POS__COMPOUND_LAST = Tuple.of(Keys.POS, Values.COMPOUND_LAST_PIECE.s);

	public static final Tuple<Keys, String> CONTAMINATION__NOUN = Tuple.of(Keys.CONTAMINATION, Values.NOUN.s);
	public static final Tuple<Keys, String> CONTAMINATION__CARD_NUM = Tuple.of(Keys.CONTAMINATION, Values.CARDINAL_NUMERAL.s);

	public static final Tuple<Keys, String> GENDER__FEM = Tuple.of(Keys.GENDER, Values.FEMININE.s);
	public static final Tuple<Keys, String> GENDER__MASC = Tuple.of(Keys.GENDER, Values.MASCULINE.s);

	public static final Tuple<Keys, String> DEFINITE_ENDING = Tuple.of(Keys.OTHER_FLAGS, Values.DEFINITE_ENDING.s);

	public static final Tuple<Keys, String> USUALLY_USED__PLURAL = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PLURAL.s);
	public static final Tuple<Keys, String> USUALLY_USED__THIRD_PERS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.THIRD_PERSON.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_IS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_IS.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_TS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_TS.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_AMS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_AMS.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_DAMS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_DAMS.s);
	public static final Tuple<Keys, String> USED_ONLY__THIRD_PERS = Tuple.of(Keys.USED_ONLY_IN_FORM, Values.THIRD_PERSON.s);
	public static final Tuple<Keys, String> USED_ONLY__PLURAL = Tuple.of(Keys.USED_ONLY_IN_FORM, Values.PLURAL.s);
	public static final Tuple<Keys, String> USED_ONLY__SINGULAR = Tuple.of(Keys.USED_ONLY_IN_FORM, Values.SINGULAR.s);

	public static final Tuple<Keys, String> USAGE_RESTR__HISTORICAL = Tuple.of(Keys.USAGE_RESTRICTIONS, Values.HISTORICAL.s);
	public static final Tuple<Keys, String> USAGE_RESTR__DIALECTICISM = Tuple.of(Keys.USAGE_RESTRICTIONS, Values.DIALECTICISM.s);
	public static final Tuple<Keys, String> USAGE_RESTR__REGIONAL = Tuple.of(Keys.USAGE_RESTRICTIONS, Values.REGIONAL_TERM.s);

	public static final Tuple<Keys, String> DOMAIN__HIST_PERSON = Tuple.of(Keys.DOMAIN, Values.HISTORICAL_PERSON.s);
	public static final Tuple<Keys, String> DOMAIN__HIST_PLACE = Tuple.of(Keys.DOMAIN, Values.HISTORICAL_PLACE.s);

	public static final Tuple<Keys, String> ENTRYWORD__PLURAL = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.PLURAL.s);
	public static final Tuple<Keys, String> ENTRYWORD__SINGULAR = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.SINGULAR.s);
	public static final Tuple<Keys, String> ENTRYWORD__FEM = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.FEMININE.s);
	public static final Tuple<Keys, String> ENTRYWORD__CHANGED_PARADIGM = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.CHANGED_PARADIGM.s);

	public static final Tuple<Keys, String> INFINITIVE_HOMOFORMS = Tuple.of(Keys.INFLECTION_WEARDNES, Values.INFINITIVE_HOMOFORMS.s);
	public static final Tuple<Keys, String> PARALLEL_FORMS = Tuple.of(Keys.INFLECTION_WEARDNES, Values.PARALLEL_FORMS.s);
	public static final Tuple<Keys, String> FIRST_CONJ_PARALLELFORM = Tuple.of(Keys.INFLECTION_WEARDNES, Values.FIRST_CONJ_PARALLELFORM.s);
	public static final Tuple<Keys, String> HAS_PRESENT_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.HAS_PRESENT_SOUNDCHANGE.s);
	public static final Tuple<Keys, String> NO_PRESENT_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.NO_PRESENT_SOUNDCHANGE.s);
	public static final Tuple<Keys, String> OPT_PRESENT_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.OPT_PRESENT_SOUNDCHANGE.s);
	public static final Tuple<Keys, String> NO_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.NO_SOUNDCHANGE.s);
	public static final Tuple<Keys, String> OPT_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.OPT_SOUNDCHANGE.s);
	public static final Tuple<Keys, String> FROZEN = Tuple.of(Keys.INFLECTION_WEARDNES, Values.FROZEN_FORM.s);

	public static final Tuple<Keys, String> UNCLEAR_PARADIGM = Tuple.of(Keys.OTHER_FLAGS, Values.UNCLEAR_PARADIGM.s);
	public static final Tuple<Keys, String> UNCLEAR_POS = Tuple.of(Keys.OTHER_FLAGS, Values.UNCLEAR_POS.s);

	public static final Tuple<Keys, String> CHANGED_PARADIGM = Tuple.of(Keys.OTHER_FLAGS, Values.CHANGED_PARADIGM.s);

	public static final Tuple<Keys, String> NON_INFLECTIVE = Tuple.of(Keys.OTHER_FLAGS, Values.NON_INFLECTIVE.s);

	public static final Tuple<Keys, String> ORIGINAL_NEEDED = Tuple.of(Keys.OTHER_FLAGS, Values.ORIGINAL_NEEDED.s);

	public static final Tuple<Keys, String> PERSON_NAME = Tuple.of(Keys.OTHER_FLAGS, Values.PERSON_NAME.s);
	public static final Tuple<Keys, String> PLACE_NAME = Tuple.of(Keys.OTHER_FLAGS, Values.PLACE_NAME.s);

}
