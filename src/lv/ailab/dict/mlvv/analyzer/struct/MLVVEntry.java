package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.Entry;
import lv.ailab.dict.struct.Lemma;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Šķirkļa izgūšana no specifiska ieejas formāta (Word eksporti).
 * Izveidots 2016-02-02.
 * @author Lauma
 */
public class MLVVEntry extends Entry
{

	/**
	 * Izanalizē rindu, un atgriež null, ja tajā nekā nav, vai MLVVEntry, ja no
	 * šīs rindas tādu var izgūt.
	 */
	public static MLVVEntry extractFromString(String line)
	{
		if (line == null || line.isEmpty()) return null;

		// Ja rinda satur tikai burtu, neizgūst neko.
		if (line.matches("<b>\\p{L}\\p{M}*</b>")) return null;

		else return new MLVVEntry(line);
	}

	/**
	 * Override, lai MLVV šķirkļos nedrukā galvenā header lemmu, jo tā jau ir
	 * iekļauta altLemmu sarakstā.
	 */
	@Override
	public Element toXML(Document doc)
	{
		Element entryN = doc.createElement("entry");

		if (homId != null)
			entryN.setAttribute("ID", homId);
		if (head != null)
		{
			//head.toXML(entryN);
			if (head.lemma != null && head.lemma.text != null)
				entryN.setAttribute("LemmaSign", head.lemma.text);
			if (head.lemma != null && head.lemma.pronunciation != null && head.lemma.pronunciation.length > 0)
			{
				System.out.printf("Šķirklim \"%s\" šķirkļavārdam ir norādīta izruna, lai gan šo lauku MLVV nav paredzēts aizpildīt!",
						head.lemma.text != null ? head.lemma.text : "");
				head.toXML(entryN);
			}
			else if (head.gram != null)
			{
				Node headerN = doc.createElement("header");
				head.gram.toXML(headerN);
				entryN.appendChild(headerN);
			}
		}
		contentsToXML(entryN);

		return entryN;
	}

	/**
	 * Konstruktors, kas no dotās rindas cenšas izgūt šķirkli, pieņemot, ka tas
	 * tur noteikti ir.
	 */
	protected MLVVEntry(String line)
	{
		super();
		// Atdala šķirkļa galvu.
		String head, body;
		Matcher m1 = Pattern.compile("(<b>.*?</b>.*?)(<b>1\\.</b>.*)").matcher(line);
		if (m1.matches()) // Šķirklis ar numurētām nozīmēm
		{
			head = m1.group(1);
			body = m1.group(2);
		} else // Šķirklis ar vienu nozīmi.
		{
			Matcher m2 = Pattern.compile("(<b>.*?</b>\\s*)(.*)").matcher(line);
			if (m2.matches())
			{
				head = m2.group(1);
				body = m2.group(2);
				Pattern headpart = Pattern.compile(
						"((?:<b>.*?</b>|<i>.*?</i>|<sup>.*?</sup>|\\[.*?\\]|(?:[-,].*?(?=<)))\\s*)(.*)");
				m2 = headpart.matcher(body);
				while (m2.matches())
				{
					head = head + m2.group(1);
					body = m2.group(2);
					m2 = headpart.matcher(body);
				}
			} else
			{
				System.out.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
				throw new IllegalArgumentException("Can't match input line with regexp!");
			}
		}
		head = head.trim();
		body = body.trim();
		if (head.isEmpty())
			System.out.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
		else extractHead(head);
		if (body.isEmpty())
			System.out.println("Neizdodas izgūt šķirkļa ķermeni no šīs rindas:\n\t" + line);
		else this.extractBody(body);
	}

