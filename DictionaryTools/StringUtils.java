/*****************
Autors: Gunârs Danovskis
Pçdçjais laboðanas datums: 28.05.2014

Klases mçríis:
	Klase StringUtils ietver sevî visas palîgmetodes ðíiríïu apstrâdei
*****************/

package DictionaryTools; //Kopîga pakotnetne, kurâ ir iekïautas visas klases veiksmîgai programmas darbîbai

//Importçtâs Java bibliotçkas
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
	// metode, kas pârbauda vai padotais ðíirklis nav tukðs
	public static boolean isEntryEmpty(String entry, ArrayList<String> bad)
	{
		boolean notEmpty = true;
		//ja ðkirklis atmetot atstarpes ir tukðs 
		if(entry.trim().isEmpty())
    	{
			//ðkirklis tiek ierakstîts slikto sarakstâ
    		bad.add("(Tukða rinda)" + entry);
    	}
		else
		{
			notEmpty = false; // ja nav tukðs tad noimaina uz false
		}
		return notEmpty;
	}
	// metode, kas pârbauda kâds skaitlis ir atrodams aiz maríiera kuram to lieto
	public static int findNumber(String s) 
	{
		String tmp = s;
		if (tmp.contains(" ")) tmp = tmp.substring(0, tmp.indexOf(" "));
		//regulârâ izteksme skaitïa meklçðanai
		Matcher m = Pattern.compile("^\\d+").matcher(tmp); 
		if (!m.find()) return 0; // ja nav atrodams tad return 0
		//tiek atgiezts pirmais objekts no grupas un pârvçrsts par integer
		return Integer.parseInt(m.group(0)); 
	}
	// metode, kas pârbauda vai padotais ðíirklis eksistç
	public static Boolean entryExist(String [][] inEntries, String word)
	{	
		boolean found = false;
		int len = inEntries.length;
		//cikls iet pa inEntries masîvu un salîdzina vai vârds ir atrodams
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
	// metode, kas pârbauda vai padotais ðkirklis ir exclusion sarakstâ
	public static boolean exclusion(String [] except, String entry) 
	{
		boolean found = false;
		int len = except.length;
		//cikls iet pa except masîvu un salîdzina vai vârds ir atrodams
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
	//metode, ka pârbauda vai padotais simbols ir lielais latvieðu burts
	public static boolean isBalticUpper(char Symbol)
	{
		String UPPER = "ÂÈÇÌÎÍÏÒÐÛÞÔª"; //iespçjamie leilie burti
		boolean good = false;
		int len = UPPER.length();
		//cikls iet pa simbolu virkni un salîdzina vai simbols ir atrodams
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
	//metode, kas atrod nâkamo simbolu aiz padotâs simbolu virknes
	public static char nextCh(String entry, String Symbol)
	{
		char nextChar;
		int pos = entry.indexOf(Symbol) + Symbol.length();
		nextChar = entry.charAt(pos);
		return nextChar;
	}
	//metode, kas atrod nâkamo vârdu aiz metodei padotâ vârda
	public static String wordAfter(String entryInf, String word)
	{
		String finalWord;
		String tempWord;
		int len = entryInf.length();
		//noteikts vârda sâkums
		int index = entryInf.indexOf(word) + word.length() + 1;
		tempWord = entryInf.substring(index);
		//ja aiz atrastâ vârda ir vçl citi vârdi		
		if(countSpaces(tempWord) != 0 && len > 1) 
		{
			finalWord = tempWord.substring(0, tempWord.indexOf(' ')).trim();
		}
		else 
		{
			finalWord = tempWord; 
		}
		return finalWord; //atgriezsts atrastais vârds
	}
	//metode, kas atrod vârdu skaitu síirkïî
	public static int wordCount(String entry)
	{
		int count = 1; //pieòam ka ir vismaz vien vârds
		char character[]= new char[entry.length()];
		//cikls iet pa ðkirkli pa vienam simbolam
	    for(int i=0; i<entry.length(); i++)
	    {
	    	character[i]=entry.charAt(i);
			//ja ir atstarpe un pirms tâs nav bijusi atstarpe tad pieskaita viens
		    if((i>0 && character[i-1]!=' ') && character[i]==' ')
		    {
		    	count++;
		    }
		}
	    return count; //atgrieþ gala vçrtîbu
	}
	//metode, kas atrod atstarpju skaitu padotajâ simbolu virknç
	public static int countSpaces(String s)
	{
		int count = 0;
		int len = s.length();
		//cikls iet pa ðkirkli pa vienam simbolam
		for(int i=0; i<len; i++)
		{
			char ch=s.charAt(i);
		    if(ch==' ') // simbols ir atstarpe tad pieskaita viens
		    {
		    	count++;
		    }
		}
		return count;//atgrieþ gala vçrtîbu
	}
	//metode, kas pârbauda vai padotais vârds eksitç ðíirílu sarakstâ
	public static boolean wordExist(String [] entries, String word)
	{
		boolean good = false;
		int len = entries.length;
		//cikls iet pa ðkirkïu sarakstu
		for(int i=0; i<len; i++)
		{
			//ja síirklis ir vismaz divi vârdi
			if(entries[i].length() > 2 && StringUtils.countSpaces(entries[i]) > 0)
			{
				//tiek atarsts pirmais vârds
				String entryName = entries[i].substring(0, entries[i].indexOf(" "));
				//ja ðíirkïa vârds atbilst padotajam vârdam
				if(entryName.equals(word))
				{
					good = true;
					break;
				}
			}
		}
		return good; //atgrieþ gala vçrtîbu
	}
	//metode, kas atgrieþ pareizu avotu skaitu ðíirklî
	public static int referCount(String [] references, String entryRefer)
	{
		int count = 0;
		// izdalîtas visas ðíirklî ierakstîtâs atsauces
		String[] parts = entryRefer.split(", "); 
		int len = parts.length;
		int len2 = entryRefer.length();
		//cikls iet pa ðkirkïa atsauèu sarakstam
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
					// ja atsauce ir atrodama avotu sarakstâ
					if(parts[i].equals(references[j]))
					{
						count++;
					}
					else
					{
						//ja astauce ir atrodama avotu sarakstâ/**(þurnâliem un avîzçm u.c.)*//
						 if( parts[i].contains(references[j]) && parts[i].charAt(referLen) == '-')
						 {
							 count ++;
						 }
					}							
				}
			}
		}
				
		return count; //atgrieþ gala vçrtîbu
	}
	//metode kas atrgrieþ lielâko IN vçrtîbu padotajam ðíirkïa vârdam
	public static int maxIN(String [][] inEntries, String word)
	{
		int max = 0;
		int len = inEntries.length;
		//cikls iet pa inEntries masîvu
		for(int i=0; i< len; i++)
				//ja padotais vârds atbilst un in vçrtîba ir lielâka par max
				// max vçr'ti'ba tiek palielinâta
				if(inEntries[i][0] == word && Integer.parseInt(inEntries[i][1]) > max) 
					max = Integer.parseInt(inEntries[i][1]);
						
		return max; //atgrieþ gala vçrtîbu
	}
}
