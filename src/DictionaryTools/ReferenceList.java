package DictionaryTools; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

//bibliotēka *.doc failu apstrādei
import org.apache.poi.hwpf.HWPFDocument; 
import org.apache.poi.hwpf.extractor.WordExtractor;

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
        			System.out.println("Reference \"" + newRef + "\" occours more than once in the reference list.");
        		references.add(newRef);
        	}  
        }
	}
	
	/**
	 * Saskaita šķirklī atsauču daļā atpazītos avotus.
	 * @param entryRefer šķirkļa daļa, kas satur atsauces.
	 */
	public int referCount(String entryRefer)
	{
		int count = 0;
		// izdalītas visas šķirklī ierakstītās atsauces
		String[] parts = entryRefer.split(", "); 
		if (parts.length < 1) return 0;
		//cikls iet pa škirkļa atsauču sarakstam
		for(String part : parts)
		{
			//if(part.length() <= 0)
			//	return 1;
			//else
			//{
				// Parasta atsauce.
				if (references.contains(part))
					count++;
				// Atsauce ar papildinformāciju - žurnāli vai avīzes.
				else if (part.contains("-"))
				{
					if (references.contains(part.substring(0, part.indexOf('-')).trim()))
						count++;
				}
			//}
		}
				
		return count; //atgriež gala vērtību
	}
}
