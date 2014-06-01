/*****************
Autors: Gunârs Danovskis
Pçdçjais laboðanas datums: 28.05.2014

Klases mçríis:
	Klase ExceptionList ietver sevî funkcijas izòçmumu saraksta izveidei
*****************/

package DictionaryTools; //Kopîga pakotnetne, kurâ ir iekïautas visas klases veiksmîgai programmas darbîbai

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//bibliotçka *.doc failu apstrâdei
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ExceptionList
{
	public static String[] exceptions;	// publisks klases mainîgais kas tiek
										// izmantots neapskatâmo ðíirkïu masîvam
	
	public static void getExceptData() //metode lai iegûtu datus par izòçmumu ðíirkïiem
	throws IOException
	{
		// pârbaude vai ieejas fails eksistç
		File testFile = new File("./files/prog files/exceptions.doc");

		if(!testFile.exists())
		{
			System.out.println("Error - can't find file 'exceptions.doc'.\n" // paziòojums lietotâjam
					+ "Please put 'exceptions.doc' file in to 'prog files'!");
			System.exit(0);
			return;
		}
		
		File excepFile = null;
		FileInputStream Fis = null;
		WordExtractor excepExtract = null;
		// tiek norâdîts ceïð uz avotu failu
		excepFile = new File("./files/prog files/exceptions.doc");
		Fis = new FileInputStream(excepFile.getAbsolutePath());
		
		HWPFDocument excepDoc=new HWPFDocument(Fis);
		excepExtract = new WordExtractor(excepDoc);
		
		// faila saturs tiek ielikts izòçmumu sarakstâ
		// ðoreiz viss saturs tiek likts masîvâ,
		//jo ir vairâki ðíirkïi ar vienâdu ðíirkïa vârdu 
        exceptions = excepExtract.getParagraphText();
	}
	
}
