package lv.ailab.dict.mlvv.struct;

import lv.ailab.dict.struct.Header;
import java.util.TreeSet;

/**
 * Pašlaik te nav būtiski paplašināta Header funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2016-02-03.
 * @author Lauma
 */
public class MLVVHeader extends Header
{
	protected MLVVHeader(){};
	/**
	 * Savāc elementā izmantotos "flagText". Semikolu uzskata par atdalītāju.
	 */
	public TreeSet<String> getFlagStrings()
	{
		TreeSet<String> res = new TreeSet<>();
		if (gram != null) res.addAll(((MLVVGram)gram).getFlagStrings());
		return res;
	}

}
