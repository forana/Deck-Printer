package forana.magic.printer.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.List;

import forana.magic.printer.StatusReceiver;

public class PrintablePage implements Printable {
	private static final double DESIRED_DPI = 300;
	private static final double STANDARD_DPI = 72; // hey oracle, maybe this could be accessible somewhere instead of magic
	private static final double SCALE = STANDARD_DPI / DESIRED_DPI;
	
	private List<Image> images;
	private StatusReceiver receiver;
	private CardFormat format;
	
	public PrintablePage(List<Image> images, CardFormat format, StatusReceiver receiver) {
		this.images = images;
		this.receiver = receiver;
		this.format = format;
	}
	
	public int print(Graphics g, PageFormat pf, int index) {
		Graphics2D g2 = (Graphics2D)g;
		int xoff = (int)Math.ceil(pf.getImageableX() / SCALE);
		int yoff = (int)Math.ceil(pf.getImageableY() / SCALE);
		double width = pf.getImageableWidth() / SCALE;
		double height = pf.getImageableHeight() / SCALE;
		g2.scale(SCALE, SCALE);
		
		int scaledWidth = (int)Math.round(format.WIDTH * DESIRED_DPI);
		int scaledHeight = (int)Math.round(format.HEIGHT * DESIRED_DPI);
		
		if (shouldRotate(width, height, scaledWidth, scaledHeight)) {
			g2.rotate(Math.PI/2);
			g2.translate(0, -(width + 2 * xoff));
			double t = width;
			width = height;
			height = t;
			int it = xoff;
			xoff = yoff;
			yoff = it;
		}
		
		int hcards = (int)Math.floor(width / scaledWidth);
		int vcards = (int)Math.floor(height / scaledHeight);
		int cardsPerPage = hcards * vcards;
		
		int xCentering = (int)Math.floor((width - (hcards * scaledWidth)) / 2);
		int yCentering = (int)Math.floor((height - (vcards * scaledHeight)) / 2);
		
		int startIndex = cardsPerPage * index;
		if (startIndex >= this.images.size()) {
			return Printable.NO_SUCH_PAGE;
		}
		this.receiver.setStatus("Rendering page " + index);
		int cardIndex = startIndex;
		int h = 0;
		int v = 0;
		
		while (cardIndex < startIndex + hcards * vcards && cardIndex < this.images.size()) {
			Image scaled = this.images.get(cardIndex).getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			
			g.drawImage(scaled,
				xoff + xCentering + h * scaledWidth,
				yoff + yCentering + v * scaledHeight,
				null);
			
			h++;
			if (h == hcards) {
				h = 0;
				v++;
			}
			
			cardIndex++;
		}
		
		return Printable.PAGE_EXISTS;
	}
	
	private static boolean shouldRotate(double areaWidth, double areaHeight, double cardWidth, double cardHeight) {
		int nrh = (int)Math.floor(areaWidth / cardWidth);
		int nrv = (int)Math.floor(areaHeight / cardHeight);
		
		int rh = (int)Math.floor(areaHeight / cardWidth);
		int rv = (int)Math.floor(areaWidth / cardHeight);
		
		return (rh * rv) > (nrh * nrv);
	}
}
