/*******************************************************************************
 * Copyright 2013-2016 Institute of Mathematics and Computer Science, University of Latvia
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
package lv.ailab.dict.struct;

import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Values;
import lv.ailab.dict.utils.CountingSet;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.MappingSet;
import lv.ailab.dict.utils.Tuple;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Datu struktūra karodziņu uzturēšanai.
 * Null vērtības nav pieļaujamas.
 *
 * NB!
 * Par karodziņiem viena objekta ietvaros tiek uzskatīts, ka ja divi karodziņi
 * ir savstarpēji izslēdzoši, tad tos saista loģiskais VAI - sieviešu vai
 * vīriešu dzimte, mija var gan būt gan nebūt. Ja karodziņi nav savstarpēji
 * izslēdzoši, tad tie ir spēkā abi (loģiskais UN) - daudzskaitlis un 3. persona
 * vai arī viens no tiem precizē otru.
 *
 * Izveidots 2015-10-08.
 * @author Lauma
 */
public class Flags implements HasToXML
{
	public MappingSet<Keys, String> pairings;

	public Flags()
	{
		pairings = new MappingSet<>();
	}

	public void addAll(Flags others)
	{
		// Šeit nav vērts pārbaudīt uz null values, jo tas jau ir Flags objekts.
		if (others.pairings != null)
			pairings.putAll(others.pairings);
	}

	public void addAll(Map<Keys, String> others)
	{
		if (others != null) for (Keys k : others.keySet())
			add(k, others.get(k));
	}

	public void addAll(Set<Tuple<Keys, String>> others)
	{
		for (Tuple<Keys, String> t : others)
			add(t.first, t.second);
	}

	public void add(Keys key, String value)
	{
		if (value == null) throw new IllegalArgumentException(
					"Flags cannot contain null as an atribute value!");
		pairings.put(key, value);
	}
	public void add(Tuple<Keys, String> feature)
	{
		add(feature.first, feature.second);
	}
	public void add(Keys key, Values value)
	{
		if (value == null) throw new IllegalArgumentException(
				"Flags cannot contain null as an atribute value!");
		add(key, value.s);
	}
	public void add(String value)
	{
		add(Keys.OTHER_FLAGS, value);
	}
	public void add(Values value)
	{
		if (value == null) throw new IllegalArgumentException(
				"Flags cannot contain null as an atribute value!");
		add(Keys.OTHER_FLAGS, value.s);
	}

	public HashSet<String> binaryFlags()
	{
		return pairings.getAll(Keys.OTHER_FLAGS);
	}

	public HashSet<String> getAll(Keys key)
	{
		return pairings.getAll(key);
	}

	/**
	 * Pārbauda, vai karodziņi satur šādu atslēgas/vērtības pārīti.
	 * Ja vērtība ir null, pārbauda, vai satur šādu atslēgu.
	 */
	public boolean test (Keys key, String value)
	{
		if (value == null) return testKey(key);
		HashSet<String> found = pairings.getAll(key);
		if (found == null || found.size() < 1) return false;
		return (found.contains(value));
	}

	/**
	 * Pārbauda, vai karodziņi satur šādu atslēgas/vērtības pārīti.
	 * Ja vērtība ir null, pārbauda, vai satur šādu atslēgu.
	 */
	public boolean test (Keys key, Values value)
	{
		if (value == null) return testKey(key);
		return test(key, value.s);
	}

	/**
	 * Pārbauda, vai karodziņi satur šādu atslēgas/vērtības pārīti.
	 * Ja vērtība (pāra otrais elements) ir null, pārbauda, vai satur šādu
	 * atslēgu.
	 */
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

	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (pairings != null && !pairings.isEmpty())
		{
			Node flagsContN = doc.createElement("flags");
			for (Keys key : pairings.keySet().stream().filter(this::testKey).sorted()
					.collect(Collectors.toList()))
				for (String value : getAll(key).stream().sorted().collect(Collectors.toList()))
				{
					Node flagN = doc.createElement("flag");
					Node keyN = doc.createElement("key");
					keyN.appendChild(doc.createTextNode(key.toString()));
					flagN.appendChild(keyN);
					Node valN = doc.createElement("value");
					valN.appendChild(doc.createTextNode(value));
					flagN.appendChild(valN);
					flagsContN.appendChild(flagN);
				}
			parent.appendChild(flagsContN);
		}
	}

}
