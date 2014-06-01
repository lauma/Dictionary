/*****************
Autors: Gun�rs Danovskis
P�d�jais labo�anas datums: 28.05.2014

Klases m�r�is:
	Klase Excel ietver sev� metodes *.xls faila izveidei un ierakst��anai
*****************/

package DictionaryTools; //Kop�ga pakotnetne, kur� ir iek�autas visas klases veiksm�gai programmas darb�bai

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//bibliot�ka *.xls failu apstr�dei
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
    //metode kas ieraksta datus *.xls fail�
    public static void write()
    throws IOException
    {
    	//p�rbaude vai ir atrodama mape "files"
    	File TestFolder = new File("./files");

		if(!TestFolder.exists())
		{
			System.out.println("K��da - nav atrasta mape 'files'.\n"
					+ "Izveidojiet mapi 'files'!");
			return;
		}
    	
    	// izejas pl�mas izveido�ana
    	FileOutputStream fileOut =  new FileOutputStream(FilePath);
    	//datu ierakst��ana
        Workbook.write(fileOut);
    }
}
