package lv.ailab.tezaurs.checker;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lv.ailab.tezaurs.utils.StringUtils;

/**
 * Statisks šķirkļa pārbaudes metožu apvienojums - galvenais pārbaužu bloks.
 * Visas metodes secīgi izsauc Dictionary klase, izmantojot Reflection
 * mehānismu, tāpēc šeit nav paredzēts atrasties metodēm, kas nav pārbaudes.
 * Vienīgie pieļaujamie metožu blakusefekti: izmaiņas slikto šķirkļu masīvā.
 * @author Lauma, Gunārs Danovskis
 */
public class EntryChecks
{
	/**
	 * Kā regulārās izteiksmes klases saturs (bez kvadrātiekavām) uzskaitīti
	 * visi simboli, kas pieļaujami šķirkļa tekstā, izņemot izrunas laukus.
	 */
	public static String contentSymbolRegexp = "a-zA-Z0-9ĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ.,:;!?\\()\\[\\]{}@+*&'\"<>~^ —/%=~–\\-";
	/**
	 * Kā regulārās izteiksmes klases saturs (bez kvadrātiekavām) uzskaitīti
	 * visi simboli, kas pieļaujami šķirkļavārdā.
	 */
	public static String nameSymbolRegexp = "a-zA-Z0-9ĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ./'\\(\\)<>\\-";

    /**
	 * Pārbaude vai šķirklim netrūkst satura.
	 */
	public static void hasContents (Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
		if (entry.contents.equals("") || entry.contents.length() < 4)
			dict.bad.addNewEntry(entry, "Trūkst šķirkļa satura");
	}
	/**
	 * Pārbaude, vai šķirklī ir iekavu līdzsvars
	 */
	public static void bracketing(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		int sqBrackets = 0; // atvērto kvadrātiekavu skaits
		int circBrackets = 0; // atvērto apaļo iekavu skaits
		//String entryInf = entries.trim().substring(entries.indexOf(" ")).trim();// šķirkļa ķermenis
		
		for(int i = 0; i < entry.contents.length(); i++) // iet cauri pa vienam simbolam
		{
			// kvadrātiekavas
			if(entry.contents.charAt(i) == '[') // atverošās iekavas
				sqBrackets++; //skaitītājs palielinās par 1
			if(entry.contents.charAt(i) == ']') // aizverošās iekavas
			{
				sqBrackets--; //skaitītājs samazinās 1
				if (sqBrackets < 0)
					bad.addNewEntry(entry, "\']\' pirms atbilstošās \'[\'");
			}

			// apaļās iekavas
			if(entry.contents.charAt(i) == '(') // atverošās iekavas
				circBrackets++; //skaitītājs palielinās par 1
			if(entry.contents.charAt(i) == ')') // aizverošās iekavas
			{
				circBrackets--; //skaitītājs samazinās 1
				if (circBrackets < 0)
					bad.addNewEntry(entry, "\')\' pirms atbilstošās \'(\'");
			}
		}
		if(sqBrackets > 0) //ja nav līdzsvars
			bad.addNewEntry(entry, "Neaizvērtas []");
		if(circBrackets > 0) //ja nav līdzsvars
			bad.addNewEntry(entry, "Neaizvērtas ()");
	}

	/**
	 * Šeit ir pārbaudes par to, kuriem marķieriem vai to kombinācijām noteikti
	 * ir jābūt.
	 */
	public static void obligatoryMarkers(Dictionary dict, int entryIndex)
	{
		Dictionary.Entry entry = dict.entries[entryIndex];
		BadEntries bad = dict.bad;
		if(!entry.contents.matches("(.*\\s)?(CD|DN)\\s.*"))
		{
			if(!entry.contents.matches("IN\\s.*"))
				bad.addNewEntry(entry, "Nav IN indikatora");
			if(!entry.contents.matches(".*\\sNS\\s.*"))
				bad.addNewEntry(entry, "Nav NS indikatora");
			if(!entry.contents.matches(".*\\sFS\\s.*"))
				bad.addNewEntry(entry, "Nav FS indikatora");
			if(!entry.contents.matches(".*\\sDS\\s.*"))
				bad.addNewEntry(entry, "Nav DS indikatora");
			if(!entry.contents.matches(".*\\sNO\\s.*"))
				bad.addNewEntry(entry, "Nav neviena NO indikatora");
		}
		// pārbauda vai CD un DN nav vienlaicīgi
		if (entry.contents.matches("(.*\\s)?CD\\s.*") && entry.contents.matches("(.*\\s)?DN\\s.*"))
			bad.addNewEntry(entry, "DN un CD vienlaicīgi");

		// pārbauda vai CD vai DN nav vienlaicīgi ar NS un NO
		if (entry.contents.matches("(.*\\s)?(CD|DN)\\s.*") && entry.contents.matches("(.*\\s)?(NS|NO)\\s.*"))
			bad.addNewEntry(entry, " CD vai DN vienlaidīgi ar NS vai NO");
	}

