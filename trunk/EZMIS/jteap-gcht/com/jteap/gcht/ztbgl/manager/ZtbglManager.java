package com.jteap.gcht.ztbgl.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 招投标管理Manager
 * 
 * @author wangyun
 *
 */
public class ZtbglManager extends JdbcManager {

	/**
	 * 
	 * 描述 : 获得招标信息
	 * 作者 : wangyun
	 * 时间 : 2010-11-17
	 * 参数 : 
	 * 		sqlWhere : 查询条件
	 * 返回值 : 
	 * 		list : 记录集合
	 * 异常 : Exception
	 */
	public List<Map<String, Object>> findZbxxList(String sqlWhere, String orderSql) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String schema = SystemConfig.getProperty("jdbc.schema", "EZMIS");
		String sql = "SELECT ID, ZBBH, XMMC, ZBPC, ZBFS, to_char(JHZBSJ,'yyyy-MM-dd') as JHZBSJ, to_char(SJZBSJ,'yyyy-MM-dd') as SJZBSJ, ZBDZ, ZBNR, ZBFL, ZBFLID FROM " + schema
				+ ".TB_HT_ZBXX obj where obj.zbzt='0' ";

		if (StringUtil.isNotEmpty(sqlWhere)) {
			sql += " and " + sqlWhere;
		}

		if (StringUtil.isNotEmpty(orderSql)) {
			sql += orderSql;
		}

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getString("ID"));
				map.put("zbbh", rs.getString("ZBBH"));
				map.put("xmmc", rs.getString("XMMC"));
				map.put("zbpc", rs.getString("ZBPC"));
				map.put("zbfs", rs.getString("ZBFS"));
				map.put("jhzbsj", rs.getString("JHZBSJ"));
				map.put("sjzbsj", rs.getString("SJZBSJ"));
				map.put("zbdz", rs.getString("ZBDZ"));
				map.put("zbnr", rs.getString("ZBNR"));
				map.put("zbfl", rs.getString("ZBFL"));
				map.put("zbflid", rs.getString("ZBFLID"));

				list.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 
	 * 描述 : 获得中标信息
	 * 作者 : wangyun
	 * 时间 : 2010-11-18
	 * 参数 : 
	 * 		sqlWhere : 查询条件
	 * 返回值 : 
	 * 		list : 记录集合
	 * 异常 : Exception
	 */
	public List<Map<String, Object>> findZhbxxList(String sqlWhere, String orderSql) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String schema = SystemConfig.getProperty("jdbc.schema", "EZMIS");
		String sql = "SELECT ID, ZBBH, XMMC, ZBPC, ZBFS, to_char(JHZBSJ,'yyyy-MM-dd') as JHZBSJ, to_char(SJZBSJ,'yyyy-MM-dd') as SJZBSJ, ZBDZ, ZBNR, ZBFL, ZBFLID, ZBDW FROM " + schema
				+ ".TB_HT_ZBXX obj where obj.zbzt='1' ";

		if (StringUtil.isNotEmpty(sqlWhere)) {
			sql += " and " + sqlWhere;
		}

		if (StringUtil.isNotEmpty(orderSql)) {
			sql += orderSql;
		}

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getString("ID"));
				map.put("zbbh", rs.getString("ZBBH"));
				map.put("xmmc", rs.getString("XMMC"));
				map.put("zbpc", rs.getString("ZBPC"));
				map.put("zbfs", rs.getString("ZBFS"));
				map.put("jhzbsj", rs.getString("JHZBSJ"));
				map.put("sjzbsj", rs.getString("SJZBSJ"));
				map.put("zbdz", rs.getString("ZBDZ"));
				map.put("zbnr", rs.getString("ZBNR"));
				map.put("zbfl", rs.getString("ZBFL"));
				map.put("zbflid", rs.getString("ZBFLID"));
				map.put("zbdw", rs.getString("ZBDW"));

				list.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
