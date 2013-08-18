package forana.magic.printer;

import java.awt.Image;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import forana.magic.printer.image.ImageCache;
import forana.magic.printer.lookup.CardDatabase;
import forana.magic.printer.lookup.CardDatabaseManager;
import forana.magic.printer.lookup.DeckLoader;
import forana.magic.printer.model.Card;
import forana.magic.printer.model.Deck;

public class DeckApp {
	public static void main(String[] args) throws Exception {
		CardDatabase db;
		ImageCache.init();
		
		StatusReceiver ns = new NullReceiver();
		ProgressPane ps = new ProgressPane("Loading database", 400, 100);
		
		try {
			db = CardDatabaseManager.getCachedDatabase();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			db = new CardDatabase();
			JOptionPane.showMessageDialog(ps, "Error loading database - had to start a new one. Long wait ahead.");
		}
		
		CardDatabaseManager.updateDatabase(db, ps);
		
		ps.setStatus("Opening deck");
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			Deck deck = DeckLoader.loadFromTextFile(file, db);
			System.out.println(deck);
			
			List<Image> images = new ArrayList<>();
			for (Card card : deck.cards) {
				images.add(ImageCache.get(card.id, ns));
			}
			
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(new PrintablePage(images, ps));
			if (job.printDialog()) {
				job.print();
			}
		}
		
		ps.setVisible(false);
		ps.dispose();
	}
}
