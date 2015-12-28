/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.wzxsd.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.wzxsd.model.WzxsdMx;
@SuppressWarnings("unchecked")
public class WzxsdMxManager extends HibernateEntityDao<WzxsdMx>{
	public Collection<WzxsdMx> findYhdglByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from WzxsdMx as g where ");
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
