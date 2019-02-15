package lv.ailab.dict.struct;

import lv.ailab.dict.utils.CountingSet;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.Tuple;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Valodas materiāls - piemēri un citāti. Tiek lietoti MLVV un LLVV, bet ne
 * Tēzaurā.
 * Izdalīts no Phrase 2019-02-15.
 * @author Lauma
 */
public class Sample implements HasToJSON, HasToXML
{
	/**
	 * Skaidrojamā frāze - parasti viena, taču reizēm var būt vairākas.
	 */
	public LinkedList<String> text;

	/**
	 * Neobligātas gramatiskās norādes.
	 */
	public Gram grammar;

	/**
	 * Frāzes tips.
	 */
	public Sample.Type type;

	/**
	 * Neobligāts autors vai avots.
	 */
	public String source;


	public Sample()
	{
		text = null;
		grammar = null;
	}

	/**
	 * Tikai statistiskām vajadzībām! Savāc visas paradigmas, kas šajā truktūrā
	 * ir pieminētas.
	 */
	protected Set<Integer> getMentionedParadigms()
	{
		HashSet<Integer> paradigms = new HashSet<>();
		if (grammar != null)
			paradigms.addAll(grammar.getMentionedParadigms());
		return paradigms;
	}

	/**
	 * Savāc visus karodziņus, kas ir lietoti šajā stuktūrā.
	 */
	public Flags getUsedFlags()
	{
		Flags flags = new Flags();
		if (grammar != null && grammar.flags != null)
			flags.addAll(grammar.flags);
		return flags;
	}

	/**
	 * Savāc visus hederus, kas parādās šajā struktūrā.
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList();
		if (grammar != null) res.addAll(grammar.getImplicitHeaders());
		return res;
	}


	/**
	 * Saskaita visus karodziņus, kas lietoti šajā struktūrā.
	 */
	public CountingSet<Tuple<String, String>> getFlagCounts()
	{
		CountingSet<Tuple<String, String>> counts = new CountingSet<>();

		if (grammar != null && grammar.flags != null)
			grammar.flags.count(counts);
		return counts;
	}

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();

		//res.append("\"Sample\":{");
		boolean hasPrev = false;

		if (type != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Type\":\"");
			res.append(JSONObject.escape(type.toString()));
			res.append("\"");
			hasPrev = true;
		}

		if (text != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Text\":[");
			res.append(text.stream().map(t -> "\"" + JSONObject.escape(t) + "\"")
					.reduce((t1, t2) -> t1 + "," + t2).orElse(""));
			res.append("]");
			hasPrev = true;
		}

		if (grammar != null)
		{
			if (hasPrev) res.append(", ");
			res.append(grammar.toJSON());
			hasPrev = true;
		}

		if (source != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Source\":\"");
			res.append(JSONObject.escape(source));
			res.append("\"");
			hasPrev = true;
		}

		//res.append("}");
		return res.toString();
	}

	/**
	 * Ja text ir "", tad to vienalga izdrukā.
	 * TODO vai tā ir labi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Element phraseN = doc.createElement("Sample");
		if (type != null) phraseN.setAttribute("Type", type.toString());
		if (text != null)
		{
			Node textN = doc.createElement("Text");
			for (String var : text)
			{
				Node textVar = doc.createElement("Variant");
				textVar.appendChild(doc.createTextNode(var));
				textN.appendChild(textVar);
			}
			//textN.appendChild(doc.createTextNode(text));
			phraseN.appendChild(textN);
		}
		if (grammar != null) grammar.toXML(phraseN);
		if (source != null)
		{
			Node sourceN = doc.createElement("Source");
			sourceN.setTextContent(source);
			phraseN.appendChild(sourceN);
		}
		parent.appendChild(phraseN);
	}

	/**
	 * Frāžu tipu uzskaitījums un atreferējumi MLVV, LLVV vajadzībām.
	 * Izveidots 2016-02-11.
	 *
	 * @author Lauma
	 */
	public enum Type
	{
		SAMPLE("Piemērs"),
		QUOTE("Citāts");

		String s;
		Type (String value)
		{
			s = value;
		}

		@Override
		public String toString()
		{
			return s;
		}

	}

}
