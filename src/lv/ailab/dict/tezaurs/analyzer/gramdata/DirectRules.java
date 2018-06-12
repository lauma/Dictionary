package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.struct.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.*;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.Adjective;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.MultiPos;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.Participle;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns.FifthDecl;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns.GenNoun;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns.SecondDecl;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.nouns.SixthDecl;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.FirstConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.SecondConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.SecondThirdConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.verbs.ThirdConj;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * TGram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar EndingRule.applyDirect().
 * Ja vienai lemmai atbilst vairāki likumi (piemēram, verbiem ir reizēm ir
 * norādīta locīšana ar paralēlformām un reizēm bez), tad visiem likumiem jābūt
 * vienā klasē - vai nu OptHypernRules vai DirectRules, bet ne juku jukām.
 * Verbu likumiem visu personu likumus, no kuriem tiek atvasināti 3. personas
 * likumi, vajag likt pirms 3. personas likumiem - tad redundantie 3. personas
 * likumi uzrādīsies statistikā kā nelietoti.
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * @author Lauma
 */
public class DirectRules
{
	/**
	 * Metode klasē iekļauto nosacīti drošo (var lietot pirms citiem likumiem,
	 * ja vien tiek ievērota šeit dotā secība) likumu bloku iegūšanai pareizā
	 * secībā.
	 * @return saraksts ar likumu blokiem.
	 */
	public static List<EndingRule[]> getAllSafe()
	{
		List<EndingRule[]> res = new ArrayList<>(14);
		// Vairākkonjugāciju likumi jāliek pirms vienas konj. likumiem, jo var
		// sanākt, ka viens likums ir otra prefikss.
		res.add(be);
		res.add(directMultiConjVerb);
		res.add(reflMultiConjVerb);

		res.add(directFirstConjVerb);
		res.add(directSecondConjVerb);
		res.add(directThirdConjVerb);
		res.add(reflFirstConjVerb);
		res.add(reflSecondConjVerb);
		res.add(reflThirdConjVerb);

		res.add(pronomen);
		res.add(numeral);

		res.add(other);

		res.add(secondDeclNoun);
		res.add(forthDeclNoun);
		res.add(fifthDeclNoun);
		res.add(nounMultiDecl);

		return res;
	}
	/**
	 * Metode klasē iekļauto nedrošo (ir citu likumu prefiksi, jālieto biegās)
	 * likumu bloku iegūšanai pareizā secībā.
	 * @return saraksts ar likumu blokiem.
	 */
	public static List<EndingRule[]> getAllDangeros()
	{
		List<EndingRule[]> res = new ArrayList<>(1);
		res.add(dangerous);
		return res;
	}

