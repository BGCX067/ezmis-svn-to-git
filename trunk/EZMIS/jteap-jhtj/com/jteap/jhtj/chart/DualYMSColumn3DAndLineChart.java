package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 功能说明:Multi-series Column 3D Line Dual Y Combination chart
 * @author 童贝		
 * @version Jul 8, 2009
 */
public class DualYMSColumn3DAndLineChart {
	private String caption;
	private String showValues; 
	private String PYAxisName;//左边丛坐标名字 
	private String SYAxisName;//右边丛坐标名字
	private String formatNumberScale="0";//显示数据的格式
	private String baseFontSize;
	private String subCaption;
	private String xAxisName;
	private String PYAxisNameWidth;
	private String PYAxisMinValue;
	private List<DualYMSColumn3DAndLineDataSet> dualYMSColumn3DAndLineDataSets=new ArrayList<DualYMSColumn3DAndLineDataSet>();
	private DualYMSColumn3DAndLineCategories dualYMSColumn3DAndLineCategories=new DualYMSColumn3DAndLineCategories();
	
	private MSLineStyles mSLineStyles=new MSLineStyles();
	
	public DualYMSColumn3DAndLineCategories getDualYMSColumn3DAndLineCategories() {
		return dualYMSColumn3DAndLineCategories;
	}
	public void setDualYMSColumn3DAndLineCategories(
			DualYMSColumn3DAndLineCategories dualYMSColumn3DAndLineCategories) {
		this.dualYMSColumn3DAndLineCategories = dualYMSColumn3DAndLineCategories;
	}
	public List<DualYMSColumn3DAndLineDataSet> getDualYMSColumn3DAndLineDataSets() {
		return dualYMSColumn3DAndLineDataSets;
	}
	public void setDualYMSColumn3DAndLineDataSets(
			List<DualYMSColumn3DAndLineDataSet> dualYMSColumn3DAndLineDataSets) {
		this.dualYMSColumn3DAndLineDataSets = dualYMSColumn3DAndLineDataSets;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getShowValues() {
		return showValues;
	}
	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}
	public String getPYAxisName() {
		return PYAxisName;
	}
	public void setPYAxisName(String axisName) {
		PYAxisName = axisName;
	}
	public String getSYAxisName() {
		return SYAxisName;
	}
	public void setSYAxisName(String axisName) {
		SYAxisName = axisName;
	}
	
	@Override
	public String toString() {
		String result="<chart ";
		if(this.caption!=null&&!this.caption.equals("")){
			result=result+"caption=\""+this.getCaption()+"\" ";
		}
		if(this.showValues!=null&&!this.showValues.equals("")){
			result=result+"showValues=\""+this.showValues+"\" ";
		}
		if(this.PYAxisName!=null&&!this.PYAxisName.equals("")){
			result=result+"PYAxisName=\""+this.PYAxisName+"\" ";
		}
		if(this.SYAxisName!=null&&!this.SYAxisName.equals("")){
			result=result+"SYAxisName=\""+this.SYAxisName+"\" ";
		}
		if(this.formatNumberScale!=null&&!this.formatNumberScale.equals("")){
			result=result+"formatNumberScale=\""+this.formatNumberScale+"\" ";
		}
		if(this.baseFontSize!=null&&!this.baseFontSize.equals("")){
			result=result+"baseFontSize=\""+this.baseFontSize+"\" ";
		}
		if(this.subCaption!=null&&!this.subCaption.equals("")){
			result=result+"subCaption=\""+this.subCaption+"\" ";
		}
		if(this.xAxisName!=null&&!this.xAxisName.equals("")){
			result=result+"xAxisName=\""+this.xAxisName+"\" ";
		}
		if(this.PYAxisNameWidth!=null&&!this.PYAxisNameWidth.equals("")){
			result=result+"PYAxisNameWidth=\""+this.PYAxisNameWidth+"\" ";
		}
		if(this.PYAxisMinValue!=null&&!this.PYAxisMinValue.equals("")){
			result=result+"PYAxisMinValue=\""+this.PYAxisMinValue+"\" ";
		}
		result=result+">";
		
		result=result+this.dualYMSColumn3DAndLineCategories.toString();
		
		
		
		Iterator<DualYMSColumn3DAndLineDataSet> itC3DS=this.dualYMSColumn3DAndLineDataSets.iterator();
		while(itC3DS.hasNext()){
			DualYMSColumn3DAndLineDataSet cs=itC3DS.next();
			result=result+cs.toString();
		}
		
		result=result+this.mSLineStyles.toString();
		
		result=result+"</chart>";
		return result;
	}
	
	public String getTableToXml(){
		StringBuffer result=new StringBuffer();
		//记录集
		for(DualYMSColumn3DAndLineDataSet dataset:this.dualYMSColumn3DAndLineDataSets){
			StringBuffer curDataSet=new StringBuffer();
			int flag=0;
			for(DualYMSColumn3DAndLineCategorie category:this.getDualYMSColumn3DAndLineCategories().getDualYMSColumn3DAndLineCategories()){
				String label=category.getLabel();
				DualYMSColumn3DAndLineSet set=null;
				if(dataset.getDualYMSColumn3DAndLineSets().size()>flag){
					set=dataset.getDualYMSColumn3DAndLineSets().get(flag);
				}
				if(set==null){
					curDataSet.append("<property label=\""+label+"\" value=\"0\" />");
				}else{
					String value=set.getValue();
					curDataSet.append("<property label=\""+label+"\" value=\""+value+"\" />");
				}
				flag++;
			}
			if(!curDataSet.toString().equals("")){
				result.append("<dataset>"+curDataSet+"</dataset>");
			}
		}
		return "<root>"+result.toString()+"</root>";
	}
	
	
	public MSLineStyles getMSLineStyles() {
		return mSLineStyles;
	}
	public void setMSLineStyles(MSLineStyles lineStyles) {
		mSLineStyles = lineStyles;
	}
	public String getFormatNumberScale() {
		return formatNumberScale;
	}
	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}
	public String getBaseFontSize() {
		return baseFontSize;
	}
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	public String getSubCaption() {
		return subCaption;
	}
	public void setSubCaption(String subCaption) {
		this.subCaption = subCaption;
	}
	public String getXAxisName() {
		return xAxisName;
	}
	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}
	public String getPYAxisNameWidth() {
		return PYAxisNameWidth;
	}
	public void setPYAxisNameWidth(String axisNameWidth) {
		PYAxisNameWidth = axisNameWidth;
	}
	public String getPYAxisMinValue() {
		return PYAxisMinValue;
	}
	public void setPYAxisMinValue(String axisMinValue) {
		PYAxisMinValue = axisMinValue;
	}
}
