package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MLVVSample extends Sample
{
	/**
	 * No dotā rindas fragmenta izgūst frāzi bez skaidrojuma, bet ar gramatiku,
	 * kas, iespējams, atdalīta ar kolu.
	 * Funkcija oriģināli izstrādāta kā daļa no frāžu bloka analīzes
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo frāzi un
	 *                      neko citu
	 */
	public static MLVVSample parseNoGlossSample(String linePart)
	{
		MLVVSample res = new MLVVSample();
		//res.type = Type.SAMPLE;
		//res.text = new LinkedList<>();
		Matcher m = Pattern.compile("(.*\\.)(?::\\s+|</i>:\\s+<i>)((?!\"|\\p{Ll}).*)").matcher(linePart);
		Matcher gramConsts = Pattern.compile("(?:<i>)?\\s*(Tr\\.|Pārn\\.|Sal\\.|Intr\\.)(?:</i>)?: (?:<i>)?(.*)").matcher(linePart);
		if (m.matches())
		{
			res.grammar = new MLVVGram(m.group(1));
			linePart = m.group(2).trim();
		}
		else if (gramConsts.matches())
		{
			res.grammar = new MLVVGram(gramConsts.group(1));
			linePart = gramConsts.group(2).trim();
		}
		linePart = linePart.replace("</i>: <i>\"", ": \"");
		//res.text.add(linePart);
		res.text = linePart;
		return res;
	}

	/**
	 * Metode, kas no dotā šķirkļa fragmenta izgūst citāta tipa frāzi.
	 * @param linePart	šķirkļa teksta daļa, kas apraksta tieši šo frāzi un neko
	 *                  citu
	 * @return izgūtā frāze vai null
	 */
	public static MLVVSample extractQuote(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.replaceAll("\\s\\s+", " ").trim();
		if (linePart.length() < 1) return null;

		MLVVSample res = new MLVVSample();
		//res.type = Type.QUOTE;
		//res.text = new LinkedList<>();
		// TODO: vai šeit likt \p{Lu}\p{Ll}+ nevis (Parn|Intr) ?
		Matcher m = Pattern.compile(
				"((?:(?:<i>)?\\s*(?:Pārn|Intr|Tr|Sal)\\.</i>:\\s*)?)"	//  Neobligāts Pārn. kursīvā :
						+ "((?:(?:\\.{2,3}|[\"\u201e\u201d])\\s*)?)<i>(.*?)</i>"	// neobligātas pieturzīmes <i> citāta teksts </i> neobligātas pieturz.
						+ "([.?!\"\u201e\u201d]*)\\s*\\((.*)\\)\\.?")			// (autors).
				.matcher(linePart);
		if (m.matches())
		{
			//res.text.add(
			res.text =
					Editors.removeCursive((m.group(2) + m.group(3) + m.group(4)))
							.replaceAll("\\s\\s+", " ").trim();
			res.citedSource = m.group(5).trim();
			String gramString = Editors.removeCursive(m.group(1)).trim();
			if (gramString.endsWith(":")) gramString = gramString.substring(0, gramString.length()-1);
			res.grammar = new MLVVGram(gramString);
		}
		else
		{
			//res.text.add(linePart);
			res.text = linePart;
			System.out.printf("Citāts \"%s\" neatbilst apstrādes šablonam\n", linePart);
		}
		return res;
	}

	/*
	 * Remove empty variants.
	 */
	/*public void variantCleanup()
	{
		for (String variant : text)
			if (variant == null || variant.isEmpty()) text.remove(variant);
	}*/
}
