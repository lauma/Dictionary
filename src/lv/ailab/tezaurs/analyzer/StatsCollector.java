package lv.ailab.tezaurs.analyzer;

import lv.ailab.tezaurs.analyzer.struct.Entry;
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
    public ArrayList<Trio<String, String, String>> pronunciations = new ArrayList<>();
    public TreeSet<String> flags = new TreeSet<>();
    // Counting entries with various properties:
    public int overallCount = 0;
    public int hasParadigm = 0;
    public int hasMultipleParadigms = 0;
    public int hasMultipleParadigmFlag = 0;
    public int hasLociitKaaFlag = 0;
    public int hasNoParadigm = 0;
    public int hasUnparsedGram = 0;

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

    }

    public void printContents(BufferedWriter out)
            throws IOException
    {
        out.write("{\n");

        out.write("\"Total entry count\":" + overallCount);
        out.write(",\n\"Entries with at least one paradigm\":" + hasParadigm);
        out.write(",\n\"Entries with more than one paradigm\":" + hasMultipleParadigms);
        out.write(",\n\"Entries with \\\"Neviennozīmīga paradigma\\\" flag\":" +
                hasMultipleParadigmFlag);
        out.write(",\n\"Entries with no paradigm\":" + hasNoParadigm);
        out.write(",\n\"Partially parsed entries\":" + hasUnparsedGram);
        out.write(",\n\"Amount of distinct flags used\":" + flags.size());
        out.write(",\n\"Amount of distinct \\\"Locīt kā...\\\" flags\":" +
                flags.stream().filter(f -> f.startsWith("Locīt kā ")).count());
        out.write(",\n\"Entries with \\\"Locīt kā...\\\" flags\":" + hasLociitKaaFlag);
        out.write(",\n\"Amount of pronunciation transcriptions\":" + pronunciations.size());

        out.write(",\n\"Pronunciations\":[\n");
        out.write(pronunciations.stream().map(t ->
                "\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
                        .escape(t.second) + "\", \"" + JSONObject
                        .escape(t.third) + "\"]")
                .reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
        out.write("\n],\n");

        out.write("\"Flags\":[\n");
        out.write(flags.stream().map(f -> "\t\"" + JSONObject.escape(f) + "\"")
                .reduce((f1, f2) -> f1 + ",\n" + f2).orElse(""));
        out.write("\n]\n");

        out.write("}\n");
    }
}
