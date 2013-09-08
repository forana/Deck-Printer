package forana.magic.printer.lookup;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import forana.magic.gatherer.CardStub;
import forana.magic.gatherer.Gatherer;
import forana.magic.printer.StatusReceiver;
import forana.magic.printer.model.CardEntry;
import forana.magic.printer.model.GathererSource;

public class GathererManager {
	private static final String PROMO_SET_PREFIX = "Promo";

	public static int updateDatabase(CardDatabase db, StatusReceiver receiver) throws IOException {
		Gatherer g = new Gatherer();
		
		int updatedSets = 0;
		Collection<String> setNames = g.getSetNames();
		receiver.setTotal(setNames.size());
		receiver.setCompleted(0);
		
		int setCounter = 0;
		
		for (String set : setNames) {
			receiver.setStatus("Checking " + set);
			List<CardEntry> cards = new LinkedList<>();
			
			if (set.startsWith(PROMO_SET_PREFIX) || !db.hasGathererSet(set)) {
				receiver.setStatus("Grabbing " + set);
				for (CardStub stub : g.getCardsInSet(set)) {
					GathererSource source = new GathererSource(stub.id, set);
					cards.add(new CardEntry(stub.name, source));
				}
				db.addGathererSet(set, cards);
				updatedSets++;
			}
			setCounter++;
			receiver.setCompleted(setCounter);
		}
		
		receiver.setStatus("Rebuilding map...");
		db.buildMap(receiver);
		
		return updatedSets;
	}

}
