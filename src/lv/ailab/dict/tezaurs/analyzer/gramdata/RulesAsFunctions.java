package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.tezaurs.analyzer.struct.THeader;
import lv.ailab.dict.tezaurs.analyzer.struct.TLemma;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Te būs likumi, ko neizdodas izlikt smukajos, parametrizējamajos EndingRule
 * objektos, bet tā vietā tie palikuši kā atsevišķas funkcijas.
 * Lai karodziņu vērtības nebūtu izkaisītas pa visurieni, šajā klasē tiek
 * lietotas tikai vērtības, kas ieviestas TValues uzskaitījumā.
 *
 * Izveidots 2015-10-22.
 * @author Lauma
 */
public class RulesAsFunctions
{
	/**
	 * Izanalizē doto formu, nosaka kas tas ir par divdabi.
	 * @param form	analizējamā forma
	 * @return 	TValue divdabja tips vai null
	 */
	public static String determineParticipleType(String form)
	{
		if (form.endsWith("damies") || form.endsWith("dams")) //aizvilkties->aizvilkdamies
			return TValues.PARTICIPLE_DAMS;
		else if (form.endsWith("ams") || form.endsWith("āms"))
			return TValues.PARTICIPLE_AMS;
		else if (form.endsWith("ošs")) // garāmbraucošs
			return TValues.PARTICIPLE_OSS;
		else if (form.endsWith("ts")) // aizdzert->aizdzerts
			return TValues.PARTICIPLE_TS;
		else if (form.endsWith("is") || form.endsWith("ies")) // aizmakt->aizsmacis, pieriesties->pieriesies
			return TValues.PARTICIPLE_IS;
		else if (form.endsWith("ot") || form.endsWith("oties")) // ievērojot
			return TValues.PARTICIPLE_OT;
		else return null;
	}

	/**
	 * Pārbauda, vai gramatikas teksts (pieņemot, ka tā ir verba gramatika)
	 * satur tikai norādes par formām vai arī vēl papildus info par to, kuras no
	 * dotajām formām ir biežākas/retākas,
	 * @param gramText	analizējamais gramatikas teksts
	 * @return	true, ja gramatikas teksts satur tikai formas; false, ja
	 * 			gramatikas teksts satur norādes par to, kuri celmi lietojami
	 * 			retāk un kuri - biežāk.
	 */
	public static boolean containsFormsOnly(String gramText)
	{
		return !gramText.matches(".*(, | \\()retāk .*");
	}

	/**
	 * Pārbauda, vai gramatikas teksts (pieņemot, ka tā ir verba gramatika)
	 * satur 1. konjugācijas paralēlformas.
	 * @param gramText	analizējamais gramatikas teksts
	 * @return	true, ja gramatikas teksts satur "1. konj.".
	 */
	public static boolean containsFirstConj(String gramText)
	{
		return gramText.matches(".*\\b1\\.\\s*konj\\..*");
	}

	public static boolean matchEtymologyFlag(String gramFragment, Flags flagCollector)
	{
		// TODO: afrikandu?
		if (gramFragment.matches(
				"(afrikandu|angļu|arābu|fr\\.|it\\.|lat\\.|latīņu|sanskr\\.|senebr\\.|spāņu|vācu|zviedru)( val\\.)? \"[- \\p{L}]+\""
						+ "( - deminutīvs no \"[ \\p{Ll}]+\")?\\.?"))
		{
			flagCollector.add(Tuple.of(TKeys.ETYMOLOGY, gramFragment));
			return true;
		}
		return false;
	}

