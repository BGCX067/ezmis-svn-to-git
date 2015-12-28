package com.jteap.wfengine.workflow.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.hibernate.Session;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springmodules.workflow.jbpm31.JbpmCallback;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.NodeConfigManager;
import com.jteap.wfengine.workflow.manager.NodePermissionManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.NodeConfig;
import com.jteap.wfengine.workflow.model.NodePermission;

@SuppressWarnings({ "unused", "serial","deprecation","unchecked" })
public class WorkflowAction extends AbstractAction {
	
	private JbpmOperateManager jbpmOperateManager;
	private FlowConfigManager flowConfigManager;
	private NodeConfigManager nodeConfigManager;
	private NodePermissionManager nodePermissionManager;
	private PersonManager personManager;
	
	public NodePermissionManager getNodePermissionManager() {
		return nodePermissionManager;
	}

	public void setNodePermissionManager(NodePermissionManager nodePermissionManager) {
		this.nodePermissionManager = nodePermissionManager;
	}

	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}

	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}
	
	public NodeConfigManager getNodeConfigManager() {
		return nodeConfigManager;
	}

	public void setNodeConfigManager(NodeConfigManager nodeConfigManager) {
		this.nodeConfigManager = nodeConfigManager;
	}

	/**
	 * 保存客户端流程定义xml，并将xml中的配置对象保存到数据库
	 *@author  MrBao 
	 *@date 	  2009-7-28
	 *@return
	 *@return String
	 * @throws Exception 
	 *@remark
	 */
	public String uploadProcessDefAction() throws Exception {
		
		String xmlData = request.getParameter("xml");
		String xmlTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xmlData = xmlTitle + xmlData;
	
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlData);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		String userName = this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();
		// 保存配置,将原始xml中的 & 替换成 %26 方便前台读取
		try{
			FlowConfig flowConfig = flowConfigManager.createWorkFlowConfig(document, xmlData.replaceAll("&", "%26"),userName);
			flowConfigManager.save(flowConfig);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new Exception();
		}
		return NONE;
	}
	

	/**
	 * 复制一个全新的流程配置对象
	 * @return
	 */
	public String copyFlowConfigAction(){
		String id=request.getParameter("id");
		if(StringUtil.isNotEmpty(id)){
			FlowConfig fc=flowConfigManager.get(id);
		}
		return null;
	}
	
	/**
	 * 发布流程定义
	 *@author  MrBao 
	 *@date 	  2009-7-28
	 *@return
	 *@return String
	 * @throws Exception 
	 *@remark
	 */
	public String deployWorkFlowAction() throws Exception{
		
		String flowConfigId = request.getParameter("flowConfigId");
		
		FlowConfig flowConfig =  flowConfigManager.get(flowConfigId);
		
		
		
		Document document = null;
		try {
			if(flowConfig.getPd_id()!=null){
				throw new Exception("流程已经发布过了，不能重复发布已发布的流程");
			}
			else if(!flowConfig.isValidated()){
				throw new Exception("流程还没有验证");
			}
			
			document = DocumentHelper.parseText(flowConfig.getDefXml());
			// 转换成jbpm的xml文件
			String processDefXml = flowConfigManager.createJbpmProcessDefXml(document, flowConfig.getNm());
			log.info("流程部署："+processDefXml);
			//发布流程,并关联到流程配置对象
			long pd_id=jbpmOperateManager.deployProcessDefinition(processDefXml);

			flowConfig.setPd_id(pd_id);
			flowConfigManager.save(flowConfig);
			
			outputJson("{success:true}");
			
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg=e.getMessage();
			outputJson("{success:false,msg:'"+errMsg+"'}");
			//e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 起草流程实例
	 *@author  MrBao 
	 *@date 	  2009-7-31
	 *@return
	 *@throws Exception
	 *@return  String
	 *@remark
	 */
	public String draftProcessInstanse() throws Exception{
		
		String flowConfigId = request.getParameter("flowConfigId");
		
		FlowConfig flowConfig =  flowConfigManager.get(flowConfigId);
		
		try {
			if(flowConfig.getPd_id()==null){
				throw new Exception("流程还没发布，请先发布流程！");
			}
			String userName = this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
			jbpmOperateManager.createProcessInstance(flowConfig.getNm(), userName);		
			
			request.setAttribute("formId", flowConfig.getFormId());
			request.setAttribute("type", flowConfig.getFormtype());
			request.setAttribute("processPageUrl", flowConfig.getProcess_url());
//			outputJson("{success:true}");
		}catch (Exception e) {
			//String errMsg=e.getMessage();
			e.printStackTrace();
//			outputJson("{success:false,msg:'"+errMsg+"'}");
		}
		return "openForm";
	}
	
	public String goToNextNodeAction(){
		String processInstanceId = request.getParameter("processInstanceId");
		String flowConfigId = request.getParameter("flowConfigId");
		
		
		
		return NONE;
	}
	
	public String deployProcessDefinition(){
		InputStream is = getClass().getResourceAsStream("processdefinition.xml");
		jbpmOperateManager.deployProcessDefinition(is);
		
		return NONE;
	}
		
	public JbpmOperateManager getJbpmOperateManager() {
		return jbpmOperateManager;
	}
	
	public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager) {
		this.jbpmOperateManager = jbpmOperateManager;
	}

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

	public String parseWorkFlowXml(String strXml) {

		return null;
	}
 
	@SuppressWarnings({ "unused", "unchecked" })
	public static void main(String[] args) {
//		InputStream is=WorkflowAction.class.getResourceAsStream("jsonProcess.xml");
//		
//		SAXReader reader = new SAXReader();
//		Document document = null;
//		//Document document = DocumentHelper.parseText(text);
//		try {
//			document = reader.read(is);
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//		//saveWorkFlowConfig(document);
//		//System.out.println(createJbpmProcessDefXml(document,null));  
		Date dt=new Date(1249090740000L);
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
	

}

