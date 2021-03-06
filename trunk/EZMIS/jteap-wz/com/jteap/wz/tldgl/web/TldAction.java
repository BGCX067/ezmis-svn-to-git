/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.tldgl.web;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.crkrzgl.manager.CrkrzglManager;
import com.jteap.wz.crkrzgl.model.Crkrzgl;
import com.jteap.wz.crkrzgl.model.Crkrzmx;
import com.jteap.wz.tldgl.manager.TldManager;
import com.jteap.wz.tldgl.manager.TldMxManager;
import com.jteap.wz.tldgl.model.TldMx;
import com.jteap.wz.tldgl.model.Tldgl;
import com.jteap.wz.wzda.model.Wzda;


@SuppressWarnings({ "unchecked", "serial" })
public class TldAction extends AbstractAction {

	private TldManager tldManager;
	
	private TldMxManager tldMxManager;
	
	private CrkrzglManager crkrzglManager;
	
	public CrkrzglManager getCrkrzglManager() {
		return crkrzglManager;
	}

	public void setCrkrzglManager(CrkrzglManager crkrzglManager) {
		this.crkrzglManager = crkrzglManager;
	}

	public TldManager getTldManager() {
		return tldManager;
	}

	public void setTldManager(TldManager tldManager) {
		this.tldManager = tldManager;
	}

	public TldMxManager getTldMxManager() {
		return tldMxManager;
	}

