package lv.ailab.dict.io.stdxml;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.DomIoUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		NodeList fields = node.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node child = fields.item(i);
			String childElemName = child.getNodeName();
			/*if (childElemName.equals("#text"))
			{
				String text = node.getTextContent();
				if (text != null) text = text.trim();
				if (text != null && !text.isEmpty())
				{
					if (stringChildren.containsKey("#text"))
						text = stringChildren.get("#text") + " " + text;
					stringChildren.put("#text", text);
				}
			}
			else*/
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
					ArrayList<String> temp = result.stringChildren.get(childElemName);
					if (temp == null) temp = new ArrayList<>();
					String text = child.getTextContent();
					if (text != null) text = text.trim();
					if (text != null && !text.isEmpty())
					{
						temp.add(child.getTextContent());
						result.stringChildren.put(childElemName, temp);
					}
				}
			}
		}
		if (result.isEmpty()) return null;
		if (result.stringChildren.isEmpty()) result.stringChildren = null;
		if (result.nodeChildren.isEmpty()) result.nodeChildren = null;
		return result;
	}

	public String getSinglarStringField(
			XmlFieldMapping fields, String parentElemName, String elemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<String> fieldTexts = fields.stringChildren.remove(elemName);
		if (fieldTexts != null && fieldTexts.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasti vairāki \"%s\"!",
					parentElemName, elemName));
		if (fieldTexts!= null && !fieldTexts.isEmpty())
			return fieldTexts.get(0);
		return null;
	}

	public LinkedList<String> getStringFieldArray(
			XmlFieldMapping fields, String parentElemName,
			String elemName, String childElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> nodesOfType = fields.nodeChildren.remove(elemName);
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

	public LinkedList<Node> getNodeList(
			XmlFieldMapping fields, String parentElemName, String elemName,
			String childElemName)
	throws DictionaryXmlReadingException
	{
		ArrayList<Node> containerNode = fields.nodeChildren.remove(elemName);
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
