package lv.ailab.dict.struct;

import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.LinkedList;

/**
 * Vārda skaidrojums (šobrīd tikai teksts).
 * @author Lauma
 */
public class Gloss implements HasToJSON, HasToXML
{
	/**
	 * Definīcijas teksts
	 */
	public LinkedList<String> text = null;

	public Gloss(String text)
	{
		this.text = new LinkedList<>();
		this.text.add(text);
	}

	/**
	 * Gloss ir viens no retajiem elementiem, ko drukā arī, ja tas ir tukšs.
	 * TODO vai tā ir pareizi?
	 */
	public String toJSON()
	{
		if (text == null)
			return "\"Gloss\":\"\"";
		//return String.format("\"Gloss\":\"%s\"", JSONObject.escape(text));
		StringBuilder res = new StringBuilder();
		res.append("\"Gloss\":[");
		res.append(text.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
				.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
		res.append("]");
		return res.toString();
	}

	/**
	 * Gloss ir viens no retajiem elementiem, ko drukā arī, ja tas ir tukšs.
	 * TODO vai tā ir pareizi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node glossN = doc.createElement("Gloss");
		//if (text != null) glossN.appendChild(doc.createTextNode(text));
		if (text != null)
		{
			for (String var : text)
			{
				Node glossVar = doc.createElement("Variant");
				glossVar.appendChild(doc.createTextNode(var));
				glossN.appendChild(glossVar);
			}
		}
		parent.appendChild(glossN);
	}
}