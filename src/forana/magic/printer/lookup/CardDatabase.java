package forana.magic.printer.lookup;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import forana.magic.printer.model.Card;

public class CardDatabase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Collection<Card>> cardMap;
	private Collection<String> knownSetNames;
	
	public CardDatabase() {
		this.cardMap = new HashMap<>();
		this.knownSetNames = new HashSet<>();
	}
	
	public void add(Card card) {
		if (!this.cardMap.containsKey(card.name)) {
			this.cardMap.put(card.name, new TreeSet<Card>());
		}
		this.cardMap.get(card.name).add(card);
	}
	
	public Collection<Card> getIds (String name) {
		return this.cardMap.get(name);
	}
	
	public boolean knowsAbout(String set) {
		return this.knownSetNames.contains(set);
	}
	
	public void addKnownSet(String set) {
		this.knownSetNames.add(set);
	}
}
