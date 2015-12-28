package com.jteap.wz.xqjh.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.xqjh.manager.XqjhJDBCManager;
import com.jteap.wz.xqjh.manager.XqjhManager;

/**
 * 描述 : 需求计划Action 作者 : caofei 时间 : Oct 21, 2010 参数 : 返回值 : 异常 :
 */
@SuppressWarnings("serial")
public class XqjhAction extends AbstractAction {

	private XqjhManager xqjhManager;
	private XqjhJDBCManager xqjhJDBCManager;

	public XqjhJDBCManager getXqjhJDBCManager() {
		return xqjhJDBCManager;
	}

	public void setXqjhJDBCManager(XqjhJDBCManager xqjhJDBCManager) {
		this.xqjhJDBCManager = xqjhJDBCManager;
	}

	public XqjhManager getXqjhManager() {
		return xqjhManager;
	}

	public void setXqjhManager(XqjhManager xqjhManager) {
		this.xqjhManager = xqjhManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return xqjhManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "ID", "XQJHSQBH", "SXSJ", "GCLB", "GCXM", "SQBM",
				"SQSJ", "STATUS", "OPERATOR", "TIME", "XQJHBH" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "ID", "XQJHSQBH", "SXSJ", "GCLB", "GCXM", "SQBM",
				"SQSJ", "STATUS", "OPERATOR", "TIME", "XQJHBH" };
	}

	/**
	 * 描述 : 数量分配(需求计划内容) 作者 : caofei 时间 : Oct 29, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings("unused")
	public String showXqjhListAction() throws Exception {
		String xqjhId = request.getParameter("sqjhid");
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "SELECT a.id, a.xqjhbh, a.xqjhsqbh, a.sxsj, a.status, a.operator, b.personname, a.gclb, a.gcxm, a.sqbm, a.sqsj " +
					"FROM tb_wz_xqjh a, tb_sys_person b " +
					"WHERE a.operator = b.login_name and (a.id = '"+xqjhId+"')";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			String xqjhid = "";
			String xqjhbh = "";
			String xqjhsqbh = "";
			Date sxsj = null;
			String status = "";
			String operator = "";
			String gclb = "";
			String gcxm = "";
			String sqbm = "";
			Date sqsj = null;
			String personName = "";

			while (rs.next()) {
				xqjhid = rs.getString("id");
				xqjhbh = rs.getString("xqjhbh");
				xqjhsqbh = rs.getString("xqjhsqbh");
				sxsj = rs.getDate("sxsj");
				status = rs.getString("status");
				operator = rs.getString("operator");
				gclb = rs.getString("gclb");
				gcxm = rs.getString("gcxm");
				sqbm = rs.getString("sqbm");
				sqsj = rs.getDate("sqsj");
				personName = rs.getString("personname");
			}
			this.outputJson("{success:true,xqjhid:'"+xqjhid+"',xqjhbh:'" + xqjhbh + "',xqjhsqbh:'"
					+ xqjhsqbh + "',sxsj:'" + sxsj + "',status:'" + status
					+ "',operator:'" + operator + "'" + ",gclb:'" + gclb
					+ "',gcxm:'" + gcxm + "',sqbm:'" + sqbm + "',sqsj:'" + sqsj + "',personName:'" + personName
					+ "'}");
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}
	
}
