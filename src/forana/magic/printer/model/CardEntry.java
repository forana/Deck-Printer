package forana.magic.printer.model;

import java.io.Serializable;

public class CardEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final String name;
	public final CardSource source; 
	
	public CardEntry(String name, CardSource source) {
		this.name = name;
		this.source = source;
	}
}
