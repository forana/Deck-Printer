package forana.magic.printer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.List;

public class PrintablePage implements Printable {
	private static final double DESIRED_DPI = 300;
	private static final double STANDARD_DPI = 72; // hey oracle, maybe this could be accessible somewhere instead of magic
	private static final double SCALE = STANDARD_DPI / DESIRED_DPI;
	
	private static final double REAL_CARD_WIDTH = 2.48;
	private static final double REAL_CARD_HEIGHT = 3.46;
	
	private static final int SCALED_CARD_WIDTH = (int)Math.round(REAL_CARD_WIDTH * DESIRED_DPI);
	private static final int SCALED_CARD_HEIGHT = (int)Math.round(REAL_CARD_HEIGHT * DESIRED_DPI);
	
	private List<Image> images;
	private StatusReceiver receiver;
	
	public PrintablePage(List<Image> images, StatusReceiver receiver) {
		this.images = images;
		this.receiver = receiver;
	}
	
	public int print(Graphics g, PageFormat pf, int index) {
		int xoff = (int)Math.ceil(pf.getImageableX() / SCALE);
		int yoff = (int)Math.ceil(pf.getImageableY() / SCALE);
		double width = pf.getImageableWidth() / SCALE;
		double height = pf.getImageableHeight() / SCALE;
		((Graphics2D)g).scale(SCALE, SCALE);
		
		int hcards = (int)Math.floor(width / SCALED_CARD_WIDTH);
		int vcards = (int)Math.floor(height / SCALED_CARD_HEIGHT);
		int cardsPerPage = hcards * vcards;
		
		int startIndex = cardsPerPage * index;
		if (startIndex >= this.images.size()) {
			return Printable.NO_SUCH_PAGE;
		}
		this.receiver.setStatus("Rendering page " + index);
		int cardIndex = startIndex;
		int h = 0;
		int v = 0;
		
		while (cardIndex < startIndex + hcards * vcards && cardIndex < this.images.size()) {
			Image scaled = this.images.get(cardIndex).getScaledInstance(SCALED_CARD_WIDTH, SCALED_CARD_HEIGHT, Image.SCALE_SMOOTH);
			
			g.drawImage(scaled, xoff + h * SCALED_CARD_WIDTH, yoff + v * SCALED_CARD_HEIGHT, null);
			
			h++;
			if (h == hcards) {
				h = 0;
				v++;
			}
			
			cardIndex++;
		}
		
		return Printable.PAGE_EXISTS;
	}
}
