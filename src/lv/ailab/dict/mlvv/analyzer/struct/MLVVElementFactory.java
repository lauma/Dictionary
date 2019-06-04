package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.GenericElementFactory;

public class MLVVElementFactory extends GenericElementFactory
{
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
