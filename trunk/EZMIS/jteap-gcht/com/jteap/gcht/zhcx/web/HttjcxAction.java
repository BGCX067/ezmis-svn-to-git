package com.jteap.gcht.zhcx.web;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.htsp.model.Ht;
import com.jteap.gcht.zhcx.manager.HttjcxManager;
/**
 * 合同统计
 * @author 童贝
 * @date Feb 17, 2011
 */
@SuppressWarnings({"serial", "unchecked"})
public class HttjcxAction extends AbstractAction {
	private HttjcxManager httjcxManager;
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}
	
	/**
	 * 显示查询列表
	 */
	public String showListAction() throws Exception{
		String id=request.getParameter("id");
		if(StringUtils.isNotEmpty(id)){
			String tableName=this.httjcxManager.findTableById(id);
			Page page=this.getList(tableName);
			String json=JSONUtil.listToJson((List)page.getResult(),listJsonProperties());
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		}else{
			String[] tableNames=new String[]{Ht.WZHT_TABLE,Ht.RLHT_TABLE,Ht.GCHT_TABLE,Ht.CWHT_TABLE};
			List list=new ArrayList();
			for(String tableName:tableNames){
				Page page=this.getList(tableName);
				list.addAll((List)page.getResult());
			}
			String json=JSONUtil.listToJson(list,listJsonProperties());
			json = "{totalCount:'" + list.size() + "',list:" + json + "}";
			this.outputJson(json);
		}
		return NONE;
	}
	
	/**
	 * 根据表名得到表信息和流程信息
	 * 作者:童贝
	 * 时间:Feb 17, 2011
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Page getList(String tableName) throws Exception{
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);

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
		
		Page page = this.httjcxManager.pagedQueryTableData(sql, iStart, iLimit);
		return page;
	}

	@Override
	public String[] listJsonProperties() {
	    return new String[]{
				"ID_", "VERSION_", "START_", "END_","PROCESSINSTANCE_","PROCESS_DATE", "FLOW_NAME", "FLOW_CONFIG_ID","FLOW_FORM_ID",
				"id","htmc","htbh","htlx","fyxz","status","htcjsj"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","htmc","htbh","htlx","fyxz","status","htcjsj","time"};
	}

	public HttjcxManager getHttjcxManager() {
		return httjcxManager;
	}

	public void setHttjcxManager(HttjcxManager httjcxManager) {
		this.httjcxManager = httjcxManager;
	}

}
