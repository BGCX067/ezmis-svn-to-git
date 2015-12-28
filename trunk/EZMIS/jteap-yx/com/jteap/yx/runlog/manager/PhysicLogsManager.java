/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.yx.fdzbfxykh.manager.DBHelper;
import com.jteap.yx.runlog.model.LogsColumnInfo;
import com.jteap.yx.runlog.model.LogsTableInfo;

/**
 * 运行日志物理表操作Manager
 * @author caihuiwen
 */
public class PhysicLogsManager extends JdbcManager{
	
	private org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PhysicLogsManager.class);
	
	private DictManager dictManager;
	private LogsTableInfoManager logsTableInfoManager;
	private LogsColumnInfoManager logsColumnInfoManager;
	
	public void setLogsColumnInfoManager(LogsColumnInfoManager logsColumnInfoManager) {
		this.logsColumnInfoManager = logsColumnInfoManager;
	}

	public void setLogsTableInfoManager(LogsTableInfoManager logsTableInfoManager) {
		this.logsTableInfoManager = logsTableInfoManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	/**
	 * 重建日志记录物理表
	 * @param dropSql
	 * @param createSql
	 */
	public void rebuildPhysicLogTable(String dropSql, String createSql, String alterSql){
			
			Connection conn = null;
			Statement statement = null;
			boolean isAutoCommit = true;
			try{
				conn = dataSource.getConnection();
				isAutoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);
				
				statement = conn.createStatement();
				if(dropSql != null){
					try{
						log.info("删除表: " + dropSql);
						statement.executeUpdate(dropSql);
					}catch(Exception ex){}
				}
				if(createSql != null){
					log.info("创建表: " + createSql);
					statement.executeUpdate(createSql);
				}
				if(alterSql != null){
					log.info("修改表: " + alterSql);
					statement.executeUpdate(alterSql);
				}
				conn.commit();
			}catch(Exception e){
				try {
					if(conn != null){
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
	 * 根据班次、采样点  获取 当前班次的采样List、当前班次开始、结束时间
	 * @param caiyangdian
	 * @param bc
	 * @return
	 */
	public Map<String, Object> findCaiYangMap(String caiyangdian, String zbbc){
		Map<String, Object> map = new HashMap<String, Object>();
		
		String[] caiyangArray = caiyangdian.split(",");
		//当前班次开始小时 如 11
		int beginHour = 0;
		int endHour = 0;
		//当前班次开始时间 如 11:27:38
		String beginTime = "";
		String endTime = "";
		
		//取得排班时间数据字典来判断
		if(zbbc.equals("夜班")){
			//夜班开始时间
			Dict dictYeB = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "ye_beginTime");
			beginHour = Integer.parseInt(dictYeB.getValue().substring(0, 2));
			beginTime += dictYeB.getValue();
			//夜班结束时间
			Dict dictYeE = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "ye_endTime");
			endHour = Integer.parseInt(dictYeE.getValue().substring(0, 2));
			endTime += dictYeE.getValue();
		}else if(zbbc.equals("白班")){
			//白班开始时间
			Dict dictBaiB = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "bai_beginTime");
			beginHour = Integer.parseInt(dictBaiB.getValue().substring(0, 2));
			beginTime += dictBaiB.getValue();
			//白班结束时间
			Dict dictBaiE = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "bai_endTime");
			endHour = Integer.parseInt(dictBaiE.getValue().substring(0, 2));
			endTime += dictBaiE.getValue();
		}else if(zbbc.equals("中班")){
			//中班开始时间
			Dict dictZhongB = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "zhong_beginTime");
			beginHour = Integer.parseInt(dictZhongB.getValue().substring(0, 2));
			beginTime += dictZhongB.getValue();
			//中班结束时间
			Dict dictZhongE = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "zhong_endTime");
			endHour = Integer.parseInt(dictZhongE.getValue().substring(0, 2));
			endTime += dictZhongE.getValue();
		} 
		
		List<String> caiyangList = new ArrayList<String>(); 
		for (int i = beginHour; i <= endHour; i++) {
			for (int j = 0; j < caiyangArray.length; j++) {
				if (caiyangArray[j].equals(i + "")) {
					caiyangList.add(caiyangArray[j]);
					break;
				}
			}
		}
		map.put("caiyangList", caiyangList);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		
		return map;
	}
	
	/**
	 * 初始化全部数据			查找该日志存放数据的物理表("TB_YX_LOG_" + tableCode),如无数据给空值.		
	 * @param tableId	表Id
	 * @param columnId	字段Id
	 * @param zbsj		值班时间
	 * @param zbbc		值班班次
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findIntiData(String tableId, String zbsj, String zbbc){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		LogsTableInfo tableInfo = logsTableInfoManager.get(tableId);
		String schema = SystemConfig.getProperty("jdbc.schema");
		String beginTime = zbsj;
		String endTime = zbsj;
		
		Map<String, Object> caiMap = this.findCaiYangMap(tableInfo.getCaiyangdian(),zbbc);
		beginTime += " " + caiMap.get("beginTime");
		endTime += " " + caiMap.get("endTime");
		List<String> caiyangList = (List<String>) caiMap.get("caiyangList");
		
		List<LogsColumnInfo> columnInfoList = logsColumnInfoManager.findBy("tableId",tableId);
		//查询所有行
		for(LogsColumnInfo columnInfo : columnInfoList){
				
				//给日志列表空值, 用于查询不到时初始化单元格TD
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("id", "");
				for (int i = 0; i < caiyangList.size(); i++) {
					String value = caiyangList.get(i);
					map.put("value" + value, "");
				}
				map.put("sumBan", "");
				map.put("avgBan", "");
				map.put("sumDay", "");
				map.put("avgDay", "");
				map.put("sumMonth", "");
				map.put("avgMonth", "");
				
				String sql = "SELECT * FROM " + schema + ".TB_YX_LOG_" + tableInfo.getTableCode() + " WHERE ROW_ID='" + columnInfo.getId() + "'" + 
				" AND TO_CHAR(ZBSJ,'yyyy-MM-dd HH24:mi:ss')>='" + beginTime + "' AND TO_CHAR(ZBSJ,'yyyy-MM-dd HH24:mi:ss')<='" + endTime + 
				"' "+"ORDER BY FILL_DATE DESC";
				
				Connection conn = null;
				Statement st = null;
				ResultSet resultSet = null;
				try {
					conn = dataSource.getConnection();
					st = conn.createStatement();
					resultSet = st.executeQuery(sql);
					if(resultSet.next()){
						//查询到结果时, 初始化Map
						map = new LinkedHashMap<String, Object>();
						map.put("id", resultSet.getString("ID"));
//							map.put("rowId", resultSet.getString("ROW_ID"));
//							map.put("fillDate", resultSet.getString("FILL_DATE"));
						
						for (int i = 0; i < caiyangList.size(); i++) {
							String value = caiyangList.get(i);
							map.put("value" + value, resultSet.getString("VALUE_" + value)==null?"":resultSet.getString("VALUE_" + value));
						}
						if(zbbc.equals("夜班")){
							map.put("sumBan", resultSet.getString("SUM_BAN_0")==null?"":resultSet.getString("SUM_BAN_0"));
							map.put("avgBan", resultSet.getString("AVG_BAN_0")==null?"":resultSet.getString("AVG_BAN_0"));
						}else if(zbbc.equals("白班")){
							map.put("sumBan", resultSet.getString("SUM_BAN_1")==null?"":resultSet.getString("SUM_BAN_1"));
							map.put("avgBan", resultSet.getString("AVG_BAN_1")==null?"":resultSet.getString("AVG_BAN_1"));
						}else if(zbbc.equals("中班")){
							map.put("sumBan", resultSet.getString("SUM_BAN_2")==null?"":resultSet.getString("SUM_BAN_2"));
							map.put("avgBan", resultSet.getString("AVG_BAN_2")==null?"":resultSet.getString("AVG_BAN_2"));
						}
						map.put("sumDay", resultSet.getString("SUM_DAY")==null?"":resultSet.getString("SUM_DAY"));
						map.put("avgDay", resultSet.getString("AVG_DAY")==null?"":resultSet.getString("AVG_DAY"));
						map.put("sumMonth", resultSet.getString("SUM_MONTH")==null?"":resultSet.getString("SUM_MONTH"));
						map.put("avgMonth", resultSet.getString("AVG_MONTH")==null?"":resultSet.getString("AVG_MONTH"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if(resultSet != null){
							resultSet.close();
						}
						if(st != null){
							st.close();
						}
						if(conn != null){
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				map.put("columnId", columnInfo.getId());
				map.put("edingzhi", columnInfo.getEdingzhi()==null?"":columnInfo.getEdingzhi());
				list.add(map);
		}
		
		return list;
	}
	
	/**
	 * 取数 				获取一行数据放入Map中
	 * @param tableId 	表Id
	 * @param columnId	字段Id
	 * @param zbsj		值班时间
	 * @param zbbc		值班班次
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getData(String tableId, String columnId, String zbsj, String zbbc){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		return map;
	}
	
	/**
	 * 获取当天记录累计值, 并更新到该条记录中去
	 * @param tableInfo		日志配置表
	 * @param columnId		字段Id
	 * @param zbsj			日期
	 * @return
	 */
	public void updateCountDay(LogsTableInfo tableInfo, String columnId, String zbsj){
		double sumDay = 0;
		double avgDay = 0;
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); 
		
		String id = "";
		String[] caiyangArray = tableInfo.getCaiyangdian().split(",");
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT * FROM " + schema + ".TB_YX_LOG_" + tableInfo.getTableCode() + " WHERE ROW_ID='" + columnId + "'" + 
					" AND TO_CHAR(ZBSJ,'yyyy-mm-dd')='" + zbsj + "'" +
					" ORDER BY FILL_DATE DESC";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean isAutoCommit = true;
		try {
			conn = dataSource.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			
			if(resultSet.next()){
				id = resultSet.getString("ID");
				
				for (int i = 0; i < caiyangArray.length; i++) {
					String tdValue = resultSet.getString("VALUE_" + caiyangArray[i]);
					if(tdValue != null){
						try {
							//当天累计值
							sumDay += Double.parseDouble(tdValue);
						} catch (Exception e) {
							sumDay = 0;
							e.printStackTrace();
						}
					}
				}
			}
			
			//当天平均值
			avgDay = sumDay/caiyangArray.length;
			
			if(id != null && id != ""){
				String sql1 = "UPDATE TB_YX_LOG_" + tableInfo.getTableCode() + 
							" SET SUM_DAY='" + decimalFormat.format(sumDay) + "',AVG_DAY='" + decimalFormat.format(avgDay) + "'" +
							" WHERE ID='" + id + "'";
				statement.executeUpdate(sql1);
			}
			
			conn.commit();
		} catch (SQLException e) {
			try {
				if(conn != null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
					conn.setAutoCommit(isAutoCommit);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 获取当月记录累计值, 并更新到最后一条记录中去
	 * @param tableInfo		日志配置表
	 * @param columnId		字段Id
	 * @param zbsj			日期
	 * @return
	 */
	public void updateCountMonth(LogsTableInfo tableInfo, String columnId, String zbsj){
		double sumMonth = 0;
		double avgMonth = 0;
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); 
		
		String id = "";
		zbsj = zbsj.substring(0,zbsj.lastIndexOf("-"));
		String[] caiyangArray = tableInfo.getCaiyangdian().split(",");
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT * FROM " + schema + ".TB_YX_LOG_" + tableInfo.getTableCode() + " WHERE ROW_ID='" + columnId + "'" + 
					" AND TO_CHAR(ZBSJ,'yyyy-mm')='" + zbsj + "'" +
					" ORDER BY FILL_DATE DESC";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean isAutoCommit = true;
		try {
			conn = dataSource.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			
			int index = 0;
			while(resultSet.next()){
				if(index == 0){
					id = resultSet.getString("ID");
				}
				
				for (int i = 0; i < caiyangArray.length; i++) {
					String tdValue = resultSet.getString("VALUE_" + caiyangArray[i]);
					if(tdValue != null){
						try {
							//当月累计值
							sumMonth += Double.parseDouble(tdValue);
						} catch (Exception e) {
							sumMonth = 0;
							e.printStackTrace();
						}
					}
				}
				index++;
			}
			
			//当月平均值
			avgMonth = sumMonth/caiyangArray.length;
			
			if(id != null && id != ""){
				String sql1 = "UPDATE TB_YX_LOG_" + tableInfo.getTableCode() + 
							" SET SUM_MONTH='" + decimalFormat.format(sumMonth) + "',AVG_MONTH='" + decimalFormat.format(avgMonth) + "'" +
							" WHERE ID='" + id + "'";
				statement.executeUpdate(sql1);
			}
			
			conn.commit();
		} catch (SQLException e) {
			try {
				if(conn != null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
					conn.setAutoCommit(isAutoCommit);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 保存单行日志信息
	 * @return
	 */
	public void saveLogs(String tableCode, Map<String, Object> map){
		String tableName = "TB_YX_LOG_" + tableCode;
		StringBuffer sql = new StringBuffer("INSERT INTO " + tableName + "(");
		
		for(Map.Entry<String, Object> entry : map.entrySet()){
			sql.append(entry.getKey() + ",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")\r\n");
		
		sql.append("VALUES(");
		for(Map.Entry<String, Object> entry : map.entrySet()){
			if(entry.getKey().equals("FILL_DATE")){
				sql.append("to_date('" + entry.getValue() + "','yyyy-MM-dd HH24:mi:ss'),");
			}else if(entry.getKey().equals("ZBSJ")){
				sql.append("to_date('" + entry.getValue() + "','yyyy-MM-dd HH24:mi:ss'),");
			}else {
				sql.append("'" + entry.getValue() + "',");
			}
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		
		Connection conn = null;
		boolean isAutoCommit = false;
		Statement st = null;
		try {
			conn = dataSource.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.executeUpdate(sql.toString());
			
			conn.commit();
		} catch (Exception e) {
			try {
				if(conn != null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(st != null){
					st.close();
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
	 * 修改单行日志信息
	 * @return
	 */
	public void updateLogs(String tableCode, Map<String, Object> map){
		String id = "";
		String tableName = "TB_YX_LOG_" + tableCode;
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + "\r\nSET ");
		
		for(Map.Entry<String, Object> entry : map.entrySet()){
			if(entry.getKey().equals("ID")){
				id = (String) entry.getValue();
			}else if(entry.getKey().equals("FILL_DATE")){
				sql.append(entry.getKey() + "=to_date('" + entry.getValue() + "','yyyy-MM-dd HH24:mi:ss'),");
			}else if(entry.getKey().equals("ZBSJ")){
				sql.append(entry.getKey() + "=to_date('" + entry.getValue() + "','yyyy-MM-dd HH24:mi:ss'),");
			}else if(entry.getKey().equals("ROW_ID")){
				
			}else{
				sql.append(entry.getKey() + "='" + entry.getValue() + "',");
			}
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append("\r\n");
		sql.append("WHERE ID='" + id + "'");
		
		Connection conn = null;
		Statement st = null; 
		boolean isAutoCommit = true;
		try {
			conn = dataSource.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.executeUpdate(sql.toString());
			
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(st != null){
					st.close();
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
	 * 根据指标Id,值班时间 获取该条记录Id
	 * @param tableCode
	 * @param rowId
	 * @param zbsj
	 * @return
	 */
	public String findByRowId(String tableCode, String rowId, String zbsj){
		String id = "";
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT ID FROM " + schema + ".TB_YX_LOG_" + tableCode + " WHERE ROW_ID='" + rowId + "'" + 
						" AND TO_CHAR(ZBSJ,'yyyy-MM-dd')='" + zbsj + "' ORDER BY FILL_DATE DESC";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				id = resultSet.getString("ID");
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
		
		return id;
	}
	
	/**
	 * 运行日志取数 (查询数据、保存数据)
	 * @param tableId		日志配置表
	 * @param zbsj			值班时间
	 * @param zbbc			值班班次
	 * @param curPersonId	当前登录人Id
	 * @return logState 	日志配置状态 1正常、2日志配置表定义被删、3该日志没有配置指标、4日志指标配置不全
	 */
	@SuppressWarnings("unchecked")
	public int logsQs(String tableId, String zbsj, String zbbc, String curPersonId){
		//日志配置状态
		int logState = 1;
		
		//获取采样点信息
		LogsTableInfo tableInfo = logsTableInfoManager.get(tableId);
		if(tableInfo == null){
			logState = 2;
			return logState;
		}
		Map<String, Object> caiMap = this.findCaiYangMap(tableInfo.getCaiyangdian(),zbbc);
		List<String> caiyangList = (List<String>) caiMap.get("caiyangList");
		
		//获取指标定义信息
		List<LogsColumnInfo> columnInfoList = logsColumnInfoManager.findBy("tableId",tableId);
		if(columnInfoList.size() < 1){
			logState = 3;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date nowDate = new Date();
		String nowYmdHs = dateFormat2.format(nowDate);
		String nowYm = dateFormat.format(nowDate);
		String nowYm2 = "";
		
		//处理ss动态取数表的日期后缀
		String qushuY = nowYm.substring(0,4);
		String qushuM = nowYm.substring(4,6);
		int intM = Integer.parseInt(qushuM);
		nowYm2 = qushuY + intM;
		
		//查询所有行
		for(LogsColumnInfo columnInfo : columnInfoList){
			String jizuNum = columnInfo.getJizu();
			String tableCode = columnInfo.getDataTableCode();
			String columnName = columnInfo.getSisCedianbianma();
			
			Map<String, Object> rowMap = new HashMap<String, Object>();
			if(jizuNum != null && tableCode != null && columnName != null){
				columnName = "c" + columnName;
				
				String id = this.findByRowId(tableInfo.getTableCode(), columnInfo.getId(), zbsj);
				if(id != null && !id.trim().equals("")){
					rowMap.put("ID", id);
				}else {
					rowMap.put("ID", UUIDGenerator.hibernateUUID());
				}
				
				rowMap.put("ROW_ID", columnInfo.getId());
				rowMap.put("PERSON_ID", curPersonId);
				rowMap.put("FILL_DATE", nowYmdHs);
				rowMap.put("ZBSJ", zbsj);
				
				//当前班汇总、平均
				double sumBan = 0;
				double avgBan = 0;
				
				/** 根据采样点查询指标数据 */
				for (int i = 0; i < caiyangList.size(); i++) {
					String qushuTime = zbsj;
					int caiyangdian = Integer.parseInt(caiyangList.get(i));
					if(caiyangdian < 10){
						//2010-10-01 00
						qushuTime += " 0" + caiyangdian;
					}else {
						qushuTime += " " + caiyangdian;
					}
					
					double data = 0.00;
					if("1#".equals(jizuNum) || "2#".equals(jizuNum)){
						String beginTime = qushuTime + ":00:00";
						String endTime = qushuTime + ":59:59";
						String tableName = "ls_" + tableCode + "_" + nowYm2;
						
						data = this.findLogsBySs(beginTime, endTime, tableName, columnName);
					}else if("3#".equals(jizuNum) || "4#".equals(jizuNum)){
						String tableName = "ls_" + tableCode + "_" + nowYm;
						
						data = this.findLogsBySis(qushuTime, tableName, columnName);
					}
					
					sumBan += data;
					rowMap.put("VALUE_" + caiyangdian, decimalFormat.format(data));
				}
				
				if(zbbc.equals("夜班")){
					rowMap.put("SUM_BAN_0", decimalFormat.format(sumBan));
					rowMap.put("AVG_BAN_0", decimalFormat.format(avgBan));
				}else if(zbbc.equals("白班")){
					rowMap.put("SUM_BAN_1",decimalFormat.format(sumBan));
					rowMap.put("AVG_BAN_1", decimalFormat.format(avgBan));
				}else if(zbbc.equals("中班")){
					rowMap.put("SUM_BAN_2", decimalFormat.format(sumBan));
					rowMap.put("AVG_BAN_2", decimalFormat.format(avgBan));
				}
				
				try {
					if(id != null && !id.trim().equals("")){
						//修改
						this.updateLogs(tableInfo.getTableCode(), rowMap);
					}else {
						//保存
						this.saveLogs(tableInfo.getTableCode(),rowMap);
					}
					
					//获取当天记录累计值, 并更新到该条记录中去
					this.updateCountDay(tableInfo, columnInfo.getId(), zbsj);
					
					//获取当月记录累计值, 并更新到最后一条记录中去
					this.updateCountMonth(tableInfo, columnInfo.getId(), zbsj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				logState = 4;
			}
		}
		
		return logState;
	}
	
	/**
	 * SIS取数
	 * @param ymdH				日志取数时间
	 * @param tableName			表名称
	 * @param columnName		字段名称
	 * @return
	 */
	public double findLogsBySis(String ymdH, String tableName, String columnName){
		double data = 0.00;
		String sql = "select " + columnName + " from sisdata." + tableName 
					+ " where to_char(times,'yyyy-MM-dd HH24')='" + ymdH + "'order by times desc";
		
		Connection conn = null;
		Statement st = null;
		ResultSet resultSet = null;
		
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			resultSet = st.executeQuery(sql);
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
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
	
	/**
	 * SS取数
	 * @param ymdH				日志取数时间
	 * @param tableName			表名称
	 * @param columnName		字段名称
	 * @return
	 */
	public double findLogsBySs(String beginTime, String endTime, String tableName, String columnName){
		double data = 0.00;
		String sql = "select " + columnName + " from ssdata.dbo." + tableName 
					+ " where time>='" + beginTime + "' and time<='" + endTime + "' order by time desc";
		
		Connection conn = null;
		Statement st = null;
		ResultSet resultSet = null;
		
		try {
			String serverName = SystemConfig.getProperty("ED_SERVER12_ADDRESS");
			String dbName = SystemConfig.getProperty("ED_SERVER12_DBNAME");
			String userName = SystemConfig.getProperty("ED_SERVER12_USERNAME");
			String userPwd = SystemConfig.getProperty("ED_SERVER12_USERPWD");
			
			conn = DBHelper.getConn(serverName, dbName, userName, userPwd);
			if(conn == null){
				return data;
			}
			
			st = conn.createStatement();
			resultSet = st.executeQuery(sql);
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
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
}
