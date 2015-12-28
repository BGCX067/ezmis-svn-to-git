/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.gcht.gysgl.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.gcht.gysgl.model.GysCatalog;
import com.jteap.sb.sbpjgl.model.SbpjCatalog;

/**
 * 供应商分类Manager
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class GysCatalogManager extends HibernateEntityDao<GysCatalog> {

	private DataSource dataSource;

	/**
	 * 
	 * 描述 : 根据父节点ID查找招投标分类
	 * 作者 : wangyun
	 * 时间 : 2010-11-18
	 * 参数 : 
	 * 		parentId ：父节点ID
	 * 返回值 : 
	 * 		List<GysCatalog> ： 供应商分类
	 * 
	 */
	public List<GysCatalog> findGysCatalogByParentId(String parentId) {
		StringBuffer hql = new StringBuffer("from GysCatalog as g where ");
		Object args[] = null;
		if (StringUtil.isEmpty(parentId)) {
			hql.append("g.parentGys is null");

		} else {
			hql.append("g.parentGys.id=?");
			args = new String[] { parentId };
		}
		hql.append(" order by g.sortno");
		return find(hql.toString(), args);
	}

	/**
	 * 
	 * 描述 : 通过供应商分类，查找供应商标信息数量
	 * 作者 : wangyun
	 * 时间 : 2010-11-18
	 * 参数 : 
	 * 		gysCatalogId : 供应商分类ID
	 * 返回值 : 
	 * 		total : 供应商信息数量
	 * 异常 :  Exception 
	 * 
	 */
	public int findGysxxByGysCatalog(String gysCatalogId) throws Exception {
		int total = 0;

		GysCatalog gysCatalog = this.get(gysCatalogId);
		total = gysCatalog.getChildGyss().size();
		if (total > 0) {
			return total;
		}
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			String sql = "select count(*) from TB_HT_GYSXX where CATALOG_ID = '"+gysCatalogId+"'";
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

	/**
	 * 
	 * 描述 : 查询最大的一个分类编码对象
	 * 作者 : wangyun
	 * 时间 : 2010-11-22
	 * 参数 : 
	 * 		condition : 父节点ID
	 * 返回值 : 
	 * 		GysCatalog : 供应商对象
	 * 
	 */
	public GysCatalog getGysCatalogByHql(String condition) {
		String hql = "";
		if(StringUtil.isEmpty(condition)){
			hql = "from GysCatalog s where s.parentGys.id is null order by s.flbm desc";
		}else{
			hql = "from GysCatalog s where s.parentGys.id='"+condition+"' order by s.flbm desc";
		}
		GysCatalog gysCatalog = null;
		List<GysCatalog> list = new ArrayList<GysCatalog>();
		Object args[] = null;
		list = find(hql, args);
		for (int i = 0; i < list.size(); i++) {
			gysCatalog = (GysCatalog)list.get(0);
		}
		return gysCatalog;
	}
	
	
}
