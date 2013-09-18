package forana.magic.printer.printing;

public enum CardFormat {
	STANDARD(2.48, 3.46, "Standard Card Size"),
	OVERSIZED(3.46, 4.96, "Oversized (Commander, Archenemy)"),
	PLANE(4.96, 3.46, "Sideways (Planechase)"),
	SMALLPLANE(3.46, 2.48, "Small Sideways");
	
	public final double WIDTH;
	public final double HEIGHT;
	private final String text;
	
	private CardFormat(double width, double height, String text) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}
