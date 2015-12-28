/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.gcht.ztbgl.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.gcht.ztbgl.model.ZtbCatalog;

/**
 * 招标分类Manager
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class ZtbCatalogManager extends HibernateEntityDao<ZtbCatalog> {

	private DataSource dataSource;

	/**
	 * 
	 * 描述 : 根据父节点ID查找招投标分类
	 * 作者 : wangyun
	 * 时间 : 2010-11-18
	 * 参数 : 
	 * 		parentId ：父节点ID
	 * 返回值 : 
	 * 		List<ZtbCatalog> ： 招投标分类
	 * 
	 */
	public List<ZtbCatalog> findZtbCatalogByParentId(String parentId) {
		StringBuffer hql = new StringBuffer("from ZtbCatalog as g where ");
		Object args[] = null;
		if (StringUtil.isEmpty(parentId)) {
			hql.append("g.parentZtb is null");

		} else {
			hql.append("g.parentZtb.id=?");
			args = new String[] { parentId };
		}
		return find(hql.toString(), args);
	}

	/**
	 * 
	 * 描述 : 通过招标分类，查找招投标信息数量
	 * 作者 : wangyun
	 * 时间 : 2010-11-18
	 * 参数 : 
	 * 		ztbCatalogId : 招投标分类ID
	 * 返回值 : 
	 * 		total : 招投标信息数量
	 * 异常 :  Exception 
	 * 
	 */
	public int findZtbxxByZtbCatalog(String ztbCatalogId) throws Exception {
		Connection conn = null;
		int total = 0;
		try {
			conn = dataSource.getConnection();
			String sql = "select count(*) from tb_ht_zbxx where zbflid = '"+ztbCatalogId+"'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		}
		return total;
		
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
}