	/**
	 * Pārbaude vai visi marķieri ir rakstīti ar lielajiem burtiem.
	 */
	public static void markerCase(Dictionary dict, int entryIndex)
	{
		Dictionary.Entry entry = dict.entries[entryIndex];
		// Izteiksme, kas meklē visu, kas aptuveni izskatās pēc tagiem.
		Matcher tagsInsens = Pattern.compile(
				"(^|\\s)" + Markers.regexp +"(\\s|$)", Pattern.CASE_INSENSITIVE)
				.matcher(entry.contents);

		//... un kamēr kaut ko atrod...
		while (tagsInsens.find())
		{
			String potTag = tagsInsens.group().trim();
			//... tikmēr pārbauda, vai tas tikai izskatās pēc taga vai arī ir
			// tags (ir ar lieliem burtiem).
			if (!potTag.equals(potTag.toUpperCase()))
			{
				if (potTag.equals("No"))
				{
					if(!entry.contents.substring(0, tagsInsens.start() + 1).matches(
							"(.*\\s)?(NO|AN|PI|FR|[.!?])\\s?"))
						dict.bad.addNewEntry(entry, "Virkne \"" + potTag + "\" izskatās pēc kļūdaina taga");
				}
				else if ((potTag.equals("in") || potTag.equals("an")))
				{
					if (!entry.contents.substring(0, tagsInsens.start() + 1).matches(
							".*[\\s\\(](angļu|vācu|angl\\.|vāc\\.)\\s\"[^\"]*"))
						dict.bad.addNewEntry(entry, "Virkne \"" + potTag + "\" izskatās pēc kļūdaina taga");
				}
				else if (!potTag.equals("no") && !potTag.equals("de"))
					dict.bad.addNewEntry(entry, "Virkne \"" + potTag + "\" izskatās pēc kļūdaina taga");
			}
		}
	}

	/**
	 * Visām gramatikām kopīgie testi: pārbauda, vai gramatika nesatur lielos
	 * burtus.
	 */
	public static void grammar(Dictionary dict, int entryIndex)
	{
		Dictionary.Entry entry = dict.entries[entryIndex];
		String grams = "(^|\\s)(GR|FG|NG|AG|PG)\\s";
		Matcher m = Pattern.compile(grams + "(((?!\\s" + Markers.regexp + "\\s).)*)(\\s" + Markers.regexp + "\\s|$)")
				.matcher(entry.contents);
		while (m.find())
		{
			String gram = m.group(3);
			if (gram.matches(".*\\p{Lu}.*"))
				dict.bad.addNewEntry(entry, "Gramatika satur lielos burtus");
		}

	}

