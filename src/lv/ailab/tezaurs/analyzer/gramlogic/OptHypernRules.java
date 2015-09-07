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
				new String[] {"Locīt kā \"aust\" (kā audumu)"}, null), //aizaust 2
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
		VerbRule.firstConjDir("-buru, -bur,", "-bur, pag. -būru", "burt"), //apburt
		// C
		VerbRule.firstConjDir("-ceļu, -cel,", "-ceļ, pag. -cēlu", "celt"), //aizcelt
		VerbRule.firstConjDir("-cepu, -cep,", "-cep, pag. -cepu", "cept"), //apcept
		VerbRule.firstConjDir("-cērpu, -cērp,", "-cērp, pag. -cirpu", "cirpt"), //apcirpt
		VerbRule.firstConjDir("-cērtu, -cērt,", "-cērt, pag. -cirtu", "cirst"), //aizcirst
		// D
		VerbRule.firstConjDir("-diebju, -dieb,", "-diebj, pag. -diebu", "diebt"), //aizdiebt
		VerbRule.firstConjDir("-diedzu, -diedz,", "-diedz, pag. -diedzu", "diegt"), //aizdiegt 1,2
		VerbRule.firstConjDir("-dilstu, -dilsti,", "-dilst, pag. -dilu", "dilt"), //apdilt
		VerbRule.firstConjDir("-dodu, -dod,", "-dod, pag. -devu", "dot"), //aizdot
		VerbRule.firstConjDir("-drāžu, -drāz,", "-drāž, pag. -drāzu", "drāzt"), //aizdrāzt
		VerbRule.firstConjDir("-duru, -dur,", "-dur, pag. -dūru", "durt"), //aizdurt
		VerbRule.firstConjDir("-dūcu, -dūc,", "-dūc, pag. -dūcu", "dūkt"), //aizdūkt
		VerbRule.firstConjDir("-dvešu, -dves,", "-dveš, pag. -dvesu", "dvest"), //apdvest
		VerbRule.firstConjDir("-dzeļu, -dzel,", "-dzeļ, pag. -dzēlu", "dzelt"), //atdzelt, aizdzelt
		VerbRule.firstConjDir("-dzeru, -dzer,", "-dzer, pag. -dzēru", "dzert"), //aizdzert
		VerbRule.firstConjDir("-dzēšu, -dzēs,", "-dzēš, pag. -dzēsu", "dzēst"), //apdzēst
		// E
		VerbRule.firstConjDir("-ēdu, -ēd,", "-ēd, pag. -ēdu", "ēst"), //aizēst
		// F, G
		VerbRule.firstConjDir("-gāžu, -gāz,", "-gāž, pag. -gāzu", "gāzt"), //aizgāzt
		VerbRule.firstConjDir("-glaužu, -glaud,", "-glauž, pag. -glaudu", "glaust"), //aizglaust
		VerbRule.firstConjDir("-graužu, -grauz,", "-grauž, pag. -grauzu", "grauzt"), //aizgrauzt
		VerbRule.firstConjDir("-grābju, -grāb,", "-grābj, pag. -grābu", "grābt"), //aizgrābt
		VerbRule.firstConjDir("-grebju, -greb,", "-grebj, pag. -grebu", "grebt"), //apgrebt
		VerbRule.firstConjDir("-griežu, -griez,", "-griež, pag. -griezu", "griezt"), //aizgriezt 1, 2
		VerbRule.firstConjDir("-grimstu, -grimsti,", "-grimst, pag. -grimu", "grimt"), //atgrimt, aizgrimt
		VerbRule.firstConjDir("-grūžu, -grūd,", "-grūž, pag. -grūdu", "grūst"), //aizgrūst
		VerbRule.firstConjDir("-gurstu, -gursti,", "-gurst, pag. -guru", "gurt"), //apgurt
		VerbRule.firstConjDir("-gūstu, -gūsti,", "-gūst, pag. -guvu", "gūt"), //aizgūt
		// Ģ
		VerbRule.firstConjDir("-ģērbju, -ģērb,", "-ģērbj, pag. -ģērbu", "ģērbt"), //apģist
		VerbRule.firstConjDir("-ģiedu, -ģied,", "-ģied, pag. -gidu", "ģist"), //apģist
		// H, I
		VerbRule.firstConjDir("-eju, -ej,", "-iet, pag. -gāju", "iet"), //apiet
		// J
		VerbRule.firstConjDir("-jāju, -jāj,", "-jāj, pag. -jāju", "jāt"), //aizjāt
		VerbRule.firstConjDir("-jēdzu, -jēdz,", "-jēdz, pag. -jēdzu", "jēgt"), //apjēgt
		VerbRule.firstConjDir("-jožu, -joz,", "-jož, pag. -jozu", "jozt"), //aizjozt 1, 2
		VerbRule.firstConjDir("-jūku, -jūc,", "-jūk, pag. -juku", "jukt"), //apjukt
		VerbRule.firstConjDir("-jūdzu, -jūdz,", "-jūdz, pag. -jūdzu", "jūgt"), //aizjūgt
		// K
		VerbRule.firstConjDir("-kalstu, -kalsti,", "-kalst, pag. -kaltu", "kalst"), //aizkalst
		VerbRule.firstConjDir("-kaļu, -kal,", "-kaļ, pag. -kalu", "kalt"), //apkalt
		VerbRule.firstConjDir("-kampju, -kamp,", "-kampj, pag. -kampu", "kampt"), //apkampt
		VerbRule.firstConjDir("-karstu, -karsti,", "-karst, pag. -karsu", "karst"), //apkarst
		VerbRule.firstConjDir("-kaucu, -kauc,", "-kauc, pag. -kaucu", "kaukt"), //izkaukt, aizkaukt
		VerbRule.firstConjDir("-kauju, -kauj,", "-kauj, pag. -kāvu", "kaut"), //apkaut
		VerbRule.firstConjDir("-kāpju, -kāp,", "-kāpj, pag. -kāpu", "kāpt"), //aizkāpt
		VerbRule.firstConjDir("-karu, -kar,", "-kar, pag. -kāru", "kārt"), //aizkārt
		VerbRule.firstConjDir("-klāju, -klāj,", "-klāj, pag. -klāju", "klāt"), //apklāt
		VerbRule.firstConjDir("-kliedzu, -kliedz,", "-kliedz, pag. -kliedzu", "kliegt"), //aizkliegt
		VerbRule.firstConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimtu", "klimst"), //aizklimst
		VerbRule.firstConjDir("-klīstu, -klīsti,", "-klīst, pag. -klīdu", "klīst"), //aizklīst
		VerbRule.firstConjDir("-klustu, -klusti,", "-klust, pag. -klusu", "klust"), //apklust
		VerbRule.firstConjDir("-kļauju, -kļauj,", "-kļauj, pag. -kļāvu", "kļaut"), //apkļaut
		VerbRule.firstConjDir("-kļūstu, -kļūsti,", "-kļūst, pag. -kļuvu", "kļūt"), //aizkļūt
		VerbRule.firstConjDir("-knābju, -knāb,", "-knābj, pag. -knābu", "knābt"), //aizknābt
		VerbRule.firstConjDir("-kniebju, -knieb,", "-kniebj, pag. -kniebu", "kniebt"), //apkniebt
		VerbRule.firstConjDir("-kopju, -kop,", "-kopj, pag. -kopu", "kopt"), //apkopt
		VerbRule.firstConjDir("-kožu, -kod,", "-kož, pag. -kodu", "kost"), //aizkost
		VerbRule.firstConjDir("-krāpju, -krāp,", "-krāpj, pag. -krāpu", "krāpt"), //aizkrāpt
		VerbRule.firstConjDir("-krauju, -krauj,", "-krauj, pag. -krāvu", "kraut"), //aizkraut
		VerbRule.firstConjDir("-kremtu, -kremt,", "-kremt, pag. -krimtu", "krimst"), //apkrimst
		VerbRule.firstConjDir("-krītu, -krīti,", "-krīt, pag. -kritu", "krist"), //aizkrist
		VerbRule.firstConjDir("-kuļu, -kul,", "-kuļ, pag. -kūlu", "kult"), //apkult
		VerbRule.firstConjDir("-kuru, -kur,", "-kur, pag. -kūru", "kurt"), //aizkurt
		VerbRule.firstConjDir("-kūstu, -kusti,", "-kūst, pag. -kusu", "kust"), //aizkust
		VerbRule.firstConjDir("-kūpu, -kūpi,", "-kūp, pag. -kūpu", "kūpt"), //apkūpt
		VerbRule.firstConjDir("-kvēpstu, -kvēpsti,", "-kvēpst, pag. -kvēpu", "kvēpt"), //apkvēpt, aizkvēpt
		// Ķ
		VerbRule.firstConjDir("-ķepu, -ķep,", "-ķep, pag. -ķepu", "ķept"), //apķept, aizķept
		VerbRule.firstConjDir("-ķeru, -ķer,", "-ķer, pag. -ķēru", "ķert"), //aizķert
		// L
		VerbRule.firstConjDir("-laižu, -laid,", "-laiž, pag. -laidu", "laist"), //aizlaist
		VerbRule.firstConjDir("-laužu, -lauz,", "-lauž, pag. -lauzu", "lauzt"), //aizlauzt
		VerbRule.firstConjDir("-lencu, -lenc,", "-lenc, pag. -lencu", "lenkt"), //aplenkt
		VerbRule.firstConjDir("-lecu, -lec,", "-lec, pag. -lēcu", "lēkt"), //aizlēkt
		VerbRule.firstConjDir("-lēšu, -lēs,", "-lēš, pag. -lēsu", "lēst"), //aplēst
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
		VerbRule.firstConjDir("-maļu, -mal,", "-maļ, pag. -malu", "malt"), //apmalt
		VerbRule.firstConjDir("-metu, -met,", "-met, pag. -metu", "mest"), //aizmest
		VerbRule.firstConjDir("-mēžu, -mēz,", "-mēž, pag. -mēzu", "mēzt"), //aizmēzt
		VerbRule.firstConjDir("-miedzu, -miedz,", "-miedz, pag. -miedzu", "miegt"), //aizmiegt
		VerbRule.firstConjDir("-miegu, -miedz,", "-mieg, pag. -migu", "migt"), //aizmigt
		VerbRule.firstConjDir("-mirstu, -mirsti,", "-mirst, pag. -mirsu", "mirst"), //aizmirst
		VerbRule.firstConjDir("-mūku, -mūc,", "-mūk, pag. -muku", "mukt"), //aizmukt
		VerbRule.firstConjDir("-mulstu, -mulsti,", "-mulst, pag. -mulsu", "mulst"), //apmulst
		// N
		VerbRule.firstConjDir("-nāku, -nāc,", "-nāk, pag. -nācu", "nākt"), //apnākt
		VerbRule.firstConjDir("-nesu, -nes,", "-nes, pag. -nesu", "nest"), //aiznest
		VerbRule.firstConjDir("-nirstu, -nirsti,", "-nirst, pag. -niru", "nirt"), //aiznirt
		// Ņ
		VerbRule.firstConjDir("-ņemu, -ņem,", "-ņem, pag. -ņēmu", "ņemt"), //aizņemt
		VerbRule.firstConjDir("-ņirdzu, -ņirdz,", "-ņirdz, pag. -ņirdzu", "ņirgt"), //apņirgt
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
		VerbRule.firstConjDir("-slīgstu, -slīgsti,", "-slīgst, pag. -slīgu", "slīgt"), //aizslīgt
		VerbRule.firstConjDir("-smoku, -smoc,", "-smok, pag. -smaku", "smakt"), //aizsmakt
		VerbRule.firstConjDir("-snaužu, -snaud,", "-snauž, pag. -snaudu", "snaust"), //aizsnaust
		VerbRule.firstConjDir("-sniedzu, -sniedz,", "-sniedz, pag. -sniedzu", "sniegt"), //aizsniegt
		VerbRule.firstConjDir("-speru, -sper,", "-sper, pag. -spēru", "spert"), //aizspert
		VerbRule.firstConjDir("-spēju, -spēj,", "-spēj, pag. -spēju", "spēt"), //aizspēt
		VerbRule.firstConjDir("-spiežu, -spied,", "-spiež, pag. -spiedu", "spiest"), //aizspiest
		VerbRule.firstConjDir("-spļauju, -spļauj,", "-spļauj, pag. -spļāvu", "spļaut"), //aizspļaut
		VerbRule.firstConjDir("-spraužu, -spraud,", "-sprauž, pag. -spraudu", "spraust"), //aizspraust
		VerbRule.firstConjDir("-spruku, -sprūc,", "-sprūk, pag. -spruku", "sprukt"), //aizsprukt
		VerbRule.firstConjDir("-stāju, -stāj,", "-stāj, pag. -stāju", "stāt"), //aizstāt
		VerbRule.firstConjDir("-steidzu, -steidz,", "-steidz, pag. -steidzu", "steigt"), //aizsteigt
		VerbRule.firstConjDir("-stiepju, -stiep,", "-stiepj, pag. -stiepu", "stiept"), //aizstiept
		VerbRule.firstConjDir("-stumju, -stum,", "-stumj, pag. -stūmu", "stumt"), //aizstumt
		VerbRule.firstConjDir("-sviežu, -svied,", "-sviež, pag. -sviedu", "sviest"), //aizsviest
		VerbRule.firstConjDir("-svilstu, -svilsti,", "-svilst, pag. -svilu", "svilt"), //aizsvilt
		// Š
		VerbRule.firstConjDir("-šalcu, -šalc,", "-šalc, pag. -šalcu", "šalkt"), //pašalkt, aizšalkt
		VerbRule.firstConjDir("-šauju, -šauj,", "-šauj, pag. -šāvu", "šaut"), //aizšaut
		VerbRule.firstConjDir("-šķeļu, -šķel,", "-šķeļ, pag. šķēlu", "šķelt"), //aizšķelt
		VerbRule.firstConjDir("-šķiežu, -šķied,", "-šķiež, pag. -šķiedu", "šķiest"), //aizšķiest
		VerbRule.firstConjDir("-šķiļu, -šķil,", "-šķiļ, pag. -šķīlu", "šķilt"), //aizšķilt
		VerbRule.firstConjDir("-šķiru, -šķir,", "-šķir, pag. -šķīru", "šķirt"), //aizšķirt
		VerbRule.firstConjDir("-šļācu, -šļāc,", "-šļāc, pag. -šļācu", "šļākt"), //aizšļākt
		VerbRule.firstConjDir("-šļuku, -šļūc,", "-šļūk, pag. -šļuku", "šļukt"), //aizšļukt
		VerbRule.firstConjDir("-šļūcu, -šļūc,", "-šļūc, pag. -šļūcu", "šļūkt"), //aizšļūkt
		VerbRule.firstConjDir("-šmaucu, -šmauc,", "-šmauc, pag. -šmaucu", "šmaukt"), //aizšmaukt
		VerbRule.firstConjDir("-šuju, -šuj,", "-šuj, pag. -šuvu", "šūt"), //aizšūt
		// T
		VerbRule.firstConjDir("-tieku, -tiec,", "-tiek, pag. -tiku", "tikt"), //aiztikt 1, 2
		VerbRule.firstConjDir("-tinu, -tin,", "-tin, pag. -tinu", "tīt"), //aiztīt
		VerbRule.firstConjDir("-traucu, -trauc,", "-trauc, pag. -traucu", "traukt"), //aiztraukt
		VerbRule.firstConjDir("-trencu, -trenc,", "-trenc, pag. -trencu", "trenkt"), //aiztrenkt
		VerbRule.firstConjDir("-triecu, -triec,", "-triec, pag. -triecu", "triekt"), //aiztriekt
		VerbRule.firstConjDir("-triepju, -triep,", "-triepj, pag. -triepu", "triept"), //aiztriept
		VerbRule.firstConjDir("-tūkstu, -tūksti,", "-tūkst; pag. -tūku", "tūkt"), //aptūkt, aiztūkt
		VerbRule.firstConjDir("-tveru, -tver,", "-tver, pag. -tvēru", "tvert"), //aiztvert
		// U
		VerbRule.firstConjDir("-urbju, -urb,", "-urbj, pag. -urbu", "urbt"), //aizurbt
		// V
		VerbRule.firstConjDir("-vācu, -vāc,", "-vāc, pag. -vācu", "vākt"), //aizvākt
		VerbRule.firstConjDir("-vāžu, -vāz,", "-vāž, pag. -vāzu", "vāzt"), //aizvāzt
		VerbRule.firstConjDir("-veļu, -vel,", "-veļ, pag. -vēlu", "velt"), //aizvelt
		VerbRule.firstConjDir("-veru, -ver,", "-ver, pag. -vēru", "vērt"), //aizvērt
		VerbRule.firstConjDir("-vedu, -ved,", "-ved, pag. -vedu", "vest"), //aizvest
		VerbRule.firstConjDir("-vedu, -ved,", "-ved, pag. -vedu", "vest"), //aizvest
		VerbRule.firstConjDir("-vērpju, -vērp,", "-vērpj, pag. -vērpu", "vērpt"), //aizvērpt
		VerbRule.firstConjDir("-vēršu, -vērs,", "-vērš, pag. -vērsu", "vērst"), //aizvērst
		VerbRule.firstConjDir("-velku, -velc,", "-velk, pag. vilku", "vilkt"), //aizvilkt
		VerbRule.firstConjDir("-viļu, -vil,", "-viļ, pag. -vīlu", "vilt"), //aizvilt
		VerbRule.firstConjDir("-viju, -vij,", "-vij, pag. -viju", "vīt"), //aizvīt
		// Z
		VerbRule.firstConjDir("-ziežu, -zied,", "-ziež, pag. -ziedu", "ziest"), // aizziest
		VerbRule.firstConjDir("-zūdu, -zūdi,", "-zūd, pag. -zudu", "zust"), // aizzust
		// Ž
		VerbRule.firstConjDir("-žmiedzu, -žmiedz,", "-žmiedz, pag. -žmiedzu", "žmiegt"), // aizžmiegt
		VerbRule.firstConjDir("-žņaudzu, -žņaudz,", "-žņaudz, pag. -žņaudzu", "žņaugt"), // aizžņaugt

		// Single case rules.
		// Verb specific rules ordered by type and alphabetically by verb infinitive.
		SimpleRule.of("-auju, -auj, -auj, arī -aunu, -aun, -aun, pag. -āvu", ".*aut", 15,
				new String[] {"Darbības vārds", "Locīt kā \"aut\"", "Paralēlās formas"},
				null), //apaut
		SimpleRule.of("-gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu", ".*gult", 15,
				new String[] {"Darbības vārds", "Locīt kā \"gult\"", "Paralēlās formas"},
				null), //aizgult
		SimpleRule.of("-jaušu, -jaut, -jauš, pag. -jautu, arī -jaužu, -jaud, -jauž, pag. -jaudu", ".*jaust", 15,
				new String[] {"Darbības vārds", "Locīt kā \"jaust\"", "Paralēlās formas"},
				null), //apjaust
		SimpleRule.of("-jumju, -jum, -jumj, pag. -jūmu, arī -jumu", ".*jumt", 15,
				new String[] {"Darbības vārds", "Locīt kā \"jumt\"", "Paralēlās formas"},
				null), //aizjumt
		SimpleRule.of("-plešu, -plet, -pleš, pag. -pletu, arī -plētu", ".*plest", 15,
				new String[] {"Darbības vārds", "Locīt kā \"plest\"", "Paralēlās formas"},
				null), //aizplest
		SimpleRule.of("-skrienu, -skrien, -skrien, arī -skreju, -skrej, -skrej, pag. -skrēju", ".*skriet", 15,
				new String[] {"Darbības vārds", "Locīt kā \"skriet\"", "Paralēlās formas"},
				null), //aizskriet
		SimpleRule.of("-slienu, -slien, -slien, arī -sleju, -slej, -slej, pag. -sleju", ".*sliet", 15,
				new String[] {"Darbības vārds", "Locīt kā \"sliet\"", "Paralēlās formas"},
				null), //aizsliet
		SimpleRule.of("-spurdzu, -spurdz, -spurdz, arī -spurgst, pag. spurdzu", ".*spurgt", 15,
				new String[] {"Darbības vārds", "Locīt kā \"spurgt\"", "Paralēlās formas"},
				null), //aizsliet
		SimpleRule.of("-tupstu, -tupsti, -tupst, pag. -tupu", ".*tupt", 15,
				new String[]{"Darbības vārds", "Locīt kā \"tupt\"", "Paralēlās formas"},
				null), //aiztupt

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
		// A, B
		VerbRule.thirdConjDir("-brauku, -brauki,", "-brauka, pag. -braucīju", "braucīt", false), //apbraucīt
		// C, D
		VerbRule.thirdConjDir("-draudu, -draudi,", "-draud, pag. -draudēju", "draudēt", false), //apdraudēt
		VerbRule.thirdConjDir("-dziedu, -dziedi,", "-dzied, pag. -dziedāju", "dziedāt", false), //aizdziedāt
		// E, F, G
		VerbRule.thirdConjDir("-glūnu, -glūni,", "-glūn, pag. -glūnēju", "glūnēt", false), //apglūnēt
		VerbRule.thirdConjDir("-grabu, -grabi,", "-grab, pag. -grabēju", "grabēt", false), //aizgrabēt
		VerbRule.thirdConjDir("-guļu, -guli,", "-guļ, pag. -gulēju", "gulēt", true), //aizgulēt
		// H, I, J, K
		VerbRule.thirdConjDir("-klabu, -klabi,", "-klab, pag. -klabēju", "klabēt", false), //paklabēt, aizklabēt
		VerbRule.thirdConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimstēju", "klimstēt", false), //aizklimstēt
		VerbRule.thirdConjDir("-kustu, -kusti,", "-kust, pag. -kustēju", "kustēt", false), //aizkustēt
		VerbRule.thirdConjDir("-kūpu, -kūpi,", "-kūp, pag. -kūpēju", "kūpēt", false), //apkūpēt, aizkūpēt
		// L
		VerbRule.thirdConjDir("-loku, -loki,", "-loka, pag. -locīju", "locīt", true), //aizlocīt
		VerbRule.thirdConjDir("-lūru, -lūri,", "-lūr, pag. -lūrēju", "lūrēt", false), //aplūrēt
		// M
		VerbRule.thirdConjDir("-murdzu, -murdzi,", "-murdza, pag. -murdzīju", "murdzīt", false), //apmurdzīt
			// N, O, P
		VerbRule.thirdConjDir("-peldu, -peldi,", "-peld, pag. -peldēju", "peldēt", false), //aizpeldēt
		VerbRule.thirdConjDir("-pilu, -pili,", "-pil, pag. -pilēju", "pilēt", false), //appilēt, aizpilēt
		VerbRule.thirdConjDir("-precu, -preci,", "-prec, pag. -precēju", "precēt", false), //aizprecēt
		VerbRule.thirdConjDir("-putu, -puti,", "-put, pag. -putēju", "putēt", false), //aizputēt, apputēt
		// R, S
		VerbRule.thirdConjDir("-slaku, -slaki,", "-slaka, pag. -slacīju", "slacīt", true), //aizslacīt
		VerbRule.thirdConjDir("-slauku, -slauki,", "-slauka, pag. slaucīju", "slaucīt", true), //aizslaucīt
		VerbRule.thirdConjDir("-stāvu, -stāvi,", "-stāv, pag. -stāvēju", "stāvēt", false), //aizstāvēt
		// T
		VerbRule.thirdConjDir("-teku, -teci,", "-tek, pag. -tecēju", "tecēt", true), //aiztecēt
		VerbRule.thirdConjDir("-turu, -turi,", "-tur, pag. -turēju", "turēt", false), //aizturēt
		// U, V, Z

		// Single case rules.
		// Verb specific rules ordered by type and alphabetically by verb infinitive.
		SimpleRule.of(
				"-moku, -moki, -moka, arī -mocu, -moci, -moca, pag. -mocīju",
				".*mocīt", 17,
				new String[]{"Darbības vārds", "Paralēlās formas", "Tagadnes mija ir", "Tagadnes mijas nav"},
				null), //aizmocīt
		SimpleRule.of(
				"-murcu, -murci, -murca, retāk -murku, -murki, -murka, pag. -murcīju",
				".*murcīt", 17,
				new String[]{"Darbības vārds", "Paralēlās formas", "Tagadnes mija ir", "Tagadnes mijas nav"},
				null), //apmurcīt


		SimpleRule.of("-slīdu, -slīdi, -slīd, pag. -slīdēju, -slīdēji, -slīdēja (retāk -slīda, 1. konj.)",
				".*slīdēt", 17,
				new String[]{"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"}, null), // aizslīdēt

		// A, B
		ThirdPersVerbRule.thirdConjDir("-blākš, pag. -blākšēja", "blākšēt", false), //aizblākšēt
		ThirdPersVerbRule.thirdConjDir("-blākšķ, pag. -blākšķēja", "blākšķēt", false), //aizblākšķēt
		// C, Č
		ThirdPersVerbRule.thirdConjDir("-čab, pag. -čabēja", "čabēt", false), //aizčabēt
		ThirdPersVerbRule.thirdConjDir("-čaukst, pag. -čaukstēja", "čaukstēt", false), //aizčaukstēt
		// D
		ThirdPersVerbRule.thirdConjDir("-dārd, pag. -dārdēja", "dārdēt", false), //aizdārdēt
		ThirdPersVerbRule.thirdConjDir("-dimd, pag. -dimdēja", "dimdēt", false), //aizdimdēt
		ThirdPersVerbRule.thirdConjDir("-dip, pag. -dipēja", "dipēt", false), //aizdipēt
		ThirdPersVerbRule.thirdConjDir("-dun, pag. -dunēja", "dunēt", false), //aizdunēt
		ThirdPersVerbRule.thirdConjDir("-džinkst, pag. -džinkstēja", "džinkstēt", false), //aizdžinkstēt
		// E, F, G
		ThirdPersVerbRule.thirdConjDir("-gurkst, pag. -gurkstēja", "gurkstēt", false), //aizgurkstēt
		// H, I, J, K
		ThirdPersVerbRule.thirdConjDir("-klakst, pag. -klakstēja", "klakstēt", false), //aizklakstēt
		ThirdPersVerbRule.thirdConjDir("-klaudz, pag. -klaudzēja", "klaudzēt", false), //aizklaudzēt
		// L, M, N, Ņ
		ThirdPersVerbRule.thirdConjDir("-ņirb, pag. -ņirbēja", "ņirbēt", false), //aizņirbēt
		// O, P, R, S, T, U, V, Z

		// Parallel forms.
		SimpleRule.of("parasti 3. pers., -grand, pag. -grandēja (retāk -granda, 1. konj.)", ".*grandēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"},
				new String[] {"Parasti 3. personā"}), //aizgrandēt
		SimpleRule.of("parasti 3. pers., -gruzd, pag. -gruzdēja (retāk -gruzda, 1. konj.)", ".*gruzdēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"},
				new String[] {"Parasti 3. personā"}), //aizgruzdēt
		SimpleRule.of("parasti 3. pers., -mirdz, pag. -mirdzēja (retāk -mirdza, 1. konj.)", ".*mirdzēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"},
				new String[] {"Parasti 3. personā"}), //aizmirdzēt
		SimpleRule.of("parasti 3. pers., -pelē, arī -pel, pag. -pelēja", ".*pelēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"},
				new String[] {"Parasti 3. personā"}), //aizpelēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu
	 */
	public static final Rule[] directMultiConjVerb = {
		ComplexRule.of("-bedīju, -bedī, -bedī, arī -bedu, -bedi, -beda, pag. -bedīju", new Trio[]{
					Trio.of(".*bedīt", new Integer[] {16, 17},
							new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // apbedīt
		ComplexRule.of("-ceru, -ceri, -cer, retāk -cerēju, -cerē, -cerē, pag. -cerēju", new Trio[]{
					Trio.of(".*cerēt", new Integer[] {16, 17},
							new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // apcerēt
		ComplexRule.of("-dēstu, -dēsti, -dēsta, retāk -dēstīju, -dēsti, -dēstī, pag. -dēstīju", new Trio[]{
					Trio.of(".*dēstīt", new Integer[] {16, 17},
							new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // apdēstīt
		ComplexRule.of("-ķēzīju, -ķēzī, -ķēzī, arī -ķēzu, -ķēzi, -ķēza, pag. -ķēzīju", new Trio[]{
					Trio.of(".*ķēzīt", new Integer[] {16, 17},
							new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // apķēzīt
		ComplexRule.of("-rotu, -roti, -rota, arī -rotīju, -roti, -rotī, pag. -rotīju", new Trio[]{
					Trio.of(".*rotīt", new Integer[] {16, 17},
							new String[]{"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // aizrotīt
		ComplexRule.of("-sargāju, -sargā, -sargā, arī -sargu, -sargi, -sarga, pag. -sargāju", new Trio[]{
				 	Trio.of(".*sargāt", new Integer[] {16, 17},
							new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // aizsargāt
		ComplexRule.of("-vētīju, -vētī, -vētī, arī -vētu, -vēti, -vēta, pag. -vētīju", new Trio[]{
					Trio.of(".*vētīt", new Integer[] {16, 17},
							new String[] {"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // aizvētīt
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
		VerbRule.of("-aužos, -audies,", "-aužas, pag. -audos", "austies", 18,
				new String[] {"Locīt kā \"austies\" (kā audumam)"},
				null), //apausties
		VerbRule.of("-dzenos, -dzenies,", "-dzenas, pag. -dzinos", "dzīties", 18,
				new String[] {"Locīt kā \"dzīties\" (kā lopiem)"},
				null), //aizdzīties
		VerbRule.of("-iros, -iries,", "-iras, pag. -īros", "irties", 18,
				new String[] {"Locīt kā \"irties\" (kā ar airiem)"},
				null), //aizirties
		VerbRule.of("-minos, -minies,", "-minas, pag. -minos", "mīties", 18,
				new String[]{"Locīt kā \"mīties\" (kā pedāļus)"},
				null), //aizmīties
		VerbRule.of("-mijos, -mijies,", "-mijas, pag. -mijos", "mīties", 18,
				new String[]{"Locīt kā \"mīties\" (kā naudu)"},
				null), //apmīties

		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A , B
		VerbRule.firstConjRefl("-beros, -beries,", "-beras, pag. -bēros", "bērties"), //apbērties
		VerbRule.firstConjRefl("-brāžos, -brāzies,", "-brāžas, pag. -brāzos", "brāzties"), //aizbrāzties
		// C
		VerbRule.firstConjRefl("-ceļos, -celies,", "-ceļas, pag. -cēlos", "celties"), //apcelties
		VerbRule.firstConjRefl("-ciešos, -cieties,", "-ciešas, pag. -cietos", "ciesties"), //aizciesties
		VerbRule.firstConjRefl("-cērtos, -cērties,", "-cērtas, pag. -cirtos", "cirsties"), //aizcirsties
		// D
		VerbRule.firstConjRefl("-drāžos, -drāzies,", "-drāžas, pag. -drāzos", "drāzties"), //aizdrāzties
		VerbRule.firstConjRefl("-duros, -duries,", "-duras, pag. -dūros", "durties"), //aizdurties
		VerbRule.firstConjRefl("-dzeros, -dzeries,", "-dzeras, pag. -dzēros", "dzerties"), //apdzerties
		// E, F, G
		VerbRule.firstConjRefl("-gāžos, -gāzies,", "-gāžas, pag. -gāzos", "gāzties"), //apgāzties, aizgāzties
		VerbRule.firstConjRefl("-graužos, -grauzies,", "-graužas, pag. -grauzos", "grauzties"), //izgrauzties, aizgrauzties
		VerbRule.firstConjRefl("-griežos, -griezies,", "-griežas, pag. -griezos", "griezties"), //aizgriezties 1, 2
		VerbRule.firstConjRefl("-gūstos, -gūsties,", "-gūstas, pag. -guvos", "gūties"), //aizgūties
		// Ģ
		VerbRule.firstConjRefl("-ģērbjos, -ģērbies,", "-ģērbjas, pag. -ģērbos", "ģērbties"), //apģērbties
		// H, I
		VerbRule.firstConjRefl("-ejos, -ejos,", "-ietas, pag. -gājos", "ieties"), //apieties
		// J
		VerbRule.firstConjRefl("-jožos, -jozies,", "-jožas, pag. -jozos", "jozties"), //apjozties
		VerbRule.firstConjRefl("-jūdzos, -jūdzies,", "-jūdzas, pag. -jūdzos", "jūgties"), //aizjūgties
		// K
		VerbRule.firstConjRefl("-kampjos, -kampies,", "-kampjas, pag. -kampos", "kampties"), //apkampties
		VerbRule.firstConjRefl("-karos, -karies,", "-karas, pag. -kāros", "kārties"), //apkārties
		VerbRule.firstConjRefl("-klājos, -klājies,", "-klājas, pag. -klājos", "klāties"), //apklāties
		VerbRule.firstConjRefl("-kļaujos, -kļaujies,", "-kļaujas, pag. -kļāvos", "kļauties"), //apkļauties
		VerbRule.firstConjRefl("-kopjos, -kopies,", "-kopjas, pag. kopos", "kopties"), //apkopties
		VerbRule.firstConjRefl("-kraujos, -kraujies,", "-kraujas, pag. -krāvos", "krauties"), //apkrauties
		VerbRule.firstConjRefl("-krāpjos, -krāpies,", "-krāpjas, pag. -krāpos", "krāpties"), //apkrāpties
		VerbRule.firstConjRefl("-kuļos, -kulies,", "-kuļas, pag. -kūlos", "kulties"), //aizkulties
		// Ķ
		VerbRule.firstConjRefl("-ķēros, -ķeries,", "-ķeras, pag. -ķēros", "ķerties"), //aizķerties
		// L
		VerbRule.firstConjRefl("-laižos, -laidies,", "-laižas, pag. -laidos", "laisties"), //aizlaisties
		VerbRule.firstConjRefl("-laužos, -lauzies,", "-laužas, pag. -lauzos", "lauzties"), //aizlauzties
		VerbRule.firstConjRefl("-liedzos, -liedzies,", "-liedzas, pag. -liedzos", "liegties"), //aizliegties
		VerbRule.firstConjRefl("-liecos, -liecies,", "-liecas, pag. -liecos", "liekties"), //aizliekties
		VerbRule.firstConjRefl("-lējos, -lējies,", "-lejas, pag. -lējos", "lieties"), //aplieties
		VerbRule.firstConjRefl("-liekos, -liecies,", "-liekas, pag. -likos", "likties"), //aizlikties
		// M
		VerbRule.firstConjRefl("-maļos, -malies,", "-maļas, pag. -mālos", "malties"), //apmalties
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
		VerbRule.firstConjRefl("-rijos, -rijies,", "-rijas, pag. -rijos", "rīties"), //aizrīties
		// S
		VerbRule.firstConjRefl("-saucos, -saucies,", "-saucas, pag. -saucos", "saukties"), //aizsaukties
		VerbRule.firstConjRefl("-sedzos, -sedzies,", "-sedzas, pag. -sedzos", "segties"), //aizsegties
		VerbRule.firstConjRefl("-sienos, -sienies,", "-sienas, pag. -sējos", "sieties"), //aizsieties
		VerbRule.firstConjRefl("-sitos, -sities,", "-sitas, pag. -sitos", "sisties"), //aizsisties
		VerbRule.firstConjRefl("-slēpjos, -slēpies,", "-slēpjas, pag. -slēpos", "slēpties"), //aizslēpties
		VerbRule.firstConjRefl("-sliecos, -sliecies,", "-sliecas, pag. -sliecos", "sliekties"), //aizsliekties
		VerbRule.firstConjRefl("-sniedzos, -sniedzies,", "-sniedzas, pag. -sniedzos", "sniegties"), //aizsniegties
		VerbRule.firstConjRefl("-spiežos, -spiedies,", "-spiežas, pag. -spiedos", "spiesties"), //aizspiesties
		VerbRule.firstConjRefl("-spraucos, -spraucies,", "-spraucas, pag. -spraucos", "spraukties"), //aizspraukties
		VerbRule.firstConjRefl("-spraužos, -spraudies,", "-spraužas, pag. -spraudos", "sprausties"), //aizsprausties
		VerbRule.firstConjRefl("-spriežos, -spriedies,", "-spriežas, pag. -spriedos", "spriesties"), //aizspriesties
		VerbRule.firstConjRefl("-stājos, -stājies,", "-stājas, pag. -stājos", "stāties"), //aizstāties
		VerbRule.firstConjRefl("-steidzos, -steidzies,", "-steidzas, pag. -steidzos", "steigties"), //aizsteigties
		VerbRule.firstConjRefl("-stiepjos, -stiepies,", "-stiepjas, pag. -stiepos", "stiepties"), //aizstiepties
		VerbRule.firstConjRefl("-stumjos, -stumies,", "-stumjas, pag. -stūmos", "stumties"), //aizstumties
		VerbRule.firstConjRefl("-sviežos, -sviedies,", "-sviežas, pag. -sviedos", "sviesties"), //aizsviesties
		// Š
		VerbRule.firstConjRefl("-šaujos, -šaujies,", "-šaujas, pag. -savos", "šauties"), //aizšauties
		// T
		VerbRule.firstConjRefl("-tinos, -tinies,", "-tinas, pag. -tinos", "tīties"), //aiztīties
		VerbRule.firstConjRefl("-traucos, -traucies,", "-traucas, pag. -traucos", "traukties"), //aiztraukties
		// U
		VerbRule.firstConjRefl("-urbjos, -urbies,", "-urbjas, pag. -urbos", "urbties"), //aizurbties
		// V
		VerbRule.firstConjRefl("-vācos, -vācies,", "-vācas, pag. -vācos", "vākties"), //aizvākties
		VerbRule.firstConjRefl("-veļos, -velies,", "-veļas, pag. -vēlos", "velties"), //aizvelties
		VerbRule.firstConjRefl("-vēršos, -vērsies,", "-vēršas, pag. -vērsos", "vērsties"), //aizvērsties
		VerbRule.firstConjRefl("-velkos, -velcies,", "-velkas, pag. -vilkos", "vilkties"), //aizvilkties
		// Z
		VerbRule.firstConjRefl("-zogos, -zodzies,", "-zogas, pag. -zagos", "zagties"), //aizzagties

		// Single case rules.
		// Paralel forms.
		SimpleRule.of("-aujos, -aujies, -aujas, arī -aunos, -aunies, -aunas, pag. -āvos",
				".*auties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"auties\"", "Paralēlās formas"},
				null), //apauties
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
		SimpleRule.of("-slienos, -slienies, -slienas, arī -slejos, -slejies, -slejas, pag. -slējos",
				".*slieties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"slieties\"", "Paralēlās formas"},
				null), //aizslieties
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
		VerbRule.thirdConjRefl("-kustos, -kusties,", "-kustas, pag. -kustējos", "kustēties", false), //aizkustēties
		VerbRule.thirdConjRefl("-lāpos, -lāpies,", "-lāpās, pag. -lāpījos", "lāpīties", false), //aplāpīties
		VerbRule.thirdConjRefl("-peros, -peries,", "-peras, pag. -pēros", "pērties", false), //aizpērties
		VerbRule.thirdConjRefl("-precos, -precies,", "-precas, pag. -precējos", "precēties", false), //aizprecēties

		// Single case rules.
		ThirdPersVerbRule.thirdConjRefl("-lokās, pag. -locījās", "locīties", true), //aizlocīties

		// Parallel forms.
		ComplexRule.of("-sargājos, -sargājies, -sargājas, arī -sargos, -sargies, -sargās, pag. -sargājos", new Trio[]{
					Trio.of(".*sargāties", new Integer[]{19, 20},
							new String[]{"Darbības vārds", "Paralēlās formas", "Tagadnes mijas nav"})},
				null), // aizsargāties
		SimpleRule.of(
				"-mokos, -mokies, -mokās, arī -mocos, -mocies, -mocās, pag. -mocījos",
				".*mocīties", 20,
				new String[]{"Darbības vārds", "Paralēlās formas", "Tagadnes mija ir", "Tagadnes mijas nav"},
				null) //aizmocīties
	};
}
