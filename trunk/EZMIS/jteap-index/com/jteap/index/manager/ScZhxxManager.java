/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jteap.core.utils.DateUtils;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 生产综合信息Manager
 * @author caihuiwen
 */
public class ScZhxxManager extends JdbcManager{
	
	/**
	 * 获取生产综合信息
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Double> findScZhxxMap() throws SQLException{
		Map<String, Double> map = null;
		
		String curDate = DateUtils.getDate("yyyy-MM-dd");
		Date curD = DateUtils.StrToDate(curDate, "yyyy-MM-dd");
		String curNian = DateUtils.getDate(curD, "yyyy");
		String curYue = DateUtils.getDate(curD, "MM");
		
		String sql = "select * from (select * from (select * from data_qc_ri t order by nian desc) order by yue desc) order by ri desc";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				map = new HashMap<String, Double>();
				map.put("scer", resultSet.getDouble("SCER"));
				map.put("se", resultSet.getDouble("SE"));
				map.put("cc", resultSet.getDouble("CC"));
				map.put("scr", resultSet.getDouble("SCR"));
			}
			
			//月度综合厂用电率
			sql = "select SUM(SCE)/SUM(GE)*100 YCYDL from data_qc_ri t where nian="+curNian+" and yue="+curYue;
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				map.put("ycydl", resultSet.getDouble("YCYDL"));
			}
			
			//月度累计上网电量
			sql = "select SUM(SE) YSWDL from data_qc_ri t where nian="+curNian+" and yue="+curYue;
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				map.put("yswdl", resultSet.getDouble("YSWDL"));
			}
			
			//月度累计耗煤量
			sql = "select SUM(CC) YHML from data_qc_ri t where nian="+curNian+" and yue="+curYue;
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				map.put("yhml", resultSet.getDouble("YHML"));
			}
			
			//月度供电标煤耗
			sql = "select SUM(SC)/SUM(SE)*100 YGDBMH from data_qc_ri t where nian="+curNian+" and yue="+curYue;
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				map.put("ygdbmh", resultSet.getDouble("YGDBMH"));
			}
			
		} catch (Exception e) {
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
		return map;
	}
	
}
