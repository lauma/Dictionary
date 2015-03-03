/*****************
Autors: Gunārs Danovskis
Pēdējais labošanas datums: 28.05.2014

Klases mērķis:
	Klase ReferenceList ietver sevī funkcijas avotu saraksta izveidei
*****************/

package DictionaryTools; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//bibliotēka *.doc failu apstrādei
import org.apache.poi.hwpf.HWPFDocument; 
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ReferenceList
{
public static String[] references; // publisks klases mainīgais kas tiek
									// izmantots visu vārdnīcas avotu glabāšanai
	
	public static void getReferData()
	throws IOException
	{
		// pārbaude vai ieejas fails eksistē
		File testFile = new File("./files/prog files/zLI.doc");

		if(!testFile.exists())
		{
			System.out.println("Error - can't find file 'zLI.doc'.\n" // paziņojums lietotājam
					+ "Please put 'zLI.doc' file in to 'prog files'!");
			System.exit(0);
			return;
		}
		
		File referFile = null;
		FileInputStream fis = null;
		WordExtractor referExtract = null;
		// tiek norādīts ceļš uz avotu failu
		referFile = new File("./files/prog files/zLI.doc");
		fis = new FileInputStream(referFile.getAbsolutePath());
		
		HWPFDocument referDoc=new HWPFDocument(fis);
		referExtract = new WordExtractor(referDoc);
		// faila saturs tiek ielikts pagiadu masīvā
		String[] referTemp = referExtract.getParagraphText();
        references = new String[referTemp.length];
        // dati tiek pārrakstīti no pagaidu masīva galvenajā masīvā
        // lai galvenajā masīvā būtu tikai avotu saīsinājumi
        for(int j=0; j<referTemp.length; j++)
        {
        	if(referTemp[j] != null)
        	{
        		references[j] = referTemp[j].substring(0, referTemp[j].indexOf("\t"));
        	}  
        }
	}
}
