package forana.magic.printer.ui;

import java.awt.Component;
import java.awt.Frame;

public interface ComponentProvider {
	public Iterable<Component> getProvidedComponents(Frame target);
}
