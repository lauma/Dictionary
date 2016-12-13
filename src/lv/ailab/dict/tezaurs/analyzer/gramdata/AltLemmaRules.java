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
		WithAltLemma.nounPluralToSingularMascStd("-ņu, vsk.", "nis, -ņa, v.", "ņi", 3), // aizvirtņi
		WithAltLemma.nounPluralToSingularMascStd("-ņu, vsk.", "lnis, -ļņa, v.", "ļņi", 3), // starpviļņi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "jis, -ja, v.", "ji", 3), // airkāji
		WithAltLemma.nounPluralToSingularMascStd("-u, v.; vsk.", "ns", "ni", 1), // antinuklons

		// 1. paradigma: Lietvārds 1. deklinācija -s
		WithAltLemma.nounPluralToSingularMascStd("-u; vsk.", "s, -a, v.", "i", 1), // abrazīvi
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "s, -a, v.", "i", 1), // aizkari
		WithAltLemma.nounPluralToSingularMascStd("-u, vsk.", "š; -a, v.", "i", 1), // aizsargstabiņi

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
		WithAltLemma.nounPluralToSingularFemStd("-ļu, vsk.", "le, s.", "les", 9), // ābeles
		WithAltLemma.nounPluralToSingularFemStd("-ļu, vsk.", "le, -es, s.", "les", 9), // akmeņogles
		WithAltLemma.nounPluralToSingularFemStd("-šu, vsk.", "te, -es, s.", "tes", 9), // aktinomicētes
		WithAltLemma.nounPluralToSingularFemStd("-šu, vsk.", "se, -es, s.", "ses", 9), // alises
		WithAltLemma.nounPluralToSingularFemStd("-ļu, vsk.", "le", "les", 9), // alēles
		// paradigma: 5. dekl. bez mijas
		WithAltLemma.nounPluralToSingularFemNoChange("-tu, vsk.", "te, -es, s.", "tes", 9), // apaļmute
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
