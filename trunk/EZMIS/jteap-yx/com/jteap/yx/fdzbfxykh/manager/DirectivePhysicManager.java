/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.fdzbfxykh.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bsh.This;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.system.dataperm.util.sqlformatter2Util;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.yx.fdzbfxykh.model.DirectiveColumnInfo;

/**
 * 小指标物理表操作Manager
 * @author caihuiwen
 */
@SuppressWarnings("unused")
public class DirectivePhysicManager extends JdbcManager{
	
	private Log log = LogFactory.getLog(DirectivePhysicManager.class);
	private DictManager dictManager;
	
	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}
	
	/**
	 * 重建表
	 * @param schema
	 * @param table
	 * @throws Exception 
	 */
	public void rebuildTable(String schema,String tableName,List<DirectiveColumnInfo> ColumnInfoList){
		tableName = StringUtil.upperCase(tableName);
		
		String dropSql = "DROP TABLE " + schema + ".TB_YX_DIRECTIVE_" + tableName + " ";
		StringBuffer createSql = new StringBuffer("CREATE TABLE " + schema + ".TB_YX_DIRECTIVE_" + tableName + "(\r\n");
		StringBuffer pkSb = new StringBuffer("ALTER TABLE " + schema + ".TB_YX_DIRECTIVE_" + tableName +
							" ADD CONSTRAINT PK_ID"+ UUIDGenerator.javaId() + " PRIMARY KEY (ID)");
		
		createSql.append("ID VARCHAR2(32),\r\n");
		//值班时间
		createSql.append("ZBSJ TIMESTAMP(6),\r\n");
		//值班班次
		createSql.append("ZBZB VARCHAR2(50),\r\n");
		for (DirectiveColumnInfo directiveColumnInfo : ColumnInfoList) {
			createSql.append(directiveColumnInfo.getDirectiveCode() + " VARCHAR2(50),\r\n");
		}
		createSql.delete(createSql.length()-3, createSql.length());
		createSql.append("\r\n )");
		
		Connection conn = null;
		boolean isAutoCommit = true;
		Statement statement = null;
		try{
			conn = dataSource.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			
			log.info("删除表:" + dropSql);
			statement.executeUpdate(dropSql);
			
			log.info("创建表:" + createSql.toString());
			statement.executeUpdate(createSql.toString());
			
			log.info(pkSb);
			statement.executeUpdate(pkSb.toString());
			
			conn.commit();
		}catch(Exception ex){
			try {
				if(conn != null){
					conn.rollback();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
	 * 构建小指标数据源(一个月中每天三行记录)
	 * @param nowYm				日期 2010-07
	 * @param tableCode			指标记录物理表 TB_YX_DIRECTIVE_XX
	 * @param columnInfoList	指标字段定义list
	 * @return
	 */
	public List<Map> buildRowData(String nowYmd, String tableCode, List<DirectiveColumnInfo> columnInfoList){
		List<Map> rowsList = new ArrayList<Map>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		tableCode = "TB_YX_DIRECTIVE_" + tableCode;
		String sql = "SELECT * FROM " + schema + "." + tableCode + " WHERE TO_CHAR(ZBSJ,'yyyy-mm')='" + nowYmd + "' ORDER BY ZBSJ DESC";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			//根据日期查询,一天三值,应有3条记录
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			
			//根据该指标的列定义, 查询数据
			while (resultSet.next()) {
				Map<String, String> rowMap = new LinkedHashMap<String, String>();
				rowMap.put("zbsj", dateFormat.format(resultSet.getDate("ZBSJ")));
				rowMap.put("zbzb", resultSet.getString("ZBZB"));
				
				for (DirectiveColumnInfo columnInfo : columnInfoList) {
					String directiveCode = columnInfo.getDirectiveCode();
					String directiveValue = resultSet.getString(directiveCode);
					rowMap.put(directiveCode, directiveValue);
				}
				
				rowsList.add(rowMap);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return rowsList;
	}
	
	/**
	 * 构建小指标汇总数据源(一个月 五条值别级别汇总,一条总计记录)
	 * @param nowYm				日期 2010-07
	 * @param tableCode			指标表定义名称
	 * @param columnInfoList	指标字段定义list
	 * @return
	 */
	public List<Map<String, Object>> buildSumRowData(String nowYm, String tableCode, List<DirectiveColumnInfo> columnInfoList){
		
		List<Map<String, Object>> rowsList = new ArrayList<Map<String,Object>>();
		String[] zb = {"一值","二值","三值","四值","五值"};
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		tableCode = "TB_YX_DIRECTIVE_" + tableCode;
		String sql = "SELECT * FROM " + schema + "." + tableCode + " WHERE TO_CHAR(ZBSJ,'yyyy-mm')='" + nowYm + "'";
		
		//总计map
		Map<String, Object> sumMap = new LinkedHashMap<String, Object>();
		sumMap.put("directiveSum", "总计");
		
		for (DirectiveColumnInfo columnInfo : columnInfoList) {
			sumMap.put(columnInfo.getDirectiveCode(), 0.00);
		}
		
		//循环查询每个值别当月的指标数据
		for (int i = 0; i < zb.length; i++) {
			
			String sqlQuery = sql + " AND ZBZB='" + zb[i] + "'";
			Connection conn = null;
			Statement statement = null;
			ResultSet resultSet = null;
			
			//行map
			Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
			rowMap.put("directiveSum", zb[i]);
			
			try {
				conn = this.getConnection();
				statement = conn.createStatement();
				resultSet = statement.executeQuery(sqlQuery);
				
				//根据该指标的列定义, 查询数据
				while (resultSet.next()) {
					for (DirectiveColumnInfo columnInfo : columnInfoList) {
						String directiveCode = columnInfo.getDirectiveCode();
						String directiveValue = resultSet.getString(directiveCode);
						
						if(directiveValue != null){
							double rsValue = Double.parseDouble(directiveValue);
							
							try {
								if(rowMap.get(directiveCode) != null){
									//值别合计
									double rowMapValue = (Double)rowMap.get(directiveCode);
									rowMap.remove(directiveCode);
									rowMap.put(directiveCode, rsValue + rowMapValue);
								}else{
									//第一条值别记录
									rowMap.put(directiveCode, rsValue);
								}
								
								//总计
								double sumMapValue = (Double)sumMap.get(directiveCode);
								sumMap.remove(directiveCode);
								sumMap.put(directiveCode, sumMapValue + rsValue);
								
							} catch (Exception e) {
								log.info(sql + "\tSQL语句查询结果中含有非数字字符,转换为Double类型时抛出异常...");
							}
						}
					}
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			rowsList.add(rowMap);
			
		}
		rowsList.add(sumMap);
		
		return rowsList;
	}
	
	/**
	 * "directiveName"	"yizhi"		"erzhi"		"sanzhi"	"sizhi"		"wuzhi"		"zongji"
	 * 
	 * 小指标名称			一值			二值			三值			四值			五值			总计
	 * 指标名称1
	 * 指标名称2
	 * 指标名称3
	 * 指标名称4
	 * 指标名称5
	 * 
	 * 构建小指标汇总数据源(一个月汇总记录)
	 * @param nowYm				日期 2010-07
	 * @param tableCode			指标表定义名称
	 * @param columnInfoList	指标字段定义list
	 * @return
	 */
	public List<Map> buildSumRowData2(String nowYm, String tableCode, List<DirectiveColumnInfo> columnInfoList){
		List<Map> rowsList = new ArrayList<Map>();
		
		String[] array_cols = new String[]{"yizhi","erzhi","sanzhi","sizhi","wuzhi"};
		String[] array_zbs = new String[]{"一值","二值","三值","四值","五值"};
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		tableCode = "TB_YX_DIRECTIVE_" + tableCode;
		String sql = "SELECT * FROM " + schema + "." + tableCode + " WHERE TO_CHAR(ZBSJ,'yyyy-mm')='" + nowYm + "' ";
		
		Connection conn = null;
		Statement statement = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			
			for(DirectiveColumnInfo columnInfo : columnInfoList){
				Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
				double sumValue = 0.00;
				
				String columnCode = columnInfo.getDirectiveCode();
				String columnName = columnInfo.getDirectiveName();
				String sumOrAvg = columnInfo.getSumOrAvg();
				if(sumOrAvg != null){
					columnName = sumOrAvg + "：" + columnName;
				}
				/** grid列 小指标名称,一值,二值,三值,四值,五值,总计 "directiveName","yizhi","erzhi","sanzhi","sizhi","wuzhi","zongji" */
				//小指标名称
				rowMap.put("directiveName", columnName);
				rowMap.put("remark", columnInfo.getRemark());
				//一值~五值
				for (int i = 0; i < array_zbs.length; i++) {
					String subSql = sql + "AND ZBZB='" + array_zbs[i] + "'";
					ResultSet resultSet = statement.executeQuery(subSql);
					
					//值别汇总
					double zbSum = 0.00;
					String showValue = "";
					
					int index = 0;
					while(resultSet.next()){
						Double colValue = resultSet.getDouble(columnCode);
						if(colValue!=null){
							zbSum += colValue;
							index++;
						}
					}
					resultSet.close();
					
					if(index == 0){
						index = 1;
					}
					
					if("平均值项".equals(sumOrAvg)){
						//值别平均值
						double avgValue = zbSum/index;
						sumValue += avgValue;
						
						showValue = decimalFormat.format(avgValue);
					}else if("计算项".equals(sumOrAvg)){
						
					} else{
						sumValue += zbSum;
						
						if("运行时间".equals(columnInfo.getDirectiveName())){
							showValue = (int)zbSum + "";
						}else {
							showValue = decimalFormat.format(zbSum);
						}
					}
					
					rowMap.put(array_cols[i], showValue);
				}
				
				String showSumValue = "";
				if("运行时间".equals(columnInfo.getDirectiveName())){
					showSumValue = (int)sumValue + "";
				}else {
					if("平均值项".equals(sumOrAvg)){
						sumValue=sumValue/5;
					}
					showSumValue = decimalFormat.format(sumValue);
				}
				
				//总计
				rowMap.put("zongji", showSumValue);
				
				//得到指标计划值
				String jhz=getJhtjZb(columnCode,nowYm,tableCode.substring(tableCode.length()-1));
				rowMap.put("mubiaozhi", jhz);
				
				//对标结果
				Double dbjg=0d;
				if(StringUtil.isNotEmpty(showSumValue)&&StringUtil.isNotEmpty(jhz)){
					dbjg=Double.parseDouble(showSumValue)-Double.parseDouble(jhz);
				}
				rowMap.put("dbjg", decimalFormat.format(dbjg));	
				
				rowsList.add(rowMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
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
		
		return rowsList;
	}
	
	/**
	 * 
	 * 作者:童贝
	 * 时间:Feb 25, 2011
	 * @param zbcode 指标编码
	 * @param curdate 当前时间
	 * @param jzNum 机组号
	 * @return
	 * @throws SQLException 
	 */
	public String getJhtjZb(String zbcode,String curdate,String jzNum) throws SQLException{
		String res=null;
		String nian=DateUtils.getDate("yyyy");
		String yue=DateUtils.getDate("MM");
		String tableName=null;
		if(StringUtil.isNotEmpty(curdate)){
			nian=DateUtils.getDate(DateUtils.StrToDate(curdate, "yyyy-MM"),"yyyy");
			yue=DateUtils.getDate(DateUtils.StrToDate(curdate, "yyyy-MM"),"MM");
		}
		yue=yue.length()==2?yue.substring(1):yue;
		//1,2号机组
		if("1".equals(jzNum)||"2".equals(jzNum)){
			tableName="data_yq_xzb_data";
		}else if("3".equals(jzNum)||"4".equals(jzNum)){
		//3,4号机组
			tableName="data_eq_xzb_data";
		}
		String sql="select "+zbcode+" from "+tableName+" where nian="+nian+" and yue="+yue;
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				res=resultSet.getString(zbcode.toUpperCase());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(resultSet != null){
				resultSet.close();
			}
			if(statement != null){
				statement.close();
			}
			if(conn != null){
				conn.close();
			}
		}
		return res;
	}
	
	/**
	 * 根据机组号、取数编码、取数值班时间段 从sis表中获取数据
	 * @param jizuNum		机组号
	 * @param columnList	机组指标list
	 * @param qushuYm		取数时间 yyyyMM(用户获取sis动态表)
	 * @param beginTime		取数开始时间数组 yyyy-MM-dd HH:mm:ss 夜/白/中班次开始时间
	 * @param enTime		取数结束时间数组 yyyy-MM-dd HH:mm:ss 夜/白/中班次结束时间
	 * @return				
	 */
	public Map<String, String> findDataBySis(String jizuNum, List<DirectiveColumnInfo> columnList, String qushuYm, String beginTime, String endTime){
		Map<String, String> rowMap = new HashMap<String, String>();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		String sisTableName = "ls_" + jizuNum + "01_" + qushuYm;
		String sql = "select * from sisdata." + sisTableName + 
					" where to_char(times,'yyyy-MM-dd HH24:mi:ss')>='"+beginTime+
					"' and to_char(times,'yyyy-MM-dd HH24:mi:ss')<='"+endTime+"' order by times desc";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				for(DirectiveColumnInfo columnInfo : columnList){
					//根据机组号、取数编码从sis表中获取数据
					if(columnInfo.getDataTable() != null){
						//ezmis表字段名称
						String columnName = columnInfo.getDirectiveCode();
						//取数编码
						String dataTable = columnInfo.getDataTable();
						String[] araayData = dataTable.split(",");
						
						double sisData = 0.00;
						if(araayData.length > 1){
							for (int i = 0; i < araayData.length; i++) {
								sisData += resultSet.getDouble("C"+araayData[i]);
							}
							if("平均值项".equals(columnInfo.getSumOrAvg())){
								sisData = sisData/araayData.length;
							}
							//对特殊的指标项单独处理
							if("计算项".equals(columnInfo.getSumOrAvg())){
								//高加下端差
								if("GJXDC".equals(columnName)){
									Double c62=resultSet.getDouble("C62");
									Double c73=resultSet.getDouble("C73");
									Double c63=resultSet.getDouble("C63");
									Double c74=resultSet.getDouble("C74");
									Double c64=resultSet.getDouble("C64");
									Double c75=resultSet.getDouble("C75");
									Double res=(c62-c73)+(c63-c74)+(c64-c75);
									sisData=res==0d?0d:res/3;
								}else if("NQQDC".equals(columnName)){
								//凝汽器端差
									Double c77=resultSet.getDouble("C77");
									Double c65=resultSet.getDouble("C65");
									sisData=c77-c65;
								}
							}
						}else {
							sisData = resultSet.getDouble("C"+dataTable);
							//对特殊的指标项单独处理
							if("计算项".equals(columnInfo.getSumOrAvg())){
								//高加投运率
								if("GJTYL".equals(columnName)){
									Double c76=resultSet.getDouble("C76");
									sisData=c76/24;
								}else if("TLTYL".equals(columnName)){
								//脱硫投运率
									Double c78=resultSet.getDouble("C78");
									sisData=c78/24;
								}
							}
						}
						rowMap.put(columnName, decimalFormat.format(sisData));
					}
				}
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return rowMap;
	}
	
	/**
	 * 根据机组号、取数编码、取数值班时间段 从SQL Server2000 ssdata数据库表中获取数据
	 * @param jizuNum		机组号
	 * @param columnList	机组指标list
	 * @param qushuYm		取数时间 yyyyMM(用户获取sis动态表)
	 * @param beginTime		取数开始时间数组 yyyy-MM-dd HH:mm:ss 夜/白/中班次开始时间
	 * @param enTime		取数结束时间数组 yyyy-MM-dd HH:mm:ss 夜/白/中班次结束时间
	 * @return				
	 */
	public Map<String, String> findDataBySs(String jizuNum, List<DirectiveColumnInfo> columnList, String qushuYm, String beginTime, String endTime){
		Map<String, String> rowMap = new HashMap<String, String>();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		String ssTableName = "ls_" + jizuNum + "01_" + qushuYm;
		String sql = "select * from ssdata.dbo." + ssTableName + " where time>='"+beginTime+
					"' and time<='"+endTime+"' order by time desc";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			String serverName = SystemConfig.getProperty("ED_SERVER12_ADDRESS");
			String dbName = SystemConfig.getProperty("ED_SERVER12_DBNAME");
			String userName = SystemConfig.getProperty("ED_SERVER12_USERNAME");
			String userPwd = SystemConfig.getProperty("ED_SERVER12_USERPWD");
			
			conn = DBHelper.getConn(serverName, dbName, userName, userPwd);
			if(conn == null){
				return rowMap;
			}
			
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				for(DirectiveColumnInfo columnInfo : columnList){
					//根据机组号、取数编码从sis表中获取数据
					if(columnInfo.getDataTable() != null){
						//ezmis表字段名称
						String columnName = columnInfo.getDirectiveCode();
						//取数编码
						String dataTable = columnInfo.getDataTable();
						String[] araayData = dataTable.split(",");
						
						double sisData = 0.00;
						if(araayData.length > 1){
							for (int i = 0; i < araayData.length; i++) {
								sisData += resultSet.getDouble("C"+araayData[i]);
							}
							if("平均值项".equals(columnInfo.getSumOrAvg())){
								sisData = sisData/araayData.length;
							}
						}else {
							sisData = resultSet.getDouble("C"+dataTable);
						}
						rowMap.put(columnName, decimalFormat.format(sisData));
					}
				}
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return rowMap;
	}
	
	/**
	 * 根据时间ymd,值班值别查询记录Id
	 * @param tableCode 物理表名称
	 * @param ymd		取数时间
	 * @param zbzb		值班值别
	 * @return
	 */
	public String findJzXzb_Id(String tableCode, String ymd, String zbzb){
		tableCode = "TB_YX_DIRECTIVE_" + tableCode;
		String id = "";
		
		String sql = "select id from ezmis."+tableCode+" where to_char(zbsj,'yyyy-MM-dd')='"+ymd+"' and zbzb='"+zbzb+"'";
		Connection conn = null;
		Statement statement = null;
		ResultSet  resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				id = resultSet.getString("id");
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
	
	/**
	 * 保存机组小指标
	 * @param tableCode		物理表名称
	 * @param qushuYmd		取数时间ymd
	 * @param zbzb			值班值别
	 * @param rowMap		key:字段名称  value:sis数据
	 */
	public void saveJzXzb(String tableCode, String qushuYmd, String zbzb, Map<String, String> rowMap){
		tableCode = "TB_YX_DIRECTIVE_" + tableCode;
		String inserSql = "insert into ezmis."+tableCode+"(id,zbsj,zbzb,";
		String id = UUIDGenerator.hibernateUUID();
		
		Iterator<String> itColumn = rowMap.keySet().iterator();
		Iterator<String> itValue = rowMap.keySet().iterator();
		
		while(itColumn.hasNext()){
			String columnName = itColumn.next();
			inserSql += columnName + ",";
		}
		inserSql = inserSql.substring(0, inserSql.length()-1);
		inserSql += ")\r\nvalues('"+id+"',to_date('"+qushuYmd+"','yyyy-MM-dd'),'"+zbzb+"',";
		
		while(itValue.hasNext()){
			String sisData = rowMap.get(itValue.next());
			
			inserSql += "'" + sisData + "',";
		}
		inserSql = inserSql.substring(0, inserSql.length()-1);
		inserSql += ")";
		
		Connection conn = null;
		Statement statement = null;
		boolean isAutoCommit = true;
		
		try {
			conn = this.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			statement = conn.createStatement();
			statement.executeUpdate(inserSql);
			
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn != null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
	 * 修改机组小指标
	 * @param id			记录Id
	 * @param tableCode		物理表名称
	 * @param qushuYmd		取数时间ymd
	 * @param rowMap		key:字段名称  value:sis数据
	 */
	public void updateJzXzb(String id, String tableCode, String qushuYmd, Map<String, String> rowMap){
		tableCode = "TB_YX_DIRECTIVE_" + tableCode;
		String updateSql = "update ezmis."+tableCode+" set ";
		
		for(Map.Entry<String, String> entry : rowMap.entrySet()){
			updateSql += entry.getKey() + "='" + entry.getValue() + "',";
		}
		updateSql = updateSql.substring(0, updateSql.length()-1);
		updateSql += " where id='" + id + "'";
	}
	
	/**
	 * 
	 * 作者:童贝
	 * 时间:Feb 25, 2011
	 * @param jizuNum 机组号
	 * @param zbbc 班次
	 * @param qushuYmd 日期
	 * @param rowMap 结果
	 */
	public void getFdlByJiziNumAndBc(String jizuNum,String zbbc,String qushuYmd,Map<String,String> rowMap){
		String tableName="";
		String field="";
		if("1".equals(jizuNum)){
			tableName="TB_YX_FORM_FDTJ300";
			field="FDJDL_1";
		}else if("2".equals(jizuNum)){
			tableName="TB_YX_FORM_FDTJ300";
			field="FDJDL_2";
		}else if("3".equals(jizuNum)){
			tableName="TB_YX_FORM_FDTJ600";
			field="FDJDL_3";
		}else if("4".equals(jizuNum)){
			tableName="TB_YX_FORM_FDTJ600";
			field="FDJDL_4";
		}
		String sql="select "+field+" from "+tableName+" where to_char(zbsj,'yyyy-MM-dd')='"+qushuYmd+"' and zbbc='"+zbbc+"'";
		Connection conn = null;
		Statement statement = null;
		ResultSet  resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				String value = resultSet.getString(field);
				rowMap.put("FDL", value);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
