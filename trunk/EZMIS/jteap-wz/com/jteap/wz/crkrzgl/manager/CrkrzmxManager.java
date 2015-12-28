/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.crkrzgl.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.crkrzgl.model.Crkrzmx;
@SuppressWarnings("unchecked")
public class CrkrzmxManager extends HibernateEntityDao<Crkrzmx>{
	
	public Collection<Crkrzmx> findCrkrzmxByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Crkrzmx as g where ");
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
