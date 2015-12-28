package com.jteap.wz.xqjh.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Collection;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.xqjh.model.XqjhDetail;

/**
 * 描述 : 需求计划明细Manager 作者 : caofei 时间 : Oct 21, 2010 参数 : 返回值 : 异常 :
 */
public class XqjhDetailManager extends HibernateEntityDao<XqjhDetail> {

	/**
	 * 
	 * 描述 : 批量保存或者修改 作者 : caofei 时间 : Oct 25, 2010 参数 : objs 返回值 : void 异常 :
	 */
	public void saveOrUpdateAll(final Collection<?> objs) {
		this.getHibernateTemplate().saveOrUpdateAll(objs);
	}

	/**
	 * 描述 : 生成采购计划编号(8位) 作者 : caofei 时间 : Nov 4, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public String getMaxNum() throws Exception {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(bh) from TB_WZ_YCGJH";
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
	
	/**
	 * 描述 : 生成采购计划明细序号(2位) 作者 : caofei 时间 : Nov 4, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public String getMaxXH(String cgjhbh) throws Exception {
		String retValue = "1";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(xh) from TB_WZ_YCGJHMX where CGJHBH = '"+cgjhbh+"'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String maxBH = rs.getString(1);
				if (StringUtil.isNotEmpty(maxBH)) {
					int max = Integer.parseInt(maxBH) + 1;
					retValue = String.valueOf(max);
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