	/**
	 * Ar @2 un @5 marķieriem saistītās pārbāudes.
 	 */
	public static void at(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		// pārbauda vai aiz @ seko 2 vai 5
		if(entry.contents.matches(".*@[13467890].*"))
			bad.addNewEntry(entry, "Aiz @ seko nepareizs cipars");
		if (entry.contents.matches(".*[^\\s(]@\\d.*"))
			bad.addNewEntry(entry, "Pirms @ neseko atstarpe vai iekava");
		if (entry.contents.matches(".*@\\d[^\\s)].*"))
			bad.addNewEntry(entry, "Pēc @ seko kas vairāk par vienu ciparu");

		if (entry.contents.matches(".*\\s@2[^\\p{L}]*@5(\\s.*)?"))
			bad.addNewEntry(entry, "Starp @2 un @5 jābūt tekstam");
        if (entry.contents.matches(".*\\s@5[^\\p{L})]*@2\\s.*"))
            bad.addNewEntry(entry, "Starp @5 un @2 jābūt tekstam vai iekavai");

		if (entry.contents.matches(".*\\s@2\\s((?!@5).)*\\s@2\\s.*"))
			bad.addNewEntry(entry, "Divi @2 pēc kārtas, bez @5");
		if (entry.contents.matches(".*\\s@5\\s((?!@2).)*\\s@5(\\s.*)?"))
			bad.addNewEntry(entry, "Divi @5 pēc kārtas, bez @2");

        if (entry.contents.matches(".*\\s@2\\s((?!\\s@5).)*"))
            bad.addNewEntry(entry, "Šķirkļa beigās jābūt @5");
        if (entry.contents.matches("((?!@2).)*\\s@5\\s.*"))
            bad.addNewEntry(entry, "Pirms @5 jābūt @2");

		if (entry.contents.matches(".*\\s@2\\s((?!@5).)*\\s" + Markers.regexp + "\\s.*"))
			bad.addNewEntry(entry, "Pēc @2 seko nākamais marķieris, nevis @5");

        if (entry.contents.matches(".*\\s(NG|AG|PG|FG)\\s(?!@2).*"))
            bad.addNewEntry(entry, "Aiz \"mazā\" gramatikas marķiera jābūt @2");
	}

	/**
	 * Ar marķieriem DS, DE saistītās pārbaudes.
	 */
	public static void dsDe(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		if (entry.contents.matches("^.*\\sDS\\s.*$")) // regulārā izteiksme pārbauda vai ir DS
		{
			Matcher ds = Pattern.compile("\\sDS(?=\\s)").matcher(entry.contents);
			ds.find(); // meklē DS pa visu šķirkli
			int dsPlace = ds.end(); // atrod kur beidzas DS
			String afterDs = entry.contents.substring(dsPlace).trim(); // iegūst to daļu kura ir aiz DS
			// Atsijaa tos, kam par daudz DS
			if (afterDs.matches("^.*\\sDS\\s.*$")) // pārbauda vai nav vēlviens DS
				bad.addNewEntry(entry, "Par daudz DS");
			else
			{
				int deCount = StringUtils.findNumber(afterDs); // skaitlis aiz DS norāda ciks ir DE
				Pattern dePat = Pattern.compile("\\sDE(?=\\s)"); // izteksme DE meklēšanai
				Matcher de = dePat.matcher(entry.contents);
				int	allDe = 0;
				while(de.find()) //meklē vius DE pa visu šķirkli
					allDe++;
				de = dePat.matcher(afterDs);
				int deAfterDs = 0;
				while(de.find()) // meklē DE pēc DS
					deAfterDs++;
				// Atsijaa tos, kam nesakriit DE un DS skaiti.
				if(deCount != allDe || deCount != deAfterDs) // pārbauda vai DE ir pareiz skaits
					bad.addNewEntry(entry, "Nesakrīt DE un DS skaiti");
			}
		}
		if(entry.contents.matches(".*\\sDS\\s(?![0-9]+(\\s.*|$))"))
			bad.addNewEntry(entry, "Aiz DS neseko skaitlis");
		if (entry.contents.matches(".*\\sDE\\s[^a-zāčēģīķļņŗšūž].*"))
			bad.addNewEntry(entry, "DE jāsākas ar mazo burtu");
	}

