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

import org.json.simple.JSONObject;

/**
 * Vārda skaidrojums (šobrīd tikai teksts).
 * @author Lauma
 */
public class Gloss implements HasToJSON
{
	/**
	 * Definīcijas teksts
	 */
	public String text = null;

	public Gloss(String text)
	{
		this.text = text;
	}

	public String toJSON()
	{
		return String.format("\"Gloss\":\"%s\"", JSONObject.escape(text));
	}
}