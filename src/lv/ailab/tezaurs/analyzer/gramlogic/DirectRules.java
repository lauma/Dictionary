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
		SimpleRule.noun(
				"ģen. -ās, akuz. -os, instr. -os, dsk. -ās, ģen. -os, akuz. -ās, s.", ".*šanās", 0,
				new Tuple[] {Features.POS__REFL_NOUN}, new Tuple[] {Features.GENDER__FEM}), //aizbildināšanās
		// Paradigmas: 7, 8 - kopdzimtes lietvārdi, galotne -a
		ComplexRule.noun("ģen. -as, v. dat. -am, s. dat. -ai, kopdz.",
				new Trio[] {Trio.of(".*a", new Integer[] {7, 8}, null)},
				new Tuple[]{Tuple.of(Keys.GENDER, Values.COGENDER.s)}), // aitasgalva, aizmārša

		// Paradigmas: 1, 2 - 1. deklinācija
		// Šeit varētu vēlāk vajadzēt likumus paplašināt, ja parādās jauni šķirkļi.
		SimpleRule.of("lietv. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
				new Tuple[]{Features.GENDER__MASC, Features.POS__NOUN}), // aerobs
		SimpleRule.noun("vsk. -a, v.", ".*[^aeiouāēīōū]s", 1, null,
				new Tuple[]{Features.GENDER__MASC, Tuple.of(Keys.NUMBER, Values.SINGULAR.s)}), // acteks

		// Paradigmas: 7, 11
		ComplexRule.noun("-as, s.", new Trio[] {
					Trio.of(".*a", new Integer[] {7}, null),
					Trio.of(".*[^aeiouāēīōū]as", new Integer[] {7}, new Tuple[] {Features.ENTRYWORD__PLURAL}),
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {11}, null)},
				new Tuple[] {Features.GENDER__FEM}), // aberācija, milns, najādas

		// Paradigmas: 9, 11
		ComplexRule.noun("dsk. ģen. -ņu, s.", new Trio[] {
					Trio.of(".*ne", new Integer[] {9}, null),
					Trio.of(".*nes", new Integer[] {9}, new Tuple[] {Features.ENTRYWORD__PLURAL}),
					Trio.of(".*[^aeiouāēīōū]s", new Integer[] {11}, null)},
				new Tuple[] {Features.GENDER__FEM}), // ādmine, bākuguns, bārkšsaknes

		// Paradigma: 11 - 6. dekl.
		SimpleRule.noun("-ts, -šu", ".*ts", 11, new Tuple[] {Features.GENDER__FEM}, null), //abonentpults
		SimpleRule.noun("-vs, -vju", ".*vs", 11, new Tuple[] {Features.GENDER__FEM}, null), //adatzivs

		SimpleRule.noun("-žu, v.", ".*ļaudis", 11,
				new Tuple[] {Features.ENTRYWORD__PLURAL, Features.USED_ONLY__PLURAL},
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
		SimpleRule.noun("-šā, v. -šās, s.", ".*ušais", 30,
				new Tuple[] {Features.POS__ADJ}, null), //iereibušais	//TODO vai te vajag alternatīvo lemmu?
		SimpleRule.noun("-ā, v.", ".*ais", 30,
				new Tuple[] {Features.POS__ADJ}, new Tuple[] {Features.GENDER__MASC}), //pirmdzimtais

		// Paradigmas: 30 -  jaundzimušais, pēdējais
		// Nedefinēta paradigma: Atgriezeniskie lietvārdi -šanās
		ComplexRule.noun("-ās, s.", new Trio[] {
					Trio.of(".*šanās", new Integer[] {0}, new Tuple[] {Features.POS__REFL_NOUN}),
					Trio.of(".*ā", new Integer[] {30}, new Tuple[] {Features.POS__ADJ})},
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
		ComplexRule.noun("-es, dsk. ģen. -pju", new Trio[]{
						Trio.of(".*pe", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM}),
						Trio.of(".*pes", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.GENDER__FEM})},
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
		SimpleRule.noun("-es, s., dsk. ģen. -bju", ".*be", 9,
				null, new Tuple[]{Features.GENDER__FEM}), //acetilsalicilskābe
		SimpleRule.noun("-es, s. dsk. -es, -bju", ".*be", 9,
				null, new Tuple[]{Features.GENDER__FEM}), //astilbe

		SimpleRule.noun("-ļu, s.", ".*les", 9,
				new Tuple[]{Features.ENTRYWORD__PLURAL}, new Tuple[]{Features.GENDER__FEM}), //bailes

		// Miju varianti
		SimpleRule.noun("-es, dsk. ģen. -stu, arī -šu", ".*ste", 9,
				new Tuple[]{Features.GENDER__FEM, Tuple.of(
						Keys.INFLECTION_WEARDNES, "Miju varianti: -stu/-šu")},
				null), //dzeņaukste

		// Bez mijām
		SimpleRule.noun("-es, dsk. ģen. -du", ".*de", 9,
				new  Tuple[] {Features.GENDER__FEM, Features.NO_SOUNDCHANGE},
				null), // diplomande
		SimpleRule.noun("-es, dsk. ģen. -fu", ".*fe", 9,
				new  Tuple[] {Features.GENDER__FEM, Features.NO_SOUNDCHANGE},
				null), //arheogrāfe
		SimpleRule.noun("-es, dsk. ģen. mufu", ".*mufe", 9,
				new Tuple[] {Features.GENDER__FEM, Features.NO_SOUNDCHANGE},
				null), //mufe
		SimpleRule.noun("-es, dsk. ģen. -pu", ".*pe", 9,
				new Tuple[] {Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
				null), // filantrope
		SimpleRule.noun("-es, dsk. ģen. -su", ".*se", 9,
				new  Tuple[] {Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
				null), // bise
		SimpleRule.noun("-es, dsk. ģen. -tu", ".*te", 9,
				new  Tuple[] {Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
				null), // antisemīte
		SimpleRule.noun("-es, dsk. ģen. -zu", ".*ze", 9,
				new  Tuple[] {Features.NO_SOUNDCHANGE, Features.GENDER__FEM},
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
		ComplexRule.noun("-ļa, v.", new Trio[]{
						Trio.of(".*lis", new Integer[]{3}, null),
						Trio.of(".*ls", new Integer[]{5}, null)},
				new Tuple[]{Features.GENDER__MASC}), // acumirklis, bacils, durkls
		ComplexRule.noun("-ņa, v.", new Trio[]{
						Trio.of(".*ņš", new Integer[]{2}, null),
						Trio.of(".*nis", new Integer[]{3}, null),
						Trio.of(".*suns", new Integer[]{5}, null)},
				new Tuple[]{Features.GENDER__MASC}), // abesīnis, dižtauriņš, dzinējsuns
		ComplexRule.noun("-ša, v.", new Trio[]{
						Trio.of(".*[sšt]is", new Integer[]{3}, null),
						Trio.of(".*ss", new Integer[]{5}, null)},
				new Tuple[]{Features.GENDER__MASC}), // abrkasis, lemess
		// Bez mijām
		ComplexRule.noun("-ra, v.", new Trio[]{
						Trio.of(".*rs", new Integer[]{1}, null),
						Trio.of(".*ris", new Integer[]{3}, null)},
				new Tuple[]{Features.GENDER__MASC}), // airis, mūrniekmeistars
		ComplexRule.noun("-sa, v.", new Trio[]{
						Trio.of(".*ss", new Integer[]{1}, null),
						Trio.of(".*sis", new Integer[]{3}, new Tuple[]{Features.NO_SOUNDCHANGE})},
				new Tuple[]{Features.GENDER__MASC}), // balanss, kūrviesis
		ComplexRule.noun("-ta, v.", new Trio[]{
						Trio.of(".*tis", new Integer[]{3}, new Tuple[]{Features.NO_SOUNDCHANGE})},
				new Tuple[]{Features.GENDER__MASC}), // stereotālskatis
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.noun("-a, v.", new Trio[]{
						Trio.of(".*[^aeiouāēīōū]s", new Integer[]{1}, null),
						Trio.of(".*š", new Integer[]{2}, null),
						Trio.of(".*[ģjķr]is", new Integer[]{3}, null)},
				new Tuple[]{Features.GENDER__MASC}), // abats, akustiķis//, sparguļi, skostiņi

		// Daudzkaitlis, vīriešu dzimte
		// Ar mijām
		ComplexRule.noun("-ņu, v.", new Trio[]{
						Trio.of(".*ņi", new Integer[]{1, 2, 3, 4, 5}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),},
				new Tuple[]{Features.GENDER__MASC}), // bretoņi
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.noun("-u, v.", new Trio[]{
						Trio.of(".*(otāji|umi|anti|nieki|[aeiouāēīōū]īdi|isti|mēsli|svārki|plūdi|rati|vecāki|bērni|raksti|vidi|rīti|vakari|vārdi|kapi|augi|svētki|audi|laiki|putni|svari)",
								new Integer[]{1}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*[bcdghklmnpstvz]i", new Integer[]{1}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*(ieši|āņi|ēži|grieži|stāvji|grauži|brunči|viļņi|ceļi|liberāļi|krampji|kaļķi)",
								new Integer[]{3}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*suņi", new Integer[]{5}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*ši", new Integer[]{1, 3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						Trio.of(".*[čļņž]i", new Integer[]{2, 3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						Trio.of(".*(ģ|[mv]j)i", new Integer[]{3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
						Trio.of(".*([ķr]|[aeiāē]j)i", new Integer[]{1, 2, 3, 4}, new Tuple[]{Features.ENTRYWORD__PLURAL, Features.UNCLEAR_PARADIGM}),
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
		ComplexRule.noun("-ņu, s.", new Trio[]{
						Trio.of(".*ne", new Integer[]{9}, null),
						Trio.of(".*ņas", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*nes", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*nis", new Integer[]{11}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
				},
				new Tuple[]{Features.GENDER__FEM}), // acenes, iemaņas, balodene, robežugunis
		ComplexRule.noun("-šu, s.", new Trio[]{
						Trio.of(".*te", new Integer[]{9}, null),
						Trio.of(".*šas", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*[st]es", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*tis", new Integer[]{11}, new Tuple[]{Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // ahajiete, aizkulises, autosacīkstes, klaušas, šķūtis
		ComplexRule.noun("-žu, s.", new Trio[]{
						Trio.of(".*žas", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*[dz]es", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),},
				new Tuple[]{Features.GENDER__FEM}), // mirādes, graizes, bažas
		// Vispārīgā galotne, kas der visam un neder nekam
		ComplexRule.noun("-u, s.", new Trio[]{
						Trio.of(".*a", new Integer[]{7}, null),
						Trio.of(".*[^aeiouāēīōū]as", new Integer[]{7}, new Tuple[]{Features.ENTRYWORD__PLURAL}),
						Trio.of(".*ķes", new Integer[]{9}, new Tuple[]{Features.ENTRYWORD__PLURAL}),},
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
		SimpleRule.noun("-ņa, dsk. ģen. -ņu", ".*ņi", 3,
				new Tuple[]{Features.ENTRYWORD__PLURAL}, new Tuple[]{Features.GENDER__MASC}), //afroamerikāņi
			// konflikts ar "bizmanis"

		// Vissliktākie šabloni - satur tikai vienu galotni un neko citu.
		// Paradigmas: 9, 7 - vienskaitlī un daudzskaitlī
		ComplexRule.noun("-žu", new Trio[]{
						Trio.of(".*[dz]e", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM}),
						Trio.of(".*[dz]es", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM, Features.ENTRYWORD__PLURAL})},
				null), // abioģenēze, ablumozes, akolāde, nematodes
		ComplexRule.noun("-ņu", new Trio[]{
						Trio.of(".*ne", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM}),
						Trio.of(".*nes", new Integer[]{9}, new Tuple[]{Features.GENDER__FEM, Features.ENTRYWORD__PLURAL}),
						Trio.of(".*ņas", new Integer[]{7}, new Tuple[]{Features.GENDER__FEM, Features.ENTRYWORD__PLURAL})},
				null), // agrene, aizlaidnes
		// Paradigma: 3
		SimpleRule.noun("-ņa", ".*nis", 3,
				new Tuple[]{Features.GENDER__FEM}, null), // abolainis
	};
	/**
	 * Šeit ir izdalīti atsevišķi tiešo darbības vārdu likumi, jo tie ir gari,
	 * specifiski un nekonfliktē ar citiem likumiem, tāpēc šos izmēģinās pirmos.
	 * Paradigm 15: Darbības vārdi 1. konjugācija tiešie
	 */
	public static final Rule[] directFirstConjVerb = {
		// Darbības vārdu specifiskie likumi, sakārtoti pa tipiem un alfabētiski
		// pēc nenoteiksmes.
		FirstConjRule.direct("-dullstu, -dullsti,", "-dullst; pag. -dullu", "dullt"), //apdullt
		FirstConjRule.direct("-dulstu, -dulsti,", "-dulst, pag. -dullu", "dult"), //apdult
		FirstConjRule.direct("-kurlstu, -kurlsti,", "-kurlst, pag. -kurlu", "kurlt"), //apkurlt
		FirstConjRule.direct("-saustu, -sausti,", "-saust, pag. -sausu", "saust"), //apsaust
		FirstConjRule.direct("-šņurkstu, -šņurksti,", "-šņurkst, pag. -šņurku", "šņurkt"), //apšņurkt
		FirstConjRule.direct("-trakstu, -traksti,", "-trakst, pag. -traku", "trakt"), //aptrakt
		FirstConjRule.direct("-trulstu, -trulsti,", "-trulst, pag. -trulu", "trult"), //aptrult
		FirstConjRule.direct("-vēstu, -vēsti,", "-vēst, pag. -vēsu", "vēst"), //atvēst
		FirstConjRule.direct("-žirbstu, -žirbsti,", "-žirbst, pag. -žirbu", "žirbt"), // atžirbt

		FirstConjRule.directAllPersParallel(
				"-nīku, -nīc, -nīk, retāk -nīkstu, -nīksti, -nīkst, pag. -niku", "nikt"), //apnikt
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

		SimpleRule.secondConjDirAllPers(
				"-ēju, -ē, -ē, -ējam, -ējat, pag. -ēju, -ējām, -ējāt; pav. -ē, -ējiet", "ēt"), //adverbializēt, anamorfēt
		SimpleRule.secondConjDirAllPers(
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
		FirstConjRule.refl("-bilstos, -bilsties,", "-bilstas, pag. -bildos", "bilsties"), //iebilsties
		FirstConjRule.refl("-bļaujos, -bļaujies,", "-bļaujas, pag. -bļāvos", "bļauties"), //iebļauties
		FirstConjRule.refl("-brēcos, -brēcies,", "-brēcas, pag. -brēcos", "brēkties"), //aizbrēkties
		// C
		FirstConjRule.refl("-cērpos, -cērpies,", "-cērpas, pag. -cirpos", "cirpties"), //apcirpties
		// D
		FirstConjRule.refl("-degos, -dedzies,", "-degas, pag. -degos", "degties"), //aizdegties
		FirstConjRule.refl("-dīcos, -dīcies,", "-dīcas, pag. -dīcos", "dīkties"), //iedīkties
		FirstConjRule.refl("-dūcos, -dūcies,", "-dūcas, pag. -dūcos", "dūkties"), //iedūkties
		// E
		FirstConjRule.refl("-elšos, -elsies,", "-elšas, pag. -elsos", "elsties"), //aizelsties
		// F, G
		FirstConjRule.refl("-gārdzos, -gārdzies,", "-gārdzas, pag. -gārdzos", "gārgties"), //aizgārgties
		FirstConjRule.refl("-grābjos, -grābies,", "-grābjas, pag. -grābos", "grābties"), //iegrābties
		// Ģ,
		FirstConjRule.refl("-ģiedos, -ģiedies,", "-ģiedas, pag. -ģidos", "ģisties"), //apģisties
		// H, I, J, K
		FirstConjRule.refl("-karstos, -karsties,", "-karstas, pag. -karsos", "karsties"), //iekarsties
		FirstConjRule.refl("-kliedzos, -kliedzies,", "-kliedzas, pag. -kliedzos", "kliegties"), //aizkliegties
		FirstConjRule.refl("-krācos, -krācies,", "-krācas, pag. -krācos", "krākties"), //aizkrākties
		FirstConjRule.refl("-kaucos, -kaucies,", "-kaucas, pag. -kaucos", "kaukties"), //iekaukties
		// Ķ
		FirstConjRule.refl("-ķērcos, -ķērcies,", "-ķērcas, pag. -ķērcos", "ķērkties"), //ieķērkties
		// L
		FirstConjRule.refl("-lokos, -locies,", "-lokās, pag. -lakos", "lakties"), //ielakties
		// M
		FirstConjRule.refl("-mirstos, -mirsties,", "-mirstas, pag. -mirsos", "mirsties"), //aizmirsties
		// N, Ņ
		FirstConjRule.refl("-ņirdzos, -ņirdzies,", "-ņirdzas, pag. -ņirdzos", "ņirgties"), //atņirgties
		// O, P, R
		FirstConjRule.refl("-reibstos, -reibsties,", "-reibstas, pag. -reibos", "reibties"), //iereibties
		FirstConjRule.refl("-rūcos, -rūcies,", "-rūcas, pag. -rūcos", "rūkties"), //aizrūkties
		// S
		FirstConjRule.refl("-snaužos, -snaudies,", "-snaužas, pag. -snaudos", "snausties"), //aizsnausties
		FirstConjRule.refl("-svelpjos, -svelpies,", "-svelpjas, pag. -svelpos", "svelpties"), //aizsvelpties
		FirstConjRule.refl("-svilpjos, -svilpies,", "-svilpjas, pag. -svilpos", "svilpties"), //aizsvilpties
		FirstConjRule.refl("-svilstos, -svilsties,", "-svilstas, pag. -svilos", "svilties"), //aizsvilties
		// Š
		FirstConjRule.refl("-šņācos, -šņācies,", "-šņācas, pag. -šņācos", "šņākties"), //aizšņākties
		// T
		FirstConjRule.refl("-topos, -topies,", "-topas, pag. -tapos", "tapties"), //attapties
		// U, V
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
		RegularVerbRule.secondConjRefl("-inos, -inies,", "-inās, pag. -inājos", "ināties"), //atspirināties

		RegularVerbRule.secondConjRefl3Pers("-ējas, pag. -ējās", "ēties"), //absorbēties
		RegularVerbRule.secondConjRefl3Pers("-ojas, pag. -ojās", "oties"), //daudzkāršoties

		SimpleRule.secondConjReflAllPers(
				"-ējos, -ējies, -ējas, -ējamies, -ējaties, pag. -ējos, -ējāmies, -ējāties; pav. -ējies, -ējieties",
				"ēties"), //adverbiēties
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
				"-drebos, -drebies,", "-drebas, pag. -drebējos", "drebēties", false), //iedrebēties
		RegularVerbRule.thirdConjRefl(
				"-drīkstos, -drīksties,", "-drīkstas, pag. -drīkstējos", "drīkstēties", false), //iedrīkstēties
		RegularVerbRule.thirdConjRefl(
				"-dusos, -dusies,", "-dusas, pag. -dusējos", "dusēties", false), //atdusēties
		RegularVerbRule.thirdConjRefl(
				"-dziedos, -dziedies,", "-dziedas, pag. -dziedājos", "dziedāties", false), //aizdziedāties
		// E, F, G
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
				"-murkšos, -murkšies,", "-murkšas, pag. -murkšējos", "murkšēties", false), //iemurkšēties
		RegularVerbRule.thirdConjRefl(
				"-murkšķos, -murkšķies,", "-murkšķas, pag. -murkšķējos", "murkšķēties", false), //iemurkšķēties
		// N, Ņ
		RegularVerbRule.thirdConjRefl(
				"-ņerkstos, -ņerksties,", "-ņerkstas, pag. -ņerkstējos", "ņerkstēties", false), //ieņerkstēties
		RegularVerbRule.thirdConjRefl(
				"-ņurdos, -ņurdies,", "-ņurdas, pag. -ņurdējos", "ņurdēties", true), //ieņurdēties
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
				"-svinos, -svinies,", "-svinas, pag. -svinējos", "svinēties", false), //aizsvinēties
		// Š
		RegularVerbRule.thirdConjRefl(
				"-šņukstos, -šņuksties,", "-šņukstas, pag. -šņukstējos", "šņukstēties", false), //aizšņukstēties
		// T, U, V, Z

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
			ComplexRule.secondThirdConjReflAllPers(
					"-ījos, -ījies, -ījas, arī -os, -ies, -ās, pag. -ījos", ".*īties", false), // blēdīties

			// Darbības vārdu specifiskie likumi.
			// Nav.
	};
}
