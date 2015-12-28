package com.jteap.wfengine.workflow.web;

import java.util.ArrayList;
import java.util.HashMap;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wfengine.workflow.manager.NodeConfigManager;
import com.jteap.wfengine.workflow.manager.NodeOperationManager;
import com.jteap.wfengine.workflow.util.WFConstants;

@SuppressWarnings("serial")
public class NodeOperationAction extends AbstractAction {

	NodeOperationManager nodeOperationManager;
	NodeConfigManager nodeConfigManager;
	
	public NodeOperationManager getNodeOperationManager() {
		return nodeOperationManager;
	}

	public void setNodeOperationManager(NodeOperationManager nodeOperationManager) {
		this.nodeOperationManager = nodeOperationManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String getNodeOperation() throws Exception{
		String name = SystemConfig.getProperty("WORKFLOW.OPS2");
		String mark = SystemConfig.getProperty("WORKFLOW.OPS1");
		String[] names = name.split(",");
		String[] marks = mark.split(",");
		String json = "";
		//配置文件中的环节操作
		ArrayList arrList = new ArrayList();
		for(int i = 0;i < names.length;i++){
			HashMap map = new HashMap();
			map.put("name", names[i]);
			map.put("mark", marks[i]);
			map.put("show", WFConstants.WF_NODE_SHOW);
			map.put("opPerm_Role", "");
			map.put("opPerm_Group", "");
			arrList.add(map);
		}
		String jsonList = JSONUtil.listToJson(arrList, new String[]{"name","mark","show","opPerm_Role","opPerm_Group"});
		json = "{'totalCount':" + names.length + ",list:" + jsonList + "}";
		this.outputJson(json);
		
		return NONE;
	}

	public NodeConfigManager getNodeConfigManager() {
		return nodeConfigManager;
	}

	public void setNodeConfigManager(NodeConfigManager nodeConfigManager) {
		this.nodeConfigManager = nodeConfigManager;
	}

}
