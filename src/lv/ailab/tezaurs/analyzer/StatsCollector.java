package lv.ailab.tezaurs.analyzer;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.analyzer.struct.Entry;
import lv.ailab.tezaurs.analyzer.struct.Flags;
import lv.ailab.tezaurs.analyzer.struct.Header;
import lv.ailab.tezaurs.utils.Trio;
import lv.ailab.tezaurs.utils.Tuple;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created on 2015-08-04.
 *
 * @author Lauma
 */
public class StatsCollector
{
	public final boolean collectFirstConj;
	public final boolean collectFifthDeclExceptions;
	public final boolean collectNonInflWithCase;

    public TreeSet<String> binaryFlags = new TreeSet<>();
    public TreeSet<String> pairingKeys = new TreeSet<>();
    // Counting entries with various properties:
    public int overallCount = 0;
    public int hasParadigm = 0;
    public int hasMultipleParadigms = 0;
    public int hasMultipleParadigmFlag = 0;
    public int hasLociitKaaFlag = 0;
    public int hasNoParadigm = 0;
    public int hasUnparsedGram = 0;
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

	public StatsCollector (
			boolean collectFirstConj, boolean collectFifthDeclExceptions,
			boolean collectNonInflWithCase)
	{
		this.collectFirstConj = collectFirstConj;
		this.collectFifthDeclExceptions = collectFifthDeclExceptions;
		this.collectNonInflWithCase = collectNonInflWithCase;
	}


    public void countEntry( Entry entry)
    {
        overallCount++;
        if (entry.hasParadigm()) hasParadigm++;
        else hasNoParadigm ++;
        if (entry.hasMultipleParadigms()) hasMultipleParadigms++;
        if (entry.hasUnparsedGram()) hasUnparsedGram++;

        Flags entryFlags = entry.getUsedFlags();
		HashSet<String> bf = entryFlags.binaryFlags();
        if (bf != null)
		{
			if (bf.contains(Values.UNCLEAR_PARADIGM.s))
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

    }

    public void printContents(BufferedWriter out)
            throws IOException
    {
        out.write("{\n");

        out.write("\"Šķirkļu kopskaits\":" + overallCount);
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

        out.write(",\n\"Binārie karodziņi\":[\n");
        out.write(binaryFlags.stream().map(f -> "\t\"" + JSONObject.escape(f) + "\"")
                .reduce((f1, f2) -> f1 + ",\n" + f2).orElse(""));
        out.write("\n]");
		out.write(",\n\"Pārīškarodziņu atslēgas\":[\n");
		out.write(pairingKeys.stream()
				.map(f -> "\t\"" + JSONObject.escape(f) + "\"")
				.reduce((f1, f2) -> f1 + ",\n" + f2).orElse(""));
		out.write("\n]");

        out.write(",\n\"Izrunas transkripcijas\":[\n");
        out.write(pronunciations.stream().map(t ->
                "\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
                        .escape(t.second) + "\", \"" + JSONObject
                        .escape(t.third) + "\"]")
                .reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
		out.write("\n]");

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

        out.write("\n}\n");
    }
}
