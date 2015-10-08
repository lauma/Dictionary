package lv.ailab.tezaurs.analyzer.struct;

import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.utils.MappingSet;

import java.util.HashSet;

/**
 * Datu struktūra karodziņu uzturēšanai.
 * Created on 2015-10-08.
 * @author Lauma
 */
public class Flags
{
	public MappingSet<Keys, String> pairings;

	public Flags()
	{
		pairings = new MappingSet<>();
	}

	public void addAll(Flags others)
	{
		if (others.pairings != null)
			pairings.putAll(others.pairings);
	}

	public void add(Keys key, String value)
	{
		pairings.put(key, value);
	}
	public void add(Keys key, Values value)
	{
		pairings.put(key, value.s);
	}
	public void add(String value)
	{
		pairings.put(Keys.OTHER_FLAGS, value);
	}
	public void add(Values value)
	{
		pairings.put(Keys.OTHER_FLAGS, value.s);
	}

	public HashSet<String> binaryFlags()
	{
		return pairings.getAll(Keys.OTHER_FLAGS);
	}

	public HashSet<String> getAll(Keys key)
	{
		return pairings.getAll(key);
	}

}
