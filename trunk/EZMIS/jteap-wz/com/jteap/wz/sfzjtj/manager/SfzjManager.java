/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.sfzjtj.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.crkrzgl.manager.CrkrzglManager;
import com.jteap.wz.crkrzgl.manager.CrkrzmxManager;
import com.jteap.wz.sfzjtj.model.Sfzj;
import com.jteap.wz.tjny.manager.TjnyManager;
import com.jteap.wz.tjny.model.Tjny;
import com.jteap.wz.tjrw.model.Tjrw;
import com.jteap.wz.tjrw.util.TjrwUtils;
import com.jteap.wz.wzda.manager.WzdaManager;
import com.jteap.wz.wzda.model.Wzda;
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class SfzjManager extends HibernateEntityDao<Sfzj>{
	/**
	 * 统计年月处理类
	 */
	private TjnyManager tjnyManager;
	/**
	 * 当前报表中最大年份
	 */
	private int maxYear;
	/**
	 * 当前报表中最大月份
	 */
	private int maxMoth;
	/**
	 * 仓库管理处理对象
	 */
	private CkglManager ckglManager;
	private CrkrzglManager crkrzglManager;
	private CrkrzmxManager crkrzmxglManager;
	public TjnyManager getTjnyManager() {
		return tjnyManager;
	}
	public CrkrzglManager getCrkrzglManager() {
		return crkrzglManager;
	}
	public CrkrzmxManager getCrkrzmxglManager() {
		return crkrzmxglManager;
	}
	/**
	 * 生成报表合计
	 * @param countList  要生成的数据对象集合
	 * @param countMap   要放入的map对象集合
	 * @param countFlat  判断是当前页数据还是合计数据 d 点击下一页时的当前页合计数据
	 * q 点击上一页时的当前页合计数据 h 总记录合计数据
	 */
	public void setCountValue(List<Sfzj> countList,Map<String,Double> countMap,String countFlag){
		Sfzj sf = new Sfzj();
		DecimalFormat decimalFormat = new DecimalFormat("###.000");
		for(Sfzj sfzj:countList){
			if(sf.getRkd()!=null){
				sf.setRkd(sfzj.getRkd()+sf.getRkd());
			}else{
				sf.setRkd(sfzj.getRkd());
			}
			if(sf.getRkje()!=null){
				sf.setRkje(Double.valueOf(decimalFormat.format(sfzj.getRkje()))+Double.valueOf(decimalFormat.format(sf.getRkje())));
			}else{
				sf.setRkje(sfzj.getRkje());
			}
			if(sf.getCkd()!=null){
				sf.setCkd(sfzj.getCkd()+sf.getCkd());
			}else{
				sf.setCkd(sfzj.getCkd());
			}
			if(sf.getCkje()!=null){
				sf.setCkje(Double.valueOf(decimalFormat.format(sfzj.getCkje()))+Double.valueOf(decimalFormat.format(sf.getCkje())));
			}else{
				sf.setCkje(sfzj.getCkje());
			}
			if(sf.getYckc()!=null){
				sf.setYckc(Double.valueOf(decimalFormat.format(sfzj.getYckc()))+Double.valueOf(decimalFormat.format(sf.getYckc())));
			}else{
				sf.setYckc(sfzj.getYckc());
			}
			if(sf.getYmkc()!=null){
				sf.setYmkc(Double.valueOf(decimalFormat.format(sfzj.getYmkc()))+Double.valueOf(decimalFormat.format(sf.getYmkc())));
			}else{
				sf.setYmkc(sfzj.getYmkc());
			}
			//转入金额
			if(sf.getZrje()!=null){
				sf.setZrje(Double.valueOf(decimalFormat.format(sfzj.getZrje()))+Double.valueOf(decimalFormat.format(sf.getZrje())));
			}else{
				sf.setZrje(sfzj.getZrje());
			}
		}
		//添加到当前页合计数据 往后翻页
		if("d".equals(countFlag)){
			countMap.put("yckc",sf.getYckc()+countMap.get("yckc"));
			countMap.put("rkd",sf.getRkd()+countMap.get("rkd"));
			countMap.put("rkje",sf.getRkje()+countMap.get("rkje"));
			countMap.put("ckd",sf.getCkd()+countMap.get("ckd"));
			countMap.put("ckje",sf.getCkje()+countMap.get("ckje"));
			countMap.put("ymkc",sf.getYmkc()+countMap.get("ymkc"));
			countMap.put("rkje",sf.getRkje()+countMap.get("rkje"));
		}
		//添加到当前页合计数据 往前翻页
		if("q".equals(countFlag)){
			countMap.put("yckc",countMap.get("yckc")-sf.getYckc() );
			countMap.put("rkd", countMap.get("rkd")-sf.getRkd());
			countMap.put("rkje",countMap.get("rkje")-sf.getRkje());
			countMap.put("ckd", countMap.get("ckd")-sf.getCkd());
			countMap.put("ckje",countMap.get("ckje")-sf.getCkje());
			countMap.put("ymkc",countMap.get("ymkc")-sf.getYmkc());
			countMap.put("zrje",countMap.get("zrje")-sf.getZrje());
		}
		//添加总合计数据
		if("h".equals(countFlag)){
			//添加统计到合计数据
			countMap.put("yckc", sf.getYckc());
			countMap.put("rkd", sf.getRkd());
			countMap.put("rkje",sf.getRkje());
			countMap.put("ckd", sf.getCkd());
			countMap.put("ckje", sf.getCkje());
			countMap.put("ymkc",sf.getYmkc());
			countMap.put("zrje",sf.getZrje());
		}
		//如果map为空 则是导出excel使用
		if(countMap==null){
			Ckgl c = new Ckgl();
			c.setCkmc("合计：");
			sf.setCk(c);
			countList.add(sf);
		}
	}
	/**
	 * 返回最近报表年月
	 */
	public void getSfzjByZj(){
		Connection conn = null;
		Statement st = null;
		ResultSet rs= null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			//查询所有物资
			String sql = "select max(nf),max(yf) from Tb_Wz_Twzsfzjdtb";
			rs = st.executeQuery(sql);
		while (rs.next()){
			this.maxYear = rs.getInt(1);
			this.maxMoth = rs.getInt(2);
		}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(st!=null){
					st.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 添加物资资金收发统计
	 */
	@SuppressWarnings("deprecation")
	public void addSfzjtj(String staRi,Tjrw tjrw)throws Exception{
		String nf = tjrw.getNf();
		String yf = tjrw.getYf();
		List<Sfzj> sfzjList = new ArrayList<Sfzj>();
		//查询是否已经生成报表
		List list = tjnyManager.find("from Tjny t where t.ny = '"+nf+"年"+yf+"月' and t.bblb = '"+tjrw.getRwlb()+"'");
		//如果有 则删除重建
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				tjnyManager.remove(list.get(i));
			}
		} 
		this.getSession().flush();
		Tjny tjny = new Tjny();
		tjny.setNy(tjrw.getNf()+"年"+tjrw.getYf()+"月");
		tjny.setBblb(tjrw.getRwlb());
		tjnyManager.save(tjny);
		
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		String ckSql = "select t.id from tb_wz_ckgl t";
		//入库金额
		double rkje = 0;
		//出库金额
		double ckje = 0;
		try{
			conn = dataSource.getConnection();
			st = conn.createStatement();
			res = st.executeQuery(ckSql);
			while(res.next()){
				Sfzj sfzj = new Sfzj();
				Ckgl ckgl = ckglManager.get(res.getString(1));
				sfzj.setCk(ckgl);
				//出库金额 出库单 Map
				Map<String,Double> rkMap = this.getRkMap(tjrw,staRi,res.getString(1));
			 
				 
				//出库金额 出库单 Map
				Map<String,Double> ckMap = this.getCkMap(tjrw,staRi,res.getString(1));
			 
				//添加入库单,入库金额
				sfzj.setRkd(rkMap.get("countNum"));
				sfzj.setRkje(rkMap.get("countMoney"));
				rkje =rkMap.get("countMoney");
			 
				//添加出库单,出库金额
				sfzj.setCkd(ckMap.get("countNum"));
				sfzj.setCkje(ckMap.get("countMoney"));
				ckje = ckMap.get("countMoney");
				
				//根据条件返回 月初盘点sql
				String ycpdSql = this.getYckcSql(nf,yf,ckgl.getId());
				//根据条件返回 月末盘点sql
				String ympdSql = this.getYmkcSQL(ckgl.getId(),nf,yf);
				//System.out.println(ympdSql);
				//添加月初库存
				st = conn.createStatement();
				ResultSet re = st.executeQuery(ycpdSql);
				if(re.next()){
					sfzj.setYckc((re.getString(1)!=null)?Double.valueOf(re.getString(1)):0.0);
				}else{
					sfzj.setYckc(0.0);
				}
//				//添加月末库存
//				st = conn.createStatement();
//				re = st.executeQuery(ympdSql);
//				if(re.next()){
//					sfzj.setYmkc((re.getString(1)!=null)?Float.parseFloat(re.getString(1)):new Float(0));
//				}else{
				
				//转入金额 2011-10月份统计使用 下个月注释
				double zrje = this.getJeMap(nf, yf, ckgl.getId()).get("countMoney");
				//转入后的月末库存 2011-10月份统计使用 下个月注释
				sfzj.setYmkc(sfzj.getYckc()+rkje-ckje+zrje);
				//正常统计库存 2011-10月份统计 注释 下月解开
//				sfzj.setYmkc(sfzj.getYckc()+rkje-ckje);
//				}
				//存入转入金额  2011-10月份统计使用 下个月注释
				sfzj.setZrje(zrje);
				
				sfzj.setNf(nf);
				sfzj.setYf(yf);
				sfzj.setTjsj(tjrw.getNf()+"-"+tjrw.getYf());
				sfzj.setTjny(tjny);
				st.executeUpdate("update tb_wz_tjrw t set t.bz = '正在统计:"+ckgl.getCkmc()+"' where t.id = '"+tjrw.getId()+"'");
				this.save(sfzj);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(res!=null){
				res.close();
			}
			if(st !=null){
				st.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		
	}
	public void initSfzj() {
		// TODO Auto-generated method stub
		WzdaManager wzdaManager = (WzdaManager) SpringContextUtil.getBean("wzdaManager");
		String tjsql = "from sfzj where nf = 2011 and yf =10";
		List<Sfzj> sfzjList = this.find(tjsql);
		for(Sfzj sfzj:sfzjList){
			String sql = "from wzda where oldwzkc>0 and ";
			List<Wzda> wzdaList = wzdaManager.find(sql);
			
		}
			
	}
	/**
	 * 返回指定仓库月末库存
	 * @return
	 */
	public String getYmkcSQL(String ckid,String nf,String yf){
		return "SELECT T.YMKC FROM TB_WZ_YMKC T WHERE T.ID='"+ckid+"' AND " +
				"TO_DATE(TO_CHAR(T.SJ,'yyyy-mm'),'YYYY-MM') = TO_DATE('"+nf+"-"+yf+"','yyyy-mm')";
	}
	/**
	 * 根据年月返回月初库存
	 * @param nf
	 * @param yf
	 * @return
	 */
	public String getYckcSql(String nf,String yf,String ckid){
		return "select t.ymkc from tb_wz_twzsfzjdtb t where to_date(t.tjsj,'yyyy-mm') = add_months(to_date('"+nf+"-"+yf+"','yyyy-mm'),-1) and t.ck='"+ckid+"'";
	}
	/**
	 * 返回入库单 入库金额 合计Map
	 * @param tjrw 统计任务
	 * @param staRi 统计开始日期
	 * @param ckid
	 * @return
	 */
	public Map<String,Double> getRkMap(Tjrw tjrw,String staRi,String ckid){
		String hql = "from Yhdmx y where y.wzdagl.kw.ckid='"+ckid+"' " +
		" and  y.rksj >= add_months(to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+staRi+"','yyyy-mm-dd'),-1)" +
		" and  y.rksj < to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+tjrw.getRi()+"', 'yyyy-mm-dd') and y.zt='1'";
		Map<String,Double> countMap = TjrwUtils.getRkMap(hql);
		return countMap;
	}
	/**
	 * 返回出库单 出库金额 合计Map
	 * @param tjrw 统计任务
	 * @param staRi 统计开始日期
	 * @param ckid
	 * @return
	 */
	public Map<String,Double> getCkMap(Tjrw tjrw,String staRi,String ckid){
		String hql = "from Lydmx l where l.wzbm.kw.ckid='"+ckid+"' " +
				" and l.lysj >= add_months(to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+staRi+"', 'yyyy-mm-dd'),-1) " +
				" and l.lysj < to_date('"+tjrw.getNf()+"-"+tjrw.getYf()+"-"+tjrw.getRi()+"', 'yyyy-mm-dd')" +
				" and l.zt = '1' ";
		Map<String,Double> countMap = TjrwUtils.getCkMap(hql);
		return countMap;
	}
	
	/**
	 * 返回 本期导入金额合计Map
	 * @param nf
	 * @param yf
	 * @param ckid
	 * @return
	 */
	public Map<String,Double> getJeMap(String nf,String yf,String ckid){
		Map<String,Double> countMap  = null;
		//如果是10月份的话 则调用此方法
		if("2011".equals(nf)&&"10".equals(yf)){
			String hql = "from Wzda l where l.kw.ckid='"+ckid+"' and oldwzkc >0 ";
			countMap = TjrwUtils.getJeMap(hql);
		}else{
			countMap = new HashMap<String, Double>();
			countMap.put("countMoney",Double.valueOf(0+""));
		}
		return countMap;
	}

	public CkglManager getCkglManager() {
		return ckglManager;
	}
	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}
	public int getMaxYear() {
		return maxYear;
	}
	public void setMaxYear(int maxYear) {
		this.maxYear = maxYear;
	}
	public int getMaxMoth() {
		return maxMoth;
	}
	public void setMaxMoth(int maxMoth) {
		this.maxMoth = maxMoth;
	}
	public void setTjnyManager(TjnyManager tjnyManager) {
		this.tjnyManager = tjnyManager;
	}
	public void setCrkrzglManager(CrkrzglManager crkrzglManager) {
		this.crkrzglManager = crkrzglManager;
	}
	public void setCrkrzmxglManager(CrkrzmxManager crkrzmxglManager) {
		this.crkrzmxglManager = crkrzmxglManager;
	}
}
