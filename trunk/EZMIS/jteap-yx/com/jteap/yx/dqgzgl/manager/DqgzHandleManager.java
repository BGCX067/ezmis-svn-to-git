/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.manager;

import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;
import com.jteap.yx.dqgzgl.model.DqgzHandle;

/**
 * 定期工作处理Manger
 * @author caihuiwen
 */
public class DqgzHandleManager extends HibernateEntityDao<DqgzHandle>{
	
	private TaskToDoManager taskToDoManager;
	private RoleManager roleManager;
	
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	/**
	 * 发送定期工作处理 并且保存【待办事项】信息
	 * @param dqgzSet
	 */
	public void hanldSend_saveDeals(DqgzHandle dqgzHandle){
		//保存定期工作处理
		this.save(dqgzHandle);
		
		//负责岗位
		String fzgw = dqgzHandle.getFzgw();
		//处理人 (处理人根据业务需求来定)
		String curNodePerson = "";
		
		//获取该负责岗位所有人员的loginName 用","拼接
		Role role = roleManager.findUniqueBy("rolename", fzgw);
		Set<P2Role> p2roleSet = role.getPersons();
		for(P2Role p2Role : p2roleSet){
			curNodePerson += p2Role.getPerson().getUserLoginName() + ",";
		}
		
		TaskToDo taskToDo = taskToDoManager.createDeals(dqgzHandle.getId(), "未完成", "url=/jteap/yx/dqgzgl/handle/handle.jsp", true, "运行管理", "定期工作："+dqgzHandle.getDqgzNr(), curNodePerson, dqgzHandle.getDqgzCreateDt());
		
		//设置定期工作的 待办Id
		dqgzHandle.setTaskId((String)taskToDo.getId());
		//保存定期工作处理
		this.save(dqgzHandle);
	}
	
}
