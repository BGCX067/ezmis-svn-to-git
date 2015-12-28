package com.jteap.gcht.gysgl.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 供应商管理Manager
 * 
 * @author wangyun
 *
 */
public class GysglManager extends JdbcManager {

	private DataSource dataSource;

	/**
	 * 
	 * 描述 : 获得供应商信息
	 * 作者 : wangyun
	 * 时间 : 2010-11-17
	 * 参数 : 
	 * 		sqlWhere : 查询条件
	 * 返回值 : 
	 * 		list : 记录集合
	 * 异常 : Exception
	 */
	public List<Map<String, Object>> findGysxxList(String sqlWhere, String orderSql) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String schema = SystemConfig.getProperty("jdbc.schema", "EZMIS");
		String sql = "SELECT a.ID, a.GYSMC, a.FRDB, a.QYLX, a.CATALOG_ID, a.GYSLB, a.SWLXR, a.SWLXRDH, a.GYSDZ, a.KHYH, a.YHZH, a.YFFZR, a.YFFZRLXFS, a.YFSGFZR, a.YFSGFZRLXFS FROM " + schema
				+ ".TB_HT_GYSXX a";

		if (StringUtil.isNotEmpty(sqlWhere)) {
			sql += " where " + sqlWhere;
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
				map.put("gysmc", rs.getString("GYSMC"));
				map.put("frdb", rs.getString("FRDB"));
				map.put("qylx", rs.getString("QYLX"));
				map.put("catalogId", rs.getString("CATALOG_ID"));
				map.put("gyslb", rs.getString("GYSLB"));
				map.put("swlxr", rs.getString("SWLXR"));
				map.put("swlxrdh", rs.getString("SWLXRDH"));
				map.put("gysdz", rs.getString("GYSDZ"));
				map.put("khyh", rs.getString("KHYH"));
				map.put("yhzh", rs.getString("YHZH"));
				map.put("yffzr", rs.getString("YFFZR"));
				map.put("yffzrlxfs", rs.getString("YFFZRLXFS"));
				map.put("yfsgfzr", rs.getString("YFSGFZR"));
				map.put("yfsgfzrlxfs", rs.getString("YFSGFZRLXFS"));

				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 描述 : 根据ID获得供应商信息
	 * 作者 : wangyi
	 * 时间 : 2012-8-30
	 * 参数 : 
	 * 		sqlWhere : 查询条件
	 * 返回值 : 
	 * 		MAP : 记录集合
	 * 异常 : Exception
	 */
	public Map<String, Object> findGysxxById(String id) throws Exception {
		Map<String, Object> gysMap = new HashMap<String, Object>();

		String schema = SystemConfig.getProperty("jdbc.schema", "EZMIS");
		String sql = "SELECT * FROM TB_HT_GYSXX WHERE ID='"+id+"'";

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				gysMap.put("id", rs.getString("ID"));
				gysMap.put("gysmc", rs.getString("GYSMC"));
				gysMap.put("frdb", rs.getString("FRDB"));
				gysMap.put("qylx", rs.getString("QYLX"));
				gysMap.put("catalogId", rs.getString("CATALOG_ID"));
				gysMap.put("gyslb", rs.getString("GYSLB"));
				gysMap.put("swlxr", rs.getString("SWLXR"));
				gysMap.put("swlxrdh", rs.getString("SWLXRDH"));
				gysMap.put("gysdz", rs.getString("GYSDZ"));
				gysMap.put("khyh", rs.getString("KHYH"));
				gysMap.put("yhzh", rs.getString("YHZH"));
				gysMap.put("yffzr", rs.getString("YFFZR"));
				gysMap.put("yffzrlxfs", rs.getString("YFFZRLXFS"));
				gysMap.put("yfsgfzr", rs.getString("YFSGFZR"));
				gysMap.put("yfsgfzrlxfs", rs.getString("YFSGFZRLXFS"));
				gysMap.put("gysdz", rs.getString("GYSDZ"));
				gysMap.put("bz", rs.getString("BZ"));
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return gysMap;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
