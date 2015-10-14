package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.analyzer.struct.Flags;
import lv.ailab.tezaurs.utils.MappingSet;
import lv.ailab.tezaurs.utils.Tuple;

/**
 * Singletonklase, kas satur visus zināmos saīsinājumus, tos atšifrējumus, kā
 * arī visus citus karodziņus, kas veidojami no fiksētām teksta virknēm.
 * @author Lauma
 *
 */
public class AbbrMap {
	protected static AbbrMap singleton;
	protected MappingSet<String, String> binaryFlags;
	protected MappingSet<String, Tuple<Keys, String>> pairingFlags;

	public static AbbrMap getAbbrMap()
	{
		if (singleton == null) singleton = new AbbrMap();
		return singleton;
	}

	/**
	 * Gramatikas fragmentu (mazāko gabaliņu bez komatiem un semikoliem) mēģina
	 * atpazīt kā saīsinājumu.
	 * @param gramSegment	gramatikas fragments, kas varētu būt karodziņš
	 * @param collector		karodziņu kolekcija, kurā savāc visu, kas atbilst
	 *                      šim gramatikas fragmentam.
	 * @return vai tika atpazīts.
	 */
	public boolean translate(String gramSegment, Flags collector )
	{
		if (binaryFlags.containsKey(gramSegment))
			collector.pairings.putAll(Keys.OTHER_FLAGS, binaryFlags
					.getAll(gramSegment));
		if (pairingFlags.containsKey(gramSegment))
			for (Tuple<Keys, String> t : pairingFlags.getAll(gramSegment))
			collector.pairings.put(t.first, t.second);
		return (binaryFlags.containsKey(gramSegment) ||
				pairingFlags.containsKey(gramSegment));
	}

