package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.struct.Header;
import lv.ailab.dict.struct.Lemma;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.tezaurs.analyzer.struct.TLemma;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Te būs likumi, ko neizdodas izlikt smukajos, parametrizējamajos Rule
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
	 * Izanalizē doto formu, nosaka kas tas ir par divdabi un saliek atbilstošos
	 * karodziņus.
	 * @param form						analizējamā forma
	 * @param overallFlagCollector		šeit liek tos karodziņus, kas attiecas
	 *                                  uz vārnīcas šķirkli
	 * @param specificFlagCollector		šeit liek tos karodziņus, kas attiecas
	 *                                  šķirkļa galvenes apakšstruktūru, kas
	 *                                  attiecas tieši uz šo formu.
	 * @param usedInFormFrequency		viens no: USUALLY_USED_IN_FORM,
	 *                                  OFTEN_USED_IN_FORM, USED_ONLY_IN_FORM,
	 *                                  ALSO_USED_IN_FORM, USED_IN_FORM
	 * @return 	true, ja viss labi, false - ja šis nav divdabis. Karodziņus liek
	 * 			tikai, ja true.
	 */
	public static boolean determineParticipleType(
			String form, Flags overallFlagCollector, Flags specificFlagCollector,
			TKeys usedInFormFrequency)
	{
		if (usedInFormFrequency != TKeys.USUALLY_USED_IN_FORM &&
				usedInFormFrequency != TKeys.OFTEN_USED_IN_FORM &&
				usedInFormFrequency != TKeys.USED_ONLY_IN_FORM &&
				usedInFormFrequency != TKeys.ALSO_USED_IN_FORM &&
				usedInFormFrequency != TKeys.USED_IN_FORM)
			throw new IllegalArgumentException();

		TValues partType = null;

		if (form.endsWith("damies") || form.endsWith("dams")) //aizvilkties->aizvilkdamies
			partType = TValues.PARTICIPLE_DAMS;
		else if (form.endsWith("ams") || form.endsWith("āms"))
			partType = TValues.PARTICIPLE_AMS;
		else if (form.endsWith("ošs")) // garāmbraucošs
			partType = TValues.PARTICIPLE_OSS;
		else if (form.endsWith("ts")) // aizdzert->aizdzerts
			partType = TValues.PARTICIPLE_TS;
		else if (form.endsWith("is") || form.endsWith("ies")) // aizmakt->aizsmacis, pieriesties->pieriesies
			partType = TValues.PARTICIPLE_IS;
		else if (form.endsWith("ot") || form.endsWith("oties")) // ievērojot
			partType = TValues.PARTICIPLE_OT;
		else
			return false;

		overallFlagCollector.add(TFeatures.POS__VERB);
		overallFlagCollector.add(usedInFormFrequency, TValues.PARTICIPLE.s);
		overallFlagCollector.add(usedInFormFrequency, partType);
		overallFlagCollector.add(usedInFormFrequency, "\"" + form  + "\"");

		specificFlagCollector.add(TKeys.POS, partType);
		specificFlagCollector.add(TFeatures.POS__PARTICIPLE);

		return true;
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
				Pattern.compile("((parasti |bieži |)savienojumā (\"\\p{L}+(,? \\p{L}+)?\"))([.,].*)?") :
				Pattern.compile("((parasti |bieži |)savienojumā (\"\\p{L}+( \\p{L}+)?\"))([.].*)?");

		int newBegin = -1;
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches()) // aijā - savienojumā "aijā, žūžū"
		{
			newBegin = m.group(1).length();
			String indicator = m.group(2).trim();
			TKeys usedType = TKeys.USED_IN_FORM;
			if (indicator.equals("parasti"))
				usedType = TKeys.USUALLY_USED_IN_FORM;
			else if (indicator.equals("bieži"))
				usedType = TKeys.OFTEN_USED_IN_FORM;
			String phrase = m.group(3);
			flagCollector.add(usedType, TValues.PHRASE);
			flagCollector.add(usedType, "\"" + phrase + "\"");
		}
		return newBegin;
	}

	public static int processUsuallyInCaseFlag(
			String gramText, Flags flagCollector)
	{
		//boolean hasComma = gramText.contains(",");
		Pattern flagPattern = Pattern.compile("(bieži lok\\.: (\\w+))([.;].*)?");
		int newBegin = -1;
		Matcher	m = flagPattern.matcher(gramText);
		if (m.matches()) // agrums->agrumā
		{
			newBegin = m.group(1).length();
			flagCollector.add(TKeys.OFTEN_USED_IN_FORM, TValues.LOCATIVE);
			flagCollector.add(TKeys.OFTEN_USED_IN_FORM, "\"" + m.group(2) + "\"");
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
			TKeys key = TKeys.USED_TOGETHER_WITH;
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
			TKeys key = TKeys.USED_TOGETHER_WITH;
			if (modifier.equals("parasti")) key = TKeys.USUALLY_USED_TOGETHER_WITH;
			String flagValueRaw = m.group(3);
			ArrayList<String> flagValues = new ArrayList<>();
			String deabbrCase = AbbrMap.getAbbrMap().translateCase(flagValueRaw);
			Set<String> deabbrPos = AbbrMap.getAbbrMap().translatePos(flagValueRaw);
			if (deabbrCase != null) flagValues.add(deabbrCase);
			else if (deabbrPos != null) flagValues.addAll(deabbrPos);
			else if (flagValueRaw.matches(
					"slimības izraisītāja mikroorganisma, arī slimības nosaukumu\\.?"))
				flagValues.add(TValues.ILLNESS_NAME.s);
			else if (flagValueRaw.matches(
					"laika mērvienības nosaukumu\\.?"))
				flagValues.add(TValues.TIME_UNIT_NAME.s);
			else if (flagValueRaw.matches(
					"apģērba gabala nosaukumu\\.?"))
				flagValues.add(TValues.CLOTHING_UNIT_NAME.s);
			else if (flagValueRaw.matches(
					"mācību priekšmeta nosaukumu\\.?"))
				flagValues.add(TValues.TEACHING_SUBJECT_NAME.s);
			else if (flagValueRaw.matches(
					"dimensiju apzīmējumiem\\.?"))
				flagValues.add(TValues.DIMENSION_NAME.s);
			else if (flagValueRaw.matches(
					"daudzskaitliniekiem vai pāra priekšmetu apzīmējumiem\\.?"))
				flagValues.add(TValues.MULTI_TINGY_NAME.s);
			else if (flagValueRaw.matches("personvārdu\\.?")) flagValues.add(TValues.PERSON_NAME.s);
			else if (flagValueRaw.matches("uzvārdu vīriešu vai sieviešu dzimtē vai savienojumā ar vārdu\\.?"))
				// TODO: Laurai pārbaudīt
				flagValues.add(TValues.PERSON_NAME.s);
			else if (flagValueRaw.matches("daudzuma, masas, lieluma, ilguma apzīmējumu\\.?"))
				flagValues.add(TValues.ADVERBIAL_TERM.s);

			else if (flagValueRaw.matches("verbu\\.?")) flagValues.add(TValues.VERB.s);
			else if (flagValueRaw.matches("bezpriedēkļa verbu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.PREFIXLESS_VERB.s);
			}
			else if (flagValueRaw.matches("priedēkļa verbu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.PREFIX_VERB.s);
			}
			else if (flagValueRaw.matches("(nenoteiksmi|verbu (nenoteiksmes|infinitīva) formā|verba nenoteiksmi|verbu nenoteiksmē)\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.INFINITIVE.s);
			}
			else if (flagValueRaw.matches("verba personas formu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.PERSON_FORM.s);
			}
			else if (flagValueRaw.matches("noliegtu verbu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.NEGATIVE_VERB.s);
			}
			else if (flagValueRaw.matches("verba ciešamās kārtas formu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.PASSIVE_VOICE.s);
			}
			else if (flagValueRaw.matches("patstāvīgas nozīmes verbu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.MAIN_VERB.s);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes verbu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.REPETITION.s);
			}
			else if (flagValueRaw.matches("verbu, kas apzīmē bojāšanu, iznīcināšanu\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.DISTRUCTION_NAME.s);
			}
			else if (flagValueRaw.matches("(verbu, parasti divd\\. formā|divd\\., retāk ar citām verba formām)\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
			}
			else if (flagValueRaw.matches("lietvārdu\\.?")) flagValues.add(TValues.NOUN.s);
			else if (flagValueRaw.matches("(atkārtotu )?vienas un tās pašas saknes (lietvārdu\\.?|lietv\\.)"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.REPETITION.s);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes lietv\\. ar laika nozīmi\\.?"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.TIME_TERM.s);
				flagValues.add(TValues.REPETITION.s);
			}
			else if (flagValueRaw.matches("lietv\\. (lokatīvā\\.?|lok\\.)"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.LOCATIVE.s);
			}
			else if (flagValueRaw.matches("lietv\\. ģen\\.|lietvārdu ģenitīvā\\.?"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.GENITIVE.s);
			}
			else if (flagValueRaw.matches("lietv\\. dsk\\. ģen\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.GENITIVE.s);
				flagValues.add(TValues.PLURAL.s);
			}
			else if (flagValueRaw.matches("lietv\\. akuz\\. vai lok\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.ACUSATIVE.s);
				flagValues.add(TValues.LOCATIVE.s);
			}
			else if (flagValueRaw.matches("lietvārdu, kas nosauc personu ar negatīvu īpašību\\.?"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.PERSON_TERM.s);
				flagValues.add(TValues.NEGATIVE_PERSON_TERM.s);
			}
			else if (flagValueRaw.matches("vienas un tās pašas saknes lietv\\. vai lietv. nozīmē lietotu vārdu\\.?"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.NOUN_CONTAMINATION.s);
				flagValues.add(TValues.REPETITION.s);
			}
			else if (flagValueRaw.matches("pamata skaitļa vārdu\\.?"))
			{
				flagValues.add(TValues.NUMERAL.s);
				flagValues.add(TValues.CARDINAL_NUMERAL.s);
			}
			else if (flagValueRaw.matches("kārtas numerāli\\.?"))
			{
				flagValues.add(TValues.NUMERAL.s);
				flagValues.add(TValues.ORDINAL_NUMERAL.s);
			}
			else if (flagValueRaw.matches("vietniekvārdiem\\.?")) flagValues.add(TValues.PRONOUN.s);
			else if (flagValueRaw.matches("adj\\. pārāko pakāpi\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.COMPARATIVE_DEGREE.s);
			}
			else if (flagValueRaw.matches("adj\\., kam ir not\\. galotne\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.DEFINITE_ENDING.s);
			}
			else if (flagValueRaw.matches("ciešamās kārtas pagātnes divdabi\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.PARTICIPLE.s);
				flagValues.add(TValues.PARTICIPLE_TS.s);
			}
			else if (flagValueRaw.matches("citām līdzīgām interjekcijām\\.?"))
			{
				flagValues.add(TValues.INTERJECTION.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
			}

			else if (flagValueRaw.matches("adj\\. vai apst\\.|apst\\. vai adj\\.|adjektīvu vai adverbu\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.ADVERB.s);
			}
			else if (flagValueRaw.matches("adj\\. vai apst\\. pārāk(ajā|o) pakāp(ē|i)\\.?"))
			{
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.COMPARATIVE_DEGREE.s);
			}
			else if (flagValueRaw.matches("adj\\. vai divd\\."))
			{
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.PARTICIPLE.s);
				flagValues.add(TValues.VERB.s);
			}
			else if (flagValueRaw.matches("adj\\., norād\\., nenot\\. vai vispārin\\. vietn\\."))
			{
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.PRONOUN.s);
				flagValues.add(TValues.DEMONSTRATIVE_PRONOUN.s);
				flagValues.add(TValues.INDEFINITE_PRONOUN.s);
			}
			else if (flagValueRaw.matches("lietv\\. vai vietn\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.PRONOUN.s);
			}
			else if (flagValueRaw.matches("pers\\. vietn\\. vai lietv."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.PRONOUN.s);
				flagValues.add(TValues.PERSONAL_PRONOUN.s);
			}
			else if (flagValueRaw.matches("skait\\. vai nenot\\. vietn\\."))
			{
				flagValues.add(TValues.NUMERAL.s);
				flagValues.add(TValues.PRONOUN.s);
				flagValues.add(TValues.INDEFINITE_PRONOUN.s);
			}
			else if (flagValueRaw.matches("subst\\. vai pron\\. dat\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.PRONOUN.s);
				flagValues.add(TValues.DATIVE.s);
			}
			else if (flagValueRaw.matches("pers. vietn\\. vai lietv., kas apzīmē personu\\.?"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.PRONOUN.s);
				flagValues.add(TValues.PERSONAL_PRONOUN.s);
				flagValues.add(TValues.PERSON_TERM.s);
			}
			else if (flagValueRaw.matches("lietv\\., retāk ar apst\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
				// TODO: specifisko karodziņu vajag?
			}
			else if (flagValueRaw.matches("lietv\\., retāk ar apst\\., verbu\\.?"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
			}
			else if (flagValueRaw.matches("(vietniekvārdiem, retāk apstākļa vārdiem|dažiem apstākļa vārdiem (un|vai) vietniekvārdiem)\\.?"))
			{
				flagValues.add(TValues.PRONOUN.s);
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes lietv\\. vai adj\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.REPETITION.s);
			}
			else if (flagValueRaw.matches("atkārtotu vienas un tās pašas saknes adj\\. vai apst\\."))
			{
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.REPETITION.s);
			}
			else if (flagValueRaw.matches("not\\. galotnes adj\\. lietv\\. nozīmē vai savienojumā ar adj\\. vispārāko pakāpi\\."))
			{
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.COMPARATIVE_DEGREE.s);
				flagValues.add(TValues.SUPERLATIVE_DEGREE.s);
				flagValues.add(TValues.NOUN_CONTAMINATION_ADJ_COMP.s);
			}

			else if (flagValueRaw.matches("apzīmētāju\\.?")) flagValues.add(TValues.ATTRIBUTE.s);
			else if (flagValueRaw.matches("apst\\. vai tā nozīmē lietotu vārdu\\.?"))
			{
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.ADVERB_CONTAMINATION.s);
				// TODO
			}
			else if (flagValueRaw.matches("vietas adv\\. vai tā nozīmē lietotu vārdu\\.?"))
			{
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.PLACE_ADVERB.s);
				flagValues.add(TValues.PLACE_ADVERB_CONTAMINATION.s);
				// TODO
			}
			else if (flagValueRaw.matches("īpašvārdu\\.?")) flagValues.add(TValues.PROPER.s);
			else if (flagValueRaw.matches("dat\\. un apst\\. vai adj\\."))
			{
				flagValues.add(TValues.DATIVE.s);
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.ADJECTIVE.s);
				flagValues.add(TValues.DATIVE_AND_ADVERB.s); // man palika žēl
															// palika silts
			}
			else if (flagValueRaw.matches("lok\\. vai nenoteiksmi\\.?"))
			{
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.INFINITIVE.s);
				flagValues.add(TValues.LOCATIVE.s);
			}
			else if (flagValueRaw.matches("vietas nosaukumu lok\\."))
			{
				flagValues.add(TValues.PLACE_NAME.s);
				flagValues.add(TValues.LOCATIVE.s);
			}
			else if (flagValueRaw.matches("divsk\\. nom\\."))
			{
				flagValues.add(TValues.DUAL.s);
				flagValues.add(TValues.NOMINATIVE.s);
			}
			else if (flagValueRaw.matches("divsk\\. nom\\., akuz\\."))
			{
				flagValues.add(TValues.DUAL.s);
				flagValues.add(TValues.ACUSATIVE.s);
				flagValues.add(TValues.NOMINATIVE.s);
			}
			else if (flagValueRaw.matches("apst\\. un noliegtu verbu\\.?"))
			{
				flagValues.add(TValues.ADVERB.s);
				flagValues.add(TValues.VERB.s);
				flagValues.add(TValues.NEGATIVE_VERB.s);
				flagValues.add(TValues.ADVERB_AND_NEGVERB.s);
			}
			else if (flagValueRaw.matches("lietv\\., skait\\. vai skait\\. un lietv\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.NUMERAL.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
			}
			else if (flagValueRaw.matches("lietv\\. lok\\. vai lietv\\. un priev\\."))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.PREPOSITION.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
			}
			else if (flagValueRaw.matches("skait\\. vai skait\\. un lietv\\. dat\\.(, ģen\\., nom\\., (akuz\\., )?divsk\\. nom\\., akuz\\.)?"))
			{
				flagValues.add(TValues.NOUN.s);
				flagValues.add(TValues.NUMERAL.s);
				flagValues.add(TValues.ORIGINAL_NEEDED.s);
			}

			// TODO: salabot, ka pa tiešo liek pārīšus iekšā flags?
			if (flagValues.size() > 0) for (String fv: flagValues)
				if (fv.equals(TValues.ORIGINAL_NEEDED.s))
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
			TKeys key = TKeys.USED_TOGETHER_WITH;
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

				flagCollector.add(key, TValues.VERB.s);
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

				flagCollector.add(key, TValues.VERB.s);
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				for (String v : verbs)
					flagCollector.add(key, v.trim());
			}
			else  if (specificVerbDeriv.matches())
			{
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				flagCollector.add(key, TValues.VERB.s);
				flagCollector.add(key, specificVerbDeriv.group(1));
			}
			else  if (specificVerbNeg.matches())
			{
				flagCollector.add(key, TValues.VERB.s);
				flagCollector.add(key, TValues.NEGATIVE_VERB.s);
				flagCollector.add(key, specificVerbNeg.group(1));
			}
			else if (specificOne.matches())
			{
				String[] prepositions = specificOne.group(2).split(",");
				for (String v : prepositions)
					flagCollector.add(key, v.trim());
				String pos = specificOne.group(1);
				if (pos.matches("prievārd(u|iem)"))
					flagCollector.add(key, TValues.PREPOSITION.s);
				else if (pos.matches("apst\\."))
					flagCollector.add(key, TValues.ADVERB.s);
				else if (pos.equals("lietvārdu"))
					flagCollector.add(key, TValues.NOUN.s);
				else if (pos.equals("jaut. part."))
				{
					flagCollector.add(key, TValues.PARTICLE.s);
					flagCollector.add(key, TValues.INTERROGATIVE_PARTICLE.s);
				}
			}
			else  if (specificTwo.matches())
			{
				flagCollector.add(key, specificTwo.group(2));
				String poses = specificTwo.group(1);
				if (poses.equals("lietv. un priev."))
				{
					flagCollector.add(key, TValues.NOUN.s);
					flagCollector.add(key, TValues.PREPOSITION.s);
					flagCollector.add(key, TValues.NOUN_WITH_PREPOSITION.s);
				} else if (poses.equals("adj., retāk vietn."))
				{
					flagCollector.add(TFeatures.ORIGINAL_NEEDED);
					flagCollector.add(key, TValues.ADJECTIVE.s);
					flagCollector.add(key, TValues.PRONOUN.s);
				}
				else if (poses.equals("adj., kam ir not. galotne, vai skait."))
				{
					flagCollector.add(key, TValues.ADJECTIVE.s);
					flagCollector.add(key, TValues.DEFINITE_ENDING.s);
					flagCollector.add(key, TValues.NUMERAL.s);
				}
			}

			else if (flagValueRaw.matches("vietn\\. \"tu\"\\.?"))
			{
				flagCollector.add(key, TValues.PRONOUN.s);
				flagCollector.add(key, TValues.PERSONAL_PRONOUN.s);
				flagCollector.add(key, "\"tu\"");
			}
			else if (flagValueRaw.matches("atgriez.\\ vietn\\. \"sevis\"\\.?"))
			{
				flagCollector.add(key, TValues.PRONOUN.s);
				flagCollector.add(key, TValues.REFLEXIVE_PRONOUN.s);
				flagCollector.add(key, "\"sevis\"");
			}
			else if (flagValueRaw.matches("verbu pavēles izteiksmes formā \\(parasti aiz šīs verba formas\\)\\.?"))
			{
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				flagCollector.add(key, TValues.VERB.s);
				flagCollector.add(key, TValues.IMPERATIVE.s);
			}
			else if (flagValueRaw.matches("verbu \\(parasti divd\\. formā\\)\\.?"))
			{
				flagCollector.add(key, TValues.VERB.s);
				flagCollector.add(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.VERB.s);
				flagCollector.add(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.PARTICIPLE.s);
			}
			else if (flagValueRaw.matches("skait\\. un \\(parasti\\) adj\\. vai apst\\. pārāko pakāpi\\.?"))
			{
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				flagCollector.add(key, TValues.NUMERAL.s);
				flagCollector.add(key, TValues.ADJECTIVE.s);
				flagCollector.add(key, TValues.ADVERB.s);
				flagCollector.add(key, TValues.COMPARATIVE_DEGREE.s);
				flagCollector.add(key, TValues.NUMERAL_AND_ADJECTIVE.s);
				flagCollector.add(key, TValues.NUMERAL_AND_ADVERB.s);
			}
			else if (flagValueRaw.matches("jaut\\. part\\. \"vai\", jaut\\. vietn\\. vai jaut\\. apst\\."))
			{
				flagCollector.add(key, TValues.PARTICLE.s);
				flagCollector.add(key, TValues.INTERROGATIVE_PARTICLE.s);
				flagCollector.add(key, "\"vai\"");
				flagCollector.add(key, TValues.PRONOUN.s);
				flagCollector.add(key, TValues.INTERROGATIVE_PRONOUN.s);
				flagCollector.add(key, TValues.ADVERB.s);
				flagCollector.add(key, TValues.INTEROGATIVE_ADVERB.s);
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
	 * @param altLemmasCollector	kolekcija, kurā pielikt izveidotās
	 *                              papildlemmas.
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processInParticipleFormFlag(String gramText,
			Flags flagCollector, List<Header> altLemmasCollector)
	{
		boolean hasComma = gramText.contains(",");
		Pattern flagPattern = hasComma ?
				Pattern.compile("((parasti |bieži |)divd\\. formā: (\\p{Ll}+(, \\p{Ll}+)?))([,].*|\\.)?") :
				Pattern.compile("((parasti |bieži |)divd\\. formā: (\\p{Ll}+))\\.?");

		int newBegin = -1;
		//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches())
		{
			String[] newLemmas = m.group(3).split(", ");
			newBegin = m.group(1).length();
			String indicator = m.group(2).trim();
			TKeys usedType = TKeys.USED_IN_FORM;
			if (indicator.equals("parasti"))
				usedType = TKeys.USUALLY_USED_IN_FORM;
			else if (indicator.equals("bieži"))
				usedType = TKeys.OFTEN_USED_IN_FORM;
			for (String newLemma : newLemmas)
			{
				Lemma altLemma = new TLemma(newLemma);
				Flags altParams = new Flags();

				flagCollector.add(TFeatures.POS__VERB);
				flagCollector.add(usedType, TValues.PARTICIPLE);
				flagCollector.add(usedType, "\"" + newLemma + "\"");
				Boolean success = RulesAsFunctions.determineParticipleType(
						newLemma, flagCollector, altParams, usedType);
				if (success)
				{
					newBegin = m.group(1).length();
					altParams.add(TFeatures.POS__PARTICIPLE);
					altParams.add(TFeatures.ENTRYWORD__CHANGED_PARADIGM);
					altLemmasCollector.add(new Header(altLemma, 0, altParams));

				} else
				{
					System.err.printf(
							"Neizdodas ielikt formu \"%s\" paradigmā 0 (Divdabis)\n",
							newLemma);
					newBegin = 0;
				}
			}
		}
		return newBegin;
	}

}
