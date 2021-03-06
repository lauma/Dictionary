package lv.ailab.dict.struct.constants.flags;

/**
 * Vispārīgās (dažādām vārdnīcām izmantojamās) karodziņu grupas (atslēgas).
 *
 * Izveidots 2016-09-19.
 * @author Lauma
 */
public class Keys
{
	//=== Vispārīgie ===========================================================
	/**
	 * Vārdšķiras, saīsinājumi, salikteņu daļas, priedēkļi, vārdi svešvalodā
	 */
	public final static String POS = "Kategorija";
	/**
	 * Mežrūpniecība, zvejniecība, utt.
	 */
	public final static String DOMAIN = "Joma";

	/**
	 * Binārie (jā/nē, piemīt/nepiemīt) karodziņi.
	 */
	public final static String OTHER_FLAGS = "Citi";

	//=== Morfoloģiskās pazīmes ================================================
	/**
	 * Gramatiskā kategorija - skaitlis: vienskaitlis, daudzskaitlis vai
	 * divskaitlis (tēzaurā ir manīts viens gadījums).
	 * Norāda, ja kaut kādu iemeslu dēļ šķirkļavārdu analizējot, to ir izdevies
	 * noskaidrot. Lielākoties nav pašmērķis.
	 */
	public final static String NUMBER = "Skaitlis";
	/**
	 * Gramatiskā kategorija - dzimte: sieviešu, vīriešu un kopdzimte
	 * (morfoanalizatoram nav, bet tēzauram ir)
	 * Norāda, ja kaut kādu iemeslu dēļ šķirkļavārdu analizējot, to ir izdevies
	 * noskaidrot. Lielākoties nav pašmērķis.
	 */
	public final static String GENDER = "Dzimte";
	/*
	 * Gramatiskā kategorija - locījums - vietas, kur norādīts fiksēts locījums.
	 */
	public final static String CASE = "Locījums";

	/*
	 * Gramatiskā kategorija - darbības vārda persona.
	 */
	public final static String PERSON = "Persona";

	/*
	 * Gramatiskā kategorija - darbības vārda laiks.
	 */
	public final static String TENSE = "Laiks";

	/*
	 * Gramatiskā kategorija - darbības vārda izteiksme.
	 */
	public final static String MOOD = "Izteiksme";

	/*
	 * Gramatiskā kategorija - darbības vārda kārta.
	 */
	public final static String VOICE = "Kārta";

	/*
	 * Gramatiskā kategorija - īpašības vārda vai divdabja noteiktība.
	 */
	public final static String DEFINITENESS = "Noteiktība";

	/*
	 * Gramatiskā kategorija - īpašības vārda, divdabja vai apstākļa vārda
	 * pakāpe.
	 */
	public final static String DEGREE = "Pakāpe";

	//=== Struktrūras ==========================================================

	/**
	 * Atslēga teikuma veida norādīšanai.
	 */
	public final static String SENTENCE_TYPE = "Teikuma komunikatīvais tips";

	//=== Uzbūve ===============================================================
	/**
	 * Priedēkļa eksistence vārdam
	 */
	public final static String PREFIXNESS = "Priedēkļa piemitība";

	//=== Morfoimporta informācija =============================================
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - celms (katram šķirkļavārdam viens), ko
	 * veido no nenoteiksmes.
	 */
	public final static String INFINITIVE_STEM = "Stem1";
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - ar komatu atdalīti celmi, ko veido no
	 * tagadnes 3. personas.
	 */
	public final static String PRESENT_STEMS = "Stem2";
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - ar komatu atdalīti celmi, ko veido no
	 * pagātnes.
	 */
	public final static String PAST_STEMS = "Stem3";

	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - priedēkļi vai, salikteņu gadījumā,
	 * vārda sākumdaļa, kas nelokās.
	 */
	public final static String VERB_PREFIX = "Darbības vārda prefikss";

	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * Norādes par mijām un formu paralelitāti, ja ar paradigmām nepietiek, kā
	 * arī norāde par vairākos punktos lokāmu vārdu.
	 */
	public final static String INFLECTION_WEARDNES = "Locīšanas īpatnības";

	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * Vairākās vietās lokāmo salikteņu (vecāmāte, šīsaule) locīšanas šablons.
	 */
	public final static String MULTIINFLECTION_PATTERN = "Šablons salikteņa vairākpunktu locīšanai";
}
