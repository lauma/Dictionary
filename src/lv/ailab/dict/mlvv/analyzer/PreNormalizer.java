/*******************************************************************************
 * Copyright 2013-2016 Institute of Mathematics and Computer Science, University of Latvia
 * Author: Lauma Pretkalniņa
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
public class PreNormalizer
{
	/**
	 * Tagi, ar kuriem šis normalizators strādā.
	 */
	public String[] tags = {"high", "gray", "sup", "sub", "b", "i", "extended"};

	/**
	 *
	 * @param line viena vārdnīcas faila rindiņa (vienlaicīgi arī šķirklis)
	 * @return normalizēta rinda
	 */
	public String normalizeLine(String line)
	{
		// Aizvāc visus tagus, kas atbild par teksta iekrāsošanu (highlight),
		// jo pašlaik izskatās, ka tā ir pagaidu informācija, kas paredzēta
		// marķētājiem.
		// NB! tags <gray>, kas ir tieši pelēki krāsotiem tekstiem, ir jāatstāj!
		line = line.replaceAll("</?high>", "");
		// Aizvāc liekās atstarpes, normalizē visas atstarpes par parasto.
		line = line.replaceAll("\\s+", " ");
		line = line.trim();
		line = removeUnneededTags(line);
		line = replaceSymbols(line);
		line = correctGeneric(line);
		line = removeUnneededTags(line);
		line = correctSpecials(line);
		line = removeUnneededTags(line);
		return line;
	}

	public String replaceSymbols(String line)
	{
		if (line == null) return null;
		// Novāc BOM.
		if (line.length() > 0 && line.codePointAt(0) == 65279)
			line = line.substring(1);

		// Sasodīts aprisinājums kaut kādiem .doc gļukiem, kuru dēļ eksportējot
		// haotiski kropļojas kvadrātiekavas.
		line = line.replace("<openSquareBrack/>", "[");
		line = line.replace("<closeSquareBrack/>", "]");
		// Speciālie simboli.
		line = line.replace("<arrow/>", "\u2192");
		line = line.replace("<sup>0</sup>", "\u00B0");
		line = line.replace("<sup>0 </sup>", "\u00B0 ");
		line = line.replace("<sup> 0</sup>", " \u00B0");
		return line;
	}

	public String correctGeneric(String line)
	{
		if (line == null || line.isEmpty()) return line;

		// Iebīda pēdējo burtu iekšā <extended> tagā.
		// Perl bija tā:
		// $line =~ s#(\p{L}\p{M}*)(</extended>)((\p{L}\p{M}*)+)#$1$3$2#g;
		// Kā var uzrakstīt smukāk Javā?
		Pattern p = Pattern.compile("(\\p{L}\\p{M}*)(</extended>)((\\p{L}\\p{M}*)+)");
		Matcher m = p.matcher(line);
		while (m.find())
		{
			String target = m.group(1) + m.group(2) + m.group(3);
			String replacement = m.group(1) + m.group(3) + m.group(2);
			line = line.replace(target, replacement);
			m = p.matcher(line);
		}

		// Atboldē vientuļās domuzīmes.
		line = line.replace("<b>-</b>", "-");
		line = line.replace("<b><i>-</i></b>", "<i>-</i>");
		line = line.replaceAll("(?<=\\p{L}) -</b>(?=\\p{L})", "</b> -");
		line = line.replaceAll("(?<!\\p{L}) -</b>(?=\\p{L})", "</b> -");
		line = line.replace("<b>\u2013</b>", "\u2013");
		line = line.replace("<b>\u2014</b>", "\u2014");
		line = line.replace("<b><i>\u2013</i></b>", "<i>\u2013</i>");
		line = line.replace("<b><i>\u2014</i></b>", "<i>\u2014</i>");

		// Iekļauj aizverošās pēdiņas tekstā pēc <i> taga. Pirms vai pēc pēdiņām
		// iespējams komats.
		p = Pattern.compile("(\\p{L}\\p{M}*)</i>(,?\",?)");
		m = p.matcher(line);
		while (m.find())
		{
			String target = m.group(1) + "</i>" + m.group(2);
			String replacement = m.group(1) + m.group(2) + "</i>";
			line = line.replace(target, replacement);
			m = p.matcher(line);
		}
		// Iekļauj atverošās pēdiņas tekstā pirms </i> taga.
		p = Pattern.compile("\"<i>(\\p{L}\\p{M}*)");
		m = p.matcher(line);
		while (m.find())
		{
			String target = "\"<i>" + m.group(1);
			String replacement = "<i>\"" + m.group(1);
			line = line.replace(target, replacement);
			m = p.matcher(line);
		}

		// Pēdiņām jābūt kursīvā kopā ar vārdu/frāzi.
		line = line.replaceAll("\u201e<i>", "<i>\u201e");
		line = line.replaceAll("\"<i>", "<i>\"");
		line = line.replaceAll("</i>\u201d", "\u201d</i>");
		line = line.replaceAll("</i>\"", "\"</i>");

		// Ja kursīvā ir tikai punkts, tā parasti ir kļūda.
		line = line.replaceAll("<i>\\.\\s+</i>", ". ");
		// Parasti arī teikuma/frāzes beigu pieturzīmei jābūt kursīvā, ja
		// pārējais teksts ir.
		line = line.replace("</i>.", ".</i>");
		line = line.replace("</i>.", ".</i>");
		line = line.replace("</i>.", ".</i>");
		line = line.replace("</i>?", "?</i>");
		line = line.replace("</i>!", "!</i>");
		//line = line.replace("</i>;", ";</i>");


		// Novāc tagu pārrāvumus.
		line = line.replaceAll("<((\\p{L}\\p{M}*)+)></\\1>", "");
		line = line.replaceAll("<((\\p{L}\\p{M}*)+)>\\s+</\\1>", " ");
		line = line.replaceAll("</((\\p{L}\\p{M}*)+)><\\1>", "");
		line = line.replaceAll("</((\\p{L}\\p{M}*)+)>\\s+<\\1>", " ");

		// Specifiskie tagu pārrāvumi.
		line = line.replaceAll("</i>; <i>", "; ");
		line = line.replaceAll("</i>, <i>", ", ");
		line = line.replaceAll("</i>\", <i>", "\", ");
		line = line.replaceAll("</i>\" <i>", "\" ");
		line = line.replaceAll("</i> \"<i>", " \"");

		line = line.replaceAll("(?<=\\p{L})<i>.\\s+(?=\\p{L})", ". <i>");


		// Aizvāc liekās atstarpes, normalizē visas atstarpes par parasto.
		line = line.replaceAll(" \\s+", " ");
		line = line.trim();
		return line;
	}

	/**
	 * Specifisku kļūdu labošana.
	 */
	public String correctSpecials(String line)
	{
		if (line == null) return line;
		// Labojums problēmai, ka nozīmes numura punkts reizēm ir kursīvā.
		Pattern cursiveDot = Pattern.compile("(.*?)<b>(\\s*)(\\d+)\\s*<i>\\s*\\.(\\s*)</i>(\\s*)</b>(.*)");
		Matcher m = cursiveDot.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + m.group(2) + "<b>" + m.group(3) + ".</b>" + m.group(4) + m.group(5) + m.group(6);
			m = cursiveDot.matcher(line);
		}
		line = line.replaceAll(" \\s+", " ");
		line = line.replace("<i>.</i>", ".");

		// Labojums problēmai, ka pie nozīmes numura treknrakstā ir "pielipis"
		// iepriekšējais punkts.
		Pattern prevBoldDot = Pattern.compile("(.*?)<b>([.!?]\\s*)(\\d+)\\s*.(\\s*)</b>(.*)");
		m = prevBoldDot.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + m.group(2) + "<b>" + m.group(3) + ".</b>" + m.group(4) + m.group(5);
			m = prevBoldDot.matcher(line);
		}

		// Labojums poblēmai, ka nozīmes numura punkts ir palicis ārā no
		// treknraksta.
		Pattern missDot = Pattern.compile("(.*?)(<b>\\d+)</b>\\.(\\s.*)");
		m = missDot.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + m.group(2) + ".</b>" + m.group(3);
			m = missDot.matcher(line);
		}

		// <i>Pārn</i>.: <i>
		line = line.replaceAll("(?<=\\p{L})</i>\\.: <i>", ".</i>: <i>");
		line = line.replaceAll("(?<=\\p{L})</i>\\.: \\(<i>", ".</i>: <i>(");
		line = line.replaceAll("(?<=\\p{L})\\.</i>: \\(<i>", ".</i>: <i>(");

		// Vienkārši kols garāka teksta vidū
		line = line.replaceAll("(?<=\\p{L},?\\s\\p{L}{1,15})</i>:\\s+<i>(?=\\p{L}+,?\\s\\p{L}+)", ": ");
		line = line.replaceAll("(?<=\\p{L},?\\s\\p{L}{1,15})</i>:<i>(?=\\s\\p{L}+,?\\s\\p{L}+)", ":");

		// Oriģinālais avots sistemātiski neliek iekavas kursīvā.
		//line = line.replaceAll("</i> \\(arī <i>", " \\(arī "); // Neviennozīmīgi lietots.
		line = line.replaceAll("</i>\\)\\s+<i>", ") ");
		line = line.replaceAll("</i>\\)<i>", ")");
		line = line.replaceAll("</i>\\)\\.\\s+<i>", "). ");
		line = line.replaceAll("</i>\\)\\?\\s+<i>", ")? ");
		line = line.replaceAll("</i>\\)!\\s+<i>", ")! ");
		line = line.replaceAll("</i>\\s+\\(<i>", " (");
		line = line.replaceAll("</i>\\(<i>", "(");
		line = line.replaceAll("</i>\\]\\s+<i>", "] ");
		line = line.replaceAll("</i>\\s+\\[<i>", " [");


		// Ja vienkāršo operāciju dēļ atverošās iekavas ir nokļuvušas kursīvā,
		// tad kursivē arī aizverošās.
		// NB! šis nestrādā, ja starp iekavām ir kāds vārds nekursīvā.
		Pattern secondPar = Pattern.compile("(.*<i>(?:(?!</i>).)*\\((?:(?!</i>).)*)</i>(\\)[.!?]*)(.*)");
		m = secondPar.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + m.group(2) + "</i>" + m.group(3);
			m = secondPar.matcher(line);
		}
		secondPar = Pattern.compile("(.*<i>(?:(?!</i>).)*\\[(?:(?!</i>).)*)</i>(\\]\\.*)(.*)");
		m = secondPar.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + m.group(2) + "</i>" + m.group(3);
			m = secondPar.matcher(line);
		}

		// Punkti citāta sākumā
		line = line.replaceAll("\\.\\.\\.<i>(?=\\p{L})", "<i>...");
		line = line.replaceAll("\\.\\.<i>(?=\\p{L})", "<i>..");
		// Nejauši iekursivēta domuzīme (galotņu šablonos)
		line = line.replaceAll("\\s+-</i>(?=\\p{L})", "</i> -");

		return line;
	}

	public String removeUnneededTags(String line)
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
