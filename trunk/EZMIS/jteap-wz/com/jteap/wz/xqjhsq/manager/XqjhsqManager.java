package com.jteap.wz.xqjhsq.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.group.model.Group;
import com.jteap.wz.xqjhsq.model.Xqjhsq;

public class XqjhsqManager extends HibernateEntityDao<Xqjhsq> {
	
	/**
	 * 根據角色取得有層次的資源
	 * 
	 * @param role
	 * @param parentResource
	 * @param communicable 是否可传播，可以为null 如果为空，则不添加该条件
	 * @returnw
	 */
	@SuppressWarnings("unchecked")
	public Collection findResourcesByGroup(Group group) {
		String hql = "from Group as g where ";
		if(group==null){
			hql+="g.parentGroup.id is null";
		}else{
			hql+="g.parentGroup.id='"+group.getId()+"' and g.groupName not in ('一值','二值','三值','四值','五值','燃管煤化班','输煤运行')";
		}
		hql+=" order by g.sortNo";
		return this.find(hql);
	}

}
