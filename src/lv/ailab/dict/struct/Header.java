package lv.ailab.dict.struct;

import lv.ailab.dict.utils.HasToJSON;
import lv.ailab.dict.utils.HasToXML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.*;

/**
 * Šķirkļa "galva" - vārds + gramatika
 * NB! Šobrīd diemžēl ir tā, ka MLVV uzskata, ka Lemma esošais vārds ir tikai
 * atslēga, bet īstā informācija par šo vārdu glabājas altLemmās, kamēr Tēzaurs
 * arī šo ārā izvelto lemmu uzskata par pilnvērtīgu un nedublē.
 *
 * @author Lauma
 */
public class Header implements HasToJSON, HasToXML
{
	/**
	 * Vārdforma
	 */
	public Lemma lemma;
	/**
	 * Neobligāta gramatika
	 */
	public Gram gram;


	protected Header() {};

	public void reinitialize(
			GenericElementFactory factory, Lemma lemma, int paradigm, Flags flags)
	{
		this.lemma = lemma;
		gram = factory.getNewGram();
		gram.flags = flags;
		gram.paradigm = new HashSet<Integer>(){{add(paradigm);}};
	}

	public void reinitialize(
			GenericElementFactory factory, Lemma lemma, Integer[] paradigm, Flags flags)
	{
		this.lemma = lemma;
		if (paradigm != null && paradigm.length > 0 || !flags.pairings.isEmpty())
		{
			gram = factory.getNewGram();
			gram.flags = flags;
			if (paradigm != null && paradigm.length > 0)
				gram.paradigm = new HashSet<>(Arrays.asList(paradigm));
			else gram.paradigm = null;
		}
		else gram = null;
	}

	public void reinitialize(
			GenericElementFactory factory, Lemma lemma, Set<Integer> paradigm, Flags flags)
	{
		this.lemma = lemma;
		if (paradigm != null && paradigm.size() > 0 || !flags.pairings.isEmpty())
		{
			gram = factory.getNewGram();
			gram.flags = flags;
			if (paradigm != null && paradigm.size() > 0)
				gram.paradigm = new HashSet<>(paradigm);
			else gram.paradigm = null;
		}
		else gram = null;
	}

	/**
	 * Paradigmas, kas attiecas uz šajā objektā iekļauto lemmu.
	 */
	public HashSet<Integer> getDirectParadigms()
	{
		if (gram!= null) return gram.getDirectParadigms();
		return new HashSet<>();
	}

	/**
	 * Paradigmas, kas vispār ir pieminētas šajā struktūrā.
	 * Izmantojams meklēšanai un statistikai.
	 */
	public HashSet<Integer> getMentionedParadigms()
	{
		if (gram != null) return gram.getMentionedParadigms();
		return new HashSet<>();
	}

	/**
	 * Savāc iekšā gramatikā esošās altlemmas. Šis objekts pats netiek iekļauts.
	 */
	public ArrayList<Header> getImplicitHeaders()
	{
		if (gram == null) return new ArrayList<>();
		return gram.getImplicitHeaders();
	}

	public String toJSON()
	{
		StringBuilder res = new StringBuilder();

		res.append("\"Header\":{");
		// Lemma mēdz būt null iekš FormRestrictions, ja forma nav precīzi izrakstīta.
		if (lemma != null) res.append(lemma.toJSON());

		if (gram != null)
		{
			if (lemma != null) res.append(", ");
			res.append(gram.toJSON());
		}

		res.append("}");
		return res.toString();
	}

	/**
	 * Noklusētais risinājums ir drukāt visu, arī header galveno lemmu - ja nu
	 * tā ir iekļauta arī altLemmās, tad apakšklasei vajag šo pārrakstīt.
	 *
	 * @param parent DOM virsotne, kurai kā bērnu pievieno jaunizveidoto Header
	 *               virsotni.
	 */
	public void toXML(Node parent)
	{
		Document doc = parent.getOwnerDocument();
		Node headerN = doc.createElement("Header");
		if (lemma != null) lemma.toXML(headerN);
		if (gram != null) gram.toXML(headerN);
		parent.appendChild(headerN);
	}
}
