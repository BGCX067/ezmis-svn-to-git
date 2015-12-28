package com.jteap.wz.xqjhsq.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;

import com.jteap.core.dao.support.Page;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;

public class XqjhsqJDBCManager extends JdbcManager{

	Connection conn = null;
	DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Collection<XqjhsqDetail> findXqjhsqDetailById(String xqjhsqId){
		XqjhsqDetail xqjhsqDetail = null;
		Collection collection = new HashSet<XqjhsqDetail>();
		List<XqjhsqDetail> list = new ArrayList<XqjhsqDetail>();
		try {
			conn = dataSource.getConnection();
			String sql = "select * from tb_wz_xqjhsq_detail g where g.xqjhsqid = '"+xqjhsqId+"'";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				xqjhsqDetail= new XqjhsqDetail();
//				xqjhsqDetail.setXqjhsq(xqjhsq)
				xqjhsqDetail.setId(rs.getString("id"));
				xqjhsqDetail.setWzbm(rs.getString("wzbm"));
				xqjhsqDetail.setXh(rs.getLong("xh"));
				xqjhsqDetail.setWzmc(rs.getString("wzmc"));
				xqjhsqDetail.setXhgg(rs.getString("xhgg"));
				xqjhsqDetail.setSqsl(rs.getDouble("sqsl"));
				xqjhsqDetail.setJldw(rs.getString("jldw"));
				xqjhsqDetail.setGjdj(rs.getDouble("gjdj"));
				xqjhsqDetail.setProvider(rs.getString("provider"));
				xqjhsqDetail.setXysj(rs.getDate("xysj"));
				xqjhsqDetail.setDone(rs.getString("done"));
				xqjhsqDetail.setIsnew(rs.getString("isnew"));
				xqjhsqDetail.setJhy(rs.getString("jhy"));
				xqjhsqDetail.setSfdh(rs.getString("sfdh"));
				xqjhsqDetail.setIsCancel(rs.getString("is_cancel"));
				xqjhsqDetail.setCflag(rs.getString("c_flag"));
				collection.add(xqjhsqDetail);
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
		return collection;
	}
	
	
	@SuppressWarnings("unchecked")
	public Xqjhsq findXqjhsqByParentId(String parentId){
		Xqjhsq xqjhsq = null;
		try {
			conn = dataSource.getConnection();
			String sql= "select * from tb_wz_xqjhsq g where ";
			if(StringUtil.isEmpty(parentId)){
				sql += "g.id is null";
			}else{
				sql += "g.id = '"+parentId+"'";
			}
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery(sql);
			while(rs.next()){
				xqjhsq = new Xqjhsq();
				for (Iterator iterator = findXqjhsqDetailById(parentId).iterator(); iterator
						.hasNext();) {
					XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) iterator.next();
					Set<XqjhsqDetail> set = new HashSet<XqjhsqDetail>();
					set.add(xqjhsqDetail);
					xqjhsq.setXqjhsqDetail(set);
				}
				xqjhsq.setGclb(rs.getString("gclb"));
				xqjhsq.setGcxm(rs.getString("gcxm"));
				xqjhsq.setSqbm(rs.getString("sqsj"));
				xqjhsq.setSqsj(rs.getDate("sqsj"));
				xqjhsq.setCzy(rs.getString("czy"));
				xqjhsq.setStatus(rs.getString("status"));
				xqjhsq.setClcbh(rs.getString("c_lcbh"));
				xqjhsq.setGcxmbh(rs.getString("gcxmbh"));
				xqjhsq.setCflag(rs.getString("c_flag"));
				xqjhsq.setFpsj(rs.getDate("fpsj"));
				xqjhsq.setXqjhsqbh(rs.getString("xqjhsqbh"));
				xqjhsq.setCzyxm(rs.getString("czyxm"));
				xqjhsq.setSqbmmc(rs.getString("sqbmmc"));
				xqjhsq.setFlowStatus(rs.getString("flow_status"));
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
		return xqjhsq;
	}
	
	
	public boolean saveOrUpdateObject(Xqjhsq xqjhsq){
		boolean status = false;
		try {
			conn = dataSource.getConnection();
			String sql = "update tb_wz_xqjhsq set fpsj = "+xqjhsq.getFpsj()+" ,status = '"+xqjhsq.getStatus()+"'";
			PreparedStatement pst = conn.prepareStatement(sql);
			int result = pst.executeUpdate();
			if(result > 0){
				status = true;
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
		return status;
	}
	
	/**
	 * 获取补料计划申请编号
	 */
	public String getBljhsqBHMax(int xqjhqf) {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(xqjhsqbh) from TB_WZ_XQJHSQ where xqjhqf = '"+xqjhqf+"'";
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
	
	
	/**
	 * 分页查询指定sql数据
	 * 
	 * @param sql
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @throws SQLException
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQueryTableData2(String sql, int start, int limit)
			throws Exception {
		System.out.println(start+"---"+limit);
		String countSql = " select count (*) from ("+sql+")";
		//System.out.println(countSql);
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(countSql);
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();

			if (count < 1)
				return new Page();

			sql = "SELECT * FROM(SELECT A.*, ROWNUM RN FROM ("+sql+") A )WHERE RN < " + (start + limit)+ " AND RN >= " + (start);
			System.out.println("---"+sql);
			rs = st.executeQuery(sql);
			List list = new ArrayList();
			ResultSetMetaData rsmeta = rs.getMetaData();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= rsmeta.getColumnCount(); i++) {
					Object obj = rs.getObject(i);
					// 针对oracle timestamp日期单独处理，转换成date
					if (obj instanceof oracle.sql.TIMESTAMP) {
						obj = ((oracle.sql.TIMESTAMP) obj).dateValue()
								.getTime();
					}

					map.put(rsmeta.getColumnName(i), obj);
				}
				list.add(map);
			}
			rs.close();
			return new Page(start, count, limit, list);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

	}
}
