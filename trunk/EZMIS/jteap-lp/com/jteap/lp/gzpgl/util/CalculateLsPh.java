package com.jteap.lp.gzpgl.util;

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
 *临时票票号计算(显示时计算)
 * 
 * @author wangyun
 *
 */
public class CalculateLsPh implements CalculateFormula {
	public String calculate(EFormExpContext context) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String docid = (String) context.getVar("docid");
		String date = sdf.format(new Date());
		String retValue = "";

		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select PH from TB_LP_LSP_LSGZP where id = '" + docid + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				retValue = rs.getString(1);
				return retValue;
			}
			rs.close();
			
			sql = "select max(PH) from TB_LP_LSP_LSGZP where PH like '临时" + date + "%'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String dqdyzNow = rs.getString(1);
				if (StringUtil.isNotEmpty(dqdyzNow)) {
					dqdyzNow = dqdyzNow.substring(14);
					Long lDqdyzbh = Long.parseLong(dqdyzNow);
					lDqdyzbh += 1;
					String dqdyzbh = lDqdyzbh.toString();
					int zeroCount = dqdyzNow.length() - dqdyzbh.length();
					for (int i = 0; i < zeroCount; i++) {
						dqdyzbh = "0" + dqdyzbh;
					}
					retValue = "临时" + date + "-" + dqdyzbh;
					return retValue;
				}
			}
			retValue = "临时" + date + "-0001";
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
