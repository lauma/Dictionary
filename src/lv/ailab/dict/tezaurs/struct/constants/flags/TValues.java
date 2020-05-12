package lv.ailab.dict.tezaurs.struct.constants.flags;

import lv.ailab.dict.struct.constants.flags.Values;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Populārākās karodziņu vērtības - iznestas šeit, lai mazinātu pārakstīšanās
 * risku. Vispopulārākās atrodamas virsklasē Values.
 *
 * Visas tekstuālās vērtību konstantes drīkst definēt tikai šeit/Values un
 * AbbrMap klasē - citās klasēs jālieto atsauces, lai kaut ko pamainot, nav
 * jadzenās pakaļ pa daudzām klasēm.
 *
 * @author Lauma
 */
public class TValues extends Values
{
	public final static String POSTPOSITION = "Postpozitīvs prievārds";

	public final static String REFLEXIVE_NOUN = "Atgriezeniskais lietvārds";
	public final static String PROPER = "Īpašvārds";
	public final static String ORDINAL_NUMERAL = "Kārtas skaitļa vārds";
	public final static String CARDINAL_NUMERAL = "Pamata skaitļa vārds";
	public final static String FRACTIONAL_NUMERAL = "Daļu skaitļa vārds";

	// Prievārdu nosaukumi pamatā no LLVV.
	public final static String PERSONAL_PRONOUN = "Personas vietniekvārds";
	public final static String POSSESIVE_PRONOUN = "Piederības vietniekvārds";
	public final static String DEMONSTRATIVE_PRONOUN = "Norādāmais vietniekvārds";
	public final static String DEFINITE_PRONOUN = "Noteiktais vietniekvārds";
	public final static String INDEFINITE_PRONOUN = "Nenoteiktais vietniekvārds";
	public final static String GENRERIC_PRONOUN = "Vispārināmais vietniekvārds";
	public final static String REFLEXIVE_PRONOUN = "Atgriezeniskais vietniekvārds";
	public final static String RELATIVE_PRONOUN = "Attieksmes vietniekvārds";
	public final static String INTERROGATIVE_PRONOUN = "Jautājamais vietniekvārds";
	public final static String NEGATIVE_PRONOUN = "Noliedzamais vietniekvārds";

	public final static String IRREGULAR_VERB = "Nekārtns darbības vārds";
	public final static String DIRECT_VERB = "Tiešs darbības vārds";
	public final static String REFLEXIVE_VERB = "Atgriezenisks darbības vārds";
	public final static String NEGATIVE_VERB = "Noliegts darbības vārds";
	public final static String MAIN_VERB = "Patstāvīgs darbības vārds";

	public final static String PLACE_ADVERB = "Vietas apstākļa vārds";
	public final static String INTEROGATIVE_ADVERB = "Jautājamais apstākļa vārds";

	public final static String INTERROGATIVE_PARTICLE = "Jautājamā partikula";

	public final static String FOREIGN = "Vārds svešvalodā";
	public final static String PIECE_OF_WORD = "Vārda daļa";
	public final static String PREFIX = "Priedēklis";
	/**
	 * Unexpected term, but https://termini.gov.lv/kolekcijas/93/skirklis/422773
	 * says, it is exactly that.
	 */
	public final static String POSTFIX = "Izskaņa";
	public final static String PREFIX_FOREIGN = "Svešvārdu priedēklis";
	public final static String COMPOUND_PIECE = "Salikteņu daļa";
	public final static String COMPOUND_FIRST_PIECE = "Salikteņu pirmā daļa";
	public final static String COMPOUND_LAST_PIECE = "Salikteņu pēdējā daļa";

	public final static String PHRASE = "Vārdu savienojums";
	public final static String COMP_CONSTR = "Salīdzinājuma konstrukcija";

	public final static String PLACE_NAME = "Vietvārds";
	public final static String PERSON_NAME = "Personvārds";
	public final static String HISTORICAL = "Vēsturisks";
	public final static String HISTORICAL_PLACE = "Vēsturisks vietvārds";
	public final static String HISTORICAL_PERSON = "Vēsturisks personvārds";
	public final static String DIALECTICISM = "Dialektisms";
	public final static String REGIONAL_TERM = "Apvidvārds";


	public final static String DUAL = "Divskaitlis";
	public static boolean isNumber(String test)
	{
		return allNumbers.contains(test);
	}
	public final static Set<String> allNumbers =
			Collections.unmodifiableSet(
					new HashSet(){{add(SINGULAR); add(PLURAL); add(DUAL);}});

	public final static String COGENDER = "Kopdzimte";
	public static boolean isGender(String test)
	{
		return allGenders.contains(test);
	}
	public final static Set<String> allGenders =
			Collections.unmodifiableSet(
					new HashSet(){{add(FEMININE); add(MASCULINE); add(COGENDER);}});

	public final static String INSTRUMENTAL = "Instrumentālis";
	public final static String NOPRON_INSTRUMENTAL = "Bezprievārda instrumentālis";
	public final static String VOCATIVE = "Vokatīvs";
	public static boolean isCase(String test)
	{
		return allCases.contains(test);
	}
	public final static Set<String> allCases =
			Collections.unmodifiableSet(
					new HashSet(){{add(NOMINATIVE); add(GENITIVE); add(DATIVE); add(ACUSATIVE); add(LOCATIVE); add(INSTRUMENTAL); add(NOPRON_INSTRUMENTAL); add(VOCATIVE);}});


	public final static String COMPOSITE_TENSES = "Saliktie laiki";

