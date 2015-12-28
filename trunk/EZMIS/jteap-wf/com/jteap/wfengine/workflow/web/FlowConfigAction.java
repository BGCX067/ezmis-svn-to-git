package com.jteap.wfengine.workflow.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLResult;
import org.dom4j.io.XMLWriter;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wfengine.workflow.manager.FlowCatalogManager;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.model.FlowConfig;

@SuppressWarnings({ "unused", "serial","deprecation","unchecked" })
public class FlowConfigAction extends AbstractAction {
	
	//选择导入的xml文件
	private File xmlFile ;
	
	//FlowConfig类对应的Manager
	private FlowConfigManager flowConfigManager ;
	
	//FlowCatalog类对应的Manager
	private FlowCatalogManager flowCatalogManager ;
	
	private JbpmOperateManager jbpmOperateManager ;
	
	
	/**
	 * 根据Id获得指定FlowConfig的流程XML
	 */
	public String getFlowConfigXmlAction() throws Exception {
		FlowConfig flowConfig = flowConfigManager.get(request.getParameter("id"));
		response.setContentType("text/xml;charset=utf-8");
		PrintWriter out = getOut();
		out.print(flowConfig.getDefXml());
//		this.outputJson(flowConfig.getDefXml());
		
//		System.out.println(flowConfig.getDefXml());
		return NONE ;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String exportFlowConfigXmlAction() throws Exception {
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("导出数据_"+System.currentTimeMillis()+".xml").getBytes(), "iso-8859-1"));

		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FlowConfig flowConfig = flowConfigManager.get(request.getParameter("id"));
		try {
			bis = new BufferedInputStream(new ByteArrayInputStream(flowConfig.getDefXml().getBytes()));
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bos != null)
				bos.close();
		}
		return NONE ;
	}
	
	/**
	 *  导入xml文件，生成流程
	 */
	public String importFlowConfigFromXmlAction() throws Exception {
		try {
			File file = getXmlFile();
			FileInputStream fileInputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(fileInputStream, "gb2312");
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(reader);
			Element eRoot = document.getRootElement();
			String xmlData = document.asXML();
			Element root = eRoot.element("root");
			Element Workflow = root.element("Workflow");
			Workflow.setAttributeValue("flowId", "");
			Workflow.setAttributeValue("flowName", Workflow
					.attributeValue("flowName")
					+ "导入");
			String jsonWorkflow = Workflow.attributeValue("workflow");
			JSONObject jsonObj = JSONObject.fromObject(jsonWorkflow);
			jsonObj.put("name", jsonObj.get("name").toString() + "导入");
			String jsonStr = jsonObj.toString();
			Workflow.setAttributeValue("workflow", jsonStr
					.replaceAll("\"", "'"));
			xmlData = eRoot.asXML();
			Document d = null;
			try {
				d = DocumentHelper.parseText(xmlData);
			} catch (DocumentException ex) {
				ex.printStackTrace();
			}
			String userName = this.sessionAttrs.get(
					Constants.SESSION_CURRENT_PERSON_NAME).toString();
			// 保存配置,将原始xml中的 & 替换成 %26 方便前台读取
			FlowConfig flowConfig = flowConfigManager.createWorkFlowConfig(d,
					xmlData, userName);
			flowConfigManager.save(flowConfig);
			request.setAttribute("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("result", false);
		}
		return "upload_result";
	}
	
	/**
	 * 版本回退
	 */
	public String moveBackAction() throws Exception {
		String ids = request.getParameter("ids") ;
		if (ids != null) {
			flowConfigManager.removeBatch(ids.split(","),jbpmOperateManager,true) ;
			getOut().print("{success:true}");
		}
		return NONE ;
	}
	
	/**
	 * 批量删除流程配置
	 */
	public String removeAction() throws Exception {
		String ids = request.getParameter("ids") ;
		if (ids != null) {
			String configids = getFlowConfigIds(ids);
			flowConfigManager.removeBatch(configids.split(","),jbpmOperateManager,false);
			getOut().print("{success:true}");
		}
		return NONE ;
	}

	
	
	private String getFlowConfigIds(String ids) {
		// TODO Auto-generated method stub
		String[] id = ids.split(",");
		String newid = "";
		for(int i = 0;i < id.length;i++){
			FlowConfig flowConfig = flowConfigManager.get(id[i]);
			List<FlowConfig> fc = flowConfigManager.findBy("nm",flowConfig.getNm());
			for(int j = 0;j < fc.size();j++){
				newid += fc.get(j).getId() + ",";
			}
		}

		newid = newid.substring(0, newid.length() - 1);
		return newid;
	}
	
	public String validateFlowAction() throws BusinessException{
		try{
		String id = request.getParameter("id") ;
		
		FlowConfig fc = flowConfigManager.get(id);
		
		ValidateWorkFlow vwf = new ValidateWorkFlow(fc.getDefXml());
		
		String msg = vwf.validate();
		if(msg.equals(ValidateWorkFlow.VALIDATE_SUCCESS)){
			//更新数据库
			fc.setValidated(true);
			flowConfigManager.save(fc);
			flowConfigManager.flush();
			getOut().print("{success:true,msg:'" + msg + "'}");
		}else{
			getOut().print("{success:false,msg:'" + msg + "'}");
		}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	};
	
	public String copyFlowAction() throws Exception {
		String id = request.getParameter("id") ;
		FlowConfig fc = flowConfigManager.get(id);

		String xmlData = fc.getDefXml();
		Document document = DocumentHelper.parseText(xmlData);
		Element e = document.getRootElement();

		Element root = e.element("root");
		Element Workflow = root.element("Workflow");
		
		Workflow.setAttributeValue("flowId", "");
		
		Workflow.setAttributeValue("flowName", Workflow.attributeValue("flowName") + "复制");
		
		String jsonWorkflow = Workflow.attributeValue("workflow");
		JSONObject jsonObj=JSONObject.fromObject(jsonWorkflow);
		jsonObj.put("name",jsonObj.get("name").toString() + "复制");

		String jsonStr = jsonObj.toString();
		Workflow.setAttributeValue("workflow", jsonStr.replaceAll("\"", "'"));
		
		xmlData = e.asXML();
		Document d = null;
		try {
			d = DocumentHelper.parseText(xmlData);
		} catch (DocumentException ex) {
			ex.printStackTrace();
		}
		String userName = this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();
		// 保存配置,将原始xml中的 & 替换成 %26 方便前台读取
		FlowConfig flowConfig = flowConfigManager.createWorkFlowConfig(d, xmlData,userName);
		flowConfigManager.save(flowConfig);
		
		getOut().print("{success:true,msg:'流程复制成功'}");
		
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 根据流程内码获得最新的流程配置，并向前台输出流程配置ID
	 * 作者 : wangyun
	 * 时间 : 2010-10-14
	 * 异常 : Exception
	 */
	public String getFlowConfigIdByNm() throws Exception {
		String flowConfigNm = request.getParameter("flowConfigNm");
		FlowConfig flowConfig = flowConfigManager.getNewFlowConfigByNm(flowConfigNm);
		if (flowConfig != null) {
			outputJson("{success:true,flowConfigId :'"+flowConfig.getId()+"'}");
		} else {
			outputJson("{success:false,msg:'你选择的流程不存在，请联系管理员！'}");
		}
		return NONE;
	}


	/**
	 * showList之前的扩展方法
	 */
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		HqlUtil.addCondition(hql, "newVer", '1');
		// 添加查询分类条件
		String catalogId = request.getParameter("catalogId");
		if (StringUtils.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "flowCatalog.id", catalogId);
		}
		// 默认按照创建时间倒序排序
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "createrTime", "desc");
		}
		
	}

	public HibernateEntityDao getManager() {
		return flowConfigManager;
	}

	/**
	 * 所生成json的属性
	 */
	public String[] listJsonProperties() {
		return new String[]{"id","name","pd_id","creater","createrTime","version","description","time","type","process_url","formtype","validated"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}

	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}

	public FlowCatalogManager getFlowCatalogManager() {
		return flowCatalogManager;
	}

	public void setFlowCatalogManager(FlowCatalogManager flowCatalogManager) {
		this.flowCatalogManager = flowCatalogManager;
	}


	public File getXmlFile() {
		return xmlFile;
	}


	public void setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
	}


	public JbpmOperateManager getJbpmOperateManager() {
		return jbpmOperateManager;
	}


	public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager) {
		this.jbpmOperateManager = jbpmOperateManager;
	}

}
