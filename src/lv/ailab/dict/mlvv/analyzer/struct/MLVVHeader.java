package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pašlaik te nav būtiski paplašināta Header funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2016-02-03.
 * @author Lauma
 */
public class MLVVHeader extends Header
{
	/**
	 * Izgūst vienkārša veida hederi - no virknes <b>lemmma</b> [izruna] grmatika
	 */
	public static MLVVHeader parseSingularHeader(String linePart)
	{
		return parseSingularHeader(linePart, null);
	}
	/**
	 * Izgūst vienkārša veida hederi - no virknes <b>lemmma</b> [izruna] grmatika
	 */
	public static MLVVHeader parseSingularHeader(String linePart, String prefix)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;
		Matcher m = Pattern.compile("<([ub])>(.+?)(\\*?)</\\1>(!?)(\\*?)\\s*(.*)").matcher(linePart);
		if (m.matches())
		{
			MLVVHeader res = new MLVVHeader();
			res.lemma = new Lemma(m.group(2) + m.group(4));
			String star = m.group(3) + m.group(5);
			String gramStr = m.group(6);

			// Homonīma infekss, ja tāds ir, šeit tiek ignorēts.
			m = Pattern.compile("<sup>\\s*<b>(.*?)</b>\\s*</sup>\\s*(.*)").matcher(gramStr);
			if (m.matches()) gramStr = m.group(2);

			// Izruna, ja tāda ir.
			m = Pattern.compile("\\[(.*?)\\]\\s*(.*)").matcher(gramStr);
			if (m.matches())
			{

				res.lemma.pronunciation = m.group(1).split(",?\\s+<i>(arī|vai)</i>\\s*");
				gramStr = m.group(2);
			}

			// Nepazaudēt prefiksu.
			if (prefix != null && !prefix.isEmpty())
			{
				if (gramStr.isEmpty()) gramStr = prefix;
				else if (gramStr.matches(".*?[,;](</?i>)?")) gramStr = gramStr + " " + prefix;
				else gramStr = gramStr + "; " + prefix;

			}
			// Nepazaudēt zvaigznīti.
			if (!star.isEmpty() && gramStr.isEmpty()) gramStr = "*";
			else if (!star.isEmpty()) gramStr = "*, " + gramStr;

			// Apstrādāt atlikumu.
			if (!gramStr.isEmpty()) res.gram = new MLVVGram(gramStr);

			return res;
		} else
		{
			System.out.printf("No rindas daļas \"%s\" nevar izgūt hederi!\n", linePart);
			return null;
		}
	}

}
