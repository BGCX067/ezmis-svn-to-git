package com.jteap.jx.dxxgl.web;

import java.util.Date;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.dxxgl.manager.GyxtjxjhJlManager;
import com.jteap.jx.dxxgl.model.GyxtjxjhJl;

/**
 * 公用系统检修计划-标记action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class GyxtjxjhJlAction extends AbstractAction {

	private GyxtjxjhJlManager gyxtjxjhJlManager;
	
	/**
	 * 
	 * 描述 : 保存
	 * 作者 : wangyun
	 * 时间 : Aug 20, 2010
	 * 异常 : Exception
	 * 
	 */
	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String sbid = request.getParameter("sbid");
		String jhnf = request.getParameter("jhnf");
		String month = request.getParameter("month");
		String jlnr = request.getParameter("jlnr");
		
		GyxtjxjhJl gyxtjxjhJl = null;
		if (StringUtil.isEmpty(id)) {
			gyxtjxjhJl = new GyxtjxjhJl();
		} else {
			gyxtjxjhJl = gyxtjxjhJlManager.get(id);
		}
		
		gyxtjxjhJl.setSbid(sbid);
		gyxtjxjhJl.setJhnf(jhnf);
		gyxtjxjhJl.setJlyf(month);
		gyxtjxjhJl.setJlnr(jlnr);
		gyxtjxjhJl.setJlr((String)sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME));
		gyxtjxjhJl.setJlsj(new Date());
		gyxtjxjhJlManager.save(gyxtjxjhJl);
		this.outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 
	 * 描述 : 显示标记
	 * 作者 : wangyun
	 * 时间 : Aug 20, 2010
	 * 异常 : Exception
	 * 
	 */
	public String showUpdateAction() throws Exception {
		String sbid = request.getParameter("sbid");
		String jhnf = request.getParameter("jhnf");
		String month = request.getParameter("month");
		
		try {
			GyxtjxjhJl gyxtjxjhJl = gyxtjxjhJlManager.findByGyxtjxjh(sbid, month, jhnf);
			if (gyxtjxjhJl != null) {
				outputJson("{success:true,id:'"+gyxtjxjhJl.getId()+"',jlnr:'"+(gyxtjxjhJl.getJlnr()!=null?gyxtjxjhJl.getJlnr():"")+"'}");
			} else {
				outputJson("{success:false}");
			}
		} catch (Exception e) {
			outputJson("{success:false}");
			e.printStackTrace();
		}
		return NONE;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return gyxtjxjhJlManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public GyxtjxjhJlManager getGyxtjxjhJlManager() {
		return gyxtjxjhJlManager;
	}

	public void setGyxtjxjhJlManager(GyxtjxjhJlManager gyxtjxjhJlManager) {
		this.gyxtjxjhJlManager = gyxtjxjhJlManager;
	}

}
