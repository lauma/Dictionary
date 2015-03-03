/*****************
Autors: Gun�rs Danovskis
P�d�jais labo�anas datums: 28.05.2014

Klases m�r�is:
	Klase Dictionary ietver sev� main funkciju
*****************/

package DictionaryTools; //Kop�ga paka, kur� ir iek�autas visas klases veiksm�gai programmas darb�bai

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
//bibliot�ka *.doc failu apstr�dei
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;



public class Dictionary
{

	public static void main(String[] args)
		throws IOException
		{
			// p�rbaude vai ir izveidota mape "files".
			File testFolder = new File("./files/");

			if(!testFolder.exists())
			{
				System.out.println("Error -can't find folder 'files'.\n" // k��das pazi�ojums
							+ "Please, create folder 'files'!");
				return;
			}
			
			File folder = null;
			int fileCount = 0;
			String fileName;
			File entryFile = null;
			WordExtractor entryExtract = null ;
			Excel table = new Excel();
			
			//Izveido .xls failu kur glab�t statistikas datus
			Excel.createXls();
			//Izveido sarakstu ar ��irk�iem ko navajag apskat�t
			ExceptionList.getExceptData();
			//Izveido sarakstu ar vis�m atsauc�m
			ReferenceList.getReferData();
			
			folder = new File("./files/");
			File[] listOfFiles = folder.listFiles();
			
			int allFileCount = listOfFiles.length;
			// mas�vs iet cauri visiem mapes "files"
			for (int p = 0; p < allFileCount; p++)
			{
				String ext = ""; // faila sa�sin�jums
				fileName = listOfFiles[p].getName(); //ieg�ts faila nosaukums
				int s = fileName.lastIndexOf('.');
				if (s > 0)
				{
				    ext = listOfFiles[p].getName().substring(s+1);
				}
				//tiek apstr�d�ti tikai tie faili kuriem gal� ir sa�sin�jums doc
			    if (listOfFiles[p].isFile() && ext.equals("doc")) 
			    {
			        fileCount ++;
			        
			        entryFile = new File("./files/" + fileName);
			        // apstr�d�jam�faila ce�a ieguve
			        FileInputStream fis=new FileInputStream(entryFile.getAbsolutePath());
			        HWPFDocument EntryDoc=new HWPFDocument(fis);
			        entryExtract = new WordExtractor(EntryDoc);
			        // visu ��irk�u ielase, katrs paragr�fs sav� mas�va lauk�
			        String [] Entries = entryExtract.getParagraphText();
			        
			        // divdimensiju mas�vs kur uzglab�t ��irk�a v�rdu un mar�iera IN v�rt�bu
			        String [][] InEntries = new String [Entries.length][2];
			        
			        int k = 0;
			        float progress = 0; // main�gais progressa uzskaitei
			        int EntryLen = Entries.length;
			        Stats statData = new Stats(); // tiek stats klases main�gais datu uzglab��anai
			        
			        //slikto ��irk�u saraksts
			        String BadRow;
			        ArrayList<String> bad = new ArrayList<String>();
			        
			        /////Datu apstr�de//////
			        for(int i=0; i<EntryLen; i++)
			        {
			        	//p�rbaude vai ��irklis nav tuk�s
			        	if(!StringUtils.isEntryEmpty(Entries[i], bad))
			        	{
			        		progress = (((float)i/EntryLen)*100);
			        		statData.wordCount = statData.wordCount + StringUtils.wordCount(Entries[i]);
			        		statData.entryCount++;
			        		//��irk�a inform�cijas ieguve
			        		String entryInf = Entries[i].substring(Entries[i].indexOf(" ")).trim();
			        		//��irk�a v�rda ieguve
			        		String entryName = Entries[i].substring(0, Entries[i].indexOf(" ")).trim();
			        		// p�rbaude vai ��irk�a v�rds ir labs
			        		if(!EntryChecks.isEntryNameGood(Entries[i], bad))
			        		{
			        			//Metode statistikas datu par ��irkli iev�k�anai
			        			Stats.Statistics(Entries[i], statData);
			        			//pa�abaude vai nav iz��mums
			        			if(!StringUtils.exclusion(ExceptionList.exceptions, Entries[i]))
			        			{
			        				//Metode, k�s p�rbauda simbolus ��irkl�
			        				EntryChecks.langCharCheck(Entries[i], bad);
			        				
			        				//Metode, kas p�rbauda sa�sin�jumu un vietniekv�rdu gramatiku
			        				EntryChecks.grammarCheck(Entries[i], bad);
			        			
			        				//Metode kas p�rbauda vai aiz @ seko pareizs skaitlis
			        				EntryChecks.atCheck(Entries[i], bad);
			        			
			        				//Metode p�rbauda vai ir visi nepiecie�amie indikatori
			        				EntryChecks.identCheck(Entries[i], bad);
						
			        				//Metode p�rbauda vai aiz IN DS NS FS oblig�ti seko skaitlis
			        				EntryChecks.inDsNsFsNumberCheck(Entries[i], bad);
			        			
			        				//iekavu l�dzsvars
			        				EntryChecks.checkBrackets(Entries[i], bad);
			        			
			        				//p�rbauda ��irk�us kas satur GR
			        				EntryChecks.grCheck(Entries[i], bad);
			        			
			        				//Metode kas iet cauri skjirklim pa vienam v�rdam un s�ki p�rbauda visus iesp�jamos gad�jumus
			        				EntryChecks.wordByWordCheck(Entries[i], bad);
						
			        				//p�rbauda s�irk�us kas satur RU
			        				EntryChecks.ruCheck(Entries[i], bad);			        			
						
			        				//P�rbauda vai eksist� ��irk�a v�rds k�ds min�ts aiz CD
			        				EntryChecks.wordAfterCd(Entries, Entries[i], bad);
			        			
			        				//P�rbauda vai eksist� ��irk�a v�rds k�ds min�ts aiz DN
			        				EntryChecks.wordAfterDn(Entries, Entries[i], bad);
			        							
			        				//Skjirkli ar IN indikatoriem
			        				if (entryInf.matches("^IN\\s.*$"))
			        				{
			        					String bezIn = entryInf.substring(3).trim();
			        					int index = StringUtils.findNumber(bezIn);
			        					
			        					//Metode kas p�rbauda likumsakar�bas ar IN 0 un IN 1
			        					EntryChecks.in0In1Check(Entries[i], bad, InEntries, Entries, i, index);
			        					
			        					//Metode, kas p�rbauda noz�mes - NS
			        					EntryChecks.nsCheck(Entries[i], bad);
			        				
			        					//Metode, kas p�rbauda piem�rus -  PI
			        					EntryChecks.piCheck(Entries[i], bad);
			        					
			        					//Metode, kas p�rbauda Frazeolo�ismus
			        					EntryChecks.fsCheck(Entries[i], bad);
			        										
			        					//Metode, kas p�rbauda Divdabjus
			        					EntryChecks.dsCheck(Entries[i], bad);
			        					
			        					//Metode, kas p�rbauda atsauces - LI
			        					EntryChecks.liCheck(Entries[i], bad, ReferenceList.references);
			        				
			        					// ieliek bijushos vaardus ar IN sarakst�
			        					InEntries[k][0] = entryName;
			        					InEntries[k][1] = String.valueOf(index); 
			        					k++;
			        				}
			        			}
			        		}
			        	}
			        	System.out.print("File " + fileName + " [");//
			        	System.out.printf("%.1f", progress);		// Progresa izvade uz ekr�na
			        	System.out.print("%]\r");					//
			        }
			        System.out.print("File " + fileName + " [DONE]\t\n"); //izvade uz ekr�na kad pabeigts fails
			        
			        ///Datu izvade *.klu fail�///
			        
			        // Izejas pluusma.
			        String[] parts = fileName.split("\\.");
			        String part1 = parts[0];
			        //statistikas datu ielike�ana tabul�
			        Stats.FillTable(table, statData , fileName, fileCount);
					// summa��anas metode
			        Stats.SumTable(table);
			        // datu ierakst��ana
			        Excel.write();
			        
			        // Savaakto ��irk�u anal�ze un atbilsto�o izejas datu izdruk��ana.
			        if(!bad.isEmpty())
			        {
			        	//izejas pl�sma *.klu failam
			        	BufferedWriter outFile = new BufferedWriter(
			        			new OutputStreamWriter(new FileOutputStream("./files/" + part1 + ".klu"), "windows-1257"));
			        	//slikto ��irk�u saraksta p�rrakst��ana izejas fail�
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
			        	outFile.flush(); // pl�smas iztuk�o�ana
			        	outFile.close(); //faila aizv�r�ana
			        }
			    }
			}
			System.out.print("ALL FILES DONE!" + "\n"); // pazi�ujoms par visu failu pabeig�anu
		}	// main funkcijas beigas

}
