package forana.magic.printer.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ThumbnailRenderer implements TableCellRenderer {
	public static final int HEIGHT = 50;
	
	private Map<Object, Component> componentMap;
	
	public ThumbnailRenderer() {
		this.componentMap = new HashMap<>();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value != null && !this.componentMap.containsKey(value)) {
			File source = (File)value;
			
			try {
				Image im = ImageIO.read(source);
				this.componentMap.put(value, new RenderingPanel(im));
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
		
		Component c = this.componentMap.get(value);
		
		if (c != null) {
			c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
		}
		
		return c;
	}
	
	private static class RenderingPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private Image source;
		
		public RenderingPanel(Image source) {
			this.source = source;
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			
			int ch = this.getHeight();
			int ih = this.source.getHeight(this);
			double ratio = 1.0 * ch/ih;
			
			g.drawImage(this.source.getScaledInstance((int)(ratio * this.source.getWidth(this)), ch, Image.SCALE_SMOOTH), 0, 0, this);
		}
	}
}
