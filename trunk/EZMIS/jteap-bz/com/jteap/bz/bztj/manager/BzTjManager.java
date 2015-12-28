package com.jteap.bz.bztj.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.jteap.system.jdbc.manager.JdbcManager;
/**
 * 班组统计处理类
 * @author lvchao
 *
 */
@SuppressWarnings({"unchecked","serial"})
public class BzTjManager extends JdbcManager{
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String getJhDt(String staDt,String endDt) throws Exception{
		DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		ca.setTime(sf.parse(staDt));
		int count = 0;
		while(true){
			int week = ca.get(Calendar.DAY_OF_WEEK)-1;
			if(week!=6&&week!=0){
				count++;
			}
			
			if(sf.format(ca.getTime()).equals(endDt)){
				break;
			}
			ca.add(Calendar.DAY_OF_MONTH, 1);
		}
		return count+"";
	}
	/** 
	 * 返回班组统计
	 * @throws Exception 
	 */
	public List getbztjCount(String sta,String end) throws Exception{
		String sql = "select id,bzm from TB_FORM_BZINFO";
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		Map<String, String> bzMap = null;
		String staNF = sta.split("-")[0];
		String endNF = end.split("-")[0];
		String staStr = sta.split("-")[0]+"年"+sta.split("-")[1]+"月"+sta.split("-")[2]+"日";
		String endStr = end.split("-")[0]+"年"+end.split("-")[1]+"月"+end.split("-")[2]+"日";
		boolean flag = false;
		if(sta.equals(end)){
			flag = true;
		}
		//System.out.println(sql);
		List list = new ArrayList();
		//计划数量
		String jhsl = this.getJhDt(sta, end);
		try{
			conn = dataSource.getConnection();
			st = conn.createStatement();
			res = st.executeQuery(sql);
			while(res.next()){
				if(res.getString(2).indexOf("一值")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
					
					//System.out.println(yzsql);
					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_YZ_JHSL"+i,jhsl);
 							bzMap.put("SJ_YZ"+i,res1.getString(i));
 						}
 					}
					list.add(bzMap);
					//System.out.println("一值");
				} 
				if(res.getString(2).indexOf("二值")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_ERZ_JHSL"+i,jhsl);
 							bzMap.put("SJ_ERZ"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("二值");
				}
				if(res.getString(2).indexOf("三值")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_SANZ_JHSL"+i,jhsl);
 							bzMap.put("SJ_SANZ"+i,res1.getString(i));
 						} 
 					}
 					list.add(bzMap);
 					//System.out.println("三值");
				}
				if(res.getString(2).indexOf("四值")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_SIZ_JHSL"+i,jhsl);
 							bzMap.put("SJ_SIZ"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("四值");
				}
				if(res.getString(2).indexOf("五值")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_WUZ_JHSL"+i,jhsl);
 							bzMap.put("SJ_WUZ"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("五值");
				}
				if(res.getString(2).indexOf("化水班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_HUAS_JHSL"+i,jhsl);
 							bzMap.put("SJ_HUAS"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("化水班");
				}
				if(res.getString(2).indexOf("发电部 化验班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_HUAY_JHSL"+i,jhsl);
 							bzMap.put("SJ_HUAY"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("化验班");
				}
				if(res.getString(2).indexOf("热工一班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_RGY_JHSL"+i,jhsl);
 							bzMap.put("SJ_RGY"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("热工一班");
				}
				if(res.getString(2).indexOf("热工二班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_RGE_JHSL"+i,jhsl);
 							bzMap.put("SJ_RGE"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("热工二班");
				}
				if(res.getString(2).indexOf("电试班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_DIANS_JHSL"+i,jhsl);
 							bzMap.put("SJ_DIANS"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("电试班");
				}
				if(res.getString(2).indexOf("电气检修班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_DIANQ_JHSL"+i,jhsl);
 							bzMap.put("SJ_DIANQ"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("电气检修");
				}
				if(res.getString(2).indexOf("锅炉检修班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_GUOL_JHSL"+i,jhsl);
 							bzMap.put("SJ_GUOL"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("锅炉检修");
				}
				if(res.getString(2).indexOf("汽机检修班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_QIJ_JHSL"+i,jhsl);
 							bzMap.put("SJ_QIJ"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("汽机检修");
				}
				if(res.getString(2).indexOf("通讯班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_TONGX_JHSL"+i,jhsl);
 							bzMap.put("SJ_TONGX"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("通讯板");
				}
				if(res.getString(2).indexOf("取样班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_QUY_JHSL"+i,jhsl);
 							bzMap.put("SJ_QUY"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("取样班");
				}
				if(res.getString(2).indexOf("制样班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_ZHIY_JHSL"+i,jhsl);
 							bzMap.put("SJ_ZHIY"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("制样班");
				}
				if(res.getString(2).indexOf("煤管化验班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_MEIG_JHSL"+i,jhsl);
 							bzMap.put("SJ_MEIG"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("媒管化验班");
				}
				if(res.getString(2).indexOf("电控检修班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_DIANK_JHSL"+i,jhsl);
 							bzMap.put("SJ_DIANK"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("电控检修");
				}
				if(res.getString(2).indexOf("机械检修班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_JIX_JHSL"+i,jhsl);
 							bzMap.put("SJ_JIX"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("机械检修");
				}
				if(res.getString(2).indexOf("汽车班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_QIC_JHSL"+i,jhsl);
 							bzMap.put("SJ_QIC"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("汽车班");
				}
				if(res.getString(2).indexOf("经管办")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i = 1;i<=14;i++){
 							bzMap.put("JH_JINGG_JHSL"+i,jhsl);
 							bzMap.put("SJ_JINGG"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("经管办");
				}
				if(res.getString(2).indexOf("综合班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_ZONGH_JHSL"+i,jhsl);
 							bzMap.put("SJ_ZONGH"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("综合班");
				}
				if(res.getString(2).indexOf("招待所")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i =1;i<=14;i++){
 							bzMap.put("JH_ZHAOD_JHSL"+i,jhsl);
 							bzMap.put("SJ_ZHAOD"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("招待所");
				}
				if(res.getString(2).indexOf("汽修班")!=-1){
					bzMap = new HashMap<String, String>();
					String yzsql;
					//如果是当天查询
					if(flag){
						yzsql = this.returnBztjSqlBydt(res.getString(1),sta,staNF,staStr);
					}else{
						yzsql = this.returnBztjSql(res.getString(1), sta, end,staNF,endNF,staStr,endStr);
					}
  					st = conn.createStatement();
 					ResultSet res1 =  st.executeQuery(yzsql);
 					bzMap.put("bzid",res.getString(1));
 					if(res1.next()){
 						for(int i=1;i<=14;i++){
 							bzMap.put("JH_QIXIU_JHSL"+i,jhsl);
 							bzMap.put("SJ_QIXIU"+i,res1.getString(i));
 						}
 					}
 					list.add(bzMap);
 					//System.out.println("汽修");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**
	 * 返回班组统计sql
	 * @param bzid	班组id
	 * @param sta	开始时间 日期型
	 * @param end	结束时间 日期型
	 * @param staNf 开始年份
	 * @param endNf 结束年份
	 * @param staStr 开始时间 yyyy年mm月dd日
	 * @param endStr 结束时间 yyyy年mm月dd日
	 * @return
	 */
	public String returnBztjSql(String bzid,String sta,String end,String staNf,String endNf,String staStr,String endStr){
		StringBuffer yzsql = new StringBuffer("select ");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_BWHJL z1 Where z1.SJ >= to_date('"+sta+"','yyyy-mm-dd')and z1.SJ <= to_date('"+end+"','yyyy-mm-dd')and z1.bzbm= '"+bzid+"') as z3,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_BZRZ z1 Where z1.SJ >= to_date('"+sta+"','yyyy-mm-dd')and z1.SJ <= to_date('"+end+"','yyyy-mm-dd')and z1.bzbm= '"+bzid+"') as z4,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NGZJH z1 Where z1.NF>='"+staNf+"' and z1.NF<='"+endNf+"' and z1.bzbm='"+bzid+"') as z11,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_ZDGZZJ z1 Where z1.NF>='"+staNf+"' and z1.NF<='"+endNf+"' and z1.bzbm='"+bzid+"') as z15,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NGZZJ z1 Where z1.NF>='"+staNf+"' and z1.NF<='"+endNf+"' and z1.bzbm='"+bzid+"') as z12,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_CQJAQDJCZJ z1 Where z1.NF>='"+staNf+"' and z1.NF<='"+endNf+"' and z1.bzbm='"+bzid+"') as z6,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_AQFXZRCBS z1 Where z1.SCSJ>='"+staStr+"' and z1.SCSJ<='"+endStr+"' and z1.bzbm='"+bzid+"') as z5,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_AQXXJL z1 Where z1.RQ >= to_date('"+sta+"','yyyy-mm-dd')and z1.RQ <= to_date('"+end+"','yyyy-mm-dd')and z1.bzbm= '"+bzid+"') as z2,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_AQPXJL z1 Where z1.KSSJ >= to_date('"+sta+"','yyyy-mm-dd')and z1.KSSJ <= to_date('"+end+"','yyyy-mm-dd')and z1.bzbm= '"+bzid+"') as z1,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NDPXJH z1 Where z1.NF>='"+staNf+"' and z1.NF<='"+endNf+"' and z1.bzbm='"+bzid+"') as z9,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NDPXZJ z1 Where z1.NF>='"+staNf+"' and z1.NF<='"+endNf+"' and z1.bzbm='"+bzid+"') as z10,");
		yzsql.append("  (select count(z7.id) as a from TB_FORM_FSGYXJL z7 Where z7.YXQSSJ>=to_date('"+sta+"','yyyy-mm-dd') and z7.YXQSSJ<=to_date('"+end+"','yyyy-mm-dd') and z7.bzbm='"+bzid+"') as z7,");
		yzsql.append("  (select count(z13.id) as a from TB_FORM_SGYXJL z13 Where z13.RQ>='"+sta+"' and z13.RQ<='"+end+"' and z13.bzbm='"+bzid+"') as z13,");
		yzsql.append("  (select count(z13.id) as a from TB_FORM_YJSPXJL z13 Where z13.JKSJ>=to_date('"+sta+"','yyyy-mm-dd') and z13.JKSJ<=to_date('"+end+"','yyyy-mm-dd') and z13.bzbm='"+bzid+"') as z14");
		yzsql.append(" from dual");
		return yzsql.toString();
	}
	/**
	 * 返回班组统计sql
	 * @param bzid	班组id
	 * @param sta	开始时间 日期型
	 * @param staNf 开始年份
	 * @param staStr 开始时间 yyyy年mm月dd日
	 * @return
	 */
	public String returnBztjSqlBydt(String bzid,String sta,String staNf,String staStr){
		StringBuffer yzsql = new StringBuffer("select ");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_BWHJL z1 Where to_char(z1.SJ,'yyyy-mm-dd') = '"+sta+"' and z1.bzbm= '"+bzid+"') as z3,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_BZRZ z1 Where to_char(z1.SJ,'yyyy-mm-dd') = '"+sta+"' and z1.bzbm= '"+bzid+"') as z4,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NGZJH z1 Where z1.NF='"+staNf+"'  and z1.bzbm='"+bzid+"') as z11,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_ZDGZZJ z1 Where z1.NF='"+staNf+"'  and z1.bzbm='"+bzid+"') as z15,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NGZZJ z1 Where z1.NF='"+staNf+"' and z1.bzbm='"+bzid+"') as z12,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_CQJAQDJCZJ z1 Where z1.NF='"+staNf+"'  and z1.bzbm='"+bzid+"') as z6,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_AQFXZRCBS z1 Where z1.SCSJ='"+staStr+"'  and z1.bzbm='"+bzid+"') as z5,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_AQXXJL z1 Where to_char(z1.RQ,'yyyy-mm-dd') = '"+sta+"' and z1.bzbm= '"+bzid+"') as z2,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_AQPXJL z1 Where to_char(z1.KSSJ,'yyyy-mm-dd') = '"+sta+"' and z1.bzbm= '"+bzid+"') as z1,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NDPXJH z1 Where z1.NF='"+staNf+"'  and z1.bzbm='"+bzid+"') as z9,");
		yzsql.append("  (select count(z1.id) as a from TB_FORM_NDPXZJ z1 Where z1.NF='"+staNf+"'  and z1.bzbm='"+bzid+"') as z10,");
		yzsql.append("  (select count(z7.id) as a from TB_FORM_FSGYXJL z7 Where to_char(z7.YXQSSJ,'yyyy-mm-dd')='"+sta+"' and z7.bzbm='"+bzid+"') as z7,");
		yzsql.append("  (select count(z13.id) as a from TB_FORM_SGYXJL z13 Where z13.RQ='"+sta+"'  and z13.bzbm='"+bzid+"') as z13,");
		yzsql.append("  (select count(z13.id) as a from TB_FORM_YJSPXJL z13 Where to_char(z13.JKSJ,'yyyy-mm-dd')='"+sta+"' and z13.bzbm='"+bzid+"') as z14");
		yzsql.append(" from dual");
		//System.out.println("++++"+yzsql.toString());
		return yzsql.toString();
	}
	
	/**
	 * 查看同一时间段中是否有统计过
	 * @param staDt
	 * @param endDt
	 * @return
	 * @throws Exception 
	 */
	public boolean findByqssj(String staDt, String endDt) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		conn = dataSource.getConnection();
		st = conn.createStatement();
		res = st.executeQuery("select t.* from TB_BZ_BZTJ t where t.strdt=to_date('"+staDt+"','yyyy-mm-dd') and t.enddt=to_date('"+endDt+"','yyyy-mm-dd')");
		if(res.next()){
			return true;
		}
		return false;
	}
	 
}
