package lv.ailab.dict.tezaurs.analyzer.legacyxmlparser;

import lv.ailab.dict.struct.Phrase;
import lv.ailab.dict.struct.Sense;
import lv.ailab.dict.tezaurs.struct.TSense;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

public class LoadingUtils
{
	/**
	 * Loads contents of g_n or g_an field into LinkedList.
	 * Reads the information about the (multiple) word senses for that entry.
	 * NB! they may have their own 'gram.' entries, not sure how best to
	 * reconcile.
	 * @param lemma is used for grammar parsing.
	 */
	public static LinkedList<Sense> loadSenses(Node allSenses, String lemma)
	{
		//if (senses == null) senses = new LinkedList<TSense>();
		LinkedList<Sense> res = new LinkedList<>();
		NodeList senseNodes = allSenses.getChildNodes(); 
		for (int i = 0; i < senseNodes.getLength(); i++)
		{
			Node sense = senseNodes.item(i);
			
			// We're ignoring the number of the senses - it's in "nr" field, we
			//assume (not tested) that it matches the order in file
			if (sense.getNodeName().equals("n"))
			{
				TSense newSense = SenseParser.me().parseSense(sense, lemma);
				res.add(newSense);
				//if (newSense.gloss == null)
				//	System.out.println ("Nav gloss! "+ lemma);
			}
			else if (!sense.getNodeName().equals("#text")) // Text nodes here are ignored.
				System.err.printf(
					"%s lauks %s netiek apstrādāts, jo sagaida tikai 'n'.\n",
					allSenses.getNodeName(), sense.getNodeName());
		}
		return res;
	}
	
	/**
	 * Load contents of g_fraz or g_piem field into LinkedList.
	 * @param lemma is used for grammar parsing.
	 */
	public static LinkedList<Phrase> loadPhrases(
			Node allPhrases, String lemma, String subElemName)
	{
		LinkedList<Phrase> res = new LinkedList<>();
		NodeList phraseNodes = allPhrases.getChildNodes(); 
		for (int i = 0; i < phraseNodes.getLength(); i++)
		{
			Node phrase = phraseNodes.item(i);
			if (phrase.getNodeName().equals(subElemName))
				res.add(PhraseParser.me().parsePhrase(phrase, lemma));
			else if (!phrase.getNodeName().equals("#text")) // Text nodes here are ignored.
				System.err.printf(
					"%s lauks %s netiek apstrādāts, jo sagaida tikai '%s'.\n",
					allPhrases.getNodeName(), phrase.getNodeName(), subElemName);
		}
		return res;
	}
}
