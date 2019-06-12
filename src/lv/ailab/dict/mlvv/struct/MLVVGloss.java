package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.struct.Gloss;

/**
 * Pašlaik te nav būtiski paplašināta Gloss funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 * TODO pielikt izdrukātāju, kas dusmojas, ja ir gramatika.
 *
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class MLVVGloss extends Gloss
{
	/**
	 * Vai <i> tagus vajag automātiski aizvietot ar apakšsvītrām vai labāk ar
	 * <em>?
	 */
	public static boolean UNDERSCORE_FOR_CURSIVE = false;

	protected  MLVVGloss() {};
}
