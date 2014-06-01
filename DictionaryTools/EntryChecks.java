/*****************
Autors: Gun�rs Danovskis
P�d�jais labo�anas datums: 28.05.2014

Klases m�r�is:
	Klase EntryChecks ietver sev� visas galven�s metodes, kas p�rbauda ��irk�us
*****************/
package DictionaryTools; //Kop�ga pakotnetne, kur� ir iek�autas visas klases veiksm�gai programmas darb�bai

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EntryChecks
{
	//metode kas p�rbauda vai ��irklim netr�kst s�irk�a v�rda
	public static boolean isEntryNameGood(String entry, ArrayList<String> bad)
	{
		boolean good = true; // main�gais, kas apz�m� vai ��irklis ir labs
		// pirm� v�rda l�dz atstarpei ieguve
		String entryName = entry.substring(0, entry.indexOf(" ")).trim();
		
		if(entryName.equals(""))
		{
			bad.add("(Tr�kst ��irk�a v�rds)" + entry); // slikto ��irk�u saraksta paildin��ana
		}
		else
		{
			good = false;
		}
		return good; // atgrie� labs vai slikts
	}
	//metode kas p�rbauda vai ��irkl� ir iekavu l�dzsvars						
	public static void checkBrackets(String entries, ArrayList<String> bad)
	{
		int sqBrackets = 0; // kvadr�tiekavu skaits
		int circBracket = 0; // apa�o iekavu skaits
		String entryInf = entries.substring(entries.indexOf(" ")).trim();//��irk�a info ieguve
		int len = entryInf.length();
		for(int i = 0; i<len; i++) // iet cauri pa vienam simbolam
		{
			if(entryInf.charAt(i) == '[') // atvero��s iekavas
			{
				sqBrackets ++; //skaits palielin�s par 1
				if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj p�di�as
				{
					sqBrackets --; // skaits samazin�s par 1
				}
			}
			if(entryInf.charAt(i) == ']') // aizvero��s iekavas
			{
				if(i < len-1) // ja p�d�jais simbols
				{
					sqBrackets --; // skaits samazin�s par 1
				}
				if(i < len-1) // ja nav p�d�jais simbols
				{
					if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj p�di�as
					{
						sqBrackets ++; //skaits palielin�s par 1
					}
				}
				if(i == len-1) // ja ir p�d�jais simbols
				{
					sqBrackets--; // skaits samazin�s par 1
				}
			}
			
			if(entryInf.charAt(i) == '(') // atvero��s iekavas
			{
				circBracket ++;   //skaits palielin�s par 1
				if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj p�di�as
				{
					circBracket --; // skaits samazin�s par 1
				}
			}
			if(entryInf.charAt(i) == ')') // aizvero��s iekavas
			{
				if(i < len-1) // ja nav p�d�jais simbols
				{
					if(entryInf.charAt(i+1) == '"' && entryInf.charAt(i-1) == '"') // ja iekavu ieskauj p�di�as
					{
						circBracket ++;  //skaits palielin�s par 1
					}
				}
				if(i == len-1) // ja ir p�d�jais simbols
				{
					circBracket--; // skaits samazin�s par 1
				}
			}
		}
		
		if(sqBrackets != 0) //ja nav l�dzsvars
		{
			bad.add("(Probl�ma ar [] iekav�m)" + entries); // pievieno slikto sarakstam
		}
		if(circBracket != 0) //ja nav l�dzsvars
		{
			bad.add("(Probl�ma ar () iekav�m)" + entries); // pievieno slikto sarakstam
		}
	}
	// metode kas p�rbauda katru ��irk�a v�rdu atsevi��i						
	public static void wordByWordCheck(String entries, ArrayList<String> bad)
	{
		//mas�vs ar v�rd�icas mar�ieriem
		String[] ident = {"NO","NS","PI","PN","FS","FR","FN","FP","DS","DE","DG","AN","DN","CD","LI"};
		String word = " ";
		String[] gramIdent = {"NG","AG","PG","FG"}; // mas�vs ar gramatikas mar�ieriem
		// main�gais ko izmanto PN beigu simbola p�rbaudei
		int pnEndSym = 0; 
		// main�gais ko izmanto NO beigu simbola p�rbaudei
		int noEndSym = 0; 
		//main�gais ko izmanto, lai p�rbaud�tu, vai p�c NO ir PN un vai pirms NG ir NO
		int no = 0; 
		// main�gais ko izmanto lai p�rbaud�tu vai pirms PN ir NO
		int pn = 0;
		// v�rda garuma main�gais
		int wordLen = 0;
		//main�gais ko izmanto, lai p�rbaud�tu vai pirms NG ir NO
		int ng = 0;
		//main�gais ko izmanto, lai p�rbaud�tu vai pirms pirms PN ir PI
		int pi = 0;
		//main�gais ko izmanto, lai p�rbaud�tu @2 un @5 l�dzsvaru
		int at = 0;
		boolean gramOpen = false; // vai teksts ir tie�i aiz gramtikas infikatora
		boolean open = false; // vai ir bijis @2 indikators
		String entryInf = entries.substring(entries.indexOf(" ")).trim();
		int len = entryInf.length();
		int index = 0;
		int spaces = StringUtils.countSpaces(entryInf); // atstarpju skaits simbolu virkn�
		
		while(len > 0 && spaces > 0) // kam�r nav palicis viens v�rds
		{
			if(StringUtils.countSpaces(entryInf) > 0)
			{
				word = entryInf.substring(0, entryInf.indexOf(" ")).trim();
				if(StringUtils.countSpaces(entryInf) == 0)
				{
					word = entryInf.substring(0).trim(); // ieg�ts pirmais v�rds virkn�
				}
				if(word.length() > 0)
				{
					if(word.equals("PI"))
					{
						if(pi == 0) // p�rbauda vai nav bijis PI bez PN pa vidu pirms tam
						{
							pi = 1;
							pn = 0;
						}
						else
						{
							bad.add("(Divi PI p�c k�rtas, bez PN)" + entries); // slikto ��irk�u saraksta papildin��ana
						}
					}
					if(word.equals("PN")) // p�rbauda vai nav bijis PN bez PI pa vidu, pirms tam
					{
						if(pn == 0)
						{
							pn = 1;
							pnEndSym = 1;
							pi = 0;
						}
						else
						{
							bad.add("(Divi PN p�c k�rtas, bez PI)" + entries); // slikto ��irk�u saraksta papildin��ana
						}
					}
					if(word.equals("NO"))
					{
						no = 1;
						noEndSym = 1;
						ng = 0;
					}
					if(word.equals("NG"))  // p�rbauda pirmd NG ir bijis NO
					{
						if(ng == 0 && no == 1)
						{
							ng = 1;
							no = 0;
						}
						else
						{
							bad.add("(Pirms NG nav atrodams NO)" + entries); // slikto ��irk�u saraksta papildin��ana
							ng = 0;
						}
					}						
					if(word.contains("@2"))
					{
						open = true; 
						if(StringUtils.countSpaces(entryInf) > 0)
						{
							if(StringUtils.wordAfter(entryInf, word).contains("@5")) // p�rbauda starp @2 un @5 ir teksts
							{
								bad.add("(Starp @2 un @5 j�b�t tekstam)" + entries); // slikto ��irk�u saraksta papildin��ana
							}
						}
						if(at == 0 || at == 5) // p�rbauda vai nav 2 @2 p�c k�rtas bez @5 pa vidu
						{
							at = 2;
						}
						else
						{
							bad.add("(Divi @2 p�c k�rtas)" + entries); // slikto ��irk�u saraksta papildin��ana
						}
					}
					if(word.contains("@5"))
					{
						open = false;
						if(StringUtils.countSpaces(entryInf) > 0)
						{
							if(StringUtils.wordAfter(entryInf, word).contains("@2") && !word.contains(")"))
							{
								bad.add("(Starp @5 un @2 j�b�t tekstam)" + entries); // p�rbauda starp @5 un @2 ir teksts
																					// iz�emot ja tos atdala iekavas
							}
						}
						if(at == 0) //p�rbauda vai pirms tam ir bijis @2 bez @5, gad�jum� ja @5 ir ��irk�a s�kum�
						{
							bad.add("(Pirms @5 j�b�t @2)" + entries); // slikto ��irk�u saraksta papildin��ana
						}
						if(at == 5) //p�rbauda vai pirms tam ir bijis @2 bez @5
						{
							bad.add("(Divi @5 p�c k�rtas)" + entries); // slikto ��irk�u saraksta papildin��ana
						}
						if(at == 2)
						{
							at = 5;
						}
					}
					if(open) //p�rbauda vai @2 un @5 ir viena mar�iera robe��s
					{
						if(Arrays.asList(ident).contains(word) // ja ir k�ds ident mas�va locek�iem no mar�ierem
						|| Arrays.asList(gramIdent).contains(word)) // vai gramident locek�iem
						{
							bad.add("(@2 un @5 j�b�t 1 mar�iera robe��s)" + entries); // slikto ��irk�u saraksta papildin��ana
						}
					}
					//beigu pieturz�mes p�rbaude
					wordLen = word.length();
					if(noEndSym == 1) // nor�da to ka ir bjis NO mar�ieris
					{
						if(Arrays.asList(ident).contains(StringUtils.wordAfter(entryInf, word))		//
						|| Arrays.asList(gramIdent).contains(StringUtils.wordAfter(entryInf, word)))// kad ir atrasts cits mar�ieris
						{
							//p�rbauda vai v�rds pirms tam satur beigu pieturz�mi
							if(word.charAt(wordLen -1) != '.' && word.charAt(wordLen -1) != '?' && word.charAt(wordLen -1) != '!')
							{
								bad.add("(NO nebeidzas ar pieturz�mi)" + entries); // slikto ��irk�u saraksta papildin��ana
								noEndSym = 0;
							}
							else
							{
								noEndSym = 0;
							}
						}
					}
					if(pnEndSym == 1) // nor�da to ka ir bjis PN mar�ieris
					{
						if(Arrays.asList(ident).contains(StringUtils.wordAfter(entryInf, word))				 //
								|| Arrays.asList(gramIdent).contains(StringUtils.wordAfter(entryInf, word))) // kad ir atrasts cits mar�ieris
						{
							//p�rbauda vai v�rds pirms tam satur beigu pieturz�mi
							if(word.charAt(wordLen -1) != '.' && word.charAt(wordLen -1) != '?'
							&& word.charAt(wordLen -1) != '!')
							{
								bad.add("(PN nebeidzas ar pieturz�mi)" + entries); // slikto ��irk�u saraksta papildin��ana
								pnEndSym = 0;
							}
							else
							{
								pnEndSym = 0;
							}
						}
					}
					if(Arrays.asList(gramIdent).contains(word)) // p�rbaudes kas saist�s ar gramatikas mar�ierim
					{
						gramOpen = true; // ir bijis gramatikas mar�ieris
						if(!StringUtils.wordAfter(entryInf, word).contains("@2")) // vai aiz mar�iera ir @2
						{
							bad.add("(Aiz gramatikas mar�iera j�b�t @2)" + entries);
						}
					}
					if(gramOpen)
					{
						if(Arrays.asList(ident).contains(word)) // ja ir gramatika un sastapts cits mar�ieris
						{
							bad.add("(Gramatikai j�beidz�s ar @5)" + entries);
							gramOpen = false;
						}
						if(word.contains("@5")) // ja @5 tad gramatika nosl�dzas
						{
							gramOpen = false;
						}
					}
				}
				//viens cikls beidzas
				index = entryInf.indexOf(word) + word.length() + 1;  //indeks tiek p�rlikts uz n�kamo v�rdu
				entryInf = entryInf.substring(index); // virkne zaud� pirmo v�rdu
				len = entryInf.length(); // jaun�s virknes garums
				spaces = StringUtils.countSpaces(entryInf); // jaun�s virknes atstarpju skaits
			}
		}
		// ja p�d�jais v�rds, tiek veiktas p�rbaudes vai ir @5 gal�
		if(gramOpen)
		{
			bad.add("(Gramatikai j�beidz�s ar @5)" + entries);
		}
		if(open && !entryInf.contains("@5"))
		{
			bad.add("(��irk�a beig�s j�b�t @5)" + entries);
		}
	}
	//metode kas veic p�rbaudes saist�tas ar mar�ieri DS		
	public static void dsCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
		
		if (entryInf.matches("^.*\\sDS\\s.*$")) // regul�r� izteiksme p�rbauda vai ir DS
		{
			Matcher ds = Pattern.compile("\\sDS(?=\\s)").matcher(entryInf);
			ds.find(); // mekl� DS pa visu ��irkli
			int dsPlace = ds.end(); // atrod kur beidzas DS
			String afterDs = entryInf.substring(dsPlace).trim(); // ieg�st to da�u kura ir aiz DS
			// Atsijaa tos, kam par daudz DS
			if (afterDs.matches("^.*\\sDS\\s.*$")) // p�rbauda vai nav v�lviens DS
			{		
				bad.add("(par daudz DS)" + entry);
			}
			else
			{
				int deCount = StringUtils.findNumber(afterDs); // skaitlis aiz DS nor�da ciks ir DE
				Pattern dePat = Pattern.compile("\\sDE(?=\\s)"); // izteksme DE mekl��anai
				Matcher de = dePat.matcher(entryInf);
				int	allDe = 0;
				while(de.find()) //mekl� vius DE pa visu ��irkli
				{
					allDe++;
				}
				de = dePat.matcher(afterDs);
				int deAfterDs = 0;
				while(de.find()) // mekl� DE p�c DS
				{
					deAfterDs++;
				}
				// Atsijaa tos, kam nesakriit DE un DS skaiti.
				if(deCount != allDe || deCount != deAfterDs) // p�rbauda vai DE ir pareiz skaits
				{
					bad.add("(nesakriit DE un DS skaiti)" + entry);
				}
			}
		}
		//P�rbauda vai DE s�kas ar mazo burtu
		if(Character.isUpperCase(StringUtils.nextCh(entryInf, "DE ")) 
				&& Character.isDigit(StringUtils.nextCh(entryInf, "DE "))
				&& StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "DE ")))
		{
			bad.add("(DE nes�kas ar mazo burtu)" + entry);
		}
	}
	//metode kas veic p�rbaudes saist�tas ar mar�ieri FS	
	public static void fsCheck(String entry, ArrayList<String> bad)
	{
		String entryInf = entry.substring(entry.indexOf(" ")).trim();
							 
		if (entryInf.matches("^.*\\sFS\\s.*$")) // regul�r� izteiksme p�rbauda vai ir FS
		{
			Matcher fs = Pattern.compile("\\sFS(?=\\s)").matcher(entryInf);
			fs.find(); // mekl� FS pa visu ��irkli
			int fsPlace = fs.end(); // nosaka kur beidzas FS
			String afterFs = entryInf.substring(fsPlace).trim();
			
			// Atsijaa tos, kam par daudz FS
			if (afterFs.matches("^.*\\sFS\\s.*$")) // vai nav v�l k�ds FS
			{		
				bad.add("(par daudz FS)" + entry);
			}
			else
			{
				int frCount = StringUtils.findNumber(afterFs); // skaitlis p�c FS nor�da FR skaitu
				int fnCount = frCount;
	
				Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
				Matcher fr = frPat.matcher(entryInf); 
				int allFr = 0;
				while(fr.find()) // mekl� FR pa visu ��irkli
				{
					allFr++;
				}
				fr = frPat.matcher(afterFs);
				int frAfterFs = 0;
				while(fr.find()) // mekl� FR p�c FS
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
					while(fn.find()) // mekl� FN pa ��irkli
					{
						allFn++;
					}
					fn = fnPat.matcher(afterFs); 
					int fnAfterFs = 0;
					while(fn.find()) // mekl� FN p�c FS
					{
						fnAfterFs++;
					}
					
					// Atsijaa tos, kam nesakriit FR un FN skaiti. var b�t  FN <= FR
					if(fnCount != allFn || fnCount != fnAfterFs)
					{
						bad.add("(nesakriit FR un FN skaits)" + entry);
					}
				}
			}
		}
		
		//P�rbauda vai FR s�kas ar lielo burtu
		if(Character.isLowerCase(StringUtils.nextCh(entryInf, "FR ")) 
				&& !Character.isDigit(StringUtils.nextCh(entryInf, "FR "))
				&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "FR ")))
		{
			bad.add("(FR nes�kas ar lielo burtu vai skaitli )" + entry);
		}	
	}
	//metode kas p�rbauda vai aiz LI nor�d�t�s atsauces ir atrodams avotu sarakst�
	public static void liCheck(String entry, ArrayList<String> bad, String [] references)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
									
				if (entryInf.matches("^.*\\sLI\\s.*$")) // p�rbauda vai ir LI
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
							int referBegin = AfterLI.indexOf( '[' ) + 1; //ieg�st apgabalu kur s�kas atsauces
							int referEnd = AfterLI.indexOf( ']' ); //ieg�st apgabalu kur beidzas atsauces
							String entryRefer = AfterLI.substring(referBegin, referEnd);// atsauces izgrie� �r� no virknes
							int ats_len = entryRefer.length();
							for(int m=0; m<ats_len; m++)
							{
								if(Character.isWhitespace(entryRefer.charAt(m))) // ieg�st atsau�u skaitu p�c t� cik ir atstarpes
								{
								referCount++;
								}
							}
							//metode sal�dzina cik pareizas atsauces ir nor�d�tas
							goodReferCount = StringUtils.referCount(references, entryRefer);
							if(referCount != goodReferCount)
							{
								bad.add("(Probl�ma ar atsauc�m)" + entry); // jan pareizo un nor�d�to avotu skaits nesakr�t
							}
						}
						else
						{
							bad.add("(Nav nor�d�tas atsauces)" + entry); // nav biju�as nor�d�tas atsauces
						}
					}
				}	
			}
			//metode kas p�rbauda ��irk�us ar PI
			public static void piCheck(String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
									
				if(entryInf.matches("^.*\\sPI\\s.*$")) // reg. izteiksme p�rbauda vai ir PI
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
		
				//P�rbauda vai PI s�kas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.nextCh(entryInf, "PI ")) 
						&& !Character.isDigit(StringUtils.nextCh(entryInf, "PI "))
						&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "PI ")))
				{
					bad.add("(PI nes�kas ar lielo burtu vai skaitli )" + entry);
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
					ns.find(); // atrod NS ��irk��
					int nsPlace = ns.end(); // atrod NS beigas
					String afterNs = entryInf.substring(nsPlace).trim();
					
					// Atsijaa tos, kam par daudz NS
					if (afterNs.matches("^.*\\sNS\\s.*$"))
					{		
						bad.add("(par daudz NS)" + entry);
					}
					else
					{
						int noCount = StringUtils.findNumber(afterNs); // skaitlis p�c NS - nor�da NO skaitu
						Pattern noPat = Pattern.compile("\\sNO\\s");
						// p�rbuda vai skaitlis p�c  NS ir liel�k par 0
						if(noCount < 1)
						{
							bad.add("(NS j�b�t liek�kam par 0)" + entry);
						}
						Matcher no = noPat.matcher(entryInf);
						int allNo = 0;
						while (no.find()) // atrod visus NO ��irkl�
						{
							allNo++; 
						}
						no = noPat.matcher(afterNs); 
						int noAfterNs = 0;
						while (no.find()) // atrod visus NO p�c NS
						{
							noAfterNs++;
						}
						// Atsij� tos, kam nesakr�t NO skaiti.
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
							ng = ngPat.matcher(afterNs); // atrod visus NG p�c NS
							int ngAfterNs = 0;
							while (ng.find())
							{
								ngAfterNs++;
							}
							// Atsijaa tos, NG skaits ir liel�ks par NO skaitu
							if (noCount < allNG || noCount < ngAfterNs)
							{
								bad.add("(par daudz NG)" + entry);
							}
						}
					}
				}
		
				//P�rbauda vai NO s�kas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.nextCh(entryInf, "NO ")) 
						&& !Character.isDigit(StringUtils.nextCh(entryInf, "NO "))
						&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "NO ")))
				{
					bad.add("(NO nes�kas ar lielo burtu vai skaitli )" + entry);
				}
				
				//P�rbauda vai AN s�kas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.nextCh(entryInf, "AN ")) 
						&& !Character.isDigit(StringUtils.nextCh(entryInf, "AN "))
						&& !StringUtils.isBalticUpper(StringUtils.nextCh(entryInf, "AN ")))
				{
					bad.add("(AN nes�kas ar lielo burtu vai skaitli )" + entry);
				}
			}
			// metode kas p�rbauda likumsakra�bas ar IN0 un IN1
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
					for(int j = i+1; j<sk_len; j++) // cikls iet cauri ��irk�iem uz priek�u un 
													//p�rbauda vai nav v�l k�ds t�ds pats ��irklis
					{
						int sk_len_j = entries[j].length();
						if(sk_len_j > 2 && StringUtils.countSpaces(entries[j]) > 0)
						{	
							String EntryName2 = entries[j].substring(0, entries[j].indexOf(" "));
							if(EntryName2.equals(entryName)) // ja atrpd t�du pa�u �kirk�a v�rdu
							{
								good_1 = true;
								break;
							}
							if(brPoint > 4) // lai programma neietu cauri l�dz galam tiek nosprausts limits 4 ��irk�i uz priek�u
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
				//Paarbauda, vai jau neeksistee ��irklis ar taadu nosaukumu 
				if (index == 0)
				{	
					int sk_len = entries.length;
					boolean good_0 = true;
					int brPoint = 0;
					for(int j = i+1; j<sk_len; j++)// cikls iet cauri ��irk�iem uz priek�u un 
													//p�rbauda vai nav v�l k�ds t�ds pats ��irklis
					{
						int sk_len_j = entries[j].length();
						if(sk_len_j > 2 && StringUtils.countSpaces(entries[j]) > 0)
						{								
							String EntryName2 = entries[j].substring(0, entries[j].indexOf(" "));
							String EntryInf2 = entries[j].substring(entries[j].indexOf(" ")).trim();
							if(EntryName2.equals(entryName) && EntryInf2.matches("^IN\\s.*$")) // ja atrod t�du pa�u ��irkla v�rdu
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
						bad.add("(past�v v�l ��irk�i ar t�du v�rdu)" + entry);
					}
					if(entryInf.matches("^.*\\sCD\\s.*$") || entryInf.matches("^.*\\sDN\\s.*$")) // ja IN0 tad nevar b�t CD un DN
					{
						bad.add("(ja IN 0 nevar b�t CD | DN)" + entry);
					}
				}
			}
			// metode p�rbaudavai ir visi nepiecie�amie mar�ieri
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
				// p�rbauda vai CD un DN nav vienlaic�gi
				if (entryInf.matches("^..+\\sCD\\s.*$") && entryInf.matches("^..+\\sDN\\s.*$")) 
    			{
    				bad.add("(DN un CD nevar b�t vienlaic�gi)" + entry);
    			}
				// p�rbauda vai CD vai DN nav vienlaic�gi ar NS un NO
    			if (entryInf.matches("^..+\\sCD\\s.*$") || entryInf.matches("^..+\\sDN\\s.*$"))
    			{
    				if (entryInf.matches ("^.*\\s(NS|NO)\\s.*$"))
    				{
    					bad.add("(ja ir CD | DN nevar b�t NS|NO)" + entry);
    				}
    		
    			}
			}
			// metode p�rbauda vai ��irkl� nav ne pareizi simboli
			public static void langCharCheck(String entry, ArrayList<String> bad)
			{
				String entryName = entry.substring(0, entry.indexOf(" ")).trim();
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				//Parbauda vai skjirkla vaardaa nav nepaziistami simboli
    			if(!entryName.matches("^[\\w��������������������������\\./�'\\(\\)\\<\\>-]*$"))
    				bad.add("(��ik�a v�rds satur neparedz�tus simbolus)" + entry);
    			//Parbauda vai skjirkla info nesatur nepaziistami simboli
    			if(!entryInf.matches("^[\\w��������������������������\\p{Punct}\\p{Space}\\�\\�\"�~`�\\�]*$") 
    					&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				//sliktos simbolus aizvieto viegl�kai atra�anai
    				entry = entry.replaceAll("[^\\w��������������������������\\p{Punct}\\p{Space}\\�\\�\"�~`�\\�]", "?");
    				bad.add("(Skjirkla info satur neparedzeetus simbolus)" + entry);
    			}
    			//ja satur � p�rbauda vai tas ir l�bie�u vai latg�u v�rds
    			if((entry.contains("�") || entry.contains("�")) && !entryInf.matches("^.*\\s(latg|l�b)\\.\\s.*$")
    				&& !entryInf.matches("^.*\\sRU\\s\\[.*\\].*$") && !entryInf.matches("^.*\\sval.\\s.*$") 
    				&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				bad.add("(S�irklis satur � bet nav latg.|l�b.)" + entry);
    			}
    			//ja satur � p�rbauda vai tas ir l�bie�u vai latg�u v�rds
    			if((entry.contains("�") || entry.contains("�")) && !entryInf.matches("^.*\\s(latg|l�b)\\.\\s.*$") 
    			&& !entryInf.matches("^.*\\sRU\\s.*$") && !entryInf.matches("^.*\\sval\\.\\s.*$") 
    			&& !entryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				bad.add("(S�irklis satur � bet nav latg.|l�b.)" + entry);
    			}
			}
			//p�rbauda vai aiz IN DS NS FS ir skait�i
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
			// p�rbauda vai aiz CD eso�ais v�rds ir v�rdn�c�
			public static void wordAfterCd(String [] entries, String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				if (entryInf.matches ("^.*\\sCD\\s.*$"))
    			{
    				String cdWord = StringUtils.wordAfter(entryInf, "CD"); // atrod v�rdu aiz CD
    				if(!StringUtils.wordExist(entries, cdWord)) // p�rbauda vai ir iek�� vardn�c�
    				{
    					bad.add("(v�rds p�c CD nav atrodams)" + entry);
    				}
    			}
			}
			// p�rbauda vai aiz DN eso�ais v�rds ir v�rdn�c�
			public static void wordAfterDn(String [] entries, String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				if (entryInf.matches ("^.*\\sDN\\s.*$"))
    			{
    				String dnWord = StringUtils.wordAfter(entryInf, "DN"); // atrod v�rdu aiz DN
    				if(!StringUtils.wordExist(entries, dnWord)) // p�rbauda vai ir v�rdn�c�
    				{
    					bad.add("(v�rds p�c DN nav atrodams)" + entry);
    				}
    			}
			}
			//metode p�rbauda vai ir iev�rotas GR likumsakar�bas
			public static void grCheck(String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				if (entryInf.matches("^.*\\sGR\\s.*$")) // ja GR ir teksta vid�
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
    				// p�rbauda vai GR ir pirms NS un nav atrodams CD un DN
    				if(!AfterGR.matches("^.*\\sNS\\s.*$") && !AfterGR.matches("^.*\\s(CD|DN)\\s.*$"))
    				{
    					bad.add("(GR j�atrodas pirms NS)" + entry); 
    				}
    			}
				
				if (entryInf.matches("^GR\\s.*$")) // ja GR ir teksta s�kum�
    			{
    				Matcher cdDn = Pattern.compile("\\s(DN|CD)(?=\\s)").matcher(entryInf);
    				int cdDnCount = 0;
    				while (cdDn.find()) // mekl� CD un DN pa ��irkli
    				{
    					cdDnCount++;
    				}
    				if (cdDnCount == 0) // ja nav atrodams
    				{
    					bad.add("(nav indikatori CD | DN)" + entry);
    				}
    				if (cdDnCount > 1) // ja ir atrasti vair�k par vienu
    				{
    					bad.add("(var eksist�t tikai viens indikators CD | DN)" + entry);
    				}
    			}
			}
			//metode p�rbauda vai ir iev�rotas RU likumsakar�bas
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
    				// p�rbauda vai RU ir pirms NS
    				if(!AfterRU.matches("^.*\\sNS\\s.*$"))
    				{
    					bad.add("(RU j�atrodas pirms NS)" + entry);
    				}
    			}
			}
			// metode p�rbauda vai aiz @2 un @5 mar�ieri ir pareizi konstru�ti
			public static void atCheck(String entry, ArrayList<String> bad)
			{
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				// p�rbauda vai aiz @ seko 2 vai 5
				if(entryInf.matches("^.*@.\\s.*$") && StringUtils.nextCh(entryInf, "@") != '2' && StringUtils.nextCh(entryInf, "@") != '5')
    			{
					bad.add("(aiz @ seko nepareizs skaitlis)" + entry);
    			}
			}
			// metode p�rbauda vai ir ir pareiza gramatika sa�sin�jumiem un vietv�rdiem
			public static void grammarCheck(String entry, ArrayList<String> bad)
			{
				String entryName = entry.substring(0, entry.indexOf(" ")).trim();
				String entryInf = entry.substring(entry.indexOf(" ")).trim();
				
				//Ja ��irk�a v�rds beidzas ar punktu, tad vajadz�tu p�rbaud�t vai ir "GR @2 sa�s. @5".
    			if(entryName.charAt(entryName.length() - 1) == '.' 
    					&& !entryInf.matches("^.*\\sGR\\s@2.*\\ssa�s\\..*\\s@5\\s.*$"))
    			{
    				bad.add("(probl�ma ar sa�s.)" + entry);
    			}
    			//Ja v�rds ir vietniekv�rds tam j�s�k�s ar lielo burtu
    			if(entryInf.matches("^.*\\sGR\\s@2\\vietv\\.\\s@5\\s.*$") && !Character.isUpperCase(entryName.charAt(0)))
    			{
    				bad.add("(��irk�a v�rds nes�kas ar lielo burtu)" + entry);
    			}
			}
}
