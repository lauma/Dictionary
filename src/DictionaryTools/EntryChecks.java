package DictionaryTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Statisks šķirkļa pārbaudes metožu apvienojums.
 * @author Lauma, Gunārs Danovskis
 */
public class EntryChecks
{
	/**
	 * Pārbaude, vai šķirklim netrūkst sķirkļa vārda
	 */
	public static boolean isEntryNameGood(String entry, int entryID, BadEntries bad)
	{
		boolean good = true; // mainīgais, kas apzīmē vai šķirklis ir labs
		// pirmā vārda līdz atstarpei ieguve
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();

		if(entryName.equals(""))
		{
			bad.addNewEntry(entryID, entry, "Trūkst šķirkļa vārda");
		}
		else
		{
			good = false;
		}
		return good; // atgriež labs vai slikts
	}

	/**
	 * Pārbaude, vai šķirklī ir iekavu līdzsvars
	 */
	public static void checkBrackets(String entries, int entryID, BadEntries bad)
	{
		int sqBrackets = 0; // atvērto kvadrātiekavu skaits
		int circBrackets = 0; // atvērto apaļo iekavu skaits
		String entryInf = entries.trim().substring(entries.indexOf(" ")).trim();// šķirkļa ķermenis
		
		for(int i = 0; i<entryInf.length(); i++) // iet cauri pa vienam simbolam
		{
			// kvadrātiekavas
			if(entryInf.charAt(i) == '[') // atverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				//if (!(i > 0 && i < entryInf.length() - 1 &&
				//		entryInf.charAt(i + 1) == '"' && entryInf.charAt(i - 1) == '"'))
					sqBrackets++; //skaitītājs palielinās par 1
			}
			if(entryInf.charAt(i) == ']') // aizverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				//if (!(i > 0 && i < entryInf.length() - 1 &&
				//		entryInf.charAt(i + 1) == '"' && entryInf.charAt(i - 1) == '"'))
				//{
					sqBrackets--; //skaitītājs samazinās 1
					if (sqBrackets < 0)
						bad.addNewEntry(entryID, entries, "] pirms atbilstošās [");
				//}
			}

