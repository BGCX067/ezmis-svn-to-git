/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.gcxmgl.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 工程立项审批Manager 
 * @author caihuiwen
 */
public class GclxManager extends JdbcManager{

	private ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 获取当前的项目序号
	 * @param tableName	合同物理表名称
	 * @param zy 		专业
	 * @return
	 */
	public  int findCurrentXmxh(String tableName, String zy){
		int htxh = 100;
		String sql = "select max(xmxh) xmxh from " + tableName +" where zy='" + zy + "'";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			lock.lock();
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				if(resultSet.getInt("xmxh") >= 101){
					htxh = resultSet.getInt("xmxh");
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
		
		return htxh + 1;
	}
	
}
