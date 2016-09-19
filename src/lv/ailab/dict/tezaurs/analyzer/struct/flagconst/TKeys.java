package lv.ailab.dict.tezaurs.analyzer.struct.flagconst;

import lv.ailab.dict.struct.flagconst.Keys;

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
	 * Lietojums kopā ar konkrētiem vārdiem vai abstraktiem ierobežojumiem,
	 * piemēram, "[vārds] ģenitīvā". Abstraktajiem ierobežojumiem pēc iespējas
	 * nepieciešams lietot jau definētās TValues. Konkrētus vārdus jāliek
	 * pēdiņās.
	 */
	public final static String USED_TOGETHER_WITH = "Lieto kopā ar";
	/**
	 * Lietojums kopā ar konkrētiem vārdiem vai abstraktiem ierobežojumiem,
	 * piemēram, "[vārds] ģenitīvā". Abstraktajiem ierobežojumiem pēc iespējas
	 * nepieciešams lietot jau definētās TValues. Konkrētus vārdus jāliek
	 * pēdiņās.
	 * Šis variants ir gadījumiem, kad vārdnīcā ir "parasti savienojumā ar".
	 */
	public final static String USUALLY_USED_TOGETHER_WITH = "Parasti lieto kopā ar";
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "parasti".
	 * Ir viens izņēmuma gadījums, kad ir "parasti nelieto".
	 */

	public final static String USUALLY_USED_IN_FORM = "Parasti lieto noteiktā formā";
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "bieži".
	 */
	public final static String OFTEN_USED_IN_FORM = "Bieži lieto noteiktā formā";
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "tikai".
	 */
	public final static String USED_ONLY_IN_FORM = "Lieto tikai noteiktā formā";
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "arī".
	 * (Šis nudien ir ļaunums!)
	 */
	public final static String ALSO_USED_IN_FORM = "Lieto arī noteiktā formā";
	/**
	 * Nespecificēts, vispārīgs formu ierobežojums.
	 */
	public final static String USED_IN_FORM = "Lieto noteiktā formā";

	/**
	 * Bieži, reti, pareti.
	 */
	public final static String USAGE_FREQUENCY = "Lietojuma biežums";

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
}
