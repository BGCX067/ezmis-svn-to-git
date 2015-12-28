/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.manager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jteap.index.chart.MSLineApplication;
import com.jteap.index.chart.MSLineApply;
import com.jteap.index.chart.MSLineCategories;
import com.jteap.index.chart.MSLineCategory;
import com.jteap.index.chart.MSLineChart;
import com.jteap.index.chart.MSLineDataSet;
import com.jteap.index.chart.MSLineDefinition;
import com.jteap.index.chart.MSLineSet;
import com.jteap.index.chart.MSLineStyle;
import com.jteap.index.chart.MSLineStyles;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 机组负荷Manager
 * @author caihuiwen
 */
public class JizuFhqxManager  extends JdbcManager{
	
	/**
	 * 根据机组号获取负荷数据 返回当前班次8小时曲线数据
	 * @param jzNum 	机组号
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Map<String, Object> findByCdDesc(String jzNum){
		Map<String, Object> map = new HashMap<String, Object>();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMM");
		
		Date nowDate = new Date();
		/** 构建多条折线图 */ 
		MSLineChart lineChart = new MSLineChart();
		lineChart.setSubcaption("");
		lineChart.setLabelStep("1");
		lineChart.setDivLineAlpha("1");
//		lineChart.setBaseFontSize("12");
		String [] color={"F1683C","2AD62A","DBDC25"};
		
		//单根线对象
		MSLineCategories mSLineCategories = new MSLineCategories();
		MSLineDataSet dataSet = new MSLineDataSet();
		dataSet.setColor(color[0]);
		dataSet.setAnchorBgColor(color[0]);
		dataSet.setAnchorBorderColor(color[0]);
		
