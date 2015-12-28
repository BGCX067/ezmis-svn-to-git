package com.jteap.wz.lydgl.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.util.CalculateFormula;
import com.jteap.form.eform.util.EFormExpContext;

public class CalculateLYDSQBH implements CalculateFormula {


	public String calculate(EFormExpContext context) {
//		String ckbm = (String) context.getVar("CKBM");
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(lysqbh) from TB_WZ_YLYD";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String lysqbhMax = rs.getString(1);
				if(StringUtil.isNotEmpty(lysqbhMax)) {
			 		int max = Integer.parseInt(lysqbhMax)+1;
				    String str_m = String.valueOf(max); 
				    String str = retValue; 
				    retValue =  str.substring(0, 8-str_m.length())+str_m;
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
