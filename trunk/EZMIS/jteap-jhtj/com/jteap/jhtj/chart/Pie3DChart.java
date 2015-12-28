package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * 功能说明:饼图
 * @author 童贝		
 * @version May 16, 2009
 */
public class Pie3DChart {
	private String caption="";
	private String palette="4"; 
	private String decimals="2";
	private String enableSmartLabels="1";//是否有展开的线
	private String enableRotation="0";//点击的时候是连接模式还是切片模式
	private String bgColor="99CCFF,FFFFFF";
	private String bgAlpha="40,100";
	private String bgRatio="0,100";
	private String bgAngle="360";
	private String showBorder="1";
	private String startingAngle="70";//饼图分开的角度
	private String slicingDistance=""; //饼图分开的距离
	private String pieRadius="125";//饼图半径的大小
	private String isSmartLineSlanted="";//智能线是直线还是斜线
	private String baseFontSize="13";
	private String forceDecimals="1";
	private String showPercentageValues="1";//显示百分比
	private String showPercentInToolTip="0";//提示实际值
	private String imageSave="";
	private String pieYScale="60";//控制显示角度
	private String imageSaveURL="";
	private String baseFontColor="";
	private String showPlotBorder="";
	private String plotBorderColor="";
	private String toolTipBorderColor="";//提示文本的边框
	private String showShadow="";//是否显示影子
	private String use3DLighting="";
	private String exportEnabled; 
	private String exportAtClient;
	private String exportHandler;
	private String exportAction;
	private String showExportDataMenuItem;
	private List<Pie3DSet> pie3DSet=new ArrayList<Pie3DSet>();
	public String getPalette() {
		return palette;
	}
	public void setPalette(String palette) {
		this.palette = palette;
	}
	public String getDecimals() {
		return decimals;
	}
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}
	public String getEnableSmartLabels() {
		return enableSmartLabels;
	}
	public void setEnableSmartLabels(String enableSmartLabels) {
		this.enableSmartLabels = enableSmartLabels;
	}
	public String getEnableRotation() {
		return enableRotation;
	}
	public void setEnableRotation(String enableRotation) {
		this.enableRotation = enableRotation;
	}
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	public String getBgAlpha() {
		return bgAlpha;
	}
	public void setBgAlpha(String bgAlpha) {
		this.bgAlpha = bgAlpha;
	}
	public String getBgRatio() {
		return bgRatio;
	}
	public void setBgRatio(String bgRatio) {
		this.bgRatio = bgRatio;
	}
	public String getBgAngle() {
		return bgAngle;
	}
	public void setBgAngle(String bgAngle) {
		this.bgAngle = bgAngle;
	}
	public String getShowBorder() {
		return showBorder;
	}
	public void setShowBorder(String showBorder) {
		this.showBorder = showBorder;
	}
	public String getStartingAngle() {
		return startingAngle;
	}
	public void setStartingAngle(String startingAngle) {
		this.startingAngle = startingAngle;
	}
	public String getSlicingDistance() {
		return slicingDistance;
	}
	public void setSlicingDistance(String slicingDistance) {
		this.slicingDistance = slicingDistance;
	}
	public String getPieRadius() {
		return pieRadius;
	}
	public void setPieRadius(String pieRadius) {
		this.pieRadius = pieRadius;
	}
	public String getIsSmartLineSlanted() {
		return isSmartLineSlanted;
	}
	public void setIsSmartLineSlanted(String isSmartLineSlanted) {
		this.isSmartLineSlanted = isSmartLineSlanted;
	}
	public List<Pie3DSet> getPie3DSet() {
		return pie3DSet;
	}
	public void setPie3DSet(List<Pie3DSet> pie3DSet) {
		this.pie3DSet = pie3DSet;
	}
	
	@Override
	public String toString() {
		String result="<chart ";
		if(!this.getPalette().equals("")){
			result=result+"palette=\""+this.getPalette()+"\" ";
		}
		if(!this.getDecimals().equals("")){
			result=result+"decimals=\""+this.getDecimals()+"\" ";
		}
		if(!this.getEnableSmartLabels().equals("")){
			result=result+"enableSmartLabels=\""+this.getEnableSmartLabels()+"\" ";
		}
		if(!this.getEnableRotation().equals("")){
			result=result+"enableRotation=\""+this.getEnableRotation()+"\" ";
		}
		if(!this.getBgColor().equals("")){
			result=result+"bgColor=\""+this.getBgColor()+"\" ";
		}
		if(!this.getBgAlpha().equals("")){
			result=result+"bgAlpha=\""+this.getBgAlpha()+"\" ";
		}
		if(!this.getBgRatio().equals("")){
			result=result+"bgRatio=\""+this.getBgRatio()+"\" ";
		}
		if(!this.getBgAngle().equals("")){
			result=result+"bgAngle=\""+this.getBgAngle()+"\" ";
		}
		if(!this.getShowBorder().equals("")){
			result=result+"showBorder=\""+this.getShowBorder()+"\" ";
		}
		if(!this.getStartingAngle().equals("")){
			result=result+"startingAngle=\""+this.getStartingAngle()+"\" ";
		}
		if(!this.getSlicingDistance().equals("")){
			result=result+"slicingDistance=\""+this.getSlicingDistance()+"\" ";
		}
		if(!this.getPieRadius().equals("")){
			result=result+"pieRadius=\""+this.getPieRadius()+"\" ";
		}
		if(!this.getIsSmartLineSlanted().equals("")){
			result=result+"isSmartLineSlanted=\""+this.getIsSmartLineSlanted()+"\"";
		}
		if(!this.getBaseFontSize().equals("")){
			result=result+"baseFontSize=\""+this.getBaseFontSize()+"\" ";
		}
		if(!this.getForceDecimals().equals("")){
			result=result+"forceDecimals=\""+this.getForceDecimals()+"\" ";
		}
		if(!this.getShowPercentageValues().equals("")){
			result=result+"showPercentageValues=\""+this.getShowPercentageValues()+"\" ";
		}
		if(!this.getShowPercentInToolTip().equals("")){
			result=result+"showPercentInToolTip=\""+this.getShowPercentInToolTip()+"\" ";
		}
		if(!this.getImageSave().equals("")){
			result=result+"imageSave=\""+this.getImageSave()+"\" ";
		}
		if(!this.getImageSaveURL().equals("")){
			result=result+"imageSaveURL=\""+this.getImageSaveURL()+"\" ";
		}
		if(!this.getCaption().equals("")){
			result=result+"caption=\""+this.getCaption()+"\" ";
		}
		if(!this.getPieYScale().equals("")){
			result=result+"pieYScale=\""+this.getPieYScale()+"\" ";
		}
		if(!this.baseFontColor.equals("")){
			result=result+"baseFontColor=\""+this.baseFontColor+"\" ";
		}
		if(!this.showPlotBorder.equals("")){
			result=result+"showPlotBorder=\""+this.showPlotBorder+"\" ";
		}
		if(!this.plotBorderColor.equals("")){
			result=result+"plotBorderColor=\""+this.plotBorderColor+"\" ";
		}
		if(!this.toolTipBorderColor.equals("")){
			result=result+"toolTipBorderColor=\""+this.toolTipBorderColor+"\" ";
		}
		if(!this.showShadow.equals("")){
			result=result+"showShadow=\""+this.showShadow+"\" ";
		}
		if(!this.use3DLighting.equals("")){
			result=result+"use3DLighting=\""+this.use3DLighting+"\" ";
		}
		if(this.exportAtClient!=null&&!this.exportAtClient.equals("")){
			result=result+"exportAtClient=\""+this.exportAtClient+"\" ";
		}
		if(this.exportEnabled!=null&&!this.exportEnabled.equals("")){
			result=result+"exportEnabled=\""+this.exportEnabled+"\" ";
		}
		if(this.exportHandler!=null&&!this.exportHandler.equals("")){
			result=result+"exportHandler=\""+this.exportHandler+"\" ";
		}
		if(this.exportAction!=null&&!this.exportAction.equals("")){
			result=result+"exportAction=\""+this.exportAction+"\" ";
		}
		if(this.showExportDataMenuItem!=null&&!this.showExportDataMenuItem.equals("")){
			result=result+"showExportDataMenuItem=\""+this.showExportDataMenuItem+"\" ";
		}
		result=result+">";
		Iterator<Pie3DSet> itPie=this.getPie3DSet().iterator();
		while(itPie.hasNext()){
			Pie3DSet pie3DSet=itPie.next();
			result=result+pie3DSet.toString();
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
	public String getForceDecimals() {
		return forceDecimals;
	}
	public void setForceDecimals(String forceDecimals) {
		this.forceDecimals = forceDecimals;
	}
	public String getShowPercentageValues() {
		return showPercentageValues;
	}
	public void setShowPercentageValues(String showPercentageValues) {
		this.showPercentageValues = showPercentageValues;
	}
	public String getShowPercentInToolTip() {
		return showPercentInToolTip;
	}
	public void setShowPercentInToolTip(String showPercentInToolTip) {
		this.showPercentInToolTip = showPercentInToolTip;
	}
	public String getImageSave() {
		return imageSave;
	}
	public void setImageSave(String imageSave) {
		this.imageSave = imageSave;
	}
	public String getImageSaveURL() {
		return imageSaveURL;
	}
	public void setImageSaveURL(String imageSaveURL) {
		this.imageSaveURL = imageSaveURL;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getPieYScale() {
		return pieYScale;
	}
	public void setPieYScale(String pieYScale) {
		this.pieYScale = pieYScale;
	}
	public String getBaseFontColor() {
		return baseFontColor;
	}
	public void setBaseFontColor(String baseFontColor) {
		this.baseFontColor = baseFontColor;
	}
	public String getShowPlotBorder() {
		return showPlotBorder;
	}
	public void setShowPlotBorder(String showPlotBorder) {
		this.showPlotBorder = showPlotBorder;
	}
	public String getPlotBorderColor() {
		return plotBorderColor;
	}
	public void setPlotBorderColor(String plotBorderColor) {
		this.plotBorderColor = plotBorderColor;
	}
	public String getToolTipBorderColor() {
		return toolTipBorderColor;
	}
	public void setToolTipBorderColor(String toolTipBorderColor) {
		this.toolTipBorderColor = toolTipBorderColor;
	}
	public String getShowShadow() {
		return showShadow;
	}
	public void setShowShadow(String showShadow) {
		this.showShadow = showShadow;
	}
	public String getUse3DLighting() {
		return use3DLighting;
	}
	public void setUse3DLighting(String use3DLighting) {
		this.use3DLighting = use3DLighting;
	}
	public String getExportEnabled() {
		return exportEnabled;
	}
	public void setExportEnabled(String exportEnabled) {
		this.exportEnabled = exportEnabled;
	}
	public String getExportAtClient() {
		return exportAtClient;
	}
	public void setExportAtClient(String exportAtClient) {
		this.exportAtClient = exportAtClient;
	}
	public String getExportHandler() {
		return exportHandler;
	}
	public void setExportHandler(String exportHandler) {
		this.exportHandler = exportHandler;
	}
	public String getExportAction() {
		return exportAction;
	}
	public void setExportAction(String exportAction) {
		this.exportAction = exportAction;
	}
	public String getShowExportDataMenuItem() {
		return showExportDataMenuItem;
	}
	public void setShowExportDataMenuItem(String showExportDataMenuItem) {
		this.showExportDataMenuItem = showExportDataMenuItem;
	}
}
