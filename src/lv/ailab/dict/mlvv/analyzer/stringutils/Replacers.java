package lv.ailab.dict.mlvv.analyzer.stringutils;

/**
 * Sīkas ērtības metodes, kas veic noteikta vieda normalizāciju, kas reizēm, bet
 * ne pilnīgi vienmēr ir vajadzīga.
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class Replacers
{
	/**
	 * Izmet visus i tagus, nornormalizē dubultās atstarpes.
	 */
	public static String removeCursive(String text)
	{
		text = text.replace("<i>", "");
		text = text.replace("</i>", "");
		text = text.replaceAll("\\s\\s+", " ");
		return text;
	}
}
