package lv.ailab.dict.tezaurs.analyzer.flagconst;

/**
 * Populārākās karodziņu vērtības - iznestas šeit, lai mazinātu pārakstīšanās
 * risku.
 *
 * Visas tekstuālās vērtību konstantes drīkst definēt tikai šeit un AbbrMap
 * klasē - citās klasēs jālieto atsauces uz Values klasē definētajām, lai kaut
 * ko pamainot, nav jadzenās pakaļ pa daudzām klasēm.
 *
 * @author Lauma
 */
public enum Values
{
	NOUN("Lietvārds"),
	REFLEXIVE_NOUN("Atgriezeniskais lietvārds"),
	PROPER("Īpašvārds"),
	GEN_ONLY("Ģenetīvenis"),
	ADJECTIVE("Īpašības vārds"),
	NUMERAL("Skaitļa vārds"),
	ORDINAL_NUMERAL("Kārtas skaitļa vārds"),
	CARDINAL_NUMERAL("Pamata skaitļa vārds"),
	FRACTIONAL_NUMERAL("Daļu skaitļa vārds"),

	// Prievārdu nosaukumi pamatā no LLVV.
	PRONOUN("Vietniekvārds"),
	PERSONAL_PRONOUN("Personas vietniekvārds"),
	POSSESIVE_PRONOUN("Piederības vietniekvārds"),
	DEMONSTRATIVE_PRONOUN("Norādāmais vietniekvārds"),
	DEFINITE_PRONOUN("Noteicamais vietniekvārds"),
	INDEFINITE_PRONOUN("Nenoteiktais vietniekvārds"),
	GENRERIC_PRONOUN("Vispārināmais vietniekvārds"),
	REFLEXIVE_PRONOUN("Atgriezeniskais vietniekvārds"),
	INTERROGATIVE_PRONOUN("Jautājamais vietniekvārds"),
	NEGATIVE_PRONOUN("Noliedzamais vietniekvārds"),

	VERB("Darbības vārds"),
	PREFIX_VERB("Ddarbības vārds ar priedēkli"),
	PREFIXLESS_VERB("Darbības vārds bez priedēkļa"),
	NEGATIVE_VERB("Noliegts darbības vārds"),

	ADVERB("Apstākļa vārds"),
	PLACE_ADVERB("Vietas apstākļa vārds"),
	INTEROGATIVE_ADVERB("Jautājamais apstākļa vārds"),

	INTERJECTION("Izsauksmes vārds"),
	CONJUNCTION("Saiklis"),
	PARTICLE("Partikula"),
	INTERROGATIVE_PARTICLE("Jautājamā partikula"),

	PREPOSITION("Prievārds"),

	ABBREVIATION("Saīsinājums"),
	FOREIGN("Vārds svešvalodā"),
	PIECE_OF_WORD("Vārda daļa"),
	PREFIX("Priedēklis"),
	PREFIX_FOREIGN("Svešvārdu priedēklis"),
	COMPOUND_PIECE("Salikteņu daļa"),
	COMPOUND_FIRST_PIECE("Salikteņu pirmā daļa"),
	COMPOUND_LAST_PIECE("Salikteņu pēdējā daļa"),

	PHRASE("Vārdu savienojums"),

	PARTICIPLE("Divdabis"),
	PARTICIPLE_OSS("Lokāmais darāmās kārtas tagadnes divdabis (-ošs, -oša)"),
	PARTICIPLE_IS("Lokāmais darāmās kārtas pagātnes divdabis (-is, -usi, -ies, -usies)"),
	PARTICIPLE_AMS("Lokāmais ciešamās kārtas tagadnes divdabis (-ams, -ama, -āms, -āma)"),
	PARTICIPLE_TS("Lokāmais ciešamās kārtas pagātnes divdabis (-ts, -ta)"),
	PARTICIPLE_DAMS("Daļēji lokāmais divdabis (-dams, -dama, -damies, -damās)"),
	PARTICIPLE_OT("Nelokāmais divdabis (-ot, -oties)"),

	PLACE_NAME("Vietvārds"),
	PERSON_NAME("Personvārds"),
	HISTORICAL("Vēsturisks"),
	HISTORICAL_PLACE("Vēsturisks vietvārds"),
	HISTORICAL_PERSON("Vēsturisks personvārds"),
	DIALECTICISM("Dialektisms"),
	REGIONAL_TERM("Apvidvārds"),

	SINGULAR("Vienskaitlis"),
	DUAL("Divskaitlis"),
	PLURAL("Daudzskaitlis"),

	FEMININE("Sieviešu dzimte"),
	MASCULINE("Vīriešu dzimte"),
	COGENDER("Kopdzimte"),

	NOMINATIVE("Nominatīvs"),
	GENITIVE("Ģenitīvs"),
	DATIVE("Datīvs"),
	ACUSATIVE("Akuzatīvs"),
	INSTRUMENTAL("Instrumentālis"),
	LOCATIVE("Lokatīvs"),

	NON_INFLECTIVE("Nelokāms vārds"),

	INFINITIVE("Nenoteiksme"),
	THIRD_PERSON("3. persona"),
	PERSON_FORM("Personas forma"), // Tipiski darbības vārdiem
	PASSIVE_VOICE("Ciešamā kārta"), // Ciešamās kārtas forma
	IMPERATIVE("Pavēles izteiksme"), // Pavēles izteiksmes forma

	MAIN_VERB("Patstāvīgs darbības vārds"),

	COMPARATIVE_DEGREE("Pārākā pakāpe"),
	SUPERLATIVE_DEGREE("Vispārākā pakāpe"),

