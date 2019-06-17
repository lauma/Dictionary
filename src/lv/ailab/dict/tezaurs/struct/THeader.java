package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.Header;

/**
 * v (vārds) field.
 */
public class THeader extends Header
{
	
	protected THeader(){};
	
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