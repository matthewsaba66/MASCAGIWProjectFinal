package model;

public class Tab3 {
	
	private String TRECID;
	private String new_sent;
	
	public Tab3(String tRECID, String new_sent) {
		super();
		TRECID = tRECID;
		this.new_sent = new_sent;
	}

	public String getTRECID() {
		return TRECID;
	}

	public void setTRECID(String tRECID) {
		TRECID = tRECID;
	}


	public String getNew_sent() {
		return new_sent;
	}

	public void setNew_sent(String new_sent) {
		this.new_sent = new_sent;
	}

	@Override
	public String toString() {
		return "Tab3 [TRECID=" + TRECID + "\t new_sent=" + new_sent + "]";
	}

	public String toFile() {
		// TODO Auto-generated method stub
		return TRECID + "\t" + new_sent;
	}
	
	

}
