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

	//=== Citi =================================================================
	public final static String NON_INFLECTIVE = "Nelokāms vārds";
	public final static String MULTI_INFLECTIVE = "Vairākos punktos lokāms saliktenis";

	//=== 1. konjugācijas informācijas kodēšanai ===============================
	public final static String STEMS_ARE_ORDERED = "Pēdējie celmi lietojami retāk";

	public final static String NO_PREFIX = "Vārds bez priedēkļa";
}
