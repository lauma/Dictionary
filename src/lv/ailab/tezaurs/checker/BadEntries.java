package lv.ailab.tezaurs.checker;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

/**
 * Slikto šķirkļu reģistrs.
 * @author Lauma
 */
public class BadEntries
{
	protected Map<Integer, BadEntry> register =
			new HashMap<Integer, BadEntry>();
	
	public void addNewEntry(Dictionary.Entry entry, String error)
	{
		BadEntry e = register.get(entry.id);
		if (e == null) e = new BadEntry(entry.fullText, error);
		else e.errors.add(error);
		register.put(entry.id, e);
	}

	public void addNewEntryFromString(int entryID, String entry, String error)
	{
		BadEntry e = register.get(entryID);
		if (e == null) e = new BadEntry(entry, error);
		else e.errors.add(error);
		register.put(entryID, e);
	}
	
	public boolean isEmpty()
	{
		return register.isEmpty();
	}
	
	public void printAll(String fileName)
			throws IOException
	{
    	//izejas plūsma *.klu failam
    	BufferedWriter out = new BufferedWriter(
    			new OutputStreamWriter(new FileOutputStream(fileName), "windows-1257"));
    	for (Integer entryId : new TreeSet<Integer>(register.keySet()))
    	{
    		out.write(register.get(entryId) + "\n");
    	}
    	out.flush(); // plūsmas iztukšošana
    	out.close(); //faila aizvēršana
	}
	
	/**
	 * Informācija par vienu slikto šķirkli - šķirklis un tajā atrastās kļūdas
	 * @author Lauma
	 */
	public static class BadEntry
	{
		public String entry;
		public HashSet <String> errors;
		
		public BadEntry(String entryText, String error)
		{
			this.entry = entryText;
			this.errors = new HashSet<String> ();
			this.errors.add(error);
		}
		
		public BadEntry(String entryText)
		{
			this.entry = entryText;
			this.errors = new HashSet<String> ();
		}
		
		public String toString()
		{
			String res = "(" + String.join(". ", errors) + ".)\t" + entry;
			return res;
		}
	}
}
