package lv.ailab.dict.utils;

public interface GramPronuncNormalizer
{
	/**
	 * Dotajā gramatikas brīvtekstā atrod visas kvadrātiekavās dotās izrunas un
	 * normalizē atbilstoši kādiem nosacījumiem.
	 * @param gramText	teksts, ko normalizēt
	 * @return	normalizētais teksts
	 */
	String normalizePronuncs(String gramText);
}
