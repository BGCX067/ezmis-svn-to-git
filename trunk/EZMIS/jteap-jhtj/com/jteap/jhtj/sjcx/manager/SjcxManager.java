package com.jteap.jhtj.sjcx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;


import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.utils.DateUtils;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjwh.model.KeyModel;

public class SjcxManager extends HibernateEntityDao<TjItemKind> {
	private DataSource dataSource;
	/**
	 * 
	 *描述：查找数据
	 *时间：2010-4-30
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 */
	public Page findDynaDataBykid(String kid,String flflag,String keyList,int start, int limit) throws SQLException{
		Page page=new Page();
		//List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		try{
			//存在查询条件
			Map<String,String> keyMap=new HashMap<String, String>();
			String[] keys=keyList.split("!");
			StringBuffer keyWhere=new StringBuffer();
			String orderWhere="";
			for(String key:keys){
				String[] nameAndValue=key.split(",");
				if(nameAndValue.length==2){
					String name=nameAndValue[0];
					String value=nameAndValue[1];
					keyMap.put(name, value);
				}
			}
			//日数据
			if("1".equals(flflag)){
				String startNian=keyMap.get(TjInterFace.SNIAN);
				String startYue=keyMap.get(TjInterFace.SYUE);
				String startRi=keyMap.get(TjInterFace.SRI);
				String endNian=keyMap.get(TjInterFace.ENIAN);
				String endYue=keyMap.get(TjInterFace.EYUE);
				String endRi=keyMap.get(TjInterFace.ERI);
//				while(true){
//					if(startNian.equals(endNian)&&startYue.equals(endYue)){
//						int start=new Integer(startRi);
//						int end=new Integer(endRi);
//						for(int i=start;i<=end;i++){
//							keyWhere.append("("+TjInterFace.NIAN+"="+startNian+" and "+TjInterFace.YUE+"="+startYue+" and RI="+i+") or ");
//						}
//						break;
//					}else{
//						int start=new Integer(startRi);
//						//本月的最后一天
//						String lastDate=DateUtils.getLastDate(startNian+"-"+startYue+"-1");
//						String endDate=DateUtils.getDate(DateUtils.StrToDate(lastDate,""),"dd");
//						endDate=(endDate.charAt(0)=='0'?endDate.substring(1):endDate);
//						int end=new Integer(endDate);
//						for(int i=start;i<=end;i++){
//							keyWhere.append("("+TjInterFace.NIAN+"="+startNian+" and "+TjInterFace.YUE+"="+startYue+" and RI="+i+") or ");
//						}
//						//把日期往后推一个月
//						Date curDate=DateUtils.StrToDate(startNian+"-"+startYue+"-"+startRi, "yyyy-MM-dd");
//						String nextDate=DateUtils.getNextDate(curDate, 1, "yyyy-MM-dd");
//						startNian=DateUtils.getDate(DateUtils.StrToDate(nextDate,""), "yyyy");
//						startYue=DateUtils.getDate(DateUtils.StrToDate(nextDate,""), "MM");
//						startYue=(startYue.charAt(0)=='0'?startYue.substring(1):startYue);
//						startRi="1";
//					}
//				}
				
				keyWhere.append("((NIAN="+startNian+" and YUE="+startYue+" and RI>="+startRi+") or (NIAN="+startNian+" and YUE>"+startYue+") or (NIAN="+endNian+" and YUE<"+endYue+") or (NIAN="+endNian+" and YUE="+endYue+" and RI<="+endRi+")) ");
				
				orderWhere=TjInterFace.NIAN+" "+TjInterFace.DESC+","+TjInterFace.YUE+" "+TjInterFace.DESC+","+TjInterFace.RI+" "+TjInterFace.DESC;
			//月数据
			}else if("2".equals(flflag)){
				String startNian=keyMap.get(TjInterFace.SNIAN);
				String startYue=keyMap.get(TjInterFace.SYUE);
				String endNian=keyMap.get(TjInterFace.ENIAN);
				String endYue=keyMap.get(TjInterFace.EYUE);
//				while(true){
//					if(startNian.equals(endNian)){
//						int start=new Integer(startYue);
//						int end=new Integer(endYue);
//						for(int i=start;i<=end;i++){
//							keyWhere.append("("+TjInterFace.NIAN+"="+startNian+" and "+TjInterFace.YUE+"="+i+") or ");
//						}
//						break;
//					}else{
//						int start=new Integer(startYue);
//						int end=12;
//						for(int i=start;i<=end;i++){
//							keyWhere.append("("+TjInterFace.NIAN+"="+startNian+" and "+TjInterFace.YUE+"="+i+") or ");
//						}
//						//直接是下一年
//						startNian=new Integer(new Integer(startNian)+1).toString();
//						//下一年的第一个月
//						startYue="1";
//					}
//				}
				
				keyWhere.append("((NIAN="+startNian+" and YUE>="+startYue+") or (NIAN>"+startNian+" and NIAN<"+endNian+") or (NIAN="+endNian+" and YUE<="+endYue+"))");
				
				orderWhere=TjInterFace.NIAN+" "+TjInterFace.DESC+","+TjInterFace.YUE+" "+TjInterFace.DESC;
			//年数据
			}else if("3".equals(flflag)){
				String startNian=keyMap.get(TjInterFace.SNIAN);
				String endNian=keyMap.get(TjInterFace.ENIAN);
//				int start=new Integer(startNian);
//				int end=new Integer(endNian);
//				for(int i=start;i<=end;i++){
//					keyWhere.append("("+TjInterFace.NIAN+"="+i+") or ");
//				}
				
				keyWhere.append("NIAN>="+startNian+" and  NIAN<="+endNian+"");
				
				orderWhere=TjInterFace.NIAN+" desc";
			}
			
			if(!keyWhere.toString().equals("")){
				//keyWhere.delete(keyWhere.lastIndexOf("or"),keyWhere.length());
				//keyWhere.insert(0, "(");
				//keyWhere.append(")");
				
				String jz=keyMap.get(TjInterFace.JZ);
				if(StringUtils.isNotEmpty(jz)){
					keyWhere.append(" and "+TjInterFace.JZ+"='"+jz+"'");
				}
				
				if(StringUtils.isNotEmpty(orderWhere)){
					keyWhere.append(" order by "+orderWhere);
				}
				
				String sql="select * from DATA_"+kid+" where "+keyWhere.toString();
				
				page=this.pagedQueryTableData(sql, start, limit);
				//System.out.println(sql+"------------");
//				conn=this.dataSource.getConnection();
//				state=conn.createStatement();
//				rs=state.executeQuery(sql);
//				ResultSetMetaData rsmeta=rs.getMetaData();
//				while(rs.next()){
//					Map<String,String> map=new LinkedHashMap<String,String>();
//					for(int i=1;i<=rsmeta.getColumnCount();i++){
//						Object obj=rs.getObject(i);
//						map.put(rsmeta.getColumnName(i).toString(), (obj==null?"":obj.toString()));
//					}
//					result.add(map);
//				}
			}
		}catch(Exception e){
			
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
		return page;
	}
	
