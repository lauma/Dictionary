package lv.ailab.dict.tezaurs.checker;

import lv.ailab.dict.utils.Trio;

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
	public static void singleIn1Check(DictionaryFragment dict)
	{
		for (String entryName : dict.prevIN.keySet())
		{
			Trio<Integer, String, Integer> entry = dict.prevIN.get(entryName);
			if (entry.first == 1)
				dict.bad.addNewEntryFromString(entry.third, entry.second, "Vientuļš IN 1");
		}
	}
}
