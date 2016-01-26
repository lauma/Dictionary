package lv.ailab.dict.tezaurs.checker;

/**
 * Pārbaudes, kuras jāizdara pirms galvenā pārbaužu bloka. Tikai pozitīvs šo
 * pārbaužu iznākums noved pie galvenā bloka sākšanas.
 * Vienīgie pieļaujamie metožu blakusefekti: izmaiņas slikto šķirkļu masīvā.
 * 2015-06-26.
 *
 * @author Lauma
 */
public class EntryPreChecks
{
	/**
	 * Pārbaude, vai šķirklis nav tukšs.
	 */
	public static boolean isNotEmpty(Dictionary dict, int entryIndex)
	{
		Dictionary.Entry entry = dict.entries[entryIndex];
		if (entry.fullText.trim().equals(""))
		{
			dict.bad.addNewEntry(entry, "Tukša rinda");
			return false;
		}
		return true;
	}
	/**
	 * Pārbaude, vai šķirklim netrūkst sķirkļa vārda.
	 */
	public static boolean hasHeaderWord(Dictionary dict, int entryIndex)
	{
        Dictionary.Entry entry = dict.entries[entryIndex];
		if(entry.name.equals(""))
		{
			dict.bad.addNewEntry(entry, "Trūkst šķirkļa vārda");
			return false;
		}
		if (!entry.fullText.contains(" ")) // Šis normāli neizpildās, ja nav kļūdas Entry konstruktorā?
		{
			dict.bad.addNewEntry(entry, "Trūkst šķirkļa vārda / šķirklī nav nevienas atstarpes");
			return false;
		}
		return true;
	}
}
