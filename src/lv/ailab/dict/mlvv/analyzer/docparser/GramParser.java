package lv.ailab.dict.mlvv.analyzer.docparser;

import lv.ailab.dict.mlvv.analyzer.PreNormalizer;
import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.mlvv.struct.MLVVGram;
import lv.ailab.dict.mlvv.struct.MLVVHeader;
import lv.ailab.dict.struct.Header;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GramParser
{
	protected static GramParser singleton = new GramParser();
	public static GramParser me()
	{
		return singleton;
	}

	public MLVVGram parse(MLVVElementFactory factory, String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;

		MLVVGram gram = factory.getNewGram();
		// ir ierobežojošās formas
		Matcher gramFullRestrForms = Pattern.compile(
				"((?:(?:(?!<u>).)*?[,;]\\s)?)" // kaut kas pirms pasvītrojuma
					+ "([^;,]+):" // ar kolu atdalīta gramatikas daļa
					+ "((?:</i>)?)\\s" // aizverošais i, ja vajag
					+ "(<u>.*?)" //pasvītrotās formas (ieskaitot izrunu)
					+ "((?:;\\s(?:(?!</?u>|\\]).)*|<i>(?:(?!</?u>|<i>|\\])[^;])*)?)")
				.matcher(linePart);
		Matcher gramSimpleRestrForms = Pattern.compile(
				"(<i>.*(?:</i>))?(?<!kopdz\\.\\s?(?:</i>)?\\s?)"	// Daļa pirms kola, bet netrigerot dalījumu, ja kols ir tieši pēc "kopdz."
						+ ":\\s*((?:<i>)?.*</i>\\.?)")				// Daļa pēc kola.
				.matcher(linePart);
		Matcher gramAltLemmaBraces = Pattern.compile(
				"(<b>(?:(?!<[bu]>).)*</b>(?:(?!<[bu]>)[^(])*)" // pirmā altlemma
					+ "\\(((?:<i>(?:(?!<b>).)*</i>)?\\s*<b>[^)]+)\\),?" // altlemma iekavās
					+ "((?:(?!<[bu]>).)*)") // pārējais, kas te drīkst būt
				.matcher(linePart);
		if (gramFullRestrForms.matches()) // ir ierobežojošās formas.
		{
			linePart = gramFullRestrForms.group(1);
			String restrFirstFlag = gramFullRestrForms.group(2).trim() + gramFullRestrForms.group(3);
			String restrForms = gramFullRestrForms.group(4);
			String restrLastFlags = gramFullRestrForms.group(5).trim();
			MLVVGram tmpGram = parse(factory, linePart);
			if (tmpGram != null) gram = tmpGram;
			parseFullRestrForms(gram, factory, restrFirstFlag, restrForms, restrLastFlags);
		}
		// Ierobežojošās formas ir pieminētas (uz to norāda kols), bet nav precīzi izrakstītas).
		else if (gramSimpleRestrForms.matches())
			parseSimpleRestForms(gram, factory, gramSimpleRestrForms.group(1),
					gramSimpleRestrForms.group(2));
		// Ir altLemma iekavās
		else if (gramAltLemmaBraces.matches())
			parseBracedAltLemmas(gram, factory, gramAltLemmaBraces.group(1),
					gramAltLemmaBraces.group(2), gramAltLemmaBraces.group(3));
		// Ir parastā altLemma
		else if (linePart.contains("<b>"))
			parseStandardAltLemmas(gram, factory, linePart);
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
			MLVVGram gram, MLVVElementFactory factory, String restrFirstFlag,
			String restrForms, String restrLastFlags)
	{
		String[] restrParts = restrForms.split("(?=\\b(retāk|arī)</i>\\s<u>|(?<!\\b(retāk|arī)</i>\\s)<u>)");
		if (gram.formRestrictions == null)gram.formRestrictions = new LinkedList<>();

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
			MLVVHeader restr = HeaderParser.me().parseSingularHeader(factory, p, prefix);
			restrFirstFlag = restrFirstFlag.replaceAll("</?i>", "").replaceAll("\\s+", " ").trim();
			restrLastFlags = restrLastFlags.replaceAll("</?i>", "").replaceAll("\\s+", " ").trim();
			if (restrLastFlags.startsWith(";") || restrLastFlags.startsWith(","))
				restrLastFlags = restrLastFlags.substring(1).trim();
			if (restrFirstFlag.length() > 0 || restrLastFlags.length() > 0)
			{
				MLVVGram restrGram = (MLVVGram)restr.gram;
				if (restrGram == null) restrGram = factory.getNewGram();
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

	/**
	 * Pārskatāmības labad izgriezts no parse(), pieņem, ka ieejas datu pārbaude
	 * jau ir veikta parse().
	 * @param preColonPart	gramatikas daļa pirms kola
	 * @param postColonPart	gramatikas daļa pēc kola
	 */
	protected void parseSimpleRestForms(
			MLVVGram gram, MLVVElementFactory factory, String preColonPart,
			String postColonPart)
	{
		preColonPart = Editors.closeCursive(preColonPart);
		postColonPart = Editors.openCursive(postColonPart);
		MLVVHeader restr = factory.getNewHeader();
		restr.gram = parse(factory, preColonPart);
		if (gram.formRestrictions == null) gram.formRestrictions = new LinkedList<>();
		gram.formRestrictions.add(restr);
		gram.freeText = postColonPart;
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
	protected void parseBracedAltLemmas(
			MLVVGram gram, MLVVElementFactory factory, String beforeBracesPart,
			String inBracesPart, String afterBracesPart)
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

		if (gram.altLemmas == null) gram.altLemmas = new LinkedList<>();
		Header altLemma = HeaderParser.me().parseSingularHeader(factory,
				PreNormalizer.correctGeneric(beforeBracesPart + afterBracesPart));
		if (altLemma!= null) gram.altLemmas.add(altLemma);
		altLemma = HeaderParser.me().parseSingularHeader(factory,
				PreNormalizer.correctGeneric(inBracesPart + afterBracesPart));
		if (altLemma!= null) gram.altLemmas.add(altLemma);
	}

	/**
	 * Pārskatāmības labad izgriezts no parse(), pieņem, ka ieejas datu pārbaude
	 * jau ir veikta parse().
	 * @param linePart	apstrādājamā rindas daļa (un tikai)
	 */
	protected void parseStandardAltLemmas(
			MLVVGram gram, MLVVElementFactory factory, String linePart)
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
					if (gram.freeText == null) gram.freeText = "";
					else if (!gram.freeText.trim().isEmpty()) gram.freeText += "; ";
					gram.freeText += bigPart;
				} else // Aizdomīga situācija, šitā nevajadzētu būt, bet nu tad piekabina
				// iepriekš atrastajiem fragmentiem, kuros ir lemmas.
				{
					System.out.printf("Nav skaidrs, uz ko attiecas daļa \"%s\" gramatikā \"%s\"!\n",
							bigPart, linePart);
					if (preprocBigBarts.isEmpty())
					{
						if (gram.freeText == null) gram.freeText = "";
						else if (!gram.freeText.trim().isEmpty()) gram.freeText += "; ";
						gram.freeText += bigPart;
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
				Matcher removeStartSpam = Pattern.compile("(?:,?\\s*|:</i>\\s*|(?:<i>)?arī(?:</i>|\\s+))*(<b>.*)").matcher(bigPart);
				if (removeStartSpam.matches()) bigPart = removeStartSpam.group(1);
				//if (bigPart.matches("</i>\\s*<b>.*"))
				//	bigPart = bigPart.substring(4).trim();
				String prefixPart = bigPart.substring(0, bigPart.indexOf("<b>")).trim();
				bigPart = bigPart.substring(bigPart.indexOf("<b>"));
				//if (Editors.removeCursive(prefixPart).equals("arī")) prefixPart = "";

				int commonStart = bigPart.lastIndexOf("<i>");
				int commonEnd = bigPart.indexOf("</i>", commonStart);
				// Ja neatrod kopīgo daļu, tad ir šitā:
				String common = "";
				String forSplit = bigPart;
				// Pēc commonStart vairs nebūtu jāatrodas nevienai galotnei.
				if (commonStart > bigPart.lastIndexOf("</b>") && !(commonEnd != -1 && commonEnd < bigPart.length() - "</i>".length() - 2))
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
					if (gram.altLemmas == null) gram.altLemmas = new LinkedList<>();
					for (String smallPart : smallParts)
					{
						Matcher getContent = Pattern.compile("(.*?)[,;]? (?:<i>)?arī(?:</i>)?").matcher(smallPart);
						if (getContent.matches()) smallPart = getContent.group(1);
						smallPart = smallPart.trim();
						if (smallPart.matches(".*</b>\\s*,"))
							smallPart = smallPart.substring(0, smallPart.length()-1).trim();
						else if (!smallPart.endsWith(",") && !smallPart.endsWith("</b>"))
							smallPart += ",";
						if (!smallPart.contains(subcommon))
							smallPart = smallPart + " " + common;
						Header altLemma = HeaderParser.me().parseSingularHeader(
								factory, smallPart, prefixPart);
						if (altLemma!= null) gram.altLemmas.add(altLemma);
					}
				}
			} else
				// Šitā nevajadzētu būt, jo taču visus šitos apstrādāja iepriekšējā ciklā.
				System.out.printf("Loģikas kļūda, apstrādājot gramatikas fragmentu \"%s\" gramatikā \"%s\"!\n",
						bigPart, linePart);
		}
	}

}
