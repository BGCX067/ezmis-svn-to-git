/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.lp.lpxxgl.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.lp.lpxxgl.model.*;

/**
 * 安全措施分类Manager
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unused", "unchecked", "serial" })
public class AqcsCatalogManager extends HibernateEntityDao<AqcsCatalog> {

	public Collection<AqcsCatalog> findAqcsCatalogByParentId(String parentId) {

		StringBuffer hql = new StringBuffer("from AqcsCatalog as g where ");
		Object args[] = null;
		if (StringUtil.isEmpty(parentId)) {
			hql.append("g.parent is null");

		} else {
			hql.append("g.parent.id=?");
			args = new String[] { parentId };
		}
		return find(hql.toString(), args);
	}
}
