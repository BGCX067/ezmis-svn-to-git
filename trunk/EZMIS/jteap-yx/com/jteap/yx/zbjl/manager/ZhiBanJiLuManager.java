/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.zbjl.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.yx.zbjl.model.ZhiBanJiLu;

/**
 * 值班记录Manager
 * @author caihuiwen
 *
 */
@SuppressWarnings("unchecked")
public class ZhiBanJiLuManager extends HibernateEntityDao<ZhiBanJiLu>{
	
	public ZhiBanJiLu findShenYueByZbsjZbbc(String zbsj, String zbbc){
		String hql = "from ZhiBanJiLu t where t.jllb='审阅' and t.zbsj=? and t.zbbc=? order by jlsj desc";
		List<ZhiBanJiLu> list = this.find(hql, new Object[]{zbsj,zbbc});
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据sql 查询出相应角色的人员ID
	 */
	public String findRolIds(String sql){
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		Connection con = null;
		Statement st = null;
		ResultSet res = null;
		String rol="";
		try{
			con = dataSource.getConnection();
			st = con.createStatement();
			res = st.executeQuery(sql);
			while(res.next()){
				rol=rol+res.getString(1)+",";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rol;
	}
}
