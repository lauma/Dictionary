package lv.ailab.tezaurs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import lv.ailab.tezaurs.io.DocLoader;

public class DictionaryToDicUI
{
	protected static String splitPattern =
			"\\s(?=(NO|NS|PI|PN|FS|FR|FN|FP|DS|DE|DG|AN|DN|CD|LI|NG|AG|PG|FG)\\s)|" +
			"\\s(?=IN\\s([^I]|I[^N]|IN[^\\s]))"; // Otrais gadījums īpaši šķirklim Indija.
	protected static String removePattern = "\\s(@5|@2)(?=\\s)";
	
	public static void main(String[] args)
			throws IOException
	{
		// pārbaude vai ir izveidota mape "files".
		File folder = new File("./files/");

		if(!folder.exists())
		{
			System.out.println("Error: can't find folder 'files'.\n"
					+ "Please, create folder 'files'!");
			return;
		}
		
		File dicFolder = new File("./files/dic/");
		if(!dicFolder.exists()) dicFolder.mkdirs();

		File[] listOfFiles = folder.listFiles();
		for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (!fileName.endsWith(".doc"))
				continue;
			BufferedWriter dicOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("./files/dic/" +fileName.substring(0, fileName.length()-".doc".length()) + ".dic"), "UTF8"));
			
			String[] entities = DocLoader.loadDictionary("./files/" + fileName);
			for (String e : entities)
			{
				String[] elements = convertEntry(e);
				printEntry(elements, dicOut);
			}
			dicOut.flush();
			dicOut.close();
			
			System.out.println(fileName + " [DONE]");
			
		}
		System.out.println("ALL FILES DONE!"); // paziņujoms par visu failu pabeigšanu
	}
	

	/**
	 * Sadala šķirkli elementos un veic citas normalizācijas.
	 * @param entry
	 * @return
	 */
	protected static String[] convertEntry(String entry)
	{
		entry = "VR " + entry.trim();
		entry = entry.replaceAll(removePattern, "");
		entry = entry.replaceAll(" \\.(?!\\.)", ". ");
		entry = entry.replaceAll("\\s\\s+", " ");
		String[] rows = entry.split(splitPattern);
		return rows;
	}
	
	/**
	 * Elementos sadalīto šķirkli izdrukā dotajā izejas failā, pabeidzot to ar
	 * tukšu rindu.
	 * @param entryElements
	 * @param out
	 * @throws IOException
	 */
	protected static void printEntry(String[] entryElements, BufferedWriter out)
			throws IOException
	{
		for (String elem : entryElements)
		{
			out.write(elem);
			out.newLine();
		}
		out.newLine();
		out.flush();
	}
}
