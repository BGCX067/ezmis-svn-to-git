/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.web;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.index.manager.PreferenceManager;
import com.jteap.index.model.Preference;

/**
 * 个性化设置Action
 * @author caihuiwen
 */
@SuppressWarnings({ "unchecked", "serial" })
public class PreferenceAction extends AbstractAction{
	
	private PreferenceManager preferenceManager;
	
	public void setPreferenceManager(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return preferenceManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","personId","config"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","personId","config"
		};
	}
	
	/**
	 * 保存个人页面布局配置
	 * @return
	 */
	public String savePreferenceAction(){
		String personId = request.getParameter("personId");
		String config = request.getParameter("config");
		
		Preference preference = new Preference();
		
		String hql = "from Preference t where t.personId=?";
		List<Preference> list = preferenceManager.find(hql, personId);
		if(list.size() > 0){
			preference = list.get(0);
		}
		
		preference.setPersonId(personId);
		preference.setConfig(config);
		preferenceManager.save(preference);
		
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
