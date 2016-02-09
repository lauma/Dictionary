package lv.ailab.dict.mlvv.analyzer.struct;

import lv.ailab.dict.struct.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MLVV šķirkļa struktūra + word izguve.
 * TODO: vai origin un nevajadzētu izcelt uz Entry?
 * TODO: uzrakstīt smuku json eksportu
 * Piemēri: miers (vairāki stabilie savienojumi), māja (vairākas nozīmes frāzei)
 * mākonis (vairāki atvasinājumi)
 *
 * Izveidots 2016-02-02.
 * @author Lauma
 *
 */
public class MLVVEntry extends Entry
{
	/**
	 * MLVV atšķirībā no citām vārdnīcām tiek šķirti stabili vārdu savienojumi
	 * no frazeoloģismiem. Entry.phrases tiek lietots stabiliem vārdu
	 * savienojumiem (atdalīts ar trijstūri), bet MLVVEntry.phraseology -
	 * frazeoloģismiem (atsalīts ar rombu).
	 */
	public LinkedList<Phrase> phraseology;
	/**
	 * MLVV tiek šķirti divu veidu brīci komentāri - komentārs par vārda cilmi
	 * (izcelsmi) tiek glabāts MLVVEntry.origin, bet normatīvā lietojuma
	 * komentārs - Entry.freeText
	 */
	public String origin;

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
	 * Konstruktors, kas no dotās rindas cenšas izgūt šķirkli, pieņemot, ka tas
	 * tur noteikti ir.
	 */
	protected MLVVEntry(String line)
	{
		super();
		// Atdala šķirkļa galvu.
		String head, body;
		Matcher m1 = Pattern.compile("(<b>.*?</b>.*?)(<b>1\\.</b>.*)")
				.matcher(line);
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
				System.out
						.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
				throw new IllegalArgumentException("Can't match input line with regexp!");
			}
		}
		head = head.trim();
		body = body.trim();
		if (head.isEmpty())
			System.out
					.println("Neizdodas izgūt šķirkļa galvu no šīs rindas:\n\t" + line);
		else extractHead(head);
		if (body.isEmpty())
			System.out
					.println("Neizdodas izgūt šķirkļa ķermeni no šīs rindas:\n\t" + line);
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
			m = Pattern.compile("^<sup>\\s*<b>(.*?)</b>\\s*</sup>\\s*(.*)")
					.matcher(gram);
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
			System.out
					.printf("Neizdodas izgūt pirmo šķirkļavārdu no šī rindas fragmenta:\n%s\n",
							linePart);
			head = null;
		}
	}

	protected void extractBody(String linePart)
	{
		linePart = linePart.trim();
		// Normatīvais komentārs
		Matcher m = Pattern.compile("(.*)<gray>(.*)</gray>").matcher(linePart);
		if (m.matches())
		{
			freeText = m.group(2).trim();
			linePart = m.group(1).trim();
		}

		// Cilme
		m = Pattern.compile("(.*) [cC]ilme: (.*)").matcher(linePart);
		if (m.matches())
		{
			origin = m.group(2).trim();
			linePart = m.group(1).trim();
		}

		// Atvasinājumi
		m = Pattern.compile("(.*)<square/>(.*)").matcher(linePart);
		if (m.matches())
		{
			extractDerivs(m.group(2));
			linePart = m.group(1).trim();
		}

		// Frazeoloģismi
		m = Pattern.compile("(.*?)<diamond/>(.*)").matcher(linePart);
		if (m.matches())
		{
			extractPhraseology(m.group(2).trim());
			linePart = m.group(1).trim();
		}

		// Stabilie vārdu savienojumi
		m = Pattern.compile("(.*?)<triangle/>(.*)").matcher(linePart);
		if (m.matches())
		{
			extractStable(m.group(2).trim());
			linePart = m.group(1).trim();
		}

		if (linePart.length() > 0)
		extractSenses(linePart);

	}

	/**
	 * TODO vai viena gramatika var attiekties uz vairākām lemmām?
	 */
	protected void extractDerivs(String linePart)
	{
		String[] derivTexts = linePart.split("(?=<b>)");
		if (derivTexts.length < 1) return;
		derivs = new LinkedList<>();
		for (String dt :derivTexts)
		{
			MLVVHeader h = MLVVHeader.extractSingularHeader(dt.trim());
			if (h != null) derivs.add(h);
		}
	}

	/**
	 * TODO gramatika pie frāzes
	 * TODO a. nozīme. b. nozīme.
	 */
	protected void extractPhraseology(String linePart)
	{
		String[] phrasesParts = linePart.split("<diamond/>");
		if (phrasesParts.length > 0)  phrases = new LinkedList<>();
		for (String phraseText : phrasesParts)
		{
			phraseText = phraseText.trim();
			Phrase p = new Phrase();
			Matcher m = Pattern.compile("(.*?)[\\-\u2014\u2013]\\s*(.*)").matcher(phraseText);
			if (!m.matches())
				System.out.printf("Neizdodas izanalizēt frazeoloģismu \"%s\"\n", phraseText);
			else
			{
				p.text = m.group(1);
				p.subsenses = new LinkedList<>();
				Sense pSense = new Sense();
				String[] senseTexts = m.group(2).trim().split("(?=<i>)");
				for (String t : senseTexts)
				{
					if (t.contains("<i>"))
					{
						String gramText = t.substring(0, t.indexOf("</i>")).trim();
						if (gramText.startsWith("<i>")) gramText = gramText.substring(3);
						if (gramText.contains("<i>") || gramText.contains("</i>"))
							System.out.printf("Frazeoloģismā \"%s\" gramatikā paliek i tagi\n", phraseText);
						t = t.substring(t.indexOf("</i>") + 4).trim();
						p.grammar = new Gram();
						p.grammar.freeText = gramText;

					}
					pSense.gloss = new Gloss(t);
					p.subsenses.add(pSense);
					if (t.contains("<i>") || t.contains("</i>"))
						System.out.printf("Frazeoloģismā \"%s\" skaidrojumā paliek i tagi\n", phraseText);
				}
				phrases.add(p);
			}
		}
	}
	protected void extractStable(String linePart)
	{
		// TODO
	}
	protected void extractSenses(String linePart)
	{
		// TODO
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
} */

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
				System.out
						.printf("Šķirklim \"%s\" šķirkļavārdam ir norādīta izruna, lai gan šo lauku MLVV nav paredzēts aizpildīt!\n",
								head.lemma.text != null ? head.lemma.text : "");
				head.toXML(entryN);
			} else if (head.gram != null)
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
	 * Override, lai šķirkļa saturs saturētu visu vajadzīgo.
	 */
	@Override
	public void contentsToXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		if (senses != null && !senses.isEmpty())
		{
			Node sensesContN = doc.createElement("senses");
			for (Sense s : senses) s.toXML(sensesContN);
			parent.appendChild(sensesContN);
		}
		if (phrases != null && !phrases.isEmpty())
		{
			Node phrasesContN = doc.createElement("stablePhrases");
			for (Phrase p : phrases) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}
		if (phraseology != null && !phraseology.isEmpty())
		{
			Node phrasesContN = doc.createElement("phraseology");
			for (Phrase p : phraseology) p.toXML(phrasesContN);
			parent.appendChild(phrasesContN);
		}
		if (derivs != null && !derivs.isEmpty())
		{
			Node derivContN = doc.createElement("derivatives");
			for (Header d : derivs) d.toXML(derivContN);
			parent.appendChild(derivContN);
		}
		if (reference != null && reference.length() > 0)
		{
			System.out
					.printf("Šķirklim \"%s\" norādītas atsauces, lai gan MLVV šo lauku nav paredzēts aizpildīt!\n",
							head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
			Node refN = doc.createElement("reference");
			refN.appendChild(doc.createTextNode(reference));
			parent.appendChild(refN);
		}
		if (origin != null && origin.length() > 0)
		{
			Node originN = doc.createElement("origin");
			originN.appendChild(doc.createTextNode(origin));
			parent.appendChild(originN);
		}
		if (freeText != null && freeText.length() > 0)
		{
			Node freeTextN = doc.createElement("normative");
			freeTextN.appendChild(doc.createTextNode(freeText));
			parent.appendChild(freeTextN);
		}
		if (sources != null && !sources.isEmpty())
		{
			System.out
					.printf("Šķirklim \"%s\" norādīti avoti, lai gan MLVV šo lauku nav paredzēts aizpildīt!\n",
							head != null && head.lemma != null && head.lemma.text != null ? head.lemma.text : "");
			sources.toXML(parent);
		}

	}
}