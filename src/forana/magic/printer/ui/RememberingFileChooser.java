package forana.magic.printer.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class RememberingFileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;
	
	private static File LAST_OPENED_FILE = new File(System.getProperty("user.home"));
	
	public RememberingFileChooser() {
		super();
		this.setCurrentDirectory(LAST_OPENED_FILE);
	}
	
	@Override
	public int showOpenDialog(Component owner) {
		int res = super.showOpenDialog(owner);
		if (res == JFileChooser.APPROVE_OPTION) {
			LAST_OPENED_FILE = this.getSelectedFile().getParentFile();
		}
		return res;
	}
}
