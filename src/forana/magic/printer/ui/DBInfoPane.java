package forana.magic.printer.ui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import forana.magic.printer.lookup.CardDatabase;
import forana.magic.printer.lookup.SetStats;

public class DBInfoPane extends JTable {
	private static final long serialVersionUID = 1L;
	
	private static String[] COLUMN_NAMES = {"Set Name", "Type", "Cards Held", "Delete?"};
	private static Class<?>[] COLUMN_CLASSES = {String.class, String.class, Integer.class, Component.class};
	
	private CardDatabase db;
	private Frame owner;
	
	public DBInfoPane(CardDatabase db, Frame owner) {
		super();
		this.db = db;
		this.owner = owner;
		this.setModel(new DBStatsModel());
		this.getColumnModel().getColumn(3).setCellRenderer(new ComponentCellRenderer());
		this.getColumnModel().getColumn(3).setCellEditor(new ComponentCellRenderer());
	}
	
	private class DBStatsModel implements TableModel {
		
		private List<SetStats> statsList;
		private List<TableModelListener> modelListeners;
		
		public DBStatsModel() {
			this.statsList = db.getSetStats();
			this.modelListeners = new LinkedList<>();
		}
		
		@Override
		public int getRowCount() {
			return this.statsList.size();
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
			return columnIndex == 3;
		}

		@Override
		public Object getValueAt(final int rowIndex, int columnIndex) {
			SetStats stats = this.statsList.get(rowIndex);
			switch (columnIndex) {
				case 0: return stats.setName;
				case 1: return stats.isCustom ? "Custom" : "Gatherer";
				case 2: return stats.cardCount;
				case 3: return new JButton("Delete") {
					private static final long serialVersionUID = 1L;
				{
					addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeRow(rowIndex);
						}
					});
				}};
			}
			return null;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		}

		@Override
		public void addTableModelListener(TableModelListener l) {
			this.modelListeners.add(l);
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			this.modelListeners.remove(l);
		}
		
		private void removeRow(int index) {
			SetStats stats = this.statsList.get(index);
			stats.removalStrategy.remove(db, stats.setName);
			this.statsList.remove(index);
			
			TableModelEvent event = new TableModelEvent(this, index, TableModelEvent.DELETE);
			for (TableModelListener l : this.modelListeners) {
				l.tableChanged(event);
			}
			
			final StatusDialog sd = new StatusDialog(owner);
			new Thread(new Runnable() {
				public void run() {
					try {
						sd.setStatus("Caching changes");
						CardDatabase.cacheDatabase(db);
						db.buildMap(sd);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(owner, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace(System.err);
					} finally {
						sd.dispose();
					}
				}
			}).start();
		}
	}
}
