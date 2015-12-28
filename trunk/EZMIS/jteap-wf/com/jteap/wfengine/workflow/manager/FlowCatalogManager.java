package com.jteap.wfengine.workflow.manager;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wfengine.workflow.model.FlowCatalog;

/**
 * 表单分类操作对象
 * @author tanchang
 *
 */
@SuppressWarnings("unchecked")
public class FlowCatalogManager extends HibernateEntityDao<FlowCatalog> {

	
	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * @param parentId  
	 * @return  如果parentId为空 返回所有顶层节点
	 * 否则返回所有指定parentId下的所有子节点
	 */
	
	public Collection<FlowCatalog> findCatalogByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from FlowCatalog as g where ");
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null");
			return find(hql.toString(),new Object[]{});
		}else{
			hql.append("g.parent.id=?");
			return find(hql.toString(),new Object[]{parentId});
		}
	}
	
	
}
