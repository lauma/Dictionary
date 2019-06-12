package lv.ailab.dict.io.stdxml;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Palīgstruktūra, kas dod iespēju XML DOM fragmentu attēlot kā kartējumu no
 * elementu vārdiem uz atbilštošajiem Node elementiem.
 */
public class XmlFieldMapping
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
