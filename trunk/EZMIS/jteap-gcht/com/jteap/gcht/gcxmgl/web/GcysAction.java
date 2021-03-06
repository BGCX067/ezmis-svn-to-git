/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.gcxmgl.web;

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
import com.jteap.gcht.gcxmgl.manager.GcysManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;

/**
 * 工程验收审批Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class GcysAction extends AbstractAction{
	
	private GcysManager gcysManager;
	private DataSource dataSource;
	private TaskToDoManager taskToDoManager;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}
	
	public void setGcysManager(GcysManager gcysManager) {
		this.gcysManager = gcysManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"ID_", "VERSION_", "START_", "END_","PROCESSINSTANCE_","PROCESS_DATE", "FLOW_NAME", "FLOW_CONFIG_ID","FLOW_FORM_ID",
			"ID", "GCXMBH", "GCXMMC", "SGDW", "KGSJ","JGSJ","CJSJ","STATUS","ISZF"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"ID_", "VERSION_", "START_", "END_","PROCESSINSTANCE_", "PROCESS_DATE", "FLOW_NAME", "FLOW_CONFIG_ID","FLOW_FORM_ID",
			"ID", "GCXMBH", "GCXMMC", "SGDW", "KGSJ","JGSJ","CJSJ","STATUS","ISZF"
		};
	}

	/**
	 * 待审批
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
		
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		String tableName = request.getParameter("tableName");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select b.FLOW_TOPIC, ");
			sbSql.append("b.ID as TASKTODOID, ");
			sbSql.append("b.FLOW_NAME, ");
			sbSql.append("b.FLOW_INSTANCE_ID, ");
			sbSql.append("b.CURRENT_TASKNAME, ");
			sbSql.append("b.POST_PERSON, ");
			sbSql.append("to_char(b.POST_TIME,'yyyy-MM-dd HH24:mi:ss') as POST_TIME, ");
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
			System.out.println(sql);
			Page page = gcysManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(),new String[]{
					"FLOW_TOPIC", "TASKTODOID","FLOW_NAME", "FLOW_INSTANCE_ID",
					"CURRENT_TASKNAME", "POST_PERSON","POST_TIME", "TOKEN", "CURSIGNIN",
					"ID", "GCXMBH", "GCXMMC", "SGDW", "KGSJ","JGSJ","CJSJ","STATUS"
			});
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 已审批
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
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
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
			sbSql.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
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
//			sbSql.append("and a.status != '已终结' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and not exists ");
    		sbSql.append("(select 1 from tb_wf_todo f  where f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"+userLoginName+"%')");
//			
//			String sqlWhere = request.getParameter("queryParamsSql");
//			if (StringUtils.isNotEmpty(sqlWhere)) {
//				String hqlWhereTemp = sqlWhere.replace("$", "%");
//				sbSql.append(" and " + hqlWhereTemp);
//			}
			sbSql.append(" order by a.cjsj desc");
			
			String sql = sbSql.toString();
			Page page = gcysManager.pagedQueryTableData(sql, iStart, iLimit);
			
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 草稿箱
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
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
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
			sbSql.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
			sbSql.append("from " + tableName +" a,");
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
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");
			
			String sql = sbSql.toString();
			Page page = gcysManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 全厂
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
			sbSql.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
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
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");
			
			String sql = sbSql.toString();
			Page page = gcysManager.pagedQueryTableData(sql, iStart, iLimit);
			
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 已作废
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
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
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
			sbSql.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
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
			Page page = gcysManager.pagedQueryTableData(sql, iStart, iLimit);
			
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 已终结
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
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
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
			sbSql.append("to_char(e.PROCESS_DATE,'yyyy-MM-dd HH24:mm:ss') as PROCESS_DATE ");
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
			Page page = gcysManager.pagedQueryTableData(sql, iStart, iLimit);
			
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 作废合同
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
			String sql = "update " + tableName + " t set t.status = '已作废' where t.id ='"+id+"'";
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
	
	/**
	 * 选择已开工签证的项目
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String chooseXmAction() throws Exception{
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
			sbSql.append("select * from tb_ht_kgqz t where status='已终结' and isys='未验收'"); 
			if(StringUtils.isNotEmpty(gcxmbh)){
				sbSql.append(" and t.gcxmbh='"+gcxmbh+"'");
			}
			if(StringUtils.isNotEmpty(gcxmmc)){
				sbSql.append(" and t.gcxmmc like '%"+gcxmmc+"%'");
			}
			if(StringUtils.isNotEmpty(gccbdw)){
				sbSql.append(" and t.gccbdw='"+gccbdw+"'");
			}
			if(StringUtils.isNotEmpty(htcjsj)){
				sbSql.append(" and to_char(t.cjsj,'yyyy')='"+htcjsj+"'");
			}
			sbSql.append(" order by t.cjsj desc");
			
			String sql = sbSql.toString();
			Page page = gcysManager.pagedQueryTableData(sql, iStart, iLimit);
			
			String json = JSONUtil.listToJson((List) page.getResult(), 
							new String[]{"ID","GCXMBH","GCXMMC","XMYJ","GCCBDW","GCNR","JHGQ","CJSJ"});
			
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
}
