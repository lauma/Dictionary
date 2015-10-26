package lv.ailab.tezaurs.analyzer.gramdata;

import lv.ailab.tezaurs.analyzer.flagconst.Features;
import lv.ailab.tezaurs.analyzer.flagconst.Keys;
import lv.ailab.tezaurs.analyzer.flagconst.Values;
import lv.ailab.tezaurs.analyzer.struct.Flags;

/**
 * Te būs likumi, ko neizdodas izlikt smukajos, parametrizējamajos Rule
 * objektos, bet tā vietā tie palikuši kā atsevišķas funkcijas.
 * Created on 2015-10-22.
 *
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
			Keys usedInFormFrequency)
	{
		if (usedInFormFrequency != Keys.USUALLY_USED_IN_FORM &&
				usedInFormFrequency != Keys.OFTEN_USED_IN_FORM &&
				usedInFormFrequency != Keys.USED_ONLY_IN_FORM &&
				usedInFormFrequency != Keys.ALSO_USED_IN_FORM &&
				usedInFormFrequency != Keys.USED_IN_FORM)
			throw new IllegalArgumentException();

		Values partType = null;

		if (form.endsWith("damies") || form.endsWith("dams")) //aizvilkties->aizvilkdamies
			partType = Values.PARTICIPLE_DAMS;
		else if (form.endsWith("ams") || form.endsWith("āms"))
			partType = Values.PARTICIPLE_AMS;
		else if (form.endsWith("ts")) // aizdzert->aizdzerts
			partType = Values.PARTICIPLE_TS;
		else if (form.endsWith("is") || form.endsWith("ies")) // aizmakt->aizsmacis, pieriesties->pieriesies
			partType = Values.PARTICIPLE_IS;
		else
			return false;

		overallFlagCollector.add(Features.POS__VERB);
		overallFlagCollector.add(usedInFormFrequency, Values.PARTICIPLE.s);
		overallFlagCollector.add(usedInFormFrequency, partType);
		overallFlagCollector.add(usedInFormFrequency, "\"" + form  + "\"");

		specificFlagCollector.add(Keys.POS, partType);
		specificFlagCollector.add(Features.POS__PARTICIPLE);

		return true;
	}
}
