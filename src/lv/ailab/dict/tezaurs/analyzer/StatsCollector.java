package lv.ailab.dict.tezaurs.analyzer;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Features;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.dict.tezaurs.analyzer.flagconst.Values;
import lv.ailab.dict.tezaurs.analyzer.struct.TEntry;
import lv.ailab.dict.utils.CountingSet;
import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.utils.Trio;
import lv.ailab.dict.utils.Tuple;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Paralēli tēzaura apstrādei var šo to saskaitīt.
 * Created 2015-08-04.
 *
 * @author Lauma
 */
public class StatsCollector
{
	/**
	 * Karodziņš, vai savākt izrunas.
	 */
	public final boolean collectPrononcations;
	/**
	 * Karodziņš, vai savākt 1. konjugāciju.
	 */
	public final boolean collectFirstConj;
	/**
	 * Karodziņš, vai savākt 5. deklinācijas izņēmumus.
	 */
	public final boolean collectFifthDeclExceptions;
	/**
	 * Karodziņš, vai savākt nelokāmos, kam norādīts locījums.
	 */
	public final boolean collectNonInflWithCase;
	/**
	 * Karodziņš, vai savākt šķirkļavārdus/atvasinājumus, kas atbilst noteiktai
	 * regulārai izteiksmei.
	 */
	public final Pattern collectWithRegexp;
	/**
	 * Saraksts ar atslēgas un vērtības pārīšiem, pēc kuriem atlasīt. Karodziņus
	 * saista ar loģisko UN.(Ja te ir null, tad kalpo kā norāde, ka nevajag šādi
	 * atlasīt).
	 */
	public final ArrayList<Tuple<Keys, String>> collectWithFeature;
	/**
	 * Ar kādiem atslēgu/vērtību pārīšiem aprakstīt patvaļīgi atlasītos
	 * rezultātus. Ja vērtība ir null, tad tiek drukātas visas vērtības ar tādu
	 * atslēgu.
	 */
	public final ArrayList<Tuple<Keys, String>> describeWithFeatures;
	/**
	 * Plūsma, kurā rakstīt vārdu sarakstu. Ja null, tad pieņem, ka vārdu
	 * sarakstu nevajag.
	 */
	public final Writer wordlistOut;

    public TreeSet<String> binaryFlags = new TreeSet<>();
    public TreeSet<String> pairingKeys = new TreeSet<>();
	public CountingSet<Tuple<Keys,String>> flagCounts = new CountingSet<>();
    // Counting entries with various properties:
    public int overallCount = 0;
    public int hasParadigm = 0;
    public int hasMultipleParadigms = 0;
    public int hasMultipleParadigmFlag = 0;
    public int hasLociitKaaFlag = 0;
    public int hasNoParadigm = 0;
    public int hasUnparsedGram = 0;
	public int referenceCount = 0;
	public int contentEntryCount = 0;
	/**
	 * Izruna, šķirkļavārds, šķirkļa homonīma indekss.
	 */
	public ArrayList<Trio<String, String, String>> pronunciations = new ArrayList<>();
	/**
	 * Darbības vārds, šķirkļavārds, šķirkļa homonīma indekss.
	 */
	public ArrayList<Trio<String, String, String>> firstConj = new ArrayList<>();
    /**
     * Darbības vārds, šķirkļavārds, šķirkļa homonīma indekss.
     */
    public ArrayList<Trio<String, String, String>> nonInflWithCase = new ArrayList<>();
	/**
	 * Vārds, šķirkļavāds, homonīma indekss.
	 */
    public ArrayList<Trio<String, String, String>> fifthDeclExceptions = new ArrayList<>();
	/**
	 * Šķirkļavāds, homonīma indekss, karodziņi.
	 */
	public ArrayList<Trio<String, String, ArrayList<String>>> entriesWithSelectedFeature = new ArrayList<>();

