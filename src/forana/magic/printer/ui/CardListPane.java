package forana.magic.printer.ui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import forana.magic.printer.PrintablePage;
import forana.magic.printer.PrinterManager;
import forana.magic.printer.StatusDialog;
import forana.magic.printer.model.CardRow;

public class CardListPane extends JTable implements ComponentProvider {
	private static final long serialVersionUID = 1L;
	
	private CardRowModel model;
	
	public CardListPane(List<CardRow> rows) {
		super();
		this.model = new CardRowModel(rows);
		this.setModel(this.model);
		this.setFillsViewportHeight(false);
		
		this.getColumnModel().getColumn(2).setCellEditor(new SelectableSourceEditor());
		this.getColumnModel().getColumn(3).setCellRenderer(new ThumbnailRenderer());
		
		this.setRowHeight(ThumbnailRenderer.HEIGHT);
	}
	
	@Override
	public Iterable<Component> getProvidedComponents(final Frame target) {
		List<Component> components = new LinkedList<>();
		JButton printButton = new JButton("Print");
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<Image> images = model.getImages();
					StatusDialog sd = new StatusDialog(target);
					PrintablePage page = new PrintablePage(images, sd);
					PrinterManager.promptToPrint(page);
					sd.dispose();
				} catch (IOException | PrinterException ex) {
					JOptionPane.showMessageDialog(target, "Problem printing:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		printButton.setEnabled(model.allCardsHaveImages());
		components.add(printButton);
		return components;
	}
	
	private static class CardRowModel implements TableModel {
		private static final String[] COLUMN_NAMES = {"#", "Name", "Set", "Preview"};
		
		List<CardRow> rows;
		
		public CardRowModel(List<CardRow> rows) {
			this.rows = rows;
		}
		
		public boolean allCardsHaveImages() {
			for (CardRow row : this.rows) {
				if (row.source.getSelectedPath() == null) {
					return false;
				}
			}
			return true;
		}
		
		public List<Image> getImages() throws IOException {
			List<Image> images = new LinkedList<>();
			for (CardRow row : this.rows) {
				images.add(ImageIO.read(row.source.getSelectedPath()));
			}
			return images;
		}

		@Override
		public int getRowCount() {
			return rows.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return COLUMN_NAMES[columnIndex];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return Object.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			CardRow row = this.rows.get(rowIndex);
			
			switch (columnIndex) {
				case 0: return row.quantity;
				case 1: return row.name;
				case 2: return row.source;
				case 3: return row.source.getSelectedPath();
			}
			return null;
		}
		
		@Override
		public void setValueAt(Object value, int row, int col) {
		}

		@Override
		public void addTableModelListener(TableModelListener l) {
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
		}
	}
}
