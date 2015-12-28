/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.lydgl.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.lydgl.model.Lydgl;

@SuppressWarnings("unchecked")
public class LydglManager extends HibernateEntityDao<Lydgl>{
	public Collection<Lydgl> findLydglByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Lydgl as g where ");
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