	/**
	 * TODO karodziņi
	 * TODO izrunas
	 */
	protected void extractHead(String linePart)
	{
		head = new MLVVHeader();
		Matcher m = Pattern.compile("<b>(.+?)</b>\\s*(.*)").matcher(linePart);
		if (m.matches())
		{
			head.lemma = new Lemma(m.group(1));
			String gram = m.group(2);
			// Homonīma infekss, ja tāds ir.
			m = Pattern.compile("^<sup>\\s*<b>(.*?)</b>\\s*</sup>\\s*(.*)").matcher(gram);
			if (m.matches())
			{
				if (homId != null) System.out.printf(
							"No šķirkļavārda \"%s\" homonīma indeksu mēģina piešķiert vairākkārt: vispirms %s, tad %s!",
							head.lemma, homId, m.group(1));
				homId = m.group(1);
			}
			head.gram = MLVVGram.extractFromString(linePart);
		} else
		{
			System.out.printf("Neizdodas izgūt pirmo šķirkļavārdu no šī rindas fragmenta:\n%s\n",
					linePart);
			head = null;
		}


		// Šitā tas strādāja, kad pirmo šķirkļa vārdu nelika pie alternatīvajām
		// lemmām.
/*		Header res = new Header();
		Matcher m = Pattern.compile("<b>(.+?)</b>\\s*(.*)").matcher(linePart);
		if (m.matches())
		{
			res.lemma = new Lemma(m.group(1));
			String gram = m.group(2);
			// Homonīma infekss, ja tāds ir.
			m = Pattern.compile("^<sup>\\s*<b>(.*?)</b>\\s*</sup>\\s*(.*)").matcher(gram);
			if (m.matches())
			{
				if (homId != null)
					System.out.printf(
							"No šķirkļavārda \"%s\" homonīma indeksu mēģina piešķiert vairākkārt: vispirms %s, tad %s!",
							res.lemma, homId, m.group(1));
				homId = m.group(1);
				gram = m.group(2);
			}
			// Izruna, ja tāda ir.
			m = Pattern.compile("\\[(.*?)\\]\\s*(.*)").matcher(gram);
			if (m.matches())
			{
				res.lemma.pronunciation = new String[]{m.group(1)};
				// TODO sadalīt izrunas sīkāk
				gram = m.group(2);
			}

			if (!gram.isEmpty() && res.gram == null) res.gram = new Gram();
			if (gram.contains("<b>")) // ir alternatīvās vārdformas, atvasinājumi
			{
				// Šādi sadalās tās daļas, kur uz vairākām vārdformām ir viena
				// vārdšķira.
				String[] bigParts = new String[]{gram};
				if (gram.contains(";")) bigParts = gram.split(";\\s*");
				for (int i = 0; i < bigParts.length; i++)
				{

					if (bigParts[i].contains("<b>"))
					{
						int commonStart = bigParts[i].lastIndexOf("<i>");
						// Ja neatrod kopīgo daļu, tad ir šitā:
						String common = "";
						String forSplit = bigParts[i];
						if (commonStart > -1)
						{
							common = bigParts[i].substring(commonStart);
							forSplit = bigParts[i].substring(0, commonStart);
						}
						ArrayList<String> smallParts = new ArrayList<>(Arrays.asList(
								forSplit.split("(?<!\\s)\\s*(?=<b>)")));

						// Pirmā gramatika iet pie šķirkļa vārda, nevis altLemmām.
						if (i == 0)
						{
							String hwGram = smallParts.get(0).trim();
							if (hwGram.equals(",") || hwGram.isEmpty()) hwGram = common;
							else
							{
								if (!hwGram.endsWith(",")) hwGram = hwGram + ",";
								hwGram = hwGram + " " + common;
							}
							if (res.gram.freeText == null) res.gram.freeText = "";
							else if (!res.gram.freeText.trim().isEmpty() && !res.gram.freeText.trim().endsWith(","))
								res.gram.freeText = res.gram.freeText.trim() + ", ";
							res.gram.freeText += hwGram;
							smallParts.remove(0);
						}
						// No pārējiem gabaliem veido altLemmas.
						if (smallParts.size() > 0)
						{
							res.gram.altLemmas = new ArrayList<>();
							for (String smallPart : smallParts)
							{
								smallPart = smallPart.trim();
								if (smallPart.matches(".*</b>\\s*,"))
									smallPart = smallPart.substring(0, smallPart.length()-1).trim();
								else if (!smallPart.endsWith(",")) smallPart += ",";
								smallPart = smallPart + " " + common;
								res.gram.altLemmas.add(extractHead(smallPart));
							}
						}
					} else
					{
						if (i == 0)
						{
							if (res.gram.freeText == null) res.gram.freeText = "";
							else if (!res.gram.freeText.trim().isEmpty()) res.gram.freeText += ", ";
							res.gram.freeText += bigParts[0];
						}
						else // Elementi, kas attiecināmi uz visām altLemmām un arī uz pamata šķirkļa vārdu.
						{
							//System.out.printf(
							//		"Neizdodas saprast, kam pieder gramatikas daļa \"%s\" šķirklī \"%s\"!",
							//		bigParts[i], res.lemma.text);
							if (res.gram.freeText == null) res.gram.freeText = "";
							else if (!res.gram.freeText.trim().isEmpty()) res.gram.freeText += "; ";
							res.gram.freeText += bigParts[i];
						}
					}
				}
				// TODO
			}
			else // citu vārdformu nav
			{
				res.gram = new Gram();
				res.gram.freeText = gram;
			}

			return res;
		} else
			System.out.printf("Neizdodas izgūt pirmo šķirkļavārdu no šī rindas fragmenta šķirklī \"%s\":\n%s\n",
					this.head == null || this.head.lemma.text == null ? "" : this.head.lemma.text,
					linePart);
		return null; //*/
	}

