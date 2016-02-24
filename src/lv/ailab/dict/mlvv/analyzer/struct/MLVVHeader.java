package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Izveidots 2016-02-03.
 *
 * @author Lauma
 */
public class MLVVHeader extends Header
{
	/**
	 * Izgūst vienkārša veida hederi - no virknes <b>lemmma</b> [izruna] grmatika
	 */
	public static MLVVHeader extractSingularHeader(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;
		Matcher m = Pattern.compile("<b>(.+?)</b>\\s*(.*)").matcher(linePart);
		if (m.matches())
		{
			MLVVHeader res = new MLVVHeader();
			res.lemma = new Lemma(m.group(1));
			String gramStr = m.group(2);

			// Homonīma infekss, ja tāds ir, šeit tiek ignorēts.
			m = Pattern.compile("<sup>\\s*<b>(.*?)</b>\\s*</sup>\\s*(.*)").matcher(gramStr);
			if (m.matches()) gramStr = m.group(2);

			// Izruna, ja tāda ir.
			m = Pattern.compile("\\[(.*?)\\]\\s*(.*)").matcher(gramStr);
			if (m.matches())
			{
				res.lemma.pronunciation = new String[]{m.group(1)};
				// TODO sadalīt izrunas sīkāk
				gramStr = m.group(2);
			}
			if (!gramStr.isEmpty())
				res.gram = new MLVVGram(gramStr);


			return res;
		} else
		{
			System.out.printf("No rindas daļas \"%s\" nevar izgūt hederi!\n", linePart);
			return null;
		}
	}

}