/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.gcxmgl.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.gcxmgl.manager.GcysManager;
import com.jteap.gcht.gcxmgl.manager.JszfManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;

/**
 * 工程结算支付单审批Action
 * 
 * @author wangyi
 */
@SuppressWarnings("serial")
public class JszfAction extends AbstractAction {

	private JszfManager jszfManager;
	private DataSource dataSource;
	private TaskToDoManager taskToDoManager;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	public void setJszfManager(JszfManager jszfManager) {
		this.jszfManager = jszfManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "PROCESS_DATE", "FLOW_NAME",
				"FLOW_CONFIG_ID", "FLOW_FORM_ID", "ID","XMBH","XMMC", "CBDW",
				"XMHTJE", "YLJYFKJE", "BCYFK", "BCSFK", "BCKK", "CJSJ",
				"FQZT","ZFZT","STATUS" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "PROCESS_DATE", "FLOW_NAME",
				"FLOW_CONFIG_ID", "FLOW_FORM_ID", "ID","XMBH","XMMC", "CBDW",
				"XMHTJE", "YLJYFKJE", "BCYFK", "BCSFK", "BCKK", "CJSJ",
				"FQZT","ZFZT","STATUS" };
	}

	/**
	 * 待审批
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showDspHeTongAction() throws Exception {
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
		String tableName = request.getParameter("tableName");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select b.FLOW_TOPIC, ");
			sbSql.append("b.ID as TASKTODOID, ");
			sbSql.append("b.FLOW_NAME, ");
			sbSql.append("b.FLOW_INSTANCE_ID, ");
			sbSql.append("b.CURRENT_TASKNAME, ");
			sbSql.append("b.POST_PERSON, ");
			sbSql
					.append("to_char(b.POST_TIME,'yyyy-MM-dd HH24:mi:ss') as POST_TIME, ");
			sbSql.append("b.TOKEN, ");
			sbSql.append("b.CURSIGNIN, ");
			sbSql.append("a.*, ");
			sbSql.append("d.personname as curSignInName ");
			sbSql.append("from tb_wf_todo b, ");
			sbSql.append(tableName + " a, ");
			sbSql.append("jbpm_variableinstance c, ");
			sbSql.append("tb_sys_person d ");
			sbSql.append("where b.current_process_person like '%");
			sbSql.append(userLoginName);
			sbSql.append("%' ");
			sbSql.append("and b.flag = '1' ");
			sbSql.append("and b.flow_instance_id = c.processinstance_ ");
			sbSql.append("and a.id = c.stringvalue_ ");
			sbSql.append("and c.name_='docid' ");
			sbSql.append("and a.status != '已作废' ");
			sbSql.append("and a.status != '已终结' ");
			sbSql.append("and d.id(+) = b.cursignin ");

			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by POST_TIME desc");
			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
							"FLOW_INSTANCE_ID", "CURRENT_TASKNAME",
							"POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN",
							"ID","XMBH","XMMC", "CBDW", "XMHTJE", "YLJYFKJE",
							"BCYFK", "BCSFK", "BCKK", "CJSJ","FQZT","ZFZT", "STATUS" });
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
	 * 已审批
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showYspHeTongAction() throws Exception {
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
		String tableName = request.getParameter("tableName");
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
			sbSql.append("d.flow_form_id,");
			sbSql
					.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
			sbSql.append("from " + tableName + " a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.status is not null and a.status != '已作废' ");
			// sbSql.append("and a.status != '已终结' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and not exists ");
			sbSql.append("(select 1 from tb_wf_todo f  where f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"
							+ userLoginName + "%')");

//			String sqlWhere = request.getParameter("queryParamsSql");
//			if (StringUtils.isNotEmpty(sqlWhere)) {
//				String hqlWhereTemp = sqlWhere.replace("$", "%");
//				sbSql.append(" and " + hqlWhereTemp);
//			}
			sbSql.append(" order by a.cjsj desc");
			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 草稿箱
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showCgxHeTongAction() throws Exception {
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
		String tableName = request.getParameter("tableName");
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
			sbSql.append("d.flow_form_id,");
			sbSql
					.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
			sbSql.append("from " + tableName + " a,");
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
			sbSql.append("and a.status is null ");
			sbSql
					.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");

			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");

			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 全厂
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showQcHeTongAction() throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String tableName = request.getParameter("tableName");
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
			sbSql.append("d.flow_form_id,");
			sbSql
					.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
			sbSql.append("from " + tableName + " a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.status is not null ");
			sbSql
					.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");

			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");

			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 已作废
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showYzfHeTongAction() throws Exception {
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
		String tableName = request.getParameter("tableName");
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
			sbSql.append("d.flow_form_id,");
			sbSql
					.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
			sbSql.append("from " + tableName + " a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.status ='已作废' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");

			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");

			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 已终结
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showYzjHeTongAction() throws Exception {
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
		String tableName = request.getParameter("tableName");
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
			sbSql.append("d.flow_form_id,");
			sbSql
					.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
			sbSql.append("from " + tableName + " a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.status ='已终结' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");

			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");

			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 作废合同
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cancelHeTongAction() throws Exception {
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		String tableName = request.getParameter("tableName");

		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update " + tableName
					+ " t set t.status = '已作废' where t.id ='" + id + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.close();

			List<TaskToDo> lstTaskToDo = taskToDoManager.findBy("flowInstance",
					pid);
			for (TaskToDo taskToDo : lstTaskToDo) {
				taskToDo.setFlag(false);
				taskToDoManager.save(taskToDo);
			}

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
	 * 选择已验收的项目
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String chooseXmAction() throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);

		String gcxmbh = request.getParameter("gcxmbh");
		String gcxmmc = request.getParameter("gcxmmc");
		String gccbdw = request.getParameter("gccbdw");
		String htcjsj = request.getParameter("htcjsj");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql
					.append("select * from tb_ht_ysd t where status='已终结' and iszf='未支付'");
			if (StringUtils.isNotEmpty(gcxmbh)) {
				sbSql.append(" and t.gcxmbh='" + gcxmbh + "'");
			}
			if (StringUtils.isNotEmpty(gcxmmc)) {
				sbSql.append(" and t.gcxmmc like '%" + gcxmmc + "%'");
			}
			if (StringUtils.isNotEmpty(htcjsj)) {
				sbSql.append(" and to_char(t.cjsj,'yyyy')='" + htcjsj + "'");
			}
			sbSql.append(" order by t.cjsj desc");

			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "ID", "GCXMBH", "GCXMMC", "SGDW", "XMYJ",
							"JGSJ", "GCNR", "YSNF", "CJSJ", "STATUS" });

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
	 * 选择欠款的支付单
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String chooseQkAction() throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);

		String gcxmbh = request.getParameter("gcxmbh");
		String gcxmmc = request.getParameter("gcxmmc");
		String gccbdw = request.getParameter("gccbdw");
		String htcjsj = request.getParameter("htcjsj");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql
					.append("select " +
							"a.ID," +
							"a.GCYS_ID," +
							"a.XMBH," +
							"a.XMMC," +
							"a.XMWCSJHNR," +
							"a.CBDW," +
							"a.XMFYLY," +
							"a.KHH," +
							"a.SSBM," +
							"a.SKDWZH," +
							"a.XMHTJE," +
							"a.YLJYFKJE," +
							"a.BCYFK," +
							"a.BCSFK," +
							"a.BCKK," +
							"a.CJSJ," +
							"a.ZFZT," +
							"a.FQZT,"+
							"a.STATUS " 
							+"from TB_HT_GCXMJSZFD a,(select max(t.cjsj) as cjsj, t.xmbh from TB_HT_GCXMJSZFD t "
							+ "having max(t.yljyfkje) < max(t.xmhtje)"
							+ "group by t.xmbh) b "
							+ "where a.xmbh = b.xmbh "
							+ "and a.cjsj = b.cjsj and a.STATUS='已终结' and a.ZFZT='欠款'");
			if (StringUtils.isNotEmpty(gcxmbh)) {
				sbSql.append(" and a.xmbh='" + gcxmbh + "'");
			}
			if (StringUtils.isNotEmpty(gcxmmc)) {
				sbSql.append(" and a.xmmc like '%" + gcxmmc + "%'");
			}
			if (StringUtils.isNotEmpty(htcjsj)) {
				sbSql.append(" and to_char(a.cjsj,'yyyy')='" + htcjsj + "'");
			}
			sbSql.append(" order by a.cjsj desc");

			String sql = sbSql.toString();
			Page page = jszfManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "ID","GCYS_ID","XMBH", "XMMC", "XMWCSJHNR", "CBDW",
							"XMFYLY", "KHH", "SKDWZH", "XMHTJE","SSBM",
							"YLJYFKJE", "BCYFK", "BCSFK", "BCKK", "CJSJ",
							"ZFZT","FQZT","STATUS" });

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
	 * 描述 : 根据供应商名称获得供应商信息
	 * 作者 : wangyi
	 * 时间 : 2012-7-17
	 * 参数 : 
	 * 		gysmc : 查询条件
	 * 返回值 : 
	 * 		list : 记录集合
	 * 异常 : Exception
	 */
	public String findGysxxList() throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String gysmc = request.getParameter("gysmc");
