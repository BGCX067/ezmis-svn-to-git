package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * 功能说明:柱状图(单个)
 * @author 童贝		
 * @version May 16, 2009
 */
public class Column3DChart {
	private String caption;
	private String xAxisName;
	private String yAxisName;
	private String showValues="0";
	private String decimals="4" ;
	private String formatNumberScale="0";
	private String baseFontSize;
	private List<Column3DSet> column3DSets=new ArrayList<Column3DSet>();

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getXAxisName() {
		return xAxisName;
	}

	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}

	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	public String getShowValues() {
		return showValues;
	}

	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}

	public String getDecimals() {
		return decimals;
	}

	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}

	public String getFormatNumberScale() {
		return formatNumberScale;
	}

	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}

	public List<Column3DSet> getColumn3DSets() {
		return column3DSets;
	}

	public void setColumn3DSets(List<Column3DSet> column3DSets) {
		this.column3DSets = column3DSets;
	}
	
	@Override
	public String toString() {
		String result="<chart ";
		if(this.caption!=null){
			result=result+"caption=\""+this.getCaption()+"\" ";
		}
		if(this.xAxisName!=null){
			result=result+"xAxisName=\""+this.getXAxisName()+"\" ";
		}
		if(this.yAxisName!=null){
			result=result+"yAxisName=\""+this.getYAxisName()+"\" ";
		}
		if(this.baseFontSize!=null){
			result=result+"baseFontSize=\""+this.baseFontSize+"\" ";
		}
		result=result+"showValues=\""+this.getShowValues()+"\" decimals=\""+this.getDecimals()+"\" formatNumberScale=\""+this.getFormatNumberScale()+"\">";
		
		Iterator<Column3DSet> itC3DS=this.getColumn3DSets().iterator();
		while(itC3DS.hasNext()){
			Column3DSet cs=itC3DS.next();
			result=result+cs.toString();
		}
		result=result+"</chart>";
		return result;
	}

	public String getBaseFontSize() {
		return baseFontSize;
	}

	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
}
