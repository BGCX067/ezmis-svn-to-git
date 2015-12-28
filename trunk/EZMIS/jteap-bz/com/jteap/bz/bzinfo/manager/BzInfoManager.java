package com.jteap.bz.bzinfo.manager;

import java.util.Calendar;

import org.springframework.jdbc.core.JdbcTemplate;
/**
 * 班组处理类
 * @author lvchao
 *
 */
public class BzInfoManager extends JdbcTemplate{
	/**
	 * 根据传入 表名 去查询当前表中 最大的日期
	 * @param tName
	 * @return
	 */
	public String getMaxDate(String hql,String tName){
		
//		String date="";
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int moth = ca.get(Calendar.MONTH)+1;
		String mothStr = "";
		if(moth<10){
			mothStr="0"+moth;
		}else{
			mothStr = moth+"";
		}
//		Connection  conn = null;
//		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try{
//			conn = dataSource.getConnection();
//			Statement st = conn.createStatement();
			if(tName.equals("TB_FORM_BZRZ")){
				if(hql.indexOf("WHERE")==-1)
					hql +=" WHERE ";
				else
					hql +=" AND ";
//				String sql = "select max(to_char(sj,'yyyy-mm')) from TB_FORM_BZRZ";
//				ResultSet rs = st.executeQuery(sql);
//				while(rs.next()){
					return hql+"  to_char(sj,'yyyy-mm') = '"+year+"-"+mothStr+"'";
//				}
			}
			if(tName.equals("TB_FORM_AQXXJL")){
				if(hql.indexOf("WHERE")==-1)
					hql +=" WHERE ";
				else
					hql +=" AND ";
//				String sql = "select max(to_char(RQ,'yyyy-mm')) from TB_FORM_AQXXJL";
//				ResultSet rs = st.executeQuery(sql);
//				while(rs.next()){
					return hql+" to_char(RQ,'yyyy-mm') = '"+year+"-"+mothStr+"'";
//				}
			}
			if(tName.equals("TB_FORM_SGYXJL")){
				if(hql.indexOf("WHERE")==-1){
					hql +=" WHERE ";
				}else{
					hql +=" AND ";
				}
				return hql+"  RQ like '%"+year+"-"+mothStr+"%'";
			}
			if(tName.equals("TB_FORM_BWHJL")){
				if(hql.indexOf("WHERE")==-1){
					hql +=" WHERE ";
				}else{
					hql +=" AND ";
				}
				return hql+"  to_char(SJ,'yyyy-mm') = '"+year+"-"+mothStr+"'";
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
//			if(conn!=null){
//				try {
//					conn.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
		}
		
		return hql;
	}
}
