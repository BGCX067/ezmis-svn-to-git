/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.ckgl.web;
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
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.kwwh.manager.KwwhManager;
import com.jteap.wz.kwwh.model.Kw;




@SuppressWarnings({"serial","unchecked"})
public class CkglAction extends AbstractTreeAction<Ckgl> {
	private CkglManager ckglManager;
	
	private KwwhManager kwwhManager;
	
	
	public void setKwwhManager(KwwhManager kwwhManager) {
		this.kwwhManager = kwwhManager;
	}

	public CkglManager getCkglManager() {
		return ckglManager;
	}

	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}
	
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "ckbm", "asc");
		}
	}
	
	@Override
	public String removeAction() throws Exception {
		// TODO Auto-generated method stub
		String ckid = request.getParameter("ids");
		try{
			//判断该仓库下是否有库位
			List<Kw> kwList = kwwhManager.find("from Kw t where t.ckid = ?",ckid);
			if(kwList.size()>0){
				this.outputJson("{success:false,msg:'请先删除该仓库下的库位!'}");
			}else{
				this.outputJson("{success:true}");
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}

	/**
	 * 返回仓库名称
	 * @return
	 * @throws Exception 
	 */
	public String getCkmc() throws Exception{
		List<Ckgl> list = ckglManager.getAll();
		String[] json = {"id","ckmc"};
		String jsons1 = JSONUtil.listToJson(list, json);
		outputJson("{data:"+jsons1+"}");
		return null;
	}
	@Override
	public HibernateEntityDao getManager() {
		return ckglManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"ckgly",
			"userName",
			"person",
			"userLoginName",
			"ckbm",
			"bz",
			"ckmc",
			"person",
			"userLoginName",
			"id",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"ckgly",
			"ckbm",
			"bz",
			"ckmc",
			"id",
		""};
	}

	@Override
	protected Collection getChildren(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getParentPropertyName(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		// TODO Auto-generated method stub
		return ckglManager.findCkglList();
	}

	@Override
	protected String getSortNoPropertyName(Class arg0) {
		// TODO Auto-generated method stub
		return "id";
	}

	@Override
	protected String getTextPropertyName(Class arg0) {
		// TODO Auto-generated method stub
		return "ckmc";
	}
}
