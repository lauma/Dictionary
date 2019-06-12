package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.TreeSet;

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
	protected MLVVGram(){};

	/**
	 * inicializē laukus par jaunugrmatikas elementu, doto tekstu sadalot pa
	 * freeText un flagText.
	 */
	public void reinitialize(String text)
	{
		flagText = null;
		this.freeText = text;
		separateFlagText();
	}

	/**
	 * Savāc elementā izmantotos "flagText". Semikolu uzskata par atdalītāju.
	 */
	public TreeSet<String> getFlagStrings()
	{
		TreeSet<String> res = new TreeSet<>();
		if (flagText != null)
			res.addAll(Arrays.asList(flagText.split("\\s*;\\s*")));
		if (altLemmas != null) for (Header h : altLemmas)
			res.addAll(((MLVVHeader)h).getFlagStrings());
		if (formRestrictions != null) for (Header h : formRestrictions)
			res.addAll(((MLVVHeader)h).getFlagStrings());
		return res;
	}

	public void addStar()
	{
		if (flagText == null) flagText = "*";
		else flagText = "*, " + flagText;

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
			if (!freeText.contains(" -") && ! freeText.startsWith("-") &&
					freeText.matches(".*?\\.\\s*[,;]?\\s*"))
			{
				flagText = freeText;
				freeText = null;
			}
		}
		// Jaunvārdu zvaigznīte, pieņemsim, ka arī ir karodziņš.
		if (freeText != null && freeText.startsWith("*"))
		{
			if (flagText == null || flagText.isEmpty()) flagText = "*";
			else flagText = "*, " + flagText;
			freeText = freeText.substring(1).trim();
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
