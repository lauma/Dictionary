package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.FormRestrRule;
import lv.ailab.dict.tezaurs.analyzer.gramlogic.shortcuts.Restrictions;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TKeys;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TValues;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * Likumi, kas apstrādā tādas gramatikas virknes kā:
 * lietv. nozīmē: aizturētais, -ā, v. aizturētā, -ās, s.
 * lietv. nozīmē: absolūtais, -ā, v.
 * akuz.: acumirkli, apst. nozīmē.
 * bieži lok.: agrumā
 * ģen.: aizmugures
 * Šie likumi rada formRestrictions Gram objektos.
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
		// lietv. nozīmē: aizturētais, -ā, v. aizturētā, -ās, s.
		Restrictions.nounContDouble("tais, -ā, v.", "tā, -ās, s.", "ts"), // aizturētais
	};

	public static final FormRestrRule[] singleLemmaWithPostGram = {
		// Bīstami, ka šīe var būt doubleLemmaWithPostGram pefiksi.
		Restrictions.nounContSimple("tais, -ā, v.", "ts"), // absolūtais
		Restrictions.anyOneForm("akuz.:", "li, apst. nozīmē.", "lis",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.ACUSATIVE)}), // acumirklis
	};

	public static final FormRestrRule[] noPostGram = {
		// Bīstami, ka šīe var būt singleLemmaWithPostGram pefiksi.
		Restrictions.noPostGram("bieži lok.:", "mā", "ms",
				new Tuple[]{Tuple.of(TKeys.OFTEN_USED_IN_FORM, TValues.LOCATIVE)}), // agrums
		Restrictions.noPostGram("ģen.:", "res", "re",
				new Tuple[]{Tuple.of(TKeys.USED_IN_FORM, TValues.GENITIVE)}), // aizmugure
	};
}
