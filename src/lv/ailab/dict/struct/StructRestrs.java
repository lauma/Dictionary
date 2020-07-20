package lv.ailab.dict.struct;

import lv.ailab.dict.struct.constants.structrestrs.Frequency;
import lv.ailab.dict.tezaurs.struct.constants.structrestrs.TFrequency;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.JSONUtils;
import lv.ailab.dict.utils.Tuple;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.*;
import java.util.stream.Collectors;

public class StructRestrs implements HasToXML, HasToJSON
{
	public LinkedHashSet<One> restrictions = new LinkedHashSet<>();

	public boolean sortForPrint = false;

	protected StructRestrs () {};

	public void addOne(
			String type, Tuple<String,String> flag)
	{
		restrictions.add(One.of(type, flag));
	}

	public void addOne(
			String type, String frequency, Tuple<String,String> flag)
	{
		restrictions.add(One.of(type, frequency, flag));
	}

	public void addOne(
			String type, String frequency, Tuple<String,String>[] flags)
	{
		restrictions.add(One.of(type, frequency, flags));
	}

	public void addOne(
			String type, String frequency, String valueText)
	{
		restrictions.add(One.of(type, frequency, valueText));
	}

	public void addOne(
			String type, String frequency, Tuple<String, String> flag, String value)
	{
		restrictions.add(One.of(type, frequency, flag, value));
	}

	public void addOne(
			String type, String frequency, Tuple<String, String>[] flags, String value)
	{
		restrictions.add(One.of(type, frequency, flags, value));
	}

	public HashSet<One> filterByType(String type)
	{
		if (restrictions == null || restrictions.isEmpty()) return null;
		HashSet<One> res = new HashSet<>();
		for (One r : restrictions)
		{
			if (type.equals(r.type))
				res.add(r);
		}
		return res;

	}

	public boolean testByTypeKey(String type, String key)
	{
		if (restrictions == null || restrictions.isEmpty()) return false;
		for (One r : restrictions)
			if (type.equals(r.type) && r.valueFlags != null &&r.valueFlags.testKey(key))
				return true;
		return false;
	}

	public boolean testByTypeKey(String type, String frequency, String key)
	{
		if (restrictions == null || restrictions.isEmpty()) return false;
		for (One r : restrictions)
			if (type.equals(r.type) && Objects.equals(frequency, r.frequency) &&
					r.valueFlags != null &&r.valueFlags.testKey(key))
				return true;
		return false;
	}

	public boolean testByTypeFeature(String type, String frequency, Tuple<String,String> feature)
	{
		if (restrictions == null || restrictions.isEmpty()) return false;
		for (One r : restrictions)
		{
			if (type.equals(r.type) && Objects.equals(frequency, r.frequency) &&
					r.valueFlags != null && r.valueFlags.test(feature))
				return true;
		}
		return false;
	}

	public LinkedHashSet<One> filterByTypeFeature(String type, Tuple<String,String> feature)
	{

		if (restrictions == null || restrictions.isEmpty()) return null;
		LinkedHashSet<One> res = new LinkedHashSet<>();
		for (One r : restrictions)
		{
			if (type.equals(r.type) && r.valueFlags != null && r.valueFlags.test(feature))
				res.add(r);
		}
		return res;
	}


	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (restrictions == null || restrictions.isEmpty()) return;
		Node structRestrContN = doc.createElement("StructuralRestrictions");
		parent.appendChild(structRestrContN);
		if (restrictions.size() == 1)
		{
			(restrictions.toArray(new One[restrictions.size()]))[0].toXML(structRestrContN);
			return;
		}

		ArrayList<ArrayList<One>> clauses = orderInClauses();
		Node listParent = parent;
		if (clauses.size() > 1)
		{
			listParent = doc.createElement("AND");
			structRestrContN.appendChild(listParent);
		}
		for (ArrayList<One> orList: clauses)
		{
			if (orList.size() > 1)
			{
				Node orContN = doc.createElement("OR");
				for (One o : orList) o.toXML(orContN);
				listParent.appendChild(orContN);
			} else orList.get(0).toXML(listParent);
		}

