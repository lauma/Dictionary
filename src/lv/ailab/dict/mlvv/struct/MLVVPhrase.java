package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.TreeSet;

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

	protected MLVVPhrase(){};
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
	 * Ja frāzei ir vairākas nozīmes, tās sanumurē.
	 */
	public void enumerateGloses()
	{
		if (subsenses != null && subsenses.size() > 1)
			for (int senseNumber = 0; senseNumber < subsenses.size(); senseNumber++)
			{
				Sense sense = subsenses.get(senseNumber);
				String newNumber = Integer.toString(senseNumber + 1);
				if (sense.ordNumber != null && !sense.ordNumber.equals(newNumber))
					System.out.printf(
							"Frāzē \"%s\" nozīme ar numuru \"%s\" tiek pārnumurēta par \"%s\"\n",
							text, sense.ordNumber, newNumber);
				sense.ordNumber = newNumber;
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
		parent.appendChild(phraseN);
	}
}
