/**
 * Kopējā vārdnīcas apstrādes paka.
 */
package lv.ailab.dict.tezaurs.checker;

import lv.ailab.dict.io.DocLoader;
import lv.ailab.dict.tezaurs.TezDocChecker;
import lv.ailab.dict.tezaurs.io.XlsOutputer;
import lv.ailab.dict.utils.StringUtils;
import lv.ailab.dict.utils.Trio;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;


//bibliotēka *.doc failu apstrādei


/**
 * Dati par vienu vārdnīcas failu.
 * @author Lauma
 */
public class DictionaryFragment
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
	public static DictionaryFragment loadFromFile(String path, ReferenceList references)
	throws IOException
	{
        DictionaryFragment dict = new DictionaryFragment();
        dict.prevIN = new HashMap<>();
        dict.bad = new BadEntries();
        dict.stats = new Stats();
        dict.references = references;

		// Ielādē šķirkļus.
		String[] entryTexts = DocLoader.loadDoc(path);
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
	public void check()
	{
		for (int i = 0; i < entries.length; i++)
		{
			DictionaryFragment.Entry entry = entries[i];
			try
			{
				stats.wordCount += StringUtils.wordCount(entry.fullText);
				stats.entryCount++;
				// Pārbauda, vai rinda nav tukša, un, ja ir, tad veic tālāku analīzi.
				if (EntryPreChecks.isNotEmpty(this, i) && EntryPreChecks
						.hasHeaderWord(this, i))
				{
					//Metode statistikas datu par šķirkli ievākšanai
					stats.collectInnerStats(entry.fullText);

					// Pārbauda, vai šķirklis nav izņēmums, un ja nav, tad veic
					// pārbaudes.
					if (!ExceptionList.isException(entry))
					{
						Method[] tests = EntryChecks.class.getDeclaredMethods();
						for (Method test : tests)
							test.invoke(null, this, i);
					}
				}

				// Papildina sastapto šķirkļu un indeksu "datubāzi".
				int index = -1;
				if (entry.contents.matches("^IN\\s.*$"))
				{
					String bezIn = entry.contents.substring(3).trim();
					index = StringUtils.findNumber(bezIn);
				}
				prevIN.put(entry.name, Trio.of(index, entry.fullText, entry.id));
			} catch (Exception e)
			{
				bad.addNewEntryFromString(i, entry.fullText, "Pilnīgi negaidīta izņēmumsituācija, visticamāk algoritmiska apstrādes kļūda");
				System.out.println("Ir radusies pilnīgi negaidīta izņēmumsituācija, visticamāk algoritmiska");
				System.out.println("apstrādes kļūda!");
				System.out.println("To izraisa šī rindkopa:");
				System.out.println(entry.fullText);
				System.out.println("Izņēmumsituācijas apraksts:");
				e.printStackTrace();
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
		if (!bad.isEmpty()) bad.printAll(TezDocChecker.inputDataPath + part1 + ".klu");
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
			// Noņem no beigām enterus, bet atstāj pārējos tukšumus, jo uz
			// citiem tukšumiem vēlāk ir jāpārbauda.
			while (fullEntry.endsWith("\n") || fullEntry.endsWith("\r"))
				fullEntry = fullEntry.substring(0, fullEntry.length() - 1);
			fullText = fullEntry;
			if (fullText.contains(" "))
			{
				name = fullText.substring(0, fullText.indexOf(' '));
				contents = fullText.substring(fullText.indexOf(' ') + 1);
			} else
			{
				name = "";
				contents = fullText;
			}
		}
		
	}
	
	
}
