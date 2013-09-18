package forana.magic.printer.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.List;

import forana.magic.printer.StatusReceiver;

/**
 * Represents a page of images to be printed.
 */
public class PrintablePage implements Printable {
	/** DPI we'd like to print at. **/
	private static final double DESIRED_DPI = 300;
	/** DPI java wants us to print at. **/
	private static final double STANDARD_DPI = 72; // hey oracle, maybe this could be accessible somewhere instead of magic
	/** Value we'll use for scale transform in order to print at the DPI we'd like. **/
	private static final double SCALE = STANDARD_DPI / DESIRED_DPI;
	
	/** The images to be printed. It's assumed they're all the same size. **/
	private List<Image> images;
	/** Receiver to give statuses to. **/
	// has to be held onto because we lose control of this flow later on.
	private StatusReceiver receiver;
	/** Format (dimensions) we want to print at. **/
	private CardFormat format;
	
	public PrintablePage(List<Image> images, CardFormat format, StatusReceiver receiver) {
		this.images = images;
		this.receiver = receiver;
		this.format = format;
	}
	
	@Override
	public int print(Graphics g, PageFormat pf, int index) {
		Graphics2D g2 = (Graphics2D)g;
		// scaled offsets
		int xoff = (int)Math.ceil(pf.getImageableX() / SCALE);
		int yoff = (int)Math.ceil(pf.getImageableY() / SCALE);
		// scaled imageable sizes (width/height of the printable area)
		double width = pf.getImageableWidth() / SCALE;
		double height = pf.getImageableHeight() / SCALE;
		
		// apply scale transform
		g2.scale(SCALE, SCALE);
		
		// scaled card dimensions
		int scaledWidth = (int)Math.round(format.WIDTH * DESIRED_DPI);
		int scaledHeight = (int)Math.round(format.HEIGHT * DESIRED_DPI);
		
		// check if we'd be able to fit more images onto the page if we rotate them
		if (shouldRotate(width, height, scaledWidth, scaledHeight)) {
			// apply that transform, swap variables, translate appropriately
			g2.rotate(Math.PI/2);
			g2.translate(0, -(width + 2 * xoff));
			// width <-> height
			double t = width;
			width = height;
			height = t;
			// x offset <-> y offset
			int it = xoff;
			xoff = yoff;
			yoff = it;
		}
		
		// number of cards we can display horizontally
		int hcards = (int)Math.floor(width / scaledWidth);
		// number of cards we can display vertically
		int vcards = (int)Math.floor(height / scaledHeight);
		
		int cardsPerPage = hcards * vcards;
		
		int xCentering = (int)Math.floor((width - (hcards * scaledWidth)) / 2);
		int yCentering = (int)Math.floor((height - (vcards * scaledHeight)) / 2);
		
		// this function will be called repeatedly until it returns NO_SUCH_PAGE, so let's return that if we've run out of images
		int startIndex = cardsPerPage * index;
		if (startIndex >= this.images.size()) {
			return Printable.NO_SUCH_PAGE;
		}
		
		this.receiver.setStatus("Rendering page " + index);
		int cardIndex = startIndex;
		int h = 0;
		int v = 0;
		
		// lay out each card for that page
		while (cardIndex < startIndex + hcards * vcards && cardIndex < this.images.size()) {
			// scale the image properly
			Image scaled = this.images.get(cardIndex).getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			
			g.drawImage(scaled,
				xoff + xCentering + h * scaledWidth,
				yoff + yCentering + v * scaledHeight,
				null); // can't pass an ImageObserver because we don't get one here... null works because this is a BufferedImage, thankfully
			
			h++;
			if (h == hcards) {
				h = 0;
				v++;
			}
			
			cardIndex++;
		}
		
		return Printable.PAGE_EXISTS;
	}
	
	/** Check if more cards would fit in the given area if they were rotated. **/
	private static boolean shouldRotate(double areaWidth, double areaHeight, double cardWidth, double cardHeight) {
		int nrh = (int)Math.floor(areaWidth / cardWidth);
		int nrv = (int)Math.floor(areaHeight / cardHeight);
		
		int rh = (int)Math.floor(areaHeight / cardWidth);
		int rv = (int)Math.floor(areaWidth / cardHeight);
		
		return (rh * rv) > (nrh * nrv);
	}
}
