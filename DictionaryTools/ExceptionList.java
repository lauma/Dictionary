/*****************
Autors: Gun�rs Danovskis
P�d�jais labo�anas datums: 28.05.2014

Klases m�r�is:
	Klase ExceptionList ietver sev� funkcijas iz��mumu saraksta izveidei
*****************/

package DictionaryTools; //Kop�ga pakotnetne, kur� ir iek�autas visas klases veiksm�gai programmas darb�bai

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//bibliot�ka *.doc failu apstr�dei
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ExceptionList
{
	public static String[] exceptions;	// publisks klases main�gais kas tiek
										// izmantots neapskat�mo ��irk�u mas�vam
	
	public static void getExceptData() //metode lai ieg�tu datus par iz��mumu ��irk�iem
	throws IOException
	{
		// p�rbaude vai ieejas fails eksist�
		File testFile = new File("./files/prog files/exceptions.doc");

		if(!testFile.exists())
		{
			System.out.println("Error - can't find file 'exceptions.doc'.\n" // pazi�ojums lietot�jam
					+ "Please put 'exceptions.doc' file in to 'prog files'!");
			System.exit(0);
			return;
		}
		
		File excepFile = null;
		FileInputStream Fis = null;
		WordExtractor excepExtract = null;
		// tiek nor�d�ts ce�� uz avotu failu
		excepFile = new File("./files/prog files/exceptions.doc");
		Fis = new FileInputStream(excepFile.getAbsolutePath());
		
		HWPFDocument excepDoc=new HWPFDocument(Fis);
		excepExtract = new WordExtractor(excepDoc);
		
		// faila saturs tiek ielikts iz��mumu sarakst�
		// �oreiz viss saturs tiek likts mas�v�,
		//jo ir vair�ki ��irk�i ar vien�du ��irk�a v�rdu 
        exceptions = excepExtract.getParagraphText();
	}
	
}
