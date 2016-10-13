package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns.*;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.FirstConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.SecondConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.SecondThirdConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.ThirdConj;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.*;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.Tuple;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * TGram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar Rule.applyOptHyphens().
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā, vai konkrēti
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
		VerbDoubleRule.of("-eju, -ej,", "-iet, pag. -gāju", "iet", 29,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "iet"), TFeatures.POS__IRREG_VERB, TFeatures.POS__DIRECT_VERB}, null), //apiet
		VerbDoubleRule.of("-ejos, -ejies,", "-ietas, pag. -gājos", "ieties", 29,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "iet"), TFeatures.POS__IRREG_VERB, TFeatures.POS__REFL_VERB}, null), //apieties\

			// 10. paradigma: 5. deklinācija, vīriešu dzimte.
		GenNoun.any("-tētes, dsk. ģen. -tētu, v.", ".*tēte", 10,
				new Tuple[]{TFeatures.NO_SOUNDCHANGE}, new Tuple[]{TFeatures.GENDER__MASC}), // tēte
		// 25. paradigma: vietniekvārdi.
		//BaseRule.of("ģen. -kā, dat. -kam, akuz., instr. -ko", ".*kas", 25,
		//		new Tuple[]{TFeatures.POS__PRONOUN, Tuple.of(TKeys.INFLECT_AS, "\"kas\"")},
		//		null), //daudzka
	};
	/**
	 * Paradigm 9: Lietvārds 5. deklinācija -e
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] fifthDeclNoun = {
		// Ar mijām
		FifthDecl.std("-aļģes, dsk. ģen. -aļģu, s.", ".*aļģe"), // kramaļģe, aļģe
		FifthDecl.std("-audzes, dsk. ģen. -audžu, s.", ".*audze"), // brūkleņaudze
		FifthDecl.std("-ātes, dsk. ģen. -āšu, s.", ".*āte"), //āte
		FifthDecl.std("-balles, dsk. ģen. -baļļu, s.", ".*balle"), // balle 1
		FifthDecl.std("-celles, dsk. ģen. -ceļļu, s.", ".*celle"), // celle
		FifthDecl.std("-cemmes, dsk. ģen. -cemmju, s.", ".*cemme"), // cemme
		FifthDecl.std("-dēles, dsk. ģen. -dēļu, s.", ".*dēle"), // dēle
		FifthDecl.std("-eģes, dsk. ģen. -eģu, s.", ".*eģe"), // eģe
		FifthDecl.std("-elles, dsk. ģen. -eļļu, s.", ".*elle"), // elle
		FifthDecl.std("-eņģes, dsk. ģen. -eņģu, s.", ".*eņģe"), // eņģe
		FifthDecl.std("-epiķes, dsk. ģen. -epiķu", ".*epiķe"), // epiķe
		FifthDecl.std("-ēzes, dsk. ģen. -ēžu, s.", ".*ēze"), // ēze
		FifthDecl.std("-halles, dsk. ģen. -haļļu, s.", ".*halle"), // halle
		FifthDecl.std("-indes, dsk. ģen. -inžu, s.", ".*inde"), // inde
		FifthDecl.std("-īzes, dsk. ģen. -īžu, s.", ".*īze"), // īze
		FifthDecl.std("-ķelles, dsk. ģen. -ķeļļu, s.", ".*ķelle"), // ķelle
		FifthDecl.std("-ķemmes, dsk. ģen. -ķemmju, s.", ".*ķemme"), // ķemme
		FifthDecl.std("-lodes, dsk. ģen. -ložu, s.", ".*lode"), // deglode
		FifthDecl.std("-nulles, dsk. ģen. -nuļļu, s.", ".*nulle"), // nulle
		FifthDecl.std("-ores, dsk. ģen. -oru, s.", ".*ore"), // ore
		FifthDecl.std("-resnes, dsk. ģen. -rešņu, s.", ".*resne"), // resne
		FifthDecl.std("-teces, dsk. ģen. -teču, s.", ".*tece"), // tece
		FifthDecl.std("-upes, dsk. ģen. -upju, s.", ".*upe"), // upe, krāčupe
		FifthDecl.std("-usnes, dsk. ģen. -ušņu, s.", ".*usne"), // usne
		FifthDecl.std("-vātes, dsk. ģen. -vāšu, s.", ".*vāte"), // vāte
		FifthDecl.std("-zīmes, dsk. ģen. -zīmju, s.", ".*zīme"), // biedruzīme
		// Bez mijām
		FifthDecl.noChange("-astes, dsk. ģen. -astu, s.", ".*aste"), // ragaste
		FifthDecl.noChange("-balles, dsk. ģen. -ballu, s.", ".*balle"), //balle 2
		SixthDecl.noChange("-gāzes, dsk. ģen. -gāzu, s.", ".*gāze"), //deggāze
	};

	/**
	 * Paradigmas 11, 35.: Lietvārds 6.. deklinācija, siev.dz.
	 * Likumi formā "-acs, dsk. ģen. -acu, s.".
	 */
	public static final Rule[] sixthDeclNoun = {
		// Ar mijām
		SixthDecl.std("-avs, dsk. ģen. -avju, s.", ".*avs"), //avs
		SixthDecl.std("-birzs, dsk. ģen. -biržu, s.", ".*birzs"), //birzs
		SixthDecl.std("-blakts, dsk. ģen. -blakšu, s.", ".*blakts"), //blakts
		SixthDecl.std("-cilts, dsk. ģen. -cilšu, s.", ".*cilts"), //cilts
		SixthDecl.std("-drāts, dsk. ģen. -drāšu, s.", ".*drāts"), //drāts
		SixthDecl.std("-dzelzs, dsk. ģen. -dzelžu, s.", ".*dzelzs"), //dzelzs, leņķdzelzs
		SixthDecl.std("-dzimts, dsk. ģen. -dzimšu, s.", ".*dzimts"), //dzimts
		SixthDecl.std("-dzirksts, dsk. ģen. -dzirkšu", ".*dzirksts"), //dzirksts
		SixthDecl.std("-govs, dsk. ģen. -govju, s.", ".*govs"), //govs
		SixthDecl.std("-grunts, dsk. ģen. -grunšu, s.", ".*grunts"), //grunts
		SixthDecl.std("-guns, dsk. ģen. -guņu", ".*guns"), //guns
		SixthDecl.std("-igvāts, dsk. ģen. -igvāšu, s.", ".*igvāts"), //igvāts
		SixthDecl.std("-ilkss, dsk. ģen. -ilkšu, s.", ".*ilkss"), //ilkss
		SixthDecl.std("-izkapts, dsk. ģen. -izkapšu, s.", ".*izkapts"), //izkapts
		SixthDecl.std("-kārts, dsk. ģen. -kāršu, s.", ".*kārts"), //kārts 1, 2
		SixthDecl.std("-klēts, dsk. ģen. -klēšu, s.", ".*klēts"), //klēts
		SixthDecl.std("-klints, dsk. ģen. klinšu, s.", ".*klints"), //klints
		SixthDecl.std("-krants, dsk. ģen. -kranšu, s.", ".*krants"), //krants
		SixthDecl.std("-krāsns, dsk. ģen. -krāšņu, s.", ".*krāsns"), //aizkrāsns
		SixthDecl.std("-krūts, dsk. ģen. -krūšu, s.", ".*krūts"), //galvkrūts
		SixthDecl.std("-kūts, dsk. ģen. -kūšu, s.", ".*kūts"), //cūkkūts
		SixthDecl.std("-kvīts, dsk. ģen. -kvīšu, s.", ".*kvīts"), //kvīts
		SixthDecl.std("-lākts, dsk. ģen. -lākšu, s.", ".*lākts"), //lākts
		SixthDecl.std("-lecekts, dsk. ģen. -lecekšu, s.", ".*lecekts"), //lecekts
		SixthDecl.std("-līksts, dsk. ģen. -līkšu, s.", ".*līksts"), //līksts
		SixthDecl.std("-maiksts, dsk. ģen. -maikšu, s.", ".*maiksts"), //maiksts
		SixthDecl.std("-nakts, dsk. ģen. -nakšu, s.", ".*nakts"), //diennakts
		SixthDecl.std("-nīts, dsk. ģen. -nīšu, s.", ".*nīts"), //nīts
		SixthDecl.std("-nots, dsk. ģen. -nošu, s.", ".*nots"), // nošu
		SixthDecl.std("-nūts, dsk. ģen. -nūšu, s.", ".*nūts"), // nūts
		SixthDecl.std("-olekts, dsk. ģen. -olekšu, s.", ".*olekts"), // olekts
		SixthDecl.std("-pāksts, dsk. ģen. -pākšu, s.", ".*pāksts"), // pāksts
		SixthDecl.std("-palts, dsk. ģen. -palšu, s.", ".*palts"), // palts
		SixthDecl.std("-pirts, dsk. ģen. -piršu, s.", ".*pirts"), // asinspirts
		SixthDecl.std("-pils, dsk. ģen. -piļu, s.", ".*pils"), // ordeņpils
		SixthDecl.std("-plāts, dsk. ģen. -plāšu, s.", ".*plāts"), // plāts
		SixthDecl.std("-pults, dsk. ģen. -pulšu, s.", ".*pults"), // operatorpults
		SixthDecl.std("-rūts, dsk. ģen. -rūšu, s.", ".*rūts"), // rūts
		SixthDecl.std("-sāls, dsk. ģen. -sāļu, s.", ".*sāls"), // sāls
		SixthDecl.std("-silkss, dsk. ģen. -silkšu, s.", ".*silkss"), // silkss
		SixthDecl.std("-sirds, dsk. ģen. -siržu, s.", ".*sirds"), // sirds
		SixthDecl.std("-skansts, dsk. ģen. -skanšu, s.", ".*skansts"), // skansts
		SixthDecl.std("-skrots, dsk. ģen. -skrošu, s.", ".*skrots"), // skrots
		SixthDecl.std("-spelts, dsk. ģen. -spelšu, s.", ".*spelts"), // spelts
		SixthDecl.std("-spīts, dsk. ģen. -spīšu, s.", ".*spīts"), // spīts
		SixthDecl.std("-stelts, dsk. ģen. stelšu, s.", ".*stelts"), // stelts
		SixthDecl.std("-tāss, dsk. ģen. -tāšu, s.", ".*tāss"), // tāss
		SixthDecl.std("-telts, dsk. ģen. -telšu, s.", ".*telts"), // telts
		SixthDecl.std("-uguns, dsk. ģen. -uguņu, s.", ".*uguns"), // jāņuguns
		SixthDecl.std("-vāts, dsk. ģen. -vāšu, s.", ".*vāts"), // vāts
		SixthDecl.std("-zivs, dsk. ģen. -zivju, s.", ".*zivs"), // haizivs

		// Bez mijām
		SixthDecl.noChange("-acs, dsk. ģen. -acu, s.", ".*acs"), //uzacs, acs
		SixthDecl.noChange("-ass, dsk. ģen. -asu, s.", ".*ass"), //ass
		SixthDecl.noChange("-auss, dsk. ģen. -ausu, s.", ".*auss"), //auss
		SixthDecl.noChange("-balss, dsk. ģen. -balsu, s.", ".*balss"), //atbalss
		SixthDecl.noChange("-dakts, dsk. ģen. -daktu, s.", ".*dakts"), //dakts
		SixthDecl.noChange("-grīsts, dsk. ģen. -grīstu, s.", ".*grīsts"), //grīsts
		SixthDecl.noChange("-debess, dsk. ģen. -debesu, s.", ".*debess"), //padebess
		SixthDecl.noChange("-maksts, dsk. ģen. -makstu, s.", ".*maksts"), //maksts
		SixthDecl.noChange("-nāss, dsk. ģen. -nāsu, s.", ".*nāss"), //nāss
		SixthDecl.noChange("-šalts, dsk. ģen. -šaltu, s.", ".*šalts"), // šalts
		SixthDecl.noChange("-takts, dsk. ģen. -taktu, s.", ".*takts"), // pietakts
		SixthDecl.noChange("-uts, dsk. ģen. -utu, s.", ".*uts"), // uts
		SixthDecl.noChange("-valsts, dsk. ģen. -valstu, s.", ".*valsts"), //agrārvalsts
		SixthDecl.noChange("-vakts, dsk. ģen. -vakšu, s.", ".*vakts"), //vakts
		SixthDecl.noChange("-versts, dsk. ģen. -verstu, s.", ".*versts"), // kvadrātversts
		SixthDecl.noChange("-vēsts, dsk. ģen. -vēstu, s.", ".*vēsts"), // vēsts
		SixthDecl.noChange("-zoss, dsk. ģen. -zosu, s.", ".*zoss"), // mežazoss

		SixthDecl.optChange("-dūksts, dsk. ģen. -dūkstu, arī -dūkšu, s.", ".*dūksts"), //dūksts
		SixthDecl.optChange("-dzeņauksts, dsk. ģen. -dzeņaukstu, arī -dzeņaukšu, s.", ".*dzeņauksts"), //dzeņauksts
	};

	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final Rule[] secondDeclNoun = {
		SecondDecl.std("-āķa, v.", ".*āķis"), // ākis
		SecondDecl.std("-aļņa, v.", ".*alnis"), // alnis
		SecondDecl.std("-āmja, v.", ".*āmis"), // āmis

		GenNoun.any("-tēta, v.", ".*tētis", 3,
					new Tuple[]{TFeatures.NO_SOUNDCHANGE}, new Tuple[]{TFeatures.GENDER__MASC}), // tētis

	};
	/**
	 * Paradigm 6: Lietvārds 3. deklinācija -us
	 */
	public static final Rule[] thirdDeclNoun = {
			ThirdDecl.std("-alus, v.", ".*alus"), // alus

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final Rule[] directFirstConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Netoteiksmes homoformas.
		FirstConj.directHomof("-aužu, -aud,", "-auž, pag. -audu", "aust",
				"\"aust\" (audumu)"), //aizaust 2
		FirstConj.directHomof("-dedzu, -dedz,", "-dedz, pag. -dedzu", "degt",
				"\"degt\" (kādu citu)"), //aizdegt 1
		FirstConj.directHomof("-degu, -dedz,", "-deg, pag. -degu", "degt",
				"\"degt\" (pašam)"), //apdegt, aizdegt 2
		FirstConj.directHomof("-dzenu, -dzen,", "-dzen, pag. -dzinu", "dzīt",
				"\"dzīt\" (lopus)"), //aizdzīt 1
		FirstConj.directHomof("-iru, -ir,", "-ir, pag. -īru", "irt",
				"\"irt\" (ar airiem)"), //aizirt 1
		FirstConj.directHomof("-lienu, -lien,", "-lien, pag. -līdu",
				"līst", "\"līst\" (zem galda)"), //aizlīst, ielīst 1
		FirstConj.directHomof("-līžu, -līd,", "-līž, pag. -līdu", "līst",
				"\"līst\" (līdumu)"), //ielīst 2
		FirstConj.directHomof("-mītu, -mīti,", "-mīt, pag. -mitu", "mist",
				"\"mist\" (mitināties)"), //iemist, izmist 2
		FirstConj.directHomof("-mistu, -misti,", "-mist, pag. -misu", "mist",
				"\"mist\" (krist izmisumā)"), //izmist 1
		FirstConj.directHomof("-minu, -min,", "-min, pag. -minu", "mīt",
				"\"mīt\" (pedāļus)"), //aizmīt 1
		FirstConj.directHomof("-miju, -mij,", "-mij, pag. -miju", "mīt",
				"\"mīt\" (mainīt naudu)"), //aizmīt 2
		FirstConj.directHomof("-spriedzu, -spriedz,", "-spriedz, pag. -spriedzu", "spriegt",
				"\"spriegt\" (kādu citu)"), //nospriegt, saspriegt 1
		FirstConj.directHomof("-spriegstu, -spriegsti,", "-spriegst, pag. -spriegu", "spriegt",
				"\"spriegt\" (pašam)"), //saspriegt 2
		FirstConj.directHomof("-sprindzu, -sprindz,", "-sprindz, pag. -sprindzu", "springt",
				"\"springt\" (kādu citu)"), //saspringt 2
		FirstConj.directHomof("-springstu, -springsti,", "-springst, pag. -springu", "springt",
				"\"springt\" (pašam)"), //saspringt 1
		FirstConj.directHomof("-tieku, -tiec,", "-tiek, pag. -tiku", "tikt",
				"\"tikt\" (nokļūt kaut kur)"), //aiztikt 1, 2
		FirstConj.directHomof("-tīku, -tīc,", "-tīk, pag. -tiku", "tikt",
				"\"tikt\" (patikt kādam)"), //patikt
		// Pēc mokošām pārrunām. Paldies, Vent Zvaigzne, paldies, Aldi Lauzi!
		FirstConj.directHomof("-verdu, -verd,", "-verd, pag. -virdu", "virst",
				"\"virst\" (izšļākt)"), //izvirst 2, virst 1 || Tēzaurā neieliktā alternatīva: -verdu, -verd, -verd, retāk -verdu, -vird, -vird, pag. -virdu
		FirstConj.directHomof("-virstu, -virsti,", "-virst, pag. -virtu", "virst",
				"\"virst\" (zaudēt tikumiskās īpašības)"), //izvirst 1, virst 2
		FirstConj.directHomof("-verdu, -verd,", "-verd, pag. -viru", "virt",
				"\"virt\" (pašam)"), //aizvirt, uzvirt, virt 1
		FirstConj.directHomof("-viru, -vir,", "-vir, pag. -viru", "virt",
				"\"virt\" (kādu citu)"), // virt 2
		// Izņēmuma izņēmums :/
		FirstConj.directHomof("-patīku, -patīc,", "-patīk, pag. -patiku", "tikt",
				"\"tikt\" (patikt kādam)"), //patikt
		FirstConj.directHomof("-vēršu, -vērs,", "-vērš, pag. -vērsu", "vērst",
				"\"vērst\" (mainīt virzienu)"), //aizvērst, izvērst 1
		FirstConj.directHomof("-vēršu, -vērt,", "-vērš, pag. -vērtu", "vērst",
				"\"vērst\" (mainīt būtību)"), //izvērst 2


			// Paralēlās formas.
		FirstConj.directAllPersParallel(
				"-auju, -auj, -auj, arī -aunu, -aun, -aun, pag. -āvu", "aut"), //apaut
		FirstConj.directAllPersParallel(
				"-gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu", "gult"), //aizgult
		FirstConj.directAllPersParallel(
				"-jaušu, -jaut, -jauš, pag. -jautu, arī -jaužu, -jaud, -jauž, pag. -jaudu", "jaust"), //apjaust
		FirstConj.directAllPersParallel(
				"-jumju, -jum, -jumj, pag. -jūmu, arī -jumu", "jumt"), //aizjumt
		FirstConj.directAllPersParallel(
				"-kaistu, -kaisti, -kaist, pag. -kaisu, retāk -kaitu", "kaist"), //iekaist
		FirstConj.directAllPersParallel(
				"-kaistu, -kaisti, -kaist, pag. -kaisu, arī -kaitu", "kaist"), //nokaist
		FirstConj.directAllPersParallel(
				"-kūpu, -kūpi, -kūp, arī -kūpstu, -kūpsti, -kūpst, pag. -kūpu", "kūpt"), //nokūpt
		FirstConj.directAllPersParallel(
				"-kvēpstu, -kvēpsti, -kvēpst, arī -kvēpu, -kvēp, -kvēp, pag. -kvēpu", "kvēpt"), //nokvēpt
		FirstConj.directAllPersParallel(
				"-meju, -mej, -mej, pag. -mēju, arī -mienu, -mien, -mien, pag. -mēju", "miet"), //iemiet
		FirstConj.directAllPersParallel(
				"-meju, -mej, -mej, arī -mienu, -mien, -mien, pag. -mēju", "miet"), // samiet
		FirstConj.directAllPersParallel(
				"-plešu, -plet, -pleš, pag. -pletu, arī -plētu", "plest"), //aizplest
		FirstConj.directAllPersParallel(
				"-purstu, -pursti, -purst, pag. -pūru, arī -pūru", "purt"), //papurt
		FirstConj.directAllPersParallel(
				"-riešu, -ries, -rieš, pag. -rietu, arī -riesu", "riest"), //ieriest 2
		FirstConj.directAllPersParallel(
				"-skārstu, -skārsti, -skārst, arī -skāršu, -skārt, -skārš, pag. -skārtu", "skārst"), //apskārst
		FirstConj.directAllPersParallel(
				"-skrienu, -skrien, -skrien, arī -skreju, -skrej, -skrej, pag. -skrēju", "skriet"), //aizskriet
		FirstConj.directAllPersParallel(
				"-slapstu, -slapsti, -slapst, retāk -slopu, -slopi, -slop, pag. -slapu", "slapt"), //apslapt
		FirstConj.directAllPersParallel(
				"-slienu, -slien, -slien, arī -sleju, -slej, -slej, pag. -slēju", "sliet"), //aizsliet
		FirstConj.directAllPersParallel(
				"-spurdzu, -spurdz, -spurdz, arī -spurgst, pag. -spurdzu", "spurgt"), //aizspurgt
		FirstConj.directAllPersParallel(
				"-spurdzu, -spurdz, -spurdz, pag. -spurdzu, retāk -spurgstu, -spurgsti, -spurgst, pag. -spurgu", "spurgt"), //iespurgt
		FirstConj.directAllPersParallel(
				"-spurstu, -spursti, -spurst, pag. -spuru, arī -spūru", "spurt"), //izspurt
		FirstConj.directAllPersParallel(
				"-stīdzu, -stīdz, -stīdz, pag. -stīdzu, retāk -stīgstu, -stīgsti, -stīgst, pag. -stīgu", "stīgt"), //izstīgt
		FirstConj.directAllPersParallel(
				"-strēdzu, -strēdz, -strēdz, pag. -strēdzu, arī -strēgstu, -strēgsti, -strēgst, pag. -strēgu", "strēgt"), //iestrēgt
		FirstConj.directAllPersParallel(
				"-tilpstu, -tilpsti, -tilpst, retāk -telpu, telpi, -telp, pag. -tilpu", "tilpt"), //ietilpt
		FirstConj.directAllPersParallel(
				"-viešu, -vies, -vieš, retāk -viežu, -vied, -viež, pag. -viedu", "viest"), //saviest

		// Izņēmums.
		VerbDoubleRule.of("-pārdodu, -pārdod,", "-pārdod, pag. -pārdevu", "pārdot", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"dot\"")}, null,
				new String[]{"do"}, new String[]{"dod"}, new String[]{"dev"}), //izpārdot
		VerbDoubleRule.of("-palieku, -paliec,", "-paliek, pag. -paliku", "palikt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"likt\"")}, null,
				new String[]{"lik"}, new String[]{"liek"}, new String[]{"lik"}), //izpalikt
		VerbDoubleRule.of("-pazīstu, -pazīsti,", "-pazīst, pag. -pazinu", "pazīt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"zīt\"")}, null,
				new String[]{"zī"}, new String[]{"zīst"}, new String[]{"zin"}), //atpazīt

		// Standartizētie.
		// A
		FirstConj.direct("-alkstu, -alksti,", "-alkst, pag. -alku", "alkt"), // alkt
		FirstConj.direct("-aru, -ar,", "-ar, pag. -aru", "art"), //aizart
		FirstConj.direct("-augu, -audz,", "-aug, pag. -augu", "augt"), //ieaugt, aizaugt
		// B
		FirstConj.direct("-baru, -bar,", "-bar, pag. -bāru", "bārt"), //iebārt
		FirstConj.direct("-bāžu, -bāz,", "-bāž, pag. -bāzu", "bāzt"), //aizbāzt
		FirstConj.direct("-beidzu, -beidz,", "-beidz, pag. -beidzu", "beigt"), //izbeigt
		FirstConj.direct("-belžu, -belz,", "-belž, pag. -belzu", "belzt"), //iebelzt
		FirstConj.direct("-beržu, -berz,", "-berž, pag. -berzu", "berzt"), //atberzt
		FirstConj.direct("-bēgu, -bēdz,", "-bēg, pag. -bēgu", "bēgt"), //aizbēgt
		FirstConj.direct("-beru, -ber,", "-ber, pag. -bēru", "bērt"), //aizbērt
		FirstConj.direct("-bilstu, -bilsti,", "-bilst, pag. -bildu", "bilst"), //aizbilst
		FirstConj.direct("-birstu, -birsti,", "-birst, pag. -biru", "birt"), //apbirt, aizbirt
		FirstConj.direct("-blenžu, -blenz,", "-blenž, pag. -blenzu", "blenzt"), //pablenzt
		FirstConj.direct("-bliežu, -bliez,", "-bliež, pag. -bliezu", "bliezt"), //iebliezt
		FirstConj.direct("-blīstu, -blīsti,", "-blīst, pag. -blīdu", "blīst"), //izblīst
		FirstConj.direct("-blīstu, -blīsti,", "-blīst, pag. -blīzu", "blīzt"), //izblīzt
		FirstConj.direct("-bļauju, -bļauj,", "-bļauj, pag. -bļāvu", "bļaut"), //atbļaut
		FirstConj.direct("-braucu, -brauc,", "-brauc, pag. -braucu", "braukt"), //aizbraukt
		FirstConj.direct("-brāžu, -brāz,", "-brāž, pag. -brāzu", "brāzt"), //aizbrāzt
		FirstConj.direct("-brēcu, -brēc,", "-brēc, pag. -brēcu", "brēkt"), //atbrēkt
		FirstConj.direct("-briestu, -briesti,", "-briest, pag. -briedu", "briest"), //nobriest
		FirstConj.direct("-brienu, -brien,", "-brien, pag. -bridu", "brist"), //aizbrist
		FirstConj.direct("-brūku, -brūc,", "-brūk, pag. -bruku", "brukt"), //iebrukt
		FirstConj.direct("-buru, -bur,", "-bur, pag. -būru", "burt"), //apburt
		// C
		FirstConj.direct("-ceļu, -cel,", "-ceļ, pag. -cēlu", "celt"), //aizcelt
		FirstConj.direct("-cepu, -cep,", "-cep, pag. -cepu", "cept"), //apcept
		FirstConj.direct("-ciešu, -ciet,", "-cieš, pag. -cietu", "ciest"), //izciest
		FirstConj.direct("-cērpu, -cērp,", "-cērp, pag. -cirpu", "cirpt"), //apcirpt
		FirstConj.direct("-cērtu, -cērt,", "-cērt, pag. -cirtu", "cirst"), //aizcirst
		// Č
		FirstConj.direct("-čiepju, -čiep,", "-čiepj, pag. -čiepu", "čiept"), //izčiept
		FirstConj.direct("-čupstu, -čupsti,", "-čupst, pag. -čupu", "čupt"), //sačupt
		// D
		FirstConj.direct("-dēju, -dēj,", "-dēj, pag. -dēju", "dēt"), //izdēt
		FirstConj.direct("-diebju, -dieb,", "-diebj, pag. -diebu", "diebt"), //aizdiebt
		FirstConj.direct("-diedzu, -diedz,", "-diedz, pag. -diedzu", "diegt"), //aizdiegt 1,2
		FirstConj.direct("-deju, -dej,", "-dej, pag. -deju", "diet"), //izdiet
		FirstConj.direct("-dilstu, -dilsti,", "-dilst, pag. -dilu", "dilt"), //apdilt
		FirstConj.direct("-diršu, -dirs,", "-dirš, pag. -dirsu", "dirst"), //piedirst
		FirstConj.direct("-dīcu, -dīc,", "-dīc, pag. -dīcu", "dīkt"), //idīkt
		FirstConj.direct("-dobju, -dob,", "-dobj, pag. -dobu", "dobt"), //iedobt
		FirstConj.direct("-dodu, -dod,", "-dod, pag. -devu", "dot"), //aizdot
		FirstConj.direct("-drāžu, -drāz,", "-drāž, pag. -drāzu", "drāzt"), //aizdrāzt
		FirstConj.direct("-dugstu, -dugsti,", "-dugst, pag. -dugu", "dugt"), //sadugt
		FirstConj.direct("-duru, -dur,", "-dur, pag. -dūru", "durt"), //aizdurt
		FirstConj.direct("-dūcu, -dūc,", "-dūc, pag. -dūcu", "dūkt"), //aizdūkt
		FirstConj.direct("-dvešu, -dves,", "-dveš, pag. -dvesu", "dvest"), //apdvest
		FirstConj.direct("-dzeļu, -dzel,", "-dzeļ, pag. -dzēlu", "dzelt"), //atdzelt, aizdzelt
		FirstConj.direct("-dzeru, -dzer,", "-dzer, pag. -dzēru", "dzert"), //aizdzert
		FirstConj.direct("-dzēšu, -dzēs,", "-dzēš, pag. -dzēsu", "dzēst"), //apdzēst
		FirstConj.direct("-dzimstu, -dzimsti,", "-dzimst, pag. -dzimu", "dzimt"), //atdzimt
		FirstConj.direct("-dzirstu, -dzirsti,", "-dzirst, pag. -dzirdu", "dzirst"), //izdzirst
		FirstConj.direct("-dziestu, -dziesti,", "-dziest, pag. -dzisu", "dzist"), //atdzist
		// E
		FirstConj.direct("-elšu, -els,", "-elš, pag. -elsu", "elst"), //atelst
		// Ē
		FirstConj.direct("-ēdu, -ēd,", "-ēd, pag. -ēdu", "ēst"), //aizēst
		// F, G
		FirstConj.direct("-gaistu, -gaisti,", "-gaist, pag. -gaisu", "gaist"), //izgaist
		FirstConj.direct("-gaužu, -gaud,", "-gauž, pag. -gaudu", "gaust"), //nogaust
		FirstConj.direct("-gārdzu, -gārdz,", "-gārdz, pag. -gārdzu", "gārgt"), //izgārgt
		FirstConj.direct("-gāžu, -gāz,", "-gāž, pag. -gāzu", "gāzt"), //aizgāzt
		FirstConj.direct("-glaužu, -glaud,", "-glauž, pag. -glaudu", "glaust"), //aizglaust
		FirstConj.direct("-glābju, -glāb,", "-glābj, pag. -glābu", "glābt"), //izglābt
		FirstConj.direct("-grauju, -grauj,", "-grauj, pag. -grāvu", "graut"), //iegraut
		FirstConj.direct("-graužu, -grauz,", "-grauž, pag. -grauzu", "grauzt"), //aizgrauzt
		FirstConj.direct("-grābju, -grāb,", "-grābj, pag. -grābu", "grābt"), //aizgrābt
		FirstConj.direct("-grebju, -greb,", "-grebj, pag. -grebu", "grebt"), //apgrebt
		FirstConj.direct("-griežu, -griez,", "-griež, pag. -griezu", "griezt"), //aizgriezt 1, 2
		FirstConj.direct("-grimstu, -grimsti,", "-grimst, pag. -grimu", "grimt"), //atgrimt, aizgrimt
		FirstConj.direct("-grūžu, -grūd,", "-grūž, pag. -grūdu", "grūst"), //aizgrūst
		FirstConj.direct("-grūstu, -grūsti,", "-grūst, pag. -gruvu", "grūt"), //iegrūt
		FirstConj.direct("-gubstu, -gubsti,", "-gubst, pag. -gubu", "gubt"), //sagubt
		FirstConj.direct("-gumstu, -gumsti,", "-gumst, pag. -gumu", "gumt"), //iegumt
		FirstConj.direct("-gurstu, -gursti,", "-gurst, pag. -guru", "gurt"), //apgurt
		FirstConj.direct("-gūstu, -gūsti,", "-gūst, pag. -guvu", "gūt"), //aizgūt
		FirstConj.direct("-gvelžu, -gvelz,", "-gvelž, pag. -gvelzu", "gvelzt"), //izgvelzt
		// Ģ
		FirstConj.direct("-ģērbju, -ģērb,", "-ģērbj, pag. -ģērbu", "ģērbt"), //apģist
		FirstConj.direct("-ģinstu, -ģinsti,", "-ģinst, pag. -ģindu", "ģinst"), //izģinst
		FirstConj.direct("-ģiedu, -ģied,", "-ģied, pag. -ģidu", "ģist"), //apģist
		FirstConj.direct("-ģībstu, -ģībsti,", "-ģībst, pag. -ģību", "ģībt"), //noģībt
		// H, I
		FirstConj.direct("-iežu, -iez,", "-iež, pag. -iezu", "iezt"), //atiezt
		FirstConj.direct("-īgstu, -īgsti,", "-īgst, pag. -īgu", "īgt"), //paīgt
		// J
		FirstConj.direct("-jaucu, -jauc,", "-jauc, pag. -jaucu", "jaukt"), //iejaukt
		FirstConj.direct("-jauju, -jauj,", "-jauj, pag. -jāvu", "jaut"), //iejaut
		FirstConj.direct("-jāju, -jāj,", "-jāj, pag. -jāju", "jāt"), //aizjāt
		FirstConj.direct("-jēdzu, -jēdz,", "-jēdz, pag. -jēdzu", "jēgt"), //apjēgt
		FirstConj.direct("-jožu, -joz,", "-jož, pag. -jozu", "jozt"), //aizjozt 1, 2
		FirstConj.direct("-jūku, -jūc,", "-jūk, pag. -juku", "jukt"), //apjukt
		FirstConj.direct("-jumju, -jum,", "-jumj, pag. -jumu", "jumt"), //uzjumt
		FirstConj.direct("-jūtu, -jūti,", "-jūt, pag. -jutu", "just"), //izjust
		FirstConj.direct("-jūdzu, -jūdz,", "-jūdz, pag. -jūdzu", "jūgt"), //aizjūgt
		// K
		FirstConj.direct("-kalstu, -kalsti,", "-kalst, pag. -kaltu", "kalst"), //aizkalst
		FirstConj.direct("-kaļu, -kal,", "-kaļ, pag. -kalu", "kalt"), //apkalt
		FirstConj.direct("-kampju, -kamp,", "-kampj, pag. -kampu", "kampt"), //apkampt
		FirstConj.direct("-karstu, -karsti,", "-karst, pag. -karsu", "karst"), //apkarst
		FirstConj.direct("-kašu, -kas,", "-kaš, pag. -kasu", "kast"), //iekast
		FirstConj.direct("-kaucu, -kauc,", "-kauc, pag. -kaucu", "kaukt"), //izkaukt, aizkaukt
		FirstConj.direct("-kauju, -kauj,", "-kauj, pag. -kāvu", "kaut"), //apkaut
		FirstConj.direct("-kāpju, -kāp,", "-kāpj, pag. -kāpu", "kāpt"), //aizkāpt
		FirstConj.direct("-kāršu, -kārs,", "-kārš, pag. -kārsu", "kārst"), //izkārst
		FirstConj.direct("-karu, -kar,", "-kar, pag. -kāru", "kārt"), //aizkārt
		FirstConj.direct("-kāšu, -kās,", "-kāš, pag. -kāsu", "kāst"), //iekāst
		FirstConj.direct("-klāju, -klāj,", "-klāj, pag. -klāju", "klāt"), //apklāt
		FirstConj.direct("-kliedzu, -kliedz,", "-kliedz, pag. -kliedzu", "kliegt"), //aizkliegt
		FirstConj.direct("-kliežu, -klied,", "-kliež, pag. -kliedu", "kliest"), //izkliest
		FirstConj.direct("-klimstu, -klimsti,", "-klimst, pag. -klimtu", "klimst"), //aizklimst
		FirstConj.direct("-klīstu, -klīsti,", "-klīst, pag. -klīdu", "klīst"), //aizklīst
		FirstConj.direct("-klūpu, -klūpi,", "-klūp, pag. -klupu", "klupt"), //ieklupt
		FirstConj.direct("-klustu, -klusti,", "-klust, pag. -klusu", "klust"), //apklust
		FirstConj.direct("-kļauju, -kļauj,", "-kļauj, pag. -kļāvu", "kļaut"), //apkļaut
		FirstConj.direct("-kļūstu, -kļūsti,", "-kļūst, pag. -kļuvu", "kļūt"), //aizkļūt
		FirstConj.direct("-knābju, -knāb,", "-knābj, pag. -knābu", "knābt"), //aizknābt
		FirstConj.direct("-kniebju, -knieb,", "-kniebj, pag. -kniebu", "kniebt"), //apkniebt
		FirstConj.direct("-knūpu, -knūpi,", "-knūp, pag. -knupu", "knupt"), //ieknupt
		FirstConj.direct("-kņūpu, -kņūpi,", "-kņūp, pag. -kņupu", "kņupt"), //iekņupt
		FirstConj.direct("-kopju, -kop,", "-kopj, pag. -kopu", "kopt"), //apkopt
		FirstConj.direct("-kožu, -kod,", "-kož, pag. -kodu", "kost"), //aizkost
		FirstConj.direct("-krācu, -krāc,", "-krāc, pag. -krācu", "krākt"), //nokrākt
		FirstConj.direct("-krāpju, -krāp,", "-krāpj, pag. -krāpu", "krāpt"), //aizkrāpt
		FirstConj.direct("-krāju, -krāj,", "-krāj, pag. -krāju", "krāt"), //iekrāt
		FirstConj.direct("-krauju, -krauj,", "-krauj, pag. -krāvu", "kraut"), //aizkraut
		FirstConj.direct("-kremšu, -kremt,", "-kremš, pag. -kremtu", "kremst"), //sakremst
		FirstConj.direct("-kremtu, -kremt,", "-kremt, pag. -krimtu", "krimst"), //apkrimst
		FirstConj.direct("-krītu, -krīti,", "-krīt, pag. -kritu", "krist"), //aizkrist
		FirstConj.direct("-krūpu, -krūpi,", "-krūp, pag. -krupu", "krupt"), //sakrupt
		FirstConj.direct("-kuļu, -kul,", "-kuļ, pag. -kūlu", "kult"), //apkult
		FirstConj.direct("-kumpstu, -kumpsti,", "-kumpst, pag. -kumpu", "kumpt"), //piekumpt
		FirstConj.direct("-kumstu, -kumsti,", "-kumst, pag. -kumu", "kumt"), //sakumt
		FirstConj.direct("-kuru, -kur,", "-kur, pag. -kūru", "kurt"), //aizkurt
		FirstConj.direct("-kūstu, -kūsti,", "-kūst, pag. -kusu", "kust"), //aizkust
		FirstConj.direct("-kūpu, -kūpi,", "-kūp, pag. -kūpu", "kūpt"), //apkūpt
		FirstConj.direct("-kvēpstu, -kvēpsti,", "-kvēpst, pag. -kvēpu", "kvēpt"), //apkvēpt, aizkvēpt
		// Ķ
		FirstConj.direct("-ķepu, -ķep,", "-ķep, pag. -ķepu", "ķept"), //apķept, aizķept
		FirstConj.direct("-ķeru, -ķer,", "-ķer, pag. -ķēru", "ķert"), //aizķert
		FirstConj.direct("-ķērcu, -ķērc,", "-ķērc, pag. -ķērcu", "ķērkt"), //atķērkt
		// L
		FirstConj.direct("-labstu, -labsti,", "-labst, pag. -labu", "labt"), //atlabt
		FirstConj.direct("-laižu, -laid,", "-laiž, pag. -laidu", "laist"), //aizlaist
		FirstConj.direct("-loku, -loc,", "-lok, pag. -laku", "lakt"), //ielakt
		FirstConj.direct("-laužu, -lauz,", "-lauž, pag. -lauzu", "lauzt"), //aizlauzt
		FirstConj.direct("-lemju, -lem,", "-lemj, pag. -lēmu", "lemt"), //izlemt
		FirstConj.direct("-lencu, -lenc,", "-lenc, pag. -lencu", "lenkt"), //aplenkt
		FirstConj.direct("-lepu, -lep,", "-lep, pag. -lepu", "lept"), //izlept
		FirstConj.direct("-lecu, -lec,", "-lec, pag. -lēcu", "lēkt"), //aizlēkt
		FirstConj.direct("-lēšu, -lēs,", "-lēš, pag. -lēsu", "lēst"), //aplēst
		FirstConj.direct("-liedzu, -liedz,", "-liedz, pag. -liedzu", "liegt"), //aizliegt
		FirstConj.direct("-liecu, -liec,", "-liec, pag. -liecu", "liekt"), //aizliekt
		FirstConj.direct("-leju, -lej,", "-lej, pag. -lēju", "liet"), //aizliet
		FirstConj.direct("-lieku, -liec,", "-liek, pag. -liku", "likt"), //aizlikt
		FirstConj.direct("-līpu, -līpi,", "-līp, pag. -lipu", "lipt"), //aplipt, aizlipt
		FirstConj.direct("-līgstu, -līgsti,", "-līgst, pag. -līgu", "līgt"), //atlīgt
		FirstConj.direct("-līkstu, -līksti,", "-līkst, pag. -līku", "līkt"), //nolīkt, aizlīkt
		FirstConj.direct("-līstu, -līsti,", "-līst, pag. -liju", "līt"), //aplīt, aizlīt
		FirstConj.direct("-lobju, -lob,", "-lobj, pag. -lobu", "lobt"), //aizlobt
		FirstConj.direct("-lūdzu, -lūdz,", "-lūdz, pag. -lūdzu", "lūgt"), //aizlūgt
		FirstConj.direct("-lūstu, -lūsti,", "-lūst, pag. -lūzu", "lūzt"), //aizlūzt, aizlūzt
		// Ļ
		FirstConj.direct("-ļauju, -ļauj,", "-ļauj, pag. -ļāvu", "ļaut"), //atļaut
			//ļauju, ļauj, pag. ļāvu
		FirstConj.direct("-ļimstu, -ļimsti,", "-ļimst, pag. -ļimu", "ļimt"), //noļimt
		FirstConj.direct("-ļumstu, -ļumsti,", "-ļumst, pag. -ļumu", "ļumt"), //saļumt
		// M
		FirstConj.direct("-maigstu, -maigsti,", "-maigst, pag. -maigu", "maigt"), //atmaigt
		FirstConj.direct("-maļu, -mal,", "-maļ, pag. -malu", "malt"), //apmalt
		FirstConj.direct("-maucu, -mauc,", "-mauc, pag. -maucu", "maukt"), //iemaukt
		FirstConj.direct("-mauju, -mauj,", "-mauj, pag. -māvu", "maut"), //iemaut
		FirstConj.direct("-mācu, -māc,", "-māc, pag. -mācu", "mākt"), //nomākt
		FirstConj.direct("-māju, -māj,", "-māj, pag. -māju", "māt"), //atmāt
		FirstConj.direct("-melšu, -mels,", "-melš, pag. -melsu", "melst"), //iemelst
		FirstConj.direct("-metu, -met,", "-met, pag. -metu", "mest"), //aizmest
		FirstConj.direct("-mērcu, -mērc,", "-mērc, pag. -mērcu", "mērkt"), //iemērkt
		FirstConj.direct("-mēžu, -mēz,", "-mēž, pag. -mēzu", "mēzt"), //aizmēzt
		FirstConj.direct("-miedzu, -miedz,", "-miedz, pag. -miedzu", "miegt"), //aizmiegt
		FirstConj.direct("-miegu, -miedz,", "-mieg, pag. -migu", "migt"), //aizmigt
		FirstConj.direct("-mirkstu, -mirksti,", "-mirkst, pag. -mirku", "mirkt"), //imirkt
		FirstConj.direct("-mirstu, -mirsti,", "-mirst, pag. -mirsu", "mirst"), //aizmirst
		FirstConj.direct("-mirstu, -mirsti,", "-mirst, pag. -miru", "mirt"), //nomirt
		FirstConj.direct("-mūku, -mūc,", "-mūk, pag. -muku", "mukt"), //aizmukt
		FirstConj.direct("-mulstu, -mulsti,", "-mulst, pag. -mulsu", "mulst"), //apmulst
		// N
		FirstConj.direct("-nāku, -nāc,", "-nāk, pag. -nācu", "nākt"), //apnākt
		FirstConj.direct("-nesu, -nes,", "-nes, pag. -nesu", "nest"), //aiznest
		FirstConj.direct("-nirstu, -nirsti,", "-nirst, pag. -niru", "nirt"), //aiznirt
		FirstConj.direct("-nīkstu, -nīksti,", "-nīkst, pag. -nīku", "nīkt"), //iznīkt
		FirstConj.direct("-nīstu, -nīsti,", "-nīst, pag. -nīdu", "nīst"), //ienīst
		// Ņ
		FirstConj.direct("-ņemu, -ņem,", "-ņem, pag. -ņēmu", "ņemt"), //aizņemt
		FirstConj.direct("-ņirdzu, -ņirdz,", "-ņirdz, pag. -ņirdzu", "ņirgt"), //apņirgt
		// O
		FirstConj.direct("-ožu, -od,", "-ož, pag. -odu", "ost"), //apost
		// P
		FirstConj.direct("-pampstu, -pampsti,", "-pampst, pag. -pampu", "pampt"), //aizpampt
		FirstConj.direct("-paužu, -paud,", "-pauž, pag. -paudu", "paust"), //iepaust
		FirstConj.direct("-peļu, -pel,", "-peļ, pag. -pēlu", "pelt"), //nopelt
		FirstConj.direct("-peru, -per,", "-per, pag. -pēru", "pērt"), //appērt
		FirstConj.direct("-pērku, -pērc,", "-pērk, pag. -pirku", "pirkt"), //appirkt
		FirstConj.direct("-pīkstu, -pīksti,", "-pīkst, pag. -pīku", "pīkt"), //iepīkt
		FirstConj.direct("-pinu, -pin,", "-pin, pag. -pinu", "pīt"), //aizpīt
		FirstConj.direct("-ploku, -ploc,", "-plok, pag. -plaku", "plakt"), //aizplakt
		FirstConj.direct("-plaukstu, -plauksti,", "-plaukst, pag. -plauku", "plaukt"), //atplaukt, aizplaukt
		FirstConj.direct("-plāju, -plāj,", "-plāj, pag. -plāju", "plāt"), //izplāt
		FirstConj.direct("-plēšu, -plēs,", "-plēš, pag. -plēsu", "plēst"), //aizplēst
		FirstConj.direct("-plīstu, -plīsti,", "-plīst, pag. -plīsu", "plīst"), //applīst, aizplīst
		FirstConj.direct("-pliju, -plij,", "-plij, pag. -pliju", "plīt"), // uzplīt
		FirstConj.direct("-plūku, -plūc,", "-plūk, pag. -pluku", "plukt"), //noplukt
		FirstConj.direct("-plūcu, -plūc,", "-plūc, pag. -plūcu", "plūkt"), //aizplūkt
		FirstConj.direct("-plūstu, -plūsti,", "-plūst, pag. -plūdu", "plūst"), //applūst, aizplūstFirstConjRule.direct("-pļauju, -pļauj,", "-pļauj, pag. -pļāvu", "pļaut"), //aizpļaut
		FirstConj.direct("-pļauju, -pļauj,", "-pļauj, pag. -pļāvu", "pļaut"), //aizpļaut
		FirstConj.direct("-pošu, -pos,", "-poš, pag. -posu", "post"), //sapost
		FirstConj.direct("-protu, -proti,", "-prot, pag. -pratu", "prast"), //izprast
		FirstConj.direct("-pūšu, -pūt,", "-pūš, pag. -pūtu", "pūst"), //aizpūst
		FirstConj.direct("-pūstu, -pūsti,", "-pūst, pag. -puvu", "pūt"), //aizpūt, pūt
		// R
		FirstConj.direct("-roku, -roc,", "-rok, pag. -raku", "rakt"), //aizrakt
		FirstConj.direct("-rodu, -rodi,", "-rod, pag. -radu", "rast"), //aprast
		FirstConj.direct("-raucu, -rauc,", "-rauc, pag. -raucu", "raukt"), //apraukt
		FirstConj.direct("-raušu, -raus,", "-rauš, pag. -rausu", "raust"), //aizraust
		FirstConj.direct("-rauju, -rauj,", "-rauj, pag. -rāvu", "raut"), //aizraut
		FirstConj.direct("-rāju, -rāj,", "-rāj, pag. -rāju", "rāt"), //aprāt
		FirstConj.direct("-reibstu, -reibsti,", "-reibst, pag. -reibu", "reibt"), //apreibt
		FirstConj.direct("-rēcu, -rēc,", "-rēc, pag. -rēcu", "rēkt"), //atrēkt
		FirstConj.direct("-riebju, -rieb,", "-riebj, pag. -riebu", "riebt"), //aizriebt
		FirstConj.direct("-reju, -rej,", "-rej, pag. -rēju", "riet"), //apriet
		FirstConj.direct("-riežu, -riez,", "-riež, pag. -riezu", "riezt"), //ieriezt
		FirstConj.direct("-rimstu, -rimsti,", "-rimst, pag. -rimu", "rimt"), //aprimt
		FirstConj.direct("-riju, -rij,", "-rij, pag. -riju", "rīt"), //aizrīt
		FirstConj.direct("-rūku, -rūc,", "-rūk, pag. -ruku", "rukt"), //sarukt
		FirstConj.direct("-rūgstu, -rūgsti,", "-rūgst, pag. -rūgu", "rūgt"), //sarūgt
		FirstConj.direct("-rūcu, -rūc,", "-rūc, pag. -rūcu", "rūkt"), //atrūkt
		// S
		FirstConj.direct("-salkstu, -salksti,", "-salkst, pag. -salku", "salkt"), //aizsalkt
		FirstConj.direct("-salstu, -salsti,", "-salst, pag. -salu", "salt"), //atsalt
		FirstConj.direct("-sarkstu, -sarksti,", "-sarkst, pag. -sarku", "sarkt"), //aizsarkt
		FirstConj.direct("-saucu, -sauc,", "-sauc, pag. -saucu", "saukt"), //aizsaukt
		FirstConj.direct("-sāku, -sāc,", "-sāk, pag. -sāku", "sākt"), //aizsākt
		FirstConj.direct("-sedzu, -sedz,", "-sedz, pag. -sedzu", "segt"), //aizsegt
		FirstConj.direct("-sēcu, -sēc,", "-sēc, pag. -sēcu", "sēkt"), //nosēkt
		FirstConj.direct("-sēršu, -sērs,", "-sērš, pag. -sērsu", "sērst"), //apsērst
		FirstConj.direct("-seru, -ser,", "-ser, pag. -sēru", "sērt"), //aizsērt
		FirstConj.direct("-sēstu, -sēsti,", "-sēst, pag. -sēdu", "sēst"), //aizsēst
		FirstConj.direct("-sēju, -sēj,", "-sēj, pag. -sēju", "sēt"), //apsēt
		FirstConj.direct("-sienu, -sien,", "-sien, pag. -sēju", "siet"), //aizsiet
		FirstConj.direct("-sieku, -siec,", "-siek, pag. -siku", "sikt"), //apsikt
		FirstConj.direct("-silstu, -silsti,", "-silst, pag. -silu", "silt"), //apsilt
		FirstConj.direct("-sirgstu, -sirgsti,", "-sirgst, pag. -sirgu", "sirgt"), //apsirgt
		FirstConj.direct("-situ, -sit,", "-sit, pag. -situ", "sist"), //aizsist
		FirstConj.direct("-sīkstu, -sīksti,", "-sīkst, pag. -sīku", "sīkt"), //apsīkt
		FirstConj.direct("-skaru, -skar,", "-skar, pag. -skāru", "skart"), //aizskart
		FirstConj.direct("-skaužu, -skaud,", "-skauž, pag. -skaudu", "skaust"), //apskaust
		FirstConj.direct("-skauju, -skauj,", "-skauj, pag. -skāvu", "skaut"), //apskaut
		FirstConj.direct("-skābstu, -skābsti,", "-skābst, pag. -skābu", "skābt"), //saskābt
		FirstConj.direct("-skumstu, -skumsti,", "-skumst, pag. -skumu", "skumt"), //apskumt
		FirstConj.direct("-skurbstu, -skurbsti,", "-skurbst, pag. -skurbu", "skurbt"), //apskurbt
		FirstConj.direct("-skuju, -skuj,", "-skuj, pag. -skuvu", "skūt"), //aizskūt
		FirstConj.direct("-slapstu, -slapsti,", "-slapst, pag. -slapu", "slapt"), //pārslapt
		FirstConj.direct("-slāpstu, -slāpsti,", "-slāpst, pag. -slāpu", "slāpt"), //aizslāpt
		FirstConj.direct("-slāju, -slāj,", "-slāj, pag. -slāju", "slāt"), //aizslāt
		FirstConj.direct("-slaucu, -slauc,", "-slauc, pag. -slaucu", "slaukt"), //aizslaukt
		FirstConj.direct("-slābstu, -slābsti,", "-slābst, pag. -slābu", "slābt"), //atslābt
		FirstConj.direct("-slēdzu, -slēdz,", "-slēdz, pag. -slēdzu", "slēgt"), //aizslēgt
		FirstConj.direct("-slēpju, -slēp,", "-slēpj, pag. -slēpu", "slēpt"), //aizslēpt
		FirstConj.direct("-sliecu, -sliec,", "-sliec, pag. -sliecu", "sliekt"), //nosliekt
		FirstConj.direct("-slimstu, -slimsti,", "-slimst, pag. -slimu", "slimt"), //apslimt
		FirstConj.direct("-slīgstu, -slīgsti,", "-slīgst, pag. -slīgu", "slīgt"), //aizslīgt
		FirstConj.direct("-slīkstu, -slīksti,", "-slīkst, pag. -slīku", "slīkt"), //ieslīkt
		FirstConj.direct("-sliju, -slij,", "-slij, pag. -sliju", "slīt"), //ieslīt
		FirstConj.direct("-smagstu, -smagsti,", "-smagst, pag. -smagu", "smagt"), //sasmagt
		FirstConj.direct("-smoku, -smoc,", "-smok, pag. -smaku", "smakt"), //aizsmakt
		FirstConj.direct("-smeļu, -smel,", "-smeļ, pag. -smēlu", "smelt"), //apsmelt
		FirstConj.direct("-smeju, -smej,", "-smej, pag. -smēju", "smiet"), //apsmiet
		FirstConj.direct("-snaužu, -snaud,", "-snauž, pag. -snaudu", "snaust"), //aizsnaust
		FirstConj.direct("-sniedzu, -sniedz,", "-sniedz, pag. -sniedzu", "sniegt"), //aizsniegt
		FirstConj.direct("-sniegu, -sniedz,", "-snieg, pag. -snigu", "snigt"), //apsnigt
		FirstConj.direct("-speru, -sper,", "-sper, pag. -spēru", "spert"), //aizspert
		FirstConj.direct("-spēju, -spēj,", "-spēj, pag. -spēju", "spēt"), //aizspēt
		FirstConj.direct("-spiedzu, -spiedz,", "-spiedz, pag. -spiedzu", "spiegt"), //atspiegt
		FirstConj.direct("-spiežu, -spied,", "-spiež, pag. -spiedu", "spiest"), //aizspiest
		FirstConj.direct("-spirgstu, -spirgsti,", "-spirgst, pag. -spirgu", "spirgt"), //atspirgt
		FirstConj.direct("-spļauju, -spļauj,", "-spļauj, pag. -spļāvu", "spļaut"), //aizspļaut
		FirstConj.direct("-spraucu, -sprauc,", "-sprauc, pag. -spraucu", "spraukt"), //iespraukt
		FirstConj.direct("-spraužu, -spraud,", "-sprauž, pag. -spraudu", "spraust"), //aizspraust
		FirstConj.direct("-sprāgstu, -sprāgsti,", "-sprāgst, pag. -sprāgu", "sprāgt"), //atsprāgt
		FirstConj.direct("-spriežu, -spried,", "-spriež, pag. -spriedu", "spriest"), //apspriest
		FirstConj.direct("-sprūku, -sprūc,", "-sprūk, pag. -spruku", "sprukt"), //aizsprukt
		FirstConj.direct("-sprūstu, -sprūsti,", "-sprūst, pag. -sprūdu", "sprūst"), //iesprūst
		FirstConj.direct("-spurcu, -spurc,", "-spurc, pag. -spurcu", "spurkt"), //nospurkt
		FirstConj.direct("-stāju, -stāj,", "-stāj, pag. -stāju", "stāt"), //aizstāt
		FirstConj.direct("-steidzu, -steidz,", "-steidz, pag. -steidzu", "steigt"), //aizsteigt
		FirstConj.direct("-stiepju, -stiep,", "-stiepj, pag. -stiepu", "stiept"), //aizstiept
		FirstConj.direct("-stiegu, -stiedz,", "-stieg, pag. -stigu", "stigt"), //iestigt
		FirstConj.direct("-stingstu, -stingsti,", "-stingst, pag. -stingu", "stingt"), //sastingt
		FirstConj.direct("-strebju, -streb,", "-strebj, pag. -strēbu", "strēbt"), //apstrēbt
		FirstConj.direct("-striegu, -striedz,", "-strieg, pag. -strigu", "strigt"), //iestrigt
		FirstConj.direct("-stulbstu, -stulbsti,", "-stulbst, pag. -stulbu", "stulbt"), //stulbt
		FirstConj.direct("-stumju, -stum,", "-stumj, pag. -stūmu", "stumt"), //aizstumt
		FirstConj.direct("-sūtu, -sūti,", "-sūt, pag. -sutu", "sust"), //apsust
		FirstConj.direct("-sūcu, -sūc,", "-sūc, pag. -sūcu", "sūkt"), //iesūkt
		FirstConj.direct("-sveicu, -sveic,", "-sveic, pag. -sveicu", "sveikt"), //apsveikt
		FirstConj.direct("-svelpju, -svelp,", "-svelpj, pag. -svelpu", "svelpt"), //iesvelpt
		FirstConj.direct("-sveļu, -svel,", "-sveļ, pag. -svēlu", "svelt"), //svelt, iesvelt
		FirstConj.direct("-sveru, -sver,", "-sver, pag. -svēru", "svērt"), //apsvērt
		FirstConj.direct("-sviežu, -svied,", "-sviež, pag. -sviedu", "sviest"), //aizsviest
		FirstConj.direct("-svilpju, -svilp,", "-svilpj, pag. -svilpu", "svilpt"), //atsvilpt
		FirstConj.direct("-svilstu, -svilsti,", "-svilst, pag. -svilu", "svilt"), //aizsvilt
		FirstConj.direct("-svīstu, -svīsti,", "-svīst, pag. -svīdu", "svīst"), //iesvīst
		// Š
		FirstConj.direct("-šalcu, -šalc,", "-šalc, pag. -šalcu", "šalkt"), //pašalkt, aizšalkt
		FirstConj.direct("-šauju, -šauj,", "-šauj, pag. -šāvu", "šaut"), //aizšaut
		FirstConj.direct("-šķeļu, -šķel,", "-šķeļ, pag. -šķēlu", "šķelt"), //aizšķelt
		FirstConj.direct("-šķēržu, -šķērd,", "-šķērž, pag. -šķērdu", "šķērst"), //izšķērst
		FirstConj.direct("-šķiebju, -šķieb,", "-šķiebj, pag. -šķiebu", "šķiebt"), //izšķiebt
		FirstConj.direct("-šķiežu, -šķied,", "-šķiež, pag. -šķiedu", "šķiest"), //aizšķiest
		FirstConj.direct("-šķiļu, -šķil,", "-šķiļ, pag. -šķīlu", "šķilt"), //aizšķilt
		FirstConj.direct("-šķiru, -šķir,", "-šķir, pag. -šķīru", "šķirt"), //aizšķirt
		FirstConj.direct("-šķīstu, -šķīsti,", "-šķīst, pag. -šķīdu", "šķīst"), //apšķīst
		FirstConj.direct("-šķinu, -šķin,", "-šķin, pag. -šķinu", "šķīt"), //iešķīt
		FirstConj.direct("-šļācu, -šļāc,", "-šļāc, pag. -šļācu", "šļākt"), //aizšļākt
		FirstConj.direct("-šļaucu, -šļauc,", "-šļauc, pag. -šļaucu", "šļaukt"), //nošļaukt
		FirstConj.direct("-šļaupju, -šļaup,", "-šļaupj, pag. -šļaupu", "šļaupt"), //nošļaupt
		FirstConj.direct("-šļūku, -šļūc,", "-šļūk, pag. -šļuku", "šļukt"), //aizšļukt
		FirstConj.direct("-šļūcu, -šļūc,", "-šļūc, pag. -šļūcu", "šļūkt"), //aizšļūkt
		FirstConj.direct("-šmaucu, -šmauc,", "-šmauc, pag. -šmaucu", "šmaukt"), //aizšmaukt
		FirstConj.direct("-šmūku, -šmūc,", "-šmūk, pag. -šmuku", "šmukt"), //iešmukt
		FirstConj.direct("-šņaucu, -šņauc,", "-šņauc, pag. -šņaucu", "šņaukt"), //iešņaukt
		FirstConj.direct("-šņācu, -šņāc,", "-šņāc, pag. -šņācu", "šņākt"), //atšņākt
		FirstConj.direct("-šņāpju, -šņāp,", "-šņāpj, pag. -šņāpu", "šņāpt"), //apšņāpt
		FirstConj.direct("-šņurkstu, -šņurksti,", "-šņurkst, pag. -šņurku", "šņurkt"), //sašņurkt
		FirstConj.direct("-šņūcu, -šņūc,", "-šņūc, pag. -šņūcu", "šņūkt"), //iešņūkt
		FirstConj.direct("-šuju, -šuj,", "-šuj, pag. -šuvu", "šūt"), //aizšūt
		// T
		FirstConj.direct("-topu, -topi,", "-top, pag. -tapu", "tapt"), //attapt
		FirstConj.direct("-teicu, -teic,", "-teic, pag. -teicu", "teikt"), //uzteikt
		FirstConj.direct("-tempju, -temp,", "-tempj, pag. -tempu", "tempt"), //iztempt
		FirstConj.direct("-tērpju, -tērp,", "-tērpj, pag. -tērpu", "tērpt"), //aptērpt
		FirstConj.direct("-tēšu, -tēs,", "-tēš, pag. -tēsu", "tēst"), //aptēst
		FirstConj.direct("-tiepju, -tiep,", "-tiepj, pag. -tiepu", "tiept"), //uztiept
		FirstConj.direct("-tilpstu, -tilpsti,", "-tilpst, pag. -tilpu", "tilpt"), //satilpt
		FirstConj.direct("-tinu, -tin,", "-tin, pag. -tinu", "tīt"), //aiztīt
		FirstConj.direct("-traucu, -trauc,", "-trauc, pag. -traucu", "traukt"), //aiztraukt
		FirstConj.direct("-traušu, -traus,", "-trauš, pag. -trausu", "traust"), //notraust
		FirstConj.direct("-trencu, -trenc,", "-trenc, pag. -trencu", "trenkt"), //aiztrenkt
		FirstConj.direct("-triecu, -triec,", "-triec, pag. -triecu", "triekt"), //aiztriekt
		FirstConj.direct("-triepju, -triep,", "-triepj, pag. -triepu", "triept"), //aiztriept
		FirstConj.direct("-trinu, -trin,", "-trin, pag. -trinu", "trīt"), //ietrīt
		FirstConj.direct("-trūkstu, -trūksti,", "-trūkst, pag. -trūku", "trūkt"), //iztrūkt
		FirstConj.direct("-tumstu, -tumsti,", "-tumst, pag. -tumsu", "tumst"), //pietumst
		FirstConj.direct("-tupstu, -tupsti,", "-tupst, pag. -tupu", "tupt"), //aiztupt
		FirstConj.direct("-tūkstu, -tūksti,", "-tūkst, pag. -tūku", "tūkt"), //aptūkt, aiztūkt
		FirstConj.direct("-tveru, -tver,", "-tver, pag. -tvēru", "tvert"), //aiztvert
		FirstConj.direct("-tvīkstu, -tvīksti,", "-tvīkst, pag. -tvīku", "tvīkt"), //ietvīkt
		// U
		FirstConj.direct("-urbju, -urb,", "-urbj, pag. -urbu", "urbt"), //aizurbt
		// V
		FirstConj.direct("-vācu, -vāc,", "-vāc, pag. -vācu", "vākt"), //aizvākt
		FirstConj.direct("-vārgstu, -vārgsti,", "-vārgst, pag. -vārgu", "vārgt"), //izvārgt
		FirstConj.direct("-vāžu, -vāz,", "-vāž, pag. -vāzu", "vāzt"), //aizvāzt
		FirstConj.direct("-veicu, -veic,", "-veic, pag. -veicu", "veikt"), //noveikt
		FirstConj.direct("-veļu, -vel,", "-veļ, pag. -vēlu", "velt"), //aizvelt
		FirstConj.direct("-vemju, -vem,", "-vemj, pag. -vēmu", "vemt"), //apvemt
		FirstConj.direct("-vedu, -ved,", "-ved, pag. -vedu", "vest"), //aizvest
		FirstConj.direct("-vērpju, -vērp,", "-vērpj, pag. -vērpu", "vērpt"), //aizvērpt
		FirstConj.direct("-veru, -ver,", "-ver, pag. -vēru", "vērt"), //aizvērt
		FirstConj.direct("-vēžu, -vēz,", "-vēž, pag. -vēzu", "vēzt"), //apvēzt
		FirstConj.direct("-viebju, -vieb,", "-viebj, pag. -viebu", "viebt"), //izviebt
		FirstConj.direct("-viešu, -vies,", "-vieš, pag. -viesu", "viest"), //ieviest
		FirstConj.direct("-vilgstu, -vilgsti,", "-vilgst, pag. -vilgu", "vilgt"), //atvilgt
		FirstConj.direct("-velku, -velc,", "-velk, pag. -vilku", "vilkt"), //aizvilkt
		FirstConj.direct("-viļu, -vil,", "-viļ, pag. -vīlu", "vilt"), //aizvilt
		FirstConj.direct("-vīstu, -vīsti,", "-vīst, pag. -vītu", "vīst"), //novīst
		FirstConj.direct("-vīkšu, -vīkši,", "-vīkš, pag. -vīkšu", "vīkšt"), //savīkšt
		FirstConj.direct("-viju, -vij,", "-vij, pag. -viju", "vīt"), //aizvīt
		// Z
		FirstConj.direct("-zogu, -zodz,", "-zog, pag. -zagu", "zagt"), // apzagt
		FirstConj.direct("-zeļu, -zel,", "-zeļ, pag. -zēlu", "zelt"), // zelt, aizzelt
		FirstConj.direct("-ziežu, -zied,", "-ziež, pag. -ziedu", "ziest"), // aizziest
		FirstConj.direct("-zīžu, -zīd,", "-zīž, pag. -zīdu", "zīst"), // iezīst
		FirstConj.direct("-zīstu, -zīsti,", "-zīst, pag. -zinu", "zīt"), // atzīt
		FirstConj.direct("-zūdu, -zūdi,", "-zūd, pag. -zudu", "zust"), // aizzust
		FirstConj.direct("-zveļu, -zvel,", "-zveļ, pag. -zvēlu", "zvelt"), // atzvelt
		FirstConj.direct("-zviedzu, -zviedz,", "-zviedz, pag. -zviedzu", "zviegt"), // nozviegt
		FirstConj.direct("-zvilstu, -zvilsti,", "-zvilst, pag. -zvilu", "zvilt"), // atzvilt
		// Ž
		FirstConj.direct("-žauju, -žauj,", "-žauj, pag. -žāvu", "žaut"), // apžaut
		FirstConj.direct("-žilbstu, -žilbsti,", "-žilbst, pag. -žilbu", "žilbt"), // apžilbt
		FirstConj.direct("-žirgstu, -žirgsti,", "-žirgst, pag. -žirgu", "žirgt"), // atžirgt
		FirstConj.direct("-žmaudzu, -žmaudz,", "-žmaudz, pag. -žmaudzu", "žmaugt"), // apžmaugt
		FirstConj.direct("-žmiedzu, -žmiedz,", "-žmiedz, pag. -žmiedzu", "žmiegt"), // aizžmiegt
		FirstConj.direct("-žņaudzu, -žņaudz,", "-žņaudz, pag. -žņaudzu", "žņaugt"), // aizžņaugt
		FirstConj.direct("-žūstu, -žūsti,", "-žūst, pag. -žuvu", "žūt"), // apžūt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas.
		FirstConj.direct3PersHomof("-aust, pag. -ausa", "aust", "\"aust\" (gaismai)"), //aizaust 1
		FirstConj.direct3PersHomof("-dzīst, pag. -dzija", "dzīt", "\"dzīt\" (ievainojumam)"), //aizdzīt 2
		FirstConj.direct3PersHomof("-irst, pag. -ira", "irt", "\"irt\" (audumam)"), //irt 2

		// Paralēlformas
		FirstConj.direct3PersParallel(
				"-glumst, arī -glum, pag. -gluma", "glumt"), // pieglumt
		FirstConj.direct3PersParallel(
				"kūp, arī kūpst, pag. kūpa", "kūpt"), // kūpt
		FirstConj.direct3PersParallel(
				"-ries, pag. -rieta, arī -riesa", "riest"), // aizriest
		// TODO pārbaudīt, vai ir pieņemami, ka abas homoformas ir kopā
		FirstConj.direct3PersParallel(
				"-stīdz, pag. -stīdza, retāk -stīgst, pag. -stīga", "stīgt"), // iestīgt
		FirstConj.direct3PersParallel(
				"-springst, arī -sprindz, pag. -springa, arī -sprindza", "springt"), // atspringt
		FirstConj.direct3PersParallel(
				"-spuldz, arī -spulgst, pag. -spuldza, arī -spulga", "spulgt"), // atspulgt
		FirstConj.direct3PersParallel(
				"-spurst, pag. -spura, arī -spura", "spurt"), // apspurt
		FirstConj.direct3PersParallel(
				"-strēdz, arī -strēgst, pag. -strēdza, arī -strēga", "strēgt"), // piestrēgt

		// Standartizētie.
		// A, B
		FirstConj.direct3Pers("-birst, pag. -birza", "birzt"), //izbirzt
		FirstConj.direct3Pers("-blēj, pag. -blēja", "blēt"), //atblēt
		FirstConj.direct3Pers("-bož, pag. -boza", "bozt"), //izbozt
		FirstConj.direct3Pers("-brūk, pag. -bruka", "brukt"), //aizbrukt
		// C, D
		FirstConj.direct3Pers("-dim, pag. -dima", "dimt"), //aizdimt
		FirstConj.direct3Pers("-dip, pag. -dipa", "dipt"), //aizdipt
		FirstConj.direct3Pers("-dīgst, pag. -dīga", "dīgt"), //apdīgt
		FirstConj.direct3Pers("-drūp, pag. -drupa", "drupt"), //apdrupt
		FirstConj.direct3Pers("-dub, pag. -duba", "dubt"), //iedubt
		// E, F, G
		FirstConj.direct3Pers("-grūst, pag. -gruva", "grūt"), //aizgrūt
		FirstConj.direct3Pers("-guldz, pag. -guldza", "gulgt"), //aizgulgt
		// H, I
		FirstConj.direct3Pers("-ilgst, pag. -ilga", "ilgt"), //ieilgt
		// J, K
		FirstConj.direct3Pers("-knīt, pag. -knita", "knist"), //izknist
		FirstConj.direct3Pers("-kviec, pag. -kvieca", "kviekt"), //aizkviekt
		// L
		FirstConj.direct3Pers("-lūp, pag. -lupa", "lupt"), //aplupt
		// Ļ
		FirstConj.direct3Pers("-ļūk, pag. -ļuka", "ļukt"), //atļukt
		// M
		FirstConj.direct3Pers("-milst, pag. -milza", "milzt"), //aizmilzt
		// N, Ņ
		FirstConj.direct3Pers("-ņirb, pag. -ņirba", "ņirbt"), //aizņirbt
		// O, P, R
		FirstConj.direct3Pers("-ris, pag. -risa", "rist"), //aprist
		// S
		FirstConj.direct3Pers("-sīc, pag. -sīca", "sīkt"), //aizsīkt
		FirstConj.direct3Pers("-smeldz, pag. -smeldza", "smelgt"), //aizsmelgt
		// Š
		FirstConj.direct3Pers("-šļirc, pag. -šļirca", "šļirkt"), //iešļirkt
		// T
		FirstConj.direct3Pers("-tirpst, pag. -tirpa", "tirpt"), //attirpt
		FirstConj.direct3Pers("-trup, pag. -trupa", "trupt"), //aptrupt
		FirstConj.direct3Pers("-trus, pag. -trusa", "trust"), //aptrust
		// U
		FirstConj.direct3Pers("-urdz, pag. -urdza", "urgt"), //aizurgt
		// V, Z
		FirstConj.direct3Pers("-zilst, pag. -zila", "zilt"), //apzilt

		// Daudzskaitļa formu likumi.
		FirstConj.directPlural("-bēgam, -bēgat, -bēg, pag. -bēgām", "bēgt"), // sabēgt
		FirstConj.directPlural("-lecam, -lecat, -lec, pag. -lēcām", "lēkt"), // salēkt
		FirstConj.directPlural("-lienam, -lienat, -lien, pag. -līdām", "līst"), // salīst
		FirstConj.directPlural("-mūkam, -mūkat, -mūk, pag. -mukām", "mukt"), // samukt
		FirstConj.directPlural("-sprūkam, -sprūkat, -sprūk, pag. -sprukām", "sprukt"), // sasprukt
		FirstConj.directPlural("-sprūstam, -sprūstat, -sprūst, pag. -sprūdām", "sprūst"), // sasprūst
		FirstConj.directPlural("-spurdzam, -spurdzat, -spurdz, pag. -spurdzām", "spurgt"), // saspurgt
		FirstConj.directPlural("-tupstam, -tupstat, -tupst, pag. -tupām", "tupt"), // satupt

		// Pilnīgs nestandarts.
		VerbDoubleRule.of("-teicu, -teic,", "-teic (tagadnes formas parasti nelieto), pag. -teicu", "teikt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"teikt\"")},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NOT_PRESENT_FORMS)},
				new String[]{"teik"}, new String[]{"teic"}, new String[]{"teic"}), //atteikt

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		SecondConj.directAllPersParallel(
				"-skandēju, -skandē, -skandē, pag. -skandēju, -skandēji, -skandēja (retāk -skanda, 1. konj.)",
				"skandēt"), // noskandēt

		// Standartīgie.
		SecondConj.direct("-dabūju, -dabū,", "-dabū, pag. -dabūju", "dabūt"), //aizdabūt
		SecondConj.direct("-jaucēju, -jaucē,", "-jaucē, pag. -jaucēju", "jaucēt"), //piejaucēt
		SecondConj.direct("-klusēju, -klusē,", "-klusē, pag. -klusēju", "klusēt"), //noklusēt
		SecondConj.direct("-ķēzīju, -ķēzī,", "-ķēzī, pag. -ķēzīju", "ķēzīt"), //uzķēzīt
		SecondConj.direct("-lāsoju, -lāso,", "-lāso, pag. -lāsoju", "lāsot"), //nolāsot
		SecondConj.direct("-ņurīju, -ņurī,", "-ņurī, pag. -ņurīju", "ņurīt"), //noņurīt
		SecondConj.direct("-svilpoju, -svilpo,", "-svilpo, pag. -svilpoju", "svilpot"), //pasvilpot
		SecondConj.direct("-verkšīju, -verkšī,", "-verkšī, pag. -verkšīju", "verkšīt"), //saverkšīt
		SecondConj.direct("-verkšķīju, -verkšķī,", "-verkšķī, pag. -verkšķīju", "verkšķīt"), //saverkšķīt
		SecondConj.direct("-žūpoju, -žūpo,", "-žūpo, pag. -žūpoju", "žūpot"), //nožūpot

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		// Īpašā piezīme par glumēšanu: 2. konjugāciju nosaka 3. personas
		// galotne "-ē" - 3. konjugācijai būtu bez.
		SecondConj.direct3PersParallel(
				"-dūmē, pag. -dūmēja (retāk -dūma, 1. konj.)", "dūmēt"), // apdūmēt
		SecondConj.direct3PersParallel(
				"-glumē, pag. -glumēja (retāk -gluma, 1. konj.)", "glumēt"), //izglumēt

		// Standartizētie.
		SecondConj.direct3Pers("-kurtē, pag. -kurtēja", "kurtēt"), //apkurtēt
		SecondConj.direct3Pers("-kūko, pag. -kūkoja", "kūkot"), //aizkūkot
		SecondConj.direct3Pers("-mirgo, pag. -mirgoja", "mirgot"), //aizmirgot
		SecondConj.direct3Pers("-sērē, pag. -sērēja", "sērēt"), //aizsērēt
		SecondConj.direct3Pers("-šalko, pag. -šalkoja", "šalkot"), // iešalkot
		SecondConj.direct3Pers("-zilē, pag. -zilēja", "zilēt"), // iezilēt

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		ThirdConj.directAllPersParallel(
				"-ļogu, -ļogi, -ļoga, retāk -ļodzu, -ļodzi, -ļodza, pag. -ļodzīju", "ļodzīt"), //paļodzīt
		ThirdConj.directAllPersParallel(
				"-moku, -moki, -moka, arī -mocu, -moci, -moca, pag. -mocīju", "mocīt"), //aizmocīt
		ThirdConj.directAllPersParallel(
				"-murcu, -murci, -murca, retāk -murku, -murki, -murka, pag. -murcīju", "murcīt"), //apmurcīt
		ThirdConj.directAllPersParallel(
				"-ņurcu, -ņurci, -ņurca, retāk -ņurku, -ņurki, -ņurka, pag. -ņurcīju", "ņurcīt"), //apmurcīt
		ThirdConj.directAllPersParallel(
				"-sēžu, -sēdi, -sēž, arī -sēdu, -sēdi, -sēd, pag. -sēdēju", "sēdēt"), //iesēdēt

		ThirdConj.directAllPersParallel(
				"-dzirdu, -dzirdi, -dzird, pag. -dzirdu (1. konj.), arī -dzirdēju",
				"dzirdēt", false), //izdzirdēt
		ThirdConj.directAllPersParallel(
				"-dzirdu, -dzirdi, -dzird, pag. -dzirdēju, arī -dzirdu (1. konj.)",
				"dzirdēt", false), //padzirdēt
		ThirdConj.directAllPersParallel(
				"-slīdu, -slīdi, -slīd, pag. -slīdēju, -slīdēji, -slīdēja (retāk -slīda, 1. konj.)",
				"slīdēt", false), // aizslīdēt
		ThirdConj.directAllPersParallel(
				"-smirdu, -smirdi, -smird, pag. -smirdēju (3. pers. arī -smirda, 1. konj.)",
				"smirdēt", false), // nosmirdēt
		ThirdConj.directAllPersParallel(
				"-spīdu, -spīdi, -spīd, pag. -spīdēju, -spīdēji, -spīdēja (retāk -spīda, 1. konj.)",
				"spīdēt", false), // paspīdēt
		ThirdConj.directAllPersParallel(
				"-vīdu, -vīdi, -vīd, pag. -vīdēju (3. pers. retāk -vīda, 1. konj.)",
				"vīdēt", false), //novīdēt
		ThirdConj.directAllPersParallel(
				"-vīdu, -vīdi, -vīd, pag. -vīdēju (retāk -vīdu, 1. konj.)",
				"vīdēt", false), // pavīdēt
		ThirdConj.directAllPersParallel(
				"-guļu, -guli, -guļ (arī -gul), pag. -gulēju", "gulēt"), // iegulēt
			//TODO kā norādīt miju + ko darīt ar otru, standartizēto gulēt?

		// Standartizētie.
		// A, B
		ThirdConj.direct("-brauku, -brauki,", "-brauka, pag. -braucīju", "braucīt", true), //apbraucīt
		ThirdConj.direct("-burkšu, -burkši,", "-burkš, pag. -burkšēju", "burkšēt", false), //izburkšēt
		ThirdConj.direct("-burkšķu, -burkšķi,", "-burkšķ, pag. -burkšķēju", "burkšķēt", false), //izburkšķēt
		// C
		ThirdConj.direct("-ceru, -ceri,", "-cer, pag. -cerēju", "cerēt", false), //iecerēt
		// Č
		ThirdConj.direct("-čākstu, -čāksti,", "-čākst, pag. -čākstēju", "čākstēt", false), //sačākstēt
		ThirdConj.direct("-čerkstu, -čerksti,", "-čerkst, pag. -čerkstēju", "čerkstēt", false), //nočerkstēt
		ThirdConj.direct("-čērkstu, -čērksti,", "-čērkst, pag. -čērkstēju", "čērkstēt", false), //nočērkstēt
		ThirdConj.direct("-čiepstu, -čiepsti,", "-čiepst, pag. -čiepstēju", "čiepstēt", false), //iečiepstēt
		ThirdConj.direct("-činkstu, -činksti,", "-činkst, pag. -činkstēju", "činkstēt", false), //izčinkstēt
		ThirdConj.direct("-čīkstu, -čīksti,", "-čīkst, pag. -čīkstēju", "čīkstēt", false), //izčīkstēt
		ThirdConj.direct("-čuču, -čuči,", "-čuč, pag. -čučēju", "čučēt", false), //pačučēt
		ThirdConj.direct("-čukstu, -čuksti,", "-čukst, pag. -čukstēju", "čukstēt", false), //atčukstēt
		ThirdConj.direct("-čurnu, -čurni,", "-čurn, pag. -čurnēju", "čurnēt", false), //nočurnēt
		// D
		ThirdConj.direct("-deru, -deri,", "-der, pag. -derēju", "derēt", false), //noderēt 1, 2
		ThirdConj.direct("-dēdu, -dēdi,", "-dēd, pag. -dēdēju", "dēdēt", false), //izdēdēt
		ThirdConj.direct("-dienu, -dieni,", "-dien, pag. -dienēju", "dienēt", false), //atdienēt
		ThirdConj.direct("-dirnu, -dirni,", "-dirn, pag. -dirnēju", "dirnēt", false), //nodirnēt
		ThirdConj.direct("-draudu, -draudi,", "-draud, pag. -draudēju", "draudēt", false), //apdraudēt
		ThirdConj.direct("-drebu, -drebi,", "-dreb, pag. -drebēju", "drebēt", false), //nodrebēt
		ThirdConj.direct("-drīkstu, -drīksti,", "-drīkst, pag. -drīkstēju", "drīkstēt", false), //uzdrīkstēt
		ThirdConj.direct("-dusu, -dusi,", "-dus, pag. -dusēju", "dusēt", false), //atdusēt
		ThirdConj.direct("-dziedu, -dziedi,", "-dzied, pag. -dziedāju", "dziedāt", false), //aizdziedāt
		ThirdConj.direct("-dzirdu, -dzirdi,", "-dzird, pag. -dzirdēju", "dzirdēt", false), //sadzirdēt
		// E, F, G
		ThirdConj.direct("-glūnu, -glūni,", "-glūn, pag. -glūnēju", "glūnēt", false), //apglūnēt
		ThirdConj.direct("-grabu, -grabi,", "-grab, pag. -grabēju", "grabēt", false), //aizgrabēt
		ThirdConj.direct("-gribu, -gribi,", "-grib, pag. -gribēju", "gribēt", false), //iegribēt
		ThirdConj.direct("-guļu, -guli,", "-guļ, pag. -gulēju", "gulēt", true), //aizgulēt
		// H, I, Ī
		ThirdConj.direct("-īdu, -īdi,", "-īd, pag. -īdēju", "īdēt", false), //noīdēt
		// J, K
		ThirdConj.direct("-klabu, -klabi,", "-klab, pag. -klabēju", "klabēt", false), //paklabēt, aizklabēt
		ThirdConj.direct("-klimstu, -klimsti,", "-klimst, pag. -klimstēju", "klimstēt", false), //aizklimstēt
		ThirdConj.direct("-krekstu, -kreksti,", "-krekst, pag. -krekstēju", "krekstēt", false), //nokrekstēt
		ThirdConj.direct("-krekšu, -krekši,", "-krekš, pag. -krekšēju", "krekšēt", false), //nokrekšēt
		ThirdConj.direct("-krekšķu, -krekšķi,", "-krekšķ, pag. -krekšķēju", "krekšķēt", false), //nokrekšķēt
		ThirdConj.direct("-kunkstu, -kunksti,", "-kunkst, pag. -kunkstēju", "kunkstēt", false), //izkunkstēt
		ThirdConj.direct("-kurnu, -kurni,", "-kurn, pag. -kurnēju", "kurnēt", false), //pakurnēt
		ThirdConj.direct("-kustu, -kusti,", "-kust, pag. -kustēju", "kustēt", false), //aizkustēt
		ThirdConj.direct("-kūpu, -kūpi,", "-kūp, pag. -kūpēju", "kūpēt", false), //apkūpēt, aizkūpēt
		ThirdConj.direct("-kvernu, -kverni,", "-kvern, pag. -kvernēju", "kvernēt", false), //nokvernēt
		ThirdConj.direct("-kvēlu, -kvēli,", "-kvēl, pag. -kvēlēju", "kvēlēt", false), //izkvēlēt
		// L
		ThirdConj.direct("-lādu, -lādi,", "-lād, pag. -lādēju", "lādēt", false), //nolādēt
		ThirdConj.direct("-līdzu, -līdzi,", "-līdz, pag. -līdzēju", "līdzēt", false), //izlīdzēt
		ThirdConj.direct("-palīdzu, -palīdzi,", "-palīdz, pag. -palīdzēju", "palīdzēt", false), //izpalīdzēt
		ThirdConj.direct("-loku, -loki,", "-loka, pag. -locīju", "locīt", true), //aizlocīt
		ThirdConj.direct("-lūru, -lūri,", "-lūr, pag. -lūrēju", "lūrēt", false), //aplūrēt
		// Ļ
		ThirdConj.direct("-ļogu, -ļogi,", "-ļoga, pag. -ļodzīju", "ļodzīt", true), //izļodzīt
		// M
		ThirdConj.direct("-minu, -mini,", "-min, pag. -minēju", "minēt", false), //atminēt
		ThirdConj.direct("-mirdzu, -mirdzi,", "-mirdz, pag. -mirdzēju", "mirdzēt", false), //uzmirdzēt
		ThirdConj.direct("-mīlu, -mīli,", "-mīl, pag. -mīlēju", "mīlēt", false), //iemīlēt
		ThirdConj.direct("-mīstu, -mīsti,", "-mīsta, pag. -mīstīju", "mīstīt", false), //izmīstīt
		ThirdConj.direct("-muldu, -muldi,", "-muld, pag. -muldēju", "muldēt", false), //atmuldēt
		ThirdConj.direct("-murdu, -murdi,", "-murd, pag. -murdēju", "murdēt", false), //nomurdēt
		ThirdConj.direct("-murdzu, -murdzi,", "-murdza, pag. -murdzīju", "murdzīt", false), //apmurdzīt
		ThirdConj.direct("-murkšu, -murkši,", "-murkš, pag. -murkšēju", "murkšēt", false), //nomurkšēt
		ThirdConj.direct("-murkšķu, -murkšķi,", "-murkšķ, pag. -murkšķēju", "murkšķēt", false), //nomurkšķēt
		// N, Ņ
		ThirdConj.direct("-ņaudu, -ņaudi,", "-ņaud, pag. -ņaudēju", "ņaudēt", false), //izņaudēt
		ThirdConj.direct("-ņerkstu, -ņerksti,", "-ņerkst, pag. -ņerkstēju", "ņerkstēt", false), //noņerkstēt
		ThirdConj.direct("-ņurdu, -ņurdi,", "-ņurd, pag. -ņurdēju", "ņurdēt", false), //atņurdēt
		ThirdConj.direct("-ņurkstu, -ņurksti,", "-ņurkst, pag. -ņurkstēju", "ņurkstēt", false), //noņurkstēt
		ThirdConj.direct("-ņurkšu, -ņurkši,", "-ņurkš, pag. -ņurkšēju", "ņurkšēt", false), //noņurkšēt
		ThirdConj.direct("-ņurkšķu, -ņurkšķi,", "-ņurkšķ, pag. -ņurkšķēju", "ņurkšķēt", false), //noņurkšķēt
		// O, P
		ThirdConj.direct("-parkšu, -parkši,", "-parkš, pag. -parkšēju", "parkšēt", false), //noparkšēt
		ThirdConj.direct("-parkšķu, -parkšķi,", "-parkšķ, pag. -parkšķēju", "parkšķēt", false), //noparkšķēt
		ThirdConj.direct("-peldu, -peldi,", "-peld, pag. -peldēju", "peldēt", false), //aizpeldēt
		ThirdConj.direct("-pilu, -pili,", "-pil, pag. -pilēju", "pilēt", false), //appilēt, aizpilēt
		ThirdConj.direct("-pinkšu, -pinkši,", "-pinkš, pag. -pinkšēju", "pinkšēt", false), //nopinkšēt
		ThirdConj.direct("-pinkšķu, -pinkšķi,", "-pinkšķ, pag. -pinkšķēju", "pinkšķēt", false), //nopinkšķēt
		ThirdConj.direct("-pīkstu, -pīksti,", "-pīkst, pag. -pīkstēju", "pīkstēt", false), //atpīkstēt
		ThirdConj.direct("-precu, -preci,", "-prec, pag. -precēju", "precēt", false), //aizprecēt
		ThirdConj.direct("-pukstu, -puksti,", "-pukst, pag. -pukstēju", "pukstēt", false), //nopukstēt
		ThirdConj.direct("-purkšu, -purkši,", "-purkš, pag. -purkšēju", "purkšēt", false), //nopurkšēt
		ThirdConj.direct("-purkšķu, -purkšķi,", "-purkšķ, pag. -purkšķēju", "purkšķēt", false), //nopurkšķēt
		ThirdConj.direct("-putu, -puti,", "-put, pag. -putēju", "putēt", false), //aizputēt, apputēt
		// R
		ThirdConj.direct("-raudu, -raudi,", "-raud, pag. -raudāju", "raudāt", false), //apraudāt
		ThirdConj.direct("-raugu, -raugi,", "-rauga, pag. -raudzīju", "raudzīt", true), //apraudāt
		ThirdConj.direct("-redzu, -redzi,", "-redz, pag. -redzēju", "redzēt", false), //apredzēt
		// S
		ThirdConj.direct("-saku, -saki,", "-saka, pag. -sacīju", "sacīt", true), //atsacīt
		ThirdConj.direct("-sāpu, -sāpi,", "-sāp, pag. -sāpēju", "sāpēt", false), //izsāpēt
		ThirdConj.direct("-sēžu, -sēdi,", "-sēž, pag. -sēdēju", "sēdēt", true), //atsēdēt
		ThirdConj.direct("-sīkstu, -sīksti,", "-sīkst, pag. -sīkstēju", "sīkstēt", false), //apsīkstēt
		ThirdConj.direct("-skanu, -skani,", "-skan, pag. -skanēju", "skanēt", false), //pieskanēt
		ThirdConj.direct("-slaku, -slaki,", "-slaka, pag. -slacīju", "slacīt", true), //aizslacīt
		ThirdConj.direct("-slauku, -slauki,", "-slauka, pag. -slaucīju", "slaucīt", true), //aizslaucīt
		ThirdConj.direct("-slogu, -slogi,", "-sloga, pag. -slodzīju", "slodzīt", true), //ieslodzīt
		ThirdConj.direct("-smirdu, -smirdi,", "-smird, pag. -smirdēju", "smirdēt", false), //pasmirdēt
		ThirdConj.direct("-smīnu, -smīni,", "-smīn, pag. -smīnēju", "smīnēt", false), //atsmīnēt
		ThirdConj.direct("-spridzu, -spridzi,", "-spridz, pag. -spridzēju", "spridzēt", false), //spridzēt
		ThirdConj.direct("-sprikstu, -spriksti,", "-sprikst, pag. -sprikstēju", "sprikstēt", false), //sprikstēt, iesprikstēt
		ThirdConj.direct("-spurkšu, -spurkši,", "-spurkš, pag. -spurkšēju", "spurkšēt", false), //spurkšēt, atspurkšēt
		ThirdConj.direct("-spurkšķu, -spurkšķi,", "-spurkšķ, pag. -spurkšķēju", "spurkšķēt", false), //spurkšķēt, atspurkšķēt
		ThirdConj.direct("-stāvu, -stāvi,", "-stāv, pag. -stāvēju", "stāvēt", false), //aizstāvēt
		ThirdConj.direct("-stenu, -steni,", "-sten, pag. -stenēju", "stenēt", false), //izstenēt
		ThirdConj.direct("-stīdzu, -stīdzi,", "-stīdz, pag. -stīdzēju", "stīdzēt", false), //izstīdzēt
		ThirdConj.direct("-strīdu, -strīdi,", "-strīd, pag. -strīdēju", "strīdēt", false), //apstrīdēt
		ThirdConj.direct("-sūdzu, -sūdzi,", "-sūdz, pag. -sūdzēju", "sūdzēt", false), //apsūdzēt
		ThirdConj.direct("-svepstu, -svepsti,", "-svepst, pag. -svepstēju", "svepstēt", false), //izsvepstēt
		ThirdConj.direct("-svinu, -svini,", "-svin, pag. -svinēju", "svinēt", false), //izsvinēt
		// Š
		ThirdConj.direct("-šļauku, -šļauki,", "-šļauka, pag. -šļaucīju", "šļaucīt", true), //atšļupstēt
		ThirdConj.direct("-šļupstu, -šļupsti,", "-šļupst, pag. -šļupstēju", "šļupstēt", false), //atšļupstēt
		ThirdConj.direct("-šņukstu, -šņuksti,", "-šņukst, pag. -šņukstēju", "šņukstēt", false), //izšņukstēt
		ThirdConj.direct("-švirkstu, -švirksti,", "-švirkst, pag. -švirkstēju", "švirkstēt", false), //uzšvirkstēt
		// T
		ThirdConj.direct("-tarkšķu, -tarkšķi,", "-tarkšķ, pag. -tarkšķēju", "tarkšķēt", false), //iztarkšķēt
		ThirdConj.direct("-teku, -teci,", "-tek, pag. -tecēju", "tecēt", true), //aiztecēt
		ThirdConj.direct("-ticu, -tici,", "-tic, pag. -ticēju", "ticēt", false), //noticēt
		ThirdConj.direct("-trīcu, -trīci,", "-trīc, pag. -trīcēju", "trīcēt", false), //ietrīcēt
		ThirdConj.direct("-trīsu, -trīsi,", "-trīs, pag. -trīsēju", "trīsēt", false), //ietrīsēt
		ThirdConj.direct("-tupu, -tupi,", "-tup, pag. -tupēju", "tupēt", false), //tupēt
		ThirdConj.direct("-turu, -turi,", "-tur, pag. -turēju", "turēt", false), //aizturēt
		ThirdConj.direct("-tūcu, -tūci,", "-tūca, pag. -tūcīju", "tūcīt", false), //pietūcīt
		// U
		ThirdConj.direct("-urdu, -urdi,", "-urda, pag. -urdīju", "urdīt", false), //saurdīt
		ThirdConj.direct("-urkšu, -urkši,", "-urkš, pag. -urkšēju", "urkšēt", false), //urkšēt, aturkšēt
		ThirdConj.direct("-urkšķu, -urkšķi,", "-urkšķ, pag. -urkšķēju", "urkšķēt", false), //urkšķēt, aturkšķēt
		// V
		ThirdConj.direct("-vaidu, -vaidi,", "-vaid, pag. -vaidēju", "vaidēt", false), //izvaidēt
		ThirdConj.direct("-varu, -vari,", "-var, pag. -varēju", "varēt", false), //pārvarēt
		ThirdConj.direct("-verkšu, -verkši,", "-verkš, pag. -verkšēju", "verkšēt", false), //saverkšēt
		ThirdConj.direct("-verkšķu, -verkšķi,", "-verkšķ, pag. -verkšķēju", "verkšķēt", false), //saverkšķēt
		ThirdConj.direct("-vēlu, -vēli,", "-vēl, pag. -vēlēju", "vēlēt", false), //atvēlēt
		ThirdConj.direct("-vīkšu, -vīkši,", "-vīkš, pag. -vīkšīju", "vīkšīt", false), //savīkšīt
		ThirdConj.direct("-vīkšķu, -vīkšķi,", "-vīkšķ, pag. -vīkšķīju", "vīkšķīt", false), //savīkšķīt
		// Z
		ThirdConj.direct("-zibu, -zibi,", "-zib, pag. -zibēju", "zibēt", false), //nozibēt
		ThirdConj.direct("-ziedu, -ziedi,", "-zied, pag. -ziedēju", "ziedēt", false), //noziedēt
		ThirdConj.direct("-zinu, -zini,", "-zina, pag. -zināju", "zināt", false), //apzināt
		ThirdConj.direct("-zvēru, -zvēri,", "-zvēr, pag. -zvērēju", "zvērēt", false), //apzvērēt
		ThirdConj.direct("-zvilnu, -zvilni,", "-zviln, pag. -zvilnēju", "zvilnēt", false), //nozvilnēt
		ThirdConj.direct("-žņaugu, -žņaugi,", "-žņauga, pag. -žņaudzīju", "žņaudzīt", true), //izžņaudzīt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		ThirdConj.direct3PersParallel(
				"-grand, pag. -grandēja (retāk -granda, 1. konj.)", "grandēt", false), //aizgrandēt
		ThirdConj.direct3PersParallel(
				"-gruzd, pag. -gruzdēja (retāk -gruzda, 1. konj.)", "gruzdēt", false), //aizgruzdēt
		ThirdConj.direct3PersParallel(
				"-gruzd, pag. -gruzdēja (arī -gruzda, 1. konj.)", "gruzdēt", false), //apgruzdēt
		ThirdConj.direct3PersParallel(
				"-mirdz, pag. -mirdzēja (retāk -mirdza, 1. konj.)", "mirdzēt", false), //aizmirdzēt
		ThirdConj.direct3PersParallel(
				"-rit, pag. -ritēja (retāk -rita, 1. konj.)", "ritēt", false), // aizritēt
		ThirdConj.direct3PersParallel(
				"-spindz, pag. -spindzēja (retāk -spindza, 1. konj.)", "spindzēt", false), // aizspindzēt
		ThirdConj.direct3PersParallel(
				"-spīd, pag. -spīdēja (retāk -spīda, 1. konj.)", "spīdēt", false), // aizspīdēt
		ThirdConj.direct3PersParallel(
				"-spridz, pag. -spridzēja (retāk -spridza, 1. konj.)", "spridzēt", false), // iespriedzēt
		ThirdConj.direct3PersParallel(
				"-šķind, pag. -šķindēja (retāk -šķinda, 1. konj.)", "šķindēt", false), // aizšķindēt
		ThirdConj.direct3PersParallel(
				"-viz, pag. -vizēja (retāk -viza, 1. konj.)", "vizēt", false), // aizvizēt
		ThirdConj.direct3PersParallel(
				"-vīd, pag. -vīdēja (retāk -vīda, 1. konj.)", "vīdēt", false), // atvīdēt

		// Standartizētie.
		// A, B
		ThirdConj.direct3Pers("-blākš, pag. -blākšēja", "blākšēt", false), //aizblākšēt
		ThirdConj.direct3Pers("-blākšķ, pag. -blākšķēja", "blākšķēt", false), //aizblākšķēt
		ThirdConj.direct3Pers("-burbē, pag. -burbēja", "burbēt", false), //apburbēt
		ThirdConj.direct3Pers("-būb, pag. -būbēja", "būbēt", false), //apbūbēt
		// C, Č
		ThirdConj.direct3Pers("-čab, pag. -čabēja", "čabēt", false), //aizčabēt
		ThirdConj.direct3Pers("-čaukst, pag. -čaukstēja", "čaukstēt", false), //aizčaukstēt
		ThirdConj.direct3Pers("-čib, pag. -čibēja", "čibēt", false), //izčibēt
		// D
		ThirdConj.direct3Pers("-dārd, pag. -dārdēja", "dārdēt", false), //aizdārdēt
		ThirdConj.direct3Pers("-dimd, pag. -dimdēja", "dimdēt", false), //aizdimdēt
		ThirdConj.direct3Pers("-dip, pag. -dipēja", "dipēt", false), //aizdipēt
		ThirdConj.direct3Pers("-dun, pag. -dunēja", "dunēt", false), //aizdunēt
		ThirdConj.direct3Pers("-dzirkst, pag. -dzirkstēja", "dzirkstēt", false), //iedzirkstēt
		ThirdConj.direct3Pers("-džinkst, pag. -džinkstēja", "džinkstēt", false), //aizdžinkstēt
		// E, F, G
		ThirdConj.direct3Pers("-gurkst, pag. -gurkstēja", "gurkstēt", false), //aizgurkstēt
		// H, I, J, K
		ThirdConj.direct3Pers("-klakst, pag. -klakstēja", "klakstēt", false), //aizklakstēt
		ThirdConj.direct3Pers("-klaudz, pag. -klaudzēja", "klaudzēt", false), //aizklaudzēt
		ThirdConj.direct3Pers("-knaukš, pag. -knaukšēja", "knaukšēt", false), //noknaukšēt
		ThirdConj.direct3Pers("-knaukšķ, pag. -knaukšķēja", "knaukšķēt", false), //noknaukšķēt
		// L, M, N, Ņ
		ThirdConj.direct3Pers("-ņirb, pag. -ņirbēja", "ņirbēt", false), //aizņirbēt
		// O, P
		ThirdConj.direct3Pers("-pukšķ, pag. -pukšķēja", "pukšķēt", false), //aizpukšķēt
		// R
		ThirdConj.direct3Pers("-rec, pag. -recēja", "recēt", false), //aprecēt
		ThirdConj.direct3Pers("-riet, pag. -rietēja", "rietēt", false), //aizrietēt
		ThirdConj.direct3Pers("-rīb, pag. -rībēja", "rībēt", false), //aizrībēt
		ThirdConj.direct3Pers("-rukš, pag. -rukšēja", "rukšēt", false), //atrukšēt
		ThirdConj.direct3Pers("-rukšķ, pag. -rukšķēja", "rukšķēt", false), //atrukšķēt
		// S
		ThirdConj.direct3Pers("-san, pag. -sanēja", "sanēt", false), //aizsanēt
		ThirdConj.direct3Pers("-skapst, pag. -skapstēja", "skapstēt", false), //apskapstēt
		ThirdConj.direct3Pers("-sprakst, pag. -sprakstēja", "sprakstēt", false), //aizsprakstēt
		ThirdConj.direct3Pers("-sprakš, pag. -sprakšēja", "sprakšēt", false), //aizsprakšēt
		ThirdConj.direct3Pers("-sprakšķ, pag. -sprakšķēja", "sprakšķēt", false), //aizsprakšķēt
		ThirdConj.direct3Pers("-sus, pag. -susēja", "susēt", false), //apsusēt
		// Š
		ThirdConj.direct3Pers("-šļakst, pag. -šļakstēja", "šļakstēt", false), //aizšļakstēt
		ThirdConj.direct3Pers("-švīkst, pag. -švīkstēja", "švīkstēt", false), //aizšvīkstēt
		// T
		ThirdConj.direct3Pers("-tarkš, pag. -tarkšēja", "tarkšēt", false), //aiztarkšēt
		ThirdConj.direct3Pers("-tikš, pag. -tikšēja", "tikšēt", false), //aiztikšēt
		ThirdConj.direct3Pers("-tikšķ, pag. -tikšķēja", "tikšķēt", false), //aiztikšķēt
		ThirdConj.direct3Pers("-tinkš, pag. -tinkšēja", "tinkšēt", false), //aiztinkšēt
		ThirdConj.direct3Pers("-tinkšķ, pag. -tinkšķēja", "tinkšķēt", false), //aiztinkšķēt
		ThirdConj.direct3Pers("-trun, pag. -trunēja", "trunēt", false), //aiztrunēt
		ThirdConj.direct3Pers("-trup, pag. -trupēja", "trupēt", false), //aiztrupēt
		ThirdConj.direct3Pers("-trus, pag. -trusēja", "trusēt", false), //ietrusēt
		ThirdConj.direct3Pers("-trūd, pag. -trūdēja", "trūdēt", false), //aptrūdēt
		// U
		ThirdConj.direct3Pers("-urdz, pag. -urdzēja", "urdzēt", false), //aizurdzēt
		// V, Z
		ThirdConj.direct3Pers("-zuz, pag. -zuzēja", "zuzēt", false), //aizzuzēt
		// Ž
		ThirdConj.direct3Pers("-žvadz, pag. -žvadzēja", "žvadzēt", false), //aizžvadzēt
		ThirdConj.direct3Pers("-žvinkst, pag. -žvinkstēja", "žvinkstēt", false), //aizžvinkstēt

		// Likumi daudzskaitļa formām.
		ThirdConj.directPlural("-tekam, -tekat, -tek, pag. -tecējām", "tecēt", true), //satecēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] directMultiConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Šobrīd sarezgitās struktūras dēļ 3. personas likums netiek atvasinats.
			// TODO uztaisīt verbu likumu, kas atvasina 3. personas likumu.
		SecondThirdConj.directAllPersParallel(
				"-bedīju, -bedī, -bedī, arī -bedu, -bedi, -beda, pag. -bedīju",
				"bedīt", false), // apbedīt
		SecondThirdConj.directAllPersParallel(
				"-brūnu, -brūni, -brūn, arī -brūnēju, -brūnē, -brūnē, pag. -brūnēju",
				"brūnēt", false), // nobrūnēt
		SecondThirdConj.directAllPersParallel(
				"-ceru, -ceri, -cer, retāk -cerēju, -cerē, -cerē, pag. -cerēju",
				"cerēt", false), // apcerēt
		SecondThirdConj.directAllPersParallel(
				"-cienu, -cieni, -ciena, arī -cienīju, -cienī, -cienī, pag. -cienīju",
				"cienīt", false), // iecienīt
		SecondThirdConj.directAllPersParallel(
				"-dēstu, -dēsti, -dēsta, retāk -dēstīju, -dēstī, -dēstī, pag. -dēstīju",
				"dēstīt", false), // apdēstīt
		SecondThirdConj.directAllPersParallel(
				"-kristīju, -kristī, -kristī, arī -kristu, -kristi, -krista, pag. -kristīju",
				"kristīt", false), // iekristīt
		SecondThirdConj.directAllPersParallel(
				"-krustīju, -krustī, -krustī, arī -krustu, -krusti, -krusta, pag. -krustīju",
				"krustīt", false), // iekrustīt
		SecondThirdConj.directAllPersParallel(
				"-kveldēju, -kveldē, -kveldē, arī -kveldu, -kveldi, -kveld, pag. -kveldēju",
				"kveldēt", false), // nokveldēt
		SecondThirdConj.directAllPersParallel(
				"-ķēzīju, -ķēzī, -ķēzī, arī -ķēzu, -ķēzi, -ķēza, pag. -ķēzīju",
				"ķēzīt", false), // apķēzīt
		SecondThirdConj.directAllPersParallel(
				"-lāsu, -lāsi, -lās, retāk -lāsēju, -lāsē, -lāsē, pag. -lāsēju",
				"lāsēt", false), // nolāsēt
		SecondThirdConj.directAllPersParallel(
				"-mērīju, -mērī, -mērī, arī -mēru, -mēri, -mēra, pag. -mērīju",
				"mērīt", false), // atmērīt
		SecondThirdConj.directAllPersParallel(
				"-pelnu, -pelni, -pelna, arī -pelnīju, -pelnī, -pelnī, pag. -pelnīju",
				"pelnīt", false), // atpelnīt
		SecondThirdConj.directAllPersParallel(
				"-pētīju, -pētī, -pētī, arī -pētu, -pēti, -pēta, pag. -pētīju",
				"pētīt", false), // appētīt
		SecondThirdConj.directAllPersParallel(
				"-pūlu, -pūli, -pūl, arī -pūlēju, -pūlē, -pūlē, pag. -pūlēju",
				"pūlēt", false), // nopūlēt
		SecondThirdConj.directAllPersParallel(
				"-rotu, -roti, -rota, arī -rotīju, -roti, -rotī, pag. -rotīju",
				"rotīt", false), // aizrotīt
		SecondThirdConj.directAllPersParallel(
				"-sargāju, -sargā, -sargā, arī -sargu, -sargi, -sarga, pag. -sargāju",
				"sargāt", false), // aizsargāt
		SecondThirdConj.directAllPersParallel(
				"-svētīju, -svētī, -svētī, arī -svētu, -svēti, -svēta, pag. -svētīju",
				"svētīt", false), // aizsargāt
		SecondThirdConj.directAllPersParallel(
				"-tašķīju, -tašķī, -tašķī, arī -tašķu, -tašķi, -tašķa, pag. -tašķīju",
				"tašķīt", false), // aptašķīt
		SecondThirdConj.directAllPersParallel(
				"-veltīju, -veltī, -veltī, arī -veltu, -velti, -velta, pag. -veltīju",
				"veltīt", false), // apveltīt
		SecondThirdConj.directAllPersParallel(
				"-vētīju, -vētī, -vētī, arī -vētu, -vēti, -vēta, pag. -vētīju",
				"vētīt", false), // aizvētīt

		// TODO vai tiešām nav saskaņojams ne ar vienu priedēkļa likumu?
		SecondThirdConj.directAllPersParallel(
				"-bālu, -bāli, -bāl, arī -bālēju, -bālē, -bālē, pag. -bālēju",
				"bālēt", false), // bālēt
		SecondThirdConj.directAllPersParallel(
				"-durnu, -durni, -durn, arī -durnēju, -durnē, -durnē, pag. -durnēju",
				"durnēt", false), // durnēt
		SecondThirdConj.directAllPersParallel(
				"-kluknu, -klukni, -klukn, arī -kluknēju, -kluknē, -kluknē, pag. -kluknēju",
				"kluknēt", false), // kluknēt
		SecondThirdConj.directAllPersParallel(
				"-kruknu, -krukni, -krukn, arī -kruknēju, -kruknē, -kruknē, pag. -kruknēju",
				"kruknēt", false), // kruknēt
		SecondThirdConj.directAllPersParallel(
				"-kuknu, -kukni, -kukn, arī -kuknēju, -kuknē, -kuknē, pag. -kuknēju",
				"kuknēt", false), // kuknēt
		SecondThirdConj.directAllPersParallel(
				"-pleknu, -plekni, -plekn, arī -pleknēju, -pleknē, -pleknē, pag. -pleknēju",
				"pleknēt", false), // pleknēt
		SecondThirdConj.directAllPersParallel(
				"-vēdīju, -vēdī, -vēdī, arī -vēdu, -vēdi, -vēda, pag. vēdīju",
				"vēdīt", false), //vēdīt

		// Paralēlformu paralēlforma.
		SecondThirdConj.directAllPersParallel(
				"-bālu, -bāli, -bāl, arī -bālēju, -bālē, -balē, pag. -bālēju (arī -bālu, 1. konj.)",
				"bālēt", false), //nobālēt
		SecondThirdConj.directAllPersParallel(
				"-bālēju, -bālē, -bālē, arī -bālu, -bāli, -bāl, pag. -bālēju (retāk -bālu, 1. konj.)",
				"bālēt", false), //sabālēt
		SecondThirdConj.directAllPersParallel(
				"-rūsēju, -rūsē, -rūsē, arī -rūsu, -rūsi, -rūs, pag. -rūsēju (retāk -rūsu, 1. konj.)",
				"rūsēt", false), // ierūsēt

		// Likumi, kam ir "parasti 3. pers." variants.
		// Paralēlformu paralēlforma
		SecondThirdConj.direct3PersParallel(
				"-rūsē, arī -rūs, pag. -rūsēja (retāk -rūsa, 1. konj.)", "rūsēt", false), // aizsūsēt

		// Standartizētie
		// A, B
		SecondThirdConj.direct3PersParallel(
				"-balē, arī -bal, pag. -balēja", "balēt", false), // apbalēt
		SecondThirdConj.direct3PersParallel(
				"-bāl, arī -bālē, pag. -bālēja", "bālēt", false), // iebālēt
		SecondThirdConj.direct3PersParallel(
				"-brūn, arī -brūnē, pag. -brūnēja", "brūnēt", false), // iebrūnēt
		SecondThirdConj.direct3PersParallel(
				"-būb, arī -būbē, pag. -būbēja", "būbēt", false), // nobūbēt
		// C, D
		SecondThirdConj.direct3PersParallel(
				"-dim, arī -dimē, pag. -dimēja", "dimēt", false), // nodimēt
		// E, F, G
		SecondThirdConj.direct3PersParallel(
				"-gail, arī -gailē, pag. -gailēja", "gailēt", false), // izgailēt
		SecondThirdConj.direct3PersParallel(
				"-gaidē, arī -gald, pag. -galdēja", "galdēt", false), // sagaldēt
		// H, I, J
		SecondThirdConj.direct3PersParallel(
				"-junda, arī -jundī, pag. -jundīja", "jundīt", false), // iejundīt
		// K
		SecondThirdConj.direct3PersParallel(
				"-kveldē, arī -kveld, pag. -kveldēja", "kveldēt", false), // pakveldēt
		// L, M, N, O, P
		SecondThirdConj.direct3PersParallel(
				"-pelē, arī -pel, pag. -pelēja", "pelēt", false), // aizpelēt
		SecondThirdConj.direct3PersParallel(
				"-plekn, arī -pleknē, pag. -pleknēja", "pleknēt", false), // sapleknēt
		SecondThirdConj.direct3PersParallel(
				"-plēn, arī -plēnē, pag. -plēnēja", "plēnēt", false), // applēnēt
		// R
		SecondThirdConj.direct3PersParallel(
				"-rec, arī -recē, pag. -recēja", "recēt", false), // sarecēt
		// S
		SecondThirdConj.direct3PersParallel(
				"-sūb, arī -sūbē, pag. -sūbēja", "sūbēt", false), // aizsūbēt
		SecondThirdConj.direct3PersParallel(
				"-sus, arī -susē, pag. -susēja", "susēt", false), // izsusēt
		// T, U, V, Z
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
		FirstConj.reflHomof("-aužos, -audies,", "-aužas, pag. -audos", "austies",
				"\"austies\" (audumam)"), //apausties
		FirstConj.reflHomof("-dzenos, -dzenies,", "-dzenas, pag. -dzinos", "dzīties",
				"\"dzīties\" (lopiem)"), //aizdzīties
		FirstConj.reflHomof("-iros, -iries,", "-iras, pag. -īros", "irties",
				"\"irties\" (ar airiem)"), //aizirties
		FirstConj.reflHomof("-lienos, -lienies,", "-lienas, pag. -līdos", "līsties",
				"\"līsties\" (zem galda)"), // palīsties // Liekas, ka otra vārdnīcā nav.
		FirstConj.reflHomof("-mistos, -misties,", "-mistas, pag. -misos", "misties",
				"\"mistties\" (krist izmisumā)"), //izmisties // Liekas, ka otra vārdnīcā nav.
		FirstConj.reflHomof("-minos, -minies,", "-minas, pag. -minos", "mīties",
				"\"mīties\" (pedāļus)"), //aizmīties
		FirstConj.reflHomof("-mijos, -mijies,", "-mijas, pag. -mijos", "mīties",
				"\"mīties\" (mainīt naudu)"), //apmīties
		FirstConj.reflHomof("-tiekos, -tiecies,", "-tiekas, pag. -tikos", "tikties",
				"\"tikties\" (satikties ar kādu)"), //satikties
		// Izņēmuma izņēmums :/
		FirstConj.reflHomof("-patīkos, -patīcies,", "-patīkas, pag. -patikos", "patikties",
				"\"tikties\" (patikties kādam)"), //iepatikties
		FirstConj.reflHomof("-vēršos, -vērsies,", "-vēršas, pag. -vērsos", "vērsties",
				"\"vērsties\" (mainīt virzienu)"), //aizvērsties, izvērsties 1
		FirstConj.reflHomof("-vēršos, -vērties,", "-vēršas, pag. -vērtos", "vērsties",
				"\"vērsties\" (mainīt būtību)"), //izvērsties 2


		// Paralēlās formas.
		FirstConj.reflAllPersParallel(
				"-aujos, -aujies, -aujas, arī -aunos, -aunies, -aunas, pag. -āvos", "auties"), //apauties
		FirstConj.reflAllPersParallel(
				"-gulstos, -gulsties, -gulstas, arī -guļos, -gulies, -guļas, pag. -gūlos, arī -gulos",
				"gulties"), //aizgulties
		FirstConj.reflAllPersParallel(
				"-plešos, -pleties, -plešas, pag. -pletos, arī -plētos", "plesties"), //ieplesties
		FirstConj.reflAllPersParallel(
				"-sēžos, -sēdies, -sēžas, arī -sēstos, -sēsties, -sēstas, pag. -sēdos", "sēsties"), //aizsēsties
		FirstConj.reflAllPersParallel(
				"-skaišos, -skaities, -skaišas, arī -skaistos, -skaisties, -skaistas, pag. -skaitos", "skaisties"), //noskaisties
		FirstConj.reflAllPersParallel(
				"-skrienos, -skrienies, -skrienas, arī -skrejos, -skrejies, -skrejas, pag. -skrējos", "skrieties"), //ieskrieties
		FirstConj.reflAllPersParallel(
				"-slienos, -slienies, -slienas, arī -slejos, -slejies, -slejas, pag. -slējos", "slieties"), //aizslieties
		FirstConj.reflAllPersParallel(
				"-spurdzos, -spurdzies, -spurdzas, pag. -spurdzos, retāk -spurgstos, -spurgsties, -spurgstas, pag. -spurgos", "spurgties"), //iespurgties

		// Izņēmums.
		VerbDoubleRule.of("-pazīstos, -pazīsties,", "-pazīstas, pag. -pazinos", "pazīties", 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"zīties\"")}, null,
				new String[]{"zī"}, new String[]{"zīst"}, new String[]{"zin"}), //iepazīties

		// Standartizētie.
		// A
		FirstConj.refl("-aros, -aries,", "-aras, pag. -aros", "arties"), //iearties
		// B
		FirstConj.refl("-baros, -baries,", "-baras, pag. -bāros", "bārties"), //izbārties
		FirstConj.refl("-bāžos, -bāzies,", "-bāžas, pag. -bāzos", "bāzties"), //iebāzties
		FirstConj.refl("-beidzos, -beidzies,", "-beidzas, pag. -beidzos", "beigties"), //izbeigties
		FirstConj.refl("-beržos, -berzies,", "-beržas, pag. -berzos", "berzties"), //izberzties
		FirstConj.refl("-bēgos, -bēdzies,", "-bēgas, pag. -bēgos", "bēgties"), //nobēgties
		FirstConj.refl("-beros, -beries,", "-beras, pag. -bēros", "bērties"), //apbērties
		FirstConj.refl("-bīstos, -bīsties,", "-bīstas, pag. -bijos", "bīties"), //izbīties
		FirstConj.refl("-božos, -bozies,", "-bozās, pag. -bozos", "bozties"), //pabozties
		FirstConj.refl("-braucos, -braucies,", "-braucas, pag. -braucos", "braukties"), //izbraukties
		FirstConj.refl("-brāžos, -brāzies,", "-brāžas, pag. -brāzos", "brāzties"), //aizbrāzties
		FirstConj.refl("-brienos, -brienies,", "-brienas, pag. -bridos", "bristies"), //atbristies
		FirstConj.refl("-būros, -buries,", "-buras, pag. -būros", "burties"), //izburties
		// C
		FirstConj.refl("-ceļos, -celies,", "-ceļas, pag. -cēlos", "celties"), //apcelties
		FirstConj.refl("-cenšos, -centies,", "-cenšas, pag. -centos", "censties"), //pazensties
		FirstConj.refl("-cepos, -cepies,", "-cepas, pag. -cepos", "cepties"), //sacepties
		FirstConj.refl("-ciešos, -cieties,", "-ciešas, pag. -cietos", "ciesties"), //aizciesties
		FirstConj.refl("-cērtos, -cērties,", "-cērtas, pag. -cirtos", "cirsties"), //aizcirsties
		// D
		FirstConj.refl("-dejos, -dejies,", "-dejas, pag. -dējos", "dieties"), //izdieties
		FirstConj.refl("-dodos, -dodies,", "-dodas, pag. -devos", "doties"), //atdoties
		FirstConj.refl("-drāžos, -drāzies,", "-drāžas, pag. -drāzos", "drāzties"), //aizdrāzties
		FirstConj.refl("-duros, -duries,", "-duras, pag. -dūros", "durties"), //aizdurties
		FirstConj.refl("-dzeļos, -dzelies,", "-dzeļas, pag. -dzēlos", "dzelties"), //sadzelties
		FirstConj.refl("-dzeros, -dzeries,", "-dzeras, pag. -dzēros", "dzerties"), //apdzerties
		FirstConj.refl("-dzēšos, -dzēsies,", "-dzēšas, pag. -dzēsos", "dzēsties"), //izdzēsties
		// E
		FirstConj.refl("-ēdos, -ēdies,", "-ēdas, pag. -ēdos", "ēsties"), //atēsties
		// F, G
		FirstConj.refl("-gaužos, -gaudies,", "-gaužas, pag. -gaudos", "gausties"), //izgausties
		FirstConj.refl("-gāžos, -gāzies,", "-gāžas, pag. -gāzos", "gāzties"), //apgāzties, aizgāzties
		FirstConj.refl("-glaužos, -glaudies,", "-glaužas, pag. -glaudos", "glausties"), //ieglausties
		FirstConj.refl("-glābjos, -glābies,", "-glābjas, pag. -glābos", "glābties"), //izglābties
		FirstConj.refl("-graužos, -grauzies,", "-graužas, pag. -grauzos", "grauzties"), //izgrauzties, aizgrauzties
		FirstConj.refl("-gremžos, -gremdies,", "-gremžas, pag. -gremdos", "gremsties"), //izgremsties
		FirstConj.refl("-gremžos, -gremzies,", "-gremžas, pag. -gremzos", "gremzties"), //izgremzties
		FirstConj.refl("-griežos, -griezies,", "-griežas, pag. -griezos", "griezties"), //aizgriezties 1, 2
		FirstConj.refl("-gružos, -grūdies,", "-grūžas, pag. -grūdos", "grūsties"), //atgrūsties
		FirstConj.refl("-gūstos, -gūsties,", "-gūstas, pag. -guvos", "gūties"), //aizgūties
		// Ģ
		FirstConj.refl("-ģērbjos, -ģērbies,", "-ģērbjas, pag. -ģērbos", "ģērbties"), //apģērbties
		// H, I, Ī
		FirstConj.refl("-īdos, -īdies,", "-īdas, pag. -īdējos", "īdēties"), //ieīdēties
		// J
		FirstConj.refl("-jaucos, -jaucies,", "-jaucas, pag. -jaucos", "jaukties"), //iejaukties
		FirstConj.refl("-jājos, -jājies,", "-jājas, pag. -jājos", "jāties"), //izjāties
		FirstConj.refl("-jēdzos, -jēdzies,", "-jēdzas, pag. -jēdzos", "jēgties"), //nojēgties
		FirstConj.refl("-jožos, -jozies,", "-jožas, pag. -jozos", "jozties"), //apjozties
		FirstConj.refl("-jūtos, -jūties,", "-jūtas, pag. -jutos", "justies"), //iejusties
		FirstConj.refl("-jūdzos, -jūdzies,", "-jūdzas, pag. -jūdzos", "jūgties"), //aizjūgties
		// K
		FirstConj.refl("-kaļos, -kalies,", "-kaļas, pag. -kalos", "kalties"), //iekalties
		FirstConj.refl("-kampjos, -kampies,", "-kampjas, pag. -kampos", "kampties"), //apkampties
		FirstConj.refl("-kaujos, -kaujies,", "-kaujas, pag. -kāvos", "kauties"), //atkauties
		FirstConj.refl("-kāpjos, -kāpies,", "-kāpjas, pag. -kāpos", "kāpties"), //atkāpties
		FirstConj.refl("-karos, -karies,", "-karas, pag. -kāros", "kārties"), //apkārties
		FirstConj.refl("-klājos, -klājies,", "-klājas, pag. -klājos", "klāties"), //apklāties
		FirstConj.refl("-kļaujos, -kļaujies,", "-kļaujas, pag. -kļāvos", "kļauties"), //apkļauties
		FirstConj.refl("-kniebjos, -kniebies,", "-kniebjas, pag. -kniebos", "kniebties"), //iekniebties
		FirstConj.refl("-kopjos, -kopies,", "-kopjas, pag. -kopos", "kopties"), //apkopties
		FirstConj.refl("-kožos, -kodies,", "-kožas, pag. -kodos", "kosties"), //atkosties
		FirstConj.refl("-kraujos, -kraujies,", "-kraujas, pag. -krāvos", "krauties"), //apkrauties
		FirstConj.refl("-krāpjos, -krāpies,", "-krāpjas, pag. -krāpos", "krāpties"), //apkrāpties
		FirstConj.refl("-krājos, -krājies,", "-krājas, pag. -krājos", "krāties"), //iekrāties
		FirstConj.refl("-krītos, -krīties,", "-krītas, pag. -kritos", "kristies"), //izkristies
		FirstConj.refl("-kuļos, -kulies,", "-kuļas, pag. -kūlos", "kulties"), //aizkulties
		// Ķ
		FirstConj.refl("-ķeros, -ķeries,", "-ķeras, pag. -ķēros", "ķerties"), //aizķerties, pieķerties
		// L
		FirstConj.refl("-laižos, -laidies,", "-laižas, pag. -laidos", "laisties"), //aizlaisties
		FirstConj.refl("-laužos, -lauzies,", "-laužas, pag. -lauzos", "lauzties"), //aizlauzties
		FirstConj.refl("-liedzos, -liedzies,", "-liedzas, pag. -liedzos", "liegties"), //aizliegties
		FirstConj.refl("-liecos, -liecies,", "-liecas, pag. -liecos", "liekties"), //aizliekties
		FirstConj.refl("-lejos, -lejies,", "-lejas, pag. -lējos", "lieties"), //aplieties
		FirstConj.refl("-liekos, -liecies,", "-liekas, pag. -likos", "likties"), //aizlikties
		FirstConj.refl("-lūdzos, -lūdzies,", "-lūdzas, pag. -lūdzos", "lūgties"), //atlūgties
		// Ļ
		FirstConj.refl("-ļaujos, -ļaujies,", "-ļaujas, pag. -ļāvos", "ļauties"), //atļauties
		// M
		FirstConj.refl("-maļos, -malies,", "-maļas, pag. -malos", "malties"), //apmalties
		FirstConj.refl("-maucos, -maucies,", "-maucas, pag. -maucos", "maukties"), //izmaukties
		FirstConj.refl("-mācos, -mācies,", "-mācas, pag. -mācos", "mākties"), //nomākties
		FirstConj.refl("-mājos, -mājies,", "-mājas, pag. -mājos", "māties"), //samāties
		FirstConj.refl("-melšos, -melsies,", "-melšas, pag. -melsos", "melsties"), //izmelsties
		FirstConj.refl("-metos, -meties,", "-metas, pag. -metos", "mesties"), //aizmesties
		FirstConj.refl("-mērcos, -mērcies,", "-mērcas, pag. -mērcos", "mērkties"), //iemērkties
		FirstConj.refl("-miedzos, -miedzies,", "-miedzas, pag. -miedzos", "miegties"), //miegties, iemiegties
		FirstConj.refl("-mostos, -mosties,", "-mostas, pag. -modos", "mosties"), //atmosties
		// N
		FirstConj.refl("-nākos, -nācies,", "-nākas, pag. -nācos", "nākties"), //iznākties
		FirstConj.refl("-nesos, -nesies,", "-nesas, pag. -nesos", "nesties"), //aiznesties
		FirstConj.refl("-nirstos, -nirsties,", "-nirstas, pag. -niros", "nirties"), //ienirties
		FirstConj.refl("-nīkstos, -nīksties,", "-nīkstas, pag. -nīkos", "nīkties"), //iznīkties
		FirstConj.refl("-nīstos, -nīsties,", "-nīstas, pag. -nīdos", "nīsties"), //sanīsties
		// Ņ
		FirstConj.refl("-ņemos, -ņemies,", "-ņemas, pag. -ņēmos", "ņemties"), //aizņemties
		// O
		FirstConj.refl("-ožos, -odies,", "-ožas, pag. -odos", "osties"), //saosties
		// P
		FirstConj.refl("-peros, -peries,", "-peras, pag. -pēros", "pērties"), //aizpērties
		FirstConj.refl("-pērkos, -pērcies,", "-pērkas, pag. -pirkos", "pirkties"), // appirkties
		FirstConj.refl("-pinos, -pinies,", "-pinas, pag. -pinos", "pīties"), // iepīties
		FirstConj.refl("-plēšos, -plēsies,", "-plēšas, pag. -plēsos", "plēsties"), // izplēsties
		FirstConj.refl("-plēšos, -plēsies,", "-plēšas, pag. -plēsos", "plēsties"), // izplēsties
		FirstConj.refl("-plijos, -plijies,", "-plijas, pag. -plijos", "plīties"), // uzplīties
		FirstConj.refl("-plūcos, -plūcies,", "-plūcas, pag. -plūcos", "plūkties"), // izplukties
		FirstConj.refl("-pļaujos, -pļaujies,", "-pļaujas, pag. -pļāvos", "pļauties"), // appļauties
		FirstConj.refl("-pošos, -posies,", "-pošas, pag. -posos", "posties"), // saposties
		FirstConj.refl("-protos, -proties,", "-protas, pag. -pratos", "prasties"), // saprasties
		FirstConj.refl("-pūšos, -pūties,", "-pūšas, pag. -pūtos", "pūsties"), // atpūsties
		// R
		FirstConj.refl("-rokos, -rocies,", "-rokas, pag. -rakos", "rakties"), // aizrakties
		FirstConj.refl("-rodos, -rodies,", "-rodas, pag. -rados", "rasties"), // atrasties
		FirstConj.refl("-raušos, -rausies,", "-raušas, pag. -rausos", "rausties"), //aizrausties
		FirstConj.refl("-raujos, -raujies,", "-raujas, pag. -rāvos", "rauties"), //aizrauties
		FirstConj.refl("-rāpjos, -rāpies,", "-rāpjas, pag. -rāpos", "rāpties"), // aizrāpties
		FirstConj.refl("-rājos, -rājies,", "-rājas, pag. -rājos", "rāties"), // izrāties
		FirstConj.refl("-redzos, -redzies,", "-redzas, pag. -redzējos", "redzēties"), // izredzēties
		FirstConj.refl("-riebjos, -riebies,", "-riebjas, pag. -riebos", "riebties"), //aizriebties, apriebties
		FirstConj.refl("-rejos, -rejies,", "-rejas, pag. -rējos", "rieties"), //atrieties
		FirstConj.refl("-rimstos, -rimsties,", "-rimstas, pag. -rimos", "rimties"), //aprimties
		FirstConj.refl("-rijos, -rijies,", "-rijas, pag. -rijos", "rīties"), //aizrīties
		// S
		FirstConj.refl("-saucos, -saucies,", "-saucas, pag. -saucos", "saukties"), //aizsaukties
		FirstConj.refl("-sedzos, -sedzies,", "-sedzas, pag. -sedzos", "segties"), //aizsegties
		FirstConj.refl("-sēršos, -sērsies,", "-sēršas, pag. -sērsos", "sērsties"), //izsērsties
		FirstConj.refl("-sējos, -sējies,", "-sējas, pag. -sējos", "sēties"), //izsēties
		FirstConj.refl("-sienos, -sienies,", "-sienas, pag. -sējos", "sieties"), //aizsieties
		FirstConj.refl("-sitos, -sities,", "-sitas, pag. -sitos", "sisties"), //aizsisties
		FirstConj.refl("-skaros, -skaries,", "-skaras, pag. -skaros", "skarties"), //pieskarties
		FirstConj.refl("-skaišos, -skaisties,", "-skaišas, pag. -skaitos", "skaisties"), //apskaisties
		FirstConj.refl("-skaujos, -skaujies,", "-skaujas, pag. -skāvos", "skauties"), //apskauties
		FirstConj.refl("-skujos, -skujies,", "-skujas, pag. -skuvos", "skūties"), //noskūties
		FirstConj.refl("-slēdzos, -slēdzies,", "-slēdzas, pag. -slēdzos", "slēgties"), //ieslēgties
		FirstConj.refl("-slēpjos, -slēpies,", "-slēpjas, pag. -slēpos", "slēpties"), //aizslēpties
		FirstConj.refl("-sliecos, -sliecies,", "-sliecas, pag. -sliecos", "sliekties"), //aizsliekties
		FirstConj.refl("-smeļos, -smelies,", "-smeļas, pag. -smēlos", "smelties"), //iesmelties
		FirstConj.refl("-smejos, -smejies,", "-smejas, pag. -smējos", "smieties"), //atsmieties
		FirstConj.refl("-sniedzos, -sniedzies,", "-sniedzas, pag. -sniedzos", "sniegties"), //aizsniegties
		FirstConj.refl("-speros, -speries,", "-speras, pag. -spēros", "sperties"), //atsperties
		FirstConj.refl("-spiežos, -spiedies,", "-spiežas, pag. -spiedos", "spiesties"), //aizspiesties
		FirstConj.refl("-spraucos, -spraucies,", "-spraucas, pag. -spraucos", "spraukties"), //aizspraukties
		FirstConj.refl("-spraužos, -spraudies,", "-spraužas, pag. -spraudos", "sprausties"), //aizsprausties
		FirstConj.refl("-spriežos, -spriedies,", "-spriežas, pag. -spriedos", "spriesties"), //aizspriesties
		FirstConj.refl("-stājos, -stājies,", "-stājas, pag. -stājos", "stāties"), //aizstāties
		FirstConj.refl("-steidzos, -steidzies,", "-steidzas, pag. -steidzos", "steigties"), //aizsteigties
		FirstConj.refl("-stiepjos, -stiepies,", "-stiepjas, pag. -stiepos", "stiepties"), //aizstiepties
		FirstConj.refl("-stumjos, -stumies,", "-stumjas, pag. -stūmos", "stumties"), //aizstumties
		FirstConj.refl("-sūcos, -sūcies,", "-sūcas, pag. -sūcos", "sūkties"), //piesūkties
		FirstConj.refl("-sveļos, -svelies,", "-sveļas, pag. -svēlos", "svelties"), //svelties, iesvelties
		FirstConj.refl("-svempjos, -svempies,", "-svempjas, pag. -svempos", "svempties"), //iesvempties
		FirstConj.refl("-sveros, -sveries,", "-sveras, pag. -svēros", "svērties"), //apsvērties
		FirstConj.refl("-sviežos, -sviedies,", "-sviežas, pag. -sviedos", "sviesties"), //aizsviesties
		// Š
		FirstConj.refl("-šaujos, -šaujies,", "-šaujas, pag. -šāvos", "šauties"), //aizšauties
		FirstConj.refl("-šķeļos, -šķelies,", "-šķeļas, pag. -šķēlos", "šķelties"), //atšķelties
		FirstConj.refl("-šķiebjos, -šķiebies,", "-šķiebjas, pag. -šķiebos", "šķiebties"), //nošķiebties
		FirstConj.refl("-šķiežos, -šķiedies,", "-šķiežas, pag. -šķiedos", "šķiesties"), //apsķiesties
		FirstConj.refl("-šķiros, -šķiries,", "-šķiras, pag. -šķīros", "šķirties"), //atšķirties
		FirstConj.refl("-šļācos, -šļācies,", "-šļācas, pag. -šļācos", "šļākties"), //apšļākties
		FirstConj.refl("-šmaucos, -šmaucies,", "-šmaucas, pag. -šmaucos", "šmaukties"), //apšmaukties
		FirstConj.refl("-šujos, -šujies,", "-šujas, pag. -šuvos", "šūties"), //izšūties
		// T
		FirstConj.refl("-teicos, -teicies,", "-teicas, pag. -teicos", "teikties"), //pateikties
		FirstConj.refl("-tērpjos, -tērpies,", "-tērpjas, pag. -tērpos", "tērpties"), //aptērpties
		FirstConj.refl("-tiecos, -tiecies,", "-tiecas, pag. -tiecos", "tiekties"), //ietiekties
		FirstConj.refl("-tiepjos, -tiepies,", "-tiepjas, pag. -tiepos", "tiepties"), //ietiepties
		FirstConj.refl("-tinos, -tinies,", "-tinas, pag. -tinos", "tīties"), //aiztīties
		FirstConj.refl("-traucos, -traucies,", "-traucas, pag. -traucos", "traukties"), //aiztraukties
		FirstConj.refl("-traušos, -trausies,", "-traušas, pag. -trausos", "trausties"), //ietrausties
		FirstConj.refl("-triecos, -triecies,", "-triecas, pag. -triecos", "triekties"), //attriekties
		FirstConj.refl("-triepjos, -triepies,", "-triepjas, pag. -triepos", "triepties"), //aptriepties
		FirstConj.refl("-trinos, -trinies,", "-trinas, pag. -trinos", "trīties"), //notrīties
		FirstConj.refl("-trūkstos, -trūksties,", "-trūkstas, pag. -trūkos", "trūkties"), //iztrūkties
		FirstConj.refl("-tupstos, -tupsties,", "-tupstas, pag. -tupos", "tupties"), //aiztupties
		FirstConj.refl("-tveros, -tveries,", "-tveras, pag. -tvēros", "tverties"), //ietverties
		// U
		FirstConj.refl("-urbjos, -urbies,", "-urbjas, pag. -urbos", "urbties"), //aizurbties
		// V
		FirstConj.refl("-vācos, -vācies,", "-vācas, pag. -vācos", "vākties"), //aizvākties
		FirstConj.refl("-veļos, -velies,", "-veļas, pag. -vēlos", "velties"), //aizvelties
		FirstConj.refl("-vedos, -vedies,", "-vedas, pag. -vedos", "vesties"), //izvesties
		FirstConj.refl("-veros, -veries,", "-veras, pag. -vēros", "vērties"), //pavērties
		FirstConj.refl("-vērpjos, -vērpies,", "-vērpjas, pag. -vērpos", "vērpties"), //apvērpties
		FirstConj.refl("-viebjos, -viebies,", "-viebjas, pag. -viebos", "viebties"), //noviebties
		FirstConj.refl("-velkos, -velcies,", "-velkas, pag. -vilkos", "vilkties"), //aizvilkties
		FirstConj.refl("-viļos, -vilies,", "-viļas, pag. -vīlos", "vilties"), //pievilties
		FirstConj.refl("-vīkšos, -vīkšies,", "-vīkšas, pag. -vīkšos", "vīkšties"), //savīkšties
		FirstConj.refl("-vijos, -vijies,", "-vijas, pag. -vijos", "vīties"), //apvīties
		// Z
		FirstConj.refl("-zogos, -zodzies,", "-zogas, pag. -zagos", "zagties"), //aizzagties
		FirstConj.refl("-ziedzos, -ziedzies,", "-ziedzas, pag. -ziedzos", "ziegties"), //noziegties
		FirstConj.refl("-ziežos, -ziedies,", "-ziežas, pag. -ziedos", "ziesties"), //apziesties
		FirstConj.refl("-zīžos, -zīdies,", "-zīžas, pag. -zīdos", "zīsties"), //iezīsties
		FirstConj.refl("-zveļos, -zvelies,", "-zveļas, pag. -zvēlos", "zvelties"), //atzvelties
		// Ž
		FirstConj.refl("-žaujos, -žaujies,", "-žaujas, pag. -žāvos", "žauties"), //iežauties
		FirstConj.refl("-žņaudzos, -žņaudzies,", "-žņaudzas, pag. -žņaudzos", "žņaugties"), //iežņaugties

		// Vairāki lemmas varianti.
		FirstConj.reflMultiLemma("-lecos, -lecies,", "-lecas, pag. -lēcos",
				new String[]{"lēkties", "lekties"}), //atlēkties, izlekties


		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas
		FirstConj.refl3PersHomof("-tīkas, pag. -tikās", "tikties",
				"\"tikties\" (patikties kādam)"), //patikties

		// Paralēlās formas.
		FirstConj.refl3PersParallel("-plešas, pag. -pletās, arī -plētās", "plesties"), //aizplesties
		FirstConj.refl3PersParallel("-riešas, pag. -riesās, arī -rietās", "riesties"), //aizriesties

		// Standarts
		// A, B, C, D, E, F, G
		FirstConj.refl3Pers("-graujas, pag. -grāvās", "grauties"), // iegrauties
		// H, I, J, K
		FirstConj.refl3Pers("-kurcas, pag. -kurcās", "kurkties"), // iekurkties
		FirstConj.refl3Pers("-kuras, pag. -kurās", "kurties"), // iekurties
		// L, M
		FirstConj.refl3Pers("-maujas, pag. -māvās", "mauties"), // atmauties
		// N, O, P, R
		FirstConj.refl3Pers("-riežas, pag. -riezās", "riezties"), //ieriezties
		// S
		FirstConj.refl3Pers("-sākas, pag. -sākās", "sākties"), //aizsākties
		FirstConj.refl3Pers("-slaucas, pag. -slaucās", "slaukties"), //atslaukties
		// Š
		FirstConj.refl3Pers("-šķiļas, pag. -šķīlās", "šķilties"), //aizšķilties
		// T, U, V
		// TODO ko darīt ar viesties?
		FirstConj.refl3Pers("-viešas, pag. -viesās", "viesties"), //ieviesties
		// Z, Ž
		FirstConj.refl3Pers("-žmaudzas, pag. -žmaudzās", "žmaugties"), //iežmaugties

		// Daudzskaitļa formu likumi
		FirstConj.reflPlural("-brāžamies, -brāžaties, -brāžas, pag. -brāzāmies", "brāzties"), // sabrāzties
		FirstConj.reflPlural("-liekamies, -liekaties, -liekas, pag. -likāmies", "likties"), // salikties
		FirstConj.reflPlural("-rodamies, -rodaties, -rodas, pag. -radāmies", "rasties"), // sarasties
		FirstConj.reflPlural("-raušamies, -raušaties, -raušas, pag. -rausāmies", "rausties"), // sarausties
		FirstConj.reflPlural("-rāpjamies, -rāpjaties, -rāpjas, pag. -rāpāmies", "rāpties"), // sarāpties
		FirstConj.reflPlural("-spriežamies, -spriežaties, -spriežas, pag. -spriedāmies", "spriesties"), // saspriesties
		FirstConj.reflPlural("-traušamies, -traušaties, -traušas, pag. -trausāmies", "trausties"), // satrausties
		FirstConj.reflPlural("-tupstamies, -tupstaties, -tupstas, pag. -tupāmies", "tupties"), // satupties

		// Pilnīgs nestandarts.
		VerbDoubleRule.of("-teicos, -teicies,", "-teicas (tagadnes formas parasti nelieto), pag. -teicos", "teikties", 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"teikties\"")},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NOT_PRESENT_FORMS)},
				new String[]{"teik"}, new String[]{"teic"}, new String[]{"teic"}), //atteikties
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 19: Darbības vārdi 2. konjugācija atgriezeniski
	 */
	public static final Rule[] reflSecondConjVerb = {
			SecondConj.refl(
					"-knābājos, -knābājies,", "-knābājas, pag. -knābājos", "knābāties"), // pieknābāties
			SecondConj.refl(
					"-tusējos, -tusējies,", "-tusējas, pag. -tusējos", "tusēties"), // notusēties
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
		ThirdConj.reflAllPersParallel(
				"-ļogos, -ļogies, -ļogās, retāk -ļodzos, -ļodzies, -ļodzās, pag. -ļodzījos", "ļodzīties"), //noļodzīties
		ThirdConj.reflAllPersParallel(
				"-mokos, -mokies, -mokās, arī -mocos, -mocies, -mocās, pag. -mocījos", "mocīties"), //aizmocīties

		// Standartizētie.
		// A, B
		ThirdConj.refl("-braukos, -braukies,", "-braukās, pag. -braucījos", "braucīties", true), //atbraucīties
		// C
		ThirdConj.refl("-ceros, -ceries,", "-ceras, pag. -cerējos", "cerēties", false), //atcerēties
		// Č
		ThirdConj.refl("-čabinos, -čabinies,", "-čabinās, pag. -čabinājos", "čabināties", false), //iečabināties
		ThirdConj.refl("-čukstos, -čuksties,", "-čukstas, pag. -čukstējos", "čukstēties", false), //iečukstēties
		// D
		ThirdConj.refl("-deros, -deries,", "-deras, pag. -derējos", "derēties", false), //iederēties
		ThirdConj.refl("-dzirdos, -dzirdies,", "-dzirdas, pag. -dzirdējos", "dzirdēties", false), //sadzirdēties
		// E, F, G
		ThirdConj.refl("-grasos, -grasies,", "-grasās, pag. -grasījos", "grasīties", false), //pagrasīties
		ThirdConj.refl("-gribos, -gribies,", "-gribas, pag. -gribējos", "gribēties", false), //izgribēties
		// H, I, J, K
		ThirdConj.refl("-kustos, -kusties,", "-kustas, pag. -kustējos", "kustēties", false), //aizkustēties
		// L
		ThirdConj.refl("-lāpos, -lāpies,", "-lāpās, pag. -lāpījos", "lāpīties", false), //aplāpīties
		ThirdConj.refl("-lokos, -lokies,", "-lokās, pag. -locījos", "locīties", true), //atlocīties
		// Ļ
		ThirdConj.refl("-ļogos, -ļogies,", "-ļogās, pag. -ļodzījos", "ļodzīties", true), //izļodzīties
		// M
		ThirdConj.refl("-minos, -minies,", "-minas, pag. -minējos", "minēties", false), //atminēties
		ThirdConj.refl("-mīlos, -mīlies,", "-mīlas, pag. -mīlējos", "mīlēties", false), //iemīlēties
		ThirdConj.refl("-muldos, -muldies,", "-muldas, pag. -muldējos", "muldēties", false), //iemuldēties 1, 2
		// N, O, P
		ThirdConj.refl("-peldos, -peldies,", "-peldas, pag. -peldējos", "peldēties", false), //izpeldēties
		ThirdConj.refl("-precos, -precies,", "-precas, pag. -precējos", "precēties", false), //aizprecēties
		// R
		ThirdConj.refl("-raugos, -raugies,", "-raugās, pag. -raudzījos", "raudzīties", true), //apraudzīties
		// S
		ThirdConj.refl("-sakos, -sakies,", "-sakās, pag. -sacījos", "sacīties", true), //atsacīties
		ThirdConj.refl("-slakos, -slakies,", "-slakās, pag. -slacījos", "slacīties", true), //apslacīties
		ThirdConj.refl("-slaukos, -slaukies,", "-slaukās, pag. -slaucījos", "slaucīties", true), //apslaucīties
		ThirdConj.refl("-slogos, -slogies,", "-slogās, pag. -slodzījos", "slodzīties", true), //ieslodzīties
		ThirdConj.refl("-snaikos, -snaikies,", "-snaikās, pag. -snaicījos", "snaicīties", true), //izsnaicīties
		ThirdConj.refl("-strīdos, -strīdies,", "-strīdas, pag. -strīdējos", "strīdēties", false), //izstrīdēties
		ThirdConj.refl("-sūdzos, -sūdzies,", "-sūdzas, pag. -sūdzējos", "sūdzēties", false), //pasūdzēties
		ThirdConj.refl("-sūkstos, -sūksties,", "-sūkstās, pag. -sūkstījos", "sūkstīties", false), //nosūkstīties
		// Š
		ThirdConj.refl("-šļaukos, -šļaukies,", "-šļaukās, pag. -šļaucījos", "šļaucīties", true), //izšļaucīties
		// T
		ThirdConj.refl("-tekos, -tecies,", "-tekas, pag. -tecējos", "tecēties", true), //iztecēties
		ThirdConj.refl("-ticos, -ticies,", "-ticas, pag. -ticējos", "ticēties", false), // uzticēties
		ThirdConj.refl("-turos, -turies,", "-turas, pag. -turējos", "turēties", false), //atturēties
		// U, V
		ThirdConj.refl("-vēlos, -vēlies,", "-vēlas, pag. -vēlējos", "vēlēties", false), //atvēlēties
		ThirdConj.refl("-vīkšos, -vīkšies,", "-vīkšas, pag. -vīkšījos", "vīkšīties", false), // savīkšīties
		ThirdConj.refl("-vīkšķos, -vīkšķies,", "-vīkšķas, pag. -vīkšķījos", "vīkšķīties", false), // savīkšķīties
		// Z
		ThirdConj.refl("-zinos, -zinies,", "-zinās, pag. -zinājos", "zināties", false), //apzināties
		ThirdConj.refl("-zvēros, -zvēries,", "-zvēras, pag. -zvērējos", "zvērēties", false), //izzvērēties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		ThirdConj.refl3Pers("-lauzās, pag. -lauzījās", "lauzīties", false), //aplauzīties
		ThirdConj.refl3Pers("-lokās, pag. -locījās", "locīties", true), //aizlocīties
		ThirdConj.refl3Pers("-vajagas, pag. -vajadzējās", "vajadzēties", true), //ievajadzēties

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
		SecondThirdConj.reflAllPersParallel(
				"-gailējos, -gailējies, -gailējās, arī -gailos, -gailies, -gailas, pag. -gailējos",
				"gailēties", false), // iegailēties
		SecondThirdConj.reflAllPersParallel(
				"-kristījos, -kristījies, -kristījas, arī -kristos, -kristies, -kristās, pag. -kristījos",
				"kristīties", false), // nokristīties
		SecondThirdConj.reflAllPersParallel(
				"-krustījos, -krustījies, -krustījas, arī -krustos, -krusties, -krustās, pag. -krustījos",
				"krustīties", false), // nokrustīties
		SecondThirdConj.reflAllPersParallel(
				"-ķēzījos, -ķēzījies, -ķēzījas, arī -ķēzos, -ķēzies, -ķēzās, pag. -ķēzījos",
				"ķēzīties", false), //noķēzīties
		SecondThirdConj.reflAllPersParallel(
				"-mērījos, -mērījies, -mērījas, arī -mēros, -mēries, -mērās, pag. -mērījos",
				"mērīties", false), // izmērīties
		SecondThirdConj.reflAllPersParallel(
				"-pelnos, -pelnies, -pelnās, arī -pelnījos, -pelnījies, -pelnījās, pag. -pelnījos",
				"pelnīties", false), //izpelnīties
		SecondThirdConj.reflAllPersParallel(
				"-pūlos, -pūlies, -pūlas, arī -pūlējos, -pūlējies, -pūlējas, pag. -pūlējos",
				"pūlēties", false), // nopūlēties
		SecondThirdConj.reflAllPersParallel(
				"-sargājos, -sargājies, -sargājas, arī -sargos, -sargies, -sargās, pag. -sargājos",
				"sargāties", false), // aizsargāties
		SecondThirdConj.reflAllPersParallel(
				"-svētījos, -svētījies, -svētījas, arī -svētos, -svēties, -svētās, pag. -svētījos",
				"svētīties", false), // iesvētīties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// TODO vai tiešām nav saskaņojams ne ar vienu priedēkļa likumu?
		SecondThirdConj.refl3PersParallel(
				"-vēdījas, arī -vēdās, pag. -vēdījas", "vēdīties", false), // vēdīties
	};

}
