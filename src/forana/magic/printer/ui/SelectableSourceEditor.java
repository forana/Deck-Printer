package forana.magic.printer.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import forana.magic.printer.model.CardSource;
import forana.magic.printer.model.SelectableCardSource;

public class SelectableSourceEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		final SelectableCardSource source = (SelectableCardSource)value;
		final JComboBox<CardSource> box = new JComboBox<>(source.getSources().toArray(new CardSource[0]));
		
		box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				source.setSelectedIndex(box.getSelectedIndex());
			}
		});
		
		return box;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}
}
