/*****************
Autors: Gunārs Danovskis
Pēdējais labošanas datums: 28.05.2014

Klases mērķis:
	Klase Stats ietver sevī metodes statistikas datu iegūšanai
	un uzglabāšanai
*****************/
package  DictionaryTools; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

//excel
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;


public class Stats
{
	String filename = "./files/vardusk.xls";
    Excel table = new Excel();
    
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
	
	//metode kas ievāc statistikas datus par šķirkli
	public static void Statistics(String entry, Stats data)
	{
		// marķiera IN skaita ieguve
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		if (entryInf.matches("^IN\\s.*$"))
		{
			String withoutIn = entryInf.substring(3).trim();
			int index = StringUtils.findNumber(withoutIn);
			
			if(!entryInf.matches("^..+\\sCD\\s.*$") && !entryInf.matches("^..+\\sDN\\s.*$"))
				data.inCount++;
			// marķiera IN 1 skaita ieguve
			if(index == 1)
			{
				data.in1Count++;
			}
			// marķiera IN 2 skaita ieguve
			if(index == 2)
			{
				data.in2Count++;
			}
			// marķiera IN 3 skaita ieguve
			if(index == 3)
			{
				data.in3Count++;
			}
			// marķiera IN 4 skaita ieguve
			if(index == 4)
			{
			data.in4Count++;
			}
			// marķiera IN 5 skaita ieguve
			if(index == 5)
			{
				data.in5Count++;
			}
		}
		// marķiera CD skaita ieguve
		if (entryInf.matches("^.*\\sCD\\s.*$") || entryInf.matches("^CD\\s.*$"))
		{
			data.cdCount++;
		}
		// marķiera DN skaita ieguve
		if (entryInf.matches("^.*\\sDN\\s.*$") || entryInf.matches("^DN\\s.*$"))
		{
			data.dnCount++;
		}
		// marķiera NO skaita ieguve
		Pattern noPat = Pattern.compile("\\sNO\\s");
		Matcher no = noPat.matcher(entryInf);
		int visiNo = 0;
		while (no.find())
		{
			visiNo++;
		}
		data.noCount = data.noCount + visiNo;
		// marķiera PI skaita ieguve
		Matcher pi = Pattern.compile("\\sPI(?=\\s)").matcher(entryInf);
		int piSkaits = 0;
		while (pi.find())
		{
			piSkaits++;
		}
		data.piCount = data.piCount + piSkaits;
		// marķiera FR skaita ieguve
		Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
		Matcher fr = frPat.matcher(entryInf);
		int visiFr = 0;
		while(fr.find())
		{
			visiFr++;
		}
		data.frCount = data.frCount + visiFr;
	}
	// metode kas ieliek datus *.xls failā	
	public static void FillTable(Excel table, Stats data , String FileName, int FileCount)
	{
		// masīvs ar kolonnu nosaukumiem
		String[] header = {" ","Vārdi","Šķirkļi","IN","IN1","IN2","IN3",
							"IN4","IN5","CD","DN","IN + DN","FR","NO","PI"}; 
		
		String[] parts = FileName.split("\\."); // apstrādāta faila nosaukuma ieguve
		String part1 = parts[0];
		
		data.inDnCd = data.inCount + data.cdCount + data.dnCount;
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
	       row.createCell(1).setCellValue(data.wordCount);
	       row.createCell(2).setCellValue(data.entryCount);
	       row.createCell(3).setCellValue(data.inCount);
	       row.createCell(4).setCellValue(data.in1Count);
	       row.createCell(5).setCellValue(data.in2Count);
	       row.createCell(6).setCellValue(data.in3Count);
	       row.createCell(7).setCellValue(data.in4Count);
	       row.createCell(8).setCellValue(data.in5Count);
	       row.createCell(9).setCellValue(data.cdCount);
	       row.createCell(10).setCellValue(data.dnCount);
	       row.createCell(11).setCellValue(data.inDnCd);
	       row.createCell(12).setCellValue(data.frCount);
	       row.createCell(13).setCellValue(data.noCount);
	       row.createCell(14).setCellValue(data.piCount);
	}
	// metode kas izveido *.xls failā jaunu rindu ar katras kolonnas kopsummu		
	public static void SumTable(Excel table)
	{
		//katru reizi tiek kad tiek izpildīta metode tiek summēti dati līdz pēdējai aizpildītajai rindai
		int lastRowNr = Excel.Sheet.getLastRowNum() + 1;
		HSSFRow sum_row = Excel.Sheet.createRow((short)2);
	       sum_row.createCell(0).setCellValue("Kopā:");
	       sum_row.createCell(1).setCellFormula("SUM(B4:B"+lastRowNr+")");
	       sum_row.createCell(2).setCellFormula("SUM(C4:C"+lastRowNr+")");
	       sum_row.createCell(3).setCellFormula("SUM(D4:D"+lastRowNr+")");
	       sum_row.createCell(4).setCellFormula("SUM(E4:E"+lastRowNr+")");
	       sum_row.createCell(5).setCellFormula("SUM(F4:F"+lastRowNr+")");
	       sum_row.createCell(6).setCellFormula("SUM(G4:G"+lastRowNr+")");
	       sum_row.createCell(7).setCellFormula("SUM(H4:H"+lastRowNr+")");
	       sum_row.createCell(8).setCellFormula("SUM(I4:I"+lastRowNr+")");
	       sum_row.createCell(9).setCellFormula("SUM(J4:J"+lastRowNr+")");
	       sum_row.createCell(10).setCellFormula("SUM(K4:K"+lastRowNr+")");
	       sum_row.createCell(11).setCellFormula("SUM(L4:L"+lastRowNr+")");
	       sum_row.createCell(12).setCellFormula("SUM(M4:M"+lastRowNr+")");
	       sum_row.createCell(13).setCellFormula("SUM(N4:N"+lastRowNr+")");
	       sum_row.createCell(14).setCellFormula("SUM(O4:O"+lastRowNr+")");
	}
}
