/*****************
Autors: Gunârs Danovskis
Pçdçjais laboðanas datums: 28.05.2014

Klases mçríis:
	Klase Excel ietver sevî metodes *.xls faila izveidei un ierakstîðanai
*****************/

package DictionaryTools; //Kopîga pakotnetne, kurâ ir iekïautas visas klases veiksmîgai programmas darbîbai

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//bibliotçka *.xls failu apstrâdei
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excel 
{
	public static HSSFWorkbook Workbook;
    public static HSSFSheet Sheet;
    private static String FilePath = "./files/vardusk.xls";
    
    //*.xls faila izveides metode
    public static void createXls()
    {
        Workbook  = new HSSFWorkbook();
        Sheet =  Workbook.createSheet("sheet_1");
        
    }
    //metode kas ieraksta datus *.xls failâ
    public static void write()
    throws IOException
    {
    	//pârbaude vai ir atrodama mape "files"
    	File TestFolder = new File("./files");

		if(!TestFolder.exists())
		{
			System.out.println("Kïûda - nav atrasta mape 'files'.\n"
					+ "Izveidojiet mapi 'files'!");
			return;
		}
    	
    	// izejas plûmas izveidoðana
    	FileOutputStream fileOut =  new FileOutputStream(FilePath);
    	//datu ierakstîðana
        Workbook.write(fileOut);
    }
}
