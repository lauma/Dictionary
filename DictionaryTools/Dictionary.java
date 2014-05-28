package DictionaryTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;



public class Dictionary
{

	public static void main(String[] args)
		throws IOException
		{
			// Argumentu paarbaude.
			File TestFolder = new File("./files/");

			if(!TestFolder.exists())
			{
				System.out.println("Kïûda - nav atrasta mape ar failiem.\n"
							+ "Izveidojiet mapi 'files'!");
				return;
			}
			
			File folder = null;
			int FileCount = 0;
			String FileName;
			File EntryFile = null;
			WordExtractor EntryExtract = null ;
			Excel Table = new Excel();
			
			//Izveido .xls failu kur glabât statistikas datus
			Excel.CreateXls();
			//Izveido sarakstu ar ðíirkïiem ko navajag apskatît
			ExceptionList.GetExceptData();
			//Izveido sarakstu ar visâm atsaucçm
			ReferenceList.GetReferData();
			
			folder = new File("./files/");
			File[] listOfFiles = folder.listFiles();
			
			int file_sk = listOfFiles.length;
		
			for (int p = 0; p < file_sk; p++)
			{
				String ext = "";
				FileName = listOfFiles[p].getName();
				int s = FileName.lastIndexOf('.');
				if (s > 0)
				{
				    ext = listOfFiles[p].getName().substring(s+1);
				}
			    if (listOfFiles[p].isFile() && ext.equals("doc")) 
			    {
			        FileCount ++;
			        
			        EntryFile = new File("./files/" + FileName);
			        FileInputStream fis=new FileInputStream(EntryFile.getAbsolutePath());
			        HWPFDocument EntryDoc=new HWPFDocument(fis);
			        EntryExtract = new WordExtractor(EntryDoc);
			        // visu skjirklu ielase
			        String [] Entries = EntryExtract.getParagraphText();
			        
			        String [][] InEntries = new String [Entries.length][2];
			        
			        int k = 0;
			        float Progress = 0;
			        int EntryLen = Entries.length;
			        Stats StatData = new Stats();
			        
			        //slikto ðíirkïu saraksts
			        String BadRow;
			        ArrayList<String> bad = new ArrayList<String>();
			        
			        /////Datu apstrâde//////
			        for(int i=0; i<EntryLen; i++)
			        {
			        	if(!StringUtils.IsEmptyEntry(Entries[i], bad))
			        	{
			        		Progress = (((float)i/EntryLen)*100);
			        		StatData.WordCount = StatData.WordCount + StringUtils.WordCount(Entries[i]);
			        		StatData.EntryCount++;
			        		
			        		String EntryInf = Entries[i].substring(Entries[i].indexOf(" ")).trim();
			        		String EntryName = Entries[i].substring(0, Entries[i].indexOf(" ")).trim();
			        		
			        		if(!EntryChecks.IsEntryNameGood(Entries[i], bad))
			        		{
			        			//Metode statistikas datu par ðíirkli ievâkðanai
			        			Stats.Statistics(Entries[i], StatData);
			        			
			        			if(!StringUtils.Exclusion(ExceptionList.Exceptions, Entries[i]))
			        			{
			        				//Metode, kâs pârbauda simbolus ðíirklî
			        				EntryChecks.LangCharCheck(Entries[i], bad);
			        				
			        				//Metode, kas pârbauda saîsinâjumu un vietniekvârdu gramatiku
			        				EntryChecks.GrammarCheck(Entries[i], bad);
			        			
			        				//Metode kas pârbauda vai aiz @ seko pareizs skaitlis
			        				EntryChecks.AtCheck(Entries[i], bad);
			        			
			        				//Metode pârbauda vai ir visi nepiecieðamie indikatori
			        				EntryChecks.IdentCheck(Entries[i], bad);
						
			        				//Metode pârbauda vai aiz IN DS NS FS obligâti seko skaitlis
			        				EntryChecks.InDsNsFsNumberCheck(Entries[i], bad);
			        			
			        				//iekavu lîdzsvars
			        				EntryChecks.CheckBrackets(Entries[i], bad);
			        			
			        				//pârbauda ðíirkïus kas satur GR
			        				EntryChecks.GrCheck(Entries[i], bad);
			        			
			        				//Metode kas iet cauri skjirklim pa vienam vârdam un sîki pârbauda visus iespçjamos gadîjumus
			        				EntryChecks.WordByWordCheck(Entries[i], bad);
						
			        				//pârbauda síirkïus kas satur RU
			        				EntryChecks.RuCheck(Entries[i], bad);			        			
						
			        				//Pârbauda vai eksistç ðíirkïa vârds kâds minçts aiz CD
			        				EntryChecks.WordAfterCd(Entries, Entries[i], bad);
			        			
			        				//Pârbauda vai eksistç ðíirkïa vârds kâds minçts aiz DN
			        				EntryChecks.WordAfterDn(Entries, Entries[i], bad);
			        							
			        				//Skjirkli ar IN indikatoriem
			        				if (EntryInf.matches("^IN\\s.*$"))
			        				{
			        					String bezIn = EntryInf.substring(3).trim();
			        					int index = StringUtils.FindNumber(bezIn);
			        					
			        					//Metode kas pârbauda likumsakarîbas ar IN 0 un IN 1
			        					EntryChecks.In0In1Check(Entries[i], bad, InEntries, Entries, i, index);
			        					
			        					//Metode, kas pârbauda NS
			        					EntryChecks.NsCheck(Entries[i], bad);
			        				
			        					//Metode, kas pârbauda PI
			        					EntryChecks.PiCheck(Entries[i], bad);
			        					
			        					//Metode, kas pârbauda Frazeoloìismus
			        					EntryChecks.FsCheck(Entries[i], bad);
			        										
			        					//Metode, kas pârbauda Divdabjus
			        					EntryChecks.DsCheck(Entries[i], bad);
			        					
			        					//Metode, kas pârbauda atsauces - LI
			        					EntryChecks.LiCheck(Entries[i], bad, ReferenceList.References);
			        				
			        					// ieliek bijushos vaardus ar IN sarakstâ
			        					InEntries[k][0] = EntryName;
			        					InEntries[k][1] = String.valueOf(index); 
			        					k++;
			        				}
			        			}
			        		}
			        	}
			        	System.out.print("File " + FileName + " [");
			        	System.out.printf("%.1f", Progress);
			        	System.out.print("%]\r");
			        }
			        System.out.print("File " + FileName + " [DONE]\t\n");
			        
			        ///Datu izvade *.klu failâ///
			        
			        // Izejas pluusma.
			        String[] parts = FileName.split("\\.");
			        String part1 = parts[0];
			        
			        Stats.FillTable(Table, StatData , FileName, FileCount);
					
			        Stats.SumTable(Table);
			        
			        Excel.Write();
			        
			        // Savaakto shkjirklju analiize un atbilstosho izejas datu izdrukaashana.
			        if(!bad.isEmpty())
			        {
			        	BufferedWriter OutFile = new BufferedWriter(
			        			new OutputStreamWriter(new FileOutputStream("./files/" + part1 + ".klu"), "windows-1257"));
			        	while (!bad.isEmpty())
			        	{
			        		int indx = 0;
			        		BadRow = bad.remove(indx);
			        		OutFile.write(BadRow + "\n");
			        		int atkaartojums = bad.indexOf(BadRow);
			        		while (atkaartojums > -1)
			        		{
			        			bad.remove(atkaartojums);
			        			atkaartojums = bad.indexOf(BadRow);
			        		}
			        		indx++;
			        	}
			        	OutFile.flush();
			        	OutFile.close();
			        }
			    }
			}
			System.out.print("ALL FILES DONE!" + "\n");
		}	// main funkcijas beigas

}
