/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.runlog.manager.LogsTableInfoManager;
import com.jteap.yx.runlog.manager.PhysicLogsManager;
import com.jteap.yx.runlog.model.LogsTableInfo;

/**
 * 运行日志报表Action
 * @author caihuiwen
 */
@SuppressWarnings({"serial","unchecked"})
public class LogsTableInfoAction extends AbstractAction {
	
	private LogsTableInfoManager logsTableInfoManager;
	private PhysicLogsManager physicLogsManager;
	
	public void setLogsTableInfoManager(LogsTableInfoManager logsTableInfoManager) {
		this.logsTableInfoManager = logsTableInfoManager;
	}
	
	public void setPhysicLogsManager(PhysicLogsManager physicLogsManager) {
		this.physicLogsManager = physicLogsManager;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return logsTableInfoManager;
	}
	
	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","logCatalogId","tableCode","logCatalogName",
			"tableName","sortno","caiyangdian","remark"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","logCatalogId","tableCode","logCatalogName",
			"tableName","sortno","caiyangdian","remark"
		};
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		// 添加查询条件
		String catalogId = request.getParameter("catalogId");
		if (StringUtils.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "logCatalogId", catalogId);
		}
		
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", " ");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		
		HqlUtil.addOrder(hql, "obj.sortno", "asc");
	}
	
	/**
	 * 保存或修改 返回表ID
	 * @return
	 */
	public String saveOrUpdateAction(){
		try {
			String id = request.getParameter("id");
			LogsTableInfo tableInfo = new LogsTableInfo();
			if(id != null && !id.trim().equals("")){
				tableInfo.setId(id);
			}
			tableInfo.setLogCatalogId(request.getParameter("logCatalogId"));
			tableInfo.setTableCode(request.getParameter("tableCode"));
			tableInfo.setLogCatalogName(request.getParameter("logCatalogName"));
			tableInfo.setTableName(request.getParameter("tableName"));
			String sortno = request.getParameter("sortno");
			if(sortno != null){
				tableInfo.setSortno(Integer.parseInt(sortno));
			}else {
				tableInfo.setSortno(0);
			}
			tableInfo.setCaiyangdian(request.getParameter("caiyangdian"));
			tableInfo.setRemark(request.getParameter("remark"));
			logsTableInfoManager.save(tableInfo);
			
			this.outputJson("{success:true,id:'"+tableInfo.getId()+"'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
	/**
	 * 根据表编号、采样点重建对应的记录物理表
	 * @return
	 */
	public String rebuildPhysicLogTable(){
		String tableCode = request.getParameter("tableCode");
		String caiyangdian = request.getParameter("caiyangdian");
		
		String schema = SystemConfig.getProperty("jdbc.schema");
		String dropSql = "DROP TABLE " + schema + ".TB_YX_LOG_" + tableCode + " ";
		StringBuffer createSql = new StringBuffer("CREATE TABLE " + schema + ".TB_YX_LOG_" + tableCode + "(\r\n");
		createSql.append("ID VARCHAR2(32),\r\n");
		createSql.append("ROW_ID VARCHAR2(32),\r\n");
		createSql.append("PERSON_ID VARCHAR2(32),\r\n");
		createSql.append("ZBSJ TIMESTAMP(6),\r\n");
		createSql.append("FILL_DATE TIMESTAMP(6),\r\n");
		createSql.append("SUM_BAN_0 VARCHAR2(100),\r\n");
		createSql.append("AVG_BAN_0 VARCHAR2(100),\r\n");
		createSql.append("SUM_BAN_1 VARCHAR2(100),\r\n");
		createSql.append("AVG_BAN_1 VARCHAR2(100),\r\n");
		createSql.append("SUM_BAN_2 VARCHAR2(100),\r\n");
		createSql.append("AVG_BAN_2 VARCHAR2(100),\r\n");
		createSql.append("SUM_DAY VARCHAR2(100),\r\n");
		createSql.append("AVG_DAY VARCHAR2(100),\r\n");
		createSql.append("SUM_MONTH VARCHAR2(100),\r\n");
		createSql.append("AVG_MONTH VARCHAR2(100),\r\n");

		//遍历采样点
		String[] caiyangArray = caiyangdian.split(",");
		for (int i = 0; i < caiyangArray.length; i++) {
			//生成的 记录物理表字段名为   VALUE + 采样点    
			createSql.append("VALUE_" + caiyangArray[i] + " VARCHAR2(100),\r\n");
		}
		
		createSql.delete(createSql.length()-3, createSql.length());
		createSql.append("\r\n )");
		
		String alterSql = "\r\n alter table " + schema + ".TB_YX_LOG_" + tableCode 
						+ " add constraint PK_ID"+ UUIDGenerator.javaId() + " primary key (ID)";
		
		//重建日志记录物理表
		physicLogsManager.rebuildPhysicLogTable(dropSql.toString(), createSql.toString(), alterSql);
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
