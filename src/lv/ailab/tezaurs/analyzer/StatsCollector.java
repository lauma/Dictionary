package lv.ailab.tezaurs.analyzer;

import lv.ailab.tezaurs.analyzer.struct.Entry;
import lv.ailab.tezaurs.utils.Trio;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    public int overallCount = 0;
    public int hasParadigm = 0;
    public int hasMultipleParadigms = 0;
    public int hasNoParadigm = 0;
    public int hasUnparsedGram = 0;


    public void countEntry( Entry entry)
    {
        overallCount++;
        if (entry.hasParadigm()) hasParadigm++;
        else hasNoParadigm ++;
        if (entry.hasMultipleParadigms()) hasMultipleParadigms++;
        if (entry.hasUnparsedGram()) hasUnparsedGram++;

        for (String p : entry.collectPronunciations())
            pronunciations.add(Trio.of(p, entry.head.lemma.text, entry.homId));

        flags.addAll(entry.getUsedFlags());
    }

    public void printContents(BufferedWriter out)
            throws IOException
    {
        out.write("{\n");

        out.write("\"Total entry count\":" + overallCount + ",\n");
        out.write("\"Entries with at least one paradigm\":" + hasParadigm + ",\n");
        out.write("\"Entries with more than one paradigm\":" + hasMultipleParadigms + ",\n");
        out.write("\"Entries with no paradigm\":" + hasNoParadigm + ",\n");
        out.write("\"Partially parsed entries\":" + hasUnparsedGram + ",\n");
        out.write("\"Amount of different flags used\":" + flags.size() + ",\n");
        out.write("\"Amount of different \\\"Locīt kā...\\\" flags\":" +
                flags.stream().filter(f -> f.startsWith("Locīt kā ")).count() + ",\n");
        out.write("\"Amount of pronunciation transcriptions\":" + pronunciations.size() + ",\n");

        out.write("\"Pronunciations\":[");
        out.write(pronunciations.stream().map(t ->
                "[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
                        .escape(t.second) + "\", \"" + JSONObject
                        .escape(t.third) + "\"]")
                .reduce((t1, t2) -> t1 + ", " + t2).orElse(""));
        out.write("],\n");

        out.write("\"Flags\":[\"");
        out.write(flags.stream().map(f -> "\"" + JSONObject.escape(f) + "\"")
                .reduce((f1, f2) -> f1 + ", " + f2).orElse(""));
        out.write("]\n");

        out.write("}\n");
    }
}
