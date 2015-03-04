/*****************
Autors: Gunārs Danovskis

Klases mērķis:
	Klase StringUtils ietver sevī visas palīgmetodes šķirķļu apstrādei
*****************/

package DictionaryTools; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

//Importētās Java bibliotēkas
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
	// metode, kas pārbauda vai padotais šķirklis nav tukšs
	public static boolean isEntryEmpty(String entry, int entryID, BadEntries bad)
	{
		boolean notEmpty = true;
		//ja škirklis atmetot atstarpes ir tukšs 
		if(entry.trim().isEmpty())
    	{
			//škirklis tiek ierakstīts slikto sarakstā
			bad.addNewEntry(entryID, entry, "Tukša rinda");
    	}
		else
		{
			notEmpty = false; // ja nav tukšs tad noimaina uz false
		}
		return notEmpty;
	}
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
	// metode, kas pārbauda vai padotais šķirklis eksistē
	public static Boolean entryExist(String [][] inEntries, String word)
	{	
		boolean found = false;
		int len = inEntries.length;
		//cikls iet pa inEntries masīvu un salīdzina vai vārds ir atrodams
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
	// metode, kas pārbauda vai padotais škirklis ir exclusion sarakstā
	public static boolean exclusion(String [] except, String entry) 
	{
		boolean found = false;
		int len = except.length;
		//cikls iet pa except masīvu un salīdzina vai vārds ir atrodams
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
	//metode, ka pārbauda vai padotais simbols ir lielais latviešu burts
	public static boolean isBalticUpper(char Symbol)
	{
		String UPPER = "ĀČĒĢĪĶĻŅŠŪŽŌŖ"; //iespējamie leilie burti
		boolean good = false;
		int len = UPPER.length();
		//cikls iet pa simbolu virkni un salīdzina vai simbols ir atrodams
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
	//metode, kas atrod nākamo simbolu aiz padotās simbolu virknes
	public static char nextCh(String entry, String Symbol)
	{
		char nextChar;
		int pos = entry.indexOf(Symbol) + Symbol.length();
		nextChar = entry.charAt(pos);
		return nextChar;
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
		if(countSpaces(tempWord) != 0 && len > 1) 
		{
			finalWord = tempWord.substring(0, tempWord.indexOf(' ')).trim();
		}
		else 
		{
			finalWord = tempWord; 
		}
		return finalWord; //atgriezsts atrastais vārds
	}
	//metode, kas atrod vārdu skaitu sķirkļī
	public static int wordCount(String entry)
	{
		int count = 1; //pieņam ka ir vismaz vien vārds
		char character[]= new char[entry.length()];
		//cikls iet pa škirkli pa vienam simbolam
	    for(int i=0; i<entry.length(); i++)
	    {
	    	character[i]=entry.charAt(i);
			//ja ir atstarpe un pirms tās nav bijusi atstarpe tad pieskaita viens
		    if((i>0 && character[i-1]!=' ') && character[i]==' ')
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
		for(int i=0; i<len; i++)
		{
			char ch=s.charAt(i);
		    if(ch==' ') // simbols ir atstarpe tad pieskaita viens
		    {
		    	count++;
		    }
		}
		return count;//atgriež gala vērtību
	}
	//metode, kas pārbauda vai padotais vārds eksitē šķirķlu sarakstā
	public static boolean wordExist(String [] entries, String word)
	{
		boolean good = false;
		int len = entries.length;
		//cikls iet pa škirkļu sarakstu
		for(int i=0; i<len; i++)
		{
			//ja sķirklis ir vismaz divi vārdi
			if(entries[i].length() > 2 && StringUtils.countSpaces(entries[i]) > 0)
			{
				//tiek atarsts pirmais vārds
				String entryName = entries[i].substring(0, entries[i].indexOf(" "));
				//ja šķirkļa vārds atbilst padotajam vārdam
				if(entryName.equals(word))
				{
					good = true;
					break;
				}
			}
		}
		return good; //atgriež gala vērtību
	}

	//metode kas atrgriež lielāko IN vērtību padotajam šķirkļa vārdam
	public static int maxIN(String [][] inEntries, String word)
	{
		int max = 0;
		int len = inEntries.length;
		//cikls iet pa inEntries masīvu
		for(int i=0; i< len; i++)
				//ja padotais vārds atbilst un in vērtība ir lielāka par max
				// max vēr'ti'ba tiek palielināta
				if(inEntries[i][0] == word && Integer.parseInt(inEntries[i][1]) > max) 
					max = Integer.parseInt(inEntries[i][1]);
						
		return max; //atgriež gala vērtību
	}
}
