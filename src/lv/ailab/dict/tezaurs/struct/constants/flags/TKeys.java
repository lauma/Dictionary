package lv.ailab.dict.tezaurs.struct.constants.flags;

import lv.ailab.dict.struct.constants.flags.Keys;

/**
 * Karodziņu grupas.
 *
 * @author Lauma
 */
public class TKeys extends Keys
{
	/**
	 * Norādes par vārda daļu sīkāku grupējumu kategorijās (vārdšķiras analogs)
	 */
	public final static String WORDPART_POS = "Kategorija (vārda daļai)";

	/**
	 * Leksiski gramatiskā kategorija - pārejamība.
	 */
	public final static String TRANSITIVITY = "Pārejamība";
	/**
	 * Valoda, kurā ir šķirkļa vārds, ja tā nav latviešu
	 */
	public final static String LANGUAGE = "Valoda";

	/**
	 * Stilistiskās nokrāsas, piederība noteiktiem valodas slāņiem, kā arī
	 * būšana apvidvārdam, dialektismam, vai bērnu valodas vārdam.
	 */
	public final static String USAGE_RESTRICTIONS = "Lietojuma ierobežojumi";
	/**
	 * Konkrēts dialekts vai izloksne, piemēram, latgaliešu vai aukšzemnieku.
	 */
	public final static String DIALECT_FEATURES = "Dialekta iezīmes";
	/**
	 * "Īpašības vārda nozīmē", "Lietvārda nozīmē".
	 */
	public final static String POS_CONVERSION = "Konversija";

	/**
	 * Norāde par lielā/mazā burta lietojumu
	 */
	public final static String CAPITALIZATION = "Lielo/mazo burtu lietojums";

	/**
	 * No "Citi" izdalīta atslēga tiem karodziņiem, kas parasti strukturālo
	 * ierobežojumu elementā rakstoru vārda jēgas ierobežojumus.
	 */
	public final static String SEMANTIC_RESTR = "Semantisks frāzes raksturojums";
	/**
	 * No "Citi" izdalīta atslēga tiem karodziņiem, kas parasti strukturālo
	 * ierobežojumu elementā raksturo apmerēram sintaktiskus ierobežojumus, bet
	 * ne teikuma veidu.
	 */
	public final static String SMALL_SYNT_RESTR = "Apmēram morfosintaktisks ierobežojums, mazāks par teikumu";

	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * Šķirkļavārda īpatnības tipiski tiek uzrādītas divos gadījumos:
	 * 1) ja tēzaura šķirkļa vārds var nesakrist ar pamatformu (visbiežāk
	 *    skaitlis - tautību daudzskaitļi);
	 * 2) ja tiek veidots alternatīvais šķirļavārds, kas ar šo pazīmi atšķiras
	 *    no pamata šķirkļa vārda.
	 */
	public final static String ENTRYWORD_WEARDNES = "Šķirkļavārda īpatnības";
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - pamatvārda, ar kuru sakrīt locīšana,
	 * nenoteiksme. Nenoteiksmes homoformu gadījumā ar papildus paskaidrojumu.
	 */
	public final static String INFLECT_AS = "Locīt kā";

	// TODO: varbūt arī INFLECTION_WEARDNES un INFLECT_AS vajag pārcelt uz Keys?

	/**
	 * Paredzēts etimoloģijas izgūšanai.
	 * Pēc gramatiku apstrādes šos jāizceļ uz Entry.etymology lauku.
	 */
	public final static String ETYMOLOGY = "Etimoloģiskas norādes";

	/**
	 * Karodziņi, ko liek kā kļūdu paziņojumus, lai identificētu vēlāk
	 * pārskatāmās vietas.
	 */
	public final static String PROBLEMS = "Gramatiku analīzes problēmas";
}
