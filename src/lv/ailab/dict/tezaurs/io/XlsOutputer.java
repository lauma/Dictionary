package lv.ailab.dict.tezaurs.io;

import lv.ailab.dict.tezaurs.checker.Stats;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.FileOutputStream;
import java.io.IOException;

//bibliotēka *.xls failu apstrādei

/**
 * Objekts, kas nodrošina DictionaryChecker savākto datu izdrukāšanu Excel
 * failā.
 * @author Lauma, Gunārs Danovskis
 */
public class XlsOutputer 
{
	public HSSFWorkbook workbook;
    public HSSFSheet sheet;
    protected String filePath;
    protected int fileCount;
    
    public XlsOutputer(String path)
    {
    	filePath = path;
        workbook  = new HSSFWorkbook();
        sheet =  workbook.createSheet("sheet_1");
        fileCount = 0;
    }
    
    /**
     * Uztaisa failu un ieraksta tajā datus.
     * @throws IOException
     */
    public void flush()
    throws IOException
    {
    	// izejas plūmas izveidošana
    	FileOutputStream fileOut =  new FileOutputStream(filePath);
    	//datu ierakstīšana
        workbook.write(fileOut);
    }
    
	/**
	 * Izveido *.xls failā jaunu rindu ar katras kolonnas kopsummu.	
	 */
	public void sumTable()
	{
		//katru reizi, kad tiek izpildīta metode, tiek summēti dati līdz pēdējai aizpildītajai rindai
		int lastRowNr = sheet.getLastRowNum() + 1;
		HSSFRow sum_row = sheet.createRow((short)2);
		sum_row.createCell(0).setCellValue("Kopā:");
		String[] columns = {"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
		int cell = 1;
		for (String col : columns)
		{
			sum_row.createCell(cell).setCellFormula("SUM("+col+"4:"+col+lastRowNr+")");
			cell++;
		}
	}
	
	/**
	 * Iepriekš savāktos datus ieliek *.xls failā.
	 */
	public void addNewStatsRow(Stats stats, String fileName)
	{
		// masīvs ar kolonnu nosaukumiem
		String[] header = {" ","Vārdi","Šķirkļi","IN","IN1","IN2","IN3",
				"IN4","IN5+","CD","DN","IN + DN","FR","NO","PI"}; 

		String[] parts = fileName.split("\\."); // apstrādāta faila nosaukuma ieguve
		String part1 = parts[0];

		stats.inDnCd = stats.inCount + stats.cdCount + stats.dnCount;
		HSSFRow rowhead = sheet.createRow((short)0);
		// tabulas šūnu stila pielāgošana
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		for(int i = 0; i<header.length; i++)
		{
			rowhead.createCell(i).setCellValue(header[i]);
			rowhead.getCell(i).setCellStyle(headerStyle);
		}
		//tabulas šūnu aizpildīšana ar statitikas datiem
		HSSFRow row = sheet.createRow((short)fileCount+3);
		row.createCell(0).setCellValue(part1);
		row.createCell(1).setCellValue(stats.wordCount);
		row.createCell(2).setCellValue(stats.entryCount);
		row.createCell(3).setCellValue(stats.inCount);
		row.createCell(4).setCellValue(stats.in1Count);
		row.createCell(5).setCellValue(stats.in2Count);
		row.createCell(6).setCellValue(stats.in3Count);
		row.createCell(7).setCellValue(stats.in4Count);
		row.createCell(8).setCellValue(stats.in5Count);
		row.createCell(9).setCellValue(stats.cdCount);
		row.createCell(10).setCellValue(stats.dnCount);
		row.createCell(11).setCellValue(stats.inDnCd);
		row.createCell(12).setCellValue(stats.frCount);
		row.createCell(13).setCellValue(stats.noCount);
		row.createCell(14).setCellValue(stats.piCount);
		
		fileCount++;
	}
}
