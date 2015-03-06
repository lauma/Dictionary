package  DictionaryTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Klase Stats ietver sevī metodes statistikas datu uzkrāšanai un izvadīšanai
 * Excel tabulās.
 * @author Lauma, Gunārs Danovskis
 */
public class Stats
{
	protected String filename = "./files/vardusk.xls";
	//protected Excel table = new Excel();

	public String FileName = "";
	public int wordCount = 0;
	public int entryCount = 0;
	public int inCount = 0; 
	public int in1Count = 0; 
	public int in2Count = 0; 
	public int in3Count = 0; 
	public int in4Count = 0; 
	public int in5Count = 0; 
	public int cdCount = 0;
	public int dnCount = 0;
	public int frCount = 0;
	public int piCount = 0;
	public int noCount = 0;
	public int inDnCd = 0;

	/**
	 * Savāc statistikas datus par šķirkli.
	 */
	public void collectStats(String entry)
	{
		// marķiera IN skaita ieguve
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		if (entryInf.matches("^IN\\s.*$"))
		{
			String withoutIn = entryInf.substring(3).trim();
			int index = StringUtils.findNumber(withoutIn);

			if(!entryInf.matches("^..+\\sCD\\s.*$") && !entryInf.matches("^..+\\sDN\\s.*$"))
				inCount++;
			// marķiera IN 1 skaita ieguve
			if(index == 1)
				in1Count++;
			// marķiera IN 2 skaita ieguve
			if(index == 2)
				in2Count++;
			// marķiera IN 3 skaita ieguve
			if(index == 3)
				in3Count++;
			// marķiera IN 4 skaita ieguve
			if(index == 4)
				in4Count++;
			// marķiera IN 5 (un tālāko) skaita ieguve
			if(index >= 5)
				in5Count++;
		}
		// marķiera CD skaita ieguve
		if (entryInf.matches("^.*\\sCD\\s.*$") || entryInf.matches("^CD\\s.*$"))
			cdCount++;
		// marķiera DN skaita ieguve
		if (entryInf.matches("^.*\\sDN\\s.*$") || entryInf.matches("^DN\\s.*$"))
			dnCount++;
		// marķiera NO skaita ieguve
		Pattern noPat = Pattern.compile("\\sNO\\s");
		Matcher no = noPat.matcher(entryInf);
		int visiNo = 0;
		while (no.find())
			visiNo++;
		noCount = noCount + visiNo;
		// marķiera PI skaita ieguve
		Matcher pi = Pattern.compile("\\sPI(?=\\s)").matcher(entryInf);
		int piSkaits = 0;
		while (pi.find())
			piSkaits++;
		piCount = piCount + piSkaits;
		// marķiera FR skaita ieguve
		Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
		Matcher fr = frPat.matcher(entryInf);
		int visiFr = 0;
		while(fr.find())
			visiFr++;
		frCount = frCount + visiFr;
	}

	/**
	 * Iepriekš savāktos datus ieliek *.xls failā.
	 */
	public void writeInTable(Excel table, String FileName, int FileCount)
	{
		// masīvs ar kolonnu nosaukumiem
		String[] header = {" ","Vārdi","Šķirkļi","IN","IN1","IN2","IN3",
				"IN4","IN5+","CD","DN","IN + DN","FR","NO","PI"}; 

		String[] parts = FileName.split("\\."); // apstrādāta faila nosaukuma ieguve
		String part1 = parts[0];

		inDnCd = inCount + cdCount + dnCount;
		HSSFRow rowhead = Excel.Sheet.createRow((short)0);
		// tabulas šūnu stila pielāgošana
		HSSFCellStyle headerStyle=Excel.Workbook.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_RIGHT);

		for(int i = 0; i<header.length; i++)
		{
			rowhead.createCell(i).setCellValue(header[i]);
			rowhead.getCell(i).setCellStyle(headerStyle);
		}
		//tabulas šūnu aizpildīšana ar statitikas datiem
		HSSFRow row = Excel.Sheet.createRow((short)FileCount+3);
		row.createCell(0).setCellValue(part1);
		row.createCell(1).setCellValue(wordCount);
		row.createCell(2).setCellValue(entryCount);
		row.createCell(3).setCellValue(inCount);
		row.createCell(4).setCellValue(in1Count);
		row.createCell(5).setCellValue(in2Count);
		row.createCell(6).setCellValue(in3Count);
		row.createCell(7).setCellValue(in4Count);
		row.createCell(8).setCellValue(in5Count);
		row.createCell(9).setCellValue(cdCount);
		row.createCell(10).setCellValue(dnCount);
		row.createCell(11).setCellValue(inDnCd);
		row.createCell(12).setCellValue(frCount);
		row.createCell(13).setCellValue(noCount);
		row.createCell(14).setCellValue(piCount);
	}


	/**
	 * Izveido *.xls failā jaunu rindu ar katras kolonnas kopsummu.	
	 */
	public static void sumTable(Excel table)
	{
		//katru reizi, kad tiek izpildīta metode, tiek summēti dati līdz pēdējai aizpildītajai rindai
		int lastRowNr = Excel.Sheet.getLastRowNum() + 1;
		HSSFRow sum_row = Excel.Sheet.createRow((short)2);
		sum_row.createCell(0).setCellValue("Kopā:");
		String[] columns = {"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
		int cell = 1;
		for (String col : columns)
		{
			sum_row.createCell(cell).setCellFormula("SUM("+col+"4:"+col+lastRowNr+")");
			cell++;
		}
	}
}
