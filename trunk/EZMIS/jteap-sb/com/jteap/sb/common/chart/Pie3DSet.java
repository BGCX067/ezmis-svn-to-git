package com.jteap.sb.common.chart;

public class Pie3DSet {
	private String borderColor="";
	private String isSliced="0"; //是否突出显示
	private String label="";
	private String value="";
	private String color="";
	private String link="";
	private String toolText="";
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public String getIsSliced() {
		return isSliced;
	}
	public void setIsSliced(String isSliced) {
		this.isSliced = isSliced;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getToolText() {
		return toolText;
	}
	public void setToolText(String toolText) {
		this.toolText = toolText;
	}
	
	@Override
	public String toString() {
		String result="<set ";
		if(!this.label.equals("")){
			result=result+" label=\""+this.getLabel()+"\" ";
		}
		if(!this.value.equals("")){
			result=result+" value=\""+this.getValue()+"\" ";
		}
		if(!this.borderColor.equals("")){
			result=result+"borderColor=\""+this.borderColor+"\" ";
		}
		if(!this.color.equals("")){
			result=result+"color=\""+this.color+"\" ";
		}
		if(!this.isSliced.equals("")){
			result=result+"isSliced=\""+this.isSliced+"\" ";
		}
		if(!this.link.equals("")){
			result=result+"link=\""+this.link+"\" ";
		}
		if(!this.toolText.equals("")){
			result=result+"toolText=\""+this.toolText+"\" ";
		}
		result=result+" />";
		return result;
	}
}
