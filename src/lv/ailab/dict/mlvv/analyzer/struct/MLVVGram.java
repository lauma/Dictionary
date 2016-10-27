package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Izveidots 2016-02-03.
 *
 * @author Lauma
 */
public class MLVVGram extends Gram
{
	/**
	 * freeText izmanto galotņu šabloniem, flagtext - gramatikas beigu daļai
	 * kursīvā.
	 */
	public String flagText = null;
	public MLVVGram(){};

	/**
	 * Uztaisa jaunu grmatikas elementu, doto tekstu sadalot pa freeText un
	 * flagText.
	 */
	public MLVVGram(String text)
	{
		super();
		this.freeText = text;
		separateFlagText();
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

		gram.separateFlagText();
		return gram;

	}

	protected static String normalizeGramField(String field)
	{
		if (field == null) return null;
		while (field.startsWith(",") || field.startsWith(";"))
			field = field.substring(1).trim();
		while (field.endsWith(",") || field.endsWith(";"))
			field = field.substring(0, field.length() - 1).trim();
		return field;
	}
	public void normalizeFreeText()
	{
		freeText = normalizeGramField(freeText);
		if (freeText != null && freeText.isEmpty()) freeText = null;
	}

	public void normalizeFlagText()
	{
		flagText = normalizeGramField(flagText);
		if (flagText != null && flagText.isEmpty()) flagText = null;
	}

	public void separateFlagText()
	{
		// Priekšsatīrīšana.
		normalizeFreeText();
		if (freeText == null) return;

		// Analīze, kā dalīt.
		int lastIOpen = freeText.lastIndexOf("<i>");
		int lastIClose = freeText.lastIndexOf("</i>");
		if (lastIOpen > -1 &&
				(lastIClose == freeText.length() - "</i>".length() ||
						lastIClose == freeText.length() - "</i>".length() - 1 ||
						lastIOpen > lastIClose))
		{
			flagText = freeText.substring(lastIOpen).trim();
			freeText = freeText.substring(0, lastIOpen).trim();
		}
		else if (lastIOpen == -1 && (lastIClose == -1 ||
				lastIClose == freeText.length() - "</i>".length() ||
				lastIClose == freeText.length() - "</i>".length() - 1))
		{
			if (!freeText.contains(" -"))
			{
				flagText = freeText;
				freeText = null;
			}
		}

		// Satīra visu iegūto.
		if (flagText != null)
		{
			flagText = flagText.replace("<i>", "");
			flagText = flagText.replace("</i>", "");
		}
		if (freeText != null)
		{
			freeText = freeText.replace("<i>", "");
			freeText = freeText.replace("</i>", "");
		}
		normalizeFreeText();
		normalizeFlagText();
	}

	/**
	 * Pārrakstīta JSON izvade, lai atspoguļotu flagText lauku.
	 */
	@Override
	public String toJSON ()
	{
		if (flagText != null)
			return toJSON("Inflection", "\"FlagText\":\"" + JSONObject.escape(flagText) + "\"");
		else return toJSON("Inflection", null);
	}

	/**
	 * Pāarakstīta XML izveide, lai atspoguļotu flagText lauku.
	 */
	@Override
	public void toXML(Node parent)
	{
		if (flagText != null)
		{
			Document doc = parent.getOwnerDocument();
			Element flagTextNode = doc.createElement("FlagText");
			flagTextNode.appendChild(doc.createTextNode(flagText));
			toXML(parent, "Inflection", flagTextNode);
		}
		else toXML(parent, "Inflection", null);
	}

}
