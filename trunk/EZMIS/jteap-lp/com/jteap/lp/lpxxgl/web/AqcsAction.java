/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.lp.lpxxgl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.lp.lpxxgl.manager.AqcsCatalogManager;
import com.jteap.lp.lpxxgl.manager.AqcsManager;
import com.jteap.lp.lpxxgl.model.Aqcs;
import com.jteap.lp.lpxxgl.model.AqcsCatalog;

/**
 * 安全措施Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unused", "unchecked", "serial" })
public class AqcsAction extends AbstractAction {
	private AqcsManager aqcsManager;
	private AqcsCatalogManager aqcsCatalogManager;

	public AqcsManager getAqcsManager() {
		return aqcsManager;
	}

	public void setAqcsManager(AqcsManager aqcsManager) {
		this.aqcsManager = aqcsManager;
	}

	public AqcsCatalogManager getAqcsCatalogManager() {
		return aqcsCatalogManager;
	}

	public void setAqcsCatalogManager(AqcsCatalogManager aqcsCatalogManager) {
		this.aqcsCatalogManager = aqcsCatalogManager;
	}

	/**
	 * 
	 * 描述 : 保存或修改安全措施
	 * 作者 : wangyun
	 * 时间 : 2010-10-9
	 * 异常 : Exception
	 */
	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String flid = request.getParameter("flid");
		String csnr = request.getParameter("csnr");
		String csmc = request.getParameter("csmc");
		
		try {
			Aqcs aqcs = null;
			if (StringUtil.isEmpty(id)) {
				aqcs = new Aqcs();
			} else {
				aqcs = aqcsManager.get(id);
			}
			AqcsCatalog aqcsCatalog = aqcsCatalogManager.get(flid);
			aqcs.setAqcsCatalog(aqcsCatalog);
			aqcs.setCsmc(csmc);
			aqcs.setCsnr(csnr);
			aqcsManager.save(aqcs);
			outputJson("{success:true,id:'"+aqcs.getId()+"'}");
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		return NONE;
	}
	
	
	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String catalogId =  request.getParameter("catalogId");
		if (StringUtil.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "aqcsCatalog.id", catalogId);
		}
		String queryParamsSql = request.getParameter("queryParamsSql");
		if (StringUtil.isNotEmpty(queryParamsSql)) {
			HqlUtil.addWholeCondition(hql, queryParamsSql);
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return aqcsManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "flid", "csmc", "csnr", "aqcsCatalog", "flmc", "id" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "flid", "csmc", "csnr", "aqcsCatalog", "flmc", "id" };
	}
}
