package com.jteap.test.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import com.jteap.core.web.SpringContextUtil;

public class DataTest {

	Connection conn = null;

	public final Connection getConnection() throws Exception {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			// System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:sqlserver://127.0.0.1:1433; databasename=localwz";
			String user = "sa";
			String password = "sa";
			conn = DriverManager.getConnection(url, user, password);
			// System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("数据库连接失败");
		}
		return conn;
	}

	public String TransationData() throws Exception {
		try {
			DataSource dataSource = (DataSource) SpringContextUtil
					.getBean("dataSource");
			conn = dataSource.getConnection();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public String getLevelCode() throws Exception {
		String resultValue = "";
		try {
			conn = getConnection();
			String sql = "select * from wz_location order by locationcode";
			Statement st = conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int i = 1;
			while (rs.next()) {
				System.out.println(i);
				if (rs.isFirst()) {
					resultValue = rs.getString("levelcode");
				} else {
					resultValue += "," + rs.getString("levelcode");
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultValue;
	}

	@SuppressWarnings("unused")
	private String getSjid() throws Exception {
		String levelcodeString = "";
		FileWriter fw = new FileWriter("c:\\a.txt ");
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			conn = getConnection();
			String sql = "";
			sql = "select levelcode from wz_category order by wzlbbm";
			Statement st = conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			int count = 1;
			while (rs.next()) {
				String retValue ="";
				System.out.println(count);
				String levelcode = rs.getString("levelcode");
				if (levelcode == null || levelcode.equals("")) {
					// if(count == 1){
					// levelcodeString = "";
					// }else{
					// levelcodeString += ","+"";
					// }
					retValue = getParentByLevelCode("");
					bw.write(retValue+ "\n");
					System.out.println(retValue);
				} else {
					if (levelcode.split("#").length == 2) {
						// if(count == 1){
						// levelcodeString += levelcode.split("#")[1];
						// }else{
						// levelcodeString += ","+levelcode.split("#")[1];
						// }
						// System.out.println("AAAAA"+levelcode.split("#")[1]);
						retValue = getParentByLevelCode("NULL");
						bw.write(retValue + "\n");
						System.out.println(retValue);
					} else {
						// if(count == 1){
						// levelcodeString +=
						// levelcode.split("#")[levelcode.split("#").length-2];
						// }else{
						// levelcodeString +=
						// ","+levelcode.split("#")[levelcode.split("#").length-2];
						// }
						// System.out.println("BBBBB"+levelcode.split("#")[levelcode.split("#").length-2]);
						retValue = getParentByLevelCode(levelcode.split("#")[levelcode.split("#").length - 2]);
						bw.write(retValue + "\n");
						System.out.println(retValue);
					}
				}
				count++;
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				fw.close();
			}
			if (bw != null) {
				bw.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return levelcodeString;
	}

	public String getParentByLevelCode(String levelcode) throws Exception {
		String locationcode = "";
		// String[] levelcode = levelcodeString.split(",");
		try {
			// for (int i = 0; i < levelcode.length; i++) {
			conn = getConnection();
			String sql = "";
			if (levelcode.equals("NULL")) {
				sql = "select wzlbbm from wz_category where levelcode is null order by wzlbbm";
			} else {
				sql = "select wzlbbm from wz_category where wzlbbm = '"
						+ levelcode + "' order by wzlbbm";
			}
			Statement st = conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				locationcode = "200000000000000000000000"
						+ rs.getString("wzlbbm");
				// System.out.println(rs.getString("locationcode"));
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return locationcode;
	}

	int sum = 0;
	int n = 1;

	public void sum() {
		sum += n;
		n += 1;
		if (n <= 100) {
			sum();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataTest dataTest = new DataTest();
		// dataTest.sum();
		// System.out.println(dataTest.sum);
		try {
			System.out.println(dataTest.getSjid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String aaa = "#1223445#123455";
		// for (int i = 0; i < aaa.split("#").length; i++) {
		// System.out.println("--------->"+aaa.split("#")[i]);
		// }
	}

}
