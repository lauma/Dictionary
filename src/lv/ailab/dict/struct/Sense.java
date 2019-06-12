package lv.ailab.dict.struct;

import lv.ailab.dict.utils.*;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Nozīmes, nozīmes nianses vai skaidrojuma lauks.
 * @author Lauma
 */
public class Sense implements HasToJSON, HasToXML
{
	/**
	 * Gramatikas lauks nav obligāts.
	 */
	public Gram grammar;

	/**
	 * Nozīmes, skaidrojuma teksts ("definīcija").
	 */
	public LinkedList<Gloss> gloss;

	/**
	 * Nozīmes numurs (ID).
	 */
	public String ordNumber;
	/**
	 * Piemēru, citātu grupa (neobligāta, Tēzaurā nav).
	 */
	public LinkedList<Sample> examples = null;

	/**
	 * Stabilu vārdu savienojumu, frazeoloģismu grupa (neobligāta, Tēzaura XML
	 * apzīmēta ar g_piem).
	 */
	public LinkedList<Phrase> phrases = null;
	/**
	 * Apakšnozīmju, nozīmes nianšu grupa ( neobligāta, Tēzaura XML apzīmēta ar
	 * g_an).
	 */
	public LinkedList<Sense> subsenses = null;

	public Sense()
	{
		grammar = null;
		gloss = null;
		phrases = null;
		subsenses = null;
		ordNumber = null;
	}

	public Sense(String glossText)
	{
		grammar = null;
		gloss = new LinkedList<>();
		gloss.add(new Gloss(glossText));
		phrases = null;
		subsenses = null;
		ordNumber = null;
	}

	public Sense(Gloss gloss)
	{
		grammar = null;
		this.gloss = new LinkedList<>();
		this.gloss.add(gloss);
		phrases = null;
		subsenses = null;
		ordNumber = null;
	}

	public void reinitialize(Gloss gloss)
	{
		grammar = null;
		this.gloss = new LinkedList<>();
		this.gloss.add(gloss);
		phrases = null;
		subsenses = null;
		ordNumber = null;
	}

	public boolean glossOnly()
	{
		return gloss != null && grammar == null &&
				(ordNumber == null || ordNumber.equals("")) &&
				(phrases == null || phrases.isEmpty()) &&
				(subsenses == null || subsenses.isEmpty());
	}

	public HashSet<Integer> getDirectParadigms()
	{
		if (grammar != null) return grammar.getDirectParadigms();
		return new HashSet<>();
	}

	/**
	 * Tikai statistiskām vajadzībām! Savāc visus paradigmu skaitlīšus, kas
	 * kaut kur šajā struktūrā parādās.
	 */
	public HashSet<Integer> getMentionedParadigms()
	{
		HashSet<Integer> paradigms = new HashSet<>();
		if (grammar != null)
			paradigms.addAll(grammar.getMentionedParadigms());
		if (phrases != null) for (Phrase e : phrases)
			paradigms.addAll(e.getMentionedParadigms());
		if (subsenses != null) for (Sense s : subsenses)
			paradigms.addAll(s.getMentionedParadigms());
		return paradigms;
	}

	/**
	 * Savāc visus karodziņus, kas kaut kur šajā struktūrā parādās.
	 */
	public Flags getUsedFlags()
	{
		Flags flags = new Flags();
		if (grammar != null && grammar.flags != null)
			flags.addAll(grammar.flags);
		if (phrases != null) for (Phrase e : phrases)
			flags.addAll(e.getUsedFlags());
		if (subsenses != null) for (Sense s : subsenses)
			flags.addAll(s.getUsedFlags());
		return flags;
	}

	/**
	 * Savāc visus hederus, kas parādās šajā struktūrā.
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		ArrayList<Header> res = new ArrayList();
		if (grammar != null) res.addAll(grammar.getImplicitHeaders());
		if (phrases != null) for (Phrase e : phrases)
			res.addAll(e.getImplicitHeaders());
		if (subsenses != null) for (Sense s : subsenses)
			res.addAll(s.getImplicitHeaders());
		return res;
	}

	/**
	 * Saskaita visus karodziņus, kas kaut kur šajā struktūrā parādās.
	 */
	public CountingSet<Tuple<String, String>> getFlagCounts()
	{
		CountingSet<Tuple<String, String>> counts = new CountingSet<>();

		if (grammar != null && grammar.flags != null)
			grammar.flags.count(counts);
		if (phrases != null) for (Phrase e : phrases)
			counts.addAll(e.getFlagCounts());
		if (subsenses != null) for (Sense s : subsenses)
			counts.addAll(s.getFlagCounts());
		return counts;
	}

	public String toJSON()
	{
		StringBuilder res = new StringBuilder();

		boolean hasPrev = false;

		if (ordNumber != null)
		{
			res.append("\"SenseNumber\":\"");
			res.append(JSONObject.escape(ordNumber));
			res.append("\"");
			hasPrev = true;
		}

		if (grammar != null)
		{
			if (hasPrev) res.append(", ");
			res.append(grammar.toJSON());
			hasPrev = true;
		}

		if (gloss != null)
		{
			if (hasPrev) res.append(", ");
			res.append("\"Gloss\":");
			res.append(JSONUtils.objectsToJSON(gloss));
			hasPrev = true;
		}

		if (examples != null && !examples.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Examples\":");
			res.append(JSONUtils.objectsToJSON(examples));
			hasPrev = true;
		}

		if (phrases != null && !phrases.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"StablePhrases\":");
			res.append(JSONUtils.objectsToJSON(phrases));
			hasPrev = true;
		}

		if (subsenses != null && !subsenses.isEmpty())
		{
			if (hasPrev) res.append(", ");
			res.append("\"Senses\":");
			res.append(JSONUtils.objectsToJSON(subsenses));
			hasPrev = true;
		}

		return res.toString();
	}

	/**
	 * Nozīmes numuru iekļauj kā atribūtu, pārējo kā elementus.
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Element senseN = doc.createElement("Sense");
		if (ordNumber != null) senseN.setAttribute("SenseNumber", ordNumber);

		if (grammar != null) grammar.toXML(senseN);
		if (gloss != null)
		{
			Node glossContN = doc.createElement("Gloss");
			for (Gloss g : gloss) g.toXML(glossContN);
			senseN.appendChild(glossContN);
		}
		if (examples != null && !examples.isEmpty())
		{
			Node exContN = doc.createElement("Examples");
			for (Sample e : examples) e.toXML(exContN);
			senseN.appendChild(exContN);
		}
		if (phrases != null && !phrases.isEmpty())
		{
			Node phContN = doc.createElement("StablePhrases");
			for (Phrase p : phrases) p.toXML(phContN);
			senseN.appendChild(phContN);
		}

		if (subsenses != null && !subsenses.isEmpty())
		{
			Node subsContN = doc.createElement("Subsenses");
			for (Sense subs : subsenses) subs.toXML(subsContN);
			senseN.appendChild(subsContN);
		}
		parent.appendChild(senseN);
	}
}
