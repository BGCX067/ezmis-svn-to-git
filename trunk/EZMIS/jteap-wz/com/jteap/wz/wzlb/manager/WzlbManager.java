/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.wzlb.manager;

import java.util.*;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.wzlb.model.*;

public class WzlbManager extends HibernateEntityDao<Wzlb>{
	protected DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@SuppressWarnings("unchecked")
	public List<Wzlb> findWzlbByParentId(String parentId){
		StringBuffer hql=new StringBuffer("from Wzlb as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.flbbm is null order by g.wzlbmc");
			
		}else{
			hql.append("g.flbbm.id=? order by g.wzlbmc");
			args=new Object[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
	public void delKwById(String kwid) {
		String sql = "delete  from tb_wz_skwgl where id in (select id from tb_wz_skwgl t start with t.id=? connect by prior t.id = t.sjid)";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, new String[] { kwid });
	}
}
