/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.htsp.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
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
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.htsp.manager.HeTongShenPiManager;
import com.jteap.gcht.htsp.model.Ht;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;

/**
 * 合同审批Action
 * @author caihuiwen
 */
@SuppressWarnings({"serial", "unchecked"})
public class HeTongShenPiAction extends AbstractAction{
	
	private HeTongShenPiManager heTongShenPiManager;
	private DataSource dataSource;
	private TaskToDoManager taskToDoManager;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	public void setHeTongShenPiManager(HeTongShenPiManager heTongShenPiManager) {
		this.heTongShenPiManager = heTongShenPiManager;
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
			"ID", "HTBH", "HTJE", "HTMC","HTLX","GFDW","SQBM","STATUS","HTCJSJ"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"ID_", "VERSION_", "START_", "END_","PROCESSINSTANCE_", "PROCESS_DATE", "FLOW_NAME", "FLOW_CONFIG_ID","FLOW_FORM_ID",
			"ID", "HTBH", "HTJE", "HTMC","HTLX","GFDW","SQBM","STATUS","HTCJSJ"
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
			Page page = heTongShenPiManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(),new String[]{
					"FLOW_TOPIC", "TASKTODOID","FLOW_NAME", "FLOW_INSTANCE_ID",
					"CURRENT_TASKNAME", "POST_PERSON","POST_TIME", "TOKEN", "CURSIGNIN",
					"ID", "HTBH", "HTJE", "HTMC", "HTLX", "GFDW", "SQBM", "STATUS","HTCJSJ"
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
			sbSql.append("and a.status != '已终结' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and a.id not in ");
			sbSql.append("(select docid from tb_wf_todo f  where f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"+userLoginName+"%')");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");
			
			String sql = sbSql.toString();
			Page page = heTongShenPiManager.pagedQueryTableData(sql, iStart, iLimit);
			
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
			Page page = heTongShenPiManager.pagedQueryTableData(sql, iStart, iLimit);

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
			Page page = heTongShenPiManager.pagedQueryTableData(sql, iStart, iLimit);
			
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
			Page page = heTongShenPiManager.pagedQueryTableData(sql, iStart, iLimit);
			
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
			Page page = heTongShenPiManager.pagedQueryTableData(sql, iStart, iLimit);
			
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
	 * 导出excel表
	 * 作者 ： 王韵
	 * 日期 ： 2010-11-25
	 */
	public String exportExcel() throws Exception {
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = request.getParameter("paraHeader");

		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
		String tableName = request.getParameter("tableName");

		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();

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

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			List list = new ArrayList();
			ResultSetMetaData rsmeta = rs.getMetaData();

			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= rsmeta.getColumnCount(); i++) {
					Object obj = rs.getObject(i);
					// 针对oracle timestamp日期单独处理，转换成date
					if (obj instanceof oracle.sql.TIMESTAMP) {
						obj = ((oracle.sql.TIMESTAMP) obj).dateValue().toString() + " " + ((oracle.sql.TIMESTAMP) obj).timeValue().toString();
					}

					map.put(rsmeta.getColumnName(i), obj);
				}
				list.add(map);
			}
			rs.close();

			// 调用导出方法
			export(list, paraHeader, paraDataIndex, paraWidth);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}

