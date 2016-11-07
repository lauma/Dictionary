package lv.ailab.dict.tezaurs.analyzer;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.tezaurs.analyzer.struct.TEntry;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.CountingSet;
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
public class GeneralStatsCollector
{
	/**
	 * Karodziņš, vai savākt izrunas.
	 */
	public final boolean collectPrononcations;
	/*
	 * Karodziņš, vai savākt 1. konjugāciju.
	 */
	//public final boolean collectFirstConjAll;
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
	 * Saraksts ar paradigmām, pēc kurām atlasīt.
	 */
	public final ArrayList<Integer> collectWithParadigms;
	/**
	 * Saraksts ar atslēgas un vērtības pārīšiem, pēc kuriem atlasīt. Karodziņus
	 * saista ar loģisko UN.(Ja te ir null, tad kalpo kā norāde, ka nevajag šādi
	 * atlasīt).
	 */
	public final ArrayList<Tuple<String, String>> collectWithFeatures;
	/**
	 * Ar kādiem atslēgu/vērtību pārīšiem aprakstīt patvaļīgi atlasītos
	 * rezultātus. Ja vērtība ir null, tad tiek drukātas visas vērtības ar tādu
	 * atslēgu.
	 */
	public final ArrayList<Tuple<String, String>> describeWithFeatures;
	/**
	 * Vai rezultātā izdrukāt pieminētās paradigmas.
	 */
	public final boolean describeWithParadigms;
	/**
	 * Vai rezultātā izdrukāt pieminētos atvasinājumus un citas lemmas.
	 */
	public final boolean describeWithOtherLemmas;
	/**
	 * Karodziņš, vai savākt locīšanas īpatnības.
	 */
	public final boolean collectInfllWeardness;
	/**
	 * Plūsma, kurā rakstīt vārdu sarakstu. Ja null, tad pieņem, ka vārdu
	 * sarakstu nevajag.
	 */
	public final Writer wordlistOut;

    public TreeSet<String> binaryFlags = new TreeSet<>();
    public TreeSet<String> pairingKeys = new TreeSet<>();
	public CountingSet<Tuple<String,String>> flagCounts = new CountingSet<>();
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
	/*
	 * Darbības vārds, šķirkļavārds, šķirkļa homonīma indekss.
	 */
	//public ArrayList<Trio<String, String, String>> firstConj = new ArrayList<>();
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

	/**
	 */
	public HashSet<String> inflWeardness = new HashSet<>();

	public GeneralStatsCollector( boolean collectPrononcations,
			//boolean collectFirstConjAll,
			boolean collectFifthDeclExceptions,
			boolean collectNonInflWithCase, String collectWithRegexp,
			ArrayList<Integer> collectWithParadigms,
			ArrayList<Tuple<String, String>> collectFeature,
			ArrayList<Tuple<String, String>> descriptionFeatures,
			boolean describeWithParadigms, boolean describeWithOtherLemmas,
			boolean collectInflectionalWieardness,
			Writer wordlistOutput)
	{
		this.collectPrononcations = collectPrononcations;
		//this.collectFirstConjAll = collectFirstConjAll;
		this.collectFifthDeclExceptions = collectFifthDeclExceptions;
		this.collectNonInflWithCase = collectNonInflWithCase;
		this.collectWithRegexp = collectWithRegexp == null ?
				null : Pattern.compile(collectWithRegexp);
		this.collectWithParadigms = collectWithParadigms;
		this.collectWithFeatures = collectFeature;
		this.describeWithFeatures = descriptionFeatures;
		this.describeWithParadigms = describeWithParadigms;
		this.describeWithOtherLemmas = describeWithOtherLemmas;
		this.collectInfllWeardness = collectInflectionalWieardness;
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
			if (bf.contains(TValues.UNCLEAR_PARADIGM))
				hasMultipleParadigmFlag++;
			binaryFlags.addAll(bf);
		}
		if (collectInfllWeardness && entryFlags.testKey(TKeys.INFLECTION_WEARDNES))
			inflWeardness.addAll(entryFlags.getAll(TKeys.INFLECTION_WEARDNES));

        if (entryFlags.getAll(TKeys.INFLECT_AS) != null &&
				entryFlags.getAll(TKeys.INFLECT_AS).size() > 0)
            hasLociitKaaFlag++;
        if (entryFlags.pairings != null)
			for (Tuple<String, String> f : entryFlags.pairings.asList())
				if (!f.first.equals(TKeys.OTHER_FLAGS))
					pairingKeys.add(f.first + ": " + f.second);

