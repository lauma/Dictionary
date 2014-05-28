package DictionaryTools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
	public static boolean IsEmptyEntry(String Entry, ArrayList<String> bad)
	{
		boolean NotEmpty = true;
		if(Entry.trim().isEmpty() || StringUtils.CountSpaces(Entry) <= 0)
    	{
    		bad.add("(Tukða rinda)" + Entry);
    	}
		else
		{
			NotEmpty = false;
		}
		return NotEmpty;
	}
	
	public static int FindNumber(String s)
	{
		String tmp = s;
		if (tmp.contains(" ")) tmp = tmp.substring(0, tmp.indexOf(" "));
		Matcher m = Pattern.compile("^\\d+").matcher(tmp);
		if (!m.find()) return 0;
		return Integer.parseInt(m.group(0));
	}
	
	public static Boolean EntryExist(String [][] InEntries, String Word)
	{
		boolean found = false;
		int len = InEntries.length;
		for(int i = 0; i<len; i++)
		{
			if(Word.equals(InEntries[i][0]))
			{
				found = true;
				break;
			}
		}
		return found;
	}
	
	public static boolean Exclusion(String [] Except, String Entry)
	{
		boolean found = false;
		int len = Except.length;
	    for(int i=0; i<len; i++)
	    {
	    	if(Except[i].equals(Entry))
	    	{
	    		found = true;
		        break;
		    }
	    }
		return found;
	}
	
	public static boolean IsBalticUpper(char Symbol)
	{
		String UPPER = "ÂÈÇÌÎÍÏÒÐÛÞÔª";
		boolean good = false;
		int len = UPPER.length();
		for(int i = 0; i < len; i++)
		{
			if(UPPER.charAt(i) == Symbol)
			{
				good = true;
				break;
			}
		}						
		return good;
	}
	
	public static char NextCh(String Entry, String Symbol)
	{
		char next_C;
		int pos = Entry.indexOf(Symbol) + Symbol.length();
		next_C = Entry.charAt(pos);
		return next_C;
	}
	
	public static String WordAfter(String EntryInf, String Word)
	{
		String vards;
		String temp_vards;
		int len = EntryInf.length();
		int index = EntryInf.indexOf(Word) + Word.length() + 1;
		temp_vards = EntryInf.substring(index);		
		if(CountSpaces(temp_vards) != 0 && len > 1)
		{
			vards = temp_vards.substring(0, temp_vards.indexOf(' ')).trim();
		}
		else 
		{
			vards = temp_vards; 
		}
		return vards;
	}
	
	
	public static int WordCount(String Entry)
	{
		int Count = 1;
		char character[]= new char[Entry.length()];
	    for(int i=0; i<Entry.length(); i++)
	    {
	    	character[i]=Entry.charAt(i);
		    if((i>0 && character[i-1]!=' ') && character[i]==' ')
		    {
		    	Count++;
		    }
		}
	    return Count;
	}
	
	public static int CountSpaces(String s)
	{
		int c = 0;
		int len = s.length();
		for(int i=0; i<len; i++)
		{
			char ch=s.charAt(i);
		    if(ch==' ')
		    {
		    	c++;
		    }
		}
		return c;
	}
	
	public static boolean WordExist(String [] Entries, String Word)
	{
		boolean good = false;
		int len = Entries.length;
		for(int i=0; i<len; i++)
		{
			if(Entries[i].length() > 2 && StringUtils.CountSpaces(Entries[i]) > 0)
			{
				String EntryName = Entries[i].substring(0, Entries[i].indexOf(" "));
				if(EntryName.equals(Word))
				{
					good = true;
					break;
				}
			}
		}
		return good;
	}
	
	public static int ReferCount(String [] EntryRefer, String References)
	{
		int sk = 0;
		String[] parts = References.split(", ");
		int len = parts.length;
		int len2 = EntryRefer.length;
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
					int ReferLen = EntryRefer[j].length();
					if(parts[i].equals(EntryRefer[j]))	sk++;
					else
					{
						 if( parts[i].contains(EntryRefer[j]) && parts[i].charAt(ReferLen) == '-')
						 {
							 sk ++;
						 }
					}							
				}
			}
		}
				
		return sk;
	}
}
