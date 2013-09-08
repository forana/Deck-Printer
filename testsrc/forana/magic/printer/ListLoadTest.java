package forana.magic.printer;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import forana.magic.printer.lookup.CardDatabase;
import forana.magic.printer.lookup.ListLoader;
import forana.magic.printer.model.CardRow;

public class ListLoadTest {
	public static void main(String[] args) throws Exception {
		CardDatabase db = CardDatabase.getCachedDatabase(new NullReceiver());
		
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			List<CardRow> rows = ListLoader.loadFromTextFile(file, db);
			for (CardRow row : rows) {
				System.out.println(row);
			}
		}
	}
}
