/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.web;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.index.manager.MyQuickManager;
import com.jteap.index.model.MyQuick;

/**
 * 我的快捷Action
 * @author caihuiwen
 */
@SuppressWarnings({ "unchecked", "serial" })
public class MyQuickAction extends AbstractAction{
	
	private MyQuickManager myQuickManager;
	
	public void setMyQuickManager(MyQuickManager myQuickManager) {
		this.myQuickManager = myQuickManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return myQuickManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","personId","moduleIds"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","personId","moduleIds"
		};
	}
	
	/**
	 * 保存我的快捷设置
	 * @return
	 */
	public String saveMyQuickAction(){
		String moduleIds = request.getParameter("moduleIds");
		String personId = request.getParameter("personId");
		
		MyQuick myQuick = myQuickManager.findUniqueBy("personId", personId);
		if(myQuick == null){
			myQuick = new MyQuick();
			myQuick.setPersonId(personId);
		}
		myQuick.setModuleIds(moduleIds);
		myQuickManager.save(myQuick);
		
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
