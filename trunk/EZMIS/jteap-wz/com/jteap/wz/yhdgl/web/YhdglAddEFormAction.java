package com.jteap.wz.yhdgl.web;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.dom4j.Element;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.eform.web.EFormAction;
import com.jteap.wz.cgjhgl.manager.CgjhglManager;
import com.jteap.wz.cgjhgl.model.Cgjhgl;
import com.jteap.wz.cgjhmx.manager.CgjhmxManager;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdgl.model.Yhdgl;
import com.jteap.wz.yhdmx.manager.YhdmxManager;
import com.jteap.wz.yhdmx.model.Yhdmx;
/**
 * 自由入库相关处理类
 * @author lc
 *
 */
@SuppressWarnings("serial")
public class YhdglAddEFormAction extends EFormAction{
	private YhdmxManager yhdmxManager;
	private CgjhmxManager cgjhmxManager;
	private CgjhglManager cgjhglManager;
	private YhdglManager yhdglManager;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void eformBeforeSave(Connection conn, String docid,
			Map<String, Element> diMap, Map<String, Object> dataMap,
			boolean isNew) throws Exception {
		String sql = "UPDATE TB_WZ_YCGJHMX SET DHSL = DHSL+%1%,ZT='3' WHERE ID=%2%";
		String sql1 = "UPDATE TB_WZ_XQJH_DETAIL SET DHSL = DHSL+%1% WHERE ID=(SELECT XQJHMX FROM TB_WZ_YCGJHMX WHERE ID=%2%)";
		
		Map<String,List<Map>> subList = new HashMap<String,List<Map>>();
		Statement pst = null;
		Statement pst1 = null;
		try{
			for(String fd:dataMap.keySet()){
				Object obj = dataMap.get(fd);
				if(obj instanceof List){
					subList.put(fd, (List<Map>) obj);
					continue;	//过滤掉子表数据
				} 
			}
			
			if(subList.size()>0){
				for(String id : subList.keySet()){
					List sub = subList.get(id);
					for (int i = 0; i < sub.size(); i++) {
						Map subFd = (Map)sub.get(i);
						pst = conn.createStatement();
						pst1 = conn.createStatement();
						pst.execute(sql.replace("%1%", subFd.get("YSSL").toString())
										.replace("%2%", "'"+(String)subFd.get("CGDMX")+"'"));
						pst1.execute(sql1.replace("%1%", subFd.get("YSSL").toString())
										.replace("%2%", "'"+(String)subFd.get("CGDMX")+"'"));
					}
				}
			}
		}finally{
			if(pst!=null){
				pst.close();
			}
			if(pst1!=null){
				pst1.close();
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onAferSave() throws Exception{
		String yhdbh = yhdglManager.getMaxBH();
		Yhdgl yhdgl =yhdglManager.findYhdglByBh(yhdbh);
		if(yhdgl!=null){
			Cgjhgl cgjh  = cgjhglManager.saveCgjhByYhd(yhdgl);
			List<Yhdmx> yhdmxList = (List<Yhdmx>) yhdmxManager.findYhdmxByYhdId(yhdgl.getId());
			cgjhmxManager.saveCgjhMxByYhdmx(yhdmxList, cgjh);
		}else{
			yhdgl.getId();
		} 
	}
	
	//根据验货单ID 返回验货单工程类别 工程项目 班组信息
	public String getYhdglByIdAction() throws Exception{
		String id = request.getParameter("id");
		Yhdgl yhdgl =yhdglManager.get(id);
		if(yhdgl!=null){
			String gcxm = " ";
			String gclb = " ";
			String bz = " ";
			if(yhdgl.getGcxm()!=null){
				gcxm = yhdgl.getGcxm();
			}
			if(yhdgl.getGclb()!=null){
				gclb= yhdgl.getGclb();
			}
			if(yhdgl.getBz()!=null){
				bz = yhdgl.getBz();
			}
			this.outputJson("{success:true,gcxm:'"+gcxm+"',gclb:'"+gclb+"',bz:'"+bz+"'}");
		}else{
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	/**
	 * 根据验货单ID 删除自由入库所虚构的采购单及采购明细
	 * @return
	 * @throws Exception 
	 */
	public String delZyrkAction() throws Exception{
		Connection conn = null;
		Statement st = null;
		try{
			DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			st = conn.createStatement();
			String id  = request.getParameter("id");
			Yhdgl yhdgl = yhdglManager.get(id);
			Set<Yhdmx> yhdmxSet = yhdgl.getYhdmxs();
			Iterator<Yhdmx> it = yhdmxSet.iterator();
			String cgjhId = "";
			//Cgjhgl cgjh = null;
			while(it.hasNext()){
				Yhdmx yhdmx = it.next();
				st.execute("delete tb_wz_ycgjhmx t where t.id='"+yhdmx.getCgjhmx().getId()+"'");
				st.execute("delete tb_wz_yyhdmx t where t.id='"+yhdmx.getId()+"'");
				cgjhId = yhdmx.getCgjhmx().getCgjhgl().getId();
			}
			if(cgjhId!=""){
				st.execute("delete tb_wz_ycgjh t where t.id='"+cgjhId+"'");
			}
			st.execute("delete tb_wz_yyhd t where t.id='"+id+"'");
			this.outputJson("{success:true}");
		}catch (Exception e) {
			e.printStackTrace();
			this.outputJson("{success:false}");
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return NONE;
	}
	
	public YhdmxManager getYhdmxManager() {
		return yhdmxManager;
	}

	public void setYhdmxManager(YhdmxManager yhdmxManager) {
		this.yhdmxManager = yhdmxManager;
	}

	public CgjhmxManager getCgjhmxManager() {
		return cgjhmxManager;
	}

	public void setCgjhmxManager(CgjhmxManager cgjhmxManager) {
		this.cgjhmxManager = cgjhmxManager;
	}

	public CgjhglManager getCgjhglManager() {
		return cgjhglManager;
	}

	public void setCgjhglManager(CgjhglManager cgjhglManager) {
		this.cgjhglManager = cgjhglManager;
	}

	public YhdglManager getYhdglManager() {
		return yhdglManager;
	}

	public void setYhdglManager(YhdglManager yhdglManager) {
		this.yhdglManager = yhdglManager;
	}
	
}
