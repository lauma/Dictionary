package lv.ailab.dict.mlvv.analyzer.validation;

import lv.ailab.dict.mlvv.analyzer.struct.MLVVEntry;
import lv.ailab.dict.struct.Entry;
import lv.ailab.dict.struct.Header;

import java.util.LinkedList;

/**
 * Samestas dažādas sīkpārbaudes šķirkļa datiem. "true" nozīmē labu šķirkli,
 * "false" sliktu.
 *
 * Izveidots 2017-05-23.
 *
 * @author Lauma
 */
public class IndividualChecks
{
	/**
	 * Vai šķirklim ir galva un šķirkļavārds?
	 * @param e	pārbaudāmais šķirklis
	 * @return	vai pārbaude ir izieta
	 */
	public static boolean hasHeadWord(Entry e)
	{
		return e != null && e.head != null && e.head.lemma != null
				&& e.head.lemma.text != null && !e.head.lemma.text.isEmpty();
	}

	/**
	 * Vai šķirkļa hederim ir šķirkļavārds?
	 * @param h	pārbaudāmais hederis
	 * @return	vai pārbaude ir izieta
	 */
	public static boolean hasHeadWord(Header h)
	{
		return h != null && h.lemma != null && h.lemma.text != null
				&& !h.lemma.text.isEmpty();
	}

	/**
	 * Vai pamata šķirkļavārds satur tikai atļautos simbolus?
	 * @param lemmaText	pārbaudāmais šķirkļavārds
	 * @return	vai pārbaude ir izieta
	 */
	public static boolean isHeadWordGood(String lemmaText)
	{
		return  lemmaText.matches(
				"^-?[a-zA-ZĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽž][a-zA-Z0-9ĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽž /.\\-]*$");
	}

	/**
	 * Vai šķirklī pirms frazeoloģismiem ir dotas nozīmes?
	 * @param e	pārbaudāmais šķirklis
	 * @return	vai pārbaude ir izieta
	 */
	public static boolean hasSensesBeforePhrasals(MLVVEntry e)
	{
		return (e.senses != null && !e.senses.isEmpty()) ||
			(e.phrases == null || e.phrases.isEmpty()) &&
			(e.phraseology == null || e.phraseology.isEmpty());

	}

	/**
	 * Vai teksta virknē __ ir pāra skaitā?
	 * @param text	pārbaudāmais teksts
	 * @return	vai pārbaude ir izieta
	 */
	public static boolean hasPairedUnderscores(String text)
	{
		return text == null || text.isEmpty() ||
				(text.length() - text.replace("__", "").length()) % 2 == 0;
	}

	/**
	 * Vai teksta virknē iekavas ()[]{} ir pareizi lietotas?
	 * @param text	pārbaudāmais teksts
	 * @return	vai pārbaude ir izieta
	 */
	public static boolean hasBalancedParentheses(String text)
	{
		if (text == null) return true;
		LinkedList<Character> stack = new LinkedList<>();
		for (char c : text.toCharArray())
		{
			if (c == '(' || c == '[' || c == '{') stack.push(c);
			if (c == ')' && (stack.isEmpty() || stack.pop() != '(')) return false;
			if (c == ']' && (stack.isEmpty() || stack.pop() != '[')) return false;
			if (c == '}' && (stack.isEmpty() || stack.pop() != '{')) return false;
		}

		return stack.isEmpty();
	}
}