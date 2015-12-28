/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.fdzbfxykh.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.yx.fdzbfxykh.model.DirectiveColumnInfo;

/**
 * 小指标字段定义Manager
 * @author caihuiwen
 */
public class DirectiveColumnInfoManager extends HibernateEntityDao<DirectiveColumnInfo>{

	/**
	 * 根据指标表Id查询字段列表 order by sortno asc 
	 * @param tableId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DirectiveColumnInfo> findByTableId(String directiveId){
		String hql = "from DirectiveColumnInfo t where t.directiveId=? order by sortno asc";
		List<DirectiveColumnInfo> list = this.find(hql, directiveId);
		return list;
	}
	
}
