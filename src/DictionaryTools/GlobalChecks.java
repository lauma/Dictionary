package DictionaryTools;

import java.util.ArrayList;
import java.util.Map;

/**
 * Struktūras pārbaudes, kas attiecināmas uz vairākiem šķirkļiem.
 * @author Lauma
 *
 */
public class GlobalChecks
{
	/**
	 * Atlasa atlasa vientuļos IN 1.
	 */
	public static void singleIn1Check(Map<String, Trio<Integer, String, Integer>> finalIN, BadEntries bad)
	{
		for (String entryName : finalIN.keySet())
		{
			Trio<Integer, String, Integer> entry = finalIN.get(entryName);
			if (entry.first == 1)
				bad.addNewEntry(entry.third, entry.second, "Vientuļš IN 1");
		}
	}
}
