package com.jteap.wz.xqjh.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.sql.DataSource;

import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 描述 : 用于多表联合查询需要 作者 : caofei 时间 : Oct 27, 2010 参数 : 返回值 : 异常 :
 */
public class XqjhJDBCManager extends JdbcManager {

	/**
	 * 描述 : 生成需求计划编号(8位) 作者 : caofei 时间 : Nov 4, 2010 参数 : 返回值 : String 异常 :
	 */
	public String getMaxBH() throws Exception {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(xqjhbh) from TB_WZ_XQJH";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String maxBH = rs.getString(1);
				if (StringUtil.isNotEmpty(maxBH)) {
					int max = Integer.parseInt(maxBH) + 1;
					NumberFormat nFormat = NumberFormat.getNumberInstance();
					nFormat.setMinimumIntegerDigits(8);
					retValue = nFormat.format(max).replaceAll(",", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return retValue;
	}

}
