package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MSColumn3DChart {
	private String caption; 
	private String xAxisName;
	private String yAxisName; 
	private String showValues="0"; 
	private String numberPrefix;//显示值的前缀
	private String shownames="1";
	private String formatNumberScale="0";//显示数据的格式
	private String decimals;
	private String showBorder;//是否显示边框
	private String baseFontSize="12";
	private String outCnvBaseFontSize;
	private String captionPadding;
	
	
	private MSColumn3DCategories msColumn3DCategories=new MSColumn3DCategories();
	private List<MSColumn3DDataSet> mSColumn3DDataSets=new ArrayList<MSColumn3DDataSet>();
	//private MSColumn3DTrendlines mSColumn3DTrendlines=new MSColumn3DTrendlines();
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
	public String getNumberPrefix() {
		return numberPrefix;
	}
	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}
	
	@Override
	public String toString() {
		String result="<chart ";
		if(this.caption!=null&&!this.caption.equals("")){
			result=result+"caption=\""+this.getCaption()+"\" ";
		}
		if(this.decimals!=null&&!this.decimals.equals("")){
			result=result+"decimals=\""+this.decimals+"\" ";
		}
		if(this.numberPrefix!=null&&!this.numberPrefix.equals("")){
			result=result+"numberPrefix=\""+this.numberPrefix+"\" ";
		}
		if(this.shownames!=null&&!this.shownames.equals("")){
			result=result+"shownames=\""+this.shownames+"\" ";
		}
		if(this.showValues!=null&&!this.showValues.equals("")){
			result=result+"showValues=\""+this.showValues+"\" ";
		}
		if(this.xAxisName!=null&&!this.xAxisName.equals("")){
			result=result+"xAxisName=\""+this.xAxisName+"\" ";
		}
		if(this.yAxisName!=null&&!this.yAxisName.equals("")){
			result=result+"yAxisName=\""+this.yAxisName+"\" ";
		}
		if(this.showBorder!=null&&!this.showBorder.equals("")){
			result=result+"showBorder=\""+this.showBorder+"\" ";
		}
		if(this.baseFontSize!=null&&!this.baseFontSize.equals("")){
			result=result+"baseFontSize=\""+this.baseFontSize+"\" ";
		}
		if(this.outCnvBaseFontSize!=null&&!this.outCnvBaseFontSize.equals("")){
			result=result+"outCnvBaseFontSize=\""+this.outCnvBaseFontSize+"\" ";
		}
		if(this.captionPadding!=null&&!this.captionPadding.equals("")){
			result=result+"captionPadding=\""+this.captionPadding+"\" ";
		}
		if(this.formatNumberScale!=null&&!this.formatNumberScale.equals("")){
			result=result+"formatNumberScale=\""+this.formatNumberScale+"\" ";
		}
		result=result+">";
		
		result=result+this.msColumn3DCategories.toString();
		
		Iterator<MSColumn3DDataSet> itC3DS=this.mSColumn3DDataSets.iterator();
		while(itC3DS.hasNext()){
			MSColumn3DDataSet cs=itC3DS.next();
			result=result+cs.toString();
		}
		result=result+"</chart>";
		//result=result+this.mSColumn3DTrendlines.toString()+"</chart>";
		return result;
	}
	
	public String getTableToXml(){
		StringBuffer result=new StringBuffer();
		//记录集
		for(MSColumn3DDataSet dataset:this.mSColumn3DDataSets){
			StringBuffer curDataSet=new StringBuffer();
			int flag=0;
			for(MSColumn3DCategory category:this.msColumn3DCategories.getMSColumn3DCategorys()){
				String label=category.getLabel();
				MSColumn3DSet set=null;
				if(dataset.getMSColumn3DSets().size()>flag){
					set=dataset.getMSColumn3DSets().get(flag);
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
	
	
	public String getShownames() {
		return shownames;
	}
	public void setShownames(String shownames) {
		this.shownames = shownames;
	}
	public String getDecimals() {
		return decimals;
	}
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}
	public MSColumn3DCategories getMsColumn3DCategories() {
		return msColumn3DCategories;
	}
	public void setMsColumn3DCategories(MSColumn3DCategories msColumn3DCategories) {
		this.msColumn3DCategories = msColumn3DCategories;
	}
	public List<MSColumn3DDataSet> getMSColumn3DDataSets() {
		return mSColumn3DDataSets;
	}
	public void setMSColumn3DDataSets(List<MSColumn3DDataSet> column3DDataSets) {
		mSColumn3DDataSets = column3DDataSets;
	}
//	public MSColumn3DTrendlines getMSColumn3DTrendlines() {
//		return mSColumn3DTrendlines;
//	}
//	public void setMSColumn3DTrendlines(MSColumn3DTrendlines column3DTrendlines) {
//		mSColumn3DTrendlines = column3DTrendlines;
//	}
	public String getShowBorder() {
		return showBorder;
	}
	public void setShowBorder(String showBorder) {
		this.showBorder = showBorder;
	}
	public String getBaseFontSize() {
		return baseFontSize;
	}
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	public String getOutCnvBaseFontSize() {
		return outCnvBaseFontSize;
	}
	public void setOutCnvBaseFontSize(String outCnvBaseFontSize) {
		this.outCnvBaseFontSize = outCnvBaseFontSize;
	}
	public String getCaptionPadding() {
		return captionPadding;
	}
	public void setCaptionPadding(String captionPadding) {
		this.captionPadding = captionPadding;
	}
	public String getFormatNumberScale() {
		return formatNumberScale;
	}
	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}
}
