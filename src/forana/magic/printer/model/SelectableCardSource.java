package forana.magic.printer.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import forana.magic.printer.NullReceiver;

public class SelectableCardSource {
	private int selectedIndex;
	private final List<CardSource> sources;
	
	public SelectableCardSource(Collection<CardSource> sources) {
		this.selectedIndex = 0;
		this.sources = new ArrayList<>();
		this.sources.addAll(sources);
	}
	
	public int getSelectedIndex() {
		return this.selectedIndex;
	}
	
	public void setSelectedIndex(int index) {
		this.selectedIndex = index;
	}
	
	public File getSelectedPath() {
		if (this.sources.size() == 0) {
			return null;
		}
		try {
			return this.sources.get(this.selectedIndex).getImageFile(new NullReceiver());
		} catch (IOException e) {
			return null;
		}
	}
	
	public Collection<CardSource> getSources() {
		return this.sources;
	}
	
	@Override
	public String toString() {
		if (this.sources.size() > 0) {
			return this.sources.get(this.selectedIndex).toString();
		}
		return "(none available)";
	}
}
