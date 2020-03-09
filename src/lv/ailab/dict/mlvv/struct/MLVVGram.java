package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Gram funkcionalitāte papildināta ar jaunu teksta lauku + izgūšanas metodes.
 * Izveidots 2016-02-03.
 * @author Lauma
 */
public class MLVVGram extends Gram
{
	/**
	 * Struktūra, kas satur norādes, ka elements, ko šī gramatika skaidro,
	 * attiecas tikai uz noteiktu šķirkļavārdu formu apakškopu. Šis elements
	 * visbiežāk sastopams gramatikās, kas atrodas pie nozīmēm.
	 * Visos saprātīgos gadījumos vajadzētu būt ne vairāk kā 1 šādam Header?
	 */
	public LinkedList<Header> formRestrictions;

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
	 * Visas paradigmas, kas vispār ir pieminētas šajā struktūrā.
	 * Izmantojams meklēšanai un statistikai.
	 */
	@Override
	public HashSet<Integer> getMentionedParadigms()
	{
		HashSet<Integer> res = super.getMentionedParadigms();
		if (formRestrictions != null && formRestrictions.size() > 0)
			for (Header h : formRestrictions)
				res.addAll(h.getMentionedParadigms());
		return res;
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
		String flagTextRep = null;
		if (flagText != null)
			flagTextRep =  "\"FlagText\":\"" + JSONObject.escape(flagText) + "\"";
		String formRestrRep = null;
		if (formRestrictions != null && !formRestrictions.isEmpty())
			formRestrRep = "\"FormRestrictions\":" + JSONUtils.objectsToJSON(formRestrictions);

		return toJSON("Inflection", formRestrRep, flagTextRep);
	}

	/**
	 * Pāarakstīta XML izveide, lai atspoguļotu flagText lauku.
	 */
	@Override
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node flagTextNode = null;
		if (flagText != null)
		{
			flagTextNode = doc.createElement("FlagText");
			flagTextNode.appendChild(doc.createTextNode(flagText));
		}
		Node formRestrNode = null;
		if (formRestrictions != null && !formRestrictions.isEmpty())
		{
			formRestrNode = doc.createElement("FormRestrictions");
			for (Header al: formRestrictions)
				if (al != null) al.toXML(formRestrNode);
		}
		toXML(parent, "Inflection", formRestrNode, flagTextNode);
	}

}
