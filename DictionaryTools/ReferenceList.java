/*****************
Autors: Gunârs Danovskis
Pçdçjais laboðanas datums: 28.05.2014

Klases mçríis:
	Klase ReferenceList ietver sevî funkcijas avotu saraksta izveidei
*****************/

package DictionaryTools; //Kopîga pakotnetne, kurâ ir iekïautas visas klases veiksmîgai programmas darbîbai

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//bibliotçka *.doc failu apstrâdei
import org.apache.poi.hwpf.HWPFDocument; 
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ReferenceList
{
public static String[] references; // publisks klases mainîgais kas tiek
									// izmantots visu vârdnîcas avotu glabâðanai
	
	public static void getReferData()
	throws IOException
	{
		// pârbaude vai ieejas fails eksistç
		File testFile = new File("./files/prog files/zLI.doc");

		if(!testFile.exists())
		{
			System.out.println("Error - can't find file 'zLI.doc'.\n" // paziòojums lietotâjam
					+ "Please put 'zLI.doc' file in to 'prog files'!");
			System.exit(0);
			return;
		}
		
		File referFile = null;
		FileInputStream fis = null;
		WordExtractor referExtract = null;
		// tiek norâdîts ceïð uz avotu failu
		referFile = new File("./files/prog files/zLI.doc");
		fis = new FileInputStream(referFile.getAbsolutePath());
		
		HWPFDocument referDoc=new HWPFDocument(fis);
		referExtract = new WordExtractor(referDoc);
		// faila saturs tiek ielikts pagiadu masîvâ
		String[] referTemp = referExtract.getParagraphText();
        references = new String[referTemp.length];
        // dati tiek pârrakstîti no pagaidu masîva galvenajâ masîvâ
        // lai galvenajâ masîvâ bûtu tikai avotu saîsinâjumi
        for(int j=0; j<referTemp.length; j++)
        {
        	if(referTemp[j] != null)
        	{
        		references[j] = referTemp[j].substring(0, referTemp[j].indexOf("\t"));
        	}  
        }
	}
}
