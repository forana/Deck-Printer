package forana.magic.printer.model;

import java.util.Collection;

public class CardRow {
	public int quantity;
	public final String name;
	public final SelectableCardSource source;
	
	public CardRow(int quantity, String name, Collection<CardSource> sources) {
		this.quantity = quantity;
		this.name = name;
		this.source = new SelectableCardSource(sources);
	}
	
	public String toString() {
		return quantity + "x\t" + name + " (" + this.source.getSources().size() + " options)";
	}
}