	public StatsCollector ( boolean collectPrononcations,
			boolean collectFirstConj, boolean collectFifthDeclExceptions,
			boolean collectNonInflWithCase, String collectWithRegexp,
			ArrayList<Tuple<Keys, String>> collectFeature,
			ArrayList<Tuple<Keys, String>> descriptionFeatures,
			Writer wordlistOutput)
	{
		this.collectPrononcations = collectPrononcations;
		this.collectFirstConj = collectFirstConj;
		this.collectFifthDeclExceptions = collectFifthDeclExceptions;
		this.collectNonInflWithCase = collectNonInflWithCase;
		this.collectWithRegexp = collectWithRegexp == null ?
				null : Pattern.compile(collectWithRegexp);
		this.collectWithFeature = collectFeature;
		this.describeWithFeatures = descriptionFeatures;
		this.wordlistOut = wordlistOutput;
	}


    public void countEntry( TEntry entry) throws IOException
	{
        overallCount++;
        if (entry.hasParadigm()) hasParadigm++;
        else hasNoParadigm ++;
        if (entry.hasMultipleParadigms()) hasMultipleParadigms++;
        if (entry.hasUnparsedGram()) hasUnparsedGram++;
		if (entry.hasReference()) referenceCount ++;
		if (entry.hasContents()) contentEntryCount++;

        Flags entryFlags = entry.getUsedFlags();
		flagCounts.addAll(entry.getFlagCounts());
		HashSet<String> bf = entryFlags.binaryFlags();
        if (bf != null)
		{
			if (bf.contains(Values.UNCLEAR_PARADIGM.s))
		//if (entryFlags.test(Features.UNCLEAR_PARADIGM))
				hasMultipleParadigmFlag++;
			binaryFlags.addAll(bf);
		}

        if (entryFlags.getAll(Keys.INFLECT_AS) != null &&
				entryFlags.getAll(Keys.INFLECT_AS).size() > 0)
            hasLociitKaaFlag++;
        if (entryFlags.pairings != null)
			for (Tuple<Keys, String> f : entryFlags.pairings.asList())
				if (!f.first.equals(Keys.OTHER_FLAGS))
					pairingKeys.add(f.first + ": " + f.second);

		if (collectPrononcations)
        	for (String p : entry.collectPronunciations())
            	pronunciations.add(Trio.of(p, entry.head.lemma.text, entry.homId));
        for (Header h : entry.getAllHeaders())
        {
			if (h.gram == null) continue;
			if (collectFirstConj &&
					(h.gram.paradigm.contains(15) || h.gram.paradigm.contains(18)))
				firstConj.add(Trio.of(h.lemma.text, entry.head.lemma.text, entry.homId));
			if (h.gram.flags == null) continue;

            if (collectFifthDeclExceptions && h.gram.paradigm.contains(9)
					&& h.gram.flags.test(Features.NO_SOUNDCHANGE))
               	fifthDeclExceptions.add(Trio.of(h.lemma.text, entry.head.lemma.text, entry.homId));

			if (collectNonInflWithCase && h.gram.flags.testKey(Keys.CASE) &&
					h.gram.flags.test(Features.NON_INFLECTIVE))
				nonInflWithCase.add(Trio.of(h.lemma.text, entry.head.lemma.text, entry.homId));
        }
		if (collectWithRegexp != null)
		{
			for (Header h : entry.getAllHeaders())
			{
				if (collectWithRegexp.matcher(h.lemma.text).matches())
				{
					ArrayList<String> flags = new ArrayList<>();
					flags.add("Vārds = " + h.lemma.text);
					if (entryFlags.getAll(Keys.POS) == null)
						flags.add(Keys.POS.s + " = NULL");
					else flags.add(Keys.POS.s + " = " +
							entryFlags.getAll(Keys.POS).stream()
									.reduce((s1, s2) -> s1 + " + " + s2).orElse("NULL"));
					entriesWithSelectedFeature.add(Trio.of(
							entry.head.lemma.text,
							entry.homId == null ? "REF" : entry.homId,
							flags));
				}
			}
		}

		if (collectWithFeature != null &&
				collectWithFeature.stream().map(entryFlags::test).reduce(true, (a, b) -> a && b))
		{
			ArrayList<String> flags = new ArrayList<>();
			if (describeWithFeatures != null)
				for (Tuple<Keys, String> feature : describeWithFeatures)
			{
				if (feature.second == null && entryFlags.getAll(feature.first) == null)
					flags.add(feature.first.s + " = NULL");
				else if (feature.second == null)
					flags.add(feature.first.s + " = " +
							entryFlags.getAll(feature.first).stream()
									.reduce((s1, s2) -> s1+" + "+s2).orElse("NULL"));
				else if (entryFlags.test(feature))
					flags.add(feature.first.s + " = " + feature.second);
				else flags.add(feature.first.s + " != " + feature.second);
			}
			if (entry.sources == null || entry.sources.isEmpty()) flags.add("NULL");
			else flags.add(entry.sources.s.stream().sorted()
					.reduce((a, b) -> a + ", " + b).orElse("NULL"));
			entriesWithSelectedFeature.add(Trio.of(
					entry.head.lemma.text,
					entry.homId == null ? "REF" : entry.homId,
					flags));
		}

		writeInWordlist(entry);

    }

