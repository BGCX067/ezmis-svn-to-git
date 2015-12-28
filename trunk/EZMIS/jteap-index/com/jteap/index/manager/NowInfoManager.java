/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.jteap.core.support.SystemConfig;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.yx.fdzbfxykh.manager.DBHelper;

/**
 * 实时信息Manager
 * @author caihuiwen
 */
public class NowInfoManager extends JdbcManager{
	
	/**
	 * 获取实时信息
	 * @return
	 */
	public Map<String, Object> findNowInfoMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		//全长总负荷
		double qczFh = 0;
		//#1、2机组负荷
		double[] jizuFh12 = this.findFhBySs2();
		//#3、4机组负荷
		double[] jizuFh34 = this.findFhBySis();
		 
		qczFh = jizuFh12[0] + jizuFh12[1] + jizuFh34[0] + jizuFh34[1]; 
		
		//获取机组运行参数
		Map<String, Object> runMap = this.findJzRunParams();
		String status1 = (String)runMap.get("status1");
		String status2 = (String)runMap.get("status2");
		String status3 = (String)runMap.get("status3");
		String status4 = (String)runMap.get("status4");
		Integer runDay1 = (Integer)runMap.get("runDay1");
		Integer runDay2 = (Integer)runMap.get("runDay2");
		Integer runDay3 = (Integer)runMap.get("runDay3");
		Integer runDay4 = (Integer)runMap.get("runDay4");
		
