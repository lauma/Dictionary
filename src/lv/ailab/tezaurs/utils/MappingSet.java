/*******************************************************************************
 * Copyright 2013, 2014 Institute of Mathematics and Computer Science, University of Latvia
 * Author: Lauma Pretkalniņa
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package lv.ailab.tezaurs.utils;

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
		HashSet<V> values = new HashSet<V>();
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
		HashSet<V> valuesInPlace = new HashSet<V>();
		if (map.containsKey(key)) valuesInPlace = map.get(key);
		valuesInPlace.addAll(values);
		map.put(key, valuesInPlace);
	}

	public void putAll(MappingSet<K, V> data)
	{
		for (K key: data.keySet())
			putAll(key, data.getAll(key));
	}
}