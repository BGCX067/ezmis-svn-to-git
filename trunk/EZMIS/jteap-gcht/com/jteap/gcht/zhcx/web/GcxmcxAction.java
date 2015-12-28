/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.zhcx.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.zhcx.manager.GcxmcxManager;

/**
 * 工程项目查询Action
 * @author caihuiwen
 */
@SuppressWarnings({"serial", "unchecked"})
public class GcxmcxAction extends AbstractAction{
	
	private GcxmcxManager gcxmcxManager;
	
	public void setGcxmcxManager(GcxmcxManager gcxmcxManager) {
		this.gcxmcxManager = gcxmcxManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","xmbh","xmmc","xmlx","sqbm","cbfs","gcys","status","cjsj"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
		};
	}
	
	/**
	 * 工程项目查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryGcxmAction() throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		String sqlWhere = request.getParameter("queryParamsSql");
		//一般立项单
		List<Map<String, String>> ybList = gcxmcxManager.findYbOrWtXm("tb_ht_yblxd", sqlWhere);
		//委托单
		List<Map<String, String>> wtList = gcxmcxManager.findYbOrWtXm("tb_ht_wtd", sqlWhere);
		
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.addAll(ybList);
		list.addAll(wtList);
		if(iLimit > list.size()){
			iLimit = list.size();
		}
		
		String json = JSONUtil.listToJson(list.subList(iStart, iLimit), listJsonProperties());
		json = "{totalCount:'" + list.size() + "',list:" + json + "}";
		this.outputJson(json);
		return NONE;
	}

}
