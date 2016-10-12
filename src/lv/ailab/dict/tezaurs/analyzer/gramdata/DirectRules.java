package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.SecondConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.SecondThirdConj;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.ThirdConj;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.*;
import lv.ailab.dict.utils.Tuple;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * TGram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar Rule.applyDirect().
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * @author Lauma
 */
public class DirectRules
{
	/**
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final Rule[] other = {
		BaseRule.of("esmu, esi, ir, 3. pers. nolieguma forma nav, dsk. esam, esat, ir, 3. pers. nolieguma forma nav, pag. biju, biji, bija (arī bij), dsk. bijām, bijāt, bija (arī bij), vajadzības izteiksme jābūt",
				"būt", 29,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "būt"), TFeatures.POS__IRREG_VERB, TFeatures.POS__DIRECT_VERB}, null), //būt
		RegularVerbRule.of("neesmu, neesi,", "nav, pag. nebiju", "nebūt", 29,
				new Tuple[]{Tuple.of(TKeys.INFLECT_AS, "nebūt"), TFeatures.POS__IRREG_VERB, TFeatures.POS__REFL_VERB}, null), //nebūt

		// 1. paradigma: 1. dekl. lietvārdi, -s
		BaseRule.noun("-a, dsk. ģen. -ku, v.", ".*ks", 1, null, new Tuple[] {TFeatures.GENDER__MASC}), // cepurnieks
		// 6. paradigma: 3. deklinācijas lietvārdi
		BaseRule.noun("-us, v.", ".*us", 6, null, new Tuple[] {TFeatures.GENDER__MASC}), // dienvidus

		// 7. paradigma: 4. dekl. lietvārdi, sieviešu dzimte
		BaseRule.noun("-jas, dsk. ģen. -ju, s.", ".*ja", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // kastrētāja
		BaseRule.noun("-as, dsk. ģen. -ju, s.", ".*ja", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // vaivadija
		BaseRule.noun("-as, dsk. ģen. -stu, s.", ".*sta", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // pasta
		BaseRule.noun("-as, dsk. ģen. -tu, s.", ".*ta", 7, null, new Tuple[]{TFeatures.GENDER__FEM}), // placenta
		BaseRule.noun("-as, dsk. ģen. -vu, s.", ".*va", 7, null, new Tuple[] {TFeatures.GENDER__FEM}), // apskava

		// 34. paradigma: Atgriezeniskie lietvārdi -šanās
		BaseRule.noun(
				"ģen. -ās, akuz. -os, instr. -os, dsk. -ās, ģen. -os, akuz. -ās, s.", ".*šanās", 34,
				new Tuple[]{TFeatures.POS__REFL_NOUN}, new Tuple[]{TFeatures.GENDER__FEM}), //aizbildināšanās
		BaseRule.noun("ģen. -ās, akuz. -os, instr. -os, s.", ".*šanās", 34,
				new Tuple[]{TFeatures.POS__REFL_NOUN}, new Tuple[]{TFeatures.GENDER__FEM}), //augšāmcelšanās
		// Paradigmas: 7, 8 - kopdzimtes lietvārdi, galotne -a
		BaseRule.noun("ģen. -as, v. dat. -am, s. dat. -ai, kopdz.", ".*a", new Integer[]{7, 8}, null,
				new Tuple[]{Tuple.of(TKeys.GENDER, TValues.COGENDER)}), // aitasgalva, aizmārša

		// Paradigmas: 1, 2 - 1. deklinācija
		// Šeit varētu vēlāk vajadzēt likumus paplašināt, ja parādās jauni šķirkļi.
		BaseRule.of("lietv. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
				new Tuple[]{TFeatures.GENDER__MASC, TFeatures.POS__NOUN}), // aerobs
		BaseRule.noun("vsk. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
				new Tuple[]{TFeatures.GENDER__MASC, Tuple.of(TKeys.NUMBER, TValues.SINGULAR)}), // acteks
		BaseRule.noun("-a, vsk.", new SimpleSubRule[]{
						SimpleSubRule.of(".*(akmen|asmen|mēnes|ziben|ūden|ruden)s", new Integer[]{4}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*suns", new Integer[]{5}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*is", new Integer[]{3}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*š", new Integer[]{2}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{1}, new Tuple[]{TFeatures.GENDER__MASC})},
				null), // aizkars, cīsiņš, sakņkājis

		// Paradigmas: 7, 11
		BaseRule.noun("-as, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*a", new Integer[]{7}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]as", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), // aberācija, milns, najādas

		// Paradigmas: 9, 11
		BaseRule.noun("dsk. ģen. -ņu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, null),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ns", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), // ādmine, bākuguns, bārkšsaknes
		BaseRule.noun("dsk. ģen. -šu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[ts]e", new Integer[]{9}, null),
						SimpleSubRule.of(".*[ts]es", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ts", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), //alžīriete, kroņprincese, autiņbiksītes, īsbikses, azots
		BaseRule.noun("dsk. ģen. -ļu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*le", new Integer[]{9}, null),
						SimpleSubRule.of(".*les", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ls", new Integer[]{11}, null)},
				new Tuple[]{TFeatures.GENDER__FEM}), //apakšcentrāle, dziesmuspēles, ezerpils



			// Paradigma: 11 - 6. dekl.
		BaseRule.sixthDeclNoChange("-ts, dsk. ģen. -tu", ".*ts"), // koloniālvalsts
		BaseRule.noun("-ts, -šu", ".*ts", 11, new Tuple[]{TFeatures.GENDER__FEM}, null), //abonentpults
		BaseRule.noun("-vs, -vju", ".*vs", 11, new Tuple[]{TFeatures.GENDER__FEM}, null), //adatzivs

		BaseRule.noun("-žu, v.", ".*ļaudis", 11,
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.USED_ONLY__PLURAL},
				new Tuple[]{TFeatures.GENDER__MASC}), //ļaudis

		// Paradigmas: 13, 14 - īpašības vārdi - skaidri pateikts
		BaseRule.adjective("īp. v. -ais; s. -a, -ā"), // aerobs
		BaseRule.of("s. -as; adj.", ".*i", new Integer[]{13, 14},
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM},
				new Tuple[]{TFeatures.POS__ADJ}), // abēji 2

		// Nedefinēta paradigma: divdabji
		BaseRule.participleIs("-gušais; s. -gusi, -gusī", ".*dzis"), // aizdudzis
		BaseRule.participleIs("-ušais; s. -usi, -usī", ".*[cdjlmprstv]is"), // aizkūpis

		BaseRule.of("s. -usī", ".*ušais", 0,
				new Tuple[]{TFeatures.POS__PARTICIPLE_IS, TFeatures.DEFINITE_ENDING},
				null), // aizpagājušais
		BaseRule.of("s. -usies", ".*ies", 0,
				new Tuple[]{TFeatures.POS__PARTICIPLE_IS},
				null), // izdevies

		// Paradigmas: 13, 14 - īpašības vārdi vai divdabji
		BaseRule.adjectiveParticiple("-ais; s. -a, -ā"), // abējāds, acains, agāms
		BaseRule.adjectiveParticiple("-ais, s. -a, -ā"), // abējāds, acains, agāms
		BaseRule.adjectiveParticiple("-ais, -a, -ā"), // pamīšs, supervienkāršs
		BaseRule.of("s. -as; tikai dsk.", new SimpleSubRule[]{
						//SimpleSubRule.of(".*oši", new Integer[]{13, 0}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*ti", new Integer[]{13, 14, 0}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						//SimpleSubRule.of(".*dami", new Integer[]{13, 14, 0}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_DAMS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						//SimpleSubRule.of(".*[aā]mi", new Integer[]{13, 14, 0}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						//SimpleSubRule.of(".*uši", new Integer[]{13, 0}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_IS, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*īgi", new Integer[]{13}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ēji", new Integer[]{13}, new Tuple[]{TFeatures.POS__ADJ, TFeatures.ENTRYWORD__PLURAL})
				},
				new Tuple[]{TFeatures.USED_ONLY__PLURAL}), // abēji 1, aizkomentētajiem nebija instanču

		// Paradigma: 30 - jaundzimušais, pēdējais
		BaseRule.of("-ā, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*tais", new Integer[] {30, 0},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*ušais", new Integer[] {30, 0},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_IS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*[aā]mais", new Integer[] {30, 0},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS}),
						SimpleSubRule.of(".*[^tšm]ais", new Integer[] {30},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.CONTAMINATION__NOUN})},
				new Tuple[]{TFeatures.GENDER__MASC}),
			//pirmdzimtais, ieslodzītais, cietušais, brīvprātīgais, mīļākais,

		// Paradigmas: 30 -  jaundzimušais, pēdējais
		// 34 paradigma: Atgriezeniskie lietvārdi -šanās
		BaseRule.of("-ās, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*šanās", new Integer[]{34},
								new Tuple[]{TFeatures.POS__REFL_NOUN}),
						SimpleSubRule.of(".*tā", new Integer[]{40, 0},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_TS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
						SimpleSubRule.of(".*ošā", new Integer[]{40, 0},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_OSS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
						SimpleSubRule.of(".*[aā]mā", new Integer[]{40, 0},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.POS__PARTICIPLE_AMS, TFeatures.CONTAMINATION__NOUN, TFeatures.UNCLEAR_PARADIGM, TFeatures.UNCLEAR_POS, TFeatures.DEFINITE_ENDING}),
						SimpleSubRule.of(".*[^tšm]ā", new Integer[]{40},
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.CONTAMINATION__NOUN, TFeatures.DEFINITE_ENDING})},
				new Tuple[]{TFeatures.GENDER__FEM}), // pirmdzimtā, notiesātā -šanās
	};

	/**
	 * Vietniekvārdu likumi. Krietna tiesa ir speciāli izveidoti, papildus
	 * pieliekot norādes par būšanu vietniekvārdam, lai nesajūk ar citām
	 * vārdšķirām.
	 * Tiek ļoti cerēts, ka te ir visi.
	 */
	public static final Rule[] pronomen = {
			// TODO pakāpeniski izravēt dubultlikumus
		BaseRule.of("vietn., -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC}), // kurš, kurš, mans, viņš
		BaseRule.of("vietn., -u, v.", ".*i", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL}), // abi
		BaseRule.of("-a, v.; vietn.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.GENDER__MASC}), // vienotrs
		BaseRule.of("-a, v.; nenoteiktais vietn.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.GENDER__MASC}), // jebkas, jebkurš, jebkāds
		BaseRule.of("nenoteiktais vietn., -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__INDEF_PRONOUN, TFeatures.GENDER__MASC}), // dažs, cits
		BaseRule.of("-a, v.; norād. vietn.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__MASC}), // šāds, šitāds
		BaseRule.of("norād. vietn., -a, v.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__MASC}), // tāds
		BaseRule.of("-a, v.; pieder. vietn.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__POSS_PRONOUN, TFeatures.GENDER__MASC}), // tavs, savs
		BaseRule.of("noliedz. vietn., -a, v.", ".*s", 25,
				null, new Tuple[]{TFeatures.POS__NEG_PRONOUN, TFeatures.GENDER__MASC}), // nekāds
		BaseRule.of("vispārin. vietn., -a, v.", ".*[sš]", 25,
				null, new Tuple[]{TFeatures.POS__GEN_PRONOUN, TFeatures.GENDER__MASC}), // ikkurš, ikkatrs

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
				null, new Tuple[]{TFeatures.POS__DEM_PRONOUN, TFeatures.GENDER__FEM}), // šāda
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
		BaseRule.of("ģen. tās, dat. tai, akuz. to, instr. ar to, lok. tai (arī tanī, tajā), dsk. nom. tās, ģen. to, akuz. tās, instr. ar tām, lok. tajās (arī tais, tanīs), s.; norād.",
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

		BaseRule.of("ģen. šitā, dat. šitam, akuz. šito, instr. ar šito, lok. šitai (arī šitanī, šitajā), dsk. nom. šitie, ģen. šito, dat. šitiem, akuz. šitos, instr. ar šitiem, lok. šitais (arī šitajos, šitos, šitanīs), v., norād. vietn.",
				"šitas", 25,
				null, new Tuple[]{TFeatures.PARALLEL_FORMS, TFeatures.POS__DEF_PRONOUN, TFeatures.GENDER__MASC}), // šitas
		BaseRule.of("ģen. šitā, dat. šitajam, akuz. šito, instr. ar šito, lok. šitai (arī šitanī, šitajā), dsk. nom. šitie, ģen. šito, dat. šitajiem, akuz. šitos, instr. ar šitajiem, lok. šitais (arī šitajos, šitanīs), v., norād. vietn.",
				"šitais", 25,
				null, new Tuple[]{TFeatures.PARALLEL_FORMS, TFeatures.POS__DEF_PRONOUN, TFeatures.GENDER__MASC}), // šitais
		BaseRule.of("ģen. šitās, dat. šitai, akuz. šito, instr. ar šito, lok. šitai (arī šitanī, šitajā), dsk. nom. šitās, ģen. šito, dat. šitām, akuz. šitās, instr. ar šitām, lok. šitais (arī šitajās, šitanīs), s., norād. vietn.",
				"šitā", 25,
				null, new Tuple[]{TFeatures.PARALLEL_FORMS, TFeatures.POS__DEF_PRONOUN, TFeatures.GENDER__FEM}), // šitā
	};

	/**
	 * Skaitļa vārdu likumi. Krietna tiesa ir speciāli izveidoti, papildus
	 * pieliekot vārdšķiru, lai nesajūk ar citām vārdšķirām.
	 * Tiek ļoti cerēts, ka te ir visi.
	 */
	public static final Rule[] numeral = {
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
		//BaseRule.of("-as, s.; pamata skait.", "viena", 0,
		//		null, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.POS__CARD_NUMERAL, TFeatures.ENTRYWORD__FEM}), // viena (iespējams, ka šito var vispār var izmest?)
		BaseRule.of("nulles, dsk. ģen. nuļļu, s.", "nulle", 9,
				new Tuple[]{TFeatures.POS__NOUN, TFeatures.CONTAMINATION__CARD_NUM},
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
	 * Paradigm 9: Lietvārds 5. deklinācija -e
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] fifthDeclNoun = {
		BaseRule.fifthDeclStd("-ķe, -es, dsk. ģen. -ķu, s.", ".*ķe"), //ciniķe

		// Standartizētie
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ču, s.", ".*[cč]e"), //ābece, veče
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ģu, s.", ".*[ģ]e"), //aeroloģe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ju, s.", ".*je"), //baskāje, aloje
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ķu, s.", ".*ķe"), //agnostiķe, leduspuķe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ļu, s.", ".*le"), //ābele
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ņu, s.", ".*[n]e"), //ābolaine
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ru, s.", ".*re"), //administratore, ādere
		BaseRule.fifthDeclStd("dsk. ģen. -ru, s.", ".*re"), // aizsargcepure
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šu, s.", ".*[sšt]e"), //abate, adrese, larkše, apokalipse, note
		BaseRule.fifthDeclStd("-es, dsk. ģen. -žu, s.", ".*[dz]e"), //ābolmaize, aģitbrigāde, bilde, pirolīze

		BaseRule.fifthDeclStd("-es, dsk. ģen. -bju, s.", ".*be"), //apdobe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -džu, s.", ".*dze"), //kāršaudze
		BaseRule.fifthDeclStd("-es, dsk. ģen. -kšu, s.", ".*kte"), //daudzpunkte
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ļļu, s.", ".*lle"), //zaļumballe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ļņu, s.", ".*lne"), //nokalne
		BaseRule.fifthDeclStd("-es, dsk. ģen. -mju, s.", ".*me"), //agronome, krustamzīme
		BaseRule.fifthDeclStd("-es, dsk. ģen. -pju, s.", ".*pe"), //bērzlapju
		BaseRule.fifthDeclStd("-es, dsk. ģen. -smju, s.", ".*sme"), //noslieksme
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šņu, s.", ".*sne"), //izloksne, aizkrāsne
		BaseRule.fifthDeclStd("dsk. ģen. -šņu, s.", ".*sne"), //apaļkoksne
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šķu, s.", ".*šķe"), //draišķe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šļu, s.", ".*sle"), //gaisagrābsle
		BaseRule.fifthDeclStd("-es, dsk. ģen. -vju, s.", ".*ve"), //agave, aizstāve
		BaseRule.fifthDeclStd("-es, dsk. ģen. -žņu, s.", ".*zne"), //asteszvaigzne

		BaseRule.fifthDeclStd("-es, -žu s.", ".*ze"), // apoteoze

		BaseRule.fifthDeclStd("-es, -mju s.", ".*me"), // apakšzeme

		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -du, s.", ".*de"), // diplomande
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -fu, s.", ".*fe"), //arheogrāfe
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. mufu, s.", ".*mufe"), //mufe
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -pu, s.", ".*pe"), // filantrope
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -su, s.", ".*se"), // bise
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -stu, s.", ".*ste"), //abolicioniste
		BaseRule.fifthDeclNoChange("dsk. ģen. -tu, s.", ".*te"), //artiste
		BaseRule.fifthDeclNoChange("dsk. ģen. -stu, s.", ".*ste"), //animiste
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -tu, s.", ".*te"), // antisemīte
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -zu, s.", ".*ze"), // autobāze


			// Vienskaitlis + daudzskaitlis
		BaseRule.noun("-es, dsk. ģen. -pju, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*pe", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM}),
						SimpleSubRule.of(".*pes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.GENDER__FEM})},
				null), // aitkope, antilope, tūsklapes

		// Nejauki, pārāk specifiski gdījumi
		// Šiem defišu izņemšanas mehānisms īsti neder, jo vienā vietā vajag atstāt.
		BaseRule.fifthDeclStd("-es, dsk. ģen. āru, s.", ".*āre"), //āre
		//BaseRule.fifthDeclStd("-es, dsk. ģen. biržu, s.", ".*birze"), //birze
		BaseRule.fifthDeclStd("-es, dsk. ģen. pūšu, s.", ".*pūte"), //pūte 1, 3
		BaseRule.fifthDeclStd("-es, dsk. ģen. puvju, s.", ".*puve"), //puve
		BaseRule.fifthDeclStd("-es, dsk. ģen. šaļļu, s.", ".*šalle"), //šalle
		//BaseRule.fifthDeclStd("-es, dsk. ģen. -upju, s.", ".*upe"), //dzirnavupe

		// Miju varianti
		BaseRule.fifthDeclOptChange("-es, dsk. ģen. -fu, arī -fju, s.", ".*fe"), //arheogrāfe
		BaseRule.fifthDeclOptChange("-es, dsk. ģen. -šu vai -tu, s.", ".*te"), //cunfte, manšete
		BaseRule.fifthDeclOptChange("-es, dsk. ģen. -šu, arī -tu, s.", ".*te"), //torte
		BaseRule.fifthDeclOptChange("-es, dsk. ģen. -stu, arī -šu, s.", ".*ste"), //dzeņaukste
		BaseRule.fifthDeclOptChange("-es, dsk. ģen. -šu, arī -stu, s.", ".*ste"), //plekste

		BaseRule.fifthDeclOptChange("-es, dsk. ģen. lešu vai letu", ".*lete"), //lete

		// Nestandartīgie
		BaseRule.noun("-ļu, s.", ".*les", 9,
			new Tuple[]{TFeatures.ENTRYWORD__PLURAL}, new Tuple[]{TFeatures.GENDER__FEM}), //bailes


	};

	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final Rule[] secondDeclNoun = {
		BaseRule.secondDeclStd("-bja, dsk. ģen. -bju, v.", ".*bis"), //ledusurbis
		BaseRule.secondDeclStd("-ča, dsk. ģen. -ču, v.", ".*cis"), //labrocis
		BaseRule.secondDeclStd("-ļa, dsk. ģen. -ļu, v.", ".*lis"), //brokolis
		BaseRule.secondDeclStd("-ņa, dsk. ģen. -ņu, v.", ".*nis"), //bizmanis
		BaseRule.secondDeclStd("-pja, dsk. ģen. -pju, v.", ".*pis"), //grāmatskapis
		BaseRule.secondDeclStd("-ša, dsk. ģen. -šu, v.", ".*[st]is"), //auseklītis, lāčplēsis
		BaseRule.secondDeclStd("-vja, dsk. ģen. -vju, v.", ".*vis"), //kapātuvis
		BaseRule.secondDeclStd("-ža, dsk. ģen. -žu, v.", ".*[dz]is"), //diskvedis, plakandzelzis

		BaseRule.noun("-ns, dsk. ģen. -ņu, v.", ".*ns", 4, null, new Tuple[] {TFeatures.GENDER__MASC}), // bruģakmens

		BaseRule.secondDeclStd("-bja, v.", ".*bis"), //aizsargdambis
		BaseRule.secondDeclStd("-dža, v.", ".*dzis"), //algādzis
		BaseRule.secondDeclStd("-ķa, v.", ".*[kķ]is"), //agnostiķis
		BaseRule.secondDeclStd("-pja, v.", ".*pis"), //aitkopis
		BaseRule.secondDeclStd("-vja, v.", ".*vis"), //aizstāvis
		BaseRule.secondDeclStd("-žņa, v.", ".*znis"), //aitkopis
		BaseRule.secondDeclStd("-ža, v.", ".*[dzž]is"), //ādgrauzis
	};

	/**
	 * Īsie šabloni ar vienu galotni un dzimti.
	 * Visiem likumiem ir iespējami vairāki galotņu varianti, bet uzskaitīti
	 * ir tikai vārdnīcā sastaptie.
	 */
	public static final Rule[] nounMultiDecl = {
		// Vienskaitlis, vīriešu dzimte
		// Ar mijām
		BaseRule.noun("-ļa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*lis", new Integer[]{3}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // acumirklis, (bacils, durkls=kļūda)
		BaseRule.noun("-ņa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ņš", new Integer[]{2}, null),
						SimpleSubRule.of(".*nis", new Integer[]{3}, null),
						SimpleSubRule.of(".*suns", new Integer[]{5}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // abesīnis, dižtauriņš, dzinējsuns
		BaseRule.noun("-ša, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[sšt]is", new Integer[]{3}, null),
						SimpleSubRule.of(".*ss", new Integer[]{5}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // abrkasis, lemess
		// Bez mijām
		BaseRule.noun("-ra, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*rs", new Integer[]{1}, null),
						SimpleSubRule.of(".*ris", new Integer[]{3}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // airis, mūrniekmeistars
		BaseRule.noun("-sa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ss", new Integer[]{1}, null),
						SimpleSubRule.of(".*sis", new Integer[]{3}, new Tuple[]{TFeatures.NO_SOUNDCHANGE})},
				new Tuple[]{TFeatures.GENDER__MASC}), // balanss, kūrviesis
		BaseRule.noun("-ta, v.", ".*tis", new Integer[]{3},
				new Tuple[]{TFeatures.NO_SOUNDCHANGE},
				new Tuple[]{TFeatures.GENDER__MASC}), // stereotālskatis
		// Vispārīgā galotne, kas der visam un neder nekam
		BaseRule.noun("-a, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{1}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]š", new Integer[]{2}, null),
						SimpleSubRule.of(".*[ģjķr]is", new Integer[]{3}, null)},
				new Tuple[]{TFeatures.GENDER__MASC}), // abats, akustiķis, sparguļi, skostiņi,

		// Daudzkaitlis, vīriešu dzimte
		// Ar mijām
		BaseRule.noun("-ņu, v.", ".*ņi", new Integer[]{1, 2, 3, 4, 5},
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM},
				new Tuple[]{TFeatures.GENDER__MASC}), // bretoņi
		// Vispārīgā galotne, kas der visam un neder nekam
		BaseRule.noun("-u, v.", new SimpleSubRule[]{
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
		// Daudzkaitlis, sieviešu dzimte
		// Ar mijām
		BaseRule.noun("-ņu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, null),
						SimpleSubRule.of(".*ņas", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nis", new Integer[]{11}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
				},
				new Tuple[]{TFeatures.GENDER__FEM}), // acenes, iemaņas, balodene, robežugunis
		BaseRule.noun("-šu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*te", new Integer[]{9}, null),
						SimpleSubRule.of(".*šas", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[st]es", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*tis", new Integer[]{11}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),},
				new Tuple[]{TFeatures.GENDER__FEM}), // ahajiete, aizkulises, autosacīkstes, klaušas, šķūtis
		BaseRule.noun("-žu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*žas", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[dz]es", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),},
				new Tuple[]{TFeatures.GENDER__FEM}), // mirādes, graizes, bažas
		// Vispārīgā galotne, kas der visam un neder nekam
		BaseRule.noun("-u, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*a", new Integer[]{7}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]as", new Integer[]{7}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ķes", new Integer[]{9}, new Tuple[]{TFeatures.ENTRYWORD__PLURAL}),},
				new Tuple[]{TFeatures.GENDER__FEM}), // aijas, spēķes, zeķes, konkrēcija


};

	/**
	 * Likumi, kas ir citu likumu prefiksi.
	 * Šajā masīvā jāievēro likumu secība, citādi slikti būs. Šo masīvu jālieto
	 * pašu pēdējo.
	 */
	public static final Rule[] dangerous = {
		// Paradigma: 9 - Lietvārds 5. deklinācija -e siev. dz.
		BaseRule.fifthDeclStd("-es, s.", ".*e"), //aizture + daudzi piemēri ar mijām
			// konflikts ar "astilbe" un "acetilsalicilskābe"

		// Paradigma: 3 - Lietvārds 2. deklinācija -is
		BaseRule.noun("-ņa, dsk. ģen. -ņu", ".*ņi", 3,
				new Tuple[]{TFeatures.ENTRYWORD__PLURAL}, new Tuple[]{TFeatures.GENDER__MASC}), //afroamerikāņi
			// konflikts ar "bizmanis"

		// Vissliktākie šabloni - satur tikai vienu galotni un neko citu.
		// Paradigmas: 9, 7 - vienskaitlī un daudzskaitlī
		BaseRule.noun("-žu", new SimpleSubRule[]{
						SimpleSubRule.of(".*ži", new Integer[]{1, 3}, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[dz]e", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM}),
						SimpleSubRule.of(".*[dz]es", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.ENTRYWORD__PLURAL})},
				null), // gliemeži, abioģenēze, ablumozes, akolāde, nematodes
		BaseRule.noun("-ņu", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM}),
						SimpleSubRule.of(".suņi", new Integer[]{5}, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*ņi", new Integer[]{1, 2, 3, 4}, new Tuple[]{TFeatures.GENDER__MASC, TFeatures.ENTRYWORD__PLURAL, TFeatures.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ņas", new Integer[]{7}, new Tuple[]{TFeatures.GENDER__FEM, TFeatures.ENTRYWORD__PLURAL})},
				null), // celtņi, agrene, aizlaidnes
		// Paradigma: 2, 3
		BaseRule.noun("-ņa", new SimpleSubRule[]{
						SimpleSubRule.of(".*akmens", new Integer[]{4}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*ņš", new Integer[]{2}, new Tuple[]{TFeatures.GENDER__MASC}),
						SimpleSubRule.of(".*nis", new Integer[]{3}, new Tuple[]{TFeatures.GENDER__MASC})},
				null), // abolainis, jūrakmens, ļautiņš
	};
	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final Rule[] directFirstConjVerb = {
		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		// Visu personu formas.
		FirstConjRule.direct("-dulbstu, -dulbsti,", "-dulbst, pag. -dulbu", "dulbt"), //sadulbt
		FirstConjRule.direct("-dullstu, -dullsti,", "-dullst, pag. -dullu", "dullt"), //apdullt
		FirstConjRule.direct("-drūmstu, -drūmsti,", "-drūmst, pag. -drūmu", "drūmt"), //sadrūmt
		FirstConjRule.direct("-dulstu, -dulsti,", "-dulst, pag. -dullu", "dult"), //apdult
		FirstConjRule.direct("-kurlstu, -kurlsti,", "-kurlst, pag. -kurlu", "kurlt"), //apkurlt
		FirstConjRule.direct("-saustu, -sausti,", "-saust, pag. -sausu", "saust"), //apsaust
		FirstConjRule.direct("-slinkstu, -slinksti,", "-slinkst, pag. -slinku", "slinkt"), //apslinkt
		FirstConjRule.direct("-šņurkstu, -šņurksti,", "-šņurkst, pag. -šņurku", "šņurkt"), //apšņurkt
		FirstConjRule.direct("-trakstu, -traksti,", "-trakst, pag. -traku", "trakt"), //aptrakt
		FirstConjRule.direct("-trulstu, -trulsti,", "-trulst, pag. -trulu", "trult"), //aptrult
		FirstConjRule.direct("-velbju, -velb,", "-velbj, pag. -velbu", "velbt"), //izvelbt
		FirstConjRule.direct("-vēstu, -vēsti,", "-vēst, pag. -vēsu", "vēst"), //atvēst
		FirstConjRule.direct("-žirbstu, -žirbsti,", "-žirbst, pag. -žirbu", "žirbt"), // atžirbt

		FirstConjRule.directAllPersParallel(
				"-nīku, -nīc, -nīk, retāk -nīkstu, -nīksti, -nīkst, pag. -niku", "nikt"), //apnikt
		FirstConjRule.directAllPersParallel(
				"-purstu, -pursti, -purst, pag. -puru, arī -pūru", "purt"), //izpurt

		// Tikai trešās personas formas.
		FirstConjRule.direct3Pers("-burbst, pag. -burba", "burbt"), //izburbt
		FirstConjRule.direct3Pers("-rep, pag. -repa", "rept"), //aprept

	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Galotņu šabloni.
		SecondConj.direct("-āju, -ā,", "-ā, pag. -āju", "āt"), //aijāt, aizkābāt
		SecondConj.direct("-ēju, -ē,", "-ē, pag. -ēju", "ēt"), //abonēt, adsorbēt
		SecondConj.direct("-ēju, -ē,", "-ē; pag. -ēju", "ēt"), //dulburēt
		SecondConj.direct("-īju, -ī,", "-ī, pag. -īju", "īt"), //apšķibīt, aizdzirkstīt
		SecondConj.direct("-oju, -o,", "-o, pag. -oju", "ot"), //aizalvot, aizbangot
		SecondConj.direct("-oju, -o,", "-o; pag. -oju", "ot"), //ielāgot

		BaseRule.secondConjDirAllPers(
				"-ēju, -ē, -ē, -ējam, -ējat, pag. -ēju, -ējām, -ējāt; pav. -ē, -ējiet", "ēt"), //adverbializēt, anamorfēt
		BaseRule.secondConjDirAllPers(
				"-oju, -o, -o, -ojam, -ojat, pag. -oju; -ojām, -ojāt; pav. -o, -ojiet", "ot"), //acot

		// Darbības vārdu specifiskie likumi.
		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		// Īpašā piezīme par glumēšanu: 2. konjugāciju nosaka 3. personas
		// galotne "-ē" - 3. konjugācijai būtu bez.
		SecondConj.direct3PersParallel(
				"-ē, pag. -ēja (retāk -gluma, 1. konj.)", "glumēt"), //aizglumēt

		// Likumi ar modifikatoru parasti/tikai daudzskaitlī.
		PluralVerbRule.secondConjDir("-ojam, -ojat, -o, pag. -ojām", "ot"), // sabizot
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {
		// Visām personām.
		ThirdConj.direct("-u, -i,", "-a, pag. -īju", "īt", false), //aizsūtīt
		ThirdConj.direct("-u, -i,", "-a; pag. -īju", "īt", false), //apdurstīt
		ThirdConj.direct("-u, -i,", "-a, pag. -āju", "āt", false), //līcināt
		ThirdConj.direct("-inu, -ini,", "-ina, pag. -ināju", "ināt", false), //aizsvilināt
		// Tikai trešajai personai.
		ThirdConj.direct3Pers("-ina, pag. -ināja", "ināt", false), //aizducināt

		// Darbības vārdu specifiskie likumi.
		ThirdConj.direct("-bildu, -bildi,", "-bild, pag. -bildēju", "bildēt", false), //atbildēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] directMultiConjVerb = {
		// Galotņu šabloni.
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-īju, -ī, -ī, arī -u, -i, -a, pag. -īju", ".*īt", false), // aprobīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-u, -i, -a, arī -īju, -ī, -ī, pag. -īju", ".*īt", false), // atrotīt
		BaseRule.secondThirdConjDirectAllPersParallel(
				"-u, -i, -a, retāk -īju, -ī, -ī, pag. -īju", ".*īt", false), // matīt

		// Darbības vārdu specifiskie likumi.
		// Nav.
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
	 */
	public static final Rule[] reflFirstConjVerb = {
		// Likumi, kam ir visu formu variants.
		// Netoteiksmes homoformas.


			// Standartizētie.
		// A
		FirstConjRule.refl("-augos, -audzies,", "-augas, pag. -augos", "augties"), //paaugties
		// B
		FirstConjRule.refl("-bilstos, -bilsties,", "-bilstas, pag. -bildos", "bilsties"), //iebilsties
		FirstConjRule.refl("-bļaujos, -bļaujies,", "-bļaujas, pag. -bļāvos", "bļauties"), //iebļauties
		FirstConjRule.refl("-brēcos, -brēcies,", "-brēcas, pag. -brēcos", "brēkties"), //aizbrēkties
		// C
		FirstConjRule.refl("-cērpos, -cērpies,", "-cērpas, pag. -cirpos", "cirpties"), //apcirpties
		// D
		FirstConjRule.refl("-degos, -dedzies,", "-degas, pag. -degos", "degties"), //aizdegties
		FirstConjRule.refl("-dīcos, -dīcies,", "-dīcas, pag. -dīcos", "dīkties"), //iedīkties
		FirstConjRule.refl("-dūcos, -dūcies,", "-dūcas, pag. -dūcos", "dūkties"), //iedūkties
		FirstConjRule.refl("-dvešos, -dvesies,", "-dvešas, pag. -dvesos", "dvesties"), //nodvesties
		// E
		FirstConjRule.refl("-elšos, -elsies,", "-elšas, pag. -elsos", "elsties"), //aizelsties
		// F, G
		FirstConjRule.refl("-gārdzos, -gārdzies,", "-gārdzas, pag. -gārdzos", "gārgties"), //aizgārgties
		FirstConjRule.refl("-grābjos, -grābies,", "-grābjas, pag. -grābos", "grābties"), //iegrābties
		// Ģ,
		FirstConjRule.refl("-ģiedos, -ģiedies,", "-ģiedas, pag. -ģidos", "ģisties"), //apģisties
		// H, I, J, K
		FirstConjRule.refl("-karstos, -karsties,", "-karstas, pag. -karsos", "karsties"), //iekarsties
		FirstConjRule.refl("-kāršos, -kārsies,", "-kāršas, pag. -kārsos", "kārsties"), //izkārsties
		FirstConjRule.refl("-kliedzos, -kliedzies,", "-kliedzas, pag. -kliedzos", "kliegties"), //aizkliegties
		FirstConjRule.refl("-krācos, -krācies,", "-krācas, pag. -krācos", "krākties"), //aizkrākties
		FirstConjRule.refl("-kaucos, -kaucies,", "-kaucas, pag. -kaucos", "kaukties"), //iekaukties
		// Ķ
		FirstConjRule.refl("-ķērcos, -ķērcies,", "-ķērcas, pag. -ķērcos", "ķērkties"), //ieķērkties
		// L
		FirstConjRule.refl("-lokos, -locies,", "-lokas, pag. -lakos", "lakties"), //ielakties, pielakties
		FirstConjRule.refl("-līstos, -līsties,", "-līstas, pag. -lijos", "līties"), //izlīties
		// M
		FirstConjRule.refl("-mirstos, -mirsties,", "-mirstas, pag. -mirsos", "mirsties"), //aizmirsties
		FirstConjRule.refl("-mirstos, -mirsties,", "-mirstas, pag. -miros", "mirties"), //izmirties
		// N, Ņ
		FirstConjRule.refl("-ņirdzos, -ņirdzies,", "-ņirdzas, pag. -ņirdzos", "ņirgties"), //atņirgties
		// O, P, R
		FirstConjRule.refl("-reibstos, -reibsties,", "-reibstas, pag. -reibos", "reibties"), //iereibties
		FirstConjRule.refl("-rēcos, -rēcies,", "-rēcas, pag. -rēcos", "rēkties"), //ierēkties
		FirstConjRule.refl("-rūcos, -rūcies,", "-rūcas, pag. -rūcos", "rūkties"), //aizrūkties
		// S
		FirstConjRule.refl("-salstos, -salsties,", "-salstas, pag. -salos", "salties"), //izsalties
		FirstConjRule.refl("-sēcos, -sēcies,", "-sēcas, pag. -sēcos", "sēkties"), //iesēkties
		FirstConjRule.refl("-sliedzos, -sliedzies,", "-sliedzas, pag. -sliedzos", "sliegties"), //piesliegties
		FirstConjRule.refl("-snaužos, -snaudies,", "-snaužas, pag. -snaudos", "snausties"), //aizsnausties
		FirstConjRule.refl("-spiedzos, -spiedzies,", "-spiedzas, pag. -spiedzos", "spiegties"), //iespiegties
		FirstConjRule.refl("-spļaujos, -spļaujies,", "-spļaujas, pag. -spļāvos", "spļauties"), //nospļauties
		FirstConjRule.refl("-spurcos, -spurcies,", "-spurcas, pag. -spurcos", "spurkties"), //iespurkties
		FirstConjRule.refl("-strebjos, -strebies,", "-strebjas, pag. -strēbos", "strēbties"), //iestrēbties
		FirstConjRule.refl("-sūtos, -sūties,", "-sūtas, pag. -sutos", "susties"), //izsusties
		FirstConjRule.refl("-svelpjos, -svelpies,", "-svelpjas, pag. -svelpos", "svelpties"), //aizsvelpties
		FirstConjRule.refl("-svilpjos, -svilpies,", "-svilpjas, pag. -svilpos", "svilpties"), //aizsvilpties
		FirstConjRule.refl("-svilstos, -svilsties,", "-svilstas, pag. -svilos", "svilties"), //aizsvilties
		FirstConjRule.refl("-svīstos, -svīsties,", "-svīstas, pag. -svīdos", "svīsties"), //izsvīsties
		// Š
		FirstConjRule.refl("-šņaucos, -šņaucies,", "-šņaucas, pag. -šņaucos", "šņaukties"), //izšņaukties
		FirstConjRule.refl("-šņācos, -šņācies,", "-šņācas, pag. -šņācos", "šņākties"), //aizšņākties
		// T
		FirstConjRule.refl("-topos, -topies,", "-topas, pag. -tapos", "tapties"), //attapties
		FirstConjRule.refl("-tempjos, -tempies,", "-tempjas, pag. -tempos", "tempties"), //pietempties
		FirstConjRule.refl("-tvīkstos, -tvīksties,", "-tvīkstas, pag. -tvīkos", "tvīkties"), //iztvīkties
		// U, V
		FirstConjRule.refl("-vārgstos, -vārgsties,", "-vārgstas, pag. -vārgos", "vārgties"), //izvārgties
		FirstConjRule.refl("-vemjos, -vemies,", "-vemjas, pag. -vēmos", "vemties"), //apvemties
		FirstConjRule.refl("-vēžos, -vēzies,", "-vēžas, pag. -vēžos", "vēzties"), //atvēzties
		// Z
		FirstConjRule.refl("-zīstos, -zīsties,", "-zīstas, pag. -zinos", "zīties"), //atzīties
		FirstConjRule.refl("-zviedzos, -zviedzies,", "-zviedzas, pag. -zviedzos", "zviegties"), //aizzviegties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlformas.
		FirstConjRule.refl3PersParallel("-mejas, arī -mienas, pag. -mējās", "mieties"), // iemieties
		// TODO pārbaudīt, vai ir pieņemami, ka abas homoformas ir kopā
		FirstConjRule.refl3PersParallel("-spuldzas, arī -spulgstas, pag. -spuldzās, arī -spulgās", "spulgties"), // iespulgties

		// Standartizētie.
		// A, B
		FirstConjRule.refl3Pers("-blējas, pag. -blējās", "blēties"), //atblēties
		// C, D
		FirstConjRule.refl3Pers("-dūcas, pag. -dūcās", "dūkties"), //aizdūkties
		// E, F, G, H, I, J, K
		FirstConjRule.refl3Pers("-kaucas, pag. -kaucās", "kaukties"), //aizkaukties
		FirstConjRule.refl3Pers("-kviecas, pag. -kviecās", "kviekties"), //iekviekties
		// L, M, N, Ņ
		FirstConjRule.refl3Pers("-ņirbjas, pag. -ņirbās", "ņirbties"), //ieņirbties
		// O, P, R, S
		FirstConjRule.refl3Pers("-sīcas, pag. -sīcās", "sīkties"), //aizsīkties
		FirstConjRule.refl3Pers("-smeldzas, pag. -smeldzās", "smelgties"), //aizsmelgties
		// Š
		FirstConjRule.refl3Pers("-šalcas, pag. -šalcās", "šalkties"), //aizšalkties
		FirstConjRule.refl3Pers("-šņāpjas, pag. -šņāpās", "šņāpties"), //iešņāpties
		// T, U, V, Z
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 19: Darbības vārdi 2. konjugācija atgriezeniski
	 */
	public static final Rule[] reflSecondConjVerb = {
		// Galotņu šabloni.
		// Likumi, kam ir visu personu forma.
		SecondConj.refl("-ojos, -ojies,", "-ojas, pag. -ojos", "oties"), //aiztuntuļoties, apgrēkoties
		SecondConj.refl("-ējos, -ējies,", "-ējas, pag. -ējos", "ēties"), //abstrahēties
		SecondConj.refl("-ājos, -ājies,", "-ājas, pag. -ājos", "āties"), //aizdomāties
		SecondConj.refl("-ījos, -ījies,", "-ījas, pag. -ījos", "īties"), //atpestīties

		BaseRule.secondConjReflAllPers(
				"-ējos, -ējies, -ējas, -ējamies, -ējaties, pag. -ējos, -ējāmies, -ējāties; pav. -ējies, -ējieties",
				"ēties"), //adverbiēties

		// Parasti/tikai daudzskaitlī.
		PluralVerbRule.secondConjRefl("-ējamies, pag. -ējāmies", "ēties"), // drūzmēties
		PluralVerbRule.secondConjRefl("-ējamies, -ējaties, -ējas, pag. -ējāmies", "ēties"), // sadrūzmēties
		PluralVerbRule.secondConjRefl("-ojamies, pag. -ojāmies", "oties"), // apciemoties
		PluralVerbRule.secondConjRefl("-ojamies, -ojaties, -ojas, pag. -ojāmies", "oties"), // sarindoties

		// Dīvainīši: dsk. + 3. pers. vsk.
		BaseRule.of(
				"parasti dsk., -ējamies, -ējaties, -ējas (3. pers. arī vsk.), pag. -ējāmies",
				".*ēties", 19,
				new Tuple[] {TFeatures.POS__VERB},
				new Tuple[]{TFeatures.USUALLY_USED__PLURAL, TFeatures.USUALLY_USED__THIRD_PERS,
							Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL_OR_THIRD_PERS)}), //konstituēties
		BaseRule.of(
				"parasti dsk., -ojamies, -ojaties, -ojas, pag. -ojāmies vai vsk. 3. pers., -ojas, pag. -ojās",
				".*oties", 19,
				new Tuple[] {TFeatures.POS__VERB},
				new Tuple[]{TFeatures.USUALLY_USED__PLURAL, TFeatures.USUALLY_USED__THIRD_PERS,
						Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL_OR_THIRD_PERS)}), //noslāņoties

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
	public static final Rule[] reflThirdConjVerb = {
		// Galotņu šabloni.
		ThirdConj.refl(
				"-os, -ies,", "-as, pag. -ājos", "āties", false), //sadziedāties
		ThirdConj.refl(
				"-os, -ies,", "-as, pag. -ējos", "ēties", false), //apkaunēties, aizņaudēties
		ThirdConj.refl(
				"-inos, -inies,", "-inās, pag. -inājos", "ināties", false), //apklaušināties
		ThirdConj.refl(
				"-os, -ies,", "-ās, pag. -ījos", "īties", false), //apklausīties
		ThirdConj.refl(
				"-os, -ies,", "-ās, pag. -ājos", "ināties", false), //novājināties

		ThirdConj.refl3Pers("-as, pag. -ējās", "ēties", false), //aizčiepstēties
		ThirdConj.refl3Pers("-inās, pag. -inājās", "ināties", false), //aizbubināties
		ThirdConj.refl3Pers("-ās, pag. -ījās", "īties", false), //aizbīdīties

		PluralVerbRule.thirdConjRefl("-ējamies, -ējaties, -ējas, pag. -ējāmies", "ēties", false), //saliedēties
		PluralVerbRule.thirdConjRefl("-ināmies, pag. -inājāmies", "ināties", false), //apdāvināties
		PluralVerbRule.thirdConjRefl("-āmies, pag. -ījāmies", "īties", false), //apšaudīties
		PluralVerbRule.thirdConjRefl("-āmies, -āties, -ās, pag. -ījāmies", "īties", false), //sagaidīties

		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		// Likumi, kam ir visu formu variants.
		// A, B
		ThirdConj.refl(
				"-burkšķos, -burkšķies,", "-burkšķas, pag. -burkšķējos", "burkšķēties", false), //ieburkšķēties
		// C, Č
		ThirdConj.refl(
				"-čerkstos, -čerksties,", "-čerkstas, pag. -čerkstējos", "čerkstēties", false), //iečerkstēties
		ThirdConj.refl(
				"-čērkstos, -čērksties,", "-čērkstas, pag. -čērkstējos", "čērkstēties", false), //iečērkstēties
		ThirdConj.refl(
				"-čiepstos, -čiepsties,", "-čiepstas, pag. -čiepstējos", "čiepstēties", false), //iečiepstēties
		ThirdConj.refl(
				"-činkstos, -činksties,", "-činkstas, pag. -činkstējos", "činkstēties", false), //iečinkstēties
		ThirdConj.refl(
				"-čīkstos, -čīksties,", "-čīkstas, pag. -čīkstējos", "čīkstēties", false), //iečīkstēties
		ThirdConj.refl(
				"-čurnos, -čurnies,", "-čurnas, pag. -čurnējos", "čurnēties", false), // sačurnēties
		// D
		ThirdConj.refl(
				"-dienos, -dienies,", "-dienas, pag. -dienējos", "dienēties", false), //izdienēties
		ThirdConj.refl(
				"-dirnos, -dirnies,", "-dirnas, pag. -dirnējos", "dirnēties", false), //izdirnēties
		ThirdConj.refl(
				"-draudos, -draudies,", "-draudas, pag. -draudējos", "draudēties", false), //izdraudēties
		ThirdConj.refl(
				"-drebos, -drebies,", "-drebas, pag. -drebējos", "drebēties", false), //iedrebēties
		ThirdConj.refl(
				"-drīkstos, -drīksties,", "-drīkstas, pag. -drīkstējos", "drīkstēties", false), //iedrīkstēties
		ThirdConj.refl(
				"-dusos, -dusies,", "-dusas, pag. -dusējos", "dusēties", false), //atdusēties
		ThirdConj.refl(
				"-dziedos, -dziedies,", "-dziedas, pag. -dziedājos", "dziedāties", false), //aizdziedāties
		// E, F, G
		ThirdConj.refl(
				"-glūnos, -glūnies,", "-glūnas, pag. -glūnējos", "glūnēties", false), //izglūnēties
		ThirdConj.refl(
				"-guļos, -gulies,", "-guļas, pag. -gulējos", "gulēties", true), //aizgulēties
		// H, I, J, K
		ThirdConj.refl(
				"-knukstos, -knuksties,", "-knukstas, pag. -knukstējos", "knukstēties", false), //ieknukstēties
		ThirdConj.refl(
				"-krekstos, -kreksties,", "-krekstas, pag. -krekstējos", "krekstēties", false), //atkrekstēties
		ThirdConj.refl(
				"-krekšos, -krekšies,", "-krekšas, pag. -krekšējos", "krekšēties", false), //iekrekšēties
		ThirdConj.refl(
				"-krekšķos, -krekšķies,", "-krekšķas, pag. -krekšķējos", "krekšķēties", false), //atkrekšķēties
		ThirdConj.refl(
				"-kunkstos, -kunksties,", "-kunkstas, pag. -kunkstējos", "kunkstēties", false), //iekunkstēties
		ThirdConj.refl(
				"-kurnos, -kurnies,", "-kurnas, pag. -kurnējos", "kurnēties", false), //iekurnēties
		// L, M
		ThirdConj.refl(
				"-līdzos, -līdzies,", "-līdzas, pag. -līdzējos", "līdzēties", false), //izlīdzēties
		ThirdConj.refl(
				"-palīdzos, -palīdzies,", "-palīdzas, pag. -palīdzējos", "palīdzēties", false), //izpalīdzēties
		ThirdConj.refl(
				"-murkšos, -murkšies,", "-murkšas, pag. -murkšējos", "murkšēties", false), //iemurkšēties
		ThirdConj.refl(
				"-murkšķos, -murkšķies,", "-murkšķas, pag. -murkšķējos", "murkšķēties", false), //iemurkšķēties
		// N, Ņ
		ThirdConj.refl(
				"-ņaudos, -ņaudies,", "-ņaudas, pag. -ņaudējos", "ņaudēties", false), //izņaudēties
		ThirdConj.refl(
				"-ņerkstos, -ņerksties,", "-ņerkstas, pag. -ņerkstējos", "ņerkstēties", false), //ieņerkstēties
		ThirdConj.refl(
				"-ņurdos, -ņurdies,", "-ņurdas, pag. -ņurdējos", "ņurdēties", false), //ieņurdēties
		// O, P
		ThirdConj.refl(
				"-pīkstos, -pīksties,", "-pīkstas, pag. -pīkstējos", "pīkstēties", false), //iepīkstēties
		ThirdConj.refl(
				"-pinkšos, -pinkšies,", "-pinkšas, pag. -pinkšējos", "pinkšēties", false), //iepinkšēties
		ThirdConj.refl(
				"-pinkšķos, -pinkšķies,", "-pinkšķas, pag. -pinkšķējos", "pinkšķēties", false), //iepinkšķēties
		ThirdConj.refl(
				"-pukstos, -puksties,", "-pukstas, pag. -pukstējos", "pukstēties", false), //iepukstēties
		// R
		ThirdConj.refl(
				"-raudos, -raudies,", "-raudas, pag. -raudājos", "raudāties", false), //aizraudāties
		ThirdConj.refl(
				"-rocos, -rocies,", "-rocās, pag. -rocījos", "rocīties", false), // sarocīties
		// S
		ThirdConj.refl(
				"-sēžos, -sēdies,", "-sēžas, pag. -sēdējos", "sēdēties", true), //aizsēdēties
		ThirdConj.refl(
				"-smīnos, -smīnies,", "-smīnas, pag. -smīnējos", "smīnēties", false), //iesmīnēties
		ThirdConj.refl(
				"-spurkšos, -spurkšies,", "-spurkšas, pag. -spurkšējos", "spurkšēties", false), //iespurkšēties
		ThirdConj.refl(
				"-spurkšķos, -spurkšķies,", "-spurkšķas, pag. -spurkšķējos", "spurkšķēties", false), //iespurkšķēties
		ThirdConj.refl(
				"-stāvos, -stāvies,", "-stāvas, pag. -stāvējos", "stāvēties", false), //izstāvēties
		ThirdConj.refl(
				"-stenos, -stenies,", "-stenas, pag. -stenējos", "stenēties", false), //iestenēties
		ThirdConj.refl(
				"-svinos, -svinies,", "-svinas, pag. -svinējos", "svinēties", false), //aizsvinēties
		// Š
		ThirdConj.refl(
				"-šļupstos, -šļupsties,", "-šļupstas, pag. -šļupstējos", "šļupstēties", false), //iešļupstēties
		ThirdConj.refl(
				"-šņukstos, -šņuksties,", "-šņukstas, pag. -šņukstējos", "šņukstēties", false), //aizšņukstēties
		// T
		ThirdConj.refl(
				"-tarkšķos, -tarkšķies,", "-tarkšķas, pag. -tarkšķējos", "tarkšķēties", false), //ietarkšķēties
		ThirdConj.refl(
				"-trīcos, -trīcies,", "-trīcas, pag. -trīcējos", "trīcēties", false), //ietrīcēties
		ThirdConj.refl(
				"-trīsos, -trīsies,", "-trīsas, pag. -trīsējos", "trīsēties", false), //ietrīsēties
		// U, V
		ThirdConj.refl(
				"-vaidos, -vaidies,", "-vaidas, pag. -vaidējos", "vaidēties", false), //ievaidēties
		// Z

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Likumi, kam ir paralēlās formas.
		ThirdConj.refl3PersParallel(
				"-grandas, pag. -grandējās (retāk -grandās, 1. konj.)", "grandēties", false), //iegrandēties
		ThirdConj.refl3PersParallel(
				"-spindzas, pag. -spindzējās (retāk -spindzās, 1. konj.)", "spindzēties", false), //iespindzēties

		// Standartizētie.
		// A, B
		ThirdConj.refl3Pers("-blarkšas, pag. -blarkšējās", "blarkšēties", false), //ieblarkšēties
		ThirdConj.refl3Pers("-blarkšķas, pag. -blarkšķējās", "blarkšķēties", false), //ieblarkšķēties
		ThirdConj.refl3Pers("-blaukšas, pag. -blaukšējās", "blaukšēties", false), //ieblaukšēties
		ThirdConj.refl3Pers("-blaukšķas, pag. -blaukšķējās", "blaukšķēties", false), //ieblaukšķēties
		ThirdConj.refl3Pers("-brakšas, pag. -brakšējās", "brakšēties", false), //iebrakšēties
		ThirdConj.refl3Pers("-brakšķas, pag. -brakšķējās", "brakšķēties", false), //iebrakšķēties
		ThirdConj.refl3Pers("-brikšas, pag. -brikšējās", "brikšēties", false), //aizbrikšēties
		ThirdConj.refl3Pers("-brikšķas, pag. -brikšķējās", "brikšķēties", false), //aizbrikšķēties
		ThirdConj.refl3Pers("-brīkšas, pag. -brīkšējās", "brīkšēties", false), //aizbrīkšēties
		ThirdConj.refl3Pers("-brīkšķas, pag. -brīkšķējās", "brīkšķēties", false), //aizbrīkšķēties
		ThirdConj.refl3Pers("-būkšas, pag. -būkšējās", "būkšēties", false), //iebūkšēties
		ThirdConj.refl3Pers("-būkšķas, pag. -būkšķējās", "būkšķēties", false), //iebūkšķēties
		// C, Č
		ThirdConj.refl3Pers("-čabas, pag. -čabējās", "čabēties", false), //aizčabēties
		ThirdConj.refl3Pers("-čakstas, pag. -čakstējās", "čakstēties", false), //iečakstēties
		ThirdConj.refl3Pers("-čaukstas, pag. -čaukstējās", "čaukstēties", false), //aizčaukstēties
		ThirdConj.refl3Pers("-čirkstas, pag. -čirkstējās", "čirkstēties", false), //iečirkstēties
		ThirdConj.refl3Pers("-čurkstas, pag. -čurkstējās", "čurkstēties", false), //iečurkstēties
		ThirdConj.refl3Pers("-čūkstas, pag. -čūkstējās", "čūkstēties", false), //iečūkstēties
		// D
		ThirdConj.refl3Pers("-dārdas, pag. -dārdējās", "dārdēties", false), //aizdārdēties
		ThirdConj.refl3Pers("-dimdas, pag. -dimdējās", "dimdēties", false), //iedimdēties
		ThirdConj.refl3Pers("-dipas, pag. -dipējās", "dipēties", false), //iedipēties
		ThirdConj.refl3Pers("-drebas, pag. -drebējās", "drebēties", false), //aizdrebēties
		ThirdConj.refl3Pers("-dunas, pag. -dunējās", "dunēties", false), //iedunēties
		ThirdConj.refl3Pers("-dzinkstas, pag. -dzinkstējās", "dzinkstēties", false), //iedzinkstēties
		ThirdConj.refl3Pers("-dzirkstas, pag. -dzirkstējās", "dzirkstēties", false), //iedzirkstēties
		ThirdConj.refl3Pers("-džinkstas, pag. -džinkstējās", "džinkstēties", false), //iedžinkstēties
		// E, F, G
		ThirdConj.refl3Pers("-grabas, pag. -grabējās", "grabēties", false), //aizgrabēties
		ThirdConj.refl3Pers("-grandās, pag. -grandījās", "grandīties", false), //iegrandīties
		ThirdConj.refl3Pers("-gurkstas, pag. -gurkstējās", "gurkstēties", false), //aizgurkstēties
		ThirdConj.refl3Pers("-guldzas, pag. -guldzējās", "guldzēties", false), //ieguldzēties
		// H, I, J, K
		ThirdConj.refl3Pers("-klabas, pag. -klabējās", "klabēties", false), //aizklabēties
		ThirdConj.refl3Pers("-klaudzas, pag. -klaudzējās", "klaudzēties", false), //aizklaudzēties
		ThirdConj.refl3Pers("-klakstas, pag. -klakstējās", "klakstēties", false), //ieklakstēties
		ThirdConj.refl3Pers("-klakšas, pag. -klakšējās", "klakšēties", false), //ieklakšēties
		ThirdConj.refl3Pers("-klakšķas, pag. -klakšķējās", "klakšķēties", false), //ieklakšķēties
		ThirdConj.refl3Pers("-klinkšas, pag. -klinkšējās", "klinkšēties", false), //ieklinkšēties
		ThirdConj.refl3Pers("-klinkšķas, pag. -klinkšķējās", "klinkšķēties", false), //ieklinkšķēties
		ThirdConj.refl3Pers("-klukstas, pag. -klukstējās", "klukstēties", false), //aizklukstēties
		ThirdConj.refl3Pers("-klunkstas, pag. -klunkstējās", "klunkstēties", false), //ieklunkstēties
		ThirdConj.refl3Pers("-klunkšas, pag. -klunkšējās", "klunkšēties", false), //aizklunkšēties
		ThirdConj.refl3Pers("-klunkšķas, pag. -klunkšķējās", "klunkšķēties", false), //aizklunkšķēties
		ThirdConj.refl3Pers("-knakstās, pag. -knakstējās", "knakstēties", false), //aizknakstēties
		ThirdConj.refl3Pers("-knakšas, pag. -knakšējās", "knakšēties", false), //aizknakšēties
		ThirdConj.refl3Pers("-knakšķas, pag. -knakšķējās", "knakšķēties", false), //aizknakšķēties
		ThirdConj.refl3Pers("-knaukstas, pag. -knaukstējās", "knaukstēties", false), //ieknaukstēties
		ThirdConj.refl3Pers("-knaukšas, pag. -knaukšējās", "knaukšēties", false), //aizknaukšēties
		ThirdConj.refl3Pers("-knaukšķas, pag. -knaukšķējās", "knaukšķēties", false), //aizknaukšķēties
		ThirdConj.refl3Pers("-knikstas, pag. -knikstējās", "knikstēties", false), //ieknikstēties
		ThirdConj.refl3Pers("-knikšas, pag. -knikšējās", "knikšēties", false), //aizknikšēties
		ThirdConj.refl3Pers("-knikšķas, pag. -knikšķējās", "knikšķēties", false), //aizknikšķēties
		ThirdConj.refl3Pers("-kņudas, pag. -kņudējās", "kņudēties", false), //iekņudēties
		ThirdConj.refl3Pers("-krakstas, pag. -krakstējās", "krakstēties", false), //aizkrakstēties
		ThirdConj.refl3Pers("-krakšķas, pag. -krakšķējās", "krakšķēties", false), //aizkrakšķēties
		ThirdConj.refl3Pers("-kraukstas, pag. -kraukstējās", "kraukstēties", false), //iekraukstēties
		ThirdConj.refl3Pers("-kraukšas, pag. -kraukšējās", "kraukšēties", false), //iekraukšēties
		ThirdConj.refl3Pers("-kraukšķas, pag. -kraukšķējās", "kraukšķēties", false), //iekraukšķēties
		ThirdConj.refl3Pers("-krikstas, pag. -krikstējās", "krikstēties", false), //iekrikstēties
		ThirdConj.refl3Pers("-krikšas, pag. -krikšējās", "krikšēties", false), //iekrikšēties
		ThirdConj.refl3Pers("-krikšķas, pag. -krikšķējās", "krikšķēties", false), //iekrikšķēties
		ThirdConj.refl3Pers("-kurkstas, pag. -kurkstējās", "kurkstēties", false), //aizkurkstēties
		ThirdConj.refl3Pers("-kurkšķas, pag. -kurkšķējās", "kurkšķēties", false), //aizkurkšķēties
		ThirdConj.refl3Pers("-kutas, pag. -kutējās", "kutēties", false), //iekutēties
		// Ķ
		ThirdConj.refl3Pers("-ķaukstas, pag. -ķaukstējās", "ķaukstēties", false), //ieķaukstēties
		ThirdConj.refl3Pers("-ķērkstas, pag. -ķērkstējās", "ķērkstēties", false), //ieķērkstēties
		// L, M
		ThirdConj.refl3Pers("-mirdzas, pag. -mirdzējās", "mirdzēties", false), //aizmirdzēties
		// N
		ThirdConj.refl3Pers("-niezas, pag. -niezējās", "niezēties", false), //ieniezēties
		// Ņ
		ThirdConj.refl3Pers("-ņirbas, pag. -ņirbējās", "ņirbēties", false), //ieņirbēties
		ThirdConj.refl3Pers("-ņirkstas, pag. -ņirkstējās", "ņirkstēties", false), //ieņirkstēties
		ThirdConj.refl3Pers("-ņirkšas, pag. -ņirkšējās", "ņirkšēties", false), //ieņirkšēties
		ThirdConj.refl3Pers("-ņirkšķas, pag. -ņirkšķējās", "ņirkšķēties", false), //ieņirkšķēties
		ThirdConj.refl3Pers("-ņurkstas, pag. -ņurkstējās", "ņurkstēties", false), //ieņurkstēties
		ThirdConj.refl3Pers("-ņurkšas, pag. -ņurkšējās", "ņurkšēties", false), //ieņurkšēties
		ThirdConj.refl3Pers("-ņurkšķas, pag. -ņurkšķējās", "ņurkšķēties", false), //ieņurkšķēties
		// O, P
		ThirdConj.refl3Pers("-pakšas, pag. -pakšējās", "pakšēties", false), //iepakšēties
		ThirdConj.refl3Pers("-pakšķas, pag. -pakšķējās", "pakšķēties", false), //iepakšķēties
		ThirdConj.refl3Pers("-parkšas, pag. -parkšējās", "parkšēties", false), //ieparkšēties
		ThirdConj.refl3Pers("-parkšķas, pag. -parkšķējās", "parkšķēties", false), //ieparkšķēties
		ThirdConj.refl3Pers("-pēkstas, pag. -pēkstējās", "pēkstēties", false), //iepēkstēties
		ThirdConj.refl3Pers("-pēkšas, pag. -pēkšējās", "pēkšēties", false), //iepēkšēties
		ThirdConj.refl3Pers("-pēkšķas, pag. -pēkšķējās", "pēkšķēties", false), //iepēkšķēties
		ThirdConj.refl3Pers("-plarkšas, pag. -plarkšējās", "plarkšēties", false), //ieplarkšēties
		ThirdConj.refl3Pers("-plarkšķas, pag. -plarkšķējās", "plarkšķēties", false), //ieplarkšķēties
		ThirdConj.refl3Pers("-plerkšas, pag. -plerkšējās", "plerkšēties", false), //ieplerkšēties
		ThirdConj.refl3Pers("-plerkšķas, pag. -plerkšķējās", "plerkšķēties", false), //ieplerkšķēties
		ThirdConj.refl3Pers("-plīkšas, pag. -plīkšējās", "plīkšēties", false), //ieplīkšēties
		ThirdConj.refl3Pers("-plīkšķas, pag. -plīkšķējās", "plīkšķēties", false), //ieplīkšķēties
		ThirdConj.refl3Pers("-plinkšas, pag. -plinkšējās", "plinkšēties", false), //ieplinkšēties
		ThirdConj.refl3Pers("-plinkšķas, pag. -plinkšķējās", "plinkšķēties", false), //ieplinkšķēties
		ThirdConj.refl3Pers("-pliukšas, pag. -pliukšējās", "pliukšēties", false), //iepliukšēties
		ThirdConj.refl3Pers("-pliukšķas, pag. -pliukšķējās", "pliukšķēties", false), //iepliukšķēties
		ThirdConj.refl3Pers("-pliukšķas, pag. -pliukšķējās", "pliukšķēties", false), //iepliukšķēties
		ThirdConj.refl3Pers("-plunkšas, pag. -plunkšējās", "plunkšēties", false), //ieplunkšēties
		ThirdConj.refl3Pers("-plunkšķas, pag. -plunkšķējās", "plunkšķēties", false), //ieplunkšķēties
		ThirdConj.refl3Pers("-pļerkšas, pag. -pļerkšējās", "pļerkšēties", false), //iepļerkšēties
		ThirdConj.refl3Pers("-pļerkšķas, pag. -pļerkšķējās", "pļerkšķēties", false), //iepļerkšķēties
		ThirdConj.refl3Pers("-pukšas, pag. -pukšējās", "pukšēties", false), //iepukšēties
		ThirdConj.refl3Pers("-pukšķas, pag. -pukšķējās", "pukšķēties", false), //iepukšķēties
		// R
		ThirdConj.refl3Pers("-rības, pag. -rībējās", "rībēties", false), //ierībēties
		ThirdConj.refl3Pers("-rukstas, pag. -rukstējās", "rukstēties", false), //ierukstēties
		ThirdConj.refl3Pers("-rukšas, pag. -rukšējās", "rukšēties", false), //aizrukšēties
		ThirdConj.refl3Pers("-rukšķas, pag. -rukšķējās", "rukšķēties", false), //aizrukšķēties
		// S
		ThirdConj.refl3Pers("-sanas, pag. -sanējās", "sanēties", false), //aizsanēties
		ThirdConj.refl3Pers("-sāpas, pag. -sāpējās", "sāpēties", false), //aizsāpeties
		ThirdConj.refl3Pers("-skanas, pag. -skanējās", "skanēties", false), //aizskanēties
		ThirdConj.refl3Pers("-skrabas, pag. -skrabējās", "skrabēties", false), //ieskrabēties
		ThirdConj.refl3Pers("-skrapstas, pag. -skrapstējās", "skrapstēties", false), //ieskrapstēties
		ThirdConj.refl3Pers("-skrapšas, pag. -skrapšējās", "skrapšēties", false), //ieskrapšēties
		ThirdConj.refl3Pers("-skrapšķas, pag. -skrapšķējās", "skrapšķēties", false), //ieskrapšķēties
		ThirdConj.refl3Pers("-smilkstas, pag. -smilkstējās", "smilkstēties", false), //iesmilkstēties
		ThirdConj.refl3Pers("-smirdas, pag. -smirdējās", "smirdēties", false), //iesmirdēties
		ThirdConj.refl3Pers("-sparkšas, pag. -sparkšējās", "sparkšēties", false), //iesparkšēties
		ThirdConj.refl3Pers("-sparkšķas, pag. -sparkšķējās", "sparkšķēties", false), //iesparkšķēties
		ThirdConj.refl3Pers("-spīdas, pag. -spīdējās", "spīdēties", false), //iespīdēties
		ThirdConj.refl3Pers("-sprakstas, pag. -sprakstējās", "sprakstēties", false), //iesprakstēties
		ThirdConj.refl3Pers("-sprakšas, pag. -sprakšējās", "sprakšēties", false), //iesprakšēties
		ThirdConj.refl3Pers("-sprakšķas, pag. -sprakšķējās", "sprakšķēties", false), //iesprakšķēties
		ThirdConj.refl3Pers("-spridzas, pag. -spridzējās", "spridzēties", false), //iespridzēties
		ThirdConj.refl3Pers("-sprikstas, pag. -sprikstējās", "sprikstēties", false), //iesprikstēties
		ThirdConj.refl3Pers("-strinkšas, pag. -strinkšējās", "strinkšēties", false), //aizstrinkšēties
		ThirdConj.refl3Pers("-strinkšķas, pag. -strinkšķējās", "strinkšķēties", false), //aizstrinkšķēties
		ThirdConj.refl3Pers("-sūkstas, pag. -sūkstējās", "sūkstēties", false), //iesūkstēties
		ThirdConj.refl3Pers("-sūrstas, pag. -sūrstējās", "sūrstēties", false), //iesūrstēties
		// Š
		ThirdConj.refl3Pers("-šķindas, pag. -šķindējās", "šķindēties", false), //aizšķindēties
		ThirdConj.refl3Pers("-šļakstas, pag. -šļakstējās", "šļakstēties", false), //iešļakstēties
		ThirdConj.refl3Pers("-šmīkstas, pag. -šmīkstējās", "šmīkstēties", false), //iešmīkstēties
		ThirdConj.refl3Pers("-šņakstas, pag. -šņakstējās", "šņakstēties", false), //iešņakstēties
		ThirdConj.refl3Pers("-šņirkstas, pag. -šņirkstējās", "šņirkstēties", false), //iešņirkstēties
		ThirdConj.refl3Pers("-švirkstas, pag. -švirkstējās", "švirkstēties", false), //aizšvirkstēties
		ThirdConj.refl3Pers("-švīkstas, pag. -švīkstējās", "švīkstēties", false), //iešvīkstēties
		// T
		ThirdConj.refl3Pers("-tarkšas, pag. -tarkšējās", "tarkšēties", false), //aiztarkšēties
		ThirdConj.refl3Pers("-tikšas, pag. -tikšējās", "tikšēties", false), //ietikšēties
		ThirdConj.refl3Pers("-tikšķas, pag. -tikšķējās", "tikšķēties", false), //ietikšķēties
		ThirdConj.refl3Pers("-tinkšas, pag. -tinkšējās", "tinkšēties", false), //aiztinkšēties
		ThirdConj.refl3Pers("-tinkšķas, pag. -tinkšķējās", "tinkšķēties", false), //aiztinkšķēties
		ThirdConj.refl3Pers("-tirkšas, pag. -tirkšējās", "tirkšēties", false), //ietirkšēties
		ThirdConj.refl3Pers("-tirkšķas, pag. -tirkšķējās", "tirkšķēties", false), //ietirkšķēties
		ThirdConj.refl3Pers("-trinkšas, pag. -trinkšējās", "trinkšēties", false), //aiztrinkšēties
		ThirdConj.refl3Pers("-trinkšķas, pag. -trinkšķējās", "trinkšķēties", false), //aiztrinkšķēties
		// U
		ThirdConj.refl3Pers("-urkšas, pag. -urkšējās", "urkšēties", false), //ieurkšēties
		ThirdConj.refl3Pers("-urkšķas, pag. -urkšķējās", "urkšķēties", false), //ieurkšķēties
		// V
		ThirdConj.refl3Pers("-vankšas, pag. -vankšējās", "vankšēties", false), //ievankšēties
		ThirdConj.refl3Pers("-vankšķas, pag. -vankšķējās", "vankšķēties", false), //ievankšķēties
		ThirdConj.refl3Pers("-vaukšas, pag. -vaukšējās", "vaukšēties", false), //ievaukšēties
		ThirdConj.refl3Pers("-vaukšķas, pag. -vaukšķējās", "vaukšķēties", false), //ievaukšķēties
		ThirdConj.refl3Pers("-vēkšas, pag. -vēkšējās", "vēkšēties", false), //ievēkšēties
		ThirdConj.refl3Pers("-vēkšķas, pag. -vēkšķējās", "vēkšķēties", false), //ievēkšķēties
		ThirdConj.refl3Pers("-vizas, pag. -vizējās", "vizēties", false), //ievizēties
		// Z
		ThirdConj.refl3Pers("-zibas, pag. -zibējās", "zibēties", false), //iezibēties
		ThirdConj.refl3Pers("-zuzas, pag. -zuzējās", "zuzēties", false), //iezuzēties
		// Ž
		ThirdConj.refl3Pers("-žvadzas, pag. -žvadzējās", "žvadzēties", false), //iežvadzēties
		ThirdConj.refl3Pers("-žvarkstas, pag. -žvarkstējās", "žvarkstēties", false), //iežvarkstēties
		ThirdConj.refl3Pers("-žvikstas, pag. -žvikstējās", "žvikstēties", false), //iežvikstēties
		ThirdConj.refl3Pers("-žvīkstas, pag. -žvīkstējās", "žvīkstēties", false), //iežvīkstēties

		// Likumi daudzskaitļa formām.
		PluralVerbRule.thirdConjRefl("-dziedamies, pag. -dziedājāmies", "dziedāties", false), //apdziedāties
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] reflMultiConjVerb = {
		// Galotņu šabloni.
		// Visām personām (3. personas likumi netiek atvasināti).
		BaseRule.secondThirdConjReflAllPersParallel(
				"-os, -ies, -as, arī -ējos, -ējies, -ējas, pag. -ējos", ".*ēties", false), // mīlēties
		BaseRule.secondThirdConjReflAllPersParallel(
				"-ījos, -ījies, -ījas, arī -os, -ies, -ās, pag. -ījos", ".*īties", false), // blēdīties

		// 3. personai.
		SecondThirdConj.direct3PersParallel(
				"-ās, arī -ījas, pag. -ījās", "īties", false), // rotīties
		SecondThirdConj.direct3PersParallel(
				"-ījas, arī -ās, pag. -ījās", "īties", false), // pietašķīties

		// Darbības vārdu specifiskie likumi.
		// Nav.
	};
}
