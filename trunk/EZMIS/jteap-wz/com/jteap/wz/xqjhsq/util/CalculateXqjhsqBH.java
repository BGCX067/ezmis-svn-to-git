package com.jteap.wz.xqjhsq.util;

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
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;

/**
 * 生成需求计划申请编号
 * @author caofei
 *
 */
public class CalculateXqjhsqBH implements CalculateFormula {

	private XqjhsqManager xqjhsqManager;

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}
	
	
//	String hql = "select max(xqjhsqbh) from Xqjhsq";
//	Object maxBh = xqjhsqManager.findUniqueByHql(hql);
//	if(maxBh== null){
//		maxBh = "00000001";
//	}else{
//		String bh = (String) maxBh;
//		int max = Integer.parseInt(bh) + 1;
//		NumberFormat nFormat=NumberFormat.getNumberInstance();
//		nFormat.setMinimumIntegerDigits(8);
//		maxBh = nFormat.format(max);
//	}
//	return defaultBH;

	public String calculate(EFormExpContext context) {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(xqjhsqbh) from TB_WZ_XQJHSQ where xqjhqf = '1'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String maxBH = rs.getString(1);
				if (StringUtil.isNotEmpty(maxBH)) {
					int max = Integer.parseInt(maxBH) + 1;
					NumberFormat nFormat=NumberFormat.getNumberInstance();
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
