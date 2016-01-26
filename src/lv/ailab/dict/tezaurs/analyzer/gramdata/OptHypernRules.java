package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.*;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Values;
import lv.ailab.dict.utils.Tuple;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * Gram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar Rule.applyOptHyphens().
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā, vai konkrēti
 * vārdi.
 *
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
		BaseRule.noun("-acs, dsk. ģen. -acu, s.", ".*acs", 11, null,
				new Tuple[]{Features.GENDER__FEM}), //uzacs, acs
		BaseRule.noun("-krāsns, dsk. ģen. -krāšņu, s.", ".*krāsns", 11, null,
				new Tuple[]{Features.GENDER__FEM}), //aizkrāsns
		BaseRule.noun("-valsts, dsk. ģen. -valstu, s.", ".*valsts", 11, null,
				new Tuple[]{Features.GENDER__FEM}), //agrārvalsts

		/* Paradigm 25: Pronouns
		 */
		BaseRule.of("ģen. -kā, dat. -kam, akuz., instr. -ko", ".*kas", 25,
				new Tuple[]{Features.POS__PRONOUN, Tuple
						.of(Keys.INFLECT_AS, "\"kas\"")},
				null), //daudzkas
	};
	/**
	 * Paradigm 9: Lietvārds 5. deklinācija -e
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] fifthDeclNoun = {
		BaseRule.fifthDeclStd("-upes, dsk. ģen. -upju", ".*upe"), //upe
	};
	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final Rule[] secondDeclNoun = {
			BaseRule.secondDeclStd("-tēta, v.", ".*tētis"), //tētis
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final Rule[] directFirstConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Netoteiksmes homoformas.
		FirstConjRule.directHomof("-aužu, -aud,", "-auž, pag. -audu", "aust",
				"\"aust\" (audumu)"), //aizaust 2
		FirstConjRule.directHomof("-dedzu, -dedz,", "-dedz, pag. -dedzu", "degt",
				"\"degt\" (kādu citu)"), //aizdegt 1
		FirstConjRule.directHomof("-degu, -dedz,", "-deg, pag. -degu", "degt",
				"\"degt\" (pašam)"), //apdegt, aizdegt 2
		FirstConjRule.directHomof("-dzenu, -dzen,", "-dzen, pag. -dzinu", "dzīt",
				"\"dzīt\" (lopus)"), //aizdzīt 1
		FirstConjRule.directHomof("-iru, -ir,", "-ir, pag. -īru", "irt",
				"\"irt\" (ar airiem)"), //aizirt 1
		FirstConjRule.directHomof("-lienu, -lien,", "-lien, pag. -līdu",
				"līst", "\"līst\" (zem galda)"), //aizlīst, ielīst 1
		FirstConjRule.directHomof("-līžu, -līd,", "-līž, pag. -līdu", "līst",
				"\"līst\" (līdumu)"), //ielīst 2
		FirstConjRule.directHomof("-mītu, -mīti,", "-mīt, pag. -mitu", "mist",
				"\"mist\" (mitināties)"), //iemist, izmist 2
		FirstConjRule.directHomof("-mistu, -misti,", "-mist, pag. -misu", "mist",
				"\"mist\" (krist izmisumā)"), //izmist 1
		FirstConjRule.directHomof("-minu, -min,", "-min, pag. -minu", "mīt",
				"\"mīt\" (pedāļus)"), //aizmīt 1
		FirstConjRule.directHomof("-miju, -mij,", "-mij, pag. -miju", "mīt",
				"\"mīt\" (mainīt naudu)"), //aizmīt 2
		FirstConjRule.directHomof("-tieku, -tiec,", "-tiek, pag. -tiku", "tikt",
				"\"tikt\" (nokļūt kaut kur)"), //aiztikt 1, 2
		FirstConjRule.directHomof("-tīku, -tīc,", "-tīk, pag. -tiku", "tikt",
				"\"tikt\" (patikt kādam)"), //patikt
		// Izņēmuma izņēmums :/
		FirstConjRule.directHomof("-patīku, -patīc,", "-patīk, pag. -patiku", "patikt",
				"\"tikt\" (patikt kādam)"), //patikt
		FirstConjRule.directHomof("-vēršu, -vērs,", "-vērš, pag. -vērsu", "vērst",
				"\"vērst\" (mainīt virzienu)"), //aizvērst, izvērst 1
		FirstConjRule.directHomof("-vēršu, -vērs,", "-vērš, pag. -vērtu", "vērst",
				"\"vērst\" (mainīt būtību)"), //izvērst 2
		//FirstConjRule.directHomof("-virstu, -virsti,", "-virst, pag. -virtu", "virst",
		//		"\"virst\" (izvirtulis)"), //izvirst 1
			// Virst/virt ir murgs, jāsagaida, ko teiks L&B


			// Paralēlās formas.
		FirstConjRule.directAllPersParallel(
				"-auju, -auj, -auj, arī -aunu, -aun, -aun, pag. -āvu", "aut"), //apaut
		FirstConjRule.directAllPersParallel(
				"-gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu", "gult"), //aizgult
		FirstConjRule.directAllPersParallel(
				"-jaušu, -jaut, -jauš, pag. -jautu, arī -jaužu, -jaud, -jauž, pag. -jaudu", "jaust"), //apjaust
		FirstConjRule.directAllPersParallel(
				"-jumju, -jum, -jumj, pag. -jūmu, arī -jumu", "jumt"), //aizjumt
		FirstConjRule.directAllPersParallel(
			"-kaistu, -kaisti, -kaist, pag. -kaisu, retāk -kaitu", "kaist"), //iekaist
		FirstConjRule.directAllPersParallel(
				"-kaistu, -kaisti, -kaist, pag. -kaisu, arī -kaitu", "kaist"), //nokaist
		FirstConjRule.directAllPersParallel(
				"-kūpu, -kūpi, -kūp, arī -kūpstu, -kūpsti, -kūpst, pag. -kūpu", "kūpt"), //nokūpt
		FirstConjRule.directAllPersParallel(
				"-kvēpstu, -kvēpsti, -kvēpst, arī -kvēpu, -kvēp, -kvēp, pag. -kvēpu", "kvēpt"), //nokvēpt
		FirstConjRule.directAllPersParallel(
				"-meju, -mej, -mej, pag. -mēju, arī -mienu, -mien, -mien, pag. -mēju", "miet"), //iemiet
		FirstConjRule.directAllPersParallel(
				"-plešu, -plet, -pleš, pag. -pletu, arī -plētu", "plest"), //aizplest
		FirstConjRule.directAllPersParallel(
				"-riešu, -ries, -rieš, pag. -rietu, arī -riesu", "riest"), //ieriest 2
		FirstConjRule.directAllPersParallel(
				"-skārstu, -skārsti, -skārst, arī -skāršu, -skārt, -skārš, pag. -skārtu", "skārst"), //apskārst
		FirstConjRule.directAllPersParallel(
				"-skrienu, -skrien, -skrien, arī -skreju, -skrej, -skrej, pag. -skrēju", "skriet"), //aizskriet
		FirstConjRule.directAllPersParallel(
				"-slapstu, -slapsti, -slapst, retāk -slopu, -slopi, -slop, pag. -slapu", "slapt"), //apslapt
		FirstConjRule.directAllPersParallel(
				"-slienu, -slien, -slien, arī -sleju, -slej, -slej, pag. -slēju", "sliet"), //aizsliet
		FirstConjRule.directAllPersParallel(
				"-spurdzu, -spurdz, -spurdz, arī -spurgst, pag. -spurdzu", "spurgt"), //aizspurgt
		FirstConjRule.directAllPersParallel(
				"-spurdzu, -spurdz, -spurdz, pag. -spurdzu, retāk -spurgstu, -spurgsti, -spurgst, pag. -spurgu", "spurgt"), //iespurgt
		FirstConjRule.directAllPersParallel(
				"-spurstu, -spursti, -spurst, pag. -spuru, arī -spūru", "spurt"), //izspurt
		FirstConjRule.directAllPersParallel(
				"-stīdzu, -stīdz, -stīdz, pag. -stīdzu, retāk -stīgstu, -stīgsti, -stīgst, pag. -stīgu", "stīgt"), //izstīgt
		FirstConjRule.directAllPersParallel(
				"-strēdzu, -strēdz, -strēdz, pag. -strēdzu, arī -strēgstu, -strēgsti, -strēgst, pag. -strēgu", "strēgt"), //iestrēgt
		FirstConjRule.directAllPersParallel(
				"-tilpstu, -tilpsti, -tilpst, retāk -telpu, telpi, -telp, pag. -tilpu", "tilpt"), //ietilpt


		// Izņēmums.
		FirstConjRule.of("-pārdodu, -pārdod,", "-pārdod, pag. -pārdevu", "pārdot", 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"dot\"")}, null,
				new String[]{"do"}, new String[]{"dod"}, new String[]{"dev"}), //izpārdot
		FirstConjRule.of("-palieku, -paliec,", "-paliek, pag. -paliku", "palikt", 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"likt\"")}, null,
				new String[]{"lik"}, new String[]{"liek"}, new String[]{"lik"}), //izpalikt
		FirstConjRule.of("-pazīstu, -pazīsti,", "-pazīst, pag. -pazinu", "pazīt", 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"zīt\"")}, null,
				new String[]{"zī"}, new String[]{"zīst"}, new String[]{"zin"}), //atpazīt

		// Standartizētie.
		// A
		FirstConjRule.direct("-aru, -ar,", "-ar, pag. -aru", "art"), //aizart
		FirstConjRule.direct("-augu, -audz,", "-aug, pag. -augu", "augt"), //ieaugt, aizaugt
		// B
		FirstConjRule.direct("-baru, -bar,", "-bar, pag. -bāru", "bārt"), //iebārt
		FirstConjRule.direct("-bāžu, -bāz,", "-bāž, pag. -bāzu", "bāzt"), //aizbāzt
		FirstConjRule.direct("-beidzu, -beidz,", "-beidz, pag. -beidzu", "beigt"), //izbeigt
		FirstConjRule.direct("-belžu, -belz,", "-belž, pag. -belzu", "belzt"), //iebelzt
		FirstConjRule.direct("-beržu, -berz,", "-berž, pag. -berzu", "berzt"), //atberzt
		FirstConjRule.direct("-bēgu, -bēdz,", "-bēg, pag. -bēgu", "bēgt"), //aizbēgt
		FirstConjRule.direct("-beru, -ber,", "-ber, pag. -bēru", "bērt"), //aizbērt
		FirstConjRule.direct("-bilstu, -bilsti,", "-bilst, pag. -bildu", "bilst"), //aizbilst
		FirstConjRule.direct("-birstu, -birsti,", "-birst, pag. -biru", "birt"), //apbirt, aizbirt
		FirstConjRule.direct("-bliežu, -bliez,", "-bliež, pag. -bliezu", "bliezt"), //iebliezt
		FirstConjRule.direct("-blīstu, -blīsti,", "-blīst, pag. -blīdu", "blīst"), //izblīst
		FirstConjRule.direct("-blīstu, -blīsti,", "-blīst, pag. -blīzu", "blīzt"), //izblīzt
		FirstConjRule.direct("-bļauju, -bļauj,", "-bļauj, pag. -bļāvu", "bļaut"), //atbļaut
		FirstConjRule.direct("-braucu, -brauc,", "-brauc, pag. -braucu", "braukt"), //aizbraukt
		FirstConjRule.direct("-brāžu, -brāz,", "-brāž, pag. -brāzu", "brāzt"), //aizbrāzt
		FirstConjRule.direct("-brēcu, -brēc,", "-brēc, pag. -brēcu", "brēkt"), //atbrēkt
		FirstConjRule.direct("-briestu, -briesti,", "-briest, pag. -briedu", "briest"), //nobriest
		FirstConjRule.direct("-brienu, -brien,", "-brien, pag. -bridu", "brist"), //aizbrist
		FirstConjRule.direct("-brūku, -brūc,", "-brūk, pag. -bruku", "brukt"), //iebrukt
		FirstConjRule.direct("-buru, -bur,", "-bur, pag. -būru", "burt"), //apburt
		// C
		FirstConjRule.direct("-ceļu, -cel,", "-ceļ, pag. -cēlu", "celt"), //aizcelt
		FirstConjRule.direct("-cepu, -cep,", "-cep, pag. -cepu", "cept"), //apcept
		FirstConjRule.direct("-ciešu, -ciet,", "-cieš, pag. -cietu", "ciest"), //izciest
		FirstConjRule.direct("-cērpu, -cērp,", "-cērp, pag. -cirpu", "cirpt"), //apcirpt
		FirstConjRule.direct("-cērtu, -cērt,", "-cērt, pag. -cirtu", "cirst"), //aizcirst
		// Č
		FirstConjRule.direct("-čiepju, -čiep,", "-čiepj, pag. -čiepu", "čiept"), //izčiept
		// D
		FirstConjRule.direct("-dēju, -dēj,", "-dēj, pag. -dēju", "dēt"), //izdēt
		FirstConjRule.direct("-diebju, -dieb,", "-diebj, pag. -diebu", "diebt"), //aizdiebt
		FirstConjRule.direct("-diedzu, -diedz,", "-diedz, pag. -diedzu", "diegt"), //aizdiegt 1,2
		FirstConjRule.direct("-deju, -dej,", "-dej, pag. -deju", "diet"), //izdiet
		FirstConjRule.direct("-dilstu, -dilsti,", "-dilst, pag. -dilu", "dilt"), //apdilt
		FirstConjRule.direct("-dīcu, -dīc,", "-dīc, pag. -dīcu", "dīkt"), //idīkt
		FirstConjRule.direct("-dobju, -dob,", "-dobj, pag. -dobu", "dobt"), //iedobt
		FirstConjRule.direct("-dodu, -dod,", "-dod, pag. -devu", "dot"), //aizdot
		FirstConjRule.direct("-drāžu, -drāz,", "-drāž, pag. -drāzu", "drāzt"), //aizdrāzt
		FirstConjRule.direct("-duru, -dur,", "-dur, pag. -dūru", "durt"), //aizdurt
		FirstConjRule.direct("-dūcu, -dūc,", "-dūc, pag. -dūcu", "dūkt"), //aizdūkt
		FirstConjRule.direct("-dvešu, -dves,", "-dveš, pag. -dvesu", "dvest"), //apdvest
		FirstConjRule.direct("-dzeļu, -dzel,", "-dzeļ, pag. -dzēlu", "dzelt"), //atdzelt, aizdzelt
		FirstConjRule.direct("-dzeru, -dzer,", "-dzer, pag. -dzēru", "dzert"), //aizdzert
		FirstConjRule.direct("-dzēšu, -dzēs,", "-dzēš, pag. -dzēsu", "dzēst"), //apdzēst
		FirstConjRule.direct("-dzimstu, -dzimsti,", "-dzimst, pag. -dzimu", "dzimt"), //atdzimt
		FirstConjRule.direct("-dzirstu, -dzirsti,", "-dzirst, pag. -dzirdu", "dzirst"), //izdzirst
		FirstConjRule.direct("-dziestu, -dziesti,", "-dziest, pag. -dzisu", "dzist"), //atdzist
		// E
		FirstConjRule.direct("-elšu, -els,", "-elš, pag. -elsu", "elst"), //atelst
		// Ē
		FirstConjRule.direct("-ēdu, -ēd,", "-ēd, pag. -ēdu", "ēst"), //aizēst
		// F, G
		FirstConjRule.direct("-gaistu, -gaisti,", "-gaist, pag. -gaisu", "gaist"), //izgaist
		FirstConjRule.direct("-gaužu, -gaud,", "-gauž, pag. -gaudu", "gaust"), //nogaust
		FirstConjRule.direct("-gārdzu, -gārdz,", "-gārdz, pag. -gārdzu", "gārgt"), //izgārgt
		FirstConjRule.direct("-gāžu, -gāz,", "-gāž, pag. -gāzu", "gāzt"), //aizgāzt
		FirstConjRule.direct("-glaužu, -glaud,", "-glauž, pag. -glaudu", "glaust"), //aizglaust
		FirstConjRule.direct("-glābju, -glāb,", "-glābj, pag. -glābu", "glābt"), //izglābt
		FirstConjRule.direct("-grauju, -grauj,", "-grauj, pag. -grāvu", "graut"), //iegraut
		FirstConjRule.direct("-graužu, -grauz,", "-grauž, pag. -grauzu", "grauzt"), //aizgrauzt
		FirstConjRule.direct("-grābju, -grāb,", "-grābj, pag. -grābu", "grābt"), //aizgrābt
		FirstConjRule.direct("-grebju, -greb,", "-grebj, pag. -grebu", "grebt"), //apgrebt
		FirstConjRule.direct("-griežu, -griez,", "-griež, pag. -griezu", "griezt"), //aizgriezt 1, 2
		FirstConjRule.direct("-grimstu, -grimsti,", "-grimst, pag. -grimu", "grimt"), //atgrimt, aizgrimt
		FirstConjRule.direct("-grūžu, -grūd,", "-grūž, pag. -grūdu", "grūst"), //aizgrūst
		FirstConjRule.direct("-grūstu, -grūsti,", "-grūst, pag. -gruvu", "grūt"), //iegrūt
		FirstConjRule.direct("-gumstu, -gumsti,", "-gumst, pag. -gumu", "gumt"), //iegumt
		FirstConjRule.direct("-gurstu, -gursti,", "-gurst, pag. -guru", "gurt"), //apgurt
		FirstConjRule.direct("-gūstu, -gūsti,", "-gūst, pag. -guvu", "gūt"), //aizgūt
		FirstConjRule.direct("-gvelžu, -gvelz,", "-gvelž, pag. -gvelzu", "gvelzt"), //izgvelzt
		// Ģ
		FirstConjRule.direct("-ģērbju, -ģērb,", "-ģērbj, pag. -ģērbu", "ģērbt"), //apģist
		FirstConjRule.direct("-ģinstu, -ģinsti,", "-ģinst, pag. -ģindu", "ģinst"), //izģinst
		FirstConjRule.direct("-ģiedu, -ģied,", "-ģied, pag. -ģidu", "ģist"), //apģist
		FirstConjRule.direct("-ģībstu, -ģībsti,", "-ģībst, pag. -ģību", "ģībt"), //noģībt
		// H, I
		FirstConjRule.direct("-eju, -ej,", "-iet, pag. -gāju", "iet"), //apiet
		FirstConjRule.direct("-iežu, -iez,", "-iež, pag. -iezu", "iezt"), //atiezt
		// J
		FirstConjRule.direct("-jaucu, -jauc,", "-jauc, pag. -jaucu", "jaukt"), //iejaukt
		FirstConjRule.direct("-jauju, -jauj,", "-jauj, pag. -jāvu", "jaut"), //iejaut
		FirstConjRule.direct("-jāju, -jāj,", "-jāj, pag. -jāju", "jāt"), //aizjāt
		FirstConjRule.direct("-jēdzu, -jēdz,", "-jēdz, pag. -jēdzu", "jēgt"), //apjēgt
		FirstConjRule.direct("-jožu, -joz,", "-jož, pag. -jozu", "jozt"), //aizjozt 1, 2
		FirstConjRule.direct("-jūku, -jūc,", "-jūk, pag. -juku", "jukt"), //apjukt
		FirstConjRule.direct("-jūtu, -jūti,", "-jūt, pag. -jutu", "just"), //izjust
		FirstConjRule.direct("-jūdzu, -jūdz,", "-jūdz, pag. -jūdzu", "jūgt"), //aizjūgt
		// K
		FirstConjRule.direct("-kalstu, -kalsti,", "-kalst, pag. -kaltu", "kalst"), //aizkalst
		FirstConjRule.direct("-kaļu, -kal,", "-kaļ, pag. -kalu", "kalt"), //apkalt
		FirstConjRule.direct("-kampju, -kamp,", "-kampj, pag. -kampu", "kampt"), //apkampt
		FirstConjRule.direct("-karstu, -karsti,", "-karst, pag. -karsu", "karst"), //apkarst
		FirstConjRule.direct("-kašu, -kas,", "-kaš, pag. -kasu", "kast"), //iekast
		FirstConjRule.direct("-kaucu, -kauc,", "-kauc, pag. -kaucu", "kaukt"), //izkaukt, aizkaukt
		FirstConjRule.direct("-kauju, -kauj,", "-kauj, pag. -kāvu", "kaut"), //apkaut
		FirstConjRule.direct("-kāpju, -kāp,", "-kāpj, pag. -kāpu", "kāpt"), //aizkāpt
		FirstConjRule.direct("-kāršu, -kārs,", "-kārš, pag. -kārsu", "kārst"), //izkārt
		FirstConjRule.direct("-karu, -kar,", "-kar, pag. -kāru", "kārt"), //aizkārt
		FirstConjRule.direct("-kāšu, -kās,", "-kāš, pag. -kāsu", "kāst"), //iekāst
		FirstConjRule.direct("-klāju, -klāj,", "-klāj, pag. -klāju", "klāt"), //apklāt
		FirstConjRule.direct("-kliedzu, -kliedz,", "-kliedz, pag. -kliedzu", "kliegt"), //aizkliegt
		FirstConjRule.direct("-kliežu, -klied,", "-kliež, pag. -kliedu", "kliest"), //izkliest
		FirstConjRule.direct("-klimstu, -klimsti,", "-klimst, pag. -klimtu", "klimst"), //aizklimst
		FirstConjRule.direct("-klīstu, -klīsti,", "-klīst, pag. -klīdu", "klīst"), //aizklīst
		FirstConjRule.direct("-klūpu, -klūpi,", "-klūp, pag. -klupu", "klupt"), //ieklupt
		FirstConjRule.direct("-klustu, -klusti,", "-klust, pag. -klusu", "klust"), //apklust
		FirstConjRule.direct("-kļauju, -kļauj,", "-kļauj, pag. -kļāvu", "kļaut"), //apkļaut
		FirstConjRule.direct("-kļūstu, -kļūsti,", "-kļūst, pag. -kļuvu", "kļūt"), //aizkļūt
		FirstConjRule.direct("-knābju, -knāb,", "-knābj, pag. -knābu", "knābt"), //aizknābt
		FirstConjRule.direct("-kniebju, -knieb,", "-kniebj, pag. -kniebu", "kniebt"), //apkniebt
		FirstConjRule.direct("-knūpu, -knūpi,", "-knūp, pag. -knupu", "knupt"), //ieknupt
		FirstConjRule.direct("-kņūpu, -kņūpi,", "-kņūp, pag. -kņupu", "kņupt"), //iekņupt
		FirstConjRule.direct("-kopju, -kop,", "-kopj, pag. -kopu", "kopt"), //apkopt
		FirstConjRule.direct("-kožu, -kod,", "-kož, pag. -kodu", "kost"), //aizkost
		FirstConjRule.direct("-krācu, -krāc,", "-krāc, pag. -krācu", "krākt"), //nokrākt
		FirstConjRule.direct("-krāpju, -krāp,", "-krāpj, pag. -krāpu", "krāpt"), //aizkrāpt
		FirstConjRule.direct("-krāju, -krāj,", "-krāj, pag. -krāju", "krāt"), //iekrāt
		FirstConjRule.direct("-krauju, -krauj,", "-krauj, pag. -krāvu", "kraut"), //aizkraut
		FirstConjRule.direct("-kremtu, -kremt,", "-kremt, pag. -krimtu", "krimst"), //apkrimst
		FirstConjRule.direct("-krītu, -krīti,", "-krīt, pag. -kritu", "krist"), //aizkrist
		FirstConjRule.direct("-kuļu, -kul,", "-kuļ, pag. -kūlu", "kult"), //apkult
		FirstConjRule.direct("-kuru, -kur,", "-kur, pag. -kūru", "kurt"), //aizkurt
		FirstConjRule.direct("-kūstu, -kūsti,", "-kūst, pag. -kusu", "kust"), //aizkust
		FirstConjRule.direct("-kūpu, -kūpi,", "-kūp, pag. -kūpu", "kūpt"), //apkūpt
		FirstConjRule.direct("-kvēpstu, -kvēpsti,", "-kvēpst, pag. -kvēpu", "kvēpt"), //apkvēpt, aizkvēpt
		// Ķ
		FirstConjRule.direct("-ķepu, -ķep,", "-ķep, pag. -ķepu", "ķept"), //apķept, aizķept
		FirstConjRule.direct("-ķeru, -ķer,", "-ķer, pag. -ķēru", "ķert"), //aizķert
		FirstConjRule.direct("-ķērcu, -ķērc,", "-ķērc, pag. -ķērcu", "ķērkt"), //atķērkt
		// L
		FirstConjRule.direct("-labstu, -labsti,", "-labst, pag. -labu", "labt"), //atlabt
		FirstConjRule.direct("-laižu, -laid,", "-laiž, pag. -laidu", "laist"), //aizlaist
		FirstConjRule.direct("-loku, -loc,", "-lok, pag. -laku", "lakt"), //ielakt
		FirstConjRule.direct("-laužu, -lauz,", "-lauž, pag. -lauzu", "lauzt"), //aizlauzt
		FirstConjRule.direct("-lemju, -lem,", "-lemj, pag. -lēmu", "lemt"), //izlemt
		FirstConjRule.direct("-lencu, -lenc,", "-lenc, pag. -lencu", "lenkt"), //aplenkt
		FirstConjRule.direct("-lepu, -lep,", "-lep, pag. -lepu", "lept"), //izlept
		FirstConjRule.direct("-lecu, -lec,", "-lec, pag. -lēcu", "lēkt"), //aizlēkt
		FirstConjRule.direct("-lēšu, -lēs,", "-lēš, pag. -lēsu", "lēst"), //aplēst
		FirstConjRule.direct("-liedzu, -liedz,", "-liedz, pag. -liedzu", "liegt"), //aizliegt
		FirstConjRule.direct("-liecu, -liec,", "-liec, pag. -liecu", "liekt"), //aizliekt
		FirstConjRule.direct("-leju, -lej,", "-lej, pag. -lēju", "liet"), //aizliet
		FirstConjRule.direct("-lieku, -liec,", "-liek, pag. -liku", "likt"), //aizlikt
		FirstConjRule.direct("-līpu, -līpi,", "-līp, pag. -lipu", "lipt"), //aplipt, aizlipt
		FirstConjRule.direct("-līgstu, -līgsti,", "-līgst, pag. -līgu", "līgt"), //atlīgt
		FirstConjRule.direct("-līkstu, -līksti,", "-līkst, pag. -līku", "līkt"), //nolīkt, aizlīkt
		FirstConjRule.direct("-līstu, -līsti,", "-līst, pag. -liju", "līt"), //aplīt, aizlīt
		FirstConjRule.direct("-lobju, -lob,", "-lobj, pag. -lobu", "lobt"), //aizlobt
		FirstConjRule.direct("-lūdzu, -lūdz,", "-lūdz, pag. -lūdzu", "lūgt"), //aizlūgt
		FirstConjRule.direct("-lūstu, -lūsti,", "-lūst, pag. -lūzu", "lūzt"), //aizlūzt, aizlūzt
		// Ļ
		FirstConjRule.direct("-ļauju, -ļauj,", "-ļauj, pag. -ļāvu", "ļaut"), //atļaut
		FirstConjRule.direct("-ļimstu, -ļimsti,", "-ļimst, pag. -ļimu", "ļimt"), //noļimt
		// M
		FirstConjRule.direct("-maigstu, -maigsti,", "-maigst, pag. -maigu", "maigt"), //atmaigt
		FirstConjRule.direct("-maļu, -mal,", "-maļ, pag. -malu", "malt"), //apmalt
		FirstConjRule.direct("-maucu, -mauc,", "-mauc, pag. -maucu", "maukt"), //iemaukt
		FirstConjRule.direct("-mauju, -mauj,", "-mauj, pag. -māvu", "maut"), //iemaut
		FirstConjRule.direct("-mācu, -māc,", "-māc, pag. -mācu", "mākt"), //nomākt
		FirstConjRule.direct("-māju, -māj,", "-māj, pag. -māju", "māt"), //atmāt
		FirstConjRule.direct("-melšu, -mels,", "-melš, pag. -melsu", "melst"), //iemelst
		FirstConjRule.direct("-metu, -met,", "-met, pag. -metu", "mest"), //aizmest
		FirstConjRule.direct("-mērcu, -mērc,", "-mērc, pag. -mērcu", "mērkt"), //iemērkt
		FirstConjRule.direct("-mēžu, -mēz,", "-mēž, pag. -mēzu", "mēzt"), //aizmēzt
		FirstConjRule.direct("-miedzu, -miedz,", "-miedz, pag. -miedzu", "miegt"), //aizmiegt
		FirstConjRule.direct("-miegu, -miedz,", "-mieg, pag. -migu", "migt"), //aizmigt
		FirstConjRule.direct("-mirkstu, -mirksti,", "-mirkst, pag. -mirku", "mirkt"), //imirkt
		FirstConjRule.direct("-mirstu, -mirsti,", "-mirst, pag. -mirsu", "mirst"), //aizmirst
		FirstConjRule.direct("-mirstu, -mirsti,", "-mirst, pag. -miru", "mirt"), //nomirt
		FirstConjRule.direct("-mūku, -mūc,", "-mūk, pag. -muku", "mukt"), //aizmukt
		FirstConjRule.direct("-mulstu, -mulsti,", "-mulst, pag. -mulsu", "mulst"), //apmulst
		// N
		FirstConjRule.direct("-nāku, -nāc,", "-nāk, pag. -nācu", "nākt"), //apnākt
		FirstConjRule.direct("-nesu, -nes,", "-nes, pag. -nesu", "nest"), //aiznest
		FirstConjRule.direct("-nirstu, -nirsti,", "-nirst, pag. -niru", "nirt"), //aiznirt
		FirstConjRule.direct("-nīkstu, -nīksti,", "-nīkst, pag. -nīku", "nīkt"), //iznīkt
		FirstConjRule.direct("-nīstu, -nīsti,", "-nīst, pag. -nīdu", "nīst"), //ienīst
		// Ņ
		FirstConjRule.direct("-ņemu, -ņem,", "-ņem, pag. -ņēmu", "ņemt"), //aizņemt
		FirstConjRule.direct("-ņirdzu, -ņirdz,", "-ņirdz, pag. -ņirdzu", "ņirgt"), //apņirgt
		// O
		FirstConjRule.direct("-ožu, -od,", "-ož, pag. -odu", "ost"), //apost
		// P
		FirstConjRule.direct("-pampstu, -pampsti,", "-pampst, pag. -pampu", "pampt"), //aizpampt
		FirstConjRule.direct("-paužu, -paud,", "-pauž, pag. -paudu", "paust"), //iepaust
		FirstConjRule.direct("-peļu, -pel,", "-peļ, pag. -pēlu", "pelt"), //nopelt
		FirstConjRule.direct("-peru, -per,", "-per, pag. -pēru", "pērt"), //appērt
		FirstConjRule.direct("-pērku, -pērc,", "-pērk, pag. -pirku", "pirkt"), //appirkt
		FirstConjRule.direct("-pīkstu, -pīksti,", "-pīkst, pag. -pīku", "pīkt"), //iepīkt
		FirstConjRule.direct("-pinu, -pin,", "-pin, pag. -pinu", "pīt"), //aizpīt
		FirstConjRule.direct("-ploku, -ploc,", "-plok, pag. -plaku", "plakt"), //aizplakt
		FirstConjRule.direct("-plaukstu, -plauksti,", "-plaukst, pag. -plauku", "plaukt"), //atplaukt, aizplaukt
		FirstConjRule.direct("-plāju, -plāj,", "-plāj, pag. -plāju", "plāt"), //izplāt
		FirstConjRule.direct("-plēšu, -plēs,", "-plēš, pag. -plēsu", "plēst"), //aizplēst
		FirstConjRule.direct("-plīstu, -plīsti,", "-plīst, pag. -plīsu", "plīst"), //applīst, aizplīst
		FirstConjRule.direct("-plūcu, -plūc,", "-plūc, pag. -plūcu", "plūkt"), //aizplūkt
		FirstConjRule.direct("-plūstu, -plūsti,", "-plūst, pag. -plūdu", "plūst"), //applūst, aizplūst
		FirstConjRule.direct("-pļauju, -pļauj,", "-pļauj, pag. -pļāvu", "pļaut"), //aizpļaut
		FirstConjRule.direct("-protu, -proti,", "-prot, pag. -pratu", "prast"), //izprast
		FirstConjRule.direct("-pūšu, -pūt,", "-pūš, pag. -pūtu", "pūst"), //aizpūst
		FirstConjRule.direct("-pūstu, -pūsti,", "-pūst, pag. -puvu", "pūt"), //aizpūt, pūt
		// R
		FirstConjRule.direct("-roku, -roc,", "-rok, pag. -raku", "rakt"), //aizrakt
		FirstConjRule.direct("-rodu, -rodi,", "-rod, pag. -radu", "rast"), //aprast
		FirstConjRule.direct("-raucu, -rauc,", "-rauc, pag. -raucu", "raukt"), //apraukt
		FirstConjRule.direct("-raušu, -raus,", "-rauš, pag. -rausu", "raust"), //aizraust
		FirstConjRule.direct("-rauju, -rauj,", "-rauj, pag. -rāvu", "raut"), //aizraut
		FirstConjRule.direct("-rāju, -rāj,", "-rāj, pag. -rāju", "rāt"), //aprāt
		FirstConjRule.direct("-reibstu, -reibsti,", "-reibst, pag. -reibu", "reibt"), //apreibt
		FirstConjRule.direct("-rēcu, -rēc,", "-rēc, pag. -rēcu", "rēkt"), //atrēkt
		FirstConjRule.direct("-riebju, -rieb,", "-riebj, pag. -riebu", "riebt"), //aizriebt
		FirstConjRule.direct("-reju, -rej,", "-rej, pag. -rēju", "riet"), //apriet
		FirstConjRule.direct("-riežu, -riez,", "-riež, pag. -riezu", "riezt"), //ieriezt
		FirstConjRule.direct("-rimstu, -rimsti,", "-rimst, pag. -rimu", "rimt"), //aprimt
		FirstConjRule.direct("-riju, -rij,", "-rij, pag. -riju", "rīt"), //aizrīt
		FirstConjRule.direct("-rūcu, -rūc,", "-rūc, pag. -rūcu", "rūkt"), //atrūkt
		// S
		FirstConjRule.direct("-salkstu, -salksti,", "-salkst, pag. -salku", "salkt"), //aizsalkt
		FirstConjRule.direct("-salstu, -salsti,", "-salst, pag. -salu", "salt"), //atsalt
		FirstConjRule.direct("-sarkstu, -sarksti,", "-sarkst, pag. -sarku", "sarkt"), //aizsarkt
		FirstConjRule.direct("-saucu, -sauc,", "-sauc, pag. -saucu", "saukt"), //aizsaukt
		FirstConjRule.direct("-sāku, -sāc,", "-sāk, pag. -sāku", "sākt"), //aizsākt
		FirstConjRule.direct("-sedzu, -sedz,", "-sedz, pag. -sedzu", "segt"), //aizsegt
		FirstConjRule.direct("-sēršu, -sērs,", "-sērš, pag. -sērsu", "sērst"), //apsērst
		FirstConjRule.direct("-seru, -ser,", "-ser, pag. -sēru", "sērt"), //aizsērt
		FirstConjRule.direct("-sēstu, -sēsti,", "-sēst, pag. -sēdu", "sēst"), //aizsēst
		FirstConjRule.direct("-sēju, -sēj,", "-sēj, pag. -sēju", "sēt"), //apsēt
		FirstConjRule.direct("-sienu, -sien,", "-sien, pag. -sēju", "siet"), //aizsiet
		FirstConjRule.direct("-sieku, -siec,", "-siek, pag. -siku", "sikt"), //apsikt
		FirstConjRule.direct("-silstu, -silsti,", "-silst, pag. -silu", "silt"), //apsilt
		FirstConjRule.direct("-sirgstu, -sirgsti,", "-sirgst, pag. -sirgu", "sirgt"), //apsirgt
		FirstConjRule.direct("-situ, -sit,", "-sit, pag. -situ", "sist"), //aizsist
		FirstConjRule.direct("-sīkstu, -sīksti,", "-sīkst, pag. -sīku", "sīkt"), //apsīkt
		FirstConjRule.direct("-skaru, -skar,", "-skar, pag. -skāru", "skart"), //aizskart
		FirstConjRule.direct("-skaužu, -skaud,", "-skauž, pag. -skaudu", "skaust"), //apskaust
		FirstConjRule.direct("-skauju, -skauj,", "-skauj, pag. -skāvu", "skaut"), //apskaut
		FirstConjRule.direct("-skumstu, -skumsti,", "-skumst, pag. -skumu", "skumt"), //apskumt
		FirstConjRule.direct("-skurbstu, -skurbsti,", "-skurbst, pag. -skurbu", "skurbt"), //apskurbt
		FirstConjRule.direct("-skuju, -skuj,", "-skuj, pag. -skuvu", "skūt"), //aizskūt
		FirstConjRule.direct("-slāpstu, -slāpsti,", "-slāpst, pag. -slāpu", "slāpt"), //aizslāpt
		FirstConjRule.direct("-slāju, -slāj,", "-slāj, pag. -slāju", "slāt"), //aizslāt
		FirstConjRule.direct("-slaucu, -slauc,", "-slauc, pag. -slaucu", "slaukt"), //aizslaukt
		FirstConjRule.direct("-slābstu, -slābsti,", "-slābst, pag. -slābu", "slābt"), //atslābt
		FirstConjRule.direct("-slēdzu, -slēdz,", "-slēdz, pag. -slēdzu", "slēgt"), //aizslēgt
		FirstConjRule.direct("-slēpju, -slēp,", "-slēpj, pag. -slēpu", "slēpt"), //aizslēpt
		FirstConjRule.direct("-slimstu, -slimsti,", "-slimst, pag. -slimu", "slimt"), //apslimt
		FirstConjRule.direct("-slinkstu, -slinksti,", "-slinkst, pag. -slinku", "slinkt"), //apslinkt
		FirstConjRule.direct("-slīgstu, -slīgsti,", "-slīgst, pag. -slīgu", "slīgt"), //aizslīgt
		FirstConjRule.direct("-slīkstu, -slīksti,", "-slīkst, pag. -slīku", "slīkt"), //ieslīkt
		FirstConjRule.direct("-sliju, -slij,", "-slij, pag. -sliju", "slīt"), //ieslīt
		FirstConjRule.direct("-smoku, -smoc,", "-smok, pag. -smaku", "smakt"), //aizsmakt
		FirstConjRule.direct("-smeļu, -smel,", "-smeļ, pag. -smēlu", "smelt"), //apsmelt
		FirstConjRule.direct("-smeju, -smej,", "-smej, pag. -smēju", "smiet"), //apsmiet
		FirstConjRule.direct("-snaužu, -snaud,", "-snauž, pag. -snaudu", "snaust"), //aizsnaust
		FirstConjRule.direct("-sniedzu, -sniedz,", "-sniedz, pag. -sniedzu", "sniegt"), //aizsniegt
		FirstConjRule.direct("-sniegu, -sniedz,", "-snieg, pag. -snigu", "snigt"), //apsnigt
		FirstConjRule.direct("-speru, -sper,", "-sper, pag. -spēru", "spert"), //aizspert
		FirstConjRule.direct("-spēju, -spēj,", "-spēj, pag. -spēju", "spēt"), //aizspēt
		FirstConjRule.direct("-spiedzu, -spiedz,", "-spiedz, pag. -spiedzu", "spiegt"), //atspiegt
		FirstConjRule.direct("-spiežu, -spied,", "-spiež, pag. -spiedu", "spiest"), //aizspiest
		FirstConjRule.direct("-spirgstu, -spirgsti,", "-spirgst, pag. -spirgu", "spirgt"), //atspirgt
		FirstConjRule.direct("-spļauju, -spļauj,", "-spļauj, pag. -spļāvu", "spļaut"), //aizspļaut
		FirstConjRule.direct("-spraucu, -sprauc,", "-sprauc, pag. -spraucu", "spraukt"), //iespraukt
		FirstConjRule.direct("-spraužu, -spraud,", "-sprauž, pag. -spraudu", "spraust"), //aizspraust
		FirstConjRule.direct("-sprāgstu, -sprāgsti,", "-sprāgst, pag. -sprāgu", "sprāgt"), //atsprāgt
		FirstConjRule.direct("-spriežu, -spried,", "-spriež, pag. -spriedu", "spriest"), //apspriest
		FirstConjRule.direct("-spruku, -sprūc,", "-sprūk, pag. -spruku", "sprukt"), //aizsprukt
		FirstConjRule.direct("-sprūstu, -sprūsti,", "-sprūst, pag. -sprūdu", "sprūst"), //iesprūst
		FirstConjRule.direct("-stāju, -stāj,", "-stāj, pag. -stāju", "stāt"), //aizstāt
		FirstConjRule.direct("-steidzu, -steidz,", "-steidz, pag. -steidzu", "steigt"), //aizsteigt
		FirstConjRule.direct("-stiepju, -stiep,", "-stiepj, pag. -stiepu", "stiept"), //aizstiept
		FirstConjRule.direct("-stiegu, -stiedz,", "-stieg, pag. -stigu", "stigt"), //iestigt
		FirstConjRule.direct("-strebju, -streb,", "-strebj, pag. -strēbu", "strēbt"), //apstrēbt
		FirstConjRule.direct("-striegu, -striedz,", "-strieg, pag. -strigu", "strigt"), //iestrigt
		FirstConjRule.direct("-stulbstu, -stulbsti,", "-stulbst, pag. -stulbu", "stulbt"), //stulbt
		FirstConjRule.direct("-stumju, -stum,", "-stumj, pag. -stūmu", "stumt"), //aizstumt
		FirstConjRule.direct("-sūtu, -sūti,", "-sūt, pag. -sutu", "sust"), //apsust
		FirstConjRule.direct("-sūcu, -sūc,", "-sūc, pag. -sūcu", "sūkt"), //iesūkt
		FirstConjRule.direct("-sveicu, -sveic,", "-sveic, pag. -sveicu", "sveikt"), //apsveikt
		FirstConjRule.direct("-svelpju, -svelp,", "-svelpj, pag. -svelpu", "svelpt"), //iesvelpt
		FirstConjRule.direct("-sveru, -sver,", "-sver, pag. -svēru", "svērt"), //apsvērt
		FirstConjRule.direct("-sviežu, -svied,", "-sviež, pag. -sviedu", "sviest"), //aizsviest
		FirstConjRule.direct("-svilpju, -svilp,", "-svilpj, pag. -svilpu", "svilpt"), //atsvilpt
		FirstConjRule.direct("-svilstu, -svilsti,", "-svilst, pag. -svilu", "svilt"), //aizsvilt
		FirstConjRule.direct("-svīstu, -svīsti,", "-svīst, pag. -svīdu", "svīst"), //iesvīst
		// Š
		FirstConjRule.direct("-šalcu, -šalc,", "-šalc, pag. -šalcu", "šalkt"), //pašalkt, aizšalkt
		FirstConjRule.direct("-šauju, -šauj,", "-šauj, pag. -šāvu", "šaut"), //aizšaut
		FirstConjRule.direct("-šķeļu, -šķel,", "-šķeļ, pag. -šķēlu", "šķelt"), //aizšķelt
		FirstConjRule.direct("-šķēržu, -šķērd,", "-šķērž, pag. -šķērdu", "šķērst"), //izšķērst
		FirstConjRule.direct("-šķiebju, -šķieb,", "-šķiebj, pag. -šķiebu", "šķiebt"), //izšķiebt
		FirstConjRule.direct("-šķiežu, -šķied,", "-šķiež, pag. -šķiedu", "šķiest"), //aizšķiest
		FirstConjRule.direct("-šķiļu, -šķil,", "-šķiļ, pag. -šķīlu", "šķilt"), //aizšķilt
		FirstConjRule.direct("-šķiru, -šķir,", "-šķir, pag. -šķīru", "šķirt"), //aizšķirt
		FirstConjRule.direct("-šķīstu, -šķīsti,", "-šķīst, pag. -šķīdu", "šķīst"), //apšķīst
		FirstConjRule.direct("-šķinu, -šķin,", "-šķin, pag. -šķinu", "šķīt"), //iešķīt
		FirstConjRule.direct("-šļācu, -šļāc,", "-šļāc, pag. -šļācu", "šļākt"), //aizšļākt
		FirstConjRule.direct("-šļuku, -šļūc,", "-šļūk, pag. -šļuku", "šļukt"), //aizšļukt
		FirstConjRule.direct("-šļūcu, -šļūc,", "-šļūc, pag. -šļūcu", "šļūkt"), //aizšļūkt
		FirstConjRule.direct("-šmaucu, -šmauc,", "-šmauc, pag. -šmaucu", "šmaukt"), //aizšmaukt
		FirstConjRule.direct("-šmūku, -šmūc,", "-šmūk, pag. -šmuku", "šmukt"), //iešmukt
		FirstConjRule.direct("-šņaucu, -šņauc,", "-šņauc, pag. -šņaucu", "šņaukt"), //iešņaukt
		FirstConjRule.direct("-šņācu, -šņāc,", "-šņāc, pag. -šņācu", "šņākt"), //atšņākt
		FirstConjRule.direct("-šņāpju, -šņāp,", "-šņāpj, pag. -šņāpu", "šņāpt"), //apšņāpt
		FirstConjRule.direct("-šņūcu, -šņūc,", "-šņūc, pag. -šņūcu", "šņūkt"), //iešņūkt
		FirstConjRule.direct("-šuju, -šuj,", "-šuj, pag. -šuvu", "šūt"), //aizšūt
		// T
		FirstConjRule.direct("-topu, -topi,", "-top, pag. -tapu", "tapt"), //attapt
		FirstConjRule.direct("-tempju, -temp,", "-tempj, pag. -tempu", "tempt"), //iztempt
		FirstConjRule.direct("-tērpju, -tērp,", "-tērpj, pag. -tērpu", "tērpt"), //aptērpt
		FirstConjRule.direct("-tēšu, -tēs,", "-tēš, pag. -tēsu", "tēst"), //aptēst
		FirstConjRule.direct("-tinu, -tin,", "-tin, pag. -tinu", "tīt"), //aiztīt
		FirstConjRule.direct("-traucu, -trauc,", "-trauc, pag. -traucu", "traukt"), //aiztraukt
		FirstConjRule.direct("-trencu, -trenc,", "-trenc, pag. -trencu", "trenkt"), //aiztrenkt
		FirstConjRule.direct("-triecu, -triec,", "-triec, pag. -triecu", "triekt"), //aiztriekt
		FirstConjRule.direct("-triepju, -triep,", "-triepj, pag. -triepu", "triept"), //aiztriept
		FirstConjRule.direct("-trinu, -trin,", "-trin, pag. -trinu", "trīt"), //ietrīt
		FirstConjRule.direct("-trūkstu, -trūksti,", "-trūkst, pag. -trūku", "trūkt"), //iztrūkt
		FirstConjRule.direct("-tupstu, -tupsti,", "-tupst, pag. -tupu", "tupt"), //aiztupt
		FirstConjRule.direct("-tūkstu, -tūksti,", "-tūkst, pag. -tūku", "tūkt"), //aptūkt, aiztūkt
		FirstConjRule.direct("-tveru, -tver,", "-tver, pag. -tvēru", "tvert"), //aiztvert
		FirstConjRule.direct("-tvīkstu, -tvīksti,", "-tvīkst, pag. -tvīku", "tvīkt"), //ietvīkt
		// U
		FirstConjRule.direct("-urbju, -urb,", "-urbj, pag. -urbu", "urbt"), //aizurbt
		// V
		FirstConjRule.direct("-vācu, -vāc,", "-vāc, pag. -vācu", "vākt"), //aizvākt
		FirstConjRule.direct("-vārgstu, -vārgsti,", "-vārgst, pag. -vārgu", "vārgt"), //izvārgt
		FirstConjRule.direct("-vāžu, -vāz,", "-vāž, pag. -vāzu", "vāzt"), //aizvāzt
		FirstConjRule.direct("-veļu, -vel,", "-veļ, pag. -vēlu", "velt"), //aizvelt
		FirstConjRule.direct("-vemju, -vem,", "-vemj, pag. -vēmu", "vemt"), //apvemt
		FirstConjRule.direct("-vedu, -ved,", "-ved, pag. -vedu", "vest"), //aizvest
		FirstConjRule.direct("-vērpju, -vērp,", "-vērpj, pag. -vērpu", "vērpt"), //aizvērpt
		FirstConjRule.direct("-veru, -ver,", "-ver, pag. -vēru", "vērt"), //aizvērt
		FirstConjRule.direct("-vēžu, -vēz,", "-vēž, pag. -vēzu", "vēzt"), //apvēzt
		FirstConjRule.direct("-viebju, -vieb,", "-viebj, pag. -viebu", "viebt"), //izviebt
		FirstConjRule.direct("-viešu, -vies,", "-vieš, pag. -viesu", "viest"), //ieviest
		FirstConjRule.direct("-vilgstu, -vilgsti,", "-vilgst, pag. -vilgu", "vilgt"), //atvilgt
		FirstConjRule.direct("-velku, -velc,", "-velk, pag. -vilku", "vilkt"), //aizvilkt
		FirstConjRule.direct("-viļu, -vil,", "-viļ, pag. -vīlu", "vilt"), //aizvilt
		FirstConjRule.direct("-viju, -vij,", "-vij, pag. -viju", "vīt"), //aizvīt
		// Z
		FirstConjRule.direct("-zogu, -zodz,", "-zog, pag. -zagu", "zagt"), // apzagt
		FirstConjRule.direct("-ziežu, -zied,", "-ziež, pag. -ziedu", "ziest"), // aizziest
		FirstConjRule.direct("-zīžu, -zīd,", "-zīž, pag. -zīdu", "zīst"), // iezīst
		FirstConjRule.direct("-zīstu, -zīsti,", "-zīst, pag. -zinu", "zīt"), // atzīt
		FirstConjRule.direct("-zūdu, -zūdi,", "-zūd, pag. -zudu", "zust"), // aizzust
		FirstConjRule.direct("-zveļu, -zvel,", "-zveļ, pag. -zvēlu", "zvelt"), // atzvelt
		FirstConjRule.direct("-zvilstu, -zvilsti,", "-zvilst, pag. -zvilu", "zvilt"), // atzvilt
		// Ž
		FirstConjRule.direct("-žauju, -žauj,", "-žauj, pag. -žāvu", "žaut"), // apžaut
		FirstConjRule.direct("-žilbstu, -žilbsti,", "-žilbst, pag. -žilbu", "žilbt"), // apžilbt
		FirstConjRule.direct("-žirgstu, -žirgsti,", "-žirgst, pag. -žirgu", "žirgt"), // atžirgt
		FirstConjRule.direct("-žmaudzu, -žmaudz,", "-žmaudz, pag. -žmaudzu", "žmaugt"), // apžmaugt
		FirstConjRule.direct("-žmiedzu, -žmiedz,", "-žmiedz, pag. -žmiedzu", "žmiegt"), // aizžmiegt
		FirstConjRule.direct("-žņaudzu, -žņaudz,", "-žņaudz, pag. -žņaudzu", "žņaugt"), // aizžņaugt
		FirstConjRule.direct("-žūstu, -žūsti,", "-žūst, pag. -žuvu", "žūt"), // apžūt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas.
		FirstConjRule.direct3PersHomof("-aust, pag. -ausa", "aust", "\"aust\" (gaismai)"), //aizaust 1
		FirstConjRule.direct3PersHomof("-dzīst, pag. -dzija", "dzīt", "\"dzīt\" (ievainojumam)"), //aizdzīt 2
		FirstConjRule.direct3PersHomof("-irst, pag. -ira", "irt", "\"irt\" (audumam)"), //irt 2
		//FirstConjRule.direct3PersHomof("-verd, pag. -virda", "virst", "\"virst\" (vārīties)"), //izvirst 2
			//TODO -virst, arī -vird, pag. -virta, arī -virda (retāk -verda)
			// Virst/virt ir murgs, jāsagaida, ko teiks L&B

		// Standartizētie.
		// A, B
		FirstConjRule.direct3Pers("-brūk, pag. -bruka", "brukt"), //aizbrukt
		// C, D
		FirstConjRule.direct3Pers("-dim, pag. -dima", "dimt"), //aizdimt
		FirstConjRule.direct3Pers("-dip, pag. -dipa", "dipt"), //aizdipt
		// E, F, G
		FirstConjRule.direct3Pers("-grūst, pag. -gruva", "grūt"), //aizgrūt
		// H, I, J, K
		FirstConjRule.direct3Pers("-kviec, pag. -kvieca", "kviekt"), //aizkviekt
		// L, M
		FirstConjRule.direct3Pers("-milst, pag. -milza", "milzt"), //aizmilzt
		// N, Ņ
		FirstConjRule.direct3Pers("-ņirb, pag. -ņirba", "ņirbt"), //aizņirbt
		// O, P, R, S, Š, T, U, V, Z

		// Pilnīgs nestandarts.
		FirstConjRule.of("-teicu, -teic,", "-teic (tagadnes formas parasti nelieto), pag. -teicu", "teikt", 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"teikt\"")},
				new Tuple[]{Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.NOT_PRESENT_FORMS.s)},
				new String[]{"teik"}, new String[]{"teic"}, new String[]{"teic"}), //atteikt
		//FirstConjRule.of("3. pers. -guldz, pag. -guldza\"", "gulgt", 15,
		//		new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"gulgt\"")},
		//		new Tuple[]{Features.USUALLY_USED__THIRD_PERS},
		//		new String[]{"gulg"}, new String[]{"guldz"}, new String[]{"guldz"}), //aizgulgt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Likumi, kam ir visu formu variants.
		RegularVerbRule.secondConjDir("-dabūju, -dabū,", "-dabū, pag. -dabūju", "dabūt"), //aizdabūt
		RegularVerbRule.secondConjDir("-klusēju, -klusē,", "-klusē, pag. -klusēju", "klusēt"), //noklusēt
		RegularVerbRule.secondConjDir("-lāsoju, -lāso,", "-lāso, pag. -lāsoju", "lāsot"), //nolāsot
		RegularVerbRule.secondConjDir("-ņurīju, -ņurī,", "-ņurī, pag. -ņurīju", "noņurīt"), //noņurīt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		// Īpašā piezīme par glumēšanu: 2. konjugāciju nosaka 3. personas
		// galotne "-ē" - 3. konjugācijai būtu bez.
		RegularVerbRule.secondConjDir3PersParallel(
				"-glumē, pag. -glumēja (retāk -gluma, 1. konj.)", "glumēt"), //izglumēt

		// Standartizētie.
		RegularVerbRule.secondConjDir3Pers("-kūko, pag. -kūkoja", "kūkot"), //aizkūkot
		RegularVerbRule.secondConjDir3Pers("-mirgo, pag. -mirgoja", "mirgot"), //aizmirgot
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		BaseRule.thirdConjDirAllPersParallel(
				"-moku, -moki, -moka, arī -mocu, -moci, -moca, pag. -mocīju", "mocīt"), //aizmocīt
		BaseRule.thirdConjDirAllPersParallel(
				"-murcu, -murci, -murca, retāk -murku, -murki, -murka, pag. -murcīju", "murcīt"), //apmurcīt
		BaseRule.thirdConjDirAllPersParallel(
				"-ņurcu, -ņurci, -ņurca, retāk -ņurku, -ņurki, -ņurka, pag. -ņurcīju", "ņurcīt"), //apmurcīt
		BaseRule.thirdConjDirAllPersParallel(
				"-sēžu, -sēdi, -sēž, arī -sēdu, -sēdi, -sēd, pag. -sēdēju", "sēdēt"), //iesēdēt

		BaseRule.thirdConjDirAllPersParallel(
				"-dzirdu, -dzirdi, -dzird, pag. -dzirdu (1. konj.), arī -dzirdēju",
				"dzirdēt", false), //izdzirdēt
		BaseRule.thirdConjDirAllPersParallel(
				"-slīdu, -slīdi, -slīd, pag. -slīdēju, -slīdēji, -slīdēja (retāk -slīda, 1. konj.)",
				"slīdēt", false), // aizslīdēt

		BaseRule.thirdConjDirAllPersParallel(
				"-guļu, -guli, -guļ (arī -gul), pag. -gulēju", "gulēt"), // iegulēt
			//TODO kā norādīt miju + ko darīt ar otru, standartizēto gulēt?

		// Standartizētie.
		// A, B
		RegularVerbRule.thirdConjDir("-brauku, -brauki,", "-brauka, pag. -braucīju", "braucīt", true), //apbraucīt
		RegularVerbRule.thirdConjDir("-burkšu, -burkši,", "-burkš, pag. -burkšēju", "burkšēt", false), //izburkšēt
		RegularVerbRule.thirdConjDir("-burkšķu, -burkšķi,", "-burkšķ, pag. -burkšķēju", "burkšķēt", false), //izburkšķēt
		// C
		RegularVerbRule.thirdConjDir("-ceru, -ceri,", "-cer, pag. -cerēju", "cerēt", false), //iecerēt
		// Č
		RegularVerbRule.thirdConjDir("-čerkstu, -čerksti,", "-čerkst, pag. -čerkstēju", "čerkstēt", false), //nočerkstēt
		RegularVerbRule.thirdConjDir("-čērkstu, -čērksti,", "-čērkst, pag. -čērkstēju", "čērkstēt", false), //nočērkstēt
		RegularVerbRule.thirdConjDir("-čiepstu, -čiepsti,", "-čiepst, pag. -čiepstēju", "čiepstēt", false), //iečiepstēt
		RegularVerbRule.thirdConjDir("-činkstu, -činksti,", "-činkst, pag. -činkstēju", "činkstēt", false), //izčinkstēt
		RegularVerbRule.thirdConjDir("-čīkstu, -čīksti,", "-čīkst, pag. -čīkstēju", "čīkstēt", false), //izčīkstēt
		RegularVerbRule.thirdConjDir("-čukstu, -čuksti,", "-čukst, pag. -čukstēju", "čukstēt", false), //atčukstēt
		RegularVerbRule.thirdConjDir("-čurnu, -čurni,", "-čurn, pag. -čurnēju", "čurnēt", false), //nočurnēt
		// D
		RegularVerbRule.thirdConjDir("-deru, -deri,", "-der, pag. -derēju", "derēt", false), //noderēt 1, 2
		RegularVerbRule.thirdConjDir("-dēdu, -dēdi,", "-dēd, pag. -dēdēju", "dēdēt", false), //izdēdēt
		RegularVerbRule.thirdConjDir("-dienu, -dieni,", "-dien, pag. -dienēju", "dienēt", false), //atdienēt
		RegularVerbRule.thirdConjDir("-dirnu, -dirni,", "-dirn, pag. -dirnēju", "dirnēt", false), //nodirnēt
		RegularVerbRule.thirdConjDir("-draudu, -draudi,", "-draud, pag. -draudēju", "draudēt", false), //apdraudēt
		RegularVerbRule.thirdConjDir("-drebu, -drebi,", "-dreb, pag. -drebēju", "drebēt", false), //nodrebēt
		RegularVerbRule.thirdConjDir("-dusu, -dusi,", "-dus, pag. -dusēju", "dusēt", false), //atdusēt
		RegularVerbRule.thirdConjDir("-dziedu, -dziedi,", "-dzied, pag. -dziedāju", "dziedāt", false), //aizdziedāt
		// E, F, G
		RegularVerbRule.thirdConjDir("-glūnu, -glūni,", "-glūn, pag. -glūnēju", "glūnēt", false), //apglūnēt
		RegularVerbRule.thirdConjDir("-grabu, -grabi,", "-grab, pag. -grabēju", "grabēt", false), //aizgrabēt
		RegularVerbRule.thirdConjDir("-gribu, -gribi,", "-grib, pag. -gribēju", "gribēt", false), //iegribēt
		RegularVerbRule.thirdConjDir("-guļu, -guli,", "-guļ, pag. -gulēju", "gulēt", true), //aizgulēt
		// H, I, Ī
		RegularVerbRule.thirdConjDir("-īdu, -īdi,", "-īd, pag. -īdēju", "īdēt", false), //noīdēt
		// J, K
		RegularVerbRule.thirdConjDir("-klabu, -klabi,", "-klab, pag. -klabēju", "klabēt", false), //paklabēt, aizklabēt
		RegularVerbRule.thirdConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimstēju", "klimstēt", false), //aizklimstēt
		RegularVerbRule.thirdConjDir("-krekstu, -kreksti,", "-krekst, pag. -krekstēju", "krekstēt", false), //nokrekstēt
		RegularVerbRule.thirdConjDir("-krekšu, -krekši,", "-krekš, pag. -krekšēju", "krekšēt", false), //nokrekšēt
		RegularVerbRule.thirdConjDir("-krekšķu, -krekšķi,", "-krekšķ, pag. -krekšķēju", "krekšķēt", false), //nokrekšķēt
		RegularVerbRule.thirdConjDir("-kunkstu, -kunksti,", "-kunkst, pag. -kunkstēju", "kunkstēt", false), //izkunkstēt
		RegularVerbRule.thirdConjDir("-kustu, -kusti,", "-kust, pag. -kustēju", "kustēt", false), //aizkustēt
		RegularVerbRule.thirdConjDir("-kūpu, -kūpi,", "-kūp, pag. -kūpēju", "kūpēt", false), //apkūpēt, aizkūpēt
		RegularVerbRule.thirdConjDir("-kvernu, -kverni,", "-kvern, pag. -kvernēju", "kvernēt", false), //nokvernēt
		RegularVerbRule.thirdConjDir("-kvēlu, -kvēli,", "-kvēl, pag. -kvēlēju", "kvēlēt", false), //izkvēlēt
		// L
		RegularVerbRule.thirdConjDir("-lādu, -lādi,", "-lād, pag. -lādēju", "lādēt", false), //nolādēt
		RegularVerbRule.thirdConjDir("-līdzu, -līdzi,", "-līdz, pag. -līdzēju", "līdzēt", false), //izlīdzēt
		RegularVerbRule.thirdConjDir("-palīdzu, -palīdzi,", "-palīdz, pag. -palīdzēju", "palīdzēt", false), //izpalīdzēt
		RegularVerbRule.thirdConjDir("-loku, -loki,", "-loka, pag. -locīju", "locīt", true), //aizlocīt
		RegularVerbRule.thirdConjDir("-lūru, -lūri,", "-lūr, pag. -lūrēju", "lūrēt", false), //aplūrēt
		// Ļ
		RegularVerbRule.thirdConjDir("-ļogu, -ļogi,", "-ļoga, pag. -ļodzīju", "ļodzīt", true), //izļodzīt
		// M
		RegularVerbRule.thirdConjDir("-minu, -mini,", "-min, pag. -minēju", "minēt", false), //atminēt
		RegularVerbRule.thirdConjDir("-mīlu, -mīli,", "-mīl, pag. -mīlēju", "mīlēt", false), //iemīlēt
		RegularVerbRule.thirdConjDir("-mīstu, -mīsti,", "-mīsta, pag. -mīstīju", "mīstīt", false), //izmīstīt
		RegularVerbRule.thirdConjDir("-muldu, -muldi,", "-muld, pag. -muldēju", "muldēt", false), //atmuldēt
		RegularVerbRule.thirdConjDir("-murdu, -murdi,", "-murd, pag. -murdēju", "murdēt", false), //nomurdēt
		RegularVerbRule.thirdConjDir("-murdzu, -murdzi,", "-murdza, pag. -murdzīju", "murdzīt", false), //apmurdzīt
		RegularVerbRule.thirdConjDir("-murkšu, -murkši,", "-murkš, pag. -murkšēju", "murkšēt", false), //nomurkšēt
		RegularVerbRule.thirdConjDir("-murkšķu, -murkšķi,", "-murkšķ, pag. -murkšķēju", "murkšķēt", false), //nomurkšķēt
		// N, Ņ
		RegularVerbRule.thirdConjDir("-ņaudu, -ņaudi,", "-ņaud, pag. -ņaudēju", "ņaudēt", false), //izņaudēt
		RegularVerbRule.thirdConjDir("-ņerkstu, -ņerksti,", "-ņerkst, pag. -ņerkstēju", "ņerkstēt", false), //noņerkstēt
		RegularVerbRule.thirdConjDir("-ņurdu, -ņurdi,", "-ņurd, pag. -ņurdēju", "ņurdēt", false), //atņurdēt
		RegularVerbRule.thirdConjDir("-ņurkstu, -ņurksti,", "-ņurkst, pag. -ņurkstēju", "ņurkstēt", false), //noņurkstēt
		RegularVerbRule.thirdConjDir("-ņurkšu, -ņurkši,", "-ņurkš, pag. -ņurkšēju", "ņurkšēt", false), //noņurkšēt
		RegularVerbRule.thirdConjDir("-ņurkšķu, -ņurkšķi,", "-ņurkšķ, pag. -ņurkšķēju", "ņurkšķēt", false), //noņurkšķēt
		// O, P
		RegularVerbRule.thirdConjDir("-parkšu, -parkši,", "-parkš, pag. -parkšēju", "parkšēt", false), //noparkšēt
		RegularVerbRule.thirdConjDir("-parkšķu, -parkšķi,", "-parkšķ, pag. -parkšķēju", "parkšķēt", false), //noparkšķēt
		RegularVerbRule.thirdConjDir("-peldu, -peldi,", "-peld, pag. -peldēju", "peldēt", false), //aizpeldēt
		RegularVerbRule.thirdConjDir("-pilu, -pili,", "-pil, pag. -pilēju", "pilēt", false), //appilēt, aizpilēt
		RegularVerbRule.thirdConjDir("-pīkstu, -pīksti,", "-pīkst, pag. -pīkstēju", "pīkstēt", false), //atpīkstēt
		RegularVerbRule.thirdConjDir("-precu, -preci,", "-prec, pag. -precēju", "precēt", false), //aizprecēt
		RegularVerbRule.thirdConjDir("-putu, -puti,", "-put, pag. -putēju", "putēt", false), //aizputēt, apputēt
		// R
		RegularVerbRule.thirdConjDir("-raudu, -raudi,", "-raud, pag. -raudāju", "raudāt", false), //apraudāt
		RegularVerbRule.thirdConjDir("-raugu, -raugi,", "-rauga, pag. -raudzīju", "raudzīt", true), //apraudāt
		RegularVerbRule.thirdConjDir("-redzu, -redzi,", "-redz, pag. -redzēju", "redzēt", false), //apredzēt
		// S
		RegularVerbRule.thirdConjDir("-saku, -saki,", "-saka, pag. -sacīju", "sacīt", true), //atsacīt
		RegularVerbRule.thirdConjDir("-sāpu, -sāpi,", "-sāp, pag. -sāpēju", "sāpēt", false), //izsāpēt
		RegularVerbRule.thirdConjDir("-sēžu, -sēdi,", "-sēž, pag. -sēdēju", "sēdēt", true), //atsēdēt
		RegularVerbRule.thirdConjDir("-sīkstu, -sīksti,", "-sīkst, pag. -sīkstēju", "sīkstēt", false), //apsīkstēt
		RegularVerbRule.thirdConjDir("-slaku, -slaki,", "-slaka, pag. -slacīju", "slacīt", true), //aizslacīt
		RegularVerbRule.thirdConjDir("-slauku, -slauki,", "-slauka, pag. -slaucīju", "slaucīt", true), //aizslaucīt
		RegularVerbRule.thirdConjDir("-slogu, -slogi,", "-sloga, pag. -slodzīju", "slodzīt", true), //ieslodzīt
		RegularVerbRule.thirdConjDir("-smīnu, -smīni,", "-smīn, pag. -smīnēju", "smīnēt", true), //atsmīnēt
		RegularVerbRule.thirdConjDir("-snaikos, -snaikies,", "-snaikās, pag. -snaicījos", "snaicīties", true), //izsnaicīties
		RegularVerbRule.thirdConjDir("-stāvu, -stāvi,", "-stāv, pag. -stāvēju", "stāvēt", false), //aizstāvēt
		RegularVerbRule.thirdConjDir("-stenu, -steni,", "-sten, pag. -stenēju", "stenēt", false), //izstenēt
		RegularVerbRule.thirdConjDir("-stīdzu, -stīdzi,", "-stīdz, pag. -stīdzēju", "stīdzēt", false), //izstīdzēt
		RegularVerbRule.thirdConjDir("-strīdu, -strīdi,", "-strīd, pag. -strīdēju", "strīdēt", false), //apstrīdēt
		RegularVerbRule.thirdConjDir("-sūdzu, -sūdzi,", "-sūdz, pag. -sūdzēju", "sūdzēt", false), //apsūdzēt
		RegularVerbRule.thirdConjDir("-svepstu, -svepsti,", "-svepst, pag. -svepstēju", "svepstēt", false), //izsvepstēt
		RegularVerbRule.thirdConjDir("-svinu, -svini,", "-svin, pag. -svinēju", "svinēt", false), //izsvinēt
		// Š
		RegularVerbRule.thirdConjDir("-šļauku, -šļauki,", "-šļauka, pag. -šļaucīju", "šļaucīt", false), //atšļupstēt
		RegularVerbRule.thirdConjDir("-šļupstu, -šļupsti,", "-šļupst, pag. -šļupstēju", "šļupstēt", false), //atšļupstēt
		RegularVerbRule.thirdConjDir("-šņukstu, -šņuksti,", "-šņukst, pag. -šņukstēju", "šņukstēt", false), //izšņukstēt
		// T
		RegularVerbRule.thirdConjDir("-tarkšķu, -tarkšķi,", "-tarkšķ, pag. -tarkšķēju", "tarkšķēt", false), //iztarkšķēt
		RegularVerbRule.thirdConjDir("-teku, -teci,", "-tek, pag. -tecēju", "tecēt", true), //aiztecēt
		RegularVerbRule.thirdConjDir("-trīcu, -trīci,", "-trīc, pag. -trīcēju", "trīcēt", false), //ietrīcēt
		RegularVerbRule.thirdConjDir("-trīsu, -trīsi,", "-trīs, pag. -trīsēju", "trīsēt", false), //ietrīsēt
		RegularVerbRule.thirdConjDir("-turu, -turi,", "-tur, pag. -turēju", "turēt", false), //aizturēt
		// U, V
		RegularVerbRule.thirdConjDir("-vēlu, -vēli,", "-vēl, pag. -vēlēju", "vēlēt", false), //atvēlēt
		RegularVerbRule.thirdConjDir("-vaidu, -vaidi,", "-vaid, pag. -vaidēju", "vaidēt", false), //izvaidēt
		// Z
		RegularVerbRule.thirdConjDir("-zinu, -zini,", "-zina, pag. -zināju", "zināt", false), //apzināt
		RegularVerbRule.thirdConjDir("-zvēru, -zvēri,", "-zvēr, pag. -zvērēju", "zvērēt", false), //apzvērēt
		RegularVerbRule.thirdConjDir("-žņaugu, -žņaugi,", "-žņauga, pag. -žņaudzīju", "žņaudzīt", true), //izžņaudzīt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		RegularVerbRule.thirdConjDir3PersParallel(
				"-grand, pag. -grandēja (retāk -granda, 1. konj.)", "grandēt", false), //aizgrandēt
		RegularVerbRule.thirdConjDir3PersParallel(
				"-gruzd, pag. -gruzdēja (retāk -gruzda, 1. konj.)", "gruzdēt", false), //aizgruzdēt
		RegularVerbRule.thirdConjDir3PersParallel(
				"-mirdz, pag. -mirdzēja (retāk -mirdza, 1. konj.)", "mirdzēt", false), //aizmirdzēt

		// Standartizētie.
		// A, B
		RegularVerbRule.thirdConjDir3Pers("-blākš, pag. -blākšēja", "blākšēt", false), //aizblākšēt
		RegularVerbRule.thirdConjDir3Pers("-blākšķ, pag. -blākšķēja", "blākšķēt", false), //aizblākšķēt
		// C, Č
		RegularVerbRule.thirdConjDir3Pers("-čab, pag. -čabēja", "čabēt", false), //aizčabēt
		RegularVerbRule.thirdConjDir3Pers("-čaukst, pag. -čaukstēja", "čaukstēt", false), //aizčaukstēt
		// D
		RegularVerbRule.thirdConjDir3Pers("-dārd, pag. -dārdēja", "dārdēt", false), //aizdārdēt
		RegularVerbRule.thirdConjDir3Pers("-dimd, pag. -dimdēja", "dimdēt", false), //aizdimdēt
		RegularVerbRule.thirdConjDir3Pers("-dip, pag. -dipēja", "dipēt", false), //aizdipēt
		RegularVerbRule.thirdConjDir3Pers("-dun, pag. -dunēja", "dunēt", false), //aizdunēt
		RegularVerbRule.thirdConjDir3Pers("-džinkst, pag. -džinkstēja", "džinkstēt", false), //aizdžinkstēt
		// E, F, G
		RegularVerbRule.thirdConjDir3Pers("-gurkst, pag. -gurkstēja", "gurkstēt", false), //aizgurkstēt
		// H, I, J, K
		RegularVerbRule.thirdConjDir3Pers("-klakst, pag. -klakstēja", "klakstēt", false), //aizklakstēt
		RegularVerbRule.thirdConjDir3Pers("-klaudz, pag. -klaudzēja", "klaudzēt", false), //aizklaudzēt
		// L, M, N, Ņ
		RegularVerbRule.thirdConjDir3Pers("-ņirb, pag. -ņirbēja", "ņirbēt", false), //aizņirbēt
		// O, P, R, S, T, U, V, Z
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] directMultiConjVerb = {
		// Likumi, kam ir visu formu variants.
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-bedīju, -bedī, -bedī, arī -bedu, -bedi, -beda, pag. -bedīju",
				".*bedīt", false), // apbedīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-brūnu, -brūni, -brūn, arī -brūnēju, -brūnē, -brūnē, pag. -brūnēju",
				".*brūnēt", false), // nobrūnēt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-ceru, -ceri, -cer, retāk -cerēju, -cerē, -cerē, pag. -cerēju",
				".*cerēt", false), // apcerēt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-cienu, -cieni, -ciena, arī -cienīju, -cienī, -cienī, pag. -cienīju",
				".*cienīt", false), // iecienīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-dēstu, -dēsti, -dēsta, retāk -dēstīju, -dēstī, -dēstī, pag. -dēstīju",
				".*dēstīt", false), // apdēstīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-kristīju, -kristī, -kristī, arī -kristu, -kristi, -krista, pag. -kristīju",
				".*kristīt", false), // iekristīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-krustīju, -krusti, -krusti, arī -krustu, -krusti, -krusta, pag. -krustīju",
				".*krustīt", false), // iekrustīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-kveldēju, -kveldē, -kveldē, arī -kveldu, -kveldi, -kveld, pag. -kveldēju",
				".*kveldēt", false), // nokveldēt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-ķēzīju, -ķēzī, -ķēzī, arī -ķēzu, -ķēzi, -ķēza, pag. -ķēzīju",
				".*ķēzīt", false), // apķēzīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-lāsu, -lāsi, -lās, retāk -lāsēju, -lāsē, -lāsē, pag. -lāsēju",
				".*lāsēt", false), // nolāsēt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-mērīju, -mērī, -mērī, arī -mēru, -mēri, -mēra, pag. -mērīju",
				".*mērīt", false), // atmērīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-pelnu, -pelni, -pelna, arī -pelnīju, -pelnī, -pelnī, pag. -pelnīju",
				".*pelnīt", false), // atpelnīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-pētīju, -pētī, -pētī, arī -pētu, -pēti, -pēta, pag. -pētīju",
				".*pētīt", false), // appētīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-rotu, -roti, -rota, arī -rotīju, -roti, -rotī, pag. -rotīju",
				".*rotīt", false), // aizrotīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-sargāju, -sargā, -sargā, arī -sargu, -sargi, -sarga, pag. -sargāju",
				".*sargāt", false), // aizsargāt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-svētīju, -svētī, -svētī, arī -svētu, -svēti, -svēta, pag. -svētīju",
				".*svētīt", false), // aizsargāt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-tašķīju, -tašķī, -tašķī, arī -tašķu, -tašķi, -tašķa, pag. -tašķīju",
				".*tašķīt", false), // aptašķīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-veltīju, -veltī, -veltī, arī -veltu, -velti, -velta, pag. -veltīju",
				".*veltīt", false), // apveltīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-vētīju, -vētī, -vētī, arī -vētu, -vēti, -vēta, pag. -vētīju",
				".*vētīt", false), // aizvētīt
		// Paralēlformu paralēlforma.
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-bālu, -bāli, -bāl, arī -bālēju, -bālē, -balē, pag. -bālēju (arī -bālu, 1. konj.)",
				".*bālēt", false), //nobālēt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-rūsēju, -rūsē, -rūsē, arī -rūsu, -rūsi, -rūs, pag. -rūsēju (retāk -rūsu, 1. konj.)",
				".*rūsēt", false), // ierūsēt

			// TODO - vai tiešām šis nav kaut kur jāiesistematizē?
		// Likumi, kam ir tikai "parasti 3. pers." variants.
		BaseRule.of("parasti 3. pers., -pelē, arī -pel, pag. -pelēja", new SimpleSubRule[]{
						SimpleSubRule.of(".*pelēt", new Integer[]{16, 17},
								new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS, Features.NO_PRESENT_SOUNDCHANGE})},
				new Tuple[]{Features.USUALLY_USED__THIRD_PERS}), // aizpelēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
	 */
	public static final Rule[] reflFirstConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Nenoteiksmes homoformas.
		FirstConjRule.reflHomof("-aužos, -audies,", "-aužas, pag. -audos", "austies",
				"\"austies\" (audumam)"), //apausties
		FirstConjRule.reflHomof("-dzenos, -dzenies,", "-dzenas, pag. -dzinos", "dzīties",
				"\"dzīties\" (lopiem)"), //aizdzīties
		FirstConjRule.reflHomof("-iros, -iries,", "-iras, pag. -īros", "irties",
				"\"irties\" (ar airiem)"), //aizirties
		FirstConjRule.reflHomof("-mistos, -misties,", "-mistas, pag. -misos", "misties",
				"\"mist\" (krist izmisumā)"), //izmisties // Liekas, ka otra vārdnīcā nav.
		FirstConjRule.reflHomof("-minos, -minies,", "-minas, pag. -minos", "mīties",
				"\"mīties\" (pedāļus)"), //aizmīties
		FirstConjRule.reflHomof("-mijos, -mijies,", "-mijas, pag. -mijos", "mīties",
				"\"mīties\" (mainīt naudu)"), //apmīties
		FirstConjRule.reflHomof("-tiekos, -tiecies,", "-tiekas, pag. -tikos", "tikties",
				"\"tikties\" (satikties ar kādu)"), //satikties
		// Izņēmuma izņēmums :/
		FirstConjRule.reflHomof("-patīkos, -patīcies,", "-patīkas, pag. -patikos", "patikties",
				"\"tikties\" (patikties kādam)"), //iepatikties
		FirstConjRule.reflHomof("-vēršos, -vērsies,", "-vēršas, pag. -vērsos", "vērsties",
					"\"vērsties\" (mainīt virzienu)"), //aizvērsties, izvērsties 1
		FirstConjRule.reflHomof("-vēršos, -vērsies,", "-vēršas, pag. -vērtos", "vērsties",
					"\"vērsties\" (mainīt būtību)"), //izvērsties 2


		// Paralēlās formas.
		FirstConjRule.reflAllPersParallel(
				"-aujos, -aujies, -aujas, arī -aunos, -aunies, -aunas, pag. -āvos", "auties"), //apauties
		FirstConjRule.reflAllPersParallel(
				"-gulstos, -gulsties, -gulstas, arī -guļos, -gulies, -guļas, pag. -gūlos, arī -gulos",
				"gulties"), //aizgulties
		FirstConjRule.reflAllPersParallel(
				"-plešos, -pleties, -plešas, pag. -pletos, arī -plētos", "plesties"), //ieplesties
		FirstConjRule.reflAllPersParallel(
				"-sēžos, -sēdies, -sēžas, arī -sēstos, -sēsties, -sēstas, pag. -sēdos", "sēsties"), //aizsēsties
		FirstConjRule.reflAllPersParallel(
				"-skrienos, -skrienies, -skrienas, arī -skrejos, -skrejies, -skrejas, pag. -skrējos", "skrieties"), //ieskrieties
		FirstConjRule.reflAllPersParallel(
				"-slienos, -slienies, -slienas, arī -slejos, -slejies, -slejas, pag. -slējos", "slieties"), //aizslieties
		FirstConjRule.reflAllPersParallel(
				"-spurdzos, -spurdzies, -spurdzas, pag. -spurdzos, retāk -spurgstos, -spurgsties, -spurgstas, pag. -spurgos", "spurgties"), //iespurgties

		// Izņēmums.
		FirstConjRule.of("-pazīstos, -pazīsties,", "-pazīstas, pag. -pazinos", "pazīties", 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"zīties\"")}, null,
				new String[]{"zī"}, new String[]{"zīst"}, new String[]{"zin"}), //iepazīties

		// Standartizētie.
		// A
		FirstConjRule.refl("-aros, -aries,", "-aras, pag. -aros", "arties"), //iearties
		// B
		FirstConjRule.refl("-baros, -baries,", "-baras, pag. -bāros", "bārties"), //izbārties
		FirstConjRule.refl("-bāžos, -bāzies,", "-bāžas, pag. -bāzos", "bāzties"), //iebāzties
		FirstConjRule.refl("-beidzos, -beidzies,", "-beidzas, pag. -beidzos", "beigties"), //izbeigties
		FirstConjRule.refl("-beržos, -berzies,", "-beržas, pag. -berzos", "berzties"), //izberzties
		FirstConjRule.refl("-bēgos, -bēdzies,", "-bēgas, pag. -bēgos", "bēgties"), //nobēgties
		FirstConjRule.refl("-beros, -beries,", "-beras, pag. -bēros", "bērties"), //apbērties
		FirstConjRule.refl("-bīstos, -bīsties,", "-bīstas, pag. -bijos", "bīties"), //izbīties
		FirstConjRule.refl("-braucos, -braucies,", "-braucas, pag. -braucos", "braukties"), //izbraukties
		FirstConjRule.refl("-brāžos, -brāzies,", "-brāžas, pag. -brāzos", "brāzties"), //aizbrāzties
		FirstConjRule.refl("-brienos, -brienies,", "-brienas, pag. -bridos", "bristies"), //atbristies
		FirstConjRule.refl("-būros, -buries,", "-buras, pag. -būros", "burties"), //izburties
		// C
		FirstConjRule.refl("-ceļos, -celies,", "-ceļas, pag. -cēlos", "celties"), //apcelties
		FirstConjRule.refl("-ceros, -ceries,", "-ceras, pag. -cerējos", "cerēties"), //atcerēties
		FirstConjRule.refl("-ciešos, -cieties,", "-ciešas, pag. -cietos", "ciesties"), //aizciesties
		FirstConjRule.refl("-cērtos, -cērties,", "-cērtas, pag. -cirtos", "cirsties"), //aizcirsties
		// D
		FirstConjRule.refl("-dejos, -dejies,", "-dejas, pag. -dējos", "dieties"), //izdieties
		FirstConjRule.refl("-dodos, -dodies,", "-dodas, pag. -devos", "doties"), //atdoties
		FirstConjRule.refl("-drāžos, -drāzies,", "-drāžas, pag. -drāzos", "drāzties"), //aizdrāzties
		FirstConjRule.refl("-duros, -duries,", "-duras, pag. -dūros", "durties"), //aizdurties
		FirstConjRule.refl("-dzeros, -dzeries,", "-dzeras, pag. -dzēros", "dzerties"), //apdzerties
		FirstConjRule.refl("-dzēšos, -dzēsies,", "-dzēšas, pag. -dzēsos", "dzēsties"), //izdzēsties
		// E
		FirstConjRule.refl("-ēdos, -ēdies,", "-ēdas, pag. -ēdos", "ēsties"), //atēsties
		// F, G
		FirstConjRule.refl("-gaužos, -gaudies,", "-gaužas, pag. -gaudos", "gausties"), //izgausties
		FirstConjRule.refl("-gāžos, -gāzies,", "-gāžas, pag. -gāzos", "gāzties"), //apgāzties, aizgāzties
		FirstConjRule.refl("-glaužos, -glaudies,", "-glaužas, pag. -glaudos", "glausties"), //ieglausties
		FirstConjRule.refl("-glābjos, -glābies,", "-glābjas, pag. -glābos", "glābties"), //izglābties
		FirstConjRule.refl("-graužos, -grauzies,", "-graužas, pag. -grauzos", "grauzties"), //izgrauzties, aizgrauzties
		FirstConjRule.refl("-gremžos, -gremdies,", "-gremžas, pag. -gremdos", "gremsties"), //izgremsties
		FirstConjRule.refl("-gremžos, -gremzies,", "-gremžas, pag. -gremzos", "gremzties"), //izgremzties
		FirstConjRule.refl("-griežos, -griezies,", "-griežas, pag. -griezos", "griezties"), //aizgriezties 1, 2
		FirstConjRule.refl("-gružos, -grūdies,", "-grūžas, pag. -grūdos", "grūsties"), //atgrūsties
		FirstConjRule.refl("-gūstos, -gūsties,", "-gūstas, pag. -guvos", "gūties"), //aizgūties
		// Ģ
		FirstConjRule.refl("-ģērbjos, -ģērbies,", "-ģērbjas, pag. -ģērbos", "ģērbties"), //apģērbties
		// H, I
		FirstConjRule.refl("-ejos, -ejies,", "-ietas, pag. -gājos", "ieties"), //apieties
		// Ī
		FirstConjRule.refl("-īdos, -īdies,", "-īdas, pag. -īdējos", "īdēties"), //ieīdēties
		// J
		FirstConjRule.refl("-jaucos, -jaucies,", "-jaucas, pag. -jaucos", "jaukties"), //iejaukties
		FirstConjRule.refl("-jājos, -jājies,", "-jājas, pag. -jājos", "jāties"), //izjāties
		FirstConjRule.refl("-jēdzos, -jēdzies,", "-jēdzas, pag. -jēdzos", "jēgties"), //nojēgties
		FirstConjRule.refl("-jožos, -jozies,", "-jožas, pag. -jozos", "jozties"), //apjozties
		FirstConjRule.refl("-jūtos, -jūties,", "-jūtas, pag. -jutos", "justies"), //iejusties
		FirstConjRule.refl("-jūdzos, -jūdzies,", "-jūdzas, pag. -jūdzos", "jūgties"), //aizjūgties
		// K
		FirstConjRule.refl("-kaļos, -kalies,", "-kaļas, pag. -kalos", "kalties"), //iekalties
		FirstConjRule.refl("-kampjos, -kampies,", "-kampjas, pag. -kampos", "kampties"), //apkampties
		FirstConjRule.refl("-kaujos, -kaujies,", "-kaujas, pag. -kāvos", "kauties"), //atkauties
		FirstConjRule.refl("-kāpjos, -kāpies,", "-kāpjas, pag. -kāpos", "kāpties"), //atkāpties
		FirstConjRule.refl("-karos, -karies,", "-karas, pag. -kāros", "kārties"), //apkārties
		FirstConjRule.refl("-klājos, -klājies,", "-klājas, pag. -klājos", "klāties"), //apklāties
		FirstConjRule.refl("-kļaujos, -kļaujies,", "-kļaujas, pag. -kļāvos", "kļauties"), //apkļauties
		FirstConjRule.refl("-kniebjos, -kniebies,", "-kniebjas, pag. -kniebos", "kniebties"), //iekniebties
		FirstConjRule.refl("-kopjos, -kopies,", "-kopjas, pag. -kopos", "kopties"), //apkopties
		FirstConjRule.refl("-kožos, -kodies,", "-kožas, pag. -kodos", "kosties"), //atkosties
		FirstConjRule.refl("-kraujos, -kraujies,", "-kraujas, pag. -krāvos", "krauties"), //apkrauties
		FirstConjRule.refl("-krāpjos, -krāpies,", "-krāpjas, pag. -krāpos", "krāpties"), //apkrāpties
		FirstConjRule.refl("-krājos, -krājies,", "-krājas, pag. -krājos", "krāties"), //iekrāties
		FirstConjRule.refl("-krītos, -krīties,", "-krītas, pag. -kritos", "kristies"), //izkristies
		FirstConjRule.refl("-kuļos, -kulies,", "-kuļas, pag. -kūlos", "kulties"), //aizkulties
		// Ķ
		FirstConjRule.refl("-ķēros, -ķeries,", "-ķeras, pag. -ķēros", "ķerties"), //aizķerties
		// L
		FirstConjRule.refl("-laižos, -laidies,", "-laižas, pag. -laidos", "laisties"), //aizlaisties
		FirstConjRule.refl("-laužos, -lauzies,", "-laužas, pag. -lauzos", "lauzties"), //aizlauzties
		FirstConjRule.refl("-liedzos, -liedzies,", "-liedzas, pag. -liedzos", "liegties"), //aizliegties
		FirstConjRule.refl("-liecos, -liecies,", "-liecas, pag. -liecos", "liekties"), //aizliekties
		FirstConjRule.refl("-lejos, -lejies,", "-lejas, pag. -lējos", "lieties"), //aplieties
		FirstConjRule.refl("-liekos, -liecies,", "-liekas, pag. -likos", "likties"), //aizlikties
		FirstConjRule.refl("-lūdzos, -lūdzies,", "-lūdzas, pag. -lūdzos", "lūgties"), //atlūgties
		// Ļ
		FirstConjRule.refl("-ļaujos, -ļaujies,", "-ļaujas, pag. -ļāvos", "ļauties"), //atļauties
		// M
		FirstConjRule.refl("-maļos, -malies,", "-maļas, pag. -malos", "malties"), //apmalties
		FirstConjRule.refl("-maucos, -maucies,", "-maucas, pag. -maucos", "maukties"), //izmaukties
		FirstConjRule.refl("-mācos, -mācies,", "-mācas, pag. -mācos", "mākties"), //nomākties
		FirstConjRule.refl("-melšos, -melsies,", "-melšas, pag. -melsos", "melsties"), //izmelsties
		FirstConjRule.refl("-metos, -meties,", "-metas, pag. -metos", "mesties"), //aizmesties
		FirstConjRule.refl("-mērcos, -mērcies,", "-mērcas, pag. -mērcos", "mērkties"), //iemērkties
		FirstConjRule.refl("-mostos, -mosties,", "-mostas, pag. -modos", "mosties"), //atmosties
		// N
		FirstConjRule.refl("-nākos, -nācies,", "-nākas, pag. -nācos", "nākties"), //iznākties
		FirstConjRule.refl("-nesos, -nesies,", "-nesas, pag. -nesos", "nesties"), //aiznesties
		FirstConjRule.refl("-nirstos, -nirsties,", "-nirstas, pag. -niros", "nirties"), //ienirties
		FirstConjRule.refl("-nīkstos, -nīksties,", "-nīkstas, pag. -nīkos", "nīkties"), //iznīkties
		// Ņ
		FirstConjRule.refl("-ņemos, -ņemies,", "-ņemas, pag. -ņēmos", "ņemties"), //aizņemties
		// O, P
		FirstConjRule.refl("-peros, -peries,", "-peras, pag. -pēros", "pērties"), //aizpērties
		FirstConjRule.refl("-pērkos, -pērcies,", "-pērkas, pag. -pirkos", "pirkties"), // appirkties
		FirstConjRule.refl("-pinos, -pinies,", "-pinas, pag. -pinos", "pīties"), // iepīties
		FirstConjRule.refl("-plēšos, -plēsies,", "-plēšas, pag. -plēsos", "plēsties"), // izplēsties
		FirstConjRule.refl("-plūcos, -plūcies,", "-plūcas, pag. -plūcos", "plūkties"), // izplūkties
		FirstConjRule.refl("-pļaujos, -pļaujies,", "-pļaujas, pag. -pļāvos", "pļauties"), // appļauties
		FirstConjRule.refl("-pūšos, -pūties,", "-pūšas, pag. -pūtos", "pūsties"), // atpūsties
		// R
		FirstConjRule.refl("-rokos, -rocies,", "-rokas, pag. -rakos", "rakties"), // aizrakties
		FirstConjRule.refl("-rodos, -rodies,", "-rodas, pag. -rados", "rasties"), // atrasties
		FirstConjRule.refl("-raušos, -rausies,", "-raušas, pag. -rausos", "rausties"), //aizrausties
		FirstConjRule.refl("-raujos, -raujies,", "-raujas, pag. -rāvos", "rauties"), //aizrauties
		FirstConjRule.refl("-rāpjos, -rāpies,", "-rāpjas, pag. -rāpos", "rāpties"), // aizrāpties
		FirstConjRule.refl("-rājos, -rājies,", "-rājas, pag. -rājos", "rāties"), // izrāties
		FirstConjRule.refl("-redzos, -redzies,", "-redzas, pag. -redzējos", "redzēties"), // izredzēties
		FirstConjRule.refl("-riebjos, -riebies,", "-riebjas, pag. -riebos", "riebties"), //apriebties
		FirstConjRule.refl("-riebjos, -riebies,", "-riebjas; pag. -riebos", "riebties"), //aizriebties
		FirstConjRule.refl("-rejos, -rejies,", "-rejas, pag. -rējos", "rieties"), //atrieties
		FirstConjRule.refl("-rimstos, -rimsties,", "-rimstas, pag. -rimos", "rimties"), //aprimties
		FirstConjRule.refl("-rijos, -rijies,", "-rijas, pag. -rijos", "rīties"), //aizrīties
		// S
		FirstConjRule.refl("-saucos, -saucies,", "-saucas, pag. -saucos", "saukties"), //aizsaukties
		FirstConjRule.refl("-sedzos, -sedzies,", "-sedzas, pag. -sedzos", "segties"), //aizsegties
		FirstConjRule.refl("-sēršos, -sērsies,", "-sēršas, pag. -sērsos", "sērsties"), //izsērsties
		FirstConjRule.refl("-sējos, -sējies,", "-sējas, pag. -sējos", "sēties"), //izsēties
		FirstConjRule.refl("-sienos, -sienies,", "-sienas, pag. -sējos", "sieties"), //aizsieties
		FirstConjRule.refl("-sitos, -sities,", "-sitas, pag. -sitos", "sisties"), //aizsisties
		FirstConjRule.refl("-skaišos, -skaisties,", "-skaišas, pag. -skaitos", "skaisties"), //apskaisties
		FirstConjRule.refl("-skaujos, -skaujies,", "-skaujas, pag. -skāvos", "skauties"), //apskauties
		FirstConjRule.refl("-slēdzos, -slēdzies,", "-slēdzas, pag. -slēdzos", "slēgties"), //ieslēgties
		FirstConjRule.refl("-slēpjos, -slēpies,", "-slēpjas, pag. -slēpos", "slēpties"), //aizslēpties
		FirstConjRule.refl("-sliecos, -sliecies,", "-sliecas, pag. -sliecos", "sliekties"), //aizsliekties
		FirstConjRule.refl("-smeļos, -smelies,", "-smeļas, pag. -smēlos", "smelties"), //iesmelties
		FirstConjRule.refl("-smejos, -smejies,", "-smejas, pag. -smējos", "smieties"), //atsmieties
		FirstConjRule.refl("-sniedzos, -sniedzies,", "-sniedzas, pag. -sniedzos", "sniegties"), //aizsniegties
		FirstConjRule.refl("-speros, -speries,", "-speras, pag. -spēros", "sperties"), //atsperties
		FirstConjRule.refl("-spiežos, -spiedies,", "-spiežas, pag. -spiedos", "spiesties"), //aizspiesties
		FirstConjRule.refl("-spraucos, -spraucies,", "-spraucas, pag. -spraucos", "spraukties"), //aizspraukties
		FirstConjRule.refl("-spraužos, -spraudies,", "-spraužas, pag. -spraudos", "sprausties"), //aizsprausties
		FirstConjRule.refl("-spriežos, -spriedies,", "-spriežas, pag. -spriedos", "spriesties"), //aizspriesties
		FirstConjRule.refl("-stājos, -stājies,", "-stājas, pag. -stājos", "stāties"), //aizstāties
		FirstConjRule.refl("-steidzos, -steidzies,", "-steidzas, pag. -steidzos", "steigties"), //aizsteigties
		FirstConjRule.refl("-stiepjos, -stiepies,", "-stiepjas, pag. -stiepos", "stiepties"), //aizstiepties
		FirstConjRule.refl("-stumjos, -stumies,", "-stumjas, pag. -stūmos", "stumties"), //aizstumties
		FirstConjRule.refl("-svempjos, -svempies,", "-svempjas, pag. -svempos", "svempties"), //iesvempties
		FirstConjRule.refl("-sveros, -sveries,", "-sveras, pag. -svēros", "svērties"), //apsvērties
		FirstConjRule.refl("-sviežos, -sviedies,", "-sviežas, pag. -sviedos", "sviesties"), //aizsviesties
		// Š
		FirstConjRule.refl("-šaujos, -šaujies,", "-šaujas, pag. -šāvos", "šauties"), //aizšauties
		FirstConjRule.refl("-šķeļos, -šķelies,", "-šķeļas, pag. -šķēlos", "šķelties"), //atšķelties
		FirstConjRule.refl("-šķiežos, -šķiedies,", "-šķiežas, pag. -šķiedos", "šķiesties"), //apsķiesties
		FirstConjRule.refl("-šķiros, -šķiries,", "-šķiras, pag. -šķīros", "šķirties"), //atšķirties
		FirstConjRule.refl("-šļācos, -šļācies,", "-šļācas, pag. -šļācos", "šļākties"), //apšļākties
		FirstConjRule.refl("-šmaucos, -šmaucies,", "-šmaucas, pag. -šmaucos", "šmaukties"), //apšmaukties
		FirstConjRule.refl("-šujos, -šujies,", "-šujas, pag. -šuvos", "šūties"), //izšūties
		// T
		FirstConjRule.refl("-tērpjos, -tērpies,", "-tērpjas, pag. -tērpos", "tērpties"), //aptērpties
		FirstConjRule.refl("-tiecos, -tiecies,", "-tiecas, pag. -tiecos", "tiekties"), //ietiekties
		FirstConjRule.refl("-tiepjos, -tiepies,", "-tiepjas, pag. -tiepos", "tiepties"), //ietiepties
		FirstConjRule.refl("-tinos, -tinies,", "-tinas, pag. -tinos", "tīties"), //aiztīties
		FirstConjRule.refl("-traucos, -traucies,", "-traucas, pag. -traucos", "traukties"), //aiztraukties
		FirstConjRule.refl("-traušos, -trausies,", "-traušas, pag. -trausos", "trausties"), //ietrausties
		FirstConjRule.refl("-triecos, -triecies,", "-triecas, pag. -triecos", "triekties"), //attriekties
		FirstConjRule.refl("-triepjos, -triepies,", "-triepjas, pag. -triepos", "triepties"), //aptriepties
		FirstConjRule.refl("-trūkstos, -trūksties,", "-trūkstas, pag. -trūkos", "trūkties"), //iztrūkties
		FirstConjRule.refl("-tupstos, -tupsties,", "-tupstas, pag. -tupos", "tupties"), //aiztupties
		FirstConjRule.refl("-tveros, -tveries,", "-tveras, pag. -tvēros", "tverties"), //ietverties
		// U
		FirstConjRule.refl("-urbjos, -urbies,", "-urbjas, pag. -urbos", "urbties"), //aizurbties
		// V
		FirstConjRule.refl("-vācos, -vācies,", "-vācas, pag. -vācos", "vākties"), //aizvākties
		FirstConjRule.refl("-veļos, -velies,", "-veļas, pag. -vēlos", "velties"), //aizvelties
		FirstConjRule.refl("-vedos, -vedies,", "-vedas, pag. -vedos", "vesties"), //izvesties
		FirstConjRule.refl("-vērpjos, -vērpies,", "-vērpjas, pag. -vērpos", "vērpties"), //apvērpties
		FirstConjRule.refl("-velkos, -velcies,", "-velkas, pag. -vilkos", "vilkties"), //aizvilkties
		FirstConjRule.refl("-vijos, -vijies,", "-vijas, pag. -vijos", "vīties"), //apvīties
		// Z
		FirstConjRule.refl("-zogos, -zodzies,", "-zogas, pag. -zagos", "zagties"), //aizzagties
		FirstConjRule.refl("-ziežos, -ziedies,", "-ziežas, pag. -ziedos", "ziesties"), //apziesties
		FirstConjRule.refl("-zīžos, -zīdies,", "-zīžas, pag. -zīdos", "zīsties"), //iezīsties
		FirstConjRule.refl("-zveļos, -zvelies,", "-zveļas, pag. -zvēlos", "zvelties"), //atzvelties
		// Ž
		FirstConjRule.refl("-žaujos, -žaujies,", "-žaujas, pag. -žāvos", "žauties"), //iežauties
		FirstConjRule.refl("-žņaudzos, -žņaudzies,", "-žņaudzas, pag. -žņaudzos", "žņaugties"), //iežņaugties

		// Vairāki lemmas varianti.
		FirstConjRule.reflMultiLemma("-lecos, -lecies,", "-lecas, pag. -lēcos",
				new String[]{"lēkties", "lekties"}), //atlēkties, izlekties


		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas
		FirstConjRule.refl3PersHomof("-tīkas, pag. -tikās", "tikties",
				"\"tikties\" (patikties kādam)"), //patikties
		// Paralēlās formas.
		FirstConjRule.refl3PersParallel("-plešas, pag. -pletās, arī -plētās", "plesties"), //aizplesties

		// Pilnīgs nestandarts.
		FirstConjRule.of("-teicos, -teicies,", "-teicas (tagadnes formas parasti nelieto), pag. -teicos", "teikties", 15,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"teikties\"")},
				new Tuple[]{Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.NOT_PRESENT_FORMS.s)},
				new String[]{"teik"}, new String[]{"teic"}, new String[]{"teic"}), //atteikties
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
	 */
	public static final Rule[] reflThirdConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		BaseRule.thirdConjReflAllPersParallel(
				"-ļogos, -ļogies, -ļogās, retāk -ļodzos, -ļodzies, -ļodzās, pag. -ļodzījos", "ļodzīties"), //noļodzīties
		BaseRule.thirdConjReflAllPersParallel(
				"-mokos, -mokies, -mokās, arī -mocos, -mocies, -mocās, pag. -mocījos", "mocīties"), //aizmocīties

		// Standartizētie.
		// A, B
		RegularVerbRule.thirdConjRefl(
				"-braukos, -braukies,", "-braukās, pag. -braucījos", "braucīties", true), //atbraucīties
		// C, Č
		RegularVerbRule.thirdConjRefl(
				"-čabinos, -čabinies,", "-čabinās, pag. -čabinājos", "čabināties", false), //iečabināties
		RegularVerbRule.thirdConjRefl(
				"-čukstos, -čuksties,", "-čukstas, pag. -čukstējos", "čukstēties", false), //iečukstēties
		// D
		RegularVerbRule.thirdConjRefl(
				"-deros, -deries,", "-deras, pag. -derējos", "derēties", false), //iederēties
		// E, F, G
		RegularVerbRule.thirdConjRefl(
				"-gribos, -gribies,", "-gribas, pag. -gribējos", "gribēties", false), //izgribēties
		// H, I, J, K
		RegularVerbRule.thirdConjRefl(
				"-kustos, -kusties,", "-kustas, pag. -kustējos", "kustēties", false), //aizkustēties
		// L
		RegularVerbRule.thirdConjRefl(
				"-lāpos, -lāpies,", "-lāpās, pag. -lāpījos", "lāpīties", false), //aplāpīties
		RegularVerbRule.thirdConjRefl(
				"-lokos, -lokies,", "-lokās, pag. -locījos", "locīties", true), //atlocīties
		// Ļ
		RegularVerbRule.thirdConjRefl(
				"-ļogos, -ļogies,", "-ļogās, pag. -ļodzījos", "ļodzīties", true), //izļodzīties
		// M
		RegularVerbRule.thirdConjRefl(
				"-minos, -minies,", "-minas, pag. -minējos", "minēties", false), //atminēties
		RegularVerbRule.thirdConjRefl(
				"-mīlos, -mīlies,", "-mīlas, pag. -mīlējos", "mīlēties", false), //iemīlēties
		RegularVerbRule.thirdConjRefl(
				"-muldos, -muldies,", "-muldas, pag. -muldējos", "muldēties", false), //iemuldēties 1, 2
		// N, O, P
		RegularVerbRule.thirdConjRefl(
				"-peldos, -peldies,", "-peldas, pag. -peldējos", "peldēties", false), //izpeldēties
		RegularVerbRule.thirdConjRefl(
				"-precos, -precies,", "-precas, pag. -precējos", "precēties", false), //aizprecēties
		// R
		RegularVerbRule.thirdConjRefl(
				"-raugos, -raugies,", "-raugās, pag. -raudzījos", "raudzīties", true), //apraudzīties
		// S
		RegularVerbRule.thirdConjRefl(
				"-sakos, -sakies,", "-sakās, pag. -sacījos", "sacīties", true), //atsacīties
		RegularVerbRule.thirdConjRefl(
				"-slakos, -slakies,", "-slakās, pag. -slacījos", "slacīties", true), //apslacīties
		RegularVerbRule.thirdConjRefl(
				"-slaukos, -slaukies,", "-slaukās, pag. -slaucījos", "slaucīties", true), //apslaucīties
		RegularVerbRule.thirdConjRefl(
				"-slogos, -slogies,", "-slogās, pag. -slodzījos", "slodzīties", true), //ieslodzīties
		RegularVerbRule.thirdConjRefl(
				"-strīdos, -strīdies,", "-strīdas, pag. -strīdējos", "strīdēties", false), //izstrīdēties
		// Š
		RegularVerbRule.thirdConjRefl(
				"-šļaukos, -šļaukies,", "-šļaukās, pag. -šļaucījos", "šļaucīties", true), //izšļaucīties
		// T
		RegularVerbRule.thirdConjRefl(
				"-tekos, -tecies,", "-tekas, pag. -tecējos", "tecēties", true), //iztecēties
		RegularVerbRule.thirdConjRefl(
				"-turos, -turies,", "-turas, pag. -turējos", "turēties", false), //atturēties
		// U, V
		RegularVerbRule.thirdConjRefl(
				"-vēlos, -vēlies,", "-vēlas, pag. -vēlējos", "vēlēties", false), //atvēlēties
		// Z
		RegularVerbRule.thirdConjRefl(
				"-zinos, -zinies,", "-zinās, pag. -zinājos", "zināties", false), //apzināties
		RegularVerbRule.thirdConjRefl(
				"-zvēros, -zvēries,", "-zvēras, pag. -zvērējos", "zvērēties", false), //izzvērēties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		RegularVerbRule.thirdConjRefl3Pers("-lokās, pag. -locījās", "locīties", true), //aizlocīties

	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] reflMultiConjVerb = {
		// Likumi, kam ir visu formu variants.
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-gailējos, -gailējies, -gailējās, arī -gailos, -gailies, -gailas, pag. -gailējos",
				".*gailēties", false), // iegailēties
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-kristījos, -kristījies, -kristījas, arī -kristos, -kristies, -kristās, pag. -kristījos",
				".*kristīties", false), // nokristīties
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-krustījos, -krustījies, -krustījas, arī -krustos, -krusties, -krustās, pag. -krustījos",
				".*krustīties", false), // nokrustīties
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-ķēzījos, -ķēzījies, -ķēzījas, arī -ķēzos, -ķēzies, -ķēzās, pag. -ķēzījos",
				".*ķēzīties", false), //noķēzīties
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-mērījos, -mērījies, -mērījas, arī -mēros, -mēries, -mērās, pag. -mērījos",
				".*mērīties", false), // izmērīties
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-pelnos, -pelnies, -pelnās, arī -pelnījos, -pelnījies, -pelnījās, pag. -pelnījos",
				".*pelnīties", false), //izpelnīties
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-sargājos, -sargājies, -sargājas, arī -sargos, -sargies, -sargās, pag. -sargājos",
				".*sargāties", false), // aizsargāties
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-svētījos, -svētījies, -svētījas, arī -svētos, -svēties, -svētās, pag. -svētījos",
				".*svētīties", false), // iesvētīties
		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nav.
	};

}
