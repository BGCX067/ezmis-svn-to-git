package com.jteap.yx.aqyxfx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.yx.zbjl.manager.ZhiBanJiLuManager;


/**
 * 被考核电量Manager
 * @author lvhao
 *
 */
@SuppressWarnings({"unchecked","serial"})
public class BkhdlTjManager extends HibernateEntityDao{
	private DataSource dataSource;
	private ZhiBanJiLuManager zbjlManager ;
	private PersonManager personManager;
	
	/**
	 * 保存被考核电量月统计
	 */
	public Map findBkhdl(String ksrq,String jsrq){
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		Map<String,String> countMap = new HashMap();
		try{
			conn = dataSource.getConnection();
			StringBuffer sqlBf = new StringBuffer("select t.zbzb,t.yy,sum(t.bkhfdl) as khdl from tb_form_sjbzb_bkhdltj t");
			sqlBf.append(" where to_char(t.CBSJ,'yyyy-mm-dd') between '"+ksrq+"' and '"+jsrq+"' group by t.zbzb,t.yy");
			st = conn.createStatement();
			res = st.executeQuery(sqlBf.toString());
			while(res.next()){
				this.setZbAndYy(res.getString("zbzb"), res.getString("yy"),countMap,res.getDouble("khdl"));
			}
			//设置原因百分比
			this.setYybfb(countMap);
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
		return countMap;
	}
	//设置原因统计合计
	private void setYybfb(Map countMap) {
		// TODO Auto-generated method stub
		for(int i =0;i<this.yymc().length;i++){
			Double per = new Double(0);
			if(countMap.get(this.yybh()[i]+"6")!=null){
				per = (Double)countMap.get(this.yybh()[i]+"6") /(Double)countMap.get("TJ_6") * 100;
				per = (double)Math.round(per*100)/100;
				String strPer = per.toString() + "%";
				countMap.put(this.yybh()[i]+"6",strPer );
			}
		}
		
	}
	/**
	 * 废弃方法
	 * 根据统计期 检查数据库中是否已经存在
	 * @param dctjq
	 */
	public boolean checkBkhdlTj(String ksrq,String jsrq) {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		try{
			conn = dataSource.getConnection();
			StringBuffer sqlBf = new StringBuffer("select t.id  from tb_form_sjbzb_bkhdlytj t where t.ksrq = '"+ksrq+"' and t.jsrq = '"+jsrq+"'");
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
	//设置原因名称
	private String[] yymc(){
		return new String[]{"锅炉","汽机","电气","热工","继电","通讯","煤质","操作","其他"};
	}
	//原因名称对应的原因编号
	private String[] yybh(){
		return new String[]{"GL_","QJ_","DQ_","RG_","JD_","TX_","MZ_","CZ_","QT_"};
	}
	/**
	 * 设置值别编号与原因编号
	 * @param zb
	 * @param yy
	 * @param sb
	 */
	private void setZbAndYy(String zb,String yy,Map countMap,double khdl){
		int zbbh=0;
		String yybh = "";
		if(zb.indexOf("一")!=-1){
			zbbh = 1;
		}
		if(zb.indexOf("二")!=-1){
			zbbh = 2;
		}
		if(zb.indexOf("三")!=-1){
			zbbh = 3;
		}
		if(zb.indexOf("四")!=-1){
			zbbh = 4;
		}
		if(zb.indexOf("五")!=-1){
			zbbh = 5;
		}
		for(int i = 0;i<this.yymc().length;i++){
			if(yy.indexOf(this.yymc()[i])!=-1){
				yybh = this.yybh()[i];
				break;
			}
		}
		countMap.put(yybh+zbbh, khdl);
		setCountMap(countMap,khdl,zbbh+"",yybh);
	}
	/**
	 * 给合计MAP里放入值
	 * @param countMap 合计Map
	 * @param khdl	考核电量
	 * @param bh  编号
	 * @param yy  考核原因
	 */
	public void setCountMap(Map<String,Double> countMap,double khdl,String bh,String yy){
		//分值合计
		if(countMap.get("TJ_"+bh)==null){
			countMap.put("TJ_"+bh,khdl);
		}else{
			countMap.put("TJ_"+bh,countMap.get("TJ_"+bh)+khdl);
		}
		//总合计
		if(countMap.get("TJ_6")==null){
			countMap.put("TJ_6",new Double(khdl));
		}else{
			countMap.put("TJ_6",new Double(countMap.get("TJ_6").doubleValue()+khdl));
		}
		//原因合计
		if(countMap.get(yy+"6")==null){
			countMap.put(yy+"6",new Double(khdl));
		}else{
			countMap.put(yy+"6",new Double(countMap.get(yy+"6").doubleValue()+khdl));
		}
	}
	
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ZhiBanJiLuManager getZbjlManager() {
		return zbjlManager;
	}

	public void setZbjlManager(ZhiBanJiLuManager zbjlManager) {
		this.zbjlManager = zbjlManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
	
	
}
