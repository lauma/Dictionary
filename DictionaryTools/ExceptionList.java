package DictionaryTools;

//word
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ExceptionList
{
	public static String[] Exceptions;
	
	public static void GetExceptData()
	throws IOException
	{
		File TestFolder = new File("./files/prog files/exceptions.doc");

		if(!TestFolder.exists())
		{
			System.out.println("Kïûda - nav atrasts fails exceptions.doc.\n"
					+ "Ievietojiet 'exceptions.doc' failu mapç 'prog files'!");
			return;
		}
		
		File ExcepFile = null;
		FileInputStream Fis = null;
		WordExtractor ExcepExtract = null;
		ExcepFile = new File("./files/prog files/exceptions.doc");
		Fis = new FileInputStream(ExcepFile.getAbsolutePath());
		
		HWPFDocument ExcepDoc=new HWPFDocument(Fis);
		ExcepExtract = new WordExtractor(ExcepDoc);
		
		//izòçmumu saraksts
        Exceptions = ExcepExtract.getParagraphText();
	}
	
}
