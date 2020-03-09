package lv.ailab.dict.struct;

import lv.ailab.dict.utils.GramPronuncNormalizer;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Gramatikas lauks.
 * @author Lauma
 */
public class Gram implements HasToJSON, HasToXML
{

	/**
	 * Brīvs gramatikas teksts, kāds tas ir vārdnīcā.
	 * Tēzaura gadījumā tā ir pilna analizējamās gramatikas kopija, MLVV
	 * gadījumā - oficiāli nestrukturējamais.
	 */
	public String freeText;
	/**
	 * No gramatikas izgūtie karodziņi.
	 */
	public Flags flags;

	/**
	 * No šīs gramatikas izsecinātās paradigmas.
	 */
	public HashSet<Integer> paradigm;
	/**
	 * Struktūra, kurā tiek savākta papildus informāciju par citiem šķirkļu
	 * vārdiem / pamatformām (kodā daudzviet saukti par alternatīvajām lemmām),
	 * ja gramatika tādu satur.
	 * Karodziņu kopa satur vienīgi tos karodziņus, kas "alternatīvajai lemmai"
	 * atšķiras no pamata lemmas.
	 * TODO: filtrēt vienādos.
	 * TODO: dublēt karodziņus.
	 */
	public LinkedList<Header> altLemmas;
	
	protected Gram() {};

	/**
	 * Atlasa tās paradigmas, kas ir tiešā veidā attiecināmas uz to objektu,
	 * kam šī gramatika ir piekārtota.
	 */
	public HashSet<Integer> getDirectParadigms()
	{
		HashSet<Integer> res = new HashSet<>();
		if (paradigm != null) res.addAll(paradigm);
		return res;
	}

	/**
	 * Visas paradigmas, kas vispār ir pieminētas šajā struktūrā.
	 * Izmantojams meklēšanai un statistikai.
	 */
	public HashSet<Integer> getMentionedParadigms()
	{
		HashSet<Integer> res = new HashSet<>();
		res.addAll(getDirectParadigms());
		for (Header h : getImplicitHeaders())
			res.addAll(h.getMentionedParadigms());
		return res;
	}

	/**
	 * Savāc altLemmas un, drošības pēc, arī pārbauda vai altLemmu objekti sevī
	 * neietver kādu gramatiku ar altLemmu. Teorētiski tā gan nevajadzētu būt,
	 * bet nu drošības pēc.
	 * TODO: vai šeit vajag iekļaut arī formRestrictions?
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList();
		if (altLemmas != null)
		{
			res.addAll(altLemmas);
			for (Header h : altLemmas)
				res.addAll(h.getImplicitHeaders());
		}
		return res;
	}

	/**
	 * Gramatikas struktūras JSON reprezentācija, kas iekļauj arī sākotnējo
	 * gramatikas tekstu.
	 */
	public String toJSON()
	{
		return toJSON(null, null, null);
	}

	/**
	 * Reālā JSON reprezentācijas izveides metode, ko konkrētais objekts sauc,
	 * dažus parametrus aizpildot automātiski.
	 * Ātruma problēmu gadījumā, iespējams, jāpāriet uz StringBuilder
	 * atgriešanu.
	 * @param freeTextFieldName		freeText lauka nosaukums
	 * @param addBeforeFT    JSON-noformēts atslēgu vērtību pārītis, ko
	 *                       		pievienotizdrukā pēc freeText lauka
	 * @param addAfterFT		JSON-noformēts atslēgu vērtību pārītis, ko
	 *                      		pievienotizdrukā pēc freeText lauka
	 */
	public String toJSON (String freeTextFieldName, String addBeforeFT, String addAfterFT)
	{
		StringBuilder res = new StringBuilder();

		res.append("\"Gram\":{");
		boolean hasPrev = false;

		if (paradigm != null && !paradigm.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Paradigms\":");
			res.append(JSONUtils.simplesToJSON(paradigm));
			hasPrev = true;
		}

