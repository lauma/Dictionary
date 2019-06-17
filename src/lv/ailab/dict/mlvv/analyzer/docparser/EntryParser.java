package lv.ailab.dict.mlvv.analyzer.docparser;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.mlvv.struct.*;
import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.struct.Phrase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryParser
{
	protected EntryParser(){};
	protected static EntryParser singleton = new EntryParser();
	public static EntryParser me()
	{
		return singleton;
	}

	/**
	 * Izanalizē rindu, un atgriež null, ja tajā nekā nav, vai MLVVEntry, ja no
	 * šīs rindas tādu var izgūt.
	 */
	public MLVVEntry parse(String line)
	{
		if (line == null || line.isEmpty()) return null;

		// Ja rinda satur tikai burtu, neizgūst neko.
		if (line.matches("<b>\\p{L}\\p{M}*</b>")) return null;

		MLVVEntry result = MLVVElementFactory.me().getNewEntry();

		// Atdala šķirkļa galvu.
		String head, body;
		Matcher multiSenseWithGram = Pattern.compile("(<b>.*?</b>.*?)(<b>1\\.</b>.*)")
				.matcher(line);
		Matcher multiSenseNoGram = Pattern.compile("(<b>(?!</?[ib]>).*)\\s(1\\.</b>.*?<b>2\\.</b>.*)")
				.matcher(line);
		// Šķirklis ar numurētām nozīmēm un gramatiku pirms pirmās nozīmes
		if (multiSenseWithGram.matches())
		{
			head = multiSenseWithGram.group(1);
			body = multiSenseWithGram.group(2);
		}
		// Šķirklis ir ar numurētām nozīmēm, bet bez gramatikas pirms pirmās nozīmes.
		else if (multiSenseNoGram.matches())
		{
			head = multiSenseNoGram.group(1) + "</b>";
			body = "<b>" + multiSenseNoGram.group(2);
		}
		// Šķirklis ar vienu nozīmi.
		else
		{
			Matcher m2 = Pattern.compile("(<b>.*?</b>\\*?\\s*)(.*)").matcher(line); // pirmais šķirkļavārds + zvaigznīte jaunvārdiem
			if (m2.matches())
			{
				head = m2.group(1);
				body = m2.group(2);
				Pattern headpart = Pattern.compile(
						"((?:" +
								"\\s*(?:\\p{Ll}\\p{M}*)+(?:\\s\\[[^\\]]+\\])?[.,]|" +	// forma ar neobligātu izrunu
								"(?:[,;]\\s*)?(?:(?:arī|retāk)\\s*)?<b>.*?</b>[,.;]?|" + 			// cita lemma
								"\\((?:(?!<b>|\\)).)*\\s*<b>.*?</b>(?:,?\\s+-(?:\\p{Ll}\\p{M}*)+,?)*\\s*\\)[,.;]?|" + 			// cita lemma iekavās
								":?\\s*<i>.*?</i>[,.;]?|<sup>.*?</sup>[,.;]?|" + 		// gram. kursīvā | homonīma indekss
								"\\[.*?\\][,.;]?|[-,][^.<]*\\.?|" + 			// [izruna] | "galotne"
								"(?:[:,]\\s)?<u>.*?</u>\\.?" +  			//formas ierobežojums
						")\\s*)(.*)");	// atstarpe pārējais
				m2 = headpart.matcher(body);
				while (m2.matches())
				{
					head = head + m2.group(1);
					body = m2.group(2);
					m2 = headpart.matcher(body);
				}
			} else
			{
				System.out.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
				return null;
				//throw new IllegalArgumentException("Can't match input line with regexp!");
			}
		}
		head = head.trim();
		body = body.trim();
		// Izparsē referenci.
		if (body.isEmpty() && head.matches(".*?<i>[Ss]k(at)?(</i>.|.</i>)\\s+[-\\p{L}]+\\."))
		{
			String referencesText = head.substring(head.lastIndexOf("</i>") + 4).trim();
			if (referencesText.endsWith("."))
				referencesText = referencesText.substring(0, referencesText.length()-1);
			head = head.substring(0, head.lastIndexOf("<i>"));

			if (!referencesText.isEmpty())
			{
				result.references = new LinkedList<>();
				result.references.addAll(Arrays.asList(referencesText.split(", ")));
				// TODO: vai šeit vajag dalījumu?
			}
		}
		if (head.isEmpty())
		{
			System.out.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
			return null;
		}
		else parseHeadBlock(result, head);
		if (body.isEmpty() && result.references == null )
			System.out.println("Neizdodas izgūt šķirkļa ķermeni no šīs rindas:\n\t" + line);
		else if (!body.isEmpty()) parseBodyBlock(result, body);
		return result;
	}

	/**
	 * TODO karodziņi
	 * TODO izrunas
	 */
	protected void parseHeadBlock(MLVVEntry entry, String linePart)
	{
		entry.head = MLVVElementFactory.me().getNewHeader();
		Matcher m = Pattern.compile("<b>(.+?)(\\*?)</b>(\\*?)\\s*(.*)").matcher(linePart);
		if (m.matches())
		{
			entry.head.lemma = MLVVElementFactory.me().getNewLemma();
			entry.head.lemma.text = m.group(1).trim();
			String star = m.group(2) + m.group(3);
			String gram = m.group(4);
			// Homonīma infekss, ja tāds ir.
			m = Pattern.compile("^<sup>\\s*(?:<b>)?(.*?)(?:</b>)?\\s*</sup>\\s*(.*)")
					.matcher(gram);
			if (m.matches())
			{
				if (entry.homId != null) System.out.printf(
						"No šķirkļavārda \"%s\" homonīma indeksu mēģina piešķiert vairākkārt: vispirms %s, tad %s!",
						entry.head.lemma, entry.homId, m.group(1));
				entry.homId = m.group(1);
				// Izmet ārā homonīma indeksu.
				linePart = "<b>" + entry.head.lemma.text + "</b> " + m.group(2);
			} else linePart = "<b>" + entry.head.lemma.text + "</b>" + gram;
			entry.head.gram = GramParser.me().parse(linePart);
			if (star!= null && !star.isEmpty())((MLVVGram)entry.head.gram).addStar();
		} else
		{
			System.out.printf(
					"Neizdodas izgūt pirmo šķirkļavārdu no šī rindas fragmenta:\n%s\n",
					linePart);
			entry.head = null;
		}
	}

	protected void parseBodyBlock(MLVVEntry entry, String linePart)
	{
		linePart = linePart.trim();
		// Normatīvais komentārs
		Matcher m = Pattern.compile("(.*)<gray>(.*)</gray>(\\.?)(.*)").matcher(linePart);
		if (m.matches())
		{
			parseNormative(entry, (m.group(2) + m.group(3)).trim());
			linePart = m.group(1).trim();
			if (m.group(4).trim().length() > 0)
				linePart = linePart + " " + m.group(4).trim();
		}

		// Cilme
		m = Pattern.compile("(.*?)\\b[cC]ilme: (.*)").matcher(linePart);
		if (m.matches())
		{
			String newEtymology = m.group(2).trim();
			linePart = m.group(1).trim();
			if (newEtymology.contains("<square/>"))
			{
				linePart = linePart + " " + newEtymology.substring(newEtymology.indexOf("<square/>")).trim();
				newEtymology = newEtymology.substring(0, newEtymology.indexOf("<square/>")).trim();
			}
			parseEtymology(entry, newEtymology);
		}

		// Atvasinājumi
		m = Pattern.compile("(.*?)<square/>(.*)").matcher(linePart);
		if (m.matches())
		{
			parseDerivs(entry, m.group(2));
			linePart = m.group(1).trim();
		}

		// Frazeoloģismi
		m = Pattern.compile("(.*?)<diamond/>(.*)").matcher(linePart);
		if (m.matches())
		{
			parsePhraseology(entry, m.group(2).trim());
			linePart = m.group(1).trim();
		}

		// Stabilie vārdu savienojumi
		m = Pattern.compile("(.*?)<triangle/>(.*)").matcher(linePart);
		if (m.matches())
		{
			parseStables(entry, m.group(2).trim());
			linePart = m.group(1).trim();
		}

		if (linePart.length() > 0)
			parseSenses(entry, linePart);

	}

	/**
	 * TODO vai viena gramatika var attiekties uz vairākām lemmām?
	 */
	protected void parseDerivs(MLVVEntry entry, String linePart)
	{
		String[] derivTexts = linePart.split("<square/>|\\s*(?=<b>)");
		if (derivTexts.length < 1) return;
		entry.derivs = new LinkedList<>();
		for (String dt :derivTexts)
		{
			MLVVHeader h = HeaderParser.me().parseSingularHeader(dt.trim());
			if (h != null) entry.derivs.add(h);
		}
	}

	protected void parsePhraseology(MLVVEntry entry, String linePart)
	{
		String[] phrasesParts = linePart.split("<diamond/>");
		//if (phrasesParts.length > 0 && phraseology == null)  phraseology = new LinkedList<>();
		if (phrasesParts.length > 0 && entry.phrases == null)  entry.phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			MLVVPhrase p = PhraseParser.me().parseSpecialPhrasal(
					phraseText.trim(), Phrase.Type.PHRASEOLOGICAL, entry.head.lemma.text);
			if (p != null) entry.phrases.add(p);
		}
	}

	protected void parseStables(MLVVEntry entry, String linePart)
	{
		linePart = linePart.trim();
		String[] phrasesParts = linePart.split("<triangle/>");
		if (phrasesParts.length > 0 && entry.phrases == null)
			entry.phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			MLVVPhrase p = PhraseParser.me().parseSpecialPhrasal(
					phraseText.trim(), Phrase.Type.STABLE_UNIT, entry.head.lemma.text);
			if (p != null) entry.phrases.add(p);
		}
	}
	protected void parseSenses(MLVVEntry entry, String linePart)
	{
		linePart = linePart.trim();
		if (linePart.length() < 1) return;
		String[] sensesParts = new String[]{linePart};
		boolean numberSenses = false;
		if (linePart.startsWith("<b>1.</b>"))
		{
			linePart = linePart.substring("<b>1.</b>".length());
			sensesParts = linePart.split("<b>\\d+\\.</b>");
			numberSenses = true;
		}
		entry.senses = new LinkedList<>();
		for (int senseNumber = 0; senseNumber < sensesParts.length; senseNumber++)
		{
			MLVVSense s = SenseParser.me().parse(sensesParts[senseNumber], entry.head.lemma.text);
			if (s != null) entry.senses.add(s);
			if (numberSenses && (s.ordNumber == null || s.ordNumber.isEmpty()))
				s.ordNumber = Integer.toString(senseNumber + 1);
		}
	}

	protected void parseEtymology(MLVVEntry entry, String linePart)
	{
		linePart = Editors.replaceHomIds(linePart, true);
		entry.etymology = Editors.translateCursive(linePart, MLVVEntry.UNDERSCORE_FOR_ETYMOLOGY_CURSIVE);
	}

	protected void parseNormative(MLVVEntry entry, String linePart)
	{
		entry.normative = Editors.translateCursive(linePart, MLVVEntry.UNDERSCORE_FOR_NORMATIVE_CURSIVE);
	}
	// TODO - atstāt kvadrātiekavas vai mest ārā


}
