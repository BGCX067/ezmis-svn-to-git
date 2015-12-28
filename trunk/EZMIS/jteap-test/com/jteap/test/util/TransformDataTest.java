package com.jteap.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.input.DOMBuilder;

import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.wz.gcxmgl.model.Proj;

/**
 * 从SQLServer数据库将wz_baseinfo、wz_category、wz_warehouse、wz_location四张表的数据
 * 通过相关处理后分别对应导入到Oracle数据库中的TB_WZ_SWZDA、TB_WZ_SWZLB、TB_WZ_CKGL、TB_WZ_SKWGL
 * 
 * @author caofei
 * 
 */
@SuppressWarnings("unused")
public class TransformDataTest {

	private static final String wzbh = "100000000000000000000000";
	private static final String wzlbbh = "2000000000000000000000000000";
	private static final String kwbh = "300000000000000000000000";
	private static final String ckbh = "400000000000000000000000000000";
	private static final String yhdbh = "500000000000000000000000";
	private static final String gysbh = "60000000000000000000000000";
	private static final String projectbh = "700000000000000000000000";
	private static final String gclbbh = "800000000000000000000000000000";
	private static final String lydbh = "900000000000000000000000";
	private static final String cgjhbh = "333333333333333333333333";
	private Connection sqlserverWzConn = null; 
	private Connection sqlserverRsConn = null;
	private Connection oracleConn = null;
	private Connection myOracleConn = null;