	/**
	 * 获取当前的合同序号
	 * @return
	 */
	public String findCurrentHtxhAction(){
		String tableName = request.getParameter("tableName");
		String htlx = request.getParameter("htlx");
		
		int htxh = heTongShenPiManager.findCurrentHtxh(tableName, htlx);
		try {
			this.outputJson("{success:true,htxh:'"+htxh+"'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 初始化子合同信息
	 * 作者:童贝
	 * 时间:Feb 11, 2011
	 * @return
	 * @throws Exception
	 */
	public String findZHtxhAction() throws Exception{
		String tableName = request.getParameter("tableName");
		String id = request.getParameter("id");
		String flbm = request.getParameter("flbm");
		if(StringUtils.isNotEmpty(id)&&!id.equals("null")){
			Ht ht=null;
			String[] ser=null;
			if(Ht.FLBM_WZ.equals(flbm)){
				ht=this.heTongShenPiManager.findWzhtByID(id, tableName);
				ser=new String[]{"id","htmc","htbh","htxh","htlx","htje","gfdw","xfdw","qddd","cjsj","jhsqdh","sqbm","sqr","qtsm","fyxz","parentid","tableName"};
			}else if(Ht.FLBM_RL.equals(flbm)){
				ht=this.heTongShenPiManager.findRlhtByID(id, tableName);
				ser=new String[]{"id","htmc","htbh","htxh","htlx","gfdw","xfdw","qddd","cjsj","jhsqdh","sqbm","sqr","qtsm","fyxz","parentid","tableName"};
			}else if(Ht.FLBM_GC.equals(flbm)){
				ht=this.heTongShenPiManager.findGchtByID(id, tableName);
				ser=new String[]{"id","htxh","gcid","htbh","htmc","htlx","htje","htxmswtf","gcmc","gcbm","cbfs","fyly","djyj","gfdw","cjsj","jhsqdh","sqbm","sqr","fyxz","qtsm","parentid","tableName"};
			}else if(Ht.FLBM_CW.equals(flbm)){
				ht=this.heTongShenPiManager.findCwhtByID(id, tableName);
				ser=new String[]{"id","htmc","htbh","htxh","htlx","htje","ll","hjh","qtsm","fyxz","parentid","tableName"};
			}
			this.heTongShenPiManager.inithtBhAndXh(ht);
			String json=JSONUtil.objectToJson(ht,ser);
			this.outputJson("{success:true,data:"+json+"}");
		}else{
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 是否是子合同
	 * 作者:童贝
	 * 时间:Feb 14, 2011
	 * @return
	 * @throws Exception
	 */
	public String isZthAction() throws Exception{
		String tableName = request.getParameter("tableName");
		String id = request.getParameter("id");
		boolean isZth=this.heTongShenPiManager.isZthByID(id, tableName);
		outputJson("{success:true,isZth:"+isZth+"}");
		return NONE;
	}
	
	//从一般立项单、委托单 选择已立项的项目
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
		
		String xmbh = request.getParameter("xmbh");
		String xmmc = request.getParameter("xmmc");
		String xmlx = request.getParameter("xmlx");
		String cbfs = request.getParameter("cbfs");
		String htcjsj = request.getParameter("htcjsj");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select id,xmbh,xmmc,xmlx,cbfs,cjsj from tb_ht_yblxd a where a.status='已立项'");
			if(StringUtils.isNotEmpty(xmbh)){
				sbSql.append(" and a.xmbh='"+xmbh+"'");
			}
			if(StringUtils.isNotEmpty(xmmc)){
				sbSql.append(" and a.xmmc like '%"+xmmc+"%'");
			}
			if(StringUtils.isNotEmpty(xmlx)){
				sbSql.append(" and a.xmlx='"+xmlx+"'");
			}
			if(StringUtils.isNotEmpty(cbfs)){
				sbSql.append(" and a.cbfs='"+cbfs+"'");
			}
			if(StringUtils.isNotEmpty(htcjsj)){
				sbSql.append(" and to_char(a.cjsj,'yyyy')='"+htcjsj+"'");
			}
			sbSql.append(" order by a.cjsj desc");
			
			StringBuffer sbSql2 = new StringBuffer();
			sbSql2.append("select id,xmbh,xmmc,xmlx,cbfs,cjsj from tb_ht_wtd b where b.status='已立项'");
			if(StringUtils.isNotEmpty(xmbh)){
				sbSql2.append(" and b.xmbh='"+xmbh+"'");
			}
			if(StringUtils.isNotEmpty(xmmc)){
				sbSql2.append(" and b.xmmc like '%"+xmmc+"%'");
			}
			if(StringUtils.isNotEmpty(xmlx)){
				sbSql2.append(" and b.xmlx='"+xmlx+"'");
			}
			if(StringUtils.isNotEmpty(cbfs)){
				sbSql2.append(" and b.cbfs='"+cbfs+"'");
			}
			if(StringUtils.isNotEmpty(htcjsj)){
				sbSql2.append(" and to_char(b.cjsj,'yyyy')='"+htcjsj+"'");
			}
			sbSql2.append(" order by b.cjsj desc");
			
			String sql = sbSql.toString();
			List list1 = heTongShenPiManager.querySqlData(sql);
			String sql2 = sbSql2.toString();
			List list2 = heTongShenPiManager.querySqlData(sql2);
			list1.addAll(list2);
			
			if(iLimit > list1.size()){
				iLimit = list1.size();
			}
			List subList = list1.subList(iStart, iLimit);
			String json = JSONUtil.listToJson(subList, 
							new String[]{"ID","XMBH","XMMC","XMLX","CBFS","CJSJ"});
			
			json = "{totalCount:'" + list1.size() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 查看合同编号是否唯一
	 * @return
	 * @throws Exception
	 */
	public String findIsUniqAction() throws Exception{
		String htbh=request.getParameter("htbh");
		String tableName=request.getParameter("tableName");
		boolean isU=this.heTongShenPiManager.isUniqHtbh(tableName, htbh);
		this.outputJson("{success:true,isUniq:"+isU+"}");
		return NONE;
	}
}
