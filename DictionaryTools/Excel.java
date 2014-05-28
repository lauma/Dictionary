package DictionaryTools;

//excel
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excel 
{
	public static HSSFWorkbook Workbook;
    public static HSSFSheet Sheet;
    private static String FilePath = "./files/vardusk.xls";
    
    //Excel file
    public static void CreateXls()
    {
        Workbook  = new HSSFWorkbook();
        Sheet =  Workbook.createSheet("sheet_1");
        
    }
    
    public static void Write()
    throws IOException
    {
    	File TestFolder = new File("./files");

		if(!TestFolder.exists())
		{
			System.out.println("Kïûda - nav atrasta mape 'files'.\n"
					+ "Izveidojiet mapi 'files'!");
			return;
		}
    	
    	
    	FileOutputStream fileOut =  new FileOutputStream(FilePath);
        Workbook.write(fileOut);
    }
}