	public final static String PARTICIPLE_OSS = "Lokāmais darāmās kārtas tagadnes divdabis (-ošs, -oša)";
	public final static String PARTICIPLE_IS = "Lokāmais darāmās kārtas pagātnes divdabis (-is, -usi, -ies, -usies)";
	public final static String PARTICIPLE_AMS = "Lokāmais ciešamās kārtas tagadnes divdabis (-ams, -ama, -āms, -āma)";
	public final static String PARTICIPLE_TS = "Lokāmais ciešamās kārtas pagātnes divdabis (-ts, -ta)";
	public final static String PARTICIPLE_DAMS = "Daļēji lokāmais divdabis (-dams, -dama, -damies, -damās)";
	public final static String PARTICIPLE_OT = "Nelokāmais divdabis (-ot, -oties)";

	//public final static String PERSON_FORM = "Personas forma"; // Jebkura persona, lietots kopā ar atslēgku "person" - tipiski darbības vārdiem

	public final static String RETORICAL_QUESTION_SENT = "Retorisks jautājuma teikums";
	//public final static String NEG_VERB_SENT = "Teikums ar noliegtu darbības vārdu";

	public final static String REPETITION = "Atkārtojums";

	public final static String ATTRIBUTE = "Apzīmētājs";

	public final static String ORIGINAL_NEEDED = "Oriģinālā gramatika satur karodziņos neatšifrētas nianses"; // Papildinformācija pārejai notekstuālajām gramatikām uz karodziņiem.

	public final static String CHANGED_PARADIGM = "Cita paradigma"; // Paredzēts morfoimporta atvieglošanai
	public final static String UNCLEAR_PARADIGM = "Neviennozīmīga paradigma"; // Paredzēts morfoimporta atvieglošanai
	public final static String UNCLEAR_POS = "Neviennozīmīga vārdšķira vai kategorija"; // Paredzēts morfoimporta atvieglošanai

	public final static String PARALLEL_FORMS = "Paralēlās formas"; // Paredzēts morfoimporta atvieglošanai, šobrīd pamatā darbības vārdiem
	public final static String FROZEN_FORM = "Sastingusi forma"; // Paredzēts morfoimporta atvieglošanai, cerams, ka pabeidzot darbu ar tēzauru, šis vairs nebūs vajadzīgs
	public final static String FIRST_CONJ_PARALLELFORM = "Pirmās konjugācijas paralēlforma"; // Paredzēts morfoimporta atvieglošanai, tikai 2. un 3. konj. darbības vārdiem
	public final static String SECOND_THIRD_CONJ = "Otrās un trešās konjugācijas paralelitāte"; // Paredzēts morfoimporta atvieglošanai, tikai 2. un 3. konj. darbības vārdiem
	public final static String INFINITIVE_HOMOFORMS = "Nenoteiksmes homoformas"; // Paredzēts morfoimporta atvieglošanai
	//public final static String HAS_PRESENT_SOUNDCHANGE = "Tagadnes mija ir"; // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	//public final static String OPT_PRESENT_SOUNDCHANGE = "Fakultatīva tagadnes mija"; // Paredzēts morfoimporta atvieglošanai, tikai 3. konj.
	//public final static String NO_PRESENT_SOUNDCHANGE = "Nav tagadnes mijas"; // Paredzēts morfoimporta atvieglošanai, tikai 3. konj. Vai šo vajadzētu pārveidot par noklusēto variantu?
	//public final static String NO_SOUNDCHANGE = "Nav mijas"; // Paredzēts morfoimporta atvieglošanai, gadījumiem, kad izņēmuma kārtā vispār nav attiecīgajai paradigmai raksturīgās mijas
	//public final static String OPT_SOUNDCHANGE = "Fakultatīva mija"; // Paredzēts morfoimporta atvieglošanai, gadījumiem, kad izņēmuma kārtā lietojama gan locīšana ar paradigmai raksturīgo miju, gan bez tās

	// Sevišķi retās vērtības:

	//// Galotņu šabloniem.
	//public final static String NO_PRESENT = "Nelieto tagadnes formas";

	//// "Savienojumā ar" apstrādei.
	public final static String ILLNESS_NAME = "Slimības izraisītāja mikroorganisma, arī slimības nosaukums";
	public final static String TIME_UNIT_NAME = "Laika mērvienības nosaukums";
	public final static String CLOTHING_UNIT_NAME = "Apģērba gabala nosaukums";
	public final static String TEACHING_SUBJECT_NAME = "Mācību priekšmeta nosaukums";
	public final static String DIMENSION_NAME = "Dimensiju apzīmējums";
	public final static String MULTI_TINGY_NAME = "Daudzskaitlinieks vai pāra priekšmetu apzīmējums"; // Līdz šim nekur citur nevajag daudzskaitlieku kā vērtību, mēģināšu izšmaukt arī te.
	public final static String PERSON_TERM = "Vārds, kas apzīmē personu"; // Nav tas pats, kas personvārds - šeit iederās arī "cilvēks" un "muļķis".
	public final static String NEGATIVE_PERSON_TERM = "Vārds, kas nosauc personu ar negatīvu īpašību";
	public final static String ADVERBIAL_TERM = "Daudzuma, masas, lieluma vai ilguma apzīmējums";
	public final static String TIME_TERM = "Vārds ar laika nozīmi";
	public final static String DISTRUCTION_NAME = "Vārds, kas apzīmē bojāšanu, iznīcināšanu";
	//public final static String VERB_WITH_DERIVS = "Dotais darbības vārds un tā atvasinājumi";
	public final static String REPETITION_WITH_ONE_STEM = "Atkārtots vienas un tās pašas saknes vārds";
	public final static String DERIVATIVES_OF = "Dotā vārda atvasinājumi";
}
