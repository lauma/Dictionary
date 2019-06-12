package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.mlvv.analyzer.docparser.GlossParser;
import lv.ailab.dict.mlvv.analyzer.docparser.GramParser;
import lv.ailab.dict.mlvv.analyzer.docparser.SenseParser;
import lv.ailab.dict.mlvv.analyzer.stringutils.Finders;
import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;

import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pašlaik te nav būtiski paplašināta Sense funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 * TODO pielikt izdrukātāju, kas dusmojas, ja ir vairākas glosas.
 *
 * Izveidots 2016-11-21.
 * @author Lauma
 */
public class MLVVSense extends Sense
{
	protected MLVVSense(){};

	/**
	 * Savāc elementā izmantotos "flagText". Semikolu uzskata par atdalītāju.
	 */
	public TreeSet<String> getFlagStrings()
	{
		TreeSet<String> res = new TreeSet<>();
		if (grammar != null) res.addAll(((MLVVGram)grammar).getFlagStrings());
		if (subsenses != null) for (Sense s : subsenses)
			res.addAll(((MLVVSense)s).getFlagStrings());
		if (phrases != null) for (Phrase e : phrases)
			res.addAll(((MLVVPhrase)e).getFlagStrings());
		return res;
	}

}
