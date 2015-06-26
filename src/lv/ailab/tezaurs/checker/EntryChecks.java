package lv.ailab.tezaurs.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lv.ailab.tezaurs.utils.StringUtils;
import lv.ailab.tezaurs.utils.Trio;

/**
 * Statisks šķirkļa pārbaudes metožu apvienojums.
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
	 * Pārbaude, vai šķirklim netrūkst sķirkļa vārda.
	 */
	public static boolean isEntryNameGood(Dictionary.Entry entry, BadEntries bad)
	{
		if(entry.name.equals(""))
		{
			bad.addNewEntry(entry, "Trūkst šķirkļa vārda");
			return false;
		}
		return true;
	}

	/**
	 * Pārbaude vai šķirklim netrūkst satura.
	 */
	public static void hasContents (Dictionary.Entry entry, BadEntries bad)
	{
		if (entry.contents.equals("") || entry.contents.length() < 4)
			bad.addNewEntry(entry, "Trūkst šķirkļa satura");
	}
	/**
	 * Pārbaude, vai šķirklī ir iekavu līdzsvars
	 */
	public static void bracketing(Dictionary.Entry entry, BadEntries bad)
	{
		int sqBrackets = 0; // atvērto kvadrātiekavu skaits
		int circBrackets = 0; // atvērto apaļo iekavu skaits
		//String entryInf = entries.trim().substring(entries.indexOf(" ")).trim();// šķirkļa ķermenis
		
		for(int i = 0; i < entry.contents.length(); i++) // iet cauri pa vienam simbolam
		{
			// kvadrātiekavas
			if(entry.contents.charAt(i) == '[') // atverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				//if (!(i > 0 && i < entryInf.length() - 1 &&
				//		entryInf.charAt(i + 1) == '"' && entryInf.charAt(i - 1) == '"'))
					sqBrackets++; //skaitītājs palielinās par 1
			}
			if(entry.contents.charAt(i) == ']') // aizverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				//if (!(i > 0 && i < entryInf.length() - 1 &&
				//		entryInf.charAt(i + 1) == '"' && entryInf.charAt(i - 1) == '"'))
				//{
					sqBrackets--; //skaitītājs samazinās 1
					if (sqBrackets < 0)
						bad.addNewEntry(entry, "\']\' pirms atbilstošās \'[\'");
				//}
			}

			// apaļās iekavas
			if(entry.contents.charAt(i) == '(') // atverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				//if (!(i > 0 && i < entry.contents.length() - 1 &&
				//		entry.contents.charAt(i + 1) == '"' && entry.contents.charAt(i - 1) == '"'))
					circBrackets++; //skaitītājs palielinās par 1
			}
			if(entry.contents.charAt(i) == ')') // aizverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				//if (!(i > 0 && i < entry.contents.length() - 1 &&
				//		entry.contents.charAt(i + 1) == '"' && entry.contents.charAt(i - 1) == '"'))
				//{
					circBrackets--; //skaitītājs samazinās 1	
					if (circBrackets < 0)
						bad.addNewEntry(entry, "\')\' pirms atbilstošās \'(\'");
				//}
			}
		}
		if(sqBrackets > 0) //ja nav līdzsvars
			bad.addNewEntry(entry, "Neaizvērtas []");
		if(circBrackets > 0) //ja nav līdzsvars
			bad.addNewEntry(entry, "Neaizvērtas ()");
	}

	/**
	 * Vārdu pa vārdam pārbauda dažādas marķieru specifiskās lietas.
	 * FIXME - iespējams, ka šo derētu kaut kā sacirst mazākos gabalos.
	 */
