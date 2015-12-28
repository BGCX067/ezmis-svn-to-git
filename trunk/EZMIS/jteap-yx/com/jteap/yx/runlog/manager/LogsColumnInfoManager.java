/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.yx.runlog.model.LogsColumnInfo;

/**
 * 运行日志指标Manager
 * @author caihuiwen
 */
public class LogsColumnInfoManager extends HibernateEntityDao<LogsColumnInfo>{
	
	/**
	 * 根据表Id查询字段列表 order by sortno asc 
	 * @param tableId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LogsColumnInfo> findByTableId(String tableId){
		String hql = "from LogsColumnInfo t where t.tableId=? order by sortno asc";
		List<LogsColumnInfo> list = this.find(hql, tableId);
		return list;
	}
	
}
