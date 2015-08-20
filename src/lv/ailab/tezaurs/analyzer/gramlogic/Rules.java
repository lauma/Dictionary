package lv.ailab.tezaurs.analyzer.gramlogic;

import lv.ailab.tezaurs.utils.StringUtils;
import lv.ailab.tezaurs.utils.Trio;

/**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * Gram.processBeginingWithPatterns(String, String)
 * Šobrīd šeit ir tikai formas ziņā vienkāršākie likumi, pārējie ir Gram klasē
 * apstrādes funkcijās.
 * @author Lauma
 */
public class Rules
{
	/**
	 * Likumi kas jālieto ar Rule.applyDirect().
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final Rule[] otherRulesDirect = {
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
	 * Likumi kas jālieto ar Rule.applyOptHyphens()
	 * Pārējie likumi, kas neatbilst citām grupām.
	 */
	public static final Rule[] otherRulesOptHyperns = {
		/* Paradigm 11: Lietvārds 6. deklinācija -s
		 * Rules in form "-valsts, dsk. ģen. -valstu, s.", i.e containing full 6th
		 * declension nouns.
		 */
		SimpleRule.of("-acs, dsk. ģen. -acu, s.", ".*acs", 11,
				new String[] {"Lietvārds"}, new String[] {"Sieviešu dzimte"}), //uzacs, acs
		SimpleRule.of("-krāsns, dsk. ģen. -krāšņu, s.", ".*krāsns", 11,
				new String[] {"Lietvārds"}, new String[] {"Sieviešu dzimte"}), //aizkrāsns
		SimpleRule.of("-valsts, dsk. ģen. -valstu, s.", ".*valsts", 11,
				new String[] {"Lietvārds"}, new String[] {"Sieviešu dzimte"}), //agrārvalsts

		/* Paradigm 25: Pronouns
		 */
		SimpleRule.of("ģen. -kā, dat. -kam, akuz., instr. -ko", ".*kas", 25,
				new String[] {"Vietniekvārds", "Locīt kā \"kas\""}, null), //daudzkas
	};

	/**
	 * Likumi kas jālieto ar Rule.applyDirect().
	 * Paradigm 9: Lietvārds 2. deklinācija -is
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] fifthDeclNounRulesDirect = {
		//Standartizētie
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ču, s.", ".*[cč]e"), //ābece
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ļu, s.", ".*[lļ]e"), //ābele
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -šu, s.", ".*[tsš]e"), //abate
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ņu, s.", ".*[nņ]e"), //ābolaine
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -žu, s.", ".*[zžd]e"), //ābolmaize
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ru, s.", ".*re"), //administratore
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -stu, s.", ".*ste"), //abolicioniste
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ģu, s.", ".*[gģ]e"), //aeroloģe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -vju, s.", ".*ve"), //agave
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ķu, s.", ".*[kķ]e"), //agnostiķe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -mju, s.", ".*me"), //agronome

		// Mazāk standartizētie
		SimpleRule.fifthDeclStd("-es, s., dsk. ģen. -bju", ".*be"), //acetilsalicilskābe
		SimpleRule.fifthDeclStd("-es, s. dsk. -es, -bju", ".*be"), //astilbe
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -ru", ".*re"), //ādere
		SimpleRule.fifthDeclStd("-es, dsk. ģen. -šņu", ".*[šs][nņ]e"), //aizkrāsne

		SimpleRule.of("-ļu, s.", ".*les", 9,
				new String[]{"Lietvārds", "Šķirkļavārds daudzskaitlī"},
				new String[]{"Sieviešu dzimte"}), //bailes
	};

	/**
	 * Likumi kas jālieto ar Rule.applyDirect().
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 * Likumi formā "-es, dsk. ģen. -ču, s.".
	 */
	public static final Rule[] secondDeclNounRulesDirect = {
		SimpleRule.secondDeclStd("-ņa, dsk. ģen. -ņu, v.", ".*nis"), //bizmanis
		SimpleRule.secondDeclStd("-ķa, v.", ".*[kķ]is"), //agnostiķis
		SimpleRule.secondDeclStd("-pja, v.", ".*pis"), //aitkopis
		SimpleRule.secondDeclStd("-žņa, v.", ".*znis"), //aitkopis
		SimpleRule.secondDeclStd("-ža, v.", ".*[dzž]is"), //ādgrauzis
	};

