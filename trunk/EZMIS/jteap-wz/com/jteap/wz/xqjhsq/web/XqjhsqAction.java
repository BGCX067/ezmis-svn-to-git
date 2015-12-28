/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.xqjhsq.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.person.manager.P2RoleManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.model.Role;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.util.WFConstants;
import com.jteap.wz.xqjhsq.manager.XqjhsqJDBCManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class XqjhsqAction extends AbstractAction {

	private XqjhsqManager xqjhsqManager;
	private XqjhsqJDBCManager xqjhsqJDBCManager;

	private DictManager dictManager;
	private DataSource dataSource;
	private PersonManager personManager;
	private P2RoleManager p2roleManager;
	private RoleManager roleManager;
	private JbpmOperateManager jbpmOperateManager;
	private WorkFlowLogManager workFlowLogManager;
	private TaskToDoManager taskToDoManager;

	public TaskToDoManager getTaskToDoManager() {
		return taskToDoManager;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	public WorkFlowLogManager getWorkFlowLogManager() {
		return workFlowLogManager;
	}

	public void setWorkFlowLogManager(WorkFlowLogManager workFlowLogManager) {
		this.workFlowLogManager = workFlowLogManager;
	}

	public JbpmOperateManager getJbpmOperateManager() {
		return jbpmOperateManager;
	}

	public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager) {
		this.jbpmOperateManager = jbpmOperateManager;
	}

	public XqjhsqJDBCManager getXqjhsqJDBCManager() {
		return xqjhsqJDBCManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public void setXqjhsqJDBCManager(XqjhsqJDBCManager xqjhsqJDBCManager) {
		this.xqjhsqJDBCManager = xqjhsqJDBCManager;
	}

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	/**
	 * 
	 * 描述 : 查看待处理物资申请 作者 : leichi
	 */
	public String showDclXqjhAction() throws Exception {
		// // 取得流程数据字典
		// String lcz = dictManager
		// .findDictByCatalogNameWithKey("WZXQJHSQ", "流程中").getValue();
		//
		// String userLoginName = sessionAttrs.get(
		// Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		//		
		// try {
		// StringBuffer sbSql = new StringBuffer();
		// sbSql.append("select a.FLOW_TOPIC, ");
		// sbSql.append("a.ID as TASKTODOID, ");
		// sbSql.append("a.FLOW_NAME, ");
		// sbSql.append("a.FLOW_INSTANCE_ID, ");
		// sbSql.append("a.CURRENT_TASKNAME, ");
		// sbSql.append("a.POST_PERSON, ");
		// sbSql
		// .append("to_char(a.POST_TIME,'yyyy-MM-dd HH:mi:ss') as POST_TIME, ");
		// sbSql.append("a.TOKEN, ");
		// sbSql.append("a.CURSIGNIN, ");
		// sbSql.append("b.*, ");
		// sbSql.append("d.personname as curSignInName ");
		// sbSql.append("from tb_wf_todo a, ");
		// sbSql.append("tb_wz_xqjhsq b, ");
		// sbSql.append("jbpm_variableinstance c, ");
		// sbSql.append("tb_sys_person d ");
		// sbSql.append("where a.current_process_person = '");
		// sbSql.append(userLoginName);
		// sbSql.append("' ");
		// sbSql.append("and a.flag = '1' ");
		// sbSql.append("and a.flow_instance_id = c.processinstance_ ");
		// sbSql.append("and b.id = c.stringvalue_ ");
		// sbSql.append("and c.name_='docid' ");
		// sbSql.append("and b.status = '" + lcz + "' ");
		// sbSql.append("and d.id(+) = a.cursignin ");
		// String sql = sbSql.toString();
		// Page page = this.getPage(sql);

		/** ************************ */
		String XQJHQF = request.getParameter("XQJHQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);

		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// 角色（专业）过滤
		// String userRole =
		// sessionAttrs.get(Constants.SESSION_CURRENT_PERSONROLES).toString();
		// String[] userRoles = userRole.split(",");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.FLOW_TOPIC, ");
			sbSql.append("a.ID as TASKTODOID, ");
			sbSql.append("a.FLOW_NAME, ");
			sbSql.append("a.FLOW_INSTANCE_ID, ");
			sbSql.append("a.CURRENT_TASKNAME, ");
			sbSql.append("a.POST_PERSON, ");
			sbSql.append("to_char(a.POST_TIME,'yyyy-MM-dd HH:mi:ss') as POST_TIME, ");
			sbSql.append("a.TOKEN, ");
			sbSql.append("a.CURSIGNIN, ");
			sbSql.append("b.*, ");
			sbSql.append("d.personname as curSignInName ");
			sbSql.append("from tb_wf_todo a, ");
			sbSql.append("tb_wz_xqjhsq b, ");
			sbSql.append("jbpm_variableinstance c, ");
			sbSql.append("tb_sys_person d ");
			sbSql.append("where a.current_process_person like '%");
			sbSql.append(userLoginName);
			sbSql.append("%' ");
			sbSql.append("and a.flag = '1'");
			sbSql.append("and a.flow_instance_id = c.processinstance_ ");
			sbSql.append("and b.id = c.stringvalue_ ");
			sbSql.append("and c.name_='docid' ");
			sbSql.append("and b.flow_status != '已作废' ");
			sbSql.append("and b.XQJHQF = '" + XQJHQF + "' ");
			sbSql.append("and d.id(+) = a.cursignin order by b.xqjhsqbh desc");
			String sql = sbSql.toString();
			
			Page page = xqjhsqJDBCManager.pagedQueryTableData(sql, iStart,
					iLimit);
			/** ************************ */

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
							"FLOW_INSTANCE_ID", "CURRENT_TASKNAME",
							"POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN",
							"ID", "XQJHSQBH", "GCLB", "GCXM", "SQBM", "SQSJ",
							"CZY", "CZYXM", "time", "STATUS", "C_LCBH",
							"GCXMBH", "C_FLAG", "FPSJ", "SQBMMC",
							"FLOW_STATUS", "CURSIGNINNAME" });
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 查看已处理需求计划 作者 : leichi
	 */
	public String showYclXqjhAction() throws Exception {

		// String limit = request.getParameter("limit");
		// if (StringUtils.isEmpty(limit))
		// limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
		//
		// // 开始索引
		// String start = request.getParameter("start");
		// if (StringUtils.isEmpty(start))
		// start = "0";
		//
		// int iStart = Integer.parseInt(start);
		// int iLimit = Integer.parseInt(limit);
		//	
		// String userLoginName = sessionAttrs.get(
		// Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// try {
		// StringBuffer sbSql = new StringBuffer();
		// sbSql.append("select a.*,");
		// sbSql.append("b.processinstance_,");
		// sbSql.append("c.id_,");
		// sbSql.append("c.version_,");
		// sbSql.append("c.start_,");
		// sbSql.append("c.end_,");
		// sbSql.append("d.flow_name,");
		// sbSql.append("d.id as FLOW_CONFIG_ID,");
		// sbSql.append("d.flow_form_id ");
		// sbSql.append("from tb_wz_xqjhsq a,");
		// sbSql.append("jbpm_variableinstance b,");
		// sbSql.append("jbpm_processinstance c,");
		// sbSql.append("tb_wf_flowconfig d,");
		// sbSql.append("tb_wf_log e ");
		// sbSql.append("where b.name_ = 'docid' ");
		// sbSql.append("and b.stringvalue_ = a.id ");
		// sbSql.append("and b.processinstance_ = c.id_ ");
		// sbSql.append("and c.processdefinition_ = d.pd_id ");
		// sbSql.append("and e.pi_id = c.id_ ");
		// sbSql.append("and a.status is not null ");
		// sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
		// sbSql.append("task_login_name ='");
		// sbSql.append(userLoginName);
		// sbSql.append("' group by pi_id)");
		// String sql = sbSql.toString();
		// System.out.println(sql);
		// Page page = xqjhsqManager.pagedQueryTableData(sql, iStart, iLimit);

		/** ************************** */
		String XQJHQF = request.getParameter("XQJHQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.xqjhsqbh,a.sqsj,a.sqbmmc,a.czyxm,a.gclb,a.gcxm,a.FLOW_STATUS, ");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from (select * from tb_wz_xqjhsq t "
							+ "where t.flow_status is not null and t.flow_status != '已作废') a ");
			sbSql.append(" inner join jbpm_variableinstance b on b.stringvalue_ = a.id ");
			sbSql.append(" inner join jbpm_processinstance c on b.processinstance_ = c.id_ ");
			sbSql.append(" inner join tb_wf_flowconfig d on c.processdefinition_ = d.pd_id ");
			sbSql.append(" inner join tb_wf_log e on e.pi_id = c.id_ ");
			sbSql.append("where b.name_ = 'docid' ");
//			sbSql.append("and b.stringvalue_ = a.id ");
//			sbSql.append("and b.processinstance_ = c.id_ ");
//			sbSql.append("and c.processdefinition_ = d.pd_id ");
//			sbSql.append("and e.pi_id = c.id_ ");
			// sbSql.append("and a.flow_status is not null ");
			// sbSql.append("and a.flow_status != '已作废' ");
			sbSql.append("and a.XQJHQF = '" + XQJHQF + "' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and not exists ");
			sbSql
					.append("(select 1 from tb_wf_todo f where a.id=docid and f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"
							+ userLoginName + "%') order by a.xqjhsqbh desc");
			String sql = sbSql.toString();
			System.out.println(sql);
			Page page = xqjhsqJDBCManager.pagedQueryTableData2(sql, iStart,
					iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 查看草稿箱 作者 : leichi 异常 : Exception
	 */
	public String showCgxXqjhAction() throws Exception {

		// String userLoginName = sessionAttrs.get(
		// Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// try {
		// StringBuffer sbSql = new StringBuffer();
		// sbSql.append("select a.*,");
		// sbSql.append("b.processinstance_,");
		// sbSql.append("c.id_,");
		// sbSql.append("c.version_,");
		// sbSql.append("c.start_,");
		// sbSql.append("c.end_,");
		// sbSql.append("d.flow_name,");
		// sbSql.append("d.id as FLOW_CONFIG_ID,");
		// sbSql.append("d.flow_form_id ");
		// sbSql.append("from tb_wz_xqjhsq a,");
		// sbSql.append("jbpm_variableinstance b,");
		// sbSql.append("jbpm_processinstance c,");
		// sbSql.append("tb_wf_flowconfig d,");
		// sbSql.append("tb_wf_log e ");
		// sbSql.append("where b.name_ = 'docid' ");
		// sbSql.append("and b.stringvalue_ = a.id ");
		// sbSql.append("and b.processinstance_ = c.id_ ");
		// sbSql.append("and c.processdefinition_ = d.pd_id ");
		// sbSql.append("and e.pi_id = c.id_ ");
		// sbSql.append("and e.task_login_name ='");
		// sbSql.append(userLoginName);
		// sbSql.append("' ");
		// sbSql.append("and a.status is null ");
		// sbSql
		// .append("and e.id in (select max(id) from tb_wf_log group by pi_id)
		// ");
		// String sql = sbSql.toString();
		// Page page = this.getPage(sql);
		// List list = (List) page.getResult();
		// String json = JSONUtil.listToJson(list, listJsonProperties());

		/** ********************* */
		String XQJHQF = request.getParameter("XQJHQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from tb_wz_xqjhsq a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' ");
			sbSql.append("and a.flow_status is null ");
			sbSql.append("and a.XQJHQF = '" + XQJHQF + "' ");
			sbSql
					.append("and e.id in (select max(id) from tb_wf_log group by pi_id) order by a.xqjhsqbh desc");
			String sql = sbSql.toString();
			Page page = xqjhsqJDBCManager.pagedQueryTableData(sql, iStart,
					iLimit);
			/** ********************* */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 查看所有流程 作者 : leichi 异常 : Exception
	 */
	public String showAllListAction() throws Exception {
		// String userLoginName = sessionAttrs.get(
		// Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// try {
		// StringBuffer sbSql = new StringBuffer();
		// sbSql.append("select a.*,");
		// sbSql.append("b.processinstance_,");
		// sbSql.append("c.id_,");
		// sbSql.append("c.version_,");
		// sbSql.append("c.start_,");
		// sbSql.append("c.end_,");
		// sbSql.append("d.flow_name,");
		// sbSql.append("d.id as FLOW_CONFIG_ID,");
		// sbSql.append("d.flow_form_id ");
		// sbSql.append("from tb_wz_xqjhsq a,");
		// sbSql.append("jbpm_variableinstance b,");
		// sbSql.append("jbpm_processinstance c,");
		// sbSql.append("tb_wf_flowconfig d,");
		// sbSql.append("tb_wf_log e ");
		// sbSql.append("where b.name_ = 'docid' ");
		// sbSql.append("and b.stringvalue_ = a.id ");
		// sbSql.append("and b.processinstance_ = c.id_ ");
		// sbSql.append("and c.processdefinition_ = d.pd_id ");
		// sbSql.append("and e.pi_id = c.id_ ");
		// sbSql.append("and e.task_login_name ='");
		// sbSql.append(userLoginName);
		// sbSql.append("' ");
		// // sbSql.append("and a.status is null ");
		// sbSql
		// .append("and e.id in (select max(id) from tb_wf_log group by pi_id)
		// ");
		// String sql = sbSql.toString();
		// System.out.println(sql);
		// Page page = this.getPage(sql);
		// List list = (List) page.getResult();

		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from tb_wz_xqjhsq a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.flow_status is not null ");
			sbSql
					.append("and e.id in (select max(id) from tb_wf_log group by pi_id)");
			String sql = sbSql.toString();
			Page page = xqjhsqJDBCManager.pagedQueryTableData(sql, iStart,
					iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 查看作废 作者 : wangyun 时间 : Jul 19, 2010
	 */
	public String showZfXqjhAction() throws Exception {
		// String yzf = dictManager
		// .findDictByCatalogNameWithKey("WZXQJHSQ", "已作废").getValue();
		// String userLoginName = sessionAttrs.get(
		// Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// try {
		// StringBuffer sbSql = new StringBuffer();
		// sbSql.append("select a.*,");
		// sbSql.append("b.processinstance_,");
		// sbSql.append("c.id_,");
		// sbSql.append("c.version_,");
		// sbSql.append("c.start_,");
		// sbSql.append("c.end_,");
		// sbSql.append("d.flow_name,");
		// sbSql.append("d.id as FLOW_CONFIG_ID,");
		// sbSql.append("d.flow_form_id ");
		// sbSql.append("from tb_wz_xqjhsq a,");
		// sbSql.append("jbpm_variableinstance b,");
		// sbSql.append("jbpm_processinstance c,");
		// sbSql.append("tb_wf_flowconfig d,");
		// sbSql.append("tb_wf_log e ");
		// sbSql.append("where b.name_ = 'docid' ");
		// sbSql.append("and b.stringvalue_ = a.id ");
		// sbSql.append("and b.processinstance_ = c.id_ ");
		// sbSql.append("and c.processdefinition_ = d.pd_id ");
		// sbSql.append("and e.pi_id = c.id_ ");
		// sbSql.append("and a.status ='" + yzf + "' ");
		// sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
		// sbSql.append("task_login_name ='");
		// sbSql.append(userLoginName);
		// sbSql.append("' group by pi_id)");
		// String sql = sbSql.toString();
		// Page page = this.getPage(sql);

		/** ************************** */
		String XQJHQF = request.getParameter("XQJHQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from tb_wz_xqjhsq a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.flow_status ='已作废' ");
			sbSql.append("and a.XQJHQF = '" + XQJHQF + "' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) order by a.xqjhsqbh desc");
			String sql = sbSql.toString();
			Page page = xqjhsqJDBCManager.pagedQueryTableData(sql, iStart,
					iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 描述 : 需求计划申请作废 作者 : leichi 异常 : Exception
	 */
	public String cancelXqjhAction() throws Exception {
		String id = request.getParameter("id");
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update tb_wz_xqjhsq t set t.flow_status = '已作废' where t.id ='"
					+ id + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.close();
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}

	/**
	 * 描述 : 导出Excel物资需求计划申请单 作者 : leichi 异常 : Exception
	 */
	@SuppressWarnings("deprecation")
	public void exportXqjhsqAction() throws Exception {
		String path = request.getSession().getServletContext().getRealPath(
				"/jteap/wz/wzxqjhsq/wzxqjhsq.xls");
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
		}
		try {
			byte[] data = null;
			HSSFWorkbook wb = null;
			File excel = new File(path);
			InputStream is = new FileInputStream(excel);
			wb = new HSSFWorkbook(is);
			HSSFSheet rs = wb.getSheetAt(0);
			HSSFCellStyle cs = wb.createCellStyle();// 创建一个style
			HSSFFont littleFont = wb.createFont();// 创建一个Font
			littleFont.setFontName("SimSun");
			littleFont.setFontHeightInPoints((short) 10);
			cs.setFont(littleFont);// 设置字体
			cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居中
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			Xqjhsq xqjhsq = (Xqjhsq) this.getManager().get(id);
			Set<XqjhsqDetail> xqjhsqDetail = xqjhsq.getXqjhsqDetail();
			int rowIndex = 5;
			rs.addMergedRegion(new Region(1, (short) 2, 1, (short) 3));
			rs.addMergedRegion(new Region(1, (short) 5, 1, (short) 7));
			rs.addMergedRegion(new Region(1, (short) 9, 1, (short) 10));
			rs.addMergedRegion(new Region(2, (short) 2, 2, (short) 3));
			rs.addMergedRegion(new Region(2, (short) 5, 2, (short) 7));
			rs.addMergedRegion(new Region(2, (short) 9, 2, (short) 10));
			// 编号
			rs.getRow(1).getCell(2).setCellValue(xqjhsq.getXqjhsqbh());
			// 申请部门
			HSSFCell sqbm = rs.getRow(1).getCell(5);
			// sqbm.setEncoding(HSSFCell.ENCODING_UTF_16);
			sqbm.setCellValue(xqjhsq.getSqbmmc());
			// 申请时间
			if (xqjhsq.getSqsj() != null)
				rs.getRow(1).getCell(9).setCellValue(
						DateUtils.formatDate(xqjhsq.getSqsj(), "yyyy-MM-dd"));
			// 工程类别
			rs.getRow(2).getCell(2).setCellValue(xqjhsq.getGclb());
			// 工程项目
			HSSFCell gcxm = rs.getRow(2).getCell(5);
			// gcxm.setEncoding(HSSFCell.ENCODING_UTF_16);
			gcxm.setCellValue(xqjhsq.getGcxm());
			// 操作员
			HSSFCell czymc = rs.getRow(2).getCell(9);
			// czymc.setEncoding(HSSFCell.ENCODING_UTF_16);
			czymc.setCellValue(xqjhsq.getCzyxm());
			int i = rowIndex;
			for (Iterator<XqjhsqDetail> iterator = xqjhsqDetail.iterator(); iterator
					.hasNext();) {
				XqjhsqDetail detail = (XqjhsqDetail) iterator.next();
				rs.addMergedRegion(new Region(rowIndex, (short) 0, rowIndex,
						(short) 0)); // 指定合并区域
				rs.addMergedRegion(new Region(rowIndex, (short) 1, rowIndex,
						(short) 3));
				rs.addMergedRegion(new Region(rowIndex, (short) 4, rowIndex,
						(short) 5));
				rs.addMergedRegion(new Region(rowIndex, (short) 9, rowIndex,
						(short) 10));
				// 第一列 序号
				HSSFCell ce = rs.getRow(rowIndex).createCell(0);
				// HSSFCell ce1 = rs.getRow(rowIndex+1).createCell(0);
				ce.setCellStyle(cs);
				ce.setCellValue(rowIndex - i + 1);
				// 第二列 物资名称
				HSSFCell wzmc = rs.getRow(rowIndex).getCell(1);
				wzmc.setCellStyle(cs);
				// wzmc.setEncoding(HSSFCell.ENCODING_UTF_16);
				wzmc.setCellValue(detail.getWzmc());
				// 第三列 型号规格
				HSSFCell xhgg = rs.getRow(rowIndex).getCell(4);
				xhgg.setCellStyle(cs);
				// xhgg.setEncoding(HSSFCell.ENCODING_UTF_16);
				xhgg.setCellValue(detail.getXhgg());
				// rs.getRow(rowIndex).getCell( 4).setCellStyle(cs);
				// 第五列 申请数量
				HSSFCell sqsl = rs.getRow(rowIndex).getCell(6);
				if (detail.getSqsl() != null)
					sqsl.setCellValue(detail.getSqsl());
				sqsl.setCellStyle(cs);
				// rs.getRow(rowIndex).getCell( 6).setCellStyle(cs);
				// 第四列 计量单位
				HSSFCell jldw = rs.getRow(rowIndex).getCell(7);
				// jldw.setEncoding(HSSFCell.ENCODING_UTF_16);
				jldw.setCellValue(detail.getJldw());
				jldw.setCellStyle(cs);

				// 第六列 估计单价
				HSSFCell gjdj = rs.getRow(rowIndex).getCell(8);
				gjdj.setCellValue(detail.getGjdj());
				gjdj.setCellStyle(cs);

				// 第七列 需用日期

				HSSFCell xyrq = rs.getRow(rowIndex).createCell(9);
				if (detail.getXysj() != null)
					xyrq.setCellValue(DateUtils.formatDate(detail.getXysj(),
							"yyyy-MM-dd"));
				rs.getRow(rowIndex).getCell(9).setCellStyle(cs);

				rowIndex++;
			}

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			data = os.toByteArray();

			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(("物资需求计划申请单_"
							+ DateUtils.getDate("yyyy-MM-dd") + ".xls")
							.getBytes(), "iso-8859-1"));

			// 开始输出
			java.io.BufferedOutputStream outs = new java.io.BufferedOutputStream(
					response.getOutputStream());
			outs.write(data);
			outs.flush();
			outs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 描述 : 查看待分配需求计划 作者 : leichi
	 */
	public String showDfpXqjhAction() throws Exception {
		// // 取得流程数据字典
		// String ypz = dictManager
		// .findDictByCatalogNameWithKey("WZXQJHSQ", "已批准").getValue();
		boolean flag = false;
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		Role role = roleManager.findRoleByRoleName("需求申请分配");
		String hql = "from P2Role as p2r where p2r.role.id='"+role.getId()+"'";
		List<P2Role> list = new ArrayList<P2Role>();
		list = p2roleManager.find(hql);
		for (int i = 0; i < list.size(); i++) {
			P2Role p2r = (P2Role) list.get(i);
			Person person = personManager.get(p2r.getPerson().getId());
			if (person.getUserLoginName().equals(userLoginName)) {
				flag = true;
				break;
			}
		}
		try {
			if(flag || ("root").equals(userLoginName)){
				StringBuffer sbSql = new StringBuffer();
				sbSql.append("select c.* from tb_wz_xqjhsq c where c.id in (select distinct(a.id) ");
				sbSql.append("from tb_wz_xqjhsq a, ");
				sbSql.append("tb_wz_xqjhsq_detail b ");
				sbSql.append("where a.id = b.xqjhsqid ");
				sbSql.append("and a.flow_status = '已完成' ");
				sbSql.append("and a.status = '1' ");
				sbSql.append("and b.c_flag = '0' ");
				sbSql.append("and b.is_cancel = '1' ");
				sbSql.append("and b.jhy is null) order by c.xqjhsqbh desc");
				String sql = sbSql.toString();
				log.info(sql);
				Page page = this.getPage(sql);
				String json = JSONUtil.listToJson((List) page.getResult(),
						listJsonProperties());
				
				json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
				+ "}";
				this.outputJson(json);
			}
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 描述 : 查看待生效需求计划 作者 : leichi
	 */
	public String showDsxXqjhAction() throws Exception {
		// // 取得流程数据字典
		// String ypz = dictManager
		// .findDictByCatalogNameWithKey("WZXQJHSQ", "已批准").getValue();
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();

		try {
			String sql = "SELECT DISTINCT wz_xqjhsq.id,wz_xqjhsq.xqjhsqbh,"
					+ " wz_xqjhsq.gclb,"
					+ " wz_xqjhsq.gcxm,"
					+ " wz_xqjhsq.sqbm,"
					+ " wz_xqjhsq.sqsj,"
					+ " wz_xqjhsq.czy,"
					+ " wz_xqjhsq.czyxm,"
					+ " wz_xqjhsq.fpsj,"
					+ " wz_xqjhsq.xqjhqf,"
					+ " wz_xqjhsq.sqbmmc,"
					+ " wz_xqjhsq.status,"
					+ " wz_xqjhsq.flow_status,"
					+ " wz_xqjhsq.is_back,"
					+ " wz_xqjhsq_detail.is_cancel"
					+ " FROM tb_wz_xqjhsq wz_xqjhsq, tb_wz_xqjhsq_detail wz_xqjhsq_detail"
					+ " WHERE (wz_xqjhsq.id = wz_xqjhsq_detail.xqjhsqId)"
					+ " and wz_xqjhsq.flow_status = '已完成'"
					+ " and wz_xqjhsq.status = '1'"
					+ " and wz_xqjhsq_detail.is_cancel = '1'"
					+ " and wz_xqjhsq_detail.done = '0'"
					+ " and wz_xqjhsq_detail.c_flag = '1'"
					+ " and (wz_xqjhsq_detail.jhy = '" + userLoginName + "')"
					+ " order by wz_xqjhsq.xqjhsqbh desc";
			// +" and (wz_xqjhsq_detail.is_cancel = '0')";
			// +" and (wz_xqjhsq_detail.done = '0')"
			// +" and (wz_xqjhsq_detail.c_flag = '1')";
			Page page = this.getPage(sql);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "ID", "XQJHSQBH", "GCLB", "GCXM", "SQBM",
							"SQSJ", "CZY", "CZYXM", "IS_BACK", "time",
							"STATUS", "C_LCBH", "GCXMBH", "C_FLAG", "FPSJ","XQJHQF",
							"SQBMMC", "FLOW_STATUS" });

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	// 取得Page
	public Page getPage(String sql) throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		return xqjhsqJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
	}

	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID","XQJHQF",
				/*
				 * "id", "xqjhsqbh", "gclb", "gcxm", "sqbm", "sqsj", "czy",
				 * "czyxm", "time", "status", "clcbh", "gcxmbh", "cflag",
				 * "fpsj", "sqbmmc"
				 */
				"ID", "XQJHSQBH", "GCLB", "GCXM", "SQBM", "SQSJ", "CZY",
				"CZYXM", "time", "STATUS", "IS_BACK", "C_LCBH", "GCXMBH",
				"C_FLAG", "FPSJ", "SQBMMC", "FLOW_STATUS"

		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID","XQJHQF",
				/*
				 * "id", "xqjhsqbh", "gclb", "gcxm", "sqbm", "sqsj", "czy",
				 * "czyxm", "time", "status", "clcbh", "gcxmbh", "cflag",
				 * "fpsj", "sqbmmc"
				 */
				"ID", "XQJHSQBH", "GCLB", "GCXM", "SQBM", "SQSJ", "CZY",
				"CZYXM", "TIME", "STATUS", "IS_BACK", "CLCBH", "GCXMBH",
				"CFLAG", "FPSJ", "SQBMMC", "FLOW_STATUS"

		};
	}

	/**
	 * 描述 : 需求计划申请单作废 作者 : caofei 时间 : Oct 19, 2010 参数 : 返回值 : 异常 :
	 */
	public String cancelXqjhsqAction() throws Exception {
		String curLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		String token = request.getParameter("token");
		String curTaskName = request.getParameter("curTaskName");
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update tb_wz_xqjhsq t set t.flow_status = '已作废' where t.id ='"
					+ id + "'";
			String sqls = "update tb_wf_todo t set t.flag = '0', t.status = '已处理', t.deal_person = '"+curLoginName+"', t.deal_time = sysdate where t.docid ='"+id+"' and t.status = '待处理' and t.flag = '1'";
			
			defeasanceNodeAction(pid,token,curTaskName);
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.executeUpdate(sqls);
			st.close();
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 作废操作
	 * 作者 : caofei
	 * 时间 : 2010-05-27
	 * 参数 : 
	 * 异常 ： IOException 
	 */
	public String defeasanceNodeAction(String pid, String token, String curTaskName) throws IOException {
		String curUserName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();//当前处理人
		String curUserLoginName = this.getCurUserLoginName();
		String curLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		
		try {
			ProcessInstance pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(Long.parseLong(pid));
			pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, this.getCurUserLoginName());
			boolean isCanCancel = true;
			// 找到所有未完成任务实例
			Collection<TaskInstance> taskInstances = jbpmOperateManager.getUnfinishedTaskInstance(pi, null);
			//当前任务实例
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(Long.parseLong(pid), this.getCurUserLoginName(),token);
			//记录流程日志
			workFlowLogManager.addFlowLog(ti.getProcessInstance().getId(), curTaskName, curUserName, curLoginName, "", "", "作废流程", "");
		}catch (Exception ex) {
			writeOpSuccessScript("作废失败");
			ex.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 查询已经推出去，但未走完的需求计划申请
	 * @return
	 * @throws Exception 
	 */
	public String findXqjhsqAction() throws Exception{
		try{
			String sql = "select ID,XQJHSQBH,GCLB,GCXM,SQBMMC " +
					" from tb_wz_xqjhsq t where t.status = '0' and t.FLOW_STATUS!='已作废'" +
					" and t.FLOW_STATUS!='填写申请'  and t.FLOW_STATUS!='已完成' and t.xqjhqf = '1' " +
					" and (t.dyszt != '2' or t.dyszt is null) order by t.XQJHSQBH desc";
			Page page = this.getPage(sql);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "ID", "XQJHSQBH", "GCLB", "GCXM", "SQBMMC"});

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 获取当前用户登录名
	 */
	private String getCurUserLoginName(){
		return sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public P2RoleManager getP2roleManager() {
		return p2roleManager;
	}

	public void setP2roleManager(P2RoleManager manager) {
		p2roleManager = manager;
	}

}
