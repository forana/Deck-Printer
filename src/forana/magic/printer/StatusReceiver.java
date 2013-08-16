package forana.magic.printer;

public interface StatusReceiver {
	public void setCompleted(int completed);
	public void setTotal(int total);
	public void setStatus(String str);
}