	DEFINITE_ENDING("Noteiktā galotne"),

	ATTRIBUTE("Apzīmētājs"),
	//ADVERBIAL_MOD("Apstāklis"),
	//ADVERBIAL_PLACE_MOD("Vietas apstāklis"),

	ORIGINAL_NEEDED("Oriģinālā gramatika satur karodziņos neatšifrētas nianses"), // Papildinformācija pārejai notekstuālajām gramatikām uz karodziņiem.

	CHANGED_PARADIGM("Cita paradigma"), // Paredzēts morfoimporta atvieglošanai
	UNCLEAR_PARADIGM("Neviennozīmīga paradigma"), // Paredzēts morfoimporta atvieglošanai
	UNCLEAR_POS("Neviennozīmīga vārdšķira vai kategorija"), // Paredzēts morfoimporta atvieglošanai

	PARALLEL_FORMS("Paralēlās formas"), // Paredzēts morfoimporta atvieglošanai, šobrīd pamatā darbības vārdiem
	FROZEN_FORM("Sastingusi forma"), // Paredzēts morfoimporta atvieglošanai, cerams, ka pabeidzot darbu ar tēzauru, šis vairs nebūs vajadzīgs
	FIRST_CONJ_PARALLELFORM("1. konjugācijas paralēlforma"), // Paredzēts morfoimporta atvieglošanai, tikai 2. un 3. konj. darbības vārdiem
	INFINITIVE_HOMOFORMS("Nenoteiksmes homoformas"), // Paredzēts morfoimporta atvieglošanai
	HAS_PRESENT_SOUNDCHANGE("Tagadnes mija ir"), // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	OPT_PRESENT_SOUNDCHANGE("Fakultatīva tagadnes mija"), // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	NO_PRESENT_SOUNDCHANGE("Nav tagadnes mijas"), // Paredzēts morfoimporta atvieglošanai, tikai 3. konj. Vai šo vajadzētu pārveidot par noklusēto variantu?
	NO_SOUNDCHANGE("Nav mijas"), // Paredzēts morfoimporta atvieglošanai, gadījumiem, kad izņēmuma kārtā vispār nav attiecīgajai paradigmai raksturīgās mijas
	OPT_SOUNDCHANGE("Fakultatīva mija"), // Paredzēts morfoimporta atvieglošanai, gadījumiem, kad izņēmuma kārtā lietojama gan locīšana ar paradigmai raksturīgo miju, gan bez tās

	// Sevišķi retās vērtības:

	//// Galotņu šabloniem.
	NOT_PRESENT_FORMS("Nelieto tagadnes formas"),

	//// "Savienojumā ar" apstrādei.
	ILLNESS_NAME("Slimības izraisītāja mikroorganisma, arī slimības nosaukums"),
	TIME_UNIT_NAME("Laika mērvienības nosaukums"),
	CLOTHING_UNIT_NAME("Apģērba gabala nosaukums"),
	TEACHING_SUBJECT_NAME("Mācību priekšmeta nosaukums"),
	DIMENSION_NAME("Dimensiju apzīmējums"),
	MULTI_TINGY_NAME("Daudzskaitlinieks vai pāra priekšmetu apzīmējums"), // Līdz šim nekur citur nevajag daudzskaitlieku kā vērtību, mēģināšu izšmaukt arī te.
	PERSON_TERM("Vārds, kas apzīmē personu"), // Nav tas pats, kas personvārds - šeit iederās arī "cilvēks" un "muļķis".
	NEGATIVE_PERSON_TERM("Vārds, kas nosauc personu ar negatīvu īpašību"),
	ADVERBIAL_TERM("Daudzuma, masas, lieluma vai ilguma apzīmējums"),
	TIME_TERM("Vārds ar laika nozīmi"),
	DISTRUCTION_NAME("Vārds, kas apzīmē bojāšanu, iznīcināšanu"),
	//VERB_WITH_DERIVS("Dotais darbības vārds un tā atvasinājumi"),
	// Slikti, ka nākamos trijus arī vajag :/
	NOUN_CONTAMINATION("Lietvārda nozīmē lietots vārds"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	NOUN_CONTAMINATION_ADJ_COMP("Lietvārda nozīmē lietots īpašības vārds ar pārāko pakāpi"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	ADVERB_CONTAMINATION("Apstākļa vārda nozīmē lietots vārds"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	PLACE_ADVERB_CONTAMINATION("Vietas apstākļa vārda nozīmē lietots vārds"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	REPETITION("Atkārtots vienas un tās pašas saknes vārds"),
	DATIVE_AND_ADVERB("Vārds datīvā un apstākļa vārds"), // Iesaistīšanās divu nosacījumu grupā.
	ADVERB_AND_NEGVERB("Apstākļa vārds un noliegts darbības vārds"), // Iesaistīšanās divu nosacījumu grupā.
	NUMERAL_AND_ADVERB("Skaitļa vārds un apstākļa vārds"), // Iesaistīšanās divu nosacījumu grupā.
	NUMERAL_AND_ADJECTIVE("Skaitļa vārds un īpašības vārds"), // Iesaistīšanās divu nosacījumu grupā.
	NOUN_WITH_PREPOSITION("Lietvārds un prievārds"), // Iesaistīšanās divu nosacījumu grupā, līdzīg lietvārda locījumam.
	PLURAL_OR_THIRD_PERS("Daudzskaitlis vai 3. persona"), // Iesaistīšanās divu nosacījumu grupā, līdzīg lietvārda locījumam.

	;

	public String s;

	Values(String name)
	{
		this.s = name;
	}

	public String toString()
	{
		return s;
	}
}