/*	public static void wordByWord(Dictionary.Entry entry, BadEntries bad)
	{
		//masīvs ar vārdņicas marķieriem
		String[] ident = {"NO","NS","PI","PN","FS","FR","FN","FP","DS","DE","DG","AN","DN","CD","LI"};
		String[] gramIdent = {"NG","AG","PG","FG"}; // masīvs ar gramatikas marķieriem
		//mainīgais ko izmanto, lai pārbaudītu @2 un @5 līdzsvaru
		int at = 0;
		boolean gramOpen = false; // vai teksts ir tieši aiz gramtikas infikatora
		boolean open = false; // vai ir bijis @2 indikators
		String forChecking = entry.contents;
		int len = forChecking.length();
		int spaces = StringUtils.countSpaces(forChecking); // atstarpju skaits simbolu virknē

		while(len > 0 && spaces > 0) // kamēr nav palicis viens vārds
		{
			if (StringUtils.countSpaces(forChecking) <= 0) continue;

			String word = forChecking.substring(0, forChecking.indexOf(" ")).trim();
			if(StringUtils.countSpaces(forChecking) == 0)
				word = forChecking.trim(); // iegūts pirmais vārds virknē
			if(word.length() > 0)
			{
				if(word.contains("@2"))
				{
					open = true;
					//if(StringUtils.countSpaces(forChecking) > 0)
					//{
						// pārbauda starp @2 un @5 ir teksts
						//if(StringUtils.wordAfter(forChecking, word).contains("@5"))
						//	bad.addNewEntry(entry, "Starp @2 un @5 jābūt tekstam");
					//}
					if(at == 0 || at == 5) // pārbauda vai nav 2 @2 pēc kārtas bez @5 pa vidu
						at = 2;
					//else
					//	bad.addNewEntry(entry, "Divi @2 pēc kārtas");
				}
				if(word.contains("@5"))
				{
					open = false;
					//if(StringUtils.countSpaces(forChecking) > 0)
					//{
						// izņemot ja tos atdala iekavas
						//if(StringUtils.wordAfter(forChecking, word).contains("@2") && !word.contains(")"))
						//	bad.addNewEntry(entry, "Starp @5 un @2 jābūt tekstam");
					//}
					//if(at == 0) //pārbauda vai pirms tam ir bijis @2 bez @5, gadījumā ja @5 ir šķirkļa sākumā
					//	bad.addNewEntry(entry, "Pirms @5 jābūt @2");
					//if(at == 5) //pārbauda vai pirms tam ir bijis @2 bez @5
					//	bad.addNewEntry(entry, "Divi @5 pēc kārtas");
					if(at == 2)
						at = 5;
				}
				//if(open) //pārbauda vai @2 un @5 ir viena marķiera robežās
				//{
					//if(Arrays.asList(ident).contains(word) // ja ir kāds ident masīva locekļiem no marķierem
					//		|| Arrays.asList(gramIdent).contains(word)) // vai gramident locekļiem
                    //    bad.addNewEntry(entry, "@2 un @5 jābūt 1 marķiera robežās");
				//}
				//beigu pieturzīmes pārbaude
				if(Arrays.asList(gramIdent).contains(word)) // pārbaudes kas saistās ar gramatikas marķierim
				{
					gramOpen = true; // ir bijis gramatikas marķieris
					if(!StringUtils.wordAfter(forChecking, word).contains("@2")) // vai aiz marķiera ir @2
						bad.addNewEntry(entry,
								"Aiz gramatikas marķiera jābūt @2");
				}
				if(gramOpen)
				{
					if(Arrays.asList(ident).contains(word)) // ja ir gramatika un sastapts cits marķieris
					{
						//bad.addNewEntry(entry, "Gramatikai jābeidzās ar @5");
						gramOpen = false;
					}
					if(word.contains("@5")) // ja @5 tad gramatika noslēdzas
						gramOpen = false;
				}
			}
			//viens cikls beidzas
			int index = forChecking.indexOf(word) + word.length() + 1;  //indekss tiek pārlikts uz nākamo vārdu
			forChecking = forChecking.substring(index); // virkne zaudē pirmo vārdu
			len = forChecking.length(); // jaunās virknes garums
			spaces = StringUtils.countSpaces(forChecking); // jaunās virknes atstarpju skaits
		}
		// ja pēdējais vārds, tiek veiktas pārbaudes vai ir @5 galā
		//if(gramOpen)
		//	bad.addNewEntry(entry, "Gramatikai jābeidzās ar @5");
		//if(open && !forChecking.contains("@5"))
		//	bad.addNewEntry(entry, "Šķirkļa beigās jābūt @5");
	}//*/

	/**
	 * Ar @2 un @5 marķieriem saistītās pārbāudes.
 	 */
	public static void at(Dictionary.Entry entry, BadEntries bad)
	{
		// pārbauda vai aiz @ seko 2 vai 5
		if(entry.contents.matches(".*@[13467890].*"))
			bad.addNewEntry(entry, "Aiz @ seko nepareizs cipars");
		if (entry.contents.matches(".*[^\\s]@.*"))
			bad.addNewEntry(entry, "Pirms @ neseko atstarpe");
		if (entry.contents.matches(".*@\\d[^\\s].*"))
			bad.addNewEntry(entry, "Pēc @ seko kas vairāk par vienu ciparu");

		if (entry.contents.matches(".*\\s@2[^\\p{L}]*@5\\s.*"))
			bad.addNewEntry(entry, "Starp @2 un @5 jābūt tekstam");
        if (entry.contents.matches(".*\\s@5[^\\p{L})]*@2\\s.*"))
            bad.addNewEntry(entry, "Starp @5 un @2 jābūt tekstam vai \')\'");

		if (entry.contents.matches(".*\\s@2\\s((?!@5).)*\\s@2\\s.*"))
			bad.addNewEntry(entry, "Divi @2 pēc kārtas, bez @5");
		if (entry.contents.matches(".*\\s@5\\s((?!@2).)*\\s@5\\s.*"))
			bad.addNewEntry(entry, "Divi @5 pēc kārtas, bez @2");

        if (entry.contents.matches(".*\\s@2\\s((?!@5).)*"))
            bad.addNewEntry(entry, "Šķirkļa beigās jābūt @5");
        if (entry.contents.matches("((?!@2).)*\\s@5\\s.*"))
            bad.addNewEntry(entry, "Pirms @5 jābūt @2");

		if (entry.contents.matches(".*@2\\s((?!@5).)*" + Markers.regexp + ".*"))
			bad.addNewEntry(entry, "Pēc @2 seko nākamais marķieris, nevis @5");

        if (entry.contents.matches(".*(NG|AG|PG|FG)\\s(?!@2).*"))
            bad.addNewEntry(entry, "Aiz \"mazā\" gramatikas marķiera jābūt @2");
	}

	/**
	 * Ar marķieriem DS, DE saistītās pārbaudes.
	 */
	public static void dsDe(Dictionary.Entry entry, BadEntries bad)
	{
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
	public static void fsFr(Dictionary.Entry entry, BadEntries bad)
	{
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
	public static void li(Dictionary.Entry entry, BadEntries bad, ReferenceList references)
	{
		if (entry.contents.matches("^.*\\sLI\\s.*$")) // pārbauda vai ir LI
		{		
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
					ArrayList<String> unrecognized = references.verifyReferences(entryRefer);
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
	}
	
	/**
	 * Pārbaudes šķirkļiem ar PI un PN
	 */
	public static void piPn(Dictionary.Entry entry, BadEntries bad)
	{
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

		if (entry.contents.matches(".*\\sPN\\s((?!" + Markers.regexp + ").)*?[^.!?](\\s" + Markers.regexp + "\\s.*|\\s?)"))
			bad.addNewEntry(entry, "PN nebeidzas ar pieturzīmi");
		if (entry.contents.matches(".*\\sPI\\s[^0-9A-ZĀČĒĢĪĶĻŅŠŪŽ(\"].*"))
			bad.addNewEntry(entry, "PI jāsākas ar lielo burtu vai skaitli");
	}

	/**
	 * Ar NS, NO, NG saistītās pārbaudes
	 */
	public static void nsNoNg(Dictionary.Entry entry, BadEntries bad)
	{
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
	public static void an(Dictionary.Entry entry, BadEntries bad)
	{
		if (entry.contents.matches(".*\\sAN\\s[^0-9A-ZĀČĒĢĪĶĻŅŠŪŽ(\"].*"))
			bad.addNewEntry(entry, "AN jāsākas ar lielo burtu vai skaitli");
	}

	/**
     * Pārbaude, vai dotais ne IN šķirkļa vārds jau nav sastapts iepriekš.
     */
	public static void notInUnity(Dictionary.Entry entry, BadEntries bad,
			Map<String, Trio<Integer, String, Integer>> prevIN)
	{
		
		if (prevIN.containsKey(entry.name))
			bad.addNewEntry(entry, "Pastāv vēl šķirkļi ar tādu vārdu");
	}
	/**
	 * Pārbaude, vai IN indekss drīkst sekot tam indeksam, kas ir bijis iepriekš:
	 * 0 vai 1 var būt, ja iepriekš šāds šķirkļa vārds nav bijis;
	 * 2 vai vairāk var būt, ja iepriekš ir bijis par vienu mazāks.
	 */
	public static void inNumber(Dictionary.Entry entry, Dictionary dict, int index)
	{
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
		}

		//ja skjirklis ir ar IN 0
		//Paarbauda, vai jau neeksistee šķirklis ar taadu nosaukumu 
		if (index == 0)
		{	
			int sk_len = dict.entries.length;
			boolean good_0 = true;
			int brPoint = 0;
			for(int j = entry.id + 1; j < sk_len; j++)// cikls iet cauri šķirkļiem uz priekšu un 
				//pārbauda vai nav vēl kāds tāds pats šķirklis
			{
				int sk_len_j = dict.entries[j].length();
				if(sk_len_j > 2 && StringUtils.countSpaces(dict.entries[j]) > 0)
				{								
					String EntryName2 = dict.entries[j].substring(0, dict.entries[j].indexOf(" "));
					String EntryInf2 = dict.entries[j].substring(dict.entries[j].indexOf(" ")).trim();
					if(EntryName2.equals(entry.name) && EntryInf2.matches("^IN\\s.*$")) // ja atrod tādu pašu šķirkla vārdu
					{
						good_0 = false;
						break;
					}
					if(brPoint > 4)
						break;
				}
				brPoint++;
			}
			if(!good_0 || dict.prevIN.containsKey(entry.name))
				dict.bad.addNewEntry(entry, "Pastāv vēl šķirkļi ar tādu vārdu");
			if(entry.contents.matches("(.*\\s)?(CD|DN)\\s.*")) // ja IN0 tad nevar būt CD un DN
				dict.bad.addNewEntry(entry, "Ir gan IN 0, gan CD vai DN");
		}
	}

	/**
	 * Šeit ir pārbaudes par to, kuriem marķieriem vai to kombinācijām noteikti
	 * ir jābūt.
	 */
	public static void obligatoryMarkers(Dictionary.Entry entry, BadEntries bad)
	{
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
	 * Šķirkļa simbolu pārbaude.
	 */
	public static void langChars(Dictionary.Entry entry, BadEntries bad)
	{
		//Parbauda vai skjirkla vaardaa nav nepaziistami simboli
		if(!entry.name.matches("[" + nameSymbolRegexp + "]*"))
			bad.addNewEntry(entry, "Šķirkļa vārds satur neparedzētus simbolus");
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
			bad.addNewEntryFromString(
					entry.id, edited, "Šķirkļa teksts satur neparedzētus simbolus");
		}

		//ja satur Ō pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.fullText.contains("ō") || entry.fullText.contains("Ō"))
				&& !entry.contents.matches("^.*\\s(latg|līb)\\.\\s.*$")
				&& !entry.contents.matches("^.*\\sRU\\s\\[.*\\].*$")
				&& !entry.contents.matches("^.*\\sval.\\s.*$") 
				&& !entry.contents.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			bad.addNewEntry(entry, "Šķirklis satur ō, bet nav latg.|līb.");
		}
		//ja satur Ŗ pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.fullText.contains("ŗ") || entry.fullText.contains("Ŗ"))
				&& !entry.contents.matches("^.*\\s(latg|līb)\\.\\s.*$") 
				&& !entry.contents.matches("^.*\\sRU\\s.*$")
				&& !entry.contents.matches("^.*\\sval\\.\\s.*$") 
				&& !entry.contents.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			bad.addNewEntry(entry, "Šķirklis satur ŗ, bet nav latg.|līb.");
		}
	}
	
	/**
	 * Pārbaude, vai aiz CD esošais vārds ir vārdnīcā.
	 * FIXME - nepārlasīt vēlreiz visu šķirkļu masīvu.
	 */
	public static void wordAfterCd(Dictionary.Entry entry, String [] entries, BadEntries bad)
	{
		if (entry.contents.matches ("^.*\\sCD\\s.*$"))
		{
			String cdWord = StringUtils.wordAfter(entry.contents, "CD"); // atrod vārdu aiz CD
			if(!StringUtils.wordExist(entries, cdWord)) // pārbauda vai ir iekšā vardnīcā
				bad.addNewEntry(entry, "Vārds pēc CD nav atrodams");
		}
	}
	
	/**
	 * Pārbaude, vai aiz DN esošais vārds ir vārdnīcā.
	 * FIXME - nepārlasīt vēlreiz visu šķirkļu masīvu.
	 */
	public static void wordAfterDn(Dictionary.Entry entry, String [] entries, BadEntries bad)
	{
		if (entry.contents.matches ("^.*\\sDN\\s.*$"))
		{
			String dnWord = StringUtils.wordAfter(entry.contents, "DN"); // atrod vārdu aiz DN
			if(!StringUtils.wordExist(entries, dnWord)) // pārbauda vai ir vārdnīcā
				bad.addNewEntry(entry, "Vārds pēc DN nav atrodams");
		}
	}
	
	//metode pārbauda vai ir ievērotas GR likumsakarības
	// metode pārbauda vai ir ir pareiza gramatika saīsinājumiem un vietvārdiem
	public static void gr(Dictionary.Entry entry, BadEntries bad)
	{
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
				&& !entry.contents.matches("^.*\\sGR\\s@2.*\\ssaīs\\..*\\s@5\\s.*$"))
			bad.addNewEntry(entry, "Problēma ar saīs.");

		//Ja vārds ir vietniekvārds tam jāsākās ar lielo burtu
		if(entry.contents.matches("^.*\\sGR\\s@2\\vietv\\.\\s@5\\s.*$") && !Character.isUpperCase(entry.name.charAt(0)))
			bad.addNewEntry(entry, "Šķirkļa vārds nesākas ar lielo burtu");
	}

	//metode pārbauda vai ir ievērotas RU likumsakarības
	public static void ru(Dictionary.Entry entry, BadEntries bad)
	{
		if(entry.contents.matches("^.*\\sRU\\s.*$"))
		{
			Matcher ru = Pattern.compile("\\sRU(?=\\s)").matcher(entry.contents);
			ru.find();
			int RuPlace = ru.end(); //atrod kur beidzas RU
			String AfterRU = entry.contents.substring(RuPlace).trim();
			// Atsijaa tos, kam par daudz RU
			if (AfterRU.matches("^.*\\sRU\\s.*$"))	
				bad.addNewEntry(entry, "Pārāk daudzi RU");

			// pārbauda vai RU ir pirms NS
			if(!AfterRU.matches("^.*\\sNS\\s.*$"))
				bad.addNewEntry(entry, "RU jāatrodas pirms NS");
		}
	}

	/**
	 * Visām gramatikām kopīgie testi: pārbauda, vai gramatika nesatur lielos
	 * burtus.
	 */
	public static void grammar(Dictionary.Entry entry, BadEntries bad)
	{
        String grams = "(^|\\s)(GR|FG|NG|AG|PG)\\s";
		Matcher m = Pattern.compile(grams + "(((?!\\s" + Markers.regexp + "\\s).)*)(\\s" + Markers.regexp + "\\s|$)")
				.matcher(entry.contents);
		while (m.find())
		{
			String gram = m.group(3);
			if (gram.matches(".*\\p{Lu}.*"))
				bad.addNewEntry(entry, "Gramatika satur lielos burtus");
		}

	}
}
