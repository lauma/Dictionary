package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.utils.Trio;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * Gram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar Rule.applyOptHyphens().
 * Šobrīd šeit ir formas ziņā vienkāršākie likumi, vēl daži ir
 * Gram.processBeginingWithPatterns()
 * @author Lauma
 */
public class OptHypernRules
{
	/**
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final Rule[] other = {
		/* Paradigm 11: Lietvārds 6. deklinācija -s
		 * Rules in form "-valsts, dsk. ģen. -valstu, s.", i.e containing full 6th
		 * declension nouns.
		 */
		SimpleRule.of("-acs, dsk. ģen. -acu, s.", ".*acs", 11,
				new String[] {"Lietvārds"}, new String[] {"Sieviešu dzimte"}), //uzacs, acs
		SimpleRule.of("-krāsns, dsk. ģen. -krāšņu, s.", ".*krāsns", 11,
				new String[] {"Lietvārds"}, new String[] {"Sieviešu dzimte"}), //aizkrāsns
		SimpleRule.of("-valsts, dsk. ģen. -valstu, s.", ".*valsts", 11,
				new String[] {"Lietvārds"}, new String[] {"Sieviešu dzimte"}), //agrārvalsts

		/* Paradigm 25: Pronouns
		 */
		SimpleRule.of("ģen. -kā, dat. -kam, akuz., instr. -ko", ".*kas", 25,
				new String[] {"Vietniekvārds", "Locīt kā \"kas\""}, null), //daudzkas
	};
	/**
	 * Paradigm 9: Lietvārds 5. deklinācija -e
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] fifthDeclNoun = {
		SimpleRule.fifthDeclStd("-upes, dsk. ģen. -upju", ".*upe"), //upe
	};
	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final Rule[] secondDeclNoun = {
			SimpleRule.secondDeclStd("-tēta, v.", ".*tētis"), //tētis
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final Rule[] directFirstConjVerb = {
		// Rules for both all person and third-person-only cases.
		// Verbs with infinitive homoforms:
		VerbRule.of("-aužu, -aud,", "-auž, pag. -audu", "aust", 15,
				new String[] {"Locīt kā \"aust\" (kā zirneklis)"}, null), //aizaust 2
		VerbRule.of("-dedzu, -dedz,", "-dedz, pag. -dedzu", "degt", 15,
				new String[] {"Locīt kā \"degt\" (kādu citu)"}, null), //aizdegt 1
		VerbRule.of("-degu, -dedz,", "-deg, pag. -degu", "degt", 15,
				new String[] {"Locīt kā \"degt\" (pašam)"}, null), //apdegt, aizdegt 2
		VerbRule.of("-dzenu, -dzen,", "-dzen, pag. -dzinu", "dzīt", 15,
				new String[] {"Locīt kā \"dzīt\" (kā lopus)"}, null), //aizdzīt 1
		VerbRule.of("-iru, -ir,", "-ir, pag. -īru", "irt", 15,
				new String[] {"Locīt kā \"irt\" (kā ar airiem)"}, null), //aizirt 1
		VerbRule.of("-minu, -min,", "-min, pag. -minu", "mīt", 15,
				new String[] {"Locīt kā \"mīt\" (kā pedāļus)"}, null), //aizmīt 1
		VerbRule.of("-miju, -mij,", "-mij, pag. -miju", "mīt", 15,
				new String[] {"Locīt kā \"mīt\" (kā naudu)"}, null), //aizmīt 2

		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A
		VerbRule.firstConjDir("-aru, -ar,", "-ar, pag. -aru", "art"), //aizart
		VerbRule.firstConjDir("-augu, -audz,", "-aug, pag. -augu", "augt"), //ieaugt, aizaugt
		// B
		VerbRule.firstConjDir("-bāžu, -bāz,", "-bāž, pag. -bāzu", "bāzt"), //aizbāzt
		VerbRule.firstConjDir("-bēgu, -bēdz,", "-bēg, pag. -bēgu", "bēgt"), //aizbēgt
		VerbRule.firstConjDir("-beru, -ber,", "-ber, pag. -bēru", "bērt"), //aizbērt
		VerbRule.firstConjDir("-bilstu, -bilsti,", "-bilst, pag. -bildu", "bilst"), //aizbilst
		VerbRule.firstConjDir("-birstu, -birsti,", "-birst, pag. -biru", "birt"), //apbirt, aizbirt
		VerbRule.firstConjDir("-braucu, -brauc,", "-brauc, pag. -braucu", "braukt"), //aizbraukt
		VerbRule.firstConjDir("-brāžu, -brāz,", "-brāž, pag. -brāzu", "brāzt"), //aizbrāzt
		VerbRule.firstConjDir("-brienu, -brien,", "-brien, pag. -bridu", "brist"), //aizbrist
		// C
		VerbRule.firstConjDir("-ceļu, -cel,", "-ceļ, pag. -cēlu", "celt"), //aizcelt
		VerbRule.firstConjDir("-cērtu, -cērt,", "-cērt, pag. -cirtu", "cirst"), //aizcirst
		// D
		VerbRule.firstConjDir("-diebju, -dieb,", "-diebj, pag. -diebu", "diebt"), //aizdiebt
		VerbRule.firstConjDir("-diedzu, -diedz,", "-diedz, pag. -diedzu", "diegt"), //aizdiegt 1,2
		VerbRule.firstConjDir("-dodu, -dod,", "-dod, pag. -devu", "dot"), //aizdot
		VerbRule.firstConjDir("-drāžu, -drāz,", "-drāž, pag. -drāzu", "drāzt"), //aizdrāzt
		VerbRule.firstConjDir("-duru, -dur,", "-dur, pag. -dūru", "durt"), //aizdurt
		VerbRule.firstConjDir("-dūcu, -dūc,", "-dūc, pag. -dūcu", "dūkt"), //atdūkt, aizdūkt
		VerbRule.firstConjDir("-dzeļu, -dzel,", "-dzeļ, pag. -dzēlu", "dzelt"), //atdzelt, aizdzelt
		VerbRule.firstConjDir("-dzeru, -dzer,", "-dzer, pag. -dzēru", "dzert"), //aizdzert
		// E
		VerbRule.firstConjDir("-ēdu, -ēd,", "-ēd, pag. -ēdu", "ēst"), //aizēst
		// F, G
		VerbRule.firstConjDir("-gāžu, -gāz,", "-gāž, pag. -gāzu", "gāzt"), //aizgāzt
		VerbRule.firstConjDir("-glaužu, -glaud,", "-glauž, pag. -glaudu", "glaust"), //aizglaust
		VerbRule.firstConjDir("-grābju, -grāb,", "-grābj, pag. -grābu", "grābt"), //aizgrābt
		VerbRule.firstConjDir("-graužu, -grauz,", "-grauž, pag. -grauzu", "grauzt"), //aizgrauzt
		VerbRule.firstConjDir("-griežu, -griez,", "-griež, pag. -griezu", "griezt"), //aizgriezt 1, 2
		VerbRule.firstConjDir("-grimstu, -grimsti,", "-grimst, pag. -grimu", "grimt"), //atgrimt, aizgrimt
		VerbRule.firstConjDir("-grūžu, -grūd,", "-grūž, pag. -grūdu", "grūst"), //aizgrūst
		VerbRule.firstConjDir("-gūstu, -gūsti,", "-gūst, pag. -guvu", "gūt"), //aizgūt
		// Ģ
		VerbRule.firstConjDir("-ģiedu, -ģied,", "-ģied, pag. -gidu", "ģist"), //apģist
		// H, I
		VerbRule.firstConjDir("-eju, -ej,", "-iet, pag. -gāju", "iet"), //apiet
		// J
		VerbRule.firstConjDir("-jāju, -jāj,", "-jāj, pag. -jāju", "jāt"), //aizjāt
		VerbRule.firstConjDir("-jožu, -joz,", "-jož, pag. -jozu", "jozt"), //aizjozt 1, 2
		VerbRule.firstConjDir("-jūdzu, -jūdz,", "-jūdz, pag. -jūdzu", "jūgt"), //aizjūgt
		// K
		VerbRule.firstConjDir("-kalstu, -kalsti,", "-kalst, pag. -kaltu", "kalst"), //izkalst, aizkalst
		VerbRule.firstConjDir("-kāpju, -kāp,", "-kāpj, pag. -kāpu", "kāpt"), //aizkāpt
		VerbRule.firstConjDir("-karu, -kar,", "-kar, pag. -kāru", "kārt"), //aizkārt
		VerbRule.firstConjDir("-kaucu, -kauc,", "-kauc, pag. -kaucu", "kaukt"), //izkaukt, aizkaukt
		VerbRule.firstConjDir("-kauju, -kauj,", "-kauj, pag. -kāvu", "kaut"), //apkaut
		VerbRule.firstConjDir("-klāju, -klāj,", "-klāj, pag. -klāju", "klāt"), //apklāt
		VerbRule.firstConjDir("-kliedzu, -kliedz,", "-kliedz, pag. -kliedzu", "kliegt"), //aizkliegt
		VerbRule.firstConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimtu", "klimst"), //aizklimst
		VerbRule.firstConjDir("-klīstu, -klīsti,", "-klīst, pag. -klīdu", "klīst"), //aizklīst
		VerbRule.firstConjDir("-kļūstu, -kļūsti,", "-kļūst, pag. -kļuvu", "kļūt"), //aizkļūt
		VerbRule.firstConjDir("-knābju, -knāb,", "-knābj, pag. -knābu", "knābt"), //uzknābt, aizknābt
		VerbRule.firstConjDir("-kožu, -kod,", "-kož, pag. -kodu", "kost"), //aizkost
		VerbRule.firstConjDir("-krāpju, -krāp,", "-krāpj, pag. -krāpu", "krāpt"), //aizkrāpt
		VerbRule.firstConjDir("-krauju, -krauj,", "-krauj, pag. -krāvu", "kraut"), //aizkraut
		VerbRule.firstConjDir("-krītu, -krīti,", "-krīt, pag. -kritu", "krist"), //aizkrist
		VerbRule.firstConjDir("-kuru, -kur,", "-kur, pag. -kūru", "kurt"), //aizkurt
		VerbRule.firstConjDir("-kūstu, -kusti,", "-kūst, pag. -kusu", "kust"), //aizkust
		VerbRule.firstConjDir("-kvēpstu, -kvēpsti,", "-kvēpst, pag. -kvēpu", "kvēpt"), //apkvēpt, aizkvēpt
		// Ķ
		VerbRule.firstConjDir("-ķepu, -ķep,", "-ķep, pag. -ķepu", "ķept"), //apķept, aizķept
		VerbRule.firstConjDir("-ķeru, -ķer,", "-ķer, pag. -ķēru", "ķert"), //aizķert
		// L
		VerbRule.firstConjDir("-laižu, -laid,", "-laiž, pag. -laidu", "laist"), //aizlaist
		VerbRule.firstConjDir("-laužu, -lauz,", "-lauž, pag. -lauzu", "lauzt"), //aizlauzt
		VerbRule.firstConjDir("-lecu, -lec,", "-lec, pag. -lēcu", "lēkt"), //aizlēkt
		VerbRule.firstConjDir("-liedzu, -liedz,", "-liedz, pag. -liedzu", "liegt"), //aizliegt
		VerbRule.firstConjDir("-liecu, -liec,", "-liec, pag. -liecu", "liekt"), //aizliekt
		VerbRule.firstConjDir("-leju, -lej,", "-lej, pag. -lēju", "liet"), //aizliet
		VerbRule.firstConjDir("-lieku, -liec,", "-liek, pag. -liku", "likt"), //aizlikt
		VerbRule.firstConjDir("-līpu, -līpi,", "-līp, pag. -lipu", "lipt"), //aplipt, aizlipt
		VerbRule.firstConjDir("-līkstu, -līksti,", "-līkst, pag. -līku", "līkt"), //nolīkt, aizlīkt
		VerbRule.firstConjDir("-lienu, -lien,", "-lien, pag. -līdu", "līst"), //aizlīst
		VerbRule.firstConjDir("-līstu, -līsti,", "-līst, pag. -liju", "līt"), //aplīt, aizlīt
		VerbRule.firstConjDir("-lobju, -lob,", "-lobj, pag. -lobu", "lobt"), //aizlobt
		VerbRule.firstConjDir("-lūdzu, -lūdz,", "-lūdz, pag. -lūdzu", "lūgt"), //aizlūgt
		VerbRule.firstConjDir("-lūstu, -lūsti,", "-lūst, pag. -lūzu", "lūzt"), //aizlūzt, aizlūzt
		// M
		VerbRule.firstConjDir("-metu, -met,", "-met, pag. -metu", "mest"), //aizmest
		VerbRule.firstConjDir("-mēžu, -mēz,", "-mēž, pag. -mēzu", "mēzt"), //aizmēzt
		VerbRule.firstConjDir("-miedzu, -miedz,", "-miedz, pag. -miedzu", "miegt"), //aizmiegt
		VerbRule.firstConjDir("-miegu, -miedz,", "-mieg, pag. -migu", "migt"), //aizmigt
		VerbRule.firstConjDir("-mirstu, -mirsti,", "-mirst, pag. -mirsu", "mirst"), //aizmirst
		VerbRule.firstConjDir("-mūku, -mūc,", "-mūk, pag. -muku", "mukt"), //aizmukt
		// N
		VerbRule.firstConjDir("-nesu, -nes,", "-nes, pag. -nesu", "nest"), //aiznest
		VerbRule.firstConjDir("-nirstu, -nirsti,", "-nirst, pag. -niru", "nirt"), //aiznirt
		// Ņ
		VerbRule.firstConjDir("-ņemu, -ņem,", "-ņem, pag. -ņēmu", "ņemt"), //aizņemt
		// O, P
		VerbRule.firstConjDir("-pampstu, -pampsti,", "-pampst, pag. -pampu", "pampt"), //nopampt, aizpampt
		VerbRule.firstConjDir("-pinu, -pin,", "-pin, pag. -pinu", "pīt"), //aizpīt
		VerbRule.firstConjDir("-ploku, -ploc,", "-plok, pag. -plaku", "plakt"), //aizplakt
		VerbRule.firstConjDir("-plaukstu, -plauksti,", "-plaukst, pag. -plauku", "plaukt"), //atplaukt, aizplaukt
		VerbRule.firstConjDir("-plēšu, -plēs,", "-plēš, pag. plēsu", "plēst"), //aizplēst
		VerbRule.firstConjDir("-plīstu, -plīsti,", "-plīst, pag. -plīsu", "plīst"), //applīst, aizplīst
		VerbRule.firstConjDir("-plūcu, -plūc,", "-plūc, pag. -plūcu", "plūkt"), //aizplūkt
		VerbRule.firstConjDir("-plūstu, -plūsti,", "-plūst, pag. -plūdu", "plūst"), //applūst, aizplūst
		VerbRule.firstConjDir("-pļauju, -pļauj,", "-pļauj, pag. -pļāvu", "pļaut"), //aizpļaut
		VerbRule.firstConjDir("-pūšu, -pūt,", "-pūš, pag. -pūtu", "pūst"), //aizpūst
		VerbRule.firstConjDir("-pūstu, -pūsti,", "-pūst, pag. -puvu", "pūt"), //aizpūt, pūt
		// R
		VerbRule.firstConjDir("-roku, -roc,", "-rok, pag. -raku", "rakt"), //aizrakt
		VerbRule.firstConjDir("-raušu, -raus,", "-rauš, pag. -rausu", "raust"), //aizraust
		VerbRule.firstConjDir("-rauju, -rauj,", "-rauj, pag. -rāvu", "raut"), //aizraut
		VerbRule.firstConjDir("-riebju, -rieb,", "-riebj, pag. -riebu", "riebt"), //aizriebt
		VerbRule.firstConjDir("-riju, -rij,", "-rij, pag. -riju", "rīt"), //aizrīt
		// S
		VerbRule.firstConjDir("-sāku, -sāc,", "-sāk, pag. -sāku", "sākt"), //aizsākt
		VerbRule.firstConjDir("-salkstu, -salksti,", "-salkst, pag. -salku", "salkt"), //aizsalkt
		VerbRule.firstConjDir("-sarkstu, -sarksti,", "-sarkst, pag. -sarku", "sarkt"), //aizsarkt
		VerbRule.firstConjDir("-saucu, -sauc,", "-sauc, pag. -saucu", "saukt"), //aizsaukt
		VerbRule.firstConjDir("-sedzu, -sedz,", "-sedz, pag. -sedzu", "segt"), //aizsegt
		VerbRule.firstConjDir("-seru, -ser,", "-ser, pag. -sēru", "sērt"), //aizsērt
		VerbRule.firstConjDir("-sēstu, -sēsti,", "-sēst, pag. -sēdu", "sēst"), //aizsēst
		VerbRule.firstConjDir("-sienu, -sien,", "-sien, pag. -sēju", "siet"), //aizsiet
		VerbRule.firstConjDir("-situ, -sit,", "-sit, pag. -situ", "sist"), //aizsist
		VerbRule.firstConjDir("-skaru, -skar,", "-skar, pag. -skāru", "skart"), //aizskart
		VerbRule.firstConjDir("-skuju, -skuj,", "-skuj, pag. -skuvu", "skūt"), //aizskūt
		VerbRule.firstConjDir("-slāpstu, -slāpsti,", "-slāpst, pag. -slāpu", "slāpt"), //aizslāpt
		VerbRule.firstConjDir("-slāju, -slāj,", "-slāj, pag. -slāju", "slāt"), //aizslāt
		VerbRule.firstConjDir("-slaucu, -slauc,", "-slauc, pag. -slaucu", "slaukt"), //aizslaukt
		VerbRule.firstConjDir("-slēdzu, -slēdz,", "-slēdz, pag. -slēdzu", "slēgt"), //aizslēgt
		VerbRule.firstConjDir("-slēpju, -slēp,", "-slēpj, pag. -slēpu", "slēpt"), //aizslēpt
		// Š
		VerbRule.firstConjDir("-šalcu, -šalc,", "-šalc, pag. -šalcu", "šalkt"), //pašalkt, aizšalkt
		// T
		VerbRule.firstConjDir("-tūkstu, -tūksti,", "-tūkst; pag. -tūku", "tūkt"), //aptūkt, aiztūkt
		VerbRule.firstConjDir("-tveru, -tver,", "-tver, pag. -tvēru", "tvert"), //aiztvert
		// U, V, Z

		// Single case rules.
		// Verb specific rules ordered by type and alphabetically by verb infinitive.
		SimpleRule.of("-gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu", ".*gult", 15,
				new String[] {"Darbības vārds", "Locīt kā \"gult\"", "Paralēlās formas"},
				null), //aizgult
		SimpleRule.of("-jumju, -jum, -jumj, pag. -jūmu, arī -jumu", ".*jumt", 15,
				new String[] {"Darbības vārds", "Locīt kā \"jumt\"", "Paralēlās formas"},
				null), //aizjumt
		SimpleRule.of("-plešu, -plet, -pleš, pag. -pletu, arī -plētu", ".*plest", 15,
				new String[] {"Darbības vārds", "Locīt kā \"plest\"", "Paralēlās formas"},
				null), //aizplest
		SimpleRule.of("-skrienu, -skrien, -skrien, arī -skreju, -skrej, -skrej, pag. -skrēju", ".*skriet", 15,
				new String[] {"Darbības vārds", "Locīt kā \"skriet\"", "Paralēlās formas"},
				null), //aizskriet
		SimpleRule.of("-tupstu, -tupsti, -tupst, pag. -tupu", ".*tupt", 15,
				new String[]{"Darbības vārds", "Locīt kā \"tupt\"", "Paralēlās formas"},
				null), //aiztupt
				// TODO tupu/tupstu

		// A, B
		ThirdPersVerbRule.firstConjDir("-brūk, pag. -bruka", "brukt"), //aizbrukt
		// C, D
		ThirdPersVerbRule.firstConjDir("-dim, pag. -dima", "dimt"), //aizdimt
		ThirdPersVerbRule.firstConjDir("-dip, pag. -dipa", "dipt"), //aizdipt
		// E, F, G
		ThirdPersVerbRule.firstConjDir("-grūst, pag. -gruva", "grūt"), //aizgrūt
		// H, I, J, K
		ThirdPersVerbRule.firstConjDir("-kviec, pag. -kvieca", "kviekt"), //aizkviekt
		// L, M
		ThirdPersVerbRule.firstConjDir("-milst, pag. -milza", "milzt"), //aizmilzt
		// N, Ņ
		ThirdPersVerbRule.firstConjDir("-ņirb, pag. -ņirba", "ņirbt"), //aizņirbt
		// O, P, R, S, Š, T, U, V, Z

		// Parallel forms and nonstandard.
		SimpleRule.of("parasti 3. pers., -aust, pag. -ausa", ".*aust", 15,
				new String[]{"Darbības vārds", "Locīt kā \"aust\" (kā gaisma)"},
				new String[]{"Parasti 3. personā"}), //aizaust 1
		SimpleRule.of("parasti 3. pers., -dzīst, pag. -dzija", ".*dzīt", 15,
				new String[]{"Darbības vārds", "Locīt kā \"dzīt\" (kā ievainojumi)"},
				new String[]{"Parasti 3. personā"}), //aizdzīt 2
		SimpleRule.of("parasti 3. pers., -irst, pag. -ira", ".*irt", 15,
				new String[] {"Darbības vārds", "Locīt kā \"irt\" (kā audums)"},
				new String[] {"Parasti 3. personā"}), //irt 2
		SimpleRule.of("3. pers. -guldz, pag. -guldza", ".*gulgt", 15,
				new String[]{"Darbības vārds", "Locīt kā \"gulgt\""},
				new String[]{"Parasti 3. personā"}), //aizgulgt

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Rules for both all person and third-person-only cases.
		VerbRule.secondConjDir("-dabūju, -dabū,", "-dabū, pag. -dabūju", "dabūt"), //aizdabūt

		// Single case rules.
		// Verb-specific rules.
		ThirdPersVerbRule.secondConjDir("-kūko, pag. -kūkoja", "kūkot"), //aizkūkot
		ThirdPersVerbRule.secondConjDir("-mirgo, pag. -mirgoja", "mirgot"), //aizmirgot
		// Parallel forms.
		// Īpašā piezīme par glumēšanu: 2. konjugāciju nosaka 3. personas
		// galotne "-ē" - 3. konjugācijai būtu bez.
		SimpleRule.of("parasti 3. pers., -ē, pag. -ēja (retāk -gluma, 1. konj.)", ".*glumēt", 16,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizglumēt
		SimpleRule.of(
				"parasti 3. pers., -glumē, pag. -glumēja (retāk -gluma, 1. konj.)",
				".*glumēt", 16,
				new String[]{"Darbības vārds", "Paralēlās formas"},
				new String[]{"Parasti 3. personā"}), //izglumēt


	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {
		// Rules for both all person and third-person-only cases.
		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A, B, C, D
		VerbRule.thirdConjDir("-dziedu, -dziedi,", "-dzied, pag. -dziedāju", "dziedāt"), //aizdziedāt
		// E, F, G
		VerbRule.thirdConjDir("-grabu, -grabi,", "-grab, pag. -grabēju", "grabēt"), //sagrabēt, aizgrabēt
		VerbRule.thirdConjDir("-guļu, -guli,", "-guļ, pag. -gulēju", "gulēt"), //aizgulēt
		// H, I, J, K
		VerbRule.thirdConjDir("-klabu, -klabi,", "-klab, pag. -klabēju", "klabēt"), //paklabēt, aizklabēt
		VerbRule.thirdConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimstēju", "klimstēt"), //aizklimstēt
		VerbRule.thirdConjDir("-kustu, -kusti,", "-kust, pag. -kustēju", "kustēt"), //aizkustēt
		VerbRule.thirdConjDir("-kūpu, -kūpi,", "-kūp, pag. -kūpēju", "kūpēt"), //apkūpēt, aizkūpēt
		// L
		VerbRule.thirdConjDir("-loku, -loki,", "-loka, pag. -locīju", "locīt"), //aizlocīt
		// M, N, O, P
		VerbRule.thirdConjDir("-peldu, -peldi,", "-peld, pag. -peldēju", "peldēt"), //aizpeldēt
		VerbRule.thirdConjDir("-pilu, -pili,", "-pil, pag. -pilēju", "pilēt"), //appilēt, aizpilēt
		VerbRule.thirdConjDir("-precu, -preci,", "-prec, pag. -precēju", "precēt"), //aizprecēt
		VerbRule.thirdConjDir("-putu, -puti,", "-put, pag. -putēju", "putēt"), //aizputēt, apputēt
		// R, S
		VerbRule.thirdConjDir("-slaku, -slaki,", "-slaka, pag. -slacīju", "slacīt"), //aizslacīt
		VerbRule.thirdConjDir("-slauku, -slauki,", "-slauka, pag. slaucīju", "slaucīt"), //aizslaucīt
		// T
		VerbRule.thirdConjDir("-turu, -turi,", "-tur, pag. -turēju", "turēt"), //aizturēt
		// U, V, Z

		// Single case rules.
		// Verb specific rules ordered by type and alphabetically by verb infinitive.
		SimpleRule.of(
				"-moku, -moki, -moka, arī -mocu, -moci, -moca, pag. -mocīju",
				".*mocīt", 17,
				new String[]{"Darbības vārds", "Paralēlās formas"}, null), //aizmocīt
		SimpleRule.of(
				"-rotu, -roti, -rota, arī -rotīju, -roti, -rotī, pag. -rotīju",
				".*rotīt", 17,
				new String[]{"Darbības vārds", "Paralēlās formas"}, null), //aizrotīt

		// A, B
		ThirdPersVerbRule.thirdConjDir("-blākš, pag. -blākšēja", "blākšēt"), //aizblākšēt
		ThirdPersVerbRule.thirdConjDir("-blākšķ, pag. -blākšķēja", "blākšķēt"), //aizblākšķēt
		// C, Č
		ThirdPersVerbRule.thirdConjDir("-čab, pag. -čabēja", "čabēt"), //aizčabēt
		ThirdPersVerbRule.thirdConjDir("-čaukst, pag. -čaukstēja", "čaukstēt"), //aizčaukstēt
		// D
		ThirdPersVerbRule.thirdConjDir("-dārd, pag. -dārdēja", "dārdēt"), //aizdārdēt
		ThirdPersVerbRule.thirdConjDir("-dimd, pag. -dimdēja", "dimdēt"), //aizdimdēt
		ThirdPersVerbRule.thirdConjDir("-dip, pag. -dipēja", "dipēt"), //aizdipēt
		ThirdPersVerbRule.thirdConjDir("-dun, pag. -dunēja", "dunēt"), //aizdunēt
		ThirdPersVerbRule.thirdConjDir("-džinkst, pag. -džinkstēja", "džinkstēt"), //aizdžinkstēt
		// E, F, G
		ThirdPersVerbRule.thirdConjDir("-gurkst, pag. -gurkstēja", "gurkstēt"), //aizgurkstēt
		// H, I, J, K
		ThirdPersVerbRule.thirdConjDir("-klakst, pag. -klakstēja", "klakstēt"), //aizklakstēt
		ThirdPersVerbRule.thirdConjDir("-klaudz, pag. -klaudzēja", "klaudzēt"), //aizklaudzēt
		// L, M, N, Ņ
		ThirdPersVerbRule.thirdConjDir("-ņirb, pag. -ņirbēja", "ņirbēt"), //aizņirbēt
		// O, P, R, S, T, U, V, Z

		// Parallel forms.
		SimpleRule.of("parasti 3. pers., -grand, pag. -grandēja (retāk -granda, 1. konj.)", ".*grandēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizgrandēt
		SimpleRule.of("parasti 3. pers., -gruzd, pag. -gruzdēja (retāk -gruzda, 1. konj.)", ".*gruzdēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizgruzdēt
		SimpleRule.of("parasti 3. pers., -mirdz, pag. -mirdzēja (retāk -mirdza, 1. konj.)", ".*mirdzēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizmirdzēt
		SimpleRule.of("parasti 3. pers., -pelē, arī -pel, pag. -pelēja", ".*pelēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizpelēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu
	 */
	public static final Rule[] directMultiConjVerb = {
		ComplexRule.of("-sargāju, -sargā, -sargā, arī -sargu, -sargi, -sarga, pag. -sargāju", new Trio[]{
				 Trio.of(".*sargāt", new Integer[] {16, 17}, new String[] {"Darbības vārds", "Paralēlās formas"})},
			  null), // aizsargāt
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
	 */
	public static final Rule[] reflFirstConjVerb = {
		// Rules for both all person and third-person-only cases.
		// Verbs with infinitive homoforms:
		VerbRule.of("-iros, -iries,", "-iras, pag. -īros", "irties", 18,
				new String[] {"Locīt kā \"irties\" (kā ar airiem)"},
				null), //aizirties
		VerbRule.of("-minos, -minies,", "-minas, pag. -minos", "mīties", 18,
				new String[] {"Locīt kā \"mīties\" (kā pedāļus)"},
				null), //aizmīties
		VerbRule.of("-mijos, -mijies,", "-mijas, pag. -mijos", "mīties", 18,
				new String[]{"Locīt kā \"mīties\" (kā naudu)"},
				null), //apmīties

		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A , B
		VerbRule.firstConjRefl("-brāžos, -brāzies,", "-brāžas, pag. -brāzos", "brāzties"), //aizbrāzties
		// C
		VerbRule.firstConjRefl("-ciešos, -cieties,", "-ciešas, pag. -cietos", "ciesties"), //aizciesties
		VerbRule.firstConjRefl("-cērtos, -cērties,", "-cērtas, pag. -cirtos", "cirsties"), //aizcirsties
		// D
		VerbRule.firstConjRefl("-drāžos, -drāzies,", "-drāžas, pag. -drāzos", "drāzties"), //aizdrāzties
		VerbRule.firstConjRefl("-duros, -duries,", "-duras, pag. -dūros", "durties"), //nodurties, aizdurties
		// E, F, G
		VerbRule.firstConjRefl("-gāžos, -gāzies,", "-gāžas, pag. -gāzos", "gāzties"), //apgāzties, aizgāzties
		VerbRule.firstConjRefl("-graužos, -grauzies,", "-graužas, pag. -grauzos", "grauzties"), //izgrauzties, aizgrauzties
		VerbRule.firstConjRefl("-griežos, -griezies,", "-griežas, pag. -griezos", "griezties"), //aizgriezties 1, 2
		// H, I, J
		VerbRule.firstConjRefl("-jūdzos, -jūdzies,", "-jūdzas, pag. -jūdzos", "jūgties"), //aizjūgties
		// K
		VerbRule.firstConjRefl("-karos, -karies,", "-karas, pag. -kāros", "kārties"), //apkārties
		VerbRule.firstConjRefl("-klājos, -klājies,", "-klājas, pag. -klājos", "klāties"), //apklāties
		VerbRule.firstConjRefl("-kuļos, -kulies,", "-kuļas, pag. -kūlos", "kulties"), //aizkulties
		VerbRule.firstConjRefl("-ķēros, -ķeries,", "-ķeras, pag. -ķēros", "ķerties"), //aizķerties
		// L
		VerbRule.firstConjRefl("-laižos, -laidies,", "-laižas, pag. -laidos", "laisties"), //aizlaisties
		VerbRule.firstConjRefl("-laužos, -lauzies,", "-laužas, pag. -lauzos", "lauzties"), //aizlauzties
		VerbRule.firstConjRefl("-liedzos, -liedzies,", "-liedzas, pag. -liedzos", "liegties"), //aizliegties
		VerbRule.firstConjRefl("-liecos, -liecies,", "-liecas, pag. -liecos", "liekties"), //aizliekties
		VerbRule.firstConjRefl("-liekos, -liecies,", "-liekas, pag. -likos", "likties"), //aizlikties
		// M
		VerbRule.firstConjRefl("-metos, -meties,", "-metas, pag. -metos", "mesties"), //aizmesties
		// N
		VerbRule.firstConjRefl("-nesos, -nesies,", "-nesas, pag. -nesos", "nesties"), //aiznesties
		// Ņ
		VerbRule.firstConjRefl("-ņemos, -ņemies,", "-ņemas, pag. -ņēmos", "ņemties"), //aizņemties
		// O, P, R
		VerbRule.firstConjRefl("-rokos, -rocies,", "-rokas, pag. -rakos", "rakties"), // aizrakties
		VerbRule.firstConjRefl("-rāpjos, -rāpies,", "-rāpjas, pag. -rāpos", "rāpties"), // aizrāpties
		VerbRule.firstConjRefl("-raušos, -rausies,", "-raušas, pag. -rausos", "rausties"), //aizrausties
		VerbRule.firstConjRefl("-raujos, -raujies,", "-raujas, pag. -rāvos", "rauties"), //aizrauties
		VerbRule.firstConjRefl("-riebjos, -riebies,", "-riebjas; pag. -riebos", "riebties"), //aizriebties
		// S
		VerbRule.firstConjRefl("-saucos, -saucies,", "-saucas, pag. -saucos", "saukties"), //aizsaukties
		VerbRule.firstConjRefl("-sedzos, -sedzies,", "-sedzas, pag. -sedzos", "segties"), //aizsegties
		VerbRule.firstConjRefl("-sitos, -sities,", "-sitas, pag. -sitos", "sisties"), //aizsisties
		// T, U, V, Z

		// Single case rules.
		// Paralel forms.
		SimpleRule.of("-gulstos, -gulsties, -gulstas, arī -guļos, -gulies, -guļas, pag. -gūlos, arī -gulos",
				".*gulties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"gulties\"", "Paralēlās formas"},
				null), //aizgulties
		SimpleRule.of("-plešos, -pleties, -plešas, pag. -pletos, arī -plētos",
				".*plesties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"plesties\"", "Paralēlās formas"},
				null), //ieplesties
		SimpleRule.of("-sēžos, -sēdies, -sēžas, arī -sēstos, -sēsties, -sēstas, pag. -sēdos",
				".*sēsties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"sēsties\"", "Paralēlās formas"},
				null), //aizsēsties
		SimpleRule.of("-tupstos, -tupsties, -tupstas, pag. -tupos",
				".*tupties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"tupties\"", "Paralēlās formas"},
				null), //aiztupties

		SimpleRule.of("parasti 3. pers., -plešas, pag. -pletās, arī -plētās",
				".*plesties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"plesties\"", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizplesties


	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
	 */
	public static final Rule[] reflThirdConjVerb = {
		// Rules for both all person and third-person-only cases.
		VerbRule.thirdConjRefl("-dzenos, -dzenies,", "-dzenas, pag. -dzinos", "dzīties"), //aizdzīties
		VerbRule.thirdConjRefl("-kustos, -kusties,", "-kustas, pag. -kustējos", "kustēties"), //aizkustēties
		VerbRule.thirdConjRefl("-peros, -peries,", "-peras, pag. -pēros", "pērties"), //aizpērties
		VerbRule.thirdConjRefl("-precos, -precies,", "-precas, pag. -precējos", "precēties"), //aizprecēties

		// Single case rules.
		ThirdPersVerbRule.thirdConjRefl("-lokās, pag. -locījās", "locīties"), //aizlocīties
		ThirdPersVerbRule.thirdConjRefl("-lokās, pag. -locījās", "locīties"), //aizlocīties

		// Parallel forms.
		ComplexRule.of("-sargājos, -sargājies, -sargājas, arī -sargos, -sargies, -sargās, pag. -sargājos", new Trio[]{
					Trio.of(".*sargāties", new Integer[]{19, 20}, new String[]{"Darbības vārds", "Paralēlās formas"})},
				null), // aizsargāties
		SimpleRule.of(
				"-mokos, -mokies, -mokās, arī -mocos, -mocies, -mocās, pag. -mocījos",
				".*mocīties", 20,
				new String[]{"Darbības vārds", "Paralēlās formas"},
				null) //aizmocīties
	};
}
