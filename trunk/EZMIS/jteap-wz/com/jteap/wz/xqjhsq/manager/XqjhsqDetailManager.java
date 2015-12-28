/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.xqjhsq.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import javax.sql.DataSource;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.xqjhsq.model.*;

public class XqjhsqDetailManager extends HibernateEntityDao<XqjhsqDetail>{	
	/**
	 * 批量保存或者修改
	 * 
	 * @param objs
	 */
	public void saveOrUpdateAll(final Collection<?> objs) {
		this.getHibernateTemplate().saveOrUpdateAll(objs);
	}
	
	/**
	 * 描述 : 生成需求计划明细序号(2位) 作者 : caofei 时间 : Nov 4, 2010 参数 : 返回值 : String 异常 :
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public String getMaxXH() throws Exception {
		String retValue = "1";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(xh) from TB_WZ_XQJH_DETAIL";
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
