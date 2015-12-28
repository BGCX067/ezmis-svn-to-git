/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.gysda.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.gysda.model.*;
@SuppressWarnings("unchecked")
public class GysdaManager extends HibernateEntityDao<Gysda>{
	
	public void useQueryCache(boolean flag){
		getHibernateTemplate().setCacheQueries(flag);
	}
	
	public Collection<Gysda> findGysdaByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Gysda as g where ");
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
