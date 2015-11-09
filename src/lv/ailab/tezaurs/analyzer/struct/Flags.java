package lv.ailab.tezaurs.analyzer.struct;

import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.utils.CountingSet;
import lv.ailab.tezaurs.utils.MappingSet;
import lv.ailab.tezaurs.utils.Tuple;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Datu struktūra karodziņu uzturēšanai.
 *
 * NB!
 * Par karodziņiem viena objekta ietvaros tiek uzskatīts, ka ja divi karodziņi
 * ir savstarpēji izslēdzoši, tad tos saista loģiskais VAI - sieviešu vai
 * vīriešu dzimte, mija var gan būt gan nebūt. Ja karodziņi nav savstarpēji
 * izslēdzoši, tad tie ir spēkā abi (loģiskais UN) vai arī viens no tiem precizē
 * otru.
 *
 * Izveidots 2015-10-08.
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

	public void addAll(Map<Keys, String> others)
	{
		if (others != null)
			pairings.putAll(others);
	}

	public void addAll(Set<Tuple<Keys, String>> others)
	{
		for (Tuple<Keys, String> t : others)
			pairings.put(t.first, t.second);
	}

	public void add(Keys key, String value)
	{
		pairings.put(key, value);
	}
	public void add(Tuple<Keys, String> feature)
	{
		pairings.put(feature.first, feature.second);
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

	public boolean test (Keys key, String value)
	{
		HashSet<String> found = pairings.getAll(key);
		if (found == null || found.size() < 1) return false;
		return (found.contains(value));
	}

	public boolean test (Keys key, Values value)
	{
		return test(key, value.s);
	}

	public boolean test (Tuple<Keys, String> feature)
	{
		return test (feature.first, feature.second);
	}

	public boolean testKey (Keys key)
	{
		HashSet<String> found = pairings.getAll(key);
		return !(found == null || found.size() < 1);
	}

	/**
	 * Pieskaita karodziņus jau esošam karodziņu skaitīšanas objektam, vai, ja
	 * padots null, tad izveido jaunu.
	 * @param accumulator	karodziņu skaitīšanas objekts
	 * @return karodziņu skaitīšanas objekts ar atjauninātu informāciju
	 */
	public CountingSet<Tuple<Keys, String>> count (
			CountingSet<Tuple<Keys, String>> accumulator)
	{
		if( accumulator == null) accumulator = new CountingSet<>();

		accumulator.addAll(pairings.asList());

		return accumulator;
	}

}