	/**
	 * 描述 : 获取SQLServer的连接(sbwz) 作者 : caofei 时间 : Dec 23, 2010 参数 : null 返回值 :
	 * Connection 异常 : Exception
	 */
	public final Connection getSqlServerWzConnection() throws Exception {
		// Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			// System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:sqlserver://10.229.41.7:1433; databasename=EZMIS_sbwz";
			String user = "sa";
			String password = "manager2329";
			sqlserverWzConn = DriverManager.getConnection(url, user, password);
			// System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("数据库连接失败");
		}
		return sqlserverWzConn;
	}
	
	/**
	 * 描述 : 获取SQLServer的连接(rs) 作者 : caofei 时间 : Dec 23, 2010 参数 : null 返回值 :
	 * Connection 异常 : Exception
	 */
	public final Connection getSqlServerRsConnection() throws Exception {
		// Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			// System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:sqlserver://10.229.41.7:1433;databasename=EZMIS_rs";
			String user = "sa";
			String password = "manager2329";
			sqlserverRsConn = DriverManager.getConnection(url, user, password);
			// System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("数据库连接失败");
		}
		return sqlserverRsConn;
	}

	/**
	 * 描述 : 获取Oracle的连接 作者 : caofei 时间 : Dec 23, 2010 参数 : null 返回值 : Connection
	 * 异常 : Exception
	 */
	public final Connection getOracleConnection() throws Exception {
		// Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			// System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:oracle:thin:@10.229.41.66:1521:ORCL";
			String user = "ezmis";
			String password = "ezmis123";
			oracleConn = DriverManager.getConnection(url, user, password);
			// System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("数据库连接失败");
		}
		return oracleConn;
	}
	/**
	 * 本地oracle数据库连接
	 * @return
	 * @throws Exception
	 */
	public final Connection getMyOracleConnection() throws Exception {
		// Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			// System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
			String user = "ezmis";
			String password = "ezmis";
			oracleConn = DriverManager.getConnection(url, user, password);
			// System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("数据库连接失败");
		}
		return oracleConn;
	}
	/**
	 * 返回68的数据库连接
	 * @return
	 * @throws Exception
	 */
	public final Connection getOracle68Connection() throws Exception {
		// Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			// System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:oracle:thin:@10.41.229.66:1521:ORCL";
			String user = "ezmis";
			String password = "ezmis123";
			oracleConn = DriverManager.getConnection(url, user, password);
			// System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("数据库连接失败");
		}
		return oracleConn;
	}
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_warehouse数据,再导入到Oracle数据库中的TB_WZ_CKGL表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void tansformCkglData() throws Exception {
		String warehousecode = ""; // 仓库编码
		String warehousename = ""; // 仓库名称
		String memo = ""; // 备注
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_warehouse where warehousename = '电料库' order by warehousecode";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				warehousecode = ckbh + rs.getString("warehousecode");
				warehousename = rs.getString("warehousename");
				memo = rs.getString("memo");
				if (memo == null) {
					memo = "";
				}
				String oracleSql = "insert into TB_WZ_CKGL(ID,CKMC,CKGLY,BZ) values ('"
						+ warehousecode
						+ "','"
						+ warehousename
						+ "','0157','"
						+ memo + "') ";
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	/**
	 * 初始化 电料库 库位
	 */
	public void initDlk(){
		try{
			String warehousecode = ""; // 仓库编码
//			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			Statement st4 = oracleConn.createStatement();
			//仓库数据 
			String[] ck = new String[]{"备品库","电料库","仪器仪表库","工具库","钢材库","加工件库","五金库","综合库"};
			//每个仓库总计划金额 数组 对应仓库数组 
			Double[] zje = new Double[]{11665783.0,1718139.0,5469754.0,1694072.9,76120.0,23550.0,416800.24,634534.0,390915.25,287001.254};
			//每个仓库差额数据 对应仓库数组 
			Double[] ce = new Double[]{-474917.604,-73165.756,-220188.35,721860.433,32448.407,10038.886,177673.462,281380.38,166650.514,122364.108};
			//每个库的总数
			Integer[] count = new Integer[]{905,72,480,348,19,29,49,121,35,282};
			
			//			oracleConn = getOracleConnection();
			int aa = 0;
			//查询老物资 系统中 仓库为电料库的仓库信息
			for(int i=0;i<ck.length;i++){
				aa++;
				//每个仓库的总数
				int ckCount = 0;
				//差额价格
				double cejg =0.0;
				//比例
				double bl = 0.0;
				//仓库总差额
				double ckSum = 0.0;
				System.out.println("<----开始导入"+ck[i]+"---->");
//				System.out.println("仓库计划价格："+zje[i]);
//				System.out.println("仓库差额："+ce[i]);
				String sql2 = "select * from tb_wz_swzdatest where kwbm in (select id from tb_wz_skwgl where ckid in (select id from tb_wz_ckgl where ckmc = '"+ck[i]+"')) and jhdj>0 and dqkc>0 order by dqkc desc";
				Statement st2 = oracleConn.createStatement();
				ResultSet rs2 = st2.executeQuery(sql2);
				while(rs2.next()){
					String kwbm = rs2.getString("kwbm");
//					ckCount++;
//					double stock_total = rs2.getDouble("dqkc");
//					double  jhdj = rs2.getDouble("jhdj");
//					//计划金额
//					double sum =stock_total*jhdj;
//					//平摊后的单价 计算规则
//					//单项物资的计划金额/该仓库计划总金额 = 该物资金额 占该库金额的比例
//					//格式化每个物资金额占总金额比例 保留两位小时
//					BigDecimal sumBd = new BigDecimal(sum);
//					BigDecimal jeBd = new BigDecimal(zje[i].doubleValue());
//					double dj1 = sumBd.divide(jeBd,6,BigDecimal.ROUND_HALF_UP).doubleValue();
//					
//					//格式化目前为止 占总金额比例 保留两位小数
//					BigDecimal blBd = new BigDecimal(bl);
//					BigDecimal one = new BigDecimal(1);
//					bl = blBd.divide(one,6,BigDecimal.ROUND_HALF_UP).doubleValue();
//					//总比例如果大于1 就用物资比例-去多余的比例
////								System.out.println(bl+dj1);
//					if(bl+dj1>1){
//						dj1 = dj1-(bl+dj1-1);
//					}
//					if(bl==1){
//						System.out.println("===");
//						break;
//					}
//					if(bl+dj1<1){
//						if(ckCount==count[i]){
//							dj1 = dj1+(1-bl-dj1);
//						}
//					}
//					bl+=dj1;
//					//占该库金额的比例*单仓库差额=该项物资按比例生成的差额
//					double dj2 = dj1*ce[i];
//					cejg+=dj2;
//					//单项物资差额/当前库存=单价差额
//					double dj3 =0;
//					BigDecimal dj2Big = new BigDecimal(dj2);
//					BigDecimal slBig = new BigDecimal(stock_total);
//					if(stock_total>0){
//						dj3=  dj2Big.divide(slBig,3,BigDecimal.ROUND_HALF_UP).doubleValue();
//					}
//					ckSum+=dj3*stock_total;
//					//单价差额+计划单价 = 修改后的单价
////					double testjhdj = jhdj+dj3;
//					if(bl==1){
//						ckSum =ckSum-dj3*stock_total;
//						System.out.println("数量："+stock_total+"单价:"+dj3+"金额:"+ckSum);
//						dj2Big = new BigDecimal(ce[i]-ckSum);
//						dj3=  dj2Big.divide(slBig,3,BigDecimal.ROUND_HALF_UP).doubleValue();
//						ckSum+=dj3*stock_total;
//						System.out.println("数量："+stock_total+"单价:"+dj3+"金额:"+ckSum);
//					}
					String updatesql  = "update tb_wz_swzda set kwbm ='"+kwbm+"'  where id = '"+rs2.getString("id")+"'";
					st4.execute(updatesql);
				}
//				st2.close();
//				System.out.println(ck[i]+"实际差额:"+ckSum);
//				System.out.println(ck[i]+"差额价格:"+cejg);
//				System.out.println(ck[i]+":"+bl);
			}
			//System.out.println("执行完毕");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	//初始化 需求计划明细中序号
	public void initXh() throws SQLException{
		Statement st = null;
		Statement st1 = null;
		Statement st2 = null;
		try{
			myOracleConn = this.getOracleConnection();
			 st = myOracleConn.createStatement();
			 st1 = myOracleConn.createStatement();
			 st2 =myOracleConn.createStatement();
			ResultSet rs = null;
			ResultSet rs1 = null;
			
			String sql = "select id from tb_wz_xqjh";
			String sql1 = "";
			int xh =1;
			int count =0;
			int counts =0;
			rs = st.executeQuery(sql);
			//遍历 所有需求计划
			while(rs.next()){
				xh =1;
				count++;
				sql1 = "select id from tb_wz_xqjh_detail where xqjhbh = '"+rs.getString(1)+"'";
//				System.out.println("sql1:"+sql1);
				rs1 = st1.executeQuery(sql1);
				//根据需求计划id 遍历明细 修改序号
				while(rs1.next()){
//					System.out.println("update:"+"update tb_wz_xqjh_detail set xh = '"+xh+"' where id = '"+rs1.getString(1)+"'");
					st2.execute("update tb_wz_xqjh_detail set xh = '"+xh+"' where id = '"+rs1.getString(1)+"'");
					xh++;
					counts++;
				}
			}
			System.out.println(count);
			System.out.println(counts);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(st!=null){
				st.close();
			}
			if(st1!=null){
				st1.close();
			}
		}
	}
	//初始化需求计划明细的物资编码
	public void initWzbm(){
		
		try {
			try {
				oracleConn = this.getOracleConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Statement  st1 = oracleConn.createStatement();
			Statement st2 = oracleConn.createStatement();
			Statement st3 = oracleConn.createStatement();
			Statement st4 = oracleConn.createStatement();
			//查询 领用申请里的需求计划明细编码 对应的物资编码
			String sql = "select l.wzbm,l.xqjhmxbm from tb_wz_ylysqmx l where l.xqjhmxbm is not null";
			ResultSet rs1 = st1.executeQuery(sql);
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			while(rs1.next()){
				String sql1 = "update tb_wz_xqjh_detail t set t.wzbm ='"+rs1.getString(1)+"'  where t.id = '"+rs1.getString(2)+"'";
				System.out.println(sql1);
				st2.execute(sql1);
			}
			//初始化 已经根据需求计划明细 进行采购了的 物资编码
			String sql2 = "update tb_wz_xqjh_detail t set t.wzbm=(select d.wzbm from tb_wz_ycgjhmx d where d.xqjhmx = t.id) where t.wzbm is null ";
			st3.execute(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
public void initWzbm2(){
		
		try {
			try {
				oracleConn = this.getOracleConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Statement  st1 = oracleConn.createStatement();
			Statement st2 = oracleConn.createStatement();
			Statement st3 = oracleConn.createStatement();
			Statement st4 = oracleConn.createStatement();
			//查询 物资编码为空的 需求计划编号
			String sql = "select t.xqjhbh  from tb_wz_xqjh_detail t where t.wzbm is null group by t.xqjhbh";
			ResultSet rs1 = st1.executeQuery(sql);
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			while(rs1.next()){
				//根据需求计划编号 取得需求计划申请编号 通过申请编号 取得 申请明细
				String sql2 = "select * from tb_wz_xqjhsq_detail t where t.xqjhsqid =  " +
						" (select d.xqjhsqbh    from tb_wz_xqjh d  where d.id = '"+rs1.getString(1)+"')";
				rs2 = st2.executeQuery(sql2);
				//获取物资编码
				String wzbm="";
				while(rs2.next()){
					if(rs1.getString(1).equals("8a65808d2fa01db2012fb4d26cda624e")||rs1.getString(1).equals("8a65808d2fa01db2012fb392f6760443")){
						System.out.println( "物资编码："+rs2.getString("wzbm"));
					}
					wzbm = rs2.getString("wzbm");
					//根据需求计划申请中的 申请数量 计量单位 去比对 需求计划明细中的 批准数量 以及计量单位
					String sql3 = "select id  from tb_wz_xqjh_detail where xqjhbh " +
							"in(select id from tb_wz_xqjh where xqjhsqbh = '"+rs2.getString("xqjhsqid")+"') and jldw ='"+rs2.getString("jldw")+"' and pzsl = "+rs2.getDouble("sqsl")+" and lysl=0 and dhsl = 0 and wzbm is null";
					if(rs1.getString(1).equals("8a65808d2fa01db2012fb4d26cda624e")||rs1.getString(1).equals("8a65808d2fa01db2012fb392f6760443")){
						System.out.println(sql3);
					}
					rs3 = st3.executeQuery(sql3);
					//如果有 且只有一条 则比对成功 修改对应的需求计划wzbm
					if(rs3.next()){
						
//						String sql4 = "update tb_wz_xqjh_detail set wzbm ='"+wzbm+"' where id='"+rs3.getString(1)+"' and pzsl = lysl";
						String sql5 = "update tb_wz_xqjh_detail set wzbm ='"+wzbm+"' where id='"+rs3.getString(1)+"'";
						if(rs1.getString(1).equals("8a65808d2fa01db2012fb4d26cda624e")||rs1.getString(1).equals("8a65808d2fa01db2012fb392f6760443")){
//							System.out.println(sql4);
							System.out.println(sql5);
						}
//						st4.execute(sql4);
						st4.execute(sql5);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
//	public void showDlk() throws Exception{
//		oracleConn = getOracleConnection();
//		String sql = "select a.id,b.cwmc from tb_wz_swzda a,tb_wz_skwgl b "+
//		  " where a.kwbm = b.id and   "+
//		          "b. ckid = (select id from tb_wz_ckgl where ckmc = '电料库') order by a.id";
//		Statement st1 = oracleConn.createStatement();
//		ResultSet rs1 = st1.executeQuery(sql);
//		   BufferedReader in2 = new BufferedReader(new StringReader(s2));
//           PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("IODemo.out")));
//           int lineCount = 1;
//           while((s = in2.readLine()) != null )
//                      out1.println(lineCount++ + ": " + s);
//           out1.close();
//		while(rs1.next()){
//			
//			System.out.println(rs1.getString(1)+":"+rs1.getString(2));
//		}
//	}
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_location数据,再导入到Oracle数据库中的TB_WZ_SKWGL表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformKwglData(String kwid) throws Exception {
		String locationcode = ""; // 库位编码
		String warehousecode = ""; // 仓库编码
		String mnemonic = ""; // 位置助记码
		String locationname = ""; // 仓位名称
		String memo = ""; // 备注
		String levelcode = ""; // 父级节点
		String wz__locationcode = "";
		String sjid = "";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_location where locationcode='"+kwid+"' order by locationcode";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				locationcode = kwbh + rs.getString("locationcode");
				warehousecode = ckbh + rs.getString("warehousecode");
				mnemonic = rs.getString("mnemonic");
				wz__locationcode = rs.getString("wz__locationcode");
				locationname = rs.getString("locationname");
				levelcode = rs.getString("levelcode");
				if (levelcode == null || levelcode.equals("")) {
//					sjid = getParentByLevelCodeForWzda("");
				} else {
					if (levelcode.split("#").length == 2) {
						sjid = "";
					} else {
//						sjid = getParentByLevelCodeForWzda(levelcode.split("#")[levelcode
//								.split("#").length - 2]);
						sjid = kwbh + levelcode.split("#")[levelcode.split("#").length - 2];
					}
				}
				memo = rs.getString("memo");
				if (memo == null) {
					memo = "";
				}
				if (mnemonic == null) {
					mnemonic = "";
				}
				String oracleSql = "insert into TB_WZ_SKWGL(ID,CKID,WZZJM,CWMC,BZ,SJID) values ('"
						+ locationcode
						+ "','"
						+ warehousecode
						+ "','"
						+ mnemonic
						+ "','"
						+ locationname
						+ "','"
						+ memo
						+ "','" + sjid + "') ";
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_location数据,再导入到Oracle数据库中的TB_WZ_SKWGL表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformKwglData() throws Exception {
		String locationcode = ""; // 库位编码
		String warehousecode = ""; // 仓库编码
		String mnemonic = ""; // 位置助记码
		String locationname = ""; // 仓位名称
		String memo = ""; // 备注
		String levelcode = ""; // 父级节点
		String wz__locationcode = "";
		String sjid = "";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_location order by locationcode";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				locationcode = kwbh + rs.getString("locationcode");
				warehousecode = ckbh + rs.getString("warehousecode");
				mnemonic = rs.getString("mnemonic");
				wz__locationcode = rs.getString("wz__locationcode");
				locationname = rs.getString("locationname");
				levelcode = rs.getString("levelcode");
				if (levelcode == null || levelcode.equals("")) {
//					sjid = getParentByLevelCodeForWzda("");
				} else {
					if (levelcode.split("#").length == 2) {
						sjid = "";
					} else {
//						sjid = getParentByLevelCodeForWzda(levelcode.split("#")[levelcode
//								.split("#").length - 2]);
						sjid = kwbh + levelcode.split("#")[levelcode.split("#").length - 2];
					}
				}
				memo = rs.getString("memo");
				if (memo == null) {
					memo = "";
				}
				if (mnemonic == null) {
					mnemonic = "";
				}
				String oracleSql = "insert into TB_WZ_SKWGL(ID,CKID,WZZJM,CWMC,BZ,SJID) values ('"
						+ locationcode
						+ "','"
						+ warehousecode
						+ "','"
						+ mnemonic
						+ "','"
						+ locationname
						+ "','"
						+ memo
						+ "','" + sjid + "') ";
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}

	/**
	 * 描述 : 从Sqlserver数据库取出表wz_baseinfo数据,再导入到Oracle数据库中的TB_WZ_SWZDA表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformWzdaData() throws Exception {
		String wzbm = "";                 //物资编码
		String wzlbbm = "";               //物资类别编码
		String wzmc = "";                 //物资名称
		String zjm = "";                  //助记码
		String xhgg = "";                 //型号规格
		String jldw = "";                 //计量单位
		double jhdj = 0.0;                //计划单价
		double jqpjj = 0.0;               //平均价
		double stock_max = 0.0;           //最高储备定额
		double stock_min = 0.0;           //最低储备定额
		double stock_total = 0.0;         //当前库存
		double stock_ordered = 0.0;       //已分配数量
		String abc = "";                  //ABC分类
		String wzlb1 = "";                //特殊分类
		String inuse = "";                //在用否
		String location = "";             //库位编码
		double init_stock = 0.0;          //初始库存
		double init_jg = 0.0;             //初始价格
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_baseinfo   order by wzbm";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				wzbm = wzbh + rs.getString("wzbm");
				wzlbbm = wzlbbh + rs.getString("wzlbbm");
				wzmc = rs.getString("wzmc");
				if(wzmc.contains("'")){
					wzmc = wzmc.replace("'", "''");
				}
				zjm = rs.getString("zjm");
				if(zjm == null){
					zjm = "";
				}
				xhgg = rs.getString("xhgg");
				if(xhgg == null){
					xhgg = "";
				}else{
					if(xhgg.contains("'")){
						xhgg = xhgg.replace("'", "''");
					}
				}
				jldw = rs.getString("jldw");
				jhdj = rs.getDouble("jhdj");
				jqpjj = rs.getDouble("jqpjj");
				stock_max = rs.getDouble("stock_max");
				stock_min = rs.getDouble("stock_min");
				stock_total = rs.getDouble("stock_total");
				stock_ordered = rs.getDouble("stock_ordered");
				abc = rs.getString("abc");
				if(abc == null){
					abc = "";
				}
				wzlb1 = rs.getString("wzlb1");
				if(wzlb1 == null){
					wzlb1 = "";
				}
				inuse = rs.getString("inuse");
				location = kwbh + rs.getString("location");
				init_stock = rs.getDouble("init_stock");
				init_jg = rs.getDouble("init_jg");
				String oracleSql  = "update TB_WZ_SWZDA t set t.jhdj = "+jhdj+" where id = '"+wzbm+"'";
//				String oracleSql = "insert into TB_WZ_SWZDATEST(ID,WZBH,WZLBBM,WZMC,ZJM,XHGG,JLDW,JHDJ,PJJ,ZGCBDE,ZDCBDE,DQKC,YFPSL,ABCFL,TSFL,ZYF,KWBM,CSKC,CSJG) values ('"
//						+ wzbm
//						+ "','"
//						+ rs.getString("wzbm")
//						+ "','"
//						+ wzlbbm
//						+ "','"
//						+ wzmc
//						+ "','"
//						+ zjm
//						+ "','"
//						+ xhgg
//						+ "','"
//						+ jldw
//						+ "',"
//						+ jhdj
//						+ ","
//						+ jqpjj
//						+ ","
//						+ stock_max
//						+ ","
//						+ stock_min
//						+ ","
//						+ stock_total
//						+ ","
//						+ stock_ordered
//						+ ",'"
//						+ abc
//						+ "','"
//						+ wzlb1
//						+ "','"
//						+ inuse
//						+ "','"
//						+ location
//						+ "',"
//						+ init_stock
//						+ ","
//						+ init_jg
//						+ ") ";
				System.out.println(oracleSql);
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}

	/**
	 * 描述 : 从Sqlserver数据库取出表wz_category数据,再导入到Oracle数据库中的TB_WZ_SWZLB表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformWzlbData() throws Exception {
		String wzlbbm = "";           //物资类别编码
		String wzlbmc = "";           //物资类别名称
		String levelcode = "";        //父类别编码
		String memo = "";             //备注
		String sjid = "";     
		if(!isExistForWzlbFlbbm()){
			insertLevelcodeForWzlb();
		}
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_category order by wzlbbm";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				wzlbbm = wzlbbh + rs.getString("wzlbbm");
				wzlbmc = rs.getString("wzlbmc");
				if(wzlbmc == null){
					wzlbmc = "";
				}
				levelcode = rs.getString("levelcode");
				if (levelcode == null || levelcode.equals("")) {
					sjid = getParentByLevelCodeForWzlb("");
				} else {
					if (levelcode.split("#").length == 2) {
						sjid = getParentByLevelCodeForWzlb("NULL");
					} else {
						sjid = getParentByLevelCodeForWzlb(levelcode.split("#")[levelcode
								.split("#").length - 2]);
					}
				}
				memo = rs.getString("memo");
				if (memo == null) {
					memo = "";
				}
				String oracleSql = "insert into TB_WZ_SWZLB(ID,FLBBM,WZLBMC,BZ) values ('"
						+ wzlbbm
						+ "','"
						+ sjid
						+ "','"
						+ wzlbmc
						+ "','"
						+ memo
						+ "') ";
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}

	public String getParentByLevelCodeForWzda(String levelcode) throws Exception {
		String locationcode = "";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			String sql = "";
			if (levelcode.equals("NULL")) {
				sql = "select locationcode from wz_location where levelcode is null order by locationcode";
			} else {
				sql = "select locationcode from wz_location where locationcode = '"
						+ levelcode + "' order by locationcode";
			}
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				locationcode = kwbh + rs.getString("locationcode");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
		}
		return locationcode;
	}
	
	public String getParentByLevelCodeForGcmx(String levelcode) throws Exception {
		String gc__projcode = "";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			String sql = "";
			if (levelcode.equals("NULL")) {
				sql = "select gc__projcode from gc_proj where levelcode is null order by projcode";
			} else {
				sql = "select gc__projcode from gc_proj where levelcode = '"
						+ levelcode + "' order by projcode";
			}
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				gc__projcode = projectbh + rs.getString("gc__projcode");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
		}
		return gc__projcode;
	} 
	
	/**
	 * 描述 : 物资类别表中通过levelcode获得父节点ID
	 * 作者 : caofei
	 * 时间 : Dec 23, 2010
	 * 参数 : levelcode
	 * 返回值 : String
	 * 异常 : Exception
	 */
	public String getParentByLevelCodeForWzlb(String levelcode) throws Exception {
		String locationcode = "";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			String sql = "";
			if (levelcode.equals("NULL")) {
				sql = "select wzlbbm from wz_category where levelcode is null order by wzlbbm";
			} else {
				sql = "select wzlbbm from wz_category where wzlbbm = '"
						+ levelcode + "' order by wzlbbm";
			}
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				locationcode = wzlbbh + rs.getString("wzlbbm");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
		}
		return locationcode;
	}
	
	/**
	 * 描述 : 物资类别表中插入一条levelcode为null的数据
	 * 作者 : caofei
	 * 时间 : Dec 23, 2010
	 * 参数 : levelcode
	 * 返回值 : String
	 * 异常 : Exception
	 */
	public final void insertLevelcodeForWzlb() throws Exception {
		String maxWzlbbm = "";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			String sql = "select max(wzlbbm) as maxWzlbbm from wz_category";
			Statement st = sqlserverWzConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				maxWzlbbm = rs.getString("maxWzlbbm");
			}
			maxWzlbbm = String.valueOf((Integer.valueOf(maxWzlbbm) + 1));
			String insertSql = "insert into wz_category values('"+maxWzlbbm+"',null,'物资类别',null,null)";
			st.executeUpdate(insertSql);
			sqlserverWzConn.commit();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
		}
	}
	
	/**
	 * 描述 : 物资类别表中插入一条levelcode为null的数据
	 * 作者 : caofei
	 * 时间 : Dec 23, 2010
	 * 参数 : levelcode
	 * 返回值 : String
	 * 异常 : Exception
	 */
	public final boolean isExistForWzlbFlbbm() throws Exception {
		boolean status = false;
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			String sql = "select * from wz_category where levelcode is null";
			Statement st = sqlserverWzConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				if(rs.getString("wzlbbm") == null){
					status = false;
				}else{
					status = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
		}
		return status;
	}
	
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_providerinfo数据,再导入到Oracle数据库中的TB_WZ_SGYSDA表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformGysData() throws Exception {
		String id = "";                      //ID
		String bm = "";                      //供应商编码
		String gysmc = "";                   //供应商名称
		String dz = "";                      //地址
		String dh = "";                      //电话
		String czh = "";                     //传真号
		String yzbm = "";                    //邮政编码
		String lxr = "";                     //联系人
		String yxdz = "";                    //邮箱地址
		String zywz = "";                    //主页网址
		String frdb = "";                    //法人代表
		String khyh = "";                    //开户银行
		String zh = "";                      //帐号
		String swdjh = "";                   //税务登记号
		String qtxx = "";                    //其它信息
		String sfxydw = "";                  //是否协议单位
		String sfsndw = "";                  //是否市内单位
		String zjm = "";                     //助记码
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_providerinfo order by providercode";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				id = gysbh + rs.getString("providercode");
				bm = rs.getString("providercode");
				if(StringUtil.isNotEmpty(rs.getString("providername"))){
					gysmc = rs.getString("providername");
				}
				if(StringUtil.isNotEmpty(rs.getString("address"))){
					dz = rs.getString("address");
				}
				if(StringUtil.isNotEmpty(rs.getString("phone"))){
					dh = rs.getString("phone");
				}
				if(StringUtil.isNotEmpty(rs.getString("fax"))){
					czh = rs.getString("fax");
				}
				if(StringUtil.isNotEmpty(rs.getString("zipcode"))){
					yzbm = rs.getString("zipcode");
				}
				if(StringUtil.isNotEmpty(rs.getString("linkman"))){
					lxr = rs.getString("linkman");
				}
				if(StringUtil.isNotEmpty(rs.getString("mailaddress"))){
					yxdz = rs.getString("mailaddress");
				}
				if(StringUtil.isNotEmpty(rs.getString("url"))){
					zywz = rs.getString("url");
				}
				if(StringUtil.isNotEmpty(rs.getString("juridicalperson"))){
					frdb = rs.getString("juridicalperson");
				}
				if(StringUtil.isNotEmpty(rs.getString("bank"))){
					khyh = rs.getString("bank");
				}
				if(StringUtil.isNotEmpty(rs.getString("accounts"))){
					zh = rs.getString("accounts");
				}
				if(StringUtil.isNotEmpty(rs.getString("taxnumber"))){
					swdjh = rs.getString("taxnumber");
				}
				if(StringUtil.isNotEmpty(rs.getString("otherinfo"))){
					qtxx = rs.getString("otherinfo");
				}
				if(StringUtil.isNotEmpty(rs.getString("protocol"))){
					sfxydw = rs.getString("protocol");
				}
				if(StringUtil.isNotEmpty(rs.getString("incity"))){
					sfsndw = rs.getString("incity");
				}
				if(StringUtil.isNotEmpty(rs.getString("zjm"))){
					zjm = rs.getString("zjm");
				}
				String oracleSql = "insert into TB_WZ_SGYSDA(ID,BM,GYSMC,DZ,DH,CZH,YZBM,LXR,YXDZ,ZYWZ,FRDB,KHYH,ZH,SWDJH,QTXX,SFXYDW,SFSNDW,ZJM) values ('"
					+ id
					+ "','"
					+ bm
					+ "','"
					+ gysmc
					+ "','"
					+ dz
					+ "','"
					+ dh
					+ "','"
					+ czh
					+ "','"
					+ yzbm
					+ "','"
					+ lxr
					+ "','"
					+ yxdz
					+ "','"
					+ zywz
					+ "','"
					+ frdb
					+ "','"
					+ khyh
					+ "','"
					+ zh
					+ "','"
					+ swdjh
					+ "','"
					+ qtxx
					+ "','"
					+ sfxydw
					+ "','"
					+ sfsndw
					+ "','"
					+ zjm
					+ "') ";
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	
	
	/**
	 * 描述 : 从Sqlserver数据库取出表gc_proj数据,再导入到Oracle数据库中的TB_WZ_PROJ表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformGcxmData() throws Exception {
		String id = "";
		String projcode = "";
		String projcatcode = "";
		String gc__projcode = "";
		String projname = "";
		String taskdesc = "";
		String target = "";
		String execdept = "";
		String starttime_plan = null;
		String endtime_plan = null;
		String starttime_fact = null;
		String endtime_fact = null;
		double fundlimit = 0.0;
		double fundused = 0.0;
		double mfundlimit = 0.0;
		double mfundused = 0.0;
		String judge = "";
		String finished = "";
		String limiton = "";
		String timelimit = "";
		String levelcode = "";
		String gcxmbh = "";
		double pprojfee = 0.0;
		double aprojfee = 0.0;
		String needplan = "";
		String c_c_sgdw = "";
		String c_c_fybm = "";
		int i = 1;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from gc_proj where len(levelcode)=4 or levelcode is null order by projcode";
			Statement st = sqlserverWzConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			List<Proj> rootList=new ArrayList<Proj>();
			while (rs.next()) {
				System.out.println(i);
				Proj proj = new Proj();
				id = UUIDGenerator.hibernateUUID();
				projcode = rs.getString("projcode");
				projcatcode = gclbbh + rs.getString("projcatcode");
				projname = rs.getString("projname");
				if(StringUtil.isNotEmpty(rs.getString("taskdesc"))){
					taskdesc = rs.getString("taskdesc");
				}
				if(StringUtil.isNotEmpty(rs.getString("target"))){
					target = rs.getString("target");
				}
				if(StringUtil.isNotEmpty(rs.getString("execdept"))){
					execdept = rs.getString("execdept");
				}
				if(StringUtil.isNotEmpty(rs.getString("starttime_plan"))){
					Date date = rs.getTimestamp("starttime_plan");
					String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
					starttime_plan = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
				}
				if(StringUtil.isNotEmpty(rs.getString("endtime_plan"))){
					Date date = rs.getTimestamp("endtime_plan");
					String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
					endtime_plan = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
				}
				if(StringUtil.isNotEmpty(rs.getString("starttime_fact"))){
					Date date = rs.getTimestamp("starttime_fact");
					String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
					starttime_fact = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
				}
				if(StringUtil.isNotEmpty(rs.getString("starttime_fact"))){
					Date date = rs.getTimestamp("starttime_fact");
					String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
					endtime_fact = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
				}
				if(Double.valueOf(rs.getDouble("fundlimit")) != null){
					fundlimit = rs.getDouble("fundlimit");
				}
				if(Double.valueOf(rs.getDouble("fundused")) != null){
					fundused = rs.getDouble("fundused");
				}
				if(Double.valueOf(rs.getDouble("mfundlimit")) != null){
					mfundlimit = rs.getDouble("mfundlimit");
				}
				if(Double.valueOf(rs.getDouble("mfundused")) != null){
					mfundused = rs.getDouble("mfundused");
				}
				if(StringUtil.isNotEmpty(rs.getString("judge"))){
					judge = rs.getString("judge");
				}
				if(StringUtil.isNotEmpty(rs.getString("finished"))){
					finished = rs.getString("finished");
				}
				if(StringUtil.isNotEmpty(rs.getString("limiton"))){
					limiton = rs.getString("limiton");
				}
				if(StringUtil.isNotEmpty(rs.getString("timelimit"))){
					timelimit = rs.getString("timelimit");
				}
				if(StringUtil.isNotEmpty(rs.getString("levelcode"))){
					levelcode = rs.getString("levelcode");
				}
				if(StringUtil.isNotEmpty(rs.getString("gcxmbh"))){
					gcxmbh = rs.getString("gcxmbh");
				}
				if(Double.valueOf(rs.getDouble("pprojfee")) != null){
					pprojfee = rs.getDouble("pprojfee");
				}
				if(Double.valueOf(rs.getDouble("aprojfee")) != null){
					aprojfee = rs.getDouble("aprojfee");
				}
				if(StringUtil.isNotEmpty(rs.getString("needplan"))){
					needplan = rs.getString("needplan");
				}
				if(StringUtil.isNotEmpty(rs.getString("c_c_sgdw"))){
					c_c_sgdw = rs.getString("c_c_sgdw");
				}
				if(StringUtil.isNotEmpty(rs.getString("c_c_fybm"))){
					c_c_fybm = rs.getString("c_c_fybm");
				}
				String oracleSql = "insert into TB_WZ_PROJ(ID,PROJCODE,PROJCATCODE,GC__PROJCODE," +
						"PROJNAME,TASKDESC,TARGET,EXECDEPT,STARTTIME_PLAN,ENDTIME_PLAN,STARTTIME_FACT," +
						"ENDTIME_FACT,FUNDLIMIT,FUNDUSED,MFUNDLIMIT,MFUNDUSED,JUDGE,FINISHED,LIMITON," +
						"TIMELIMIT,LEVELCODE,GCXMBH,PPROJFEE,APROJFEE,NEEDPLAN,C_C_SGDW,C_C_FYBM) values ('"
					+ id
					+ "','"
					+ projcode
					+ "','"
					+ projcatcode
					+ "','"
					+ gc__projcode
					+ "','"
					+ projname
					+ "','"
					+ taskdesc
					+ "','"
					+ target
					+ "','"
					+ execdept
					+ "',"
					+ starttime_plan
					+ ","
					+ endtime_plan
					+ ","
					+ starttime_fact
					+ ","
					+ endtime_fact
					+ ","
					+ fundlimit
					+ ","
					+ fundused
					+ ","
					+ mfundlimit
					+ ","
					+ mfundused
					+ ",'"
					+ judge
					+ "','"
					+ finished
					+ "','"
					+ limiton
					+ "','"
					+ timelimit
					+ "','"
					+ levelcode
					+ "','"
					+ gcxmbh
					+ "',"
					+ pprojfee
					+ ","
					+ aprojfee
					+ ",'"
					+ needplan
					+ "','"
					+ c_c_sgdw
					+ "','"
					+ c_c_fybm
					+ "') ";
				System.out.println(oracleSql);
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				proj.setId(id);
				proj.setLevelcode(this.findProjLevelcodeById(id));
				rootList.add(proj);
				i++;
			}
			this.findChild(rootList, sqlserverWzConn, oracleConn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	
	/**
	 * 描述 : 从Sqlserver数据库取出表gc_projcat数据,再导入到Oracle数据库中的TB_WZ_PROJCAT表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformGclbData() throws Exception {
		String id = "";
		String projcatcode = "";
		String projcatname = "";
		String memo = "";
		String predefined = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from gc_projcat order by projcatcode";
			Statement st = sqlserverWzConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				id = gclbbh + rs.getString("projcatcode");
				if(StringUtil.isNotEmpty(rs.getString("projcatcode"))){
					projcatcode = rs.getString("projcatcode");
				}
				if(StringUtil.isNotEmpty(rs.getString("projcatname"))){
					projcatname = rs.getString("projcatname");
				}
				if(StringUtil.isNotEmpty(rs.getString("memo"))){
					memo = rs.getString("memo");
				}
				if(StringUtil.isNotEmpty(rs.getString("predefined"))){
					predefined = rs.getString("predefined");
				}
				String oracleSql = "insert into TB_WZ_PROJCAT(ID,PROJCATCODE,PROJCATNAME,MEMO,PREDEFINED) values ('"
					+ id
					+ "','"
					+ projcatcode
					+ "','"
					+ projcatname
					+ "','"
					+ memo
					+ "','"
					+ predefined
					+ "') ";
				System.out.println(oracleSql);
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	
	
	/**
	 * 描述 : 查询工程项目子项目（递归查询）
	 * 作者 : caofei
	 * 时间 : Mar 3, 2011
	 * 参数 : rootList, sqlserverWzConn, oracleConn
	 * 返回值 : null
	 * 异常 : 
	 */
	public void findChild(List<Proj> rootList, Connection sqlserverWzConn, Connection oracleConn) throws Exception{
		String id = "";
		String projcode = "";
		String projcatcode = "";
		String gc__projcode = "";
		String projname = "";
		String taskdesc = "";
		String target = "";
		String execdept = "";
		String starttime_plan = null;
		String endtime_plan = null;
		String starttime_fact = null;
		String endtime_fact = null;
		double fundlimit = 0.0;
		double fundused = 0.0;
		double mfundlimit = 0.0;
		double mfundused = 0.0;
		String judge = "";
		String finished = "";
		String limiton = "";
		String timelimit = "";
		String levelcode = "";
		String gcxmbh = "";
		double pprojfee = 0.0;
		double aprojfee = 0.0;
		String needplan = "";
		String c_c_sgdw = "";
		String c_c_fybm = "";
		for(Proj proj:rootList){
			String sql="select * from gc_proj where len(levelcode)="+(proj.getLevelcode().length()+4)+" and levelcode like '"+proj.getLevelcode()+"%' order by projcode";
			Statement st = sqlserverWzConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				rs.beforeFirst();
				List<Proj> childList=new ArrayList<Proj>();
				while(rs.next()){
					Proj childProj = new Proj();
					id = UUIDGenerator.hibernateUUID();
					projcode = rs.getString("projcode");
					projcatcode = gclbbh + rs.getString("projcatcode");
					gc__projcode = proj.getId();
					projname = rs.getString("projname");
					if(StringUtil.isNotEmpty(rs.getString("taskdesc"))){
						taskdesc = rs.getString("taskdesc");
					}
					if(StringUtil.isNotEmpty(rs.getString("target"))){
						target = rs.getString("target");
					}
					if(StringUtil.isNotEmpty(rs.getString("execdept"))){
						execdept = rs.getString("execdept");
					}
					if(StringUtil.isNotEmpty(rs.getString("starttime_plan"))){
						Date date = rs.getTimestamp("starttime_plan");
						String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
						starttime_plan = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
					}
					if(StringUtil.isNotEmpty(rs.getString("endtime_plan"))){
						Date date = rs.getTimestamp("endtime_plan");
						String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
						endtime_plan = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
					}
					if(StringUtil.isNotEmpty(rs.getString("starttime_fact"))){
						Date date = rs.getTimestamp("starttime_fact");
						String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
						starttime_fact = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
					}
					if(StringUtil.isNotEmpty(rs.getString("endtime_fact"))){
						Date date = rs.getTimestamp("endtime_fact");
						String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
						endtime_fact = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
					}
					if(Double.valueOf(rs.getDouble("fundlimit")) != null){
						fundlimit = rs.getDouble("fundlimit");
					}
					if(Double.valueOf(rs.getDouble("fundused")) != null){
						fundused = rs.getDouble("fundused");
					}
					if(Double.valueOf(rs.getDouble("mfundlimit")) != null){
						mfundlimit = rs.getDouble("mfundlimit");
					}
					if(Double.valueOf(rs.getDouble("mfundused")) != null){
						mfundused = rs.getDouble("mfundused");
					}
					if(StringUtil.isNotEmpty(rs.getString("judge"))){
						judge = rs.getString("judge");
					}
					if(StringUtil.isNotEmpty(rs.getString("finished"))){
						finished = rs.getString("finished");
					}
					if(StringUtil.isNotEmpty(rs.getString("limiton"))){
						limiton = rs.getString("limiton");
					}
					if(StringUtil.isNotEmpty(rs.getString("timelimit"))){
						timelimit = rs.getString("timelimit");
					}
					if(StringUtil.isNotEmpty(rs.getString("levelcode"))){
						levelcode = rs.getString("levelcode");
					}
					if(StringUtil.isNotEmpty(rs.getString("gcxmbh"))){
						gcxmbh = rs.getString("gcxmbh");
					}
					if(Double.valueOf(rs.getDouble("pprojfee")) != null){
						pprojfee = rs.getDouble("pprojfee");
					}
					if(Double.valueOf(rs.getDouble("aprojfee")) != null){
						aprojfee = rs.getDouble("aprojfee");
					}
					if(StringUtil.isNotEmpty(rs.getString("needplan"))){
						needplan = rs.getString("needplan");
					}
					if(StringUtil.isNotEmpty(rs.getString("c_c_sgdw"))){
						c_c_sgdw = rs.getString("c_c_sgdw");
					}
					if(StringUtil.isNotEmpty(rs.getString("c_c_fybm"))){
						c_c_fybm = rs.getString("c_c_fybm");
					}
					String oracleSql = "insert into TB_WZ_PROJ(ID,PROJCODE,PROJCATCODE,GC__PROJCODE," +
							"PROJNAME,TASKDESC,TARGET,EXECDEPT,STARTTIME_PLAN,ENDTIME_PLAN,STARTTIME_FACT," +
							"ENDTIME_FACT,FUNDLIMIT,FUNDUSED,MFUNDLIMIT,MFUNDUSED,JUDGE,FINISHED,LIMITON," +
							"TIMELIMIT,LEVELCODE,GCXMBH,PPROJFEE,APROJFEE,NEEDPLAN,C_C_SGDW,C_C_FYBM) values ('"
						+ id
						+ "','"
						+ projcode
						+ "','"
						+ projcatcode
						+ "','"
						+ gc__projcode
						+ "','"
						+ projname
						+ "','"
						+ taskdesc
						+ "','"
						+ target
						+ "','"
						+ execdept
						+ "',"
						+ starttime_plan
						+ ","
						+ endtime_plan
						+ ","
						+ starttime_fact
						+ ","
						+ endtime_fact
						+ ","
						+ fundlimit
						+ ","
						+ fundused
						+ ","
						+ mfundlimit
						+ ","
						+ mfundused
						+ ",'"
						+ judge
						+ "','"
						+ finished
						+ "','"
						+ limiton
						+ "','"
						+ timelimit
						+ "','"
						+ levelcode
						+ "','"
						+ gcxmbh
						+ "',"
						+ pprojfee
						+ ","
						+ aprojfee
						+ ",'"
						+ needplan
						+ "','"
						+ c_c_sgdw
						+ "','"
						+ c_c_fybm
						+ "') ";
					System.out.println(oracleSql);
					Statement statement = oracleConn.createStatement();
					statement.executeUpdate(oracleSql);
					oracleConn.commit();
					statement.close();
					childProj.setId(id);
					childProj.setLevelcode(this.findProjLevelcodeById(id));
					childList.add(childProj);
				}
				findChild(childList,sqlserverWzConn,oracleConn);
			}else{
				continue;
			}
		}
	}
	
	/**
	 * 
	 * 描述 : 查询工程项目级别编码
	 * 作者 : caofei
	 * 时间 : Mar 3, 2011
	 * 参数 : id
	 * 返回值 : String
	 * 异常 :
	 */
	public String findProjLevelcodeById(String id) throws Exception {
		String levelcode = "";
		try {
			oracleConn = getOracleConnection();
			String sql = "select * from tb_wz_proj where id = '"+id+"'";
			Statement st = oracleConn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				levelcode = rs.getString("levelcode");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (oracleConn != null) {
//				oracleConn.close();
//			}
		}
		return levelcode;
	}
	
	
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_yhd数据,再导入到Oracle数据库中的TB_WZ_YYHD表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformYhdData() throws Exception {
		String id = "";             //ID
		String ysrq = "";           //验收日期
		String ghdw = "";           //供货单位
		String cgy = "0035";            //采购员(对应老物资系统里面的"ysy"验收员)
		String zt = "";             //状态("0"表示未生效;"2"表示生效)
		String bgy = "";            //保管员(【0157】易崇龙)
		String bh = "";             //待验货单编号
		String htbh = "";           //合同编号
		String dhrq = "";           //到货日期
		String bz = "";
		String jhy = "0055";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_yhd where ysrq is null and status = 0 order by yhdbh";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				id = yhdbh+rs.getString("yhdbh");
				if(StringUtil.isNotEmpty(rs.getString("ysrq"))){
					Date date = rs.getTimestamp("ysrq");
					System.out.println(date);
					String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
					ysrq = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
				}
				if(StringUtil.isNotEmpty(rs.getString("ghdw"))){
					ghdw = rs.getString("ghdw");
				}
				//if(!("").equals(rs.getString("ysy"))){
				//	cgy = this.findLoginNameByPersonName(this.getMemberName(rs.getString("ysy")));
				//}
				zt = "0";
				bgy = "0157";
				if(StringUtil.isNotEmpty(rs.getString("yhdbh"))){
					bh = rs.getString("yhdbh");
				}
				if(StringUtil.isNotEmpty(rs.getString("htbh"))){
					htbh = rs.getString("htbh");
				}
				if(StringUtil.isNotEmpty(rs.getString("htbh"))){
					Date date = rs.getTimestamp("htbh");
					System.out.println(date);
					String strDate=DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
					dhrq = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
				}
				String oracleSql = "insert into TB_WZ_YYHD(ID,YSRQ,GHDW,CGY,ZT,BGY,BH,HTBH,DHRQ) values ('"
					+ id
					+ "','"
					+ ysrq
					+ "','"
					+ ghdw
					+ "','"
					+ cgy
					+ "','"
					+ zt
					+ "','"
					+ bgy
					+ "','"
					+ bh
					+ "','"
					+ htbh
					+ "','"
					+ dhrq
					+ "') ";
				//虚拟一条采购计划数据
				String cgjhOracleSql = "insert into TB_WZ_YCGJH(ID,BH,ZDSJ,SXSJ,BZ,ZT,JHY) values ('"
					+ (cgjhbh + rs.getString("yhdbh"))
					+ "','"
					+ bh
					+ "','"
					+ dhrq
					+ "','"
					+ dhrq
					+ "','"
					+ bz
					+ "','"
					+ 1
					+ "','"
					+ jhy
					+ "') ";
				System.out.println(oracleSql);
				System.out.println(cgjhOracleSql);
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				statement.executeUpdate(cgjhOracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	
	/**
	 * 通过用户名(中文)查询登录名（0012）
	 */
	public String findLoginNameByPersonName(String personName) throws Exception {
		String loginName = "";
		try {
			oracleConn = getOracleConnection();
			String sql = "select * from tb_sys_person where personname = '"+personName+"'";
			Statement st = oracleConn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				loginName = rs.getString("LOGIN_NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (oracleConn != null) {
//				oracleConn.close();
//			}
		}
		return loginName;
	}
	
	
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_yhd_detail数据,再导入到Oracle数据库中的TB_WZ_YYHDMX表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformYhdDetailData() throws Exception {
		String id = "";            //id
		String ghdw = "";          //供货单位
		String xh = "";            //序号
		double sl = 0.0;           //税率(对应老物资【jdxs】加点系数)
		double zf = 0.0;           //杂费
		String wzbm = "";          //物资编码
		String yhdBH = "";         //验货单编号
		double tssl = 0.0;         //推送数量
		String fpbh = "";          //发票编号(在yhd表里面获得)
		double dhsl = 0.0;         //到货数量
		String cgjldw = "";        //采购计量单位
		double sqdj = 0.0;         //税前单价
		double yssl = 0.0;         //验货数量
		double jhdj = 0.0;         //计划单价(对应老物资【gsdj】)
		double hsxs = 0.0;         //换算系数
		String cgdmx = "";         //采购单明细
		String zt = "";            //状态
		Date rksj = null;          //入库时间
		String dhrq = "";          //到货日期
		String cgy = "0035";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_yhd_detail where yhdbh in (select yhdbh from wz_yhd where ysrq is null and status = 0)";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				id = UUIDGenerator.hibernateUUID();
				if(StringUtil.isNotEmpty(rs.getString("ghdw"))){
					ghdw = rs.getString("ghdw");
				}
				xh = rs.getString("xh");
				sl = rs.getDouble("jdxs");
				wzbm = wzbh + rs.getString("wzbm");
				yhdBH = yhdbh + rs.getString("yhdbh");
//				fpbh = this.findYhdFpbhByYhdbh(rs.getString("yhdbh"));
				dhsl = rs.getDouble("dhsl");
				cgjldw = rs.getString("cgjldw");
				sqdj = rs.getDouble("sqdj");
				yssl = rs.getDouble("yssl");
				jhdj = rs.getDouble("gsdj");
				hsxs = rs.getDouble("hsxs");
				zt = "0";
				cgdmx = UUIDGenerator.hibernateUUID();
				String oracleSql = "insert into TB_WZ_YYHDMX(ID,GHDW,XH,SL,WZBM,YHDBH," +
						"FPBH,DHSL,CGJLDW,SQDJ,YSSL,JHDJ,HSXS,ZT,CGDMX) values ('"
					+ id
					+ "','"
					+ ghdw
					+ "','"
					+ xh
					+ "','"
					+ sl
					+ "','"
					+ wzbm
					+ "','"
					+ yhdBH
					+ "','"
					+ fpbh
					+ "','"
					+ dhsl
					+ "','"
					+ cgjldw
					+ "','"
					+ sqdj
					+ "','"
					+ yssl
					+ "','"
					+ jhdj
					+ "','"
					+ hsxs
					+ "','"
					+ zt
					+ "','"
					+ cgdmx
					+ "') ";
				//虚拟一条采购计划明细数据
				String cgjhmxOracleSql = "insert into TB_WZ_YCGJHMX(ID,CGJHBH,WZBM,XH,JHDJ,CGJLDW," +
					"CGSL,HSXS,JHDHRQ,DHSL,CGFX,CGY,ZT) values ('"
					+ cgdmx
					+ "','"
					+ (cgjhbh + rs.getString("yhdbh"))
					+ "','"
					+ wzbm
					+ "','"
					+ xh
					+ "','"
					+ jhdj
					+ "','"
					+ cgjldw
					+ "','"
					+ dhsl
					+ "','"
					+ hsxs
					+ "','"
					+ dhrq
					+ "','"
					+ dhsl
					+ "','"
					+ 0
					+ "','"
					+ cgy
					+ "','"
					+ 1
					+ "') ";
				System.out.println(oracleSql);
				System.out.println(cgjhmxOracleSql);
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				statement.executeUpdate(cgjhmxOracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	
	/**
	 * 通过验货单编号查询发票号(从yhd表中查询)
	 */
	public String findYhdFpbhByYhdbh(String yhdbh) throws Exception {
		String fpbh = "";
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			String sql = "select * from wz_yhd where yhdbh = '"+yhdbh+"'";
			Statement st = sqlserverRsConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				if(StringUtil.isEmpty(rs.getString("fpbh"))){
					fpbh = "";
				}else{
					fpbh = rs.getString("fpbh");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
		}
		return fpbh;
	}
	
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_lyd数据,再导入到Oracle数据库中的TB_WZ_YLYD表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformLydData() throws Exception {
		String id = "";         //id
		String gclb = "";       //工程类别
		String czr = "";        //操作人
		String lysqbh = "";     //领用申请编号
		String zt = "";         //状态
		String gcxm = "";       //工程项目
		String llr = "";        //领料人
		String lysj = null;       //领用时间
		String lybm = "";       //领用部门
		String lydmx = "";      //领用单明细
		String bh = "";         //领用单编号
		String lydqf = "1";     //领用单区分字段,1：领用单   2：借料单
		String lczt = "已完结";  //流程状态：批准中；已完结
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_lyd where status = '0' order by lydbh";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				id = lydbh + rs.getString("lydbh");
				gclb = this.findGclbNameById(gclbbh + rs.getString("gclb"));
				if(StringUtil.isNotEmpty(rs.getString("operator"))){
					czr = this.getMemberName(rs.getString("operator"));
				}
//				if(StringUtil.isNotEmpty(rs.getString("lysqbh"))){
//					lysqbh = rs.getString("lysqbh");
//				}else{
//					lysqbh = "";
//				}
				zt = "0";
				gcxm = this.findGcxmNameById(rs.getString("gcxm"));
				if(StringUtil.isNotEmpty(rs.getString("llr"))){
					llr = this.getMemberName(rs.getString("llr"));
				}
				if(StringUtil.isNotEmpty(rs.getString("lysj"))){
					Date date = rs.getTimestamp("lysj");
					String strDate = DateUtils.getDate(date, "yyyy-MM-dd hh:mm:ss");
					lysj = "to_date('"+strDate+"',"+"'yyyy-mm-dd hh24:mi:ss')";
				}
				lybm = this.findGroupByLoginName(this.findLoginNameByPersonName(this.getMemberName(rs.getString("llr"))));
				bh = rs.getString("lydbh");
				String oracleSql = "insert into TB_WZ_YLYD(id,gclb,czr,lysqbh,zt,gcxm," +
						"llr,lysj,lybm,lydmx,bh,lydqf,lczt) values ('"
					+ id
					+ "','"
					+ gclb
					+ "','"
					+ czr
					+ "','"
					+ lysqbh
					+ "','"
					+ zt
					+ "','"
					+ gcxm
					+ "','"
					+ llr
					+ "',"
					+ lysj
					+ ",'"
					+ lybm
					+ "','"
					+ lydmx
					+ "','"
					+ bh
					+ "','"
					+ lydqf
					+ "','"
					+ lczt
					+ "')";
				System.out.println(oracleSql);
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	
	/**
	 * 通过login_name获取所在部门
	 */
	public String findGroupByLoginName(String loginName) throws Exception {
		String groupName = "";
		try {
			oracleConn = getOracleConnection();
			String sql = "select t.groupname from tb_sys_group t where t.id in (select s.groupid from tb_sys_person2group s where s.personid in (select p.id from tb_sys_person p where p.login_name = '"+loginName+"' ))";
			Statement st = oracleConn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				groupName = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (oracleConn != null) {
//				oracleConn.close();
//			}
		}
		return groupName;
		
	}
	
	/**
	 * 通过工程类别ID获取工程名称
	 */
	public String findGclbNameById(String id) throws Exception {
		String gclbName = "";
		try {
			oracleConn = getOracleConnection();
			String sql = "select * from tb_wz_projcat t where t.id = '"+id+"'";
			Statement st = oracleConn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				gclbName = rs.getString("PROJCATNAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (oracleConn != null) {
//				oracleConn.close();
//			}
		}
		return gclbName;
	}
	
	/**
	 * 通过工程项目ID获取工程项目名称
	 */
	public String findGcxmNameById(String id) throws Exception {
		String gcxmName = "";
		try {
			oracleConn = getOracleConnection();
			String sql = "select * from tb_wz_proj where  projcode = '"+id+"'";
			Statement st = oracleConn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				gcxmName = rs.getString("PROJNAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (oracleConn != null) {
//				oracleConn.close();
//			}
		}
		return gcxmName;
	}
	
	
	/**
	 * 描述 : 从Sqlserver数据库取出表wz_lyd_detail数据,再导入到Oracle数据库中的TB_WZ_YLYDMX表中 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public void transformLydDetailData() throws Exception {
		String id = "";          //id
		double jhdj = 0.0;       //计划单价【对应老物资jg】
		double pzlysl = 0.0;     //批转领用数量
		String xh = "";          //序号
		double sjlysl = 0.0;     //实际领用数量
		String jldw = "";        //计量单位
		String wzbm = "";        //物资编码
		String lydBh = "";       //领用单编号
		String lysqmx = "";      //领用申请明细
		String zt = "0";          //状态
		try {
			sqlserverWzConn = getSqlServerWzConnection();
			oracleConn = getOracleConnection();
			String sql = "select * from wz_lyd_detail where lydbh in (select lydbh from wz_lyd where status = '0')";
			Statement st = sqlserverWzConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				id = UUIDGenerator.hibernateUUID();
				jhdj = rs.getDouble("jg");
				if(StringUtil.isNotEmpty(rs.getString("pzlysl"))){
					pzlysl = rs.getDouble("pzlysl");
				}
				xh = rs.getString("xh");
				sjlysl = rs.getDouble("sjlysl");
				jldw = rs.getString("jldw");
				wzbm = wzbh + rs.getString("wzbm");
				lydBh = lydbh + rs.getString("lydbh");
				String oracleSql = "insert into TB_WZ_YLYDMX(ID,JHDJ,PZLYSL,XH,SJLYSL,JLDW," +
						"WZBM,LYDBH,LYSQMX,ZT) values ('"
					+ id
					+ "',"
					+ jhdj
					+ ","
					+ pzlysl
					+ ",'"
					+ xh
					+ "',"
					+ sjlysl
					+ ",'"
					+ jldw
					+ "','"
					+ wzbm
					+ "','"
					+ lydBh
					+ "','"
					+ lysqmx
					+ "','"
					+ zt
					+ "')";
				System.out.println(oracleSql);
				Statement statement = oracleConn.createStatement();
				statement.executeUpdate(oracleSql);
				oracleConn.commit();
				statement.close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverWzConn != null) {
				sqlserverWzConn.close();
			}
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	
	/**
	 * 描述 : 从Sqlserver数据库通过用户编号取出表t_su_memdept中用户名称 作者 :
	 * caofei 时间 : Dec 23, 2010 参数 : null 返回值 : null 异常 : Exception
	 */
	public String getMemberName(String memberBh) throws Exception{
		String memberName = "";
		try {
			sqlserverRsConn = getSqlServerRsConnection();
			String sql = "select c_username from t_su_user where c_userid = '"+memberBh+"'";
			Statement st = sqlserverRsConn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				memberName = rs.getString("c_username");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlserverRsConn != null) {
				sqlserverRsConn.close();
			}
		}
		return memberName;
	}
	/**
	 * 根据表名称恢复历史数据  
	 */
	private void reloadDateFormAll(String table){
		try {
			oracleConn = this.getOracle68Connection();
			Statement st = oracleConn.createStatement();
			ResultSetMetaData metadata = null; 
			ResultSet rs = st.executeQuery("select * from "+table);
			

			myOracleConn = this.getOracleConnection();			
			Statement st1 = myOracleConn.createStatement();
			ResultSet rs1 = null;
			
			int j =0;
			while(rs.next()){
				StringBuffer values = new StringBuffer();
				rs1 = st1.executeQuery("select * from "+table+" where id = '"+rs.getString("id")+"'");
				if(!rs1.next()){
					j++;
					metadata=rs.getMetaData();
					for(int i=1;i<=metadata.getColumnCount();i++){
						if("NUMBER".equals(metadata.getColumnTypeName(i))){
							values.append(rs.getDouble(metadata.getColumnName(i)));
						}
		                if("VARCHAR2".equals(metadata.getColumnTypeName(i))){
		                	if("null".equals(rs.getString(metadata.getColumnName(i)))){
		                		values.append("''");
		                	}else{
		                		values.append("'"+rs.getString(metadata.getColumnName(i))+"'");
		                	}
		                }
		                if(i<metadata.getColumnCount()){
		                	values.append(",");
		                }
		            }
					System.out.println("insert into "+table+" values("+values.toString()+")");
					st1.execute("insert into "+table+" values("+values.toString()+")");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 描述 : 清空Oracle数据库中TB_WZ_SWZDA、TB_WZ_SWZLB、TB_WZ_CKGL、TB_WZ_SKWGL的数据
	 * 作者 : caofei
	 * 时间 : Dec 23, 2010
	 * 参数 : levelcode
	 * 返回值 : String
	 * 异常 : Exception
	 */
	public final void clearData() throws Exception {
		try {
			oracleConn = getOracleConnection();
			String wzdaSql = "truncate table TB_WZ_SWZDA";
			String wzlbSql = "truncate table TB_WZ_SWZLB";
			String ckglSql = "truncate table TB_WZ_CKGL";
			String kwglSql = "truncate table TB_WZ_SKWGL";
			String gysSql = "truncate table TB_WZ_SGYSDA";
			String gcxmSql = "truncate table TB_WZ_PROJ";
			String gclbSql = "truncate table TB_WZ_PROJCAT";
			String yhdSql = "truncate table TB_WZ_YYHD";
			String yhdmxSql = "truncate table TB_WZ_YYHDMX";
			String lydSql = "truncate table TB_WZ_YLYD";
			String lydmxSql = "truncate table TB_WZ_YLYDMX";
			String cgjhSql = "truncate table TB_WZ_YCGJH";
			String cgjhmxSql = "truncate table TB_WZ_YCGJHMX";
			Statement statement = oracleConn.createStatement();
			statement.executeUpdate(wzdaSql);
			statement.executeUpdate(wzlbSql);
			statement.executeUpdate(ckglSql);
			statement.executeUpdate(kwglSql);
			statement.executeUpdate(gysSql);
			statement.executeUpdate(gcxmSql);
			statement.executeUpdate(gclbSql);
//			statement.executeUpdate(cgjhSql);
//			statement.executeUpdate(cgjhmxSql);
//			statement.executeUpdate(yhdSql);
//			statement.executeUpdate(yhdmxSql);
//			statement.executeUpdate(lydSql);
//			statement.executeUpdate(lydmxSql);
			oracleConn.commit();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oracleConn != null) {
				oracleConn.close();
			}
		}
	}
	private static DOMBuilder builder=null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TransformDataTest test = new TransformDataTest();
		//统计错误的几个日期
		String[] dateArr = new String[]{"2011-09","2011-10","2011-11"};
		try {
			 Connection conn = test.getMyOracleConnection();
			 Statement st = conn.createStatement();
			 Statement updateSt = conn.createStatement();
			 for(int i =0;i<dateArr.length-1;i++){
				 String sqlQuery ="select a.xmmc, a.nzj + b.hj as nzj from (select nzj, xmmc from tb_jx_qxgl_qxtj" +
			 		"  where dctjq = '"+dateArr[i]+"' and fl = 'xq' and xmmc != 'FDJSXQL' and xmmc !='JXJSXQL' and xmmc!='JXXQL') a, " +
			 		"(select hj, xmmc from tb_jx_qxgl_qxtj  where dctjq = '"+dateArr[i+1]+"' and fl = 'xq' and xmmc != 'FDJSXQL' " +
			 				"and xmmc != 'JXJSXQL' and xmmc != 'JXXQL') b" +
			 		" where a.xmmc = b.xmmc  order by a.xmmc";
				 //查询正确的 年总计
				 System.out.println(sqlQuery);
				 ResultSet rs =st.executeQuery(sqlQuery);
				 while(rs.next()){
					 String sql = "update tb_jx_qxgl_qxtj t set t.nzj = '"+rs.getString(2)+"'" +
				 		" where t.dctjq ='"+dateArr[i+1]+"' and t.fl='xq' and t.xmmc ='"+rs.getString(1)+"'";
					 System.out.println(sql);
					 updateSt.execute(sql);
				}
			 }
			
//			System.out.println("<----数据导入完成!---->");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
}