	/**
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final EndingRule[] other = {

		// Lietvārdi ar lokāmību vidū:
		GenNoun.any("labasdienas, s.", "labadiena", 7,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.MULTI_INFLECTIVE,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"laba[13] diena[7]\"")},
				null), //labadiena
		GenNoun.any("labasnakts, s.", "labanakts", 11,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.MULTI_INFLECTIVE,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"laba[13] nakts[11]\"")},
				null), //labanakts
		GenNoun.any("labarīta, v.", "labsrīts", 1,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.MULTI_INFLECTIVE,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"labs[13] rīts[1]\"")},
				null), //labsrīts
		GenNoun.any("labavakara, v.", "labsvakars", 1,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.MULTI_INFLECTIVE,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"labs[13] vakars[1]\"")},
				null), //labsvakars
		GenNoun.any("šāssaules, arī šīssaules, s.", "šīsaule", 9,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.MULTI_INFLECTIVE, TFeatures.PARALLEL_FORMS,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"šī[25] saule[9]\"")},
				null), //šīsaule
		GenNoun.any("šāspasaules, arī šīspasaules, s.", "šīpasaule", 9,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.MULTI_INFLECTIVE, TFeatures.PARALLEL_FORMS,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"šī[25] pasaule[9]\"")},
				null), //šīpasaule
		GenNoun.any("šāszemes, arī šīszemes, s.", "šīzeme", 9,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.MULTI_INFLECTIVE, TFeatures.PARALLEL_FORMS,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"šī[25] zeme[9]\"")},
				null), //šīzeme
		GenNoun.any("vecātēva, v.", "vecaistēvs", 1,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.MULTI_INFLECTIVE,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"vecais[30] tēvs[1]\"")},
				null), //vecaistēvs
		GenNoun.any("vecāsmātes, dsk. ģen. vecomāšu, s.", "vecāmāte", 9,
				new Tuple[]{TFeatures.GENDER__FEM, TFeatures.MULTI_INFLECTIVE,
						Tuple.of(TKeys.MULTIINFLECTION_PATTERN, "\"vecā[40] māte[9]\"")},
				null), //vecāmāte


		// 1. paradigma: 1. dekl. lietvārdi, -s
		GenNoun.any("-a, dsk. ģen. -ku, v.", ".*ks", 1, null, new Tuple[] {TFeatures.GENDER__MASC}), // cepurnieks
		GenNoun.any("-a, dsk. ģen. -nu, v.", ".*ns", 1, null, new Tuple[] {TFeatures.GENDER__MASC}), // aizsargspārns
		GenNoun.any("-la, v.", ".*ls", 1, null, new Tuple[] {TFeatures.GENDER__MASC}), // durkls
		// 6. paradigma: 3. deklinācijas lietvārdi
		GenNoun.any("-us, v.", ".*us", 6, null, new Tuple[] {TFeatures.GENDER__MASC}), // dienvidus

		// 34. paradigma: Atgriezeniskie lietvārdi -šanās
		GenNoun.any(
				"ģen. -ās, akuz. -os, instr. -os, dsk. -ās, ģen. -os, akuz. -ās, s.", ".*šanās", 34,
				new Tuple[]{TFeatures.POS__REFL_NOUN}, new Tuple[]{TFeatures.GENDER__FEM}), //aizbildināšanās
		GenNoun.any("ģen. -ās, akuz. -os, instr. -os, s.", ".*šanās", 34,
				new Tuple[]{TFeatures.POS__REFL_NOUN}, new Tuple[]{TFeatures.GENDER__FEM}), //augšāmcelšanās
		// 34. paradigma: Atgriezeniskie lietvārdi -umies
		GenNoun.any("akuz. -os, instr. -os, dsk. -ies, akuz. -os, instr. -os, v.",
				".*umies", 33,
				new Tuple[]{TFeatures.POS__REFL_NOUN}, new Tuple[]{TFeatures.GENDER__MASC}), // vēlējumies
		//GenNoun.any("akuz. -os, instr. -os, v.", ".*umies", 33,
		//		new Tuple[]{TFeatures.POS__REFL_NOUN}, new Tuple[]{TFeatures.GENDER__MASC}), //atlūgumies

		// Paradigma: 11 - 6. dekl.
		SixthDecl.noChange("-ts, dsk. ģen. -tu", ".*ts"), // koloniālvalsts
		GenNoun.any("-ts, -šu", ".*ts", 11, new Tuple[]{TFeatures.GENDER__FEM}, null), //abonentpults
		GenNoun.any("-vs, -vju", ".*vs", 11, new Tuple[]{TFeatures.GENDER__FEM}, null), //adatzivs

		// Paradigma: 11 - 6. dekl.
		GenNoun.any("-žu, v.", ".*ļaudis", 11,
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.USED_ONLY__PLURAL},
				new Tuple[]{TFeatures.GENDER__MASC}), //ļaudis

		// Paradigmas: 13, 14 - īpašības vārdi - skaidri pateikts
		Adjective.std("īp. v. -ais; s. -a, -ā"), // aerobs
		Adjective.std("-ais, īp."), // albīns
		BaseRule.of("s. -as; adj.", ".*i", new Integer[]{13, 14},
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.USUALLY_USED__INDEFINITE},
				new Tuple[]{TFeatures.POS__ADJ}), // abēji 2
		BaseRule.of("-ie; s. -as, -ās", ".*i", new Integer[]{13, 14},
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM},
				new Tuple[]{TFeatures.POS__ADJ}), // daudzi

		// Paradigma 42: -is/-usi
		Participle.isUsi("-gušais; s. -gusi, -gusī", ".*dzis"), // aizdudzis
		Participle.isUsi("-kušais; s. -kusi, -kusī", ".*cis"), // jauniznācis
		Participle.isUsi("-ušais; s. -usi, -usī", ".*[cdjlmprstv]is"), // aizkūpis
		Participle.isUsi("-ušais, s. -usi, -usī", ".*[bdjt]is"), // caurkritis
		// Paradigma 43: -ies/-usies
		Participle.iesUsies("s. -usies", ".*ies"), // izdevies, pusapģērbies
		//BaseRule.of("s. -usies", ".*ies", 43,
		//		new Tuple[]{TFeatures.POS__PARTICIPLE_IS}, null), // izdevies

		// Paradigmas: 13, 14 - īpašības vārdi vai divdabji
		Participle.tsTa("-ais; s. -a, -ā, divd. īp. v. nozīmē", ".*ts"), // nerafinēts
		BaseRule.of("-ais; s. -a, -ā; divd. īp. v. nozīmē", new SimpleSubRule[]{
						SimpleSubRule.of(".*ošs", new Integer[]{13}, new Tuple[]{TFeatures.POS__PARTICIPLE, TFeatures.POS__PARTICIPLE_OSS}),
						SimpleSubRule.of(".*ts", new Integer[]{13}, new Tuple[]{TFeatures.POS__PARTICIPLE, TFeatures.POS__PARTICIPLE_TS}),
						SimpleSubRule.of(".*[aā]ms", new Integer[]{13}, new Tuple[]{TFeatures.POS__PARTICIPLE, TFeatures.POS__PARTICIPLE_AMS}),},
				new Tuple[]{TFeatures.CONTAMINATION__ADJECTIVE}), // maskējošs, mazaizsargāts, nezināms, vienreizlietojams
		BaseRule.of("-ais; s. -a, -ā; divd. īp. nozīmē", new SimpleSubRule[]{
						SimpleSubRule.of(".*ošs", new Integer[]{13}, new Tuple[]{TFeatures.POS__PARTICIPLE, TFeatures.POS__PARTICIPLE_OSS}),
						SimpleSubRule.of(".*ts", new Integer[]{13}, new Tuple[]{TFeatures.POS__PARTICIPLE, TFeatures.POS__PARTICIPLE_TS}),
						SimpleSubRule.of(".*[aā]ms", new Integer[]{13}, new Tuple[]{TFeatures.POS__PARTICIPLE, TFeatures.POS__PARTICIPLE_AMS}),},
				new Tuple[]{TFeatures.CONTAMINATION__ADJECTIVE}), // tiesībsargājošs
		Adjective.std("-ais; s. -a, -ā; īp. v."), // sovjetisks
		Adjective.std("-ais; s. -a, -ā; īp."), // albīns
		MultiPos.adjectiveParticiple("-ais; s. -a, -ā"), // abējāds, acains, agāms
		//MultiPos.adjectiveParticiple("-ais; s. -a; -ā"), // aloģisks
		//MultiPos.adjectiveParticiple("-ais, s. -a, -ā"), // abējāds, acains, agāms
		//MultiPos.adjectiveParticiple("-ais, -a, -ā"), // pamīšs, supervienkāršs
		//MultiPos.adjectiveParticiple("-ais, v."), // aizmugurējs
		BaseRule.of("s. -as; tikai dsk.", new SimpleSubRule[]{
						//SimpleSubRule.of(".*oši", new Integer[]{13}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*ti", new Integer[]{13}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.ENTRYWORD__PLURAL, TFeatures.USUALLY_USED__INDEFINITE, TFeatures.UNCLEAR_POS}),
						//SimpleSubRule.of(".*dami", new Integer[]{13, 0}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_DAMS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						//SimpleSubRule.of(".*[aā]mi", new Integer[]{13}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_POS}),
						//SimpleSubRule.of(".*uši", new Integer[]{13, 42}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_IS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*īgi", new Integer[]{13}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.ENTRYWORD__PLURAL, TFeatures.USUALLY_USED__INDEFINITE}),
						SimpleSubRule.of(".*ēji", new Integer[]{13}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.ENTRYWORD__PLURAL, TFeatures.USUALLY_USED__INDEFINITE})},
				new Tuple[]{TFeatures.USED_ONLY__PLURAL}), // abēji 1, aizkomentētajiem nebija instanču
				// Šķiet, ka pēc -t- un -am- nemēdz sekot īpašības vārda galotne š.
		// Paradigma: 30 - jaundzimušais, pēdējais
		BaseRule.of("-ā, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*tais", new Integer[] {30},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*ušais", new Integer[] {30},
								new Tuple[]{TFeatures.POS__PARTICIPLE_IS, TFeatures.CONTAMINATION__NOUN}),
						SimpleSubRule.of(".*[aā]mais", new Integer[] {30},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*([^tšm]|[^aā]m|[^u]š)ais", new Integer[] {30},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.CONTAMINATION__NOUN})},
				new Tuple[]{TFeatures.GENDER__MASC}),
			//pirmdzimtais, ieslodzītais, cietušais, brīvprātīgais, mīļākais, sirmais, svešais
		BaseRule.of("-šā, v.", ".*ušais", 30,
				new Tuple[]{TFeatures.POS__PARTICIPLE_IS, TFeatures.CONTAMINATION__NOUN},
				new Tuple[]{TFeatures.GENDER__MASC}), // iereibušais

		// 34 paradigma: Atgriezeniskie lietvārdi -šanās
		// 40 paradigma: Siev. dz. ar not. galotni
		BaseRule.of("-ās, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*šanās", new Integer[]{34},
								new Tuple[]{TFeatures.POS__REFL_NOUN}),
						SimpleSubRule.of(".*tā", new Integer[]{40},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*ošā", new Integer[]{40},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*[aā]mā", new Integer[]{40},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*([^tšm]|[^aā]m)ā", new Integer[]{40},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.CONTAMINATION__NOUN, TFeatures.DEFINITE_ENDING})},
				new Tuple[]{TFeatures.GENDER__FEM}), // pirmdzimtā, notiesātā, vispirmā, -šanās
		BaseRule.of("-ušās, s.", ".*usī", 41,
				new Tuple[]{TFeatures.POS__PARTICIPLE_IS, TFeatures.CONTAMINATION__NOUN},
				new Tuple[]{TFeatures.GENDER__FEM}), // cietusī
		BaseRule.of("-šās, s.", ".*usī", 41,
				new Tuple[]{TFeatures.POS__PARTICIPLE_IS, TFeatures.CONTAMINATION__NOUN},
				new Tuple[]{TFeatures.GENDER__FEM}) // jaundzimusī
	};

	public static final EndingRule[] be = {
		// būt
		VerbDoubleRule.of(
				"esmu, esi, ir, 3. pers. nolieguma forma nav, dsk. esam, esat, ir, 3. pers. nolieguma forma nav, pag. biju, biji, bija (arī bij), dsk. bijām, bijāt, bija (arī bij), vajadzības izteiksme jābūt", null,
				"būt", 50,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"būt\""), TFeatures.PARALLEL_FORMS,
						TFeatures.POS__IRREG_VERB, TFeatures.POS__DIRECT_VERB},
				null), // būt

		VerbDoubleRule.of("parasti divd. formā: izbijis, retāk pag. -biju, -biji, -bija, dsk. -bijām, -bijāt, -bija", null,
				"būt", 50,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"būt\""), TFeatures.POS__IRREG_VERB,
						TFeatures.POS__DIRECT_VERB},
				new Tuple[]{Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.PARTICIPLE_IS), Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.PARTICIPLE),Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.PAST), TFeatures.ORIGINAL_NEEDED}), // izbūt
		VerbDoubleRule.of("tagadnes formas nelieto, pag. -biju, -biji, -bija, dsk. -bijām, -bijāt, -bija", null,
				"būt", 50,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"būt\""), TFeatures.POS__IRREG_VERB,
						TFeatures.POS__DIRECT_VERB},
				new Tuple[]{Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.NO_PRESENT)}), // pabūt, sabūt
		VerbDoubleRule.of("parasti pag. -biju, -biji, -bija, dsk. -bijām, -bijāt, -bija", null,
				"būt", 50,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"būt\""), TFeatures.POS__IRREG_VERB,
						TFeatures.POS__DIRECT_VERB},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PAST)}), // pārbūt

		// nebūt
		VerbDoubleRule.of(
				"neesmu, neesi, nav, pag. nebiju", null,
				"būt", 50,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"nebūt\""), TFeatures.POS__IRREG_VERB,
						TFeatures.POS__DIRECT_VERB},
				null), // nebūt
	};
	/**
	 * Vietniekvārdu likumi. Krietna tiesa ir speciāli izveidoti, papildus
	 * pieliekot norādes par būšanu vietniekvārdam, lai nesajūk ar citām
	 * vārdšķirām.
	 * Tiek ļoti cerēts, ka te ir visi.
	 */
	public static final EndingRule[] pronomen = {
			// TODO pakāpeniski izravēt dubultlikumus
		BaseRule.of("vietn., -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC}), // kurš, kurš, mans, viņš
		BaseRule.of("vietn. -u, v.", ".*i", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL}), // abi
		BaseRule.of("-a, v.; vietn.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC}), // vienotrs
		BaseRule.of("-a, v.; nenoteiktais vietn.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.GENDER__MASC}), // jebkas, jebkurš, jebkāds
		BaseRule.of("nenot. vietn. -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.GENDER__MASC}), // dažs
		BaseRule.of("nenot. vietn. -as, s.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.GENDER__FEM}), // daža
		BaseRule.of("nenoteiktais vietn. -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.GENDER__MASC}), // cits
		BaseRule.of("-a, v.; norād. vietn.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__MASC}), // šāds, šitāds
		BaseRule.of("norād. vietn. -a, v.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__MASC}), // tāds
		BaseRule.of("-a, v.; pieder. vietn.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__POSS_PRONOUN, TFeatures.GENDER__MASC}), // tavs, savs
		BaseRule.of("noliedz. vietn., -a, v.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__NEG_PRONOUN, TFeatures.GENDER__MASC}), // nekāds, neviens
		BaseRule.of("vispārin. vietn. -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__GEN_PRONOUN, TFeatures.GENDER__MASC}), // ikkurš, ikkatrs
		BaseRule.of("vispārin. vietn., -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__GEN_PRONOUN, TFeatures.GENDER__MASC}), // ikviens

		BaseRule.of("vietn., -as, s.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__FEM}), // ref: šāda, jebkāda
		BaseRule.of("vietn., -u, s.", ".*as", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__FEM, TFeatures.ENTRYWORD__PLURAL}), // abas
		BaseRule.of("-as, s.; vietn.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__POSS_PRONOUN, TFeatures.GENDER__FEM}), // viņa, kāda
		BaseRule.of("nenoteiktais vietn., -as, s.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.GENDER__FEM}), // jebkura, jebkāda
		BaseRule.of("-as, s.; pieder. vietn.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__POSS_PRONOUN, TFeatures.GENDER__FEM}), // sava, mana, tava
		BaseRule.of("norād. vietn., -as, s.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__FEM}), // šitāda
		BaseRule.of("noliedz. vietn., -as, s.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__NEG_PRONOUN, TFeatures.GENDER__FEM}), // nekāda
		BaseRule.of("vispārin. vietn., -as, s.", ".*a", 25,
				null, new Tuple[]{TFeatures.POS__GEN_PRONOUN, TFeatures.GENDER__FEM}), // ikkura

		BaseRule.of("vietn. paša, pašam, pašu, ar pašu, pašā, dsk. ģen. pašu, v.", "pats", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC}), // pats
		BaseRule.of("pašas, pašai, pašu, ar pašu, pašā, dsk. ģen. pašu, s.; noteic. vietn.", "pati", 25,
				null, new Tuple[]{TFeatures.POS__DEF_PRONOUN, TFeatures.GENDER__FEM}), // pati
		BaseRule.of("vietn.; ģen. šā (arī šī), dat. šim, akuz. šo, instr. ar šo, lok. šai (arī šinī, šajā), dsk. nom. šie, ģen. šo, dat. šiem, akuz. šos, instr. ar šiem, lok. šajos (arī šais, šinīs), v.",
				"šis", 25,
				new Tuple[]{TFeatures.PARALLEL_FORMS}, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC}), // šis
		BaseRule.of("ģen. šās (arī šīs), dat. šai, akuz. šo, instr. ar šo, lok. šai (arī šinī, šajā ), dsk. nom. šīs (arī šās), ģen. šo, dat. šīm, (arī šām), akuz. šīs (arī šās), instr. ar šīm (arī šām), lok. šajās (arī šais, šinīs), s.; norād. vietn.",
				"šī", 25,
				new Tuple[]{TFeatures.PARALLEL_FORMS}, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__FEM}), // šī
		BaseRule.of("vietn.; ģen. tā, dat. tam, akuz. to, instr. ar to, lok. tai (arī tanī, tajā), dsk. nom. tie, ģen. to, dat. tiem, akuz. tos, instr. ar tiem, lok. tajos (arī tais, tanīs), v.",
				"tas", 25,
				new Tuple[]{TFeatures.PARALLEL_FORMS}, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC}), // tas
		BaseRule.of("ģen. tās, dat. tai, akuz. to, instr. ar to, lok. tai (arī tanī, tajā), dsk. nom. tās, ģen. to, akuz. tās, instr. ar tām, lok. tajās (arī tais, tanīs), s.; norād. vietn.",
				"tā", 25,
				new Tuple[]{TFeatures.PARALLEL_FORMS}, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__FEM}), // tas
		BaseRule.of("ģen. jebkā, dat. jebkam, akuz. jebko, instr. ar jebko; tikai vsk.; nenoteiktais vietn.",
				"jebkas", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.USED_ONLY__SINGULAR}), // jebkas
		BaseRule.of("ģen. kā, dat. kam, akuz. ko, instr. ar ko, lok. kamī (apv.); vietn.", "kas", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.ORIGINAL_NEEDED}), // kas
		BaseRule.of("ģen. -kā, dat. -kam, akuz., instr. -ko; nenoteiktais vietn.", "daudzkas", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN}), // daudzkas
		BaseRule.of("ģen. nekā, dat. nekam, akuz. neko, instr. ar neko; noliedz. vietn.", "nekas", 25,
				null, new Tuple[]{TFeatures.POS__NEG_PRONOUN}), // nekas

		BaseRule.of("ģen. manis, dat. man, ak. mani, instr. ar mani (ar manim), lok. manī; pers. vietn. (vsk. 1. pers.).",
				"es", 25,
				new Tuple[]{TFeatures.PARALLEL_FORMS}, new Tuple[]{TFeatures.POS__PERS_PRONOUN}), // es
		BaseRule.of("ģen. tevis, dat. tev, akuz. tevi, instr. ar tevi (ar tevim), lok. tevī; pers. vietn. (vsk. 2. pers.).",
				"tu", 25,
				new Tuple[]{TFeatures.PARALLEL_FORMS}, new Tuple[]{TFeatures.POS__PERS_PRONOUN}), // tu
		BaseRule.of("ģen. mūsu, dat. mums, akuz. mūs, instr. ar mums, lok. mūsos; vietn. (dsk. 1. pers.).",
				"mēs", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN}), // mēs
		BaseRule.of("ģen. jūsu, dat. jums, akuz. jūs, instr. ar jums, lok. jūsos; vietn. (dsk. 2. pers.).",
				"jūs", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN}), // jūs
		BaseRule.of("ģen.; dat. sev, akuz. sevi, instr. ar sevi, lok. sevī; atgriez. vietn.", "sevis", 25,
				null, new Tuple[]{TFeatures.POS__REFL_PRONOUN}), // sevis

		BaseRule.of("ģen. šitā, dat. šitam, akuz. šito, instr. ar šito, lok. šitai (arī šitanī, šitajā), dsk. nom. šitie, ģen. šito, dat. šitiem, akuz. šitos, instr. ar šitiem, lok. šitais (arī šitajos, šitos, šitanīs), v., vietn.",
				"šitas", 25,
				null, new Tuple[]{TFeatures.PARALLEL_FORMS, TFeatures.POS__DEF_PRONOUN, TFeatures.GENDER__MASC}), // šitas
		BaseRule.of("ģen. šitā, dat. šitajam, akuz. šito, instr. ar šito, lok. šitai (arī šitanī, šitajā), dsk. nom. šitie, ģen. šito, dat. šitajiem, akuz. šitos, instr. ar šitajiem, lok. šitais (arī šitajos, šitanīs), v., norād. vietn.",
				"šitais", 25,
				null, new Tuple[]{TFeatures.PARALLEL_FORMS, TFeatures.POS__DEF_PRONOUN, TFeatures.GENDER__MASC}), // šitais
		BaseRule.of("ģen. šitās, dat. šitai, akuz. šito, instr. ar šito, lok. šitai (arī šitanī, šitajā), dsk. nom. šitās, ģen. šito, dat. šitām, akuz. šitās, instr. ar šitām, lok. šitais (arī šitajās, šitanīs), s., norād. vietn.",
				"šitā", 25,
				null, new Tuple[]{TFeatures.PARALLEL_FORMS, TFeatures.POS__DEF_PRONOUN, TFeatures.GENDER__FEM}), // šitā

		BaseRule.of("dat. jam, vietniekv.", "jis", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN}) // jis
	};

	/**
	 * Skaitļa vārdu likumi. Krietna tiesa ir speciāli izveidoti, papildus
	 * pieliekot vārdšķiru, lai nesajūk ar citām vārdšķirām.
	 * Tiek ļoti cerēts, ka te ir visi.
	 */
	public static final EndingRule[] numeral = {
		// NB! Pašlaik nelokāmie skaitļa vārdi iet Hardcoded paradigmā.
		/*BaseRule.of("nelok.; pamata skait.", ".*t", 29,
				null, new Tuple[]{TFeatures.POS__CARD_NUMERAL, TFeatures.NON_INFLECTIVE}), // deviņsimt
		BaseRule.of("nelok.; daļu skait.", ".*pus", 29,
				null, new Tuple[]{TFeatures.POS__FRACT_NUMERAL, TFeatures.NON_INFLECTIVE}), // četrarpus, divarpus
		*/
		// Eksperimentālā ideja: visi skaitļa vārdi, kam ir tikai viena dzimte,
		// lai ir lietvārdi.
		//BaseRule.of("-a, v.; pamata skait. lietvārda nozīmē.", ".*[dtn]s", 1,
		//		null, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN, TFeatures.CONTAMINATION__CARD_NUM}), // desmits, pussimts
		BaseRule.of("-a, v.; pamata skait. lietv. nozīmē.", ".*[dtn]s", 1,
				null, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN, TFeatures.CONTAMINATION__CARD_NUM}), // simts1, miljons, pusmiljons, miljards, triljons
		BaseRule.of("-ša, v.; pamata skait. lietv. nozīmē.", ".*tis", 3,
				null, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN, TFeatures.CONTAMINATION__CARD_NUM}), // tūkstotis
		GenNoun.any("nulles, dsk. ģen. nuļļu, s.", "nulle", 9,
				new Tuple[]{TFeatures.CONTAMINATION__CARD_NUM},
				new Tuple[]{TFeatures.GENDER__FEM}), // nulle
		// Puse un ??daļa arī kļūst par lietvārdiem, turklāt pat bez
		// kontaminācijas, 		//

		BaseRule.of("skait.; s. -ā", ".*ais", 22,
				new Tuple[]{TFeatures.POS__ORD_NUMERAL, TFeatures.DEFINITE_ENDING},
				null), // ceturtais, otrais, nultais
		BaseRule.of("skait.; s. -a", ".*s", 23,
				null, new Tuple[]{TFeatures.POS__CARD_NUMERAL}), // otrs
		BaseRule.of("s. -a; daļu skait.", ".*s", 23,
				null, new Tuple[]{TFeatures.POS__FRACT_NUMERAL}), // pusotrs, pustrešs

		BaseRule.of("s. -as; pamata skait.", ".*i", 24,
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL},
				new Tuple[]{TFeatures.POS__CARD_NUMERAL}), // deviņi, divi, trejdeviņi

		BaseRule.of("ģen. triju, dat. trim (arī trijiem), akuz. trīs, instr. ar trim (arī ar trijiem), lok. trijos (arī trīs), v.; " +
				"ģen. triju, dat. trim (arī trijām), akuz. trīs, instr. ar trim (arī ar trijām), lok. trijās (arī trīs), s.; " +
				"arī trīs visu locījumu formu nozīmē; pamata skait.",
				"trīs", 29,
				null, new Tuple[]{TFeatures.POS__CARD_NUMERAL, TFeatures.PARALLEL_FORMS}), // trīs

		// TODO: pulsteņlaiki
	};

	/**
	 * Paradigm 7, 8: 4. deklinācija.
	 */
	public static final EndingRule[] forthDeclNoun = {
		// Paradigmas: 7, 8 - kopdzimtes lietvārdi, galotne -a
		GenNoun.any("ģen. -as, v. dat. -am, s. dat. -ai, kopdz.", ".*a", new Integer[]{7, 8}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}), // aitasgalva, aizmārša
		GenNoun.any("ģen. -as, v. dat. -am, s. dat. -ai; kopdz.", ".*a", new Integer[]{7, 8}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}), // tiepša
		GenNoun.any("-as, v. dat. -am, s. dat. -ai, kopdz.", ".*a", new Integer[]{7, 8}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}), // žūpa
		GenNoun.any("kopdz.: dat. v. -am, s. -ai", ".*a", new Integer[]{7, 8}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}), // barista

		// 7. paradigma: 4. dekl. lietvārdi, sieviešu dzimte
		GenNoun.any("-as, dsk. ģen. -u, s.", ".*a", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // alpa
		GenNoun.any("-jas, dsk. ģen. -ju, s.", ".*ja", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // kastrētāja
		GenNoun.any("-as, dsk. ģen. -du, s.", ".*da", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // aldrovanda
		GenNoun.any("-as, dsk. ģen. -ju, s.", ".*ja", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // vaivadija
		GenNoun.any("-as, dsk. ģen. -pu, s.", ".*pa", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // aizsarglapa
		GenNoun.any("-as, dsk. ģen. -stu, s.", ".*sta", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // pasta
		GenNoun.any("-as, dsk. ģen. -tu, s.", ".*ta", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // placenta
		GenNoun.any("-as, dsk. ģen. -vu, s.", ".*va", 7, null, new Tuple[] {TFeatures.GENDER__FEM}), // apskava

		// 7. paradigma: 4. dekl. lietvārdi, vīriešu dzimte
		GenNoun.any("ģen. -as, dat. -am, v.", ".*a", 8, null, new Tuple[] {TFeatures.GENDER__MASC}), // papa
		GenNoun.any("-as, v.", ".*a", 8, null, new Tuple[] {TFeatures.GENDER__MASC}), // puika
	};

	/**
	 * Paradigm 9, 10, 44, 47: Lietvārds 5. deklinācija -e
	 */
	public static final EndingRule[] fifthDeclNoun = {
		// Paradigmas: 9, 10 - kopdzimtes lietvārdi, galotne -
		GenNoun.any("ģen. -es, v. dat. -em, s. dat. -ei, dsk. ģen. -tu, kopdz.",
				".*te", new Integer[]{44, 47}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}), // balamute

			//ģen. -es, v. dat. -em, s. dat. -ei, dsk. ģen. -ču, kopdz.
		GenNoun.any("ģen. -es, v. dat. -em, s. dat. -ei, dsk. ģen. -ču, kopdz.",
				".*ce", new Integer[]{9, 10}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}), // ekselence
		GenNoun.any("ģen. -es, v. dat. -em, s. dat. -ei, dsk. ģen. -ru, kopdz.",
				".*re", new Integer[]{9, 10}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}),// aitaspiere
		GenNoun.any("ģen. -es, v. dat. -em, s. dat. -ei, dsk. ģen. -žu, kopdz.",
				".*de", new Integer[]{9, 10}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}),// bende

		// Paradigma: 9 - sieviešu dzimte.
		//FifthDecl.std("-es, dsk. ģen. -ķu, s.", ".*ķe"), //ciniķe

		// Standartizētie
		FifthDecl.std("dsk. ģen. -ču, s.", ".*[cč]e"), //ietece
		FifthDecl.std("-es, dsk. ģen. -ču, s.", ".*[cč]e"), //ābece, veče
		FifthDecl.std("ģen. -es, dat. -ei, dsk. ģen. -ču, s.", ".*[cč]e"), //eminence
		FifthDecl.std("-es, dsk. ģen. -ģu, s.", ".*[ģ]e"), //aeroloģe
		FifthDecl.std("-es, dsk. ģen. -ju, s.", ".*je"), //baskāje, aloje
		FifthDecl.std("-es, dsk. ģen. -ķu, s.", ".*ķe"), //agnostiķe, leduspuķe
		FifthDecl.std("-es, dsk. ģen. -ļu, s.", ".*le"), //ābele
		FifthDecl.std("-es, dsk. ģen. -ņu, s.", ".*[n]e"), //ābolaine
		FifthDecl.std("-es, dsk. ģen. -ru, s.", ".*re"), //administratore, ādere
		FifthDecl.std("dsk. ģen. -ru, s.", ".*re"), // aizsargcepure
		FifthDecl.std("-es, dsk. ģen. -šu, s.", ".*[sšt]e"), //abate, adrese, larkše, apokalipse, note
		FifthDecl.std("-es, dsk. ģen. -žu, s.", ".*[dz]e"), //ābolmaize, aģitbrigāde, bilde, pirolīze
		FifthDecl.std("dsk. ģen. -žu, s.", ".*[dz]e"), //izklaide

		FifthDecl.std("-es, dsk. ģen. -bju, s.", ".*be"), //apdobe
		FifthDecl.std("-es, dsk. ģen. -džu, s.", ".*dze"), //kāršaudze
		FifthDecl.std("-es, dsk. ģen. -kšu, s.", ".*kte"), //daudzpunkte
		FifthDecl.std("-es, dsk. ģen. -ļļu, s.", ".*lle"), //zaļumballe
		FifthDecl.std("-es, dsk. ģen. -ļņu, s.", ".*lne"), //nokalne
		FifthDecl.std("-es, dsk. ģen. -mju, s.", ".*me"), //agronome, krustamzīme
		FifthDecl.std("-es, dsk. ģen. -smju, s.", ".*sme"), //noslieksme
		FifthDecl.std("-es, dsk. ģen. -šņu, s.", ".*sne"), //izloksne, aizkrāsne
		FifthDecl.std("dsk. ģen. -šņu, s.", ".*sne"), //apaļkoksne
		FifthDecl.std("-es, dsk. ģen. -šķu, s.", ".*šķe"), //draišķe
		FifthDecl.std("-es, dsk. ģen. -šļu, s.", ".*sle"), //gaisagrābsle
		FifthDecl.std("dsk. ģen. -vju, s.", ".*ve"), //ieskrietuve
		FifthDecl.std("-es, dsk. ģen. -vju, s.", ".*ve"), //agave, aizstāve
		FifthDecl.std("-es, dsk. ģen. -žņu, s.", ".*zne"), //asteszvaigzne

		FifthDecl.std("-es, -žu s.", ".*ze"), // apoteoze

		FifthDecl.std("-es, -mju, s.", ".*me"), // apakšzeme

		FifthDecl.std("vsk. -es, s.", ".*e"), // antikvitāte

		FifthDecl.noChange("-es, dsk. ģen. -du, s.", ".*de"), // diplomande
		//FifthDecl.noChange("-es, dsk. ģen. -fu, s.", ".*fe"), //vairs nav, jo atļāva miju.
		FifthDecl.noChange("-es, dsk. ģen. -su, s.", ".*se"), // bise
		FifthDecl.noChange("-es, dsk. ģen. -stu, s.", ".*ste"), //abolicioniste
		FifthDecl.noChange("dsk. ģen. -tu, s.", ".*te"), //artiste
		FifthDecl.noChange("dsk. ģen. -stu, s.", ".*ste"), //animiste
		FifthDecl.noChange("-es, dsk. ģen. -tu, s.", ".*te"), // antisemīte
		FifthDecl.noChange("-es, dsk. ģen. -zu, s.", ".*ze"), // autobāze


			// Vienskaitlis + daudzskaitlis
		GenNoun.any("-es, dsk. ģen. -pju, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*pe", new Integer[]{9}, null),
						SimpleSubRule.of(".*pes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL})},
				new Tuple[]{TFeatures.GENDER__FEM}), // aitkope, antilope, tūsklapes
		GenNoun.any("dsk. ģen. -pju, s.", new SimpleSubRule[]{
						//SimpleSubRule.of(".*pe", new Integer[]{9}, null),
						SimpleSubRule.of(".*pes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL})},
				new Tuple[]{TFeatures.GENDER__FEM}), // galvassāpes

		// Nejauki, pārāk specifiski gdījumi
		// Šiem defišu izņemšanas mehānisms īsti neder, jo vienā vietā vajag atstāt.
		FifthDecl.std("-es, dsk. ģen. āru, s.", ".*āre"), //āre
		//BaseRule.std("-es, dsk. ģen. biržu, s.", ".*birze"), //birze
		FifthDecl.std("-es, dsk. ģen. pūšu, s.", ".*pūte"), //pūte 1, 3
		FifthDecl.std("-es, dsk. ģen. puvju, s.", ".*puve"), //puve
		FifthDecl.std("-es, dsk. ģen. šaļļu, s.", ".*šalle"), //šalle
		//BaseRule.std("-es, dsk. ģen. -upju, s.", ".*upe"), //dzirnavupe

		// Miju varianti
		FifthDecl.optChange("-es, dsk. ģen. mufu, arī mufju, s.", ".*mufe"), //mufe
		FifthDecl.optChange("-es, dsk. ģen. -fu, arī -fju, s.", ".*fe"), //arheogrāfe
		FifthDecl.optChange("-es, dsk. ģen. -šu vai -tu, s.", ".*te"), //cunfte, manšete
		FifthDecl.optChange("-es, dsk. ģen. -šu, arī -tu, s.", ".*te"), //torte
		FifthDecl.optChange("-es, dsk. ģen. -stu, arī -šu, s.", ".*ste"), //dzeņaukste
		FifthDecl.optChange("-es, dsk. ģen. -šu, arī -stu, s.", ".*ste"), //plekste

		FifthDecl.optChange("-es, dsk. ģen. lešu vai letu", ".*lete"), //lete

		// Nestandartīgie
		GenNoun.any("-ķu, s.", new SimpleSubRule[]{
				SimpleSubRule.of(".*ķes", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL})},
				//SimpleSubRule.of(".*le", 9, null)},
			new Tuple[]{TFeatures.GENDER__FEM}), //duļķes
		GenNoun.any("-ļu, s.", new SimpleSubRule[]{
				SimpleSubRule.of(".*les", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*le", 9, null)},
			new Tuple[]{TFeatures.GENDER__FEM}), //bailes, abisāle
		GenNoun.any("-ņņu, s.", new SimpleSubRule[]{
				SimpleSubRule.of(".*nnes", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL})},
				//SimpleSubRule.of(".*nne", 9, null)},
			new Tuple[]{TFeatures.GENDER__FEM}), //pinnes
		GenNoun.any("-šņu, s.", new SimpleSubRule[]{
				//SimpleSubRule.of(".*snis", 11, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*snes", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				/*SimpleSubRule.of(".*sne", 9, null)*/},
				new Tuple[]{TFeatures.GENDER__FEM}), // nogulsnes
		GenNoun.any("-ru, s.", new SimpleSubRule[]{
				SimpleSubRule.of(".*res", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*re", 9, null)},
			new Tuple[]{TFeatures.GENDER__FEM}), //apakšīre, asinsdzīres
		GenNoun.any("-mju, s.", new SimpleSubRule[]{
				//SimpleSubRule.of(".*mis", 11, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*mes", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*me", 9, null)},
			new Tuple[]{TFeatures.GENDER__FEM}), //ārzemes, mācītprasme
		GenNoun.any("-pju, s.", new SimpleSubRule[]{
				//SimpleSubRule.of(".*pis", 11, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*pes", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL})},
				//SimpleSubRule.of(".*pe", 9, null)},
			new Tuple[]{TFeatures.GENDER__FEM}), //atsāpes
		GenNoun.any("-vju, s.", new SimpleSubRule[]{
				SimpleSubRule.of(".*vis", 11, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*ves", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*ve", 9, null)},
			new Tuple[]{TFeatures.GENDER__FEM}), //apaļgalve, ārdurvis, nestuves
		GenNoun.any("-žņu, s.", new SimpleSubRule[]{
				//SimpleSubRule.of(".*znis", 11, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				SimpleSubRule.of(".*znes", 9, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				//SimpleSubRule.of(".*zne", 9, null)
				},
			new Tuple[]{TFeatures.GENDER__FEM}), //grieznes

	};

	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final EndingRule[] secondDeclNoun = {
		SecondDecl.std("-bja, dsk. ģen. -bju, v.", ".*bis"), //ledusurbis
		SecondDecl.std("-ča, dsk. ģen. -ču, v.", ".*cis"), //labrocis
		SecondDecl.std("-ķa, dsk. ģen. -ķu, v.", ".*ķis"), //aizsarglenķis
		SecondDecl.std("-ļa, dsk. ģen. -ļu, v.", ".*lis"), //brokolis
		SecondDecl.std("-ņa, dsk. ģen. -ņu, v.", ".*nis"), //bizmanis
		SecondDecl.std("-pja, dsk. ģen. -pju, v.", ".*pis"), //grāmatskapis
		SecondDecl.std("-ša, dsk. ģen. -šu, v.", ".*[st]is"), //auseklītis, lāčplēsis
		SecondDecl.std("-vja, dsk. ģen. -vju, v.", ".*vis"), //kapātuvis
		SecondDecl.std("-ža, dsk. ģen. -žu, v.", ".*[dz]is"), //diskvedis, plakandzelzis

		SecondDecl.stdNomGen("-ns, dsk. ģen. -ņu, v.", ".*ns"), // bruģakmens

		SecondDecl.std("-bja, v.", ".*bis"), //aizsargdambis
		SecondDecl.std("-dža, v.", ".*dzis"), //algādzis
		SecondDecl.std("-kļa, v.", ".*klis"), // krīklis
		SecondDecl.std("-ļļa, v.", ".*llis"), // amarillis
		SecondDecl.std("-ļņa, v.", ".*lnis"), // aizsargvalnis
		SecondDecl.std("-ņņa, v.", ".*nnis"), // hunnis
		SecondDecl.std("-mja, v.", ".*mis"), // aplamis
		SecondDecl.std("-šķa, v.", ".*šķis"), // draišķis
		SecondDecl.std("-šļa, v.", ".*slis"), // bauslis
		SecondDecl.std("-šņa, v.", ".*snis"), // alksnis
		SecondDecl.std("-pja, v.", ".*pis"), //aitkopis
		SecondDecl.std("-vja, v.", ".*vis"), //aizstāvis
		SecondDecl.std("-žņa, v.", ".*znis"), //aitkopis

		SecondDecl.std("vsk. -ķa, v.", ".*ķis"), // antibiotiķi/antibiotiķis

		SecondDecl.std("-ča, v.", ".*cis"), // akacis
		SecondDecl.std("-ģa, v.", ".*ģis"), // āliņģis
		SecondDecl.std("-ķa, v.", ".*ķis"), //agnostiķis
		SecondDecl.std("-ža, v.", ".*[dzž]is"), //ādgrauzis

		//SecondDecl.std("-ša, vsk.", ".*tis"), //aizkrācietis
	};

	/**
	 * Īsie šabloni ar vienu galotni un dzimti.
	 * Visiem likumiem ir iespējami vairāki galotņu varianti, bet uzskaitīti
	 * ir tikai vārdnīcā sastaptie.
	 */
	public static final EndingRule[] nounMultiDecl = {
		// Vienskaitlis, vīriešu dzimte
		// Ar mijām
		GenNoun.any("-a, dsk. ģen. -u, v.", ".*[^aeiouāēīōū]s", 1,
				null, new Tuple[]{TFeatures.GENDER__MASC}), // adventists
		GenNoun.any("-ja, dsk. ģen. -ju, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*js", 1, null),
						SimpleSubRule.of(".*jis", 3, null)},
				new Tuple[] {TFeatures.GENDER__MASC}), // algotājs

		GenNoun.any("vsk. -ņa, v.", ".*nis", 3, null,
				new Tuple[]{TFeatures.GENDER__MASC, Tuple.of(TKeys.NUMBER, TValues.SINGULAR)}), // amerikānis
		GenNoun.any("-ļa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*lis", new Integer[]{3}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // acumirklis, (bacils, durkls=kļūda)
		GenNoun.any("-ņa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ņš", new Integer[]{2}, null),
						SimpleSubRule.of(".*nis", new Integer[]{3}, null),
						SimpleSubRule.of(".*(suns|sāls|uguns)", new Integer[]{5}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // abesīnis, dižtauriņš, dzinējsuns
		GenNoun.any("-ša, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[sšt]is", new Integer[]{3}, null),
						SimpleSubRule.of(".*ss", new Integer[]{5}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // abrkasis, lemess
		// Bez mijām
		GenNoun.any("-ja, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*js", new Integer[]{1}, null),
						SimpleSubRule.of(".*jis", new Integer[]{3}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // bezkājis, kastrētājs
		GenNoun.any("-ra, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*rs", new Integer[]{1}, null),
						SimpleSubRule.of(".*ris", new Integer[]{3}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // airis, mūrniekmeistars
		GenNoun.any("-sa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ss", new Integer[]{1}, null),
						SimpleSubRule.of(".*sis", new Integer[]{48}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // balanss, kūrviesis
		GenNoun.any("-ta, v.", ".*tis", new Integer[]{48},
				null, new Tuple[]{TFeatures.GENDER__MASC}), // stereotālskatis
		// Vispārīgā galotne, kas der visam un neder nekam
		GenNoun.any("-a, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{1}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]š", new Integer[]{2}, null),
						SimpleSubRule.of(".*ti", new Integer[]{1, 2}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*[ģjķr]is", new Integer[]{3}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // abats, akustiķis, sparguļi, skostiņi,

		BaseRule.of("lietv. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
					new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN}), // aerobs
		GenNoun.any("vsk. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
					new Tuple[]{TFeatures.GENDER__MASC, Tuple.of(TKeys.NUMBER, TValues.SINGULAR)}), // acteks
		GenNoun.any("-a, vsk.", new SimpleSubRule[]{
						SimpleSubRule.of(".*(akmen|asmen|mēnes|ziben|ūden|ruden)s", new Integer[]{4}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*(suns|sāls|uguns)", new Integer[]{5}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*is", new Integer[]{3}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*š", new Integer[]{2}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{1}, new Tuple[]{TFeatures.GENDER__MASC})},
				null), // aizkars, cīsiņš, sakņkājis

		// Daudzkaitlis, vīriešu dzimte
		// Ar mijām
		GenNoun.any("-ļu, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*sāļi", new Integer[]{5}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ļi", new Integer[]{1, 2, 3}, new Tuple[]{ TFeatures.UNCLEAR_PARADIGM, TFeatures.ENTRYWORD__PLURAL})},
				new Tuple[]{TFeatures.GENDER__MASC}), // akļi
		GenNoun.any("-ņu, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*(akme|asme|zibe|ūde|rude)ņi", new Integer[]{4}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*(su|ugu)ņi", new Integer[]{5}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ņi", new Integer[]{1, 2, 3}, new Tuple[]{TFeatures.UNCLEAR_PARADIGM, TFeatures.ENTRYWORD__PLURAL})},
				new Tuple[]{TFeatures.GENDER__MASC}), // bretoņi, jūrakmeņi
		GenNoun.any("-ču, v.", ".*či", new Integer[]{1, 3},
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM},
				new Tuple[]{TFeatures.GENDER__MASC}), // divriči
		GenNoun.any("-šu, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*mēneši", new Integer[]{3, 4}, new Tuple[]{TFeatures.UNCLEAR_PARADIGM, TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ši", new Integer[]{1, 3}, new Tuple[]{ TFeatures.UNCLEAR_PARADIGM, TFeatures.ENTRYWORD__PLURAL})},
				new Tuple[]{TFeatures.GENDER__MASC}), // alžīrieši
		GenNoun.any("-džu, v.", ".*dži", new Integer[]{1, 2, 3},
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM},
				new Tuple[]{TFeatures.GENDER__MASC}), // dobradži
		// Vispārīgā galotne, kas der visam un neder nekam
		GenNoun.any("-ru, v.", new SimpleSubRule[]{
						//SimpleSubRule.of(".*š", new Integer[]{2}, null),
						//SimpleSubRule.of(".*(vakari|svari)", new Integer[]{1}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*turi", new Integer[]{1, 2, 3}, new Tuple[]{TFeatures.UNCLEAR_PARADIGM, TFeatures.ENTRYWORD__PLURAL}),
						//SimpleSubRule.of(".*([ķr]|[aeiāē]j)i", new Integer[]{1, 2, 3, 4}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
				},
				new Tuple[]{TFeatures.GENDER__MASC}), // bikšturi

		GenNoun.any("-u, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*š", new Integer[]{2}, null),
						SimpleSubRule.of(".*(otāji|umi|anti|nieki|[aeiouāēīōū]īdi|isti|mēsli|svārki|plūdi|rati|vecāki|bērni|raksti|vidi|rīti|vakari|vārdi|kapi|augi|svētki|audi|laiki|putni|svari)",
								new Integer[]{1}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[bcdghklmnpstvz]i", new Integer[]{1}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*(ieši|āņi|ēži|grieži|stāvji|grauži|brunči|viļņi|ceļi|liberāļi|krampji|kaļķi)",
								new Integer[]{3}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*suņi", new Integer[]{5}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ši", new Integer[]{1, 3, 4}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*[čļņž]i", new Integer[]{2, 3, 4}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*(ģ|[mv]j)i", new Integer[]{3, 4}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*([ķr]|[aeiāē]j)i", new Integer[]{1, 2, 3, 4}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
				},
				new Tuple[]{TFeatures.GENDER__MASC}),
			// dededstiņš
			// atgremotāji, apstādījumi, antikoagulanti, austrumnieki, eiropeīdi,
				// leiboristi, amonijmēsli, apakšsvārki, asinsplūdi, atsperrati,
				// audžuvecāki, bērnubērni, ciltsraksti, dienvidi, dienvidrīti,
				// dienvidvakari, dievvārdi, dzimtkapi, čiekuraugi, pēcsvētki,
				// vadaudi, senlaiki, dziedātājputni, decimalsvari, dolomītkaļķi
			// abhāzi, arābi, alimenti, antinukloni, aplausi, ārdi, baltkrievi,
				// beduīni, būvkoki, būvmateriāli, čehi, dūmi, nevāci, kvēpi, varjagi
			// adigejieši, afgāņi, apakšbrunči, īsviļņi, urīnceļi, trūdēži, ādgrauži,
				// laikgrieži, saulstāvji, nacionalliberāļi, stingumkrampji
			// nav precedenta
			// anglosakši
			// abesīņi, angļi, čukči, būvgruži
			// beļģi, gliemji, latvji
			// adžāri, grieķi, ebreji, karačaji, maiji, rudenāji, sodrēji

			// NB! Likums neņem vērā 2.deklinācijas izņēmumus bez mijas.

		// Sieviešu dzimte, vienskaitlis
		// 7., 11. paradigma
		GenNoun.any("vsk. -as, s.", ".*a", new Integer[]{7},
				null, new Tuple[]{TFeatures.GENDER__FEM}), // antibiotikas/antibiotika
		GenNoun.any("-as; s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*a", new Integer[]{7}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), // anihilācija

		// Daudzkaitlis, sieviešu dzimte
		// 7., 9., 11. paradigma, iespējamas mijas
		GenNoun.any("-stu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*stas", 7,	new Tuple[] {TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*stis", 35,	new Tuple[] {TFeatures.ENTRYWORD__PLURAL})},
				new Tuple[] {Features.GENDER__FEM}), // salaistas, brokastis

		GenNoun.any("-ču, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ce", new Integer[]{9}, null),
						//SimpleSubRule.of(".*čas", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ces", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						//SimpleSubRule.of(".*cis", new Integer[]{11}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				},
				new Tuple[]{TFeatures.GENDER__FEM}), // absistnce
		GenNoun.any("-ņu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, null),
						SimpleSubRule.of(".*ņas", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nis", new Integer[]{11}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				},
				new Tuple[]{TFeatures.GENDER__FEM}), // acenes, iemaņas, balodene, robežugunis
		GenNoun.any("-šu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[st]e", new Integer[]{9}, null),
						SimpleSubRule.of(".*šas", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[st]es", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*tis", new Integer[]{11}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),},
				new Tuple[]{TFeatures.GENDER__FEM}), // ahajiete, aizkulises, autosacīkstes, klaušas, šķūtis
		GenNoun.any("-žu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*žas", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[dz]es", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[dz]e", new Integer[]{9}, null),},
				new Tuple[]{TFeatures.GENDER__FEM}), // mirādes, graizes, bažas, aponeiroze
		// Vispārīgā galotne, kas der visam un neder nekam
		GenNoun.any("-u, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*a", new Integer[]{7}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]as", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ķes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),},
				new Tuple[]{TFeatures.GENDER__FEM}), // aijas, spēķes, zeķes, konkrēcija

		// Sieviešu dzimte, vienskaitlis un daudzskaitlis
		// 7., 11. paradigma.
		GenNoun.any("-as, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*a", new Integer[]{7}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]as", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), // aberācija, milns, najādas

		// 7., 9., 11. paradigma.
		GenNoun.any("dsk. ģen. -ņu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, null),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ns", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), // ādmine, bākuguns, bārkšsaknes
		GenNoun.any("dsk. ģen. -šu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[ts]e", new Integer[]{9}, null),
						SimpleSubRule.of(".*[ts]es", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ts", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), //alžīriete, kroņprincese, autiņbiksītes, īsbikses, azots
		GenNoun.any("dsk. ģen. -ļu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*le", new Integer[]{9}, null),
						SimpleSubRule.of(".*les", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ls", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), //apakšcentrāle, dziesmuspēles, ezerpils

		GenNoun.any("-ņu, s.; tikai dsk.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ņas", 7, new Tuple[] {TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nes", 9, new Tuple[] {TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nis", 11, new Tuple[] {TFeatures.ENTRYWORD__PLURAL})},
				new Tuple[] {TFeatures.USED_ONLY__PLURAL, TFeatures.GENDER__FEM}), // aizsargacenes, durtiņas, robežugunis
		GenNoun.any("-ļļu, s.", ".*lles", 9,
				new Tuple[] {TFeatures.ENTRYWORD__PLURAL},
				new Tuple[] {TFeatures.USED_ONLY__PLURAL, TFeatures.GENDER__FEM}), // aizsargbrilles

	};

	/**
	 * Likumi, kas ir citu likumu prefiksi.
	 * Šajā masīvā jāievēro likumu secība, citādi slikti būs. Šo masīvu jālieto
	 * pašu pēdējo.
	 */
	public static final EndingRule[] dangerous = {
		// Paradigma: 9 - Lietvārds 5. deklinācija -e siev. dz.
		FifthDecl.std("-es, s.", ".*e"), //aizture + daudzi piemēri ar mijām
			// konflikts ar "astilbe" un "acetilsalicilskābe"

		// Paradigma: 3 - Lietvārds 2. deklinācija -is
	/*	GenNoun.any("-ņa, dsk. ģen. -ņu", new SimpleSubRule[]{
						SimpleSubRule.of(".*ņi", 3, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*nis", 3, new Tuple[]{TFeatures.GENDER__MASC})},
				null), //afroamerikāņi, šovmenis
			// konflikts ar "bizmanis"

		// Vissliktākie šabloni - satur tikai vienu galotni un neko citu.
		// Paradigmas: 9, 7 - vienskaitlī un daudzskaitlī
		GenNoun.any("-žu", new SimpleSubRule[]{
						SimpleSubRule.of(".*ži", new Integer[]{1, 3}, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[dz]e", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM}),
						SimpleSubRule.of(".*[dz]es", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.ENTRYWORD__PLURAL})},
				null), // gliemeži, abioģenēze, ablumozes, akolāde, nematodes
		GenNoun.any("-ņu", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM}),
						SimpleSubRule.of(".suņi", new Integer[]{5}, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*ņi", new Integer[]{1, 2, 3, 4}, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ņas", new Integer[]{7}, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.ENTRYWORD__PLURAL})},
				null), // celtņi, agrene, aizlaidnes
				*/
	};
	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final EndingRule[] directFirstConjVerb = {
		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		// Visu personu formas.
		FirstConj.direct("-dulbstu, -dulbsti,", "-dulbst, pag. -dulbu", "dulbt"), //sadulbt
		FirstConj.direct("-dullstu, -dullsti,", "-dullst, pag. -dullu", "dullt"), //apdullt
		FirstConj.direct("-drūmstu, -drūmsti,", "-drūmst, pag. -drūmu", "drūmt"), //sadrūmt
		//FirstConj.direct("-dulstu, -dulsti,", "-dulst, pag. -dullu", "dult"), //apdult // Baiba izņēma kā kļūdu
		FirstConj.direct("-kurlstu, -kurlsti,", "-kurlst, pag. -kurlu", "kurlt"), //apkurlt
		FirstConj.direct("-saustu, -sausti,", "-saust, pag. -sausu", "saust"), //apsaust
		FirstConj.direct("-slinkstu, -slinksti,", "-slinkst, pag. -slinku", "slinkt"), //apslinkt
		FirstConj.direct("-trakstu, -traksti,", "-trakst, pag. -traku", "trakt"), //aptrakt
		FirstConj.direct("-trulstu, -trulsti,", "-trulst, pag. -trulu", "trult"), //aptrult
		FirstConj.direct("-velbju, -velb,", "-velbj, pag. -velbu", "velbt"), //izvelbt
		FirstConj.direct("-vēstu, -vēsti,", "-vēst, pag. -vēsu", "vēst"), //atvēst
		FirstConj.direct("-žirbstu, -žirbsti,", "-žirbst, pag. -žirbu", "žirbt"), // atžirbt

		FirstConj.directAllPersParallel(
				"-nīku, -nīc, -nīk, retāk -nīkstu, -nīksti, -nīkst, pag. -niku", "nikt"), //apnikt
		FirstConj.directAllPersParallel(
				"-purstu, -pursti, -purst, pag. -puru, arī -pūru", "purt"), //izpurt

		// Tikai trešās personas formas.
		FirstConj.direct3Pers("-burbst, pag. -burba", "burbt"), //izburbt
		FirstConj.direct3Pers("-rep, pag. -repa", "rept"), //aprept

		// Pilnīgs nestandarts.
		VerbDoubleRule.of("parasti pag. -sārtu, -sārti, -sārta", null, "sārst", 15,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "\"sārst\""), TFeatures.POS__DIRECT_VERB},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PAST)},
				FirstConjStems.of("sārs", null, "sārt")), // piesārst
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final EndingRule[] directSecondConjVerb = {
		// Galotņu šabloni.
		SecondConj.direct("-āju, -ā,", "-ā, pag. -āju", "āt"), //aijāt, aizkābāt
		SecondConj.direct("-ēju, -ē,", "-ē, pag. -ēju", "ēt"), //abonēt, adsorbēt
			// -ēju, -ē, -ē, pag. -ēju
		SecondConj.direct("-ēju, -ē,", "-ē; pag. -ēju", "ēt"), //dulburēt
		SecondConj.direct("-īju, -ī,", "-ī, pag. -īju", "īt"), //apšķibīt, aizdzirkstīt
		SecondConj.direct("-oju, -o,", "-o, pag. -oju", "ot"), //aizalvot, aizbangot
		SecondConj.direct("-oju, -o,", "-o; pag. -oju", "ot"), //ielāgot

		SecondConj.directAllPers(
				"-ēju, -ē, -ē, -ējam, -ējat, pag. -ēju, -ējām, -ējāt; pav. -ē, -ējiet", "ēt"), //adverbializēt, anamorfēt
		SecondConj.directAllPers(
				"-oju, -o, -o, -ojam, -ojat, pag. -oju; -ojām, -ojāt; pav. -o, -ojiet", "ot"), //acot

		// Likumi ar modifikatoru parasti/tikai daudzskaitlī.
		SecondConj.directPlural("-ojam, -ojat, -o, pag. -ojām", "ot"), // sabizot
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final EndingRule[] directThirdConjVerb = {
		// Visām personām.
		ThirdConj.directStd("-u, -i,", "-a, pag. -īju", "īt"), //aizsūtīt
		ThirdConj.directStd("-u, -i,", "-a, pag. -āju", "āt"), //līcināt
		ThirdConj.directStd("-inu, -ini,", "-ina, pag. -ināju", "ināt"), //aizsvilināt

		// Darbības vārdu specifiskie likumi.
		ThirdConj.directStd("-bildu, -bildi,", "-bild, pag. -bildēju", "bildēt"), //atbildēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final EndingRule[] directMultiConjVerb = {
		// Galotņu šabloni.
		SecondThirdConj.directStdAllPersParallel(
				"-īju, -ī, -ī, arī -u, -i, -a, pag. -īju", "īt"), // aprobīt
		SecondThirdConj.directStdAllPersParallel(
				"-u, -i, -a, arī -īju, -ī, -ī, pag. -īju", "īt"), // atrotīt
		SecondThirdConj.directStdAllPersParallel(
				"-u, -i, -a, retāk -īju, -ī, -ī, pag. -īju", "īt"), // matīt

		// Darbības vārdu specifiskie likumi.
		// Nav.
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
	 */
	public static final EndingRule[] reflFirstConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Standartizētie.
		// A
		FirstConj.refl("-augos, -audzies,", "-augas, pag. -augos", "augties"), //paaugties
		// B
		FirstConj.refl("-bilstos, -bilsties,", "-bilstas, pag. -bildos", "bilsties"), //iebilsties
		FirstConj.refl("-bļaujos, -bļaujies,", "-bļaujas, pag. -bļāvos", "bļauties"), //iebļauties
		FirstConj.refl("-brēcos, -brēcies,", "-brēcas, pag. -brēcos", "brēkties"), //aizbrēkties
		// C
		FirstConj.refl("-cērpos, -cērpies,", "-cērpas, pag. -cirpos", "cirpties"), //apcirpties
		// D
		FirstConj.refl("-degos, -dedzies,", "-degas, pag. -degos", "degties"), //aizdegties
		FirstConj.refl("-dīcos, -dīcies,", "-dīcas, pag. -dīcos", "dīkties"), //iedīkties
		FirstConj.refl("-dūcos, -dūcies,", "-dūcas, pag. -dūcos", "dūkties"), //iedūkties
		FirstConj.refl("-dvešos, -dvesies,", "-dvešas, pag. -dvesos", "dvesties"), //nodvesties
		// E
		FirstConj.refl("-elšos, -elsies,", "-elšas, pag. -elsos", "elsties"), //aizelsties
		// F, G
		FirstConj.refl("-gārdzos, -gārdzies,", "-gārdzas, pag. -gārdzos", "gārgties"), //aizgārgties
		FirstConj.refl("-grābjos, -grābies,", "-grābjas, pag. -grābos", "grābties"), //iegrābties
		// Ģ,
		FirstConj.refl("-ģiedos, -ģiedies,", "-ģiedas, pag. -ģidos", "ģisties"), //apģisties
		// H, I, J, K
		FirstConj.refl("-karstos, -karsties,", "-karstas, pag. -karsos", "karsties"), //iekarsties
		FirstConj.refl("-kāršos, -kārsies,", "-kāršas, pag. -kārsos", "kārsties"), //izkārsties
		FirstConj.refl("-kliedzos, -kliedzies,", "-kliedzas, pag. -kliedzos", "kliegties"), //aizkliegties
		FirstConj.refl("-krācos, -krācies,", "-krācas, pag. -krācos", "krākties"), //aizkrākties
		FirstConj.refl("-kaucos, -kaucies,", "-kaucas, pag. -kaucos", "kaukties"), //iekaukties
		// Ķ
		FirstConj.refl("-ķērcos, -ķērcies,", "-ķērcas, pag. -ķērcos", "ķērkties"), //ieķērkties
		// L
		FirstConj.refl("-lokos, -locies,", "-lokas, pag. -lakos", "lakties"), //ielakties, pielakties
		FirstConj.refl("-līstos, -līsties,", "-līstas, pag. -lijos", "līties"), //izlīties
		// M
		FirstConj.refl("-mirstos, -mirsties,", "-mirstas, pag. -mirsos", "mirsties"), //aizmirsties
		FirstConj.refl("-mirstos, -mirsties,", "-mirstas, pag. -miros", "mirties"), //izmirties
		// N, Ņ
		FirstConj.refl("-ņirdzos, -ņirdzies,", "-ņirdzas, pag. -ņirdzos", "ņirgties"), //atņirgties
		// O, P, R
		FirstConj.refl("-reibstos, -reibsties,", "-reibstas, pag. -reibos", "reibties"), //iereibties
		FirstConj.refl("-rēcos, -rēcies,", "-rēcas, pag. -rēcos", "rēkties"), //ierēkties
		FirstConj.refl("-rūcos, -rūcies,", "-rūcas, pag. -rūcos", "rūkties"), //aizrūkties
		// S
		FirstConj.refl("-salstos, -salsties,", "-salstas, pag. -salos", "salties"), //izsalties
		FirstConj.refl("-sēcos, -sēcies,", "-sēcas, pag. -sēcos", "sēkties"), //iesēkties
		FirstConj.refl("-sliedzos, -sliedzies,", "-sliedzas, pag. -sliedzos", "sliegties"), //piesliegties
		FirstConj.refl("-snaužos, -snaudies,", "-snaužas, pag. -snaudos", "snausties"), //aizsnausties
		FirstConj.refl("-spiedzos, -spiedzies,", "-spiedzas, pag. -spiedzos", "spiegties"), //iespiegties
		FirstConj.refl("-spļaujos, -spļaujies,", "-spļaujas, pag. -spļāvos", "spļauties"), //nospļauties
		FirstConj.refl("-spurcos, -spurcies,", "-spurcas, pag. -spurcos", "spurkties"), //iespurkties
		FirstConj.refl("-strebjos, -strebies,", "-strebjas, pag. -strēbos", "strēbties"), //iestrēbties
		FirstConj.refl("-sūtos, -sūties,", "-sūtas, pag. -sutos", "susties"), //izsusties
		FirstConj.refl("-svelpjos, -svelpies,", "-svelpjas, pag. -svelpos", "svelpties"), //aizsvelpties
		FirstConj.refl("-svilpjos, -svilpies,", "-svilpjas, pag. -svilpos", "svilpties"), //aizsvilpties
		FirstConj.refl("-svilstos, -svilsties,", "-svilstas, pag. -svilos", "svilties"), //aizsvilties
		FirstConj.refl("-svīstos, -svīsties,", "-svīstas, pag. -svīdos", "svīsties"), //izsvīsties
		// Š
		FirstConj.refl("-šņaucos, -šņaucies,", "-šņaucas, pag. -šņaucos", "šņaukties"), //izšņaukties
		FirstConj.refl("-šņācos, -šņācies,", "-šņācas, pag. -šņācos", "šņākties"), //aizšņākties
		// T
		FirstConj.refl("-topos, -topies,", "-topas, pag. -tapos", "tapties"), //attapties
		FirstConj.refl("-tempjos, -tempies,", "-tempjas, pag. -tempos", "tempties"), //pietempties
		FirstConj.refl("-tvīkstos, -tvīksties,", "-tvīkstas, pag. -tvīkos", "tvīkties"), //iztvīkties
		// U, V
		FirstConj.refl("-vārgstos, -vārgsties,", "-vārgstas, pag. -vārgos", "vārgties"), //izvārgties
		FirstConj.refl("-vemjos, -vemies,", "-vemjas, pag. -vēmos", "vemties"), //apvemties
		FirstConj.refl("-vēžos, -vēzies,", "-vēžas, pag. -vēžos", "vēzties"), //atvēzties
		// Z
		FirstConj.refl("-zīstos, -zīsties,", "-zīstas, pag. -zinos", "zīties"), //atzīties
		FirstConj.refl("-zviedzos, -zviedzies,", "-zviedzas, pag. -zviedzos", "zviegties"), //aizzviegties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Nenoteiksmes homoformas
		FirstConj.refl3PersHomof("-sīcas, pag. -sīcās", "sīkties",
				"\"sīkties\" (kā odam)"), //aizsīkties
		// Paralēlformas.
		FirstConj.refl3PersParallel("-mejas, arī -mienas, pag. -mējās", "mieties"), // iemieties
		// TODO pārbaudīt, vai ir pieņemami, ka abas homoformas ir kopā
		FirstConj.refl3PersParallel("-spuldzas, arī -spulgstas, pag. -spuldzās, arī -spulgās", "spulgties"), // iespulgties


		// Standartizētie.
		// A, B
		FirstConj.refl3Pers("-blējas, pag. -blējās", "blēties"), //atblēties
		// C, D, E, F, G, H, I, J, K
		FirstConj.refl3Pers("-kviecas, pag. -kviecās", "kviekties"), //iekviekties
		// L, M, N, Ņ
		FirstConj.refl3Pers("-ņirbjas, pag. -ņirbās", "ņirbties"), //ieņirbties
		// O, P
		FirstConj.refl3Pers("-pūstas, pag. -puvās", "pūties"), //izpūties
		// R, S
		FirstConj.refl3Pers("-smeldzas, pag. -smeldzās", "smelgties"), //aizsmelgties
		// Š
		FirstConj.refl3Pers("-šalcas, pag. -šalcās", "šalkties"), //aizšalkties
		FirstConj.refl3Pers("-šņāpjas, pag. -šņāpās", "šņāpties"), //iešņāpties
		// T, U, V, Z
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 19: Darbības vārdi 2. konjugācija atgriezeniski
	 */
	public static final EndingRule[] reflSecondConjVerb = {
		// Galotņu šabloni.
		// Likumi, kam ir visu personu forma.
		SecondConj.refl("-ojos, -ojies,", "-ojas, pag. -ojos", "oties"), //aiztuntuļoties, apgrēkoties
		SecondConj.refl("-ējos, -ējies,", "-ējas, pag. -ējos", "ēties"), //abstrahēties
		SecondConj.refl("-ājos, -ājies,", "-ājas, pag. -ājos", "āties"), //aizdomāties
		SecondConj.refl("-ījos, -ījies,", "-ījas, pag. -ījos", "īties"), //atpestīties

		SecondConj.reflAllPers(
				"-ējos, -ējies, -ējas, -ējamies, -ējaties, pag. -ējos, -ējāmies, -ējāties; pav. -ējies, -ējieties",
				"ēties"), //adverbiēties

		// Parasti/tikai 3. personā.

		// Dīvainīši: dsk. + 3. pers. vsk.
		/*BaseRule.of(
				"parasti dsk., -ējamies, -ējaties, -ējas (3. pers. arī vsk.), pag. -ējāmies",
				".*ēties", 19,
				new Tuple[] {TFeatures.POS__VERB},
				new Tuple[]{TFeatures.USUALLY_USED__PLURAL, TFeatures.USUALLY_USED__THIRD_PERS,
							Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL_OR_THIRD_PERS)}), //konstituēties*/
		SecondConj.reflPlural(
				"-ējamies, -ējaties, -ējas, pag. -ējāmies, vai vsk. 3. pers. -ējas, pag. -ējās",
				"ēties"), // konstituēties
		SecondConj.reflPlural(
				"-ojamies, -ojaties, -ojas, pag. -ojāmies vai vsk. 3. pers., -ojas, pag. -ojās",
				"oties"), // noslāņoties
		SecondConj.reflPlural(
				"-ojamies, pag. -ojāmies vai vsk. 3. pers., -ojas, pag. -ojās",
				"oties"), // izretoties

		// Parasti/tikai daudzskaitlī.
		SecondConj.reflPlural("-ējamies, pag. -ējāmies", "ēties"), // drūzmēties
		SecondConj.reflPlural("-ējamies, -ējaties, -ējas, pag. -ējāmies", "ēties"), // sadrūzmēties
		SecondConj.reflPlural("-ojamies, pag. -ojāmies", "oties"), // apciemoties
		SecondConj.reflPlural("-ojamies, -ojaties, -ojas, pag. -ojāmies", "oties"), // sarindoties



		// Darbības vārdu specifiskie likumi.
		// Tikai 3. personas formas.
		SecondConj.refl3Pers("-spundējas, pag. -spundējās", "spundēties"), //aizspundēties
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
	 */
	public static final EndingRule[] reflThirdConjVerb = {
		// Galotņu šabloni.
		ThirdConj.reflStd("-os, -ies,", "-as, pag. -ājos", "āties"), //sadziedāties
		ThirdConj.reflStd("-os, -ies,", "-as, pag. -ējos", "ēties"), //apkaunēties, aizņaudēties
		ThirdConj.reflStd("-inos, -inies,", "-inās, pag. -inājos", "ināties"), //apklaušināties
		ThirdConj.reflStd("-os, -ies,", "-ās, pag. -ījos", "īties"), //apklausīties
		ThirdConj.reflStd("-os, -ies,", "-ās, pag. -ājos", "ināties"), //novājināties

		ThirdConj.reflStdPlural(
				"-ināmies, pag. -inājāmies vai vsk. 3. pers., -inās, pag. -inājās", "ināties"), // izretināties

		ThirdConj.reflStdPlural("-ējamies, -ējaties, -ējas, pag. -ējāmies", "ēties"), //saliedēties
		ThirdConj.reflStdPlural("-ināmies, pag. -inājāmies", "ināties"), //apdāvināties
		ThirdConj.reflStdPlural("-āmies, pag. -ījāmies", "īties"), //apšaudīties
		ThirdConj.reflStdPlural("-āmies, -āties, -ās, pag. -ījāmies", "īties"), //sagaidīties

		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		// Likumi, kam ir visu formu variants, ar mijām.
		ThirdConj.reflChange(
				"-guļos, -gulies,", "-guļas, pag. -gulējos", "gulēties"), //aizgulēties
		ThirdConj.reflChange(
				"-sēžos, -sēdies,", "-sēžas, pag. -sēdējos", "sēdēties"), //aizsēdēties

		// Likumi, kam ir visu formu variants, bez mijām.
		// A, B
		ThirdConj.reflStd(
				"-burkšos, -burkšies,", "-burkšas, pag. -burkšējos", "burkšēties"), // ieburkšēties
		ThirdConj.reflStd(
				"-burkšķos, -burkšķies,", "-burkšķas, pag. -burkšķējos", "burkšķēties"), //ieburkšķēties
		// C, Č
		ThirdConj.reflStd(
				"-čerkstos, -čerksties,", "-čerkstas, pag. -čerkstējos", "čerkstēties"), //iečerkstēties
		ThirdConj.reflStd(
				"-čērkstos, -čērksties,", "-čērkstas, pag. -čērkstējos", "čērkstēties"), //iečērkstēties
			//  -čērkstas, pag. -čerkstējās
		ThirdConj.reflStd(
				"-čiepstos, -čiepsties,", "-čiepstas, pag. -čiepstējos", "čiepstēties"), //iečiepstēties
		ThirdConj.reflStd(
				"-činkstos, -činksties,", "-činkstas, pag. -činkstējos", "činkstēties"), //iečinkstēties
		ThirdConj.reflStd(
				"-čīkstos, -čīksties,", "-čīkstas, pag. -čīkstējos", "čīkstēties"), //iečīkstēties
		ThirdConj.reflStd(
				"-čurnos, -čurnies,", "-čurnas, pag. -čurnējos", "čurnēties"), // sačurnēties
		// D
		ThirdConj.reflStd(
				"-dienos, -dienies,", "-dienas, pag. -dienējos", "dienēties"), //izdienēties
		ThirdConj.reflStd(
				"-dirnos, -dirnies,", "-dirnas, pag. -dirnējos", "dirnēties"), //izdirnēties
		ThirdConj.reflStd(
				"-draudos, -draudies,", "-draudas, pag. -draudējos", "draudēties"), //izdraudēties
		ThirdConj.reflStd(
				"-drebos, -drebies,", "-drebas, pag. -drebējos", "drebēties"), //iedrebēties
		ThirdConj.reflStd(
				"-drīkstos, -drīksties,", "-drīkstas, pag. -drīkstējos", "drīkstēties"), //iedrīkstēties
		ThirdConj.reflStd(
				"-dusos, -dusies,", "-dusas, pag. -dusējos", "dusēties"), //atdusēties
		ThirdConj.reflStd(
				"-dziedos, -dziedies,", "-dziedas, pag. -dziedājos", "dziedāties"), //aizdziedāties
		// E, F, G
		ThirdConj.reflStd(
				"-glūnos, -glūnies,", "-glūnas, pag. -glūnējos", "glūnēties"), //izglūnēties
		// H, I, J, K
		ThirdConj.reflStd(
				"-knukstos, -knuksties,", "-knukstas, pag. -knukstējos", "knukstēties"), //ieknukstēties
		ThirdConj.reflStd(
				"-krekstos, -kreksties,", "-krekstas, pag. -krekstējos", "krekstēties"), //atkrekstēties
		ThirdConj.reflStd(
				"-krekšos, -krekšies,", "-krekšas, pag. -krekšējos", "krekšēties"), //iekrekšēties
		ThirdConj.reflStd(
				"-krekšķos, -krekšķies,", "-krekšķas, pag. -krekšķējos", "krekšķēties"), //atkrekšķēties
		ThirdConj.reflStd(
				"-kunkstos, -kunksties,", "-kunkstas, pag. -kunkstējos", "kunkstēties"), //iekunkstēties
		ThirdConj.reflStd(
				"-kurnos, -kurnies,", "-kurnas, pag. -kurnējos", "kurnēties"), //iekurnēties
		// L, M
		ThirdConj.reflStd(
				"-līdzos, -līdzies,", "-līdzas, pag. -līdzējos", "līdzēties"), //izlīdzēties
		ThirdConj.reflStd(
				"-palīdzos, -palīdzies,", "-palīdzas, pag. -palīdzējos", "palīdzēties"), //izpalīdzēties
		ThirdConj.reflStd(
				"-murkšos, -murkšies,", "-murkšas, pag. -murkšējos", "murkšēties"), //iemurkšēties
		ThirdConj.reflStd(
				"-murkšķos, -murkšķies,", "-murkšķas, pag. -murkšķējos", "murkšķēties"), //iemurkšķēties
		// N, Ņ
		ThirdConj.reflStd(
				"-ņaudos, -ņaudies,", "-ņaudas, pag. -ņaudējos", "ņaudēties"), //izņaudēties
		ThirdConj.reflStd(
				"-ņerkstos, -ņerksties,", "-ņerkstas, pag. -ņerkstējos", "ņerkstēties"), //ieņerkstēties
		ThirdConj.reflStd(
				"-ņurdos, -ņurdies,", "-ņurdas, pag. -ņurdējos", "ņurdēties"), //ieņurdēties
		// O, P
		ThirdConj.reflStd(
				"-pīkstos, -pīksties,", "-pīkstas, pag. -pīkstējos", "pīkstēties"), //iepīkstēties
		ThirdConj.reflStd(
				"-pinkšos, -pinkšies,", "-pinkšas, pag. -pinkšējos", "pinkšēties"), //iepinkšēties
		ThirdConj.reflStd(
				"-pinkšķos, -pinkšķies,", "-pinkšķas, pag. -pinkšķējos", "pinkšķēties"), //iepinkšķēties
		ThirdConj.reflStd(
				"-pukstos, -puksties,", "-pukstas, pag. -pukstējos", "pukstēties"), //iepukstēties
		// R
		ThirdConj.reflStd(
				"-raudos, -raudies,", "-raudas, pag. -raudājos", "raudāties"), //aizraudāties
		ThirdConj.reflStd(
				"-rocos, -rocies,", "-rocās, pag. -rocījos", "rocīties"), // sarocīties
		// S
		ThirdConj.reflStd(
				"-smīnos, -smīnies,", "-smīnas, pag. -smīnējos", "smīnēties"), //iesmīnēties
		ThirdConj.reflStd(
				"-spurkšos, -spurkšies,", "-spurkšas, pag. -spurkšējos", "spurkšēties"), //iespurkšēties
		ThirdConj.reflStd(
				"-spurkšķos, -spurkšķies,", "-spurkšķas, pag. -spurkšķējos", "spurkšķēties"), //iespurkšķēties
		ThirdConj.reflStd(
				"-stāvos, -stāvies,", "-stāvas, pag. -stāvējos", "stāvēties"), //izstāvēties
		ThirdConj.reflStd(
				"-stenos, -stenies,", "-stenas, pag. -stenējos", "stenēties"), //iestenēties
		ThirdConj.reflStd(
				"-svinos, -svinies,", "-svinas, pag. -svinējos", "svinēties"), //aizsvinēties
		// Š
		ThirdConj.reflStd(
				"-šļupstos, -šļupsties,", "-šļupstas, pag. -šļupstējos", "šļupstēties"), //iešļupstēties
		ThirdConj.reflStd(
				"-šņukstos, -šņuksties,", "-šņukstas, pag. -šņukstējos", "šņukstēties"), //aizšņukstēties
		// T
		ThirdConj.reflStd(
				"-tarkšos, -tarkšies,", "-tarkšas, pag. -tarkšējos", "tarkšēties"), //ietarkšēties
		ThirdConj.reflStd(
				"-tarkšķos, -tarkšķies,", "-tarkšķas, pag. -tarkšķējos", "tarkšķēties"), //ietarkšķēties
		ThirdConj.reflStd(
				"-trīcos, -trīcies,", "-trīcas, pag. -trīcējos", "trīcēties"), //ietrīcēties
		ThirdConj.reflStd(
				"-trīsos, -trīsies,", "-trīsas, pag. -trīsējos", "trīsēties"), //ietrīsēties
		// U, V
		ThirdConj.reflStd(
				"-vaidos, -vaidies,", "-vaidas, pag. -vaidējos", "vaidēties"), //ievaidēties
		// Z

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Likumi, kam ir paralēlās formas.
		ThirdConj.reflStd3PersParallel(
				"-grandas, pag. -grandējās (retāk -grandās, 1. konj.)", "grandēties"), //iegrandēties
		ThirdConj.reflStd3PersParallel(
				"-spindzas, pag. -spindzējās (retāk -spindzās, 1. konj.)", "spindzēties"), //iespindzēties

		// Standartizētie.
		// A, B
		ThirdConj.reflStd3Pers("-blarkšas, pag. -blarkšējās", "blarkšēties"), //ieblarkšēties
		ThirdConj.reflStd3Pers("-blarkšķas, pag. -blarkšķējās", "blarkšķēties"), //ieblarkšķēties
		ThirdConj.reflStd3Pers("-blaukšas, pag. -blaukšējās", "blaukšēties"), //ieblaukšēties
		ThirdConj.reflStd3Pers("-blaukšķas, pag. -blaukšķējās", "blaukšķēties"), //ieblaukšķēties
		ThirdConj.reflStd3Pers("-brakšas, pag. -brakšējās", "brakšēties"), //iebrakšēties
		ThirdConj.reflStd3Pers("-brakšķas, pag. -brakšķējās", "brakšķēties"), //iebrakšķēties
		ThirdConj.reflStd3Pers("-brikšas, pag. -brikšējās", "brikšēties"), //aizbrikšēties
		ThirdConj.reflStd3Pers("-brikšķas, pag. -brikšķējās", "brikšķēties"), //aizbrikšķēties
		ThirdConj.reflStd3Pers("-brīkšas, pag. -brīkšējās", "brīkšēties"), //aizbrīkšēties
		ThirdConj.reflStd3Pers("-brīkšķas, pag. -brīkšķējās", "brīkšķēties"), //aizbrīkšķēties
		ThirdConj.reflStd3Pers("-būkšas, pag. -būkšējās", "būkšēties"), //iebūkšēties
		ThirdConj.reflStd3Pers("-būkšķas, pag. -būkšķējās", "būkšķēties"), //iebūkšķēties
		// C, Č
		ThirdConj.reflStd3Pers("-čabas, pag. -čabējās", "čabēties"), //aizčabēties
		ThirdConj.reflStd3Pers("-čakstas, pag. -čakstējās", "čakstēties"), //iečakstēties
		ThirdConj.reflStd3Pers("-čaukstas, pag. -čaukstējās", "čaukstēties"), //aizčaukstēties
		ThirdConj.reflStd3Pers("-čirkstas, pag. -čirkstējās", "čirkstēties"), //iečirkstēties
		ThirdConj.reflStd3Pers("-čurkstas, pag. -čurkstējās", "čurkstēties"), //iečurkstēties
		ThirdConj.reflStd3Pers("-čūkstas, pag. -čūkstējās", "čūkstēties"), //iečūkstēties
		// D
		ThirdConj.reflStd3Pers("-dārdas, pag. -dārdējās", "dārdēties"), //aizdārdēties
		ThirdConj.reflStd3Pers("-dimdas, pag. -dimdējās", "dimdēties"), //iedimdēties
		ThirdConj.reflStd3Pers("-dipas, pag. -dipējās", "dipēties"), //iedipēties
		ThirdConj.reflStd3Pers("-dunas, pag. -dunējās", "dunēties"), //iedunēties
		ThirdConj.reflStd3Pers("-dzinkstas, pag. -dzinkstējās", "dzinkstēties"), //iedzinkstēties
		ThirdConj.reflStd3Pers("-dzirkstas, pag. -dzirkstējās", "dzirkstēties"), //iedzirkstēties
		ThirdConj.reflStd3Pers("-džinkstas, pag. -džinkstējās", "džinkstēties"), //iedžinkstēties
		// E, F, G
		ThirdConj.reflStd3Pers("-grabas, pag. -grabējās", "grabēties"), //aizgrabēties
		ThirdConj.reflStd3Pers("-grandās, pag. -grandījās", "grandīties"), //iegrandīties
		ThirdConj.reflStd3Pers("-gurkstas, pag. -gurkstējās", "gurkstēties"), //aizgurkstēties
		ThirdConj.reflStd3Pers("-guldzas, pag. -guldzējās", "guldzēties"), //ieguldzēties
		// H, I, J, K
		ThirdConj.reflStd3Pers("-klabas, pag. -klabējās", "klabēties"), //aizklabēties
		ThirdConj.reflStd3Pers("-klaudzas, pag. -klaudzējās", "klaudzēties"), //aizklaudzēties
		ThirdConj.reflStd3Pers("-klakstas, pag. -klakstējās", "klakstēties"), //ieklakstēties
		ThirdConj.reflStd3Pers("-klakšas, pag. -klakšējās", "klakšēties"), //ieklakšēties
		ThirdConj.reflStd3Pers("-klakšķas, pag. -klakšķējās", "klakšķēties"), //ieklakšķēties
		ThirdConj.reflStd3Pers("-klinkšas, pag. -klinkšējās", "klinkšēties"), //ieklinkšēties
		ThirdConj.reflStd3Pers("-klinkšķas, pag. -klinkšķējās", "klinkšķēties"), //ieklinkšķēties
		ThirdConj.reflStd3Pers("-klukstas, pag. -klukstējās", "klukstēties"), //aizklukstēties
		ThirdConj.reflStd3Pers("-klunkstas, pag. -klunkstējās", "klunkstēties"), //ieklunkstēties
		ThirdConj.reflStd3Pers("-klunkšas, pag. -klunkšējās", "klunkšēties"), //aizklunkšēties
		ThirdConj.reflStd3Pers("-klunkšķas, pag. -klunkšķējās", "klunkšķēties"), //aizklunkšķēties
		ThirdConj.reflStd3Pers("-knakstās, pag. -knakstējās", "knakstēties"), //aizknakstēties
		ThirdConj.reflStd3Pers("-knakšas, pag. -knakšējās", "knakšēties"), //aizknakšēties
		ThirdConj.reflStd3Pers("-knakšķas, pag. -knakšķējās", "knakšķēties"), //aizknakšķēties
		ThirdConj.reflStd3Pers("-knaukstas, pag. -knaukstējās", "knaukstēties"), //ieknaukstēties
		ThirdConj.reflStd3Pers("-knaukšas, pag. -knaukšējās", "knaukšēties"), //aizknaukšēties
		ThirdConj.reflStd3Pers("-knaukšķas, pag. -knaukšķējās", "knaukšķēties"), //aizknaukšķēties
		ThirdConj.reflStd3Pers("-knikstas, pag. -knikstējās", "knikstēties"), //ieknikstēties
		ThirdConj.reflStd3Pers("-knikšas, pag. -knikšējās", "knikšēties"), //aizknikšēties
		ThirdConj.reflStd3Pers("-knikšķas, pag. -knikšķējās", "knikšķēties"), //aizknikšķēties
		ThirdConj.reflStd3Pers("-kņudas, pag. -kņudējās", "kņudēties"), //iekņudēties
		ThirdConj.reflStd3Pers("-krakstas, pag. -krakstējās", "krakstēties"), //aizkrakstēties
		ThirdConj.reflStd3Pers("-krakšas, pag. -krakšējās", "krakšēties"), //nokrakšēties
		ThirdConj.reflStd3Pers("-krakšķas, pag. -krakšķējās", "krakšķēties"), //aizkrakšķēties
		ThirdConj.reflStd3Pers("-kraukstas, pag. -kraukstējās", "kraukstēties"), //iekraukstēties
		ThirdConj.reflStd3Pers("-kraukšas, pag. -kraukšējās", "kraukšēties"), //iekraukšēties
		ThirdConj.reflStd3Pers("-kraukšķas, pag. -kraukšķējās", "kraukšķēties"), //iekraukšķēties
		ThirdConj.reflStd3Pers("-krikstas, pag. -krikstējās", "krikstēties"), //iekrikstēties
		ThirdConj.reflStd3Pers("-krikšas, pag. -krikšējās", "krikšēties"), //iekrikšēties
		ThirdConj.reflStd3Pers("-krikšķas, pag. -krikšķējās", "krikšķēties"), //iekrikšķēties
		ThirdConj.reflStd3Pers("-kurkstas, pag. -kurkstējās", "kurkstēties"), //aizkurkstēties
		ThirdConj.reflStd3Pers("-kurkšķas, pag. -kurkšķējās", "kurkšķēties"), //aizkurkšķēties
		ThirdConj.reflStd3Pers("-kutas, pag. -kutējās", "kutēties"), //iekutēties
		// Ķ
		ThirdConj.reflStd3Pers("-ķaukstas, pag. -ķaukstējās", "ķaukstēties"), //ieķaukstēties
		ThirdConj.reflStd3Pers("-ķērkstas, pag. -ķērkstējās", "ķērkstēties"), //ieķērkstēties
		// L, M
		ThirdConj.reflStd3Pers("-mirdzas, pag. -mirdzējās", "mirdzēties"), //aizmirdzēties
		ThirdConj.reflStd3Pers("-mudas, pag. -mudējās", "mudēties"), //samudēties
		// N
		ThirdConj.reflStd3Pers("-niezas, pag. -niezējās", "niezēties"), //ieniezēties
		// Ņ
		ThirdConj.reflStd3Pers("-ņirbas, pag. -ņirbējās", "ņirbēties"), //ieņirbēties
		ThirdConj.reflStd3Pers("-ņirkstas, pag. -ņirkstējās", "ņirkstēties"), //ieņirkstēties
		ThirdConj.reflStd3Pers("-ņirkšas, pag. -ņirkšējās", "ņirkšēties"), //ieņirkšēties
		ThirdConj.reflStd3Pers("-ņirkšķas, pag. -ņirkšķējās", "ņirkšķēties"), //ieņirkšķēties
		ThirdConj.reflStd3Pers("-ņurkstas, pag. -ņurkstējās", "ņurkstēties"), //ieņurkstēties
		ThirdConj.reflStd3Pers("-ņurkšas, pag. -ņurkšējās", "ņurkšēties"), //ieņurkšēties
		ThirdConj.reflStd3Pers("-ņurkšķas, pag. -ņurkšķējās", "ņurkšķēties"), //ieņurkšķēties
		// O, P
		ThirdConj.reflStd3Pers("-pakšas, pag. -pakšējās", "pakšēties"), //iepakšēties
		ThirdConj.reflStd3Pers("-pakšķas, pag. -pakšķējās", "pakšķēties"), //iepakšķēties
		ThirdConj.reflStd3Pers("-parkšas, pag. -parkšējās", "parkšēties"), //ieparkšēties
		ThirdConj.reflStd3Pers("-parkšķas, pag. -parkšķējās", "parkšķēties"), //ieparkšķēties
		ThirdConj.reflStd3Pers("-pēkstas, pag. -pēkstējās", "pēkstēties"), //iepēkstēties
		ThirdConj.reflStd3Pers("-pēkšas, pag. -pēkšējās", "pēkšēties"), //iepēkšēties
		ThirdConj.reflStd3Pers("-pēkšķas, pag. -pēkšķējās", "pēkšķēties"), //iepēkšķēties
		ThirdConj.reflStd3Pers("-plarkšas, pag. -plarkšējās", "plarkšēties"), //ieplarkšēties
		ThirdConj.reflStd3Pers("-plarkšķas, pag. -plarkšķējās", "plarkšķēties"), //ieplarkšķēties
		ThirdConj.reflStd3Pers("-plerkšas, pag. -plerkšējās", "plerkšēties"), //ieplerkšēties
		ThirdConj.reflStd3Pers("-plerkšķas, pag. -plerkšķējās", "plerkšķēties"), //ieplerkšķēties
		ThirdConj.reflStd3Pers("-plīkšas, pag. -plīkšējās", "plīkšēties"), //ieplīkšēties
		ThirdConj.reflStd3Pers("-plīkšķas, pag. -plīkšķējās", "plīkšķēties"), //ieplīkšķēties
		ThirdConj.reflStd3Pers("-plinkšas, pag. -plinkšējās", "plinkšēties"), //ieplinkšēties
		ThirdConj.reflStd3Pers("-plinkšķas, pag. -plinkšķējās", "plinkšķēties"), //ieplinkšķēties
		ThirdConj.reflStd3Pers("-pliukšas, pag. -pliukšējās", "pliukšēties"), //iepliukšēties
		ThirdConj.reflStd3Pers("-pliukšķas, pag. -pliukšķējās", "pliukšķēties"), //iepliukšķēties
		ThirdConj.reflStd3Pers("-plunkšas, pag. -plunkšējās", "plunkšēties"), //ieplunkšēties
		ThirdConj.reflStd3Pers("-plunkšķas, pag. -plunkšķējās", "plunkšķēties"), //ieplunkšķēties
		ThirdConj.reflStd3Pers("-pļerkšas, pag. -pļerkšējās", "pļerkšēties"), //iepļerkšēties
		ThirdConj.reflStd3Pers("-pļerkšķas, pag. -pļerkšķējās", "pļerkšķēties"), //iepļerkšķēties
		ThirdConj.reflStd3Pers("-pukšas, pag. -pukšējās", "pukšēties"), //iepukšēties
		ThirdConj.reflStd3Pers("-pukšķas, pag. -pukšķējās", "pukšķēties"), //iepukšķēties
		// R
		ThirdConj.reflStd3Pers("-rības, pag. -rībējās", "rībēties"), //ierībēties
		ThirdConj.reflStd3Pers("-rukstas, pag. -rukstējās", "rukstēties"), //ierukstēties
		ThirdConj.reflStd3Pers("-rukšas, pag. -rukšējās", "rukšēties"), //aizrukšēties
		ThirdConj.reflStd3Pers("-rukšķas, pag. -rukšķējās", "rukšķēties"), //aizrukšķēties
		// S
		ThirdConj.reflStd3Pers("-sanas, pag. -sanējās", "sanēties"), //aizsanēties
		ThirdConj.reflStd3Pers("-sāpas, pag. -sāpējās", "sāpēties"), //aizsāpeties
		ThirdConj.reflStd3Pers("-skanas, pag. -skanējās", "skanēties"), //aizskanēties
		ThirdConj.reflStd3Pers("-skrabas, pag. -skrabējās", "skrabēties"), //ieskrabēties
		ThirdConj.reflStd3Pers("-skrapstas, pag. -skrapstējās", "skrapstēties"), //ieskrapstēties
		ThirdConj.reflStd3Pers("-skrapšas, pag. -skrapšējās", "skrapšēties"), //ieskrapšēties
		ThirdConj.reflStd3Pers("-skrapšķas, pag. -skrapšķējās", "skrapšķēties"), //ieskrapšķēties
		ThirdConj.reflStd3Pers("-smilkstas, pag. -smilkstējās", "smilkstēties"), //iesmilkstēties
		ThirdConj.reflStd3Pers("-smirdas, pag. -smirdējās", "smirdēties"), //iesmirdēties
		ThirdConj.reflStd3Pers("-sparkšas, pag. -sparkšējās", "sparkšēties"), //iesparkšēties
		ThirdConj.reflStd3Pers("-sparkšķas, pag. -sparkšķējās", "sparkšķēties"), //iesparkšķēties
		ThirdConj.reflStd3Pers("-spīdas, pag. -spīdējās", "spīdēties"), //iespīdēties
		ThirdConj.reflStd3Pers("-sprakstas, pag. -sprakstējās", "sprakstēties"), //iesprakstēties
		ThirdConj.reflStd3Pers("-sprakšas, pag. -sprakšējās", "sprakšēties"), //iesprakšēties
		ThirdConj.reflStd3Pers("-sprakšķas, pag. -sprakšķējās", "sprakšķēties"), //iesprakšķēties
		ThirdConj.reflStd3Pers("-spridzas, pag. -spridzējās", "spridzēties"), //iespridzēties
		ThirdConj.reflStd3Pers("-sprikstas, pag. -sprikstējās", "sprikstēties"), //iesprikstēties
		ThirdConj.reflStd3Pers("-strinkšas, pag. -strinkšējās", "strinkšēties"), //aizstrinkšēties
		ThirdConj.reflStd3Pers("-strinkšķas, pag. -strinkšķējās", "strinkšķēties"), //aizstrinkšķēties
		ThirdConj.reflStd3Pers("-sūkstas, pag. -sūkstējās", "sūkstēties"), //iesūkstēties
		ThirdConj.reflStd3Pers("-sūrstas, pag. -sūrstējās", "sūrstēties"), //iesūrstēties
		// Š
		ThirdConj.reflStd3Pers("-šķindas, pag. -šķindējās", "šķindēties"), //aizšķindēties
		ThirdConj.reflStd3Pers("-šļakstas, pag. -šļakstējās", "šļakstēties"), //iešļakstēties
		ThirdConj.reflStd3Pers("-šmīkstas, pag. -šmīkstējās", "šmīkstēties"), //iešmīkstēties
		ThirdConj.reflStd3Pers("-šņakstas, pag. -šņakstējās", "šņakstēties"), //iešņakstēties
		ThirdConj.reflStd3Pers("-šņirkstas, pag. -šņirkstējās", "šņirkstēties"), //iešņirkstēties
		ThirdConj.reflStd3Pers("-švirkstas, pag. -švirkstējās", "švirkstēties"), //aizšvirkstēties
		ThirdConj.reflStd3Pers("-švīkstas, pag. -švīkstējās", "švīkstēties"), //iešvīkstēties
		// T
		ThirdConj.reflStd3Pers("-tikšas, pag. -tikšējās", "tikšēties"), //ietikšēties
		ThirdConj.reflStd3Pers("-tikšķas, pag. -tikšķējās", "tikšķēties"), //ietikšķēties
		ThirdConj.reflStd3Pers("-tinkšas, pag. -tinkšējās", "tinkšēties"), //aiztinkšēties
		ThirdConj.reflStd3Pers("-tinkšķas, pag. -tinkšķējās", "tinkšķēties"), //aiztinkšķēties
		ThirdConj.reflStd3Pers("-tirkšas, pag. -tirkšējās", "tirkšēties"), //ietirkšēties
		ThirdConj.reflStd3Pers("-tirkšķas, pag. -tirkšķējās", "tirkšķēties"), //ietirkšķēties
		ThirdConj.reflStd3Pers("-trinkšas, pag. -trinkšējās", "trinkšēties"), //aiztrinkšēties
		ThirdConj.reflStd3Pers("-trinkšķas, pag. -trinkšķējās", "trinkšķēties"), //aiztrinkšķēties
		// U
		ThirdConj.reflStd3Pers("-urkšas, pag. -urkšējās", "urkšēties"), //ieurkšēties
		ThirdConj.reflStd3Pers("-urkšķas, pag. -urkšķējās", "urkšķēties"), //ieurkšķēties
		// V
		ThirdConj.reflStd3Pers("-vankšas, pag. -vankšējās", "vankšēties"), //ievankšēties
		ThirdConj.reflStd3Pers("-vankšķas, pag. -vankšķējās", "vankšķēties"), //ievankšķēties
		ThirdConj.reflStd3Pers("-vaukšas, pag. -vaukšējās", "vaukšēties"), //ievaukšēties
		ThirdConj.reflStd3Pers("-vaukšķas, pag. -vaukšķējās", "vaukšķēties"), //ievaukšķēties
		ThirdConj.reflStd3Pers("-verkšas, pag. -verkšējās", "verkšēties"), //saverkšēties
		ThirdConj.reflStd3Pers("-verkšķas, pag. -verkšķējās", "verkšķēties"), //saverkšķēties
		ThirdConj.reflStd3Pers("-vēkšas, pag. -vēkšējās", "vēkšēties"), //ievēkšēties
		ThirdConj.reflStd3Pers("-vēkšķas, pag. -vēkšķējās", "vēkšķēties"), //ievēkšķēties
		ThirdConj.reflStd3Pers("-vizas, pag. -vizējās", "vizēties"), //ievizēties
		// Z
		ThirdConj.reflStd3Pers("-zibas, pag. -zibējās", "zibēties"), //iezibēties
		ThirdConj.reflStd3Pers("-ziedas, pag. -ziedējās", "ziedēties"), //izziedēties
		ThirdConj.reflStd3Pers("-zuzas, pag. -zuzējās", "zuzēties"), //iezuzēties
		// Ž
		ThirdConj.reflStd3Pers("-žvadzas, pag. -žvadzējās", "žvadzēties"), //iežvadzēties
		ThirdConj.reflStd3Pers("-žvarkstas, pag. -žvarkstējās", "žvarkstēties"), //iežvarkstēties
		ThirdConj.reflStd3Pers("-žvikstas, pag. -žvikstējās", "žvikstēties"), //iežvikstēties
		ThirdConj.reflStd3Pers("-žvīkstas, pag. -žvīkstējās", "žvīkstēties"), //iežvīkstēties

		// Likumi daudzskaitļa formām.
		ThirdConj.reflStdPlural("-dziedamies, pag. -dziedājāmies", "dziedāties"), //apdziedāties
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final EndingRule[] reflMultiConjVerb = {
		// Galotņu šabloni.
		// Visām personām (3. personas likumi netiek atvasināti).
		SecondThirdConj.reflStdAllPersParallel(
				"-os, -ies, -as, arī -ējos, -ējies, -ējas, pag. -ējos", "ēties"), // mīlēties
		SecondThirdConj.reflStdAllPersParallel(
				"-ījos, -ījies, -ījas, arī -os, -ies, -ās, pag. -ījos", "īties"), // blēdīties

		// 3. personai.
		SecondThirdConj.reflStd3PersParallel(
				"-ās, arī -ījas, pag. -ījās", "īties"), // rotīties
		SecondThirdConj.reflStd3PersParallel(
				"-ījas, arī -ās, pag. -ījās", "īties"), // pietašķīties
		SecondThirdConj.reflStd3PersParallel(
				"-ās, retāk -ījas, pag. -ījās", "īties"), // atrotīties

		// Darbības vārdu specifiskie likumi.
		// Nav.
	};
}
