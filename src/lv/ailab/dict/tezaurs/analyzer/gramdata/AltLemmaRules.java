package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltEndingRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltLemmaRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltFullLemmaRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.AltLemmaSubRule;
import lv.ailab.dict.utils.Tuple;

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
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
 *
 * Izveidots 2015-10-26.
 *
 * @author Lauma
 */
public class AltLemmaRules
{
	/**
	 * Likumi formā:
	 * -ņu, vsk. dižtauriņš, -ņa, v.
	 * -ņu, vsk. aizvirtnis, -ņa, v.
	 */
	public static final AltLemmaRule[] pluralToSingular = {
		// 2. paradigma: Lietvārds 1. deklinācija -š
		AltFullLemmaRule.nounPluralToSingularMasc("-ņu, vsk.", "ņš, -ņa, v.", "ņi", 2), // dižtauriņi
		// 3. paradigma: Lietvārds 2. deklinācija -is
		AltFullLemmaRule.nounPluralToSingularMasc("-ņu, vsk.", "nis, -ņa, v.", "ņi", 3), // aizvirtņi
		AltFullLemmaRule.nounPluralToSingularMasc("-ņu, vsk.", "lnis, -ļņa, v.", "ļņi", 3), // starpviļņi
		AltFullLemmaRule.nounPluralToSingularMasc("-u, vsk.", "jis, -ja, v.", "ji", 3), // airkāji
			// -u; vsk. abrazīvs, -a, v.
		AltFullLemmaRule.nounPluralToSingularMasc("-u; vsk.", "vs, -a, v.", "vi", 1), // abrazīvi
		// 1. paradigma: Lietvārds 1. deklinācija -s
		AltFullLemmaRule.nounPluralToSingularMasc("-u, vsk.", "s, -a, v.", "i", 1), // aizkari

		//	Neviennozīmīgie likumi.
	};

	/**
	 * Likumi formā:
	 * -ķa; s. -ķe, -ķu
	 * s. -ā
	 */
	public static final AltLemmaRule[] mascToFem = {
		AltEndingRule.mascFirstDeclToFemFifthDecl("s. -te, -šu", "ts", "te"), // abstinents
		AltEndingRule.mascFirstDeclToFemFifthDecl("s. -ce, -ču", "ks", "ce"), // aizsardzībnieks
		AltEndingRule.mascFirstDeclToFemFifthDecl("s. -re, -ru", "rs", "re"), // akvizitors
		AltEndingRule.mascFirstDeclToFemFifthDecl("s. -ste, -stu", "sts", "ste"), // almānists

		AltEndingRule.mascSeconDeclToFemFifthDecl("-ķa; s. -ķe, -ķu", "ķis", "ķe"), // agonistiķis
		AltEndingRule.mascSeconDeclToFemFifthDecl("-ša; s. -te, -šu", "tis", "te"), // aiolietis

		AltEndingRule.of("s. -ā", new AltLemmaSubRule[]{
			AltLemmaSubRule.of(".*ošais", new Integer[] {30, 0},
					new Tuple[]{Features.GENDER__MASC, Features.POS__ADJ, Features.POS__PARTICIPLE_OSS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS, Features.DEFINITE_ENDING},
					3, "ā", new Integer[] {40, 0},
					new Tuple[]{Features.GENDER__FEM, Features.POS__ADJ, Features.POS__PARTICIPLE_OSS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS, Features.DEFINITE_ENDING}),
			AltLemmaSubRule.of(".*tais", new Integer[] {30, 0},
					new Tuple[]{Features.GENDER__MASC, Features.POS__ADJ, Features.POS__PARTICIPLE_TS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS, Features.DEFINITE_ENDING},
					3, "ā", new Integer[] {40, 0},
					new Tuple[]{Features.GENDER__FEM, Features.POS__ADJ, Features.POS__PARTICIPLE_TS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS, Features.DEFINITE_ENDING}),
			AltLemmaSubRule.of(".*[aā]mais", new Integer[] {30, 0},
					new Tuple[]{Features.GENDER__MASC, Features.POS__ADJ, Features.POS__PARTICIPLE_AMS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS, Features.DEFINITE_ENDING},
					3, "ā", new Integer[] {40, 0},
					new Tuple[]{Features.GENDER__FEM, Features.POS__ADJ, Features.POS__PARTICIPLE_AMS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS, Features.DEFINITE_ENDING}),
			AltLemmaSubRule.of(".*ais", new Integer[] {30},
					new Tuple[]{Features.GENDER__MASC, Features.POS__ADJ, Features.DEFINITE_ENDING},
					3, "ā", new Integer[] {40},
					new Tuple[]{Features.GENDER__FEM, Features.POS__ADJ, Features.DEFINITE_ENDING})
		}), // agrākais, aiznākošais

	};

}
