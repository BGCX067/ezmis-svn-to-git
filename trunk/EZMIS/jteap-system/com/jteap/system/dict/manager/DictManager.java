package com.jteap.system.dict.manager;

import java.util.Collection;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.dict.model.Dict;

/**
 * 数据字典处理类
 * @author tantyou
 *
 */
@SuppressWarnings("unchecked")
public class DictManager extends HibernateEntityDao<Dict> {

	
	/**
	 * 根据分类唯一名称查询该类型的数据字典集合
	 * @param catalog dictcatalog.uniquename
	 * @return
	 */
	public Collection<Dict> findDictByUniqueCatalogName(String catalog){
		String hql = "from Dict as dict where dict.catalog.uniqueName = ? order by dict.sortNo";
		List<Dict> result = this.find(hql, catalog);
		return result;
	}
	
	/**
	 * 根据字典唯一标识和KEY值，查询唯一的一个数据字典对象
	 * @param catalog
	 * @param key
	 * @return
	 */
	public Dict findDictByCatalogNameWithKey(String catalog,String key){
		String hql = "from Dict as dict where dict.catalog.uniqueName = ? and dict.key = ? order by dict.sortNo";
		Dict result = (Dict) this.findUniqueByHql(hql, catalog,key);
		return result;
	}
}
