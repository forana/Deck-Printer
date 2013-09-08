package forana.magic.printer.lookup;

public interface SetRemovalStrategy {
	public void remove(CardDatabase db, String setName);
	
	public static SetRemovalStrategy GATHERER = new SetRemovalStrategy() {
		public void remove(CardDatabase db, String setName) {
			db.removeGathererSet(setName);
		}
	};
	
	public static SetRemovalStrategy CUSTOM = new SetRemovalStrategy() {
		public void remove(CardDatabase db, String setName) {
			db.removeCustomSet(setName);
		}
	};
}
