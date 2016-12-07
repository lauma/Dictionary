package lv.ailab.dict.mlvv.analyzer.stringutils;

/**
 * Sīkas ērtības metodes, kas veic noteikta vieda normalizāciju, kas reizēm, bet
 * ne pilnīgi vienmēr ir vajadzīga.
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class Editors
{
	/**
	 * Izmet visus i tagus, nornormalizē dubultās atstarpes.
	 */
	public static String removeCursive(String text)
	{
		if (text == null) return text;
		text = text.replace("<i>", "");
		text = text.replace("</i>", "");
		text = text.replaceAll("\\s\\s+", " ");
		return text;
	}

	/**
	 * Ja tekstā ir nesapārots atverošais i tags, beigās pieliek aizverošo.
	 */
	public static String closeCursive(String text)
	{
		if (text == null || !text.contains("<i>")) return text;
		if (text.contains("<i>") && ! text.contains("</i>")) return text + "</i>";
		if (text.lastIndexOf("<i>") > text.lastIndexOf("</i>")) return text + "</i>";
		return text;
	}

	/**
	 * Ja tekstā ir nesapārots aizverošais i tags, sākumā pieliek atverošo.
	 */
	public static String openCursive(String text)
	{
		if (text == null || !text.contains("</i>")) return text;
		if (text.contains("</i>") && ! text.contains("<i>")) return "<i>" + text;
		if (text.indexOf("</i>") < text.indexOf("<i>")) return "<i>" + text;
		return text;
	}

	/**
	 * Vienkāršā veidā aizvāc doto tagu pārrāvumus.
	 */
	public static String removeTagSplits(String line, String[] tags)
	{
		String prevLine = null;
		do // Ārējais cikls paredzēts, lai tiktu galā ar tukšu tagu virtenēm.
		{
			prevLine = line;
			for (String tag : tags)
			{
				// Iznes ārā no anotētajiem elementiem to galos esošās atstarpes.
				line = line.replace(" </" + tag + ">", "</" + tag + "> ");
				line = line.replace("<" + tag + "> ", " <" + tag + ">");

				// Aizvāc nevajadzīgus tagu pārrāvumus un tukšus tagus.
				String history = line;
				do
				{
					history = line;
					line = line.replaceAll("<" + tag + "></" + tag + ">", "");
					line = line.replaceAll("<" + tag + ">\\s+</" + tag + ">", " ");
					line = line.replaceAll("</" + tag + "><" + tag + ">", "");
					line = line.replaceAll("</" + tag + ">\\s+<" + tag + ">", " ");
				} while (!history.equals(line));
			}
		}
		while (!line.equals(prevLine));
		return line;
	}
}
