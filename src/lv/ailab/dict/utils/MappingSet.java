package lv.ailab.dict.utils;

import java.util.*;

/**
 * Limited use multimap. Incomplete interface, might need additional
 * methods later.
 */
public class MappingSet<K, V>
{
	protected HashMap<K, HashSet<V>> map = new HashMap<>();
	
	public void put (K key, V value)
	{
		HashSet<V> values = new HashSet<>();
		if (map.containsKey(key)) values = map.get(key);
		values.add(value);
		map.put(key, values);
	}

	/**
	 * TODO šo aizstāt ar Iterable interfeisa realizāciju.
	 */
	public ArrayList<Tuple<K, V>> asList()
	{
		ArrayList<Tuple<K, V>> res = new ArrayList<>();
		for (K k : map.keySet())
			for (V v : map.get(k))
				res.add(Tuple.of(k, v));
		return res;
	}
	
	public HashSet<V> getAll(K key)
	{
		return map.get(key);
	}
	
	public boolean containsKey(K key)
	{
		return map.containsKey(key);
	}
	
	public boolean isEmpty()
	{
		return map.isEmpty();
	}
	
	public Set<K> keySet()
	{
		return map.keySet();
	}

	public void putAll(Map<K, V> data)
	{
		for (K key : data.keySet())
			put(key, data.get(key));
	}

	public void putAll(K key, Collection<V> values)
	{
		HashSet<V> valuesInPlace = new HashSet<>();
		if (map.containsKey(key)) valuesInPlace = map.get(key);
		valuesInPlace.addAll(values);
		map.put(key, valuesInPlace);
	}

	public void putAll(MappingSet<K, V> data)
	{
		for (K key: data.keySet())
			putAll(key, data.getAll(key));
	}

	public HashSet<V> removeAll(K key)
	{
		return map.remove(key);
	}

	public boolean remove(K key, V value)
	{
		if (!map.containsKey(key)) return false;
		if (!map.get(key).contains(value)) return false;
		boolean returnValue = map.get(key).remove(value);
		if (map.get(key).isEmpty()) map.remove(key);
		return returnValue;
	}

}