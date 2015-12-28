/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.ckgl.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.ckgl.model.Ckgl;
@SuppressWarnings("unchecked")
public class CkglManager extends HibernateEntityDao<Ckgl>{
	
	public Collection<Ckgl> findCkglList(){
		StringBuffer hql=new StringBuffer("from Ckgl as g ");
		return find(hql.toString());
	}
	/**
	 * 保存仓库的月末库存
	 */
	public void saveYmkc(){
		Connection conn = null;
		Statement st = null;
		Statement batchSt = null;
		ResultSet res = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		List<Ckgl> ckglList = (List<Ckgl>) this.findCkglList();
		try {
			conn = dataSource.getConnection();
			st= conn.createStatement();
			batchSt = conn.createStatement();
			String sql = "";
			for(Ckgl ck:ckglList){
				sql = "SELECT SUM(T.DQKC) FROM TB_WZ_SWZDA T " +
						"WHERE T.DQKC >0 AND T.KWBM IN (SELECT K.ID FROM TB_WZ_SKWGL K WHERE K.CKID ='"+ck.getId()+"') ";
				//System.out.println(sql);
				res = st.executeQuery(sql);
				while(res.next()){
					batchSt.addBatch("insert into tb_wz_ymkc values('"+ck.getId()+"','sfzj',sysdate,"+res.getDouble(1)+")");
				}
			}
			batchSt.executeBatch();
			System.out.println("添加完毕");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
