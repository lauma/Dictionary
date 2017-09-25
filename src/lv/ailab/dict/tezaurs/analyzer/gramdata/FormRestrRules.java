package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.FormRestrRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.Restrictions;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.Tuple;
import org.apache.poi.ss.usermodel.RichTextString;

import java.util.ArrayList;

/**
 * Likumi, kas apstrādā tādas gramatikas virknes kā:
 * lietv. nozīmē: aizturētais, -ā, v. aizturētā, -ās, s.
 * lietv. nozīmē: absolūtais, -ā, v.
 * akuz.: acumirkli, apst. nozīmē.
 * bieži lok.: agrumā
 * ģen.: aizmugures
 * Šie likumi rada formRestrictions Gram objektos.
 * NB! šie likumi rekursīvi izsauc apaksvirkņu analīzi.
 * TODO: Vai kontamināciju, kas atrodas aiz, nevajag izcelt pie pamata karodziņiem?
 *
 * Izveidots 2016-10-26.
 * @author Lauma
 */
public class FormRestrRules
{
	/**
	 * Metode visu klasē iekļauto likumu iegūšanai pareizā secībā.
	 * @return saraksts ar likumu blokiem.
	 */
	public static ArrayList<FormRestrRule[]> getAll()
	{
		ArrayList<FormRestrRule[]> res = new ArrayList<>(3);
		res.add(doubleLemmaWithPostGram);
		res.add(singleLemmaWithPostGram);
		res.add(noPostGram);
		return res;
	}

	public static final FormRestrRule[] doubleLemmaWithPostGram = {
		// lietv. nozīmē: piedzērušais, -ā, v., piedzērusī, -ušas, s.
		Restrictions.nounContDouble("ušais, -ā, v.,", "usī, -ušas, s.", "ies"), // piedzēries
		// lietv. nozīmē: beidzamais, -ā, v., beidzamā, -ās, s.
		Restrictions.nounContDouble("ais, -ā, v.,", "ā, -ās, s.", "ais"), // beidzamais
		// lietv. nozīmē: aizturētais, -ā, v., aizturētā, -ās, s.
		Restrictions.nounContDouble("ais, -ā, v.,", "ā, -ās, s.", "s"), // aizturēts, akls
		Restrictions.nounContDouble("ais, -ā, v.", "ā, -ās, s.", "s"), // balts

	};

	public static final FormRestrRule[] singleLemmaWithPostGram = {
		// Bīstami, ka šīe var būt doubleLemmaWithPostGram pefiksi.
		Restrictions.nounContSimple("ais, -ā, v.", "s"), // absolūtais
		Restrictions.anyOneForm("akuz.:", "i, apst. nozīmē", "is", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.ACUSATIVE)}), // acumirklis
		Restrictions.anyOneForm("akuz.:", "u, apst. nozīmē", "a", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.ACUSATIVE)}), // diena
		Restrictions.anyOneForm("ģen.:", "a, adj. nozīmē.", "s", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // bezspēks
		Restrictions.anyOneForm("ģen.:", "u, adj. nozīmē.", "as", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // bruņas
		Restrictions.anyOneForm("ģen.:", "a, adj. nozīmē", "s", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // caurmērs
		Restrictions.anyOneForm("ģen.:", "a, adj. nozīmē.", "š", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // bezspēks
		Restrictions.anyOneForm("ģen.:", "a, adj. nozīmē.", "is", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // brāķis
		Restrictions.anyOneForm("ģen.:", "as, adj. nozīmē.", "a", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // diena
		Restrictions.anyOneForm("instr.:", "iem, apst. nozīmē", "i", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.INSTRUMENTAL)}), // aplinki
		Restrictions.anyOneForm("lok.:", "ā, apst. nozīmē", "a", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // apakšā
		Restrictions.anyOneForm("lok.:", "ā, apst. nozīmē", "s", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // ātrums
		Restrictions.anyOneForm("lok.:", "ās, apst. nozīmē", "as", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // beigas
		Restrictions.anyOneForm("lok.:", "ē, apst. nozīmē", "e", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // ārpuse
		Restrictions.anyOneForm("lok.:", "ī, apst. nozīmē", "is", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // acumirklis

		Restrictions.anyOneForm("parasti lok.:", "ā, apst. nozīmē.", "a", null,
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE)}), // daba
		Restrictions.anyOneForm("parasti lok.:", "ā, apst. nozīmē.", "s", null,
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE)}), // čumurs

		Restrictions.anyOneForm("dsk. ģen.:", "u, adj. nozīmē.", "a", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL),
						Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // bura
		Restrictions.anyOneForm("dsk. ģen.:", "u, adj. nozīmē", "a", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL),
						Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // dāmu
		Restrictions.anyOneForm("dsk. ģen.:", "u, adj. nozīmē.", "as", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL),
						Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // briesmas
		Restrictions.anyOneForm("dsk. ģen.:", "u, adj. nozīmē", "e", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL),
						Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // atspere
		Restrictions.anyOneForm("dsk. ģen.:", "vju, adj. nozīmē", "vis", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL),
						Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // burvis

		Restrictions.anyOneForm("dsk. lok.:", "ās, apst. nozīmē.", "a", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.PLURAL),
						Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // diena

	};

	public static final FormRestrRule[] noPostGram = {
		// Bīstami, ka šīe var būt singleLemmaWithPostGram pefiksi.
		Restrictions.noPostGram("bieži lok.:", "ā", "s",
				new Tuple[]{Tuple.of(TKeys.OFTEN_USED_IN_FORM, TValues.LOCATIVE)}), // agrums

		Restrictions.noPostGram("s., parasti dsk.:", "is", "s",
				new Tuple[]{TFeatures.GENDER__FEM},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE)}), // akts
		Restrictions.noPostGram("parasti lok.:", "ā", "s",
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE)}), // atstatums
		Restrictions.noPostGram("parasti lok.:", "ē", "e",
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE)}), // aizvārte

		Restrictions.noPostGram("parasti dsk.:", "as", "a",
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL)}), // basņas
		Restrictions.noPostGram("parasti dsk.:", "es", "e",
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL)}), // banānene
		Restrictions.noPostGram("parasti dsk.:", "i", "is",
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.PLURAL)}), // bamperis

		Restrictions.noPostGram("ģen.:", "akmens", "akmens",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // akmens
		Restrictions.noPostGram("ģen.:", "es", "e",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // aizmugure
		Restrictions.noPostGram("ģen.:", "a", "s",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // agrums
		Restrictions.noPostGram("ģen.:", "u", "as",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // atvadas
		Restrictions.noPostGram("dat.:", "ām", "as",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.DATIVE)}), // atvadas
		Restrictions.noPostGram("vsk. ģen.:", "a", "s",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE), Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR)}), // ārs
		Restrictions.noPostGram("lok.:", "ā", "a",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // augša
		Restrictions.noPostGram("lok.:", "ā", "s",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // aizsegs
		Restrictions.noPostGram("vsk. lok.:", "ā", "a",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE), Tuple.of(TKeys.USED_IN_FORM, TValues.SINGULAR)}), // atšķirība

		Restrictions.noPostGram("pārākajā pakāpē:", "āk", "i",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.COMPARATIVE_DEGREE)}), // ātri

		Restrictions.noPostGram("pavēles formā:", "šujies!", "šūties",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.IMPERATIVE)}), // atšūties
	};
}
