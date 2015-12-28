package com.jteap.gcht.htsp.manager;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.gcht.htsp.model.HeTongFyxz;

public class HeTongFyxzManager extends HibernateEntityDao<HeTongFyxz> {
	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * @param parentId  
	 * @return  如果parentId为空 返回所有顶层节点
	 * 否则返回所有指定parentId下的所有子节点
	 */
	
	@SuppressWarnings("unchecked")
	public Collection<HeTongFyxz> findCatalogByParentId(String parentId,String flbm){
		
		StringBuffer hql=new StringBuffer("from HeTongFyxz as g where ");
		Object args[]=null;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null and g.flbm='"+flbm+"'");
			
		}else{
			hql.append("g.parent.id=? and g.flbm='"+flbm+"'");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<HeTongFyxz> findCatalogByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from HeTongFyxz as g where ");
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