	/**
	 * Ar marķieriem FS, FR saistītās pārbaudes.
	 */
	public static void fsFr(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		if (entry.contents.matches("^.*\\sFS\\s.*$")) // regulārā izteiksme pārbauda vai ir FS
		{
			Matcher fs = Pattern.compile("\\sFS(?=\\s)").matcher(entry.contents);
			fs.find(); // meklē FS pa visu šķirkli
			int fsPlace = fs.end(); // nosaka kur beidzas FS
			String afterFs = entry.contents.substring(fsPlace).trim();

			// Atsijaa tos, kam par daudz FS
			if (afterFs.matches("^.*\\sFS\\s.*$")) // vai nav vēl kāds FS
				bad.addNewEntry(entry, "Pārāk daudzi FS");
			else
			{
				int frCount = StringUtils.findNumber(afterFs); // skaitlis pēc FS norāda FR skaitu
				Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
				Matcher fr = frPat.matcher(entry.contents); 
				int allFr = 0;
				while(fr.find()) // meklē FR pa visu šķirkli
					allFr++;
				fr = frPat.matcher(afterFs);
				int frAfterFs = 0;
				while(fr.find()) // meklē FR pēc FS
					frAfterFs++;

				// Atsijaa tos, kam nesakriit FR skaiti.
				if(frCount != allFr || frCount != frAfterFs)// ja skaits nav pareizs
					bad.addNewEntry(entry, "Nesakrīt FR un FS skaits");
				else
				{
					Pattern fnPat = Pattern.compile("\\sFN(?=\\s)"); 
					Matcher fn = fnPat.matcher(entry.contents); 
					int allFn = 0;
					while(fn.find()) // meklē FN pa šķirkli
						allFn++;
					fn = fnPat.matcher(afterFs); 
					int fnAfterFs = 0;
					while(fn.find()) // meklē FN pēc FS
						fnAfterFs++;

					// Atsijaa tos, kam nesakriit FR un FN skaiti. var būt  FN <= FR
					if(frCount != allFn || frCount != fnAfterFs)
						bad.addNewEntry(entry, "Nesakrīt FR un FN skaits");
				}
			}
		}

		if (entry.contents.matches(".*\\s(?!FR)" + Markers.regexp + "\\s((?!\\s" + Markers.regexp +"\\s).)*\\sFG\\s.*"))
			bad.addNewEntry(entry, "FG seko pēc identifikatora, kas nav FR");

		if(entry.contents.matches(".*\\sFS\\s(?![0-9]+(\\s.*|$))"))
			bad.addNewEntry(entry, "Aiz FS neseko skaitlis");
		if (entry.contents.matches(".*\\sFR\\s[^0-9A-ZĀČĒĢĪĶĻŅŠŪŽ(\"].*"))
			bad.addNewEntry(entry, "FR jāsākas ar lielo burtu vai skaitli");
	}

	/**
	 * Pārbaude, vai aiz LI norādītās atsauces ir atrodams avotu sarakstā.
	 */
	public static void li(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		if (!entry.contents.matches("^.*\\sLI\\s.*$"))
            return; // pārbauda vai ir LI

        Matcher li = Pattern.compile("\\sLI(?=\\s)").matcher(entry.contents);
        li.find();
        int liPlace = li.end();
        String AfterLI = entry.contents.substring(liPlace).trim();

        // Atsijaa tos, kam par daudz LI
        if (AfterLI.matches("^.*\\sLI\\s.*$"))
            bad.addNewEntry(entry, "Pārāk daudzi LI");
        else
        {
            if(AfterLI.contains("["))
            {
                int referBegin = AfterLI.indexOf( '[' ) + 1; //iegūst apgabalu kur sākas atsauces
                int referEnd = AfterLI.indexOf( ']' ); //iegūst apgabalu kur beidzas atsauces
                String entryRefer = AfterLI.substring(referBegin, referEnd);// atsauces izgriež ārā no virknes
                ArrayList<String> unrecognized = dict.references.verifyReferences(entryRefer);
                if (!unrecognized.isEmpty())
                {
                    String errorMsg = String.join(", ", unrecognized);
                    bad.addNewEntry(entry, "Neatpazīta(s) atsauce(s): " + errorMsg); // jan pareizo un norādīto avotu skaits nesakrīt
                }
            }
            else
                bad.addNewEntry(entry, "Nav norādītas atsauces"); // nav bijušas norādītas atsauces
        }
	}
	
