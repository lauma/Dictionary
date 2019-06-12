package lv.ailab.dict.mlvv.analyzer.validation;

import lv.ailab.dict.mlvv.struct.MLVVEntry;
import lv.ailab.dict.mlvv.struct.MLVVGloss;
import lv.ailab.dict.struct.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Rīks izgūto šķirkļu pārbaudīšanai. Iekšējā atmiņā uzkrāj līdz šim redzētos
 * šķirkļus un to homonīmu indeksus, lai pārliecinātos par unikalitāti.
 *
 * Izveidots 2017-05-18.
 *
 * @author Lauma
 */
public class Validator
{
	/**
	 * Šķirkļavārds -> atrastie homonīmu indeksi
	 */
	protected HashMap<String, ArrayList<Integer>> entrywords = new HashMap<>();
	/**
	 * Reģistrs, kādā kārtībā šķirkļi tika pievienoti.
	 */
	protected ArrayList<String> entrywordsInOrder = new ArrayList<>();
	/**
	 * Iepriekšējā apstrādātā sķirkļa šķirkļavārds<homonīma indekss>.
	 */
	protected String previousEntryWord = "";
	/**
	 * Šķirkļu skaits.
	 */
	protected int allEntryCount = 0;
	/**
	 * Šķirkļu skaits, kam nebija sķirkļavārda.
	 */
	protected int headlessEntryCount = 0;

	/**
	 * Pārbauda konkrētu šķirkli un reģistrē to atmiņā.
	 * @param e	pārbaudāmais šķirklis
	 */
	public void checkEntry(MLVVEntry e)
	{
		allEntryCount++;

		// Vai ir galva?
		if (!IndividualChecks.hasHeadWord(e))
		{
			System.out.printf(
					"Konstatēts šķirklis bez šķirkļavārda. Iepriekšējais šķirklis bija %s.\n", previousEntryWord);
			headlessEntryCount++;
			// Bez galvas nava labi.
			return;
		}

		// Ja galva ir tad, var pārbaudīt visu ar galvu saistīto.
		int homId = checkLemmasHomIds(e);
		String entryword = e.head.lemma.text;

		// Tālāk taisa šķirkļa ķermeņa pārbaudes.
		if (!IndividualChecks.hasSensesBeforePhrasals(e))
			System.out.printf(
					"Šķirklim %s<%s> norādītas frāzes, bet nav nozīmes!\n",
					entryword, homId);
		checkTexts(e, entryword + "<" + homId + ">");
		// Lai nākamais šķirklis zina, kā sauca iepriekšējo.
		previousEntryWord = entryword + "<" + homId + ">";
	}

	/**
	 * Pārbauda ar lemmām un šķirkļavārdiem saistīto loģiku.
	 * Iznests no checkEntry.
	 * @param e	pārbaudāmais šķirklis
	 * @return	izparsētais/pieņemtais homonīma indekss
	 */
	protected int checkLemmasHomIds (Entry e)
	{
		// Vai ir homonīma indekss?
		String entryword = e.head.lemma.text;
		String homIdStr = e.homId;
		if (homIdStr == null || homIdStr.isEmpty()) homIdStr = "0";
		int homId = 0;
		try
		{
			homId = Integer.parseInt(homIdStr);
		} catch (NumberFormatException nfe)
		{
			System.out.printf("Šķirklī %s ir homonīma indekss %s, kas nav skaitlis.\n",
					entryword, homIdStr);
		}

		// Vai galva un homonīma indekss saskan ar iepriekš bijušajiem šķirkļiem?
		if (entrywords.containsKey(entryword))
		{
			ArrayList<Integer> homIds = entrywords.get(entryword);
			int lastId = homIds.get(homIds.size()-1);
			if (lastId == 0)
			{
				if (homId == 0)
					System.out.printf("Šķirklis %s atkārtojas vairākkārt.\n",
							entryword);
				else System.out.printf("Šķirklim %s ir gan nenumurēts homonīms, gan homonīms ar numuru %s.\n",
						entryword, homId);
			}
			else if (lastId + 1 != homId)
				System.out.printf("Šķirklim %s pēc homonīma ar numuru %s seko %s.\n",
						entryword, lastId, homId);
			homIds.add(homId);
			entrywords.put(entryword, homIds);
		}
		else
		{
			if (homId > 1)
				System.out.printf("Šķirklim %s mazākais homonīma numurs ir %s.\n",
						entryword, homId);
			entrywords.put(entryword, new ArrayList<>());
			entrywords.get(entryword).add(homId);
			entrywordsInOrder.add(entryword);
		}

		// Vai šķirkļavārdi sastāv no labiem burtiem?
		for (Header h : e.getAllHeaders())
		{
			if (!IndividualChecks.hasHeadWord(h) || !IndividualChecks
					.isHeadWordGood(h.lemma.text))
				System.out.printf(
						"Šķirklim %s<%s> šķirkļavārds %s satur neatļautus burtus.\n",
						entryword, homId, h.lemma.text);
		}

		return homId;
	}

