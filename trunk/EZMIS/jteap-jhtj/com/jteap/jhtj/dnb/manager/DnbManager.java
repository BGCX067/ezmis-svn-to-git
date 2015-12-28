package com.jteap.jhtj.dnb.manager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.util.Assert;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.jhtj.chart.MSLineApplication;
import com.jteap.jhtj.chart.MSLineApply;
import com.jteap.jhtj.chart.MSLineCategories;
import com.jteap.jhtj.chart.MSLineCategory;
import com.jteap.jhtj.chart.MSLineChart;
import com.jteap.jhtj.chart.MSLineDataSet;
import com.jteap.jhtj.chart.MSLineDefinition;
import com.jteap.jhtj.chart.MSLineSet;
import com.jteap.jhtj.chart.MSLineStyle;
import com.jteap.jhtj.chart.MSLineStyles;
import com.jteap.jhtj.dnb.model.Dnb;
import com.jteap.jhtj.dnb.model.DnbData;
@SuppressWarnings({ "unchecked", "serial"})
public class DnbManager extends HibernateEntityDao<Dnb> {
	private DataSource dataSource;
	/**
	 * 
	 * 描述:得到当前日期的最新的一条数据
	 * 时间:2010 11 9
	 * 作者:童贝
	 * 参数:
	 * 返回值:DnbData
	 * 抛出异常:
	 * @throws SQLException 
	 */
	public DnbData getNewDataByTimeAndID(String id,String curDate) throws SQLException{
		if(StringUtil.isEmpty(id)||StringUtil.isEmpty(curDate)){
			return null;
		}
		Dnb dnb=(Dnb) this.findUniqueByHql("from Dnb where id="+id);
		DnbData res=new DnbData();
		Connection conn=null;
		try{
			//得到最新的表码读数
			conn=this.dataSource.getConnection();
			Statement state=conn.createStatement();
			Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
			String curNian=DateUtils.getDate(curD, "yyyy");       //当前年
			String curYue=DateUtils.getDate(curD, "MM");		  //当前月
			String sql="select * from elec.elec_z_"+curNian+curYue+" where elecid="+id+" and times like to_date('"+curDate+"','yyyy-MM-dd') order by times desc";
			ResultSet rs=state.executeQuery(sql);
			if(rs.next()){
				Date times=rs.getDate("TIMES");
				Double pz=rs.getDouble("PZ");
				Double pf=rs.getDouble("PF");
				Double qz=rs.getDouble("QZ");
				Double qf=rs.getDouble("QF");
				res.setPf(this.round2(pf, 4));
				res.setPz(this.round2(pz, 4));
				res.setQf(this.round2(qf, 4));
				res.setQz(this.round2(qz, 4));
				res.setTimes(times);
			}
			
			//本日累计电量
			DnbData ress=new DnbData();
			String rsql="select * from elec.elec_z_"+curNian+curYue+" where elecid="+id+" and times=to_date('"+curDate+"','yyyy-MM-dd')";
			Statement rstate=conn.createStatement();
			ResultSet rrs=rstate.executeQuery(rsql);
			if(rrs.next()){
				Date times=rrs.getDate("TIMES");
				Double pz=rrs.getDouble("PZ");
				Double pf=rrs.getDouble("PF");
				Double qz=rrs.getDouble("QZ");
				Double qf=rrs.getDouble("QF");
				ress.setPf(this.round2(pf, 4));
				ress.setPz(this.round2(pz, 4));
				ress.setQf(this.round2(qf, 4));
				ress.setQz(this.round2(qz, 4));
				ress.setTimes(times);
			}
			//本月累计电量
			DnbData resy=new DnbData();
			String ysql="select * from elec.elec_z_"+curNian+curYue+" where elecid="+id+" and times=to_date('"+curNian+"-"+curYue+"-1','yyyy-MM-dd')";
			Statement ystate=conn.createStatement();
			ResultSet yrs=ystate.executeQuery(ysql);
			if(yrs.next()){
				Date times=yrs.getDate("TIMES");
				Double pz=yrs.getDouble("PZ");
				Double pf=yrs.getDouble("PF");
				Double qz=yrs.getDouble("QZ");
				Double qf=yrs.getDouble("QF");
				resy.setPf(this.round2(pf, 4));
				resy.setPz(this.round2(pz, 4));
				resy.setQf(this.round2(qf, 4));
				resy.setQz(this.round2(qz, 4));
				resy.setTimes(times);
			}
			
			String sysDate=DateUtils.getDate("yyyy-MM-dd");
			long compare = DnbManager.compareStringTime(curDate, sysDate, "yyyy-MM-dd");
			//如果是当天
			if(compare==0){
				//日累计电量=(最新表码数-当日0点表码数)*PT*CT
				Double brpf=(res.getPf()-ress.getPf())*dnb.getPt()*dnb.getCt();
				Double brpz=(res.getPz()-ress.getPz())*dnb.getPt()*dnb.getCt();
				Double brqf=(res.getQf()-ress.getQf())*dnb.getPt()*dnb.getCt();
				Double brqz=(res.getQz()-ress.getQz())*dnb.getPt()*dnb.getCt();
				res.setBrpf(this.round2(brpf, 4));
				res.setBrpz(this.round2(brpz, 4));
				res.setBrqf(this.round2(brqf, 4));
				res.setBrqz(this.round2(brqz, 4));
				//月累计电量=（最新表码数-当月1日0点表码数）*PT*CT
				Double bypf=(res.getPf()-resy.getPf())*dnb.getPt()*dnb.getCt();
				Double bypz=(res.getPz()-resy.getPz())*dnb.getPt()*dnb.getCt();
				Double byqf=(res.getQf()-resy.getQf())*dnb.getPt()*dnb.getCt();
				Double byqz=(res.getQz()-resy.getQz())*dnb.getPt()*dnb.getCt();
				res.setBypf(this.round2(bypf, 4));
				res.setBypz(this.round2(bypz, 4));
				res.setByqf(this.round2(byqf, 4));
				res.setByqz(this.round2(byqz, 4));
			}else if(compare<0){
				//日期推后一天，去后一天的零点
				//如果选的是11月2日，日累计电量=（11月3日0点表码数-11月2号0点表码数）*PT*CT
				String nextDate=DateUtils.endDate(DateUtils.StrToDate(curDate, "yyyy-MM-dd"));
				String nextNian=DateUtils.getDate(DateUtils.StrToDate(nextDate, "yyyy-MM-dd"), "yyyy");
				String nextYue=DateUtils.getDate(DateUtils.StrToDate(nextDate, "yyyy-MM-dd"), "MM");
				String nsql="select * from elec.elec_z_"+nextNian+nextYue+" where elecid="+id+" and times=to_date('"+nextDate+"','yyyy-MM-dd')";
				Statement nstate=conn.createStatement();
				ResultSet nrs=nstate.executeQuery(nsql);
				if(nrs.next()){
					Double pz=nrs.getDouble("PZ");
					Double pf=nrs.getDouble("PF");
					Double qz=nrs.getDouble("QZ");
					Double qf=nrs.getDouble("QF");
					Double brpf=(pf-ress.getPf())*dnb.getPt()*dnb.getCt();
					Double brpz=(pz-ress.getPz())*dnb.getPt()*dnb.getCt();
					Double brqf=(qf-ress.getQf())*dnb.getPt()*dnb.getCt();
					Double brqz=(qz-ress.getQz())*dnb.getPt()*dnb.getCt();
					res.setBrpf(this.round2(brpf, 4));
					res.setBrpz(this.round2(brpz, 4));
					res.setBrqf(this.round2(brqf, 4));
					res.setBrqz(this.round2(brqz, 4));
					Double bypf=(pf-resy.getPf())*dnb.getPt()*dnb.getCt();
					Double bypz=(pz-resy.getPz())*dnb.getPt()*dnb.getCt();
					Double byqf=(qf-resy.getQf())*dnb.getPt()*dnb.getCt();
					Double byqz=(qz-resy.getQz())*dnb.getPt()*dnb.getCt();
					res.setBypf(this.round2(bypf, 4 ));
					res.setBypz(this.round2(bypz, 4));
					res.setByqf(this.round2(byqf, 4));
					res.setByqz(this.round2(byqz, 4));
				}
			}
		}catch(Exception e){
			
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return res;
	}
	
	/**
	 * 
	 * 描述:生成图形
	 * 时间:2010 11 9
	 * 作者:童贝
	 * 参数:
	 * 返回值:MSLineChart
	 * 抛出异常:
	 */
	public MSLineChart generateChart(String id,String curDate,String type) throws Exception{
		//图形
		MSLineChart chart=new MSLineChart();
		//chart.setCaption(this.getInameByKidAndItem(kid, item));
		chart.setSubcaption("");
		chart.setLabelStep("1");
		chart.setBaseFontSize("12");
		//标题
		MSLineCategories mSLineCategories=new MSLineCategories();
		
		//数据集
		MSLineDataSet dataSet=new MSLineDataSet();
		dataSet.setSeriesName("");
		String [] color={"F1683C","2AD62A"};
		dataSet.setColor(color[0]);
		dataSet.setAnchorBgColor(color[0]);
		dataSet.setAnchorBorderColor(color[0]);
		
		Connection conn=this.dataSource.getConnection();
		Statement state=conn.createStatement();
		ResultSet rs=null;
	
		try{
			int flag=0;
			Double maxValue=0d;
			Double minValue=0d;
			Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
			String curNian=DateUtils.getDate(curD, "yyyy");       //当前年
			String curYue=DateUtils.getDate(curD, "MM");		  //当前月
			String sql="select * from elec.elec_z_"+curNian+curYue+" where elecid="+id+" and times like to_date('"+curDate+"','yyyy-MM-dd') order by times asc";
			rs=state.executeQuery(sql);
			while(rs.next()){
				Date times=rs.getTimestamp("TIMES");
				MSLineCategory category=new MSLineCategory();
				category.setLabel(DateUtils.getDate(times, "HH:mm"));
				mSLineCategories.getCategorys().add(category);
				String value="0";
				if("PZ".equals(type)){
					Double pz=rs.getDouble("PZ");
					pz=this.round2(pz, 4);
					if(maxValue<pz){
						maxValue=pz;
					}
					if(flag==0){
						minValue=pz;
					}
					if(minValue>pz){
						minValue=pz;
					}
					value=pz.toString();
				}else if("PF".equals(type)){
					Double pf=rs.getDouble("PF");
					pf=this.round2(pf, 4);
					if(maxValue<pf){
						maxValue=pf;
					}
					if(flag==0){
						minValue=pf;
					}
					if(minValue>pf){
						minValue=pf;
					}
					value=pf.toString();
				}else if("QZ".equals(type)){
					Double qz=rs.getDouble("QZ");
					qz=this.round2(qz, 4);
					if(maxValue<qz){
						maxValue=qz;
					}
					if(flag==0){
						minValue=qz;
					}
					if(minValue>qz){
						minValue=qz;
					}
					value=qz.toString();
				}else if("QF".equals(type)){
					Double qf=rs.getDouble("QF");
					qf=this.round2(qf, 4);
					if(maxValue<qf){
						maxValue=qf;
					}
					if(flag==0){
						minValue=qf;
					}
					if(minValue>qf){
						minValue=qf;
					}
					value=qf.toString();
				}
				MSLineSet set=new MSLineSet();
				set.setValue(value);
				dataSet.getMSLineSets().add(set);
				flag++;
			}
			//Integer maxInt=maxValue.intValue()/10*10;
			//Integer minInt=minValue.intValue()/10*10;
			chart.setYAxisMaxValue(this.getHightNumber(maxValue*2).toString());
			chart.setYAxisMinValue(this.getHightNumber(minValue/2).toString());
			chart.setLabelStep(new Integer(flag/10).toString());
			//chart.setYAxisValuesStep(new Double(maxValue/10).toString());
			chart.getMSLineDataSets().add(dataSet);
			
			//下面公共的样式属性
			MSLineStyle style=new MSLineStyle();
			style.setName("captionFont");
			MSLineDefinition defin=new MSLineDefinition();
			defin.setMSLineStyle(style);
			
			MSLineApply apply=new MSLineApply();
			MSLineApplication applition=new MSLineApplication();
			applition.getMSLineApplys().add(apply);
			
			MSLineStyles styles=new MSLineStyles();
			styles.setMSLineApplication(applition);
			styles.setMSLineDefinition(defin);
			
			
			chart.setMSLineCategories(mSLineCategories);
			chart.setMSLineStyles(styles);
			System.out.println(chart+"----------------");
		}catch(Exception e){
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(state!=null){
				state.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return chart;
	}
	
	public static long compareStringTime(String t1, String t2, String parrten) {
		SimpleDateFormat formatter = new SimpleDateFormat(parrten);
		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos1 = new ParsePosition(0);
		Date dt1 = formatter.parse(t1, pos);
		Date dt2 = formatter.parse(t2, pos1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt1);
		long day1 = calendar.getTimeInMillis();
		calendar.setTime(dt2);
		long day2 = calendar.getTimeInMillis();
		
		return (day1 - day2);
	}
	
	/**
	 * 
	 * 描述:得到当日的所有数据
	 * 时间:2010 11 10
	 * 作者:童贝
	 * 参数:
	 * 返回值:Page
	 * 抛出异常:
	 * @throws Exception 
	 */
	public Page getListByCurdate(String id,String curDate,int start,int limit) throws Exception{
		Page page=null;
		
		Date curD=DateUtils.StrToDate(curDate, "yyyy-MM-dd");
		String curNian=DateUtils.getDate(curD, "yyyy");       //当前年
		String curYue=DateUtils.getDate(curD, "MM");		  //当前月
		String sql="select * from elec.elec_z_"+curNian+curYue+" where elecid="+id+" and times like to_date('"+curDate+"','yyyy-MM-dd') order by times desc";
		
		page=this.pagedQueryTableData(sql, start, limit);
		return page;
	}
	
	
	/**
	 * 分页查询指定sql数据
	 * @param sql
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQueryTableData(String sql, int start, int limit) throws Exception {
		
		String countSql = " select count (*) " + removeSelect(removeOrders(sql));
		Connection conn=this.dataSource.getConnection();
		try{
			
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(countSql);
			int count=0;
			if(rs.next()){
				count=rs.getInt(1);
			}
			rs.close();
			
			if(count<1)
				return new Page();

			sql="SELECT * FROM(SELECT A.*, ROWNUM RN FROM ("+sql+") A WHERE ROWNUM <= "+(start+limit)+") WHERE RN >= "+(start+1);
			
			rs=st.executeQuery(sql);
			List list=new ArrayList();
			
			while(rs.next()){
				Map map=new HashMap();
				
				Date times=rs.getTimestamp("TIMES");
				Double pz=rs.getDouble("PZ");
				Double pf=rs.getDouble("PF");
				Double qz=rs.getDouble("QZ");
				Double qf=rs.getDouble("QF");
				map.put("PZ", this.round2(pz, 4));
				map.put("PF", this.round2(pf, 4));
				map.put("QZ", this.round2(qz, 4));
				map.put("QF", this.round2(qf, 4));
				map.put("TIMES", times.getTime());
				
				list.add(map);
			}
			rs.close();
			return new Page(start, count, limit, list);
		}catch(Exception ex){
			throw ex;
		}finally{
			conn.close();
		}
		
	}
	
	
	/**
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 *
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 *
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	public double round(String v, int scale,boolean isAdd){
		double result=0d;
		String value=v;
		if(value.indexOf(".")>-1){
			String zs=value.substring(0,value.indexOf("."));
			String xs=value.substring(value.indexOf(".")+1);
			//判断小数位数是否比保留的位数要大
			if(xs.length()>scale){
				String lastNum=xs.substring(xs.length()-1);
				Integer num=Integer.parseInt(lastNum);
				if(num>=5){
					String jyNum=xs.substring(xs.length()-2,xs.length()-1);
					Integer jy=Integer.parseInt(jyNum);
					jy=jy+1;
					String res=zs+"."+xs.substring(0,xs.length()-1);
					return round(res,scale,true);
				}else{
					if(isAdd){
						String jyNum=xs.substring(xs.length()-1);
						Integer jy=Integer.parseInt(jyNum);
						jy=jy+1;
						String res=zs+"."+xs.substring(0,xs.length()-1)+jy;
						return round(res,scale,false);
					}else{
						String res=value.substring(0,value.length()-1);
						return round(res,scale,false);
					}
				}
			}else{
				if(isAdd){
					String jyNum=xs.substring(xs.length()-1);
					Integer jy=Integer.parseInt(jyNum);
					jy=jy+1;
					String res="";
					if(jy==10){
						res=new Double(round2(new Double(v).doubleValue(), scale)).toString();
					}else{
						res=zs+"."+xs.substring(0,xs.length()-1)+jy;
					}
					return Double.parseDouble(res);
				}else{
					result=new Double(v).doubleValue();
				}
			}
			
		}else{
			result=new Double(v).doubleValue();
		}
		return result;
	}
	
	/**
	 * 
	 * 描述:得到最高位取整
	 * 时间:2010 11 10
	 * 作者:童贝
	 * 参数:
	 * 返回值:Double
	 * 抛出异常:
	 */
	public Double getHightNumber(Double number){
		if(number.doubleValue()==0d){
			return number;
		}
		Integer curNum=number.intValue();
		Integer basicNum=new Double(Math.pow(10,curNum.toString().length()-1)).intValue();
		Double res=new Double((curNum/basicNum)*basicNum);
		return res;
	}
	
	public static void main(String[] args) {
//		Double d=1.6;
//		System.out.println(getHightNumber(d));
	}
	
	public double round2(double v, int scale){
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, BigDecimal.ROUND_UP).doubleValue();
	}
	
}