	protected AbbrMap()
	{
		binaryFlags = new MappingSet<>();
		pairingFlags = new MappingSet<>();

		pairingFlags.put("adj.", Features.POS__ADJ);
		pairingFlags.put("adv.", Tuple.of(Keys.POS, "Apstākļa vārds"));
		pairingFlags.put("apst.", Tuple.of(Keys.POS, "Apstākļa vārds"));
		pairingFlags.put("divd.", Features.POS__PARTICIPLE);
		pairingFlags.put("Divd.", Features.POS__PARTICIPLE);
		pairingFlags.put("interj.", Tuple.of(Keys.POS, "Izsauksmes vārds"));
		pairingFlags.put("īp. v.", Features.POS__ADJ);
		pairingFlags.put("īp.", Features.POS__ADJ);
		pairingFlags.put("izsauk.", Tuple.of(Keys.POS, "Izsauksmes vārds"));
		pairingFlags.put("jaut.", Tuple.of(Keys.POS, "Jautājamais vietniekvārds"));
		pairingFlags.put("lietv.", Features.POS__NOUN);
		pairingFlags.put("noliedz.", Tuple.of(Keys.POS, "Noliedzamais vietniekvārds"));
		pairingFlags.put("norād.", Tuple.of(Keys.POS, "Norādāmais vietniekvārds"));
		pairingFlags.put("noteic.", Tuple.of(Keys.POS, "Noteicamais vietniekvārds"));
		pairingFlags.put("part.", Tuple.of(Keys.POS, "Partikula"));
		pairingFlags.put("pieder.", Tuple.of(Keys.POS, "Piederības vietniekvārds"));
		pairingFlags.put("pried.", Tuple.of(Keys.POS, "Priedēklis")); // Vēlāk vajadzēs specifisku apstrādi?
		pairingFlags.put("prep.", Tuple.of(Keys.POS, "Prievārds"));
		pairingFlags.put("priev.", Tuple.of(Keys.POS, "Prievārds"));
		pairingFlags.put("saiklis.", Tuple.of(Keys.POS, "Saiklis"));
		pairingFlags.put("saiklis", Tuple.of(Keys.POS, "Saiklis"));
		pairingFlags.put("skait.", Features.POS__NUMERAL);
		pairingFlags.put("vietn.", Features.POS__PRONOUN);
		pairingFlags.put("vietniekv.", Features.POS__PRONOUN);	// ?
		pairingFlags.put("vispārin.", Tuple.of(Keys.POS, "Vispārināmais vietniekvārds"));
		pairingFlags.put("saīs.", Tuple.of(Keys.POS, "Saīsinājums"));
		pairingFlags.put("simb.", Tuple.of(Keys.POS, "Saīsinājums"));	// ?
		pairingFlags.put("salikteņu pirmā daļa.", Tuple.of(Keys.POS, "Salikteņu daļa"));
		pairingFlags.put("salikteņu pirmā daļa", Tuple.of(Keys.POS, "Salikteņu daļa"));
		pairingFlags.put("salikteņa pirmā daļa.", Tuple.of(Keys.POS, "Salikteņu daļa"));
		pairingFlags.put("salikteņa pirmā daļa", Tuple.of(Keys.POS, "Salikteņu daļa"));
		pairingFlags.put("salikteņu daļa.", Tuple.of(Keys.POS, "Salikteņu daļa"));
		pairingFlags.put("salikteņu daļa", Tuple.of(Keys.POS, "Salikteņu daļa"));

		pairingFlags.put("priev. ar ģen.", Tuple.of(Keys.POS, "Prievārds"));
		pairingFlags.put("priev. ar ģen.", Tuple.of(Keys.USED_TOGETHER_WITH, Values.GENITIVE.s));
		pairingFlags.put("ar ģen.", Tuple.of(Keys.POS, "Prievārds")); // Šķiet, ka bez papildus komentāriem to raksta tikai pie prievārdiem.
		pairingFlags.put("ar ģen.", Tuple.of(Keys.USED_TOGETHER_WITH, Values.GENITIVE.s));
		pairingFlags.put("priev. ar dat.", Tuple.of(Keys.POS, "Prievārds"));
		pairingFlags.put("priev. ar dat.", Tuple.of(Keys.USED_TOGETHER_WITH, Values.DATIVE.s));

		pairingFlags.put("persv.", Features.POS__NOUN);
		binaryFlags.put("persv.", "Personvārds");
		//pairingFlags.put("vietv.", Features.POS__NOUN); // jāuzmanās ar mazciemu "Siltais"
														// Ko darīt ar sastingušajām formām?
		binaryFlags.put("vietv.", "Vietvārds");

		binaryFlags.put("nelok.", Values.NON_INFLECTIVE.s);

		pairingFlags.put("akuz.", Tuple.of(Keys.CASE, Values.ACUSATIVE.s));
		pairingFlags.put("dat.", Tuple.of(Keys.CASE, Values.DATIVE.s));
		pairingFlags.put("ģen.", Tuple.of(Keys.CASE, Values.GENITIVE.s));
		pairingFlags.put("instr.", Tuple.of(Keys.CASE, "Instrumentālis"));
		pairingFlags.put("lok.", Tuple.of(Keys.CASE, Values.LOCATIVE.s));
		pairingFlags.put("nom.", Tuple.of(Keys.CASE, "Nominatīvs"));

		//binaryFlags.put("dsk. ģen.", "Daudzskaitļa ģenitīvs"); // Daudzskaitlinieks?
		pairingFlags.put("dsk. ģen.", Tuple.of(Keys.CASE, Values.GENITIVE.s));
		pairingFlags.put("dsk. ģen.", Tuple.of(Keys.NUMBER, Values.PLURAL.s));
		//binaryFlags.put("vsk. ģen.", "Vienskaitļa ģenitīvs");
		pairingFlags.put("vsk. ģen.", Tuple.of(Keys.CASE, Values.GENITIVE.s));
		pairingFlags.put("vsk. ģen.", Tuple.of(Keys.NUMBER, Values.SINGULAR.s));


		pairingFlags.put("divsk.", Tuple.of(Keys.NUMBER, "Divskaitlis")); // Mums tiešām tādi ir?
		pairingFlags.put("dsk.", Tuple.of(Keys.NUMBER, Values.PLURAL.s));
		pairingFlags.put("vsk.", Tuple.of(Keys.NUMBER, Values.SINGULAR.s));
		
		/*binaryFlags.put("nāk.", "Nākotne");
		binaryFlags.put("pag.", "Pagātne");
		binaryFlags.put("tag.", "Tagadne");
		
		binaryFlags.put("nenot.", "Nenoteiktā galotne");
		binaryFlags.put("not.", "Noteiktā galotne");*/
		
		pairingFlags.put("s.", Features.GENDER__FEM);
		pairingFlags.put("v.", Features.GENDER__MASC);
		pairingFlags.put("kopdz.", Tuple.of(Keys.GENDER, Values.COGENDER.s));
		
		binaryFlags.put("intrans.", "Nepārejošs");
		//pairingFlags.put("intrans.", Features.POS__VERB); // Kas ir ar divdabjiem?
		binaryFlags.put("intr.", "Nepārejošs");
		//pairingFlags.put("intr.", Features.POS__VERB);
		binaryFlags.put("trans.", "Pārejošs");
		//pairingFlags.put("trans.", Features.POS__VERB);
		binaryFlags.put("tr.", "Pārejošs");
		//pairingFlags.put("tr.", Features.POS__VERB);

		/*binaryFlags.put("konj.", "Konjugācija");
		binaryFlags.put("pers.", "Persona");*/

		binaryFlags.put("dem.", "Deminutīvs");
		binaryFlags.put("Dem.", "Deminutīvs");
		binaryFlags.put("imperf.", "Imperfektīva forma"); //???
		//pairingFlags.put("imperf.", Features.POS__VERB); // Kas ir ar divdabjiem?
		binaryFlags.put("Nol.", "Noliegums"); // Check with other sources!
		binaryFlags.put("refl.", "Refleksīvs");
		pairingFlags.put("refl.", Features.POS__VERB); // Laura sapētīja, ka pie lietvārdiem nav.
		//binaryFlags.put("Refl.", "Refleksīvs");	// šis ir tikai šķirkļos, garmatikās nē, līdz ar to, īsti vajadzīgs nav.
		//pairingFlags.put("Refl.", Features.POS__VERB);

		// joma
		pairingFlags.put("aeron.", Tuple.of(Keys.DOMAIN, "Aeronautika"));
		pairingFlags.put("agr.", Tuple.of(Keys.DOMAIN, "Agronomija"));
		pairingFlags.put("anat.", Tuple.of(Keys.DOMAIN, "Anatomija"));
		pairingFlags.put("antr.", Tuple.of(Keys.DOMAIN, "Antropoloģija"));
		pairingFlags.put("arheol.", Tuple.of(Keys.DOMAIN, "Arheoloģija"));
		pairingFlags.put("arhit.", Tuple.of(Keys.DOMAIN, "Arhitektūra"));
		pairingFlags.put("arh.", Tuple.of(Keys.DOMAIN, "Arhitektūra"));
		pairingFlags.put("astr.", Tuple.of(Keys.DOMAIN, "Astronomija"));
		pairingFlags.put("av.", Tuple.of(Keys.DOMAIN, "Aviācija"));
		pairingFlags.put("biol.", Tuple.of(Keys.DOMAIN, "Bioloģija"));
		pairingFlags.put("biškop.", Tuple.of(Keys.DOMAIN, "Biškopība"));
		pairingFlags.put("bot.", Tuple.of(Keys.DOMAIN, "Botānika"));
		pairingFlags.put("būvn.", Tuple.of(Keys.DOMAIN, "Būvniecība"));
		pairingFlags.put("dārzk.", Tuple.of(Keys.DOMAIN, "Dārzkopība"));
		pairingFlags.put("dzc.", Tuple.of(Keys.DOMAIN, "Dzelzceļš"));
		pairingFlags.put("ek.", Tuple.of(Keys.DOMAIN, "Ekonomika"));
		pairingFlags.put("ekol.", Tuple.of(Keys.DOMAIN, "Ekoloģija"));
		pairingFlags.put("ekon.", Tuple.of(Keys.DOMAIN, "Ekonomika"));
		pairingFlags.put("el.", Tuple.of(Keys.DOMAIN, "Elektrotehnika"));
		pairingFlags.put("etn.", Tuple.of(Keys.DOMAIN, "Etnogrāfija"));
		pairingFlags.put("farm.", Tuple.of(Keys.DOMAIN, "Farmakoloģija"));
		pairingFlags.put("filol.", Tuple.of(Keys.DOMAIN, "Filoloģija"));
		pairingFlags.put("filoz.", Tuple.of(Keys.DOMAIN, "Filozofija"));
		pairingFlags.put("fin.", Tuple.of(Keys.DOMAIN, "Finanses"));
		pairingFlags.put("fiz.", Tuple.of(Keys.DOMAIN, "Fizika"));
		pairingFlags.put("fiziol.", Tuple.of(Keys.DOMAIN, "Fizioloģija"));
		pairingFlags.put("fizk.", Tuple.of(Keys.DOMAIN, "Fiziskā kultūra un sports"));
		pairingFlags.put("folkl.", Tuple.of(Keys.DOMAIN, "Folklora"));
		pairingFlags.put("fotogr.", Tuple.of(Keys.DOMAIN, "Fotogrāfija"));
		pairingFlags.put("ģenēt.", Tuple.of(Keys.DOMAIN, "Ģenētika"));
		pairingFlags.put("ģeod.", Tuple.of(Keys.DOMAIN, "Ģeodēzija"));
		pairingFlags.put("ģeogr.", Tuple.of(Keys.DOMAIN, "Ģeogrāfija"));
		pairingFlags.put("ģeol.", Tuple.of(Keys.DOMAIN, "Ģeoloģija"));
		pairingFlags.put("ģeom.", Tuple.of(Keys.DOMAIN, "Ģeometrija"));
		pairingFlags.put("grāmatv.", Tuple.of(Keys.DOMAIN, "Grāmatvedība"));
		pairingFlags.put("hidr.", Tuple.of(Keys.DOMAIN, "Hidroloģija"));
		pairingFlags.put("hidrotehn.", Tuple.of(Keys.DOMAIN, "Hidrotehnika"));
		pairingFlags.put("hist.", Tuple.of(Keys.DOMAIN, "Historigrāfija"));
		pairingFlags.put("inf.", Tuple.of(Keys.DOMAIN, "Informātika"));
		pairingFlags.put("jur.", Tuple.of(Keys.DOMAIN, "Jurisprudence"));
		pairingFlags.put("jūrn.", Tuple.of(Keys.DOMAIN, "Jūrniecība"));
		pairingFlags.put("jūr.", Tuple.of(Keys.DOMAIN, "Jūrniecība"));
		pairingFlags.put("kap.", Tuple.of(Keys.DOMAIN, "Attiecas uz kapitālistisko iekārtu, kapitālistisko sabiedrību"));
		pairingFlags.put("kardioloģijā", Tuple.of(Keys.DOMAIN, "Kardioloģija"));
		pairingFlags.put("kart.", Tuple.of(Keys.DOMAIN, "Kartogrāfija"));
		pairingFlags.put("ker.", Tuple.of(Keys.DOMAIN, "Keramika"));
		pairingFlags.put("kibern.", Tuple.of(Keys.DOMAIN, "Kibernētika"));
		pairingFlags.put("kino", Tuple.of(Keys.DOMAIN, "Kinematogrāfija"));
		pairingFlags.put("kokapstr.", Tuple.of(Keys.DOMAIN, "Kokapstrāde"));
		pairingFlags.put("kul.", Tuple.of(Keys.DOMAIN, "Kulinārija"));
		pairingFlags.put("ķīm.", Tuple.of(Keys.DOMAIN, "Ķīmija"));
		pairingFlags.put("lauks.", Tuple.of(Keys.DOMAIN, "Lauksaimniecība"));
		pairingFlags.put("lauks. tehn.", Tuple.of(Keys.DOMAIN, "Lauksaimniecības tehnika"));
		pairingFlags.put("literat.", Tuple.of(Keys.DOMAIN, "Literatūrzinātne"));
		pairingFlags.put("loģ.", Tuple.of(Keys.DOMAIN, "Loģika"));
		pairingFlags.put("lopk.", Tuple.of(Keys.DOMAIN, "Lopkopība"));
		pairingFlags.put("mat.", Tuple.of(Keys.DOMAIN, "Matemātika"));
		pairingFlags.put("matem.", Tuple.of(Keys.DOMAIN, "Matemātika"));
		pairingFlags.put("materiālt.", Tuple.of(Keys.DOMAIN, "Materiālzinātne"));		// ?
		pairingFlags.put("med.", Tuple.of(Keys.DOMAIN, "Medicīna"));
		pairingFlags.put("medn.", Tuple.of(Keys.DOMAIN, "Medniecība"));
		pairingFlags.put("meh.", Tuple.of(Keys.DOMAIN, "Mehānika"));
		pairingFlags.put("met.", Tuple.of(Keys.DOMAIN, "Meteoroloģija"));
		pairingFlags.put("metal.", Tuple.of(Keys.DOMAIN, "Metalurģija"));
		pairingFlags.put("metāl.", Tuple.of(Keys.DOMAIN, "Metālapstrāde"));
		pairingFlags.put("meteorol.", Tuple.of(Keys.DOMAIN, "Meteoroloģija"));
		pairingFlags.put("mež.", Tuple.of(Keys.DOMAIN, "Mežniecība"));
		pairingFlags.put("mežr.", Tuple.of(Keys.DOMAIN, "Mežrūpniecība"));
		pairingFlags.put("mežs.", Tuple.of(Keys.DOMAIN, "Mežsaimniecība"));
		pairingFlags.put("mēb.", Tuple.of(Keys.DOMAIN, "Mēbeles"));
		pairingFlags.put("mil.", Tuple.of(Keys.DOMAIN, "Militārās zinātnes"));
		pairingFlags.put("min.", Tuple.of(Keys.DOMAIN, "Mineraloģija"));
		pairingFlags.put("mit.", Tuple.of(Keys.DOMAIN, "Mitoloģija"));
		pairingFlags.put("mūz.", Tuple.of(Keys.DOMAIN, "Mūzika"));
		pairingFlags.put("oftalmoloģijā", Tuple.of(Keys.DOMAIN, "Oftalmoloģija"));
		pairingFlags.put("ornit.", Tuple.of(Keys.DOMAIN, "Ornitoloģija"));
		pairingFlags.put("papīrr.", Tuple.of(Keys.DOMAIN, "Papīrrūpniecība"));
		pairingFlags.put("pārt.", Tuple.of(Keys.DOMAIN, "Pārtika"));
		pairingFlags.put("pol.", Tuple.of(Keys.DOMAIN, "Politika"));
		pairingFlags.put("poligr.", Tuple.of(Keys.DOMAIN, "Poligrāfija"));
		pairingFlags.put("polit.", Tuple.of(Keys.DOMAIN, "Politisks")); // ?
		pairingFlags.put("psih.", Tuple.of(Keys.DOMAIN, "Psiholoģija"));
		pairingFlags.put("rel.", Tuple.of(Keys.DOMAIN, "Reliģija"));
		pairingFlags.put("social.", Tuple.of(Keys.DOMAIN, "Socioloģija"));
		pairingFlags.put("sociol.", Tuple.of(Keys.DOMAIN, "Socioloģija"));
		pairingFlags.put("tehn.", Tuple.of(Keys.DOMAIN, "Tehnika"));
		pairingFlags.put("tehnol.", Tuple.of(Keys.DOMAIN, "Tehnoloģija"));
		pairingFlags.put("telek.", Tuple.of(Keys.DOMAIN, "Telekomunikācijas"));
		pairingFlags.put("telev.", Tuple.of(Keys.DOMAIN, "Televīzija"));
		pairingFlags.put("tekst.", Tuple.of(Keys.DOMAIN, "Tekstilrūpniecība"));
		pairingFlags.put("tekstilr.", Tuple.of(Keys.DOMAIN, "Tekstilrūpniecība"));
		pairingFlags.put("transp.", Tuple.of(Keys.DOMAIN, "Transports"));
		pairingFlags.put("TV", Tuple.of(Keys.DOMAIN, "Televīzija"));
		pairingFlags.put("val.", Tuple.of(Keys.DOMAIN, "Valodniecība"));
		pairingFlags.put("vet.", Tuple.of(Keys.DOMAIN, "Veterinārija"));
		pairingFlags.put("zool.", Tuple.of(Keys.DOMAIN, "Zooloģija"));

		pairingFlags.put("angļu", Tuple.of(Keys.LANGUAGE, "Angļu"));
		pairingFlags.put("angļu", Features.POS__FOREIGN);
		pairingFlags.put("angļu val.", Tuple.of(Keys.LANGUAGE, "Angļu"));
		pairingFlags.put("angļu val.", Features.POS__FOREIGN);
		pairingFlags.put("arābu", Tuple.of(Keys.LANGUAGE, "Arābu"));
		pairingFlags.put("arābu", Features.POS__FOREIGN);
		pairingFlags.put("arābu.", Tuple.of(Keys.LANGUAGE, "Arābu"));
		pairingFlags.put("arābu.", Features.POS__FOREIGN);
		pairingFlags.put("arābu val.", Tuple.of(Keys.LANGUAGE, "Arābu"));
		pairingFlags.put("arābu val.", Features.POS__FOREIGN);
		pairingFlags.put("ebr.", Tuple.of(Keys.LANGUAGE, "Ebreju"));
		pairingFlags.put("ebr.", Features.POS__FOREIGN);
		pairingFlags.put("ig. val.", Tuple.of(Keys.LANGUAGE, "Igauņu"));
		pairingFlags.put("ig. val.", Features.POS__FOREIGN);
		pairingFlags.put("krievu val.", Tuple.of(Keys.LANGUAGE, "Krievu"));
		pairingFlags.put("krievu val.", Features.POS__FOREIGN);
		pairingFlags.put("poļu val.", Tuple.of(Keys.LANGUAGE, "Poļu"));
		pairingFlags.put("poļu val.", Features.POS__FOREIGN);
		pairingFlags.put("turku val.", Tuple.of(Keys.LANGUAGE, "Turku"));
		pairingFlags.put("turku val.", Features.POS__FOREIGN);
		pairingFlags.put("vācu val.", Tuple.of(Keys.LANGUAGE, "Vācu"));
		pairingFlags.put("vācu val.", Features.POS__FOREIGN);
		pairingFlags.put("fr.", Tuple.of(Keys.LANGUAGE, "Franču"));
		pairingFlags.put("fr.", Features.POS__FOREIGN);
		pairingFlags.put("grieķu", Tuple.of(Keys.LANGUAGE, "Grieķu"));
		pairingFlags.put("grieķu", Features.POS__FOREIGN);
		pairingFlags.put("gr.", Tuple.of(Keys.LANGUAGE, "Grieķu"));
		pairingFlags.put("gr.", Features.POS__FOREIGN);
		pairingFlags.put("gr. val.", Tuple.of(Keys.LANGUAGE, "Grieķu"));
		pairingFlags.put("gr. val.", Features.POS__FOREIGN);
		pairingFlags.put("it.", Tuple.of(Keys.LANGUAGE, "Itāļu/Itāliešu"));
		pairingFlags.put("it.", Features.POS__FOREIGN);
		pairingFlags.put("lat.", Tuple.of(Keys.LANGUAGE, "Latīņu"));
		pairingFlags.put("lat.", Features.POS__FOREIGN);
		pairingFlags.put("latīņu val.", Tuple.of(Keys.LANGUAGE, "Latīņu"));
		pairingFlags.put("latīņu val.", Features.POS__FOREIGN);
		pairingFlags.put("liet.", Tuple.of(Keys.LANGUAGE, "Lietuviešu"));
		pairingFlags.put("liet.", Features.POS__FOREIGN);
		pairingFlags.put("port.", Tuple.of(Keys.LANGUAGE, "Portugāļu"));
		pairingFlags.put("port.", Features.POS__FOREIGN);
		pairingFlags.put("sanskr.", Tuple.of(Keys.LANGUAGE, "Sanskrits"));
		pairingFlags.put("sanskr.", Features.POS__FOREIGN);
		pairingFlags.put("sengr.", Tuple.of(Keys.LANGUAGE, "Sengrieķu"));
		pairingFlags.put("sengr.", Features.POS__FOREIGN);
		pairingFlags.put("sp.", Tuple.of(Keys.LANGUAGE, "Spāņu"));
		pairingFlags.put("sp.", Features.POS__FOREIGN);
		pairingFlags.put("ung.", Tuple.of(Keys.LANGUAGE, "Ungāru"));
		pairingFlags.put("ung.", Features.POS__FOREIGN);



		// Dialekti un izloksnes (tie ir arī lietojuma ierobežojumi)
		pairingFlags.put("dial. (augšzemnieku)", Tuple.of(Keys.DIALECT_FEATURES, "Agušzemnieku"));	// Unikāls.
		pairingFlags.put("dial. (augšzemnieku)", Tuple.of(Keys.USAGE_RESTRICTIONS, "Dialektisms"));	// Unikāls.
		pairingFlags.put("latg.", Tuple.of(Keys.DIALECT_FEATURES, "Latgaliešu"));
		pairingFlags.put("latg.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Dialektisms"));

