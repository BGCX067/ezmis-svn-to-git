/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.yhdgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wz.yhdgl.model.Wzht;
@SuppressWarnings("unchecked")
public class WzhtManager extends HibernateEntityDao<Wzht>{
	public Collection<Wzht> findYhdglByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Wzht as g");
		Object args[]=null;
		return find(hql.toString(),args);
	}
}
