package com.jteap.jx.qxgl.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.util.CalculateFormula;
import com.jteap.form.eform.util.EFormExpContext;

/**
 * 缺陷申请-缺陷单编号创建时计算公式
 * @author wangyun
 *
 */
public class CalculateQxdbh implements CalculateFormula {

	public String calculate(EFormExpContext context) {
		String qxzy = (String) context.getVar("QXZY");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		String qxdbhTmp = qxzy.toUpperCase() + date;
		String retValue = "";
		
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select max(qxdbh) from TB_JX_QXGL_QXD where qxdbh like '"+qxdbhTmp+"%'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String qxdbhNow = rs.getString(1);
				if (StringUtil.isNotEmpty(qxdbhNow)) {
					qxdbhNow =  qxdbhNow.substring(10);
					Long lnQxdbh = Long.parseLong(qxdbhNow);
					lnQxdbh += 1;
					String qxdbh = lnQxdbh.toString();
					int zeroCount = qxdbhNow.length() - qxdbh.length();
					for (int i = 0; i < zeroCount; i++) {
						qxdbh = "0" + qxdbh;
					}
					retValue = qxdbhTmp + qxdbh;
					return retValue;
				}
			}
			retValue = qxdbhTmp + "0001";
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