//		String schema = SystemConfig.getProperty("jdbc.schema", "EZMIS");
		String sql = "SELECT a.ID, a.GYSMC, a.FRDB, a.QYLX, a.CATALOG_ID, a.GYSLB, a.SWLXR, a.SWLXRDH, a.GYSDZ, a.KHYH, a.YHZH, a.YFFZR, a.YFFZRLXFS, a.YFSGFZR, a.YFSGFZRLXFS FROM TB_HT_GYSXX a";

		if (StringUtil.isNotEmpty(gysmc)) {
			sql += " where a.GYSMC= " + gysmc+"";
		}
//		Connection conn = null;
//		PreparedStatement statement = null;
//		ResultSet rs = null;

		try {
//			conn = dataSource.getConnection();
//			statement = conn.prepareStatement(sql);
//			rs = statement.executeQuery();
//			while (rs.next()) {
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("ID", rs.getString("ID"));
//				map.put("GYSMC", rs.getString("GYSMC"));
//				map.put("FRDB", rs.getString("FRDB"));
//				map.put("QYLX", rs.getString("QYLX"));
//				map.put("CATALOGID", rs.getString("CATALOG_ID"));
//				map.put("GYSLB", rs.getString("GYSLB"));
//				map.put("SWLXR", rs.getString("SWLXR"));
//				map.put("SWLXRDH", rs.getString("SWLXRDH"));
//				map.put("GYSDZ", rs.getString("GYSDZ"));
//				map.put("KHYH", rs.getString("KHYH"));
//				map.put("YHZH", rs.getString("YHZH"));
//				map.put("YFFZR", rs.getString("YFFZR"));
//				map.put("YFFZRLXFS", rs.getString("YFFZRLXFS"));
//				map.put("YFSGFZR", rs.getString("YFSGFZR"));
//				map.put("YFSGFZRLXFS", rs.getString("YFSGFZRLXFS"));
//
//				list.add(map);
//			}
		    Page page = jszfManager.pagedQueryTableData(sql, 0, 10);
			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "ID","KHYH","YHZH"});

			json = "{totalCount:'" + list.size() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		} 
//		finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (statement != null) {
//					statement.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		return NONE;
	}
	
	/**
	 * 获取当前的项目编号
	 * @return
	 */
	public String findCurrentXmxhAction(){
		try {
			String tableName = request.getParameter("tableName");
			int xmbh = jszfManager.findCurrentXmbh(tableName);
			this.outputJson("{success:true,xmbh:'"+xmbh+"'}");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
}
