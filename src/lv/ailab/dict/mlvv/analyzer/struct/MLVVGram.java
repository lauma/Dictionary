package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.PreNormalizer;
import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.utils.StringUtils;
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
				"((?:(?:(?!<u>).)*?[,;]\\s)?)" // kaut kas pirms pasvītrojuma
					+ "([^;,]+):" // ar kolu atdalīta gramatikas daļa
					+ "((?:</i>)?)\\s" // aizverošais i, ja vajag
					+ "(<u>.*?)" // pirmā pasvītrotā forma
					+ "((?:;\\s(?:(?!</?u>).)*|<i>(?:(?!</?u>|<i>)[^;])*)?)")
				.matcher(linePart);
		Matcher gramSimpleRestrForms = Pattern.compile(
				"(<i>.*(?:</i>))?(?<!kopdz\\.\\s?(?:</i>)?\\s?)"	// Daļa pirms kola, bet netrigerot dalījumu, ja kols ir tieši pēc "kopdz."
						+ ":\\s*((?:<i>)?.*</i>\\.?)")				// Daļa pēc kola.
				.matcher(linePart);
		Matcher gramAltLemmaBraces = Pattern.compile(
				"(<b>(?:(?!<[bu]>).)*</b>(?:(?!<[bu]>)[^(])*)" // pirmā altlemma
					+ "\\(((?:<i>(?:(?!<b>).)*</i>)\\s*<b>[^)]+)\\)" // altlemma iekavās
					+ "((?:(?!<[bu]>).)*)") // pārējais, kas te drīkst būt
				.matcher(linePart);
		if (gramFullRestrForms.matches()) // ir ierobežojošās formas.
		{
			linePart = gramFullRestrForms.group(1);
			String restrFirstFlag = gramFullRestrForms.group(2).trim() + gramFullRestrForms.group(3);
			String restrForms = gramFullRestrForms.group(4);
			String restrLastFlags = gramFullRestrForms.group(5).trim();
			MLVVGram tmpGram = MLVVGram.parse(linePart);
			if (tmpGram != null) gram = tmpGram;
			gram.parseFullRestrForms(restrFirstFlag, restrForms, restrLastFlags);
		}
		// Ierobežojošās formas ir pieminētas (uz to norāda kols), bet nav precīzi izrakstītas).
		else if (gramSimpleRestrForms.matches())
			gram.parseSimpleRestForms(
					gramSimpleRestrForms.group(1), gramSimpleRestrForms.group(2));
		// Ir altLemma iekavās
		else if (gramAltLemmaBraces.matches())
			gram.parseBracedAltLemmas(gramAltLemmaBraces.group(1),
					gramAltLemmaBraces.group(2), gramAltLemmaBraces.group(3));
		// Ir parastā altLemma
		else if (linePart.contains("<b>"))
			gram.parseStandardAltLemmas(linePart);
		else // nav altLemmu
			gram.freeText = linePart;

		gram.separateFlagText();
		return gram;
	}

	/**
	 * Pārskatāmības labad izgriezts no parse(), pieņem, ka ieejas datu pārbaude
	 * jau ir veikta parse().
	 * @param restrFirstFlag
	 * @param restrForms
	 * @param restrLastFlags
	 */
	protected void parseFullRestrForms(
			String restrFirstFlag, String restrForms, String restrLastFlags)
	{
		String[] restrParts = restrForms.split("(?=\\b(retāk|arī)</i>\\s<u>|(?<!\\b(retāk|arī)</i>\\s)<u>)");
		if (formRestrictions == null) formRestrictions = new ArrayList<>();

		for (String p : restrParts)
		{
			String prefix = null;
			if (p.startsWith("retāk</i>"))
			{
				prefix = "<i>retāk</i>";
				p = p.substring("retāk</i>".length()).trim();
			} else if (p.startsWith("arī</i>"))
			{
				p = p.substring("arī</i>".length()).trim();
			}
			MLVVHeader restr = MLVVHeader.parseSingularHeader(p, prefix);
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
			formRestrictions.add(restr);
		}
	}

	/**
	 * Pārskatāmības labad izgriezts no parse(), pieņem, ka ieejas datu pārbaude
	 * jau ir veikta parse().
	 * @param preColonPart	gramatikas daļa pirms kola
	 * @param postColonPart	gramatikas daļa pēc kola
	 */
	protected void parseSimpleRestForms(String preColonPart, String postColonPart)
	{
		preColonPart = Editors.closeCursive(preColonPart);
		postColonPart = Editors.openCursive(postColonPart);
		MLVVHeader restr = new MLVVHeader();
		restr.gram = MLVVGram.parse(preColonPart);
		if (formRestrictions == null) formRestrictions = new ArrayList<>();
		formRestrictions.add(restr);
		freeText = postColonPart;
	}

	/**
	 * Pārskatāmības labad izgriezts no parse(), pieņem, ka ieejas datu pārbaude
	 * jau ir veikta parse().
	 * @param beforeBracesPart	gramatikas daļa pirms iekavām (pirmais
	 *                          šķirkļavārds + tā pazīmes)
	 * @param inBracesPart		gramatikas daļa iekavās (otrs šķirkļavārds + tā
	 *                          pazīmes)
	 * @param afterBracesPart	gramatikas daļa pēc iekavām
	 */
	protected void parseBracedAltLemmas(String beforeBracesPart, String inBracesPart, String afterBracesPart)
	{
		beforeBracesPart = beforeBracesPart.trim();
		afterBracesPart = Editors.openCursive(afterBracesPart);
		inBracesPart = inBracesPart.trim();
		String inBracesPrefix = inBracesPart.substring(0, inBracesPart.indexOf("<b>")).trim();
		inBracesPart = inBracesPart.substring(inBracesPart.indexOf("<b>"));
		if (!beforeBracesPart.endsWith("</b>") && !afterBracesPart.isEmpty())
			beforeBracesPart = beforeBracesPart + ";";
		beforeBracesPart = Editors.closeCursive(beforeBracesPart);
		if (!inBracesPrefix.isEmpty())
			inBracesPart = inBracesPart + "; " + inBracesPrefix;
		if (!afterBracesPart.isEmpty())
			inBracesPart = inBracesPart + "; ";
		inBracesPart = Editors.closeCursive(inBracesPart);

		if (altLemmas == null) altLemmas = new ArrayList<>();
		Header altLemma = MLVVHeader.parseSingularHeader(
				PreNormalizer.correctGeneric(beforeBracesPart + afterBracesPart));
		if (altLemma!= null) altLemmas.add(altLemma);
		altLemma = MLVVHeader.parseSingularHeader(
				PreNormalizer.correctGeneric(inBracesPart + afterBracesPart));
		if (altLemma!= null) altLemmas.add(altLemma);
	}

	/**
	 * Pārskatāmības labad izgriezts no parse(), pieņem, ka ieejas datu pārbaude
	 * jau ir veikta parse().
	 * @param linePart	apstrādājamā rindas daļa (un tikai)
	 */
	protected void parseStandardAltLemmas(String linePart)
	{
		// Šādi sadalās tās daļas, kur uz vairākām vārdformām ir viena
		// vārdšķira.
		String[] bigParts = new String[]{linePart};
		if (linePart.contains(";")) bigParts = linePart.split(";\\s*");
		ArrayList<String> preprocBigBarts = new ArrayList<>();
		// Vispirms apstrādā tos gabalus, kuros nav savu lemmu.
		for (int i = 0; i < bigParts.length; i++)
		{
			String bigPart = Editors.openCursive(Editors.closeCursive(bigParts[i]));
			if (bigPart.contains("<b>")) preprocBigBarts.add(bigPart);
			else
			{
				boolean hasNextAltLemma = false;
				for (int j = i + 1; j < bigParts.length; j ++)
					if (bigParts[j].contains("<b>")) hasNextAltLemma = true;
				if (!hasNextAltLemma) // Elementi, kas attiecināmi uz visām altLemmām un arī uz pamata šķirkļa vārdu.
				{
					if (freeText == null) freeText = "";
					else if (!freeText.trim().isEmpty()) freeText += "; ";
					freeText += bigPart;
				} else // Aizdomīga situācija, šitā nevajadzētu būt, bet nu tad piekabina
				// iepriekš atrastajiem fragmentiem, kuros ir lemmas.
				{
					System.out.printf("Nav skaidrs, uz ko attiecas daļa \"%s\" gramatikā \"%s\"!\n",
							bigPart, linePart);
					if (preprocBigBarts.isEmpty())
					{
						if (freeText == null) freeText = "";
						else if (!freeText.trim().isEmpty()) freeText += "; ";
						freeText += bigPart;
					} else for (int j = 0; j < preprocBigBarts.size(); j++)
						preprocBigBarts.set(j, preprocBigBarts.get(j) + "; " + bigPart);
				}
			}
		}
		// Apstrādā gramatikas daļas, kurās ir lemmas.
		for (String bigPart : preprocBigBarts)
		{
			if (bigPart.contains("<b>"))
			{
				if (bigPart.matches("</i>\\s*<b>.*"))
					bigPart = bigPart.substring(4).trim();
				String prefixPart = bigPart.substring(0, bigPart.indexOf("<b>")).trim();
				bigPart = bigPart.substring(bigPart.indexOf("<b>"));
				if (Editors.removeCursive(prefixPart).equals("arī")) prefixPart = "";

				int commonStart = bigPart.lastIndexOf("<i>");
				int commonEnd = bigPart.indexOf("</i>", commonStart);
				// Ja neatrod kopīgo daļu, tad ir šitā:
				String common = "";
				String forSplit = bigPart;
				// Pēc commonStart vairs nebūtu jāatrodas nevienai galotnei.
				if (commonStart > -1 && !(commonEnd != -1 && commonEnd < bigPart.length() - "</i>".length() - 2))
				{
					common = bigPart.substring(commonStart);
					forSplit = bigPart.substring(0, commonStart);
				}
				ArrayList<String> smallParts = new ArrayList<>(Arrays.asList(
						forSplit.split("(?<!\\s)\\s*(?=<b>)")));

				// No visiem gabaliem veido altLemmas.
				if (smallParts.size() > 0)
				{
					String subcommon = common;
					if (subcommon.startsWith("<i>")) subcommon = subcommon.substring(3);
					if (subcommon.endsWith("</i>")) subcommon = subcommon.substring(0, subcommon.length()-4);
					if (altLemmas == null) altLemmas = new ArrayList<>();
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
						Header altLemma = MLVVHeader.parseSingularHeader(smallPart, prefixPart);
						if (altLemma!= null) altLemmas.add(altLemma);
					}
				}
			} else
				// Šitā nevajadzētu būt, jo taču visus šitos apstrādāja iepriekšējā ciklā.
				System.out.printf("Loģikas kļūda, apstrādājot gramatikas fragmentu \"%s\" gramatikā \"%s\"!\n",
						bigPart, linePart);
		}
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
