/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.tzmgl.web;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.tzmgl.manager.TjbmManager;
import com.jteap.wz.tzmgl.model.Tjbm;




@SuppressWarnings({"serial","unchecked"})
public class TjbmAction extends AbstractAction {
	private TjbmManager tjbmManager;

	public TjbmManager getTjbmManager() {
		return tjbmManager;
	}

	public void setTjbmManager(TjbmManager tjbmManager) {
		this.tjbmManager = tjbmManager;
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
			HqlUtil.addOrder(hql, "wzbm", "asc");
		}
	}
	
	/**
	 * 删除操作
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Tjbm tjbm = null;
 		try {
			if (keys != null) {
				//删除明细表
				String cgjhKeys[]=keys.split(","); 
				for(int i=0;i<cgjhKeys.length;i++){
					tjbm = tjbmManager.get((Serializable)cgjhKeys[i]);
					tjbmManager.remove(tjbm);
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
		return tjbmManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"wzbm",
			"wzbmlr"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"wzbm",
				"wzbmlr"
		};
	}
}
