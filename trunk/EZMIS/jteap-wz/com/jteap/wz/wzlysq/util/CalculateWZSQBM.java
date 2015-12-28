package com.jteap.wz.wzlysq.util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.sql.DataSource;

import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.util.CalculateFormula;
import com.jteap.form.eform.util.EFormExpContext;

/**
 * 生成领用计划申请编号
 * @author caofei
 *
 */
public class CalculateWZSQBM implements CalculateFormula {


	public String calculate(EFormExpContext context) {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(BH) from TB_WZ_YLYSQ where lydqf = '1'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if(StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getInstance();
					nformat.setMinimumIntegerDigits(8);
			 		int max = Integer.parseInt(ckbmMax)+1;
			 		retValue = nformat.format(max).replaceAll(",", "");
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