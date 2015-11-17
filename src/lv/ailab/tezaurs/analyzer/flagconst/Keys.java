package lv.ailab.tezaurs.analyzer.flagconst;

/**
 * Karodziņu grupas.
 *
 * @author Lauma
 */
public enum Keys
{
	/**
	 * Vārdšķiras, saīsinājumi, salikteņu daļas, priedēkļi, vārdi svešvalodā
	 */
	POS("Kategorija"),
	/**
	 * Valoda, kurā ir šķirkļa vārds, ja tā nav latviešu
	 */
	LANGUAGE("Valoda"),
	/**
	 * Lietojums kopā ar konkrētiem vārdiem vai abstraktiem ierobežojumiem,
	 * piemēram, "[vārds] ģenitīvā". Abstraktajiem ierobežojumiem pēc iespējas
	 * nepieciešams lietot jau definētās Values. Konkrētus vārdus jāliek
	 * pēdiņās.
	 */
	USED_TOGETHER_WITH("Lieto kopā ar"),
	/**
	 * Lietojums kopā ar konkrētiem vārdiem vai abstraktiem ierobežojumiem,
	 * piemēram, "[vārds] ģenitīvā". Abstraktajiem ierobežojumiem pēc iespējas
	 * nepieciešams lietot jau definētās Values. Konkrētus vārdus jāliek
	 * pēdiņās.
	 * Šis variants ir gadījumiem, kad vārdnīcā ir "parasti savienojumā ar".
	 */
	USUALLY_USED_TOGETHER_WITH("Parasti lieto kopā ar"),
	/**
	 * Mežrūpniecība, zvejniecība, utt.
	 */
	DOMAIN("Joma"),
	/**
	 * Stilistiskās nokrāsas, piederība noteiktiem valodas slāņiem, kā arī
	 * būšana apvidvārdam, dialektismam, vai bērnu valodas vārdam.
	 */
	USAGE_RESTRICTIONS("Lietojuma ierobežojumi"),
	/**
	 * Konkrēts dialekts vai izloksne, piemēram, latgaliešu vai aukšzemnieku.
	 */
	DIALECT_FEATURES("Dialekta iezīmes"),
	/**
	 * "Īpašības vārda nozīmē", "Lietvārda nozīmē".
	 */
	CONTAMINATION("Kontaminācija"),
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "parasti".
	 * Ir viens izņēmuma gadījums, kad ir "parasti nelieto".
	 */
	USUALLY_USED_IN_FORM("Parasti lieto noteiktā formā"),
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "bieži".
	 */
	OFTEN_USED_IN_FORM("Bieži lieto noteiktā formā"),
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "tikai".
	 */
	USED_ONLY_IN_FORM("Lieto tikai noteiktā formā"),
	/**
	 * Formu ierobežojumi, kas tēzaurā norādīti ar atslēgvārdu "arī".
	 * (Šis nudien ir ļaunums!)
	 */
	ALSO_USED_IN_FORM("Lieto arī noteiktā formā"),
	USED_IN_FORM("Lieto noteiktā formā"),
	/**
	 * Bieži, reti, pareti.
	 */
	USAGE_FREQUENCY("Lietojuma biežums"),
	/**
	 * Gramatiskā kategorija - skaitlis: vienskaitlis, daudzskaitlis vai
	 * divskaitlis (tēzaurā ir manīts viens gadījums).
	 * Norāda, ja kaut kādu iemeslu dēļ šķirkļavārdu analizējot, to ir izdevies
	 * noskaidrot. Lielākoties nav pašmērķis.
	 */
	NUMBER("Skaitlis"),
	/**
	 * Gramatiskā kategorija - dzimte: sieviešu, vīriešu un kopdzimte
	 * (morfoanalizatoram nav, bet tēzauram ir)
	 * Norāda, ja kaut kādu iemeslu dēļ šķirkļavārdu analizējot, to ir izdevies
	 * noskaidrot. Lielākoties nav pašmērķis.
	 */
	GENDER("Dzimte"),
	/**
	 * Gramatiskā kategorija - locījums - vietas, kur norādīts fiksēts locījums.
	 */
	CASE("Locījums"),
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * Šķirkļavārda īpatnības tipiski tiek uzrādītas divos gadījumos:
	 * 1) ja tēzaura šķirkļa vārds var nesakrist ar pamatformu (visbiežāk
	 *    skaitlis - tautību daudzskaitļi);
	 * 2) ja tiek veidots alternatīvais šķirļavārds, kas ar šo pazīmi atšķiras
	 *    no pamata šķirkļa vārda.
	 */
	ENTRYWORD_WEARDNES("Šķirkļavārda īpatnības"),
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * Norādes par mijām, 1. konjugācijas darbības vārdu homoformām un darbības
	 * vārdu paralēlformām.
	 */
	INFLECTION_WEARDNES("Locīšanas īpatnības"),
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - pamatvārda, ar kuru sakrīt locīšana,
	 * nenoteiksme. Nenoteiksmes homoformu gadījumā ar papildus paskaidrojumu.
	 */
	INFLECT_AS("Locīt kā"),

	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - celms, ko veido no nenoteiksmes.
	 */
	INFINITY_STEM("Stem1"),
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - celms, ko veido no tagadnes
	 * 3. personas.
	 */
	PRESENT_STEM("Stem2"),
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - celms, ko veido no pagātnes.
	 */
	PAST_STEM("Stem3"),
	/**
	 * Paredzēts morfoimporta atvieglošanai.
	 * 1. konjugācijas darbības vārdiem - priedēkļi vai, salikteņu gadījumā,
	 * vārda sākumdaļa, kas nelokās.
	 */
	VERB_PREFIX("Darbības vārda prefikss"),
	/**
	 * Binārie (jā/nē, piemīt/nepiemīt) karodziņi.
	 */
	OTHER_FLAGS("Citi"),
	;

	public String s;

	Keys(String name)
	{
		this.s = name;
	}

	public String toString()
	{
		return s;
	}
}
