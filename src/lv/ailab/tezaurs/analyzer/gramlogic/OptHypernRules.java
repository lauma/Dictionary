package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.utils.Trio;
import lv.ailab.tezaurs.utils.Tuple;

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
				new Tuple[] {Features.POS__NOUN}, new Tuple[] {Features.GENDER__FEM}), //uzacs, acs
		SimpleRule.of("-krāsns, dsk. ģen. -krāšņu, s.", ".*krāsns", 11,
				new Tuple[] {Features.POS__NOUN}, new Tuple[] {Features.GENDER__FEM}), //aizkrāsns
		SimpleRule.of("-valsts, dsk. ģen. -valstu, s.", ".*valsts", 11,
				new Tuple[] {Features.POS__NOUN}, new Tuple[] {Features.GENDER__FEM}), //agrārvalsts

		/* Paradigm 25: Pronouns
		 */
		SimpleRule.of("ģen. -kā, dat. -kam, akuz., instr. -ko", ".*kas", 25,
				new Tuple[] {Features.POS__PRONOUN, Tuple.of(Keys.INFLECT_AS, "kas")},
				null), //daudzkas
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
		// Likumi, kam ir visu formu variants.
		// Netoteiksmes homoformas.
		VerbRule.of("-aužu, -aud,", "-auž, pag. -audu", "aust", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"aust\" (kā audumu)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizaust 2
		VerbRule.of("-dedzu, -dedz,", "-dedz, pag. -dedzu", "degt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"degt\" (kādu citu)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizdegt 1
		VerbRule.of("-degu, -dedz,", "-deg, pag. -degu", "degt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"degt\" (pašam)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //apdegt, aizdegt 2
		VerbRule.of("-dzenu, -dzen,", "-dzen, pag. -dzinu", "dzīt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"dzīt\" (kā lopus)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizdzīt 1
		VerbRule.of("-iru, -ir,", "-ir, pag. -īru", "irt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"irt\" (kā ar airiem)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizirt 1
		VerbRule.of("-lienu, -lien,", "-lien, pag. -līdu", "līst", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"līst\" (kā zem galda)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizlīst, ielīst 1
		VerbRule.of("-līžu, -līd,", "-līž, pag. -līdu", "līst", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"līst\" (kā līdumu)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //ielīst 2
		VerbRule.of("-mītu, -mīti,", "-mīt, pag. -mitu", "mist", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"mist\" (kā mitināties)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //iemist, izmist 2
		VerbRule.of("-mistu, -misti,", "-mist, pag. -misu", "mist", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"mist\" (kā krist izmisumā)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //izmist 1
		VerbRule.of("-minu, -min,", "-min, pag. -minu", "mīt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"mīt\" (kā pedāļus)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizmīt 1
		VerbRule.of("-miju, -mij,", "-mij, pag. -miju", "mīt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"mīt\" (kā naudu)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizmīt 2
		VerbRule.of("-tieku, -tiec,", "-tiek, pag. -tiku", "tikt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"tikt\" (kā nokļūt kaut kur)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aiztikt 1, 2
		VerbRule.of("-tīku, -tīc,", "-tīk, pag. -tiku", "tikt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"tikt\" (kā patikt kādam)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //patikt
		// Izņēmuma izņēmums :/
		VerbRule.of("-patīku, -patīc,", "-patīk, pag. -patiku", "patikt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"tikt\" (kā patikt kādam)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //patikt

			// Paralēlās formas.
		SimpleRule.of("-auju, -auj, -auj, arī -aunu, -aun, -aun, pag. -āvu", ".*aut", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"aut\""),
						Features.PARALLEL_FORMS},
				null), //apaut
		SimpleRule.of("-gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu", ".*gult", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"gult\""),
						Features.PARALLEL_FORMS},
				null), //aizgult
		SimpleRule.of("-jaušu, -jaut, -jauš, pag. -jautu, arī -jaužu, -jaud, -jauž, pag. -jaudu", ".*jaust", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"jaust\""),
						Features.PARALLEL_FORMS},
				null), //apjaust
		SimpleRule.of("-jumju, -jum, -jumj, pag. -jūmu, arī -jumu", ".*jumt", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"jumt\""),
						Features.PARALLEL_FORMS},
				null), //aizjumt
		SimpleRule.of("-kaistu, -kaisti, -kaist, pag. -kaisu, retāk -kaitu", ".*kaist", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"kaist\""),
						Features.PARALLEL_FORMS},
				null), //iekaist
		SimpleRule.of("-meju, -mej, -mej, pag. -mēju, arī -mienu, -mien, -mien, pag. -mēju", ".*miet", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"miet\""),
						Features.PARALLEL_FORMS},
				null), //iemiet
		SimpleRule.of("-plešu, -plet, -pleš, pag. -pletu, arī -plētu", ".*plest", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"plest\""),
						Features.PARALLEL_FORMS},
				null), //aizplest
		SimpleRule.of("-skārstu, -skārsti, -skārst, arī -skāršu, -skārt, -skārš, pag. -skārtu", ".*skārst", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"skārst\""),
						Features.PARALLEL_FORMS},
				null), //apskārst
		SimpleRule.of("-skrienu, -skrien, -skrien, arī -skreju, -skrej, -skrej, pag. -skrēju", ".*skriet", 15,
				new Tuple[] {Features.POS__VERB,
						Tuple.of(Keys.INFLECT_AS, "\"skriet\""),
						Features.PARALLEL_FORMS},
				null), //aizskriet
		SimpleRule.of("-slapstu, -slapsti, -slapst, retāk -slopu, -slopi, -slop, pag. -slapu", ".*slapt", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"slapt\""),
						Features.PARALLEL_FORMS},
				null), //apslapt
		SimpleRule.of("-slienu, -slien, -slien, arī -sleju, -slej, -slej, pag. -sleju", ".*sliet", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"sliet\""),
						Features.PARALLEL_FORMS},
				null), //aizsliet
		SimpleRule.of("-spurdzu, -spurdz, -spurdz, arī -spurgst, pag. -spurdzu", ".*spurgt", 15,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"spurgt\""),
						Features.PARALLEL_FORMS},
				null), //aizspurgt

		// Izņēmums.
		VerbRule.of("-pazīstu, -pazīsti,", "-pazīst, pag. -pazinu", "pazīt", 15,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"zīt\"")}, null), //atpazīt

		// Standartizētie.
		// A
		VerbRule.firstConjDir("-aru, -ar,", "-ar, pag. -aru", "art"), //aizart
		VerbRule.firstConjDir("-augu, -audz,", "-aug, pag. -augu", "augt"), //ieaugt, aizaugt
		// B
		VerbRule.firstConjDir("-baru, -bar,", "-bar, pag. -bāru", "bārt"), //iebārt
		VerbRule.firstConjDir("-bāžu, -bāz,", "-bāž, pag. -bāzu", "bāzt"), //aizbāzt
		VerbRule.firstConjDir("-belžu, -belz,", "-belž, pag. -belzu", "belzt"), //iebelzt
		VerbRule.firstConjDir("-beržu, -berz,", "-berž, pag. -berzu", "berzt"), //atberzt
		VerbRule.firstConjDir("-bēgu, -bēdz,", "-bēg, pag. -bēgu", "bēgt"), //aizbēgt
		VerbRule.firstConjDir("-beru, -ber,", "-ber, pag. -bēru", "bērt"), //aizbērt
		VerbRule.firstConjDir("-bilstu, -bilsti,", "-bilst, pag. -bildu", "bilst"), //aizbilst
		VerbRule.firstConjDir("-birstu, -birsti,", "-birst, pag. -biru", "birt"), //apbirt, aizbirt
		VerbRule.firstConjDir("-bliežu, -bliez,", "-bliež, pag. -bliezu", "bliezt"), //iebliezt
		VerbRule.firstConjDir("-bļauju, -bļauj,", "-bļauj, pag. -bļāvu", "bļaut"), //atbļaut
		VerbRule.firstConjDir("-braucu, -brauc,", "-brauc, pag. -braucu", "braukt"), //aizbraukt
		VerbRule.firstConjDir("-brāžu, -brāz,", "-brāž, pag. -brāzu", "brāzt"), //aizbrāzt
		VerbRule.firstConjDir("-brienu, -brien,", "-brien, pag. -bridu", "brist"), //aizbrist
		VerbRule.firstConjDir("-brēcu, -brēc,", "-brēc, pag. -brēcu", "brēkt"), //atbrēkt
		VerbRule.firstConjDir("-brūku, -brūc,", "-brūk, pag. -bruku", "brukt"), //iebrukt
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
		VerbRule.firstConjDir("-dobju, -dob,", "-dobj, pag. -dobu", "dobt"), //iedobt
		VerbRule.firstConjDir("-dodu, -dod,", "-dod, pag. -devu", "dot"), //aizdot
		VerbRule.firstConjDir("-drāžu, -drāz,", "-drāž, pag. -drāzu", "drāzt"), //aizdrāzt
		VerbRule.firstConjDir("-duru, -dur,", "-dur, pag. -dūru", "durt"), //aizdurt
		VerbRule.firstConjDir("-dūcu, -dūc,", "-dūc, pag. -dūcu", "dūkt"), //aizdūkt
		VerbRule.firstConjDir("-dvešu, -dves,", "-dveš, pag. -dvesu", "dvest"), //apdvest
		VerbRule.firstConjDir("-dzeļu, -dzel,", "-dzeļ, pag. -dzēlu", "dzelt"), //atdzelt, aizdzelt
		VerbRule.firstConjDir("-dzeru, -dzer,", "-dzer, pag. -dzēru", "dzert"), //aizdzert
		VerbRule.firstConjDir("-dzēšu, -dzēs,", "-dzēš, pag. -dzēsu", "dzēst"), //apdzēst
		VerbRule.firstConjDir("-dzimstu, -dzimsti,", "-dzimst, pag. -dzimu", "dzimt"), //atdzimt
		VerbRule.firstConjDir("-dziestu, -dziesti,", "-dziest, pag. -dzisu", "dzist"), //atdzist
		// E
		VerbRule.firstConjDir("-elšu, -els,", "-elš, pag. -elsu", "elst"), //atelst
		// Ē
		VerbRule.firstConjDir("-ēdu, -ēd,", "-ēd, pag. -ēdu", "ēst"), //aizēst
		// F, G
		VerbRule.firstConjDir("-gāžu, -gāz,", "-gāž, pag. -gāzu", "gāzt"), //aizgāzt
		VerbRule.firstConjDir("-glaužu, -glaud,", "-glauž, pag. -glaudu", "glaust"), //aizglaust
		VerbRule.firstConjDir("-grauju, -grauj,", "-grauj, pag. -grāvu", "graut"), //iegraut
		VerbRule.firstConjDir("-graužu, -grauz,", "-grauž, pag. -grauzu", "grauzt"), //aizgrauzt
		VerbRule.firstConjDir("-grābju, -grāb,", "-grābj, pag. -grābu", "grābt"), //aizgrābt
		VerbRule.firstConjDir("-grebju, -greb,", "-grebj, pag. -grebu", "grebt"), //apgrebt
		VerbRule.firstConjDir("-griežu, -griez,", "-griež, pag. -griezu", "griezt"), //aizgriezt 1, 2
		VerbRule.firstConjDir("-grimstu, -grimsti,", "-grimst, pag. -grimu", "grimt"), //atgrimt, aizgrimt
		VerbRule.firstConjDir("-grūžu, -grūd,", "-grūž, pag. -grūdu", "grūst"), //aizgrūst
		VerbRule.firstConjDir("-grūstu, -grūsti,", "-grūst, pag. -gruvu", "grūt"), //iegrūt
		VerbRule.firstConjDir("-gumstu, -gumsti,", "-gumst, pag. -gumu", "gumt"), //iegumt
		VerbRule.firstConjDir("-gurstu, -gursti,", "-gurst, pag. -guru", "gurt"), //apgurt
		VerbRule.firstConjDir("-gūstu, -gūsti,", "-gūst, pag. -guvu", "gūt"), //aizgūt
		// Ģ
		VerbRule.firstConjDir("-ģērbju, -ģērb,", "-ģērbj, pag. -ģērbu", "ģērbt"), //apģist
		VerbRule.firstConjDir("-ģiedu, -ģied,", "-ģied, pag. -ģidu", "ģist"), //apģist
		// H, I
		VerbRule.firstConjDir("-eju, -ej,", "-iet, pag. -gāju", "iet"), //apiet
		VerbRule.firstConjDir("-iežu, -iez,", "-iež, pag. -iezu", "iezt"), //atiezt
		// J
		VerbRule.firstConjDir("-jaucu, -jauc,", "-jauc, pag. -jaucu", "jaukt"), //iejaukt
		VerbRule.firstConjDir("-jauju, -jauj,", "-jauj, pag. -jāvu", "jaut"), //iejaut
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
		VerbRule.firstConjDir("-kašu, -kas,", "-kaš, pag. -kasu", "kast"), //iekast
		VerbRule.firstConjDir("-kaucu, -kauc,", "-kauc, pag. -kaucu", "kaukt"), //izkaukt, aizkaukt
		VerbRule.firstConjDir("-kauju, -kauj,", "-kauj, pag. -kāvu", "kaut"), //apkaut
		VerbRule.firstConjDir("-kāpju, -kāp,", "-kāpj, pag. -kāpu", "kāpt"), //aizkāpt
		VerbRule.firstConjDir("-karu, -kar,", "-kar, pag. -kāru", "kārt"), //aizkārt
		VerbRule.firstConjDir("-kāšu, -kās,", "-kāš, pag. -kāsu", "kāst"), //iekāst
		VerbRule.firstConjDir("-klāju, -klāj,", "-klāj, pag. -klāju", "klāt"), //apklāt
		VerbRule.firstConjDir("-kliedzu, -kliedz,", "-kliedz, pag. -kliedzu", "kliegt"), //aizkliegt
		VerbRule.firstConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimtu", "klimst"), //aizklimst
		VerbRule.firstConjDir("-klīstu, -klīsti,", "-klīst, pag. -klīdu", "klīst"), //aizklīst
		VerbRule.firstConjDir("-klūpu, -klūpi,", "-klūp, pag. -klupu", "klupt"), //ieklupt
		VerbRule.firstConjDir("-klustu, -klusti,", "-klust, pag. -klusu", "klust"), //apklust
		VerbRule.firstConjDir("-kļauju, -kļauj,", "-kļauj, pag. -kļāvu", "kļaut"), //apkļaut
		VerbRule.firstConjDir("-kļūstu, -kļūsti,", "-kļūst, pag. -kļuvu", "kļūt"), //aizkļūt
		VerbRule.firstConjDir("-knābju, -knāb,", "-knābj, pag. -knābu", "knābt"), //aizknābt
		VerbRule.firstConjDir("-kniebju, -knieb,", "-kniebj, pag. -kniebu", "kniebt"), //apkniebt
		VerbRule.firstConjDir("-knūpu, -knūpi,", "-knūp, pag. -knupu", "knupt"), //ieknupt
		VerbRule.firstConjDir("-kņupu, -kņupi,", "-kņūp, pag. -kņupu", "kņupt"), //iekņupt
		VerbRule.firstConjDir("-kopju, -kop,", "-kopj, pag. -kopu", "kopt"), //apkopt
		VerbRule.firstConjDir("-kožu, -kod,", "-kož, pag. -kodu", "kost"), //aizkost
		VerbRule.firstConjDir("-krāpju, -krāp,", "-krāpj, pag. -krāpu", "krāpt"), //aizkrāpt
		VerbRule.firstConjDir("-krāju, -krāj,", "-krāj, pag. -krāju", "krāt"), //iekrāt
		VerbRule.firstConjDir("-krauju, -krauj,", "-krauj, pag. -krāvu", "kraut"), //aizkraut
		VerbRule.firstConjDir("-kremtu, -kremt,", "-kremt, pag. -krimtu", "krimst"), //apkrimst
		VerbRule.firstConjDir("-krītu, -krīti,", "-krīt, pag. -kritu", "krist"), //aizkrist
		VerbRule.firstConjDir("-kuļu, -kul,", "-kuļ, pag. -kūlu", "kult"), //apkult
		VerbRule.firstConjDir("-kuru, -kur,", "-kur, pag. -kūru", "kurt"), //aizkurt
		VerbRule.firstConjDir("-kūstu, -kūsti,", "-kūst, pag. -kusu", "kust"), //aizkust
		VerbRule.firstConjDir("-kūpu, -kūpi,", "-kūp, pag. -kūpu", "kūpt"), //apkūpt
		VerbRule.firstConjDir("-kvēpstu, -kvēpsti,", "-kvēpst, pag. -kvēpu", "kvēpt"), //apkvēpt, aizkvēpt
		// Ķ
		VerbRule.firstConjDir("-ķepu, -ķep,", "-ķep, pag. -ķepu", "ķept"), //apķept, aizķept
		VerbRule.firstConjDir("-ķeru, -ķer,", "-ķer, pag. -ķēru", "ķert"), //aizķert
		VerbRule.firstConjDir("-ķērcu, -ķērc,", "-ķērc, pag. -ķērcu", "ķērkt"), //atķērkt
		// L
		VerbRule.firstConjDir("-labstu, -labsti,", "-labst, pag. -labu", "labt"), //atlabt
		VerbRule.firstConjDir("-laižu, -laid,", "-laiž, pag. -laidu", "laist"), //aizlaist
		VerbRule.firstConjDir("-loku, -loc,", "-lok, pag. -laku", "lakt"), //ielakt
		VerbRule.firstConjDir("-laužu, -lauz,", "-lauž, pag. -lauzu", "lauzt"), //aizlauzt
		VerbRule.firstConjDir("-lencu, -lenc,", "-lenc, pag. -lencu", "lenkt"), //aplenkt
		VerbRule.firstConjDir("-lecu, -lec,", "-lec, pag. -lēcu", "lēkt"), //aizlēkt
		VerbRule.firstConjDir("-lēšu, -lēs,", "-lēš, pag. -lēsu", "lēst"), //aplēst
		VerbRule.firstConjDir("-liedzu, -liedz,", "-liedz, pag. -liedzu", "liegt"), //aizliegt
		VerbRule.firstConjDir("-liecu, -liec,", "-liec, pag. -liecu", "liekt"), //aizliekt
		VerbRule.firstConjDir("-leju, -lej,", "-lej, pag. -lēju", "liet"), //aizliet
		VerbRule.firstConjDir("-lieku, -liec,", "-liek, pag. -liku", "likt"), //aizlikt
		VerbRule.firstConjDir("-līpu, -līpi,", "-līp, pag. -lipu", "lipt"), //aplipt, aizlipt
		VerbRule.firstConjDir("-līgstu, -līgsti,", "-līgst, pag. -līgu", "līgt"), //atlīgt
		VerbRule.firstConjDir("-līkstu, -līksti,", "-līkst, pag. -līku", "līkt"), //nolīkt, aizlīkt
		VerbRule.firstConjDir("-līstu, -līsti,", "-līst, pag. -liju", "līt"), //aplīt, aizlīt
		VerbRule.firstConjDir("-lobju, -lob,", "-lobj, pag. -lobu", "lobt"), //aizlobt
		VerbRule.firstConjDir("-lūdzu, -lūdz,", "-lūdz, pag. -lūdzu", "lūgt"), //aizlūgt
		VerbRule.firstConjDir("-lūstu, -lūsti,", "-lūst, pag. -lūzu", "lūzt"), //aizlūzt, aizlūzt
		// Ļ
		VerbRule.firstConjDir("-ļauju, -ļauj,", "-ļauj, pag. -ļāvu", "ļaut"), //atļaut
		// M
		VerbRule.firstConjDir("-maigstu, -maigsti,", "-maigst, pag. -maigu", "maigt"), //atmaigt
		VerbRule.firstConjDir("-maļu, -mal,", "-maļ, pag. -malu", "malt"), //apmalt
		VerbRule.firstConjDir("-maucu, -mauc,", "-mauc, pag. -maucu", "maukt"), //iemaukt
		VerbRule.firstConjDir("-mauju, -mauj,", "-mauj, pag. -māvu", "maut"), //iemaut
		VerbRule.firstConjDir("-māju, -māj,", "-māj, pag. -māju", "māt"), //atmāt
		VerbRule.firstConjDir("-melšu, -mels,", "-melš, pag. -melsu", "melst"), //iemelst
		VerbRule.firstConjDir("-metu, -met,", "-met, pag. -metu", "mest"), //aizmest
		VerbRule.firstConjDir("-mērcu, -mērc,", "-mērc, pag. -mērcu", "mērkt"), //iemērkt
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
		VerbRule.firstConjDir("-nīstu, -nīsti,", "-nīst, pag. -nīdu", "nīst"), //ienīst
		// Ņ
		VerbRule.firstConjDir("-ņemu, -ņem,", "-ņem, pag. -ņēmu", "ņemt"), //aizņemt
		VerbRule.firstConjDir("-ņirdzu, -ņirdz,", "-ņirdz, pag. -ņirdzu", "ņirgt"), //apņirgt
		// O
		VerbRule.firstConjDir("-ožu, -od,", "-ož, pag. -odu", "ost"), //apost
		// P
		VerbRule.firstConjDir("-pampstu, -pampsti,", "-pampst, pag. -pampu", "pampt"), //aizpampt
		VerbRule.firstConjDir("-paužu, -paud,", "-pauž, pag. -paudu", "paust"), //iepaust
		VerbRule.firstConjDir("-peru, -per,", "-per, pag. -pēru", "pērt"), //appērt
		VerbRule.firstConjDir("-pērku, -pērc,", "-pērk, pag. -pirku", "pirkt"), //appirkt
		VerbRule.firstConjDir("-pīkstu, -pīksti,", "-pīkst, pag. -pīku", "pīkt"), //iepīkt
		VerbRule.firstConjDir("-pinu, -pin,", "-pin, pag. -pinu", "pīt"), //aizpīt
		VerbRule.firstConjDir("-ploku, -ploc,", "-plok, pag. -plaku", "plakt"), //aizplakt
		VerbRule.firstConjDir("-ploku, -ploc,", "-plok; pag. -plaku", "plakt"), //ieplakt
		VerbRule.firstConjDir("-plaukstu, -plauksti,", "-plaukst, pag. -plauku", "plaukt"), //atplaukt, aizplaukt
		VerbRule.firstConjDir("-plēšu, -plēs,", "-plēš, pag. -plēsu", "plēst"), //aizplēst
		VerbRule.firstConjDir("-plīstu, -plīsti,", "-plīst, pag. -plīsu", "plīst"), //applīst, aizplīst
		VerbRule.firstConjDir("-plūcu, -plūc,", "-plūc, pag. -plūcu", "plūkt"), //aizplūkt
		VerbRule.firstConjDir("-plūstu, -plūsti,", "-plūst, pag. -plūdu", "plūst"), //applūst, aizplūst
		VerbRule.firstConjDir("-pļauju, -pļauj,", "-pļauj, pag. -pļāvu", "pļaut"), //aizpļaut
		VerbRule.firstConjDir("-pūšu, -pūt,", "-pūš, pag. -pūtu", "pūst"), //aizpūst
		VerbRule.firstConjDir("-pūstu, -pūsti,", "-pūst, pag. -puvu", "pūt"), //aizpūt, pūt
		// R
		VerbRule.firstConjDir("-roku, -roc,", "-rok, pag. -raku", "rakt"), //aizrakt
		VerbRule.firstConjDir("-rodu, -rodi,", "-rod, pag. -radu", "rast"), //aprast
		VerbRule.firstConjDir("-raucu, -rauc,", "-rauc, pag. -raucu", "raukt"), //apraukt
		VerbRule.firstConjDir("-raušu, -raus,", "-rauš, pag. -rausu", "raust"), //aizraust
		VerbRule.firstConjDir("-rauju, -rauj,", "-rauj, pag. -rāvu", "raut"), //aizraut
		VerbRule.firstConjDir("-rāju, -rāj,", "-rāj, pag. -rāju", "rāt"), //aprāt
		VerbRule.firstConjDir("-reibstu, -reibsti,", "-reibst, pag. -reibu", "reibt"), //apreibt
		VerbRule.firstConjDir("-rēcu, -rēc,", "-rēc, pag. -rēcu", "rēkt"), //atrēkt
		VerbRule.firstConjDir("-riebju, -rieb,", "-riebj, pag. -riebu", "riebt"), //aizriebt
		VerbRule.firstConjDir("-reju, -rej,", "-rej, pag. -rēju", "riet"), //apriet
		VerbRule.firstConjDir("-rimstu, -rimsti,", "-rimst, pag. -rimu", "rimt"), //aprimt
		VerbRule.firstConjDir("-riju, -rij,", "-rij, pag. -riju", "rīt"), //aizrīt
		VerbRule.firstConjDir("-rūcu, -rūc,", "-rūc, pag. -rūcu", "rūkt"), //atrūkt
		// S
		VerbRule.firstConjDir("-salkstu, -salksti,", "-salkst, pag. -salku", "salkt"), //aizsalkt
		VerbRule.firstConjDir("-salstu, -salsti,", "-salst, pag. -salu", "salt"), //atsalt
		VerbRule.firstConjDir("-sarkstu, -sarksti,", "-sarkst, pag. -sarku", "sarkt"), //aizsarkt
		VerbRule.firstConjDir("-saucu, -sauc,", "-sauc, pag. -saucu", "saukt"), //aizsaukt
		VerbRule.firstConjDir("-sāku, -sāc,", "-sāk, pag. -sāku", "sākt"), //aizsākt
		VerbRule.firstConjDir("-sedzu, -sedz,", "-sedz, pag. -sedzu", "segt"), //aizsegt
		VerbRule.firstConjDir("-sēršu, -sērs,", "-sērš, pag. -sērsu", "sērst"), //apsērst
		VerbRule.firstConjDir("-seru, -ser,", "-ser, pag. -sēru", "sērt"), //aizsērt
		VerbRule.firstConjDir("-sēstu, -sēsti,", "-sēst, pag. -sēdu", "sēst"), //aizsēst
		VerbRule.firstConjDir("-sēju, -sēj,", "-sēj, pag. -sēju", "sēt"), //apsēt
		VerbRule.firstConjDir("-sienu, -sien,", "-sien, pag. -sēju", "siet"), //aizsiet
		VerbRule.firstConjDir("-sieku, -siec,", "-siek, pag. -siku", "sikt"), //apsikt
		VerbRule.firstConjDir("-silstu, -silsti,", "-silst, pag. -silu", "silt"), //apsilt
		VerbRule.firstConjDir("-sirgstu, -sirgsti,", "-sirgst, pag. -sirgu", "sirgt"), //apsirgt
		VerbRule.firstConjDir("-situ, -sit,", "-sit, pag. -situ", "sist"), //aizsist
		VerbRule.firstConjDir("-sīkstu, -sīksti,", "-sīkst, pag. -sīku", "sīkt"), //apsīkt
		VerbRule.firstConjDir("-skaru, -skar,", "-skar, pag. -skāru", "skart"), //aizskart
		VerbRule.firstConjDir("-skaužu, -skaud,", "-skauž, pag. -skaudu", "skaust"), //apskaust
		VerbRule.firstConjDir("-skauju, -skauj,", "-skauj, pag. -skāvu", "skaut"), //apskaut
		VerbRule.firstConjDir("-skumstu, -skumsti,", "-skumst, pag. -skumu", "skumt"), //apskumt
		VerbRule.firstConjDir("-skurbstu, -skurbsti,", "-skurbst, pag. -skurbu", "skurbt"), //apskurbt
		VerbRule.firstConjDir("-skuju, -skuj,", "-skuj, pag. -skuvu", "skūt"), //aizskūt
		VerbRule.firstConjDir("-slāpstu, -slāpsti,", "-slāpst, pag. -slāpu", "slāpt"), //aizslāpt
		VerbRule.firstConjDir("-slāju, -slāj,", "-slāj, pag. -slāju", "slāt"), //aizslāt
		VerbRule.firstConjDir("-slaucu, -slauc,", "-slauc, pag. -slaucu", "slaukt"), //aizslaukt
		VerbRule.firstConjDir("-slābstu, -slābsti,", "-slābst, pag. -slābu", "slābt"), //atslābt
		VerbRule.firstConjDir("-slēdzu, -slēdz,", "-slēdz, pag. -slēdzu", "slēgt"), //aizslēgt
		VerbRule.firstConjDir("-slēpju, -slēp,", "-slēpj, pag. -slēpu", "slēpt"), //aizslēpt
		VerbRule.firstConjDir("-slimstu, -slimsti,", "-slimst, pag. -slimu", "slimt"), //apslimt
		VerbRule.firstConjDir("-slinkstu, -slinksti,", "-slinkst, pag. -slinku", "slinkt"), //apslinkt
		VerbRule.firstConjDir("-slīgstu, -slīgsti,", "-slīgst, pag. -slīgu", "slīgt"), //aizslīgt
		VerbRule.firstConjDir("-smoku, -smoc,", "-smok, pag. -smaku", "smakt"), //aizsmakt
		VerbRule.firstConjDir("-smeļu, -smel,", "-smeļ, pag. -smēlu", "smelt"), //apsmelt
		VerbRule.firstConjDir("-smeju, -smej,", "-smej, pag. -smēju", "smiet"), //apsmiet
		VerbRule.firstConjDir("-snaužu, -snaud,", "-snauž, pag. -snaudu", "snaust"), //aizsnaust
		VerbRule.firstConjDir("-sniedzu, -sniedz,", "-sniedz, pag. -sniedzu", "sniegt"), //aizsniegt
		VerbRule.firstConjDir("-sniegu, -sniedz,", "-snieg, pag. -snigu", "snigt"), //apsnigt
		VerbRule.firstConjDir("-speru, -sper,", "-sper, pag. -spēru", "spert"), //aizspert
		VerbRule.firstConjDir("-spēju, -spēj,", "-spēj, pag. -spēju", "spēt"), //aizspēt
		VerbRule.firstConjDir("-spiedzu, -spiedz,", "-spiedz, pag. -spiedzu", "spiegt"), //atspiegt
		VerbRule.firstConjDir("-spiežu, -spied,", "-spiež, pag. -spiedu", "spiest"), //aizspiest
		VerbRule.firstConjDir("-spirgstu, -spirgsti,", "-spirgst, pag. -spirgu", "spirgt"), //atspirgt
		VerbRule.firstConjDir("-spļauju, -spļauj,", "-spļauj, pag. -spļāvu", "spļaut"), //aizspļaut
		VerbRule.firstConjDir("-spraužu, -spraud,", "-sprauž, pag. -spraudu", "spraust"), //aizspraust
		VerbRule.firstConjDir("-sprāgstu, -sprāgsti,", "-sprāgst, pag. -sprāgu", "sprāgt"), //atsprāgt
		VerbRule.firstConjDir("-spriežu, -spried,", "-spriež, pag. -spriedu", "spriest"), //apspriest
		VerbRule.firstConjDir("-spruku, -sprūc,", "-sprūk, pag. -spruku", "sprukt"), //aizsprukt
		VerbRule.firstConjDir("-stāju, -stāj,", "-stāj, pag. -stāju", "stāt"), //aizstāt
		VerbRule.firstConjDir("-steidzu, -steidz,", "-steidz, pag. -steidzu", "steigt"), //aizsteigt
		VerbRule.firstConjDir("-stiepju, -stiep,", "-stiepj, pag. -stiepu", "stiept"), //aizstiept
		VerbRule.firstConjDir("-strebju, -streb,", "-strebj, pag -strēbu", "strēbt"), //apstrēbt
		VerbRule.firstConjDir("-stulbstu, -stulbsti,", "-stulbst, pag. -stulbu", "stulbt"), //stulbt
		VerbRule.firstConjDir("-stumju, -stum,", "-stumj, pag. -stūmu", "stumt"), //aizstumt
		VerbRule.firstConjDir("-sūtu, -sūti,", "-sūt, pag. -sutu", "sust"), //apsust
		VerbRule.firstConjDir("-sveicu, -sveic,", "-sveic, pag. -sveicu", "sveikt"), //apsveikt
		VerbRule.firstConjDir("-sveru, -sver,", "-sver, pag. -svēru", "svērt"), //apsvērt
		VerbRule.firstConjDir("-sviežu, -svied,", "-sviež, pag. -sviedu", "sviest"), //aizsviest
		VerbRule.firstConjDir("-svilpju, -svilp,", "-svilpj, pag. -svilpu", "svilpt"), //atsvilpt
		VerbRule.firstConjDir("-svilstu, -svilsti,", "-svilst, pag. -svilu", "svilt"), //aizsvilt
		// Š
		VerbRule.firstConjDir("-šalcu, -šalc,", "-šalc, pag. -šalcu", "šalkt"), //pašalkt, aizšalkt
		VerbRule.firstConjDir("-šauju, -šauj,", "-šauj, pag. -šāvu", "šaut"), //aizšaut
		VerbRule.firstConjDir("-šķeļu, -šķel,", "-šķeļ, pag. -šķēlu", "šķelt"), //aizšķelt
		VerbRule.firstConjDir("-šķiežu, -šķied,", "-šķiež, pag. -šķiedu", "šķiest"), //aizšķiest
		VerbRule.firstConjDir("-šķiļu, -šķil,", "-šķiļ, pag. -šķīlu", "šķilt"), //aizšķilt
		VerbRule.firstConjDir("-šķiru, -šķir,", "-šķir, pag. -šķīru", "šķirt"), //aizšķirt
		VerbRule.firstConjDir("-šķīstu, -šķīsti,", "-šķīst, pag. -šķīdu", "šķīst"), //apšķīst
		VerbRule.firstConjDir("-šļācu, -šļāc,", "-šļāc, pag. -šļācu", "šļākt"), //aizšļākt
		VerbRule.firstConjDir("-šļuku, -šļūc,", "-šļūk, pag. -šļuku", "šļukt"), //aizšļukt
		VerbRule.firstConjDir("-šļūcu, -šļūc,", "-šļūc, pag. -šļūcu", "šļūkt"), //aizšļūkt
		VerbRule.firstConjDir("-šmaucu, -šmauc,", "-šmauc, pag. -šmaucu", "šmaukt"), //aizšmaukt
		VerbRule.firstConjDir("-šņācu, -šņāc,", "-šņāc, pag. -šņācu", "šņākt"), //atšņākt
		VerbRule.firstConjDir("-šņāpju, -šņāp,", "-šņāpj, pag. -šņāpu", "šņāpt"), //apšņāpt
		VerbRule.firstConjDir("-šuju, -šuj,", "-šuj, pag. -šuvu", "šūt"), //aizšūt
		// T
		VerbRule.firstConjDir("-topu, -topi,", "-top, pag. -tapu", "tapt"), //attapt
		VerbRule.firstConjDir("-tērpju, -tērp,", "-tērpj, pag. -tērpu", "tērpt"), //aptērpt
		VerbRule.firstConjDir("-tēšu, -tēs,", "-tēš, pag. -tēsu", "tēst"), //aptēst
		VerbRule.firstConjDir("-tinu, -tin,", "-tin, pag. -tinu", "tīt"), //aiztīt
		VerbRule.firstConjDir("-traucu, -trauc,", "-trauc, pag. -traucu", "traukt"), //aiztraukt
		VerbRule.firstConjDir("-trencu, -trenc,", "-trenc, pag. -trencu", "trenkt"), //aiztrenkt
		VerbRule.firstConjDir("-triecu, -triec,", "-triec, pag. -triecu", "triekt"), //aiztriekt
		VerbRule.firstConjDir("-triepju, -triep,", "-triepj, pag. -triepu", "triept"), //aiztriept
		VerbRule.firstConjDir("-tupstu, -tupsti,", "-tupst, pag. -tupu", "tupt"), //aiztupt
		VerbRule.firstConjDir("-tūkstu, -tūksti,", "-tūkst; pag. -tūku", "tūkt"), //aptūkt, aiztūkt
		VerbRule.firstConjDir("-tveru, -tver,", "-tver, pag. -tvēru", "tvert"), //aiztvert
		// U
		VerbRule.firstConjDir("-urbju, -urb,", "-urbj, pag. -urbu", "urbt"), //aizurbt
		// V
		VerbRule.firstConjDir("-vācu, -vāc,", "-vāc, pag. -vācu", "vākt"), //aizvākt
		VerbRule.firstConjDir("-vāžu, -vāz,", "-vāž, pag. -vāzu", "vāzt"), //aizvāzt
		VerbRule.firstConjDir("-veļu, -vel,", "-veļ, pag. -vēlu", "velt"), //aizvelt
		VerbRule.firstConjDir("-vemju, -vem,", "-vemj, pag. -vēmu", "vemt"), //apvemt
		VerbRule.firstConjDir("-vedu, -ved,", "-ved, pag. -vedu", "vest"), //aizvest
		VerbRule.firstConjDir("-vērpju, -vērp,", "-vērpj, pag. -vērpu", "vērpt"), //aizvērpt
		VerbRule.firstConjDir("-vēršu, -vērs,", "-vērš, pag. -vērsu", "vērst"), //aizvērst
		VerbRule.firstConjDir("-veru, -ver,", "-ver, pag. -vēru", "vērt"), //aizvērt
		VerbRule.firstConjDir("-vēžu, -vēz,", "-vēž, pag. -vēzu", "vēzt"), //apvēzt
		VerbRule.firstConjDir("-vilgstu, -vilgsti,", "-vilgst, pag. -vilgu", "vilgt"), //atvilgt
		VerbRule.firstConjDir("-velku, -velc,", "-velk, pag. -vilku", "vilkt"), //aizvilkt
		VerbRule.firstConjDir("-viļu, -vil,", "-viļ, pag. -vīlu", "vilt"), //aizvilt
		VerbRule.firstConjDir("-viju, -vij,", "-vij, pag. -viju", "vīt"), //aizvīt
		VerbRule.firstConjDir("-viju, -vij,", "-vij; pag. -viju", "vīt"), //apvīt
		// Z
		VerbRule.firstConjDir("-zogu, -zodz,", "-zog, pag. -zagu", "zagt"), // apzagt
		VerbRule.firstConjDir("-ziežu, -zied,", "-ziež, pag. -ziedu", "ziest"), // aizziest
		VerbRule.firstConjDir("-zīstu, -zīsti,", "-zīst, pag. -zinu", "zīt"), // atzīt
		VerbRule.firstConjDir("-zūdu, -zūdi,", "-zūd, pag. -zudu", "zust"), // aizzust
		VerbRule.firstConjDir("-zveļu, -zvel,", "-zveļ, pag. -zvēlu", "zvelt"), // atzvelt
		VerbRule.firstConjDir("-zvilstu, -zvilsti,", "-zvilst, pag. -zvilu", "zvilt"), // atzvilt
		// Ž
		VerbRule.firstConjDir("-žauju, -žauj,", "-žauj, pag. -žāvu", "žaut"), // apžaut
		VerbRule.firstConjDir("-žilbstu, -žilbsti,", "-žilbst, pag. -žilbu", "žilbt"), // apžilbt
		VerbRule.firstConjDir("-žirgstu, -žirgsti,", "-žirgst, pag. -žirgu", "žirgt"), // atžirgt
		VerbRule.firstConjDir("-žmaudzu, -žmaudz,", "-žmaudz, pag. -žmaudzu", "žmaugt"), // apžmaugt
		VerbRule.firstConjDir("-žmiedzu, -žmiedz,", "-žmiedz, pag. -žmiedzu", "žmiegt"), // aizžmiegt
		VerbRule.firstConjDir("-žņaudzu, -žņaudz,", "-žņaudz, pag. -žņaudzu", "žņaugt"), // aizžņaugt
		VerbRule.firstConjDir("-žūstu, -žūsti,", "-žūst, pag. -žuvu", "žūt"), // apžūt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas.
		SimpleRule.of("parasti 3. pers., -aust, pag. -ausa", ".*aust", 15,
				new Tuple[]{Features.POS__VERB,
						Tuple.of(Keys.INFLECT_AS, "\"aust\" (kā gaisma)"),
						Features.INFINITIVE_HOMOFORMS},
				new Tuple[]{Features.USUALLY_USED__THIRD_PERS}), //aizaust 1
		SimpleRule.of("parasti 3. pers., -dzīst, pag. -dzija", ".*dzīt", 15,
				new Tuple[]{Features.POS__VERB,
						Tuple.of(Keys.INFLECT_AS, "\"dzīt\" (kā ievainojumi)"),
						Features.INFINITIVE_HOMOFORMS},
				new Tuple[]{Features.USUALLY_USED__THIRD_PERS}), //aizdzīt 2
		SimpleRule.of("parasti 3. pers., -irst, pag. -ira", ".*irt", 15,
				new Tuple[] {Features.POS__VERB,
						Tuple.of(Keys.INFLECT_AS, "\"irt\" (kā audums)"),
						Features.INFINITIVE_HOMOFORMS},
				new Tuple[] {Features.USUALLY_USED__THIRD_PERS}), //irt 2

		// Standartizētie.
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

		// Pilnīgs nestandarts.
		SimpleRule.of("-teicu, -teic, -teic (tagadnes formas parasti nelieto), pag. -teicu", ".*teikt", 15,
				new Tuple[]{Features.POS__VERB,
						Tuple.of(Keys.INFLECT_AS, "\"teikt\"")},
				new Tuple[]{Tuple.of(Keys.USUALLY_USED_IN_FORM, "Nelieto tagadnes formas")}), //atteikt
		SimpleRule.of("3. pers. -guldz, pag. -guldza", ".*gulgt", 15,
				new Tuple[]{Features.POS__VERB,
						Tuple.of(Keys.INFLECT_AS, "\"gulgt\"")},
				new Tuple[]{Features.USUALLY_USED__THIRD_PERS}), //aizgulgt

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Likumi, kam ir visu formu variants.
		VerbRule.secondConjDir("-dabūju, -dabū,", "-dabū, pag. -dabūju", "dabūt"), //aizdabūt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		// Īpašā piezīme par glumēšanu: 2. konjugāciju nosaka 3. personas
		// galotne "-ē" - 3. konjugācijai būtu bez.
		SimpleRule.of("parasti 3. pers., -ē, pag. -ēja (retāk -gluma, 1. konj.)", ".*glumēt", 16,
				new Tuple[] {Features.POS__VERB, Features.PARALLEL_FORMS},
				new Tuple[] {Features.USUALLY_USED__THIRD_PERS}), //aizglumēt
		SimpleRule.of(
				"parasti 3. pers., -glumē, pag. -glumēja (retāk -gluma, 1. konj.)",
				".*glumēt", 16,
				new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS},
				new Tuple[]{Features.USUALLY_USED__THIRD_PERS}), //izglumēt

		// Standartizētie.
		ThirdPersVerbRule.secondConjDir("-kūko, pag. -kūkoja", "kūkot"), //aizkūkot
		ThirdPersVerbRule.secondConjDir("-mirgo, pag. -mirgoja", "mirgot"), //aizmirgot
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		SimpleRule.of(
				"-moku, -moki, -moka, arī -mocu, -moci, -moca, pag. -mocīju",
				".*mocīt", 17,
				new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.HAS_PRESENT_SOUNDCHANGE, Features.NO_PRESENT_SOUNDCHANGE},
				null), //aizmocīt
		SimpleRule.of(
				"-murcu, -murci, -murca, retāk -murku, -murki, -murka, pag. -murcīju",
				".*murcīt", 17,
				new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.HAS_PRESENT_SOUNDCHANGE, Features.NO_PRESENT_SOUNDCHANGE},
				null), //apmurcīt
		SimpleRule.of(
				"-ņurcu, -ņurci, -ņurca, retāk -ņurku, -ņurki, -ņurka, pag. -ņurcīju",
				".*ņurcīt", 17,
				new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.HAS_PRESENT_SOUNDCHANGE, Features.NO_PRESENT_SOUNDCHANGE},
				null), //apmurcīt

		SimpleRule.of("-slīdu, -slīdi, -slīd, pag. -slīdēju, -slīdēji, -slīdēja (retāk -slīda, 1. konj.)",
				".*slīdēt", 17,
				new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.NO_PRESENT_SOUNDCHANGE},
				null), // aizslīdēt

		SimpleRule.of("-guļu, -guli, -guļ (arī -gul), pag. -gulēju",
				".*gulēt", 17,
				new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.HAS_PRESENT_SOUNDCHANGE, Features.NO_PRESENT_SOUNDCHANGE},
				null), // iegulēt
			//TODO kā norādīt miju + ko darīt ar otru, standartizēto gulēt?

		// Standartizētie.
		// A, B
		VerbRule.thirdConjDir("-brauku, -brauki,", "-brauka, pag. -braucīju", "braucīt", false), //apbraucīt
		// C, Č
		VerbRule.thirdConjDir("-ceru, -ceri,", "-cer, pag. -cerēju", "cerēt", false), //iecerēt
		VerbRule.thirdConjDir("-čukstu, -čuksti,", "-čukst, pag. -čukstēju", "čukstēt", false), //atčukstēt
		// D
		VerbRule.thirdConjDir("-dienu, -dieni,", "-dien, pag. -dienēju", "dienēt", false), //atdienēt
		VerbRule.thirdConjDir("-draudu, -draudi,", "-draud, pag. -draudēju", "draudēt", false), //apdraudēt
		VerbRule.thirdConjDir("-dusu, -dusi,", "-dus, pag. -dusēju", "dusēt", false), //atdusēt
		VerbRule.thirdConjDir("-dziedu, -dziedi,", "-dzied, pag. -dziedāju", "dziedāt", false), //aizdziedāt
		// E, F, G
		VerbRule.thirdConjDir("-glūnu, -glūni,", "-glūn, pag. -glūnēju", "glūnēt", false), //apglūnēt
		VerbRule.thirdConjDir("-grabu, -grabi,", "-grab, pag. -grabēju", "grabēt", false), //aizgrabēt
		VerbRule.thirdConjDir("-gribu, -gribi,", "-grib, pag. -gribēju", "gribēt", false), //iegribēt
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
		VerbRule.thirdConjDir("-minu, -mini,", "-min, pag. -minēju", "minēt", false), //atminēt
		VerbRule.thirdConjDir("-mīlu, -mīli,", "-mīl, pag. -mīlēju", "mīlēt", false), //iemīlēt
		VerbRule.thirdConjDir("-muldu, -muldi,", "-muld, pag. -muldēju", "muldēt", false), //atmuldēt
		VerbRule.thirdConjDir("-murdzu, -murdzi,", "-murdza, pag. -murdzīju", "murdzīt", false), //apmurdzīt
		// N, Ņ
		VerbRule.thirdConjDir("-ņurdu, -ņurdi,", "-ņurd, pag. -ņurdēju", "ņurdēt", false), //atņurdēt
		// O, P
		VerbRule.thirdConjDir("-peldu, -peldi,", "-peld, pag. -peldēju", "peldēt", false), //aizpeldēt
		VerbRule.thirdConjDir("-pilu, -pili,", "-pil, pag. -pilēju", "pilēt", false), //appilēt, aizpilēt
		VerbRule.thirdConjDir("-pīkstu, -pīksti,", "-pīkst, pag. -pīkstēju", "pīkstēt", false), //atpīkstēt
		VerbRule.thirdConjDir("-precu, -preci,", "-prec, pag. -precēju", "precēt", false), //aizprecēt
		VerbRule.thirdConjDir("-putu, -puti,", "-put, pag. -putēju", "putēt", false), //aizputēt, apputēt
		// R
		VerbRule.thirdConjDir("-raudu, -raudi,", "-raud, pag. -raudāju", "raudāt", false), //apraudāt
		VerbRule.thirdConjDir("-raugu, -raugi,", "-rauga, pag. -raudzīju", "raudzīt", true), //apraudāt
		VerbRule.thirdConjDir("-redzu, -redzi,", "-redz, pag. -redzēju", "redzēt", false), //apredzēt
		// S
		VerbRule.thirdConjDir("-saku, -saki,", "-saka, pag. -sacīju", "sacīt", true), //atsacīt
		VerbRule.thirdConjDir("-sēžu, -sēdi,", "-sēž, pag. -sēdēju", "sēdēt", true), //atsēdēt
		VerbRule.thirdConjDir("-sīkstu, -sīksti,", "-sīkst, pag. -sīkstēju", "sīkstēt", false), //apsīkstēt
		VerbRule.thirdConjDir("-slaku, -slaki,", "-slaka, pag. -slacīju", "slacīt", true), //aizslacīt
		VerbRule.thirdConjDir("-slauku, -slauki,", "-slauka, pag. -slaucīju", "slaucīt", true), //aizslaucīt
		VerbRule.thirdConjDir("-smīnu, -smīni,", "-smīn, pag. -smīnēju", "smīnēt", true), //atsmīnēt
		VerbRule.thirdConjDir("-stāvu, -stāvi,", "-stāv, pag. -stāvēju", "stāvēt", false), //aizstāvēt
		VerbRule.thirdConjDir("-strīdu, -strīdi,", "-strīd, pag. -strīdēju", "strīdēt", false), //apstrīdēt
		VerbRule.thirdConjDir("-sūdzu, -sūdzi,", "-sūdz, pag. -sūdzēju", "sūdzēt", false), //apsūdzēt
		// Š
		VerbRule.thirdConjDir("-šļupstu, -šļupsti,", "-šļupst, pag. -šļupstēju", "šļupstēt", false), //atšļupstēt
		// T
		VerbRule.thirdConjDir("-teku, -teci,", "-tek, pag. -tecēju", "tecēt", true), //aiztecēt
		VerbRule.thirdConjDir("-turu, -turi,", "-tur, pag. -turēju", "turēt", false), //aizturēt
		// U, V
		VerbRule.thirdConjDir("-vēlu, -vēli,", "-vēl, pag. -vēlēju", "vēlēt", false), //atvēlēt
		// Z
		VerbRule.thirdConjDir("-zinu, -zini,", "-zina, pag. -zināju", "zināt", false), //apzināt
		VerbRule.thirdConjDir("-zvēru, -zvēri,", "-zvēr, pag. -zvērēju", "zvērēt", false), //apzvērēt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		SimpleRule.of("parasti 3. pers., -grand, pag. -grandēja (retāk -granda, 1. konj.)", ".*grandēt", 17,
				new Tuple[] {Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.NO_PRESENT_SOUNDCHANGE},
				new Tuple[] {Features.USUALLY_USED__THIRD_PERS}), //aizgrandēt
		SimpleRule.of("parasti 3. pers., -gruzd, pag. -gruzdēja (retāk -gruzda, 1. konj.)", ".*gruzdēt", 17,
				new Tuple[] {Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.NO_PRESENT_SOUNDCHANGE},
				new Tuple[] {Features.USUALLY_USED__THIRD_PERS}), //aizgruzdēt
		SimpleRule.of("parasti 3. pers., -mirdz, pag. -mirdzēja (retāk -mirdza, 1. konj.)", ".*mirdzēt", 17,
				new Tuple[] {Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.NO_PRESENT_SOUNDCHANGE},
				new Tuple[] {Features.USUALLY_USED__THIRD_PERS}), //aizmirdzēt

		// Standartizētie.
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
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] directMultiConjVerb = {
		// Likumi, kam ir visu formu variants.
		ComplexRule.secondThirdConjDirectAllPers(
				"-bedīju, -bedī, -bedī, arī -bedu, -bedi, -beda, pag. -bedīju",
				".*bedīt", false), // apbedīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-ceru, -ceri, -cer, retāk -cerēju, -cerē, -cerē, pag. -cerēju",
				".*cerēt", false), // apcerēt
		ComplexRule.secondThirdConjDirectAllPers(
				"-cienu, -cieni, -ciena, arī -cienīju, -cienī, -cienī, pag. -cienīju",
				".*cienīt", false), // iecienīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-dēstu, -dēsti, -dēsta, retāk -dēstīju, -dēstī, -dēstī, pag. -dēstīju",
				".*dēstīt", false), // apdēstīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-kristīju, -kristī, -kristī, arī -kristu, -kristi, -krista, pag. -kristīju",
				".*kristīt", false), // iekristīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-krustīju, -krusti, -krusti, arī -krustu, -krusti, -krusta, pag. -krustīju",
				".*krustīt", false), // iekrustīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-ķēzīju, -ķēzī, -ķēzī, arī -ķēzu, -ķēzi, -ķēza, pag. -ķēzīju",
				".*ķēzīt", false), // apķēzīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-mērīju, -mērī, -mērī, arī -mēru, -mēri, -mēra, pag. -mērīju",
				".*mērīt", false), // atmērīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-pelnu, -pelni, -pelna, arī -pelnīju, -pelnī, -pelnī, pag. -pelnīju",
					".*pelnīt", false), // atpelnīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-pētīju, -pētī, -pētī, arī -pētu, -pēti, -pēta, pag. -pētīju",
				".*pētīt", false), // appētīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-rotu, -roti, -rota, arī -rotīju, -roti, -rotī, pag. -rotīju",
				".*rotīt", false), // aizrotīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-sargāju, -sargā, -sargā, arī -sargu, -sargi, -sarga, pag. -sargāju",
				".*sargāt", false), // aizsargāt
		ComplexRule.secondThirdConjDirectAllPers(
				"-svētīju, -svētī, -svētī, arī -svētu, -svēti, -svēta, pag. -svētīju",
				".*svētīt", false), // aizsargāt
		ComplexRule.secondThirdConjDirectAllPers(
				"-tašķīju, -tašķī, -tašķī, arī -tašķu, -tašķi, -tašķa, pag. -tašķīju",
				".*tašķīt", false), // aptašķīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-veltīju, -veltī, -veltī, arī -veltu, -velti, -velta, pag. -veltīju",
				".*veltīt", false), // apveltīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-vētīju, -vētī, -vētī, arī -vētu, -vēti, -vēta, pag. -vētīju",
				".*vētīt", false), // aizvētīt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		ComplexRule.of("parasti 3. pers., -pelē, arī -pel, pag. -pelēja", new Trio[]{
					Trio.of(".*pelēt", new Integer[] {16, 17},
							new Tuple[] {Features.POS__VERB, Features.PARALLEL_FORMS,
									Features.NO_PRESENT_SOUNDCHANGE})},
				new Tuple[] {Features.USUALLY_USED__THIRD_PERS}), // aizpelēt
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
		VerbRule.of("-aužos, -audies,", "-aužas, pag. -audos", "austies", 18,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"austies\" (kā audumam)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //apausties
		VerbRule.of("-dzenos, -dzenies,", "-dzenas, pag. -dzinos", "dzīties", 18,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"dzīties\" (kā lopiem)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizdzīties
		VerbRule.of("-iros, -iries,", "-iras, pag. -īros", "irties", 18,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"irties\" (kā ar airiem)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizirties
		VerbRule.of("-minos, -minies,", "-minas, pag. -minos", "mīties", 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"mīties\" (kā pedāļus)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //aizmīties
		VerbRule.of("-mijos, -mijies,", "-mijas, pag. -mijos", "mīties", 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"mīties\" (kā naudu)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //apmīties
		VerbRule.of("-tiekos, -tiecies,", "-tiekas, pag. -tikos", "tikties", 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"tikties\" (kā satikties ar kādu)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //satikties
		// Izņēmuma izņēmums :/
		VerbRule.of("-patīkos, -patīcies,", "-patīkas, pag. -patikos", "patikties", 18,
				new Tuple[]{Tuple.of(Keys.INFLECT_AS, "\"tikties\" (kā patikties kādam)"),
						Features.INFINITIVE_HOMOFORMS},
				null), //iepatikties


		// Paralēlās formas.
		SimpleRule.of("-aujos, -aujies, -aujas, arī -aunos, -aunies, -aunas, pag. -āvos",
				".*auties", 18,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"auties\""),
						Features.PARALLEL_FORMS},
				null), //apauties
		SimpleRule.of("-gulstos, -gulsties, -gulstas, arī -guļos, -gulies, -guļas, pag. -gūlos, arī -gulos",
				".*gulties", 18,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"gulties\""),
						Features.PARALLEL_FORMS},
				null), //aizgulties
		SimpleRule.of("-plešos, -pleties, -plešas, pag. -pletos, arī -plētos",
				".*plesties", 18,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"plesties\""),
						Features.PARALLEL_FORMS},
				null), //ieplesties
		SimpleRule.of("-sēžos, -sēdies, -sēžas, arī -sēstos, -sēsties, -sēstas, pag. -sēdos",
				".*sēsties", 18,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"sēsties\""),
						Features.PARALLEL_FORMS},
				null), //aizsēsties
		SimpleRule.of("-slienos, -slienies, -slienas, arī -slejos, -slejies, -slejas, pag. -slējos",
				".*slieties", 18,
				new Tuple[] {Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"slieties\""),
						Features.PARALLEL_FORMS},
				null), //aizslieties

		// Izņēmums.
		VerbRule.of("-pazīstos, -pazīsties,", "-pazīstas, pag. -pazinos", "pazīties", 18,
				new Tuple[] {Tuple.of(Keys.INFLECT_AS, "\"zīties\"")}, null), //iepazīties

		// Standartizētie.
		// A
		VerbRule.firstConjRefl("-aros, -aries,", "-aras, pag. -aros", "arties"), //iearties
		// B
		VerbRule.firstConjRefl("-bāžos, -bāzies,", "-bāžas, pag. -bāžos", "bāzties"), //iebāzties
		VerbRule.firstConjRefl("-beros, -beries,", "-beras, pag. -bēros", "bērties"), //apbērties
		VerbRule.firstConjRefl("-brāžos, -brāzies,", "-brāžas, pag. -brāzos", "brāzties"), //aizbrāzties
		VerbRule.firstConjRefl("-brienos, -brienies,", "-brienas, pag. -bridos", "bristies"), //atbristies
		// C
		VerbRule.firstConjRefl("-ceļos, -celies,", "-ceļas, pag. -cēlos", "celties"), //apcelties
		VerbRule.firstConjRefl("-ceros, -ceries,", "-ceras, pag. -cerējos", "cerēties"), //atcerēties
		VerbRule.firstConjRefl("-ciešos, -cieties,", "-ciešas, pag. -cietos", "ciesties"), //aizciesties
		VerbRule.firstConjRefl("-cērtos, -cērties,", "-cērtas, pag. -cirtos", "cirsties"), //aizcirsties
		// D
		VerbRule.firstConjRefl("-dodos, -dodies,", "-dodas, pag. -devos", "doties"), //atdoties
		VerbRule.firstConjRefl("-drāžos, -drāzies,", "-drāžas, pag. -drāzos", "drāzties"), //aizdrāzties
		VerbRule.firstConjRefl("-duros, -duries,", "-duras, pag. -dūros", "durties"), //aizdurties
		VerbRule.firstConjRefl("-dzeros, -dzeries,", "-dzeras, pag. -dzēros", "dzerties"), //apdzerties
		// E
		VerbRule.firstConjRefl("-ēdos, -ēdies,", "-ēdas, pag. -ēdos", "ēsties"), //atēsties
		// F, G
		VerbRule.firstConjRefl("-gāžos, -gāzies,", "-gāžas, pag. -gāzos", "gāzties"), //apgāzties, aizgāzties
		VerbRule.firstConjRefl("-glaužos, -glaudies,", "-glaužas, pag. -glaudos", "glausties"), //ieglausties
		VerbRule.firstConjRefl("-graužos, -grauzies,", "-graužas, pag. -grauzos", "grauzties"), //izgrauzties, aizgrauzties
		VerbRule.firstConjRefl("-griežos, -griezies,", "-griežas, pag. -griezos", "griezties"), //aizgriezties 1, 2
		VerbRule.firstConjRefl("-gružos, -grūdies,", "-grūžas, pag. -grūdos", "grūsties"), //atgrūsties
		VerbRule.firstConjRefl("-gūstos, -gūsties,", "-gūstas, pag. -guvos", "gūties"), //aizgūties
		// Ģ
		VerbRule.firstConjRefl("-ģērbjos, -ģērbies,", "-ģērbjas, pag. -ģērbos", "ģērbties"), //apģērbties
		// H, I
		VerbRule.firstConjRefl("-ejos, -ejies,", "-ietas, pag. -gājos", "ieties"), //apieties
		// Ī
		VerbRule.firstConjRefl("-īdos, -īdies,", "-īdas, pag. -īdējos", "īdēties"), //ieīdēties
		// J
		VerbRule.firstConjRefl("-jaucos, -jaucies,", "-jaucas, pag. -jaucos", "jaukties"), //iejaukties
		VerbRule.firstConjRefl("-jožos, -jozies,", "-jožas, pag. -jozos", "jozties"), //apjozties
		VerbRule.firstConjRefl("-jūtos, -jūties,", "-jūtas, pag. -jutos", "justies"), //iejusties
		VerbRule.firstConjRefl("-jūdzos, -jūdzies,", "-jūdzas, pag. -jūdzos", "jūgties"), //aizjūgties
		// K
		VerbRule.firstConjRefl("-kaļos, -kalies,", "-kaļas, pag. -kalos", "kalties"), //iekalties
		VerbRule.firstConjRefl("-kampjos, -kampies,", "-kampjas, pag. -kampos", "kampties"), //apkampties
		VerbRule.firstConjRefl("-kaujos, -kaujies,", "-kaujas, pag. -kāvos", "kauties"), //atkauties
		VerbRule.firstConjRefl("-kāpjos, -kāpies,", "-kāpjas, pag. -kāpos", "kāpties"), //atkāpties
		VerbRule.firstConjRefl("-karos, -karies,", "-karas, pag. -kāros", "kārties"), //apkārties
		VerbRule.firstConjRefl("-klājos, -klājies,", "-klājas, pag. -klājos", "klāties"), //apklāties
		VerbRule.firstConjRefl("-kļaujos, -kļaujies,", "-kļaujas, pag. -kļāvos", "kļauties"), //apkļauties
		VerbRule.firstConjRefl("-kniebjos, -kniebies,", "-kniebjas, pag. -kniebos", "kniebties"), //iekniebties
		VerbRule.firstConjRefl("-kopjos, -kopies,", "-kopjas, pag. -kopos", "kopties"), //apkopties
		VerbRule.firstConjRefl("-kožos, -kodies,", "-kožas, pag. -kodos", "kosties"), //atkosties
		VerbRule.firstConjRefl("-kraujos, -kraujies,", "-kraujas, pag. -krāvos", "krauties"), //apkrauties
		VerbRule.firstConjRefl("-krāpjos, -krāpies,", "-krāpjas, pag. -krāpos", "krāpties"), //apkrāpties
		VerbRule.firstConjRefl("-krājos, -krājies,", "-krājas, pag. -krājos", "krāties"), //iekrāties
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
		VerbRule.firstConjRefl("-lūdzos, -lūdzies,", "-lūdzas, pag. -lūdzos", "lūgties"), //atlūgties
		// Ļ
		VerbRule.firstConjRefl("-ļaujos, -ļaujies,", "-ļaujas, pag. -ļāvos", "ļauties"), //atļauties
		// M
		VerbRule.firstConjRefl("-maļos, -malies,", "-maļas, pag. -mālos", "malties"), //apmalties
		VerbRule.firstConjRefl("-metos, -meties,", "-metas, pag. -metos", "mesties"), //aizmesties
		VerbRule.firstConjRefl("-mērcos, -mērcies,", "-mērcas, pag. -mērcos", "mērkties"), //iemērkties
		VerbRule.firstConjRefl("-mostos, -mosties,", "-mostas, pag. -modos", "mosties"), //atmosties
		// N
		VerbRule.firstConjRefl("-nesos, -nesies,", "-nesas, pag. -nesos", "nesties"), //aiznesties
		VerbRule.firstConjRefl("-nirstos, -nirsties,", "-nirstas, pag. -niros", "nirties"), //ienirties
		// Ņ
		VerbRule.firstConjRefl("-ņemos, -ņemies,", "-ņemas, pag. -ņēmos", "ņemties"), //aizņemties
		// O, P
		VerbRule.firstConjRefl("-peros, -peries,", "-peras, pag. -pēros", "pērties"), //aizpērties
		VerbRule.firstConjRefl("-pērkos, -pērcies,", "-pērkas, pag. -pirkos", "pirkties"), // appirkties
		VerbRule.firstConjRefl("-pinos, -pinies,", "-pinas, pag. -pinos", "pīties"), // iepīties
		VerbRule.firstConjRefl("-pļaujos, -pļaujies,", "-pļaujas, pag. -pļāvos", "pļauties"), // appļauties
		VerbRule.firstConjRefl("-pūšos, -pūties,", "-pūšas, pag. -pūtos", "pūsties"), // atpūsties
		// R
		VerbRule.firstConjRefl("-rokos, -rocies,", "-rokas, pag. -rakos", "rakties"), // aizrakties
		VerbRule.firstConjRefl("-rodos, -rodies,", "-rodas, pag. -rados", "rasties"), // atrasties
		VerbRule.firstConjRefl("-raušos, -rausies,", "-raušas, pag. -rausos", "rausties"), //aizrausties
		VerbRule.firstConjRefl("-raujos, -raujies,", "-raujas, pag. -rāvos", "rauties"), //aizrauties
		VerbRule.firstConjRefl("-rāpjos, -rāpies,", "-rāpjas, pag. -rāpos", "rāpties"), // aizrāpties
		VerbRule.firstConjRefl("-riebjos, -riebies,", "-riebjas, pag. -riebos", "riebties"), //apriebties
		VerbRule.firstConjRefl("-riebjos, -riebies,", "-riebjas; pag. -riebos", "riebties"), //aizriebties
		VerbRule.firstConjRefl("-rejos, -rejies,", "-rejas, pag. -rējos", "rieties"), //atrieties
		VerbRule.firstConjRefl("-rimstos, -rimsties,", "-rimstas, pag. -rimos", "rimties"), //aprimties
		VerbRule.firstConjRefl("-rijos, -rijies,", "-rijas, pag. -rijos", "rīties"), //aizrīties
		// S
		VerbRule.firstConjRefl("-saucos, -saucies,", "-saucas, pag. -saucos", "saukties"), //aizsaukties
		VerbRule.firstConjRefl("-sedzos, -sedzies,", "-sedzas, pag. -sedzos", "segties"), //aizsegties
		VerbRule.firstConjRefl("-sienos, -sienies,", "-sienas, pag. -sējos", "sieties"), //aizsieties
		VerbRule.firstConjRefl("-sitos, -sities,", "-sitas, pag. -sitos", "sisties"), //aizsisties
		VerbRule.firstConjRefl("-skaišos, -skaisties,", "-skaišas, pag. -skaitos", "skaisties"), //apskaisties
		VerbRule.firstConjRefl("-skaujos, -skaujies,", "-skaujas, pag. -skāvos", "skauties"), //apskauties
		VerbRule.firstConjRefl("-slēpjos, -slēpies,", "-slēpjas, pag. -slēpos", "slēpties"), //aizslēpties
		VerbRule.firstConjRefl("-sliecos, -sliecies,", "-sliecas, pag. -sliecos", "sliekties"), //aizsliekties
		VerbRule.firstConjRefl("-smejos, -smejies,", "-smejas, pag. -smējos", "smieties"), //atsmieties
		VerbRule.firstConjRefl("-sniedzos, -sniedzies,", "-sniedzas, pag. -sniedzos", "sniegties"), //aizsniegties
		VerbRule.firstConjRefl("-speros, -speries,", "-speras, pag. -spēros", "sperties"), //atsperties
		VerbRule.firstConjRefl("-spiežos, -spiedies,", "-spiežas, pag. -spiedos", "spiesties"), //aizspiesties
		VerbRule.firstConjRefl("-spraucos, -spraucies,", "-spraucas, pag. -spraucos", "spraukties"), //aizspraukties
		VerbRule.firstConjRefl("-spraužos, -spraudies,", "-spraužas, pag. -spraudos", "sprausties"), //aizsprausties
		VerbRule.firstConjRefl("-spriežos, -spriedies,", "-spriežas, pag. -spriedos", "spriesties"), //aizspriesties
		VerbRule.firstConjRefl("-stājos, -stājies,", "-stājas, pag. -stājos", "stāties"), //aizstāties
		VerbRule.firstConjRefl("-steidzos, -steidzies,", "-steidzas, pag. -steidzos", "steigties"), //aizsteigties
		VerbRule.firstConjRefl("-stiepjos, -stiepies,", "-stiepjas, pag. -stiepos", "stiepties"), //aizstiepties
		VerbRule.firstConjRefl("-stumjos, -stumies,", "-stumjas, pag. -stūmos", "stumties"), //aizstumties
		VerbRule.firstConjRefl("-sveros, -sveries,", "-sveras, pag. -svēros", "svērties"), //apsvērties
		VerbRule.firstConjRefl("-sviežos, -sviedies,", "-sviežas, pag. -sviedos", "sviesties"), //aizsviesties
		// Š
		VerbRule.firstConjRefl("-šaujos, -šaujies,", "-šaujas, pag. -šāvos", "šauties"), //aizšauties
		VerbRule.firstConjRefl("-šķeļos, -šķelies,", "-šķeļas, pag. -šķēlos", "šķelties"), //atšķelties
		VerbRule.firstConjRefl("-šķiežos, -šķiedies,", "-šķiežas, pag. -šķiedos", "šķiesties"), //apsķiesties
		VerbRule.firstConjRefl("-šķiros, -šķiries,", "-šķiras, pag. -šķīros", "šķirties"), //atšķirties
		VerbRule.firstConjRefl("-šļācos, -šļācies,", "-šļācas, pag. -šļācos", "šļākties"), //apšļākties
		VerbRule.firstConjRefl("-šmaucos, -šmaucies,", "-šmaucas, pag. -šmaucos", "šmaukties"), //apšmaukties
		// T
		VerbRule.firstConjRefl("-tērpjos, -tērpies,", "-tērpjas; pag. -tērpos", "tērpties"), //aptērpties
		VerbRule.firstConjRefl("-tinos, -tinies,", "-tinas, pag. -tinos", "tīties"), //aiztīties
		VerbRule.firstConjRefl("-traucos, -traucies,", "-traucas, pag. -traucos", "traukties"), //aiztraukties
		VerbRule.firstConjRefl("-triecos, -triecies,", "-triecas, pag. -triecos", "triekties"), //attriekties
		VerbRule.firstConjRefl("-triepjos, -triepies,", "-triepjas, pag. -triepos", "triepties"), //aptriepties
		VerbRule.firstConjRefl("-tupstos, -tupsties,", "-tupstas, pag. -tupos", "tupties"), //aiztupties
		// U
		VerbRule.firstConjRefl("-urbjos, -urbies,", "-urbjas, pag. -urbos", "urbties"), //aizurbties
		// V
		VerbRule.firstConjRefl("-vācos, -vācies,", "-vācas, pag. -vācos", "vākties"), //aizvākties
		VerbRule.firstConjRefl("-veļos, -velies,", "-veļas, pag. -vēlos", "velties"), //aizvelties
		VerbRule.firstConjRefl("-vērpjos, -vērpies,", "-vērpjas, pag. -vērpos", "vērpties"), //apvērpties
		VerbRule.firstConjRefl("-vēršos, -vērsies,", "-vēršas, pag. -vērsos", "vērsties"), //aizvērsties
		VerbRule.firstConjRefl("-velkos, -velcies,", "-velkas, pag. -vilkos", "vilkties"), //aizvilkties
		VerbRule.firstConjRefl("-vijos, -vijies,", "-vijas, pag. -vijos", "vīties"), //apvīties
		// Z
		VerbRule.firstConjRefl("-zogos, -zodzies,", "-zogas, pag. -zagos", "zagties"), //aizzagties
		VerbRule.firstConjRefl("-ziežos, -ziedies,", "-ziežas, pag. -ziedos", "ziesties"), //apziesties
		VerbRule.firstConjRefl("-zveļos, -zvelies,", "-zveļas, pag. -zvēlos", "zvelties"), //atzvelties

		// Vairāki lemmas varianti.
		VerbRule.firstConjRefl("-lecos, -lecies,", "-lecas, pag. -lēcos", "l[eē]kties"), //atlēkties, izlekties


		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas
		SimpleRule.of("parasti 3. pers., -tīkas, pag. -tikās", ".*tikties", 15,
				new Tuple[] {Features.POS__VERB,
						Tuple.of(Keys.INFLECT_AS, "\"tikties\" (kā patikties kādam)"),
						Features.INFINITIVE_HOMOFORMS},
				new Tuple[] {Features.USUALLY_USED__THIRD_PERS}), //patikties
		// Paralēlās formas.
		SimpleRule.of("parasti 3. pers., -plešas, pag. -pletās, arī -plētās",
				".*plesties", 18,
				new Tuple[]{Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"plesties\""),
						Features.PARALLEL_FORMS},
				new Tuple[]{Features.USUALLY_USED__THIRD_PERS}), //aizplesties

		// Pilnīgs nestandarts.
		SimpleRule.of("-teicos, -teicies, -teicas (tagadnes formas parasti nelieto), pag. -teicos", ".*teikties", 18,
				new Tuple[]{Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "\"teikties\"")},
				new Tuple[]{Tuple.of(Keys.USUALLY_USED_IN_FORM, "Nelieto tagadnes formas")}), //atteikties
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
		SimpleRule.of(
				"-mokos, -mokies, -mokās, arī -mocos, -mocies, -mocās, pag. -mocījos",
				".*mocīties", 20,
				new Tuple[]{Features.POS__VERB, Features.PARALLEL_FORMS,
						Features.HAS_PRESENT_SOUNDCHANGE, Features.NO_PRESENT_SOUNDCHANGE},
				null), //aizmocīties

		// Standartizētie.
		VerbRule.thirdConjRefl("-braukos, -braukies,", "-braukās, pag. -braucījos", "braucīties", true), //atbraucīties
		VerbRule.thirdConjRefl("-čabinos, -čabinies,", "-čabinās, pag. -čabinājos", "čabināties", false), //iečabināties
		VerbRule.thirdConjRefl("-čukstos, -čuksties,", "-čukstas, pag. -čukstējos", "čukstēties", false), //iečukstēties
		VerbRule.thirdConjRefl("-deros, -deries,", "-deras, pag. -derējos", "derēties", false), //iederēties
		VerbRule.thirdConjRefl("-kustos, -kusties,", "-kustas, pag. -kustējos", "kustēties", false), //aizkustēties
		VerbRule.thirdConjRefl("-lāpos, -lāpies,", "-lāpās, pag. -lāpījos", "lāpīties", false), //aplāpīties
		VerbRule.thirdConjRefl("-lokos, -lokies,", "-lokās, pag. -locījos", "locīties", true), //atlocīties
		VerbRule.thirdConjRefl("-minos, -minies,", "-minas, pag. -minējos", "minēties", false), //atminēties
		VerbRule.thirdConjRefl("-mīlos, -mīlies,", "-mīlas, pag. -mīlējos", "mīlēties", false), //iemīlēties
		VerbRule.thirdConjRefl("-muldos, -muldies,", "-muldas, pag. -muldējos", "muldēties", false), //iemuldēties 1, 2
		VerbRule.thirdConjRefl("-precos, -precies,", "-precas, pag. -precējos", "precēties", false), //aizprecēties
		VerbRule.thirdConjRefl("-raugos, -raugies,", "-raugās, pag. -raudzījos", "raudzīties", true), //apraudzīties
		VerbRule.thirdConjRefl("-sakos, -sakies,", "-sakās, pag. -sacījos", "sacīties", true), //atsacīties
		VerbRule.thirdConjRefl("-slakos, -slakies,", "-slakās, pag. -slacījos", "slacīties", true), //apslacīties
		VerbRule.thirdConjRefl("-slaukos, -slaukies,", "-slaukās, pag. -slaucījos", "slaucīties", true), //apslaucīties
		VerbRule.thirdConjRefl("-turos, -turies,", "-turas, pag. -turējos", "turēties", false), //atturēties
		VerbRule.thirdConjRefl("-vēlos, -vēlies,", "-vēlas, pag. -vēlējos", "vēlēties", false), //atvēlēties
		VerbRule.thirdConjRefl("-zinos, -zinies,", "-zinās, pag. -zinājos", "zināties", true), //apzināties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		ThirdPersVerbRule.thirdConjRefl("-lokās, pag. -locījās", "locīties", true), //aizlocīties

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
		ComplexRule.secondThirdConjDirectAllPers(
				"-gailējos, -gailējies, -gailējās, arī -gailos, -gailies, -gailas, pag. -gailējos",
				".*gailēties", false), // iegailēties
		ComplexRule.secondThirdConjDirectAllPers(
				"-sargājos, -sargājies, -sargājas, arī -sargos, -sargies, -sargās, pag. -sargājos",
				".*sargāties", false), // aizsargāties
		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nav.
	};

}
