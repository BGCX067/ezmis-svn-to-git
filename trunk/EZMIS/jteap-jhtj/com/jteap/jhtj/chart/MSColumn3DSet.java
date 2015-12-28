package com.jteap.jhtj.chart;

public class MSColumn3DSet {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		String result="";
		if(this.value!=null&&!this.value.equals("")){
			result="<set value=\""+this.getValue()+"\" />";
		}
		return result;
	}
}
