/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.wz.kcpd.web;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
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
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.crkrzgl.manager.CrkrzglManager;
import com.jteap.wz.crkrzgl.model.Crkrzgl;
import com.jteap.wz.crkrzgl.model.Crkrzmx;
import com.jteap.wz.kcpd.manager.PddManager;
import com.jteap.wz.kcpd.manager.PddMxManager;
import com.jteap.wz.kcpd.model.Pdd;
import com.jteap.wz.kcpd.model.PddMx;
import com.jteap.wz.kwwh.manager.KwwhManager;
import com.jteap.wz.kwwh.model.Kw;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.yhdmx.manager.YhdmxManager;


@SuppressWarnings({ "unchecked", "serial", "unused" })
public class PddAction extends AbstractAction {

	private PddManager pddManager;
	
	private PddMxManager pddMxManager;
	
	private CrkrzglManager crkrzglManager;
	
	private WzdaManager wzdaManager;
	
	private KwwhManager kwwhManager;
	
	private YhdmxManager yhdmxManager;
	
	private static StringBuffer jsonBuf;
	
	public KwwhManager getKwwhManager() {
		return kwwhManager;
	}

	public void setKwwhManager(KwwhManager kwwhManager) {
		this.kwwhManager = kwwhManager;
	}

	public WzdaManager getWzdaManager() {
		return wzdaManager;
	}

	public void setWzdaManager(WzdaManager wzdaManager) {
		this.wzdaManager = wzdaManager;
	}

	public CrkrzglManager getCrkrzglManager() {
		return crkrzglManager;
	}

	public void setCrkrzglManager(CrkrzglManager crkrzglManager) {
		this.crkrzglManager = crkrzglManager;
	}

	public PddManager getPddManager() {
		return pddManager;
	}

	public void setPddManager(PddManager pddManager) {
		this.pddManager = pddManager;
	}

	public PddMxManager getPddMxManager() {
		return pddMxManager;
	}

