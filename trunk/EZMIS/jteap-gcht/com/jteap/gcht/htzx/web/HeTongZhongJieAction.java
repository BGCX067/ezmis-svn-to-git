/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.htzx.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.htzx.manager.HeTongZhongJieManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.tasktodo.model.TaskToDo;

/**
 * 合同终结Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class HeTongZhongJieAction extends AbstractAction{
	
	private DataSource dataSource;
	private TaskToDoManager taskToDoManager;
	private HeTongZhongJieManager heTongZhongJieManager;
	
	public void setHeTongZhongJieManager(HeTongZhongJieManager heTongZhongJieManager) {
		this.heTongZhongJieManager = heTongZhongJieManager;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
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
			"ID", "HTBH","HTJE","HTMC","HTLX","SQR","SQBM","STATUS"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"ID_", "VERSION_", "START_", "END_","PROCESSINSTANCE_", "PROCESS_DATE", "FLOW_NAME", "FLOW_CONFIG_ID","FLOW_FORM_ID",
			"ID", "HTBH","HTJE","HTMC","HTLX","SQR","SQBM","STATUS"
		};
	}
	
	/**
	 * 查看全厂、审批状态为'合同生效'、【合同审批】中的合同
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showQcsxHeTongAction() throws Exception {
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
		String htlx = request.getParameter("htlx");
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
			sbSql.append("and a.status='合同生效' ");
			if(StringUtil.isNotEmpty(htlx)){
				sbSql.append("and a.htlx='" + htlx + "' ");
			}
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sbSql.append(" and " + hqlWhereTemp);
			}
			sbSql.append(" order by PROCESS_DATE desc");
			
			String sql = sbSql.toString();
			Page page = heTongZhongJieManager.pagedQueryTableData(sql, iStart, iLimit);
			
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
	 * 终结【合同审批】中的合同、【合同付款审批】中的关联合同
	 * @return
	 * @throws Exception
	 */
	public String zhongJieHeTongAction() throws Exception {
		String id = request.getParameter("id");
		//合同审批合同表名
		String tableName = request.getParameter("tableName");
		//合同付款审批合同表名
		String childTableName = request.getParameter("childTableName");
		
		//终结【合同审批】中的主合同
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update " + tableName + " t set t.status = '已终结' where t.id ='"+id+"'";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		if(StringUtils.isNotEmpty(childTableName)){
			//终结【合同付款审批】中的子合同、设置对应流程的待办为无效
			Connection conn2 = null;
			ResultSet resultSet2 = null;
			Statement statement2 = null;
			Statement statement3 = null;
			ResultSet resultSet4 = null;
			Statement statement4 = null;
			
			try {
				conn2 = dataSource.getConnection();
				
				//获取所有【合同付款审批】中子的合同
				String sql2 = "select id from " + childTableName + " where htid='" + id + "'";
				statement2 = conn2.createStatement();
				resultSet2 = statement2.executeQuery(sql2);
				//遍历终结【合同付款审批】中的终结子合同
				while(resultSet2.next()){
					String childId = resultSet2.getString("id");
					String sql3 = "update " + childTableName + " t set t.status = '已终结' where t.id ='"+childId+"'";
					statement3 = conn2.createStatement();
					statement3.executeUpdate(sql3);
					
					//获取【合同付款审批】中所有有效流程实例
					StringBuffer sbSql = new StringBuffer();
					sbSql.append("select b.FLOW_INSTANCE_ID ");
					sbSql.append("from tb_wf_todo b, ");
					sbSql.append(childTableName + " a, ");
					sbSql.append("jbpm_variableinstance c ");
					sbSql.append("where b.flag = '1' ");
					sbSql.append("and b.flow_instance_id = c.processinstance_ ");
					sbSql.append("and a.id = c.stringvalue_ ");
					sbSql.append("and c.name_='docid' ");
					sbSql.append("and a.id = '" + childId + "' ");
					
					statement4 = conn2.createStatement();
					resultSet4 = statement4.executeQuery(sbSql.toString());
					//遍历【合同付款审批】中的未结束流程, 设置待办为无效
					while(resultSet4.next()){
						String childPid = resultSet4.getString("FLOW_INSTANCE_ID");
						List<TaskToDo> lstTaskToDo = taskToDoManager.findBy("flowInstance", childPid);
						for (TaskToDo taskToDo : lstTaskToDo) {
							taskToDo.setFlag(false);
							taskToDoManager.save(taskToDo);
						}
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(resultSet2 != null){
					resultSet2.close();
				}
				if(resultSet4 != null){
					resultSet4.close();
				}
				if(statement2 != null){
					statement2.close();
				}
				if(statement3 != null){
					statement3.close();
				}
				if(statement4 != null){
					statement4.close();
				}
				if(conn2 != null){
					conn2.close();
				}
			}
		}
		this.outputJson("{success:true}");
		return NONE;
	}
	
}
