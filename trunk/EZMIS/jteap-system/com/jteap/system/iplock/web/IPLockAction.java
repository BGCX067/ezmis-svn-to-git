package com.jteap.system.iplock.web;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.iplock.manager.IPLockManager;
import com.jteap.system.iplock.support.IpLockSupport;

/**
 * 自定义表单动作对象
 * 
 * @author tanchang
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class IPLockAction extends AbstractAction {

	private IPLockManager ipLockManager;
	
	public IPLockManager getIpLockManager() {
		return ipLockManager;
	}

	public void setIpLockManager(IPLockManager ipLockManager) {
		this.ipLockManager = ipLockManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return ipLockManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","startIp","endIp","comm","rule"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","startIp","endIp","comm","rule"};
	}

	@Override
	public String saveUpdateAction() throws BusinessException {
		String result =  super.saveUpdateAction();
		IpLockSupport.ipLocks = null;
		return result;
	}

	@Override
	public String removeAction() throws Exception {
		String result =  super.removeAction();
		IpLockSupport.ipLocks = null;
		return result;
	}
	
	
	
	
	
}
