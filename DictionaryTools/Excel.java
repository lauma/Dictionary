/*****************
Autors: Gunārs Danovskis
Pēdējais labošanas datums: 28.05.2014

Klases mērķis:
	Klase Excel ietver sevī metodes *.xls faila izveidei un ierakstīšanai
*****************/

package DictionaryTools; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//bibliotēka *.xls failu apstrādei
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
    //metode kas ieraksta datus *.xls failā
    public static void write()
    throws IOException
    {
    	//pārbaude vai ir atrodama mape "files"
    	File TestFolder = new File("./files");

		if(!TestFolder.exists())
		{
			System.out.println("Error - cant find folder 'files'.\n"
					+ "Please, create folder 'files'!");
			return;
		}
    	
    	// izejas plūmas izveidošana
    	FileOutputStream fileOut =  new FileOutputStream(FilePath);
    	//datu ierakstīšana
        Workbook.write(fileOut);
    }
}
