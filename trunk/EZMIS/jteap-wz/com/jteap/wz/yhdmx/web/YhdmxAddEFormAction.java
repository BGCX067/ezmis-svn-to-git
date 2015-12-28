package com.jteap.wz.yhdmx.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.web.EFormAction;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
/**
 * 处理验货入库 分批次填发票编号 生效
 * @author lvchao
 *
 */

@SuppressWarnings( { "unchecked", "serial" })
public class YhdmxAddEFormAction extends EFormAction{
	private YhdglManager yhdglManager;
	private YhdmxManager yhdmxManager;
	
	
	public YhdglManager getYhdglManager() {
		return yhdglManager;
	}


	public void setYhdglManager(YhdglManager yhdglManager) {
		this.yhdglManager = yhdglManager;
	}


	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}


	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}


	@Override
	protected void onAferSave() throws Exception {
		String yhdBh = request.getParameter("BH");
		DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Statement mxst = null;
		ResultSet mxrs = null;
		String keys ="ghdw,sl,wzbm,yhdbh,tssl,cgjldw,jhdj,hsxs,cgdmx,zt,rksj,bl_zt,jl_zt,remark";
		try{
			conn = dataSource.getConnection();
			String sql = "select t.dhsl,t.id,t.yssl from tb_wz_yyhdmx t where t.yhdbh in (select d.id from tb_wz_yyhd d where d.bh = '"+yhdBh+"')";
			st = conn.createStatement();
			mxst = conn.createStatement();
			mxrs = mxst.executeQuery(sql);
			while(mxrs.next()){
				double dhsl = mxrs.getDouble(1);
				double yssl = mxrs.getDouble(3);
				if(yssl<dhsl){
					sql = "update tb_wz_yyhdmx set dhsl = yssl where id = '"+mxrs.getString("id")+"'";
					st.addBatch(sql);
					sql = "insert into tb_wz_yyhdmx ("+keys+",id,xh,dhsl,yssl,sqdj,zf,fpbh) " +
						  "select "+keys+",'"+UUIDGenerator.hibernateUUID()+"' as id,1 as xh,"+(dhsl-yssl)+" as dhsl,"+(dhsl-yssl)+" as yssl,0 as sqdj,0 as zf,'' as fpbh from tb_wz_yyhdmx where id = '"+mxrs.getString("id")+"'";
					//System.out.println(sql);
					st.addBatch(sql);
					st.executeBatch();
				}
			}
			//重新排列序号
			sql = "select * from tb_wz_yyhdmx t where t.yhdbh = (select d.id from tb_wz_yyhd d where d.bh = '"+yhdBh+"')";
			rs = st.executeQuery(sql);
			int i =1;
			while(rs.next()){	
				 st.addBatch("update tb_wz_yyhdmx set xh = '"+i+"' where id ='"+rs.getString("id")+"'");
				 i++;
			}
			st.executeBatch();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(st!=null){
				st.close();
			}
			if(mxst!=null){
				mxst.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
//		String yhdBh = request.getParameter("BH");
//		yhdglManager.saveNewYhdmx(yhdBh);
		
	}
	
}
