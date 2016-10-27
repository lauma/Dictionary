/*****************
Autors: Gunārs Danovskis
Pēdējais labošanas datums: 28.05.2014

Klases mērķis:
	Klase ExceptionList ietver sevī funkcijas izņēmumu saraksta izveidei
*****************/

package lv.ailab.dict.tezaurs.checker; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

//bibliotēka *.doc failu apstrādei

public class ExceptionList
{
	/**
	 * Izņēmumu saraksts. Tam jābūt sakārtotam.
	 */
	public static String[] exceptions;

	/**
	 * Izņēmumu saraksta ielādēšana no .doc faila.
	 */
	public static void loadExceptions(String path)
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
        exceptions = Arrays.stream(excepExtract.getParagraphText())
				.map(e -> e.trim()).sorted().toArray(size -> new String[size]);
	}

	/**
	 * Pārbauda, vai meklējamais šķirklis ir izņēmumu sarakstā, pieņemot, ka
	 * izņēmumu saraksts ir sakārtots.
	 */
	public static boolean isException (DictionaryFragment.Entry query)
	{
		int index = Arrays.binarySearch(exceptions, query.fullText);
		return index >= 0;
	}
	
}
