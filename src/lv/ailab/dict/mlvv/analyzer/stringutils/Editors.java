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

	public static String cursiveToUnderscore(String line)
	{
		// Kursīvs šajā stadijā parasti tiek lietots atsevišķiem vārdiem.
		line = line.replace("<i> ", " _");
		line = line.replace("<i>", "_");
		line = line.replace("...</i>", "_...");
		line = line.replace("..</i>", "_..");
		line = line.replace(".</i>", "_.");
		line = line.replace(" </i>", "_ ");
		line = line.replace("</i>", "_");
		return line;
	}

	public static String replaceHomIds(String line, boolean cursiveAware)
	{

		line = line.replaceAll("(?<=\\p{L}-?)\\s*<sup>\\s*1</sup>", " [1]");
		line = line.replaceAll("(?<=\\p{L}-?)\\s*<sup>\\s*2</sup>", " [2]");
		line = line.replaceAll("(?<=\\p{L}-?)\\s*<sup>\\s*3</sup>", " [3]");
		if (cursiveAware)
		{
			line = line.replaceAll("</i>\\s*<sup>\\s*1</sup>", " [1]</i>");
			line = line.replaceAll("</i>\\s*<sup>\\s*2</sup>", " [2]</i>");
			line = line.replaceAll("</i>\\s*<sup>\\s*3</sup>", " [3]</i>");

			//line = line.replaceAll("(?<=\\p{L})</i>\\s*<sup>\\s*<i>1</i></sup><i>", " [1]");
			//line = line.replaceAll("(?<=\\p{L})</i>\\s*<sup>\\s*<i>2</i></sup><i>", " [2]");
			line = line.replaceAll("(?<=\\p{L}-?)</i>\\s*<sup>\\s*<i>3</i></sup><i>", " [3]");

			//line = line.replaceAll("(?<=\\p{L})</i>\\s*<sup>\\s*<i>1</i></sup>", " [1]</i>");
			//line = line.replaceAll("(?<=\\p{L})</i>\\s*<sup>\\s*<i>2</i></sup>", " [2]</i>");
			line = line.replaceAll("(?<=\\p{L}-?)</i>\\s*<sup>\\s*<i>3</i></sup>", " [3]</i>");
		}
		return line;
	}

}
