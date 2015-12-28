/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.kwwh.manager;

import java.util.*;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.kwwh.model.*;

public class KwwhManager extends HibernateEntityDao<Kw> {
	protected DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void addDefaultKW(String ckbm) {
		Kw kw = new Kw();
		kw.setCkid(ckbm);
		kw.setCwmc("缺省库位");
		this.save(kw);
	}
	
	@SuppressWarnings("unchecked")
	public List<Kw> findKwByParentId(String parentId){
		StringBuffer hql=new StringBuffer("from Kw as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.pkw is null");
			
		}else{
			hql.append("g.pkw.id=?");
			args=new Object[]{parentId};
		}
		hql.append(" order by cwmc asc");
		return find(hql.toString(),args);
	}

	@SuppressWarnings("unchecked")
	public List<Kw> getKWList(String ckbm, String parentId) {
		String hql = "from Kw g where";
		if(parentId==null){
			hql += " g.pkw.id is null and ckid = ?";
			return this.find(hql, new Object[]{ckbm});
		}else{
			hql += " g.pkw.id = ?";
			return this.find(hql, new Object[]{parentId});		
		}
	}

	public void delKwById(String kwid) {
		String sql = "delete from tb_wz_skwgl where id in (select id from tb_wz_skwgl t start with t.id=? connect by prior t.id = t.pkw.id)";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, new String[] { kwid });
	}
}
