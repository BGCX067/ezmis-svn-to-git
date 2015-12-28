package com.jteap.form.eform.manager;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HZ2PY;
import com.jteap.form.eform.model.EFormCatalog;

/**
 * 表单分类操作对象
 * @author tanchang
 *
 */
@SuppressWarnings("unchecked")
public class EFormCatalogManager extends HibernateEntityDao<EFormCatalog> {

	
	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * @param parentId  
	 * @return  如果parentId为空 返回所有顶层节点
	 * 否则返回所有指定parentId下的所有子节点
	 */
	
	public Collection<EFormCatalog> findCatalogByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from EFormCatalog as g where ");
		Object args[]=null;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
	/**
	 * 根据分类编号取得该分类存放表单文件的目录名称
	 * @param catalogId
	 * @return
	 */
	public String getCatalogDirectoryName(String catalogId){
		String hql="select catalogName from EFormCatalog where id=?";
		List list=this.find(hql, new Object[]{catalogId});
		String catalogName=list.get(0).toString();
		return HZ2PY.getPy1(catalogName,false).toUpperCase();
	}
	
	
}
