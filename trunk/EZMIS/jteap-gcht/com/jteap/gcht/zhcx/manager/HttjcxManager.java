package com.jteap.gcht.zhcx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.jteap.core.dao.support.Page;
import com.jteap.core.utils.StringUtil;
import com.jteap.gcht.htsp.model.Ht;
import com.jteap.system.jdbc.manager.JdbcManager;
/**
 * 
 * @author 童贝
 * @date Feb 17, 2011
 */
@SuppressWarnings({"serial", "unchecked"})
public class HttjcxManager extends JdbcManager {
	/**
	 * 根据ID查找对应的表
	 * 作者:童贝
	 * 时间:Feb 17, 2011
	 * @param id
	 * @return
	 */
	public String findTableById(String id){
		String tableName="";
		String[] tableNames=new String[]{Ht.WZHT_TABLE,Ht.RLHT_TABLE,Ht.GCHT_TABLE,Ht.CWHT_TABLE};
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = this.getConnection();
			for(String table:tableNames){
				String sql = "select * from " + table + " a where id='"+id+"'";
				statement = conn.createStatement();
				resultSet = statement.executeQuery(sql);
				if(resultSet.next()){
					return table;
				}
				if(resultSet != null){
					resultSet.close();
				}
				if(statement != null){
					statement.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(statement != null){
					statement.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tableName;
	}
	
	public List<Map<String, String>> findTjcx(String sqlWhere){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = this.getConnection();
			String[] tableNames=new String[]{Ht.WZHT_TABLE,Ht.RLHT_TABLE,Ht.GCHT_TABLE,Ht.CWHT_TABLE};
			for(String tableName:tableNames){
				String sql = "select * from " + tableName + " a where 1=1 ";
				if(sqlWhere != null && !"".equals(sqlWhere)){
					sql += " and " + sqlWhere;
				}
				statement = conn.createStatement();
				resultSet = statement.executeQuery(sql);
				while(resultSet.next()){
					Map map = new HashMap();
					map.put("id", resultSet.getString("id"));
					map.put("htmc", resultSet.getString("HTMC"));
					map.put("htbh", resultSet.getString("HTBH"));
					map.put("htlx", resultSet.getString("HTLX"));
					map.put("fyxz", resultSet.getString("FYXZ"));
					map.put("status", resultSet.getString("STATUS"));
					Object obj=resultSet.getObject("htcjsj");
					//针对oracle timestamp日期单独处理，转换成date
					if(obj instanceof oracle.sql.TIMESTAMP){
						obj=((oracle.sql.TIMESTAMP)obj).dateValue().getTime();
					}
					obj = StringUtil.clobToStringByDB(obj);
					map.put("htcjsj", obj);
					list.add(map);
				}
				
				if(resultSet != null){
					resultSet.close();
				}
				if(statement != null){
					statement.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(resultSet != null){
					resultSet.close();
				}
				if(statement != null){
					statement.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	
	public Page pagedQueryTableData(String sql, int start, int limit) throws Exception {
		
		String countSql = " select count (*) " + removeSelect(removeOrders(sql));
		Connection conn=getConnection();
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
			sql="SELECT * FROM(SELECT A.*, ROWNUM RN FROM ("+sql+") A WHERE ROWNUM <= "+(start+limit-1)+") WHERE RN >= "+start;
			rs=st.executeQuery(sql);
			List list=new ArrayList();
			while(rs.next()){
				Map map = new HashMap();
				map.put("id", rs.getString("id"));
				map.put("htmc", rs.getString("HTMC"));
				map.put("htbh", rs.getString("HTBH"));
				map.put("htlx", rs.getString("HTLX"));
				map.put("fyxz", rs.getString("FYXZ"));
				map.put("status", rs.getString("STATUS"));
				Object obj=rs.getObject("htcjsj");
				//针对oracle timestamp日期单独处理，转换成date
				if(obj instanceof oracle.sql.TIMESTAMP){
					obj=((oracle.sql.TIMESTAMP)obj).dateValue().getTime();
				}
				obj = StringUtil.clobToStringByDB(obj);
				map.put("htcjsj", obj);
				map.put("ID_", rs.getString("ID_"));
				map.put("VERSION_", rs.getString("VERSION_"));
				map.put("START_", rs.getString("START_"));
				map.put("END_", rs.getString("END_"));
				map.put("PROCESSINSTANCE_", rs.getString("PROCESSINSTANCE_"));
				map.put("FLOW_NAME", rs.getString("FLOW_NAME"));
				map.put("FLOW_CONFIG_ID", rs.getString("FLOW_CONFIG_ID"));
				map.put("FLOW_FORM_ID", rs.getString("FLOW_FORM_ID"));
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
}
