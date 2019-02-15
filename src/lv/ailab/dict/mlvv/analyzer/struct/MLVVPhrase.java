package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.PhrasalExtractor;
import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pašlaik te nav būtiski paplašināta Phrase funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class MLVVPhrase extends Phrase
{
	/**
	 * Skaidrojamā frāze latīniski - tikai taksoniem.
	 */
	public LinkedList<String> sciName;
	/**
	 * Savāc elementā izmantotos "flagText". Semikolu uzskata par atdalītāju.
	 */
	public TreeSet<String> getFlagStrings()
	{
		TreeSet<String> res = new TreeSet<>();
		if (grammar != null) res.addAll(((MLVVGram)grammar).getFlagStrings());
		if (subsenses != null) for (Sense s : subsenses)
			res.addAll(((MLVVSense)s).getFlagStrings());
		return res;
	}

	/**
	 * Metode, kas izgūst dotā tipa frāzi no dotās simbolu virknes.
	 * @param linePart		šķirkļa teksta daļa, kas apraksta tieši šo frāzi un
	 *                      neko citu
	 * @param phraseType	frāzes tips (Type)
	 * @param lemma			šķirkļavārds šķirklim, kurā šī frāze atrodas (kļūdu
	 *                      paziņojumiem)
	 * @return	izgūtā frāze vai null
	 */
	public static MLVVPhrase parseSpecialPhrasal(String linePart, Phrase.Type phraseType, String lemma)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.length() < 1) return null;
		Matcher dashMatcher = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)").matcher(linePart);
		MLVVPhrase res = new MLVVPhrase();
		res.type = phraseType;
		res.text = new LinkedList<>();

		// Izanalizē frāzi ar skaidrojumu
		if (dashMatcher.matches())
		{
			String begin = dashMatcher.group(1).trim();
			String end = dashMatcher.group(2).trim();
			// Izanalizē frāzes tekstu un pie tā piekārtoto gramatiku.
			res.extractGramAndText(begin);
			// Analizē skaidrojumus.
			res.parseGramAndGloss(end, lemma, linePart);
		}
		// Nu nesanāca!
		else
		{
			System.out.printf("Neizdodas izanalizēt frāzi \"%s\" ar tipu \"%s\"",
					linePart, phraseType);
			if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
			System.out.println();
			res.text.add(linePart);
		}
		for (String variant : res.text)
			if (variant == null || variant.isEmpty()) res.text.remove(variant);

		if (res.text.isEmpty())
		{
			System.out.printf("Frāzes skaidrojumam \"%s\" nav neviena frāzes teksta",
					linePart);
			if (lemma != null) System.out.printf(" (lemma \"%s\")", lemma);
			System.out.println();
		}
		return res;
	}

	/**
	 * Metode, kas no dotā šķirkļa fragmenta izgūst vienu vai vairākas taxona
	 * tipa frāzes.
	 * @param linePart	šķirkļa teksta daļa, kas apraksta tikai taksona tipa
	 *                  frāzes un neko citu.
	 * @return izgūtā frāze vai null
	 */
	public static ArrayList<MLVVPhrase> parseTaxons(String linePart)
	{
		if (linePart == null) return null;
		linePart = linePart.trim();
		if (linePart.isEmpty()) return null;

		ArrayList<MLVVPhrase> results = new ArrayList<>();
		MLVVPhrase res = new MLVVPhrase();
		results.add(res);
		res.type = Type.TAXON;
		res.text = new LinkedList<>();
		res.sciName = new LinkedList<>();
		Matcher m = Pattern.compile("(?:<bullet/>)?\\s*<i>(.*?)</i>\\s*\\[([^\\]]*)\\](.*?)((?:<i>.*|<bullet/>.*)?)").matcher(linePart);
		if (m.matches())
		{
			res.text.add(Editors.removeCursive(m.group(1).trim()));
			String sciNames = Editors.removeCursive(m.group(2).trim());
			if (!sciNames.isEmpty())
				res.sciName.addAll(Arrays.asList(sciNames.split("\\s*[;,](\\s*arī)?\\s*")));
			String gloss = m.group(3).trim();
			if (gloss.equals(".")) gloss = null;
			else if (gloss.matches("[-\u2013\u2014]"))
			{
				System.out.printf("Taksonam \"%s\" sanāk tukša skaidrojošā daļa pēc domuzīmes\n", linePart);
				gloss = null;
			}
			else if (gloss.matches("[-\u2013\u2014].+"))
				gloss = gloss.substring(1).trim();
				// TODO: iespējams, ka te vajag brīdinājumu par trūkstošu domuzīmi.
			if (gloss != null && !gloss.isEmpty())
			{
				res.subsenses = new LinkedList<>();
				res.subsenses.add(new MLVVSense(MLVVGloss.parse(gloss.trim())));
			}
			// TODO vai uzskatāmāk nebūs bez rekursijas?
			if (m.group(4) != null && !m.group(4).isEmpty())
				results.addAll(parseTaxons(m.group(4)));
		}
		else
		{
			String resText = linePart.trim();
			if (resText.matches("<i>((?!</i>).)*</i>"))
				resText = resText.substring(3, resText.length()-4);
			res.text.add(Editors.removeCursive(resText));
			System.out.printf("Taksons \"%s\" neatbilst apstrādes šablonam\n", resText);
		}
		return results;

	}

	/**
	 * No apstrādājamās rindiņas daļas izvelk ar kolu vai kursīvu atdalītu
	 * gramatiku un vienu vai vairākus frāzes tekstus.
	 * Funkcija oriģināli izstrādāta sarežgītu frāžu (ar skaidrojumiem)
	 * analīzei.
	 * @param preDefiseLinePart	rindiņas daļa, kas tiek izmantota gramatikas un
	 *                          frāžu tekstu formēšanai.
	 */
	public void extractGramAndText(String preDefiseLinePart)
	{
		// Ja vajag, frāzi sadala.
		Pattern phrasesplitter = Pattern.compile("(?:(?<=</i>)[,;?!]|(?<=[,;?!]</i>)) (?:arī|biežāk|retāk)\\s*(?=<i>)");
		ArrayList<String> beginParts = new ArrayList<>();
		Matcher tmp = phrasesplitter.matcher(preDefiseLinePart);
		int whereToStart = 0;
		while (tmp.find(whereToStart))
		{
			String begin = preDefiseLinePart.substring(0, tmp.start());
			String end = preDefiseLinePart.substring(tmp.end());
			if (begin.matches(".*\\([^\\)]*"))
				whereToStart = tmp.end();
			else
			{
				beginParts.add(begin);
				preDefiseLinePart = end;
				whereToStart = 0;
				tmp = phrasesplitter.matcher(preDefiseLinePart);
			}
		}
		beginParts.add(preDefiseLinePart);
		//String[] beginParts = preDefiseLinePart.split("(?:(?<=</i>)[,?!]|(?<=[,;?!]</i>)) (?:arī|biežāk|retāk)\\s*(?=<i>)");
		String gramText = "";
		for (String part : beginParts)
		{
			Matcher endsWithCursive = Pattern.compile("(.*?)(<i>(?:(?!</i>).)*?</i>[.]?)\\s*").matcher(part);
			// Frāze pati ir kursīvā
			if (part.startsWith("<i>") || part.startsWith("(<i>"))
			{
				// ir ar kolu atdalītā gramatika
				if (part.contains(".: "))
				{
					if (gramText.length() > 0) gramText = gramText + "; ";
					gramText = (gramText + part.substring(0, part.indexOf(".: ") + 1)).trim();
					part = part.substring(part.indexOf(".: ") + 2).trim();
				} else if (part.contains(".</i>: <i>"))
				{
					if (gramText.length() > 0) gramText = gramText + "; ";
					gramText = (gramText + part.substring(0, part.indexOf(".</i>: <i>") + 1))
							.trim();
					part = part.substring(part.indexOf(".</i>: <i>") + ".</i>: <i>".length())
							.trim();
				}

				String newText = Editors.removeCursive(part);
				if (newText.endsWith(",")) newText = newText.substring(0, newText.length()-1);
				text.add(newText);
			}
			// Kursīvs sākas kaut kur vidū un iet līdz beigām - tātad kursīvā ir gramatika
			// Izņēmums "... <i>arī</i> ...)
			else if (endsWithCursive.matches())
			{
				text.add(Editors.removeCursive(endsWithCursive.group(1).trim()));
				if (gramText.length() > 0) gramText = gramText + "; ";
				gramText = (gramText + endsWithCursive.group(2)).trim();
			}
			// Frāzē nekas nav kursīvā - tātad tur ir tikai frāze bez problēmām.
			else text.add(Editors.removeCursive(part));
		}
		if (gramText.length() > 0) grammar = new MLVVGram(gramText);
	}

	/**
	 * No apstrādājamās rindiņas daļas izvelk ar kursīvu atdalītas gramatikas un
	 * vienu vai vairākas glosas.
	 * Funkcija oriģināli izstrādāta sarežgītu frāžu (ar skaidrojumiem)
	 * analīzei.
	 * @param postDefiseLinePart	rindiņas daļa, kas tiek izmantota gramatikas
	 *                              un frāžu tekstu formēšanai.
	 * @param lemma					reizēm vajag zināt šķirkļa lemmu.
	 */
	public void parseGramAndGloss(String postDefiseLinePart, String lemma,
			String linePartForErrorMsg)
	{
		// Ja te ir lielā gramatika un vairākas nozīmes, tad gramatiku piekārto
		// pie frāzes, nevis pirmās nozīmes.
		if (postDefiseLinePart.startsWith("</i>"))
			postDefiseLinePart = postDefiseLinePart.substring("</i>".length()).trim(); // ja defise nejauši ir kursīvā.
			//else if (end.startsWith("<i>") && end.contains(" b. "))
		else
		{
			Matcher endMatcher = Pattern
					.compile("(.*?)(?:</i> | </i>)(a\\.\\s.*?\\sb\\.\\s.*)")
					.matcher(postDefiseLinePart);
			if (endMatcher.matches())
			{
				String gram = endMatcher.group(1).trim();
				if (grammar == null)
				{
					if (gram.startsWith("<i>")) gram = gram.substring(3);
					grammar = new MLVVGram(gram);
					postDefiseLinePart = endMatcher.group(2).trim();
				} else
					System.out.printf(
							"Frāzē \"%s\" ir divas vispārīgās gramatikas, tāpēc nozīmes netiek sadalītas\n",
							linePartForErrorMsg);
			}
		}

		String[] subsenseTexts = new String[]{postDefiseLinePart};
		// Ir vairākas nozīmes.
		if (postDefiseLinePart.startsWith("a."))
		{
			subsenseTexts = postDefiseLinePart.split("\\s(?=[bcdefghijklmnop]\\.\\s)");
			// Ja skaidrojums ir formā "a. skaidrojums <i>piemēri b.</i> b. skaidrojums...",
			// tad var sanākt nepareizs dalījums dalot tā pa prasto.
			if (postDefiseLinePart.contains("<i>"))
			{
				LinkedList<String> betterGloses = new LinkedList<>();
				betterGloses.addLast(subsenseTexts[0]);
				for (int i = 1; i < subsenseTexts.length; i++)
				{
					String prev = betterGloses.getLast();
					if (prev.indexOf("<i>") > prev.indexOf("</i>"))
						betterGloses.addLast(
								betterGloses.removeLast() + " " +subsenseTexts[i]);
					else betterGloses.addLast(subsenseTexts[i]);
				}
				subsenseTexts = betterGloses.toArray(new String[betterGloses.size()]);
			}
			subsenseTexts = Arrays.stream(subsenseTexts)
					.map(s -> s.substring(2).trim())
					.toArray(String[]::new);
		}
		parseSubsenses(subsenseTexts, lemma);
	}

	/**
	 * No rindas fragmentu masīva izgūst apakšnozīmes - no katra fragmenta
	 * vienu.
	 * @param subsenseTexts	apstrādājamo rindas fragmentu masīvs.
	 * @param lemma			veidojot jaunu apakšnozīmi, reizēm vajag zināt
	 *                      šķirkļa lemmu.
	 */
	protected void parseSubsenses(String[] subsenseTexts, String lemma)
	{
		// Normalizē un apstrādā izgūtos apakšnozīmju fragmentus.
		subsenses = new LinkedList<>();
		for (String subsense : subsenseTexts)
		{
			if (subsense.contains("</i>") && !subsense.contains("<i>"))
				subsense = "<i>" + subsense;
			else if (subsense.contains("<i>") && !subsense.contains("</i>"))
				subsense = subsense + "</i>";
			subsenses.add(MLVVSense.parse(subsense, lemma));
		}
		// Ja frāzei ir vairākas nozīmes, tās sanumurē.
		enumerateGloses();
	}

	/**
	 * Ja frāzei ir vairākas nozīmes, tās sanumurē.
	 */
	protected void enumerateGloses()
	{
		if (subsenses != null && subsenses.size() > 1)
			for (int senseNumber = 0; senseNumber < subsenses.size(); senseNumber++)
			{
				Sense sense = subsenses.get(senseNumber);
				if (sense.ordNumber != null)
					System.out.printf(
							"Frāzē \"%s\" nozīme ar numuru \"%s\" tiek pārnumurēta par \"%s\"\n",
							text, sense.ordNumber, senseNumber + 1);
				sense.ordNumber = Integer.toString(senseNumber + 1);
			}
	}
	/**
	 * Remove empty variants.
	 */
	public void variantCleanup()
	{
		for (String variant : text)
			if (variant == null || variant.isEmpty()) text.remove(variant);
	}

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
	@Override
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		boolean hasPrev = false;

		if (type != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Type\":\"");
			res.append(JSONObject.escape(type.toString()));
			res.append("\"");
			hasPrev = true;
		}

		if (text != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Text\":[");
			res.append(text.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
					.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			res.append("]");
			hasPrev = true;
		}

		if (sciName != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"SciName\":[");
			res.append(sciName.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
					.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			res.append("]");
			hasPrev = true;
		}

		if (grammar != null)
		{
			if (hasPrev) res.append(", ");
			res.append(grammar.toJSON());
			hasPrev = true;
		}

		if (subsenses != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Senses\":");
			res.append(JSONUtils.objectsToJSON(subsenses));
			hasPrev = true;
		}

		if (source != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Source\":\"");
			res.append(JSONObject.escape(source));
			res.append("\"");
			hasPrev = true;
		}

		//res.append("}");
		return res.toString();
	}

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Element phraseN = doc.createElement("Phrase");
		if (type != null) phraseN.setAttribute("Type", type.toString());
		if (text != null)
		{
			Node textN = doc.createElement("Text");
			for (String var : text)
			{
				Node textVar = doc.createElement("Variant");
				textVar.appendChild(doc.createTextNode(var));
				textN.appendChild(textVar);
			}
			//textN.appendChild(doc.createTextNode(text));
			phraseN.appendChild(textN);
		}
		if (sciName != null)
		{
			Node textN = doc.createElement("SciName");
			for (String var : sciName)
			{
				Node textVar = doc.createElement("Variant");
				textVar.appendChild(doc.createTextNode(var));
				textN.appendChild(textVar);
			}
			//textN.appendChild(doc.createTextNode(text));
			phraseN.appendChild(textN);
		}
		if (grammar != null) grammar.toXML(phraseN);
		if (subsenses != null)
		{
			Node sensesContN = doc.createElement("Senses");
			for (Sense s : subsenses) s.toXML(sensesContN);
			phraseN.appendChild(sensesContN);
		}
		if (source != null)
		{
			Node sourceN = doc.createElement("Source");
			sourceN.setTextContent(source);
			phraseN.appendChild(sourceN);
		}
		parent.appendChild(phraseN);
	}
}
