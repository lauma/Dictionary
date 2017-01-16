package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.PreNormalizer;
import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gram funkcionalitāte papildināta ar jaunu teksta lauku + izgūšanas metodes.
 * Izveidots 2016-02-03.
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
	public static MLVVGram parse(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;

		MLVVGram gram = new MLVVGram();
		// ir ierobežojošās formas
		Matcher gramFullRestrForms = Pattern.compile(
				"((?:(?:(?!<u>).)*?[,;]\\s)?)([^;,]+):((?:</i>)?)\\s(<u>.*?)((?:;\\s(?:(?!</?u>).)*|<i>(?:(?!</?u>|<i>)[^;])*)?)")
				.matcher(linePart);
		Matcher gramSimpleRestrForms = Pattern.compile(
				"(<i>.*(?:</i>))?:\\s*((?:<i>)?.*</i>\\.?)")
				.matcher(linePart);
		Matcher gramAltLemmaBraces = Pattern.compile(
				"(<b>(?:(?!<[bu]>).)*</b>(?:(?!<[bu]>)[^(])*)\\((<b>[^)]+)\\)((?:(?!<[bu]>).)*)")
				.matcher(linePart);
		if (gramFullRestrForms.matches()) // ir ierobežojošās formas.
		{
			linePart = gramFullRestrForms.group(1);
			String restrFirstFlag = gramFullRestrForms.group(2).trim() + gramFullRestrForms.group(3);
			String restrForms = gramFullRestrForms.group(4);
			String restrLastFlags = gramFullRestrForms.group(5).trim();
			String[] restrParts = restrForms.split("(?=<u>)");
			gram.formRestrictions = new ArrayList<>();

			for (String p : restrParts)
			{
				MLVVHeader restr = MLVVHeader.parseSingularHeader(p);
				restrFirstFlag = restrFirstFlag.replaceAll("</?i>", "").replaceAll("\\s+", " ").trim();
				restrLastFlags = restrLastFlags.replaceAll("</?i>", "").replaceAll("\\s+", " ").trim();
				if (restrLastFlags.startsWith(";") || restrLastFlags.startsWith(","))
					restrLastFlags = restrLastFlags.substring(1).trim();
				if (restrFirstFlag.length() > 0 || restrLastFlags.length() > 0)
				{
					MLVVGram restrGram = (MLVVGram)restr.gram;
					if (restrGram == null) restrGram = new MLVVGram();
					if (restrGram.flagText != null)
						restrGram.flagText = restrGram.flagText.trim();
					if (restrFirstFlag.length() > 0 )
					{
						if (restrGram.flagText == null || restrGram.flagText.length() < 1)
							restrGram.flagText = restrFirstFlag;
						else restrGram.flagText = restrFirstFlag + "; " + restrGram.flagText;
					}
					if (restrLastFlags.length() > 0)
					{
						if (restrGram.flagText == null || restrGram.flagText.length() < 1)
							restrGram.flagText = restrLastFlags;
						else
						{
							restrGram.flagText = restrGram.flagText.trim();
							if (restrGram.flagText.endsWith(",") || restrGram.flagText.endsWith(";"))
								restrGram.flagText = restrGram.flagText + " " + restrLastFlags;
							else restrGram.flagText = restrGram.flagText + "; " + restrLastFlags;
						}
					}
					restr.gram = restrGram;
				}
				gram.formRestrictions.add(restr);
			}
		}
		else if (gramSimpleRestrForms.matches())
		{
			//System.out.println("Simple restr! " + linePart);
			String beginPart = Editors.closeCursive(gramSimpleRestrForms.group(1));
			String endPart = Editors.openCursive(gramSimpleRestrForms.group(2));
			MLVVHeader restr = new MLVVHeader();
			restr.gram = MLVVGram.parse(beginPart);
			gram.formRestrictions = new ArrayList<>();
			gram.formRestrictions.add(restr);
			gram.freeText = endPart;
		}
		// Ir altLemma iekavās
		else if (gramAltLemmaBraces.matches())
		{
			String endPart = Editors.openCursive(gramAltLemmaBraces.group(3));
			String beginPart = gramAltLemmaBraces.group(1).trim();
			if (!beginPart.endsWith("</b>") && !endPart.isEmpty())
				beginPart = beginPart + ";";
			beginPart = Editors.closeCursive(beginPart);
			String middlePart = gramAltLemmaBraces.group(2);
			if (!endPart.isEmpty())
				middlePart = middlePart + "; ";
			middlePart = Editors.closeCursive(middlePart);

			if (gram.altLemmas == null) gram.altLemmas = new ArrayList<>();
			Header altLemma = MLVVHeader.parseSingularHeader(
					PreNormalizer.correctGeneric(beginPart + endPart));
			if (altLemma!= null) gram.altLemmas.add(altLemma);
			altLemma = MLVVHeader.parseSingularHeader(
					PreNormalizer.correctGeneric(middlePart + endPart));
			if (altLemma!= null) gram.altLemmas.add(altLemma);
		}
		// Ir parastā altLemma
		else if (linePart.contains("<b>"))
		{
			// Šādi sadalās tās daļas, kur uz vairākām vārdformām ir viena
			// vārdšķira.
			String[] bigParts = new String[]{linePart};
			if (linePart.contains(";")) bigParts = linePart.split(";\\s*");
			for (int i = 0; i < bigParts.length; i++)
			{
				if (bigParts[i].contains("<b>"))
				{
					if (bigParts[i].matches("</i>\\s*<b>.*"))
						bigParts[i] = bigParts[i].substring(4).trim();
					int commonStart = bigParts[i].lastIndexOf("<i>");
					int commonEnd = bigParts[i].indexOf("</i>", commonStart);
					// Ja neatrod kopīgo daļu, tad ir šitā:
					String common = "";
					String forSplit = bigParts[i];
					// Pēc commonStart vairs nebūtu jāatrodas nevienai galotnei.
					if (commonStart > -1 && !(commonEnd != -1 && commonEnd < bigParts[i].length() - "</i>".length() - 2))
					{
						common = bigParts[i].substring(commonStart);
						forSplit = bigParts[i].substring(0, commonStart);
					}
					ArrayList<String> smallParts = new ArrayList<>(Arrays.asList(
							forSplit.split("(?<!\\s)\\s*(?=<b>)")));

					// No visiem gabaliem veido altLemmas.
					if (smallParts.size() > 0)
					{
						String subcommon = common;
						if (subcommon.startsWith("<i>")) subcommon = subcommon.substring(3);
						if (subcommon.endsWith("</i>")) subcommon = subcommon.substring(0, subcommon.length()-4);
						if (gram.altLemmas == null) gram.altLemmas = new ArrayList<>();
						for (String smallPart : smallParts)
						{
							Matcher getContent = Pattern.compile("(.*?)[,;]? arī").matcher(smallPart);
							if (getContent.matches()) smallPart = getContent.group(1);
							smallPart = smallPart.trim();
							if (smallPart.matches(".*</b>\\s*,"))
								smallPart = smallPart.substring(0, smallPart.length()-1).trim();
							else if (!smallPart.endsWith(",") && !smallPart.endsWith("</b>"))
								smallPart += ",";
							if (!smallPart.contains(subcommon))
								smallPart = smallPart + " " + common;
							Header altLemma = MLVVHeader.parseSingularHeader(smallPart);
							if (altLemma!= null) gram.altLemmas.add(altLemma);
						}
					}
				} else
				{
					boolean hasNextAltLemma = false;
					for (int j = i + 1; j < bigParts.length; j ++)
						if (bigParts[j].contains("<b>")) hasNextAltLemma = true;
					if (!hasNextAltLemma) // Elementi, kas attiecināmi uz visām altLemmām un arī uz pamata šķirkļa vārdu.
					{
						if (gram.freeText == null) gram.freeText = "";
						else if (!gram.freeText
								.trim().isEmpty()) gram.freeText += "; ";
						gram.freeText += bigParts[i];
					} else // Aizdomīga situācija, šitā nevajadzētu būt, bet nu tad piekabina
					       // iepriekš atrastajām alt lemmām.
					{
						//if (gram.altLemmas != null && gram.altLemmas.size() > 1)
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
		field = field.trim();
		if (".".equals(field)) return null;
		while (field.startsWith(",") || field.startsWith(";"))
			field = field.substring(1).trim();
		while (field.endsWith(",") || field.endsWith(";"))
			field = field.substring(0, field.length() - 1).trim();
		// Normāli šitām lietām būtu jābūt normalizētām jau priekšapstrādē, bet
		// tad, kad lipina kopā vairākas gramatikas, var rasties atkal šitāda
		// situācija.
		field = field.replace("</i>, <i>", ", ");
		field = field.replace("</i>; <i>", "; ");
		return field;
	}
	public void normalizeFreeText()
	{
		if (freeText == null) return;
		if (freeText.isEmpty())
		{
			freeText = null;
			return;
		}
		//if (freeText.endsWith(" arī"))
		//	freeText = freeText.substring(0, freeText.length() - 4);
		freeText = normalizeGramField(freeText);
		if ("arī".equals(freeText)) freeText = null;
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
		flagText = Editors.removeCursive(flagText);
		freeText = Editors.removeCursive(freeText);
		normalizeFreeText();
		normalizeFlagText();
	}

	/**
	 * Šai gramatikai pieliek tekstuālo informāciju no citas gramatikas. Ja
	 * atbilstošie lauki aizpildīti abās gramatikās, tad pievienojamo
	 * tekstu/elementus liek vispirms.
	 * @param other				gramatika, ko pievienot.
	 * @param warnNonEmptyTexts	brīdina, ja abām apvienojamajām gramatikām ir
	 *                          netukši tie paši teksta lauki
	 */
	public void addTextsBefore(MLVVGram other, boolean warnNonEmptyTexts)
	{
		if (other.freeText != null && !other.freeText.trim().isEmpty())
		{
			if (freeText == null)
				freeText = other.freeText;
			else if (warnNonEmptyTexts)
			{
				System.out.printf(
						"Gramatika ar locījumu tekstu \"%s\" mēģina citas gramatikas locījumu tekstu \"%s\"\n",
						freeText, other.freeText);
				freeText = other.freeText + "; " + freeText;
			}
		}
		if (other.flagText != null && !other.flagText.trim().isEmpty())
		{
			if (flagText == null)
				flagText = other.flagText;
			else
			{
				System.out.printf(
						"Gramatika ar karodziņu tekstu \"%s\" mēģina citas gramatikas karodziņu tekstu \"%s\"\n",
						flagText, other.flagText);
				flagText = other.flagText + "; " + flagText;
			}
		}
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
