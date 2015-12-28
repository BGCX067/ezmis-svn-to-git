/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.dbdgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.dbdgl.model.Dbd;
@SuppressWarnings("unchecked")
public class DbdManager extends HibernateEntityDao<Dbd>{
	public Collection<Dbd> findYhdglByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Dbd as g where ");
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
