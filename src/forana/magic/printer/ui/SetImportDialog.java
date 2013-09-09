package forana.magic.printer.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import forana.magic.printer.StatusReceiver;
import forana.magic.printer.image.ImageCache;
import forana.magic.printer.model.CardEntry;
import forana.magic.printer.model.CustomSource;

public class SetImportDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final Pattern[] FILENAME_PATTERNS = {
		Pattern.compile("\\.png$", Pattern.CASE_INSENSITIVE),
		Pattern.compile("\\.jpe?g$", Pattern.CASE_INSENSITIVE),
		Pattern.compile("\\.full$", Pattern.CASE_INSENSITIVE),
		Pattern.compile("\\(?[0-9]\\)?$")
	};
	
	private String setName;
	private SetImportModel model;
	private boolean cancelled;
	
	public SetImportDialog(Frame owner, String setName, File[] imageFiles) {
		super(owner, "Import Set", true);
		this.setName = setName;
		this.model = new SetImportModel(imageFiles);
		this.cancelled = false;
		
		this.setMinimumSize(new Dimension(600, 400));
		this.buildUI();
		
		this.setVisible(true);
	}
	
	public void buildUI() {
		final SetImportDialog thisDialog = this;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipadx = 3;
		c.ipady = 3;
		
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		
		this.add(new JLabel("Set Name"), c);
		
		c.gridx = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_END;
		final JTextField setNameField = new JTextField(this.setName);
		setNameField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				thisDialog.setName = setNameField.getText();
			}
		});
		this.add(setNameField, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		JTable table = new JTable(this.model);
		table.getColumnModel().getColumn(3).setCellRenderer(new ThumbnailRenderer());
		table.setRowHeight(ThumbnailRenderer.HEIGHT);
		JScrollPane tableScroll = new JScrollPane(table);
		this.add(tableScroll, c);
		
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thisDialog.cancelled = true;
				thisDialog.dispose();
			}
		});
		this.add(cancelButton, c);
		
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_END;
		JButton acceptButton = new JButton("Import");
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thisDialog.cancelled = false;
				thisDialog.dispose();
			}
		});
		this.add(acceptButton, c);
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public String getSetName() {
		return this.setName;
	}
	
	public Collection<CardEntry> getCardEntries(StatusReceiver sr) throws IOException {
		return this.model.getCardEntries(this.getSetName(), sr);
	}
	
	protected static String guessCardName(String fileName) {
		String name = fileName;
		for (Pattern p : FILENAME_PATTERNS) {
			name = p.matcher(name).replaceAll("");
		}
		return name;
	}
	
	private static class SetImportModel implements TableModel {
		private boolean[] includes;
		private String[] cardNames;
		private File[] imageFiles;
		
		private static final String[] COLUMN_NAMES = {"Import?", "Name", "Filename", "Thumbnail"};
		private static final Class<?>[] COLUMN_CLASSES = {Boolean.class, String.class, String.class, File.class};
		
		public SetImportModel(File[] imageFiles) {
			this.imageFiles = imageFiles;
			this.cardNames = new String[imageFiles.length];
			this.includes = new boolean[imageFiles.length];
			for (int i=0; i<this.cardNames.length; i++) {
				this.cardNames[i] = guessCardName(this.imageFiles[i].getName());
				this.includes[i] = true;
			}
		}
		
		public Collection<CardEntry> getCardEntries(String setName, StatusReceiver sr) throws IOException {
			List<CardEntry> entries = new LinkedList<>();
			sr.setStatus("Checking form");
			sr.setCompleted(0);
			sr.setTotal(this.includes.length);
			for (int i=0; i<this.includes.length; i++) {
				if (this.includes[i]) {
					sr.setStatus("Importing " + this.cardNames[i] + " (" + this.imageFiles[i].getName() + ")");
					File file = ImageCache.migrateAndCache(setName, this.imageFiles[i], sr);
					entries.add(new CardEntry(this.cardNames[i],
						new CustomSource(setName, file)));
				}
				sr.setCompleted(i);
			}
			return entries;
		}

		@Override
		public int getRowCount() {
			return this.cardNames.length;
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
			return COLUMN_CLASSES[columnIndex];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex < 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case 0:
					return this.includes[rowIndex];
				case 1:
					return this.cardNames[rowIndex];
				case 2:
					return this.imageFiles[rowIndex].getName();
				case 3:
					return this.imageFiles[rowIndex];
				default:
					return null;
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				this.includes[rowIndex] = (Boolean)aValue;
			} else if (columnIndex == 1) {
				String text = aValue.toString();
				this.cardNames[rowIndex] = text;
			}
		}

		@Override
		public void addTableModelListener(TableModelListener l) {
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
		}
	}
}
