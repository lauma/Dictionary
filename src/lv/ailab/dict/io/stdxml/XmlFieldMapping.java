package lv.ailab.dict.io.stdxml;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Palīgstruktūra, kas dod iespēju XML DOM fragmentu attēlot kā kartējumu no
 * elementu vārdiem uz atbilštošajiem Node elementiem.
 */
public class XmlFieldMapping
{
	protected HashMap<String, LinkedList<Node>> nodeChildren = null;
	protected HashMap<String, LinkedList<String>> stringChildren = null;

	public boolean isEmpty()
	{
		return (nodeChildren == null || nodeChildren.isEmpty()) &&
				(stringChildren == null || stringChildren.isEmpty());
	}

	public LinkedList<Node> removeNodeChildren(String key)
	{
		if (nodeChildren == null) return null;
		return nodeChildren.remove(key);
	}

	public LinkedList<String> removeStringChildren(String key)
	{
		if (stringChildren == null) return null;
		return stringChildren.remove(key);
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

	/**
	 * Standarta brīdinājums, ko lieto, lai pateiktu, ka pēc apstrādes
	 * XmlFieldMapping struktūrā ir palicis kas neapstrādāts (neizņemts).
	 * @param parentElemName	elementa vārds, ko izmantot kļūdas paziņojumā
	 */
	public void dieOnNonempty(String parentElemName)
	throws DictionaryXmlReadingException
	{
		if (!isEmpty())
			throw new DictionaryXmlReadingException(String.format(
					"Elementā \"%s\" atrasts neatpazina \"%s\"!", parentElemName,
					keyStringForLog()));
	}
}
