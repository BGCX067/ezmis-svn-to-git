package com.jteap.system.dict.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.dict.model.DictCatalog;

/**
 * 数据字典类型管理类
 * @author 叶飞
 *
 */
public class DictCatalogManager extends HibernateEntityDao<DictCatalog> {
	/**
	 * 查询根节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<DictCatalog> findRootDictCatalog(){
		String hql="from DictCatalog as g where g.parentDictCatalog=null order by g.sortNo";
		return this.find(hql);
	}
	
}
