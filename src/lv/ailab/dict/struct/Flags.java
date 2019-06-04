package lv.ailab.dict.struct;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.io.DomIoUtils;
import lv.ailab.dict.io.StdXmlFieldInputHelper;
import lv.ailab.dict.struct.constants.flags.Keys;
import lv.ailab.dict.utils.CountingSet;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.MappingSet;
import lv.ailab.dict.utils.Tuple;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
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
 * vai arī viens no tiem precizē otru. Ja ir neieciešams norādīt, ka darbības
 * vārdu lieto daudzskaitlī vai 3. personā, tad jāizveido atsevišķs karodziņš,
 * kas šo "vai" norāda.
 *
 * Izveidots 2015-10-08.
 * @author Lauma
 */
public class Flags implements HasToXML
{
	public MappingSet<String, String> pairings;

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

	public void addAll(Map<String, String> others)
	{
		if (others != null) for (String k : others.keySet())
			add(k, others.get(k));
	}

	public void addAll(Set<Tuple<String, String>> others)
	{
		for (Tuple<String, String> t : others)
			add(t.first, t.second);
	}

	public void add(String key, String value)
	{
		if (value == null) throw new IllegalArgumentException(
					"Flags cannot contain null as an atribute value!");
		pairings.put(key, value);
	}
	public void add(Tuple<String, String> feature)
	{
		add(feature.first, feature.second);
	}

	public void add(String value)
	{
		if (value == null) throw new IllegalArgumentException(
				"Flags cannot contain null as an atribute value!");
		add(Keys.OTHER_FLAGS, value);
	}

	public HashSet<String> binaryFlags()
	{
		return pairings.getAll(Keys.OTHER_FLAGS);
	}

	public HashSet<String> getAll(String key)
	{
		return pairings.getAll(key);
	}

	/**
	 * Pārbauda, vai karodziņi satur šādu atslēgas/vērtības pārīti.
	 * Ja vērtība ir null, pārbauda, vai satur šādu atslēgu.
	 */
	public boolean test (String key, String value)
	{
		if (value == null) return testKey(key);
		HashSet<String> found = pairings.getAll(key);
		if (found == null || found.size() < 1) return false;
		return (found.contains(value));
	}

	/**
	 * Pārbauda, vai karodziņi satur šādu atslēgas/vērtības pārīti.
	 * Ja vērtība (pāra otrais elements) ir null, pārbauda, vai satur šādu
	 * atslēgu.
	 */
	public boolean test (Tuple<String, String> feature)
	{
		return test (feature.first, feature.second);
	}

	/**
	 * Pārbauda, vai karodziņi satur šādu atslēgu ar kādu no dotajām vērtībām.
	 * Ja vērtība ir null, pārbauda, vai satur šādu atslēgu.
	 */
	public boolean testAnyValue (String key, Set<String> anyValue)
	{
		if (anyValue == null || anyValue.isEmpty()) return testKey(key);
		HashSet<String> found = pairings.getAll(key);
		if (found == null || found.size() < 1) return false;
		for (String value : anyValue)
			return (found.contains(value));
		return false;
	}
	public boolean testKey (String key)
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
	public CountingSet<Tuple<String, String>> count (
			CountingSet<Tuple<String, String>> accumulator)
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
			Node flagsContN = doc.createElement("Flags");
			for (String key : pairings.keySet().stream().filter(this::testKey).sorted()
					.collect(Collectors.toList()))
				for (String value : getAll(key).stream().sorted().collect(Collectors.toList()))
				{
					Node flagN = doc.createElement("Flag");
					Node keyN = doc.createElement("Key");
					keyN.appendChild(doc.createTextNode(key));
					flagN.appendChild(keyN);
					Node valN = doc.createElement("Value");
					valN.appendChild(doc.createTextNode(value));
					flagN.appendChild(valN);
					flagsContN.appendChild(flagN);
				}
			parent.appendChild(flagsContN);
		}
	}

	public static Flags fromStdXML(Node flagsNode, GenericElementFactory elemFact)
	throws DictionaryXmlReadingException
	{
		Flags result = elemFact.getNewFlags();
		result.pairings = new MappingSet<>();
		DomIoUtils.FieldMapping fields = DomIoUtils.domElemToHash((Element) flagsNode);
		if (fields == null || fields.isEmpty()) return null;

		ArrayList<Node> flagNodes = fields.nodeChildren.remove("Flag");
		if (flagNodes != null) for (Node flagNode : flagNodes)
		{
			String key = null;
			String value = null;

			DomIoUtils.FieldMapping flagFields = DomIoUtils.domElemToHash((Element) flagNode);
			if (flagFields == null || flagFields.isEmpty()) break;

			ArrayList<String> keyTexts = flagFields.stringChildren.remove("Key");
			if (keyTexts != null && keyTexts.size() > 1)
				throw new DictionaryXmlReadingException("Elementā \"Flag\" atrasti vairāki \"Key\"!");
			if (keyTexts!= null && !keyTexts.isEmpty()) key = keyTexts.get(0);

			ArrayList<String> valueTexts = flagFields.stringChildren.remove("Value");
			if (valueTexts != null && valueTexts.size() > 1)
				throw new DictionaryXmlReadingException("Elementā \"Flag\" atrasti vairāki \"Value\"!");
			if (valueTexts!= null && !valueTexts.isEmpty()) value = valueTexts.get(0);

			StdXmlFieldInputHelper.dieOnNonempty(flagFields, "Flag");
			result.pairings.put(key, value);
		}
		// Warn, if there is something else
		StdXmlFieldInputHelper.dieOnNonempty(fields, "Flags");
		if (result.pairings.isEmpty()) return null;
		return result;
	}

}
