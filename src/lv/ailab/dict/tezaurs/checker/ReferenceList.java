package lv.ailab.dict.tezaurs.checker; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//bibliotēka *.doc failu apstrādei

/**
 * Avotu saraksts.
 * @author Lauma, Gunārs Danovskis
 */
public class ReferenceList
{
	public Set<String> references; // publisks klases mainīgais kas tiek
									// izmantots visu vārdnīcas avotu glabāšanai
	
	/**
	 * Uzkonstruē avotu sarakstu no dota faila.
	 * @param fileName
	 * @throws IOException
	 */
	public ReferenceList(String fileName)
	throws IOException
	{
		// pārbaude vai ieejas fails eksistē
		File referFile = new File(fileName);

		if(!referFile.exists())
		{
			System.out.println("Error - can't find file 'zLI.doc'.\n" // paziņojums lietotājam
					+ "Please put 'zLI.doc' file in to 'prog files'!");
			System.exit(0);
			return;
		}
		
		HWPFDocument referDoc = new HWPFDocument(
				new FileInputStream(referFile.getAbsolutePath()));
		WordExtractor referExtract = new WordExtractor(referDoc);
		// faila saturs tiek ielikts pagiadu masīvā
		String[] referTemp = referExtract.getParagraphText();
        references = new HashSet<String>();
        // Dati tiek pārrakstīti no pagaidu masīva galvenajā datu struktūrā, lai
        // tajā paliek avotu saīsinājumi.
        for(int j=0; j<referTemp.length; j++)
        {
        	if(referTemp[j] != null && !referTemp[j].trim().equals(""))
        	{
        		String newRef = referTemp[j].substring(0, referTemp[j].indexOf("\t")).trim(); 
        		if (references.contains(newRef))
        			System.out.println("Reference \"" + newRef + "\" occours more than once in the references list.");
        		references.add(newRef);
        	}  
        }
	}
	
	/**
	 * Pārbauda, vai visi šķirķļa atsauču daļā uzskaitītie avoti ir atpazīstami
	 * un atgriež neatpazītos.
	 * @param entryRefer šķirkļa daļa, kas satur atsauces (bez []).
	 */
	public ArrayList<String> verifyReferences(String entryRefer)
	{
		ArrayList<String> unrec = new ArrayList<String> ();
		// izdalītas visas šķirklī ierakstītās atsauces
		String[] parts = entryRefer.split(", "); 
		if (parts.length < 1) return unrec;
		//cikls iet pa škirkļa atsauču sarakstam
		for(String part : parts)
		{
			part = part.trim();
			if(part.length() <= 1)
				continue;
					// Parasta atsauce.
			if ( ! (references.contains(part) ||
					// Atsauce ar papildinformāciju - žurnāli vai avīzes.
					part.contains("-") && references.contains(part.substring(0, part.indexOf('-')).trim())) )
				unrec.add(part);
		}
				
		return unrec;
	}
}
