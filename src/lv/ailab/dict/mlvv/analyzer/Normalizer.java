package lv.ailab.dict.mlvv.analyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * No Word eksportētā teksta normalizācija, lai pēc tam var izgūt struktūru,
 * nedomājot par liekiem tagiem.
 * Izveidots 2016-02-01, balstoties uz vecajā perl skriptā realizēto
 * funkcionalitāti.
 *
 * @author Lauma
 */
public class Normalizer
{
	public static String[] tags = {"gray", "sup", "sub", "b", "i", "extended"};
	/**
	 *
	 * @param line viena vārdnīcas faila rindiņa (vienlaicīgi arī šķirklis)
	 * @return normalizēta rinda
	 */
	public static String normalizeLine(String line)
	{
		// Aizvāc visus tagus, kas atbild par teksta iekrāsošanu (highlight),
		// jo pašlaik izskatās, ka tā ir pagaidu informācija, kas paredzēta
		// marķētājiem.
		line = line.replaceAll("</?gray>", "");
		// Aizvāc liekās atstarpes, normalizē visas atstarpes par parasto.
		line = line.replaceAll("\\s+", " ");
		line = line.trim();

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
		// Iebīda pēdējo burtu iekšā <extended> tagā.
		// Perl bija tā:
		// $line =~ s#(\p{L}\p{M}*)(</extended>)((\p{L}\p{M}*)+)#$1$3$2#g;
		// Kā var uzrakstīt smukāk Javā?
		Pattern p = Pattern.compile("(\\p{L}\\p{M}*)(</extended>)((\\p{L}\\p{M}*)+)");
		Matcher m = p.matcher(line);
		while (m.matches())
		{
			String target = m.group(1) + m.group(2) + m.group(3);
			String replacement = m.group(1) + m.group(3) + m.group(2);
			line = line.replace(target, replacement);
			m = p.matcher(line);
		}

		// Ieikļauj atverošās pēdiņas tekstā pēc <i> taga.
		p = Pattern.compile("(\\p{L}\\p{M}*)</i>\"");
		m = p.matcher(line);
		while (m.matches())
		{
			String target = m.group(1) + "</i>\"";
			String replacement = m.group(1) + "\"</i>";
			line = line.replace(target, replacement);
			m = p.matcher(line);
		}
		// Iekļauj aizverošās pēdiņas tekstā pirms </i> taga.
		p = Pattern.compile("\"<i>(\\p{L}\\p{M}*)");
		m = p.matcher(line);
		while (m.matches())
		{
			String target = "\"<i>" + m.group(1);
			String replacement = "<i>\"" + m.group(1);
			line = line.replace(target, replacement);
			m = p.matcher(line);
		}

		// Parasti arī teikuma/frāzes beigu pieturzīmei jābūt kursīvā, ja
		// pārējais teksts ir.
		line = line.replace("</i>.", ".</i>");
		line = line.replace("</i>;", ";</i>");

		// Novāc tagu pārrāvumus.
		line = line.replaceAll("<((\\p{L}\\p{M}*)+)></\\1>", "");
		line = line.replaceAll("<((\\p{L}\\p{M}*)+)>\\s+</\\1>", " ");
		line = line.replaceAll("</((\\p{L}\\p{M}*)+)><\\1>", "");
		line = line.replaceAll("</((\\p{L}\\p{M}*)+)>\\s+<\\1>", " ");

		// Aizvāc liekās atstarpes, normalizē visas atstarpes par parasto.
		line = line.replaceAll(" \\s+", " ");
		line = line.trim();
		return line;
	}
}
