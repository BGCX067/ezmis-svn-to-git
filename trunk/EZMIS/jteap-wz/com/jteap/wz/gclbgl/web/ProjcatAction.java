/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.gclbgl.web;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.wz.gclbgl.manager.ProjcatManager;
import com.jteap.wz.gclbgl.model.Projcat;
import com.jteap.wz.gcxmgl.manager.ProjManager;
import com.jteap.wz.gcxmgl.model.Proj;




@SuppressWarnings({"serial","unchecked"})
public class ProjcatAction extends AbstractTreeAction<Projcat> {
	private ProjcatManager projcatManager;
	private ProjManager projManager;
	
	public ProjcatManager getProjcatManager() {
		return projcatManager;
	}

	public void setProjcatManager(ProjcatManager projcatManager) {
		this.projcatManager = projcatManager;
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
			}else{
				hql.append(" where 1=1 ");
			}
			hql.append(" and obj.predefined is null");
			System.out.println(hql.toString());
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "projcatcode", "asc");
		}
	}
	
	/**
	 * 返回仓库名称
	 * @return
	 * @throws Exception 
	 */
	public String getCkmc() throws Exception{
		List<Projcat> list = projcatManager.findResourcesByProjcat();
		String[] json = {"id","projcatname"};
		String jsons1 = JSONUtil.listToJson(list, json);
		outputJson("{data:"+jsons1+"}");
		return null;
	}
	
	/**
	 * 删除操作，删除采购计划及明细
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Projcat projcat = null;
 		try {
			if (keys != null) {
				//删除明细表
				String cgjhKeys[]=keys.split(","); 
				for(int i=0;i<cgjhKeys.length;i++){
					projcat = projcatManager.get((Serializable)cgjhKeys[i]);
					List<Proj> projmx = projManager.findBy("projcat", projcat);
					for(Proj mx:projmx){
						//修改显示状态
						mx.setIsviable("0");
						projManager.save(mx);
					}
					//删除主表
					projcat.setPredefined("0");
					projcatManager.save(projcat);
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
		return projcatManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"projcatcode",
			"projcatname",
			"memo",
			"predefined"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"projcatcode",
				"projcatname",
				"memo",
				"predefined"
		};
	}

	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		return this.projcatManager.findResourcesByProjcat();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "projcatcode";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "projcatname";
	}

	public ProjManager getProjManager() {
		return projManager;
	}

	public void setProjManager(ProjManager projManager) {
		this.projManager = projManager;
	}
}
