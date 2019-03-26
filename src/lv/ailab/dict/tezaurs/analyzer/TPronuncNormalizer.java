package lv.ailab.dict.tezaurs.analyzer;

import lv.ailab.dict.struct.constants.PronunciationCoding;
import lv.ailab.dict.utils.GramPronuncNormalizer;

public class TPronuncNormalizer implements GramPronuncNormalizer
{
	private static TPronuncNormalizer onlyOne = new TPronuncNormalizer();
	public static TPronuncNormalizer singleton()
	{
		return onlyOne;
	}
	public String normalizePronuncs (String pronunciation)
	{
		if (pronunciation == null) return null;
		pronunciation = pronunciation.trim();
		if (pronunciation.isEmpty()) return pronunciation;
		pronunciation = pronunciation.replace("`", PronunciationCoding.DEFAULT_CODE.FALLING_TONE);
		pronunciation = pronunciation.replace("'", PronunciationCoding.DEFAULT_CODE.STRESS);
		return pronunciation;
	}
}
