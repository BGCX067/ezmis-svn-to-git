package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 
 * 功能说明:折线图
 * @author 童贝		
 * @version May 11, 2009
 */
public class MSLineChart {
	private String caption; 
	private String subcaption; 
	private String lineThickness;
	private String showValues="0";
	private String formatNumberScale="0";
	private String anchorRadius="2";
	private String divLineAlpha="20";
	private String divLineColor="00676B";
	private String divLineIsDashed="1";
	private String showAlternateHGridColor="1";
	private String alternateHGridAlpha="5";
	private String alternateHGridColor="00676B";//"CC3300";
	private String shadowAlpha="40";
	private String labelStep="4";
	private String numvdivlines="5";
	private String chartRightMargin="35";
	private String bgColor="FFFFFF,00676B";
	private String bgAngle="270";
	private String bgAlpha="10,10";
	private String baseFontSize;
	private String numVisiblePlot;
	private String yAxisMaxValue;
	private String yAxisMinValue ;
	private String yAxisValuesStep;
	
	private MSLineCategories mSLineCategories=new MSLineCategories();
	private List<MSLineDataSet> mSLineDataSets=new ArrayList<MSLineDataSet>();
	private MSLineStyles mSLineStyles=new MSLineStyles();
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getSubcaption() {
		return subcaption;
	}
	public void setSubcaption(String subcaption) {
		this.subcaption = subcaption;
	}
	public String getLineThickness() {
		return lineThickness;
	}
	public void setLineThickness(String lineThickness) {
		this.lineThickness = lineThickness;
	}
	public String getShowValues() {
		return showValues;
	}
	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}
	public String getFormatNumberScale() {
		return formatNumberScale;
	}
	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}
	public String getAnchorRadius() {
		return anchorRadius;
	}
	public void setAnchorRadius(String anchorRadius) {
		this.anchorRadius = anchorRadius;
	}
	public String getDivLineAlpha() {
		return divLineAlpha;
	}
	public void setDivLineAlpha(String divLineAlpha) {
		this.divLineAlpha = divLineAlpha;
	}
	public String getDivLineColor() {
		return divLineColor;
	}
	public void setDivLineColor(String divLineColor) {
		this.divLineColor = divLineColor;
	}
	public String getDivLineIsDashed() {
		return divLineIsDashed;
	}
	public void setDivLineIsDashed(String divLineIsDashed) {
		this.divLineIsDashed = divLineIsDashed;
	}
	public String getShowAlternateHGridColor() {
		return showAlternateHGridColor;
	}
	public void setShowAlternateHGridColor(String showAlternateHGridColor) {
		this.showAlternateHGridColor = showAlternateHGridColor;
	}
	public String getAlternateHGridAlpha() {
		return alternateHGridAlpha;
	}
	public void setAlternateHGridAlpha(String alternateHGridAlpha) {
		this.alternateHGridAlpha = alternateHGridAlpha;
	}
	public String getAlternateHGridColor() {
		return alternateHGridColor;
	}
	public void setAlternateHGridColor(String alternateHGridColor) {
		this.alternateHGridColor = alternateHGridColor;
	}
	public String getShadowAlpha() {
		return shadowAlpha;
	}
	public void setShadowAlpha(String shadowAlpha) {
		this.shadowAlpha = shadowAlpha;
	}
	public String getLabelStep() {
		return labelStep;
	}
	public void setLabelStep(String labelStep) {
		this.labelStep = labelStep;
	}
	public String getNumvdivlines() {
		return numvdivlines;
	}
	public void setNumvdivlines(String numvdivlines) {
		this.numvdivlines = numvdivlines;
	}
	public String getChartRightMargin() {
		return chartRightMargin;
	}
	public void setChartRightMargin(String chartRightMargin) {
		this.chartRightMargin = chartRightMargin;
	}
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	public String getBgAngle() {
		return bgAngle;
	}
	public void setBgAngle(String bgAngle) {
		this.bgAngle = bgAngle;
	}
	public String getBgAlpha() {
		return bgAlpha;
	}
	public void setBgAlpha(String bgAlpha) {
		this.bgAlpha = bgAlpha;
	}
	public MSLineCategories getMSLineCategories() {
		return mSLineCategories;
	}
	public void setMSLineCategories(MSLineCategories lineCategories) {
		mSLineCategories = lineCategories;
	}

	public List<MSLineDataSet> getMSLineDataSets() {
		return mSLineDataSets;
	}
	public void setMSLineDataSets(List<MSLineDataSet> lineDataSets) {
		mSLineDataSets = lineDataSets;
	}
	public MSLineStyles getMSLineStyles() {
		return mSLineStyles;
	}
	public void setMSLineStyles(MSLineStyles lineStyles) {
		mSLineStyles = lineStyles;
	}
	
	@Override
	public String toString() {
		String result="<chart ";
		if(this.caption!=null&&!this.caption.equals("")){
			result=result+"caption=\""+this.getCaption()+"\" ";
		}
		if(this.subcaption!=null&&!this.subcaption.equals("")){
			result=result+"subcaption=\""+this.subcaption+"\" ";
		}
		if(this.lineThickness!=null&&!this.lineThickness.equals("")){
			result=result+"lineThickness=\""+this.lineThickness+"\" ";
		}
		if(this.showValues!=null&&!this.showValues.equals("")){
			result=result+"showValues=\""+this.showValues+"\" ";
		}
		if(this.formatNumberScale!=null&&!this.formatNumberScale.equals("")){
			result=result+"formatNumberScale=\""+this.formatNumberScale+"\" ";
		}
		if(this.anchorRadius!=null&&!this.anchorRadius.equals("")){
			result=result+"anchorRadius=\""+this.anchorRadius+"\" ";
		}
		if(this.divLineAlpha!=null&&!this.divLineAlpha.equals("")){
			result=result+"divLineAlpha=\""+this.divLineAlpha+"\" ";
		}
		if(this.divLineColor!=null&&!this.divLineColor.equals("")){
			result=result+"divLineColor=\""+this.divLineColor+"\" ";
		}
		if(this.divLineIsDashed!=null&&!this.divLineIsDashed.equals("")){
			result=result+"divLineIsDashed=\""+this.divLineIsDashed+"\" ";
		}
		if(this.showAlternateHGridColor!=null&&!this.showAlternateHGridColor.equals("")){
			result=result+"showAlternateHGridColor=\""+this.showAlternateHGridColor+"\" ";
		}
		if(this.alternateHGridAlpha!=null&&!this.alternateHGridAlpha.equals("")){
			result=result+"alternateHGridAlpha=\""+this.alternateHGridAlpha+"\" ";
		}
		if(this.alternateHGridColor!=null&&!this.alternateHGridColor.equals("")){
			result=result+"alternateHGridColor=\""+this.alternateHGridColor+"\" ";
		}
		if(this.shadowAlpha!=null&&!this.shadowAlpha.equals("")){
			result=result+"shadowAlpha=\""+this.shadowAlpha+"\" ";
		}
		if(this.labelStep!=null&&!this.labelStep.equals("")){
			result=result+"labelStep=\""+this.labelStep+"\" ";
		}
		if(this.numvdivlines!=null&&!this.numvdivlines.equals("")){
			result=result+"numvdivlines=\""+this.numvdivlines+"\" ";
		}
		if(this.chartRightMargin!=null&&!this.chartRightMargin.equals("")){
			result=result+"chartRightMargin=\""+this.chartRightMargin+"\" ";
		}
		if(this.bgColor!=null&&!this.bgColor.equals("")){
			result=result+"bgColor=\""+this.bgColor+"\" ";
		}
		if(this.bgAngle!=null&&!this.bgAngle.equals("")){
			result=result+"bgAngle=\""+this.bgAngle+"\" ";
		}
		if(this.bgAlpha!=null&&!this.bgAlpha.equals("")){
			result=result+"bgAlpha=\""+this.bgAlpha+"\" ";
		}
		if(this.baseFontSize!=null&&!this.baseFontSize.equals("")){
			result=result+"baseFontSize=\""+this.baseFontSize+"\" ";
		}
		if(this.numVisiblePlot!=null&&!this.numVisiblePlot.equals("")){
			result=result+"numVisiblePlot=\""+this.numVisiblePlot+"\" ";
		}
		if(this.yAxisMaxValue!=null&&!this.yAxisMaxValue.equals("")){
			result=result+"yAxisMaxValue=\""+this.yAxisMaxValue+"\" ";
		}
		if(this.yAxisMinValue!=null&&!this.yAxisMinValue.equals("")){
			result=result+"yAxisMinValue=\""+this.yAxisMinValue+"\" ";
		}
		if(this.yAxisValuesStep!=null&&!this.yAxisValuesStep.equals("")){
			result=result+"yAxisValuesStep=\""+this.yAxisValuesStep+"\" ";
		}
		result=result+">";
		
		result=result+this.getMSLineCategories().toString();
		
		Iterator<MSLineDataSet> itMSDS=this.getMSLineDataSets().iterator();
		while(itMSDS.hasNext()){
			MSLineDataSet msds=itMSDS.next();
			result=result+msds.toString();
		}
		
		result=result+this.getMSLineStyles().toString();
		
		result=result+"</chart>";
		return result;
	}
	
	/**
	 * 
	 *描述：得到该图形下的数据以json的形式返回
	 *时间：2010-5-6
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getTableToXml(){
		StringBuffer result=new StringBuffer();
//		for(MSLineDataSet dataset:mSLineDataSets){
//			StringBuffer curDataSet=new StringBuffer();
//			int flag=0;
//			for(MSLineCategory category:mSLineCategories.getCategorys()){
//				String label=category.getLabel();
//				MSLineSet set=null;
//				if(dataset.getMSLineSets().size()>flag){
//					set=dataset.getMSLineSets().get(flag);
//				}
//				if(set==null){
//					curDataSet.append("'"+label+"':'0',");
//				}else{
//					String value=set.getValue();
//					curDataSet.append("'"+label+"':'"+value+"',");
//				}
//				flag++;
//			}
//			if(!curDataSet.toString().equals("")){
//				result.append("{"+curDataSet.deleteCharAt(curDataSet.length()-1)+"},");
//			}
//		}
//		if(!result.toString().equals("")){
//			result.deleteCharAt(result.length()-1);
//		}
		
		//记录集
		for(MSLineDataSet dataset:this.mSLineDataSets){
			StringBuffer curDataSet=new StringBuffer();
			int flag=0;
			for(MSLineCategory category:this.getMSLineCategories().getCategorys()){
				String label=category.getLabel();
				MSLineSet set=null;
				if(dataset.getMSLineSets().size()>flag){
					set=dataset.getMSLineSets().get(flag);
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
	
	public String getBaseFontSize() {
		return baseFontSize;
	}
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	public String getNumVisiblePlot() {
		return numVisiblePlot;
	}
	public void setNumVisiblePlot(String numVisiblePlot) {
		this.numVisiblePlot = numVisiblePlot;
	}
	public String getYAxisMaxValue() {
		return yAxisMaxValue;
	}
	public void setYAxisMaxValue(String axisMaxValue) {
		yAxisMaxValue = axisMaxValue;
	}
	public String getYAxisValuesStep() {
		return yAxisValuesStep;
	}
	public void setYAxisValuesStep(String axisValuesStep) {
		yAxisValuesStep = axisValuesStep;
	}
	public String getYAxisMinValue() {
		return yAxisMinValue;
	}
	public void setYAxisMinValue(String axisMinValue) {
		yAxisMinValue = axisMinValue;
	}
}
