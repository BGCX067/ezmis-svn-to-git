package com.jteap.wfengine.workflow.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.NodeConfig;
import com.jteap.wfengine.workflow.model.NodePermission;
import com.jteap.wfengine.workflow.model.NodeVariable;
import com.jteap.wfengine.workflow.model.Variable;

/**
 * 节点配置管理器
 * @author tanchang
 *
 */
@SuppressWarnings("unchecked")
public class NodeConfigManager extends HibernateEntityDao<NodeConfig> {

	/**
	 * 根据json字符串保存对应的节点配置对象
	 * @param strJson
	 * @param flowConfig
	 */
	public String jsonSaveToNodeConfig(String strJson, FlowConfig flowConfig){		
		
		//json字符串转换成json对象
		JSONObject jsonObj=JSONObject.fromObject(strJson);
		
		Map map = new HashMap();
		map.put("nodeVariables",NodeVariable.class);
//		map.put("taskOperations", NodeOperation.class);
		map.put("nodePermission:", NodePermission.class);
		
		//将json字符串中的属性值转换到对象
		@SuppressWarnings("unused")
		NodeConfig nodeConfig = (NodeConfig) JSONObject.toBean(jsonObj, NodeConfig.class,map);
		
		//得到流程配置对象，关联到节点配置对象
		nodeConfig.setFlowConfig(flowConfig);
		
		//得到流程变量对象，关联到节点变量对象
		for(NodeVariable nb : nodeConfig.getNodeVariables()){
			Set<Variable> flowVariables = flowConfig.getFlowVariables();
			for (Variable var : flowVariables) {
				if(var.getName().equals(nb.getVariable().getName())){
					nb.setVariable(var);
				}
			}
			
//			StringBuffer hql=new StringBuffer("from Variable as v where v.name=? and v.flowConfig.id =?");
//			Object args[]=new Object[2];
//			args[0]= nb.getVariable().getName();
//			args[1]= flowConfig.getId();
//			Variable variable = (Variable)createQuery(hql.toString(), args).uniqueResult();
//			nb.setVariable(variable);
			nb.setNodeConfig(nodeConfig);
		}
//		//得到流程操作对象，关联到节点配置对象
//		for(FlowOperation fo : nodeConfig.getFlowOperations()){
//			StringBuffer hql = new StringBuffer("from FlowOperation as f where f.name=? and f.flowConfig.id =?");
//			Object args[] = new Object[2];
//			args[0] = fo.getName();
//			args[1] = flowConfig.getId();
//			FlowOperation flowOperation = (FlowOperation)createQuery(hql.toString(), args).uniqueResult();
//			fo = flowOperation	;
//		}
		
		save(nodeConfig);
		return nodeConfig.getId();
	}
	
	/**
	 * 根据节点名称查询节点配置对象
	 * @param flowConfigId	流程配置编号
	 * @param nodeName		节点配置名称
	 * @return
	 */
	
	public NodeConfig findNodeConfigByName(String flowConfigId,String nodeName){
		String hql = "from NodeConfig n where n.flowConfig.id = ? and n.name = ?";
		List<NodeConfig> list = this.find(hql, new Object[]{flowConfigId,nodeName});
		NodeConfig result = null;
		if(list.size()>0)
			result = list.get(0);
		return result;
		
	}
	
	public static void main(String[] args) {
		String xx = "{'name':'起草','description':null,'process_url':null,'flowConfig':{'name':'第一种工作票'},'nodeVariables':[{'variable':{'name':'PiaoHao','name_cn':'票号','kind':'表单变量'},'isNeed':'可编辑','value':''},{'variable':{'name':'Peoinfo','name_cn':'工作班人员信息','kind':'表单变量'},'isNeed':'可编辑','value':''},{'variable':{'name':'Peonum','name_cn':'工作班人数','kind':'表单变量'},'isNeed':'可编辑','value':''},{'variable':{'name':'Fuzeren','name_cn':'工作负责人','kind':'表单变量'},'isNeed':'可编辑','value':''},{'variable':{'name':'DeptName','name_cn':'部门名称','kind':'表单变量'},'isNeed':'可编辑','value':''},{'variable':{'name':'Diaozihao','name_cn':'调字号','kind':'表单变量'},'isNeed':'可编辑','value':''}],'flowOperations':[],'nodePermission':{'name':'褚福行','processMode':'1','processKind':'single','isChooseActor':false,'isOneProcessActor':true,'expression':'[{'persons':'0415','groups':null,'roles':null,'is_self_dept':false,'is_pcreator':false,'is_up_dept':false,'up_node_person':false,'name':'褚福行'}]'}}";
		JSONObject obj = JSONObject.fromObject(xx);
		System.out.println(obj);
	}
}
