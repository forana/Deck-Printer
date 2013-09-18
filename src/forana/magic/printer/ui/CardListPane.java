package forana.magic.printer.ui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import forana.magic.printer.image.CornerNormalizer;
import forana.magic.printer.model.CardRow;
import forana.magic.printer.printing.CardFormat;
import forana.magic.printer.printing.PrintablePage;
import forana.magic.printer.printing.PrinterManager;

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
		
		final JCheckBox shuffleCheckbox = new JCheckBox("Shuffle printed cards", false);
		components.add(shuffleCheckbox);
		
		final JCheckBox roundCornersCheckbox = new JCheckBox("Attempt to normalize borders", true);
		components.add(roundCornersCheckbox);
		
		final JComboBox<CardFormat> cardSizeComboBox = new JComboBox<>(CardFormat.values());
		components.add(cardSizeComboBox);
		
		JButton printButton = new JButton("Print");
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<Image> images = model.getImages(roundCornersCheckbox.isSelected());
					
					if (shuffleCheckbox.isSelected()) {
						Collections.shuffle(images);
					}
					
					StatusDialog sd = new StatusDialog(target);
					
					PrintablePage page = new PrintablePage(images,
						cardSizeComboBox.getItemAt(cardSizeComboBox.getSelectedIndex()),
						sd);
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
		
		public List<Image> getImages(boolean roundCorners) throws IOException {
			List<Image> images = new LinkedList<>();
			for (CardRow row : this.rows) {
				BufferedImage image = ImageIO.read(row.source.getSelectedPath());
				if (roundCorners) {
					image = CornerNormalizer.normalize(image);
				}
				for (int i=0; i<row.quantity; i++) {
					images.add(image);
				}
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
