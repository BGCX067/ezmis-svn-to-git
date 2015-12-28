/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.htzx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 合同付款审批Manager 
 * @author caihuiwen
 */
public class HeTongFkShenPiManager extends JdbcManager{
	
	/**
	 * 根据合同ID获取合同信息
	 * @param tableName	合同物理表名称
	 * @param htid		合同Id
	 * @return
	 */
	public Map<String, String> findCurrentHtbh(String tableName, String id){
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select * from " + tableName + " t where id='" + id + "'";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				map.put("htbh", resultSet.getString("htbh"));
				map.put("htlx", resultSet.getString("htlx"));
				if(!"tb_ht_rlht".equals(tableName.toLowerCase())){
					map.put("htje", resultSet.getString("htje"));
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
		
		return map;
	}
	
}
