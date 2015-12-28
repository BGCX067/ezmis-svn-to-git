/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.gcxmgl.manager;

import java.sql.*;
import java.util.concurrent.locks.ReentrantLock;

import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 工程项目结算支付单审批Manager 
 * @author wangyi
 */
public class JszfManager extends JdbcManager{
	
	private ReentrantLock lock = new ReentrantLock();
	/**
	 * 获取当前的项目编号
	 * @param tableName	合同物理表名称
	 * @return
	 */
	public  int findCurrentXmbh(String tableName){
		int xmbh = 1000;
		String sql = "select max(xmbh) as xmbh from " + tableName  +" WHERE XMBH LIKE '%ZF%'";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			lock.lock();
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			//System.out.println(resultSet);
			if(resultSet.next()){
				if(resultSet.getString("xmbh")!=null){
					int bh = Integer.parseInt(resultSet.getString("xmbh").substring(4,8));
					if(bh >= 1001){
						xmbh = bh;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
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
		
		return xmbh + 1;
	}
}
