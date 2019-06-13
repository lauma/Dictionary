package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.GenericElementFactory;

public class TElementFactory extends GenericElementFactory
{
	protected TElementFactory(){};
	protected static TElementFactory singleton = new TElementFactory();
	public static TElementFactory me()
	{
		return singleton;
	}

	@Override
	public TEntry getNewEntry() { return new TEntry(); }
	@Override
	public TGloss getNewGloss() { return new TGloss(); }
	@Override
	public TGram getNewGram() { return new TGram(); }
	@Override
	public THeader getNewHeader() { return new THeader(); }
	@Override
	public TSense getNewSense() { return new TSense(); }
	@Override
	public TPhrase getNewPhrase() { return new TPhrase(); }
}
