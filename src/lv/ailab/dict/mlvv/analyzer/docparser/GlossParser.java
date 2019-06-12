package lv.ailab.dict.mlvv.analyzer.docparser;

import lv.ailab.dict.mlvv.analyzer.stringutils.Editors;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.mlvv.struct.MLVVGloss;

public class GlossParser
{
	protected static GlossParser singleton = new GlossParser();
	public static GlossParser me()
	{
		return singleton;
	}

	/**
	 * Izveido glosu un normalizē tajā homonīmu indeksus.
	 */
	public MLVVGloss parse(MLVVElementFactory factory, String text)
	{
		text = Editors.replaceHomIds(text, false);
		text = Editors.translateCursive(text, MLVVGloss.UNDERSCORE_FOR_CURSIVE);
		MLVVGloss result = factory.getNewGloss();
		result.text = text;
		return result;
	}
}
