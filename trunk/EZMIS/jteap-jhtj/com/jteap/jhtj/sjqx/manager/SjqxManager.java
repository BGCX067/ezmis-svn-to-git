package com.jteap.jhtj.sjqx.manager;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.sjqx.model.Sjqx;

public class SjqxManager extends HibernateEntityDao<com.jteap.jhtj.sjqx.model.Sjqx> {
	/**
	 * 
	 *描述：根据角色ID批量删除数据权限
	 *时间：2010-5-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void removeAllByRoleId(String roleid){
		this.removeBatchInHql(entityClass, "roleid='"+roleid+"'");
	}
	
	/**
	 * 
	 *描述：保存数据权限对象
	 *时间：2010-5-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void saveSjqx(String ids,String roleid,String lx){
		String[] idArray=ids.split(",");
		for(String id:idArray){
			Sjqx sjqx=new Sjqx();
			sjqx.setQxid(id);
			sjqx.setQxlx(lx);
			sjqx.setRoleid(roleid);
			this.save(sjqx);
		}
	}

}
