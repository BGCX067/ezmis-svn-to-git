/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.wzda.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.kwwh.model.Kw;
import com.jteap.wz.wzda.model.Wzda;
@SuppressWarnings("unchecked")
public class WzdaManager extends HibernateEntityDao<Wzda>{
	
	private DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	public Collection<Wzda> findWzdaByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from Wzda as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new Object[]{parentId};
		}
		return find(hql.toString(),args);
	}
	public void updateWzdalb(String ids,String newwzlb){
		String hql = "update Wzda w set wzlb.id=? where w.id in('"+ids+"')";
		this.getHibernateTemplate().bulkUpdate(hql, new String[]{newwzlb});
	}
	public void updateKW(String ids,String kw){
		String hql = "update Wzda w set kw.id=? where w.id in('"+ids+"')";
		this.getHibernateTemplate().bulkUpdate(hql, new String[]{kw});
	}
	public void updateTsfl(String ids,String tsfl){
		String hql = "update Wzda w set w.tsfl=? where w.id in('"+ids+"')";
		this.getHibernateTemplate().bulkUpdate(hql, new String[]{tsfl});
	}
	public void updateDqkc(String ids,double dqkc){
		String hql = "update Wzda w set w.dqkc=? where w.id in('"+ids+"')";
		this.getHibernateTemplate().bulkUpdate(hql, new Object[]{dqkc});
	}
	/**
	 * 返回有库存的物资id
	 * @return
	 */
	public String getYkcids(){
		String hql = "from Wzda d where d.dqkc>0";
		List<Wzda> list = this.find(hql);
		StringBuffer ids = new StringBuffer();
		for(Wzda wz:list){
			ids.append("-"+wz.getId()+"-,");
		}
		return ids.toString();
	}
	/**
	 * 批量修改库位
	 * 用于库位删除
	 * oldKwid：要删除的库位ID
	 * newKw:赋予新的库位
	 */
	public void updateKw(Kw oldKw,Kw newKw){
		List<Wzda> wzdaList = this.find("from Wzda where kw.id=?", oldKw.getId());

		for(Wzda wzda:wzdaList){
			wzda.setKw(newKw);
			this.save(wzda);
		}
//		oldKw.getWzda().removeAll(wzdaList);
//		kwwhManager.save(oldKw);
		this.getSession().flush();
	}
	/**
	 * 保存有库存的物资 月末库存
	 * @throws SQLException 
	 */
	public void saveYmkc() throws SQLException{
		Connection conn = null;
		Statement st = null;
		ResultSet res = null;
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		try {
			//根据当前库存大于0 ID 库存
			String sql ="SELECT W.id,W.DQKC FROM TB_WZ_SWZDA W WHERE W.DQKC>0";
//			System.out.println(sql);
			conn = dataSource.getConnection();
			st = conn.createStatement();
			res = st.executeQuery(sql);
			//存入月末库存表
			while(res.next()){
				st.addBatch("insert into tb_wz_ymkc values('"+res.getString(1)+"','sfjc',sysdate,"+res.getDouble(2)+")");
			}
			st.executeBatch();
//			System.out.println("添加完毕");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(st!=null){
				st.close();
			}
			if(conn!=null){
				conn.close();
			}
			
		}
	}
	/**
	 * 返回物资 在库里的时间 单位：月
	 * @param wzid
	 * @return
	 */
	public int getZksj(String wzid){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			conn =dataSource.getConnection();
			st = conn.createStatement();
			String sql = "select Months_between(sysdate, t.rksj) from tb_wz_yyhdmx t " +
					"where t.wzbm = '"+wzid+"' and t.zt = '1' and t.sysl>0 order by Months_between(sysdate, t.rksj) desc";
			rs = st.executeQuery(sql);
			if(rs.next()){
				return rs.getInt(1);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}
