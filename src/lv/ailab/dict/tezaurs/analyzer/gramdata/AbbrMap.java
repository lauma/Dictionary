package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.struct.Flags;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
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
		if (!feature.first.equals(TKeys.CASE)) return null;
		return feature.second;
	}

	protected AbbrMap()
	{
		binaryFlags = new MappingSet<>();
		pairingFlags = new MappingSet<>();

		pairingFlags.put("adj.", TFeatures.POS__ADJ);
		pairingFlags.put("adv.", Tuple.of(TKeys.POS, TValues.ADVERB));
		pairingFlags.put("apst.", Tuple.of(TKeys.POS, TValues.ADVERB));
		pairingFlags.put("darb.", TFeatures.POS__VERB);
		pairingFlags.put("divd.", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("Divd.", TFeatures.POS__PARTICIPLE);
		pairingFlags.put("interj.", Tuple.of(TKeys.POS, TValues.INTERJECTION));
		pairingFlags.put("īp. v.", TFeatures.POS__ADJ);
		pairingFlags.put("īp.", TFeatures.POS__ADJ);
		pairingFlags.put("izsauk.", Tuple.of(TKeys.POS, TValues.INTERJECTION));
		pairingFlags.put("izsauksmes vārds", Tuple.of(TKeys.POS, TValues.INTERJECTION));
		pairingFlags.put("lietv.", TFeatures.POS__NOUN);
		pairingFlags.put("subst.", TFeatures.POS__NOUN);
		pairingFlags.put("part.", TFeatures.POS__PARTICLE);
		pairingFlags.put("prep.", TFeatures.POS__PREPOSITION);
		pairingFlags.put("priev.", TFeatures.POS__PREPOSITION);
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

		pairingFlags.put("pron.", TFeatures.POS__PRONOUN);
		pairingFlags.put("vietn.", TFeatures.POS__PRONOUN);
		pairingFlags.put("vietniekv.", TFeatures.POS__PRONOUN);
		pairingFlags.put("atgriez. vietn.", TFeatures.POS__REFL_PRONOUN);
		pairingFlags.put("jaut.", TFeatures.POS__INTERROG_PRONOUN);
		pairingFlags.put("jaut. vietn.", TFeatures.POS__INTERROG_PRONOUN);
		pairingFlags.put("nenot. vietn.", TFeatures.POS__INDEF_PRONOUN);
		pairingFlags.put("nenoteiktais vietn.", TFeatures.POS__INDEF_PRONOUN);
		pairingFlags.put("noliedz.", TFeatures.POS__NEG_PRONOUN);
		pairingFlags.put("noliedz. vietn.", TFeatures.POS__NEG_PRONOUN);
		pairingFlags.put("norād.", TFeatures.POS__DEM_PRONOUN);
		pairingFlags.put("norād. vietn.", TFeatures.POS__DEM_PRONOUN);
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

		pairingFlags.put("vienojuma saikļa sastāvdaļa", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma saikļa sastāvdaļa", Tuple.of(TKeys.POS, "Vienojuma saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("vienojuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("vienojuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Vienojuma saikļa sastāvdaļa"));
		pairingFlags.put("pieļāvuma saikļa sastāvdaļa.", TFeatures.POS__CONJUNCTION);
		pairingFlags.put("pieļāvuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Saikļa sastāvdaļa"));
		pairingFlags.put("pieļāvuma saikļa sastāvdaļa.", Tuple.of(TKeys.POS, "Pieļāvuma saikļa sastāvdaļa"));

		pairingFlags.put("priev. ar akuz.", TFeatures.POS__PREPOSITION);
		pairingFlags.put("priev. ar akuz.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.ACUSATIVE));
		pairingFlags.put("priev. ar ģen.", TFeatures.POS__PREPOSITION);
		pairingFlags.put("priev. ar ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("ar ģen.", TFeatures.POS__PREPOSITION); // Šķiet, ka bez papildus komentāriem to raksta tikai pie prievārdiem.
		pairingFlags.put("ar ģen.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("priev. ar dat.", TFeatures.POS__PREPOSITION);
		pairingFlags.put("priev. ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));
		pairingFlags.put("priev. ar instr.", TFeatures.POS__PREPOSITION);
		pairingFlags.put("priev. ar instr.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.INSTRUMENTAL));

		pairingFlags.put("parasti ar ģen.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.GENITIVE));
		pairingFlags.put("parasti savienojamā ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADJECTIVE));
		pairingFlags.put("parasti savienojamā ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADVERB));
		pairingFlags.put("parasti savienojamā ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.COMPARATIVE_DEGREE));
		pairingFlags.put("parasti ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADJECTIVE));
		pairingFlags.put("parasti ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.ADVERB));
		pairingFlags.put("parasti ar adj. vai apst. pārāko pakāpi.", Tuple.of(TKeys.USUALLY_USED_TOGETHER_WITH, TValues.COMPARATIVE_DEGREE));


		pairingFlags.put("persv.", TFeatures.POS__NOUN);
		binaryFlags.put("persv.", TValues.PERSON_NAME);
		//pairingFlags.put("vietv.", TFeatures.POS__NOUN); // jāuzmanās ar mazciemu "Siltais"
														// Ko darīt ar sastingušajām formām?
		binaryFlags.put("vietv.", "Vietvārds");

		binaryFlags.put("nelok.", TValues.NON_INFLECTIVE);
		binaryFlags.put("lokāms.", "Lokāms vārds");

		pairingFlags.put("akuz.", Tuple.of(TKeys.CASE, TValues.ACUSATIVE));
		pairingFlags.put("dat.", Tuple.of(TKeys.CASE, TValues.DATIVE));
		pairingFlags.put("ģen.", Tuple.of(TKeys.CASE, TValues.GENITIVE));
		pairingFlags.put("instr.", Tuple.of(TKeys.CASE, TValues.INSTRUMENTAL));
		pairingFlags.put("lok.", Tuple.of(TKeys.CASE, TValues.LOCATIVE));
		pairingFlags.put("nom.", Tuple.of(TKeys.CASE, TValues.NOMINATIVE));

		pairingFlags.put("dsk. ģen.", Tuple.of(TKeys.CASE, TValues.GENITIVE));
		pairingFlags.put("dsk. ģen.", Tuple.of(TKeys.NUMBER, TValues.PLURAL));
		pairingFlags.put("vsk. ģen.", Tuple.of(TKeys.CASE, TValues.GENITIVE));
		pairingFlags.put("vsk. ģen.", Tuple.of(TKeys.NUMBER, TValues.SINGULAR));


		pairingFlags.put("divsk.", Tuple.of(TKeys.NUMBER, TValues.DUAL));
		pairingFlags.put("dsk.", Tuple.of(TKeys.NUMBER, TValues.PLURAL));
		pairingFlags.put("vsk.", Tuple.of(TKeys.NUMBER, TValues.SINGULAR));

		pairingFlags.put("ģen. nelok. īp. nozīmē.", Tuple.of(TKeys.CASE, TValues.GENITIVE));
		pairingFlags.put("ģen. nelok. īp. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		binaryFlags.put("ģen. nelok. īp. nozīmē.", TValues.NON_INFLECTIVE);
		pairingFlags.put("ģen. nelok. īp. nozīmē", Tuple.of(TKeys.CASE, TValues.GENITIVE));
		pairingFlags.put("ģen. nelok. īp. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		binaryFlags.put("ģen. nelok. īp. nozīmē", TValues.NON_INFLECTIVE);

		/*binaryFlags.put("nāk.", "Nākotne");
		binaryFlags.put("pag.", "Pagātne");
		binaryFlags.put("tag.", "Tagadne");
		
		binaryFlags.put("nenot.", "Nenoteiktā galotne");
		binaryFlags.put("not.", "Noteiktā galotne");*/
		
		pairingFlags.put("s.", TFeatures.GENDER__FEM);
		pairingFlags.put("v.", TFeatures.GENDER__MASC);
		pairingFlags.put("kopdz.", Tuple.of(TKeys.GENDER, TValues.COGENDER));
		
		binaryFlags.put("intrans.", "Nepārejošs");
		binaryFlags.put("intr.", "Nepārejošs");
		binaryFlags.put("nepārej.", "Nepārejošs");
		binaryFlags.put("trans.", "Pārejošs");
		binaryFlags.put("pārej.", "Pārejošs");
		binaryFlags.put("tr.", "Pārejošs");
		binaryFlags.put("tr. un intr. darb. v.", "Pārejošs");
		binaryFlags.put("tr. un intr. darb. v.", "Nepārejošs");
		binaryFlags.put("tr. un intr. darb. v.", "Pārejošs un nepārejošs");

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
		pairingFlags.put("agr.", Tuple.of(TKeys.DOMAIN, "Agronomija"));
		pairingFlags.put("anat.", Tuple.of(TKeys.DOMAIN, "Anatomija"));
		pairingFlags.put("antr.", Tuple.of(TKeys.DOMAIN, "Antropoloģija"));
		pairingFlags.put("antrop.", Tuple.of(TKeys.DOMAIN, "Antropoloģija"));
		pairingFlags.put("arheol.", Tuple.of(TKeys.DOMAIN, "Arheoloģija"));
		pairingFlags.put("arhit.", Tuple.of(TKeys.DOMAIN, "Arhitektūra"));
		pairingFlags.put("arh.", Tuple.of(TKeys.DOMAIN, "Arhitektūra"));
		pairingFlags.put("astr.", Tuple.of(TKeys.DOMAIN, "Astronomija"));
		pairingFlags.put("av.", Tuple.of(TKeys.DOMAIN, "Aviācija"));
		pairingFlags.put("ādas apstr.", Tuple.of(TKeys.DOMAIN, "Ādas apstrāde"));
		pairingFlags.put("ādu apstr.", Tuple.of(TKeys.DOMAIN, "Ādas apstrāde"));
		pairingFlags.put("bioķīm.", Tuple.of(TKeys.DOMAIN, "Bioķīmija"));
		pairingFlags.put("biol.", Tuple.of(TKeys.DOMAIN, "Bioloģija"));
		pairingFlags.put("bišk.", Tuple.of(TKeys.DOMAIN, "Biškopība"));
		pairingFlags.put("biškop.", Tuple.of(TKeys.DOMAIN, "Biškopība"));
		pairingFlags.put("bot.", Tuple.of(TKeys.DOMAIN, "Botānika"));
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
		pairingFlags.put("hist.", Tuple.of(TKeys.DOMAIN, "Historigrāfija"));
		pairingFlags.put("inf.", Tuple.of(TKeys.DOMAIN, "Informātika"));
		pairingFlags.put("jur.", Tuple.of(TKeys.DOMAIN, "Jurisprudence"));
		pairingFlags.put("jūrn.", Tuple.of(TKeys.DOMAIN, "Jūrniecība"));
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
		pairingFlags.put("ornit.", Tuple.of(TKeys.DOMAIN, "Ornitoloģija"));
		pairingFlags.put("papīrr.", Tuple.of(TKeys.DOMAIN, "Papīrrūpniecība"));
		pairingFlags.put("pārt.", Tuple.of(TKeys.DOMAIN, "Pārtika"));
		pairingFlags.put("pol.", Tuple.of(TKeys.DOMAIN, "Politika"));
		pairingFlags.put("poligr.", Tuple.of(TKeys.DOMAIN, "Poligrāfija"));
		pairingFlags.put("polit.", Tuple.of(TKeys.DOMAIN, "Politisks"));
		pairingFlags.put("psih.", Tuple.of(TKeys.DOMAIN, "Psiholoģija"));
		pairingFlags.put("psihol.", Tuple.of(TKeys.DOMAIN, "Psiholoģija"));
		pairingFlags.put("rel.", Tuple.of(TKeys.DOMAIN, "Reliģija"));
		pairingFlags.put("rūpn.", Tuple.of(TKeys.DOMAIN, "Rūpniecība"));
		pairingFlags.put("sil. tehnol.", Tuple.of(TKeys.DOMAIN, "Silikātu tehnoloģijas")); // ?
		pairingFlags.put("siltumt.", Tuple.of(TKeys.DOMAIN, "Siltumtehnika"));
		pairingFlags.put("social.", Tuple.of(TKeys.DOMAIN, "Socioloģija"));
		pairingFlags.put("sociol.", Tuple.of(TKeys.DOMAIN, "Socioloģija"));
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
		pairingFlags.put("ebr.", Tuple.of(TKeys.LANGUAGE, "Ebreju"));
		pairingFlags.put("ebr.", TFeatures.POS__FOREIGN);
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
		pairingFlags.put("ig.", Tuple.of(TKeys.LANGUAGE, "Igauņu"));
		pairingFlags.put("ig.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ig. val.", Tuple.of(TKeys.LANGUAGE, "Igauņu"));
		pairingFlags.put("ig. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("it.", Tuple.of(TKeys.LANGUAGE, "Itāļu/Itāliešu"));
		pairingFlags.put("it.", TFeatures.POS__FOREIGN);
		pairingFlags.put("jap.", Tuple.of(TKeys.LANGUAGE, "Japāņu"));
		pairingFlags.put("jap.", TFeatures.POS__FOREIGN);
		pairingFlags.put("krievu val.", Tuple.of(TKeys.LANGUAGE, "Krievu"));
		pairingFlags.put("krievu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("kr.", Tuple.of(TKeys.LANGUAGE, "Krievu"));
		pairingFlags.put("kr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("ķīn.", Tuple.of(TKeys.LANGUAGE, "Ķīniešu"));
		pairingFlags.put("ķīn.", TFeatures.POS__FOREIGN);
		pairingFlags.put("lat.", Tuple.of(TKeys.LANGUAGE, "Latīņu"));
		pairingFlags.put("lat.", TFeatures.POS__FOREIGN);
		pairingFlags.put("latīņu val.", Tuple.of(TKeys.LANGUAGE, "Latīņu"));
		pairingFlags.put("latīņu val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("liet.", Tuple.of(TKeys.LANGUAGE, "Lietuviešu"));
		pairingFlags.put("liet.", TFeatures.POS__FOREIGN);
		pairingFlags.put("lietuv. val.", Tuple.of(TKeys.LANGUAGE, "Lietuviešu"));
		pairingFlags.put("lietuv. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("līb.", Tuple.of(TKeys.LANGUAGE, "Lībiešu"));
		pairingFlags.put("līb.", TFeatures.POS__FOREIGN);
		pairingFlags.put("mong.", Tuple.of(TKeys.LANGUAGE, "Mongoļu"));
		pairingFlags.put("mong.", TFeatures.POS__FOREIGN);
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
		pairingFlags.put("no sanskrita", Tuple.of(TKeys.LANGUAGE, "Sanskrits"));
		pairingFlags.put("no sanskrita", TFeatures.POS__FOREIGN);
		pairingFlags.put("senebr.", Tuple.of(TKeys.LANGUAGE, "Senebreju"));
		pairingFlags.put("senebr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("sengr.", Tuple.of(TKeys.LANGUAGE, "Sengrieķu"));
		pairingFlags.put("sengr.", TFeatures.POS__FOREIGN);
		pairingFlags.put("sp. val.", Tuple.of(TKeys.LANGUAGE, "Spāņu"));
		pairingFlags.put("sp. val.", TFeatures.POS__FOREIGN);
		pairingFlags.put("sp.", Tuple.of(TKeys.LANGUAGE, "Spāņu"));
		pairingFlags.put("sp.", TFeatures.POS__FOREIGN);
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

		// Lietojuma ierobežojums
		pairingFlags.put("bērnu val.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("bērnu valodā", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("parasti bērnu val.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Bērnu valoda"));
		pairingFlags.put("parasti sar. ar bērniem.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunā ar bērniem"));
		pairingFlags.put("apv.", TFeatures.USAGE_RESTR__REGIONAL);
		pairingFlags.put("vēst.", Tuple.of(TKeys.USAGE_RESTRICTIONS, TValues.HISTORICAL));
		// TODO vēst. laikam ir domēns nevis ierobežojums!!!!!!!!!
		pairingFlags.put("novec.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Novecojis"));
		pairingFlags.put("neakt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Neaktuāls")); // Historicismi
		pairingFlags.put("ar sirsnīgu emocionālo noskaņu", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sirsnīga emocionālā nokrāsa"));
		pairingFlags.put("ar sirsnīgu emocionālo noskaņu.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sirsnīga emocionālā nokrāsa"));
		pairingFlags.put("poēt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Poētiska stilistiskā nokrāsa"));
		pairingFlags.put("t. poēt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Poētiska stilistiskā nokrāsa"));
		pairingFlags.put("t. poēt.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Folkloras valodai raksturīga stilistiskā nokrāsa"));
		pairingFlags.put("mīlin.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Mīlinājuma nokrāsa"));
		pairingFlags.put("niev.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nievājoša ekspresīvā nokrāsa"));
		pairingFlags.put("nievājoši", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nievājoša ekspresīvā nokrāsa"));
		pairingFlags.put("nicin.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nicinoša ekspresīvā nokrāsa"));
		pairingFlags.put("iron.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Ironiska ekspresīvā nokrāsa"));
		pairingFlags.put("hum.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Humoristiska ekspresīvā nokrāsa"));
		pairingFlags.put("vienk.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Vienkāršrunas stilistiskā nokrāsa"));
		pairingFlags.put("pārn.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Pārnestā nozīmē"));
		pairingFlags.put("eif.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Eifēmisms"));
		pairingFlags.put("bibl.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Biblisms"));
		pairingFlags.put("nevēl.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Nevēlams")); // TODO - nevēlamos, neliterāros un žargonus apvienot??
		pairingFlags.put("nelit.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Neliterārs"));
		pairingFlags.put("žarg.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Žargonvārds"));
		pairingFlags.put("sar.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunvaloda"));
		pairingFlags.put("sarunv.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sarunvaloda"));
		pairingFlags.put("vulg.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Vulgārisms"));
		pairingFlags.put("barb.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Barbarisms"));
		pairingFlags.put("īsziņās", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Īsziņās"));
		pairingFlags.put("senv.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Sens, reti lietots vārds"));
		pairingFlags.put("paširon.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Pašironija"));
		pairingFlags.put("lamuvārda noz.", Tuple.of(TKeys.USAGE_RESTRICTIONS, "Lamuvārds"));

		// Formu ierobežojumi
		pairingFlags.put("arī vsk.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.SINGULAR));		// Ļaunums.
		pairingFlags.put("parasti vsk.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("par. vsk.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("parasti vsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("tikai vsk.", Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.SINGULAR));
		pairingFlags.put("arī dsk.", Tuple.of(TKeys.ALSO_USED_IN_FORM, TValues.PLURAL));	// Ļaunums.
		pairingFlags.put("parasti dsk.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("parasti dsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL));
		pairingFlags.put("parasti dsk. ģen.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.GENITIVE));
		pairingFlags.put("tikai dsk.", TFeatures.USED_ONLY__PLURAL);
		pairingFlags.put("tikai v.", Tuple.of(TKeys.USED_ONLY_IN_FORM, TValues.MASCULINE));
		pairingFlags.put("parasti dem.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Deminutīvs"));
		pairingFlags.put("parasti 3. pers.", TFeatures.USUALLY_USED__THIRD_PERS);
		pairingFlags.put("tikai 3. pers.", TFeatures.USED_ONLY__THIRD_PERS);
		pairingFlags.put("parasti saliktajos laikos", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Saliktie laiki"));
		pairingFlags.put("parasti saliktajos laikos", TFeatures.POS__VERB);
		pairingFlags.put("parasti saliktajos laikos.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Saliktie laiki"));
		pairingFlags.put("parasti saliktajos laikos.", TFeatures.POS__VERB);
		pairingFlags.put("parasti nenoteiksmē", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.INFINITIVE));
		pairingFlags.put("parasti nenoteiksmē", TFeatures.POS__VERB);
		pairingFlags.put("parasti pavēles formā", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("parasti pavēles formā", TFeatures.POS__VERB);
		pairingFlags.put("parasti pavēles formā.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("parasti pavēles formā.", TFeatures.POS__VERB);
		pairingFlags.put("pavēles izteiksmē", Tuple.of(TKeys.USED_IN_FORM, TValues.IMPERATIVE));
		pairingFlags.put("pavēles izteiksmē", TFeatures.POS__VERB);
		pairingFlags.put("ar not. gal.", Tuple.of(TKeys.USED_IN_FORM, "Ar noteikto galotni"));
		pairingFlags.put("ar not. galotni", Tuple.of(TKeys.USED_IN_FORM, "Ar noteikto galotni"));
		pairingFlags.put("ar not. galotni.", Tuple.of(TKeys.USED_IN_FORM, "Ar noteikto galotni"));
		pairingFlags.put("ar nototeikto galotni", Tuple.of(TKeys.USED_IN_FORM, "Ar noteikto galotni"));
		pairingFlags.put("parasti ar not. galotni.", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Ar noteikto galotni"));
		pairingFlags.put("parasti ar not. galotni", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Ar noteikto galotni"));
		pairingFlags.put("parasti ar lielo sākumburtu", Tuple.of(TKeys.USUALLY_USED_IN_FORM, "Ar lielo sākumburtu"));
		pairingFlags.put("ar lielo sākumburtu", Tuple.of(TKeys.USED_IN_FORM, "Ar lielo sākumburtu"));
		pairingFlags.put("ar mazo sākumburtu", Tuple.of(TKeys.USED_IN_FORM, "Ar mazo sākumburtu"));
		pairingFlags.put("ar mazo burtu", Tuple.of(TKeys.USED_IN_FORM, "Ar mazo sākumburtu"));
		pairingFlags.put("parasti apst.", Tuple.of(TKeys.USED_IN_FORM, TValues.ADVERB));

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
		pairingFlags.put("adj. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("adj. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("īp. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("īp. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADJECTIVE));
		pairingFlags.put("apst. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("apst. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.ADVERB));
		pairingFlags.put("izsauk. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("izsauk. nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("izsauksmes vārda nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.INTERJECTION));
		pairingFlags.put("saikļa nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.CONJUNCTION));
		pairingFlags.put("saikļa nozīmē.", Tuple.of(TKeys.CONTAMINATION, TValues.CONJUNCTION));
		pairingFlags.put("skait. nozīmē", Tuple.of(TKeys.CONTAMINATION, TValues.NUMERAL));

		pairingFlags.put("priev. nozīmē ar dat.", Tuple.of(TKeys.CONTAMINATION, TValues.PREPOSITION));
		pairingFlags.put("priev. nozīmē ar dat.", Tuple.of(TKeys.USED_TOGETHER_WITH, TValues.DATIVE));

		//binaryFlags.put("var.", "Variants"); // Izņemts no datiem.
		binaryFlags.put("hip.", "Hipotēze");
	}

}
