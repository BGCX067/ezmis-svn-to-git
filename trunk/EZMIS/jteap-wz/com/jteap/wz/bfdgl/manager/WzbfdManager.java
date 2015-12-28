/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.bfdgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.bfdgl.model.Wzbfd;
@SuppressWarnings("unchecked")
public class WzbfdManager extends HibernateEntityDao<Wzbfd>{
	public Collection<Wzbfd> findYhdglByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Wzbfd as g where ");
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
