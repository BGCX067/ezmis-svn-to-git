/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.fjhsdgl.web;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.fjhsdgl.manager.FjhsdManager;
import com.jteap.wz.fjhsdgl.manager.FjhsdmxManager;
import com.jteap.wz.fjhsdgl.model.Fjhsd;
import com.jteap.wz.fjhsdgl.model.Fjhsdmx;




@SuppressWarnings({"unchecked","serial"})
public class FjhsdAction extends AbstractAction {
	private FjhsdManager fjhsdManager;
	private FjhsdmxManager fjhsdmxManager;
	
	public FjhsdManager getFjhsdManager() {
		return fjhsdManager;
	}

	public void setFjhsdManager(FjhsdManager fjhsdManager) {
		this.fjhsdManager = fjhsdManager;
	}
	
	
	
	public FjhsdmxManager getFjhsdmxManager() {
		return fjhsdmxManager;
	}

	public void setFjhsdmxManager(FjhsdmxManager fjhsdmxManager) {
		this.fjhsdmxManager = fjhsdmxManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String zt = "0";
			zt = request.getParameter("zt");
			HqlUtil.addCondition(hql, "zt",zt);
			
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "hsrq", "desc");
		}
	}
	
	/**
	 * 删除废旧回收单以及明细
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Fjhsd fjhsd = null;
 		try {
			if (keys != null) {
				//删除明细表
				String fjdKeys[]=keys.split(","); 
				for(int i=0;i<fjdKeys.length;i++){
					fjhsd = fjhsdManager.get((Serializable)fjdKeys[i]);
					List<Fjhsdmx> hsdmx = fjhsdmxManager.findBy("fjhsd", fjhsd);
					for(Fjhsdmx mx:hsdmx){
						fjhsdmxManager.remove(mx);
					}
					//删除主表
					fjhsdManager.remove(fjhsd);
				}
				outputJson("{success:true}");
			}
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	
	/**
	 * 生效
	 */
	public String enable() throws Exception{
		String ids = request.getParameter("ids");
		try {
			if(StringUtil.isNotEmpty(ids)) {
				List<Fjhsd> hsds = fjhsdManager.findByIds(ids.split(","));
				for(Fjhsd obj:hsds){
					obj.setZt("1"); //调入生效、
					fjhsdManager.save(obj);
				}
				outputJson("{success:true}");
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			e.printStackTrace();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	@Override
	public HibernateEntityDao getManager() {
		return fjhsdManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"hsrq",
			"czr",
			"hsdh",
			"sjbm",
			"sjr",
			"zt",
			"jsr",
		"time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"hsrq",
			"czr",
			"hsdh",
			"sjbm",
			"sjr",
			"zt",
			"jsr",
		"time"};
	}
}
