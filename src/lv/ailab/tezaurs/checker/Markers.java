package lv.ailab.tezaurs.checker;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Visi v?rdn?cas failos izmantotie "spec?lv?rdi".
 * @author Lauma
 */
public class Markers
{
    public static final Collection<String> all = Collections
            .unmodifiableCollection(new HashSet<String>(Arrays.asList(
                    "IN", "GR", "RU", "NO", "NS", "PI", "PN", "FS", "FR", "FN",
                    "FP", "DS", "DE", "DG", "AN", "DN", "CD", "LI", "NG", "AG",
                    "PG", "FG")));
    public static final String regexp = "(" + String.join("|", all) + ")";
}
