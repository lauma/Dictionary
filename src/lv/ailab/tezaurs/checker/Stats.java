package  lv.ailab.tezaurs.checker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lv.ailab.tezaurs.utils.StringUtils;

/**
 * Klase Stats ietver sevī metodes statistikas datu uzkrāšanai un izvadīšanai
 * Excel tabulās.
 * @author Lauma, Gunārs Danovskis
 */
public class Stats
{
	//protected String filename = "./files/vardusk.xls";
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
	public void collectInnerStats(String entry)
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



}
