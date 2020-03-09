package lv.ailab.dict.struct;

import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Ierobežojumi par lietošanu noteiktās formās vai sintaktiskajās struktūrās.
 * @author Lauma
 */
public class StructRestriction implements HasToJSON, HasToXML
{
	public String type;
	public String frequency;
	public String valueText;
	public Flags valueFlags;

	/**
	 * Atdod tikai to, kas būs JSON objektā iekšā - bez ietverošajām { }
	 * un bez elementa nosaukuma.
	 */
	@Override
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();

		//res.append("\"StructuralRestriction\":{");
		boolean hasPrev = false;

		if (type != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Restriction\":\"");
			res.append(JSONObject.escape(type));
			res.append("\"");
			hasPrev = true;
		}

		if (frequency != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Frequency\":\"");
			res.append(JSONObject.escape(frequency));
			res.append("\"");
			hasPrev = true;
		}

		if (valueText != null || valueFlags != null && !valueFlags.pairings.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Value\":{");

			if (valueFlags != null && !valueFlags.pairings.isEmpty())
			{
				res.append("\"Flags\":");
				res.append(JSONUtils.mappingSetToJSON(valueFlags.pairings));
			}
			if (valueText != null && valueFlags != null && !valueFlags.pairings.isEmpty())
				res.append(", ");
			if (valueText != null)
			{
				res.append("\"LanguageMaterial\":\"");
				res.append(JSONObject.escape(valueText));
				res.append("\"");
			}

			res.append("}");
			hasPrev = true;
		}

		return res.toString();
	}

	/**
	 * Attiecīgā klase prot visu būtisko saturu pārtasīt par DOM nodēm un
	 * sakabināt zem dotās vecāknodes.
	 *
	 * @param parent
	 */
	@Override
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Element trN = doc.createElement("StructuralRestriction");

		if (type != null)
		{
			Node typeN = doc.createElement("Restriction");
			typeN.appendChild(doc.createTextNode(type));
			trN.appendChild(typeN);
		}

		if (frequency != null)
		{
			Node freqN = doc.createElement("Frequency");
			freqN.appendChild(doc.createTextNode(frequency));
			trN.appendChild(freqN);
		}

		if (valueText != null || valueFlags != null && !valueFlags.pairings.isEmpty())
		{
			Node valueN = doc.createElement("Value");

			if (valueFlags != null && !valueFlags.pairings.isEmpty())
				valueFlags.toXML(valueN);

			if (valueText != null)
			{
				Node langN = doc.createElement("LanguageMaterial");
				langN.appendChild(doc.createTextNode(valueText));
				trN.appendChild(langN);
			}
			trN.appendChild(valueN);
		}

		parent.appendChild(trN);
	}
}


