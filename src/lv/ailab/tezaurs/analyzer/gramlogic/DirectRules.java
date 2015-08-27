package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.utils.Trio;

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
				new String[] {"Lietvārds", "Atgriezeniskais lietvārds"},
				new String[] {"Sieviešu dzimte"}), //aizbildināšanās
		// Paradigmas: 7, 8 - kopdzimtes lietvārdi, galotne -a
		ComplexRule.of("ģen. -as, v. dat. -am, s. dat. -ai, kopdz.", new Trio[] {
					Trio.of(".*a", new Integer[] {7, 8}, new String[] {"Lietvārds"})},
				new String[]{"Kopdzimte"}), // aitasgalva, aizmārša

		// Paradigmas: 1, 2 - 1. deklinācija
		// Šeit varētu vēlāk vajadzēt likumus paplašināt, ja parādās jauni šķirkļi.
		SimpleRule.of("lietv. -a, v.", ".*[^aeiouāēīōū]s", 1,
				null, new String[]{"Vīriešu dzimte", "Lietvārds"}), // aerobs
		SimpleRule.of("vsk. -a, v.", ".*[^aeiouāēīōū]s", 1,
				new String[]{"Lietvārds"},
				new String[]{"Vīriešu dzimte", "Vienskaitlis"}), // acteks

		// Paradigmas: 7, 11
		ComplexRule.of("-as, s.", new Trio[] {
					Trio.of(".*a", new Integer[] {7}, new String[] {"Lietvārds"}),
					Trio.of(".*[^aeiouāēīōū]as", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {11}, new String[] {"Lietvārds"})},
				new String[] {"Sieviešu dzimte"}), // aberācija, milns, najādas

		// Paradigmas: 9, 11
		ComplexRule.of("dsk. ģen. -ņu, s.", new Trio[] {
					Trio.of(".*ne", new Integer[] {9}, new String[] {"Lietvārds"}),
					Trio.of(".*nes", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {11}, new String[] {"Lietvārds"})},
				new String[] {"Sieviešu dzimte"}), // ādmine, bākuguns, bārkšsaknes

		// Paradigma: 11 - 6. dekl.
		SimpleRule.of("-ts, -šu", ".*ts", 11,
				new String[] {"Lietvārds"},
				new String[] {"Sieviešu dzimte"}), //abonentpults
		SimpleRule.of("-vs, -vju", ".*vs", 11,
				new String[] {"Lietvārds"},
				new String[] {"Sieviešu dzimte"}), //adatzivs

		SimpleRule.of("-žu, v.", ".*ļaudis", 11,
				new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Tikai daudzskaitlī"},
				new String[] {"Vīriešu dzimte"}), //ļaudis

		// Paradigmas: 13, 14 - īpašības vārdi daudzskaitlī
		ComplexRule.of("s. -as; adj.", new Trio[] {
					Trio.of(".*i", new Integer[] {13, 14}, new String[] {"Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"})},
				new String[] {"Īpašības vārds"}), // abēji 2
		ComplexRule.of("s. -as; tikai dsk.", new Trio[] {
					Trio.of(".*i", new Integer[] {13, 14}, new String[] {"Īpašības vārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"})},
				new String[] {"Tikai daudzskaitlī"}), // abēji 1

		// Paradigma: 25 - vietniekvārdi
		SimpleRule.of("s. -as; vietniekv.", ".*i", 25,
				new String[] {"Šķirkļavārds daudzskaitlī"},
				new String[] {"Vietniekvārds"}), //abi

		// Paradigma: 30 - jaundzimušais, pēdējais
		SimpleRule.of("-šā, v. -šās, s.", ".*ušais", 30,
				new String[] {"Īpašības vārds", "Lietvārds"},
				null), //iereibušais	//TODO vai te vajag alternatīvo lemmu?
		SimpleRule.of("-ā, v.", ".*ais", 30,
				new String[] {"Īpašības vārds", "Lietvārds"},
				new String[] {"Vīriešu dzimte"}), //pirmdzimtais

		// Paradigmas: 30 -  jaundzimušais, pēdējais
		// Nedefinēta paradigma: Atgriezeniskie lietvārdi -šanās
		ComplexRule.of("-ās, s.", new Trio[] {
					Trio.of(".*šanās", new Integer[] {0}, new String[] {"Atgriezeniskais lietvārds", "Lietvārds"}),
					Trio.of(".*ā", new Integer[] {30}, new String[] {"Īpašības vārds", "Lietvārds"})},
				new String[] {"Sieviešu dzimte"}), // pirmdzimtā, -šanās

		// Paradigmas: 22, 30
		ComplexRule.of("s. -ā", new Trio[] {
					Trio.of(".*ais", new Integer[] {22, 30}, new String[] {"Īpašības vārds", "Skaitļa vārds", "Neviennozīmīga paradigma"})},
				null) // agrākais, pirmais	//TODO vai te vajag alternatīvo lemmu?
	};

	/**
	 * Paradigm 9: Lietvārds 5. deklinācija -e
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] fifthDeclNoun = {
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
					Trio.of(".*pe", new Integer[]{9}, new String[]{"Lietvārds", "Sieviešu dzimte"}),
					Trio.of(".*pes", new Integer[]{9}, new String[]{"Lietvārds", "Šķirkļavārds daudzskaitlī", "Sieviešu dzimte"}),},
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
				new String[]{"Lietvārds"},
				new String[]{"Sieviešu dzimte"}), //acetilsalicilskābe
		SimpleRule.of("-es, s. dsk. -es, -bju", ".*be", 9,
				new String[]{"Lietvārds"},
				new String[]{"Sieviešu dzimte"}), //astilbe

		SimpleRule.of("-ļu, s.", ".*les", 9,
				new String[]{"Lietvārds", "Šķirkļavārds daudzskaitlī"},
				new String[]{"Sieviešu dzimte"}), //bailes

		// Miju varianti
		SimpleRule.of("-es, dsk. ģen. -stu, arī -šu", ".*ste", 9,
				new String[]{"Lietvārds", "Miju varianti: -stu/-šu", "Sieviešu dzimte"},
				null), //dzeņaukste

		// Bez mijām
		SimpleRule.of("-es, dsk. ģen. -du", ".*de", 9,
				new  String[] {"Lietvārds", "Locīt bez mijas", "Sieviešu dzimte"},
				null), // diplomande
		SimpleRule.of("-es, dsk. ģen. -fu", ".*fe", 9,
				new  String[] {"Lietvārds", "Locīt bez mijas", "Sieviešu dzimte"},
				null), //arheogrāfe
		SimpleRule.of("-es, dsk. ģen. mufu", ".*mufe", 9,
				new String[]{"Lietvārds", "Locīt bez mijas", "Sieviešu dzimte"},
				null), //mufe
		SimpleRule.of("-es, dsk. ģen. -pu", ".*pe", 9,
				new  String[] {"Lietvārds", "Locīt bez mijas", "Sieviešu dzimte"},
				null), // filantrope
		SimpleRule.of("-es, dsk. ģen. -su", ".*se", 9,
				new  String[] {"Lietvārds", "Locīt bez mijas", "Sieviešu dzimte"},
				null), // bise
		SimpleRule.of("-es, dsk. ģen. -tu", ".*te", 9,
				new  String[] {"Lietvārds", "Locīt bez mijas", "Sieviešu dzimte"},
				null), // antisemīte
		SimpleRule.of("-es, dsk. ģen. -zu", ".*ze", 9,
				new  String[] {"Lietvārds", "Locīt bez mijas", "Sieviešu dzimte"},
				null), // autobāze
	};

	/**
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 */
	public static final Rule[] secondDeclNounRulesDirect = {
		SimpleRule.secondDeclStd("-ņa, dsk. ģen. -ņu, v.", ".*nis"), //bizmanis
		SimpleRule.secondDeclStd("-bja, v.", ".*bis"), //aizsargdambis
		SimpleRule.secondDeclStd("-ķa, v.", ".*[kķ]is"), //agnostiķis
		SimpleRule.secondDeclStd("-pja, v.", ".*pis"), //aitkopis
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
					Trio.of(".*lis", new Integer[] {3}, new String[] {"Lietvārds"}),
					Trio.of(".*ls", new Integer[] {5}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // acumirklis, bacils, durkls
		ComplexRule.of("-ņa, v.", new Trio[] {
					Trio.of(".*ņš", new Integer[] {2}, new String[] {"Lietvārds"}),
					Trio.of(".*nis", new Integer[] {3}, new String[] {"Lietvārds"}),
					Trio.of(".*suns", new Integer[] {5}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // abesīnis, dižtauriņš, dzinējsuns
		ComplexRule.of("-ša, v.", new Trio[] {
					Trio.of(".*[sšt]is", new Integer[] {3}, new String[] {"Lietvārds"}),
					Trio.of(".*ss", new Integer[] {5}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // abrkasis, lemess
		// Bez mijām
		ComplexRule.of("-ra, v.", new Trio[] {
					Trio.of(".*rs", new Integer[] {1}, new String[] {"Lietvārds"}),
					Trio.of(".*ris", new Integer[] {3}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // airis, mūrniekmeistars
		ComplexRule.of("-sa, v.", new Trio[] {
					Trio.of(".*ss", new Integer[] {1}, new String[] {"Lietvārds"}),
					Trio.of(".*sis", new Integer[] {3}, new String[] {"Lietvārds", "Locīt bez mijas"})},
				new String[]{"Vīriešu dzimte"}), // balanss, kūrviesis
		ComplexRule.of("-ta, v.", new Trio[] {
					Trio.of(".*tis", new Integer[] {3}, new String[] {"Lietvārds", "Locīt bez mijas"})},
				new String[]{"Vīriešu dzimte"}), // stereotālskatis
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.of("-a, v.", new Trio[] {
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {1}, new String[] {"Lietvārds"}),
					Trio.of(".*š", new Integer[] {2}, new String[] {"Lietvārds"}),
					Trio.of(".*[ģjķr]is", new Integer[] {3}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // abats, akustiķis//, sparguļi, skostiņi

		// Daudzkaitlis, vīriešu dzimte
		// Ar mijām
		ComplexRule.of("-ņu, v.", new Trio[] {
					Trio.of(".*ņi", new Integer[] {1, 2, 3, 4, 5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),},
				new String[]{"Vīriešu dzimte"}), // bretoņi
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.of("-u, v.", new Trio[] {
					Trio.of(".*(otāji|umi|anti|nieki|[aeiouāēīōū]īdi|isti|mēsli|svārki|plūdi|rati|vecāki|bērni|raksti|vidi|rīti|vakari|vārdi|kapi|augi|svētki|audi|laiki|putni|svari)",
							new Integer[] {1}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[bcdghklmnpstvz]i", new Integer[] {1}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*(ieši|āņi|ēži|grieži|stāvji|grauži|brunči|viļņi|ceļi|liberāļi|krampji|kaļķi)",
							new Integer[] {3}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*suņi", new Integer[] {5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*ši", new Integer[] {1, 3, 4}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
					Trio.of(".*[čļņž]i", new Integer[] {2, 3, 4}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
					Trio.of(".*(ģ|[mv]j)i", new Integer[] {3, 4}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
					Trio.of(".*([ķr]|[aeiāē]j)i", new Integer[] {1, 2, 3, 4}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
				},
				new String[]{"Vīriešu dzimte"}),
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
					Trio.of(".*ne", new Integer[] {9}, new String[] {"Lietvārds"}),
					Trio.of(".*ņas", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*nes", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
				},
				new String[]{"Sieviešu dzimte"}), // acenes, iemaņas, balodene
		ComplexRule.of("-šu, s.", new Trio[] {
					Trio.of(".*te", new Integer[] {9}, new String[] {"Lietvārds"}),
					Trio.of(".*šas", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[st]es", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*tis", new Integer[] {11}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // ahajiete, aizkulises, autosacīkstes, klaušas, šķūtis
		ComplexRule.of("-žu, s.", new Trio[] {
					Trio.of(".*žas", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[dz]es", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // mirādes, graizes, bažas
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.of("-u, s.", new Trio[] {
					Trio.of(".*a", new Integer[] {7}, new String[] {"Lietvārds"}),
					Trio.of(".*[^aeiouāēīōū]as", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*ķes", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // aijas, spēķes, zeķes, konkrēcija


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
				new String[]{"Lietvārds", "Šķirkļavārds daudzskaitlī"},
				new String[]{"Vīriešu dzimte"}), //afroamerikāņi
			// konflikts ar "bizmanis"

		// Vissliktākie šabloni - satur tikai vienu galotni un neko citu.
		// Paradigmas: 9, 7 - vienskaitlī un daudzskaitlī
		ComplexRule.of("-žu", new Trio[] {
					Trio.of(".*[dz]e", new Integer[] {9}, new String[] {"Lietvārds", "Sieviešu dzimte"}),
					Trio.of(".*[dz]es", new Integer[] {9}, new String[] {"Lietvārds", "Sieviešu dzimte", "Šķirkļavārds daudzskaitlī"})},
				null), // abioģenēze, ablumozes, akolāde, nematodes
		ComplexRule.of("-ņu", new Trio[] {
					Trio.of(".*ne", new Integer[] {9}, new String[] {"Lietvārds", "Sieviešu dzimte"}),
					Trio.of(".*nes", new Integer[] {9}, new String[] {"Lietvārds", "Sieviešu dzimte", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*ņas", new Integer[] {7}, new String[] {"Lietvārds", "Sieviešu dzimte", "Šķirkļavārds daudzskaitlī"})},
				null), // agrene, aizlaidnes
		// Paradigma: 3
		SimpleRule.of("-ņa", ".*nis", 3,
				new String[]{"Lietvārds", "Sieviešu dzimte"}, null), // abolainis
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 16: Darbības vārdi 2. konjugācija tiešie
	 */
	public static final Rule[] directSecondConjVerb = {
		// Rules for both all person and third-person-only cases.
		VerbRule.secondConjDir("-āju, -ā,", "-ā, pag. -āju", "āt"), //aijāt, aizkābāt
		VerbRule.secondConjDir("-ēju, -ē,", "-ē, pag. -ēju", "ēt"), //abonēt, adsorbēt
		VerbRule.secondConjDir("-īju, -ī,", "-ī, pag. -īju", "īt"), //apšķibīt, aizdzirkstīt
		VerbRule.secondConjDir("-oju, -o,", "-o, pag. -oju", "ot"), //aizalvot, aizbangot

		// Single-case rules.
		SimpleRule.of(	"-ēju, -ē, -ē, -ējam, -ējat, pag. -ēju, -ējām, -ējāt; pav. -ē, -ējiet", ".*ēt", 16,
				new String[] {"Darbības vārds"}, null), //adverbializēt
		SimpleRule.of("-oju, -o, -o, -ojam, -ojat, pag. -oju; -ojām, -ojāt; pav. -o, -ojiet", ".*ot", 16,
				new String[] {"Darbības vārds"}, null), //acot
	};

	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 17: Darbības vārdi 3. konjugācija tiešie
	 */
	public static final Rule[] directThirdConjVerb = {
		// Rules for both all person and third-person-only cases.
		VerbRule.thirdConjDir("-u, -i,", "-a, pag. -īju", "īt"), //aizsūtīt
		VerbRule.thirdConjDir("-inu, -ini,", "-ina, pag. -ināju", "ināt"), //aizsvilināt

		// Single-case rules.
		ThirdPersVerbRule.thirdConjDir("-ina, pag. -ināja", "ināt"), //aizducināt

	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
	 */
	public static final Rule[] reflFirstConjVerb = {
		// Rules for both all person and third-person-only cases.
		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A, B
		VerbRule.firstConjRefl("-brēcos, -brēcies,", "-brēcas, pag. -brēcos", "brēkties"), //aizbrēkties
		// C, D
		VerbRule.firstConjRefl("-degos, -dedzies,", "-degas, pag. -degos", "degties"), //aizdegties
		// E
		VerbRule.firstConjRefl("-elšos, -elsies,", "-elšas, pag. -elsos", "elsties"), //aizelsties
		// F, G
		VerbRule.firstConjRefl("-gārdzos, -gārdzies,", "-gārdzas, pag. -gārdzos", "gārgties"), //aizgārgties
		VerbRule.firstConjRefl("-gūstos, -gūsties,", "-gūstas, pag. -guvos", "gūties"), //aizgūties
		// Ģ,
		VerbRule.firstConjRefl("-ģiedos, -ģiedies,", "-ģiedas, pag. -ģidos", "ģisties"), //apģisties
		// H, I
		VerbRule.firstConjRefl("-ejos, -ejos,", "-ietas, pag. -gājos", "ieties"), //apieties
		// J, K
		VerbRule.firstConjRefl("-kliedzos, -kliedzies,", "-kliedzas, pag. -kliedzos", "kliegties"), //aizkliegties
		VerbRule.firstConjRefl("-krācos, -krācies,", "-krācas, pag. -krācos", "krākties"), //aizkrākties
		// L, M
		VerbRule.firstConjRefl("-mirstos, -mirsties,", "-mirstas, pag. -mirsos", "mirsties"), //aizmirsties
		// N, O, P, R
		VerbRule.firstConjRefl("-rijos, -rijies,", "-rijas, pag. -rijos", "rīties"), //aizrīties
		VerbRule.firstConjRefl("-rūcos, -rūcies,", "-rūcas, pag. -rūcos", "rūkties"), //aizrūkties
		// S
		VerbRule.firstConjRefl("-sienos, -sienies,", "-sienas, pag. -sējos", "sieties"), //aizsieties
		// T, U, V, Z

		// Single-case rules.
		// Verb-specific rules ordered alphabetically by verb infinitive.
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
		// Rules for both all person and third-person-only cases.
		VerbRule.secondConjRefl("-ojos, -ojies,", "-ojas, pag. -ojos", "oties"), //aiztuntuļoties, apgrēkoties
		VerbRule.secondConjRefl("-ējos, -ējies,", "-ējas, pag. -ējos", "ēties"), //abstrahēties
		VerbRule.secondConjRefl("-ājos, -ājies,", "-ājas, pag. -ājos", "āties"), //aizdomāties

		// Single-case rules.
		ThirdPersVerbRule.secondConjRefl("-ējas, pag. -ējās", "ēties"), //absorbēties
		ThirdPersVerbRule.secondConjRefl("-ojas, pag. -ojās", "oties"), //daudzkāršoties

		SimpleRule.of("-ējos, -ējies, -ējas, -ējamies, -ējaties, pag. -ējos, -ējāmies, -ējāties; pav. -ējies, -ējieties",
				".*ēties", 19,
				new String[] {"Darbības vārds"}, null), //adverbiēties

	};

	/**
	 * Šeit ir izdalīti atsevišķi atgriezenisko darbības vārdu likumi, jo tie ir
	 * gari, specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās
	 * pirmos.
	 * Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
	 */
	public static final Rule[] reflThirdConjVerb = {
		/* Rules in form "parasti 3. pers., -ās, pag. -ījās" and
		 * "-os, -ies, -ās, pag. -ījos".
		 */
		// Rules for both all person and third-person-only cases.
		VerbRule.thirdConjRefl("-os, -ies,", "-as, pag. -ējos", "ēties"), //apkaunēties, aizņaudēties
		VerbRule.thirdConjRefl("-inos, -inies,", "-inās, pag. -inājos", "ināties"), //apklaušināties
		VerbRule.thirdConjRefl("-os, -ies,", "-ās, pag. -ījos", "īties"), //apklausīties

		VerbRule.thirdConjRefl("-dziedos, -dziedies,", "-dziedas, pag. -dziedājos", "dziedāties"), //aizdziedāties
		VerbRule.thirdConjRefl("-guļos, -gulies,", "-guļas, pag. -gulējos", "gulēties"), //aizgulēties
		VerbRule.thirdConjRefl("-raudos, -raudies,", "-raudas, pag. -raudājos", "raudāties"), //aizraudāties
		VerbRule.thirdConjRefl("-sēžos, -sēdies,", "-sēžas, pag. -sēdējos", "sēdēties"), //aizsēdēties

		// Single-case rules.
		// Generic ending rules.
		ThirdPersVerbRule.thirdConjRefl("-as, pag. -ējās", "ēties"), //aizčiepstēties
		ThirdPersVerbRule.thirdConjRefl("-inās, pag. -inājās", "ināties"), //aizbubināties
		ThirdPersVerbRule.thirdConjRefl("-ās, pag. -ījās", "īties"), //aizbīdīties

		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A, B
		ThirdPersVerbRule.thirdConjRefl("-brikšķas, pag. -brikšķējās", "brikšķēties"), //aizbrikšķēties
		ThirdPersVerbRule.thirdConjRefl("-brikšas, pag. -brikšējās", "brikšēties"), //aizbrikšēties
		ThirdPersVerbRule.thirdConjRefl("-brīkšķas, pag. -brīkšķējās", "brīkšķēties"), //aizbrīkšķēties
		ThirdPersVerbRule.thirdConjRefl("-brīkšas, pag. -brīkšējās", "brīkšēties"), //aizbrīkšēties
		// C, Č
		ThirdPersVerbRule.thirdConjRefl("-čabas, pag. -čabējās", "čabēties"), //aizčabēties
		ThirdPersVerbRule.thirdConjRefl("-čaukstas, pag. -čaukstējās", "čaukstēties"), //aizčaukstēties
		// D
		ThirdPersVerbRule.thirdConjRefl("-dārdas, pag. -dārdējās", "dārdēties"), //aizdārdēties
		ThirdPersVerbRule.thirdConjRefl("-drebas, pag. -drebējās", "drebēties"), //aizdrebēties
		// E, F, G
		ThirdPersVerbRule.thirdConjRefl("-grābās, pag. -grābējās", "grabēties"), //aizgrabēties
		ThirdPersVerbRule.thirdConjRefl("-gurkstas, pag. -gurkstējās", "gurkstēties"), //aizgurkstēties
		// H, I, J, K
		ThirdPersVerbRule.thirdConjRefl("-klabas, pag. -klabējās", "klabēties"), //aizklabēties
		ThirdPersVerbRule.thirdConjRefl("-klaudzas, pag. -klaudzējās", "klaudzēties"), //aizklaudzēties
		ThirdPersVerbRule.thirdConjRefl("-klukstas, pag. -klukstējās", "klukstēties"), //aizklukstēties
		ThirdPersVerbRule.thirdConjRefl("-klunkšas, pag. -klunkšējās", "klunkšēties"), //aizklunkšēties
		ThirdPersVerbRule.thirdConjRefl("-klunkšķas, pag. -klunkšķējās", "klunkšķēties"), //aizklunkšķēties
		ThirdPersVerbRule.thirdConjRefl("-knakstās, pag. -knakstējās", "knakstēties"), //aizknakstēties
		ThirdPersVerbRule.thirdConjRefl("-knakšas, pag. -knakšējās", "knakšēties"), //aizknakšēties
		ThirdPersVerbRule.thirdConjRefl("-knakšķas, pag. -knakšķējās", "knakšķēties"), //aizknakšķēties
		ThirdPersVerbRule.thirdConjRefl("-knaukšas, pag. -knaukšējās", 	"knaukšēties"), //aizknaukšēties
		ThirdPersVerbRule.thirdConjRefl("-knaukšķas, pag. -knaukšķējās", "knaukšķēties"), //aizknaukšķēties
		ThirdPersVerbRule.thirdConjRefl("-knikšas, pag. -knikšējās", "knikšēties"), //aizknikšēties
		ThirdPersVerbRule.thirdConjRefl("-knikšķas, pag. -knikšķējās", "knikšķēties"), //aizknikšķēties
		ThirdPersVerbRule.thirdConjRefl("-krakstas, pag. -krakstējās", "krakstēties"), //aizkrakstēties
		ThirdPersVerbRule.thirdConjRefl("-krakšķas, pag. -krakšķējās", "krakšķēties"), //aizkrakšķēties
		ThirdPersVerbRule.thirdConjRefl("-kurkstas, pag. -kurkstējās", "kurkstēties"), //aizkurkstēties
		ThirdPersVerbRule.thirdConjRefl("-kurkšķas, pag. -kurkšķējās", "kurkšķēties"), //aizkurkšķēties
		// L, M
		ThirdPersVerbRule.thirdConjRefl("-mirdzas, pag. -mirdzējās", "mirdzēties") //aizmirdzēties
		// N, O, P, R, S, T, U, V, Z
	};

}