	/**
	 * Pārbauda atbilstošajos laukos, vai kursīvs ir pareizi sanācis.
	 * @param e	pārbaudāmais šķirklis
	 * @param debugEntryWord	šķirkļavārds<homonīma indekss>, lai būtu ko
	 *                          izdrukāt, ja ir kļūda
	 */
	protected void checkTexts(MLVVEntry e, String debugEntryWord)
	{
		if (e.senses != null) for (Sense s : e.senses)
			checkTexts(s, debugEntryWord);
		if (e.phrases != null) for (Phrase p : e.phrases)
			checkTexts(p, debugEntryWord);
		/*if (e.phraseology != null) for (Phrase p : e.phraseology)
			checkTexts(p, debugEntryWord);*/

		if (MLVVEntry.UNDERSCORE_FOR_ETYMOLOGY_CURSIVE)
		{
			if (!IndividualChecks.hasPairedUnderscores(e.etymology))
				System.out.printf("Šķirklī %s ir nesapārotas __ cilmē \"%s\".\n",
						debugEntryWord, e.etymology);
		}
		else
		{
			if (!IndividualChecks.hasPairedEmTags(e.etymology))
				System.out.printf("Šķirklī %s ir nekorekti <em></em> cilmē \"%s\".\n",
						debugEntryWord, e.etymology);
		}
		if (!IndividualChecks.hasBalancedParentheses(e.etymology))
			System.out.printf("Šķirklī %s ir nesapārotas iekavas cilmē \"%s\".\n",
					debugEntryWord, e.etymology);

		if (MLVVEntry.UNDERSCORE_FOR_NORMATIVE_CURSIVE)
		{
			if (!IndividualChecks.hasPairedUnderscores(e.normative))
				System.out.printf("Šķirklī %s ir nesapārotas __ normatīvajā komentārā \"%s\".\n",
						debugEntryWord, e.normative);
		}
		else
		{
			if(!IndividualChecks.hasPairedEmTags(e.normative))
				System.out.printf("Šķirklī %s ir nekorekti <em></em> normatīvajā komentārā \"%s\".\n",
						debugEntryWord, e.normative);
		}
		if (!IndividualChecks.hasBalancedParentheses(e.normative))
			System.out.printf("Šķirklī %s ir nesapārotas iekavas normatīvajā komentārā \"%s\".\n",
					debugEntryWord, e.normative);
	}

	/**
	 * Pārbauda atbilstošajos laukos, vai kursīvs ir pareizi sanācis.
	 * @param s	pārbaudāmā nozīme
	 * @param debugEntryWord	šķirkļavārds<homonīma indekss>, lai būtu ko
	 *                          izdrukāt, ja ir kļūda
	 */	protected void checkTexts(Sense s, String debugEntryWord)
	{
		for (Gloss glossVariant : s.gloss)
		{
			if (MLVVGloss.UNDERSCORE_FOR_CURSIVE)
			{
				if (!IndividualChecks.hasPairedUnderscores(glossVariant.text))
					System.out.printf("Šķirklī %s ir nesapārotas __ glosā \"%s\".\n",
							debugEntryWord, glossVariant.text);
			}
			else
			{
				if (!IndividualChecks.hasPairedEmTags(glossVariant.text))
					System.out.printf("Šķirklī %s ir nekorekti <em></em> glosā \"%s\".\n",
							debugEntryWord, glossVariant.text);
			}
			if (!IndividualChecks.hasBalancedParentheses(glossVariant.text))
				System.out.printf("Šķirklī %s ir nesapārotas iekavas glosā \"%s\".\n",
						debugEntryWord, glossVariant.text);
		}

		if (s.subsenses != null) for (Sense sub : s.subsenses)
			checkTexts(sub, debugEntryWord);
		if (s.phrases != null) for (Phrase p : s.phrases)
			checkTexts(p, debugEntryWord);
	}

	/**
	 * Pārbauda atbilstošajos laukos, vai kursīvs ir pareizi sanācis.
	 * @param p	pārbaudāmā frāze
	 * @param debugEntryWord	šķirkļavārds<homonīma indekss>, lai būtu ko
	 *                          izdrukāt, ja ir kļūda
	 */	protected void checkTexts(Phrase p, String debugEntryWord)
	{
		for (String t : p.text)
		{
			if (!IndividualChecks.hasBalancedParentheses(t))
				System.out.printf(
						"Šķirklī %s ir nesapārotas iekavas piemēra tekstā \"%s\".\n",
								debugEntryWord, t);
		}

		if (p.subsenses != null) for (Sense sub : p.subsenses)
			checkTexts(sub, debugEntryWord);
	}

	/**
	 * Pārbaudes atmiņā reģistrētajam vārdnīcas stāvoklim: paziņo, kuriem
	 * šķirkļiem šobrīd lielākais homonīma indekss ir 1.
	 */
	public void checkAfterAll()
	{
		for (String entryword : entrywordsInOrder)
		{
			ArrayList<Integer> homIds = entrywords.get(entryword);
			if (homIds.get(homIds.size()-1) == 1)
					System.out.printf(
							"Šķirklim %s lielākais homonīma numurs ir 1.\n",
							entryword);
		}
	}

	/**
	 * Izsdrukā šķirkļu skaitus.
	 */
	public void printStats()
	{
		System.out.printf("%s šķirkļi kopā.\n", allEntryCount);
		System.out.printf("%s šķirkļi bez šķirkļavādriem.\n", headlessEntryCount);
	}
}
