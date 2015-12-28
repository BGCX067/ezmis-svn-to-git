package com.jteap.system.iplock.support;

import java.util.List;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.iplock.manager.IPLockManager;
import com.jteap.system.iplock.model.IPLock;

public class IpLockSupport {
	public static List<IPLock> ipLocks = null;

	
	/**
	 * 初始化IP规则列表
	 */
	public static void initIpLockList(){
		IPLockManager ipLockManager = (IPLockManager) SpringContextUtil.getBean("ipLockManager");
		ipLocks = ipLockManager.getAll();
	}
	
}