	/**
	 * Pārbaudes šķirkļiem ar PI un PN
	 */
	public static void piPn(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		if (!entry.contents.matches(".*\\s(PI|PN)\\s.*"))
			return; // Šīs pārbaudes nav attiecināmas uz šo šķirkli.

		// Saskaita PI
		Matcher pi = Pattern.compile("\\sPI(?=\\s)").matcher(entry.contents);
		int piCount = 0;
		while (pi.find()) piCount++;
		Matcher pn = Pattern.compile("\\sPN(?=\\s)").matcher(entry.contents);
		// Saskaita PN
		int pnCount = 0;
		while (pn.find()) pnCount++;
		// Atsijaa tos, kam nesakriit PI un PN skaits.
		if (piCount != pnCount)
			bad.addNewEntry(entry, "Nesakrīt PI un PN skaits");

		if (entry.contents.matches(".*\\sPI\\s((?!PN).)*\\sPI\\s.*"))
			bad.addNewEntry(entry, "Divi PI pēc kārtas, bez PN");
		if (entry.contents.matches(".*\\sPN\\s((?!PI).)*\\sPN\\s.*"))
			bad.addNewEntry(entry, "Divi PN pēc kārtas, bez PI");

        if (entry.contents.matches(".*\\sPI\\s[.!?]?\\s*" + Markers.regexp + "\\s.*|\\s?"))
            bad.addNewEntry(entry, "PI bez tekstuāla satura");
		if (entry.contents.matches(".*\\sPN\\s[.!?]?\\s*" + Markers.regexp + "\\s.*|\\s?"))
			bad.addNewEntry(entry, "PN bez tekstuāla satura");
		if (entry.contents.matches(".*[^\u2013\u2014-]\\sPN\\s.*"))
			bad.addNewEntry(entry, "Pirms PN nav defises");

		if (entry.contents.matches(".*\\sPN\\s((?!" + Markers.regexp + ").)*?[^.!?](\\s" + Markers.regexp + "\\s.*|\\s?)"))
			bad.addNewEntry(entry, "PN nebeidzas ar pieturzīmi");
		if (entry.contents.matches(".*\\sPI\\s[^0-9A-ZĀČĒĢĪĶĻŅŠŪŽ(\"].*"))
			bad.addNewEntry(entry, "PI jāsākas ar lielo burtu vai skaitli");
	}

	/**
	 * Ar NS, NO, NG saistītās pārbaudes
	 */
	public static void nsNoNg(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		if (entry.contents.matches("^.*\\sNS\\s.*$"))
		{
			Matcher ns = Pattern.compile("\\sNS\\s").matcher(entry.contents);
			ns.find(); // atrod NS šķirkļī
			int nsPlace = ns.end(); // atrod NS beigas
			String afterNs = entry.contents.substring(nsPlace).trim();

			// Atsijaa tos, kam par daudz NS
			if (afterNs.matches("^.*\\sNS\\s.*$"))
				bad.addNewEntry(entry, "Pārāk daudzi NS");
			else
			{
				int noCount = StringUtils.findNumber(afterNs); // skaitlis pēc NS - norāda NO skaitu
				Pattern noPat = Pattern.compile("\\sNO\\s");
				// pārbuda vai skaitlis pēc  NS ir lielāk par 0
				if(noCount < 1)
					bad.addNewEntry(entry, "NS jābūt lielākam par 0");
				Matcher no = noPat.matcher(entry.contents);
				int allNo = 0;
				while (no.find()) // atrod visus NO šķirklī
					allNo++;
				no = noPat.matcher(afterNs); 
				int noAfterNs = 0;
				while (no.find()) // atrod visus NO pēc NS
					noAfterNs++;
				// Atsijā tos, kam nesakrīt NO skaiti.
				if(noCount != allNo || noCount != noAfterNs)
					bad.addNewEntry(entry, "Nesakrīt NO skaiti");
				else
				{
					Pattern ngPat = Pattern.compile("\\sNG(?=\\s)");
					Matcher ng = ngPat.matcher(entry.contents);
					int allNG = 0;
					while (ng.find()) // atrod visus NG
						allNG++;
					ng = ngPat.matcher(afterNs); // atrod visus NG pēc NS
					int ngAfterNs = 0;
					while (ng.find())
						ngAfterNs++;
					// Atsijaa tos, NG skaits ir lielāks par NO skaitu
					if (noCount < allNG || noCount < ngAfterNs)
						bad.addNewEntry(entry, "Pārāk daudzi NG");
				}
			}
		}

		if (entry.contents.matches(".*\\sNG\\s((?!NO).)*\\sNG\\s.*"))
			bad.addNewEntry(entry, "Divi NG pēc kārtas, bez NO");
		if (entry.contents.matches("((?!\\sNO\\s).)*\\sNG\\s.*"))
			bad.addNewEntry(entry, "Pirms pirmā NG nav atrodams NO");
		if (entry.contents.matches(".*\\s(?!NO)" + Markers.regexp + "\\s((?!\\s" + Markers.regexp +"\\s).)*\\sNG\\s.*"))
			bad.addNewEntry(entry, "NG seko pēc identifikatora, kas nav NO");

		if(entry.contents.matches(".*\\sNS\\s(?![0-9]+(\\s.*|$))"))
			bad.addNewEntry(entry, "Aiz NS neseko skaitlis");

		if (entry.contents.matches(".*\\sNO\\s[^0-9A-ZĀČĒĢĪĶĻŅŠŪŽ(\"].*"))
			bad.addNewEntry(entry, "NO jāsākas ar lielo burtu vai skaitli");
		if (entry.contents.matches(".*\\sNO\\s\\P{L}*?\\s" + Markers.regexp + ".*"))
			bad.addNewEntry(entry, "NO nesatur tekstu");
		if (entry.contents.matches(".*\\sNO\\s((?!" + Markers.regexp + ").)*?[^.!?](\\s" + Markers.regexp + "\\s.*|\\s?)"))
			bad.addNewEntry(entry, "NO nebeidzas ar pieturzīmi");
	}

