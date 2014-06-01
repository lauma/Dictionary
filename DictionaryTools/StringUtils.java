/*****************
Autors: Gun�rs Danovskis
P�d�jais labo�anas datums: 28.05.2014

Klases m�r�is:
	Klase StringUtils ietver sev� visas pal�gmetodes ��ir��u apstr�dei
*****************/

package DictionaryTools; //Kop�ga pakotnetne, kur� ir iek�autas visas klases veiksm�gai programmas darb�bai

//Import�t�s Java bibliot�kas
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
	// metode, kas p�rbauda vai padotais ��irklis nav tuk�s
	public static boolean isEntryEmpty(String entry, ArrayList<String> bad)
	{
		boolean notEmpty = true;
		//ja �kirklis atmetot atstarpes ir tuk�s 
		if(entry.trim().isEmpty())
    	{
			//�kirklis tiek ierakst�ts slikto sarakst�
    		bad.add("(Tuk�a rinda)" + entry);
    	}
		else
		{
			notEmpty = false; // ja nav tuk�s tad noimaina uz false
		}
		return notEmpty;
	}
	// metode, kas p�rbauda k�ds skaitlis ir atrodams aiz mar�iera kuram to lieto
	public static int findNumber(String s) 
	{
		String tmp = s;
		if (tmp.contains(" ")) tmp = tmp.substring(0, tmp.indexOf(" "));
		//regul�r� izteksme skait�a mekl��anai
		Matcher m = Pattern.compile("^\\d+").matcher(tmp); 
		if (!m.find()) return 0; // ja nav atrodams tad return 0
		//tiek atgiezts pirmais objekts no grupas un p�rv�rsts par integer
		return Integer.parseInt(m.group(0)); 
	}
	// metode, kas p�rbauda vai padotais ��irklis eksist�
	public static Boolean entryExist(String [][] inEntries, String word)
	{	
		boolean found = false;
		int len = inEntries.length;
		//cikls iet pa inEntries mas�vu un sal�dzina vai v�rds ir atrodams
		for(int i = 0; i<len; i++) 
		{
			if(word.equals(inEntries[i][0]))
			{
				found = true;
				break;
			}
		}
		return found; // tiek atgriezta atbilde vai atrasts
	}
	// metode, kas p�rbauda vai padotais �kirklis ir exclusion sarakst�
	public static boolean exclusion(String [] except, String entry) 
	{
		boolean found = false;
		int len = except.length;
		//cikls iet pa except mas�vu un sal�dzina vai v�rds ir atrodams
	    for(int i=0; i<len; i++)
	    {
	    	if(except[i].equals(entry))
	    	{
	    		found = true;
		        break;
		    }
	    }
		return found;//tiek atgriezta atbilde vai atrasts
	}
	//metode, ka p�rbauda vai padotais simbols ir lielais latvie�u burts
	public static boolean isBalticUpper(char Symbol)
	{
		String UPPER = "�����������Ԫ"; //iesp�jamie leilie burti
		boolean good = false;
		int len = UPPER.length();
		//cikls iet pa simbolu virkni un sal�dzina vai simbols ir atrodams
		for(int i = 0; i < len; i++)
		{
			if(UPPER.charAt(i) == Symbol)
			{
				good = true;
				break;
			}
		}						
		return good;//tiek atgriezta atbilde vai atrasts
	}
	//metode, kas atrod n�kamo simbolu aiz padot�s simbolu virknes
	public static char nextCh(String entry, String Symbol)
	{
		char nextChar;
		int pos = entry.indexOf(Symbol) + Symbol.length();
		nextChar = entry.charAt(pos);
		return nextChar;
	}
	//metode, kas atrod n�kamo v�rdu aiz metodei padot� v�rda
	public static String wordAfter(String entryInf, String word)
	{
		String finalWord;
		String tempWord;
		int len = entryInf.length();
		//noteikts v�rda s�kums
		int index = entryInf.indexOf(word) + word.length() + 1;
		tempWord = entryInf.substring(index);
		//ja aiz atrast� v�rda ir v�l citi v�rdi		
		if(countSpaces(tempWord) != 0 && len > 1) 
		{
			finalWord = tempWord.substring(0, tempWord.indexOf(' ')).trim();
		}
		else 
		{
			finalWord = tempWord; 
		}
		return finalWord; //atgriezsts atrastais v�rds
	}
	//metode, kas atrod v�rdu skaitu s�irk��
	public static int wordCount(String entry)
	{
		int count = 1; //pie�am ka ir vismaz vien v�rds
		char character[]= new char[entry.length()];
		//cikls iet pa �kirkli pa vienam simbolam
	    for(int i=0; i<entry.length(); i++)
	    {
	    	character[i]=entry.charAt(i);
			//ja ir atstarpe un pirms t�s nav bijusi atstarpe tad pieskaita viens
		    if((i>0 && character[i-1]!=' ') && character[i]==' ')
		    {
		    	count++;
		    }
		}
	    return count; //atgrie� gala v�rt�bu
	}
	//metode, kas atrod atstarpju skaitu padotaj� simbolu virkn�
	public static int countSpaces(String s)
	{
		int count = 0;
		int len = s.length();
		//cikls iet pa �kirkli pa vienam simbolam
		for(int i=0; i<len; i++)
		{
			char ch=s.charAt(i);
		    if(ch==' ') // simbols ir atstarpe tad pieskaita viens
		    {
		    	count++;
		    }
		}
		return count;//atgrie� gala v�rt�bu
	}
	//metode, kas p�rbauda vai padotais v�rds eksit� ��ir�lu sarakst�
	public static boolean wordExist(String [] entries, String word)
	{
		boolean good = false;
		int len = entries.length;
		//cikls iet pa �kirk�u sarakstu
		for(int i=0; i<len; i++)
		{
			//ja s�irklis ir vismaz divi v�rdi
			if(entries[i].length() > 2 && StringUtils.countSpaces(entries[i]) > 0)
			{
				//tiek atarsts pirmais v�rds
				String entryName = entries[i].substring(0, entries[i].indexOf(" "));
				//ja ��irk�a v�rds atbilst padotajam v�rdam
				if(entryName.equals(word))
				{
					good = true;
					break;
				}
			}
		}
		return good; //atgrie� gala v�rt�bu
	}
	//metode, kas atgrie� pareizu avotu skaitu ��irkl�
	public static int referCount(String [] references, String entryRefer)
	{
		int count = 0;
		// izdal�tas visas ��irkl� ierakst�t�s atsauces
		String[] parts = entryRefer.split(", "); 
		int len = parts.length;
		int len2 = entryRefer.length();
		//cikls iet pa �kirk�a atsau�u sarakstam
		for(int i = 0; i<len; i++)
		{
			if(parts[i].length() <= 0)
			{
				return 1;
			}
			else
			{
				for(int j=0; j<len2; j++)
				{
					int referLen = references[j].length();
					// ja atsauce ir atrodama avotu sarakst�
					if(parts[i].equals(references[j]))
					{
						count++;
					}
					else
					{
						//ja astauce ir atrodama avotu sarakst�/**(�urn�liem un av�z�m u.c.)*//
						 if( parts[i].contains(references[j]) && parts[i].charAt(referLen) == '-')
						 {
							 count ++;
						 }
					}							
				}
			}
		}
				
		return count; //atgrie� gala v�rt�bu
	}
	//metode kas atrgrie� liel�ko IN v�rt�bu padotajam ��irk�a v�rdam
	public static int maxIN(String [][] inEntries, String word)
	{
		int max = 0;
		int len = inEntries.length;
		//cikls iet pa inEntries mas�vu
		for(int i=0; i< len; i++)
				//ja padotais v�rds atbilst un in v�rt�ba ir liel�ka par max
				// max v�r'ti'ba tiek palielin�ta
				if(inEntries[i][0] == word && Integer.parseInt(inEntries[i][1]) > max) 
					max = Integer.parseInt(inEntries[i][1]);
						
		return max; //atgrie� gala v�rt�bu
	}
}
