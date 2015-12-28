/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.index.model.Preference;

/**
 * 个性化设置Manager
 * @author caihuiwen
 */
@SuppressWarnings("unchecked")
public class PreferenceManager extends HibernateEntityDao<Preference>{
	
	public Preference findConfig(String personId){
		Preference preference = null;
		
		String hql = "from Preference t where t.personId=?";
		List<Preference> list = this.find(hql, personId);
		if(list.size() > 0){
			preference = list.get(0);
		}
		
		return preference;
	}
	
}
