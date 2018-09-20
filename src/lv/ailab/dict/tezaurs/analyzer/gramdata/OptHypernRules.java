package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.struct.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.EndingRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.FirstConjStems;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.VerbDoubleRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.Participle;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns.*;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.FirstConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.SecondConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.SecondThirdConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.ThirdConj;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * TGram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar EndingRule.applyOptHyphens().
 * Ja vienai lemmai atbilst vairāki likumi (piemēram, verbiem ir reizēm ir
 * norādīta locīšana ar paralēlformām un reizēm bez), tad visiem likumiem jābūt
 * vienā klasē - vai nu OptHypernRules vai DirectRules, bet ne juku jukām.
 * Verbu likumiem visu personu likumus, no kuriem tiek atvasināti 3. personas
 * likumi, vajag likt pirms 3. personas likumiem - tad redundantie 3. personas
 * likumi uzrādīsies statistikā kā nelietoti.
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
	 * Metode klasē iekļauto likumu bloku iegūšanai pareizā secībā.
	 * @return saraksts ar likumu blokiem.
	 */
	public static ArrayList<EndingRule[]> getAll()
	{
		ArrayList<EndingRule[]> res = new ArrayList<>();
		// Vairākkonjugāciju likumi jāliek pirms vienas konj. likumiem, jo var
		// sanākt, ka viens likums ir otra prefikss.
		res.add(directMultiConjVerb);
		res.add(reflMultiConjVerb);

		res.add(directFirstConjVerb);
		res.add(directSecondConjVerb);
		res.add(directThirdConjVerb);
		res.add(reflFirstConjVerb);
		res.add(reflSecondConjVerb);
		res.add(reflThirdConjVerb);

		res.add(other);

		res.add(secondDeclNoun);
		res.add(thirdDeclNoun);
		res.add(fifthDeclNoun);
		res.add(sixthDeclNoun);

		return res;
	}

	/**
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final EndingRule[] other = {

		// 25. paradigma: vietniekvārdi.
		//BaseRule.of("ģen. -kā, dat. -kam, akuz., instr. -ko", ".*kas", 25,
		//		new Tuple[]{TFeatures.POS__PRONOUN, Tuple.of(TKeys.INFLECT_AS, "\"kas\"")},
		//		null), //daudzka

		// Nedefinēta paradigma: divdabji
		Participle.isUsi("-augušais; s. -augusi, -augusī", ".*audzis"), // cauraudzis
		Participle.isUsi("-brukušais; s. -brukuši, -brukusī", ".*brucis"), // pussabrucis
		Participle.isUsi("-jukušais; s. -jukusi, -jukusi", ".*jucis"), // pusjucis
		Participle.isUsi("-likušais; s. -likusi, -likusī", ".*licis"), // atpalicis
		Participle.isUsi("-plaukušais; s. -plaukusi, -plaukusī.", ".*plaucis"), // pusplaucis
		Participle.isUsi("-plukušais; s. -plukusi, -plukusī", ".*plucis"), // applucis
		Participle.isUsi("-sprāgušais; s. -sprāgusi, -sprāgusī", ".*sprādzis"), // pussprādzis
		Participle.isUsi("-tikušais; s. -tikusi, -tikusī", ".*ticis"), // pussprādzis
	};
	/**
	 * Paradigm 9: Lietvārds 5. deklinācija -e
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final EndingRule[] fifthDeclNoun = {
		// Ar mijām
		//FifthDecl.std("-es, dsk. ģen. -aļģu, s.", ".*aļģe"), // aļģe
		FifthDecl.std("-aļģes, dsk. ģen. -aļģu, s.", ".*aļģe"), // kramaļģe
		FifthDecl.std("-audzes, dsk. ģen. -audžu, s.", ".*audze"), // brūkleņaudze
		FifthDecl.std("-ātes, dsk. ģen. -āšu, s.", ".*āte"), //āte
		FifthDecl.std("-balles, dsk. ģen. -baļļu, s.", ".*balle"), // balle 1
		FifthDecl.std("-celles, dsk. ģen. -ceļļu, s.", ".*celle"), // celle
		FifthDecl.std("-cemmes, dsk. ģen. -cemmju, s.", ".*cemme"), // cemme
		FifthDecl.std("-dēles, dsk. ģen. -dēļu, s.", ".*dēle"), // dēle
		FifthDecl.std("-duktes, dsk. ģen. -dukšu, s.", ".*dukte"), // dukte
		FifthDecl.std("-eģes, dsk. ģen. -eģu, s.", ".*eģe"), // eģe
		FifthDecl.std("-elles, dsk. ģen. -eļļu, s.", ".*elle"), // elle
		FifthDecl.std("-eņģes, dsk. ģen. -eņģu, s.", ".*eņģe"), // eņģe
		FifthDecl.std("-epiķes, dsk. ģen. -epiķu", ".*epiķe"), // epiķe
		FifthDecl.std("-ērces, dsk. ģen. -ērču, s.", ".*ērce"), // ērce
		FifthDecl.std("-ēzes, dsk. ģen. -ēžu, s.", ".*ēze"), // ēze
		FifthDecl.std("-halles, dsk. ģen. -haļļu, s.", ".*halle"), // halle
		FifthDecl.std("-indes, dsk. ģen. -inžu, s.", ".*inde"), // inde
		FifthDecl.std("dsk. ģen. -īvju, s.", ".*īve"), // īve
		FifthDecl.std("-īzes, dsk. ģen. -īžu, s.", ".*īze"), // īze
		FifthDecl.std("-ķelles, dsk. ģen. -ķeļļu, s.", ".*ķelle"), // ķelle
		FifthDecl.std("-ķemmes, dsk. ģen. -ķemmju, s.", ".*ķemme"), // ķemme
		FifthDecl.std("-lodes, dsk. ģen. -ložu, s.", ".*lode"), // deglode
		FifthDecl.std("-mēres, dsk. ģen. -mēru, s.", ".*mēre"), // mēre
		FifthDecl.std("-ores, dsk. ģen. -oru, s.", ".*ore"), // ore
		FifthDecl.std("-resnes, dsk. ģen. -rešņu, s.", ".*resne"), // resne
		FifthDecl.std("-teces, dsk. ģen. -teču, s.", ".*tece"), // tece
		FifthDecl.std("-upes, dsk. ģen. -upju, s.", ".*upe"), // upe, krāčupe
		FifthDecl.std("-usnes, dsk. ģen. -ušņu, s.", ".*usne"), // usne
		FifthDecl.std("-vātes, dsk. ģen. -vāšu, s.", ".*vāte"), // vāte
		FifthDecl.std("-zīmes, dsk. ģen. -zīmju, s.", ".*zīme"), // biedruzīme

		FifthDecl.std("-ēdes, s.", ".*ēde"), // pienēde
		// Bez mijām
		FifthDecl.noChange("-astes, dsk. ģen. -astu, s.", ".*aste"), // ragaste
		FifthDecl.noChange("-balles, dsk. ģen. -ballu, s.", ".*balle"), //balle 2
		FifthDecl.noChange("-gāzes, dsk. ģen. -gāzu, s.", ".*gāze"), //deggāze

		// Vīriešu dzimte
		GenNoun.any("-tētes, dsk. ģen. -tētu, v.", ".*tēte", 47,
				null, new Tuple[]{TFeatures.GENDER__MASC}), // tēte

		GenNoun.any("-aļģu, s.", ".*aļģes", 9,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // zilaļģes
		GenNoun.any("-kreļļu, s.", ".*krelles", 9,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // krelles
	};

	/**
	 * Paradigmas 11, 35.: Lietvārds 6.. deklinācija, siev.dz.
	 * Likumi formā "-acs, dsk. ģen. -acu, s.".
	 */
	public static final EndingRule[] sixthDeclNoun = {
		// Ar mijām
		SixthDecl.std("-asins, dsk. ģen. -asiņu, s.", ".*asins"), // asins
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
		SixthDecl.std("dsk. ģen. -ģinšu", ".*ģints"), //ģints
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
		SixthDecl.std("-nāss, dsk. ģen. -nāšu, s.", ".*nāss"), //nāss
		SixthDecl.std("-nīts, dsk. ģen. -nīšu, s.", ".*nīts"), //nīts
		SixthDecl.std("-nots, dsk. ģen. -nošu, s.", ".*nots"), // nošu
		SixthDecl.std("-nūts, dsk. ģen. -nūšu, s.", ".*nūts"), // nūts
		SixthDecl.std("-olekts, dsk. ģen. -olekšu, s.", ".*olekts"), // olekts
		SixthDecl.std("-pāksts, dsk. ģen. -pākšu, s.", ".*pāksts"), // pāksts
		SixthDecl.std("-palts, dsk. ģen. -palšu, s.", ".*palts"), // palts
		SixthDecl.std("-pirts, dsk. ģen. -pirtu, s.", ".*pirts"), // asinspirts
		SixthDecl.std("-pils, dsk. ģen. -piļu, s.", ".*pils"), // ordeņpils
		SixthDecl.std("-plāts, dsk. ģen. -plāšu, s.", ".*plāts"), // plāts
		SixthDecl.std("-plīts, dsk. ģen. -plīšu, s.", ".*plīts"), // plīts
		SixthDecl.std("-pults, dsk. ģen. -pulšu, s.", ".*pults"), // operatorpults
		SixthDecl.std("-rūts, dsk. ģen. -rūšu, s.", ".*rūts"), // rūts
		SixthDecl.std("-sāls, dsk. ģen. -sāļu, s.", ".*sāls"), // sāls
		SixthDecl.std("-silkss, dsk. ģen. -silkšu, s.", ".*silkss"), // silkss
		SixthDecl.std("-sirds, dsk. ģen. -siržu, s.", ".*sirds"), // sirds
		SixthDecl.std("-smilts, dsk. ģen. -smilšu, s.", ".*smilts"), // viedsmilts
		SixthDecl.std("-skansts, dsk. ģen. -skanšu, s.", ".*skansts"), // skansts
		SixthDecl.std("-skrots, dsk. ģen. -skrošu, s.", ".*skrots"), // skrots
		SixthDecl.std("-spelts, dsk. ģen. -spelšu, s.", ".*spelts"), // spelts
		SixthDecl.std("-spīts, dsk. ģen. -spīšu, s.", ".*spīts"), // spīts
		SixthDecl.std("-stelts, dsk. ģen. stelšu, s.", ".*stelts"), // stelts
		SixthDecl.std("-tāss, dsk. ģen. -tāšu, s.", ".*tāss"), // tāss
		SixthDecl.std("-telts, dsk. ģen. -telšu, s.", ".*telts"), // telts
		SixthDecl.std("-uguns, dsk. ģen. -uguņu, s.", ".*uguns"), // jāņuguns
		SixthDecl.std("-vants, dsk. ģen. -vanšu, s.", ".*vants"), // vants
		SixthDecl.std("-vakts, dsk. ģen. -vakšu, s.", ".*vakts"), //vakts
		SixthDecl.std("-vāts, dsk. ģen. -vāšu, s.", ".*vāts"), // vāts
		SixthDecl.std("-zints, dsk. ģen. -zinšu, s.", ".*zints"), // zints
		SixthDecl.std("-zivs, dsk. ģen. -zivju, s.", ".*zivs"), // haizivs

		SixthDecl.std("-sāls, dsk. ģen. -sāļu, s.", ".*sāls"), // sāls 2
		SixthDecl.std("-dzelzs, s.", ".*dzelzs"), // stiegrdzelzs
		SixthDecl.std("-grants, s.", ".*grants"), // grants
		SixthDecl.std("-ģikts, s.", ".*ģikts"), // ģikts // TODO check!
		SixthDecl.std("-nakts, s.", ".*nakts"), // pastarnakts
		SixthDecl.std("-sāls, s.", ".*sāls"), // gabalsāls 2
		SixthDecl.std("-smilts, s.", ".*smilts"), // mālsmilts
		SixthDecl.std("-uguns, s.", ".*uguns"), // uguns

		// Bez mijām
		SixthDecl.noChange("-acs, dsk. ģen. -acu, s.", ".*acs"), //uzacs, acs
		SixthDecl.noChange("-akts, dsk. ģen. -aktu, s.", ".*akts"), //akts
		SixthDecl.noChange("-ass, dsk. ģen. -asu, s.", ".*ass"), //ass
		SixthDecl.noChange("-auss, dsk. ģen. -ausu, s.", ".*auss"), //auss
		SixthDecl.noChange("-balss, dsk. ģen. -balsu, s.", ".*balss"), //atbalss
		SixthDecl.noChange("-bikts, dsk. ģen. -biktu, s.", ".*bikts"), //bikts
		SixthDecl.noChange("-dakts, dsk. ģen. -daktu, s.", ".*dakts"), //dakts
		SixthDecl.noChange("-debess, dsk. ģen. -debesu, s.", ".*debess"), //debess 1, padebess
		SixthDecl.noChange("-grīsts, dsk. ģen. -grīstu, s.", ".*grīsts"), //grīsts
		SixthDecl.noChange("-maksts, dsk. ģen. -makstu, s.", ".*maksts"), //maksts
		SixthDecl.noChange("-šalts, dsk. ģen. -šaltu, s.", ".*šalts"), // šalts
		SixthDecl.noChange("-takts, dsk. ģen. -taktu, s.", ".*takts"), // pietakts
		SixthDecl.noChange("-uts, dsk. ģen. -utu, s.", ".*uts"), // uts
		SixthDecl.noChange("-valsts, dsk. ģen. -valstu, s.", ".*valsts"), //agrārvalsts
		SixthDecl.noChange("-versts, dsk. ģen. -verstu, s.", ".*versts"), // kvadrātversts
		SixthDecl.noChange("-vēsts, dsk. ģen. -vēstu, s.", ".*vēsts"), // vēsts
		SixthDecl.noChange("-zoss, dsk. ģen. -zosu, s.", ".*zoss"), // mežazoss

		SixthDecl.noChange("-auss, s.", ".*auss"), // zaķauss
		SixthDecl.noChange("-balss, s.", ".*balss"), // sirdsbalss
		SixthDecl.noChange("-valsts, s.", ".*valsts"), // padomjvalsts
		SixthDecl.noChange("-žults, s.", ".*žults"), // žults

		SixthDecl.optChange("-dūksts, dsk. ģen. -dūkstu, arī -dūkšu, s.", ".*dūksts"), //dūksts
		SixthDecl.optChange("-dzeņauksts, dsk. ģen. -dzeņaukstu, arī -dzeņaukšu, s.", ".*dzeņauksts"), //dzeņauksts

		GenNoun.any("-asiņu, s.", ".*asinis", 11,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // asinis
		GenNoun.any("-blakšu, s.", ".*blaktis", 11,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // mīkstblaktis
		GenNoun.any("-durvju, s.", ".*durvis", 11,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // kūtsdurvis
		GenNoun.any("-jūšu, s.", ".*jūtis", 11,
					new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // jūtis
		GenNoun.any("-krūšu, s.", ".*krūtis", 11,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // pakaļkrūtis

		GenNoun.any("-īkstu, s.", ".*īkstis", 35,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // īkstis
		GenNoun.any("-pirkstu, s.", ".*pirkstis", 35,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // pirkstis
		GenNoun.any("-spirkstu, s.", ".*spirkstis", 35,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // spirkstis
		GenNoun.any("-utu, s.", ".*utis", 35,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__FEM}), // grauzējutis

	};

	/**
	 * Paradigma 3, 4, 5: lietvārdi 2. deklinācijā
	 */
	public static final EndingRule[] secondDeclNoun = {
		SecondDecl.stdNomGen("-akmens, dsk. -akmeņi, v.", ".*akmens"), // akmens
		SecondDecl.stdNomGen("-asmens, dsk. -asmeņi, v.", ".*asmens"), // asmens, pārasmens
		SecondDecl.stdNomGen("-debess, dsk. -debeši, v.", ".*debess"), // padebess
		SecondDecl.stdNomGen("-rudens, dsk. -rudeņi, v.", ".*rudens"), // parudens
		SecondDecl.stdNomGen("-sāls, dsk. -sāļi, v.", ".*sāls"), // glaubersāls
		SecondDecl.stdNomGen("-uguns, dsk. -uguņi, v.", ".*uguns"), // uguns 2
		SecondDecl.stdNomGen("-ūdens, dsk. -ūdeņi, v.", ".*ūdens"), // notekūdens
		SecondDecl.stdNomGen("-zibens, dsk. -zibeņi, v.", ".*zibens"), // notekūdens
		SecondDecl.stdNomGen("-sāls, dsk. ģen. -sāļu, v.", ".*sāls"), // akmenssals
		SecondDecl.stdNomGen("-akmens, v.", ".*akmens"), // būvakmens
		SecondDecl.stdNomGen("-mēness, v.", ".*mēness"), // mēness, pilnmēness
		SecondDecl.stdNomGen("-sāls, v.", ".*sāls"), // gabalsāls
		SecondDecl.stdNomGen("-ūdens, v.", ".*ūdens"), // amonjakūdens
		SecondDecl.stdNomGen("-debess, v.", ".*debess"), // debess 2

		SecondDecl.std("-ača, v.", ".*acis"), // pieacis
		SecondDecl.std("-āķa, v.", ".*āķis"), // āķis
		SecondDecl.std("-aļņa, v.", ".*alnis"), // alnis
		SecondDecl.std("-āmja, v.", ".*āmis"), // āmis
		SecondDecl.std("-āža, v.", ".*āzis"), // āzis
		SecondDecl.std("-ciļņa, v.", ".*cilnis"), // cilnis
		SecondDecl.std("-eža, v.", ".*ezis"), // ezis
		SecondDecl.std("-naža, v.", ".*nazis"), // ēveļnazis
		SecondDecl.std("-oļa, v.", ".*olis"), // olis
		SecondDecl.std("-pūšļa, v.", ".*pūslis"), // pūslis
		SecondDecl.std("-pūžņa, v.", ".*pūznis"), // pūznis
		SecondDecl.std("-rešņa, v.", ".*resnis"), // resnis
		SecondDecl.std("-tešļa, v.", ".*teslis"), // teslis
		SecondDecl.std("-urbja, v.", ".*urbis"), // kloķurbis
		SecondDecl.std("-ūpja, v.", ".*ūpis"), // ūpis
		SecondDecl.std("-viļņa, v.", ".*vilnis"), // vilnis
		SecondDecl.std("-vižņa, v.", ".*viznis"), // viznis
		SecondDecl.std("-zižļa, v.", ".*zizlis"), // zizlis
		SecondDecl.std("-zuša, v.", ".*zutis"), // zutis
		SecondDecl.std("-zviļņa, v.", ".*zvilnis"), // zvilnis

		GenNoun.any("-suņa, v.", ".*suns", 5, null, new Tuple[]{TFeatures.GENDER__MASC}),

		GenNoun.any("-tēta, v.", ".*tētis", 48,
					null, new Tuple[]{TFeatures.GENDER__MASC}), // tētis

		GenNoun.any("-akmeņu, v.", ".*akmeņi", 4,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[] {Features.GENDER__MASC}), // būvakmeņi
	};
	/**
	 * Paradigm 6: Lietvārds 3. deklinācija -us
	 */
	public static final EndingRule[] thirdDeclNoun = {
			ThirdDecl.std("-alus, v.", ".*alus"), // alus
			ThirdDecl.std("-klepus, v.", ".*klepus"), // klepus
			ThirdDecl.std("-tirgus, v.", ".*tirgus"), // sīktirgus
			GenNoun.any("-dzirnu, s.", ".*dzirnus", 31,
					new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[]{TFeatures.GENDER__FEM}), // dzirnus
			GenNoun.any("-pelu, s.", ".*pelus", 31,
					new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[]{TFeatures.GENDER__FEM}), // pelus
			GenNoun.any("-ragu, s.", ".*ragus", 31,
					new Tuple[] {TFeatures.ENTRYWORD__PLURAL}, new Tuple[]{TFeatures.GENDER__FEM}), // ragus
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final EndingRule[] directFirstConjVerb = {
		// Pēteris solīja, ka tas strādās.
		VerbDoubleRule.of("-eju, -ej,", "-iet, pag. -gāju", "iet", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"iet\""), TFeatures.POS__IRREG_VERB, TFeatures.POS__DIRECT_VERB},
				null,
				FirstConjStems.of("ie", "ej", "gāj")), //apiet

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
		FirstConj.directHomof("-sīkstu, -sīksti,", "-sīkst, pag. -sīku", "sīkt",
				"\"sīkt\" (mazināties)"), //apsīkt
		FirstConj.directHomof("-sīcu, -sīc,", "-sīc, pag. -sīcu", "sīkt",
				"\"sīkt\" (kā odam)"), // sīkt, aizsīkt
		FirstConj.directHomof("-spriedzu, -spriedz,", "-spriedz, pag. -spriedzu", "spriegt",
				"\"spriegt\" (kādu citu)"), //nospriegt, saspriegt 1
		FirstConj.directHomof("-spriegstu, -spriegsti,", "-spriegst, pag. -spriegu", "spriegt",
				"\"spriegt\" (pašam)"), //saspriegt 2
		FirstConj.directHomof("-sprindzu, -sprindz,", "-sprindz, pag. -sprindzu", "springt",
				"\"springt\" (kādu citu)"), //saspringt 2
		FirstConj.directHomof("-springstu, -springsti,", "-springst, pag. -springu", "springt",
				"\"springt\" (pašam)"), //saspringt 1
		FirstConj.directHomof("-sūtu, -sūti,", "-sūt, pag. -sutu", "sust",
				"\"sust\" (mitrumā, karstumā)"), //apsust, nosust1
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
		FirstConj.directHomof("-vēršu, -vērs,", "-vērš, pag. -vērsu", "vērst",
				"\"vērst\" (mainīt virzienu)"), //aizvērst, izvērst 1
		FirstConj.directHomof("-vēršu, -vērt,", "-vērš, pag. -vērtu", "vērst",
				"\"vērst\" (mainīt būtību)"), //izvērst 2
		FirstConj.directHomof("-viešu, -vies,", "-vieš, pag. -viesu", "viest",
				"\"viest\" (būt par cēloni)"), //ieviest
		FirstConj.directHomof("-viežu, -vied,", "-viež, pag. -viedu", "viest",
				"\"viest\" (neskaidri just)"), //saviest 1

		// Izņēmuma izņēmums.
		VerbDoubleRule.of("-patīku, -patīc,", "-patīk, pag. -patiku", "patikt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"tikt\" (patikt kādam)"),
						TFeatures.INFINITIVE_HOMOFORMS, TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("tik", "tīk", "tik")), //patikt
		// TODO: sakārtot "riest" un "riesties"
		FirstConj.directAllPersParallelHomof(
				"-riešu, -ries, -rieš, pag. -rietu, arī -riesu", "riest", "\"riest\" (parastais)"), //ieriest 2

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

		// Izņēmums.
		VerbDoubleRule.of("-pārdodu, -pārdod,", "-pārdod, pag. -pārdevu", "dot", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"dot\""), TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("do", "dod", "dev")), //izpārdot
		VerbDoubleRule.of("-nesajēdzu, -nesajēdz,", "-nesajēdz, pag. -nesajēdzu", "jēgt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"jēgt\""), TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("jēg", "jēdz", "jēdz")), //nesajēgt
		VerbDoubleRule.of("-palieku, -paliec,", "-paliek, pag. -paliku", "likt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"likt\""), TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("lik", "liek", "lik")), //izpalikt
		VerbDoubleRule.of("-nespēju, -nespēj,", "-nespēj, pag. -nespēju", "nespēt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"spēt\""), TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("spē", "spēj", "spēj")), //nespēt
		VerbDoubleRule.of("-sastopu, -sastopi,", "-sastop, pag. -sastapu", "sastapt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"stapt\""), TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("stap", "stop", "stap")), //sastapt
		VerbDoubleRule.of("-sašūtu, -sašūti,", "-sašūt, pag. -sašutu", "sašust", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"šust\""), TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("šus", "šū", "šut")), //sašust
		VerbDoubleRule.of("-pazīstu, -pazīsti,", "-pazīst, pag. -pazinu", "zīt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"zīt\""), TFeatures.POS__DIRECT_VERB}, null,
				FirstConjStems.of("zī", "zīst", "zin")), //atpazīt

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
		FirstConj.direct("-bedu, -bed,", "-bed, pag. -bedu", "best"), //best 1
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
		FirstConj.direct("-deldzu, -deldz,", "-deldz, pag. -deldzu", "delgt"), //delgt
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
		FirstConj.direct("-gremžu, -gremz,", "-gremž, pag. -gremdu", "gremst"), //gremst
		FirstConj.direct("-gremžu, -gremz,", "-gremž, pag. -gremzu", "gremzt"), //gremzt
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
		FirstConj.direct("-irdzu, -irdz,", "-irdz, pag. -irdzu", "irgt"), //irgt
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
		FirstConj.direct("-karcu, -karc,", "-karc, pag. -karcu", "karkt"), //karkt
		FirstConj.direct("-karstu, -karsti,", "-karst, pag. -karsu", "karst"), //apkarst
		FirstConj.direct("-kašu, -kas,", "-kaš, pag. -kasu", "kast"), //iekast
		FirstConj.direct("-kaucu, -kauc,", "-kauc, pag. -kaucu", "kaukt"), //izkaukt, aizkaukt
		FirstConj.direct("-kauju, -kauj,", "-kauj, pag. -kāvu", "kaut"), //apkaut
		FirstConj.direct("-kāpju, -kāp,", "-kāpj, pag. -kāpu", "kāpt"), //aizkāpt
		FirstConj.direct("-kārcu, -kārc,", "-kārc, pag. -kārcu", "kārkt"), //kārkt
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
		FirstConj.direct("-mēdzu, -mēdz,", "-mēdz, pag. -mēdzu", "mēgt"), //mēgt
		FirstConj.direct("-mērcu, -mērc,", "-mērc, pag. -mērcu", "mērkt"), //iemērkt
		FirstConj.direct("-mēžu, -mēz,", "-mēž, pag. -mēzu", "mēzt"), //aizmēzt
		FirstConj.direct("-miedzu, -miedz,", "-miedz, pag. -miedzu", "miegt"), //aizmiegt
		FirstConj.direct("-miegu, -miedz,", "-mieg, pag. -migu", "migt"), //aizmigt
		FirstConj.direct("-mirkstu, -mirksti,", "-mirkst, pag. -mirku", "mirkt"), //imirkt
		FirstConj.direct("-mirstu, -mirsti,", "-mirst, pag. -mirsu", "mirst"), //aizmirst
		FirstConj.direct("-mirstu, -mirsti,", "-mirst, pag. -miru", "mirt"), //nomirt
		FirstConj.direct("-mīžu, -mīz,", "-mīž, pag. -mīzu", "mīzt"), //apmīzt
		FirstConj.direct("-mostu, -mosti,", "-most, pag. -modu", "most"), //most
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
		FirstConj.direct("-piržu, -pird,", "-pirž, pag. -pirdu", "pirst"), //aizpirst
		FirstConj.direct("-pišu, -pis,", "-piš, pag. -pisu", "pist"), //izpist
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
		FirstConj.direct("-slempju, -slemp,", "-slempj, pag. -slempu", "slempt"), //slempt
		FirstConj.direct("-slēdzu, -slēdz,", "-slēdz, pag. -slēdzu", "slēgt"), //aizslēgt
		FirstConj.direct("-slēpju, -slēp,", "-slēpj, pag. -slēpu", "slēpt"), //aizslēpt
		FirstConj.direct("-sliecu, -sliec,", "-sliec, pag. -sliecu", "sliekt"), //nosliekt
		FirstConj.direct("-sliepju, -sliep,", "-sliepj, pag. -sliepu", "sliept"), //sliept
		FirstConj.direct("-slimstu, -slimsti,", "-slimst, pag. -slimu", "slimt"), //apslimt
		FirstConj.direct("-slīgstu, -slīgsti,", "-slīgst, pag. -slīgu", "slīgt"), //aizslīgt
		FirstConj.direct("-slīkstu, -slīksti,", "-slīkst, pag. -slīku", "slīkt"), //ieslīkt
		FirstConj.direct("-sliju, -slij,", "-slij, pag. -sliju", "slīt"), //ieslīt
		FirstConj.direct("-smagstu, -smagsti,", "-smagst, pag. -smagu", "smagt"), //sasmagt
		FirstConj.direct("-smoku, -smoc,", "-smok, pag. -smaku", "smakt"), //aizsmakt
		FirstConj.direct("-smeļu, -smel,", "-smeļ, pag. -smēlu", "smelt"), //apsmelt
		FirstConj.direct("-smeju, -smej,", "-smej, pag. -smēju", "smiet"), //apsmiet
		FirstConj.direct("-snaužu, -snaud,", "-snauž, pag. -snaudu", "snaust"), //aizsnaust
		FirstConj.direct("-snāju, -snāj,", "-snāj, pag. -snāju", "snāt"), //snāt
		FirstConj.direct("-sniedzu, -sniedz,", "-sniedz, pag. -sniedzu", "sniegt"), //aizsniegt
		FirstConj.direct("-sniecu, -sniec,", "-sniec, pag. -sniecu", "sniekt"), //sniekt
		FirstConj.direct("-sniegu, -sniedz,", "-snieg, pag. -snigu", "snigt"), //apsnigt
		FirstConj.direct("-speru, -sper,", "-sper, pag. -spēru", "spert"), //aizspert
		FirstConj.direct("-spēju, -spēj,", "-spēj, pag. -spēju", "spēt"), //aizspēt
		FirstConj.direct("-spiedzu, -spiedz,", "-spiedz, pag. -spiedzu", "spiegt"), //atspiegt
		FirstConj.direct("-spiežu, -spied,", "-spiež, pag. -spiedu", "spiest"), //aizspiest
		FirstConj.direct("-spindzu, -spindz,", "-spindz, pag. -spindzu", "spingt"), //spingt, nospingt
		FirstConj.direct("-spirgstu, -spirgsti,", "-spirgst, pag. -spirgu", "spirgt"), //atspirgt
		FirstConj.direct("-spļauju, -spļauj,", "-spļauj, pag. -spļāvu", "spļaut"), //aizspļaut
		FirstConj.direct("-spraucu, -sprauc,", "-sprauc, pag. -spraucu", "spraukt"), //iespraukt
		FirstConj.direct("-spraužu, -spraud,", "-sprauž, pag. -spraudu", "spraust"), //aizspraust
		FirstConj.direct("-sprāgstu, -sprāgsti,", "-sprāgst, pag. -sprāgu", "sprāgt"), //atsprāgt
		FirstConj.direct("-sprēžu, -sprēd,", "-sprēž, pag. -sprēdu", "sprēst"), //sprēst
		FirstConj.direct("-spriežu, -spried,", "-spriež, pag. -spriedu", "spriest"), //apspriest
		FirstConj.direct("-sprūku, -sprūc,", "-sprūk, pag. -spruku", "sprukt"), //aizsprukt
		FirstConj.direct("-sprūstu, -sprūsti,", "-sprūst, pag. -sprūdu", "sprūst"), //iesprūst
		FirstConj.direct("-spurcu, -spurc,", "-spurc, pag. -spurcu", "spurkt"), //nospurkt
		FirstConj.direct("-stāju, -stāj,", "-stāj, pag. -stāju", "stāt"), //aizstāt
		FirstConj.direct("-steidzu, -steidz,", "-steidz, pag. -steidzu", "steigt"), //aizsteigt
		FirstConj.direct("-stiepju, -stiep,", "-stiepj, pag. -stiepu", "stiept"), //aizstiept
		FirstConj.direct("-stiegu, -stiedz,", "-stieg, pag. -stigu", "stigt"), //iestigt
		FirstConj.direct("-stilstu, -stilsti,", "-stilst, pag. -stilu", "stilt"), //stilt
		FirstConj.direct("-stingstu, -stingsti,", "-stingst, pag. -stingu", "stingt"), //sastingt
		FirstConj.direct("-strebju, -streb,", "-strebj, pag. -strēbu", "strēbt"), //apstrēbt
		FirstConj.direct("-striegu, -striedz,", "-strieg, pag. -strigu", "strigt"), //iestrigt
		FirstConj.direct("-stulbstu, -stulbsti,", "-stulbst, pag. -stulbu", "stulbt"), //stulbt
		FirstConj.direct("-stumju, -stum,", "-stumj, pag. -stūmu", "stumt"), //aizstumt
		FirstConj.direct("-sūcu, -sūc,", "-sūc, pag. -sūcu", "sūkt"), //iesūkt
		FirstConj.direct("-sveicu, -sveic,", "-sveic, pag. -sveicu", "sveikt"), //apsveikt
		FirstConj.direct("-svelpju, -svelp,", "-svelpj, pag. -svelpu", "svelpt"), //iesvelpt
		FirstConj.direct("-sveļu, -svel,", "-sveļ, pag. -svēlu", "svelt"), //svelt, iesvelt
		FirstConj.direct("-svempju, -svemp,", "-svempj, pag. -svempu", "svempt"), //svempt
		FirstConj.direct("-sveru, -sver,", "-sver, pag. -svēru", "svērt"), //apsvērt
		FirstConj.direct("-sviežu, -svied,", "-sviež, pag. -sviedu", "sviest"), //aizsviest
		FirstConj.direct("-svilpju, -svilp,", "-svilpj, pag. -svilpu", "svilpt"), //atsvilpt
		FirstConj.direct("-svilstu, -svilsti,", "-svilst, pag. -svilu", "svilt"), //aizsvilt
		FirstConj.direct("-svīstu, -svīsti,", "-svīst, pag. -svīdu", "svīst"), //iesvīst
		// Š
		FirstConj.direct("-šalcu, -šalc,", "-šalc, pag. -šalcu", "šalkt"), //pašalkt, aizšalkt
		FirstConj.direct("-šaušu, -šaut,", "-šauš, pag. -šautu", "šaust"), //šaust
		FirstConj.direct("-šauju, -šauj,", "-šauj, pag. -šāvu", "šaut"), //aizšaut
		FirstConj.direct("-šķeļu, -šķel,", "-šķeļ, pag. -šķēlu", "šķelt"), //aizšķelt
		FirstConj.direct("-šķēržu, -šķērd,", "-šķērž, pag. -šķērdu", "šķērst"), //izšķērst
		FirstConj.direct("-šķiebju, -šķieb,", "-šķiebj, pag. -šķiebu", "šķiebt"), //izšķiebt
		FirstConj.direct("-šķiežu, -šķied,", "-šķiež, pag. -šķiedu", "šķiest"), //aizšķiest
		FirstConj.direct("-šķiļu, -šķil,", "-šķiļ, pag. -šķīlu", "šķilt"), //aizšķilt
		FirstConj.direct("-šķiru, -šķir,", "-šķir, pag. -šķīru", "šķirt"), //aizšķirt
		FirstConj.direct("-šķietu, -šķieti,", "-šķiet, pag. -šķitu", "šķist"), //šķist
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
		FirstConj.direct("-šņurkstu, -šņurksti,", "-šņurkst, pag. -šņurku", "šņurkt"), //apšņurkt, sašņurkt
		FirstConj.direct("-šņūcu, -šņūc,", "-šņūc, pag. -šņūcu", "šņūkt"), //iešņūkt
		FirstConj.direct("-šuju, -šuj,", "-šuj, pag. -šuvu", "šūt"), //aizšūt
		// T
		FirstConj.direct("-topu, -topi,", "-top, pag. -tapu", "tapt"), //attapt
		FirstConj.direct("-teicu, -teic,", "-teic, pag. -teicu", "teikt"), //uzteikt
		FirstConj.direct("-tempju, -temp,", "-tempj, pag. -tempu", "tempt"), //iztempt
		FirstConj.direct("-tešu, -tes,", "-teš, pag. -tesu", "test"), //test
		FirstConj.direct("-tērpju, -tērp,", "-tērpj, pag. -tērpu", "tērpt"), //aptērpt
		FirstConj.direct("-tēšu, -tēs,", "-tēš, pag. -tēsu", "tēst"), //aptēst
		FirstConj.direct("-tiepju, -tiep,", "-tiepj, pag. -tiepu", "tiept"), //uztiept
		FirstConj.direct("-tilpstu, -tilpsti,", "-tilpst, pag. -tilpu", "tilpt"), //satilpt
		FirstConj.direct("-tinu, -tin,", "-tin, pag. -tinu", "tīt"), //aiztīt
		FirstConj.direct("-tošu, -tos,", "-tos, pag. -tosu", "tost"), //tost
		FirstConj.direct("-traucu, -trauc,", "-trauc, pag. -traucu", "traukt"), //aiztraukt
		FirstConj.direct("-traušu, -traus,", "-trauš, pag. -trausu", "traust"), //notraust
		FirstConj.direct("-trencu, -trenc,", "-trenc, pag. -trencu", "trenkt"), //aiztrenkt
		FirstConj.direct("-triecu, -triec,", "-triec, pag. -triecu", "triekt"), //aiztriekt
		FirstConj.direct("-triepju, -triep,", "-triepj, pag. -triepu", "triept"), //aiztriept
		FirstConj.direct("-trinu, -trin,", "-trin, pag. -trinu", "trīt"), //ietrīt
		FirstConj.direct("-trūkstu, -trūksti,", "-trūkst, pag. -trūku", "trūkt"), //iztrūkt
		FirstConj.direct("-tumstu, -tumsti,", "-tumst, pag. -tumsu", "tumst"), //pietumst
		FirstConj.direct("-tupstu, -tupsti,", "-tupst, pag. -tupu", "tupt"), //aiztupt
		FirstConj.direct("-tušu, -tus,", "-tuš, pag. -tušu", "tust"), //tust
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
		FirstConj.direct("-žurbstu, -žurbsti,", "-žurbst, pag. -žurbu", "žurbt"), // apžurbt
		FirstConj.direct("-žūstu, -žūsti,", "-žūst, pag. -žuvu", "žūt"), // apžūt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas.
		// Ar paralēlformām.
		// TODO: sakārtot "riest" un "riesties"
		FirstConj.direct3PersParallelHomof(
				"-rieš, pag. -rieta, arī -riesa", "riest", "\"riest\" (parastais)"), // aizriest
		// Bez paralēlformām.
		FirstConj.direct3PersHomof("-aust, pag. -ausa", "aust", "\"aust\" (gaismai)"), //aizaust 1
		FirstConj.direct3PersHomof("-dzīst, pag. -dzija", "dzīt", "\"dzīt\" (ievainojumam)"), //aizdzīt 2
		FirstConj.direct3PersHomof("-irst, pag. -ira", "irt", "\"irt\" (audumam)"), //irt 2
		// TODO: sakārtot "riest" un "riesties"
		FirstConj.direct3PersHomof("-rieš, pag. -rieta", "riest", "\"riest\" (parastais)"), //ieriest 1
		FirstConj.direct3PersHomof("-riest, pag. -rieta", "riest", "\"riest\" (izņēmums)"), //pieriest 1
		FirstConj.direct3PersHomof("-sus, pag. -susa", "sust", "\"sust\" (žūt)"), //nosust 2

		// Paralēlformas
		FirstConj.direct3PersParallel(
				"-glumst, arī -glum, pag. -gluma", "glumt"), // pieglumt
		FirstConj.direct3PersParallel(
				"-kaist, pag. -kaisa, arī -kaita", "kaist"), // pārkaist
		FirstConj.direct3PersParallel(
				"-kūp, arī -kūpst, pag. -kūpa", "kūpt"), // kūpt
				// TODO pārbaudīt, vai ir pieņemami, ka abas homoformas ir kopā
		FirstConj.direct3PersParallel(
				"-stīdz, pag. -stīdza, retāk -stīgst, pag. -stīga", "stīgt"), // iestīgt
		FirstConj.direct3PersParallel(
				"-springst, arī -sprindz, pag. -springa, arī -sprindza", "springt"), // atspringt
		FirstConj.direct3PersParallel(
				"-spuldz, arī -spulgst, pag. -spuldza, arī -spulga", "spulgt"), // atspulgt
		FirstConj.direct3PersParallel(
				"-spurst, pag. -spura, arī -spūra", "spurt"), // apspurt
		FirstConj.direct3PersParallel(
				"-strēdz, arī -strēgst, pag. -strēdza, arī -strēga", "strēgt"), // piestrēgt

		// Standartizētie.
		// A, B
		FirstConj.direct3Pers("-birst, pag. -birza", "birzt"), //izbirzt
		FirstConj.direct3Pers("-blēj, pag. -blēja", "blēt"), //atblēt
		FirstConj.direct3Pers("-bož, pag. -boza", "bozt"), //izbozt
		// C, D
		FirstConj.direct3Pers("-derdz, pag. -derdza", "dergt"), //dergt
		FirstConj.direct3Pers("-dim, pag. -dima", "dimt"), //aizdimt
		FirstConj.direct3Pers("-dip, pag. -dipa", "dipt"), //aizdipt
		FirstConj.direct3Pers("-dīgst, pag. -dīga", "dīgt"), //apdīgt
		FirstConj.direct3Pers("-drūp, pag. -drupa", "drupt"), //apdrupt
		FirstConj.direct3Pers("-dub, pag. -duba", "dubt"), //iedubt
		// E, F, G
		FirstConj.direct3Pers("-galdz, pag. -galdza", "galgt"), //galgt
		FirstConj.direct3Pers("-glumst, pag. -gluma", "glumt"), //saglumt
		FirstConj.direct3Pers("-grauž, pag. -grauda", "graust"), //graust
		FirstConj.direct3Pers("-guldz, pag. -guldza", "gulgt"), //aizgulgt
		// H, I
		FirstConj.direct3Pers("-ilgst, pag. -ilga", "ilgt"), //ieilgt
		// J, K
		FirstConj.direct3Pers("-knieš, pag. -kniesa", "kniest"), //nokniest
		FirstConj.direct3Pers("-knīt, pag. -knita", "knist"), //izknist
		FirstConj.direct3Pers("-krauc, pag. -krauca", "kraukt"), //pakraukt
		FirstConj.direct3Pers("-kviec, pag. -kvieca", "kviekt"), //aizkviekt
		FirstConj.direct3Pers("-kupst, pag. -kupa", "kupt"), //sakupt
		FirstConj.direct3Pers("-kurc, pag. -kurca", "kurkt"), //kurkt
		// L
		FirstConj.direct3Pers("-lūp, pag. -lupa", "lupt"), //aplupt
		// Ļ
		FirstConj.direct3Pers("-ļekst, pag. -ļeka", "ļekt"), //noļekt
		FirstConj.direct3Pers("-ļūk, pag. -ļuka", "ļukt"), //atļukt
		FirstConj.direct3Pers("-ļurkst, pag. -ļurka", "ļurkt"), //izļurkt
		// M
		FirstConj.direct3Pers("-milst, pag. -milza", "milzt"), //aizmilzt
		FirstConj.direct3Pers("-mēj, pag. -mēja", "mēt"), //mēt
		// N
		FirstConj.direct3Pers("-nērš, pag. -nērsa", "nērst"), //iznērst
		// Ņ
		FirstConj.direct3Pers("-ņirb, pag. -ņirba", "ņirbt"), //aizņirbt
		// O, P
		FirstConj.direct3Pers("-paist, pag. -paisa", "paist"), //paist
		FirstConj.direct3Pers("-palst, pag. -palsa", "palst"), //nopalst
		FirstConj.direct3Pers("-pilst, pag. -pila", "pilt"), //pilt
		// R
		FirstConj.direct3Pers("-ris, pag. -risa", "rist"), //aprist
		// S
		FirstConj.direct3Pers("-smog, pag. -smaga", "smagt"), //piesmagt
		FirstConj.direct3Pers("-smeldz, pag. -smeldza", "smelgt"), //aizsmelgt
		FirstConj.direct3Pers("-smirkst, pag. -smirka", "smirkt"), //piesmirkt
		FirstConj.direct3Pers("-spilgst, pag. -spilga", "spilgt"), //spilgt
		FirstConj.direct3Pers("-spurdz, pag. -spurdza", "spurgt"), //pārspurgt
		FirstConj.direct3Pers("-svēpst, pag. -svēpa", "svēpt"), //piesvēpt
		// Š
		FirstConj.direct3Pers("-šļirc, pag. -šļirca", "šļirkt"), //iešļirkt
		// T
		FirstConj.direct3Pers("-tirpst, pag. -tirpa", "tirpt"), //attirpt
		FirstConj.direct3Pers("-trup, pag. -trupa", "trupt"), //aptrupt
		FirstConj.direct3Pers("-trus, pag. -trusa", "trust"), //aptrust
		// U
		FirstConj.direct3Pers("-urdz, pag. -urdza", "urgt"), //aizurgt
		// V
		FirstConj.direct3Pers("-veldz, pag. -veldza", "velgt"), //velgt
		// Z
		FirstConj.direct3Pers("-zilgst, pag. -zilga", "zilgt"), //zilgt
		FirstConj.direct3Pers("-zilst, pag. -zila", "zilt"), //apzilt
		// Ž
		FirstConj.direct3Pers("-žulgst, pag. -žulga", "žulgt"), //izžulgt

		// Daudzskaitļa formu likumi.
		FirstConj.directPluralHomof("-lienam, -lienat, -lien, pag. -līdām", "līst", "\"līst\" (zem galda)"), // salīst

		FirstConj.directPluralAllPersParallel(
				"-sēžam, -sēžat, -sēž, arī -sēstam, -sēstat, -sēst, pag. -sēdām", "sēst"), // sasēst
		// Ar trešo personu
		FirstConj.directPluralOr3Pers(
				"-klīstam, pag. -klīdām vai vsk. 3. pers.", "-klīst, pag. -klīda", "klīst"), // izklīst
		FirstConj.directPluralOr3Pers(
				"-mirstam, pag. -mirām vai vsk. 3. pers.,", "-mirst, pag. -mira", "mirt"), // izmirt
		// Bez 3. personas
		FirstConj.directPlural("-bēgam, -bēgat, -bēg, pag. -bēgām", "bēgt"), // sabēgt
		FirstConj.directPlural("-lecam, -lecat, -lec, pag. -lēcām", "lēkt"), // salēkt
		FirstConj.directPlural("-mūkam, -mūkat, -mūk, pag. -mukām", "mukt"), // samukt
		FirstConj.directPlural("-sprūkam, -sprūkat, -sprūk, pag. -sprukām", "sprukt"), // sasprukt
		FirstConj.directPlural("-sprūstam, -sprūstat, -sprūst, pag. -sprūdām", "sprūst"), // sasprūst
		FirstConj.directPlural("-spurdzam, -spurdzat, -spurdz, pag. -spurdzām", "spurgt"), // saspurgt
		FirstConj.directPlural("-tupstam, -tupstat, -tupst, pag. -tupām", "tupt"), // satupt

		// Pilnīgs nestandarts.
		VerbDoubleRule.of(
				"-teicu, -teic,", "-teic (tagadnes formas parasti nelieto), pag. -teicu", "teikt", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"teikt\""), TFeatures.POS__DIRECT_VERB},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NO_PRESENT)},
				FirstConjStems.of("teik", "teic", "teic")), //atteikt
		VerbDoubleRule.of("parasti pag., -sēdu, -sēdi, -sēda", null, "sēst", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"sēst\""), TFeatures.POS__DIRECT_VERB},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PAST)},
				FirstConjStems.of("sēs", null, "sēd")), // apsēst

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final EndingRule[] directSecondConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		SecondConj.directAllPersParallel(
				"-skandēju, -skandē, -skandē, pag. -skandēju, -skandēji, -skandēja (retāk -skanda, 1. konj.)",
				"skandēt"), // noskandēt

		// Standartīgie.
		SecondConj.direct("-dabūju, -dabū,", "-dabū, pag. -dabūju", "dabūt"), //aizdabūt
		SecondConj.direct("-ecēju, -ecē,", "-ecē, pag. -ecēju", "ecēt"), //ecēt
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
		SecondConj.direct3Pers("-knābā, pag. -knābāja", "knābāt"), //pieknābāt
		SecondConj.direct3Pers("-kurtē, pag. -kurtēja", "kurtēt"), //apkurtēt
		SecondConj.direct3Pers("-kūko, pag. -kūkoja", "kūkot"), //aizkūkot
		SecondConj.direct3Pers("-līņā, pag. -līņāja", "līņāt"), //uzlīņāt
		SecondConj.direct3Pers("-mirgo, pag. -mirgoja", "mirgot"), //aizmirgot
		SecondConj.direct3Pers("-piepē, pag. -piepēja", "piepēt"), //nopiepēt
		SecondConj.direct3Pers("-plīvo, pag. -plīvoja", "plīvot"), //noplīvot
		SecondConj.direct3Pers("-sērē, pag. -sērēja", "sērēt"), //aizsērēt
		SecondConj.direct3Pers("-skānē, pag. -skānēja", "skānēt"), //skānēt
		SecondConj.direct3Pers("-smadzē, pag. -smadzēja", "smadzēt"), //piesmadzēt
		SecondConj.direct3Pers("-šalko, pag. -šalkoja", "šalkot"), // iešalkot
		SecondConj.direct3Pers("-tumē, pag. -tumēja", "tumēt"), // satumēt
		SecondConj.direct3Pers("-vilnī, pag. -vilnīja", "vilnīt"), // vilnīt
		SecondConj.direct3Pers("-vižņo, pag. -vižņoja", "vižņot"), // savižņot
		SecondConj.direct3Pers("-zilē, pag. -zilēja", "zilēt"), // iezilēt

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final EndingRule[] directThirdConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		ThirdConj.directOptChangeAllPersParallel(
				"-ļogu, -ļogi, -ļoga, retāk -ļodzu, -ļodzi, -ļodza, pag. -ļodzīju", "ļodzīt"), //paļodzīt
		ThirdConj.directOptChangeAllPersParallel(
				"-moku, -moki, -moka, arī -mocu, -moci, -moca, pag. -mocīju", "mocīt"), //aizmocīt
		ThirdConj.directOptChangeAllPersParallel(
				"-murcu, -murci, -murca, retāk -murku, -murki, -murka, pag. -murcīju", "murcīt"), //apmurcīt
		ThirdConj.directOptChangeAllPersParallel(
				"-ņurcu, -ņurci, -ņurca, retāk -ņurku, -ņurki, -ņurka, pag. -ņurcīju", "ņurcīt"), //apmurcīt
		ThirdConj.directOptChangeAllPersParallel(
				"-sēžu, -sēdi, -sēž, arī -sēdu, -sēdi, -sēd, pag. -sēdēju", "sēdēt"), //iesēdēt

		ThirdConj.directStdAllPersParallel(
				"-dzirdu, -dzirdi, -dzird, pag. -dzirdu (1. konj.), arī -dzirdēju", "dzirdēt"), //izdzirdēt
		ThirdConj.directStdAllPersParallel(
				"-dzirdu, -dzirdi, -dzird, pag. -dzirdēju, arī -dzirdu (1. konj.)", "dzirdēt"), //padzirdēt
		ThirdConj.directStdAllPersParallel(
				"-slīdu, -slīdi, -slīd, pag. -slīdēju, -slīdēji, -slīdēja (retāk -slīda, 1. konj.)", "slīdēt"), // aizslīdēt
		ThirdConj.directStdAllPersParallel(
				"-smirdu, -smirdi, -smird, pag. -smirdēju (3. pers. arī -smirda, 1. konj.)", "smirdēt"), // nosmirdēt
		ThirdConj.directStdAllPersParallel(
				"-spīdu, -spīdi, -spīd, pag. -spīdēju, -spīdēji, -spīdēja (retāk -spīda, 1. konj.)", "spīdēt"), // paspīdēt
		ThirdConj.directStdAllPersParallel(
				"-vīdu, -vīdi, -vīd, pag. -vīdēju (3. pers. retāk -vīda, 1. konj.)", "vīdēt"), //novīdēt
		ThirdConj.directStdAllPersParallel(
				"-vīdu, -vīdi, -vīd, pag. -vīdēju (retāk -vīdu, 1. konj.)", "vīdēt"), // pavīdēt

			ThirdConj.directOptChangeAllPersParallel(
				"-guļu, -guli, -guļ (arī -gul), pag. -gulēju", "gulēt"), // iegulēt
			//TODO kā norādīt miju + ko darīt ar otru, standartizēto gulēt?

		// Izņēmums.
		ThirdConj.directStdAllPersParallel(
				"-zinu, -zini, -zina (retāk -zin), pag. -zināju", "zināt"), // zināt

		// Standartizētie ar miju.
		ThirdConj.directChange("-brauku, -brauki,", "-brauka, pag. -braucīju", "braucīt"), //apbraucīt
		ThirdConj.directChange("-loku, -loki,", "-loka, pag. -locīju", "locīt"), //aizlocīt
		ThirdConj.directChange("-ļogu, -ļogi,", "-ļoga, pag. -ļodzīju", "ļodzīt"), //izļodzīt
			//TODO šis ir izņēmums
		ThirdConj.directChange("-māku, -māki,", "-māk, pag. -mācēju", "mācēt"), //mācēt
		ThirdConj.directChange("-raugu, -raugi,", "-rauga, pag. -raudzīju", "raudzīt"), //apraudāt
		ThirdConj.directChange("-saku, -saki,", "-saka, pag. -sacīju", "sacīt"), //atsacīt
		ThirdConj.directChange("-sēžu, -sēdi,", "-sēž, pag. -sēdēju", "sēdēt"), //atsēdēt
		ThirdConj.directChange("-slaku, -slaki,", "-slaka, pag. -slacīju", "slacīt"), //aizslacīt
		ThirdConj.directChange("-slauku, -slauki,", "-slauka, pag. -slaucīju", "slaucīt"), //aizslaucīt
		ThirdConj.directChange("-slogu, -slogi,", "-sloga, pag. -slodzīju", "slodzīt"), //ieslodzīt
		ThirdConj.directChange("-šļauku, -šļauki,", "-šļauka, pag. -šļaucīju", "šļaucīt"), //atšļupstēt
		ThirdConj.directChange("-teku, -teci,", "-tek, pag. -tecēju", "tecēt"), //aiztecēt
		ThirdConj.directChange("-žņaugu, -žņaugi,", "-žņauga, pag. -žņaudzīju", "žņaudzīt"), //izžņaudzīt

		// Standartizētie bez mijas.
		// A, B
		ThirdConj.directStd("-burkšu, -burkši,", "-burkš, pag. -burkšēju", "burkšēt"), //izburkšēt
		ThirdConj.directStd("-burkšķu, -burkšķi,", "-burkšķ, pag. -burkšķēju", "burkšķēt"), //izburkšķēt
		// C
		ThirdConj.directStd("-ceru, -ceri,", "-cer, pag. -cerēju", "cerēt"), //iecerēt
		ThirdConj.directStd("-ciepstu, -ciepsti,", "-ciepst, pag. -ciepstēju", "ciepstēt"), //ciepstēt
		// Č
		ThirdConj.directStd("-čabu, -čabi,", "-čab, pag. -čabēju", "čabēt"), //čabēt, aizčabēt
		ThirdConj.directStd("-čaukstu, -čauksti,", "-čaukst, pag. -čaukstēju", "čaukstēt"), //čaukstēt, aizčaukstēt
		ThirdConj.directStd("-čākstu, -čāksti,", "-čākst, pag. -čākstēju", "čākstēt"), //sačākstēt
		ThirdConj.directStd("-čerkstu, -čerksti,", "-čerkst, pag. -čerkstēju", "čerkstēt"), //nočerkstēt
		ThirdConj.directStd("-čērkstu, -čērksti,", "-čērkst, pag. -čērkstēju", "čērkstēt"), //nočērkstēt
		ThirdConj.directStd("-čiepstu, -čiepsti,", "-čiepst, pag. -čiepstēju", "čiepstēt"), //iečiepstēt
		ThirdConj.directStd("-činkstu, -činksti,", "-činkst, pag. -činkstēju", "činkstēt"), //izčinkstēt
		ThirdConj.directStd("-čīkstu, -čīksti,", "-čīkst, pag. -čīkstēju", "čīkstēt"), //izčīkstēt
		ThirdConj.directStd("-čuču, -čuči,", "-čuč, pag. -čučēju", "čučēt"), //pačučēt
		ThirdConj.directStd("-čukstu, -čuksti,", "-čukst, pag. -čukstēju", "čukstēt"), //atčukstēt
		ThirdConj.directStd("-čurnu, -čurni,", "-čurn, pag. -čurnēju", "čurnēt"), //nočurnēt
		// D
		ThirdConj.directStd("-deru, -deri,", "-der, pag. -derēju", "derēt"), //noderēt 1, 2
		ThirdConj.directStd("-dēdu, -dēdi,", "-dēd, pag. -dēdēju", "dēdēt"), //izdēdēt
		ThirdConj.directStd("-dienu, -dieni,", "-dien, pag. -dienēju", "dienēt"), //atdienēt
		ThirdConj.directStd("-dirnu, -dirni,", "-dirn, pag. -dirnēju", "dirnēt"), //nodirnēt
		ThirdConj.directStd("-draudu, -draudi,", "-draud, pag. -draudēju", "draudēt"), //apdraudēt
		ThirdConj.directStd("-drebu, -drebi,", "-dreb, pag. -drebēju", "drebēt"), //nodrebēt
		ThirdConj.directStd("-drīkstu, -drīksti,", "-drīkst, pag. -drīkstēju", "drīkstēt"), //uzdrīkstēt
		ThirdConj.directStd("-dusu, -dusi,", "-dus, pag. -dusēju", "dusēt"), //atdusēt
		ThirdConj.directStd("-dziedu, -dziedi,", "-dzied, pag. -dziedāju", "dziedāt"), //aizdziedāt
		ThirdConj.directStd("-dzirdu, -dzirdi,", "-dzird, pag. -dzirdēju", "dzirdēt"), //sadzirdēt
		ThirdConj.directStd("-džinkstu, -džinksti,", "-džinkst, pag. -džinkstēju", "džinkstēt"), //atdžinkstēt
		// E, F, G
		ThirdConj.directStd("-gārkstu, -gārksti,", "-gārkst, pag. -gārkstēju", "gārkstēt"), //gārkstēt
		ThirdConj.directStd("-glūnu, -glūni,", "-glūn, pag. -glūnēju", "glūnēt"), //apglūnēt
		ThirdConj.directStd("-grabu, -grabi,", "-grab, pag. -grabēju", "grabēt"), //aizgrabēt
		ThirdConj.directStd("-gribu, -gribi,", "-grib, pag. -gribēju", "gribēt"), //iegribēt
		ThirdConj.directStd("-guļu, -guli,", "-guļ, pag. -gulēju", "gulēt"), //aizgulēt
		// Ģ
		ThirdConj.directStd("-paģēru, -paģēri,", "-paģēr, pag. -paģērēju", "paģērēt"), //paģērēt
		// H, I, Ī
		ThirdConj.directStd("-īdu, -īdi,", "-īd, pag. -īdēju", "īdēt"), //noīdēt
		// J, K
		ThirdConj.directStd("-karkšu, -karkši,", "-karkš, pag. -karkšēju", "karkšēt"), //karkšēt
		ThirdConj.directStd("-karkšķu, -karkšķi,", "-karkšķ, pag. -karkšķēju", "karkšķēt"), //karkšķēt
		ThirdConj.directStd("-kārkšu, -kārkši,", "-kārkš, pag. -kārkšēju", "kārkšēt"), //kārkšēt
		ThirdConj.directStd("-kārkšķu, -kārkšķi,", "-kārkšķ, pag. -kārkšķēju", "kārkšķēt"), //kārkšķēt
		ThirdConj.directStd("-kārnu, -kārni,", "-kārn, pag. -kārnēju", "kārnēt"), //kārnēt
		ThirdConj.directStd("-klabu, -klabi,", "-klab, pag. -klabēju", "klabēt"), //paklabēt, aizklabēt
		ThirdConj.directStd("-klaudzu, -klaudzi,", "-klaudz, pag. -klaudzēju", "klaudzēt"), //klaudzet, aizklaudzēt
		ThirdConj.directStd("-klimstu, -klimsti,", "-klimst, pag. -klimstēju", "klimstēt"), //aizklimstēt
		ThirdConj.directStd("-knukstu, -knuksti,", "-knukst, pag. -knukstēju", "knukstēt"), //knukstēt
		ThirdConj.directStd("-krekstu, -kreksti,", "-krekst, pag. -krekstēju", "krekstēt"), //nokrekstēt
		ThirdConj.directStd("-krekšu, -krekši,", "-krekš, pag. -krekšēju", "krekšēt"), //nokrekšēt
		ThirdConj.directStd("-krekšķu, -krekšķi,", "-krekšķ, pag. -krekšķēju", "krekšķēt"), //nokrekšķēt
		ThirdConj.directStd("-kukstu, -kuksti,", "-kukst, pag. -kukstēju", "kukstēt"), //kukstēt
		ThirdConj.directStd("-kunkstu, -kunksti,", "-kunkst, pag. -kunkstēju", "kunkstēt"), //izkunkstēt
		ThirdConj.directStd("-kurnu, -kurni,", "-kurn, pag. -kurnēju", "kurnēt"), //pakurnēt
		ThirdConj.directStd("-kustu, -kusti,", "-kust, pag. -kustēju", "kustēt"), //aizkustēt
		ThirdConj.directStd("-kūpu, -kūpi,", "-kūp, pag. -kūpēju", "kūpēt"), //apkūpēt, aizkūpēt
		ThirdConj.directStd("-kvernu, -kverni,", "-kvern, pag. -kvernēju", "kvernēt"), //nokvernēt
		ThirdConj.directStd("-kvēlu, -kvēli,", "-kvēl, pag. -kvēlēju", "kvēlēt"), //izkvēlēt
		// Ķ
		ThirdConj.directStd("-ķerkstu, -ķerksti,", "-ķerkst, pag. -ķerkstēju", "ķerkstēt"), //ķerkstēt
		ThirdConj.directStd("-ķērkstu, -ķērksti,", "-ķērkst, pag. -ķērkstēju", "ķērkstēt"), //ķērkstēt
		// L
		ThirdConj.directStd("-larkšu, -larkši,", "-larkš, pag. -larkšēju", "larkšēt"), //larkšēt
		ThirdConj.directStd("-larkšķu, -larkšķi,", "-larkšķ, pag. -larkšķēju", "larkšķēt"), //larkšķēt
		ThirdConj.directStd("-lādu, -lādi,", "-lād, pag. -lādēju", "lādēt"), //nolādēt
		ThirdConj.directStd("-līdzu, -līdzi,", "-līdz, pag. -līdzēju", "līdzēt"), //izlīdzēt
		ThirdConj.directStd("-palīdzu, -palīdzi,", "-palīdz, pag. -palīdzēju", "palīdzēt"), //izpalīdzēt
		ThirdConj.directStd("-lūru, -lūri,", "-lūr, pag. -lūrēju", "lūrēt"), //aplūrēt
		// Ļ
		ThirdConj.directStd("-ļerkstu, -ļerksti,", "-ļerkst, pag. -ļerkstēju", "ļerkstēt"), //ļerkstēt
		ThirdConj.directStd("-ļerkšu, -ļerkši,", "-ļerkš, pag. -ļerkšēju", "ļerkšēt"), //ļerkšēt
		ThirdConj.directStd("-ļerkšķu, -ļerkšķi,", "-ļerkšķ, pag. -ļerkšķēju", "ļerkšķēt"), //ļerkšķēt
		ThirdConj.directStd("-ļumu, -ļumi,", "-ļum, pag. -ļumēju", "ļumēt"), //ļumēt
		ThirdConj.directStd("-ļurkstu, -ļurksti,", "-ļurkst, pag. -ļurkstēju", "ļurkstēt"), //ļurkstēt
		ThirdConj.directStd("-ļurkšu, -ļurkši,", "-ļurkš, pag. -ļurkšēju", "ļurkšēt"), //ļurkšēt
		ThirdConj.directStd("-ļurkšķu, -ļurkšķi,", "-ļurkšķ, pag. -ļurkšķēju", "ļurkšķēt"), //ļurkšķēt
		// M
		ThirdConj.directStd("-minu, -mini,", "-min, pag. -minēju", "minēt"), //atminēt
		ThirdConj.directStd("-mirdzu, -mirdzi,", "-mirdz, pag. -mirdzēju", "mirdzēt"), //uzmirdzēt
		ThirdConj.directStd("-mīlu, -mīli,", "-mīl, pag. -mīlēju", "mīlēt"), //iemīlēt
		ThirdConj.directStd("-mīstu, -mīsti,", "-mīsta, pag. -mīstīju", "mīstīt"), //izmīstīt
		ThirdConj.directStd("-muldu, -muldi,", "-muld, pag. -muldēju", "muldēt"), //atmuldēt
		ThirdConj.directStd("-murdu, -murdi,", "-murd, pag. -murdēju", "murdēt"), //nomurdēt
		ThirdConj.directStd("-murdzu, -murdzi,", "-murdza, pag. -murdzīju", "murdzīt"), //apmurdzīt
		ThirdConj.directStd("-murkšu, -murkši,", "-murkš, pag. -murkšēju", "murkšēt"), //nomurkšēt
		ThirdConj.directStd("-murkšķu, -murkšķi,", "-murkšķ, pag. -murkšķēju", "murkšķēt"), //nomurkšķēt
		// N
		ThirdConj.directStd("-nīdu, -nīdi,", "-nīd, pag. -nīdēju", "nīdēt"), //nīdēt
		// Ņ
		ThirdConj.directStd("-ņaudu, -ņaudi,", "-ņaud, pag. -ņaudēju", "ņaudēt"), //izņaudēt
		ThirdConj.directStd("-ņerkstu, -ņerksti,", "-ņerkst, pag. -ņerkstēju", "ņerkstēt"), //noņerkstēt
		ThirdConj.directStd("-ņerkšu, -ņerkši,", "-ņerkš, pag. -ņerkšēju", "ņerkšēt"), //ņerkšēt
		ThirdConj.directStd("-ņerkšķu, -ņerkšķi,", "-ņerkšķ, pag. -ņerkšķēju", "ņerkšķēt"), //ņerkšķēt
		ThirdConj.directStd("-ņurdu, -ņurdi,", "-ņurd, pag. -ņurdēju", "ņurdēt"), //atņurdēt
		ThirdConj.directStd("-ņurkstu, -ņurksti,", "-ņurkst, pag. -ņurkstēju", "ņurkstēt"), //noņurkstēt
		ThirdConj.directStd("-ņurkšu, -ņurkši,", "-ņurkš, pag. -ņurkšēju", "ņurkšēt"), //noņurkšēt
		ThirdConj.directStd("-ņurkšķu, -ņurkšķi,", "-ņurkšķ, pag. -ņurkšķēju", "ņurkšķēt"), //noņurkšķēt
		// O, P
		ThirdConj.directStd("-parkšu, -parkši,", "-parkš, pag. -parkšēju", "parkšēt"), //noparkšēt
		ThirdConj.directStd("-parkšķu, -parkšķi,", "-parkšķ, pag. -parkšķēju", "parkšķēt"), //noparkšķēt
		ThirdConj.directStd("-peldu, -peldi,", "-peld, pag. -peldēju", "peldēt"), //aizpeldēt
		ThirdConj.directStd("-pilu, -pili,", "-pil, pag. -pilēju", "pilēt"), //appilēt, aizpilēt
		ThirdConj.directStd("-pinkšu, -pinkši,", "-pinkš, pag. -pinkšēju", "pinkšēt"), //nopinkšēt
		ThirdConj.directStd("-pinkšķu, -pinkšķi,", "-pinkšķ, pag. -pinkšķēju", "pinkšķēt"), //nopinkšķēt
		ThirdConj.directStd("-pīkstu, -pīksti,", "-pīkst, pag. -pīkstēju", "pīkstēt"), //atpīkstēt
		ThirdConj.directStd("-plarkšu, -plarkši,", "-plarkš, pag. -plarkšēju", "plarkšēt"), //plarkšēt, noplarkšēt
		ThirdConj.directStd("-plarkšķu, -plarkšķi,", "-plarkšķ, pag. -plarkšķēju", "plarkšķēt"), //plarkšķēt, noplarkšēt
		ThirdConj.directStd("-plerkšu, -plerkši,", "-plerkš, pag. -plerkšēju", "plerkšēt"), //plerkšēt
		ThirdConj.directStd("-plerkšķu, -plerkšķi,", "-plerkšķ, pag. -plerkšķēju", "plerkšķēt"), //plerkšķēt
		ThirdConj.directStd("-plosu, -plosi,", "-plosa, pag. -plosīju", "plosīt"), //plosīt
		ThirdConj.directStd("-pļekstu, -pļeksti,", "-pļekst, pag. -pļekstēju", "pļekstēt"), //pļekstēt
		ThirdConj.directStd("-pļekšu, -pļekši,", "-pļekš, pag. -pļekšēju", "pļekšēt"), //pļekšēt
		ThirdConj.directStd("-pļekšķu, -pļekšķi,", "-pļekšķ, pag. -pļekšķēju", "pļekšķēt"), //pļekšķēt
		ThirdConj.directStd("-pļerkstu, -pļerksti,", "-pļerkst, pag. -pļerkstēju", "pļerkstēt"), //pļerkstēt
		ThirdConj.directStd("-pļerkšu, -pļerkši,", "-pļerkš, pag. -pļerkšēju", "pļerkšēt"), //pļerkšēt, nopļerkšēt
		ThirdConj.directStd("-pļerkšķu, -pļerkšķi,", "-pļerkšķ, pag. -pļerkšķēju", "pļerkšķēt"), //pļerkšķēt, nopļerkšķēt
		ThirdConj.directStd("-pļurkstu, -pļurksti,", "-pļurkst, pag. -pļurkstēju", "pļurkstēt"), //pļurkstēt
		ThirdConj.directStd("-pļurkšu, -pļurkši,", "-pļurkš, pag. -pļurkšēju", "pļurkšēt"), //pļurkšēt
		ThirdConj.directStd("-pļurkšķu, -pļurkšķi,", "-pļurkšķ, pag. -pļurkšķēju", "pļurkšķēt"), //pļurkšķēt
		ThirdConj.directStd("-precu, -preci,", "-prec, pag. -precēju", "precēt"), //aizprecēt
		ThirdConj.directStd("-pukstu, -puksti,", "-pukst, pag. -pukstēju", "pukstēt"), //nopukstēt
		ThirdConj.directStd("-pukšu, -pukši,", "-pukš, pag. -pukšēju", "pukšēt"), //pukšēt, nopukšēt
		ThirdConj.directStd("-pukšķu, -pukšķi,", "-pukšķ, pag. -pukšķēju", "pukšķēt"), //pukšķēt, aizpukšķēt
		ThirdConj.directStd("-purkšu, -purkši,", "-purkš, pag. -purkšēju", "purkšēt"), //nopurkšēt
		ThirdConj.directStd("-purkšķu, -purkšķi,", "-purkšķ, pag. -purkšķēju", "purkšķēt"), //nopurkšķēt
		ThirdConj.directStd("-putu, -puti,", "-put, pag. -putēju", "putēt"), //aizputēt, apputēt
		// R
		ThirdConj.directStd("-raudu, -raudi,", "-raud, pag. -raudāju", "raudāt"), //apraudāt
		ThirdConj.directStd("-redzu, -redzi,", "-redz, pag. -redzēju", "redzēt"), //apredzēt
		// S
		ThirdConj.directStd("-sāpu, -sāpi,", "-sāp, pag. -sāpēju", "sāpēt"), //izsāpēt
		ThirdConj.directStd("-sīkstu, -sīksti,", "-sīkst, pag. -sīkstēju", "sīkstēt"), //apsīkstēt
		ThirdConj.directStd("-skanu, -skani,", "-skan, pag. -skanēju", "skanēt"), //pieskanēt
		ThirdConj.directStd("-smilkstu, -smilksti,", "-smilkst, pag. -smilkstēju", "smilkstēt"), //smilkstēt, nosmilkstēt
		ThirdConj.directStd("-smirdu, -smirdi,", "-smird, pag. -smirdēju", "smirdēt"), //pasmirdēt
		ThirdConj.directStd("-smīnu, -smīni,", "-smīn, pag. -smīnēju", "smīnēt"), //atsmīnēt
		ThirdConj.directStd("-snaiku, -snaiki,", "-snaika, pag. -snaicīju", "snaicīt"), //snaicīt
		ThirdConj.directStd("-spindzu, -spindzi,", "-spindz, pag. -spindzēju", "spindzēt"), //spindzēt, nospindzēt
		ThirdConj.directStd("-spirdzu, -spirdzi,", "-spirdz, pag. -spirdzēju", "spirdzēt"), //spirdzēt
		ThirdConj.directStd("-spīdu, -spīdi,", "-spīd, pag. -spīdēju", "spīdēt"), //spīdēt, nespīdēt
		ThirdConj.directStd("-spridzu, -spridzi,", "-spridz, pag. -spridzēju", "spridzēt"), //spridzēt
		ThirdConj.directStd("-sprikstu, -spriksti,", "-sprikst, pag. -sprikstēju", "sprikstēt"), //sprikstēt, iesprikstēt
		ThirdConj.directStd("-spurkšu, -spurkši,", "-spurkš, pag. -spurkšēju", "spurkšēt"), //spurkšēt, atspurkšēt
		ThirdConj.directStd("-spurkšķu, -spurkšķi,", "-spurkšķ, pag. -spurkšķēju", "spurkšķēt"), //spurkšķēt, atspurkšķēt
		ThirdConj.directStd("-stāvu, -stāvi,", "-stāv, pag. -stāvēju", "stāvēt"), //aizstāvēt
		ThirdConj.directStd("-stenu, -steni,", "-sten, pag. -stenēju", "stenēt"), //izstenēt
		ThirdConj.directStd("-stīdzu, -stīdzi,", "-stīdz, pag. -stīdzēju", "stīdzēt"), //izstīdzēt
		ThirdConj.directStd("-strīdu, -strīdi,", "-strīd, pag. -strīdēju", "strīdēt"), //apstrīdēt
		ThirdConj.directStd("-sūdzu, -sūdzi,", "-sūdz, pag. -sūdzēju", "sūdzēt"), //apsūdzēt
		ThirdConj.directStd("-svepstu, -svepsti,", "-svepst, pag. -svepstēju", "svepstēt"), //izsvepstēt
		ThirdConj.directStd("-svinu, -svini,", "-svin, pag. -svinēju", "svinēt"), //izsvinēt
		// Š
		ThirdConj.directStd("-šļupstu, -šļupsti,", "-šļupst, pag. -šļupstēju", "šļupstēt"), //atšļupstēt
		ThirdConj.directStd("-šļūdu, -šļūdi,", "-šļūd, pag. -šļūdēju", "šļūdēt"), //šļūdēt
		ThirdConj.directStd("-šņukstu, -šņuksti,", "-šņukst, pag. -šņukstēju", "šņukstēt"), //izšņukstēt
		ThirdConj.directStd("-švirkstu, -švirksti,", "-švirkst, pag. -švirkstēju", "švirkstēt"), //uzšvirkstēt
		// T
		ThirdConj.directStd("-tarkšu, -tarkši,", "-tarkš, pag. -tarkšēju", "tarkšēt"), //iztarkšēt
		ThirdConj.directStd("-tarkšķu, -tarkšķi,", "-tarkšķ, pag. -tarkšķēju", "tarkšķēt"), //iztarkšķēt
		ThirdConj.directStd("-neticu, -netici,", "-netic, pag. -neticēju", "neticēt"), //neticēt //TODO vai vajag citur?
		ThirdConj.directStd("-ticu, -tici,", "-tic, pag. -ticēju", "ticēt"), //noticēt
		ThirdConj.directStd("-trīcu, -trīci,", "-trīc, pag. -trīcēju", "trīcēt"), //ietrīcēt
		ThirdConj.directStd("-trīsu, -trīsi,", "-trīs, pag. -trīsēju", "trīsēt"), //ietrīsēt
		ThirdConj.directStd("-tupu, -tupi,", "-tup, pag. -tupēju", "tupēt"), //tupēt
		ThirdConj.directStd("-turu, -turi,", "-tur, pag. -turēju", "turēt"), //aizturēt
		ThirdConj.directStd("-tūcu, -tūci,", "-tūca, pag. -tūcīju", "tūcīt"), //pietūcīt
		// U
		ThirdConj.directStd("-urdu, -urdi,", "-urda, pag. -urdīju", "urdīt"), //saurdīt
		ThirdConj.directStd("-urkšu, -urkši,", "-urkš, pag. -urkšēju", "urkšēt"), //urkšēt, aturkšēt
		ThirdConj.directStd("-urkšķu, -urkšķi,", "-urkšķ, pag. -urkšķēju", "urkšķēt"), //urkšķēt, aturkšķēt
		// V
		ThirdConj.directStd("-vaidu, -vaidi,", "-vaid, pag. -vaidēju", "vaidēt"), //izvaidēt
		ThirdConj.directStd("-varu, -vari,", "-var, pag. -varēju", "varēt"), //pārvarēt
		ThirdConj.directStd("-verkšu, -verkši,", "-verkš, pag. -verkšēju", "verkšēt"), //saverkšēt
		ThirdConj.directStd("-verkšķu, -verkšķi,", "-verkšķ, pag. -verkšķēju", "verkšķēt"), //saverkšķēt
		ThirdConj.directStd("-vēkšu, -vēkši,", "-vēkš, pag. -vēkšēju", "vēkšēt"), //vēkšēt
		ThirdConj.directStd("-vēkšķu, -vēkšķi,", "-vēkšķ, pag. -vēkšķēju", "vēkšķēt"), //vēkšķēt
		ThirdConj.directStd("-vēlu, -vēli,", "-vēl, pag. -vēlēju", "vēlēt"), //atvēlēt
		ThirdConj.directStd("-vīkšu, -vīkši,", "-vīkš, pag. -vīkšīju", "vīkšīt"), //savīkšīt
		ThirdConj.directStd("-vīkšķu, -vīkšķi,", "-vīkšķ, pag. -vīkšķīju", "vīkšķīt"), //savīkšķīt
		// Z
		ThirdConj.directStd("-zibu, -zibi,", "-zib, pag. -zibēju", "zibēt"), //nozibēt
		ThirdConj.directStd("-ziedu, -ziedi,", "-zied, pag. -ziedēju", "ziedēt"), //noziedēt
		ThirdConj.directStd("-zinu, -zini,", "-zina, pag. -zināju", "zināt"), //apzināt
		ThirdConj.directStd("-zvēru, -zvēri,", "-zvēr, pag. -zvērēju", "zvērēt"), //apzvērēt
		ThirdConj.directStd("-zvilnu, -zvilni,", "-zviln, pag. -zvilnēju", "zvilnēt"), //nozvilnēt
		ThirdConj.directStd("-žņaudzu, -žņaudzi,", "-žņaudza, pag. -žņaudzīju", "žņaudzīt"), //žņaudzīt

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		// Fakultatīva mija.
		ThirdConj.directOptChange3PersParallel(
				"-kait, arī -kaiš, pag. -kaitēja", "kaitēt"), // kaitēt
		// 1. konj. paralēlforma.
		ThirdConj.directStd3PersParallel(
				"-grand, pag. -grandēja (retāk -granda, 1. konj.)", "grandēt"), //aizgrandēt
		ThirdConj.directStd3PersParallel(
				"-gruzd, pag. -gruzdēja (retāk -gruzda, 1. konj.)", "gruzdēt"), //aizgruzdēt
		ThirdConj.directStd3PersParallel(
				"-gruzd, pag. -gruzdēja (arī -gruzda, 1. konj.)", "gruzdēt"), //apgruzdēt
		ThirdConj.directStd3PersParallel(
				"-mirdz, pag. -mirdzēja (retāk -mirdza, 1. konj.)", "mirdzēt"), //aizmirdzēt
		ThirdConj.directStd3PersParallel(
				"-rit, pag. -ritēja (retāk -rita, 1. konj.)", "ritēt"), // aizritēt
		ThirdConj.directStd3PersParallel(
				"-skand, pag. -skandēja (retāk -skanda, 1. konj.)", "skandēt"), // skandēt
		ThirdConj.directStd3PersParallel(
				"-smird, pag. -smirdēja (arī -smirda, 1. konj.)", "smirdēt"), // sasmirdēt
		ThirdConj.directStd3PersParallel(
				"-spindz, pag. -spindzēja (retāk -spindza, 1. konj.)", "spindzēt"), // aizspindzēt
		ThirdConj.directStd3PersParallel(
				"-spīd, pag. -spīdēja (retāk -spīda, 1. konj.)", "spīdēt"), // aizspīdēt
		ThirdConj.directStd3PersParallel(
				"-spridz, pag. -spridzēja (retāk -spridza, 1. konj.)", "spridzēt"), // iespriedzēt
		ThirdConj.directStd3PersParallel(
				"-šķind, pag. -šķindēja (retāk -šķinda, 1. konj.)", "šķindēt"), // aizšķindēt
		ThirdConj.directStd3PersParallel(
				"-viz, pag. -vizēja (retāk -viza, 1. konj.)", "vizēt"), // aizvizēt
		ThirdConj.directStd3PersParallel(
				"-vīd, pag. -vīdēja (retāk -vīda, 1. konj.)", "vīdēt"), // atvīdēt

		// Standartizētie ar mijām.
		ThirdConj.directChange3Pers("-vajag, pag. -vajadzēja", "vajadzēt"), //vajadzēt
		// Standartizētie bez mijām.
		// A, B
		ThirdConj.directStd3Pers("-baukš, pag. -baukšēja", "baukšēt"), //nobaukšēt
		ThirdConj.directStd3Pers("-baukšķ, pag. -baukšķēja", "baukšķēt"), //nobaukšķēt
		ThirdConj.directStd3Pers("-blakš, pag. -blakšēja", "blakšēt"), //noblakšēt
		ThirdConj.directStd3Pers("-blakšķ, pag. -blakšķēja", "blakšķēt"), //noblakšķēt
		ThirdConj.directStd3Pers("-blarkš, pag. -blarkšēja", "blarkšēt"), //noblarkšēt
		ThirdConj.directStd3Pers("-blarkšķ, pag. -blarkšķēja", "blarkšķēt"), //noblarkšķēt
		ThirdConj.directStd3Pers("-blaukš, pag. -blaukšēja", "blaukšēt"), //noblaukšēt
		ThirdConj.directStd3Pers("-blaukšķ, pag. -blaukšķēja", "blaukšķēt"), //noblaukšķēt
		ThirdConj.directStd3Pers("-blākš, pag. -blākšēja", "blākšēt"), //aizblākšēt
		ThirdConj.directStd3Pers("-blākšķ, pag. -blākšķēja", "blākšķēt"), //aizblākšķēt
		ThirdConj.directStd3Pers("-blāv, pag. -blāvēja", "blāvēt"), //pablāvēt
		ThirdConj.directStd3Pers("-bliukš, pag. -bliukšēja", "bliukšēt"), //nobliukšēt
		ThirdConj.directStd3Pers("-bliukšķ, pag. -bliukšķēja", "bliukšķēt"), //nobliukšķēt
		ThirdConj.directStd3Pers("-blīkš, pag. -blīkšēja", "blīkšēt"), //noblīkšēt
		ThirdConj.directStd3Pers("-blīkšķ, pag. -blīkšķēja", "blīkšķēt"), //noblīkšķēt
		ThirdConj.directStd3Pers("-blūkš, pag. -blūkšēja", "blūkšēt"), //noblūkšēt
		ThirdConj.directStd3Pers("-blūkšķ, pag. -blūkšķēja", "blūkšķēt"), //noblūkšķēt
		ThirdConj.directStd3Pers("-brakst, pag. -brakstēja", "brakstēt"), //nobrakstēt
		ThirdConj.directStd3Pers("-brakš, pag. -brakšēja", "brakšēt"), //nobrakšēt
		ThirdConj.directStd3Pers("-brakšķ, pag. -brakšķēja", "brakšķēt"), //nobrakšķēt
		ThirdConj.directStd3Pers("-brākš, pag. -brākšēja", "brākšēt"), //nobrākšēt
		ThirdConj.directStd3Pers("-brākšķ, pag. -brākšķēja", "brākšķēt"), //nobrākšķēt
		ThirdConj.directStd3Pers("-brikš, pag. -brikšēja", "brikšēt"), //nobrikšēt
		ThirdConj.directStd3Pers("-brikšķ, pag. -brikšķēja", "brikšķēt"), //nobrikšķēt
		ThirdConj.directStd3Pers("-brīkš, pag. -brīkšēja", "brīkšēt"), //nobrīkšēt
		ThirdConj.directStd3Pers("-brīkšķ, pag. -brīkšķēja", "brīkšķēt"), //nobrīkšķēt
		ThirdConj.directStd3Pers("-burbē, pag. -burbēja", "burbēt"), //apburbēt
		ThirdConj.directStd3Pers("-būb, pag. -būbēja", "būbēt"), //apbūbēt
		ThirdConj.directStd3Pers("-būkš, pag. -būkšēja", "būkšēt"), //nobūkšēt
		ThirdConj.directStd3Pers("-būkšķ, pag. -būkšķēja", "būkšķēt"), //nobūkšķēt
		// C
		ThirdConj.directStd3Pers("-cipst, pag. -cipstēja", "cipstēt"), //cipstēt
		ThirdConj.directStd3Pers("-cirpst, pag. -cirpstēja", "cirpstēt"), //cirpstēt
		// Č
		ThirdConj.directStd3Pers("-čakst, pag. -čakstēja", "čakstēt"), //nočakstēt
		ThirdConj.directStd3Pers("-čarkst, pag. -čarkstēja", "čarkstēt"), //čarkstēt
		ThirdConj.directStd3Pers("-čārkst, pag. -čārkstēja", "čārkstēt"), //čarkstēt
		ThirdConj.directStd3Pers("-čib, pag. -čibēja", "čibēt"), //izčibēt
		ThirdConj.directStd3Pers("-čirkst, pag. -čirkstēja", "čirkstēt"), //nočirkstēt
		ThirdConj.directStd3Pers("-čirpst, pag. -čirpstēja", "čirpstēt"), //čirpstēt
		ThirdConj.directStd3Pers("-čiukst, pag. -čiukstēja", "čiukstēt"), //čiukstēt
		ThirdConj.directStd3Pers("-čum, pag. -čumēja", "čumēt"), //čumēt
		ThirdConj.directStd3Pers("-čurkst, pag. -čurkstēja", "čurkstēt"), //izčurkstēt
		ThirdConj.directStd3Pers("-čūkst, pag. -čūkstēja", "čūkstēt"), //nočūkstēt
		// D
		ThirdConj.directStd3Pers("-dārd, pag. -dārdēja", "dārdēt"), //aizdārdēt
		ThirdConj.directStd3Pers("-dimd, pag. -dimdēja", "dimdēt"), //aizdimdēt
		ThirdConj.directStd3Pers("-dip, pag. -dipēja", "dipēt"), //aizdipēt
		ThirdConj.directStd3Pers("-dund, pag. -dundēja", "dundēt"), //dundēt
		ThirdConj.directStd3Pers("-dun, pag. -dunēja", "dunēt"), //aizdunēt
		ThirdConj.directStd3Pers("-dzinkst, pag. -dzinkstēja", "dzinkstēt"), //nodzinkstēt
		ThirdConj.directStd3Pers("-dzirkst, pag. -dzirkstēja", "dzirkstēt"), //iedzirkstēt
		ThirdConj.directStd3Pers("-džerkst, pag. -džerkstēja", "džerkstēt"), //džerkstēt
		ThirdConj.directStd3Pers("-džinkst, pag. -džinkstēja", "džinkstēt"), //aizdžinkstēt
		ThirdConj.directStd3Pers("-džirkst, pag. -džirkstēja", "džirkstēt"), //džirkstēt
		// E, F, G
		ThirdConj.directStd3Pers("-granda, pag. -grandīja", "grandīt"), //nograndīt
		ThirdConj.directStd3Pers("-guldz, pag. -guldzēja", "guldzēt"), //noguldzēt
		ThirdConj.directStd3Pers("-gurkst, pag. -gurkstēja", "gurkstēt"), //aizgurkstēt
		ThirdConj.directStd3Pers("-gurkš, pag. -gurkšēja", "gurkšēt"), //pagurkšēt
		ThirdConj.directStd3Pers("-gurkšķ, pag. -gurkšķēja", "gurkšķēt"), //pagurkšķēt
		// H, I
		ThirdConj.directStd3Pers("-indz, pag. -indzēja", "indzēt"), //indzēt
		ThirdConj.directStd3Pers("-irdz, pag. -irdzēja", "irdzēt"), //irdzēt
		// J, K
		ThirdConj.directStd3Pers("-klakst, pag. -klakstēja", "klakstēt"), //aizklakstēt
		ThirdConj.directStd3Pers("-klakš, pag. -klakšēja", "klakšēt"), //noklakšēt
		ThirdConj.directStd3Pers("-klakšķ, pag. -klakšķēja", "klakšķēt"), //noklakšķēt
		ThirdConj.directStd3Pers("-kland, pag. -klandēja", "klandēt"), //klandēt
		ThirdConj.directStd3Pers("-klankst, pag. -klankstēja", "klankstēt"), //noklankstēt
		ThirdConj.directStd3Pers("-klankš, pag. -klankšēja", "klankšēt"), //noklankšēt
		ThirdConj.directStd3Pers("-klankšķ, pag. -klankšķēja", "klankšķēt"), //noklankšķēt
		ThirdConj.directStd3Pers("-klaukš, pag. -klaukšēja", "klaukšēt"), //noklaukšēt
		ThirdConj.directStd3Pers("-klaukšķ, pag. -klaukšķēja", "klaukšķēt"), //noklaukšķēt
		ThirdConj.directStd3Pers("-klikst, pag. -klikstēja", "klikstēt"), //noklikstēt
		ThirdConj.directStd3Pers("-klikš, pag. -klikšēja", "klikšēt"), //noklikšēt
		ThirdConj.directStd3Pers("-klikšķ, pag. -klikšķēja", "klikšķēt"), //noklikšķēt
		ThirdConj.directStd3Pers("-klinkst, pag. -klinkstēja", "klinkstēt"), //noklinkstēt
		ThirdConj.directStd3Pers("-klinkš, pag. -klinkšēja", "klinkšēt"), //noklinkšēt
		ThirdConj.directStd3Pers("-klinkšķ, pag. -klinkšķēja", "klinkšķēt"), //noklinkšķēt
		ThirdConj.directStd3Pers("-klukst, pag. -klukstēja", "klukstēt"), //noklukstēt
		ThirdConj.directStd3Pers("-klukš, pag. -klukšēja", "klukšēt"), //klukšēt
		ThirdConj.directStd3Pers("-klukšķ, pag. -klukšķēja", "klukšķēt"), //klukšķēt
		ThirdConj.directStd3Pers("-klunkst, pag. -klunkstēja", "klunkstēt"), //noklunkstēt
		ThirdConj.directStd3Pers("-klunkš, pag. -klunkšēja", "klunkšēt"), //noklunkšēt
		ThirdConj.directStd3Pers("-klunkšķ, pag. -klunkšķēja", "klunkšķēt"), //noklunkšķēt
		ThirdConj.directStd3Pers("-knakst, pag. -knakstēja", "knakstēt"), //noknakstēt
		ThirdConj.directStd3Pers("-knakš, pag. -knakšēja", "knakšēt"), //noknakšēt
		ThirdConj.directStd3Pers("-knakšķ, pag. -knakšķēja", "knakšķēt"), //noknakšķēt
		ThirdConj.directStd3Pers("-knaukst, pag. -knaukstēja", "knaukstēt"), //noknaukstēt
		ThirdConj.directStd3Pers("-knaukš, pag. -knaukšēja", "knaukšēt"), //noknaukšēt
		ThirdConj.directStd3Pers("-knaukšķ, pag. -knaukšķēja", "knaukšķēt"), //noknaukšķēt
		ThirdConj.directStd3Pers("-knies, pag. -kniesēja", "kniesēt"), //kniesēt
		ThirdConj.directStd3Pers("-kniez, pag. -kniezēja", "kniezēt"), //kniezēt
		ThirdConj.directStd3Pers("-knikst, pag. -knikstēja", "knikstēt"), //noknikstēt
		ThirdConj.directStd3Pers("-knikš, pag. -knikšēja", "knikšēt"), //noknikšēt
		ThirdConj.directStd3Pers("-knikšķ, pag. -knikšķēja", "knikšķēt"), //noknikšķēt
		ThirdConj.directStd3Pers("-knirkst, pag. -knirkstēja", "knirkstēt"), //knirkstēt
		ThirdConj.directStd3Pers("-kniukst, pag. -kniukstēja", "kniukstēt"), //kniukstēt
		ThirdConj.directStd3Pers("-kniukš, pag. -kniukšēja", "kniukšēt"), //kniukšēt
		ThirdConj.directStd3Pers("-kniukšķ, pag. -kniukšķēja", "kniukšķēt"), //kniukšķēt
		ThirdConj.directStd3Pers("-kņud, pag. -kņudēja", "kņudēt"), //nokņudēt
		ThirdConj.directStd3Pers("-krakst, pag. -krakstēja", "krakstēt"), //nokrakstēt
		ThirdConj.directStd3Pers("-krakš, pag. -krakšēja", "krakšēt"), //nokrakšēt
		ThirdConj.directStd3Pers("-krakšķ, pag. -krakšķēja", "krakšķēt"), //nokrakšķēt
		ThirdConj.directStd3Pers("-krapst, pag. -krapstēja", "krapstēt"), //krapstēt
		ThirdConj.directStd3Pers("-krapš, pag. -krapšēja", "krapšēt"), //krapšēt
		ThirdConj.directStd3Pers("-krapšķ, pag. -krapšķēja", "krapšķēt"), //krapšķēt
		ThirdConj.directStd3Pers("-kraukst, pag. -kraukstēja", "kraukstēt"), //nokraukstēt
		ThirdConj.directStd3Pers("-kraukš, pag. -kraukšēja", "kraukšēt"), //nokraukšēt
		ThirdConj.directStd3Pers("-kraukšķ, pag. -kraukšķēja", "kraukšķēt"), //nokraukšķēt
		ThirdConj.directStd3Pers("-krikst, pag. -krikstēja", "krikstēt"), //nokrikstēt
		ThirdConj.directStd3Pers("-krikš, pag. -krikšēja", "krikšēt"), //nokrikšēt
		ThirdConj.directStd3Pers("-krikšķ, pag. -krikšķēja", "krikšķēt"), //nokrikšķēt
		ThirdConj.directStd3Pers("-krimš, pag. -krimšēja", "krimšēt"), //nokrimšēt
		ThirdConj.directStd3Pers("-krimšķ, pag. -krimšķēja", "krimšķēt"), //nokrimšķēt
		ThirdConj.directStd3Pers("-kripst, pag. -kripstēja", "kripstēt"), //kripstēt
		ThirdConj.directStd3Pers("-kripš, pag. -kripšēja", "kripšēt"), //kripšēt
		ThirdConj.directStd3Pers("-kripšķ, pag. -kripšķēja", "kripšķēt"), //kripšķēt
		ThirdConj.directStd3Pers("-kriukst, pag. -kriukstēja", "kriukstēt"), //kriukstēt
		ThirdConj.directStd3Pers("-kriukš, pag. -kriukšēja", "kriukšēt"), //pakriukšēt
		ThirdConj.directStd3Pers("-kriukšķ, pag. -kriukšķēja", "kriukšķēt"), //pakriukšķēt
		ThirdConj.directStd3Pers("-kurkst, pag. -kurkstēja", "kurkstēt"), //nokurkstēt
		ThirdConj.directStd3Pers("-kurkš, pag. -kurkšēja", "kurkšēt"), //nokurkšēt
		ThirdConj.directStd3Pers("-kurkšķ, pag. -kurkšķēja", "kurkšķēt"), //nokurkšķēt
		ThirdConj.directStd3Pers("-kut, pag. -kutēja", "kutēt"), //pakutēt
		ThirdConj.directStd3Pers("-kvakst, pag. -kvakstēja", "kvakstēt"), //nokvakstēt
		ThirdConj.directStd3Pers("-kvakš, pag. -kvakšēja", "kvakšēt"), //nokvakšēt
		ThirdConj.directStd3Pers("-kvakšķ, pag. -kvakšķēja", "kvakšķēt"), //nokvakšķēt
		ThirdConj.directStd3Pers("-kvankst, pag. -kvankstēja", "kvankstēt"), //nokvankstēt
		ThirdConj.directStd3Pers("-kvankš, pag. -kvankšēja", "kvankšēt"), //nokvankšēt
		ThirdConj.directStd3Pers("-kvankšķ, pag. -kvankšķēja", "kvankšķēt"), //nokvankšķēt
		ThirdConj.directStd3Pers("-kvarkš, pag. -kvarkšēja", "kvarkšēt"), //kvarkšēt
		ThirdConj.directStd3Pers("-kvarkšķ, pag. -kvarkšķēja", "kvarkšķēt"), //kvarkšķēt
		ThirdConj.directStd3Pers("-kvekst, pag. -kvekstēja", "kvekstēt"), //kvekstēt
		ThirdConj.directStd3Pers("-kvekš, pag. -kvekšēja", "kvekšēt"), //kvekšēt
		ThirdConj.directStd3Pers("-kvekšķ, pag. -kvekšķēja", "kvekšķēt"), //kvekšķēt
		ThirdConj.directStd3Pers("-kverkst, pag. -kverkstēja", "kverkstēt"), //kverkstēt
		ThirdConj.directStd3Pers("-kverkš, pag. -kverkšēja", "kverkšēt"), //kverkšēt
		ThirdConj.directStd3Pers("-kverkšķ, pag. -kverkšķēja", "kverkšķēt"), //kverkšķēt
		// Ķ
		ThirdConj.directStd3Pers("-ķaukst, pag. -ķaukstēja", "ķaukstēt"), //ķaukstēt
		ThirdConj.directStd3Pers("-ķiukst, pag. -ķiukstēja", "ķiukstēt"), //ķiukstēt
		// L
		ThirdConj.directStd3Pers("-lās, pag. -lāsēja", "lāsēt"), //pielāsēt
		ThirdConj.directStd3Pers("-lon, pag. -lonēja", "lonēt"), //lonēt
		// M
		ThirdConj.directStd3Pers("-mēkšķ, pag. -mēkšķēja", "mēkšķēt"), //mēkšķēt
		ThirdConj.directStd3Pers("-mud, pag. -mudēja", "mudēt"), //samudet
		ThirdConj.directStd3Pers("-mudž, pag. -mudžēja", "mudžēt"), //mudžēt
		// N
		ThirdConj.directStd3Pers("-niez, pag. -niezēja", "niezēt"), //paniezēt
		// Ņ
		ThirdConj.directStd3Pers("-ņirb, pag. -ņirbēja", "ņirbēt"), //aizņirbēt
		ThirdConj.directStd3Pers("-ņirkst, pag. -ņirkstēja", "ņirkstēt"), //noņirkstēt
		ThirdConj.directStd3Pers("-ņirkš, pag. -ņirkšēja", "ņirkšēt"), //noņirkšēt
		ThirdConj.directStd3Pers("-ņirkšķ, pag. -ņirkšķēja", "ņirkšķēt"), //noņirkšķēt
		ThirdConj.directStd3Pers("-ņudz, pag. -ņudzēja", "ņudzēt"), //ņudzēt
		// O, P
		ThirdConj.directStd3Pers("-pakš, pag. -pakšēja", "pakšēt"), //nopakšēt
		ThirdConj.directStd3Pers("-pakšķ, pag. -pakšķēja", "pakšķēt"), //nopakšķēt
		ThirdConj.directStd3Pers("-paukš, pag. -paukšēja", "paukšēt"), //nopaukšēt
		ThirdConj.directStd3Pers("-paukšķ, pag. -paukšķēja", "paukšķēt"), //nopaukšķēt
		ThirdConj.directStd3Pers("-pēkst, pag. -pēkstēja", "pēkstēt"), //nopēkstēt
		ThirdConj.directStd3Pers("-pēkš, pag. -pēkšēja", "pēkšēt"), //nopēkšēt
		ThirdConj.directStd3Pers("-pēkšķ, pag. -pēkšķēja", "pēkšķēt"), //nopēkšķēt
		ThirdConj.directStd3Pers("-plakst, pag. -plakstēja", "plakstēt"), //noplakstēt
		ThirdConj.directStd3Pers("-plakš, pag. -plakšēja", "plakšēt"), //noplakšēt
		ThirdConj.directStd3Pers("-plakšķ, pag. -plakšķēja", "plakšķēt"), //noplakšķēt
		ThirdConj.directStd3Pers("-plākš, pag. -plākšēja", "plākšēt"), //noplākšēt
		ThirdConj.directStd3Pers("-plākšķ, pag. -plākšķēja", "plākšķēt"), //noplākšķēt
		ThirdConj.directStd3Pers("-pland, pag. -plandēja", "plandēt"), //noplandēt
		ThirdConj.directStd3Pers("-plaukš, pag. -plaukšēja", "plaukšēt"), //noplaukšēt
		ThirdConj.directStd3Pers("-plaukšķ, pag. -plaukšķēja", "plaukšķēt"), //noplaukšķēt
		ThirdConj.directStd3Pers("-plekst, pag. -plekstēja", "plekstēt"), //plekstēt
		ThirdConj.directStd3Pers("-plekš, pag. -plekšēja", "plekšēt"), //plekšēt
		ThirdConj.directStd3Pers("-plekšķ, pag. -plekšķēja", "plekšķēt"), //plekšķēt
		ThirdConj.directStd3Pers("-plikst, pag. plikstēja", "plikstēt"), //plikstēt
		ThirdConj.directStd3Pers("-plikš, pag. -plikšēja", "plikšēt"), //paplikšēt
		ThirdConj.directStd3Pers("-plikšķ, pag. -plikšķēja", "plikšķēt"), //paplikškēt
		ThirdConj.directStd3Pers("-plinkš, pag. -plinkšēja", "plinkšēt"), //noplinkšēt
		ThirdConj.directStd3Pers("-plinkšķ, pag. -plinkšķēja", "plinkšķēt"), //noplinkšķēt
		ThirdConj.directStd3Pers("-plirkš, pag. -plirkšēja", "plirkšēt"), //plirkšēt
		ThirdConj.directStd3Pers("-plirkšķ, pag. -plirkšķēja", "plirkšķēt"), //plirkšķēt
		ThirdConj.directStd3Pers("-pliukš, pag. -pliukšēja", "pliukšēt"), //nopliukšēt
		ThirdConj.directStd3Pers("-pliukšķ, pag. -pliukšķēja", "pliukšķēt"), //nopliukšķēt
		ThirdConj.directStd3Pers("-plīkš, pag. -plīkšēja", "plīkšēt"), //noplīkšēt
		ThirdConj.directStd3Pers("-plīkšķ, pag. -plīkšķēja", "plīkšķēt"), //noplīkšķēt
		ThirdConj.directStd3Pers("-plīv, pag. -plīvēja", "plīvēt"), //noplīvēt
		ThirdConj.directStd3Pers("-plunkš, pag. -plunkšēja", "plunkšēt"), //noplunkšēt
		ThirdConj.directStd3Pers("-plunkšķ, pag. -plunkšķēja", "plunkšķēt"), //noplunkšķēt
		ThirdConj.directStd3Pers("-pūkš, pag. -pūkšēja", "pūkšēt"), //pūkšēt
		ThirdConj.directStd3Pers("-pūkšķ, pag. -pūkšķēja", "pūkšķēt"), //pūkšķēt
		// R
		ThirdConj.directStd3Pers("-rec, pag. -recēja", "recēt"), //aprecēt
		ThirdConj.directStd3Pers("-rekš, pag. -rekšēja", "rekšēt"), //rekšēt
		ThirdConj.directStd3Pers("-rekšķ, pag. -rekšķēja", "rekšķēt"), //rekšķēt
		ThirdConj.directStd3Pers("-riet, pag. -rietēja", "rietēt"), //aizrietēt
		ThirdConj.directStd3Pers("-ris, pag. -risēja", "risēt"), //norisēt
		ThirdConj.directStd3Pers("-rit, pag. -ritēja", "ritēt"), // pārritēt
		ThirdConj.directStd3Pers("-rīb, pag. -rībēja", "rībēt"), //aizrībēt
		ThirdConj.directStd3Pers("-rukst, pag. -rukstēja", "rukstēt"), //norukstēt
		ThirdConj.directStd3Pers("-rukš, pag. -rukšēja", "rukšēt"), //atrukšēt
		ThirdConj.directStd3Pers("-rukšķ, pag. -rukšķēja", "rukšķēt"), //atrukšķēt
		ThirdConj.directStd3Pers("-rūp, pag. -rūpēja", "rūpēt"), //rūpēt
		// S
		ThirdConj.directStd3Pers("-san, pag. -sanēja", "sanēt"), //aizsanēt
		ThirdConj.directStd3Pers("-sekš, pag. -sekšēja", "sekšēt"), //sasekšēt
		ThirdConj.directStd3Pers("-sekšķ, pag. -sekšķēja", "sekšķēt"), //sasekškēt
		ThirdConj.directStd3Pers("-skrab, pag. -skrabēja", "skrabēt"), //skrabēt
		ThirdConj.directStd3Pers("-skrakst, pag. -skrakstēja", "skrakstēt"), //skrakstēt
		ThirdConj.directStd3Pers("-skrakš, pag. -skrakšēja", "skrakšēt"), //skrakšēt
		ThirdConj.directStd3Pers("-skrakšķ, pag. -skrakšķēja", "skrakšķēt"), //skrakšķēt
		ThirdConj.directStd3Pers("-skapst, pag. -skapstēja", "skapstēt"), //apskapstēt
		ThirdConj.directStd3Pers("-skrapst, pag. -skrapstēja", "skrapstēt"), //noskrapstēt
		ThirdConj.directStd3Pers("-skrapš, pag. -skrapšēja", "skrapšēt"), //noskrapšēt
		ThirdConj.directStd3Pers("-skrapšķ, pag. -skrapšķēja", "skrapšķēt"), //noskrapšķēt
		ThirdConj.directStd3Pers("-skraukst, pag. -skraukstēja", "skraukstēt"), //skraukstēt
		ThirdConj.directStd3Pers("-skraukš, pag. -skraukšēja", "skraukšēt"), //noskraukšēt
		ThirdConj.directStd3Pers("-skraukšķ, pag. -skraukšķēja", "skraukšķēt"), //noskraukšķēt
		ThirdConj.directStd3Pers("-skripst, pag. -skripstēja", "skripstēt"), //skripstēt
		ThirdConj.directStd3Pers("-skripš, pag. -skripšēja", "skripšēt"), //skripšēt
		ThirdConj.directStd3Pers("-skripšķ, pag. -skripšķēja", "skripšķēt"), //skripšķēt
		ThirdConj.directStd3Pers("-skriukst, pag. -skriukstēja", "skriukstēt"), //skriukstēt
		ThirdConj.directStd3Pers("-skriukš, pag. -skriukšēja", "skriukšēt"), //skriukšēt
		ThirdConj.directStd3Pers("-skriukšķ, pag. -skriukšķēja", "skriukšķēt"), //skriukšķēt
		ThirdConj.directStd3Pers("-sparkst, pag. -sparkstēja", "sparkstēt"), //sparkstēt
		ThirdConj.directStd3Pers("-sparkš, pag. -sparkšēja", "sparkšēt"), //nosparkšēt
		ThirdConj.directStd3Pers("-sparkšķ, pag. -sparkšķēja", "sparkšķēt"), //nosparkšķēt
		ThirdConj.directStd3Pers("-sprakst, pag. -sprakstēja", "sprakstēt"), //aizsprakstēt
		ThirdConj.directStd3Pers("-sprakš, pag. -sprakšēja", "sprakšēt"), //aizsprakšēt
		ThirdConj.directStd3Pers("-sprakšķ, pag. -sprakšķēja", "sprakšķēt"), //aizsprakšķēt
		ThirdConj.directStd3Pers("-spraukst, pag. -spraukstēja", "spraukstēt"), //nospraukstēt
		ThirdConj.directStd3Pers("-spraukš, pag. -spraukšēja", "spraukšēt"), //nospraukšēt
		ThirdConj.directStd3Pers("-spraukšķ, pag. -spraukšķēja", "spraukšķēt"), //nospraukšķēt
		ThirdConj.directStd3Pers("-stinkš, pag. -stinkšēja", "stinkšēt"), //stinkšēt
		ThirdConj.directStd3Pers("-stinkšķ, pag. -stinkšķēja", "stinkšķēt"), //stinkšķēt
		ThirdConj.directStd3Pers("-stirkst, pag. -stirkstēja", "stirkstēt"), //stirkstēt
		ThirdConj.directStd3Pers("-stirkš, pag. -stirkšēja", "stirkšēt"), //nosptirkšēt
		ThirdConj.directStd3Pers("-stirkšķ, pag. -stirkšķēja", "stirkšķēt"), //nostirkšķēt
		ThirdConj.directStd3Pers("-strikš, pag. -strikšēja", "strikšēt"), //strikšēt
		ThirdConj.directStd3Pers("-strikšķ, pag. -strikšķēja", "strikšķēt"), //strikšķēt
		ThirdConj.directStd3Pers("-strinkš, pag. -strinkšēja", "strinkšēt"), //nostrinkšēt
		ThirdConj.directStd3Pers("-strinkšķ, pag. -strinkšķēja", "strinkšķēt"), //nostrinkšķēt
		ThirdConj.directStd3Pers("-sus, pag. -susēja", "susēt"), //apsusēt
		ThirdConj.directStd3Pers("-sūb, pag. -sūbēja", "sūbēt"), //piesūbēt
		ThirdConj.directStd3Pers("-sūkst, pag. -sūkstēja", "sūkstēt"), //sūkstēt
		ThirdConj.directStd3Pers("-sūrkst, pag. -sūrkstēja", "sūrkstēt"), //sūrkstēt
		ThirdConj.directStd3Pers("-sūrst, pag. -sūrstēja", "sūrstēt"), //nosūrstēt
		// Š
		ThirdConj.directStd3Pers("-šļakst, pag. -šļakstēja", "šļakstēt"), //aizšļakstēt
		ThirdConj.directStd3Pers("-šļankst, pag. -šļankstēja", "šļankstēt"), //šļankstēt
		ThirdConj.directStd3Pers("-šļaukst, pag. -šļaukstēja", "šļaukstēt"), //šļaukstēt
		ThirdConj.directStd3Pers("-šļirkst, pag. -šļirkstēja", "šļirkstēt"), //šļirkstēt
		ThirdConj.directStd3Pers("-šļurkst, pag. -šļurkstēja", "šļurkstēt"), //šļurkstēt
		ThirdConj.directStd3Pers("-šmakst, pag. -šmakstēja", "šmakstēt"), //nošmakstēt
		ThirdConj.directStd3Pers("-šmaukst, pag. -šmaukstēja", "šmaukstēt"), //nošmaukstēt
		ThirdConj.directStd3Pers("-šmīkst, pag. -šmīkstēja", "šmīkstēt"), //nošmīkstēt
		ThirdConj.directStd3Pers("-šmiukst, pag. -šmiukstēja", "šmiukstēt"), //nošmiukstēt
		ThirdConj.directStd3Pers("-šmuikst, pag. -šmuikstēja", "šmuikstēt"), //šmuikstēt
		ThirdConj.directStd3Pers("-šmurkst, pag. -šmurkstēja", "šmurkstēt"), //šmurkstēt
		ThirdConj.directStd3Pers("-šmūkst, pag. -šmūkstēja", "šmūkstēt"), //nošmūkstēt
		ThirdConj.directStd3Pers("-šņakst, pag. -šņakstēja", "šņakstēt"), //nošņakstēt
		ThirdConj.directStd3Pers("-šņarkst, pag. -šņarkstēja", "šņarkstēt"), //šņarkstēt
		ThirdConj.directStd3Pers("-šņerkst, pag. -šņerkstēja", "šņerkstēt"), //nošņerkstēt
		ThirdConj.directStd3Pers("-šņikst, pag. -šņikstēja", "šņikstēt"), //šņikstēt
		ThirdConj.directStd3Pers("-šņirkst, pag. -šņirkstēja", "šņirkstēt"), //nošņirkstēt
		ThirdConj.directStd3Pers("-švīkst, pag. -švīkstēja", "švīkstēt"), //aizšvīkstēt
		// T
		ThirdConj.directStd3Pers("-takš, pag. -takšēja", "takšēt"), //takšēt
		ThirdConj.directStd3Pers("-takšķ, pag. -takšķēja", "takšķēt"), //takšķēt
		ThirdConj.directStd3Pers("-tankš, pag. -tankšēja", "tankšēt"), //tankšēt
		ThirdConj.directStd3Pers("-tankšķ, pag. -tankšķēja", "tankšķēt"), //tankšķēt
		ThirdConj.directStd3Pers("-tikš, pag. -tikšēja", "tikšēt"), //aiztikšēt
		ThirdConj.directStd3Pers("-tikšķ, pag. -tikšķēja", "tikšķēt"), //aiztikšķēt
		ThirdConj.directStd3Pers("-tinkš, pag. -tinkšēja", "tinkšēt"), //aiztinkšēt
		ThirdConj.directStd3Pers("-tinkšķ, pag. -tinkšķēja", "tinkšķēt"), //aiztinkšķēt
		ThirdConj.directStd3Pers("-tirkš, pag. -tirkšēja", "tirkšēt"), //notirkšēt
		ThirdConj.directStd3Pers("-tirkšķ, pag. -tirkšķēja", "tirkšķēt"), //notirkšķēt
		ThirdConj.directStd3Pers("-tramš, pag. -tramšēja", "tramšēt"), //tramšēt
		ThirdConj.directStd3Pers("-tramšķ, pag. -tramšķēja", "tramšķēt"), //tramšķēt
		ThirdConj.directStd3Pers("-trankš, pag. -trankšēja", "trankšēt"), //notrankšēt
		ThirdConj.directStd3Pers("-trankšķ, pag. -trankšķēja", "trankšķēt"), //notrankšķēt
		ThirdConj.directStd3Pers("-trinkš, pag. -trinkšēja", "trinkšēt"), //notrinkšēt
		ThirdConj.directStd3Pers("-trinkšķ, pag. -trinkšķēja", "trinkšķēt"), //notrinkšķēt
		ThirdConj.directStd3Pers("-trun, pag. -trunēja", "trunēt"), //aiztrunēt
		ThirdConj.directStd3Pers("-trup, pag. -trupēja", "trupēt"), //aiztrupēt
		ThirdConj.directStd3Pers("-trus, pag. -trusēja", "trusēt"), //ietrusēt
		ThirdConj.directStd3Pers("-trūd, pag. -trūdēja", "trūdēt"), //aptrūdēt
		ThirdConj.directStd3Pers("-tvan, pag. -tvanēja", "tvanēt"), //iztvanēt
		// U
		ThirdConj.directStd3Pers("-urd, pag. -urdēja", "urdēt"), //urdēt
		ThirdConj.directStd3Pers("-urdz, pag. -urdzēja", "urdzēt"), //aizurdzēt
		// Ū
		ThirdConj.directStd3Pers("-ūkš, pag. -ūkšēja", "ūkšēt"), //ūkšēt
		ThirdConj.directStd3Pers("-ūkšķ, pag. -ūkšķēja", "ūkšķēt"), //ūkšķēt
		// V
		ThirdConj.directStd3Pers("-vankš, pag. -vankšēja", "vankšēt"), //vankšēt
		ThirdConj.directStd3Pers("-vankšķ, pag. -vankšķēja", "vankšķēt"), //vankšķēt
		ThirdConj.directStd3Pers("-vaukš, pag. -vaukšēja", "vaukšēt"), //novaukšēt
		ThirdConj.directStd3Pers("-vaukšķ, pag. -vaukšķēja", "vaukšķēt"), //novaukšķēt
		ThirdConj.directStd3Pers("-vidž, pag. -vidžēja", "vidžēt"), //vidžēt
		ThirdConj.directStd3Pers("-virkš, pag. -virkšēja", "virkšēt"), //virkšēt
		ThirdConj.directStd3Pers("-virkšķ, pag. -virkšķēja", "virkšķēt"), //novirkšķēt
		ThirdConj.directStd3Pers("-viz, pag. -vizēja", "vizēt"), //novizēt
		// Z
		ThirdConj.directStd3Pers("-zad, pag. -zadēja", "zadēt"), //zadēt
		ThirdConj.directStd3Pers("-zimz, pag. -zimzēja", "zimzēt"), //zimzēt
		ThirdConj.directStd3Pers("-zlarkš, pag. -zlarkšēja", "zlarkšēt"), //nozlarkšēt
		ThirdConj.directStd3Pers("-zlarkšķ, pag. -zlarkšķēja", "zlarkšķēt"), //nozlarkšķēt
		ThirdConj.directStd3Pers("-zum, pag. -zumēja", "zumēt"), //zumēt
		ThirdConj.directStd3Pers("-zuz, pag. -zuzēja", "zuzēt"), //aizzuzēt
		// Ž
		ThirdConj.directStd3Pers("-žļadz, pag. -žļadzēja", "žļadzēt"), //žļadzēt
		ThirdConj.directStd3Pers("-žļakst, pag. -žļakstēja", "žļakstēt"), //nožļakstēt
		ThirdConj.directStd3Pers("-žļankst, pag. -žļankstēja", "žļankstēt"), //žļankstēt
		ThirdConj.directStd3Pers("-žļarkst, pag. -žļarkstēja", "žļarkstēt"), //nožļarkstēt
		ThirdConj.directStd3Pers("-žļerkst, pag. -žļerkstēja", "žļerkstēt"), //nožļerkstēt
		ThirdConj.directStd3Pers("-žļirkst, pag. -žļirkstēja", "žļirkstēt"), //nožļirkstēt
		ThirdConj.directStd3Pers("-žļurkst, pag. -žļurkstēja", "žļurkstēt"), //nožļurkstēt
		ThirdConj.directStd3Pers("-žņerkst, pag. -žņerkstēja", "žņerkstēt"), //nožņerkstēt
		ThirdConj.directStd3Pers("-žņirkst, pag. -žņirkstēja", "žņirkstēt"), //nožņirkstēt
		ThirdConj.directStd3Pers("-žvadz, pag. -žvadzēja", "žvadzēt"), //aizžvadzēt
		ThirdConj.directStd3Pers("-žvakst, pag. -žvakstēja", "žvakstēt"), //nožvakstēt
		ThirdConj.directStd3Pers("-žvankst, pag. -žvankstēja", "žvankstēt"), //nožvankstēt
		ThirdConj.directStd3Pers("-žvarkst, pag. -žvarkstēja", "žvarkstēt"), //nožvarkstēt
		ThirdConj.directStd3Pers("-žvaukst, pag. -žvaukstēja", "žvaukstēt"), //nožvaukstēt
		ThirdConj.directStd3Pers("-žvākst, pag. -žvākstēja", "žvākstēt"), //nožvākstēt
		ThirdConj.directStd3Pers("-žvindz, pag. -žvindzēja", "žvindzēt"), //žvindzēt
		ThirdConj.directStd3Pers("-žvinkst, pag. -žvinkstēja", "žvinkstēt"), //aizžvinkstēt
		ThirdConj.directStd3Pers("-žvirkst, pag. -žvirkstēja", "žvirkstēt"), //nožvirkstēt
		ThirdConj.directStd3Pers("-žviukst, pag. -žviukstēja", "žviukstēt"), //žviukstēt
		ThirdConj.directStd3Pers("-žvīkst, pag. -žvīkstēja", "žvīkstēt"), //nožvīkstēt

		// Likumi daudzskaitļa formām.
		ThirdConj.directChangePlural("-tekam, -tekat, -tek, pag. -tecējām", "tecēt"), //satecēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final EndingRule[] directMultiConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Šobrīd sarezgitās struktūras dēļ 3. personas likums netiek atvasinats.
			// TODO uztaisīt verbu likumu, kas atvasina 3. personas likumu.
		SecondThirdConj.directStdAllPersParallel(
				"-bedīju, -bedī, -bedī, arī -bedu, -bedi, -beda, pag. -bedīju", "bedīt"), // apbedīt
		SecondThirdConj.directStdAllPersParallel(
				"-bezdu, -bezdi, -bezd, arī -bezdēju, -bezdē, -bezdē, pag. -bezdēju", "bezdēt"), // aizbezdēt
		SecondThirdConj.directStdAllPersParallel(
				"-brūnu, -brūni, -brūn, arī -brūnēju, -brūnē, -brūnē, pag. -brūnēju", "brūnēt"), // nobrūnēt
		SecondThirdConj.directStdAllPersParallel(
				"-ceru, -ceri, -cer, retāk -cerēju, -cerē, -cerē, pag. -cerēju", "cerēt"), // apcerēt
		SecondThirdConj.directStdAllPersParallel(
				"-cienu, -cieni, -ciena, arī -cienīju, -cienī, -cienī, pag. -cienīju", "cienīt"), // iecienīt
		SecondThirdConj.directStdAllPersParallel(
				"-dēstu, -dēsti, -dēsta, retāk -dēstīju, -dēstī, -dēstī, pag. -dēstīju", "dēstīt"), // apdēstīt
		SecondThirdConj.directStdAllPersParallel(
				"-klusēju, -klusē, -klusē, pag. -klusēju, retāk -klusu, -klusi, -klus, pag. -klusēju", "klusēt"), // paklusēt, klusēt
		SecondThirdConj.directStdAllPersParallel(
				"-kristīju, -kristī, -kristī, arī -kristu, -kristi, -krista, pag. -kristīju", "kristīt"), // iekristīt
		SecondThirdConj.directStdAllPersParallel(
				"-krustīju, -krustī, -krustī, arī -krustu, -krusti, -krusta, pag. -krustīju", "krustīt"), // iekrustīt
		SecondThirdConj.directStdAllPersParallel(
				"-kveldēju, -kveldē, -kveldē, arī -kveldu, -kveldi, -kveld, pag. -kveldēju", "kveldēt"), // nokveldēt
		SecondThirdConj.directStdAllPersParallel(
				"-kvēlu, -kvēli, -kvēl, retāk -kvēlēju, -kvēlē, -kvēlē, pag. -kvēlēju", "kvēlēt"), // kvēlēt
		SecondThirdConj.directStdAllPersParallel(
				"-ķēzīju, -ķēzī, -ķēzī, arī -ķēzu, -ķēzi, -ķēza, pag. -ķēzīju", "ķēzīt"), // apķēzīt
		SecondThirdConj.directStdAllPersParallel(
				"-lāsu, -lāsi, -lās, retāk -lāsēju, -lāsē, -lāsē, pag. -lāsēju", "lāsēt"), // nolāsēt
		SecondThirdConj.directStdAllPersParallel(
				"-mērīju, -mērī, -mērī, arī -mēru, -mēri, -mēra, pag. -mērīju", "mērīt"), // atmērīt
		SecondThirdConj.directStdAllPersParallel(
				"-pelnu, -pelni, -pelna, arī -pelnīju, -pelnī, -pelnī, pag. -pelnīju", "pelnīt"), // atpelnīt
		SecondThirdConj.directStdAllPersParallel(
				"-pētīju, -pētī, -pētī, arī -pētu, -pēti, -pēta, pag. -pētīju", "pētīt"), // appētīt
		SecondThirdConj.directStdAllPersParallel(
				"-pūlu, -pūli, -pūl, arī -pūlēju, -pūlē, -pūlē, pag. -pūlēju", "pūlēt"), // nopūlēt
		SecondThirdConj.directStdAllPersParallel(
				"-rotu, -roti, -rota, arī -rotīju, -roti, -rotī, pag. -rotīju",
				"rotīt"), // aizrotīt
		SecondThirdConj.directStdAllPersParallel(
				"-sargāju, -sargā, -sargā, arī -sargu, -sargi, -sarga, pag. -sargāju", "sargāt"), // aizsargāt
		SecondThirdConj.directStdAllPersParallel(
				"-svētīju, -svētī, -svētī, arī -svētu, -svēti, -svēta, pag. -svētīju", "svētīt"), // aizsargāt
		SecondThirdConj.directStdAllPersParallel(
				"-tašķīju, -tašķī, -tašķī, arī -tašķu, -tašķi, -tašķa, pag. -tašķīju", "tašķīt"), // aptašķīt
		SecondThirdConj.directStdAllPersParallel(
				"-veltīju, -veltī, -veltī, arī -veltu, -velti, -velta, pag. -veltīju", "veltīt"), // apveltīt
		SecondThirdConj.directStdAllPersParallel(
				"-vētīju, -vētī, -vētī, arī -vētu, -vēti, -vēta, pag. -vētīju", "vētīt"), // aizvētīt

		// TODO vai tiešām nav saskaņojams ne ar vienu priedēkļa likumu?
		SecondThirdConj.directStdAllPersParallel(
				"-bālu, -bāli, -bāl, arī -bālēju, -bālē, -bālē, pag. -bālēju", "bālēt"), // bālēt
		SecondThirdConj.directStdAllPersParallel(
				"-durnu, -durni, -durn, arī -durnēju, -durnē, -durnē, pag. -durnēju", "durnēt"), // durnēt
		SecondThirdConj.directStdAllPersParallel(
				"-kluknu, -klukni, -klukn, arī -kluknēju, -kluknē, -kluknē, pag. -kluknēju", "kluknēt"), // kluknēt
		SecondThirdConj.directStdAllPersParallel(
				"-kruknu, -krukni, -krukn, arī -kruknēju, -kruknē, -kruknē, pag. -kruknēju", "kruknēt"), // kruknēt
		SecondThirdConj.directStdAllPersParallel(
				"-kuknu, -kukni, -kukn, arī -kuknēju, -kuknē, -kuknē, pag. -kuknēju", "kuknēt"), // kuknēt
		SecondThirdConj.directStdAllPersParallel(
				"-pleknu, -plekni, -plekn, arī -pleknēju, -pleknē, -pleknē, pag. -pleknēju", "pleknēt"), // pleknēt
		SecondThirdConj.directStdAllPersParallel(
				"-vēdīju, -vēdī, -vēdī, arī -vēdu, -vēdi, -vēda, pag. vēdīju", "vēdīt"), //vēdīt

		// Paralēlformu paralēlforma.
		SecondThirdConj.directStdAllPersParallel(
				"-bālu, -bāli, -bāl, arī -bālēju, -bālē, -balē, pag. -bālēju (arī -bālu, 1. konj.)",
				"bālēt"), //nobālēt
		SecondThirdConj.directStdAllPersParallel(
				"-bālēju, -bālē, -bālē, arī -bālu, -bāli, -bāl, pag. -bālēju (retāk -bālu, 1. konj.)",
				"bālēt"), //sabālēt
		SecondThirdConj.directStdAllPersParallel(
				"-rūsēju, -rūsē, -rūsē, arī -rūsu, -rūsi, -rūs, pag. -rūsēju (retāk -rūsu, 1. konj.)",
				"rūsēt"), // ierūsēt

		// Likumi, kam ir "parasti 3. pers." variants.
		// Paralēlformu paralēlforma
		SecondThirdConj.directStd3PersParallel(
				"-rūsē, arī -rūs, pag. -rūsēja (retāk -rūsa, 1. konj.)", "rūsēt"), // aizsūsēt

		// Standartizētie
		// A, B
		SecondThirdConj.directStd3PersParallel(
				"-balē, arī -bal, pag. -balēja", "balēt"), // apbalēt
		SecondThirdConj.directStd3PersParallel(
				"-bāl, arī -bālē, pag. -bālēja", "bālēt"), // iebālēt
		SecondThirdConj.directStd3PersParallel(
				"-brūn, arī -brūnē, pag. -brūnēja", "brūnēt"), // iebrūnēt
		SecondThirdConj.directStd3PersParallel(
				"-būb, arī -būbē, pag. -būbēja", "būbēt"), // nobūbēt
		// C, D
		SecondThirdConj.directStd3PersParallel(
				"-dim, arī -dimē, pag. -dimēja", "dimēt"), // nodimēt
		// E, F, G
		SecondThirdConj.directStd3PersParallel(
				"-gail, arī -gailē, pag. -gailēja", "gailēt"), // izgailēt
		SecondThirdConj.directStd3PersParallel(
				"-galdē, arī -gald, pag. -galdēja", "galdēt"), // sagaldēt
		// H, I, J
		SecondThirdConj.directStd3PersParallel(
				"-junda, arī -jundī, pag. -jundīja", "jundīt"), // iejundīt
		// K
		SecondThirdConj.directStd3PersParallel(
				"-kveldē, arī -kveld, pag. -kveldēja", "kveldēt"), // pakveldēt
		// L
		SecondThirdConj.directStd3PersParallel(
				"-lās, retāk -lāsē, pag. -lāsēja", "lāsēt"), // lāsēt
		// M, N, O, P
		SecondThirdConj.directStd3PersParallel(
				"-melnē, retāk -meln, pag. -melnēja", "melnēt"), // melnēt
		SecondThirdConj.directStd3PersParallel(
				"-pelē, arī -pel, pag. -pelēja", "pelēt"), // aizpelēt
		SecondThirdConj.directStd3PersParallel(
				"-plekn, arī -pleknē, pag. -pleknēja", "pleknēt"), // sapleknēt
		SecondThirdConj.directStd3PersParallel(
				"-plēn, arī -plēnē, pag. -plēnēja", "plēnēt"), // applēnēt
		// R
		SecondThirdConj.directStd3PersParallel(
				"-rec, arī -recē, pag. -recēja", "recēt"), // sarecēt
		SecondThirdConj.directStd3PersParallel(
				"-rec, retāk -recē, pag. -recēja", "recēt"), // ierecēt
		// S
		SecondThirdConj.directStd3PersParallel(
				"-sūb, arī -sūbē, pag. -sūbēja", "sūbēt"), // aizsūbēt
		SecondThirdConj.directStd3PersParallel(
				"-sus, arī -susē, pag. -susēja", "susēt"), // izsusēt
		// T, U, V, Z
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
	 */
	public static final EndingRule[] reflFirstConjVerb = {
		/// Pēteris solīja, ka tas strādās.
		VerbDoubleRule.of("-ejos, -ejies,", "-ietas, pag. -gājos", "ieties", 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"iet\""), TFeatures.POS__IRREG_VERB, TFeatures.POS__REFL_VERB}, null,
				FirstConjStems.of("ie", "ej", "gāj")), //apieties

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
				"\"misties\" (krist izmisumā)"), //izmisties // Liekas, ka otra vārdnīcā nav.
		FirstConj.reflHomof("-minos, -minies,", "-minas, pag. -minos", "mīties",
				"\"mīties\" (pedāļus)"), //aizmīties
		FirstConj.reflHomof("-mijos, -mijies,", "-mijas, pag. -mijos", "mīties",
				"\"mīties\" (mainīt naudu)"), //apmīties
		FirstConj.reflHomof("-tiekos, -tiecies,", "-tiekas, pag. -tikos", "tikties",
				"\"tikties\" (satikties ar kādu)"), //satikties
		FirstConj.reflHomof("-tīkos, -tīcies,", "-tīkas, pag. -tikos", "tikties",
				"\"tikties\" (patikties kādam)"), //tikties, patikties
		FirstConj.reflHomof("-vēršos, -vērsies,", "-vēršas, pag. -vērsos", "vērsties",
				"\"vērsties\" (mainīt virzienu)"), //aizvērsties, izvērsties 1
		FirstConj.reflHomof("-vēršos, -vērties,", "-vēršas, pag. -vērtos", "vērsties",
				"\"vērsties\" (mainīt būtību)"), //izvērsties 2

		// Izņēmums.
		VerbDoubleRule.of("-patīkos, -patīcies,", "-patīkas, pag. -patikos", "patikties", 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"tikties\" (patikties kādam)"),
						TFeatures.INFINITIVE_HOMOFORMS, TFeatures.POS__REFL_VERB}, null,
				FirstConjStems.of("tik", "tīk", "tik")), //iepatikties

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
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"zīties\""), TFeatures.POS__REFL_VERB}, null,
				FirstConjStems.of("zī", "zīst", "zin")), //iepazīties
		VerbDoubleRule.of("-sastopos, -sastopies,", "-sastopas, pag. -sastapos", "sastapties", 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"stapties\""), TFeatures.POS__REFL_VERB}, null,
				FirstConjStems.of("stap", "stop", "stap")), //sastapties

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
		FirstConj.refl("-božos, -bozies,", "-božas, pag. -bozos", "bozties"), //pabozties
		FirstConj.refl("-braucos, -braucies,", "-braucas, pag. -braucos", "braukties"), //izbraukties
		FirstConj.refl("-brāžos, -brāzies,", "-brāžas, pag. -brāzos", "brāzties"), //aizbrāzties
		FirstConj.refl("-brienos, -brienies,", "-brienas, pag. -bridos", "bristies"), //atbristies
		FirstConj.refl("-buros, -buries,", "-buras, pag. -būros", "burties"), //izburties
		// C
		FirstConj.refl("-ceļos, -celies,", "-ceļas, pag. -cēlos", "celties"), //apcelties
		FirstConj.refl("-cenšos, -centies,", "-cenšas, pag. -centos", "censties"), //pacensties
		FirstConj.refl("-cepos, -cepies,", "-cepas, pag. -cepos", "cepties"), //sacepties
		FirstConj.refl("-ciešos, -cieties,", "-ciešas, pag. -cietos", "ciesties"), //aizciesties
		FirstConj.refl("-cērtos, -cērties,", "-cērtas, pag. -cirtos", "cirsties"), //aizcirsties
		// D
		FirstConj.refl("-dējos, -dējies,", "-dējas, pag. -dējos", "dēties"), //dēties
		FirstConj.refl("-dejos, -dejies,", "-dejas, pag. -dējos", "dieties"), //izdieties
		FirstConj.refl("-diršos, -dirsies,", "-diršas, pag. -dirsos", "dirsties"), //apdirsties
		FirstConj.refl("-dodos, -dodies,", "-dodas, pag. -devos", "doties"), //atdoties
		FirstConj.refl("-drāžos, -drāzies,", "-drāžas, pag. -drāzos", "drāzties"), //aizdrāzties
		FirstConj.refl("-duros, -duries,", "-duras, pag. -dūros", "durties"), //aizdurties
		FirstConj.refl("-dzeļos, -dzelies,", "-dzeļas, pag. -dzēlos", "dzelties"), //sadzelties
		FirstConj.refl("-dzeros, -dzeries,", "-dzeras, pag. -dzēros", "dzerties"), //apdzerties
		FirstConj.refl("-dzēšos, -dzēsies,", "-dzēšas, pag. -dzēsos", "dzēsties"), //izdzēsties
		FirstConj.refl("-dziros, -dziries,", "-dziras, pag. -dzīros", "dzirties"), //dzirties
		// E
		FirstConj.refl("-ēdos, -ēdies,", "-ēdas, pag. -ēdos", "ēsties"), //atēsties
		// F, G
		FirstConj.refl("-gaužos, -gaudies,", "-gaužas, pag. -gaudos", "gausties"), //izgausties
		FirstConj.refl("-gāžos, -gāzies,", "-gāžas, pag. -gāzos", "gāzties"), //apgāzties, aizgāzties
		FirstConj.refl("-glaužos, -glaudies,", "-glaužas, pag. -glaudos", "glausties"), //ieglausties
		FirstConj.refl("-glābjos, -glābies,", "-glābjas, pag. -glābos", "glābties"), //izglābties
		FirstConj.refl("-graužos, -grauzies,", "-graužas, pag. -grauzos", "grauzties"), //izgrauzties, aizgrauzties
		FirstConj.refl("-gremjos, -gremies,", "-gremjas, pag. -gremos", "gremties"), //gremties
		FirstConj.refl("-gremžos, -gremdies,", "-gremžas, pag. -gremdos", "gremsties"), //izgremsties
		FirstConj.refl("-gremžos, -gremzies,", "-gremžas, pag. -gremzos", "gremzties"), //izgremzties
		FirstConj.refl("-griežos, -griezies,", "-griežas, pag. -griezos", "griezties"), //aizgriezties 1, 2
		FirstConj.refl("-grūžos, -grūdies,", "-grūžas, pag. -grūdos", "grūsties"), //atgrūsties
		FirstConj.refl("-gūstos, -gūsties,", "-gūstas, pag. -guvos", "gūties"), //aizgūties
		// Ģ
		FirstConj.refl("-ģērbjos, -ģērbies,", "-ģērbjas, pag. -ģērbos", "ģērbties"), //apģērbties
		// H, I, Ī, J
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
		FirstConj.refl("-kremšos, -kremties,", "-kremšas, pag. -kremtos", "kremsties"), //kremsties
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
		FirstConj.refl("-mīžos, -mīzies,", "-mīžas, pag. -mīzos", "mīzties"), //apmīzties
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
		FirstConj.refl("-piržos, -pirdies,", "-piržas, pag. -pirdos", "pirsties"), // appirsties
		FirstConj.refl("-pišos, -pisies,", "-pišas, pag. -pisos", "pisties"), // aizpisties
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
		FirstConj.refl("-raucos, -raucies,", "-raucas, pag. -raucos", "raukties"), //raukties, paraukties
		FirstConj.refl("-raušos, -rausies,", "-raušas, pag. -rausos", "rausties"), //aizrausties
		FirstConj.refl("-raujos, -raujies,", "-raujas, pag. -rāvos", "rauties"), //aizrauties
		FirstConj.refl("-rāpjos, -rāpies,", "-rāpjas, pag. -rāpos", "rāpties"), // aizrāpties
		FirstConj.refl("-rājos, -rājies,", "-rājas, pag. -rājos", "rāties"), // izrāties
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
		FirstConj.refl("-sniecos, -sniecies,", "-sniecas, pag. -sniecos", "sniekties"), //sniekties
		FirstConj.refl("-speros, -speries,", "-speras, pag. -spēros", "sperties"), //atsperties
		FirstConj.refl("-spiežos, -spiedies,", "-spiežas, pag. -spiedos", "spiesties"), //aizspiesties
		FirstConj.refl("-spraucos, -spraucies,", "-spraucas, pag. -spraucos", "spraukties"), //aizspraukties
		FirstConj.refl("-spraužos, -spraudies,", "-spraužas, pag. -spraudos", "sprausties"), //aizsprausties
		FirstConj.refl("-spriežos, -spriedies,", "-spriežas, pag. -spriedos", "spriesties"), //aizspriesties
		FirstConj.refl("-stājos, -stājies,", "-stājas, pag. -stājos", "stāties"), //aizstāties
		FirstConj.refl("-steidzos, -steidzies,", "-steidzas, pag. -steidzos", "steigties"), //aizsteigties
		FirstConj.refl("-stiepjos, -stiepies,", "-stiepjas, pag. -stiepos", "stiepties"), //aizstiepties
		FirstConj.refl("-stilstos, -stilsties,", "-stilstas, pag. -stilos", "stilties"), //stilties
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
		FirstConj.refl("-šķietos, -šķieties,", "-šķietas, pag. -šķitos", "šķisties"), //šķisties
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
		FirstConj.refl("-trencos, -trencies,", "-trencas, pag. -trencos", "trenkties"), //trenkties
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
		FirstConj.refl3PersHomof("-viešas, pag. -viesās", "viesties",
				"\"viesties\" (pakāpeniski veidoties)"), // ieviesties
		FirstConj.refl3PersHomof("viežas, pag. viedās", "viesties",
				"\"viesties\" (būt neskaidri jūtamam)"), // viesties 2

			// TODO: sakārtot "riest" un "riesties"
		FirstConj.refl3PersParallelHomof(
				"-riešas, pag. -riesās, arī -rietās", "riesties", "\"riesties\" (parastais)"), //aizriesties
		FirstConj.refl3PersHomof("-riešas, pag. -rietās", "riesties", "\"riesties\" (parastais)"), // ieriesties 1

		// Paralēlās formas.
		FirstConj.refl3PersParallel("-jaušas, pag. -jautās, arī -jaužas, pag. -jaudās", "jausties"), //jausties
		FirstConj.refl3PersParallel("-jumjas, pag. -jūmās, arī -jumās", "jumties"), //jumties
		FirstConj.refl3PersParallel("-nīkas, retāk -nīkstas, pag. -nikās", "nikties"), //apnikties
		FirstConj.refl3PersParallel("-plešas, pag. -pletās, arī -plētās", "plesties"), //aizplesties

		// Standarts
		// A, B, C, D
		FirstConj.refl3Pers("-derdzas, pag. -derdzās", "dergties"), // dergties
		// E, F, G
		FirstConj.refl3Pers("-graujas, pag. -grāvās", "grauties"), // iegrauties
		// H, I, J, K
		FirstConj.refl3Pers("-knābjas, pag. -knābās", "knābties"), // knābties
		FirstConj.refl3Pers("-kremtas, pag. -krimtās", "krimsties"), // krimsties
		FirstConj.refl3Pers("-kurcas, pag. -kurcās", "kurkties"), // iekurkties
		FirstConj.refl3Pers("-kuras, pag. -kūrās", "kurties"), // iekurties
		// L, M
		FirstConj.refl3Pers("-maujas, pag. -māvās", "mauties"), // atmauties
		// N, O, P
		FirstConj.refl3Pers("-paužas, pag. -paudās", "pausties"), //izpausties
		FirstConj.refl3Pers("-plokas, pag. -plakās", "plakties"), //plakties
		// R
		FirstConj.refl3Pers("-riežas, pag. -riezās", "riezties"), //ieriezties
		// S
		FirstConj.refl3Pers("-sākas, pag. -sākās", "sākties"), //aizsākties
		FirstConj.refl3Pers("-sokas, pag. -secās", "sekties"), //sekties
		FirstConj.refl3Pers("-skrienas, pag. -skrējās", "skrieties"), //apskrieties
		FirstConj.refl3Pers("-slaucas, pag. -slaucās", "slaukties"), //atslaukties
		FirstConj.refl3Pers("-spriedzas, pag. -spriedzās", "spriegties"), //saspriegties
		// Š
		FirstConj.refl3Pers("-šķiļas, pag. -šķīlās", "šķilties"), //aizšķilties
		// T, U, V
		FirstConj.refl3Pers("-vāžas, pag. -vāzās", "vāzties"), //uzvāzties
		FirstConj.refl3Pers("-veicas, pag. -veicās", "veikties"), //izveikties
		FirstConj.refl3Pers("-velbjas, pag. -velbās", "velbties"), //izvelbties
		// Z, Ž
		FirstConj.refl3Pers("-žmaudzas, pag. -žmaudzās", "žmaugties"), //iežmaugties

		// Daudzskaitļa formu likumi
		FirstConj.reflAllPersPlural("-brāžamies, -brāžaties, -brāžas, pag. -brāzāmies", "brāzties"), // sabrāzties
		FirstConj.reflAllPersPlural("-kaujamies, -kaujaties, -kaujas, pag. -kāvāmies", "kauties"), // apkauties
		FirstConj.reflAllPersPlural("-liekamies, -liekaties, -liekas, pag. -likāmies", "likties"), // salikties
		FirstConj.reflAllPersPlural("-rodamies, -rodaties, -rodas, pag. -radāmies", "rasties"), // sarasties
		FirstConj.reflAllPersPlural("-raušamies, -raušaties, -raušas, pag. -rausāmies", "rausties"), // sarausties
		FirstConj.reflAllPersPlural("-rāpjamies, -rāpjaties, -rāpjas, pag. -rāpāmies", "rāpties"), // sarāpties
		FirstConj.reflAllPersPlural("-spraucamies, -spraucaties, -spraucas, pag. -spraucāmies", "spraukties"), // saspraukties
		FirstConj.reflAllPersPlural("-spriežamies, -spriežaties, -spriežas, pag. -spriedāmies", "spriesties"), // saspriesties
		FirstConj.reflAllPersPlural("-traušamies, -traušaties, -traušas, pag. -trausāmies", "trausties"), // satrausties
		FirstConj.reflAllPersPlural("-tupstamies, -tupstaties, -tupstas, pag. -tupāmies", "tupties"), // satupties

		FirstConj.reflPluralOr3PersParallel(
				"-sēžamies, arī -sēstamies, pag. -sēdāmies vai vsk. 3. pers.,", "-sēžas, arī -sēstas, pag. -sēdās",
				"sēsties"), // izsēsties

		// Pilnīgs nestandarts.
		VerbDoubleRule.of("-teicos, -teicies,", "-teicas (tagadnes formas parasti nelieto), pag. -teicos",
				"teikties", 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"teikties\""), TFeatures.POS__REFL_VERB},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NO_PRESENT)},
				FirstConjStems.of("teik", "teic", "teic")), //atteikties

		VerbDoubleRule.of("parasti dsk. 3. pers., -sveicas, pag. -sveicās", null,
				"sveikties", 18,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"sveikties\""), TFeatures.POS__REFL_VERB},
				new Tuple[]{TFeatures.USUALLY_USED__PLURAL, TFeatures.USUALLY_USED__THIRD_PERS},
				FirstConjStems.of("sveik", "sveic", "sveic")), // apsveikties


	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 19: Darbības vārdi 2. konjugācija atgriezeniski
	 */
	public static final EndingRule[] reflSecondConjVerb = {
			SecondConj.refl(
					"-knābājos, -knābājies,", "-knābājas, pag. -knābājos", "knābāties"), // pieknābāties
			SecondConj.refl(
					"-tusējos, -tusējies,", "-tusējas, pag. -tusējos", "tusēties"), // notusēties

			SecondConj.refl3Pers("-plandas, pag. -plandējās", "plandēties"), // noplandēties
			SecondConj.refl3Pers("-verkšījas, pag. -verkšījās", "verkšīties"), // saverkšīties
			SecondConj.refl3Pers("-verkšķījas, pag. -verkšķījās", "verkšķīties"), // saverkšķīties
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
	 */
	public static final EndingRule[] reflThirdConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Paralēlās formas.
		ThirdConj.reflOptChangeAllPersParallel(
				"-ļogos, -ļogies, -ļogās, retāk -ļodzos, -ļodzies, -ļodzās, pag. -ļodzījos", "ļodzīties"), //noļodzīties
		ThirdConj.reflOptChangeAllPersParallel(
				"-mokos, -mokies, -mokās, arī -mocos, -mocies, -mocās, pag. -mocījos", "mocīties"), //aizmocīties

		// Standartizētie ar mijām.
		ThirdConj.reflChange("-braukos, -braukies,", "-braukās, pag. -braucījos", "braucīties"), //atbraucīties
		ThirdConj.reflChange("-lokos, -lokies,", "-lokās, pag. -locījos", "locīties"), //atlocīties
		ThirdConj.reflChange("-ļogos, -ļogies,", "-ļogās, pag. -ļodzījos", "ļodzīties"), //izļodzīties
		ThirdConj.reflChange("-raugos, -raugies,", "-raugās, pag. -raudzījos", "raudzīties"), //apraudzīties
		ThirdConj.reflChange("-sakos, -sakies,", "-sakās, pag. -sacījos", "sacīties"), //atsacīties
		ThirdConj.reflChange("-slakos, -slakies,", "-slakās, pag. -slacījos", "slacīties"), //apslacīties
		ThirdConj.reflChange("-slaukos, -slaukies,", "-slaukās, pag. -slaucījos", "slaucīties"), //apslaucīties
		ThirdConj.reflChange("-slogos, -slogies,", "-slogās, pag. -slodzījos", "slodzīties"), //ieslodzīties
		ThirdConj.reflChange("-snaikos, -snaikies,", "-snaikās, pag. -snaicījos", "snaicīties"), //izsnaicīties
		ThirdConj.reflChange("-šļaukos, -šļaukies,", "-šļaukās, pag. -šļaucījos", "šļaucīties"), //izšļaucīties
		ThirdConj.reflChange("-tekos, -tecies,", "-tekas, pag. -tecējos", "tecēties"), //iztecēties

		// Standartizētie bez mijām.
		// A, B, C
		ThirdConj.reflStd("-ceros, -ceries,", "-ceras, pag. -cerējos", "cerēties"), //atcerēties
		// Č
		ThirdConj.reflStd("-čabinos, -čabinies,", "-čabinās, pag. -čabinājos", "čabināties"), //iečabināties
		ThirdConj.reflStd("-čukstos, -čuksties,", "-čukstas, pag. -čukstējos", "čukstēties"), //iečukstēties
		// D
		ThirdConj.reflStd("-deros, -deries,", "-deras, pag. -derējos", "derēties"), //iederēties
		ThirdConj.reflStd("-dzirdos, -dzirdies,", "-dzirdas, pag. -dzirdējos", "dzirdēties"), //sadzirdēties
		// E, F, G
		ThirdConj.reflStd("-grasos, -grasies,", "-grasās, pag. -grasījos", "grasīties"), //pagrasīties
		ThirdConj.reflStd("-gribos, -gribies,", "-gribas, pag. -gribējos", "gribēties"), //izgribēties
		// H, I
		ThirdConj.reflStd("-īdos, -īdies,", "-īdas, pag. -īdējos", "īdēties"), //ieīdēties
		// J, K
		ThirdConj.reflStd("-kustos, -kusties,", "-kustas, pag. -kustējos", "kustēties"), //aizkustēties
		// L
		ThirdConj.reflStd("-lāpos, -lāpies,", "-lāpās, pag. -lāpījos", "lāpīties"), //aplāpīties
		// M
		ThirdConj.reflStd("-minos, -minies,", "-minas, pag. -minējos", "minēties"), //atminēties
		ThirdConj.reflStd("-mīlos, -mīlies,", "-mīlas, pag. -mīlējos", "mīlēties"), //iemīlēties
		ThirdConj.reflStd("-muldos, -muldies,", "-muldas, pag. -muldējos", "muldēties"), //iemuldēties 1, 2
		// N, O, P
		ThirdConj.reflStd("-peldos, -peldies,", "-peldas, pag. -peldējos", "peldēties"), //izpeldēties
		ThirdConj.reflStd("-precos, -precies,", "-precas, pag. -precējos", "precēties"), //aizprecēties
		// R
		ThirdConj.reflStd("-redzos, -redzies,", "-redzas, pag. -redzējos", "redzēties"), // izredzēties
		// S
		ThirdConj.reflStd("-strīdos, -strīdies,", "-strīdas, pag. -strīdējos", "strīdēties"), //izstrīdēties
		ThirdConj.reflStd("-sūdzos, -sūdzies,", "-sūdzas, pag. -sūdzējos", "sūdzēties"), //pasūdzēties
		ThirdConj.reflStd("-sūkstos, -sūksties,", "-sūkstās, pag. -sūkstījos", "sūkstīties"), //nosūkstīties
		// T
		ThirdConj.reflStd("-ticos, -ticies,", "-ticas, pag. -ticējos", "ticēties"), // uzticēties
		ThirdConj.reflStd("-turos, -turies,", "-turas, pag. -turējos", "turēties"), //atturēties
		// U, V
		ThirdConj.reflStd("-vēlos, -vēlies,", "-vēlas, pag. -vēlējos", "vēlēties"), //atvēlēties
		ThirdConj.reflStd("-vīkšos, -vīkšies,", "-vīkšas, pag. -vīkšījos", "vīkšīties"), // savīkšīties
		ThirdConj.reflStd("-vīkšķos, -vīkšķies,", "-vīkšķas, pag. -vīkšķījos", "vīkšķīties"), // savīkšķīties
		// Z
		ThirdConj.reflStd("-zinos, -zinies,", "-zinās, pag. -zinājos", "zināties"), //apzināties
		ThirdConj.reflStd("-zvēros, -zvēries,", "-zvēras, pag. -zvērējos", "zvērēties"), //izzvērēties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlformas.
		// Fakultatatīvā mija.
		ThirdConj.reflOptChange3PersParallel(
				"-ņurcās, retāk -ņurkās, pag. -ņurcījās", "ņurcīties"), // noņurcīties

		// Standartīgie ar mijām.
		ThirdConj.reflChange3Pers("-vajagas, pag. -vajadzējās", "vajadzēties"), //ievajadzēties
		// Standartīgie bez mijām.
		ThirdConj.reflStd3Pers("-gailējas, pag. -gailējās", "gailēties"), //pagailēties
		ThirdConj.reflStd3Pers("-lauzās, pag. -lauzījās", "lauzīties"), //aplauzīties
		ThirdConj.reflStd3Pers("-mīcās, pag. -mīcījās", "mīcīties"), //piemīcīties
		ThirdConj.reflStd3Pers("-vārās, pag. -vārījās", "vārīties"), //pievārīties

		ThirdConj.reflStdPlural("-čukstamies, -čukstaties, -čukstas, pag. -čukstējāmies", "čukstēties"), // pačukstēties

	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final EndingRule[] reflMultiConjVerb = {
		// Likumi, kam ir visu formu variants.
		SecondThirdConj.reflStdAllPersParallel(
				"-gailējos, -gailējies, -gailējās, arī -gailos, -gailies, -gailas, pag. -gailējos",
				"gailēties"), // iegailēties
		SecondThirdConj.reflStdAllPersParallel(
				"-kristījos, -kristījies, -kristījas, arī -kristos, -kristies, -kristās, pag. -kristījos",
				"kristīties"), // nokristīties
		SecondThirdConj.reflStdAllPersParallel(
				"-krustījos, -krustījies, -krustījas, arī -krustos, -krusties, -krustās, pag. -krustījos",
				"krustīties"), // nokrustīties
		SecondThirdConj.reflStdAllPersParallel(
				"-ķēzījos, -ķēzījies, -ķēzījas, arī -ķēzos, -ķēzies, -ķēzās, pag. -ķēzījos",
				"ķēzīties"), //noķēzīties
		SecondThirdConj.reflStdAllPersParallel(
				"-mērījos, -mērījies, -mērījas, arī -mēros, -mēries, -mērās, pag. -mērījos",
				"mērīties"), // izmērīties
		SecondThirdConj.reflStdAllPersParallel(
				"-pelnos, -pelnies, -pelnās, arī -pelnījos, -pelnījies, -pelnījās, pag. -pelnījos",
				"pelnīties"), //izpelnīties
		SecondThirdConj.reflStdAllPersParallel(
				"-pūlos, -pūlies, -pūlas, arī -pūlējos, -pūlējies, -pūlējas, pag. -pūlējos",
				"pūlēties"), // nopūlēties
		SecondThirdConj.reflStdAllPersParallel(
				"-sargājos, -sargājies, -sargājas, arī -sargos, -sargies, -sargās, pag. -sargājos",
				"sargāties"), // aizsargāties
		SecondThirdConj.reflStdAllPersParallel(
				"-svētījos, -svētījies, -svētījas, arī -svētos, -svēties, -svētās, pag. -svētījos",
				"svētīties"), // iesvētīties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// TODO vai tiešām nav saskaņojams ne ar vienu priedēkļa likumu?
		SecondThirdConj.reflStd3PersParallel(
				"-vēdījas, arī -vēdās, pag. -vēdījas", "vēdīties"), // vēdīties
	};

}
