package lv.ailab.dict.utils;

import org.w3c.dom.Node;

/**
 * Created on 2016-02-02.
 *
 * @author Lauma
 */
public interface HasToXML
{
	/**
	 * Attiecīgā klase prot visu būtisko saturu pārtasīt par DOM nodēm un
	 * sakabināt zem dotās vecāknodes.
	 */
	void toXML(Node parent);
}