	public void setPddMxManager(PddMxManager pddMxManager) {
		this.pddMxManager = pddMxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
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
	 * 盘点入库生效
	 */
	public String enableDR() throws Exception{
		String ids = request.getParameter("ids");
		try {
			if(StringUtil.isNotEmpty(ids)) {
				List<Pdd> pdds = pddManager.findByIds(ids.split(","));
				//获取出入库日志当前最大编号
				String crkmxbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrzmx");
				//获取出入库日志明细当前最大编号
				String crkbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrz");
				for(Pdd obj:pdds){
					obj.setZt("1"); //调入生效、
					
					Set<PddMx> mxs = obj.getPddmxs();
					String ckbm = "";
					Crkrzgl crkrzgl = new Crkrzgl();
					Set crkrzmxs = crkrzgl.getCrkrzmxs();
					int index = 1;
					for(PddMx mx : mxs){
						//修改物资档案平均价和当前库存量
						Wzda wzda = mx.getWzda();
						Crkrzmx crk = new Crkrzmx();
						
						ckbm = mx.getWzda().getKw().getCkid();
						
//						double wz_pjj = wzda.getPjj();
//						double mx_dj = mx.getDbdj();
//						double new_pjj = (wz_pjj+mx_dj)/2;
						double pqsl = mx.getPqsl();
						double pdsl = mx.getPdsl();
						
						wzda.setDqkc(wzda.getDqkc()+Math.abs(pqsl-pdsl));
//						wzda.setPjj(new_pjj);
						
						//生成一个出入库日志明细信息
						crk.setCrkrzgl(crkrzgl);
						crk.setWzda(mx.getWzda());
						crk.setCrksl(Math.abs(pqsl-pdsl));
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
					crkrzgl.setCrklb("03");
					crkrzgl.setCrkqf("1");
					crkrzgl.setXgdjbh(obj.getBh());
					
					crkrzglManager.save(crkrzgl);
					pddManager.save(obj);
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
	 * 盘点单生效
	 */
	public String enableDC() throws Exception{
		String ids = request.getParameter("ids");
		try {
			if(StringUtil.isNotEmpty(ids)) {
				List<Pdd> pdds = pddManager.findByIds(ids.split(","));
				//获取出入库日志当前最大编号
				String crkmxbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrzmx");
				//获取出入库日志明细当前最大编号
				String crkbh = this.getMaxNum("select max(crkdh) from tb_wz_ycrkrz");
				for(Pdd obj:pdds){
					obj.setZt("1"); //调入生效、
					
					Set<PddMx> mxs = obj.getPddmxs();
					String ckbm = "";
					
					//出库记录
					Crkrzgl crkrzgl_ck = new Crkrzgl();
					Set crkrzmxs_ck = crkrzgl_ck.getCrkrzmxs();
					
					//入库记录
					Crkrzgl crkrzgl_rk = new Crkrzgl();
					Set crkrzmxs_rk = crkrzgl_rk.getCrkrzmxs();
					int index = 1;
					for(PddMx mx : mxs){
						double pqsl = mx.getPqsl();
						double pdsl = (mx.getPdsl() == null)?0.0:mx.getPdsl();
						double crsl = pqsl-pdsl;
						Crkrzmx crk = new Crkrzmx();
						if(Math.abs(crsl)>0){
							//修改物资档案平均价和当前库存量
							Wzda wzda = mx.getWzda();
							
							ckbm = mx.getWzda().getKw().getCkid();
							//生成一个出入库日志明细信息
							
							crk.setWzda(mx.getWzda());
							crk.setCrksl(Math.abs(crsl));
							crk.setCrkjg(wzda.getJhdj());
							crk.setCrkdh(crkmxbh);
							crkmxbh = this.getNextNum(crkmxbh);
							crk.setXh(String.valueOf(index));
							index++;
							//盘点前库存-盘点库存 如果大于0 表示物资减少即为出库 小于-即为入库
							if(crsl>0){
								crk.setCrkrzgl(crkrzgl_ck);
								wzda.setDqkc(wzda.getDqkc()- Math.abs(crsl));
								crkrzmxs_ck.add(crk);
							}else{
								crk.setCrkrzgl(crkrzgl_rk);
								wzda.setDqkc(wzda.getDqkc()+ Math.abs(crsl));
								crkrzmxs_rk.add(crk);
							}
						}
					}

					if(crkrzmxs_ck.size()>0){
						//生成一个出入库日志信息
						crkrzgl_ck.setCrkdh(crkbh);
					    crkbh = this.getNextNum(crkbh);
					    crkrzgl_ck.setCrksj(DateUtils.parseDate(DateUtils.getTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
					    crkrzgl_ck.setCkbh(ckbm);
						String curPersonId=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
						String curPersonName = (String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME);
						crkrzgl_ck.setCzr(curPersonId);
						crkrzgl_ck.setCzrxm(curPersonName);
						crkrzgl_ck.setCrklb("13");
						crkrzgl_ck.setCrkqf("2");
						crkrzgl_ck.setXgdjbh(obj.getBh());
						crkrzglManager.save(crkrzgl_ck);
					}
					if(crkrzmxs_rk.size()>0){
						//生成一个出入库日志信息
						crkrzgl_rk.setCrkdh(crkbh);
					    crkbh = this.getNextNum(crkbh);
					    crkrzgl_rk.setCrksj(DateUtils.parseDate(DateUtils.getTime("yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
					    crkrzgl_rk.setCkbh(ckbm);
						String curPersonId=(String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
						String curPersonName = (String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME);
						crkrzgl_rk.setCzr(curPersonId);
						crkrzgl_rk.setCzrxm(curPersonName);
						crkrzgl_rk.setCrklb("03");
						crkrzgl_rk.setCrkqf("1");
						crkrzgl_rk.setXgdjbh(obj.getBh());
						crkrzglManager.save(crkrzgl_rk);
					}

					pddManager.save(obj);
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
	 * 盘点单生效  吕超 2011.10.13
	 */
	public String enablePdd() throws Exception{
		String ids = request.getParameter("ids");
		String mxJson = request.getParameter("pdmxJson");
		try {
			if(StringUtil.isNotEmpty(ids)) {
				List<Pdd> pdds = pddManager.findByIds(ids.split(","));
				for(Pdd obj:pdds){
					obj.setZt("1"); //调入生效、
					pddManager.save(obj);
				}
			}
			if(StringUtil.isNotEmpty(mxJson)){
				List<Map<String, String>> mxList = JSONUtil.parseList(mxJson);
				for(Map<String,String> map:mxList){
					PddMx pdmx = pddMxManager.findUniqueBy("id", map.get("id"));
					pdmx.setZt("1");
					double pdsl  =Double.valueOf(map.get("pdsl"));
					pdmx.setPdsl(pdsl);
					pdmx.setSlcy(pdsl-pdmx.getPqsl());
					if(pdmx.getPjj()==null){
						pdmx.setJecy(0.0);
					}else{
						pdmx.setJecy((pdsl-pdmx.getPqsl())*pdmx.getPjj());
					}
					pdmx.setCyyy(map.get("cyyy"));
					pddMxManager.save(pdmx);
				}
			}
			outputJson("{success:true}");
		} catch (Exception e) {
			String msg = e.getMessage();
			e.printStackTrace();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
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
		Pdd pdd = null;
 		try {
			if (keys != null) {
				//删除明细表
				String cgjhKeys[]=keys.split(","); 
				for(int i=0;i<cgjhKeys.length;i++){
					pdd = pddManager.get((Serializable)cgjhKeys[i]);
					List<PddMx> pddmxs = pddMxManager.findBy("pdd", pdd);
					for(PddMx mx:pddmxs){
						pddMxManager.remove(mx);
					}
					//删除主表
					pddManager.remove(pdd);
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
		return this.pddManager;
	}


	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"bh",
			"pdsj",
			"time",
			"czr",
			"ddj",
			"xdj",
			"sl",
			"zksj",
			"ckmc",
			"zt"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"bh",
				"pdsj",
				"time",
				"czr",
				"ddj",
				"xdj",
				"sl",
				"zksj",
				"ckmc",
				"zt"};
	}
	
	/**
	 * 随机获取500或1000个物资
	 */
	public String findRandomWzda() throws Exception{
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		double countPqsl = 0.0;
		double countPdsl = 0.0;
		jsonBuf = new StringBuffer();
		jsonBuf.append("[");
		//System.out.println("-------");
		try {
			conn = dataSource.getConnection();
			String sql = "select * from (select * from tb_wz_swzda sample(10) order by trunc(dbms_random.value(0, 1000))) where rownum <= 50";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				jsonBuf.append("{WZBM:'"+rs.getString("id")+"',");
				Wzda wzda = wzdaManager.get(rs.getString("id"));
				Kw kw = kwwhManager.get(wzda.getKw().getId());
				jsonBuf.append( "WZMC:'"+rs.getString("wzmc")+"',");
				jsonBuf.append( "XHGG:'"+rs.getString("xhgg")+"',");
				jsonBuf.append( "JLDW:'"+rs.getString("jldw")+"',");
				jsonBuf.append( "IS_CANCEL:'1',");
				jsonBuf.append( "ISNEW:'0',");
				jsonBuf.append( "WZZJM:'"+kw.getWzzjm()+"',");
				jsonBuf.append( "JHDJ:'"+rs.getDouble("jhdj")+"',");
				jsonBuf.append( "PQSL:'"+rs.getDouble("dqkc")+"',");
				jsonBuf.append( "PDSL:'"+rs.getDouble("dqkc")+"'},");
				countPqsl = countPqsl+rs.getDouble("dqkc");
				countPdsl = countPdsl+rs.getDouble("dqkc");
			}
			//jsonBuf.append("{WZZJM:'<font color = red>合计：</font>',PQSL:'"+countPqsl+"',PDSL:'"+countPdsl+"'}]");
			String json = "{totalCount:'" + 500 + "',list:"+jsonBuf.toString()+"}";
			//outputJson("list:["+json.toString().substring(0, json.toString().length()-1)+"]}");
			outputJson(json);
		} catch (Exception e) {
//			String msg = e.getMessage();
//			outputJson("{success:false,msg:'" + msg + "'}");
			throw new BusinessException("显示列表异常", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}
	/**
	 * 返回指定的盘点物资
	 * @return
	 */
	public String findRanPdwzAction(){
		String parWhere = request.getParameter("parWhere");
		String sl = request.getParameter("sl");
		String ddj = request.getParameter("ddj");
		String xdj = request.getParameter("xdj");
		StringBuffer hql = new StringBuffer("from Yhdmx t ");
		//随即次数
		int r_sl=0;
		//物资单价
		double d_dj=0;
		//物资单价
		double x_dj=0;
		if(StringUtils.isNotEmpty(sl)){
			r_sl = Integer.valueOf(sl);
		}
		if(StringUtils.isNotEmpty(ddj)){
			d_dj = Double.valueOf(ddj);
		}
		if(StringUtils.isNotEmpty(xdj)){
			x_dj = Double.valueOf(xdj);
		}
		if(StringUtils.isNotEmpty(parWhere)){
			HqlUtil.addWholeCondition(hql, parWhere);
		}else{
			hql.append("where 1=1");
		}
		hql.append(" and t.zt = '1' and t.sysl>0 order by t.wzdagl.dqkc desc");
		System.out.println(hql.toString());
		List<Map> list = yhdmxManager.getPdWz(hql.toString(), d_dj,x_dj, r_sl);
		try {
			
			jsonBuf = new StringBuffer();
			jsonBuf.append("[");
			int i = 0;
			for(Map<String,String> map:list){
				i++;
				jsonBuf.append("{WZBM:'"+map.get("wzid")+"',");
				jsonBuf.append( "WZMC:'"+map.get("wzmc")+"',");
				jsonBuf.append( "XHGG:'"+map.get("xhgg")+"',");
				jsonBuf.append( "KW:'"+map.get("kw")+"',");
				jsonBuf.append( "PJJ:'"+map.get("pjj")+"',");
				jsonBuf.append( "PQSL:'"+map.get("dqkc")+"',");
				jsonBuf.append( "PQJE:'"+map.get("dqje")+"',");
				jsonBuf.append( "ZKSJ:'"+map.get("zksj")+"',");
				jsonBuf.append( "PDSL:'"+map.get("dqkc")+"',");
				jsonBuf.append( "SLCY:'',");
				jsonBuf.append( "JECY:'',");
				jsonBuf.append( "CYYY:''}");
				if(i<list.size()){
					jsonBuf.append( ",");
				}
			}
			jsonBuf.append("]");
			String json = "{list:"+jsonBuf.toString()+"}";
			System.out.println(json);
			//outputJson("list:["+json.toString().substring(0, json.toString().length()-1)+"]}");
			outputJson(json);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 重写导出excel方法
	 * lvchao
	 */
	public String exportExcel() throws Exception {
		
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = WebUtils.getRequestParam(request, "paraHeader");
		//System.out.println(paraHeader);
		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");
		//System.out.println(paraDataIndex);
		//System.out.println(paraDataIndex);
		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
		if(jsonBuf==null){
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = getOut();
			out.print("<script>alert(\"导出失败：要导出的报表不存在\");window.close()</script>");
			return NONE;
		}
		//System.out.println(jsonBuf.toString());
		List list = JSONUtil.parseList(jsonBuf.toString().replace("null","").replace("<font color = red>合计：</font>","合计"));
		
		export(list, paraHeader, paraDataIndex, paraWidth);
	 
		// 调用导出方法
		return NONE;
	}
	
}
