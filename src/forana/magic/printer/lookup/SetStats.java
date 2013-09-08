package forana.magic.printer.lookup;

public class SetStats {
	public final String setName;
	public final int cardCount;
	public final boolean isCustom;
	public final SetRemovalStrategy removalStrategy;
	
	public SetStats(String setName, int cardCount, boolean isCustom, SetRemovalStrategy removalStrategy) {
		this.setName = setName;
		this.cardCount = cardCount;
		this.isCustom = isCustom;
		this.removalStrategy = removalStrategy;
	}
}
