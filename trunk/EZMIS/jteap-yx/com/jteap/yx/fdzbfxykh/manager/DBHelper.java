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
import java.text.ParseException;

import com.jteap.core.utils.StringUtil;

/**
 * SQL Server数据库连接类
 * @author caihuiwen
 */
public class DBHelper {
	
/**	String driverName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	String dbUrl = "jdbc:microsoft:sqlserver:"+serverName+"; DatabaseName="+dataBaseName; */
	
	/**
	 * JTDS 获取SQL Server2000连接对象
	 * @param serverName		服务器名:端口号  如 10.229.41.10:1433
	 * @param dataBaseName		数据库名称	  如 ssdata
	 * @param userName			用户名		  如 sa
	 * @param userPwd			密码			  如 sa
	 * @return
	 */
	public static Connection getConn(String serverName, String dataBaseName, String userName, String userPwd){
		if("null".equals(serverName) || StringUtil.isEmpty(serverName)){
			return null;
		}
		
		String driverName = "net.sourceforge.jtds.jdbc.Driver";
		String dbUrl = "jdbc:jtds:sqlserver://"+serverName+"/"+dataBaseName;
		Connection conn = null;
		
		try {
			Class.forName(driverName).newInstance();
			conn = DriverManager.getConnection(dbUrl,userName,userPwd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static void main(String[] args) throws ParseException {
		String sql = "select * from ssdata.dbo.ls_101_20109 where time>='2010-09-28 00:00:00' and time<='2010-09-28 07:59:59' order by time desc";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		for (int i = 0; i < 10; i++) {
			try {
				conn = DBHelper.getConn("10.229.41.10:1433", "ssdata", "sa", "sa");
				statement = conn.createStatement();
				resultSet = statement.executeQuery(sql);
				
				if(resultSet.next()){
					System.out.println(i + "  " + resultSet.getDouble("c1"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}
}
