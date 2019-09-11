package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.tezaurs.struct.constants.flags.TFeatures;
import lv.ailab.dict.tezaurs.struct.constants.flags.TKeys;
import lv.ailab.dict.tezaurs.struct.constants.flags.TValues;
import lv.ailab.dict.utils.MappingSet;
import lv.ailab.dict.utils.Tuple;

import java.util.HashSet;

/**
 * Singletonklase, kas satur visus zināmos saīsinājumus, tos atšifrējumus, kā
 * arī visus citus karodziņus, kas veidojami no fiksētām teksta virknēm.
 *
 * Visas tekstuālās vērtību konstantes drīkst definēt tikai šeit un Values,
 * TValues klasēs - citās klasēs jālieto atsauces uz TValues klasē definētajām,
 * lai kaut ko pamainot, nav jadzenās pakaļ pa daudzām klasēm.
 *
 * @author Lauma
 */
public class AbbrMap {
	protected static AbbrMap singleton;
	protected MappingSet<String, String> binaryFlags;
	protected MappingSet<String, Tuple<String, String>> pairingFlags;

	public static AbbrMap getAbbrMap()
	{
		if (singleton == null) singleton = new AbbrMap();
		return singleton;
	}

	/**
	 * Gramatikas fragmentu (mazāko gabaliņu bez komatiem un semikoliem) mēģina
	 * atpazīt kā saīsinājumu.
	 * @param gramSegment	gramatikas fragments, kas varētu būt karodziņš
	 * @param collector		karodziņu kolekcija, kurā savāc visu, kas atbilst
	 *                      šim gramatikas fragmentam.
	 * @return vai tika atpazīts.
	 */
	public boolean translate(String gramSegment, Flags collector )
	{
		if (binaryFlags.containsKey(gramSegment))
			collector.pairings.putAll(TKeys.OTHER_FLAGS, binaryFlags
					.getAll(gramSegment));
		if (pairingFlags.containsKey(gramSegment))
			for (Tuple<String, String> t : pairingFlags.getAll(gramSegment))
			collector.pairings.put(t.first, t.second);
		return (binaryFlags.containsKey(gramSegment) ||
				pairingFlags.containsKey(gramSegment));
	}

	/**
	 * Atšifrēt gramatikas fragmentu, meklējot tikai vārdšķirās un tikai, ja ir
	 * pieejams tieši viens atšifrējums.
	 * @param gramSegment	gramatikas fragments, kas varētu būt karodziņš
	 * @return	vārdšķira, ja gramatikas fragmentam ir piekārtota tieši viens
	 * 			karodziņš un tas ir POS tipa, vai null pārējos gadījumos
	 */
	public HashSet<String> translatePos(String gramSegment)
	{
		HashSet<Tuple<String, String>> found = pairingFlags.getAll(gramSegment);
		if (found == null) return null;
		HashSet<String> result = new HashSet<>();
		for (Tuple<String, String> feature : found)
			if (feature.first.equals(TKeys.POS))
				result.add(feature.second);
		if (result.size() < 1) return null;
		return result;
	}

	/**
	 * Atšifrēt gramatikas fragmentu, meklējot tikai locījumos un tikai, ja ir
	 * pieejams tieši viens atšifrējums.
	 * @param gramSegment	gramatikas fragments, kas varētu būt karodziņš
	 * @return	vārdšķira, ja gramatikas fragmentam ir piekārtota tieši viens
	 * 			karodziņš un tas ir POS tipa, vai null pārējos gadījumos
	 */
	public String translateCase(String gramSegment)
	{
		HashSet<Tuple<String, String>> found = pairingFlags.getAll(gramSegment);
		if (found == null || found.size() != 1) return null;
		Tuple<String, String> feature = found.iterator().next();
		if (!TValues.isCase(feature.second)) return null;
		return feature.second;
	}

