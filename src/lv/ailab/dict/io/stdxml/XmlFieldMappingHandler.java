package lv.ailab.dict.io.stdxml;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.DomIoUtils;
import org.w3c.dom.*;

import java.util.*;

public class XmlFieldMappingHandler
{
	protected static XmlFieldMappingHandler singleton = new XmlFieldMappingHandler();

	public static XmlFieldMappingHandler me()
	{
		return singleton;
	}

	/**
	 * No DOM Element izgūst kartējumu no lauka vārdiem uz lauka saturu.
	 * Laukiem, kuru saturs ir tikai teksts, izgūst tekstu, laukiem, kas paši ir
	 * elementi ar bērniem - Node objektu.
	 * @param node	apstrādājamais XML elements
	 * @return divi kartējumi - viens no tekstuālo lauku vārdiem uz to vērtībām
	 * (String), otrs - no elementu, kam ir bērni, lauku vārdiem uz atbilstošo
	 * elementu objektiem (Node); tukšiem elementiem rezultāts ir null; katrs no
	 * kartējumiem arī var būt null.
	 */
	public XmlFieldMapping domElemToHash (Element node)
	{
		if (node == null) return null;
		XmlFieldMapping result = new XmlFieldMapping();
		result.nodeChildren = new HashMap<>();
		result.stringChildren = new HashMap<>();

		// Bērnelementi.
		NodeList fields = node.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node child = fields.item(i);
			String childElemName = child.getNodeName();
			if (child.getChildNodes().getLength() > 0)
			{
				boolean hasElementChild = false;
				NodeList grandchildren = child.getChildNodes();
				for (int j = 0; j < grandchildren.getLength(); j++)
					if (grandchildren.item(j).getNodeType() == Node.ELEMENT_NODE)
					{
						hasElementChild = true;
						break;
					}

				if (hasElementChild)
				{
					ArrayList<Node> temp = result.nodeChildren.get(childElemName);
					if (temp == null) temp = new ArrayList<>();
					temp.add(child);
					result.nodeChildren.put(childElemName, temp);
				}
				else
				{
					String text = child.getTextContent();
					if (text != null) text = text.trim();
					if (text != null && !text.isEmpty())
					{
						ArrayList<String> temp = result.stringChildren.get(childElemName);
						if (temp == null) temp = new ArrayList<>();
						temp.add(child.getTextContent());
						result.stringChildren.put(childElemName, temp);
					}
				}
			}
		}

		// Atribūti.
		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++)
		{
			Attr attr = (Attr) attributes.item(i);
			String attrName = attr.getNodeName();
			String attrVal = attr.getNodeValue();
			if (attrVal != null) attrVal = attrVal.trim();
			if (attrVal != null && !attrVal.isEmpty())
			{
				ArrayList<String> temp = result.stringChildren.get(attrName);
				if (temp == null) temp = new ArrayList<>();
				temp.add(attr.getNodeValue());
				result.stringChildren.put(attrName, temp);
			}
		}

		// Pēcapstrāde.
		if (result.isEmpty()) return null;
		if (result.stringChildren.isEmpty()) result.stringChildren = null;
		if (result.nodeChildren.isEmpty()) result.nodeChildren = null;
		return result;
	}

	public String takeoutSinglarStringField(
			XmlFieldMapping fields, String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<String> fieldTexts = fields.removeStringChildren(elemName);
		if (fieldTexts != null && fieldTexts.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!",
					parentElemName, elemName));
		if (fieldTexts!= null && !fieldTexts.isEmpty())
			return fieldTexts.get(0);
		return null;
	}

	public LinkedList<String> takeoutStringFieldArray(
			XmlFieldMapping fields, String parentElemName,
			String elemName, String childElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> nodesOfType = fields.removeNodeChildren(elemName);
		if (nodesOfType != null && nodesOfType.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!",
					parentElemName, elemName));
		if (nodesOfType != null && !nodesOfType.isEmpty())
		{
			LinkedList<String> nodeContents = DomIoUtils.getPrimitiveArrayFromXml(
					nodesOfType.get(0), childElemName);
			if (nodeContents != null && nodeContents.size() > 0)
				return nodeContents;
			return null;
		}
		return null;
	}

	public LinkedList<Node> takeoutNodeList(
			XmlFieldMapping fields, String parentElemName, String elemName,
			String childElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> containerNode = fields.removeNodeChildren(elemName);
		if (containerNode != null && containerNode.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!", parentElemName, elemName));
		if (containerNode!= null && !containerNode.isEmpty())
		{
			LinkedList<Node> resultNodes = DomIoUtils.getSingleTypeNodeArrayFromXml(
					containerNode.get(0), childElemName);
			if (resultNodes == null) return null;
			resultNodes.removeAll(Collections.singleton(null));
			if (resultNodes.isEmpty()) return null;
			return resultNodes;
		}
		return null;
	}

}
