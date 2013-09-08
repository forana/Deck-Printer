package forana.magic.printer.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import forana.magic.printer.lookup.CardDatabase;
import forana.magic.printer.lookup.GathererManager;
import forana.magic.printer.lookup.ListLoader;
import forana.magic.printer.model.CardRow;

public class AppFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private JPanel secondaryContainer;
	
	public AppFrame() {
		super();
		
		this.setTitle("Magic Printer");
		this.setLocationByPlatform(true);
		this.setMinimumSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		CardDatabase db;
		
		StatusDialog sd = new StatusDialog(this);
		try {
			db = CardDatabase.getCachedDatabase(sd);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			db = new CardDatabase();
			this.showError("Error loading database.\nPlease click 'Update from Gatherer', and reload any custom sets.");
		}
		sd.dispose();
		
		this.buildComponents(db);
		
		this.setVisible(true);
	}
	
	@SuppressWarnings("serial")
	private void buildComponents(final CardDatabase db) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 0;
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonPanel.add(new JButton("Load Deck") {{
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					promptToLoadList(db);
				}
			});
		}});
		
		buttonPanel.add(new JButton("Load Custom Set") {{
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					promptToLoadCustomSet(db);
				}
			});
		}});
		
		buttonPanel.add(new JButton("Update from Gatherer") {{
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateGatherer(db);
				}
			});
		}});
		
		buttonPanel.add(new JButton("Check Loaded Sets") {{
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showCardDatabaseInfo(db);
				}
			});
		}});
		
		this.add(buttonPanel, c);
		
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		
		this.scrollPane = new JScrollPane();
		
		this.add(this.scrollPane, c);
		
		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 0;
		
		this.secondaryContainer = new JPanel();
		this.secondaryContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.secondaryContainer.setBorder(BorderFactory.createRaisedBevelBorder());
		
		this.add(this.secondaryContainer, c);
	}
	
	private void setComponent(Component component) {
		this.scrollPane.setViewportView(component);
		this.secondaryContainer.removeAll();
		if (component instanceof ComponentProvider) {
			ComponentProvider provider = (ComponentProvider)component;
			for (Component innerComponent : provider.getProvidedComponents(this)) {
				this.secondaryContainer.add(innerComponent);
			}
		}
		this.validate();
	}
	
	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void promptToLoadList(CardDatabase db) {
		JFileChooser chooser = new RememberingFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				List<CardRow> rows = ListLoader.loadFromTextFile(file, db);
				this.setComponent(new CardListPane(rows));
			} catch (IOException e) {
				this.showError(e.getMessage());
				e.printStackTrace(System.err);
			}
		}
	}
	
	private void promptToLoadCustomSet(final CardDatabase db) {
		JFileChooser chooser = new RememberingFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File setFolder = chooser.getSelectedFile();
			String setName = setFolder.getName();
			File[] imageFiles = setFolder.listFiles(new FileFilter() {
				public boolean accept(File f) {
					return f.isFile() && (f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg"));
				}
			});
			
			final SetImportDialog sid = new SetImportDialog(this, setName, imageFiles);
			if (!sid.isCancelled()) {
				final StatusDialog sd = new StatusDialog(this);
				
				new Thread(new Runnable() {
					public void run() {
						try {
							try {
								db.addCustomSet(sid.getSetName(), sid.getCardEntries(sd));
								CardDatabase.cacheDatabase(db);
							} catch (IOException e) {
								showError(e.getMessage());
								e.printStackTrace(System.err);
							}
							db.buildMap(sd);
						} finally {
							sd.dispose();
						}
					}
				}).start();
			}
		}
	}
	
	private void showCardDatabaseInfo(CardDatabase db) {
		this.setComponent(new DBInfoPane(db, this));
	}
	
	private void updateGatherer(final CardDatabase db) {
		final StatusDialog sd = new StatusDialog(this);
		new Thread(new Runnable() {
			public void run() {
				try {
					GathererManager.updateDatabase(db, sd);
					CardDatabase.cacheDatabase(db);
				} catch (IOException e) {
					showError(e.getMessage());
					e.printStackTrace(System.err);
				} finally {
					sd.dispose();
				}
			}
		}).start();
	}
}
