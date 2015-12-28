package com.jteap.index.chart;

public class MSLineSet {
	private String anchorRadius;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		String result="<set ";
		if(this.value!=null){
			result=result+"value=\""+this.value+"\" ";
		}else if(this.anchorRadius!=null){
			result=result+"anchorRadius=\""+this.anchorRadius+"\" ";
		}
		
		result=result+"/>";
		return result;
	}

	public String getAnchorRadius() {
		return anchorRadius;
	}

	public void setAnchorRadius(String anchorRadius) {
		this.anchorRadius = anchorRadius;
	}

	
}
