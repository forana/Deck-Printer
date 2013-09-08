package forana.magic.printer.model;

import java.io.File;
import java.io.IOException;

import forana.magic.printer.StatusReceiver;
import forana.magic.printer.image.ImageCache;

public class GathererSource extends CardSource {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String set;
	
	public GathererSource(long id, String set) {
		this.id = id;
		this.set = set;
	}
	
	public File getImageFile(StatusReceiver sr) throws IOException {
		return ImageCache.get(this.id, sr);
	}
	
	@Override
	public String toString() {
		return set + " (gatherer)";
	}
}
