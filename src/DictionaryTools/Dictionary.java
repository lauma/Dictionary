/*****************
Autors: Gunārs Danovskis
Pēdējais labošanas datums: 28.05.2014

Klases mērķis:
	Klase Dictionary ietver sevī main funkciju
*****************/

package DictionaryTools; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
//bibliotēka *.doc failu apstrādei
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;



public class Dictionary
{

	public static void main(String[] args)
		throws IOException
		{
			// pārbaude vai ir izveidota mape "files".
			File testFolder = new File("./files/");

			if(!testFolder.exists())
			{
				System.out.println("Error -can't find folder 'files'.\n" // kļūdas paziņojums
							+ "Please, create folder 'files'!");
				return;
			}
			
			File folder = null;
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
			
			folder = new File("./files/");
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
			        String [] Entries = entryExtract.getParagraphText();
			        
			        // divdimensiju masīvs kur uzglabāt šķirkļa vārdu un marķiera IN vērtību
			        String [][] InEntries = new String [Entries.length][2];
			        
			        int k = 0;
			        float progress = 0; // mainīgais progressa uzskaitei
			        int EntryLen = Entries.length;
			        Stats statData = new Stats(); // tiek stats klases mainīgais datu uzglabāšanai
			        
			        //slikto šķirkļu saraksts
			        String BadRow;
			        ArrayList<String> bad = new ArrayList<String>();
			        
			        /////Datu apstrāde//////
			        for(int i=0; i<EntryLen; i++)
			        {
			        	//pārbaude vai šķirklis nav tukšs
			        	if(!StringUtils.isEntryEmpty(Entries[i], bad))
			        	{
			        		progress = (((float)i/EntryLen)*100);
			        		statData.wordCount = statData.wordCount + StringUtils.wordCount(Entries[i]);
			        		statData.entryCount++;
			        		//šķirkļa informācijas ieguve
			        		String entryInf = Entries[i].substring(Entries[i].indexOf(" ")).trim();
			        		//šķirkļa vārda ieguve
			        		String entryName = Entries[i].substring(0, Entries[i].indexOf(" ")).trim();
			        		// pārbaude vai šķirkļa vārds ir labs
			        		if(!EntryChecks.isEntryNameGood(Entries[i], bad))
			        		{
			        			//Metode statistikas datu par šķirkli ievākšanai
			        			Stats.Statistics(Entries[i], statData);
			        			//paŗabaude vai nav izņēmums
			        			if(!StringUtils.exclusion(ExceptionList.exceptions, Entries[i]))
			        			{
			        				//Metode, kās pārbauda simbolus šķirklī
			        				EntryChecks.langCharCheck(Entries[i], bad);
			        				
			        				//Metode, kas pārbauda saīsinājumu un vietniekvārdu gramatiku
			        				EntryChecks.grammarCheck(Entries[i], bad);
			        			
			        				//Metode kas pārbauda vai aiz @ seko pareizs skaitlis
			        				EntryChecks.atCheck(Entries[i], bad);
			        			
			        				//Metode pārbauda vai ir visi nepieciešamie indikatori
			        				EntryChecks.identCheck(Entries[i], bad);
						
			        				//Metode pārbauda vai aiz IN DS NS FS obligāti seko skaitlis
			        				EntryChecks.inDsNsFsNumberCheck(Entries[i], bad);
			        			
			        				//iekavu līdzsvars
			        				EntryChecks.checkBrackets(Entries[i], bad);
			        			
			        				//pārbauda šķirkļus kas satur GR
			        				EntryChecks.grCheck(Entries[i], bad);
			        			
			        				//Metode kas iet cauri skjirklim pa vienam vārdam un sīki pārbauda visus iespējamos gadījumus
			        				EntryChecks.wordByWordCheck(Entries[i], bad);
						
			        				//pārbauda sķirkļus kas satur RU
			        				EntryChecks.ruCheck(Entries[i], bad);			        			
						
			        				//Pārbauda vai eksistē šķirkļa vārds kāds minēts aiz CD
			        				EntryChecks.wordAfterCd(Entries, Entries[i], bad);
			        			
			        				//Pārbauda vai eksistē šķirkļa vārds kāds minēts aiz DN
			        				EntryChecks.wordAfterDn(Entries, Entries[i], bad);
			        							
			        				//Skjirkli ar IN indikatoriem
			        				if (entryInf.matches("^IN\\s.*$"))
			        				{
			        					String bezIn = entryInf.substring(3).trim();
			        					int index = StringUtils.findNumber(bezIn);
			        					
			        					//Metode kas pārbauda likumsakarības ar IN 0 un IN 1
			        					EntryChecks.in0In1Check(Entries[i], bad, InEntries, Entries, i, index);
			        					
			        					//Metode, kas pārbauda nozīmes - NS
			        					EntryChecks.nsCheck(Entries[i], bad);
			        				
			        					//Metode, kas pārbauda piemērus -  PI
			        					EntryChecks.piCheck(Entries[i], bad);
			        					
			        					//Metode, kas pārbauda Frazeoloģismus
			        					EntryChecks.fsCheck(Entries[i], bad);
			        										
			        					//Metode, kas pārbauda Divdabjus
			        					EntryChecks.dsCheck(Entries[i], bad);
			        					
			        					//Metode, kas pārbauda atsauces - LI
			        					EntryChecks.liCheck(Entries[i], bad, refList);
			        				
			        					// ieliek bijushos vaardus ar IN sarakstā
			        					InEntries[k][0] = entryName;
			        					InEntries[k][1] = String.valueOf(index); 
			        					k++;
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
			        if(!bad.isEmpty())
			        {
			        	//izejas plūsma *.klu failam
			        	BufferedWriter outFile = new BufferedWriter(
			        			new OutputStreamWriter(new FileOutputStream("./files/" + part1 + ".klu"), "windows-1257"));
			        	//slikto šķirkļu saraksta pārrakstīšana izejas failā
			        	while (!bad.isEmpty())
			        	{
			        		int indx = 0;
			        		BadRow = bad.remove(indx);
			        		outFile.write(BadRow + "\n");
			        		int atkaartojums = bad.indexOf(BadRow);
			        		while (atkaartojums > -1)
			        		{
			        			bad.remove(atkaartojums);
			        			atkaartojums = bad.indexOf(BadRow);
			        		}
			        		indx++;
			        	}
			        	outFile.flush(); // plūsmas iztukšošana
			        	outFile.close(); //faila aizvēršana
			        }
			    }
			}
			System.out.print("ALL FILES DONE!" + "\n"); // paziņujoms par visu failu pabeigšanu
		}	// main funkcijas beigas

}
