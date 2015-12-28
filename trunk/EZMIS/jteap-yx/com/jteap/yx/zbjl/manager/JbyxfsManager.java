/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.zbjl.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 交班运行方式Manager
 * @author caihuiwen
 */
@SuppressWarnings({"unchecked","deprecation"})
public class JbyxfsManager extends JdbcManager{
	
	/**
	 * 根据 表单对应物理表名、值班时间、值班班次、机长号 获取所有交班运行方式
	 * @param forms      表单对应物理表名、岗位类别
	 * @param zbsj        值班时间
	 * @param zbbc        值班班次
	 * @return
	 */
	public List<Map<String, Object>> findByFormSn(String forms[],String zbsj, String zbbc){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String jizhangtype = "";
			
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT ID,to_char(ZBSJ,'yyyy-MM-dd') ZBSJ,ZBBC,ZBZB,TIANXIEREN,to_char(TIANXIESHIJIAN,'yyyy-MM-dd HH24:mi:ss') TIANXIESHIJIAN FROM " 
					+ schema + "." + forms[0] + " WHERE 1=1";
		if(StringUtil.isNotEmpty(zbsj)){
			sql += " AND TO_CHAR(ZBSJ,'YYYY-MM-DD')='" + zbsj + "'";
		}
		if(StringUtil.isNotEmpty(zbbc)){
			sql += " AND ZBBC='" + zbbc + "'";
		}
		if(forms[1].indexOf("机长") != -1){
			jizhangtype = forms[1].substring(1,2);
			sql += " AND JIZHANGHAO='" + jizhangtype + "'";
		}
		sql += " ORDER BY TIANXIESHIJIAN DESC";
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", resultSet.getString("ID"));
				map.put("zbsj", resultSet.getString("ZBSJ"));
				map.put("zbbc", resultSet.getString("ZBBC"));
				map.put("zbzb", resultSet.getString("ZBZB"));
				map.put("tianxieren", resultSet.getString("TIANXIEREN"));
				map.put("tianxieshijian", resultSet.getString("TIANXIESHIJIAN"));
				map.put("gwlb", forms[1]);
				map.put("formSn", forms[0]);
				map.put("jizhangtype", jizhangtype);
				
				list.add(map);
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
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 根据 值班时间、值班班次、机长号(可为空) 查询最后一条交班运行方式
	 * @param formSn 交班运行方式表单对应的 物理表
	 * @param zbsj 值班时间
	 * @param zbbc 值班班次
	 * @param jiZhangHao 机长号(可为空)
	 * @return 该条交班运行方式的ID
	 */
	public String findJbyxfs(String formSn, String zbsj, String zbbc, String jiZhangHao){
		String id = null;
		String schema = SystemConfig.getProperty("jdbc.schema");
		
		if(zbsj == null || "".equals(zbsj)){
			SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
			zbsj = dataFormat.format(new Date());
		}
		
		String sql = "SELECT ID FROM (SELECT ID FROM " + schema + "." + formSn + " WHERE 1=1";
		if(StringUtil.isNotEmpty(zbsj)){
			sql += " AND TO_CHAR(ZBSJ,'YYYY-MM-DD')='" + zbsj + "'";
		}
		if(StringUtil.isNotEmpty(zbbc)){
			sql += " AND ZBBC='" + zbbc + "'";
		}
		if(null != jiZhangHao && jiZhangHao.length() > 0 && !jiZhangHao.equals("null")){
			sql += " AND JIZHANGHAO='" + jiZhangHao + "'";
		}
		sql += " ORDER BY TIANXIESHIJIAN DESC) WHERE ROWNUM=1";
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getString("id");
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
	 * 根据 表单对应物理表名、值班时间、值班班次、机长号 获取最后一条交班运行方式
	 * @param formSn      表单对应物理表名
	 * @param jiZhangHao  机长号
	 * @return			  该条记录的Id	
	 */
	public String findLastByFormSn(String formSn, String jiZhangHao){
		String id = "";
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT ID FROM " + schema + "." + formSn;
		
		if(null != jiZhangHao && jiZhangHao.length() > 0 && !jiZhangHao.equals("null")){
			sql += " AND JIZHANGHAO='" + jiZhangHao + "'";
		}
		
		sql += " ORDER BY TIANXIESHIJIAN DESC";
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			
			if(resultSet.next()){
				id = resultSet.getString("ID");
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
	 * 根据表单对应物理表名、开始时间、结束时间 获取化学监督
	 * @param formSn      表单对应物理表名
	 * @param beginYmd    开始时间 
	 * @param endYmd      结束时间
	 * @param tianxieren  填写人
	 * @param sylx		  审阅类型
	 * @return
	 */
	public List<Map<String, Object>> findHxjd(String formSn,String beginYmd, String endYmd, String tianxieren, String sylx){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		boolean hasQyrq = false;
		if("TB_YX_FORM_JCRYYZFXBB".equals(formSn) || "TB_YX_FORM_BYQYZFXBB".equals(formSn) || "TB_YX_FORM_FHLZKRWRCFXBB".equals(formSn)
			|| "TB_YX_FORM_QILUNJYFXBG".equals(formSn) || "TB_YX_FORM_KRYYZFXBB".equals(formSn) || "TB_YX_FORM_MEIFENXI".equals(formSn)
			|| "TB_YX_FORM_MFXDRCFXBB".equals(formSn)||"TB_YX_FORM_JZXTCDBB".equals(formSn)){
			hasQyrq = true;
		}
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT ID,TIANXIEREN,TIANXIESHIJIAN,SYR,SYSJ,STATUS"; 
		if(hasQyrq){
			sql += ",QYRQ";
		}
		sql +=" FROM " + schema + "." + formSn;
		sql += " WHERE 1=1 ";
		
		if(StringUtil.isNotEmpty(beginYmd)){
			sql += " AND to_char(TIANXIESHIJIAN,'yyyy-MM-dd')>='" + beginYmd + "'";
		}
		if(StringUtil.isNotEmpty(endYmd)){
			sql += " AND to_char(TIANXIESHIJIAN,'yyyy-MM-dd')<='" + endYmd + "'";
		}
		if(StringUtil.isNotEmpty(tianxieren)){
			sql += " AND TIANXIEREN='" + tianxieren + "'";
		}
		if(StringUtil.isNotEmpty(sylx)){
			sql += " AND STATUS ='" + sylx + "'"; 
		}
		sql += " ORDER BY TIANXIESHIJIAN DESC";
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", resultSet.getString("ID"));
				map.put("tianxieren", resultSet.getString("TIANXIEREN"));
				oracle.sql.TIMESTAMP tianxieshijianDate = (oracle.sql.TIMESTAMP)resultSet.getObject("TIANXIESHIJIAN");
				map.put("tianxieshijian", dateFormat.format(tianxieshijianDate.dateValue()));
				map.put("shenyueren", resultSet.getString("SYR"));
				map.put("shenyueshijian", resultSet.getString("SYSJ"));
				map.put("status", resultSet.getString("STATUS"));
				if(hasQyrq){
					map.put("qyrq", resultSet.getString("QYRQ"));
				}
				
				list.add(map);
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
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 获取当前时间下的化学监督Id
	 * @param formSn 交班运行方式表单对应的 物理表
	 * @param nowYmd 当前时间
	 * @return
	 */
	public String findCurrentHxjd(String formSn, String nowYmd){
		String id = null;
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT ID FROM (SELECT ID FROM " + schema + "." + formSn + " WHERE to_char(TIANXIESHIJIAN,'yyyy-MM-dd')='" + nowYmd 
					+ "' ORDER BY TIANXIESHIJIAN DESC) WHERE ROWNUM=1";
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getString("id");
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
	 * 保存化学监督审阅信息
	 */
	public void saveShenYue(String id, String formSn,  String userName, String nowYmdHms){
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "update " + schema + "." + formSn + " set SYR='" + userName + 
						"',SYSJ='" + nowYmdHms + "',STATUS='已审阅' where id='" + id + "'";
		
		Connection conn = null;
		PreparedStatement statement = null;
		boolean isAutoCommit = true;
		
		try {
			conn = dataSource.getConnection();
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			statement = conn.prepareStatement(sql);
			statement.executeUpdate(sql);
			conn.commit();
		} catch (SQLException e) {
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
	 * 根据表单对应物理表名、开始时间、结束时间 获取运行日志
	 * @param formSn      表单对应物理表名
	 * @param beginYmd    开始时间 
	 * @param endYmd      结束时间
	 * @param tianxieren  填写人
	 * @return
	 */
	public List<Map<String, Object>> findRunLog(String formSn,String beginYmd, String endYmd, String tianxieren){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT ID,TIANXIEREN,TIANXIESHIJIAN FROM " + schema + "." + formSn;
		
		if(StringUtil.isNotEmpty(beginYmd)){
			sql += " WHERE to_char(TIANXIESHIJIAN,'yyyy-MM-dd')>='" + beginYmd + "'";
			if(StringUtil.isNotEmpty(endYmd)){
				sql += " AND to_char(TIANXIESHIJIAN,'yyyy-MM-dd')<='" + endYmd + "'";
			}
			if(StringUtil.isNotEmpty(tianxieren)){
				sql += " AND TIANXIEREN='" + tianxieren + "'";
			}
		}else if(StringUtil.isNotEmpty(endYmd)){
			sql += " WHERE to_char(TIANXIESHIJIAN,'yyyy-MM-dd')<='" + endYmd + "'";
			if(StringUtil.isNotEmpty(tianxieren)){
				sql += " AND TIANXIEREN='" + tianxieren + "'";
			}
		}else if(StringUtil.isNotEmpty(tianxieren)){
			sql += " WHERE TIANXIEREN='" + tianxieren + "'";
		}
		sql += " ORDER BY TIANXIESHIJIAN DESC";
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", resultSet.getString("ID"));
				map.put("tianxieren", resultSet.getString("TIANXIEREN"));
				oracle.sql.TIMESTAMP tianxieshijianDate = (oracle.sql.TIMESTAMP)resultSet.getObject("TIANXIESHIJIAN");
				map.put("tianxieshijian", dateFormat.format(tianxieshijianDate.dateValue()));
				
				list.add(map);
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
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 根据表单对应物理表名、开始时间、结束时间 获取运行台账
	 * @param formSn      表单对应物理表名
	 * @param beginYmd    开始时间 
	 * @param endYmd      结束时间
	 * @param tianxieren  填写人
	 * @return
	 */
	public List<Map<String, Object>> findTzForm(String formSn,String beginYmd, String endYmd, String tianxieren){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		String sql = "SELECT ID,TIANXIEREN,TIANXIESHIJIAN FROM " + schema + "." + formSn;
		
		if(StringUtil.isNotEmpty(beginYmd)){
			sql += " WHERE to_char(TIANXIESHIJIAN,'yyyy-MM-dd')>='" + beginYmd + "'";
			if(StringUtil.isNotEmpty(endYmd)){
				sql += " AND to_char(TIANXIESHIJIAN,'yyyy-MM-dd')<='" + endYmd + "'";
			}
			if(StringUtil.isNotEmpty(tianxieren)){
				sql += " AND TIANXIEREN='" + tianxieren + "'";
			}
		}else if(StringUtil.isNotEmpty(endYmd)){
			sql += " WHERE to_char(TIANXIESHIJIAN,'yyyy-MM-dd')<='" + endYmd + "'";
			if(StringUtil.isNotEmpty(tianxieren)){
				sql += " AND TIANXIEREN='" + tianxieren + "'";
			}
		}else if(StringUtil.isNotEmpty(tianxieren)){
			sql += " WHERE TIANXIEREN='" + tianxieren + "'";
		}
		sql += " ORDER BY TIANXIESHIJIAN DESC";
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while(resultSet.next()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", resultSet.getString("ID"));
				map.put("tianxieren", resultSet.getString("TIANXIEREN"));
				oracle.sql.TIMESTAMP tianxieshijianDate = (oracle.sql.TIMESTAMP)resultSet.getObject("TIANXIESHIJIAN");
				map.put("tianxieshijian", dateFormat.format(tianxieshijianDate.dateValue()));
				
				list.add(map);
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
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 返回上一时期的尾水表码
	 * @param sql
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public StringBuffer findWsdateStr(String sql) throws SQLException{
		StringBuffer datastr = new StringBuffer();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try{
		conn = dataSource.getConnection();
		st = conn.createStatement();
		rs = st.executeQuery(sql);
		datastr.append("{");
		while(rs.next()){
			datastr.append("ysbm_1:"+rs.getString("BM"));
			datastr.append(",ysbm_2:"+rs.getString("BM_2"));
		}
		datastr.append("}");
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return datastr;
	}
}
