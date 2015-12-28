/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.gqjtz.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.wz.gqjtz.model.*;

public class GqjtzManager extends HibernateEntityDao<Gqjtz>{
	@SuppressWarnings("unchecked")
	public Collection<Gqjtz> findGqjtzByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Gqjtz as g where ");
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
