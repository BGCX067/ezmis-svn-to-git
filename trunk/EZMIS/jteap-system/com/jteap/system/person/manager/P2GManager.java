package com.jteap.system.person.manager;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.OperationManager;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.model.Role;

/**
 * 人员组织管理类
 * @author tantyou
 * @date 2007-12-10
 */
@SuppressWarnings("unchecked")
public class P2GManager extends HibernateEntityDao<P2G>{
	//操作管理对象
	private OperationManager operationManager;
	private P2ResManager p2resManager;
	/**
	 * 向指定组织添加管理员，作为管理员，有自身默认的权限
	 * @param userLoginName
	 * @param groupId
	 */
	public void addAdminToGroup(String userLoginName,String groupId,String indicator)throws Exception{
		String hql="from P2G as p2g where p2g.person.userLoginName=? and p2g.group.id=?";
		List<P2G> list=this.find(hql,userLoginName,groupId);
		P2G p2g=(P2G) list.toArray()[0];
		p2g.setAdmin(true);
		this.save(p2g);
		Person person=this.findUniqueBy(Person.class, "userLoginName", userLoginName);
		//设定管理员默认的权限
		Collection<Resource> ress=operationManager.findAdminOperationList();
		for (Resource res : ress) {
			p2resManager.setupResourceDirect(person, res, indicator);
		}
	}
	
	/**
	 * 移除管理员身份
	 * @param userLoginName
	 * @param groupId
	 * @throws Exception
	 */
	public void removeAdminFromGroup(String userLoginName,String groupId) throws Exception{
		//移除管理员资格
		String hql="from P2G as p2g where p2g.person.userLoginName=? and p2g.group.id=?";
		List<P2G> list=this.find(hql,userLoginName,groupId);
		P2G p2g=(P2G) list.toArray()[0];
		p2g.setAdmin(false);
		this.save(p2g);
		
		//移除管理员默认的权限
		Person person=this.findUniqueBy(Person.class, "userLoginName", userLoginName);
		Collection<Resource> ownRess=p2resManager.findAllDirResourceOfThePerson(person);//当前用户拥有的所有直接资源
		Collection<Resource> ress=operationManager.findAdminOperationList();//所有管理员默认操作
		for (Resource ownRes : ownRess) {
			if(ress.contains(ownRes)){
				p2resManager.removeResource(person, ownRes);
			}
		}
	}
	
	/**
	 * 根据人员和组织删除关联
	 * @param person
	 * @param group
	 * @throws Exception
	 */
	public void deleteByPersonAndGroup(Person person,Group group) throws Exception{
		String hql="delete P2G as p2g where p2g.person=? and p2g.group=?";
		Query query=this.createQuery(hql, person,group);
		query.executeUpdate();
	}

	/**
	 * 移除指定人员与组织之间的关联
	 * @param personId
	 * @param groupId
	 * @throws Exception
	 */
	public void deleteByPersonAndGroup(String personId,String groupId) throws Exception{
		String hql="delete P2G as p2g where p2g.person.id='"+personId+"' and p2g.group.id='"+groupId+"'";
		Query query=this.createQuery(hql);
		query.executeUpdate();
	}
	/**
	 * 
	 * 方法功能描述 :批理为组织指定管理员
	 * @author 唐剑钢
	 * @throws Exception
	 * 2008-2-19
	 * 返回类型：void
	 */
	public boolean batchSetupAdminForGroup(String p2gs[])throws Exception{
		String hql="update	P2G	as p2g set p2g.isAdmin=? where ";
		boolean flag=false;
		for (Serializable id : p2gs) {
			if(flag)
				hql+=" or";
			hql += " p2g.id='" + id + "'";
			flag=true;
		}
		Query query = this.createQuery(hql, new Boolean(true));
		return query.executeUpdate() > 0;
	}
	/**
	 * 
	 * 方法功能描述 :移除指定人员与组织之间的所有关联
	 * @author 唐剑钢
	 * @param p2g adminMap　用于存放是不否是管理员用户
	 * @throws Exception
	 * 2007-12-24
	 * 返回类型：void
	 */
	public HashMap<String, Boolean> deleteByAllPersonAndGroup(Set<P2G> p2g) throws Exception{
		String	p2gIds[]=new String[p2g.size()];
		HashMap<String, Boolean> adminMap=new HashMap<String, Boolean>();
		Iterator<P2G> it=p2g.iterator();
		int i=0;
	    while(it.hasNext()){
	    	P2G pg=it.next();
	    	p2gIds[i]=pg.getId();
	    	if(pg.isAdmin()){
	    		adminMap.put(pg.getGroup().getId().toString(),true);
	    	}
	    	i++;
	    }
	    removeBatch(p2gIds);
	    return adminMap;
	}
	
	/**
	 * 根据指定的参数判断组织中是否已经有人员了
	 * @param groupId		组织编号
	 * @param personId		人员编号
	 * @return	如果存在 返回true 否则false
	 */
	public boolean isExist(String groupId,String personId){
		String hql="from P2G as p2g where p2g.group.id='"+groupId+"' and p2g.person.id='"+personId+"'";
		return this.find(hql).size()>0;
	}
	
	/**
	 * 查询指定用户所存在的所有组织
	 * @param person
	 * @return
	 */
	public Collection<Group> findGroupsOfThePerson(Person person){
		Collection<P2G> p2gs=person.getGroups();
		Collection<Group> result=new ArrayList<Group>();
		for (P2G p2g : p2gs) {
			result.add(p2g.getGroup());
		}
		return result;
	} 
	/**
	 * 查询指定用户所存在的所有组织
	 * @param person
	 * @return
	 */
	public Collection<Group> findGroupsOfThePerson(String personId){
		String hql="from P2G as p2g where p2g.person.id='"+personId+"'";
		Collection<P2G> p2gs=this.find(hql);
		Collection<Group> result=new ArrayList<Group>();
		for (P2G p2g : p2gs) {
			result.add(p2g.getGroup());
		}
		return result;
	}
	
	public void evitPerson(){
		this.getSessionFactory().evict(Person.class);
		this.getSessionFactory().evict(Role.class);
		this.getSessionFactory().evict(Group.class);
		this.getSessionFactory().evictCollection("com.jteap.system.person.model.Person.groups");
		this.getSessionFactory().evictCollection("com.jteap.system.person.model.Person.roles");
		this.getSessionFactory().evictCollection("com.jteap.system.group.model.Group.childGroups");
		this.getSessionFactory().evictQueries();
	}

	public OperationManager getOperationManager() {
		return operationManager;
	}

	public void setOperationManager(OperationManager operationManager) {
		this.operationManager = operationManager;
	}

	public P2ResManager getP2resManager() {
		return p2resManager;
	}

	public void setP2resManager(P2ResManager manager) {
		p2resManager = manager;
	} 
	
	
}