	/**
	 * Izanalizē gramatikas virknes formā:
	 * parasti savienojumā "X, Y".
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText		analizējamais gramatikas teksta fragments
	 * @param flagCollector kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                      gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processInPhraseFlag(String gramText, Flags flagCollector)
	{
		boolean hasComma = gramText.contains(",");
		Pattern flagPattern = hasComma ?
				Pattern.compile("((parasti |bieži |)(?:savienojumā|atkārtojumā) [\"'](\\p{L}+((, | - |-| )\\p{L}+)*)[\"'])([.,].*)?") :
				Pattern.compile("((parasti |bieži |)(?:savienojumā|atkārtojumā) [\"'](\\p{L}+(( - |-| )\\p{L}+)*)[\"'])([.].*)?");

		int newBegin = -1;
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches()) // aijā - savienojumā "aijā, žūžū"
		{
			newBegin = m.group(1).length();
			String indicator = m.group(2).trim();
			String usedType = TKeys.USED_IN_STRUCT;
			if (indicator.equals("parasti"))
				usedType = TKeys.USUALLY_USED_IN_STRUCT;
			else if (indicator.equals("bieži"))
				usedType = TKeys.OFTEN_USED_IN_STRUCT;
			String phrase = m.group(3);
			flagCollector.add(usedType, TValues.PHRASE);
			flagCollector.add(usedType, "\"" + phrase + "\"");
		}
		return newBegin;
	}

	/**
	 * Izanalizē gramatikas virknes formā:
	 * savienojumā ar "..."
	 * savienojumā ar "...", "..." u. tml.
	 * Piemēri:
	 * aizbļaut - savienojumā ar "ausis"
	 * aizliegt - savienojumā ar "zona", "josla", "teritorija", "ūdeņi" u. tml.
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText		analizējamais gramatikas teksta fragments
	 * @param flagCollector kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                      gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processTogetherWithQuotFlag(
			String gramText, Flags flagCollector)
	{
		//boolean hasComma = gramText.contains(",");
		Pattern flagPattern = Pattern.compile("((parasti |)savienojumā ar (\"\\p{L}+\"(, \"\\p{L}+\")*( u\\. tml\\.)?)\\.?)([,;].*)?");
		int newBegin = -1;
		Matcher	m = flagPattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			String modifier = m.group(2).trim();
			String key = TKeys.USED_TOGETHER_WITH;
			if (modifier.equals("parasti")) key = TKeys.USUALLY_USED_TOGETHER_WITH;
			String phrase = m.group(3);
			if (phrase.endsWith(" u. tml."))
			{
				phrase = phrase.substring(0, phrase.length() - " u. tml.".length());
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
			}
			String[] words = phrase.split("(?<=\"), (?=\")");
			for (String w : words)
				flagCollector.add(key, w.trim());
		}
		return newBegin;
	}

	/**
	 * Izanalizē gramatikas virknes formā:
	 * savienojumā ar ...
	 * parasti savienojumā ar ...
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText		analizējamais gramatikas teksta fragments
	 * @param flagCollector kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                      gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processTogetherWithGenFlag(
			String gramText, Flags flagCollector)
	{
		boolean hasComma = gramText.contains(",");
		Pattern flagPattern = hasComma ?
				//Pattern.compile("(savienojumā ar (\\p{L}+\\.?( \\p{L}+\\.?)*, arī( \\p{L}+\\.?)+))([,].*)?") :
				Pattern.compile("((parasti |)savienojumā ar (\\p{L}+\\.?(,? \\p{L}+\\.?)*?))(, adj. nozīmē\\.?)?") :
				Pattern.compile("((parasti |)savienojumā ar (\\p{L}+\\.?( \\p{L}+\\.?)*))");

		int newBegin = -1;
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			String modifier = m.group(2).trim();
			String key = TKeys.USED_TOGETHER_WITH;
			if (modifier.equals("parasti")) key = TKeys.USUALLY_USED_TOGETHER_WITH;
			String flagValueRaw = m.group(3);
			ArrayList<String> flagValues = new ArrayList<>();
			String deabbrCase = AbbrMap.getAbbrMap().translateCase(flagValueRaw);
			Set<String> deabbrPos = AbbrMap.getAbbrMap().translatePos(flagValueRaw);
			if (deabbrCase != null) flagValues.add(deabbrCase);
			else if (deabbrPos != null) flagValues.addAll(deabbrPos);
			else if (flagValueRaw.matches(
					"slimības izraisītāja mikroorganisma, arī slimības nosaukumu\\.?"))
				flagValues.add(TValues.ILLNESS_NAME);
			else if (flagValueRaw.matches(
					"laika mērvienības nosaukumu\\.?"))
				flagValues.add(TValues.TIME_UNIT_NAME);
			else if (flagValueRaw.matches(
					"apģērba gabala nosaukumu\\.?"))
				flagValues.add(TValues.CLOTHING_UNIT_NAME);
			else if (flagValueRaw.matches(
					"mācību priekšmeta nosaukumu\\.?"))
				flagValues.add(TValues.TEACHING_SUBJECT_NAME);
			else if (flagValueRaw.matches(
					"dimensiju apzīmējumiem\\.?"))
				flagValues.add(TValues.DIMENSION_NAME);
			else if (flagValueRaw.matches(
					"daudzskaitliniekiem vai pāra priekšmetu apzīmējumiem\\.?"))
				flagValues.add(TValues.MULTI_TINGY_NAME);
			else if (flagValueRaw.matches("personvārdu\\.?")) flagValues.add(TValues.PERSON_NAME);
			else if (flagValueRaw.matches("uzvārdu vīriešu vai sieviešu dzimtē vai savienojumā ar vārdu\\.?"))
				// TODO: Laurai pārbaudīt
				flagValues.add(TValues.PERSON_NAME);
			else if (flagValueRaw.matches("daudzuma, masas, lieluma, ilguma apzīmējumu\\.?"))
				flagValues.add(TValues.ADVERBIAL_TERM);

			else if (flagValueRaw.matches("verbu\\.?")) flagValues.add(TValues.VERB);
			else if (flagValueRaw.matches("bezpriedēkļa verbu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.PREFIXLESS_VERB);
			}
			else if (flagValueRaw.matches("priedēkļa verbu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.PREFIX_VERB);
			}
			else if (flagValueRaw.matches("(nenoteiksmi|verbu (nenoteiksmes|infinitīva) formā|verba nenoteiksmi|verbu nenoteiksmē)\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.INFINITIVE);
			}
			else if (flagValueRaw.matches("verba personas formu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.PERSON_FORM);
			}
			else if (flagValueRaw.matches("noliegtu verbu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.NEGATIVE_VERB);
				flagValues.add(TValues.NEGATIVE);
			}
			else if (flagValueRaw.matches("verba ciešamās kārtas formu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.PASSIVE_VOICE);
			}
			else if (flagValueRaw.matches("patstāvīgas nozīmes verbu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.MAIN_VERB);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes verbu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.REPETITION);
			}
			else if (flagValueRaw.matches("verbu, kas apzīmē bojāšanu, iznīcināšanu\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.DISTRUCTION_NAME);
			}
			else if (flagValueRaw.matches("(verbu, parasti divd\\. formā|divd\\., retāk ar citām verba formām)\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.ORIGINAL_NEEDED);
			}
			else if (flagValueRaw.matches("lietvārdu\\.?")) flagValues.add(TValues.NOUN);
			else if (flagValueRaw.matches("(atkārtotu )?vienas un tās pašas saknes (lietvārdu\\.?|lietv\\.)"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.REPETITION);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes lietv\\. ar laika nozīmi\\.?"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.TIME_TERM);
				flagValues.add(TValues.REPETITION);
			}
			else if (flagValueRaw.matches("lietv\\. (lokatīvā\\.?|lok\\.)"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.LOCATIVE);
			}
			else if (flagValueRaw.matches("lietv\\. ģen\\.|lietvārdu ģenitīvā\\.?"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.GENITIVE);
			}
			else if (flagValueRaw.matches("lietv\\. dsk\\. ģen\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.GENITIVE);
				flagValues.add(TValues.PLURAL);
				flagValues.add(TValues.PLURAL_GENITIVE);
			}
			else if (flagValueRaw.matches("lietv\\. akuz\\. vai lok\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.ACUSATIVE);
				flagValues.add(TValues.LOCATIVE);
			}
			else if (flagValueRaw.matches("lietvārdu, kas nosauc personu ar negatīvu īpašību\\.?"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.PERSON_TERM);
				flagValues.add(TValues.NEGATIVE_PERSON_TERM);
			}
			else if (flagValueRaw.matches("vienas un tās pašas saknes lietv\\. vai lietv. nozīmē lietotu vārdu\\.?"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.NOUN_CONTAMINATION);
				flagValues.add(TValues.REPETITION);
			}
			else if (flagValueRaw.matches("pamata skaitļa vārdu\\.?"))
			{
				flagValues.add(TValues.NUMERAL);
				flagValues.add(TValues.CARDINAL_NUMERAL);
			}
			else if (flagValueRaw.matches("kārtas numerāli\\.?"))
			{
				flagValues.add(TValues.NUMERAL);
				flagValues.add(TValues.ORDINAL_NUMERAL);
			}
			else if (flagValueRaw.matches("vietniekvārdiem\\.?")) flagValues.add(TValues.PRONOUN);
			else if (flagValueRaw.matches("adj\\. pārāko pakāpi\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.COMPARATIVE_DEGREE);
			}
			else if (flagValueRaw.matches("adj\\., kam ir not\\. galotne\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.DEFINITE_ENDING);
			}
			else if (flagValueRaw.matches("ciešamās kārtas pagātnes divdabi\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.PARTICIPLE);
				flagValues.add(TValues.PARTICIPLE_TS);
			}
			else if (flagValueRaw.matches("citām līdzīgām interjekcijām\\.?"))
			{
				flagValues.add(TValues.INTERJECTION);
				flagValues.add(TValues.ORIGINAL_NEEDED);
			}

			else if (flagValueRaw.matches("adj\\. vai apst\\.|apst\\. vai adj\\.|adjektīvu vai adverbu\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.ADVERB);
			}
			else if (flagValueRaw.matches("adj\\. vai apst\\. pārāk(ajā|o) pakāp(ē|i)\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.COMPARATIVE_DEGREE);
			}
			else if (flagValueRaw.matches("adj\\. vai divd\\."))
			{
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.PARTICIPLE);
				flagValues.add(TValues.VERB);
			}
			else if (flagValueRaw.matches("adj\\., norād\\., nenot\\. vai vispārin\\. vietn\\."))
			{
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.PRONOUN);
				flagValues.add(TValues.DEMONSTRATIVE_PRONOUN);
				flagValues.add(TValues.INDEFINITE_PRONOUN);
			}
			else if (flagValueRaw.matches("lietv\\. vai vietn\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.PRONOUN);
			}
			else if (flagValueRaw.matches("pers\\. vietn\\. vai lietv."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.PRONOUN);
				flagValues.add(TValues.PERSONAL_PRONOUN);
			}
			else if (flagValueRaw.matches("skait\\. vai nenot\\. vietn\\."))
			{
				flagValues.add(TValues.NUMERAL);
				flagValues.add(TValues.PRONOUN);
				flagValues.add(TValues.INDEFINITE_PRONOUN);
			}
			else if (flagValueRaw.matches("subst\\. vai pron\\. dat\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.PRONOUN);
				flagValues.add(TValues.DATIVE);
			}
			else if (flagValueRaw.matches("pers. vietn\\. vai lietv., kas apzīmē personu\\.?"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.PRONOUN);
				flagValues.add(TValues.PERSONAL_PRONOUN);
				flagValues.add(TValues.PERSON_TERM);
			}
			else if (flagValueRaw.matches("lietv\\., retāk ar apst\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.ORIGINAL_NEEDED);
				// TODO: specifisko karodziņu vajag?
			}
			else if (flagValueRaw.matches("lietv\\., retāk ar apst\\., verbu\\.?"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.ORIGINAL_NEEDED);
			}
			else if (flagValueRaw.matches("(vietniekvārdiem, retāk apstākļa vārdiem|dažiem apstākļa vārdiem (un|vai) vietniekvārdiem)\\.?"))
			{
				flagValues.add(TValues.PRONOUN);
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.ORIGINAL_NEEDED);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes lietv\\. vai adj\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.REPETITION);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes adj\\. vai apst\\."))
			{
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.REPETITION);
			}
			else if (flagValueRaw.matches("not\\. galotnes adj\\. lietv\\. nozīmē vai savienojumā ar adj\\. vispārāko pakāpi\\."))
			{
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.COMPARATIVE_DEGREE);
				flagValues.add(TValues.SUPERLATIVE_DEGREE);
				flagValues.add(TValues.NOUN_CONTAMINATION_ADJ_COMP);
			}

			else if (flagValueRaw.matches("apzīmētāju\\.?")) flagValues.add(TValues.ATTRIBUTE);
			else if (flagValueRaw.matches("apst\\. vai tā nozīmē lietotu vārdu\\.?"))
			{
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.ADVERB_CONTAMINATION);
				// TODO
			}
			else if (flagValueRaw.matches("vietas adv\\. vai tā nozīmē lietotu vārdu\\.?"))
			{
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.PLACE_ADVERB);
				flagValues.add(TValues.PLACE_ADVERB_CONTAMINATION);
				// TODO
			}
			else if (flagValueRaw.matches("īpašvārdu\\.?")) flagValues.add(TValues.PROPER);
			else if (flagValueRaw.matches("dat\\. un apst\\. vai adj\\."))
			{
				flagValues.add(TValues.DATIVE);
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.ADJECTIVE);
				flagValues.add(TValues.DATIVE_AND_ADVERB); // man palika žēl
															// palika silts
			}
			else if (flagValueRaw.matches("lok\\. vai nenoteiksmi\\.?"))
			{
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.INFINITIVE);
				flagValues.add(TValues.LOCATIVE);
			}
			else if (flagValueRaw.matches("vietas nosaukumu lok\\."))
			{
				flagValues.add(TValues.PLACE_NAME);
				flagValues.add(TValues.LOCATIVE);
			}
			else if (flagValueRaw.matches("divsk\\. nom\\."))
			{
				flagValues.add(TValues.DUAL);
				flagValues.add(TValues.NOMINATIVE);
				flagValues.add(TValues.DUAL_NOMINATIVE);
			}
			else if (flagValueRaw.matches("divsk\\. nom\\., akuz\\."))
			{
				flagValues.add(TValues.DUAL);
				flagValues.add(TValues.ACUSATIVE);
				flagValues.add(TValues.NOMINATIVE);
				flagValues.add(TValues.DUAL_ACUSATIVE);
				flagValues.add(TValues.DUAL_NOMINATIVE);
			}
			else if (flagValueRaw.matches("apst\\. un noliegtu verbu\\.?"))
			{
				flagValues.add(TValues.ADVERB);
				flagValues.add(TValues.VERB);
				flagValues.add(TValues.NEGATIVE_VERB);
				flagValues.add(TValues.NEGATIVE);
				flagValues.add(TValues.ADVERB_AND_NEGVERB);
			}
			else if (flagValueRaw.matches("lietv\\., skait\\. vai skait\\. un lietv\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.NUMERAL);
				flagValues.add(TValues.ORIGINAL_NEEDED);
			}
			else if (flagValueRaw.matches("lietv\\. lok\\. vai lietv\\. un priev\\."))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.LOCATIVE);
				flagValues.add(TValues.ADPOSITION);
				flagValues.add(TValues.ORIGINAL_NEEDED);
			}
			else if (flagValueRaw.matches("skait\\. vai skait\\. un lietv\\. dat\\.(, ģen\\., nom\\.(, akuz\\.)?)?"))
			{
				flagValues.add(TValues.NOUN);
				flagValues.add(TValues.NUMERAL);
				flagValues.add(TValues.ORIGINAL_NEEDED);
			}

			// TODO: salabot, ka pa tiešo liek pārīšus iekšā flags?
			if (flagValues.size() > 0) for (String fv: flagValues)
				if (fv.equals(TValues.ORIGINAL_NEEDED))
					flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				else flagCollector.add(key, fv);
			else
			{
				System.err.printf("Neizdevās atšifrēt karodziņu \"%s\"\n", m
						.group(1));
				newBegin = 0;
			}
		}
		return newBegin;
	}

	/**
	 * Izanalizē gramatikas virknes formā:
	 * savienojumā ar ... " ... " ...
	 * parasti savienojumā ar ... " ... " ...
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText		analizējamais gramatikas teksta fragments
	 * @param flagCollector kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                      gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processTogetherWithGenQuotFlag(
			String gramText, Flags flagCollector)
	{
		boolean hasComma = gramText.contains(",");
		Pattern flagPattern = hasComma ?
				Pattern.compile("((parasti |)savienojumā ar ([\\p{L}()]+\\.?(,? \"?[\\p{L}()]+\\.?\"?)*\\.?))") :
				Pattern.compile("((parasti |)savienojumā ar ([\\p{L}()]+\\.?( \"?[\\p{L}()]+\\.?\"?)*\\.?))");

		int newBegin = -1;
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			String modifier = m.group(2).trim();
			String key = TKeys.USED_TOGETHER_WITH;
			if (modifier.equals("parasti")) key = TKeys.USUALLY_USED_TOGETHER_WITH;
			String flagValueRaw = m.group(3);
			//ArrayList<String> flagValues = new ArrayList<>();

			Matcher specificVerb1 = Pattern.compile("verb[ua] (\"\\p{L}+(\\(ies\\))?\"(, (retāk )?\"\\p{L}+(\\(ies\\))?\")*)( formām)?\\.?").matcher(flagValueRaw);
			Matcher specificVerb2 = Pattern.compile("verbiem (\"\\p{L}+\"(, \"\\p{L}+\")*) u\\. tml\\.").matcher(flagValueRaw);
			Matcher specificVerbDeriv = Pattern.compile("verbu (\"\\p{L}+\") (un|vai) atvasinājumiem no tā\\.?").matcher(flagValueRaw);
			Matcher specificVerbNeg = Pattern.compile("verba (\"\\p{L}+\") noliegt(aj)?ām formām\\.?").matcher(flagValueRaw);
			Matcher specificOne = Pattern.compile("(prievārd(?:u|iem)|apst\\.|lietvārdu|jaut\\. part\\.) (\"\\p{L}+\"(, \"\\p{L}+\")*)\\.?").matcher(flagValueRaw);
			Matcher specificTwo = Pattern.compile("(lietv\\. un priev\\.|adj\\., retāk vietn\\.|adj\\., kam ir not\\. galotne, vai skait\\.) (\"\\p{L}+\")\\.?").matcher(flagValueRaw);
			if (specificVerb1.matches())
			{
				String verbStr = specificVerb1.group(1);
				if (verbStr.contains(", retāk "))
				{
					flagCollector.add(TFeatures.ORIGINAL_NEEDED);
					verbStr = verbStr.replace(", retāk ", ", ");
				}
				String[] verbs = verbStr.split(",");

				flagCollector.add(key, TValues.VERB);
				for (String v : verbs)
				if (v.endsWith("(ies)\""))
				{
					flagCollector.add(key, v.replace("(ies)", ""));
					flagCollector.add(key, v.replace("(", "").replace(")", ""));
				}
				else flagCollector.add(key, v.trim());
			}
			else if (specificVerb2.matches())
			{
				String[] verbs = specificVerb2.group(1).split(",");

				flagCollector.add(key, TValues.VERB);
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				for (String v : verbs)
					flagCollector.add(key, v.trim());
			}
			else  if (specificVerbDeriv.matches())
			{
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				flagCollector.add(key, TValues.VERB);
				flagCollector.add(key, specificVerbDeriv.group(1));
			}
			else  if (specificVerbNeg.matches())
			{
				flagCollector.add(key, TValues.VERB);
				flagCollector.add(key, TValues.NEGATIVE_VERB);
				flagCollector.add(key, TValues.NEGATIVE);
				flagCollector.add(key, specificVerbNeg.group(1));
			}
			else if (specificOne.matches())
			{
				String[] prepositions = specificOne.group(2).split(",");
				for (String v : prepositions)
					flagCollector.add(key, v.trim());
				String pos = specificOne.group(1);
				if (pos.matches("prievārd(u|iem)"))
					flagCollector.add(key, TValues.ADPOSITION);
				else if (pos.matches("apst\\."))
					flagCollector.add(key, TValues.ADVERB);
				else if (pos.equals("lietvārdu"))
					flagCollector.add(key, TValues.NOUN);
				else if (pos.equals("jaut. part."))
				{
					flagCollector.add(key, TValues.PARTICLE);
					flagCollector.add(key, TValues.INTERROGATIVE_PARTICLE);
				}
			}
			else  if (specificTwo.matches())
			{
				flagCollector.add(key, specificTwo.group(2));
				String poses = specificTwo.group(1);
				if (poses.equals("lietv. un priev."))
				{
					flagCollector.add(key, TValues.NOUN);
					flagCollector.add(key, TValues.ADPOSITION);
					flagCollector.add(key, TValues.NOUN_WITH_PREPOSITION);
				} else if (poses.equals("adj., retāk vietn."))
				{
					flagCollector.add(TFeatures.ORIGINAL_NEEDED);
					flagCollector.add(key, TValues.ADJECTIVE);
					flagCollector.add(key, TValues.PRONOUN);
				}
				else if (poses.equals("adj., kam ir not. galotne, vai skait."))
				{
					flagCollector.add(key, TValues.ADJECTIVE);
					flagCollector.add(key, TValues.DEFINITE_ENDING);
					flagCollector.add(key, TValues.NUMERAL);
				}
			}

			else if (flagValueRaw.matches("vietn\\. \"tu\"\\.?"))
			{
				flagCollector.add(key, TValues.PRONOUN);
				flagCollector.add(key, TValues.PERSONAL_PRONOUN);
				flagCollector.add(key, "\"tu\"");
			}
			else if (flagValueRaw.matches("atgriez\\. vietn\\. \"sevis\"\\.?"))
			{
				flagCollector.add(key, TValues.PRONOUN);
				flagCollector.add(key, TValues.REFLEXIVE_PRONOUN);
				flagCollector.add(key, "\"sevis\"");
			}
			else if (flagValueRaw.matches("verbu pavēles izteiksmes formā \\(parasti aiz šīs verba formas\\)\\.?"))
			{
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				flagCollector.add(key, TValues.VERB);
				flagCollector.add(key, TValues.IMPERATIVE);
			}
			else if (flagValueRaw.matches("verbu \\(parasti divd\\. formā\\)\\.?"))
			{
				flagCollector.add(key, TValues.VERB);
				flagCollector.add(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.VERB);
				flagCollector.add(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.PARTICIPLE);
			}
			else if (flagValueRaw.matches("skait\\. un \\(parasti\\) adj\\. vai apst\\. pārāko pakāpi\\.?"))
			{
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				flagCollector.add(key, TValues.NUMERAL);
				flagCollector.add(key, TValues.ADJECTIVE);
				flagCollector.add(key, TValues.ADVERB);
				flagCollector.add(key, TValues.COMPARATIVE_DEGREE);
				flagCollector.add(key, TValues.NUMERAL_AND_ADJECTIVE);
				flagCollector.add(key, TValues.NUMERAL_AND_ADVERB);
			}
			else if (flagValueRaw.matches("jaut\\. part\\. \"vai\", jaut\\. vietn\\. vai jaut\\. apst\\."))
			{
				flagCollector.add(key, TValues.PARTICLE);
				flagCollector.add(key, TValues.INTERROGATIVE_PARTICLE);
				flagCollector.add(key, "\"vai\"");
				flagCollector.add(key, TValues.PRONOUN);
				flagCollector.add(key, TValues.INTERROGATIVE_PRONOUN);
				flagCollector.add(key, TValues.ADVERB);
				flagCollector.add(key, TValues.INTEROGATIVE_ADVERB);
			}
			else
			{
				System.err.printf("Neizdevās atšifrēt karodziņu \"%s\"\n", m
						.group(1));
				newBegin = 0;
			}
		}
		return newBegin;
	}

	/**
	 * Izanalizē gramatikas virknes formā:
	 * divd. formā: ...
	 * TODO: ielikt speciālgadījumu, ja aiz divdabja ir izruna.
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText				analizējamais gramatikas teksta fragments
	 * @param flagCollector 		kolekcija, kurā pielikt karodziņus gadījumā,
	 *                      		ja gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processInParticipleFormFlag(String gramText, Flags flagCollector)
	{
		boolean hasComma = gramText.contains(",");
		Pattern flagPattern = hasComma ?
				Pattern.compile("((parasti |bieži |)divd\\.(?: formā)?: (\\p{Ll}+(, \\p{Ll}+)?))([,].*|\\.)?") :
				Pattern.compile("((parasti |bieži |)divd\\.(?: formā)?: (\\p{Ll}+))\\.?");

		int newBegin = -1;
		//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches())
		{
			String[] forms = m.group(3).split(", ");
			newBegin = m.group(1).length();
			String indicator = m.group(2).trim();
			String usedType = TKeys.USED_IN_FORM;
			if (indicator.equals("parasti"))
				usedType = TKeys.USUALLY_USED_IN_FORM;
			else if (indicator.equals("bieži"))
				usedType = TKeys.OFTEN_USED_IN_FORM;
			for (String wordForm : forms)
			{
				Lemma lemma = new TLemma(wordForm);

				flagCollector.add(usedType, TValues.PARTICIPLE);
				String partType = RulesAsFunctions.determineParticipleType(wordForm);
				if (partType != null)
				{
					flagCollector.add(usedType, partType);
					flagCollector.add(TFeatures.POS__VERB);
					newBegin = m.group(1).length();

				} else
				{
					System.err.printf(
							"Neizdodas ielikt divdabja formu formu \"%s\" uztaisīt kā ierobežojumu\n",
							wordForm);
					newBegin = 0;
				}
			}
		}
		return newBegin;
	}

}
