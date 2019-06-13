package lv.ailab.dict.tezaurs.struct;

import lv.ailab.dict.struct.*;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.tezaurs.struct.constants.flags.TKeys;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Tēzaura šķirklis, papildināts ar melnajiem sarakstiem.
 */
public class TEntry extends Entry
{
	public final static String BLACKLIST_LOCATION = "saraksti/blacklist.txt";
	/**
	 * Lemmas šķirkļiem, kurus šobrīd ignorē (neapstrādā).
	 * Skatīt arī inBlacklist().
	 */
	protected static HashMap<String, HashSet<String>> blacklist = initBlacklist();

	protected TEntry(){};

	public boolean inBlacklist()
	{
		HashSet<String> homs = blacklist.get(head.lemma.text);
		if (homs == null) return false;
		if (homs.contains("-1")) return true;
		return homs.contains(homId);
		//return blacklist.contains(head.lemma.text);
	}
	
	/**
	 * Constructing a list of lemmas to ignore - basically meant to ease
	 * development and testing.
	 * Blacklist file format - one word (lemma) per line with one optional space
	 * separated homonym index. No homonym index mean all homonyms.
	 */
	protected static HashMap<String, HashSet<String>> initBlacklist()
	{
		HashMap<String, HashSet<String>> blist = new HashMap<>();
		BufferedReader input;
		try {
			// Blacklist file format - one word (lemma) per line with optional
			// space separated homonym index. No homonym index mean all homonyms.
			input = new BufferedReader(
					new InputStreamReader(
					new FileInputStream(BLACKLIST_LOCATION), StandardCharsets.UTF_8));
			String line;
			while ((line = input.readLine()) != null)
			{
				line = line.trim();
				if (line.contains(" "))
				{
					String[] parts = line.split(" ");
					HashSet<String> homs = blist.get(parts[0]);
					if (homs == null) homs = new HashSet<>();
					if (!homs.contains("-1"))homs.add(parts[1]);
					blist.put(parts[0].trim(), homs);
				}
				else blist.put(line, new HashSet<String>(){{add("-1");}});
			}		
			input.close();
		} catch (Exception e)
		{
			System.err.println("Ignorējamo šķirkļu saraksts netiek lietots.");
		} //TODO - any IO issues ignored
		return blist;
	}

	public boolean hasUnparsedGram()
	{
		return TEntry.hasUnparsedGram(this);
	}
	public static boolean hasUnparsedGram(Entry entry)
	{
		if (entry == null) return false;
		if (THeader.hasUnparsedGram(entry.head)) return true;
		if (entry.senses != null) for (Sense s : entry.senses)
		{
			if (TSense.hasUnparsedGram(s)) return true;
		}
		if (entry.phrases != null) for (Phrase p : entry.phrases)
		{
			if (TPhrase.hasUnparsedGram(p)) return true;
		}
		if (entry.derivs != null) for (Header h : entry.derivs)
		{
			if (THeader.hasUnparsedGram(h)) return true;
		}
		return false;
	}

	/**
	 * Pieņemot, ka šķirklis ir apstrādāts un pabeigts, pārbauda dažādas loģikas
	 * lietas, kas varētu norādīt uz kļūdainu programmas loģiku apstrādes
	 * laikā.
	 */
	public void printConsistencyReport()
	{
		Flags usedFlags = getUsedFlags();
		if (usedFlags.test(TFeatures.UNCLEAR_PARADIGM)
				&& !hasMultipleParadigms()
				&& !getMentionedParadigms().contains(0))
			System.err.printf(
					"Šķirklī \"%s\" ir neskaidro paradigmu karodziņš, bet nav vairāku paradigmu.\n",
					head.lemma.text);
		if (usedFlags.testKey(TKeys.ETYMOLOGY))
			System.err.printf(
					"Šķirklī \"%s\" ir etimoloģijas karodziņš.\n",
					head.lemma.text);
	}
	public int countEmptyGloss()
	{
		return countEmptyGloss(this);
	}

	public static int countEmptyGloss(Entry e)
	{
		if (e == null) return 0;
		int res = 0;
		if (e.senses != null) for (Sense s : e.senses)
			 res = res + TSense.countEmptyGloss(s);
		if (e.phrases != null) for (Phrase p : e.phrases)
			res = res + TPhrase.countEmptyGloss(p);
		return res;
	}

}
