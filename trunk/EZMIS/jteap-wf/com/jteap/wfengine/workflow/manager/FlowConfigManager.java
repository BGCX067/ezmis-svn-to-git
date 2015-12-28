package com.jteap.wfengine.workflow.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.form.cform.model.CForm;
import com.jteap.wfengine.workflow.model.FlowCatalog;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.model.Variable;
import com.jteap.wfengine.workflow.util.WFConstants;

/**
 * 
 * @filename FlowConfigManager.java
 * @author MrBao
 * @date 2009-7-28
 * @remark
 */
@SuppressWarnings( { "unused", "serial", "deprecation", "unchecked" })
public class FlowConfigManager extends HibernateEntityDao<FlowConfig> {

	private NodeConfigManager nodeConfigManager;
	private NodeOperationManager nodeOperationManager;
	
	public NodeConfigManager getNodeConfigManager() {
		return nodeConfigManager;
	}

	public void setNodeConfigManager(NodeConfigManager nodeConfigManager) {
		this.nodeConfigManager = nodeConfigManager;
	}

	public boolean addFlowConfig() {

		return true;
	}

	/**
	 * 根据流程配置内码获取最新版本号
	 * 
	 * @author MrBao
	 * @date 2009-7-9
	 * @param workFlowName
	 * @return
	 * @return int
	 * @remark
	 */
	public int getNewVersion(String strNm) {

		int verNumber = 1;
		StringBuffer hql = new StringBuffer(
				"select max(f.version) from FlowConfig as f where ");
		Object args[] = null;

		if (StringUtils.isEmpty(strNm)) {
			hql.append("f.nm is null");
		} else {
			hql.append("f.nm =?");
			args = new String[] { strNm };
		}
		Number nVer = (Number) createQuery(hql.toString(), args).uniqueResult();
		if (nVer != null) {
			verNumber = nVer.intValue() + 1;
		}
		return verNumber;
	}

	/**
	 * 
	 * @param strNm
	 * @return
	 */
	public FlowConfig getNewVersionConfig(String strNm, int version) {
		String hql = "from FlowConfig as f where f.nm = ? and f.version<? order by f.version desc";
		List list = this.find(hql, new Object[] { strNm, version });
		FlowConfig result = null;
		if (list.size() > 0)
			result = (FlowConfig) list.get(0);
		return result;

	}

	/**
	 * 根据内码和版本获取唯一流程配置对象
	 * 
	 * @author MrBao
	 * @date 2009-7-28
	 * @param version
	 * @param strNm
	 * @return
	 * @return FlowConfig
	 * @remark
	 */
	public FlowConfig getFlowConfigByNmAndVersion(int version, String strNm) {

		StringBuffer hql = new StringBuffer(
				" from FlowConfig as f where f.version =? and f.nm=? ");
		Object args[] = new Object[2];
		args[0] = version;
		args[1] = strNm;

		FlowConfig flowConfig = (FlowConfig) createQuery(hql.toString(), args)
				.uniqueResult();

		return flowConfig;
	}

	/**
	 * 保存json化的流程配置对象
	 * 
	 * @author MrBao
	 * @date 2009-7-28
	 * 参数 ：
	 * 		strJson ： WorkFlow节点中workflow属性
	 * 		strNm ： 内码
	 * 		strVersion ： 版本号
	 * 		strXML ： 流程xml定义
	 * 		userName ： 创建人名称
	 * 		isNewVer ： 是否新版本
	 * 		topicCF ： 主题
	 */

