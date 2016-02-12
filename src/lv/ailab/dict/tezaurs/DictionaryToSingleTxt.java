package lv.ailab.dict.tezaurs;

import lv.ailab.dict.io.DocLoader;

import java.io.*;

/**
 * Visu tekstu no visiem .doc ieraksta vienā Windowa-1257 .txt failā.
 * Created on 2015-09-15.
 *
 * @author Lauma
 */
public class DictionaryToSingleTxt
{
	public static String inputDataPath = "./dati/";

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

		BufferedWriter dicOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(inputDataPath + "tezaurs.txt"), "Windows-1257"));

		File[] listOfFiles = folder.listFiles();
		for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (!fileName.endsWith(".doc"))
				continue;

			String[] entities = DocLoader
					.loadDoc(inputDataPath + fileName);
			for (String e : entities)
			{
				if (e != null && !e.trim().equals(""))
					dicOut.write(e);
			}
			dicOut.flush();
			System.out.println(fileName + " [Pabeigts]");

		}
		dicOut.flush();
		dicOut.close();
		System.out.println("Viss pabeigts!"); // paziņujoms par visu failu pabeigšanu
	}
}
