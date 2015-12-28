package com.jteap.jhtj.chart;
/**
 * 
 * 功能说明:set
 * @author 童贝		
 * @version Jul 8, 2009
 */
public class DualYMSColumn3DAndLineSet {
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
