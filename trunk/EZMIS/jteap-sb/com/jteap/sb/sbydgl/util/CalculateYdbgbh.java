package com.jteap.sb.sbydgl.util;

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
 * 异动报告-异动报告单编号创建时计算公式
 * @author caofei
 *
 */
public class CalculateYdbgbh implements CalculateFormula {

	public String calculate(EFormExpContext context) {
//		String qxzy = (String) context.getVar("QXZY");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		String ydbgbhTmp = "BG" + date;
		String retValue = "";
		
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select max(ydbh) from tb_sb_sbydgl_ydxx where ydbh like '"+ydbgbhTmp+"%'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String ydbgbhNow = rs.getString(1);
				if (StringUtil.isNotEmpty(ydbgbhNow)) {
					ydbgbhNow =  ydbgbhNow.trim().substring(10);
					Long lnYdbgbh = Long.parseLong(ydbgbhNow);
					lnYdbgbh += 1;
					String ydbgbh = lnYdbgbh.toString();
					int zeroCount = ydbgbhNow.length() - ydbgbh.length();
					for (int i = 0; i < zeroCount; i++) {
						ydbgbh = "0" + ydbgbh;
					}
					retValue = ydbgbhTmp + ydbgbh;
					return retValue;
				}
			}
			retValue = ydbgbhTmp + "01";
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
