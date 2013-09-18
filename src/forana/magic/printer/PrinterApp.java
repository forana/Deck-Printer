package forana.magic.printer;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import forana.magic.printer.image.ImageCache;
import forana.magic.printer.ui.AppFrame;

/** Main class for the printer application. **/
public class PrinterApp {
	public static void main(String[] args) {
		// ask nicely to set up the look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// don't care
		}
		
		try {
			ImageCache.init();
		} catch (IOException e) {
			// TODO centralize these calls
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		new AppFrame();
	}
}
