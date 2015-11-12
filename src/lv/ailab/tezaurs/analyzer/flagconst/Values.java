package lv.ailab.tezaurs.analyzer.flagconst;

/**
 * Populārākās karodziņu vērtības - iznestas šeit, lai mazinātu pārakstīšanās
 * risku.
 * Lielais mērķis ir panākt, ka visas tekstuālās vērtību konstantes ir tikai
 * šeit un AbbrMap klasē, lai kaut ko pamainot, nav jadzenās pakaļ pa daudzām
 * klasēm.
 *
 * @author Lauma
 */ // TODO papildināt
public enum Values
{
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
	LOCATIVE("Lokatīvs"),

	NON_INFLECTIVE("Nelokāms vārds"),

	NOUN("Lietvārds"),
	REFL_NOUN("Atgriezeniskais lietvārds"),
	PROPER("Īpašvārds"),
	ADJECTIVE("Īpašības vārds"),
	NUMERAL("Skaitļa vārds"),
	ORDINAL_NUMERAL("Kārtas skaitļa vārds"),
	CARDINAL_NUMERAL("Pamata skaitļa vārds"),

	PRONOUN("Vietniekvārds"),
	PERSONAL_PRONOUN("Personas vietniekvārds"),
	DEMONSTRATIVE_PRONOUN("Norādāmais vietniekvārds"),
	INDEFINITE_PRONOUN("Nenoteiktais vietniekvārds"),
	GENRERIC_PRONOUN("Vispārināmais vietniekvārds"),

	VERB("Darbības vārds"),
	PREFIX_VERB("Ddarbības vārds ar priedēkli"),
	PREFIXLESS_VERB("Darbības vārds bez priedēkļa"),
	NEGATIVE_VERB("Noliegts darbības vārds"),

	ADVERB("Apstākļa vārds"),
	PLACE_ADVERB("Vietas apstākļa vārds"),

	INTERJECTION("Izsauksmes vārds"),
	CONJUNCTION("Saiklis"),
	PARTICLE("Partikula"),
	PREPOSITION("Prievārds"),

	ABBREVIATION("Saīsinājums"),
	FOREIGN("Vārds svešvalodā"),
	PART_OF_WORD("Vārda daļa"),
	PREFIX("Priedēklis"),
	COMPOUND_PART("Salikteņu daļa"),
	COMPOUND_FIRST_PART("Salikteņu pirmā daļa"),
	COMPOUND_LAST_PART("Salikteņu pēdējā daļa"),

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

	ATTRIBUTE("Apzīmētājs"),
	//ADVERBIAL_MOD("Apstāklis"),
	//ADVERBIAL_PLACE_MOD("Vietas apstāklis"),

	HISTORICAL("Vēsturisks"),

	HISTORICAL_PLACE("Vēsturisks vietvārds"),
	HISTORICAL_PERSON("Vēsturisks personvārds"),

	THIRD_PERSON("3. persona"),
	PERSON_FORM("Personas forma"), // Tipiski darbības vārdiem
	PASSIVE_VOICE("Ciešamā kārta"), // Ciešamās kārtas forma
	MAIN_VERB("Patstāvīgs darbības vārds"),

	INFINITIVE("Nenoteiksme"),

	COMPARATIVE_DEGREE("Pārākā pakāpe"),
	SUPERLATIVE_DEGREE("Vispārākā pakāpe"),

	DEFINITE_ENDING("Noteiktā galotne"),

	ORIGINAL_NEEDED("Oriģinālā gramatika satur pārāk specifiskas nianses"), // Papildinformācija pārejai notekstuālajām gramatikām uz karodziņiem.

	CHANGED_PARADIGM("Cita paradigma"), // Paredzēts morfoimporta atvieglošanai
	UNCLEAR_PARADIGM("Neviennozīmīga paradigma"), // Paredzēts morfoimporta atvieglošanai

	PARALLEL_FORMS("Paralēlās formas"), // Paredzēts morfoimporta atvieglošanai, šobrīd tikai darbības vārdiem
	INFINITIVE_HOMOFORMS("Nenoteiksmes homoformas"), // Paredzēts morfoimporta atvieglošanai
	HAS_PRESENT_SOUNDCHANGE("Tagadnes mija ir"), // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	NO_PRESENT_SOUNDCHANGE("Tagadnes mijas nav"), // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	NO_SOUNDCHANGE("Mijas nav"), // Paredzēts morfoimporta atvieglošanai, gadījumiem, kad izņēmuma kārtā nav mijas

	// Sevišķi retās vērtības.
	// "Savienojumā ar" apstrādei.
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
	// Slikti, ka nākamos trijus arī vajag :/
	NOUN_CONTAMINATION("Lietvārda nozīmē lietots vārds"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	NOUN_CONTAMINATION_ADJ_COMP("Lietvārda nozīmē lietots īpašības vārds ar pārāko pakāpi"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	ADVERB_CONTAMINATION("Apstākļa vārda nozīmē lietots vārds"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	PLACE_ADVERB_CONTAMINATION("Vietas apstākļa vārda nozīmē lietots vārds"), // Šo lieto tikai tur, kur nevar likt "kontaminācija=lietvārds"
	REPETITION("Atkārtots vienas un tās pašas saknes vārds"),
	DATIVE_AND_ADVERB("Vārds datīvā un apstākļa vārds"), // Iesaistīšanās divu nosacījumu grupā.
	ADVERB_AND_NEGVERB("Apstākļa vārds un noliegts darbības vārds"), // Iesaistīšanās divu nosacījumu grupā.

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
