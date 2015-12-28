/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.runlog.manager.LogsColumnInfoManager;

/**
 * 运行日志指标Action
 * @author caihuiwen
 */
@SuppressWarnings({"serial","unchecked"})
public class LogsColumnInfoAction extends AbstractAction {
	
	private LogsColumnInfoManager logsColumnInfoManager;
	
	public void setLogsColumnInfoManager(LogsColumnInfoManager logsColumnInfoManager) {
		this.logsColumnInfoManager = logsColumnInfoManager;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return logsColumnInfoManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","tableId","columnCode","columnName","edingzhi",
			"jiliangdanwei","sisCedianbianma","sortno","dataTableCode","jizu"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","tableId","columnCode","columnName","edingzhi",
			"jiliangdanwei","sisCedianbianma","sortno","dataTableCode","jizu"
		};
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", " ");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		
		// 添加查询条件
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			HqlUtil.addCondition(hql, "tableId", tableId);
		}
		HqlUtil.addOrder(hql, "obj.sortno", "asc");
	}
	
}
