package lv.ailab.dict.llvv.analyzer;

import lv.ailab.dict.struct.constants.PronunciationCoding;

public class PronuncNormalizer
{
	public static String normalize (String pronunciation)
	{
		if (pronunciation == null) return null;
		pronunciation = pronunciation.trim();
		if (pronunciation.isEmpty()) return pronunciation;
		pronunciation = pronunciation.replace("`", PronunciationCoding.DEFAULT_CODE.FALLING_TONE);
		pronunciation = pronunciation.replace("'", PronunciationCoding.DEFAULT_CODE.STRESS);
		pronunciation = pronunciation.replace(" ", PronunciationCoding.DEFAULT_CODE.STRESS);
		return pronunciation;
	}
}
