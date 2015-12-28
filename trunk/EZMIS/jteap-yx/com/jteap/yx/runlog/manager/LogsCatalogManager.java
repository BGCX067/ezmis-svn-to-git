/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.manager;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.yx.runlog.model.LogsCatalog;

/**
 * 运行日志分类Manager
 * @author caihuiwen
 */
public class LogsCatalogManager extends HibernateEntityDao<LogsCatalog>{
	
	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * @param parentId  
	 * @return  如果parentId为空 返回所有顶层节点
	 * 否则返回所有指定parentId下的所有子节点
	 */
	@SuppressWarnings("unchecked")
	public Collection<LogsCatalog> findCatalogByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from LogsCatalog as g where ");
		Object args[]=null;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
}