		map.put("qczFh", decimalFormat.format(qczFh));
		map.put("jizuFh1", decimalFormat.format((jizuFh12[0]<0)?0.00:jizuFh12[0]));
		map.put("jizuFh2", decimalFormat.format((jizuFh12[1]<0)?0.00:jizuFh12[1]));
		map.put("jizuFh3", decimalFormat.format((jizuFh34[0]<0)?0.00:jizuFh34[0]));
		map.put("jizuFh4", decimalFormat.format((jizuFh34[1]<0)?0.00:jizuFh34[1]));
		map.put("status1", status1);
		map.put("status2", status2);
		map.put("status3", status3);
		map.put("status4", status4);
		map.put("runDay1", runDay1);
		map.put("runDay2", runDay2);
		map.put("runDay3", runDay3);
		map.put("runDay4", runDay4);
		return map;
	}
	
	/**
	 * 根据机组号从SIS中查询实时负荷 (#3,#4)
	 * @param jizuNum
	 * @return
	 */
	public double[] findFhBySis(){
		double dataArray[] = new double[]{0,0};
		
		String sql3 = "select c1 from sisdata.qs_301 t where rownum=1 order by times desc";
		String sql4 = "select c1 from sisdata.qs_401 t where rownum=1 order by times desc";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql3);
			if(resultSet.next()){
				dataArray[0] = resultSet.getDouble("c1");
			}
			resultSet2 = statement.executeQuery(sql4);
			if(resultSet2.next()){
				dataArray[1] = resultSet2.getDouble("c1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(resultSet2 != null){
					resultSet2.close();
				}
				if(statement != null){
					statement.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dataArray;
	}
	
	/**
	 * 根据机组号从SS中查询实时负荷 (#1,#2)
	 * @param jizuNum
	 * @return
	 */
	public double[] findFhBySs(){
		double dataAraay[] = new double[]{0,0};
		
		String sql1 = "select c11 from ssdata.dbo.qs_101 t order by time desc";
		String sql2 = "select c11 from ssdata.dbo.qs_201 t order by time desc";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		
		try {
			String serverName = SystemConfig.getProperty("ED_SERVER12_ADDRESS");
			String dbName = SystemConfig.getProperty("ED_SERVER12_DBNAME");
			String userName = SystemConfig.getProperty("ED_SERVER12_USERNAME");
			String userPwd = SystemConfig.getProperty("ED_SERVER12_USERPWD");
			
			conn = DBHelper.getConn(serverName, dbName, userName, userPwd);
			if(conn == null){
				return dataAraay;
			}
			
			statement = conn.createStatement();
			
			resultSet = statement.executeQuery(sql1);
			if(resultSet.next()){
				dataAraay[0] = resultSet.getDouble("c11");
			}
			resultSet2 = statement.executeQuery(sql2); 
			if(resultSet2.next()){
				dataAraay[1] = resultSet2.getDouble("c11");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(resultSet2 != null){
					resultSet2.close();
				}
				if(statement != null){
					statement.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dataAraay;
	}
	
	/**
	 * 根据机组号从SS中查询实时负荷 (#1,#2)
	 * @param jizuNum
	 * @return
	 */
	public double[] findFhBySs2(){
		double dataAraay[] = new double[]{0,0};
		
		String sql1 = "select c1 from sisdata.ls_101 t order by times desc";
		String sql2 = "select c1 from sisdata.ls_201 t order by times desc";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		
		try {
			conn = this.getConnection();
			if(conn == null){
				return dataAraay;
			}
			
			statement = conn.createStatement();
			
			resultSet = statement.executeQuery(sql1);
			if(resultSet.next()){
				dataAraay[0] = resultSet.getDouble("c1");
			}
			resultSet2 = statement.executeQuery(sql2); 
			if(resultSet2.next()){
				dataAraay[1] = resultSet2.getDouble("c1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(resultSet2 != null){
					resultSet2.close();
				}
				if(statement != null){
					statement.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dataAraay;
	}
	
	/**
	 * 设置机组连续运行、停运天数
	 */
	public void setJzRunDay(){
		//#1、2机组负荷
		double[] jizuFh12 = this.findFhBySs2();
		//#3、4机组负荷
		double[] jizuFh34 = this.findFhBySis();
		
		//#1、#2机组负荷底线 (低于该负荷,则为停运状态)
		double ssJzFh = 320*0.15;
		//#3、#4机组负荷底线 (低于该负荷,则为停运状态)
		double sisJzFh = 640*0.15;
		
		//机组状态
		String status1 = "";
		String status2 = "";
		String status3 = "";
		String status4 = "";
		//机组连续运行、停运天数
		int runDay1 = 0;
		int runDay2 = 0;
		int runDay3 = 0;
		int runDay4 = 0;
		
		Map<String, Object> map = this.findJzRunParams();
		status1 = (String)map.get("status1");
		status2 = (String)map.get("status2");
		status3 = (String)map.get("status3");
		status4 = (String)map.get("status4");
		runDay1 = (Integer)map.get("runDay1");
		runDay2 = (Integer)map.get("runDay2");
		runDay3 = (Integer)map.get("runDay3");
		runDay4 = (Integer)map.get("runDay4");
		
		Calendar calendar = Calendar.getInstance();
		
		//根据#1机组的实时负荷 判断#1机组状态为运行或停运 及其状态持续天数
		if(jizuFh12[0] >= ssJzFh){
			if("连续运行".equals(status1) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay1++;
			}else if("连续停运".equals(status1)){
				status1 = "连续运行";
				runDay1 = 0;
			}
		}else{
			if("连续运行".equals(status1)){
				status1 = "连续停运";
				runDay1 = 0;
			}else if("连续停运".equals(status1) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay1++;
			}
		}
		
		//根据#2机组的实时负荷 判断#1机组状态为运行或停运 及其状态持续天数
		if(jizuFh12[1] >= ssJzFh){
			if("连续运行".equals(status2) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay2++;
			}else if("连续停运".equals(status2)){
				status2 = "连续运行";
				runDay2 = 0;
			}
		}else{
			if("连续运行".equals(status2)){
				status2 = "连续停运";
				runDay2 = 0;
			}else if("连续停运".equals(status2) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay2++;
			}
		}
		
		//根据#3机组的实时负荷 判断#1机组状态为运行或停运 及其状态持续天数
		if(jizuFh34[0] >= sisJzFh){
			if("连续运行".equals(status3) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay3++;
			}else if("连续停运".equals(status3)){
				status3 = "连续运行";
				runDay3 = 0;
			}
		}else{
			if("连续运行".equals(status3)){
				status3 = "连续停运";
				runDay3 = 0;
			}else if("连续停运".equals(status3) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay3++;
			}
		}
		
		//根据#4机组的实时负荷 判断#1机组状态为运行或停运 及其状态持续天数
		if(jizuFh34[1] >= ssJzFh){
			if("连续运行".equals(status4) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay4++;
			}else if("连续停运".equals(status4)){
				status4 = "连续运行";
				runDay4 = 0;
			}
		}else{
			if("连续运行".equals(status4)){
				status4 = "连续停运";
				runDay4 = 0;
			}else if("连续停运".equals(status4) && calendar.get(Calendar.HOUR_OF_DAY) == 0){
				runDay4++;
			}
		}
		
		this.updateJzRunParams(1, status1, runDay1);
		this.updateJzRunParams(2, status2, runDay2);
		this.updateJzRunParams(3, status3, runDay3);
		this.updateJzRunParams(4, status4, runDay4);
	}
	
	/**
	 * 获取机组运行参数
	 * @return
	 */
	public Map<String, Object> findJzRunParams(){
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select * from tb_sys_nowinfo t";
		
		Connection conn = null;
		ResultSet resultSet = null;
		Statement statement = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				String jizu = resultSet.getString("jizu");
				map.put("status" + jizu, resultSet.getString("status"));
				map.put("runDay" + jizu, resultSet.getInt("run_day"));
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
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return map;
	}
	
	/**
	 * 更新机组运行参数
	 * @param jizu		机组号
	 * @param status	机组状态
	 * @param runDay	连续运行、停运天数
	 */
	public void updateJzRunParams(int jizu, String status, int runDay){
		String sql = "update tb_sys_nowinfo set zt_bc = status||run_day, status='" + status + "',run_day=" + runDay + " where jizu='" + jizu + "'";
		
		Connection conn = null;
		Statement statement = null;
		boolean isAutoCommit = true;
		
		try {
			conn = this.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			
			conn.commit();
		} catch (Exception e) {
			try {
				if(conn != null){
					conn.setAutoCommit(isAutoCommit);
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(statement != null){
					statement.close();
				}
				if(conn != null){
					conn.setAutoCommit(isAutoCommit);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存1、2机组负荷
	 */
	public void saveJzFh(String tableName, Map<String, String> map){
		String times = map.get("times");
		String data = map.get("data");
		times = times.substring(0,times.length()-2);
		String sql = "insert into "+tableName+"(times,c1) values(to_date('"+times+"','yyyy-MM-dd HH24:mi:ss'),"+data+")";
		
		Connection conn = null;
		Statement statement = null;
		boolean isAutoCommit = true;
		
		try {
			conn = this.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			
			conn.commit();
		} catch (Exception e) {
			try {
				if(conn != null){
					conn.setAutoCommit(isAutoCommit);
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(statement != null){
					statement.close();
				}
				if(conn != null){
					conn.setAutoCommit(isAutoCommit);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据机组号从SS中查询实时负荷 (#1,#2)
	 * @param tableName
	 * @return
	 */
	public Map<String, String> findFh(Connection conn, String tableName){
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select c11,time from "+tableName+" t order by time desc";
		
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			if(conn == null){
				return map;
			}
			
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				map.put("times", resultSet.getString("time"));
				map.put("data", resultSet.getString("c11"));
			}
		} catch (SQLException e) {
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
		return map;
	}
	
}
