package forana.magic.printer.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import forana.magic.printer.StatusReceiver;

public abstract class CardSource implements Comparable<CardSource>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public abstract File getImageFile(StatusReceiver sr) throws IOException;
	
	@Override
	public boolean equals(Object other) {
		return other instanceof CardSource && other.toString().equals(this.toString());
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public int compareTo(CardSource other) {
		return this.toString().compareTo(other.toString());
	}
}
