package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.struct.StructRestrs;
import lv.ailab.dict.struct.constants.structrestrs.Type;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.tezaurs.struct.constants.flags.TKeys;
import lv.ailab.dict.tezaurs.struct.constants.flags.TValues;
import lv.ailab.dict.tezaurs.struct.constants.structrestrs.TFrequency;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;
import java.util.LinkedList;
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
				"(saīs\\. no )?(afrikandu|angļu|arābu|ebreju|fr\\.|it\\.|ivrita|lat\\.|latīņu|sanskr\\.|senebr\\.|spāņu|tibetiešu|vācu|zviedru)( val\\.)? "
						+ "(\"[-, \\p{L}]+\"|- [, \\p{L}]+)"
						+ "( - deminutīvs no \"[ \\p{Ll}]+\")?\\.?"))
		{
			flagCollector.add(Tuple.of(TKeys.ETYMOLOGY, gramFragment));
			return true;
		}
		return false;
	}
	/**
	 * Izanalizē gramatikas virknes formā:
	 * parasti savienojumā "X, Y" vai atkārtojumā "X, X".
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText		analizējamais gramatikas teksta fragments
	 * @param restrCollector	kolekcija, kurā pielikt ierobežojumus gadījumā,
	 *                      	ja gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processInPhraseOrRepFlag(String gramText, StructRestrs restrCollector)
	{
		Pattern flagPattern = Pattern.compile(
				"((parasti |bieži |arī |)(?:savienojumā|atkārtojumā):? [\"'](\\p{L}+((, | - | )\\p{L}+)*)[\"'] vai (?:savienojumā|atkārtojumā) [\"'](\\p{L}+((, | - | )\\p{L}+)*)[\"'])([.,].*)?");

		int newBegin = -1;
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches()) // aijā - savienojumā "aijā, žūžū"
		{
			newBegin = m.group(1).length();
			String indicator = m.group(2).trim();
			String restrFreq = TFrequency.UNDISCLOSED;
			switch (indicator)
			{
				case "parasti": restrFreq = TFrequency.USUALLY;
					break;
				case "bieži": restrFreq = TFrequency.OFTEN;
					break;
				case "arī": restrFreq = TFrequency.ALSO;
					break;
			}
			String phrase1 = m.group(3);
			restrCollector.addOne(Type.IN_STRUCT, restrFreq,
					Tuple.of(TKeys.OTHER_FLAGS, TValues.PHRASE), phrase1);
			String phrase2 = m.group(4);
			restrCollector.addOne(Type.IN_STRUCT, restrFreq,
					Tuple.of(TKeys.OTHER_FLAGS, TValues.PHRASE), phrase2);
		}
		return newBegin;
	}
	/**
	 * Izanalizē gramatikas virknes formā:
	 * parasti savienojumā "X, Y".
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText		analizējamais gramatikas teksta fragments
	 * @param flagCollector kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                      gramatikas fragments atbilst šim likumam
	 * @param restrCollector 	kolekcija, kurā pielikt ierobežojumus gadījumā,
	 * 	                    	ja gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processInPhraseFlag(
			String gramText, Flags flagCollector, StructRestrs restrCollector)
	{
		//boolean hasComma = gramText.contains(",");
		//Pattern flagPattern = hasComma ?
		//		Pattern.compile("((parasti |bieži |arī |)(?:savienojumā|atkārtojumā) [\"'](\\p{L}+((, | - |-| )\\p{L}+)*)[\"'])([.,].*)?") :
		//		Pattern.compile("((parasti |bieži |arī )(?:savienojumā|atkārtojumā) [\"'](\\p{L}+(( - |-| )\\p{L}+)*)[\"'])([.].*)?");
		Pattern flagPattern = Pattern.compile(
				"((parasti |bieži |arī |)(?:(?:savienojum|atkārtojum)(?:ā|os)):? ((\"\\p{L}+((, | ?- ?| )\\p{L}+)*\"(?:,(?: arī)? \"\\p{L}+((, | ?- ?| )\\p{L}+)*\")*)(?: u\\. tml\\.)?))([.,].*)?");

		int newBegin = -1;
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches()) // aijā - savienojumā "aijā, žūžū"
		{
			newBegin = m.group(1).length();
			String indicator = m.group(2).trim();
			//String usedType = TKeys.USED_IN_STRUCT;
			String restrFreq = TFrequency.UNDISCLOSED;
			switch (indicator)
			{
				case "parasti": restrFreq = TFrequency.USUALLY;
					break;
				case "bieži": restrFreq = TFrequency.OFTEN;
					break;
				case "arī": restrFreq = TFrequency.ALSO;
					break;
			}

			String phrasesWithStuff = m.group(3);
			if (phrasesWithStuff.endsWith(" u. tml."))
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
			String phrasesOnly = m.group(4);
			if (phrasesOnly.startsWith("\"")) phrasesOnly = phrasesOnly.substring(1);
			if (phrasesOnly.endsWith("\"")) phrasesOnly = phrasesOnly.substring(0, phrasesOnly.length()-1);
			String[] phrases = phrasesOnly.split("\",(?: arī)? \"");
			for (String p : phrases)
				restrCollector.addOne(Type.IN_STRUCT, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.PHRASE), p.trim());
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
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 * 	                    	ja gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processTogetherWithQuotFlag(
			String gramText, Flags flagCollector, StructRestrs restrCollector)
	{
		//boolean hasComma = gramText.contains(",");
		Pattern flagPattern = Pattern.compile("((parasti |)savienojum(?:ā|os) ar ((\"\\p{L}+(?:(?:, | ?- ?| )\\p{L}+)*\"((?:,| vai| arī) \"\\p{L}+(?:(?:, | ?- ?| )\\p{L}+)*\")*)(?: formām)?( u\\. tml\\.)?)\\.?)([,;].*)?");
		int newBegin = -1;
		Matcher	m = flagPattern.matcher(gramText);
		if (m.matches())
		{
			newBegin = m.group(1).length();
			String modifier = m.group(2).trim();
			//String key = TKeys.USED_TOGETHER_WITH;
			//if (modifier.equals("parasti")) key = TKeys.USUALLY_USED_TOGETHER_WITH;
			String restrFreq = modifier.equals("parasti") ? TFrequency.USUALLY : TFrequency.UNDISCLOSED;

			String phrasesWithStuff = m.group(3);
			String phrasesOnly = m.group(4);
			if (phrasesWithStuff.endsWith(" u. tml."))
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
			String[] phrases = phrasesOnly.split("(?<=\")(, | vai | arī )(?=\")");
			for (String w : phrases)
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, w.trim());
				//flagCollector.add(key, w.trim());
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
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 * 	                    	ja gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processTogetherWithGenFlag(
			String gramText, Flags flagCollector, StructRestrs restrCollector)
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
			String restrFreq = modifier.equals("parasti") ? TFrequency.USUALLY : TFrequency.UNDISCLOSED;

			String restrValueRaw = m.group(3);
			//TODO
			Set<String> deabbrPos = AbbrMap.getAbbrMap().translatePos(restrValueRaw);
			//else if (deabbrPos != null) flagValues.addAll(deabbrPos);
			if (restrValueRaw.matches("dat\\.|ģen\\.|lok\\.|adj\\.|apst\\.|adv\\.|lietv\\.|skait\\.|subst\\.|vietn\\."))
			{
				Tuple<String, String> feature = null;
				switch (restrValueRaw)
				{
					case "dat.": feature = TFeatures.CASE__DATIVE;
						break;
					case "ģen.": feature = TFeatures.CASE__GENITIVE;
						break;
					case "lok.": feature = TFeatures.CASE__LOCATIVE;
						break;
					case "adj.": feature = TFeatures.POS__ADJ;
						break;
					case "apst.":
					case "adv.": feature = TFeatures.POS__ADV;
						break;
					case "lietv.":
					case "subst.": feature = TFeatures.POS__NOUN;
						break;
					case "skait.": feature = TFeatures.POS__NUMERAL;
						break;
					case "vietn.": feature = TFeatures.POS__PRONOUN;
						break;
				}
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, feature);
			}
			else if (restrValueRaw.matches(
					"slimības izraisītāja mikroorganisma, arī slimības nosaukumu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.ILLNESS_NAME));
			else if (restrValueRaw.matches(
					"laika mērvienības nosaukumu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.TIME_UNIT_NAME));
			else if (restrValueRaw.matches(
					"apģērba gabala nosaukumu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.CLOTHING_UNIT_NAME));
			else if (restrValueRaw.matches(
					"mācību priekšmeta nosaukumu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.TEACHING_SUBJECT_NAME));
			else if (restrValueRaw.matches(
					"dimensiju apzīmējumiem\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.DIMENSION_NAME));
			else if (restrValueRaw.matches(
					"daudzskaitliniekiem vai pāra priekšmetu apzīmējumiem\\.?"))
			restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
					Tuple.of(TKeys.OTHER_FLAGS, TValues.MULTI_TINGY_NAME));
			else if (restrValueRaw.matches("personvārdu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.PERSON_NAME));
			else if (restrValueRaw.matches("uzvārdu vīriešu vai sieviešu dzimtē vai savienojumā ar vārdu\\.?"))
				// TODO: Laurai pārbaudīt
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.PERSON_NAME));
			else if (restrValueRaw.matches("daudzuma, masas, lieluma, ilguma apzīmējumu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.ADVERBIAL_TERM));

			else if (restrValueRaw.matches("verbu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__VERB);
			else if (restrValueRaw.matches("bezpriedēkļa verbu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.PREFIX__NONE});
			else if (restrValueRaw.matches("priedēkļa verbu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.PREFIX__HAS});
			else if (restrValueRaw.matches("(nenoteiksmi|verbu (nenoteiksmes|infinitīva) formā|verba nenoteiksmi|verbu nenoteiksmē)\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__INFINITIVE});
			else if (restrValueRaw.matches("verba personas formu\\.?"))
			{
				// TODO: check this!
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__INDICATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__IMPERATIVE});
			}
			else if (restrValueRaw.matches("noliegtu verbu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.POS__NEG_VERB});
			else if (restrValueRaw.matches("verba ciešamās kārtas formu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.VOICE__PASIVE});
			else if (restrValueRaw.matches("patstāvīgas nozīmes verbu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, Tuple.of(TKeys.POS, TValues.MAIN_VERB)});
			else if (restrValueRaw.matches("atkārtotu vienas un tās pašas saknes verbu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.REPETITION_WITH_ONE_STEM});
			else if (restrValueRaw.matches("verbu, kas apzīmē bojāšanu, iznīcināšanu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, Tuple.of(TKeys.OTHER_FLAGS, TValues.DISTRUCTION_NAME)});
			else if (restrValueRaw.matches("divd\\."))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__PARTICIPLE});
			else if (restrValueRaw.matches("(verbu, parasti divd\\. formā|divd\\., retāk ar citām verba formām)\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.USUALLY,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__PARTICIPLE});

				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__CONDITIONAL});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__DEBITIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__IMPERATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__INFINITIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__INDICATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__RELATIVE});
			}

			else if (restrValueRaw.matches("lietvārdu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NOUN);
			else if (restrValueRaw.matches("(atkārtotu )?vienas un tās pašas saknes (lietvārdu\\.?|lietv\\.)"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.REPETITION_WITH_ONE_STEM});
			else if (restrValueRaw.matches("atkārtotu vienas un tās pašas saknes lietv\\. ar laika nozīmi\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, new Tuple[] {
						TFeatures.POS__NOUN, Tuple.of(TKeys.OTHER_FLAGS, TValues.TIME_TERM),
						TFeatures.REPETITION_WITH_ONE_STEM});
			else if (restrValueRaw.matches("lietv\\. (lokatīvā\\.?|lok\\.)"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.CASE__LOCATIVE});
			else if (restrValueRaw.matches("lietv\\. ģen\\.|lietvārdu ģenitīvā\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.CASE__GENITIVE});
			else if (restrValueRaw.matches("lietv\\. dsk\\. ģen\\."))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.CASE__GENITIVE, TFeatures.NUMBER__PLURAL});
			else if (restrValueRaw.matches("lietv\\. akuz\\. vai lok\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.CASE__ACUSATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.CASE__LOCATIVE});
			}
			else if (restrValueRaw.matches("lietvārdu, kas nosauc personu ar negatīvu īpašību\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, new Tuple[] {
						TFeatures.POS__NOUN, Tuple.of(TKeys.OTHER_FLAGS, TValues.PERSON_TERM),
						Tuple.of(TKeys.OTHER_FLAGS, TValues.NEGATIVE_PERSON_TERM)});
			else if (restrValueRaw.matches("vienas un tās pašas saknes lietv\\. vai lietv. nozīmē lietotu vārdu\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.REPETITION_WITH_ONE_STEM});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.CONTAMINATION__NOUN, TFeatures.REPETITION_WITH_ONE_STEM});
			}

			else if (restrValueRaw.matches("pamata skaitļa vārdu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NUMERAL, TFeatures.POS__CARD_NUMERAL});
			else if (restrValueRaw.matches("kārtas numerāli\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NUMERAL, TFeatures.POS__ORD_NUMERAL});
			else if (restrValueRaw.matches("norād\\. vietn\\."))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.POS__DEM_PRONOUN});
			else if (restrValueRaw.matches("pieder\\. vietn\\."))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.POS__POSS_PRONOUN});
			else if (restrValueRaw.matches("vietniekvārdiem\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__PRONOUN);

			else if (restrValueRaw.matches("adj\\. pārāko pakāpi\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADJ, TFeatures.DEGREE__COMP});
			else if (restrValueRaw.matches("adj\\., kam ir not\\. galotne\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADJ, TFeatures.DEFINITNESS__DEF});
			else if (restrValueRaw.matches("ciešamās kārtas pagātnes divdabi\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, new Tuple[] {
						TFeatures.POS__VERB, TFeatures.MOOD__PARTICIPLE,
						Tuple.of(TKeys.MOOD, TValues.PARTICIPLE_TS)});
			}

			else if (restrValueRaw.matches("citām līdzīgām interjekcijām\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__INTERJECTION);
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
			}

			else if (restrValueRaw.matches("adj\\. vai apst\\.|apst\\. vai adj\\.|adjektīvu vai adverbu\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADJ);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADV);
			}
			else if (restrValueRaw.matches("adj\\. vai apst\\. pārāk(ajā|o) pakāp[ēi]\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADJ, TFeatures.DEGREE__COMP});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADV, TFeatures.DEGREE__COMP});
			}
			else if (restrValueRaw.matches("adj\\. vai divd\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADJ);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__PARTICIPLE});
			}
			else if (restrValueRaw.matches("adj\\., norād\\., nenot\\. vai vispārin\\. vietn\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADJ);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.POS__DEM_PRONOUN});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.POS__INDEF_PRONOUN});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.POS__GEN_PRONOUN});
			}
			else if (restrValueRaw.matches("lietv\\. vai vietn\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NOUN);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__PRONOUN);
			}
			else if (restrValueRaw.matches("pers\\. vietn\\. vai lietv."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NOUN);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.POS__PERS_PRONOUN});
			}
			else if (restrValueRaw.matches("skait\\. vai nenot\\. vietn\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NUMERAL);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.POS__INDEF_PRONOUN});
			}
			else if (restrValueRaw.matches("subst\\. vai pron\\. dat\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.CASE__DATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__PRONOUN, TFeatures.CASE__DATIVE});
			}
			else if (restrValueRaw.matches("pers. vietn\\. vai lietv., kas apzīmē personu\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, Tuple.of(TKeys.OTHER_FLAGS, TValues.PERSON_TERM)});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, new Tuple[] {
						TFeatures.POS__PRONOUN, TFeatures.POS__PERS_PRONOUN,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.PERSON_TERM)});
			}
			else if (restrValueRaw.matches("lietv\\., retāk ar apst\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NOUN);
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER, TFeatures.POS__ADV);
			}
			else if (restrValueRaw.matches("lietv\\., retāk ar apst\\., verbu\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NOUN);
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER, TFeatures.POS__ADV);
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER, TFeatures.POS__VERB);
			}
			else if (restrValueRaw.matches("(vietniekvārdiem, retāk apstākļa vārdiem)\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__PRONOUN);
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER, TFeatures.POS__ADV);
			}
			else if (restrValueRaw.matches("(dažiem apstākļa vārdiem (un|vai) vietniekvārdiem)\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__PRONOUN);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADV);
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
			}
			else if (restrValueRaw.matches("atkārtotu vienas un tās pašas saknes lietv\\. vai adj\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.REPETITION_WITH_ONE_STEM});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADJ, TFeatures.REPETITION_WITH_ONE_STEM});
			}
			else if (restrValueRaw.matches("atkārtotu vienas un tās pašas saknes adj\\. vai apst\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADJ, TFeatures.REPETITION_WITH_ONE_STEM});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADV, TFeatures.REPETITION_WITH_ONE_STEM});
			}
			else if (restrValueRaw.matches("not\\. galotnes adj\\. lietv\\. nozīmē vai savienojumā ar adj\\. vispārāko pakāpi\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADJ, TFeatures.DEFINITNESS__DEF, TFeatures.CONTAMINATION__NOUN});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.UNDISCLOSED,
						new Tuple[] {TFeatures.POS__ADJ, TFeatures.DEGREE__SUPER, });
			}

			else if (restrValueRaw.matches("apzīmētāju\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.ATTRIBUTE));
			else if (restrValueRaw.matches("apst\\. vai tā nozīmē lietotu vārdu\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADV);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.CONTAMINATION__ADVERB);
			}
			else if (restrValueRaw.matches("vietas adv\\. vai tā nozīmē lietotu vārdu\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__ADV, Tuple.of(TKeys.POS, TValues.PLACE_ADVERB)});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.CONTAMINATION, TValues.PLACE_ADVERB));
			}
			else if (restrValueRaw.matches("īpašvārdu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						Tuple.of(TKeys.OTHER_FLAGS, TValues.PROPER));
			else if (restrValueRaw.matches("dat\\. un apst\\. vai adj\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.CASE__DATIVE, TFeatures.POS__ADV});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADV);
				// man palika žēl
				// palika silts
			}
			else if (restrValueRaw.matches("lok\\. vai nenoteiksmi\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.CASE__LOCATIVE);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__INFINITIVE});
			}
			else if (restrValueRaw.matches("vietas nosaukumu lok\\."))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.CASE__LOCATIVE, TFeatures.PLACE_NAME});
			else if (restrValueRaw.matches("divsk\\. nom\\."))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.NUMBER__DUAL, TFeatures.CASE__NOMINATIVE});
			else if (restrValueRaw.matches("divsk\\. nom\\., akuz\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.NUMBER__DUAL, TFeatures.CASE__NOMINATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.NUMBER__DUAL, TFeatures.CASE__ACUSATIVE});
			}
			else if (restrValueRaw.matches("apst\\. un noliegtu verbu\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.POS__NEG_VERB, TFeatures.POS__ADV,});
			else if (restrValueRaw.matches("lietv\\., skait\\. vai skait\\. un lietv\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NOUN);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NUMERAL);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.POS__NUMERAL});
			}
			else if (restrValueRaw.matches("lietv\\. lok\\. vai lietv\\. un priev\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.CASE__LOCATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.POS__ADPOSITION});
			}
			else if (restrValueRaw.matches("skait\\. vai skait\\. un lietv\\. dat\\.(, ģen\\., nom\\.(, akuz\\.)?)?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__NUMERAL);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__NOUN, TFeatures.POS__NUMERAL});
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
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
	 * savienojumā ar ... " ... " ...
	 * parasti savienojumā ar ... " ... " ...
	 * Metode pielāgota gan gramatiku fragmentiem ar komatiem, gan bez.
	 * @param gramText		analizējamais gramatikas teksta fragments
	 * @param flagCollector kolekcija, kurā pielikt karodziņus gadījumā, ja
	 *                      gramatikas fragments atbilst šim likumam
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 * 	                    	ja gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processTogetherWithGenQuotFlag(
			String gramText, Flags flagCollector, StructRestrs restrCollector)
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
			String restrFreq = modifier.equals("parasti") ? TFrequency.USUALLY : TFrequency.UNDISCLOSED;

			//String key = TKeys.USED_TOGETHER_WITH;
			//if (modifier.equals("parasti")) key = TKeys.USUALLY_USED_TOGETHER_WITH;
			String flagValueRaw = m.group(3);
			//ArrayList<String> flagValues = new ArrayList<>();

			Matcher specificVerb1 = Pattern.compile("verb[ua] (\"\\p{L}+(\\(ies\\))?\"(, (retāk )?\"\\p{L}+(\\(ies\\))?\")*)( formām)?\\.?").matcher(flagValueRaw);
			Matcher specificVerb2 = Pattern.compile("verbiem (\"\\p{L}+\"(, \"\\p{L}+\")*) u\\. tml\\.").matcher(flagValueRaw);
			Matcher specificVerbDeriv = Pattern.compile("verbu \"(\\p{L}+)\" (un|vai) atvasinājumiem no tā\\.?").matcher(flagValueRaw);
			Matcher specificVerbNeg = Pattern.compile("verba \"(\\p{L}+)\" noliegt(aj)?ām formām\\.?").matcher(flagValueRaw);
			Matcher specificOne = Pattern.compile("(prievārd(?:u|iem)|apst\\.|lietvārdu|jaut\\. part\\.) (\"\\p{L}+\"(, \"\\p{L}+\")*)\\.?").matcher(flagValueRaw);
			Matcher specificTwo = Pattern.compile("(lietv\\. un priev\\.|adj\\., retāk vietn\\.|adj\\., kam ir not\\. galotne, vai skait\\.) \"(\\p{L}+)\"\\.?").matcher(flagValueRaw);
			if (specificVerb1.matches())
			{
				String verbGroup = specificVerb1.group(1);
				LinkedList<String> verbStrings = new LinkedList<>();
				if (!verbGroup.contains(", retāk "))
					verbStrings.add(verbGroup);
				else
				{
					verbStrings.add(verbGroup.substring(0, verbGroup.indexOf(", retāk ")));
					verbStrings.add(verbGroup
							.substring(verbStrings.indexOf(", retāk ") + ", retāk ".length())
							.replace(", retāk ", ", "));
				}

				for (int i = 0; i < verbStrings.size(); i++)
				{
					String verbStr = verbStrings.get(0);
					String[] verbs = verbStr.split(",");
					String updatedFreq = i > 0 ? TFrequency.RARER : restrFreq;
					for (String v : verbs)
					{
						if (v.startsWith("\"")) v = v.substring(1);
						if (v.endsWith("\"")) v = v.substring(0, v.length() - 1);
						if (v.endsWith("(ies)"))
						{
							restrCollector.addOne(Type.TOGETHER_WITH, updatedFreq,
									TFeatures.POS__VERB, v.replace("(ies)", "").trim());
							restrCollector.addOne(Type.TOGETHER_WITH, updatedFreq,
									TFeatures.POS__VERB, v.replaceAll("[)(]", "").trim());
						} else
							restrCollector.addOne(Type.TOGETHER_WITH, updatedFreq,
									TFeatures.POS__VERB, v.trim());
					}

				}
			}
			else if (specificVerb2.matches())
			{
				String[] verbs = specificVerb2.group(1).split(",");
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				for (String v : verbs)
				{
					v = v.trim();
					if (v.startsWith("\"")) v = v.substring(1);
					if (v.endsWith("\"")) v = v.substring(0, v.length() - 1);
					restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__VERB, v);
				}
			}
			else  if (specificVerbDeriv.matches())
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, Tuple.of(TKeys.OTHER_FLAGS, TValues.DERIVATIVES_OF)},
						specificVerbDeriv.group(1));
			else  if (specificVerbNeg.matches())
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[] {TFeatures.POS__VERB, TFeatures.POS__NEG_VERB},
						specificVerbNeg.group(1));
			else if (specificOne.matches())
			{
				String pos = specificOne.group(1);
				ArrayList<Tuple> posList = new ArrayList<>();
				if (pos.matches("prievārd(u|iem)"))
					posList.add(TFeatures.POS__ADPOSITION);
				else if (pos.matches("apst\\."))
					posList.add(TFeatures.POS__ADV);
				else if (pos.equals("lietvārdu"))
					posList.add(TFeatures.POS__NOUN);
				else if (pos.equals("jaut. part."))
				{
					posList.add(TFeatures.POS__PARTICLE);
					posList.add(Tuple.of(TKeys.POS, TValues.INTERROGATIVE_PARTICLE));
				}
				String[] prepositions = specificOne.group(2).split(",");
				for (String v : prepositions)
				{
					v = v.trim();
					if (v.startsWith("\"")) v = v.substring(1);
					if (v.endsWith("\"")) v = v.substring(0, v.length() - 1);
					restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
							posList.toArray(new Tuple[posList.size()]), v);
				}
			}
			else  if (specificTwo.matches())
			{
				String word = specificTwo.group(2);
				String poses = specificTwo.group(1);
				switch (poses)
				{
					case "lietv. un priev.":
						flagCollector.add(TFeatures.ORIGINAL_NEEDED);
						restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
								new Tuple[]{TFeatures.POS__NOUN, TFeatures.POS__ADPOSITION},
								word);
						break;
					case "adj., retāk vietn.":
						restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__ADJ);
						restrCollector.addOne(Type.TOGETHER_WITH, restrFreq, TFeatures.POS__PRONOUN, word);
						break;
					case "adj., kam ir not. galotne, vai skait.":
						restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
								new Tuple[]{TFeatures.POS__ADJ, TFeatures.DEFINITNESS__DEF});
						restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
								TFeatures.POS__NUMERAL, word);
						break;
				}
			}
			else if (flagValueRaw.matches("vietn\\. \"tu\"\\.?"))
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.POS__PERS_PRONOUN},
						"tu");
			else if (flagValueRaw.matches("atgriez\\. vietn\\. \"sevis\"\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.POS__REFL_PRONOUN},
						"sevis");
			}
			else if (flagValueRaw.matches("verbu pavēles izteiksmes formā \\(parasti aiz šīs verba formas\\)\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__IMPERATIVE});
				flagCollector.add(TFeatures.ORIGINAL_NEEDED);
			}
			else if (flagValueRaw.matches("verbu \\(parasti divd\\. formā\\)\\.?"))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.USUALLY,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__PARTICIPLE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__CONDITIONAL});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__DEBITIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__IMPERATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__INDICATIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__INFINITIVE});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[]{TFeatures.POS__VERB, TFeatures.MOOD__RELATIVE});
			}
			else if (flagValueRaw.matches("skait\\. un \\(parasti\\) adj\\. vai apst\\. pārāko pakāpi\\.?"))
			{
				//flagCollector.add(TFeatures.ORIGINAL_NEEDED);
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__NUMERAL, TFeatures.POS__ADJ, TFeatures.DEGREE__COMP});
				restrCollector.addOne(Type.TOGETHER_WITH, TFrequency.RARER,
						new Tuple[]{TFeatures.POS__NUMERAL, TFeatures.POS__ADV, TFeatures.DEGREE__COMP});
			}
			else if (flagValueRaw.matches("jaut\\. part\\. \"vai\", jaut\\. vietn\\. vai jaut\\. apst\\."))
			{
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__PARTICLE, Tuple.of(TKeys.POS, TValues.INTERROGATIVE_PARTICLE)},
						"vai");
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__PRONOUN, TFeatures.POS__INTERROG_PRONOUN});
				restrCollector.addOne(Type.TOGETHER_WITH, restrFreq,
						new Tuple[]{TFeatures.POS__ADV, Tuple.of(TKeys.POS, TValues.INTEROGATIVE_ADVERB)});
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
	 * @param gramText			analizējamais gramatikas teksta fragments
	 * @param restrCollector    kolekcija, kurā pielikt ierobežojumus gadījumā,
	 * 	                    	ja gramatikas fragments atbilst šim likumam
	 * @return	indekss neapstrādātās gramatikas daļas sākumam
	 */
	public static int processInParticipleFormFlag(
			String gramText, StructRestrs restrCollector)
	{
		boolean hasComma = gramText.contains(",");
		Pattern flagPattern = hasComma ?
				Pattern.compile("((parasti |bieži |)divd\\.(?: formā)?: (\\p{Ll}+(, \\p{Ll}+)*))([,].*|\\.)?") :
				Pattern.compile("((parasti |bieži |)divd\\.(?: formā)?: (\\p{Ll}+))\\.?");

		int newBegin = -1;
		//aizelsties->aizelsies, aizelsdamies, aizdzert->aizdzerts
		Matcher m = flagPattern.matcher(gramText);
		if (m.matches())
		{
			String[] forms = m.group(3).split(", ");
			newBegin = m.group(1).length();
			String indicator = m.group(2).trim();
			String restrFreq = TFrequency.UNDISCLOSED;
			if (indicator.equals("parasti"))
				restrFreq = TFrequency.USUALLY;
			else if (indicator.equals("bieži"))
				restrFreq = TFrequency.OFTEN;

			for (String wordForm : forms)
			{
				String partType = RulesAsFunctions.determineParticipleType(wordForm);
				if (partType != null)
				{
					restrCollector.addOne(Type.IN_FORM, restrFreq, new Tuple[] {
							TFeatures.POS__VERB, TFeatures.MOOD__PARTICIPLE,
							Tuple.of(TKeys.MOOD, partType)});
					newBegin = m.group(1).length();

				} else
				{
					restrCollector.addOne(Type.IN_FORM, restrFreq,
							new Tuple[] {TFeatures.POS__VERB, TFeatures.MOOD__PARTICIPLE});
					System.err.printf(
							"Neizdodas ielikt divdabja formu formu \"%s\" uztaisīt kā ierobežojumu\n",
							wordForm);
					newBegin = 0;
					break;
				}
			}
		}
		return newBegin;
	}

}
