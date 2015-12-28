package com.jteap.jx.bpbjgl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.bpbjgl.manager.BpbjZyflManager;
import com.jteap.jx.bpbjgl.manager.BpbjxxManager;
import com.jteap.jx.bpbjgl.model.BpbjZyfl;
import com.jteap.jx.bpbjgl.model.Bpbjxx;

/**
 * 备品备件信息action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class BpbjxxAction extends AbstractAction {

	private BpbjxxManager bpbjxxManager;
	private BpbjZyflManager bpbjZyflManager;

	/**
	 * 
	 * 描述 : 保存备品备件信息
	 * 作者 : wangyun
	 * 时间 : Aug 16, 2010
	 * 异常 : Exception
	 */
	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String zyflId = request.getParameter("zyflId");
		String sbbm = request.getParameter("sbbm");
		String sbmc = request.getParameter("sbmc");
		String xsjgg = request.getParameter("xsjgg");
		String dw = request.getParameter("dw");
		String edsl = request.getParameter("edsl");
		String sjsl = request.getParameter("sjsl");
		String yjsl = request.getParameter("yjsl");
		String remark = request.getParameter("remark");
		
		try {
			Bpbjxx bpbjxx = null;
			if (StringUtil.isEmpty(id)) {
				bpbjxx = new Bpbjxx();
				BpbjZyfl bpbjZyfl = bpbjZyflManager.get(zyflId);
				bpbjxx.setBpbjZyfl(bpbjZyfl);
			} else {
				bpbjxx = bpbjxxManager.get(id);
			}
			
			bpbjxx.setSbbm(sbbm);
			bpbjxx.setSbmc(sbmc);
			bpbjxx.setXsjgg(xsjgg);
			bpbjxx.setDw(dw);
			if (StringUtil.isNotEmpty(edsl)) {
				bpbjxx.setEdsl(Long.parseLong(edsl));
			}
			if (StringUtil.isNotEmpty(sjsl)) {
				bpbjxx.setSjsl(Long.parseLong(sjsl));
			}
			if (StringUtil.isNotEmpty(yjsl)) {
				bpbjxx.setYjsl(Long.parseLong(yjsl));
			}
			bpbjxx.setRemark(remark);
			bpbjxxManager.save(bpbjxx);
			outputJson("{success:true,id:'"+bpbjxx.getId()+"'}");
		} catch (Exception e) {
			outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			e.printStackTrace();
		}
		return NONE;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String zyflId = request.getParameter("zyflId");
		if (StringUtil.isNotEmpty(zyflId)) {
			BpbjZyfl bpbjZyfl = bpbjZyflManager.get(zyflId);
			if (bpbjZyfl.getBpbjZyfls().size() > 0) {
				HqlUtil.addCondition(hql, "bpbjZyfl.parentZyfl.id", zyflId);
			} else {
				HqlUtil.addCondition(hql, "bpbjZyfl.id", zyflId);
			}
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return bpbjxxManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] {"id", "bpbjZyfl", "sbbm", "sbmc", "xsjgg", "dw", "edsl", "sjsl", "yjsl", "remark"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] {"id", "bpbjZyfl", "sbbm", "sbmc", "xsjgg", "dw", "edsl", "sjsl", "yjsl", "remark"};
	}

	public BpbjxxManager getBpbjxxManager() {
		return bpbjxxManager;
	}

	public void setBpbjxxManager(BpbjxxManager bpbjxxManager) {
		this.bpbjxxManager = bpbjxxManager;
	}

	public BpbjZyflManager getBpbjZyflManager() {
		return bpbjZyflManager;
	}

	public void setBpbjZyflManager(BpbjZyflManager bpbjZyflManager) {
		this.bpbjZyflManager = bpbjZyflManager;
	}

}
