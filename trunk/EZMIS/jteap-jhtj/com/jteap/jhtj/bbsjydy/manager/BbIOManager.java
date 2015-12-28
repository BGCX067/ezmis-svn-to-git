package com.jteap.jhtj.bbsjydy.manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.bbsjydy.model.BbIO;
import com.jteap.jhtj.bbsjydy.model.BbSjzd;

public class BbIOManager extends HibernateEntityDao<BbIO> {
	private DataSource dataSource;
	public String getSchema() throws SQLException{
		String schema="";
		Connection conn=null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData dmd = conn.getMetaData();
			schema = dmd.getUserName();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
			
		}
		return schema;
	}
	
	public void deleteBbSjzdByBbioid(String bbioid){
		String whereSql="bbioid='"+bbioid+"'";
		this.removeBatchInHql(BbSjzd.class, whereSql);
	}
	
	public Long getSortNo(){
		Long res=1l;
		String hql="select max(sortno) from BbIO";
		Long curMaxSort=(Long)this.findUniqueByHql(hql);
		if(curMaxSort!=null){
			res=curMaxSort;
		}
		return res;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
