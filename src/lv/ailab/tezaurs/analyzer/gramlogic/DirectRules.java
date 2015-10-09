package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.utils.Trio;
import lv.ailab.tezaurs.utils.Tuple;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * Gram.processBeginingWithPatterns(String, String)
 * Likumi kas jālieto ar Rule.applyDirect().
 * Šobrīd šeit ir formas ziņā vienkāršākie likumi, vēl daži ir
 * Gram.processBeginingWithPatterns()
 * @author Lauma
 */
public class DirectRules
{
	/**
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final Rule[] other = {
		// Nedefinēta paradigma: Atgriezeniskie lietvārdi -šanās
		SimpleRule.of("ģen. -ās, akuz. -os, instr. -os, dsk. -ās, ģen. -os, akuz. -ās, s.", ".*šanās", 0,
				new Tuple[] {Features.POS__NOUN, Features.POS__REFL_NOUN},
				new Tuple[] {Features.GENDER__FEM}), //aizbildināšanās
		// Paradigmas: 7, 8 - kopdzimtes lietvārdi, galotne -a
		ComplexRule.of("ģen. -as, v. dat. -am, s. dat. -ai, kopdz.", new Trio[] {
					Trio.of(".*a", new Integer[] {7, 8}, new Tuple[] {Features.POS__NOUN})},
				new Tuple[]{Tuple.of(Keys.GENDER, Values.COGENDER.s)}), // aitasgalva, aizmārša

		// Paradigmas: 1, 2 - 1. deklinācija
		// Šeit varētu vēlāk vajadzēt likumus paplašināt, ja parādās jauni šķirkļi.
		SimpleRule.of("lietv. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
				new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN}), // aerobs
		SimpleRule.of("vsk. -a, v.", ".*[^aeiouāēīōū]s", 1,
				new Tuple[]{Features.POS__NOUN},
				new Tuple[]{Features.GENDER__MASC, Tuple.of(Keys.NUMBER, Values.SINGULAR.s)}), // acteks

		// Paradigmas: 7, 11
		ComplexRule.of("-as, s.", new Trio[] {
					Trio.of(".*a", new Integer[] {7}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*[^aeiouāēīōū]as", new Integer[] {7}, new Tuple[] {
							Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {11}, new Tuple[] {
							Features.POS__NOUN})},
				new Tuple[] {Features.GENDER__FEM}), // aberācija, milns, najādas

		// Paradigmas: 9, 11
		ComplexRule.of("dsk. ģen. -ņu, s.", new Trio[] {
					Trio.of(".*ne", new Integer[] {9}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*nes", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {11}, new Tuple[] {Features.POS__NOUN})},
				new Tuple[] {Features.GENDER__FEM}), // ādmine, bākuguns, bārkšsaknes

		// Paradigma: 11 - 6. dekl.
		SimpleRule.of("-ts, -šu", ".*ts", 11,
				new Tuple[] {Features.POS__NOUN}, new Tuple[] {Features.GENDER__FEM}), //abonentpults
		SimpleRule.of("-vs, -vju", ".*vs", 11,
				new Tuple[] {Features.POS__NOUN}, new Tuple[] {Features.GENDER__FEM}), //adatzivs

		SimpleRule.of("-žu, v.", ".*ļaudis", 11,
				new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL, Features.USED_ONLY__PLURAL},
				new Tuple[] {Features.GENDER__MASC}), //ļaudis

		// Paradigmas: 13, 14 - īpašības vārdi daudzskaitlī
		ComplexRule.of("s. -as; adj.", new Trio[] {
					Trio.of(".*i", new Integer[] {13, 14}, new Tuple[] {Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM})},
				new Tuple[] {Features.POS__ADJ}), // abēji 2
		ComplexRule.of("s. -as; tikai dsk.", new Trio[] {
					Trio.of(".*i", new Integer[] {13, 14}, new Tuple[] {Features.POS__ADJ, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM})},
				new Tuple[] {Features.USED_ONLY__PLURAL}), // abēji 1

		// Paradigma: 25 - vietniekvārdi
		SimpleRule.of("s. -as; vietniekv.", ".*i", 25,
				new Tuple[] {Features.ENTRYWORD__PLURAL},
				new Tuple[] {Features.POS__PRONOUN}), //abi

		// Paradigma: 30 - jaundzimušais, pēdējais
		SimpleRule.of("-šā, v. -šās, s.", ".*ušais", 30,
				new Tuple[] {Features.POS__ADJ, Features.POS__NOUN},
				null), //iereibušais	//TODO vai te vajag alternatīvo lemmu?
		SimpleRule.of("-ā, v.", ".*ais", 30,
				new Tuple[] {Features.POS__ADJ, Features.POS__NOUN},
				new Tuple[] {Features.GENDER__MASC}), //pirmdzimtais

		// Paradigmas: 30 -  jaundzimušais, pēdējais
		// Nedefinēta paradigma: Atgriezeniskie lietvārdi -šanās
		ComplexRule.of("-ās, s.", new Trio[] {
					Trio.of(".*šanās", new Integer[] {0}, new Tuple[] {Features.POS__REFL_NOUN, Features.POS__NOUN}),
					Trio.of(".*ā", new Integer[] {30}, new Tuple[] {Features.POS__ADJ, Features.POS__NOUN})},
				new Tuple[] {Features.GENDER__FEM}), // pirmdzimtā, -šanās

		// Paradigmas: 22, 30
		ComplexRule.of("s. -ā", new Trio[] {
					Trio.of(".*ais", new Integer[] {22, 30}, new Tuple[] {Features.POS__ADJ, Features.POS__NUMERAL, Features.UNCLEAR_PARADIGM})},
				null) // agrākais, pirmais	//TODO vai te vajag alternatīvo lemmu?
	};

	/**
	 * Paradigm 9: Lietvārds 5. deklinācija -e
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] fifthDeclNoun = {
		SimpleRule.fifthDeclStd("-ķe, -es, dsk. ģen. -ķu", ".*ķe"), //ciniķe

		// Standartizētie
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ču", ".*[cč]e"), //ābece, veče
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ģu", ".*[ģ]e"), //aeroloģe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ju", ".*je"), //baskāje
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ķu", ".*[ķ]e"), //agnostiķe, leduspuķe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ļu", ".*[l]e"), //ābele
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ņu", ".*[n]e"), //ābolaine
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ru", ".*re"), //administratore, ādere
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -šu", ".*[sšt]e"), //abate, adrese, larkše, apokalipse, note
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -žu", ".*[dz]e"), //ābolmaize, aģitbrigāde, bilde, pirolīze

		SimpleRule.fifthDeclStd("-es, dsk. ģen. -bju", ".*be"), //apdobe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -džu", ".*dze"), //kāršaudze
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -kšu", ".*kte"), //daudzpunkte
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ļļu", ".*lle"), //zaļumballe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ļņu", ".*lne"), //nokalne
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -mju", ".*me"), //agronome
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -smju", ".*sme"), //noslieksme
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -stu", ".*ste"), //abolicioniste
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -šķu", ".*šķe"), //draišķe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -šļu", ".*sle"), //gaisagrābsle
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -šņu", ".*sne"), //aizkrāsne
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -vju", ".*ve"), //agave
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -vju", ".*ve"), //aizstāve
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -žņu", ".*zne"), //asteszvaigzne

		// Vienskaitlis + daudzskaitlis
		ComplexRule.of("-es, dsk. ģen. -pju", new Trio[]{
					Trio.of(".*pe", new Integer[]{9}, new Tuple[]{Features.POS__NOUN, Features.GENDER__FEM}),
					Trio.of(".*pes", new Integer[]{9}, new Tuple[]{Features.POS__NOUN, Features.ENTRYWORD__PLURAL, Features.GENDER__FEM})},
				null), // aitkope, antilope, tūsklapes

		// Nejauki, pārāk specifiski gdījumi
		// Šiem defišu izņemšanas mehānisms īsti neder, jo vienā vietā vajag atstāt.
		SimpleRule.fifthDeclStd("-es, dsk. ģen. aļģu", ".*aļģe"), //aļģe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. āru", ".*āre"), //āre
		SimpleRule.fifthDeclStd("-es, dsk. ģen. biržu", ".*birze"), //birze
		SimpleRule.fifthDeclStd("-es, dsk. ģen. pūšu", ".*pūte"), //pūte 1, 3
		SimpleRule.fifthDeclStd("-es, dsk. ģen. puvju", ".*puve"), //puve
		SimpleRule.fifthDeclStd("-es, dsk. ģen. šaļļu", ".*šalle"), //šalle
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -upju", ".*upe"), //dzirnavupe

		// Nestandartīgie
		SimpleRule.of("-es, s., dsk. ģen. -bju", ".*be", 9,
				new Tuple[]{Features.POS__NOUN}, new Tuple[]{Features.GENDER__FEM}), //acetilsalicilskābe
		SimpleRule.of("-es, s. dsk. -es, -bju", ".*be", 9,
				new Tuple[]{Features.POS__NOUN}, new Tuple[]{Features.GENDER__FEM}), //astilbe

		SimpleRule.of("-ļu, s.", ".*les", 9,
				new Tuple[]{Features.POS__NOUN, Features.ENTRYWORD__PLURAL},
				new Tuple[]{Features.GENDER__FEM}), //bailes

		// Miju varianti
		SimpleRule.of("-es, dsk. ģen. -stu, arī -šu", ".*ste", 9,
				new Tuple[]{Features.POS__NOUN, Features.GENDER__FEM,
						Tuple.of(Keys.INFLECTION_WEARDNES, "Miju varianti: -stu/-šu")},
				null), //dzeņaukste

		// Bez mijām
		SimpleRule.of("-es, dsk. ģen. -du", ".*de", 9,
				new  Tuple[] {Features.POS__NOUN, Features.GENDER__FEM, Features.NO_SOUNDCHANGE},
				null), // diplomande
		SimpleRule.of("-es, dsk. ģen. -fu", ".*fe", 9,
				new  Tuple[] {Features.POS__NOUN, Features.GENDER__FEM, Features.NO_SOUNDCHANGE},
				null), //arheogrāfe
		SimpleRule.of("-es, dsk. ģen. mufu", ".*mufe", 9,
				new Tuple[] {Features.POS__NOUN, Features.GENDER__FEM, Features.NO_SOUNDCHANGE},
				null), //mufe
		SimpleRule.of("-es, dsk. ģen. -pu", ".*pe", 9,
				new Tuple[] {Features.POS__NOUN, Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
				null), // filantrope
		SimpleRule.of("-es, dsk. ģen. -su", ".*se", 9,
				new  Tuple[] {Features.POS__NOUN, Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
				null), // bise
		SimpleRule.of("-es, dsk. ģen. -tu", ".*te", 9,
				new  Tuple[] {Features.POS__NOUN, Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
				null), // antisemīte
		SimpleRule.of("-es, dsk. ģen. -zu", ".*ze", 9,
				new  Tuple[] {Features.POS__NOUN, Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
				null), // autobāze
	};

	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final Rule[] secondDeclNounRulesDirect = {
		SimpleRule.secondDeclStd("-ņa, dsk. ģen. -ņu, v.", ".*nis"), //bizmanis
		SimpleRule.secondDeclStd("-bja, v.", ".*bis"), //aizsargdambis
		SimpleRule.secondDeclStd("-dža, v.", ".*dzis"), //algādzis
		SimpleRule.secondDeclStd("-ķa, v.", ".*[kķ]is"), //agnostiķis
		SimpleRule.secondDeclStd("-pja, v.", ".*pis"), //aitkopis
		SimpleRule.secondDeclStd("-vja, v.", ".*vis"), //aizstāvis
		SimpleRule.secondDeclStd("-žņa, v.", ".*znis"), //aitkopis
		SimpleRule.secondDeclStd("-ža, v.", ".*[dzž]is"), //ādgrauzis
	};

	/**
	 * Īsie šabloni ar vienu galotni un dzimti.
	 * Visiem likumiem ir iespējami vairāki galotņu varianti, bet uzskaitīti
	 * ir tikai vārdnīcā sastaptie.
	 */
	public static final Rule[] nounMultiDecl = {
		// Vienskaitlis, vīriešu dzimte
		// Ar mijām
		ComplexRule.of("-ļa, v.", new Trio[] {
					Trio.of(".*lis", new Integer[] {3}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*ls", new Integer[] {5}, new Tuple[] {Features.POS__NOUN})},
				new Tuple[]{Features.GENDER__MASC}), // acumirklis, bacils, durkls
		ComplexRule.of("-ņa, v.", new Trio[] {
					Trio.of(".*ņš", new Integer[] {2}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*nis", new Integer[] {3}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*suns", new Integer[] {5}, new Tuple[] {Features.POS__NOUN})},
				new Tuple[]{Features.GENDER__MASC}), // abesīnis, dižtauriņš, dzinējsuns
		ComplexRule.of("-ša, v.", new Trio[] {
					Trio.of(".*[sšt]is", new Integer[] {3}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*ss", new Integer[] {5}, new Tuple[] {Features.POS__NOUN})},
				new Tuple[]{Features.GENDER__MASC}), // abrkasis, lemess
		// Bez mijām
		ComplexRule.of("-ra, v.", new Trio[] {
					Trio.of(".*rs", new Integer[] {1}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*ris", new Integer[] {3}, new Tuple[] {Features.POS__NOUN})},
				new Tuple[]{Features.GENDER__MASC}), // airis, mūrniekmeistars
		ComplexRule.of("-sa, v.", new Trio[] {
					Trio.of(".*ss", new Integer[] {1}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*sis", new Integer[] {3}, new Tuple[] {Features.POS__NOUN, Features.NO_SOUNDCHANGE})},
				new Tuple[]{Features.GENDER__MASC}), // balanss, kūrviesis
		ComplexRule.of("-ta, v.", new Trio[] {
					Trio.of(".*tis", new Integer[] {3}, new Tuple[] {Features.POS__NOUN, Features.NO_SOUNDCHANGE})},
				new Tuple[]{Features.GENDER__MASC}), // stereotālskatis
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.of("-a, v.", new Trio[] {
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {1}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*š", new Integer[] {2}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*[ģjķr]is", new Integer[] {3}, new Tuple[] {Features.POS__NOUN})},
				new Tuple[]{Features.GENDER__MASC}), // abats, akustiķis//, sparguļi, skostiņi

		// Daudzkaitlis, vīriešu dzimte
		// Ar mijām
		ComplexRule.of("-ņu, v.", new Trio[] {
					Trio.of(".*ņi", new Integer[] {1, 2, 3, 4, 5}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),},
				new Tuple[]{Features.GENDER__MASC}), // bretoņi
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.of("-u, v.", new Trio[] {
					Trio.of(".*(otāji|umi|anti|nieki|[aeiouāēīōū]īdi|isti|mēsli|svārki|plūdi|rati|vecāki|bērni|raksti|vidi|rīti|vakari|vārdi|kapi|augi|svētki|audi|laiki|putni|svari)",
							new Integer[] {1}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*[bcdghklmnpstvz]i", new Integer[] {1}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*(ieši|āņi|ēži|grieži|stāvji|grauži|brunči|viļņi|ceļi|liberāļi|krampji|kaļķi)",
							new Integer[] {3}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*suņi", new Integer[] {5}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*ši", new Integer[] {1, 3, 4}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
					Trio.of(".*[čļņž]i", new Integer[] {2, 3, 4}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
					Trio.of(".*(ģ|[mv]j)i", new Integer[] {3, 4}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
					Trio.of(".*([ķr]|[aeiāē]j)i", new Integer[] {1, 2, 3, 4}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
				},
				new Tuple[]{Features.GENDER__MASC}),
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
		ComplexRule.of("-ņu, s.", new Trio[] {
					Trio.of(".*ne", new Integer[] {9}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*ņas", new Integer[] {7}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*nes", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*nis", new Integer[] {11}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
				},
				new Tuple[]{Features.GENDER__FEM}), // acenes, iemaņas, balodene, robežugunis
		ComplexRule.of("-šu, s.", new Trio[] {
					Trio.of(".*te", new Integer[] {9}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*šas", new Integer[] {7}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*[st]es", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*tis", new Integer[] {11}, new Tuple[] {Features.POS__NOUN,	Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // ahajiete, aizkulises, autosacīkstes, klaušas, šķūtis
		ComplexRule.of("-žu, s.", new Trio[] {
					Trio.of(".*žas", new Integer[] {7}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*[dz]es", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // mirādes, graizes, bažas
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.of("-u, s.", new Trio[] {
					Trio.of(".*a", new Integer[] {7}, new Tuple[] {Features.POS__NOUN}),
					Trio.of(".*[^aeiouāēīōū]as", new Integer[] {7}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*ķes", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // aijas, spēķes, zeķes, konkrēcija


};

	/**
	 * Likumi, kas ir citu likumu prefiksi.
	 * Šajā masīvā jāievēro likumu secība, citādi slikti būs. Šo masīvu jālieto
	 * pašu pēdējo.
	 */
	public static final Rule[] dangerous = {
		// Paradigma: 9 - Lietvārds 5. deklinācija -e siev. dz.
		SimpleRule.fifthDeclStd("-es, s.", ".*e"), //aizture + daudzi piemēri ar mijām
			// konflikts ar "astilbe" un "acetilsalicilskābe"

		// Paradigma: 3 - Lietvārds 2. deklinācija -is
		SimpleRule.of("-ņa, dsk. ģen. -ņu", ".*ņi", 3,
				new Tuple[]{Features.POS__NOUN, Features.ENTRYWORD__PLURAL},
				new Tuple[]{Features.GENDER__MASC}), //afroamerikāņi
			// konflikts ar "bizmanis"

		// Vissliktākie šabloni - satur tikai vienu galotni un neko citu.
		// Paradigmas: 9, 7 - vienskaitlī un daudzskaitlī
		ComplexRule.of("-žu", new Trio[] {
					Trio.of(".*[dz]e", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.GENDER__FEM}),
					Trio.of(".*[dz]es", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.GENDER__FEM, Features.ENTRYWORD__PLURAL})},
				null), // abioģenēze, ablumozes, akolāde, nematodes
		ComplexRule.of("-ņu", new Trio[] {
					Trio.of(".*ne", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.GENDER__FEM}),
					Trio.of(".*nes", new Integer[] {9}, new Tuple[] {Features.POS__NOUN, Features.GENDER__FEM, Features.ENTRYWORD__PLURAL}),
					Trio.of(".*ņas", new Integer[] {7}, new Tuple[] {Features.POS__NOUN, Features.GENDER__FEM, Features.ENTRYWORD__PLURAL})},
				null), // agrene, aizlaidnes
		// Paradigma: 3
		SimpleRule.of("-ņa", ".*nis", 3,
				new Tuple[]{Features.POS__NOUN, Features.GENDER__FEM}, null), // abolainis
	};
	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final Rule[] directFirstConjVerb = {
		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		VerbRule.firstConjDir("-dullstu, -dullsti,", "-dullst; pag. -dullu", "dullt"), //apdullt
		VerbRule.firstConjDir("-dulstu, -dulsti,", "-dulst, pag. -dullu", "dult"), //apdult
		VerbRule.firstConjDir("-kurlstu, -kurlsti,", "-kurlst, pag. -kurlu", "kurlt"), //apkurlt
		VerbRule.firstConjDir("-saustu, -sausti,", "-saust, pag. -sausu", "saust"), //apsaust
		VerbRule.firstConjDir("-šņurkstu, -šņurksti,", "-šņurkst, pag. -šņurku", "šņurkt"), //apšņurkt
		VerbRule.firstConjDir("-trakstu, -traksti,", "-trakst, pag. -traku", "trakt"), //aptrakt
		VerbRule.firstConjDir("-trulstu, -trulsti,", "-trulst, pag. -trulu", "trult"), //aptrult
		VerbRule.firstConjDir("-vēstu, -vēsti,", "-vēst, pag. -vēsu", "vēst"), //atvēst
		VerbRule.firstConjDir("-žirbstu, -žirbsti,", "-žirbst, pag. -žirbu", "žirbt"), // atžirbt

		SimpleRule.of("-nīku, -nīc, -nīk, retāk -nīkstu, -nīksti, -nīkst, pag. -niku", ".*nikt", 15,
				new Tuple[]{Features.POS__VERB, Tuple.of(Keys.INFLECT_AS, "nikt"),	Features.PARALLEL_FORMS},
				null), //apnikt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Galotņu šabloni.
		VerbRule.secondConjDir("-āju, -ā,", "-ā, pag. -āju", "āt"), //aijāt, aizkābāt
		VerbRule.secondConjDir("-ēju, -ē,", "-ē, pag. -ēju", "ēt"), //abonēt, adsorbēt
		VerbRule.secondConjDir("-ēju, -ē,", "-ē; pag. -ēju", "ēt"), //dulburēt
		VerbRule.secondConjDir("-īju, -ī,", "-ī, pag. -īju", "īt"), //apšķibīt, aizdzirkstīt
		VerbRule.secondConjDir("-oju, -o,", "-o, pag. -oju", "ot"), //aizalvot, aizbangot
		VerbRule.secondConjDir("-oju, -o,", "-o; pag. -oju", "ot"), //ielāgot

		SimpleRule.of("-ēju, -ē, -ē, -ējam, -ējat, pag. -ēju, -ējām, -ējāt; pav. -ē, -ējiet", ".*ēt", 16,
				new Tuple[]{Features.POS__VERB}, null), //adverbializēt, anamorfēt
		SimpleRule.of("-oju, -o, -o, -ojam, -ojat, pag. -oju; -ojām, -ojāt; pav. -o, -ojiet", ".*ot", 16,
				new Tuple[]{Features.POS__VERB}, null), //acot

		// Darbības vārdu specifiskie likumi.
		// Nav.
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {

		VerbRule.thirdConjDir("-u, -i,", "-a, pag. -īju", "īt", false), //aizsūtīt
		VerbRule.thirdConjDir("-u, -i,", "-a; pag. -īju", "īt", false), //apdurstīt
		VerbRule.thirdConjDir("-inu, -ini,", "-ina, pag. -ināju", "ināt", false), //aizsvilināt

		ThirdPersVerbRule.thirdConjDir("-ina, pag. -ināja", "ināt", false), //aizducināt

		// Darbības vārdu specifiskie likumi.
		VerbRule.thirdConjDir("-bildu, -bildi,", "-bild, pag. -bildēju", "bildēt", false), //atbildēt
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Vārdi ar vairāk kā vienu paradigmu. Šie likumi jālieto pirms
	 * atbilstošajiem vienas paradigmas likumiem.
	 */
	public static final Rule[] directMultiConjVerb = {
		// Galotņu šabloni.
		ComplexRule.secondThirdConjDirectAllPers(
				"-īju, -ī, -ī, arī -u, -i, -a, pag. -īju", ".*īt", false), // aprobīt
		ComplexRule.secondThirdConjDirectAllPers(
				"-u, -i, -a, arī -īju, -ī, -ī, pag. -īju", ".*īt", false), // atrotīt

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
		VerbRule.firstConjRefl("-bilstos, -bilsties,", "-bilstas, pag. -bildos", "bilsties"), //iebilsties
		VerbRule.firstConjRefl("-bļaujos, -bļaujies,", "-bļaujas, pag. -bļāvos", "bļauties"), //iebļauties
		VerbRule.firstConjRefl("-brēcos, -brēcies,", "-brēcas, pag. -brēcos", "brēkties"), //aizbrēkties
		// C
		VerbRule.firstConjRefl("-cērpos, -cērpies,", "-cērpas, pag. -cirpos", "cirpties"), //apcirpties
		// D
		VerbRule.firstConjRefl("-degos, -dedzies,", "-degas, pag. -degos", "degties"), //aizdegties
		VerbRule.firstConjRefl("-dīcos, -dīcies,", "-dīcas, pag. -dīcos", "dīkties"), //iedīkties
		VerbRule.firstConjRefl("-dūcos, -dūcies,", "-dūcas, pag. -dūcos", "dūkties"), //iedūkties
		// E
		VerbRule.firstConjRefl("-elšos, -elsies,", "-elšas, pag. -elsos", "elsties"), //aizelsties
		// F, G
		VerbRule.firstConjRefl("-gārdzos, -gārdzies,", "-gārdzas, pag. -gārdzos", "gārgties"), //aizgārgties
		VerbRule.firstConjRefl("-grābjos, -grābies,", "-grābjas, pag. -grābos", "grābties"), //iegrābties
		// Ģ,
		VerbRule.firstConjRefl("-ģiedos, -ģiedies,", "-ģiedas, pag. -ģidos", "ģisties"), //apģisties
		// H, I, J, K
		VerbRule.firstConjRefl("-karstos, -karsties,", "-karstas, pag. -karsos", "karsties"), //iekarsties
		VerbRule.firstConjRefl("-kliedzos, -kliedzies,", "-kliedzas, pag. -kliedzos", "kliegties"), //aizkliegties
		VerbRule.firstConjRefl("-krācos, -krācies,", "-krācas, pag. -krācos", "krākties"), //aizkrākties
		VerbRule.firstConjRefl("-kaucos, -kaucies,", "-kaucas, pag. -kaucos", "kaukties"), //iekaukties
		// Ķ
		VerbRule.firstConjRefl("-ķērcos, -ķērcies,", "-ķērcas, pag. -ķērcos", "ķērkties"), //ieķērkties
		// L
		VerbRule.firstConjRefl("-lokos, -locies,", "-lokās, pag. -lakos", "lakties"), //ielakties
		// M
		VerbRule.firstConjRefl("-mirstos, -mirsties,", "-mirstas, pag. -mirsos", "mirsties"), //aizmirsties
		// N, Ņ
		VerbRule.firstConjRefl("-ņirdzos, -ņirdzies,", "-ņirdzas, pag. -ņirdzos", "ņirgties"), //atņirgties
		// O, P, R
		VerbRule.firstConjRefl("-reibstos, -reibsties,", "-reibstas, pag. -reibos", "reibties"), //iereibties
		VerbRule.firstConjRefl("-rūcos, -rūcies,", "-rūcas, pag. -rūcos", "rūkties"), //aizrūkties
		// S
		VerbRule.firstConjRefl("-snaužos, -snaudies,", "-snaužas, pag. -snaudos", "snausties"), //aizsnausties
		VerbRule.firstConjRefl("-svelpjos, -svelpies,", "-svelpjas, pag. -svelpos", "svelpties"), //aizsvelpties
		VerbRule.firstConjRefl("-svilpjos, -svilpies,", "-svilpjas, pag. -svilpos", "svilpties"), //aizsvilpties
		VerbRule.firstConjRefl("-svilstos, -svilsties,", "-svilstas, pag. -svilos", "svilties"), //aizsvilties
		// Š
		VerbRule.firstConjRefl("-šņācos, -šņācies,", "-šņācas, pag. -šņācos","šņākties"), //aizšņākties
		// T
		VerbRule.firstConjRefl("-topos, -topies,", "-topas, pag. -tapos","tapties"), //attapties
		// U, V
		VerbRule.firstConjRefl("-vemjos, -vemies,", "-vemjas, pag. -vēmos","vemties"), //apvemties
		VerbRule.firstConjRefl("-vēžos, -vēzies,", "-vēžas, pag. -vēžos", "vēzties"), //atvēzties
		// Z
		VerbRule.firstConjRefl("-zīstos, -zīsties,", "-zīstas, pag. -zinos", "zīties"), //atzīties
		VerbRule.firstConjRefl("-zviedzos, -zviedzies,", "-zviedzas, pag. -zviedzos", "zviegties"), //aizzviegties

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// A, B, C, D
		ThirdPersVerbRule.firstConjRefl("-dūcas, pag. -dūcās", "dūkties"), //aizdūkties
		// E, F, G, H, I, J, K
		ThirdPersVerbRule.firstConjRefl("-kaucas, pag. -kaucās", "kaukties"), //aizkaukties
		// L, M, N, O, P, R, S, Š
		ThirdPersVerbRule.firstConjRefl("-šalcas, pag. -šalcās", "šalkties"), //aizšalkties
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
		VerbRule.secondConjRefl("-ojos, -ojies,", "-ojas, pag. -ojos", "oties"), //aiztuntuļoties, apgrēkoties
		VerbRule.secondConjRefl("-ējos, -ējies,", "-ējas, pag. -ējos", "ēties"), //abstrahēties
		VerbRule.secondConjRefl("-ājos, -ājies,", "-ājas, pag. -ājos", "āties"), //aizdomāties
		VerbRule.secondConjRefl("-ījos, -ījies,", "-ījas, pag. -ījos", "īties"), //atpestīties
		VerbRule.secondConjRefl("-inos, -inies,", "-inās, pag. -inājos", "ināties"), //atspirināties

		ThirdPersVerbRule.secondConjRefl("-ējas, pag. -ējās", "ēties"), //absorbēties
		ThirdPersVerbRule.secondConjRefl("-ojas, pag. -ojās", "oties"), //daudzkāršoties

		SimpleRule.of("-ējos, -ējies, -ējas, -ējamies, -ējaties, pag. -ējos, -ējāmies, -ējāties; pav. -ējies, -ējieties",
				".*ēties", 19,
				new Tuple[] {Features.POS__VERB}, null), //adverbiēties
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
		VerbRule.thirdConjRefl("-os, -ies,", "-as, pag. -ējos", "ēties", false), //apkaunēties, aizņaudēties
		VerbRule.thirdConjRefl("-inos, -inies,", "-inās, pag. -inājos", "ināties", false), //apklaušināties
		VerbRule.thirdConjRefl("-os, -ies,", "-ās, pag. -ījos", "īties", false), //apklausīties

		ThirdPersVerbRule.thirdConjRefl("-as, pag. -ējās", "ēties", false), //aizčiepstēties
		ThirdPersVerbRule.thirdConjRefl("-inās, pag. -inājās", "ināties", false), //aizbubināties
		ThirdPersVerbRule.thirdConjRefl("-ās, pag. -ījās", "īties", false), //aizbīdīties

		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		// Likumi, kam ir visu formu variants.
		// A, B
		VerbRule.thirdConjRefl("-burkšķos, -burkšķies,", "-burkšķas, pag. -burkšķējos", "burkšķēties", false), //ieburkšķēties
		// C, Č
		VerbRule.thirdConjRefl("-čerkstos, -čerksties,", "-čerkstas, pag. -čerkstējos", "čerkstēties", false), //iečerkstēties
		VerbRule.thirdConjRefl("-čērkstos, -čērksties,", "-čērkstas, pag. -čērkstējos", "čērkstēties", false), //iečērkstēties
		VerbRule.thirdConjRefl("-čiepstos, -čiepsties,", "-čiepstas, pag. -čiepstējos", "čiepstēties", false), //iečiepstēties
		VerbRule.thirdConjRefl("-činkstos, -činksties,", "-činkstas, pag. -činkstējos", "činkstēties", false), //iečinkstēties
		VerbRule.thirdConjRefl("-čīkstos, -čīksties,", "-čīkstas, pag. -čīkstējos", "čīkstēties", false), //iečīkstēties
		// D
		VerbRule.thirdConjRefl("-drebos, -drebies,", "-drebas, pag. -drebējos", "drebēties", false), //iedrebēties
		VerbRule.thirdConjRefl("-drīkstos, -drīksties,", "-drīkstas, pag. -drīkstējos", "drīkstēties", false), //iedrīkstēties
		VerbRule.thirdConjRefl("-dusos, -dusies,", "-dusas, pag. -dusējos", "dusēties", false), //atdusēties
		VerbRule.thirdConjRefl("-dziedos, -dziedies,", "-dziedas, pag. -dziedājos", "dziedāties", false), //aizdziedāties
		// E, F, G
		VerbRule.thirdConjRefl("-guļos, -gulies,", "-guļas, pag. -gulējos", "gulēties", true), //aizgulēties
		// H, I, J, K
		VerbRule.thirdConjRefl("-knukstos, -knuksties,", "-knukstas, pag. -knukstējos", "knukstēties", false), //ieknukstēties
		VerbRule.thirdConjRefl("-krekstos, -kreksties,", "-krekstas, pag. -krekstējos", "krekstēties", false), //atkrekstēties
		VerbRule.thirdConjRefl("-krekšos, -krekšies,", "-krekšas, pag. -krekšējos", "krekšēties", false), //iekrekšēties
		VerbRule.thirdConjRefl("-krekšķos, -krekšķies,", "-krekšķas, pag. -krekšķējos", "krekšķēties", false), //atkrekšķēties
		VerbRule.thirdConjRefl("-kunkstos, -kunksties,", "-kunkstas, pag. -kunkstējos", "kunkstēties", false), //iekunkstēties
		VerbRule.thirdConjRefl("-kurnos, -kurnies,", "-kurnas, pag. -kurnējos", "kurnēties", false), //iekurnēties
		// L, M
		VerbRule.thirdConjRefl("-murkšos, -murkšies,", "-murkšas, pag. -murkšējos", "murkšēties", false), //iemurkšēties
		VerbRule.thirdConjRefl("-murkšķos, -murkšķies,", "-murkšķas, pag. -murkšķējos", "murkšķēties", false), //iemurkšķēties
		// N, Ņ
		VerbRule.thirdConjRefl("-ņerkstos, -ņerksties,", "-ņerkstas, pag. -ņerkstējos", "ņerkstēties", false), //ieņerkstēties
		VerbRule.thirdConjRefl("-ņurdos, -ņurdies,", "-ņurdas, pag. -ņurdējos", "ņurdēties", true), //ieņurdēties
		// O, P
		VerbRule.thirdConjRefl("-pīkstos, -pīksties,", "-pīkstas, pag. -pīkstējos", "pīkstēties", false), //iepīkstēties
		VerbRule.thirdConjRefl("-pinkšos, -pinkšies,", "-pinkšas, pag. -pinkšējos", "pinkšēties", false), //iepinkšēties
		VerbRule.thirdConjRefl("-pinkšķos, -pinkšķies,", "-pinkšķas, pag. -pinkšķējos", "pinkšķēties", false), //iepinkšķēties
		VerbRule.thirdConjRefl("-pukstos, -puksties,", "-pukstas, pag. -pukstējos", "pukstēties", false), //iepukstēties
		// R
		VerbRule.thirdConjRefl("-raudos, -raudies,", "-raudas, pag. -raudājos", "raudāties", false), //aizraudāties
		// S
		VerbRule.thirdConjRefl("-sēžos, -sēdies,", "-sēžas, pag. -sēdējos", "sēdēties", true), //aizsēdēties
		VerbRule.thirdConjRefl("-svinos, -svinies,", "-svinas, pag. -svinējos", "svinēties", false), //aizsvinēties
		// Š
		VerbRule.thirdConjRefl("-šņukstos, -šņuksties,", "-šņukstas, pag. -šņukstējos", "šņukstēties", false), //aizšņukstēties
		// T, U, V, Z

		// Likumi, kam ir tikai "parasti 3. pers." variants.
		// A, B
		ThirdPersVerbRule.thirdConjRefl("-brikšķas, pag. -brikšķējās", "brikšķēties", false), //aizbrikšķēties
		ThirdPersVerbRule.thirdConjRefl("-brikšas, pag. -brikšējās", "brikšēties", false), //aizbrikšēties
		ThirdPersVerbRule.thirdConjRefl("-brīkšķas, pag. -brīkšķējās", "brīkšķēties", false), //aizbrīkšķēties
		ThirdPersVerbRule.thirdConjRefl("-brīkšas, pag. -brīkšējās", "brīkšēties", false), //aizbrīkšēties
		// C, Č
		ThirdPersVerbRule.thirdConjRefl("-čabas, pag. -čabējās", "čabēties", false), //aizčabēties
		ThirdPersVerbRule.thirdConjRefl("-čaukstas, pag. -čaukstējās", "čaukstēties", false), //aizčaukstēties
		// D
		ThirdPersVerbRule.thirdConjRefl("-dārdas, pag. -dārdējās", "dārdēties", false), //aizdārdēties
		ThirdPersVerbRule.thirdConjRefl("-drebas, pag. -drebējās", "drebēties", false), //aizdrebēties
		// E, F, G
		ThirdPersVerbRule.thirdConjRefl("-grābās, pag. -grābējās", "grabēties", false), //aizgrabēties
		ThirdPersVerbRule.thirdConjRefl("-gurkstas, pag. -gurkstējās", "gurkstēties", false), //aizgurkstēties
		// H, I, J, K
		ThirdPersVerbRule.thirdConjRefl("-klabas, pag. -klabējās", "klabēties", false), //aizklabēties
		ThirdPersVerbRule.thirdConjRefl("-klaudzas, pag. -klaudzējās", "klaudzēties", false), //aizklaudzēties
		ThirdPersVerbRule.thirdConjRefl("-klukstas, pag. -klukstējās", "klukstēties", false), //aizklukstēties
		ThirdPersVerbRule.thirdConjRefl("-klunkšas, pag. -klunkšējās", "klunkšēties", false), //aizklunkšēties
		ThirdPersVerbRule.thirdConjRefl("-klunkšķas, pag. -klunkšķējās", "klunkšķēties", false), //aizklunkšķēties
		ThirdPersVerbRule.thirdConjRefl("-knakstās, pag. -knakstējās", "knakstēties", false), //aizknakstēties
		ThirdPersVerbRule.thirdConjRefl("-knakšas, pag. -knakšējās", "knakšēties", false), //aizknakšēties
		ThirdPersVerbRule.thirdConjRefl("-knakšķas, pag. -knakšķējās", "knakšķēties", false), //aizknakšķēties
		ThirdPersVerbRule.thirdConjRefl("-knaukšas, pag. -knaukšējās", 	"knaukšēties", false), //aizknaukšēties
		ThirdPersVerbRule.thirdConjRefl("-knaukšķas, pag. -knaukšķējās", "knaukšķēties", false), //aizknaukšķēties
		ThirdPersVerbRule.thirdConjRefl("-knikšas, pag. -knikšējās", "knikšēties", false), //aizknikšēties
		ThirdPersVerbRule.thirdConjRefl("-knikšķas, pag. -knikšķējās", "knikšķēties", false), //aizknikšķēties
		ThirdPersVerbRule.thirdConjRefl("-krakstas, pag. -krakstējās", "krakstēties", false), //aizkrakstēties
		ThirdPersVerbRule.thirdConjRefl("-krakšķas, pag. -krakšķējās", "krakšķēties", false), //aizkrakšķēties
		ThirdPersVerbRule.thirdConjRefl("-kurkstas, pag. -kurkstējās", "kurkstēties", false), //aizkurkstēties
		ThirdPersVerbRule.thirdConjRefl("-kurkšķas, pag. -kurkšķējās", "kurkšķēties", false), //aizkurkšķēties
		// L, M
		ThirdPersVerbRule.thirdConjRefl("-mirdzas, pag. -mirdzējās", "mirdzēties", false) //aizmirdzēties
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
			ComplexRule.secondThirdConjReflAllPers(
					"-ījos, -ījies, -ījas, arī -os, -ies, -ās, pag. -ījos", ".*īties", false), // blēdīties

			// Darbības vārdu specifiskie likumi.
			// Nav.
	};
}
