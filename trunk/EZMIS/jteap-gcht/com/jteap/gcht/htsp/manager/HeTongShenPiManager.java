/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.htsp.manager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

import com.jteap.gcht.htsp.model.Cwht;
import com.jteap.gcht.htsp.model.Gcht;
import com.jteap.gcht.htsp.model.Ht;
import com.jteap.gcht.htsp.model.Rlht;
import com.jteap.gcht.htsp.model.Wzht;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 合同审批Manager 
 * @author caihuiwen
 */
public class HeTongShenPiManager extends JdbcManager{
	
	/**
	 * 获取当前的合同序号
	 * @param tableName	合同物理表名称
	 * @param htlx		合同类型
	 * @return
	 */
	public int findCurrentHtxh(String tableName, String htlx){
		int htxh = 100;
		String sql = "select max(htxh) htxh from " + tableName + " t where htlx='" + htlx + "'";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				if(resultSet.getInt("htxh") >= 101){
					htxh = resultSet.getInt("htxh");
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
		
		return htxh + 1;
	}
	
	/**
	 * 查找子合同条数
	 * @param tableName
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public Integer findZHtxh(String tableName,String id) throws SQLException{
		Integer res=1;
		String sql = "select count(*) as ct from " + tableName + " t where parentid='" + id + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				Object obj=resultSet.getObject("ct");
				if(obj!=null){
					int count=((BigDecimal)obj).intValue();
					if(count>=1){
						res=count+1;
					}
				}
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
		return res;
	}
	
	
	/**
	 * 判断合同编号是否唯一
	 * @param tableName
	 * @param htbh
	 * @return
	 */
	public boolean isUniqHtbh(String tableName, String htbh){
		boolean result=true;
		String sql = "select * from " + tableName + " t where htbh='" + htbh + "'";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				result=false;
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
		
		return result;
	}
	
	/**
	 * 查找物资合同信息
	 * 作者:童贝
	 * 时间:Feb 11, 2011
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public Ht findWzhtByID(String id,String tableName) throws SQLException{
		Wzht wzht=new Wzht();
		wzht.setId(id);
		wzht.setTableName(tableName);
		String sql = "select * from " + tableName + " t where id='" + id + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);

			if(resultSet.next()){
				wzht.setHtmc(resultSet.getString("HTMC"));
				wzht.setHtbh(resultSet.getString("HTBH"));
				wzht.setHtlx(resultSet.getString("HTLX"));
				BigDecimal je=resultSet.getBigDecimal("HTJE");
				wzht.setHtje(je==null?"":je.toString());
				wzht.setGfdw(resultSet.getString("GFDW"));
				wzht.setXfdw(resultSet.getString("XFDW"));
				wzht.setQddd(resultSet.getString("QDDD"));
				wzht.setCjsj(resultSet.getString("CJSJ"));
				wzht.setJhsqdh(resultSet.getString("JHSQDH"));
				wzht.setSqbm(resultSet.getString("SQBM"));
				wzht.setSqr(resultSet.getString("SQR"));
				wzht.setQtsm(resultSet.getString("QTSM"));
				wzht.setFyxz(resultSet.getString("FYXZ"));
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
		return wzht;
	}
	
	
	/**
	 * 查找燃料合同信息
	 * 作者:童贝
	 * 时间:Feb 11, 2011
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public Ht findRlhtByID(String id,String tableName) throws SQLException{
		Rlht rlht=new Rlht();
		rlht.setId(id);
		rlht.setTableName(tableName);
		String sql = "select * from " + tableName + " t where id='" + id + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);

			if(resultSet.next()){
				rlht.setHtmc(resultSet.getString("HTMC"));
				rlht.setHtbh(resultSet.getString("HTBH"));
				rlht.setHtlx(resultSet.getString("HTLX"));
				rlht.setGfdw(resultSet.getString("GFDW"));
				rlht.setXfdw(resultSet.getString("XFDW"));
				rlht.setQddd(resultSet.getString("QDDD"));
				rlht.setCjsj(resultSet.getString("CJSJ"));
				rlht.setJhsqdh(resultSet.getString("JHSQDH"));
				rlht.setSqbm(resultSet.getString("SQBM"));
				rlht.setSqr(resultSet.getString("SQR"));
				rlht.setQtsm(resultSet.getString("QTSM"));
				rlht.setFyxz(resultSet.getString("FYXZ"));
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
		return rlht;
	}
	
	
	
	/**
	 * 查找工程合同信息
	 * 作者:童贝
	 * 时间:Feb 11, 2011
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public Ht findGchtByID(String id,String tableName) throws SQLException{
		Gcht gcht=new Gcht();
		gcht.setId(id);
		gcht.setTableName(tableName);
		String sql = "select * from " + tableName + " t where id='" + id + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				gcht.setHtxh(resultSet.getString("HTXH"));
				gcht.setGcid(resultSet.getString("GCID"));
				gcht.setHtmc(resultSet.getString("HTMC"));
				gcht.setHtbh(resultSet.getString("HTBH"));
				gcht.setHtlx(resultSet.getString("HTLX"));
				BigDecimal je=resultSet.getBigDecimal("HTJE");
				gcht.setHtje(je==null?"":je.toString());
				gcht.setHtxmswtf(resultSet.getString("HTXMSWTF"));
				gcht.setGcmc(resultSet.getString("GCMC"));
				gcht.setGcbm(resultSet.getString("GCBM"));
				gcht.setCbfs(resultSet.getString("CBFS"));
				gcht.setFyly(resultSet.getString("FYLY"));
				gcht.setDjyj(resultSet.getString("DJYJ"));
				gcht.setGfdw(resultSet.getString("GFDW"));
				gcht.setCjsj(resultSet.getString("CJSJ"));
				gcht.setJhsqdh(resultSet.getString("JHSQDH"));
				gcht.setSqbm(resultSet.getString("SQBM"));
				gcht.setSqr(resultSet.getString("SQR"));
				gcht.setQtsm(resultSet.getString("QTSM"));
				gcht.setFyxz(resultSet.getString("FYXZ"));
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
		return gcht;
	}
	
	
	/**
	 * 查找工程合同信息
	 * 作者:童贝
	 * 时间:Feb 11, 2011
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public Ht findCwhtByID(String id,String tableName) throws SQLException{
		Cwht cwht=new Cwht();
		cwht.setId(id);
		cwht.setTableName(tableName);
		String sql = "select * from " + tableName + " t where id='" + id + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				cwht.setHtxh(resultSet.getString("HTXH"));
				cwht.setHtcjsj(resultSet.getString("HTCJSJ"));
				cwht.setHtmc(resultSet.getString("HTMC"));
				cwht.setHtbh(resultSet.getString("HTBH"));
				cwht.setHtlx(resultSet.getString("HTLX"));
				BigDecimal je=resultSet.getBigDecimal("HTJE");
				cwht.setHtje(je==null?"":je.toString());
				cwht.setQtsm(resultSet.getString("QTSM"));
				cwht.setFyxz(resultSet.getString("FYXZ"));
				cwht.setLl(resultSet.getString("LL"));
				cwht.setHjh(resultSet.getString("HJH"));
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
		return cwht;
	}
	
	/**
	 * 初始化合同编号和序号
	 * 作者:童贝
	 * 时间:Feb 11, 2011
	 * @param wzht
	 * @throws SQLException
	 */
	public void inithtBhAndXh(Ht ht) throws SQLException{
		//得到当前的序号
		Integer zhtCount=this.findZHtxh(ht.getTableName(), ht.getId());
		String xh=zhtCount.toString();
		//补零
		xh=xh.length()==2?xh:("0"+xh);
		ht.setHtbh(ht.getHtbh()+"补"+xh);
		ht.setHtxh(xh);
	}
	
	/**
	 * 判断是否是子合同
	 * 作者:童贝
	 * 时间:Feb 11, 2011
	 * @param id
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public boolean isZthByID(String id,String tableName) throws SQLException{
		boolean res=false;
		String sql = "select parentid from " + tableName + " t where id='" + id + "'";
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				String  parentid=resultSet.getString("parentid");
				if(StringUtils.isNotEmpty(parentid)){
					res=true;
				}
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
		return res;
	}
}
