package lv.ailab.dict.tezaurs.analyzer.struct;

import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;
import lv.ailab.dict.tezaurs.analyzer.io.Loaders;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;


/**
 * n (nozīme / nozīmes nianse) field.
 * TODO pielikt izdrukātāju, kas dusmojas, ja ir vairākas glosas.
 */
public class TSense extends Sense
{
	public TSense()
	{
		super();
	}
	
	/**
	 * @param lemma is used for grammar parsing.
	 */
	public TSense(Node nNode, String lemma)
	{
		NodeList fields = nNode.getChildNodes(); 
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram"))
				grammar = new TGram(field, lemma);
			else if (fieldname.equals("d"))
			{
				NodeList glossFields = field.getChildNodes();
				for (int j = 0; j < glossFields.getLength(); j++)
				{
					Node glossField = glossFields.item(j);
					String glossFieldname = glossField.getNodeName();
					if (glossFieldname.equals("t"))
					{
						if (gloss != null)
							System.err.println("\'d\' elements satur vairāk kā vienu \'t\'");
						else gloss = new LinkedList<>();
						gloss.add(new TGloss(glossField));
					}
					else if (!glossFieldname.equals("#text")) // Text nodes here are ignored.
						System.err.printf("\'d\' elements \'%s\' netiek apstrādāts\n", glossFieldname);
				}
			}
			else if (fieldname.equals("g_piem"))
				phrases = Loaders.loadPhrases(field, lemma, "piem");
			else if (fieldname.equals("g_an"))
				subsenses = Loaders.loadSenses(field, lemma);
			else if (!fieldname.equals("#text")) // Text nodes here are ignored.
				System.err.printf("\'n\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
		ordNumber = ((org.w3c.dom.Element)nNode).getAttribute("nr");
		if ("".equals(ordNumber)) ordNumber = null;
	}

	public boolean hasUnparsedGram()
	{
		return hasUnparsedGram(this);
	}

	public static boolean hasUnparsedGram(Sense sense)
	{
		if (sense == null) return false;
		if (sense.grammar != null && TGram.hasUnparsedGram(sense.grammar))
			return true;

		if (sense.phrases != null) for (Phrase e : sense.phrases)
		{
			if (TPhrase.hasUnparsedGram(e)) return true;
		}
		if (sense.subsenses != null) for (Sense s : sense.subsenses)
		{
			if (TSense.hasUnparsedGram(s)) return true;
		}			
		return false;
	}

	public int countEmptyGloss()
	{
		return countEmptyGloss(this);
	}

	public static int countEmptyGloss(Sense s)
	{
		if (s == null) return 0;
		int res = 0;
		if (s.gloss == null || s.gloss.isEmpty() ||
				s.gloss.stream().anyMatch(g -> g.text == null || g.text.isEmpty()))
			res++;
		if (s.subsenses != null) for (Sense sub : s.subsenses)
			res = res + TSense.countEmptyGloss(sub);
		if (s.phrases != null) for (Phrase p : s.phrases)
			res = res +TPhrase.countEmptyGloss(p);
		return res;
	}

}