		//for (StructRestrs.One restr: sortedRestr)
		//	if (restr != null) restr.toXML(structRestrContN);
	}

	protected ArrayList<ArrayList<One>> orderInClauses()
	{
		List<StructRestrs.One> sortedRestr = sortForPrint ?
				restrictions.stream().sorted(One.getPartialComparator())
						.collect(Collectors.toList()) :
				new ArrayList<>(restrictions);
		ArrayList<ArrayList<One>> clauses = new ArrayList<>();
		while (!sortedRestr.isEmpty())
		{
			One first  = sortedRestr.get(0);
			ArrayList<One> orList = new ArrayList<>();
			ArrayList<One> other = new ArrayList<>();
			for (One o : sortedRestr)
			{
				if (o.equalTypeFreq(first)) orList.add(o);
				else other.add(o);
			}

			clauses.add(orList);
			sortedRestr = other;
		}
		return clauses;
	}

	public String toJSON()
	{
		if (restrictions == null || restrictions.size() < 1) return "";
		if (restrictions.size() == 1)
			return (restrictions.toArray(new One[restrictions.size()]))[0].toJSON();
		StringBuilder res = new StringBuilder();
		ArrayList<ArrayList<One>> clauses = orderInClauses();
		if (clauses.size() > 1) res.append("\"AND\":[{");
		boolean hasPrev = false;
		for (ArrayList<One> orList : clauses)
		{
			if (hasPrev) res.append("}, {");
			if (orList.size() > 1)
			{
				res.append("\"OR\":");
				res.append(JSONUtils.objectsToJSON(orList));
			}
			else
				res.append(orList.get(0).toJSON());
			hasPrev = true;
		}
		if (clauses.size() > 1) res.append("}]");

		return res.toString();
	}



	/**
	 * Ierobežojumi par lietošanu noteiktās formās vai sintaktiskajās struktūrās.
	 * @author Lauma
	 */
	public static class One implements HasToJSON, HasToXML, Cloneable
	{
		public String type;
		public String frequency;
		public LinkedHashSet<String> valueText;
		public Flags valueFlags;

		public static One of(
				String type, Tuple<String, String> flag)
		{
			One result = new One();
			result.type = type;
			result.frequency = Frequency.UNDISCLOSED;
			if (flag != null)
			{
				result.valueFlags = new Flags();
				result.valueFlags.add(flag);
			}
			return result;
		}

		public static One of(String type, String frequency)
		{
			One result = new One();
			result.type = type;
			result.frequency = frequency;
			return result;
		}

		public static One of(
				String type, String frequency, Tuple<String, String> flag)
		{
			One result = new One();
			result.type = type;
			result.frequency = frequency;
			if (flag != null)
			{
				result.valueFlags = new Flags();
				result.valueFlags.add(flag);
			}
			return result;
		}

		public static One of(
				String type, String frequency, Tuple<String, String> flag, String value)
		{
			One result = new One();
			result.type = type;
			result.frequency = frequency;
			if (flag != null)
			{
				result.valueFlags = new Flags();
				result.valueFlags.add(flag);
			}
			if (value != null && !value.isEmpty())
			{
				result.valueText = new LinkedHashSet<>();
				result.valueText.add(value);
			}
			return result;
		}

		public static One of(
				String type, Tuple<String,String>[] flags)
		{
			One result = new One();
			result.type = type;
			result.frequency = Frequency.UNDISCLOSED;
			if (flags != null)
			{
				result.valueFlags = new Flags();
				result.valueFlags.addAll(new HashSet<>(Arrays.asList(flags)));
			}
			return result;
		}

		public static One of(
				String type, String frequency, Tuple<String,String>[] flags)
		{
			One result = new One();
			result.type = type;
			result.frequency = frequency;
			if (flags != null)
			{
				result.valueFlags = new Flags();
				result.valueFlags.addAll(new HashSet<>(Arrays.asList(flags)));
			}
			return result;
		}

		public static One of(
				String type, String frequency, Tuple<String,String>[] flags,
				String value)
		{
			One result = new One();
			result.type = type;
			result.frequency = frequency;
			if (flags != null)
			{
				result.valueFlags = new Flags();
				result.valueFlags.addAll(new HashSet<>(Arrays.asList(flags)));
			}
			if (value != null && !value.isEmpty())
			{
				result.valueText = new LinkedHashSet<>();
				result.valueText.add(value);
			}
			return result;
		}

		public static One of(
				String type, String frequency, String valueText)
		{
			One result = new One();
			result.type = type;
			result.frequency = frequency;
			result.valueText = new LinkedHashSet<>();
			result.valueText.add(valueText);
			return result;
		}

		public void addFlag (Tuple <String, String> flag)
		{
			if (flag == null) return;
			if (valueFlags == null) valueFlags = new Flags();
			valueFlags.add(flag);
		}

		/**
		 * Atdod tikai to, kas būs JSON objektā iekšā - bez ietverošajām { }
		 * un bez elementa nosaukuma.
		 */
		@Override
		public String toJSON()
		{
			StringBuilder res = new StringBuilder();

			//res.append("\"StructuralRestriction\":{");
			boolean hasPrev = false;

			if (type != null)
			{
				if (hasPrev) res.append(", ");
				res.append("\"Restriction\":\"");
				res.append(JSONObject.escape(type));
				res.append("\"");
				hasPrev = true;
			}

			if (frequency != null)
			{
				if (hasPrev) res.append(", ");
				res.append("\"Frequency\":\"");
				res.append(JSONObject.escape(frequency));
				res.append("\"");
				hasPrev = true;
			}

			if (valueText != null || valueFlags != null && !valueFlags.pairings.isEmpty())
			{
				if (hasPrev) res.append(", ");
				res.append("\"Value\":{");

				if (valueFlags != null && !valueFlags.pairings.isEmpty())
				{
					res.append("\"Flags\":");
					res.append(JSONUtils.mappingSetToJSON(valueFlags.pairings));
				}
				if (valueText != null && valueFlags != null && !valueFlags.pairings.isEmpty())
					res.append(", ");
				if (valueText != null)
				{
					res.append("\"LanguageMaterial\":");
					res.append(JSONUtils.simplesToJSON(valueText));
				}

				res.append("}");
				hasPrev = true;
			}

			return res.toString();
		}

		/**
		 * Attiecīgā klase prot visu būtisko saturu pārtasīt par DOM nodēm un
		 * sakabināt zem dotās vecāknodes.
		 *
		 * @param parent
		 */
		@Override
		public void toXML(Node parent)
		{
			Document doc = parent.getOwnerDocument();
			Element trN = doc.createElement("StructuralRestriction");

			if (type != null)
			{
				Node typeN = doc.createElement("Restriction");
				typeN.appendChild(doc.createTextNode(type));
				trN.appendChild(typeN);
			}

			if (frequency != null)
			{
				Node freqN = doc.createElement("Frequency");
				freqN.appendChild(doc.createTextNode(frequency));
				trN.appendChild(freqN);
			}

			if (valueText != null && !valueText.isEmpty()
					|| valueFlags != null && !valueFlags.pairings.isEmpty())
			{
				Node valueN = doc.createElement("Value");

				if (valueFlags != null && !valueFlags.pairings.isEmpty())
					valueFlags.toXML(valueN);

				if (valueText != null && !valueText.isEmpty())
				{
					Node langN = doc.createElement("LanguageMaterial");
					for (String singleText : valueText)
					{
						Node textNode = doc.createElement("Text");
						textNode.appendChild(doc.createTextNode(singleText));
						langN.appendChild(textNode);
					}
					valueN.appendChild(langN);
				}
				trN.appendChild(valueN);
			}

			parent.appendChild(trN);
		}

		public boolean equalTypeFreq (One other)
		{
			if (other == null) return false;
			return Objects.equals(type, other.type)
					&& Objects.equals(frequency, other.frequency);
		}

		// This is needed for putting Lemmas in hash structures (hasmaps, hashsets).
		@Override
		public boolean equals (Object o)
		{
			if (o == null) return false;
			if (this.getClass() != o.getClass()) return false;
			return (Objects.equals(type, ((One) o).type)) &&
					(Objects.equals(frequency, ((One) o).frequency)) &&
					(Objects.equals(valueText, ((One) o).valueText)) &&
					(Objects.equals(valueFlags, ((One) o).valueFlags));
		}

		// This is needed for putting Lemmas in hash structures (hasmaps, hashsets).
		@Override
		public int hashCode()
		{
			return 3343 * (type == null ? 1 : type.hashCode())
					+ 691 * (frequency == null ? 1 : frequency.hashCode())
					+ 43 * (valueText == null ? 1 : valueText.hashCode())
					+ 7 * (valueFlags == null ? 1 : valueFlags.hashCode());
		}

		/**
		 * @return a clone of this instance.
		 * @see Cloneable
		 */
		@Override
		public Object clone()
		{
			One clone = One.of(type, frequency);
			if (valueFlags != null) clone.valueFlags = (Flags) valueFlags.clone();
			if (valueText != null) clone.valueText = (LinkedHashSet<String>) valueText.clone();
			return clone;
		}

		public static Comparator<One> getPartialComparator()
		{
			return (o1, o2) -> {
				if (o1 == o2) return 0;
				if (o1 == null) return -1;
				if (o2 == null) return 1;
				if (Objects.equals(o1.type, o2.type))
				{
					if (Objects.equals(o1.frequency, o2.frequency)) return 0;
					if (o1.frequency == null) return -1;
					if (o2.frequency == null) return 1;
					return o1.frequency.compareTo(o2.frequency);
				}
				if (o1.type == null) return -1;
				if (o2.type == null) return 1;
				return o1.type.compareTo(o2.type);
			};
		}
	}
}
