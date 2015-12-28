/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.zhcx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 工程项目查询Manager 
 * @author caihuiwen
 */
public class GcxmcxManager extends JdbcManager{
	
	/**
	 * 获取一般立项、委托项目信息,并加入其执行情况
	 * @param tableName		tb_ht_yblxd,tb_ht_wtd
	 * @param sqlWhere 		查询条件
	 * @return
	 */
	public List<Map<String, String>> findYbOrWtXm(String tableName, String sqlWhere){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		String sql = "select a.*,to_char(a.cjsj,'yyyy-MM-dd HH24:mi:ss') cjsjStr from " + tableName + " a where 1=1 ";
		if(sqlWhere != null && !"".equals(sqlWhere)){
			sql += " and " + sqlWhere;
		}
		sql += " order by cjsj desc";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()){
				String id = resultSet.getString("ID");
				String status = resultSet.getString("STATUS");
				
				if("已立项".equals(status)){
					//根据立项Id 查询其【开工签证】状态
					String kgStatus = getKgqzStatusById(id);
					if(!"".equals(kgStatus)){
						status = kgStatus;
					}
				}else{
					status = "工程立项审批中";
				}
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("xmbh", resultSet.getString("XMBH"));
				map.put("xmmc", resultSet.getString("XMMC"));
				map.put("xmlx", resultSet.getString("XMLX"));
				map.put("sqbm", resultSet.getString("SQBM"));
				map.put("cbfs", resultSet.getString("CBFS"));
				map.put("gcys", resultSet.getString("GCYS"));
				map.put("status", status);
				map.put("cjsj", resultSet.getString("cjsjStr"));
				list.add(map);
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
		
		return list;
	}
	
	/**
	 * 根据立项Id 查询其【开工签证】状态
	 * @param lxid
	 * @return
	 */
	public String getKgqzStatusById(String lxid){
		String status = "";
		
		String sql = "select * from tb_ht_kgqz t where lxid='" + lxid + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			
			if(resultSet.next()){
				String rsStatus = resultSet.getString("STATUS");
				if(rsStatus != null && !"".equals(rsStatus)){
					if("已开工".equals(rsStatus)){
						status = "已开工";
					}else {
						status = "开工签证审批中";
					}
				}
				
				String id = resultSet.getString("ID");
				//根据开工签证Id 查询其【工程验收】状态
				String ysStatus = this.getGcysStatusById(id);
				if(!"".equals(ysStatus)){
					status = ysStatus;
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return status;
	}
	
	/**
	 * 根据开工Id 查询其【工程验收】状态
	 * @param kgid
	 * @return
	 */
	public String getGcysStatusById(String kgid){
		String status = "";
		
		String sql = "select * from tb_ht_ysd t where kgqz_id='" + kgid + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			
			if(resultSet.next()){
				String rsStatus = resultSet.getString("STATUS");
				if(rsStatus != null && !"".equals(rsStatus)){
					if("已验收".equals(rsStatus)){
						status = "已验收";
					}else {
						status = "工程验收审批中";
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return status;
	}
	
}
