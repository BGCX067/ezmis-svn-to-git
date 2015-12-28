/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.gcxmgl.manager;

import java.util.*;
import javax.sql.DataSource;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.gcxmgl.model.Proj;

public class ProjManager extends HibernateEntityDao<Proj> {
	protected DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

//	public void addDefaultKW(String ckbm) {
//		Kw kw = new Kw();
//		kw.setCkid(ckbm);
//		kw.setCwmc("缺省库位");
//		this.save(kw);
//	}
	
	@SuppressWarnings("unchecked")
	public List<Proj> findProjByParentId(String parentId){
		StringBuffer hql=new StringBuffer("from Proj as g where g.finished='0'  and g.projcat.predefined is null and g.isviable is null  and ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.p_proj is null");
			
		}else{
			hql.append("g.p_proj.id=?");
			args=new Object[]{parentId};
		}
		return find(hql.toString(),args);
	}

	@SuppressWarnings("unchecked")
	public List<Proj> getProjList(String ckbm, String parentId) {
		String hql = "from Proj g where";
		if(parentId==null){
			hql += " g.p_proj.id is null and g.isviable is null and  g.projcat.id = ?";
			return this.find(hql, new Object[]{ckbm});
		}else{
			hql += " g.p_proj.id = ? and g.isviable is null ";
			return this.find(hql, new Object[]{parentId});		
		}
	}

//	public void delKwById(String kwid) {
//		String sql = "delete from tb_wz_skwgl where id in (select id from tb_wz_skwgl t start with t.id=? connect by prior t.id = t.pkw.id)";
//		JdbcTemplate template = new JdbcTemplate(dataSource);
//		template.update(sql, new String[] { kwid });
//	}
}
