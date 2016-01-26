package lv.ailab.dict.tezaurs;

import java.io.File;
import java.io.IOException;

import lv.ailab.dict.tezaurs.checker.Dictionary;
import lv.ailab.dict.tezaurs.checker.ExceptionList;
import lv.ailab.dict.tezaurs.checker.ReferenceList;
import lv.ailab.dict.tezaurs.io.XlsOutputer;

/**
 * Programmas ārējā saskarne, no kuras FIXME daļu analīzes funkcionalitātes
 * vajadzētu izcelt ārā.
 * @author Lauma, Gunārs Danovskis
 */
public class DictionaryChecker
{
	public static String inputDataPath = "./dati/";
	public static String settingsPath = "./saraksti/";

	public static void main(String[] args)
    throws IOException
    {
		// pārbaude vai ir izveidota datu failu mape.
		File folder = new File(inputDataPath);
		if(!folder.exists())
		{
			System.out.println(
					"Ups! Nevar atrast ieejas datu mapi \"" + inputDataPath + "\"!");
			return;
		}
		if (!(new File (settingsPath + "zLI.doc")).exists())
		{
			System.out.println(
					"Ups! Nevar atrast avotu sarakstu \"" + settingsPath + "zLI.doc\"!");
			return;
		}
		if (!(new File (settingsPath + "exceptions.doc")).exists())
		{
			System.out.println(
					"Ups! Nevar atrast sarakstu ar sliktajiem \"" + settingsPath + "exceptions.doc\"!");
			return;
		}

		//int fileCount = 0;
		String fileName;
		//Izveido .xls failu kur glabāt statistikas datus
		XlsOutputer table = new XlsOutputer(inputDataPath + "vardusk.xls");

		//Izveido sarakstu ar šķirkļiem ko navajag apskatīt
		ExceptionList.loadExceptions(settingsPath + "exceptions.doc");
		//Izveido sarakstu ar visām atsaucēm
		ReferenceList refList = new ReferenceList(settingsPath + "zLI.doc");

		File[] listOfFiles = folder.listFiles();

		// masīvs iet cauri visiem mapes "files"
		if (listOfFiles!= null) for (File listOfFile : listOfFiles)
		{
			String ext = ""; // faila saīsinājums
			fileName = listOfFile.getName(); //iegūts faila nosaukums
			int s = fileName.lastIndexOf('.');
			if (s > 0)
			{
				ext = listOfFile.getName().substring(s + 1);
			}
			//tiek apstrādāti tikai tie faili kuriem galā ir saīsinājums doc
			if (listOfFile.isFile() && ext.equals("doc") && !fileName
					.startsWith("~"))
			{
				// Inicē.
				Dictionary dictFile = Dictionary.loadFromFile(
						inputDataPath + fileName, refList);

				// Apstrādā.
				dictFile.check();

				// Izdrukā rezultātus.
				dictFile.printResults(table);
			}
		}
		System.out.print("Viss pabeigts!" + "\n"); // paziņujoms par visu failu pabeigšanu
	}	// main funkcijas beigas
}
