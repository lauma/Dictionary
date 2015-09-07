package lv.ailab.tezaurs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import lv.ailab.tezaurs.io.DocLoader;

public class DictionaryToDicUI
{
	public static String inputDataPath = "./dati/";

	protected static String splitPattern =
			"\\s(?=(GR|RU|NO|NS|PI|PN|FS|FR|FN|FP|DS|DE|DG|AN|DN|CD|LI|NG|AG|PG|FG)\\s)|" +
			"\\s(?=IN\\s([^I]|I[^N]|IN[^\\s]))"; // Otrais gadījums īpaši šķirklim Indija.
	protected static String removePattern = "(?<=[\\s()])(@5|@2)((?=[\\s()])|$)";
	
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
		
		File dicFolder = new File(inputDataPath + "dic/");
		if(!dicFolder.exists()) dicFolder.mkdirs();

		File[] listOfFiles = folder.listFiles();
		for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (!fileName.endsWith(".doc"))
				continue;
			BufferedWriter dicOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(inputDataPath + "dic/" +fileName.substring(0, fileName.length()-".doc".length()) + ".dic"), "Windows-1257"));
			
			String[] entities = DocLoader.loadDictionary(inputDataPath + fileName);
			for (String e : entities)
			{
				String[] elements = convertEntry(e);
				printEntry(elements, dicOut);
			}
			dicOut.flush();
			dicOut.close();
			
			System.out.println(fileName + " [Pabeigts]");
			
		}
		System.out.println("Viss pabeigts!"); // paziņujoms par visu failu pabeigšanu
	}
	

	/**
	 * Sadala šķirkli elementos un veic citas normalizācijas.
	 */
	protected static String[] convertEntry(String entry)
	{
		//entry = "VR " + entry.trim();
		entry = entry.replaceAll(removePattern, "");
		entry = entry.replaceAll("[\u2013\u2014]", "-"); // Aizstāj n-dash un m-dash ar parasto defisi u002D
		entry = entry.replaceAll("[\u2018\u2019]", "'"); // Aizstāj ar apastrofu vienpēdiņas
		entry = entry.replaceAll(" \\.(?!\\.)", ". ");
		entry = entry.replaceAll("\\s\\s+", " ");
		// Te tiek nodrošināts, ka, ja šķirkļa vārds sakrīt ar kādu no
		// marķieriem, tas netiek atdalīts atsevšķā rindā no VR.
		String[] rows = entry.split(splitPattern);
		if (rows.length > 0)
			rows[0] = "VR " + rows[0];
		return rows;
	}
	
	/**
	 * Elementos sadalīto šķirkli izdrukā dotajā izejas failā, pabeidzot to ar
	 * tukšu rindu.
	 */
	protected static void printEntry(String[] entryElements, BufferedWriter out)
			throws IOException
	{
		for (String elem : entryElements)
		{
			out.write(elem.trim());
			out.newLine();
		}
		out.newLine();
		out.flush();
	}
}
