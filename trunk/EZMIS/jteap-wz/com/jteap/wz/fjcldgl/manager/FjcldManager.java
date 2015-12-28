/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.fjcldgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.fjcldgl.model.Fjcld;

@SuppressWarnings("unchecked")
public class FjcldManager extends HibernateEntityDao<Fjcld>{
	
	public Collection<Fjcld> findFjhsdByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Fjcld as g where ");
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
