package lv.ailab.dict.struct;

import lv.ailab.dict.struct.constants.structrestrs.Frequency;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.JSONUtils;
import lv.ailab.dict.utils.Tuple;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class StructRestrs implements HasToXML
{
	public HashSet<One> restrictions = new HashSet<>();

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

	/*public HashSet<One> filterByTypeKey(String type, String key)
	{
		if (restrictions == null || restrictions.isEmpty()) return null;
		HashSet<One> res = new HashSet<>();
		for (One r : restrictions)
		{
			if (type.equals(r.type) && r.valueFlags.testKey(key))
				res.add(r);
		}
		return res;

	}*/
	public boolean testByTypeKey(String type, String key)
	{
		if (restrictions == null || restrictions.isEmpty()) return false;
		for (One r : restrictions)
			if (type.equals(r.type) && r.valueFlags != null &&r.valueFlags.testKey(key))
				return true;
		return false;
	}

	public boolean testByTypeFeature(String type, Tuple<String,String> feature)
	{
		if (restrictions == null || restrictions.isEmpty()) return false;
		for (One r : restrictions)
		{
			if (type.equals(r.type) && r.valueFlags != null && r.valueFlags.test(feature))
				return true;
		}
		return false;
	}


	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (restrictions != null && !restrictions.isEmpty())
		{
			Node structRestrContN = doc.createElement("StructuralRestrictions");
			for (StructRestrs.One restr: restrictions)
				if (restr != null) restr.toXML(structRestrContN);
			parent.appendChild(structRestrContN);
		}
	}


	/**
	 * Ierobežojumi par lietošanu noteiktās formās vai sintaktiskajās struktūrās.
	 * @author Lauma
	 */
	public static class One implements HasToJSON, HasToXML
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

			if (valueText != null || valueFlags != null && !valueFlags.pairings.isEmpty())
			{
				Node valueN = doc.createElement("Value");

				if (valueFlags != null && !valueFlags.pairings.isEmpty())
					valueFlags.toXML(valueN);

				if (valueText != null)
				{
					Node langN = doc.createElement("LanguageMaterial");
					for (String singleText : valueText)
					{
						Node textNode = doc.createElement("Text");
						textNode.appendChild(doc.createTextNode(singleText));
						langN.appendChild(textNode);
					}
					trN.appendChild(langN);
				}
				trN.appendChild(valueN);
			}

			parent.appendChild(trN);
		}

		// This is needed for putting Lemmas in hash structures (hasmaps, hashsets).
		@Override
		public boolean equals (Object o)
		{
			if (o == null) return false;
			if (this.getClass() != o.getClass()) return false;
			return (type == null && ((One) o).type == null
					|| type != null && type.equals(((One) o).type))
					&& (frequency == null && ((One) o).frequency == null
					|| frequency != null && frequency.equals(((One) o).frequency))
					&& (valueText == null && ((One) o).valueText == null
					|| valueText != null && valueText.equals(((One) o).valueText))
					&& (valueFlags == null && ((One) o).valueFlags == null
					|| valueFlags != null && valueFlags.equals(((One) o).valueFlags));
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
	}
}
