/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.gcht.htzx.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.htzx.manager.HeTongBeiWangManager;
import com.jteap.gcht.htzx.manager.HeTongZhongJieManager;
import com.jteap.gcht.htzx.model.HeTongBeiWang;

/**
 * 合同备忘Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class HeTongBeiWangAction extends AbstractAction {
	
	private HeTongBeiWangManager heTongBeiWangManager;
	private HeTongZhongJieManager heTongZhongJieManager;
	
	public void setHeTongZhongJieManager(HeTongZhongJieManager heTongZhongJieManager) {
		this.heTongZhongJieManager = heTongZhongJieManager;
	}

	public void setHeTongBeiWangManager(HeTongBeiWangManager heTongBeiWangManager) {
		this.heTongBeiWangManager = heTongBeiWangManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return heTongBeiWangManager;
	}
	
	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"HTID","HTMC","HTBH","BWID","BWNR","BWSJ"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"HTID","HTMC","HTBH","BWID","BWNR","BWSJ"
		};
	}
	
	/**
	 * 查看审批状态不为作废、终结,【合同审批】中的合同备忘
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String showQcHtbwAction() throws Exception{
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		String tableName = request.getParameter("tableName");
		String htlx = request.getParameter("htlx");
		String currPersonId  = (String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
		try {
			String sql = "select a.id HTID,a.htmc HTMC,a.htbh HTBH,b.id BWID,b.bwnr BWNR,to_char(b.bwsj,'yyyy-MM-dd HH24:mi:ss') BWSJ from "+tableName
						+" a,tb_ht_htbw b where a.id=b.htid and a.status not in('已作废','已终结')";
			
			if(StringUtils.isNotEmpty(htlx)){
				sql += " and htlx='"+htlx+"'";
			}
			
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sql += " and " + hqlWhereTemp;
			}
			
			sql += " and b.bwrid='"+currPersonId+"'";
			sql += " order by a.htcjsj desc";
			
			Page page = heTongZhongJieManager.pagedQueryTableData(sql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());
			
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 保存合同备忘
	 * @return
	 */
	public String saveHtbwAction(){
		String htid = request.getParameter("htid");
		String htmc = request.getParameter("htmc");
		String htbh = request.getParameter("htbh");
		String bwid = request.getParameter("bwid");
		String bwnr = request.getParameter("bwnr");
		String currPersonId  = (String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
		String currPersonName = (String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSON_NAME);
		
		HeTongBeiWang heTongBeiWang = new HeTongBeiWang();
		if(StringUtils.isNotEmpty(bwid)){
			heTongBeiWang.setId(bwid);
		}
		heTongBeiWang.setHtid(htid);
		heTongBeiWang.setHtmc(htmc);
		heTongBeiWang.setHtbh(htbh);
		heTongBeiWang.setBwnr(bwnr);
		heTongBeiWang.setBwsj(new Date());
		heTongBeiWang.setBwr(currPersonName);
		heTongBeiWang.setBwrId(currPersonId);
		heTongBeiWangManager.save(heTongBeiWang);
		
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 查看审批状态不为作废、终结的合同名称、合同编号
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String findAllHtmcAction() throws Exception{
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String tableName = request.getParameter("tableName");
		try {
			String sql = "select a.id HTID,a.htmc HTMC,a.htbh HTBH,a.htlx HTLX,to_char(a.htcjsj,'yyyy-MM-dd') HTCJSJ,a.status STATUS from "+tableName+" a where a.status not in('已作废','已终结')";
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sql += " and " + hqlWhereTemp;
			}
			sql += " order by a.htcjsj desc";
			
			Page page = heTongZhongJieManager.pagedQueryTableData(sql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), new String[]{"HTID","HTMC","HTBH","HTLX","HTCJSJ","STATUS"});
			
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
}