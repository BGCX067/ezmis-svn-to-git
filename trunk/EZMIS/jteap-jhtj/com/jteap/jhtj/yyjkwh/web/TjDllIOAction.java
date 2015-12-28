package com.jteap.jhtj.yyjkwh.web;


import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.yyjkwh.manager.TjDllIOManager;
@SuppressWarnings({ "unchecked", "serial" })
public class TjDllIOAction extends AbstractAction {

	private TjDllIOManager tjDllIOManager;
	@Override
	public HibernateEntityDao getManager() {
		return tjDllIOManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","dname","dcname","dms","dorder"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","dname","dcname","dms","dorder"};
	}

	public TjDllIOManager getTjDllIOManager() {
		return tjDllIOManager;
	}

	public void setTjDllIOManager(TjDllIOManager tjDllIOManager) {
		this.tjDllIOManager = tjDllIOManager;
	}
	
}
