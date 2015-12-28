package com.jteap.lp.lspgl.web;

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
import com.jteap.lp.czpgl.manager.CzpglManager;
import com.jteap.lp.lspgl.manager.LspglManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;
import com.jteap.wfengine.wfi.manager.WorkFlowInstanceManager;

/**
 * 临时票管理Action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"unused","unchecked","serial"})
public class LspglAction extends AbstractAction {

	private LspglManager lspglManager;
	private PersonManager personManager;
	private DataSource dataSource;
	private WorkFlowInstanceManager workFlowInstanceManager;
	private TaskToDoManager taskToDoManager;
	private DictManager dictManager;

	/**
	 * 
	 * 描述 : 查看待处理操作票
	 * 作者 : wangyun
	 * 时间 : Jul 19, 2010
	 */
	public String showDclLspAction() throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		// 操作票票种对应数据库表名
		List<Dict> lstLsp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_LSP2TABLE");
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
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
			sbSql.append("t.*, ");
			sbSql.append("d.personname as curSignInName ");
			sbSql.append("from tb_wf_todo b, ");
			
			sbSql.append("(");
			for (Dict dict : lstLsp) {
				String tableName = dict.getValue();
				sbSql.append("select ID, PH,STATUS, FZRMC ");
				sbSql.append(" from ");
				sbSql.append(tableName);
				sbSql.append(" a ");
				sbSql.append("where  a.status != '已作废' ");
				sbSql.append("union all ");
			}
			sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
			sbSql.append(" ) t,");

			sbSql.append("jbpm_variableinstance c, ");
			sbSql.append("tb_sys_person d ");
			sbSql.append("where b.current_process_person like '%");
			sbSql.append(userLoginName);
			sbSql.append("%' ");
			sbSql.append("and b.flag = '1' ");
			sbSql.append("and b.flow_instance_id = c.processinstance_ ");
			sbSql.append("and t.id = c.stringvalue_ ");
			sbSql.append("and c.name_='docid' ");
			sbSql.append("and d.id(+) = b.cursignin ");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}

			String sql = sbSql.toString();
			Page page = lspglManager.pagedQueryTableData(sql, iStart, iLimit);

			String json = JSONUtil.listToJson((List) page.getResult(), 
					new String[] { "FLOW_TOPIC", "TASKTODOID","FLOW_NAME", "FLOW_INSTANCE_ID", "CURRENT_TASKNAME", "POST_PERSON",
					"POST_TIME", "TOKEN", "CURSIGNIN", "PH", "FZRMC",  "STATUS", "PMC", "curSignInName"});
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 查看已处理操作票
	 * 作者 : wangyun
	 * 时间 : Jul 19, 2010
	 */
	public String showYclLspAction() throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		// 操作票票种对应数据库表名
		List<Dict> lstCzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_LSP2TABLE");
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select t.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from ");
			
			sbSql.append("(");
			for (Dict dict : lstCzp) {
				String tableName = dict.getValue();
				sbSql.append("select ID, PH,STATUS, FZRMC ");
				sbSql.append(" from ");
				sbSql.append(tableName);
				sbSql.append(" a ");
				sbSql.append("where  a.status is not null and a.status != '已作废' ");
				sbSql.append("union all ");
			}
			sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
			sbSql.append(" ) t,");
			
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = t.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and t.id not in ");
			sbSql.append("(select docid from tb_wf_todo f  where f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"+userLoginName+"%')");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}

			String sql = sbSql.toString();
			Page page = lspglManager.pagedQueryTableData(sql, iStart, iLimit);
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
	 * 
	 * 描述 : 查看草稿箱
	 * 作者 : wangyun
	 * 时间 : Jul 21, 2010
	 * 异常 : Exception
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
		
		// 操作票票种对应数据库表名
		List<Dict> lstCzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_LSP2TABLE");
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select t.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from ");
			
			sbSql.append("(");
			for (Dict dict : lstCzp) {
				String tableName = dict.getValue();
				sbSql.append("select ID, PH,STATUS, FZRMC ");
				sbSql.append(" from ");
				sbSql.append(tableName);
				sbSql.append(" a ");
				sbSql.append("where a.status is null ");
				sbSql.append("union all ");
			}
			sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
			sbSql.append(" ) t,");
			
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = t.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
		
			String sql = sbSql.toString();
			Page page = lspglManager.pagedQueryTableData(sql, iStart, iLimit);

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
	 * 
	 * 描述 : 查看全厂
	 * 作者 : wangyun
	 * 时间 : Jul 21, 2010
	 * 异常 : Exception
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
		
		// 操作票票种对应数据库表名
		List<Dict> lstCzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_LSP2TABLE");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select t.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from ");
			
			sbSql.append("(");
			for (Dict dict : lstCzp) {
				String tableName = dict.getValue();
				sbSql.append("select ID, PH,STATUS, FZRMC ");
				sbSql.append(" from ");
				sbSql.append(tableName);
				sbSql.append(" a ");
				sbSql.append("where a.status is not null ");
				sbSql.append("union all ");
			}
			sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
			sbSql.append(" ) t,");
		
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = t.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
		
			String sql = sbSql.toString();
			Page page = lspglManager.pagedQueryTableData(sql, iStart, iLimit);
			
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
	 * 
	 * 描述 : 查看作废
	 * 作者 : wangyun
	 * 时间 : Jul 19, 2010
	 */
	public String showZfLspAction() throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		// 操作票票种对应数据库表名
		List<Dict> lstCzp = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_LSP2TABLE");
		String userLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select t.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from ");
			
			sbSql.append("(");
			for (Dict dict : lstCzp) {
				String tableName = dict.getValue();
				sbSql.append("select ID, PH,STATUS, FZRMC ");
				sbSql.append(" from ");
				sbSql.append(tableName);
				sbSql.append(" a ");
				sbSql.append("where a.status ='已作废' ");
				sbSql.append("union all ");
			}
			sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
			sbSql.append(" ) t,");
			
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = t.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
		
			String sql = sbSql.toString();
			Page page = lspglManager.pagedQueryTableData(sql, iStart, iLimit);
			
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
	 * 
	 * 描述 : 操作票作废
	 * 作者 : wangyun
	 * 时间 : Jul 28, 2010
	 * 异常 : Exception
	 */
	public String cancelCzpAction() throws Exception {
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update TB_LP_LSP_LSGZP t set t.status = '已作废' where t.id ='"+id+"'";
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

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID", "PH", "FZRMC", "STATUS", "ID"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}


	public LspglManager getLspglManager() {
		return lspglManager;
	}

	public void setLspglManager(LspglManager lspglManager) {
		this.lspglManager = lspglManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public WorkFlowInstanceManager getWorkFlowInstanceManager() {
		return workFlowInstanceManager;
	}

	public void setWorkFlowInstanceManager(WorkFlowInstanceManager workFlowInstanceManager) {
		this.workFlowInstanceManager = workFlowInstanceManager;
	}

	public TaskToDoManager getTaskToDoManager() {
		return taskToDoManager;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}
	
}
