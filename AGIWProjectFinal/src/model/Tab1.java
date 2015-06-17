package model;

public class Tab1 {

	private String TRECID;
	private String str;
	private String tag;
	
	public Tab1(String tRECID, 
			String str, String tag) {
		super();
		TRECID = tRECID;
		
		this.str = str;
		this.tag = tag;
	}

	public String getTRECID() {
		return TRECID;
	}

	public void setTRECID(String tRECID) {
		TRECID = tRECID;
	}

	

	
	@Override
	public String toString() {
		return "Tab1 [TRECID=" + TRECID + "\t str=" + str + "\t tag=" + tag + "]";
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String toFile() {	
		return TRECID + "\t" + str + "\t" + tag;
	}
	
	
	
	
}
