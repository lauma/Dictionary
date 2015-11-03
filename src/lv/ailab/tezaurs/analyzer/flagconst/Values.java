package lv.ailab.tezaurs.analyzer.flagconst;

/**
 * Populārākās karodziņu vērtības - iznestas šeit, lai mazinātu pārakstīšanās
 * risku.
 *
 * @author Lauma
 */ // TODO papildināt
public enum Values
{
	SINGULAR("Vienskaitlis"),
	PLURAL("Daudzskaitlis"),

	FEMININE("Sieviešu dzimte"),
	MASCULINE("Vīriešu dzimte"),
	COGENDER("Kopdzimte"),

	GENITIVE("Ģenitīvs"),
	DATIVE("Datīvs"),
	ACUSATIVE("Akuzatīvs"),
	LOCATIVE("Lokatīvs"),

	NON_INFLECTIVE("Nelokāms vārds"),

	NOUN("Lietvārds"),
	REFL_NOUN("Atgriezeniskais lietvārds"),
	ADJECTIVE("Īpašības vārds"),
	NUMERAL("Skaitļa vārds"),
	PRONOUN("Vietniekvārds"),
	VERB("Darbības vārds"),

	FOREIGN("Vārds svešvalodā"),

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

	THIRD_PERSON("3. persona"),

	CHANGED_PARADIGM("Cita paradigma"), // Paredzēts morfoimporta atvieglošanai
	UNCLEAR_PARADIGM("Neviennozīmīga paradigma"), // Paredzēts morfoimporta atvieglošanai

	PARALLEL_FORMS("Paralēlās formas"), // Paredzēts morfoimporta atvieglošanai, šobrīd tikai darbības vārdiem
	INFINITIVE_HOMOFORMS("Nenoteiksmes homoformas"), // Paredzēts morfoimporta atvieglošanai
	HAS_PRESENT_SOUNDCHANGE("Tagadnes mija ir"), // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	NO_PRESENT_SOUNDCHANGE("Tagadnes mijas nav"), // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	NO_SOUNDCHANGE("Mijas nav"), // Paredzēts morfoimporta atvieglošanai, gadījumiem, kad izņēmuma kārtā nav mijas

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
