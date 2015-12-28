/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.fjcldgl.web;
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
import com.jteap.wz.fjcldgl.manager.FjcldManager;
import com.jteap.wz.fjcldgl.manager.FjcldmxManager;
import com.jteap.wz.fjcldgl.model.Fjcld;
import com.jteap.wz.fjcldgl.model.Fjcldmx;


@SuppressWarnings({"unchecked","serial"})
public class FjcldAction extends AbstractAction {
	
	private FjcldManager fjcldManager;
	
	private FjcldmxManager fjcldmxManager;

	public FjcldManager getFjcldManager() {
		return fjcldManager;
	}

	public void setFjcldManager(FjcldManager fjcldManager) {
		this.fjcldManager = fjcldManager;
	}

	public FjcldmxManager getFjcldmxManager() {
		return fjcldmxManager;
	}

	public void setFjcldmxManager(FjcldmxManager fjcldmxManager) {
		this.fjcldmxManager = fjcldmxManager;
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
			HqlUtil.addOrder(hql, "clrq", "desc");
		}
	}
	
	/**
	 * 删除废旧回收单以及明细
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Fjcld fjcld = null;
 		try {
			if (keys != null) {
				//删除明细表
				String fjdKeys[]=keys.split(","); 
				for(int i=0;i<fjdKeys.length;i++){
					fjcld = fjcldManager.get((Serializable)fjdKeys[i]);
					List<Fjcldmx> cldmx = fjcldmxManager.findBy("fjcld", fjcld);
					for(Fjcldmx mx:cldmx){
						fjcldmxManager.remove(mx);
					}
					//删除主表
					fjcldManager.remove(fjcld);
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
				List<Fjcld> clds = fjcldManager.findByIds(ids.split(","));
				for(Fjcld obj:clds){
					obj.setZt("1"); //调入生效、
					fjcldManager.save(obj);
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
		return fjcldManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"bh",
			"bm",
			"clrq",
			"zwy",
			"zg",
			"czr",
			"zt",
		"time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"bh",
				"bm",
				"clrq",
				"zwy",
				"zg",
				"czr",
				"zt",
			"time"};
	}
}
