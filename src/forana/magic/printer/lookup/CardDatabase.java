package forana.magic.printer.lookup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import forana.magic.printer.StatusReceiver;
import forana.magic.printer.model.CardEntry;
import forana.magic.printer.model.CardSource;

public class CardDatabase implements Serializable {
	private static final long serialVersionUID = 2L;
	
	private static final String DATABASE_CACHE_NAME = "cards.db";
	
	public static void cacheDatabase(CardDatabase db) throws IOException {
		File dbFile = new File(DATABASE_CACHE_NAME);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFile));
		oos.writeObject(db);
		oos.close();
	}

	public static CardDatabase getCachedDatabase(StatusReceiver sr) throws IOException {
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
			db.buildMap(sr);
			return db;
		}
		
		throw new IOException("No DB file exists");
	}
	
	private transient Map<String, Collection<CardSource>> cardMap;
	
	private Map<String, Collection<CardEntry>> gathererSets;
	private Map<String, Collection<CardEntry>> customSets;
	
	public CardDatabase() {
		this.cardMap = null;
		this.gathererSets = new TreeMap<>();
		this.customSets = new TreeMap<>();
	}
	
	public void addCustomSet(String name, Collection<CardEntry> set) {
		this.customSets.put(name, set);
	}
	
	public void addGathererSet(String name, Collection<CardEntry> set) {
		this.gathererSets.put(name, set);
	}
	
	public boolean hasGathererSet(String name) {
		return this.gathererSets.containsKey(name);
	}
	
	public void buildMap(StatusReceiver sr) {
		String baseStatus = "Rebuilding map... ";
		sr.setStatus(baseStatus);
		
		sr.setCompleted(0);
		sr.setTotal(this.customSets.size() + this.gathererSets.size());
		int total = 0;
		
		this.cardMap = new HashMap<>();
		
		for (Map<String, Collection<CardEntry>> map : Arrays.asList(this.customSets, this.gathererSets)) {
			for (Map.Entry<String, Collection<CardEntry>> entry : map.entrySet()) {
				String setName = entry.getKey();
				Collection<CardEntry> cards = entry.getValue();
				sr.setStatus(baseStatus + setName);
				for (CardEntry card : cards) {
					if (!this.cardMap.containsKey(card.name)) {
						this.cardMap.put(card.name, new TreeSet<CardSource>());
					}
					
					this.cardMap.get(card.name).add(card.source);
				}
				total++;
				sr.setCompleted(total);
			}
		}
		sr.setStatus(null);
	}
	
	public Collection<CardSource> getCards (String name) {
		Collection<CardSource> ret = this.cardMap.get(name);
		if (ret == null) {
			return new LinkedList<CardSource>();
		}
		return ret;
	}
	
	public List<SetStats> getSetStats() {
		List<SetStats> stats = new ArrayList<>();
		
		for (Map.Entry<String, Collection<CardEntry>> entry : this.customSets.entrySet()) {
			stats.add(new SetStats(entry.getKey(), entry.getValue().size(), true, SetRemovalStrategy.CUSTOM));
		}
		
		for (Map.Entry<String, Collection<CardEntry>> entry : this.gathererSets.entrySet()) {
			stats.add(new SetStats(entry.getKey(), entry.getValue().size(), false, SetRemovalStrategy.GATHERER));
		}
		
		return stats;
	}
	
	public void removeGathererSet(String name) {
		this.gathererSets.remove(name);
	}
	
	public void removeCustomSet(String name) {
		this.customSets.remove(name);
	}
}