	protected AbbrMap()
	{
		binaryFlags = new MappingSet<>();
		pairingFlags = new MappingSet<>();

		pairingFlags.put("adj.", TFeatures.POS__ADJ);
		pairingFlags.put("adv.", Tuple.of(TKeys.POS, TValues.ADVERB));
		pairingFlags.put("apst.", Tuple.of(TKeys.POS, TValues.ADVERB));
		pairingFlags.put("apstākļa vārds", Tuple.of(TKeys.POS, TValues.ADVERB));
		pairingFlags.put("intr. darb.", TFeatures.POS__VERB);
		pairingFlags.put("darb.", TFeatures.POS__VERB);
		pairingFlags.put("divd.", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("Divd.", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("ģenit.", TFeatures.POS__GEN_ONLY);
		pairingFlags.put("interj.", Tuple.of(TKeys.POS, TValues.INTERJECTION));
		pairingFlags.put("īp. v.", TFeatures.POS__ADJ);
		pairingFlags.put("īp.", TFeatures.POS__ADJ);
		pairingFlags.put("nelok. īp. v.", TFeatures.NON_INFLECTIVE);
		pairingFlags.put("nelok. īp. v.", TFeatures.POS__ADJ);
		pairingFlags.put("īp. v. ar not. galotni", TFeatures.POS__ADJ);
		pairingFlags.put("izsauk.", Tuple.of(TKeys.POS, TValues.INTERJECTION));
		pairingFlags.put("izsauksmes vārds", Tuple.of(TKeys.POS, TValues.INTERJECTION));
		pairingFlags.put("lietv.", TFeatures.POS__NOUN);
		pairingFlags.put("subst.", TFeatures.POS__NOUN);
		pairingFlags.put("part.", TFeatures.POS__PARTICLE);
		pairingFlags.put("partikula", TFeatures.POS__PARTICLE);
		pairingFlags.put("prep.", TFeatures.POS__ADPOSITION);
		pairingFlags.put("priev.", TFeatures.POS__ADPOSITION);
		pairingFlags.put("saiklis.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("konj.", Tuple.of(TKeys.POS, TValues.CONJUNCTION));
		pairingFlags.put("skait.", TFeatures.POS__NUMERAL);
		pairingFlags.put("daļu skait.", TFeatures.POS__FRACT_NUMERAL);
		pairingFlags.put("pamata skait.", TFeatures.POS__CARD_NUMERAL);
		pairingFlags.put("kārtas skait.", TFeatures.POS__ORD_NUMERAL);
		pairingFlags.put("saīs.", TFeatures.POS__ABBR);
		pairingFlags.put("simb.", TFeatures.POS__ABBR);
		pairingFlags.put("refl. darb.", TFeatures.POS__REFL_VERB);
		pairingFlags.put("refl. darb.", TFeatures.POS__VERB);

		pairingFlags.put("dsk. ģen. īp. v. nozīmē.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("dsk. ģen. īp. v. nozīmē.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("dsk. ģen. īp. v. nozīmē.", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("dsk. ģen. īp. v. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("divd. īp. nozīmē", TFeatures.POS__VERB);
		pairingFlags.put("divd. īp. nozīmē", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("divd. īp. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("divd. īp. nozīmē.", TFeatures.POS__VERB);
		pairingFlags.put("divd. īp. nozīmē.", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("divd. īp. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("divd. īp. v. nozīmē", TFeatures.POS__VERB);
		pairingFlags.put("divd. īp. v. nozīmē", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("divd. īp. v. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("divd. īp. v. nozīmē.", TFeatures.POS__VERB);
		pairingFlags.put("divd. īp. v. nozīmē.", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("divd. īp. v. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));

		pairingFlags.put("pron.", TFeatures.POS__PRONOUN);
		pairingFlags.put("vietn.", TFeatures.POS__PRONOUN);
		pairingFlags.put("vietniekv.", TFeatures.POS__PRONOUN);
		pairingFlags.put("atgriez. vietn.", TFeatures.POS__REFL_PRONOUN);
		pairingFlags.put("attieksmes vietn.", TFeatures.POS__PRONOUN);
		pairingFlags.put("attieksmes vietn.", TFeatures.POS__REL_PRONOUN);
		pairingFlags.put("jaut.", TFeatures.POS__INTERROG_PRONOUN);
		pairingFlags.put("jaut. vietn.", TFeatures.POS__INTERROG_PRONOUN);
		pairingFlags.put("nenot. vietn.", TFeatures.POS__INDEF_PRONOUN);
		pairingFlags.put("nenoteiktais vietn.", TFeatures.POS__INDEF_PRONOUN);
		pairingFlags.put("noliedz.", TFeatures.POS__NEG_PRONOUN);
		pairingFlags.put("noliedz. vietn.", TFeatures.POS__NEG_PRONOUN);
		pairingFlags.put("norād.", TFeatures.POS__DEM_PRONOUN);
		pairingFlags.put("norād. vietn.", TFeatures.POS__DEM_PRONOUN);
		pairingFlags.put("norādāmais pron.", TFeatures.POS__DEM_PRONOUN);
		pairingFlags.put("norādāmais vietniekvārds", TFeatures.POS__DEM_PRONOUN);
		pairingFlags.put("noteic.", TFeatures.POS__DEF_PRONOUN);
		pairingFlags.put("noteic. vietn.", TFeatures.POS__DEF_PRONOUN);
		pairingFlags.put("pers. vietn.", TFeatures.POS__PERS_PRONOUN);
		pairingFlags.put("pieder.", TFeatures.POS__POSS_PRONOUN);
		pairingFlags.put("pieder. vietn.", TFeatures.POS__POSS_PRONOUN);
		pairingFlags.put("pers. vietn.", TFeatures.POS__PERS_PRONOUN);
		pairingFlags.put("vispārin.", TFeatures.POS__GEN_PRONOUN);
		pairingFlags.put("vispārin. vietn.", TFeatures.POS__GEN_PRONOUN);

		pairingFlags.put("pried.", TFeatures.POS__PREFIX);
		pairingFlags.put("pried.", TFeatures.POS__PIECE);
		pairingFlags.put("priedēklis", TFeatures.POS__PREFIX);
		pairingFlags.put("priedēklis", TFeatures.POS__PIECE);
		pairingFlags.put("svešvārdu pried.", Tuple.of(TKeys.POS, TValues.PREFIX_FOREIGN));
		pairingFlags.put("svešvārdu pried.", TFeatures.POS__PREFIX);
		pairingFlags.put("svešvārdu pried.", TFeatures.POS__PIECE);

		pairingFlags.put("salikteņu otrā daļa", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņu otrā daļa", TFeatures.POS__COMPOUND_LAST);
		pairingFlags.put("salikteņu otrā daļa", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņu pirmā daļa.", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņu pirmā daļa.", TFeatures.POS__COMPOUND_FIRST);
		pairingFlags.put("salikteņu pirmā daļa.", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņu pirmā daļa", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņu pirmā daļa", TFeatures.POS__COMPOUND_FIRST);
		pairingFlags.put("salikteņu pirmā daļa", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņa pirmā daļa.", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņa pirmā daļa.", TFeatures.POS__COMPOUND_FIRST);
		pairingFlags.put("salikteņa pirmā daļa.", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņa pirmā daļa", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņa pirmā daļa", TFeatures.POS__COMPOUND_FIRST);
		pairingFlags.put("salikteņa pirmā daļa", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņa 1. daļa", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņa 1. daļa", TFeatures.POS__COMPOUND_FIRST);
		pairingFlags.put("salikteņa 1. daļa", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņu daļa.", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņu daļa.", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņu daļa", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņu daļa", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņa daļa", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņa daļa", TFeatures.POS__PIECE);
		pairingFlags.put("salikteņos", TFeatures.POS__COMPOUND_PIECE);
		pairingFlags.put("salikteņos", TFeatures.POS__PIECE);

		pairingFlags.put("vārdu daļa", TFeatures.POS__PIECE);

		pairingFlags.put("pāru saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pāru saikļa sastāvdaļa"));
		pairingFlags.put("pakārtojuma saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pakārtojuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("pakārtojuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pakārtojuma saikļa sastāvdaļa"));
		pairingFlags.put("pieļāvuma saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pieļāvuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("pieļāvuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pieļāvuma saikļa sastāvdaļa"));
		pairingFlags.put("pretstatījuma saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pretstatījuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("pretstatījuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pretstatījuma saikļa sastāvdaļa"));
		pairingFlags.put("pretstatījuma pāru saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pretstatījuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("pretstatījuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pāru saikļa sastāvdaļa"));
		pairingFlags.put("pretstatījuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pretstatījuma saikļa sastāvdaļa"));
		pairingFlags.put("pretstatījuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pretstatījuma pāru saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma saikļa sastāvdaļa", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Vienojuma saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Vienojuma saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Pāru saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Vienojuma saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Vienojuma pāru saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pāru saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Vienojuma saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma pāru saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Vienojuma pāru saikļa sastāvdaļa"));

		pairingFlags.put("pakārtojuma saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pakārtojuma saiklis", Tuple.of(TKeys.POS, "Pakārtojuma saiklis"));
		pairingFlags.put("pakārtojuma saiklis.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pakārtojuma saiklis.", Tuple.of(TKeys.POS, "Pakārtojuma saiklis"));
		pairingFlags.put("paskaidrojuma saiklis.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("paskaidrojuma saiklis.", Tuple.of(TKeys.POS, "Paskaidrojuma saiklis"));
		pairingFlags.put("pieļāvuma saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pieļāvuma saiklis", Tuple.of(TKeys.POS, "Pieļāvuma saiklis"));
		pairingFlags.put("pretstatījuma saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pretstatījuma saiklis", Tuple.of(TKeys.POS, "Pretstatījuma saiklis"));
		pairingFlags.put("pretstatījuma saiklis.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pretstatījuma saiklis.", Tuple.of(TKeys.POS, "Pretstatījuma saiklis"));
		pairingFlags.put("sakārtojuma saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("sakārtojuma saiklis", Tuple.of(TKeys.POS, "Sakartojuma saiklis"));
		pairingFlags.put("šķīruma saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("šķīruma saiklis", Tuple.of(TKeys.POS, "Šķīruma saiklis"));
		pairingFlags.put("sakārtojuma (šķīruma) saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("sakārtojuma (šķīruma) saiklis", Tuple.of(TKeys.POS, "Šķīruma saiklis"));
		pairingFlags.put("sakārtojuma (šķīruma) saiklis", Tuple.of(TKeys.POS, "Sakārtojuma saiklis"));
		pairingFlags.put("sakārtojuma (šķīruma) saiklis.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("sakārtojuma (šķīruma) saiklis.", Tuple.of(TKeys.POS, "Šķīruma saiklis"));
		pairingFlags.put("sakārtojuma (šķīruma) saiklis.", Tuple.of(TKeys.POS, "Sakārtojuma saiklis"));
		pairingFlags.put("vienojuma saiklis", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma saiklis", Tuple.of(TKeys.POS, "Vienojuma saiklis"));
		pairingFlags.put("vienojuma saiklis.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma saiklis.", Tuple.of(TKeys.POS, "Vienojuma saiklis"));

		pairingFlags.put("priev. ar akuz.", TFeatures.POS__ADPOSITION);
		pairingFlags.put("priev. ar akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.ACUSATIVE));
		pairingFlags.put("priev. ar ģen.", TFeatures.POS__ADPOSITION);
		pairingFlags.put("priev. ar ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("priev. ar ģen. vai dat.",TFeatures.POS__ADPOSITION);
		pairingFlags.put("priev. ar ģen. vai dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("priev. ar ģen. vai dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("priev. (aiz pārvaldāmā vārda) ar vsk. vai dsk. ģen.", TFeatures.POS__ADPOSITION);
		pairingFlags.put("priev. (aiz pārvaldāmā vārda) ar vsk. vai dsk. ģen.", TFeatures.POS__POSTPOSITION);
		pairingFlags.put("priev. (aiz pārvaldāmā vārda) ar vsk. vai dsk. ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("priev. ar dat.", TFeatures.POS__ADPOSITION);
		pairingFlags.put("priev. ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("priev. ar instr.", TFeatures.POS__ADPOSITION);
		pairingFlags.put("priev. ar instr.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.INSTRUMENTAL));

		pairingFlags.put("persv.", TFeatures.POS__NOUN);
		binaryFlags.put("persv.", TValues.PERSON_NAME);
		//pairingFlags.put("vietv.", TFeatures.POS__NOUN); // jāuzmanās ar mazciemu "Siltais"
														// Ko darīt ar sastingušajām formām?
		binaryFlags.put("vietv.", "Vietvārds");

		binaryFlags.put("nelok.", TValues.NON_INFLECTIVE);
		binaryFlags.put("lokāms.", "Lokāms vārds");
		binaryFlags.put("lokāms", "Lokāms vārds");

		pairingFlags.put("ģen. nelok.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		binaryFlags.put("ģen. nelok.", TValues.NON_INFLECTIVE);
		pairingFlags.put("ģen. nelok. īp. nozīmē.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen. nelok. īp. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		binaryFlags.put("ģen. nelok. īp. nozīmē.", TValues.NON_INFLECTIVE);
		pairingFlags.put("ģen. nelok. īp. nozīmē", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen. nelok. īp. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		binaryFlags.put("ģen. nelok. īp. nozīmē", TValues.NON_INFLECTIVE);

		/*binaryFlags.put("nāk.", "Nākotne");
		binaryFlags.put("pag.", "Pagātne");
		binaryFlags.put("tag.", "Tagadne");
		
		binaryFlags.put("nenot.", "Nenoteiktā galotne");
		binaryFlags.put("not.", "Noteiktā galotne");*/
		
		pairingFlags.put("s.", TFeatures.GENDER__FEM);
		pairingFlags.put("v.", TFeatures.GENDER__MASC);
		pairingFlags.put("v. un s.", Tuple.of(TKeys.GENDER, TValues.COGENDER));
		//pairingFlags.put("v. un s.", TFeatures.GENDER__FEM);
		//pairingFlags.put("v. un s.", TFeatures.GENDER__MASC);
		pairingFlags.put("kopdz.", Tuple.of(TKeys.GENDER, TValues.COGENDER));
		
		binaryFlags.put("intrans.", "Nepārejošs");
		binaryFlags.put("intr.", "Nepārejošs");
		binaryFlags.put("nepārej.", "Nepārejošs");
		binaryFlags.put("intr. darb.", "Nepārejošs");
		binaryFlags.put("trans.", "Pārejošs");
		binaryFlags.put("pārej.", "Pārejošs");
		binaryFlags.put("tr.", "Pārejošs");
		binaryFlags.put("tr. darb.", "Pārejošs");
		binaryFlags.put("tr. un intr. darb. v.", "Pārejošs");
		binaryFlags.put("tr. un intr. darb. v.", "Nepārejošs");
		binaryFlags.put("tr. un intr. darb. v.", "Pārejošs un nepārejošs");
		binaryFlags.put("arī intrans.", "Nepārejošs");
		binaryFlags.put("arī intrans.", "Pārejošs un nepārejošs");
		binaryFlags.put("arī trans.", "Pārejošs");
		binaryFlags.put("arī trans.", "Pārejošs un nepārejošs");

		/*binaryFlags.put("konj.", "Konjugācija");
		binaryFlags.put("pers.", "Persona");*/

		binaryFlags.put("dem.", "Deminutīvs");
		binaryFlags.put("Dem.", "Deminutīvs");
		binaryFlags.put("imperf.", "Imperfektīva forma"); //???
		//pairingFlags.put("imperf.", TFeatures.POS__VERB); // Kas ir ar divdabjiem?
		binaryFlags.put("Nol.", "Noliegums"); // Check with other sources!
		binaryFlags.put("refl.", "Refleksīvs");
		pairingFlags.put("refl.", TFeatures.POS__VERB); // Laura sapētīja, ka pie lietvārdiem nav.
		//binaryFlags.put("Refl.", "Refleksīvs");	// šis ir tikai šķirkļos, garmatikās nē, līdz ar to, īsti vajadzīgs nav.
		//pairingFlags.put("Refl.", TFeatures.POS__VERB);

		// joma
		pairingFlags.put("apģ.", Tuple.of(TKeys.DOMAIN, "Apģērbi"));
		pairingFlags.put("aeron.", Tuple.of(TKeys.DOMAIN, "Aeronautika"));
		pairingFlags.put("agr.", Tuple.of(TKeys.DOMAIN, "Lauksaimniecība"));	// Bija Agronomija
		pairingFlags.put("anat.", Tuple.of(TKeys.DOMAIN, "Anatomija"));
		pairingFlags.put("antr.", Tuple.of(TKeys.DOMAIN, "Antropoloģija"));
		pairingFlags.put("antrop.", Tuple.of(TKeys.DOMAIN, "Antropoloģija"));
		pairingFlags.put("arheol.", Tuple.of(TKeys.DOMAIN, "Arheoloģija"));
		pairingFlags.put("arhit.", Tuple.of(TKeys.DOMAIN, "Arhitektūra"));
		pairingFlags.put("arh.", Tuple.of(TKeys.DOMAIN, "Arhitektūra"));
		pairingFlags.put("astr.", Tuple.of(TKeys.DOMAIN, "Astronomija"));
		pairingFlags.put("astron.", Tuple.of(TKeys.DOMAIN, "Astronomija"));
		pairingFlags.put("av.", Tuple.of(TKeys.DOMAIN, "Aviācija"));
		pairingFlags.put("ādas apstr.", Tuple.of(TKeys.DOMAIN, "Ādas apstrāde"));
		pairingFlags.put("ādu apstr.", Tuple.of(TKeys.DOMAIN, "Ādas apstrāde"));
		pairingFlags.put("bioķīm.", Tuple.of(TKeys.DOMAIN, "Bioķīmija"));
		pairingFlags.put("biol.", Tuple.of(TKeys.DOMAIN, "Bioloģija"));
		pairingFlags.put("bišk.", Tuple.of(TKeys.DOMAIN, "Biškopība"));
		pairingFlags.put("biškop.", Tuple.of(TKeys.DOMAIN, "Biškopība"));
		pairingFlags.put("bot.", Tuple.of(TKeys.DOMAIN, "Botānika"));
		pairingFlags.put("bur.", Tuple.of(TKeys.DOMAIN, "Burāšana"));
		pairingFlags.put("bur. sl.", Tuple.of(TKeys.DOMAIN, "Burāšana"));
		pairingFlags.put("būvn.", Tuple.of(TKeys.DOMAIN, "Būvniecība"));
		pairingFlags.put("celtn.", Tuple.of(TKeys.DOMAIN, "Celtniecība"));
		pairingFlags.put("dārzk.", Tuple.of(TKeys.DOMAIN, "Dārzkopība"));
		pairingFlags.put("dzelzc.", Tuple.of(TKeys.DOMAIN, "Dzelzceļš"));
		pairingFlags.put("dzc.", Tuple.of(TKeys.DOMAIN, "Dzelzceļš"));
		pairingFlags.put("ek.", Tuple.of(TKeys.DOMAIN, "Ekonomika"));
		pairingFlags.put("ekol.", Tuple.of(TKeys.DOMAIN, "Ekoloģija"));
		pairingFlags.put("ekon.", Tuple.of(TKeys.DOMAIN, "Ekonomika"));
		pairingFlags.put("el.", Tuple.of(TKeys.DOMAIN, "Elektrotehnika"));
		pairingFlags.put("elektron.", Tuple.of(TKeys.DOMAIN, "Elektronika"));
		pairingFlags.put("enerģ.", Tuple.of(TKeys.DOMAIN, "Enerģētika"));
		pairingFlags.put("ent.", Tuple.of(TKeys.DOMAIN, "Entomoloģija"));
		pairingFlags.put("etn.", Tuple.of(TKeys.DOMAIN, "Etnogrāfija"));
		pairingFlags.put("farm.", Tuple.of(TKeys.DOMAIN, "Farmakoloģija"));
		pairingFlags.put("filol.", Tuple.of(TKeys.DOMAIN, "Filoloģija"));
		pairingFlags.put("filoz.", Tuple.of(TKeys.DOMAIN, "Filozofija"));
		pairingFlags.put("fin.", Tuple.of(TKeys.DOMAIN, "Finanses"));
		pairingFlags.put("fiz.", Tuple.of(TKeys.DOMAIN, "Fizika"));
		pairingFlags.put("fiziol.", Tuple.of(TKeys.DOMAIN, "Fizioloģija"));
		pairingFlags.put("fizk.", Tuple.of(TKeys.DOMAIN, "Fiziskā kultūra un sports"));
		pairingFlags.put("folkl.", Tuple.of(TKeys.DOMAIN, "Folklora"));
		pairingFlags.put("parasti folkl.", Tuple.of(TKeys.DOMAIN, "Folklora"));
		pairingFlags.put("fot.", Tuple.of(TKeys.DOMAIN, "Fototehnika"));
		pairingFlags.put("fotogr.", Tuple.of(TKeys.DOMAIN, "Fotogrāfija"));
		pairingFlags.put("foto", Tuple.of(TKeys.DOMAIN, "Fotogrāfija"));
		pairingFlags.put("glezn.", Tuple.of(TKeys.DOMAIN, "Glezniecība"));
		pairingFlags.put("ģenēt.", Tuple.of(TKeys.DOMAIN, "Ģenētika"));
		pairingFlags.put("ģeod.", Tuple.of(TKeys.DOMAIN, "Ģeodēzija"));
		pairingFlags.put("ģeogr.", Tuple.of(TKeys.DOMAIN, "Ģeogrāfija"));
		pairingFlags.put("ģeol.", Tuple.of(TKeys.DOMAIN, "Ģeoloģija"));
		pairingFlags.put("ģeom.", Tuple.of(TKeys.DOMAIN, "Ģeometrija"));
		pairingFlags.put("grāmatv.", Tuple.of(TKeys.DOMAIN, "Grāmatvedība"));
		pairingFlags.put("herald.", Tuple.of(TKeys.DOMAIN, "Heraldika"));
		pairingFlags.put("hidr.", Tuple.of(TKeys.DOMAIN, "Hidroloģija"));
		pairingFlags.put("hidrotehn.", Tuple.of(TKeys.DOMAIN, "Hidrotehnika"));
		pairingFlags.put("hist.", Tuple.of(TKeys.DOMAIN, "Historiogrāfija"));
		pairingFlags.put("inf.", Tuple.of(TKeys.DOMAIN, "Informātika"));
		pairingFlags.put("jlat.", Tuple.of(TKeys.DOMAIN, "Jaunlatīņu literatūra"));
		pairingFlags.put("jur.", Tuple.of(TKeys.DOMAIN, "Jurisprudence"));
		pairingFlags.put("jūrn.", Tuple.of(TKeys.DOMAIN, "Jūrniecība"));
		pairingFlags.put("jūrn. sl.", Tuple.of(TKeys.DOMAIN, "Jūrniecība"));
		pairingFlags.put("jūr.", Tuple.of(TKeys.DOMAIN, "Jūrniecība"));
		pairingFlags.put("kalnr.", Tuple.of(TKeys.DOMAIN, "Kalnrūpniecība"));
		pairingFlags.put("kap.", Tuple.of(TKeys.DOMAIN, "Attiecas uz kapitālistisko iekārtu, kapitālistisko sabiedrību"));
		pairingFlags.put("kardioloģijā", Tuple.of(TKeys.DOMAIN, "Kardioloģija"));
		pairingFlags.put("kart.", Tuple.of(TKeys.DOMAIN, "Kartogrāfija"));
		pairingFlags.put("ker.", Tuple.of(TKeys.DOMAIN, "Keramika"));
		pairingFlags.put("kibern.", Tuple.of(TKeys.DOMAIN, "Kibernētika"));
		pairingFlags.put("kino", Tuple.of(TKeys.DOMAIN, "Kinematogrāfija"));
		pairingFlags.put("kino.", Tuple.of(TKeys.DOMAIN, "Kinematogrāfija"));
		pairingFlags.put("kokapstr.", Tuple.of(TKeys.DOMAIN, "Kokapstrāde"));
		pairingFlags.put("kul.", Tuple.of(TKeys.DOMAIN, "Kulinārija"));
		pairingFlags.put("ķīm.", Tuple.of(TKeys.DOMAIN, "Ķīmija"));
		pairingFlags.put("ķīm. tehnol.", Tuple.of(TKeys.DOMAIN, "Ķīmija"));
		pairingFlags.put("ķīm. tehnol.", Tuple.of(TKeys.DOMAIN, "Tehnoloģija"));
		pairingFlags.put("ķīm. tehnol.", Tuple.of(TKeys.DOMAIN, "Ķīmijas tehnoloģija"));
		pairingFlags.put("lauks.", Tuple.of(TKeys.DOMAIN, "Lauksaimniecība"));
		pairingFlags.put("lauks. tehn.", Tuple.of(TKeys.DOMAIN, "Lauksaimniecības tehnika"));
		// TODO: iziet cauri, kas ar to tagots, vai nav daļa jāpārceļ uz lietojuma ierobežojumiem.
		pairingFlags.put("lingv.", Tuple.of(TKeys.DOMAIN, "Valodniecība"));
		pairingFlags.put("lit.", Tuple.of(TKeys.DOMAIN, "Literatūra"));
		pairingFlags.put("literat.", Tuple.of(TKeys.DOMAIN, "Literatūrzinātne"));
		pairingFlags.put("loģ.", Tuple.of(TKeys.DOMAIN, "Loģika"));
		pairingFlags.put("lopk.", Tuple.of(TKeys.DOMAIN, "Lopkopība"));
		pairingFlags.put("mat.", Tuple.of(TKeys.DOMAIN, "Matemātika"));
		pairingFlags.put("matem.", Tuple.of(TKeys.DOMAIN, "Matemātika"));
		pairingFlags.put("materiālt.", Tuple.of(TKeys.DOMAIN, "Materiālzinātne"));
		pairingFlags.put("med.", Tuple.of(TKeys.DOMAIN, "Medicīna"));
		pairingFlags.put("medn.", Tuple.of(TKeys.DOMAIN, "Medniecība"));
		pairingFlags.put("meh.", Tuple.of(TKeys.DOMAIN, "Mehānika"));
		pairingFlags.put("met.", Tuple.of(TKeys.DOMAIN, "Meteoroloģija"));
		pairingFlags.put("metal.", Tuple.of(TKeys.DOMAIN, "Metalurģija"));
		pairingFlags.put("metāl.", Tuple.of(TKeys.DOMAIN, "Metālapstrāde"));
		pairingFlags.put("meteorol.", Tuple.of(TKeys.DOMAIN, "Meteoroloģija"));
		pairingFlags.put("metr.", Tuple.of(TKeys.DOMAIN, "Metroloģija"));
		pairingFlags.put("mež.", Tuple.of(TKeys.DOMAIN, "Mežniecība"));
		pairingFlags.put("mežr.", Tuple.of(TKeys.DOMAIN, "Mežrūpniecība"));
		pairingFlags.put("mežs.", Tuple.of(TKeys.DOMAIN, "Mežsaimniecība"));
		pairingFlags.put("mēb.", Tuple.of(TKeys.DOMAIN, "Mēbeles"));
		pairingFlags.put("mil.", Tuple.of(TKeys.DOMAIN, "Militārās zinātnes"));
		pairingFlags.put("min.", Tuple.of(TKeys.DOMAIN, "Mineraloģija"));
		pairingFlags.put("mit.", Tuple.of(TKeys.DOMAIN, "Mitoloģija"));
		pairingFlags.put("mitoloģijā", Tuple.of(TKeys.DOMAIN, "Mitoloģija"));
		pairingFlags.put("mūz.", Tuple.of(TKeys.DOMAIN, "Mūzika"));
		pairingFlags.put("oftalmoloģijā", Tuple.of(TKeys.DOMAIN, "Oftalmoloģija"));
		pairingFlags.put("okean.", Tuple.of(TKeys.DOMAIN, "Okeanoloģija"));
		pairingFlags.put("optikā", Tuple.of(TKeys.DOMAIN, "Optika"));
		pairingFlags.put("ornit.", Tuple.of(TKeys.DOMAIN, "Ornitoloģija"));
		pairingFlags.put("papīrr.", Tuple.of(TKeys.DOMAIN, "Papīrrūpniecība"));
		pairingFlags.put("pārt.", Tuple.of(TKeys.DOMAIN, "Pārtika"));
		pairingFlags.put("pol.", Tuple.of(TKeys.DOMAIN, "Politika"));
		pairingFlags.put("poligr.", Tuple.of(TKeys.DOMAIN, "Poligrāfija"));
		pairingFlags.put("polit.", Tuple.of(TKeys.DOMAIN, "Politika")); // Bija "Politisks"
		pairingFlags.put("psih.", Tuple.of(TKeys.DOMAIN, "Psiholoģija"));
		pairingFlags.put("psihol.", Tuple.of(TKeys.DOMAIN, "Psiholoģija"));
		pairingFlags.put("psihoan.", Tuple.of(TKeys.DOMAIN, "Psihoanalīze"));
		pairingFlags.put("radioelektronikā", Tuple.of(TKeys.DOMAIN, "Radioelektronika"));
		pairingFlags.put("ras.", Tuple.of(TKeys.DOMAIN, "Rasēšana"));
		pairingFlags.put("rel.", Tuple.of(TKeys.DOMAIN, "Reliģija"));
		pairingFlags.put("rūpn.", Tuple.of(TKeys.DOMAIN, "Rūpniecība"));
		pairingFlags.put("sil. tehnol.", Tuple.of(TKeys.DOMAIN, "Silikātu tehnoloģija")); // ?
		pairingFlags.put("silikātu tehnol.", Tuple.of(TKeys.DOMAIN, "Silikātu tehnoloģija")); // ?
		pairingFlags.put("siltumt.", Tuple.of(TKeys.DOMAIN, "Siltumtehnika"));
		pairingFlags.put("social.", Tuple.of(TKeys.DOMAIN, "Socioloģija"));
		pairingFlags.put("sociol.", Tuple.of(TKeys.DOMAIN, "Socioloģija"));
		pairingFlags.put("socioloģijā", Tuple.of(TKeys.DOMAIN, "Socioloģija"));
		pairingFlags.put("sports", Tuple.of(TKeys.DOMAIN, "Sports"));
		pairingFlags.put("stat.", Tuple.of(TKeys.DOMAIN, "Statistika"));
		pairingFlags.put("tehn.", Tuple.of(TKeys.DOMAIN, "Tehnika"));
		pairingFlags.put("tehnol.", Tuple.of(TKeys.DOMAIN, "Tehnoloģija"));
		pairingFlags.put("telek.", Tuple.of(TKeys.DOMAIN, "Telekomunikācijas"));
		pairingFlags.put("telev.", Tuple.of(TKeys.DOMAIN, "Televīzija"));
		pairingFlags.put("tekst.", Tuple.of(TKeys.DOMAIN, "Tekstilrūpniecība"));
		pairingFlags.put("tekstilr.", Tuple.of(TKeys.DOMAIN, "Tekstilrūpniecība"));
		pairingFlags.put("transp.", Tuple.of(TKeys.DOMAIN, "Transports"));
		pairingFlags.put("TV", Tuple.of(TKeys.DOMAIN, "Televīzija"));
		pairingFlags.put("val.", Tuple.of(TKeys.DOMAIN, "Valodniecība"));
		pairingFlags.put("vet.", Tuple.of(TKeys.DOMAIN, "Veterinārija"));
		pairingFlags.put("veter.", Tuple.of(TKeys.DOMAIN, "Veterinārija"));
		pairingFlags.put("zool.", Tuple.of(TKeys.DOMAIN, "Zooloģija"));
		pairingFlags.put("zvejn.", Tuple.of(TKeys.DOMAIN, "Zvejniecība"));

		pairingFlags.put("abhāzu", Tuple.of(TKeys.LANGUAGE, "Abhāzu"));
		pairingFlags.put("abhāzu", TFeatures.POS__FOREIGN);
		pairingFlags.put("ang.", Tuple.of(TKeys.LANGUAGE, "Angļu"));
		pairingFlags.put("ang.", TFeatures.POS__FOREIGN);
		pairingFlags.put("angļu", Tuple.of(TKeys.LANGUAGE, "Angļu"));
		pairingFlags.put("angļu", TFeatures.POS__FOREIGN);
		pairingFlags.put("angļu val.", Tuple.of(TKeys.LANGUAGE, "Angļu"));
		pairingFlags.put("angļu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("arābu", Tuple.of(TKeys.LANGUAGE, "Arābu"));
		pairingFlags.put("arābu", TFeatures.POS__FOREIGN);
		pairingFlags.put("arābu.", Tuple.of(TKeys.LANGUAGE, "Arābu"));
		pairingFlags.put("arābu.", TFeatures.POS__FOREIGN);
		pairingFlags.put("arābu val.", Tuple.of(TKeys.LANGUAGE, "Arābu"));
		pairingFlags.put("arābu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("čigānu val.", Tuple.of(TKeys.LANGUAGE, "Čigānu"));
		pairingFlags.put("čigānu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ebr.", Tuple.of(TKeys.LANGUAGE, "Ebreju"));
		pairingFlags.put("ebr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("farsi val.", Tuple.of(TKeys.LANGUAGE, "Persiešu")); // TODO vai persiešu?
		pairingFlags.put("farsi val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("fr.", Tuple.of(TKeys.LANGUAGE, "Franču"));
		pairingFlags.put("fr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("franču", Tuple.of(TKeys.LANGUAGE, "Franču"));
		pairingFlags.put("franču", TFeatures.POS__FOREIGN);
		pairingFlags.put("grieķu val.", Tuple.of(TKeys.LANGUAGE, "Grieķu"));
		pairingFlags.put("grieķu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("grieķu", Tuple.of(TKeys.LANGUAGE, "Grieķu"));
		pairingFlags.put("grieķu", TFeatures.POS__FOREIGN);
		pairingFlags.put("gr.", Tuple.of(TKeys.LANGUAGE, "Grieķu"));
		pairingFlags.put("gr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("gr. val.", Tuple.of(TKeys.LANGUAGE, "Grieķu"));
		pairingFlags.put("gr. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("gruz.", Tuple.of(TKeys.LANGUAGE, "Gruzīnu"));
		pairingFlags.put("gruz.", TFeatures.POS__FOREIGN);
		pairingFlags.put("gruzīnu val.", Tuple.of(TKeys.LANGUAGE, "Gruzīnu"));
		pairingFlags.put("gruzīnu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("hindi", Tuple.of(TKeys.LANGUAGE, "Hindi"));
		pairingFlags.put("hindi", TFeatures.POS__FOREIGN);
		pairingFlags.put("holandiešu val.", Tuple.of(TKeys.LANGUAGE, "Nīderlandiešu"));
		pairingFlags.put("holandiešu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ig.", Tuple.of(TKeys.LANGUAGE, "Igauņu"));
		pairingFlags.put("ig.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ig. val.", Tuple.of(TKeys.LANGUAGE, "Igauņu"));
		pairingFlags.put("ig. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("igauniski", Tuple.of(TKeys.LANGUAGE, "Igauņu"));
		pairingFlags.put("igauniski", TFeatures.POS__FOREIGN);
		pairingFlags.put("indiāņu valodās", Tuple.of(TKeys.LANGUAGE, "Indiāņu valodu saime"));
		pairingFlags.put("indiāņu valodās", TFeatures.POS__FOREIGN);
		pairingFlags.put("it.", Tuple.of(TKeys.LANGUAGE, "Itāļu"));
		pairingFlags.put("it.", TFeatures.POS__FOREIGN);
		pairingFlags.put("it. val.", Tuple.of(TKeys.LANGUAGE, "Itāļu"));
		pairingFlags.put("it. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("itāļu", Tuple.of(TKeys.LANGUAGE, "Itāļu"));
		pairingFlags.put("itāļu", TFeatures.POS__FOREIGN);
		pairingFlags.put("jap.", Tuple.of(TKeys.LANGUAGE, "Japāņu"));
		pairingFlags.put("jap.", TFeatures.POS__FOREIGN);
		pairingFlags.put("japāņu", Tuple.of(TKeys.LANGUAGE, "Japāņu"));
		pairingFlags.put("japāņu", TFeatures.POS__FOREIGN);
		pairingFlags.put("jap. val.", Tuple.of(TKeys.LANGUAGE, "Japāņu"));
		pairingFlags.put("jap. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("kirgīzu val.", Tuple.of(TKeys.LANGUAGE, "Krievu"));
		pairingFlags.put("kirgīzu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("krievu val.", Tuple.of(TKeys.LANGUAGE, "Krievu"));
		pairingFlags.put("krievu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("kr.", Tuple.of(TKeys.LANGUAGE, "Krievu"));
		pairingFlags.put("kr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ķīn.", Tuple.of(TKeys.LANGUAGE, "Ķīniešu"));
		pairingFlags.put("ķīn.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ķīn. val.", Tuple.of(TKeys.LANGUAGE, "Ķīniešu"));
		pairingFlags.put("ķīn. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("lat.", Tuple.of(TKeys.LANGUAGE, "Latīņu"));
		pairingFlags.put("lat.", TFeatures.POS__FOREIGN);
		pairingFlags.put("latīņu", Tuple.of(TKeys.LANGUAGE, "Latīņu"));
		pairingFlags.put("latīņu", TFeatures.POS__FOREIGN);
		pairingFlags.put("latīņu val.", Tuple.of(TKeys.LANGUAGE, "Latīņu"));
		pairingFlags.put("latīņu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("liet.", Tuple.of(TKeys.LANGUAGE, "Lietuviešu"));
		pairingFlags.put("liet.", TFeatures.POS__FOREIGN);
		pairingFlags.put("lietuv. val.", Tuple.of(TKeys.LANGUAGE, "Lietuviešu"));
		pairingFlags.put("lietuv. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("līb.", Tuple.of(TKeys.LANGUAGE, "Lībiešu"));
		pairingFlags.put("līb.", TFeatures.POS__FOREIGN);
		pairingFlags.put("lībiešu val.", Tuple.of(TKeys.LANGUAGE, "Lībiešu"));
		pairingFlags.put("lībiešu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("mong.", Tuple.of(TKeys.LANGUAGE, "Mongoļu"));
		pairingFlags.put("mong.", TFeatures.POS__FOREIGN);
		pairingFlags.put("norv. val.",  Tuple.of(TKeys.LANGUAGE, "Norvēģu"));
		pairingFlags.put("norv. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("polinēziešu", Tuple.of(TKeys.LANGUAGE, "Polinēziešu"));
		pairingFlags.put("polinēziešu", TFeatures.POS__FOREIGN);
		pairingFlags.put("poļu", Tuple.of(TKeys.LANGUAGE, "Poļu"));
		pairingFlags.put("poļu", TFeatures.POS__FOREIGN);
		pairingFlags.put("poļu val.", Tuple.of(TKeys.LANGUAGE, "Poļu"));
		pairingFlags.put("poļu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("port.", Tuple.of(TKeys.LANGUAGE, "Portugāļu"));
		pairingFlags.put("port.", TFeatures.POS__FOREIGN);
		pairingFlags.put("portug.", Tuple.of(TKeys.LANGUAGE, "Portugāļu"));
		pairingFlags.put("portug.", TFeatures.POS__FOREIGN);
		pairingFlags.put("prūšu val.", Tuple.of(TKeys.LANGUAGE, "Prūšu"));
		pairingFlags.put("prūšu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("sanskr.", Tuple.of(TKeys.LANGUAGE, "Sanskrits"));
		pairingFlags.put("sanskr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("no sanskrita", Tuple.of(TKeys.LANGUAGE, "Sanskrits")); // TODO?
		pairingFlags.put("no sanskrita", TFeatures.POS__FOREIGN);
		pairingFlags.put("senebr.", Tuple.of(TKeys.LANGUAGE, "Senebreju"));
		pairingFlags.put("senebr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("sengr.", Tuple.of(TKeys.LANGUAGE, "Sengrieķu"));
		pairingFlags.put("sengr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("senpersiešu val.", Tuple.of(TKeys.LANGUAGE, "Senpersiešu"));
		pairingFlags.put("senpersiešu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("sp. val.", Tuple.of(TKeys.LANGUAGE, "Spāņu"));
		pairingFlags.put("sp. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("sp.", Tuple.of(TKeys.LANGUAGE, "Spāņu"));
		pairingFlags.put("sp.", TFeatures.POS__FOREIGN);
		pairingFlags.put("spāņu", Tuple.of(TKeys.LANGUAGE, "Spāņu"));
		pairingFlags.put("spāņu", TFeatures.POS__FOREIGN);
		pairingFlags.put("turku val.", Tuple.of(TKeys.LANGUAGE, "Turku"));
		pairingFlags.put("turku val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("turku", Tuple.of(TKeys.LANGUAGE, "Turku"));
		pairingFlags.put("turku", TFeatures.POS__FOREIGN);
		pairingFlags.put("ukr.", Tuple.of(TKeys.LANGUAGE, "Ukraiņu"));
		pairingFlags.put("ukr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ung.", Tuple.of(TKeys.LANGUAGE, "Ungāru"));
		pairingFlags.put("ung.", TFeatures.POS__FOREIGN);
		pairingFlags.put("vācu val.", Tuple.of(TKeys.LANGUAGE, "Vācu"));
		pairingFlags.put("vācu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("vācu", Tuple.of(TKeys.LANGUAGE, "Vācu"));
		pairingFlags.put("vācu", TFeatures.POS__FOREIGN);
		pairingFlags.put("velsiešu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("velsiešu val.", Tuple.of(TKeys.LANGUAGE, "Velsiešu"));
		pairingFlags.put("zv.", Tuple.of(TKeys.LANGUAGE, "Zviedru"));
		pairingFlags.put("zv.", TFeatures.POS__FOREIGN);
		pairingFlags.put("zviedr.", Tuple.of(TKeys.LANGUAGE, "Zviedru"));
		pairingFlags.put("zviedr.", TFeatures.POS__FOREIGN);

		// Dialekti un izloksnes (tie ir arī lietojuma ierobežojumi)
		//2016-01-06 ir konstatēts, ka šis ir pazudis - izlabots par apv.
		//pairingFlags.put("dial. (augšzemnieku)", Tuple.of(TKeys.DIALECT_FEATURES, "Agušzemnieku"));	// Unikāls.
		//pairingFlags.put("dial. (augšzemnieku)", TFeatures.USAGE_RESTR__DIALECTICISM);	// Unikāls.
		pairingFlags.put("latg.", Tuple.of(TKeys.DIALECT_FEATURES, "Latgaliešu"));
		pairingFlags.put("latg.", TFeatures.USAGE_RESTR__DIALECTICISM);
		pairingFlags.put("tāmnieku", Tuple.of(TKeys.DIALECT_FEATURES, "Tāmnieku"));
		pairingFlags.put("tāmnieku", TFeatures.USAGE_RESTR__DIALECTICISM);

		// Lietojuma ierobežojums
		pairingFlags.put("bērnu val.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("bērnu valodā", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("arī bērnu valodā", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("parasti bērnu val.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("sar. ar bērniem", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("sarunā ar bērniem", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("sarunās ar bērniem", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("parasti sar. ar bērniem", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("parasti sar. ar bērniem.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("parasti sarunā ar bērnu", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("parasti sarunā ar bērniem", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("apv.", TFeatures.USAGE_RESTR__REGIONAL);
		pairingFlags.put("vēst.", Tuple.of(TKeys.USAGE_RESTRICTIONS, TValues.HISTORICAL));
		// TODO vēst. laikam ir domēns nevis ierobežojums!!!!!!!!!
		pairingFlags.put("novec.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Novecojis"));
		pairingFlags.put("neakt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Neaktuāls")); // Historicismi
		pairingFlags.put("ar sirsnīgu emocionālo noskaņu", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sirsnīga emocionālā nokrāsa"));
		pairingFlags.put("ar sirsnīgu emocionālo noskaņu.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sirsnīga emocionālā nokrāsa"));
		pairingFlags.put("parasti dzejas valodā.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Poētiska stilistiskā nokrāsa"));
		pairingFlags.put("poēt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Poētiska stilistiskā nokrāsa"));
		pairingFlags.put("t. poēt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Poētiska stilistiskā nokrāsa"));
		pairingFlags.put("t. poēt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Folkloras valodai raksturīga stilistiskā nokrāsa"));
		pairingFlags.put("ar mazvērtīguma nokrāsu", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Mazvērtīguma nokrāsa"));
		pairingFlags.put("mīlin.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Mīlinājuma nokrāsa"));
		pairingFlags.put("niev.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nievājoša ekspresīvā nokrāsa"));
		pairingFlags.put("nievājoši", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nievājoša ekspresīvā nokrāsa"));
		pairingFlags.put("nicin.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nicinoša ekspresīvā nokrāsa"));
		pairingFlags.put("nicinoši", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nicinoša ekspresīvā nokrāsa"));
		pairingFlags.put("nicīgi", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nicīga ekspresīvā nokrāsa"));
		pairingFlags.put("iron.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Ironiska ekspresīvā nokrāsa"));
		pairingFlags.put("iron. ar mazo burtu", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Ironiska ekspresīvā nokrāsa"));
		pairingFlags.put("hum.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Humoristiska ekspresīvā nokrāsa"));
		pairingFlags.put("vienk.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Vienkāršrunas stilistiskā nokrāsa"));
		pairingFlags.put("pārn.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Pārnestā nozīmē"));
		pairingFlags.put("eif.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Eifēmisms"));
		pairingFlags.put("bibl.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Biblisms"));
		pairingFlags.put("nevēl.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nevēlams")); // TODO - nevēlamos, neliterāros un žargonus apvienot??
		pairingFlags.put("nelit.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Neliterārs"));
		pairingFlags.put("žarg.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Žargonvārds"));
		pairingFlags.put("slengs", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Slengs"));
		pairingFlags.put("sl.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Slengs"));
		pairingFlags.put("bur. sl.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Slengs"));
		pairingFlags.put("jūrn. sl.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Slengs"));
		pairingFlags.put("sar.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunvaloda"));
		pairingFlags.put("sarunv.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunvaloda"));
		pairingFlags.put("vulg.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Vulgārisms"));
		pairingFlags.put("barb.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Barbarisms"));
		pairingFlags.put("īsziņās", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Īsziņās"));
		pairingFlags.put("īsziņas", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Īsziņās"));
		pairingFlags.put("senv.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sens, reti lietots vārds"));
		pairingFlags.put("paširon.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Pašironija"));
		pairingFlags.put("lamuvārda noz.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Lamuvārds"));
		pairingFlags.put("lamu vārda noz.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Lamuvārds"));
		pairingFlags.put("lamu vārda nozīmē", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Lamuvārds"));
		pairingFlags.put("lamu vārds", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Lamuvārds"));

		// Formu ierobežojumi
		pairingFlags.put("akuz.", Tuple.of(TKeys.USED_IN_FORM, TValues.ACUSATIVE));
		pairingFlags.put("dat.", Tuple.of(TKeys.USED_IN_FORM, TValues.DATIVE));
		pairingFlags.put("ģen.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("arī ģen.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("instr.", Tuple.of(TKeys.USED_IN_FORM, TValues.INSTRUMENTAL));
		pairingFlags.put("lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("nom.", Tuple.of(TKeys.USED_IN_FORM, TValues.NOMINATIVE));
		pairingFlags.put("nom. formā.", Tuple.of(TKeys.USED_IN_FORM, TValues.NOMINATIVE));		// Ļaunums.
		pairingFlags.put("vok.", Tuple.of(TKeys.USED_IN_FORM, TValues.VOCATIVE));		// Ļaunums.
		pairingFlags.put("parasti akuz.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.ACUSATIVE));
		pairingFlags.put("parasti lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("parasti vok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.VOCATIVE));

		pairingFlags.put("dsk. vai divsk.", Tuple.of(TKeys.USED_IN_FORM, TValues.DUAL));
		pairingFlags.put("dsk. vai divsk.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("divsk.", Tuple.of(TKeys.USED_IN_FORM, TValues.DUAL));
		pairingFlags.put("dsk.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("vsk.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("arī dsk.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL));	// Ļaunums.
		pairingFlags.put("arī vsk.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR));		// Ļaunums.
		pairingFlags.put("parasti dsk.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("parasti vsk.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("par. vsk.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("tikai dsk.", TFeatures.USED_ONLY__PLURAL);
		pairingFlags.put("tikai vsk.", Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.SINGULAR));

		// TODO to kombinēto bezprievārda instrumentāli kopā ar skaitli vajag?
		pairingFlags.put("dsk. bezpriev. instr.", Tuple.of(TKeys.USED_IN_FORM, TValues.NOPRON_INSTRUMENTAL));
		pairingFlags.put("dsk. bezpriev. instr.", Tuple.of(TKeys.USED_IN_FORM, TValues.INSTRUMENTAL));
		pairingFlags.put("dsk. bezpriev. instr.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("dsk. bezpriev. instr.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL_INSTRUMENTAL));
		pairingFlags.put("dsk. bezpriev. instr.", Tuple.of(TKeys.USED_IN_FORM, "Daudzskaitļa bezprievārda instrumentālis"));
		pairingFlags.put("dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.NOPRON_INSTRUMENTAL));
		pairingFlags.put("dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.INSTRUMENTAL));
		pairingFlags.put("dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL_INSTRUMENTAL));
		pairingFlags.put("dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, "Daudzskaitļa bezprievārda instrumentālis"));
		pairingFlags.put("dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL_LOCATIVE));
		pairingFlags.put("dsk. dat.", Tuple.of(TKeys.USED_IN_FORM, TValues.DATIVE));
		pairingFlags.put("dsk. dat.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("dsk. dat.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL_DATIVE));
		pairingFlags.put("dsk. ģen.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("dsk. ģen.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("dsk. ģen.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL_GENITIVE));
		pairingFlags.put("dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("dsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL_LOCATIVE));
		pairingFlags.put("vsk. akuz.", Tuple.of(TKeys.USED_IN_FORM, TValues.ACUSATIVE));
		pairingFlags.put("vsk. akuz.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("vsk. akuz.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR_ACUSATIVE));
		pairingFlags.put("vsk. dat.", Tuple.of(TKeys.USED_IN_FORM, TValues.DATIVE));
		pairingFlags.put("vsk. dat.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("vsk. dat.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR_DATIVE));
		pairingFlags.put("vsk. ģen.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("vsk. ģen.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("vsk. ģen.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR_GENITIVE));
		pairingFlags.put("vsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("vsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("vsk. lok.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR_LOCATIVE));
		pairingFlags.put("vsk. nom.", Tuple.of(TKeys.USED_IN_FORM, TValues.NOMINATIVE));
		pairingFlags.put("vsk. nom.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("vsk. nom.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR_NOMINATIVE));
		pairingFlags.put("vsk. vok.", Tuple.of(TKeys.USED_IN_FORM, TValues.VOCATIVE));
		pairingFlags.put("vsk. vok.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("vsk. vok.", Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR_VOCATIVE));

		pairingFlags.put("bieži vsk. lok.", Tuple.of(TKeys.OFTEN_USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("bieži vsk. lok.", Tuple.of(TKeys.OFTEN_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("bieži vsk. lok.", Tuple.of(TKeys.OFTEN_USED_IN_FORM, TValues.SINGULAR_LOCATIVE));

		pairingFlags.put("parasti dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NOPRON_INSTRUMENTAL));
		pairingFlags.put("parasti dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.INSTRUMENTAL));
		pairingFlags.put("parasti dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("parasti dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("parasti dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL_INSTRUMENTAL));
		pairingFlags.put("parasti dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Daudzskaitļa bezprievārda instrumentālis"));
		pairingFlags.put("parasti dsk. bezpriev. instr. vai dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL_LOCATIVE));
		pairingFlags.put("parasti dsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("parasti dsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("parasti dsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL_GENITIVE));
		pairingFlags.put("parasti dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("parasti dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("parasti dsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL_LOCATIVE));
		pairingFlags.put("parasti vsk. akuz.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.ACUSATIVE));
		pairingFlags.put("parasti vsk. akuz.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk. akuz.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR_ACUSATIVE));
		pairingFlags.put("parasti vsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("parasti vsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR_GENITIVE));
		pairingFlags.put("parasti vsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("parasti vsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk. lok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR_LOCATIVE));
		pairingFlags.put("parasti vsk. nom.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NOMINATIVE));
		pairingFlags.put("parasti vsk. nom.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk. nom.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR_NOMINATIVE));
		pairingFlags.put("parasti vsk. vok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.VOCATIVE));
		pairingFlags.put("parasti vsk. vok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk. vok.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR_VOCATIVE));

		pairingFlags.put("retāk dsk. dat.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.DATIVE));
		pairingFlags.put("retāk dsk. dat.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("retāk dsk. dat.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL_DATIVE));
		pairingFlags.put("retāk dsk. ģen.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("retāk dsk. ģen.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("retāk dsk. ģen.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL_GENITIVE));
		pairingFlags.put("retāk dsk. lok.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("retāk dsk. lok.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("retāk dsk. lok.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL_LOCATIVE));
		pairingFlags.put("retāk vsk. akuz.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.ACUSATIVE));
		pairingFlags.put("retāk vsk. akuz.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("retāk vsk. akuz.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR_ACUSATIVE));
		pairingFlags.put("retāk vsk. ģen.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("retāk vsk. ģen.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("retāk vsk. ģen.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR_GENITIVE));
		pairingFlags.put("retāk vsk. lok.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("retāk vsk. lok.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("retāk vsk. lok.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL_LOCATIVE));
		pairingFlags.put("retāk vsk. nom.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.NOMINATIVE));
		pairingFlags.put("retāk vsk. nom.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("retāk vsk. nom.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR_NOMINATIVE));

		pairingFlags.put("tikai v.", Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.MASCULINE));
		pairingFlags.put("parasti dem.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Deminutīvs"));

		pairingFlags.put("parasti 3. personas tag.", TFeatures.USUALLY_USED__THIRD_PERS);
		pairingFlags.put("parasti 3. personas tag.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Tagadne"));
		pairingFlags.put("parasti 3. pers.", TFeatures.USUALLY_USED__THIRD_PERS);
		pairingFlags.put("tikai 3. pers.", TFeatures.USED_ONLY__THIRD_PERS);
		pairingFlags.put("parasti nāk. formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Nākotne"));
		pairingFlags.put("parasti nāk. formā", TFeatures.POS__VERB);
		pairingFlags.put("parasti saliktajos laikos", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Saliktie laiki"));
		pairingFlags.put("parasti saliktajos laikos", TFeatures.POS__VERB);
		pairingFlags.put("parasti saliktajos laikos.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Saliktie laiki"));
		pairingFlags.put("parasti saliktajos laikos.", TFeatures.POS__VERB);
		pairingFlags.put("tikai infinitīvā", Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("tikai infinitīvā", TFeatures.POS__VERB);
		pairingFlags.put("parasti nenoteiksmē", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("parasti nenoteiksmē", TFeatures.POS__VERB);
		pairingFlags.put("infinitīva formā", Tuple.of(TKeys.USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("infinitīva formā", TFeatures.POS__VERB);
		pairingFlags.put("nenoteiksmes formā", Tuple.of(TKeys.USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("nenoteiksmes formā", TFeatures.POS__VERB);
		pairingFlags.put("nenoteiksmes formā.", Tuple.of(TKeys.USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("nenoteiksmes formā.", TFeatures.POS__VERB);
		pairingFlags.put("parasti nenoteiksmes formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("parasti nenoteiksmes formā", TFeatures.POS__VERB);
		pairingFlags.put("parasti nenoteiksmes vai divd. formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("parasti nenoteiksmes vai divd. formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PARTICIPLE));
		pairingFlags.put("parasti nenoteiksmes vai divd. formā", TFeatures.POS__VERB);
		pairingFlags.put("parasti nenoteiksmē vai divd. formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("parasti nenoteiksmē vai divd. formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PARTICIPLE));
		pairingFlags.put("parasti nenoteiksmē vai divd. formā", TFeatures.POS__VERB);
		pairingFlags.put("parasti pavēles formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("parasti pavēles formā", TFeatures.POS__VERB);
		pairingFlags.put("parasti pavēles formā.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("parasti pavēles formā.", TFeatures.POS__VERB);
		pairingFlags.put("parasti pavēles izteiksmē", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("parasti pavēles izteiksmē", TFeatures.POS__VERB);
		pairingFlags.put("pavēles izteiksmē", Tuple.of(TKeys.USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("pavēles izteiksmē", TFeatures.POS__VERB);
		pairingFlags.put("pavēles formā", Tuple.of(TKeys.USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("pavēles formā", TFeatures.POS__VERB);
		pairingFlags.put("ar nenot. gal.", TFeatures.USED__INDEFINITE);
		pairingFlags.put("ar nenot. galotni", TFeatures.USED__INDEFINITE);
		pairingFlags.put("ar nenot. galotni.", TFeatures.USED__INDEFINITE);
		pairingFlags.put("parasti ar nenot. galotni", TFeatures.USUALLY_USED__INDEFINITE);
		pairingFlags.put("parasti ar nenot. galotni.", TFeatures.USUALLY_USED__INDEFINITE);
		pairingFlags.put("parasti ar nenoteikto gal.", TFeatures.USUALLY_USED__INDEFINITE);
		pairingFlags.put("parasti ar nenoteikto galotni", TFeatures.USUALLY_USED__INDEFINITE);
		pairingFlags.put("retāk ar nenot. gal.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.INDEFINITE_ENDING));
		pairingFlags.put("ar not. gal.", TFeatures.USED__DEFINITE);
		pairingFlags.put("ar not. galotni", TFeatures.USED__DEFINITE);
		pairingFlags.put("ar not. galotni.", TFeatures.USED__DEFINITE);
		pairingFlags.put("ar noteikto gal.", TFeatures.USED__DEFINITE);
		pairingFlags.put("ar noteikto gal.", TFeatures.USED__DEFINITE);
		pairingFlags.put("ar noteikto galotni", TFeatures.USED__DEFINITE);
		pairingFlags.put("retāk ar not. gal.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.DEFINITE_ENDING));
		pairingFlags.put("īp. v. ar not. galotni", TFeatures.USED__DEFINITE);
		pairingFlags.put("parasti ar not. gal.", TFeatures.USUALLY_USED__DEFINITE);
		pairingFlags.put("parasti ar not. galotni.", TFeatures.USUALLY_USED__DEFINITE);
		pairingFlags.put("parasti ar not. galotni", TFeatures.USUALLY_USED__DEFINITE);
		pairingFlags.put("parasti ar noteikto gal.", TFeatures.USUALLY_USED__DEFINITE);
		pairingFlags.put("parasti ar noteikto galotni.", TFeatures.USUALLY_USED__DEFINITE);
		pairingFlags.put("parasti pārākajā pakāpē.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("parasti pārākās pakāpes formā.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("pamata pak.", Tuple.of(TKeys.USED_IN_FORM, TValues.POSITIVE_DEGREE));
		pairingFlags.put("pārākajā pakāpē", Tuple.of(TKeys.USED_IN_FORM, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("pārākajā pakāpē.", Tuple.of(TKeys.USED_IN_FORM, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("pārākajā vai vispārākajā pak.", Tuple.of(TKeys.USED_IN_FORM, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("pārākajā vai vispārākajā pak.", Tuple.of(TKeys.USED_IN_FORM, TValues.SUPERLATIVE_DEGREE));
		pairingFlags.put("parasti ar lielo sākumburtu", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Ar lielo sākumburtu"));
		pairingFlags.put("ar lielo sākumburtu", Tuple.of(TKeys.USED_IN_FORM, "Ar lielo sākumburtu"));
		pairingFlags.put("ar lielo sāk. b.", Tuple.of(TKeys.USED_IN_FORM, "Ar lielo sākumburtu"));
		pairingFlags.put("ar mazo sākumburtu", Tuple.of(TKeys.USED_IN_FORM, "Ar mazo sākumburtu"));
		pairingFlags.put("ar mazo burtu", Tuple.of(TKeys.USED_IN_FORM, "Ar mazo sākumburtu"));
		pairingFlags.put("iron. ar mazo burtu", Tuple.of(TKeys.USED_IN_FORM, "Ar mazo sākumburtu"));
		pairingFlags.put("parasti nolieguma formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NEGATIVE));
		pairingFlags.put("parasti nolieguma formā.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.NEGATIVE));
		pairingFlags.put("nolieg. formā.", Tuple.of(TKeys.USED_IN_FORM, TValues.NEGATIVE));
		pairingFlags.put("nolieguma formā", Tuple.of(TKeys.USED_IN_FORM, TValues.NEGATIVE));
		pairingFlags.put("nolieguma formā.", Tuple.of(TKeys.USED_IN_FORM, TValues.NEGATIVE));
		pairingFlags.put("parasti uzrunā.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.VOCATIVE));

		pairingFlags.put("ar akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.ACUSATIVE));
		pairingFlags.put("ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("ar ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("ar instr.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.INSTRUMENTAL));
		pairingFlags.put("ar lietv. ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("ar lietv. ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.NOUN));
		pairingFlags.put("ar lietv. ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, "Lietvārds ģenitīvā"));

		pairingFlags.put("savienojumā ar verbālsubstantīvu ar izskaņu -šana.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.NOUN));
		pairingFlags.put("savienojumā ar verbālsubstantīvu ar izskaņu -šana.", Tuple.of(TKeys.USED_TOGETHER_WITH, "Lietvārds ar izskaņu -šana"));

		pairingFlags.put("parasti ar dat.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("parasti ar ģen.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("parasti savienojumā ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADJECTIVE));
		pairingFlags.put("parasti savienojumā ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADVERB));
		pairingFlags.put("parasti savienojumā ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("parasti ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADJECTIVE));
		pairingFlags.put("parasti ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADVERB));
		pairingFlags.put("parasti ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("parasti ar negāciju", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.NEGATIVE));
		pairingFlags.put("parasti ar negāciju.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.NEGATIVE));

		pairingFlags.put("retāk ar akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.ACUSATIVE));
		pairingFlags.put("retāk ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("retāk ar divsk. nom. vai akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DUAL));
		pairingFlags.put("retāk ar divsk. nom. vai akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.NOMINATIVE));
		pairingFlags.put("retāk ar divsk. nom. vai akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.ACUSATIVE));
		pairingFlags.put("retāk ar divsk. nom. vai akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, "Divskaitļa nominatīvs"));
		pairingFlags.put("retāk ar divsk. nom. vai akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, "Divskaitļa akuzatīvs"));

		// TODO: iztīrīt šito
		pairingFlags.put("parasti apst.", Tuple.of(TKeys.USED_IN_FORM, TValues.ADVERB));

		pairingFlags.put("izsaukuma teikumos", Tuple.of(TKeys.USED_IN_STRUCT, "Izsaukuma teikums"));
		pairingFlags.put("izsaukuma teikumos.", Tuple.of(TKeys.USED_IN_STRUCT, "Izsaukuma teikums"));
		pairingFlags.put("izsaukuma un pamudinājuma teikumos", Tuple.of(TKeys.USED_IN_STRUCT, "Izsaukuma teikums"));
		pairingFlags.put("izsaukuma un pamudinājuma teikumos", Tuple.of(TKeys.USED_IN_STRUCT, "Pamudinājuma teikums"));		pairingFlags.put("parasti nolieguma teikumos", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Nolieguma teikums"));
		pairingFlags.put("jautājuma teikumos", Tuple.of(TKeys.USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("jautājuma teikumos.", Tuple.of(TKeys.USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("retoriskajos jautājuma teikumos", Tuple.of(TKeys.USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("retoriskajos jautājuma teikumos", Tuple.of(TKeys.USED_IN_STRUCT, "Retorisks jautājuma teikums"));

		pairingFlags.put("parasti izsaukuma teikumos", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Izsaukuma teikums"));
		pairingFlags.put("parasti izsaukuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Izsaukuma teikums"));
		pairingFlags.put("parasti nolieguma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Nolieguma teikums"));
		pairingFlags.put("parasti izsaukuma teikumos vai retoriskajos jautājuma teikumos", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("parasti izsaukuma teikumos vai retoriskajos jautājuma teikumos", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Retorisks jautājuma teikums"));
		pairingFlags.put("parasti izsaukuma teikumos vai retoriskajos jautājuma teikumos", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Izsaukuma teikums"));
		pairingFlags.put("parasti izsaukuma teikumos vai retoriskajos jautājuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("parasti izsaukuma teikumos vai retoriskajos jautājuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Retorisks jautājuma teikums"));
		pairingFlags.put("parasti izsaukuma teikumos vai retoriskajos jautājuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Izsaukuma teikums"));
		pairingFlags.put("parasti jautājuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("parasti jautājuma teikumos", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("parasti jautājuma teikumā.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("parasti pamudinājuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Pamudinājuma teikums"));
		pairingFlags.put("parasti retoriskajos jautājuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("parasti retoriskajos jautājuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Retorisks jautājuma teikums"));
		pairingFlags.put("parasti retoriskajos jautājumos un nolieguma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Jautājuma teikums"));
		pairingFlags.put("parasti retoriskajos jautājumos un nolieguma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Retorisks jautājuma teikums"));
		pairingFlags.put("parasti retoriskajos jautājumos un nolieguma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Nolieguma teikums"));
		pairingFlags.put("parasti stāstījuma teikumos", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Stāstījuma teikums"));
		pairingFlags.put("parasti stāstījuma teikumos.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Stāstījuma teikums"));

		pairingFlags.put("teikumā ar noliegtu verbu", Tuple.of(TKeys.USED_IN_STRUCT, "Teikums ar noliegtu darbības vārdu"));
		pairingFlags.put("teikumā ar noliegtu verbu", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.VERB));
		pairingFlags.put("teikumā ar noliegtu verbu", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.NEGATIVE_VERB));
		pairingFlags.put("teikumā ar noliegtu verbu.", Tuple.of(TKeys.USED_IN_STRUCT, "Teikums ar noliegtu darbības vārdu"));
		pairingFlags.put("teikumā ar noliegtu verbu.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.VERB));
		pairingFlags.put("teikumā ar noliegtu verbu.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.NEGATIVE_VERB));
		pairingFlags.put("teikumos ar noliegtu verbu", Tuple.of(TKeys.USED_IN_STRUCT, "Teikums ar noliegtu darbības vārdu"));
		pairingFlags.put("teikumos ar noliegtu verbu", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.VERB));
		pairingFlags.put("teikumos ar noliegtu verbu", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.NEGATIVE_VERB));
		pairingFlags.put("teikumos ar noliegtu verbu.", Tuple.of(TKeys.USED_IN_STRUCT, "Teikums ar noliegtu darbības vārdu"));
		pairingFlags.put("teikumos ar noliegtu verbu.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.VERB));
		pairingFlags.put("teikumos ar noliegtu verbu.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.NEGATIVE_VERB));

		pairingFlags.put("parasti teikumā ar noliegtu verbu.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Teikums ar noliegtu darbības vārdu"));
		pairingFlags.put("parasti teikumā ar noliegtu verbu.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.VERB));
		pairingFlags.put("parasti teikumā ar noliegtu verbu.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.NEGATIVE_VERB));
		pairingFlags.put("parasti teikumos ar noliegtu verbu", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Teikums ar noliegtu darbības vārdu"));
		pairingFlags.put("parasti teikumos ar noliegtu verbu", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.VERB));
		pairingFlags.put("parasti teikumos ar noliegtu verbu", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.NEGATIVE_VERB));
		pairingFlags.put("parasti teikumos ar noliegtu verbu.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Teikums ar noliegtu darbības vārdu"));
		pairingFlags.put("parasti teikumos ar noliegtu verbu.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.VERB));
		pairingFlags.put("parasti teikumos ar noliegtu verbu.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.NEGATIVE_VERB));

		pairingFlags.put("parasti teikuma sākumā.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Teikuma sākums"));
		pairingFlags.put("parasti izsaukuma teikuma beigās.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Teikuma beigas"));
		pairingFlags.put("parasti izsaukuma teikuma beigās.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Izsaukuma teikums"));

		pairingFlags.put("aiz apzīmējamā vārda", Tuple.of(TKeys.USED_IN_STRUCT, "Aiz apzīmējamā vārda"));

		pairingFlags.put("atkārtojumā.", Tuple.of(TKeys.USED_IN_STRUCT, "Atkārtojums"));
		pairingFlags.put("atkārtotā lietojumā.", Tuple.of(TKeys.USED_IN_STRUCT, "Atkārtojums"));
		pairingFlags.put("viena un tā paša vārda atkārtojumā", Tuple.of(TKeys.USED_IN_STRUCT, "Atkārtojums"));
		pairingFlags.put("salīdzinājuma konstrukcijā", Tuple.of(TKeys.USED_IN_STRUCT, "Salīdzinājuma konstrukcija"));

		pairingFlags.put("parasti atkārtojumā", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Atkārtojums"));
		pairingFlags.put("parasti atkārtojumā.", Tuple.of(TKeys.USUALLY_USED_IN_STRUCT, "Atkārtojums"));

		pairingFlags.put("arī atkārtojumā.", Tuple.of(TKeys.ALSO_USED_IN_STRUCT, "Atkārtojums"));

		pairingFlags.put("saikļa nozīmē palīgteikumos.", Tuple.of(TKeys.USED_IN_STRUCT, "Palīgteikums"));
		pairingFlags.put("saikļa nozīmē palīgteikumos.", Tuple.of(TKeys.CONTAMINATION, TValues.CONJUNCTION));

		// Lietojuma biežums.
		pairingFlags.put("pareti.", Tuple.of(TKeys.USAGE_FREQUENCY, "Pareti"));
		pairingFlags.put("pareti", Tuple.of(TKeys.USAGE_FREQUENCY, "Pareti"));
		pairingFlags.put("reti.", Tuple.of(TKeys.USAGE_FREQUENCY, "Reti"));
		pairingFlags.put("reti", Tuple.of(TKeys.USAGE_FREQUENCY, "Reti"));
		pairingFlags.put("retāk.", Tuple.of(TKeys.USAGE_FREQUENCY, "Retāk"));
		pairingFlags.put("retāk", Tuple.of(TKeys.USAGE_FREQUENCY, "Retāk"));

		// Kontaminācija
		pairingFlags.put("subst. noz.", TFeatures.CONTAMINATION__NOUN);
		pairingFlags.put("substantīva nozīmē", TFeatures.CONTAMINATION__NOUN);
		pairingFlags.put("lietv. noz.", TFeatures.CONTAMINATION__NOUN);
		pairingFlags.put("lietv. nozīmē", TFeatures.CONTAMINATION__NOUN);
		pairingFlags.put("lietv. nozīmē.", TFeatures.CONTAMINATION__NOUN);
		pairingFlags.put("īpašvārda nozīmē.", TFeatures.CONTAMINATION__NOUN);
		pairingFlags.put("īpašvārda nozīmē.", Tuple.of(TKeys.CONTAMINATION, "Īpašvārds"));
		pairingFlags.put("parasti īpašvārda nozīmē.", TFeatures.CONTAMINATION__NOUN);
		pairingFlags.put("parast īpašvārda nozīmē.", Tuple.of(TKeys.CONTAMINATION, "Īpašvārds"));
		pairingFlags.put("palīgverba nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.VERB));
		pairingFlags.put("palīgverba nozīmē", Tuple.of(TKeys.CONTAMINATION, "Palīgdarbības vārds"));
		pairingFlags.put("palīgverba nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.VERB));
		pairingFlags.put("palīgverba nozīmē.", Tuple.of(TKeys.CONTAMINATION, "Palīgdarbības vārds"));
		pairingFlags.put("adj. noz.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("adj. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("adj. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("īp. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("īp. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("īp. v. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("īp. v. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("apst. noz.", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("apst. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("apst. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("adv. noz.", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("adv. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("adv. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("interj. noz.", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("izsauk. noz.", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("izsauk. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("izsauk. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("izsauksmes vārda nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("saikļa nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.CONJUNCTION));
		pairingFlags.put("saikļa nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.CONJUNCTION));
		pairingFlags.put("skait. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.NUMERAL));
		pairingFlags.put("skait. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.NUMERAL));
		pairingFlags.put("part. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.PARTICLE));
		pairingFlags.put("part. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.PARTICLE));
		pairingFlags.put("partikulas nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.PARTICLE));
		pairingFlags.put("modāla vārda nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.PARTICLE));
		pairingFlags.put("modāla vārda nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.PARTICLE));

		pairingFlags.put("ģen.: adj. nozīmē.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen.: adj. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("ģen.: adj. nozīmē", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen.: adj. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("ģen.: īp. nozīmē.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen.: īp. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("ģen.: īp. nozīmē", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen.: īp. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("ģen. īp. v. nozīmē", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen. īp. v. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("ģen.: īp. v. nozīmē", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen.: īp. v. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("ģen. īp. v. nozīmē.", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen. īp. v. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("ģen. īp. nozīmē", Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("ģen. īp. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("lok.: apst. nozīmē", Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE));
		pairingFlags.put("lok.: apst. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("nenoteiktā vietn. un adj. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("nenoteiktā vietn. un adj. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.INDEFINITE_PRONOUN));
		pairingFlags.put("nenoteiktā vietn. un adj. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.PRONOUN));
		pairingFlags.put("nenoteiktā vietn. un lietv. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.NOUN));
		pairingFlags.put("nenoteiktā vietn. un lietv. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.INDEFINITE_PRONOUN));
		pairingFlags.put("nenoteiktā vietn. un lietv. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.PRONOUN));
		pairingFlags.put("nenot. vietn. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.PRONOUN));
		pairingFlags.put("nenot. vietn. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.INDEFINITE_PRONOUN));

		pairingFlags.put("priev. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADPOSITION));

		pairingFlags.put("priev. noz. ar dat.", Tuple.of(TKeys.CONTAMINATION, TValues.ADPOSITION));
		pairingFlags.put("priev. noz. ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("priev. nozīmē ar dat.", Tuple.of(TKeys.CONTAMINATION, TValues.ADPOSITION));
		pairingFlags.put("priev. nozīmē ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("arī priev. nozīmē ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("priev. nozīmē ar instr.", Tuple.of(TKeys.CONTAMINATION, TValues.ADPOSITION));
		pairingFlags.put("priev. nozīmē ar instr.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.INSTRUMENTAL));
		pairingFlags.put("priev. nozīmē ar dat. vai ģen.", Tuple.of(TKeys.CONTAMINATION, TValues.ADPOSITION));
		pairingFlags.put("priev. nozīmē ar dat. vai ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("priev. nozīmē ar dat. vai ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.INSTRUMENTAL));

		//binaryFlags.put("var.", "Variants"); // Izņemts no datiem.
		binaryFlags.put("hip.", "Hipotēze");
		binaryFlags.put("dsk. formas parasti lietojamas vsk. formu nozīmē.", "Daudzskaitļa formas parasti lieto vienskaitļa formu nozīmē");
		binaryFlags.put("dsk. formas vsk. formu nozīmē.", "Daudzskaitļa formas lieto vienskaitļa formu nozīmē");
		binaryFlags.put("dsk. lietojams arī vsk. nozīmē.", "Daudzskaitļa formas lieto vienskaitļa formu nozīmē");
		binaryFlags.put("vsk. formas lietojamas arī dsk. formu nozīmē.", "Vienskaitļa formas lieto daudzskaitļa formu nozīmē");
		binaryFlags.put("sar. arī vsk.", "Vienskaitļa formas lieto sarunvalodā");
		binaryFlags.put("nelit. arī vsk.", "Vienskaitļa formas lieto sarunvalodā");
		binaryFlags.put("apv. arī vsk.", "Vienskaitļa formas lieto dialektos");
		binaryFlags.put("lit. val. tikai dsk.", "Vienskaitļa formas lieto sarunvalodā");

		//pairingFlags.put("angļu val. \"byte\"", Tuple.of(TKeys.ETYMOLOGY, "angļu val. \"byte\""));
	}

}
