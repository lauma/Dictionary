package lv.ailab.dict.tezaurs.analyzer.gramdata;

import lv.ailab.dict.tezaurs.analyzer.gramlogic.FormRestrRule;
import lv.ailab.dict.tezaurs.analyzer.struct.flagconst.TFeatures;
import lv.ailab.dict.utils.Tuple;

import java.util.ArrayList;

/**
 * Likumi, kas apstrādā tādas gramatikas virknes kā:
 * lietv. nozīmē: absolūtais, -ā, v.
 * lietv. nozīmē: aizturētais, -ā, v. aizturētā, -ās, s.
 * akuz.: acumirkli, apst. nozīmē.
 * bieži lok.: agrumā
 * ģen.: aizmugures
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
		ArrayList<FormRestrRule[]> res = new ArrayList<>(1);
		res.add(other);
		return res;
	}

	public static final FormRestrRule[] other = {
		FormRestrRule.of("lietv. nozīmē: ", "tais, -ā, v.", ".*ts", 2, null,
				"tais", new Tuple[] {TFeatures.CONTAMINATION__NOUN}), // absolūtais
	};
}