	/**
	 * Ar AN saistītās pārbaudes
	 */
	public static void an(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
		if (entry.contents.matches(".*\\sAN\\s[^0-9A-ZĀČĒĢĪĶĻŅŠŪŽ(\"].*"))
			dict.bad.addNewEntry(entry, "AN jāsākas ar lielo burtu vai skaitli");
	}

	/**
     * Pārbaude, vai dotais ne IN šķirkļa vārds jau nav sastapts iepriekš.
     */
	public static void notInUniquety(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
		if (!entry.contents.matches("^IN\\s.*$") && dict.prevIN.containsKey(entry.name))
			dict.bad.addNewEntry(entry, "Pastāv vēl šķirkļi ar tādu vārdu");
	}
	/**
	 * Pārbaude, vai IN indekss drīkst sekot tam indeksam, kas ir bijis iepriekš:
	 * 0 vai 1 var būt, ja iepriekš šāds šķirkļa vārds nav bijis;
	 * 2 vai vairāk var būt, ja iepriekš ir bijis par vienu mazāks.
	 */
    public static void inNumber(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        if(!entry.contents.matches("^IN\\s.*$"))
            return; // Visas pārbaudes attiecināmas tikai uz šķirkļiem, kuros ir IN.

        String bezIn = entry.contents.substring(3).trim();
        int index = StringUtils.findNumber(bezIn);

        // Pārbauda, vai nav IN vispār bez skaitļa.
        if(entry.contents.matches("IN\\s(?![0-9]+(\\s.*|$))"))
            dict.bad.addNewEntry(entry, "Aiz IN neseko skaitlis");

        //Atsijaa ar sliktajiem indeksiem.
        if(!dict.prevIN.containsKey(entry.name) && index != 0 && index != 1 ||
                dict.prevIN.containsKey(entry.name) &&
                        (index != dict.prevIN.get(entry.name).first + 1 ||
                                dict.prevIN.get(entry.name).first == 0))
        {
            dict.bad.addNewEntry(entry, "Slikts indekss pie IN");
            if (dict.prevIN.containsKey(entry.name))
                dict.bad.addNewEntry(
                        dict.entries[dict.prevIN.get(entry.name).third], "Slikts indekss pie IN");
        }

        //ja skjirklis ir ar IN 0
        //Paarbauda, vai jau neeksistee šķirklis ar taadu nosaukumu
        if (index == 0)
        {
            if(dict.prevIN.containsKey(entry.name))
                dict.bad.addNewEntry(entry, "Pastāv vēl šķirkļi ar tādu vārdu");
            if(entry.contents.matches("(.*\\s)?(CD|DN)\\s.*")) // ja IN0 tad nevar būt CD un DN
                dict.bad.addNewEntry(entry, "Ir gan IN 0, gan CD vai DN");
        }
    }

