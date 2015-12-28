package com.jteap.system.doclib.manager;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.doclib.model.DoclibLevel;

/**
 * 文档级别管理对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public class DoclibLevelManager extends HibernateEntityDao<DoclibLevel> {

	
	public Collection<DoclibLevel> findAllDoclibLevel() {

		StringBuffer hql = new StringBuffer("from DoclibLevel as  ");
		Object args[] = null;
		return find(hql.toString(), args);
	}
	
	/**
	 * 
	 * @param o
	 * @param kyes
	 */
	public void remove(Object o,Serializable[] kyes) {
		removeBatch( o.getClass(), kyes);
	}
 
}
