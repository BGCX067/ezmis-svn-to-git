/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.slgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wz.slgl.model.Slgl;
@SuppressWarnings("unchecked")
public class SlglManager extends HibernateEntityDao<Slgl>{
	
	public Collection<Slgl> findProjcatList(){
		StringBuffer hql=new StringBuffer("from Slgl as g ");
		return find(hql.toString());
	}
}
