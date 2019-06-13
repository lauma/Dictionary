package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;

/**
 * n (nozīme / nozīmes nianse) field.
 * TODO pielikt izdrukātāju, kas dusmojas, ja ir vairākas glosas.
 */
public class TSense extends Sense
{
	protected TSense() {};

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