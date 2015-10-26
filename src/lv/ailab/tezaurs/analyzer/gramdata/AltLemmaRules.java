package lv.ailab.tezaurs.analyzer.gramdata;

import lv.ailab.tezaurs.analyzer.gramlogic.AltLemmaRule;
import lv.ailab.tezaurs.analyzer.gramlogic.AltFullLemmaRule;

/**
 * /**
 * Gramatiku apstrādes likumi. Lasāmības labad izdalīti atsevišķi no
 * Gram.processBeginingWithPatterns(String, String)
 * Likumi, kas veido alternatīvās formas.
 * Izveidots 2015-10-26.
 *
 * @author Lauma
 */
public class AltLemmaRules
{
	/**
	 * Likumi formā:
	 * -ņu, vsk. dižtauriņš, -ņa, v.
	 * -ņu, vsk. aizvirtnis, -ņa, v.
	 */
	public static final AltLemmaRule[] pluralToSingular = {
		// 2. paradigma: Lietvārds 1. deklinācija -š
		AltFullLemmaRule.pluralToSingularMasc("-ņu, vsk.", "ņš, -ņa, v.", "ņi", 2), // dižtauriņi
		// 3. paradigma: Lietvārds 2. deklinācija -is
		AltFullLemmaRule.pluralToSingularMasc("-ņu, vsk.", "nis, -ņa, v.", "ņi", 3), // aizvirtņi
		AltFullLemmaRule.pluralToSingularMasc("-ņu, vsk.", "lnis, -ļņa, v.", "ļņi", 3), // starpviļņi
		AltFullLemmaRule.pluralToSingularMasc("-u, vsk.", "jis, -ja, v.", "ji", 3), // airkāji
		// 1. paradigma: Lietvārds 1. deklinācija -s
		AltFullLemmaRule.pluralToSingularMasc("-u, vsk.", "s, -a, v.", "i", 1), // aizkars
	};

}
