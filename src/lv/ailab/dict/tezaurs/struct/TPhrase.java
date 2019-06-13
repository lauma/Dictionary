package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;

/**
 * piem (piemērs) un fraz (frazeoloģisms) lauki Tēzaura XML.
 */
public class TPhrase extends Phrase
{
	protected TPhrase(){};

	public boolean hasUnparsedGram()
	{
		return hasUnparsedGram(this);
	}

	public static boolean hasUnparsedGram(Phrase phrase)
	{
		if (phrase == null) return false;
		if (phrase.grammar != null && TGram.hasUnparsedGram(phrase.grammar))
			return true;

		if (phrase.subsenses != null) for (Sense s : phrase.subsenses)
			if (TSense.hasUnparsedGram(s)) return true;

		return false;
	}

	public int countEmptyGloss()
	{
		return countEmptyGloss(this);
	}

	public static int countEmptyGloss(Phrase p)
	{
		if (p == null) return 0;
		int res = 0;
		if (p.subsenses != null) for (Sense sub : p.subsenses)
			res = res + TSense.countEmptyGloss(sub);
		return res;
	}

}