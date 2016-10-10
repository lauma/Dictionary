package lv.ailab.dict.tezaurs.analyzer;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.tezaurs.analyzer.struct.TEntry;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;

import java.io.*;
import java.util.*;

/**
 * Objekts, kas uzvāc dažādas statistikas par 1. konjugācijas verbiem Tēzaurā.
 * Pamatā paredzāts 2016. gada "Vārds un tā pētīšanas aspekti" rakstam, bet
 * nekad jau nevar zināt, kad atkal noderēs.
 *
 * Izveidots 2016-10-10.
 *
 * @author Lauma
 */
public class FirstConjStatsCollector
{
	/**
	 * Karodziņš, vai savākt 1. konjugācijas tiešos darbības vārdus un sakārtot
	 * pēc nenoteiksmes saknes.
	 */
	public final boolean collectDirectByInfinitive;
	/**
	 * Karodziņš, vai savākt 1. konjugācijas atgriezeniskos darbības vārdus un
	 * sakārtot pēc nenoteiksmes saknes.
	 */
	public final boolean collectReflByInfinitive;
	/**
	 * Karodziņš, vai savākt 1. konjugācijas tiešos darbības vārdus un sakārtot
	 * pēc visiem celmiem.
	 */
	public final boolean collectDirectByStems;
	/**
	 * Karodziņš, vai savākt 1. konjugācijas atgriezeniskos darbības vārdus un
	 * sakārtot pēc visiem celmiem.
	 */
	public final boolean collectReflByStems;
	/**
	 * "Locīt kā" -> attiecīgais darbības vārds.
	 */
	public TreeMap<String, TreeSet<String>> directByInf = new TreeMap<>();
	/**
	 * "Locīt kā" -> attiecīgais darbības vārds.
	 */
	public TreeMap<String, TreeSet<String>> reflByInf = new TreeMap<>();

	/**
	 * Celmi -> attiecīgais darbības vārds.
	 * TODO: adekvāta celmu reprezentācija.
	 */
	public TreeMap<String, TreeSet<String>> directbyStems = new TreeMap<>();

	/**
	 * Celmi -> attiecīgais darbības vārds.
	 * TODO: adekvāta celmu reprezentācija.
	 */
	public TreeMap<String, TreeSet<String>> reflByStems = new TreeMap<>();


	public FirstConjStatsCollector(boolean collectDirect,
			boolean collectRefl)
	{
		this.collectDirectByInfinitive = collectDirect;
		this.collectDirectByStems = collectDirect;

		this.collectReflByInfinitive = collectRefl;
		this.collectReflByStems = collectRefl;
	}

	public FirstConjStatsCollector(
			boolean collectDirectByInfinitive, boolean collectReflByInfinitive,
			boolean collectDirectByStems, boolean collectReflByStems)
	{
		this.collectDirectByInfinitive = collectDirectByInfinitive;
		this.collectReflByInfinitive = collectReflByInfinitive;
		this.collectDirectByStems = collectDirectByStems;
		this.collectReflByStems = collectReflByStems;
	}

	/**
	 * Apstrādāt doto šķirkli - papildināt vācamās statistikas ar visu
	 * nepieciešamo informāciju par šo šķirkli.
	 * @param entry	apstrādājamais šķirklis
	 */
	public void countEntry( TEntry entry)
	{
		// Uzmanīgi, šī ir optimizācija ātrumam: if nosacījums daļēji dublē
		// iekšā esošos nosacījumus.
		if (collectDirectByInfinitive || collectReflByInfinitive ||
				collectDirectByStems || collectReflByStems)
			for (Header h : entry.getAllHeaders())
		{
			if (h.gram == null) continue;
			if (h.gram.flags == null) continue;
			// Ir atpazīts, ka locīs kā tiešo.
			if (h.gram.getDirectParadigms().contains(15))
			{
				if (collectDirectByInfinitive) addByInf(directByInf, h);
				if (collectReflByStems) addByStem(directbyStems, h);
			}
			// Ir atpazīts, ka locīs kā atgriezenisko.
			if (h.gram.getDirectParadigms().contains(18))
			{
				if (collectReflByInfinitive) addByInf(reflByInf, h);
				if (collectReflByStems) addByStem(reflByStems, h);
			}
		}
	}

