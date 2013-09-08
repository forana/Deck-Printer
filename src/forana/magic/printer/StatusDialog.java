package forana.magic.printer;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class StatusDialog extends JDialog implements StatusReceiver {
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JProgressBar bar;
	
	public StatusDialog(Frame owner) {
		super(owner, "Magic Printer", false);
		
		this.label = new JLabel();
		this.bar = new JProgressBar();
		
		this.setLocationByPlatform(true);
		this.setSize(400, 100);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 5;
		c.ipady = 5;
		c.anchor = GridBagConstraints.PAGE_END;
		c.weightx = 0.5;
		c.weighty = 0.5;
		
		this.add(this.label, c);
		
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		
		this.add(this.bar, c);
		
		this.setVisible(true);
		this.requestFocus();
	}
	
	public void setStatus(final String status) {
		label.setText(status);
	}
	
	public void setCompleted(int completed) {
		this.bar.setValue(completed);
	}
	
	public void setTotal(int total) {
		this.bar.setMaximum(total);
	}
}
