package com.jteap.system.resource.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.resource.model.Operation;
/**
 * 操作管理器
 * 
 * @author tantyou
 * @date 2008-1-30
 */

@SuppressWarnings("unchecked")
public class OperationManager extends HibernateEntityDao<Operation>{
	
	/**
	 * 查询所有管理员默认操作集合
	 * @return
	 */
	public Collection findAdminOperationList(){
		String hql="from Operation as op where op.adminOp='1'";
		return this.find(hql);
	}

}
