/*****************
Autors: Gunārs Danovskis
Pēdējais labošanas datums: 28.05.2014

Klases mērķis:
	Klase ExceptionList ietver sevī funkcijas izņēmumu saraksta izveidei
*****************/

package lv.ailab.tezaurs.checker; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//bibliotēka *.doc failu apstrādei
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ExceptionList
{
	public static String[] exceptions;	// publisks klases mainīgais kas tiek
										// izmantots neapskatāmo šķirkļu masīvam
	
	public static void getExceptData(String path) //metode lai iegūtu datus par izņēmumu šķirkļiem
	throws IOException
	{

		File excepFile = new File(path);
		FileInputStream Fis = null;
		WordExtractor excepExtract = null;
		// tiek norādīts ceļš uz avotu failu
		Fis = new FileInputStream(excepFile.getAbsolutePath());
		
		HWPFDocument excepDoc=new HWPFDocument(Fis);
		excepExtract = new WordExtractor(excepDoc);
		
		// faila saturs tiek ielikts izņēmumu sarakstā
		// šoreiz viss saturs tiek likts masīvā,
		//jo ir vairāki šķirkļi ar vienādu šķirkļa vārdu 
        exceptions = excepExtract.getParagraphText();
	}
	
}
