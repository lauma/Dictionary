package lv.ailab.dict.struct.constants;

public class PronunciationCoding
{
	public static final PronunciationCoding DEFAULT_CODE = new PronunciationCoding();
	/**
	 * Stieptā intonācija
	 */
	public final String LEVEL_TONE;
	/**
	 * Lauztā intonācija
	 */
	public final String BROKEN_TONE;
	/**
	 * Krītošā intonācija.
	 */
	public final String FALLING_TONE;
	/**
	 * Kāpjošā intonācija.
	 */
	public final String RISING_TONE;
	/**
	 * Uzsvars.
	 */
	public final String STRESS;
	/**
	 * Zilbes robeža.
	 */
	public final String SYLLABLE_SPLIT;

	protected PronunciationCoding()
	{
		LEVEL_TONE = "~";
		BROKEN_TONE = "^";
		FALLING_TONE = "\\";
		RISING_TONE = "/";
		STRESS = "!";
		SYLLABLE_SPLIT = "-";
	};

	public PronunciationCoding(
			String levelTone, String brokenTone, String fallingTone,
			String risingTone, String stress, String syllableSplit)
	{
		LEVEL_TONE = levelTone;
		BROKEN_TONE = brokenTone;
		FALLING_TONE = fallingTone;
		RISING_TONE = risingTone;
		STRESS = stress;
		SYLLABLE_SPLIT = syllableSplit;
	}
}
