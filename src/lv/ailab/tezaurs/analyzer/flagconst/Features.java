package lv.ailab.tezaurs.analyzer.flagconst;

import lv.ailab.tezaurs.utils.Tuple;

/**
 * Atslēgu un vērtību pārīši, ko lietot, lai likumus pierakstīt ir īsāk.
 * Created on 2015-10-09.
 * @author Lauma
 */
public class Features
{
	public static final Tuple<Keys, String> POS__NOUN = Tuple.of(Keys.POS, Values.NOUN.s);
	public static final Tuple<Keys, String> POS__REFL_NOUN = Tuple.of(Keys.POS, Values.REFL_NOUN.s);
	public static final Tuple<Keys, String> POS__VERB = Tuple.of(Keys.POS, Values.VERB.s);
	public static final Tuple<Keys, String> POS__ADJ = Tuple.of(Keys.POS, Values.ADJECTIVE.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE = Tuple.of(Keys.POS, Values.PARTICIPLE.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_OSS = Tuple.of(Keys.POS, Values.PARTICIPLE_OSS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_IS = Tuple.of(Keys.POS, Values.PARTICIPLE_IS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_TS = Tuple.of(Keys.POS, Values.PARTICIPLE_TS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_AMS = Tuple.of(Keys.POS, Values.PARTICIPLE_AMS.s);
	public static final Tuple<Keys, String> POS__PARTICIPLE_DAMS = Tuple.of(Keys.POS, Values.PARTICIPLE_DAMS.s);
	public static final Tuple<Keys, String> POS__PRONOUN = Tuple.of(Keys.POS, Values.PRONOUN.s);
	public static final Tuple<Keys, String> POS__NUMERAL = Tuple.of(Keys.POS, Values.NUMERAL.s);
	public static final Tuple<Keys, String> POS__FOREIGN = Tuple.of(Keys.POS, Values.FOREIGN.s);

	public static final Tuple<Keys, String> GENDER__FEM = Tuple.of(Keys.GENDER, Values.FEMININE.s);
	public static final Tuple<Keys, String> GENDER__MASC = Tuple.of(Keys.GENDER, Values.MASCULINE.s);

	public static final Tuple<Keys, String> USUALLY_USED__THIRD_PERS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.THIRD_PERSON.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_IS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_IS.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_TS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_TS.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_AMS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_AMS.s);
	public static final Tuple<Keys, String> USUALLY_USED__PARTICIPLE_DAMS = Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PARTICIPLE_DAMS.s);
	public static final Tuple<Keys, String> USED_ONLY__THIRD_PERS = Tuple.of(Keys.USED_ONLY_IN_FORM, Values.THIRD_PERSON.s);
	public static final Tuple<Keys, String> USED_ONLY__PLURAL = Tuple.of(Keys.USED_ONLY_IN_FORM, Values.PLURAL.s);

	public static final Tuple<Keys, String> USAGE_RESTR__HISTORICAL = Tuple.of(Keys.USAGE_RESTRICTIONS, Values.HISTORICAL.s);

	public static final Tuple<Keys, String> DOMAIN__HIST_PERSON = Tuple.of(Keys.DOMAIN, Values.HISTORICAL_PERSON.s);
	public static final Tuple<Keys, String> DOMAIN__HIST_PLACE = Tuple.of(Keys.DOMAIN, Values.HISTORICAL_PLACE.s);

	public static final Tuple<Keys, String> ENTRYWORD__PLURAL = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.PLURAL.s);
	public static final Tuple<Keys, String> ENTRYWORD__SINGULAR = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.SINGULAR.s);
	public static final Tuple<Keys, String> ENTRYWORD__FEM = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.FEMININE.s);
	public static final Tuple<Keys, String> ENTRYWORD__CHANGED_PARADIGM = Tuple.of(Keys.ENTRYWORD_WEARDNES, Values.CHANGED_PARADIGM.s);

	public static final Tuple<Keys, String> INFINITIVE_HOMOFORMS = Tuple.of(Keys.INFLECTION_WEARDNES, Values.INFINITIVE_HOMOFORMS.s);
	public static final Tuple<Keys, String> PARALLEL_FORMS = Tuple.of(Keys.INFLECTION_WEARDNES, Values.PARALLEL_FORMS.s);
	public static final Tuple<Keys, String> HAS_PRESENT_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.HAS_PRESENT_SOUNDCHANGE.s);
	public static final Tuple<Keys, String> NO_PRESENT_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.NO_PRESENT_SOUNDCHANGE.s);
	public static final Tuple<Keys, String> NO_SOUNDCHANGE = Tuple.of(Keys.INFLECTION_WEARDNES, Values.NO_SOUNDCHANGE.s);

	public static final Tuple<Keys, String> UNCLEAR_PARADIGM = Tuple.of(Keys.OTHER_FLAGS, Values.UNCLEAR_PARADIGM.s);

	public static final Tuple<Keys, String> CHANGED_PARADIGM = Tuple.of(Keys.OTHER_FLAGS, Values.CHANGED_PARADIGM.s);

	public static final Tuple<Keys, String> NON_INFLECTIVE = Tuple.of(Keys.OTHER_FLAGS, Values.NON_INFLECTIVE.s);

	public static final Tuple<Keys, String> PERSON_NAME = Tuple.of(Keys.OTHER_FLAGS, Values.PERSON_NAME.s);
	public static final Tuple<Keys, String> PLACE_NAME = Tuple.of(Keys.OTHER_FLAGS, Values.PLACE_NAME.s);

}
