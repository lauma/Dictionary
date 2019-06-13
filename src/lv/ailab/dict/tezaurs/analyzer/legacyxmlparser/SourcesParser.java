package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.struct.Sources;
import lv.ailab.dict.tezaurs.struct.TElementFactory;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.LinkedList;

public class SourcesParser
{
	protected SourcesParser(){};
	protected static SourcesParser singleton = new SourcesParser();
	public static SourcesParser me()
	{
		return singleton;
	}

	public Sources parseSources(Node avotsNode)
	{
		Sources result = TElementFactory.me().getNewSources();
		result.orig = avotsNode.getTextContent();
		result.s = parseSources(result.orig);
		return result;
	}

	protected static LinkedList<String> parseSources (String sourcesText)
	{
		if (sourcesText == null) return null;
		if (sourcesText.startsWith("["))
			sourcesText = sourcesText.substring(1);
		if (sourcesText.endsWith("]"))
			sourcesText = sourcesText.substring(0, sourcesText.length() - 1);
		sourcesText = sourcesText.trim();
		if (sourcesText.isEmpty()) return null;

		LinkedList<String> res = new LinkedList<>();
		res.addAll(Arrays.asList(sourcesText.split(",\\s*")));
		return res;
	}

}
