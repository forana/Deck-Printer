package forana.magic.printer;

import java.io.IOException;

import forana.magic.printer.lookup.CardDatabase;
import forana.magic.printer.lookup.GathererManager;

public class DatabaseUpdateTest {
	public static void main(String[] args) throws IOException {
		CardDatabase db;
		
		try {
			db = CardDatabase.getCachedDatabase(new NullReceiver());
			System.out.println("db loaded from cache");
		} catch (IOException e) {
			System.err.println(e.getMessage());
			db = new CardDatabase();
			System.out.println("created new db");
		}
		
		System.out.println("updating...");
		GathererManager.updateDatabase(db, new StatusReceiver() {
			public void setCompleted(int completed) {
				System.out.println(completed);
			}
			
			public void setTotal(int total) {
				System.out.println("(total = " + total + ")");
			}
			
			public void setStatus(String status) {
				System.out.println("> " + status);
			}
		});
		
		System.out.println("saving...");
		CardDatabase.cacheDatabase(db);
		
		System.out.println("looks good.");
	}
}
