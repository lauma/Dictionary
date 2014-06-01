/*****************
Autors: Gun�rs Danovskis
P�d�jais labo�anas datums: 28.05.2014

Klases m�r�is:
	Klase ReferenceList ietver sev� funkcijas avotu saraksta izveidei
*****************/

package DictionaryTools; //Kop�ga pakotnetne, kur� ir iek�autas visas klases veiksm�gai programmas darb�bai

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//bibliot�ka *.doc failu apstr�dei
import org.apache.poi.hwpf.HWPFDocument; 
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ReferenceList
{
public static String[] references; // publisks klases main�gais kas tiek
									// izmantots visu v�rdn�cas avotu glab��anai
	
	public static void getReferData()
	throws IOException
	{
		// p�rbaude vai ieejas fails eksist�
		File testFile = new File("./files/prog files/zLI.doc");

		if(!testFile.exists())
		{
			System.out.println("Error - can't find file 'zLI.doc'.\n" // pazi�ojums lietot�jam
					+ "Please put 'zLI.doc' file in to 'prog files'!");
			System.exit(0);
			return;
		}
		
		File referFile = null;
		FileInputStream fis = null;
		WordExtractor referExtract = null;
		// tiek nor�d�ts ce�� uz avotu failu
		referFile = new File("./files/prog files/zLI.doc");
		fis = new FileInputStream(referFile.getAbsolutePath());
		
		HWPFDocument referDoc=new HWPFDocument(fis);
		referExtract = new WordExtractor(referDoc);
		// faila saturs tiek ielikts pagiadu mas�v�
		String[] referTemp = referExtract.getParagraphText();
        references = new String[referTemp.length];
        // dati tiek p�rrakst�ti no pagaidu mas�va galvenaj� mas�v�
        // lai galvenaj� mas�v� b�tu tikai avotu sa�sin�jumi
        for(int j=0; j<referTemp.length; j++)
        {
        	if(referTemp[j] != null)
        	{
        		references[j] = referTemp[j].substring(0, referTemp[j].indexOf("\t"));
        	}  
        }
	}
}
