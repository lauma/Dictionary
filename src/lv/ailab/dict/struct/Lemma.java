package lv.ailab.dict.struct;

import lv.ailab.dict.utils.GramPronuncNormalizer;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * Vārdforma (vf Tēzaura XML).
 * @author Lauma
 */
public class Lemma implements HasToJSON, HasToXML
{
	public String text;
	/**
	 * ru (runa) field, optional here.
	 */
	public String[] pronunciation;

	protected Lemma() {};

	public Lemma(String lemma, String[] pronunciation)
	{
		text = lemma;
		this.pronunciation = pronunciation;
	}
	/**
	 *  Uzstāda lemmu un pārbauda, vai tā jau nav bijusi uzstādīta, par to
	 *  pabrīdinot.
	 */
	public void set(String lemmaText) {
		if (text != null)
			System.err.printf(
					"Duplicēts saturs laukam 'lemma' : '%s' un '%s'", text,
					lemmaText);
		text = lemmaText;
	}

	// Šo vajag, lai lemmas liktu hash struktūrās (hasmap, hashset).
	@Override
	public boolean equals (Object o)
	{
		if (o == null) return false;
		if (this.getClass() != o.getClass()) return false;
		return (text == null && ((Lemma) o).text == null ||
				text != null && text.equals(((Lemma) o).text))
				&& (pronunciation == null && ((Lemma) o).pronunciation == null
				|| pronunciation != null && pronunciation.equals(((Lemma) o).pronunciation));
	}

	// Šo vajag, lai lemmas liktu hash struktūrās (hasmap, hashset).
	@Override
	public int hashCode()
	{
		return 1721 *(text == null ? 1 : text.hashCode())
				+ (pronunciation == null ? 1 : pronunciation.hashCode());
	}

	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		res.append(String.format("\"Lemma\":\"%s\"", JSONObject.escape(text)));
		if (pronunciation != null && pronunciation.length > 0)
		{
			res.append(", \"Pronunciations\":[\"");
			ArrayList<String> escaped = new ArrayList<>();
			for (String pron : pronunciation)
				escaped.add(JSONObject.escape(pron));
			res.append(String.join("\", \"", escaped));
			res.append("\"]");
		}
		return res.toString();
	}

	/**
	 * Izrunas un lemma ir atsevišķi elementi.
	 * TODO vai tā ir labi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node lemmaN = doc.createElement("Lemma");
		lemmaN.appendChild(doc.createTextNode(text));
		parent.appendChild(lemmaN);
		if (pronunciation != null && pronunciation.length > 0)
		{
			Node pronContN = doc.createElement("Pronunciations");
			for (String pron : pronunciation)
			{
				Node pronN = doc.createElement("Pronunciation");
				pronN.appendChild(doc.createTextNode(pron));
				pronContN.appendChild(pronN);
			}
			parent.appendChild(pronContN);
		}

	}

	/**
	 * Izkasa izrunas no klasiskās vārdnīcas formāta teksta:
	 * <code>[izruna, arī izruna, izruna]</code>
	 * @param pronsFromLegacyDict	izrunu teksts formā <code>[izruna, arī izruna, izruna]</code>
	 * @param normalizer			normalizators, ko pielietot uz izrunu
	 *                              tekstiem vai null, ja normalizēt nevajag.
	 */
	public void setPronunciation(
			String pronsFromLegacyDict, GramPronuncNormalizer normalizer)
	{
		if ("".equals(pronsFromLegacyDict)) return;
		pronunciation = pronsFromLegacyDict.trim().split(
				"(, arī | vai |, (?!arī ))");
		for (int i = 0; i < pronunciation.length; i++)
		{
			pronunciation[i] = pronunciation[i].trim();
			if (pronunciation[i].startsWith("["))
				pronunciation[i] = pronunciation[i].substring(1);
			if (pronunciation[i].endsWith("]"))
				pronunciation[i] = pronunciation[i].substring(0, pronunciation[i].length()-1);
			if (normalizer != null)
				pronunciation[i] = normalizer.normalizePronuncs(pronunciation[i]);
		}
	}
}