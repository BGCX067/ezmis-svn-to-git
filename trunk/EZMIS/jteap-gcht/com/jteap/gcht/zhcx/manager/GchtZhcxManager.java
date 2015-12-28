package com.jteap.gcht.zhcx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 综合查询Manager
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unused", "unchecked"})
public class GchtZhcxManager extends JdbcManager {
	
	/**
	 * 
	 * 描述 : 获得物资执行情况统计
	 * 作者 : wangyun
	 * 时间 : 2010-12-9
	 * 参数 : 
	 * 		tjnf ： 统计年份
	 * 返回值 : 
	 * 异常 : Exception
	 * 
	 */
	public String getZxntj(String tjnf, String tableName) throws Exception {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer sb = new StringBuffer();

			String sql = "select count(*),sum(HTJE) from "+tableName+" where cjsj = '"+ tjnf+"'||'年'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String htzfs = "0";
			double htzje = 0;
			while (rs.next()) {
				// 合同总份数
				htzfs = rs.getString(1);
				// 合同总金额
				htzje = rs.getDouble(2);
				sb.append("'htzfs':'");
				sb.append(htzfs);
				sb.append("','htzje':'");
				sb.append(htzje);
				sb.append("',");
			}
			rs.close();

			sql = "select count(*) from "+tableName+" where status != '合同生效' "
					+"and status != '作废' and status != '已终结' and cjsj = '"+ tjnf+"'||'年'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			String zspht = "0";
			while (rs.next()) {
				// 在审批合同
				zspht = rs.getString(1);
				sb.append("'zspht':'");
				sb.append(zspht);
				sb.append("',");
			}
			rs.close();

			sql = "select count(*) from "+tableName+" where status = '合同生效' and cjsj = '"+ tjnf+"'||'年'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			String zzxht = "0";
			while (rs.next()) {
				// 在执行合同
				zzxht = rs.getString(1);
				sb.append("'zzxht':'");
				sb.append(zzxht);
				sb.append("',");
			}

			sql = "select count(*) from "+tableName+" where status = '已终结' and cjsj = '"+ tjnf+"'||'年'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			String zjht = "0";
			while (rs.next()) {
				// 终结合同
				zjht = rs.getString(1);
				sb.append("'zjht':'");
				sb.append(zjht);
				sb.append("',");
			}

			sql = "select sum(a.htje) from "+tableName+"fk"
				+" a, tb_ht_wzht b where a.htid = b.id and b.cjsj = '"+ tjnf+"'||'年'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			double zzfje = 0;
			while (rs.next()) {
				// 总支付金额
				zzfje = rs.getDouble(1);
				sb.append("'zzfje':'");
				sb.append(zzfje);
				sb.append("',");
			}

			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() -1);
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	/**
	 * 
	 * 描述 : 获得物资执行情况统计
	 * 作者 : wangyun
	 * 时间 : 2010-12-9
	 * 参数 : 
	 * 		tjnf ： 统计年份
	 * 返回值 : 
	 * 异常 : Exception
	 * 
	 */
	public String getRlZxntj(String tjnf) throws Exception {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			
			String sql = "select count(*) from tb_ht_rlht where cjsj = '"+ tjnf+"'||'年'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String htzfs = "0";
			while (rs.next()) {
				// 合同总份数
				htzfs = rs.getString(1);
			}
			rs.close();
			
			sql = "select count(*) from tb_ht_rlht where status != '合同生效' "
					+"and status != '作废' and status != '已终结' and cjsj = '"+ tjnf+"'||'年'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			String zspht = "0";
			while (rs.next()) {
				// 在审批合同
				zspht = rs.getString(1);
			}
			rs.close();

			sql = "select count(*) from tb_ht_rlht where status = '合同生效' and cjsj = '"+ tjnf+"'||'年'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			String zzxht = "0";
			while (rs.next()) {
				// 在执行合同
				zzxht = rs.getString(1);
			}

			sql = "select count(*) from tb_ht_rlht where status = '已终结' and cjsj = '"+ tjnf+"'||'年'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			String zjht = "0";
			while (rs.next()) {
				// 终结合同
				zjht = rs.getString(1);
			}

			StringBuffer sb = new StringBuffer();
			sb.append("'htzfs':'");
			sb.append(htzfs);
			sb.append("','zspht':'");
			sb.append(zspht);
			sb.append("','zzxht':'");
			sb.append(zzxht);
			sb.append("','zjht':'");
			sb.append(zjht);
			sb.append("'");
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

}
