package lv.ailab.dict.struct;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.DomIoUtils;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.JSONUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.LinkedList;

/**
 * Avotu saraksts.
 * @author Lauma
 */
public class Sources implements HasToJSON, HasToXML
{
	public String orig;
	public LinkedList<String> s;

	public Sources()
	{
		orig = null;
		s = null;
	}

	public boolean isEmpty()
	{
		return s == null || s.isEmpty();
	}

	// In case of speed problems StringBuilder can be returned.
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		if (s != null && s.size() > 0)
		{
			res.append("\"Sources\":");
			res.append(JSONUtils.simplesToJSON(s));
		}
		return res.toString();
	}

	/**
	 * Oriģināltekstu neiekļauj.
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (s != null && s.size() > 0)
		{
			Node containerN = doc.createElement("Sources");
			for (String source : s)
			{
				Node sourceN = doc.createElement("Source");
				sourceN.appendChild(doc.createTextNode(source));
				containerN.appendChild(sourceN);
			}
			parent.appendChild(containerN);
		}
	}

	public static Sources fromStdXML(Node sourcesNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Sources result = elemFact.getNewSources();
		result.s = DomIoUtils.getPrimitiveArrayFromXml(sourcesNode, "Source");
		if (result.s != null && result.s.isEmpty()) return null;
		return result;
	}
}