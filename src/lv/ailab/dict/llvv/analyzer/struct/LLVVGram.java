package lv.ailab.dict.llvv.analyzer.struct;

import lv.ailab.dict.llvv.analyzer.PronuncNormalizer;
import lv.ailab.dict.struct.Gram;
import org.w3c.dom.Node;

/**
 * Pašlaik te nav būtiski paplašināta Gram funkcionalitāte, te ir iznestas
 * izgūšanas metodes. Veidojot gramatiku, tiek normalizētas izrunas
 * kvadrātiekavās!
 * TODO: atdalīt flagText no inflection text
 *
 * Izveidots 2019-02-25.
 * @author Lauma
 */
public class LLVVGram extends Gram
{
	public LLVVGram () {};

	public LLVVGram(Node gramNode)
	{
		freeText = gramNode.getTextContent();
		normalizePronunc();
	}

	public LLVVGram(String text)
	{
		this.freeText = text;
		normalizePronunc();
	}

	protected void normalizePronunc()
	{
		if (freeText == null) return;
		if (freeText.isEmpty()) return;
		// Apstrāde notiek, gramatiku apstrādājot pa gabaliņam un apstrādātos
		// gabaliņus pārceļot uz rezultāta mainīgo.
		StringBuilder processed = new StringBuilder();
		String toProcess = freeText;
		String currentPronunc = "";
		// Katrai izrunai viena cikla iterācija.
		while (!toProcess.isEmpty() && toProcess.contains("["))
		{
			// Pārceļ nerediģējamo gramatikas daļu pirms izrunas.
			processed.append(toProcess.substring(0, toProcess.indexOf("[") + 1));
			toProcess = toProcess.substring(toProcess.indexOf("[") + 1);
			// Izdala izrunu.
			if (toProcess.contains("]"))
			{
				currentPronunc = toProcess.substring(0, toProcess.indexOf("]"));
				toProcess = toProcess.substring(toProcess.indexOf("]"));
			}
			else
			{
				System.err.printf("\'gram\' \"%s\" satur trūkst \']\'\n", freeText);
				currentPronunc = toProcess;
				toProcess = "";
			}
			if (currentPronunc.contains("["))
				System.err.printf("\'gram\' \"%s\" ir par daudz \'[\'\n", freeText);
			// Pārveido izrunu un pārceļ to uz apstrādāto.
			processed.append(PronuncNormalizer.normalize(currentPronunc));
			// Pārceļ uz apstrādāto izrunas kvadrātiekavu.
			if (toProcess.startsWith("]"))
			{
				processed.append("]");
				toProcess = toProcess.substring(1);
			}
		}
		// Pārceļ uz apstrādāto to, kas palicis aiz pēdējās izrunas.
		if (toProcess.contains("]"))
			System.err.printf("\'gram\' \"%s\" ir par daudz \']\'\n", freeText);
		processed.append(toProcess);
		// Noliek pie vietas apstrādāto.
		freeText = processed.toString();
	}
}
