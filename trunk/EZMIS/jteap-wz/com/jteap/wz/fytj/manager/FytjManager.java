package com.jteap.wz.fytj.manager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.fytj.model.Fytj;
import com.jteap.wz.gclbgl.manager.ProjcatManager;
import com.jteap.wz.gclbgl.model.Projcat;
import com.jteap.wz.gcxmgl.manager.ProjManager;
import com.jteap.wz.gcxmgl.model.Proj;
import com.jteap.wz.tjny.manager.TjnyManager;
import com.jteap.wz.tjny.model.Tjny;
import com.jteap.wz.tjrw.model.Tjrw;
import com.jteap.wz.tjrw.util.TjrwUtils;
/**
 * 费用统计处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class FytjManager extends HibernateEntityDao<Fytj>{
	
	/**
	 * 统计年月处理类
	 */
	private TjnyManager tjnyManager;
	//工程项目 处理类
	private ProjManager projManager;
	//仓库 Manager
	private CkglManager ckglManager;
	//工程类别 Manager
	private ProjcatManager projcatManager;
	 //子节点标记
	private boolean is_leaf;
	private Map<String,Double> countMap;
	/**
	 * 当前报表中最大年份
	 */
	private int maxYear;
	/**
	 * 当前报表中最大月份
	 */
	private int maxMoth;
	/**
	 * 新增费用统计
	 * 传入参数 当前年份 当前月份 统计任务
	 * @param nf
	 * @param yf
	 */
	public void addFytj(String ri,Tjrw tjrw){
		String strDt = "";
		String nf = tjrw.getNf();
		String yf = tjrw.getYf();
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			Statement st = null;
			ResultSet res = null;
			ResultSet xqjhRes = null;
			ResultSet xqjhMxRes = null;
			st  = conn.createStatement();
			
			//查询是否已经生成报表
			List nylist = tjnyManager.find("from Tjny t where t.ny = '"+nf+"年"+yf+"月' and t.bblb = '"+tjrw.getRwlb()+"'");
			//如果有 则删除重建
			if(nylist.size()>0){
				for(int i=0;i<nylist.size();i++){
					tjnyManager.remove(nylist.get(i));
				}
			} 
			this.getSession().flush();
			Tjny tjny = new Tjny();
			tjny.setNy(nf+"年"+yf+"月");
			tjny.setBblb(tjrw.getRwlb());
			tjnyManager.save(tjny);
			//查询所有仓库
			List<Ckgl> ckglList = ckglManager.getAll();
			//取出所有工程类别
			List<Projcat> parojcatList = projcatManager.findResourcesByProjcat();
			countMap = new HashMap<String, Double>();
			for(Projcat projcat : parojcatList){
				//查询工程项目父节点
				List<Proj> projList  = projManager.find("from Proj as p where p.projcat.id =? and p.finished='0' and p.isviable is null and p.p_proj is null", projcat.getId());
				for(Proj proj :projList){
					//	查询所有仓库
					for(Ckgl ckgl:ckglList){
						//初始化赋值
						countMap.put(proj.getProjname()+"_"+ckgl.getCkmc(), 0.0);
						//保存费用统计
						this.saveFytj(proj,projcat,nf, yf, ri, res, st, tjny, tjrw.getId(),ckgl);
						//保存合计费用统计
						this.saveCountFytj(proj, projcat, nf, yf, ri, res, st,tjny, tjrw.getId(),ckgl);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 根据编号 查询需求计划
	 * @param bh
	 * @return
	 */
	private String getXqjhSql(String bh) {
		// TODO Auto-generated method stub
		return "select t.id,t.gcxm,t.gclb from tb_wz_xqjh t where t.bh = '"+bh+"'";
	}
	/**
	 * 返回最近报表年月
	 */
	public void getFytjByZj(){
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			Statement st = conn.createStatement();
			//查询所有物资
			String sql = "select max(nf),max(yf) from tb_wz_twzfytjb";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()){
				this.maxYear = rs.getInt(1);
				this.maxMoth = rs.getInt(2);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 根据费用统计集合返回json字符串
	 * @param ht
	 * @param fytj
	 * @param index
	 * @return
	 */
	public String getJson(StringBuffer hql){
		StringBuffer json = new StringBuffer();
		List<Projcat> projCatList = projcatManager.findResourcesByProjcat();
		//总合计年累计
		double countNlj = 0;
		//总合计月累计
		double countYlj = 0;
		hql.append(" and obj.proj.id=? and obj.ckgl.id=? order by obj.proj.projname asc");
		DecimalFormat decimalFormat = new DecimalFormat("###.00");
		
		for(Projcat projcat:projCatList){
//			double catNjl = 0;
//			double catYjl = 0;
			//查出所有工程项目根节点
			List<Proj> projList  = projManager.find("from Proj as p where p.projcat.id =? and p.finished='0' and p.isviable is null and p.p_proj is null order by p.projname asc", projcat.getId());
			//循环所有工程项目根项目
			for(Proj proj : projList){
				//根项目年累计
//				double nlj = 0;
//				//根项目月累计
//				double ylj = 0;
				//初始化 月累计 年累计值
//				countMap.put("ySum", 0.0);
//				countMap.put("nSum", 0.0);
				//遍历子节点 拼接JSON 并将子节点的累计值 存入MAP
				findSaveJson(proj, json,hql);
				json.append(JSONUtil.mapToJson(this.getJsonObj(hql, proj,projcat))+",");
			}
			//总合计
//			countNlj = Double.valueOf(decimalFormat.format(countNlj)) + Double.valueOf(decimalFormat.format(catNjl));
//			countYlj = Double.valueOf(decimalFormat.format(countYlj)) + Double.valueOf(decimalFormat.format(catYjl));
			json.append(JSONUtil.mapToJson(this.getJsonObj(projcat))+",");
//			json.append("{xmmc:'"+projcat.getProjcatname()+"',path:'"+projcat.getId()+"',nlj:"+ Double.valueOf(decimalFormat.format(catNjl))+",ylj:"+Double.valueOf(decimalFormat.format(catYjl))+",_parent:null,_is_leaf:false},");
		}
//		if(projCatList.size()>0){
//			json.append("{xmmc:'合计',path:'"+UUIDGenerator.hibernateUUID()+"',nlj:"+ Double.valueOf(decimalFormat.format(countNlj))+",ylj:"+ Double.valueOf(decimalFormat.format(countYlj))+",_parent:null,_is_leaf:true}");
//		}
		String jsonStr = "{success:1,data:["+json.toString().substring(0,json.toString().length()-1)+"]}";
		return jsonStr;

	}
	private Map<String,Object> getJsonObj(Projcat projcat) {
		// TODO Auto-generated method stub
		//获取所有仓库
		List<Ckgl> ckglList = ckglManager.getAll();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		//循环每个仓库
		for(Ckgl ckgl:ckglList){
				jsonMap.put("xmmc", projcat.getProjcatname());
				jsonMap.put("path",projcat.getId());
				jsonMap.put(ckgl.getCkbm(),"");
				jsonMap.put("_parent",null);
				jsonMap.put("_is_leaf", false);
			}
		return jsonMap;
//			countMap.put(ckgl.getCkbm()+"_"+"Sum", fytj.getByje()+countMap.get(ckgl.getCkbm()+"_"+"Sum"));
	}
	/**
	 * 根据工程项目 递归查询 拼接json
	 * @param proj
	 * @param json
	 * @param hql
	 */
	private void findSaveJson(Proj proj,StringBuffer json,StringBuffer hql){
		 //如果是根节点
		 if(proj.getC_proj().size()>0){
			 //循环该根节点下的子节点
			for(Proj cProj:proj.getC_proj()){
				json.append(JSONUtil.mapToJson(this.getJsonObj(hql, cProj))+",");
				this.findSaveJson(cProj, json,hql);
			}
		 }else{
			//如果是子节点
			if(proj.getP_proj()!=null){
				json.append(JSONUtil.mapToJson(this.getJsonObj(hql, proj))+",");
			}
		 }
	}
	
	/**
	 *  根据Hql 语句返回 一个Proj的JSONMAP对象
	 * @param hql
	 * @param proj
	 * @return
	 */
	public Map<String,Object> getJsonObj(StringBuffer hql,Proj proj){
		//获取所有仓库
		List<Ckgl> ckglList = ckglManager.getAll();
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		boolean  _is_leaf = false;
		if(proj.getC_proj().size()<=0){
			_is_leaf = true;
		}else{
			_is_leaf = false;
		}
		BigDecimal one = new BigDecimal("1");
		BigDecimal value = null;
		//循环每个仓库
		for(Ckgl ckgl:ckglList){
			List<Fytj> fylist = this.find(hql.toString(), proj.getId(),ckgl.getId());
			Fytj fytj = (fylist.size()>0)?fylist.get(0):null;
			if(fytj!=null){
				jsonMap.put("xmmc", proj.getProjname());
				jsonMap.put("path",proj.getId());
				value = new BigDecimal(fytj.getByje());
				jsonMap.put(ckgl.getCkbm(),value.divide(one,2,BigDecimal.ROUND_HALF_UP));
				if(proj.getP_proj()!=null){
					jsonMap.put("_parent", proj.getP_proj().getId());
				}
				jsonMap.put("_is_leaf", _is_leaf);
			}
		}
		return jsonMap;
	}
	/**
	 * 返回一个 Projcat的 jsonMap对象
	 * @param hql
	 * @param proj
	 * @param projCat
	 * @return
	 */
	public Map<String,Object> getJsonObj(StringBuffer hql,Proj proj,Projcat projCat){
		Map<String,Object> jsonMap = this.getJsonObj(hql, proj);
		jsonMap.put("_parent", projCat.getId());
		return jsonMap;
	}
	/**
	 * 保存费用统计 目前只保存所有子节点 并统计出该子节点父节点合计
	 * @param proj 工程项目 对象
	 * @param projcat 工程类别 对象
	 * @param nf 年份
	 * @param yf 月份
	 * @param ri 日
	 * @param res ResultSet
	 * @param st Statement
	 * @param tjny 统计年月对象
	 * @param tjrw 统计任务对象
	 * @throws SQLException
	 */
	private void saveFytj(Proj proj,Projcat projcat,String nf,String yf,String ri,ResultSet res,Statement st,Tjny tjny,String tjrwId,Ckgl ckgl) throws SQLException{

		if(proj.getC_proj().size()>0){
			for(Proj projs:proj.getC_proj()){
				this.saveFytj(projs, projcat, nf, yf, ri, res, st, tjny, tjrwId,ckgl);
			}
		}else{
			Fytj fytj = new Fytj();
			//添加合同类别
			fytj.setProj(proj);
			//返回本月累计
			String strDt = nf+"-"+yf+"-"+ri;
			//如果要统计的月份是1月份 则起始日期是去年统计的截止日期
			if("01".equals(yf)){
				//返回费用统计中 所有日期去年最大的日期
				res = st.executeQuery(this.getPerStrDt(nf));
				if(res.next()){
					strDt = res.getString(1);
				}
			}
			Map<String,Double> fyMap = this.getFyMap(strDt,proj.getProjname(),projcat.getProjcatname(),ckgl.getId());
			//仓库对应的工程项目
			countMap.put(proj.getProjname()+"_"+ckgl.getCkmc(),fyMap.get("countMoney"));
			//保存仓库
			fytj.setCkgl(ckgl);
			//查询月合计
			fytj.setByje(fyMap.get("countMoney"));
			//如果父节点不为空 则累计值存入map
			if(proj.getP_proj()!=null){
				double oldValue = (countMap.get(proj.getP_proj().getProjname()+"_"+ckgl.getCkmc())==null)?0:countMap.get(proj.getP_proj().getProjname()+"_"+ckgl.getCkmc());
				countMap.put(proj.getP_proj().getProjname()+"_"+ckgl.getCkmc(),oldValue+fyMap.get("countMoney"));
			}
			//年份，月份 统计时间
			fytj.setNf(nf);
			fytj.setYf(yf);
			fytj.setTjsj(nf+"-"+yf+"-"+ri);
			//设置统计年月
			fytj.setTjny(tjny);
			this.save(fytj);
			///修改统计任务对象的备注信息
			st.executeUpdate("update tb_wz_tjrw t set t.bz = '正在统计:"+proj.getProjname()+"工程' where t.id = '"+tjrwId+"'");
		}
	}
	/**
	 * 添加所有有子节点的项目合计
	 * @param proj 工程项目 
	 * @param projcat 工程类别
	 * @param nf 年份
	 * @param yf 月份
	 * @param ri 日
	 * @param res 
	 * @param st
	 * @param tjny 统计年月对象
	 * @param tjrw 统计任务对象
	 * @throws SQLException
	 */
	public void saveCountFytj(Proj proj,Projcat projcat,String nf,String yf,String ri,ResultSet res,Statement st,Tjny tjny,String tjrwId,Ckgl ckgl) throws SQLException{
		//如果该父节点 父节点不为空 则累计其父节点值
		if(proj.getC_proj().size()>0){
			Fytj fytj = new Fytj();
			//添加合同类别
			fytj.setProj(proj);
			//返回本月累计
			String strDt = nf+"-"+yf+"-"+ri;
			//如果要统计的月份是1月份 则起始日期是去年统计的截止日期
			if("01".equals(yf)){
				//返回费用统计中 所有日期去年最大的日期
				res = st.executeQuery(this.getPerStrDt(nf));
				if(res.next()){
					strDt = res.getString(1);
				}
			}
			List<Proj> projList = projManager.find("from Proj p where p.p_proj.id =? and p.isviable is null", proj.getId());
			double nlj = 0;
			double ylj = 0;
			//遍历出该父节点下 所有子节点合计值  (用于多级节点）
			for(Proj projs:projList){
				ylj = ylj+countMap.get(projs.getProjname()+"_"+ckgl.getCkmc());
				countMap.put(proj.getProjname()+"_"+ckgl.getCkmc(), ylj);
			}
			Map<String,Double> fyMap = this.getFyMap(strDt,proj.getProjname(),projcat.getProjcatname(),ckgl.getId());
			//添加合计
			fytj.setByje(countMap.get(proj.getProjname()+"_"+ckgl.getCkmc())+fyMap.get("countMoney"));
			fytj.setCkgl(ckgl);
			//年份，月份 统计时间
			fytj.setNf(nf);
			fytj.setYf(yf);
			fytj.setTjsj(nf+"-"+yf+"-"+ri);
			//设置统计年月
			fytj.setTjny(tjny);
			this.save(fytj);
			///修改统计任务对象的备注信息
			st.executeUpdate("update tb_wz_tjrw t set t.bz = '正在统计:"+proj.getProjname()+"工程' where t.id = '"+tjrwId+"'");
			for(Proj projs:proj.getC_proj()){
				this.saveCountFytj(projs, projcat, nf, yf, ri, res, st, tjny, tjrwId,ckgl);
			}
		}
	}
	/**
	 * 新增合计
	 * @param fytjs
	 */
	public void addCount(List<Fytj> fytjs) {
		DecimalFormat decimalFormat = new DecimalFormat("###.00");
		Fytj fy = new Fytj();
		Proj proj = new Proj();
		proj.setProjname("合计");
		fy.getProj();
		fy.setBnlj(0.0);
		fy.setByje(0.0);
//		for(Fytj fytj:fytjs){
////			fy.setBnlj(Double.valueOf(decimalFormat.format(fytj.getBnlj()))+Double.valueOf(decimalFormat.format(fy.getBnlj())));
////			fy.setByje(Double.valueOf(decimalFormat.format(fytj.getByje()))+Double.valueOf(decimalFormat.format(fy.getByje())));
//		}
//		fytjs.add(fy);
	}
	/**
	 * 返回excel 总列数
	 */
	public int getColcount(){
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("oldCount", 0);
		List<Proj> projList  = projManager.find("from Proj as p where p.finished='0' and p.p_proj is null and p.isviable is null");
		for(Proj proj:projList){
			map.put("newCount", 0);
			this.getColcount(proj, map);
			if(map.get("newCount")>map.get("oldCount")){
				map.put("oldCount", map.get("newCount"));
			}
		}
		return map.get("oldCount");
	}
	
	public void getColcount(Proj proj,Map<String,Integer> map){
		if(proj.getC_proj().size()>0){
			int count = map.get("newCount")+1;
			map.put("newCount",count);
			for(Proj projs:proj.getC_proj()){
				this.getColcount(projs, map);
			}
		}
	}
	/**
	 * 根据起始时间 工程项目 工程类别 从验货单明细中取出费用金额
	 * @param strDt 开始时间
	 * @param endDt 结束时间
	 * @param gcxm  工程项目
	 * @param gclb  工程类别
	 * 废弃方法
	 * @return
	 */
	private String getCgjhSql(String strDt,String gcxm,String gclb){
		return "select sum(t.jhdj * t.sjlysl) as je from tb_wz_ylydmx t "+
		 	    " where t.lydbh in (select a.id from tb_wz_ylyd a "+
		        " where a.gclb = '"+gclb+"' and a.gcxm = '"+gcxm+"'"+
		        " and a.lysj >= add_months(to_date('"+strDt+"', 'yyyy-mm-dd'),-1) " +
		        " and a.lysj < to_date('"+strDt+"', 'yyyy-mm-dd'))  and t.zt='1'";
	}
	/**
	 * 根据起始时间 工程项目 工程类别 从验货单明细中取出费用金额
	 * @param strDt 开始时间
	 * @param endDt 结束时间
	 * @param gcxm  工程项目
	 * @param gclb  工程类别
	 * @return
	 */
	private Map<String,Double> getFyMap(String strDt,String gcxm,String gclb,String ckId){
		String hql = " from Lydmx l where l.gclb = '"+gclb+"' and l.gcxm = '"+gcxm+"'" +
					 " and l.lysj >= add_months(to_date('"+strDt+"', 'yyyy-mm-dd'),-1) " +
					 " and l.lysj < to_date('"+strDt+"', 'yyyy-mm-dd') and l.zt = '1' and l.wzbm.kw.ckid='"+ckId+"'";
		Map<String,Double> countMap = TjrwUtils.getCkMap(hql);
		return countMap;
	}

	/**
	 * 根据起始时间 工程项目 工程类别 从领用单明细中取出费用金额 (年累计)
	 * @param strDt 开始时间
	 * @param endDt 结束时间
	 * @param gcxm  工程项目
	 * @param gclb  工程类别
	 * @return 弃用
	 */
	private Map<String,Double>  getFyNljMap(String strDt,String endDt,String gcxm,String gclb){
		String hql = " from Lydmx l where l.gclb = '"+gclb+"' and l.gcxm = '"+gcxm+"'" +
		 " and l.lysj >= to_date('"+strDt+"', 'yyyy-mm-dd') " +
		 " and l.lysj < to_date('"+endDt+"', 'yyyy-mm-dd') and l.zt = '1'";
		Map<String,Double> countMap = TjrwUtils.getCkMap(hql);
		return countMap;
	}
	/**
	 * 根据起始时间 工程项目 工程类别 从验货单明细中取出费用金额 (年累计)
	 * @param strDt 开始时间
	 * @param endDt 结束时间
	 * @param gcxm  工程项目
	 * @param gclb  工程类别
	 * @return
	 * 弃用
	 */
//	private String getCgjhNljSql(String strDt,String endDt,String gcxm,String gclb){
//		return "select sum(t.jhdj * t.sjlysl) as je from tb_wz_ylydmx t "+
//		 	    " where t.lydbh in (select a.id from tb_wz_ylyd a "+
//		        " where a.gclb = '"+gclb+"' and a.gcxm = '"+gcxm+"'"+
//		        " and a.lysj > to_date('"+strDt+"', 'yyyy-mm-dd') " +
//		        " and a.lysj < to_date('"+endDt+"', 'yyyy-mm-dd')) and t.zt='1'";
//	}
	/**
	 * 返回费用统计中去年最后一个月统计日期
	 * @return
	 */
	public String getPerStrDt(String nf){
		return "select t.tjsj as maxDt from tb_wz_twzfytjb t where t.nf = '"+(Integer.parseInt(nf)-1)+"' order by t.tjsj desc";
	}
	
	public void setTjnyManager(TjnyManager tjnyManager) {
		this.tjnyManager = tjnyManager;
	}
	 
	public void setProjManager(ProjManager projManager) {
		this.projManager = projManager;
	}
	public void setProjcatManager(ProjcatManager projcatManager) {
		this.projcatManager = projcatManager;
	}
	public int getMaxYear() {
		return maxYear;
	}
	public int getMaxMoth() {
		return maxMoth;
	}
	public void setCkglManager(CkglManager ckglManager) {
		this.ckglManager = ckglManager;
	}
	
}	
