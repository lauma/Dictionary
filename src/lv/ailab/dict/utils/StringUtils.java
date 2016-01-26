package lv.ailab.dict.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Palīgmetodes vārdnīcas simbolu virkņu apstrādei.
 * @author Gunārs Danovskis, Lauma
 */
public class StringUtils
{

	// metode, kas pārbauda kāds skaitlis ir atrodams aiz marķiera kuram to lieto
	public static int findNumber(String s)
	{
		String tmp = s;
		if (tmp.contains(" ")) tmp = tmp.substring(0, tmp.indexOf(" "));
		//regulārā izteksme skaitļa meklēšanai
		Matcher m = Pattern.compile("^\\d+").matcher(tmp);
		if (!m.find()) return 0; // ja nav atrodams tad return 0
		//tiek atgiezts pirmais objekts no grupas un pārvērsts par integer
		return Integer.parseInt(m.group(0));
	}

	//metode, kas atrod nākamo vārdu aiz metodei padotā vārda
	public static String wordAfter(String entryInf, String word)
	{
		String finalWord;
		String tempWord;
		int len = entryInf.length();
		//noteikts vārda sākums
		int index = entryInf.indexOf(word) + word.length() + 1;
		tempWord = entryInf.substring(index);
		//ja aiz atrastā vārda ir vēl citi vārdi		
		if (countSpaces(tempWord) != 0 && len > 1)
		{
			finalWord = tempWord.substring(0, tempWord.indexOf(' ')).trim();
		} else
		{
			finalWord = tempWord;
		}
		return finalWord; //atgriezsts atrastais vārds
	}

	//metode, kas atrod vārdu skaitu sķirkļī
	public static int wordCount(String entry)
	{
		int count = 1; //pieņam ka ir vismaz vien vārds
		char character[] = new char[entry.length()];
		//cikls iet pa škirkli pa vienam simbolam
		for (int i = 0; i < entry.length(); i++)
		{
			character[i] = entry.charAt(i);
			//ja ir atstarpe un pirms tās nav bijusi atstarpe tad pieskaita viens
			if ((i > 0 && character[i - 1] != ' ') && character[i] == ' ')
			{
				count++;
			}
		}
		return count; //atgriež gala vērtību
	}

	//metode, kas atrod atstarpju skaitu padotajā simbolu virknē
	public static int countSpaces(String s)
	{
		int count = 0;
		int len = s.length();
		//cikls iet pa škirkli pa vienam simbolam
		for (int i = 0; i < len; i++)
		{
			char ch = s.charAt(i);
			if (ch == ' ') // simbols ir atstarpe tad pieskaita viens
			{
				count++;
			}
		}
		return count;//atgriež gala vērtību
	}
}