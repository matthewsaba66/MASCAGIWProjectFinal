package model;

public class Tab2 {
	
	private String TRECID;
	private String old_sent;
	
	
	public Tab2(String tRECID, String old_sent) {
		super();
		TRECID = tRECID;
		this.old_sent = old_sent;
	}

	public String getTRECID() {
		return TRECID;
	}

	public void setTRECID(String tRECID) {
		TRECID = tRECID;
	}

	public String getOld_sent() {
		return old_sent;
	}

	public void setOld_sent(String old_sent) {
		this.old_sent = old_sent;
	}

	

	@Override
	public String toString() {
		return "Tab2 [TRECID=" + TRECID + "\t old_sent=" + old_sent + "]";
	}

	public String toFile() {
		// TODO Auto-generated method stub
		return TRECID + "\t" + old_sent;
	}
	
	

}
