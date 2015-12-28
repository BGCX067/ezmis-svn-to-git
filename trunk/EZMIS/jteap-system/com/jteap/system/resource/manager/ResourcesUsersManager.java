package com.jteap.system.resource.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.resource.model.ResourcesUsers;

@SuppressWarnings( { "unchecked", "unused", "serial" })
public class ResourcesUsersManager extends HibernateEntityDao<ResourcesUsers> {

	/**
	 * 根据人员与资源找到个性化资源
	 * 
	 * @param res
	 * @param person
	 */
	public ResourcesUsers findByResourceUser(Resource res, Person person) {
		List<ResourcesUsers> list = null;
		String hql = "from ResourcesUsers t where t.resource.id = ? and t.user.id = ?";
		list = this.find(hql, new Object[] { res.getId(), person.getId() });
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据人员找到所有的个性化资源
	 */
	public List<ResourcesUsers> findResourcesUsersByPerson(Person person) {
		System.out.println("2");
		List<ResourcesUsers> list = null;
		String hql = "from ResourcesUsers t where t.user.id=?";
		list = this.find(hql, new Object[] { person.getId() });
		return list;
	}

	/**
	 * 得到当前用户的快速链接资源
	 */
	public List<ResourcesUsers> findQuickLink(Person person) {
		String hql = "from ResourcesUsers t where t.user.id=? and t.isQuickLink='1'";
		List<ResourcesUsers> result = this.find(hql, new Object[] { person
				.getId() });
		return result;
	}
}
