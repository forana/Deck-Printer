package forana.magic.printer.lookup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import forana.magic.printer.model.CardRow;
import forana.magic.printer.model.CardSource;

public class ListLoader {
	private ListLoader() {}
	
	public static List<CardRow> loadFromTextFile(File file, CardDatabase db) throws IOException {
		List<CardRow> rows = new LinkedList<>();
		
		BufferedReader r = new BufferedReader(new FileReader(file));
		try {
			while (r.ready()) {
				String line = r.readLine().trim();
				if (!line.equals("")) {
					String[] pieces = line.split("x?\\s+", 2);
					if (pieces.length == 2) {
						try {
							int count = Integer.parseInt(pieces[0]);
							String name = pieces[1];
							name = name.replace("AE", "Æ");
							
							Collection<CardSource> cards = db.getCards(name);
							if (cards == null) {
								throw new IOException("Unable to find match for '" + name + "'");
							}
							
							rows.add(new CardRow(count, name, cards));
							
						} catch (NumberFormatException e) {
							throw new IOException(e);
						}
					}
				}
			}
		} finally {
			r.close();
		}
		
		return rows;
	}
}
