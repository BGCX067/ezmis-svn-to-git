/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.jjb.web;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.jjb.manager.PaiBanManager;
import com.jteap.yx.jjb.model.PaiBan;

/**
 * 排班Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class PaiBanAction extends AbstractAction {
	
	private PaiBanManager paiBanManager;
	
	public void setPaiBanManager(PaiBanManager paiBanManager) {
		this.paiBanManager = paiBanManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return paiBanManager;
	}
	
	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","bc","zb","cssj"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","bc","zb","cssj"
		};
	}
	
	/**
	 * 根据班次获取值别信息
	 * @return
	 */
	public String findByBcAction(){
		//交班班次
		String jiaobbc = request.getParameter("jiaobbc");
		//接班班次
		String jiebbc = request.getParameter("jiebbc");
		
		try {
			String json = "{success:true,jiaobzb:'',jiebzb:''}";
			//交班班次的 排班信息
			List<PaiBan> jiaobList = paiBanManager.findBy("bc", jiaobbc);
			//接班班次的 排班信息
			List<PaiBan> jiebList = paiBanManager.findBy("bc", jiebbc);
			
			if(jiaobList.size() > 0 && jiebList.size() > 0){
				json = "{success:true,jiaobzb:'" + jiaobList.get(0).getZb() +
				"',jiebzb:'" + jiebList.get(0).getZb() + "'}";
			}
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 保存排班信息
	 * @return
	 */
	public String saveAction(){ 
		String baiResult = request.getParameter("baiResult");
		String zhongResult = request.getParameter("zhongResult");
		String yeResult = request.getParameter("yeResult");
		
		PaiBan yeBan = paiBanManager.findUniqueBy("bc", "夜班");
		PaiBan baiBan = paiBanManager.findUniqueBy("bc", "白班");
		PaiBan zhongBan = paiBanManager.findUniqueBy("bc", "中班");
		
		yeBan.setZb(yeResult);
		baiBan.setZb(baiResult);
		zhongBan.setZb(zhongResult);
		
		paiBanManager.save(baiBan);
		paiBanManager.save(zhongBan);
		paiBanManager.save(yeBan);
		
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}