/*****************
Autors: Gunârs Danovskis
Pçdçjais laboðanas datums: 28.05.2014

Klases mçríis:
	Klase Dictionary ietver sevî main funkciju
*****************/

package DictionaryTools; //Kopîga paka, kurâ ir iekïautas visas klases veiksmîgai programmas darbîbai

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
//bibliotçka *.doc failu apstrâdei
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;



public class Dictionary
{

	public static void main(String[] args)
		throws IOException
		{
			// pârbaude vai ir izveidota mape "files".
			File testFolder = new File("./files/");

			if(!testFolder.exists())
			{
				System.out.println("Error -can't find folder 'files'.\n" // kïûdas paziòojums
							+ "Please, create folder 'files'!");
				return;
			}
			
			File folder = null;
			int fileCount = 0;
			String fileName;
			File entryFile = null;
			WordExtractor entryExtract = null ;
			Excel table = new Excel();
			
			//Izveido .xls failu kur glabât statistikas datus
			Excel.createXls();
			//Izveido sarakstu ar ðíirkïiem ko navajag apskatît
			ExceptionList.getExceptData();
			//Izveido sarakstu ar visâm atsaucçm
			ReferenceList.getReferData();
			
			folder = new File("./files/");
			File[] listOfFiles = folder.listFiles();
			
			int allFileCount = listOfFiles.length;
			// masîvs iet cauri visiem mapes "files"
			for (int p = 0; p < allFileCount; p++)
			{
				String ext = ""; // faila saîsinâjums
				fileName = listOfFiles[p].getName(); //iegûts faila nosaukums
				int s = fileName.lastIndexOf('.');
				if (s > 0)
				{
				    ext = listOfFiles[p].getName().substring(s+1);
				}
				//tiek apstrâdâti tikai tie faili kuriem galâ ir saîsinâjums doc
			    if (listOfFiles[p].isFile() && ext.equals("doc")) 
			    {
			        fileCount ++;
			        
			        entryFile = new File("./files/" + fileName);
			        // apstrâdâjamâfaila ceïa ieguve
			        FileInputStream fis=new FileInputStream(entryFile.getAbsolutePath());
			        HWPFDocument EntryDoc=new HWPFDocument(fis);
			        entryExtract = new WordExtractor(EntryDoc);
			        // visu ðíirkïu ielase, katrs paragrâfs savâ masîva laukâ
			        String [] Entries = entryExtract.getParagraphText();
			        
			        // divdimensiju masîvs kur uzglabât ðíirkïa vârdu un maríiera IN vçrtîbu
			        String [][] InEntries = new String [Entries.length][2];
			        
			        int k = 0;
			        float progress = 0; // mainîgais progressa uzskaitei
			        int EntryLen = Entries.length;
			        Stats statData = new Stats(); // tiek stats klases mainîgais datu uzglabâðanai
			        
			        //slikto ðíirkïu saraksts
			        String BadRow;
			        ArrayList<String> bad = new ArrayList<String>();
			        
			        /////Datu apstrâde//////
			        for(int i=0; i<EntryLen; i++)
			        {
			        	//pârbaude vai ðíirklis nav tukðs
			        	if(!StringUtils.isEntryEmpty(Entries[i], bad))
			        	{
			        		progress = (((float)i/EntryLen)*100);
			        		statData.wordCount = statData.wordCount + StringUtils.wordCount(Entries[i]);
			        		statData.entryCount++;
			        		//ðíirkïa informâcijas ieguve
			        		String entryInf = Entries[i].substring(Entries[i].indexOf(" ")).trim();
			        		//ðíirkïa vârda ieguve
			        		String entryName = Entries[i].substring(0, Entries[i].indexOf(" ")).trim();
			        		// pârbaude vai ðíirkïa vârds ir labs
			        		if(!EntryChecks.isEntryNameGood(Entries[i], bad))
			        		{
			        			//Metode statistikas datu par ðíirkli ievâkðanai
			        			Stats.Statistics(Entries[i], statData);
			        			//paºabaude vai nav izòçmums
			        			if(!StringUtils.exclusion(ExceptionList.exceptions, Entries[i]))
			        			{
			        				//Metode, kâs pârbauda simbolus ðíirklî
			        				EntryChecks.langCharCheck(Entries[i], bad);
			        				
			        				//Metode, kas pârbauda saîsinâjumu un vietniekvârdu gramatiku
			        				EntryChecks.grammarCheck(Entries[i], bad);
			        			
			        				//Metode kas pârbauda vai aiz @ seko pareizs skaitlis
			        				EntryChecks.atCheck(Entries[i], bad);
			        			
			        				//Metode pârbauda vai ir visi nepiecieðamie indikatori
			        				EntryChecks.identCheck(Entries[i], bad);
						
			        				//Metode pârbauda vai aiz IN DS NS FS obligâti seko skaitlis
			        				EntryChecks.inDsNsFsNumberCheck(Entries[i], bad);
			        			
			        				//iekavu lîdzsvars
			        				EntryChecks.checkBrackets(Entries[i], bad);
			        			
			        				//pârbauda ðíirkïus kas satur GR
			        				EntryChecks.grCheck(Entries[i], bad);
			        			
			        				//Metode kas iet cauri skjirklim pa vienam vârdam un sîki pârbauda visus iespçjamos gadîjumus
			        				EntryChecks.wordByWordCheck(Entries[i], bad);
						
			        				//pârbauda síirkïus kas satur RU
			        				EntryChecks.ruCheck(Entries[i], bad);			        			
						
			        				//Pârbauda vai eksistç ðíirkïa vârds kâds minçts aiz CD
			        				EntryChecks.wordAfterCd(Entries, Entries[i], bad);
			        			
			        				//Pârbauda vai eksistç ðíirkïa vârds kâds minçts aiz DN
			        				EntryChecks.wordAfterDn(Entries, Entries[i], bad);
			        							
			        				//Skjirkli ar IN indikatoriem
			        				if (entryInf.matches("^IN\\s.*$"))
			        				{
			        					String bezIn = entryInf.substring(3).trim();
			        					int index = StringUtils.findNumber(bezIn);
			        					
			        					//Metode kas pârbauda likumsakarîbas ar IN 0 un IN 1
			        					EntryChecks.in0In1Check(Entries[i], bad, InEntries, Entries, i, index);
			        					
			        					//Metode, kas pârbauda nozîmes - NS
			        					EntryChecks.nsCheck(Entries[i], bad);
			        				
			        					//Metode, kas pârbauda piemçrus -  PI
			        					EntryChecks.piCheck(Entries[i], bad);
			        					
			        					//Metode, kas pârbauda Frazeoloìismus
			        					EntryChecks.fsCheck(Entries[i], bad);
			        										
			        					//Metode, kas pârbauda Divdabjus
			        					EntryChecks.dsCheck(Entries[i], bad);
			        					
			        					//Metode, kas pârbauda atsauces - LI
			        					EntryChecks.liCheck(Entries[i], bad, ReferenceList.references);
			        				
			        					// ieliek bijushos vaardus ar IN sarakstâ
			        					InEntries[k][0] = entryName;
			        					InEntries[k][1] = String.valueOf(index); 
			        					k++;
			        				}
			        			}
			        		}
			        	}
			        	System.out.print("File " + fileName + " [");//
			        	System.out.printf("%.1f", progress);		// Progresa izvade uz ekrâna
			        	System.out.print("%]\r");					//
			        }
			        System.out.print("File " + fileName + " [DONE]\t\n"); //izvade uz ekrâna kad pabeigts fails
			        
			        ///Datu izvade *.klu failâ///
			        
			        // Izejas pluusma.
			        String[] parts = fileName.split("\\.");
			        String part1 = parts[0];
			        //statistikas datu ielikeðana tabulâ
			        Stats.FillTable(table, statData , fileName, fileCount);
					// summaçðanas metode
			        Stats.SumTable(table);
			        // datu ierakstîðana
			        Excel.write();
			        
			        // Savaakto ðíirkïu analîze un atbilstoðo izejas datu izdrukâðana.
			        if(!bad.isEmpty())
			        {
			        	//izejas plûsma *.klu failam
			        	BufferedWriter outFile = new BufferedWriter(
			        			new OutputStreamWriter(new FileOutputStream("./files/" + part1 + ".klu"), "windows-1257"));
			        	//slikto ðíirkïu saraksta pârrakstîðana izejas failâ
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
			        	outFile.flush(); // plûsmas iztukðoðana
			        	outFile.close(); //faila aizvçrðana
			        }
			    }
			}
			System.out.print("ALL FILES DONE!" + "\n"); // paziòujoms par visu failu pabeigðanu
		}	// main funkcijas beigas

}
