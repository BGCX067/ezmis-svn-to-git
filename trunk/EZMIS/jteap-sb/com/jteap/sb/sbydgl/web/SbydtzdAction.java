package com.jteap.sb.sbydgl.web;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.sb.sbydgl.manager.SbydglManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;

/**
 * 设备异动通知单Action
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class SbydtzdAction extends AbstractAction {

	private SbydglManager sbydglManager;

	private DataSource dataSource;
	
	private TaskToDoManager taskToDoManager;

	public TaskToDoManager getTaskToDoManager() {
		return taskToDoManager;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	public SbydglManager getSbydglManager() {
		return sbydglManager;
	}

	public void setSbydglManager(SbydglManager sbydglManager) {
		this.sbydglManager = sbydglManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID", "ID", "SBMC", "STATUS", "SQBM", "SQR", "SQSJ" };
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	/**
	 * 描述 : 查看待处理异动报告 作者 : caofei 时间 : Aug 9, 2010 参数 : 返回值 : 异常 :
	 */
	public String showDclQxdAction() throws Exception {
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
			sbSql
					.append("to_char(a.POST_TIME,'yyyy-MM-dd HH:mi:ss') as POST_TIME, ");
			sbSql.append("a.TOKEN, ");
			sbSql.append("a.CURSIGNIN, ");
			sbSql.append("b.*, ");
			sbSql.append("d.personname as curSignInName ");
			sbSql.append("from tb_wf_todo a, ");
			sbSql.append("tb_sb_sbydgl_ydxx b, ");
			sbSql.append("jbpm_variableinstance c, ");
			sbSql.append("tb_sys_person d ");
			sbSql.append("where a.current_process_person like '%");
			sbSql.append(userLoginName);
			sbSql.append("%' ");
			sbSql.append("and a.flag = '1' and b.type ='异动通知单' ");
			// sbSql.append("and b.QXZY in (");
			// for (String role : userRoles) {
			// sbSql.append("'");
			// sbSql.append(role);
			// sbSql.append("',");
			// }
			// if (userRoles.length >0) {
			// sbSql.delete(sbSql.length()-1, sbSql.length());
			// }
			// sbSql.append(") ");
			sbSql.append("and a.flow_instance_id = c.processinstance_ ");
			sbSql.append("and b.id = c.stringvalue_ ");
			sbSql.append("and c.name_='docid' ");
			sbSql.append("and b.status != '已作废' ");
			sbSql.append("and d.id(+) = a.cursignin ");
			String sql = sbSql.toString();
			Page page = sbydglManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
							"FLOW_INSTANCE_ID", "CURRENT_TASKNAME",
							"POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN",
							"ID", "SBMC", "STATUS", "SQBM", "SQR", "SQSJ",
							"curSignInName" });
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
	 * 描述 : 查看已处理异动报告 作者 : caofei 时间 : Aug 9, 2010 参数 : 返回值 : 异常 :
	 */
	public String showYclQxdAction() throws Exception {
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
			sbSql.append("from tb_sb_sbydgl_ydxx a,");
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
			sbSql.append("and a.type ='异动通知单' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and a.id not in ");
			sbSql.append("(select docid from tb_wf_todo f  where f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"+userLoginName+"%')");
			String sql = sbSql.toString();
			Page page = sbydglManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 描述 : 查看草稿箱 作者 : caofei 时间 : Aug 9, 2010 参数 : 返回值 : 异常 :
	 */
	public String showCgxAction() throws Exception {
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
			sbSql.append("from tb_sb_sbydgl_ydxx a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and a.type ='异动通知单' ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' ");
			sbSql.append("and a.status is null ");
			sbSql
					.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			String sql = sbSql.toString();
			Page page = sbydglManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 描述 : 查看全厂 作者 : caofei 时间 : Aug 9, 2010 参数 : 返回值 : 异常 :
	 */
	public String showQcAction() throws Exception {
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
			sbSql.append("from tb_sb_sbydgl_ydxx a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.type ='异动通知单' ");
			sbSql.append("and a.status is not null ");
			sbSql
					.append("and e.id in (select max(id) from tb_wf_log group by pi_id)");
			String sql = sbSql.toString();
			Page page = sbydglManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 描述 : 查看作废 作者 : caofei 时间 : Aug 9, 2010 参数 : 返回值 : 异常 :
	 */
	public String showZfQxdAction() throws Exception {
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
			sbSql.append("from tb_sb_sbydgl_ydxx a,");
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
			sbSql.append("and a.type ='异动通知单' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id)");
			String sql = sbSql.toString();
			Page page = sbydglManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 描述 : 异动报告作废 作者 : caofei 时间 : Aug 9, 2010 参数 : 返回值 : 异常 :
	 */
	public String cancelQxdAction() throws Exception {
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update tb_sb_sbydgl_ydxx t set t.status = '已作废' where t.id ='"
					+ id + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.close();

			List<TaskToDo> lstTaskToDo = taskToDoManager.findBy("flowInstance", pid);
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

}
