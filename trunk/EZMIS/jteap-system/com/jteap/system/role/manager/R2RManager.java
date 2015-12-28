package com.jteap.system.role.manager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.model.R2R;
import com.jteap.system.role.model.Role;

/**
 * Resource Role 中間對象管理
 * 
 * @author tantyou
 * @date 2008-1-23
 */

@SuppressWarnings("unchecked")
public class R2RManager extends HibernateEntityDao<R2R> {

	/**
	 * 根據角色取得有層次的資源
	 * 
	 * @param role
	 * @param parentResource
	 * @param communicable 是否可传播，可以为null 如果为空，则不添加该条件
	 * @returnw
	 */
	public Collection findResourcesByRole(Role role, Resource parentResource,Boolean communicable) {
		String hql = "from R2R as r2r where r2r.role.id='"+role.getId()+"' and r2r.resource.parentRes";
		if(parentResource==null){
			hql+=" is null";
		}else{
			hql+=".id='"+parentResource.getId()+"'";
		}
		if(communicable!=null){
			hql+=" and r2r.communicable="+communicable;
		}
		hql+=" order by r2r.resource.sortNo";
		return this.find(hql);
	}

	/**
	 * 判断指定的角色和资源之间是否存在关联
	 * @param roleId
	 * @param resId
	 * @return
	 */
	public boolean isExist(String roleId,String resId){
		String hql="from R2R as r2r where r2r.role.id='"+roleId+"' and r2r.resource.id='"+resId+"'";
		List list=this.find(hql);
		return list.size()>0;
	}

	/**
	 * 为角色指定资源，及创建R2R中间对象
	 * 为角色指定资源之后，该角色的子角色将需要继承该资源
	 * @param roleId
	 * @param resId
	 * @return
	 */
	public boolean indicatResourceToRole(String roleId,String resId){
		Role role=this.get(Role.class,roleId);
		Resource res=this.get(Resource.class,resId);
		R2R r2r=new R2R();
		r2r.setRole(role);
		r2r.setResource(res);
		r2r.setInherit(false);
		r2r.setCommunicable(true);
		this.save(r2r);
		this.cascadeSetupCommunicableResource(role, res, true);
		return true;
	}

	/**
	 * 级联继承资源
	 * @param role
	 * @param parentRole
	 */
	public void cascadeExtendResouce(String roleId,String parentRoleId){
		Role role=this.get(Role.class,roleId);
		String hql="from R2R as r2r where r2r.role.id='"+parentRoleId+"' and communicable='1'";
		Collection<R2R> r2rs=this.find(hql);
		for (R2R r2r : r2rs) {
			createR2R(role, r2r.getResource(), true, true);
			cascadeSetupCommunicableResource(role,r2r.getResource(),true);
		}
	}
	
	/**
	 * 指定角色的“继承资源”
	 * @param roleId
	 * @param resId
	 * @return
	 */
	public boolean extendResourceForRole(String roleId,String resId){
		Role role=this.get(Role.class,roleId);
		Resource res=this.get(Resource.class,resId);
		this.createR2R(role, res, true, true);
		return true;
	}
	/**
	 * 解除指定角色和指定资源之间的关联
	 * @param role 角色对象
	 * @param resId	资源编号
	 */
	public void cascadeRemoveResourceFromRole(Role role,String resId){
		R2R r2r=this.findR2R(role.getId().toString(), resId); 
		if(r2r == null) {
			return ;
		}
		this.remove(r2r);
		for (Role childRole : role.getChildRoles()) {
			cascadeRemoveResourceFromRole(childRole, resId);
		}
	}
	
	/**
	 * 根据指定的参数创建R2R，如果已经存在关联的，根据参数进行更新
	 * @param role
	 * @param res
	 * @param inherit
	 * @param communicable
	 */
	public void createR2R(Role role,Resource res,boolean inherit,boolean communicable){
		R2R r2r=findR2R(role.getId().toString(),res.getId().toString());
		if(r2r!=null){
			r2r.setInherit(inherit);
			r2r.setCommunicable(communicable);
			this.save(r2r);
		}else{
			R2R newR2R=new R2R();
			newR2R.setRole(role);
			newR2R.setResource(res);
			newR2R.setInherit(inherit);
			newR2R.setCommunicable(communicable);
			this.save(newR2R);
		}
	}
	
	public R2R findR2R(String roleId,String resId){
		String hql="from R2R as r2r where r2r.role.id='"+roleId+"' and r2r.resource.id='"+resId+"'";
		List results=this.find(hql);
		if(results.size()>0)
			return (R2R) results.get(0);
		else
			return null;
	}
	
