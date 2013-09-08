package forana.magic.printer.model;

import java.io.File;

import forana.magic.printer.StatusReceiver;

public class CustomSource extends CardSource {
	private static final long serialVersionUID = 1L;
	
	private String set;
	private File file;
	
	public CustomSource(String set, File path) {
		this.set = set;
		this.file = path;
	}
	
	public String getSetName() {
		return this.set;
	}
	
	public File getImageFile(StatusReceiver sr) {
		return this.file;
	}
	
	@Override
	public String toString() {
		return this.set + " (custom)";
	}
}
