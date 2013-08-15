package forana.magic.printer.model;

import java.util.Collection;
import java.util.LinkedList;

public class Deck {
	public final Collection<Card> cards;
	
	public Deck() {
		this.cards = new LinkedList<>();
	}
	
	public void add(Card card) {
		this.cards.add(card);
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Card card : this.cards) {
			s.append(card.toString());
			s.append("\n");
		}
		return s.toString();
	}
}
