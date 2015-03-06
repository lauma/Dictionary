package DictionaryTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 * Programmas ārējā saskarne, no kuras FIXME daļu analīzes funkcionalitātes
 * vajadzētu izcelt ārā.
 * @author Lauma, Gunārs Danovskis
 */
public class DictionaryCheckerUI
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
				Dictionary dictFile = new Dictionary();
				dictFile.readFromFile("./files/" + fileName);
				Stats statData = new Stats(); // tiek stats klases mainīgais datu uzglabāšanai

				/////Datu apstrāde//////
				dictFile.check(statData, refList);
				///Datu izvade *.klu failā///

				// Izejas pluusma.
				String[] parts = fileName.split("\\.");
				String part1 = parts[0];
				//statistikas datu ielikešana tabulā
				statData.writeInTable(table, fileName, fileCount);
				// summaēšanas metode
				Stats.sumTable(table);
				// datu ierakstīšana
				Excel.write();
				// Savaakto šķirkļu analīze un atbilstošo izejas datu izdrukāšana.
				if (!dictFile.bad.isEmpty()) dictFile.bad.printAll("./files/" + part1 + ".klu");
			}
		}
		System.out.print("ALL FILES DONE!" + "\n"); // paziņujoms par visu failu pabeigšanu
	}	// main funkcijas beigas
}
