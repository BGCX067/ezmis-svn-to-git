/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.gclbgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wz.gclbgl.model.Projcat;
@SuppressWarnings("unchecked")
public class ProjcatManager extends HibernateEntityDao<Projcat>{
	
	public Collection<Projcat> findProjcatList(){
		StringBuffer hql=new StringBuffer("from Projcat as g ");
		return find(hql.toString());
	}
	
	public List<Projcat> findResourcesByProjcat(){
		String hql = "from Projcat as g where g.predefined is null";
		hql+=" order by g.projcatcode";
		return this.find(hql);
	}
	
}
