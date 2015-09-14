package lv.ailab.tezaurs.checker;

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
		return true;
	}
}
