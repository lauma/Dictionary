package lv.ailab.tezaurs.analyzer.flagconst;

/**
 * Karodziņu grupas.
 *
 * @author Lauma
 */
public enum Keys
{
	POS("Vārdšķira"),
	LANGUAGE("Valoda"),
	USED_TOGETHER_WITH("Lieto kopā ar"),
	DOMAIN("Joma"),
	USAGE_RESTRICTIONS("Lietojuma ierobežojumi"),
	DIALECT_FEATURES("Dialekta iezīmes"),
	CONTAMINATION("Kontaminācija"),
	USUALLY_USED_IN_FORM("Parasti lieto noteiktā formā"),
	OFTEN_USED_IN_FORM("Bieži lieto noteiktā formā"),
	USED_ONLY_IN_FORM("Lieto tikai noteiktā formā"),
	ALSO_USED_IN_FORM("Lieto arī noteiktā formā"),
	USED_IN_FORM("Lieto noteiktā formā"),
	USAGE_FREQUENCY("Lietojuma biežums"),
	NUMBER("Skaitlis"),
	GENDER("Dzimte"),
	CASE("Locījums"),
	ENTRYWORD_WEARDNES("Šķirkļavārda īpatnības"), // Paredzēts morfoimporta atvieglošanai
	INFLECTION_WEARDNES("Locīšanas īpatnības"), // Paredzēts morfoimporta atvieglošanai
	INFLECT_AS("Locīt kā"), // Paredzēts morfoimporta atvieglošanai 1. konjugācijai
	OTHER_FLAGS("Citi"), // Binārie "jā/nē" karodziņi
	;

	public String str;

	Keys(String name)
	{
		this.str = name;
	}

	public String toString()
	{
		return str;
	}
}