	public FlowConfig jsonWriteToFlowConfig(String strJson, String strNm,
			int strVersion, String strXML, String userName, boolean isNewVer,
			String topicCF) throws Exception {

		try {
			JSONObject jsonObj = JSONObject.fromObject(strJson);
			
			Map map = new HashMap();
			map.put("flowCatalog", FlowCatalog.class);
//		map.put("cform", CForm.class);
			map.put("flowVariables", Variable.class);
			// map.put("flowOperations", FlowOperation.class);
			
			// 将json字符串中的属性值转换到对象
			FlowConfig flowConfig = (FlowConfig) JSONObject.toBean(jsonObj,
					FlowConfig.class, map);
			
			// 得到分类对象并设置到缓存中的，防止重复保存
			FlowCatalog flowCatalog = get(FlowCatalog.class, flowConfig
					.getFlowCatalog().getId());
			flowConfig.setFlowCatalog(flowCatalog);
			
			// 得到表单对象并设置缓存中的，防止重复保存
//		if (flowConfig.getCform().getId() != null) {
//			CForm cform = get(CForm.class, flowConfig.getCform().getId());
//			flowConfig.setCform(cform);
//		} else {
//			flowConfig.setCform(null);
//		}
			
			// //操作对象关联到流程配置对象
			// for(FlowOperation fo : flowConfig.getFlowOperations()){
			// fo.setFlowConfig(flowConfig);
			// }
			
			// 流程变量对象关联到流程配置对象
			for (Variable variable : flowConfig.getFlowVariables()) {
				variable.setFlowConfig(flowConfig);
			}
			// 设置版本
			if (strVersion == 0) {// 新建版本
				flowConfig.setVersion(getNewVersion(strNm));
			} else {// 保持原来版本
				flowConfig.setVersion(strVersion);
			}
			// 如果内码为空，则生成一个新内码
			if (strNm == null) {
				strNm = UUIDGenerator.hibernateUUID();
			}
			flowConfig.setNm(strNm);
			// 设置流程配置xml
			flowConfig.setDefXml(strXML);
			flowConfig.setCreaterTime(new Date());
			
			// 设置创建人
			flowConfig.setCreater(userName);
			flowConfig.setNewVer(isNewVer);
			// save(flowConfig);
			
			flowConfig.setTopicCF(topicCF);
			return flowConfig;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据客户端流程xmlDocument，转换成jbpm 流程定义xml
	 * 
	 * @author MrBao
	 * @date 2009-6-19
	 * @param srcDoc
	 *            客户端流程Document
	 * @return
	 * @return String
	 * @remark
	 */
	public String createJbpmProcessDefXml(Document srcDoc, String workFlowNm) {

		Collection<Element> al = new ArrayList<Element>();
		/** 建立document对象 */
		Document document = DocumentHelper.createDocument();

		/** 建立XML文档的根process-definition */
		// List workflowList = srcDoc.selectNodes("//Workflow");
		// Element el = (Element) workflowList.get(0);
		// String workflowName = el.attributeValue("flowName");
		Element rootElement = document.addElement("process-definition",
				"urn:jbpm.org:jpdl-3.2");

		// 此处流程名称为流程配置内码
		rootElement.addAttribute("name", workFlowNm);

		// 加入流程开始事件，初始化流程系统变量
		addWorkFlowEvent("process-start",
				WFConstants.WORKFLOW_PROCESS_START_CLASS, rootElement);

		/** 建立流程开始任务节点 */
		List<Element> startNodeList = srcDoc.selectNodes("//start-state");
		Element startElement = rootElement.addElement("start-state");
		// 获取mxCline定义的流程开始任务节点元素
		Element mxStartElement = startNodeList.get(0);
		startElement
				.addAttribute("name", mxStartElement.attributeValue("name"));
		al.add(mxStartElement);

		/** 建立流程结束任务节点 */
		// List<Element> endNodeList = srcDoc.selectNodes("//End-state");
		// Element endElement = rootElement.addElement("end-state");
		// endElement.addAttribute("name",
		// endNodeList.get(0).attributeValue("节点名称"));
		// al.add(endElement);
		/** 递归添加其他元素节点 */
		addNode(rootElement, startElement, mxStartElement, al, document, srcDoc);

		return document.asXML();
	}

	/**
	 * 获取跳转元素集合
	 * 
	 * @author MrBao
	 * @date 2009-6-19
	 * @param id
	 * @param document
	 * @return
	 * @return List<Element>
	 * @remark
	 */
	public List<Element> findEdgeElementById(String id, Document document) {
		List<Element> composites = document
				.selectNodes("//Edge/mxCell[@source='" + id + "']");
		return composites;
	}

	/**
	 * 添加jbpm节点元素
	 * 
	 * @author MrBao
	 * @date 2009-6-19
	 * @param parentE
	 *            新建xml的root元素
	 * @param currentE
	 *            当前新建的root元素
	 * @param srcE
	 *            当前取的客户端流程定义中的元素
	 * @param al
	 *            客户端流程定义中已经添加的元素集合
	 * @param document
	 *            文档对象
	 * @param srcDoc
	 *            客户端流程定义文档对象
	 * @return void
	 * @remark
	 */
	public void addNode(Element parentE, Element currentE, Element srcE,
			Collection<Element> al, Document document, Document srcDoc) {

		String currNodeId = srcE.attributeValue("id");
		List<Element> edgeList = findEdgeElementById(currNodeId, srcDoc);
		// 排序默认路由
		sortDefalutTransition(edgeList);
		for (Element e : edgeList) {
			Element eTransition = currentE.addElement("transition");
			eTransition.addAttribute("name", e.getParent().attributeValue(
					"connName"));
			String strCondition = e.getParent().attributeValue("connCondition");

			if (StringUtil.isNotEmpty(strCondition)) {
				strCondition = StringUtil.decodeChars(strCondition,"',\",&,<,>");
				// eTransition.addAttribute("condition", strCondition);
				Element eCondition = eTransition.addElement("condition");
				eCondition
						.addAttribute("expression", "#{" + strCondition + "}");
			}

			// 获取下一个task节点
			List<Element> nextNodeList = srcDoc.selectNodes("//*[@id='"
					+ e.attributeValue("target") + "']");
			Element nextNode = null;
			if (nextNodeList.size() > 0) {
				nextNode = nextNodeList.get(0);
				eTransition.addAttribute("to", nextNode.attributeValue("name"));
			}
			// eTransition.addText("");
			// 如果节点已经添加或者本路径已经到达结束节点
			if (nextNode == null || al.contains(nextNode)) {
				continue;
			} else {
				Element nextParentNode = null;
				if (nextNode.getName().equals("task")) {
					// 添加任务节点
					nextParentNode = parentE.addElement("task-node");
					nextParentNode.addAttribute("name", nextNode.attributeValue("name"));

					// 添加任务
					Element el = nextParentNode.addElement(nextNode.getName());
					el.addAttribute("name", nextNode.attributeValue("name"));

					// 添加任务参与者节点
					Element taskEl = el.addElement("assignment");
					String actorsEl = nextNode.attributeValue("taskPower");
					if (StringUtil.isNotEmpty(actorsEl)) {
						actorsEl = StringUtil.replace(actorsEl, "'", "\\'");
					}
					taskEl.addAttribute("pooled-actors", "#{mytag:actors('" + actorsEl + "')}");

					//添加节点事件和脚本
                    String strJsonEvent = nextNode.attributeValue("event");
                    if(StringUtil.isNotEmpty(strJsonEvent)){
						 addTaskEvent(strJsonEvent,nextParentNode);
					}
                    
					// //给每个任务节点添加初始化事件
					// addWorkFlowEvent("task-create",
					// Constants.WORKFLOW_TASK_START_CLASS, el);
					//					
					// //给每个任务节点添加结束事件
					// addWorkFlowEvent("task-end",
					// Constants.WORKFLOW_TASK_END_CLASS, el);

					

					// 处理会签
					String processKind = nextNode.attributeValue("processKind");// 获取节点处理类别，是单人模式，还是多人模式
					// String processMode =
					// nextNode.attributeValue("processMode");//会签模式，串行还是，并行
					// signal="last-wait" create-tasks="false" end-tasks="true"
					if (processKind.equals(WFConstants.WORKFLOW_PORCESS_KIND_MULTI)) {
						nextParentNode.addAttribute("signal", "last-wait");
						nextParentNode.addAttribute("create-tasks", "false");
						nextParentNode.addAttribute("end-tasks", "true");

						addCountersignEvent(nextParentNode);
					}

				} else {// 添加非任务节点
					nextParentNode = parentE.addElement(nextNode.getName());
					nextParentNode.addAttribute("name", nextNode
							.attributeValue("name"));
				}
				// 放入以添加集合
				al.add(nextNode);

				/** 递归添加其他元素节点 */
				addNode(parentE, nextParentNode, nextNode, al, document, srcDoc);
			}
		}
	}

	/**
	 * 添加节点事件
	 * 
	 * @author MrBao
	 * @date 2009-7-23
	 * @param jsonEvent
	 *            json格式的事件数组
	 * @param taskNodeEl
	 *            任务节点元素对象
	 * @return void
	 * @remark
	 */
	public void addTaskEvent(String jsonEvent, Element taskNodeEl) {
		JSONArray jsonArray = JSONArray.fromObject(jsonEvent);
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			Element eventEl = null;

			String eventName = JSONUtil.getPropertyValueByJsonObject(object
					.toString(), "eventName");
			String eventProcessClass = JSONUtil.getPropertyValueByJsonObject(
					object.toString(), "proceClass");

			if (StringUtil.isNotEmpty(eventName)
					&& StringUtil.isNotEmpty(eventProcessClass)) {
				// 添加event节点
				eventEl = taskNodeEl.addElement("event");

				eventEl.addAttribute("type", eventName);
				// 添加action节点
				Element actionEl = eventEl.addElement("action");
				actionEl.addAttribute("name", eventName);
				actionEl.addAttribute("class", eventProcessClass);
			} else {
				if ("node-enter".equals(eventName)) {
			        // 给每个任务节点添加初始化事件
					addWorkFlowEvent("node-enter",
							WFConstants.WORKFLOW_INIT_NODE_CLASS,
							taskNodeEl);
				}
				if ("node-leave".equals(eventName)) {
					// 给每个任务节点添加初始化事件
					addWorkFlowEvent("node-leave",
							WFConstants.WORKFLOW_LEAVE_NODE_CLASS,
							taskNodeEl);
				}
			}

			// 添加脚本节点
			String strScript = JSONUtil.getPropertyValueByJsonObject(object.toString(), "proceScript");
			if (StringUtil.isNotEmpty(strScript)) {
				if (eventEl == null) {
					// 添加event节点
					eventEl = taskNodeEl.addElement("event");
				}
				Element scriptEl = eventEl.addElement("script");
				scriptEl.addText(strScript);
			}
		}
	}

	/**
	 * 添加流程事件
	 * 
	 * @author MrBao
	 * @date 2009-7-27
	 * @param eventName
	 * @param processClass
	 * @param el
	 * @return void
	 * @remark
	 */
	public void addWorkFlowEvent(String eventName, String processClass,
			Element el) {

		Element eventEl = el.addElement("event");
		eventEl.addAttribute("type", eventName);
		// 添加action节点
		Element actionEl = eventEl.addElement("action");
		actionEl.addAttribute("name", eventName);
		actionEl.addAttribute("class", processClass);
	}

	/**
	 * 添加会签事件处理
	 * 
	 * @author MrBao
	 * @date 2009-7-23
	 * @return void
	 * @remark
	 */
	public void addCountersignEvent(Element taskNodeEl) {
		// 添加新增任务实例event节点
		// addWorkFlowEvent("node-enter",
		// Constants.WORKFLOW_CREATE_TASKINSTANCE_CLASS, taskNodeEl);

		// 添加任务实例结束event
		addWorkFlowEvent("task-end", WFConstants.WORKFLOW_COUNTERSIGN_CLASS,
				taskNodeEl);
	}

	/**
	 * 排序路由元素，使缺省路由放在第一个位置
	 * 
	 * @author MrBao
	 * @date 2009-7-24
	 * @param edgeList
	 * @return void
	 * @remark
	 */
	public void sortDefalutTransition(List<Element> edgeList) {
		Element tempEl = null;
		if (edgeList.size() > 0)
			tempEl = edgeList.get(0);
		for (int i = 0; i < edgeList.size(); i++) {
			// 如果是默认跳转对象，则放在第一个位置
			if (edgeList.get(i).getParent().attributeValue("chkIsDefault")
					.equals("1")) {
				edgeList.set(0, edgeList.get(i));
				edgeList.set(i, tempEl);
				break;
			}
		}
	}

	/**
	 * 更新流程配置对象
	 * 
	 * @author MrBao
	 * @date 2009-7-24
	 * 参数 ： 
	 * 		srcDoc ： xml文件转换的Document对象
	 * 		mxXmlData ： xml文件
	 */
	public FlowConfig createWorkFlowConfig(Document srcDoc, String mxXmlData,
			String userName) throws Exception {

		try {
			Collection<Element> al = new ArrayList<Element>();
			System.out.println(mxXmlData);
			/** 获取流程配置节点，并保存流程配置对象 */
			List workflowList = srcDoc.selectNodes("//Workflow");
			Element el = (Element) workflowList.get(0);
			String strJsonFlowConfig = el.attributeValue("workflow");
			String topicCF = el.attributeValue("topicCF");
			
//		HashMap map = new HashMap();
//		String flowVariable = el.attributeValue("flowVariable");
//		JSONArray jsonArray = JSONArray.fromObject(flowVariable);
//		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
//			Object object = (Object) iterator.next();
//			Element eventEl = null;
//
//			String name = JSONUtil.getPropertyValueByJsonObject(object
//					.toString(), "name");
//			String value = JSONUtil.getPropertyValueByJsonObject(object
//					.toString(), "value");
//			if (name != null) {
//				map.put("${" + name + "}", value);
//			}
//		}
			
			// topicCF = this.eval(topicCF,map);
			
			String workFlowId = el.attributeValue("flowId");
			
			// 删除先前旧版本流程配置对象
			FlowConfig flowConfig = null;
			
			if (StringUtil.isNotEmpty(workFlowId)) {
				flowConfig = get(workFlowId);
			}
			
			// 新建流程配置
			if (flowConfig == null) {
				flowConfig = jsonWriteToFlowConfig(strJsonFlowConfig, null, 0,
						mxXmlData, userName, true, topicCF);
				// 修改未发布的流程配置
			} else if (flowConfig != null && flowConfig.getPd_id() == null) {
				int strVer = flowConfig.getVersion();
				String strNm = flowConfig.getNm();
				// 先删除
				remove(flowConfig);
				// 保存新的流程配置对象,保持原有内码和版本号
				flowConfig = jsonWriteToFlowConfig(strJsonFlowConfig, strNm,
						strVer, mxXmlData, userName, true, topicCF);
				// 对于已发布的流程配置，创建新版本的流程配置
			} else if (flowConfig != null && flowConfig.getPd_id() != null) {
				FlowConfig old = flowConfig;
				old.setNewVer(false);
				save(old);
				
				flowConfig = jsonWriteToFlowConfig(strJsonFlowConfig, flowConfig
						.getNm(), 0, mxXmlData, userName, true, topicCF);
			}
			
			// 获取mxCline定义的流程开始任务节点元素
			List<Element> startNodeList = srcDoc.selectNodes("//start-state");
			if (startNodeList.size() > 0) {
				Element mxStartElement = startNodeList.get(0);
				al.add(mxStartElement);
				
				/** 递归保存节点配置对象 */
				createNodeConfig(mxStartElement, al, srcDoc, flowConfig);
			}
			return flowConfig;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 保存节点配置对象
	 * 
	 * @author MrBao
	 * @date 2009-7-24
	 * 参数 ：
	 * 		srcE ： 现在节点
	 * 		al ： 储存节点的集合
	 * 		strDoc ： xml文件转换的Document对象
	 * 		flowConfig ： 流程配置
	 */
	public void createNodeConfig(Element srcE, Collection<Element> al,
			Document srcDoc, FlowConfig flowConfig) {

		String currNodeId = srcE.attributeValue("id");
		// 得到所有源于现在处理节点的路由
		List<Element> edgeList = findEdgeElementById(currNodeId, srcDoc);
		// 遍历路由
		for (Element e : edgeList) {
			// 获取此路由的目标节点
			List<Element> nextNodeList = srcDoc.selectNodes("//*[@id='"
					+ e.attributeValue("target") + "']");
			Element nextNode = null;
			if (nextNodeList.size() > 0) {
				nextNode = nextNodeList.get(0);
			}
			// 如果节点已经添加或者本路径已经到达结束节点
			if (nextNode == null || al.contains(nextNode)) {
				continue;
			} else {
				Element nextParentNode = null;
				if (nextNode.getName().equals("task")) {
					// 保存节点配置对象
					String strJsonNodeConfig = nextNode.attributeValue("task");
					String strJsonNodeOperate = nextNode.attributeValue("taskOperate");
					strJsonNodeConfig = strJsonNodeConfig.replaceAll("%26quot;", "\"");
					String nodeId = nodeConfigManager.jsonSaveToNodeConfig(strJsonNodeConfig, flowConfig);                       
					nodeOperationManager.jsonSaveToNodeOperation(strJsonNodeOperate,nodeId);
				}
				// 放入以添加集合
				al.add(nextNode);

				/** 递归保存其他节点配置对象 */
				createNodeConfig(nextNode, al, srcDoc, flowConfig);
			}
		}
	}

	/**
	 * @Title: 			removeBatch
	 * @author 			MrBao
	 * @date 				2010-3-19 下午03:40:12
	 * @Description: 	批量删除流程配置
	 * @param keys
	 * @param jbpmOperateManager
	 * @param setNewVersion
	 * @return			void
	 * @throws
	 */
	public void removeBatch(Serializable[] keys,
			JbpmOperateManager jbpmOperateManager, boolean setNewVersion) {
		for (int i = 0; i < keys.length; i++) {
			FlowConfig flowConfig = (FlowConfig) get(keys[i]);
			if (flowConfig.getPd_id() != null) {
				jbpmOperateManager.deleteProcessDefinition(flowConfig
						.getPd_id());
			}
			getHibernateTemplate().delete(flowConfig);

			if (setNewVersion) {
				if (flowConfig.getVersion() > 1) {
					flowConfig = this.getNewVersionConfig(flowConfig.getNm(),
							flowConfig.getVersion());
					flowConfig.setNewVer(true);
					save(flowConfig);
				}
			}

		}
	}

	/**
	 * 根据流程实例编号得到流程配置对象
	 * 
	 * @param pd_id
	 * @return
	 */
	public FlowConfig findFlowConfigByPDID(Long pd_id) {
		String hql = "from FlowConfig as f where f.pd_id = ?";
		List<FlowConfig> list = this.find(hql, new Object[] { pd_id });
		FlowConfig result = null;
		if (list.size() > 0)
			result = list.get(0);
		return result;
	}

	/**
	 * 
	 * 描述 : 根据流程名称取出最新版本的流程配置
	 * 作者 : wangyun
	 * 时间 : Jul 22, 2010
	 * 异常 :
	 */
	public FlowConfig getNewFlowConfigByName(String flowName) {
		String hql = "from FlowConfig t where t.name=? and t.newVer='1'";
		List<FlowConfig> lstFlowConfigs = this.find(hql, new Object[] {flowName});
		if (lstFlowConfigs.size() > 0) {
			return lstFlowConfigs.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * 描述 : 根据流程内码，获得最新版本的流程配置
	 * 作者 : wangyun
	 * 时间 : 2010-10-14
	 * 参数 : 
	 * 		strNm ： 流程内码
	 * 返回值 : FlowConfig
	 */
	public FlowConfig getNewFlowConfigByNm(String strNm) {
		String hql = "from FlowConfig t where t.nm = ? and t.newVer='1'";
		List<FlowConfig> lstFlowConfigs = this.find(hql, new Object[] {strNm});
		if (lstFlowConfigs.size() > 0) {
			return lstFlowConfigs.get(0);
		}
		return null;
	}
	
	public NodeOperationManager getNodeOperationManager() {
		return nodeOperationManager;
	}

	public void setNodeOperationManager(NodeOperationManager nodeOperationManager) {
		this.nodeOperationManager = nodeOperationManager;
	}

}
