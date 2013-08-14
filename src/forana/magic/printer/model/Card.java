package forana.magic.printer.model;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public final Long id;
	public final String name;
	public final String set;
	
	public Card(String name, String set, Long id) {
		this.name = name;
		this.id = id;
		this.set = set;
	}
	
	public int compareTo(Card other) {
		return this.set.compareTo(other.set);
	}
	
	@Override
	public int hashCode() {
		return (this.set + this.name).hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Card && this.name.equals(((Card)other).name) && this.set.equals(((Card)other).set);
	}
}
