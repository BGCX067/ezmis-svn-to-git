package com.jteap.system.resource.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.person.manager.P2ResManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.model.R2R;
import com.jteap.system.role.model.Role;

/**
 * 资源管理器
 * @author Ugen 
 * 2008-1-31
 */
@SuppressWarnings("unchecked")
public class ResourceManager extends HibernateEntityDao<Resource> {

	private P2ResManager p2resManager;

	/**
	 * 取得根资源,包括操作
	 * 2008-1-16
	 * Ugen
	 * @return
	 */
	public Collection<Resource> findRootResource(){
		String hsql = "from Resource as m where m.parentRes=null order by m.sortNo";
		return this.find(hsql);
	}

	/**
	 * 
	 * 方法功能描述 :通过指定的用户找这个用户的资源，只有可见资源
	 * @author 唐剑钢
	 * @param person
	 * @return
	 * 2008-1-27
	 * 返回类型：Collection<Resource>
	 */
	public Collection<Resource> findResourceByPerson(Person person)throws Exception {
		ArrayList<Resource> al = new ArrayList<Resource>();
		Set<P2Role> roleSet = person.getRoles();
		// 找到角色集合
		for (P2Role p2r : roleSet) {
			Role role = p2r.getRole();
			// 找到资源集合
			Set<R2R> r2rSet = role.getResources();
			for (R2R r2r : r2rSet) {
				Resource resource = r2r.getResource();
				if (resource.isVisiabled())// 只有可见资源
					al.add(resource);
			}
		}
		// 所有直接资源
		Collection<Resource> dirRes = p2resManager.findAllDirResourceOfThePerson(person);
		for (Resource resource : dirRes) {
			if(!al.contains(resource)){
				al.add(resource);
			}
		}

		return al;
	}
	

	/**
	 * 查询所有可见的系统资源
	 * @return
	 */
	public Collection<Resource> findAllVisibledResource(){
		String hql="from Resource as res where res.visiabled='1' and res.resStyle='0'";
		return find(hql);
	}

	public P2ResManager getP2resManager() {
		return p2resManager;
	}

	public void setP2resManager(P2ResManager manager) {
		p2resManager = manager;
	}
	
	
	
}

