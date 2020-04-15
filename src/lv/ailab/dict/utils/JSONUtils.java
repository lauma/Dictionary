package lv.ailab.dict.utils;

import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class JSONUtils
{
	public static <E extends HasToJSON> String objectsToJSON(Collection<E> c)
	{
		return objectsToJSON(c, null);
	}

	public static <E extends HasToJSON> String objectsToJSON(Collection<E> c, Comparator<E> sorter)
	{
		if (c == null) return "[]";
		StringBuilder res = new StringBuilder();
		res.append("[");
		Iterator<E> i =  sorter == null ? c.iterator() :
			c.stream().sorted(sorter).iterator();

		while (i.hasNext())
		{
			res.append("{");
			res.append(i.next().toJSON());
			res.append("}");
			if (i.hasNext()) res.append(", ");
		}
		res.append("]");			
		return res.toString();
	}
	
	public static<E> String simplesToJSON(Iterable<E> l)
	{
		if (l == null) return "[]";
		StringBuilder res = new StringBuilder();
		res.append("[");
		Iterator<E> i = l.iterator();
		while (i.hasNext())
		{
			res.append("\"");
			res.append(JSONObject.escape(i.next().toString()));
			res.append("\"");
			if (i.hasNext()) res.append(", ");
		}
		res.append("]");			
		return res.toString();
	}

	public static<K, V> String mappingSetToJSON(MappingSet<K, V> l)
	{
		if (l == null) return "{}";
		StringBuilder res = new StringBuilder();
		res.append("{");
		Iterator<K> i = new TreeSet<>(l.keySet()).iterator();
		while (i.hasNext())
		{
			K key = i.next();
			res.append("\"");
			res.append(JSONObject.escape(key.toString()));
			res.append("\":");
			res.append(JSONUtils.simplesToJSON(l.getAll(key).stream().sorted().collect(Collectors.toList())));
			if (i.hasNext()) res.append(", ");
		}
		res.append("}");
		return res.toString();
	}
	
}