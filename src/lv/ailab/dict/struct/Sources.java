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

import java.util.LinkedList;
import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import lv.ailab.dict.utils.JSONUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.print.Doc;

/**
 * Avotu saraksts.
 * @author Lauma
 */
public class Sources implements HasToJSON, HasToXML
{
	public String orig;
	public LinkedList<String> s;

	public Sources()
	{
		orig = null;
		s = null;
	}

	public boolean isEmpty()
	{
		return s == null || s.isEmpty();
	}

	// In case of speed problems StringBuilder can be returned.
	public String toJSON()
	{
		StringBuilder res = new StringBuilder();
		if (s != null && s.size() > 0)
		{
			res.append("\"Sources\":");
			res.append(JSONUtils.simplesToJSON(s));
		}
		return res.toString();
	}

	/**
	 * Oriģināltekstu neiekļauj.
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (s != null && s.size() > 0)
		{
			Node containerN = doc.createElement("Sources");
			for (String source : s)
			{
				Node sourceN = doc.createElement("Source");
				sourceN.appendChild(doc.createTextNode(source));
				containerN.appendChild(sourceN);
			}
			parent.appendChild(containerN);
		}
	}
}