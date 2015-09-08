/**
 * Kopējā vārdnīcas apstrādes paka.
 */
package lv.ailab.tezaurs.checker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;


import lv.ailab.tezaurs.DictionaryCheckerUI;
import lv.ailab.tezaurs.io.DocLoader;
import lv.ailab.tezaurs.io.XlsOutputer;
import lv.ailab.tezaurs.utils.StringUtils;
import lv.ailab.tezaurs.utils.Trio;


//bibliotēka *.doc failu apstrādei


/**
 * Dati par vienu vārdnīcas failu.
 * @author Lauma
 */
public class Dictionary
{
	/**
	 * Šķirkļu teksti.
	 */
	public Entry [] entries;

    /**
     * Šķirkļu vārdi.
     */
    public HashSet<String> entryNames;
    /**
     * Avotu saraksts.
     */
    public ReferenceList references;
	/**
	 * Kļūdainie šķirkļi
	 */
	public BadEntries bad;
	/**
	 * Attēlojums no šķirkļa kārtas numura uz IN indeksu (vai -1, ja IN indeksa
	 * nav), šķirkļa pilno tekstu un rindas numuru vārdnīcā.
	 */
	public HashMap<String, Trio<Integer, String, Integer>> prevIN;
	
	/**
	 * Apstrādājamā faila vārds - papildinformācija, kas palīdz identificēt
	 * vārdnīcas failu konsoles izdrukās.
	 */
	public String fileName;
	
	/**
	 * Statistics collector.
	 */
	public Stats stats;

	/**
	 * Ielādē no word faila vārdnīcas fragmenta saturu un sagatavo pārbaudēm.
	 */
	public static Dictionary loadFromFile(String path, ReferenceList references)
	throws IOException
	{
        Dictionary dict = new Dictionary();
        dict.prevIN = new HashMap<>();
        dict.bad = new BadEntries();
        dict.stats = new Stats();
        dict.references = references;

		// Ielādē šķirkļus.
		String[] entryTexts = DocLoader.loadDictionary(path);
        dict.entries = new Entry[entryTexts.length];
        dict.entryNames = new HashSet<>();
        for (int i = 0; i < entryTexts.length; i++)
        {
            dict.entries[i] = new Entry(entryTexts[i], i);
            dict.entryNames.add(dict.entries[i].name);
        }
		// Savāc faila nosaukumu.
		dict.fileName = path;
		if (dict.fileName.contains("\\"))
			dict.fileName = dict.fileName.substring(dict.fileName.lastIndexOf('\\') + 1);
		if (dict.fileName.contains("/"))
			dict.fileName = dict.fileName.substring(dict.fileName.lastIndexOf('/') + 1);
        return dict;
	}
	
	/**
	 * Izpilda visas šķirkļu pārbaudes visiem šķirkļiem.
	 */
	public void check() throws InvocationTargetException, IllegalAccessException
    {
		for(int i=0; i < entries.length; i++)
		{
			//pārbaude vai šķirklis nav tukšs
			if(!StringUtils.isEntryEmpty(this, i))
			{
				Dictionary.Entry entry = entries[i];
				stats.wordCount += StringUtils.wordCount(entry.fullText);
				stats.entryCount++;

				// pārbaude vai šķirkļa vārds ir labs
				if(EntryPreChecks.isEntryNameGood(this, i))
				{
					//Metode statistikas datu par šķirkli ievākšanai
					stats.collectStats(entry.fullText);
					//paŗabaude vai nav izņēmums
					if(!ExceptionList.isException(entry))
					{
                        Method[] tests = EntryChecks.class.getDeclaredMethods();
                        for (Method test : tests)
                            test.invoke(null, this, i);

                        // Papildina sastapto šķirkļu un indeksu "datubāzi".
                        int index = -1;
						if (entry.contents.matches("^IN\\s.*$"))
                        {
                            String bezIn = entry.contents.substring(3).trim();
                            index = StringUtils.findNumber(bezIn);
                        }
                        prevIN.put(entry.name, Trio.of(index, entry.fullText, entry.id));
					}
				}
			}
            // Progresa izvade uz ekrāna
			if (i % 50 == 0)
			{
				System.out.print(fileName + " [");
				System.out.printf("%.1f", ((float) i) / entries.length * 100);
				System.out.print("%]\r");
			}
		}
		System.out.print(fileName + " [...]\r"); // Progresa izvade uz ekrāna
		GlobalChecks.singleIn1Check(this);
		System.out.print(fileName + " [Pabeigts]\t\t\t\n"); //izvade uz ekrāna kad pabeigts fails

	}
	
	/**
	 * Izdrukā pārbaužu rezultātus.
	 */
	public void printResults(XlsOutputer table) throws IOException
	{
		// Izejas pluusma.
		String[] parts = fileName.split("\\.");
		String part1 = parts[0];
		// Statistikas datu ielikšana tabulā.
		table.addNewStatsRow(stats, fileName);
		table.sumTable();
		table.flush();
		// Kļūdaino šķirkļu izdrukāšana .klu failā.
		if (!bad.isEmpty()) bad.printAll(DictionaryCheckerUI.inputDataPath + part1 + ".klu");
	}
	
	/**
	 * Dati par vienu vārdnīcas šķirkli
	 * @author Lauma
	 *
	 */
	public static class Entry
	{
		/**
		 * Šķirkļa vārds.
		 */
		public final String name;
		/**
		 * Šķirkļa saturs, kas nav šķirkļa vārds.
		 */
		public final String contents;
		/**
		 * Pilnais šķirkļa teksts, kāds nu tas nāk no vārdnīcas
		 */
		public final String fullText;
		/**
		 * Kārtas numurs vārdnīcas failā (sākot no 0).
		 */
		public final int id;
		
		public Entry (String fullEntry, int entryID)
		{
			id = entryID;
			fullText = fullEntry.trim();
			if (fullText.contains(" "))
			{
				name = fullText.substring(0, fullText.indexOf(' '));
				contents = fullText.substring(fullText.indexOf(' ')).trim();
			} else
			{
				name = "";
				contents = fullText;
			}
		}
		
	}
	
	
}
