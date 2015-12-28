/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.fjcldgl.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.fjcldgl.model.Fjcldmx;
@SuppressWarnings("unchecked")
public class FjcldmxManager extends HibernateEntityDao<Fjcldmx>{
	public Collection<Fjcldmx> findFjhsdmxByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Fjcldmx as g where ");
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
