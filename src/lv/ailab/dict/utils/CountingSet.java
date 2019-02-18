package lv.ailab.dict.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Kopa, kas skaita, cik reižu atsēga ir tikusi pievienota.
 * Created on 2015-10-23.
 *
 * @author Lauma
 */
public class CountingSet <K>
{
	protected HashMap<K, Integer> map = new HashMap<>();

	public void add(K k)
	{
		if (map.containsKey(k))
			map.put(k, map.get(k) + 1);
		else map.put(k, 1);
	}

	public void addAll(Collection<K> data)
	{
		for (K k : data)
			add(k);
	}

	public void addAll (CountingSet<K> data)
	{
		for (K k : data.map.keySet())
			if (map.containsKey(k))
				map.put(k, map.get(k) + data.map.get(k));
			else map.put(k, data.map.get(k));
	}

	public Integer getCount (K key)
	{
		return map.getOrDefault(key, 0);
	}

	public HashMap getCounts()
	{
		return map;
	}

	public Stream<K> keyStream()
	{
		return map.keySet().stream();
	}
}
