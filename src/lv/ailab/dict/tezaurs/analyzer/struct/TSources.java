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
package lv.ailab.dict.tezaurs.analyzer.struct;

import java.util.Arrays;
import java.util.LinkedList;
import org.w3c.dom.Node;

import lv.ailab.dict.struct.Sources;

/**
 * Avotu saraksts (lauks avots Tēzaura XML).
 */
public class TSources extends Sources
{
	public String orig;

	public TSources(Node avotsNode)
	{
		orig = avotsNode.getTextContent();
		s = parseSources(orig);
		if (s.size() < 1 && orig.length() > 0)
			System.err.printf(
				"Field 'sources' '%s' can't be parsed!\n", orig);
	}

	/**
	 *  Izvelk avotus no teksta un pārbauda, vai šis lauks jau nav ticis
	 *  aizpildīts iepriekš.
	 */
	public void set(String sourcesText)
	{
		if (orig != null || s != null)
		{
			System.err.printf(
				"Duplicate info for field 'sources' : '%s' and '%s'!\n", orig, sourcesText);
		}
		orig = sourcesText;
		s = parseSources(sourcesText);
		if (s.size() < 1 && orig.length() > 0)
			System.err.printf(
				"Field 'sources' '%s' can't be parsed!\n", orig);
	}

	private static LinkedList<String> parseSources (String sourcesText)
	{
		if (sourcesText.startsWith("["))
			sourcesText = sourcesText.substring(1);
		if (sourcesText.endsWith("]"))
			sourcesText = sourcesText.substring(0, sourcesText.length() - 1);
		
		LinkedList<String> res = new LinkedList<>();
		res.addAll(Arrays.asList(sourcesText.split(",\\s*")));
		return res;
	}
}