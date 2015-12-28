/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.fjhsdgl.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.fjhsdgl.model.Fjhsdmx;
@SuppressWarnings("unchecked")
public class FjhsdmxManager extends HibernateEntityDao<Fjhsdmx>{
	public Collection<Fjhsdmx> findFjhsdmxByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Fjhsdmx as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
}
