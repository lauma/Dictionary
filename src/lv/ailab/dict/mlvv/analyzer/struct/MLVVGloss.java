package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.struct.Gloss;

/**
 * Pašlaik te nav būtiski paplašināta Gloss funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class MLVVGloss extends Gloss
{
	/**
	 * Vai <i> tagus vajag automātiski aizvietot ar apakšsvītrām?
	 */
	public static boolean UNDERSCORE_FOR_CURSIVE = false;

	public MLVVGloss (String text)
	{
		super (text);
	}

	/**
	 * Izveido glosu un normalizē tajā homonīmu indeksus.
	 * @param text
	 * @return
	 */
	public static MLVVGloss parse(String text)
	{
		text = Editors.replaceHomIds(text, false);
		MLVVGloss res = new MLVVGloss(text);
		if (UNDERSCORE_FOR_CURSIVE)
			res.text = Editors.cursiveToUnderscore(res.text);
		return res;
	}

}
