package  DictionaryTools;

//excel
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;


public class Stats
{
	String filename = "./files/vardusk.xls";
    Excel Table = new Excel();
    
	public String FileName = "";
	public int WordCount = 0;
	public int EntryCount = 0;
	public int InCount = 0; 
	public int In1Count = 0; 
	public int In2Count = 0; 
	public int In3Count = 0; 
	public int In4Count = 0; 
	public int In5Count = 0; 
	public int CdCount = 0;
	public int DnCount = 0;
	public int FrCount = 0;
	public int PiCount = 0;
	public int NoCount = 0;
	public int InDnCd = 0;
	
	public static void Statistics(String Entry, Stats data)
	{
		String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
		if (EntryInf.matches("^IN\\s.*$"))
		{
			String bezIn = EntryInf.substring(3).trim();
			int index = StringUtils.FindNumber(bezIn);
			
			if(!EntryInf.matches("^..+\\sCD\\s.*$") && !EntryInf.matches("^..+\\sDN\\s.*$"))
				data.InCount++;
			
			if(index == 1)
			{
				data.In1Count++;
			}
			if(index == 2)
			{
				data.In2Count++;
			}
			if(index == 3)
			{
				data.In3Count++;
			}
			if(index == 4)
			{
			data.In4Count++;
			}
			if(index == 5)
			{
				data.In5Count++;
			}
		}
		if (EntryInf.matches("^.*\\sCD\\s.*$") || EntryInf.matches("^CD\\s.*$"))
		{
			data.CdCount++;
		}
		if (EntryInf.matches("^.*\\sDN\\s.*$") || EntryInf.matches("^DN\\s.*$"))
		{
			data.DnCount++;
		}

		Pattern noPat = Pattern.compile("\\sNO\\s");
		Matcher no = noPat.matcher(EntryInf);
		int visiNo = 0;
		while (no.find())
		{
			visiNo++;
		}
		data.NoCount = data.NoCount + visiNo;
		
		Matcher pi = Pattern.compile("\\sPI(?=\\s)").matcher(EntryInf);
		int piSkaits = 0;
		while (pi.find())
		{
			piSkaits++;
		}
		data.PiCount = data.PiCount + piSkaits;
		
		Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
		Matcher fr = frPat.matcher(EntryInf);
		int visiFr = 0;
		while(fr.find())
		{
			visiFr++;
		}
		data.FrCount = data.FrCount + visiFr;
	}
		
	public static void FillTable(Excel Table, Stats data , String FileName, int FileCount)
	{
		String[] header = {" ","Vârdi","Ðíirkïi","IN","IN1","IN2","IN3",
							"IN4","IN5","CD","DN","IN + DN","FR","NO","PI"};
		String[] parts = FileName.split("\\.");
		String part1 = parts[0];
	
		data.InDnCd = data.InCount + data.CdCount + data.DnCount;
		HSSFRow rowhead = Excel.Sheet.createRow((short)0);
		
		HSSFCellStyle headerStyle=Excel.Workbook.createCellStyle();
	    headerStyle.setAlignment(CellStyle.ALIGN_RIGHT);
				
		for(int i = 0; i<header.length; i++)
		{
			rowhead.createCell(i).setCellValue(header[i]);
			rowhead.getCell(i).setCellStyle(headerStyle);
		}
		
		HSSFRow row = Excel.Sheet.createRow((short)FileCount+3);
	       row.createCell(0).setCellValue(part1);
	       row.createCell(1).setCellValue(data.WordCount);
	       row.createCell(2).setCellValue(data.EntryCount);
	       row.createCell(3).setCellValue(data.InCount);
	       row.createCell(4).setCellValue(data.In1Count);
	       row.createCell(5).setCellValue(data.In2Count);
	       row.createCell(6).setCellValue(data.In3Count);
	       row.createCell(7).setCellValue(data.In4Count);
	       row.createCell(8).setCellValue(data.In5Count);
	       row.createCell(9).setCellValue(data.CdCount);
	       row.createCell(10).setCellValue(data.DnCount);
	       row.createCell(11).setCellValue(data.InDnCd);
	       row.createCell(12).setCellValue(data.FrCount);
	       row.createCell(13).setCellValue(data.NoCount);
	       row.createCell(14).setCellValue(data.PiCount);
	}
			
	public static void SumTable(Excel Table)
	{
		int lastRowNr = Excel.Sheet.getLastRowNum() + 1;
		HSSFRow sum_row = Excel.Sheet.createRow((short)2);
	       sum_row.createCell(0).setCellValue("Kopâ:");
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
