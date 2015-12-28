package com.jteap.yx.aqyxfx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.yx.aqyxfx.model.Yyqkfx;

/**
 * 用油情况分析 Manager
 * @author lvchao
 *
 */
@SuppressWarnings({"unchecked","serial"})
public class YyqkfxManager extends HibernateEntityDao<Yyqkfx>{
	private DataSource dataSource;
	
	/**
	 * 查询用油情况分析
	 */
	public Map findYyqxfx(String ksrq,String jsrq){
		Connection conn = null;
		Statement sta = null;
		ResultSet res = null;
		Map<String, Double> map = new HashMap<String, Double>();
		String sql =  "select t.jzbh,t.xz,count(t.id) as cs,sum(t.yyzm-t.yyqm) as yyl from tb_form_sjbzb_fxb t " +
					  " where to_char(t.txsj,'yyyy-mm-dd') between '"+ksrq+"' and '"+jsrq+"' group by t.jzbh,t.xz";
		
		//System.out.println(sql);
		try {//to_char(sysdate,'yyyy-mm')||
			conn = dataSource.getConnection();
			sta = conn.createStatement();
			res = sta.executeQuery(sql);
			while(res.next()){
				String xz = res.getString("xz");
				String jz = res.getString("jzbh");
				double yyl = res.getDouble("yyl");
				int cs = res.getInt("cs");
				this.setValue(xz, jz, yyl, cs, map);
				//this.setMapValue(xz, jz, yyl, cs, map);
			}
		} catch (SQLException e) {
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
		return map;
	}
	/**
	 *  
	 * 根据开始时间结束时间 查询是否存在用油情况分析
	 * @param dctjq
	 * @return
	 */
	public boolean checkYyqkfx(String ksrq,String jsrq) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		try{
			conn = dataSource.getConnection();
			StringBuffer sqlBf = new StringBuffer("select t.id  from TB_FORM_SJBZB_YYFXB t where t.ksrq = '"+ksrq+"' and t.jsrq = '"+jsrq+"'");
			st = conn.createStatement();
			res = st.executeQuery(sqlBf.toString());
			return res.next();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
		return false;
	}
//	public void saveYyqxfx(){
//		Connection conn = null;
//		Statement st = null;
//		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
//		StringBuffer sqlSb  = new StringBuffer("select t.id from tb_sys_role t where t.role_sn='sczg'");
//		ZhiBanJiLuManager zbjlManager = (ZhiBanJiLuManager)SpringContextUtil.getBean("zhiBanJiLuManager");
//		String roleIds = zbjlManager.findRolIds(sqlSb.toString());
//		PersonManager personManager = (PersonManager)SpringContextUtil.getBean("personManager");
//		List<Person> personList = (List) personManager.findPersonByRoleIds(roleIds);
//		try{
//			conn = dataSource.getConnection();
//			StringBuffer sbsql = new StringBuffer();
//			sbsql.append("insert into TB_FORM_SJBZB_YYFX t(id,");
//			sbsql.append("tjr,DCTJQ,TJSJ) ");
//			String id = UUIDGenerator.hibernateUUID();
//			sbsql.append("values('");
//			sbsql.append(id);
//			sbsql.append("',");
//			sbsql.append("'");
//			if(personList.size()>0){
//				sbsql.append(personList.get(0).getUserName());
//			}else{
//				sbsql.append("系统管理员");
//			}
//			sbsql.append("',");
//			sbsql.append("to_char(add_months(trunc(sysdate),-1),'yyyy-mm')");
//			sbsql.append(",sysdate");		 
//			sbsql.append(")");
//			st = conn.createStatement();
//			st.execute(sbsql.toString());
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally{
//			if(conn!=null){
//				try {
//					conn.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	//删除用油情况分析
//	public void delYyqkfx(String ids) throws SQLException {
//		Connection conn = null;
//		Statement st = null;
//		String sql = "DELETE tb_form_sjbzb_yyfx t  WHERE t.id IN ("+ids+")";
//		try {
//			conn = dataSource.getConnection();
//			st = conn.createStatement();
//			st.execute(sql);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			if(conn!=null){
//				conn.close();
//			}
//		}
//		
//		
//	}
	//要替换的字符串
	public String[] stringProperties(){
		return new String[]{"冷态","温态","热态","掉峰","备用","检修","停运","非停","降出力"};
	}
	//要替换成的编号
	public String[] bhProperties(){
		return new String[]{"LT","WT","RT","DF","BY","JX","TY","FT","JCL"};
	}
	//将指定的字符转换成对应的编号
	public void setValue(String xz,String jz,double yyl,int cs,Map<String,Double> map){
		//获得性质编号
		String xmmc = getXzBh(xz);
		//合计性质编号
		String hjmc = xmmc;
		String flag = null;
		if(jz.indexOf("1")!=-1){
			xmmc = xmmc+"_1";
			flag = "yq";
		}else if(jz.indexOf("2")!=-1){
			xmmc = xmmc+"_2";
			flag = "yq";
		}else if(jz.indexOf("3")!=-1){
			xmmc = xmmc+"_3";
			flag = "eq";
		}else if(jz.indexOf("4")!=-1){
			xmmc = xmmc+"_4";
			flag = "eq";
		}
		if(map.get(hjmc+"_YQ_CS")==null){
			map.put(hjmc+"_YQ_CS",new Double(0));
		}
		if(map.get(hjmc+"_YQ_YYL")==null){
			map.put(hjmc+"_YQ_YYL",new Double(0));
		}
		if(map.get(hjmc+"_EQ_CS")==null){
			map.put(hjmc+"_EQ_CS",new Double(0));
		}
		if(map.get(hjmc+"_EQ_YYL")==null){
			map.put(hjmc+"_EQ_YYL",new Double(0));
		}
		
		//添加合计信息
		if("yq".equals(flag)){
			map.put(hjmc+"_YQ_CS",map.get(hjmc+"_YQ_CS")+cs);
			map.put(hjmc+"_YQ_YYL",map.get(hjmc+"_YQ_YYL")+yyl);
		}
		if("eq".equals(flag)){
			map.put(hjmc+"_EQ_CS",map.get(hjmc+"_EQ_CS")+cs);
			map.put(hjmc+"_EQ_YYL",map.get(hjmc+"_EQ_YYL")+yyl);
		}
		map.put(hjmc+"_QC_CS",map.get(hjmc+"_EQ_CS")+map.get(hjmc+"_YQ_CS"));
		map.put(hjmc+"_QC_YYL",map.get(hjmc+"_EQ_YYL")+map.get(hjmc+"_YQ_YYL"));
		
		map.put(xmmc+"_CS",new Double(cs));
		map.put(xmmc+"_YYL",yyl);
	}
	/**
	 * 根据性质名称返回性质编号
	 * @param xz
	 * @return
	 */
	public String getXzBh(String xz){
		for(int i = 0;i<this.stringProperties().length;i++){
			 if(xz.indexOf(this.stringProperties()[i])!=-1){
				 return  this.bhProperties()[i];
			 }
		}
		return null;
	}
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}
