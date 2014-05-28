package DictionaryTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ReferenceList
{
public static String[] References;
	
	public static void GetReferData()
	throws IOException
	{
		File TestFile = new File("./files/prog files/zLI.doc");

		if(!TestFile.exists())
		{
			System.out.println("Kïûda - nav atrasts fails 'zLI.doc'.\n"
					+ "Ievietojiet 'zLI.doc' failu mapç 'prog files'!");
			return;
		}
		
		File ReferFile = null;
		FileInputStream Fis = null;
		WordExtractor ReferExtract = null;
		ReferFile = new File("./files/prog files/zLI.doc");
		Fis = new FileInputStream(ReferFile.getAbsolutePath());
		
		HWPFDocument ExcepDoc=new HWPFDocument(Fis);
		ReferExtract = new WordExtractor(ExcepDoc);
		
		String[] ReferTemp = ReferExtract.getParagraphText();
        References = new String[ReferTemp.length];

        for(int j=0; j<ReferTemp.length; j++)
        {
        	if(ReferTemp[j] != null)
        	{
        		References[j] = ReferTemp[j].substring(0, ReferTemp[j].indexOf("\t"));
        	}  
        }
	}
}
