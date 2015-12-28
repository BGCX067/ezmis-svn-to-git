package com.jteap.wfengine.workflow.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.wfengine.workflow.model.NodeConfig;
import com.jteap.wfengine.workflow.model.NodeOperation;

/**
 * 结点操作管理器
 *
 * @author 肖平松	
 * @version Oct 19, 2009
 */
public class NodeOperationManager extends HibernateEntityDao<NodeOperation> {

	@SuppressWarnings("unchecked")
	public void jsonSaveToNodeOperation(String strJson,String nodeId) {
		List<Map<String, String>> list = JSONUtil.parseList(strJson);
		for(int i = 0;i < list.size();i++){
			HashMap map = (HashMap) list.get(i);
			
			String name = (String) map.get("name");
			String mark = (String) map.get("mark");
			String show = (String) map.get("show");
			String opPerm_Role = (String) map.get("opPerm_Role");
			String opPerm_Group = (String) map.get("opPerm_Group");
				
			NodeOperation no = new NodeOperation();
			no.setMark(mark);
			no.setName(name);
			no.setShow(show);
			no.setOpPerm_Group(opPerm_Group);
			no.setOpPerm_Role(opPerm_Role);
			NodeConfig nodeConfig = this.get(NodeConfig.class, nodeId);
			no.setNodeConfig(nodeConfig);
			
			this.save(no);
		}
	}

}