	public void setTldMxManager(TldMxManager tldMxManager) {
		this.tldMxManager = tldMxManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String hqlWhere = request.getParameter("queryParamsSql");
//			//默认状态为未生效
			String zt = "0";
			zt = request.getParameter("zt");
			HqlUtil.addCondition(hql, "zt",zt);
			
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "bh", "asc");
		}
	}
	

	/**
	 * 调入单生效
	 */
	public String enable() throws Exception{
		String ids = request.getParameter("ids");
		try {
			if(StringUtil.isNotEmpty(ids)) {
				List<Tldgl> tlds = tldManager.findByIds(ids.split(","));
				//获取出入库日志当前最大编号
				String crkmxbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrzmx");
				//获取出入库日志明细当前最大编号
				String crkbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrz");
				for(Tldgl obj:tlds){
					obj.setZt("1"); //调入生效、
					
					Set<TldMx> mxs = obj.getTldmxs();
					String ckbm = "";
					Crkrzgl crkrzgl = new Crkrzgl();
					Set crkrzmxs = crkrzgl.getCrkrzmxs();
					int index = 1;
					for(TldMx mx : mxs){
						//修改物资档案平均价和当前库存量
						Wzda wzda = mx.getWzda();
						Crkrzmx crk = new Crkrzmx();
						
						ckbm = mx.getWzda().getKw().getCkid();
						
//						double wz_pjj = wzda.getPjj();
//						double mx_dj = mx.getDbdj();
//						double new_pjj = (wz_pjj+mx_dj)/2;
						wzda.setDqkc(wzda.getDqkc()+mx.getTlsl());
//						wzda.setPjj(new_pjj);
						
						//生成一个出入库日志明细信息
						crk.setCrkrzgl(crkrzgl);
						crk.setWzda(mx.getWzda());
						crk.setCrksl(mx.getTlsl());
						crk.setCrkjg(wzda.getJhdj());
						crk.setCrkdh(crkmxbh);
						crkmxbh = this.getNextNum(crkmxbh);
						crk.setXh(String.valueOf(index));
						index ++;
						crkrzmxs.add(crk);
					}
					//生成一个出入库日志信息
					crkrzgl.setCrkdh(crkbh);
				    crkbh = this.getNextNum(crkbh);
					crkrzgl.setCrksj(DateUtils.parseDate(DateUtils.getTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
					crkrzgl.setCkbh(ckbm);
					String curPersonId=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
					String curPersonName = (String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME);
					crkrzgl.setCzr(curPersonId);
					crkrzgl.setCzrxm(curPersonName);
					crkrzgl.setCrklb("04");
					crkrzgl.setCrkqf("1");
					crkrzgl.setXgdjbh(obj.getBh());
					
					crkrzglManager.save(crkrzgl);
					tldManager.save(obj);
				}
				outputJson("{success:true}");
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			e.printStackTrace();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	
	/**
	 * 调出生效
	 */
//	public String enableDC() throws Exception{
//		String ids = request.getParameter("ids");
//		try {
//			if(StringUtil.isNotEmpty(ids)) {
//				List<Dbd> dbds = dbdManager.findByIds(ids.split(","));
//				//获取出入库日志当前最大编号
//				String crkmxbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrzmx");
//				//获取出入库日志明细当前最大编号
//				String crkbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrz");
//				for(Dbd obj:dbds){
//					obj.setZt("1"); //调入生效、
//					
//					Set<DbdMx> mxs = obj.getDbdmxs();
//					String ckbm = "";
//					Crkrzgl crkrzgl = new Crkrzgl();
//					Set crkrzmxs = crkrzgl.getCrkrzmxs();
//					
//					for(DbdMx mx : mxs){
//						//修改物资档案平均价和当前库存量
//						Wzda wzda = mx.getWzda();
//						Crkrzmx crk = new Crkrzmx();
//						
//						ckbm = mx.getWzda().getKw().getCkid();
//						
//						wzda.setDqkc(wzda.getDqkc()- mx.getDbsl());
//						
//						//生成一个出入库日志明细信息
//						crk.setCrkrzgl(crkrzgl);
//						crk.setWzda(mx.getWzda());
//						crk.setCrksl(mx.getDbsl());
//						crk.setCrkjg(mx.getDbdj());
//						crk.setCrkdh(crkmxbh);
//						crkmxbh = this.getNextNum(crkmxbh);
//						crk.setXh("0");
//						crkrzmxs.add(crk);
//					}
//					//生成一个出入库日志信息
//					crkrzgl.setCrkdh(crkbh);
//				    crkbh = this.getNextNum(crkbh);
//					crkrzgl.setCrksj(DateUtils.parseDate(DateUtils.getTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
//					crkrzgl.setCkbh(ckbm);
//					String curPersonId=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
//					crkrzgl.setCzr(curPersonId);
//					crkrzgl.setCrklb("12");
//					crkrzgl.setCrkqf("2");
//					crkrzgl.setXgdjbh(obj.getId());
//					
//					crkrzglManager.save(crkrzgl);
//					dbdManager.save(obj);
//				}
//				outputJson("{success:true}");
//			}
//		} catch (Exception e) {
//			String msg = e.getMessage();
//			e.printStackTrace();
//			outputJson("{success:false,msg:'" + msg + "'}");
//		}
//		return NONE;
//	}
	
	/**
	 * 获取目标表中最大的编号，生成新的编号，规则：最大编号+1
	 * @param entityManager
	 * @param hql
	 * @return
	 * @throws Exception 
	 */
	private String getMaxNum(String sql) throws Exception{
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			//String sql = sql;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if(StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getInstance();
					nformat.setMinimumIntegerDigits(8);
			 		int max = Integer.parseInt(ckbmMax)+1;
			 		retValue = nformat.format(max).replaceAll(",", "");
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return retValue;
	}
	
	private String getNextNum(String num){
		NumberFormat nformat = NumberFormat.getIntegerInstance();
		int nextnum = Integer.parseInt(num)+1;
		nformat.setMinimumIntegerDigits(8);
		return nformat.format(nextnum).replaceAll(",", "");
	}
	
	/**
	 * 删除操作，删除验货单及其明细
	 */
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		Tldgl tldgl = null;
 		try {
			if (keys != null) {
				//删除明细表
				String cgjhKeys[]=keys.split(","); 
				for(int i=0;i<cgjhKeys.length;i++){
					tldgl = tldManager.get((Serializable)cgjhKeys[i]);
					List<TldMx> tldmxs = tldMxManager.findBy("tldgl", tldgl);
					for(TldMx mx:tldmxs){
						tldMxManager.remove(mx);
					}
					//删除主表
					tldManager.remove(tldgl);
				}
				outputJson("{success:true}");
			}
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return this.tldManager;
	}


	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"bh",
			"tlsj",
			"time",
			"czr",
			"ysr",
			"tlr",
			"cwfzr",
			"personCzr",
			"personYsr",
			"personTlr",
			"personCwfzr",
			"tlbm",
			"gclb",
			"gcxm",
			"tlyy",
			"zt",
			"userName","userLoginName"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"bh",
				"tlsj",
				"czr",
				"ysr",
				"tlr",
				"cwfzr",
				"personCzr",
				"personYsr",
				"personTlr",
				"personCwfzr",
				"tlbm",
				"gclb",
				"gcxm",
				"tlyy",
				"zt",
				"userName","userLoginName"};
	}
}
