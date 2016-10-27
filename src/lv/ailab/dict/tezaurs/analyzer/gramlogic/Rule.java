package lv.ailab.dict.tezaurs.analyzer.gramlogic;

/**
 * Vispārīgais likumu interfeiss: likumiem var paprasīt, cik reižu tie ir tikuši
 * lietoti un kā tos sauc. apply() šeit nav pievienots tāpēc, ka pa realizācijām
 * atšķiras nepieciešamie parametri. Nākotnē to varētu mainīt.
 *
 * Izveidots 2016-10-21.
 * @author Lauma
 */
public interface Rule
{
	/**
	 * Metode, kas ļauj uzzināt, cik reižu likums ir lietots.
	 * @return skaits, cik reižu likums ir lietots.
	 */
	int getUsageCount();

	/**
	 * Metode, kas ļauj dabūt likuma nosaukumu, kas ļautu šo likumu atšķirt no
	 * citiem. Iesakāmā realizācija satur likumā izmantoto šablonu un, ja vajag,
	 * arī galotņu nosacījumus, klases vārdu, utt.
	 * @return likuma vienkāršota reprezentācija, kas izmantojama diagnostikas
	 * izdrukās.
	 */
	String getStrReprezentation();

}