			// apaļās iekavas
			if(entryInf.charAt(i) == '(') // atverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				if (!(i > 0 && i < entryInf.length() - 1 &&
						entryInf.charAt(i + 1) == '"' && entryInf.charAt(i - 1) == '"'))
					circBrackets++; //skaitītājs palielinās par 1
			}
			if(entryInf.charAt(i) == ')') // aizverošās iekavas
			{
				// Ja iekava nav pēdiņās.
				if (!(i > 0 && i < entryInf.length() - 1 &&
						entryInf.charAt(i + 1) == '"' && entryInf.charAt(i - 1) == '"'))
				{
					circBrackets--; //skaitītājs samazinās 1	
					if (circBrackets < 0)
						bad.addNewEntry(entryID, entries, ") pirms atbilstošās (");
				}
			}
		}
		if(sqBrackets > 0) //ja nav līdzsvars
			bad.addNewEntry(entryID, entries, "Neaizvērtas []");
		if(circBrackets > 0) //ja nav līdzsvars
			bad.addNewEntry(entryID, entries, "Neaizvērtas ()");
	}

	/**
	 * Vārdu pa vārdam pārbauda dažādas marķieru specifiskās lietas.
	 * FIXME - iespējams, ka šo derētu kaut kā sacirst mazākos gabalos.
	 */
	public static void wordByWordCheck(String entry, int entryID, BadEntries bad)
	{
		//masīvs ar vārdņicas marķieriem
		String[] ident = {"NO","NS","PI","PN","FS","FR","FN","FP","DS","DE","DG","AN","DN","CD","LI"};
		String word = " ";
		String prevWord = "";
		String[] gramIdent = {"NG","AG","PG","FG"}; // masīvs ar gramatikas marķieriem
		// mainīgais ko izmanto PN beigu simbola pārbaudei
		int pnEndSym = 0; 
		// mainīgais ko izmanto NO beigu simbola pārbaudei
		int noEndSym = 0; 
		//mainīgais ko izmanto, lai pārbaudītu, vai pēc NO ir PN un vai pirms NG ir NO
		int no = 0; 
		// mainīgais ko izmanto lai pārbaudītu vai pirms PN ir NO
		int pn = 0;
		// vārda garuma mainīgais
		int wordLen = 0;
		//mainīgais ko izmanto, lai pārbaudītu vai pirms NG ir NO
		int ng = 0;
		//mainīgais ko izmanto, lai pārbaudītu vai pirms pirms PN ir PI
		int pi = 0;
		//mainīgais ko izmanto, lai pārbaudītu @2 un @5 līdzsvaru
		int at = 0;
		boolean gramOpen = false; // vai teksts ir tieši aiz gramtikas infikatora
		boolean open = false; // vai ir bijis @2 indikators
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		int len = entryInf.length();
		int index = 0;
		int spaces = StringUtils.countSpaces(entryInf); // atstarpju skaits simbolu virknē

		while(len > 0 && spaces > 0) // kamēr nav palicis viens vārds
		{
			if(StringUtils.countSpaces(entryInf) > 0)
			{
				prevWord = word;
				word = entryInf.substring(0, entryInf.indexOf(" ")).trim();
				if(StringUtils.countSpaces(entryInf) == 0)
				{
					word = entryInf.substring(0).trim(); // iegūts pirmais vārds virknē
				}
				if(word.length() > 0)
				{
					if(word.equals("PI"))
					{
						if(pi == 0) // pārbauda vai nav bijis PI bez PN pa vidu pirms tam
						{
							pi = 1;
							pn = 0;
						}
						else
						{
							bad.addNewEntry(entryID, entry, "Divi PI pēc kārtas, bez PN");
						}
					}
					if(word.equals("PN")) // pārbauda vai nav bijis PN bez PI pa vidu, pirms tam
					{
						if(pn == 0)
						{
							pn = 1;
							pnEndSym = 1;
							pi = 0;
						}
						else
						{
							bad.addNewEntry(entryID, entry, "Divi PN pēc kārtas, bez PI");
						}
					}
					if(word.equals("NO"))
					{
						no = 1;
						noEndSym = 1;
						ng = 0;
					}
					if(word.equals("NG"))  // pārbauda pirmd NG ir bijis NO
					{
						if(ng == 0 && no == 1)
						{
							ng = 1;
							no = 0;
						}
						else
						{
							bad.addNewEntry(entryID, entry, "Pirms NG nav atrodams NO");
							ng = 0;
						}
					}						
					if(word.contains("@2"))
					{
						open = true; 
						if(StringUtils.countSpaces(entryInf) > 0)
						{
							if(StringUtils.wordAfter(entryInf, word).contains("@5")) // pārbauda starp @2 un @5 ir teksts
							{
								bad.addNewEntry(entryID, entry, "Starp @2 un @5 jābūt tekstam");
							}
						}
						if(at == 0 || at == 5) // pārbauda vai nav 2 @2 pēc kārtas bez @5 pa vidu
						{
							at = 2;
						}
						else
						{
							bad.addNewEntry(entryID, entry, "Divi @2 pēc kārtas");
						}
					}
					if(word.contains("@5"))
					{
						open = false;
						if(StringUtils.countSpaces(entryInf) > 0)
						{
							if(StringUtils.wordAfter(entryInf, word).contains("@2") && !word.contains(")"))
							{
								bad.addNewEntry(entryID, entry, "Starp @5 un @2 jābūt tekstam");
								// izņemot ja tos atdala iekavas
							}
						}
						if(at == 0) //pārbauda vai pirms tam ir bijis @2 bez @5, gadījumā ja @5 ir šķirkļa sākumā
						{
							bad.addNewEntry(entryID, entry, "Pirms @5 jābūt @2");
						}
						if(at == 5) //pārbauda vai pirms tam ir bijis @2 bez @5
						{
							bad.addNewEntry(entryID, entry, "Divi @5 pēc kārtas");
						}
						if(at == 2)
						{
							at = 5;
						}
					}
					if(open) //pārbauda vai @2 un @5 ir viena marķiera robežās
					{
						if(Arrays.asList(ident).contains(word) // ja ir kāds ident masīva locekļiem no marķierem
								|| Arrays.asList(gramIdent).contains(word)) // vai gramident locekļiem
						{
							bad.addNewEntry(entryID, entry, "@2 un @5 jābūt 1 marķiera robežās");
						}
					}
					//beigu pieturzīmes pārbaude
					wordLen = word.length();
					if(noEndSym == 1) // norāda to ka ir bjis NO marķieris
					{
						if(Arrays.asList(ident).contains(StringUtils.wordAfter(entryInf, word))		//
								|| Arrays.asList(gramIdent).contains(StringUtils.wordAfter(entryInf, word)))// kad ir atrasts cits marķieris
						{
							//pārbauda vai vārds pirms tam satur beigu pieturzīmi
							if(word.charAt(wordLen -1) != '.' && word.charAt(wordLen -1) != '?' && word.charAt(wordLen -1) != '!')
							{
								bad.addNewEntry(entryID, entry, "NO nebeidzas ar pieturzīmi");
							}
							// pārbauda, vai nav tukšs NO, piemēram, NO . FS
							else if (word.matches("^[^\\p{L}]*$") && (prevWord.trim().equals("") || prevWord.trim().equals("NO")))
							{
								bad.addNewEntry(entryID, entry, "NO nesatur tekstu");
							}
							
							noEndSym = 0;
						}
					}
					if(pnEndSym == 1) // norāda to ka ir bjis PN marķieris
					{
						if(Arrays.asList(ident).contains(StringUtils.wordAfter(entryInf, word))				 //
								|| Arrays.asList(gramIdent).contains(StringUtils.wordAfter(entryInf, word))) // kad ir atrasts cits marķieris
						{
							//pārbauda vai vārds pirms tam satur beigu pieturzīmi
							if(word.charAt(wordLen -1) != '.' && word.charAt(wordLen -1) != '?'
									&& word.charAt(wordLen -1) != '!')
							{
								bad.addNewEntry(entryID, entry, "PN nebeidzas ar pieturzīmi");
								pnEndSym = 0;
							}
							else
							{
								pnEndSym = 0;
							}
						}
					}
					if(Arrays.asList(gramIdent).contains(word)) // pārbaudes kas saistās ar gramatikas marķierim
					{
						gramOpen = true; // ir bijis gramatikas marķieris
						if(!StringUtils.wordAfter(entryInf, word).contains("@2")) // vai aiz marķiera ir @2
						{
							bad.addNewEntry(entryID, entry, "Aiz gramatikas marķiera jābūt @2");
						}
					}
					if(gramOpen)
					{
						if(Arrays.asList(ident).contains(word)) // ja ir gramatika un sastapts cits marķieris
						{
							bad.addNewEntry(entryID, entry, "Gramatikai jābeidzās ar @5");
							gramOpen = false;
						}
						if(word.contains("@5")) // ja @5 tad gramatika noslēdzas
						{
							gramOpen = false;
						}
					}
				}
				//viens cikls beidzas
				index = entryInf.indexOf(word) + word.length() + 1;  //indekss tiek pārlikts uz nākamo vārdu
				entryInf = entryInf.substring(index); // virkne zaudē pirmo vārdu
				len = entryInf.length(); // jaunās virknes garums
				spaces = StringUtils.countSpaces(entryInf); // jaunās virknes atstarpju skaits
			}
		}
		// ja pēdējais vārds, tiek veiktas pārbaudes vai ir @5 galā
		if(gramOpen)
		{
			bad.addNewEntry(entryID, entry, "Gramatikai jābeidzās ar @5");
		}
		if(open && !entryInf.contains("@5"))
		{
			bad.addNewEntry(entryID, entry, "Šķirkļa beigās jābūt @5");
		}
	}

	/**
	 * Ar marķieri DS saistītās pārbaudes
	 */
	public static void dsCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches("^.*\\sDS\\s.*$")) // regulārā izteiksme pārbauda vai ir DS
		{
			Matcher ds = Pattern.compile("\\sDS(?=\\s)").matcher(entryInf);
			ds.find(); // meklē DS pa visu šķirkli
			int dsPlace = ds.end(); // atrod kur beidzas DS
			String afterDs = entryInf.substring(dsPlace).trim(); // iegūst to daļu kura ir aiz DS
			// Atsijaa tos, kam par daudz DS
			if (afterDs.matches("^.*\\sDS\\s.*$")) // pārbauda vai nav vēlviens DS
			{
				bad.addNewEntry(entryID, entry, "Par daudz DS");
			}
			else
			{
				int deCount = StringUtils.findNumber(afterDs); // skaitlis aiz DS norāda ciks ir DE
				Pattern dePat = Pattern.compile("\\sDE(?=\\s)"); // izteksme DE meklēšanai
				Matcher de = dePat.matcher(entryInf);
				int	allDe = 0;
				while(de.find()) //meklē vius DE pa visu šķirkli
				{
					allDe++;
				}
				de = dePat.matcher(afterDs);
				int deAfterDs = 0;
				while(de.find()) // meklē DE pēc DS
				{
					deAfterDs++;
				}
				// Atsijaa tos, kam nesakriit DE un DS skaiti.
				if(deCount != allDe || deCount != deAfterDs) // pārbauda vai DE ir pareiz skaits
				{
					bad.addNewEntry(entryID, entry, "Nesakrīt DE un DS skaiti");
				}
			}
		}
		//Pārbauda vai DE sākas ar mazo burtu
		if(Character.isUpperCase(StringUtils.nextCh(entryInf, "DE ")) 
				&& Character.isDigit(StringUtils.nextCh(entryInf, "DE "))
				&& StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "DE ")))
		{
			bad.addNewEntry(entryID, entry, "DE nesākas ar mazo burtu");
		}
	}

	/**
	 * Ar marķieri FS saistītās pārbaudes
	 */
	public static void fsCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches("^.*\\sFS\\s.*$")) // regulārā izteiksme pārbauda vai ir FS
		{
			Matcher fs = Pattern.compile("\\sFS(?=\\s)").matcher(entryInf);
			fs.find(); // meklē FS pa visu šķirkli
			int fsPlace = fs.end(); // nosaka kur beidzas FS
			String afterFs = entryInf.substring(fsPlace).trim();

			// Atsijaa tos, kam par daudz FS
			if (afterFs.matches("^.*\\sFS\\s.*$")) // vai nav vēl kāds FS
			{		
				bad.addNewEntry(entryID, entry, "Pārāk daudzi FS");
			}
			else
			{
				int frCount = StringUtils.findNumber(afterFs); // skaitlis pēc FS norāda FR skaitu
				int fnCount = frCount;

				Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
				Matcher fr = frPat.matcher(entryInf); 
				int allFr = 0;
				while(fr.find()) // meklē FR pa visu šķirkli
				{
					allFr++;
				}
				fr = frPat.matcher(afterFs);
				int frAfterFs = 0;
				while(fr.find()) // meklē FR pēc FS
				{
					frAfterFs++;
				}

				// Atsijaa tos, kam nesakriit FR skaiti.
				if(frCount != allFr || frCount != frAfterFs)// ja skaits nav pareizs
				{
					bad.addNewEntry(entryID, entry, "Nesakrīt FR un FS skaits");
				}
				else
				{
					Pattern fnPat = Pattern.compile("\\sFN(?=\\s)"); 
					Matcher fn = fnPat.matcher(entryInf); 
					int allFn = 0;
					while(fn.find()) // meklē FN pa šķirkli
					{
						allFn++;
					}
					fn = fnPat.matcher(afterFs); 
					int fnAfterFs = 0;
					while(fn.find()) // meklē FN pēc FS
					{
						fnAfterFs++;
					}

					// Atsijaa tos, kam nesakriit FR un FN skaiti. var būt  FN <= FR
					if(fnCount != allFn || fnCount != fnAfterFs)
					{
						bad.addNewEntry(entryID, entry, "Nesakrīt FR un FN skaits");
					}
				}
			}
		}

		//Pārbauda vai FR sākas ar lielo burtu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "FR ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "FR "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "FR ")))
		{
			bad.addNewEntry(entryID, entry, "FR jāsākas ar lielo burtu vai skaitli");
		}	
	}


	/**
	 * Pārbaude, vai aiz LI norādītās atsauces ir atrodams avotu sarakstā.
	 */
	public static void liCheck(String entry, int entryID, BadEntries bad, ReferenceList references)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches("^.*\\sLI\\s.*$")) // pārbauda vai ir LI
		{		
			Matcher li = Pattern.compile("\\sLI(?=\\s)").matcher(entryInf);
			li.find();
			int liPlace = li.end();
			String AfterLI = entryInf.substring(liPlace).trim();

			// Atsijaa tos, kam par daudz LI
			if (AfterLI.matches("^.*\\sLI\\s.*$"))
			{	
				bad.addNewEntry(entryID, entry, "Pārāk daudzi LI");
			}
			else
			{
				//int referCount = 1;
				//int goodReferCount = 0;
				if(AfterLI.contains("["))
				{
					int referBegin = AfterLI.indexOf( '[' ) + 1; //iegūst apgabalu kur sākas atsauces
					int referEnd = AfterLI.indexOf( ']' ); //iegūst apgabalu kur beidzas atsauces
					String entryRefer = AfterLI.substring(referBegin, referEnd);// atsauces izgriež ārā no virknes
					//int ats_len = entryRefer.length();
					//for(int m=0; m<ats_len; m++)
					//{
					//	if(Character.isWhitespace(entryRefer.charAt(m))) // iegūst atsauču skaitu pēc tā cik ir atstarpes
					//	{
					//		referCount++;
					//	}
					//}
					//metode salīdzina cik pareizas atsauces ir norādītas
					//goodReferCount = references.referCount(entryRefer);
					//if(referCount != goodReferCount)
					ArrayList<String> unrecognized = references.verifyReferences(entryRefer);
					if (!unrecognized.isEmpty())
					{
						String errorMsg = String.join(", ", unrecognized);
						bad.addNewEntry(entryID, entry, "Neatpazīta(s) atsauce(s): " + errorMsg); // jan pareizo un norādīto avotu skaits nesakrīt
					}
				}
				else
				{
					bad.addNewEntry(entryID, entry, "Nav norādītas atsauces"); // nav bijušas norādītas atsauces
				}
			}
		}	
	}
	
	/**
	 * Pārbaudes šķirkļiem ar PI
	 */
	public static void piCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if(entryInf.matches("^.*\\sPI\\s.*$")) // reg. izteiksme pārbauda vai ir PI
		{
			Matcher pi = Pattern.compile("\\sPI(?=\\s)").matcher(entryInf);
			int piCount = 0;
			while (pi.find()) //tiek atrsti visi PI
			{
				piCount++;
			}
			Matcher pn = Pattern.compile("\\sPN(?=\\s)").matcher(entryInf);
			int pnCount = 0;
			while (pn.find()) // tiek atrsti vis PN
			{
				pnCount++;
			}
			// Atsijaa tos, kam nesakriit PI un PN skaits.
			if (piCount != pnCount)
			{
				bad.addNewEntry(entryID, entry, "Nesakrīt PI un PN skaits");
			}
		}

		//Pārbauda vai PI sākas ar lielo burtu vai ciparu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "PI ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "PI "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "PI ")))
		{
			bad.addNewEntry(entryID, entry, "PI jāsākas ar lielo burtu vai skaitli");
		}
	}

	/**
	 * Ar NS saistītās pārbaudes
	 */
	public static void nsCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		// Atsijaa tos, kam nav NS
		if (!entryInf.matches("^.*\\sNS\\s.*$")  && !entryInf.matches("^..+\\s(DN|CD)\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Trūkst NS");
		}
		else
		{
			Matcher ns = Pattern.compile("\\sNS\\s").matcher(entryInf);
			ns.find(); // atrod NS šķirkļī
			int nsPlace = ns.end(); // atrod NS beigas
			String afterNs = entryInf.substring(nsPlace).trim();

			// Atsijaa tos, kam par daudz NS
			if (afterNs.matches("^.*\\sNS\\s.*$"))
			{		
				bad.addNewEntry(entryID, entry, "Pārāk daudzi NS");
			}
			else
			{
				int noCount = StringUtils.findNumber(afterNs); // skaitlis pēc NS - norāda NO skaitu
				Pattern noPat = Pattern.compile("\\sNO\\s");
				// pārbuda vai skaitlis pēc  NS ir lielāk par 0
				if(noCount < 1)
				{
					bad.addNewEntry(entryID, entry, "NS jābūt lielākam par 0");
				}
				Matcher no = noPat.matcher(entryInf);
				int allNo = 0;
				while (no.find()) // atrod visus NO šķirklī
				{
					allNo++; 
				}
				no = noPat.matcher(afterNs); 
				int noAfterNs = 0;
				while (no.find()) // atrod visus NO pēc NS
				{
					noAfterNs++;
				}
				// Atsijā tos, kam nesakrīt NO skaiti.
				if(noCount != allNo || noCount != noAfterNs)
				{
					bad.addNewEntry(entryID, entry, "Nesakrīt NO skaiti");

				}
				else
				{
					Pattern ngPat = Pattern.compile("\\sNG(?=\\s)");
					Matcher ng = ngPat.matcher(entryInf);
					int allNG = 0;
					while (ng.find()) // atrod visus NG
					{
						allNG++;
					}
					ng = ngPat.matcher(afterNs); // atrod visus NG pēc NS
					int ngAfterNs = 0;
					while (ng.find())
					{
						ngAfterNs++;
					}
					// Atsijaa tos, NG skaits ir lielāks par NO skaitu
					if (noCount < allNG || noCount < ngAfterNs)
					{
						bad.addNewEntry(entryID, entry, "Pārāk daudzi NG");
					}
				}
			}
		}

		//Pārbauda vai NO sākas ar lielo burtu vai ciparu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "NO ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "NO "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "NO ")))
		{
			bad.addNewEntry(entryID, entry, "NO jāsākas ar lielo burtu vai skaitli");
		}

		//Pārbauda vai AN sākas ar lielo burtu vai ciparu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "AN ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "AN "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "AN ")))
		{
			bad.addNewEntry(entryID, entry, "AN jāsākas ar lielo burtu vai skaitli");
		}
	}
	
	/**
	 * Pārbaude, vai dotais ne IN šķirkļa vārds jau nav sastapts iepriekš.
	 */
	public static void notInUnityCheck (String entry, int entryID, BadEntries bad, 
			Map<String, Trio<Integer, String, Integer>> prevIN)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();
		
		if (prevIN.containsKey(entryName))
			bad.addNewEntry(entryID, entry, "Pastāv vēl šķirkļi ar tādu vārdu");
	}
	/**
	 * Pārbaude, vai IN indekss drīkst sekot tam indeksam, kas ir bijis iepriekš:
	 * 0 vai 1 var būt, ja iepriekš šāds šķirkļa vārds nav bijis;
	 * 2 vai vairāk var būt, ja iepriekš ir bijis par vienu mazāks.
	 */
	public static void inNumberCheck(String entry, int entryID, BadEntries bad, 
			Map<String, Trio<Integer, String, Integer>> prevIN, String [] entries, int index)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();

		//Atsijaa ar sliktajiem indeksiem.
		if(!prevIN.containsKey(entryName) && index != 0 && index != 1 ||
				prevIN.containsKey(entryName) &&
				(index != prevIN.get(entryName).first + 1 || prevIN.get(entryName).first == 0))
		{
			bad.addNewEntry(entryID, entry, "Slikts indekss pie IN");
		}

		//ja skjirklis ir ar IN 0
		//Paarbauda, vai jau neeksistee šķirklis ar taadu nosaukumu 
		if (index == 0)
		{	
			int sk_len = entries.length;
			boolean good_0 = true;
			int brPoint = 0;
			for(int j = entryID+1; j<sk_len; j++)// cikls iet cauri šķirkļiem uz priekšu un 
				//pārbauda vai nav vēl kāds tāds pats šķirklis
			{
				int sk_len_j = entries[j].length();
				if(sk_len_j > 2 && StringUtils.countSpaces(entries[j]) > 0)
				{								
					String EntryName2 = entries[j].substring(0, entries[j].indexOf(" "));
					String EntryInf2 = entries[j].substring(entries[j].indexOf(" ")).trim();
					if(EntryName2.equals(entryName) && EntryInf2.matches("^IN\\s.*$")) // ja atrod tādu pašu šķirkla vārdu
					{
						good_0 = false;
						break;
					}
					if(brPoint > 4)
					{
						break;
					}
				}
				brPoint++;
			}
			if(!good_0 || prevIN.containsKey(entryName))
			{
				bad.addNewEntry(entryID, entry, "Pastāv vēl šķirkļi ar tādu vārdu");
			}
			if(entryInf.matches("^.*\\s(CD|DN)\\s.*$")) // ja IN0 tad nevar būt CD un DN
			{
				bad.addNewEntry(entryID, entry, "Ir gan IN 0, gan CD vai DN");
			}
		}
	}
	
	// metode pārbaudavai ir visi nepieciešamie marķieri
	public static void identCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		if(!entryInf.matches("^.*CD\\s.*$") && !entryInf.matches("^.*DN\\s.*$"))
		{
			if(!entryInf.matches("^IN\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "Nav IN indikatora");
			}
			if(!entryInf.matches("^..+\\sNS\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "Nav NS indikatora");
			}
			if(!entryInf.matches("^..+\\sFS\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "Nav FS indikatora");
			}
			if(!entryInf.matches("^..+\\sDS\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "Nav DS indikatora");
			}
			if(!entryInf.matches("^..+\\sNO\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "Nav neviena NO");
			}
		}
		// pārbauda vai CD un DN nav vienlaicīgi
		if (entryInf.matches("^..+\\sCD\\s.*$") && entryInf.matches("^..+\\sDN\\s.*$")) 
		{
			bad.addNewEntry(entryID, entry, "DN un CD vienlaicīgi");
		}
		// pārbauda vai CD vai DN nav vienlaicīgi ar NS un NO
		if (entryInf.matches("^..+\\sCD\\s.*$") || entryInf.matches("^..+\\sDN\\s.*$"))
		{
			if (entryInf.matches ("^.*\\s(NS|NO)\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "Ja ir CD vai DN, nedrīkst būt NS vai NO");
			}

		}
	}
	
	/**
	 * Šķirkļa simbolu pārbaude.
	 */
	public static void langCharCheck(String entry, int entryID, BadEntries bad)
	{
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		//Parbauda vai skjirkla vaardaa nav nepaziistami simboli
		if(!entryName.matches("^[\\wĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ\\./’'\\(\\)\\<\\>-]*$"))
			bad.addNewEntry(entryID, entry, "Šķirkļa vārds satur neparedzētus simbolus");
		//Parbauda vai skjirkla info nesatur nepaziistami simboli
		if(!entryInf.matches("^[\\wĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]*$") 
				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			//sliktos simbolus aizvieto vieglākai atrašanai
			entry = entry.replaceAll("[^\\wĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]", "?");
			bad.addNewEntry(entryID, entry, "Šķirkļa teksts satur neparedzētus simbolus");
		}
		//ja satur Ō pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.contains("ō") || entry.contains("Ō")) && !entryInf.matches("^.*\\s(latg|līb)\\.\\s.*$")
				&& !entryInf.matches("^.*\\sRU\\s\\[.*\\].*$") && !entryInf.matches("^.*\\sval.\\s.*$") 
				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Šķirklis satur ō, bet nav latg.|līb.");
		}
		//ja satur Ŗ pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.contains("ŗ") || entry.contains("Ŗ")) && !entryInf.matches("^.*\\s(latg|līb)\\.\\s.*$") 
				&& !entryInf.matches("^.*\\sRU\\s.*$") && !entryInf.matches("^.*\\sval\\.\\s.*$") 
				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Šķirklis satur ŗ, bet nav latg.|līb.");
		}
	}
	//pārbauda vai aiz IN DS NS FS ir skaitļi
	public static void inDsNsFsNumberCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if(!entryInf.matches("^IN\\s\\d*\\s.*$") && entryInf.matches("^IN\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Aiz IN neseko skaitlis");
		}
		if(!entryInf.matches("^..+\\sNS\\s\\d*\\s.*$") && entryInf.matches("^..+\\sNS\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Aiz NS neseko skaitlis");
		}
		if(!entryInf.matches("^..+\\sDS\\s\\d*.*$") && entryInf.matches("^..+\\sDS\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Aiz DS neseko skaitlis");
		}
		if(!entryInf.matches("^..+\\sFS\\s\\d*\\s.*$") && entryInf.matches("^..+\\sFS\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Aiz FS neseko skaitlis");
		}

	}
	// pārbauda vai aiz CD esošais vārds ir vārdnīcā
	public static void wordAfterCd(String [] entries, String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches ("^.*\\sCD\\s.*$"))
		{
			String cdWord = StringUtils.wordAfter(entryInf, "CD"); // atrod vārdu aiz CD
			if(!StringUtils.wordExist(entries, cdWord)) // pārbauda vai ir iekšā vardnīcā
			{
				bad.addNewEntry(entryID, entry, "Vārds pēc CD nav atrodams");
			}
		}
	}
	// pārbauda vai aiz DN esošais vārds ir vārdnīcā
	public static void wordAfterDn(String [] entries, String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches ("^.*\\sDN\\s.*$"))
		{
			String dnWord = StringUtils.wordAfter(entryInf, "DN"); // atrod vārdu aiz DN
			if(!StringUtils.wordExist(entries, dnWord)) // pārbauda vai ir vārdnīcā
			{
				bad.addNewEntry(entryID, entry, "Vārds pēc DN nav atrodams");
			}
		}
	}
	//metode pārbauda vai ir ievērotas GR likumsakarības
	public static void grCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches("^.*\\sGR\\s.*$")) // ja GR ir teksta vidū
		{
			Matcher gr = Pattern.compile("\\sGR(?=\\s)").matcher(entryInf);
			gr.find();
			int grPlace = gr.end();
			String AfterGR = entryInf.substring(grPlace).trim();
			// Atsijaa tos, kam par daudz GR
			if (AfterGR.matches("^.*\\sGR\\s.*$"))	
			{
				bad.addNewEntry(entryID, entry, "Pārāk daudzi GR");
			}
			// pārbauda vai GR ir pirms NS un nav atrodams CD un DN
			if(!AfterGR.matches("^.*\\sNS\\s.*$") && !AfterGR.matches("^.*\\s(CD|DN)\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "GR jāatrodas pirms NS");
			}
		}

		if (entryInf.matches("^GR\\s.*$")) // ja GR ir teksta sākumā
		{
			Matcher cdDn = Pattern.compile("\\s(DN|CD)(?=\\s)").matcher(entryInf);
			int cdDnCount = 0;
			while (cdDn.find()) // meklē CD un DN pa šķirkli
			{
				cdDnCount++;
			}
			if (cdDnCount == 0) // ja nav atrodams
			{
				bad.addNewEntry(entryID, entry, "Trūkst indikatora CD vai DN");
			}
			if (cdDnCount > 1) // ja ir atrasti vairāk par vienu
			{
				bad.addNewEntry(entryID, entry, "Pārāk daudzi CD un/vai DN");
			}
		}
	}
	//metode pārbauda vai ir ievērotas RU likumsakarības
	public static void ruCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if(entryInf.matches("^.*\\sRU\\s.*$"))
		{
			Matcher ru = Pattern.compile("\\sRU(?=\\s)").matcher(entryInf);
			ru.find();
			int RuPlace = ru.end(); //atrod kur beidzas RU
			String AfterRU = entryInf.substring(RuPlace).trim();
			// Atsijaa tos, kam par daudz RU
			if (AfterRU.matches("^.*\\sRU\\s.*$"))	
			{
				bad.addNewEntry(entryID, entry, "Pārāk daudzi RU");
			}
			// pārbauda vai RU ir pirms NS
			if(!AfterRU.matches("^.*\\sNS\\s.*$"))
			{
				bad.addNewEntry(entryID, entry, "RU jāatrodas pirms NS");
			}
		}
	}
	// metode pārbauda vai aiz @2 un @5 marķieri ir pareizi konstruēti
	public static void atCheck(String entry, int entryID, BadEntries bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		// pārbauda vai aiz @ seko 2 vai 5
		if(entryInf.matches("^.*@.\\s.*$") && StringUtils.nextCh(entryInf, "@") != '2' && StringUtils.nextCh(entryInf, "@") != '5')
		{
			bad.addNewEntry(entryID, entry, "Aiz @ seko nepareizs skaitlis");
		}
	}
	// metode pārbauda vai ir ir pareiza gramatika saīsinājumiem un vietvārdiem
	public static void grammarCheck(String entry, int entryID, BadEntries bad)
	{
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		//Ja šķirkļa vārds beidzas ar punktu, tad vajadzētu pārbaudīt vai ir "GR @2 saīs. @5".
		if(entryName.charAt(entryName.length() - 1) == '.' 
				&& !entryInf.matches("^.*\\sGR\\s@2.*\\ssaīs\\..*\\s@5\\s.*$"))
		{
			bad.addNewEntry(entryID, entry, "Problēma ar saīs.");
		}
		//Ja vārds ir vietniekvārds tam jāsākās ar lielo burtu
		if(entryInf.matches("^.*\\sGR\\s@2\\vietv\\.\\s@5\\s.*$") && !Character.isUpperCase(entryName.charAt(0)))
		{
			bad.addNewEntry(entryID, entry, "Šķirkļa vārds nesākas ar lielo burtu");
		}
	}
}
