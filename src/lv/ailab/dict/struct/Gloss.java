package lv.ailab.dict.struct;

import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Vārda skaidrojums (šobrīd tikai teksts).
 * @author Lauma
 */
public class Gloss implements HasToJSON, HasToXML
{
	/**
	 * Definīcijas teksts
	 */
	public String text = null;

	/**
	 * Papildu gramatiskās norādes, reti lietota lieta.
	 * TODO vienādot lauku nosakumus - citur ir gram
	 */
	public Gram grammar = null;


	public Gloss(String text)
	{
		this.text = text;
	}

	protected  Gloss() {};

	/**
	 * Gloss ir viens no retajiem elementiem, ko drukā arī, ja tas ir tukšs.
	 * TODO vai tā ir pareizi?
	 */
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		res.append("\"GlossText\":\"");
		if (text != null)
			res.append(JSONObject.escape(text));
		res.append("\"");
		if (grammar != null)
		{
			res.append(", ");
			res.append(grammar.toJSON());
		}
		return res.toString();
	}

	/**
	 * Gloss ir viens no retajiem elementiem, ko drukā arī, ja tas ir tukšs.
	 * TODO vai tā ir pareizi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node glossVariantN = doc.createElement("GlossVariant");
		Node glossN = doc.createElement("GlossText");
		if (text != null) glossN.appendChild(doc.createTextNode(text));
		if (grammar != null) grammar.toXML(glossVariantN);
		glossVariantN.appendChild(glossN);
		parent.appendChild(glossVariantN);
	}
}