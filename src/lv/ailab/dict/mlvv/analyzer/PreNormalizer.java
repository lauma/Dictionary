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

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;

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
	 * FIXME: Specifiskie kāda taga labojumi tiek veikti arī tad, ja to tagu no šī masīva izņem.
	 */
	protected final static String[] tags = {
			"high", "gray", "sup", "sub", "b", "i", "extended"};

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
		// NB! tags <gray>, kas ir tieši pelēki krāsotiem tekstiem, ir jāatstāj!
		line = line.replaceAll("</?high>", "");

		// Izmet <gray>, kas ir viens otrā iekšā.
		Pattern doubleGrayPat = Pattern.compile("(.*?<gray>(?:(?!</gray>).)*?)<gray>(.*)", Pattern.DOTALL);
		Matcher doubleGrayMat = doubleGrayPat.matcher(line);
		while (doubleGrayMat.matches())
		{
			line = doubleGrayMat.group(1) + doubleGrayMat.group(2);
			doubleGrayMat = doubleGrayPat.matcher(line);
		}
		doubleGrayPat = Pattern.compile("(.*?)</gray>((?:(?!<gray>).)*?</gray>.*)", Pattern.DOTALL);
		doubleGrayMat = doubleGrayPat.matcher(line);
		while (doubleGrayMat.matches())
		{
			line = doubleGrayMat.group(1) + doubleGrayMat.group(2);
			doubleGrayMat = doubleGrayPat.matcher(line);
		}

		// Aizvāc liekās atstarpes, normalizē visas atstarpes par parasto.
		line = line.replaceAll("\\s+", " ");
		line = line.trim();

		line = Editors.removeTagSplits(line, tags);
		line = replaceSymbols(line);
		line = correctGeneric(line);
		line = correctSpecials(line);
		return line;
	}

	public static String replaceSymbols(String line)
	{
		if (line == null) return null;
		// Novāc BOM.
		if (line.length() > 0 && line.codePointAt(0) == 65279)
			line = line.substring(1);

		line = line.replace("\u2012", "\u2013");

		// Sasodīts aprisinājums kaut kādiem .doc gļukiem, kuru dēļ eksportējot
		// haotiski kropļojas kvadrātiekavas.
		line = line.replace("<openSquareBrack/>", "[");
		line = line.replace("<closeSquareBrack/>", "]");
		// Speciālie simboli.
		line = line.replace("<arrow/>", "\u2192");
		line = line.replace("<sup>0</sup>", "\u00B0");
		line = line.replace("<sup>0 </sup>", "\u00B0 ");
		line = line.replace("</i><sup><i>0</i></sup> <i>", "\u00B0 ");
		line = line.replace("<sup> 0</sup>", " \u00B0");

		// Aprisinājums, lai nesajūk ar atsaucēm uz homoformām.
		line = line.replace("[km<sup>2</sup>]", "[km\u00B2]");
		line = line.replace("[m<sup>2</sup>]", "[m\u00B2]");
		line = line.replace("[cm<sup>2</sup>]", "[cm\u00B2]");
		line = line.replace("[km<sup>3</sup>]", "[km\u00B3]");
		line = line.replace("[m<sup>3</sup>]", "[m\u00B3]");
		line = line.replace("[cm<sup>3</sup>]", "[cm\u00B3]");

		//line = line.replace("</i><sup><i>1</i></sup><i>", "\u00B9");
		line = line.replace("</i><sup><i>2</i></sup><i>", "\u00B2");
		line = line.replace("</i><sup><i>2</i></sup>", "\u00B2</i>");

		return line;
	}

	public static String correctGeneric(String line)
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
		line = line.replace(",</u>", "</u>,");
		line = line.replace(", </u>", "</u>, ");

		// Atboldē vientuļās domuzīmes.
		line = line.replace("<b>-</b>", "-");
		line = line.replace("<b><i>-</i></b>", "<i>-</i>");
		line = line.replaceAll("(?<=\\p{L}) -</b>(?=\\p{L})", "</b> -");
		line = line.replaceAll("(?<=\\p{L}) <i>-</i></b>(?=\\p{L})", "</b> <i>-</i>");
		line = line.replace("<b>\u2013</b>", "\u2013");
		line = line.replace("<b>\u2014</b>", "\u2014");
		line = line.replace("<b><i>\u2013</i></b>", "<i>\u2013</i>");
		line = line.replace("<b><i>\u2014</i></b>", "<i>\u2014</i>");

		line = line.replace("<b>)</b>", ")");
		line = line.replace("<b>(</b>", "(");
		line = line.replace("<b>.</b>", ".");
		line = line.replace("<b><i>.</i></b>", ".");

		// Izkļauj komatus no treknraksta beigām
		p = Pattern.compile("(\\p{L}\\p{M}*),(\\s*)</b>");
		m = p.matcher(line);
		while (m.find())
		{
			String target = m.group();
			String replacement = m.group(1) +"</b>," + m.group(2);
			line = line.replace(target, replacement);
			m = p.matcher(line);
		}

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
		line = Editors.removeTagSplits(line, tags);
		//line = line.replaceAll("<((\\p{L}\\p{M}*)+)></\\1>", "");
		//line = line.replaceAll("<((\\p{L}\\p{M}*)+)>\\s+</\\1>", " ");
		//line = line.replaceAll("</((\\p{L}\\p{M}*)+)><\\1>", "");
		//line = line.replaceAll("</((\\p{L}\\p{M}*)+)>\\s+<\\1>", " ");

		// Specifiskie tagu pārrāvumi.
		line = line.replaceAll("</i>; <i>", "; ");
		line = line.replaceAll("</i>, <i>", ", ");
		line = line.replaceAll("</i>\", <i>", "\", ");
		line = line.replaceAll("</i>\" <i>", "\" ");
		line = line.replaceAll("</i> \"<i>", " \"");

		line = line.replaceAll("(?<=\\p{L})<i>.\\s+(?=\\p{L})", ". <i>");

		// <i>Pārn</i>.: <i>
		line = line.replaceAll("(?<=\\p{L})</i>\\.: <i>", ".</i>: <i>");
		line = line.replaceAll("(?<=\\p{L})</i>\\.: \\(<i>", ".</i>: <i>(");
		line = line.replaceAll("(?<=\\p{L})\\.</i>: \\(<i>", ".</i>: <i>(");

		// Vienkārši kols garāka teksta vidū
		line = line.replaceAll("(?<=\\p{L},?\\s\\p{L}{1,15})</i>:\\s+<i>(?=\\p{L}+,?\\s\\p{L}+)", ": ");
		line = line.replaceAll("(?<=\\p{L},?\\s\\p{L}{1,15})</i>:<i>(?=\\s\\p{L}+,?\\s\\p{L}+)", ":");

		// Oriģinālais avots sistemātiski neliek iekavas kursīvā.
		//line = line.replaceAll("</i> \\(arī <i>", " \\(arī "); // Neviennozīmīgi lietots.
		line = line.replaceAll("</i>\\)\\s+\\(<i>", ") (");
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
		Pattern parMatcher = Pattern.compile("(.*<i>(?:(?!</i>).)*\\((?:(?!</i>).)*)</i>(\\)[.!?]*)(.*)");
		m = parMatcher.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + m.group(2) + "</i>" + m.group(3);
			m = parMatcher.matcher(line);
		}
		parMatcher = Pattern.compile("(.*<i>(?:(?!</i>).)*\\[(?:(?!</i>).)*)</i>(\\]\\.*)(.*)");
		m = parMatcher.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + m.group(2) + "</i>" + m.group(3);
			m = parMatcher.matcher(line);
		}
		// Ja vienkāršo operāciju dēļ aizverošās iekavas ir nokļuvušas kursīvā,
		// tad kursivē arī atverošās.
		parMatcher = Pattern.compile("(.*)\\(<i>((?:(?!</i>).)*\\)(?:(?!</i>).)*</i>.*)");
		m = parMatcher.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + "<i>(" + m.group(2);
			m = parMatcher.matcher(line);
		}
		parMatcher = Pattern.compile("(.*)\\[<i>((?:(?!</i>).)*\\](?:(?!</i>).)*</i>.*)");
		m = parMatcher.matcher(line);
		while (m.matches())
		{
			line = m.group(1) + "<i>[" + m.group(2);
			m = parMatcher.matcher(line);
		}

		// Punkti citāta sākumā
		line = line.replaceAll("\\.\\.\\.<i>(?=\\p{L})", "<i>...");
		line = line.replaceAll("\\.\\.<i>(?=\\p{L})", "<i>..");
		// Nejauši iekursivēta domuzīme (galotņu šablonos)
		line = line.replaceAll("\\s+-</i>(?=\\p{L})", "</i> -");
		// Nejauši iekursivēts punkts?
		line = line.replace(":</i>", "</i>:");

		// Aizvāc liekās atstarpes, normalizē visas atstarpes par parasto.
		line = line.replaceAll(" \\s+", " ");
		line = line.trim();

		line = Editors.removeTagSplits(line, tags);
		return line;
	}

	/**
	 * Specifisku kļūdu labošana.
	 */
	public static String correctSpecials(String line)
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

		line = Editors.removeTagSplits(line, tags);

		return line;
	}

}