		// Lietojuma ierobežojums
		pairingFlags.put("bērnu val.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("parasti bērnu val.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("apv.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Apvidvārds"));
		pairingFlags.put("vēst.", Tuple.of(Keys.USAGE_RESTRICTIONS, Values.HISTORICAL.s));
		// TODO vēst. laikam ir domēns nevis ierobežojums!!!!!!!!!
		pairingFlags.put("novec.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Novecojis"));
		pairingFlags.put("neakt.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Neaktuāls"));
		pairingFlags.put("poēt.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Poētiska stilistiskā nokrāsa"));
		pairingFlags.put("mīlin.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Mīlinājuma nokrāsa"));
		pairingFlags.put("niev.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Nievīga ekspresīvā nokrāsa"));
		pairingFlags.put("iron.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Ironiska ekspresīvā nokrāsa"));
		pairingFlags.put("hum.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Humoristiska ekspresīvā nokrāsa"));
		pairingFlags.put("vienk.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Vienkāršrunas stilistiskā nokrāsa"));
		pairingFlags.put("pārn.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Pārnestā nozīmē"));
		pairingFlags.put("eif.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Eifēmisms"));
		pairingFlags.put("bibl.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Biblisms"));
		pairingFlags.put("nevēl.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Nevēlams")); // TODO - nevēlamos, neliterāros un žargonus apvienot??
		pairingFlags.put("nelit.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Neliterārs"));
		pairingFlags.put("žarg.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Žargonvārds"));
		pairingFlags.put("sar.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Sarunvaloda"));
		pairingFlags.put("vulg.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Vulgārisms"));
		pairingFlags.put("barb.", Tuple.of(Keys.USAGE_RESTRICTIONS, "Barbarisms"));

		// Formu ierobežojumi
		pairingFlags.put("arī vsk.", Tuple.of(Keys.ALSO_USED_IN_FORM, Values.SINGULAR.s));		// Ļaunums.
		pairingFlags.put("parasti vsk.", Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.SINGULAR.s));
		pairingFlags.put("parasti vsk", Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.SINGULAR.s));
		pairingFlags.put("par. vsk.", Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.SINGULAR.s));
		pairingFlags.put("tikai vsk.", Tuple.of(Keys.USED_ONLY_IN_FORM, Values.SINGULAR.s));
		pairingFlags.put("arī dsk.", Tuple.of(Keys.ALSO_USED_IN_FORM, Values.PLURAL.s));	// Ļaunums.
		pairingFlags.put("parasti dsk.", Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PLURAL.s));
		pairingFlags.put("parasti dsk.", Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PLURAL.s));
		pairingFlags.put("tikai dsk.", Features.USED_ONLY__PLURAL);
		pairingFlags.put("parasti 3. pers.", Features.USUALLY_USED__THIRD_PERS);
		pairingFlags.put("parasti saliktajos laikos", Tuple.of(Keys.USUALLY_USED_IN_FORM, "Saliktie laiki"));
		pairingFlags.put("parasti saliktajos laikos", Features.POS__VERB);
		pairingFlags.put("parasti saliktajos laikos.", Tuple.of(Keys.USUALLY_USED_IN_FORM, "Saliktie laiki"));
		pairingFlags.put("parasti saliktajos laikos.", Features.POS__VERB);
		pairingFlags.put("parasti nenoteiksmē", Tuple.of(Keys.USUALLY_USED_IN_FORM, "Nenoteiksme"));
		pairingFlags.put("parasti nenoteiksmē", Features.POS__VERB);
		pairingFlags.put("parasti pavēles formā", Tuple.of(Keys.USUALLY_USED_IN_FORM, "Pavēles izteiksme"));
		pairingFlags.put("parasti pavēles formā", Features.POS__VERB);
		pairingFlags.put("parasti pavēles formā.", Tuple.of(Keys.USUALLY_USED_IN_FORM, "Pavēles izteiksme"));
		pairingFlags.put("parasti pavēles formā.", Features.POS__VERB);
		pairingFlags.put("ar not. gal.", Tuple.of(Keys.USED_IN_FORM, "Ar noteikto galotni"));
		pairingFlags.put("ar lielo sākumburtu", Tuple.of(Keys.USED_IN_FORM, "Ar lielo sākumburtu"));
		pairingFlags.put("ar mazo sākumburtu", Tuple.of(Keys.USED_IN_FORM, "Ar mazo sākumburtu"));

		// Lietojuma biežums.
		pairingFlags.put("pareti.", Tuple.of(Keys.USAGE_FREQUENCY, "Pareti"));
		pairingFlags.put("pareti", Tuple.of(Keys.USAGE_FREQUENCY, "Pareti"));
		pairingFlags.put("reti.", Tuple.of(Keys.USAGE_FREQUENCY, "Reti"));
		pairingFlags.put("reti", Tuple.of(Keys.USAGE_FREQUENCY, "Reti"));
		pairingFlags.put("retāk", Tuple.of(Keys.USAGE_FREQUENCY, "Retāk"));

		// Kontaminācija
		pairingFlags.put("subst. noz.", Tuple.of(Keys.CONTAMINATION, "Lietvārda nozīmē"));
		pairingFlags.put("lietv. nozīmē.", Tuple.of(Keys.CONTAMINATION, "Lietvārda nozīmē"));
		pairingFlags.put("īp. nozīmē.", Tuple.of(Keys.CONTAMINATION, "Īpašības vārda nozīmē"));

		binaryFlags.put("var.", "Variants");
		binaryFlags.put("hip.", "Hipotēze");
	}

}
