package lv.ailab.dict.llvv.analyzer;

import lv.ailab.dict.struct.constants.PronunciationCoding;
import lv.ailab.dict.utils.GramPronuncNormalizer;

public class LLVVPronuncNormalizer implements GramPronuncNormalizer
{
	protected LLVVPronuncNormalizer(){};
	protected static LLVVPronuncNormalizer singleton = new LLVVPronuncNormalizer();
	public static LLVVPronuncNormalizer me()
	{
		return singleton;
	}
	public String normalizePronuncs (String pronunciation)
	{
		if (pronunciation == null) return null;
		pronunciation = pronunciation.trim();
		if (pronunciation.isEmpty()) return pronunciation;
		pronunciation = pronunciation.replace("`", PronunciationCoding.DEFAULT_CODE.FALLING_TONE);
		pronunciation = pronunciation.replace("'", PronunciationCoding.DEFAULT_CODE.STRESS);
		//pronunciation = pronunciation.replace(" ", PronunciationCoding.DEFAULT_CODE.STRESS);
		return pronunciation;
	}
}