	/**
	 * 删除指定角色和资源的关联
	 * @param roleId
	 * @param resId
	 * @return
	 */
	public boolean removeR2R(String roleId,String resId){
		String hql="delete R2R as r2r where r2r.role.id='"+roleId+"' and r2r.resource.id='"+resId+"'";
		Query query=this.createQuery(hql);
		return query.executeUpdate()>0;
	}

	
	/**
	 * 清除指定角色所有的‘指定资源’
	 * @param roleId
	 */
	public void clearIndicatResourceOfTheRole(String roleId){
		String hql="delete R2R as r2r where r2r.role.id='"+roleId+"' and r2r.inherit='0'";
		Query query=this.createQuery(hql);
		query.executeUpdate();
	}

	
	
	/**
	 * 清除指定角色的所有继承的资源，并级联更新子角色的继承资源
	 * @param roleId 指定角色的编号
	 */
	public void clearExtendResourceOfTheRole(final String roleId){
		String hql="from R2R as r2r where r2r.role.id='"+roleId+"' and inherit='1'";
		Collection<R2R> r2rs=this.find(hql);
		for (R2R r2r : r2rs) {
			//this.remove(r2r);
			String hql2="delete R2R as r2r where r2r.id='"+r2r.getId()+"'";
			this.createQuery(hql2).executeUpdate();
			this.flush();
			cascadeRemoveResourceFromRole(r2r.getRole(),r2r.getResource());
		}
	}
	/**
	 * 级联删除指定角色的子角色与指定资源之间的关联
	 * @param role
	 * @param res
	 */
	private void cascadeRemoveResourceFromRole(Role role,Resource res){
		for (Role rolex : role.getChildRoles()) {
			String hql="delete R2R as r2r where r2r.role.id='"+rolex.getId()+"' and r2r.resource.id='"+res.getId()+"'";
			this.createQuery(hql).executeUpdate();
			this.flush();
			cascadeRemoveResourceFromRole(rolex,res);
		}
		
	}
	/**
	 * 批量更新资源的可传播性
	 * @param ids r2r编号集合
	 * @param communicable 可传播性
	 */
	public void batchUpdateR2RCommunicable(final String[] ids,final boolean communicable){

		this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				String hsql = "";
				for (int i = 0; i < ids.length; i++) {
					if (hsql.equals("")) {
						hsql += "entity.id='" + ids[i] + "'";
					} else {
						hsql += " or entity.id='" + ids[i] + "'";
					}
				}

				hsql = "from R2R as entity where "
						+ hsql;
				Query query = session.createQuery(hsql);
				query.setCacheable(true);
				ScrollableResults scRes = query.scroll();
				while (scRes.next()) {
					R2R r2r = (R2R) scRes.get(0);
					r2r.setCommunicable(communicable);
					cascadeSetupCommunicableResource(r2r.getRole(), r2r.getResource(), communicable);
				}
				return null;
			}
		});
	}
	/**
	 * 级联设置R2R
	 * @param role
	 * @param res
	 * @param communicable
	 */
	private void cascadeSetupCommunicableResource(Role role,Resource res,boolean communicable){
		if(communicable){//可传播的情况,为所有子角色设置该资源
			for (Role childNode : role.getChildRoles()) {
				if(childNode.isInheritable()){
					createR2R(childNode,res,true,communicable);
					cascadeSetupCommunicableResource(childNode,res,communicable);	
				}
			}
		}else{//不可传播的情况，删除所有子角色中的该资源 级联收回资源
			for(Role childNode:role.getChildRoles()){
				removeR2R(childNode.getId()+"",res.getId()+"");
				cascadeSetupCommunicableResource(childNode, res, communicable);
			}
		}
	}
	
	
	/**
	 * 删除指定资源的所有关联
	 * @param resourceId
	 * @throws Exception
	 */
	public void deleteByResourceId(String resourceId) throws Exception{
		String hql="delete R2R as r2r where r2r.resource.id='"+resourceId+"'";
		this.createQuery(hql).executeUpdate();
	}
	
	/**
	 * 级联保存角色的权限
	 * @param role 顶角色
	 * @param resId 资源ID
	 */
	public void cascadeSaveResource4Role(Role role,String resId) {
		R2R r2r = null ;
		r2r = new R2R();
		r2r.setResource(this.findByIds(Resource.class, new String[]{resId}).get(0)) ;
		r2r.setRole(role) ;
		r2r.setInherit(true);
		r2r.setCommunicable(true);
		this.save(r2r);
		if(role.getChildRoles() != null && role.getChildRoles().isEmpty() == false) {
				for (Role childRole : role.getChildRoles()) {
					cascadeSaveResource4Role(childRole, resId);
				}
		}
}
	
}