		if (altLemmas != null && !altLemmas.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"AltLemmas\":");
			res.append(JSONUtils.objectsToJSON(altLemmas));
			hasPrev = true;
		}

		if (addBeforeFT != null && addBeforeFT.length() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append(addBeforeFT);
			hasPrev = true;
		}

		if (flags != null && !flags.pairings.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Flags\":");
			res.append(JSONUtils.mappingSetToJSON(flags.pairings));
			hasPrev = true;
		}

		if (freeText != null && freeText.length() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append("\"");
			if (freeTextFieldName == null || freeTextFieldName.trim().isEmpty())
				res.append("FreeText");
			else res.append(JSONObject.escape(freeTextFieldName));
			res.append("\":\"");
			res.append(JSONObject.escape(freeText));
			res.append("\"");
			hasPrev = true;
		}

		if (addAfterFT != null && addAfterFT.length() > 0)
		{
			if (hasPrev) res.append(", ");
			res.append(addAfterFT);
			hasPrev = true;
		}

		res.append("}");
		return res.toString();
	}

	public void toXML(Node parent)
	{
		toXML(parent, null, null, null);
	}

	/**
	 * Reālā XML izveidošanas metode, ko konkrētais obekts sauc, dažus
	 * parametrus aizpildot automatiski.
 	 * @param freeTextFieldName	freeText lauka nosaukums
	 * @param addBeforeFT	XML node ar papildus informāciju, ko pievienot pirms
	 *                      freeText lauka.
	 * @param addAfterFT	XML node ar papildus informāciju, ko pievienot pēc
	 *                      freeText lauka.
	 */

	public void toXML(Node parent, String freeTextFieldName, Node addBeforeFT, Node addAfterFT)
	{
		Document doc = parent.getOwnerDocument();
		Node gramN = doc.createElement("Gram");

		if (paradigm != null && !paradigm.isEmpty())
		{
			Node paradigmContN = doc.createElement("Paradigms");
			for (Integer p : paradigm.stream().sorted().collect(Collectors.toList()))
			{
				Node paradigmN = doc.createElement("Paradigm");
				paradigmN.appendChild(doc.createTextNode(p.toString()));
				paradigmContN.appendChild(paradigmN);
			}
			gramN.appendChild(paradigmContN);
		}

		if (altLemmas != null && !altLemmas.isEmpty())
		{
			Node altLemmasContN = doc.createElement("AltLemmas");
			for (Header al: altLemmas)
				if (al != null) al.toXML(altLemmasContN); // TODO MLVV kā te var nonākt null?
			gramN.appendChild(altLemmasContN);
		}

		if (addBeforeFT != null)
			gramN.appendChild(addBeforeFT);

		if (flags != null) flags.toXML(gramN);

		if (freeText != null && !freeText.isEmpty())
		{
			Node origN;
			if (freeTextFieldName == null || freeTextFieldName.trim().isEmpty())
				origN = doc.createElement("FreeText");
			else origN = doc.createElement(freeTextFieldName);
			origN.appendChild(doc.createTextNode(freeText));
			gramN.appendChild(origN);
		}
		if (addAfterFT != null)
			gramN.appendChild(addAfterFT);

		parent.appendChild(gramN);
	}

	public static String normalizePronunc(
			String text, GramPronuncNormalizer normalizer)
	{
		if (text == null || text.isEmpty()) return text;
		// Apstrāde notiek, gramatiku apstrādājot pa gabaliņam un apstrādātos
		// gabaliņus pārceļot uz rezultāta mainīgo.
		StringBuilder processed = new StringBuilder();
		String toProcess = text;
		String currentPronunc = "";
		// Katrai izrunai viena cikla iterācija.
		while (!toProcess.isEmpty() && toProcess.contains("["))
		{
			// Pārceļ nerediģējamo gramatikas daļu pirms izrunas.
			processed.append(toProcess.substring(0, toProcess.indexOf("[") + 1));
			toProcess = toProcess.substring(toProcess.indexOf("[") + 1);
			// Izdala izrunu.
			if (toProcess.contains("]"))
			{
				currentPronunc = toProcess.substring(0, toProcess.indexOf("]"));
				toProcess = toProcess.substring(toProcess.indexOf("]"));
			}
			else
			{
				System.err.printf("\'gram\' \"%s\" satur trūkst \']\'\n", text);
				currentPronunc = toProcess;
				toProcess = "";
			}
			if (currentPronunc.contains("["))
				System.err.printf("\'gram\' \"%s\" ir par daudz \'[\'\n", text);
			// Pārveido izrunu un pārceļ to uz apstrādāto.
			processed.append(normalizer.normalizePronuncs(currentPronunc));
			// Pārceļ uz apstrādāto izrunas kvadrātiekavu.
			if (toProcess.startsWith("]"))
			{
				processed.append("]");
				toProcess = toProcess.substring(1);
			}
		}
		// Pārceļ uz apstrādāto to, kas palicis aiz pēdējās izrunas.
		if (toProcess.contains("]"))
			System.err.printf("\'gram\' \"%s\" ir par daudz \']\'\n", text);
		processed.append(toProcess);
		// Noliek pie vietas apstrādāto.
		return processed.toString();
	}
}

