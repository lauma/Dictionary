package lv.ailab.tezaurs.analyzer;

import lv.ailab.tezaurs.analyzer.struct.Entry;
import lv.ailab.tezaurs.analyzer.struct.Header;
import lv.ailab.tezaurs.utils.Trio;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created on 2015-08-04.
 *
 * @author Lauma
 */
public class StatsCollector
{

    public TreeSet<String> flags = new TreeSet<>();
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
	 * Vārds, šķirkļavāds, homonīma indekss.
	 */
    public ArrayList<Trio<String, String, String>> fifthDeclExceptions = new ArrayList<>();

    public void countEntry( Entry entry)
    {
        overallCount++;
        if (entry.hasParadigm()) hasParadigm++;
        else hasNoParadigm ++;
        if (entry.hasMultipleParadigms()) hasMultipleParadigms++;
        if (entry.hasUnparsedGram()) hasUnparsedGram++;

        Set<String> entryFlags = entry.getUsedFlags();
        if (entryFlags.contains("Neviennozīmīga paradigma"))
            hasMultipleParadigmFlag++;
        if (entryFlags.stream().filter(f -> f.startsWith("Locīt kā ")).count() > 0)
            hasLociitKaaFlag++;
        flags.addAll(entryFlags);

        for (String p : entry.collectPronunciations())
            pronunciations.add(Trio.of(p, entry.head.lemma.text, entry.homId));
        for (Header h : entry.getAllHeaders())
        {
            if (h.gram != null &&
                    h.gram.paradigm.contains(9) && h.gram.flags.contains("Locīt bez mijas"))
                fifthDeclExceptions.add(Trio.of(h.lemma.text, entry.head.lemma.text, entry.homId));
			if (h.gram != null &&
					(h.gram.paradigm.contains(15) || h.gram.paradigm.contains(18)))
				firstConj.add(Trio.of(h.lemma.text, entry.head.lemma.text, entry.homId));
        }

    }

    public void printContents(BufferedWriter out, boolean printFifthDeclExc, boolean printFirstConj)
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
        out.write(",\n\"Unikālo karodziņu skaits\":" + flags.size());
        out.write(",\n\"Unikālu \\\"Locīt kā...\\\" karodziņu skaits\":" +
                flags.stream().filter(f -> f.startsWith("Locīt kā ")).count());
        out.write(",\n\"Šķirkļi ar \\\"Locīt kā...\\\" karodziņiem\":" + hasLociitKaaFlag);
        out.write(",\n\"Izrunas transkripciju kopskaits\":" + pronunciations.size());

        out.write("\"Karodziņi\":[\n");
        out.write(flags.stream().map(f -> "\t\"" + JSONObject.escape(f) + "\"")
                .reduce((f1, f2) -> f1 + ",\n" + f2).orElse(""));
        out.write("\n],\n");

        out.write(",\n\"Izrunas transkripcijas\":[\n");
        out.write(pronunciations.stream().map(t ->
                "\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
                        .escape(t.second) + "\", \"" + JSONObject
                        .escape(t.third) + "\"]")
                .reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
		if (printFifthDeclExc)
		{
			out.write("\n],\n");
			out.write(",\n\"5. deklinācijas izņēmumi\":[\n");
			out.write(fifthDeclExceptions.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
							.escape(t.second) + "\", \"" + JSONObject
							.escape(t.third) + "\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
		}
		if (printFirstConj)
		{
			out.write("\n],\n");
			out.write(",\n\"1. konjugācija\":[\n");
			out.write(firstConj.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
							.escape(t.second) + "\", \"" + JSONObject
							.escape(t.third) + "\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
		}
        out.write("\n]\n");

        out.write("}\n");
    }
}
