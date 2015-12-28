package com.jteap.wfengine.workflow.manager;

import java.util.ArrayList;
import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.NodeConfig;
import com.jteap.wfengine.workflow.model.NodePermission;

public class NodePermissionManager extends HibernateEntityDao<NodePermission> {
	
	private FlowConfigManager flowConfigManager;
	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}
	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}
	@SuppressWarnings("unchecked")
	public ArrayList getNodePermission(String flowConfigId,String nodesname){
		FlowConfig fc = flowConfigManager.get(flowConfigId);
		Set<NodeConfig> ncSet = fc.getNodeConfigs();
		ArrayList npList = new ArrayList();
		String[] nodenameArr = nodesname.split("~");
		for(int i = 0;i < nodenameArr.length;i++){
			for(NodeConfig nc : ncSet){
				if(nc.getName().equals(nodenameArr[i])){
					npList.add(nc.getNodePermission());
				}
			}
		}
		
		return npList;
	}


	
}
