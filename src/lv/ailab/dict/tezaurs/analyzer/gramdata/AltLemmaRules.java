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
		WithAltLemma.nounPluralToSingularMascStd("-ļu, vsk.", "lis, -ļa, v.", "ļi", 3), // budēļi
		WithAltLemma.nounPluralToSingularMascStd("-ņu, vsk.", "nis, -ņa, v.", "ņi", 3), // aizvirtņi
		WithAltLemma.nounPluralToSingularMascStd("-ņu, vsk.", "lnis, -ļņa, v.", "ļņi", 3), // starpviļņi
		WithAltLemma.nounPluralToSingularMascStd("vsk.", "lis, -ļa, v.", "ļi", 3), // asinsbrālis
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "cis, -ča, v.", "či", 3), // atomieroči
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "dis, -ža, v.", "ži", 3), // čemurzieži
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "jis, -ja, v.", "ji", 3), // airkāji
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "ķis, -ķa, v.", "ķi", 3), // ašķi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "nis, -ņa, v.", "ņi", 3), // cukurzirņi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "pis, -pja, v.", "pji", 3), // biezlapis
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "tis, -ša, v.", "ši", 3), // asinsķermenīši

		WithAltLemma.nounPluralToSingularMascStd("-akmeņu, vsk.", "akmens, -akmens, v.", "akmeņi", 4), // brugakmeņi

		// 1. paradigma: 1. dekl., s
		WithAltLemma.nounPluralToSingularMascStd("-u, v.; vsk.", "ns", "ni", 1), // antinukloni
		WithAltLemma.nounPluralToSingularMascStd("-u; vsk.", "s, -a, v.", "i", 1), // abrazīvi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "s, -a, v.", "i", 1), // aizkari
 		// 2. paradigma: 1. deklš, š
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
		WithAltLemma.nounPluralToSingularFemStd("-u, vsk.", "a, -as, s.", "as", 7), // akvareļkrāsas
		WithAltLemma.nounPluralToSingularFemStd("vsk.", "a", "as", 7), // angiospermas
		// 9. paradigma: 5. dekl.
		WithAltLemma.nounPluralToSingularFemStd("-bju, vsk.", "be, -es, s.", "bes", 9), // aminoskābes
		WithAltLemma.nounPluralToSingularFemStd("-ļu, vsk.", "le, -es, s.", "les", 9), // ābeles, akmeņogles
		WithAltLemma.nounPluralToSingularFemStd("-ņu, vsk.", "ne, -es, s.", "nes", 9), // blusenes
		WithAltLemma.nounPluralToSingularFemStd("-šu, vsk.", "te, -es, s.", "tes", 9), // aktinomicētes
		WithAltLemma.nounPluralToSingularFemStd("-šu, vsk.", "se, -es, s.", "ses", 9), // alises
		WithAltLemma.nounPluralToSingularFemStd("-u, vsk.", "e, -es, s.", "es", 9), // brūnaļģes // TODO varbūt te vajag bezmijas paradigmu?
		// paradigma: 5. dekl. bez mijas
		WithAltLemma.nounPluralToSingularFemNoChange("-tu, vsk.", "te, -es, s.", "tes", 9), // apaļmute
		// 11. paradigma: 6. dekl.
		WithAltLemma.nounPluralToSingularFemStd("bārkšu, vsk.", "bārksts, bārksts, s.", "bārkstis", 11), // bārkstis
		WithAltLemma.nounPluralToSingularFemStd("-zivju, vsk. ", "zivs, -zivs, s.", "zivis", 11), // bruņzivis
		// 35. paradigma: 6. dekl. bez mijas.
		WithAltLemma.nounPluralToSingularFemStd("-tu, vsk.", "akts, akts, s.", "aktis", 35), // aktis

		//	Neviennozīmīgie likumi.
	};

	/**
	 * Likumi formā:
	 * -ķa; s. -ķe, -ķu
	 * s. -ā
	 */
	public static final AdditionalHeaderRule[] mascToFem = {
		AltFullLemmaRule.simple("-ā, v., ", "ā, -ās, s.", new StemSlotSubRule[]{
			StemSlotSubRule.of(".*tais", new Integer[] {30, 0},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING},
					3, "ā", new Integer[] {40, 0},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
			StemSlotSubRule.of(".*[^tšm]ais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.DEFINITE_ENDING},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.DEFINITE_ENDING}),
		}), // aizturēts/aizturētais, akls/aklais
		AltFullLemmaRule.simple("-ā, v. ", "ā, -ās, s.", new StemSlotSubRule[]{
			StemSlotSubRule.of(".*tais", new Integer[] {30, 0},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING},
					3, "ā", new Integer[] {40, 0},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
			StemSlotSubRule.of(".*[^tšm]ais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.DEFINITE_ENDING},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.DEFINITE_ENDING}),
			}), // balts/baltais

		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -te, -šu", "ts", "te"), // abstinents
		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -ce, -ču", "ks", "ce"), // aizsardzībnieks
		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -re, -ru", "rs", "re"), // akvizitors
		WithAltLemma.mascFirstDeclToFemFifthDecl("s. -ste, -stu", "sts", "ste"), // almānists
		WithAltLemma.mascFirstDeclToFemFifthDecl("-a; s. -te, -šu", "ts", "te"), // akcelerāts

		WithAltLemma.mascSeconDeclToFemFifthDecl("-ķa; s. -ķe, -ķu", "ķis", "ķe"), // agonistiķis
		WithAltLemma.mascSeconDeclToFemFifthDecl("-ša, s. -te, -šu", "tis", "te"), // albiģietis
		WithAltLemma.mascSeconDeclToFemFifthDecl("-ša; s. -te, -šu", "tis", "te"), // aiolietis

		AltEndingRule.of("s. -ā", new StemSlotSubRule[]{
			StemSlotSubRule.of(".*ošais", new Integer[] {30, 0},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING},
					3, "ā", new Integer[] {40, 0},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
			//StemSlotSubRule.of(".*tais", new Integer[] {30, 0},
			//		new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING},
			//		3, "ā", new Integer[] {40, 0},
			//		new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
			StemSlotSubRule.of(".*[aā]mais", new Integer[] {30, 0},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING},
					3, "ā", new Integer[] {40, 0},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
			StemSlotSubRule.of(".*[^štm]ais|visupirmais|vispirmais", new Integer[] {30},
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__ADJ, TFeatures.DEFINITE_ENDING},
					3, "ā", new Integer[] {40},
					new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__ADJ, TFeatures.DEFINITE_ENDING})
		}), // agrākais, aiznākošais, aiznākamais, beidzamais, visupirmais, pēdējais aizkomentētie vārdnīcā nav sastopami.

	};

}
