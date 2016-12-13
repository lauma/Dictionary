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
		// lietv. nozīmē: piedzērušais, -ā, v.; piedzērusī, -ušas, s.
		Restrictions.nounContDouble("ušais, -ā, v.,", "usī, -ušas, s.", "ies"), // piedzēries
		// lietv. nozīmē: aizturētais, -ā, v., aizturētā, -ās, s.
		Restrictions.nounContDouble("ais, -ā, v.,", "ā, -ās, s.", "s"), // aizturēts, akls
	};

	public static final FormRestrRule[] singleLemmaWithPostGram = {
		// Bīstami, ka šīe var būt doubleLemmaWithPostGram pefiksi.
		Restrictions.nounContSimple("ais, -ā, v.", "s"), // absolūtais
		Restrictions.anyOneForm("akuz.:", "i, apst. nozīmē", "is", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.ACUSATIVE)}), // acumirklis
		Restrictions.anyOneForm("lok.:", "ā, apst. nozīmē", "a", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // apakšā
		Restrictions.anyOneForm("lok.:", "ī, apst. nozīmē", "is", null,
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // acumirklis
	};

	public static final FormRestrRule[] noPostGram = {
		// Bīstami, ka šīe var būt singleLemmaWithPostGram pefiksi.
		Restrictions.noPostGram("bieži lok.:", "ā", "s",
				new Tuple[]{Tuple.of(TKeys.OFTEN_USED_IN_FORM, TValues.LOCATIVE)}), // agrums

		Restrictions.noPostGram("s., parasti dsk.:", "is", "s",
				new Tuple[]{TFeatures.GENDER__FEM},
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE)}), // akts
		Restrictions.noPostGram("parasti lok.:", "ē", "e",
				new Tuple[]{Tuple.of(TKeys.USUALLY_USED_IN_FORM, TValues.LOCATIVE)}), // aizvārte

		Restrictions.noPostGram("ģen.:", "akmens", "akmens",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // akmens
		Restrictions.noPostGram("ģen.:", "es", "e",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // aizmugure
		Restrictions.noPostGram("lok.:", "ā", "s",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.LOCATIVE)}), // aizsegs
	};
}
