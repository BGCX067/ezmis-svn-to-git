/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.component.livegrid.manager;

import java.util.Collection;

import com.jteap.component.livegrid.model.BigDt;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
@SuppressWarnings("unchecked")
public class BigDtManager extends HibernateEntityDao<BigDt>{
	
	public Collection<BigDt> findBigDtByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from BigDt as g where ");
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
