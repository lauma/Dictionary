package lv.ailab.dict.llvv.struct;

import lv.ailab.dict.struct.GenericElementFactory;

public class LLVVElementFactory extends GenericElementFactory
{
	protected LLVVElementFactory(){};
	protected static LLVVElementFactory singleton = new LLVVElementFactory();
	public static LLVVElementFactory me()
	{
		return singleton;
	}
}
