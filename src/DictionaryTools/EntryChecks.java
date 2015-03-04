/*****************
Autors: Gunārs Danovskis
Pēdējais labošanas datums: 28.05.2014

Klases mērķis:
	Klase EntryChecks ietver sevī visas galvenās metodes, kas pārbauda šķirkļus
 *****************/
package DictionaryTools; //Kopīga paka, kurā ir iekļautas visas klases veiksmīgai programmas darbībai

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EntryChecks
{
	/**
	 * Pārbaude, vai šķirklim netrūkst sķirkļa vārda
	 */
	public static boolean isEntryNameGood(String entry, ArrayList<String> bad)
	{
		boolean good = true; // mainīgais, kas apzīmē vai šķirklis ir labs
		// pirmā vārda līdz atstarpei ieguve
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();

		if(entryName.equals(""))
		{
			bad.add("(Trūkst šķirkļa vārds)" + entry); // slikto šķirkļu saraksta paildināšana
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
	public static void checkBrackets(String entries, ArrayList<String> bad)
	{
		int sqBrackets = 0; // kvadrātiekavu skaits
		int circBracket = 0; // apaļo iekavu skaits
		String entryInf = entries.substring(entries.indexOf(" ")).trim();//šķirkļa info ieguve
		int len = entryInf.length();
		for(int i = 0; i<len; i++) // iet cauri pa vienam simbolam
		{
			if(entryInf.charAt(i) == '[') // atverošās iekavas
			{
				sqBrackets ++; //skaits palielinās par 1
				if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pēdiņas
				{
					sqBrackets --; // skaits samazinās par 1
				}
			}
			if(entryInf.charAt(i) == ']') // aizverošās iekavas
			{
				if(i < len-1) // ja pēdējais simbols
				{
					sqBrackets --; // skaits samazinās par 1
				}
				if(i < len-1) // ja nav pēdējais simbols
				{
					if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pēdiņas
					{
						sqBrackets ++; //skaits palielinās par 1
					}
				}
				if(i == len-1) // ja ir pēdējais simbols
				{
					sqBrackets--; // skaits samazinās par 1
				}
			}

			if(entryInf.charAt(i) == '(') // atverošās iekavas
			{
				circBracket ++;   //skaits palielinās par 1
				if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pēdiņas
				{
					circBracket --; // skaits samazinās par 1
				}
			}
			if(entryInf.charAt(i) == ')') // aizverošās iekavas
			{
				if(i < len-1) // ja nav pēdējais simbols
				{
					if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pēdiņas
					{
						circBracket ++;  //skaits palielinās par 1
					}
				}
				if(i == len-1) // ja ir pēdējais simbols
				{
					circBracket--; // skaits samazinās par 1
				}
			}
		}

		if(sqBrackets != 0) //ja nav līdzsvars
		{
			bad.add("(Problēma ar [] iekavām)" + entries); // pievieno slikto sarakstam
		}
		if(circBracket != 0) //ja nav līdzsvars
		{
			bad.add("(Problēma ar () iekavām)" + entries); // pievieno slikto sarakstam
		}
	}

	// metode kas pārbauda katru šķirkļa vārdu atsevišķi						
	public static void wordByWordCheck(String entry, ArrayList<String> bad)
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
							bad.add("(Divi PI pēc kārtas, bez PN)" + entry); // slikto šķirkļu saraksta papildināšana
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
							bad.add("(Divi PN pēc kārtas, bez PI)" + entry); // slikto šķirkļu saraksta papildināšana
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
							bad.add("(Pirms NG nav atrodams NO)" + entry); // slikto šķirkļu saraksta papildināšana
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
								bad.add("(Starp @2 un @5 jābūt tekstam)" + entry); // slikto šķirkļu saraksta papildināšana
							}
						}
						if(at == 0 || at == 5) // pārbauda vai nav 2 @2 pēc kārtas bez @5 pa vidu
						{
							at = 2;
						}
						else
						{
							bad.add("(Divi @2 pēc kārtas)" + entry); // slikto šķirkļu saraksta papildināšana
						}
					}
					if(word.contains("@5"))
					{
						open = false;
						if(StringUtils.countSpaces(entryInf) > 0)
						{
							if(StringUtils.wordAfter(entryInf, word).contains("@2") && !word.contains(")"))
							{
								bad.add("(Starp @5 un @2 jābūt tekstam)" + entry); // pārbauda starp @5 un @2 ir teksts
								// izņemot ja tos atdala iekavas
							}
						}
						if(at == 0) //pārbauda vai pirms tam ir bijis @2 bez @5, gadījumā ja @5 ir šķirkļa sākumā
						{
							bad.add("(Pirms @5 jābūt @2)" + entry); // slikto šķirkļu saraksta papildināšana
						}
						if(at == 5) //pārbauda vai pirms tam ir bijis @2 bez @5
						{
							bad.add("(Divi @5 pēc kārtas)" + entry); // slikto šķirkļu saraksta papildināšana
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
							bad.add("(@2 un @5 jābūt 1 marķiera robežās)" + entry); // slikto šķirkļu saraksta papildināšana
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
								bad.add("(NO nebeidzas ar pieturzīmi)" + entry); // slikto šķirkļu saraksta papildināšana
							}
							// pārbauda, vai nav tukšs NO, piemēram, NO . FS
							else if (word.matches("^[^\\p{L}]*$") && (prevWord.trim().equals("") || prevWord.trim().equals("NO")))
							{
								bad.add("(NO nesatur tekstu)" + entry);
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
								bad.add("(PN nebeidzas ar pieturzīmi)" + entry); // slikto šķirkļu saraksta papildināšana
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
							bad.add("(Aiz gramatikas marķiera jābūt @2)" + entry);
						}
					}
					if(gramOpen)
					{
						if(Arrays.asList(ident).contains(word)) // ja ir gramatika un sastapts cits marķieris
						{
							bad.add("(Gramatikai jābeidzās ar @5)" + entry);
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
			bad.add("(Gramatikai jābeidzās ar @5)" + entry);
		}
		if(open && !entryInf.contains("@5"))
		{
			bad.add("(Šķirkļa beigās jābūt @5)" + entry);
		}
	}

	/**
	 * Ar marķieri DS saistītās pārbaudes
	 */
	public static void dsCheck(String entry, ArrayList<String> bad)
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
				bad.add("(par daudz DS)" + entry);
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
					bad.add("(nesakriit DE un DS skaiti)" + entry);
				}
			}
		}
		//Pārbauda vai DE sākas ar mazo burtu
		if(Character.isUpperCase(StringUtils.nextCh(entryInf, "DE ")) 
				&& Character.isDigit(StringUtils.nextCh(entryInf, "DE "))
				&& StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "DE ")))
		{
			bad.add("(DE nesākas ar mazo burtu)" + entry);
		}
	}

	/**
	 * Ar marķieri FS saistītās pārbaudes
	 */
	public static void fsCheck(String entry, ArrayList<String> bad)
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
				bad.add("(par daudz FS)" + entry);
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
					bad.add("(nesakriit FR un FS skaiti)" + entry);
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
						bad.add("(nesakriit FR un FN skaits)" + entry);
					}
				}
			}
		}

		//Pārbauda vai FR sākas ar lielo burtu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "FR ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "FR "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "FR ")))
		{
			bad.add("(FR nesākas ar lielo burtu vai skaitli )" + entry);
		}	
	}


	/**
	 * pārbaude, vai aiz LI norādītās atsauces ir atrodams avotu sarakstā
	 */
	public static void liCheck(String entry, ArrayList<String> bad, ReferenceList references)
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
				bad.add("(par daudz LI)" + entry);
			}
			else
			{
				int referCount = 1;
				int goodReferCount = 0;
				if(AfterLI.contains("["))
				{
					int referBegin = AfterLI.indexOf( '[' ) + 1; //iegūst apgabalu kur sākas atsauces
					int referEnd = AfterLI.indexOf( ']' ); //iegūst apgabalu kur beidzas atsauces
					String entryRefer = AfterLI.substring(referBegin, referEnd);// atsauces izgriež ārā no virknes
					int ats_len = entryRefer.length();
					for(int m=0; m<ats_len; m++)
					{
						if(Character.isWhitespace(entryRefer.charAt(m))) // iegūst atsauču skaitu pēc tā cik ir atstarpes
						{
							referCount++;
						}
					}
					//metode salīdzina cik pareizas atsauces ir norādītas
					goodReferCount = references.referCount(entryRefer);
					if(referCount != goodReferCount)
					{
						bad.add("(Problēma ar atsaucēm)" + entry); // jan pareizo un norādīto avotu skaits nesakrīt
					}
				}
				else
				{
					bad.add("(Nav norādītas atsauces)" + entry); // nav bijušas norādītas atsauces
				}
			}
		}	
	}
	
	/**
	 * Pārbaudes šķirkļiem ar PI
	 */
	public static void piCheck(String entry, ArrayList<String> bad)
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
				bad.add("(nesakriit PI un PN skaits)" + entry);
			}
		}

		//Pārbauda vai PI sākas ar lielo burtu vai ciparu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "PI ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "PI "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "PI ")))
		{
			bad.add("(PI nesākas ar lielo burtu vai skaitli )" + entry);
		}
	}

	/**
	 * Ar NS saistītās pārbaudes
	 */
	public static void nsCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		// Atsijaa tos, kam nav NS
		if (!entryInf.matches("^.*\\sNS\\s.*$")  && !entryInf.matches("^..+\\s(DN|CD)\\s.*$"))
		{
			bad.add("(nav NS)" + entry);
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
				bad.add("(par daudz NS)" + entry);
			}
			else
			{
				int noCount = StringUtils.findNumber(afterNs); // skaitlis pēc NS - norāda NO skaitu
				Pattern noPat = Pattern.compile("\\sNO\\s");
				// pārbuda vai skaitlis pēc  NS ir lielāk par 0
				if(noCount < 1)
				{
					bad.add("(NS jābūt liekākam par 0)" + entry);
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
					bad.add("(nesakriit NO skaiti)" + entry);

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
						bad.add("(par daudz NG)" + entry);
					}
				}
			}
		}

		//Pārbauda vai NO sākas ar lielo burtu vai ciparu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "NO ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "NO "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "NO ")))
		{
			bad.add("(NO nesākas ar lielo burtu vai skaitli )" + entry);
		}

		//Pārbauda vai AN sākas ar lielo burtu vai ciparu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "AN ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "AN "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "AN ")))
		{
			bad.add("(AN nesākas ar lielo burtu vai skaitli )" + entry);
		}
	}
	
	// metode kas pārbauda likumsakraības ar IN0 un IN1
	public static void in0In1Check(String entry, ArrayList<String> bad, 
			String [][] InEntries, String [] entries, int i, int index)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();

		//Atsijaa ar sliktajiem indeksiem.
		if(index <= StringUtils.maxIN(InEntries, entryName) && index != 0)
		{
			bad.add("(slikts indekss pie IN)" + entry);
		}

		//Paarbauda, lai nebuutu vientulji entries ar IN 1 
		if (index == 1)
		{
			boolean good_1 = false;
			int sk_len = entries.length;
			int brPoint = 0;
			for(int j = i+1; j<sk_len; j++) // cikls iet cauri šķirkļiem uz priekšu un 
				//pārbauda vai nav vēl kāds tāds pats šķirklis
			{
				int sk_len_j = entries[j].length();
				if(sk_len_j > 2 && StringUtils.countSpaces(entries[j]) > 0)
				{	
					String EntryName2 = entries[j].substring(0, entries[j].indexOf(" "));
					if(EntryName2.equals(entryName)) // ja atrpd tādu pašu škirkļa vārdu
					{
						good_1 = true;
						break;
					}
					if(brPoint > 4) // lai programma neietu cauri līdz galam tiek nosprausts limits 4 šķirkļi uz priekšu
					{
						break;
					}
				}
				brPoint++;
			}
			if(!good_1)
			{
				bad.add("(vientulsh IN 1)" + entry);
			}	
		}
		//ja skjirklis ir ar IN 0
		//Paarbauda, vai jau neeksistee šķirklis ar taadu nosaukumu 
		if (index == 0)
		{	
			int sk_len = entries.length;
			boolean good_0 = true;
			int brPoint = 0;
			for(int j = i+1; j<sk_len; j++)// cikls iet cauri šķirkļiem uz priekšu un 
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
			if(!good_0 || StringUtils.entryExist(InEntries, entryName))
			{
				bad.add("(pastāv vēl šķirkļi ar tādu vārdu)" + entry);
			}
			if(entryInf.matches("^.*\\sCD\\s.*$") || entryInf.matches("^.*\\sDN\\s.*$")) // ja IN0 tad nevar būt CD un DN
			{
				bad.add("(ja IN 0 nevar būt CD | DN)" + entry);
			}
		}
	}
	
	// metode pārbaudavai ir visi nepieciešamie marķieri
	public static void identCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		if(!entryInf.matches("^.*CD\\s.*$") && !entryInf.matches("^.*DN\\s.*$"))
		{
			if(!entryInf.matches("^IN\\s.*$"))
			{
				bad.add("(Nav IN indikatora)" + entry);
			}
			if(!entryInf.matches("^..+\\sNS\\s.*$"))
			{
				bad.add("(Nav NS indikatora)" + entry);
			}
			if(!entryInf.matches("^..+\\sFS\\s.*$"))
			{
				bad.add("(Nav FS indikatora)" + entry);
			}
			if(!entryInf.matches("^..+\\sDS\\s.*$"))
			{
				bad.add("(Nav DS indikatora)" + entry);
			}
			if(!entryInf.matches("^..+\\sNO\\s.*$"))
			{
				bad.add("(Nav neviena NO)" + entry);
			}
		}
		// pārbauda vai CD un DN nav vienlaicīgi
		if (entryInf.matches("^..+\\sCD\\s.*$") && entryInf.matches("^..+\\sDN\\s.*$")) 
		{
			bad.add("(DN un CD nevar būt vienlaicīgi)" + entry);
		}
		// pārbauda vai CD vai DN nav vienlaicīgi ar NS un NO
		if (entryInf.matches("^..+\\sCD\\s.*$") || entryInf.matches("^..+\\sDN\\s.*$"))
		{
			if (entryInf.matches ("^.*\\s(NS|NO)\\s.*$"))
			{
				bad.add("(ja ir CD | DN nevar būt NS|NO)" + entry);
			}

		}
	}
	// metode pārbauda vai šķirklī nav ne pareizi simboli
	public static void langCharCheck(String entry, ArrayList<String> bad)
	{
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		//Parbauda vai skjirkla vaardaa nav nepaziistami simboli
		if(!entryName.matches("^[\\wĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ\\./’'\\(\\)\\<\\>-]*$"))
			bad.add("(Šķikļa vārds satur neparedzētus simbolus)" + entry);
		//Parbauda vai skjirkla info nesatur nepaziistami simboli
		if(!entryInf.matches("^[\\wĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]*$") 
				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			//sliktos simbolus aizvieto vieglākai atrašanai
			entry = entry.replaceAll("[^\\wĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽžŌōŖŗ\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]", "?");
			bad.add("(Skjirkla info satur neparedzeetus simbolus)" + entry);
		}
		//ja satur Ō pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.contains("ō") || entry.contains("Ō")) && !entryInf.matches("^.*\\s(latg|līb)\\.\\s.*$")
				&& !entryInf.matches("^.*\\sRU\\s\\[.*\\].*$") && !entryInf.matches("^.*\\sval.\\s.*$") 
				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			bad.add("(Sķirklis satur ō bet nav latg.|līb.)" + entry);
		}
		//ja satur Ŗ pārbauda vai tas ir lībiešu vai latgļu vārds
		if((entry.contains("ŗ") || entry.contains("Ŗ")) && !entryInf.matches("^.*\\s(latg|līb)\\.\\s.*$") 
				&& !entryInf.matches("^.*\\sRU\\s.*$") && !entryInf.matches("^.*\\sval\\.\\s.*$") 
				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
		{
			bad.add("(Sķirklis satur ŗ bet nav latg.|līb.)" + entry);
		}
	}
	//pārbauda vai aiz IN DS NS FS ir skaitļi
	public static void inDsNsFsNumberCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if(!entryInf.matches("^IN\\s\\d*\\s.*$") && entryInf.matches("^IN\\s.*$"))
		{
			bad.add("(Aiz IN neseko skaitlis)" + entry);
		}
		if(!entryInf.matches("^..+\\sNS\\s\\d*\\s.*$") && entryInf.matches("^..+\\sNS\\s.*$"))
		{
			bad.add("(Aiz NS neseko skaitlis)" + entry);
		}
		if(!entryInf.matches("^..+\\sDS\\s\\d*.*$") && entryInf.matches("^..+\\sDS\\s.*$"))
		{
			bad.add("(Aiz DS neseko skaitlis)" + entry);
		}
		if(!entryInf.matches("^..+\\sFS\\s\\d*\\s.*$") && entryInf.matches("^..+\\sFS\\s.*$"))
		{
			bad.add("(Aiz FS neseko skaitlis)" + entry);
		}

	}
	// pārbauda vai aiz CD esošais vārds ir vārdnīcā
	public static void wordAfterCd(String [] entries, String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches ("^.*\\sCD\\s.*$"))
		{
			String cdWord = StringUtils.wordAfter(entryInf, "CD"); // atrod vārdu aiz CD
			if(!StringUtils.wordExist(entries, cdWord)) // pārbauda vai ir iekšā vardnīcā
			{
				bad.add("(vārds pēc CD nav atrodams)" + entry);
			}
		}
	}
	// pārbauda vai aiz DN esošais vārds ir vārdnīcā
	public static void wordAfterDn(String [] entries, String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		if (entryInf.matches ("^.*\\sDN\\s.*$"))
		{
			String dnWord = StringUtils.wordAfter(entryInf, "DN"); // atrod vārdu aiz DN
			if(!StringUtils.wordExist(entries, dnWord)) // pārbauda vai ir vārdnīcā
			{
				bad.add("(vārds pēc DN nav atrodams)" + entry);
			}
		}
	}
	//metode pārbauda vai ir ievērotas GR likumsakarības
	public static void grCheck(String entry, ArrayList<String> bad)
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
				bad.add("(par daudz GR)" + entry);
			}
			// pārbauda vai GR ir pirms NS un nav atrodams CD un DN
			if(!AfterGR.matches("^.*\\sNS\\s.*$") && !AfterGR.matches("^.*\\s(CD|DN)\\s.*$"))
			{
				bad.add("(GR jāatrodas pirms NS)" + entry); 
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
				bad.add("(nav indikatori CD | DN)" + entry);
			}
			if (cdDnCount > 1) // ja ir atrasti vairāk par vienu
			{
				bad.add("(var eksistēt tikai viens indikators CD | DN)" + entry);
			}
		}
	}
	//metode pārbauda vai ir ievērotas RU likumsakarības
	public static void ruCheck(String entry, ArrayList<String> bad)
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
				bad.add("(par daudz RU)" + entry);
			}
			// pārbauda vai RU ir pirms NS
			if(!AfterRU.matches("^.*\\sNS\\s.*$"))
			{
				bad.add("(RU jāatrodas pirms NS)" + entry);
			}
		}
	}
	// metode pārbauda vai aiz @2 un @5 marķieri ir pareizi konstruēti
	public static void atCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		// pārbauda vai aiz @ seko 2 vai 5
		if(entryInf.matches("^.*@.\\s.*$") && StringUtils.nextCh(entryInf, "@") != '2' && StringUtils.nextCh(entryInf, "@") != '5')
		{
			bad.add("(aiz @ seko nepareizs skaitlis)" + entry);
		}
	}
	// metode pārbauda vai ir ir pareiza gramatika saīsinājumiem un vietvārdiem
	public static void grammarCheck(String entry, ArrayList<String> bad)
	{
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();
		String entryInf = entry.substring(entry.indexOf(" ")).trim();

		//Ja šķirkļa vārds beidzas ar punktu, tad vajadzētu pārbaudīt vai ir "GR @2 saīs. @5".
		if(entryName.charAt(entryName.length() - 1) == '.' 
				&& !entryInf.matches("^.*\\sGR\\s@2.*\\ssaīs\\..*\\s@5\\s.*$"))
		{
			bad.add("(problēma ar saīs.)" + entry);
		}
		//Ja vārds ir vietniekvārds tam jāsākās ar lielo burtu
		if(entryInf.matches("^.*\\sGR\\s@2\\vietv\\.\\s@5\\s.*$") && !Character.isUpperCase(entryName.charAt(0)))
		{
			bad.add("(Šķirkļa vārds nesākas ar lielo burtu)" + entry);
		}
	}
}
