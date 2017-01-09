package lv.ailab.dict.mlvv.analyzer.struct;

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
		MLVVGloss res = new MLVVGloss(text);
		res.replaceHomIds();
		return res;
	}

	protected void replaceHomIds()
	{
		text = text.replaceAll("(?<=\\p{L})\\s*<sup>\\s*1</sup>", " [1]");
		text = text.replaceAll("(?<=\\p{L})\\s*<sup>\\s*2</sup>", " [2]");
		text = text.replaceAll("(?<=\\p{L})\\s*<sup>\\s*3</sup>", " [3]");
	}
}
