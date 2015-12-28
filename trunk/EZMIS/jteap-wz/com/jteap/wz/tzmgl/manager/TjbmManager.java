/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.tzmgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wz.tzmgl.model.Tjbm;
@SuppressWarnings("unchecked")
public class TjbmManager extends HibernateEntityDao<Tjbm>{
	
	public Collection<Tjbm> findProjcatList(){
		StringBuffer hql=new StringBuffer("from Tjbm as g ");
		return find(hql.toString());
	}
}
