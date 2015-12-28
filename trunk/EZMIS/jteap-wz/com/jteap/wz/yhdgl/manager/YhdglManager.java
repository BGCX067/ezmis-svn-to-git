/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.yhdgl.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Collection;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.yhdgl.model.Yhdgl;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;

@SuppressWarnings("unchecked")
public class YhdglManager extends HibernateEntityDao<Yhdgl>{
	
	private YhdmxManager yhdmxManager; 
	private DataSource dataSource;
	
	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}
	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}
	public Collection<Yhdgl> findYhdglByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Yhdgl as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
//	//根据验货单编号 返回验货单对象
//	public Yhdgl findYhdglByBh(String bh){
//		
//		StringBuffer hql=new StringBuffer("from Yhdgl as g where ");
//		hql.append("g.bh=?");
//		List yhdglList = find(hql.toString(),bh);
//		if(yhdglList!=null&&yhdglList.size()>0){
//			return (Yhdgl) yhdglList.get(0);
//		}
//		return null;
//	}
	public void saveNewYhdmx(String yhdBh) {
		// TODO Auto-generated method stub
		Yhdgl yhdgl = this.findUniqueBy("bh",yhdBh);
		for(Yhdmx yhdmx:yhdgl.getYhdmxs()){
			if(yhdmx.getDhsl()>yhdmx.getYssl()){
				Yhdmx newYhdmx = new Yhdmx();
				newYhdmx.setId(UUIDGenerator.hibernateUUID());
				newYhdmx.setYhdgl(yhdgl);
				newYhdmx.setDhsl(yhdmx.getDhsl()-yhdmx.getYssl());
				newYhdmx.setYssl(yhdmx.getDhsl()-yhdmx.getYssl());
//				System.out.println(yhdgl.getYhdmxs().size());
				yhdgl.getYhdmxs().add(newYhdmx);
//				System.out.println(yhdgl.getYhdmxs().size());
				this.getHibernateTemplate().saveOrUpdate(yhdgl);
//				System.out.println("保存成功");
			}
		}
	}
	
	public Yhdgl findYhdglByBh(String bh) throws Exception {
		Yhdgl yhdgl = null;
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "select * from TB_WZ_YYHD y where y.bh = '"+bh+"'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				yhdgl = new Yhdgl();
				yhdgl.setId(rs.getString("id"));
				yhdgl.setBh(rs.getString("bh"));
				yhdgl.setCgy(rs.getString("cgy"));
				yhdgl.setDhrq(rs.getDate("dhrq"));
				yhdgl.setGhdw(rs.getString("ghdw"));
				yhdgl.setHtbh(rs.getString("htbh"));
				yhdgl.setYsrq(rs.getDate("ysrq"));
				yhdgl.setZt(rs.getString("zt"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return yhdgl;
	}
	/**
	 * 返回验货单编号最大
	 * @param context
	 * @return
	 */
	public String getMaxBH() {
//		String ckbm = (String) context.getVar("CKBM");
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");

		try {
			conn = dataSource.getConnection();
			String sql = "select max(BH) from tb_wz_yyhd";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if(StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getInstance();
					nformat.setMinimumIntegerDigits(8);
			 		int max = Integer.parseInt(ckbmMax);
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
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
}
