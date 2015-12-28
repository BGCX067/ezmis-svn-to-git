package com.jteap.system.group.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.Person;

/**
 * 组织管理器
 * 
 * @author tanchang
 * 
 */
@SuppressWarnings("unchecked")
public class GroupManager extends HibernateEntityDao<Group> {

	/**
	 * 清除集合缓存，主要在添加组织是使用
	 */
	public void evitGroups() {
		this.getSessionFactory().evict(Group.class);
		this.getSessionFactory().evictCollection("com.jteap.system.person.model.Person.groups");
		this.getSessionFactory().evictCollection("com.jteap.system.group.model.Group.childGroups");
		this.getSessionFactory().evictQueries();
	}



	/**
	 * 根据父亲节点编号查询群组
	 * 
	 * @param parentId
	 * @return
	 */
	public Collection<Group> findGroupByParentId(String parentId) {
		String hql = "from Group as g where g.parentGroup.id='" + parentId
				+ "' order by g.sortNo";
		return this.find(hql);
	}

	@SuppressWarnings("unchecked")
	public Collection<P2G> findAdminPersons(String groupId) {
		String hql = "from P2G as p2g where p2g.isAdmin='1' and p2g.group.id='"
				+ groupId + "'";
		return this.find(hql);
	}

	/**
	 * 
	 * 方法功能描述 :根据指定的人员找这个人员所拥有的组织
	 * 
	 * @author 唐剑钢
	 * @param person
	 * @return 2008-1-26 返回类型：Collection<Group>
	 */
	public Collection<Group> findGroupByPerson(Person person) {
		ArrayList<Group> al = new ArrayList<Group>();
		Set<P2G> p2g = person.getGroups();
		for (P2G pg : p2g) {
			Group group = pg.getGroup();
			al.add(group);
		}
		return al;
	}

	/**
	 * 根据组织名称查找组织
	 * 
	 * @param cn
	 * @return
	 */
	public Group findGroupByName(String cn) {
		String hql = "from Group as g where g.groupName='" + cn + "'";
		List<Group> lstGroup = this.find(hql);
		if (lstGroup.size() > 0) {
			return lstGroup.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 根据班组名称(热工一班)查找所属部门(检修部)
	 */
	public Group findGroupByBzName(String bzName){
		String hql = "from Group as gp where gp.groupName like '%办公室' and gp.parentGroup.id in (select g.id from Group as g where g.id in (select t.parentGroup.id from Group as t where t.groupName='"+bzName+"'))";
		List<Group> lstGroup = this.find(hql);
		if (lstGroup.size() > 0) {
			return lstGroup.get(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * 取得根组织集合
	 * 
	 * @return
	 */
	public Collection<Group> findRootGroups() {
		String hql = "from Group as a where a.parentGroup is null order by a.sortNo";
		List<Group> roots = this.find(hql);
		return roots; 
	}
	
//
//	/**
//	 * 查询指定用户管理哪些组织的集合
//	 * @param personId
//	 * @return
//	 */
//	public Collection<Group> findAdminGroupsOfThePerson(String personId){
//		String hql="from P2G as p2g where p2g.person.id='"+personId+"' and p2g.isAdmin='1'";
//		List<Group> roots = new ArrayList<Group>();
//		_findAllGroupsInHashTable();
//		Collection<P2G> p2gs=this.find(hql);
//		List<Group> permGroupList = new ArrayList<Group> ();
//		
//		for (P2G p2g : p2gs) {
//			Group group = p2g.getGroup();
//			permGroupList.add(group);
//		}
//		
//		for (Group group : permGroupList) {
//			_process(roots,permGroupList,group);
//		}
//		
//		return roots;
//	}
	
//	private void _process(List<Group> roots,List<Group> permGroupList,Group g){
//		Group parent = g.getParentGroup();
//		
//		if(parent!=null){
//			List<Group> tmp = new ArrayList<Group>();
//			for (Group childG : parent.getChildGroups()) {
//				if(!permGroupList.contains(childG)){
//					tmp.add(childG);
//				}
//			}
//			for (Group group : tmp) {
//				parent.getChildGroups().remove(group);
//			}
//			_process(roots,permGroupList,parent);
//		}else{
//			roots.add(g);
//		}
//	}
	
//	
//	/**
//	 * 递归调用，将当前组织对象放入hashtable
//	 * @param ht
//	 * @param g
//	 */
//	private void _pushGroupToTopInHashTable(Hashtable<String,Group> ht,Group g,List<Group> roots){
//		ht.put(g.getId().toString(), g);
//		String parentId = g.getParentId();
//		if(StringUtil.isNotEmpty(parentId)){
//			Group parentGroup = this.get(parentId);
//			if(parentGroup!=null){
//				_pushGroupToTopInHashTable(ht,parentGroup,roots);
//			}
//		}else{
//			roots.add(g);
//		}
//	}

//	/**
//	 * 找到所有群组对象，并放入集合
//	 * @return
//	 */
//	public Hashtable<String,Group> _findAllGroupsInHashTable() {
//		Hashtable<String, Group> ht = new Hashtable<String, Group>();
//		String hql = "from Group as a order by a.sortNo";
//		List<Group> roots = new ArrayList<Group>();
//		List<Group> groups = this.find(hql);
//		List<Group>  childGroups = new ArrayList<Group>();
//		for (Group g : groups) {
//			String pgId = g.getParentId();
//			if(StringUtil.isNotEmpty(pgId)){
//				childGroups.add(g);
//			}else{
//				roots.add(g);
//			}
//			ht.put(g.getId().toString(),g);
//		}
//		for (Group cg : childGroups) {
//			Group pg = ht.get(cg.getParentId());
//			if(pg!=null){
//				cg.setParentGroup(pg);
//				pg.getChildGroups().add(cg);
//			}
//		}
//		return ht; 
//	}
	

}
