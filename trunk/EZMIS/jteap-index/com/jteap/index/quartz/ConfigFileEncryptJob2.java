package com.jteap.index.quartz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.index.manager.NowInfoManager;
import com.jteap.yx.fdzbfxykh.manager.DBHelper;

/**
 * 定期设置各机组连续运行、停运天数
 * @author caihuiwen
 */
public class ConfigFileEncryptJob2 {
	private Log log=LogFactory.getLog(ConfigFileEncryptJob2.class);
	
	ConfigFileEncryptJob2(){
	}
	
	/**
	 * 设置各机组连续运行、停运天数
	 */
	public void setJzRunDay(){
		NowInfoManager nowInfoManager = (NowInfoManager)SpringContextUtil.getBean("nowInfoManager");
		if(nowInfoManager != null){
			nowInfoManager.setJzRunDay();
			log.info("设置各机组连续运行、停运天数");
		}
	}
	
	/**
	 * 保存1、2机组负荷
	 */
	public void saveJzFh(){
		NowInfoManager nowInfoManager = (NowInfoManager)SpringContextUtil.getBean("nowInfoManager");
		//服务启动时会执行该方法，如果nowInfoManager未被spring实例化会抛出异常
		if(nowInfoManager != null){
			Connection conn = null;
			try {
				
				String serverName = SystemConfig.getProperty("ED_SERVER12_ADDRESS");
				String dbName = SystemConfig.getProperty("ED_SERVER12_DBNAME");
				String userName = SystemConfig.getProperty("ED_SERVER12_USERNAME");
				String userPwd = SystemConfig.getProperty("ED_SERVER12_USERPWD");
				conn = DBHelper.getConn(serverName, dbName, userName, userPwd);
				//1机组
				Map<String, String> jz1Map = nowInfoManager.findFh(conn,"ssdata.dbo.qs_101");
				//2机组
				Map<String, String> jz2Map = nowInfoManager.findFh(conn,"ssdata.dbo.qs_201");
				
				nowInfoManager.saveJzFh("sisdata.ls_101", jz1Map);
				nowInfoManager.saveJzFh("sisdata.ls_201", jz2Map);
			} catch (Exception e) {
				try {
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			log.info("保存1、2机组负荷");
		}
	}
	/**
	 * 保存全厂机组负荷 并算出总负荷
	 */
	public void saveAllJzFh(){
		Connection conn = null;
		Connection sqlseverConn= null;
		Statement st = null;
		Statement sqlseverSt= null;
		ResultSet res = null;
		ResultSet sqlseverRs= null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		//sqlserver数据库 连接池
		DataSource sqlserverSource = (DataSource) SpringContextUtil.getBean("sqldataSource");
		try{
			conn = dataSource.getConnection();
			sqlseverConn =sqlserverSource.getConnection();
			//新增添加机组负荷到SISDATA.SS_EZDC
			st = conn.createStatement();
			sqlseverSt = sqlseverConn.createStatement();
			StringBuffer updateBf = new StringBuffer();
			//查询所有数据结果集
			String selSql = "SELECT SYSDATE AS TIMES,L1.C1 AS JZ1,L2.C1 AS JZ2,L3.C1 AS JZ3," +
			"L4.C1 AS JZ4,L1.C1+L2.C1+L3.C1+L4.C1 AS JZ, " +
			"JZ1.ZT1 as ZT1, JZ2.ZT2 AS ZT2,JZ3.ZT3 AS ZT3 ,JZ4.ZT4 AS ZT4 " +
			"FROM (SELECT C1 FROM SISDATA.LS_101 ORDER BY TIMES DESC) L1," +
			"(SELECT C1 FROM SISDATA.LS_201 ORDER BY TIMES DESC) L2, " +
			"SISDATA.SS_301 L3,SISDATA.SS_401 L4," +
			"(SELECT '#1'||STATUS||RUN_DAY||'天' as ZT1 FROM TB_SYS_NOWINFO WHERE JIZU=1) JZ1," +
			"(SELECT '#2'||STATUS||RUN_DAY||'天' as ZT2 FROM TB_SYS_NOWINFO WHERE JIZU=2) JZ2," +
			"(SELECT '#3'||STATUS||RUN_DAY||'天' as ZT3 FROM TB_SYS_NOWINFO WHERE JIZU=3) JZ3," +
			"(SELECT '#4'||STATUS||RUN_DAY||'天' as ZT4 FROM TB_SYS_NOWINFO WHERE JIZU=4) JZ4" +
			" WHERE ROWNUM <= 1";
			res = st.executeQuery(selSql);
			//存放结果集的数组
			String[] values = new String[9];
			//将结果集每个字段值放入数组中
			while(res.next()){
				for(int i=2;i<=10;i++){
					values[i-2] = res.getString(i);
				}
			}
			//System.out.println(values[1]);
			sqlseverRs = sqlseverSt.executeQuery("select * from SS_EZDC");
			if(sqlseverRs.next()){
				//System.out.println("修改数据");
				updateBf.append("UPDATE SS_EZDC SET ");
				updateBf.append("TIMES =(SELECT getdate()), ");
				updateBf.append("JZ1 = "+values[0]+",");
				updateBf.append("JZ2 = "+values[1]+",");
				updateBf.append("JZ3 = "+values[2]+", ");
				updateBf.append("JZ4 = "+values[3]+", ");
				updateBf.append("JZ = "+values[4]+", ");
				updateBf.append("ZT1 = '"+values[5]+"', ");
				updateBf.append("ZT2 = '"+values[6]+"',");
				updateBf.append("ZT3 = '"+values[7]+"', ");
				updateBf.append("ZT4 = '"+values[8]+"' ");
			}else{
				//System.out.println("新增数据");
				updateBf.append("insert into SS_EZDC  VALUES(");
				updateBf.append("getdate(), ");
				updateBf.append(values[0]+",");
				updateBf.append(values[1]+",");
				updateBf.append(values[2]+", ");
				updateBf.append(values[3]+", ");
				updateBf.append(values[4]+", ");
				updateBf.append("'"+values[5]+"', ");
				updateBf.append("'"+values[6]+"',");
				updateBf.append("'"+values[7]+"', ");
				updateBf.append("'"+values[8]+"')");
			}
			sqlseverSt.execute(updateBf.toString());
//			System.out.println("数据添加完毕");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				if(sqlseverSt!=null){
					sqlseverSt.close();
				}
				if(conn != null){
					conn.close();
				}
				if(sqlseverConn!=null){
					sqlseverConn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 描述 : 获取SQLServer的连接(sbwz) 作者 : caofei 时间 : Dec 23, 2010 参数 : null 返回值 :
	 * Connection 异常 : Exception
	 */
	public final static Connection getSqlServerWzConnection() {
		 Connection sqlserverConn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			// System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:sqlserver://10.229.41.7:1433; databasename=sisdata";
			String user = "sa";
			String password = "manager2329";
			sqlserverConn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			 System.out.println("数据库连接失败");
		}
		return sqlserverConn;
	}
	public static void main(String agrs[]) throws SQLException{
		Connection conn =getSqlServerWzConnection();
		try {
			PreparedStatement st = conn.prepareStatement("select * from SS_EZDC order by times desc");
			ResultSet rs =st.executeQuery();
			while (rs.next()){
				System.out.println(rs.getObject(1)+","+rs.getObject(2)+","+rs.getObject(3)
						+","+rs.getObject(4)+","+rs.getObject(5)+","+rs.getObject(6)+","
						+rs.getObject(7)+","+rs.getObject(8)+","+rs.getObject(9)+","+rs.getObject(10));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			conn.close();
		}
	}
}
