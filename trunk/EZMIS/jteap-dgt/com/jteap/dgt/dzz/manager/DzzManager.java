package com.jteap.dgt.dzz.manager;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.dgt.dzz.model.Dzz;

/**
 * 
 * @author lvchao
 *
 */
@SuppressWarnings("unchecked")
public class DzzManager extends HibernateEntityDao<Dzz> {

	
	
	public Collection findDzzByParentId(String parentId) {
		StringBuffer hql = new StringBuffer("from dzz as d where ");
		Object args[] = null;
		if (StringUtils.isEmpty(parentId)) {
			hql.append("d.PARENT_ID is null");

		} else {
			hql.append("d.PARENT_ID.id=?");
			args = new String[] { parentId };
		}
		return find(hql.toString(), args);
	}
	
	
	public List findDzzByDangZu(String id){
		StringBuffer sb=new StringBuffer("from Dyxxk as d where ");
		Object args[]=null;
		if(StringUtils.isNotEmpty(id)){
			sb.append("d.id = ?");
			args = new String[] { id };
		}else {
			
			return null;
		}
		return find(sb.toString(),args);
	}
	
	
	
}
