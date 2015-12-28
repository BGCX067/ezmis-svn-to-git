package com.jteap.wz.gclbgl.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.util.CalculateFormula;
import com.jteap.form.eform.util.EFormExpContext;

public class CalculateProjcatBM implements CalculateFormula {


	public String calculate(EFormExpContext context) {
//		String ckbm = (String) context.getVar("CKBM");
		String retValue = "11";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(PROJCATCODE) from TB_WZ_PROJCAT";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if(StringUtil.isNotEmpty(ckbmMax)) {
			 		int max = Integer.parseInt(ckbmMax)+1;
				    String str_m = String.valueOf(max); 
				    String str ="10"; 
				    retValue =  str.substring(0, 2-str_m.length())+str_m;
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
		//创建默认库位
		//KwwhManager kwwhManager = (KwwhManager)SpringContextUtil.getBean("kwwhManager");
		//kwwhManager.addDefaultKW(retValue);
		return retValue;
	}

}
