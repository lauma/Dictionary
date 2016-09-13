/*******************************************************************************
 * Copyright 2013-2016 Institute of Mathematics and Computer Science, University of Latvia
 * Author: Lauma Pretkalniņa
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package lv.ailab.dict.struct;

import lv.ailab.dict.utils.HasToJSON;

import lv.ailab.dict.utils.HasToXML;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Vārda skaidrojums (šobrīd tikai teksts).
 * @author Lauma
 */
public class Gloss implements HasToJSON, HasToXML
{
	/**
	 * Definīcijas teksts
	 */
	public String text = null;

	public Gloss(String text)
	{
		this.text = text;
	}

	/**
	 * Gloss ir viens no retajiem elementiem, ko drukā arī, ja tas ir tukšs.
	 * TODO vai tā ir pareizi?
	 */
	public String toJSON()
	{
		if (text == null)
			return String.format("\"Gloss\":\"\"");
		return String.format("\"Gloss\":\"%s\"", JSONObject.escape(text));
	}

	/**
	 * Gloss ir viens no retajiem elementiem, ko drukā arī, ja tas ir tukšs.
	 * TODO vai tā ir pareizi?
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node glossN = doc.createElement("Gloss");
		if (text != null) glossN.appendChild(doc.createTextNode(text));
		parent.appendChild(glossN);
	}
}