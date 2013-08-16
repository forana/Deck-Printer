package forana.magic.printer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

public class PrinterConfigTest {
	public static void main(String[] args) throws Exception {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(new Printable() {
			public int print(Graphics g, PageFormat pf, int index) {
				if (index == 0) {
					double SCALING = (72.0/300);
					((Graphics2D)g).scale(SCALING, SCALING);
					g.setColor(Color.BLACK);
					double xoff = pf.getImageableX() / SCALING;
					double yoff = pf.getImageableY() / SCALING;
					double width = pf.getImageableWidth() / SCALING;
					double height = pf.getImageableHeight() / SCALING;
					g.drawLine((int)xoff, (int)yoff, (int)(xoff + width), (int)(yoff + height));
					return Printable.PAGE_EXISTS;
				} else {
					return Printable.NO_SUCH_PAGE;
				}
			}
		});
		job.printDialog();
		job.print();
	}
}
