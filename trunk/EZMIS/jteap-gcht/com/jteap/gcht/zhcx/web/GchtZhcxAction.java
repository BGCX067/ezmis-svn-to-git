package com.jteap.gcht.zhcx.web;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.zhcx.manager.GchtZhcxManager;

/**
 * 综合查询Action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unused", "unchecked"})
public class GchtZhcxAction extends AbstractAction {
	
	private GchtZhcxManager gchtZhcxManager;
	
	/**
	 * 
	 * 描述 : 获得合同执行情况统计
	 * 作者 : wangyun
	 * 时间 : 2010-12-9
	 * 异常 : Exception
	 * 
	 */
	public String getZxntjAction() throws Exception {
		String tjnf = request.getParameter("tjnf");
		String tableName = request.getParameter("tableName");
		try {
			String data = gchtZhcxManager.getZxntj(tjnf, tableName);
			String json = "{success:true,data:[{"+data+"}]}";
			outputJson(json);
		} catch (Exception e) {
			outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 获得燃料执行情况统计
	 * 作者 : wangyun
	 * 时间 : 2010-12-9
	 * 异常 : Exception
	 * 
	 */
	public String getRlZxntjAction() throws Exception {
		String tjnf = request.getParameter("tjnf");
		try {
			String data = gchtZhcxManager.getRlZxntj(tjnf);
			String json = "{success:true,data:[{"+data+"}]}";
			outputJson(json);
		} catch (Exception e) {
			outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
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

	public GchtZhcxManager getGchtZhcxManager() {
		return gchtZhcxManager;
	}

	public void setGchtZhcxManager(GchtZhcxManager gchtZhcxManager) {
		this.gchtZhcxManager = gchtZhcxManager;
	}

}
