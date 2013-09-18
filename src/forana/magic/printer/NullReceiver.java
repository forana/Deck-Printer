package forana.magic.printer;

/** Null implementation of StatusReceiver. **/
public class NullReceiver implements StatusReceiver {
	public void setCompleted(int completed) {}
	public void setTotal(int total) {}
	public void setStatus(String str) {}
}
