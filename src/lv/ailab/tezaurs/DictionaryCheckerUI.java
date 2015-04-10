package lv.ailab.tezaurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import lv.ailab.tezaurs.checker.Dictionary;
import lv.ailab.tezaurs.checker.ExceptionList;
import lv.ailab.tezaurs.checker.ReferenceList;
import lv.ailab.tezaurs.io.XlsOutputer;

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

		//int fileCount = 0;
		String fileName;
		//Izveido .xls failu kur glabāt statistikas datus
		XlsOutputer table = new XlsOutputer();

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
				// Inicē.
				Dictionary dictFile = new Dictionary();
				dictFile.loadFromFile("./files/" + fileName);

				// Apstrādā.
				dictFile.check(refList);
				
				// Izdrukā rezultātus.
				dictFile.printResults(table);
			}
		}
		System.out.print("ALL FILES DONE!" + "\n"); // paziņujoms par visu failu pabeigšanu
	}	// main funkcijas beigas
}
