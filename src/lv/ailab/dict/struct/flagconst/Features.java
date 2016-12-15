package lv.ailab.dict.struct.flagconst;

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

	public static final Tuple<String, String> POS__PREPOSITION = Tuple.of(Keys.POS, Values.PREPOSITION);
	public static final Tuple<String, String> POS__PARTICLE = Tuple.of(Keys.POS, Values.PARTICLE);
	public static final Tuple<String, String> POS__CONJUNCTION = Tuple.of(Keys.POS, Values.CONJUNCTION);

	public static final Tuple<String, String> POS__GEN_ONLY = Tuple.of(Keys.POS, Values.GEN_ONLY);

	public static final Tuple<String, String> POS__ABBR = Tuple.of(Keys.POS, Values.ABBREVIATION);


	public static final Tuple<String, String> GENDER__FEM = Tuple.of(Keys.GENDER, Values.FEMININE);
	public static final Tuple<String, String> GENDER__MASC = Tuple.of(Keys.GENDER, Values.MASCULINE);


	public static final Tuple<String, String> NON_INFLECTIVE = Tuple.of(Keys.OTHER_FLAGS, Values.NON_INFLECTIVE);

	public static final Tuple<String, String> STEMS_ARE_ORDERED = Tuple.of(Keys.OTHER_FLAGS, Values.STEMS_ARE_ORDERED);

	public static final Tuple<String, String> NO_PREFIX = Tuple.of(Keys.OTHER_FLAGS, Values.NO_PREFIX);

}
