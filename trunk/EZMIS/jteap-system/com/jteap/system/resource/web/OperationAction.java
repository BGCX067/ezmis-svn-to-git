package com.jteap.system.resource.web;



import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.resource.manager.ModuleManager;
import com.jteap.system.resource.manager.OperationManager;
import com.jteap.system.resource.manager.ResourceManager;
import com.jteap.system.resource.model.Module;
import com.jteap.system.resource.model.Operation;


/**
 * 操作动作
 * @author Ugen
 * 2008-1-18
 */

@SuppressWarnings({"serial"})
public class OperationAction extends AbstractAction{
	
	//操作管理对象
	private OperationManager operationManager;
	
	//资源管理对象
	private ResourceManager resManager;
	
	//模块管理对象
	private ModuleManager moduleManager;

	
	/**
	 * 获取指定资源的操作列表
	 * Ugen
	 * 2008-1-19
	 * @return
	 * @throws Exception
	 */
	public String showOperationListAction() throws Exception{
		String resId = request.getParameter("resourceId");
		
		if (StringUtils.isNotEmpty(resId)){
			Module mod = moduleManager.get(resId);
			Collection<Operation> op = moduleManager.findAllOperationOfTheModule(mod);

			String[] field2 = {"style","action","beforeScript","resName"};
			
			StringBuffer dataBlock=new StringBuffer();
			dataBlock.append("{totalCount:'"+op.size()+"',list:"+JSONUtil.listToJson(op,field2)+"}"); 
			//this.outputJson(this.listToJson(op,field2));
			this.outputJson(dataBlock.toString());
			log.info(JSONUtil.listToJson(op,field2));
		}
		return NONE;
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

	public OperationManager getOperationManager() {
		return operationManager;
	}

	public void setOperationManager(OperationManager operationManager) {
		this.operationManager = operationManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		
		return operationManager;
	}

	@Override
	public String[] listJsonProperties() {
		
		return new String []{"name","beforeScript","style","action"};
	}

	@Override
	public String[] updateJsonProperties() {
		
		return null;
	}

}
