package lv.ailab.tezaurs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lv.ailab.tezaurs.checker.Markers;
import lv.ailab.tezaurs.io.DocLoader;

public class DictionaryToDicUI
{
	public static String inputDataPath = "./dati/";

	protected static String splitPattern =
			"\\s(?=(GR|RU|NO|NS|PI|PN|FS|FR|FN|FP|DS|DE|DG|AN|DN|CD|LI|NG|AG|PG|FG)\\s)|" +
			"\\s(?=IN\\s([^I]|I[^N]|IN[^\\s]))"; // Otrais gadījums īpaši šķirklim Indija.
	protected static String[][] cleanupPatterns = {
			{"(?<=[(\\[])\\s*(@5|@2)\\s", ""},
			{"\\s(@5|@2)\\s*(?=[;,.?!)\\]])", ""},
			{"\\s(@5|@2)\\s", " "},
			{"[\u2013\u2014]", "-"},	// Aizstāj n-dash un m-dash ar parasto defisi u002D
			{"[\u2018\u2019]", "'"},	// Aizstāj ar apastrofu vienpēdiņas
			{" \\.(?!\\.)", ". "},		// Pārvieto atstarpes aiz punkta
			{"\\s\\s+", " "},
	};	//"(?<=[\\s()])(@5|@2)(\\s|$)|\\s(@5|@2)((?=[\\s()])|$)";
	
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
		System.out.println("Viss pabeigts!"); // paziņojums par visu failu pabeigšanu
	}
	

	/**
	 * Sadala šķirkli elementos un veic citas normalizācijas.
	 * Tukšām rindām atgriež tukšu masīvu.
	 */
	protected static String[] convertEntry(String entry)
	{
		if (entry.trim().length() < 1) return new String[] {};
		String[] rows = entry.split(splitPattern);
		for (int i = 0; i < rows.length; i++)
		{
			for (String[] replPat : cleanupPatterns)
				rows[i] = rows[i].replaceAll(replPat[0], replPat[1]);
			// Lai nesanāk tā, ka pēc tīrīšanas aiz marķiera vairs nav atstarpes.
			Matcher securityCheck = Pattern.compile(Markers.regexp + "([^ ].*)").matcher(rows[i]);
			if (securityCheck.matches())
				rows[i] = securityCheck.group(1) + " " + securityCheck.group(2);
		}

		// Te tiek nodrošināts, ka, ja šķirkļa vārds sakrīt ar kādu no
		// marķieriem, tas netiek atdalīts atsevšķā rindā no VR.
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
		int rows = 0;
		for (String elem : entryElements)
		{
			elem = elem.trim();
			if (elem.length() < 1) continue;
			if (elem.length() == 2) elem = elem + " "; // Jo tukšiem elementiem aiz elementa idenentifikatora jāseko vienai atstarpei.
			out.write(elem);
			out.newLine();
			rows++;
		}
		if (rows > 0) out.newLine();
		out.flush();
	}
}
