package forana.magic.printer.printing;

import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class PrinterManager {
	public static void promptToPrint(Printable printable) throws PrinterException {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(printable);
		if (job.printDialog()) {
			job.print();
		}
	}
}
