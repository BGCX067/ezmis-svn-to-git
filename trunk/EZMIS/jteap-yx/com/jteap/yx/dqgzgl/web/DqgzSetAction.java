/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.dqgzgl.manager.DqgzHandleManager;
import com.jteap.yx.dqgzgl.manager.DqgzSetManager;
import com.jteap.yx.dqgzgl.model.DqgzHandle;
import com.jteap.yx.dqgzgl.model.DqgzSet;

/**
 * 定期工作Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class DqgzSetAction extends AbstractAction {
	
	private DqgzSetManager dqgzSetManager;
	private DqgzHandleManager dqgzHandleManager;
	
	public void setDqgzHandleManager(DqgzHandleManager dqgzHandleManager) {
		this.dqgzHandleManager = dqgzHandleManager;
	}

	public void setDqgzSetManager(DqgzSetManager dqgzSetManager) {
		this.dqgzSetManager = dqgzSetManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return dqgzSetManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","dqgzCatalogId","fzbm","fzgw","gzgl","dqgzzy","bc","dqgzMc","dqgzNr","dqgzFl","dqgzCreateDt"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","dqgzCatalogId","fzbm","fzgw","gzgl","dqgzzy","bc","dqgzMc","dqgzNr","dqgzFl","dqgzCreateDt"
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
		
		// 添加查询分类条件
		String catalogId = request.getParameter("catalogId");
		if (StringUtils.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "dqgzCatalogId", catalogId);
		}
		
		HqlUtil.addOrder(hql, "obj.dqgzCreateDt", "DESC");
	}
	
	/**
	 * 保存或修改定期工作设置
	 * @return
	 */
	public String saveOrUpdateAction(){
		try {
			DqgzSet dqgzSet = new DqgzSet();
			String id = request.getParameter("id");
			String dqgzCatalogId = request.getParameter("dqgzCatalogId");
			if(StringUtil.isNotEmpty(id)){
				dqgzSet.setId(id);
			}
			if(StringUtil.isNotEmpty(dqgzCatalogId)){
				dqgzSet.setDqgzCatalogId(dqgzCatalogId);
			}
			dqgzSet.setFzbm(request.getParameter("fzbm"));
			dqgzSet.setFzgw(request.getParameter("fzgw"));
			dqgzSet.setGzgl(request.getParameter("gzgl"));
			dqgzSet.setDqgzzy(request.getParameter("dqgzzy"));
			dqgzSet.setBc(request.getParameter("bc"));
			dqgzSet.setDqgzNr(request.getParameter("dqgzNr"));
			dqgzSet.setDqgzCreateDt(new Date());
			dqgzSetManager.save(dqgzSet);
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 手工发送
	 * @return
	 */
	public String hanldSendAction(){
		try {
			String ids = request.getParameter("ids");
			String[] idsArray = ids.split(",");
			for (int i = 0; i < idsArray.length; i++) {
				//获取定期工作设置
				DqgzSet dqgzSet = dqgzSetManager.get(idsArray[i]);
				//给定期工作处理赋值
				DqgzHandle dqgzHandle = new DqgzHandle();
				dqgzHandle.setDqgzSetId(dqgzSet.getId());
				dqgzHandle.setFzbm(dqgzSet.getFzbm());
				dqgzHandle.setFzgw(dqgzSet.getFzgw());
				dqgzHandle.setGzgl(dqgzSet.getGzgl());
				dqgzHandle.setDqgzzy(dqgzSet.getDqgzzy());
				dqgzHandle.setBc(dqgzSet.getBc());
				dqgzHandle.setDqgzNr(dqgzSet.getDqgzNr());
				dqgzHandle.setDqgzCreateDt(new Date());
				dqgzHandle.setStatus("未完成");
				
				dqgzHandleManager.hanldSend_saveDeals(dqgzHandle);
			}
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