		if("1".equals(jzNum) || "2".equals(jzNum)){
			/** 1、2机组负荷 */
			double maxValue = 0d;
			double minValue = 0d;
			Connection conn = null;
			try {
				/*String serverName = SystemConfig.getProperty("ED_SERVER12_ADDRESS");
				String dbName = SystemConfig.getProperty("ED_SERVER12_DBNAME");
				String userName = SystemConfig.getProperty("ED_SERVER12_USERNAME");
				String userPwd = SystemConfig.getProperty("ED_SERVER12_USERPWD");
				
				conn = DBHelper.getConn(serverName, dbName, userName, userPwd);*/
				conn = this.getConnection();
				for (int j = 5; j >= 0; j--) {
					String arrayFz[] = new String[]{":00",":20",":40"};
					
					for(int k=0; k<arrayFz.length; k++){
						if(j == 0 && k > 0){
							break;
						}
						
						Calendar cal = Calendar.getInstance();    
						cal.setTime(nowDate);    
						cal.add(Calendar.HOUR_OF_DAY,-j);       
						
						//循环获取6小时之前的每一小时
						int currentHour = cal.getTime().getHours();
						//循环获取当前时间点的日期 ymd
						String currentYmd = dateFormat.format(cal.getTime());
						//当前ym
						String currentYm = dateFormat2.format(cal.getTime());
						//处理动态取数表的日期后缀
						String qushuY = currentYm.substring(0,4);
						String qushuM = currentYm.substring(4,6);
						int intM = Integer.parseInt(qushuM);
						currentYm = qushuY + intM;
						
						String currentHourStr = "";
						if(currentHour < 10){
							currentHourStr = "0" + currentHour;
						}else {
							currentHourStr = "" + currentHour;
						}
						currentHourStr += arrayFz[k];
						
						//SS取数表名称  实际负荷 qs_101
						//String sisTableName = "ls_" + jzNum + "01_" + currentYm;
						String sisTableName = "ls_" + jzNum + "01";
						//SS取数字段(负荷)
//						String sisColumnName = "c11";
						String sisColumnName = "c1";
//						double data = this.getSsFh(conn, sisTableName, sisColumnName, currentHourStr, currentYmd);
						double data = this.getSisFh(conn, sisTableName, sisColumnName, currentHourStr,currentYmd);
						
						if(j == 5){
							maxValue = data;
							minValue = data;
						}else{
							if(data > maxValue){
								maxValue = data;
							}else if(data < minValue){
								minValue = data;
							}
						}
						
						MSLineSet set = new MSLineSet();
						MSLineCategory ca = new MSLineCategory();
						if(k == 0){
							ca.setLabel(currentHour+"");
						}else {
							ca.setLabel("");
						}
						set.setValue(decimalFormat.format(data));
						
						dataSet.getMSLineSets().add(set);
						mSLineCategories.getCategorys().add(ca);
					}
				}
				BigDecimal maxDecimal = new BigDecimal(maxValue);
				maxDecimal = maxDecimal.setScale(-1,BigDecimal.ROUND_UP);
				BigDecimal minDecimal = new BigDecimal(minValue);
				minDecimal = minDecimal.setScale(-1,BigDecimal.ROUND_UP);
				//如果负荷小于0 则视为停机状态 修改负荷曲线
				if(maxValue<=0){
					lineChart.setYAxisMaxValue((maxDecimal.intValue()+19)+"");
					lineChart.setYAxisMinValue((minDecimal.intValue()+10)+"");
				}else{
					lineChart.setYAxisMaxValue((maxDecimal.intValue()+10)+"");
					lineChart.setYAxisMinValue((minDecimal.intValue()-10)+"");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else if("3".equals(jzNum) || "4".equals(jzNum)){
			/** 3、4机组负荷 */
			double maxValue = 0d;
			double minValue = 0d;
			Connection conn = null;
			try {
				conn = this.getConnection();
				
				for (int j = 5; j >= 0; j--) {
					String arrayFz[] = new String[]{":00",":20",":40"};
					
					for(int k=0; k<arrayFz.length; k++){
						if(j == 0 && k > 0){
							break;
						}
						Calendar cal = Calendar.getInstance();    
				    	cal.setTime(nowDate);    
				    	cal.add(Calendar.HOUR_OF_DAY,-j);   
				    	
				    	//循环获取8小时之前的每一小时
				    	int currentHour = cal.getTime().getHours();
				    	//循环获取当前时间点的日期 ymd
				    	String currentYmd = dateFormat.format(cal.getTime());
				    	//当前ym
				    	String currentYm = dateFormat2.format(cal.getTime());
				    	
						String currentHourStr = "";
						if(currentHour < 10){
							currentHourStr = "0" + currentHour;
						}else {
							currentHourStr = "" + currentHour;
						}
						currentHourStr += arrayFz[k];
						
						//SS取数表名称  实际负荷 qs_101
						String sisTableName = "ls_" + jzNum + "01_" + currentYm;
						//SIS取数字段(负荷)
						String sisColumnName = "c1";
						double data = this.getSisFh(conn, sisTableName, sisColumnName, currentHourStr,currentYmd);
						
						if(j == 5){
							maxValue = data;
							minValue = data;
						}else{
							if(data > maxValue){
								maxValue = data;
							}else if(data < minValue){
								minValue = data;
							}
						}
						
						MSLineSet set = new MSLineSet();
						MSLineCategory ca = new MSLineCategory();
						if(k == 0){
							ca.setLabel(currentHour+"");
						}else {
							ca.setLabel("");
						}
						set.setValue(decimalFormat.format(data));
						
						dataSet.getMSLineSets().add(set);
						mSLineCategories.getCategorys().add(ca);
					}
				}
				BigDecimal maxDecimal = new BigDecimal(maxValue);
				maxDecimal = maxDecimal.setScale(-1,BigDecimal.ROUND_UP);
				BigDecimal minDecimal = new BigDecimal(minValue);
				minDecimal = minDecimal.setScale(-1,BigDecimal.ROUND_UP);
				//如果负荷小于0 则视为停机状态 修改负荷曲线
				if(maxValue<=0){
					lineChart.setYAxisMaxValue((maxDecimal.intValue()+19)+"");
					lineChart.setYAxisMinValue((minDecimal.intValue()+10)+"");
				}else{
					lineChart.setYAxisMaxValue((maxDecimal.intValue()+10)+"");
					lineChart.setYAxisMinValue((minDecimal.intValue()-10)+"");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
			
		
		MSLineStyle style = new MSLineStyle();
		style.setName("captionFont");
		
		MSLineDefinition defin = new MSLineDefinition();
		defin.setMSLineStyle(style);
		
		MSLineApply apply = new MSLineApply();
		MSLineApplication applition = new MSLineApplication();
		applition.getMSLineApplys().add(apply);
		
		MSLineStyles styles = new MSLineStyles();
		styles.setMSLineApplication(applition);
		styles.setMSLineDefinition(defin);
		
		lineChart.setMSLineStyles(styles);
		lineChart.getMSLineDataSets().add(dataSet);
		lineChart.setMSLineCategories(mSLineCategories);
		map.put("chartData", lineChart.toString());
		return map; 
	}
	
	/**
	 * 获取sis中的数据 3、4机组
	 * @param tableName		表名称
	 * @param columnName	字段名称
	 * @param hour			取数小时
	 * @return
	 */
	public double getSisFh(Connection conn, String tableName, String columnName, String hour, String currentYmd){
		double data = 0.00;
		
		Statement statement = null;
		ResultSet resultSet = null;
		
		String sql = "select * from sisdata." + tableName + " t where to_char(times,'yyyy-MM-dd HH24:mi')>='" 
					+ currentYmd + " " + hour + "' order by times asc";
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				data = resultSet.getDouble(columnName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(statement != null){
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
	
	/**
	 * 获取ss中的数据 1、2机组 (SQL Server 2000)
	 * @param tableName		表名称
	 * @param columnName	字段名称
	 * @param hour			取数小时
	 * @param nowYmd		当前ymd
	 * @return
	 */
	public double getSsFh(Connection conn, String tableName, String columnName, String hour, String nowYmd){
		double data = 0.00;
		if(conn == null){
			return data;
		}
		
		String sql = "select " + columnName + " from ssdata.dbo." + tableName +
			" t where time>='"+nowYmd+" "+hour+":00:00"+"' and time<='"+nowYmd+" "+hour+":59:59"+"' order by time desc";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				data = resultSet.getDouble(columnName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(statement != null){
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
	
}
