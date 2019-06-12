package lv.ailab.dict.io;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

public class DomIoUtils
{
	/**
	 * Vecākelementam salasa visus bērnus ar doto vārdu un to tekstuālo saturu
	 * savāc masīvā. Tiek sagaidīts, ka visi bērni ar vienādiem elementu vārdiem,
	 * atšķirības izraisa Exception. Tiek sagaidīts, ka tekstuālais saturs starp
	 * elementiem nav svarīgs.
	 * @param parent		elements, kura bērnus apstrādā
	 * @param elementName	pieļautais bērna elementa vārds
	 * @return	masīvs ar bērnu tekstuālo saturu
	 * @throws DictionaryXmlReadingException, ja tiek atrasts vecākelementa
	 * 			bērns ar vārdu, kas nav `elementName`.
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
