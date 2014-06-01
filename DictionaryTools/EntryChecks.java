/*****************
Autors: Gunârs Danovskis
Pçdçjais laboðanas datums: 28.05.2014

Klases mçríis:
	Klase EntryChecks ietver sevî visas galvenâs metodes, kas pârbauda ðíirkïus
*****************/
package DictionaryTools; //Kopîga pakotnetne, kurâ ir iekïautas visas klases veiksmîgai programmas darbîbai

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EntryChecks
{
	//metode kas pârbauda vai ðíirklim netrûkst síirkïa vârda
	public static boolean isEntryNameGood(String entry, ArrayList<String> bad)
	{
		boolean good = true; // mainîgais, kas apzîmç vai ðíirklis ir labs
		// pirmâ vârda lîdz atstarpei ieguve
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();
		
		if(entryName.equals(""))
		{
			bad.add("(Trûkst ðíirkïa vârds)" + entry); // slikto ðíirkïu saraksta paildinâðana
		}
		else
		{
			good = false;
		}
		return good; // atgrieþ labs vai slikts
	}
	//metode kas pârbauda vai ðíirklî ir iekavu lîdzsvars						
	public static void checkBrackets(String entries, ArrayList<String> bad)
	{
		int sqBrackets = 0; // kvadrâtiekavu skaits
		int circBracket = 0; // apaïo iekavu skaits
		String entryInf = entries.substring(entries.indexOf(" ")).trim();//ðíirkïa info ieguve
		int len = entryInf.length();
		for(int i = 0; i<len; i++) // iet cauri pa vienam simbolam
		{
			if(entryInf.charAt(i) == '[') // atveroðâs iekavas
			{
				sqBrackets ++; //skaits palielinâs par 1
				if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pçdiòas
				{
					sqBrackets --; // skaits samazinâs par 1
				}
			}
			if(entryInf.charAt(i) == ']') // aizveroðâs iekavas
			{
				if(i < len-1) // ja pçdçjais simbols
				{
					sqBrackets --; // skaits samazinâs par 1
				}
				if(i < len-1) // ja nav pçdçjais simbols
				{
					if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pçdiòas
					{
						sqBrackets ++; //skaits palielinâs par 1
					}
				}
				if(i == len-1) // ja ir pçdçjais simbols
				{
					sqBrackets--; // skaits samazinâs par 1
				}
			}
			
			if(entryInf.charAt(i) == '(') // atveroðâs iekavas
			{
				circBracket ++;   //skaits palielinâs par 1
				if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pçdiòas
				{
					circBracket --; // skaits samazinâs par 1
				}
			}
			if(entryInf.charAt(i) == ')') // aizveroðâs iekavas
			{
				if(i < len-1) // ja nav pçdçjais simbols
				{
					if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj pçdiòas
					{
						circBracket ++;  //skaits palielinâs par 1
					}
				}
				if(i == len-1) // ja ir pçdçjais simbols
				{
					circBracket--; // skaits samazinâs par 1
				}
			}
		}
		
		if(sqBrackets != 0) //ja nav lîdzsvars
		{
			bad.add("(Problçma ar [] iekavâm)" + entries); // pievieno slikto sarakstam
		}
		if(circBracket != 0) //ja nav lîdzsvars
		{
			bad.add("(Problçma ar () iekavâm)" + entries); // pievieno slikto sarakstam
		}
	}
	// metode kas pârbauda katru ðíirkïa vârdu atseviðíi						
	public static void wordByWordCheck(String entries, ArrayList<String> bad)
	{
		//masîvs ar vârdòicas maríieriem
		String[] ident = {"NO","NS","PI","PN","FS","FR","FN","FP","DS","DE","DG","AN","DN","CD","LI"};
		String word = " ";
		String[] gramIdent = {"NG","AG","PG","FG"}; // masîvs ar gramatikas maríieriem
		// mainîgais ko izmanto PN beigu simbola pârbaudei
		int pnEndSym = 0; 
		// mainîgais ko izmanto NO beigu simbola pârbaudei
		int noEndSym = 0; 
		//mainîgais ko izmanto, lai pârbaudîtu, vai pçc NO ir PN un vai pirms NG ir NO
		int no = 0; 
		// mainîgais ko izmanto lai pârbaudîtu vai pirms PN ir NO
		int pn = 0;
		// vârda garuma mainîgais
		int wordLen = 0;
		//mainîgais ko izmanto, lai pârbaudîtu vai pirms NG ir NO
		int ng = 0;
		//mainîgais ko izmanto, lai pârbaudîtu vai pirms pirms PN ir PI
		int pi = 0;
		//mainîgais ko izmanto, lai pârbaudîtu @2 un @5 lîdzsvaru
		int at = 0;
		boolean gramOpen = false; // vai teksts ir tieði aiz gramtikas infikatora
		boolean open = false; // vai ir bijis @2 indikators
		String entryInf = entries.substring(entries.indexOf(" ")).trim();
		int len = entryInf.length();
		int index = 0;
		int spaces = StringUtils.countSpaces(entryInf); // atstarpju skaits simbolu virknç
		
		while(len > 0 && spaces > 0) // kamçr nav palicis viens vârds
		{
			if(StringUtils.countSpaces(entryInf) > 0)
			{
				word = entryInf.substring(0, entryInf.indexOf(" ")).trim();
				if(StringUtils.countSpaces(entryInf) == 0)
				{
					word = entryInf.substring(0).trim(); // iegûts pirmais vârds virknç
				}
				if(word.length() > 0)
				{
					if(word.equals("PI"))
					{
						if(pi == 0) // pârbauda vai nav bijis PI bez PN pa vidu pirms tam
						{
							pi = 1;
							pn = 0;
						}
						else
						{
							bad.add("(Divi PI pçc kârtas, bez PN)" + entries); // slikto ðíirkïu saraksta papildinâðana
						}
					}
					if(word.equals("PN")) // pârbauda vai nav bijis PN bez PI pa vidu, pirms tam
					{
						if(pn == 0)
						{
							pn = 1;
							pnEndSym = 1;
							pi = 0;
						}
						else
						{
							bad.add("(Divi PN pçc kârtas, bez PI)" + entries); // slikto ðíirkïu saraksta papildinâðana
						}
					}
					if(word.equals("NO"))
					{
						no = 1;
						noEndSym = 1;
						ng = 0;
					}
					if(word.equals("NG"))  // pârbauda pirmd NG ir bijis NO
					{
						if(ng == 0 && no == 1)
						{
							ng = 1;
							no = 0;
						}
						else
						{
							bad.add("(Pirms NG nav atrodams NO)" + entries); // slikto ðíirkïu saraksta papildinâðana
							ng = 0;
						}
					}						
					if(word.contains("@2"))
					{
						open = true; 
						if(StringUtils.countSpaces(entryInf) > 0)
						{
							if(StringUtils.wordAfter(entryInf, word).contains("@5")) // pârbauda starp @2 un @5 ir teksts
							{
								bad.add("(Starp @2 un @5 jâbût tekstam)" + entries); // slikto ðíirkïu saraksta papildinâðana
							}
						}
						if(at == 0 || at == 5) // pârbauda vai nav 2 @2 pçc kârtas bez @5 pa vidu
						{
							at = 2;
						}
						else
						{
							bad.add("(Divi @2 pçc kârtas)" + entries); // slikto ðíirkïu saraksta papildinâðana
						}
					}
					if(word.contains("@5"))
					{
						open = false;
						if(StringUtils.countSpaces(entryInf) > 0)
						{
							if(StringUtils.wordAfter(entryInf, word).contains("@2") && !word.contains(")"))
							{
								bad.add("(Starp @5 un @2 jâbût tekstam)" + entries); // pârbauda starp @5 un @2 ir teksts
																					// izòemot ja tos atdala iekavas
							}
						}
						if(at == 0) //pârbauda vai pirms tam ir bijis @2 bez @5, gadîjumâ ja @5 ir ðíirkïa sâkumâ
						{
							bad.add("(Pirms @5 jâbût @2)" + entries); // slikto ðíirkïu saraksta papildinâðana
						}
						if(at == 5) //pârbauda vai pirms tam ir bijis @2 bez @5
						{
							bad.add("(Divi @5 pçc kârtas)" + entries); // slikto ðíirkïu saraksta papildinâðana
						}
						if(at == 2)
						{
							at = 5;
						}
					}
					if(open) //pârbauda vai @2 un @5 ir viena maríiera robeþâs
					{
						if(Arrays.asList(ident).contains(word) // ja ir kâds ident masîva locekïiem no maríierem
						|| Arrays.asList(gramIdent).contains(word)) // vai gramident locekïiem
						{
							bad.add("(@2 un @5 jâbût 1 maríiera robeþâs)" + entries); // slikto ðíirkïu saraksta papildinâðana
						}
					}
					//beigu pieturzîmes pârbaude
					wordLen = word.length();
					if(noEndSym == 1) // norâda to ka ir bjis NO maríieris
					{
						if(Arrays.asList(ident).contains(StringUtils.wordAfter(entryInf, word))		//
						|| Arrays.asList(gramIdent).contains(StringUtils.wordAfter(entryInf, word)))// kad ir atrasts cits maríieris
						{
							//pârbauda vai vârds pirms tam satur beigu pieturzîmi
							if(word.charAt(wordLen -1) != '.' && word.charAt(wordLen -1) != '?' && word.charAt(wordLen -1) != '!')
							{
								bad.add("(NO nebeidzas ar pieturzîmi)" + entries); // slikto ðíirkïu saraksta papildinâðana
								noEndSym = 0;
							}
							else
							{
								noEndSym = 0;
							}
						}
					}
					if(pnEndSym == 1) // norâda to ka ir bjis PN maríieris
					{
						if(Arrays.asList(ident).contains(StringUtils.wordAfter(entryInf, word))				 //
								|| Arrays.asList(gramIdent).contains(StringUtils.wordAfter(entryInf, word))) // kad ir atrasts cits maríieris
						{
							//pârbauda vai vârds pirms tam satur beigu pieturzîmi
							if(word.charAt(wordLen -1) != '.' && word.charAt(wordLen -1) != '?'
							&& word.charAt(wordLen -1) != '!')
							{
								bad.add("(PN nebeidzas ar pieturzîmi)" + entries); // slikto ðíirkïu saraksta papildinâðana
								pnEndSym = 0;
							}
							else
							{
								pnEndSym = 0;
							}
						}
					}
					if(Arrays.asList(gramIdent).contains(word)) // pârbaudes kas saistâs ar gramatikas maríierim
					{
						gramOpen = true; // ir bijis gramatikas maríieris
						if(!StringUtils.wordAfter(entryInf, word).contains("@2")) // vai aiz maríiera ir @2
						{
							bad.add("(Aiz gramatikas maríiera jâbût @2)" + entries);
						}
					}
					if(gramOpen)
					{
						if(Arrays.asList(ident).contains(word)) // ja ir gramatika un sastapts cits maríieris
						{
							bad.add("(Gramatikai jâbeidzâs ar @5)" + entries);
							gramOpen = false;
						}
						if(word.contains("@5")) // ja @5 tad gramatika noslçdzas
						{
							gramOpen = false;
						}
					}
				}
				//viens cikls beidzas
				index = entryInf.indexOf(word) + word.length() + 1;  //indeks tiek pârlikts uz nâkamo vârdu
				entryInf = entryInf.substring(index); // virkne zaudç pirmo vârdu
				len = entryInf.length(); // jaunâs virknes garums
				spaces = StringUtils.countSpaces(entryInf); // jaunâs virknes atstarpju skaits
			}
		}
		// ja pçdçjais vârds, tiek veiktas pârbaudes vai ir @5 galâ
		if(gramOpen)
		{
			bad.add("(Gramatikai jâbeidzâs ar @5)" + entries);
		}
		if(open && !entryInf.contains("@5"))
		{
			bad.add("(Ðíirkïa beigâs jâbût @5)" + entries);
		}
	}
	//metode kas veic pârbaudes saistîtas ar maríieri DS		
	public static void dsCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		
		if (entryInf.matches("^.*\\sDS\\s.*$")) // regulârâ izteiksme pârbauda vai ir DS
		{
			Matcher ds = Pattern.compile("\\sDS(?=\\s)").matcher(entryInf);
			ds.find(); // meklç DS pa visu ðíirkli
			int dsPlace = ds.end(); // atrod kur beidzas DS
			String afterDs = entryInf.substring(dsPlace).trim(); // iegûst to daïu kura ir aiz DS
			// Atsijaa tos, kam par daudz DS
			if (afterDs.matches("^.*\\sDS\\s.*$")) // pârbauda vai nav vçlviens DS
			{		
				bad.add("(par daudz DS)" + entry);
			}
			else
			{
				int deCount = StringUtils.findNumber(afterDs); // skaitlis aiz DS norâda ciks ir DE
				Pattern dePat = Pattern.compile("\\sDE(?=\\s)"); // izteksme DE meklçðanai
				Matcher de = dePat.matcher(entryInf);
				int	allDe = 0;
				while(de.find()) //meklç vius DE pa visu ðíirkli
				{
					allDe++;
				}
				de = dePat.matcher(afterDs);
				int deAfterDs = 0;
				while(de.find()) // meklç DE pçc DS
				{
					deAfterDs++;
				}
				// Atsijaa tos, kam nesakriit DE un DS skaiti.
				if(deCount != allDe || deCount != deAfterDs) // pârbauda vai DE ir pareiz skaits
				{
					bad.add("(nesakriit DE un DS skaiti)" + entry);
				}
			}
		}
		//Pârbauda vai DE sâkas ar mazo burtu
		if(Character.isUpperCase(StringUtils.nextCh(entryInf, "DE ")) 
				&& Character.isDigit(StringUtils.nextCh(entryInf, "DE "))
				&& StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "DE ")))
		{
			bad.add("(DE nesâkas ar mazo burtu)" + entry);
		}
	}
	//metode kas veic pârbaudes saistîtas ar maríieri FS	
	public static void fsCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
							 
		if (entryInf.matches("^.*\\sFS\\s.*$")) // regulârâ izteiksme pârbauda vai ir FS
		{
			Matcher fs = Pattern.compile("\\sFS(?=\\s)").matcher(entryInf);
			fs.find(); // meklç FS pa visu ðíirkli
			int fsPlace = fs.end(); // nosaka kur beidzas FS
			String afterFs = entryInf.substring(fsPlace).trim();
			
			// Atsijaa tos, kam par daudz FS
			if (afterFs.matches("^.*\\sFS\\s.*$")) // vai nav vçl kâds FS
			{		
				bad.add("(par daudz FS)" + entry);
			}
			else
			{
				int frCount = StringUtils.findNumber(afterFs); // skaitlis pçc FS norâda FR skaitu
				int fnCount = frCount;
	
				Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
				Matcher fr = frPat.matcher(entryInf); 
				int allFr = 0;
				while(fr.find()) // meklç FR pa visu ðíirkli
				{
					allFr++;
				}
				fr = frPat.matcher(afterFs);
				int frAfterFs = 0;
				while(fr.find()) // meklç FR pçc FS
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
					while(fn.find()) // meklç FN pa ðíirkli
					{
						allFn++;
					}
					fn = fnPat.matcher(afterFs); 
					int fnAfterFs = 0;
					while(fn.find()) // meklç FN pçc FS
					{
						fnAfterFs++;
					}
					
					// Atsijaa tos, kam nesakriit FR un FN skaiti. var bût  FN <= FR
					if(fnCount != allFn || fnCount != fnAfterFs)
					{
						bad.add("(nesakriit FR un FN skaits)" + entry);
					}
				}
			}
		}
		
		//Pârbauda vai FR sâkas ar lielo burtu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "FR ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "FR "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "FR ")))
		{
			bad.add("(FR nesâkas ar lielo burtu vai skaitli )" + entry);
		}	
	}
	//metode kas pârbauda vai aiz LI norâdîtâs atsauces ir atrodams avotu sarakstâ
	public static void liCheck(String entry, ArrayList<String> bad, String [] references)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
									
				if (entryInf.matches("^.*\\sLI\\s.*$")) // pârbauda vai ir LI
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
							int referBegin = AfterLI.indexOf( '[' ) + 1; //iegûst apgabalu kur sâkas atsauces
							int referEnd = AfterLI.indexOf( ']' ); //iegûst apgabalu kur beidzas atsauces
							String entryRefer = AfterLI.substring(referBegin, referEnd);// atsauces izgrieþ ârâ no virknes
							int ats_len = entryRefer.length();
							for(int m=0; m<ats_len; m++)
							{
								if(Character.isWhitespace(entryRefer.charAt(m))) // iegûst atsauèu skaitu pçc tâ cik ir atstarpes
								{
								referCount++;
								}
							}
							//metode salîdzina cik pareizas atsauces ir norâdîtas
							goodReferCount = StringUtils.referCount(references, entryRefer);
							if(referCount != goodReferCount)
							{
								bad.add("(Problçma ar atsaucçm)" + entry); // jan pareizo un norâdîto avotu skaits nesakrît
							}
						}
						else
						{
							bad.add("(Nav norâdîtas atsauces)" + entry); // nav bijuðas norâdîtas atsauces
						}
					}
				}	
			}
			//metode kas pârbauda ðíirkïus ar PI
			public static void piCheck(String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
									
				if(entryInf.matches("^.*\\sPI\\s.*$")) // reg. izteiksme pârbauda vai ir PI
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
		
				//Pârbauda vai PI sâkas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.nextCh(entryInf, "PI ")) 
						&& !Character.isDigit(StringUtils.nextCh(entryInf, "PI "))
						&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "PI ")))
				{
					bad.add("(PI nesâkas ar lielo burtu vai skaitli )" + entry);
				}
			}
			
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
					ns.find(); // atrod NS ðíirkïî
					int nsPlace = ns.end(); // atrod NS beigas
					String afterNs = entryInf.substring(nsPlace).trim();
					
					// Atsijaa tos, kam par daudz NS
					if (afterNs.matches("^.*\\sNS\\s.*$"))
					{		
						bad.add("(par daudz NS)" + entry);
					}
					else
					{
						int noCount = StringUtils.findNumber(afterNs); // skaitlis pçc NS - norâda NO skaitu
						Pattern noPat = Pattern.compile("\\sNO\\s");
						// pârbuda vai skaitlis pçc  NS ir lielâk par 0
						if(noCount < 1)
						{
							bad.add("(NS jâbût liekâkam par 0)" + entry);
						}
						Matcher no = noPat.matcher(entryInf);
						int allNo = 0;
						while (no.find()) // atrod visus NO ðíirklî
						{
							allNo++; 
						}
						no = noPat.matcher(afterNs); 
						int noAfterNs = 0;
						while (no.find()) // atrod visus NO pçc NS
						{
							noAfterNs++;
						}
						// Atsijâ tos, kam nesakrît NO skaiti.
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
							ng = ngPat.matcher(afterNs); // atrod visus NG pçc NS
							int ngAfterNs = 0;
							while (ng.find())
							{
								ngAfterNs++;
							}
							// Atsijaa tos, NG skaits ir lielâks par NO skaitu
							if (noCount < allNG || noCount < ngAfterNs)
							{
								bad.add("(par daudz NG)" + entry);
							}
						}
					}
				}
		
				//Pârbauda vai NO sâkas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.nextCh(entryInf, "NO ")) 
						&& !Character.isDigit(StringUtils.nextCh(entryInf, "NO "))
						&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "NO ")))
				{
					bad.add("(NO nesâkas ar lielo burtu vai skaitli )" + entry);
				}
				
				//Pârbauda vai AN sâkas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.nextCh(entryInf, "AN ")) 
						&& !Character.isDigit(StringUtils.nextCh(entryInf, "AN "))
						&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "AN ")))
				{
					bad.add("(AN nesâkas ar lielo burtu vai skaitli )" + entry);
				}
			}
			// metode kas pârbauda likumsakraîbas ar IN0 un IN1
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
					for(int j = i+1; j<sk_len; j++) // cikls iet cauri ðíirkïiem uz priekðu un 
													//pârbauda vai nav vçl kâds tâds pats ðíirklis
					{
						int sk_len_j = entries[j].length();
						if(sk_len_j > 2 && StringUtils.countSpaces(entries[j]) > 0)
						{	
							String EntryName2 = entries[j].substring(0, entries[j].indexOf(" "));
							if(EntryName2.equals(entryName)) // ja atrpd tâdu paðu ðkirkïa vârdu
							{
								good_1 = true;
								break;
							}
							if(brPoint > 4) // lai programma neietu cauri lîdz galam tiek nosprausts limits 4 ðíirkïi uz priekðu
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
				//Paarbauda, vai jau neeksistee ðíirklis ar taadu nosaukumu 
				if (index == 0)
				{	
					int sk_len = entries.length;
					boolean good_0 = true;
					int brPoint = 0;
					for(int j = i+1; j<sk_len; j++)// cikls iet cauri ðíirkïiem uz priekðu un 
													//pârbauda vai nav vçl kâds tâds pats ðíirklis
					{
						int sk_len_j = entries[j].length();
						if(sk_len_j > 2 && StringUtils.countSpaces(entries[j]) > 0)
						{								
							String EntryName2 = entries[j].substring(0, entries[j].indexOf(" "));
							String EntryInf2 = entries[j].substring(entries[j].indexOf(" ")).trim();
							if(EntryName2.equals(entryName) && EntryInf2.matches("^IN\\s.*$")) // ja atrod tâdu paðu ðíirkla vârdu
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
						bad.add("(pastâv vçl ðíirkïi ar tâdu vârdu)" + entry);
					}
					if(entryInf.matches("^.*\\sCD\\s.*$") || entryInf.matches("^.*\\sDN\\s.*$")) // ja IN0 tad nevar bût CD un DN
					{
						bad.add("(ja IN 0 nevar bût CD | DN)" + entry);
					}
				}
			}
			// metode pârbaudavai ir visi nepiecieðamie maríieri
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
				// pârbauda vai CD un DN nav vienlaicîgi
				if (entryInf.matches("^..+\\sCD\\s.*$") && entryInf.matches("^..+\\sDN\\s.*$")) 
    			{
    				bad.add("(DN un CD nevar bût vienlaicîgi)" + entry);
    			}
				// pârbauda vai CD vai DN nav vienlaicîgi ar NS un NO
    			if (entryInf.matches("^..+\\sCD\\s.*$") || entryInf.matches("^..+\\sDN\\s.*$"))
    			{
    				if (entryInf.matches ("^.*\\s(NS|NO)\\s.*$"))
    				{
    					bad.add("(ja ir CD | DN nevar bût NS|NO)" + entry);
    				}
    		
    			}
			}
			// metode pârbauda vai ðíirklî nav ne pareizi simboli
			public static void langCharCheck(String entry, ArrayList<String> bad)
			{
				String entryName = entry.substring(0, entry.indexOf(" ")).trim();
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				//Parbauda vai skjirkla vaardaa nav nepaziistami simboli
    			if(!entryName.matches("^[\\wÂâÈèÇçÌìÎîÍíÏïÒòÐðÛûÞþÔôªº\\./’'\\(\\)\\<\\>-]*$"))
    				bad.add("(Ðíikïa vârds satur neparedzçtus simbolus)" + entry);
    			//Parbauda vai skjirkla info nesatur nepaziistami simboli
    			if(!entryInf.matches("^[\\wÂâÈèÇçÌìÎîÍíÏïÒòÐðÛûÞþÔôªº\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]*$") 
    					&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				//sliktos simbolus aizvieto vieglâkai atraðanai
    				entry = entry.replaceAll("[^\\wÂâÈèÇçÌìÎîÍíÏïÒòÐðÛûÞþÔôªº\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]", "?");
    				bad.add("(Skjirkla info satur neparedzeetus simbolus)" + entry);
    			}
    			//ja satur Ô pârbauda vai tas ir lîbieðu vai latgïu vârds
    			if((entry.contains("ô") || entry.contains("Ô")) && !entryInf.matches("^.*\\s(latg|lîb)\\.\\s.*$")
    				&& !entryInf.matches("^.*\\sRU\\s\\[.*\\].*$") && !entryInf.matches("^.*\\sval.\\s.*$") 
    				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				bad.add("(Síirklis satur ô bet nav latg.|lîb.)" + entry);
    			}
    			//ja satur ª pârbauda vai tas ir lîbieðu vai latgïu vârds
    			if((entry.contains("º") || entry.contains("ª")) && !entryInf.matches("^.*\\s(latg|lîb)\\.\\s.*$") 
    			&& !entryInf.matches("^.*\\sRU\\s.*$") && !entryInf.matches("^.*\\sval\\.\\s.*$") 
    			&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				bad.add("(Síirklis satur º bet nav latg.|lîb.)" + entry);
    			}
			}
			//pârbauda vai aiz IN DS NS FS ir skaitïi
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
			// pârbauda vai aiz CD esoðais vârds ir vârdnîcâ
			public static void wordAfterCd(String [] entries, String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				if (entryInf.matches ("^.*\\sCD\\s.*$"))
    			{
    				String cdWord = StringUtils.wordAfter(entryInf, "CD"); // atrod vârdu aiz CD
    				if(!StringUtils.wordExist(entries, cdWord)) // pârbauda vai ir iekðâ vardnîcâ
    				{
    					bad.add("(vârds pçc CD nav atrodams)" + entry);
    				}
    			}
			}
			// pârbauda vai aiz DN esoðais vârds ir vârdnîcâ
			public static void wordAfterDn(String [] entries, String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				if (entryInf.matches ("^.*\\sDN\\s.*$"))
    			{
    				String dnWord = StringUtils.wordAfter(entryInf, "DN"); // atrod vârdu aiz DN
    				if(!StringUtils.wordExist(entries, dnWord)) // pârbauda vai ir vârdnîcâ
    				{
    					bad.add("(vârds pçc DN nav atrodams)" + entry);
    				}
    			}
			}
			//metode pârbauda vai ir ievçrotas GR likumsakarîbas
			public static void grCheck(String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				if (entryInf.matches("^.*\\sGR\\s.*$")) // ja GR ir teksta vidû
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
    				// pârbauda vai GR ir pirms NS un nav atrodams CD un DN
    				if(!AfterGR.matches("^.*\\sNS\\s.*$") && !AfterGR.matches("^.*\\s(CD|DN)\\s.*$"))
    				{
    					bad.add("(GR jâatrodas pirms NS)" + entry); 
    				}
    			}
				
				if (entryInf.matches("^GR\\s.*$")) // ja GR ir teksta sâkumâ
    			{
    				Matcher cdDn = Pattern.compile("\\s(DN|CD)(?=\\s)").matcher(entryInf);
    				int cdDnCount = 0;
    				while (cdDn.find()) // meklç CD un DN pa ðíirkli
    				{
    					cdDnCount++;
    				}
    				if (cdDnCount == 0) // ja nav atrodams
    				{
    					bad.add("(nav indikatori CD | DN)" + entry);
    				}
    				if (cdDnCount > 1) // ja ir atrasti vairâk par vienu
    				{
    					bad.add("(var eksistçt tikai viens indikators CD | DN)" + entry);
    				}
    			}
			}
			//metode pârbauda vai ir ievçrotas RU likumsakarîbas
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
    				// pârbauda vai RU ir pirms NS
    				if(!AfterRU.matches("^.*\\sNS\\s.*$"))
    				{
    					bad.add("(RU jâatrodas pirms NS)" + entry);
    				}
    			}
			}
			// metode pârbauda vai aiz @2 un @5 maríieri ir pareizi konstruçti
			public static void atCheck(String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				// pârbauda vai aiz @ seko 2 vai 5
				if(entryInf.matches("^.*@.\\s.*$") && StringUtils.nextCh(entryInf, "@") != '2' && StringUtils.nextCh(entryInf, "@") != '5')
    			{
					bad.add("(aiz @ seko nepareizs skaitlis)" + entry);
    			}
			}
			// metode pârbauda vai ir ir pareiza gramatika saîsinâjumiem un vietvârdiem
			public static void grammarCheck(String entry, ArrayList<String> bad)
			{
				String entryName = entry.substring(0, entry.indexOf(" ")).trim();
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				//Ja ðíirkïa vârds beidzas ar punktu, tad vajadzçtu pârbaudît vai ir "GR @2 saîs. @5".
    			if(entryName.charAt(entryName.length() - 1) == '.' 
    					&& !entryInf.matches("^.*\\sGR\\s@2.*\\ssaîs\\..*\\s@5\\s.*$"))
    			{
    				bad.add("(problçma ar saîs.)" + entry);
    			}
    			//Ja vârds ir vietniekvârds tam jâsâkâs ar lielo burtu
    			if(entryInf.matches("^.*\\sGR\\s@2\\vietv\\.\\s@5\\s.*$") && !Character.isUpperCase(entryName.charAt(0)))
    			{
    				bad.add("(Ðíirkïa vârds nesâkas ar lielo burtu)" + entry);
    			}
			}
}
