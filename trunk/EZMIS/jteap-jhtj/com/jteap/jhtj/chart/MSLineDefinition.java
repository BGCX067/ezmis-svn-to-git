package com.jteap.jhtj.chart;

/**
 * 
 * 功能说明:
 * @author 童贝		
 * @version May 11, 2009
 */
public class MSLineDefinition {
	private MSLineStyle mSLineStyle;

	public MSLineStyle getMSLineStyle() {
		return mSLineStyle;
	}

	public void setMSLineStyle(MSLineStyle lineStyle) {
		mSLineStyle = lineStyle;
	}
	
	@Override
	public String toString() {
		String result="<definition>";
		result=result+this.mSLineStyle.toString();
		result=result+"</definition>";
		return result;
	}
}
