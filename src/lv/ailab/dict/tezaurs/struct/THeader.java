package lv.ailab.dict.tezaurs.struct;


import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.Set;

/**
 * v (vārds) field.
 */
public class THeader extends Header
{
	
	public THeader()
	{
		lemma = null;
		gram = null;
	}
	
	public THeader(Node vNode)
	{
		NodeList fields = vNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("vf")) // lemma
			{
				if (lemma != null)
					System.err.printf("\'vf\' ar lemmu \"%s\" satur vēl vienu \'vf\'\n", lemma.text);
				lemma = new TLemma(field);
			}
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				postponed.add(field);
		}
		if (lemma == null)
			System.err.printf("Elements \'v\' ir bez lemmas:\n %s", vNode.toString());
		
		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram")) // grammar
				gram = new TGram(field, lemma.text);
			else System.err.printf(
					"\'v\' elements \'%s\' netika apstrādāts\n", fieldname);
		}				
	}
	public THeader(Lemma lemma, Set<Integer> paradigm, Flags flags)
	{
		super (lemma, paradigm, flags);
	}

	public THeader(Lemma lemma, int paradigm, Flags flags)
	{
		super (lemma, paradigm, flags);
	}

	public THeader(Lemma lemma, String gramText)
	{
		this.lemma = lemma;
		TGram newGram = new TGram();
		newGram.set(gramText, lemma.text);
		this.gram = newGram;
	}
	
	public boolean hasUnparsedGram()
	{
		return THeader.hasUnparsedGram(this);
	}
	public static boolean hasUnparsedGram(Header header)
	{
		if (header == null || header.gram == null) return false;
		return TGram.hasUnparsedGram(header.gram);
	}

}