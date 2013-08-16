package forana.magic.printer;

import java.awt.Image;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import forana.magic.printer.image.ImageCache;
import forana.magic.printer.lookup.CardDatabase;
import forana.magic.printer.lookup.CardDatabaseManager;
import forana.magic.printer.lookup.DeckLoader;
import forana.magic.printer.model.Card;
import forana.magic.printer.model.Deck;

public class App {
	public static void main(String[] args) throws Exception {
		CardDatabase db;
		ImageCache.init();
		
		StatusReceiver s = new StatusReceiver() {
			public void setTotal(int dc) {}
			public void setCompleted(int dc) {}
			public void setStatus(String status) {
				System.out.println(">> " + status);
			}
		};
		
		try {
			db = CardDatabaseManager.getCachedDatabase();
			System.out.println("db loaded from cache");
		} catch (IOException e) {
			System.err.println(e.getMessage());
			db = new CardDatabase();
			System.out.println("created new db");
		}
		
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			Deck deck = DeckLoader.loadFromTextFile(file, db);
			System.out.println(deck);
			
			List<Image> images = new ArrayList<>();
			for (Card card : deck.cards) {
				images.add(ImageCache.get(card.id, s));
			}
			
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(new PrintablePage(images, s));
			if (job.printDialog()) {
				job.print();
			}
		}
	}
}
