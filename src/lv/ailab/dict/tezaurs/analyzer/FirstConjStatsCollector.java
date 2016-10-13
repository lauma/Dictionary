package lv.ailab.dict.tezaurs.analyzer;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.flagconst.Keys;
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
	 * Karodziņš, vai savākt 1. konjugācijas tiešos darbības vārdus un sakārtot
	 * pēc priedēkļiem (un nenoteiksmes saknes).
	 */
	public final boolean collectDirectByPrefix;
	/**
	 * Karodziņš, vai savākt 1. konjugācijas atgriezeniskos darbības vārdus un
	 * sakārtot pēc priedēkļiem (un nenoteiksmes saknes).
	 */
	public final boolean collectReflByPrefix;

	/**
	 * Karodziņš, vai savākt lemmas, kas varētu būt 1. konjugācijas tiešie
	 * verbi.
	 */
	public final boolean collectDirectPotential;
	/**
	 * Karodziņš, vai savākt lemmas, kas varētu būt 1. konjugācijas
	 * atgriezeniskie verbi.
	 */
	public final boolean collectReflPotential;

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

	/**
	 * Priedēkļi, pēc kuriem tiek kārtoti darbības vārdi.
	 */
	public TreeSet<String> prefixes = new TreeSet<>();
	/**
	 * "Locīt kā" -> priedēklis -> attiecīgie darbības vārdi
	 */
	public TreeMap<String,TreeMap<String, TreeSet<String>>> directByPrefix = new TreeMap<>();
	/**
	 * "Locīt kā" -> priedēklis -> attiecīgie darbības vārdi
	 */
	public TreeMap<String,TreeMap<String, TreeSet<String>>> reflByPrefix = new TreeMap<>();

	/**
	 * Nenoteiksmes celmi.
	 */
	public TreeSet<String> infinitiveStems = new TreeSet<>();
	/**
	 * Vārdi, kam nav paradigmas un kas beidzas ar -t.
	 */
	public TreeSet<String> potentialDirect = new TreeSet<>();
	/**
	 * Vārdi, kam nav paradigmas un kas beidzas ar -ties.
	 */
	public TreeSet<String> potentialRefl = new TreeSet<>();

	public FirstConjStatsCollector(boolean collectDirect,
			boolean collectRefl)
	{
		this.collectDirectByInfinitive = collectDirect;
		this.collectDirectByStems = collectDirect;

		this.collectReflByInfinitive = collectRefl;
		this.collectReflByStems = collectRefl;

		this.collectDirectByPrefix = collectDirect;
		this.collectReflByPrefix = collectRefl;

		this.collectDirectPotential = collectDirect;
		this.collectReflPotential = collectRefl;
	}

	public FirstConjStatsCollector(
			boolean collectDirectByInfinitive, boolean collectReflByInfinitive,
			boolean collectDirectByStems, boolean collectReflByStems,
			boolean collectDirectByPrefix, boolean collectReflByPrefix,
			boolean collectDirectPotential, boolean collectReflPotential)
	{
		this.collectDirectByInfinitive = collectDirectByInfinitive;
		this.collectReflByInfinitive = collectReflByInfinitive;
		this.collectDirectByStems = collectDirectByStems;
		this.collectReflByStems = collectReflByStems;
		this.collectDirectByPrefix = collectDirectByPrefix;
		this.collectReflByPrefix = collectReflByPrefix;
		this.collectDirectPotential = collectDirectPotential;
		this.collectReflPotential = collectReflPotential;
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
				collectDirectByStems || collectReflByStems ||
				collectDirectByPrefix || collectReflByPrefix ||
				collectDirectPotential || collectReflPotential)
			for (Header h : entry.getAllHeaders())
		{
			Set<Integer> paradigms = null;
			if (h.gram != null) paradigms = h.getDirectParadigms();
			if (paradigms == null || paradigms.isEmpty())
			{
				if (collectDirectPotential && h.lemma.text.endsWith("t"))
					potentialDirect.add(h.lemma.text);
				if (collectReflPotential && h.lemma.text.endsWith("ties"))
					potentialRefl.add(h.lemma.text);
			}
			if (h.gram == null) continue;
			if (h.gram.flags == null) continue;

			if (paradigms.contains(15)) // Ir atpazīts, ka locīs kā tiešo.
			{
				if (collectDirectByInfinitive) addByInf(directByInf, h);
				if (collectReflByStems) addByStem(directbyStems, h);
				if (collectDirectByPrefix) addByPrefix(directByPrefix, h);
			}

			if (paradigms.contains(18)) // Ir atpazīts, ka locīs kā atgriezenisko.
			{
				if (collectReflByInfinitive) addByInf(reflByInf, h);
				if (collectReflByStems) addByStem(reflByStems, h);
				if (collectReflByPrefix) addByPrefix(reflByPrefix, h);
			}

			if (collectDirectPotential || collectReflPotential)
			{
				Set<String> stems = h.gram.flags.getAll(TKeys.INFINITIVE_STEM);
				Set<String> prefs = h.gram.flags.getAll(TKeys.VERB_PREFIX);
				if (stems != null)
				{
					if (prefs == null || prefs.isEmpty())
						infinitiveStems.addAll(stems);
					else for (String stem : stems) for (String pref : prefs)
						if (stem.startsWith(pref))
							infinitiveStems.add(stem.substring(pref.length()));

				}
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
	protected static void addByInf(
			TreeMap<String, TreeSet<String>> where, Header what)
	{
		Set<String> keys = what.gram.flags.getAll(TKeys.INFLECT_AS);
		if (keys == null) keys = new HashSet<>();
		if (keys.isEmpty()) keys.add("0");
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
	protected static void addByStem(
			TreeMap<String, TreeSet<String>> where, Header what)
	{
		String key = "";
		final String prefix;
		Set<String> prefixes = what.gram.flags.getAll(Keys.VERB_PREFIX);
		if (prefixes != null)
		{
			if (prefixes.size() > 1)
				System.out.println("Pie \"" + what.lemma.text
						+ "\" ir vairāk kā 1 priedēklis: "
						+ prefixes.stream().sorted().reduce((p1, p2) -> p1 + ", " + p2).orElse("")
						+ "!");
			prefix = prefixes.stream().sorted().findFirst().orElse("");
		}
		else prefix = "";
		Set<String> stemKeys = what.gram.flags.getAll(Keys.INFINITIVE_STEM);
		key = key + stemKeys.stream().sorted()
				.map(s -> s.startsWith(prefix) ? s.substring(prefix.length()) : s)
				.reduce((a, b) -> a + ", " + b).orElse("0")
				+ "\t";
		stemKeys = what.gram.flags.getAll(Keys.PRESENT_STEM);
		key = key + stemKeys.stream().sorted()
				.map(s -> s.startsWith(prefix) ? s.substring(prefix.length()) : s)
				.reduce((a, b) -> a + ", " + b).orElse("0")
				+ "\t";
		stemKeys = what.gram.flags.getAll(Keys.PAST_STEM);
		key = key + stemKeys.stream().sorted()
				.map(s -> s.startsWith(prefix) ? s.substring(prefix.length()) : s)
				.reduce((a, b) -> a + ", " + b).orElse("0");

		TreeSet<String> values = where.get(key);
		if (values == null) values = new TreeSet<>();
		values.add(what.lemma.text);
		where.put(key, values);
	}
	/**
	 * Pievienot informāciju par doto Header objektu directBySPrefix vai
	 * reflByPrefix statistiku vākšanas objektā.
	 * @param where	datu struktūra, kuru nepieciešams papildināt (directByPrefix
	 *              vai reflByPrefix)
	 * @param what	Header, par kuru informācija jāpievieno papildināmajai datu
	 *              struktūrai
	 */
	protected void addByPrefix(
			TreeMap<String,TreeMap<String, TreeSet<String>>> where, Header what)
	{
		Set<String> prefs = what.gram.flags.getAll(Keys.VERB_PREFIX);
		Set<String> infs = what.gram.flags.getAll(TKeys.INFLECT_AS);
		if (prefs == null) prefs = new HashSet<>();
		if (prefs.isEmpty()) prefs.add("0");
		if (infs == null) infs = new HashSet<>();
		if (infs.isEmpty()) infs.add("0");
		for (String inf : infs)
		{
			TreeMap<String, TreeSet<String>> prefMap = where.get(inf);
			if (prefMap == null) prefMap = new TreeMap<>();
			for (String pref : prefs)
			{
				prefixes.add(pref);
				TreeSet<String> verbs = prefMap.get(pref);
				if (verbs == null) verbs = new TreeSet<>();
				verbs.add(what.lemma.text);
				prefMap.put(pref, verbs);
			}
			where.put(inf, prefMap);
		}
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

		if (collectDirectByPrefix)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_direct-by-prefix.txt"), "UTF-8"));
			printPrefixMap(out, directByPrefix, prefixes);
			out.flush();
			out.close();
		}

		if (collectDirectPotential)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_direct-potentials.txt"), "UTF-8"));
			printPotentials(out, potentialDirect, "t");
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

		if (collectReflByPrefix)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_refl-by-prefix.txt"), "UTF-8"));
			printPrefixMap(out, reflByPrefix, prefixes);
			out.flush();
			out.close();
		}

		if (collectReflPotential)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(folderPath + "/" + fileNamePrefix + "_refl-potentials.txt"), "UTF-8"));
			printPotentials(out, potentialRefl, "ties");
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
			where.write("Celms+izskaņa\tDisambiguators\tDarbības vārdu rinda\n");
			where.write(what.keySet().stream().map(k ->
					k.replaceAll("\" \\(", "\t").replaceAll("\"$", "\t0").replaceAll("\"\\(?|\\)", "")
							+ "\t" + what.get(k).stream()
								.reduce((v1, v2) -> v1 + ", " + v2)	.orElse(""))
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
			where.write("Nenoteiksme\tTagadne\tPagātne\tDarbības vārdi\n");
			where.write(what.keySet().stream().map(k ->
					k + "\t" +
							what.get(k).stream()
									.reduce((v1, v2) -> v1 + ", " + v2)
									.orElse(""))
					.reduce((p1, p2) -> p1 + "\n" + p2).orElse(""));
			where.write("\n");
		}
	}
	/**
	 * Ertības metode, kas tiek izmantota directByPrefix un reflByPrefix
	 * izdrukāšanai.
	 * @param where	plusma/fails, kur notiks izdeukāšana
	 * @param what	datu struktūra, kuru nepieciešams izdrukāt (directByPrefix
	 *              vai reflByPrefix)
	 * @throws IOException
	 */
	protected static void printPrefixMap (
			BufferedWriter where,
			TreeMap<String,TreeMap<String, TreeSet<String>>> what,
			TreeSet<String> axisLabels)
	throws IOException
	{
		if (what != null && what.size() > 0)
		{
			where.write("Celms+izskaņa\t");
			where.write(axisLabels.stream().map(p -> "0".equals(p) ? p : p + "-").reduce(
					(p1, p2) -> p1 + "\t" + p2).orElse(""));
			where.write("\n");
			for (String inf : what.keySet())
			{
				where.write(inf + "\t");
				TreeMap<String, TreeSet<String>> prefMap = what.get(inf);
				where.write(axisLabels.stream().sorted().map(pref -> prefMap
						.getOrDefault(pref, new TreeSet<>()).stream().sorted()
						.reduce((v1, v2) -> v1 + ", " + v2).orElse("0"))
					.reduce((v1, v2) -> v1 + "\t" + v2).orElse(""));
				where.write("\n");
			}
		}
	}

	protected void printPotentials(
			BufferedWriter where, TreeSet<String> source, String ending)
	throws IOException
	{
		// Celms + izskaņa -> priedēklis -> attiecīgie darbības vārdi
		TreeMap<String,TreeMap<String, TreeSet<String>>> potentials = new TreeMap<>();
		TreeSet<String>  prefixes = new TreeSet<>();
		String[] sortedStems = infinitiveStems.stream()
				.sorted((a, b) -> b.length() - a.length())
				.toArray(size -> new String[size]);

		for (String potVerb : source)
		{
			for (String stem : sortedStems)
			{
				String inf = stem + ending;
				if (potVerb.endsWith(inf))
				{
					String prefix = "0";
					if (potVerb.length() > inf.length())
						prefix = potVerb.substring(0, potVerb.length() - inf.length());
					prefixes.add(prefix);

					TreeMap<String, TreeSet<String>> prefMap = potentials.get(inf);
					if (prefMap == null) prefMap = new TreeMap<>();
					TreeSet<String> verbs = prefMap.get(prefix);
					if (verbs == null) verbs = new TreeSet<>();
					verbs.add(potVerb);
					prefMap.put(prefix, verbs);
					potentials.put(inf, prefMap);
					// Tiek pieņemts, ka visgarākais celms ir vislabākais, lai
					// "aizgraut" neanalizē kā "aizgr" + "aut". Tāpat tiek
					// pieņemts, ka katram vardam ir ne vairāk par vienu pareizu
					// dalījumu - tam vajadzētu strādāt vismaz kamēr visas
					// nenoteiksmju saknes ir vienzilbīgas.
					break;
				}
			}
		}
		if (potentials.size() > 0)
			printPrefixMap(where, potentials, prefixes);
	}
}
