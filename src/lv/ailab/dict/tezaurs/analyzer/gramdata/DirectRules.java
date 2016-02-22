package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Values;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.*;
import lv.ailab.dict.utils.Tuple;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * TGram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar Rule.applyDirect().
 *
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas Values uzskaitījumā.
 *
 * @author Lauma
 */
public class DirectRules
{
	/**
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final Rule[] other = {

		// 1. paradigma: 1. dekl. lietvārdi, -s
		BaseRule.noun("-a, dsk. ģen. -ku, v.", ".*ks", 1, null, new Tuple[] {Features.GENDER__MASC}), // cepurnieks
		// 6. paradigma: 3. deklinācijas lietvārdi
		BaseRule.noun("-us, v.", ".*us", 6, null, new Tuple[] {Features.GENDER__MASC}), // dienvidus
		// 7. paradigma: 4. dekl. lietvārdi, sieviešu dzimte
		BaseRule.noun("-as, dsk. ģen. -vu, s.", ".*va", 7, null, new Tuple[] {Features.GENDER__FEM}), // apskava
		// 34. paradigma: Atgriezeniskie lietvārdi -šanās
		BaseRule.noun(
				"ģen. -ās, akuz. -os, instr. -os, dsk. -ās, ģen. -os, akuz. -ās, s.", ".*šanās", 34,
				new Tuple[]{Features.POS__REFL_NOUN}, new Tuple[]{Features.GENDER__FEM}), //aizbildināšanās
		// Paradigmas: 7, 8 - kopdzimtes lietvārdi, galotne -a
		BaseRule.noun("ģen. -as, v. dat. -am, s. dat. -ai, kopdz.", ".*a", new Integer[]{7, 8}, null,
				new Tuple[]{Tuple.of(Keys.GENDER, Values.COGENDER.s)}), // aitasgalva, aizmārša

		// Paradigmas: 1, 2 - 1. deklinācija
		// Šeit varētu vēlāk vajadzēt likumus paplašināt, ja parādās jauni šķirkļi.
		BaseRule.of("lietv. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
				new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN}), // aerobs
		BaseRule.noun("vsk. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
				new Tuple[]{Features.GENDER__MASC, Tuple.of(Keys.NUMBER, Values.SINGULAR.s)}), // acteks
		BaseRule.noun("-a, vsk.", new SimpleSubRule[]{
						SimpleSubRule.of(".*(akmen|asmen|mēnes|ziben|ūden|ruden)s", new Integer[]{4}, new Tuple[]{Features.GENDER__MASC}),
						SimpleSubRule.of(".*suns", new Integer[]{5}, new Tuple[]{Features.GENDER__MASC}),
						SimpleSubRule.of(".*is", new Integer[]{3}, new Tuple[]{Features.GENDER__MASC}),
						SimpleSubRule.of(".*š", new Integer[]{2}, new Tuple[]{Features.GENDER__MASC}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{1}, new Tuple[]{Features.GENDER__MASC})},
				null), // aizkars, cīsiņš, sakņkājis

		// Nedefinēta paradigma: divdabji
		BaseRule.participleIs("-gušais; s. -gusi, -gusī", ".*dzis"), // aizdudzis
		BaseRule.participleIs("-ušais; s. -usi, -usī", ".*[cdjlmprstv]is"), // aizkūpis

		// Paradigmas: 7, 11
		BaseRule.noun("-as, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*a", new Integer[]{7}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]as", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{11}, null)},
				new Tuple[]{Features.GENDER__FEM}), // aberācija, milns, najādas

		// Paradigmas: 9, 11
		BaseRule.noun("dsk. ģen. -ņu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, null),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{11}, null)},
				new Tuple[]{Features.GENDER__FEM}), // ādmine, bākuguns, bārkšsaknes

		// Paradigma: 11 - 6. dekl.
		BaseRule.noun("-ts, -šu", ".*ts", 11, new Tuple[]{Features.GENDER__FEM}, null), //abonentpults
		BaseRule.noun("-vs, -vju", ".*vs", 11, new Tuple[]{Features.GENDER__FEM}, null), //adatzivs

		BaseRule.noun("-žu, v.", ".*ļaudis", 11,
				new Tuple[]{Features.ENTRYWORD__PLURAL, Features.USED_ONLY__PLURAL},
				new Tuple[]{Features.GENDER__MASC}), //ļaudis

		// Paradigmas: 13, 14 - īpašības vārdi - skaidri pateikts
		BaseRule.adjective("īp. v. -ais; s. -a, -ā"), // aerobs
		BaseRule.of("s. -as; adj.", ".*i", new Integer[]{13, 14},
				new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM},
				new Tuple[]{Features.POS__ADJ, Features.GENDER__MASC, Features.GENDER__FEM}), // abēji 2

		// Paradigmas: 13, 14 - īpašības vārdi vai divdabji
		BaseRule.adjectiveParticiple("-ais; s. -a, -ā"), // abējāds, acains, agāms
		BaseRule.adjectiveParticiple("-ais, s. -a, -ā"), // abējāds, acains, agāms
		BaseRule.of("s. -as; tikai dsk.", new SimpleSubRule[]{
						SimpleSubRule.of(".*oši", new Integer[]{13, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_OSS, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*ti", new Integer[]{13, 14, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_TS, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*dami", new Integer[]{13, 14, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_DAMS, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*[aā]mi", new Integer[]{13, 14, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_AMS, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*uši", new Integer[]{13, 0}, new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_IS, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*īgi", new Integer[]{13}, new Tuple[]{Features.POS__ADJ, Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*i", new Integer[]{13, 14}, new Tuple[]{Features.POS__ADJ, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM})},
				new Tuple[]{Features.USED_ONLY__PLURAL, Features.GENDER__MASC, Features.GENDER__FEM}), // abēji 1

		// Paradigma: 25 - vietniekvārdi
		BaseRule.of("s. -as; vietniekv.", ".*i", 25, new Tuple[]{Features.ENTRYWORD__PLURAL},
				new Tuple[]{Features.POS__PRONOUN}), //abi

		// Paradigma: 30 - jaundzimušais, pēdējais
		//BaseRule.noun("-šā, v. -šās, s.", ".*ušais", new Integer[] {30, 0},
		//		new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE, Features.POS__PARTICIPLE_IS, Features.UNCLEAR_PARADIGM}, null), //iereibušais	//TODO vai te vajag alternatīvo lemmu?
		BaseRule.of("-ā, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*tais", new Integer[] {30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_TS, Features.CONTAMINATION__NOUN, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*ušais", new Integer[] {30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_IS, Features.CONTAMINATION__NOUN, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*[aā]mais", new Integer[] {30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_AMS, Features.CONTAMINATION__NOUN, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*ais", new Integer[] {30},
								new Tuple[]{Features.POS__ADJ, Features.CONTAMINATION__NOUN})},
				new Tuple[]{Features.GENDER__MASC}), //pirmdzimtais, ieslodzītais, cietušais, brīvprātīgais, mīļākais

		// Paradigmas: 30 -  jaundzimušais, pēdējais
		// 34 paradigma: Atgriezeniskie lietvārdi -šanās
		BaseRule.of("-ās, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*šanās", new Integer[]{34},
								new Tuple[]{Features.POS__REFL_NOUN}),
						SimpleSubRule.of(".*tā", new Integer[]{30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_TS, Features.CONTAMINATION__NOUN, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*ošā", new Integer[]{30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_OSS, Features.CONTAMINATION__NOUN, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*[aā]mā", new Integer[]{30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__PARTICIPLE_AMS, Features.CONTAMINATION__NOUN, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*ā", new Integer[]{30},
								new Tuple[]{Features.POS__ADJ, Features.CONTAMINATION__NOUN})},
				new Tuple[]{Features.GENDER__FEM}), // pirmdzimtā, notiesātā -šanās

		// Paradigmas: 22, 30, 0
		BaseRule.of("s. -ā", new SimpleSubRule[]{
						SimpleSubRule.of(".*ošais", new Integer[]{22, 30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__NUMERAL, Features.POS__PARTICIPLE_OSS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*tais", new Integer[]{22, 30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__NUMERAL, Features.POS__PARTICIPLE_TS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*[aā]mais", new Integer[]{22, 30, 0},
								new Tuple[]{Features.POS__ADJ, Features.POS__NUMERAL, Features.POS__PARTICIPLE_AMS, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS}),
						SimpleSubRule.of(".*ais", new Integer[]{22, 30},
								new Tuple[]{Features.POS__ADJ, Features.POS__NUMERAL, Features.UNCLEAR_PARADIGM, Features.UNCLEAR_POS})},
				new Tuple[]{Features.GENDER__FEM, Features.GENDER__MASC}) // agrākais, pirmais, aiznākošais	//TODO vai te vajag alternatīvo lemmu?
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
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ju, s.", ".*je"), //baskāje
		BaseRule.noun("-es, dsk. ģen. -ju", ".*je", 9, new Tuple[]{Features.GENDER__FEM}, null), // aloje
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ķu, s.", ".*[ķ]e"), //agnostiķe, leduspuķe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ļu, s.", ".*[l]e"), //ābele
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ņu, s.", ".*[n]e"), //ābolaine
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ru, s.", ".*re"), //administratore
		BaseRule.noun("-es, dsk. ģen. -ru", ".*re", 9, new Tuple[]{Features.GENDER__FEM}, null), // ādere
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šu, s.", ".*[sšt]e"), //abate, adrese, larkše, apokalipse, note
		BaseRule.fifthDeclStd("-es, dsk. ģen. -žu, s.", ".*[dz]e"), //ābolmaize, aģitbrigāde, bilde, pirolīze

		BaseRule.fifthDeclStd("-es, dsk. ģen. -bju, s.", ".*be"), //apdobe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -džu, s.", ".*dze"), //kāršaudze
		BaseRule.fifthDeclStd("-es, dsk. ģen. -kšu, s.", ".*kte"), //daudzpunkte
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ļļu, s.", ".*lle"), //zaļumballe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -ļņu, s.", ".*lne"), //nokalne
		BaseRule.fifthDeclStd("-es, dsk. ģen. -mju, s.", ".*me"), //agronome
		BaseRule.fifthDeclStd("-es, dsk. ģen. -pju, s.", ".*pe"), //bērzlapju
		BaseRule.noun("-es, dsk. ģen. -pju", ".*pe", 9, new Tuple[]{Features.GENDER__FEM}, null), // antilope
		BaseRule.fifthDeclStd("-es, dsk. ģen. -smju, s.", ".*sme"), //noslieksme
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šņu, s.", ".*sne"), //izloksne
		BaseRule.noun("-es, dsk. ģen. -šņu", ".*sne", 9, new Tuple[]{Features.GENDER__FEM}, null), // aizkrāsne
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šķu, s.", ".*šķe"), //draišķe
		BaseRule.fifthDeclStd("-es, dsk. ģen. -šļu, s.", ".*sle"), //gaisagrābsle
		BaseRule.fifthDeclStd("-es, dsk. ģen. -vju, s.", ".*ve"), //agave, aizstāve
		BaseRule.fifthDeclStd("-es, dsk. ģen. -žņu, s.", ".*zne"), //asteszvaigzne

		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -du, s.", ".*de"), // diplomande
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -fu, s.", ".*fe"), //arheogrāfe
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. mufu, s.", ".*mufe"), //mufe
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -pu, s.", ".*pe"), // filantrope
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -su, s.", ".*se"), // bise
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -stu, s.", ".*ste"), //abolicioniste
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -tu, s.", ".*te"), // antisemīte
		BaseRule.fifthDeclNoChange("-es, dsk. ģen. -zu, s.", ".*ze"), // autobāze
		BaseRule.noun("-es, dsk. ģen. -zu", ".*ze", 9, new Tuple[]{Features.GENDER__FEM, Features.NO_SOUNDCHANGE}, null), // dabasgāze


			// Vienskaitlis + daudzskaitlis
		BaseRule.noun("-es, dsk. ģen. -pju, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*pe", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM}),
						SimpleSubRule.of(".*pes", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.GENDER__FEM})},
				null), // aitkope, antilope, tūsklapes

		// Nejauki, pārāk specifiski gdījumi
		// Šiem defišu izņemšanas mehānisms īsti neder, jo vienā vietā vajag atstāt.
		BaseRule.fifthDeclStd("-es, dsk. ģen. aļģu, s.", ".*aļģe"), //aļģe
		BaseRule.fifthDeclStd("-es, dsk. ģen. āru, s.", ".*āre"), //āre
		BaseRule.fifthDeclStd("-es, dsk. ģen. biržu, s.", ".*birze"), //birze
		BaseRule.fifthDeclStd("-es, dsk. ģen. pūšu, s.", ".*pūte"), //pūte 1, 3
		BaseRule.fifthDeclStd("-es, dsk. ģen. puvju, s.", ".*puve"), //puve
		BaseRule.fifthDeclStd("-es, dsk. ģen. šaļļu, s.", ".*šalle"), //šalle
		BaseRule.fifthDeclStd("-es, dsk. ģen. -upju, s.", ".*upe"), //dzirnavupe

		// Nestandartīgie
		BaseRule.noun("-es, s., dsk. ģen. -bju", ".*be", 9,
				null, new Tuple[]{Features.GENDER__FEM}), //acetilsalicilskābe
		BaseRule.noun("-es, s. dsk. -es, -bju", ".*be", 9,
				null, new Tuple[]{Features.GENDER__FEM}), //astilbe

		BaseRule.noun("-ļu, s.", ".*les", 9,
			new Tuple[]{Features.ENTRYWORD__PLURAL}, new Tuple[]{Features.GENDER__FEM}), //bailes

		// Miju varianti
		BaseRule.noun("-es, dsk. ģen. -stu, arī -šu", ".*ste", 9,
				new Tuple[]{Features.GENDER__FEM, Tuple.of(Keys.INFLECTION_WEARDNES, Values.STU_SHU_CHANGE.s)},
				null), //dzeņaukste
	};

	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final Rule[] secondDeclNoun = {
		BaseRule.secondDeclStd("-ļa, dsk. ģen. -ļu, v.", ".*lis"), //brokolis
		BaseRule.secondDeclStd("-ņa, dsk. ģen. -ņu, v.", ".*nis"), //bizmanis
		BaseRule.secondDeclStd("-ša, dsk. ģen. -šu, v.", ".*[st]is"), //auseklītis

		BaseRule.noun("-ns, dsk. ģen. -ņu, v.", ".*ns", 4, null, new Tuple[] {Features.GENDER__MASC}), // bruģakmens

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
				new Tuple[]{Features.GENDER__MASC}), // acumirklis, (bacils, durkls=kļūda)
		BaseRule.noun("-ņa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ņš", new Integer[]{2}, null),
						SimpleSubRule.of(".*nis", new Integer[]{3}, null),
						SimpleSubRule.of(".*suns", new Integer[]{5}, null)},
				new Tuple[]{Features.GENDER__MASC}), // abesīnis, dižtauriņš, dzinējsuns
		BaseRule.noun("-ša, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*[sšt]is", new Integer[]{3}, null),
						SimpleSubRule.of(".*ss", new Integer[]{5}, null)},
				new Tuple[]{Features.GENDER__MASC}), // abrkasis, lemess
		// Bez mijām
		BaseRule.noun("-ra, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*rs", new Integer[]{1}, null),
						SimpleSubRule.of(".*ris", new Integer[]{3}, null)},
				new Tuple[]{Features.GENDER__MASC}), // airis, mūrniekmeistars
		BaseRule.noun("-sa, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*ss", new Integer[]{1}, null),
						SimpleSubRule.of(".*sis", new Integer[]{3}, new Tuple[]{Features.NO_SOUNDCHANGE})},
				new Tuple[]{Features.GENDER__MASC}), // balanss, kūrviesis
		BaseRule.noun("-ta, v.", ".*tis", new Integer[]{3},
				new Tuple[]{Features.NO_SOUNDCHANGE},
				new Tuple[]{Features.GENDER__MASC}), // stereotālskatis
		// Vispārīgā galotne, kas der visam un neder nekam
		// TODO: "viņš" ari ir šāda galotne
		BaseRule.noun("-a, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*(desmit|simt|miljon|miljard|biljard)s", new Integer[]{1, 23}, new Tuple[]{Features.POS__NUMERAL, Features.UNCLEAR_POS, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*(vien|otr)s", new Integer[]{1, 23, 25}, new Tuple[]{Features.POS__NUMERAL, Features.POS__PRONOUN, Features.UNCLEAR_POS, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*[^aeiouāēīōū]s", new Integer[]{1, 25}, new Tuple[]{Features.POS__PRONOUN, Features.UNCLEAR_POS, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*š", new Integer[]{2, 25}, new Tuple[]{Features.POS__PRONOUN, Features.UNCLEAR_POS, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*[ģjķr]is", new Integer[]{3}, null)},
				new Tuple[]{Features.GENDER__MASC}), // abats, akustiķis//, sparguļi, skostiņi, viens

		// Daudzkaitlis, vīriešu dzimte
		// Ar mijām
		BaseRule.noun("-ņu, v.", ".*ņi", new Integer[]{1, 2, 3, 4, 5},
				new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM},
				new Tuple[]{Features.GENDER__MASC}), // bretoņi
		// Vispārīgā galotne, kas der visam un neder nekam
		BaseRule.noun("-u, v.", new SimpleSubRule[]{
						SimpleSubRule.of(".*š", new Integer[]{2}, null),
						SimpleSubRule.of(".*(otāji|umi|anti|nieki|[aeiouāēīōū]īdi|isti|mēsli|svārki|plūdi|rati|vecāki|bērni|raksti|vidi|rīti|vakari|vārdi|kapi|augi|svētki|audi|laiki|putni|svari)",
								new Integer[]{1}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[bcdghklmnpstvz]i", new Integer[]{1}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*(ieši|āņi|ēži|grieži|stāvji|grauži|brunči|viļņi|ceļi|liberāļi|krampji|kaļķi)",
								new Integer[]{3}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*suņi", new Integer[]{5}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ši", new Integer[]{1, 3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*[čļņž]i", new Integer[]{2, 3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*(ģ|[mv]j)i", new Integer[]{3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*([ķr]|[aeiāē]j)i", new Integer[]{1, 2, 3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
				},
				new Tuple[]{Features.GENDER__MASC}),
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
						SimpleSubRule.of(".*ņas", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*nis", new Integer[]{11}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
				},
				new Tuple[]{Features.GENDER__FEM}), // acenes, iemaņas, balodene, robežugunis
		BaseRule.noun("-šu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*te", new Integer[]{9}, null),
						SimpleSubRule.of(".*šas", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[st]es", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*tis", new Integer[]{11}, new Tuple[]{Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // ahajiete, aizkulises, autosacīkstes, klaušas, šķūtis
		BaseRule.noun("-žu, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*žas", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[dz]es", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // mirādes, graizes, bažas
		// Vispārīgā galotne, kas der visam un neder nekam
		BaseRule.noun("-u, s.", new SimpleSubRule[]{
						SimpleSubRule.of(".*a", new Integer[]{7}, null),
						SimpleSubRule.of(".*[^aeiouāēīōū]as", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ķes", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // aijas, spēķes, zeķes, konkrēcija


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
				new Tuple[]{Features.ENTRYWORD__PLURAL}, new Tuple[]{Features.GENDER__MASC}), //afroamerikāņi
			// konflikts ar "bizmanis"

		// Vissliktākie šabloni - satur tikai vienu galotni un neko citu.
		// Paradigmas: 9, 7 - vienskaitlī un daudzskaitlī
		BaseRule.noun("-žu", new SimpleSubRule[]{
						SimpleSubRule.of(".*ži", new Integer[]{1, 3}, new Tuple[]{Features.GENDER__MASC, Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*[dz]e", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM}),
						SimpleSubRule.of(".*[dz]es", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM, Features.ENTRYWORD__PLURAL})},
				null), // gliemeži, abioģenēze, ablumozes, akolāde, nematodes
		BaseRule.noun("-ņu", new SimpleSubRule[]{
						SimpleSubRule.of(".*ne", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM}),
						SimpleSubRule.of(".suņi", new Integer[]{5}, new Tuple[]{Features.GENDER__MASC, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*ņi", new Integer[]{1, 2, 3, 4}, new Tuple[]{Features.GENDER__MASC, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						SimpleSubRule.of(".*nes", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM, Features.ENTRYWORD__PLURAL}),
						SimpleSubRule.of(".*ņas", new Integer[]{7}, new Tuple[]{Features.GENDER__FEM, Features.ENTRYWORD__PLURAL})},
				null), // celtņi, agrene, aizlaidnes
		// Paradigma: 2, 3
		BaseRule.noun("-ņa", new SimpleSubRule[]{
						SimpleSubRule.of(".*akmens", new Integer[]{4}, new Tuple[]{Features.GENDER__MASC}),
						SimpleSubRule.of(".*ņš", new Integer[]{2}, new Tuple[]{Features.GENDER__MASC}),
						SimpleSubRule.of(".*nis", new Integer[]{3}, new Tuple[]{Features.GENDER__MASC})},
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
		FirstConjRule.direct("-dullstu, -dullsti,", "-dullst, pag. -dullu", "dullt"), //apdullt
		FirstConjRule.direct("-dulstu, -dulsti,", "-dulst, pag. -dullu", "dult"), //apdult
		FirstConjRule.direct("-kurlstu, -kurlsti,", "-kurlst, pag. -kurlu", "kurlt"), //apkurlt
		FirstConjRule.direct("-saustu, -sausti,", "-saust, pag. -sausu", "saust"), //apsaust
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
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Galotņu šabloni.
		RegularVerbRule.secondConjDir("-āju, -ā,", "-ā, pag. -āju", "āt"), //aijāt, aizkābāt
		RegularVerbRule.secondConjDir("-ēju, -ē,", "-ē, pag. -ēju", "ēt"), //abonēt, adsorbēt
		RegularVerbRule.secondConjDir("-ēju, -ē,", "-ē; pag. -ēju", "ēt"), //dulburēt
		RegularVerbRule.secondConjDir("-īju, -ī,", "-ī, pag. -īju", "īt"), //apšķibīt, aizdzirkstīt
		RegularVerbRule.secondConjDir("-oju, -o,", "-o, pag. -oju", "ot"), //aizalvot, aizbangot
		RegularVerbRule.secondConjDir("-oju, -o,", "-o; pag. -oju", "ot"), //ielāgot

		BaseRule.secondConjDirAllPers(
				"-ēju, -ē, -ē, -ējam, -ējat, pag. -ēju, -ējām, -ējāt; pav. -ē, -ējiet", "ēt"), //adverbializēt, anamorfēt
		BaseRule.secondConjDirAllPers(
				"-oju, -o, -o, -ojam, -ojat, pag. -oju; -ojām, -ojāt; pav. -o, -ojiet", "ot"), //acot

		// Darbības vārdu specifiskie likumi.
		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// Paralēlās formas.
		// Īpašā piezīme par glumēšanu: 2. konjugāciju nosaka 3. personas
		// galotne "-ē" - 3. konjugācijai būtu bez.
		RegularVerbRule.secondConjDir3PersParallel(
				"-ē, pag. -ēja (retāk -gluma, 1. konj.)", "glumēt"), //aizglumēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {
		// Visām personām.
		RegularVerbRule.thirdConjDir("-u, -i,", "-a, pag. -īju", "īt", false), //aizsūtīt
		RegularVerbRule.thirdConjDir("-u, -i,", "-a; pag. -īju", "īt", false), //apdurstīt
		RegularVerbRule.thirdConjDir("-u, -i,", "-a, pag. -āju", "āt", false), //līcināt
		RegularVerbRule.thirdConjDir("-inu, -ini,", "-ina, pag. -ināju", "ināt", false), //aizsvilināt
		// Tikai trešajai personai.
		RegularVerbRule.thirdConjDir3Pers("-ina, pag. -ināja", "ināt", false), //aizducināt

		// Darbības vārdu specifiskie likumi.
		RegularVerbRule.thirdConjDir("-bildu, -bildi,", "-bild, pag. -bildēju", "bildēt", false), //atbildēt
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
		// A, B
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
		FirstConjRule.refl("-lokos, -locies,", "-lokās, pag. -lakos", "lakties"), //ielakties
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
		FirstConjRule.refl("-salstos, -salsties,", "-salstās, pag. -salos", "salties"), //izsalties
		FirstConjRule.refl("-sēcos, -sēcies,", "-sēcas, pag. -sēcos", "sēkties"), //iesēkties
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
		FirstConjRule.refl("-tvīkstos, -tvīksties,", "-tvīkstas, pag. -tvīkos", "tvīkties"), //iztvīkties
		// U, V
		FirstConjRule.refl("-vārgstos, -vārgsties,", "-vārgstas, pag. -vārgos", "vārgties"), //izvārgties
		FirstConjRule.refl("-vemjos, -vemies,", "-vemjas, pag. -vēmos", "vemties"), //apvemties
		FirstConjRule.refl("-vēžos, -vēzies,", "-vēžas, pag. -vēžos", "vēzties"), //atvēzties
		// Z
		FirstConjRule.refl("-zīstos, -zīsties,", "-zīstas, pag. -zinos", "zīties"), //atzīties
		FirstConjRule.refl("-zviedzos, -zviedzies,", "-zviedzas, pag. -zviedzos", "zviegties"), //aizzviegties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// A, B, C, D
		FirstConjRule.refl3Pers("-dūcas, pag. -dūcās", "dūkties"), //aizdūkties
		// E, F, G, H, I, J, K
		FirstConjRule.refl3Pers("-kaucas, pag. -kaucās", "kaukties"), //aizkaukties
		// L, M, N, O, P, R, S, Š
		FirstConjRule.refl3Pers("-šalcas, pag. -šalcās", "šalkties"), //aizšalkties
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
		RegularVerbRule.secondConjRefl("-ojos, -ojies,", "-ojas, pag. -ojos", "oties"), //aiztuntuļoties, apgrēkoties
		RegularVerbRule.secondConjRefl("-ējos, -ējies,", "-ējas, pag. -ējos", "ēties"), //abstrahēties
		RegularVerbRule.secondConjRefl("-ājos, -ājies,", "-ājas, pag. -ājos", "āties"), //aizdomāties
		RegularVerbRule.secondConjRefl("-ījos, -ījies,", "-ījas, pag. -ījos", "īties"), //atpestīties

		RegularVerbRule.secondConjRefl3Pers("-ējas, pag. -ējās", "ēties"), //absorbēties
		RegularVerbRule.secondConjRefl3Pers("-ojas, pag. -ojās", "oties"), //daudzkāršoties

		BaseRule.secondConjReflAllPers(
				"-ējos, -ējies, -ējas, -ējamies, -ējaties, pag. -ējos, -ējāmies, -ējāties; pav. -ējies, -ējieties",
				"ēties"), //adverbiēties
		BaseRule.of(
				"parasti dsk., -ējamies, -ējaties, -ējas (3. pers. arī vsk.), pag. -ējāmies",
				".*ēties", 19,
				new Tuple[] {Features.POS__VERB},
				new Tuple[]{Features.USUALLY_USED__PLURAL, Features.USUALLY_USED__THIRD_PERS,
							Tuple.of(Keys.USUALLY_USED_IN_FORM, Values.PLURAL_OR_THIRD_PERS.s)}), //konstituēties
			//parasti dsk., -ējamies, -ējaties, -ējas (3. pers. arī vsk.), pag. -ējāmies
		// Darbības vārdu specifiskie likumi.
		// Nav.
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
	 */
	public static final Rule[] reflThirdConjVerb = {
		// Galotņu šabloni.
		RegularVerbRule.thirdConjRefl(
				"-os, -ies,", "-as, pag. -ējos", "ēties", false), //apkaunēties, aizņaudēties
		RegularVerbRule.thirdConjRefl(
				"-inos, -inies,", "-inās, pag. -inājos", "ināties", false), //apklaušināties
		RegularVerbRule.thirdConjRefl(
				"-os, -ies,", "-ās, pag. -ījos", "īties", false), //apklausīties

		RegularVerbRule.thirdConjRefl3Pers("-as, pag. -ējās", "ēties", false), //aizčiepstēties
		RegularVerbRule.thirdConjRefl3Pers("-inās, pag. -inājās", "ināties", false), //aizbubināties
		RegularVerbRule.thirdConjRefl3Pers("-ās, pag. -ījās", "īties", false), //aizbīdīties

		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		// Likumi, kam ir visu formu variants.
		// A, B
		RegularVerbRule.thirdConjRefl(
				"-burkšķos, -burkšķies,", "-burkšķas, pag. -burkšķējos", "burkšķēties", false), //ieburkšķēties
		// C, Č
		RegularVerbRule.thirdConjRefl(
				"-čerkstos, -čerksties,", "-čerkstas, pag. -čerkstējos", "čerkstēties", false), //iečerkstēties
		RegularVerbRule.thirdConjRefl(
				"-čērkstos, -čērksties,", "-čērkstas, pag. -čērkstējos", "čērkstēties", false), //iečērkstēties
		RegularVerbRule.thirdConjRefl(
				"-čiepstos, -čiepsties,", "-čiepstas, pag. -čiepstējos", "čiepstēties", false), //iečiepstēties
		RegularVerbRule.thirdConjRefl(
				"-činkstos, -činksties,", "-činkstas, pag. -činkstējos", "činkstēties", false), //iečinkstēties
		RegularVerbRule.thirdConjRefl(
				"-čīkstos, -čīksties,", "-čīkstas, pag. -čīkstējos", "čīkstēties", false), //iečīkstēties
		// D
		RegularVerbRule.thirdConjRefl(
				"-dienos, -dienies,", "-dienas, pag. -dienējos", "dienēties", false), //izdienēties
		RegularVerbRule.thirdConjRefl(
				"-dirnos, -dirnies,", "-dirnas, pag. -dirnējos", "dirnēties", false), //izdirnēties
		RegularVerbRule.thirdConjRefl(
				"-draudos, -draudies,", "-draudas, pag. -draudējos", "draudēties", false), //izdraudēties
		RegularVerbRule.thirdConjRefl(
				"-drebos, -drebies,", "-drebas, pag. -drebējos", "drebēties", false), //iedrebēties
		RegularVerbRule.thirdConjRefl(
				"-drīkstos, -drīksties,", "-drīkstas, pag. -drīkstējos", "drīkstēties", false), //iedrīkstēties
		RegularVerbRule.thirdConjRefl(
				"-dusos, -dusies,", "-dusas, pag. -dusējos", "dusēties", false), //atdusēties
		RegularVerbRule.thirdConjRefl(
				"-dziedos, -dziedies,", "-dziedas, pag. -dziedājos", "dziedāties", false), //aizdziedāties
		// E, F, G
		RegularVerbRule.thirdConjRefl(
				"-glūnos, -glūnies,", "-glūnas, pag. -glūnējos", "glūnēties", false), //izglūnēties
		RegularVerbRule.thirdConjRefl(
				"-guļos, -gulies,", "-guļas, pag. -gulējos", "gulēties", true), //aizgulēties
		// H, I, J, K
		RegularVerbRule.thirdConjRefl(
				"-knukstos, -knuksties,", "-knukstas, pag. -knukstējos", "knukstēties", false), //ieknukstēties
		RegularVerbRule.thirdConjRefl(
				"-krekstos, -kreksties,", "-krekstas, pag. -krekstējos", "krekstēties", false), //atkrekstēties
		RegularVerbRule.thirdConjRefl(
				"-krekšos, -krekšies,", "-krekšas, pag. -krekšējos", "krekšēties", false), //iekrekšēties
		RegularVerbRule.thirdConjRefl(
				"-krekšķos, -krekšķies,", "-krekšķas, pag. -krekšķējos", "krekšķēties", false), //atkrekšķēties
		RegularVerbRule.thirdConjRefl(
				"-kunkstos, -kunksties,", "-kunkstas, pag. -kunkstējos", "kunkstēties", false), //iekunkstēties
		RegularVerbRule.thirdConjRefl(
				"-kurnos, -kurnies,", "-kurnas, pag. -kurnējos", "kurnēties", false), //iekurnēties
		// L, M
		RegularVerbRule.thirdConjRefl(
				"-līdzos, -līdzies,", "-līdzas, pag. -līdzējos", "līdzēties", false), //izlīdzēties
		RegularVerbRule.thirdConjRefl(
				"-palīdzos, -palīdzies,", "-palīdzas, pag. -palīdzējos", "palīdzēties", false), //izpalīdzēties
		RegularVerbRule.thirdConjRefl(
				"-murkšos, -murkšies,", "-murkšas, pag. -murkšējos", "murkšēties", false), //iemurkšēties
		RegularVerbRule.thirdConjRefl(
				"-murkšķos, -murkšķies,", "-murkšķas, pag. -murkšķējos", "murkšķēties", false), //iemurkšķēties
		// N, Ņ
		RegularVerbRule.thirdConjRefl(
				"-ņaudos, -ņaudies,", "-ņaudas, pag. -ņaudējos", "ņaudēties", false), //izņaudēties
		RegularVerbRule.thirdConjRefl(
				"-ņerkstos, -ņerksties,", "-ņerkstas, pag. -ņerkstējos", "ņerkstēties", false), //ieņerkstēties
		RegularVerbRule.thirdConjRefl(
				"-ņurdos, -ņurdies,", "-ņurdas, pag. -ņurdējos", "ņurdēties", false), //ieņurdēties
		// O, P
		RegularVerbRule.thirdConjRefl(
				"-pīkstos, -pīksties,", "-pīkstas, pag. -pīkstējos", "pīkstēties", false), //iepīkstēties
		RegularVerbRule.thirdConjRefl(
				"-pinkšos, -pinkšies,", "-pinkšas, pag. -pinkšējos", "pinkšēties", false), //iepinkšēties
		RegularVerbRule.thirdConjRefl(
				"-pinkšķos, -pinkšķies,", "-pinkšķas, pag. -pinkšķējos", "pinkšķēties", false), //iepinkšķēties
		RegularVerbRule.thirdConjRefl(
				"-pukstos, -puksties,", "-pukstas, pag. -pukstējos", "pukstēties", false), //iepukstēties
		// R
		RegularVerbRule.thirdConjRefl(
				"-raudos, -raudies,", "-raudas, pag. -raudājos", "raudāties", false), //aizraudāties
		// S
		RegularVerbRule.thirdConjRefl(
				"-sēžos, -sēdies,", "-sēžas, pag. -sēdējos", "sēdēties", true), //aizsēdēties
		RegularVerbRule.thirdConjRefl(
				"-smīnos, -smīnies,", "-smīnas, pag. -smīnējos", "smīnēties", false), //iesmīnēties
		RegularVerbRule.thirdConjRefl(
				"-spurkšos, -spurkšies,", "-spurkšas, pag. -spurkšējos", "spurkšēties", false), //iespurkšēties
		RegularVerbRule.thirdConjRefl(
				"-spurkšķos, -spurkšķies,", "-spurkšķas, pag. -spurkšķējos", "spurkšķēties", false), //iespurkšķēties
		RegularVerbRule.thirdConjRefl(
				"-stāvos, -stāvies,", "-stāvas, pag. -stāvējos", "stāvēties", false), //izstāvēties
		RegularVerbRule.thirdConjRefl(
				"-stenos, -stenies,", "-stenas, pag. -stenējos", "stenēties", false), //iestenēties
		RegularVerbRule.thirdConjRefl(
				"-svinos, -svinies,", "-svinas, pag. -svinējos", "svinēties", false), //aizsvinēties
		// Š
		RegularVerbRule.thirdConjRefl(
				"-šļupstos, -šļupsties,", "-šļupstas, pag. -šļupstējos", "šļupstēties", false), //iešļupstēties
		RegularVerbRule.thirdConjRefl(
				"-šņukstos, -šņuksties,", "-šņukstas, pag. -šņukstējos", "šņukstēties", false), //aizšņukstēties
		// T
		RegularVerbRule.thirdConjRefl(
				"-tarkšķos, -tarkšķies,", "-tarkšķas, pag. -tarkšķējos", "tarkšķēties", false), //ietarkšķēties
		RegularVerbRule.thirdConjRefl(
				"-trīcos, -trīcies,", "-trīcas, pag. -trīcējos", "trīcēties", false), //ietrīcēties
		RegularVerbRule.thirdConjRefl(
				"-trīsos, -trīsies,", "-trīsas, pag. -trīsējos", "trīsēties", false), //ietrīsēties
		// U, V
		RegularVerbRule.thirdConjRefl(
				"-vaidos, -vaidies,", "-vaidas, pag. -vaidējos", "vaidēties", false), //ievaidēties
		// Z

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// A, B
		RegularVerbRule.thirdConjRefl3Pers(
				"-brikšķas, pag. -brikšķējās", "brikšķēties", false), //aizbrikšķēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-brikšas, pag. -brikšējās", "brikšēties", false), //aizbrikšēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-brīkšķas, pag. -brīkšķējās", "brīkšķēties", false), //aizbrīkšķēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-brīkšas, pag. -brīkšējās", "brīkšēties", false), //aizbrīkšēties
		// C, Č
		RegularVerbRule.thirdConjRefl3Pers(
				"-čabas, pag. -čabējās", "čabēties", false), //aizčabēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-čaukstas, pag. -čaukstējās", "čaukstēties", false), //aizčaukstēties
		// D
		RegularVerbRule.thirdConjRefl3Pers(
				"-dārdas, pag. -dārdējās", "dārdēties", false), //aizdārdēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-drebas, pag. -drebējās", "drebēties", false), //aizdrebēties
		// E, F, G
		RegularVerbRule.thirdConjRefl3Pers(
				"-grābās, pag. -grābējās", "grabēties", false), //aizgrabēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-gurkstas, pag. -gurkstējās", "gurkstēties", false), //aizgurkstēties
		// H, I, J, K
		RegularVerbRule.thirdConjRefl3Pers(
				"-klabas, pag. -klabējās", "klabēties", false), //aizklabēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-klaudzas, pag. -klaudzējās", "klaudzēties", false), //aizklaudzēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-klukstas, pag. -klukstējās", "klukstēties", false), //aizklukstēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-klunkšas, pag. -klunkšējās", "klunkšēties", false), //aizklunkšēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-klunkšķas, pag. -klunkšķējās", "klunkšķēties", false), //aizklunkšķēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-knakstās, pag. -knakstējās", "knakstēties", false), //aizknakstēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-knakšas, pag. -knakšējās", "knakšēties", false), //aizknakšēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-knakšķas, pag. -knakšķējās", "knakšķēties", false), //aizknakšķēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-knaukšas, pag. -knaukšējās", "knaukšēties", false), //aizknaukšēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-knaukšķas, pag. -knaukšķējās", "knaukšķēties", false), //aizknaukšķēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-knikšas, pag. -knikšējās", "knikšēties", false), //aizknikšēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-knikšķas, pag. -knikšķējās", "knikšķēties", false), //aizknikšķēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-krakstas, pag. -krakstējās", "krakstēties", false), //aizkrakstēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-krakšķas, pag. -krakšķējās", "krakšķēties", false), //aizkrakšķēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-kurkstas, pag. -kurkstējās", "kurkstēties", false), //aizkurkstēties
		RegularVerbRule.thirdConjRefl3Pers(
				"-kurkšķas, pag. -kurkšķējās", "kurkšķēties", false), //aizkurkšķēties
		// L, M
		RegularVerbRule.thirdConjRefl3Pers(
				"-mirdzas, pag. -mirdzējās", "mirdzēties", false) //aizmirdzēties
		// N, O, P, R, S, T, U, V, Z
	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] reflMultiConjVerb = {
			// Galotņu šabloni.
			BaseRule.secondThirdConjReflAllPersParallel(
					"-os, -ies, -as, arī -ējos, -ējies, -ējas, pag. -ējos", ".*ēties", false), // mīlēties
			BaseRule.secondThirdConjReflAllPersParallel(
					"-ījos, -ījies, -ījas, arī -os, -ies, -ās, pag. -ījos", ".*īties", false), // blēdīties

			// Darbības vārdu specifiskie likumi.
			// Nav.
	};
}
