package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.struct.GenericElementFactory;

public class MLVVElementFactory extends GenericElementFactory
{
	protected MLVVElementFactory(){};
	protected static MLVVElementFactory singleton = new MLVVElementFactory();
	public static MLVVElementFactory me()
	{
		return singleton;
	}

	@Override
	public MLVVEntry getNewEntry() { return new MLVVEntry(); }
	@Override
	public MLVVGloss getNewGloss() { return new MLVVGloss(); }
	@Override
	public MLVVGram getNewGram() { return new MLVVGram(); }
	@Override
	public MLVVHeader getNewHeader() { return new MLVVHeader(); }
	@Override
	public MLVVPhrase getNewPhrase() { return new MLVVPhrase(); }
	@Override
	public MLVVSense getNewSense() { return new MLVVSense(); }
}
