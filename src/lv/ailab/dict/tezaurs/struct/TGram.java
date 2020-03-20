package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.Gram;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.tezaurs.analyzer.gramdata.*;
import lv.ailab.dict.utils.JSONUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.LinkedList;


public class TGram extends Gram
{
	/**
	 * Neatpazītas / neizparsētās gramatikas teksta daļas.
	 */
	public LinkedList<LinkedList<String>> leftovers;

	/**
	 * Zināmie saīsinājumi un to atšifrējumi.
	 */
	public static AbbrMap knownAbbr = AbbrMap.getAbbrMap();

	protected TGram() {};

	public int paradigmCount()
	{
		if (paradigm == null) return 0;
		return paradigm.size();
	}
	
	/**
	 * Šitais strādā pareizi tikai tad, ja cleanupLeftovers tiek izsaukts katru
	 * reizi, kad vajag.
	 */
	public boolean hasUnparsedGram()
	{
		return TGram.hasUnparsedGram(this);
	}

	/**
	 * Šitais strādā pareizi tikai tad, ja cleanupLeftovers tiek izsaukts katru
	 * reizi, kad vajag.
	 */
	public static boolean hasUnparsedGram(Gram gram)
	{
		//cleanupLeftovers();		// Kas ir labāk - nagaidīti blakusefekti vai tas, ka nestrādā, ja lieto nepareizi?
		if (gram == null) return false;
		try
		{
			// Interesanti, vai ar refleksiju šis triks būtu ātrāks?
			// Šito šobrīd it kā nevajag, bet nu drošības pēc:
			if (gram.altLemmas != null)
				for (Header alt : gram.altLemmas)
					if (((THeader) alt).hasUnparsedGram()) return true;
			TGram tGram = (TGram) gram;
			return tGram.leftovers != null && !tGram.leftovers.isEmpty();
		}
		catch (ClassCastException e)
		{
			return false;
		}
	}

	/**
	 * Šo jāizsauc katru reizi, kad kaut ko izņem no leftovers.
	 */
	public void cleanupLeftovers()
	{
		for (int i = leftovers.size() - 1; i >= 0; i--)
			if (leftovers.get(i).isEmpty()) leftovers.remove(i);
	}
	
	/**
	 * Gramatikas struktūras JSON reprezentācija, kas iekļauj arī norādes par
	 * neapstrādātajiem pārpalikumiem "leftovers".
	 */
	@Override
	public String toJSON()
	{
		if (leftovers != null && leftovers.size() > 0)
		{
			StringBuilder additional = new StringBuilder();
			additional.append("\"Leftovers\":[");
			Iterator<LinkedList<String>> it = leftovers.iterator();
			while (it.hasNext())
			{
				LinkedList<String> next = it.next();
				if (!next.isEmpty())
				{
					additional.append(JSONUtils.simplesToJSON(next));
					if (it.hasNext()) additional.append(", ");
				}
			}
			additional.append("]");
			return toJSON(null, null, additional.toString());
		}
		else return toJSON(null, null, null);
	}

	/**
	 * Gramatikas struktūras XML reprezentācija, kas iekļauj arī norādes par
	 * neapstrādātajiem pārpalikumiem "leftovers".
	 */
	@Override
	public void toXML(Node parent)
	{
		if (leftovers != null && !leftovers.isEmpty())
		{
			Document doc = parent.getOwnerDocument();
			Node loContN = doc.createElement("Leftovers");
			for (LinkedList<String> lo1 : leftovers)
			{
				if (lo1 == null || lo1.isEmpty()) continue;
				Node loSemicolonN = doc.createElement("Leftovers");
				for (String lo2 : lo1)
				{
					if (lo2 == null || lo2.isEmpty()) continue;
					Node loColonN = doc.createElement("Leftover");
					loColonN.appendChild(doc.createTextNode(lo2));
					loSemicolonN.appendChild(loColonN);
				}
				loContN.appendChild(loSemicolonN);
			}
			toXML(parent, null, null, loContN);
		}
		else toXML(parent, null, null, null);;
	}
}