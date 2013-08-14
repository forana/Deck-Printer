package forana.magic.printer.lookup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import forana.magic.gatherer.CardStub;
import forana.magic.gatherer.Gatherer;
import forana.magic.printer.model.Card;

public class CardDatabaseManager {
	private static final String DATABASE_CACHE_NAME = "cards.db";
	private static final String PROMO_SET_PREFIX = "Promo";
	
	private CardDatabaseManager() {};
	
	public static CardDatabase getCachedDatabase() throws IOException {
		File dbFile = new File(DATABASE_CACHE_NAME);
		
		if (dbFile.exists()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile));
			CardDatabase db = null;
			try {
				db = (CardDatabase)ois.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException(e);
			} finally {
				ois.close();
			}
			return db;
		}
		
		throw new IOException("No DB file exists");
	}
	
	public static void cacheDatabase(CardDatabase db) throws IOException {
		File dbFile = new File(DATABASE_CACHE_NAME);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFile));
		oos.writeObject(db);
		oos.close();
	}
	
	public static int updateDatabase(CardDatabase db, StatusReceiver receiver) throws IOException {
		Gatherer g = new Gatherer();
		
		int updatedSets = 0;
		Collection<String> setNames = g.getSetNames();
		receiver.setTotal(setNames.size());
		receiver.setCompleted(0);
		
		int setCounter = 0;
		
		for (String set : setNames) {
			receiver.setStatus("Checking " + set);
			if (set.startsWith(PROMO_SET_PREFIX) || !db.knowsAbout(set)) {
				receiver.setStatus("Grabbing " + set);
				for (CardStub stub : g.getCardsInSet(set)) {
					Card card = new Card(stub.name, set, stub.id);
					db.add(card);
				}
				db.addKnownSet(set);
				updatedSets++;
			}
			setCounter++;
			receiver.setCompleted(setCounter);
		}
		
		return updatedSets;
	}
}
