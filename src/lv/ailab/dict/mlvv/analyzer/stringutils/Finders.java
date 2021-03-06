package lv.ailab.dict.mlvv.analyzer.stringutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sīkas ērtības metodes, kas kaut ko atrod oriģinālajā vārdnīcas
 * tekstā/noformējumā.
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class Finders
{
	/**
	 * Atrod pirmo bullet vai circle tagu. Circle tagam indeksu rēķina atkarībā
	 * no tā, vai pirms tam ir ar kolu atdalīta gramatika kursīvā.
	 */
	public static int getAnyCircleIndex(String linePart)
	{
		int bulletIndex = linePart.indexOf("<bullet/>");

		// Oriģināli te bija (Pārn|Intr|Tr|Sal) kā vienīgie atzītie saīsinājumi,
		// bet tā kā to pašu čeko arī frāžu apstrādes funkcija, tad te ir
		// atstāts vispārīgais.
		Matcher m = Pattern.compile("((<i>)?\\p{Lu}\\p{Ll}+\\.</i>: )?<circle/>").matcher(linePart);
		int circleIndex = -1;
		if (m.find()) circleIndex = m.start();
		int res = -1;
		/*if (bulletIndex > -1 && circleGramIndex > -1)
			res = Math.min(bulletIndex, circleGramIndex);
		else if (bulletIndex > -1 && circleGramShortIndex > -1)
			res = Math.min(bulletIndex, circleGramShortIndex); else*/
		if (bulletIndex > -1 && circleIndex > -1)
			res = Math.min(bulletIndex, circleIndex);
		else if (bulletIndex > -1)
			res = bulletIndex;
		/*else if (circleGramIndex > -1)
			res = circleGramIndex;
		else if (circleGramShortIndex > -1)
			res = circleGramShortIndex;*/
		else if (circleIndex > -1)
			res = circleIndex;
		return res;
	}
}
