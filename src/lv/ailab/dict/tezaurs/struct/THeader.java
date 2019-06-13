package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.Header;

/**
 * v (vārds) field.
 */
public class THeader extends Header
{
	
	protected THeader(){};
	
	/*public THeader(Lemma lemma, Set<Integer> paradigm, Flags flags)
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
	}*/
	
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