/**
 * Kopējā vārdnīcas apstrādes paka.
 */
package DictionaryTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

//bibliotēka *.doc failu apstrādei
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

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
	
	public void readFromFile(String path)
	throws IOException
	{
		File entryFile = new File(path);
		FileInputStream fis = new FileInputStream(entryFile.getAbsolutePath());
		WordExtractor entryExtract = new WordExtractor(new HWPFDocument(fis));
		fileName = path;
		if (fileName.contains("\\"))
			fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
		if (fileName.contains("/"))
			fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		
		// Ielādē šķirkļus.
		entries = entryExtract.getParagraphText();

		// Notīra uzkrājošos statistikas mainīgos.
		prevIN = new HashMap<String, Trio<Integer, String, Integer>>();
		bad = new BadEntries();		
	}
	
	public void check (Stats statData, ReferenceList references)
	{
		float progress = 0;
		for(int i=0; i < entries.length; i++)
		{
			//pārbaude vai šķirklis nav tukšs
			if(!StringUtils.isEntryEmpty(this, i))
			{
				//progress = (((float)i/EntryLen)*100);
				statData.wordCount = statData.wordCount + StringUtils.wordCount(entries[i]);
				statData.entryCount++;
				//šķirkļa informācijas ieguve
				Dictionary.Entry entry = new Dictionary.Entry(entries[i], i);
				//String entryInf = entries[i].substring(entries[i].indexOf(" ")).trim();
				//šķirkļa vārda ieguve
				//String entryName = entries[i].substring(0, entries[i].indexOf(" ")).trim();
				// pārbaude vai šķirkļa vārds ir labs
				if(!EntryChecks.isEntryNameGood(entry, bad))
				{
					//Metode statistikas datu par šķirkli ievākšanai
					statData.collectStats(entries[i]);
					//paŗabaude vai nav izņēmums
					if(!StringUtils.exclusion(ExceptionList.exceptions, entries[i]))
					{
						//Metode, kās pārbauda simbolus šķirklī
						EntryChecks.langCharCheck(entry, bad);

						//Metode, kas pārbauda saīsinājumu un vietniekvārdu gramatiku
						EntryChecks.grammarCheck(entry, bad);

						//Metode kas pārbauda vai aiz @ seko pareizs skaitlis
						EntryChecks.atCheck(entry, bad);

						//Metode pārbauda vai ir visi nepieciešamie indikatori
						EntryChecks.identCheck(entry, bad);

						//Metode pārbauda vai aiz IN DS NS FS obligāti seko skaitlis
						EntryChecks.inDsNsFsNumberCheck(entry, bad);

						//iekavu līdzsvars
						EntryChecks.checkBrackets(entry, bad);

						//pārbauda šķirkļus kas satur GR
						EntryChecks.grCheck(entry, bad);

						//Metode kas iet cauri skjirklim pa vienam vārdam un sīki pārbauda visus iespējamos gadījumus
						EntryChecks.wordByWordCheck(entry, bad);

						//pārbauda sķirkļus kas satur RU
						EntryChecks.ruCheck(entry, bad);			        			

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
							EntryChecks.inNumberCheck(entry, this, index);

							//Metode, kas pārbauda nozīmes - NS
							EntryChecks.nsCheck(entry, bad);

							//Metode, kas pārbauda piemērus -  PI
							EntryChecks.piCheck(entry, bad);

							//Metode, kas pārbauda Frazeoloģismus
							EntryChecks.fsCheck(entry, bad);

							//Metode, kas pārbauda Divdabjus
							EntryChecks.dsCheck(entry, bad);

							//Metode, kas pārbauda atsauces - LI
							EntryChecks.liCheck(entry, bad, references);

							// ieliek bijushos vaardus ar IN sarakstā
							prevIN.put(entry.name, Trio.of(index, entry.fullText, entry.id));
						}
						else
						{
							EntryChecks.notInUnityCheck(entry, bad, prevIN);

							prevIN.put(entry.name, Trio.of(-1, entry.fullText, entry.id));
						}
					}
				}
			}
			progress = ((float) i) / entries.length * 100;
			if (i % 50 == 0)
			{
				System.out.print("File " + fileName + " [");//
				System.out.printf("%.1f", progress);		// Progresa izvade uz ekrāna
				System.out.print("%]\r");					//
			}
		}
		System.out.print("File " + fileName + " [postprocessing]\r"); // Progresa izvade uz ekrāna
		GlobalChecks.singleIn1Check(this);
		System.out.print("File " + fileName + " [DONE]\t\t\t\n"); //izvade uz ekrāna kad pabeigts fails

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
