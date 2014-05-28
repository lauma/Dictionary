package DictionaryTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EntryChecks
{
	public static boolean IsEntryNameGood(String Entry, ArrayList<String> bad)
	{
		boolean Good = true;
		String EntryName = Entry.substring(0, Entry.indexOf(" ")).trim();
		
		if(EntryName.equals(""))
		{
			bad.add("(Trûkst ðíirkïa vârds)" + Entry);
		}
		else
		{
			Good = false;
		}
		return Good;
	}
		
	public static int MaxIN(String [][] InEntries, String Word)
	{
		int Max = 0;
		int len = InEntries.length;
		for(int i=0; i< len; i++)
				if(InEntries[i][0] == Word && Integer.parseInt(InEntries[i][1]) > Max) 
					Max = Integer.parseInt(InEntries[i][1]);
						
		return Max;
	}
								
	public static void CheckBrackets(String Entries, ArrayList<String> bad)
	{
		int SqBrackets = 0;
		int CircBracket = 0;
		String EntryInf = Entries.substring(Entries.indexOf(" ")).trim();
		int len = EntryInf.length();
		for(int i = 0; i<len; i++)
		{
			if(EntryInf.charAt(i) == '[')
			{
				SqBrackets ++;
				if(EntryInf.charAt(i+1) == '"' && EntryInf.charAt(i-1) == '"')
				{
					SqBrackets --;
				}
			}
			if(EntryInf.charAt(i) == ']')
			{
				if(i < len-1)
				{
					SqBrackets --;
				}
				if(i < len-1)
				{
					if(EntryInf.charAt(i+1) == '"' && EntryInf.charAt(i-1) == '"')
					{
						SqBrackets ++;
					}
				}
				if(i == len-1)
				{
					SqBrackets--;
				}
			}
			
			if(EntryInf.charAt(i) == '(')
			{
				CircBracket ++;
				if(EntryInf.charAt(i+1) == '"' && EntryInf.charAt(i-1) == '"')
				{
					CircBracket --;
				}
			}
			if(EntryInf.charAt(i) == ')')
			{
				if(i < len-1)
				{
					CircBracket --;
				}
				if(i < len-1)
				{
					if(EntryInf.charAt(i+1) == '"' && EntryInf.charAt(i-1) == '"')
					{
						CircBracket ++;
					}
				}
				if(i == len-1)
				{
					CircBracket--;
				}
			}
		}
		
		if(SqBrackets != 0)
		{
			bad.add("(Problçma ar [] iekavâm)" + Entries);
		}
		if(CircBracket != 0)
		{
			bad.add("(Problçma ar () iekavâm)" + Entries);
		}
	}
							
	public static void WordByWordCheck(String Entries, ArrayList<String> bad)
	{
		String[] ident = {"NO","NS","PI","PN","FS","FR","FN","FP","DS","DE","DG","AN","DN","CD","LI"};
		String word = " ";
		String[] GramIdent = {"NG","AG","PG","FG"};
		int pn_p = 0;
		int no_p = 0;
		int no = 0;
		int pn = 0;
		int garums = 0;
		int ng = 0;
		int pi = 0;
		int at = 0;
		boolean GramOpen = false;
		boolean open = false;
		String EntryInf = Entries.substring(Entries.indexOf(" ")).trim();
		int len = EntryInf.length();
		int index = 0;
		int spaces = StringUtils.CountSpaces(EntryInf);
		
		while(len > 0 && spaces > 0)
		{
			if(StringUtils.CountSpaces(EntryInf) > 0)
			{
				word = EntryInf.substring(0, EntryInf.indexOf(" ")).trim();
				if(StringUtils.CountSpaces(EntryInf) == 0)
				{
					word = EntryInf.substring(0).trim();
				}
				if(word.length() > 0)
				{
					if(word.equals("PI"))
					{
						if(pi == 0)
						{
							pi = 1;
							pn = 0;
						}
						else
						{
							bad.add("(Divi PI pçc kârtas, bez PN)" + Entries);
						}
					}
					if(word.equals("PN")){
						if(pn == 0)
						{
							pn = 1;
							pn_p = 1;
							pi = 0;
						}
						else
						{
							bad.add("(Divi PN pçc kârtas, bez PI)" + Entries);
						}
					}
					if(word.equals("NO"))
					{
						no = 1;
						no_p = 1;
						ng = 0;
					}
					if(word.equals("NG"))
					{
						if(ng == 0 && no == 1)
						{
							ng = 1;
							no = 0;
						}
						else
						{
							bad.add("(Pirms NG nav atrodams NO)" + Entries);
							ng = 0;
						}
					}						
					if(word.contains("@2"))
					{
						open = true;
						if(StringUtils.CountSpaces(EntryInf) > 0)
						{
							if(StringUtils.WordAfter(EntryInf, word).contains("@5"))
							{
								bad.add("(Starp @2 un @5 jâbût tekstam)" + Entries);
							}
						}
						if(at == 0 || at == 5)
						{
							at = 2;
						}
						else
						{
							bad.add("(Divi @2 pçc kârtas)" + Entries);
						}
					}
					if(word.contains("@5"))
					{
						open = false;
						if(StringUtils.CountSpaces(EntryInf) > 0)
						{
							if(StringUtils.WordAfter(EntryInf, word).contains("@2") && !word.contains(")"))
							{
								bad.add("(Starp @5 un @2 jâbût tekstam)" + Entries);
							}
						}
						if(at == 0)
						{
							bad.add("(Pirms @5 jâbût @2)" + Entries);;
						}
						if(at == 5)
						{
							bad.add("(Divi @5 pçc kârtas)" + Entries);
						}
						if(at == 2)
						{
							at = 5;
						}
					}
					if(open)
					{
						if(Arrays.asList(ident).contains(word)
						|| Arrays.asList(GramIdent).contains(word))
						{
							bad.add("(@2 un @5 jâbût 1 maríiera robeþâs)" + Entries);
						}
					}
					//beigu pieturzîmes pârbaude
					garums = word.length();
					if(no_p == 1)
					{
						if(Arrays.asList(ident).contains(StringUtils.WordAfter(EntryInf, word))
						|| Arrays.asList(GramIdent).contains(StringUtils.WordAfter(EntryInf, word)))
						{
							if(word.charAt(garums -1) != '.' && word.charAt(garums -1) != '?' && word.charAt(garums -1) != '!')
							{
								bad.add("(NO nebeidzas ar pieturzîmi)" + Entries);
								no_p = 0;
							}
							else
							{
								no_p = 0;
							}
						}
					}
					if(pn_p == 1)
					{
						if(Arrays.asList(ident).contains(StringUtils.WordAfter(EntryInf, word))
								|| Arrays.asList(GramIdent).contains(StringUtils.WordAfter(EntryInf, word)))
						{
							if(word.charAt(garums -1) != '.' && word.charAt(garums -1) != '?'
							&& word.charAt(garums -1) != '!')
							{
								bad.add("(PN nebeidzas ar pieturzîmi)" + Entries);
								pn_p = 0;
							}
							else
							{
								pn_p = 0;
							}
						}
					}
					if(Arrays.asList(GramIdent).contains(word))
					{
						GramOpen = true;
						if(!StringUtils.WordAfter(EntryInf, word).contains("@2"))
						{
							bad.add("(Aiz gramatikas maríiera jâbût @2)" + Entries);
						}
					}
					if(GramOpen)
					{
						if(Arrays.asList(ident).contains(word))
						{
							bad.add("(Gramatikai jâbeidzâs ar @5)" + Entries);
							GramOpen = false;
						}
						if(word.contains("@5"))
						{
							GramOpen = false;
						}
					}
				}
				index = EntryInf.indexOf(word) + word.length() + 1;
				EntryInf = EntryInf.substring(index);
				len = EntryInf.length();
				spaces = StringUtils.CountSpaces(EntryInf);
			}
		}
		if(GramOpen)
		{
			bad.add("(Gramatikai jâbeidzâs ar @5)" + Entries);
		}
		if(open && !EntryInf.contains("@5"))
		{
			bad.add("(Ðíirkïa beigâs jâbût @5)" + Entries);
		}
	}
			
	public static void DsCheck(String Entry, ArrayList<String> bad)
	{
		String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
		
		if (EntryInf.matches("^.*\\sDS\\s.*$"))
		{
			Matcher ds = Pattern.compile("\\sDS(?=\\s)").matcher(EntryInf);
			ds.find();
			int dsVieta = ds.end();
			String PeecDs = EntryInf.substring(dsVieta).trim();
			// Atsijaa tos, kam par daudz DS
			if (PeecDs.matches("^.*\\sDS\\s.*$"))
			{		
				bad.add("(par daudz DS)" + Entry);
			}
			else
			{
				int deSkaits = StringUtils.FindNumber(PeecDs);
				Pattern dePat = Pattern.compile("\\sDE(?=\\s)");
				Matcher de = dePat.matcher(EntryInf);
				int	visiDe = 0;
				while(de.find())
				{
					visiDe++;
				}
				de = dePat.matcher(PeecDs);
				int dePeecDs = 0;
				while(de.find())
				{
					dePeecDs++;
				}
				// Atsijaa tos, kam nesakriit DE un DS skaiti.
				if(deSkaits != visiDe || deSkaits != dePeecDs)
				{
					bad.add("(nesakriit DE un DS skaiti)" + Entry);
				}
			}
		}
		//Pârbauda vai DE sâkas ar mazo burtu
		if(Character.isUpperCase(StringUtils.NextCh(EntryInf, "DE ")) 
				&& Character.isDigit(StringUtils.NextCh(EntryInf, "DE "))
				&& StringUtils.IsBalticUpper(StringUtils.NextCh(EntryInf, "DE ")))
		{
			bad.add("(DE nesâkas ar mazo burtu)" + Entry);
		}
	}
	
	public static void FsCheck(String Entry, ArrayList<String> bad)
	{
		String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
							
		if (EntryInf.matches("^.*\\sFS\\s.*$"))
		{
			Matcher fs = Pattern.compile("\\sFS(?=\\s)").matcher(EntryInf);
			fs.find();
			int fsVieta = fs.end();
			String PeecFs = EntryInf.substring(fsVieta).trim();
			
			// Atsijaa tos, kam par daudz FS
			if (PeecFs.matches("^.*\\sFS\\s.*$"))
			{		
				bad.add("(par daudz FS)" + Entry);
			}
			else
			{
				int frSkaits = StringUtils.FindNumber(PeecFs);
				int fnSkaits = frSkaits;
	
				Pattern frPat = Pattern.compile("\\sFR(?=\\s)");
				Matcher fr = frPat.matcher(EntryInf);
				int visiFr = 0;
				while(fr.find())
				{
					visiFr++;
				}
				fr = frPat.matcher(PeecFs);
				int frPeecFs = 0;
				while(fr.find())
				{
					frPeecFs++;
				}
				
				// Atsijaa tos, kam nesakriit FR skaiti.
				if(frSkaits != visiFr || frSkaits != frPeecFs)
				{
					bad.add("(nesakriit FR un FS skaiti)" + Entry);
				}
				else
				{
					Pattern fnPat = Pattern.compile("\\sFN(?=\\s)");
					Matcher fn = fnPat.matcher(EntryInf);
					int visiFn = 0;
					while(fn.find())
					{
						visiFn++;
					}
					fn = fnPat.matcher(PeecFs);
					int fnPeecFs = 0;
					while(fn.find())
					{
						fnPeecFs++;
					}
					
					// Atsijaa tos, kam nesakriit FR un FN skaiti.
					if(fnSkaits != visiFn || fnSkaits != fnPeecFs)
					{
						bad.add("(nesakriit FR un FN skaits)" + Entry);
					}
				}
			}
		}
		
		//Pârbauda vai FR sâkas ar lielo burtu
		if(Character.isLowerCase(StringUtils.NextCh(EntryInf, "FR ")) 
				&& !Character.isDigit(StringUtils.NextCh(EntryInf, "FR "))
				&& !StringUtils.IsBalticUpper(StringUtils.NextCh(EntryInf, "FR ")))
		{
			bad.add("(FR nesâkas ar lielo burtu vai skaitli )" + Entry);
		}	
	}
	public static void LiCheck(String Entry, ArrayList<String> bad, String [] Refer)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
									
				if (EntryInf.matches("^.*\\sLI\\s.*$"))
				{		
					Matcher li = Pattern.compile("\\sLI(?=\\s)").matcher(EntryInf);
					li.find();
					int liVieta = li.end();
					String PeecLI = EntryInf.substring(liVieta).trim();
			
					// Atsijaa tos, kam par daudz LI
					if (PeecLI.matches("^.*\\sLI\\s.*$"))
					{	
						bad.add("(par daudz LI)" + Entry);
					}
					else
					{
						int atsauc_sk = 1;
						int ierakst_sk = 0;
						if(PeecLI.contains("["))
						{
							int atsauc_s = PeecLI.indexOf( '[' ) + 1;
							int atsauc_b = PeecLI.indexOf( ']' );
							String atsauc = PeecLI.substring(atsauc_s, atsauc_b);
							int ats_len = atsauc.length();
							for(int m=0; m<ats_len; m++)
							{
								if(Character.isWhitespace(atsauc.charAt(m)))
								{
								atsauc_sk++;
								}
							}
							ierakst_sk = StringUtils.ReferCount(Refer, atsauc);
							if(atsauc_sk != ierakst_sk)
							{
								bad.add("(Problçma ar atsaucçm)" + Entry);
							}
						}
						else
						{
							bad.add("(Nav norâdîtas atsauces)" + Entry);
						}
					}
				}	
			}
			
			public static void PiCheck(String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
									
				if(EntryInf.matches("^.*\\sPI\\s.*$"))
				{
					Matcher pi = Pattern.compile("\\sPI(?=\\s)").matcher(EntryInf);
					int piSkaits = 0;
					while (pi.find())
					{
						piSkaits++;
					}
					Matcher pn = Pattern.compile("\\sPN(?=\\s)").matcher(EntryInf);
					int pnSkaits = 0;
					while (pn.find())
					{
						pnSkaits++;
					}
					// Atsijaa tos, kam nesakriit PI un PN skaits.
					if (piSkaits != pnSkaits)
					{
						bad.add("(nesakriit PI un PN skaits)" + Entry);
					}
				}
		
				//Pârbauda vai PI sâkas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.NextCh(EntryInf, "PI ")) 
						&& !Character.isDigit(StringUtils.NextCh(EntryInf, "PI "))
						&& !StringUtils.IsBalticUpper(StringUtils.NextCh(EntryInf, "PI ")))
				{
					bad.add("(PI nesâkas ar lielo burtu vai skaitli )" + Entry);
				}
			}
			
			public static void NsCheck(String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
									
				// Atsijaa tos, kam nav NS
				if (!EntryInf.matches("^.*\\sNS\\s.*$")  && !EntryInf.matches("^..+\\s(DN|CD)\\s.*$"))
				{
					bad.add("(nav NS)" + Entry);
				}
				else
				{
					Matcher ns = Pattern.compile("\\sNS\\s").matcher(EntryInf);
					ns.find();
					int nsVieta = ns.end();
					String peecNs = EntryInf.substring(nsVieta).trim();
					
					// Atsijaa tos, kam par daudz NS
					if (peecNs.matches("^.*\\sNS\\s.*$"))
					{		
						bad.add("(par daudz NS)" + Entry);
					}
					else
					{
						int noSkaits = StringUtils.FindNumber(peecNs);
						Pattern noPat = Pattern.compile("\\sNO\\s");
						Matcher no = noPat.matcher(EntryInf);
						int visiNo = 0;
						while (no.find())
						{
							visiNo++;
						}
						no = noPat.matcher(peecNs);
						int noPeecNs = 0;
						while (no.find())
						{
							noPeecNs++;
						}
						// Atsijaa tos, kam nesakriit NO skaiti.
						if(noSkaits != visiNo || noSkaits != noPeecNs)
						{
							bad.add("(nesakriit NO skaiti)" + Entry);
							if(StringUtils.FindNumber(peecNs) < 1)
							{
								bad.add("(NS jâbût liekâkam par 0)" + Entry);
							}
						}
						else
						{
							Pattern ngPat = Pattern.compile("\\sNG(?=\\s)");
							Matcher ng = ngPat.matcher(EntryInf);
							int visiNG = 0;
							while (ng.find())
							{
								visiNG++;
							}
							ng = ngPat.matcher(peecNs);
							int ngPeecNs = 0;
							while (ng.find())
							{
								ngPeecNs++;
							}
							// Atsijaa tos, kam nesakriit NG skaiti.
							if (noSkaits < visiNG || noSkaits < ngPeecNs)
							{
								bad.add("(par daudz NG)" + Entry);
							}
						}
					}
				}
		
				//Pârbauda vai NO sâkas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.NextCh(EntryInf, "NO ")) 
						&& !Character.isDigit(StringUtils.NextCh(EntryInf, "NO "))
						&& !StringUtils.IsBalticUpper(StringUtils.NextCh(EntryInf, "NO ")))
				{
					bad.add("(NO nesâkas ar lielo burtu vai skaitli )" + Entry);
				}
				
				//Pârbauda vai AN sâkas ar lielo burtu vai ciparu
				if(Character.isLowerCase(StringUtils.NextCh(EntryInf, "AN ")) 
						&& !Character.isDigit(StringUtils.NextCh(EntryInf, "AN "))
						&& !StringUtils.IsBalticUpper(StringUtils.NextCh(EntryInf, "AN ")))
				{
					bad.add("(AN nesâkas ar lielo burtu vai skaitli )" + Entry);
				}
			}
			
			public static void In0In1Check(String Entry, ArrayList<String> bad, 
									String [][] InEntries, String [] Entries, int i, int index)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				String EntryName = Entry.substring(0, Entry.indexOf(" ")).trim();
										
				//Atsijaa ar sliktajiem indeksiem.
				if(index <= MaxIN(InEntries, EntryName) && index != 0)
				{
					bad.add("(slikts indekss pie IN)" + Entry);
				}

				//Paarbauda, lai nebuutu vientulji Entries ar IN 1 
				if (index == 1)
				{
					boolean good_1 = false;
					int sk_len = Entries.length;
					int BrPoint = 0;
					for(int j = i+1; j<sk_len; j++)
					{
						int sk_len_j = Entries[j].length();
						if(sk_len_j > 2 && StringUtils.CountSpaces(Entries[j]) > 0)
						{	
							String EntryName2 = Entries[j].substring(0, Entries[j].indexOf(" "));
							if(EntryName2.equals(EntryName))
							{
								good_1 = true;
								break;
							}
							if(BrPoint > 4)
							{
								break;
							}
						}
						BrPoint++;
					}
					if(!good_1)
					{
						bad.add("(vientulsh IN 1)" + Entry);
					}	
				}
				//ja skjirklis ir ar IN 0
				//Paarbauda, vai jau neeksistee ðíirklis ar taadu nosaukumu 
				if (index == 0)
				{	
					int sk_len = Entries.length;
					boolean good_0 = true;
					int BrPoint = 0;
					for(int j = i+1; j<sk_len; j++)
					{
						int sk_len_j = Entries[j].length();
						if(sk_len_j > 2 && StringUtils.CountSpaces(Entries[j]) > 0)
						{								
							String EntryName2 = Entries[j].substring(0, Entries[j].indexOf(" "));
							String EntryInf2 = Entries[j].substring(Entries[j].indexOf(" ")).trim();
							if(EntryName2.equals(EntryName) && EntryInf2.matches("^IN\\s.*$"))
							{
								good_0 = false;
								break;
							}
							if(BrPoint > 4)
							{
								break;
							}
						}
						BrPoint++;
					}
					if(!good_0 || StringUtils.EntryExist(InEntries, EntryName))
					{
						bad.add("(pastâv vçl ðíirkïi ar tâdu vârdu)" + Entry);
					}
					if(EntryInf.matches("^.*\\sCD\\s.*$") || EntryInf.matches("^.*\\sDN\\s.*$"))
					{
						bad.add("(ja IN 0 nevar bût CD | DN)" + Entry);
					}
				}
			}
			
			public static void IdentCheck(String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				if(!EntryInf.matches("^.*CD\\s.*$") && !EntryInf.matches("^.*DN\\s.*$"))
    			{
    				if(!EntryInf.matches("^IN\\s.*$"))
    				{
    					bad.add("(Nav IN indikatora)" + Entry);
    				}
    				if(!EntryInf.matches("^..+\\sNS\\s.*$"))
    				{
    					bad.add("(Nav NS indikatora)" + Entry);
    				}
    				if(!EntryInf.matches("^..+\\sFS\\s.*$"))
    				{
    					bad.add("(Nav FS indikatora)" + Entry);
    				}
    				if(!EntryInf.matches("^..+\\sDS\\s.*$"))
    				{
    					bad.add("(Nav DS indikatora)" + Entry);
    				}
    				if(!EntryInf.matches("^..+\\sNO\\s.*$"))
    				{
    					bad.add("(Nav neviena NO)" + Entry);
    				}
    			}
				if (EntryInf.matches("^..+\\sCD\\s.*$") && EntryInf.matches("^..+\\sDN\\s.*$"))
    			{
    				bad.add("(DN un CD nevar bût vienlaicîgi)" + Entry);
    			}
    			if (EntryInf.matches("^..+\\sCD\\s.*$") || EntryInf.matches("^..+\\sDN\\s.*$"))
    			{
    				if (EntryInf.matches ("^.*\\s(NS|NO)\\s.*$"))
    				{
    					bad.add("(ja ir CD | DN nevar bût NS|NO)" + Entry);
    				}
    		
    			}
			}
			
			public static void LangCharCheck(String Entry, ArrayList<String> bad)
			{
				String EntryName = Entry.substring(0, Entry.indexOf(" ")).trim();
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				//Parbauda vai skjirkla vaardaa nav nepaziistami simboli
    			if(!EntryName.matches("^[\\wÂâÈèÇçÌìÎîÍíÏïÒòÐðÛûÞþÔôªº\\./’'\\(\\)\\<\\>-]*$"))
    				bad.add("(Ðíikïa vârds satur neparedzçtus simbolus)" + Entry);
    			//Parbauda vai skjirkla info nesatur nepaziistami simboli
    			if(!EntryInf.matches("^[\\wÂâÈèÇçÌìÎîÍíÏïÒòÐðÛûÞþÔôªº\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]*$") 
    					&& !EntryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				Entry = Entry.replaceAll("[^\\wÂâÈèÇçÌìÎîÍíÏïÒòÐðÛûÞþÔôªº\\p{Punct}\\p{Space}\\’\\—\"„~`‘\\–]", "?");
    				bad.add("(Skjirkla info satur neparedzeetus simbolus)" + Entry);
    			}
    			//ja satur Ô pârbauda vai tas ir lîbieðu vai latgïu vârds
    			if((Entry.contains("ô") || Entry.contains("Ô")) && !EntryInf.matches("^.*\\s(latg|lîb)\\.\\s.*$")
    				&& !EntryInf.matches("^.*\\sRU\\s\\[.*\\].*$") && !EntryInf.matches("^.*\\sval.\\s.*$") 
    				&& !EntryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				bad.add("(Síirklis satur ô bet nav latg.|lîb.)" + Entry);
    			}
    			//ja satur ª pârbauda vai tas ir lîbieðu vai latgïu vârds
    			if((Entry.contains("º") || Entry.contains("ª")) && !EntryInf.matches("^.*\\s(latg|lîb)\\.\\s.*$") 
    			&& !EntryInf.matches("^.*\\sRU\\s.*$") && !EntryInf.matches("^.*\\sval\\.\\s.*$") 
    			&& !EntryInf.matches("^.*\\sRU\\s[.*]\\s.*$"))
    			{
    				bad.add("(Síirklis satur º bet nav latg.|lîb.)" + Entry);
    			}
			}
			
			public static void InDsNsFsNumberCheck(String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				if(!EntryInf.matches("^IN\\s\\d*\\s.*$") && EntryInf.matches("^IN\\s.*$"))
    			{
    				bad.add("(Aiz IN neseko skaitlis)" + Entry);
    			}
    			if(!EntryInf.matches("^..+\\sNS\\s\\d*\\s.*$") && EntryInf.matches("^..+\\sNS\\s.*$"))
    			{
    				bad.add("(Aiz NS neseko skaitlis)" + Entry);
    			}
    			if(!EntryInf.matches("^..+\\sDS\\s\\d*.*$") && EntryInf.matches("^..+\\sDS\\s.*$"))
    			{
    				bad.add("(Aiz DS neseko skaitlis)" + Entry);
    			}
    			if(!EntryInf.matches("^..+\\sFS\\s\\d*\\s.*$") && EntryInf.matches("^..+\\sFS\\s.*$"))
    			{
    				bad.add("(Aiz FS neseko skaitlis)" + Entry);
    			}
				
			}
			
			public static void WordAfterCd(String [] Entries, String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				if (EntryInf.matches ("^.*\\sCD\\s.*$"))
    			{
    				String CD_word = StringUtils.WordAfter(EntryInf, "CD");
    				if(!StringUtils.WordExist(Entries, CD_word))
    				{
    					bad.add("(vârds pçc CD nav atrodams)" + Entry);
    				}
    			}
			}
			
			public static void WordAfterDn(String [] Entries, String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				if (EntryInf.matches ("^.*\\sDN\\s.*$"))
    			{
    				String DN_word = StringUtils.WordAfter(EntryInf, "DN");
    				if(!StringUtils.WordExist(Entries, DN_word))
    				{
    					bad.add("(vârds pçc DN nav atrodams)" + Entry);
    				}
    			}
			}
			
			public static void GrCheck(String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				if (EntryInf.matches("^.*\\sGR\\s.*$"))
    			{
    				Matcher gr = Pattern.compile("\\sGR(?=\\s)").matcher(EntryInf);
    				gr.find();
    				int grVieta = gr.end();
    				String PeecGR = EntryInf.substring(grVieta).trim();
    				// Atsijaa tos, kam par daudz GR
    				if (PeecGR.matches("^.*\\sGR\\s.*$"))	
    				{
    					bad.add("(par daudz GR)" + Entry);
    				}
    				if(!PeecGR.matches("^.*\\sNS\\s.*$") && !PeecGR.matches("^.*\\s(CD|DN)\\s.*$"))
    				{
    					bad.add("(GR jâatrodas pirms NS)" + Entry);
    				}
    			}
				
				if (EntryInf.matches("^GR\\s.*$"))
    			{
    				Matcher cdDn = Pattern.compile("\\s(DN|CD)(?=\\s)").matcher(EntryInf);
    				int cdDnSkaits = 0;
    				while (cdDn.find())
    				{
    					cdDnSkaits++;
    				}
    				if (cdDnSkaits == 0)
    				{
    					bad.add("(nav indikatori CD | DN)" + Entry);
    				}
    				if (cdDnSkaits > 1)
    				{
    					bad.add("(var eksistçt tikai viens indikators CD | DN)" + Entry);
    				}
    			}
			}
			
			public static void RuCheck(String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				if(EntryInf.matches("^.*\\sRU\\s.*$"))
    			{
    				Matcher ru = Pattern.compile("\\sRU(?=\\s)").matcher(EntryInf);
    				ru.find();
    				int RuVieta = ru.end();
    				String PeecRU = EntryInf.substring(RuVieta).trim();
    				// Atsijaa tos, kam par daudz RU
    				if (PeecRU.matches("^.*\\sRU\\s.*$"))	
    				{
    					bad.add("(par daudz RU)" + Entry);
    				}
    				if(!PeecRU.matches("^.*\\sNS\\s.*$"))
    				{
    					bad.add("(RU jâatrodas pirms NS)" + Entry);
    				}
    			}
			}
			
			public static void AtCheck(String Entry, ArrayList<String> bad)
			{
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				if(EntryInf.matches("^.*@.\\s.*$") && StringUtils.NextCh(EntryInf, "@") != '2' && StringUtils.NextCh(EntryInf, "@") != '5')
    			{
					bad.add("(aiz @ seko nepareizs skaitlis)" + Entry);
    			}
			}
			
			public static void GrammarCheck(String Entry, ArrayList<String> bad)
			{
				String EntryName = Entry.substring(0, Entry.indexOf(" ")).trim();
				String EntryInf = Entry.substring(Entry.indexOf(" ")).trim();
				
				//Ja ðíirkïa vârds beidzas ar punktu, tad vajadzçtu pârbaudît vai ir "GR @2 saîs. @5".
    			if(EntryName.charAt(EntryName.length() - 1) == '.' 
    					&& !EntryInf.matches("^.*\\sGR\\s@2.*\\ssaîs\\..*\\s@5\\s.*$"))
    			{
    				bad.add("(problçma ar saîs.)" + Entry);
    			}
    			//Ja vârds ir vietniekvârds tam jâsâkâs ar lielo burtu
    			if(EntryInf.matches("^.*\\sGR\\s@2\\vietv\\.\\s@5\\s.*$") && !Character.isUpperCase(EntryName.charAt(0)))
    			{
    				bad.add("(Ðíirkïa vârds nesâkas ar lielo burtu)" + Entry);
    			}
			}
}
