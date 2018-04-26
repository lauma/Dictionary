package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.AdditionalHeaderRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltEndingRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltFullLemmaRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.StemSlotSubRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns.WithAltLemma;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * TGram.processBeginingWithPatterns(String, String)
 * Likumi, kas veido alternatīvās formas.
 *
 * Īpašības vārdu, divdabju un skaitļa vārda likumus tikai tāpēc vien, ka tiem
 * norādīts, kā veidot sieviešu dzimti, vēl šeit neliek, jo sieviešu dzimti
 * Morforīki šīm vārdšķirām veido paši.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * Izveidots 2015-10-26.
 *
 * @author Lauma
 */
public class AltLemmaRules
{
	/**
	 * Metode visu klasē iekļauto likumu iegūšanai pareizā secībā.
	 * @return saraksts ar likumu blokiem.
	 */
	public static ArrayList<AdditionalHeaderRule[]> getAll()
	{
		ArrayList<AdditionalHeaderRule[]> res = new ArrayList<>(2);
		res.add(pluralToSingular);
		res.add(mascToFem);
		return res;
	}

	/**
	 * Likumi formā:
	 * -ņu, vsk. dižtauriņš, -ņa, v.
	 * -ņu, vsk. aizvirtnis, -ņa, v.
	 */
	public static final AdditionalHeaderRule[] pluralToSingular = {
		// 2. paradigma: Lietvārds 1. deklinācija -š
		//AltFullLemmaRule.nounPluralToSingularMascStd("-ņu, vsk.", "ņš, -ņa, v.", "ņi", 2), // dižtauriņi
		// 3. paradigma: Lietvārds 2. deklinācija -is
			//-ju, vsk. daudzkājis, -ja, v.
		WithAltLemma.nounPluralToSingularMascStd("-ju, vsk.", "jis, -ja, v.", "ji", 3), // daudzkāji
		WithAltLemma.nounPluralToSingularMascStd("-ju, vsk.", "jis, -a, v.", "ji", 3), // desmitkāji
		WithAltLemma.nounPluralToSingularMascStd("-ļu, vsk.", "lis, -ļa, v.", "ļi", 3), // budēļi
		WithAltLemma.nounPluralToSingularMascStd("-ņu, vsk.", "nis, -ņa, v.", "ņi", 3), // aizvirtņi
		WithAltLemma.nounPluralToSingularMascStd("-ņu, vsk.", "lnis, -ļņa, v.", "ļņi", 3), // starpviļņi
		WithAltLemma.nounPluralToSingularMascStd("-šu, vsk.", "sis, -ša, v.", "ši", 3), // gaiļpieši
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "lis, -ļa, v.", "ļi", 3), // asinsbrālis
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "tis, -ša, v.", "ši", 3), // čečenieši
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "sis, -ša, v.", "ši", 3), // kvieši
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "cis, -ča, v.", "či", 3), // atomieroči
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "dis, -ža, v.", "ži", 3), // čemurzieži
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "jis, -ja, v.", "ji", 3), // airkāji
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ķis, -ķa, v.", "ķi", 3), // ašķi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "lis, -ļa, v.", "ļi", 3), // dzeguļi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "llis, -ļļa, v.", "ļļi", 3), // traļļi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "nis, -ņa, v.", "ņi", 3), // cukurzirņi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "pis, -pja, v.", "pji", 3), // biezlapis
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "tis, -ša, v.", "ši", 3), // asinsķermenīši
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "zis, -ža, v.", "ži", 3), // dzelži
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "jis, -a, v.", "ji", 3), // galvkājis
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ris, -a, v.", "ri", 3), // klaķieri
		WithAltLemma.nounPluralToSingularMascStd("-ru, vsk.", "ris, -ra, v.", "ri", 3), // dzimumdziedzeri

		WithAltLemma.nounPluralToSingularMascStd("-akmeņu, vsk.", "akmens, -akmens, v.", "akmeņi", 4), // brugakmeņi

		// 1. paradigma: 1. dekl., s
		WithAltLemma.nounPluralToSingularMascStd("-u, v.; vsk.", "ns", "ni", 1), // antinukloni
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "bs, -a, v.", "bi", 1), // būvdarbi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ds, -a, v.", "di", 1), // aizsargcimdi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "gs, -a, v.", "gi", 1), // bišaugi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "js, -a, v.", "ji", 1), // dārzāji
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ks, -a, v.", "ki", 1), // abinieki
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ls, -a, v.", "li", 1), // dvīņkristāli
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ms, -a, v.", "mi", 1), // civillikumi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ns, -a, v.", "ni", 1), // aborigēni
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ps, -a, v.", "pi", 1), // ciltslopi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "rs, -a, v.", "ri", 1), // aizkari
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ss, -a, v.", "si", 1), // onkovīrusi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ts, -a, v.", "ti", 1), // akotmats
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "vs, -a, v.", "vi", 1), // apavi, abrazīvi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "zs, -a, v.", "zi", 1), // plagioklazi
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "gs, -a, v.", "gi", 1), // balstaugi
		// 2. paradigma: 1. dekl., š
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "š, -a, v.", "i", 2), // cekuliņi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "š; -a, v.", "i", 2), // aizsargstabiņi

		// 30. paradigma: noteiktā galotne.
		WithAltLemma.nounPluralToSingularMascStd("-o, vsk.", "īgais, -ā, v.", "īgie", 30), // baložveidīgie

		WithAltLemma.nounPluralToSingularMascStd("vsk.", "ds", "di", 1), // aktinoīdi
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "hs", "hi", 1), // akritarhi
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "ns", "ni", 1), // alohtoni
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "ms", "mi", 1), // ampērvijums
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "ts", "ti", 1), // alumināti

		WithAltLemma.nounPluralToSingularMascStd("vsk.", "lis", "ļi", 3), // apjomskaitļi
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "īgais", "īgie", 30), // amēbveidīgie

		// 7. paradigma: 4. dekl.
		WithAltLemma.nounPluralToSingularFem("-u, vsk.", "a, -as, s.", "as", 7), // akvareļkrāsas
		WithAltLemma.nounPluralToSingularFem("-ņu, vsk.", "ņa, -as, s.", "ņas", 7), // elementārdaļiņas
		WithAltLemma.nounPluralToSingularFem("-u,", "a, -as, s.", "as", 7), // cukurviela
		WithAltLemma.nounPluralToSingularFem("vsk.", "a", "as", 7), // angiospermas
		// 9. paradigma: 5. dekl.
		WithAltLemma.nounPluralToSingularFem("-bju, vsk.", "be, -es, s.", "bes", 9), // aminoskābes
		WithAltLemma.nounPluralToSingularFem("-ļļu, vsk.", "lle, -es, s.", "lles", 9), // dilles
		WithAltLemma.nounPluralToSingularFem("-mju, vsk.", "me, -es, s.", "mes", 9), // dienvidzemes
		WithAltLemma.nounPluralToSingularFem("-ču, vsk.", "ce, -es, s.", "ces", 9), // mūsmājniece
		WithAltLemma.nounPluralToSingularFem("-ļu, vsk.", "le, -es, s.", "les", 9), // ābeles, akmeņogles
		WithAltLemma.nounPluralToSingularFem("-ņu, vsk.", "ne, -es, s.", "nes", 9), // blusenes
		WithAltLemma.nounPluralToSingularFem("-šu, vsk.", "te, -es, s.", "tes", 9), // aktinomicētes
		WithAltLemma.nounPluralToSingularFem("-šu, vsk.", "se, -es, s.", "ses", 9), // alises
		WithAltLemma.nounPluralToSingularFem("-u, vsk.", "e, -es, s.", "es", 9), // brūnaļģes // TODO varbūt te vajag bezmijas paradigmu?
		// paradigma: 5. dekl. bez mijas
		WithAltLemma.nounPluralToSingularFem("-tu, vsk.", "te, -es, s.", "tes", 44), // apaļmute
		// 11. paradigma: 6. dekl.
		WithAltLemma.nounPluralToSingularFem("bārkšu, vsk.", "bārksts, bārksts, s.", "bārkstis", 11), // bārkstis
		WithAltLemma.nounPluralToSingularFem("-zivju, vsk.", "zivs, -zivs, s.", "zivis", 11), // bruņzivis
		// 35. paradigma: 6. dekl. bez mijas.
		WithAltLemma.nounPluralToSingularFem("-tu, vsk.", "akts, akts, s.", "aktis", 35), // aktis
		WithAltLemma.nounPluralToSingularFem("-utu, vsk.", "uts, -uts, s.", "utis", 35), // dūrējuts

		//	Neviennozīmīgie likumi.
	};

	/**
	 * Likumi formā:
	 * -ķa; s. -ķe, -ķu
	 * s. -ā
	 */
	public static final AdditionalHeaderRule[] mascToFem = {
		AltFullLemmaRule.simple("-ā, v., ", "ā, -ās, s.", new StemSlotSubRule[]{
			StemSlotSubRule.of(".*tais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS}),
			StemSlotSubRule.of(".*amais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.UNCLEAR_POS},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS}),
			StemSlotSubRule.of(".*[^tšm]ais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ}),
		}), // aizturēts/aizturētais, akls/aklais
		AltFullLemmaRule.simple("-ā, v. ", "ā, -ās, s.", new StemSlotSubRule[]{
			StemSlotSubRule.of(".*tais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS}),
			StemSlotSubRule.of(".*amais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.UNCLEAR_POS},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS}),
			StemSlotSubRule.of(".*[^tšm]ais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ}),
			}), // balts/baltais

		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -te, -šu", "ts", "te"), // abstinents
		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -ce, -ču", "ks", "ce"), // aizsardzībnieks
		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -re, -ru", "rs", "re"), // akvizitors
		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -ste, -stu", "sts", "ste"), // almānists
		WithAltLemma.mascFirstDeclToFemFifthDecl("-a; s. -te, -šu", "ts", "te"), // akcelerāts

		WithAltLemma.mascSeconDeclToFemFifthDecl("-ķa; s. -ķe, -ķu", "ķis", "ķe"), // agonistiķis
		WithAltLemma.mascSeconDeclToFemFifthDecl("-ša, s. -te, -šu", "tis", "te"), // albiģietis
		WithAltLemma.mascSeconDeclToFemFifthDecl("-ša; s. -te, -šu", "tis", "te"), // aiolietis

		AltEndingRule.of("s. -usī", ".*ušais", 5,
				30, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__PARTICIPLE_IS},
				"usī", 41, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__PARTICIPLE_IS}), //aizpagājušais
		AltEndingRule.of("s. -ā", new StemSlotSubRule[]{
			StemSlotSubRule.of(".*ošais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.UNCLEAR_POS},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.UNCLEAR_POS}),
			//StemSlotSubRule.of(".*tais", new Integer[] {30},
			//		new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS},
			//		3, "ā", new Integer[] {40},
			//		new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_POS}),
			StemSlotSubRule.of(".*[aā]mais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.UNCLEAR_POS},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.UNCLEAR_POS}),
			StemSlotSubRule.of(".*[^štm]ais|visupirmais|vispirmais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ})
		}), // agrākais, aiznākošais, aiznākamais, beidzamais, visupirmais, pēdējais aizkomentētie vārdnīcā nav sastopami.

	};

}
