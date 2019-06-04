package lv.ailab.dict.io;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class DomIoUtils
{
	public static class FieldMapping
	{
		public HashMap<String, ArrayList<Node>> nodeChildren = null;
		public HashMap<String, ArrayList<String>> stringChildren = null;

		public boolean isEmpty()
		{
			return (nodeChildren == null || nodeChildren.isEmpty()) &&
					(stringChildren == null || stringChildren.isEmpty());
		}

		public HashSet<String> allKeys()
		{
			HashSet<String> result = new HashSet<>();
			if (nodeChildren != null) result.addAll(nodeChildren.keySet());
			if (stringChildren != null) result.addAll(stringChildren.keySet());
			if (result.isEmpty()) return null;
			return result;
		}

		public String keyStringForLog()
		{
			if (isEmpty()) return null;
			return allKeys().stream()
					.sorted().map(s -> "\"" + s + "\"")
					.reduce((s1, s2) -> s1 + ", " + s2).orElse(null);
		}
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
	public static FieldMapping domElemToHash (Element node)
	{
		if (node == null) return null;
		FieldMapping result = new FieldMapping();
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
	/**
	 * Vecākelementam salasa visus bērnus ar doto vārdu un to tekstuālo saturu
	 * savāc masīvā. Tiek sagaidīts, ka visi bērni ar vienādiem elementu vārdiem,
	 * atšķirības izraisa Exception. Tiek sagaidīts, ka tekstuālais saturs starp
	 * elementiem nav svarīgs.
	 * @param parent		elements, kura bērnus apstrādā
	 * @param elementName	pieļautais bērna elementa vārds
	 * @return	masīvs ar bērnu tekstuālo saturu
	 * @throws DictionaryXmlReadingException, ja tiek atrasts vecākelementa
	 * 		bērns ar vārdu, kas nav `elementName`.
	 */
	public static LinkedList<String> getPrimitiveArrayFromXml(Node parent, String elementName)
	throws DictionaryXmlReadingException
	{
		LinkedList<String> result = new LinkedList<>();
		NodeList fields = parent.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node child = fields.item(i);
			String childElemName = child.getNodeName();
			String elementText = child.getTextContent();
			if (elementText != null) elementText = elementText.trim();
			if (childElemName.equals(elementName))
			{
				if (elementText != null && !elementText.isEmpty())
					result.add(elementText);
			}
			else if (!childElemName.equals("#text")
					&& (elementText == null || elementText.isEmpty()))
				throw new DictionaryXmlReadingException(String.format(
						"Vietā, kur sagaida \"%s\" masīvu, atrasts elments \"%s\"!",
						elementName, childElemName));
		}
		if (result.isEmpty()) return null;
		return result;
	}

	/**
	 * Vecākelementam salasa visus dotā tipa bērnus un savāc masīvā. Tiek
	 * sagaidīts, ka visi bērni ar doto elementa vārdu, atšķirības izraisa
	 * Exception. Tiek sagaidīts, ka tekstuālais saturs starp elementiem ir
	 * tikai atstarpjojums.
	 * @param parent	elements, kura bērnus apstrādā
	 * @return	masīvs ar izgūtajiem elementiem
	 * @throws DictionaryXmlReadingException, ja tiek atrasts neatbilstošs
	 * 		vecākelementa bērns.
	 */
	public static LinkedList<Node> getSingleTypeNodeArrayFromXml(
			Node parent, String elementName)
	throws DictionaryXmlReadingException
	{
		LinkedList<Node> result = new LinkedList<>();
		NodeList fields = parent.getChildNodes();
		for (int j = 0; j < fields.getLength(); j++)
		{
			Node field = fields.item(j);
			String fieldName = field.getNodeName();
			if (fieldName.equals(elementName)) result.add(field);
			else if (!fieldName.equals("#text")
					&& (field.getTextContent() == null || field.getTextContent().trim().isEmpty()))
				throw new DictionaryXmlReadingException(String.format(
						"Vietā, kur sagaida \"%s\", atrasts elments \"%s\"!",
						elementName, fieldName));
		}
		if (result.isEmpty()) return null;
		return result;
	}

	/**
	 * Vecākelementam atrod vienīgo dotā tipa bērnu. Tiek sagaidīts, ka nav cita
	 * tipa bērnu, atšķirības izraisa Exception. Tiek sagaidīts, ka tekstuālais
	 * saturs starp elementiem ir tikai atstarpjojums.
	 * @param parent	elements, kura bērnus apstrādā
	 * @return	masīvs ar izgūtajiem elementiem
	 * @throws DictionaryXmlReadingException, ja tiek atrasts neatbilstošs
	 * 		vecākelementa bērns.
	 */
	public static Node getOnlyChildFromXml(
			Node parent, String elementName)
	throws DictionaryXmlReadingException
	{
		LinkedList<Node> result = getSingleTypeNodeArrayFromXml(parent, elementName);
		if (result == null || result.isEmpty()) return null;
		if (result.size() > 1)
			throw new DictionaryXmlReadingException(String.format(
					"Vietā, kur sagaida vienu \"%s\", atrasti %d!",
					elementName,result.size()));
		return result.getFirst();
	}

}
