package com.jteap.system.resource.web;

import org.hibernate.SessionFactory;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.ModuleManager;
import com.jteap.system.resource.manager.ResourceManager;
import com.jteap.system.resource.manager.ResourcesUsersManager;
import com.jteap.system.resource.model.Module;
import com.jteap.system.resource.model.Operation;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.resource.model.ResourcesUsers;

@SuppressWarnings( { "unchecked", "unused", "serial" })
public class ResourcesUsersAction extends AbstractAction {

	// 模块管理对象
	private ModuleManager moduleManager;
	// 资源管理器
	private ResourceManager resManager;

	private ResourcesUsersManager resourcesUsersManager;
	private PersonManager personManager;
	

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public void setModuleManager(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
	}

	public ResourceManager getResManager() {
		return resManager;
	}

	public void setResManager(ResourceManager resManager) {
		this.resManager = resManager;
	}

	public ResourcesUsersManager getResourcesUsersManager() {
		return resourcesUsersManager;
	}

	public void setResourcesUsersManager(
			ResourcesUsersManager resourcesUsersManager) {
		this.resourcesUsersManager = resourcesUsersManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return resourcesUsersManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	/**
	 * 系统模块名称个性化保存
	 */
	public String updateSysModuleAction() throws BusinessException {
		try {
			String resName = request.getParameter("resName"); // 模块名称
			String id = request.getParameter("resourceId"); // 编号

			Resource resource = resManager.get(id);
			Module module = moduleManager.get(id);

			// 在资源人员表中查找是否有对应记录
			ResourcesUsers resourcesUsers = resourcesUsersManager
					.findByResourceUser(resource, personManager.getCurrentPerson(sessionAttrs));
			// 如果有，修改其模块名称
			if (null != resourcesUsers) {
				resourcesUsers.setNewName(resName);
			} else {
				// 没有，新建一条记录
				resourcesUsers = new ResourcesUsers();
				resourcesUsers.setResource(resource);
				resourcesUsers.setUser(personManager.getCurrentPerson(sessionAttrs));
				resourcesUsers.setNewName(resName);
				resourcesUsers.setIsQuickLink('0');
			}
			resourcesUsersManager.save(resourcesUsers);

			// 清除二级缓存
			clearHibernateL2CacheAction();
			// 保存
			this.outputJson("{success:true}");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	}

	/**
	 * 恢复系统默认值
	 * 
	 * @throws Exception
	 */
	public String resetAction() throws Exception {
		try {
			String resourceId = request.getParameter("resourceId");

			// 得到当前资源
			Resource res = resManager.get(resourceId);
			Person person = personManager.getCurrentPerson(sessionAttrs);

			// 查找个性化资源
			ResourcesUsers resUser = resourcesUsersManager.findByResourceUser(
					res, person);
			// 如果有个性化资源
			if (resUser != null) {
				// 没有快速链接设置，则删除个性化资源
				if(resUser.getIsQuickLink() == '0') {
					resourcesUsersManager.remove(resUser);
				} else {
					// 有快速链接设置，个性化名字设置为空
					resUser.setNewName("");
					resourcesUsersManager.save(resUser);
				}
			}

			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException();
		}
		return NONE;
	}

	/**
	 * 添加批量快速链接
	 */
	public String setupQuickLinkAction() {
		try {
			// 当前用户
			Person currentPerson = personManager.getCurrentPerson(sessionAttrs);
			// 勾选的模块
			String linkids = request.getParameter("linkids");
			String resouceIds[] = linkids.split(",");
			// 原来的模块
			String oldIds = request.getParameter("oldIds");

			// 遍历勾选的模块
			for (String resId : resouceIds) {
				if (StringUtil.isNotEmpty(resId)) {
					Resource res = resManager.get(resId);
					// 如果不是父模块
					if (res.getParentRes() != null) {
						ResourcesUsers resUser = resourcesUsersManager
								.findByResourceUser(res, currentPerson);
						// 修改
						if (resUser != null) {
							resUser.setIsQuickLink('1');
							// 新增
						} else {
							resUser = new ResourcesUsers();
							resUser.setIsQuickLink('1');
							resUser.setResource(res);
							resUser.setUser(currentPerson);
						}
						resourcesUsersManager.save(resUser);
					}
					// 在原来勾选的模块中删除现在勾选的模块
					oldIds = delChecked(oldIds, resId);
				}
			}
			// 原来勾选的模块中剩余没被勾选的
			String strOldIds[] = oldIds.split(",");

			// 遍历没被勾选的原模块
			for (String oldId : strOldIds) {
				if (StringUtil.isNotEmpty(oldId)) {
					Resource res = resManager.get(oldId);
					ResourcesUsers resUser = resourcesUsersManager
							.findByResourceUser(res, currentPerson);
					// 如果没有新资源名，则删除该记录
					if (StringUtil.isEmpty(resUser.getNewName())) {
						resourcesUsersManager.remove(resUser);
						// 如果有新资源名，则将此记录设为非快速链接
					} else {
						resUser.setIsQuickLink('0');
						resourcesUsersManager.save(resUser);
					}
				}
			}

			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 在原勾选模块中删除现在勾选的模块
	 * 
	 * @param oldIds
	 * @param resId
	 */
	private String delChecked(String oldIds, String resId) {
		if(StringUtil.isEmpty(oldIds)){
			return "";
		}
		int index = oldIds.indexOf(resId);
		if (index >= 0) {
			oldIds = oldIds.substring(0, index) + oldIds.substring(index + 32);
		}
		return oldIds;
	}

	/**
	 * 删除二级缓存
	 */
	/**
	 * 清空hibernate二级缓存
	 * 
	 * @return
	 * @throws Exception
	 */
	private void clearHibernateL2CacheAction() throws Exception {
		sessionFactory.evict(Resource.class);
		sessionFactory.evict(Module.class);
		sessionFactory.evict(Operation.class);
		sessionFactory
				.evictCollection("com.jteap.system.resource.model.Resource.childRes");
		sessionFactory.evictQueries();
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
}
