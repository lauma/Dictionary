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
 * Programmas ārējā saskarne, no kuras FIXME daļu analīzes funkcionalitātes
 * vajadzētu izcelt ārā.
 * @author Lauma, Gunārs Danovskis
 */
public class Dictionary
{

	public static void main(String[] args)
		throws IOException
		{
			// pārbaude vai ir izveidota mape "files".
			File folder = new File("./files/");

			if(!folder.exists())
			{
				System.out.println("Error -can't find folder 'files'.\n" // kļūdas paziņojums
							+ "Please, create folder 'files'!");
				return;
			}
			
			int fileCount = 0;
			String fileName;
			File entryFile = null;
			WordExtractor entryExtract = null ;
			Excel table = new Excel();
			
			//Izveido .xls failu kur glabāt statistikas datus
			Excel.createXls();
			//Izveido sarakstu ar šķirkļiem ko navajag apskatīt
			ExceptionList.getExceptData();
			//Izveido sarakstu ar visām atsaucēm
			ReferenceList refList = new ReferenceList("./files/prog files/zLI.doc");
			
			File[] listOfFiles = folder.listFiles();
			
			int allFileCount = listOfFiles.length;
			// masīvs iet cauri visiem mapes "files"
			for (int p = 0; p < allFileCount; p++)
			{
				String ext = ""; // faila saīsinājums
				fileName = listOfFiles[p].getName(); //iegūts faila nosaukums
				int s = fileName.lastIndexOf('.');
				if (s > 0)
				{
				    ext = listOfFiles[p].getName().substring(s+1);
				}
				//tiek apstrādāti tikai tie faili kuriem galā ir saīsinājums doc
			    if (listOfFiles[p].isFile() && ext.equals("doc")) 
			    {
			        fileCount ++;
			        
			        entryFile = new File("./files/" + fileName);
			        // apstrādājamāfaila ceļa ieguve
			        FileInputStream fis=new FileInputStream(entryFile.getAbsolutePath());
			        HWPFDocument EntryDoc=new HWPFDocument(fis);
			        entryExtract = new WordExtractor(EntryDoc);
			        // visu šķirkļu ielase, katrs paragrāfs savā masīva laukā
			        String [] entries = entryExtract.getParagraphText();
			        
			        // divdimensiju masīvs kur uzglabāt šķirkļa vārdu un marķiera IN vērtību
			        //String [][] InEntries = new String [entries.length][2];
			        HashMap<String, Trio<Integer, String, Integer>> prevIN = new HashMap<String, Trio<Integer, String, Integer>>();
			        
			        float progress = 0; // mainīgais progressa uzskaitei
			        int EntryLen = entries.length;
			        Stats statData = new Stats(); // tiek stats klases mainīgais datu uzglabāšanai
			        
			        //slikto šķirkļu saraksts
			        BadEntries bad = new BadEntries();
			        
			        /////Datu apstrāde//////
			        for(int i=0; i<EntryLen; i++)
			        {
			        	//pārbaude vai šķirklis nav tukšs
			        	if(!StringUtils.isEntryEmpty(entries[i], i, bad))
			        	{
			        		progress = (((float)i/EntryLen)*100);
			        		statData.wordCount = statData.wordCount + StringUtils.wordCount(entries[i]);
			        		statData.entryCount++;
			        		//šķirkļa informācijas ieguve
			        		String entryInf = entries[i].substring(entries[i].indexOf(" ")).trim();
			        		//šķirkļa vārda ieguve
			        		String entryName = entries[i].substring(0, entries[i].indexOf(" ")).trim();
			        		// pārbaude vai šķirkļa vārds ir labs
			        		if(!EntryChecks.isEntryNameGood(entries[i], i, bad))
			        		{
			        			//Metode statistikas datu par šķirkli ievākšanai
			        			Stats.Statistics(entries[i], statData);
			        			//paŗabaude vai nav izņēmums
			        			if(!StringUtils.exclusion(ExceptionList.exceptions, entries[i]))
			        			{
			        				//Metode, kās pārbauda simbolus šķirklī
			        				EntryChecks.langCharCheck(entries[i], i, bad);
			        				
			        				//Metode, kas pārbauda saīsinājumu un vietniekvārdu gramatiku
			        				EntryChecks.grammarCheck(entries[i], i, bad);
			        			
			        				//Metode kas pārbauda vai aiz @ seko pareizs skaitlis
			        				EntryChecks.atCheck(entries[i], i, bad);
			        			
			        				//Metode pārbauda vai ir visi nepieciešamie indikatori
			        				EntryChecks.identCheck(entries[i], i, bad);
						
			        				//Metode pārbauda vai aiz IN DS NS FS obligāti seko skaitlis
			        				EntryChecks.inDsNsFsNumberCheck(entries[i], i, bad);
			        			
			        				//iekavu līdzsvars
			        				EntryChecks.checkBrackets(entries[i], i, bad);
			        			
			        				//pārbauda šķirkļus kas satur GR
			        				EntryChecks.grCheck(entries[i], i, bad);
			        			
			        				//Metode kas iet cauri skjirklim pa vienam vārdam un sīki pārbauda visus iespējamos gadījumus
			        				EntryChecks.wordByWordCheck(entries[i], i, bad);
						
			        				//pārbauda sķirkļus kas satur RU
			        				EntryChecks.ruCheck(entries[i], i, bad);			        			
						
			        				//Pārbauda vai eksistē šķirkļa vārds kāds minēts aiz CD
			        				EntryChecks.wordAfterCd(entries, entries[i], i, bad);
			        			
			        				//Pārbauda vai eksistē šķirkļa vārds kāds minēts aiz DN
			        				EntryChecks.wordAfterDn(entries, entries[i], i, bad);
			        							
			        				//Skjirkli ar IN indikatoriem
			        				if (entryInf.matches("^IN\\s.*$"))
			        				{
			        					String bezIn = entryInf.substring(3).trim();
			        					int index = StringUtils.findNumber(bezIn);
			        					
			        					//Metode kas pārbauda likumsakarības ar IN 0 un IN 1
			        					EntryChecks.inNumberCheck(entries[i], i, bad, prevIN, entries, index);
			        					
			        					//Metode, kas pārbauda nozīmes - NS
			        					EntryChecks.nsCheck(entries[i], i, bad);
			        				
			        					//Metode, kas pārbauda piemērus -  PI
			        					EntryChecks.piCheck(entries[i], i, bad);
			        					
			        					//Metode, kas pārbauda Frazeoloģismus
			        					EntryChecks.fsCheck(entries[i], i, bad);
			        										
			        					//Metode, kas pārbauda Divdabjus
			        					EntryChecks.dsCheck(entries[i], i, bad);
			        					
			        					//Metode, kas pārbauda atsauces - LI
			        					EntryChecks.liCheck(entries[i], i, bad, refList);
			        				
			        					// ieliek bijushos vaardus ar IN sarakstā
			        					//InEntries[k][0] = entryName;
			        					//InEntries[k][1] = String.valueOf(index); 
			        					prevIN.put(entryName, Trio.of(index, entries[i], i));
			        				}
			        				else
			        				{
			        					EntryChecks.notInUnityCheck(entries[i], i, bad, prevIN);
			        						
			        					prevIN.put(entryName, Trio.of(-1, entries[i], i));
			        				}
			        			}
			        		}
			        	}
			        	
			        	if (i % 50 == 0)
			        	{
			        		System.out.print("File " + fileName + " [");//
			        		System.out.printf("%.1f", progress);		// Progresa izvade uz ekrāna
			        		System.out.print("%]\r");					//
			        	}
			        }
			        System.out.print("File " + fileName + " [postprocessing]\t\n"); //izvade uz ekrāna kad pabeigts fails
			        GlobalChecks.singleIn1Check(prevIN, bad);
			        System.out.print("File " + fileName + " [DONE]\t\n"); //izvade uz ekrāna kad pabeigts fails
			        
			        ///Datu izvade *.klu failā///
			        
			        // Izejas pluusma.
			        String[] parts = fileName.split("\\.");
			        String part1 = parts[0];
			        //statistikas datu ielikešana tabulā
			        Stats.FillTable(table, statData , fileName, fileCount);
					// summaēšanas metode
			        Stats.SumTable(table);
			        // datu ierakstīšana
			        Excel.write();
			        // Savaakto šķirkļu analīze un atbilstošo izejas datu izdrukāšana.
			        bad.printAll("./files/" + part1 + ".klu");
			    }
			}
			System.out.print("ALL FILES DONE!" + "\n"); // paziņujoms par visu failu pabeigšanu
		}	// main funkcijas beigas

}
