/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.slgl.web;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.slgl.manager.SlglManager;
import com.jteap.wz.slgl.model.Slgl;




@SuppressWarnings({"serial","unchecked"})
public class SlglAction extends AbstractAction {
	private SlglManager slglManager;

	public SlglManager getSlglManager() {
		return slglManager;
	}

	public void setSlglManager(SlglManager slglManager) {
		this.slglManager = slglManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "bh", "asc");
		}
	}
	
	/**
	 * 删除操作
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Slgl slgl = null;
 		try {
			if (keys != null) {
				//删除明细表
				String cgjhKeys[]=keys.split(","); 
				for(int i=0;i<cgjhKeys.length;i++){
					slgl = slglManager.get((Serializable)cgjhKeys[i]);
					slglManager.remove(slgl);
				}
				outputJson("{success:true}");
			}
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return slglManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"bh",
			"c_name",
			"n_value"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"bh",
				"c_name",
				"n_value"
		};
	}
}
