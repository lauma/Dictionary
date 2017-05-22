package lv.ailab.dict.mlvv.analyzer;

import lv.ailab.dict.mlvv.analyzer.struct.MLVVEntry;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Rīks izgūto šķirkļu pārbaudīšanai. Iekšējā atmiņā uzkrāj līdz šim redzētos
 * šķirkļus un to homonīmu indeksus, lai pārliecinātos par unikalitāti.
 *
 * Individuālājās šķirkļa pārbaudēs "true" nozīmē labu šķirkli, "false" sliktu.
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
	protected String previousEntryWord = "";
	protected int allEntryCount = 0;
	protected int headlessEntryCount = 0;

	public void checkEntry(MLVVEntry e)
	{
		allEntryCount++;
		// Vai ir galva?
		if (!hasHeadWord(e))
		{
			System.out.printf(
					"Konstatēts šķirklis bez šķirkļavārda. Iepriekšējais šķirklis bija %s.\n", previousEntryWord);
			headlessEntryCount++;
			return;
		}

		// Labi, ja ir galva, vai ir homonīma indekss?
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
			entrywords.put(entryword, new ArrayList<Integer>());
			entrywords.get(entryword).add(homId);
		}
		// Vai šķirkļavārds sastāv no labiem burtiem?
		// TODO: pārbaudīt visus šķirkļavārdus?
		if (!isHeadWordGood(e))
			System.out.printf(
					"Šķirklim %s<%s> šķirkļavārds satur neatļautus burtus.\n",
					entryword, homId);

		// Tālāk taisa struktūras pārbaudes.

		if (!hasSensesBeforePhrasals(e))
			System.out.printf(
					"Šķirklim %s<%s> norādītas frāzes, bet nav nozīmes!\n",
					entryword, homId);


		previousEntryWord = entryword + "<" + homId + ">";
	}
	/**
	 * Vai šķirklim ir galva un šķirkļavārds?
	 * @param e	pārbaudāmais šķirklis
	 * @return	vai pārbaude ir izieta
	 */
	public boolean hasHeadWord(MLVVEntry e)
	{
		return e != null && e.head != null && e.head.lemma != null
				&& e.head.lemma.text != null && !e.head.lemma.text.isEmpty();
	}
	/**
	 * Vai pamata šķirkļavārds satur tikai atļautos simbolus?
	 * @param e	pārbaudāmais šķirklis
	 * @return	vai pārbaude ir izieta
	 */
	public boolean isHeadWordGood(MLVVEntry e)
	{
		return hasHeadWord(e) &&
				e.head.lemma.text.matches("^[a-zA-Z0-9ĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽž /-]+$");
	}

	/**
	 * Vai šķirklī pirms frazeoloģismiem ir dotas nozīmes?
	 * @param e	pārbaudāmais šķirklis
	 * @return	vai pārbaude ir izieta
	 */
	public boolean hasSensesBeforePhrasals(MLVVEntry e)
	{
		return (e.senses != null && !e.senses.isEmpty()) ||
			(e.phrases == null || e.phrases.isEmpty()) &&
			(e.phraseology == null || e.phraseology.isEmpty());

	}

	public void printStats()
	{
		System.out.printf("%s šķirkļi kopā.\n", allEntryCount);
		System.out.printf("%s šķirkļi bez šķirkļavādriem.\n", headlessEntryCount);
	}
}
