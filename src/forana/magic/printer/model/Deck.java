package forana.magic.printer.model;

import java.util.Collection;
import java.util.LinkedList;

public class Deck {
	public final Collection<Card> cards;
	
	public Deck() {
		this(new LinkedList<Card>());
	}
	
	public Deck(Collection<Card> cards) {
		this.cards = cards;
	}
}