	public List<KeyModel> findDayList(String curDate){
		List<KeyModel> dayList=new ArrayList<KeyModel>();
		String lastDateString=DateUtils.getLastDate(curDate);
		Date lastDate=DateUtils.StrToDate(lastDateString, "");
		String lastri=DateUtils.getDate(lastDate,"dd");
		Integer ri=new Integer(lastri);
		for(int i=1;i<=ri;i++){
			KeyModel key=new KeyModel();
			key.setDisplayValue(i+"");
			key.setValue(i+"");
			dayList.add(key);
		}
		return dayList;
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
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		try{
			conn=this.dataSource.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery(countSql);
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
			ResultSetMetaData rsmeta=rs.getMetaData();
			
			while(rs.next()){
				Map map=new HashMap();
				for(int i=1;i<=rsmeta.getColumnCount();i++){
					Object obj=rs.getObject(i);
					//针对oracle timestamp日期单独处理，转换成date
					if(obj instanceof oracle.sql.TIMESTAMP){
						obj=((oracle.sql.TIMESTAMP)obj).dateValue().getTime();
					}
					
					map.put(rsmeta.getColumnName(i), obj);
				}
				list.add(map);
			}
			return new Page(start, count, limit, list);
		}catch(Exception ex){
			throw ex;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(st!=null){
				st.close();
			}
			if(conn!=null){
				conn.close();
			}
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
}