	/**
	 * Pievienot informāciju par doto Header objektu directByInf vai reflByInf
	 * statistiku vākšanas objektā.
	 * @param where	datu struktūra, kuru nepieciešams papildināt (directByInf
	 *              vai reflByInf)
	 * @param what	Header, par kuru informācija jāpievieno papildināmajai datu
	 *              struktūrai
	 */
	private static void addByInf(TreeMap<String, TreeSet<String>> where, Header what)
	{
		Set<String> keys = what.gram.flags.getAll(TKeys.INFLECT_AS);
		if (keys == null) keys = new HashSet<>();
		if (keys.isEmpty()) keys.add(" ");
		for (String key : keys)
		{
			TreeSet<String> values = where.get(key);
			if (values == null) values = new TreeSet<>();
			values.add(what.lemma.text);
			where.put(key, values);
		}
	}

	/**
	 * Pievienot informāciju par doto Header objektu directByStem vai reflByStem
	 * statistiku vākšanas objektā.
	 * @param where	datu struktūra, kuru nepieciešams papildināt (directByStem
	 *              vai reflByStem)
	 * @param what	Header, par kuru informācija jāpievieno papildināmajai datu
	 *              struktūrai
	 */
	private static void addByStem(TreeMap<String, TreeSet<String>> where, Header what)
	{
		String key = "Infinitive=";
		Set<String> stemKeys = what.gram.flags.getAll(TKeys.INFINITIVE_STEM);
		key = key + stemKeys.stream().sorted().reduce((a, b) -> a + "," + b).orElse("??")
				+ "; Present=";
		stemKeys = what.gram.flags.getAll(TKeys.PRESENT_STEM);
		key = key + stemKeys.stream().sorted().reduce((a, b) -> a + "," + b).orElse("??")
				+ "; Past=";
		stemKeys = what.gram.flags.getAll(TKeys.PAST_STEM);
		key = key + stemKeys.stream().sorted().reduce((a, b) -> a + "," + b).orElse("??");

		TreeSet<String> values = where.get(key);
		if (values == null) values = new TreeSet<>();
		values.add(what.lemma.text);
		where.put(key, values);
	}

	/**
	 * Izdrukāt visu savākto. Datu objekti netiek iztīrīti pēc drukāšanas.
	 * @param folderPath		ceļš, kur drukāt
	 * @param fileNamePrefix	faila prefikss, kuru izmantot izveidotajiem
	 *                          failiem (piemēram, "entities" vai "references")
	 * @throws IOException
	 */
	public void printContents(String folderPath, String fileNamePrefix)
	throws IOException
	{
		if (collectDirectByInfinitive)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_direct-by-roots.txt"), "UTF-8"));
			printInfMap(out, directByInf);
			out.flush();
			out.close();
		}

		if (collectDirectByStems)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_direct-by-stems.txt"), "UTF-8"));
			printStemMap(out, directbyStems);
			out.flush();
			out.close();
		}

		if (collectReflByInfinitive)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_refl-by-roots.txt"), "UTF-8"));
			printInfMap(out, reflByInf);
			out.flush();
			out.close();
		}

		if (collectReflByStems)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_refl-by-stems.txt"), "UTF-8"));
			printStemMap(out, reflByStems);
			out.flush();
			out.close();
		}
	}

	/**
	 * Ertības metode, kas tiek izmantota directByInf un reflByInf izdrukāšanai.
	 * @param where	plusma/fails, kur notiks izdeukāšana
	 * @param what	datu struktūra, kuru nepieciešams izdrukāt (directByInf vai
	 *              reflByInf)
	 * @throws IOException
	 */
	protected static void printInfMap (
			BufferedWriter where, TreeMap<String, TreeSet<String>> what)
	throws IOException
	{
		if (what != null && what.size() > 0)
		{
			where.write(what.keySet().stream().map(k ->
					k + "\t" + what.get(k).stream()
								.reduce((v1, v2) -> v1 + ", " + v2)
								.orElse(""))
					.reduce((p1, p2) -> p1 + "\n" + p2).orElse(""));
			where.write("\n");
		}
	}

	/**
	 * Ertības metode, kas tiek izmantota directByStem un reflByStem
	 * izdrukāšanai.
	 * @param where	plusma/fails, kur notiks izdeukāšana
	 * @param what	datu struktūra, kuru nepieciešams izdrukāt (directByStem vai
	 *              reflByStem)
	 * @throws IOException
	 */
	protected static void printStemMap (
			BufferedWriter where, TreeMap<String, TreeSet<String>> what)
	throws IOException
	{
		if (what != null && what.size() > 0)
		{
			where.write(what.keySet().stream().map(k ->
					k + "\t" +
							what.get(k).stream()
									.reduce((v1, v2) -> v1 + ", " + v2)
									.orElse(""))
					.reduce((p1, p2) -> p1 + "\n" + p2).orElse(""));
			where.write("\n");
		}
	}
}