	/**
	 * Šķirkļa simbolu un tipogrāfisku kļūdu pārbaude.
	 */
	public static void characters(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
		//Parbauda vai skjirkla vaardaa nav nepaziistami simboli
		if(!entry.name.matches("[" + nameSymbolRegexp + "]*"))
			dict.bad.addNewEntry(entry, "Šķirkļa vārds satur neparedzētus simbolus");
		//Parbauda vai skjirkla info nesatur nepaziistami simboli
		if(!entry.contents.matches("[" +contentSymbolRegexp + "]*"+
				"(\\sRU\\s\\[[^]]+\\]\\s)?[" + contentSymbolRegexp + "]*"))
		{
			//sliktos simbolus aizvieto vieglākai atrašanai
			String edited = entry.contents;
			if (entry.contents.matches(".*\\sRU\\s\\[[^]]+\\]\\s.*"))
				edited = entry.contents.replaceAll("\\sRU\\s\\[[^]]+\\]", " RU [..]" );
			edited = entry.name + " " + edited.replaceAll(
					"[^" + contentSymbolRegexp + "]", "?");
			dict.bad.addNewEntryFromString(
					entry.id, edited, "Šķirkļa teksts satur neparedzētus simbolus");
		}

		//ja satur Ō pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.fullText.contains("ō") || entry.fullText.contains("Ō"))
				&& !entry.contents.matches("^.*\\s(latg|līb)\\.\\s.*$")
				&& !entry.contents.matches("^.*\\sRU\\s\\[.*\\].*$")
				&& !entry.contents.matches("^.*\\sval.\\s.*$") 
				&& !entry.contents.matches("^.*\\sRU\\s[.*]\\s.*$"))
			dict.bad.addNewEntry(entry, "Šķirklis satur ō, bet nav latg.|līb.");
		//ja satur Ŗ pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.fullText.contains("ŗ") || entry.fullText.contains("Ŗ"))
				&& !entry.contents.matches("^.*\\s(latg|līb)\\.\\s.*$") 
				&& !entry.contents.matches("^.*\\sRU\\s.*$")
				&& !entry.contents.matches("^.*\\sval\\.\\s.*$") 
				&& !entry.contents.matches("^.*\\sRU\\s[.*]\\s.*$"))
			dict.bad.addNewEntry(entry, "Šķirklis satur ŗ, bet nav latg.|līb.");

		// Īpašā prasība.
		if (entry.fullText.matches(".*?\\s"))
			dict.bad.addNewEntry(entry, "Šķirklis beidzas ar tukšumsimbolu");

		// Pārbauda specifiskas tipogrāfiskas kļūdas
		if(entry.fullText.matches("\\*,\\s*,\\*"))
			dict.bad.addNewEntry(entry, "Šķirklis satur divus secīgus komatus");
		if(entry.fullText.matches("\\*,\\.\\*"))
			dict.bad.addNewEntry(entry, "Šķirklis satur punktu tieši aiz komata");
		if(entry.fullText.matches("\\*\\s,\\*"))
			dict.bad.addNewEntry(entry, "Šķirklis satur atstarpi pirms komata");

	}
	
	/**
	 * Pārbaude, vai aiz CD esošais vārds ir vārdnīcā.
	 */
	public static void wordAfterCd(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
		if (entry.contents.matches ("^.*\\sCD\\s.*$"))
		{
			String cdWord = StringUtils.wordAfter(entry.contents, "CD"); // atrod vārdu aiz CD
			if(!dict.entryNames.contains(cdWord)) // pārbauda vai ir iekšā vardnīcā
				dict.bad.addNewEntry(entry, "Vārds pēc CD nav atrodams");
		}
	}
	
	/**
	 * Pārbaude, vai aiz DN esošais vārds ir vārdnīcā.
	 */
	public static void wordAfterDn(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
		if (entry.contents.matches ("^.*\\sDN\\s.*$"))
		{
			String dnWord = StringUtils.wordAfter(entry.contents, "DN"); // atrod vārdu aiz DN
			if(!dict.entryNames.contains(dnWord)) // pārbauda vai ir vārdnīcā
				dict.bad.addNewEntry(entry, "Vārds pēc DN nav atrodams");
		}
	}
	
	//metode pārbauda vai ir ievērotas GR likumsakarības
	// metode pārbauda vai ir ir pareiza gramatika saīsinājumiem un vietvārdiem
	public static void gr(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		if (entry.contents.matches("^.*\\sGR\\s.*$")) // ja GR ir teksta vidū
		{
			Matcher gr = Pattern.compile("\\sGR(?=\\s)").matcher(entry.contents);
			gr.find();
			int grPlace = gr.end();
			String AfterGR = entry.contents.substring(grPlace).trim();
			// Atsijaa tos, kam par daudz GR
			if (AfterGR.matches(".*\\sGR\\s.*"))
				bad.addNewEntry(entry, "Pārāk daudzi GR");
			// pārbauda vai GR ir pirms NS un nav atrodams CD un DN
			if(!AfterGR.matches(".*\\sNS\\s.*") && !AfterGR.matches("(.*\\s)?(CD|DN)\\s.*"))
				bad.addNewEntry(entry, "GR jāatrodas pirms NS");
		}

		if (entry.contents.matches("^GR\\s.*$")) // ja GR ir teksta sākumā
		{
			Matcher cdDn = Pattern.compile("(\\s|^)(DN|CD)(?=\\s)").matcher(entry.contents);
			int cdDnCount = 0;
			while (cdDn.find()) // meklē CD un DN pa šķirkli
				cdDnCount++;
			if (cdDnCount == 0) // ja nav atrodams
				bad.addNewEntry(entry, "Trūkst indikatora CD vai DN");
			if (cdDnCount > 1) // ja ir atrasti vairāk par vienu
				bad.addNewEntry(entry, "Pārāk daudzi CD un/vai DN");
		}

		//Ja šķirkļa vārds beidzas ar punktu, tad vajadzētu pārbaudīt vai ir "GR @2 saīs. @5".
		if(entry.name.charAt(entry.name.length() - 1) == '.'
				&& !entry.contents.matches(".*\\sGR\\s@2.*\\ssaīs\\..*\\s@5\\s.*"))
			bad.addNewEntry(entry, "Problēma ar saīs.");

		//Ja vārds ir vietniekvārds tam jāsākās ar lielo burtu
		if(entry.contents.matches(".*\\sGR\\s@2\\s(((?!(" + Markers.regexp + "|@5|@2)).)+\\s)?vietv\\.\\s.*") && !Character.isUpperCase(entry.name.charAt(0)))
			bad.addNewEntry(entry, "Šķirkļa vārds nesākas ar lielo burtu");
	}

	//metode pārbauda vai ir ievērotas RU likumsakarības
	public static void ru(Dictionary dict, int entryIndex)
    {
        Dictionary.Entry entry = dict.entries[entryIndex];
        BadEntries bad = dict.bad;
		if(entry.contents.matches(".*\\sRU\\s.*"))
		{
			Matcher ru = Pattern.compile("\\sRU(?=\\s)").matcher(entry.contents);
			ru.find();
			int RuPlace = ru.end(); //atrod kur beidzas RU
			String AfterRU = entry.contents.substring(RuPlace).trim();
			// Atsijaa tos, kam par daudz RU
			if (AfterRU.matches(".*\\sRU\\s.*"))
				bad.addNewEntry(entry, "Pārāk daudzi RU");

			// pārbauda vai RU ir pirms NS
			if(!AfterRU.matches(".*\\sNS\\s.*"))
				bad.addNewEntry(entry, "RU jāatrodas pirms NS");
		}
	}
}
