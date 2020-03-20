package lv.ailab.dict.struct.constants.flags;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Vispārīgās (dažādām vārdnīcām izmantojamās) karodziņu vērtības.
 *
 * Izveidots 2016-09-19.
 * @author Lauma
 */
public class Values
{
	//=== Kategorija ==========================================================
	public final static String NOUN = "Lietvārds";
	public final static String ADJECTIVE = "Īpašības vārds";
	public final static String NUMERAL = "Skaitļa vārds";
	public final static String PRONOUN = "Vietniekvārds";

	public final static String VERB = "Darbības vārds";
	public final static String ADVERB = "Apstākļa vārds";

	public final static String ADPOSITION = "Prievārds";
	public final static String CONJUNCTION = "Saiklis";
	public final static String PARTICLE = "Partikula";
	public final static String INTERJECTION = "Izsauksmes vārds";

	public final static String GEN_ONLY = "Ģenitīvenis";

	public final static String ABBREVIATION = "Saīsinājums";

	//=== Skaitlis =============================================================
	public final static String SINGULAR = "Vienskaitlis";
	public final static String PLURAL = "Daudzskaitlis";
	public static boolean isNumber(String test)
	{
		return allNumbers.contains(test);
	}
	public final static Set<String> allNumbers =
			Collections.unmodifiableSet(
					new HashSet(){{add(SINGULAR); add(PLURAL);}});
	//=== Dzmite ===============================================================
	public final static String FEMININE = "Sieviešu dzimte";
	public final static String MASCULINE = "Vīriešu dzimte";
	public static boolean isGender(String test)
	{
		return allGenders.contains(test);
	}
	public final static Set<String> allGenders =
			Collections.unmodifiableSet(
					new HashSet(){{add(FEMININE); add(MASCULINE);}});


	//=== Locījums =============================================================
	public final static String NOMINATIVE = "Nominatīvs";
	public final static String GENITIVE = "Ģenitīvs";
	public final static String DATIVE = "Datīvs";
	public final static String ACUSATIVE = "Akuzatīvs";
	public final static String LOCATIVE = "Lokatīvs";

	public static boolean isCase(String test)
	{
		return allCases.contains(test);
	}
	public final static Set<String> allCases =
			Collections.unmodifiableSet(
					new HashSet(){{add(NOMINATIVE); add(GENITIVE); add(DATIVE); add(ACUSATIVE); add(LOCATIVE);}});

	//=== Persona ==============================================================
	public final static String FIRST_PERSON = "Pirmā persona";
	public final static String SECOND_PERSON = "Otrā persona";
	public final static String THIRD_PERSON = "Trešā persona";

	//=== Laiks ================================================================
	public final static String PRESENT = "Tagadne";
	public final static String FUTURE = "Nākotne";
	public final static String PAST = "Pagātne";

	//=== Izteiksme ============================================================
	public final static String CONDITIONAL = "Vēlējuma izteiksme";
	public final static String DEBITIVE = "Vajadzības izteiksme";
	public final static String INFINITIVE = "Nenoteiksme";
	public final static String INDICATIVE = "Īstenības izteiksme";
	public final static String IMPERATIVE = "Pavēles izteiksme"; // Pavēles izteiksmes forma
	public final static String PARTICIPLE = "Divdabis";
	public final static String RELATIVE = "Atstāstījuma izteiksme";

	//=== Kārta ================================================================
	public final static String ACTIVE_VOICE = "Darāmā kārta"; // Ciešamās kārtas forma
	public final static String PASSIVE_VOICE = "Ciešamā kārta"; // Ciešamās kārtas forma


	//=== Noteiktība ===========================================================
	public final static String INDEFINITE_ENDING = "Nenoteiktā galotne";
	public final static String DEFINITE_ENDING = "Noteiktā galotne";

	//=== Pakāpe ===============================================================
	public final static String POSITIVE_DEGREE = "Pamata pakāpe";
	public final static String COMPARATIVE_DEGREE = "Pārākā pakāpe";
	public final static String SUPERLATIVE_DEGREE = "Vispārākā pakāpe";

	//=== Noliegums: universāls ================================================
	public final static String NEGATIVE = "Noliegums";

	//=== Teikuma veidi ========================================================
	public final static String DECLARATIVE_SENT = "Stāstījuma teikums";
	public final static String ENCOURAGE_SENT = "Pamudinājuma teikums";
	public final static String INTERJECTION_SENT = "Izsaukuma teikums";
	public final static String NEGATION_SENT = "Nolieguma teikums";
	public final static String QUESTION_SENT = "Jautājuma teikums";


	//=== Citi =================================================================
	public final static String NON_INFLECTIVE = "Nelokāms vārds";
	public final static String MULTI_INFLECTIVE = "Vairākos punktos lokāms saliktenis";

	//=== 1. konjugācijas informācijas kodēšanai ===============================
	public final static String STEMS_ARE_ORDERED = "Pēdējie celmi lietojami retāk";

	public final static String NO_PREFIX = "Vārds bez priedēkļa";
	public final static String HAS_PREFIX = "Vārds ar priedēkli";
}
