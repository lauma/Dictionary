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
	public final static String CONTAMINATION = "Kontaminācija";

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
	 * Norādes par mijām, 1. konjugācijas darbības vārdu homoformām un darbības
	 * vārdu paralēlformām.
	 */
	public final static String INFLECTION_WEARDNES = "Locīšanas īpatnības";
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
}
