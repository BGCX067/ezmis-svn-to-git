package com.jteap.jx.dxxgl.web;

import java.util.Date;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.dxxgl.manager.GyxtjxjhBjManager;
import com.jteap.jx.dxxgl.model.GyxtjxjhBj;

/**
 * 公用检修计划-标记action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class GyxtjxjhBjAction extends AbstractAction {

	private GyxtjxjhBjManager gyxtjxjhBjManager;

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
		String bjys = request.getParameter("bjys");
		String bjtb = request.getParameter("bjtb");
		
		GyxtjxjhBj gyxtjxjhBj = null;
		if (StringUtil.isEmpty(id)) {
			gyxtjxjhBj = new GyxtjxjhBj();
		} else {
			gyxtjxjhBj = gyxtjxjhBjManager.get(id);
		}
		
		gyxtjxjhBj.setSbid(sbid);
		gyxtjxjhBj.setJhnf(jhnf);
		gyxtjxjhBj.setBjyf(month);
		gyxtjxjhBj.setBjys(bjys);
		gyxtjxjhBj.setBjtb(bjtb);
		gyxtjxjhBj.setBjr((String)sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME));
		gyxtjxjhBj.setBjsj(new Date());
		gyxtjxjhBjManager.save(gyxtjxjhBj);
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
		
		GyxtjxjhBj gyxtjxjhBj = gyxtjxjhBjManager.findByGyxtjxjh(sbid, month, jhnf);
		if (gyxtjxjhBj != null) {
			String bjys = gyxtjxjhBj.getBjys();
			String bjtb = gyxtjxjhBj.getBjtb();
			String id = gyxtjxjhBj.getId();
			outputJson("{success:true,id:'"+id+"',bjys:'"+bjys+"',bjtb:'"+bjtb+"'}");
		} else {
			outputJson("{success:false}");
		}
		return NONE;
	}

	@Override
	public HibernateEntityDao getManager() {
		return gyxtjxjhBjManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public GyxtjxjhBjManager getGyxtjxjhBjManager() {
		return gyxtjxjhBjManager;
	}

	public void setGyxtjxjhBjManager(GyxtjxjhBjManager gyxtjxjhBjManager) {
		this.gyxtjxjhBjManager = gyxtjxjhBjManager;
	}

}
