/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.cgjhgl.manager;

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
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.yhdgl.model.Yhdgl;


@SuppressWarnings("unchecked")
public class CgjhglManager extends HibernateEntityDao<Cgjhgl>{
	public Collection<Cgjhgl> findCgjhglByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Cgjhgl as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	/**
	 * 根据验货单生成采购计划
	 */
	public Cgjhgl saveCgjhByYhd(Yhdgl yhd){
		//this.getSession().beginTransaction();
		Cgjhgl cgjh = new Cgjhgl();
		//cgjh.setId(UUIDGenerator.hibernateUUID());
		cgjh.setBh(getMaxCgjhBh());
		cgjh.setZdsj(yhd.getDhrq());
		cgjh.setSxsj(yhd.getDhrq());
		cgjh.setZt("1");
		cgjh.setBz("自由入库");
		cgjh.setJhy(yhd.getCgy());
		//this.getSession().getTransaction().commit();
		return cgjh;
	}
	/**
	 * 生成采购计划编号
	 * @return
	 */
	public String getMaxCgjhBh(){
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(BH) from TB_WZ_YCGJH";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if(StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getInstance();
					nformat.setMinimumIntegerDigits(8);
			 		int max = Integer.parseInt(ckbmMax)+1;
			 		retValue = nformat.format(max).replaceAll(",", "");
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
