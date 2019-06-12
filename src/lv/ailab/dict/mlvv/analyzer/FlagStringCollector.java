package lv.ailab.dict.mlvv.analyzer;

import lv.ailab.dict.mlvv.struct.MLVVEntry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Izveidots 2017-05-24.
 *
 * @author Lauma
 */
public class FlagStringCollector
{
	public TreeMap<String, ArrayList<String>> flagStrings = new TreeMap<>();

	public void addFromEntry(MLVVEntry e)
	{
		TreeSet<String> flagtexts = e.getFlagStrings();
		String entryName = e.head.lemma.text + "<" + (e.homId == null ? 0 : e.homId) + ">";
		for (String ft : flagtexts)
		{
			ArrayList<String> entries = flagStrings.get(ft);
			if (entries == null) entries = new ArrayList<>();
			entries.add(entryName);
			flagStrings.put(ft, entries);
		}
	}

	public void printToFile(BufferedWriter out)
	throws IOException
	{
		for (String flag : flagStrings.keySet())
		{
			out.append(flag);
			out.append("\t");
			ArrayList<String> entries = flagStrings.get(flag);
			out.append(Integer.toString(entries.size()));
			out.append("\t");
			if (entries.size() <= 10)
				out.append(entries.stream()
						.reduce((a, b) -> a + ", " + b).orElse(""));
			else
			{
				out.append(entries.subList(0, 10).stream()
						.reduce((a, b) -> a + ", " + b).orElse(""));
				out.append(", ...");
			}
			out.newLine();
		}
	}
}