	/**
	 * Likumi kas jālieto ar Rule.applyDirect().
	 * Paradigm 1: Lietvārds 1. deklinācija -s
	 * Paradigm 2: Lietvārds 1. deklinācija -š
	 * Paradigm 3: Lietvārds 2. deklinācija -is
	 * Paradigm 4: Lietvārds 2. deklinācija -s (nom. == ģen.)
	 * Paradigm 5: Lietvārds 2. deklinācija -suns, durkls, (nom.!= ģen.)
	 * Paradigm 7: Lietvārds 4. deklinācija -a siev. dz.
	 * Paradigm 9: Lietvārds 5. deklinācija -e siev. dz.
	 * Paradigm 11: Lietvārds 6. deklinācija -s
	 * Vīriešu dzimtes galotnes, kur jāveic paradigmu šķirošana.
	 */
	public static final Rule[] nounMultiDeclRulesDirect = {
		// Paradigmas: 3, 5
		ComplexRule.of("-ļa, v.", new Trio[] {
					Trio.of(".*lis", new Integer[] {3}, new String[] {"Lietvārds"}),
					Trio.of(".*ls", new Integer[] {5}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // acumirklis, durkls
		ComplexRule.of("-ša, v.", new Trio[] {
					Trio.of(".*[stš]is", new Integer[] {3}, new String[] {"Lietvārds"}),
					Trio.of(".*ss", new Integer[] {5}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // abrkasis, lemess
		//Paradigmas: 1, 3
		ComplexRule.of("-ra, v.", new Trio[] {
					Trio.of(".*rs", new Integer[] {1}, new String[] {"Lietvārds"}),
					Trio.of(".*ris", new Integer[] {3}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // airis, mūrniekmeistars
		// Paradigmas: 2, 3, 5
		ComplexRule.of("-ņa, v.", new Trio[] {
					Trio.of(".*ņš", new Integer[] {2}, new String[] {"Lietvārds"}),
					Trio.of(".*nis", new Integer[] {3}, new String[] {"Lietvārds"}),
					Trio.of(".*suns", new Integer[] {5}, new String[] {"Lietvārds"})},
				new String[]{"Vīriešu dzimte"}), // abesīnis, dižtauriņš, dzinējsuns
		// Paradigmsa: 1, 2, 3 (bez mijas), 1-5 (daudzskaitlī)
		ComplexRule.of("-a, v.", new Trio[] {
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {1}, new String[] {"Lietvārds"}),
					Trio.of(".*š", new Integer[] {2}, new String[] {"Lietvārds"}),
					Trio.of(".*[ģjķrt]is", new Integer[] {3}, new String[] {"Lietvārds"}),
					Trio.of(".*[ņ]i", new Integer[] {1, 2, 3, 4, 5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
					Trio.of(".*[ļ]i", new Integer[] {1, 2, 3, 5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),},
				new String[]{"Vīriešu dzimte"}), // abats, akustiķis, sparguļi, skostiņi

		// Paradigms: 9, vienskaitlis + daudzskaitlis.
		ComplexRule.of("-es, dsk. ģen. -pju, s.", new Trio[] {
					Trio.of(".*pe", new Integer[] {9}, new String[] {"Lietvārds"}),
					Trio.of(".*pes", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // aitkope, tūsklapes

		// Paradigms: 7, 9
		ComplexRule.of("-žu, s.", new Trio[] {
					Trio.of(".*žas", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[dz]es", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // mirādes, graizes, bažas
		ComplexRule.of("-ņu, s.", new Trio[] {
					Trio.of(".*ne", new Integer[] {9}, new String[] {"Lietvārds"}),
					Trio.of(".*ņas", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*nes", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // acenes, iemaņas, balodene
		// Paradigms: 7, 9, 11
		ComplexRule.of("-šu, s.", new Trio[] {
					Trio.of(".*te", new Integer[] {9}, new String[] {"Lietvārds"}),
					Trio.of(".*šas", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[st]es", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*tis", new Integer[] {11}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // ahajiete, aizkulises, bikses, klaušas

		// Paradigas: 7, 9
		ComplexRule.of("-u, s.", new Trio[] {
					Trio.of(".*a", new Integer[] {7}, new String[] {"Lietvārds"}),
					Trio.of(".*as", new Integer[] {7}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*ķes", new Integer[] {9}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),},
				new String[]{"Sieviešu dzimte"}), // aijas, zeķes, konkrēcija

		// Paradigms: 1-5 (plural forms)
		ComplexRule.of("-ņu, v.", new Trio[] {
					Trio.of(".*ņi", new Integer[] {1, 2, 3, 4, 5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),},
				new String[]{"Vīriešu dzimte"}), // bretoņi
		ComplexRule.of("-u, v.", new Trio[] {
					Trio.of(".*(nieki|umi|otāji)", new Integer[] {1}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī"}),
					Trio.of(".*[cdlmnpvz]i", new Integer[] {1, 2}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
					Trio.of(".*(ieši|[vpm]ji)", new Integer[] {3, 5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
					Trio.of(".*[ņš]i", new Integer[] {1, 2, 3, 4, 5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),
					Trio.of(".*([bgkhrstčģķļž]|[aeiouāēīōū]j)i", new Integer[] {1, 2, 3, 5}, new String[] {"Lietvārds", "Šķirkļavārds daudzskaitlī", "Neviennozīmīga paradigma"}),},
				new String[]{"Vīriešu dzimte"}), // abesīņi, abhāzi, ādgrauži, adigejieši, adžāri, alimenti, angļi, antinukloni, apakšbrunči
};

	/**
	 * Likumi kas jālieto ar Rule.applyDirect().
	 * Likumi, kas ir citu likumu prefiksi.
	 * Šajā masīvā jāievēro likumu secība, citādi slikti būs. Šo masīvu jālieto
	 * pašu pēdējo.
	 */
	public static final Rule[] dangerousRulesDirect = {
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
	 * Likumi kas jālieto ar Rule.applyDirect().
	 * Šeit ir izdalīti atsevišķi darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 */
	public static final Rule[] verbRulesDirect = {
		/* Paradigm 16: Darbības vārdi 2. konjugācija tiešie
		 */
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


		/* Paradigm 17: Darbības vārdi 3. konjugācija tiešie
		 */
		// Rules for both all person and third-person-only cases.
		VerbRule.thirdConjDir("-u, -i,", "-a, pag. -īju", "īt"), //aizsūtīt
		VerbRule.thirdConjDir("-inu, -ini,", "-ina, pag. -ināju", "ināt"), //aizsvilināt

		// Single-case rules.
		ThirdPersVerbRule.thirdConjDir("-ina, pag. -ināja", "ināt"), //aizducināt


		/* Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
		 */
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
		// N, O, P, R, S, T, U, V, Z

		// Single-case rules.
		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A, B, C, D
		ThirdPersVerbRule.firstConjRefl("-dūcas, pag. -dūcās", "dūkties"), //aizdūkties
		// E, F, G, H, I, J, K
		ThirdPersVerbRule.firstConjRefl("-kaucas, pag. -kaucās", "kaukties"), //aizkaukties
		// L, M, N, O, P, R, S, Š
		ThirdPersVerbRule.firstConjRefl("-šalcas, pag. -šalcās", "šalkties"), //aizšalkties
		// T, U, V, Z


		/* Paradigm 19: Darbības vārdi 2. konjugācija atgriezeniski
		 */
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


		/* Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
		 * Rules in form "parasti 3. pers., -ās, pag. -ījās" and
		 * "-os, -ies, -ās, pag. -ījos".
		 */
		// Rules for both all person and third-person-only cases.
		VerbRule.thirdConjRefl("-os, -ies,", "-as, pag. -ējos", "ēties"), //apkaunēties, aizņaudēties
		VerbRule.thirdConjRefl("-inos, -inies,", "-inās, pag. -inājos", "ināties"), //apklaušināties
		VerbRule.thirdConjRefl("-os, -ies,", "-ās, pag. -ījos", "īties"), //apklausīties

		VerbRule.thirdConjRefl("-dziedos, -dziedies,", "-dziedas, pag. -dziedājos", "dziedāties"), //aizdziedāties
		VerbRule.thirdConjRefl("-guļos, -gulies,", "-guļas, pag. -gulējos", "gulēties"), //aizgulēties
		VerbRule.thirdConjRefl("-raudos, -raudies,", "-raudas, pag. -raudājos", "raudāties"), //aizraudāties

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

	/**
	 * Likumi kas jālieto ar SimpleRule.applyOptHyphens().
	 * Šeit ir izdalīti atsevišķi darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 */
	public static final Rule[] verbRulesOptHyperns = {
		/* Paradigm 15: Darbības vārdi 1. konjugācija tiešie + parasti 3. pers.
		 */
		// Rules for both all person and third-person-only cases.
		// Verbs with infinitive homoforms:
		VerbRule.of("-aužu, -aud,", "-auž, pag. -audu", "aust", 15,
				new String[] {"Locīt kā \"aust\" (kā zirneklis)"}, null), //aizaust 2
		VerbRule.of("-dedzu, -dedz,", "-dedz, pag. -dedzu", "degt", 15,
				new String[] {"Locīt kā \"degt\" (kādu citu)"}, null), //aizdegt 1
		VerbRule.of("-degu, -dedz,", "-deg, pag. -degu", "degt", 15,
				new String[] {"Locīt kā \"degt\" (pašam)"}, null), //apdegt, aizdegt 2
		VerbRule.of("-dzenu, -dzen,", "-dzen, pag. -dzinu", "dzīt", 15,
				new String[] {"Locīt kā \"dzīt\" (kā lopus)"}, null), //aizdzīt 1
		VerbRule.of("-iru, -ir,", "-ir, pag. -īru", "irt", 15,
				new String[] {"Locīt kā \"irt\" (kā ar airiem)"}, null), //aizirt 1
		VerbRule.of("-minu, -min,", "-min, pag. -minu", "mīt", 15,
				new String[] {"Locīt kā \"mīt\" (kā pedāļus)"}, null), //aizmīt 1
		VerbRule.of("-miju, -mij,", "-mij, pag. -miju", "mīt", 15,
				new String[] {"Locīt kā \"mīt\" (kā naudu)"}, null), //aizmīt 2

		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A
		VerbRule.firstConjDir("-aru, -ar,", "-ar, pag. -aru", "art"), //aizart
		VerbRule.firstConjDir("-augu, -audz,", "-aug, pag. -augu", "augt"), //ieaugt, aizaugt
		// B
		VerbRule.firstConjDir("-bāžu, -bāz,", "-bāž, pag. -bāzu", "bāzt"), //aizbāzt
		VerbRule.firstConjDir("-bēgu, -bēdz,", "-bēg, pag. -bēgu", "bēgt"), //aizbēgt
		VerbRule.firstConjDir("-beru, -ber,", "-ber, pag. -bēru", "bērt"), //aizbērt
		VerbRule.firstConjDir("-bilstu, -bilsti,", "-bilst, pag. -bildu", "bilst"), //aizbilst
		VerbRule.firstConjDir("-birstu, -birsti,", "-birst, pag. -biru", "birt"), //apbirt, aizbirt
		VerbRule.firstConjDir("-braucu, -brauc,", "-brauc, pag. -braucu", "braukt"), //aizbraukt
		VerbRule.firstConjDir("-brāžu, -brāz,", "-brāž, pag. -brāzu", "brāzt"), //aizbrāzt
		VerbRule.firstConjDir("-brienu, -brien,", "-brien, pag. -bridu", "brist"), //aizbrist
		// C
		VerbRule.firstConjDir("-ceļu, -cel,", "-ceļ, pag. -cēlu", "celt"), //aizcelt
		VerbRule.firstConjDir("-cērtu, -cērt,", "-cērt, pag. -cirtu", "cirst"), //aizcirst
		// D
		VerbRule.firstConjDir("-diebju, -dieb,", "-diebj, pag. -diebu", "diebt"), //aizdiebt
		VerbRule.firstConjDir("-diedzu, -diedz,", "-diedz, pag. -diedzu", "diegt"), //aizdiegt 1,2
		VerbRule.firstConjDir("-dodu, -dod,", "-dod, pag. -devu", "dot"), //aizdot
		VerbRule.firstConjDir("-drāžu, -drāz,", "-drāž, pag. -drāzu", "drāzt"), //aizdrāzt
		VerbRule.firstConjDir("-duru, -dur,", "-dur, pag. -dūru", "durt"), //aizdurt
		VerbRule.firstConjDir("-dūcu, -dūc,", "-dūc, pag. -dūcu", "dūkt"), //atdūkt, aizdūkt
		VerbRule.firstConjDir("-dzeļu, -dzel,", "-dzeļ, pag. -dzēlu", "dzelt"), //atdzelt, aizdzelt
		VerbRule.firstConjDir("-dzeru, -dzer,", "-dzer, pag. -dzēru", "dzert"), //aizdzert
		// E
		VerbRule.firstConjDir("-ēdu, -ēd,", "-ēd, pag. -ēdu", "ēst"), //aizēst
		// F, G
		VerbRule.firstConjDir("-gāžu, -gāz,", "-gāž, pag. -gāzu", "gāzt"), //aizgāzt
		VerbRule.firstConjDir("-glaužu, -glaud,", "-glauž, pag. -glaudu", "glaust"), //aizglaust
		VerbRule.firstConjDir("-grābju, -grāb,", "-grābj, pag. -grābu", "grābt"), //aizgrābt
		VerbRule.firstConjDir("-graužu, -grauz,", "-grauž, pag. -grauzu", "grauzt"), //aizgrauzt
		VerbRule.firstConjDir("-griežu, -griez,", "-griež, pag. -griezu", "griezt"), //aizgriezt 1, 2
		VerbRule.firstConjDir("-grimstu, -grimsti,", "-grimst, pag. -grimu", "grimt"), //atgrimt, aizgrimt
		VerbRule.firstConjDir("-grūžu, -grūd,", "-grūž, pag. -grūdu", "grūst"), //aizgrūst
		VerbRule.firstConjDir("-gūstu, -gūsti,", "-gūst, pag. -guvu", "gūt"), //aizgūt
		// Ģ
		VerbRule.firstConjDir("-ģiedu, -ģied,", "-ģied, pag. -gidu", "ģist"), //apģist
		// H, I
		VerbRule.firstConjDir("-eju, -ej,", "-iet, pag. -gāju", "iet"), //apiet
		// J
		VerbRule.firstConjDir("-jāju, -jāj,", "-jāj, pag. -jāju", "jāt"), //aizjāt
		VerbRule.firstConjDir("-jožu, -joz,", "-jož, pag. -jozu", "jozt"), //aizjozt 1, 2
		VerbRule.firstConjDir("-jūdzu, -jūdz,", "-jūdz, pag. -jūdzu", "jūgt"), //aizjūgt
		// K
		VerbRule.firstConjDir("-kalstu, -kalsti,", "-kalst, pag. -kaltu", "kalst"), //izkalst, aizkalst
		VerbRule.firstConjDir("-kāpju, -kāp,", "-kāpj, pag. -kāpu", "kāpt"), //aizkāpt
		VerbRule.firstConjDir("-karu, -kar,", "-kar, pag. -kāru", "kārt"), //aizkārt
		VerbRule.firstConjDir("-kaucu, -kauc,", "-kauc, pag. -kaucu", "kaukt"), //izkaukt, aizkaukt
		VerbRule.firstConjDir("-kauju, -kauj,", "-kauj, pag. -kāvu", "kaut"), //apkaut
		VerbRule.firstConjDir("-klāju, -klāj,", "-klāj, pag. -klāju", "klāt"), //apklāt
		VerbRule.firstConjDir("-kliedzu, -kliedz,", "-kliedz, pag. -kliedzu", "kliegt"), //aizkliegt
		VerbRule.firstConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimtu", "klimst"), //aizklimst
		VerbRule.firstConjDir("-klīstu, -klīsti,", "-klīst, pag. -klīdu", "klīst"), //aizklīst
		VerbRule.firstConjDir("-kļūstu, -kļūsti,", "-kļūst, pag. -kļuvu", "kļūt"), //aizkļūt
		VerbRule.firstConjDir("-knābju, -knāb,", "-knābj, pag. -knābu", "knābt"), //uzknābt, aizknābt
		VerbRule.firstConjDir("-kožu, -kod,", "-kož, pag. -kodu", "kost"), //aizkost
		VerbRule.firstConjDir("-krāpju, -krāp,", "-krāpj, pag. -krāpu", "krāpt"), //aizkrāpt
		VerbRule.firstConjDir("-krauju, -krauj,", "-krauj, pag. -krāvu", "kraut"), //aizkraut
		VerbRule.firstConjDir("-krītu, -krīti,", "-krīt, pag. -kritu", "krist"), //aizkrist
		VerbRule.firstConjDir("-kuru, -kur,", "-kur, pag. -kūru", "kurt"), //aizkurt
		VerbRule.firstConjDir("-kūstu, -kusti,", "-kūst, pag. -kusu", "kust"), //aizkust
		VerbRule.firstConjDir("-kvēpstu, -kvēpsti,", "-kvēpst, pag. -kvēpu", "kvēpt"), //apkvēpt, aizkvēpt
		// Ķ
		VerbRule.firstConjDir("-ķepu, -ķep,", "-ķep, pag. -ķepu", "ķept"), //apķept, aizķept
		VerbRule.firstConjDir("-ķeru, -ķer,", "-ķer, pag. -ķēru", "ķert"), //aizķert
		// L
		VerbRule.firstConjDir("-laižu, -laid,", "-laiž, pag. -laidu", "laist"), //aizlaist
		VerbRule.firstConjDir("-laužu, -lauz,", "-lauž, pag. -lauzu", "lauzt"), //aizlauzt
		VerbRule.firstConjDir("-lecu, -lec,", "-lec, pag. -lēcu", "lēkt"), //aizlēkt
		VerbRule.firstConjDir("-liedzu, -liedz,", "-liedz, pag. -liedzu", "liegt"), //aizliegt
		VerbRule.firstConjDir("-liecu, -liec,", "-liec, pag. -liecu", "liekt"), //aizliekt
		VerbRule.firstConjDir("-leju, -lej,", "-lej, pag. -lēju", "liet"), //aizliet
		VerbRule.firstConjDir("-lieku, -liec,", "-liek, pag. -liku", "likt"), //aizlikt
		VerbRule.firstConjDir("-līpu, -līpi,", "-līp, pag. -lipu", "lipt"), //aplipt, aizlipt
		VerbRule.firstConjDir("-līkstu, -līksti,", "-līkst, pag. -līku", "līkt"), //nolīkt, aizlīkt
		VerbRule.firstConjDir("-lienu, -lien,", "-lien, pag. -līdu", "līst"), //aizlīst
		VerbRule.firstConjDir("-līstu, -līsti,", "-līst, pag. -liju", "līt"), //aplīt, aizlīt
		VerbRule.firstConjDir("-lobju, -lob,", "-lobj, pag. -lobu", "lobt"), //aizlobt
		VerbRule.firstConjDir("-lūdzu, -lūdz,", "-lūdz, pag. -lūdzu", "lūgt"), //aizlūgt
		VerbRule.firstConjDir("-lūstu, -lūsti,", "-lūst, pag. -lūzu", "lūzt"), //aizlūzt, aizlūzt
		// M
		VerbRule.firstConjDir("-metu, -met,", "-met, pag. -metu", "mest"), //aizmest
		VerbRule.firstConjDir("-mēžu, -mēz,", "-mēž, pag. -mēzu", "mēzt"), //aizmēzt
		VerbRule.firstConjDir("-miedzu, -miedz,", "-miedz, pag. -miedzu", "miegt"), //aizmiegt
		VerbRule.firstConjDir("-miegu, -miedz,", "-mieg, pag. -migu", "migt"), //aizmigt
		VerbRule.firstConjDir("-mirstu, -mirsti,", "-mirst, pag. -mirsu", "mirst"), //aizmirst
		VerbRule.firstConjDir("-mūku, -mūc,", "-mūk, pag. -muku", "mukt"), //aizmukt
		// N
		VerbRule.firstConjDir("-nesu, -nes,", "-nes, pag. -nesu", "nest"), //aiznest
		VerbRule.firstConjDir("-nirstu, -nirsti,", "-nirst, pag. -niru", "nirt"), //aiznirt
		// Ņ
		VerbRule.firstConjDir("-ņemu, -ņem,", "-ņem, pag. -ņēmu", "ņemt"), //aizņemt
		// O, P
		VerbRule.firstConjDir("-pampstu, -pampsti,", "-pampst, pag. -pampu", "pampt"), //nopampt, aizpampt
		VerbRule.firstConjDir("-pinu, -pin,", "-pin, pag. -pinu", "pīt"), //aizpīt
		VerbRule.firstConjDir("-ploku, -ploc,", "-plok, pag. -plaku", "plakt"), //aizplakt
		VerbRule.firstConjDir("-plaukstu, -plauksti,", "-plaukst, pag. -plauku", "plaukt"), //atplaukt, aizplaukt
		VerbRule.firstConjDir("-plēšu, -plēs,", "-plēš, pag. plēsu", "plēst"), //aizplēst
		VerbRule.firstConjDir("-plīstu, -plīsti,", "-plīst, pag. -plīsu", "plīst"), //applīst, aizplīst
		VerbRule.firstConjDir("-plūcu, -plūc,", "-plūc, pag. -plūcu", "plūkt"), //aizplūkt
		VerbRule.firstConjDir("-plūstu, -plūsti,", "-plūst, pag. -plūdu", "plūst"), //applūst, aizplūst
		VerbRule.firstConjDir("-pļauju, -pļauj,", "-pļauj, pag. -pļāvu", "pļaut"), //aizpļaut
		VerbRule.firstConjDir("-pūšu, -pūt,", "-pūš, pag. -pūtu", "pūst"), //aizpūst
		VerbRule.firstConjDir("-pūstu, -pūsti,", "-pūst, pag. -puvu", "pūt"), //aizpūt, pūt
		// R
		VerbRule.firstConjDir("-roku, -roc,", "-rok, pag. -raku", "rakt"), //aizrakt
		VerbRule.firstConjDir("-raušu, -raus,", "-rauš, pag. -rausu", "raust"), //aizraust
		// S, Š
		VerbRule.firstConjDir("-šalcu, -šalc,", "-šalc, pag. -šalcu", "šalkt"), //pašalkt, aizšalkt
		// T
		VerbRule.firstConjDir("-tūkstu, -tūksti,", "-tūkst; pag. -tūku", "tūkt"), //aptūkt, aiztūkt
		VerbRule.firstConjDir("-tveru, -tver,", "-tver, pag. -tvēru", "tvert"), //aiztvert
		// U, V, Z

		// Single case rules.
		// Verb specific rules ordered by type and alphabetically by verb infinitive.
		SimpleRule.of("-gulstu, -gulsti, -gulst, pag. -gūlu, arī -gulu", ".*gult", 15,
				new String[] {"Darbības vārds", "Locīt kā \"gult\"", "Paralēlās formas"},
				null), //aizgult
		SimpleRule.of("-jumju, -jum, -jumj, pag. -jūmu, arī -jumu", ".*jumt", 15,
				new String[] {"Darbības vārds", "Locīt kā \"jumt\"", "Paralēlās formas"},
				null), //aizjumt
		SimpleRule.of("-plešu, -plet, -pleš, pag. -pletu, arī -plētu", ".*plest", 15,
				new String[] {"Darbības vārds", "Locīt kā \"plest\"", "Paralēlās formas"},
				null), //aizplest
		SimpleRule.of("-tupstu, -tupsti, -tupst, pag. -tupu", ".*tupt", 15,
				new String[]{"Darbības vārds", "Locīt kā \"tupt\"", "Paralēlās formas"},
				null), //aiztupt
				// TODO tupu/tupstu

		// A, B
		ThirdPersVerbRule.firstConjDir("-brūk, pag. -bruka", "brukt"), //aizbrukt
		// C, D
		ThirdPersVerbRule.firstConjDir("-dim, pag. -dima", "dimt"), //aizdimt
		ThirdPersVerbRule.firstConjDir("-dip, pag. -dipa", "dipt"), //aizdipt
		// E, F, G
		ThirdPersVerbRule.firstConjDir("-grūst, pag. -gruva", "grūt"), //aizgrūt
		// H, I, J, K
		ThirdPersVerbRule.firstConjDir("-kviec, pag. -kvieca", "kviekt"), //aizkviekt
		// L, M
		ThirdPersVerbRule.firstConjDir("-milst, pag. -milza", "milzt"), //aizmilzt
		// N, Ņ
		ThirdPersVerbRule.firstConjDir("-ņirb, pag. -ņirba", "ņirbt"), //aizņirbt
		// O, P, R, S, Š, T, U, V, Z

		// Parallel forms and nonstandard.
		SimpleRule.of("parasti 3. pers., -aust, pag. -ausa", ".*aust", 15,
				new String[]{"Darbības vārds", "Locīt kā \"aust\" (kā gaisma)"},
				new String[]{"Parasti 3. personā"}), //aizaust 1
		SimpleRule.of("parasti 3. pers., -dzīst, pag. -dzija", ".*dzīt", 15,
				new String[]{"Darbības vārds", "Locīt kā \"dzīt\" (kā ievainojumi)"},
				new String[]{"Parasti 3. personā"}), //aizdzīt 2
		SimpleRule.of("parasti 3. pers., -irst, pag. -ira", ".*irt", 15,
				new String[] {"Darbības vārds", "Locīt kā \"irt\" (kā audums)"},
				new String[] {"Parasti 3. personā"}), //irt 2
		SimpleRule.of("3. pers. -guldz, pag. -guldza", ".*gulgt", 15,
				new String[]{"Darbības vārds", "Locīt kā \"gulgt\""},
				new String[]{"Parasti 3. personā"}), //aizgulgt

		/* Paradigm 16: Darbības vārdi 2. konjugācija tiešie
		 */
		// Rules for both all person and third-person-only cases.
		VerbRule.secondConjDir("-dabūju, -dabū,", "-dabū, pag. -dabūju", "dabūt"), //aizdabūt

		// Single case rules.
		// Verb-specific rules.
		ThirdPersVerbRule.secondConjDir("-kūko, pag. -kūkoja", "kūkot"), //aizkūkot
		ThirdPersVerbRule.secondConjDir("-mirgo, pag. -mirgoja", "mirgot"), //aizmirgot
		// Parallel forms.
		SimpleRule.of("parasti 3. pers., -ē, pag. -ēja (retāk -gluma, 1. konj.)", ".*glumēt", 16,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizglumēt
		SimpleRule.of(
				"parasti 3. pers., -glumē, pag. -glumēja (retāk -gluma, 1. konj.)",
				".*glumēt", 16,
				new String[]{"Darbības vārds", "Paralēlās formas"},
				new String[]{"Parasti 3. personā"}), //izglumēt


		/* Paradigm 17: Darbības vārdi 3. konjugācija tiešie
		 */
		// Rules for both all person and third-person-only cases.
		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A, B, C, D
		VerbRule.thirdConjDir("-dziedu, -dziedi,", "-dzied, pag. -dziedāju", "dziedāt"), //aizdziedāt
		// E, F, G
		VerbRule.thirdConjDir("-grabu, -grabi,", "-grab, pag. -grabēju", "grabēt"), //sagrabēt, aizgrabēt
		VerbRule.thirdConjDir("-guļu, -guli,", "-guļ, pag. -gulēju", "gulēt"), //aizgulēt
		// H, I, J, K
		VerbRule.thirdConjDir("-klabu, -klabi,", "-klab, pag. -klabēju", "klabēt"), //paklabēt, aizklabēt
		VerbRule.thirdConjDir("-klimstu, -klimsti,", "-klimst, pag. -klimstēju", "klimstēt"), //aizklimstēt
		VerbRule.thirdConjDir("-kustu, -kusti,", "-kust, pag. -kustēju", "kustēt"), //aizkustēt
		VerbRule.thirdConjDir("-kūpu, -kūpi,", "-kūp, pag. -kūpēju", "kūpēt"), //apkūpēt, aizkūpēt
		// L
		VerbRule.thirdConjDir("-loku, -loki,", "-loka, pag. -locīju", "locīt"), //aizlocīt
		// M, N, O, P
		VerbRule.thirdConjDir("-peldu, -peldi,", "-peld, pag. -peldēju", "peldēt"), //aizpeldēt
		VerbRule.thirdConjDir("-pilu, -pili,", "-pil, pag. -pilēju", "pilēt"), //appilēt, aizpilēt
		VerbRule.thirdConjDir("-precu, -preci,", "-prec, pag. -precēju", "precēt"), //aizprecēt
		VerbRule.thirdConjDir("-putu, -puti,", "-put, pag. -putēju", "putēt"), //aizputēt, apputēt
		// R
		VerbRule.thirdConjDir("-raušos, -rausies,", "-raušas, pag. -rausos", "rausties"), //aizrausties
		// S, T
		VerbRule.thirdConjDir("-turu, -turi,", "-tur, pag. -turēju", "turēt"), //aizturēt
		// U, V, Z

		// Single case rules.
		// Verb specific rules ordered by type and alphabetically by verb infinitive.
		SimpleRule.of(
				"-moku, -moki, -moka, arī -mocu, -moci, -moca, pag. -mocīju",
				".*mocīt", 17,
				new String[]{"Darbības vārds", "Paralēlās formas"}, null), //aizmocīt

		// A, B
		ThirdPersVerbRule.thirdConjDir("-blākš, pag. -blākšēja", "blākšēt"), //aizblākšēt
		ThirdPersVerbRule.thirdConjDir("-blākšķ, pag. -blākšķēja", "blākšķēt"), //aizblākšķēt
		// C, Č
		ThirdPersVerbRule.thirdConjDir("-čab, pag. -čabēja", "čabēt"), //aizčabēt
		ThirdPersVerbRule.thirdConjDir("-čaukst, pag. -čaukstēja", "čaukstēt"), //aizčaukstēt
		// D
		ThirdPersVerbRule.thirdConjDir("-dārd, pag. -dārdēja", "dārdēt"), //aizdārdēt
		ThirdPersVerbRule.thirdConjDir("-dimd, pag. -dimdēja", "dimdēt"), //aizdimdēt
		ThirdPersVerbRule.thirdConjDir("-dip, pag. -dipēja", "dipēt"), //aizdipēt
		ThirdPersVerbRule.thirdConjDir("-dun, pag. -dunēja", "dunēt"), //aizdunēt
		ThirdPersVerbRule.thirdConjDir("-džinkst, pag. -džinkstēja", "džinkstēt"), //aizdžinkstēt
		// E, F, G
		ThirdPersVerbRule.thirdConjDir("-gurkst, pag. -gurkstēja", "gurkstēt"), //aizgurkstēt
		// H, I, J, K
		ThirdPersVerbRule.thirdConjDir("-klakst, pag. -klakstēja", "klakstēt"), //aizklakstēt
		ThirdPersVerbRule.thirdConjDir("-klaudz, pag. -klaudzēja", "klaudzēt"), //aizklaudzēt
		// L, M, N, Ņ
		ThirdPersVerbRule.thirdConjDir("-ņirb, pag. -ņirbēja", "ņirbēt"), //aizņirbēt
		// O, P

		// R, S, T, U, V, Z

		// Parallel forms.
		SimpleRule.of("parasti 3. pers., -grand, pag. -grandēja (retāk -granda, 1. konj.)", ".*grandēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizgrandēt
		SimpleRule.of("parasti 3. pers., -gruzd, pag. -gruzdēja (retāk -gruzda, 1. konj.)", ".*gruzdēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizgruzdēt
		SimpleRule.of("parasti 3. pers., -mirdz, pag. -mirdzēja (retāk -mirdza, 1. konj.)", ".*mirdzēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizmirdzēt
		SimpleRule.of("parasti 3. pers., -pelē, arī -pel, pag. -pelēja", ".*pelēt", 17,
				new String[] {"Darbības vārds", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizpelēt

		/* Paradigm 18: Darbības vārdi 1. konjugācija atgriezeniski
		 */
		// Rules for both all person and third-person-only cases.
		// Verbs with infinitive homoforms:
		VerbRule.of("-iros, -iries,", "-iras, pag. -īros", "irties", 18,
				new String[] {"Locīt kā \"irties\" (kā ar airiem)"},
				null), //aizirties
		VerbRule.of("-minos, -minies,", "-minas, pag. -minos", "mīties", 18,
				new String[] {"Locīt kā \"mīties\" (kā pedāļus)"},
				null), //aizmīties
		VerbRule.of("-mijos, -mijies,", "-mijas, pag. -mijos", "mīties", 18,
				new String[] {"Locīt kā \"mīties\" (kā naudu)"},
				null), //apmīties

		// Verb-specific rules ordered alphabetically by verb infinitive.
		// A , B
		VerbRule.firstConjRefl("-brāžos, -brāzies,", "-brāžas, pag. -brāzos", "brāzties"), //aizbrāzties
		// C
		VerbRule.firstConjRefl("-ciešos, -cieties,", "-ciešas, pag. -cietos", "ciesties"), //aizciesties
		VerbRule.firstConjRefl("-cērtos, -cērties,", "-cērtas, pag. -cirtos", "cirsties"), //aizcirsties
		// D
		VerbRule.firstConjRefl("-drāžos, -drāzies,", "-drāžas, pag. -drāzos", "drāzties"), //aizdrāzties
		VerbRule.firstConjRefl("-duros, -duries,", "-duras, pag. -dūros", "durties"), //nodurties, aizdurties
		// E, F, G
		VerbRule.firstConjRefl("-gāžos, -gāzies,", "-gāžas, pag. -gāzos", "gāzties"), //apgāzties, aizgāzties
		VerbRule.firstConjRefl("-graužos, -grauzies,", "-graužas, pag. -grauzos", "grauzties"), //izgrauzties, aizgrauzties
		VerbRule.firstConjRefl("-griežos, -griezies,", "-griežas, pag. -griezos", "griezties"), //aizgriezties 1, 2
		// H, I, J
		VerbRule.firstConjRefl("-jūdzos, -jūdzies,", "-jūdzas, pag. -jūdzos", "jūgties"), //aizjūgties
		// K
		VerbRule.firstConjRefl("-karos, -karies,", "-karas, pag. -kāros", "kārties"), //apkārties
		VerbRule.firstConjRefl("-klājos, -klājies,", "-klājas, pag. -klājos", "klāties"), //apklāties
		VerbRule.firstConjRefl("-kuļos, -kulies,", "-kuļas, pag. -kūlos", "kulties"), //aizkulties
		VerbRule.firstConjRefl("-ķēros, -ķeries,", "-ķeras, pag. -ķēros", "ķerties"), //aizķerties
		// L
		VerbRule.firstConjRefl("-laižos, -laidies,", "-laižas, pag. -laidos", "laisties"), //aizlaisties
		VerbRule.firstConjRefl("-laužos, -lauzies,", "-laužas, pag. -lauzos", "lauzties"), //aizlauzties
		VerbRule.firstConjRefl("-liedzos, -liedzies,", "-liedzas, pag. -liedzos", "liegties"), //aizliegties
		VerbRule.firstConjRefl("-liecos, -liecies,", "-liecas, pag. -liecos", "liekties"), //aizliekties
		VerbRule.firstConjRefl("-liekos, -liecies,", "-liekas, pag. -likos", "likties"), //aizlikties
		// M
		VerbRule.firstConjRefl("-metos, -meties,", "-metas, pag. -metos", "mesties"), //aizmesties
		// N
		VerbRule.firstConjRefl("-nesos, -nesies,", "-nesas, pag. -nesos", "nesties"), //aiznesties
		// Ņ
		VerbRule.firstConjRefl("-ņemos, -ņemies,", "-ņemas, pag. -ņēmos", "ņemties"), //aizņemties
		// O, P, R
		VerbRule.firstConjRefl("-rokos, -rocies,", "-rokas, pag. -rakos", "rakties"), // aizrakties
		VerbRule.firstConjRefl("-rāpjos, -rāpies,", "-rāpjas, pag. -rāpos", "rāpties"), // aizrāpties
		// S, T, U, V, Z


		// Single case rules.
		SimpleRule.of("-gulstos, -gulsties, -gulstas, arī -guļos, -gulies, -guļas, pag. -gūlos, arī -gulos",
				".*gulties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"gulties\"", "Paralēlās formas"},
				null), //aizgulties
		SimpleRule.of("-plešos, -pleties, -plešas, pag. -pletos, arī -plētos",
				".*plesties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"plesties\"", "Paralēlās formas"},
				null), //ieplesties
		SimpleRule.of("-tupstos, -tupsties, -tupstas, pag. -tupos",
				".*tupties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"tupties\"", "Paralēlās formas"},
				null), //aiztupties

		SimpleRule.of("parasti 3. pers., -plešas, pag. -pletās, arī -plētās",
				".*plesties", 18,
				new String[] {"Darbības vārds", "Locīt kā \"plesties\"", "Paralēlās formas"},
				new String[] {"Parasti 3. personā"}), //aizplesties


		/* Paradigm 20: Darbības vārdi 3. konjugācija atgriezeniski
		 */
		// Rules for both all person and third-person-only cases.
		VerbRule.thirdConjRefl("-dzenos, -dzenies,", "-dzenas, pag. -dzinos", "dzīties"), //aizdzīties
		VerbRule.thirdConjRefl("-kustos, -kusties,", "-kustas, pag. -kustējos", "kustēties"), //aizkustēties
		VerbRule.thirdConjRefl("-peros, -peries,", "-peras, pag. -pēros", "pērties"), //aizpērties
		VerbRule.thirdConjRefl("-precos, -precies,", "-precas, pag. -precējos", "precēties"), //aizprecēties

		// Single case rules.
		ThirdPersVerbRule.thirdConjRefl("-lokās, pag. -locījās", "locīties"), //aizlocīties
		ThirdPersVerbRule.thirdConjRefl("-lokās, pag. -locījās", "locīties"), //aizlocīties

		SimpleRule.of(
				"-mokos, -mokies, -mokās, arī -mocos, -mocies, -mocās, pag. -mocījos",
				".*mocīties", 20,
				new String[]{"Darbības vārds", "Paralēlās formas"},
				null) //aizmocīties
	};
}