	protected void writeInWordlist(TEntry entry) throws IOException
	{
		if (wordlistOut == null) return;
		StringBuilder line = new StringBuilder();

		line.append(entry.head.lemma.text);
		line.append("\t");
		if (entry.homId != null)line.append(entry.homId);
		else line.append("0");
		line.append("\t");
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(Keys.POS) &&
				entry.head.gram.flags.test(Features.UNCLEAR_POS))
		{
			line.append(Values.UNCLEAR_POS.s);
			line.append(",");
			line.append(String.join(",", entry.head.gram.flags.getAll(Keys.POS)));
		}
		else if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.test(Features.UNCLEAR_POS))
			line.append(Values.UNCLEAR_POS.s);
		else line.append("NULL");
		line.append("\t");
		if (entry.head.paradigmCount() == 1)
			// Nē, nu stulbi kaut kā taisīt mapošanu, ja objekts ir tikai viens
			// Bet varbūt ka tā ir labāk nākotnei
			line.append(entry.head.gram.paradigm.stream()
					.map(Object::toString).reduce((s1, s2) -> s1 + "," + s2)
					.orElse("NULL"));
		else line.append("NULL");
		line.append("\t");
		if (entry.sources == null || entry.sources.isEmpty())
		{
			if (entry.hasReference()) line.append("REF");
			else line.append("NULL");
		}
		else line.append(String.join(",", entry.sources.s));
		line.append("\t");
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(Keys.DOMAIN))
			line.append(String
					.join(",", entry.head.gram.flags.getAll(Keys.DOMAIN)));
		else line.append("NULL");
		line.append("\t");
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(Keys.USAGE_RESTRICTIONS))
			line.append(String.join(",", entry.head.gram.flags
					.getAll(Keys.USAGE_RESTRICTIONS)));
		else line.append("NULL");
		line.append("\t");
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(Keys.LANGUAGE))
			line.append(String.join(",", entry.head.gram.flags.getAll(Keys.LANGUAGE)));
		else line.append("NULL");
		/*line.append("\t");
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				(entry.head.gram.flags.testKey(Keys.USED_TOGETHER_WITH) ||
						entry.head.gram.flags.testKey(Keys.CONTAMINATION) ||
						entry.head.gram.flags.testKey(Keys.USUALLY_USED_IN_FORM) ||
						entry.head.gram.flags.testKey(Keys.OFTEN_USED_IN_FORM) ||
						entry.head.gram.flags.testKey(Keys.USED_ONLY_IN_FORM) ||
						entry.head.gram.flags.testKey(Keys.ALSO_USED_IN_FORM) ||
						entry.head.gram.flags.testKey(Keys.USED_IN_FORM)))
		{
			ArrayList<String> tmp = new ArrayList<>();
			if (entry.head.gram.flags.testKey(Keys.CONTAMINATION))
				for (String value : entry.head.gram.flags.getAll(Keys.CONTAMINATION))
					tmp.add(Keys.CONTAMINATION.s + " = " + value);
			if (entry.head.gram.flags.testKey(Keys.USED_ONLY_IN_FORM))
				for (String value : entry.head.gram.flags.getAll(Keys.USED_ONLY_IN_FORM))
					tmp.add(Keys.USED_ONLY_IN_FORM.s + " = " + value);
			if (entry.head.gram.flags.testKey(Keys.USUALLY_USED_IN_FORM))
				for (String value : entry.head.gram.flags.getAll(Keys.USUALLY_USED_IN_FORM))
					tmp.add(Keys.USUALLY_USED_IN_FORM.s + " = " + value);
			if (entry.head.gram.flags.testKey(Keys.OFTEN_USED_IN_FORM))
				for (String value : entry.head.gram.flags.getAll(Keys.OFTEN_USED_IN_FORM))
					tmp.add(Keys.OFTEN_USED_IN_FORM.s + " = " + value);
			if (entry.head.gram.flags.testKey(Keys.ALSO_USED_IN_FORM))
				for (String value : entry.head.gram.flags.getAll(Keys.ALSO_USED_IN_FORM))
					tmp.add(Keys.ALSO_USED_IN_FORM.s + " = " + value);
			if (entry.head.gram.flags.testKey(Keys.USED_IN_FORM))
				for (String value : entry.head.gram.flags.getAll(Keys.USED_IN_FORM))
					tmp.add(Keys.USED_IN_FORM.s + " = " + value);
			if (entry.head.gram.flags.testKey(Keys.USED_TOGETHER_WITH))
				for (String value : entry.head.gram.flags.getAll(Keys.USED_TOGETHER_WITH))
					tmp.add(Keys.USED_TOGETHER_WITH.s + " = " + value);
			line.append(String.join(",", tmp));
		}
		else line.append("NULL");//*/
		line.append("\n");
		wordlistOut.write(line.toString().replace(" ", "_"));
	}

	public void printContents(BufferedWriter out)
            throws IOException
    {
        out.write("{\n");

        out.write("\"Šķirkļu kopskaits\":" + overallCount);
        out.write(",\n\"Šķirkļi ar nozīmēm, frāzēm vai atvasinājumiem\":" + contentEntryCount);
        out.write(",\n\"Šķirkļi ar referencēm\":" + referenceCount);
        out.write(",\n\"Šķirkļi ar vismaz vienu paradigmu\":" + hasParadigm);
        out.write(",\n\"Šķirkļi ar vairāk kā vienu paradigmu\":" + hasMultipleParadigms);
        out.write(",\n\"Šķirkļi ar karodziņu \\\"Neviennozīmīga paradigma\\\"\":" +
                hasMultipleParadigmFlag);
        out.write(",\n\"Šķirkļi bez paradigmām\":" + hasNoParadigm);
        out.write(",\n\"Daļēji atpazīti šķirkļi\":" + hasUnparsedGram);
        out.write(",\n\"Unikālo bināro karodziņu skaits\":" + binaryFlags.size());
        out.write(",\n\"Unikālu \\\"Locīt kā...\\\" karodziņu skaits\":" +
                binaryFlags.stream().filter(f -> f.startsWith("Locīt kā ")).count());
        out.write(",\n\"Šķirkļi ar \\\"Locīt kā...\\\" karodziņiem\":" + hasLociitKaaFlag);
		out.write(",\n\"Unikālo pārīskarodziņu atslēgu skaits\":" + pairingKeys
				.size());
        out.write(",\n\"Izrunas transkripciju kopskaits\":" + pronunciations.size());

        /*out.write(",\n\"Binārie karodziņi\":[\n");
        out.write(binaryFlags.stream().map(f -> "\t\"" + JSONObject.escape(f) + "\"")
                .reduce((f1, f2) -> f1 + ",\n" + f2).orElse(""));
        out.write("\n]");
		out.write(",\n\"Pārīškarodziņi\":[\n");
		out.write(pairingKeys.stream()
				.map(f -> "\t\"" + JSONObject.escape(f) + "\"")
				.reduce((f1, f2) -> f1 + ",\n" + f2).orElse(""));
		out.write("\n]");*/

		out.write(",\n\"Karodziņi\":[");
		HashMap<Tuple<Keys, String>, Integer> counts = flagCounts.getCounts();
		for (Tuple<Keys, String> feature : counts.keySet().stream().sorted(
				(t1, t2) -> (t1.first != null && t1.first.equals(t2.first)) ?
						t1.second.compareTo(t2.second) : t1.first.compareTo(t2.first)).toArray(i -> new Tuple[i]))
		{
			out.write("\n\t[\"");
			out.write(JSONObject.escape(feature.first.s));
			out.write("\", \"");
			out.write(JSONObject.escape(feature.second));
			out.write("\", \"");
			out.write(counts.get(feature).toString());
			out.write("\"],");
		}
		out.write("\n]");

		if (collectPrononcations)
		{
			out.write(",\n\"Izrunas transkripcijas\":[\n");
			out.write(pronunciations.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
							.escape(t.second) + "\", \"" + JSONObject
							.escape(t.third) + "\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
			out.write("\n]");
		}
		if (collectFifthDeclExceptions && fifthDeclExceptions != null &&
				fifthDeclExceptions.size() > 0)
		{
			out.write(",\n\"5. deklinācijas izņēmumi\":[\n");
			out.write(fifthDeclExceptions.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
							.escape(t.second) + "\", \"" + JSONObject
							.escape(t.third) + "\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
			out.write("\n]");
		}
		if (collectFirstConj && firstConj != null && firstConj.size() > 0)
		{
			out.write(",\n\"1. konjugācija\":[\n");
			out.write(firstConj.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
							.escape(t.second) + "\", \"" + JSONObject
							.escape(t.third) + "\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
			out.write("\n]");
		}
		if (collectNonInflWithCase && nonInflWithCase != null
				&& nonInflWithCase.size() > 0)
		{
			out.write(",\n\"Sastingušās formas\":[\n");
			out.write(nonInflWithCase.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
							.escape(t.second) + "\", \"" + JSONObject
							.escape(t.third) + "\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
			out.write("\n]");
		}

		if ((collectWithFeature != null || collectWithRegexp != null) && entriesWithSelectedFeature != null
				&& entriesWithSelectedFeature.size() > 0)
		{
			out.write(",\n\"");
			String title = "";
			if (collectWithFeature != null)
				title = collectWithFeature.stream()
						.map(f -> f.first.s + " = " + (f.second == null ? "*" : JSONObject.escape(f.second)))
						.reduce((a, b) -> a + ", " + b).orElse("");
			if (collectWithRegexp != null)
			{
				if (title.length() > 0) title = title + ", ";
				title = title + "Šķirkļavārda izteiksme = " + collectWithRegexp.pattern();
			}
			if (title.length() < 1) title = "Apkopotie šķirkļi";
			out.write(JSONObject.escape(title));
			out.write("\":[\n");
			out.write(entriesWithSelectedFeature.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" +
							JSONObject.escape(t.second) + "\", \"" +
							t.third.stream().map(f -> JSONObject.escape(f))
									.reduce((f1, f2) -> f1 + "\", \"" + f2).orElse("") +
							"\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
			out.write("\n]");
		}

        out.write("\n}\n");
    }
}
