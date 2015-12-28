package com.jteap.jhtj.chart;

public class RadarSet {
	private String value;
	private String showValue="0";
	private String displayValue;
	private String anchorRadius; 

	public String getAnchorRadius() {
		return anchorRadius;
	}

	public void setAnchorRadius(String anchorRadius) {
		this.anchorRadius = anchorRadius;
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		String result="<set ";
		if(this.value!=null&&!this.value.equals("")){
			result=result+"value=\""+this.getValue()+"\" ";
		}
		if(this.showValue!=null&&!this.showValue.equals("")){
			result=result+"showValue=\""+this.showValue+"\" ";
		}
		if(this.anchorRadius!=null&&!this.anchorRadius.equals("")){
			result=result+"anchorSides=\""+this.anchorRadius+"\" ";
		}
		if(this.displayValue!=null&&!this.displayValue.equals("")){
			result=result+"displayValue=\""+this.displayValue+"\" ";
		}
		result=result+"/>";
		return result;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
}
