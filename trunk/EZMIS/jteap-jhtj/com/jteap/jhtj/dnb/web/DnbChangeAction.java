/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.jhtj.dnb.web;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.dnb.manager.DnbChangeManager;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 电能表换表Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class DnbChangeAction extends AbstractAction {
	
	private DnbChangeManager dnbChangeManager;
	private JdbcManager jdbcManager;
	
	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

	public void setDnbChangeManager(DnbChangeManager dnbChangeManager) {
		this.dnbChangeManager = dnbChangeManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return dnbChangeManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","elecId","times","hbr","ctN","ptN","ctO","ptO","elecbhN","elecbhO",
			"pzN","pfN","qzN","qfN","fpzN","fpfN","fqzN","fqfN","ppzN","ppfN","pqzN","pqfN","gpzN","gpfN","gqzN","gqfN",
			"pzO","pfO","qzO","qfO","fpzO","fpfO","fqzO","fqfO","ppzO","ppfO","pqzO","pqfO","gpzO","gpfO","gqzO","gqfO"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","elecId","times","hbr","ctN","ptN","ctO","ptO","elecbhN","elecbhO",
			"pzN","pfN","qzN","qfN","fpzN","fpfN","fqzN","fqfN","ppzN","ppfN","pqzN","pqfN","gpzN","gpfN","gqzN","gqfN",
			"pzO","pfO","qzO","qfO","fpzO","fpfO","fqzO","fqfO","ppzO","ppfO","pqzO","pqfO","gpzO","gpfO","gqzO","gqfO"
		};
	}

	@Override
	public String saveUpdateAction() throws BusinessException {
		String elecId = request.getParameter("elecId");
		String elecbhN = request.getParameter("elecbhN");
		String cts = request.getParameter("ctN");
		String pts = request.getParameter("ptN");
		int ct = 0;
		int pt = 0;
		
		try {
			String[] ctsArray = cts.split("/");
			int ct1 = Integer.parseInt(ctsArray[0].trim());
			int ct2 = Integer.parseInt(ctsArray[1].trim());
			ct = ct1/ct2;
			
			String[] ptsArray = pts.split("/");
			int pt1 = Integer.parseInt(ptsArray[0].trim());
			int pt2 = Integer.parseInt(ptsArray[1].trim());
			pt = pt1/pt2;
		} catch (Exception e) {
			log.info("输入ct、pt数据非法");
			try {
				this.outputJson("{success:false}");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return NONE;
		}
		pts = "PT:"+pts;
		cts = "CT:"+cts;
		elecbhN = "表号："+elecbhN;
		
		//更新 elec.elec_param
		String sql = "update elec.elec_param set pt="+pt+",ct="+ct+",pts='"+pts+"',cts='"+cts+"',elecbh='"+elecbhN+"' where elecid="+elecId;
		try {
			jdbcManager.executeSqlBatch(sql);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.outputJson("{success:false}");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return NONE;
		}
		
		return super.saveUpdateAction();
	}
	
}
