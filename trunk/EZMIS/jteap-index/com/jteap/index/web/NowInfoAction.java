/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.web;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.index.manager.NowInfoManager;

/**
 * 实时信息设置Action
 * @author caihuiwen
 */
@SuppressWarnings({ "unchecked", "serial" })
public class NowInfoAction extends AbstractAction{
	
	private NowInfoManager nowInfoManager;
	
	public void setNowInfoManager(NowInfoManager nowInfoManager) {
		this.nowInfoManager = nowInfoManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}
	
	/**
	 * 获取机组状态
	 */
	public String getJzStatusAction(){
		String sql = "select * from tb_sys_nowinfo t";
		try {
			List list = nowInfoManager.querySqlData(sql);
			String jsonList = JSONUtil.listToJson(list);
			this.outputJson("{success:true,list:"+jsonList+"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 设置机组状态
	 * @return
	 */
	public String setJzStatusAction(){
		String status1 = request.getParameter("status1");
		String runDay1 = request.getParameter("runDay1");
		String status2 = request.getParameter("status2");
		String runDay2 = request.getParameter("runDay2");
		String status3 = request.getParameter("status3");
		String runDay3 = request.getParameter("runDay3");
		String status4 = request.getParameter("status4");
		String runDay4= request.getParameter("runDay4");
		
		String sql = "update tb_sys_nowinfo set zt_bc = status||run_day, status='"+status1+"',run_day="+runDay1+" where jizu='1';";
		sql += "update tb_sys_nowinfo set zt_bc = status||run_day, status='"+status2+"',run_day="+runDay2+" where jizu='2';";
		sql += "update tb_sys_nowinfo set zt_bc = status||run_day, status='"+status3+"',run_day="+runDay3+" where jizu='3';";
		sql += "update tb_sys_nowinfo set zt_bc = status||run_day ,status='"+status4+"',run_day="+runDay4+" where jizu='4';";
		try {
			nowInfoManager.executeSqlBatch(sql);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
