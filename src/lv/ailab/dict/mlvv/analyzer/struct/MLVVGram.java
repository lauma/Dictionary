package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 2016-02-03.
 *
 * @author Lauma
 */
public class MLVVGram extends Gram
{
	public MLVVGram(){};

	/**
	 * Uztaisa jaunu grmatikas elementu, uzstādot freeText un normalizējot to.
	 */
	public MLVVGram(String freeText)
	{
		super();
		this.freeText = freeText;
		normalizeFreeText();
	}
	public static MLVVGram extractFromString(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;

		MLVVGram gram = new MLVVGram();
		if (linePart.contains("<b>")) // Ir altLemmas
		{
			// Šādi sadalās tās daļas, kur uz vairākām vārdformām ir viena
			// vārdšķira.
			String[] bigParts = new String[]{linePart};
			if (linePart.contains(";")) bigParts = linePart.split(";\\s*");
			for (int i = 0; i < bigParts.length; i++)
			{

				if (bigParts[i].contains("<b>"))
				{
					int commonStart = bigParts[i].lastIndexOf("<i>");
					// Ja neatrod kopīgo daļu, tad ir šitā:
					String common = "";
					String forSplit = bigParts[i];
					if (commonStart > -1)
					{
						common = bigParts[i].substring(commonStart);
						forSplit = bigParts[i].substring(0, commonStart);
					}
					ArrayList<String> smallParts = new ArrayList<>(Arrays.asList(
							forSplit.split("(?<!\\s)\\s*(?=<b>)")));

					// No visiem gabaliem veido altLemmas.
					if (smallParts.size() > 0)
					{
						if (gram.altLemmas == null) gram.altLemmas = new ArrayList<>();
						for (String smallPart : smallParts)
						{
							smallPart = smallPart.trim();
							if (smallPart.matches(".*</b>\\s*,"))
								smallPart = smallPart.substring(0, smallPart.length()-1).trim();
							else if (!smallPart.endsWith(",")) smallPart += ",";
							smallPart = smallPart + " " + common;
							Header altLemma = MLVVHeader.extractSingularHeader(smallPart);
							if (altLemma!= null) gram.altLemmas.add(altLemma);
						}
					}
				} else
				{
					if (i == bigParts.length - 1) // Elementi, kas attiecināmi uz visām altLemmām un arī uz pamata šķirkļa vārdu.
					{
						if (gram.freeText == null) gram.freeText = "";
						else if (!gram.freeText
								.trim().isEmpty()) gram.freeText += "; ";
						gram.freeText += bigParts[i];
					} else // Aizdomīga situācija, šitā nevajadzētu būt, bet nu tad piekabina
					       // iepriekš atrastajām alt lemmām.
					{
						System.out.printf("Nav skaidrs, uz ko attiecas daļa \"%s\" gramatikā \"%s\"!\n",
								bigParts[i], linePart);
						if (gram.altLemmas == null || gram.altLemmas.isEmpty())
						{
							if (gram.freeText == null) gram.freeText = "";
							else if (!gram.freeText
									.trim().isEmpty()) gram.freeText += "; ";
							gram.freeText += bigParts[i];
						} else for (Header al : gram.altLemmas)
						{
							if (al.gram.freeText == null) al.gram.freeText = "";
							else if (!al.gram.freeText
									.trim().isEmpty()) al.gram.freeText += "; ";
							al.gram.freeText += bigParts[i];
						}
					}
				}
			}
		} else // nav altLemmu
			gram.freeText = linePart;

		gram.normalizeFreeText();
		return gram;

	}

	public void normalizeFreeText()
	{
		if (freeText == null) return;
		while (freeText.startsWith(",") || freeText.startsWith(";"))
			freeText = freeText.substring(1).trim();
		while (freeText.endsWith(",") || freeText.endsWith(";"))
			freeText = freeText.substring(0, freeText.length() - 1).trim();
	}

}
