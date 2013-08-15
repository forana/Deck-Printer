package forana.magic.printer;

import java.io.File;

import javax.swing.JFileChooser;

import forana.magic.printer.lookup.CardDatabase;
import forana.magic.printer.lookup.CardDatabaseManager;
import forana.magic.printer.lookup.DeckLoader;
import forana.magic.printer.model.Deck;

public class DeckLoadTest {
	public static void main(String[] args) throws Exception {
		CardDatabase db = CardDatabaseManager.getCachedDatabase();
		
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			Deck deck = DeckLoader.loadFromTextFile(file, db);
			System.out.println(deck);
		}
	}
}