	protected void extractBody (String linePart)
	{

	}


}

/*

# Annotate entry body (word senses + etymology + references + usage advices + phraseology).
sub _annotateBody
{
	my $str = shift;
	my $res = "\t\t<body>\n";

	# Seperate word senses from other stuff like etymology.
	my ($senses, $nonsenses) = ($str, '');
	if ($str =~ m#^(.*?)\s*((<square/>|<triangle/>|<diamond/>|<extended>[Cc]ilme</extended>|<gray>...+?</gray>).*)$#)
	{
		$senses = $1;
		$nonsenses = $2;
	}

	# Annotate word senses.
	while ($senses =~ m#^(.+?)(<b>\d+\.</b>.*)$#)
	{
		$senses = $2;
		$res .= &_annotateSense($1);
	}
	$res .= &_annotateSense($senses) if ($senses and $senses !~ /^\s*$/);

	warn "In \"$currentHW\" word sense after <square/>, <triangle/>, Cilme or <gray> found!"
		if ($nonsenses =~ m#<b>\d+\.</b>.#);

	# Annotate stuff after word senses.
	while ($nonsenses and
		$nonsenses =~ m#^(<square/>|<triangle/>|<diamond/>|<extended>[Cc]ilme</extended>|<gray>)(.*)$#)
	{
		my $tag = $1;
		if ($tag eq '<gray>')
		{
			$nonsenses =~ m#^<gray>(...+?)</gray>\s*(.*)$#;
			$nonsenses = $2;
			$res .= "\t\t\t<norm>$1</norm>\n";
		} else
		{
			$nonsenses =~ m#^$tag\s*(.*?)\s*((<square/>|<triangle/>|<diamond/>|<extended>[Cc]ilme</extended>|<gray>).*)?$#;
			$nonsenses = $2;
			if ($tag eq '<square/>')
			{
				$res .= "\t\t\t<deriv>$1</deriv>\n";
			} elsif ($tag eq '<triangle/>')
			{
				$res .= "\t\t\t<wgroup>$1</wgroup>\n";
			} elsif ($tag eq '<diamond/>')
			{
				$res .= "\t\t\t<phras>$1</phras>\n";
			} else
			{
				$1 =~ /^:?\s*(.*)$/;
				$res .= "\t\t\t<etym>$1</etym>\n";
			}
		}
	}

	$res .= "\t\t</body>\n";

	# Annotating current entry has been finished, so no current headword is available.
	# For debuging purposes only!
	$currentHW = undef;

	return $res;
}

sub _annotateSense
{
	my $str = shift;

	my $res = "\t\t\t<sense";
	# Process number of current sense.
	my $sense = $str;
	if ($str =~ m#^<b>(\d+)\.</b>\s*(.*)$#)
	{
		$res .= " id=\"$1\"";
		$sense  = $2;
	}
	$res .= ">";

	$res .= $sense;
	$res .= "</sense>\n";
	return $res;
}


 */