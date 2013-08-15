package forana.magic.printer.lookup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import forana.magic.printer.model.Card;
import forana.magic.printer.model.Deck;

public class DeckLoader {
	private DeckLoader() {}
	
	public static Deck loadFromTextFile(File file, CardDatabase db) throws IOException {
		Deck deck = new Deck();
		
		BufferedReader r = new BufferedReader(new FileReader(file));
		while (r.ready()) {
			String line = r.readLine().trim();
			String[] pieces = line.split("\t", 2);
			try {
				int count = Integer.parseInt(pieces[0]);
				String name = pieces[1];
				name = name.replace("AE", "Æ");
				
				Collection<Card> cards = db.getCards(name);
				Card card = null;
				if (cards == null) {
					throw new IOException("Unable to find match for '" + name + "'");
				}
				for (Card iCard : cards) {
					card = iCard;
				}
				
				for (int i=0; i<count; i++) {
					deck.add(card);
				}
				
			} catch (NumberFormatException e) {
			}
		}
		r.close();
		
		return deck;
	}
}
