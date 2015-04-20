/**
 * Kopējā vārdnīcas apstrādes paka.
 */
package lv.ailab.tezaurs.checker;

import java.io.IOException;
import java.util.HashMap;


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
	public String [] entries;
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
	public void loadFromFile(String path)
	throws IOException
	{
		// Ielādē šķirkļus.
		entries = DocLoader.loadDictionary(path);
		
		// Savāc faila nosaukumu.
		fileName = path;
		if (fileName.contains("\\"))
			fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
		if (fileName.contains("/"))
			fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		
		// Notīra uzkrājošos statistikas mainīgos.
		prevIN = new HashMap<String, Trio<Integer, String, Integer>>();
		bad = new BadEntries();
		stats = new Stats();
	}
	
	/**
	 * Izpilda visas šķirkļu pārbaudes.
	 */
	public void check (ReferenceList references)
	{
		float progress = 0;
		for(int i=0; i < entries.length; i++)
		{
			//pārbaude vai šķirklis nav tukšs
			if(!StringUtils.isEntryEmpty(this, i))
			{
				//progress = (((float)i/EntryLen)*100);
				stats.wordCount += StringUtils.wordCount(entries[i]);
				stats.entryCount++;
				//šķirkļa informācijas ieguve
				Dictionary.Entry entry = new Dictionary.Entry(entries[i], i);
				//String entryInf = entries[i].substring(entries[i].indexOf(" ")).trim();
				//šķirkļa vārda ieguve
				//String entryName = entries[i].substring(0, entries[i].indexOf(" ")).trim();
				// pārbaude vai šķirkļa vārds ir labs
				if(EntryChecks.isEntryNameGood(entry, bad))
				{
					//Metode statistikas datu par šķirkli ievākšanai
					stats.collectStats(entries[i]);
					//paŗabaude vai nav izņēmums
					if(!StringUtils.exclusion(ExceptionList.exceptions, entries[i]))
					{
						//Metode, kās pārbauda simbolus šķirklī
						EntryChecks.langChars(entry, bad);

						//Metode, kas pārbauda saīsinājumu un vietniekvārdu gramatiku
						EntryChecks.grammar(entry, bad);

						//Metode kas pārbauda vai aiz @ seko pareizs skaitlis
						EntryChecks.at(entry, bad);

						//Metode pārbauda vai ir visi nepieciešamie indikatori
						EntryChecks.obligatoryMarkers(entry, bad);

						//iekavu līdzsvars
						EntryChecks.bracketing(entry, bad);

						//pārbauda šķirkļus kas satur GR
						EntryChecks.gr(entry, bad);

						//Metode kas iet cauri skjirklim pa vienam vārdam un sīki pārbauda visus iespējamos gadījumus
						EntryChecks.wordByWord(entry, bad);

						//pārbauda sķirkļus kas satur RU
						EntryChecks.ru(entry, bad);

						//Pārbauda vai eksistē šķirkļa vārds kāds minēts aiz CD
						EntryChecks.wordAfterCd(entry, entries, bad);

						//Pārbauda vai eksistē šķirkļa vārds kāds minēts aiz DN
						EntryChecks.wordAfterDn(entry, entries, bad);

						//Skjirkli ar IN indikatoriem
						if (entry.contents.matches("^IN\\s.*$"))
						{
							String bezIn = entry.contents.substring(3).trim();
							int index = StringUtils.findNumber(bezIn);

							//Metode kas pārbauda likumsakarības ar IN 0 un IN 1
							EntryChecks.inNumber(entry, this, index);

							//Metode, kas pārbauda nozīmes - NS
							EntryChecks.nsNoNgAn(entry, bad);
							EntryChecks.an(entry, bad);

							//Metode, kas pārbauda piemērus -  PI
							EntryChecks.piPn(entry, bad);

							//Metode, kas pārbauda Frazeoloģismus
							EntryChecks.fsFr(entry, bad);

							//Metode, kas pārbauda Divdabjus
							EntryChecks.dsDe(entry, bad);

							//Metode, kas pārbauda atsauces - LI
							EntryChecks.li(entry, bad, references);

							// ieliek bijushos vaardus ar IN sarakstā
							prevIN.put(entry.name, Trio.of(index, entry.fullText, entry.id));
						}
						else
						{
							EntryChecks.notInUnity(entry, bad, prevIN);

							prevIN.put(entry.name, Trio.of(-1, entry.fullText, entry.id));
						}
					}
				}
			}
			progress = ((float) i) / entries.length * 100;
			if (i % 50 == 0)
			{
				System.out.print(fileName + " [");//
				System.out.printf("%.1f", progress);		// Progresa izvade uz ekrāna
				System.out.print("%]\r");					//
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
