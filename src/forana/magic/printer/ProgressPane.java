package forana.magic.printer;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressPane extends JFrame implements StatusReceiver {
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JProgressBar bar;
	
	public ProgressPane(String initialText, int width, int height) {
		super();
		
		this.setLayout(new BorderLayout());
		
		this.label = new JLabel(initialText);
		this.bar = new JProgressBar(0, 1);
		
		this.add(this.label, BorderLayout.NORTH);
		this.add(this.bar, BorderLayout.SOUTH);
		
		this.setLocationByPlatform(true);
		this.setSize(width, height);
		this.setVisible(true);
	}
	
	public void setTotal(int total) {
		this.bar.setMaximum(total);
	}
	
	public void setCompleted(int completed) {
		this.bar.setValue(completed);
	}
	
	public void setStatus(String status) {
		this.label.setText(status);
	}
}