		if (collectPrononcations)
        	for (String p : entry.collectPronunciations())
            	pronunciations.add(Trio.of(p, entry.head.lemma.text, entry.homId));

		// Uzmanīgi, šī ir optimizācija ātrumam: if nosacījums daļēji dublē
		// iekšā esošos nosacījumus.
		if (collectFifthDeclExceptions || collectNonInflWithCase)
        	for (Header h : entry.getAllHeaders())
        {
			if (h.gram == null) continue;
			/*if (collectFirstConjAll &&
					(h.gram.getDirectParadigms().contains(15) ||
							h.gram.getDirectParadigms().contains(18)))
				firstConj.add(Trio.of(h.lemma.text, entry.head.lemma.text, entry.homId));//*/
			if (h.gram.flags == null) continue;

            if (collectFifthDeclExceptions && h.gram.getDirectParadigms().contains(9)
					&& h.gram.flags.test(TFeatures.NO_SOUNDCHANGE))
               	fifthDeclExceptions.add(Trio.of(h.lemma.text, entry.head.lemma.text, entry.homId));

			if (collectNonInflWithCase && h.gram.flags.testKey(TKeys.CASE) &&
					h.gram.flags.test(TFeatures.NON_INFLECTIVE))
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
					if (entryFlags.getAll(TKeys.POS) == null)
						flags.add(TKeys.POS + " = NULL");
					else flags.add(TKeys.POS + " = " +
							entryFlags.getAll(TKeys.POS).stream()
									.reduce((s1, s2) -> s1 + " + " + s2).orElse("NULL"));
					entriesWithSelectedFeature.add(Trio.of(
							entry.head.lemma.text,
							entry.homId == null ? "REF" : entry.homId,
							flags));
				}
			}
		}

		if (collectWithParadigms != null &&
				collectWithParadigms.stream().map(entry.getMentionedParadigms()::contains).reduce(false, (a, b) -> a || b))
		{
			entriesWithSelectedFeature.add(Trio.of(
					entry.head.lemma.text,
					entry.homId == null ? "REF" : entry.homId,
					describe(entry)));
		}

		if (collectWithFeatures != null &&
				collectWithFeatures.stream().map(entryFlags::test).reduce(true, (a, b) -> a && b))
		{
			entriesWithSelectedFeature.add(Trio.of(
					entry.head.lemma.text,
					entry.homId == null ? "REF" : entry.homId,
					describe(entry)));
		}

		writeInWordlist(entry);

    }

	protected ArrayList<String> describe(TEntry entry)
	{
		ArrayList<String> flags = new ArrayList<>();
		if (describeWithParadigms) flags.add(describeParadigms(entry));
		if (describeWithOtherLemmas) flags.add(describeOtherLemmas(entry));
		if (describeWithFeatures != null) flags.addAll(describeWithFeatures(entry));
		if (entry.sources == null || entry.sources.isEmpty()) flags.add("NULL");
		else flags.add(entry.sources.s.stream().sorted()
				.reduce((a, b) -> a + ", " + b).orElse("NULL"));
		return flags;

	}

	protected ArrayList<String> describeWithFeatures(TEntry entry)
	{
		ArrayList<String> flags = new ArrayList<>();
		Flags entryFlags = entry.getUsedFlags();
		if (describeWithFeatures != null)
			for (Tuple<String, String> feature : describeWithFeatures)
			{
				if (feature.second == null && entryFlags.getAll(feature.first) == null)
					flags.add(feature.first + " = NULL");
				else if (feature.second == null)
					flags.add(feature.first + " = " +
							entryFlags.getAll(feature.first).stream()
									.reduce((s1, s2) -> s1+" + "+s2).orElse("NULL"));
				else if (entryFlags.test(feature))
					flags.add(feature.first + " = " + feature.second);
				else flags.add(feature.first + " != " + feature.second);
			}
		return flags;
	}

	protected String describeParadigms(TEntry entry)
	{
		return "Pieminētās paradigmas = " +
				entry.getMentionedParadigms().stream().sorted()
						.map(Object::toString).reduce((a, b) -> a + ", " + b).orElse("NULL");

	}

	protected String describeOtherLemmas(TEntry entry)
	{
		return "Visas lemmas = " +
				entry.getAllHeaders().stream().map(a -> a.lemma.text)
						.reduce((a, b) -> a + ", " + b).orElse("NULL");
	}

	protected void writeInWordlist(TEntry entry) throws IOException
	{
		if (wordlistOut == null) return;
		StringBuilder line = new StringBuilder();

		// [0] šķirkļavārds
		line.append(entry.head.lemma.text);
		line.append("\t");
		// [1] homonīma indekss
		if (entry.homId != null)line.append(entry.homId);
		else line.append("0");
		line.append("\t");
		// [2] vārdšķira
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.POS))
		{
			if (entry.head.gram.flags.test(TFeatures.UNCLEAR_POS))
			{
				line.append(TValues.UNCLEAR_POS);
				line.append(",");
			}
			line.append(String.join(",", entry.head.gram.flags.getAll(TKeys.POS)));
		}
		else line.append("NULL");
		line.append("\t");
		HashSet<Integer> paradigms = entry.head.getDirectParadigms();

		// [3] paradigma
		if (paradigms.size() == 1)
			// Nē, nu stulbi kaut kā taisīt mapošanu, ja objekts ir tikai viens
			// Bet varbūt ka tā ir labāk nākotnei
			line.append(entry.head.gram.paradigm.stream()
					.map(Object::toString).reduce((s1, s2) -> s1 + "," + s2)
					.orElse("NULL"));
		else if (paradigms.size() == 2 && paradigms.contains(0))
			line.append(entry.head.gram.paradigm.stream().filter(p -> p != 0)
					.map(Object::toString).reduce((s1, s2) -> s1 + "," + s2)
					.orElse("NULL"));
		else if (paradigms.size() == 2 && paradigms.contains(16) && paradigms.contains(17))
			// TODO smukāk izvēlēties, kuru drukāt.
			line.append("16");
		else if (paradigms.size() == 2 && paradigms.contains(19) && paradigms.contains(20))
			// TODO smukāk izvēlēties, kuru drukāt.
			line.append("19");
		else if (paradigms.size() == 2 && paradigms.contains(7) && paradigms.contains(8))
			// TODO smukāk izvēlēties, kuru drukāt.
			line.append("7");
		else if (paradigms.size() == 2 && paradigms.contains(9) && paradigms.contains(10))
			// TODO smukāk izvēlēties, kuru drukāt.
			line.append("9");
		else line.append("NULL");
		line.append("\t");
		// [4] avoti
		if (entry.sources == null || entry.sources.isEmpty())
		{
			if (entry.hasReference()) line.append("REF");
			else line.append("NULL");
		}
		else line.append(String.join(",", entry.sources.s));
		line.append("\t");
		// [5] joma
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.DOMAIN))
			line.append(String
					.join(",", entry.head.gram.flags.getAll(TKeys.DOMAIN)));
		else line.append("NULL");
		line.append("\t");
		// [6] lietojuma ierobežojumi
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.USAGE_RESTRICTIONS))
			line.append(String.join(",", entry.head.gram.flags
					.getAll(TKeys.USAGE_RESTRICTIONS)));
		else line.append("NULL");
		line.append("\t");
		// [7] valoda
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.LANGUAGE))
			line.append(String.join(",", entry.head.gram.flags.getAll(TKeys.LANGUAGE)));
		else line.append("NULL");
		line.append("\t");

		// [8] nenoteiksmes celms
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.INFINITIVE_STEM))
			line.append(String.join(",", entry.head.gram.flags.getAll(TKeys.INFINITIVE_STEM)));
		else line.append("NULL");
		line.append("\t");
		// [9] tagadnes celmi
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.PRESENT_STEMS))
			line.append(String.join(",", entry.head.gram.flags.getAll(TKeys.PRESENT_STEMS)));
		else line.append("NULL");
		line.append("\t");
		// [10] pagātnes celmi
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.PAST_STEMS))
			line.append(String.join(",", entry.head.gram.flags.getAll(TKeys.PAST_STEMS)));
		else line.append("NULL");
		line.append("\t");
		// [11] priedēklis
		if (entry.head.gram != null && entry.head.gram.flags != null &&
				entry.head.gram.flags.testKey(TKeys.VERB_PREFIX))
			line.append(String.join(",", entry.head.gram.flags.getAll(TKeys.VERB_PREFIX)));
		else line.append("NULL");
		line.append("\t");
		// [12] inflmisc: 3. konjugācijas grupas, 2./5. deklinācijas mijas,
		// 1. konjugācijas paralēlformas, dzimte, daudzskaitlis.
		if (entry.head.gram != null && entry.head.gram.flags != null)
		{
			HashSet<String> inflFlags = new HashSet<>();
			if (entry.head.gram.flags.testKey(TKeys.INFLECTION_WEARDNES))
				inflFlags.addAll(entry.head.gram.flags.getAll(TKeys.INFLECTION_WEARDNES));
			if (entry.head.gram.flags.test(TFeatures.GENDER__CO) ||
					paradigms.size() == 2 && paradigms.contains(7) && paradigms.contains(8) ||
					paradigms.size() == 2 && paradigms.contains(9) && paradigms.contains(10))
				inflFlags.add(TValues.COGENDER);
			if (!inflFlags.contains(TValues.COGENDER) && entry.head.gram.flags.testKey(TKeys.GENDER))
				inflFlags.addAll(entry.head.gram.flags.getAll(TKeys.GENDER));
			/*if (entry.head.gram.flags.testKey(TKeys.ENTRYWORD_WEARDNES))
				entry.head.gram.flags.getAll(TKeys.ENTRYWORD_WEARDNES).stream().filter(
						p -> !p.equals(TValues.CHANGED_PARADIGM)).forEach(inflFlags::add);*/
			if (entry.head.gram.flags.test(TFeatures.ENTRYWORD__PLURAL))
				inflFlags.add(TValues.PLURAL);
			if (entry.head.gram.flags.test(TFeatures.USED_ONLY__SINGULAR))
				inflFlags.add(TValues.SINGULAR);

			if (inflFlags.isEmpty())line.append("NULL");
			else line.append(String.join(",", inflFlags));
		}
		else line.append("NULL");
		line.append("\t");
		// [13] LLVV izrunas
		if (entry.head.lemma.pronunciation != null && entry.head.lemma.pronunciation.length > 0)
			line.append(String.join(",", entry.head.lemma.pronunciation));
		else line.append("NULL");
		line.append("\n");
		// Pārbaudes un brīdinajumi
		String[] fields = line.toString().split("\t");
		if (fields.length != 14)
			System.out.printf("Šķirklim %s ir %d lauki!\n", entry.head.lemma.text, fields.length);
		for (int i = 0; i < fields.length; i++)
		{
			if (fields[i].isEmpty())
				System.out.printf("Šķirklim %s ir %d. lauks ir tukšs!\n", entry.head.lemma.text, i);

		}
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
		out.write(",\n\"Unikālo pārīškarodziņu atslēgu skaits\":" + pairingKeys
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
		HashMap<Tuple<String, String>, Integer> counts = flagCounts.getCounts();
		for (Tuple<String, String> feature : counts.keySet().stream().sorted(
				(t1, t2) -> (t1.first != null && t1.first.equals(t2.first)) ?
						t1.second.compareTo(t2.second) : t1.first.compareTo(t2.first)).toArray(i -> new Tuple[i]))
		{
			out.write("\n\t[\"");
			out.write(JSONObject.escape(feature.first));
			out.write("\", \"");
			out.write(JSONObject.escape(feature.second));
			out.write("\", \"");
			out.write(counts.get(feature).toString());
			out.write("\"],");
		}
		out.write("\n]");

		if (collectInfllWeardness)
		{
			out.write(",\n\"Locīšanas īpatnību uzskaitījums\":[\n");
			out.write(inflWeardness.stream().map(s -> "\"" + JSONObject.escape(s) + "\"")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
			out.write("\n]");
		}

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
		/*if (collectFirstConjAll && firstConj != null && firstConj.size() > 0)
		{
			out.write(",\n\"1. konjugācija\":[\n");
			out.write(firstConj.stream().map(t ->
					"\t[\"" + JSONObject.escape(t.first) + "\", \"" + JSONObject
							.escape(t.second) + "\", \"" + JSONObject
							.escape(t.third) + "\"]")
					.reduce((t1, t2) -> t1 + ",\n" + t2).orElse(""));
			out.write("\n]");
		}*/

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

		if ((collectWithFeatures != null || collectWithRegexp != null || collectWithParadigms != null)
				&& entriesWithSelectedFeature != null
				&& entriesWithSelectedFeature.size() > 0)
		{
			out.write(",\n\"");
			String title = "";
			if (collectWithFeatures != null)
				title = collectWithFeatures.stream()
						.map(f -> f.first + " = " + (f.second == null ? "*" : JSONObject.escape(f.second)))
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
